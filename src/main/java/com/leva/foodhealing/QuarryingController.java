package com.leva.foodhealing;

import com.leva.foodhealing.capability.ShokugiProvider;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/** Only the successful native player-harvest call may authorize a bonus. */
public final class QuarryingController {
    private static final ThreadLocal<Harvest> HARVEST = new ThreadLocal<>();
    private QuarryingController() { }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void breakSpeed(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();
        if (!event.getState().is(Blocks.DEEPSLATE) || player instanceof FakePlayer
                || event.getPosition().isEmpty() || !enabled(player)) return;
        BlockPos pos = event.getPosition().get();
        float stone = Blocks.STONE.defaultBlockState().getDestroySpeed(player.level(), pos);
        float deep = event.getState().getDestroySpeed(player.level(), pos);
        float adjusted = event.getNewSpeed() * (deep / stone);
        if (stone > 0 && deep > 0 && Float.isFinite(adjusted) && adjusted > 0)
            event.setNewSpeed(adjusted);
    }

    private static boolean enabled(Player player) {
        return player.getCapability(ShokugiProvider.SHOKUGI_CAPA)
                .map(data -> FoodHealingSkills.isCanonicalSkillEnabled(data, FoodHealingSkillIds.QUARRYING))
                .orElse(false);
    }

    public static void harvest(Player player, BlockPos pos, BlockState state, Runnable nativeHarvest) {
        Harvest previous = HARVEST.get();
        // Do not let nested, unrelated loot calls inherit an outer authorization.
        HARVEST.remove();
        if (player instanceof ServerPlayer server && !(player instanceof FakePlayer)
                && !player.isCreative() && !player.isSpectator() && target(state) && enabled(player))
            HARVEST.set(new Harvest(server, pos.immutable(), state));
        try { nativeHarvest.run(); }
        finally { if (previous == null) HARVEST.remove(); else HARVEST.set(previous); }
    }

    public static ObjectArrayList<ItemStack> addBonus(ObjectArrayList<ItemStack> loot, LootContext context) {
        Harvest harvest = HARVEST.get();
        if (harvest == null || harvest.consumed || context.getLevel() != harvest.player.serverLevel()
                || context.getParamOrNull(LootContextParams.THIS_ENTITY) != harvest.player
                || context.getParamOrNull(LootContextParams.BLOCK_STATE) != harvest.state
                || context.hasParam(LootContextParams.EXPLOSION_RADIUS)) return loot;
        Vec3 origin = context.getParamOrNull(LootContextParams.ORIGIN);
        if (origin == null || !BlockPos.containing(origin).equals(harvest.pos) || !enabled(harvest.player)) return loot;
        harvest.consumed = true;
        int selection = selectBonus(context.getRandom().nextDouble());
        if (selection == 0) return loot;
        int level = harvest.player.getCapability(ShokugiProvider.SHOKUGI_CAPA)
                .map(d -> FoodHealingSkills.getEnabledLevel(d, FoodHealingSkillIds.GATHERING)).orElse(0);
        int count = level >= 1 && level <= 3 ? level * 2 : 1;
        ObjectArrayList<ItemStack> result = new ObjectArrayList<>(loot);
        result.add(new ItemStack(selection == 1 ? Items.IRON_ORE : Items.COPPER_ORE, count));
        return result;
    }

    public static int selectBonus(double roll) {
        if (!Double.isFinite(roll) || roll < 0 || roll >= 0.01D) return 0;
        return roll < 0.005D ? 1 : 2;
    }

    private static boolean target(BlockState state) { return state.is(Blocks.STONE) || state.is(Blocks.DEEPSLATE); }
    private static final class Harvest {
        final ServerPlayer player; final BlockPos pos; final BlockState state; boolean consumed;
        Harvest(ServerPlayer player, BlockPos pos, BlockState state) { this.player=player; this.pos=pos; this.state=state; }
    }
}
