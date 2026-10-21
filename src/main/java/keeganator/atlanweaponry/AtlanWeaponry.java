package keeganator.atlanweaponry;

import keeganator.atlanweaponry.advancements.ModCriteria;
import keeganator.atlanweaponry.block.ModBlocks;
import keeganator.atlanweaponry.datacomponents.ModDataComponents;
import keeganator.atlanweaponry.datacomponents.TorrentTravelComponent;
import keeganator.atlanweaponry.effect.ModEffects;
import keeganator.atlanweaponry.enchantments.ModEnchantmentEffects;
import keeganator.atlanweaponry.entity.ClamEntity;
import keeganator.atlanweaponry.entity.ModEntities;
import keeganator.atlanweaponry.item.ModItems;
import keeganator.atlanweaponry.loottables.ModLootTableModifiers;
import keeganator.atlanweaponry.particles.ModParticles;
import keeganator.atlanweaponry.sound.ModSounds;
import keeganator.atlanweaponry.util.ModTags;
import keeganator.atlanweaponry.util.ThunderstormBuffs;
import keeganator.atlanweaponry.util.network.ModNetworking;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.registry.CompostableRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class AtlanWeaponry implements ModInitializer {
	public static final String MOD_ID = "atlanweaponry";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModEffects.registerEffects();
		ModDataComponents.initialize();
		ModNetworking.register();

		ModItems.registerModItems();
		ModBlocks.registerModBlocks();

		ModEntities.registerModEntities();

		ModCriteria.init();
		ModSounds.initialize();

		ModEnchantmentEffects.registerEnchantmentEffects();
		ModLootTableModifiers.modifyLootTables();
		ModParticles.init();

		TorrentTravelComponent.registerTickEvent();

		ServerTickEvents.END_LEVEL_TICK.register(ThunderstormBuffs::tick);


		AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
			if (player.hasEffect(ModEffects.STUNNED)) {
				return InteractionResult.FAIL;
			}
			return InteractionResult.PASS;
		});

		FabricDefaultAttributeRegistry.register(ModEntities.CLAM, ClamEntity.createClamAttributes());

		SpawnPlacements.register(ModEntities.CLAM, SpawnPlacementTypes.IN_WATER, Heightmap.Types.OCEAN_FLOOR, ClamEntity::checkSurfaceAgeableWaterCreatureSpawnRules);

		BiomeModifications.addSpawn(
				BiomeSelectors.includeByKey(Biomes.WARM_OCEAN, Biomes.DEEP_LUKEWARM_OCEAN),
				MobCategory.WATER_AMBIENT, ModEntities.CLAM, 80, 1, 2
		);

		CompostableRegistry.INSTANCE.add(ModItems.PEARL, 1.0f);

		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			if (world.isClientSide()) return InteractionResult.PASS;

			ItemStack stack = player.getItemInHand(hand);
			if (!stack.is(Items.POTION)) return InteractionResult.PASS;

			var contents = stack.get(DataComponents.POTION_CONTENTS);
			if (contents == null) return InteractionResult.PASS;

			List<MobEffectInstance> effects = new ArrayList<>((Collection) contents.getAllEffects());

			if (effects.isEmpty()) return InteractionResult.PASS;

			MobEffectInstance waterBreathing = null;
			for (MobEffectInstance effect : effects) {
				if (effect.getEffect() == MobEffects.WATER_BREATHING) {
					waterBreathing = effect;
					break;
				}
			}

			if (waterBreathing == null) return InteractionResult.PASS;

			BlockPos pos = hitResult.getBlockPos();
			BlockState state = world.getBlockState(pos);

			if (!state.is(BlockTags.CORAL_BLOCKS) || state.is(ModTags.Blocks.CRYSTALLIZED_CORAL)) {
				return InteractionResult.PASS;
			}

			world.setBlockAndUpdate(pos, getCrystallizedVariant(state));

			if (player.getAbilities().instabuild) {
				return InteractionResult.SUCCESS;
			}

			int newDuration = waterBreathing.getDuration() - 1200;

			if (newDuration <= 0) {
				player.setItemInHand(hand, new ItemStack(Items.GLASS_BOTTLE));
				return InteractionResult.SUCCESS;
			}

			MobEffectInstance newEffect = new MobEffectInstance(
					MobEffects.WATER_BREATHING, newDuration,
					waterBreathing.getAmplifier(), waterBreathing.isAmbient(),
					waterBreathing.isVisible(), waterBreathing.showIcon()
			);

			PotionContents newContents = new PotionContents(
					Optional.empty(), Optional.empty(), List.of(newEffect), Optional.empty()
			);

			stack.set(DataComponents.POTION_CONTENTS, newContents);
			stack.set(DataComponents.CUSTOM_NAME, Component.translatable("item.minecraft.potion.effect.water_breathing"));

			return InteractionResult.SUCCESS;
		});
	}

	public static BlockState getCrystallizedVariant(BlockState original) {
		Block block = original.getBlock();

		if (block == Blocks.TUBE_CORAL_BLOCK) return ModBlocks.CRYSTALLIZED_TUBE_CORAL.defaultBlockState();
		if (block == Blocks.BRAIN_CORAL_BLOCK) return ModBlocks.CRYSTALLIZED_BRAIN_CORAL.defaultBlockState();
		if (block == Blocks.BUBBLE_CORAL_BLOCK) return ModBlocks.CRYSTALLIZED_BUBBLE_CORAL.defaultBlockState();
		if (block == Blocks.FIRE_CORAL_BLOCK) return ModBlocks.CRYSTALLIZED_FIRE_CORAL.defaultBlockState();
		if (block == Blocks.HORN_CORAL_BLOCK) return ModBlocks.CRYSTALLIZED_HORN_CORAL.defaultBlockState();
		return ModBlocks.CRYSTALLIZED_TUBE_CORAL.defaultBlockState();
	}
}