package keeganator.atlanweaponry.entity.client.clam;

import net.minecraft.client.renderer.entity.state.ItemClusterRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.AnimationState;

public class ClamEntityRenderState extends LivingEntityRenderState {
    public final ItemClusterRenderState mainHandItemState = new ItemClusterRenderState();
    public AnimationState idleAnimationState = new AnimationState();
    public AnimationState openAnimationState = new AnimationState();

    public float ageInTicks;
}