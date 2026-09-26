package keeganator.atlanweaponry.entity;

import keeganator.atlanweaponry.AtlanWeaponry;
import keeganator.atlanweaponry.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.AgeableWaterCreature;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jspecify.annotations.Nullable;

public class ClamEntity extends AgeableWaterCreature implements Bucketable {
    public ClamEntity(EntityType<? extends ClamEntity> entityType, Level world) {
        super(entityType, world);
    }

    private static final EntityDataAccessor<Boolean> OPEN_TRIGGER =
            SynchedEntityData.defineId(ClamEntity.class, EntityDataSerializers.BOOLEAN);

    private static final int OPEN_PLATEAU_START_TICK = 35;   // 1.75 sec
    private static final int OPEN_PLATEAU_END_TICK = 120;    // 6.0 sec
    private static final int OPEN_ANIMATION_TOTAL_TICKS = 150; // 7.5 sec

    public final AnimationState idleAnimationState = new AnimationState();

    public final AnimationState openAnimationState = new AnimationState();

    private int closedStateTicksRemaining;

    public static AttributeSupplier.Builder createClamAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 16.0).add(Attributes.MOVEMENT_SPEED, 0.0)
                .add(Attributes.GRAVITY, 0.24).add(Attributes.KNOCKBACK_RESISTANCE, 0.2);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData entityData) {
        this.closedStateTicksRemaining = this.random.nextIntBetweenInclusive(100, 600);
        entityData = super.finalizeSpawn(world, difficulty, spawnReason, entityData);

        this.populateDefaultEquipmentSlots(this.random, difficulty);

        float yaw = this.random.nextFloat() * 360.0F;
        this.setYRot(yaw);
        this.setYBodyRot(yaw);
        this.setYHeadRot(yaw);

        return entityData;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
        return ModEntities.CLAM.create(world, EntitySpawnReason.BREEDING);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance localDifficulty) {
        if (random.nextFloat() < 0.7F) {
            float f = random.nextFloat();
            ItemStack itemStack;
            if (f < 0.01F) {
                itemStack = new ItemStack(Items.DIAMOND);
            } else if (f < 0.05F) {
                itemStack = new ItemStack(Items.TURTLE_SCUTE);
            } else if (f < 0.15F) {
                itemStack = new ItemStack(ModItems.PEARL);
            } else if (f < 0.45F) {
                itemStack = new ItemStack(Items.NAUTILUS_SHELL);
            } else if (f < 0.55F) {
            itemStack = random.nextBoolean() ? new ItemStack(Items.COD) : new ItemStack(Items.TROPICAL_FISH);
            } else {
                itemStack = new ItemStack(Items.SAND);
            }

            setHeldItem(itemStack);
        }
    }

    private void setHeldItem(ItemStack stack) {
        this.setItemSlot(EquipmentSlot.MAINHAND, stack);
        if (!stack.isEmpty()) {
            this.setPersistenceRequired();
        }
    }

    @Override
    protected void dropEquipment(ServerLevel world) {
        super.dropEquipment(world);

        ItemStack held = this.getItemBySlot(EquipmentSlot.MAINHAND);
        if (!held.isEmpty()) {
            this.spawnAtLocation(world, held.copy());
            this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        }
    }

    public static boolean checkSurfaceAgeableWaterCreatureSpawnRules(EntityType<? extends AgeableWaterCreature> type, LevelAccessor world, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
        return world.getBlockState(pos.below()).isRedstoneConductor(world, pos.below()) && world.getFluidState(pos).is(FluidTags.WATER);
    }

    @Override
    public void tick() {
        super.tick();

        boolean openTrigger = this.entityData.get(OPEN_TRIGGER);

        this.idleAnimationState.startIfStopped(this.tickCount);
        if (openTrigger) {
            if (!this.openAnimationState.isStarted()) {
                this.openAnimationState.start(this.tickCount);
            }
        } else {
            this.openAnimationState.stop();
        }

        if (!this.level().isClientSide()) {
            if (--this.closedStateTicksRemaining <= 0) {
                if (!openTrigger) {
                    this.entityData.set(OPEN_TRIGGER, true);
                    this.closedStateTicksRemaining = OPEN_ANIMATION_TOTAL_TICKS;
                } else {
                    this.entityData.set(OPEN_TRIGGER, false);
                    this.closedStateTicksRemaining = this.random.nextIntBetweenInclusive(300, 1000);
                }
            }
        }
        if (this.level().isClientSide() && this.isMouthOpen() && this.random.nextFloat() < 0.5F) {
            double x = this.getX() + (this.random.nextDouble() - 0.5) * this.getBbWidth();
            double y = this.getY() + this.getBbHeight() * 0.4;
            double z = this.getZ() + (this.random.nextDouble() - 0.5) * this.getBbWidth();
            this.level().addParticle(ParticleTypes.BUBBLE_COLUMN_UP, x, y, z, 0.0, 0.04, 0.0);
        }
    }

    public boolean isMouthOpen() {
        if (!this.openAnimationState.isStarted()) {
            return false;
        }
        long ticks = this.openAnimationState.getTimeInMillis(this.tickCount) / 50L;
        return ticks >= OPEN_PLATEAU_START_TICK && ticks <= OPEN_PLATEAU_END_TICK;
    }

    @Override
    public float getAgeScale() {
        return this.isBaby() ? 0.5F : 1.0F;
    }

    @Override
    public boolean canBeLeashed() {
        return true;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(OPEN_TRIGGER, false);
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (!this.isMouthOpen()) {
            return super.mobInteract(player, hand);
        }

        ItemStack handStack = player.getItemInHand(hand);
        ItemStack clamStack = this.getItemBySlot(EquipmentSlot.MAINHAND);

        if (!clamStack.isEmpty()) {
            if (handStack.isEmpty()) {
                if (!this.level().isClientSide()) {
                    ItemStack toGive = clamStack.copy();
                    this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                    player.getInventory().placeItemBackInInventory(toGive);
                }
                return InteractionResult.SUCCESS;
            }
        } else {
            if (!handStack.isEmpty()) {
                if (!this.level().isClientSide()) {
                    this.setHeldItem(handStack.copyWithCount(1));
                    if (!player.getAbilities().instabuild) {
                        handStack.shrink(1);
                    }
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    private boolean fromBucketBool;

    @Override
    public boolean fromBucket() {
        return fromBucketBool;
    }

    @Override
    public void setFromBucket(boolean fromBucket) {
        fromBucketBool = fromBucket;
    }

    private static final String HELD_ITEM_KEY = "HeldItem";

    @Override
    public void saveToBucketTag(ItemStack stack) {
        Bucketable.saveDefaultDataToBucketTag(this, stack);

        ItemStack held = this.getItemBySlot(EquipmentSlot.MAINHAND);
        if (!held.isEmpty()) {
            CompoundTag nbt = new CompoundTag();

            ItemStack.OPTIONAL_CODEC.encodeStart(this.level().registryAccess().createSerializationContext(NbtOps.INSTANCE), held)
                    .resultOrPartial(msg -> AtlanWeaponry.LOGGER.error("Failed to encode clam held item: {}", msg))
                    .ifPresent(encoded -> nbt.put(HELD_ITEM_KEY, encoded));

            stack.set(DataComponents.BUCKET_ENTITY_DATA, CustomData.of(nbt));
        }
    }

    @Override
    public void loadFromBucketTag(CompoundTag nbt) {
        Bucketable.loadDefaultDataFromBucketTag(this, nbt);

        if (nbt.contains(HELD_ITEM_KEY)) {
            ItemStack.OPTIONAL_CODEC.parse(this.level().registryAccess().createSerializationContext(NbtOps.INSTANCE), nbt.get(HELD_ITEM_KEY))
                    .resultOrPartial(msg -> AtlanWeaponry.LOGGER.error("Failed to decode clam held item: {}", msg))
                    .ifPresent(this::setHeldItem);
        }
    }

    @Override
    public ItemStack getBucketItemStack() {
        return new ItemStack(ModItems.BABY_CLAM_BUCKET);
    }

    @Override
    public SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_FISH;
    }
}
