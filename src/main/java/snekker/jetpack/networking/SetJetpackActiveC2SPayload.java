package snekker.jetpack.networking;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import snekker.jetpack.Jetpack;
import snekker.jetpack.item.JetpackItem;

public record SetJetpackActiveC2SPayload(boolean active) implements CustomPayload {
    public static Id<SetJetpackActiveC2SPayload> ID = new CustomPayload.Id<>(Jetpack.id("set_jetpack_active_c2s_payload"));
    public static final PacketCodec<RegistryByteBuf, SetJetpackActiveC2SPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.BOOLEAN, SetJetpackActiveC2SPayload::active,
            SetJetpackActiveC2SPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public void handle(ServerPlayNetworking.Context context) {
        context.server().execute(() -> {
            var jetpackStack = JetpackItem.getEquippedJetpack(context.player());
            if (!jetpackStack.isEmpty()) {
                JetpackItem.setActive(jetpackStack, active());
                context.player().getAbilities().allowFlying = active();
                context.player().getAbilities().flying = active();
            }
        });
    }
}
