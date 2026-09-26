package keeganator.atlanweaponry.entity.client.sea_lantern_shard;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import keeganator.atlanweaponry.AtlanWeaponry;
import keeganator.atlanweaponry.entity.SeaLanternShardProjectile;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Brightness;
import net.minecraft.world.phys.Vec3;

public class SeaLanternShardProjectileRenderer extends EntityRenderer<SeaLanternShardProjectile, SeaLanternShardRenderState> {

    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(AtlanWeaponry.MOD_ID, "textures/entity/sea_lantern_shard.png");

    private final SeaLanternShardProjectileModel model;

    public SeaLanternShardProjectileRenderer(
            EntityRendererProvider.Context context) {
                super(context);
                this.model = new SeaLanternShardProjectileModel(context.bakeLayer(SeaLanternShardProjectileModel.LAYER_LOCATION));
    }

    @Override
    public SeaLanternShardRenderState createRenderState() {
        return new SeaLanternShardRenderState();
    }

    @Override
    public void extractRenderState(SeaLanternShardProjectile entity, SeaLanternShardRenderState state, float tickDelta) {
        super.extractRenderState(entity, state, tickDelta);

        state.ageInTicks = entity.tickCount + tickDelta;

        Vec3 velocity = entity.getDeltaMovement();

        double horizontalLength = Math.sqrt(velocity.x * velocity.x + velocity.z * velocity.z);

        state.yaw = (float)(Math.toDegrees(Math.atan2(velocity.z, velocity.x))) - 90F;
        state.pitch = (float)(-Math.toDegrees(Math.atan2(velocity.y, horizontalLength)));
    }

    @Override
    public void submit(SeaLanternShardRenderState state, PoseStack matrices, SubmitNodeCollector queue, CameraRenderState cameraState) {
        matrices.pushPose();
        matrices.scale(1.5F, 1.5F, 1.5F);

        matrices.mulPose(Axis.YP.rotationDegrees(state.yaw + 180F));
        matrices.mulPose(Axis.XP.rotationDegrees(state.pitch));

        model.setupAnim(state);

        queue.submitModel(model, state, matrices, model.renderType(TEXTURE), 0xF000F0, OverlayTexture.NO_OVERLAY, 0, null);

        matrices.popPose();

        super.submit(state, matrices, queue, cameraState);
    }
}