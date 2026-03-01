package com.shidran.hsrphainon.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.shidran.hsrphainon.client.model.EffectModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import static com.shidran.hsrphainon.common.HsrPhainonConstants.*;

@Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
public class EffectRenderer {
    private static int effectTimer = 0;

    public static void startTransformationEffect() {
        effectTimer = 200;
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && effectTimer > 0) {
            effectTimer--;
        }
    }

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES || effectTimer <= 0) return;

        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;

        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
        Vec3 cam = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();

        poseStack.pushPose();

        double x = Mth.lerp(event.getPartialTick(), player.xo, player.getX()) - cam.x;
        double y = Mth.lerp(event.getPartialTick(), player.yo, player.getY()) - cam.y + 1.8D;
        double z = Mth.lerp(event.getPartialTick(), player.zo, player.getZ()) - cam.z;

        poseStack.translate(x, y, z);

        EffectModel.renderExplosionLight(200 - effectTimer, poseStack, buffer);

        poseStack.popPose();
    }
}