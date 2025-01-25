package snekker.jetpack.networking;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class JetpackNetworking {
    public static void registerPayloads() {
        registerSetJetpackActiveC2SPayload();
        registerSetDamageC2SPayload();
    }

    private static void registerSetJetpackActiveC2SPayload() {
        PayloadTypeRegistry.playC2S().register(SetJetpackActiveC2SPayload.ID, SetJetpackActiveC2SPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(SetJetpackActiveC2SPayload.ID, SetJetpackActiveC2SPayload::handle);
    }

    private static void registerSetDamageC2SPayload() {
        PayloadTypeRegistry.playC2S().register(SetJetpackFuelC2SPayload.ID, SetJetpackFuelC2SPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(SetJetpackFuelC2SPayload.ID, SetJetpackFuelC2SPayload::handle);
    }
}
