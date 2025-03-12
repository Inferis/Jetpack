package snekker.jetpack.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryChangedListener;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
import snekker.jetpack.item.JetpackItems;
import snekker.jetpack.screen.RechargerScreenHandler;

import java.util.Random;

public class RechargerBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, InventoryChangedListener {
    public final SimpleInventory inventory;
    public final PropertyDelegate propertyDelegate;
    private int fuelLeft;
    private int fuelMax;
    static final Random random = new Random();

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

    public void setFuelSlotStack(ItemStack stack) {
        inventory.setStack(1, stack);
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
                var replaceWithBucket = fuelStack.getItem() instanceof BucketItem;
                // take a fuel
                fuelLeft = fuelMax = world.getFuelRegistry().getFuelTicks(fuelStack);
                if (fuelLeft > 0) {
                    fuelStack.decrement(1);
                    if (replaceWithBucket) {
                        setFuelSlotStack(new ItemStack(Items.BUCKET));
                    }
                }
            }
            else {
                fuelMax = 0;
            }
        }

        if (fuelLeft > 0) {
            var r = random.nextInt(3);
            fuel = Math.clamp(fuel + 3 + r, 0, JetpackItem.MAX_FUEL);
            jetpackStack.set(JetpackItem.JETPACK_FUEL, fuel);
            fuelLeft = Math.clamp(fuelLeft - 4 - r, 0, fuelLeft);
            markDirty();
        }
    }

    @Override
    public void onInventoryChanged(Inventory sender) {
        if (world != null) {
            var hasJetpack = sender.getStack(0).isOf(JetpackItems.JETPACK);
            world.setBlockState(pos, world.getBlockState(pos)
                    .with(RechargerBlock.HAS_JETPACK, hasJetpack));
        }
        markDirty();
    }
}
