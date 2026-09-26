package keeganator.atlanweaponry.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import keeganator.atlanweaponry.entity.SeaLanternProjectile;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.Blocks;

public class SeaLanternProjectileRenderer extends EntityRenderer<SeaLanternProjectile, EntityRenderState> {

    private static final BlockDisplayContext BLOCK_DISPLAY_CONTEXT = BlockDisplayContext.create();
    private final BlockModelResolver blockModelResolver;

    public SeaLanternProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.blockModelResolver = context.getBlockModelResolver();
    }

    @Override
    public EntityRenderState createRenderState() {
        return new EntityRenderState();
    }

    @Override
    public void extractRenderState(SeaLanternProjectile entity, EntityRenderState state, float tickDelta) {
        super.extractRenderState(entity, state, tickDelta);

        state.ageInTicks = entity.tickCount + tickDelta;
    }

    @Override
    public void submit(EntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();

        poseStack.translate(-0.5F, 0.0F, -0.5F);

        BlockModelRenderState blockModelRenderState = new BlockModelRenderState();

        this.blockModelResolver.update(blockModelRenderState, Blocks.SEA_LANTERN.defaultBlockState(), BLOCK_DISPLAY_CONTEXT);

        blockModelRenderState.submit(poseStack, submitNodeCollector, 0xF000F0, OverlayTexture.NO_OVERLAY, state.outlineColor);

        poseStack.popPose();

        super.submit(state, poseStack, submitNodeCollector, camera);
    }
}