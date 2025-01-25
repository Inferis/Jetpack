package snekker.jetpack.networking;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import snekker.jetpack.Jetpack;
import snekker.jetpack.item.JetpackItem;

public record SetJetpackFuelC2SPayload(int fuel) implements CustomPayload {
    public static CustomPayload.Id<SetJetpackFuelC2SPayload> ID = new CustomPayload.Id<>(Jetpack.id("set_jetpack_damage_c2s_payload"));
    public static final PacketCodec<RegistryByteBuf, SetJetpackFuelC2SPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, SetJetpackFuelC2SPayload::fuel,
            SetJetpackFuelC2SPayload::new);

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }

    public void handle(ServerPlayNetworking.Context context) {
        context.server().execute(() -> {
            var jetpackStack = JetpackItem.getEquippedJetpack(context.player());
            if (!jetpackStack.isEmpty()) {
                jetpackStack.set(JetpackItem.JETPACK_FUEL, fuel());
            }
        });
    }
}
