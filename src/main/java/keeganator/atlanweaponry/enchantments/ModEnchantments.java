package keeganator.atlanweaponry.enchantments;

import keeganator.atlanweaponry.AtlanWeaponry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;

public class ModEnchantments {

        public static final ResourceKey<Enchantment> SHATTERING =
                ResourceKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath(AtlanWeaponry.MOD_ID, "shattering"));
        public static final ResourceKey<Enchantment> FIERY_LANTERNS =
                ResourceKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath(AtlanWeaponry.MOD_ID, "fiery_lanterns"));
        public static final ResourceKey<Enchantment> WHIRLPOOL =
                ResourceKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath(AtlanWeaponry.MOD_ID, "whirlpool"));
        public static final ResourceKey<Enchantment> TORRENT =
                ResourceKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath(AtlanWeaponry.MOD_ID, "torrent"));
}
