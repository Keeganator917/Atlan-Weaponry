package keeganator.atlanweaponry;

import keeganator.atlanweaponry.entity.client.ModEntityRenderers;
import keeganator.atlanweaponry.item.ModItems;
import keeganator.atlanweaponry.particles.ModParticles;
import keeganator.atlanweaponry.particles.ShellStunParticle;
import keeganator.atlanweaponry.util.network.ModNetworking;
import keeganator.atlanweaponry.util.network.ShowNeptunesOfferingPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;

public class AtlanWeaponryClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModEntityRenderers.register();
        ModNetworking.registerClient();

        ClientPlayNetworking.registerGlobalReceiver(
                ShowNeptunesOfferingPayload.ID,
                (payload, context) -> context.client().execute(() -> {
                    LocalPlayer player = context.client().player;
                    if (player != null) {
                        player.displayItemActivation(new ItemStack(ModItems.NEPTUNES_OFFERING));
                    }
                })
        );

        ParticleProviderRegistry.getInstance().register(
                ModParticles.SHELL_STUN, ShellStunParticle.Provider::new
        );
    }
}
