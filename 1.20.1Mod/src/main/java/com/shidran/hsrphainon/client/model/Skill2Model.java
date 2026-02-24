package com.shidran.hsrphainon.client.model;

import com.shidran.hsrphainon.entity.Skill2Entity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class Skill2Model extends GeoModel<Skill2Entity> {
    @Override
    public ResourceLocation getModelResource(Skill2Entity animatable) {
        // Blockbenchで書き出した隕石のgeoファイルを指定
        return ResourceLocation.fromNamespaceAndPath("hsrphainon", "geo/skill2.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Skill2Entity animatable) {
        // マグマブロックのテクスチャをバニラから参照します
        return ResourceLocation.fromNamespaceAndPath("minecraft", "textures/block/magma.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Skill2Entity animatable) {
        // 隕石のアニメーションを指定（回転など）
        return ResourceLocation.fromNamespaceAndPath("hsrphainon", "animations/skill2.animation.json");
    }
}
