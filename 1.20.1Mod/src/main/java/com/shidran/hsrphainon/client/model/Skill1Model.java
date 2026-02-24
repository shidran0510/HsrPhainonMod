package com.shidran.hsrphainon.client.model;

import com.shidran.hsrphainon.entity.Skill1Entity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class Skill1Model extends GeoModel<Skill1Entity> {
    @Override
    public ResourceLocation getModelResource(Skill1Entity animatable) {
        return ResourceLocation.fromNamespaceAndPath("hsrphainon", "geo/skill1.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Skill1Entity animatable) {
        return ResourceLocation.fromNamespaceAndPath("hsrphainon", "textures/item/dawnmaker2.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Skill1Entity animatable) {
        return ResourceLocation.fromNamespaceAndPath("hsrphainon", "animations/skill1.animation.json");
    }
}
