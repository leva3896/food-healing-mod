package com.example.examplemod.network;

import com.example.examplemod.capability.IShokugiData;
import com.example.examplemod.capability.ShokugiProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ShokugiSyncPacket {
    private final int level;
    private final int eatCount;

    public ShokugiSyncPacket(int level, int eatCount) {
        this.level = level;
        this.eatCount = eatCount;
    }

    public ShokugiSyncPacket(FriendlyByteBuf buf) {
        this.level = buf.readInt();
        this.eatCount = buf.readInt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.level);
        buf.writeInt(this.eatCount);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            // Client-side handling
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.getCapability(ShokugiProvider.SHOKUGI_CAPA).ifPresent(cap -> {
                    cap.setLevel(this.level);
                    cap.setEatCount(this.eatCount);
                });
            }
        });
        context.setPacketHandled(true);
    }
}
