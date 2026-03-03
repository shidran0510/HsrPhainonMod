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
    public static final ForgeConfigSpec.DoubleValue Skill2MeteorHorizontalRange;
    public static final ForgeConfigSpec.DoubleValue Skill2MeteorVerticalRange;
    public static final ForgeConfigSpec.DoubleValue Skill2DamageSmall;
    public static final ForgeConfigSpec.DoubleValue Skill2DamageLarge;
    public static final ForgeConfigSpec.DoubleValue LastAttackDamage;
    public static final ForgeConfigSpec.EnumValue<Level.ExplosionInteraction> ExplosionType;

    static {
        BUILDER.push("General Settings");
        PhainonDamage = BUILDER
                .comment("Dawnmaker(Phainon) - Damage")
                .defineInRange("PhainonDamage", 12.0, 0.0, 33550336.0);
        KhaslanaDamage = BUILDER
                .comment("Dawnmaker(Khaslana) - Damage")
                .defineInRange("KhaslanaDamage", 24.0, 0.0, 33550336.0);
        BasicATKDamage = BUILDER
                .comment("Basic ATK - Damage")
                .defineInRange("BasicATKDamage", 30.0, 0.0, 33550336.0);
        Skill1Damage = BUILDER
                .comment("Skill1 - Damage")
                .defineInRange("Skill1Damage", 50.0, 0.0, 33550336.0);
        Skill2DamageSmall = BUILDER
                .comment("Skill2 Meteor(small) - Damage")
                .defineInRange("Skill2DamageSmall", 10.0, 5.0, 50);
        Skill2DamageLarge = BUILDER
                .comment("Skill2 Meteor(large) - Damage")
                .defineInRange("Skill2DamageLarge", 20.0, 0.0, 100);
        Skill2MeteorAmount = BUILDER
                .comment("Skill2 Meteor(small) Amount")
                .defineInRange("Skill2MeteorAmount", 30, 0, 100);
        Skill2MeteorHorizontalRange = BUILDER
                .comment("Skill2 Meteor(small) HorizontalRange")
                .defineInRange("Skill2MeteorHorizontalRange", 30.0, 0.0, 100.0);
        Skill2MeteorVerticalRange = BUILDER
                .comment("Skill2 Meteor(small) VerticalRange")
                .defineInRange("Skill2MeteorVerticalRange", 6.0, 0.0, 100.0);
        LastAttackDamage = BUILDER
                .comment("Ultimate LastAttack - Damage")
                .defineInRange("LastAttackDamage", 20.0, 0.0, 100.0);
        ExplosionType = BUILDER
                .comment("Explosion Interaction",
                        "NONE: No world destruction or fire (Safe)",
                        "BLOCK: Destroys blocks and creates fire",
                        "MOB: Damages entities but no block destruction",
                        "TNT: Standard TNT-like destruction (Destroys blocks)")
                .defineEnum("ExplosionType", Level.ExplosionInteraction.NONE);
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
