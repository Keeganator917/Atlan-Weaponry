package keeganator.atlanweaponry.entity.client.clam;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.joml.Quaternionf;

public class ClamHeldItemFeatureRenderer extends RenderLayer<ClamEntityRenderState, ClamEntityModel> {

    public ClamHeldItemFeatureRenderer(RenderLayerParent<ClamEntityRenderState, ClamEntityModel> context) {
        super(context);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, ClamEntityRenderState state, float limbAngle, float limbDistance) {
        ItemStackRenderState itemRenderState = state.mainHandItemState.item;

        if (itemRenderState.isEmpty()) {
            return;
        }

        ModelPart tongue = this.getParentModel().getTonguePart();

        poseStack.pushPose();

        if (tongue.zRot != 0.0F || tongue.yRot != 0.0F || tongue.xRot != 0.0F) {
            poseStack.mulPose(new Quaternionf().rotationZYX(tongue.zRot, tongue.yRot, tongue.xRot));
        }

        poseStack.mulPose(new Quaternionf().rotationX((float) Math.PI));
        poseStack.translate(0.0, -1.3, 0.0);
        poseStack.scale(0.5F, 0.5F, 0.5F);

        itemRenderState.submit(poseStack, submitNodeCollector, light, OverlayTexture.NO_OVERLAY, 0);

        poseStack.popPose();
    }
}