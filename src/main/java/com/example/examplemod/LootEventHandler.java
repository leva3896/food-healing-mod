package com.example.examplemod;

import com.example.examplemod.capability.ShokugiProvider;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = FoodHealingMod.MODID)
public class LootEventHandler {

    // Lv9: 屠殺の心得 (Secret of Slaughter)
    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        if (event.getSource().getEntity() instanceof Player player) {
            // 対象がモンスター（敵対的）ではない生物かどうか（イカ、コウモリ、村人等も含める広域設定）
            if (event.getEntity().getType().getCategory() != net.minecraft.world.entity.MobCategory.MONSTER) {
                player.getCapability(ShokugiProvider.SHOKUGI_CAPA).ifPresent(cap -> {
                    if (cap.getLevel() >= 9 && !cap.isSkillDisabled("屠殺")) {
                        // ConcurrentModificationExceptionを防ぐため、追加分のエンティティを一旦別リストに退避
                        java.util.List<ItemEntity> extraDrops = new java.util.ArrayList<>();
                        
                        // ドロップアイテムのアイテムスタック量を一律3倍にする
                        // (Lootingエンチャントによる増幅の後で適用されるため完全重複する)
                        event.getDrops().forEach(drop -> {
                            ItemStack stack = drop.getItem();
                            int newCount = stack.getCount() * 3;
                            // 最大スタックサイズ(64)を超えないように制限しながら設定、超えるぶんは無視させない為に増殖
                            if (newCount <= stack.getMaxStackSize()) {
                                stack.setCount(newCount);
                                drop.setItem(stack);
                            } else {
                                stack.setCount(stack.getMaxStackSize());
                                drop.setItem(stack);
                                
                                int remaining = newCount - stack.getMaxStackSize();
                                while (remaining > 0) {
                                    int spawnCount = Math.min(remaining, stack.getMaxStackSize());
                                    ItemStack extraStack = stack.copy();
                                    extraStack.setCount(spawnCount);
                                    extraDrops.add(new ItemEntity(drop.level(), drop.getX(), drop.getY(), drop.getZ(), extraStack));
                                    remaining -= spawnCount;
                                }
                            }
                        });
                        
                        // 退避しておいた追加エンティティを安全にオリジナルのリストに追加
                        event.getDrops().addAll(extraDrops);
                    }
                });
            }
        }
    }

    // Lv14-16: 採取の心得 (Secret of Gathering)
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (player == null || player.level().isClientSide() || !(player.level() instanceof ServerLevel serverLevel)) return;

        player.getCapability(ShokugiProvider.SHOKUGI_CAPA).ifPresent(cap -> {
            int level = cap.getLevel();
            if (level >= 14 && !cap.isSkillDisabled("採取")) {
                // 倍率: Lv16=6倍, Lv15=4倍, Lv14=2倍
                int multiplier = level >= 16 ? 6 : (level == 15 ? 4 : 2);

                BlockState state = event.getState();
                Block block = state.getBlock();
                String name = net.minecraftforge.registries.ForgeRegistries.BLOCKS.getKey(block).getPath();

                // 鉱石、作物、特例(グロウストーン、粘土など) の判定
                boolean isOre = name.contains("ore") || block instanceof net.minecraft.world.level.block.DropExperienceBlock;
                boolean isCrop = block instanceof net.minecraft.world.level.block.CropBlock
                        || block instanceof net.minecraft.world.level.block.NetherWartBlock
                        || block instanceof net.minecraft.world.level.block.CocoaBlock
                        || block instanceof net.minecraft.world.level.block.SweetBerryBushBlock;
                boolean isException = name.contains("glowstone") || name.contains("clay") || name.contains("melon") || name.contains("pumpkin");

                if (isOre || isCrop || isException) {
                    // バニラでドロップする予定のアイテム（幸運エンチャント処理後のリスト）をLootTableを回して取得
                    LootParams.Builder builder = new LootParams.Builder(serverLevel)
                            .withParameter(LootContextParams.ORIGIN, net.minecraft.world.phys.Vec3.atCenterOf(event.getPos()))
                            .withParameter(LootContextParams.TOOL, player.getMainHandItem())
                            .withParameter(LootContextParams.THIS_ENTITY, player)
                            .withParameter(LootContextParams.BLOCK_STATE, state);

                    List<ItemStack> drops = state.getDrops(builder);

                    if (!drops.isEmpty()) {
                        // バニラのBreakEventはそのまま継続し1倍分を落とすため、
                        // ここでは「(倍率 - 1) 倍」分のアイテムを空の空間に追加スポーンさせる
                        int extraMultiplier = multiplier - 1;
                        if (extraMultiplier > 0) {
                            for (ItemStack drop : drops) {
                                int extraAmount = drop.getCount() * extraMultiplier;
                                while (extraAmount > 0) {
                                    int spawnCount = Math.min(extraAmount, drop.getMaxStackSize());
                                    ItemStack extraStack = drop.copy();
                                    extraStack.setCount(spawnCount);
                                    
                                    ItemEntity extraEntity = new ItemEntity(
                                            player.level(),
                                            event.getPos().getX() + 0.5,
                                            event.getPos().getY() + 0.5,
                                            event.getPos().getZ() + 0.5,
                                            extraStack
                                    );
                                    player.level().addFreshEntity(extraEntity);
                                    extraAmount -= spawnCount;
                                }
                            }
                        }
                    }
                }
            }
        });
    }
}
