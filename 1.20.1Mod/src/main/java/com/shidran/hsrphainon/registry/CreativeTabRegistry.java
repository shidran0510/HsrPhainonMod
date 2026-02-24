package com.shidran.hsrphainon.registry;

import com.shidran.hsrphainon.HsrPhainon;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CreativeTabRegistry {
    public static final DeferredRegister<CreativeModeTab> MOD_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, HsrPhainon.MOD_ID);

    public static final RegistryObject<CreativeModeTab> HsrPhainon_MAIN = MOD_TABS.register("hsrphainon_main",
            ()-> {return CreativeModeTab.builder()
                    .icon(()->new ItemStack(ItemsRegistry.DAWNMAKER.get())) //タブのアイコン
                    .title(Component.translatable("itemGroup.hsrphainon_main"))
                    .displayItems((param,output)->{
                        ItemStack phainonStack = new ItemStack(ItemsRegistry.DAWNMAKER.get());
                        phainonStack.getOrCreateTag().putBoolean("mode", false);
                        output.accept(phainonStack);

                        // 2. カスライナ形態の追加
                        ItemStack khaslanaStack = new ItemStack(ItemsRegistry.DAWNMAKER.get());
                        khaslanaStack.getOrCreateTag().putBoolean("mode", true);
                        // クリエイティブモードで取り出した時にモデルが変わるよう CustomModelData も設定
                        khaslanaStack.getOrCreateTag().putInt("CustomModelData", 1);
                        output.accept(khaslanaStack);
                    })
                    .build();

            });
}