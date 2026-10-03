package com.leva.foodhealing.client;
import com.leva.foodhealing.compat.endinglibrary.TimeStopGate;
import com.leva.foodhealing.network.TimeStopLeasePacket;
import net.minecraft.network.Connection;
import net.minecraftforge.common.MinecraftForge;
public final class TimeStopClientCompatibility {
    private TimeStopClientCompatibility() { }
    public static void initialize() {
        if(TimeStopGate.supported())MinecraftForge.EVENT_BUS.register(TimeStopClientLease.class);
    }
    public static void receive(TimeStopLeasePacket packet,Connection origin) {
        if(TimeStopGate.supported())TimeStopClientLease.receive(packet,origin);
    }
}
