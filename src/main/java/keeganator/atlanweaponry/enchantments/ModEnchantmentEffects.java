package keeganator.atlanweaponry.enchantments;

import com.mojang.serialization.MapCodec;
import keeganator.atlanweaponry.AtlanWeaponry;
import keeganator.atlanweaponry.enchantments.custom.FieryLanternsEnchantment;
import keeganator.atlanweaponry.enchantments.custom.ShatteringEnchantment;
import keeganator.atlanweaponry.enchantments.custom.TorrentEnchantment;
import keeganator.atlanweaponry.enchantments.custom.WhirlpoolEnchantment;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;

public class ModEnchantmentEffects {

    public static final MapCodec<? extends EnchantmentEntityEffect> SHATTERING =
            registerEntityEffect("shattering", ShatteringEnchantment.CODEC);
    public static final MapCodec<? extends EnchantmentEntityEffect> FIERY_LANTERNS =
            registerEntityEffect("fiery_lanterns", FieryLanternsEnchantment.CODEC);
    public static final MapCodec<? extends EnchantmentEntityEffect> WHIRLPOOL =
            registerEntityEffect("whirlpool", WhirlpoolEnchantment.CODEC);
    public static final MapCodec<? extends EnchantmentEntityEffect> TORRENT =
            registerEntityEffect("torrent", TorrentEnchantment.CODEC);

    private static MapCodec<? extends EnchantmentEntityEffect> registerEntityEffect(String name,
                                                                                    MapCodec<? extends EnchantmentEntityEffect> codec) {
        return Registry.register(BuiltInRegistries.ENCHANTMENT_ENTITY_EFFECT_TYPE, Identifier.fromNamespaceAndPath(AtlanWeaponry.MOD_ID, name), codec);
    }

    public static void registerEnchantmentEffects() {
        AtlanWeaponry.LOGGER.info("Registering Mod Enchantment Effects for " + AtlanWeaponry.MOD_ID);
    }
}
