package keeganator.atlanweaponry.util.network;

import keeganator.atlanweaponry.AtlanWeaponry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record ShowNeptunesOfferingPayload() implements CustomPacketPayload {

    public static final Type<ShowNeptunesOfferingPayload> ID =
            new Type<>(Identifier.fromNamespaceAndPath(AtlanWeaponry.MOD_ID, "show_neptunes_offering"));

    public static final StreamCodec<FriendlyByteBuf, ShowNeptunesOfferingPayload> CODEC =
            StreamCodec.unit(new ShowNeptunesOfferingPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}