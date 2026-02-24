package com.shidran.hsrphainon.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.shidran.hsrphainon.client.model.DawnmakerModel;
import com.shidran.hsrphainon.item.ItemDawnmaker;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class DawnmakerRenderer extends GeoItemRenderer<ItemDawnmaker> {
    public DawnmakerRenderer() {
        super(new DawnmakerModel());
    }
    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (stack.hasTag() && stack.getTag().getInt("LockTimer") > 0) {
            if (!stack.getTag().getBoolean("ShowWeapon")) {
                return;
            }
        }

        super.renderByItem(stack, displayContext, poseStack, bufferSource, packedLight, packedOverlay);
    }
}