package com.shidran.hsrphainon.common;

import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec;

public class HsrPhainonConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.DoubleValue PhainonDamage;
    public static final ForgeConfigSpec.DoubleValue KhaslanaDamage;
    public static final ForgeConfigSpec.DoubleValue BasicATKDamage;
    public static final ForgeConfigSpec.DoubleValue Skill1Damage;
    public static final  ForgeConfigSpec.IntValue Skill2MeteorAmount;
    public static final ForgeConfigSpec.DoubleValue Skill2MeteorDensity;
    public static final ForgeConfigSpec.DoubleValue Skill2DamageMin;
    public static final ForgeConfigSpec.DoubleValue Skill2DamageMax;
    public static final ForgeConfigSpec.DoubleValue LastAttackDamage;
    public static final ForgeConfigSpec.EnumValue<Level.ExplosionInteraction> ExplosionType;

    static {
        BUILDER.push("General Settings");
        PhainonDamage = BUILDER
                .comment("ヘリオス(ファイノン) - ダメージ")
                .defineInRange("PhainonDamage", 12.0, 0.0, 33550336.0);
        KhaslanaDamage = BUILDER
                .comment("ヘリオス(カスライナ) - ダメージ")
                .defineInRange("KhaslanaDamage", 24.0, 0.0, 33550336.0);
        BasicATKDamage = BUILDER
                .comment("通常攻撃 - ダメージ")
                .defineInRange("BasicATKDamage", 30.0, 0.0, 33550336.0);
        Skill1Damage = BUILDER
                .comment("スキル1 - ダメージ")
                .defineInRange("Skill1Damage", 50.0, 0.0, 33550336.0);
        Skill2DamageMin = BUILDER
                .comment("スキル2 - 隕石(小)のダメージ")
                .defineInRange("Skill2DamageMin", 10.0, 0.0, 33550336.0);
        Skill2DamageMax = BUILDER
                .comment("スキル2 - 隕石(大)のダメージ")
                .defineInRange("Skill2DamageMax", 20.0, 0.0, 33550336.0);
        Skill2MeteorAmount = BUILDER
                .comment("スキル2 - 隕石の数")
                .defineInRange("Skill2MeteorAmount", 30, 0, 100);
        Skill2MeteorDensity = BUILDER
                .comment("スキル2 - 隕石の密度")
                .defineInRange("Skill2MeteorDensity", 30.0, 0.0, 100.0);
        LastAttackDamage = BUILDER
                .comment("ラストアタック - ダメージ")
                .defineInRange("LastAttackDamage", 20.0, 0.0, 33550336.0);
        ExplosionType = BUILDER
                .comment("爆発の挙動 (NONE: 破壊なし, BLOCK: 地形破壊, MOB: エンティティ, TNT: TNT)")
                .defineEnum("ExplosionType", Level.ExplosionInteraction.NONE);
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
