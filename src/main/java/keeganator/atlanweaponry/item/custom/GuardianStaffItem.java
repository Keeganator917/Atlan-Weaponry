package keeganator.atlanweaponry.item.custom;

import keeganator.atlanweaponry.enchantments.ModEnchantments;
import keeganator.atlanweaponry.entity.SeaLanternProjectile;
import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

public class GuardianStaffItem extends Item {
    public GuardianStaffItem(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResult use(Level world, Player user, InteractionHand hand) {
        ItemStack stack = user.getItemInHand(hand);
        ItemStack ammoStack = findAmmo(user);

        if (user.getCooldowns().isOnCooldown(stack)) {
            return InteractionResult.FAIL;
        }

        if (ammoStack.isEmpty() && !user.isCreative()) {
            return InteractionResult.FAIL;
        }

        if (!world.isClientSide()) {
            SeaLanternProjectile projectile = new SeaLanternProjectile(world, user, stack);

            projectile.setPosRaw(user.getX(), user.getEyeY() - 0.1, user.getZ());
            projectile.shootFromRotation(user, user.getXRot(), user.getYRot(), 0.0F, 1.2F, 1.5F);
            world.addFreshEntity(projectile);

            if (!user.isCreative()) {
                ammoStack.shrink(1);
                stack.hurtAndBreak(1, user, hand);
            }

            user.getCooldowns().addCooldown(stack, 30);
            user.awardStat(Stats.ITEM_USED.get(this));
        }

        world.playSound(null, user.blockPosition(), SoundEvents.GLASS_PLACE, SoundSource.PLAYERS, 1.0F, 1.0F);
        return InteractionResult.SUCCESS;
    }

    private ItemStack findAmmo(Player player) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.is(Items.SEA_LANTERN)) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canBeEnchantedWith(ItemStack stack, Holder<Enchantment> enchantment, EnchantingContext context) {
        boolean allowed = enchantment.is(Enchantments.UNBREAKING) || enchantment.is(Enchantments.MENDING) || enchantment.is(ModEnchantments.SHATTERING)
                || enchantment.is(ModEnchantments.FIERY_LANTERNS);
        if (allowed) {
            return true;
        }
        boolean denied = enchantment.is(Enchantments.SHARPNESS) || enchantment.is(Enchantments.BANE_OF_ARTHROPODS) || enchantment.is(Enchantments.SMITE)
                || enchantment.is(Enchantments.FORTUNE) || enchantment.is(Enchantments.IMPALING) || enchantment.is(Enchantments.SWEEPING_EDGE)
                || enchantment.is(Enchantments.BREACH) || enchantment.is(Enchantments.DENSITY) || enchantment.is(Enchantments.WIND_BURST)
                || enchantment.is(Enchantments.FIRE_ASPECT) || enchantment.is(Enchantments.LOOTING) || enchantment.is(Enchantments.KNOCKBACK);
        if (denied) {
            return false;
        }
        return enchantment.value().canEnchant(stack);
    }
}