package keeganator.atlanweaponry.datacomponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public record PoseidonsBlessingComponent(List<MobEffectInstance> effects) {

    public static final Codec<PoseidonsBlessingComponent> CODEC =
            RecordCodecBuilder.create(instance ->
                    instance.group(
                            MobEffectInstance.CODEC.listOf()
                                    .optionalFieldOf("effects", List.of())
                                    .forGetter(PoseidonsBlessingComponent::effects)
                    ).apply(instance, PoseidonsBlessingComponent::new)
            );

    public static final StreamCodec<RegistryFriendlyByteBuf, PoseidonsBlessingComponent> PACKET_CODEC =
            StreamCodec.composite(
                    MobEffectInstance.STREAM_CODEC.apply(ByteBufCodecs.list()),
                    PoseidonsBlessingComponent::effects,
                    PoseidonsBlessingComponent::new
            );

    public void applyEffects(ItemStack stack, LivingEntity entity) {
        for (MobEffectInstance effect : effects) {
            entity.addEffect(new MobEffectInstance(effect));
        }
    }

}
