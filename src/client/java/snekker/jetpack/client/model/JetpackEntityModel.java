package snekker.jetpack.client.model;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.util.Identifier;

public class JetpackEntityModel<T extends PlayerEntityRenderState> extends BipedEntityModel<T> {
    public static EntityModelLayer JETPACK = new EntityModelLayer(Identifier.of("jetpack"), "main");

    //private ModelPart jetpack;
    public JetpackEntityModel(ModelPart root) {
        super(root);
        //jetpack = root.getChild("jetpack");
    }

    public static TexturedModelData getTexturedModelData() {
        var modelData = BipedEntityModel.getModelData(Dilation.NONE, 0.0F);
        var root = modelData.getRoot();
        var head = root.addChild("head");
        head.addChild("hat");
        var modelPartData3 = root.addChild("body");
        root.addChild("left_arm");
        root.addChild("right_arm");
        root.addChild("left_leg");
        root.addChild("right_leg");
        var canisterBuilder = ModelPartBuilder.create().uv(0, 0).cuboid(-3.0F, 0F, -3.0F, 2.0F, 7.0F, 2.0F, new Dilation(1.0F));
        root.addChild("canister_left", canisterBuilder, ModelTransform.pivot(-0.5F, 0, 6.0F));
        root.addChild("canister_right", canisterBuilder, ModelTransform.pivot(4.5F, 0, 6.0F));
        var thrusterBuilder = ModelPartBuilder.create().uv(8, 0).cuboid(-2.5F, 0F, -2.5F, 1F, 0.5F, 1.0F, new Dilation(1.0F));
        root.addChild("thruster_left", thrusterBuilder, ModelTransform.pivot(-0.5F, 7.5F, 6.0F));
        root.addChild("thruster_right", thrusterBuilder, ModelTransform.pivot(4.5F, 7.5F, 6.0F));
        var centerBuilder = ModelPartBuilder.create().uv(0, 12).cuboid(-4.0F, 0.5F, -3.5F, 4.0F, 4.0F, 3.0F, new Dilation(1.0F));
        root.addChild("center", centerBuilder, ModelTransform.pivot(2F, 1F, 6.5F));
        return TexturedModelData.of(modelData, 32, 32);
    }
}
