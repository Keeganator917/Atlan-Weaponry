package keeganator.atlanweaponry.effect;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class PoseidonsBlessingEffect extends MobEffect {

    public PoseidonsBlessingEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x1E90FF);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(ServerLevel world, LivingEntity entity, int amplifier) {

        if (world.isThundering() && !entity.hasEffect(MobEffects.REGENERATION)) {
            entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 80, amplifier, true, false, false));
        }
        if (entity.hasEffect(MobEffects.CONDUIT_POWER) && !entity.hasEffect(MobEffects.STRENGTH)) {
            entity.addEffect(new MobEffectInstance(MobEffects.STRENGTH, 80, amplifier, true, false, false));
        }

        return true;
    }
}