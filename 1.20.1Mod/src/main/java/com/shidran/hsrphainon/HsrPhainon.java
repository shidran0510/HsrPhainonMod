package com.shidran.hsrphainon;

import com.shidran.hsrphainon.common.HsrPhainonConfig;
import com.shidran.hsrphainon.network.AnimationLayer;
import com.shidran.hsrphainon.registry.EntityRegistry;
import com.shidran.hsrphainon.registry.PacketRegistry;
import com.shidran.hsrphainon.registry.ItemsRegistry;
import com.shidran.hsrphainon.registry.CreativeTabRegistry;
import com.shidran.hsrphainon.registry.SoundsRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.geckolib.GeckoLib;

@Mod("hsrphainon")

public class HsrPhainon {

    @SuppressWarnings("removal")
    public HsrPhainon(){
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ItemsRegistry.ITEMS.register(bus);
        CreativeTabRegistry.MOD_TABS.register(bus);
        SoundsRegistry.SOUND_EVENTS.register(bus);
        PacketRegistry.register();
        GeckoLib.initialize();
        bus.addListener(AnimationLayer::onClientSetup);
        EntityRegistry.ENTITIES.register(bus);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, HsrPhainonConfig.SPEC);
    }
}
