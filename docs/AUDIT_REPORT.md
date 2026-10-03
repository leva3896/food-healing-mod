# AUDIT_REPORT.md — v2.2.5 → Food Healing RPG v3.0.0

現行release更新: **2026-10-03 13:00 JST / #1–#9 COMPLETE / RC=YES / FOOD HEALING RPG v3.0.0 FORMAL RELEASE COMPLETE**。現行仕様の単一正本は[Food Healing RPG v3 Specification](FOOD_HEALING_RPG_V3_SPECIFICATION.md)。本書の詳細LOCK・日付付き記録は根拠/履歴として保持し、古いRC/次工程の記載は現行指示にしない。 #8全11分類の限定受入を継承し、新しい製品差分/blockerなし。Security scanではなくsource/Jar/仕様整合確認。

Audit date: 2026-08-24  
Source baseline: public GitHub `leva3896/food-healing-mod`, main reporting v2.2.5 in project metadata.

This document exists to prevent the v3 rewrite from accidentally deleting old fixes or reviving old bugs.

## A. Confirmed baseline facts to preserve or deliberately replace

### A1. Core healing
v2 spec/code:
- food Nutrition × recovery multiplier heals HP
- full-hunger eating exists
- max-health config range reaches 1e12

v3:
- preserve behaviors
- replace unsafe implementation details only

### A2. Shokugi legacy
v2:
- Lv cap 1000
- automatic skill unlock
- global outgoing formula approximately `1 + level*0.1`
- generic incoming reduction up to99%

v3:
- replace auto-unlock with SP tree
- **2000 Nutrition/Food-Level units = 1 Shokugi level = 1 SP**（current default）。日本語では2000 Nutrition/Food-Level単位 = 食技レベル1 = スキルポイント1。旧200 food-action方式は過去仕様で、現行計算へ戻さない。
- repeatable base damage keeps +0.1x per purchase (current20SP); historical per-SP price was superseded by the 2026-09-29 user decision
- reduction becomes explicit repeatable stat with post99 transcendence

### A3. TaCZ legacy multiplier
v2 code contains independent TaCZ multiplier approximately:

`1 + ShokugiLevel * 0.01`

v3:
- preserve as repeatable TaCZ Base Outgoing Damage
- current2SP per purchase +0.01x; historical1SP was superseded by the 2026-09-29 user decision

This item was previously easy to omit. **Do not omit it again.**

### A4. SuperbWarfare legacy multiplier
The existing v2.2.5 bytecode confirms the dedicated SuperbWarfare multiplier:

`1 + ShokugiLevel * 1.0`

**Retention and formula RESOLVED / LOCKED (2026-09-13).** The user chose the old
level-based multiplier with no new SP stat/cost. The approved 0.8.9.1 artifact now has
a bounded v3 gunfire connection; supported routes and evidence are in
[COMPATIBILITY_POLICY §14](COMPATIBILITY_POLICY.md#14-superbwarfare-legacy).
History: its replacement/retention was previously OPEN / LEGACY_PENDING_DECISION.

### A5. Grave/player drop exploit
v2.2.2 changelog/code fixed player death inventory duplication by excluding Player from drop multiplication.

v3:
- mandatory regression invariant
- never multiply Player death drops

### A6. End/dimension lifecycle
v2.2.3 and current capability code contain fixes for Shokugi HUD/data restoration across login/dimension.

v3:
- preserve and expand lifecycle handling for every new persistent stat.

---

## B. Direct code risks found in v2.2.5

### B1. Hunger dedup uses a time heuristic
A recent meal timestamp is used to avoid the generic Food Level path repeating a food heal.

Risk:
- legitimate mod hunger gain near a meal can be suppressed
- delayed food gain can duplicate

v3: transaction/source-aware exactly-once design.

### B2. Non-food hunger can trigger old Guts
Current HungerChangeHandler can activate old Guts from Food Level increases.

v3 locked rule:
- non-food Food Level gain can heal
- Root trigger is consumed-food Nutrition only

### B3. Food healing/effects occur before explicit server-only block
Move gameplay mutation to server authority.

### B4. MAX_HEALTH global reflection
Food Healing modifies global Attribute limit via reflection.

v3: remove.

### B5. AlwaysEat directly starts using item
May interfere with custom food `use()`.

v3: preserve full-hunger eating through a safer canonical path.

### B6. HUD draws inside PLAYER_HEALTH pre-event
Can disappear when another HUD/combat mod interferes.

v3: independent numeric rendering.

### B7. Absorption not represented
v3: numeric absorption display.

### B8. Flight tick forcibly clears mayfly
Can revoke another mod's flight.

v3: ownership-aware permission handling.

### B9. Effect ownership inferred by amplifier/duration
Can remove an equivalent effect from another mod.

v3: explicit ownership/safe mechanism.

### B10. Satisfaction consume→refund
Can conflict with result containers/NBT/backpacks.

v3: one authoritative non-consumption decision.

### B11. Food Production post-craft copy
UI result and actual received quantity differ.

v3: result/transaction doubling.

### B12. Gathering re-runs loot
Can conflict with other loot modifiers and contexts.

v3: final-loot safe modification.

### B13. Gathering fragile ore detection
String-based identification is brittle.

v3: tags/context/explicit classification.

### B14. Pursuit mutates `invulnerableTime`
Target state may remain altered.

v3: targeted iframe bypass with state safety.

### B15. Broad ItemStack durability Mixin

**現在の扱い（2026-09-29 21:42 JST）**：下記は旧監査提案の履歴。ArmorItem限定へ狭める案は不採用。今回利用者LOCKに基づき防具の極意/不壊とも有資格ServerPlayerのstandard damageable全般・direct hurt/hurtAndBreakへ1回接続し、指定回帰完了。[SPEC§19](SPEC.md#legacy-armor-mastery-parity-lock) / [実装証拠](MASTERY_IMPLEMENTATION_PREPARATION.md#legacy-skill-parity-fix-completed)。energy/customcap等と旧L2完全免疫説明は保証しない。

Can touch unrelated item durability.

v3: narrow intended armor/equipment semantics.

### B16. Optional TaCZ Mixin/direct imports
Can create absence/classloading risk and preserves old skill coupling.

v3: isolated optional Adapter.

### B17. Persistent skill toggle uses display names
Renaming/localizing can break saved data.

v3: resource IDs.

### B18. int level/count
Not suitable for effective unlimited progression.

v3: long/safer.

### B19. static per-player maps
Need reliable lifecycle cleanup.

v3: lifecycle-owned state.

---

## C. Conversation contradictions resolved

### C1. “actual Food Level increase” vs “eat while full”
If all healing used only actual Food Level delta, eating at Food Level20 would heal zero, contradicting a core existing feature.

**Resolution for v3:**
- consumed food → declared item Nutrition is healing units
- non-food hunger gain → actual positive Food Level delta
- deduplicate so a food transaction is not counted again by generic delta

This is a reconciliation of two existing requirements, not a new feature.

### C2. Old Guts vs new Root
Old v2 behavior includes Nutrition>=19 and several bonus effects.

Final v3:
- Root threshold = Nutrition18
- cumulative 15s option for vanilla food
- Root is a purchased leveled skill
- Resistance IV / Fire Resistance are represented by Kongo/Fire skills, not free high-Nutrition bonuses
- non-food hunger gain does not trigger Root

### C3. `帰零の領域` name
Retired.

Final display name: **真実の極意**

---

## D. Current LOCKED SP summary

Finite skills total: **2,590 SP**（本文更新: **2026-09-29 23:31 JST**）。今回利用者LOCK後の実registry集計。

- Normal/Shokugi **1,150**（新Quarrying1 / Immovable50含む）
- Root **70**
- Heroics **250**
- high-difficulty **620**
- TaCZ Ammo Conservation **500**

Repeatable base statsは除外。価格は[SKILL_TREE_SPEC](SKILL_TREE_SPEC.md)、効果は[SPEC§21/22](SPEC.md#quarrying-lock)、証拠は[共通計画§14.70](MASTERY_IMPLEMENTATION_PREPARATION.md#skill-cost-rebalance-quarrying-immovable-completed)。過去Spent/取得/point/toggle保全、遡及請求・返金なし。v2移行の全額SP返還/自動取得0は不変。旧1,025/1,821と旧監査の時系列は履歴として維持。

---

## E. Decisions and remaining OPEN items

### OPEN-01 v2.2.5 existing-world migration to SP
**RESOLVED / LOCKED - 2026-09-08 user decision.**

Valid nonnegative integral legacy level L / count C becomes level L / count C,
unspent SP L / spent SP 0. This is a full refund/respec, not automatic skill purchase.
AcquiredSkills, BaseStats and canonical toggles start fresh; no Root state is inferred.
No legacy skill cost/sublevel deductions or conversion tables are permitted.
See `SPEC.md` section 11 for validation, backup, idempotency and pending boundaries.

History: from 2026-08-24 until this decision, the refund formula, automatic ownership
and spent-SP representation were OPEN. Raw-only pending fixtures from that period remain
historical evidence, not the current expected result for valid legacy data.

Safety requirement:
- do not erase legacy data
- version migration data
- preserve backup/raw legacy values where useful
- fresh-world implementation may proceed
- the migration rule is now explicit; actual v3 old-world boot still requires separate approval/testing

### OPEN-02 prerequisite graph for 浄化の極意
**RESOLVED / LOCKED - 2026-09-13 user decision.** Only acquired normal 浄化 Lv1;
100 SP for this node, no additional prerequisites. Authoritative definition and
readiness boundary: [SKILL_TREE_SPEC §9](SKILL_TREE_SPEC.md#9-purification-mastery--浄化の極意).
History: unspecified additional high-difficulty investments were intended but not
frozen; the user explicitly removes that policy from v3.0.0 acquisition conditions.

### OPEN-03 additional prerequisite graph for 真実の極意
**RESOLVED / LOCKED - 2026-09-13 user decision.** Only acquired 浄化の極意 Lv1;
500 SP for this node. Future extra prerequisites require another decision, and do
not reopen today's graph. See [SKILL_TREE_SPEC §10](SKILL_TREE_SPEC.md#10-truth-mastery--真実の極意).
History: additional nodes were undecided. Effects/adapters are still incomplete.

### OPEN-04 SuperbWarfare legacy multiplier
**Retention/formula RESOLVED / LOCKED - 2026-09-13.** See A4 and Compatibility Policy §14.
Approved artifact received; the bounded gunfire connection is implemented. Wider routes
and realclient remain unverified; see [current evidence](CODEX_STATUS.md#evidence-superbwarfare-20260913).

### OPEN-05 legacy sub-level mapping for 採取 / 不壊 / 追撃

**2026-09-29 21:42 JST current clarification**：OPEN-05のmigration自動取得禁止/全SP返還/Spent0/永久backup/exactly onceはLOCK維持。fresh購入構造は今回利用者がGathering max3、Unbreaking max3、Pursuit max9の同一ID段階購入へ正式変更し、実装/自動受入を完了。[正式費用・効果](SKILL_TREE_SPEC.md#existing-multi-stage-legacy-normalization)。既存v3へ遡及請求/返金/取得変更なし、schema5/protocol7不変。**以下の2026-09-08 Lv1-only/2・3・4SPは当時の決定履歴で、現行購入定義ではない**。

**RESOLVED / LOCKED - 2026-09-08 user decision.**

Do not map any legacy Gathering/Unbreaking/Pursuit sublevel into acquired v3 nodes.
Retain all old sublevel/skill/toggle information in LegacyV2Backup. The player buys
fresh Lv1-only nodes with the OPEN-01 refund: Gathering 2 SP, Unbreaking 3 SP,
Pursuit 4 SP. Existing effects and toggles are unchanged.

Do not change the confirmed SP table.

History: sublevel mapping was OPEN from 2026-08-24. The previous recommendation
to preserve representable legacy effects was a pending approach, not an approved
automatic conversion table. The explicit respec decision replaces that pending approach.

History at the OPEN-01/05 decision: OPEN-02/03/04 and completed True Root OFF
reservation handling were still undecided. They were subsequently resolved within
their stated scopes; True Root's completed retention/re-enable rule is in SPEC §17.
Unresolved partial/lifecycle boundaries and implementation/artifact gates remain
separate; use current CODEX_STATUS rather than this historical list.

---

## F. Release blockers

Do not call v3.0.0 release-ready if any of these remain:

- startup crash
- optional mod absence crash
- player/world data loss
- End/dimension reset
- duplicate Food Healing AttributeModifier
- numeric HP HUD unusable
- player death/grave duplication
- double food healing/counting
- multiplayer state leakage
- another mod's flight/effects/modifiers being removed
- unresolved destructive migration



<a id="queue8-release-blocker-matrix"></a>
### F.1 Queue #8 final blocker matrix — 2026-09-30 07:19 JST

現行対象は[共通計画§14.74](MASTERY_IMPLEMENTATION_PREPARATION.md#queue8-fe-shader-blocker-resolution)の最終Jar、**372,242 bytes / 286 entries / SHA-256 8B8A31FA308CD24C4A139D65FEF5818F1A2DABD8587A238D93974F787FFE10DD**。今回startupだけを更新し、他10分類は§14.73の結果を保持（全旧class/asset byte同一＋最終core114/114×2）。A3/B3直接受入・通常保存終了もPASS。全MOD/全lifecycle保証やSecurity scanではない。旧B2 FAILは§14.73/§37/原logへ保全。

| 正式条件 | #8分類 | 根拠・限界 |
|---|---|---|
| startup crash | **FINAL #8 RUN PASS — supported FE2.7.20 formal client startup** | 内蔵exact compatだけのB3でtitle/通常ingot描画/正常保存Quit、Uniform→MUniform cast0、FH/compat起動ERROR0。A3不在回帰もPASS。B2のtitle前FAILは当時のまま保全。旧補助を正式依存へ追加せず、製品内蔵方式を利用者承認後に実装・実測 |
| optional mod absence crash | **FINAL #8 RUN PASS** | optionalなしA2 title/world/GUI/保存再読込、vanilla114。TaCZ不在購入欠落を修正しSP/canonical不変。任意の全組合せを網羅しない |
| player/world data loss | **FINAL #8 RUN PASS** | A2の一回再読込で通常保存canonical/HUD保持、最終NBT。core clone/save/無効データ保護もPASS。原本world不使用 |
| End/dimension reset | **FINAL #8 RUN PASS** | core actualRespawnAndDimensionTransferPreserveCanonicalData / endPortalCreditsResponsePreservesPartialHealthAndProgress / Flight native End・dimensionケース。実client Endの追加試験ではない |
| duplicate FH AttributeModifier | **FINAL #8 RUN PASS** | core priority-zero/heroics所有UUIDの反復再構築・foreign保全、Flight88。製品attribute経路は#8不変 |
| numeric HP HUD unusable | **FINAL #8 RUN PASS** | A2 ja/en/readload後もcurrent/max20/20を画面確認、vanillaheartだけへの退行なし |
| player death/grave duplication | **FINAL #8 RUN PASS（native death/drop境界）** | core playerDeathDropsAreNeverMultiplied・Pursuit死/例外・FoodProduction境界。墓MOD全artifact互換は未試験で、native PASSへ含めない |
| double food healing/counting | **FINAL #8 RUN PASS** | core foodConsumptionHealsAndProgressesExactlyOnce / nutritionRegisteredFoodsExactlyOnce、Nutrition93/data5000。既存count/migration不変 |
| multiplayer state leakage | **FINAL #8 RUN PASS（server-side分離）** | 登録packet/replay・他player不変、購入/SP/同期。**実2-clientはBLOCKED BY EXTERNAL REQUIREMENT：第二Minecraft account必要**。二者fixtureを実2-client PASSにしない |
| 他MOD flight/effect/modifier剥奪 | **PRIOR VERIFIED / CURRENT CODE BYTE-IDENTICAL（限定route）** | 最終exact Flight11classとAvaritia23＋EL2証拠は同一。以前のFE/EL/Mek追加は後続統合で検証。L2/Trial/FE6 native Adapter等同一、今回core managed foreign effects/Flight再確認。unknown provider/別版/全混在は保証せず、新blockerへ自動昇格しない |
| unresolved destructive migration | **FINAL #8 RUN PASS（既存unit/coreの移行境界）** | legacy respec・Nutrition比例移行・pending/invalid保護・clone/sync。migration製品classは開始Jarと同一、過去初回/第二/第三boot証拠維持。原本world再起動0、FOURTH BOOTはNOT AUTHORIZEDのまま |

全版/全GunPack/全boss/全trait/全performance/WARN0/全external combinationは **NOT RELEASE BLOCKING UNDER CURRENT POLICY**（保証済みの意味ではない）。Food Core1.0.5 exact3 ERRORは§14.62既知upstream非blocking受入維持。REAL2CLIENTは上記外部要件待ちを明示し、今回未解除。新artifact不足なし。利用者承認済みPRECHECK10/10後、旧shader契約をFH所有client-only互換へ正式内蔵し、B3で指定blockerを解消。外部ログ15 ERROR行はasset/tag/loot/biome/advancementの範囲外記録として保全（詳細§14.74）、全ログERROR0とはしない。

**当時の総合（2026-09-30の履歴）：#8 COMPLETE / #1–#8 COMPLETE / #9 NOT STARTED / RC=NO / READY FOR RC DECISION**。Flight/P/T readyの仕様・実装を巻き戻さない。GitHub/正式名/完全仕様書/公開は未実施。

今回：[判定集計](../build/verification/fe-shader-production-20260930-065113/audit/reviewed-results.json) / [PRECHECK](../build/verification/fe-shader-production-20260930-065113/audit/promotion-precheck.json) / [static426](../build/verification/fe-shader-production-20260930-065113/audit/static-promotion-result.json) / [最終Jar監査](../build/verification/fe-shader-production-20260930-065113/audit/final-jar-audit.json) / [source差分](../build/verification/fe-shader-production-20260930-065113/audit/implementation.diff) / [build・全unit・check](../build/verification/fe-shader-production-20260930-065113/audit/build-final-local.log) / [vanilla114](../build/verification/fe-shader-production-20260930-065113/audit/vanilla-final.log) / [TaCZ114](../build/verification/fe-shader-production-20260930-065113/audit/tacz-final.log) / [実画面](../build/verification/fe-shader-production-20260930-065113/audit/screens/events.jsonl) / [A3保存](../build/verification/fe-shader-production-20260930-065113/audit/A3-final-save.json) / [B3保存](../build/verification/fe-shader-production-20260930-065113/audit/B3-final-save.json) / [終了process](../build/verification/fe-shader-production-20260930-065113/audit/process-final.json)

旧§14.73の証拠（履歴保全）：[最終Jar監査](../build/verification/release-final-client-20260930-002426/audit/final-jar-audit.json) / [source差分](../build/verification/release-final-client-20260930-002426/audit/implementation.diff) / [build・全unit・check](../build/verification/release-final-client-20260930-002426/audit/final-build-fixed2.log) / [vanilla114](../build/verification/release-final-client-20260930-002426/audit/vanilla-fixed2.log) / [TaCZ114](../build/verification/release-final-client-20260930-002426/audit/tacz-fixed.log) / [外部影響照合](../build/verification/release-final-client-20260930-002426/audit/external-impact-final.json) / [実画面記録](../build/verification/release-final-client-20260930-002426/audit/screens/events.jsonl) / [A保存](../build/verification/release-final-client-20260930-002426/audit/A2-final-save.json) / [Bクラッシュ](../build/verification/release-final-client-20260930-002426/audit/B2-crash.txt) / [終了process](../build/verification/release-final-client-20260930-002426/audit/process-final.json)

### F.2 Queue #9 current RC decision — 2026-10-03 13:00 JST

**#1–#9 COMPLETE / RC=YES / FOOD HEALING RPG v3.0.0 FORMAL RELEASE COMPLETE**。F.1の限定11分類を維持。Frozen source `d40b37d19ed71139dcb749bdf8aef3669ef53ca7` をmainへFFしreadback一致。詳細は[Receipt](../release/v3.0.0/RELEASE_RECEIPT.md)。

<details><summary>公開前RC判定（11:22 JSTの履歴）</summary>

### F.2 Queue #9 previous RC decision — 2026-10-03 11:22 JST

**#1–#8 COMPLETE / #9 PARTIAL / RC=YES / LOCAL RELEASE ARTIFACT AND SPECIFICATION COMPLETE / GITHUB UPDATE BLOCKED**。F.1の11分類は同一製品/限定scopeの受入根拠として維持し、再実行PASSにしない。source274とbuild設定の差分0、正式Jar byte一致、仕様26/7/6・価格・schema/protocol・互換範囲を照合。新しい製品矛盾/blockerなしのためRC=YES。
GitHub連携のrelease branch作成が403 Resource not accessible by integrationで拒否。branch/commit/main更新0、再試行0。GitHub mainは開始commitのまま。 正式release全体の完了判定とRC適格性を分離する。[Release Receipt](../release/v3.0.0/RELEASE_RECEIPT.md)。


</details>

## G. Pre-#8 表示・文書整合の監査

本文更新: **2026-09-29 23:31 JST**。[共通計画§14.71](MASTERY_IMPLEMENTATION_PREPARATION.md#player-facing-localization-cleanup) / [TEST_PLAN§35](TEST_PLAN.md#localization-message-acceptance)。A2の旧200 countを正式2000単位へ訂正し、現行日本語は食技へ。旧v2自動取得・旧価格の履歴と過去FAILは保持。有限2590/費用/効果/migration/内部Shokugi/schema5/protocol7不変。localization unit1,094＋既存全unit、vanilla/TaCZ111/111、最終Jar監査/正常終了。Security scanではなく表示・動作回帰。GitHub/完全仕様書/正式Jar名はSPEC§23の将来release要求のみ。READY FOR USER AUTHORIZATION OF #8、#8/#9 NOT STARTED、RC=NO。


## H. Pre-#8 release command / verification artifact cleanup

本文更新: **2026-09-30 00:07 JST**。[共通計画§14.72](MASTERY_IMPLEMENTATION_PREPARATION.md#release-command-verification-cleanup) / [TEST_PLAN§36](TEST_PLAN.md#release-command-verification-acceptance)。v2.2.5 commitのcached command sourceをblob/hash再照合し、開始9経路をA管理3/B診断修復3/C旧chat3/D0/E0へ分類。Cだけ削除、A/Bのpermission2・long検証・本人canonical・syncは既存処理不変。旧alias migrationは保全。全文の旧実行記録は当時の証拠として残す。

製品verification HUD/helper/FE検証patch混入0、raw Observer class2は正式TimeStop ownership機構として保持。command専用翻訳54/言語だけ削除、その他製品class意図しない差分0、finite2590/schema5/protocol7不変。localization815＋全既存unit、vanilla/TaCZ113/113、Jar監査/正常保存終了。初回4FAILとfixture修正は§14.72へ記録。Security scanではなくcleanupと動作回帰。正式Jar **358,640 bytes / 277 entries / SHA-256 4DF4954EC7082DA0276A0614EAEB871971F4A69538983EEED2F688BA4E3B5DEC**。

**PRE-#8 PRODUCT CLEANUP COMPLETE / READY FOR USER AUTHORIZATION OF #8**。#8実clientの表示/警告/command候補は未試験。#8/#9 NOT STARTED、RC=NO、REAL2CLIENT=BLOCKED、Flight/P/T PURCHASE READY・SP保護・既存個別gate維持。可逆クラフト増加は既知許容仕様・バグ修正対象外。
