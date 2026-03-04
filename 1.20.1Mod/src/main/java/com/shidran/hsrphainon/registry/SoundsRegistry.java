package com.shidran.hsrphainon.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import static com.shidran.hsrphainon.common.HsrPhainonConstants.MOD_ID;

public class SoundsRegistry {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MOD_ID);

    public static final RegistryObject<SoundEvent> UltimateSE =
            SOUND_EVENTS.register(
                    "ultimate",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath("hsrphainon","ultimate")
                    )
            );
    public static final RegistryObject<SoundEvent> Skill1SE =
            SOUND_EVENTS.register(
                    "skill1",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(MOD_ID,"skill1")
                    )
            );
    public static final RegistryObject<SoundEvent> Skill2SE =
            SOUND_EVENTS.register(
                    "skill2",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(MOD_ID,"skill2")
                    )
            );
    public static final RegistryObject<SoundEvent> LastAttackSE =
            SOUND_EVENTS.register(
                    "lastattack",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(MOD_ID,"lastattack")
                    )
            );
    public static final RegistryObject<SoundEvent> BasicAttackSE =
            SOUND_EVENTS.register(
                    "basicattack",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(MOD_ID,"basicattack")
                    )
            );
    public static final RegistryObject<SoundEvent> SkillSE =
            SOUND_EVENTS.register(
                    "skill",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(MOD_ID,"skill")
                    )
            );
}
