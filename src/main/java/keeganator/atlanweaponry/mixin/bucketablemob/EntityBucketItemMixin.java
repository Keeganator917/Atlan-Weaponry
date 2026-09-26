package keeganator.atlanweaponry.mixin.bucketablemob;

import keeganator.atlanweaponry.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MobBucketItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MobBucketItem.class)
public class EntityBucketItemMixin {

    @Redirect(method = "spawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;addFreshEntityWithPassengers(Lnet/minecraft/world/entity/Entity;)V"))
    private void atlanweaponry$makeBaby(ServerLevel world, Entity entity, ServerLevel worldParam, ItemStack stack, BlockPos pos) {
        if (entity instanceof Mob mobEntity && stack.is(ModTags.Items.BABY_MOB_BUCKETS)) {
            mobEntity.setBaby(true);
        }
        world.addFreshEntityWithPassengers(entity);
    }
}