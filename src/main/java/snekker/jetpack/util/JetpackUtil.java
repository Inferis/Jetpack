package snekker.jetpack.util;

import net.minecraft.entity.player.PlayerEntity;

public class JetpackUtil {
    public static void setFlying(PlayerEntity playerEntity, boolean flying) {
        playerEntity.getAbilities().flying = flying;
        playerEntity.getAbilities().allowFlying = flying;
    }
}
