package com.shidran.hsrphainon.client.model;

import com.shidran.hsrphainon.item.ItemDawnmaker;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.model.GeoModel;

import static com.shidran.hsrphainon.common.HsrPhainonConstants.MOD_ID;
import static com.shidran.hsrphainon.common.HsrPhainonConstants.Mode;

public class DawnmakerModel extends GeoModel<ItemDawnmaker> {

    private ItemStack currentStack;
    private boolean hidden = false;

    public void setModel(boolean model) {
    }
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
    public void setCustomResources(ResourceLocation model, ResourceLocation texture) {
    }

    @Override
    public ResourceLocation getModelResource(ItemDawnmaker animatable) {
        if (this.hidden) {
            return ResourceLocation.fromNamespaceAndPath(MOD_ID, "geo/none.geo.json");
        }
        ItemStack stack = animatable.getRenderingStack();
        if (stack != null && stack.hasTag() && stack.getOrCreateTag().getBoolean(Mode)) {
            return ResourceLocation.fromNamespaceAndPath(MOD_ID, "geo/dawnmaker2.geo.json");
        }
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, "geo/dawnmaker.geo.json");
    }


    @Override
    public ResourceLocation getTextureResource(ItemDawnmaker animatable) {
        if (this.hidden) {
            return ResourceLocation.fromNamespaceAndPath(MOD_ID, "textures/item/none.png");
        }
        ItemStack stack = animatable.getRenderingStack();
        if (stack != null && stack.hasTag() && stack.getOrCreateTag().getBoolean(Mode)) {
            return ResourceLocation.fromNamespaceAndPath(MOD_ID, "textures/item/dawnmaker2.png");
        }
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, "textures/item/dawnmaker.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ItemDawnmaker animatable) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, "animations/none.animation.json");
    }
}
