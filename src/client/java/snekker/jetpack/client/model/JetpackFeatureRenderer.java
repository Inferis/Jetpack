package snekker.jetpack.client.model;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.LoadedEntityModels;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import snekker.jetpack.Jetpack;
import snekker.jetpack.item.JetpackItem;

public class JetpackFeatureRenderer extends FeatureRenderer<PlayerEntityRenderState, PlayerEntityModel> {
    private final JetpackEntityModel model;

    public JetpackFeatureRenderer(FeatureRendererContext<PlayerEntityRenderState, PlayerEntityModel> context, LoadedEntityModels entityModels) {
        super(context);
        this.model = new JetpackEntityModel(entityModels.getModelPart(JetpackEntityModelLayer.JETPACK));
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, PlayerEntityRenderState state, float limbAngle, float limbDistance) {
        if (state.equippedChestStack.getItem() instanceof JetpackItem) {
            matrices.push();

            var jetpackTexture = Jetpack.id("textures/entity/jetpack.png");
            //var jetpackTexture = Identifier.ofVanilla("textures/block/stone.png");
            var skinTexture = state.skinTextures.texture();
            var vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntitySolid(jetpackTexture));
            ((PlayerEntityModel)this.getContextModel()).copyTransforms(this.model);
            this.model.setAngles(state);
            this.model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);

            matrices.pop();
        }
    }
}
