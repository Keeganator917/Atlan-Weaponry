package keeganator.atlanweaponry.block;

import keeganator.atlanweaponry.AtlanWeaponry;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class ModBlocks {
    public static final ResourceKey<Block> CRYSTALLIZED_HORN_CORAL_KEY =
            ResourceKey.create(Registries.BLOCK, id("crystallized_horn_coral"));
    public static final ResourceKey<Block> CRYSTALLIZED_TUBE_CORAL_KEY =
            ResourceKey.create(Registries.BLOCK, id("crystallized_tube_coral"));
    public static final ResourceKey<Block> CRYSTALLIZED_BRAIN_CORAL_KEY =
            ResourceKey.create(Registries.BLOCK, id("crystallized_brain_coral"));
    public static final ResourceKey<Block> CRYSTALLIZED_BUBBLE_CORAL_KEY =
            ResourceKey.create(Registries.BLOCK, id("crystallized_bubble_coral"));
    public static final ResourceKey<Block> CRYSTALLIZED_FIRE_CORAL_KEY =
            ResourceKey.create(Registries.BLOCK, id("crystallized_fire_coral"));

    public static final Block CRYSTALLIZED_HORN_CORAL = new Block(
            BlockBehaviour.Properties.of().setId(CRYSTALLIZED_HORN_CORAL_KEY).strength(1.5f).mapColor(MapColor.COLOR_YELLOW)
                    .requiresCorrectToolForDrops().sound(SoundType.AMETHYST));
    public static final Block CRYSTALLIZED_TUBE_CORAL = new Block(
            BlockBehaviour.Properties.of().setId(CRYSTALLIZED_TUBE_CORAL_KEY).strength(1.5f).mapColor(MapColor.COLOR_BLUE)
                    .requiresCorrectToolForDrops().sound(SoundType.AMETHYST));
    public static final Block CRYSTALLIZED_BRAIN_CORAL = new Block(
            BlockBehaviour.Properties.of().setId(CRYSTALLIZED_BRAIN_CORAL_KEY).strength(1.5f).mapColor(MapColor.COLOR_PINK)
                    .requiresCorrectToolForDrops().sound(SoundType.AMETHYST));
    public static final Block CRYSTALLIZED_BUBBLE_CORAL = new Block(
            BlockBehaviour.Properties.of().setId(CRYSTALLIZED_BUBBLE_CORAL_KEY).strength(1.5f).mapColor(MapColor.COLOR_PURPLE)
                    .requiresCorrectToolForDrops().sound(SoundType.AMETHYST));
    public static final Block CRYSTALLIZED_FIRE_CORAL = new Block(
            BlockBehaviour.Properties.of().setId(CRYSTALLIZED_FIRE_CORAL_KEY).strength(1.5f).mapColor(MapColor.COLOR_RED)
                    .requiresCorrectToolForDrops().sound(SoundType.AMETHYST));

    public static void registerModBlocks() {
        register("crystallized_horn_coral", CRYSTALLIZED_HORN_CORAL);
        register("crystallized_tube_coral", CRYSTALLIZED_TUBE_CORAL);
        register("crystallized_brain_coral", CRYSTALLIZED_BRAIN_CORAL);
        register("crystallized_bubble_coral", CRYSTALLIZED_BUBBLE_CORAL);
        register("crystallized_fire_coral", CRYSTALLIZED_FIRE_CORAL);

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.BUILDING_BLOCKS).register(entries ->{
            entries.insertAfter(Items.DARK_PRISMARINE_SLAB,
                    CRYSTALLIZED_TUBE_CORAL,
                    CRYSTALLIZED_BRAIN_CORAL,
                    CRYSTALLIZED_BUBBLE_CORAL,
                    CRYSTALLIZED_FIRE_CORAL,
                    CRYSTALLIZED_HORN_CORAL);
        });
    }


    private static void register(String name, Block block) {
        Identifier id = id(name);

        Registry.register(BuiltInRegistries.BLOCK, id, block);
        Registry.register(BuiltInRegistries.ITEM, id, new BlockItem(block, new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))
                .useBlockDescriptionPrefix()));
    }

    private static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(AtlanWeaponry.MOD_ID, path);
    }
}
