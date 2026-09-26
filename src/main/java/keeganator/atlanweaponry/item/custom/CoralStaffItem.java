package keeganator.atlanweaponry.item.custom;

import keeganator.atlanweaponry.enchantments.ModEnchantments;
import keeganator.atlanweaponry.entity.BubbleMagicProjectile;
import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

public class CoralStaffItem extends Item {
    public CoralStaffItem(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResult use(Level world, Player user, InteractionHand hand) {
        ItemStack stack = user.getItemInHand(hand);
        if (!user.getCooldowns().isOnCooldown(stack)) {
            if (!world.isClientSide()) {
                BubbleMagicProjectile projectile = new BubbleMagicProjectile(world, user, stack);

                projectile.setPosRaw(user.getX(), user.getEyeY() - 0.1, user.getZ());
                projectile.shootFromRotation(user, user.getXRot(), user.getYRot(), 0.0F, 1.0F, 0.0F);

                world.addFreshEntity(projectile);
                if (world.isThundering()) {
                    for (int i = 0; i <= 7; i++) {
                        float rand = (float) (world.getRandom().nextIntBetweenInclusive(-20, 20)) / 8;
                        float rand2 = (float) (world.getRandom().nextIntBetweenInclusive(-20, 20)) / 8;
                        BubbleMagicProjectile spreadProjectiles = new BubbleMagicProjectile(world, user, stack);

                        spreadProjectiles.setPosRaw(user.getX(), user.getEyeY() - 0.1, user.getZ());
                        spreadProjectiles.shootFromRotation(user, user.getXRot() + rand, user.getYRot() + rand2, 0.0F, 1.0F, 5.0F);

                        world.addFreshEntity(spreadProjectiles);
                    }
                }
                user.getCooldowns().addCooldown(stack, 20);
            }
            world.playSound(user.asLivingEntity(), user.blockPosition(), SoundEvents.BUBBLE_COLUMN_UPWARDS_INSIDE, SoundSource.PLAYERS);
            if (!user.hasInfiniteMaterials()) {
                stack.hurtAndBreak(1, user, hand);
            }
        }


        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean canBeEnchantedWith(ItemStack stack, Holder<Enchantment> enchantment, EnchantingContext context) {
        boolean allowed = enchantment.is(Enchantments.UNBREAKING) || enchantment.is(Enchantments.MENDING);
        if (allowed) {
            return true;
        }
        boolean denied = enchantment.is(Enchantments.SHARPNESS) || enchantment.is(Enchantments.BANE_OF_ARTHROPODS) || enchantment.is(Enchantments.SMITE)
                || enchantment.is(Enchantments.FORTUNE) || enchantment.is(Enchantments.IMPALING) || enchantment.is(Enchantments.SWEEPING_EDGE)
                || enchantment.is(Enchantments.BREACH) || enchantment.is(Enchantments.DENSITY) || enchantment.is(Enchantments.WIND_BURST)
                || enchantment.is(Enchantments.FIRE_ASPECT) || enchantment.is(Enchantments.LOOTING) || enchantment.is(Enchantments.KNOCKBACK)
                || enchantment.is(ModEnchantments.SHATTERING) || enchantment.is(ModEnchantments.FIERY_LANTERNS);
        if (denied) {
            return false;
        }
        return enchantment.value().canEnchant(stack);
    }
}
