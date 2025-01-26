package snekker.jetpack.screen;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import snekker.jetpack.item.JetpackItems;

public class JetpackSlot extends Slot {
    public JetpackSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return stack.isOf(JetpackItems.JETPACK);
    }

    @Override
    public void onQuickTransfer(ItemStack newItem, ItemStack original) {
        super.onQuickTransfer(newItem, original);
    }

    @Override
    protected void onCrafted(ItemStack stack) {
        super.onCrafted(stack);
    }

    @Override
    protected void onCrafted(ItemStack stack, int amount) {
        super.onCrafted(stack, amount);
    }

    @Override
    protected void onTake(int amount) {
        super.onTake(amount);
    }
}
