package com.shidran.hsrphainon.client.model;

import com.shidran.hsrphainon.item.ItemDawnmaker;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class DawnmakerModel extends GeoModel<ItemDawnmaker> {
    @Override
    public ResourceLocation getModelResource(ItemDawnmaker animatable) {
        return ResourceLocation.fromNamespaceAndPath("hsrphainon", "geo/dawnmaker2.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ItemDawnmaker animatable) {
        return ResourceLocation.fromNamespaceAndPath("hsrphainon", "textures/item/dawnmaker2.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ItemDawnmaker animatable) {
        return ResourceLocation.fromNamespaceAndPath("hsrphainon", "animations/dawnmaker2.animation.json");
    }
}