package com.leva.foodhealing;

import com.leva.foodhealing.capability.*;
import com.leva.foodhealing.compat.tacz.TaczAmmoAdapter;
import com.leva.foodhealing.network.*;
import com.mojang.logging.LogUtils;
import com.tacz.guns.api.TimelessAPI;
import com.tacz.guns.api.entity.*;
import com.tacz.guns.api.event.common.GunFireEvent;
import com.tacz.guns.api.item.IGun;
import com.tacz.guns.api.item.builder.*;
import com.tacz.guns.api.item.gun.FireMode;
import com.tacz.guns.entity.EntityKineticBullet;
import com.tacz.guns.entity.shooter.*;
import com.tacz.guns.item.*;
import com.tacz.guns.resource.modifier.AttachmentCacheProperty;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.util.ReferenceCountUtil;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.*;

/** Real approved TaCZ APIs/packets; only synthetic players and disposable worlds. */
public final class TaczAmmoConservationVerification {
    private static final String SKILL = FoodHealingSkillIds.TACZ_AMMO_CONSERVATION;
    private TaczAmmoConservationVerification() { }

    public static void verify(ServerPlayer player, Connection connection, EmbeddedChannel channel) {
        IShokugiData data = player.getCapability(ShokugiProvider.SHOKUGI_CAPA).orElseThrow(IllegalStateException::new);
        data.copyFrom(new ShokugiData());
        Observer observer = new Observer(player);
        MinecraftForge.EVENT_BUS.register(observer);
        try {
            heatAndPurchase(player, data, connection, channel, observer);
            ammunition(player, data, observer);
            burst(player, data, observer);
            manualBolt(player, data, observer);
            reload(player, data);
            replay(player, data, observer);
            LogUtils.getLogger().info("FOODHEALING_TACZ_AMMO_PASS heat=A-M purchase=10x5SP chamber=true magazine=true inventory=true reload=true replay=true");
        } finally { MinecraftForge.EVENT_BUS.unregister(observer); }
    }

    private static void heatAndPurchase(ServerPlayer player, IShokugiData data, Connection connection,
                                        EmbeddedChannel channel, Observer observer) {
        ItemStack hot = gun("minigun", 0, false);
        heat(hot, 360, true);
        var script = equip(player, hot);
        require(heat(hot) == 360 && locked(hot), "A: unowned draw changed heat");
        data.setUnspentSkillPoints(500);
        drain(channel);
        dispatch(connection, new PurchaseSkillPacket(SKILL, 0));
        require(data.getSkillLevel(SKILL) == 1 && data.getUnspentSkillPoints() == 450
                && data.getSpentSkillPoints() == 50, "purchase/default ON 50SP transaction failed");
        require(heat(hot) == 0 && !locked(hot), "D: purchase did not normalize active gun");
        require(synced(channel, hot), "M: authoritative normalized stack not synchronized");
        dispatch(connection, new PurchaseSkillPacket(SKILL, 0));
        require(data.getUnspentSkillPoints() == 450 && data.getSpentSkillPoints() == 50, "purchase replay spent SP");
        dispatch(connection, new ToggleSkillPacket(SKILL, true));
        heat(hot, 360, true);
        equip(player, hot);
        require(heat(hot) == 360 && locked(hot), "B: acquired OFF changed heat");
        drain(channel);
        dispatch(connection, new ToggleSkillPacket(SKILL, false));
        require(heat(hot) == 0 && !locked(hot) && synced(channel, hot), "C/M: OFF-ON normalization/sync");
        // Duplicate ON and same-gun draw notifications are not activation transitions.
        heat(hot, 17, false);
        dispatch(connection, new ToggleSkillPacket(SKILL, false));
        IGunOperator.fromLivingEntity(player).draw(player::getMainHandItem);
        require(heat(hot) == 17, "normalization repeated without a transition");
        ItemStack other = gun("minigun", 0, false);
        heat(other, 123, true);
        player.getInventory().setItem(8, other);
        var untouched = other.save(new net.minecraft.nbt.CompoundTag());
        for (int i = 0; i < 4; i++) {
            dispatch(connection, new ToggleSkillPacket(SKILL, true));
            dispatch(connection, new ToggleSkillPacket(SKILL, false));
        }
        require(untouched.equals(other.save(new net.minecraft.nbt.CompoundTag())), "F/K: inactive gun mutated");
        equip(player, other);
        require(heat(other) == 0 && !locked(other), "E: hot replacement gun not normalized");
        require(heat(hot) == 0, "L: prior gun mutated on switch");
        player.getInventory().setItem(8, ItemStack.EMPTY);
        for (int level = 1; level < 10; level++) dispatch(connection, new PurchaseSkillPacket(SKILL, level));
        require(data.getSkillLevel(SKILL) == 10 && data.getUnspentSkillPoints() == 0
                && data.getSpentSkillPoints() == 500, "10 levels must cost exactly 500SP");
        dispatch(connection, new PurchaseSkillPacket(SKILL, 10));
        require(data.getSpentSkillPoints() == 500, "max level purchase changed SP");
        stockReserve(player, "308", 256);
        script = equip(player, hot);
        int before = observer.bullets;
        for (int i = 0; i < 200; i++) script.shootOnce(true);
        require(observer.bullets == before + 200 && heat(hot) == 0 && !locked(hot), "G: ON heat/lock or shot loss");
        require(reserveTotal(player) == 256, "Lv10 inventory ammo changed");
        dispatch(connection, new ToggleSkillPacket(SKILL, true));
        require(heat(hot) == 0 && !locked(hot), "H: OFF restored old heat");
        for (int i = 0; i < 180; i++) script.shootOnce(true);
        require(heat(hot) == 360 && locked(hot), "I: OFF normal heat threshold did not lock");
        require(reserveTotal(player) == 76, "K: OFF consumed other than one per shot");
        var holder = IGunOperator.fromLivingEntity(player).getDataHolder();
        holder.heatTimestamp = System.currentTimeMillis() - 4000;
        for (int i = 0; i < 1000 && locked(hot); i++) ((ModernKineticGunItem) hot.getItem()).tickHeat(holder, hot, player);
        require(!locked(hot), "J: native cooldown did not release lock");
        float cooled = heat(hot);
        script.shootOnce(true);
        require(heat(hot) > cooled && reserveTotal(player) == 75, "H/J: native resumed shot failed");
        clearReserve(player);
        LogUtils.getLogger().info("FOODHEALING_TACZ_HEAT_PASS A-M=true onShots=200 offThresholdShots=180 sync=canonicalSlotPacket");
    }

    private static void ammunition(ServerPlayer player, IShokugiData data, Observer observer) {
        for (boolean enabled : new boolean[]{false, true}) {
            data.setSkillDisabled(SKILL, !enabled);
            for (String id : new String[]{"glock_17", "aa12"}) {
                ItemStack stack = gun(id, 8, true);
                var script = equip(player, stack);
                int before = observer.bullets;
                long rolls = ((TaczAmmoTestPlayer) player).rolls();
                script.shootOnce(true);
                require(((TaczAmmoTestPlayer) player).rolls() - rolls == (enabled ? 1 : 0), "physical shot RNG count mismatch");
                require(IGun.getIGunOrNull(stack).getCurrentAmmoCount(stack) == (enabled ? 8 : 7), id + " magazine mismatch");
                require(observer.bullets - before == (id.equals("aa12") ? 10 : 1), "pellet count mismatch");
                observer.cancel = true;
                rolls = ((TaczAmmoTestPlayer) player).rolls();
                int magazine = IGun.getIGunOrNull(stack).getCurrentAmmoCount(stack);
                script.shootOnce(true);
                require(IGun.getIGunOrNull(stack).getCurrentAmmoCount(stack) == magazine, "canceled shot consumed ammo");
                require(((TaczAmmoTestPlayer) player).rolls() == rolls, "canceled shot rolled conservation");
                observer.cancel = false;
                script.shootOnce(false);
                require(((TaczAmmoTestPlayer) player).rolls() == rolls, "TaCZ non-consuming shot rolled conservation");
                var item = IGun.getIGunOrNull(stack);
                item.setCurrentAmmoCount(stack, 0);
                item.setBulletInBarrel(stack, false);
                before = observer.bullets;
                script.shootOnce(true);
                require(observer.bullets == before, "empty gun created bullets");
                require(((TaczAmmoTestPlayer) player).rolls() == rolls, "empty gun rolled conservation");
            }
            ItemStack last = gun("glock_17", 0, true);
            var script = equip(player, last);
            int before = observer.bullets;
            script.shootOnce(true);
            require(observer.bullets == before + 1 && IGun.getIGunOrNull(last).hasBulletInBarrel(last) == enabled,
                    "closed bolt final chamber shot mismatch");
        }
        data.setSkillLevel(SKILL, 1);
        data.setSkillDisabled(SKILL, false);
        ItemStack glock = gun("glock_17", 1000, true);
        var script = equip(player, glock);
        for (int i = 0; i < 300; i++) script.shootOnce(true);
        int remaining = IGun.getIGunOrNull(glock).getCurrentAmmoCount(glock);
        require(remaining > 700 && remaining < 1000, "Lv1 never preserved or never consumed");
        data.setSkillLevel(SKILL, 10);
        LogUtils.getLogger().info("FOODHEALING_TACZ_AMMO_BOLTS_PASS closed=true open=true pellets=10 canceled=true empty=true partialRemaining={}", remaining);
    }

    private static void burst(ServerPlayer player, IShokugiData data, Observer observer) {
        data.setSkillLevel(SKILL, 10);
        for (boolean enabled : new boolean[]{false, true}) {
            data.setSkillDisabled(SKILL, !enabled);
            ItemStack stack = gun("minigun", 0, false);
            IGun.getIGunOrNull(stack).setFireMode(stack, FireMode.BURST);
            player.getInventory().setItem(9, ammo("308", 20));
            var script = equip(player, stack);
            int before = observer.bullets;
            long rolls = ((TaczAmmoTestPlayer) player).rolls();
            script.shootOnce(true);
            long deadline = System.nanoTime() + 2_000_000_000L;
            while (observer.bullets < before + 6 && System.nanoTime() < deadline) {
                try { Thread.sleep(10); } catch (InterruptedException e) { Thread.currentThread().interrupt(); throw new AssertionError(e); }
                com.tacz.guns.util.CycleTaskHelper.tick();
            }
            require(observer.bullets == before + 6, "native six-shot burst incomplete");
            require(((TaczAmmoTestPlayer) player).rolls() - rolls == (enabled ? 6 : 0), "burst RNG must count physical shots, not triggers");
            require(player.getInventory().getItem(9).getCount() == (enabled ? 20 : 14), "burst consumed wrong ammunition count");
            player.getInventory().setItem(9, ItemStack.EMPTY);
        }
        LogUtils.getLogger().info("FOODHEALING_TACZ_BURST_PASS shotsPerTrigger=6 enabledRolls=6 disabledRolls=0");
    }

    private static void manualBolt(ServerPlayer player, IShokugiData data, Observer observer) {
        data.setSkillDisabled(SKILL, false);
        for (int magazine : new int[]{0, 5}) {
            ItemStack stack = gun("m700", magazine, true);
            var script = equip(player, stack);
            var holder = IGunOperator.fromLivingEntity(player).getDataHolder();
            var draw = new LivingEntityDrawGun(player, holder);
            var shoot = new LivingEntityShoot(player, holder, draw);
            var bolt = new LivingEntityBolt(holder, player, draw, shoot);
            script.shootOnce(true);
            require(TaczAmmoAdapter.needsBolt(player, stack), "manual action skipped mechanical cycle");
            int before = observer.bullets;
            script.shootOnce(true);
            bolt.tickBolt();
            require(observer.bullets == before && TaczAmmoAdapter.needsBolt(player, stack), "pending state vanished or extra shot fired");
            holder.drawTimestamp = -1;
            holder.shootTimestamp = -5000;
            require(shoot.getShootCoolDown() == 0 && draw.getDrawCoolDown() == 0, "fixture cooldown not elapsed");
            bolt.bolt();
            require(holder.isBolting, "retained last/full chamber did not start native bolt cycle");
            holder.boltTimestamp = System.currentTimeMillis() - 5000;
            bolt.tickBolt();
            require(!holder.isBolting && !TaczAmmoAdapter.needsBolt(player, stack), "native bolt completion did not release cycle");
            require(IGun.getIGunOrNull(stack).getCurrentAmmoCount(stack) == magazine
                    && IGun.getIGunOrNull(stack).hasBulletInBarrel(stack), "bolt cycle consumed/duplicated saved round");
            script.shootOnce(true);
            data.setSkillDisabled(SKILL, true);
            require(TaczAmmoAdapter.needsBolt(player, stack), "OFF skipped an already-required mechanical cycle");
            holder.shootTimestamp = -5000;
            bolt.bolt();
            holder.boltTimestamp = System.currentTimeMillis() - 5000;
            bolt.tickBolt();
            script.shootOnce(true);
            require(!IGun.getIGunOrNull(stack).hasBulletInBarrel(stack), "OFF failed ordinary chamber consumption");
            data.setSkillDisabled(SKILL, false);
        }
        LogUtils.getLogger().info("FOODHEALING_TACZ_MANUAL_BOLT_PASS emptyMagazine=true fullMagazine=true pending=true normalCycle=true off=true");
    }

    private static void reload(ServerPlayer player, IShokugiData data) {
        for (boolean enabled : new boolean[]{false, true}) {
            data.setSkillDisabled(SKILL, !enabled);
            for (boolean chamber : new boolean[]{false, true}) {
                ItemStack stack = gun("glock_17", 0, chamber);
                equip(player, stack);
                stockReserve(player, "9mm", 40);
                var holder = IGunOperator.fromLivingEntity(player).getDataHolder();
                holder.drawTimestamp = -1;
                holder.shootTimestamp = -5000;
                IGunOperator.fromLivingEntity(player).reload();
                require(holder.reloadStateType.isReloading(), "normal/tactical reload did not start");
                holder.reloadTimestamp = System.currentTimeMillis() - 5000;
                ((ModernKineticGunItem) stack.getItem()).tickReload(holder, stack, player);
                var gun = IGun.getIGunOrNull(stack);
                int total = gun.getCurrentAmmoCount(stack) + (gun.hasBulletInBarrel(stack) ? 1 : 0)
                        + reserveTotal(player);
                require(total == 40 + (chamber ? 1 : 0), "reload duplicated or lost rounds");
                require(gun.getCurrentAmmoCount(stack) > 0, "reload fixture did not load magazine");
                IGunOperator.fromLivingEntity(player).cancelReload();
                clearReserve(player);
            }
        }
        LogUtils.getLogger().info("FOODHEALING_TACZ_RELOAD_PASS enabled=true disabled=true empty=true tactical=true totalRounds=conserved");
    }

    private static void replay(ServerPlayer player, IShokugiData data, Observer observer) {
        data.setSkillDisabled(SKILL, false);
        equip(player, gun("glock_17", 17, true));
        var op = IGunOperator.fromLivingEntity(player);
        var holder = op.getDataHolder();
        holder.drawTimestamp = -1;
        holder.shootTimestamp = -5000;
        long timestamp = System.currentTimeMillis() - holder.baseTimestamp;
        int before = observer.bullets;
        ShootResult first = op.shoot(() -> 0F, () -> 0F, timestamp);
        require(first == ShootResult.SUCCESS, "native shoot setup failed: " + first);
        require(observer.bullets == before + 1, "native shoot did not emit one projectile");
        require(op.shoot(() -> 0F, () -> 0F, timestamp) != ShootResult.SUCCESS
                && observer.bullets == before + 1, "same timestamp replay emitted another shot");
        LogUtils.getLogger().info("FOODHEALING_TACZ_REPLAY_PASS nativeShoot=true duplicateTimestamp=rejected");
    }

    private static ItemStack gun(String id, int magazine, boolean chamber) {
        ResourceLocation key = new ResourceLocation("tacz", id);
        require(TimelessAPI.getCommonGunIndex(key).isPresent(), "missing approved default gun " + id);
        return GunItemBuilder.create().setId(key).setAmmoCount(magazine).setAmmoInBarrel(chamber).setFireMode(FireMode.SEMI).build();
    }
    private static ItemStack ammo(String id, int count) {
        ItemStack result = AmmoItemBuilder.create().setId(new ResourceLocation("tacz", id)).build();
        result.setCount(count);
        require(count > 0 && count <= result.getMaxStackSize(), "fixture ammo exceeds actual stack limit");
        return result;
    }
    private static void stockReserve(ServerPlayer player, String id, int count) {
        clearReserve(player);
        int limit = ammo(id, 1).getMaxStackSize();
        for (int slot = 9; slot < 36 && count > 0; slot++) {
            int next = Math.min(limit, count);
            player.getInventory().setItem(slot, ammo(id, next));
            count -= next;
        }
        require(count == 0, "fixture reserve does not fit inventory");
    }
    private static void clearReserve(ServerPlayer player) {
        for (int slot = 9; slot < 36; slot++) player.getInventory().setItem(slot, ItemStack.EMPTY);
    }
    private static int reserveTotal(ServerPlayer player) {
        int total = 0;
        for (int slot = 9; slot < 36; slot++) total += player.getInventory().getItem(slot).getCount();
        return total;
    }
    private static ModernKineticGunScriptAPI equip(ServerPlayer player, ItemStack stack) {
        player.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        IGunOperator op = IGunOperator.fromLivingEntity(player);
        op.draw(player::getMainHandItem);
        player.setItemSlot(EquipmentSlot.MAINHAND, stack);
        op.draw(player::getMainHandItem);
        var index = TimelessAPI.getCommonGunIndex(IGun.getIGunOrNull(stack).getGunId(stack)).orElseThrow();
        AttachmentCacheProperty cache = new AttachmentCacheProperty();
        cache.eval(stack, index.getGunData());
        op.updateCacheProperty(cache);
        ModernKineticGunScriptAPI script = new ModernKineticGunScriptAPI();
        script.setShooter(player);
        script.setItemStack(stack);
        script.setDataHolder(op.getDataHolder());
        script.setPitchSupplier(() -> 0F);
        script.setYawSupplier(() -> 0F);
        return script;
    }
    private static void heat(ItemStack stack, float amount, boolean locked) {
        IGun.getIGunOrNull(stack).setHeatAmount(stack, amount);
        IGun.getIGunOrNull(stack).setOverheatLocked(stack, locked);
    }
    private static float heat(ItemStack stack) { return IGun.getIGunOrNull(stack).getHeatAmount(stack); }
    private static boolean locked(ItemStack stack) { return IGun.getIGunOrNull(stack).isOverheatLocked(stack); }
    private static void dispatch(Connection connection, Object message) {
        ICustomPacket<?> packet = (ICustomPacket<?>) PacketHandler.INSTANCE.toVanillaPacket(message, NetworkDirection.PLAY_TO_SERVER);
        try { require(NetworkHooks.onCustomPayload(packet, connection), "registered server packet rejected"); }
        finally { packet.getInternalData().release(); }
    }
    private static boolean synced(EmbeddedChannel channel, ItemStack expected) {
        channel.runPendingTasks();
        boolean found = false;
        Object message;
        while ((message = channel.readOutbound()) != null) {
            if (message instanceof ClientboundContainerSetSlotPacket slot && slot.getContainerId() == -2) {
                found |= ItemStack.matches(expected, slot.getItem());
            }
            ReferenceCountUtil.release(message);
        }
        return found;
    }
    private static void drain(EmbeddedChannel channel) { synced(channel, ItemStack.EMPTY); }
    private static void require(boolean condition, String message) {
        if (!condition) throw new IllegalStateException("TaCZ Ammo Conservation: " + message);
    }
    public static final class Observer {
        private final ServerPlayer player;
        private int bullets;
        private boolean cancel;
        private Observer(ServerPlayer player) { this.player = player; }
        @SubscribeEvent public void fire(GunFireEvent event) {
            if (event.getShooter() == player && cancel) event.setCanceled(true);
        }
        @SubscribeEvent public void bullet(EntityJoinLevelEvent event) {
            if (event.getEntity() instanceof EntityKineticBullet bullet && bullet.getOwner() == player) {
                bullets++;
                // Count real projectiles without populating the disposable world.
                event.setCanceled(true);
            }
        }
    }
}
