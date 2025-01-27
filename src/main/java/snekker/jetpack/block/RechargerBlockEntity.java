package snekker.jetpack.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryChangedListener;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import snekker.jetpack.item.JetpackItem;
import snekker.jetpack.screen.RechargerScreenHandler;

public class RechargerBlockEntity extends BlockEntity implements @Nullable NamedScreenHandlerFactory, InventoryChangedListener {
    public final SimpleInventory inventory;
    public final PropertyDelegate propertyDelegate;
    private int fuelLeft;
    private int fuelMax;

    public RechargerBlockEntity(BlockPos pos, BlockState state) {
        super(JetpackBlockEntityTypes.RECHARGER, pos, state);
        inventory = new SimpleInventory(2);
        inventory.addListener(this);
        fuelLeft = 0;
        fuelMax = 0;

        this.propertyDelegate = new PropertyDelegate() {
            public int get(int index) {
                switch (index) {
                    case 0: return RechargerBlockEntity.this.fuelLeft;
                    case 1: return RechargerBlockEntity.this.fuelMax;
                    case 2: {
                        var jetpackStack = getJetpackSlotStack();
                        return jetpackStack.getOrDefault(JetpackItem.JETPACK_FUEL, 0);
                    }
                    case 3: return JetpackItem.MAX_FUEL;
                    default: return 0;
                }
            }

            public void set(int index, int value) {
                switch (index) {
                    case 0: RechargerBlockEntity.this.fuelLeft = value; break;
                    case 1: RechargerBlockEntity.this.fuelMax = value; break;
                }
            }

            public int size() {
                return 4;
            }
        };
    }

    public ItemStack getJetpackSlotStack() {
        return inventory.getStack(0);
    }

    public ItemStack getFuelSlotStack() {
        return inventory.getStack(1);
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("screen.jetpack.recharger.title");
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new RechargerScreenHandler(syncId, playerInventory, inventory, propertyDelegate);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.writeNbt(nbt, registries);

        var stack = inventory.getStack(0);
        if (!stack.isEmpty()) {
            nbt.put("jetpack_stack", stack.toNbt(registries));
        }
        stack = inventory.getStack(1);
        if (!stack.isEmpty()) {
            nbt.put("fuel_stack", stack.toNbt(registries));
        }
        nbt.putInt("fuel_left", fuelLeft);
        nbt.putInt("fuel_max", fuelMax);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.readNbt(nbt, registries);
        if (nbt.contains("jetpack_stack")) {
            var stack = ItemStack.fromNbt(registries, nbt.get("jetpack_stack"));
            stack.ifPresent(s -> inventory.setStack(0, s));
        }
        if (nbt.contains("fuel_stack")) {
            var stack = ItemStack.fromNbt(registries, nbt.get("fuel_stack"));
            stack.ifPresent(s -> inventory.setStack(1, s));
        }
        fuelLeft = nbt.getInt("fuel_left");
        fuelMax = nbt.getInt("fuel_max");
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        var jetpackStack = getJetpackSlotStack();
        var fuelStack = getFuelSlotStack();
        var hasJetpack = !jetpackStack.isEmpty();
        var hasFuel = !fuelStack.isEmpty();
        var fuel = jetpackStack.getOrDefault(JetpackItem.JETPACK_FUEL, 0);
        var needsCharging = fuel < JetpackItem.MAX_FUEL;

        if (!hasJetpack || !needsCharging) {
            fuelLeft = fuelMax = 0;
            return;
        }

        if (fuelLeft == 0) {
            if (hasFuel) {
                // take a fuel
                fuelLeft = fuelMax = world.getFuelRegistry().getFuelTicks(fuelStack);
                fuelStack.decrement(1);
            }
            else {
                fuelMax = 0;
            }
        }

        if (fuelLeft > 0) {
            fuel += 1;
            jetpackStack.set(JetpackItem.JETPACK_FUEL, fuel);
            fuelLeft = Math.clamp(fuelLeft - 1, 0, fuelLeft);
            markDirty();
        }
    }

    @Override
    public void onInventoryChanged(Inventory sender) {
        markDirty();
    }
}
