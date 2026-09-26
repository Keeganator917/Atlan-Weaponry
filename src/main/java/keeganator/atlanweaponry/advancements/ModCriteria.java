package keeganator.atlanweaponry.advancements;

import keeganator.atlanweaponry.AtlanWeaponry;
import net.minecraft.advancements.triggers.CriterionTrigger;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public class ModCriteria {

    public static final ThrownIntoWaterCriterion THROWN_INTO_WATER = register("thrown_into_water", new ThrownIntoWaterCriterion());

    public static <T extends CriterionTrigger<?>> T register(String id, T criterion) {
        return Registry.register(BuiltInRegistries.TRIGGER_TYPES, Identifier.fromNamespaceAndPath(AtlanWeaponry.MOD_ID, id), criterion);
    }

    public static void init() {
    }

}
