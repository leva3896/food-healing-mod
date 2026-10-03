# SKILL_TREE_SPEC.md — Food Healing RPG v3.0.0

現行release更新: **2026-10-03 13:00 JST / #1–#9 COMPLETE / RC=YES / FOOD HEALING RPG v3.0.0 FORMAL RELEASE COMPLETE**。現行仕様の単一正本は[Food Healing RPG v3 Specification](FOOD_HEALING_RPG_V3_SPECIFICATION.md)。本書の詳細LOCK・日付付き記録は根拠/履歴として保持し、古いRC/次工程の記載は現行指示にしない。

過去の侵略者限定追加: 2026-09-15 21:11 JST。実装・自動検証の証拠は[共通計画§11](MASTERY_IMPLEMENTATION_PREPARATION.md#trial-invader-result)。当時は両極意全体完成・購入解放ではなかった。現在の購入ready接続は共通計画§14.58で完了。

本文更新: **2026-09-29 23:31 JST**。今回の利用者明示判断に基づき全SP再調整と採石/不動を **LOCKED / IMPLEMENTED / AUTOMATED TESTED**。[共通計画§14.70](MASTERY_IMPLEMENTATION_PREPARATION.md#skill-cost-rebalance-quarrying-immovable-completed)。以下を現在の正式値とし、将来の利用者明示変更を妨げる永久凍結とはしない。前工程parity4技能の効果・上限は維持。

関連仕様更新: 2026-09-28 18:50 JST。**旧Nutrition19以上だけによる高栄養食品ボーナス廃止 = RESOLVED / LOCKED / v3.0.0 FORMAL RELEASE REQUIRED**。正本は[SPEC§12](SPEC.md#legacy-high-nutrition-retirement)。耐火/金剛等は正式取得＋ON＋skill固有条件を入口とし、Nutritionだけの無料grant/refreshを認めない。Nutritionを使う回復/Root/True Root等は維持。移行時の出所不明effect一括remove禁止・外部effect保全も同節に従う。今回仕様記録のみ、skill費用・購入gate・SP・production変更なし。

## 1. Existing skills and confirmed SP

| Skill | SP | v3 effect |
|---|---:|---|
| 耐火の心得 | 1 | permanent/managed Fire Resistance while enabled |
| 水月と暗視の心得 | 1 | Water Breathing + Night Vision |
| 早食いⅠ | 10 | food use time approximately half |
| 軽業の心得 | 1 | fall damage nullification |
| 炎の加護 | 2 | while burning, incoming damage ×0.70 |
| 爆破耐性の心得 | 10 | explosion damage ×0.10 |
| 浄化 | 3 | remove/prevent harmful MobEffects safely |
| 食料生産の極意 | 4 | food production ×2 using safe result/transaction integration |
| 屠殺の心得 | 5 | legacy friendly/non-hostile mob drop multiplier; Player is NEVER a target |
| 満足感 Lv1 | 5 | 25% food non-consumption |
| 満足感 Lv2 | 5 | 50% food non-consumption |
| 満足感 Lv3 | 5 | 75% food non-consumption |
| 採石の心得 | 1 | `foodhealing:quarrying`、max1/前提なし/初回ON/toggle。深層岩の石相当速度、正常採掘で鉄0.5%・銅0.5%。[SPEC§21](SPEC.md#quarrying-lock) |
| 採取の心得 Lv1–3 | 5 / 5 / 5（累計15） | `foodhealing:gathering`、生成済み対象dropの最終倍率×2 / ×4 / ×6。GLM・Fortune後の結果に1回適用、再抽選なし |
| 不壊の心得 Lv1–3 | 10 / 20 / 30（累計60） | `foodhealing:unbreaking`、無効率90% / 95% / 29⁄30（比較は厳密な `<`）。有資格ServerPlayerの標準damageable全般、hurtAndBreak/direct hurt |
| 防具の極意 Lv1 | 20 | `foodhealing:armor_mastery`、有資格ServerPlayerの標準damageable全般。amount≤0不変・1→1・>1→1。[SPEC§19](SPEC.md#legacy-armor-mastery-parity-lock) |
| 飛翔の心得 | 2 | survival creative-style flight without revoking other mods' flight |
| 金剛の心得 | 50 | managed Resistance IV |
| 不動の極意 | 50 | `foodhealing:immovable_mastery`、max1/前提なし/初回ON/toggle。数値damage独立×0.50・標準knockback抑止。[SPEC§22](SPEC.md#immovable-lock) |
| 追撃の心得 Lv1–9 | 各100（累計900） | `foodhealing:pursuit`、extra hit数＝現在Lv。native hurtとiframe/guard復元を維持 |

Current normal/Shokugi-group total: **1,150 SP**。Root **70**、Heroics **250**、高難易度 **620**、TaCZ有限 **500**、有限総額 **2,590 SP**。repeatable base statsは除外。実registry機械集計一致。旧1,025/1,821は前工程の履歴。

### Existing multi-stage legacy normalization

**RESOLVED / LOCKED / IMPLEMENTED / AUTOMATED TESTED — 2026-09-29 21:42 JST**。

Gathering / Unbreaking / Pursuitは同一IDのmax3 / 3 / 9のleveled skill。費用・効果は上表を正本とする。登録済みserver購入transactionで現在Lv→次Lvだけを購入し、stale/replay/最大超過/不正canonical/pendingはSP消費0。初回取得は既存default ON、上位購入は保存済みOFFを保持する。generic GUIのlevel/max・next cost・不足SP・maxed・toggleは共通定義を使う。

不壊/防具の極意は共通`ItemStack.hurt(int, RandomSource, ServerPlayer)`入口で1回だけ調整する。処理順・対象外は[SPEC§19](SPEC.md#legacy-armor-mastery-parity-lock)、購入/保存の受入は[TEST_PLAN§31](TEST_PLAN.md#legacy-parity-future-acceptance)。L2全耐久破壊の完全免疫という旧説明は採用しない。

**OPEN-05のmigration判断はRESOLVED / LOCKED維持**：legacy sublevelからv3 nodeを自動取得/自動level化しない。食技Lv相当SP全返還・Spent0・LegacyV2Backup永久保持・exactly once。既存v3の取得Lv・SP・toggleも遡及補正せず、新費用は今後の正常購入だけに適用する。schema5/protocol7を維持。

履歴：20:36監査時点はLv1-only（2/3/4SP）で上位効果へ正常到達不可だった。[旧監査§1–7](LEGACY_SKILL_PARITY_AUDIT.md)と[共通計画§14.68](MASTERY_IMPLEMENTATION_PREPARATION.md#legacy-existing-skill-parity-audit)は当時の事実として保持。今回の利用者決定はfresh購入構造を変更し、旧データ自動取得禁止とは分離する。

---

## 2. Satisfaction — absolute separation from TaCZ

### LOCKED

Satisfaction is food-only.

- Lv1: 25%
- Lv2: 50%
- Lv3: 75%

One random roll per item-use.

The player-facing result on success is “food was not consumed”.

### MUST FIX

v2.2.5 consumes then refunds/replaces a result stack. Rewrite.

Must preserve:
- special result stacks
- bowls/containers
- NBT/components
- modded food
- final single item
- full inventory
- multiplayer

### ABSOLUTE RULE

Do not associate Satisfaction with:
- ammo conservation
- TaCZ overheat
- TaCZ ammo
- TaCZ damage

Legacy coupling must be removed.

---

## 3. Food Production Mastery / 食料生産の極意

Cost: **4 SP**

Renamed from legacy `豊穣`.

### Vanilla crafting

Result must be doubled in the proper result/transaction path:

- 1 → 2
- base recipe 4 → 8
- ingredients consumed for one normal recipe
- normal click
- shift-click
- 2×2
- 3×3

Do not craft 1 and later `Inventory.add()` a second copy.

### Reversible crafting - LOCKED / KNOWN AND ALLOWED BEHAVIOR

**2026-09-12 explicit user decision:** item-count growth caused by Food Production Mastery's
ordinary multiplier across reversible crafts is known and allowed, not a bug to fix. This includes
ingredient -> storage block -> ingredient cycles, such as dried kelp, when the existing food-result
eligibility permits them. Do not blacklist reverse recipes, add provenance-based prevention,
disable their multiplier, arbitrarily exclude ingredients, or offset the increase with consume/refund.

The 4 SP cost, output eligibility, x2 result transaction, and one-recipe ingredient consumption are
unchanged. Player-death drops, replay/desync, duplicate events, unintended repeat result delivery,
and unrelated ammo/reload duplication remain bugs. See [SPEC.md section 16](SPEC.md#16-food-production-mastery-reversible-crafting)
for the authoritative boundary; this record does not claim a new integration-test PASS.

### Farmer's Delight

Support standard Farmer's Delight cooking mechanisms, especially Cooking Pot.

Addons using the same standard FD recipe type should inherit support automatically.

Do not claim automatic support for addon-specific machines with independent recipe systems.

### Furnace / smoker / campfire

Food output produced by standard:
- smelting
- smoking
- campfire cooking

should support the skill.

For shared BlockEntity/machine output:
- manual extraction by a skill-owning player → ×2 transaction
- hopper / fully automatic extraction → normal ×1
- do not mutate the shared result inventory into a player-specific doubled value

Standard mod recipe types that behave equivalently may be supported if safe.

---

## 4. Root / 根性

### Trigger

A Root activation becomes eligible when either:

A. one consumed food has **Nutrition >= 18**, OR  
B. consumed foods within **15 seconds** have cumulative Nutrition >=18.

**LOCKED clarification (user decision, 2026-09-05): fixed accumulation window.**
The first eligible food opens a 300-tick window. Later foods within that window add Nutrition
without moving its deadline. The exact 300-tick boundary is included. If the threshold has not
been reached after that deadline, reset the accumulation; the next eligible food opens a new
window. This is not a rolling/sliding window. Activation resets the accumulation and discards
excess Nutrition. Active/cooldown and True Root reservation rules below remain unchanged.

Important:
- use declared food Nutrition, not actual hunger-gauge increase
- works while hunger is full
- once threshold is reached, cumulative amount resets to 0
- excess Nutrition is not carried forward
- non-food Food Level gains DO NOT trigger Root
- Food Level gains may still heal via the core healing system

### Levels

| Skill | SP | Invulnerability | Cooldown after invulnerability |
|---|---:|---:|---:|
| 根性 Lv1 | 10 | 2 sec | 20 sec |
| 根性 Lv2 | 10 | 4 sec | 15 sec |
| 根性 Lv3 | 10 | 6 sec | 10 sec |
| 根性 Lv4 | 10 | 10 sec | 5 sec |
| 根性 Lv5 | 10 | 15 sec | 0 sec |

Total Lv1–5: **50 SP**。True Root20を含むRoot系70SP。発動時間/固定窓/予約等は不変。

Levels are sequential prerequisites.

### Protection while active

Root Lv1–5 must protect against, where safely interceptable:

- ordinary damage
- rapid multi-hit
- damage that bypasses normal invulnerability
- direct forced death
- direct kill
- direct HP-to-zero
- death-event bypass techniques

This protection exists **only while Root is active**.

Do not globally make the player immortal outside the active window.

### Reservation rule Lv1–5

During Root active/cooldown:
- do not bank Nutrition for the next activation
- new accumulation begins only when activation is available again

---

## 5. True Root / 真・根性

Cost: **20 SP**  
Prerequisite: 根性 Lv5

This is not displayed as “Lv6”.

### Effect

Same protection class as Root, plus:

- while Root is active, consumed Nutrition can be banked for **one next activation**
- cap reservation at one activation
- once reserved amount reaches 18, mark next activation ready
- while enabled, when current invulnerability ends, immediately reactivate if reservation is ready
- consume/reset the reservation used for that activation
- during the next window, a new one-activation reservation can be built
- no 2x/3x/10x stored chain

### Completed reservation when True Root is switched OFF

**RETAIN / LOCKED (2026-09-12):** preserve an already completed reservation across True Root's
own ON-to-OFF transition. The same-day clarification also locks one-time use on post-expiry
re-enable with normal Root enabled. Scope, unchanged active deadlines and other boundaries are defined in
[SPEC.md section 17](SPEC.md#17-true-root-completed-reservation-retention).
Recording this decision does not mark its implementation or tests complete.
The later 2026-09-12 implementation and automated results are recorded separately in
[CODEX_STATUS.md](CODEX_STATUS.md#現在の要約). The 9/12 reservation cases and 9/13 protection/unchanged deadline have LIMITED real-client evidence; untested lifecycle boundaries remain separate.

### Auto-feeding

Traveler's Backpack / Sophisticated Backpacks auto-feeding may satisfy this reservation.

This strong synergy is **intentional** and must not be nerfed merely because it can sustain Root against Trial Monolith-class content.

---

## 6. Heroics / 火事場

Normal Heroics activates while HP <= **40% max HP**.

Each level below describes the final effect at that level; do not multiply Lv1×Lv2×Lv3 together.

| Level | SP | Outgoing damage | Effective Armor/Toughness | Heroics DR |
|---|---:|---:|---:|---:|
| Lv1 | 30 | ×2.0 | ×2 | 10% |
| Lv2 | 30 | ×2.5 | ×4 | 20% |
| Lv3 | 30 | ×3.0 | ×8 | 30% |
| Lv4 | 30 | ×4.0 | ×16 | 40% |
| Lv5 | 30 | ×5.0 | ×32 | 50% |

Total Lv1–5: **150 SP**。True Heroics100を含むHeroics系250SP。効果倍率は不変。

Levels are sequential prerequisites.

### Heroics DR

Separate multiplicative reduction layer.

Do not add percentages with:
- Base Damage Reduction
- High-Difficulty Base Damage Reduction
- Resistance effect

### Armor/Toughness

Use the player's legitimate current Armor/Toughness as the source value.

Do not permanently rewrite another mod's attributes to 2x/4x/...

Use safe owned modifiers or effective damage-calculation logic.

---

## 7. True Heroics / 真・火事場

Cost: **100 SP**  
Prerequisite: 火事場 Lv5  
Not displayed as “Lv6”.

Activation: HP <= **80%**

Effect:
- outgoing damage ×20
- Armor ×64
- Armor Toughness ×64
- Heroics-specific damage reduction **99%**

This skill itself is not the direct-kill protection system. Direct-death protection is handled by active Root or Purification Mastery.

---

## 8. Break Realm Mastery / 破界の極意

Cost: **20 SP**（価格のみ変更。既存の実装/購入gate・Adapter expansion開始条件は不変）。

Design goal: available relatively early because L2-style undying/invulnerability can appear early.

Prerequisites must remain shallow/light.

### Effect

For non-player LivingEntities, attacks from a player owning this skill can bypass supported:

- invulnerability
- immortality
- revival
- death cancellation
- related defensive gates

so that **the player's legitimate calculated damage** can be applied and normal death flow can finish.

### Not allowed

- no infinite damage
- no direct entity deletion
- no `discard()` as the normal kill mechanism
- no bypassing loot/XP/advancement/quest/death events
- no Player/PvP application

Example:

Enemy HP 10,000,000  
Player attack 5,000  
→ still 5,000 damage; the skill removes the “0 because invincible” rule, not the HP pool.

### LOCKED interpretation

Break Realm is neither an instant-kill skill nor an infinite-damage skill. It allows the legitimate final
damage produced by the normal Minecraft and Food Healing RPG damage pipeline to pass through a supported
external mod's identified invulnerability, damage-cancel, health-clamp, revival, death-cancel, or immortal
trait gate. It must not replace that final amount with the target's health or any larger synthetic amount.

The target is limited to a non-player `LivingEntity`. The attacker must be a Player whose server-authoritative
canonical data says Break Realm is acquired and enabled. The skill never applies to a Player target.

### Absolute prohibitions

- no `Float.MAX_VALUE`, infinite, NaN, or otherwise extreme synthetic damage
- no `discard()`, `remove()`, unconditional `setHealth(0)`, or per-tick HP-zero loop
- no forced deletion that skips loot, XP, death events, advancements, quests, or boss completion
- no global disabling of another mod's invulnerability system
- no guessed registry IDs, classes, trait IDs, NBT keys, or Procedure names
- no unconditional reference to an optional-mod class

### Damage transaction boundary

A Break Realm transaction must retain the damage source, attacker, target, original amount, and legitimate
final amount. If the normal hit already succeeded, it must add no damage. An Adapter fallback may retry at
most once, under a recursion guard, and must apply the same legitimate final amount exactly once. Pursuit or
another real additional hit remains a separate transaction; vanilla hurt-frame handling is not a substitute
for a boss-specific invulnerability Adapter.

### Truth Mastery boundary

Break Realm means **allow this Player's legitimate damage to pass**. It does not permanently remove a trait
or affix. Permanent supported trait nullification belongs to Truth Mastery.

### Implementation status

**BREAK REALM ADAPTER EXPANSION SPECIFIED / NOT IMPLEMENTED.** Implementation begins only after the current
vanilla release blockers and manual regression work are complete. Named external-mod internals remain
unlocked until the exact target version's Jar, source, or public API has been audited.

---

## 9. Purification Mastery / 浄化の極意

Cost: **100 SP**

### Acquisition - OPEN-02 RESOLVED / LOCKED (2026-09-13 user decision)

The only prerequisite is acquired normal 浄化 **Lv1** (`foodhealing:purification`).
This node is `foodhealing:purification_mastery` Lv1 and costs **100 SP** itself.
No additional skill, Shokugi level, quest, fee or parent-ON requirement is imposed.
Keep the data-driven requirement list capable of multiple prerequisites.

The earlier policy requiring unspecified additional high-difficulty investments is
superseded for v3.0.0. Missing additional user decisions must no longer block purchase.
Acquisition requirements and implementation readiness are independent: do not spend
SP while this node's effect/required adapter remains unimplemented or unready.
Report missing prerequisites, insufficient SP, effect/adapter readiness and unavailable
target MOD/version separately. This decision does not complete the effects below or
change effect-time parent dependencies/toggle rules.

<a id="purification-mastery-activation"></a>

### Effect activation — Q2 RESOLVED / LOCKED (2026-09-15 user decision)

Normal Purification and Purification Mastery must both be acquired at Lv1 and ON.
Parent OFF/mastery ON, parent ON/mastery OFF, and both OFF grant no mastery protection.
Missing ownership, invalid canonical data or migration-pending data grant none.
Evaluate the attacked server player's own canonical data at the attack; never use a
client assertion. Parent OFF does not rewrite the mastery node's stored ON setting.
No new timer, cooldown, auto-ON or refund is added. This is an effect condition, not
a parent-ON purchase prerequisite. Truth Mastery's separate effect-time decision is recorded in
[section 10](#truth-mastery-activation). The effect condition remains LOCKED; both masteries are PURCHASE READY after the later §14.58 implementation/acceptance. SP protection remains in force.

### Self-directed hostile trait nullification

While enabled under the activation rule above, supported hostile high-difficulty traits should fail against this player, including conceptually:

- harmful potion traits
- Drain
- Corrosion / Erosion
- Dementor / Dispelling
- Killer Aura
- Ragnarok
- Pulling / Repelling
- other supported hostile Trait/Affix effects

Do NOT delete the mob's trait for this skill.

### Forced-death immunity

Persistent immunity, where safely interceptable, to:
- direct kill
- direct HP0
- forced death
- death-event bypass
- direct forced entity removal used as an attack

### Critical rule: real damage still passes

If an attack has a legitimate damage amount, Purification Mastery does not nullify it just because it is huge.

Examples:
- 100 damage → passes
- 1,000,000 damage → passes
- 1,000,000,000 damage → passes

It then goes through Food Healing's normal defense/reduction pipeline.

For Trial Monolith Soul Damage:
- actual numeric damage portion remains damage
- separate forced-death/death-bypass portion may be blocked

The 2026-09-15 **Q1 RESOLVED / LOCKED** Damage Cube decision and its exact 1.4.9
attack scope are authoritative in [COMPATIBILITY_POLICY §7](COMPATIBILITY_POLICY.md#trial-monolith-149).

### Invader Monolith 1.4.9 — attack-specific exception LOCKED (2026-09-15)

Under the same personal activation predicate, block identified Invader-origin new Soul
addition/forced Soul addition and its hostile request to clear the target's existing
Soul Protection. Never turn an existing false flag true, heal old Soul damage or restore HP.
Soul Protection is an external flag, not a MobEffect. Prevent an identified Invader buff
removal at its own call site if found; do not invent a removal path, freeze natural effect
expiry, block legitimate removal, or constantly reapply buffs.

**Explicit numeric exception:** the approved Invader-owned HugeBeamEntity attack's
Float.MAX_VALUE hurt is also blocked. Identify its actual owner and that exact call site,
not a damage threshold or shared DamageType alone. Small Beam, Damage Cube, other owners
of the shared beam classes and other large numeric attacks retain their normal numeric
processing. Truth ownership/ON is not required. This does not change Break Realm,
Root, acquisition/100SP, saved toggles or either mastery purchase block.
Exact artifact/path boundaries: [Compatibility §7](COMPATIBILITY_POLICY.md#trial-invader-149).

---

## 10. Truth Mastery / 真実の極意

Cost: **500 SP**

### Acquisition - OPEN-03 RESOLVED / LOCKED (2026-09-13 user decision)

The only prerequisite is acquired 浄化の極意 **Lv1** (`foodhealing:purification_mastery`).
This node is `foodhealing:truth_mastery` Lv1. There are no additional prerequisites
in v3.0.0; an acquired parent may be OFF for purchase eligibility. The acquisition
order is normal 浄化 → 浄化の極意 → 真実の極意. The 100/500 SP values are per-node costs,
not an assertion that the whole chain including normal 浄化 costs 600 SP (normal 浄化
currently costs 3 SP). The independent implementation/readiness rule in section 9 applies.

Future extra prerequisites require a new user decision. Do not mark today's graph
OPEN because it may change later. Application to existing owners, repurchase,
refunds or loss of ownership in a future update is undecided and not implemented.
Effect-time conditions are separately locked below; they do not add purchase prerequisites.

Old temporary name `帰零の領域` is retired. Do not use it as the display name.

<a id="truth-mastery-activation"></a>

### Effect activation — RESOLVED / LOCKED (2026-09-15 user decision)

Recorded: 2026-09-15 17:20 JST.

Apply **new permanent nullification** only while normal Purification,
Purification Mastery and Truth Mastery are all acquired at Lv1 and all ON.
Evaluate the acting server player's valid canonical data; migration-pending or
invalid data does not qualify. If any one is OFF, stop applying to new targets.
Already permanently nullified traits do not return when a skill is switched OFF
or the mob leaves the area. Never rewrite another skill's saved ON/OFF setting
when one is toggled.

This is an effect-time condition only. The acquisition prerequisite and 500 SP
cost above remain unchanged; purchase does not require any of these skills to be ON.
This was only a specification decision at the time. Later implementation and purchase readiness are complete within the accepted scope (common plan §14.58); SP protection remains in force.

### Area

Player-centered axis-aligned box:

- X: ±75
- Y: ±75
- Z: ±75

Conceptual dimensions: 151×151×151.

### Trigger

Apply when a supported mob:
- spawns inside
- enters from outside
- teleports inside
- becomes newly covered because the player moves

### Effect

Permanently strip/nullify supported high-difficulty Trait/Affix/Modifier abilities from that mob.

After leaving the area, the stripped/nullified state remains.

Persist across chunk unload/reload when feasible.

### Do not remove unrelated mob identity

Do not indiscriminately strip:
- AI
- EntityType
- normal HP
- normal attack damage
- movement
- base boss phases/story state
- loot table
- arbitrary Capability data

unless a specific supported trait system legitimately owns the data being removed.

### Safe implementation

Prefer:
1. safe public trait removal
2. otherwise Food Healing-owned persistent logical-nullification marker

Must be idempotent.

### Performance

Never scan every block/entity in the full 151³ region every tick.

Prefer spawn/entry tracking and a low-frequency fallback.

---

## 11. TaCZ — independent tree

### Absolute isolation

Ammo Conservation is completely independent from every non-TaCZ skill.

### Ammo Conservation Lv1–10

Each level costs **50 SP**.  
Total: **500 SP**.

| Lv | Ammo conservation | Separate TaCZ damage multiplier |
|---:|---:|---:|
| 1 | 10% | ×1.05 |
| 2 | 20% | ×1.10 |
| 3 | 30% | ×1.15 |
| 4 | 40% | ×1.20 |
| 5 | 50% | ×1.25 |
| 6 | 60% | ×1.30 |
| 7 | 70% | ×1.35 |
| 8 | 80% | ×1.40 |
| 9 | 90% | ×1.45 |
| 10 | 100% | ×1.50 |

From **Lv1 onward**, applicable TaCZ overheat gauges are disabled.

**2026-09-09 LOCKED SPEC:** On acquisition/default ON or OFF to ON, clear existing
heat/overheat lock once on the equipped supported gun through TaCZ's canonical
API and synchronize it. When already ON, do the same once when a hot supported
gun becomes active. Do not change unequipped guns. ON prevents new shot heat;
OFF restores normal TaCZ processing from current state without restoring past
heat. Toggle-based immediate relief is intentional for acquired/enabled players.
No per-tick resets, inventory-wide clearing, client-only concealment, or
server-only lock bypass. See `SPEC.md` section 10.8 for transition invariants.

### TaCZ missing

Nodes remain visible but locked.

Exact user-facing message:

**「TaCZ導入時のみ利用可能」**

Server must reject purchase and consume **0 SP**.

### Separate TaCZ base stat

Do not confuse Ammo Conservation's damage multiplier with the repeatable:

**TaCZ Base Outgoing Damage: 2 SP → +0.01x**

---

## 12. Repeatable base-stat SP

| Stat | Cost |
|---|---:|
| Base Defense +5 | 5 SP |
| Base Damage Reduction +1 stage | 10 SP |
| High-Difficulty Base Damage Reduction +1 stage | 10 SP |
| Base Max HP +2 | 5 SP |
| Food Healing Recovery Multiplier +0.25x | 20 SP |
| Base Outgoing Damage +0.10x | 20 SP |
| TaCZ Base Outgoing Damage +0.01x | 2 SP |

High-Difficulty Base Damage Reduction allocation requires Auto Leveling OR L2 Hostility installed.

---

## 13. Skill IDs

Current stable resource IDs are implementation identifiers, not display text:

- `foodhealing:fire_resistance`
- `foodhealing:water_night_vision`
- `foodhealing:fast_eating`
- `foodhealing:acrobatics`
- `foodhealing:flame_blessing`
- `foodhealing:explosion_resistance`
- `foodhealing:purification`
- `foodhealing:food_production_mastery`
- `foodhealing:slaughter`
- `foodhealing:satisfaction`
- `foodhealing:quarrying`
- `foodhealing:gathering`
- `foodhealing:unbreaking`
- `foodhealing:armor_mastery`
- `foodhealing:flight`
- `foodhealing:kongo`
- `foodhealing:immovable_mastery`
- `foodhealing:pursuit`
- `foodhealing:guts`
- `foodhealing:true_guts`
- `foodhealing:heroics`
- `foodhealing:true_heroics`
- `foodhealing:break_realm_mastery`
- `foodhealing:purification_mastery`
- `foodhealing:truth_mastery`
- `foodhealing:tacz_ammo_conservation`

These existing IDs remain unchanged when display names are edited. Persistent IDs must never be localized display strings.

新価格は今後の正常購入だけに適用する。既取得Lv/Spent/未使用SP/point count/toggleを再計算・遡及請求・返金しない。新2skillも既存AcquiredSkills/DisabledSkillsと登録packetを使用し、schema5/protocol7のまま。v2移行の全額SP返還/Spent0/backup/exactly once/自動取得0は不変。


現在の日本語名称は食技（しょくぎ）、英語Shokugi、内部ID不変。表示/習得メッセージとrelease要求の正本は[SPEC§23](SPEC.md#player-facing-terminology-release)、今回の検証は[共通計画§14.71](MASTERY_IMPLEMENTATION_PREPARATION.md#player-facing-localization-cleanup)。価格2590・repeatable費用・効果・gateを表示都合で変更しない。
