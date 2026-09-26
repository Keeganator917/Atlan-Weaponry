package keeganator.atlanweaponry.util.network;

import keeganator.atlanweaponry.datacomponents.TorrentTravelComponent;
import keeganator.atlanweaponry.enchantments.ModEnchantments;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;

public class ModNetworking {

    public static final double PROPEL_DISTANCE = 200.0;
    public static final int TRAVEL_TICKS = 40; // 2 seconds for 200 blocks

    public static void register() {
        PayloadTypeRegistry.serverboundPlay().register(
                ShowNeptunesOfferingPayload.ID,
                ShowNeptunesOfferingPayload.CODEC
        );

        PayloadTypeRegistry.serverboundPlay().register(TorrentPayload.ID, TorrentPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(TorrentPayload.ID, ModNetworking::handleTorrentActivate);
    }

    public static void registerClient() {

        PayloadTypeRegistry.clientboundPlay().register(ShowNeptunesOfferingPayload.ID, ShowNeptunesOfferingPayload.CODEC);

        PayloadTypeRegistry.clientboundPlay().register(TorrentPayload.ID, TorrentPayload.CODEC);

    }

    private static void handleTorrentActivate(TorrentPayload payload, ServerPlayNetworking.Context context) {
        ServerPlayer player = context.player();
        if (!player.isInWater()) {
            return;
        }

        var heldStack = player.getMainHandItem();
        Holder<Enchantment> torrent =
                context.server().registryAccess()
                        .lookupOrThrow(Registries.ENCHANTMENT)
                        .get(ModEnchantments.TORRENT)
                        .orElse(null);

        if (torrent == null) {
            return;
        }

        int level = EnchantmentHelper.getItemEnchantmentLevel(torrent, heldStack);
        if (level <= 0) {
            return;
        }

        player.addEffect(new MobEffectInstance(MobEffects.RESISTANCE, TRAVEL_TICKS + 5, 255, false, false, false));
        player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, TRAVEL_TICKS + 20, 0, false, false, false));

        Vec3 look = player.getViewVector(1.0f);
        double speedPerTick = PROPEL_DISTANCE / TRAVEL_TICKS;

        Vec3 velocity = new Vec3(look.x * speedPerTick, 0.0, look.z * speedPerTick);

        player.setDeltaMovement(velocity);
        player.needsSync = true;

        player.connection.send(new ClientboundSetEntityMotionPacket(player));

        player.level().playSound(null,
                player.getX(), player.getY(), player.getZ(),
                SoundEvents.TRIDENT_RETURN, player.getSoundSource(),
                2.4F, 0.2F
        );

        player.startAutoSpinAttack(TRAVEL_TICKS, 0.0f, heldStack);

        TorrentTravelComponent.start(player, velocity, TRAVEL_TICKS);

    }

}
