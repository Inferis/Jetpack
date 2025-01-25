package snekker.jetpack.client.event;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.particle.ParticleTypes;
import snekker.jetpack.item.JetpackItem;
import snekker.jetpack.networking.SetJetpackFuelC2SPayload;

public class ClientEvents {
    public static void registerEvents() {
        ClientTickEvents.START_CLIENT_TICK.register(ClientEvents::onClientTick);
    }

    private static void onClientTick(MinecraftClient minecraftClient) {
        var player = minecraftClient.player;
        if (player != null) {
            if (player.isSpectator() || player.isInCreativeMode()) {
                // we don't want this happening when in creative or spectator
                return;
            }
            if (minecraftClient.world == null) {
                // just in case
                return;
            }

            var jetpackStack = JetpackItem.getEquippedJetpack(player);
            if (!player.getAbilities().flying) {
//                if (player.isLoaded() && !jetpackStack.isEmpty()) {
//                    JetpackItem.setActive(jetpackStack, false);
//                    JetpackUtil.setFlying(player, false);
//                }
                // not flying, so no particles needed
                return;
            }

            if (!jetpackStack.isEmpty() && JetpackItem.getActive(jetpackStack)) {
                // get vector that describes our rotation, then move it behind us to
                // add cloud particles.
                var vec = player.getRotationVector();
                var x = player.getX() - vec.getX();
                var y = player.getY() + 0.5;
                var z = player.getZ() - vec.getZ();
                minecraftClient.world.addParticle(ParticleTypes.CLOUD, x, y, z, 0, -0.05, 0);

                var fuel = JetpackItem.consumeFuel(jetpackStack);
                fuel.ifPresent(f -> ClientPlayNetworking.send(new SetJetpackFuelC2SPayload(f)));
            }
        }
    }
}
