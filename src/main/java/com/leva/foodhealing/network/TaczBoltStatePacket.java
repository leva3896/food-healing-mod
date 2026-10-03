package com.leva.foodhealing.network;

import com.leva.foodhealing.compat.TaczAmmoCompatibility;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

/** Mechanical-cycle acknowledgement only; never grants ammo or changes an inventory stack. */
public record TaczBoltStatePacket(int slot, ResourceLocation gunId, boolean pending) {
    public TaczBoltStatePacket(FriendlyByteBuf buffer) {
        this(buffer.readVarInt(), buffer.readResourceLocation(), buffer.readBoolean());
    }
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeVarInt(slot);
        buffer.writeResourceLocation(gunId);
        buffer.writeBoolean(pending);
    }
    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        var context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isClient() && slot >= 0 && slot < 9
                && TaczAmmoCompatibility.supported()) {
            com.leva.foodhealing.compat.tacz.client.ClientTaczBoltState.accept(this);
        }
        context.setPacketHandled(true);
    }
}
