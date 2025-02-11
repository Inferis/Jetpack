package snekker.jetpack.client.event;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.text.Text;
import snekker.jetpack.Jetpack;
import snekker.jetpack.client.JetpackClient;
import snekker.jetpack.item.JetpackItem;
import snekker.jetpack.networking.SetJetpackActiveC2SPayload;
import snekker.jetpack.networking.SetJetpackFuelC2SPayload;
import snekker.jetpack.util.JetpackUtil;

public class ClientEvents {
    public static void registerEvents() {
        ClientTickEvents.START_CLIENT_TICK.register(ClientEvents::onClientTick);
        HudRenderCallback.EVENT.register(ClientEvents::onHudRender);
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
            if (!jetpackStack.isEmpty() && JetpackItem.getActive(jetpackStack)) {
                var fuel = JetpackItem.consumeFuel(jetpackStack);
                fuel.ifPresent(f -> ClientPlayNetworking.send(new SetJetpackFuelC2SPayload(f)));

                if (!player.isOnGround()) {
                    if (fuel.isPresent() && fuel.get() == 0) {
                        JetpackItem.setActive(jetpackStack, false);
                        JetpackUtil.setFlying(player,false);
                        ClientPlayNetworking.send(new SetJetpackActiveC2SPayload(false));
                    }

                    // get vector that describes our rotation, then move it behind us to
                    // add cloud particles.
                    var vec = player.getRotationVector(0, player.getBodyYaw());
                    var x = player.getX() - vec.getX();
                    var y = player.getY() + 0.5;
                    var z = player.getZ() - vec.getZ();
                    minecraftClient.world.addParticle(ParticleTypes.CLOUD, x, y, z, 0, -0.05, 0);
                }
            }
        }
    }

    private static void onHudRender(DrawContext drawContext, RenderTickCounter renderTickCounter) {
        var matrices = drawContext.getMatrices();
        matrices.push();
        var client = MinecraftClient.getInstance();
        var player = client.player;
        if (player != null) {
            var jetpack = JetpackItem.getEquippedJetpack(player);
            var fuel = jetpack.getOrDefault(JetpackItem.JETPACK_FUEL, 0);
            drawContext.drawItem(jetpack, 8, drawContext.getScaledWindowHeight() / 2 - 8);
            if (JetpackItem.getActive(jetpack)) {
                matrices.scale(0.5F, 0.5F, 1);
                drawContext.drawText(client.textRenderer, Text.literal(fuel + "/" + JetpackItem.MAX_FUEL), 54, drawContext.getScaledWindowHeight() - client.textRenderer.fontHeight + 5, 0xffffff, true);
            }
        }
        matrices.pop();
    }
}
