package snekker.jetpack.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import snekker.jetpack.screen.RechargerScreenHandler;

public class RechargerBlockEntity extends BlockEntity implements @Nullable NamedScreenHandlerFactory {
    private final SimpleInventory inventory;

    public RechargerBlockEntity(BlockPos pos, BlockState state) {
        super(JetpackBlockEntityTypes.RECHARGER, pos, state);
        inventory = new SimpleInventory(2);
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("screen.jetpack.recharger.title");
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new RechargerScreenHandler(syncId, playerInventory, inventory);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.writeNbt(nbt, registries);

        NbtCompound rechargerNbt = new NbtCompound();
        Inventories.writeNbt(rechargerNbt, inventory.getHeldStacks(), registries);
        nbt.put("recharger_inventory", rechargerNbt);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.readNbt(nbt, registries);
        if (nbt.contains("recharger_inventory")) {
            NbtCompound rechargerNbt = nbt.getCompound("recharger_inventory");
            DefaultedList<ItemStack> inventory = DefaultedList.of();
            Inventories.readNbt(rechargerNbt, inventory, registries);
            this.inventory.getHeldStacks().clear();
            this.inventory.getHeldStacks().addAll(inventory);
        }
    }
}
