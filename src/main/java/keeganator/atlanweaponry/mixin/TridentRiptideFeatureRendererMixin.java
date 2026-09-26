package keeganator.atlanweaponry.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import keeganator.atlanweaponry.AtlanWeaponry;
import keeganator.atlanweaponry.item.ModItems;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.SpinAttackEffectLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpinAttackEffectLayer.class)
public class TridentRiptideFeatureRendererMixin {

    @Unique
    private static final Identifier HELLFORK_RIPTIDE_TEXTURE =
            Identifier.fromNamespaceAndPath(AtlanWeaponry.MOD_ID, "textures/entity/hellfork_riptide.png");

    @Unique
    private AvatarRenderState atlanweaponry$currentState;

    @Inject(method = "submit", at = @At("HEAD"))
    private void atlanweaponry$captureState(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, AvatarRenderState state, float yRot, float xRot, CallbackInfo ci) {
        this.atlanweaponry$currentState = state;
    }

    @ModifyArg(method = "submit", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/resources/Identifier;I I I Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V"), index = 3)
    private Identifier atlanweaponry$swapRiptideTexture(Identifier texture) {
        AvatarRenderState state = this.atlanweaponry$currentState;

        if (state != null) {
            ItemStack mainHand = state.rightHandItemStack;
            ItemStack offHand = state.leftHandItemStack;

            if (mainHand.getItem() == ModItems.HELLFORK || offHand.getItem() == ModItems.HELLFORK) {
                return HELLFORK_RIPTIDE_TEXTURE;
            }
        }

        return texture;
    }
}