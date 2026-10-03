package com.leva.foodhealing.compat.tacz;

import com.leva.foodhealing.FoodHealingSkillIds;
import com.leva.foodhealing.FoodHealingSkills;
import com.leva.foodhealing.capability.ShokugiProvider;
import com.leva.foodhealing.compat.TaczAmmoCompatibility;
import com.leva.foodhealing.compat.AmmoConservationChance;
import com.leva.foodhealing.network.PacketHandler;
import com.leva.foodhealing.network.TaczBoltStatePacket;
import com.tacz.guns.api.TimelessAPI;
import com.tacz.guns.api.event.common.GunDrawEvent;
import com.tacz.guns.api.item.IGun;
import com.tacz.guns.item.ModernKineticGunItem;
import com.tacz.guns.item.ModernKineticGunScriptAPI;
import com.tacz.guns.resource.pojo.data.gun.Bolt;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.PacketDistributor;
import java.util.Map;
import java.util.WeakHashMap;

/** TaCZ 1.1.7-hotfix2 only. Gun state remains owned by TaCZ. */
public final class TaczAmmoAdapter {
    private static final Map<LivingEntity, ItemStack> ACTIVE = new WeakHashMap<>();
    private static final Map<ItemStack, Boolean> RETAINED_CHAMBER_CYCLE = new WeakHashMap<>();
    private TaczAmmoAdapter() { }

    public static void register() {
        // Resolve all server targets before publishing readiness; required injection errors remain fatal.
        for (String target : new String[]{"com.tacz.guns.item.ModernKineticGunScriptAPI",
                "com.tacz.guns.entity.shooter.LivingEntityBolt", "com.tacz.guns.entity.shooter.LivingEntityShoot"}) {
            try { Class.forName(target); }
            catch (ClassNotFoundException error) { throw new IllegalStateException("Supported TaCZ target missing", error); }
        }
        MinecraftForge.EVENT_BUS.register(TaczAmmoAdapter.class);
    }

    public static int level(LivingEntity entity, ItemStack stack) {
        if (!(entity instanceof ServerPlayer) || entity.getMainHandItem() != stack || !TaczAmmoCompatibility.supported()
                || !(stack.getItem() instanceof ModernKineticGunItem)) return 0;
        IGun gun = IGun.getIGunOrNull(stack);
        if (TimelessAPI.getCommonGunIndex(gun.getGunId(stack)).isEmpty()) return 0;
        return entity.getCapability(ShokugiProvider.SHOKUGI_CAPA)
                .map(data -> Math.min(10, FoodHealingSkills.getEnabledLevel(data,
                        FoodHealingSkillIds.TACZ_AMMO_CONSERVATION))).orElse(0);
    }

    public static void normalizeActive(ServerPlayer player) {
        ACTIVE.put(player, player.getMainHandItem());
        normalize(player, player.getMainHandItem());
    }

    @SubscribeEvent
    public static void onDraw(GunDrawEvent event) {
        if (event.getLogicalSide().isServer() && event.getEntity() instanceof ServerPlayer player
                && event.getCurrentGunItem() == player.getMainHandItem()) {
            ItemStack current = event.getCurrentGunItem();
            if (ACTIVE.put(player, current) != current) {
                normalize(player, current);
                sendBoltState(player, current, needsBolt(player, current));
            }
        }
    }

    /** Called at the single consumption call site, before pellets/damage are produced. */
    public static boolean consumeShot(ModernKineticGunScriptAPI api, ItemStack stack) {
        LivingEntity shooter = api.getShooter();
        int level = level(shooter, stack);
        if (level <= 0) return api.reduceAmmoOnce();
        IGun gun = IGun.getIGunOrNull(stack);
        Bolt bolt = api.getBolt();
        boolean chamber = gun.hasBulletInBarrel(stack) && bolt != Bolt.OPEN_BOLT;
        boolean feed = gun.useInventoryAmmo(stack)
                ? gun.hasInventoryAmmo(shooter, stack, api.isReloadingNeedConsumeAmmo())
                : gun.getCurrentAmmoCount(stack) > 0;
        boolean available = bolt == Bolt.MANUAL_ACTION ? chamber
                : bolt == Bolt.CLOSED_BOLT ? chamber || feed : bolt == Bolt.OPEN_BOLT && feed;
        if (!available || needsBolt(shooter, stack)) return false;
        boolean preserved = AmmoConservationChance.preserves(level, shooter.getRandom().nextFloat());
        boolean success;
        if (preserved) {
            // The live round never leaves TaCZ's chamber. A separate mechanical cycle
            // still runs through TaCZ's usual bolt timing, without loading a second round.
            if (bolt == Bolt.MANUAL_ACTION) {
                RETAINED_CHAMBER_CYCLE.put(stack, true);
                sendBoltState((ServerPlayer) shooter, stack, true);
            }
            success = true;
        } else {
            success = api.reduceAmmoOnce();
        }
        syncGun((ServerPlayer) shooter, stack);
        return success;
    }

    public static boolean needsBolt(LivingEntity player, ItemStack stack) {
        return player instanceof ServerPlayer && player.getMainHandItem() == stack
                && RETAINED_CHAMBER_CYCLE.containsKey(stack);
    }

    public static void finishBolt(LivingEntity player, ItemStack stack) {
        if (player instanceof ServerPlayer serverPlayer && RETAINED_CHAMBER_CYCLE.remove(stack) != null) {
            sendBoltState(serverPlayer, stack, false);
        }
    }

    private static void sendBoltState(ServerPlayer player, ItemStack stack, boolean pending) {
        IGun gun = IGun.getIGunOrNull(stack);
        if (gun == null || player.connection == null) return;
        PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player),
                new TaczBoltStatePacket(player.getInventory().selected, gun.getGunId(stack), pending));
    }

    private static void normalize(ServerPlayer player, ItemStack stack) {
        if (level(player, stack) <= 0) return;
        IGun gun = IGun.getIGunOrNull(stack);
        var index = TimelessAPI.getCommonGunIndex(gun.getGunId(stack)).orElseThrow();
        if (!index.getGunData().hasHeatData()) return;
        if (gun.getHeatAmount(stack) == 0F && !gun.isOverheatLocked(stack)) return;
        gun.setHeatAmount(stack, 0F);
        gun.setOverheatLocked(stack, false);
        syncGun(player, stack);
    }

    public static void syncGun(ServerPlayer player, ItemStack stack) {
        if (player.getMainHandItem() != stack || player.connection == null) return;
        // Vanilla's inventory-slot synchronization carries TaCZ's canonical full stack.
        player.connection.send(new ClientboundContainerSetSlotPacket(-2, 0,
                player.getInventory().selected, stack.copy()));
        player.inventoryMenu.broadcastChanges();
    }
}
