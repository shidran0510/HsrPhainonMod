package com.shidran.hsrphainon.registry;

import com.shidran.hsrphainon.item.ItemDawnmaker;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.shidran.hsrphainon.common.HsrPhainonConstants.MOD_ID;

public class ItemsRegistry {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    public static final RegistryObject<SwordItem> DAWNMAKER = ITEMS.register("dawnmaker",
            () -> new ItemDawnmaker(
                    Tiers.NETHERITE,     // Tier
                    1,                   // 攻撃力補正
                    -2.4F,               // 攻撃速度
                    new Item.Properties()// 設定
                            .stacksTo(1)
                            .fireResistant()
                    )
            );

    public static final RegistryObject<Item> COREFLAME_WORLDBEARING = ITEMS.register("coreflame_worldbearing",
            () -> new Item(new Item.Properties()));
}
