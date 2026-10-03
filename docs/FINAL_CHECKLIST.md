# Food Healing RPG v3.0.0 — Formal Release Checklist

本文更新: 2026-10-03 11:22 JST。**#1–#8 COMPLETE / #9 PARTIAL / RC=YES / LOCAL RELEASE ARTIFACT AND SPECIFICATION COMPLETE / GITHUB UPDATE BLOCKED**。

- [x] source274/build設定は#8から不変、正式Jar byte一致
- [x] 26skill/7stat/6command、finite2590、Nutrition2000/schema5/protocol7整合
- [x] metadata/6Mixin/refmap/reobf/禁止混入0
- [x] 完全仕様書・maintenance rule・README・current docs整合
- [ ] authorized GitHub release branch/main反映・remote readback
- [x] 未検証/別開始gate・履歴を保持。全MOD互換を宣言しない

詳細は[完全仕様書](FOOD_HEALING_RPG_V3_SPECIFICATION.md)と[Release Receipt](../release/v3.0.0/RELEASE_RECEIPT.md)。下記は実装前の旧チェック表であり、現在の費用/完了判定に使わない。

<details><summary>Historical pre-Codex checklist（旧値を保全）</summary>

# FINAL_CHECKLIST.md — Pre-Codex Review

## Frozen identity
- [x] Food Healing RPG
- [x] v3.0.0
- [x] MOD ID `foodhealing`
- [x] Minecraft 1.20.1 Forge
- [x] New gameplay feature additions paused

## Progression
- [x] 200 Shokugi count = 1 level = 1 SP
- [x] Skill tree replaces auto-unlock
- [x] finite skills eventually obtainable
- [x] repeatable infinite base stats
- [x] long/safer progression storage

## Easy-to-forget items
- [x] Global Base Outgoing +0.1x/SP
- [x] **TaCZ Base Outgoing +0.01x/SP**
- [x] Ammo Conservation completely separate
- [x] Player death drops excluded from multipliers
- [x] End/dimension reset regression
- [x] AttributeFix optional compatibility
- [x] max HP config theoretical 1e12
- [x] full-hunger eating preserved
- [x] non-food Food Level gain healing preserved
- [x] two-tick hunger heuristic removed
- [x] numeric HUD/absorption
- [x] flight does not revoke other-mod flight

## Root
- [x] Nutrition18 single
- [x] cumulative18 within15s
- [x] non-food hunger does not trigger
- [x] Lv1–5 durations 2/4/6/10/15s
- [x] cooldowns 20/15/10/5/0s
- [x] forced death protection while active
- [x] True Root 20SP, one reserved next activation
- [x] backpack auto-feeding synergy intentionally retained

## Heroics
- [x] <=40% normal
- [x] levels: outgoing ×2/2.5/3/4/5
- [x] armor/toughness ×2/4/8/16/32
- [x] Heroics DR 10/20/30/40/50%
- [x] True Heroics 100SP
- [x] <=80%, ×20, armor/toughness×64, DR99%

## High difficulty
- [x] Break Realm 5SP
- [x] Purification Mastery 100SP
- [x] Truth Mastery 500SP
- [x] Truth Mastery ±75 each axis
- [x] no every-tick full-volume scan

## TaCZ
- [x] Ammo Conservation 10 levels
- [x] 5SP each / total50
- [x] 10–100% ammo save
- [x] x1.05–x1.50 damage
- [x] overheat disabled from Lv1
- [x] exact unavailable message

## Food production
- [x] normal crafting result×2
- [x] Farmer's Delight standard cooking
- [x] furnace/smoker/campfire
- [x] manual player extraction×2; automation×1
- [x] no post-result copy hack

## Priority compatibility
- [x] AttributeFix
- [x] TaCZ
- [x] L2 Hostility
- [x] Auto Leveling
- [x] Traveler's Backpack
- [x] Sophisticated Backpacks
- [x] Farmer's Delight
- [x] Trial Monolith
- [x] Hyperlink/Fumetsu Wither
- [x] grave/tombstone regression

## OPEN before final release
- [ ] v2.2.5 existing-world → SP migration formula
- [ ] exact prerequisite list for Purification Mastery
- [ ] exact additional prerequisite list for Truth Mastery
- [ ] SuperbWarfare legacy multiplier disposition
- [ ] verify legacy sub-level mapping for Gathering/Unbreaking/Pursuit

</details>
