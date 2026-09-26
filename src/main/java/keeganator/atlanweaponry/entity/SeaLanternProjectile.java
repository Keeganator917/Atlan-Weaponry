package keeganator.atlanweaponry.entity;

import keeganator.atlanweaponry.damagetypes.ModDamageTypes;
import keeganator.atlanweaponry.enchantments.ModEnchantments;
import keeganator.atlanweaponry.util.ExplosionShield;
import keeganator.atlanweaponry.util.NoWaterDrag;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SeaLanternProjectile extends ThrowableProjectile implements ItemSupplier, NoWaterDrag {

    @Nullable
    private LivingEntity owner;
    private ItemStack itemStack = ItemStack.EMPTY;

    public SeaLanternProjectile(EntityType<? extends SeaLanternProjectile> type, Level world) {
        super(type, world);
    }

    public SeaLanternProjectile(Level world, LivingEntity owner) {
        super(ModEntities.SEA_LANTERN_PROJECTILE, world);
    }

    public SeaLanternProjectile(Level world, Player owner, ItemStack itemStack) {
        super(ModEntities.SEA_LANTERN_PROJECTILE, world);
        this.setOwner(owner);
        this.owner = owner;
        this.itemStack = itemStack;

        this.noPhysics = false;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    public void tick() {
        super.tick();

        this.level().addParticle(ParticleTypes.GLOW, this.getX(), this.getY(), this.getZ(), 0, 0, 0);

        if (!this.onGround()) {
            this.setDeltaMovement(this.getDeltaMovement().scale(0.98));
        }
    }

    @Override
    protected double getDefaultGravity() {
        return 0.06; // vanilla arrow is 0.05
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);

        LivingEntity target = (LivingEntity) entityHitResult.getEntity();
        Level world = this.level();

        if (!world.isClientSide()) {
            target.hurtServer((ServerLevel) world, ModDamageTypes.seaLantern((ServerLevel) world, this.getOwner()), 14.0f);

            Vec3 velocity = this.getDeltaMovement().normalize();

            double horizontalStrength = 0.5;
            double verticalStrength = 0.05;

            target.push(velocity.x * horizontalStrength, verticalStrength, velocity.z * horizontalStrength);
            target.needsSync = true;

            if (world.isThundering()) {
                explosionOnImpact();
            }

            Holder<Enchantment> shattering = enchantmentHelper(ModEnchantments.SHATTERING);
            if (shattering != null) {
                int level = EnchantmentHelper.getItemEnchantmentLevel(shattering, this.itemStack);
                if (level > 0) {
                    shatteringExplosion();
                }
            }

            Holder<Enchantment> fiery_lanterns = enchantmentHelper(ModEnchantments.FIERY_LANTERNS);
            if (fiery_lanterns != null) {
                int level = EnchantmentHelper.getItemEnchantmentLevel(fiery_lanterns, this.itemStack);
                if (level > 0) {
                    target.igniteForSeconds(6);
                }
            }

            this.discard();
        }
    }

    @Override
    public ItemStack getItem() {
        return itemStack;
    }


    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);

        Level world = this.level();

        if (!world.isClientSide()) {
            world.levelEvent(2001, this.blockPosition(), Block.getId(Blocks.SEA_LANTERN.defaultBlockState()));
            if (world.isThundering()) {
                explosionOnImpact();
            }

            Holder<Enchantment> shattering = enchantmentHelper(ModEnchantments.SHATTERING);
            if (shattering != null) {
                int level = EnchantmentHelper.getItemEnchantmentLevel(shattering, this.itemStack);
                if (level > 0) {
                    shatteringExplosion();
                }
            }

            Holder<Enchantment> fiery_lanterns = enchantmentHelper(ModEnchantments.FIERY_LANTERNS);
            if (fiery_lanterns != null) {
                int level = EnchantmentHelper.getItemEnchantmentLevel(fiery_lanterns, this.itemStack);
                if (level > 0) {
                    BlockPos firePos = blockHitResult.getBlockPos().relative(blockHitResult.getDirection());

                    if (BaseFireBlock.canBePlacedAt(world, firePos, this.getDirection())) {
                        world.setBlockAndUpdate(firePos, BaseFireBlock.getState(world, firePos));
                    }
                }
            }

            this.discard();
        }
    }

    private void explosionOnImpact() {
        BlockPos pos = this.blockPosition();
        List<ItemEntity> shieldedItems = this.level().getEntitiesOfClass(ItemEntity.class,
                AABB.ofSize(pos.getCenter(), 7, 7, 7), itemEntity -> !itemEntity.isInvulnerable());

        for (ItemEntity itemEntity : shieldedItems) { //prevents items from exploding
            ExplosionShield.shield(itemEntity);
        }

        Holder<Enchantment> fiery_lanterns = enchantmentHelper(ModEnchantments.FIERY_LANTERNS);
        if (fiery_lanterns != null) {
            int level = EnchantmentHelper.getItemEnchantmentLevel(fiery_lanterns, this.itemStack);
            this.level().explode(this, pos.getX(), pos.getY(), pos.getZ(), 1.0f, level > 0, Level.ExplosionInteraction.NONE);
        } else {
            this.level().explode(this, pos.getX(), pos.getY(), pos.getZ(), 1.2f, false, Level.ExplosionInteraction.NONE);
        }

        for (ItemEntity itemEntity : shieldedItems) { //allows items to explode again
            ExplosionShield.unshield(itemEntity);
        }
    }

    private void shatteringExplosion() {
        if (!this.level().isClientSide()) {
            for (int i = 0; i < 44; i++) {
                SeaLanternShardProjectile shard = new SeaLanternShardProjectile(this.level(), (LivingEntity) this.getOwner());

                double theta = random.nextDouble() * Math.PI * 2.0;
                double phi = Math.acos(2.0 * random.nextDouble() - 1.0);

                double x = Math.sin(phi) * Math.cos(theta);
                double y = Math.cos(phi);
                double z = Math.sin(phi) * Math.sin(theta);

                Vec3 direction = new Vec3(x, y, z);
                Vec3 spawnOffset = direction.scale(0.4);

                shard.setPos(this.getX() + spawnOffset.x, this.getY() + spawnOffset.y, this.getZ() + spawnOffset.z);
                shard.setDeltaMovement(direction.scale(0.9F));
                shard.needsSync = true;

                this.level().addFreshEntity(shard);
            }
        }
    }

    public Holder<Enchantment> enchantmentHelper(ResourceKey<Enchantment> modEnchant) {
        Holder<Enchantment> enchantment =
                this.owner.level()
                        .registryAccess()
                        .lookupOrThrow(Registries.ENCHANTMENT)
                        .get(modEnchant)
                        .orElse(null);
        return enchantment;
    }

}