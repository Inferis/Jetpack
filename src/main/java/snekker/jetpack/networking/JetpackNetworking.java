package snekker.jetpack.networking;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import snekker.jetpack.Jetpack;
import snekker.jetpack.item.JetpackItem;
import snekker.jetpack.util.JetpackUtil;

public class JetpackNetworking {
    public static void registerPayloads() {
        registerSetJetpackActivePayload();
    }

    private static void registerSetJetpackActivePayload() {
        PayloadTypeRegistry.playC2S().register(SetJetpackActivePayload.ID, SetJetpackActivePayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(SetJetpackActivePayload.ID, (payload, context) -> {
            context.server().execute(() -> {
                var jetpackStack = JetpackUtil.getEquippedJetpack(context.player());
                if (!jetpackStack.isEmpty()) {
                    Jetpack.LOGGER.info("active(server)=" + payload.active());
                    JetpackItem.setActive(jetpackStack, payload.active());
                    context.player().getAbilities().allowFlying = payload.active();
                    context.player().getAbilities().flying = payload.active();
                }
            });
        });
    }
}
