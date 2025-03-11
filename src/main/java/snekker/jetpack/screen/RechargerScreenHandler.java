package snekker.jetpack.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import snekker.jetpack.item.JetpackItems;

public class RechargerScreenHandler extends ScreenHandler {
    private final World world;
    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;

    public RechargerScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(2), new ArrayPropertyDelegate(4));
    }

    public RechargerScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(JetpackScreens.RECHARGER, syncId);

        this.propertyDelegate = propertyDelegate;
        this.world = playerInventory.player.getWorld();
        this.inventory = inventory;

        addSlot(new JetpackSlot(inventory, 0, 84, 25));
        addSlot(new FuelSlot(this, inventory, 1, 105, 53));
        addPlayerSlots(playerInventory, 8, 84);

        addProperties(propertyDelegate);
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
                    if (!insertItem(slotStack, 2, 38, false)) {
                        return ItemStack.EMPTY;
                    }
                    slot.markDirty();
                }
                else if (!jetpackSlot.hasStack()) {
                    // going from inventory to jetpack slot
                    if (!insertItem(slotStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }
            else if (slotIndex == 1) {
                // going from fuel slot to inventory
                if (!insertItem(slotStack, 2, 38, false)) {
                    return ItemStack.EMPTY;
                }
                slot.markDirty();
            }
            else if (isFuel(slot.getStack()) && !fuelSlot.hasStack()) {
                // going from inventory to fuel slot
                if (!insertItem(slotStack, 1, 2, false)) {
                    return ItemStack.EMPTY;
                }
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
        return getChargeProgress() > 0;
    }

    public float getChargeProgress() {
        return ((float)propertyDelegate.get(1) - propertyDelegate.get(0)) / propertyDelegate.get(1);
    }

    public Text getJetpackFuelStat() {
        return Text.literal("" + propertyDelegate.get(2)).withColor(0x373737)
                .append(Text.literal("/").withColor(0x0))
                .append(Text.literal("" + propertyDelegate.get(3)));
    }

    public Text getFuelStat() {
        return Text.literal("" + propertyDelegate.get(0)).withColor(0x0096ff)
                .append(Text.literal("/").withColor(0x0))
                .append(Text.literal("" + propertyDelegate.get(1)));
    }
}
