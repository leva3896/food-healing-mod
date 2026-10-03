package com.leva.foodhealing;

import com.leva.foodhealing.capability.ShokugiProvider;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = FoodHealingMod.MODID)
public final class LootEventHandler {
    private LootEventHandler() {
    }

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        if (!(event.getSource().getEntity() instanceof Player player)) {
            return;
        }
        // Absolute grave/PvP invariant: a Player is never a Food Healing drop-multiplier target.
        if (event.getEntity() instanceof Player) {
            return;
        }
        if (event.getEntity().getType().getCategory() == net.minecraft.world.entity.MobCategory.MONSTER) {
            return;
        }

        player.getCapability(ShokugiProvider.SHOKUGI_CAPA).ifPresent(data -> {
            if (!FoodHealingSkills.isEnabled(data, FoodHealingSkillIds.SLAUGHTER)) {
                return;
            }

            List<ItemEntity> extraDrops = new ArrayList<>();
            event.getDrops().forEach(drop -> multiplyDrop(drop, 3, extraDrops));
            event.getDrops().addAll(extraDrops);
        });
    }

    private static void multiplyDrop(ItemEntity drop, int multiplier, List<ItemEntity> extraDrops) {
        ItemStack source = drop.getItem();
        long total = (long) source.getCount() * multiplier;
        int firstCount = (int) Math.min(total, source.getMaxStackSize());
        ItemStack first = source.copy();
        first.setCount(firstCount);
        drop.setItem(first);

        long remaining = total - firstCount;
        while (remaining > 0L) {
            int count = (int) Math.min(remaining, source.getMaxStackSize());
            ItemStack extra = source.copy();
            extra.setCount(count);
            extraDrops.add(new ItemEntity(drop.level(), drop.getX(), drop.getY(), drop.getZ(), extra));
            remaining -= count;
        }
    }
}
