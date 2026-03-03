package com.shidran.hsrphainon.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.shidran.hsrphainon.client.model.DawnmakerModel;
import com.shidran.hsrphainon.item.ItemDawnmaker;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class DawnmakerRenderer extends GeoItemRenderer<ItemDawnmaker> {
    public DawnmakerRenderer() {
        super(new DawnmakerModel());
    }

    @Override
    public void preRender(PoseStack poseStack, ItemDawnmaker animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {

    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext transformType, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        CompoundTag tag = stack.getOrCreateTag();
        boolean isAnimating = tag.getBoolean("IsAnimating");

        if (!isAnimating) {
            net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();

            String modelName = tag.getBoolean(com.shidran.hsrphainon.common.HsrPhainonConstants.Mode) ? "dawnmaker2" : "dawnmaker";
            net.minecraft.resources.ResourceLocation vanillaModelLocation =
                    net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("hsrphainon", "item/" + modelName);

            net.minecraft.client.resources.model.BakedModel bakedModel =
                    mc.getModelManager().getModel(new net.minecraft.client.resources.model.ModelResourceLocation(vanillaModelLocation, "inventory"));

            poseStack.pushPose();
            mc.getItemRenderer().render(stack, transformType, false, poseStack, bufferSource, packedLight, packedOverlay, bakedModel);
            poseStack.popPose();
            return;
        }

        super.renderByItem(stack, transformType, poseStack, bufferSource, packedLight, packedOverlay);
    }
}
