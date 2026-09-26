package keeganator.atlanweaponry.entity.client.sea_lantern_shard;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.resources.Identifier;

public class SeaLanternShardProjectileModel extends EntityModel<EntityRenderState> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath("atlanweaponry", "sea_lantern_shard"), "main");

    private final ModelPart bb_main;

    public SeaLanternShardProjectileModel(ModelPart root) {
        super(root);
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();

        modelPartData.addOrReplaceChild("bb_main", CubeListBuilder.create()
                        .texOffs(0, 3)
                        .addBox(-0.5F, -3.0F, -1.0F, 0.1F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(4, 0)
                        .addBox(-1.0F, -3.0F, -0.5F, 1.0F, 2.0F, 0.1F, new CubeDeformation(0.0F))
                        .texOffs(0, 0)
                        .addBox(-0.5F, -1.0F, -1.5F, 0.1F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                        .texOffs(2, 3)
                        .addBox(-1.5F, -1.0F, -0.5F, 2.0F, 1.0F, 0.1F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, 0.0F)
        );
        return LayerDefinition.create(modelData, 8, 8);
    }

    @Override
    public void setupAnim(EntityRenderState state) {

    }
}