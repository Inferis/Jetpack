package snekker.jetpack.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import snekker.jetpack.item.JetpackItem;

public class JetpackUtil {
    public static ItemStack getEquippedJetpack(PlayerEntity player) {
        var chest = player.getInventory().armor.get(2);
        if (chest.getItem() instanceof JetpackItem) {
            return chest;
        }
        else {
            return ItemStack.EMPTY;
        }
    }
}
