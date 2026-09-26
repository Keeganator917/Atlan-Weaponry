package keeganator.atlanweaponry.entity;

import keeganator.atlanweaponry.damagetypes.ModDamageTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class SeaLanternShardProjectile extends Projectile {

    public SeaLanternShardProjectile(EntityType<? extends Projectile> type, Level world) {
        super(type, world);
    }

    public SeaLanternShardProjectile(Level world, LivingEntity owner) {
        super(ModEntities.SEA_LANTERN_SHARD_PROJECTILE, world);
        this.setOwner(owner);
    }

    @Override
    public void tick() {
        super.tick();

        this.setPos(this.getX() + this.getDeltaMovement().x, this.getY() + this.getDeltaMovement().y, this.getZ() + this.getDeltaMovement().z);
        //drag
        this.setDeltaMovement(this.getDeltaMovement().scale(0.85));
        //gravity
        this.push(0, -0.1, 0);

        HitResult hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);

        if (hitResult.getType() != HitResult.Type.MISS) {
            this.onHit(hitResult);
        }

        if (this.tickCount > 800) {
            this.discard();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);

        if (!(level() instanceof ServerLevel serverWorld)) {
            return;
        }

        if (entityHitResult.getEntity() != this.getOwner()) {
            entityHitResult.getEntity().hurtServer(serverWorld, ModDamageTypes.seaLanternShard((ServerLevel) this.level(), this.getOwner()), 2.0f);
            this.discard();
        }
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);

        if (!this.level().isClientSide()) {
            this.discard();
        }
    }

    @Override
    public boolean canHitEntity(Entity entity) {
        if (entity == this.getOwner()) {
            return false;
        }
        return super.canHitEntity(entity);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }
}