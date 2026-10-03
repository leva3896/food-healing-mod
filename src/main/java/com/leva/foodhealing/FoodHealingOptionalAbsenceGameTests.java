package com.leva.foodhealing;

import com.leva.foodhealing.capability.IShokugiData;
import com.leva.foodhealing.capability.ShokugiProvider;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestAssertException;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(FoodHealingMod.MODID)
@PrefixGameTestTemplate(false)
public final class FoodHealingOptionalAbsenceGameTests {
    private static final String EMPTY_TEMPLATE = "empty";

    private FoodHealingOptionalAbsenceGameTests() {
    }

    @GameTest(template = EMPTY_TEMPLATE)
    public static void ammoPurchaseRequiresSupportedReadyAdapter(GameTestHelper helper) {
        Player player = helper.makeMockSurvivalPlayer();
        IShokugiData data = shokugi(player);
        data.setUnspentSkillPoints(50L);
        data.setSpentSkillPoints(7L);

        FoodHealingSkills.PurchaseResult result = FoodHealingSkills.tryPurchase(
                data, FoodHealingSkillIds.TACZ_AMMO_CONSERVATION, 0);

        boolean ready = com.leva.foodhealing.compat.TaczAmmoCompatibility.ready();
        FoodHealingSkills.PurchaseResult expected = ready ? FoodHealingSkills.PurchaseResult.SUCCESS
                : ModList.get().isLoaded("tacz")
                ? FoodHealingSkills.PurchaseResult.IMPLEMENTATION_PENDING
                : FoodHealingSkills.PurchaseResult.OPTIONAL_MOD_MISSING;
        helper.assertTrue(result == expected,
                "Ammo Conservation readiness gate returned unexpected result: " + result);
        helper.assertTrue(data.getSkillLevel(FoodHealingSkillIds.TACZ_AMMO_CONSERVATION) == (ready ? 1 : 0),
                "readiness gate granted wrong level");
        helper.assertTrue(data.getUnspentSkillPoints() == (ready ? 0L : 50L), "readiness gate spent wrong unspent SP");
        helper.assertTrue(data.getSpentSkillPoints() == (ready ? 57L : 7L), "readiness gate changed wrong spent SP");
        helper.succeed();
    }

    @GameTest(template = EMPTY_TEMPLATE)
    public static void missingDifficultyModsRejectHighDifficultyStatWithoutSpLoss(GameTestHelper helper) {
        helper.assertTrue(!ModList.get().isLoaded("l2hostility")
                        && !ModList.get().isLoaded("autoleveling"),
                "high-difficulty absence test requires L2 Hostility and Auto Leveling to be absent");

        Player player = helper.makeMockSurvivalPlayer();
        IShokugiData data = shokugi(player);
        data.setUnspentSkillPoints(25L);
        data.setSpentSkillPoints(9L);

        FoodHealingBaseStats.PurchaseResult result = FoodHealingBaseStats.tryPurchase(
                data, FoodHealingBaseStatIds.HIGH_DIFFICULTY_REDUCTION_LINEAR, 0L);

        helper.assertTrue(result == FoodHealingBaseStats.PurchaseResult.OPTIONAL_MOD_MISSING,
                "missing high-difficulty mods did not reject the gated base stat");
        helper.assertTrue(data.getBaseStatPoints(
                        FoodHealingBaseStatIds.HIGH_DIFFICULTY_REDUCTION_LINEAR) == 0L,
                "missing-mod purchase rejection changed high-difficulty reduction points");
        helper.assertTrue(data.getUnspentSkillPoints() == 25L,
                "missing-mod high-difficulty purchase rejection consumed unspent SP");
        helper.assertTrue(data.getSpentSkillPoints() == 9L,
                "missing-mod high-difficulty purchase rejection changed spent-SP accounting");
        helper.succeed();
    }

    private static IShokugiData shokugi(Player player) {
        return player.getCapability(ShokugiProvider.SHOKUGI_CAPA)
                .orElseThrow(() -> new GameTestAssertException("Shokugi capability is missing"));
    }
}
