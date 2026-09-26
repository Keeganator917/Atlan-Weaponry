package keeganator.atlanweaponry.mixin;

import keeganator.atlanweaponry.datacomponents.ModDataComponents;
import keeganator.atlanweaponry.datacomponents.PoseidonsBlessingComponent;
import keeganator.atlanweaponry.effect.ModEffects;
import keeganator.atlanweaponry.item.ModItems;
import keeganator.atlanweaponry.util.OfferingLocation;
import keeganator.atlanweaponry.util.network.ShowNeptunesOfferingPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @ModifyVariable(method = "hurtServer", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private float atlanweaponry$poseidonsBlessingDamageReduction(float amount, ServerLevel world, DamageSource source, float originalAmount) {
        LivingEntity entity = (LivingEntity)(Object)this;

        if (entity.hasEffect(ModEffects.POSEIDONS_BLESSING) && entity.isInWaterOrRain()) {
            return amount * 0.8F;
        }

        return amount;
    }

    @Inject(method = "calculateFallDamage", at = @At("RETURN"), cancellable = true)
    private void atlanweaponry$reduceFallDamage(double fallDistance, float damagePerDistance, CallbackInfoReturnable<Integer> cir) {
        LivingEntity entity = (LivingEntity)(Object)this;

        if (entity.hasEffect(ModEffects.POSEIDONS_BLESSING) && entity.isInWaterOrRain()) {
            cir.setReturnValue((int)(cir.getReturnValue() * 0.4F));
        }
    }

    @Inject(method = "hurtServer", at = @At("TAIL"))
    private void atlanweaponry$triggerBlessing(ServerLevel world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity)(Object)this;

        if (!(entity instanceof Player player)) {
            return;
        }

        if (world.isClientSide()) {
            return;
        }

        float threshold = player.getMaxHealth() / 2.0F;

        if (player.getHealth() > threshold) {
            return;
        }

        OfferingLocation offering = findOffering(player);

        if (offering == null) {
            return;
        }
        ItemStack stack = offering.stack();

        PoseidonsBlessingComponent component = stack.get(ModDataComponents.POSEIDONS_BLESSING_COMPONENT);

        if (component != null) {
            component.applyEffects(stack, player);
            world.playSound(null, player.blockPosition(), SoundEvents.TOTEM_USE, player.getSoundSource(), 1.0F, 1.0F);
            ServerPlayNetworking.send((ServerPlayer) player, new ShowNeptunesOfferingPayload());
            stack.shrink(1);
        }
    }

    @Unique
    private OfferingLocation findOffering(Player player) {

        if (player.getMainHandItem().is(ModItems.NEPTUNES_OFFERING)) {
            return new OfferingLocation(InteractionHand.MAIN_HAND, player.getMainHandItem());
        }

        if (player.getOffhandItem().is(ModItems.NEPTUNES_OFFERING)) {
            return new OfferingLocation(InteractionHand.OFF_HAND, player.getOffhandItem());
        }

        return null;
    }
}