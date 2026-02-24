package com.shidran.hsrphainon.network;

import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = "hsrphainon", value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AnimationLayer {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        String[] anims = {"transform", "skill1", "skill2", "lastattack", "basicattack"};
        for (String id : anims) {
            PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(
                    ResourceLocation.fromNamespaceAndPath("hsrphainon", id),
                    42,
                    (player) -> new ModifierLayer<>()
            );
        }
    }
}