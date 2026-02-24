package com.shidran.hsrphainon.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;
import static com.shidran.hsrphainon.common.HsrPhainonConstants.*;

public class Skill2Entity extends Entity implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private static final EntityDataAccessor<Boolean> IS_BIG = SynchedEntityData.defineId(Skill2Entity.class, EntityDataSerializers.BOOLEAN);

    public Skill2Entity(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {

        double d0 = 64.0 * 64.0;
        return distance < d0 * 4.0;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(IS_BIG, false);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    public net.minecraft.world.phys.AABB getBoundingBoxForCulling() {
        return this.getBoundingBox().inflate(30.0D);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, event -> {
            return event.setAndContinue(RawAnimation.begin().thenLoop("falling"));
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public void setBig(boolean big) {
        this.entityData.set(IS_BIG, big);
    }

    public boolean isBig() {
        return this.entityData.get(IS_BIG);
    }

    @Override
    public void tick() {
        super.tick();
        this.setDeltaMovement(0, -0.5, 0);
        this.move(MoverType.SELF, this.getDeltaMovement());

        if (this.onGround()) {
            if (!this.level().isClientSide) {
                float power = this.isBig() ? Skill2DamageMax : Skill2DamageMin;
                this.level().explode(
                        this,
                        this.getX(),
                        this.getY(),
                        this.getZ(),
                        power,
                        false,
                        explosionType);
            }
            this.discard();
        }

        if (this.tickCount > 1200) {
            this.discard();
        }
    }
}
