package com.leva.foodhealing.network;

import com.leva.foodhealing.FoodHealingSkillIds;
import com.leva.foodhealing.FoodHealingSkills;
import com.leva.foodhealing.RootController;
import com.leva.foodhealing.capability.CapabilityEvents;
import com.leva.foodhealing.capability.ShokugiProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record ToggleSkillPacket(String skillId, boolean disabled) {
    public ToggleSkillPacket(FriendlyByteBuf buffer) {
        this(buffer.readUtf(128), buffer.readBoolean());
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeUtf(skillId, 128);
        buffer.writeBoolean(disabled);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        ServerPlayer player = context.getSender();
        if (player == null) {
            context.setPacketHandled(true);
            return;
        }
        player.getCapability(ShokugiProvider.SHOKUGI_CAPA).ifPresent(data -> {
            String normalized = FoodHealingSkillIds.normalize(skillId);
            if (FoodHealingSkills.getEffectiveLevel(data, normalized) > 0) {
                boolean wasEnabled = FoodHealingSkills.isEnabled(data, normalized);
                RootController.setSkillDisabled(data, normalized, disabled, player.level().getGameTime());
                if (!wasEnabled && !disabled && FoodHealingSkillIds.TACZ_AMMO_CONSERVATION.equals(normalized)) {
                    com.leva.foodhealing.compat.TaczAmmoCompatibility.onEnabled(player);
                }
                CapabilityEvents.syncToClient(player);
            }
        });
        context.setPacketHandled(true);
    }
}
