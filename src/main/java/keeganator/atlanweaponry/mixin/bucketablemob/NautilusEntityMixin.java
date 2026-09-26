package keeganator.atlanweaponry.mixin.bucketablemob;

import keeganator.atlanweaponry.item.ModItems;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Bucketable;
import net.minecraft.world.entity.animal.nautilus.Nautilus;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Nautilus.class)
public abstract class NautilusEntityMixin implements Bucketable {

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
        return new ItemStack(ModItems.BABY_NAUTILUS_BUCKET);
    }

    @Override
    public SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_FISH;
    }

    @Override
    public void saveToBucketTag(ItemStack stack) {
        Bucketable.saveDefaultDataToBucketTag((Nautilus) (Object) this, stack);
    }

    @Override
    public void loadFromBucketTag(net.minecraft.nbt.CompoundTag nbt) {
        Bucketable.loadDefaultDataFromBucketTag((Nautilus) (Object) this, nbt);
    }
}