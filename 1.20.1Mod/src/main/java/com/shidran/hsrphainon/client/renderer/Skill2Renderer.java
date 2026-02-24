package com.shidran.hsrphainon.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.shidran.hsrphainon.client.model.Skill2Model;
import com.shidran.hsrphainon.entity.Skill2Entity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class Skill2Renderer extends GeoEntityRenderer<Skill2Entity> {
    public Skill2Renderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new Skill2Model());
        this.shadowRadius = 1.0F;
    }

    @Override
    public void preRender(PoseStack poseStack, Skill2Entity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {

        float scale = animatable.isBig() ? 10.0F : 1.0F;
        poseStack.scale(scale, scale, scale);

        poseStack.translate(0.0D, -0.5D, 0.0D);

        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }


}
