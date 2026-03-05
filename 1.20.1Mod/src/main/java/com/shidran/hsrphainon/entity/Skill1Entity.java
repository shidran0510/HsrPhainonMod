package com.shidran.hsrphainon.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nonnull;
import java.util.List;

import static com.shidran.hsrphainon.common.HsrPhainonConstants.*;

public class Skill1Entity extends Entity implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public Skill1Entity(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        double d0 = 64.0 * 64.0;
        return distance < d0 * 4.0;
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void addAdditionalSaveData(@Nonnull CompoundTag tag) {
    }

    @Override
    protected void readAdditionalSaveData(@Nonnull CompoundTag tag) {
    }

    @Override @Nonnull
    public net.minecraft.world.phys.AABB getBoundingBoxForCulling() {

        return this.getBoundingBox().inflate(30.0D);
    }

    @Override @Nonnull
    public net.minecraft.network.protocol.Packet<net.minecraft.network.protocol.game.ClientGamePacketListener> getAddEntityPacket() {
        return net.minecraftforge.network.NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    private Player owner;

    public void setOwner(Player player) {
        this.owner = player;
    }

    private static final float START_PITCH = 90.0F; //出現時の角度
    private static final float END_PITCH = 0.0F; //目標ピッチ角
    private static final int ROTATION_DURATION = 100; //何秒かけて回転させるか

    @Override
    public void tick() {

        this.xRotO = this.getXRot();
        this.yRotO = this.getYRot();

        if (this.tickCount <= ROTATION_DURATION) {
            float progress = (float) this.tickCount / (float) ROTATION_DURATION;
            float currentPitch = START_PITCH - (progress * (START_PITCH - END_PITCH));
            this.setXRot(currentPitch);
        }

        if (!this.level().isClientSide) {

            AABB hitBox = getHitBox();

            List<LivingEntity> targets = this.level().getEntitiesOfClass(LivingEntity.class, hitBox, e -> e != owner);
            for (LivingEntity target : targets) {
                target.hurt(this.damageSources().playerAttack(owner), getSkill1Damage());
            }

            if (owner != null) {

                CompoundTag playerTag = owner.getPersistentData();

                if (playerTag.contains(LockTimer)) {
                    if (playerTag.getInt(LockTimer) <= 0) {
                        this.discard();
                    }
                } else{this.discard();}
            }else if (this.tickCount > 200) {
                this.discard();
            }
        }
        super.tick();
    }

    private @NotNull AABB getHitBox() {
        net.minecraft.world.phys.Vec3 lookVec = this.getLookAngle();

        double maxReach = 21.0;
        double currentReach = maxReach * Math.min(1.0, (double)this.tickCount / 20);
        double midDist = currentReach * 0.5;

        double hitCenterX = this.getX() + (lookVec.x * midDist);
        double hitCenterY = this.getY() + (lookVec.y * midDist);
        double hitCenterZ = this.getZ() + (lookVec.z * midDist);

        float thickness = 0.5F;
        double growX = Math.abs(lookVec.x) * midDist + thickness;
        double growY = Math.abs(lookVec.y) * midDist + thickness;
        double growZ = Math.abs(lookVec.z) * midDist + thickness;

        return new AABB(
                hitCenterX - growX, hitCenterY - growY, hitCenterZ - growZ,
                hitCenterX + growX, hitCenterY + growY, hitCenterZ + growZ
        );
    }
}