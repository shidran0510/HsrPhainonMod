package com.shidran.hsrphainon.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.RandomSource;
import org.joml.Matrix4f;

public class EffectModel {
    private static final float HALF_SQRT_3 = (float)(Math.sqrt(3.0D) / 2.0D);

    private static final float TopWidthratio = 1.0F;
    private static final float SideWidthratio = -0.5F;

    public static void renderExplosionLight(float deathTicks, PoseStack poseStack, MultiBufferSource buffer) {
        float progress = deathTicks / 40.0F;
        float fadeOut = progress > 0.8F ? (progress - 0.8F) / 0.2F : 0.0F;

        RandomSource random = RandomSource.create(432L);
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.lightning());

        poseStack.pushPose();

        int lightCount = (int)(10.0F * (1.0F - fadeOut));
        //(int)((progress + progress * progress) / 2.0F * 60.0F); 光が徐々に増加する
        //(int)(20.0F * (1.0F - fadeOut)); 光が一定

        for (int i = 0; i < lightCount; ++i) {
            poseStack.pushPose();

            poseStack.mulPose(Axis.XP.rotationDegrees(random.nextFloat() * 360.0F));
            poseStack.mulPose(Axis.YP.rotationDegrees(random.nextFloat() * 360.0F));
            poseStack.mulPose(Axis.ZP.rotationDegrees(random.nextFloat() * 360.0F));
            poseStack.mulPose(Axis.XP.rotationDegrees(random.nextFloat() * 360.0F));
            poseStack.mulPose(Axis.YP.rotationDegrees(random.nextFloat() * 360.0F));
            poseStack.mulPose(Axis.ZP.rotationDegrees(random.nextFloat() * 360.0F + progress * 90.0F));

            float length = random.nextFloat() * 20.0F + 5.0F + fadeOut * 10.0F;
            float width = random.nextFloat() * 2.0F + 1.0F + fadeOut * 2.0F;
            Matrix4f matrix = poseStack.last().pose();

            int alpha = (int)(255.0F * (1.0F - fadeOut));

            renderVertex(vertexConsumer, matrix, 0, 0, 0, alpha);
            renderVertex(vertexConsumer, matrix, -HALF_SQRT_3 * width, length, SideWidthratio * width, 0); // 先端1
            renderVertex(vertexConsumer, matrix, HALF_SQRT_3 * width, length, SideWidthratio * width, 0);  // 先端2

            renderVertex(vertexConsumer, matrix, 0, 0, 0, alpha);
            renderVertex(vertexConsumer, matrix, HALF_SQRT_3 * width, length, SideWidthratio * width, 0);
            renderVertex(vertexConsumer, matrix, 0.0F, length, TopWidthratio * width, 0);

            renderVertex(vertexConsumer, matrix, 0, 0, 0, alpha);
            renderVertex(vertexConsumer, matrix, 0.0F, length, TopWidthratio * width, 0);
            renderVertex(vertexConsumer, matrix, -HALF_SQRT_3 * width, length, SideWidthratio * width, 0);

            poseStack.popPose();
        }

        poseStack.popPose();
    }

    private static void renderVertex(VertexConsumer consumer, Matrix4f matrix, float x, float y, float z, int alpha) {

        consumer.vertex(matrix, x, y, z).color(255, 255, 0, alpha).endVertex();
    }
}