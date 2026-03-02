package com.shidran.hsrphainon.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static com.shidran.hsrphainon.common.HsrPhainonConstants.*;

public class CreativeTabRegistry {

    public static final DeferredRegister<CreativeModeTab> MOD_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

    public static final RegistryObject<CreativeModeTab> HsrPhainon_MAIN = MOD_TABS.register("hsrphainon_tab",
            ()-> {return CreativeModeTab.builder()
                    .icon(()->new ItemStack(ItemsRegistry.DAWNMAKER.get()))
                    .title(Component.translatable("itemGroup.hsrphainon_tab"))
                    .displayItems((param,output)->{
                        ItemStack phainonStack = new ItemStack(ItemsRegistry.DAWNMAKER.get());
                        phainonStack.getOrCreateTag().putBoolean(Mode, false);
                        output.accept(phainonStack);

                        ItemStack khaslanaStack = new ItemStack(ItemsRegistry.DAWNMAKER.get());
                        khaslanaStack.getOrCreateTag().putBoolean(Mode, true);

                        khaslanaStack.getOrCreateTag().putInt(CustomModelData, 1);
                        output.accept(khaslanaStack);

                        ItemStack coreflame_worldbearing = new ItemStack(ItemsRegistry.COREFLAME_WORLDBEARING.get());
                        output.accept(coreflame_worldbearing);
                    })
                    .build();

            });
}