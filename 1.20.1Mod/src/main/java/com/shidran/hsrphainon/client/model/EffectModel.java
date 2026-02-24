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

    /**
     * エンダードラゴンの死亡エフェクト（光の筋）を描画する
     * @param deathTicks 0〜200程度の経過時間
     * @param poseStack ポーズスタック
     * @param buffer バッファソース
     */
    public static void renderExplosionLight(float deathTicks, PoseStack poseStack, MultiBufferSource buffer) {
        float progress = deathTicks / 40.0F; // 0.0 to 1.0

        // 80%を超えたらフェードアウト開始
        float fadeOut = progress > 0.8F ? (progress - 0.8F) / 0.2F : 0.0F;

        RandomSource random = RandomSource.create(432L); // 固定シードで形状を維持
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.lightning());

        poseStack.pushPose();
        // 必要に応じてここで中心位置を調整
        // poseStack.translate(x, y, z);

        // 光の筋の数は時間とともに加速して増える
        int lightCount = (int)(10.0F * (1.0F - fadeOut));
        //(int)((progress + progress * progress) / 2.0F * 60.0F); 光が徐々に増加する
        //(int)(20.0F * (1.0F - fadeOut)); 光が一定

        for (int i = 0; i < lightCount; ++i) {
            poseStack.pushPose();

            // 全方位にランダム回転
            poseStack.mulPose(Axis.XP.rotationDegrees(random.nextFloat() * 360.0F));
            poseStack.mulPose(Axis.YP.rotationDegrees(random.nextFloat() * 360.0F));
            poseStack.mulPose(Axis.ZP.rotationDegrees(random.nextFloat() * 360.0F));
            poseStack.mulPose(Axis.XP.rotationDegrees(random.nextFloat() * 360.0F));
            poseStack.mulPose(Axis.YP.rotationDegrees(random.nextFloat() * 360.0F));
            // Z軸にのみ進捗に応じた回転を加え、ドリル的な動きを出す
            poseStack.mulPose(Axis.ZP.rotationDegrees(random.nextFloat() * 360.0F + progress * 90.0F));

            float length = random.nextFloat() * 20.0F + 5.0F + fadeOut * 10.0F;
            float width = random.nextFloat() * 2.0F + 1.0F + fadeOut * 2.0F;
            Matrix4f matrix = poseStack.last().pose();

            // 中心の不透明度 (最後は0になる)
            int alpha = (int)(255.0F * (1.0F - fadeOut));

            // 3枚の三角形で「光のトゲ」を作る
            // 三角形面 1
            renderVertex(vertexConsumer, matrix, 0, 0, 0, alpha); // 中心
            renderVertex(vertexConsumer, matrix, -HALF_SQRT_3 * width, length, -0.5F * width, 0); // 先端1
            renderVertex(vertexConsumer, matrix, HALF_SQRT_3 * width, length, -0.5F * width, 0);  // 先端2

            // 三角形面 2
            renderVertex(vertexConsumer, matrix, 0, 0, 0, alpha);
            renderVertex(vertexConsumer, matrix, HALF_SQRT_3 * width, length, -0.5F * width, 0);
            renderVertex(vertexConsumer, matrix, 0.0F, length, 1.0F * width, 0);

            // 三角形面 3
            renderVertex(vertexConsumer, matrix, 0, 0, 0, alpha);
            renderVertex(vertexConsumer, matrix, 0.0F, length, 1.0F * width, 0);
            renderVertex(vertexConsumer, matrix, -HALF_SQRT_3 * width, length, -0.5F * width, 0);

            poseStack.popPose();
        }

        poseStack.popPose();
    }

    private static void renderVertex(VertexConsumer consumer, Matrix4f matrix, float x, float y, float z, int alpha) {
        // 白ベース (255, 255, 255)
        // どちらでも加算合成で眩しく光る
        consumer.vertex(matrix, x, y, z).color(255, 255, 0, alpha).endVertex();
    }
}