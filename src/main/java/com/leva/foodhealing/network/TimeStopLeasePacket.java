package com.leva.foodhealing.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import java.util.UUID;
import java.util.function.Supplier;

/** Owning-client cache only. No client packet can issue a server grant. */
public record TimeStopLeasePacket(UUID player, ResourceLocation dimension, long session, long epoch,
                                  long sequence, long revision, boolean allowed, int expiryMillis) {
    public TimeStopLeasePacket(FriendlyByteBuf b) {
        this(b.readUUID(),b.readResourceLocation(),b.readLong(),b.readLong(),b.readLong(),b.readLong(),b.readBoolean(),b.readVarInt());
    }
    public void encode(FriendlyByteBuf b) {
        b.writeUUID(player);b.writeResourceLocation(dimension);b.writeLong(session);b.writeLong(epoch);
        b.writeLong(sequence);b.writeLong(revision);b.writeBoolean(allowed);b.writeVarInt(expiryMillis);
    }
    public void handle(Supplier<NetworkEvent.Context> supplier) {
        var context=supplier.get();var origin=context.getNetworkManager();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> () -> com.leva.foodhealing.client.TimeStopClientCompatibility.receive(this,origin)));
        context.setPacketHandled(true);
    }
}
