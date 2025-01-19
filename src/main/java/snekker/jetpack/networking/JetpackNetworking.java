package snekker.jetpack.networking;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import snekker.jetpack.item.JetpackItem;

public class JetpackNetworking {
    public static void registerPayloads() {
        registerSetJetpackActivePayload();
    }

    private static void registerSetJetpackActivePayload() {
        PayloadTypeRegistry.playC2S().register(SetJetpackActiveC2SPayload.ID, SetJetpackActiveC2SPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(SetJetpackActiveC2SPayload.ID, (payload, context) -> {
            context.server().execute(() -> {
                var jetpackStack = JetpackItem.getEquippedJetpack(context.player());
                if (!jetpackStack.isEmpty()) {
                    JetpackItem.setActive(jetpackStack, payload.active());
                    context.player().getAbilities().allowFlying = payload.active();
                    context.player().getAbilities().flying = payload.active();
                }
            });
        });
    }
}
