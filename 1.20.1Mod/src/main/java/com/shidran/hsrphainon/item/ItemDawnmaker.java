package com.shidran.hsrphainon.item;

import com.google.common.collect.Multimap;
import com.shidran.hsrphainon.client.renderer.DawnmakerRenderer;
import com.shidran.hsrphainon.registry.SoundsRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;
import static com.shidran.hsrphainon.common.HsrPhainonConstants.*;

public class ItemDawnmaker extends SwordItem implements GeoItem {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public static final UUID BaseAttackDmageUUID = BASE_ATTACK_DAMAGE_UUID;
    public static final UUID BaseAttackSpeedUUID = BASE_ATTACK_SPEED_UUID;

    public ItemDawnmaker(Tier tier, int attackDamage, float attackSpeed, Properties properties) {
        super(tier, attackDamage, attackSpeed, properties);
    }

    private boolean getMode(ItemStack stack) {
        if (stack == null || !stack.hasTag()) return false;
        return stack.getOrCreateTag().getBoolean("mode");
    }

    public void inventoryTick (ItemStack stack, Level world, Entity entity,int slot, boolean isSelected){
        CompoundTag tag = stack.getTag();
        if (tag == null) return;

        LogicDawnmaker.RunTimer(stack,world,entity,slot,isSelected);
    }

    public void Ultimate(ItemStack stack, Player player) {
        Level world = player.level();
        if (player.getCooldowns().isOnCooldown(this)) return;

        if (!world.isClientSide) {
            CompoundTag tag = stack.getOrCreateTag();
            boolean newMode = !tag.getBoolean("mode");
            tag.putBoolean("mode", newMode);

            String name;

            if (!newMode) {
                name = "ファイノン";

                String PlayerName = player.getName().getString();

                LogicDawnmaker.EffectSkill(
                        player,
                        this,
                        PlayerName + "は再び輪廻に足を踏み入れた・・・",
                        SoundsRegistry.LastAttackSE.get(),
                        "lastattack",
                        240
                );

                tag.putInt(ModelTimer,100);
                tag.putInt(LastAttackTimer,80);
                tag.putInt(LockTimer, 120);
            } else {
                name = "カスライナ";

                LogicDawnmaker.EffectSkill(
                        player,
                        this,
                        "永劫の焼世、背負うべき未来",
                        SoundsRegistry.UltimateSE.get(),
                        "transform",
                        160
                );

                tag.putInt(ModelTimer,28);
                tag.putInt(LockTimer, 120);
                tag.putBoolean("ShowWeapon", true);
                tag.putInt(EffectTImer, 12);
            }
            player.displayClientMessage(Component.literal(name), true);
        }
    }

    public void Skill1(ItemStack stack, Player player) {
        if (!getMode(stack) || player.getCooldowns().isOnCooldown(this)) return;

        CompoundTag tag = stack.getOrCreateTag();
        Level world = player.level();
        if (!world.isClientSide) {

            LogicDawnmaker.EffectSkill(
                    player,
                    this,
                    "災厄・魂命の滅却",
                    SoundsRegistry.Skill1SE.get(),
                    "skill1",
                    120
                    );

            LogicDawnmaker.LogicSkill1(stack, player);

            tag.putInt(LockTimer, 100);
        }
    }

    public void Skill2(ItemStack stack, Player player) {
        if (!getMode(stack) || player.getCooldowns().isOnCooldown(this)) return;

        CompoundTag tag = stack.getOrCreateTag();
        Level world = player.level();
        if (!world.isClientSide) {

            LogicDawnmaker.EffectSkill(
                    player,
                    this,
                    "支柱・死星の天裁",
                    SoundsRegistry.Skill2SE.get(),
                    "skill2",
                    120
            );

            LogicDawnmaker.LogicSkill2(stack, player);

            tag.putInt(LockTimer, 100);
        }
    }
    public void BasicATK(ItemStack stack, Player player) {
        if (!getMode(stack) || player.getCooldowns().isOnCooldown(this)) return;

        CompoundTag tag = stack.getOrCreateTag();
        Level world = player.level();
        if (!world.isClientSide) {
            LogicDawnmaker.EffectSkill(
                    player,
                    this,
                    "創生・血荊の葬送",
                    SoundsRegistry.BasicAttackSE.get(),
                    "basicattack",
                    60
            );
            LogicDawnmaker.LogicBasicATK(stack, player);

            tag.putInt(LockTimer, 60);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> list, TooltipFlag flag) {
        boolean isKhaslana = stack.hasTag() && stack.getTag().getBoolean("mode");

        // モード名の表示（常に表示）
        if (isKhaslana) {
            list.add(Component.literal("§f現在の形態: §eカスライナ").withStyle(ChatFormatting.ITALIC));
        } else {
            list.add(Component.literal("§f現在の形態: §bファイノン").withStyle(ChatFormatting.ITALIC));
        }

        list.add(Component.literal("")); // 空行

        // Shiftキー判定
        if (net.minecraft.client.gui.screens.Screen.hasShiftDown()) {
            list.add(Component.literal("§fキャラクター詳細").withStyle(ChatFormatting.BOLD));

            if (isKhaslana) {
                // カスライナモード中の説明
                list.add(Component.literal("エリュシオンは世界から切り離された辺境の村であり、今では複雑で謎めいた伝説しか残されていない。\n" +
                        "無名の英雄カスライナ、「世負い」の火種を宿す黄金裔。全世界の理想をその胸に刻み、万人の運命を背負い、新たな世界に最初の曙光をもたらす者である\n" +
                        "——「もし黎明が未だ訪れていないというのなら、この身を心火で燃やし尽くし、明日の烈日となろう！」").withStyle(ChatFormatting.GRAY));
            } else {
                // ファイノンモード中の説明
                list.add(Component.literal("エリュシオンは世界から切り離された辺境の村であり、今では複雑で謎めいた伝説しか残されていない。\n" +
                        "無名の英雄█████、「世負い」の火種を宿す黄金裔。全世界の理想をその胸に刻み、万人の運命を背負い、新たな世界に最初の曙光をもたらす者である\n" +
                        "——「もし黎明が未だ訪れていないというのなら、この身を心火で燃やし尽くし、明日の烈日となろう！」").withStyle(ChatFormatting.GRAY));
            }
        } else {
            // 非Shift時のガイド
            list.add(Component.literal("§7[Shiftキーで詳細を表示]").withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        if (slotChanged) return true;

        return oldStack.getOrCreateTag().getBoolean("mode") != newStack.getOrCreateTag().getBoolean("mode");
    }

    // --- GeckoLibの実装 ---
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private DawnmakerRenderer renderer;

            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null) {
                    this.renderer = new DawnmakerRenderer();
                }
                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (slot != EquipmentSlot.MAINHAND) {
            return super.getAttributeModifiers(slot, stack);
        }

        return getMode(stack) ? KHASLANA_MODIFIERS : PHAINON_MODIFIERS;
    }
}