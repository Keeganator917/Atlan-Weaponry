package keeganator.atlanweaponry.item.custom;

import keeganator.atlanweaponry.effect.ModEffects;
import keeganator.atlanweaponry.particles.ModParticles;
import keeganator.atlanweaponry.sound.ModSounds;
import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.minecraft.core.Holder;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import java.util.List;

public class ShellHammerItem extends Item {
    public ShellHammerItem(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResult use(Level world, Player user, InteractionHand hand) {
        ItemStack stack = user.getItemInHand(hand);

        if (!world.isClientSide() && world instanceof ServerLevel serverWorld) {
            double radius = 8.0;
            AABB area = user.getBoundingBox().inflate(radius);

            List<LivingEntity> targets = world.getEntitiesOfClass(LivingEntity.class, area, target -> target != user && target.isAlive());

            for (LivingEntity target : targets) {
                target.addEffect(new MobEffectInstance(ModEffects.STUNNED, 40, 0, false, false));
            }

            serverWorld.playSound(null, user.blockPosition(), SoundEvents.ILLUSIONER_CAST_SPELL, SoundSource.PLAYERS, 0.4F, 0.7F);
            serverWorld.playSound(null, user.blockPosition(), ModSounds.SHELL_STUN, SoundSource.PLAYERS, 1.2F, 1.0F);

            serverWorld.sendParticles(ModParticles.SHELL_STUN, user.getX(), user.getY() + 1.3, user.getZ(), 1, 0.0, 0.0, 0.0, 0.0);
            serverWorld.sendParticles(ModParticles.SHELL_STUN, user.getX(), user.getY() + 1.5, user.getZ(), 1, 0.0, 0.0, 0.0, 0.0);

            user.getCooldowns().addCooldown(stack, 360);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker.level().isThundering()) {
            target.push(target.getDeltaMovement().x() * 0.10, -0.20, target.getDeltaMovement().z() * 0.10);
            if (target instanceof ServerPlayer player) {
                player.connection.send(new ClientboundSetEntityMotionPacket(player));
            }
        }
    }

    @Override
    public boolean canBeEnchantedWith(ItemStack stack, Holder<Enchantment> enchantment, EnchantingContext context) {
        boolean allowed = enchantment.is(Enchantments.FORTUNE) || enchantment.is(Enchantments.SHARPNESS) || enchantment.is(Enchantments.BANE_OF_ARTHROPODS)
                || enchantment.is(Enchantments.SMITE) || enchantment.is(Enchantments.UNBREAKING) || enchantment.is(Enchantments.FIRE_ASPECT)
                || enchantment.is(Enchantments.LOOTING) || enchantment.is(Enchantments.KNOCKBACK) || enchantment.is(Enchantments.MENDING)
                || enchantment.is(Enchantments.EFFICIENCY) || enchantment.is(Enchantments.SILK_TOUCH);
        if (allowed) {
            return true;
        }
        boolean denied = enchantment.is(Enchantments.IMPALING) || enchantment.is(Enchantments.SWEEPING_EDGE)
                || enchantment.is(Enchantments.BREACH) || enchantment.is(Enchantments.DENSITY) || enchantment.is(Enchantments.WIND_BURST);
        if (denied) {
            return false;
        }
        return enchantment.value().canEnchant(stack);
    }

}
