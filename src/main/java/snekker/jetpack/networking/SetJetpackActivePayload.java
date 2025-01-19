package snekker.jetpack.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import snekker.jetpack.Jetpack;

public record SetJetpackActivePayload(boolean active) implements CustomPayload {
    public static Id<SetJetpackActivePayload> ID = new CustomPayload.Id<>(Jetpack.id("set_jetpack_active"));
    public static final PacketCodec<RegistryByteBuf, SetJetpackActivePayload> CODEC = PacketCodec.tuple(
            PacketCodecs.BOOLEAN, SetJetpackActivePayload::active,
            SetJetpackActivePayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
