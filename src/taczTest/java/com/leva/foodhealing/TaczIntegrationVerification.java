package com.leva.foodhealing;

import com.leva.foodhealing.capability.IShokugiData;
import com.leva.foodhealing.capability.ShokugiData;
import com.leva.foodhealing.capability.ShokugiProvider;
import com.mojang.authlib.GameProfile;
import com.mojang.logging.LogUtils;
import com.tacz.guns.api.GunProperties;
import com.tacz.guns.api.TimelessAPI;
import com.tacz.guns.api.entity.IGunOperator;
import com.tacz.guns.api.event.common.EntityHurtByGunEvent;
import com.tacz.guns.api.item.IGun;
import com.tacz.guns.api.item.builder.GunItemBuilder;
import com.tacz.guns.entity.EntityKineticBullet;
import com.tacz.guns.item.ModernKineticGunScriptAPI;
import com.tacz.guns.resource.modifier.AttachmentCacheProperty;
import com.tacz.guns.resource.pojo.data.gun.GunData;
import com.tacz.guns.util.TacHitResult;
import io.netty.channel.embedded.EmbeddedChannel;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

/** Optional integration fixture; this source set is absent from normal builds and the distributable Jar. */
public final class TaczIntegrationVerification {
    private static final ResourceLocation GUN_ID = new ResourceLocation("tacz", "glock_17");

    private TaczIntegrationVerification() {
    }

    public static void verify(MinecraftServer server) throws java.io.IOException {
        TaczClientHookBytecodeVerification.verify();
        var index = TimelessAPI.getCommonGunIndex(GUN_ID).orElseThrow();
        GunData gunData = index.getGunData();
        ItemStack gun = GunItemBuilder.create().setId(GUN_ID).setAmmoCount(17).setAmmoInBarrel(true).build();
        require(!gun.isEmpty(), "approved default gun failed to load");
        ServerPlayer shooter = new TaczAmmoTestPlayer(server, server.overworld(),
                new GameProfile(UUID.randomUUID(), "FHTaczFixture"));
        Connection connection = new Connection(PacketFlow.SERVERBOUND);
        EmbeddedChannel channel = new EmbeddedChannel(connection);
        shooter.connection = new ServerGamePacketListenerImpl(server, connection, shooter);
        shooter.setPos(0, 80, 0);
        shooter.xOld = 0;
        shooter.yOld = 80;
        shooter.zOld = 0;
        shooter.setItemSlot(EquipmentSlot.MAINHAND, gun);
        IShokugiData data = shooter.getCapability(ShokugiProvider.SHOKUGI_CAPA)
                .orElseThrow(IllegalStateException::new);
        data.copyFrom(new ShokugiData());
        AttachmentCacheProperty cache = new AttachmentCacheProperty();
        cache.eval(gun, gunData);
        IGunOperator.fromLivingEntity(shooter).updateCacheProperty(cache);
        Cow target = new Cow(EntityType.COW, server.overworld());
        target.setPos(0, 80, 2);
        target.getAttribute(Attributes.MAX_HEALTH).setBaseValue(1000);
        target.getAttribute(Attributes.ARMOR).setBaseValue(0);
        target.getAttribute(Attributes.ARMOR_TOUGHNESS).setBaseValue(0);
        HitObserver observer = new HitObserver(target);
        MinecraftForge.EVENT_BUS.register(observer);
        int cases = 0;
        try {
            // Default Glock body hit at two blocks is 7 according to the actual bundled gun data.
            hit(shooter, target, gun, gunData, observer, 7, "baseline"); cases++;
            data.setBaseStatPoints(FoodHealingBaseStatIds.BASE_OUTGOING_DAMAGE, 10);
            hit(shooter, target, gun, gunData, observer, 14, "global x2"); cases++;
            data.setBaseStatPoints(FoodHealingBaseStatIds.TACZ_BASE_OUTGOING_DAMAGE, 100);
            hit(shooter, target, gun, gunData, observer, 28, "global x2 and TaCZ x2"); cases++;
            data.setSkillLevel(FoodHealingSkillIds.HEROICS, 5);
            shooter.setHealth(8);
            hit(shooter, target, gun, gunData, observer, 140, "Heroics x5 composition"); cases++;
            data.setSkillDisabled(FoodHealingSkillIds.HEROICS, true);
            hit(shooter, target, gun, gunData, observer, 28, "Heroics OFF"); cases++;
            data.setSkillLevel(FoodHealingSkillIds.TACZ_AMMO_CONSERVATION, 10);
            hit(shooter, target, gun, gunData, observer, 42, "existing ammo-skill damage component"); cases++;
            data.setSkillDisabled(FoodHealingSkillIds.TACZ_AMMO_CONSERVATION, true);
            hit(shooter, target, gun, gunData, observer, 28, "ammo-skill OFF damage component"); cases++;

            // Exercise both real TaCZ damage components, not an invented duplicate-event filter.
            cache.setCache(GunProperties.ARMOR_IGNORE, 0.5F);
            hit(shooter, target, gun, gunData, observer, 28, "split armor-piercing hit"); cases++;
            observer.cancel = true;
            hit(shooter, target, gun, gunData, observer, 0, "TaCZ Pre cancellation"); cases++;
            observer.cancel = false;
            hit(shooter, target, gun, gunData, observer, 28, "after cancellation"); cases++;
            require(IGun.getIGunOrNull(gun).getCurrentAmmoCount(gun) == 17,
                    "hit-only fixture unexpectedly mutated ammunition");
            data.copyFrom(new ShokugiData());
            data.setSkillLevel(FoodHealingSkillIds.SATISFACTION, 3);
            data.setSkillLevel(FoodHealingSkillIds.GATHERING, 10);
            data.setSkillLevel(FoodHealingSkillIds.UNBREAKING, 10);
            data.setSkillLevel(FoodHealingSkillIds.ARMOR_MASTERY, 1);
            ModernKineticGunScriptAPI script = new ModernKineticGunScriptAPI();
            script.setShooter(shooter);
            script.setItemStack(gun);
            script.setDataHolder(IGunOperator.fromLivingEntity(shooter).getDataHolder());
            require(script.reduceAmmoOnce(), "normal TaCZ ammo consumption failed");
            require(IGun.getIGunOrNull(gun).getCurrentAmmoCount(gun) == 16,
                    "non-TaCZ skills affected real ammunition consumption");
            cases++;
            verifyPlayerBoundary(server, shooter, gun, gunData, cache);
            cases += 2;
            LogUtils.getLogger().info("FOODHEALING_TACZ_DAMAGE_PASS version=1.1.7-hotfix2 cases={} gun={}", cases, GUN_ID);
            TaczAmmoConservationVerification.verify(shooter, connection, channel);
            TaczAmmoRestartVerification.verify(server);
        } finally {
            MinecraftForge.EVENT_BUS.unregister(observer);
            channel.finishAndReleaseAll();
        }
    }

    private static void verifyPlayerBoundary(MinecraftServer server, ServerPlayer shooter, ItemStack gun,
                                             GunData gunData, AttachmentCacheProperty cache) {
        ServerPlayer victim = new ServerPlayer(server, server.overworld(),
                new GameProfile(UUID.randomUUID(), "FHTaczVictim"));
        Connection connection = new Connection(PacketFlow.SERVERBOUND);
        EmbeddedChannel channel = new EmbeddedChannel(connection);
        victim.connection = new ServerGamePacketListenerImpl(server, connection, victim);
        DropObserver drops = new DropObserver(victim);
        MinecraftForge.EVENT_BUS.register(drops);
        try {
            // Let vanilla's spawn protection expire through its actual tick method.
            for (int tick = 0; tick < 61; tick++) victim.tick();
            victim.setPos(0, 80, 2);
            var data = victim.getCapability(ShokugiProvider.SHOKUGI_CAPA).orElseThrow(IllegalStateException::new);
            data.setSkillLevel(FoodHealingSkillIds.GUTS, 5);
            RootController.onFoodConsumed(victim, 18);
            require(RootController.isActive(victim), "Root setup failed");
            victim.setHealth(4);
            cache.setCache(GunProperties.ARMOR_IGNORE, 0F);
            new ProbeBullet(shooter, gun, gunData).hit(victim);
            require(victim.isAlive() && victim.getHealth() == 1 && drops.calls == 0,
                    "TaCZ hit bypassed active Root or caused death drops");

            data.setSkillDisabled(FoodHealingSkillIds.GUTS, true);
            victim.setHealth(4);
            victim.getInventory().setItem(0, new ItemStack(Items.DIAMOND, 7));
            shooter.getCapability(ShokugiProvider.SHOKUGI_CAPA).orElseThrow(IllegalStateException::new)
                    .setSkillLevel(FoodHealingSkillIds.SLAUGHTER, 1);
            new ProbeBullet(shooter, gun, gunData).hit(victim);
            require(!victim.isAlive() && drops.calls == 1, "Root OFF did not allow one normal player death");
            require(drops.items.stream().filter(item -> item.getItem().is(Items.DIAMOND))
                    .mapToInt(item -> item.getItem().getCount()).sum() == 7,
                    "TaCZ player kill duplicated/lost player inventory drops");
            LogUtils.getLogger().info("FOODHEALING_TACZ_PLAYER_PASS rootProtected=true deathEvents=1 diamonds=7");
        } finally {
            MinecraftForge.EVENT_BUS.unregister(drops);
            // Only the ephemeral entities created by this isolated fixture are cleaned up.
            for (ItemEntity item : drops.items) item.discard();
            channel.finishAndReleaseAll();
        }
    }

    private static void hit(ServerPlayer shooter, Cow target, ItemStack gun, GunData gunData,
                            HitObserver observer, float expected, String name) {
        target.setHealth(1000);
        target.invulnerableTime = 0;
        observer.calls = 0;
        ProbeBullet bullet = new ProbeBullet(shooter, gun, gunData);
        long rollsBeforeHit = ((TaczAmmoTestPlayer) shooter).rolls();
        require(Math.abs(bullet.getDamage(target.position()) - 7) < 0.001F,
                "unexpected default gun damage for " + name);
        bullet.hit(target);
        require(((TaczAmmoTestPlayer) shooter).rolls() == rollsBeforeHit,
                name + ": hit/penetration/cancellation incorrectly performed an ammunition roll");
        float actual = 1000 - target.getHealth();
        require(Math.abs(actual - expected) < 0.002F,
                name + ": expected damage " + expected + ", got " + actual);
        require(observer.calls == 1, name + ": unexpected TaCZ Pre event count " + observer.calls);
        LogUtils.getLogger().info("FOODHEALING_TACZ_CASE {} damage={}", name, actual);
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    public static final class HitObserver {
        private final LivingEntity target;
        private int calls;
        private boolean cancel;

        private HitObserver(LivingEntity target) {
            this.target = target;
        }

        @SubscribeEvent
        public void beforeHit(EntityHurtByGunEvent.Pre event) {
            if (event.getHurtEntity() == target) {
                calls++;
                if (cancel) event.setCanceled(true);
            }
        }
    }

    public static final class DropObserver {
        private final ServerPlayer victim;
        private final List<ItemEntity> items = new ArrayList<>();
        private int calls;

        private DropObserver(ServerPlayer victim) {
            this.victim = victim;
        }

        @SubscribeEvent(priority = EventPriority.LOWEST)
        public void drops(LivingDropsEvent event) {
            if (event.getEntity() == victim) {
                calls++;
                items.addAll(event.getDrops());
            }
        }
    }

    private static final class ProbeBullet extends EntityKineticBullet {
        private ProbeBullet(ServerPlayer shooter, ItemStack gun, GunData data) {
            super(shooter.level(), shooter, gun, data.getAmmoId(), GUN_ID, false, data, data.getBulletData());
        }

        private void hit(LivingEntity target) {
            Vec3 end = target.position();
            onHitEntity(new TacHitResult(new EntityResult(target, end, false)), position(), end);
        }
    }
}
