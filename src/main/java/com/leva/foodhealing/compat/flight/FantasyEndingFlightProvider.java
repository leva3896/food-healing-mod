package com.leva.foodhealing.compat.flight;

import com.leva.foodhealing.FlightAuthority;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.CuriosApi;

/** Exact registered items and native ticking containers; cosmetic/inventory items do not grant. */
final class FantasyEndingFlightProvider {
    private static final ResourceLocation CHEST = new ResourceLocation("fantasy_ending", "fantasy_ending_chestplate");
    private static final ResourceLocation CURIO = new ResourceLocation("fantasy_ending", "the_domain_of_fade");
    private FantasyEndingFlightProvider() { }

    static FlightAuthority query(ServerPlayer player) {
        // Inventory.tick invokes onArmorTick for every armor slot; this item selects CHEST internally.
        boolean armor = false;
        for (ItemStack stack : player.getArmorSlots()) if (matches(stack, CHEST)) armor = true;
        final boolean armorActive = armor;
        return CuriosApi.getCuriosInventory(player).map(inventory -> {
            boolean curio = inventory.getCurios().values().stream().anyMatch(handler -> {
                var stacks = handler.getStacks();
                for (int i = 0; i < handler.getSlots(); i++) if (matches(stacks.getStackInSlot(i), CURIO)) return true;
                return false;
            });
            return new FlightAuthority(armorActive || curio, false, true);
        }).orElse(armor ? new FlightAuthority(true, false, true) : FlightAuthority.UNKNOWN);
    }

    private static boolean matches(ItemStack stack, ResourceLocation id) {
        return !stack.isEmpty() && stack.getItem() == ForgeRegistries.ITEMS.getValue(id);
    }
}
