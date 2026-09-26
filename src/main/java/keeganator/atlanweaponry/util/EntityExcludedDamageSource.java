package keeganator.atlanweaponry.util;

import java.util.Arrays;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class EntityExcludedDamageSource extends DamageSource {

    protected final List<EntityType<?>> entities;
    private final ItemStack weaponStack;

    public EntityExcludedDamageSource(Holder<DamageType> type, EntityType<?>... entities) {
        super(type);
        this.entities = Arrays.stream(entities).toList();
        this.weaponStack = this.getWeaponItem();
    }

    @Override
    public Component getLocalizedDeathMessage(LivingEntity living) {
        LivingEntity attacker = living.getLastHurtByMob();

        String baseKey = "death.attack." + this.type().msgId();
        String playerKey = baseKey + ".player";
        String itemKey = baseKey + ".item";

        if (attacker != null) {
            for (EntityType<?> entity : entities) {
                if (attacker.getType() == entity) {
                    return Component.translatable(baseKey, living.getDisplayName());
                }
            }

            ItemStack weapon = this.weaponStack;

            if (!weapon.isEmpty()) {
                return Component.translatable(itemKey, living.getDisplayName(), attacker.getDisplayName(), weapon.getCustomName());
            }

            return Component.translatable(playerKey, living.getDisplayName(), attacker.getDisplayName());
        }

        return Component.translatable(baseKey, living.getDisplayName());
    }
}