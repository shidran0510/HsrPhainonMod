package com.shidran.hsrphainon.registry;

import com.shidran.hsrphainon.client.renderer.Skill1Renderer;
import com.shidran.hsrphainon.client.renderer.Skill2Renderer;
import com.shidran.hsrphainon.entity.Skill1Entity;
import com.shidran.hsrphainon.entity.Skill2Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.shidran.hsrphainon.common.HsrPhainonConstants.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class EntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MOD_ID);

    public static final RegistryObject<EntityType<Skill1Entity>> SKILL1_ENTITY =
            ENTITIES.register("dawnmaker", () -> EntityType.Builder.of(Skill1Entity::new, MobCategory.MISC)
                    .sized(1.0F, 1.0F)
                    .updateInterval(1)
                    .build("dawnmaker"));

    public static final RegistryObject<EntityType<Skill2Entity>> SKILL2_ENTITY =
            ENTITIES.register("meteor", () -> EntityType.Builder.of(Skill2Entity::new, MobCategory.MISC)
                    .sized(2.0F, 2.0F)
                    .build("meteor"));

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityRegistry.SKILL1_ENTITY.get(), Skill1Renderer::new);

        event.registerEntityRenderer(EntityRegistry.SKILL2_ENTITY.get(), Skill2Renderer::new);
    }
}
