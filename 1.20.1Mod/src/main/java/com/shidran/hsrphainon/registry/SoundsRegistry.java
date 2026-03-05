package com.shidran.hsrphainon.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.shidran.hsrphainon.common.HsrPhainonConstants.*;

public class SoundsRegistry {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MOD_ID);

    private static RegistryObject<SoundEvent> registerSound(String name) {
        return SOUND_EVENTS.register(name,
                () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MOD_ID, name))
        );
    }

    public static final RegistryObject<SoundEvent> UltimateSE = registerSound("ultimate");
    public static final RegistryObject<SoundEvent> SkillSE = registerSound("skill");
    public static final RegistryObject<SoundEvent> Skill1SE = registerSound("skill1");
    public static final RegistryObject<SoundEvent> Skill2SE = registerSound("skill2");
    public static final RegistryObject<SoundEvent> LastAttackSE = registerSound("lastattack");
    public static final RegistryObject<SoundEvent> BasicAttackSE = registerSound("basicattack");
}
