package com.leva.foodhealing.compat.flight;

import com.leva.foodhealing.FlightAuthority;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraftforge.registries.ForgeRegistries;

/** Called only after the exact archive gate. Normal Infinity Chestplate route only. */
public final class AvaritiaFlightProvider {
    private static final ResourceLocation CHEST = new ResourceLocation("avaritia", "infinity_chestplate");
    private AvaritiaFlightProvider() { }

    public static FlightAuthority query(ServerPlayer player) {
        var stack = player.getItemBySlot(EquipmentSlot.CHEST);
        boolean positive = !stack.isEmpty() && CHEST.equals(ForgeRegistries.ITEMS.getKey(stack.getItem()));
        return new FlightAuthority(positive, false, true);
    }
}
