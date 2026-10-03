package com.leva.foodhealing.compat.l2hostility;

import com.leva.foodhealing.TruthMasteryController;
import com.leva.foodhealing.capability.ShokugiProvider;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import java.util.Map;
import java.util.WeakHashMap;

/** No external class in signatures or fields; the adapter is linked only after version checks. */
@Mod.EventBusSubscriber(modid = "foodhealing")
public final class L2HostilityCompatibility {
    private static final Map<ServerPlayer, Scan> SCANS = new WeakHashMap<>();
    private record Scan(long tick, double x, double y, double z, Object level) { }
    private L2HostilityCompatibility() { }

    public static boolean available() { return Loaded.AVAILABLE; }
    private static final class Loaded {
        private static String version(String id) {
            return ModList.get().getModContainerById(id).map(m -> m.getModInfo().getVersion().toString()).orElse("");
        }
        private static final boolean AVAILABLE = L2HostilityVersions.supports(version("l2hostility"), version("l2library"),
                version("l2complements"), version("l2damagetracker"));
    }

    @SubscribeEvent
    public static void join(EntityJoinLevelEvent event) {
        if (!event.getLevel().isClientSide() && event.getEntity() instanceof Mob mob && available()) {
            L2HostilityAdapter.consider(mob, true);
        }
    }

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(event.player instanceof ServerPlayer player) || !available()) return;
        if (!player.isAlive() || !player.getCapability(ShokugiProvider.SHOKUGI_CAPA)
                .map(TruthMasteryController::isEnabled).orElse(false)) {
            SCANS.remove(player);
            return;
        }
        long now = player.serverLevel().getGameTime();
        Scan prior = SCANS.get(player);
        boolean moved = prior == null || prior.level != player.level()
                || prior.x != player.getX() || prior.y != player.getY() || prior.z != player.getZ();
        // At most one loaded-entity query per five ticks while moving, twenty while stationary.
        if (prior != null && prior.level == player.level() && now >= prior.tick
                && now - prior.tick < (moved ? 5 : 20)) return;
        SCANS.put(player, new Scan(now, player.getX(), player.getY(), player.getZ(), player.level()));
        double r = TruthMasteryController.RANGE;
        AABB box = new AABB(player.getX()-r, player.getY()-r, player.getZ()-r,
                player.getX()+r, player.getY()+r, player.getZ()+r).inflate(0.001);
        for (Mob mob : player.serverLevel().getEntitiesOfClass(Mob.class, box,
                m -> m.isAlive() && TruthMasteryController.contains(player.getX(), player.getY(), player.getZ(),
                        m.getX(), m.getY(), m.getZ()))) {
            L2HostilityAdapter.consider(mob, true);
        }
    }
}
