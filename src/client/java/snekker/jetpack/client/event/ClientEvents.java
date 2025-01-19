package snekker.jetpack.client.event;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.particle.EntityEffectParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import snekker.jetpack.Jetpack;
import snekker.jetpack.item.JetpackItem;

public class ClientEvents {
    public static void registerEvents() {
        ClientTickEvents.START_CLIENT_TICK.register(ClientEvents::onClientTick);
    }

    private static void onClientTick(MinecraftClient minecraftClient) {
        var player = minecraftClient.player;
        if (player != null) {
            if (player.isSpectator() || player.isInCreativeMode()) {
                return;
            }
            if (!player.getAbilities().flying) {
                return;
            }
            var jetpackStack = JetpackItem.getEquippedJetpack(player);
            if (!jetpackStack.isEmpty() && JetpackItem.getActive(jetpackStack)) {
                var vec = player.getRotationVector();
                var x = player.getX() - vec.getX();
                var y = player.getY() - 0.2;
                var z = player.getZ() - vec.getZ();
                minecraftClient.world.addParticle(ParticleTypes.CLOUD, x, y, z, 0, -0.05, 0);
            }
        }
    }
}
