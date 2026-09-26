package keeganator.atlanweaponry.datacomponents;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TorrentTravelComponent {

    private record TravelState(Vec3 velocity, int ticksRemaining) {}

    private static final Map<UUID, TravelState> ACTIVE_TRAVELERS = new HashMap<>();

    public static void start(ServerPlayer player, Vec3 velocity, int durationTicks) {
        ACTIVE_TRAVELERS.put(player.getUUID(), new TravelState(velocity, durationTicks));
    }

    public static void registerTickEvent() {
        ServerTickEvents.END_SERVER_TICK.register(TorrentTravelComponent::onServerTick);
    }

    private static void onServerTick(MinecraftServer server) {
        if (ACTIVE_TRAVELERS.isEmpty()) return;

        ACTIVE_TRAVELERS.entrySet().removeIf(entry -> {
            UUID uuid = entry.getKey();
            TravelState state = entry.getValue();

            ServerPlayer player = server.getPlayerList().getPlayer(uuid);
            if (player == null) return true;

            int remaining = state.ticksRemaining() - 1;

            if (remaining <= 0) {
                player.removeEffect(MobEffects.RESISTANCE);
                return true;
            }

            player.setDeltaMovement(state.velocity());
            player.needsSync = true;

            player.connection.send(new ClientboundSetEntityMotionPacket(player));

            entry.setValue(new TravelState(state.velocity(), remaining));
            return false;
        });
    }

}
