package com.leva.foodhealing.network;

import com.leva.foodhealing.FoodHealingMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {
    private static final String PROTOCOL_VERSION = "7";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
        new ResourceLocation(FoodHealingMod.MODID, "main"),
        () -> PROTOCOL_VERSION,
        PROTOCOL_VERSION::equals,
        PROTOCOL_VERSION::equals
    );

    public static void register() {
        int id = 0;
        INSTANCE.messageBuilder(ShokugiSyncPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(ShokugiSyncPacket::encode).decoder(ShokugiSyncPacket::new)
                .consumerNetworkThread(ShokugiSyncPacket::handle).add();
        INSTANCE.messageBuilder(FoodDiversitySyncPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(FoodDiversitySyncPacket::encode).decoder(FoodDiversitySyncPacket::new)
                .consumerNetworkThread(FoodDiversitySyncPacket::handle).add();
        INSTANCE.messageBuilder(PurchaseSkillPacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(PurchaseSkillPacket::encode).decoder(PurchaseSkillPacket::new)
                .consumerMainThread(PurchaseSkillPacket::handle).add();
        INSTANCE.messageBuilder(PurchaseBaseStatPacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(PurchaseBaseStatPacket::encode).decoder(PurchaseBaseStatPacket::new)
                .consumerMainThread(PurchaseBaseStatPacket::handle).add();
        INSTANCE.messageBuilder(ToggleSkillPacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(ToggleSkillPacket::encode).decoder(ToggleSkillPacket::new)
                .consumerMainThread(ToggleSkillPacket::handle).add();
        INSTANCE.messageBuilder(SatisfactionDecisionPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(SatisfactionDecisionPacket::encode).decoder(SatisfactionDecisionPacket::new)
                .consumerMainThread(SatisfactionDecisionPacket::handle).add();
        INSTANCE.messageBuilder(TaczBoltStatePacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(TaczBoltStatePacket::encode).decoder(TaczBoltStatePacket::new)
                .consumerMainThread(TaczBoltStatePacket::handle).add();
        INSTANCE.messageBuilder(TimeStopLeasePacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(TimeStopLeasePacket::encode).decoder(TimeStopLeasePacket::new)
                .consumerNetworkThread(TimeStopLeasePacket::handle).add();
    }
}
