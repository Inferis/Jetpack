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
}
