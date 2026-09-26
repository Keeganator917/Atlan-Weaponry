package keeganator.atlanweaponry.datacomponents;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.function.UnaryOperator;

public class ModDataComponents {

    public static final DataComponentType<PoseidonsBlessingComponent> POSEIDONS_BLESSING_COMPONENT = register(
            "poseidons_blessing_component", builder -> builder.persistent(PoseidonsBlessingComponent.CODEC).networkSynchronized(PoseidonsBlessingComponent.PACKET_CODEC).cacheEncoding()
    );

    private static <T> DataComponentType<T> register(String id, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, id, builderOperator.apply(DataComponentType.<T>builder()).build());
    }

    public static void initialize() {
    }

}
