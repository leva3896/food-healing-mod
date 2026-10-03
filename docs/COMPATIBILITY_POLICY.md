# COMPATIBILITY_POLICY.md — Food Healing RPG v3.0.0

現行release更新: **2026-10-03 11:22 JST / #1–#8 COMPLETE / #9 PARTIAL / RC=YES / LOCAL RELEASE ARTIFACT AND SPECIFICATION COMPLETE / GITHUB UPDATE BLOCKED**。現行仕様の単一正本は[Food Healing RPG v3 Specification](FOOD_HEALING_RPG_V3_SPECIFICATION.md)。本書の詳細LOCK・日付付き記録は根拠/履歴として保持し、古いRC/次工程の記載は現行指示にしない。

現行互換方針の追加更新: 2026-09-30 07:19 JST。[§19 FE shader正式内蔵](#fe-uniform-built-in)。侵略者限定追加の過去更新（2026-09-15 21:11 JST）は[共通計画§11](MASTERY_IMPLEMENTATION_PREPARATION.md#trial-invader-result)を参照。P/T購入readinessの現行状態は[Status](CODEX_STATUS.md#現在の要約)。

## 1. Global compatibility policy

Food Healing RPG is intended to be safe inside large modpacks.

Core principle:

> Do not “win” compatibility by overwriting another mod's global state.

Prefer:
- Forge events
- public APIs
- standard recipe/loot systems
- owned modifiers/data
- optional Adapter packages

Avoid:
- global private-field Reflection
- broad mixins
- class-name substring detection
- every-tick state overwrites
- consume/refund duplication patterns

---

## 2. Optional-mod loading

Every optional integration must:
- be guarded by proper mod-presence checks
- live in an isolated compatibility layer where practical
- avoid direct classloading when the dependency is absent
- never make Food Healing fail to start because the optional mod is missing

Suggested structure:

`compat/<modid>/...`

---

## 3. AttributeFix

Status: **Priority 0 optional compatibility**

### Required

- Food Healing must work without AttributeFix.
- Food Healing must work with AttributeFix.
- AttributeFix is not a mandatory dependency.
- Food Healing owns only its modifiers/progression.
- Never remove/overwrite AttributeFix's changes.

### MUST FIX from v2.2.5

Remove the global Reflection-based mutation of `Attributes.MAX_HEALTH` maximum.

Preserve Food Healing config support for theoretical max HP up to 1e12, while acknowledging vanilla float precision.

Test:
- no AttributeFix
- AttributeFix installed
- huge Food Diversity/base HP
- death/respawn
- End return
- login/restart
- Heroics Armor/Toughness
- duplicate-modifier detection

---

## 4. TaCZ

Status: **explicit optional compatibility**

### Absolute rules

- TaCZ is optional.
- Ammo Conservation is independent of Satisfaction/Gathering/Unbreaking/Armor Mastery/etc.
- TaCZ Base Damage repeatable stat remains separate.
- Ammo Conservation Lv1–10 remains separate.

### MUST FIX

v2.2.5 contains TaCZ-related Mixin/direct class coupling and legacy ammo behavior tied to old Shokugi/Satisfaction progression.

v3:
- isolate TaCZ code
- no class loading when TaCZ absent
- remove legacy non-TaCZ skill coupling
- server validate ammo-skill level
- overheat disable starts at Ammo Conservation Lv1
- 5 SP each level
- damage bonus x1.05..x1.50
- base TaCZ stat +0.01x/SP remains independent

Test TaCZ absent as a first-class scenario.

**2026-09-09 LOCKED SPEC:** Apply the one-shot heat normalization transitions in
`SPEC.md` section 10.8 using actual TaCZ APIs and a verified active-gun lifecycle.
Only TaCZ **1.1.7-hotfix2** is authorized for the current Adapter implementation
and integration run. Do not broaden the version gate; absent/unsupported or
unready Adapter states must reject purchase without spending SP. No gunpack or
other TaCZ version becomes tested by association. Client/server heat and lock
must agree; OFF restores ordinary processing without restoring historical heat.

Implementation boundary (2026-09-09): the exact-version conditional Mixins remain
required when applicable. The Adapter normalizes only acquisition/enable/active-draw
transitions through TaCZ setters, synchronizing that inventory slot with Minecraft's
canonical stack packet. A preserved manual-action round stays in the chamber;
a separate transient mechanical-cycle acknowledgement allows the native bolt timing
to complete without feeding a second round. This acknowledgement carries no ammo
and does not change client inventory. TaCZ's ordinary reload and cooling remain owned
by TaCZ. Network protocol **6** requires matching updated client/server builds.
The client hook bytecode audit is not a client-runtime or visual-prediction PASS.

---

## 5. L2 Hostility

Status: **major high-difficulty compatibility target**

Use proper APIs/data where possible.

### High-Difficulty Base Reduction
Allocation enabled when L2 Hostility OR Auto Leveling is installed.

### Purification Mastery
Player-specific nullification; do not delete mob Trait.

### Truth Mastery
May physically remove supported traits only if safe; otherwise persistent logical nullification.

### Break Realm Mastery
Bypass supported invulnerability/Undying/revival gates while preserving normal damage/death/loot flow.

Do not treat L2 as permission to globally bypass every custom boss phase in every mod.

---

## 6. Auto Leveling

Status: high-difficulty compatibility target.

- Presence enables High-Difficulty Base Damage Reduction allocation.
- Avoid assumptions about internal class names.
- Build an Adapter only for behavior Food Healing actually needs.
- Do not claim full Auto Leveling compatibility without integration testing.

---

## 7. Trial Monolith

Status: major high-difficulty integration-test target.

### Defensive rule

Soul Damage or equivalent:
- if it has an actual numeric damage amount, that part is real damage
- Purification Mastery does not simply set it to zero; the explicitly locked Invader Huge Beam call-site exception below is separate
- Food Healing universal reduction may apply if a safe interceptable damage event exists
- active Root is intended to protect during its invulnerability window, including special/instant-death layers where integration can safely intercept
- separate forced-death/death-bypass layer is blocked by Root while active and Purification Mastery persistently

<a id="trial-monolith-149"></a>

### Damage Cube 1.4.9 — Q1 RESOLVED / LOCKED (2026-09-15)

For the approved `the_trial_monolith` 1.4.9 artifact, prevent new Damage Cube
SoulDamage accumulation, the resulting effective HP-cap reduction and forced actions.
Preserve its separate ordinary numeric `cube_attack` hurt, arguments, count and
subsequent motion processing; legitimate lethal numeric damage still follows normal
defense/death rules. Do not cure/clear previously accumulated SoulDamage or revive
targets. This is not blanket protection against every MOD's HP-cap changes.

The effect predicate is [SKILL_TREE_SPEC §9 Q2](SKILL_TREE_SPEC.md#purification-mastery-activation).
At `DamageCubeEntity.lambda$activate$0(Level,Entity)`, intercept only the selected
`EntityHelper.addSoulDamage(Entity,float)` or `addSoulDamageForce(Entity,float)` call.
1.4.9 selects the forced call with config amount / 10 when the cube is high-dimensional
and the target has the external Soul immunity; otherwise it selects the ordinary call.
Both descriptors are `(Lnet/minecraft/world/entity/Entity;F)V`. Each unprotected target
retains exactly its original selected call. Do not cancel the whole lambda, globally
change helper/death processing, add mastery to Root's predicate, restore HP afterward,
or write external SoulProtection/SoulBypass flags to implement this defense.

Use an optional exact-version adapter. Absence/unsupported versions receive no hook;
supported-version hook mismatch must fail verification. Other attacks, commands,
ordinary save/load and non-player targets are outside this intervention. Limited
implementation does not narrow the full planned compatibility scope or unlock either
mastery's purchase gate. Evidence and remaining coverage: [joint plan §7](MASTERY_IMPLEMENTATION_PREPARATION.md#mastery-first-unit).

<a id="trial-invader-149"></a>

### Invader Monolith 1.4.9 — LOCKED addition (2026-09-15)

Approved artifact SHA256 **5EFE4C068F24E611C215A0A20FE9698D5C5F7EFC1476C8E5CEAEA78B8CE1A0AD**.
Use the existing §9 Q2 personal predicate: normal Purification and Mastery each Lv1/ON,
valid server canonical, no migration pending. Missing/either OFF/invalid and non-player
targets retain native behavior. Truth is not an additional condition.

- At `InvaderMonolithEntity.lambda$tick$2(Entity)`, preserve an eligible player's existing
  Soul Protection by suppressing only this hostile `setSoulProtected(target,false)` request.
  Do not intercept all EntityHelper calls or change false to true. The goal code's separate
  `setSoulProtected(attackEntity,true)` is not a victim buff removal and remains untouched.
- At SmallBeamEntity/HugeBeamEntity `lambda$activate$0(Level,Entity)`, block the selected
  ordinary/forced Soul addition only when the beam's actual resolved owner is the registered
  Invader Monolith in that level. Unresolved, player/item and other boss owners are outside
  this addition. Shared class/type names or a guessed ancestry chain are insufficient.
- **Numeric exception:** block only HugeBeamEntity's identified `Entity.hurt(DamageSource,float)`
  call with native Float.MAX_VALUE, under that same owner/personal predicate. Preserve the
  rest of activate, target enumeration and subsequent motion. Small Beam's independent
  numeric damage (`max(1.5, target.getMaxHealth()*0.05)`) and Cube numeric damage still pass.
  Other attacks, even with the same laser damage type or extreme amounts, remain ordinary damage.
- Protect a general MobEffect only from a specifically established Invader hostile-removal
  call. The scoped Invader/goal/beam/helper inspection has identified Soul Protection clearing;
  it has not established a separate general buff-removal call. Do not claim that none exists
  anywhere, or create a hypothetical hook. Natural expiry, normal removal and owner management
  remain active. No state rollback, continuous healing/reapplication, global damage/death cancel.

Existing Cube Q1 remains unchanged; this exception does not extend Cube numeric protection.
Optional/version gating, purchase blocks/SP protection and unrelated start gates remain.
Implementation/evidence are tracked in the shared plan; a specification decision is not a test PASS.

### Offensive rule

Break Realm may bypass supported boss invulnerability/death-prevention only through a dedicated safe Adapter.

Never direct-delete the boss.

---

## 8. Hyperlink / Fumetsu Wither

Status: major high-difficulty integration-test target.

- Direct-kill/Novelcalibur-like forced death:
  - Root protects while active
  - Purification Mastery protects persistently
- normal numeric damage still follows normal rules
- Break Realm may bypass supported defensive invulnerability/death prevention while preserving legitimate death sequence

No promise to protect against a mod intentionally crashing the client, corrupting world data, or otherwise operating outside normal entity-combat rules.

---

## 9. Farmer's Delight

Status: explicit food-production compatibility target.

### Food Production Mastery

Support standard FD cooking/result flows such as Cooking Pot.

Addons using the same standard FD recipe system should inherit support naturally.

Do not “support every FD addon machine” by invasive heuristics.

Independent machines such as an addon-specific kettle/keg require a dedicated Adapter and are outside the frozen scope unless already standard-compatible.

### Machine ownership rule

For shared machine/BlockEntity output:
- player manual extraction and skill owned → doubled result transaction
- hopper/fully automated extraction → normal result
- do not store player-specific doubled count in shared machine inventory

---

## 10. Traveler's Backpack

Status: explicit Priority 0 auto-feeding compatibility target.

Test:
- auto-feed ordinary food
- full hunger
- Nutrition >=18
- cumulative Root Nutrition
- True Root reservation
- Food Healing HP exactly once
- Shokugi exactly once
- Food Diversity exactly once
- Satisfaction inventory consistency
- container-return foods

---

## 11. Sophisticated Backpacks

Status: explicit Priority 0 auto-feeding compatibility target.

Same invariants as Traveler's Backpack.

Auto-feeding + True Root is intentionally powerful and is not to be nerfed merely for balance.

---

## 12. Grave / Tombstone mods

Status: regression-critical generic compatibility.

v2.2.5 already contains a fix for player-death drop multiplication.

### Absolute invariant

**Player death drops must never be multiplied by Food Healing's slaughter/gathering/drop multipliers.**

Test:
- normal death
- mob kill
- PvP
- suicide/self damage
- grave creation
- grave retrieval

Player inventory count before death must equal recovered inventory count, aside from the grave mod's own intended rules.

---

## 13. Other HUD/combat mods

Numeric health HUD must not depend on another mod allowing the vanilla `PLAYER_HEALTH` pre-overlay event.

Test with at least one realistic HUD/combat-mod environment if available.

---

## 14. SuperbWarfare legacy

### OPEN-04 retention and formula - RESOLVED / LOCKED (2026-09-13 user decision)

Retain the Shokugi-level-based dedicated multiplier **`1 + ShokugiLevel * 1.0`**.
Dedicated multiplier expectations: Lv0 → x1, Lv1 → x2, Lv200 → x201. These are not
fixed final damage values after all other multipliers/defenses. Do not replace this
with a purchased SP stat, a new skill, additional SP cost or a TaCZ formula. Do not
invent a cap, decay or nerf because the multiplier is large. Keep long progression
and the existing numeric-safety policy; no int narrowing or silent overflow.

Route/owner/composition must match verified v2.2.5 behavior and the canonical damage
rules. Do not widen coverage to unverified explosions, mounted weapons or other mods.
Exact supported artifact/version and real route validation remain distinct from the
locked formula. Missing evidence blocks that integration only, never reopens retention.
No placeholder external class/event/Mixin target, arbitrary download or hard dependency.

2026-09-13 read-only comparison of the existing v2.2.5 Jar establishes:
- `LivingHurtEvent`, source entity must be a Player; use that player's Shokugi capability.
- Skip the handler during the existing pursuit recursion guard.
- For level > 0 and a non-null direct entity, old code matches the direct entity's
  full class name containing `superbwarfare` and multiplies the event amount once.
- Old local order is optional TaCZ-specific factor, SuperbWarfare-specific factor,
  global level-derived factor, then Heroics. This does not resolve ordering against
  other mods' event handlers or identify every actual supported projectile type.

### Audited v3 gunfire connection (2026-09-13)

Approved artifact: `superbwarfare-0.8.9.1-hotfix-mc1.20.1-993063bed-all.jar`,
42,162,514 bytes, SHA-256 `3AAF4C239BC0FB31F9217927A44D74071D904D1DD03C5308CA3395ECD67D86DC`.
The loader reports **0.8.9.1**; Implementation-Version is **0.8.9.1-mc1.20.1-993063bed**.
The filename's `hotfix` is not the loader version. Kotlin for Forge >=4.11.0 is required.

`SuperbWarfareCompat` connects fresh and successfully migrated v3 data only for the
audited `superbwarfare:projectile` / exact `ProjectileEntity` class and damage types
`gunfire`, `gunfire_headshot`, `gunfire_absolute`, `gunfire_headshot_absolute` in the
`superbwarfare` namespace. Activation checks loader version 0.8.9.1 and has no direct
optional-class linkage. The version check is not a hash check or a claim about other
artifacts reporting the same version; only the approved hash has been tested.

The real `ProjectileEntity.performDamage` constructs these sources with itself as
directEntity and `getOwner()` as source entity. Food Healing requires the source entity
to be a Player and reads that player's canonical `getLevel()`; it does not traverse
vehicles/owners or infer an attacker from a weapon name. The new local order is
dedicated factor → existing global outgoing factor → Heroics. Global remains the
v3 SP stat, not a restored legacy global level bonus. TaCZ factors/order are unchanged.
Armor-piercing split damage emits two distinct events; each receives the factor once.
The existing pursuit recursion guard and positive finite damage handling remain intact.

This is a bounded subset of the old class-name-contains predicate, not proof that the
old predicate and a namespace-only check are equivalent. Explosions, separate projectile
classes, mounted-weapon-specific routes, laser/melee and all-weapon coverage remain
outside the new v3 connection. A vehicle path that emits the exact same audited source
would match it; no vehicle gameplay coverage is claimed. Unknown owner/non-Player/null
source and unrelated attacks gain no dedicated multiplier.

The pre-existing migration-pending fallback is unchanged: guarded legacy level,
registry namespace, global → legacy dedicated → Heroics. The v3 branch is mutually
exclusive with it. Unknown/invalid legacy raw data still yields no legacy bonus; edited
pending scaffolds are not silently migrated or switched to the unvalidated current level.
No schema, SP, saved-data or external artifact changes are made.

See [current integration evidence](CODEX_STATUS.md#evidence-superbwarfare-20260913)
for actual selected dependencies, attack tests, negative synthetic tests and limitations.
The earlier artifact-missing state is preserved in the [previous phase](CODEX_STATUS.md#evidence-open-decisions-20260913).

History: retention/SP replacement was OPEN / LEGACY_PENDING_DECISION before this
user decision. The old choice is resolved; wider attack-route coverage remains unverified.

---

## 15. Compatibility claim standard

A successful compile is not proof of runtime compatibility.

For each named mod, record:
- version tested
- startup result
- dedicated server result if applicable
- exact feature scenario
- result
- logs if failed

---

## 16. Pam's HarvestCraft 2 - Trees — v3.0.0 release required

Updated: **2026-09-29 09:40 JST**. **SPEC LOCKED / IMPLEMENTED / LIMITED TESTED / #5 COMPLETE**.

Full contract: [SPEC§14.1](SPEC.md#141-pams-harvestcraft-2---trees-harvest-duplication).
Approved Trees target: pamhc2trees1.0.2, original SHA-256
69E0C722C786B78AEA299E7FAF27991E6533A671A66F281FB5CED279EB31B0DD.

- Pam direct-harvest canonical remains50, including apple. Runtime production recipes are **49 Pam
  mod-loaded-conditioned + unconditional vanilla apple/cocoa2 =51**. Pam absent has2 active; present51.
- Old Pam-conditioned apple ID is removed. Paper/string remain conditional. Ordinary apple/cocoa require
  no provenance/other MOD/skill; cocoa is not inserted into Pam's audit whitelist.
- Full item minecraft:logs boundary is unchanged, including Nether and tagged other-mod items.
  FPM remains food-only: apple2/2/4, cocoa2/2/2. No custom serializer or direct external-class reference.
- Other Trees versions remain UNTESTED / UNSUPPORTED CLAIM. Condition checks presence only.
- [Final-Jar evidence](MASTERY_IMPLEMENTATION_PREPARATION.md#pam-vanilla-51-recipes-result): present150, absent146 PASS, core67×2. Native transaction, reload,
 2x2 and ServerRecipeBook checks do not establish real-client/JEI UI or authenticated REAL2CLIENT PASS.
- Crops1.0.3 declares only MC/Forge dependencies; **no Food Core dependency/version range is declared**.
  Two unguarded recipe resources require pamhc2foodcore:cookingoilitem regardless.
- Existing local Food Core file pamhc2foodcore-1.20.4-1.0.5.jar identifies MOD ID pamhc2foodcore,
  Manifest1.0.5, loader/Forge [40,), Minecraft [1.20,), no additional mandatory MOD. Its native registration
  supplies cookingoilitem; those declared requirements fit the tested MC1.20.1/Forge47.4.0 environment.
- **Food Core1.0.5 — SUPPORTED FOR FOOD HEALING PAM INTEGRATION WITH KNOWN UPSTREAM RECIPE DEFECTS**.
  Exact file pamhc2foodcore-1.20.4-1.0.5.jar, 911,489 bytes, SHA-256
  **1F18655D5EEEA99EECCF0D9D458DBF88D6B59C1A9F00C17E322DB8D55A54ED5B** only.
  No additional mandatory MOD or nested Jar. Version1.0.0 is not substituted or adopted by this decision.
- The existing supplement supplied cookingoilitem and loaded both required Crops recipes. Its complete
  log has exactly3 ERROR events, all native Food Core recipes below; Food Healing errors0/unexpected errors0.
  The file name alone is not compatibility evidence. This is existing MC1.20.1/Forge47.4.0 runtime evidence.

| Native recipe ID | Missing result item | Current classification |
|---|---|---|
| pamhc2foodcore:melonpieitem | pamhc2foodcore:melonpieitem | KNOWN UPSTREAM ISSUE / NON-BLOCKING FOR FOOD HEALING QUEUE #5 |
| pamhc2foodcore:honeymuffinitem | pamhc2foodcore:honeymuffinitem | same exact-artifact allowance |
| pamhc2foodcore:caramelcupcakeitem_x4 | pamhc2foodcore:caramelcupcakeitem | same exact-artifact allowance |

**All three recipes are UNAVAILABLE / BROKEN IN THIS UPSTREAM ARTIFACT**, not repaired or usable by this decision.
The user explicitly replaced unconditional supplement ERROR0 with Food Healing ERROR0 + required Pam paths
passing + unexpected errors0 + exactly these3 accepted-known events. Match artifact hash AND namespace AND
recipe/result pair; a different hash or count (including2/4), another missing item, or any unexpected ERROR
requires STOP/re-audit. No pamhc2foodcore:* wildcard or generic FoodCore-text exclusion.
Missing Food Healing51/conditional49/apple/cocoa, required Crops/cookingoil failure, crash, save failure,
duplicate delivery or product regression remain blockers. No Jar/recipe edits, datapack override, fake item,
registry injection or bundled repair. A future separate patch requires a separate explicit request.

- Crops/Food Core remain test-only. Do not add them to product metadata/classpath/runtime dependencies,
  bundle them, or expand Food Core's entire functionality into the product compatibility claim.

Historical §14.59's all50-conditional/absent0 results and Crops STOP remain preserved. The former
cookingoil absence is resolved. §14.60/14.61 retain their old ERROR0-policy FAIL/PENDING and original runner exit1.
[Current reclassification](MASTERY_IMPLEMENTATION_PREPARATION.md#pam-foodcore-known-upstream-acceptance)
uses the unchanged logs/7cases75assertions/save-stop-exit0 and needs no new run or artifact. The three defects
did not prevent server startup, tested Food Healing/Pam operations or normal save/stop in this environment.
This does not guarantee all Food Core food/recipes/cooking/growth/nutrition/textures/JEI/client UI or long-term play.

---

## 17. Break Realm Adapter expansion

Status: **SPECIFIED / NOT IMPLEMENTED / POST-VANILLA-RELEASE-BLOCKER**

### Core isolation rule

Break Realm compatibility must use a Food Healing-owned Adapter interface/registry whose core signatures do
not reference optional-mod classes. An Adapter is registered or activated only after a guarded mod/version
check. With every target mod absent, Food Healing RPG must classload, start a client and dedicated server,
and run ordinary combat without touching Adapter implementation classes.

Public APIs are preferred. Registry IDs, capabilities, NBT/data, events, reflection, or conditional Mixins
may be used only after the exact version is audited and only inside a dedicated compat package. Never infer
an ID, class, trait, capability, or MCreator Procedure from a display name or another version.

### Ownership and restoration rule

Each Adapter must identify the external owner of the defensive behavior and bypass only that confirmed
mechanism for one legitimate Break Realm hit. Any temporary state change is scoped to the transaction and
restored in guaranteed cleanup. Do not globally disable invulnerability, permanently remove traits, alter
Player targets, or disturb boss AI, identity, phase controllers, loot, XP, drops, advancements, quests, or
normal death completion.

The Adapter must audit where survival is actually owned, including `isInvulnerableTo`, `hurt` returning
false, Forge damage/death cancellation, health clamps, forced healing, revival/respawn, tick HP restoration,
custom capability/data, and scripted phase invulnerability. It must not bypass a different mechanism merely
because it looks similar.

### Named target matrix

- **L2 Hostility:** bypass a supported immortal/lethal-prevention trait or affix only for an enabled Break
  Realm attack. Do not remove the trait. Permanent nullification belongs to Truth Mastery.
- **Trial Monolith:** audit numeric damage, damage cancellation, forced-death layers, revival, phase state,
  and normal boss completion before designing its dedicated Adapter.
- **Hyperlink / Fumetsu Wither:** audit the exact defensive and death-prevention owners separately from its
  offensive forced-death behavior; preserve normal Wither death flow.
- **Draconic Evolution / Chaos Guardian:** audit the selected version's public API, shield/invulnerability,
  phase controller, death sequence, loot, and progression. Do not assume ordinary Forge events own all gates.
- **Bloodbath / Bloodbath Godzilla:** assume neither ordinary Forge events nor stable hand-written classes.
  Read the selected Jar as reference-only and identify the Godzilla entity class, tick/hurt/death Procedures,
  healing, invulnerability reset, death cancellation, revival/respawn, and capability/NBT dependencies before
  designing a `BloodbathBreakRealmAdapter`-equivalent.
- **Official-modpack urban-legend/high-difficulty bosses:** add one version-audited Adapter per confirmed
  ownership model. There is no broad heuristic or universal forced-kill Adapter.

Bloodbath and every other reference Jar remains external reference material. Do not copy, bundle, or
redistribute its code/assets/Jar with Food Healing RPG.

### Damage and failure safety

The Adapter transaction retains source, attacker, target, original amount, and legitimate final amount; is
recursion-guarded; and permits at most one non-amplifying fallback. A hit that already succeeded receives no
fallback. Pursuit is a distinct hit, and vanilla hurt frames are not treated as boss invulnerability.

Unsupported or unsafe cases fall back to ordinary damage, never `Float.MAX_VALUE`, direct HP zeroing,
`discard()`, `remove()`, a per-tick kill loop, or another synthetic kill. Optional rate-limited diagnostics
may record mod ID, entity ID, source, result, HP before/after, and observable cancellation state without
changing the outcome.

### Compatibility claims

For each exact target version, record independently:

1. `STATIC AUDITED`
2. `AUTOMATED INTEGRATION TESTED`
3. `MANUAL INTEGRATION TESTED`

A Jar/source review grants only `STATIC AUDITED`. It never grants either integration status.


<a id="flight-exact-providers"></a>
## 18. Flight exact providers — 2026-09-29 19:54 JST

正式契約は[SPEC§18](SPEC.md#flight-exact-provider-contract)。**SUPPORTED FLIGHT OWNERSHIP: Vanilla Creative/Spectator + Food Healing + Fantasy Ending 2.7.20 / EndingLibrary 2.1.19fix + Mekanism 10.4.16 / Avaritia4.0.3承認原物（下表の経路限定）**。既存FE/ELの[監査](../build/verification/flight-exact-20260929-125743/audit/final-jar-audit.json)と、Mekanism追加後の[最終Jar監査](../build/verification/flight-mekanism-20260929-184800/audit/final-jar-audit.json)を分けて保存。Avaritia追加後は[今回の最終監査](../build/verification/flight-avaritia-20260929-193800/audit/final-jar-audit.json)。

| provider / exact版 | 実predicate・grant | revoke / 今回の判定 |
|---|---|---|
| vanilla / MC1.20.1 | Survival/AdventureではFH自己付与、Creative/Spectatorはnative GameMode authority | 正常mode往復・本人だけのFH-only解除をGameTestで確認 |
| `fantasy_ending` 2.7.20 + `ending_library` 2.1.19fix + `curios` **5.14.1+1.20.1** | 実登録`fantasy_ending:the_domain_of_fade`、Curios機能slot内（native back）、`CuriosEventHandler.tick→curioTick`。cosmetic/単なるinventoryは除外 | **正式最低SUPPORTED route**。native `onUnequip`の非Creative/Spectator mayfly/flying解除、FE-before/after、provider-first/FH-first OFFを実証。native毎tickpacketとFH変更時packetは区別 |
| 同FE/EL/Curios構成の胸装備 | 実登録`fantasy_ending:fantasy_ending_chestplate`。native Inventory armor tickの実item/typeOfSlot CHEST分岐。armor containerを照会 | **FH OFFによる権限保全は4項目確認。FE自身のnative解除処理なし**。外した後mayflyが残る外部挙動を実測。これを正常解除PASSへ書き換えず、全Armor lifecycle対応・leak修復をclaimしない。正式grant/revoke最低routeは上のCurio |
| `ending_library` 2.1.19fix | attach済みPlayer capabilityの`getAbilityMayfly/getAbilityFlying`、-1/0/+1をreadonly。実`endinglib abilities <player> ...`から設定しnative modifyAbilitiesを通した | positive保持、両fieldのDENY・既付与後DENY・解除後再評価を個別PASS。capability/override書換え0。全MOD provider集合として使わない |
| `mekanism` **10.4.16**・下記SHA一致 | 実item `mekanism:mekasuit_bodyarmor`をCHESTに装備、実module `mekanism:gravitational_modulating_unit`。native `CommonPlayerTickHandler.isGravitationalModulationReady(Player)Z`＝playing mode・module存在/ON・native必要energy。active flyingは要求しない | **SUPPORTED exact artifact / AUTOMATED INTEGRATION TESTED**。actual PlayerTick END→PlayerStateのgrant/revoke、前後取得・両OFF順・正式module disable/chest removal・energy境界を最終配布Jarで確認。[§14.66](MASTERY_IMPLEMENTATION_PREPARATION.md#flight-mekanism-exact-provider-completed) |
| `avaritia` **4.0.3**・下記SHA一致 | CHEST `avaritia:infinity_chestplate`、通常装備のnative `LivingTick→update→updateFly`。胸単独・readonly registry predicate、外部class linkage0 | **SUPPORTED exact artifact / AUTOMATED INTEGRATION TESTED**。実native grant/revoke・両取得順/OFF順・100評価反復0・mode/lifecycle代表23件、EL DENY混在2件。[§14.67](MASTERY_IMPLEMENTATION_PREPARATION.md#flight-avaritia-exact-provider-completed) |

原物：

- Avaritia：`<LOCAL_DOWNLOADS>/Avaritia-1.20.1-4.0.3-universal.jar`、**579,372 bytes / 537 entries / SHA-256 48F23CEA99D1D2E6CED4215B9FE3F9F45641B2CD5650C8725A3EE75B500483BB**。mods.toml `avaritia`/`4.0.3`、javafml/Forge `[47,)`、Minecraft原文`[1.20.1,]`。追加必須MOD/同梱Jar0。実Forge47.4.0ロード確認。原物不変。[照合](../build/verification/flight-avaritia-20260929-193800/audit/inputs.json)。
- Mekanism：`<LOCAL_DOWNLOADS>/Mekanism-1.20.1-10.4.16.80.jar`、**12,558,456 bytes / 11,172 entries / SHA-256 3B0D191FA4A45E662F9725991D76A30192F30186ABB2BA694B873B5F7B9358D2**。MOD ID `mekanism`、mods.toml/Manifest/実ロード版 **10.4.16**（filenameの`.80`をloader版へ付けない）、javafml `[47,)`、必須Minecraft `[1.20.1]` / Forge `[47.1.1,)`。同梱Jar0、追加必須MOD0。Curios/CraftTweaker/JEI/JsonThings/WTHITは任意、Generators/Tools/Additions不要。実行Forge47.4.0。[原物・metadata照合](../build/verification/flight-mekanism-20260929-184800/audit/inputs.json)。
- FE：18,417,128 bytes / SHA-256 `E32FD4BA2E48FCF2C84F71AA07155C63D6BFA2178BB840899307DA0DA5E5D141`。
- EL：2,409,255 bytes / SHA-256 `0E29AF51DABD4E0EA8A315C1886E61F3ABEE62045F196EB6913D7F4DBA6CDC34`。
- Curiosと既存GeckoLib/Iron's Spellbooks/Iron's Lib/nested playeranimator等の実ロード版・hashは[最終入力](../build/verification/flight-exact-20260929-125743/integration-final/inputs.json) / [実ロード結果](../build/verification/flight-exact-20260929-125743/integration-final/flight-result.json)。新規取得・原Jar編集・同梱依存の重複配置なし。

coreは外部classをsignatureに持たず、ModListのexact metadata照合後だけ個別Adapterへ入る。FE側は既存実証のCurios版も限定し、Mekanismは公開APIへlinkする前にloader版と実mod file SHA-256の両方を照合する。同版別buildもinactive/UNSUPPORTED。原Jar・module/energy/装備/内部mapは製品から変更せず、Mekanism必須依存化0。違う版・未知providerはinactive/UNTESTED。照会失敗時はUNKNOWNで値を保全。外部のsetter/装備変更/override解除/外部writer抑制Mixinは製品へ追加していない。

Supported lifecycleの証拠：core最終sourceでnative GameMode往復、同instance dimension、PlayerList death/End-return replacement、通常remove/save→同UUID new login、他player分離を確認。外部routeの実配布Jar確認は上表の順序/状態に限定。**実client GUI/飛行操作・別JVM再起動・全provider組合せのlifecycle網羅はNOT TESTED**。未知foreign-after、未承認Mekanism artifact/別版、未承認Avaritia artifact/別版、別FE/EL/Curios版の公式保証なし。Mekanismの限定lifecycleは同ServerPlayer dimension往復、native death-respawn replacement（keepInventory=false）、通常保存/logout→同UUID new login・装備/module/energy復元まで。restart、keepInventory全設定、実client、FE/EL/Mekanism同時導入での全相互作用はNOT TESTED。EL DENY＋positiveの既存policy unitはPASSを維持し、Mek混在実MODのPASSへ転記しない。今回のEL+Avaritia代表2件は別の実測PASS。

Avaritia追加境界（2026-09-29 19:54 JST）：exact gate一致後だけCHEST registry IDを照会し、private Info/map/外部setterを製品から参照しない。同版別hashはinactive、読取例外はUNKNOWN。LivingTickは両sideのnative実装であり、今回測定したのはdedicated server側。実ServerPlayer.doTickで自然発火するLivingTick→Player END順を記録し、native解除1回→FH自己再付与1回・steady100 write/packet0を確認。EL DENYはFH grant/regrantを止め、Avaritia自身のwriterを妨げない。

Supported lifecycleは同player dimension往復、native respawn replacement（keepInventory=false）、通常保存/logout→同UUID新playerの胸/canonical復元と現在装備再評価。Infoの複製/復元はFHが行わない。実client・別JVM restart・全keepInventory・全provider混在・異常slot/第三者subclass・Armor他能力は未保証。Avaritiaの任意IC2/Botania tagや未登録flower/potato lootのresource ERRORは保存し、全resource互換とは宣言しない。[writer一覧・実測の詳細](MASTERY_IMPLEMENTATION_PREPARATION.md#flight-avaritia-exact-provider-completed)。


<a id="fe-uniform-built-in"></a>
## 19. Fantasy Ending 2.7.20 exact client shader compatibility — 2026-09-30 07:19 JST

正式にFood Healing本体へ内蔵する。独立`fe_uniform_compat`/observer/helper MODの導入は不要かつ今回の受入構成に含めない。根拠は旧[共通計画§14.19](MASTERY_IMPLEMENTATION_PREPARATION.md#fe-shader-compat-execution-result)の意味同一を維持した、[§14.74 PRECHECK/static426/A3/B3](MASTERY_IMPLEMENTATION_PREPARATION.md#queue8-fe-shader-blocker-resolution)。gameplayの対応範囲は既存仕様のまま。

- 有効profile：**CLIENT / Minecraft1.20.1 / Forge47.4.0 / fantasy_ending2.7.20**、原Jar SHA-256 **E32FD4BA2E48FCF2C84F71AA07155C63D6BFA2178BB840899307DA0DA5E5D141** のすべて完全一致。47.2.0や同版別hashへ拡張しない。
- FE不在、metadata不足、別版/hash/MC/Forge、dedicated/serverは介入0。gate以外の一般startup互換を全版保証する意味ではない。FE class typed referenceをcommonへ持ち込まない。専用client Mixin＋@Pseudo/string target。
- EndingLibrary2.1.19fix、SHA-256 `0E29AF51DABD4E0EA8A315C1886E61F3ABEE62045F196EB6913D7F4DBA6CDC34` は実受入構成の照合値。旧shader契約に無いEL gateを追加しない。

| 完全一致receiver | 許可ResourceLocation（これらのみ） |
|---|---|
| `com.mega.uom.client.render.shader.cosmic.CosmicShaderInstance` | `fantasy_ending:cosmic`、`fantasy_ending:cosmic_2` |
| `com.mega.uom.client.render.shader.core.MShaderInstance` | `fantasy_ending:hash`、`fantasy_ending:rendertype_light_beacon_beam`、`fantasy_ending:rendertype_cil_particle` |

親ShaderInstanceのResourceLocation constructor内private parser呼出**1か所だけ**をredirectし、該当組合せだけFE自身のnative public parser/factoryへ1回委譲。他shaderは元parent private parserを1回実行。parser本体/visibility/constructor残処理/初期化順序は変更しない。call0/2・descriptor/bridge/half apply/competing redirectは契約不成立として拒否し、広域fallbackへ緩和しない。shader JSON/GLSL/resource replacement・外部原Jar編集なし。

最終配布JarによるB3は、上記＋Curios5.14.1+1.20.1 / GeckoLib4.8.2 / Iron's Spellbooks3.16.3 / Iron's Lib2.1.0・実選択nested依存で**正式title startup＋通常ingot inventory描画＋通常保存Quitを限定PASS**。内部GL22型/object identity/current programは今回未観測であり、旧§14.19＋静的意味同一の根拠を分離する。全GPU/全shader/MOD版/他resource pack/全gameplayを保証しない。B3の範囲外asset等ERROR/WARNはログを保持し、無害との全般宣言をしない。
