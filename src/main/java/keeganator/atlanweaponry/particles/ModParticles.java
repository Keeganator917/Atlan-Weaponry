package keeganator.atlanweaponry.particles;

import keeganator.atlanweaponry.AtlanWeaponry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public class ModParticles {

    public static final SimpleParticleType SHELL_STUN =
            register("shell_stun", true);

    private static SimpleParticleType register(String name, boolean alwaysShow) {
        return Registry.register(
                BuiltInRegistries.PARTICLE_TYPE,
                Identifier.fromNamespaceAndPath(AtlanWeaponry.MOD_ID, name),
                FabricParticleTypes.simple(alwaysShow)
        );
    }

    public static void init() {
        //intentionally empty to force game to load class
    }
}
