package keeganator.atlanweaponry.entity.client.hellfork;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import keeganator.atlanweaponry.AtlanWeaponry;
import keeganator.atlanweaponry.entity.HellforkEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ThrownTridentRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;
import org.joml.Matrix4f;

@Environment(EnvType.CLIENT)
public class HellforkEntityRenderer extends EntityRenderer<HellforkEntity, ThrownTridentRenderState> {
    public static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(AtlanWeaponry.MOD_ID, "textures/item/hellfork.png");
    private final HellforkEntityModel model;

    public HellforkEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new HellforkEntityModel(context.bakeLayer(ModelLayers.TRIDENT));
    }

    public void submit(ThrownTridentRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();

        poseStack.mulPose(new Matrix4f().rotation(Axis.YP.rotationDegrees(state.yRot - 90.0F)));
        poseStack.mulPose(new Matrix4f().rotation(Axis.ZP.rotationDegrees(state.xRot + 90.0F)));

        submitNodeCollector.order(0).submitModel(this.model, Unit.INSTANCE, poseStack, TEXTURE, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);

        if (state.isFoil) {
            submitNodeCollector.order(1).submitModel(this.model, Unit.INSTANCE, poseStack, this.model.renderType(TEXTURE), state.lightCoords,
                    OverlayTexture.NO_OVERLAY, state.outlineColor);
        }

        RenderType glowLayer = RenderTypes.breezeEyes(HellforkEntityModel.GLOW_TEXTURE);

        submitNodeCollector.order(2).submitModel(this.model, Unit.INSTANCE, poseStack, glowLayer, 0xF000F0, OverlayTexture.NO_OVERLAY,
                state.outlineColor);

        poseStack.popPose();

        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    public ThrownTridentRenderState createRenderState() {
        return new ThrownTridentRenderState();
    }

    @Override
    public void extractRenderState(HellforkEntity entity, ThrownTridentRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.yRot = entity.getYRot(partialTicks);
        state.xRot = entity.getXRot(partialTicks);
        state.isFoil = entity.isEnchanted();
    }
}