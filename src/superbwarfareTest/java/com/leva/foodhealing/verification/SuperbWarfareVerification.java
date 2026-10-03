package com.leva.foodhealing.verification;

import com.google.gson.GsonBuilder;
import com.leva.foodhealing.FoodHealingBaseStatIds;
import com.leva.foodhealing.FoodHealingSkillIds;
import com.leva.foodhealing.capability.IShokugiData;
import com.leva.foodhealing.capability.ShokugiData;
import com.leva.foodhealing.capability.ShokugiProvider;
import com.leva.foodhealing.compat.SuperbWarfareCompat;
import com.mojang.authlib.GameProfile;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/** Separate reobfuscated test mod, executed only in a fresh production Forge server. */
@Mod("foodhealing_sw_verification")
public final class SuperbWarfareVerification {
    private final List<Map<String, Object>> results = new ArrayList<>();
    private final List<Map<String, Object>> hits = new ArrayList<>();
    private Cow target;

    public SuperbWarfareVerification() { MinecraftForge.EVENT_BUS.register(this); }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void observe(LivingHurtEvent event) {
        if (event.getEntity() != target) return;
        var source = event.getSource();
        hits.add(Map.of("amount", event.getAmount(), "type", source.typeHolder().unwrapKey().orElseThrow().location().toString(),
                "directClass", source.getDirectEntity() == null ? "null" : source.getDirectEntity().getClass().getName(),
                "attacker", source.getEntity() == null ? "null" : source.getEntity().getUUID().toString(),
                "supported", SuperbWarfareCompat.isSupportedGunfire(source)));
    }

    @SubscribeEvent
    public void run(ServerStartedEvent event) throws Exception {
        var server = event.getServer();
        Path root = server.getServerDirectory().toPath().toRealPath();
        if (!root.equals(Path.of(System.getProperty("foodhealing.sw.verificationRoot")).toRealPath())) {
            throw new IllegalStateException("wrong isolated verification directory");
        }
        var receipt = new LinkedHashMap<String, Object>();
        receipt.put("pid", ProcessHandle.current().pid());
        receipt.put("loadedMods", ModList.get().getMods().stream().map(m -> Map.of(
                "id", m.getModId(), "version", m.getVersion().toString(),
                "file", m.getOwningFile().getFile().getFilePath().toString())).toList());
        try {
            require(server.isDedicatedServer(), "not dedicated");
            require("0.8.9.1".equals(ModList.get().getModContainerById("superbwarfare").orElseThrow()
                    .getModInfo().getVersion().toString()), "wrong loader version");
            ServerLevel level = server.overworld();
            Player shooter = FakePlayerFactory.get(level, new GameProfile(UUID.randomUUID(), "FHSW-shooter"));
            Player other = FakePlayerFactory.get(level, new GameProfile(UUID.randomUUID(), "FHSW-other"));
            target = new Cow(EntityType.COW, level);
            target.setNoAi(true);
            target.setPos(0, 4, 0);
            target.getAttribute(Attributes.MAX_HEALTH).setBaseValue(1024D);
            level.addFreshEntity(target);
            for (boolean migrated : new boolean[]{false, true}) {
                for (long value : new long[]{0, 1, 200}) {
                    setData(shooter, value, migrated);
                    shot(level, shooter, "level-" + value + "-migrated-" + migrated, 1F, false, 0F,
                            1D + value, 1);
                }
            }
            setData(shooter, 1, false);
            shot(level, shooter, "headshot", 1F, true, 0F, 4D, 1);
            shot(level, shooter, "absolute", 1F, false, 1F, 2D, 1);
            shot(level, shooter, "split-one-factor-per-event", 2F, false, .5F, 4D, 2);
            shot(level, shooter, "headshot-split", 2F, true, .5F, 8D, 2);
            var data = data(shooter);
            data.setBaseStatPoints(FoodHealingBaseStatIds.BASE_OUTGOING_DAMAGE, 10);
            data.setBaseStatPoints(FoodHealingBaseStatIds.TACZ_BASE_OUTGOING_DAMAGE, 900);
            data.setSkillLevel(FoodHealingSkillIds.TACZ_AMMO_CONSERVATION, 10);
            data.setSkillLevel(FoodHealingSkillIds.HEROICS, 1);
            shooter.setHealth(4F);
            shot(level, shooter, "dedicated-global-heroics-no-tacz", 1F, false, 0F, 8D, 1);
            setData(other, 0, false);
            shot(level, other, "other-player-no-level-leak", 1F, false, 0F, 1D, 1);
            shot(level, null, "ownerless-no-factor", 1F, false, 0F, 1D, 1);
            shot(level, new Cow(EntityType.COW, level), "non-player-owner-no-factor", 1F, false, 0F, 1D, 1);
            setData(shooter, (long) Integer.MAX_VALUE + 1, false);
            shot(level, shooter, "long-level-no-int-narrowing", 1E-8F, false, 0F,
                    (double) 1E-8F * (1D + (long) Integer.MAX_VALUE + 1), 1);
            CompoundTag invalid = new CompoundTag();
            invalid.putInt("FoodHealingDataVersion", 99);
            invalid.putLong("ShokugiLevel", 200);
            invalid.putLong("LegacyShokugiLevel", 200);
            data(shooter).deserializeNBT(invalid);
            require(data(shooter).isLegacyMigrationPending(), "invalid pending lost");
            shot(level, shooter, "invalid-pending-no-current-level", 1F, false, 0F, 1D, 1);
            setData(shooter, 2, true);
            var pending = data(shooter).serializeNBT();
            pending.putBoolean("LegacyMigrationPending", true);
            pending.putLong("ShokugiLevel", 200);
            data(shooter).deserializeNBT(pending);
            shot(level, shooter, "edited-pending-old-fallback", 1F, false, 0F, 3.6D, 1);
            setData(shooter, 200, false);
            var projectile = projectile(level, shooter);
            // These negative cases deliberately use synthetic sources, reported separately.
            for (String damageId : new String[]{"projectile_explosion", "projectile_hit", "custom_explosion"}) {
                var key = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("superbwarfare", damageId));
                var source = new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE)
                        .getHolderOrThrow(key), projectile, shooter);
                resetTarget();
                target.hurt(source, 1F);
                require(hits.size() == 1 && !(boolean) hits.get(0).get("supported"), "unsupported route admitted");
                require(Math.abs(target.getHealth() - 1023F) < .01, "unsupported route amplified");
                results.add(Map.of("case", "synthetic-negative-" + damageId, "status", "PASS", "hits", List.copyOf(hits)));
            }
            target.discard();
            target = null;
            receipt.put("status", "PASS");
            LogUtils.getLogger().info("FOODHEALING_SW_INTEGRATION_PASS cases={} actualAttackCases=17 syntheticNegativeCases=3", results.size());
        } catch (Throwable failure) {
            receipt.put("status", "FAIL");
            receipt.put("failure", failure.toString());
            LogUtils.getLogger().error("FOODHEALING_SW_INTEGRATION_FAIL", failure);
        } finally {
            receipt.put("results", results);
            server.saveEverything(true, true, true);
            receipt.put("saveRequested", true);
            Files.writeString(root.resolve("sw-result.json"), new GsonBuilder().setPrettyPrinting().create().toJson(receipt));
            server.halt(false);
        }
    }

    private void shot(ServerLevel level, Entity owner, String label, float raw, boolean headshot,
                      float bypass, double expected, int count) throws Exception {
        var projectile = projectile(level, owner);
        var clazz = projectile.getClass();
        clazz.getMethod("setHeadShot", float.class).invoke(projectile, 2F);
        clazz.getMethod("setBypassArmorRate", float.class).invoke(projectile, bypass);
        resetTarget();
        // Invoke the real MOD's public attack processing, not a handmade DamageSource or FHR handler.
        clazz.getMethod("performDamage", Entity.class, float.class, boolean.class)
                .invoke(projectile, target, raw, headshot);
        require(hits.size() == count, label + " event count: " + hits);
        double sum = hits.stream().mapToDouble(h -> ((Number) h.get("amount")).doubleValue()).sum();
        require(Math.abs(sum - expected) < .001, label + " damage sum " + sum + " != " + expected);
        require(Math.abs((1024D - target.getHealth()) - expected) < .002,
                label + " actual HP loss " + (1024D-target.getHealth()) + " != " + expected);
        for (var hit : hits) {
            require(hit.get("directClass").equals(clazz.getName()), "not the real direct projectile");
            require(hit.get("attacker").equals(owner == null ? "null" : owner.getUUID().toString()), "owner mapping mismatch");
        }
        results.add(Map.of("case", label, "status", "PASS", "raw", raw, "expected", expected,
                "hpLoss", 1024D - target.getHealth(), "hits", List.copyOf(hits)));
        LogUtils.getLogger().info("FOODHEALING_SW_CASE_PASS {} events={} amount={} hpLoss={}", label, count, sum, 1024D-target.getHealth());
    }

    private Projectile projectile(ServerLevel level, Entity owner) {
        var type = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation("superbwarfare", "projectile"));
        require(type != null, "actual projectile type missing");
        var entity = type.create(level);
        require(entity != null && entity.getClass().getName().equals(
                "com.atsuishio.superbwarfare.entity.projectile.ProjectileEntity"), "wrong actual projectile class");
        Projectile projectile = (Projectile) entity;
        projectile.setOwner(owner);
        return projectile;
    }

    private void resetTarget() { hits.clear(); target.setHealth(1024F); target.invulnerableTime = 0; }
    private static IShokugiData data(Player player) {
        return player.getCapability(ShokugiProvider.SHOKUGI_CAPA).orElseThrow(AssertionError::new);
    }
    private static void setData(Player player, long level, boolean migrated) {
        var fresh = new ShokugiData();
        fresh.setLevel(level);
        var tag = fresh.serializeNBT();
        if (migrated) { tag = new CompoundTag(); tag.putLong("ShokugiLevel", level); tag.putLong("EatCount", 1); }
        data(player).deserializeNBT(tag);
        player.setHealth(player.getMaxHealth());
        require(!data(player).isLegacyMigrationPending(), "fresh/migrated still pending");
    }
    private static void require(boolean value, String message) { if (!value) throw new AssertionError(message); }
}
