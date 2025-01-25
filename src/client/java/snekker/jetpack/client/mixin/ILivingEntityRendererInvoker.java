package snekker.jetpack.client.mixin;

import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntityRenderer.class)
public interface ILivingEntityRendererInvoker<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> {
    // For whatever reason, we can't use the same method
    // name in the mixin method as the original method.
    @Invoker("addFeature")
    boolean addFeature_mixin(FeatureRenderer<S, M> feature);
}
