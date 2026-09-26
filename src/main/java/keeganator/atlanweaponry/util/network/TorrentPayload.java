package keeganator.atlanweaponry.util.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record TorrentPayload() implements CustomPacketPayload {

    public static final Type<TorrentPayload> ID =
            new Type<>(Identifier.fromNamespaceAndPath("atlanweaponry", "torrent_activate"));

    public static final StreamCodec<FriendlyByteBuf, TorrentPayload> CODEC =
            StreamCodec.unit(new TorrentPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}