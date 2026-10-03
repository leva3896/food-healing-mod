package com.leva.foodhealing;

import com.leva.foodhealing.capability.ShokugiData;
import com.leva.foodhealing.capability.FoodDiversityData;
import com.leva.foodhealing.client.SkillRowControlsRegression;
import com.leva.foodhealing.client.HealthHudLayoutRegression;
import com.leva.foodhealing.client.ClientSatisfactionStateRegression;
import com.leva.foodhealing.client.FoodHealingGuiEntryPolicyRegression;
import com.leva.foodhealing.network.FoodDiversitySyncPacket;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.FriendlyByteBuf;

public final class ShokugiDataUnitTest {
    private ShokugiDataUnitTest() {
    }

    public static void main(String[] args) {
        FlightRegression.run();
        LegacySkillParityRegression.run();
        SkillCostRebalanceRegression.run();
        LocalizationRegression.run();
        com.leva.foodhealing.compat.endinglibrary.TimeStopRegression.run();
        NutritionCountRegression.run();
        LegacyRespecRegression.run();
        migratesLegacyIntDataAndSkillNames();
        writesLongV3Data();
        preservesRootTransactionState();
        validatesRootTimeline();
        validatesRootFixedWindowDoesNotSlide();
        validatesTrueRootReservation();
        saturatesLargeProgressionCounters();
        validatesAtomicSkillPointSpending();
        validatesFrozenSkillCostsAndPurchases();
        SkillRowControlsRegression.run();
        HealthHudLayoutRegression.run();
        ClientSatisfactionStateRegression.run();
        FoodHealingGuiEntryPolicyRegression.run();
        validatesDataDrivenSkillPrerequisites();
        OpenPrerequisiteRegression.run();
        SuperbWarfareRegression.run();
        PurificationMasteryRegression.run();
        TruthMasteryRegression.run();
        validatesRepeatableBaseStatPurchases();
        validatesFoodHungerReconciliation();
        validatesFoodDiversityNumericSafety();
        validatesFoodDiversitySyncSnapshot();
        validatesDamageReductionMath();
        validatesSaturatedPurchaseGeneration();
        DataBoundaryRegression.run();
        AmmoConservationRegression.run();
    }

    private static void migratesLegacyIntDataAndSkillNames() {
        CompoundTag legacy = new CompoundTag();
        legacy.putInt("ShokugiLevel", 1000);
        legacy.putInt("EatCount", 199);

        ListTag disabled = new ListTag();
        disabled.add(StringTag.valueOf("満足感"));
        legacy.put("DisabledSkills", disabled);

        ShokugiData data = new ShokugiData();
        data.deserializeNBT(legacy);

        require(data.getLevel() == 1000L, "legacy int level should migrate to long");
        require(data.getEatCount() == 1990L, "legacy int count should migrate to long");
        require(data.getUnspentSkillPoints() == 1000L, "legacy level must refund SP 1:1");
        require(!data.isLegacyMigrationPending(), "valid legacy migration should complete");
        require(data.getLegacyShokugiLevel() == 1000L, "raw legacy level should be retained");
        require(data.getDisabledSkills().isEmpty(), "respec must reset canonical toggles");

        CompoundTag migrated = data.serializeNBT();
        require(migrated.contains("LegacyV2Backup"), "legacy source NBT should be backed up");
        require(migrated.getCompound("LegacyV2Backup").equals(legacy), "legacy Japanese toggles must remain raw");
    }

    private static void writesLongV3Data() {
        ShokugiData data = new ShokugiData();
        data.setLevel(1234567890123L);
        data.setEatCount(456789012345L);
        data.setUnspentSkillPoints(42L);
        data.setSpentSkillPoints(7L);
        data.toggleSkill("飛翔");
        data.setSkillLevel(FoodHealingSkillIds.SATISFACTION, 2);
        data.setBaseStatPoints(FoodHealingBaseStatIds.BASE_DEFENSE, 12L);

        CompoundTag serialized = data.serializeNBT();
        require(serialized.getInt("FoodHealingDataVersion") == 5, "current schema marker missing");
        require(serialized.getLong("ShokugiLevel") == 1234567890123L, "level should serialize as long");
        require(serialized.getLong("EatCount") == 456789012345L, "count should serialize as long");
        require(serialized.getLong("UnspentSkillPoints") == 42L, "unspent SP should serialize");
        require(serialized.getLong("SpentSkillPoints") == 7L, "spent SP should serialize");

        ShokugiData roundTrip = new ShokugiData();
        roundTrip.deserializeNBT(serialized);
        require(roundTrip.isSkillDisabled(FoodHealingSkillIds.FLIGHT), "skill toggle should round-trip as resource ID");
        roundTrip.setSkillDisabled(FoodHealingSkillIds.FLIGHT, true);
        roundTrip.setSkillDisabled(FoodHealingSkillIds.FLIGHT, true);
        require(roundTrip.isSkillDisabled(FoodHealingSkillIds.FLIGHT),
                "duplicate toggle-state requests should be idempotent");
        roundTrip.setSkillDisabled(FoodHealingSkillIds.FLIGHT, false);
        roundTrip.setSkillDisabled(FoodHealingSkillIds.FLIGHT, false);
        require(!roundTrip.isSkillDisabled(FoodHealingSkillIds.FLIGHT),
                "duplicate enable requests should be idempotent");
        require(roundTrip.getSkillLevel(FoodHealingSkillIds.SATISFACTION) == 2, "skill level should round-trip");
        require(roundTrip.getBaseStatPoints(FoodHealingBaseStatIds.BASE_DEFENSE) == 12L, "base stat should round-trip");
        require(!roundTrip.isLegacyMigrationPending(), "fresh v3 data must not enter legacy compatibility mode");
        require(FoodHealingSkills.getEffectiveLevel(roundTrip, FoodHealingSkillIds.FIRE_RESISTANCE) == 0,
                "fresh v3 progression level must not implicitly unlock a skill");
    }

    private static void saturatesLargeProgressionCounters() {
        ShokugiData data = new ShokugiData();
        data.setLevel(Long.MAX_VALUE - 1L);
        data.addLevel(10L);
        require(data.getLevel() == Long.MAX_VALUE, "level addition should saturate");

        data.setEatCount(Long.MAX_VALUE - 1L);
        data.addEatCount(10L);
        require(data.getEatCount() == Long.MAX_VALUE, "eat count addition should saturate");

        data.setUnspentSkillPoints(Long.MAX_VALUE - 1L);
        data.addUnspentSkillPoints(10L);
        require(data.getUnspentSkillPoints() == Long.MAX_VALUE, "SP addition should saturate");
    }

    private static void preservesRootTransactionState() {
        ShokugiData data = new ShokugiData();
        data.setRootAccumulatedNutrition(17);
        data.setRootAccumulationDeadline(1_234L);
        data.setRootActiveUntil(5_678L);
        data.setRootCooldownUntil(9_012L);
        data.setRootReservedNutrition(18);

        ShokugiData roundTrip = new ShokugiData();
        roundTrip.deserializeNBT(data.serializeNBT());
        require(roundTrip.getRootAccumulatedNutrition() == 17,
                "Root accumulated nutrition should survive NBT round-trip");
        require(roundTrip.getRootAccumulationDeadline() == 1_234L,
                "Root accumulation deadline should survive NBT round-trip");
        require(roundTrip.getRootActiveUntil() == 5_678L,
                "Root active deadline should survive NBT round-trip");
        require(roundTrip.getRootCooldownUntil() == 9_012L,
                "Root cooldown should survive NBT round-trip");
        require(roundTrip.getRootReservedNutrition() == 18,
                "True Root reservation should survive NBT round-trip");
    }

    private static void validatesRootFixedWindowDoesNotSlide() {
        ShokugiData data = new ShokugiData();
        data.setSkillLevel(FoodHealingSkillIds.GUTS, 5);
        RootController.recordNutrition(data, 6, 100);
        require(data.getRootAccumulationDeadline() == 400, "first food must open a 300-tick fixed window");
        RootController.recordNutrition(data, 6, 390);
        require(data.getRootAccumulatedNutrition() == 12 && data.getRootAccumulationDeadline() == 400,
                "later food must not extend the fixed window");
        RootController.recordNutrition(data, 6, 680);
        require(data.getRootActiveUntil() == 0 && data.getRootAccumulatedNutrition() == 6
                        && data.getRootAccumulationDeadline() == 980,
                "expired nutrition must reset before the next food opens a new window");
        RootController.recordNutrition(data, 12, 980);
        require(data.getRootActiveUntil() == 1280 && data.getRootAccumulatedNutrition() == 0
                        && data.getRootAccumulationDeadline() == 0,
                "exact 15-second boundary must activate Root Lv5 and clear accumulation");
        RootController.advanceState(data, 1280);
        RootController.recordNutrition(data, 50, 1281);
        require(data.getRootAccumulatedNutrition() == 0 && data.getRootReservedNutrition() == 0,
                "single high-nutrition food must not carry excess into another activation");

        ShokugiData expired = new ShokugiData();
        expired.setSkillLevel(FoodHealingSkillIds.GUTS, 5);
        RootController.recordNutrition(expired, 1, 100);
        require(RootController.advanceState(expired, 401), "expiration must signal a changed state for client sync");
        require(expired.getRootAccumulatedNutrition() == 0 && expired.getRootAccumulationDeadline() == 0,
                "unsatisfied fixed window must expire without another food");
        require(!RootController.advanceState(expired, 402), "expiration must not signal another redundant update");
    }

    private static void validatesRootTimeline() {
        ShokugiData data = new ShokugiData();
        data.setSkillLevel(FoodHealingSkillIds.GUTS, 1);

        RootController.recordNutrition(data, 10, 100L);
        require(data.getRootAccumulatedNutrition() == 10,
                "Root should accumulate declared nutrition");
        require(data.getRootAccumulationDeadline() == 400L,
                "Root should open an exact 15-second accumulation window");

        RootController.recordNutrition(data, 8, 400L);
        require(data.getRootActiveUntil() == 440L,
                "nutrition at the exact window boundary should activate Root");
        require(data.getRootAccumulatedNutrition() == 0,
                "Root activation should reset cumulative nutrition without carry-over");

        RootController.recordNutrition(data, 18, 420L);
        require(data.getRootReservedNutrition() == 0,
                "normal Root must not reserve nutrition while active");
        require(RootController.advanceState(data, 440L),
                "Root should transition when its active deadline is reached");
        require(data.getRootActiveUntil() == 0L && data.getRootCooldownUntil() == 840L,
                "Root Lv1 should retain its 20-second post-active cooldown");

        RootController.recordNutrition(data, 18, 600L);
        require(data.getRootAccumulatedNutrition() == 0 && data.getRootActiveUntil() == 0L,
                "normal Root must not bank nutrition during cooldown");
        RootController.recordNutrition(data, 18, 840L);
        require(data.getRootActiveUntil() == 880L,
                "Root should be available at the exact cooldown boundary");
    }

    private static void validatesTrueRootReservation() {
        ShokugiData data = new ShokugiData();
        data.setSkillLevel(FoodHealingSkillIds.GUTS, 5);
        data.setSkillLevel(FoodHealingSkillIds.TRUE_GUTS, 1);

        RootController.recordNutrition(data, 18, 1_000L);
        require(data.getRootActiveUntil() == 1_300L,
                "Root Lv5 should activate for 15 seconds");
        RootController.recordNutrition(data, 10, 1_010L);
        RootController.recordNutrition(data, 20, 1_020L);
        require(data.getRootReservedNutrition() == 18,
                "True Root reservation must cap at exactly one activation");

        require(RootController.advanceState(data, 1_300L),
                "True Root should transition at the active deadline");
        require(data.getRootActiveUntil() == 1_600L,
                "a ready True Root reservation should reactivate immediately");
        require(data.getRootReservedNutrition() == 0,
                "True Root should consume and reset its one reserved activation");
    }

    private static void validatesAtomicSkillPointSpending() {
        ShokugiData data = new ShokugiData();
        data.setUnspentSkillPoints(5L);
        data.setSpentSkillPoints(Long.MAX_VALUE - 3L);

        require(!data.trySpendSkillPoints(4L), "spent-SP overflow must reject the transaction");
        require(data.getUnspentSkillPoints() == 5L, "failed transaction must not consume unspent SP");
        require(data.getSpentSkillPoints() == Long.MAX_VALUE - 3L, "failed transaction must not mutate spent SP");

        data.setSpentSkillPoints(10L);
        require(data.trySpendSkillPoints(4L), "valid transaction should succeed");
        require(data.getUnspentSkillPoints() == 1L, "successful transaction should consume exact SP");
        require(data.getSpentSkillPoints() == 14L, "successful transaction should audit spent SP");
    }

    private static void validatesFrozenSkillCostsAndPurchases() {
        long finiteCost = FoodHealingSkills.definitions().values().stream()
                .flatMapToLong(definition -> java.util.Arrays.stream(definition.levelCosts()))
                .sum();
        require(finiteCost == 2590L, "current finite-skill cost must be 2590 SP");

        ShokugiData data = new ShokugiData();
        data.setUnspentSkillPoints(3L);
        require(FoodHealingSkills.tryPurchase(data, FoodHealingSkillIds.FIRE_RESISTANCE)
                        == FoodHealingSkills.PurchaseResult.SUCCESS,
                "known skill purchase should succeed");
        require(data.getSkillLevel(FoodHealingSkillIds.FIRE_RESISTANCE) == 1,
                "purchase should grant exactly one skill level");
        require(data.getUnspentSkillPoints() == 2L && data.getSpentSkillPoints() == 1L,
                "purchase should atomically move SP to spent accounting");
        require(FoodHealingSkills.tryPurchase(data, FoodHealingSkillIds.FIRE_RESISTANCE)
                        == FoodHealingSkills.PurchaseResult.MAX_LEVEL,
                "finite skill must reject a duplicate purchase");
        data.setUnspentSkillPoints(10L);
        require(FoodHealingSkills.tryPurchase(data, FoodHealingSkillIds.SATISFACTION, 0)
                        == FoodHealingSkills.PurchaseResult.SUCCESS,
                "current skill purchase generation should succeed");
        long pointsAfterFirstLevel = data.getUnspentSkillPoints();
        require(FoodHealingSkills.tryPurchase(data, FoodHealingSkillIds.SATISFACTION, 0)
                        == FoodHealingSkills.PurchaseResult.STALE_REQUEST,
                "duplicate stale skill request must be rejected");
        require(data.getSkillLevel(FoodHealingSkillIds.SATISFACTION) == 1
                        && data.getUnspentSkillPoints() == pointsAfterFirstLevel,
                "stale skill request must not buy a second level or consume SP");
        long spentBeforeBlockedPurchase = data.getSpentSkillPoints();
        require(FoodHealingSkills.tryPurchase(data, FoodHealingSkillIds.PURIFICATION_MASTERY)
                        == FoodHealingSkills.PurchaseResult.PREREQUISITE_MISSING,
                "missing normal Purification must prevent purchase");
        require(data.getUnspentSkillPoints() == pointsAfterFirstLevel
                        && data.getSpentSkillPoints() == spentBeforeBlockedPurchase,
                "prerequisite rejection must not consume SP");
        require(FoodHealingSkills.tryPurchase(data, FoodHealingSkillIds.GATHERING)
                        == FoodHealingSkills.PurchaseResult.SUCCESS,
                "completed Gathering should be purchasable for 5 SP on fresh v3 data");
        require(data.getSkillLevel(FoodHealingSkillIds.GATHERING) == 1
                        && data.getUnspentSkillPoints() == pointsAfterFirstLevel - 5L,
                "Gathering purchase should grant one level and spend exactly 5 SP");

        data.setUnspentSkillPoints(110L);
        require(FoodHealingSkills.tryPurchase(data, FoodHealingSkillIds.UNBREAKING)
                        == FoodHealingSkills.PurchaseResult.SUCCESS,
                "completed Unbreaking should be purchasable for 10 SP on fresh v3 data");
        require(FoodHealingSkills.tryPurchase(data, FoodHealingSkillIds.PURSUIT)
                        == FoodHealingSkills.PurchaseResult.SUCCESS,
                "completed Pursuit should be purchasable for 100 SP on fresh v3 data");
        require(data.getUnspentSkillPoints() == 0L
                        && data.getSkillLevel(FoodHealingSkillIds.UNBREAKING) == 1
                        && data.getSkillLevel(FoodHealingSkillIds.PURSUIT) == 1,
                "Unbreaking/Pursuit purchases did not spend exactly 10/100 SP or grant one level");

        data.setUnspentSkillPoints(4L);
        require(FoodHealingSkills.tryPurchase(data, FoodHealingSkillIds.FOOD_PRODUCTION_MASTERY)
                        == FoodHealingSkills.PurchaseResult.SUCCESS,
                "completed Food Production Mastery should be purchasable for 4 SP");
        require(data.getSkillLevel(FoodHealingSkillIds.FOOD_PRODUCTION_MASTERY) == 1
                        && data.getUnspentSkillPoints() == 0L,
                "Food Production Mastery purchase should grant one level and spend exactly 4 SP");

        data.setUnspentSkillPoints(100L);
        long spentBeforePendingPurchase = data.getSpentSkillPoints();
        require(FoodHealingSkills.tryPurchase(data, FoodHealingSkillIds.FLIGHT)
                        == FoodHealingSkills.PurchaseResult.SUCCESS,
                "verified exact-provider Flight must be purchasable");
        require(data.getSkillLevel(FoodHealingSkillIds.FLIGHT) == 1
                        && data.getUnspentSkillPoints() == 98L
                        && data.getSpentSkillPoints() == spentBeforePendingPurchase + 2L
                        && !data.isSkillDisabled(FoodHealingSkillIds.FLIGHT),
                "Flight purchase must spend exactly 2 SP and default ON");

        data.setUnspentSkillPoints(5L);
        require(FoodHealingBaseStats.tryPurchase(data, FoodHealingBaseStatIds.BASE_DEFENSE)
                        == FoodHealingBaseStats.PurchaseResult.SUCCESS,
                "base stat purchase should succeed");
        require(data.getBaseStatPoints(FoodHealingBaseStatIds.BASE_DEFENSE) == 1L,
                "base stat purchase should increment canonical points");
        long pointsAfterBaseStat = data.getUnspentSkillPoints();
        require(FoodHealingBaseStats.tryPurchase(data, FoodHealingBaseStatIds.BASE_DEFENSE, 0L)
                        == FoodHealingBaseStats.PurchaseResult.STALE_REQUEST,
                "duplicate stale base-stat request must be rejected");
        require(data.getBaseStatPoints(FoodHealingBaseStatIds.BASE_DEFENSE) == 1L
                        && data.getUnspentSkillPoints() == pointsAfterBaseStat,
                "stale base-stat request must not consume SP");
    }

    private static void validatesDamageReductionMath() {
        require(close(FoodHealingBaseStats.damageRemaining(0L, 0L), 1.0D), "0 DR points should leave full damage");
        require(close(FoodHealingBaseStats.damageRemaining(1L, 0L), 0.99D), "1 DR point should leave 99%");
        require(close(FoodHealingBaseStats.damageRemaining(50L, 0L), 0.50D), "50 DR points should leave 50%");
        require(close(FoodHealingBaseStats.damageRemaining(99L, 0L), 0.01D), "99 DR points should leave 1%");
        require(close(FoodHealingBaseStats.damageRemaining(99L, 1L), 0.001D), "first post-99 point should leave 0.1%");
        require(FoodHealingBaseStats.damageRemaining(99L, Long.MAX_VALUE) > 0.0D,
                "extreme transcendence must not store exact immunity");
        require(FoodHealingBaseStats.multiplyPositiveDamage(1.0F, Double.MIN_NORMAL) > 0.0F,
                "positive damage must not underflow to exact zero");
        for (float amount : new float[]{Float.MIN_VALUE, Float.MIN_NORMAL, 1, Float.MAX_VALUE}) {
            for (double multiplier : new double[]{Double.MIN_VALUE, Double.MIN_NORMAL, 0.01, 1, Double.MAX_VALUE}) {
                float result = FoodHealingBaseStats.multiplyPositiveDamage(amount, multiplier);
                require(Float.isFinite(result) && result > 0, "positive finite inputs must remain finite and positive");
            }
            require(FoodHealingBaseStats.multiplyPositiveDamage(amount, 0) == 0,
                    "an explicitly zero multiplier must remain zero");
        }
        require(FoodHealingBaseStats.multiplyPositiveDamage(Float.POSITIVE_INFINITY, 1.0D) == Float.MAX_VALUE,
                "positive infinite damage should clamp instead of becoming immunity");
        require(FoodHealingBaseStats.multiplyPositiveDamage(1.0F, Double.POSITIVE_INFINITY) == Float.MAX_VALUE,
                "positive infinite multiplier should clamp instead of becoming immunity");
        require(FoodHealingBaseStats.multiplyPositiveDamage(Float.NaN, 1.0D) == 0.0F,
                "NaN damage must not propagate into the event pipeline");
    }

    private static void validatesRepeatableBaseStatPurchases() {
        require(FoodHealingBaseStats.cost(FoodHealingBaseStatIds.BASE_DEFENSE) == 5L,
                "Base Defense must cost 5 SP");
        require(FoodHealingBaseStats.cost(FoodHealingBaseStatIds.BASE_DAMAGE_REDUCTION_LINEAR) == 10L,
                "Base Damage Reduction must cost 10 SP per stage");
        require(FoodHealingBaseStats.cost(FoodHealingBaseStatIds.HIGH_DIFFICULTY_REDUCTION_LINEAR) == 10L,
                "High-Difficulty Damage Reduction must cost 10 SP per stage");
        require(FoodHealingBaseStats.cost(FoodHealingBaseStatIds.BASE_MAX_HEALTH) == 5L,
                "Base Max HP must cost 5 SP");
        require(FoodHealingBaseStats.cost(FoodHealingBaseStatIds.RECOVERY_MULTIPLIER) == 20L,
                "Food Healing Recovery must cost 20 SP");
        require(FoodHealingBaseStats.cost(FoodHealingBaseStatIds.BASE_OUTGOING_DAMAGE) == 20L,
                "Base Outgoing Damage must cost 20 SP");
        require(FoodHealingBaseStats.cost(FoodHealingBaseStatIds.TACZ_BASE_OUTGOING_DAMAGE) == 2L,
                "TaCZ Base Outgoing Damage must cost 2 SP");

        ShokugiData data = new ShokugiData();
        data.setUnspentSkillPoints(52L);
        require(FoodHealingBaseStats.tryPurchase(data, FoodHealingBaseStatIds.BASE_DEFENSE, 0L)
                        == FoodHealingBaseStats.PurchaseResult.SUCCESS,
                "Base Defense purchase should succeed");
        require(FoodHealingBaseStats.tryPurchase(data, FoodHealingBaseStatIds.BASE_MAX_HEALTH, 0L)
                        == FoodHealingBaseStats.PurchaseResult.SUCCESS,
                "Base Max HP purchase should succeed");
        require(FoodHealingBaseStats.tryPurchase(data, FoodHealingBaseStatIds.RECOVERY_MULTIPLIER, 0L)
                        == FoodHealingBaseStats.PurchaseResult.SUCCESS,
                "Food Healing Recovery purchase should succeed");
        require(FoodHealingBaseStats.tryPurchase(data, FoodHealingBaseStatIds.BASE_OUTGOING_DAMAGE, 0L)
                        == FoodHealingBaseStats.PurchaseResult.SUCCESS,
                "Base Outgoing Damage purchase should succeed");
        CompoundTag beforeMissingTacz = data.serializeNBT();
        require(FoodHealingBaseStats.tryPurchase(data, FoodHealingBaseStatIds.TACZ_BASE_OUTGOING_DAMAGE, 0L)
                        == FoodHealingBaseStats.PurchaseResult.OPTIONAL_MOD_MISSING
                        && beforeMissingTacz.equals(data.serializeNBT()),
                "absent TaCZ must reject without spending or altering canonical data");
        require(FoodHealingBaseStats.tryPurchase(data, FoodHealingBaseStatIds.TACZ_BASE_OUTGOING_DAMAGE, 0L,
                        modId -> modId.equals("tacz"))
                        == FoodHealingBaseStats.PurchaseResult.SUCCESS,
                "TaCZ Base Outgoing Damage purchase should succeed");
        require(data.getUnspentSkillPoints() == 0L && data.getSpentSkillPoints() == 52L,
                "repeatable base stats must consume and audit exactly 52 SP");
        require(data.getBaseStatPoints(FoodHealingBaseStatIds.BASE_DEFENSE) == 1L
                        && data.getBaseStatPoints(FoodHealingBaseStatIds.BASE_MAX_HEALTH) == 1L
                        && data.getBaseStatPoints(FoodHealingBaseStatIds.RECOVERY_MULTIPLIER) == 1L
                        && data.getBaseStatPoints(FoodHealingBaseStatIds.BASE_OUTGOING_DAMAGE) == 1L
                        && data.getBaseStatPoints(FoodHealingBaseStatIds.TACZ_BASE_OUTGOING_DAMAGE) == 1L,
                "each repeatable purchase must increment only its canonical point count");
        require(close(FoodHealingBaseStats.recoveryMultiplier(data), 1.25D),
                "one Recovery point must produce x1.25");
        require(close(FoodHealingBaseStats.outgoingDamageMultiplier(data), 1.10D),
                "one Base Outgoing point must produce x1.10");
        require(close(FoodHealingBaseStats.taczDamageMultiplier(data), 1.01D),
                "one TaCZ Base Outgoing point must produce x1.01");

        ShokugiData reduction = new ShokugiData();
        reduction.setBaseStatPoints(FoodHealingBaseStatIds.BASE_DAMAGE_REDUCTION_LINEAR, 98L);
        reduction.setUnspentSkillPoints(20L);
        require(FoodHealingBaseStats.tryPurchase(reduction,
                        FoodHealingBaseStatIds.BASE_DAMAGE_REDUCTION_LINEAR, 98L)
                        == FoodHealingBaseStats.PurchaseResult.SUCCESS,
                "the 99th reduction purchase should remain in the linear stage");
        require(reduction.getBaseStatPoints(FoodHealingBaseStatIds.BASE_DAMAGE_REDUCTION_LINEAR) == 99L
                        && reduction.getBaseStatPoints(
                        FoodHealingBaseStatIds.BASE_DAMAGE_REDUCTION_TRANSCENDENCE) == 0L,
                "the 99th reduction point must not enter transcendence early");
        require(FoodHealingBaseStats.tryPurchase(reduction,
                        FoodHealingBaseStatIds.BASE_DAMAGE_REDUCTION_LINEAR, 99L)
                        == FoodHealingBaseStats.PurchaseResult.SUCCESS,
                "the first post-99 purchase should succeed");
        require(reduction.getBaseStatPoints(FoodHealingBaseStatIds.BASE_DAMAGE_REDUCTION_LINEAR) == 99L
                        && reduction.getBaseStatPoints(
                        FoodHealingBaseStatIds.BASE_DAMAGE_REDUCTION_TRANSCENDENCE) == 1L,
                "post-99 reduction must use the integer transcendence stage");
        require(reduction.getUnspentSkillPoints() == 0L && reduction.getSpentSkillPoints() == 20L,
                "linear/transcendence purchases must each consume exactly 10 SP");

        reduction.setBaseStatPoints(FoodHealingBaseStatIds.BASE_DAMAGE_REDUCTION_TRANSCENDENCE,
                Long.MAX_VALUE);
        reduction.setUnspentSkillPoints(1L);
        long spentBeforeLimit = reduction.getSpentSkillPoints();
        require(FoodHealingBaseStats.tryPurchase(reduction,
                        FoodHealingBaseStatIds.BASE_DAMAGE_REDUCTION_LINEAR, Long.MAX_VALUE)
                        == FoodHealingBaseStats.PurchaseResult.NUMERIC_LIMIT,
                "a saturated transcendence counter must reject another purchase");
        require(reduction.getUnspentSkillPoints() == 1L
                        && reduction.getSpentSkillPoints() == spentBeforeLimit,
                "numeric-limit rejection must not consume SP");

        ShokugiData insufficient = new ShokugiData();
        insufficient.setUnspentSkillPoints(19L);
        require(FoodHealingBaseStats.tryPurchase(insufficient,
                        FoodHealingBaseStatIds.RECOVERY_MULTIPLIER, 0L)
                        == FoodHealingBaseStats.PurchaseResult.INSUFFICIENT_SP,
                "Recovery purchase must reject fewer than 20 SP");
        require(insufficient.getUnspentSkillPoints() == 19L
                        && insufficient.getSpentSkillPoints() == 0L
                        && insufficient.getBaseStatPoints(FoodHealingBaseStatIds.RECOVERY_MULTIPLIER) == 0L,
                "insufficient-SP rejection must leave the transaction unchanged");
    }

    private static void validatesSaturatedPurchaseGeneration() {
        ShokugiData data = new ShokugiData();
        data.setBaseStatPoints(FoodHealingBaseStatIds.BASE_DAMAGE_REDUCTION_LINEAR, 99);
        data.setBaseStatPoints(FoodHealingBaseStatIds.BASE_DAMAGE_REDUCTION_TRANSCENDENCE, Long.MAX_VALUE - 100);
        data.setUnspentSkillPoints(10);
        require(FoodHealingBaseStats.tryPurchase(data, FoodHealingBaseStatIds.BASE_DAMAGE_REDUCTION_LINEAR,
                Long.MAX_VALUE - 1) == FoodHealingBaseStats.PurchaseResult.SUCCESS,
                "last representable purchase generation should succeed");
        CompoundTag before = data.serializeNBT();
        require(FoodHealingBaseStats.tryPurchase(data, FoodHealingBaseStatIds.BASE_DAMAGE_REDUCTION_LINEAR,
                Long.MAX_VALUE) == FoodHealingBaseStats.PurchaseResult.NUMERIC_LIMIT,
                "saturated aggregate generation must reject further purchases before spending SP");
        require(before.equals(data.serializeNBT()), "rejected generation must leave all canonical data unchanged");
    }

    private static void validatesDataDrivenSkillPrerequisites() {
        FoodHealingSkills.SkillDefinition trueRoot = FoodHealingSkills.definitions()
                .get(FoodHealingSkillIds.TRUE_GUTS);
        require(trueRoot.requirements().equals(java.util.List.of(
                        new FoodHealingSkills.SkillRequirement(FoodHealingSkillIds.GUTS, 5))),
                "True Root must retain its frozen Root Lv5 prerequisite");

        FoodHealingSkills.SkillDefinition purificationMastery = FoodHealingSkills.definitions()
                .get(FoodHealingSkillIds.PURIFICATION_MASTERY);
        require(!purificationMastery.openPrerequisites(),
                "Purification Mastery prerequisites are locked");
        require(purificationMastery.requirements().equals(java.util.List.of(
                        new FoodHealingSkills.SkillRequirement(FoodHealingSkillIds.PURIFICATION, 1))),
                "Purification Mastery must record only its known mandatory prerequisite");

        FoodHealingSkills.SkillDefinition truthMastery = FoodHealingSkills.definitions()
                .get(FoodHealingSkillIds.TRUTH_MASTERY);
        require(!truthMastery.openPrerequisites(),
                "Truth Mastery prerequisites are locked");
        require(truthMastery.requirements().equals(java.util.List.of(
                        new FoodHealingSkills.SkillRequirement(FoodHealingSkillIds.PURIFICATION_MASTERY, 1))),
                "Truth Mastery must record only its known mandatory prerequisite");
    }

    private static void validatesFoodDiversityNumericSafety() {
        CompoundTag negative = new CompoundTag();
        negative.putInt("MaxHealthBonus", -1);
        FoodDiversityData data = new FoodDiversityData();
        data.deserializeNBT(negative);
        require(data.getMaxHealthBonus() == 0,
                "negative Food Diversity max-health data must clamp to zero");

        CompoundTag nearLimit = new CompoundTag();
        nearLimit.putInt("MaxHealthBonus", Integer.MAX_VALUE - 1);
        data.deserializeNBT(nearLimit);
        data.addMaxHealthBonus(1000);
        require(data.getMaxHealthBonus() == Integer.MAX_VALUE,
                "Food Diversity max-health addition must saturate instead of overflowing");
        data.addMaxHealthBonus(-1);
        require(data.getMaxHealthBonus() == Integer.MAX_VALUE,
                "negative Food Diversity additions must not reduce canonical progression");
    }

    private static void validatesFoodDiversitySyncSnapshot() {
        CompoundTag source = new CompoundTag();
        source.putInt("MaxHealthBonus", 6);
        FoodDiversitySyncPacket packet = new FoodDiversitySyncPacket(source);
        source.putInt("MaxHealthBonus", 99);

        FriendlyByteBuf encoded = new FriendlyByteBuf(Unpooled.buffer());
        try {
            packet.encode(encoded);
            CompoundTag snapshot = encoded.readNbt();
            require(snapshot != null && snapshot.getInt("MaxHealthBonus") == 6,
                    "Food Diversity sync must retain a defensive snapshot of canonical data");
        } finally {
            encoded.release();
        }

        FriendlyByteBuf nullableWire = new FriendlyByteBuf(Unpooled.buffer());
        FriendlyByteBuf normalizedWire = new FriendlyByteBuf(Unpooled.buffer());
        try {
            nullableWire.writeNbt(null);
            FoodDiversitySyncPacket decoded = new FoodDiversitySyncPacket(nullableWire);
            decoded.encode(normalizedWire);
            require(normalizedWire.readNbt() != null,
                    "a null Food Diversity payload must normalize to empty NBT");
        } finally {
            nullableWire.release();
            normalizedWire.release();
        }
    }

    private static void validatesFoodHungerReconciliation() {
        FoodHealingTransactions.HungerReconciliation foodOnly =
                FoodHealingTransactions.reconcileHungerIncrease(4, 4);
        require(foodOnly.nonFoodUnits() == 0 && foodOnly.remainingReservation() == 0,
                "food hunger increase must not heal again through the generic path");

        FoodHealingTransactions.HungerReconciliation mixed =
                FoodHealingTransactions.reconcileHungerIncrease(6, 4);
        require(mixed.nonFoodUnits() == 2 && mixed.remainingReservation() == 0,
                "mixed food/non-food increase should expose only non-food units");

        FoodHealingTransactions.HungerReconciliation partial =
                FoodHealingTransactions.reconcileHungerIncrease(2, 4);
        require(partial.nonFoodUnits() == 0 && partial.remainingReservation() == 0,
                "masked completed food gains must not reserve credit for a later observation");

        FoodHealingTransactions.HungerReconciliation noReservation =
                FoodHealingTransactions.reconcileHungerIncrease(3, 0);
        require(noReservation.nonFoodUnits() == 3 && noReservation.remainingReservation() == 0,
                "non-food increase without a reservation should heal exactly once");
    }

    private static boolean close(double actual, double expected) {
        return Math.abs(actual - expected) < 1.0E-12D;
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
