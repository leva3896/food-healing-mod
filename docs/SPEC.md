# SPEC.md — Food Healing RPG v3.0.0 Core Specification

現行release更新: **2026-10-03 13:00 JST / #1–#9 COMPLETE / RC=YES / FOOD HEALING RPG v3.0.0 FORMAL RELEASE COMPLETE**。現行仕様の単一正本は[Food Healing RPG v3 Specification](FOOD_HEALING_RPG_V3_SPECIFICATION.md)。本書の詳細LOCK・日付付き記録は根拠/履歴として保持し、古いRC/次工程の記載は現行指示にしない。

Status: **FROZEN FEATURE SCOPE / FORMAL v3.0.0 SPECIFICATION — consolidated below**

本文更新: **2026-09-29 23:31 JST**。今回の利用者明示承認により採石の心得/不動の極意の2skillだけをv3.0.0 scopeへ追加し、その範囲で再frozen。価格正本は[SKILL_TREE_SPEC](SKILL_TREE_SPEC.md)、実装証拠は[共通計画§14.70](MASTERY_IMPLEMENTATION_PREPARATION.md#skill-cost-rebalance-quarrying-immovable-completed)。

## 1. Identity

- Display name: **Food Healing RPG**
- MOD ID: `foodhealing`
- Version: **3.0.0**
- Minecraft: 1.20.1 Forge
- Baseline: public Food Healing v2.2.5 code

## 2. Design goal

Food Healing RPG is a food-driven RPG progression mod intended to remain usable in large, high-difficulty modpacks.

Core loop:

**eat / discover food → heal → gain Shokugi progress → obtain SP → choose skills/base stats → survive harder content**

Compatibility and player-data safety are Priority 0.

---

## 3. Shokugi / Skill Points

Updated: **2026-09-29 10:51 JST** — queue #6 implemented; [§14.2](#142-nutrition--food-level-based-shokugi-count) is canonical.

**2000 Nutrition/Food-Level units = +1 Shokugi level = +1 unspent SP**, configurable.
Historical 200 food-actions was the old production baseline; provisional1000 was not adopted.
Those old units are not used as the new runtime threshold. See §14.2 for proportional migration.

### LOCKED progression

- Old “level automatically unlocks skills” system is removed.
- Skills are purchased through a skill tree.
- Finite skills are not permanently exclusive; long-term players can eventually obtain all finite skills.
- Remaining SP can be spent indefinitely on repeatable base stats.
- Old Lv1000 progression cap must not be the gameplay cap in v3.
- Store level/count/SP in `long` or safer representation.

### SP accounting

Current confirmed finite-skill cost:
- Normal/Shokugi skill group: 1,150 SP
- Root group: 70 SP
- Heroics group: 250 SP
- High-difficulty group: 620 SP
- TaCZ Ammo Conservation group: 500 SP
- **Total: 2,590 SP**

Repeatable base-stat spending is excluded. 今回の利用者LOCK後の実registryと一致。既取得Lv/過去Spent/未使用SP/point count/toggleは保全し、遡及請求・返金・新価格での再計算を行わない。

---

## 4. Dedicated GUI

### LOCKED

- The normal player inventory is the entry point.
- While the **normal player inventory** is open, press **S** by default to open the Food Healing GUI.
- S is a configurable Minecraft keybind under a Food Healing category.
- Do not open it from chest/furnace/crafting/other container screens by default.
- GUI is the normal user-facing hub.
- Commands remain admin/debug/repair tools, not the primary UI.
- Formal v3 has no legacy chat skill list/detail/toggle UI. Retained `/foodhealing syokugi` utilities: `level`/`count` (self read-only diagnostics), `setlevel`/`setcount`/`setskillpoint`/`addskillpoint` (permission 2, executing player only). GUI purchases/toggles still use the registered server-validated packets. No verification prepare/hit/seal/arm commands ship.
- Exact command inventory and scoped cleanup evidence: [common plan §14.72](MASTERY_IMPLEMENTATION_PREPARATION.md#release-command-verification-cleanup).

GUI should expose:
- Shokugi level/count/progress
- unspent/used SP
- skill tree
- acquired/unacquired skills
- descriptions
- prerequisites
- The 2026-09-13 acquisition decisions are authoritative in [SKILL_TREE_SPEC sections 9–10](SKILL_TREE_SPEC.md#9-purification-mastery--浄化の極意).
  Owned-parent prerequisites are separate from ON/OFF effects and implementation readiness.
  A fulfilled prerequisite does not permit SP spending for an unfinished effect/adapter.
- skill ON/OFF states where applicable
- base stats / repeatable upgrades
- Food Diversity status
- relevant optional-mod lock reasons

Gameplay purchases/toggles are server validated.

---

## 5. Numeric HP HUD

### LOCKED

Keep numeric HP display because health can be extremely large.

Required:
- survival numeric HP display
- current/max HP
- absorption shown separately
- compact K/M/B/T style large-number display
- creative may retain vanilla health behavior if that matches current intended design

### MUST FIX

Current v2.2.5 health rendering is coupled to `RenderGuiOverlayEvent.Pre` for `PLAYER_HEALTH`, where it both cancels vanilla hearts and draws Food Healing text. Other HUD/combat mods can cancel/replace before it, causing the numeric display to disappear or hearts to return.

v3 design:
- Separate “suppress vanilla hearts” from “draw Food Healing numeric health”.
- Draw Food Healing HUD independently in a post/custom overlay path not dependent on another mod allowing the `PLAYER_HEALTH` pre-event.
- Test absorption and huge-health precision.

---

## 6. Health scale

### LOCKED

- Preserve configurable theoretical max health up to **1,000,000,000,000 HP (1 trillion)**.
- Do NOT restore the v2.2.5 global Reflection hack that changes vanilla `Attributes.MAX_HEALTH` max globally.
- Food Healing owns only its progression and modifiers.
- AttributeFix, when installed, may own global attribute-limit expansion.

### Numeric precision note

Minecraft player health is float-based. Integer-by-integer precision is exact only to approximately:

`2^24 = 16,777,216`

At ~1 trillion HP, float spacing is on the order of ~65,536 HP. Therefore 1 trillion remains a supported/theoretical setting but small damage/healing steps cannot be represented precisely at that scale.

Do not implement a new virtual-double-HP system in v3.0.0; that is outside the frozen scope.

---

## 7. Core food healing

### LOCKED baseline

Default Food Healing ratio:

**1 Nutrition/Food-Level unit → 2 HP**

A repeatable recovery multiplier can increase this later.

### Critical reconciliation: full-hunger eating + non-food hunger gains

Two required behaviors must coexist:

1. **Food consumption** must heal by the food item's declared Nutrition even when the hunger bar is already full.
2. **Non-food Food Level increases** (commands, other mods, effects/abilities) must also heal.

Therefore v3 must distinguish transaction sources:

#### A. Actual food-consumption transaction
Use the **consumed item's declared Nutrition** as the canonical healing units.

This preserves:
- healing while Food Level is 20/full
- high-Nutrition mod foods
- legacy Food Healing behavior

#### B. Non-food Food Level increase
Use the actual positive Food Level delta as healing units.

#### Exactly-once invariant
One underlying food action must never be healed again by the generic Food Level-delta path.

No “2 tick since eating” guesswork.

All gameplay mutation is server authoritative.

---

## 8. Eat while full

### LOCKED

Food Healing RPG must retain the ability to consume food while the hunger bar is full.

### MUST FIX

Current `AlwaysEatHandler` directly starts item use. Reimplement so custom/modded food right-click/use behavior is respected as much as possible.

Avoid globally replacing or bypassing a food item's own `use()` semantics.

---

## 9. Food Diversity

### LOCKED

- Track unique food item identities.
- Every **5 newly discovered food types → +2 max HP**.
- Permanent progression.
- Additive with SP-based base max HP.
- Persist/sync across death, dimensions, logout, restart.

### MUST FIX

Rebuild Food Healing-owned max-health modifier idempotently from canonical Food Diversity data.

Always remove/update the old Food Healing modifier even if the new computed bonus is zero. Never infer canonical progress from the Attribute value.

---

## 10. Base / repeatable stats

All are conceptually uncapped for gameplay, with numeric safety.

### 10.1 Base Defense
- **5 SP → +5 Armor**
- Combines with armor/equipment/other legitimate modifiers.
- Armor-bypass mechanics may bypass this layer.
- No global Attribute overwrites.

### 10.2 Base Damage Reduction
Universal Food Healing reduction layer for any damage event Food Healing can safely capture.

- 0–99%: **10 SP → +1 percentage point**
- after 99% (each next stage also **10 SP**):
  - 99%
  - 99.9%
  - 99.99%
  - 99.999%
  - ...

Store:
- normal linear stage
- integer post-99 transcendence stage

Do not store a giant decimal string of nines.

At 99% plus `n` post-99 points, conceptual remaining multiplier:

`10^-(n+2)`

### 10.3 High-Difficulty Base Damage Reduction
Same independent system as 10.2. **10 SP per stage**, including post-99 transcendence.

- Allocation allowed only when **Auto Leveling OR L2 Hostility** is installed.
- If neither is installed: show it but prevent allocation.
- Normal and high-difficulty reduction multiply; they do not add.

Conceptual:
`final = damage * normalRemaining * highDifficultyRemaining`

### 10.4 Base Max HP
- **5 SP → +2 max HP**
- Independent/additive with Food Diversity.

### 10.5 Food Healing Recovery Multiplier
- Baseline `x1.00`
- **20 SP → +0.25x**
- Applies to Food Healing's food/hunger→HP conversion only.
- Does not multiply vanilla natural regeneration, potions, or unrelated mod healing.

### 10.6 Base Outgoing Damage
Preserve v2.2.5 growth scale as SP allocation:

- baseline x1.00
- **20 SP → +0.10x**

Formula:
`1.0 + globalDamageUpgradePoints * 0.10`

Examples:
- 100 purchases / 2,000 SP → x11
- 1000 purchases / 20,000 SP → x101

### 10.7 TaCZ Base Outgoing Damage
Independent TaCZ-only repeatable stat:

- baseline x1.00
- **2 SP → +0.01x**

Formula:
`1.0 + taczBaseDamageUpgradePoints * 0.01`

This is separate and multiplicative with:
- global Base Outgoing Damage
- Ammo Conservation skill's TaCZ damage multiplier

**Do not omit this stat.**

### 10.8 Ammo Conservation heat transitions

**2026-09-09 LOCKED SPEC (explicit user decision).** While the acquired skill is
level 1 or higher and enabled, supported TaCZ guns have heat immunity.

- On first purchase with default ON, or OFF to ON, normalize only the currently
  equipped supported gun's existing heat/overheat lock once.
- When an enabled player switches to an already-hot supported gun, normalize
  that newly active gun once at the verified TaCZ/Forge equip transition.
- Normalization means canonical heat zero and unlocked, using TaCZ's actual
  API and server-authoritative synchronization, not arbitrary NBT editing.
- Subsequent supported shots while ON do not generate heat or overheat locks.
- ON to OFF does not restore old heat or invent heat for past shots. TaCZ's
  normal heat, lock, and cooldown lifecycle resumes from the current canonical
  state for subsequent shots.
- Immediate overheat relief by toggling an acquired skill ON is intentional.
  Unowned, absent-TaCZ, and unsupported-version players cannot use this path.
- Do not clear unequipped inventory guns, scan all guns per tick, repeatedly
  reset heat/lock, bypass only the server lock, or falsify the client HUD.
- If a safe active-gun transition cannot be established from the real supported
  artifact, stop as a technical blocker rather than guessing a hook.

This resolves the 2026-09-09 pre-implementation heat-transition question only.
Skill levels, costs, saving chances, and damage multipliers remain unchanged.

---

## 11. Persistence

Purification Mastery's 2026-09-15 effect activation decision is canonical in
[SKILL_TREE_SPEC §9 Q2](SKILL_TREE_SPEC.md#purification-mastery-activation).
Parent and mastery toggle values remain independently saved; the limited Trial
adapter adds no player schema, migration, timer, refund or foreign-state ownership.

### LOCKED persistent canonical data

At minimum persist:

- Shokugi level
- Shokugi count
- unspent SP
- used SP / auditable spent points
- acquired skill resource IDs + levels
- skill toggle states
- Food Diversity unique-food history
- Food Diversity HP progression
- Base Defense upgrade count
- Base Max HP upgrade count
- Base Damage Reduction linear + transcendence stage
- High-Difficulty Reduction linear + transcendence stage
- Recovery multiplier upgrade count
- Base Outgoing Damage upgrade count
- TaCZ Base Outgoing Damage upgrade count
- Ammo Conservation level
- other future persistent values already defined by frozen skills

Transient combat timers may be treated separately; do not persist them unless needed for correctness/exploit prevention.

### LOCKED legacy full refund / respec (2026-09-08 user decision)

OPEN-01 and OPEN-05 are **RESOLVED / LOCKED**, not pending balance decisions.
For a valid v2.2.5 Shokugi compound with nonnegative integral level L and count C:

- Preserve ShokugiLevel L; set UnspentSkillPoints L and SpentSkillPoints 0. The historical same-unit rule preserved canonical C; the 2026-09-29 LOCK now converts canonical C proportionally under §14.2. LegacyV2Backup always preserves original C.
- Start AcquiredSkills / BaseStats / canonical toggles fresh. Infer no Root/True Root runtime state.
- Do not auto-purchase any legacy skill or deduct historical skill costs.
- Gathering / Unbreaking / Pursuit sublevels do not map to v3 nodes, regardless of old level.
  Fresh purchases use the current multi-stage registry: Gathering5/5/5, Unbreaking10/20/30, Pursuit100×9. Old sublevels grant none automatically; see SKILL_TREE_SPEC §1.
- Preserve the full original compound in LegacyV2Backup permanently, including unknown fields,
  nested compounds, arrays, sublevels, old toggles and count. Never replace it with v3 canonical data.
- Finish by clearing the existing LegacyMigrationPending flag in schema5 (schema4 was the initial respec implementation). Subsequent canonical
  deserialize / save / reload / clone / sync / separate-JVM load must not refund again.
- Fresh schema5 and completed v3 data are not respec targets. Normal schema4 only receives the §14.2 one-time count-unit conversion, without another refund.
- Invalid, missing, negative, wrong-type or unsupported inputs do not generate guessed SP.
  Preserve raw evidence and keep the existing migration-pending purchase gate. An unvalidated
  raw payload is not evidence of ownership for the old level-derived effect fallback either.
- Food Diversity is separate and unchanged. The actual extracted Lv2/count35 fixture must yield
  canonical count350/raw count35, unspent2/spent0, no acquired skills/base stats/toggles, no Root runtime, complete raw backup,
  and unchanged Diversity All28 / Current3 / MaxHealthBonus10.

The verified v2.2.5 source format is unversioned. Integral byte/short/int/long values are
read without truncating floating-point or string values into migration input. Unknown version
markers are not interpreted as v2. Already pending schema4 scaffolds may finish only when
their preserved valid raw level/count still match canonical progression and no v3 SP,
skill or base-stat investment would be overwritten. Conflicting/edited scaffolds remain pending
for review; no merge/refund policy is inferred for them.

History: OPEN-01/05 were unresolved before this user decision; earlier raw-only retention PASS
does not prove the new refund path. This authorization covers implementation and automated
fixtures only. **REAL V3 MIGRATION WORLD TEST / V3 OLD-WORLD BOOT = NOT RUN** until separately
authorized and executed (historical condition; see current Status for later completed boots).
OPEN-02/03 acquisition conditions and OPEN-04 retention/formula were subsequently LOCKED
on 2026-09-13: see [Skill Tree sections 9–10](SKILL_TREE_SPEC.md#9-purification-mastery--浄化の極意)
and [Compatibility Policy section 14](COMPATIBILITY_POLICY.md#14-superbwarfare-legacy).
This does not complete the effects or external integration. The later True Root completed-reservation
ON-to-OFF decision is **RETAIN / LOCKED**; see [section 17](#17-true-root-completed-reservation-retention)
for its limited scope and the boundaries not decided by retention alone.

### lifecycle requirements

Rebuild Food Healing-owned modifiers idempotently on:
- login
- clone/respawn
- dimension transfer
- world reload
- dedicated server restart

Never remove or overwrite modifiers owned by another mod.

---

## 12. Legacy high-Nutrition bonus

<a id="legacy-high-nutrition-retirement"></a>
更新: 2026-09-28 18:50 JST。**RESOLVED / LOCKED / v3.0.0 FORMAL RELEASE REQUIRED**（利用者の明示決定）。仕様記録の完了であり、今回の削除実装・動作試験PASSではない。

### 12.1 v2公開仕様とv3正式仕様

v2.2.5の旧仕様は、Nutritionが設定閾値（既定19）以上の食事で、旧GutsとResistance IV・Fire Resistance等の一時ボーナスを付与するもの。今回の利用者説明と既存監査記録に基づく過去仕様であり、CurseForgeページを今回再調査・編集したという意味ではない。

**v3.0.0では、このNutrition閾値だけによる旧高栄養食品ボーナスを廃止する。** Nutrition19以上という理由だけでResistance / Fire Resistance / その他の旧ボーナスeffectを新規grant・refresh・延長してはならない。類似するFood Healing能力の正規入口は対応skillの正式取得・ON/OFF・固有条件とする。未取得またはOFFなら旧bonusは0、取得＋ONならそのskillの正式仕様だけを適用する。耐火の心得・金剛の心得を迂回する無料効果、別timer、二重refreshは認めない。

Nutritionそのもの、食事HP回復、Nutrition/Food-Level食技count、Root/True Root、Food Diversity、その他正本でNutritionを使う正式仕様は維持する。食品自体・他MOD・Potion等の独立した正規effectをこの廃止対象と混同しない。v3公開説明へ旧「Nutrition19以上で自動Ironclad Defense」を転記しない。

### 12.2 移行とeffect ownership

旧bonusの新規grant/refreshを止める一方、移行/load時にResistance / Fire Resistance等を一括強制removeしない。出所を証明できない既存effectは通常期限でexpireさせる。Potion・Beacon・装備・他MOD・別能力のeffectを削除・downgrade・短縮しない。正式に有効なv3 skillは自身のownership規則に従い、強い外部effectの非上書き・skill OFF時の外部effect非削除回帰を維持する。

### 12.3 現行sourceのREAD ONLY監査（2026-09-28）

下表のauto grant/refresh/bypassは**旧Nutrition閾値ボーナス経路**についての判定。全MODのeffect所有権安全性の認定ではない。

| 項目 | 現行v3実装 | 根拠・限界 |
|---|---|---|
| Nutrition>=19 trigger | **ABSENT**（実行経路） | `FoodHealingHandler.onItemUseFinish`に閾値判定なし。`FoodHealingConfig.CommonConfig`の`bonusThreshold=19`・`bonusDurationSeconds=1200`は宣言/初期化だけの互換キー。設定名が残ることと経路残存を区別 |
| Resistance auto grant | **ABSENT** | `ShokugiTickHandler.onPlayerTick`→`FoodHealingSkills.isEnabled(KONGO)`→`applyManagedEffect(DAMAGE_RESISTANCE,3)`だけ。Nutritionを参照しない |
| Fire Resistance auto grant | **ABSENT** | 同tick→`isEnabled(FIRE_RESISTANCE)`→`applyManagedEffect(FIRE_RESISTANCE,0)`。`DamageEventHandler`の耐火防御もskill判定 |
| refresh path | **ABSENT**（旧bonus） | 現存refreshは取得/ON判定後のmanaged effect240tickのみ。20分/24000tick旧bonusを呼ぶ経路なし |
| skill bypass possibility | **NO**（Nutritionだけによる迂回） | server Finishから回復/transaction/Root/count/syncへ到達。正常fresh・正常移行済みでは取得＋ON判定。別件のpending互換fallbackは下記の限界として分離 |
| migration force-remove | **ABSENT** | `ShokugiProvider.deserializeNBT`→`ShokugiData.deserializeNBT`、`CapabilityEvents`のjoin/login/clone/respawn/dimension経路にResistance/Fire Resistance一括削除なし |
| external effect ownership risk | **UNVERIFIED**（一般的な同値置換衝突） | 旧bonus廃止によるremove経路はない。managed effectはmarkerなし/より強い外部effectを保持し、OFFはmarkerだけ消す。ただし既存markerと同amplifier・期限差±3tick以内の外部置換を完全識別する保証は今回ない |
| 次回production変更が必要か | **NO**（旧bonus経路削除として） | 既に実行経路なし。今回は実装変更0。新LOCKの正式受入回帰は未実行であり「高栄養bonus削除 IMPLEMENTED」と新規認定しない |

呼出元までの照合：`FoodHealingMod`は`FoodHealingHandler`/`ShokugiTickHandler`/`FoodDiversityHandler`を登録。server FinishはHP回復→`FoodHealingTransactions.recordFoodConsumed`（hunger二重回復防止）→`RootController.onFoodConsumed`（GUTS取得/ON・Nutrition18の別仕様）→現行食事count/同期。Food Diversity Finishは食品ID/種類数/自身の最大HP補正であり防御effectではない。`ItemStackMixin`/`ClientItemStackMixin`の`finishUsingItem`はSatisfaction時に元Itemのfinishをcopyへ1回呼ぶ経路であり、旧閾値bonusを追加しない。元Item固有のeffectは別物。productionの`MobEffectInstance`/`addEffect`はmanaged skill、Root専用GUTS表示、外部L2への元API委譲を確認した。GameTest内のeffect作成は試験fixtureとして分離。

`FoodHealingSkills.getEffectiveLevel`にはmigration-pending時の旧食義Lvによるfallbackが存在するが、Nutrition19判定ではない。正常v2移行は`ShokugiData.deserializeNBT`で全額SP返却・取得空・pending解除へ進む。編集済みpending等の別境界を今回修復/承認せず、fallback全体が新LOCKの取得条件に適合するかの包括保証はしない。

`FoodHealingMod`登録コメントの「ボーナス効果を付与」は古い説明で、実call-siteの証拠と区別する。今回コメント/source/Configは変更しない。固定queue #2を先行維持し、#6の食事/count工程でこの不在・移行/ownership境界を再接続時の受入条件へ取り込み、必要な修正が判明した場合のみ最小差分を検討、#8で最終Jar回帰を確認する。削除すべき経路がない現時点で、削除patchを作る工程は追加しない。将来回帰は[TEST_PLAN§28](TEST_PLAN.md#legacy-bonus-negative-regression)参照。

---

## 13. Server/client rule

Server is the single source of truth for:
- healing
- Shokugi count/SP
- skill ownership
- skill activation
- damage multipliers/reductions
- Root/Heroics state
- Food Diversity
- modifiers

Client:
- input
- rendering
- synced status display

---

## 14. v3.0.0 formal release required additions

Updated: **2026-09-27 10:30 JST** — explicit user release-scope decision.
Status: **FORMAL RELEASE REQUIRED / §14.1 and §14.2 LOCKED / IMPLEMENTED / LIMITED AUTOMATED TESTED**. Current results: common plan §14.62/§14.63. Formal RC decision: common plan §14.75 / release receipt.

Historical scope: on 2026-08-27 these were post-stabilization specification candidates, not LOCKED or
approved for implementation. That historical decision is preserved; the current user decision supersedes
the candidate-only release classification. Both features must be implemented before the formal v3.0.0
release. Pam's Trees remains an optional installed dependency; optional installation does not make this
Food Healing feature optional release scope.

Historical SJ1 authorization (superseded for completed #5/#6 implementation): that phase did not authorize product code, recipes,
Config, migration, threshold, or build changes. Current runtime behavior remains until the separately
approved implementation phase; releasing the old food-use count model unchanged is not an alternative.
The fixed major release queue is [common plan §14.34](MASTERY_IMPLEMENTATION_PREPARATION.md#v3-fixed-release-completion-queue).

### 14.1 Pam's HarvestCraft 2 - Trees harvest duplication

Updated: **2026-09-29 09:40 JST**. **FORMAL RELEASE REQUIRED / SPEC LOCKED / IMPLEMENTED / LIMITED TESTED / #5 COMPLETE**.

#### LOCKED gameplay and implementation

**1 eligible harvest/item H + 1 item in #minecraft:logs -> H x2**, shapeless, ingredient positions free.
Consume one item from each of two occupied slots, net +1 H. No skill prerequisite or provenance tracking.

**Pam Trees1.0.2 direct-harvest whitelist =50 IDs**, unchanged including its apple output.
Production recipes are a separate set:

| Recipe group | Count | Pam absent | Pam present |
|---|---|---|---|
| Pam direct outputs except apple |49|inactive|active|
| Standard vanilla apple |1|active|active|
| Standard vanilla cocoa beans |1|active|active|
| Total production recipes |**51**|**2 active**|**51 active**|

- Apple is still a Pam direct harvest, but its recipe is now a **Food Healing standard vanilla feature**.
  Stable ID foodhealing:log_duplication/apple. The former
  foodhealing:pam_tree_duplication/minecraft_apple ID is removed; no duplicate apple recipe.
- Cocoa beans are also a standard vanilla feature: foodhealing:log_duplication/cocoa_beans.
  Neither vanilla recipe has mod-loaded or other-mod conditions. Food Healing alone is sufficient.
- Paper/string remain Pam-conditioned. Cocoa is not added to the Pam direct-harvest50 audit mapping.
- Ingredient2 uses the full **item tag minecraft:logs**: logs/wood/stripped, Nether stems/hyphae/stripped,
  and properly tagged other-mod items. No name/class/namespace fallback, logs_that_burn narrowing,
  or block-tag-only inference.
- Exclude non-whitelisted Pam saplings/tree block items/shear-preserved blocks/processed nuts/unrelated
  crops and other-mod fruit. The only separate vanilla exceptions are apple and cocoa beans.
- Food Production Mastery's food-only contract is unchanged: apple unowned/OFF/ON = **2/2/4**;
  cocoa beans = **2/2/2**. Cocoa is not made edible or forced into the multiplier. Paper/string remain2.
  Base recipe output is always2; an eligible food's additional multiplier is a separate existing layer.
- Extra occupied slots do not match. Stack count>1 in either valid slot is legal vanilla semantics;
  each craft consumes exactly one from each.
- [Canonical Pam mapping](../tools/data/pam_trees_1_0_2_harvests.json) remains byte-identical50.
  [Generator](../tools/generate_pam_tree_recipes.py) skips apple only for Pam-conditioned generation,
  writes49 stable pam_tree_duplication IDs and2 explicit log_duplication IDs. Same input regeneration diff0.
- All51 are normal minecraft:crafting_shapeless/category misc. Only the49 Pam recipes have
  Forge conditions type forge:mod_loaded / modid pamhc2trees, evaluated before registry ingredient parsing.
- The formally tested Pam target is1.0.2/hash below. Other Trees versions: **UNTESTED / UNSUPPORTED CLAIM**;
  mod-loaded gating is not exact-version gating.
- No custom serializer/runtime dependency/Pam Java import/external content redistribution. Generator,
  canonical mapping and verification helpers remain outside the product Jar. Crops/Food Core remain
  supplemental-test-only and are not Food Healing dependencies.

#### Reference Jar static audit

Reference artifact: `pamhc2trees-1.20-1.0.2.jar`  
SHA-256: `69E0C722C786B78AEA299E7FAF27991E6533A671A66F281FB5CED279EB31B0DD`

This was a read-only static Jar/resource/bytecode audit, not a runtime integration test.

- `META-INF/mods.toml` and the main class identify MOD ID `pamhc2trees` and implementation version `1.0.2`.
- The Jar contains 50 mature `pamhc2trees:pam*` harvest blocks and 50 corresponding saplings.
- Most harvest blocks use the age-7 `BlockPamFruit` path; cinnamon, maple, and paperbark use trunk-style
  fruit blocks. The right-click harvest event obtains normal block drops and resets the block, so each
  age-7 block loot table is the strongest data-level evidence for the direct harvest item in this version.
- There are 50 sapling recipes plus 27 raw-nut roasting recipes. Roasted nuts are processed recipe outputs,
  not direct tree harvests, and are therefore outside the approved direct-harvest set.
- `forge:fruits` resolves to only 29 of the 50 direct tree outputs.
- `forge:nuts` resolves to 9 direct outputs.
- `forge:crops` resolves to 47 direct outputs but omits `minecraft:apple`, `minecraft:paper`, and
  `minecraft:string`. The global aggregate can also contain unrelated crops from other mods.
- Therefore neither `forge:fruits` nor aggregate `forge:crops` is a safe and complete selector.
- The actual maple harvest registry ID is `pamhc2trees:maplesyrupitem`; the language file contains a stale
  `item.pamhc2trees.mapleitem` key. Likewise an `appleitem` asset/key exists, but the mature apple tree loot
  is `minecraft:apple`; an assumed `pamhc2trees:appleitem` must not be used as the registry target.
- `pamhc2trees:pam*` block items, saplings, and shear-preserved fruit blocks are not the consumable/direct
  harvest item merely because they belong to the tree implementation.

LOCKED direct-harvest set in this exact Jar version, grouped for audit clarity:

- Fruit-class outputs (Pam's `forge:fruits` members plus the apple-tree output): `minecraft:apple` and
  `pamhc2trees:{apricotitem,bananaitem,breadfruititem,cherryitem,dateitem,dragonfruititem,durianitem,
  figitem,gooseberryitem,grapefruititem,guavaitem,jackfruititem,lemonitem,limeitem,lycheeitem,mangoitem,
  orangeitem,papayaitem,passionfruititem,pawpawitem,peachitem,pearitem,persimmonitem,plumitem,
  pomegranateitem,rambutanitem,soursopitem,starfruititem,tamarinditem}`.
- Nut-tag outputs: `pamhc2trees:{acornitem,almonditem,cashewitem,chestnutitem,hazelnutitem,pecanitem,
  pinenutitem,pistachioitem,walnutitem}`.
- Tree spice outputs: `pamhc2trees:{cinnamonitem,nutmegitem,peppercornitem,vanillabeanitem}`.
- Other direct tree outputs: `pamhc2trees:{avocadoitem,candlenutitem,coconutitem,maplesyrupitem,oliveitem}`,
  plus `minecraft:paper` from paperbark and `minecraft:string` from spiderweb.

The grouped list is now the **LOCKED 50-ID whitelist**. The 2026-09-29 mechanical re-extraction found
50 mature mappings / 50 unique IDs, exactly matching the earlier snapshot. Machine-readable canonical:
[pam_trees_1_0_2_harvests.json](../tools/data/pam_trees_1_0_2_harvests.json). Re-audit before extending support to another version.

#### Verification and historical boundary

[Current results](MASTERY_IMPLEMENTATION_PREPARATION.md#pam-vanilla-51-recipes-result): final distribution Jar Trees present150/150 and absent146/146 PASS.
Pam direct-harvest coverage50/50 plus cocoa, 49+2 recipe split, actual manager/assemble/native consumption,
apple/cocoa FPM, tag boundaries, negative inputs and one reload in each configuration are verified.
Core vanilla/TaCZ67/67 each; build/unit/check PASS. Supplement7 assertion cases passed, including all51
ServerRecipeBook acceptance, but local Food Core1.0.5 has three self-referencing missing-output recipe
errors. Under the user-LOCKED acceptance decision below, these exact upstream defects are non-blocking
for queue #5; the existing evidence satisfies #5 COMPLETE. The old ERROR0-policy FAIL remains historical.
No real-client/JEI UI/authenticated2-client compatibility claim.

Historical §14.59 used50 Pam-conditioned recipes, absent0, present138/absent103/core67×2 and stopped on
Crops cookingoilitem absence. Those results remain valid for that old Jar/specification. The current
supplement resolves those two Crops errors; the new Food Core errors are a separate finding.
The original50 mapping is not redefined. SPEC§14.2 and queue #6 remain unchanged/not started.

#### User-LOCKED acceptance policy for Food Core1.0.5

Food Core1.0.5 exact SHA-256 **1F18655D5EEEA99EECCF0D9D458DBF88D6B59C1A9F00C17E322DB8D55A54ED5B**
remains supported for this Food Healing integration scope with known upstream recipe defects.
Only its own missing-result defects for **pamhc2foodcore:melonpieitem**, **pamhc2foodcore:honeymuffinitem**,
and **pamhc2foodcore:caramelcupcakeitem** are NON-BLOCKING FOR FOOD HEALING QUEUE #5.
These three recipes remain **UNAVAILABLE / BROKEN IN THIS UPSTREAM ARTIFACT**; Food Healing does not repair them.
The exact recipe/result mapping and artifact-bound exception are in [Compatibility§16](COMPATIBILITY_POLICY.md#16-pams-harvestcraft-2---trees--v300-release-required).

Food Healing errors must remain0, all51 product recipes and required Trees/Crops paths must work,
and unexpected external recipe/registry errors must remain0. This replaces the former unconditional
supplement-environment ERROR0 acceptance; it does not permit namespace wildcards, another artifact hash,
cookingoil failures, missing product recipes, classloading/registry crashes, save failure or duplicate delivery.
No external Jar edits, overrides, fake registry entries or product workaround. Crops/Food Core remain optional,
without production hard dependency. No claim of all Food Core functions, client UI or long-term play guarantees.
[Existing-evidence reclassification](MASTERY_IMPLEMENTATION_PREPARATION.md#pam-foodcore-known-upstream-acceptance)
adds no new test execution. The gameplay/FPM/51-recipe contract above is unchanged.

### 14.2 Nutrition / Food Level-based Shokugi count

Status: **FORMAL RELEASE REQUIRED / LOCKED / IMPLEMENTED / AUTOMATED TESTED / queue #6 COMPLETE**

#### LOCKED counting model

The old production unit "one completed food use equals one Shokugi count" is replaced with:

- successful food consumption: add the food item's declared Nutrition
- non-food Food Level increase: add the actual positive Food Level delta
- one Nutrition/Food-Level unit equals one Shokugi count

Examples for a successful food transaction:

- declared Nutrition 1 -> count +1
- declared Nutrition 4 -> count +4
- declared Nutrition 5 -> count +5
- declared Nutrition 8 -> count +8

Full-hunger eating remains. A declared-Nutrition-8 food successfully consumed at Food Level 20 still adds
8 count even though the actual Food Level delta is zero.

For a non-food change from Food Level 10 to 15, add 5 count. Sources include commands, other-mod
abilities, effects, and other legitimate non-food increases. A generic saturation-only increase with no
Food Level increase adds zero count.

#### Exactly-once and subsystem boundaries

- A food transaction uses declared Nutrition only. Its resulting Food Level delta must not be counted again.
  Nutrition 8 consumed at Food Level 12 produces count +8, never +16.
- Food and generic Food Level sources must share transaction/source-aware deduplication so one cause is
  processed once, with server authority and no client prediction double count.
- Satisfaction success still represents a successful food transaction and therefore adds declared Nutrition
  once even though the food stack is not consumed.
- A valid backpack auto-feed may use the same declared-Nutrition rule, but only after dedicated integration
  tests prove no duplicate feeding, healing, Shokugi, Food Diversity, Root Nutrition, or inventory mutation.
- This count change affects Shokugi count only. It does not change Food Healing HP conversion.
- Root and True Root remain food-Nutrition systems. Generic non-food Food Level increases may add Shokugi
  count but must never enter Root accumulation or activate Root.

#### LOCKED threshold, arithmetic and migration — 2026-09-29 10:51 JST

- **Default2000** Nutrition/Food-Level count = +1 level = +1 unspent SP. Configurable; 1000 provisional is rejected. No per-MOD food nerf.
- Config: `general.shokugiNutritionLevelUpRequirement` (long, positive). `general.shokugiLevelUpRequirement` is deprecated legacy input only. Model marker `general.shokugiCountModelVersion=1` is separate from player schema.
- Before Forge default correction, inspect original TOML: explicit new key wins; otherwise Tnew=Told×10. Freeze effective Told as `general.legacyFoodActionThreshold`; retain original file backup and atomically persist once. Model1 never multiplies again. Invalid inputs stop loading without rewriting them.
- Canonical EatCount is partial progress, 0≤count<T. Checked long arithmetic: total=count+units, gained=total/T, remainder=total%T; add gained to level and unspent, preserve spent/skills/stats/toggles. Any invalid/pending/overflow rejects the whole progression update, independently of food/heal/Root/Diversity.
- Normal old partial count becomes **floor(C×Tnew/Told)** exactly once in **FoodHealingDataVersion5**. Default0/1/50/100/199→0/10/500/1000/1990. Custom125/250→1250/2500, 300/400→3000/4000. Explicit Tnew4000/Told250/C125→2000.
- Existing v3 schema4: only count and schema change; level/unspent/spent/skills/base stats/toggles/Root/Diversity are retained. v2 full-respec still grants unspent=L/spent0 exactly once; raw C remains untouched, canonical C converts. Lv2/count35 yields Lv2/count350/unspent2/spent0/raw35.
- Invalid type/negative/out-of-range/overflow or unknown/pending provenance retains raw evidence and blocks migration/progression. No clamp/modulo/reset/reward as repair. Historical player thresholds absent from old NBT cannot be recovered; the first confirmed effective legacy Config is the documented input.
- Generic delta observation is at actual FoodData API mutation boundaries; native food mutation is scoped by its item-food call site. No tick reservation, subtraction after the fact, or proximity inference. Health conversion remains units×2×Recovery; generic gains never enter Root.
- GUI/HUD/command and canonical NBT sync use the new configured threshold, without int narrowing. NBT packet envelope/registration unchanged; protocol7 retained. Separate-JVM, clone/load/sync cannot award another migration SP or repeat conversion.
- Later lowering of the new threshold below an existing partial count does not silently normalize/refund it: progression rejects invalid input pending administrator review. Unsafe private-field writes and backpack auto-feed are not claimed integrated without their own tests.

Evidence: [common plan §14.63](MASTERY_IMPLEMENTATION_PREPARATION.md#nutrition-count-completed) / [TEST_PLAN§30](TEST_PLAN.md#nutrition-count-acceptance).

<details><summary>Historical proposal before 2026-09-29 — superseded, not current instructions</summary>

#### RELEASE-BLOCKING USER DECISION — final threshold

The counting model is release required. The threshold remains a provisional candidate:

**1000 Shokugi count = +1 Shokugi level = +1 SP**

This value is **PROVISIONAL / not LOCKED**. The user must decide the final threshold before the new
count model is implemented. Illustrative rates at 1000 are:

- Nutrition 1 average -> about 1000 meals per level
- Nutrition 4 average -> about 250 meals per level
- Nutrition 5 average -> about 200 meals per level
- Nutrition 8 average -> about 125 meals per level

Large food mods, Food Production Mastery, Botany Pots, auto-feeding backpacks, and automated modpack food
production may make progression faster than this estimate. After real modpack playtesting, candidate values
such as 500-750, 1000, 1500, or 2000 may be compared before locking a final default.

The threshold remains configurable in the eventual design. **Production 200 and its current Config are
unchanged in SJ1.** This temporary runtime preservation does not permit formal release using the old
200-food-actions mode. The final new-unit threshold and treatment of existing customized Config remain
RELEASE-BLOCKING USER DECISIONS; 1000 must not be silently locked.

#### Data-safety decisions required before implementation

- Keep Shokugi level/count/SP in `long` or safer storage.
- Reject negative/invalid increments and define an overflow policy that cannot partially update count,
  level, SP, or synced client state.
- Test transactions that cross one or multiple level thresholds in a single Nutrition increment.
- Decide how existing partial count and customized count-threshold Config values are interpreted when the
  unit changes from food actions to Nutrition/Food Level. Do not silently reduce or inflate existing progress.
- Define v2/v3 migration, sync/save/reload, multiple-threshold level/SP increments and existing-SP
  exactly-once behavior. Never silently multiply or convert old partial EatCount into Nutrition units.
- Add focused unit/GameTest/integration cases before enabling the new model. Preserve Food Healing HP
  conversion and food-only Root/True Root Nutrition. Current runtime stays unchanged until implementation;
  auto-feed integrations remain separate and are not automatically release prerequisites.


</details>

---

## 15. Break Realm Adapter expansion

Status: **LOCKED BEHAVIOR / ARCHITECTURE SPECIFIED / NOT IMPLEMENTED UNTIL VANILLA RELEASE BLOCKERS CLEAR**

This section formalizes implementation architecture for the existing locked Break Realm Mastery skill. It
does not add a new skill and does not authorize Java, Mixin, or dependency work during the current vanilla
stabilization phase.

### 15.1 Legitimate-damage contract

Break Realm allows a supported non-player `LivingEntity` to receive the legitimate final damage calculated
for an attack by a Player who owns and enables Break Realm. The amount is the output of the ordinary
Minecraft and Food Healing RPG damage calculations before the identified external defensive owner rejects,
clamps, revives, or cancels it. Break Realm does not recalculate the hit as target HP, amplify it, or convert
it into an instant kill.

For example, target HP 1,000,000 and legitimate final damage 5,000 must produce at most that legitimate
5,000 damage from this hit. A normal entity whose damage already succeeds receives no fallback damage.
Player targets are always excluded.

### 15.2 Adapter registry

Core code will depend only on a Food Healing-owned `BreakRealmAdapter`-equivalent interface and registry.
The core interface must not expose optional-mod types. Each Adapter must be able to determine:

- whether its target mod and supported version are present without loading absent-mod classes
- whether the target entity and the identified defensive mechanism are owned by that mod
- whether the source attack is attributable to a server-authoritative Player with Break Realm enabled
- which confirmed invulnerability, hurt cancellation, HP clamp, forced heal, revival, death cancellation,
  tick restoration, capability/data, or scripted phase gate must be bypassed for this transaction
- what state, if any, must be restored in transaction cleanup
- whether ordinary death, loot, XP, advancement, quest, and boss-completion flow can remain intact

Use a public API first. If none exists, use only version-audited registry IDs, capabilities, NBT/data,
events, or a narrow conditional Mixin. Reflection and conditional Mixins, when unavoidable, stay isolated in
the compat package. Optional-mod absence must leave Food Healing RPG startup and ordinary combat unchanged.

### 15.3 Exactly-once transaction

Each hit carries a recursion/transaction guard and retains source, attacker, target, original amount, and
legitimate final amount. The transaction rules are:

- successful ordinary damage ends the transaction with no additional damage
- Adapter fallback may occur at most once and may not increase the legitimate final amount
- cleanup/restoration executes even when damage, death, or an Adapter operation fails
- the fallback cannot recursively trigger itself
- Pursuit and other genuine additional hits use distinct transactions
- vanilla hurt-frame bypass and external boss invulnerability bypass are separate concerns
- one cause produces one damage application; no duplicate damage or unbounded retry is allowed

### 15.4 Supported Adapter targets

The formal compatibility targets are L2 Hostility immortal/lethal-prevention traits or affixes, Trial
Monolith, Hyperlink/Fumetsu Wither, Draconic Evolution/Chaos Guardian, Bloodbath/Bloodbath Godzilla, and
other urban-legend or high-difficulty bosses selected for the official Food Healing RPG modpack.

No concrete class name, entity registry ID, trait ID, NBT key, capability, Procedure name, or phase hook is
locked until the exact target version's Jar/source/API is audited. Each Adapter targets only the confirmed
owner mechanism and must avoid altering unrelated AI, identity, phase controllers, loot, XP, drops,
advancements, or normal death flow.

For L2 Hostility, Break Realm bypasses the supported defensive trait only for this Player's attack; it never
removes the trait. Permanent trait removal or logical nullification is Truth Mastery's responsibility.

### 15.5 Failure safety and diagnostics

An unsupported target receives ordinary damage behavior. There is no generic forced-kill fallback. A
rate-limited debug path may record mod ID, entity registry ID, damage source, transaction result, HP
before/after, and observable hurt/death cancellation data so a future dedicated Adapter can be audited.
Diagnostics must not mutate combat state.

### 15.6 Delivery gate

Generic fixtures and absent-mod classloading tests must pass before any external Adapter is enabled. Each
external target then advances separately through `STATIC AUDITED`, `AUTOMATED INTEGRATION TESTED`, and
`MANUAL INTEGRATION TESTED`. Reading a Jar alone never qualifies as integration testing.

---

## 16. Food Production Mastery reversible crafting

**LOCKED / KNOWN AND ALLOWED BEHAVIOR - 2026-09-12 explicit user decision.**

「食料生産の極意による可逆クラフト経由の増加は既知かつ許容された仕様。バグ修正対象外」

A reversible sequence such as nine ingredients -> one storage block -> ingredients may increase
the total item count when Food Production Mastery's ordinary output multiplier applies again to
an eligible crafting result. This increase is intentional and allowed, including a dried-kelp
sequence when it meets the existing eligibility rules. It is not a duplication bug or an exploit
to close. This clarification does not broaden which outputs qualify as food or require new recipes.

Do not add reversible-recipe blacklists, provenance tags to prevent the increase, reverse-crafting
multiplier suppression, arbitrary ingredient exclusions, or compensating consume/refund logic.
Do not change otherwise correct production code merely to prevent this allowed increase.

This permission is limited to the ordinary Food Production Mastery output multiplier. Player-death
drop multiplication, inventory desync, packet replay, reload/ammo refunds, duplicate event processing,
unintended repeated delivery of the same craft result, and unexplained increases from other causes
remain bugs. Each individual craft still consumes one recipe's ingredients and delivers its intended
result once. The existing rule excluding Player death drops from loot multiplication remains absolute.

This is a specification clarification, not a new runtime-test result. Existing PASS scopes and
all unrelated OPEN, compatibility, migration, and release gates remain unchanged.

---

## 17. True Root completed reservation retention

**RETAIN / LOCKED - 2026-09-12 explicit user decision.**

「真・根性で次回発動分を予約した後にスキルをOFFにしても、その予約は破棄せず保持する」

This section is the authoritative rule for an already completed one-activation reservation when
**True Root itself** changes from ON to OFF. It resolves the previous discard-versus-retain choice.

- Do not erase or consume the completed reservation because True Root was switched OFF.
  During ordinary waiting with normal Root still enabled, retain it even when the original active
  window expires while True Root remains OFF. Retention must not depend on switching back ON early.
- While True Root is OFF, do not create a new True Root reservation or activate from the retained one.
  Normal Root Lv5 activation and protection continue under their own existing rules.
- **Post-expiry re-enable / LOCKED (same-day user clarification):** after the original active window
  has expired while True Root was OFF, switching True Root back ON with normal Root enabled consumes
  the retained completed reservation exactly once and activates once. After consumption, further
  OFF-to-ON operations without a newly earned reservation do not activate again.
- Retain only the completed next activation, not a paused invulnerability timer. The original active
  window expires normally. Retention grants no continuing protection after that window expires,
  and repeated ON/OFF operations must not copy the reservation or reset/extend an active deadline.
- The one-reservation cap, Nutrition 18 threshold, 20 SP cost, Root Lv5 prerequisite, Root durations,
  and fixed accumulation window in [SKILL_TREE_SPEC.md sections 4-5](SKILL_TREE_SPEC.md#4-root--根性)
  are unchanged. The already specified enabled-at-window-end activation rule is unchanged.

### Boundaries not decided by this retention choice

| Boundary | Existing specification and remaining question |
|---|---|
| Partial reservation below Nutrition 18 | The existing rule allows accumulation while active and enabled, with a cap of one activation. This completed-reservation decision does not choose discard/retain for partial progress across OFF or expiry. |
| Normal Root itself switched OFF | Existing parent-skill rules prevent Root/True Root protection and activation while normal Root is disabled. The disposition of a completed reservation across that separate parent toggle is not determined here. |
| Death, logout, reload, restart or other lifecycle boundaries | Section 11 locks canonical progression preservation but treats transient combat timers separately. Persisting a field in current code does not by itself lock reservation survival or activation timing across these boundaries. This decision adds no such lifecycle rule and changes no migration policy. |

The retention and post-expiry re-enable rules can be recorded and their implementation gaps identified without deciding these
additional boundaries. Do not silently choose them in a patch or a regression-test expectation.
The original specification-only record was **SPECIFICATION DECIDED**, with implementation and
runtime tests still incomplete. The later authorized implementation on 2026-09-12 passed the
scoped automated regressions; this does not decide the additional boundaries above or establish
real-client coverage. See [CODEX_STATUS.md](CODEX_STATUS.md#現在の要約) for current implementation,
automated-test evidence and the remaining real-client status.


<a id="flight-exact-provider-contract"></a>
## 18. Flight — exact provider ownership contract

LOCKED: 2026-09-29 13:35 JST、利用者のexact-provider方針とEndingLibrary DENY優先の明示回答。Mekanism限定追加: 2026-09-29 19:12 JST。Avaritia限定追加: 2026-09-29 19:54 JST（契約・優先順位不変）。

- v3.0.0は **exact-provider allowlist方式**。vanilla Survival/Adventure/Creative/Spectator、Food Healing自身、実証したFantasy Ending **2.7.20** / EndingLibrary **2.1.19fix**、および承認hashのMekanism **10.4.16** MekaSuit chest＋Gravitational Modulating Unit、承認hashのAvaritia **4.0.3** Infinity Chestplateの経路を対象とする。実際の経路・依存版・hash・未保証範囲は[COMPATIBILITY_POLICY§18](COMPATIBILITY_POLICY.md#flight-exact-providers)が正本。
- `foodhealing:flight`はLv1上限・**2SP・前提なし・取得時ON・toggle可能**を維持。購入は既存server transaction、効果反映は次の正常server END評価に統一する。
- 正常canonical（対応schema読込済み・migration pending=false・不正でない取得Lv1）かつONのServerPlayerだけが新規付与対象。legacy fallback、future/pending、不正level/型/重複取得からの新規付与は禁止。
- FHが実際に`mayfly=false→true`を書いた瞬間だけ、**ServerPlayer object/session transient**の自己tokenを持つ。既存trueの引継ぎ・UUIDだけの帰属・NBTからのtoken復元をしない。
- OFF/資格喪失時、tokenなしならabilities変更0。tokenありなら現在のvanillaとSUPPORTED positive authorityを再照会し、存在すればmayfly/flyingを保持してtokenだけ終了。存在しなければFH-onlyのmayfly/flyingをfalseへ戻しtoken終了。実変化時のみability同期1回。照会不能は付与・破壊的解除を行わず、既存token/値を保持して次評価へ。
- **Survival/AdventureではEndingLibraryのmayflyまたはflyingの負overrideがFHに優先する。** 禁止中はFHの付与/再付与0、外部state変更0。禁止解除後、取得済みONなら通常再評価する。別のSUPPORTED positive authorityの値をFHが強制修復する意味ではない。
- Creative/Spectatorの現在authorityではFHはmayfly/flyingを書かずtokenを終了。EndingLibrary等自身のnative変更をFHが上書きして修復する仕様は追加しない。
- 同ServerPlayerの通常dimension移動ではtokenを維持し通常評価。logout・clone replacement・server stopで破棄。respawn/End return/new loginは新instanceとして評価し旧tokenをcopyしない。
- flyingSpeed/walkingSpeed、gravity/velocity/fallDistance、invulnerability、外部equipment/Curios/capabilityをFHから変更しない。schema **5**・protocol **7**、所有権の永続field追加0。
- **GENERIC FLIGHT PROVIDER ATTRIBUTION = NOT AVAILABLE**。標準booleanにprovider/refcountはなく、unknown foreign-afterは**NOT GUARANTEED / NOT OFFICIALLY SUPPORTED**。未承認Mekanism/Avaritia artifact・別版・未知providerをヒューリスティックで判別したり、今回のblockerへ自動追加しない。

- MekaSuitはexact metadata＋原物SHA-256一致後だけ、native `isGravitationalModulationReady(Player)`をreadonly照会する。飛行中判定`isGravitationalModulationOn`やstale mayflyをauthorityにしない。native module/equipment/energy失効時の解除を妨げず、FHが取得済みONなら次の正常評価で必要な自己再付与を行う。Mekanism独自DENY、energy補充、外部ownership map操作、Jetpack/boost対応は追加しない。Meka positiveとEL DENYが併存してもFH grant/regrantは禁止し、Mekanism native writerの強制解除は行わない。

- Avaritiaはexact id/4.0.3/原物hash一致後だけ、CHESTの`avaritia:infinity_chestplate`をreadonly positive authorityとして照会する（正常胸装備の経路）。full setを要求せず、内部Info/外部classに依存しない。native胸解除のmayfly/flying=falseを妨げず、FH資格あり・ON・EL DENYなしなら次の正常評価で必要な自己再付与を一度行う。外部Info/装備/他能力の修復・新DENYなし。異常slot・全Armor能力・別forkはこの保証に含めない。

現在の実装・試験・購入readinessは[Status](CODEX_STATUS.md#現在の要約)を参照。旧Gate Aの識別不能という技術的事実は維持し、generic解決とは表現しない。


<a id="legacy-armor-mastery-parity-lock"></a>
## 19. 防具の極意 — pre-#8 parity利用者LOCK

本文更新: **2026-09-29 21:42 JST**。**LOCKED / IMPLEMENTED / AUTOMATED TESTED**。20:36時点の仕様記録のみ・ArmorItem限定未修正から、[§14.69](MASTERY_IMPLEMENTATION_PREPARATION.md#legacy-skill-parity-fix-completed)の実装/指定回帰完了へ更新した。旧監査/旧PASSは当時の履歴として保持する。

`foodhealing:armor_mastery`はmaxLevel1・**20SP**。正式対象は、有資格ServerPlayerに帰属する**Minecraft標準damageable ItemStack全般**（armor/weapon/tool/bow/crossbow/shield/fishing rod/modded標準耐久）。cap段階で **amount≤0不変 / 1→1 / >1→1**。ArmorItem限定化は移植事故として解消した。

両耐久skillの入口を`ItemStack.hurt(int, RandomSource, ServerPlayer)`のHEAD引数調整へ一本化し、hurtAndBreak内の旧Redirectは除去。direct hurt・hurtAndBreakのいずれもFH調整1回、余分な確率rollなし。正式順序は **hurtAndBreak時のForge Item.damageItem callback → 共通hurt入口 → ArmorMastery cap → FH不壊 → vanilla Unbreaking enchantment → native damage/break**。direct hurtには元からないItem callbackを新設しない。break callback/shrink/NBT/vanilla RNGを置換しない。

無資格/未取得/OFF・player帰属なし・非damageableは保護対象外。amount≤0・不壊未取得/OFF・非対象ではFH不壊用RNGを消費しない。energy・capability独自耐久・標準durability完全迂回・arbitrary setDamageValueを保証せず、外部値のReflection修復もしない。L2 corrosion/erosionが標準経路を通る場合の通常確率判定とP極意の保護は別物で、L2全耐久破壊完全無効とはしない。

<a id="legacy-multi-stage-parity"></a>
## 20. Legacy multi-stage parity — Gathering / Unbreaking / Pursuit

本文更新: **2026-09-29 21:42 JST**。今回の利用者LOCKに基づく正式maxLevel・SP・効果表は[SKILL_TREE_SPEC§1](SKILL_TREE_SPEC.md#1-existing-skills-and-confirmed-sp)に集約する。

- Gathering：同一IDの3段階。現在のGLMでMinecraft/Fortune/LootTable等が生成した現在の対象stackに最終倍率×2/×4/×6を1回適用する。生成済み3なら6/12/18。旧BreakEvent再抽選/追加ItemEntity方式・path substring分類を復元しない。metadataコピー・stack分割・Player死亡drop非対象を維持。
- Unbreaking：同一IDの3段階。float RNGと旧actual同等の閾値`0.90F / 0.95F / 29.0F/30.0F`を厳密な`<`で比較し、exact equalityは失敗。標準damageable全般・両入口、順序は§19。
- Pursuit：同一IDの9段階。extra hit＝現在有効Lv（最大9）、累積和ではない。同じDamageSource・元LivingDamageEventのcurrent amountを基準に各native target.hurtへ通す。FH outgoing再乗算/再帰を防ぎ、victim armor/Resistance/軽減/Root/外部防御はnative経路に残す。iframe bypass後はfinallyで元値/guard復元、死亡後は中断する。

既存server transactionとAcquiredSkillsのlevel fieldを使い、新ID/schema/protocolを追加しない。初回ON/上位購入OFF保持、long SP・失敗時消費0・保存/同期は[§31受入](TEST_PLAN.md#legacy-parity-future-acceptance)を参照。既存v3取得者のLv/SP/toggleへ遡及請求・返金・削除・最大化なし。新価格は将来の正常購入だけ。v2は食技Lv相当SP全返還/Spent0/LegacyV2Backup永久保持/exactly onceで、旧16/19/999等から段階skillを自動取得しない。

<a id="quarrying-lock"></a>
## 21. 採石の心得 — 利用者LOCK / IMPLEMENTED

本文更新: **2026-09-29 22:38 JST**。`foodhealing:quarrying` max1/1SP/前提なし/default ON/toggle。正常canonicalかつmigration-pending=falseの取得者を対象とする。既存schema5/protocol7の取得/disabled fieldを使う。

- 速度対象はexact `minecraft:deepslate`のみ。Forge BreakSpeedの既存算出値へhardness(deepslate)/hardness(stone)=2を乗じる。同じplayer/tool/effectのstone相当destroy progressとし、Efficiency/Haste/Fatigue等を上書きしない。stone・cobbled/polished/ores/tuff等は不変。clientは同期canonicalを用いる。
- bonusは正常ServerPlayerのstone/deepslate採掘だけ。ForgeのcanHarvestBlockとnative removal成功後のBlock.playerDestroy内で1回だけ資格を作り、GLMの生成済みlootへ追加。FakePlayer/Creative/機械/爆発/無関係context/不適切tool/取消は非対象。scopeはfinallyで閉じる。
- 1個のdoubleからexclusiveに `roll < .005` はiron_ore、`.005 <= roll < .010`はcopper_ore、`.010以上`はnone。各0.5%/合計1%。raw/ingot/deepslate oreへ置き換えない。1harvestにつきFH抽選1、OFFは抽選0。通常lootのrerollや別spawn loopはない。
- 当選quantityだけGathering未取得/OFF=1、Lv1/2/3 ON=2/4/6。確率は不変、Fortuneをbonusへ追加適用しない。通常Silk/Fortune dropと既存Gathering対象の倍率は維持。
- GUIは既存rowを使いGathering隣へ配置。受入結果は[TEST_PLAN§32](TEST_PLAN.md#quarrying-acceptance)。全MODの採掘改変への保証には広げない。

<a id="immovable-lock"></a>
## 22. 不動の極意 — 利用者LOCK / IMPLEMENTED

本文更新: **2026-09-29 22:38 JST**。`foodhealing:immovable_mastery` max1/50SP/前提なし/default ON/toggle、正常server canonical/取得ONだけを対象とする。

- 既存LivingDamage numeric処理のExplosion/Flame/BaseDR/HighDR/Heroics/Kongo後に独立×0.50。対象hookへ到達した正当な数値damageは名称で限定しない。100→50、Explosion100→10→5、Flame100→70→35、Kongo100→20→10、BaseDR50%100→50→25。Rootが防いだdamageを作り直さない。
- direct setHealth(0)/kill/discard/非数値forced death/Soul deathを防ぐ機能ではない。Root/Purificationとの分担を維持する。
- 標準LivingEntity.knockbackはForge LivingKnockBackEvent取消。eventを通らないnative矢/PunchのLivingEntity.push、Explosionの追加velocityとplayer用packet impulseだけをexact Mixinで抑止する。爆発mapの本人vectorはZEROとしpacketからの追加も防ぐ。
- **既存の正当な速度は保持**。毎tickゼロ化/全setDeltaMovement取消/座標固定/attributes/abilities/Flight token操作を行わない。歩行・jump・gravity・swimming・Elytra・flight・vehicle・teleport・portal・piston等の通常移動を対象にしない。
- 他MODの独自deltaMovement/teleport/setter/capability/custom packetによる強制移動は、監査済みAdapterがない限り **NOT GUARANTEED**。実client/HUD/全外部移動を今回自動試験PASSへ転記しない。[TEST_PLAN§33](TEST_PLAN.md#immovable-acceptance)。


<a id="player-facing-terminology-release"></a>
## 23. Player-facing terminology and formal-release delivery — LOCKED

本文更新: **2026-09-30 00:07 JST**。日本語正式表示は **食技**、読みは **しょくぎ / syokugi**、英語正式名称は **Shokugi**。既存Java class/method/package、translation key、Config/NBT/capability/resource IDは既存Shokugiを維持する。旧「食義」はlegacy/過去証拠のみ。skill説明は一般playerが対象・条件・数値を理解できる日本語とし、英語も同じ意味/数値にする。

習得は初回「%sを習得した！」、Lv2以降「%sがLv%sになった！」、基礎ステータスは「%sが上昇した！」。英語はLearned / reached Lv. / increased。正式server成功後だけ1回送信し、失敗時成功表示0。transaction/cost/effect/state/schema/protocolは表示変更のために変えない。実装/自動受入は[共通計画§14.71](MASTERY_IMPLEMENTATION_PREPARATION.md#player-facing-localization-cleanup) / [TEST_PLAN§35](TEST_PLAN.md#localization-message-acceptance)。

**正式完成後のrelease工程として利用者LOCK**：
- GitHub `leva3896/food-healing-mod` を正式v3.0.0 sourceへ更新する。
- GitHub `docs/FOOD_HEALING_RPG_V3_SPECIFICATION.md` に、最終実装を正本とする全機能・隅々の開発引継ぎ用完全仕様書を作成する。
- 正式配布Jar名は **`Food Healing RPG v3.0.0.jar`**。release packaging時に変更する。

完全仕様書・正式名Jar・GitHub main source/docs公開はQueue #9で完成。過去の連携403/Git不在はfresh clone＋通常Git CLIで解消。現在の成果物・GitHub反映状態はRelease ReceiptとStatus冒頭を参照。検証artifact名は`foodhealing-3.0.0.jar`を保持し、正式名は別copyとする。

正式#8実client受入として、正式配布構成の通常title到達、verification metadata警告/補助HUD/人力READY表示/obsolete command候補の不在、GUI通常入口とja/en、正常Quitを確認する。具体手順は[TEST_PLAN§36](TEST_PLAN.md#release-command-verification-acceptance)。今回の静的cleanup/自動dispatcher陰性だけで実画面PASSとしない。
