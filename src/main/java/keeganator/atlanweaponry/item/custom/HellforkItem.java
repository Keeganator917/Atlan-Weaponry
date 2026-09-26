package keeganator.atlanweaponry.item.custom;

import keeganator.atlanweaponry.AtlanWeaponry;
import keeganator.atlanweaponry.entity.HellforkEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Position;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import java.util.List;

public class HellforkItem extends Item implements ProjectileItem {
    public static final int MIN_DRAW_DURATION = 10;
    public static final float ATTACK_DAMAGE = 9.0F;
    public static final float THROW_SPEED = 2.5F;

    public HellforkItem(Item.Properties settings) {
        super(settings);
    }

    public static ItemAttributeModifiers createAttributeModifiers() {
        return ItemAttributeModifiers.builder()
                .add(
                        Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(BASE_ATTACK_DAMAGE_ID, 9.0, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND
                )
                .add(
                        Attributes.ATTACK_SPEED,
                        new AttributeModifier(BASE_ATTACK_SPEED_ID, -2.9F, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND
                )
                .build();
    }

    public static Tool createToolComponent() {
        return new Tool(List.of(), 1.0F, 2, false);
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.TRIDENT;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity user) {
        return 72000;
    }

    @Override
    public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.igniteForSeconds(4);
    }

    @Override
    public boolean releaseUsing(ItemStack stack, Level world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof Player playerEntity) {
            int i = this.getUseDuration(stack, user) - remainingUseTicks;
            if (i < 10) {
                return false;
            } else {
                float f = EnchantmentHelper.getTridentSpinAttackStrength(stack, playerEntity);
                if (f > 0.0F && !(playerEntity.isOnFire() || playerEntity.isInLava())) {
                    return false;
                } else if (stack.nextDamageWillBreak()) {
                    return false;
                } else {
                    Holder<SoundEvent> registryEntry = (Holder<SoundEvent>)EnchantmentHelper.pickHighestLevel(stack, EnchantmentEffectComponents.TRIDENT_SOUND)
                            .orElse(SoundEvents.TRIDENT_THROW);
                    playerEntity.awardStat(Stats.ITEM_USED.get(this));
                    if (world instanceof ServerLevel serverWorld) {
                        stack.hurtWithoutBreaking(1, playerEntity);
                        if (f == 0.0F) {
                            ItemStack itemStack = stack.consumeAndReturn(1, playerEntity);

                            HellforkEntity tridentEntity = new HellforkEntity(serverWorld, user, itemStack);
                            tridentEntity.shootFromRotation(playerEntity, playerEntity.getXRot(), playerEntity.getYRot(), 0.0F, 2.5F, 1.0F);
                            serverWorld.addFreshEntity(tridentEntity);

                            if (playerEntity.hasInfiniteMaterials()) {
                                tridentEntity.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                            }

                            world.playSound(null, tridentEntity, registryEntry.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
                            return true;
                        }
                    }

                    if (f > 0.0F) {
                        float g = playerEntity.getYRot();
                        float h = playerEntity.getXRot();
                        float j = -Mth.sin(g * (float) (Math.PI / 180.0)) * Mth.cos(h * (float) (Math.PI / 180.0));
                        float k = -Mth.sin(h * (float) (Math.PI / 180.0));
                        float l = Mth.cos(g * (float) (Math.PI / 180.0)) * Mth.cos(h * (float) (Math.PI / 180.0));
                        float m = Mth.sqrt(j * j + k * k + l * l);
                        j *= f / m;
                        k *= f / m;
                        l *= f / m;
                        playerEntity.push(j, k, l);
                        playerEntity.startAutoSpinAttack(20, 8.0F, stack);
                        if (playerEntity.onGround()) {
                            float n = 1.1999999F;
                            playerEntity.move(MoverType.SELF, new Vec3(0.0, 1.1999999F, 0.0));
                        }

                        world.playSound(null, playerEntity, registryEntry.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        } else {
            return false;
        }
    }

    @Override
    public InteractionResult use(Level world, Player user, InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);
        if (itemStack.nextDamageWillBreak()) {
            return InteractionResult.FAIL;
        } else if (EnchantmentHelper.getTridentSpinAttackStrength(itemStack, user) > 0.0F && !(user.isOnFire() || user.isInLava())) {
            return InteractionResult.FAIL;
        } else {
            user.startUsingItem(hand);
            return InteractionResult.CONSUME;
        }
    }

    @Override
    public Projectile asProjectile(Level world, Position pos, ItemStack stack, Direction direction) {
        HellforkEntity tridentEntity = new HellforkEntity(world, pos.x(), pos.y(), pos.z(), stack.copyWithCount(1));
        tridentEntity.pickup = AbstractArrow.Pickup.ALLOWED;
        return tridentEntity;
    }
}
