package snekker.jetpack.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import snekker.jetpack.item.JetpackItem;

public class JetpackUtil {
    public static void setFlying(PlayerEntity playerEntity, boolean flying) {
        playerEntity.getAbilities().flying = flying;
    }
}
