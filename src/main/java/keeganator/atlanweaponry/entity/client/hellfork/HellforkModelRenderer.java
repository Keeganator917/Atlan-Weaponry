package keeganator.atlanweaponry.entity.client.hellfork;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.Vector3fc;

import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class HellforkModelRenderer implements NoDataSpecialModelRenderer {
    private final HellforkEntityModel model;

    public HellforkModelRenderer(HellforkEntityModel model) {
        this.model = model;
    }

    @Override
    public void getExtents(Consumer<Vector3fc> consumer) {
        PoseStack matrixStack = new PoseStack();
        matrixStack.scale(1.0F, -1.0F, -1.0F);
        this.model.root().getExtentsForGui(matrixStack, consumer);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
        poseStack.pushPose();

        poseStack.scale(1.0F, -1.0F, -1.0F);

        submitNodeCollector.submitModelPart(this.model.root(), poseStack, this.model.renderType(HellforkEntityModel.TEXTURE), lightCoords, overlayCoords, null);

        RenderType glowLayer = RenderTypes.breezeEyes(HellforkEntityModel.GLOW_TEXTURE);

        submitNodeCollector.submitModelPart(this.model.root(), poseStack, glowLayer, 0xF000F0, overlayCoords, null);

        poseStack.popPose();
    }

    @Environment(EnvType.CLIENT)
    public record Unbaked() implements SpecialModelRenderer.Unbaked {
        public static final MapCodec<HellforkModelRenderer.Unbaked> CODEC = MapCodec.unit(new HellforkModelRenderer.Unbaked());

        @Override
        public MapCodec<HellforkModelRenderer.Unbaked> type() {
            return CODEC;
        }

        @Override
        public SpecialModelRenderer<?> bake(SpecialModelRenderer.BakingContext context) {
            return new HellforkModelRenderer(new HellforkEntityModel(context.entityModelSet().bakeLayer(ModelLayers.TRIDENT)));
        }
    }
}
