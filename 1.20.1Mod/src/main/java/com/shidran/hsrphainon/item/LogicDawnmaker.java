package com.shidran.hsrphainon.item;

import com.shidran.hsrphainon.HsrPhainon;
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
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.shidran.hsrphainon.common.HsrPhainonConstants.*;

public class LogicDawnmaker {
    public static void EffectSkill(Player player, Item item, String message, net.minecraft.sounds.SoundEvent sound, String animation, int cooldown) {
        Level world = player.level();

        player.sendSystemMessage(Component.literal(message)); //必殺技・発動
        world.playSound(null, player, sound, SoundSource.PLAYERS, 1.0F, 1.0F);
        PacketRegistry.sendToAllClients(new AnimationPacket(player.getUUID(), animation));
        player.getCooldowns().addCooldown(item, cooldown);
        player.fallDistance = 0;
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

    public static void RunTimer(ItemStack stack, Level world, Entity entity, int slot, boolean isSelected) {
        if (world.isClientSide || !(entity instanceof Player player)) return;
        CompoundTag tag = stack.getTag();

        if (tag.contains(LockTimer)) { //ロック中にアイテムを失うと物理状態の変更が永続する
            int lockTimer = tag.getInt(LockTimer);

            if (lockTimer > 0) {
                LogicDawnmaker.RunPlayerLock(player);
                tag.putInt(LockTimer, lockTimer - 1);
            } else {
                tag.remove(LockTimer);
                tag.remove("ShowWeapon");
                LogicDawnmaker.StopPlayerLock(player);
            }
        }

        if (tag.contains(EffectTImer)) {
            int EffectTimer = tag.getInt(EffectTImer);

            if (EffectTimer > 0) {
                tag.putInt(EffectTImer, EffectTimer - 1);
            } else {
                EffectRenderer.startTransformationEffect();
                tag.remove(EffectTImer);
            }
        }

        if (tag.contains(LastAttackTimer)) {
            int Skill2Timer = tag.getInt(LastAttackTimer);

            if (Skill2Timer > 0) {
                tag.putInt(LastAttackTimer, Skill2Timer - 1);
            } else {
                world.explode(player, player.getX(), player.getY(), player.getZ(),
                        LastAttackDamage, false, explosionType);
                tag.remove(LastAttackTimer);
            }
        }

        if (tag.contains(ModelTimer)) {
            int Skill2Timer = tag.getInt(ModelTimer);

            if (Skill2Timer > 0) {
                tag.putInt(ModelTimer, Skill2Timer - 1);
            } else {
                if (tag.getBoolean("mode")) {
                    tag.putInt("CustomModelData", 1); // カスライナのモデルへ
                } else {
                    tag.putInt("CustomModelData", 0); // ファイノンのモデルへ
                }

                tag.remove(ModelTimer);
            }
        }

    }

    public static void LogicSkill1(ItemStack stack, Player player) {
        Level world = player.level();

        float yRot = player.getYRot();
        float f = yRot * ((float) Math.PI / 180F);

        double dxSide = (double) (-net.minecraft.util.Mth.cos(f));
        double dzSide = (double) (-net.minecraft.util.Mth.sin(f));

        double sx = player.getX() + dxSide * 2.0;
        double sy = player.getY() + 10;
        double sz = player.getZ() + dzSide * 2.0;

        Skill1Entity slash = new Skill1Entity(EntityRegistry.SKILL1_ENTITY.get(), world);
        slash.moveTo(sx, sy, sz, player.getYRot(), 90.0F);

        slash.setOwner(player);
        world.addFreshEntity(slash);

    }

    public static void LogicSkill2(ItemStack stack, Player player) {
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

    public static void LogicBasicATK(ItemStack stack, Player player) {
        Level world = player.level();
        int range = 1;

        // 1. プレイヤーの視線方向（ベクトル）を取得
        Vec3 lookVec = player.getLookAngle();

        // 2. 正面2ブロック先の中心座標を計算
        // プレイヤーの目の位置 (getEyePosition) から視線方向に2.0倍した位置
        Vec3 center = player.getEyePosition().add(lookVec.scale(2.0));

        // 3. 中心座標から指定したレンジ（半径）のAABB（当たり判定箱）を作成
        net.minecraft.world.phys.AABB hitBox = new net.minecraft.world.phys.AABB(
                center.x - range, center.y - range, center.z - range,
                center.x + range, center.y + range, center.z + range
        );

        // 4. 範囲内のエンティティ（自分以外）を取得して処理
        world.getEntities(player, hitBox, entity -> entity instanceof net.minecraft.world.entity.LivingEntity).forEach(entity -> {
            if (entity instanceof net.minecraft.world.entity.LivingEntity livingEntity) {
                // ダメージを与える
                livingEntity.hurt(player.damageSources().playerAttack(player), BasicDamage);

                // 必要であればノックバックなどの追加効果
                livingEntity.knockback(0.5, -lookVec.x, -lookVec.z);
            }
        });
    }

    @Mod.EventBusSubscriber(modid = HsrPhainon.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class EventHandler {

        @SubscribeEvent
        public static void onPlayerTick(TickEvent.PlayerTickEvent event) {

            if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide) {
                return;
            }

            Player player = event.player;

            boolean hasSword = hasDawnmakerInInventory(player);

            if (!hasSword && player.isNoGravity()) {
                player.setNoGravity(false);
                player.setInvulnerable(false);
                player.hurtMarked = false;
            }
        }

        @SubscribeEvent
        public static void onPlayerDeath(LivingDeathEvent event) {
            if (!(event.getEntity() instanceof Player player)) return;

            // メインハンドのアイテムを確認
            ItemStack stack = player.getMainHandItem();
            if (!(stack.getItem() instanceof ItemDawnmaker sword)) return;

            CompoundTag tag = stack.getOrCreateTag();

            // カスライナ形態（mode == true）かチェック
            if (tag.getBoolean("mode")) {
                // 1. 死亡イベントをキャンセルして復活させる
                event.setCanceled(true);

                // 2. 体力と状態のリセット（トーテムのような挙動）
                player.setHealth(2.0F); // ハート1個分で復活
                player.removeAllEffects(); // デバフ解除
                // 必要に応じて回復や耐性のエフェクトを付与
                // player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));

                if (stack.getItem() instanceof ItemDawnmaker dawnmaker) {
                    dawnmaker.Ultimate(stack, player);
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
