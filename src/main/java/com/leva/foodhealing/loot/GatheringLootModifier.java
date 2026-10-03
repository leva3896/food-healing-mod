package com.leva.foodhealing.loot;

import com.leva.foodhealing.FoodHealingSkillIds;
import com.leva.foodhealing.FoodHealingSkills;
import com.leva.foodhealing.capability.ShokugiProvider;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CocoaBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

public final class GatheringLootModifier extends LootModifier {
    public static final Codec<GatheringLootModifier> CODEC = RecordCodecBuilder.create(instance ->
            codecStart(instance).apply(instance, GatheringLootModifier::new));
    private static final TagKey<Block> FORGE_ORES = BlockTags.create(new ResourceLocation("forge", "ores"));

    public GatheringLootModifier(LootItemCondition[] conditions) {
        super(conditions);
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot,
                                                           LootContext context) {
        return com.leva.foodhealing.QuarryingController.addBonus(applyGathering(generatedLoot, context), context);
    }

    private ObjectArrayList<ItemStack> applyGathering(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        BlockState state = context.getParamOrNull(LootContextParams.BLOCK_STATE);
        if (state == null || !isGatheringTarget(state)) {
            return generatedLoot;
        }
        if (!(context.getParamOrNull(LootContextParams.THIS_ENTITY) instanceof Player player)) {
            return generatedLoot;
        }

        int skillLevel = player.getCapability(ShokugiProvider.SHOKUGI_CAPA)
                .map(data -> FoodHealingSkills.getEnabledLevel(data, FoodHealingSkillIds.GATHERING))
                .orElse(0);
        if (skillLevel <= 0) {
            return generatedLoot;
        }

        int multiplier = skillLevel >= 3 ? 6 : skillLevel == 2 ? 4 : 2;
        ObjectArrayList<ItemStack> multiplied = new ObjectArrayList<>();
        for (ItemStack stack : generatedLoot) {
            addMultipliedStacks(multiplied, stack, multiplier);
        }
        return multiplied;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }

    private static boolean isGatheringTarget(BlockState state) {
        Block block = state.getBlock();
        return state.is(FORGE_ORES)
                || block instanceof DropExperienceBlock
                || block instanceof CropBlock
                || block instanceof NetherWartBlock
                || block instanceof CocoaBlock
                || block instanceof SweetBerryBushBlock
                || state.is(Blocks.GLOWSTONE)
                || state.is(Blocks.CLAY)
                || state.is(Blocks.MELON)
                || state.is(Blocks.PUMPKIN);
    }

    private static void addMultipliedStacks(ObjectArrayList<ItemStack> output, ItemStack source, int multiplier) {
        if (source.isEmpty()) {
            return;
        }
        long remaining = (long) source.getCount() * multiplier;
        while (remaining > 0L) {
            int count = (int) Math.min(remaining, source.getMaxStackSize());
            ItemStack copy = source.copy();
            copy.setCount(count);
            output.add(copy);
            remaining -= count;
        }
    }
}
