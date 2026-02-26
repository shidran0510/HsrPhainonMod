package com.shidran.hsrphainon.common;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import static com.shidran.hsrphainon.item.ItemDawnmaker.BaseAttackDmageUUID;
import static com.shidran.hsrphainon.item.ItemDawnmaker.BaseAttackSpeedUUID;

//import static com.shidran.hsrphainon.common.HsrPhainonConstants.*;
public class HsrPhainonConstants {
//タイマー
    public static final String LockTimer = "LockTimer";
    public static final String EffectTImer = "EffectTimer";
    public static final String LastAttackTimer = "LastAttackTimer";
    public static final String ModelTimer = "ModelTimer";
    public static final String DelayTimer = "DelayTimer";
    public static final String ActionName = "ActionName";
//キー番号
    public static final int BasicATK = 0;
    public static final int Skill1 = 1;
    public static final int Skill2 = 2;
    public static final int Ultimate = 3;
//スキル設定
    public static final float BasicDamage = 30.0F;
    public static final float Skill1Damage = 50.0F; //スキル1のダメージ
    public static final int Skill2MeteorAmount = 30; //隕石の数
    public static final float Skill2MeteorDensity = 30.0F; //隕石の密度
    public static final float Skill2DamageMin = 10.0F; //隕石最小爆発ダメージ
    public static final float Skill2DamageMax = 20.0F; //隕石最大爆発ダメージ
    public static final float LastAttackDamage = 20.0F;//必殺技終了時の爆発ダメージ
    public static final Level.ExplosionInteraction explosionType = Level.ExplosionInteraction.NONE;
//ダメージ
    public static final double KhaslanaDamage = 24.0D;
    public static final double PhainonDamage = 12.0D;
    public static final Multimap<Attribute, AttributeModifier> PHAINON_MODIFIERS;
    public static final Multimap<Attribute, AttributeModifier> KHASLANA_MODIFIERS;
    static {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> phainonBuilder = ImmutableMultimap.builder();
        phainonBuilder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BaseAttackDmageUUID, "Weapon modifier", PhainonDamage - 1.0D, AttributeModifier.Operation.ADDITION));
        phainonBuilder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BaseAttackSpeedUUID, "Weapon modifier", -2.4D, AttributeModifier.Operation.ADDITION));
        PHAINON_MODIFIERS = phainonBuilder.build();

        ImmutableMultimap.Builder<Attribute, AttributeModifier> khaslanaBuilder = ImmutableMultimap.builder();
        khaslanaBuilder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BaseAttackDmageUUID, "Weapon modifier", KhaslanaDamage - 1.0D, AttributeModifier.Operation.ADDITION));
        khaslanaBuilder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BaseAttackSpeedUUID, "Weapon modifier", -2.4D, AttributeModifier.Operation.ADDITION));
        KHASLANA_MODIFIERS = khaslanaBuilder.build();
    }
}
