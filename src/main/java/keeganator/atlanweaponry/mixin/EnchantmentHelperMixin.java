package keeganator.atlanweaponry.mixin;

import keeganator.atlanweaponry.item.ModItems;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    @Inject(method = "modifyDamage", at = @At("HEAD"), cancellable = true)
    private static void atlanweaponry$hellforkImpaling(ServerLevel world, ItemStack stack, Entity target, DamageSource damageSource, float baseDamage, CallbackInfoReturnable<Float> cir) {
        if (!stack.is(ModItems.HELLFORK)) {
            return;
        }

        Holder<Enchantment> impaling = world.registryAccess()
                .lookupOrThrow(Registries.ENCHANTMENT)
                .get(Enchantments.IMPALING)
                .orElse(null);
        int impalingLevel = impaling == null ? 0 : EnchantmentHelper.getItemEnchantmentLevel(impaling, stack);

        float damage = baseDamage;
        if (impalingLevel > 0 && (target.isOnFire() || target.isInLava())) {
            damage += 2.0F + 1.5F * (impalingLevel - 1);
        }

        cir.setReturnValue(damage);
    }

}
