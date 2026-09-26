package keeganator.atlanweaponry.mixin;

import keeganator.atlanweaponry.advancements.ModCriteria;
import keeganator.atlanweaponry.item.ModItems;
import keeganator.atlanweaponry.util.ExplosionShield;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.saveddata.WeatherData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void atlan$checkForOffering(CallbackInfo ci) {
        ItemEntity entity = (ItemEntity) (Object) this;

        if (!(entity.level() instanceof ServerLevel serverWorld)) {
            return;
        }

        if (entity.isInWater() && entity.getItem().is(ModItems.NEPTUNES_OFFERING)) {
            if (entity.getOwner() instanceof ServerPlayer player) {
                ModCriteria.THROWN_INTO_WATER.trigger(player, entity.getItem());
            }

            WeatherData weatherData = serverWorld.getWeatherData();

            weatherData.setClearWeatherTime(0);
            weatherData.setRainTime(12000);
            weatherData.setThunderTime(12000);
            weatherData.setRaining(true);
            weatherData.setThundering(true);

            for (int i = 0; i < 20; i++) {
                double angle = (Math.PI * 2 * i) / 20.0;
                double radius = 0.75;

                serverWorld.sendParticles(ParticleTypes.GLOW, entity.getX() + Math.cos(angle) * radius, entity.getY() + 0.25,
                        entity.getZ() + Math.sin(angle) * radius, 1, 0, 0, 0, 0);
            }
            serverWorld.playSound(entity, entity.blockPosition(), SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.WEATHER, 2.2F, 0.01F);
            entity.discard();
        }
    }

    @Inject(method = "ignoreExplosion", at = @At("HEAD"), cancellable = true)
    private void atlan$shieldFromExplosion(Explosion explosion, CallbackInfoReturnable<Boolean> cir) {
        ItemEntity self = (ItemEntity) (Object) this;
        if (ExplosionShield.isShielded(self)) {
            cir.setReturnValue(true);
        }
    }

}
