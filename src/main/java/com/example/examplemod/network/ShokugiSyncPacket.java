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
    private final java.util.Set<String> disabledSkills;

    public ShokugiSyncPacket(int level, int eatCount, java.util.Set<String> disabledSkills) {
        this.level = level;
        this.eatCount = eatCount;
        this.disabledSkills = new java.util.HashSet<>(disabledSkills);
    }

    public ShokugiSyncPacket(FriendlyByteBuf buf) {
        this.level = buf.readInt();
        this.eatCount = buf.readInt();
        int size = buf.readInt();
        this.disabledSkills = new java.util.HashSet<>();
        for (int i = 0; i < size; i++) {
            this.disabledSkills.add(buf.readUtf());
        }
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.level);
        buf.writeInt(this.eatCount);
        buf.writeInt(this.disabledSkills.size());
        for (String skill : this.disabledSkills) {
            buf.writeUtf(skill);
        }
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            // Client-side handling
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.getCapability(ShokugiProvider.SHOKUGI_CAPA).ifPresent(cap -> {
                    cap.setLevel(this.level);
                    cap.setEatCount(this.eatCount);
                    cap.setDisabledSkills(this.disabledSkills);
                });
            }
        });
        context.setPacketHandled(true);
    }
}
