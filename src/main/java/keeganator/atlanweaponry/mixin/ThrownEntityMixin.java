package keeganator.atlanweaponry.mixin;

import keeganator.atlanweaponry.util.NoWaterDrag;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrowableProjectile.class)
public class ThrownEntityMixin {

    //Prevents water increasing drag on entities implementing NoWaterDrag
    @Inject(method = "applyInertia", at = @At("TAIL"))
    private void atlanweaponry$modifyWaterDrag(CallbackInfo ci) {
        ThrowableProjectile entity = (ThrowableProjectile)(Object)this;

        if (entity instanceof NoWaterDrag && entity.isInWater()) {
            Vec3 velocity = entity.getDeltaMovement();

            entity.setDeltaMovement(velocity.scale(0.99 / 0.8));
        }
    }

}
