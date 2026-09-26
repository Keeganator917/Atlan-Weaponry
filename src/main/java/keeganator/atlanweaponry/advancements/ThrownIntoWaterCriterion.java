package keeganator.atlanweaponry.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Optional;

public class ThrownIntoWaterCriterion extends SimpleCriterionTrigger<ThrownIntoWaterCriterion.Conditions> {

    @Override
    public Codec<Conditions> codec() {
        return Conditions.CODEC;
    }

    public void trigger(ServerPlayer player, ItemStack stack) {
        this.trigger(player, conditions -> conditions.matches(stack));
    }

    public record Conditions(Optional<Holder<LootItemCondition>> player, Optional<ItemPredicate> item) implements SimpleInstance {

        public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        LootItemCondition.CODEC.optionalFieldOf("player").forGetter(Conditions::player),
                        ItemPredicate.CODEC.optionalFieldOf("item").forGetter(Conditions::item)
                ).apply(instance, Conditions::new)
        );

        public boolean matches(ItemStack stack) {
            return this.item.isEmpty() || this.item.get().test(stack);
        }
    }
}