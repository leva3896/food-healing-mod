package com.leva.foodhealing;

import com.leva.foodhealing.capability.ShokugiData;
import com.leva.foodhealing.capability.ShokugiProvider;
import com.leva.foodhealing.compat.TaczAmmoCompatibility;
import com.mojang.authlib.GameProfile;
import com.mojang.logging.LogUtils;
import com.tacz.guns.api.item.IGun;
import com.tacz.guns.api.item.builder.GunItemBuilder;
import io.netty.channel.embedded.EmbeddedChannel;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.LevelResource;
import java.nio.file.Files;
import java.util.UUID;

/** Persistence through PlayerList/PlayerDataStorage, never a user world or edited save. */
public final class TaczAmmoRestartVerification {
    private static final UUID ID = UUID.fromString("1b7537b0-caa8-4a56-941e-a303c5a15002");
    public static void verify(MinecraftServer server) {
        boolean write = "write".equals(System.getProperty("foodhealing.restart.phase"));
        var file = server.getWorldPath(LevelResource.PLAYER_DATA_DIR).resolve(ID + ".dat");
        require(Files.exists(file) != write, "unexpected Ammo fixture playerdata presence");
        var player = new ServerPlayer(server, server.overworld(), new GameProfile(ID, "FHAmmoRestart"));
        var connection = new Connection(PacketFlow.SERVERBOUND);
        var channel = new EmbeddedChannel(connection);
        try {
            server.getPlayerList().placeNewPlayer(connection, player);
            var actual = player.getCapability(ShokugiProvider.SHOKUGI_CAPA).orElseThrow(IllegalStateException::new);
            ShokugiData expected = new ShokugiData();
            expected.setLevel(1234567890123L);
            expected.setEatCount(456);
            expected.setUnspentSkillPoints(517);
            expected.setSpentSkillPoints(11);
            for (int level = 0; level < 10; level++) {
                require(FoodHealingSkills.tryPurchase(expected, FoodHealingSkillIds.TACZ_AMMO_CONSERVATION, level)
                        == FoodHealingSkills.PurchaseResult.SUCCESS, "ordinary 50SP purchase failed");
            }
            expected.setSkillDisabled(FoodHealingSkillIds.TACZ_AMMO_CONSERVATION, true);
            if (write) {
                actual.copyFrom(expected);
                var stack = GunItemBuilder.create().setId(new ResourceLocation("tacz", "m700"))
                        .setAmmoCount(5).setAmmoInBarrel(true).build();
                player.getInventory().setItem(0, stack);
                var hot = GunItemBuilder.create().setId(new ResourceLocation("tacz", "minigun")).build();
                IGun.getIGunOrNull(hot).setHeatAmount(hot, 123);
                IGun.getIGunOrNull(hot).setOverheatLocked(hot, true);
                player.getInventory().setItem(1, hot);
            }
            require(actual.serializeNBT().equals(expected.serializeNBT()), "Ammo SP/level/toggle canonical mismatch");
            require(actual.getUnspentSkillPoints() == 17 && actual.getSpentSkillPoints() == 511, "SP accounting mismatch");
            var main = player.getInventory().getItem(0);
            var hot = player.getInventory().getItem(1);
            require(IGun.getIGunOrNull(main) != null && IGun.getIGunOrNull(main).getCurrentAmmoCount(main) == 5
                    && IGun.getIGunOrNull(main).hasBulletInBarrel(main), "magazine/chamber changed across restart");
            require(IGun.getIGunOrNull(hot).getHeatAmount(hot) == 123 && IGun.getIGunOrNull(hot).isOverheatLocked(hot),
                    "inactive heat state changed across restart");
            TaczAmmoCompatibility.onEnabled(player);
            require(actual.serializeNBT().equals(expected.serializeNBT()), "OFF normalization changed progression");
            server.getPlayerList().saveAll();
            require(Files.exists(file), "normal player save missing");
            verifyRespawn(server, expected);
            LogUtils.getLogger().info("FOODHEALING_TACZ_AMMO_RESTART_PASS phase={} pid={} level=10 unspent=17 spent=61 disabled=true chamber=true otherHeat=123",
                    write ? "write" : "read", ProcessHandle.current().pid());
        } finally {
            server.getPlayerList().remove(player);
            channel.finishAndReleaseAll();
        }
    }
    private static void verifyRespawn(MinecraftServer server, ShokugiData expected) {
        var original = new ServerPlayer(server, server.overworld(), new GameProfile(
                UUID.fromString("1b7537b0-caa8-4a56-941e-a303c5a15003"), "FHAmmoRespawn"));
        var connection = new Connection(PacketFlow.SERVERBOUND);
        var channel = new EmbeddedChannel(connection);
        ServerPlayer current = original;
        try {
            server.getPlayerList().placeNewPlayer(connection, original);
            original.getCapability(ShokugiProvider.SHOKUGI_CAPA).orElseThrow(IllegalStateException::new).copyFrom(expected);
            current = server.getPlayerList().respawn(original, false);
            require(current != original, "respawn fixture did not replace the player");
            var cloned = current.getCapability(ShokugiProvider.SHOKUGI_CAPA).orElseThrow(IllegalStateException::new);
            require(cloned.serializeNBT().equals(expected.serializeNBT()), "normal respawn lost Ammo progression/toggle");
            require(current.getHealth() == current.getMaxHealth(), "respawn health regression");
            LogUtils.getLogger().info("FOODHEALING_TACZ_AMMO_RESPAWN_PASS actualPlayerListRespawn=true canonical=unchanged");
        } finally {
            server.getPlayerList().remove(current);
            channel.finishAndReleaseAll();
        }
    }
    private static void require(boolean value, String message) { if (!value) throw new AssertionError(message); }
}
