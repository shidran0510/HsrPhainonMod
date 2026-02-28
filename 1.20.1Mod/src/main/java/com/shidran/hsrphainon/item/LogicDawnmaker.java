package com.shidran.hsrphainon.item;

import com.shidran.hsrphainon.client.renderer.EffectRenderer;
import com.shidran.hsrphainon.entity.Skill1Entity;
import com.shidran.hsrphainon.entity.Skill2Entity;
import com.shidran.hsrphainon.network.AnimationPacket;
import com.shidran.hsrphainon.registry.EntityRegistry;
import com.shidran.hsrphainon.registry.PacketRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.shidran.hsrphainon.common.HsrPhainonConstants.*;

public class LogicDawnmaker {
    public static void EffectSkill(Player player, String message, net.minecraft.sounds.SoundEvent sound, String animation, int cooldown, int tick, boolean show) {
        Level world = world(player);
        CompoundTag tag = tag(player);
        Item item = item(player);

        player.sendSystemMessage(Component.literal(message)); //必殺技・発動
        world.playSound(null, player, sound, SoundSource.PLAYERS, 1.0F, 1.0F);
        PacketRegistry.sendToAllClients(new AnimationPacket(player.getUUID(), animation));
        player.getCooldowns().addCooldown(item, cooldown);
        player.fallDistance = 0;
        tag.putBoolean(ShowDawnmaker, show);
        tag.putInt(ModelTimer, tick);
        tag.putInt(LockTimer, tick);
    }

    public static void RunPlayerLock(Player player) {
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
        None("None", (player, stack) -> {}),

        BasicAttack2("BasicAttack2", (player,stack) -> LogicDawnmaker.LogicBasicATK(player, 2)),

        LastAttack("LastAttack", (player,stack) -> player.level().explode(player, player.getX(), player.getY(), player.getZ(),
                LastAttackDamage, false, explosionType)),

        TransFormEffect("TransFormEffect", (player,stack) -> EffectRenderer.startTransformationEffect());

        private final java.util.function.BiConsumer<Player, ItemStack> action;

        Action(String actionId, java.util.function.BiConsumer<Player, ItemStack> actionLogic) {
            this.action = actionLogic;
        }

        public void execute(Player player, ItemStack stack) {
            this.action.accept(player, stack);
        }

        public static void Delay(ItemStack stack, int ticks, Action action) {
            CompoundTag tag = stack.getOrCreateTag();
            tag.putInt(DelayTimer, ticks);
            tag.putString(ActionName, action.name());
        }
    }

    public static void RunTimer(ItemStack stack, Level world, Entity entity) {
        if (world.isClientSide || !(entity instanceof Player player)) return;
        CompoundTag tag = stack.getTag();

        if (tag.contains(LockTimer)) {
            int lockTimer = tag.getInt(LockTimer);


            if (lockTimer > 0) {
                LogicDawnmaker.RunPlayerLock(player);
                tag.putInt(LockTimer, lockTimer - 1);
            } else {
                LogicDawnmaker.StopPlayerLock(player);
                tag.remove(LockTimer);
            }
        }

        if (tag.contains(ModelTimer)) {
            int modelTimer = tag.getInt(ModelTimer);
            if (!tag.getBoolean(ShowDawnmaker)) { tag.putInt(CustomModelData, 33550336); }

            if (modelTimer > 0) {
                tag.putInt(ModelTimer, modelTimer - 1);
            } else {
                if (tag.getBoolean(Mode)) {
                    tag.putInt(CustomModelData, 1); // カスライナのモデルへ
                } else {
                    tag.putInt(CustomModelData, 0); // ファイノンのモデルへ
                }
                tag.putBoolean(ShowDawnmaker,false);
                tag.remove(ModelTimer);
            }
        }

        if (tag.contains(DelayTimer)) {
            int delayTimer = tag.getInt(DelayTimer);

            if (delayTimer > 0) {
                tag.putInt(DelayTimer, delayTimer - 1);
            } else {
                String actionName = tag.getString(ActionName);
                try {
                    Action action = Action.valueOf(actionName);
                    action.execute(player, stack);
                } catch (IllegalArgumentException e) {

                }

                tag.remove(DelayTimer);
                tag.remove(ActionName);
            }
        }
    }

    public static void LogicSkill1(Player player) {
        Level world = player.level();

        float yRot = player.getYRot();
        float f = yRot * ((float) Math.PI / 180F);

        double dxSide = (double) (-net.minecraft.util.Mth.cos(f));
        double dzSide = (double) (-net.minecraft.util.Mth.sin(f));

        double sx = player.getX() + dxSide * 1.5;
        double sy = player.getY() + 10;
        double sz = player.getZ() + dzSide * 1.5;

        Skill1Entity slash = new Skill1Entity(EntityRegistry.SKILL1_ENTITY.get(), world);
        slash.moveTo(sx, sy, sz, player.getYRot(), 90.0F);

        slash.setOwner(player);
        world.addFreshEntity(slash);

    }

    public static void LogicSkill2(Player player) {
        Level world = player.level();
        int amount = Skill2MeteorAmount; // 出したい数
        float yRot = player.getYRot();
        float f = yRot * ((float) Math.PI / 180F);
        double dx = (double) (-net.minecraft.util.Mth.sin(f));
        double dz = (double) net.minecraft.util.Mth.cos(f);

        net.minecraft.world.phys.Vec3 horizontalLook = new net.minecraft.world.phys.Vec3(dx, 0, dz);
        net.minecraft.world.phys.Vec3 sideVec = new net.minecraft.world.phys.Vec3(-dz, 0, dx);

        for (int i = 0; i < amount; i++) {
            Skill2Entity meteor = new Skill2Entity(EntityRegistry.SKILL2_ENTITY.get(), world);

            // ランダムなオフセットを計算 (例: 横幅±5ブロック、前後±3ブロックの範囲)
            double randomSide = (world.random.nextDouble() - 0.5) * Skill2MeteorDensity;
            double randomForward = (world.random.nextDouble() - 0.5) * Skill2MeteorDensity;

            double forwardOffset = 20.0 + randomForward; // 基本の前方距離 8.0
            double heightOffset = 15.0 + (world.random.nextDouble() * 6.0); // 高さもランダムに

            double spawnX = player.getX() + (horizontalLook.x * forwardOffset) + (sideVec.x * randomSide);
            double spawnY = player.getY() + heightOffset;
            double spawnZ = player.getZ() + (horizontalLook.z * forwardOffset) + (sideVec.z * randomSide);

            meteor.moveTo(spawnX, spawnY, spawnZ, player.getYRot(), player.getXRot());
            world.addFreshEntity(meteor);
        }
        Skill2Entity bigMeteor = new Skill2Entity(EntityRegistry.SKILL2_ENTITY.get(), world);

        double bigForwardOffset = 20.0; // 少し遠くに
        double bigHeightOffset = 40.0; // 小さいのより高い位置から

        double bigX = player.getX() + (horizontalLook.x * bigForwardOffset);
        double bigY = player.getY() + bigHeightOffset;
        double bigZ = player.getZ() + (horizontalLook.z * bigForwardOffset);

        bigMeteor.moveTo(bigX, bigY, bigZ, player.getYRot(), player.getXRot());
        bigMeteor.setBig(true);
        world.addFreshEntity(bigMeteor);


    }

    public static void LogicBasicATK(Player player,int combo) {
        int range = 1;
        Level world = player.level();
        if (combo == 1) {

            Vec3 lookVec = player.getLookAngle();
            Vec3 center = player.getEyePosition().add(lookVec.scale(2.0));

            net.minecraft.world.phys.AABB hitBox = new net.minecraft.world.phys.AABB(
                    center.x - range, center.y - range, center.z - range,
                    center.x + range, center.y + range, center.z + range
            );

            world.getEntities(player, hitBox, entity -> entity instanceof net.minecraft.world.entity.LivingEntity).forEach(entity -> {
                if (entity instanceof net.minecraft.world.entity.LivingEntity livingEntity) {

                    livingEntity.hurt(player.damageSources().playerAttack(player), BasicDamage);
                    livingEntity.knockback(0.5, -lookVec.x, -lookVec.z);
                }
            });
        }
        if (combo == 2) {
            // 1. 判定のパラメータ設定
            double radius = 4.5;    // 攻撃の届く距離（半径）
            double angle = 60.0;    // 扇状の広さ（左右30度ずつ、計60度）
            Vec3 center = player.position();
            Vec3 lookVec = player.getLookAngle().normalize(); // プレイヤーの向いている方向

            // 2. まずは大きめの立方体(AABB)で周囲のエンティティをざっくり取得
            net.minecraft.world.phys.AABB area = player.getBoundingBox().inflate(radius, 2.0, radius);

            world.getEntities(player, area, entity -> entity instanceof net.minecraft.world.entity.LivingEntity)
                    .forEach(entity -> {
                        if (entity instanceof net.minecraft.world.entity.LivingEntity livingEntity) {
                            // 3. プレイヤーから敵への方向ベクトルを計算
                            Vec3 toTarget = livingEntity.position().subtract(center).normalize();

                            // 4. ベクトルの内積を使って、射程内かつ扇状の範囲内にいるか判定
                            double dotProduct = lookVec.dot(toTarget); // 2つのベクトルの重なり具合（1.0で完全一致）
                            double cosAngle = Math.cos(Math.toRadians(angle)); // 判定のしきい値

                            // 距離チェック ＆ 角度チェック（内積 > cos(角度) で範囲内）
                            if (player.distanceTo(livingEntity) <= radius && dotProduct > cosAngle) {

                                // 攻撃処理
                                livingEntity.hurt(player.damageSources().playerAttack(player), BasicDamage * 1.5f);

                                // ノックバック（プレイヤーから遠ざける方向）
                                double dX = livingEntity.getX() - player.getX();
                                double dZ = livingEntity.getZ() - player.getZ();
                                livingEntity.knockback(0.8, dX, dZ); // 拡散するように外側へ飛ばす
                            }
                        }
                    });
        }
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class EventHandler {

        @SubscribeEvent
        public static void onPlayerTick(TickEvent.PlayerTickEvent event) {

            if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide) {
                return;
            }

            Player player = event.player;

            boolean hasSword = hasDawnmakerInInventory(player);

            if (!hasSword && player.isNoGravity()) {
                LogicDawnmaker.StopPlayerLock(player);
            }
        }

        @SubscribeEvent
        public static void onLivingDamage(LivingDamageEvent event) {
            if (!(event.getEntity() instanceof Player player)) return;

            ItemStack stack = player.getMainHandItem();
            if (!(stack.getItem() instanceof ItemDawnmaker)) return;

            CompoundTag tag = stack.getOrCreateTag();

            float damage = event.getAmount();

            if (tag.getBoolean(Mode) && damage >= player.getHealth()) {
                event.setCanceled(true);

                player.setHealth(player.getMaxHealth()); // ハート1個分で復活
                player.removeAllEffects(); // デバフ解除

                if (stack.getItem() instanceof ItemDawnmaker dawnmaker) {
                    dawnmaker.Ultimate(stack, player);

                    tag.putBoolean(Mode, false);
                }
            }
        }

        private static boolean hasDawnmakerInInventory(Player player) {
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack stack = player.getInventory().getItem(i);
                if (!stack.isEmpty() && stack.getItem() instanceof ItemDawnmaker) {
                    return true;
                }
            }
            return false;
        }
    }
}
