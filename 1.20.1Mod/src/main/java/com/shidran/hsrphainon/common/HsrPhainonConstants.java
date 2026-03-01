package com.shidran.hsrphainon.common;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import static com.shidran.hsrphainon.item.ItemDawnmaker.BaseAttackDmageUUID;
import static com.shidran.hsrphainon.item.ItemDawnmaker.BaseAttackSpeedUUID;

//import static com.shidran.hsrphainon.common.HsrPhainonConstants.*;
public class HsrPhainonConstants {
//文字定義
    public static final String MOD_ID = "hsrphainon";
    public static final String LockTimer = "LockTimer";
    public static final String ModelTimer = "ModelTimer";
    public static final String DelayTimer = "DelayTimer";
    public static final String ActionName = "ActionName";
    public static final String CustomModelData = "CustomModelData";
    public static final String Mode = "mode";
    public static final String ShowDawnmaker = "showDawnmaker";
    public static final String[] animID = {"transform", "skill1", "skill2", "lastattack", "basicattack"};
//共通変数定義
    public static Level world(Player player) { return player.level(); }
    public static CompoundTag tag(Player player) { return player.getMainHandItem().getOrCreateTag(); }
    public static CompoundTag tag(ItemStack stack) {return stack.getTag(); }
    public static Item item(Player player) {return player.getMainHandItem().getItem();}
//キー番号
    public static final int BasicATK = 0;
    public static final int Skill1 = 1;
    public static final int Skill2 = 2;
    public static final int Ultimate = 3;
//スキルダメージ
    public static float getBasicATKDamage() { return HsrPhainonConfig.BasicATKDamage.get().floatValue(); }
    public static float getSkill1Damage() { return HsrPhainonConfig.Skill1Damage.get().floatValue(); }
    public static float getSkill2DamageMin() { return HsrPhainonConfig.Skill2DamageMin.get().floatValue(); }
    public static float getSkill2DamageMax() { return HsrPhainonConfig.Skill2DamageMax.get().floatValue(); }
    public static int getSkill2MeteorAmount() { return HsrPhainonConfig.Skill2MeteorAmount.get(); }
    public static float getSkill2MeteorDensity() { return HsrPhainonConfig.Skill2MeteorDensity.get().floatValue(); }
    public static float getLastAttackDamage() { return HsrPhainonConfig.LastAttackDamage.get().floatValue(); }
    public static Level.ExplosionInteraction getExplosionType() {return HsrPhainonConfig.ExplosionType.get();}
// 武器属性 (AttributeModifier)
    public static Multimap<Attribute, AttributeModifier> PhainonDamage;
    public static Multimap<Attribute, AttributeModifier> KhaslanaDamage;

// MOD起動時に呼び出す初期化メソッド
public static Multimap<Attribute, AttributeModifier> getPhainonModifiers() {
    double phainonDmg = HsrPhainonConfig.PhainonDamage.get();
    ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
    builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BaseAttackDmageUUID, "Weapon modifier", phainonDmg - 1.0D, AttributeModifier.Operation.ADDITION));
    builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BaseAttackSpeedUUID, "Weapon modifier", -2.4D, AttributeModifier.Operation.ADDITION));
    return builder.build();
}

    public static Multimap<Attribute, AttributeModifier> getKhaslanaModifiers() {
        double khaslanaDmg = HsrPhainonConfig.KhaslanaDamage.get();
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BaseAttackDmageUUID, "Weapon modifier", khaslanaDmg - 1.0D, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BaseAttackSpeedUUID, "Weapon modifier", -2.4D, AttributeModifier.Operation.ADDITION));
        return builder.build();
    }
}
