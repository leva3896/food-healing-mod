package com.example.examplemod;

import com.example.examplemod.client.HealthDisplayOverlay;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

/**
 * 満腹度回復連動ヒーリングMOD
 * 食事で回復した満腹度に応じて体力が回復する
 */
@Mod(FoodHealingMod.MODID)
public class FoodHealingMod {
    public static final String MODID = "foodhealing";
    private static final Logger LOGGER = LogUtils.getLogger();

    // アイテム登録用
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister
            .create(Registries.CREATIVE_MODE_TAB, MODID);

    // テスト用超強力食料（満腹度50回復）
    public static final RegistryObject<Item> SUPER_FOOD = ITEMS.register("super_food",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(50)
                    .saturationMod(2.0f)
                    .alwaysEat()
                    .build())));

    // 普通のテスト食料（満腹度25回復、ボーナス効果なし確認用）
    public static final RegistryObject<Item> NORMAL_TEST_FOOD = ITEMS.register("normal_test_food",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(25)
                    .saturationMod(1.5f)
                    .alwaysEat()
                    .build())));

    // クリエイティブタブ
    public static final RegistryObject<CreativeModeTab> FOOD_HEALING_TAB = CREATIVE_MODE_TABS.register(
            "food_healing_tab",
            () -> CreativeModeTab.builder()
                    .withTabsBefore(CreativeModeTabs.FOOD_AND_DRINKS)
                    .title(net.minecraft.network.chat.Component.literal("Food Healing"))
                    .icon(() -> SUPER_FOOD.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        output.accept(SUPER_FOOD.get());
                        output.accept(NORMAL_TEST_FOOD.get());
                    }).build());

    public FoodHealingMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // コンフィグを登録
        FoodHealingConfig.register();

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);

        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        // 共通イベントハンドラーを登録
        // FoodHealingHandler: 食事時にHP回復＋ボーナス効果を付与
        MinecraftForge.EVENT_BUS.register(FoodHealingHandler.class);
        // HungerChangeHandler は FoodHealingHandler と重複するため無効化
        // MinecraftForge.EVENT_BUS.register(HungerChangeHandler.class);
        MinecraftForge.EVENT_BUS.register(AlwaysEatHandler.class);
        MinecraftForge.EVENT_BUS.register(FoodDiversityHandler.class);

        // クライアントサイドのみ：体力数値表示オーバーレイを登録
        if (FMLEnvironment.dist == Dist.CLIENT) {
            MinecraftForge.EVENT_BUS.register(HealthDisplayOverlay.class);
        }

        LOGGER.info("[FoodHealing] MOD initialized!");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // 最大体力の上限をコンフィグ値に拡張（デフォルト: 100万, 最大: 1兆）
        event.enqueueWork(() -> {
            try {
                double maxHealthCap = FoodHealingConfig.COMMON.maxHealthCap.get();
                RangedAttribute maxHealth = (RangedAttribute) Attributes.MAX_HEALTH;
                // リフレクションでmaxValueフィールドにアクセス
                java.lang.reflect.Field maxValueField = RangedAttribute.class.getDeclaredField("maxValue");
                maxValueField.setAccessible(true);
                maxValueField.setDouble(maxHealth, maxHealthCap);
                LOGGER.info("[FoodHealing] Max health cap extended to " + String.format("%,.0f", maxHealthCap) + "!");
            } catch (Exception e) {
                LOGGER.error("[FoodHealing] Failed to extend max health cap: " + e.getMessage());
            }
        });
        LOGGER.info("[FoodHealing] Common setup complete!");
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        LOGGER.info("[FoodHealing] Client setup complete - HP display overlay enabled!");
    }
}
