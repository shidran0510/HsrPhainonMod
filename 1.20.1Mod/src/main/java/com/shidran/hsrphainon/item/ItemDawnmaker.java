package com.shidran.hsrphainon.item;

import com.google.common.collect.Multimap;
import com.shidran.hsrphainon.common.HsrPhainonConstants;
import com.shidran.hsrphainon.registry.SoundsRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

import static com.shidran.hsrphainon.common.HsrPhainonConstants.*;
import static com.shidran.hsrphainon.item.LogicDawnmaker.Action.*;

public class ItemDawnmaker extends SwordItem {

    public static final UUID BaseAttackDmageUUID = BASE_ATTACK_DAMAGE_UUID;
    public static final UUID BaseAttackSpeedUUID = BASE_ATTACK_SPEED_UUID;

    public ItemDawnmaker(Tier tier, int attackDamage, float attackSpeed, Properties properties) {
        super(tier, attackDamage, attackSpeed, properties);
    }

    private boolean getMode(ItemStack stack) {
        if (stack == null || !stack.hasTag()) return false;
        return stack.getOrCreateTag().getBoolean(Mode);
    }

    public void inventoryTick (ItemStack stack,@Nonnull Level world,@Nonnull Entity entity, int slot, boolean isSelected){
        CompoundTag tag = tag(stack);
        if (tag == null) return;

        LogicDawnmaker.RunTimer(stack,world,entity);
    }

    public void Ultimate(ItemStack stack, Player player) {
        if (player.getCooldowns().isOnCooldown(this)) return;
        CompoundTag tag = tag(player);
        Level world = world(player);

        if (!world.isClientSide) {
            boolean newMode = !tag.getBoolean(Mode);
            tag.putBoolean(Mode, newMode);

            String PlayerName = player.getName().getString();
            int LockTick = !newMode ? 100 : 130;

            if (!newMode) {
                LogicDawnmaker.EffectSkill(
                        player,
                        PlayerName + "は再び輪廻に足を踏み入れた・・・",
                        SoundsRegistry.LastAttackSE.get(),
                        "lastattack",
                        240,
                        100,
                        false
                );
                Delay(stack, 80, LastAttack);
            } else {
                LogicDawnmaker.EffectSkill(
                        player,
                        "永劫の焼世、背負うべき未来",
                        SoundsRegistry.UltimateSE.get(),
                        "transform",
                        160,
                        28,
                        true

                );
                Delay(stack, 12, TransFormEffect);
            }
            tag.putInt(LockTimer, LockTick);
        }
    }

    public void Skill1(ItemStack stack, Player player) {
        if (!getMode(stack) || player.getCooldowns().isOnCooldown(this)) return;
        Level world = world(player);

        if (!world.isClientSide) {

            LogicDawnmaker.EffectSkill(
                    player,
                    "災厄・魂命の滅却",
                    SoundsRegistry.Skill1SE.get(),
                    "skill1",
                    120,
                    100,
                    false
            );

            LogicDawnmaker.LogicSkill1(player);
        }
    }

    public void Skill2(ItemStack stack, Player player) {
        if (!getMode(stack) || player.getCooldowns().isOnCooldown(this)) return;
        Level world = world(player);

        if (!world.isClientSide) {

            LogicDawnmaker.EffectSkill(
                    player,
                    "支柱・死星の天裁",
                    SoundsRegistry.Skill2SE.get(),
                    "skill2",
                    120,
                    100,
                    false
            );

            LogicDawnmaker.LogicSkill2(player);
        }
    }

    public void BasicATK(ItemStack stack, Player player) {
        if (!getMode(stack) || player.getCooldowns().isOnCooldown(this)) return;
        Level world = world(player);

        if (!world.isClientSide) {
            LogicDawnmaker.EffectSkill(
                    player,
                    "創生・血荊の葬送",
                    SoundsRegistry.BasicAttackSE.get(),
                    "basicattack",
                    60,
                    60,
                    true
            );
            LogicDawnmaker.LogicBasicATK(player,1);
            LogicDawnmaker.Action.Delay(stack, 40, BasicAttack2);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world,@Nonnull List<Component> list,@Nonnull TooltipFlag flag) {
        // モード名の表示（常に表示）
        if (stack.hasTag() && stack.getTag().getBoolean(Mode)) {
            list.add(Component.literal("§f現在の形態: §eカスライナ").withStyle(ChatFormatting.ITALIC));
        } else {
            list.add(Component.literal("§f現在の形態: §bファイノン").withStyle(ChatFormatting.ITALIC));
        }

        list.add(Component.literal("")); // 空行

        // Shiftキー判定
        if (net.minecraft.client.gui.screens.Screen.hasShiftDown()) {
            list.add(Component.literal("§fキャラクター詳細").withStyle(ChatFormatting.BOLD));

            String trueName = stack.getTag().getBoolean(Mode) ? "カスライナ" : "█████";

            list.add(Component.literal("""
            エリュシオンは世界から切り離された辺境の村であり、今では複雑で謎めいた伝説しか残されていない。
            無名の英雄%s、「世負い」の火種を宿す黄金裔。全世界の理想をその胸に刻み、万人の運命を背負い、新たな世界に最初の曙光をもたらす者である
            ——「もし黎明が未だ訪れていないというのなら、この身を心火で燃やし尽くし、明日の烈日となろう！」
            """.formatted(trueName)).withStyle(ChatFormatting.GRAY));

        } else {
            // 非Shift時のガイド
            list.add(Component.literal("§7[Shiftキーで詳細を表示]").withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        if (slotChanged) return true;

        return oldStack.getOrCreateTag().getBoolean(Mode) != newStack.getOrCreateTag().getBoolean(Mode);
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (slot != EquipmentSlot.MAINHAND) {
            return super.getAttributeModifiers(slot, stack);
        }

        return getMode(stack) ? KHASLANA_MODIFIERS : PHAINON_MODIFIERS;
    }
}

//geckolib ここから
//    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
//
//    @Override
//    public AnimatableInstanceCache getAnimatableInstanceCache() {
//        return this.cache;
//    }
//
//    @Override
//    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
//    }
//
//    @Override
//    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
//        consumer.accept(new IClientItemExtensions() {
//            private DawnmakerRenderer renderer;
//
//            @Override
//            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
//                if (this.renderer == null)
//                    this.renderer = new DawnmakerRenderer(); // ここで作ったレンダラーを指定
//                return this.renderer;
//            }
//        });
//    }
// ここまで