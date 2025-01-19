package snekker.jetpack.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import snekker.jetpack.Jetpack;
import snekker.jetpack.item.JetpackItem;
import snekker.jetpack.item.JetpackItems;

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

    public static ItemStack getCarriedJetpack(PlayerEntity player) {
        var inventory = player.getInventory();
        for (var slot=0; slot<inventory.size(); ++slot) {
            var stack = inventory.getStack(slot);
            if (stack.getItem() instanceof JetpackItem) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }
}
