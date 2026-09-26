package keeganator.atlanweaponry.loottables;

import keeganator.atlanweaponry.item.ModItems;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProviders;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;


public class ModLootTableModifiers {

    private static final Identifier ELDER_GUARDIAN_LOOT =
            Identifier.withDefaultNamespace("entities/elder_guardian");

    public static void modifyLootTables() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {

            if (!source.isBuiltin()) {
                return;
            }

            if (ELDER_GUARDIAN_LOOT.equals(key.identifier())) {

                LootPool pool = LootPool.lootPool()
                        .setRolls(ContextIntProviders.exactly(1))
                        .when(LootItemRandomChanceCondition.randomChance(0.5f))
                        .add(LootItem.lootTableItem(ModItems.GUARDIAN_ROD)
                                .apply(SetItemCountFunction.setCount(ContextIntProviders.exactly(1)))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(
                                        registries.lookupOrThrow(Registries.ENCHANTMENT),
                                        ContextFloatProviders.between(0.0F, 1.0F)
                                ))
                        )
                        .build();

                tableBuilder.pool(pool);
            }
        });

        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (key.identifier().equals(Identifier.fromNamespaceAndPath("minecraft", "chests/bastion_treasure"))) {
                tableBuilder.withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1)).add(LootItem.lootTableItem(ModItems.HELLFORK_SCRAP).setWeight(10)));
            }
        });
    }
}
