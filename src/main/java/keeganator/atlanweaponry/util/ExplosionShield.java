package keeganator.atlanweaponry.util;

import net.minecraft.world.entity.Entity;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ExplosionShield {

    private static final Set<UUID> shielded = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public static void shield(Entity entity) {
        shielded.add(entity.getUUID());
    }

    public static void unshield(Entity entity) {
        shielded.remove(entity.getUUID());
    }

    public static boolean isShielded(Entity entity) {
        return shielded.contains(entity.getUUID());
    }

}
