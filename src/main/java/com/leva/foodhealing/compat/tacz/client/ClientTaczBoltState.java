package com.leva.foodhealing.compat.tacz.client;

import com.leva.foodhealing.network.TaczBoltStatePacket;
import com.tacz.guns.api.item.IGun;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public final class ClientTaczBoltState {
    private static LivingEntity owner;
    private static TaczBoltStatePacket state;
    private ClientTaczBoltState() { }

    public static void accept(TaczBoltStatePacket packet) {
        owner = Minecraft.getInstance().player;
        state = packet;
    }

    public static boolean pending(LivingEntity player, ItemStack stack) {
        if (player != owner || player != Minecraft.getInstance().player || state == null || !state.pending()
                || Minecraft.getInstance().player.getInventory().selected != state.slot()
                || player.getMainHandItem() != stack) return false;
        IGun gun = IGun.getIGunOrNull(stack);
        return gun != null && gun.getGunId(stack).equals(state.gunId());
    }
}
