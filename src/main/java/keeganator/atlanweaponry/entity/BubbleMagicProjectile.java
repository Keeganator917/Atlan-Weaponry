package keeganator.atlanweaponry.entity;

import keeganator.atlanweaponry.damagetypes.ModDamageTypes;
import keeganator.atlanweaponry.util.NoWaterDrag;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class BubbleMagicProjectile extends Projectile implements ItemSupplier, NoWaterDrag {

    @Nullable
    private LivingEntity owner;
    private ItemStack itemStack = ItemStack.EMPTY;

    private int life;

    public BubbleMagicProjectile(EntityType<? extends Projectile> entityType, Level world) {
        super(entityType, world);
        this.setNoGravity(true);
        this.noPhysics = false;
    }

    public BubbleMagicProjectile(Level world, LivingEntity owner, ItemStack stack) {
        super(ModEntities.BUBBLE_MAGIC, world);
        this.setOwner(owner);
        this.owner = owner;
        this.itemStack = stack;

        this.setNoGravity(true);
        this.noPhysics = false;
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        Entity entity = entityHitResult.getEntity();

        if (this.level() instanceof ServerLevel serverWorld) {
            entity.hurtServer(serverWorld, ModDamageTypes.bubbleMagic((ServerLevel) this.level(), owner), 4f);
        }
        if (entity instanceof LivingEntity living) {
            float yawOffset = (this.random.nextFloat() * 160f) - 80f;   // -80 to +80
            float pitchOffset = (this.random.nextFloat() * 120f) - 60f; // -60 to +60

            living.setYRot(living.getYRot() + yawOffset);
            living.setXRot(Mth.clamp(living.getXRot() + pitchOffset, -90f, 90f));

            living.yRotO = living.getYRot();
            living.xRotO = living.getXRot();
        }

        this.discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        this.discard();
    }

    @Override
    public void tick() {
        super.tick();
        Vec3 velocity = this.getDeltaMovement();
        Vec3 nextPos = this.position().add(velocity);

        HitResult hitResult = net.minecraft.world.entity.projectile.ProjectileUtil.getHitResultOnMoveVector(
                this,
                this::canHitEntity
        );

        if (hitResult.getType() != HitResult.Type.MISS) {
            this.onHit(hitResult);
        }

        this.setPos(nextPos);

        if (this.level() instanceof ServerLevel serverWorld) {
            serverWorld.sendParticles(
                    ParticleTypes.BUBBLE, this.getX(), this.getY() + this.getBbHeight(), this.getZ(),
                    2, 0.1, 0.1, 0.1, 0.02
            );
        }
        if (++life > 80) {
            this.discard();
        }
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    public ItemStack getItem() {
        return itemStack;
    }
}
