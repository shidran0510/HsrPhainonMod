package com.shidran.hsrphainon.item;

import com.google.common.collect.Multimap;
import com.shidran.hsrphainon.client.renderer.DawnmakerRenderer;
import com.shidran.hsrphainon.common.HsrPhainonConstants;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import static com.shidran.hsrphainon.common.HsrPhainonConstants.*;
import static com.shidran.hsrphainon.item.LogicDawnmaker.Action.*;

public class ItemDawnmaker extends SwordItem implements GeoItem {

    public static final UUID BaseAttackDmageUUID = BASE_ATTACK_DAMAGE_UUID;
    public static final UUID BaseAttackSpeedUUID = BASE_ATTACK_SPEED_UUID;

    public ItemDawnmaker(Tier tier, int attackDamage, float attackSpeed, Properties properties) {
        super(tier, attackDamage, attackSpeed, properties);
    }

    public void inventoryTick(@NotNull ItemStack stack, @Nonnull Level world, @Nonnull Entity entity, int slot, boolean isSelected) {
        if (!world.isClientSide && entity instanceof Player player) {
            CompoundTag playerTag = tagPlayerData(player);
            CompoundTag itemTag = tagItemStack(stack);

            if (!itemTag.contains(ModelTimer)) {
                if (itemTag.getInt(CustomModelData) == 2) {
                    if (itemTag.getBoolean(Mode)) {
                        itemTag.putInt(CustomModelData, 1);
                    } else {
                        itemTag.putInt(CustomModelData, 0);
                    }
                }
            }
        }
    }

    public void Ultimate(ItemStack stack, Player player) {
        if (player.getCooldowns().isOnCooldown(this)) return;
        Level world = world(player);
        CompoundTag itemTag = tagMainHand(player);
        CompoundTag playerTag = tagPlayerData(player);

        if (!world.isClientSide) {
            boolean newMode = !itemTag.getBoolean(Mode);
            itemTag.putBoolean(Mode, newMode);

            int LockTick = newMode ? 130 : 100;

            if (itemTag.getBoolean(Mode)) {
                LogicDawnmaker.EffectSkill(
                        player,
                        "skill.hsrphainon.ultimate.start",
                        SoundsRegistry.UltimateSE.get(),
                        "transform",
                        160, 28

                );
                Delay(player, 12, TransFormEffect);
            } else {
                LogicDawnmaker.EffectSkill(
                        player,
                        "skill.hsrphainon.ultimate.end",
                        SoundsRegistry.LastAttackSE.get(),
                        "lastattack",
                        240, 100
                );
                itemTag.putInt(CustomModelData, 2);
                Delay(player, 80, LastAttack);
            }
            playerTag.putInt(LockTimer, LockTick);
        }
    }

    public void Skill1(ItemStack stack, Player player) {
        if (player.getCooldowns().isOnCooldown(this)) return;
        Level world = world(player);
        CompoundTag itemTag = tagMainHand(player);
        CompoundTag playerTag = tagPlayerData(player);

        if (!world.isClientSide) {
            if (itemTag.getBoolean(Mode)) {

                LogicDawnmaker.EffectSkill(
                        player,
                        "skill.hsrphainon.skill1.name",
                        SoundsRegistry.Skill1SE.get(),
                        "skill1",
                        120, 90
                );
                itemTag.putInt(CustomModelData, 2);
                LogicDawnmaker.LogicSkill1(player);
            } else {
                LogicDawnmaker.EffectSkill(
                        player,
                        "skill.hsrphainon.skill.name",
                        SoundsRegistry.SkillSE.get(),
                        "skill",
                        60, 17
                );
                Delay(player, 40, Skill);
                itemTag.putInt(CustomModelData,3);
                playerTag.putInt(LockTimer, 50);
            }
        }
    }

    public void Skill2(ItemStack stack, Player player) {
        if (player.getCooldowns().isOnCooldown(this)) return;
        Level world = world(player);
        CompoundTag itemTag = tagMainHand(player);

        if (!world.isClientSide) {
            if (itemTag.getBoolean(Mode)) {
                LogicDawnmaker.EffectSkill(
                        player,
                        "skill.hsrphainon.skill2.name",
                        SoundsRegistry.Skill2SE.get(),
                        "skill2",
                        120, 80
                );
                itemTag.putInt(CustomModelData, 2);
                LogicDawnmaker.LogicSkill2(player);
            }
        }
    }

    public void BasicATK(ItemStack stack, Player player) {
        if (player.getCooldowns().isOnCooldown(this)) return;
        Level world = world(player);
        CompoundTag itemTag = tagMainHand(player);

        if (!world.isClientSide) {
            if (itemTag.getBoolean(Mode)) {
                LogicDawnmaker.EffectSkill(
                        player,
                        "skill.hsrphainon.basic_atk.name",
                        SoundsRegistry.BasicAttackSE.get(),
                        "basicattack",
                        60, 60
                );
                LogicDawnmaker.LogicBasicATK(player, 1);
                Delay(player, 40, BasicAttack2);
            }
        }
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, Level world, @Nonnull List<Component> list, @Nonnull TooltipFlag flag) {
        CompoundTag tag = tagItemStack(stack);

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
            return tagItemStack(stack).getBoolean(Mode) ?
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
    @Override //火炎耐性
    public boolean isFireResistant() {
        return true;
    }
    @Override
    public boolean onEntityItemUpdate(ItemStack stack, net.minecraft.world.entity.item.ItemEntity entity) {
        entity.setInvulnerable(true);

        CompoundTag itemTag = stack.getTag();
        if (itemTag != null && itemTag.getInt(CustomModelData) == 2) {
            itemTag.remove(ModelTimer);
            itemTag.putInt(CustomModelData, itemTag.getBoolean(Mode) ? 1 : 0);
        }
        return false;
    }

//Geckolib
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, event ->
                PlayState.STOP));
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private DawnmakerRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null) {
                    this.renderer = new DawnmakerRenderer();
                }
                return this.renderer;
            }
        });
    }

    private ItemStack renderingStack;

    public void setRenderingStack(ItemStack stack) {
        this.renderingStack = stack;
    }

    public ItemStack getRenderingStack() {
        return this.renderingStack;
    }
}
