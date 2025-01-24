package snekker.jetpack.client.mixin;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import snekker.jetpack.item.JetpackItem;
import snekker.jetpack.networking.SetJetpackActiveC2SPayload;
import snekker.jetpack.util.JetpackUtil;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(at=@At("TAIL"), method="setLoaded")
    public void setLoaded(boolean loaded, CallbackInfo info) {
        var playerEntity = (PlayerEntity)(Object)this;
        if (loaded && playerEntity instanceof ClientPlayerEntity clientPlayerEntity) {
            var jetpackStack = JetpackItem.getEquippedJetpack(clientPlayerEntity);
            if (!jetpackStack.isEmpty() && JetpackItem.getActive(jetpackStack) && !playerEntity.isOnGround()) {
                JetpackUtil.setFlying(clientPlayerEntity, true);
                if (ClientPlayNetworking.canSend(SetJetpackActiveC2SPayload.ID)) {
                    ClientPlayNetworking.send(new SetJetpackActiveC2SPayload(true));
                }
            }
        }
    }
}
