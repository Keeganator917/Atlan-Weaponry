package keeganator.atlanweaponry.entity.client.clam;

import com.mojang.blaze3d.vertex.PoseStack;
import keeganator.atlanweaponry.AtlanWeaponry;
import keeganator.atlanweaponry.entity.ClamEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;

public class ClamEntityRenderer extends MobRenderer<ClamEntity, ClamEntityRenderState, ClamEntityModel> {

    private static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath(AtlanWeaponry.MOD_ID, "textures/entity/clam.png");

    private final ItemModelResolver itemModelManager;

    public ClamEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new ClamEntityModel(context.bakeLayer(ClamModelLayers.CLAM)), 0.3F);
        this.itemModelManager = context.getItemModelResolver();

        this.addLayer(new ClamHeldItemFeatureRenderer(this));
    }

    @Override
    public ClamEntityRenderState createRenderState() {
        return new ClamEntityRenderState();
    }

    @Override
    public void extractRenderState(ClamEntity entity, ClamEntityRenderState state, float tickDelta) {
        super.extractRenderState(entity, state, tickDelta);
        state.idleAnimationState = entity.idleAnimationState;
        state.openAnimationState = entity.openAnimationState;

        state.ageInTicks = entity.tickCount + tickDelta;
        state.isBaby = entity.isBaby();

        state.mainHandItemState.extractItemGroupRenderState(entity, entity.getItemBySlot(EquipmentSlot.MAINHAND), this.itemModelManager);
    }

    @Override
    public Identifier getTextureLocation(ClamEntityRenderState livingEntityRenderState) {
        return TEXTURE;
    }

    @Override
    protected void scale(ClamEntityRenderState state, PoseStack matrices) {
        if (state.isBaby) {
            matrices.scale(0.5F, 0.5F, 0.5F);
        }
    }

}
