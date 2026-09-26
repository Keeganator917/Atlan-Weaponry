package keeganator.atlanweaponry.mixin;

import com.mojang.serialization.MapCodec;
import keeganator.atlanweaponry.AtlanWeaponry;
import keeganator.atlanweaponry.entity.client.hellfork.HellforkModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderers;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpecialModelRenderers.class)
public class SpecialModelTypesMixin {

    @Shadow
    @Final
    public static ExtraCodecs.LateBoundIdMapper<Identifier, MapCodec<? extends SpecialModelRenderer.Unbaked>> ID_MAPPER;

    @Inject(method = "bootstrap", at = @At("TAIL"))
    private static void registerHellforkRenderer(CallbackInfo ci) {
        ID_MAPPER.put(Identifier.fromNamespaceAndPath(AtlanWeaponry.MOD_ID, "hellfork"), HellforkModelRenderer.Unbaked.CODEC);
    }
}