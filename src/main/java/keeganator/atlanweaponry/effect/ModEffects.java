package keeganator.atlanweaponry.effect;

import keeganator.atlanweaponry.AtlanWeaponry;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;

public class ModEffects {

    public static Holder<MobEffect> POSEIDONS_BLESSING;
    public static Holder<MobEffect> STUNNED;

    public static void registerEffects() {
        POSEIDONS_BLESSING = Registry.registerForHolder(
                BuiltInRegistries.MOB_EFFECT, Identifier.fromNamespaceAndPath(AtlanWeaponry.MOD_ID, "poseidons_blessing"), new PoseidonsBlessingEffect()
        );
        STUNNED = Registry.registerForHolder(
                BuiltInRegistries.MOB_EFFECT, Identifier.fromNamespaceAndPath(AtlanWeaponry.MOD_ID, "stunned"), new StunnedEffect()
        );
    }
}