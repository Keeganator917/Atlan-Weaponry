package keeganator.atlanweaponry.util;

import keeganator.atlanweaponry.item.custom.HellforkItem;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;

public class ThunderstormBuffs {

    private static final Identifier BONUS_ID =
            Identifier.fromNamespaceAndPath("atlanweaponry", "storm_trident_reach");

    public static void tick(ServerLevel world) {
        if (!world.isThundering()) return;

        for (ServerPlayer player : world.players()) {

            ItemStack stack = player.getMainHandItem();

            var instance = player.getAttribute(Attributes.ENTITY_INTERACTION_RANGE);
            if (instance == null) return;

            instance.removeModifier(BONUS_ID);

            if (stack.getItem() instanceof TridentItem || stack.getItem() instanceof HellforkItem) {
                instance.addTransientModifier(new AttributeModifier(BONUS_ID, 2.5, AttributeModifier.Operation.ADD_VALUE)
                );
            }
        }
    }

}
