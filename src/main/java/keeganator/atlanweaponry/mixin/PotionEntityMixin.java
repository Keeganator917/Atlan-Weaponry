package keeganator.atlanweaponry.mixin;

import keeganator.atlanweaponry.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.projectile.throwableitemprojectile.AbstractThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static keeganator.atlanweaponry.AtlanWeaponry.getCrystallizedVariant;

@Mixin(AbstractThrownPotion.class)
public abstract class PotionEntityMixin {

    @Inject(method = "onHit", at = @At("HEAD"))
    private void onHit(HitResult hitResult, CallbackInfo ci) {
        AbstractThrownPotion self = (AbstractThrownPotion)(Object)this;

        if (!(self.level() instanceof ServerLevel world)) return;

        ItemStack stack = self.getItem();

        if (!stack.is(Items.SPLASH_POTION)) return;

        var contents = stack.get(DataComponents.POTION_CONTENTS);
        if (contents == null || contents.potion().isEmpty()) return;

        if (contents.potion().get() != Potions.WATER_BREATHING) return;

        BlockPos center = BlockPos.containing(self.position());

        BlockPos.withinManhattan(center, 2, 2, 2).forEach(pos -> {
            BlockState state = world.getBlockState(pos);

            if (state.is(BlockTags.CORAL_BLOCKS) && !state.is(ModTags.Blocks.CRYSTALLIZED_CORAL)) {
                world.setBlockAndUpdate(pos, getCrystallizedVariant(state));
            }
        });
    }
}