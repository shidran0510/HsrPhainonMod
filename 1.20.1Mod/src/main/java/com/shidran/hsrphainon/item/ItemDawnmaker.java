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
import org.jetbrains.annotations.NotNull;

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
        CompoundTag tag = tag(stack);
        return tag.getBoolean(Mode);
    }

    public void inventoryTick(@NotNull ItemStack stack, @Nonnull Level world, @Nonnull Entity entity, int slot, boolean isSelected) {
        CompoundTag tag = tag(stack);

        LogicDawnmaker.RunTimer(stack, world, entity);
    }

    public void Ultimate(ItemStack stack, Player player) {
        if (player.getCooldowns().isOnCooldown(this)) return;
        CompoundTag tag = tag(player);
        Level world = world(player);

        if (!world.isClientSide) {
            boolean newMode = !tag.getBoolean(Mode);
            tag.putBoolean(Mode, newMode);


            int LockTick = !newMode ? 100 : 130;

            if (!newMode) {
                LogicDawnmaker.EffectSkill(
                        player,
                        "skill.hsrphainon.ultimate.end",
                        SoundsRegistry.LastAttackSE.get(),
                        "lastattack",
                        240, 100, false
                );
                Delay(stack, 80, LastAttack);
            } else {
                LogicDawnmaker.EffectSkill(
                        player,
                        "skill.hsrphainon.ultimate.start",
                        SoundsRegistry.UltimateSE.get(),
                        "transform",
                        160, 28, true

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
                    "skill.hsrphainon.skill1.name",
                    SoundsRegistry.Skill1SE.get(),
                    "skill1",
                    120, 100, false
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
                    "skill.hsrphainon.skill2.name",
                    SoundsRegistry.Skill2SE.get(),
                    "skill2",
                    120, 100, false
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
                    "skill.hsrphainon.basic_atk.name",
                    SoundsRegistry.BasicAttackSE.get(),
                    "basicattack",
                    60, 60, true
            );
            LogicDawnmaker.LogicBasicATK(player, 1);
            LogicDawnmaker.Action.Delay(stack, 40, BasicAttack2);
        }
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, Level world, @Nonnull List<Component> list, @Nonnull TooltipFlag flag) {
        CompoundTag tag = tag(stack);

        String modeKey = (stack.hasTag() && tag.getBoolean(Mode)) ?
                "message.hsrphainon.mode_change.khaslana" : "message.hsrphainon.mode_change.phainon";
        list.add(Component.translatable(modeKey).withStyle(ChatFormatting.ITALIC));

        list.add(Component.literal(""));

        if (net.minecraft.client.gui.screens.Screen.hasShiftDown()) {
            list.add(Component.translatable("message.hsrphainon.details_title").withStyle(ChatFormatting.BOLD));

            String nameKey = tag.getBoolean(Mode) ? "name.hsrphainon.khaslana" : "name.hsrphainon.nameless";

            Component trueName = Component.translatable(nameKey);

            list.add(Component.translatable("message.hsrphainon.description", trueName).withStyle(ChatFormatting.GRAY));
        } else {
            list.add(Component.translatable("message.hsrphainon.shift_hint").withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        if (slotChanged) return true;
        return oldStack.getOrCreateTag().getBoolean(Mode) != newStack.getOrCreateTag().getBoolean(Mode);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (slot == EquipmentSlot.MAINHAND) {
            return tag(stack).getBoolean(Mode) ?
                    HsrPhainonConstants.getKhaslanaModifiers() :
                    HsrPhainonConstants.getPhainonModifiers();
        }
        return super.getAttributeModifiers(slot, stack);
    }

    @Override //耐久値無限
    public boolean canBeDepleted() {
        return false;
    }
    @Override //エンチャント許可
    public boolean isEnchantable(@Nonnull ItemStack stack) {
        return true;
    }
    @Override //アイテム破壊無効
    public boolean onEntityItemUpdate(ItemStack stack, net.minecraft.world.entity.item.ItemEntity entity) {
        entity.setInvulnerable(true);return false;
    }
    @Override //火炎耐性
    public boolean isFireResistant() {
        return true;
    }
}
