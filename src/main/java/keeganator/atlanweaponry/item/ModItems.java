package keeganator.atlanweaponry.item;

import keeganator.atlanweaponry.AtlanWeaponry;
import keeganator.atlanweaponry.datacomponents.ModDataComponents;
import keeganator.atlanweaponry.datacomponents.PoseidonsBlessingComponent;
import keeganator.atlanweaponry.effect.ModEffects;
import keeganator.atlanweaponry.entity.ModEntities;
import keeganator.atlanweaponry.item.custom.*;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Weapon;
import net.minecraft.world.level.material.Fluids;
import java.util.List;
import java.util.function.Function;

public class ModItems {

    private static final Identifier ATTACK_DAMAGE_MODIFIER_ID =
            Identifier.fromNamespaceAndPath(AtlanWeaponry.MOD_ID, "guardian_rod_attack_damage");
    private static final Identifier ATTACK_SPEED_MODIFIER_ID =
            Identifier.fromNamespaceAndPath(AtlanWeaponry.MOD_ID, "guardian_rod_attack_speed");

    public static final Item GUARDIAN_ROD = registerItem("guardian_rod", settings -> {
        ItemAttributeModifiers modifiers = ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(
                        ATTACK_DAMAGE_MODIFIER_ID, 4.0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED, new AttributeModifier(
                        ATTACK_SPEED_MODIFIER_ID, -2.2, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .build();
        return new Item(settings.attributes(modifiers).rarity(Rarity.UNCOMMON).stacksTo(16));
    });

    public static final Item CORAL_STAFF = registerItem("coral_staff", settings ->
            new CoralStaffItem(settings.durability(400).enchantable(15).rarity(Rarity.UNCOMMON)));
    public static final Item GUARDIAN_STAFF = registerItem("guardian_staff", settings ->
            new GuardianStaffItem(settings.durability(800).enchantable(15).rarity(Rarity.RARE)));
    public static final Item GUARDIAN_BATTLE_AXE = registerItem("guardian_battle_axe", settings ->
            new GuardianBattleAxeItem(settings.axe(ToolMaterial.DIAMOND, 5.0f, -3.0f).rarity(Rarity.RARE)));
    public static final Item NEPTUNES_OFFERING = registerItem("neptunes_offering", settings ->
            new Item(settings.rarity(Rarity.EPIC).stacksTo(1).component(ModDataComponents.POSEIDONS_BLESSING_COMPONENT,
                    new PoseidonsBlessingComponent(List.of(new MobEffectInstance(ModEffects.POSEIDONS_BLESSING, 48000, 0))))));
    public static final Item SHELL_HAMMER = registerItem("shell_hammer", settings ->
            new ShellHammerItem(settings.tool(ModToolMaterials.SHELL_HAMMER, BlockTags.MINEABLE_WITH_PICKAXE, 4.0F, -2.8F, 5.0F)
                    .rarity(Rarity.RARE)));
    public static final Item HELLFORK = registerItem("hellfork", settings ->
            new HellforkItem(settings.rarity(Rarity.RARE).durability(350)
                    .attributes(HellforkItem.createAttributeModifiers())
                    .component(DataComponents.TOOL, HellforkItem.createToolComponent())
                    .enchantable(1)
                    .component(DataComponents.WEAPON, new Weapon(1))
            )
    );
    public static final Item HELLFORK_SCRAP = registerItem("hellfork_scrap", settings ->
            new Item(settings.rarity(Rarity.UNCOMMON)));

    public static final Item PEARL = registerItem("pearl", settings ->
            new Item(settings.rarity(Rarity.UNCOMMON)));
    public static final Item CLAM_SPAWN_EGG = registerItem("clam_spawn_egg", settings ->
            new SpawnEggItem(settings.spawnEgg(ModEntities.CLAM)));

    public static final Item BABY_CLAM_BUCKET = registerItem("baby_clam_bucket",
            settings -> new MobBucketItem(ModEntities.CLAM, Fluids.WATER,
                    SoundEvents.BUCKET_EMPTY_FISH, settings.stacksTo(1)));
    public static final Item BABY_SQUID_BUCKET = registerItem("baby_squid_bucket",
            settings -> new MobBucketItem(EntityType.SQUID, Fluids.WATER,
                    SoundEvents.BUCKET_EMPTY_FISH, settings.stacksTo(1)));
    public static final Item BABY_GLOW_SQUID_BUCKET = registerItem("baby_glow_squid_bucket",
            settings -> new MobBucketItem(EntityType.GLOW_SQUID, Fluids.WATER,
                    SoundEvents.BUCKET_EMPTY_FISH, settings.stacksTo(1)));
    public static final Item BABY_TURTLE_BUCKET = registerItem("baby_turtle_bucket",
            settings -> new MobBucketItem(EntityType.TURTLE, Fluids.WATER,
                    SoundEvents.BUCKET_EMPTY_FISH, settings.stacksTo(1)));
    public static final Item BABY_NAUTILUS_BUCKET = registerItem("baby_nautilus_bucket",
            settings -> new MobBucketItem(EntityType.NAUTILUS, Fluids.WATER,
                    SoundEvents.BUCKET_EMPTY_FISH, settings.stacksTo(1)));

    private static Item registerItem(String name, Function<Item.Properties, Item> function) {
        return Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(AtlanWeaponry.MOD_ID, name),
                function.apply(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(AtlanWeaponry.MOD_ID, name)))));
    }

    public static void registerModItems() {
        AtlanWeaponry.LOGGER.info("Registering Mod Items for " + AtlanWeaponry.MOD_ID);

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT).register(entries -> {
            entries.accept(ModItems.CORAL_STAFF);
            entries.accept(ModItems.GUARDIAN_STAFF);
            entries.accept(ModItems.GUARDIAN_BATTLE_AXE);
            entries.accept(ModItems.NEPTUNES_OFFERING);
            entries.accept(ModItems.SHELL_HAMMER);
            entries.accept(ModItems.HELLFORK);

        });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register(entries -> {
            entries.accept(ModItems.GUARDIAN_ROD);
            entries.accept(ModItems.PEARL);
            entries.accept(ModItems.HELLFORK_SCRAP);
        });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.SPAWN_EGGS).register(entries -> {
            entries.accept(ModItems.CLAM_SPAWN_EGG);
        });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(entries -> {
            entries.accept(ModItems.BABY_CLAM_BUCKET);
            entries.accept(ModItems.BABY_SQUID_BUCKET);
            entries.accept(ModItems.BABY_GLOW_SQUID_BUCKET);
            entries.accept(ModItems.BABY_TURTLE_BUCKET);
            entries.accept(ModItems.BABY_NAUTILUS_BUCKET);
        });
    }
}
