package keeganator.atlanweaponry.mixin.bucketablemob;

import keeganator.atlanweaponry.entity.ClamEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.nautilus.Nautilus;
import net.minecraft.world.entity.animal.squid.Squid;
import net.minecraft.world.entity.animal.turtle.Turtle;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(Bucketable.class)
public interface BucketableMixin {
    @Inject(method = "bucketMobPickup", at = @At("HEAD"), cancellable = true)
    private static <T extends LivingEntity & Bucketable> void atlanweaponry$onlyAllowBabies(Player player, InteractionHand hand, T entity, CallbackInfoReturnable<Optional<InteractionResult>> cir) {
        if (entity instanceof Squid || entity instanceof Turtle || entity instanceof Nautilus || entity instanceof ClamEntity) {
            if (!entity.isBaby()) {
                cir.setReturnValue(Optional.of(InteractionResult.PASS));
            }
        }
    }
}