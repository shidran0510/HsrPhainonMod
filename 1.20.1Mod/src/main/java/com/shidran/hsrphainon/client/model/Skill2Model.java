package com.shidran.hsrphainon.client.model;

import com.shidran.hsrphainon.entity.Skill2Entity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

import static com.shidran.hsrphainon.common.HsrPhainonConstants.*;

public class Skill2Model extends GeoModel<Skill2Entity> {
    @Override
    public ResourceLocation getModelResource(Skill2Entity animatable) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, "geo/skill2.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Skill2Entity animatable) {
        return ResourceLocation.fromNamespaceAndPath("minecraft", "textures/block/magma.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Skill2Entity animatable) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, "animations/none.animation.json");
    }
}
