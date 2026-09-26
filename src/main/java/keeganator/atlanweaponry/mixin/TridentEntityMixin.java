package keeganator.atlanweaponry.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownTrident.class)
public class TridentEntityMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void atlan$tridentTick(CallbackInfo ci) {
        ThrownTrident trident = (ThrownTrident) (Object) this;
        Level world = trident.level();

        if (!(world instanceof ServerLevel serverWorld)) return;

        if (!serverWorld.isThundering()) return;

        //Code for riptide and channeling enhancements
    }

}
