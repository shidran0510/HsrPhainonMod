package com.shidran.hsrphainon.registry;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.awt.event.KeyEvent;

import static com.shidran.hsrphainon.common.HsrPhainonConstants.*;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class KeyBindRegistry {

    public static KeyMapping[] hsrphainonKey =  new KeyMapping[4];

    @SubscribeEvent
    public static void keyRegistar(final RegisterKeyMappingsEvent event){
        hsrphainonKey[BasicATK] = create("BasicATK", KeyEvent.VK_C);
        hsrphainonKey[Skill1] = create("Skill1", KeyEvent.VK_Q);
        hsrphainonKey[Skill2] = create("Skill2", KeyEvent.VK_E);
        hsrphainonKey[Ultimate] = create("Ultimate", KeyEvent.VK_X);
        for (KeyMapping key:hsrphainonKey){
            event.register(key);
        }
    }

    private static KeyMapping create(String name, int key){
        return new KeyMapping("key.hsrphainon." + name, key, "key.categories.hsrphainon");
    }
}
