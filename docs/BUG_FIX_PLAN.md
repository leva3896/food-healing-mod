# BUG_FIX_PLAN.md — Food Healing RPG v3.0.0

## Priority 0 — release blockers

### P0-01 Numeric HP HUD can disappear / hearts return

Current issue:
- Food Healing both cancels `PLAYER_HEALTH` and draws its text in a pre-overlay event.
- Other mods can interfere first.

Fix:
- separate vanilla-heart suppression and Food Healing numeric rendering
- render own HUD independently
- add absorption display
- regression test combat/HUD mod coexistence

---

### P0-02 End/dimension/respawn progression reset

Historical bug class.

Must preserve/rebuild:
- Shokugi
- SP
- skills
- base-stat allocations
- Food Diversity
- toggles
- all Food Healing-owned modifiers

Use canonical saved progression, idempotent modifier reconstruction.

Any loss or duplicate modifier = RELEASE BLOCKER.

---

### P0-03 Double healing / missed healing

Current v2.2.5 heuristic:
- food Finish event heals
- HungerChangeHandler heals positive Food Level delta
- ignore changes near an eating timestamp

This can:
- discard a legitimate non-food hunger increase near a meal
- double-heal if a mod applies delayed Food Level changes

Fix:
- transaction/source-aware exactly-once pipeline
- food transaction uses declared Nutrition
- non-food path uses actual positive Food Level delta
- deduplicate same underlying food action

---

### P0-04 Game-state mutation on client side

Audit current FoodHealingHandler.

All:
- heal
- skill/progression update
- effect state
- damage state

must be server authoritative.

---

### P0-05 Global MAX_HEALTH Reflection

Remove v2.2.5 private-field/global Attribute max mutation.

Use Food Healing-owned modifier only.

AttributeFix optional compatibility.

---

### P0-06 Flight revokes another mod's flight

Current tick logic can set `mayfly=false` when Food Healing flight is not active.

Fix:
- never revoke a flight permission Food Healing does not own
- transition/ownership-aware grant
- restore only state Food Healing itself changed, if safe

---

### P0-07 Permanent-effect ownership collision

Current effect cleanup can infer “our effect” from amplifier/duration.

This may remove another mod's equivalent effect.

Fix:
- track ownership/source explicitly where possible
- avoid removing arbitrary positive effects
- Kongo/Fire Resistance/etc. must not delete other mods' buffs

---

### P0-08 Optional TaCZ classloading / Mixin risk

Current Mixin config/implementation directly references TaCZ behavior.

Fix:
- isolate TaCZ
- conditional loading
- Food Healing starts with TaCZ absent
- remove legacy ammo coupling

---

### P0-09 Player-death/grave duplication regression

Current v2.2.5 fixed this by excluding Player from LivingDrops multiplication.

Preserve invariant:
- Player entity is never a target of Food Healing drop multiplication.

Test with grave/tombstone style mods and PvP/self-death.

---

### P0-10 Static player maps / stale state

Audit `HungerChangeHandler` and any static per-player Maps.

Ensure cleanup:
- logout
- clone
- dimension
- server stop as appropriate

Prefer lifecycle-owned per-player state over unsafe global static maps.

---

## Priority 1 — major correctness

### P1-01 Satisfaction consume→refund

Current design can collide with:
- bowls
- custom result stack
- NBT
- auto-feeding backpack inventory

Rewrite as one server-authoritative non-consumption decision per use.

---

### P1-02 Food Production post-craft item copy

Current `ItemCraftedEvent` extra-item delivery causes result-slot mismatch.

Rewrite result/transaction flow.

---

### P1-03 Gathering re-runs LootTable

Current behavior can disagree with another mod's final loot/context.

Prefer Forge Global Loot Modifier or equivalent final-loot transformation.

Do not re-roll loot independently.

---

### P1-04 Ore detection by name/string

Remove fragile `contains("ore")`-style logic where present.

Use tags/loot context/explicit supported classifications.

---

### P1-05 Pursuit leaves invulnerability state modified

Current follow-up sets `invulnerableTime=0`.

Fix:
- only Food Healing follow-up bypasses vanilla hurt i-frame
- preserve/restore relevant target state or use a targeted damage mechanism
- do not pierce custom boss phase invulnerability by default
- prevent recursion/double multiplier

---

### P1-06 Global durability Mixin

Current ItemStack durability interception is broad.

Audit:
- it may affect weapons/tools/unrelated mod items, not only intended armor behavior

Rewrite/scoping goal:
- modify only intended durability events
- preserve other mods' custom durability behavior
- Armor Mastery and Unbreaking should not become global ItemStack corruption

---

### P1-07 Food Diversity stale modifier

Always remove/update Food Healing modifier before reapplying, including zero-bonus state.

---

### P1-08 Skill display names as save IDs

Replace localized strings with immutable resource IDs.

Add migration support for known old Japanese names if safe.

---

### P1-09 int level/count

Move Shokugi count/level/SP/upgrades to long/safer representation.

Validate arithmetic overflow.

---

### P1-10 Class-name string mod detection

Replace class-name substring checks for TaCZ/SuperbWarfare/etc. with:
- mod-presence
- dedicated compat
- public APIs/tags where possible

---

### P1-11 Legacy unconditional high-Nutrition buffs

Old Nutrition>=threshold path grants Resistance IV/Fire Resistance outside the new skill tree.

Remove that bypass in v3; Root trigger remains Nutrition-based, while Fire/Kongo are purchased skills.

---

### P1-12 Non-food hunger incorrectly triggers Root

Current HungerChangeHandler can activate old Guts on hunger gain.

v3:
- non-food hunger gain heals
- Root only uses consumed-food Nutrition rules

---

## Priority 2 — frozen redesign implementation

- GUI/keybind
- skill tree
- infinite base stats
- Root/True Root
- Heroics/True Heroics
- Break Realm
- Purification Mastery
- Truth Mastery
- TaCZ independent tree
- food-production integration

---

## Priority 3 — polish

- clear HUD labels
- lock tooltips
- “TaCZ導入時のみ利用可能”
- Root cumulative display such as `16/18`, remaining window
- large-number formatting
- user-facing migration/help text
