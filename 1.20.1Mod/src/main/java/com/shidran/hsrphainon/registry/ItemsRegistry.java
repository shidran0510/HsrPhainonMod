package com.shidran.hsrphainon.registry;

import com.shidran.hsrphainon.HsrPhainon;
import com.shidran.hsrphainon.item.ItemDawnmaker;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemsRegistry {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, HsrPhainon.MOD_ID);

    public static final RegistryObject<SwordItem> DAWNMAKER = ITEMS.register("dawnmaker",
            () -> new ItemDawnmaker(
                    Tiers.DIAMOND,          // Tier
                    7,                      // 攻撃力補正
                    0F,                  // 攻撃速度
                    new Item.Properties()   // 設定
                    )
            );
}
