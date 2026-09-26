package keeganator.atlanweaponry.entity.client.hellfork;

import keeganator.atlanweaponry.AtlanWeaponry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;

@Environment(EnvType.CLIENT)
public class HellforkEntityModel extends Model<Unit> {
    public static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(AtlanWeaponry.MOD_ID, "textures/item/hellfork.png");
    public static final Identifier GLOW_TEXTURE = Identifier.fromNamespaceAndPath(AtlanWeaponry.MOD_ID, "textures/item/hellfork_glow.png");

    public HellforkEntityModel(ModelPart root) {
        super(root, RenderTypes::entitySolid);
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        PartDefinition modelPartData2 = modelPartData.addOrReplaceChild(
                "pole", CubeListBuilder.create().texOffs(0, 6).addBox(-0.5F, 2.0F, -0.5F, 1.0F, 25.0F, 1.0F), PartPose.ZERO
        );
        modelPartData2.addOrReplaceChild("base", CubeListBuilder.create().texOffs(4, 0).addBox(-1.5F, 0.0F, -0.5F, 3.0F, 2.0F, 1.0F), PartPose.ZERO);
        modelPartData2.addOrReplaceChild("left_spike", CubeListBuilder.create().texOffs(4, 3).addBox(-2.5F, -3.0F, -0.5F, 1.0F, 4.0F, 1.0F), PartPose.ZERO);
        modelPartData2.addOrReplaceChild("middle_spike", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 4.0F, 1.0F), PartPose.ZERO);
        modelPartData2.addOrReplaceChild("right_spike", CubeListBuilder.create().texOffs(4, 3).mirror().addBox(1.5F, -3.0F, -0.5F, 1.0F, 4.0F, 1.0F), PartPose.ZERO);
        return LayerDefinition.create(modelData, 32, 32);
    }
}
