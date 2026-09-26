package keeganator.atlanweaponry.entity;

import keeganator.atlanweaponry.AtlanWeaponry;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntities {
    public static final ResourceKey<EntityType<?>> BUBBLE_MAGIC_KEY =
            ResourceKey.create(BuiltInRegistries.ENTITY_TYPE.key(), Identifier.fromNamespaceAndPath(AtlanWeaponry.MOD_ID, "bubble_magic"));
    public static final ResourceKey<EntityType<?>> SEA_LANTERN_PROJECTILE_KEY =
            ResourceKey.create(BuiltInRegistries.ENTITY_TYPE.key(), Identifier.fromNamespaceAndPath(AtlanWeaponry.MOD_ID, "sea_lantern_projectile"));
    public static final ResourceKey<EntityType<?>> SEA_LANTERN_SHARD_PROJECTILE_KEY =
            ResourceKey.create(BuiltInRegistries.ENTITY_TYPE.key(), Identifier.fromNamespaceAndPath(AtlanWeaponry.MOD_ID, "sea_lantern_shard_projectile"));
    public static final ResourceKey<EntityType<?>> CLAM_KEY =
            ResourceKey.create(BuiltInRegistries.ENTITY_TYPE.key(), Identifier.fromNamespaceAndPath(AtlanWeaponry.MOD_ID, "clam"));
    public static final ResourceKey<EntityType<?>> HELLFORK_KEY =
            ResourceKey.create(BuiltInRegistries.ENTITY_TYPE.key(), Identifier.fromNamespaceAndPath(AtlanWeaponry.MOD_ID, "hellfork"));


    public static final EntityType<BubbleMagicProjectile> BUBBLE_MAGIC =
            Registry.register(BuiltInRegistries.ENTITY_TYPE, BUBBLE_MAGIC_KEY,
                    EntityType.Builder.<BubbleMagicProjectile>of(BubbleMagicProjectile::new, MobCategory.MISC)
                            .sized(0.3f, 0.3f).updateInterval(64).build(BUBBLE_MAGIC_KEY)
            );

    public static final EntityType<SeaLanternProjectile> SEA_LANTERN_PROJECTILE =
            Registry.register(BuiltInRegistries.ENTITY_TYPE, SEA_LANTERN_PROJECTILE_KEY,
                    EntityType.Builder.<SeaLanternProjectile>of(SeaLanternProjectile::new, MobCategory.MISC)
                            .sized(0.98F, 0.98F).clientTrackingRange(4).updateInterval(10).build(SEA_LANTERN_PROJECTILE_KEY)
            );

    public static final EntityType<SeaLanternShardProjectile> SEA_LANTERN_SHARD_PROJECTILE =
            Registry.register(BuiltInRegistries.ENTITY_TYPE, SEA_LANTERN_SHARD_PROJECTILE_KEY,
                    EntityType.Builder.<SeaLanternShardProjectile>of(SeaLanternShardProjectile::new, MobCategory.MISC)
                            .sized(0.15F, 0.15F).clientTrackingRange(4).updateInterval(10).build(SEA_LANTERN_SHARD_PROJECTILE_KEY)
            );

    public static final EntityType<ClamEntity> CLAM = Registry.register(BuiltInRegistries.ENTITY_TYPE, CLAM_KEY,
            EntityType.Builder.of(ClamEntity::new, MobCategory.WATER_AMBIENT)
                    .sized(0.7F, 0.5F).eyeHeight(0.25F).clientTrackingRange(6).build(CLAM_KEY));

    public static final EntityType<HellforkEntity> HELLFORK = Registry.register(BuiltInRegistries.ENTITY_TYPE, HELLFORK_KEY,
            EntityType.Builder.<HellforkEntity>of(HellforkEntity::new, MobCategory.MISC)
                    .noLootTable().sized(0.5F, 0.5F).eyeHeight(0.13F).clientTrackingRange(4).updateInterval(20).build(HELLFORK_KEY));


    public static void registerModEntities() {
        AtlanWeaponry.LOGGER.info("Registering Mod Entities for " + AtlanWeaponry.MOD_ID);
    }
}
