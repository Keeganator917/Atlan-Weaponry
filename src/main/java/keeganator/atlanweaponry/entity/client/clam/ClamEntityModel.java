package keeganator.atlanweaponry.entity.client.clam;

import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import org.joml.Vector3f;

public class ClamEntityModel extends EntityModel<ClamEntityRenderState> {

    private final ModelPart bottomShell;
    private final ModelPart topShell;
    private final ModelPart tongue;

    private final KeyframeAnimation idle;
    private final KeyframeAnimation open;

    private final Vector3f tempVec = new Vector3f();

    public ClamEntityModel(ModelPart root) {
        super(root.getChild("Clam"));
        ModelPart clamBone = this.root();
        this.bottomShell = clamBone.getChild("Bottom Shell");
        this.topShell = clamBone.getChild("Top Shell");
        this.tongue = clamBone.getChild("Tongue");

        this.idle = ClamEntityAnimation.IDLE.bake(root);
        this.open = ClamEntityAnimation.OPEN.bake(root);
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();

        PartDefinition clam = modelPartData.addOrReplaceChild("Clam", CubeListBuilder.create(), PartPose.offset(4.0F, 21.0F, 5.0F));

        PartDefinition bottomShell = clam.addOrReplaceChild("Bottom Shell", CubeListBuilder.create()
                        .texOffs(48, 0).addBox(1.0F, -1.0F, -11.0F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
                        .texOffs(40, 46).addBox(-10.0F, -1.0F, -11.0F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 0).addBox(-10.0F, 0.0F, -11.0F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 28).addBox(-9.0F, 2.0F, -10.0F, 10.0F, 1.0F, 10.0F, new CubeDeformation(0.0F)),
                PartPose.ZERO);

        bottomShell.addOrReplaceChild("cube_r1", CubeListBuilder.create()
                        .texOffs(26, 59).addBox(0.0F, -1.0F, -1.0F, 1.0F, 1.0F, 10.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -11.0F, 0.0F, -1.5708F, 0.0F));

        bottomShell.addOrReplaceChild("cube_r2", CubeListBuilder.create()
                        .texOffs(48, 59).addBox(0.0F, -1.0F, -1.0F, 1.0F, 1.0F, 10.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition topShell = clam.addOrReplaceChild("Top Shell", CubeListBuilder.create(),
                PartPose.offsetAndRotation(1.0F, -1.0F, -10.0F, 0.0F, 0.0F, 0.0F));

        topShell.addOrReplaceChild("cube_r3", CubeListBuilder.create()
                        .texOffs(40, 37).addBox(-7.0F, -1.0F, -1.0F, 8.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-2.0F, -4.0F, 2.0F, 0.2618F, 0.0F, 0.0F));

        topShell.addOrReplaceChild("cube_r4", CubeListBuilder.create()
                        .texOffs(0, 39).addBox(-9.0F, -1.0F, -1.0F, 10.0F, 1.0F, 10.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1.0F, -3.0F, 1.0F, 0.2618F, 0.0F, 0.0F));

        topShell.addOrReplaceChild("cube_r5", CubeListBuilder.create()
                        .texOffs(0, 50).addBox(0.0F, -1.0F, -1.0F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
                        .texOffs(48, 13).addBox(11.0F, -1.0F, -1.0F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-11.0F, 0.0F, 0.25F, 0.2618F, 0.0F, 0.0F));

        topShell.addOrReplaceChild("cube_r6", CubeListBuilder.create()
                        .texOffs(0, 63).addBox(-9.0F, -2.75F, -0.25F, 10.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1.0F, 1.75F, 0.0F, 0.2618F, 0.0F, 0.0F));

        topShell.addOrReplaceChild("cube_r7", CubeListBuilder.create()
                        .texOffs(48, 26).addBox(-9.0F, -2.75F, -0.25F, 10.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1.0F, -1.0F, 10.75F, 0.2618F, 0.0F, 0.0F));

        topShell.addOrReplaceChild("cube_r8", CubeListBuilder.create()
                        .texOffs(0, 14).addBox(-11.0F, -2.0F, -1.0F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

        clam.addOrReplaceChild("Tongue", CubeListBuilder.create()
                        .texOffs(40, 28).addBox(-8.0F, -1.0F, -10.0F, 8.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)),
                PartPose.ZERO);

        return LayerDefinition.create(modelData, 128, 128);
    }

    @Override
    public void setupAnim(ClamEntityRenderState state) {
        this.resetPose();

        if (state.openAnimationState.isStarted()) {
            long runningTime = state.openAnimationState.getTimeInMillis(state.ageInTicks);
            this.open.apply(runningTime, 1.0F);
        } else {
            long runningTime = state.idleAnimationState.getTimeInMillis(state.ageInTicks);
            this.idle.apply(runningTime, 1.0F);
        }
    }

    public ModelPart getTonguePart() {
        return this.tongue;
    }
}