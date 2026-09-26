package keeganator.atlanweaponry.entity.client;

import keeganator.atlanweaponry.entity.BubbleMagicProjectile;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

public class BubbleMagicRenderer extends ThrownItemRenderer<BubbleMagicProjectile> {

    public BubbleMagicRenderer(EntityRendererProvider.Context ctx, float scale, boolean lit) {
        super(ctx, scale, lit);
    }
}
