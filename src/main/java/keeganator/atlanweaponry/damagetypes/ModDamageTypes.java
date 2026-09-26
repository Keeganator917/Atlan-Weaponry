package keeganator.atlanweaponry.damagetypes;

import keeganator.atlanweaponry.AtlanWeaponry;
import keeganator.atlanweaponry.util.EntityExcludedDamageSource;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;

public class ModDamageTypes {
    public static final ResourceKey<DamageType> BUBBLE_MAGIC =
            create("bubble_magic");
    public static final ResourceKey<DamageType> SEA_LANTERN =
            create("sea_lantern");
    public static final ResourceKey<DamageType> SEA_LANTERN_SHARD =
            create("sea_lantern_shard");

    private static ResourceKey<DamageType> create(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath(AtlanWeaponry.MOD_ID, name));
    }

    /**
     * Unified method for creating DamageSource with optional owner and excluded entity types
     */
    public static DamageSource create(ServerLevel world, ResourceKey<DamageType> type, Entity direct, @Nullable Entity owner, EntityType<?>... toIgnore) {
        HolderGetter<DamageType> lookup = world.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE);
        Holder<DamageType> entry = lookup.getOrThrow(type);

        if (toIgnore.length > 0) {
            return new EntityExcludedDamageSource(entry, toIgnore);
        }

        return new DamageSource(entry, direct, owner);
    }

    /**
     * Convenience wrappers for your mod-specific damage types
     */

    public static DamageSource bubbleMagic(ServerLevel world, Entity attacker) {
        return create(world, BUBBLE_MAGIC, attacker, null);
    }

    public static DamageSource seaLantern(ServerLevel world, Entity attacker) {
        return create(world, SEA_LANTERN, attacker, null);
    }

    public static DamageSource seaLanternShard(ServerLevel world, Entity attacker) {
        return create(world, SEA_LANTERN_SHARD, attacker, null);
    }

    /**
     * Generic convenience methods
     */
    public static DamageSource direct(ServerLevel world, ResourceKey<DamageType> type, Entity direct) {
        return create(world, type, direct, null);
    }

    public static DamageSource indirect(ServerLevel world, ResourceKey<DamageType> type, Entity direct, @Nullable Entity owner) {
        return create(world, type, direct, owner);
    }

    public static DamageSource exclude(ServerLevel world, ResourceKey<DamageType> type, Entity direct, EntityType<?>... toIgnore) {
        return create(world, type, direct, null, toIgnore);
    }
}
