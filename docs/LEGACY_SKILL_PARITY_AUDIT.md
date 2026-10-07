# LEGACY_SKILL_PARITY_AUDIT — v2.2.5 → v3.0.0

本文更新: 2026-09-30 00:07 JST

**PRE-#8 LEGACY EXISTING-SKILL PARITY AUDIT COMPLETE / PRE-#8 LEGACY SKILL PARITY FIX COMPLETE**。
現行parity/価格結果は§9、表示は[共通計画§14.71](MASTERY_IMPLEMENTATION_PREPARATION.md#player-facing-localization-cleanup)、旧commandの後続cleanupは[§14.72](MASTERY_IMPLEMENTATION_PREPARATION.md#release-command-verification-cleanup)、過去parity結果は[§8](#parity-fix-result)と[共通計画§14.69](MASTERY_IMPLEMENTATION_PREPARATION.md#legacy-skill-parity-fix-completed)。利用者判断2問は解決済み、4技能の実装・指定自動回帰・最終Jar監査を完了。#1–#7 COMPLETE／#8・#9 NOT STARTED／RC=NO、Flight/P/T PURCHASE READYを維持。

**§1～§7は2026-09-29 20:36 JSTの静的監査履歴**。「現行」「今回」「判断待ち」「次工程」は監査当時の意味で、現在の残件ではない。当時はproduction/test/Jar変更・新build/test/game0だった事実と、Lv1-only/防具限定/direct hurt迂回の比較結果を保存する。

## 1. 基準・証拠・数え方

- 旧基準は公開[commit 7ccfaa087fadcb4d48e4b1e98f01616f5cc18df1](https://github.com/leva3896/food-healing-mod/tree/7ccfaa087fadcb4d48e4b1e98f01616f5cc18df1)。候補を無条件採用せず、`gradle.properties`の**mod_version=2.2.5 / mod_id=foodhealing / MC1.20.1 / Forge47.2.0**、`FoodHealingMod.MODID`、resources/lang/Mixin、同commit収録仕様を照合した。
- 既存9/5 source ZIP2件はいずれもmetadataが3.0.0なので不採用。公開commitのsourceを監査rootだけへ取得。Git treeのblob SHA-1と**35基準ファイル**を照合。[identity](../build/verification/legacy-parity-20260929-202500/audit/baseline-identity.json)。ローカル`.git`なし。git diffを実行したとは記録しない。
- 旧仕様書のファイル名・本文は**v2.2.0**のままv2.2.5 commitに収録されている。[旧仕様][O-SPEC]と[旧日本語lang][O-LANG]を「当該commitに同梱された説明」として扱い、2.2.5 actual Javaより優先しない。旧チャットGUIはcommand一覧/詳細リンクで、独立スキル画面はない。
- 旧Java **25ファイル全体**を機械検索。**17 skill/toggle群、31 unlock/stage値、literal `isSkillDisabled`効果参照19箇所**（17 skill本体＋満足感に結合したTaCZ節約/熱2箇所）。commandの可変key照会2箇所とinterface/getterはこの19に含めない。31はLv1–20・30・100・991–999で、31個の独立購入nodeという意味ではない。
- [機械検索JSON](../build/verification/legacy-parity-20260929-202500/audit/legacy-mechanical-inventory.json) / [行付き全結果](../build/verification/legacy-parity-20260929-202500/audit/legacy-mechanical-inventory.txt)：threshold、getLevel、toggle、translation、config、Mixin、L2/durability等236行。下表がthreshold→call-site→effectの意味付け。level表外の高Nutrition/Guts/global/TaCZ/SuperbWarfare等は§5に別記する。
- 保存済み旧Jarも補助照合：**80,331 bytes / SHA256 5BAC83E55419D96849823D7A7BB0933E01600564E13739992E43782C925A6A8C**。[identity](../build/verification/legacy-parity-20260929-202500/audit/legacy-jar-identity.json)。旧Damage/Tick/Food/Loot/ItemStackのjavapで主要分岐・倍率を裏付ける。全旧Jarの再現buildを証明したものではない。
- 開始・終了製品は **350,807 bytes / 269 entries / C106B5B67F63821289B0BB51F77E165CB7D1E0684F6BFBE228A8DF3E9851F7BC / protocol7 / schema5**。主要14 classをjavapで確認し、残存reobf出力・Avaritia前Jarとのclass byte一致、対応14 sourceの前回build前hash一致も確認。[source/Jar静的照合](../build/verification/legacy-parity-20260929-202500/audit/source-jar-static-comparison.json)。今回比較の範囲で**CURRENT SOURCE / FINAL JAR MISMATCHなし**。新しいcompileや全sourceの再現buildではない。

## 2. 判定の読み方・共通条件

下表のPARITYは**明記した効果の意味**についての判定。旧の自動解放からSP購入、表示文字列keyからresource ID、server authority/保存安全化への変更は全行共通のINTENTIONAL_V3_REDESIGNである。旧unsafe処理を復元する意味ではない。

- 旧全17群：食義int Lvが閾値以上・日本語toggleがDisabledSkills集合にないとON。NBTは`ShokugiLevel`/`EatCount`/`DisabledSkills`、commandが本人のcapを変更・同期。[旧保存][O-DATA] / [旧command][O-CMD]。技能固有SPや独立skill levelはない。
- 現在：IDは全て`foodhealing:`接頭辞。表のLv/SPは正常fresh・正常移行済みの**正式購入可能値**。[registry/資格判定][N-SKILLS] / [ID・旧alias][N-IDS]。通常は保存AcquiredSkills/DisabledSkillsを参照し、効果なし/OFFではそのskillの新規処理をしない。migration-pending fallbackや人工的な高level注入は正式購入の証拠にしない。
- config欄が「なし」の効果は確率/倍率自体にconfig依存なし。旧自動解放の時期には旧`shokugiLevelUpRequirement`が共通で影響。新取得/SP・toggleはserver同期/保存対象、被弾・drop・耐久の計算自体は通常永続stateではない。
- `E`は[ExistingSkillsGameTests][T-E]、`C`は[CombatGameTests][T-C]、`Z`は[PriorityZeroGameTests][T-Z]、`F`は[FoodProductionGameTests][T-F]。表は**実在する既存試験の期待値**を記す。直前の[vanilla84/84](../build/verification/flight-avaritia-20260929-193800/audit/vanilla-final.log) / [TaCZ84/84](../build/verification/flight-avaritia-20260929-193800/audit/tacz-final.log)を保持し、未収録のparity境界までPASSに拡張しない。

## 3. 全17群の4層比較matrix

| legacy skill | old level/stage・toggle | old actual | old documented | v3 spec・ID/maxLevel/SP | v3 actual | current test | classification | decision needed |
|---|---|---|---|---|---|---|---|---|
| 耐火の心得 | 1 / `耐火` | Tick END、Fire Resistance amp0 | 常時耐火・lava/fire無効 | `fire_resistance` /1/1、managed effect | amp0＋IS_FIRE cancel、外部effect保持 | E managedSkills… / fastEatingAndDefensive… | PARITY（耐火goal）＋INTENTIONAL_V3_REDESIGN（ownership/直接防御） | なし |
| 水月と暗視の心得 | 2 / `水月と暗視` | Water Breathing/Night Vision amp0 | 常時両effect | `water_night_vision` /1/1 | 両amp0、owned refreshのみ | E managedSkills… | PARITY（効果）＋INTENTIONAL_V3_REDESIGN（期限/所有権） | なし |
| 早食いⅠ | 3 / `早食い` | foodのStart duration整数÷2 | 全食料半分 | `fast_eating` /1/1、半減 | food限定、max(1,duration/2)、LOWEST | E fastEatingAndDefensive…32→16/OFF32 | PARITY（通常food）＋LEGACY_BUG_INTENTIONALLY_NOT_RESTORED（0tick化） | 通常範囲なし。custom duration全版は未保証 |
| 軽業の心得 | 4 / `軽業` | LivingDamage、exact FALL cancel | 落下0 | `acrobatics` /1/1 | same exact FALL cancel、serverのみ | E fastEatingAndDefensive…ON/OFF | PARITY | なし |
| 炎の加護 | 5 / `炎` | isOnFire時**全incoming**×0.70 | 炎上中全被ダメージ30%減 | `flame_blessing` /1/2 | 同じ。fire source限定ではない | E generic100→70/OFF100 | PARITY | なし |
| 爆破耐性の心得 | 6 / `爆破耐性` | IS_EXPLOSION×0.10 | TNT/creeper等90%減 | `explosion_resistance` /1/2 | 同tag、同倍率 | E explosion100→10/OFF100 | PARITY | なし |
| 浄化の心得 | 7 / `浄化` | server ENDでHARMFULをremove | 毎tickデバフ消去 | `purification` /1/3 | 同じ。新規付与そのものは防がない | E poison除去・speed保持・OFF | PARITY | なし。極意とは別 |
| 豊穣の心得 | 8 / `豊穣` | crafted foodを後から同数Inventory.add/drop | crafting完成品×2 | `food_production_mastery` /1/4、§3/§16安全×2＋拡張 | transactionで×2、機械owner経路、Pam別限定Adapter | F 7case・Pam既存証拠 | INTENTIONAL_V3_REDESIGN（SUPERSET）＋LEGACY_BUG_INTENTIONALLY_NOT_RESTORED | なし。FD/独自食品全面互換は別未確認 |
| 屠殺の心得 | 9 / `屠殺` | source Player、target非Playerかつcategory≠MONSTER、最終drop×3 | 友好Mob、Looting重複 | `slaughter` /1/2、非敵対/Player絶対除外 | 同predicate/×3、longでoverflow安全化 | Z playerDeathDrops…cow2→6、OFF、Player死 | PARITY（actual対象/倍率） | なし。旧「友好」は実際にはcategory判定 |
| 火事場力解放 | 10 / `火事場力` | HP≤config40%でoutgoing×config2 | 同条件・全与ダメージ | `heroics` /5/1,2,2,3,4；`true_heroics` /1/100 | 基礎×2を包含、上位×2.5/3/4/5＋防御・真追加 | C heroicsUsesFinalLevelValues…他 | INTENTIONAL_V3_REDESIGN | なし。旧値への巻戻し禁止 |
| 満足感Ⅰ–Ⅲ | 11/12/13 / `満足感` | food25/50/75%＋別TaCZ11–20 coupling | lang/specはTaCZ説明中心、food確率説明不足 | `satisfaction` /3/1,2,3、food-only | food確率維持、use1回1roll、原stack非消費 | E satisfactionUsesOneRoll… | PARITY（food確率）＋INTENTIONAL_V3_REDESIGN（TaCZ分離/transaction） | なし |
| 採取の心得Ⅰ–Ⅲ | 14/15/16 / `採取` | ×2/4/6、別loot抽選＋追加spawn | Fortune後×2/4/6 | `gathering` /1/2、Lv1-only記述再監査中 | 正規購入では×2だけ。×4/6分岐は残存 | E gathering…clay4→8/OFF4のみ | LIKELY_UNINTENTIONAL_REGRESSION（上位到達不可）＋LEGACY_BUG_INTENTIONALLY_NOT_RESTORED（re-roll/name判定） | 段階・費用方針 |
| 不壊の心得Ⅰ–Ⅲ | 17/18/19 / `不壊` | 全標準hurt入口、90/95/29⁄30% | 「防具」＋L2完全無効 | `unbreaking` /1/3、Lv1-only再監査中 | 正規購入90%だけ。計算は全damageable、95/29⁄30分岐残存 | E pickaxe .899成功/.90失敗/OFF/RNG | LIKELY_UNINTENTIONAL_REGRESSION（上位・直接hurt経路）＋DOCUMENTATION_IMPLEMENTATION_MISMATCH（旧L2説明） | 段階/費用・標準耐久対象方針 |
| 防具の極意 | 20 / `防具の極意` | **armor checkなし**、amount>1を1へ | 防具1被弾上限1、TaCZ100%と併記 | `armor_mastery` /1/4。今回LOCKは標準damageable全般 | **ArmorItem限定**、直接hurtも未接続 | E chestplate5→1 / **pickaxe5→5** | LIKELY_UNINTENTIONAL_REGRESSION — **CONFIRMED V3 NARROWING REGRESSION** | 方向LOCK済み。質問不要、修正は次工程 |
| 飛翔の心得 | 30 / `飛翔` | mayfly付与、OFFでforeignもfalse | Survival creative flight | `flight` /1/2、SPEC§18exact契約 | 自己token・supported provider・DENY/UNKNOWN保持 | Flight unit88、FE/Mek/Avaritia既報 | PARITY（goal）＋INTENTIONAL_V3_REDESIGN（ownership） | なし。既存PASSを再試験しない |
| 金剛の心得 | 100 / `金剛` | Resistance amp3常時 | Resistance IV | `kongo` /1/5、managed Resistance IV | 同等80%goal、外部effect保持＋不足分防御 | E managedSkills… / C reductions | PARITY（IV goal）＋INTENTIONAL_V3_REDESIGN（補完/ownership） | なし。高Nutrition無料bonusとは別 |
| 追撃の心得 極 | 991–999 / `追撃` | min(9,Lv−990) extra1～9、同source/currentamount | Lv毎に1回、999で9追撃 | `pursuit` /1/4、Lv1-only再監査中 | 正規購入extra1だけ。min(9,enabledLevel)残存、iframe復元 | E pursuitAddsOneHit…1hit/復元/OFF | LIKELY_UNINTENTIONAL_REGRESSION（上位8段階）＋LEGACY_BUG_INTENTIONALLY_NOT_RESTORED（iframe漏れ） | 段階・費用方針 |

## 4. Call-site・条件・順序・OFF・保存の詳細

### 4.1 耐火・水月暗視・金剛（3群）

旧[ShokugiTickHandler.onPlayerTick][O-TICK]はserver Player END、閾値1/2/100＋各toggle。`applyPermanentEffect`が不存在/低amp/**残400tick未満**で10,000tickをaddする。これは毎tick再付与ではなく毎tick条件評価。耐火・水月・暗視amp0、金剛amp3。効果自体のlav/fire、呼吸、視覚、被ダメージはvanillaに委ねる。config/外部MOD必須条件なし。

旧OFFは同amp・duration>8000という推定でremove。旧20分高Nutrition Fire/Resistanceや同値の他MOD効果も条件に入る。強いeffectへの`addEffect`成否/結合はvanilla任せで、外部所有を証明していない。これを復元しない。

現[ShokugiTickHandler.applyManagedEffect][N-TICK]はserver END、**240tick付与・残200tick未満refresh**。`foodhealing:owned_effects`のamplifier/Untilと実effectを比較（±3tick）、markerなしの外部effectは弱い/強いを問わず上書きしない。OFFはmarkerだけ消し、effectを強制removeせず自然失効（自身の残存は最大約240tick）。再ONも他人の残存を奪わない。同値/同期限の外部置換完全識別までは保証していない（SPEC§12.3既知境界）。markerと効果の保存は進行度とは別。

耐火の現[DamageEventHandler.onLivingDamage][N-DAMAGE]は取得ON＋`IS_FIRE`でcancel。効果付与前/外部effectを保持する間も耐火goalを満たす補助であり、fire文字列判定ではない。金剛の`kongoDamageRemaining`は`BYPASSES_EFFECTS`を除外、Resistance amp≥3なら追加軽減なし、不在なら×.20、amp0–2なら既存残率に対し`.20/existingRemaining`。Resistanceと二重80%にはしない。外部より強いeffectを下げない。各canonical OFFは追加防御も停止するが、既存MobEffectの自然失効までのvanilla効果は残り得る。

### 4.2 早食い・満腹経路

旧[FoodHealingHandler.onItemUseStart][O-FOOD]はPlayer＋`getFoodProperties(stack,player)!=null`＋Lv≥3＋早食いON、event duration整数÷2。食品以外の弓/盾/飲用一般には適用しない（飲用でもFoodPropertiesがあれば対象）。odd31→15、1→0。client/server side guardなし。旧[AlwaysEatHandler][O-ALWAYS]はfoodのRightClickで`startUsingItem`を直接呼び、旧仕様文の「HP減少時のみ」と違ってHP条件なし。

現[同Start][N-FOOD]は食品判定同じ、EventPriority.LOWEST、`max(1,duration/2)`で31→15/1→1。既存modのevent duration変更後へ適用する順序で、custom useの独立override全面互換ではない。Start自体は両side、Satisfaction decisionはServerPlayerのみ、Finish mutationはserverのみ。OFFはduration不変。食事進行の永続値と使用中transactionは分離。

満腹許可は現[ItemMixin][N-ITEM]の通常Item.use→`player.canEat(true)`。旧直接開始を戻さずvanilla/Forgeのuse経路を保持する。player-aware FoodPropertiesを使うmodded foodは候補、独自useメソッド迂回はAdapterが必要。現行32→16/OFFの試験はあり、odd/custom duration全組合せの実試験は未確認。

### 4.3 軽業・炎・爆破（3群）

旧[DamageEventHandler.onLivingDamage][O-DAMAGE] HIGHEST、target Playerのcapを使う。Lv4 exact `DamageTypes.FALL` cancel（amountを単に0へ置く方式ではない）、Lv6 `DamageTypeTags.IS_EXPLOSION`×.10、Lv5 `player.isOnFire()`×.70、続いてglobal Lv軽減。ローカル順は**fall→explosion→flame→global**。このhandlerにside guardなし（通常damage gameplayはserver）。modded fall-like名や無tag爆発は自動対象ではない。vanilla armor/enchant等の後段LivingDamage額に作用し、boot効果/他MODとの最終合成順全般はevent priorityにも依存。

現[N-DAMAGE]はserverのみ、同fall判定・cancel、耐火IS_FIRE cancel追加、explosion→flame→Base DR→High Difficulty DR→Heroics DR→Kongo不足分。炎は燃焼中のgenericを含む**全incoming**、火ダメージ限定化なし。OFF/未取得では該当層を掛けない。configなし・永続追加値なし。modded sourceは正式damage type/tagで決まり、名称類似で広げない。

### 4.4 通常浄化

旧[O-TICK]と現[N-TICK]の`onPlayerTick`でserver ENDにHARMFULカテゴリを列挙→remove。Lv≥7から取得Lv1へ変えた以外、trigger/本人/対象分類は同じ。beneficial/neutral保持、HARMFULならvanilla/外部にかかわらず対象。これは仕様上のデバフ除去であり「owned effectだけ除去」と取り違えない。OFFは新規除去0、既に除去したeffectを復元しない。tick前に作用した一瞬の効果を巻き戻さず、L2新規付与防止/P極意を代行しない。config/技能固有永続状態なし。

### 4.5 豊穣→食料生産の極意

旧[FoodHealingHandler.onItemCrafted][O-FOOD]：ServerPlayer、非empty craftedのFoodPropertiesあり、Lv≥8/豊穣ONなら**同数copyを後付けInventory.add、余りdrop**。表示結果そのものを倍にする実装ではない。材料消費とresult受取の一貫性・shift/再通知の安全性をこの仕組みだけでは保証できない。configなし。

現[FoodProductionTransactions][N-PRODUCTION]＋ResultSlot/ContainerMenu等Mixinは、正常結果transactionで×2、材料1recipe、NBT/container/overflowを保全する。手動furnace/smoker、campfire owner準備・保存経路、Pam exact果実収穫等へ拡張。自動hopperは×1、共有機械に恒久的な「最後に触ったplayer」の権利を付けない。campfireは設置時の資格booleanをslotごとに正常保存し、完了時に二重配布しない（manual extraction方式と同一とはしない）。各callerは[検索可能なproduction一覧](../build/verification/legacy-parity-20260929-202500/audit/current-evidence-index.txt)を参照。

**INTENTIONAL_V3_REDESIGN / SUPERSET**は既存対応範囲のvisible×2と安全な追加経路について。FD独自機械・全modded foodを実統合済みとはしない。`planPersonalCraftingResult`後の`planEligibleFoodResult`は`getFoodProperties(null)`も確認するため、player依存food全てが旧と同一という保証はない（INSUFFICIENT_EVIDENCE、具体artifact未特定の範囲外）。ON/OFF/未取得は資格を確定するtransaction入口に反映（campfireは設置時の資格を完了まで保持するため、後からOFFにしてもその予約済み出力資格を再判定しない）。可逆クラフトによる増加は**既知かつ許容、バグ修正対象外**。死亡drop/replay/desyncの意図しない重複は別。

### 4.6 屠殺

旧[O-LOOT]／現[N-LOOT] `onLivingDrops`：source entityがPlayer、victimは非PlayerかつMobCategory≠MONSTER。同じ判定なので動物だけでなく村人/蝙蝠/イカ等、分類上非MONSTERを含む。中立Mobの実際の敵対状態では判定しない。loot/装備由来を区別せず既存event ItemEntityの全stack×3。Looting済みdropへ掛けるため倍率は重なる。他GLMで既に増えたitemもevent時点の量へ×3、**他handlerの後から追加されたdropまで一律保証するものではない**。旧にもPlayer除外が実在し、墓dup修正は維持される。

旧int count*3から現long計算へ変更、上限stackで分割しmetadata copy。どちらもこのmethodに独立side guardはなく、通常serverのLivingDrops発火へ依存。OFF/未取得は変更0、効果固有保存stateなし、configなし。既存cow/OFF/Player実死亡drop試験はあるが、全友好分類/実Looting/他GLM順の網羅PASSへ広げない。

### 4.7 採取 — 上位段階と対象の差

旧[O-LOOT] `onBlockBreak`：ServerLevelのみ、Lv14→2、15→4、16以上→6。対象はregistry **pathに`ore`を含む** OR DropExperienceBlock、CropBlock/NetherWart/Cocoa/SweetBerryBush、pathにglowstone/clay/melon/pumpkinを含むblock。ドロップitem名自体は絞らない。TOOL（Fortuneを含む）/THIS_ENTITY/ORIGIN/BLOCK_STATEで**別の`state.getDrops`を抽選し、(倍率−1)分をspawn**、通常breakの1倍分も別途落ちる。説明の「同じFortune抽選後の最終listを正確に×N」とactualは一致せず、独立抽選なので総数が単一roll×Nと一致しない場合がある。

現[GatheringLootModifier.doApply][N-GATHER]はLootContextにBLOCK_STATE/Playerが必要、`forge:ores`tag OR DropExperienceBlock、同crop class群、vanilla GLOWSTONE/CLAY/MELON/PUMPKINをexact判定。対象の**生成済み全stack**へ×2/4/6、NBT copy・long・stack分割。再抽選/spawnなし。Fortuneはnative loot生成時に処理済み。他GLMとは登録順のlist合成で、全他MODの最後に実行する保証はない。

path substring廃止は旧誤検知/タグ未整備問題への意図した安全化（AUDIT B12/B13）。ただしmodded clay等を名前だけで拾っていた旧範囲は狭まり、未受領の全blockを保証しない。上位式は存在するが、registryがmaxLevel1/2SPのため正常購入では**×4/×6が到達不可**。pending fallbackの3値を根拠にPARITYとしない。OFFはGLM list不変。既存試験はclay4→8/OFF4で、3段階保持を検証していない。

<a id="durability-parity-details"></a>
### 4.8 不壊／防具の極意／vanilla耐久力 — 別skillとしての監査

旧[ItemStackMixin.foodhealing$modifyHurtAmount][O-ITEM]：`@ModifyVariable(method="hurt", at=HEAD,argsOnly=true,ordinal=0)`、引数 `(int amount,int originalAmount,RandomSource random,ServerPlayer player)`、player nonnullのcapを参照。**ArmorItem/装備slot判定なし**。Lv20かつON/amount>1を先に1へ、その後Lv17/18/19かつ不壊ON/amount>0でplayer RNGを1回、`<.9/.95/29f÷30f`なら0。通常はstandard damageable ItemStack全般に効く。HEADなので非damageable直呼びでも旧RNG消費が起こり得るが、vanillaが後で耐久対象外として返す。この副作用は復元しない。

現[DurabilityTransactions.adjustDamage][N-DURABILITY]はamount≤0/非damageableならそのまま、ArmorMasteryだけ**instanceof ArmorItem**、次に不壊のenabledLevelによる同3確率。不壊自体はpickaxe/toolも対象。したがって「現在の不壊も防具限定」は事実ではない。正式購入では不壊max1なので95%/29⁄30には到達できない。乱数は未取得/OFF/対象外/amount0で消費せず、NaN/負値を成功としない。

現[ItemStackMixin.foodhealing$applyEquipmentDurabilitySkills][N-ITEMSTACK]は`hurtAndBreak`中の`hurt(int,RandomSource,ServerPlayer)`だけRedirectし、調整後に本来の`hurt`を1回呼ぶ。**直接`stack.hurt(amount,rng,serverPlayer)`はFH調整を通らない**。これは型とは別の経路縮小。ArmorMasteryは今回LOCKの標準耐久契約に未達、不壊は対象範囲判断に含める。playerがnullの環境/energy/customcap/setDamageValue直書きを勝手に対象へ拡大しない。

#### 実順序（推測ではなくdescriptor＋バイトコード）

[native ItemStack bytecode](../build/verification/legacy-parity-20260929-202500/audit/native-ItemStack.txt)では`hurtAndBreak`がserver/instabuild/damageableを確認→**Item.damageItem（Forge/custom item callback）**→`hurt`。`hurt`内はdamageable確認→Enchantments.UNBREAKING→DigDurabilityEnchantment.shouldIgnoreDurabilityDropを残amount分→setDamageValue→break判定。旧HEAD・現Redirectの[annotation証拠](../build/verification/legacy-parity-20260929-202500/audit/current-ItemStackMixin-annotations.txt)も確認。

| 経路 | 実順序・amount100の例 |
|---|---|
| 旧standard hurt（有資格） | ArmorMastery ON:100→1 → 不壊成功0/失敗1 → vanilla耐久力は残1だけ → 実damage |
| 現hurtAndBreakのarmor | 外部damageItem後のamount100→ArmorMastery1→不壊0/1→vanilla。**順序PARITY** |
| 現hurtAndBreakのtool/weapon | ArmorMastery capなし：100→不壊成功0/失敗100→vanilla。**型縮小regression** |
| 現direct hurt | FH両skillなし→vanillaが100に作用。**入口縮小** |

ArmorMastery OFFならcapなし、不壊OFFならFH抽選なし。amount0→0/1→1は**cap段階**の契約で、その後の不壊/enchantによって1→0になり得る。[native DigDurabilityEnchantment](../build/verification/legacy-parity-20260929-202500/audit/native-DigDurabilityEnchantment.txt)はArmorItemで60%の「無視しない」分岐、それ以外/残分で`nextInt(level+1)>0`。FH確率と混同せず、0/I/IIIを将来別途受入対象にする。旧はplayer RNG、現はhurtに渡されたRNG；通常hurtAndBreakでは同じliving.getRandom。任意direct-call RNGまで同一stream保証はしない。

**Armor Mastery利用者LOCK（今回）：** 有資格playerに帰属するstandard damageable ItemStack全般でcap max1。armor/weapon/tool/bow/crossbow/modded標準経路を含む。防具限定化は意図した再設計ではなく事故。energy/customcap/標準処理完全迂回/外部Reflection修復は自動保証外。正式仕様は[SPEC§19](SPEC.md#legacy-armor-mastery-parity-lock)。今回実装しない。

既存Eの`durabilitySkillsAreScopedAndUseExactSaveChanceBoundary`は**tool5→5を成功条件**としている。TEST_PLAN§12のtool/weapon項目だけではこの期待値は分からず、「全対象が保護される」との解釈や今回LOCKと矛盾する。既存84/84は当時の期待の成功として保全し、新LOCK適合PASSへ転記しない。

#### 旧L2説明

全25 Java、2 Mixin登録、config/dependencyを確認し、**L2 class/API、conditional L2 Mixin、custom耐久write interceptionはなし**。専用classへの結合があるのはTaCZ。旧lang/同梱仕様はL2防具破壊完全無効を主張するが、generic確率hurtだけでは100%無効や`setDamageValue`/独自capの遮断を証明できない。

判定：**DOCUMENTED INTENT / ACTUAL IMPLEMENTATION NOT ESTABLISHED**（DOCUMENTATION_IMPLEMENTATION_MISMATCH、当時の実L2全版動作はINSUFFICIENT_EVIDENCE）。現P極意のcorrosion/erosion対応は別技能/条件/費用であり、旧不壊の実装証拠に付け替えない。COMPATIBILITY_POLICYへ新claimを追加しない。

### 4.9 追撃

旧[O-DAMAGE] `onLivingDamage`：source Player、Lv≥991、追撃ON、`min(9,level−990)`。追撃開始時の**LivingDamageEvent amount**と**同DamageSource**でtarget.hurtを呼ぶ。victimはLivingEntity（Playerも排除しない）。ThreadLocal IS_PURSUITで再帰追撃とonLivingHurtのFH outgoing再計算を抑止、各hit前にinvulnerableTime=0。生存中だけ呼ぶが旧loopは死亡後も残iterationのalive検査を行う。finallyでguardは戻すが**元invulnerableTimeを復元しない**。

現[N-DAMAGE] `applyPursuitHits`：serverのみ・original event非cancel、`min(9,enabledLevel)`、同source/現在amount、alive時のみloop、finallyで**元invulnerableTime＋guard復元**。正式購入max1のためextra2～9到達不可。OFFでは追加hit0。各native hurtはvanilla armor/Resistanceや他MOD処理へ再入する。**FH outgoingはguardで再乗算しないが、victim側LivingDamageの軽減・Rootや外部handlerは再び動く**。従って「最初の最終HP減少と完全に同量の9hit」まで保証する設計ではなく、この構造は旧にも存在する。configなし、skill/toggle以外の保存stateなし。既存試験はcowに追加1回とiframe13復元/OFFで、全上位/死中断/全倍率網羅ではない。

### 4.10 満足感とTaCZ

旧[FoodHealingHandler.onItemUseFinish][O-FOOD]：FoodPropertiesあり、Lv11/12/13→25/50/75%、満足感ONで**消費後copy refund/result置換**。Finish内でside guardの外にありclient mutationも可能。食事countのlevel-up後に判定するのでその食事で11へ上がれば同Finishで資格を得る。器置換・最後の1個・NBT/stack整合の危険は旧実装の証拠であり復元しない。

現[SatisfactionTransactions][N-SAT]：server Startで1roll/stack identity/期限付きdecision→finish時にconsume、元Item.finishUsingItemをcopyへ1回、原stack保持。fallback custom feederはserver判定できてもclient予測には別Adapterが必要。clientは同期decisionに基づく表示/予測、OFFは新decisionで節約0（開始済みtransactionを途中のtoggleで再rollしない）。food25/50/75%維持、ammo/heat効果0。

旧[TaCZGunScriptMixin][O-TACZ]はserver shooter Player・**満足感toggle**・食義11以上、`min((level−10)*.1,1)`。magazine/chamber/inventoryのRedirectそれぞれで判定し、shot単位の1rollではない。shootOnce RETURNでgunId pathにminigunが含まれるとheat0/overheatLocked false、Lv11から。Lv14以降のlangに節約率が表示されても採取/不壊/ArmorMasteryのtoggleは参照しない。

現`foodhealing:tacz_ammo_conservation` Lv1–10/各5SP、approved adapter限定、1shot transaction、熱遷移/bolt/replay等は既存LOCKへ独立化済み。**INTENTIONAL_V3_REDESIGN**。ArmorMastery Lv20＝弾100%という旧同時解放を因果関係として戻さない。旧TaCZ direct classの不在ロード危険もLEGACY_BUG_INTENTIONALLY_NOT_RESTORED。

### 4.11 火事場

旧[O-DAMAGE] `onLivingHurt`：source Player、Lv10以上/ON、HP≤maxHP×heroicsThreshold(default .4)、heroicsMultiplier(default2)、optional/global倍率の後に乗算。旧Armor/Toughness倍・Heroics軽減はこのexact sourceにない。Configでthreshold0–1/multiplier1–100、HUDはthreshold表示で取得条件との完全一致は別。

現[HeroicsController][N-HEROICS]：正式取得は固定40%以下、Lv1–5 outgoing2/2.5/3/4/5、effectiveArmor/Toughness2/4/8/16/32、DR10/20/30/40/50%。Trueは80%以下、outgoing20/armor64/DR99%、normalとの累乗なし。旧既定×2をLv1が包含し防御を追加したLOCK済み再設計。高HP/OFFで適用せず、他MODmodifierを保持し旧FH ownedUUIDだけ整理する。pending fallbackの旧config参照は正式購入仕様とは別。SP12＋True100、効果stateを外部属性の上書き保存で代用しない。再質問不要。

### 4.12 飛翔

旧[O-TICK]：server END、Lv30/ONでmayflyがfalseならtrue＋abilities sync。OFF/資格なし、非Creative/Spectatorなら**誰が付与したmayflyか問わずmayfly/flying=false**。configなし。skillON/OFFは保存、他provider ownership tokenなし。

現[SPEC§18](SPEC.md#flight-exact-provider-contract)と`FlightController/FlightGrantPolicy`：正常ServerPlayer/取得Lv1/ON、自己false→true時だけsession token、native modes保全、exact FE/EL/Mek/Avaritia positiveをread-only照会、EL明示DENY優先、unknown非破壊、actual変化時のみsync。tokenはNBT保存しない。Survival creative-style goalは同じだがforeign revokeは意図的修正。今回既存FE/Mek/Avaritia統合を再実行せず、generic/all lifecycleの新PASSは付けない。

## 5. Level表外・基礎能力の対応

| old能力・actual call-site | old actual / documented | v3移行先・actual・既存証拠 | 分類 |
|---|---|---|---|
| 高Nutrition無料bonus / [O-FOOD].Finish | defaultNutrition≥19でResistanceIV＋Fire Resistance、1200秒。config可。旧lang/specに強力bonus記載 | [SPEC§12](SPEC.md#legacy-high-nutrition-retirement)で廃止LOCK。互換config宣言だけ残りgrant0。Nutrition高値・外部effect保持の既存NutritionGameTestsあり | INTENTIONAL_V3_REDESIGN。通常耐火/金剛を無料迂回させない |
| Guts / [O-FOOD].Finish・[旧HungerChangeHandler][O-HUNGER] | defaultNutrition19食事または非food FoodLevel増分≥19で5秒GUTS。Damage/Death/LivingTick/PlayerENDでHP1・deathTime修復、effect所持LivingEntityを広く保護。config可、skill toggleなし | `guts` Lv1–5 SP1/1/2/2/3、[RootController][N-ROOT]：食事Nutrition18/固定15秒累積、2/4/6/10/15秒・CD20/15/10/5/0。True20SP/Root5、予約1回。Damage/Death/HP0保護goalを正常server本人・active期限へ限定。C rootProtects…、TrueRoot8case・限定実client既報 | INTENTIONAL_V3_REDESIGN。非foodのRoot発火は復元しない。部分/死亡等未決定は勝手にLOCKしない |
| global outgoing / [O-DAMAGE].onLivingHurt | sourcePlayer、Lv>0で1+Lv*.1、TaCZ/SW後・Heroics前、toggleなし | [FoodHealingBaseStats][N-STATS] `outgoingDamageMultiplier`はSP購入base points*.1（1SP/回）。pendingだけlegacyLv。finite/saturation安全化、combat/unit既存 | INTENTIONAL_V3_REDESIGN、係数goalは移管 |
| incoming / [O-DAMAGE].onLivingDamage | Player、1−min(.99,Lv*.01)、flame/explosion後 | base linear99まで1%/SP、その後0.1^n、別高難度DRと乗算。C normalAndHighDifficulty… / extreme… | INTENTIONAL_V3_REDESIGN、level無料防御は復元しない |
| TaCZ専用damage / [O-DAMAGE].onLivingHurt | directEntity class名contains `tacz.guns.entity`、Lv>0、1+Lv*.01、global前 | SP TaCZ base+1%/1SP、現namespace predicate、global→TaCZbase→Ammo Lv*5%→Heroics。source Player。exact判定/順序は現compat LOCK。乗算は通常域同値でも丸め/飽和位置の差を隠さない | INTENTIONAL_V3_REDESIGN、TaCZ専用旧式をSatisfactionへ戻さない |
| SuperbWarfare / [O-DAMAGE].onLivingHurt | direct class名contains superbwarfare、1+Lv*1、TaCZ後→global→Heroics | approved0.8.9.1 exactProjectileEntity/gunfire4種、normal canonical食義Lvで**同式**→globalSP→Heroics。pending fallback別分岐。実弾17＋negative3・限定client既報 | PARITY（式）＋INTENTIONAL_V3_REDESIGN（安全な限定経路）。全爆発/搭乗兵器へ転記しない |
| food回復/満腹/非food増分 / [O-FOOD]・[O-HUNGER]・[O-ALWAYS] | Nutrition×config2 heal。2tick時刻推定で重複抑止。old food1回count1/1000でLv↑、Lv1000cap | 正式food transaction/非food正deltaで回復とNutrition単位count、2000単位/SP、非foodでRoot0。SPEC§7/14.2、Nutrition existing8case等。満腹通常経路を利用 | INTENTIONAL_V3_REDESIGN＋LEGACY_BUG_INTENTIONALLY_NOT_RESTORED（二重/時刻推定） |
| Food Diversity / 旧FoodDiversityHandler | registry食品種類、既定5種ごと+2HP、capability。旧maxHP上限global Reflection/クローン不備は別問題 | FoodDiversity既存server exactly-once/owned modifier・保存維持。回復/食義/Rootとは独立、Z persistence/food等 | PARITY（種類ボーナス）＋LEGACY_BUG_INTENTIONALLY_NOT_RESTORED（属性上限/保存破壊） |

新SP statsの費用・係数、Root/TrueRoot、Heroics/TrueHeroics、Ammo仕様は既存LOCKを維持し、parityの名目で旧unsafe経路へ戻さない。

## 6. 結論と最小判断

### A. 確認できたPARITY

早食い通常food半減、軽業exactFALL、炎の燃焼中全incoming×.70、爆発tag×.10、通常浄化HARMFUL後消去、屠殺非Player/non-MONSTER×3、満足感food25/50/75。耐火/水月暗視/金剛は主効果、FlightはSurvival飛行goal、SWは専用式を維持。ownership/期限・取得方式まで旧とbyte同一という意味ではない。

### B. 意図的再設計・戻さない旧不具合

SP tree/respec/resource IDs/server authority、food transaction・安全GLM、Satisfaction/TaCZ分離、FoodProduction SUPERSET、Heroics/Root、globalSP化、高Nutrition無料bonus廃止、Flight exact ownership、managed effect非剥奪、Pursuit iframe復元、RNG不要消費/有限数安全化。既存PASSを棚卸しだけで未実施へ戻さない。

### C. 修正前に分離すべき実差分

1. **Gathering ×4/×6、Unbreaking95%/29⁄30、Pursuit extra2～9は正常購入で到達不可**。式残存と実際の利用可能範囲は別。3項目ともLIKELY_UNINTENTIONAL_REGRESSION、fresh構造のUSER_DECISION_REQUIRED。
2. **ArmorMasteryのArmorItem限定はCONFIRMED V3 NARROWING REGRESSION**。今回利用者LOCKで標準耐久全般を正式目標にした。4SP/Lv1は今回変更せず修正未実施。
3. **`hurtAndBreak`外のdirect `hurt`がFH耐久skillを迂回**。旧→現の追加経路縮小。ArmorMasteryについては同LOCK未達、不壊については下記対象判断に含む。これは実際の分岐比較であり、特定未承認MODでの症状再現という意味ではない。

### D. 文書・実装・試験の不一致

- 旧不壊の「防具のみ」説明と実標準ItemStack範囲、旧L2完全無効説明と専用実装不在。
- 旧満足感lang/specはTaCZ中心だがfood確率も実在。旧豊穣は「完成品stack×2」と説明し実装は後付けcopy。旧採取の同一Fortune結果×N説明と二重抽選の差。
- 現SKILL_TREE_SPECの「Lv1-only」＋「all effect magnitudes unchanged」は上位効果到達不可と両立しない。migrationのno-auto-acquireは維持し、fresh構造だけ**UNDER PRE-#8 PARITY RE-AUDIT**。
- 現TEST_PLANのtool/weapon項目は手順対象一覧で、実EはArmorMastery tool非適用をassert。今回LOCK適合を意味しない。AUDIT B15の防具限定を推す旧提案も今回LOCKで置き換える（履歴は残す）。

<a id="parity-decisions"></a>
### E. 利用者が決める最小2問（監査時点の履歴・現在は解決済み）

**現行：問1＝3技能とも段階購入、問2＝標準damageable全般/direct hurtを利用者LOCK。費用を含む正式表は[SKILL_TREE_SPEC§1](SKILL_TREE_SPEC.md#1-existing-skills-and-confirmed-sp)、実装結果は[§8](#parity-fix-result)。以下の質問を再提示しない。**

**問1 — 3技能の購入構造（技能別回答も可）**：Gathering〔×2/×4/×6〕、Unbreaking〔90/95/29⁄30%〕、Pursuit〔extra1～9〕を、(A) **段階購入として戻す**、(B) **単一購入で旧最大効果**、(C) その他指定、のどれにするか。Aなら追加段階のSP配分、Bなら現2/3/4SPを維持するかも同時に指定。Codexが費用を自動決定しない。既存**43SP/839SPは今は不変**、変更承認後に合計再計算が必要。

**問2 — 不壊の正式対象**：(A) **旧actualどおり有資格playerに帰属する標準damageable ItemStack全般（hurtAndBreakとdirect hurtを含む）**、(B) 防具だけに意図的限定、(C) その他指定。Aでもenergy/customcap/標準耐久完全迂回は含めず、旧L2「完全無効」説明を無条件に保証しない。現在の不壊は通常hurtAndBreakのtoolにも効くので、Bは新たな意図的縮小になる。**防具の極意は全般対象で既にLOCK済み、再質問しない**。

上記以外の既存LOCK（migration/Root/Heroics/Ammo/高Nutrition/Flight等）は再質問しない。mod名substring復元や不明artifactの全面対応を必須質問として増やさない。

## 7. 今回の境界・次工程

- migrationはlegacy自動取得なし・level相当SP全返還・LegacyV2Backup保持・exactly onceのまま。fresh技能の将来maxLevel変更とは独立。migration code/取得済みdata/原本worldを変更していない。
- 後続fixの受入候補は[TEST_PLAN§31](TEST_PLAN.md#legacy-parity-future-acceptance)。**NOT RUN / fix未承認**。旧期待値を消して今回PASSへ見せかけない。
- 次の1作業は問1/2の利用者判断を正本へ記録し、LOCK済みArmorMasteryと判断された段階・範囲に限る修正単位を確定すること。今回Java/通常test/resources/build.gradle/Jar/config/schema/protocol/購入gate変更0。
- #1–#7 COMPLETE・残2、Avaritia/FE/Mekの完了維持。**#8/#9 NOT STARTED、RC=NO、REAL2CLIENT=BLOCKED**。P/T/Flight PURCHASE READYを取り消さない。Break Realm/Bulwark/FOURTH BOOT/試作型機関弩等へ着手しない。

[O-SPEC]: ../build/verification/legacy-parity-20260929-202500/baseline/foodhealing_v2_2_0_specification.md
[O-LANG]: ../build/verification/legacy-parity-20260929-202500/baseline/src/main/resources/assets/foodhealing/lang/ja_jp.json
[O-DATA]: ../build/verification/legacy-parity-20260929-202500/baseline/src/main/java/com/leva/foodhealing/capability/ShokugiData.java
[O-CMD]: ../build/verification/legacy-parity-20260929-202500/baseline/src/main/java/com/leva/foodhealing/command/FoodHealingCommands.java
[O-TICK]: ../build/verification/legacy-parity-20260929-202500/baseline/src/main/java/com/leva/foodhealing/ShokugiTickHandler.java
[O-FOOD]: ../build/verification/legacy-parity-20260929-202500/baseline/src/main/java/com/leva/foodhealing/FoodHealingHandler.java
[O-ALWAYS]: ../build/verification/legacy-parity-20260929-202500/baseline/src/main/java/com/leva/foodhealing/AlwaysEatHandler.java
[O-HUNGER]: ../build/verification/legacy-parity-20260929-202500/baseline/src/main/java/com/leva/foodhealing/HungerChangeHandler.java
[O-DAMAGE]: ../build/verification/legacy-parity-20260929-202500/baseline/src/main/java/com/leva/foodhealing/DamageEventHandler.java
[O-LOOT]: ../build/verification/legacy-parity-20260929-202500/baseline/src/main/java/com/leva/foodhealing/LootEventHandler.java
[O-ITEM]: ../build/verification/legacy-parity-20260929-202500/baseline/src/main/java/com/leva/foodhealing/mixin/ItemStackMixin.java
[O-TACZ]: ../build/verification/legacy-parity-20260929-202500/baseline/src/main/java/com/leva/foodhealing/mixin/TaCZGunScriptMixin.java
[N-SKILLS]: ../src/main/java/com/leva/foodhealing/FoodHealingSkills.java
[N-IDS]: ../src/main/java/com/leva/foodhealing/FoodHealingSkillIds.java
[N-TICK]: ../src/main/java/com/leva/foodhealing/ShokugiTickHandler.java
[N-FOOD]: ../src/main/java/com/leva/foodhealing/FoodHealingHandler.java
[N-DAMAGE]: ../src/main/java/com/leva/foodhealing/DamageEventHandler.java
[N-ITEM]: ../src/main/java/com/leva/foodhealing/mixin/ItemMixin.java
[N-ITEMSTACK]: ../src/main/java/com/leva/foodhealing/mixin/ItemStackMixin.java
[N-DURABILITY]: ../src/main/java/com/leva/foodhealing/DurabilityTransactions.java
[N-GATHER]: ../src/main/java/com/leva/foodhealing/loot/GatheringLootModifier.java
[N-LOOT]: ../src/main/java/com/leva/foodhealing/LootEventHandler.java
[N-PRODUCTION]: ../src/main/java/com/leva/foodhealing/FoodProductionTransactions.java
[N-SAT]: ../src/main/java/com/leva/foodhealing/SatisfactionTransactions.java
[N-HEROICS]: ../src/main/java/com/leva/foodhealing/HeroicsController.java
[N-ROOT]: ../src/main/java/com/leva/foodhealing/RootController.java
[N-STATS]: ../src/main/java/com/leva/foodhealing/FoodHealingBaseStats.java
[T-E]: ../src/main/java/com/leva/foodhealing/FoodHealingExistingSkillsGameTests.java
[T-C]: ../src/main/java/com/leva/foodhealing/FoodHealingCombatGameTests.java
[T-Z]: ../src/main/java/com/leva/foodhealing/FoodHealingPriorityZeroGameTests.java
[T-F]: ../src/main/java/com/leva/foodhealing/FoodHealingFoodProductionGameTests.java


<a id="parity-fix-result"></a>
## 8. 利用者判断の解決・parity修正結果（当時の履歴） — 2026-09-29 21:42 JST

**PRE-#8 LEGACY SKILL PARITY FIX COMPLETE**。Gatheringは5/5/5SPでmax3、Unbreakingは10/20/30SPでmax3、Pursuitは各100SPでmax9。ArmorMasteryはmax1/20SP。上位効果へ正常購入で到達し、標準耐久両入口は1回だけ調整する。既存group1,025SP/有限総額1,821SPを実定義照合。他費用は今回不変で永久凍結しない。

変更はregistry、DurabilityTransactionsの型限定解除、ItemStack共通hurt入口、4技能のja/en説明。GLM/Pursuit処理自体は既存の安全な段階式を維持。migration自動取得禁止と既存v3への遡及請求/返金なしを別々に回帰。上の旧監査結果を書き換えて最初から対応済みだったとはしない。

[正式受入§31](TEST_PLAN.md#legacy-parity-future-acceptance)：新parity unit320 assertions＋既存unit、compileJava/build/check、vanilla95/95・TaCZ95/95、実L2標準耐久限定24/24、全最終server通常保存/停止/exit0。旧84ケース保持・追加11・削除0。試験途中の旧費用fixture/rod破損/PvP準備FAILは[§14.69](MASTERY_IMPLEMENTATION_PREPARATION.md#legacy-skill-parity-fix-completed)へ分離して残す。

最終[配布Jar](../build/libs/foodhealing-3.0.0.jar)：**350,858 bytes / 269 entries / SHA-256 E24CAC325229A4AC341AB7D715C0A61A8070984B209107CCA11B40B50CCF2B8B**、schema5/protocol7。[監査](../build/verification/legacy-parity-fix-20260929-211107/audit/final-jar-audit.json)、[変更差分](../build/verification/legacy-parity-fix-20260929-211107/audit/implementation.diff)。実client/全外部item保証なし。queue #8/#9は未開始、次は利用者による他SP調整または#8範囲の指示待ち。

## 9. post-parity SP rebalance / new-skill phase

本文更新: **2026-09-29 22:38 JST**。前§8のparity fixはCOMPLETE維持。今回は別の利用者判断で既存skill価格を調整し、Quarrying1SP/Immovable50SPを追加した。current normal1150/有限2590、正式価格は[SKILL_TREE_SPEC](SKILL_TREE_SPEC.md)、効果・結果・途中FAILは[共通計画§14.70](MASTERY_IMPLEMENTATION_PREPARATION.md#skill-cost-rebalance-quarrying-immovable-completed)。旧§1–7の監査本文は変更していない。vanilla/TaCZ各109/109、新unit602＋既存、L2限定8/8・最終Jar監査/正常保存停止完了。旧parity耐久24/24のPASSを維持し今回再実行しない。配布Jar **360,970 bytes / 275 entries / SHA-256 36A79E98C8E854DA79B82811EBD5DD57FE01CF4F7EDFC93296031DDFD8A265BE**。#8/#9 NOT STARTED、RC=NO。


### post-parity current localization summary

本文更新: **2026-09-29 23:31 JST**。§9までのparity/SP再調整/採石・不動の完了結果を維持。今回[共通計画§14.71](MASTERY_IMPLEMENTATION_PREPARATION.md#player-facing-localization-cleanup)で食技表記・日本語26skill/7stat・英語意味一致・習得メッセージを整理し、localization1,094と最終core111/111×2を確認。§1–7本文、§8の1,025/1,821と95、§9の109/L2限定8は当時の証拠のまま。現在finite2590、Flight/P/T PURCHASE READY、#1–#7 COMPLETE、READY FOR USER AUTHORIZATION OF #8／#8/#9 NOT STARTED／RC=NO。最新Jarは[Status現在の要約](CODEX_STATUS.md#現在の要約)を参照。
