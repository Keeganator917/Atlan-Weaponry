package keeganator.atlanweaponry.util;

import keeganator.atlanweaponry.AtlanWeaponry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {

    public static class Blocks {
        public static final TagKey<Block> CRYSTALLIZED_CORAL = createTag("crystallized_coral");

        private static TagKey<Block> createTag(String name) {
            return TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(AtlanWeaponry.MOD_ID, name));
        }
    }

    public static class Items {

        public static final TagKey<Item> GUARDIAN_STAFF = createTag("guardian_staff");
        public static final TagKey<Item> GUARDIAN_BATTLE_AXE = createTag("guardian_battle_axe");
        public static final TagKey<Item> NAUTILUS_SHELL = createTag("nautilus_shell");
        public static final TagKey<Item> ATLAN_WEAPONS = createTag("atlan_weapons");

        public static final TagKey<Item> BABY_MOB_BUCKETS = createTag("baby_mob_buckets");

        private static TagKey<Item> createTag(String name) {
            return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(AtlanWeaponry.MOD_ID, name));
        }
    }
}
