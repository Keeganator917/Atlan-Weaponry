package keeganator.atlanweaponry.entity.client;

import keeganator.atlanweaponry.entity.ModEntities;
import keeganator.atlanweaponry.entity.client.clam.ClamEntityModel;
import keeganator.atlanweaponry.entity.client.clam.ClamEntityRenderer;
import keeganator.atlanweaponry.entity.client.clam.ClamModelLayers;
import keeganator.atlanweaponry.entity.client.hellfork.HellforkEntityRenderer;
import keeganator.atlanweaponry.entity.client.sea_lantern_shard.SeaLanternShardProjectileModel;
import keeganator.atlanweaponry.entity.client.sea_lantern_shard.SeaLanternShardProjectileRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

public class ModEntityRenderers {
    public static void register() {
        EntityRenderers.register(
                ModEntities.BUBBLE_MAGIC,
                ThrownItemRenderer::new
        );
        EntityRenderers.register(
                ModEntities.SEA_LANTERN_PROJECTILE,
                SeaLanternProjectileRenderer::new
        );
        EntityRenderers.register(
                ModEntities.SEA_LANTERN_SHARD_PROJECTILE,
                SeaLanternShardProjectileRenderer::new
        );
        ModelLayerRegistry.registerModelLayer(
                SeaLanternShardProjectileModel.LAYER_LOCATION,
                SeaLanternShardProjectileModel::getTexturedModelData
        );

        ModelLayerRegistry.registerModelLayer(ClamModelLayers.CLAM, ClamEntityModel::getTexturedModelData);
        EntityRenderers.register(ModEntities.CLAM, ClamEntityRenderer::new);

        EntityRenderers.register(ModEntities.HELLFORK, HellforkEntityRenderer::new);

    }
}
