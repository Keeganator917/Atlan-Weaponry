package keeganator.atlanweaponry.mixin.bucketablemob;

import keeganator.atlanweaponry.entity.ClamEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
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

@Mixin(Mob.class)
public abstract class MobEntityMixin {

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void atlanweaponry$bucketBabyAquaticMob(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        Mob mob = (Mob) (Object) this;
        if (!(mob instanceof Squid) && !(mob instanceof Turtle) && !(mob instanceof Nautilus) && !(mob instanceof ClamEntity)) {
            return;
        }
        if (!(mob instanceof LivingEntity livingEntity) || !(livingEntity instanceof Bucketable bucketable)) {
            return;
        }
        Optional<InteractionResult> result = Bucketable.bucketMobPickup(player, hand, (LivingEntity & Bucketable) livingEntity);
        if (result.isPresent()) {
            cir.setReturnValue(result.get());
        }
    }

}