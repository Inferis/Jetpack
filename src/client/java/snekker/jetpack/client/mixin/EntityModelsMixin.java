package snekker.jetpack.client.mixin;

import com.google.common.collect.ImmutableMap;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModels;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import snekker.jetpack.client.model.JetpackEntityModel;
import snekker.jetpack.client.model.JetpackEntityModelLayer;

import java.util.Map;

@Mixin(EntityModels.class)
public class EntityModelsMixin {
    @Inject(at= @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/client/render/entity/model/SalmonEntityModel;getTexturedModelData()Lnet/minecraft/client/model/TexturedModelData;"), method="getModels")
    private static void getModels(CallbackInfoReturnable<Map<EntityModelLayer, TexturedModelData>> info, @Local ImmutableMap.Builder<EntityModelLayer, TexturedModelData> builder) {
        var layer = JetpackEntityModelLayer.JETPACK;
        builder.put(layer, JetpackEntityModel.getTexturedModelData());
    }
}