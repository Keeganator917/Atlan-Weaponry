package keeganator.atlanweaponry.effect;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

public class StunnedEffect extends MobEffect {

    public StunnedEffect() {
        super(MobEffectCategory.HARMFUL, 0xE08E1B);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(ServerLevel world, LivingEntity entity, int amplifier) {
        if (entity instanceof Mob mob) {
            mob.setTarget(null);
        }
        return true;
    }
}