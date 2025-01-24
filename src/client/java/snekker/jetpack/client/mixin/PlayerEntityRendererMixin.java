package snekker.jetpack.client.mixin;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.ArmorEntityModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import snekker.jetpack.client.model.JetpackFeatureRenderer;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {
    @Inject(at=@At("TAIL"), method="<init>")
    public void init(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo info) {
        var invoker = (ILivingEntityRendererInvoker)this;
        var renderer = (PlayerEntityRenderer)(Object)this;
        invoker.addFeatur(new JetpackFeatureRenderer(renderer, ctx.getEntityModels()));
    }
}

