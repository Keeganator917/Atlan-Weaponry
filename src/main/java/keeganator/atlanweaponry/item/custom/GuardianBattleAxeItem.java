package keeganator.atlanweaponry.item.custom;

import keeganator.atlanweaponry.enchantments.ModEnchantments;
import keeganator.atlanweaponry.screen.TorrentScreen;
import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class GuardianBattleAxeItem extends Item {

    public GuardianBattleAxeItem(Properties settings) {
        super(settings);
    }

    @Override
    public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        Level world = attacker.level();
        if (world.isThundering()) {
            if (!target.hasEffect(MobEffects.SLOWNESS)) {
                target.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 90, 0));
            }
        }

        Holder<Enchantment> whirlpool =
                world.registryAccess()
                        .lookupOrThrow(Registries.ENCHANTMENT)
                        .get(ModEnchantments.WHIRLPOOL)
                        .orElse(null);

        if (whirlpool != null) {
            int level = EnchantmentHelper.getItemEnchantmentLevel(whirlpool, stack);
            if (level > 0) {
                target.push(0, 0.11 * level, 0);
                if (target instanceof ServerPlayer player) {
                    player.connection.send(new ClientboundSetEntityMotionPacket(player));
                }
                if (!world.isClientSide()) {
                    ServerLevel serverWorld = (ServerLevel) world;
                    Vec3 velocity = target.getDeltaMovement();
                    target.setDeltaMovement(velocity.x, 1.0, velocity.z);
                    target.needsSync = true;

                    double radius = 1.0;
                    int particleCount = 40;

                    for (int i = 0; i < particleCount; i++) {
                        double progress = (double) i / particleCount;
                        double angle = progress * Math.PI * 6;
                        double y = progress * 2.5;

                        double xOffset = Math.cos(angle) * radius;
                        double zOffset = Math.sin(angle) * radius;

                        serverWorld.sendParticles(ParticleTypes.SPLASH, target.getX() + xOffset, target.getY() + y, target.getZ() + zOffset,
                                1, 0, 0, 0, 0);
                        serverWorld.sendParticles(ParticleTypes.BUBBLE, target.getX() + xOffset, target.getY() + y, target.getZ() + zOffset,
                                1, 0, 0, 0, 0);
                    }
                }
            }
        }
        stack.hurtWithoutBreaking(1, (Player) attacker);
    }

    @Override
    public InteractionResult use(Level world, Player user, InteractionHand hand) {
        ItemStack stack = user.getItemInHand(hand);
        Holder<Enchantment> torrent =
                world.registryAccess()
                        .lookupOrThrow(Registries.ENCHANTMENT)
                        .get(ModEnchantments.TORRENT)
                        .orElse(null);

        if (torrent != null) {
            int level = EnchantmentHelper.getItemEnchantmentLevel(torrent, stack);
            if (level > 0 && user.isUnderWater() && !user.getCooldowns().isOnCooldown(stack)) {
                if (world.isClientSide()) {
                    user.getCooldowns().addCooldown(stack, 400);
                    Minecraft.getInstance().gui.setScreen(new TorrentScreen());
                }
            }
        }

        return InteractionResult.FAIL;
    }

    @Override
    public boolean canBeEnchantedWith(ItemStack stack, Holder<Enchantment> enchantment, EnchantingContext context) {
        boolean allowed = enchantment.is(Enchantments.UNBREAKING) || enchantment.is(Enchantments.MENDING) || enchantment.is(Enchantments.SHARPNESS)
                || enchantment.is(Enchantments.BANE_OF_ARTHROPODS) || enchantment.is(Enchantments.SMITE) || enchantment.is(Enchantments.SWEEPING_EDGE)
                || enchantment.is(Enchantments.LOOTING) || enchantment.is(Enchantments.EFFICIENCY) || enchantment.is(Enchantments.FORTUNE)
                || enchantment.is(Enchantments.SILK_TOUCH) || enchantment.is(ModEnchantments.WHIRLPOOL) || enchantment.is(ModEnchantments.TORRENT);
        if (allowed) {
            return true;
        }
        boolean denied = enchantment.is(Enchantments.IMPALING) || enchantment.is(Enchantments.BREACH)
                || enchantment.is(Enchantments.DENSITY) || enchantment.is(Enchantments.WIND_BURST) || enchantment.is(Enchantments.FIRE_ASPECT)
                || enchantment.is(Enchantments.KNOCKBACK) || enchantment.is(ModEnchantments.SHATTERING) || enchantment.is(ModEnchantments.FIERY_LANTERNS);
        if (denied) {
            return false;
        }
        return enchantment.value().canEnchant(stack);
    }
}
