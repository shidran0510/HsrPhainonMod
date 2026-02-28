package com.shidran.hsrphainon.network;

import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static com.shidran.hsrphainon.common.HsrPhainonConstants.*;

@Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AnimationLayer {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        for (String id : animID) {
            PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(
                    ResourceLocation.fromNamespaceAndPath(MOD_ID, id),
                    42,
                    (player) -> new ModifierLayer<>()
            );
        }
    }
}