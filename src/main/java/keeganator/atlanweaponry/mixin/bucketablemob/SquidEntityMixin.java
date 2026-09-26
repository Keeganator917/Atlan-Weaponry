package keeganator.atlanweaponry.mixin.bucketablemob;

import keeganator.atlanweaponry.item.ModItems;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Bucketable;
import net.minecraft.world.entity.animal.squid.GlowSquid;
import net.minecraft.world.entity.animal.squid.Squid;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Squid.class)
public abstract class SquidEntityMixin implements Bucketable {

    @Unique
    private boolean atlanweaponry$fromBucket;

    @Override
    public boolean fromBucket() {
        return this.atlanweaponry$fromBucket;
    }

    @Override
    public void setFromBucket(boolean fromBucket) {
        this.atlanweaponry$fromBucket = fromBucket;
    }

    @Override
    public ItemStack getBucketItemStack() {
        Squid squid = (Squid) (Object) this;
        if (squid instanceof GlowSquid) {
            return new ItemStack(ModItems.BABY_GLOW_SQUID_BUCKET);
        }
        return new ItemStack(ModItems.BABY_SQUID_BUCKET);
    }

    @Override
    public SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_FISH;
    }

    @Override
    public void saveToBucketTag(ItemStack stack) {
        Bucketable.saveDefaultDataToBucketTag((Squid) (Object) this, stack);
    }

    @Override
    public void loadFromBucketTag(net.minecraft.nbt.CompoundTag nbt) {
        Bucketable.loadDefaultDataFromBucketTag((Squid) (Object) this, nbt);
    }
}