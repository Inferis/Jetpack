package snekker.jetpack.client.mixin;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import snekker.jetpack.item.JetpackItem;
import snekker.jetpack.networking.SetJetpackActiveC2SPayload;
import snekker.jetpack.util.JetpackUtil;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(at = @At("RETURN"), method = "onEquipStack(Lnet/minecraft/entity/EquipmentSlot;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)V")
    public void onEquipStack(EquipmentSlot slot, ItemStack oldStack, ItemStack newStack, CallbackInfo info) {
        var livingEntity = (LivingEntity)(Object)this;
        if (livingEntity instanceof ClientPlayerEntity playerEntity) {
            if (oldStack.getItem() instanceof JetpackItem && slot.isArmorSlot()) {
                JetpackItem.setActive(oldStack, false);
                JetpackUtil.setFlying(playerEntity, false);

                if (ClientPlayNetworking.canSend(SetJetpackActiveC2SPayload.ID)) {
                    ClientPlayNetworking.send(new SetJetpackActiveC2SPayload(false));
                }
            }
        }
    }
}
