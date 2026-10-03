package com.leva.foodhealing;

import com.leva.foodhealing.capability.*;
import com.leva.foodhealing.network.ShokugiSyncPacket;
import io.netty.buffer.Unpooled;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

/** Test-only migration checks; no real-world payload is included in source or the mod Jar. */
@GameTestHolder(FoodHealingMod.MODID)
@PrefixGameTestTemplate(false)
public final class LegacyMigrationGameTests {
    private LegacyMigrationGameTests() { }

    @GameTest(template = "empty")
    public static void legacyRespecSurvivesPlayerLoadCloneAndSync(GameTestHelper helper) {
        Player template = helper.makeMockSurvivalPlayer();
        var diversity = template.getCapability(FoodDiversityProvider.FOOD_DIVERSITY).orElseThrow(IllegalStateException::new);
        for (int i = 0; i < 28; i++) {
            if (i == 25) diversity.resetFoodCount();
            diversity.addEatenFood("fixture:food_" + i);
        }
        diversity.addMaxHealthBonus(10);
        CompoundTag expectedDiversity = template.saveWithoutId(new CompoundTag()).getCompound("ForgeCaps")
                .getCompound("foodhealing:food_diversity").copy();
        CompoundTag legacy = new CompoundTag();
        legacy.putInt("ShokugiLevel", 2);
        legacy.putInt("EatCount", 35);
        legacy.putIntArray("Unknown", new int[]{3, 2, 1});
        CompoundTag seed = template.saveWithoutId(new CompoundTag());
        seed.getCompound("ForgeCaps").put("foodhealing:shokugi_data", legacy.copy());
        Player player = helper.makeMockSurvivalPlayer();
        player.load(seed);
        IShokugiData data = progress(player);
        helper.assertTrue(data.getLevel() == 2 && data.getEatCount() == 350 && data.getUnspentSkillPoints() == 2
                && data.getSpentSkillPoints() == 0 && !data.isLegacyMigrationPending()
                && data.getAcquiredSkills().isEmpty() && data.getBaseStats().isEmpty()
                && data.getDisabledSkills().isEmpty(), "player load respec mismatch");
        CompoundTag canonical = data.serializeNBT();
        helper.assertTrue(canonical.getCompound("LegacyV2Backup").equals(legacy), "raw backup mismatch");
        for (int i = 0; i < 3; i++) {
            player.load(player.saveWithoutId(new CompoundTag()));
            MinecraftForge.EVENT_BUS.post(new PlayerEvent.PlayerLoggedInEvent(player));
            Player clone = helper.makeMockSurvivalPlayer();
            MinecraftForge.EVENT_BUS.post(new PlayerEvent.Clone(clone, player, true));
            MinecraftForge.EVENT_BUS.post(new PlayerEvent.PlayerRespawnEvent(clone, false));
            helper.assertTrue(progress(clone).serializeNBT().equals(canonical), "clone/save/login repeated refund");
            helper.assertTrue(clone.saveWithoutId(new CompoundTag()).getCompound("ForgeCaps")
                    .getCompound("foodhealing:food_diversity").equals(expectedDiversity), "Diversity changed");
            FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
            try {
                new ShokugiSyncPacket(progress(clone).serializeNBT()).encode(buffer);
                CompoundTag packet = buffer.readNbt();
                ShokugiData clientSnapshot = new ShokugiData();
                clientSnapshot.deserializeNBT(packet);
                helper.assertTrue(clientSnapshot.serializeNBT().equals(canonical), "sync snapshot re-migrated");
            } finally {
                buffer.release();
            }
            player = clone;
        }
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void refundBuysFreshNodesWithoutAutomaticOwnershipOrReplay(GameTestHelper helper) {
        Player player = helper.makeMockSurvivalPlayer();
        IShokugiData data = progress(player);
        CompoundTag legacy = new CompoundTag();
        legacy.putInt("ShokugiLevel", 115);
        legacy.putInt("EatCount", 35);
        data.deserializeNBT(legacy);
        helper.assertTrue(data.getAcquiredSkills().isEmpty(), "migration auto purchased a node");
        String[] ids = {FoodHealingSkillIds.GATHERING, FoodHealingSkillIds.UNBREAKING, FoodHealingSkillIds.PURSUIT};
        long[] remaining = {110, 100, 0};
        for (int i = 0; i < ids.length; i++) {
            helper.assertTrue(FoodHealingSkills.tryPurchase(data, ids[i], 0) == FoodHealingSkills.PurchaseResult.SUCCESS,
                    "normal purchase rejected after migration");
            helper.assertTrue(data.getUnspentSkillPoints() == remaining[i] && data.getSpentSkillPoints() == 115 - remaining[i]
                    && data.getSkillLevel(ids[i]) == 1, "canonical 5/10/100 cost mismatch");
            CompoundTag beforeReplay = data.serializeNBT();
            helper.assertTrue(FoodHealingSkills.tryPurchase(data, ids[i], 0) == FoodHealingSkills.PurchaseResult.STALE_REQUEST
                    && data.serializeNBT().equals(beforeReplay), "replay consumed or refunded SP");
        }
        CompoundTag purchased = data.serializeNBT();
        data.deserializeNBT(legacy);
        helper.assertTrue(data.serializeNBT().equals(purchased), "replayed legacy payload undid purchase");
        player.load(player.saveWithoutId(new CompoundTag()));
        helper.assertTrue(progress(player).serializeNBT().equals(purchased), "purchased state reset on player reload");
        helper.succeed();
    }

    private static IShokugiData progress(Player player) {
        return player.getCapability(ShokugiProvider.SHOKUGI_CAPA).orElseThrow(IllegalStateException::new);
    }
}
