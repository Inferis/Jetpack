package snekker.jetpack.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.world.World;
import snekker.jetpack.item.JetpackItems;

public class RechargerScreenHandler extends ScreenHandler {
    private final World world;
    private final Inventory inventory;

    public RechargerScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(2));
    }

    public RechargerScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(JetpackScreens.RECHARGER, syncId);

        this.world = playerInventory.player.getWorld();
        this.inventory = inventory;

        addSlot(new JetpackSlot(inventory, 0, 84, 25));
        addSlot(new FuelSlot(this, inventory, 1, 105, 53));
        addPlayerSlots(playerInventory, 8, 84);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slotIndex) {
        ItemStack itemStack = ItemStack.EMPTY;
        var slot = this.slots.get(slotIndex);
        var jetpackSlot = (JetpackSlot)slots.get(0);
        var fuelSlot = (FuelSlot)slots.get(1);
        if (slot.hasStack()) {
            var slotStack = slot.getStack();
            if (slot.getStack().isOf(JetpackItems.JETPACK)) {
                if (slotIndex == 0) {
                    // going from jetpack slot to inventory
                    insertItem(slotStack, 2, 38, false);
                    return ItemStack.EMPTY;
                }
                else if (!jetpackSlot.hasStack()) {
                    // going from inventory to jetpack slot
                    insertItem(slotStack, 0, 1, false);
                    return ItemStack.EMPTY;
                }
            }
            else if (slotIndex == 1) {
                // going from fuel slot to inventory
                insertItem(slotStack, 2, 38, false);
                return ItemStack.EMPTY;
            }
            else if (isFuel(slot.getStack()) && !fuelSlot.hasStack()) {
                // going from inventory to fuel slot
                insertItem(slotStack, 1, 2, false);
                return ItemStack.EMPTY;
            }
        }
        return itemStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return inventory.canPlayerUse(player);
    }

    public boolean isFuel(ItemStack item) {
        return world.getFuelRegistry().isFuel(item);
    }

    public boolean isCharging() {
        return true;
    }

    public float getChargeProgress() {
        return 0.5F;
    }
}
