package snekker.jetpack.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;

public class RechargerScreenHandler extends ScreenHandler {
    protected RechargerScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(JetpackScreens.RECHARGER, syncId);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

}
