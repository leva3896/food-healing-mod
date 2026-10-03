package com.leva.foodhealing.mixin;

import com.leva.foodhealing.QuarryingController;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayerGameMode.class)
public abstract class QuarryingHarvestMixin {
    // This call exists only after the Forge harvest check AND successful native removal.
    @Redirect(method = "destroyBlock", at = @At(value = "INVOKE", target =
            "Lnet/minecraft/world/level/block/Block;playerDestroy(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/item/ItemStack;)V"))
    private void foodhealing$normalHarvest(Block block, Level level, Player player, BlockPos pos,
                                          BlockState state, BlockEntity blockEntity, ItemStack tool) {
        QuarryingController.harvest(player, pos, state,
                () -> block.playerDestroy(level, player, pos, state, blockEntity, tool));
    }
}
