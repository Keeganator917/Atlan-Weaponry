package keeganator.atlanweaponry.sound;

import keeganator.atlanweaponry.AtlanWeaponry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

public class ModSounds {

    private ModSounds() {

    }

    public static final SoundEvent SHELL_STUN = registerSound("shell_stun");


    private static SoundEvent registerSound(String id) {
        Identifier identifier = Identifier.fromNamespaceAndPath(AtlanWeaponry.MOD_ID, id);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, identifier, SoundEvent.createVariableRangeEvent(identifier));
    }


    public static void initialize() {

    }

}
