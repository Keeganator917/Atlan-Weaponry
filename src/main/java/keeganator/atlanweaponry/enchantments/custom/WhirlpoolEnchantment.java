package keeganator.atlanweaponry.enchantments.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;

public class WhirlpoolEnchantment implements EnchantmentEntityEffect {
    public static final MapCodec<WhirlpoolEnchantment> CODEC = MapCodec.unit(WhirlpoolEnchantment::new);

    @Override
    public void apply(ServerLevel world, int level, EnchantedItemInUse context, Entity user, Vec3 pos) {
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}
