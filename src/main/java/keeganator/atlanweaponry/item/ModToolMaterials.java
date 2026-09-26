package keeganator.atlanweaponry.item;

import keeganator.atlanweaponry.util.ModTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ToolMaterial;

public class ModToolMaterials {

    public static final ToolMaterial SHELL_HAMMER = new ToolMaterial(
            BlockTags.INCORRECT_FOR_IRON_TOOL,
            800,
            8.0f,
            3.0f,
            6,
            ModTags.Items.NAUTILUS_SHELL
    );

}
