package com.shidran.hsrphainon.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.shidran.hsrphainon.client.model.Skill1Model;
import com.shidran.hsrphainon.entity.Skill1Entity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class Skill1Renderer extends GeoEntityRenderer<Skill1Entity> {
    public Skill1Renderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new Skill1Model());
    }

    @Override
    public void preRender(PoseStack poseStack, Skill1Entity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {

        float scale = 30.0F;
        float yaw = animatable.getViewYRot(partialTick);
        float pitch = net.minecraft.util.Mth.lerp(partialTick, animatable.xRotO, animatable.getXRot());

        poseStack.scale(scale, scale, scale);
        poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(-yaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(pitch));

        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}