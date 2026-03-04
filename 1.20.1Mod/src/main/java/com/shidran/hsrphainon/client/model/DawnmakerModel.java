package com.shidran.hsrphainon.client.model;

import com.shidran.hsrphainon.item.ItemDawnmaker;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.model.GeoModel;

import static com.shidran.hsrphainon.common.HsrPhainonConstants.MOD_ID;
import static com.shidran.hsrphainon.common.HsrPhainonConstants.Mode;

public class DawnmakerModel extends GeoModel<ItemDawnmaker> {

    @Override
    public ResourceLocation getModelResource(ItemDawnmaker animatable) {
        ItemStack stack = animatable.getRenderingStack();

        if (stack == null || !stack.getOrCreateTag().getBoolean("IsAnimating")) {
            return ResourceLocation.fromNamespaceAndPath(MOD_ID, "geo/none.geo.json");
        }

        return ResourceLocation.fromNamespaceAndPath(MOD_ID, "geo/dawnmaker.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ItemDawnmaker animatable) {
        ItemStack stack = animatable.getRenderingStack();

        if (stack != null && stack.getOrCreateTag().getBoolean("IsAnimating")) {
            var player = net.minecraft.client.Minecraft.getInstance().player;
            if (player != null) return player.getSkinTextureLocation();
        }
        return stack.getOrCreateTag().getBoolean(Mode) ?
                ResourceLocation.fromNamespaceAndPath(MOD_ID, "textures/item/dawnmaker2.png") :
                ResourceLocation.fromNamespaceAndPath(MOD_ID, "textures/item/dawnmaker.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ItemDawnmaker animatable) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, "animations/none.animation.json");
    }
}