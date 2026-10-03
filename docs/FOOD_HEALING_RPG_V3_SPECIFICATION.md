# Food Healing RPG v3 Specification

| Metadata | Value |
|---|---|
| Document | Food Healing RPG v3 Specification |
| Specification Version | 3.0.0 |
| Applies To | Food Healing RPG v3.x |
| Minecraft | 1.20.1 |
| Loader | Forge |
| MOD ID | `foodhealing` |
| Schema Version | **5** |
| Network Protocol | **7** |
| Last Updated | 2026-10-03 11:19 JST |
| Status | **Formal v3.0.0 Release Specification** |

本書はv3.0.0の最終実装と利用者LOCKを統合した、v3.x保守の単一の仕様正本である。
設計目標、実装、検証、未対応を区別する。過去の計画にある「次」「STOP」「NOT RUN」、旧価格・旧自動取得を現行仕様として復活させない。
配布物・commit・RC判断の受領記録は[Release Receipt](../release/v3.0.0/RELEASE_RECEIPT.md)、試験の時系列は[TEST_PLAN](TEST_PLAN.md)と[共通計画](MASTERY_IMPLEMENTATION_PREPARATION.md)、現在の完了状態は[CODEX_STATUS](CODEX_STATUS.md#現在の要約)に置く。

## 1. 製品と適用範囲

- 正式表示名 **Food Healing RPG**、版 **3.0.0**、作者Leva、license **All Rights Reserved**。package `com.leva.foodhealing`、MOD IDを変更しない。
- 正式配布名 **Food Healing RPG v3.0.0.jar**。Food Healing v2.2.5を置き換える。旧版と新旧2本を同時に入れない。
- Minecraft1.20.1、Java17、Forge。ビルド基準は47.2.0、metadataはForge `[47,)` / MC `[1.20.1,1.21)`。これは全Forge版の試験済み宣言ではない。
- 最終実client・外部MOD統合の基準は **Forge47.4.0**。Trial1.4.9やFE shaderの受入を47.2.0へ転用しない。
- scopeは既存LOCKに限定。新要素・新互換・新UIを仕様から推測して追加しない。
- 正式配布Jarは **372,242 bytes / 286 entries / SHA-256 `8B8A31FA308CD24C4A139D65FEF5818F1A2DABD8587A238D93974F787FFE10DD`**。
  Queue #8検証済みJarのコピーであり、Queue #9で再compile/repackしない。

## 2. 用語・単位・表示

- 日本語は **食技**（しょくぎ / syokugi）、英語は **Shokugi**。旧「食義」は履歴・legacy aliasのみ。
- Java/NBT/Config/翻訳キーの`Shokugi`は維持する。表示変更を保存キー変更にしない。
- HPはハートの数ではない（通常20HP＝10ハート）。NutritionはFoodPropertiesの宣言栄養値、Food Levelは現在の満腹度。
- countはNutrition/Food-Level単位の部分進捗。食事回数・saturation・SPと混同しない。
- 1秒は通常20server ticks。停止・pauseで実時間とgameTimeが異なるため、Root期限はserver gameTimeで判定する。
- Pは浄化の極意、Tは真実の極意。Pの効果条件には通常浄化も必要。試験記録のP/M/T表記では通常浄化/P極意/T極意を指す場合があるため、本文では名称を明記する。

## 3. Architectureとserver権威

| 層 | 正本・責務 | 書換え禁止の境界 |
|---|---|---|
| 永続進行 | `ShokugiData`、`FoodDiversityData` | 表示名、現在Attribute値をcanonicalにしない |
| 購入/強化 | `FoodHealingSkills`、`FoodHealingBaseStats` | client提示の費用/SP/levelを信用しない |
| 効果 | 各Controller、Forge event、限定Mixin | 他MODのstate全体を置換しない |
| optional | mod/version gate後のAdapter | 不在MODのclassを無条件ロードしない |
| network/UI | server canonicalの表示と本人の操作要求 | client独自にSP・取得・HPを確定しない |
| lifecycle | Clone/login/respawn/dimension/save | 旧sessionの飛行/TimeStop権限を永続化しない |

serverは取引・効果・予約を確定し、clientは入力と同期表示を行う。
Food Healing所有の固定UUID modifierだけを冪等に再構築する。foreign modifier・capability・ability・effectを一括削除しない。
preferred mechanismはForge/vanilla→公開API→所有データ→Adapter→必要最小Mixinの順。
global Attribute private上限Reflection、全damage/全deathの一律cancel、全tick速度ゼロ化は禁止。

## 4. 食技・SP・数値安全性

既定 **2000単位＝食技Lv+1＝未使用SP+1**。食技Lv、count、未使用/使用済SP、基礎強化回数は`long`。
上限なしというゲーム上の表現は無限精度を意味しない。`Long.MAX_VALUE`と浮動小数の限界を守る。

正常状態`0≤C<T`、正の入力`U`に対し、checked arithmeticで:

```text
total = C + U
gained = total / T
count = total % T
level = oldLevel + gained
unspentSP = oldUnspentSP + gained
```

負数、threshold≤0、範囲外count、pending、加算overflowは進行更新を全拒否し、途中だけ更新しない。
食事/回復/Root/Diversityと進行拒否を区別し、拒否を修復SPや自動moduloに変換しない。
thresholdを既存count以下へ下げた場合も勝手に余りへ正規化しない。

正常food完了は宣言Nutritionを1回加算。Nutrition8・満腹度20でもcount+8。満腹度12→20も+8であり+16にしない。
非foodのFood Level10→15は+5。saturationだけ、減少、無変化、読込だけでは0。
`FoodData.eat(Item,ItemStack,LivingEntity)`内の原因scopeと`eat(int,float)`/`setFoodLevel`前後差で区別する。
tick近接推測や後日の差引予約は使わない。Satisfactionで消費0でも成功した食事は1回計上する。
特殊auto-feed・private field直接書込は、独立統合を通していなければ保証しない。

## 5. 回復・満腹時食事・Food Diversity

食事HP回復＝**宣言Nutrition × `general.healMultiplier`（既定2.0） × 回復倍率**。
非food正deltaも同じ換算を1回行う。native `heal`で現在最大HPまで。非foodはRootの栄養蓄積へ入れない。
通常`Item.use`の`canEat`で満腹制限だけを緩和し、満腹時も食べられる。独自use/menuを全て書き換える機能ではない。
早食いはfoodのみuse durationを整数半分（最低1tick）。非food使用・射撃を短縮しない。

Food Diversityはitem registry IDを生涯集合へ記録し、初めての食品だけが進む。
既定 **新しい5種ごとに最大HP+2**。達成すると今回分集合だけclearし、生涯集合は保持。
Configの`foodsRequired`/`healthIncrease`に従う。NBT差・同じ食品の再食を新種にしない。
最大HPbonusは非負intで飽和し、Food Healing所有modifierとして再構築。clone/save/reloadで履歴・bonusを維持。

## 6. 基礎強化7系統

以下の「1回」は1SPではなく、表の価格を支払った1購入。価格順は **5/10/10/5/20/20/2**。

| 正式購入ID | 1回のSP | 効果 | 購入制限・保存 |
|---|---:|---|---|
| `foodhealing:base_defense` | 5 | Armor +5 | 回数long、所有UUIDのみ |
| `foodhealing:base_damage_reduction_linear` | 10 | 通常軽減+1 percentage point、99%後は超越 | linear/transcendence別long |
| `foodhealing:high_difficulty_reduction_linear` | 10 | 高難度軽減+1 percentage point、99%後は超越 | L2 HostilityまたはAutoLeveling導入時に購入可能 |
| `foodhealing:base_max_health` | 5 | 最大HP+2 | 回数を保存、Config/実Attribute上限は別 |
| `foodhealing:recovery_multiplier` | 20 | 回復倍率+0.25、初期1.0 | `1+0.25n` |
| `foodhealing:base_outgoing_damage` | 20 | 全体与ダメージ倍率+0.10、初期1.0 | `1+0.10n` |
| `foodhealing:tacz_base_outgoing_damage` | 2 | TaCZ与ダメージ倍率+0.01、初期1.0 | TaCZ存在が必要、`1+0.01n`、Ammoと別 |

内部保存IDは上記7に`foodhealing:base_damage_reduction_transcendence`と
`foodhealing:high_difficulty_reduction_transcendence`を加えた9。UI購入系統は7のまま。
軽減のremainingはlinear<99で`(100-linear)/100`、99到達時`0.01`、超越tで`10^-(t+2)`。
通常と高難度のremainingは乗算する。超越は表示100%でも正の下限を守り、完全無敵へ丸めない。
double指数の限界は`Double.MIN_NORMAL`、正damageのfloat underflowは`Float.MIN_NORMAL`、正overflowは`Float.MAX_VALUE`へ安全化する。
高難度MODの存在条件は購入条件であり、すべての敵対traitを無効化する意味ではない。

Armor所有UUIDは`13ab10e9-952c-4fa2-a58e-62fe3af981b7`、最大HPは`25c88c1b-093d-4d45-bfeb-5d6f46b1c854`。
最大HP設定は既定1,000,000、許容最大1e12。これはvanilla/他MODの実Attribute limitを強制変更しない。

## 7. 全26スキルの登録・価格・前提

実registry26/26（通常18、Root2、Heroics2、高難度3、Ammo1）。下表は実ja/en翻訳とcost配列から照合した。
初回取得時ON。段階購入で既存OFFを勝手にONへ戻さない。永続キーはIDで、localized nameではない。
全行server取引・本人canonicalに保存し、各効果のON条件は後述する。未取得は効果なし。

| ID | 日本語 / English（実翻訳） | 最大Lv | 各Lvの必要SP | 合計SP | 取得前提 | 初回 / 上位購入時toggle |
|---|---|---:|---|---:|---|---|
| `foodhealing:fire_resistance` | 耐火の心得 / Fire Resistance Mastery | 1 | 1 | 1 | なし | 初回ON / 既存OFF保持 |
| `foodhealing:water_night_vision` | 水月と暗視の心得 / Water and Night Vision Mastery | 1 | 1 | 1 | なし | 初回ON / 既存OFF保持 |
| `foodhealing:fast_eating` | 早食いI / Fast Eating I | 1 | 10 | 10 | なし | 初回ON / 既存OFF保持 |
| `foodhealing:acrobatics` | 軽業の心得 / Acrobatics | 1 | 1 | 1 | なし | 初回ON / 既存OFF保持 |
| `foodhealing:flame_blessing` | 炎の加護 / Flame Blessing | 1 | 2 | 2 | なし | 初回ON / 既存OFF保持 |
| `foodhealing:explosion_resistance` | 爆破耐性の心得 / Explosion Resistance | 1 | 10 | 10 | なし | 初回ON / 既存OFF保持 |
| `foodhealing:purification` | 浄化 / Purification | 1 | 3 | 3 | なし | 初回ON / 既存OFF保持 |
| `foodhealing:food_production_mastery` | 食料生産の極意 / Food Production Mastery | 1 | 4 | 4 | なし | 初回ON / 既存OFF保持 |
| `foodhealing:slaughter` | 屠殺の心得 / Slaughter Mastery | 1 | 5 | 5 | なし | 初回ON / 既存OFF保持 |
| `foodhealing:satisfaction` | 満足感 / Satisfaction | 3 | 5 / 5 / 5 | 15 | なし | 初回ON / 既存OFF保持 |
| `foodhealing:quarrying` | 採石の心得 / Quarrying | 1 | 1 | 1 | なし | 初回ON / 既存OFF保持 |
| `foodhealing:gathering` | 採取の心得 / Gathering Mastery | 3 | 5 / 5 / 5 | 15 | なし | 初回ON / 既存OFF保持 |
| `foodhealing:unbreaking` | 不壊の心得 / Unbreaking Mastery | 3 | 10 / 20 / 30 | 60 | なし | 初回ON / 既存OFF保持 |
| `foodhealing:armor_mastery` | 防具の極意 / Armor Mastery | 1 | 20 | 20 | なし | 初回ON / 既存OFF保持 |
| `foodhealing:flight` | 飛翔の心得 / Flight Mastery | 1 | 2 | 2 | なし | 初回ON / 既存OFF保持 |
| `foodhealing:kongo` | 金剛の心得 / Kongo | 1 | 50 | 50 | なし | 初回ON / 既存OFF保持 |
| `foodhealing:immovable_mastery` | 不動の極意 / Immovable Mastery | 1 | 50 | 50 | なし | 初回ON / 既存OFF保持 |
| `foodhealing:pursuit` | 追撃の心得 / Pursuit Mastery | 9 | 100 / 100 / 100 / 100 / 100 / 100 / 100 / 100 / 100 | 900 | なし | 初回ON / 既存OFF保持 |
| `foodhealing:guts` | 根性 / Root | 5 | 10 / 10 / 10 / 10 / 10 | 50 | なし | 初回ON / 既存OFF保持 |
| `foodhealing:true_guts` | 真・根性 / True Root | 1 | 20 | 20 | foodhealing:guts Lv5 | 初回ON / 既存OFF保持 |
| `foodhealing:heroics` | 火事場 / Heroics | 5 | 30 / 30 / 30 / 30 / 30 | 150 | なし | 初回ON / 既存OFF保持 |
| `foodhealing:true_heroics` | 真・火事場 / True Heroics | 1 | 100 | 100 | foodhealing:heroics Lv5 | 初回ON / 既存OFF保持 |
| `foodhealing:break_realm_mastery` | 破界の極意 / Break Realm Mastery | 1 | 20 | 20 | なし | 初回ON / 既存OFF保持 |
| `foodhealing:purification_mastery` | 浄化の極意 / Purification Mastery | 1 | 100 | 100 | foodhealing:purification Lv1 | 初回ON / 既存OFF保持 |
| `foodhealing:truth_mastery` | 真実の極意 / Truth Mastery | 1 | 500 | 500 | foodhealing:purification_mastery Lv1 | 初回ON / 既存OFF保持 |
| `foodhealing:tacz_ammo_conservation` | TaCZ 弾薬節約 / TaCZ Ammo Conservation | 10 | 50 / 50 / 50 / 50 / 50 / 50 / 50 / 50 / 50 / 50 | 500 | なし | 初回ON / 既存OFF保持 |

| 有限総費用の内訳 | SP |
|---|---:|
| 通常18 | 1150 |
| Root + True Root | 70 |
| Heroics + True Heroics | 250 |
| 高難度3（Break Realm予約ノードを含む） | 620 |
| Ammo Conservation | 500 |
| **設計上の有限全26合計** | **2590** |

2590は未実装Break Realm20SPを含む有限設計総額であり、現在全26を購入できるという宣言ではない。
`IMPLEMENTATION_PENDING`は現在 **break_realm_masteryのみ**。P/T/FlightはPURCHASE READY。
Ammoは対応TaCZ不在/未対応で購入不可。基礎強化のrepeatable費用は2590に含めない。
旧価格で取得済みのLv/SP/toggleを遡及請求・返金・最大化しない。新価格は今後の正常購入だけ。

## 8. 通常スキル18の効果・対象

| ID末尾 | 発動条件と効果 | 対象外・限界 |
|---|---|---|
| fire_resistance | 取得ONでFire Resistance I管理と`IS_FIRE`damage取消 | 外部の非fire強制作用は対象外 |
| water_night_vision | 取得ONでWater Breathing I/Night Vision I | 外部effectを上書きしない |
| fast_eating | food使用時間を半分、最低1tick | 非food/TaCZ使用は対象外 |
| acrobatics | `minecraft:fall`damage取消 | 全移動/void等の総称ではない |
| flame_blessing | 本人が燃焼中、通常numeric remaining×0.70 | 火を自動で付ける機能ではない |
| explosion_resistance | `IS_EXPLOSION`remaining×0.10 | 爆発以外不変 |
| purification | PlayerTick ENDで現在HARMFUL MobEffectを除去 | 付与前抑止ではない。既に起きたdamageを巻き戻さない |
| food_production_mastery | 対応food result×2 | 非food・独自machine・無人取出し保証なし、§14 |
| slaughter | Player killの非Player・非MONSTERカテゴリdrop×3 | Player死亡dropを絶対に増やさない。名前による動物推測なし |
| satisfaction | food消費回避25/50/75% | 食事の栄養/回復は維持、弾薬と独立 |
| quarrying | exact deepslate速度補正とstone/deepslate限定追加鉱石 | §9 |
| gathering | 対象blockの生成済みloot×2/4/6 | 対象分類とGLM境界は§10 |
| unbreaking | 標準耐久消耗回避90/95/29÷30 | §10。全独自耐久/energyを保証しない |
| armor_mastery | 標準damageable全般の1回消耗を最大1へ | 名前に反してArmorItem限定ではない、§10 |
| flight | Survival/Adventureで所有権付きmayfly付与 | exact provider・DENY・unknown境界は§18 |
| kongo | Resistance IV相当の通常damage80%軽減 | BYPASSES_EFFECTS対象外、外部Resistanceを尊重 |
| immovable_mastery | 標準攻撃knockback抑止＋通常numeric50%軽減 | §11。全強制移動/強制死を防がない |
| pursuit | Lvと同数の追加native hit（最大9） | recursion防止・victim防御維持、§10 |

Food Healing管理effectは240tickを付与し残200未満で更新。`foodhealing:owned_effects`の
amplifier/期限一致（±3tick）だけを所有と判断する。foreign effectへ上書きしない。
OFF時は所有印を外し、効果は自然失効するため最大約12秒残る場合がある。これを即時削除仕様と誤記しない。
通常浄化のHARMFUL除去は明示skill効果であり、この管理effect所有規則とは別。
Kongoは既存Resistanceが弱い場合だけ不足分を補正し、IV以上へ追加80%を重ねない。

## 9. 採石の心得

正常canonical/非pendingの取得ONが必要。exact `minecraft:deepslate`に対し、既存Forge BreakSpeed値へ
hardness(deepslate)/hardness(stone)（標準2）を掛け、同条件のstone相当destroy progressにする。
clientは同期canonicalによる予測、serverが権威。Efficiency/Haste/Fatigue、工具条件を置換しない。
stone自体の速度、cobbled/polished/tuff/deepslate oresは速度対象外。

通常ServerPlayerがstone/deepslateを適切なtoolで正常harvestしたnative `playerDestroy`だけtokenを作る。
取消/爆発/FakePlayer/Creative/機械/任意loot再実行は対象外。finallyでscopeを閉じる。
生成済みlootに1回、単一double rollを排他的に適用する:

- `0≤r<0.005`: `minecraft:iron_ore`。
- `0.005≤r<0.010`: `minecraft:copper_ore`。
- その他: bonus0。各0.5%、合計1%。raw/ingot/deepslate oreへ置換しない。

当選数のみGathering未取得/OFF=1、Lv1/2/3 ON=2/4/6。確率不変。Fortuneをbonusへ追加適用しない。
通常Silk/Fortune lootを再抽選しない。対応外MODの全採掘経路への互換は未保証。

## 10. 採取・耐久・追撃・満足感

### 採取

Forge GLMに届いた現在の生成済みstackを1回倍率化。元3個なら6/12/18。metadataをcopyしmaxStackごとに分割。
対象は`forge:ores`、DropExperienceBlock、CropBlock、NetherWart/Cocoa/SweetBerryBush、
Glowstone/Clay/Melon/Pumpkin。PlayerをTHIS_ENTITYに持つblock lootのみ。
旧BreakEventでLootTableを再rollしてコピーspawnする方式を復元しない。Player死亡dropや一般mobdropは別。

### 耐久

正常ServerPlayer帰属の標準damageable ItemStack全般（tool/weapon/armor/bow/crossbow/shield/釣竿等）。
順序は`hurtAndBreak`のForge Item.damageItem callback→共通`ItemStack.hurt` HEAD→
Armor Mastery cap→FH Unbreaking→vanilla Unbreaking enchant→native damage/break。
direct hurtに元から無いcallbackを追加しない。両入口でFH処理は一度。
Armor Masteryはamount≤0不変、1→1、>1→1。FH不壊rollはfloat `r<0.90F / 0.95F / 29.0F/30.0F`、同値は失敗。
非対象/非正量/不壊OFFで余分なFH RNGなし。callback/shrink/vanilla RNGを置換しない。
arbitrary setDamageValue、capability独自耐久、energy消費はAdapterがない限り対象外。

### 追撃

現在有効Lvだけ追加hit（Lv1…9で1…9、累積和ではない）。同DamageSource・元LivingDamageEventのcurrent amountを基準にnative hurtへ通す。
ThreadLocal guardでFH outgoing再乗算と再帰を防止。victim armor/Resistance/軽減/Root/外部防御はnative経路に残る。
追加hit中のiframe bypassはfinallyで復元し、死亡後中止。boss直接削除・death処理置換ではない。

### 満足感

通常food use開始時にserverで一度rollし、expected stack付き判断をclientへ同期。完了時に一度消費し、TTL20秒/中断/logout等でclear。
当選時はnative food shrinkを回避し、後からコピーrefundしない。container/remainderは取引に従う。
native開始eventを迂回する特殊feedはserver側fallback rollがあるが、client予測は専用統合なしには保証しない。
TaCZ Ammo/採取/不壊/防具の極意に結び付けない。

## 11. 不動の極意とdamage合成

不動は正常server canonicalの取得ONで、既存numeric軽減後に独立remaining×0.50。
LivingKnockBackEvent、native矢/Punchの追加push、Explosionの追加velocity/本人packet impulseだけを抑止。
本人Explosion vectorはZERO、既存正当速度は保持。全tick速度0・座標固定・全setter取消をしない。
歩行/jump/重力/水泳/Elytra/flight/vehicle/teleport/portal/pistonを止める仕様ではない。
外部独自movement/cap/custom packet、kill/discard/setHealth強制死、Soul閾値死は対象外。

Player所有攻撃のFH outgoing順は、対応SW専用倍率→全体基礎→TaCZ該当時の基礎/Ammo→Heroics。
pending legacy SWは別枝で、v3専用倍率と二重適用しない。
incomingは既存native armor等の後、fall/fireの明示取消、Explosion×0.10、燃焼Flame×0.70、
通常DR、高難度DR、Heroics、Kongo補正、不動×0.50。最後にRootのHP下限保護。
例: Explosion100→10→不動5。減算百分率を足して一律100%へしない。
保護対象はhookに届くnumericであり、外部全強制作用を通常damageとしてまとめない。

## 12. 根性・真・根性

Root取得ON、food由来Nutritionのみ。単一食品18以上、または最初の食品から固定300tick以内に合計18で発動。
途中の食品で蓄積窓を延長しない。超過分を次回へ繰り越さない。

| Lv | SP/段階 | active | 終了後cooldown |
|---:|---:|---:|---:|
| 1 | 10 | 40tick / 2秒 | 400tick / 20秒 |
| 2 | 10 | 80tick / 4秒 | 300tick / 15秒 |
| 3 | 10 | 120tick / 6秒 | 200tick / 10秒 |
| 4 | 10 | 200tick / 10秒 | 100tick / 5秒 |
| 5 | 10 | 300tick / 15秒 | 0 |

`RootActiveUntil > server gameTime`がactive。効果表示はfoodhealing:guts。現行保護は
LivingDamageでHP1未満にしない、LivingDeath取消、tickで保護中HP≤0を1へ戻す処理。
「無敵」はこの保護契約の呼称であり、全ての非致死damageを0にする意味ではない。外部の全直接削除は保証しない。

True Rootは20SP/RootLv5前提。通常Root active中に本人True RootもONならNutritionを最大18まで蓄積し、次回1回分だけ予約。
完成予約はTrue Root自身ON→OFFでも**保持 / RETAIN LOCKED**。OFF中は新規予約/保持予約による発動をしない。
OFF中通常待機で完成18を消費・削除しない。元期限後に通常Root有効のままTrue Rootを再ONすると1回消費・1回発動。
消費後OFF→ONだけでは再発動しない。元active中OFF→ONは絶対期限を変更/延長/満タン化しない。
通常Rootの保護をTrue Root OFFで無効にしない。発動中の時間を停止保存する仕様ではない。
True ONで元期限が来た際は完成予約を一度消費して次activeへ。非food Food Level増加は予約へ入らない。
**未LOCKの拡張境界**: Nutrition18未満の途中蓄積、親Root自身OFF、死亡/logout/restartを跨ぐ予約。
保存fieldや現在コードの挙動があることだけを、新しい利用者保証にしない。

## 13. 火事場・真・火事場

通常Heroics取得ON、現在HP≤最大HP40%。各段階は上書き値で、段階倍率を重ねない。

| Lv | SP/段階 | outgoing | 有効Armor/Toughness | 独立DR |
|---:|---:|---:|---:|---:|
| 1 | 30 | ×2 | ×2 | 10% |
| 2 | 30 | ×2.5 | ×4 | 20% |
| 3 | 30 | ×3 | ×8 | 30% |
| 4 | 30 | ×4 | ×16 | 40% |
| 5 | 30 | ×5 | ×32 | 50% |

True Heroicsは100SP、取得前提HeroicsLv5、自身ONでHP≤80%ならoutgoing×20、Armor/Toughness×64、DR99%。
通常との条件重複ではTrueを優先。購入前提と効果時条件は別で、通常Heroics自身ONを追加要求しない。
正当な装備/外部modifierを読み、有効防具値の計算へ倍率を適用。foreign Attributeを削除せず、旧FH owned2UUIDだけ整理。
Armor拡張時もnative MAX_ARMORの吸収天井を保ち、その後の独立DRと分離する。数値finite/clampを守る。
直接kill/forced deathの無条件免疫ではない。

## 14. 食料生産と通常取引

通常2×2/3×3のfood resultを取引内で×2。通常click/shift取出し、remainder、overflow、fullinventoryを同一確定処理へ結び付ける。
未取得/OFF・非foodは元個数。完成後のイベントで同じ結果を再コピーする方式ではない。
対応furnace/smoker/campfireは本人が手動で受け取るfoodの範囲。共有inventory/hopper等の自動取出しは×1。
標準menu経路に乗る調理と、Farmer's Delight等の独自料理装置全般を区別する。独自機械の全操作は未保証。
サーバーが最終個数を確定し、client previewと取引stateを合わせる。未受領resultを再login等で再付与しない。

**食料生産の極意による可逆クラフト経由の増加は既知かつ許容された仕様。バグ修正対象外。**
この許容はPlayer死亡drop、packet replay、desync、意図しない二重取出しまで認めるものではない。

## 15. 浄化の極意の正式対応範囲

100SP、通常浄化Lv1取得前提。効果には正常非pending server canonical、通常浄化Lv1と極意Lv1の両取得/両ONが必要。
Truth取得/ONは不要。親OFFでも極意の保存されたONを変えない。購入時に親ONは要求しない。
**optional購入方針A**: 対応MOD0でも購入できる。未対応version混在でも購入可、各Adapterが対応版だけで働く。
価格を使ったから全MODを保護するという保証はしない。

| MOD / exact版 | 効果対象 | 抑止するもの | 維持するもの |
|---|---|---|---|
| `the_trial_monolith` 1.4.9 | Damage Cube | 新規通常/強制Soul加算の2callsite | 独立numeric damage、既存Soul |
| 同上 | InvaderMonolith tick | 本人Soul Protectionをfalseへ解除する作用 | true設定・他entity・外部state全体 |
| 同上 | Invader owner Small Beam | 新規Soul Damage | native numeric |
| 同上 | Invader owner Huge Beam | 新規Soul + LOCK済み巨大numeric例外 | 他owner/非Invader/他playerはnative |
| L2 Hostility2.5.19（§17構成） | poison/slowness/weakness/wither | 本人への新規MobEffect付与 | 既存effect、普通のhit、source trait |
| 同上 | corrosion/erosion | 本人装備への対象trait追加消耗 | 通常装備消耗・他player |
| `fantasy_ending`2.7.20 | UOMのFE6 secondary | §19の6 addEffect命令 | 独立numeric、既存effect |
| FE2.7.20 + EndingLibrary2.1.19fix原物 | UOM限定TimeStop | §20の条件付き本人movement保護 | native停止state・UNKNOWN拒否 |

本リストがv3.0.0最低対応LOCK。未対応L2/Hyperlink/Fumetsu/別bossを自動release必須にしない。
一般MobEffect敵対解除のTrial実処理は未特定。架空hookや全buff保護は存在しない。

## 16. 真実の極意

500SP、取得前提は浄化の極意Lv1。新規適用条件は通常浄化/浄化の極意/真実の極意が全取得Lv1・全ON・非pending。
購入時3ONは要求しない。独立保存toggleを連動書換えしない。optional購入方針はPと同じA。
現在の正式対象は **L2の6 IDだけ**。Trial/FE/全bossの永久能力削除へ拡張しない。

同dimension、本人中心で各軸±75 inclusive（球形ではない）の生存Mob。
移動中最低5tick間隔、静止中20tick間隔のloaded entity AABB query、joinと効果直前にも整合する。
151³block全走査・強制chunkloadはしない。膨大entity/全modpackの性能網羅は未証明。
`foodhealing:truth_l2`にVersion1/Mob UUID/対象Traitsの所有markerを書き、native `removeTrait`と`syncToClient`を使う。
effect直前nullifyと安全なprune境界を分け、live iteratorを破壊しない。
marked trait再付与は再抑止。OFF・範囲外・保存再読込でも元に戻さない。
対象外trait、level/AI/equipment/無関係NBT/capabilityは保全。markerだけ書いて無効化完了としない。
現在6種はmodifier cleanupを必要としない実経路。Speedy/Tank等のAttributeTrait追加時は別cleanup監査が必要。

## 17. L2・Trialの実物と検証境界

L2正式registry IDは `l2hostility:poison`、`l2hostility:slowness`、`l2hostility:corrosion`、
`l2hostility:erosion`、`l2hostility:weakness`、`l2hostility:wither`。
版gateはHostility2.5.19 / Library2.5.3 / Complements2.6.1 / DamageTracker0.4.4の組。
Tracker0.4.3はHostilityの同梱候補、実選択0.4.4はComplements側。nestedを外側へ重複配置しない。
Curios5.12.0+1.20.1、Patchouli1.20.1-84-FORGEの既存承認構成で統合。Cataclysmは必須扱いしない。
39登録中6対応、残33は未対応。pulling/repellingのclient作用、attribute cleanup等を対応済みに数えない。

Trialは `the_trial_monolith`1.4.9のnative Cube/Invader限定。Cube通常加算・force加算を止めるがSoulを治療しない。
Invader Beamはnative owner解決、実Invader型/EntityType、同level、本人P条件を要求。
Huge例外はexact Huge `lambda$activate$0`のhurt1call（当該版native `Float.MAX_VALUE`）を抑止する。
巨大な数値なら全sourceをcancelする閾値filterではない。Smallと一般numericは残す。
Soulにより変わる実効HP、NBT Health、MAX_HEALTH、HUDは別観測量。既存Soul0.06を両ONで治療しない。

既存自動 **L2 expanded133/133、Cube49/49、Invader86/86** と限定実clientを保持。
L2旧4IDはGUI/A/B/T/C・全OFF・範囲外・同source保存再読込まで、weakness/witherは旧GUI/A/B/T＋別run C/sealまで。
Cube3条件と保存再読込、InvaderF1/F2/S1＋別runS2/H1は限定PASS。自然boss戦/全attack/全version/全lifecycleとはしない。

## 18. Flight ownership

2SP、正常非pending取得Lv1/ON。survival/adventureのnative mayfly false→trueをFHが実際に付与した時だけ、
**ServerPlayer実instance**を所有集合へ入れる。UUID/NBTだけでは権限を復元しない。
OFF/無資格で自分のgrantを取り消す際、対応providerのpositiveをreadonly確認し、外部権限を保持する。
Creative/Spectatorはnative authority優先、FH所有を外し、mayfly/flyingを破壊しない。
実値が変わった時だけonUpdateAbilities。logout/clone/server stopでruntime所有を消す。
flyingSpeed/walkingSpeed/velocity/gravity/fallDistance/invulnerability、外部equipment/capabilityを書き換えない。

| provider | 版/条件 | 正式保護境界 |
|---|---|---|
| FE/EL/Curios | 2.7.20 / 2.1.19fix / 5.14.1+1.20.1 | functional Curio `fantasy_ending:the_domain_of_fade` のnative grant/revoke |
| FE chest | `fantasy_ending:fantasy_ending_chestplate` CHEST | FH OFFで保全。FE自身にnative解除なしという制限は残る |
| EndingLibrary | 2.1.19fix、abilityMayfly/abilityFlying | ±値をreadonly。負値の明示DENYをFHより優先 |
| Mekanism | loader **10.4.16**、§23原物hash | CHEST MekaSuit、Gravitational Modulating Unit存在/ON/必要energy、native readiness |
| Avaritia | 4.0.3、§23原物hash | `avaritia:infinity_chestplate` CHEST、full set要求なし |

EL DENY中はFHから付与/再付与しない。解除後、FH取得ONなら正常再評価。
他provider writerを妨害せず、EL stateも変更しない。external query例外はUNKNOWNとして現在値を保持。
Mekaはactive flyingを要求せず、module/energy失効を修復しない。Avaritia private Info/mapも変更しない。
未知provider/同版別hash/foreign-afterの一般的な所有者推定はできない。
**GENERIC FLIGHT PROVIDER ATTRIBUTION = NOT AVAILABLE**。標準booleanには所有者/refcountがない。
対応外を勝手に消す/heuristic採用する保証はしない。実client全飛行、全provider混在、別JVM全lifecycleは未検証。

## 19. Fantasy Ending FE6 secondary

FE2.7.20のexact登録 `fantasy_ending:ultimate_order_manager`、実`UomWither`、同level、
本人P資格を照合する。projectileはnative ownerがそのUOMであることを確認する。

| native経路 | call数 | 新規secondary対象 |
|---|---:|---|
| `UomWither.dreamShadowBeam` | 2 | slowness / poison |
| `UomWither.finalSkillAttack` | 1 | ban_healing |
| `UomWither` powered counter | 1 | instant_damage |
| `StarEntity` AOE | 1 | ban_healing |
| UOM-owned vanilla `WitherSkull.onHitEntity` | 1 | wither |

対象addEffectだけfalseにし、numeric hit/爆発/owner処理・既存effectを保持。
全MobEffect event取消・全UOM damage免疫ではない。別owner/非player/無資格はnative1回。
自動63＋補足8実行（metadata重複1除く**70 unique**）、実clientはDream代表1回・GUI/同期/HUD限定。
Star/final BAN・counter・skullの実client全再現や自然boss戦は未実施。

## 20. UOM TimeStopの限定製品設計

exact FE2.7.20とEndingLibrary2.1.19fixの**版＋原Jar SHA**をgateとする。
server `AuthorityContext`はMinecraftServer/ServerLevel世代に属し、sessionはServerPlayer/listener/Connection/channel実参照に束縛。
同UUIDでもnew sessionで旧Grantを復活させない。epoch/sequence/revisionで同一権限と新規権限を分離する。
native初期UOM開始の実証、source全件UOM帰属、global/dimension一致、canonical P資格、alive/current sessionが揃う場合だけALLOW。
FOREIGN/unknown source、観測喪失、entry deserialize/initial processing中、terminal未完了、参照不一致はDENY。

native terminal callbackのexact完了をwitnessして終了をcommitする。countが減った/0に見えたという推測だけで帰属を修復しない。
source lifecycle/転送不明、途中再延長など未証明枝ではUNKNOWNを保つ。外部EndingLibraryのflag/count/NBT/entity fieldsをFHが修正しない。
Grantは永続化しない。logout/disconnect/clone/context closeで不可逆revoke。新epochを観測してから新Grantを作る。
停止中loginはfresh epochを要求し、旧停止の見かけだけで新sessionへ許可しない。
server move packet境界も現在sessionとownershipを照合し、古いhold packetをnative canMove-onlyで通さない。
vehicleは証明済みDENY境界を保持。**UOM/P vehicle ALLOW方針は未LOCK**。

client leaseは対象UUID/dimension/session/epoch/sequence/revisionと現在client world/listener/Connectionに結合。
短命lease（server送信1000ms、権威tick5ごとの更新）・旧revision拒否・logout/world-null/context境界でclear。
native停止の解除やclientの主張をserver authorityへ昇格しない。pause/resumeと画面のworld-nullを別境界として扱う。
positive native保存値を読んでも、FHの旧context/session/Grant/UNKNOWN ledgerを復元しない。

queue #1/#2で指定されたdedicated・client movement・正常復帰・positive保存・same-JVM reload・5 END独立観測・teardownを限定完了。
**広い判定は SAFE DESIGN PROVEN=NO / SOURCE DIMENSION TRANSITION VERIFIED=NO / BLOCKED - TIME STOP SOURCE OWNERSHIP を維持**。
これはv3の利用者LOCK済み限定受入と区別する。全TimeStop源/全process故障/全render/correction/全vehicleを証明したとはしない。

## 21. FE shader正式内蔵

client-only、MC1.20.1 / Forge47.4.0 / FE2.7.20 / §23 exactFE hashの全一致が必要。
専用`foodhealing.feuniform.mixins.json`を本体に内蔵。別`fe_uniform_compat`/検証observer/helperを配布条件にしない。
対象は次の**2 receiver × 合計5組だけ**。

| exact receiver | resource |
|---|---|
| `com.mega.uom.client.render.shader.cosmic.CosmicShaderInstance` | `fantasy_ending:cosmic`, `fantasy_ending:cosmic_2` |
| `com.mega.uom.client.render.shader.core.MShaderInstance` | `fantasy_ending:hash`, `fantasy_ending:rendertype_light_beacon_beam`, `fantasy_ending:rendertype_cil_particle` |

親ShaderInstanceのResourceLocation constructor内のprivate `m_173354_(JsonElement)V`呼出1か所を限定redirect。
対象だけFE native public parser/factoryへ一度delegate、対象外は元private parserへ一度fallback。
親parser visibility/body、初期化順序、asset/GLSL、外部原Jarを変更しない。
call0/2・descriptor/bridge不一致・half apply/競合は拒否し、広い代替処理へ緩和しない。
FE不在/別版/別hash/server/MC/Forge不一致は介入0。EndingLibrary hashは受入構成照合で、shaderの追加gateではない。
STATIC11profiles/426checks、A3不在startup、B3正式startup＋通常ingot描画PASS。
内部uniform22型/GL identityは旧専用試験＋意味同一照合を根拠とし、B3で再測定したとはしない。全GPU/shader/resourcepack保証なし。

## 22. TaCZとSuperbWarfare

### TaCZ

正式Ammo Adapter版は `tacz` **1.1.7-hotfix2**。通常基礎TaCZ倍率とAmmo skillを分離。
基礎は2SP/購入、+0.01。Ammoは50SP×10、保存率10/20/…/100%、専用damage×1.05/1.10/…/1.50。
Lv1以降、対応gunのheat加算を抑止し、取得/再ON/持替えでactive gunの既存heat/overheat lockを正規APIで整理。
food消費・不壊・採取などのskillに結合しない。
対象はServerPlayerの現在main hand実`ModernKineticGunItem`、実gun index存在、対応版。
1shotのsingle consume callsiteで判定し、pelletごとrollしない。実弾なし/bolt未完了から発射を作らない。
manual actionの保存弾でも通常bolt機械サイクルが必要。弾を抜いてコピーrefundせずchamberに保持し、二重装填を防ぐ。
server fullstack slot同期＋専用bolt stateでclient予測を合わせる。slot/gun/current stack境界を保持。
不在時もnodeを表示するが購入不可、SP消費0。基礎TaCZ不在購入もserverで拒否。
既存9/9実client限定記録は維持。購入時heat解除INCONCLUSIVE、厳密physical shot計数/全bolt packet内部遷移未観測、hot-gun持替えSKIPPED等の限界を消さない。
**ALL TACZ / ALL GUNPACK COMPATIBILITY = NOT TESTED**、dedicated遅延全条件未試験。

### SuperbWarfare

承認原物0.8.9.1-mc1.20.1-993063bed、loader versionは**0.8.9.1**（filename hotfixを追加しない）。
DamageSource ownerがPlayer、directEntity実class `com.atsuishio.superbwarfare.entity.projectile.ProjectileEntity`、
registry `superbwarfare:projectile`、DamageTypeは同namespaceの
`gunfire`/`gunfire_absolute`/`gunfire_headshot`/`gunfire_headshot_absolute`に限定。
専用倍率 **1 + ShokugiLevel × 1.0**、Lv0/1/200で1/2/201。
fresh/正常移行済みv3へ同じ式を一度適用し、全体→Heroics等の既存合成を維持。
pendingは検証済みlegacy levelの旧fallbackだけを保持し、不正データの保護を解除しない。
名前/namespace推測で爆発・搭乗兵器・全攻撃を含めない。通常銃GLOCK-17実clientはHP差5.5/11・比2の限定証拠。
Kotlin for Forge4.11.0以上が本体要件。nested依存はloaderの実選択版と候補を区別し、外へ一律展開しない。

## 23. Optional構成・原物識別

Food Healing単体が起動する。optionalを必須依存へ変更せず、対応版確認前に外部classをlinkしない。
対応外Adapter inactiveと、その外部MOD自身が起動できることは別。全版互換の宣言ではない。
同時導入時は適合Adapterのみ有効。P/TはMOD0でも購入可能だが、対応効果0なら効果対象も0。

| 原物 | version | SHA-256 |
|---|---|---|
| Fantasy Ending | 2.7.20 | `E32FD4BA2E48FCF2C84F71AA07155C63D6BFA2178BB840899307DA0DA5E5D141` |
| EndingLibrary | 2.1.19fix | `0E29AF51DABD4E0EA8A315C1886E61F3ABEE62045F196EB6913D7F4DBA6CDC34` |
| L2 Hostility | 2.5.19 | `168665D887B34C5BD79311F0F302531CBED3F1954C43D59C23FF8564DC16C65B` |
| SuperbWarfare | 0.8.9.1 / 993063bed | `3AAF4C239BC0FB31F9217927A44D74071D904D1DD03C5308CA3395ECD67D86DC` |
| Mekanism | loader10.4.16 / filename10.4.16.80 | `3B0D191FA4A45E662F9725991D76A30192F30186ABB2BA694B873B5F7B9358D2` |
| Avaritia | 4.0.3 | `48F23CEA99D1D2E6CED4215B9FE3F9F45641B2CD5650C8725A3EE75B500483BB` |
| Pam Trees | 1.0.2 | `69E0C722C786B78AEA299E7FAF27991E6533A671A66F281FB5CED279EB31B0DD` |
| Pam Food Core補足 | 1.0.5（filename1.20.4） | `1F18655D5EEEA99EECCF0D9D458DBF88D6B59C1A9F00C17E322DB8D55A54ED5B` |
| the_trial_monolith | 1.4.9 | `5EFE4C068F24E611C215A0A20FE9698D5C5F7EFC1476C8E5CEAEA78B8CE1A0AD` |
| l2library | 2.5.3 | `C36A9C5C93B6114C15AEA11D084600A8ED230F179F1B70D6222633A50F77FC18` |
| l2complements | 2.6.1 | `2AAE9D5FC4286F2952939F9EDDA636AD85D6A9451C960D1C2ECE6732B8862E43` |
| tacz | 1.1.7-hotfix2 | `FC5F1DAB09AFD5399604DB0F60845D41D46CF015C158CBC12E7EF716CF6BDB46` |

表のhash照合とruntime gateは同義ではない。実hash gateはTimeStop、Mekanism、Avaritia、shaderなど各節に明記したもの。
L2/Trial/FE6/SWは各metadata/型/ID gate、Pam recipesはpresence condition。
受入FE client依存はCurios5.14.1+1.20.1、GeckoLib4.8.2、Iron's Spellbooks3.16.3、Iron's Lib2.1.0。
nested PlayerAnimator1.0.2-rc1+1.20、MixinExtras0.4.1はloader選択。全dependencyを本体へ同梱しない。
外部API・原Jarは権利元のもの。source releaseへ外部class/Jar/assetsをコピーしない。

## 24. Pam Treesと原木クラフト

**対象収穫物H1個 + item tag `#minecraft:logs` 1個 → H2個**、shapeless。
skill前提/出所追跡不要。2つのoccupied slotから各1個消費、net+1H。余分なslotは不一致。
logsはwood/stripped/Nether stems/hyphae/適切にtagされた他MOD品を含み、logs_that_burnへ狭めない。
Pam Trees1.0.2 direct harvest whitelist50は[機械可読mapping](../tools/data/pam_trees_1_0_2_harvests.json)を正本とする。
実製品recipesは**Pam条件49＋無条件vanilla apple/cocoa2＝51**。Pam不在2/present51。
`foodhealing:log_duplication/apple`と`foodhealing:log_duplication/cocoa_beans`は標準feature。
旧Pam apple recipeは削除済み。paper/stringはPam条件。cocoaをPam50へ混入させない。
生成器は[generate_pam_tree_recipes.py](../tools/generate_pam_tree_recipes.py)、serializerはvanilla crafting_shapeless。
条件はForge mod_loadedをitem解決前に評価。別Trees版は未保証。

FPM未取得/OFF/ONでapple出力2/2/4、非food cocoa2/2/2、paper/string2。非foodをedibleにしない。
苗木/treeblock/processed nuts/無関係作物は対象外。原物mapleはmaplesyrupitem、appleはminecraft:appleで、古いlang名をIDにしない。

Food Core1.0.5補足構成はcookingoilを供給し必要Crops経路を満たすが、次3recipeは**UPSTREAM BROKEN / UNAVAILABLE**:

| recipe | 欠落result |
|---|---|
| `pamhc2foodcore:melonpieitem` | `pamhc2foodcore:melonpieitem` |
| `pamhc2foodcore:honeymuffinitem` | `pamhc2foodcore:honeymuffinitem` |
| `pamhc2foodcore:caramelcupcakeitem_x4` | `pamhc2foodcore:caramelcupcakeitem` |

exact原物hash・この組合せ・ERROR3件だけの既知例外を利用者が非blockerとして受入。
Food Healing ERROR0/必要Pam経路PASS/unexpected ERROR0が条件。namespace wildcardや別件数へ一般化しない。
外部Jar/recipe修復、fake item、datapack上書きは製品に無い。Food Core全機能/JEI/texture保証ではない。

## 25. 永続データ・lifecycle

Shokugi capabilityは`foodhealing:shokugi_data`。現在schema **FoodHealingDataVersion=5**。

| NBT key | 型・意味 |
|---|---|
| FoodHealingDataVersion | int5 |
| ShokugiLevel / EatCount | long、Lv/部分Nutrition進捗 |
| UnspentSkillPoints / SpentSkillPoints | long、未使用/使用済 |
| AcquiredSkills | list compound `{Id:string,Level:int}` |
| BaseStats | compound、§6の9 ID→long |
| DisabledSkills | list string、OFFのIDだけ |
| LegacyMigrationPending | boolean（NBT byte） |
| LegacyShokugiLevel / LegacyEatCount | long、旧証拠値 |
| LegacyV2Backup | compound、raw旧データを永久保全 |
| RootAccumulatedNutrition / RootReservedNutrition | int0〜18 |
| RootAccumulationDeadline / RootActiveUntil / RootCooldownUntil | long gameTime期限 |
| CountMigrationError / CountMigrationInput | string / raw compound、移行拒否時の証拠 |

Food DiversityはCurrentEatenFoods/AllEatenFoodsのstring list、MaxHealthBonus int。旧EatenFoodsを読取移行。
死/End-return Cloneではcapabilityを一度revive/copy/invalidateし、両capを同windowでコピー。
join/login/dimension/respawnで自分のmodifierを再構築し同期。progression/upgradesを保存し、現在modifier値を正本にしない。
PlayerHealthLifecycleは通常死亡respawnで再構築後の最大HPへ、End帰還では非death Cloneから運んだ部分HPをclampして保持し、同player dimension移動では現在HPを実上限へclampする。外部全lifecycleを一律初期化しない。
Flight所有、TimeStop Context/Session/Grant、Satisfaction一時判断はcanonical保存fieldに追加しない。
個別Root予約の未LOCK境界は§12を守り、一般capability copyの存在から保証を拡大しない。

## 26. v2.2.5移行・count model移行

正常な未versioned legacy payload（非負integralLv/count、適切な旧count範囲等）だけをfull respecする。
**Lv維持、未使用SP=旧Lv、使用済SP=0、取得/基礎強化/disabled/Root予約をclear**。旧rawはLegacyV2Backupへ永久保持。
旧Lv閾値からスキル自動取得しない。加算refundでなく代入でexactly once。重複payloadで後続購入を巻き戻さない。
不明schema/編集済pending scaffold/不正型/負数等はpendingとrawを保持し、修復して購入可能にしない。
Flight重複/不正Levelのrawも保存し、取得資格・購入へ使わない。

Config移行はForge補正前の原TOMLを確認し、明示new threshold優先、無ければ旧threshold×10。
旧実効thresholdをlegacyFoodActionThresholdとして固定、原filebackupとatomic保存、model1で二重変換しない。
player schema4または正常v2移行で、部分countを一度だけ **floor(C×Tnew/Told)** へ。
既定0/1/50/100/199→0/10/500/1000/1990。schema4のLv/SP/skills/stats/toggle/Rootはそのまま。
v2例Lv2/count35→Lv2/count350/SP2/Spent0/raw35。overflow・不明provenanceはraw保存/pending。
過去playerの独自thresholdがNBTに無い場合、失われた履歴を推測せず最初に確認したlegacy Configを入力とする。
現行3.0.0→同版の通常loadで再返還/再変換しない。原world移行試験と別JVM証拠を分けて保持。

## 27. Network protocol 7

channel `foodhealing:main`、両side protocol文字列**7の完全一致**。

| 順番/ID | packet | 方向 | 内容・検証 |
|---:|---|---|---|
| 0 | ShokugiSyncPacket | S2C | canonical NBT snapshot、client表示state |
| 1 | FoodDiversitySyncPacket | S2C | Diversity snapshot |
| 2 | PurchaseSkillPacket | C2S | ID UTF128/expectedLevel、server取引 |
| 3 | PurchaseBaseStatPacket | C2S | ID UTF128/expectedPurchases long、server取引 |
| 4 | ToggleSkillPacket | C2S | ID UTF128/disabled boolean、本人取得確認 |
| 5 | SatisfactionDecisionPacket | S2C | expected stack/preserve、消費予測 |
| 6 | TaczBoltStatePacket | S2C | slot/gun ID/pending |
| 7 | TimeStopLeasePacket | S2C | UUID/dimension/session/epoch/sequence/revision/allow/期限 |

clientは任意他player UUIDを指定して購入しない。sender本人、ID、current期待値、上限、前提、readiness、optional、SPとoverflowを検証。
失敗時SP消費0・level更新0。成功後canonicalを同期し、stale/replayはcurrent不一致で二重購入を防ぐ。
toggleは期待値付き購入とは別の**指定disabled状態**で、反転命令ではない。True Root消費は実OFF→ONだけで重複抑止。
SP previewはreadonly、取引時に再検証。未知ID/pendingをsilent successにしない。
serverメインスレッド処理とclient main-thread適用をpacketごとに守る。任意のschema/protocol追加を表示修正に混ぜない。

## 28. GUI・HUD・localization

通常InventoryScreen/CreativeModeInventoryScreenで既定 **S** を押してFoodHealingScreenへ。キーは設定で変更可能。
常時global shortcut、chat画面、全外部menuの入口にはしない。skill購入/各独立toggle/7基礎強化/進捗を表示。
serverと同じreadonly購入理由を利用し、TaCZ不在はvisible locked・理由あり。UI無効化だけに頼らずserverも拒否。
日本語/英語両方131キー、26skill/7statの名称・数値を一致させる。内部ID/debug記号を通常説明に露出しない。

成功時のみserverから一度:

- 初回: 「%sを習得した！」 / “Learned …”。
- 段階: 「%sがLv%sになった！」 / “… reached Lv. …”。
- 基礎: 「%sが上昇した！」 / “… increased”。

失敗時成功表示0、失敗理由を表示。購入成功と効果がoptional対象に発動したことは別。
HP HUDはvanilla player heartsを置換し、**HP: current / maximum、ABS別欄**。Creativeでは通常表示を妨げない。
chat等に埋もれないforeground、画面内clamp、local背景/輪郭、Config offset。F1やplayer/worldなしでは描画しない。
食技Lv/count/configured thresholdとHeroics状態も同期表示。SP/HPを描画のために書換えない。
MinecraftのHP floatは2^24以降1単位刻みを保持できず、1e12付近の刻みは約65536。
表示の丸めを内部double HP補完や完全精度保証と誤解しない。AttributeFix等が実上限を拡張することとFH自身の上限変更は別。

## 29. 正式command6経路

prefixは **`/foodhealing syokugi`**。すべて実行者本人playerが対象。console/他player指定版を発明しない。

| subcommand | 引数 | permission | 動作 |
|---|---|---:|---|
| level | なし | 通常 | 現在食技Lv表示 |
| count | なし | 通常 | countと現行threshold表示 |
| setlevel | 非負long | 2 | 本人Lvだけ設定、sync。自動SP付与/skill取得なし |
| setcount | 非負long | 2 | 本人count設定、sync。高値の後続進行安全検証は別 |
| setskillpoint | 非負long | 2 | 未使用SP設定、Spent/取得等は不変 |
| addskillpoint | 非負long | 2 | 未使用SP加算、overflowは全拒否 |

この管理操作は通常GUI購入の成功証拠ではない。通常playerの権限昇格やSP保護解除をしない。
旧 `/foodhealing skill`、旧chat toggle/skill一覧/command UIは撤去。autocomplete6だけ。
legacy保存aliasは維持するが、削除commandを正式手順に記載しない。

## 30. ConfigとMixin/配布構成

Configは`config/foodhealing-common.toml`。値の範囲は実ForgeConfigSpecの通り。

| セクション・キー | 既定値 | 範囲 | 役割 |
|---|---:|---|---|
| `general.maxHealthCap` | 1_000_000.0 | 1024.0〜1_000_000_000_000.0 | 現行設定 |
| `general.healMultiplier` | 2.0 | 0.1〜100.0 | 現行設定 |
| `general.bonusThreshold` | 19 | 1〜100 | 旧互換用。現行スキルの数値を変更しない |
| `general.bonusDurationSeconds` | 1200 | 10〜86400 | 旧互換用。現行スキルの数値を変更しない |
| `general.gutsThreshold` | 19 | 1〜100 | 旧互換用。現行スキルの数値を変更しない |
| `general.gutsDurationSeconds` | 5 | 1〜86400 | 旧互換用。現行スキルの数値を変更しない |
| `general.heroicsThreshold` | 0.4 | 0.0〜1.0 | 旧互換用。現行スキルの数値を変更しない |
| `general.heroicsMultiplier` | 2.0 | 1.0〜100.0 | 旧互換用。現行スキルの数値を変更しない |
| `general.shokugiNutritionLevelUpRequirement` | 2000L | 1L〜Long.MAX_VALUE | 現行設定 |
| `general.legacyFoodActionThreshold` | 200L | 1L〜1_000_000L | 移行確定後の旧threshold。任意変更しない |
| `general.shokugiCountModelVersion` | 1 | 1〜1 | count Configの移行印（player schemaとは別） |
| `general.shokugiLevelUpRequirement` | 200 | 1〜1_000_000 | 旧回数モデルの移行入力のみ |
| `diversity.foodsRequired` | 5 | 1〜100 | 現行設定 |
| `diversity.healthIncrease` | 2 | 1〜1000 | 現行設定 |
| `gui.overlayOffsetY` | -10 | -1000〜1000 | 現行設定 |
| `gui.overlayOffsetX` | 0 | -1000〜1000 | 現行設定 |
| `gui.heroicsTextOffsetY` | 30 | -1000〜1000 | 現行設定 |
| `gui.heroicsTextOffsetX` | 10 | -1000〜1000 | 現行設定 |
| `gui.shokugiTextOffsetY` | 42 | -1000〜1000 | 現行設定 |
| `gui.shokugiTextOffsetX` | 10 | -1000〜1000 | 現行設定 |

| Mixin config（6） | 役割 |
|---|---|
| foodhealing.mixins.json | 標準food/craft/durability/armor/採石/不動等 |
| foodhealing.trial_monolith.mixins.json | Trial exact Cube/Invader |
| foodhealing.l2hostility.mixins.json | L2 six trait/cap限定 |
| foodhealing.endinglibrary.mixins.json | UOM TimeStop限定authority/boundary |
| foodhealing.fantasyending.mixins.json | FE6 secondary |
| foodhealing.feuniform.mixins.json | exact client shader |

refmapは`foodhealing.refmap.json`、distributionはreobf済み。pack format15。
productionのCountObserver/UseObserverはTimeStop機構であり名前だけでverification混入判定しない。
ExampleMod/GameTest/外部MOD class/Jar/verification-only helper/observer/READY HUD/standalone shader patchは配布に含めない。

## 31. 対応・試験・計画の区別と既知制約

STATIC AUDITEDは実物読取、AUTOMATED TESTEDは自動、INTEGRATION TESTEDは実MOD経路、REAL CLIENT LIMITED PASSは記録範囲の画面/入力/同期まで。
compile成功を互換PASSにしない。過去JarのPASSは対象hashと後続変更影響を明記し、最終Jarで新規実行したように転記しない。

| 残る限界 | 現行扱い |
|---|---|
| 認証済み実2-client | **REAL2CLIENT=BLOCKED**。実1-client dedicated/自動2playerと別 |
| L2他33ID・他版/全boss | optional backlog、現在6IDへ含めない |
| Hyperlink/Fumetsu等 | artifact/追加対応待ち。v3購入必須へ自動昇格しない |
| TimeStop広域proof・source dimension・UOM/P vehicle | §20の未証明/未LOCK維持 |
| 未知Flight provider/全組合せ | generic attribution不可、§18限定 |
| 全TaCZ/gunpack・通信遅延 | NOT TESTED |
| 全GPU/shader/FE resource | NOT TESTED。B3外部ERROR15行も保全 |
| 全modpack/performance/大量entity | NOT TESTED |
| backpack auto-feed/独自機械 | 経路別統合が必要、全般対応claimなし |
| True Root部分/親OFF/死亡等 | 個別未LOCK、保持仕様から推定しない |

これらを隠さず、利用者承認済みv3の有限release scopeと区別する。
RCはこのscopeの完成判断で、全MOD/全機構の安全証明ではない。
Security scan結果や実行していない検査を根拠にしない。

## 32. 廃止・保留・将来境界

v2食技Lv自動skill取得、旧1食1count/既定200、旧高Nutrition無料Resistance/Fire/Root、旧chat skill UIは現行機能ではない。
旧Config key/alias/LegacyV2Backup・過去試験を互換証拠として残す。原データの由来不明effectを一括removeしない。
旧有限価格/Satisfaction/Ammo5SP等は履歴であり、新規購入へ使わない。

Break Realm Mastery20SPは登録済み・**IMPLEMENTATION_PENDING / 購入不可**、Adapter expansionはNOT IMPLEMENTED。
将来の正当damage通過という既存仕様を、直接削除/即死実装やv3全boss対応と読み替えない。
Bulwark、FOURTH BOOT、他保留機能は個別承認が必要。
**試作型機関弩は完全一致の開始指示まで監査・実装禁止**。本書で新設計/対象追加をしない。
今回のrelease完了を次version/公開サイト/CurseForge/新機能開始の自動承認にしない。

## 33. 検証履歴とdeveloper handoff

| queue / 関連段階 | 既存受入の要点 | 記録 |
|---|---|---|
| #1 | same-JVM isolation/source分離/cleanup | 共通計画§14.36 |
| #2 | UOM限定製品/actual movement/通常復帰/positive保存/new context/5 END/終了 | §14.55 |
| #3 | FE6 native70 unique＋Dream限定client | §14.56–14.57 |
| #4 | P/T購入readiness・server SP取引153等 | §14.58 |
| #5 | Pam present150/absent146・補足既知3ERROR受入 | §14.60–14.62 |
| #6 | Nutrition count/schema5・93unit・native/lifecycle | §14.63 |
| #7 | Flight exact FE/EL＋Mek/Avaritia追加、ownership | §14.65–14.67 |
| pre-#8 | legacy parity/cost2590/採石/不動/localization/command cleanup | §14.69–14.72 |
| #8 | 最終source114×2、全unit/build/check、shader426、最終Jar A3/B3 | §14.74 / TEST_PLAN§38 |
| #9 | 本書・static整合・同一Jar正式コピー・RC・GitHub | §14.75 / TEST_PLAN§39、Release Receipt |

原証拠は各文書のrun/hash付きリンクに残る。`build/verification`、world、screens、log、backupsはローカル証拠保全領域で**GitHubへ配布しない**。
GitHubではそれらへの歴史リンクはlocal evidence locatorであり、公開ダウンロード可能な証拠と称さない。
新しい本書/receiptのsource・docsリンクは公開treeで解決する。原FAIL/NOT RUNは消さない。

### ビルド引継ぎ

Java17 / Gradle8.1.1 / official1.20.1 mappings / Forge47.2.0 userdev。外部統合は47.4.0別環境。
既存正常ローカル環境ではlocal Gradle executable、既存`-g`cache、`--offline`で
`build foodHealingUnitTest check`を実行する。必要な変更がないrelease packagingで再実行しない。
GitHub sourceをcloneしただけでは外部compileOnly原物/cacheは付属しない。未知座標を発明して自動取得しない。

| 現行buildが参照するlocal原物 | 目的 |
|---|---|
| libs/tacz-1.20.1.jar（local参照1.1.7-hotfix2） | isolated TaCZ compile/API・承認時のみruntime |
| libs/TheTrialMonolith-1.20.1-Forge-1.4.9.jar | compileOnly |
| libs/l2hostility-2.5.19.jar / l2library-2.5.3.jar / l2complements-2.6.1.jar | compileOnly |
| build/l2-compile-only/Registrate.jar / l2serial.jar / l2damagetracker.jar | 承認同梱候補から得たcompile API |
| build/verification/fantasy-ending-20260920-204205/audit/artifacts/ | EndingLibrary/FE/Curios5.14.1 compileOnly原物 |
| build/verification/flight-mekanism-20260929-184800/audit/Mekanism-1.20.1-10.4.16.80.jar | compileOnly |
| build/verification/source-world-boot-20260906-172148/game/mods/curios-forge-5.12.0+1.20.1.jar | L2 fixture compileOnly |

パス/要件の実正本は[build.gradle](../build.gradle)、[gradle.properties](../gradle.properties)。
ローカル原物不足はbuild setup不足として扱い、fake class/要求version書換え/外部Jarcommitで回避しない。
main sourceに置かれたGameTest classesはjar task除外、独立sourceSet fixtureも製品に混入させない。

主要実装参照:
[Skills](../src/main/java/com/leva/foodhealing/FoodHealingSkills.java)、
[BaseStats](../src/main/java/com/leva/foodhealing/FoodHealingBaseStats.java)、
[Data](../src/main/java/com/leva/foodhealing/capability/ShokugiData.java)、
[Root](../src/main/java/com/leva/foodhealing/RootController.java)、
[Damage](../src/main/java/com/leva/foodhealing/DamageEventHandler.java)、
[Flight](../src/main/java/com/leva/foodhealing/FlightController.java)、
[Network](../src/main/java/com/leva/foodhealing/network/PacketHandler.java)、
[Compatibility](COMPATIBILITY_POLICY.md)。

## 34. Specification Maintenance Rule

v3.xの実装・修正・互換追加で挙動、費用、前提、数値、永続、network、scope、制限が変わる場合、
**同じ作業単位で本書を更新する**。sourceだけ変更して完了としない。
実装の現状と利用者LOCKの矛盾を見つけたら、勝手に期待値/仕様を緩めず、必要な判断と回帰範囲を分離する。
将来major versionは別仕様書を作り、本v3.x仕様と履歴を残す。

保守完了チェック:

1. registry ID/ja/en/maxLv/各価格/前提/default/toggle/effect/optionalを照合。
2. schema/protocol/Config/migration/保存・同期を更新。old rawとexactly onceを保全。
3. 対象Jar/hash/version、実装/自動/実client/未検証を分離し、変更影響に合う回帰を完了。
4. 新しい互換claimをexact artifact/経路へ限定。不在起動、所有権、他playerを保護。
5. 現在の要約・本書・README・receiptの数値を一致させ、古い一覧を現在の未完へ残さない。
6. 過去FAIL/NOT RUN/log/hashは履歴として保持。文書整理を新規ゲームPASSへ数えない。
7. 配布Jarに外部/fixture/ExampleMod/検証表示を混入させず、metadata/Mixin/refmap/reobf/hashを測定。
8. release実施範囲を超える公開/次version/保留機能を自動開始しない。
