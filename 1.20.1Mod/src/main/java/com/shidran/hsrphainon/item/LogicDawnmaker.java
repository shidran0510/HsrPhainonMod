package com.shidran.hsrphainon.item;

import com.shidran.hsrphainon.client.renderer.EffectRenderer;
import com.shidran.hsrphainon.entity.Skill1Entity;
import com.shidran.hsrphainon.entity.Skill2Entity;
import com.shidran.hsrphainon.network.AnimationPacket;
import com.shidran.hsrphainon.network.LockTimerPacket;
import com.shidran.hsrphainon.registry.EntityRegistry;
import com.shidran.hsrphainon.registry.PacketRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import static com.shidran.hsrphainon.common.HsrPhainonConstants.*;

public class LogicDawnmaker {
    public static void EffectSkill(Player player, String message, SoundEvent sound, String animation, int cooldown, int tick) {
        Level world = world(player);
        CompoundTag playerTag = tagPlayerData(player);
        CompoundTag itemTag = tagMainHand(player);
        Item item = item(player);
        Object PlayerName = player.getName().getString();

        player.sendSystemMessage(Component.translatable((message), PlayerName));
        world.playSound(null, player, sound, SoundSource.PLAYERS, 1.0F, 1.0F);
        PacketRegistry.sendToAllClients(new AnimationPacket(player.getUUID(), animation));
        player.getCooldowns().addCooldown(item, cooldown);
        player.fallDistance = 0;
        itemTag.putInt(ModelTimer, tick);
        playerTag.putInt(LockTimer, tick);
    }

    public static void RunPlayerLock(Player player) {
        CompoundTag playerTag = tagPlayerData(player);

        playerTag.putDouble("LockX", player.getX());
        playerTag.putDouble("LockY", player.getY());
        playerTag.putDouble("LockZ", player.getZ());

        player.teleportTo(playerTag.getDouble("LockX"), playerTag.getDouble("LockY"), playerTag.getDouble("LockZ"));
        player.setDeltaMovement(Vec3.ZERO);
        player.setNoGravity(true);
        player.setInvulnerable(true);
        player.hurtMarked = true;
    }

    public static void StopPlayerLock(Player player) {
        player.setNoGravity(false);
        player.setInvulnerable(false);
        player.hurtMarked = false;
    }

    public enum Action {
        None((player, stack) -> {
        }),

        BasicAttack2((player, stack) -> LogicDawnmaker.LogicBasicATK(player, 2)),

        LastAttack((player, stack) -> world(player).explode(player, player.getX(), player.getY(), player.getZ(),
                getLastAttackDamage(), false, getExplosionType())),

        TransFormEffect((player, stack) -> EffectRenderer.startTransformationEffect()),

        Skill(((player, stack) -> LogicDawnmaker.LogicSkill(player)));

        private final java.util.function.BiConsumer<Player, ItemStack> action;

        Action(java.util.function.BiConsumer<Player, ItemStack> actionLogic) {
            this.action = actionLogic;
        }

        public void execute(Player player, ItemStack stack) {
            this.action.accept(player, stack);
        }

        public static void Delay(Player player, int ticks, Action action) {
            CompoundTag tag = tagPlayerData(player);
            tag.putInt(DelayTimer, ticks);
            tag.putString(ActionName, action.name());
        }
    }

    public static void RunTimer(Player player) {
        CompoundTag playerTag = tagPlayerData(player);
        CompoundTag itemTag = tagMainHand(player);

        if (playerTag.contains(LockTimer)) {
            int lockTimer = playerTag.getInt(LockTimer);
            int nextTick = Math.max(0, lockTimer - 1);

            if (lockTimer > 0) {
                LogicDawnmaker.RunPlayerLock(player);
                playerTag.putInt(LockTimer, nextTick);
            } else {
                LogicDawnmaker.StopPlayerLock(player);
                if (!player.level().isClientSide && player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
                    PacketRegistry.sendToPlayer(new LockTimerPacket(0), serverPlayer);
                }
                playerTag.remove(LockTimer);
            }
            if (!player.level().isClientSide && player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
                PacketRegistry.sendToPlayer(new LockTimerPacket(nextTick), serverPlayer);
            }
        }

        if (itemTag.contains(ModelTimer)) {
            int modelTimer = itemTag.getInt(ModelTimer);

            if (modelTimer > 0) {
                itemTag.putInt(ModelTimer, modelTimer - 1);
            } else {
                if (itemTag.getBoolean(Mode)) {
                    itemTag.putInt(CustomModelData, 1);
                } else {
                    itemTag.putInt(CustomModelData, 0);
                }
                itemTag.remove(ModelTimer);
            }
        }

        if (playerTag.contains(DelayTimer)) {
            int delayTimer = playerTag.getInt(DelayTimer);

            if (delayTimer > 0) {
                playerTag.putInt(DelayTimer, delayTimer - 1);
            } else {
                String actionName = playerTag.getString(ActionName);
                try {
                    Action action = Action.valueOf(actionName);
                    action.execute(player, player.getMainHandItem());
                } catch (IllegalArgumentException e) {
                    return;
                }

                playerTag.remove(DelayTimer);
                playerTag.remove(ActionName);
            }
        }
    }

    public static void LogicSkill1(Player player) {
        Level world = world(player);

        float yRot = player.getYRot();
        float f = yRot * ((float) Math.PI / 180F);

        double dxSide = -net.minecraft.util.Mth.cos(f);
        double dzSide = -net.minecraft.util.Mth.sin(f);

        double sx = player.getX() + dxSide * 1.5;
        double sy = player.getY() + 10;
        double sz = player.getZ() + dzSide * 1.5;

        Skill1Entity slash = new Skill1Entity(EntityRegistry.SKILL1_ENTITY.get(), world);
        slash.moveTo(sx, sy, sz, player.getYRot(), 90.0F);

        slash.setOwner(player);
        world.addFreshEntity(slash);

    }

    public static void LogicSkill2(Player player) {
        Level world = world(player);
        float yRot = player.getYRot();
        float f = yRot * ((float) Math.PI / 180F);
        double dx = -net.minecraft.util.Mth.sin(f);
        double dz = net.minecraft.util.Mth.cos(f);

        net.minecraft.world.phys.Vec3 horizontalLook = new net.minecraft.world.phys.Vec3(dx, 0, dz);
        net.minecraft.world.phys.Vec3 sideVec = new net.minecraft.world.phys.Vec3(-dz, 0, dx);

        for (int i = 0; i < getSkill2MeteorAmount(); i++) {
            Skill2Entity meteor = new Skill2Entity(EntityRegistry.SKILL2_ENTITY.get(), world);

            double randomSide = (world.random.nextDouble() - 0.5) * getSkill2MeteorHorizontalRange();
            double randomForward = (world.random.nextDouble() - 0.5) * getSkill2MeteorHorizontalRange();

            double forwardOffset = 20.0 + randomForward;
            double heightOffset = 15.0 + (world.random.nextDouble() * getSkill2MeteorVerticalRange());

            double spawnX = player.getX() + (horizontalLook.x * forwardOffset) + (sideVec.x * randomSide);
            double spawnY = player.getY() + heightOffset;
            double spawnZ = player.getZ() + (horizontalLook.z * forwardOffset) + (sideVec.z * randomSide);

            meteor.moveTo(spawnX, spawnY, spawnZ, player.getYRot(), player.getXRot());
            world.addFreshEntity(meteor);
        }
        Skill2Entity bigMeteor = new Skill2Entity(EntityRegistry.SKILL2_ENTITY.get(), world);

        double bigForwardOffset = 20.0;
        double bigHeightOffset = getSkill2MeteorVerticalRange() + 30.0;

        double bigX = player.getX() + (horizontalLook.x * bigForwardOffset);
        double bigY = player.getY() + bigHeightOffset;
        double bigZ = player.getZ() + (horizontalLook.z * bigForwardOffset);

        bigMeteor.moveTo(bigX, bigY, bigZ, player.getYRot(), player.getXRot());
        bigMeteor.setBig(true);
        world.addFreshEntity(bigMeteor);


    }

    public static void LogicBasicATK(Player player, int combo) {
        int range = 1;
        Level world = world(player);
        if (combo == 1) {

            Vec3 lookVec = player.getLookAngle();
            Vec3 center = player.getEyePosition().add(lookVec.scale(2.0));

            net.minecraft.world.phys.AABB hitBox = new net.minecraft.world.phys.AABB(
                    center.x - range, center.y - range, center.z - range,
                    center.x + range, center.y + range, center.z + range
            );

            world.getEntities(player, hitBox, entity -> entity instanceof net.minecraft.world.entity.LivingEntity).forEach(entity -> {
                if (entity instanceof net.minecraft.world.entity.LivingEntity livingEntity) {

                    livingEntity.hurt(player.damageSources().playerAttack(player), getBasicATKDamage());
                    livingEntity.knockback(0.5, -lookVec.x, -lookVec.z);
                }
            });
        }
        if (combo == 2) {
            double radius = 4.5;
            double angle = 60.0;
            Vec3 center = player.position();
            Vec3 lookVec = player.getLookAngle().normalize();

            net.minecraft.world.phys.AABB area = player.getBoundingBox().inflate(radius, 2.0, radius);

            world.getEntities(player, area, entity -> entity instanceof net.minecraft.world.entity.LivingEntity)
                    .forEach(entity -> {
                        if (entity instanceof net.minecraft.world.entity.LivingEntity livingEntity) {

                            Vec3 toTarget = livingEntity.position().subtract(center).normalize();

                            double dotProduct = lookVec.dot(toTarget);
                            double cosAngle = Math.cos(Math.toRadians(angle));

                            if (player.distanceTo(livingEntity) <= radius && dotProduct > cosAngle) {

                                livingEntity.hurt(player.damageSources().playerAttack(player), getBasicATKDamage() * 1.5f);

                                double dX = livingEntity.getX() - player.getX();
                                double dZ = livingEntity.getZ() - player.getZ();
                                livingEntity.knockback(0.8, dX, dZ);
                            }
                        }
                    });
        }
    }

    public static void LogicSkill(Player player) {
        Level world = world(player);
        Vec3 pos = player.position();

        float yRot = player.getYRot();
        float f = yRot * ((float) Math.PI / 180F);
        double dx = -net.minecraft.util.Mth.sin(f);
        double dz = net.minecraft.util.Mth.cos(f);
        Vec3 horizontalLook = new Vec3(dx, 0, dz).normalize();

        Vec3 rightVec = new Vec3(-dz, 0, dx).normalize();

        Vec3 boxCenter = pos.add(horizontalLook.scale(1.0)).add(0, 1.0, 0);

        double sideWidth = 1.5;
        net.minecraft.world.phys.AABB hitBox = new net.minecraft.world.phys.AABB(
                boxCenter.x - sideWidth, boxCenter.y - 1.0, boxCenter.z - sideWidth,
                boxCenter.x + sideWidth, boxCenter.y + 1.0, boxCenter.z + sideWidth
        );
        world.getEntities(player, hitBox, entity -> entity instanceof net.minecraft.world.entity.LivingEntity)
                .forEach(entity -> {
                    if (entity instanceof net.minecraft.world.entity.LivingEntity livingEntity) {

                        Vec3 toTarget = livingEntity.position().subtract(pos);
                        Vec3 toTargetHorizontal = new Vec3(toTarget.x, 0, toTarget.z).normalize();

                        double dotProduct = horizontalLook.dot(toTargetHorizontal);
                        if (dotProduct > Math.cos(Math.toRadians(60.0))) {

                            livingEntity.hurt(player.damageSources().playerAttack(player), getSkillDamage());

                            double strength = 0.6;
                            Vec3 knockbackDir = horizontalLook.add(rightVec.scale(0.8)).normalize();
                            livingEntity.setDeltaMovement(knockbackDir.scale(strength));
                        }
                    }
                });
    }

}
