# TEST_PLAN.md — Food Healing RPG v3.0.0

関連節の最終更新: 2026-10-03 13:00 JST。**#1–#9 COMPLETE / RC=YES / FOOD HEALING RPG v3.0.0 FORMAL RELEASE COMPLETE**。Git CLI publication完了は[§40](#queue9-git-publication-readback)。今回の静的受入は[§39](#queue9-release-static-acceptance)、ゲーム受入は既存§38ほかの対象Jar/範囲を保持。過去の試験設計・STOP・NOT RUNを現在の再実行指示にしない。正式契約は[完全仕様書](FOOD_HEALING_RPG_V3_SPECIFICATION.md)。

## 1. Baseline

- clean checkout builds before refactor
- record Java/Gradle/Forge environment
- record baseline warnings
- create backup of a representative v2.2.5 world before migration tests

---

## 2. Persistence / lifecycle — RELEASE BLOCKER

For each scenario verify:
- Shokugi level/count
- unspent/used SP
- skills/levels
- toggles
- Food Diversity history/HP
- every base-stat upgrade
- TaCZ-related progression
- no duplicate Food Healing modifiers

Scenarios:
1. Overworld → Nether → Overworld
2. Overworld → End → central exit portal → Overworld
3. End gateway
4. normal death/respawn
5. `/kill`
6. logout/login
7. client restart/world reload
8. dedicated server restart
9. supported modded dimension round-trip
10. very large HP/SP values

2026-09-06 automated coverage: `endGatewayAndPearlPreservePlayerStateWithoutRespawn` exercises
vanilla End gateway `teleportTick` with configured exact exits, direct-player and Ender Pearl-owner
transfers, and a duplicate call during cooldown. It checks the same ServerPlayer/capability, complete
Shokugi NBT (long progression, all base-stat counters, levels/toggles/Root state), Food Diversity,
owned/foreign modifiers, partial HP63/100, NBT inventory, and one position packet/no credits per leg.
This is a server-side GameTest PASS, not a manual rendering result or remote-island generation test.
No additional human persistence test is required for this covered path.

Any progression loss = RELEASE BLOCKER.

Real v2.2.5 capability verification (explicit opt-in, test-only):
- Place only an unchanged archive copy/extraction inside a fresh `build/verification/real-v225-migration-<timestamp>`.
- `foodHealingRealLegacyAudit -PfoodHealingRealLegacyRun=<yyyyMMdd-HHmmss>` compares level.dat Player and
  playerdata, records provenance/LoadingModList/hashes, and preserves both actual Food Healing payloads.
- Memory round-trips check the 2026-09-08 approved respec plus §30 count conversion: level unchanged, canonical count proportional, unspent=legacy level,
  spent=0, empty skills/base stats/toggles, no inferred Root state, cleared pending, full raw backup,
  unchanged Diversity and defensive copies. Actual extracted Lv2/count35 yields canonical350/raw35/SP2, All28/Current3/bonus10.
- `runServer` with the same run ID, explicit EULA approval and `foodHealingRestartPhase=write` then `read`
  imports the actual payload into a vanilla-only PlayerDataStorage fixture; the read phase requires a different
  JVM and never reseeds player data. Both phases must save and stop normally with fresh success receipts.
- This is **REAL LEGACY CAPABILITY FIXTURE**, not a full modpack world boot, authenticated client test,
  or external-mod integration PASS. OPEN-01/05 were explicitly resolved by the user on 2026-09-08;
  old raw-only/SP0 results remain historical, not proof of the new refund implementation.
- Full world boot needs a separate exact source-modset/version audit and explicit user approval. Do not
  open the original world or silently remove its unavailable mods. Keep original/copy hashes and protected
  world file hashes/mtimes unchanged; no actual user payload is packaged in the mod Jar.

Approved migration regressions (unit + test-only GameTest + isolated separate JVM):
- Lv0/1/2/100/1000 and values above int through Long.MAX_VALUE; exact 1:1 refund without addition overflow.
- Each legacy Gathering/Unbreaking/Pursuit sublevel and combinations: no canonical ownership,
  full nested/array/raw preservation; effective legacy skill fallback must end after valid respec.
- Repeated raw deserialize, canonical save/reload, post-purchase reload and separate-JVM load:
  no additive refund, no backup mutation, fresh schema5 unchanged; normal schema4 count migrates once under §30.
- Negative/missing/wrong-type/floating/unsupported legacy inputs: no guessed SP/legacy effects and purchase blocked.
- Existing pending scaffold: finish only the unchanged valid raw case; do not overwrite edited v3 state.
- Player NBT load/clone/respawn and Shokugi sync payload preserve refund and separate Diversity.
- Separate ordinary-purchase fixture uses current multi-stage prices (Gathering5/5/5, Unbreaking10/20/30, Pursuit100×9) and rejects replay. Historical2/3/4 belonged to the old single-node audit, not the current acceptance.
- Test-only migration classes remain outside the distributable Jar; existing vanilla39 tests remain.

---

## 3. Core healing exactly-once matrix

### Manual food

Test:
- hunger empty/partial/full
- ordinary vanilla food
- modded high-Nutrition food
- stacked food
- final item
- container-return food
- NBT/component food

Expected:
- declared Nutrition heals exactly once
- full hunger still allows eating/healing
- one declared-Nutrition Shokugi count transaction
- one Food Diversity transaction

### Non-food Food Level gain

Examples:
- command
- another mod ability/effect
- same tick as food
- 1 tick after food
- delayed after food

Expected:
- positive Food Level delta heals exactly once
- no false suppression because a meal happened “within 2 ticks”
- no Root activation from non-food gain

### Saturation-only change

No Food Level increase and no actual food consumption:
- should not create Food Healing HP from a generic hunger-delta path

---

## 4. Root

### trigger

- Nutrition17 single → no activation
- Nutrition18 single → activation
- Nutrition30/40 → activation
- cumulative 8+8+5 within15s → activation
- threshold at exactly15s boundary
- expired window → reset
- fixed window begins with the first eligible food; later foods do not extend the deadline
- Nutrition 6 at ticks100/390/680 must not activate; tick680 starts a new window ending980
- expiration signals one canonical sync update, not a repeated update every tick
- excess not carried
- full hunger food still triggers by declared Nutrition
- command hunger +18 → no Root

### levels

Verify exact:
- Lv1 2s / cooldown20s
- Lv2 4s / 15s
- Lv3 6s / 10s
- Lv4 10s / 5s
- Lv5 15s / 0s

Cooldown starts after invulnerability ends.

### damage

While active:
- ordinary lethal hit
- rapid multihit
- armor bypass
- invulnerability bypass
- direct HP0 test harness
- forced death test harness
- death-event bypass compatibility test where available

After active window:
- protection is gone unless another skill applies

### True Root

- reserve 0→18 while active
- cap at one next activation
- immediate reactivation at end while True Root is enabled
- reservation used for that activation resets
- cannot preload multiple activations
- auto-feeding can create one reservation
- repeated high-Nutrition auto-feed can sustain successive windows as designed
- acquired + toggle OFF does not create a new reservation
- toggle OFF at the active-window end does not perform a True Root reactivation
- True Root OFF does not disable normal Root Lv5 activation/protection
- OFF→ON restores enabled reservation behavior; completed reservations retained past expiry
  follow the separately locked re-enable rule below

#### Completed reservation retention - LOCKED / AUTOMATED TESTED (2026-09-12)

The authoritative expectation is [SPEC.md section 17](SPEC.md#17-true-root-completed-reservation-retention),
**RETAIN / LOCKED (2026-09-12)**. Required regression coverage distinguishes:

- Complete one reservation, switch True Root OFF, and wait with normal Root ON: preserve that one
  reservation both before and after the original active deadline, with no expiry-triggered consumption.
- Food during True Root OFF creates no additional reservation; the retained one gives no OFF-state
  reactivation or protection after the original deadline. Normal Root Lv5 still works independently.
- Repeated toggle inputs neither duplicate the retained reservation nor reset/extend an active window.
- After OFF-state expiry, re-enable True Root with normal Root enabled: consume the completed
  reservation once and activate once. With that reservation consumed and no new reservation earned,
  repeated OFF-to-ON must neither activate again nor refill/reset the current active deadline.
- Toggle, tick, food, canonical sync and save handling agree on the same state within the tested scope.

The old toggle tests and manual PASS covered other boundaries; they do not establish this new
completed-reservation retention case. The specification-only phase ending at 20:44 ran no tests.
The later authorized implementation added eight `FoodHealingTrueRootGameTests` cases using actual
RootController methods, decoded Forge packets, registered commands and outbound canonical payloads.
Final vanilla and TaCZ 1.1.7-hotfix2 GameTests each passed **49/49** (the previous 41 plus eight).
The separate TaCZ 13-case suite, Ammo/heat/bolt/reload regressions and existing write/read fixtures
also passed; these are separate suites, not additional GameTest cases. Passive NBT/copy checks and
existing persistence regressions do not lock new reservation gameplay across death/restart.
See [the implementation record](CODEX_STATUS.md#evidence-true-root-implementation) for commands,
RED failures, final evidence and Jar identity. **Real-client coverage of this change remains NOT RUN.**
Post-expiry re-enable timing is now LOCKED by the user's additional answer. Do not infer policies
for partial progress, parent Root OFF or lifecycle transitions from that answer; see SPEC section 17.

---

## 5. Heroics

Threshold:
- 40.1% → inactive
- 40.0% → active
- below40 → active

Each level:
- outgoing multiplier
- effective Armor/Toughness
- separate DR

Verify levels are final states, not compounded with previous levels.

### True Heroics

- 80.1% inactive
- 80.0% active
- x20 outgoing
- x64 Armor/Toughness
- 99% Heroics DR
- does not itself block direct kill when Root/Purification Mastery absent
- acquired + toggle OFF applies none of x20 outgoing, x64 Armor/Toughness, or 99% Heroics DR

---

## 6. Damage reduction math

Normal Base DR:
- 0
- 1%
- 50%
- 99%
- 99.9%
- 99.99%
- many transcendence stages

High-Difficulty DR:
same.

Combined:
- verify multiplication, never additive 100%

Numeric:
- no NaN
- no negative remaining multiplier
- no accidental float rounding to exactly full immunity due stored “99.999...”
- clamp/underflow policy safe at extreme stage counts

---

## 7. Base outgoing damage

Global:
- 0 points x1
- 1 x1.1
- 10 x2
- 100 x11

TaCZ base:
- 0 x1
- 1 x1.01
- 100 x2

If TaCZ active verify multiplication:
global × TaCZ base × Ammo Conservation damage multiplier exactly once.

---

## 8. Satisfaction

Each level probability can be deterministic under test RNG.

Test:
- ordinary food
- final item
- stacked item
- bowl/stew/container
- modded food
- NBT/components
- full inventory
- manual use
- backpack auto-feed

Invariant:
- one roll
- no item duplication
- no container loss
- no TaCZ behavior

---

## 9. Food Production Mastery

### crafting
- 2×2
- 3×3
- normal click
- shift-click
- recipe output1→2
- output4→8
- stack overflow
- special result NBT/components
- modded standard crafting recipe

### Reversible crafting acceptance boundary - 2026-09-12 LOCKED

- Apply [SPEC.md section 16](SPEC.md#16-food-production-mastery-reversible-crafting): count growth
  attributable to Food Production Mastery's ordinary multiplier across reversible crafts is allowed.
- Evaluate each individual craft against its recipe and existing skill eligibility: one recipe's
  ingredient consumption and one intended result delivery, including correctly handled overflow.
  Do not require total-item conservation across an entire compression/decompression cycle.
- Dried-kelp or other eligible reversible cycles must not be marked FAIL merely because the
  skill increases their total output. Do not require reverse-recipe exclusions or provenance tracking.
- Unowned/OFF behavior still follows the existing baseline. Replay, desync, duplicate events,
  repeated delivery of the same result, ammo/reload duplication, and Player-death drop multiplication
  remain failures; the player-death regression in section 10 is unchanged.
- This is an acceptance-policy update only. No new reversible-cycle test was run or declared PASS
  by adding this section; existing automated and manual results retain their recorded scopes.

### Farmer's Delight
- Cooking Pot
- standard FD food recipe
- addon recipe that uses same FD recipe type
- container outputs
- manual extraction with/without skill
- multiplayer different players
- hopper extraction remains x1

### furnace/smoker/campfire
- manual extraction with skill x2
- no skill x1
- hopper x1
- XP/result bookkeeping preserved

---

## 10. Loot / grave regression

### Slaughter
- intended mob x3 legacy behavior
- Looting compatibility
- no double/re-roll anomalies

### Player death — absolute invariant
With/without a grave mod:
- mob kill
- PvP
- suicide
- fall death
- explosion
- retrieve grave

Recovered player items must not be multiplied by Food Healing.

---

## 11. Gathering

- vanilla ore/resource
- Fortune
- glowstone
- clay
- modded loot
- other Global Loot Modifier installed
- no independent loot-table re-roll
- no Player entity target

---

## 12. Durability

**現在のparity判定 — 2026-09-29 21:42 JST**：[§31](#legacy-parity-future-acceptance)の4技能正式受入を完了。ArmorMasteryの旧tool5→5期待を新LOCKの5→1へ更新し、weapon/direct hurt等を追加。既存84を保持してvanilla/TaCZ各95/95。下の旧試験結果は当時の証拠で、新LOCKの全受入とは分離する。


Unbreaking/Armor Mastery:
- armor
- tool
- weapon
- modded armor
- durability bypass/corrosion mechanics where supported
- ensure unrelated items do not become globally protected accidentally
- ensure other mod custom durability hook still runs

---

## 13. Flight

### 現在の判定 — 2026-09-29 19:54 JST

**AVARITIA FLIGHT EXACT-PROVIDER EXTENSION COMPLETE**。**#1–#7 COMPLETE／残2／#8・#9 NOT STARTED／Flight PURCHASE READY／RC=NO**。exact原物とwriter/Infoの[記録](MASTERY_IMPLEMENTATION_PREPARATION.md#flight-avaritia-exact-provider-completed)、正式routeは[COMPATIBILITY_POLICY§18](COMPATIBILITY_POLICY.md#flight-exact-providers)を参照。

| 今回の受入 | 最終結果 |
|---|---|
| 単独actual | **23/23 PASS**：正式胸/native LivingTick grant/revoke、before/after・OFF順・FH再付与・steady100、Creative/Spectator、dimension、respawn/new login、不正6種、他player。[実測](../build/verification/flight-avaritia-20260929-193800/integration-focus01/flight-result.json) |
| EL DENY混在actual | **2/2 PASS**：実commandでDENY、FH grant/regrant0、Avaritia writer抑止/外部修復0。[実測](../build/verification/flight-avaritia-20260929-193800/integration-el01/flight-result.json) |
| readonly・absence・identity | 前後931 checks・外部mutation0、100評価の能力write/packet反復0。id/版/hash拒否unitと外部2MOD物理的不在7 class linkage PASS |
| build/unit/check/core | compileJava/build/foodHealingUnitTest/check PASS、Flight **88 assertions**（旧83＋5）、**vanilla84/84・TaCZ84/84**（削除0）。coreはuserdev、actualは最終配布Jar直接 |
| 最終Jar/通常保存終了 | **350,807 bytes / 269 entries / SHA-256 C106B5B67F63821289B0BB51F77E165CB7D1E0684F6BFBE228A8DF3E9851F7BC**、schema5/protocol7、metadata/5 Mixin/refmap/reobf/非混入監査PASS。全4server save/stop/all dimensions/exit0・残存0 |

実client/TCP認証・別JVM restart・全provider同時・全Armor能力はNOT TESTED。自然tick経路のfixture駆動と人力飛行を区別。原物の任意tag/loot resource ERRORを保全。既存Mek28・FE18＋診断4は当時の結果を維持し再実行0。generic帰属NOT AVAILABLE、RC=NO、#8/#9未開始。

### §14.66の完了履歴 — 2026-09-29 19:12 JST

**MEKANISM FLIGHT EXACT-PROVIDER EXTENSION COMPLETE**。#1–#7 COMPLETE／残2／#8・#9 NOT STARTED／Flight PURCHASE READY／RC=NO。 既存§14.65のFE/EL契約・PASSを維持し、Mekanism承認原物だけを追加。仕様/identity/routeは[COMPATIBILITY_POLICY§18](COMPATIBILITY_POLICY.md#flight-exact-providers)、28件の実測と限界は[共通計画§14.66](MASTERY_IMPLEMENTATION_PREPARATION.md#flight-mekanism-exact-provider-completed)へ集約。

| 今回の受入 | 最終結果 |
|---|---|
| exact metadata/hash・readonly native query・optional不在 | 原物一致、現在ready照会、1032前後checks外部mutation0、同版別hash/別版拒否unit、Mek物理的不在5class linkage PASS |
| actual MekaSuit専用統合 | **28/28 PASS**：native grant/revoke、前後取得・OFF順、module/chest喪失、energy1000/999/0、modes、dimension、respawn、新login、canonical6境界、他player。[実結果](../build/verification/flight-mekanism-20260929-184800/integration-focus01/flight-result.json) |
| native revokeとFH再付与・同期 | native解除1回/FH再付与1回、その後steady100の反復write/能力packet0。FH/Mek/nativeの登録順と観測callerを記録 |
| compile/build/unit/check・core | **PASS**、Flight83 assertions、**vanilla84/84・TaCZ84/84**。既存84削除0。FE/EL共通policy/既存class不変、無関係suite再実行0 |
| 最終配布Jar/保存終了 | **347,673 bytes / 267 entries / SHA-256 9E14D382192CC500E963CA4ACAEC0BF4FF866E816C75730B2731BF05A51144A4**。protocol7/schema5、全Mixin/refmap/reobf/非混入監査PASS。実統合コピーhash一致。Mek1/core2 server通常保存・停止/exit0、残存0 |

実client・全混在構成・別JVM restartはNOT RUN。phase結果をそれらへ転記しない。EL DENY優先、generic帰属NOT AVAILABLE、unknown-after非保証を維持。次は#8の利用者指示待ち、今回#8/#9は実行0。

### §14.65の完了履歴 — 2026-09-29 13:35 JST

**#7 COMPLETE / PURCHASE READY（exact-provider契約）**。正本：[SPEC§18](SPEC.md#flight-exact-provider-contract)、[対応route/版](COMPATIBILITY_POLICY.md#flight-exact-providers)、[共通計画§14.65](MASTERY_IMPLEMENTATION_PREPARATION.md#flight-exact-provider-completed)。GENERIC FLIGHT PROVIDER ATTRIBUTION = NOT AVAILABLE / unknown foreign-after NOT GUARANTEED。旧Gate Aの技術的事実を解除しない。

| 最終受入群 | 今回の結果 |
|---|---|
| FH-only ON/OFF・foreign-before・invalid/pending・他player | server GameTest PASS、自己tokenだけを解除しforeign trueは修復しない |
| exact FE-before/after・provider-first/FH-first OFF | 実Curio正式経路PASS。胸装備は権限保全4観測成立・native解除なし、全Armor lifecycle supportではない |
| EL tri-state / DENY優先 | 実endinglib commandとnative modifyAbilitiesでmayfly/flyingを各別確認。禁止中FH grant/regrant0、解除後通常評価PASS |
| Creative/Spectator・mode・native lifecycle | mode往復、同instance dimension、death/End-return replacement、remove/save→同UUID new login、canonical保持PASS。別JVM再起動・外部全組合せはNOT TESTED |
| purchase/toggle/sync | 通常2SP/default ON・registered packet・replay・command・次END反映PASS。FH変化時各1packet、steady100/DENY50評価のFH packet0 |
| final build/unit/core | compileJava/build/foodHealingUnitTest/check PASS、Flight unit78 assertions。旧75を削らず**vanilla84/84・TaCZ84/84**、userdev最終source |
| final distribution actual provider | Forge47.4.0/FE2.7.20/EL2.1.19fix/Curios5.14.1、**22指定観測＝18経路PASS＋4胸装備保全/解除欠落診断**。[最終結果](../build/verification/flight-exact-20260929-125743/integration-final/flight-result.json)。中間Jarから転記0 |
| Jar/save/stop | **344,559 bytes / 265 entries / SHA-256 F53C890F77CED09F55B3DBAF0406BE25DEAF1F72D6EA1B76B218F90AA4D2216C**、protocol7/schema5、非混入/reobf PASS。試験server通常保存/stop/exit0・該当Java残存0。client/Prism/人力GUI飛行は今回NOT RUN |

失敗run0測定（Curios版照合）・8観測後停止（補助command引数）は保全し、原因と修正は共通計画へ集約。外部全ERROR/WARN解消とはしない。次は#8の承認範囲確認、今回#8/#9実行0。RC=NO・REAL2CLIENT=BLOCKED・他gate維持。

<details>
<summary>Gate A実施時の受入整理（2026-09-29 12:46、当時の未実行・STOPを保持）</summary>

#### Gate A時点のFlight整理

- obtain Food Healing flight
- disable Food Healing flight
- another mod grants flight before Food Healing
- another mod grants flight after Food Healing
- dimension transfer
- death/respawn

Food Healing must not revoke another mod's flight permission.

#### 当時の判定 — 2026-09-29 12:46 JST

**#7 BLOCKED - GENERIC FLIGHT PROVIDER ATTRIBUTION。Gate A STATIC AUDITEDのみ、製品変更・新試験0。** 全writer/native lifecycle/実artifact・候補A–E/STOP根拠は[共通計画§14.64](MASTERY_IMPLEMENTATION_PREPARATION.md#flight-gate-a-attribution-blocked)へ集約。現製品はmayfly=trueだけをserver ENDで書き、OFFでは何もしない。FH単独の解除欠落を、foreign保護PASSで代用しない。旧OFF非付与GameTestは既付与後revokeを証明しない。

以下は利用者が指定した受入条件の整理であり、今回の実行計画・新しい承認ではない。安全な帰属根拠が成立するまでfixture作成/実行へ進まない。

| 必須受入群 | 判定対象 | 今回の結果 |
|---|---|---|
| FH-only | ONでmayfly=true、OFFでmayfly/flying=false、外部state変更0 | NOT RUN。OFF自己解除の欠落は静的確認済み |
| foreign-before / after | どちらの順でもFH OFFがforeign flightを解除しない。fixtureから秘密API通知禁止 | NOT RUN。unknown-afterはGate Aで識別根拠なし |
| foreign-first-OFF / FH-first-OFF | 前者はFH維持後FH OFFで解除、後者はforeign維持しそのnative解除に従う | NOT RUN。古いbaselineでは現provider存否を証明しない |
| Creative / Spectator / mode往復 | vanilla authority中mayfly/flying非解除、stale自己tokenなし | STATIC source確認のみ、runtime NOT RUN |
| native dimension / death-respawn / relog / restart | 同instance移動と新playerを区別、旧token非適用、canonical再評価、同期重複なし | STATIC source確認のみ、runtime NOT RUN |
| another player / invalid-pending / optional不在 | 他player・外部state不変、不正時新規grant0/foreign false修復0、不在classloading安全 | NOT RUN。現effect predicateのpending拒否不足を別残件として保持 |
| write/sync counts | mayfly/flying遷移・ability packetを実測、毎tick反復0、canonicalとvanilla syncを分離 | 静的1writer/変更時1呼出。実測0件（未実行）。EmbeddedChannelと実認証clientを区別 |
| final compile/build/unit/check/core/Jar | 安全な製品変更後のみ焦点追加＋core75を保持し最終source/Jarで検証 | 今回NOT RUN。§30の既存vanilla75/TaCZ75/build/unit/checkは維持。現Jar identity/非混入の静的再照合のみ |

known-providerはexact MOD ID/version/native predicateとabsence-safeを証明した範囲だけ。今回FE2.7.20/EndingLibrary2.1.19fixはSTATIC AUDITED、実provider統合/clientはNOT RUN。Mekasuit/Avaritia原物は限定配置先で未確認。受領だけでunknown-afterが解決したり、起動/互換PASSにならない。
Flight購入停止・SP保護、RC=NO、REAL2CLIENT=BLOCKED・他gate維持。次は対応範囲を限定する方針変更かBLOCKED継続かの利用者判断。既存Pam/Trial/L2/FE/core試験を再実行しない。

---


</details>

---

## 14. Numeric HP HUD

- ordinary HP
- huge HP
- absorption
- creative
- survival
- combat/HUD mod installed
- resolution/UI scale
- dimension change
- death/respawn

No unexpected return to 10 hearts.

---

## 15. AttributeFix

Run matrix with AttributeFix absent/present.

Test:
- huge max HP
- base defense
- Heroics armor/toughness
- modifier duplication
- login/restart/dimensions
- removal of Food Healing does not rewrite AttributeFix state

---

## 16. TaCZ

### TaCZ absent
- game starts
- world loads
- GUI displays locked nodes
- reason key `message.foodhealing.tacz_required`: supported TaCZ 1.1.7-hotfix2 required; localized text must not claim SP was spent
- purchase rejected server-side, no SP loss

### TaCZ present
- levels1..10 cost 50 SP each (current; old 5-SP runs remain historical evidence)
- ammo save10..100
- overheat disabled from Lv1
- damage x1.05..1.50
- no Satisfaction/Gathering/Unbreaking/Armor Mastery coupling
- global base damage × TaCZ base damage × ammo damage exactly once

### 2026-09-09 locked heat-transition regressions

- Unowned or acquired/OFF hot gun: no normalization.
- OFF to ON or new purchase/default ON with hot equipped gun: normalize once.
- ON and equip another hot supported gun: normalize only the newly active gun.
- Unequipped hot inventory gun: unchanged, including repeated equip/toggle cycles.
- ON shots: no new heat/lock; one ammo decision per actual consuming shot,
  independent of hit/miss, pellet count, or penetration damage components.
- ON to OFF: do not restore heat; subsequent shots accumulate normal TaCZ heat,
  reach the normal overheat threshold, cool normally, and can fire again.
- Repeated toggles/equips: no ammo duplication/deletion or unrelated gun changes.
- Server canonical heat/lock and synchronized client state agree.
- Preserve manual-action/auto-bolt, closed/open-bolt, chamber/magazine/inventory
  ammo, reload/tactical reload, cancellation/replay boundaries, and existing
  13 TaCZ integration cases. Use the approved real 1.1.7-hotfix2 APIs/data.
- Separate-JVM persistence retains canonical SP/skills/toggles; no unnecessary
  ammo/heat runtime state in the Food Healing capability.
- No real-client Ammo Conservation PASS without separately authorized testing.

---

## 17. Backpacks

Run separately:
- Traveler's Backpack
- Sophisticated Backpacks

Auto-feed:
- ordinary food
- high Nutrition
- full hunger
- Root
- True Root reservation
- Satisfaction
- Food Diversity
- Shokugi
- HP

No duplicate processing.

---

## 18. L2 Hostility / Auto Leveling

- high-difficulty Base DR allocation gate
- no supported mod → locked
- one installed → enabled
- both installed → one shared high-difficulty stat, not duplicated

L2:
- representative hostile traits
- Purification Mastery self-nullification
- Truth Mastery permanent strip/logical-nullification
- Break Realm vs supported Undying/invulnerability
- other player without skill remains affected


### weakness/wither追加 — 限定実装・自動統合完了（2026-09-17 20:34 JST）

詳細正本は[共通計画§13](MASTERY_IMPLEMENTATION_PREPARATION.md#l2-weakness-wither-implementation)。現行allowlistはpoison/slowness/corrosion/erosion/weakness/witherの6 ID。実TargetEffectTrait.postHurtImplの2 addEffect siteと既存恒久marker/remove/sync基盤を使用。

- **最終配布Jar L2 expanded133/133 PASS**：[receipt](../build/verification/l2-weakness-wither-20260917-193800/final-l2/l2-result.json)。旧95目的を保持＋38（P18、既存effect4、通常浄化対照2、反射2、T無資格12）。旧weakness保全は旧契約、新しい対象外対照はlevitation。T範囲/再付与/同期/保存の6 ID化を別ケースとして水増ししない。
- P保護0/負対照1の即時Added event、numeric1回/source mob、通常Purification tick後の消去との区別、既存effect強弱・別source有害・beneficialのNBT保持、他player・native反射を実測。T全toggle/資格・native remove/sync・再付与/init/copyFrom/pending・全XYZ境界・同UUID/別Mob非漏洩・通常player/chunk保存再読込を確認。
- **build/unit/check PASS、vanilla60/60、approved TaCZ60/60、最終Jar Cube49/49・Invader86/86**。[集計](../build/verification/l2-weakness-wither-20260917-193800/audit/test-summary.json)。coreは既存dev Forge47.2.0、実Jar統合はForge47.4.0。試験済み配布Jar229,494 bytes/150 entries/5C1A716E…DB327、最終3環境のhash一致。
- 初回vanillaは60件後の公開鍵取得ERRORでtask FAIL。検査を緩めず、新規offline試験JVMだけlocalhost空公開鍵応答とloader versionCheck=falseを使い再実行成功。認証/二者TCPを試したことにはしない。旧Gradle環境失敗/審査拒否も保全。gameplayのFAIL/修正はなし。
- 全試験serverの通常保存/停止・process終了を確認。新weakness/wither実client **NOT RUN**。既存4 ID/Trial実clientは再実行なしでPASS維持。購入停止/SP保護・RC=NO・既存gate維持、他traitへ進まない。
- 20:34時点では新2 ID実clientはNOT RUN。後続の現在結果は下記・共通計画§13.11（旧GUI/A/B/T＋今回C/seal・通常保存終了を限定完了）。旧4停止履歴は§13.7–13.10に保全。

<a id="l2-weakness-wither-limited-client-plan"></a>

### 新weakness/wither限定実clientの現在結果（2026-09-20 17:38 JST）

詳細正本は[共通計画§13.11](MASTERY_IMPLEMENTATION_PREPARATION.md#l2-weakness-wither-client-c-completed-result)、[実測集計](../build/verification/l2-weakness-wither-client-20260920-163342/audit/completed-run-analysis.json)。run20260920-163342、製品229,494 bytes/150 entries/5C1A716E…DB327、Forge47.4.0/承認L2/Tracker0.4.4。旧§13.10のGUI/A/B/Tは限定PASSのまま、以下のA/B/TはC開始状態用の準備だけで新規PASSに加算しない。

| 単位 | 実測・判定 |
|---|---|
| prepare/準備A/B/T | prepare1・SP0/603・3nodeLv1、正常fresh/level0/native接地/NoAI/trait後の別2組。A20→17/対象付与0、B17→14/各200tick amp0付与1→同tick実remove1、T native remove各1/元sync/client空cap。**PREPARATION ONLY** |
| HUMAN歩行後安全 | 同runの通常前進→停止→Esc。server8604/client8603 pausedの個別snapshotを保存。位置(0.16319124,80,0.5)、source距離2.3368087、onGround/石床/空気/無衝突成立、water/bubble/wall/fireなし、HP14/3OFF/effects空/source空trait/marker2 ID保持。停止後9093/9094の新規2組＋予約直前9829再確認。水平速度0、Y−0.0784000015258789をそのまま記録 |
| **C** | **REAL CLIENT LIMITED PASS**。9830・17:21:43にnative1、3OFF、HP14→11。Hurt1/Damage1・各amount3・uncancelled・minecraft:mob_attack、source/directEntity/attackerの3UUIDが同source。対象2 IDのHEAD/Added/RETURN/remove各0、Wither DOT0、source空trait/marker保持 |
| GUI/client/HUD | 通常GUI3OFFがserver/client canonicalへ収束。C後9838/9837・sequence44727で新規2組一致。HUD11/20のF2。B瞬間iconの目視PASSなし。server-only markerとclient capを区別 |
| seal/保存/終了 | **完了**。SEALED/prepare1/hit3。17:29:45全保存後readonlyで本人HP11/3OFF/SP0・603/空effectsと同sourceHP20/lv0/空trait/marker2 IDを確認。17:30:09Quit→PID1168不在。再読込/範囲外/cleanup/repair/再送0 |
| 境界 | helperのみ44,267 bytes/18 entries/A7B86765…16FB55を専用offline compile/reobf。製品146/旧証拠304/旧4run853・追加0/旧metadata24件不変。旧suiteを再実行していない。自然AI戦闘/新2 ID実client再読込/他trait/全体readiness PASSへ拡張しない |

COMPUTER USE=GUI/command/F2、HUMAN=通常歩行のみ、AUTOMATED=native攻撃/readonly記録。新規Cは1回のみで、リセット後の利用者メッセージ到着時にはC-DONEが存在したため再送なし。個別証拠は[歩行後snapshot](../build/verification/l2-weakness-wither-client-20260920-163342/audit/human-walk-post-pause-snapshot.json)、[C](../build/verification/l2-weakness-wither-client-20260920-163342/audit/C-events.json)、[保存](../build/verification/l2-weakness-wither-client-20260920-163342/audit/final-save-values.json)、[不変照合](../build/verification/l2-weakness-wither-client-20260920-163342/audit/final-invariants.json)。両購入停止/SP保護/RC=NO/REAL2CLIENT=BLOCKED維持。次候補は§12.5のreadiness判断整理であり、C再試験や新機能実装は未開始。

#### 過去の部分結果（2026-09-18 22:16 JST・現在の未完了一覧ではない）


詳細正本は[共通計画§13.10](MASTERY_IMPLEMENTATION_PREPARATION.md#l2-weakness-wither-client-level0-partial-result)、[実測](../build/verification/l2-weakness-wither-client-20260918-213302/audit/stopped-run-analysis.json)。新run20260918-213302、製品229,494 bytes/150 entries/5C1A716E…DB327、Forge47.4.0/承認L2/Tracker0.4.4。以下は今回の実施結果。後続の旧21:12記録は履歴であり、現在の未完了一覧へ戻さない。

| 単位 | 実測・判定 |
|---|---|
| source準備 | 正式reinit(z,0,false)1回。lv0/baseMAX20/effectiveMAX20/HP20/attack3。native hostility_health amount0を保持。自然接地3695/3696→NoAI後3697/3698→正式weakness1/wither1後3699/3700の別2組ずつ、PREPARED成立 |
| 本人prepare/GUI | prepare1、3nodeLv1、SP0/603、P/M ON/T OFF。通常GUI P OFF→ON非連動、製品packet/sync/actual LocalPlayerの新規2組収束。GUI購入成功ではない |
| A | **REAL CLIENT LIMITED PASS**。native1回、HP20→17、各対象ID EffectUtil HEAD/RETURN・Added・remove0、source2trait保持/markerなし |
| B | **REAL CLIENT LIMITED PASS**。native1回、HP17→14。各weakness/wither200tick/amp0、HEAD1/Added1/RETURN1、attack returnで実instanceあり→同tick通常浄化のremove前実instance/uncancelled通知/RETURNtrue各1→なし。Wither DOT0 |
| T | **REAL CLIENT LIMITED PASS**。GUI M ON→T ON、攻撃0。製品のnative remove各1、元L2 sync、server markerVersion1/同UUID/2 ID、実client空cap2組収束。HP14維持 |
| 全OFF GUI | T→M→P OFF、canonical/HP14/空effects/空sourcecapのserver/client2組収束。marker保持。C攻撃の証拠ではない |
| C/seal | **NOT RUN / UNVERIFIED**。同床へ戻す通常tp直後のplayer guardで停止。C-FIRING/DONE・sealなし。HP11未観測 |
| numeric/帰属 | A/B各Hurt1/Damage1/amount3/uncancelled、damageType=minecraft:mob_attack、sourceUUID/directEntityUUID/attackerUUID独立キーが同Zombieを指す。Heal0。guts raw通知は対象2 IDの実removeに数えない |
| GUI/HUD/client | HUD20→17→14/MAX20、取得/SP・toggleと独立canonical/cap収束を確認。Bの瞬間icon目視は未確認、server即時とclient収束後空を区別 |
| 保存/終了 | **停止状態の保存・終了完了**。22:07:30全保存→readonly：本人HP14/3OFF/SP0・603、同sourceHP20/lv0/trait空/marker2 ID→22:08:19Quit/PID12604終了。prepare1/hit2、再読込/範囲外/cleanup/修復/再試行0 |

- 生成前Survival/Normal/cheats/structures OFF/自然湧き・自然回復false、閉じた床壁屋根＋source固体隔離室。helper41,850 bytes/18 entries/BD6264C7…2E049だけ専用offline compile/reobf。製品task/既存suiteを含まない。COMPUTER USEのUI、AUTOMATEDの明示native攻撃/readonly記録、HUMAN操作なし。
- **停止の限界**：Bのknockback後に測定距離へ戻す許容tpを1回実行した直後、`environment(player)`の複合guardがfalse。瞬間player個別operandがないため、source cached値や後時点の保存OnGround0で原因を断定しない。製品不具合とは判定しない。次はguard/期待値を変えないplayer個別operand観測＋通常歩行での位置復帰の整合を、別新run承認後に補助限定で扱う。今回は停止後の補助変更/再起動なし。
- 製品等146/旧証拠304/旧3 run606・追加0/旧instance metadata24件不変。全体PASS・C/seal・新2 IDの保存再読込PASSへ昇格しない。購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKEDと既存PASSを維持。

#### 過去の停止記録（2026-09-18 21:12 JST・現在の指示ではない）

以下はrun20260918-204657当時の記録。その後のlevel0準備成立・GUI/A/B/T部分実測は上記と共通計画§13.10。旧記録の「次」「未実施」は当時の範囲を表す。

##### 当時の限定実client（2026-09-18 21:12 JST・初期level/HP不一致）

開始条件とA/B/T/C期待値の正本は[共通計画§13.6](MASTERY_IMPLEMENTATION_PREPARATION.md#l2-weakness-wither-client-plan)、最新の承認済み準備変更・実測・原因は[§13.9](MASTERY_IMPLEMENTATION_PREPARATION.md#l2-weakness-wither-client-initialization-mismatch)。run20260918-204657でhelper限定compile/reobfと新安全world、prepare1回を実施。初期AIを許可したsourceがL2 reinit level1によりHP/MAX20.6となり、期待level0/HP20に不一致で停止。trait付与/NoAI切替/prepare-complete/native攻撃は0、通常保存/終了済み。旧瞬間operand未採取の停止§13.7と自然接地timeout§13.8は当時の証拠として保全。製品P/T効果FAILとは判定しない。

- 最新製品 **5C1A716E…DB327 /229,494 bytes/150 entries**、MC1.20.1/Forge47.4.0、承認L2/依存5Jar。新instance/world、生成前Survival/Normal・自然湧き/自然回復false、閉じた屋根付き室、fresh HP20/MAX20・無装備/無防御/効果なし。購入停止のまま本人3nodeLv1・credit603/spent603/unspent0を別補助prepare1回。Persistent Zombie1体を本人から固体壁で隔離し、初期通常AIで自然接地2連続→NoAI→正式2 ID rank1→安定2連続を要求する補助を作成。今回はsource生成までで、NoAI固定/trait付与には未到達。旧4trait/剣/Trial/133fixtureを配置しない。
- 未実施の手順：通常GUI P OFF→ONで非連動と本人canonical収束を確認し、その後は下表。各GUI→製品packet→server→実LocalPlayer、sourceの元L2 sync→実client capを独立に記録する。今回はこのGUI操作より前に停止した。

| 単位 | 開始P/M/T・native攻撃 | 期待値・合格条件 | 実施状態 |
|---|---|---|---|
| A 本人保護 | ON/ON/OFF・1回 | HP20→17、各新effect付与0/実remove0、source2trait保持/markerなし | NOT RUN |
| B 実付与対照 | MのみOFF、ON/OFF/OFF・1回 | HP17→14、weakness/wither各200tick/amp0・Added1、attack return時に実在。同tick通常浄化の実remove各1後は空、source2trait保持 | NOT RUN |
| T 製品除去/同期 | M ON→T ON・攻撃0 | HP14保持、同sourceをnative remove各1、server marker2 IDと実client空cap。補助が除去を代行しない | NOT RUN |
| C 受け手保護なし | T→M→P OFF・1回 | HP14→11、各付与/実remove0、同source空cap/marker保持、通常数値3だけ成立 | NOT RUN |
| GUI/HUD・同期・seal | 全OFF/seal・追加攻撃0 | GUIの取得/SP/ON設定と独立client/server2連続新規snapshot一致。HUD17/14/11・MAX20。prepare1/hit3/SEALED | GUI/攻撃後HUD/規定収束/sealはNOT RUN。初期HUD20/20のみ確認。sourceは未付与の空capで2trait表示なし |
| 停止状態の通常保存・終了 | FAILED・追加攻撃0 | 値を修復せず保存→readonly→Quit/process終了 | **完了**：本人HP20/3取得/SP0・603/P/M ON・T OFF、sourceHP20.6/lv1/trait空/markerなし・NoAI false。再読込0 |

- 3攻撃すべてnative `Zombie.doHurtTarget`、Hurt/Damage各1・`minecraft:mob_attack`・amount3・同attacker/direct、非cancel。HIGHEST ENDで攻撃/実instance観測→製品NORMALで通常浄化→LOWESTで実remove完了を記録する。Witherを次のeffect tickへ持ち越さず、補助がDOT取消/効果除去をしない。DOT/余分なHP変化は停止理由で、差し引き補正しない。
- EffectUtil HEAD/RETURN・Added・remove前実instance/通知/実return・攻撃return・通常浄化後を **case/entity/tick/対象effect ID** ごとに記録。B各ID1が観測器の正対照。guts等の対象外Remove通知を実除去へ誤集計しない。B一瞬のiconは目視必須にせず、server実測とclient収束後表示を分ける。
- HP/amount/MAXは1e-3、ID/rank/SP/回数は完全一致、HUD丸め±0.05相当。収束期限は100server ticksまたは独立monotonic wall10秒の先着、ポーズを除外せず、新規client観測2組。初期接地は独立200server ticks/wall10秒。source未追跡は空capと数えない。異常時は次のhitを拒否し即pause/通常退出、FAILED/receiptを保全。値修復/再試行/期待値緩和なし。
- **再読込0・範囲外移動なし**を推薦。6 IDの保存復元は133件、同じ基盤の実client再読込/再追跡は§10.11で完了。今回追加するのは新2 IDの実GUI/LocalPlayer/元sync/HP表示。seal後の通常保存値（3取得/SP0・603/3OFF/HP11/空effects、同source marker2 ID/空trait/対象外data）は読み取り照合する。今回を新2 ID実client再読込PASSへ拡張しない。
- 既存effectの強弱・他source有害/有益保全、別player/反射、全資格/再付与/境界等は133件に任せ、注入による追加画面テストは不要。通常PがHARMFULを消す既存動作と極意の非治療を混同しない。旧4 ID/Trial実client・自動suiteは再演しない。
- 今回の停止詳細：[source-safety](../build/verification/l2-weakness-wither-client-20260918-204657/audit/source-safety.jsonl)はgameTime4037の**新規1回**。failureのcached値を2回目に数えない。source位置(2.5,80,.5)、velocityY−.0784000015258789、onGround=false/fall0、石床差0/水壁火false/外部敵空、foreign damage/heal0。**L2 lv1/HP・MAX20.6**、NoAI=false、PersistenceRequired=true、attack3/armor2/装備空、rank各0/markerなし。自然接地2連続・NoAI後2連続は未確認。native物理経路と全operandは§13.9。
- EffectUtil/Added/実removeは新2 ID各0、Hurt/Damage/Heal0。準備sync HEAD/RETURN各2、case=PREPARATION、実client lv1/空capはT除去成功ではない。guts2組は実instanceなし/RETURN false。本人server/保存/実LocalPlayer HP20/MAX20・3取得/SP0/603を確認。GUI/A/B/T/C/sealはNOT RUN / UNVERIFIED。
- 21:00:22.883全保存→保存値readonly→21:01:13.246 Quit→PID27036不在。保存sourceHP20.6/lv1/traits空、OnGround1は後時点であり準備snapshotの代替にしない。修復/再試行/範囲外/再読込/cleanup0。**COMPUTER USE**がGUI/command/F2/保存終了、**AUTOMATED**が準備/観測/保存解析、HUMANゲーム操作なし。
- 次は初期1回の正式L2 level0準備（候補reinit(z,0,false)）の整合を、新run承認後にhelperだけ最小修正する。Config/player寄与を含む成立は未実測。observerのdamageType/sourceUUID/directEntityUUID/attackerUUID分離は今回修正・build済み、native攻撃での観測は未到達。期待値/guardを緩めず、追加MOD不要。製品146/旧証拠304/旧2 run398/旧instance metadata22件不変、旧PASS・購入停止/SP保護/RC=NO/REAL2CLIENT=BLOCKEDを維持。

#### 過去の停止記録（2026-09-17 22:25 JST・現在の指示ではない）

以下3項はrun20260917-220121当時の記録。後続の接地方式/observer修正は§13.9に記録済みであり、ここでの「次」「未修正」を現在へ復活させない。

- 今回の停止詳細：[個別operand](../build/verification/l2-weakness-wither-client-20260917-220121/audit/source-safety.jsonl)はgameTime2804〜3004の201回＋failure直前1回。全onGround=false、位置(2.5,80,.5)/速度0/床差0、HP20/MAX20、NoAI/Persistence true、水/壁/火false、fall0、外部敵空、foreign damage/heal0。弱体/衰弱各rank1、markerなし。接地未成立を保存値から推定せず、その瞬間の値で記録した。prepare-completeとcase収束の作成を見送った。
- EffectUtil/Added/実removeは新2 ID各0、Hurt/Damage/Heal0。準備sync3組と実client2trait表示はT remove/空capの成功ではない。guts381組は実instanceなし/return falseで別扱い。保存後Health20・3取得/SP0・603/P/M ON・T OFF、同source2trait保持をreadonly照合し、22:17:25全保存/22:17:55 Quit/PID11668終了を確認。再読込/範囲外/cleanup/修復/再試行0。
- 次は**NoAI固定の受動待機を繰り返さず、自然physicsによる初期接地→NoAI固定などの準備方式変更を別承認**する。静的に見つかったobserverの`source`キー競合も、DamageTypeとUUIDを分離する補助限定修正の対象候補（今回は未修正・未実測）。guard/期待値を緩めない。追加MOD不要。製品146/旧証拠304/前run全198/旧instance metadata20件不変、既存cachedVolatile例外を保持。両極意IMPLEMENTATION_PENDING/購入停止/SP保護・RC=NO/REAL2CLIENT=BLOCKED等を維持。

以下は旧4 ID契約の承認手順・完了履歴であり、新2 IDの試験結果へ遡及変更しない。

<a id="l2-2519-focused-plan"></a>

### L2 Hostility 2.5.19 — focused implementation / automated integration completed (2026-09-15 19:35 JST)

Artifact/dependencies, actual call sites and implemented files are centralized in
[shared plan §10](MASTERY_IMPLEMENTATION_PREPARATION.md#mastery-l2-2519).
Receipt and static bytecode review are complete for the stated body-side paths
and the missing dependency internals ([§10.7](MASTERY_IMPLEMENTATION_PREPARATION.md#mastery-l2-dependencies)).
The approved four-trait implementation and final packaged-Jar integration now pass95 cases;
results and limits are recorded below. Exact
allowlist: `l2hostility:poison`, `slowness`, `corrosion`, `erosion` (all four use the
l2hostility namespace). Other traits, Auto Leveling and Break Realm are separate scope.

- Use real approved L2, actual dependency Jars and a new isolated server/fixture under the approved limited implementation. Assign the registered traits through native APIs to
  a real supported mob; wait for native pending assignment/initialization, then use
  its real attack/event/tick path. Synthetic DamageSource/helper-only checks are
  separate diagnostics, not real L2 integration PASS. Record actual selected loader
  and JarJar versions, not only the metadata ranges. Use approved Library2.5.3 /
  Complements2.6.1, existing Curios/Patchouli and MC1.20.1/Forge47.4.0. Hostility embeds
  Tracker0.4.3 with [0.4.3,), Complements embeds0.4.4 with [0.4.4,); expected selection
  among these candidates is0.4.4, now confirmed by the final real loader. Verify loaded version and API/hooks;
  do not externally duplicate bundled Jars. Required API classes are byte-identical,
  but magic classification data differs: do not assume all0.4.3 behavior carries over.
- Purification: both acquired/ON vs each OFF, unowned, malformed and migration-pending.
  Verify own canonical SP/levels/saved toggle; parent OFF never rewrites mastery ON.
  Keep source mob traits unchanged; another player remains affected. At the actual
  TargetEffect add call, the protected player receives no new poison/slowness; record
  an immediate observation before normal Purification's tick cleanse could hide a
  failure. Preserve other-source and beneficial effects and native reflection targets.
- Corrosion/Erosion: damaged and fresh equipment, damaged-count and remaining-durability
  calculations, rank and insufficient-equipment branches. Protected target receives
  zero additional trait equipment wear, while native selection/count and ordinary
  numeric damage—including that native insufficient-equipment multiplier—remain.
  Do not cancel the whole onHurtTarget or fake the selected count to make this pass.
- Truth: [three acquired/ON condition LOCK](SKILL_TREE_SPEC.md#truth-mastery-activation).
  Test all toggle combinations; only all ON applies to new mobs. Any OFF stops new
  applications but never restores already-nullified traits or rewrites other toggles.
  Missing/invalid/pending ownership never qualifies. Purchase prerequisites and500SP
  remain unchanged; both mastery purchases still return IMPLEMENTATION_PENDING with
  no SP debit. Effect data prepared in a fixture is not successful GUI purchase evidence.
- Truth spatial/persistence: ±75 AABB boundaries on X/Y/Z, spawn/entry/teleport/player
  movement, exit/re-entry, overlapping players. Remove only the four supported IDs;
  native mob level/scaling, HP/base attributes, AI/type/loot, unrelated traits/caps and
  other players' canonical data remain. A stripped trait's own bonus is removed;
  the remaining ordinary attack is not canceled. No full-region every-tick scan or
  forced chunk loading. Record candidate counts and bounded fallback work.
- Reapplication: native setTrait/command queued assignment, initialized and natural
  init/reinit paths on the same marked mob, pending additions and repeated processing.
  Never clear the whole pending queue/map. The supported effects cannot leak before
  pruning. Assert actual callback suppression as well as marker/map contents. Include
  stable UUID, normal save/load and chunk unload/reload; verify original tracking sync
  to client payload and persistent marker, no re-grant or unrelated data loss. Native
  codec details are now statically confirmed in §10.7. Assert that native empty traits
  Compound clears the previous client map, preserving unrelated fields. A missing key
  is not equivalent to an empty map; partial fromTag injection is not a removal API.
  Direct tag edits are not a substitute for normal serialization and reload.
- Safe optional absence and unsupported-version behavior; both readiness gates remain.
  After a production change run build/foodHealingUnitTest/check and relevant
  vanilla/TaCZ/Trial regression, preserving existing cases and reporting actual counts.
  Inspect the regenerated product Jar: metadata/Mixin/refmap/hash/size, no external
  L2/bundled libraries/fixture/ExampleMod. Stop/save any future test servers normally.

Initial batch does not assert all L2 traits/bosses, Pulling/Repelling client motion,
attribute-trait cleanup, real TCP/two-client or all MODpack compatibility. Four-trait
implementation authorization is fulfilled; both purchase blocks and SP protection remain.
No new specification question remains for this scope. Other scope and realclient require
separate approval.

**実施結果 2026-09-15 19:35 JST: AUTOMATED INTEGRATION TESTED（4 ID限定）**

- [最終配布Jarで95/95](../build/verification/l2-implementation-20260915-182900/final-l2-fixed/l2-result.json)：P36、装備20、他効果/反射2、購入拒否2、T資格14、範囲/包含14、level5対照1、再付与/反復2、sync/player保存2、chunk保存復元2。[具体的な操作・内訳・制約](MASTERY_IMPLEMENTATION_PREPARATION.md#l2-implementation-result)。実native Zombie attack/event/tick、正式trait付与を使用。scope外のhelper/markerだけでattack PASSにしない。
- Pは即時poison/slowness、追加wear0、numeric/選択RNGを対照確認。Tは受け手のPを無効にし、通常攻撃を残して対象traitの作用/数値ボーナスを消す。Lv5の通常損失3.300003は無trait同levelと一致、sourceHP16保持。
- native player保存再読込、chunkアンロード/再ロードで別entityインスタンス、marker/対象外cap/HP/modifierと再付与抑止を確認。元payload/codecの空traits mapとEmbeddedChannel送信を確認。実client/TCP同期・別JVM再起動の証拠ではない。
- [build/unit/check](../build/verification/l2-implementation-20260915-182900/audit/plugin-fix-build.log)、Truth53 assertions、[vanilla60/60](../build/verification/l2-implementation-20260915-182900/audit/final-vanilla-fixed.log)、[TaCZ60/60](../build/verification/l2-implementation-20260915-182900/audit/final-tacz-fixed.log)成功。既存59＋Truth packet/toggle同期1。L2不在の起動・無介入を確認。同じ最終Jarで[Trial49/49](../build/verification/l2-implementation-20260915-182900/final-trial-fixed/trial-result.json)。旧59/57/49等と旧実client記録は当時のまま保持。
- Mixin版判定によるゲームclass早期loadをTrial回帰で検出しpure版判定へ修正。fixtureの属性/login/反射対象/query準備不備も修正。[FAIL/PASSと正常停止の全記録](../build/verification/l2-implementation-20260915-182900/audit/test-summary.json)。期待値緩和・不正データ修復・購入解放なし。
- 最終Jar **225,242 bytes /146 entries / SHA256 D6F5032C3B40A78D2BCCEDB554EE1C302AA3C5367080F93E35184A5A96162D9A**、実L2/Trial配置同hash、外部/fixture/GameTests/ExampleModなし。[現物検査](../build/verification/l2-implementation-20260915-182900/audit/final-jar.json)。全server通常保存・終了。実client/Prism起動なし。
- 未検証：他trait、未対応版の実物起動（版拒否は単体のみ）、実client/HUD/自然歩行、実2-client/TCP、別JVM再起動、L2＋Trial同時構成、大規模候補数/TPS/長時間。範囲query間隔と反復の安全性を確認したことだけで性能PASSにしない。両極意IMPLEMENTATION_PENDING/SP保護・RC=NO維持。

---

<a id="l2-limited-client-plan"></a>

### L2 4特性の限定実client — 承認済み手順 / 新run限定完了

操作・準備値・必要ファイル・保存手順の正本は[共通計画§10.9](MASTERY_IMPLEMENTATION_PREPARATION.md#l2-client-plan)。20:04の計画は旧中断§10.10を保全し、新補助・新runで限定完了。[実施結果§10.11](MASTERY_IMPLEMENTATION_PREPARATION.md#l2-client-completed-result)。下記は期待値であり、実測と混同しない。既存自動suiteと旧Trial実clientは再試験していない。

- 新しい別MOD/allowlist/instance/worldで、生成前にdoMobSpawning=false・naturalRegeneration=false、Normal/Survival。屋根/床/壁で日光燃焼・待機中の侵入/外部damageを防ぎ、NoAIの新source1体、本人HP20/最大20・防御なしを確認。旧world・receipt/journal・95件fixtureは使わない。
- 本人の正常fresh状態へprepare1回：P/M/T各Lv1、credit603/支出603/未使用0、P/M ON・T OFF。SPと取得以外のcanonicalを保持、製品保存/syncへ接続。製品の両購入停止/readinessを変更しない。取得済み状態の準備であり、通常GUI購入成功の証拠ではない。
- native setTrait/pending/tickでpoison/slowness/corrosion/erosion各rank1を正式付与。通常GUIの親OFF→ONで各保存toggle非連動を確認後、共通計画の3攻撃を行う。AではT OFFを維持しsource4 trait不変のまま本人保護を確認。BではMだけOFF、P ONの新規付与を通常浄化前に記録。CではTによる正式除去後に3つOFFとし、Pの保護/後消去で残存traitを隠さない。
- HP予定20→17→14→11。無enchantの手持ちdiamond_sword Damage100→100→182→182（最大1561）。Bで実corrosion追加10・erosion追加72を各呼出し前後に記録、通常無防具被弾による手持ち剣消耗0と分離。native mob_attackの数値3とsource帰属を維持。BはPOISON200tick/amplifier0・SLOWNESS160tick/amplifier1各1の即時存在→同tickの通常浄化後なし。helperの消去/修復で合わせない。
- HIGHESTの本人ENDで明示予約したnative attackを1回実行、return時snapshot→製品NORMAL浄化→後snapshotを記録。人間のtick操作には依存しない。HP/amount/MAX_HEALTH許容1e-3、整数値・回数・markerは完全一致、HUDは実client値の小数1桁。client収束は共通計画の上限内で確認する。一瞬のiconを撮れなければserver即時実測とclient収束値/目視の範囲を分ける。
- T適用は製品の範囲/tick処理・公開remove・元L2 syncを通し、server markerと実clientの空trait capを区別して記録。全OFF・範囲外でseal、通常保存→同world1回再読込→全OFFのまま再接近/再追跡し、同source UUID/marker・4 ID除去・対象外保存データ・本人SP/取得/toggle/HP/剣保持を確認する。prepare1/hit3/SEALEDのまま再付与なし、再度通常保存→Quit Game→process終了。
- 判定をA本人保護、B正の対照、T native除去/同期、C全OFFで作用なし、恒久性/対象外保存、GUI/HUD/装備、正常終了に分ける。未観測はUNVERIFIED、想定外damage/heal/耐久/traitや途中失敗は次の攻撃を中止し通常pause/退出、receiptを消してやり直さない。
- 対象外：自然spawn/AI連続戦闘、毒持続/DOT・歩行速度、他trait/反射/他player/全装備/全toggle/全境界・明示再付与の再現、負荷、別JVM/TCP/実2-client、L2＋Trial同時構成。製品修正・新download・購入解放は次回この計画の実行承認にも含めない。両極意IMPLEMENTATION_PENDING/SP保護・RC=NOを維持。

---

<a id="l2-limited-client-stopped-result"></a>

### L2限定実clientの過去の中断結果（2026-09-15 22:57 JST）

以下は当時の結果。後続の新runは下記完了節と共通計画§10.11。旧A総合UNVERIFIEDを昇格させない。

対象3807DECC…76DE6 /229,423 bytes/150 entries、MC1.20.1/Forge47.4.0、L2H2.5.19/Library2.5.3/Complements2.6.1、実採用Tracker0.4.4。別補助offline build成功、新run `20260915-220916` を使用。詳細・証拠は[共通計画§10.10](MASTERY_IMPLEMENTATION_PREPARATION.md#l2-client-stopped-result)、[集計](../build/verification/l2-client-20260915-220916/audit/stopped-run-analysis.json)。

| 判定単位 | 今回の実測と結果 |
|---|---|
| 安全準備/once取得 | 生成前GUIで自然湧き/自然回復OFF、Survival/Normal、閉じた石室/不透明屋根・周囲guard。本人freshからprepare1回、3node Lv1/SP0・603・P/M ON/T OFF、無enchant剣Damage100。通常GUI購入成功ではない |
| 親toggle | GUI P OFF→ON、M ON/T OFF非連動をserver/client canonicalで確認。2連続の攻撃前同期を記録 |
| A | 攻撃1回、hurt/damage各1/source mob_attack/3、HP20→17、効果新規付与なし、剣100、source4trait保持。実測一致だが補助finish FAILでA総合UNVERIFIED |
| B/T適用/C | NOT RUN / UNVERIFIED。毒鈍足の正対照、110→182、製品remove/空clientcap、全OFF比較を未実施 |
| 恒久性/範囲外/seal/再読込 | NOT RUN / UNVERIFIED。成功状態を作るための再注入・同case再試行なし |
| GUI/HUD/装備/同期 | P切替、SP/取得表示、独立client HP17/max20/sword100/4traitとHUD17.0/20.0を部分確認。M/T切替・精密耐久tooltip・Bの一瞬のiconは未観測 |
| 保存/終了 | 中断状態の通常保存/終了PASS。両player保存Health17/SP0・603/剣100/P/M ON/T OFF、同Zombie4trait保持。通常再読込0、prepare1/hit1/DONE0/SEALEDなし。22:45:42全dimension保存・22:46:11Quit、PID17792終了 |

停止原因は補助が対象外 `foodhealing:guts` のRemove要求まで「通常浄化の実除去」に数えたこと。生log・Forge実bytecode・RootControllerの読取で確認し、製品不具合とは判定しない。次回は対象poison/slownessの実instanceと前後状態・ID/case/tickを観測し、対象外通知は生logに別記録する補助修正＋新runの承認待ち。今回の補助/receipt/worldはそのまま保全。期待数値/回数やstop guardは緩めない。

実施主体はCOMPUTER USEの通常UI操作、AUTOMATEDのnative明示攻撃/観測・保存解析。HUMAN入力なし。製品source/test/build.gradle/Jar・購入停止・SP保護は不変、旧PASS維持。RC=NO・既存個別gate/対象外範囲を維持。

<a id="l2-limited-client-completed-result"></a>

### L2限定実clientの新run完了結果（2026-09-16 19:45 JST）

run `20260916-185745` /製品3807DECC…76DE6・229,423 bytes/150 entries不変。MC1.20.1/Forge47.4.0・承認L2依存構成、Tracker0.4.4実ロード。新補助は33,309 bytes/17 entries、BAF84079…4D106。詳細・環境・build限定範囲・実施主体・証拠は[共通計画§10.11](MASTERY_IMPLEMENTATION_PREPARATION.md#l2-client-completed-result)に集約。[新実測の照合](../build/verification/l2-client-20260916-185745/audit/completed-run-analysis.json)。

| 判定単位 | 新run実測 / 合否 |
|---|---|
| observer修正 | 対象poison/slownessをentity/case/tickで集計。remove実instance/通知/実return/後状態を分離、gutsはraw保持のみ。A/C各0、B各1の正対照成立。製品/数値/guardは変更なし |
| 安全準備/once取得 | 生成前2gamerule false、Survival/Normal、閉じた屋根付き2室/侵入guard。fresh本人prepare1、3nodeLv1/SP0・603・P/M ON/T OFF。通常GUI購入成功ではない |
| A本人保護 | **LIMITED PASS**。HP20→17、対象新規add0/実remove0、剣100、source4trait保持。native1/hurt1/damage1・mob_attack3 |
| B通常浄化との分離 | **LIMITED PASS**。HP17→14。poison200 amp0/slowness160 amp1各Added1、attack returnに存在、remove要求前に実instanceあり/return true/後空。剣100→110→182、source4trait保持。native1・numeric3 |
| T製品除去/元sync | **LIMITED PASS**。GUI M ON→T ON、同source4traitを製品が正式remove。server marker/実client空capを別観測、HP14/剣182不変 |
| C全OFF | **LIMITED PASS**。GUI T→M→P OFF後、HP14→11・対象新規add0/追加wear0/剣182/空traits保持。native1・numeric3、受け手保護なし |
| GUI/HUD/装備/同期 | **LIMITED PASS**。親OFF→ON非連動、P/M/T切替と本人canonical/対象traitの独立server/client収束2回。HUD17→14→11/MAX20、剣残1461→1379/1561。B一瞬のiconは未撮影、server即時付与と収束後client空に限定 |
| 全OFF・範囲外・恒久性 | **LIMITED PASS**。X差90でseal、prepare1/hit3/SEALED。sourceは90block時も追跡されておりunloadと誤記しない。同world通常再読込1回、全OFFで同UUID再追跡、marker/空traits/対象外cap/attributes保持 |
| 保存/終了 | **LIMITED PASS**。19:28:06/19:31:21全dimension保存、19:31:41Quit/PID484終了。両player保存HP11/DeathTime0/SP0・603/3取得3OFF/剣182、source保存・receipt hash一致。再付与/修復/追加attackなし |

製品144ファイル/配置Jarは不変、旧ゲーム証拠も不変。ただし旧instance `mmc-pack.json`のLWJGL cachedVolatile:true削除を検出、書換主体未確認（[例外記録](../build/verification/l2-client-20260916-185745/audit/old-instance-metadata-exception.json)）。事後集計初回の停止を残し、実測判定とは分離した。旧A総合UNVERIFIEDは過去のまま保持。

COMPUTER USE=通常GUI/command/F2/保存再読込/終了、AUTOMATED=一度だけの準備/native3攻撃/readonly観測、HUMAN入力なし。自然AI戦闘/毒持続・全装備/全trait/全95/別JVM/TCP/実2-client/L2＋Trial/侵略者実clientは未確認。既存suite再実行・製品変更・購入解放なし。両極意購入停止/SP保護・RC=NO・既存個別gateと可逆クラフト既知許容仕様を維持。

## 19. Trial Monolith

現行配布Jarの自動回帰は2026-09-17の[§18/共通計画§13](MASTERY_IMPLEMENTATION_PREPARATION.md#l2-weakness-wither-implementation)でCube49/49・Invader86/86 PASS。下記の旧実client/過去自動結果は当時のJarと条件のまま保持し、再実client試験は行っていない。

後続L2変更時の49ケースは§18の当時の結果。今回の侵略者追加後も最終Jarで49/49回帰（下記）。旧Jar/実client記録は当時の証拠として保持する。

### Invader Monolith 1.4.9 — 新run残確認完了（2026-09-16 21:51 JST）

新run `20260916-211828`。詳細は[共通計画§11.8](MASTERY_IMPLEMENTATION_PREPARATION.md#trial-invader-client-followup-result)、[新run生証拠集計](../build/verification/trial-invader-client-20260916-211828/audit/completed-run-analysis.json)。製品229,423 bytes/150 entries/3807DECC…76DE6、Trial1.4.9/Forge47.4.0/Java17.0.15不変。外側modsは製品/Trial/新helperのみ。旧5ケースの計画/失敗履歴は下記へ保全し、同じF1/F2/S1を再証明しない。

| 実施単位 | 実測・合格条件との照合 | 判定 |
|---|---|---|
| 一度だけのprepare/開始状態 | fresh・ルール/閉鎖区画を確認。P/M各Lv1、SP0/103、Truth/Root未取得、HP20/MAX20/Soul0/flagfalse。外部flag初期設定0。準備用Small1回でSoul0/flagfalseを保持しHP18.5、native numeric1.5 | **PREPARATION COMPLETE**。GUI購入PASS/旧S1再試験/新PASS件数ではない |
| S2 M OFF/P ON | registered Small→Owner NBT/native owner→ray/callback各1。Soul0→0.01、HP18.5→17、flagfalse、actualHurt/Hurt/Damage各1×1.5 uncancelled、同Invader owner/direct | **REAL CLIENT LIMITED PASS**。[DONE](../build/verification/trial-invader-client-20260916-211828/audit/S2-DONE.json)：strict物理安全と独立server/LocalPlayerのcanonical/Soul/flag/HP/MAXが2連続一致、3ticks/147.3694ms |
| H1 両ON | S2-DONE後GUI M ON、HP17/Soul0.01/MAX20/flagfalse、免疫/Core/bypass/Root/Truthなし。native Huge1/本人callback1、Float.MAX_VALUE3.4028235E38→専用redirect各1/保護true、実hurt/Hurt/Damage/death0、HP/Soul不変 | **REAL CLIENT LIMITED PASS**。[DONE](../build/verification/trial-invader-client-20260916-211828/audit/H1-DONE.json)：3ticks/146.7827ms/strict安全＋2連続収束。射線外/owner違い/callback0による無傷ではない |
| GUI/HUD/同期 | 通常GUI M OFF/ON→製品packet/server/実LocalPlayer。SP0/使用済103、HUD17.0/20.0とreadonly値を対応。Soul/flagはobserverで観測 | **LIMITED PASS**。通常画面にないSoul/flagを目視PASSへ補完しない |
| seal/cleanup | prepare1/nativeTick0/preparationSmall1/measurementSmall1/Huge1。両ON/SP0・103/Soul0.01/flagfalse/HP17/MAX20。所有UUID/run/world一致の1体だけnative setRemoved経路、worldAbsent=true、本人値不変 | **PASS**。旧cleanup FAILは保全 |
| 通常保存/終了 | Save & Quit→全dimension保存→readonly2保存でHealth17/MAX属性20/Soul0.009999999776482582/flagfalse/両ON/取得・SP/DeathTime0/GameRules保持、source UUID0件→Quit Game/PID11644終了 | **PASS**。world再読込0（今回不要/未実施を維持） |

- 物理判定前に全operand/time/run/caseを同一snapshotへ記録。S2は8564 native returnのvelocity0→8565接地false/vertical -0.0784000015258789→8566/8567接地true。位置・fallDistance0・火水なし・stone支持/air feet/head・敵/外部damage0を確認して自然収束を待ち、setter補正0。H1/準備用Smallも同じ一過性。原因不明・実危険・期限超過は停止し、完了guardはstrict接地2連続のまま。
- 100ticksと独立monotonic wall10秒watchdogを実装し、DONE直前まで両方検査。今回の完了は期限内、期限超過の意図的負試験は未実施。旧F1/F2/S1の実測を新guard試験へ転記しない。
- local Gradle/offline・helper限定compile/reobf成功、製品/既存suite/購入停止不変。COMPUTER USE=GUI/command/F2/保存終了、AUTOMATED=準備/native/readonly観測、HUMANゲーム入力0。今回の予定限定確認は終了。自然boss/Beam描画・force/閾値死/Huge OFF死亡/他owner・player実client/全86/一般MobEffect未特定/L2＋Trial/他trait/全readinessへ拡張しない。
- 旧runのF1/F2/S1は下記のPASSを保持、旧S2/H1を過去からPASSへ書き換えない。現行の限定5ケースは2 runの証拠で充足。両極意IMPLEMENTATION_PENDING/購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKEDと個別開始条件を維持。

### Invader Monolith 1.4.9 — 限定実client部分実施（2026-09-16 21:04 JST・過去履歴）

**以下は旧run当時の結果/次回条件。新runによる残確認完了は直上および§11.8を参照。旧停止・cleanup失敗・未実施はそのまま保全する。**

実施run `20260916-201822`。承認前の5ケース/許容差/対象外分担は[共通計画§11.6](MASTERY_IMPLEMENTATION_PREPARATION.md#trial-invader-client-plan)に履歴として保全。今回の詳細/失敗/次回条件は[§11.7](MASTERY_IMPLEMENTATION_PREPARATION.md#trial-invader-client-partial-result)、[実測集計](../build/verification/trial-invader-client-20260916-201822/audit/stopped-run-analysis.json)。同じ製品229,423 bytes/150 entries/3807DECC…76DE6、Trial1.4.9/Forge47.4.0/Java17.0.15。外側modsは製品/Trial/新別補助の3本のみ。

| case | 実測・期待値との照合 | 現在判定 |
|---|---|---|
| F1 両ON | native tick1・本人targets/解除lambda1、flagtrue維持、元false setter0、Soul0/HP20/MAX20、hurt0 | **REAL CLIENT LIMITED PASS**。server/client2連続一致、2ticks/93.126ms |
| F2 GUI親OFF | M保存ON、native tick1・lambda1/false setter1、flagtrue→false、Soul0/HP20。外部免疫falseへ両側収束 | **REAL CLIENT LIMITED PASS**。2連続、3ticks/145.622ms |
| S1 GUI両ON | Small1/本人callback1/native owner、Soul0、Hurt/final Damage各1×1.5 uncancelled、HP20→18.5、flagfalse、HUD18.5/20.0 | **REAL CLIENT LIMITED PASS**。2連続、2ticks/83.491ms |
| S2 GUI M OFF | P保存ON、Small1/本人callback1、native Soul加算0.01、Hurt/Damage各1×1.5、HP18.5→17/flagfalse。停止後client/HUD17.0/20.0一致 | **server期待値成立・総合UNVERIFIED**。physical safety停止で所定の収束完了記録なし。S2-DONEを事後作成しない |
| H1 Huge | 実変換後の専用redirect観測hookは照合済み。activate/callback/Float.MAX_VALUE実到達は未実行 | **NOT RUN / UNVERIFIED**。hook準備や0攻撃を保護PASSへ数えない |

- 生成前GUIのSurvival/Normal・自然湧き/回復false、安全な閉鎖区画/退避室/source隔離、freshHP20/Soul0を確認後prepare1。P/M Lv1・credit103/spent103/unspent0・初期flagtrue1回。購入成功ではない。通常GUIの切替は製品packet/sync、observerは実ServerPlayer/LocalPlayerを独立読取。Truth/Root未取得。
- native tick2、Small2、Huge0。F1/F2はnative targets/解除lambda到達。Smallはregistered factory/Owner NBT save-load/native解決を通し、owner/directEntityとも同Invader、source `the_trial_monolith:laser_attack`、通常次元・world未登録。permit外tick/goal生成0の途中記録と各攻撃前guardあり。sealなしのため最終予定回数完了とはしない。
- Soul1e-5、HP/MAX/amount1e-3、flag/canonical/回数完全一致を維持。完了3ケースの2連続収束は100tick/壁10秒以内の実測。ただし補助には独立壁時計deadlineが欠けており次runの修正対象。Soul/flagは通常HUDに表示されないためreadonly値、目視確認ではない。
- **補助FAILと製品判定を分離**：S2直後にonGround/火/水・bubble/fallDistanceの複合guard失敗。個別値が失敗瞬間に未記録で原因未特定。次case停止→permit閉鎖/pause。cleanupも1回FAIL：Trial Invaderの`remove`はno-opで、native killは別の`setRemoved`経路。製品不具合は未確定、本人値修復・再送・guard緩和・別手段削除なし。
- **seal NOT RUN/cleanup FAIL、通常保存/終了は完了**。20:51:15.392全dimension保存、20:52:36.447Quit/PID12280終了。保存2箇所HP17/MAX20/Soul約0.01/flagfalse/P ON/M OFF/SP0・103/DeathTime0、prepare1・SEALEDなし。actor1体を残す失敗worldを保全し、再読込0/再利用なし。成功両ONや再読込PASSへ書き換えない。
- helper初回起動FAIL（callback descriptor）を保全し起動前に補助だけ修正。測定後の修正/再起動なし。製品144・旧証拠231・対象旧world等85ファイル不変、L2 §10.11本文不変。製品build/unit/check/86/49/95/60は再実行しない。
- COMPUTER USE=GUI/command/F2/pause/保存終了、AUTOMATED=prepare/native/readonly観測、HUMANゲーム入力なし。次は補助物理値診断・wall guard・own actor cleanupを直す別新runの承認待ち。追加外部artifact/仕様判断の既知不足なし。
- 自然boss/Beam描画、force/閾値死/Huge OFF/他owner/他player/全86再現、一般MobEffect敵対解除、実2-client/L2＋Trial等は今回対象外。両極意IMPLEMENTATION_PENDING/購入停止/SP保護、RC=NO、既存個別gate・可逆クラフト既知許容仕様を維持。

### Invader Monolith 1.4.9 — attack-specific integration (2026-09-15 21:11 JST)

仕様正本は[Compatibility §7](COMPATIBILITY_POLICY.md#trial-invader-149)、実物・手順・失敗/修正・配布Jar・終了の記録は[共通計画§11](MASTERY_IMPLEMENTATION_PREPARATION.md#trial-invader-result)。新しい使い捨てForge47.4.0 serverと独立fixtureを使用。購入停止/SP保護を維持。

- native Invader.tickの範囲/predicate（対象tickCount>=100）を通し、9状態×既存flag true/falseで敵対false解除だけを抑止。既存falseをtrueにせず、他の正当な解除と攻撃entityへのtrue付与を保持。
- native Small/Huge.activate/rayTrace、ordinary/force、新規Soul/実効HPとnumericを分離。正常本人のみ、各OFF・未取得・pending/不正・他player/mob・別ownerに漏れない。native生成goalとOwner UUID保存/解決後の実攻撃も確認。
- HugeのFloat.MAX_VALUEはInvader owner/本人条件/call-siteだけの例外。Root/Truth未取得・外部Soul免疫falseでもnumeric0/death0。極意OFF・別owner・同DamageTypeの別直接hurtは元numeric/死亡。直接hurtは負対照でありbeam統合件数とは区別。
- Soul追加1.1/10.1の閾値1/10対照は旧Soul.2を保護し、非保護では元soul_damage death/10側respawnを維持。Smallは通常数値5を最終LivingDamageまで通す。force側外部免疫によるgetHealthの見かけ上の不変をnumeric取消の証拠にしない。準備後の修復なし。
- 一般MobEffect敵対解除は未特定。通常Luck・自然期限・通常remove・本人OFFとmastery保存toggle非連動を確認。全buff耐性や他MOD所有権解除の抑止を宣言しない。
- 両極意のIMPLEMENTATION_PENDING購入拒否/SP不変、登録toggle/sync、通常PlayerList保存/再読込でcanonical/既存Soul/外部flag/foreign data保持。optional/version gateの早期classloadingを検査。

**実施結果：新規86/86**（native tick18、beam状態36、閾値16、別owner8、goal2、owner保存1、二者1、非Player1、直接hurt負対照1、buff1、購入/保存1）。[receipt](../build/verification/trial-invader-20260915-203400/final-invader/invader-result.json)。既存Cube **49/49**・L2 **95/95** は同じ最終配布Jar、vanilla/TaCZ各 **60/60** は最終sourceの新規専用GameTest。[全run/保存終了](../build/verification/trial-invader-20260915-203400/audit/test-summary.json)。既存ケース削除・弱化なし。

fixture compile引数とnative対象年齢の準備を修正し、途中0件FAIL3 runを保持。70/70後に閾値16を追加し86/86。すべてAUTOMATED、実client/Prism/HUD/自然boss戦はNOT RUN。旧実clientA/B/CやL2計画を再実行しない。元Cubeのnumeric維持と過去49/59/60/95は当時の対象のまま残す。


### Damage Cube 1.4.9 focused automated integration (2026-09-15)

Expected scope: [COMPATIBILITY_POLICY §7 Q1](COMPATIBILITY_POLICY.md#trial-monolith-149).
Effect eligibility: [SKILL_TREE_SPEC §9 Q2](SKILL_TREE_SPEC.md#purification-mastery-activation).
Use the approved artifact in a new disposable Forge server, with a separate reobfuscated
fixture and the product Jar. Both mastery purchase gates remain closed. Record actual
loader versions separately from the declared ranges; do not patch the external Jar.

- Real registered DamageCubeEntity.activate and native target selection, including its
  getPosition(0) sampling. Test ordinary and high-dimensional/external-immunity forced
  addition separately; assert the actual transformed call sites and one hook each.
- Both ON, each OFF, both OFF, each unacquired, malformed/unknown schema and pending.
  Registered parent-toggle packet and management command affect the next actual attack;
  canonical sync, mastery setting, ownership and SP remain correct.
- Soul 0, existing values below1, proposed additions crossing native thresholds1/10:
  preserve existing Soul, prevent new cap loss/forced death/drop/respawn for protected
  targets. Record test-only config/flags; never expect treatment of initial Soul>=1.
- Observe numeric cube_attack event amount/source/count independently of external
  immunity's HP effects. Root absent: legitimate lethal numeric damage still causes one
  ordinary death/drop. Unprotected threshold controls retain native death/respawn.
- Same cube, different players/mob/owner; two independent attacks; direct helper calls
  outside Cube reported as negative scope checks, not real attack integration.
- Normal player save/reload and existing Soul/canonical/foreign NBT/modifier retention;
  no lifecycle decision about True Root is inferred.
- Preserve existing57 vanilla and57 approved-TaCZ cases and add core regressions;
  build/foodHealingUnitTest/check. Core synthetic events are not Trial integration.
  Verify distribution metadata/Mixin/refmap/reobf and absence of external/fixture classes.
- Normal save/stop and process exit; keep failures, corrections, errors and unobserved
  client behavior in Status. No realclient/Prism in this authorized phase.

### Limited real-client follow-up — normal Cube A/B/C and save/reload LIMITED PASS

The concrete preparation/measurement procedure is maintained once in
[the joint plan §8.1–8.8](MASTERY_IMPLEMENTATION_PREPARATION.md#trial-client-preparation).
Use the unchanged final product Jar, Trial1.4.9 and Forge47.4.0, a new disposable
instance/world, and a separate verification-only one-time acquisition initializer.
Keep both mastery purchase gates closed. Planned scope: normal Cube activate once
for each of both ON / parent only OFF / mastery only OFF, independent server/client
Soul/HP/canonical observations and numeric event counts, then seal/save/reload/exit.
This is an acquired-state effect test using an explicit verification trigger, not
normal GUI purchase, natural boss attack or a rerun of the49 automatic cases.
Health NBT, effective getHealth, MAX_HEALTH and HUD denominator are distinct; use
the meanings, expected values and tolerances in the shared plan. No HP/Soul repair,
force/threshold death tests, food hold, human tick measurement or new download.
Historical first run (2026-09-15 14:52 JST; preserved): separate helper built and used with the exact product
Jar in a fresh instance/world. One preparation and one real Cube A activation.
A: Soul0→0, HP20→17, numeric3, server/actual-client/HUD agree (LIMITED PASS).
B parent-OFF/mastery-ON GUI synchronization was observed, but an already-spawned
slime killed the player before B was submitted. Agent environment-preparation
failure: doMobSpawning=false does not remove existing mobs. B/C attacks and
seal/reload are NOT RUN / UNVERIFIED. No respawn, repair, receipt reset or retry.
Normal title-exit saving and Quit Game/process exit confirmed; saved Health0/Soul0
is the interrupted state, not the planned success state.
See [shared-plan §8.9](MASTERY_IMPLEMENTATION_PREPARATION.md#trial-client-result-20260915)
and [evidence](../build/verification/trial-client-20260915-142100/audit/measurement-review.json). Preserve prior59/59 and49/49 PASS.
The above retry proposal was subsequently authorized and completed in a different
run as recorded below. The failed world/receipt/journal/helper remain unchanged;
no respawn, repair or reuse occurred.

Current execution update (**2026-09-15 16:10 JST**, run20260915-153300): **REAL CLIENT LIMITED PASS**.
Pre-generation GUI set doMobSpawning=false and naturalRegeneration=false, then
Normal Survival flat creation. A closed glass floor/walls/roof (1634 blocks) and
read-only 32-block hostile/enclosure checks prevented waiting-time approaches.
Initial fresh/HP20/MAX20/Soul0 verified without repair. One explicit prepare,
credit103/spend103, only purification/mastery Lv1, purchase readiness unchanged.
A was performed once as the new B baseline. A/B/C each used one real normal
Cube.activate: Soul0→0 /0→0.03 /0.03→0.06; HP20→17 /17→14 /14→11.
GUI/production packet synchronization, independent actual client/server canonical,
Soul/HP/MAX and HUD matched; convergence0.0632733/0.0640446/0.0891157 seconds.
Each Hurt/Damage event occurred once per case, amount3, cube_attack/Cow owner,
uncanceled. No foreign numeric damage. Parent OFF preserved mastery ON in B.
Both ON restoration did not heal Soul0.06/HP11. SEALED, normal save, exactly one
reload of this new world without prepare/hit, second save and Quit Game completed.
Skills/SP/toggles/Soul/HP and prepare1/activate3/SEALED persisted; receipt/journal
bytes unchanged. Both Data.Player and UUID.dat agree; PID13168 exited normally.
Helper-only offline compile/reobf passed; product/source/tests/build.gradle/Jar and
purchase gates unchanged. No59/49 rerun. Initial simulationDistance=4 option-range
ERROR is retained with the raw log; do not infer that all warnings are resolved.
Details and evidence are maintained once in [shared-plan §8.10](MASTERY_IMPLEMENTATION_PREPARATION.md#trial-client-safe-result-20260915)
and [measurement review](../build/verification/trial-client-20260915-153300/audit/measurement-review.json).
Computer Use handled UI/commands/F2/lifecycle; helper and read-only observers handled
automated preparation/activation/measurement. No human gameplay requested. Normal
GUI purchase, natural boss/Cube rendering, force/threshold deaths, healing-to-cap,
other attacks/versions, separate-JVM/TCP/two-client behavior remain outside this PASS.
This limited follow-up is complete; no additional feature execution is authorized.
Keep both mastery purchase gates closed and RC=NO.

The broader tests below remain planned; the focused Cube unit does not complete them.

Manual/integration:
- ordinary numeric attack
- Soul Damage numeric component
- forced-death/death-bypass component
- Root active/inactive
- Purification Mastery
- Break Realm offensive interaction
- boss death/loot/progression remains correct

---

## 20. Hyperlink / Fumetsu Wither

Manual/integration:
- direct-kill attack
- Root active/inactive
- Purification Mastery
- normal numeric damage path
- Break Realm vs supported invulnerability
- normal boss death flow

---

## 21. Truth Mastery performance

L2代表4 IDの限定実装・範囲/冪等性/保存の今回結果は[§18](#l2-2519-focused-plan)。
大規模候補数/TPS/長時間・自然歩行AIは未測定。以下の性能条件を今回PASSへ昇格しない。

- spawn mob in range
- mob walks in
- teleports in
- player moves range over mob
- leaves after strip
- chunk unload/reload
- multiple Truth Mastery players
- thousands of nearby entities stress test if feasible

Assert:
- no full 151³ every-tick scan
- idempotent processing
- no major TPS collapse

---

## 22. Multiplayer

Prepared vanilla two-client runbook: [MULTIPLAYER_MANUAL_TEST.md](MULTIPLAYER_MANUAL_TEST.md).
This is a plan, not a completed manual integration result. It keeps real authenticated client
operations separate from the existing server-side/synthetic connection tests.

Current real two-client status: **BLOCKED - SECOND MINECRAFT ACCOUNT REQUIRED**.
The user has one legitimate account; do not require another purchase or an external helper.
Separate authenticated Prism Launcher single-client dedicated runbook:
[SINGLE_CLIENT_DEDICATED_MANUAL_TEST.md](SINGLE_CLIENT_DEDICATED_MANUAL_TEST.md).
Current phase: **MANUAL SINGLE-CLIENT DEDICATED INTEGRATION TEST / STEP 1-6 / COMPLETE - PASS WITH RECORDED SCOPE AND CONNECTION HISTORY** (2026-09-05).
Recorded scope: authenticated connection/GUI sync, Root Lv1 purchase/toggle/reopen, one apple meal and
progression sync, death/respawn, disconnect/reconnect retention, and normal shutdown/final log review.
Both processes stopped normally. See `CODEX_STATUS.md` for the actual observations and canonical/log
checks, not a startup-only inference. The initial timeout and incomplete IPv4 login remain unexplained;
the wrong IPv6 endpoint refusal remains a separate historical result. No repeat manual test is requested.
Use an audited, isolated Prism instance and a legitimately authenticated Microsoft/Minecraft account,
with server `online-mode=true`. No Offline Account, authentication bypass, duplicate-account connection,
or fake-player substitution. Neither this test nor server-side automated results clear the real
two-client gate or demonstrate cross-player isolation. The existing two-player matrix below is retained.

At least two players:
- independent SP/skills
- different Root levels
- one owns Food Production, one not
- one owns Purification/Truth, one not
- one owns TaCZ skill, one not
- shared machines
- same mob
- logout/rejoin

No cross-player state leakage.

---

## 23. Break Realm Adapter expansion

Status: **TEST DESIGN SPECIFIED / IMPLEMENTATION DEFERRED UNTIL VANILLA RELEASE BLOCKERS CLEAR**

### Generic GameTest fixtures

- normal non-player entity receives ordinary damage once; Break Realm adds no fallback
- Player target is never eligible
- acquired+enabled Break Realm bypasses a fake owned invulnerability gate
- acquired+enabled Break Realm bypasses a fake owned revival/death-cancel gate
- unowned or toggle-OFF Break Realm remains blocked by those fake gates
- applied amount equals the captured legitimate final amount and is never amplified to target HP
- one hit produces exactly one successful damage transaction and fallback runs at most once
- recursion guard prevents Adapter re-entry and unbounded retry
- Pursuit/additional-hit transaction remains distinct from Break Realm fallback
- vanilla hurt-frame handling remains distinct from fake boss invulnerability
- lethal damage preserves normal death event, loot, XP, and completion hooks
- unsupported Adapter falls back to ordinary damage without forced kill
- optional target mods absent: client/dedicated-server classloading and ordinary combat succeed
- temporary Adapter-owned state is restored on success, cancellation, exception, and lethal completion where
  restoration remains applicable

### External Adapter audit matrix

Run a version-specific Jar/source/API audit before adding fixtures for:

- L2 Hostility immortal/lethal-prevention traits or affixes
- Trial Monolith
- Hyperlink / Fumetsu Wither
- Draconic Evolution / Chaos Guardian
- Bloodbath / Bloodbath Godzilla, including generated tick/hurt/death Procedures and data dependencies
- each additional official-modpack urban-legend or high-difficulty boss

For every target, test Break Realm OFF and ON, exact legitimate damage preservation, one-hit exactly-once,
normal death/loot/XP/advancement/boss completion, another Player without the skill, and optional-mod absence.
Do not reuse a result from one version or ownership mechanism for another.

### Result labels

Record the stages separately as `STATIC AUDITED`, `AUTOMATED INTEGRATION TESTED`, and
`MANUAL INTEGRATION TESTED`. Jar inspection alone must never be reported as integration-tested behavior.


## 26. Locked acquisition conditions / SuperbWarfare retention (2026-09-13)

Authoritative expectations are [SKILL_TREE_SPEC sections 9–10](SKILL_TREE_SPEC.md#9-purification-mastery--浄化の極意)
and [COMPATIBILITY_POLICY section 14](COMPATIBILITY_POLICY.md#14-superbwarfare-legacy).
Do not duplicate or invent new effects/parent-ON conditions to test these decisions.

### 購入readinessのLOCK・実行結果（2026-09-29 07:49 JST）

[共通計画§12.5](MASTERY_IMPLEMENTATION_PREPARATION.md#mastery-purchase-policy-decisions)のP=A/T=Aと最低範囲を維持。効果最低範囲は§14.55–14.57で充足済み、通常購入は[§14.58](MASTERY_IMPLEMENTATION_PREPARATION.md#mastery-production-purchase-ready)で **#4 COMPLETE**。過去の固定pending拒否結果は当時の契約として保持し、現在の期待をP100/T500・前提取得のみ・親OFF可へ更新した。

- 新購入unit **153 assertions**：optional4構成相当pure version/purchase境界、親ON/OFF、正確支出/default ON、Long、既取得NBT保持、効果時親ON条件との分離。外部4構成の実ロードではない。
- registered packet coreは既存60を削除せず、fresh603 chain/他player、P99/T499境界、P/T異常canonical、既取得者、Longの**7 caseを追加**。旧P/T固定pending caseはready購入＋拒否SP保護へ更新。
- P100/T500、親OFF、前提不足、SP不足、duplicate/stale/invalid ID・expected level、pending/future schema、異常取得map、旧所有者/toggle保持を確認。previewは無変更でserverと一致。購入成功は前提fixtureによる直接取得注入と区別し、登録SimpleChannel encode/decode/NetworkHooks→通常server transactionを使用。
- 成功/新規境界拒否ごとに送出canonical payload一致と1回同期、他player変更/同期0。client実画面操作や実TCP認証配送の証拠ではない。新たな実client必須条件は追加しない。
- TaCZだけのoptional/readiness拒否、Flight/Break Realm pendingを維持。P/Tのoptional購入方針を他nodeへ流用しない。通常購入と効果Adapter版gateは分離、既存全Adapter/Mixin不変。
- malformed親Levelは既存読込で未取得に正規化された後PREREQUISITE_MISSING。購入時のraw修復/新schema/移行仕様は変更しない。現存不正取得ID/範囲外LvはSTALE_REQUESTで無変更拒否。

**最終回帰**：[build/unit/check成功](../build/verification/mastery-purchase-20260929-073000/audit/build-02.log)、[vanilla67/67](../build/verification/mastery-purchase-20260929-073000/audit/vanilla-01.log)／[承認TaCZ67/67](../build/verification/mastery-purchase-20260929-073000/audit/tacz-01.log)。最終sourceのMC1.20.1/Forge47.2.0 userdev試験、両新規world通常保存/停止・exit0・対象Java残存0。新Jar307,233 bytes/198 entries/SHA256 C12C046E0A218E1FB159A607F09F9B681B248AB4C10D0DCE3C4650D88F12584F、[metadata/Mixin/refmap/reobf/非混入](../build/verification/mastery-purchase-20260929-073000/audit/final-jar-audit.json)確認。配布Jar直接攻撃・実client試験は0。Trial/L2/FE・#1–#3受入は再実行せず旧PASS維持。

初回Gradleはsandbox native DLL起動失敗（compile前）、権限付きlocal offline再実行で成功。期待値緩和/ケース削除なし。旧49/55/60等は当時の結果を保持。#4 COMPLETE／残5／#5未開始、#7Flight blocker未着手、RC=NO/REAL2CLIENT=BLOCKEDと他gate維持。

SuperbWarfare coverage when the exact artifact/route evidence is available:
- Dedicated formula Lv0/1/200 →1/2/201 and existing long/numeric boundaries.
- Verified direct attack/Player source only, one application, preserved composition,
  excluded routes unaffected, absent optional MOD starts without classloading failures.
- Record target ID/version/artifact hash and distinguish math/absence tests from real integration.
Missing artifact/route evidence is BLOCKED, not a failed user retention choice or a formula-only
integration PASS. No invented explosion/vehicle/owner mapping or target class.

Current purchase regression is recorded above: build/unit/check and vanilla/TaCZ67 each.
The older49/55/60 and separate13-case/restart results remain historical evidence; no extra
external attack/restart suite was run for this purchase-only change. No realclient/Prism/old-world launch.

### Superb Warfare 0.8.9.1 focused verification (2026-09-13)

Scope and formula are authoritative in Compatibility Policy §14. Preserve the pre-change
55 vanilla / 55 approved-TaCZ cases. Add the two core optional-absence/unrelated-source
GameTests without representing them as actual Superb Warfare attack tests.

- Unit: Lv0/1/200, above-int and Long.MAX_VALUE, canonical round trip, valid migration,
  unknown schema and edited pending data, no SP/raw mutation, finite/NaN/overflow/underflow.
- Separate `superbwarfareTest` source set and reobfuscated fixture Jar; never ship this
  fixture, its metadata or any external Jar inside Food Healing's distribution.
- Fresh disposable production Forge server: load the approved actual Superb Warfare,
  local Kotlin for Forge and loader-selected nested dependencies. Create its actual
  registered ProjectileEntity and call its public `performDamage`, through its source
  factories/forceHurt/vanilla hurt/Forge events. Observe source/owner, event count/amount
  and real target HP loss; never substitute handmade source tests for this coverage.
- Test fresh/migrated Lv0/1/200, normal/headshot/absolute/split damage, one factor per
  event, global/Heroics composition without TaCZ leakage, another Player, null/non-Player
  owner, above-int level, invalid pending and preserved edited-pending legacy fallback.
- Report synthetic explosion/projectile-hit/custom-explosion negatives separately.
  Calling damage processing directly does not verify gun input, ballistic collision,
  network firing/replay, mounted weapons, every weapon or realclient gameplay.
- Verify loaded versions against metadata; preserve startup errors/WARN and their scope.
  Finish with normal save/stop, logs, receipt, final artifact comparison and process checks.
- Final vanilla/TaCZ counts are 55 existing + 2 new when all execute. Existing separate
  13 TaCZ damage cases, Ammo/heat/bolt/reload/replay and distinct-JVM write/read remain
  separate suites. Do not rewrite the historic 55/55, 49/49 or 41/41 results.

<a id="fantasy-ending-uom-tests"></a>

## 27. Fantasy Ending 2.7.20 UOM — 依存結果・TimeStop設計検証（2026-09-20 21:47 JST）

現行結果は本節末尾および[共通計画§14.56](MASTERY_IMPLEMENTATION_PREPARATION.md#fe6-secondary-production-result)。以下の日付付き「次」「STOP」「NOT RUN」は当時の履歴。

現在：queue #1/#2/#3 COMPLETE、残6、#4 NOT STARTED。FE6の実装/焦点自動統合は§14.56、Dream代表実client/GUI/canonical/HP/HUD・正常終了は§14.57で限定完了。#2/既存自動suite再実行0。BAN等の実clientへ拡張しない。

**過去の初回静的調査（2026-09-20）**：決定/原物/実call-siteの参照先は[共通計画§12.5/§14](MASTERY_IMPLEMENTATION_PREPARATION.md#fantasy-ending-uom-readonly)。本体18,417,128 bytes/1,379 entries/SHA256 `E32FD4BA2E48FCF2C84F71AA07155C63D6BFA2178BB840899307DA0DA5E5D141`。**静的確認・loader smokeは効果統合PASSではない。** 承認6 artifactの一致/依存実採用を[§14.7](MASTERY_IMPLEMENTATION_PREPARATION.md#fantasy-ending-dependency-timestop-result)で確認済み。TimeStop本人限定安全方式はBLOCKED継続。製品効果fixture/製品Mixin未作成・製品build/保護効果test/clientは0。9/20 21:47〜22:26の別補助observer/台帳は専用compile/reobf・新規dedicated設計検証を実施し、下記結果と共通計画§14.11/14.12へ分離。既存133/60/49/86と全限定実client結果を維持する。

### 新規MobEffect付与6 siteの受入条件（初回未実行時の表・現行結果は末尾）

以下は初回設計時の受入表。当時はTimeStopの指定停止条件で未実装・未実行。今回の実行済み範囲と未確認は末尾/§14.56を参照し、全項目網羅PASSへ転記しない。

| ケース群 | 必要な実経路・判別 | 必ず保持/観測するもの |
|---|---|---|
| FE-P1 Dream Beam | native UOM.dreamShadowBeamのhurt成功→slowness/poisonの2実call。P有効本人は新規付与0、無効本人は元duration/amp | DS入力10+random×5とhurtの成立/HP結果、演出、付与直後と通常浄化tick後を分離 |
| FE-P2 Star周辺衝突 | native onHit、正式UOM owner、直撃とは別のAOE被害者、実random<0.5分岐のBAN_HEALING | 付与分岐が起きなかった試行を防止PASSにしない。有限反復/RNG制御はfixture限定で方法を記録し、別owner・owner自身・直撃同UUIDの除外とDS/FE numericを保持 |
| FE-P3 final | native finalSkillAttackの生存/範囲/phase分岐→BAN_HEALING | 負delta/Health/damage反復/死亡等の元処理を保全。人間の死亡試験を要求しない |
| FE-P4 反撃 | native hurt→actuallyHurt、isPoweredの実条件と攻撃者Player→HARM | HARM付与だけ抑止、元magic反射amount×0.3を保持。非powered/除外DamageSource/別attackerの比較 |
| FE-P5 UOM skull | UOM/BaseEntityの発射で実WitherSkull ownerを解決→native onHitEntity | Normal/Hard/Easy/Peaceful、hurt成功/失敗、UOM/通常Wither/他owner、元hurt入力8・heal/enchant/爆発保持 |
| Star直撃の重要な負対照 | UOM ownerは4effectをgotoで飛ばす元挙動。非UOM ownerは元4effect分岐 | WITHER/WEAKNESS/BLINDNESS/INSTANT_DAMAGEを他ownerへ残す。Star namespace全取消にしない |
| 本人条件/外部不在 | 取得不足、親OFF、極意OFF、正常/不正/pending canonical、client/別player/mob、未対応版・本体不在 | 共通controllerのserver本人条件、optional classloading、登録EntityType同一性、正式owner/null/異levelを確認。対象外の原callは元どおり |
| 既存外部state | 既存他source BAN_HEALINGや効果を持つ本人にもnative入口 | 既存effectを消去/短縮/治療しない。amp7/6の元回復縮小、rawHealth/getHealth/MAX/FE delta/syncを別観測 |
| 保存・同期・数値保全 | effect新規抑止以外のsource/owner・numeric回数/amount・death/loot/状態 | 式だけ/手作りDamageSourceだけをnative統合PASSにしない。製品Jar/元FE不変・非混入、実ロード版、途中FAIL、save/stop/processを記録 |

- 全6siteでPの本人保護述語を確認する。Truthの取得/ONを条件へ追加しない。HARMは今回承認されたsecondary新規MobEffect、元numeric反射とは別。
- 新規MobEffect未付与と、通常浄化の後消去を即時observer/元call回数で区別する。回復阻害をGUIのeffectカテゴリだけで判定しない。
- 実class/method/descriptor・reobf変換後の対象call数・外部Jar hash/versionを確認し、対象を広げるfallbackを作らない。
- 製品変更後のbuild/unit/check・関連既存回帰は承認済み。今回は製品変更前の明示停止条件に到達したため実行しない。将来実行する場合は最終コード/Jarと実件数を記録する。

### 時間停止と外部経路

**FE-P6はBLOCKED - TIME STOP SOURCE OWNERSHIP**。EndingLibrary2.1.19fixとIron's Spells1.20.1-3.16.3等は受領/loader限定PASS済み、旧ARTIFACT REQUIREDは解除。実call graphと未立証箇所は[共通計画§14.8–14.10](MASTERY_IMPLEMENTATION_PREPARATION.md#fantasy-ending-dependency-timestop-result)。最後のpacket sourceやcount>0だけで所有関係を代用しない。

安全方式を証明できた場合の必要試験は、UOM/P本人だけcanMove、無資格/他player/別source停止維持、開始/延長/終了、複数source/複数dimension、未観測開始・count直接変更/復元、途中例外、logout/death/再接続/保存相当、foreign count/flag/NBT不変、server権威のclient許可/失効。global解除・偽count・全体use取消は禁止。server mirrorを実client PASSとしない。21:07時点は全て**NOT RUN**。今回の別補助によるnative設計確認は下記「TimeStop ownership設計検証」へ分離し、製品効果PASSにはしない。

Ironのvisualは位置/距離/向き/particleのみ、FEのEasyDamageSourceはapplyDamageの非Spell分岐から元hurtへ到達する。確認範囲で追加secondaryなし。ただしEldritch native統合は未実行で、全Iron互換PASSではない。6siteだけでP最低範囲全完了にしない。

### 21:07フェーズの実行・非実行（履歴）

| 対象 | 結果 | 証拠・範囲 |
|---|---|---|
| 承認6 artifact / dependency closure | STATIC AUDITED | 全指定hash一致、FE参照EndingLib35 class提供元あり。追加必須不足なし |
| 新規Forge47.4.0 dedicated loader | 1 run / LIMITED PASS | 原ログDone・通常stop/全dimension保存/Java exit0。監視補助FAILはready文字列不一致として原receipt保全（§14.7） |
| FE6 site/TimeStop本人保護/Eldritch native | 0件 / NOT RUN | TimeStop所有関係の安全性未立証で停止。製品/fixture変更なし |
| build/unit/check、vanilla60、TaCZ60、L2133、Cube49、Invader86 | 今回再実行0 | 既存完了の件数/PASSは元対象Jarで保持、新しいPASSへ転記しない |
| FE実client/Prism | NOT RUN | 未承認。旧L2/Trial等の限定client結果は維持 |

### 実clientで追加する最小範囲

[共通計画§14.5](MASTERY_IMPLEMENTATION_PREPARATION.md#fantasy-ending-uom-readonly)の**安全性を自動で確認したDream Beam単発＋通常GUI/canonical/HP/HUD収束**を第一候補とする。新規の閉鎖world、自然湧き/自然回復OFF、本人だけprepare1回/SP0・103、Truthなし。事前HP20/MAX20/delta0/effects空をreadonly確認し、不一致をsetterで合わせない。全boss AI戦、Star AOE/反撃HARM/finalの危険な連続実clientを必須にしない。

readonly observerはserver/client canonical、source/owner、numeric/effectの順序、Health/getHealth/MAX/delta/HUDを対応付ける。通常浄化が残る陰性の瞬間effectはserverで捉え、撮れていないiconを目視PASSに補完しない。BAN_HEALING/TimeStopをDreamだけでclient PASSへ含めない。具体的負対照はnative結果と安全性が揃ってから実行承認を受ける。

不明な被弾/回復、actor暴走、想定外HP/effect、sync未収束なら次発を止め通常退出。seal/own actorの安全停止→通常Save & Quit→readonly保存/log→Quit/process終了を記録する。新しい永続stateがないこの単位では、同world再読込を自動必須とせず、TimeStopの保存条件は別途判断する。上記は当初の受入案。Dream代表の実施結果は本節末尾/共通計画§14.57、BAN等は実client未観測として分離する。下記dedicated設計・過去結果は保持。



### TimeStop ownership設計検証 — 2026-09-20 21:47 JST

以下は21:47時点の履歴。次の焦点の実施結果は後続の「server movement boundary」を参照。

**BLOCKED - TIME STOP SOURCE OWNERSHIP**。[共通計画§14.11](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-timestop-ownership-design-verification)へactive source/UNKNOWN/再接続B案・介入網羅性・20枠を一本化。[reviewed-results.json](../build/verification/uom-timestop-design-20260920-212237/audit/reviewed-results.json)が採否判定、各server原ログ/resultは消さない。

| 実行範囲 | 今回の判定 |
|---|---|
| use開始/延長/終了、UOM/FOREIGN順序と複数source、early return | native LIMITED PASS。P ALLOWは候補計算のみ、実canMove overrideは未設置 |
| direct positive/zero、実deserialize、台帳喪失、SavedData/global不一致 | native入力または明記した台帳喪失相当でUNKNOWN/DENY。loaded UOM集合だけで失われたownerを復元しない |
| setRemoved/unload/death/logout-login | 実remove理由/FOREIGN Cow死亡/PlayerList remove-placeを限定測定。全chunk eviction・UOM独自death・認証/実再接続は未確認 |
| source dimension | native APIはnull、source未removed/count80で移動不成立。raw `15-source-dimension`のPASS行は移動試験PASSへ**不採用**。入口PortalInfo分岐との整合はあるが最終原因未確定。成功したnative遷移の証拠が必要 |
| 最低20枠 | **18 native限定・1台帳喪失相当のみ・1移動未成立**。付随canonical/別player/cacheモデル込みで有効PASS assertion行28。全20/20や28 native gameplay PASSとはしない |
| server移動権威 | **canMove-only設計FAIL**。FOREIGN追加後server候補=false/native canMove=falseで`handleMovePlayer`がX+0.0625を受理。packet lease/seqだけでは逆方向のin-flight入力を止めない |
| client lease | connection/dimension/epoch/seq/revoke/expiryモデルの拒否を確認。Forge実クライアント配送/描画/入力/遅延はNOT RUN |
| 正常終了 | 新規server-01/02とも通常save-all flush→stop、全dimension保存、Java exit0。01のfixture assertion FAILは終了PASSと別に保全 |

01の生成直後UOMに対する1 tick終了assertionはfixture前提誤り。02で実TimeStopSkillGoal180→184 native ticks→0の経路を確認した。期待する最終0を緩めず、age/countを修復していない。過去の補助・worldは再利用せず、01既完了matrixの一括やり直しなし。

次の焦点：今の移動反例を負対照として、serverの移動受理境界で本人session/epoch/P/source許可を再照合する最小guard案。元native canMove=trueを奪わず、外国source追加/UNKNOWN/終了/切断後に遅延入力を受理しないこと。追加guardは今回未作成・production未接続。この方式の成立後、未到達dimension/再接続新epoch・FE goal再延長・複数source末尾tick順序を補う。既存18枠/loader/旧suite全体の再実行を次回の開始条件にしない。

`src/main`/通常test/build.gradle/配布Jar/製品Config/schema/network/purchase gate変更0。専用helper compile/reobfのみ。build/unit/check/L2133/core60/Cube49/Invader86再実行0。client/Prism0。FE6 site STATIC AUDITED/NOT IMPLEMENTED、製品FE保護統合0/NOT RUN、IMPLEMENTATION_PENDING/SP保護、RC=NO、REAL2CLIENT=BLOCKEDと既存限定PASSを維持。


### TimeStop server movement boundary — 2026-09-20 22:26 JST

以下のvehicle未被覆/次候補は22:26時点の履歴。実反例は保持し、後続のvehicle専用DENY結果へ続く。

**player handler LIMITED PASS / 全体BLOCKED（vehicle未被覆）**。[共通計画§14.12](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-server-movement-boundary-verification)が実装境界・最低17枠・停止/次単位の正本。[review](../build/verification/uom-movement-boundary-20260920-220541/audit/reviewed-results.json) / [native raw](../build/verification/uom-movement-boundary-20260920-220541/server-02/movement-result.json)。新規dedicated、MC1.20.1/Forge47.4.0＋承認6物＋不変製品＋別補助。AUTOMATED/EmbeddedChannelであり、実client/TCPではない。

| 試験の役割 | 実測・判定 |
|---|---|
| 非搭乗player最低17枠 | 全枠を限定確認。OFF/未取得/別player/FOREIGN/混在/UNKNOWN/台帳喪失相当/epoch/session/dimension/seq/revokedを拒否。current UOM/P、新Grant、native停止終了では+0.0625 |
| held stale負対照 | ALLOW中に同packetを保持→FOREIGN参加→DENY。guardなし0.5→0.5625、guardあり0.5→0.5。payload/identity保持。前者のraw PASSは反例再現成功で安全PASSではない |
| 元native権限/processing | creative/spectator/本来のnative self-source権限は各+0.0625。処理中台帳は0 |
| teleport/速度 | 正式teleport待機は旧packet無視/guard0、正しいack後新packet受理/guard1。過大速度は元補正が先に作動/guard0 |
| 拒否副作用/外部state | 26移動assertionでglobal/dim SavedData/count/capability serialized NBT/persistent NBT前後一致。拒否では位置/previous/lastGood/fall/onGround/velocity/collision/awaiting/floating/接続/HP不変。元received/time bookkeepingは別記録 |
| 未被覆vehicle | **COUNTEREXAMPLE**：FOREIGN/nativefalseでBoat2.5625→2.625、guard0。native positionRider明示1回で本人も追随。実client/通常tickの自動追随試験とは分離。playerだけのguardでは全体を保証できず指定条件で停止 |
| 補助と件数 | compile getter誤り修正、01 readonly saveのreobf不整合FAIL0件を保全、02はPASS29行＋反例1＋観測1。29 gameplay PASSではない。旧18枠や製品0件へ水増ししない |
| 正常終了 | 01/02ともsave-all flush→stop→全dimension保存→exit0。終了手続きPASSは01 fixture FAILを消さない |

**SERVER MOVEMENT BOUNDARY VERIFIED=NO / SAFE DESIGN PROVEN=NO**。次候補は`handleMoveVehicle`の限定DENY境界検証のみ。UOM/Pの車両ALLOWは未LOCK、今回はguard未実装。全packet取消/独自永続state/production protocol変更なし。実client補正、実reconnect、長時間floating/fall、他入力、source lifecycle後続は未検証として維持し、同一17枠の全再実行を開始条件にしない。

FE6siteはSTATIC AUDITED/NOT IMPLEMENTED、TimeStop production NOT IMPLEMENTED、製品FE効果integration0/NOT RUN。製品229,494 bytes/150 entries/SHA256 `5C1A716EEDE7AEB4449B36EEFFBDDA156176B16B60A1676229224AA3390DB327`不変。build/unit/check/既存suite/実client/Prismなし。両極意IMPLEMENTATION_PENDING・購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、個別開始条件と可逆クラフト増加の既知許容仕様を維持。


### TimeStop vehicle専用DENY boundary — 2026-09-20 22:50 JST

**PLAYER + VEHICLE SERVER DENY BOUNDARY VERIFIED（LIMITED）/ SAFE DESIGN PROVEN=NO**。現在の全体ownership BLOCKEDはsource lifecycle等の後続残件であり、vehicle DENY未実施ではない。正本：[共通計画§14.13](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-vehicle-deny-boundary-verification) / [判定集計](../build/verification/uom-vehicle-deny-20260920-223504/audit/reviewed-results.json)。AUTOMATED/EmbeddedChannel/native handler、新world 2 run。real reconnect/TCP/client/描画はNOT RUN。

| 対象 | 結果・証拠の意味 |
|---|---|
| held vehicle反例・guard比較 | ALLOW候補時の同packetをFOREIGN後配送。unguarded Boat2.5625→2.625、guarded2.5625のまま。payload/packet/vehicle/player/Connection identity保持。負対照を安全PASSへ変更しない |
| A–I、canonical/processing/empty/inconsistent/invalid connection | **16拒否でXYZおよび比較全state不変**。session等はtransientモデル＋実handler、認証/再接続試験ではない |
| no-stop・native true | 通常車両/creative/self-source各+0.0625。非LivingのBoat canMove=falseを変更せず、元player権限を保全。spectator車両は今回未測定 |
| vanilla拒否6件 | root本人/lastVehicle/controllingPassenger不一致/invalid numeric/速度過大はguard0で元処理。実壁への衝突はguard無介入のまま元move/補正が戻す |
| passenger | DENY後native positionRider明示1回で本人も不変。通常client/tick/renderのPASSへ拡張しない |
| player連携 | no-stop正常/current UOM-P正常/FOREIGN held拒否の3件のみ。旧17枠は当時の結果を維持 |
| 未LOCK分岐 | UOM/P vehicle方針はUNDECIDED。該当packet配送0/PASS採否0。今回の安全DENYに方針決定は不要 |
| 補助と件数 | raw32 PASS＋UNDECIDED1＋観測1。run01操縦者準備assertion FAILを保全、native player優先順序に合わせたrun02で未完了3件のみ補足。guard compiled bytes不変 |
| 外部state/終了 | vehicle20 assertion全てglobal/dim Set/count/capability/NBT前後一致。全run通常save-all flush/stop/全dimension保存/Java exit0。終了PASSとfixture FAILを分離 |

専用helperだけoffline compile/reobf、task出力もverification内に限定。製品source/test/build.gradle/Jar/Mixin/protocol6/Config/schema/gate不変。製品229,494 bytes/150 entries/SHA256 `5C1A716EEDE7AEB4449B36EEFFBDDA156176B16B60A1676229224AA3390DB327`を維持。製品FE統合0、既存build/unit/check/L2/Trial/core/loader smoke/実client再実行0。

次候補は未到達source dimension遷移のnative実到達・台帳失効/DENYのverification-only補足。real reconnect/reload/new epoch/goal再延長/複数source末尾tick/client correction/製品写像とUOM車両方針は別工程。両極意IMPLEMENTATION_PENDING・購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、他個別開始条件・既存PASS/C/seal・可逆クラフト増加の既知許容仕様を維持して今回は終了。


### TimeStop source native dimension — 2026-09-21 10:27 JST

正本：[共通計画§14.14](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-native-source-dimension-result) / [判定集計](../build/verification/uom-dimension-20260921-101349/audit/reviewed-results.json)。AUTOMATED、別helper、新規dedicated/world1回、EmbeddedChannel。player17/vehicle16/旧L2/Trial/core/実clientの再実行なし。

| 最小ケース | 実測・判定 |
|---|---|
| fresh出口なし対照 | native Forge event cancel=false→PortalInfo null→API null。旧runとは別・count0準備対照。旧§14.11当時のNOT RUNを保持 |
| 正式入口/出口 | native PortalForcer.createPortal/POI検索、実portal block刺激、native PortalInfo非null。fake dimension/setter/手動再spawnなし |
| 一般Cow単独転送 | 同UUID/new object・ID2→13、oldCHANGED_DIMENSION/lookupなし/count0、新Nether/count80/capコピー/canMove true。**GENERAL NATIVE TRANSFER CONFIRMED** |
| old/new停止と台帳 | 単独時global/A/B=false、old台帳空/ALLOWfalse、新countだけ残りB UNKNOWN。native通常movementをDENYしたとはしない |
| UOM自身 | canChangeDimensions=false、両APIはthis返却、Forge event0/newlevel不存在。**NATIVE DIMENSION TRANSFER NOT SUPPORTED**。nonnullだけではPASS不可 |
| FOREIGN転送のstale連携 | UOM-onlyで保持Grant→FOREIGN追加→Cow実転送。UOM残存/A停止/UNKNOWN/ALLOWfalse。player1・vehicle1とも既存guard各1、XYZ0、比較state/外部snapshot不変。LIMITED PASS |
| 要求したUOM消失後のstale | native移動できず入力未成立。上のFOREIGN結果はこのケースの代わりではない |
| native外部state | observer/guard外部setter/store0。count/NBTコピー・native停止変化は観測対象。save出力`{}`はnative実装がListTagを格納しないため全Setの不変証明に使わない。A/Bはnative Set.containsの実測 |
| 終了 | PID8344、10:21:14–15 save-all flush/stop/全dimension保存、10:21:16 exit0・後続不在。最終native停止なし/UOMcount0。reloadなし |

**総合SOURCE DIMENSION TRANSITION VERIFIED=NO / SAFE DESIGN PROVEN=NO / BLOCKED継続**。一般source転送の未到達とUOM固有制限は切り分け済み。外部state修復・製品source/Jar/gate変更なし。両極意IMPLEMENTATION_PENDING/購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、可逆クラフト増加の既知許容仕様を維持。

観測の限界：raw補助のsetRemovedラベルは実Entity.remove hook、native転送は直接setRemoved。CHANGED_DIMENSIONは実post-stateとruntime bytecodeで確認し、callback観測を捏造しない。全fault injection、実reconnect/reload、FE再延長、複数source末尾countdown、client補正は別工程。次候補はreal reconnect/新sessionの最小手順READ ONLY整理だけで、今回は開始しない。UOM/P vehicle方針も未決定。

上記は10:27時点の履歴。手順整理の完了は次の計画欄を参照し、当時の試験結果は変更しない。

<a id="uom-reconnect-planned-cases"></a>
### TimeStop real reconnect — READ ONLY計画（2026-09-21 10:56 JST）

以下は当時の計画履歴。手順・期待値は維持し、最新の実施結果は[共通計画§14.24](MASTERY_IMPLEMENTATION_PREPARATION.md#endinglib-login-compat-real-reconnect-result)と本節末尾を参照。過去のFAIL/NOT RUNは当時のまま保全。

**全case NOT RUN / 次回実行承認待ち。** 操作・準備・helperの作成予定・lifecycle実物・許容差・停止/終了は[共通計画§14.15](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-real-reconnect-runbook)が正本。ここは採否と証拠の対応表。今回helper/packet作成、compile、build/unit/check/既存suite、server/client/Prism/world起動は0。

対象：MC1.20.1/Forge47.4.0＋承認FE2.7.20/EndingLibrary2.1.19fix/Iron's Spells3.16.3/Curios5.14.1/GeckoLib4.8.2/Iron's Lib2.1.0＋不変製品229,494 bytes/150 entries/SHA256 `5C1A716EEDE7AEB4449B36EEFFBDDA156176B16B60A1676229224AA3390DB327`＋次回作成の別helper。新dedicated/world、新Prism instance、1正規account/実TCP/online-mode=true、最大3 login/2 reconnect。同梱重複・旧fixture・旧worldなし。

| case | 次回の限定合格条件 | 必須証拠 | 今回 |
|---|---|---|---|
| R1 | 停止なしの通常Disconnect/reconnect、same UUID/new session、空cacheでも通常歩行 | 認証済login/logout lifecycle、実参照比較、入力・server/client位置 | NOT RUN |
| R2 | prepare1・P両取得/ON、完全観測E1/knownUOM1/FOREIGN0/UNKNOWNfalseでG1一度。native本人canMove=false＋client候補bridge後に歩行受理 | 通常GUI/canonical、native開始、session/epoch/seq、S2C/cache、実movement | NOT RUN |
| R3 | 通常DisconnectでG1不可逆失効。旧listener/Grantがactive mapから消えclient cache空 | channel close→onDisconnect→LoggedOut→保存/removeの順序/thread、revoke/send/receiveを分離 | NOT RUN |
| R4 | same UUIDのnew player/listener/Connection/channel/session。途中E1はNO_GRANT/UNKNOWN/DENY、P保存同期だけで許可しない | server/client各identity、oldG1 revoked、cache空、native global/dimension受信状態 | NOT RUN |
| R5 | 旧G1が実new Connectionのplayer認可predicateで不一致・拒否 | pure guard比較1件、active mapへ旧G1 install0、packet跨session注入0 | NOT RUN |
| R6 | 旧sessionがvehicle認可の同じsession predicateでもUNKNOWN/DENY | 実new Connection比較1件、搭乗/vehicle配送0。UOM/P車両方針はUNDECIDED | NOT RUN |
| R7 | native停止完全終了、global/Set/count/stack静止5 tick、client revoke/cache空、通常歩行復帰 | native use(false)、live Set/源count、実revoke受信と位置収束 | NOT RUN |
| R8 | 完全終了後の新native E2を最初から観測しS3/new epoch/new seqでG2だけ発行 | 全入口journal、UNKNOWNfalse/known非空全UOM/P正常、G2 identity | NOT RUN |
| R9 | G2で通常歩行、G1はrevokedのままで復活0 | real input/packet/server受理/client収束、HP20/effects空 | NOT RUN |

合否の補足：

- serverの参照`==`をidentity証明に使う。UUID/entity ID/identityHashCodeの単独一致・不一致だけで合否にしない。global ledger UNKNOWNと途中参加session UNKNOWNを分けて記録する。
- client own leaseはdisconnect/level unload/dimension/login/clone/new epoch/revoke/期限切れでclear。clientの通知とserverの毎packet判定は別。server非許可をclientの表示だけでPASSにしない。
- movement許容差・収束は共通計画f。瞬間的native state/実入力/packet/補正を機械観測し、F2はその時点の画面だけの証拠。HUMAN報告・COMPUTER USE操作・AUTOMATED観測/候補介入を混同しない。
- **静的に見つかったR4停止候補**：native loginはTimeStopSkillPacketを送るがdimension同期packetは同handlerにない。new client levelのcurrentTimeStopDimensionはnullから始まる。5秒でglobal/dimensionが収束しなければ部分結果保全→通常終了。補助によるflag修復/packet補送/epoch作り直しを認めない。R4が止まれば後続NOT RUNで、R1–R9全PASSではない。
- 通常認証が必要。ローカルartifact不足は今回の[限定照合](../build/verification/uom-reconnect-plan-20260921-103304/audit/environment-inventory.json)では0。現在のaccount有効性/実接続は未確認。認証失敗・必要ファイル不足でoffline/fake login/downloadへ自動切替しない。
- 既存EmbeddedChannelの17枠/16拒否、held stale、source dimension/FOREIGN連携、旧L2/Trial/core/loaderは再実行しない。R5/R6はreal new Connection identityの差分証拠であり、旧matrixの置換ではない。vehicle ALLOW/実2-client採否は0。
- 異常時dedicatedのEscでは停止しない。次caseを止め通常Disconnect、native停止終了を安全な時点だけで行い、save-all flush/stop・client Quit・両process終了・readonly保存/hashを記録。失敗runを修復/削除しない。process/world再起動・再読込は今回の計画範囲外。

計画完成をSAFE DESIGN PROVENや製品FE統合PASSとしない。**BLOCKED - TIME STOP SOURCE OWNERSHIP / SAFE DESIGN PROVEN=NO / SOURCE DIMENSION TRANSITION VERIFIED=NO**。既存のDENY境界・一般source native転送はLIMITED完了、UOM native転送非対応。FE6site STATIC AUDITED/NOT IMPLEMENTED、TimeStop production NOT IMPLEMENTED、FE製品integration0/NOT RUN、両極意IMPLEMENTATION_PENDING・購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKEDと個別開始条件を維持。次回の範囲は共通計画iに一本化し、今回は計画完成で停止する。


<a id="uom-reconnect-startup-result"></a>
### TimeStop real reconnect — 起動段階の停止（2026-09-21 11:52 JST）

正本：[共通計画§14.16](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-real-reconnect-startup-result)、[case判定](../build/verification/uom-reconnect-20260921-112109/audit/reviewed-results.json)。今回実行は承認済み。補助専用offline compile/reobf成功・新server/world・新Prism instanceを準備したが、**FE2.7.20 MShaderInstance.getUniform:118のUniform→MUniform ClassCastExceptionでclientが初回接続前にクラッシュ**。R4の静的同期懸念まで到達していない。

| 項目 | 実結果 |
|---|---|
| 専用helper準備 | compile01の3エラーとserver01継承method観測先不備を保全。補助だけ修正、compile03/reobf成功、58,256 bytes/35 entries/D83C8A67…DDA9ED。製品build/既存suite0 |
| R1 | **NOT RUN**。S1/S2、auth/TCP/実参照比較・通常歩行未実施 |
| R2 | **NOT RUN**。prepare/UOM/E1/G1/S2C実許可0 |
| R3 | **NOT RUN**。native disconnect/不可逆失効未観測 |
| R4 | **NOT RUN**。S3なし。native global/dimensionの実同期はUNVERIFIED |
| R5 | **NOT RUN**。旧Grant/new player predicate比較0 |
| R6 | **NOT RUN**。vehicle predicate比較0、搭乗/packet0 |
| R7 | **NOT RUN**。native clean boundary/通常歩行未測定 |
| R8 | **NOT RUN**。E2/G2未発行 |
| R9 | **NOT RUN**。実入力/packet/位置収束/seal未実施 |
| 保存・終了 | server02 online0→11:44:09全保存→11:44:49 stop→11:44:50 exit0。clientは11:43:26 crash/Prism exit−1、正常Quit PASSとはしない。Java残存0、再起動/再読込0 |
| 主体・境界 | COMPUTER USE:新Prism起動と終了表示。AUTOMATED:補助build/配置/専用server/console/readonly照合。HUMAN操作0。GUI/HUD/player状態の目視PASSなし |
| 製品・外部state | source/test/build.gradle/Jar/gate不変、製品229,494 bytes/150 entries/5C1A716E…DB327。native use0・外部setter/修復0。runtime再接続のstate保全を証明したとはしない |

次候補は承認原物のshader初期化READ ONLY切り分け。外部MOD/依存版/Configの変更やshader登録skipで成功へ合わせず停止した。新artifact不足は未確定。既存L2/Trial/core/17枠/16拒否/一般source転送の結果を未実施へ戻さない。P/T購入停止/SP保護、SAFE DESIGN PROVEN=NO、SOURCE DIMENSION TRANSITION VERIFIED=NO、ownership BLOCKED、RC=NO、REAL2CLIENT=BLOCKED、その他個別開始条件・既知許容の可逆クラフト仕様を維持。


<a id="uom-shader-readonly-result"></a>
### FE shader起動失敗のREAD ONLY診断（2026-09-21 12:11 JST）

正本：[共通計画§14.17](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-shader-readonly-diagnosis)、[照合結果](../build/verification/shader-readonly-20260921-115707/reviewed-results.json)。**新しい動作試験PASSではない。** 既存原物/実Mixin export/logをread-onlyで照合した結果のみ。

| 項目 | 結果・限界 |
|---|---|
| Uniform生成・cast | **STATIC CAUSE IDENTIFIED / A**。親private parser offset240 new Uniform→list/map→constructor ModelViewMat getter→FE bridge/cast。FE public同名parser内のMUniform.makeUniformは親privateをoverrideしない |
| 実Mixin/transformer | FE Accessor、EL Accessor/RETURN inject/Uniform Accessorは旧実export+debugで適用確認。parser公開化/生成差替なし。MShaderInstance自身の実export/heapは未取得。競合・版不整合・helper原因の証拠なし |
| resources | cosmic JSON/vsh/fsh/imports存在、JSON22uniform読取済み。vec4はFE専用語彙でvanilla語彙外。cast回避だけの正常化は根拠なし。GLSL再compile/全resource PASSではない |
| artifacts | 承認6本・配置copy・製品/補助hash一致。対象class重複なし、追加必須artifactなし。loaderで選択されたnested版とmetadata宣言を区別 |
| 前runと今回 | 前runのhelperbuild成功・client起動FAIL/server通常save/stop exit0を維持。今回はclient/server/Prism/world再起動0、compile/build/test0、補助/外部/製品変更0 |
| 未到達 | R1–R9全NOT RUN、auth/TCP UNVERIFIED、prepare/UOM/native use/Grant0。shader起動FAILをTimeStop ownership実測FAILにしない |

次の1作業：Food Healingとは独立したclient互換patchの最小設計・検証条件を、別承認の下で具体化する。対象はFEのMUniform生成経路と親ShaderInstanceのprivate parserの契約不整合。今回は候補分類までで、patch作成/適用・版変更・起動・R1–R9は開始しない。 R1–R9や旧suiteの再実行は承認していない。player17/vehicle16/held stale/一般source native転送/UOM native非対応/L2133/vanilla・TaCZ60/Cube49/Invader86/限定実clientの既存結果を維持。SAFE DESIGN PROVEN=NO / SOURCE DIMENSION TRANSITION VERIFIED=NO / ownership BLOCKED、P/T IMPLEMENTATION_PENDING・購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKEDと全個別開始条件・可逆クラフトの許容仕様を維持。


<a id="fe-shader-compat-planned-tests"></a>
### FE独立shader patch — 次回検証条件（2026-09-21 12:37 JST）

**設計のみ。PATCH NOT IMPLEMENTED / 全検査NOT RUN。** exact target/descriptor・2 Mixin・constructor安全性・版/side gate・22 uniform期待表・終了手順の正本は[共通計画§14.18](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-shader-compat-design)。§14.16のcrash、§14.17の原因Aは上記の当時の記録を維持。今回は既存bytecode/metadataの読取と文書更新だけで、patch/observer/static fixtureは作成・compile・適用していない。

| 段階 | 合格条件と必須証拠 | 今回 |
|---|---|---|
| STATIC-S / package | 独立task graph・own class allowlist、FHR/FE/EL/MC/ExampleMod/fixture/observer/ shader assets混入0、原物不変、metadata/Mixin/refmap/reobf照合 | NOT RUN |
| STATIC-S / transform | ResourceLocation constructorのexact parser call総数1、redirect1、delegate init/親list初期化後・link/map/getter前。native FE bridgeとprivate fallbackのowner/name/descriptor、constructor frame正常。実FE/EL関連Mixin併用時も成立 | NOT RUN |
| STATIC-S / 対象外 | 親private parser/link/getter等の元method命令を保全。非対象型/resourceで元private parser1回のみ。親class全体の同hashを要求する意味ではない | NOT RUN |
| STATIC-S / gate・異常 | absent/unsupported FE・別原物hash・dedicated・対象外MC/Forgeでは両Mixin無適用/patch由来byte差分0・禁止classload0。対象profileでcall0/2・競合・bridge不在・半適用は失敗を検出し停止 | NOT RUN |
| STATIC-S / resource | 原cosmic22件のname/type/count/valuesとnative factory/UniformType/carrier/flush対応。vec4をintへ誤変換しない。これはGL動作PASSではない | NOT RUN |
| STARTUP-S1 | 新instanceでtitle到達、cast failure0、cosmic等5登録consumer完走、compile/link成功、ModelViewMat取得成功、22件型/cacheとactive list/map/getter同参照・GL型照合。他shader新規ERROR0、実ロード版/hash記録 | NOT RUN |
| VISUAL-V1 | 別検証Screenの実FE ingot→native cosmic GUI描画。3 frameのdraw/bind/apply/flush・有限uniform更新・time進行・GL error/render exception0、画面上のmask/item描画。描画OFFやshader skipはFAIL | NOT RUN |
| 通常終了・成果物 | Screen→title→Quit、終了log/該当process不在、原物/製品/helper/配置hash不変。worldなしのためSave & Quit/再読込は非該当。crashを正常終了へ転記しない | NOT RUN |

観測はreadonly：生成直後のJSON初期cacheとnative apply/描画後の値を分離し、location=-1・最適化でinactiveなuniformは元のwarning/nullを保持する。22個すべてをmapへ強制登録しない。GL型・base vanilla type整数・UniformType・carrierは別欄。値/location/Config修正、例外握り潰し、GL errorの無記録消費、画像から未観測の内部値を推定することは禁止。GL errorの観測経路が不足する場合はUNVERIFIEDとして停止する。

実施主体は将来の証拠へ明記する：AUTOMATEDはstatic transform/readonly snapshotとframe観測、COMPUTER USEは利用可能な画面操作/撮影、HUMANは実際に報告された操作/見た目だけ。今回これらの起動・画面観測は0。人間のtick計測・boss戦・死亡・食事長押しを要求しない。

STOP条件は版/hash/構造不一致、Mixin競合、getter/cast/compile/link失敗、native対象描画へ未到達、NaN/Infinity/GL error/render thread exception、新規shader ERROR、観測不足。成功結果を作るためのrepairや別版/Config切替はしない。可能な通常Quitまで行い失敗logも保全する。

**STATIC-S→STARTUP-S1→VISUAL-V1の順序で個別採否。** ゲーム起動まで承認された範囲だけ実施する。両実client gateが成立してもR1–R9自動開始はしない。既存reconnect helperをそのまま使う新runの別承認・§14.15手順が必要。認証/TCP/R4同期・TimeStop ownershipの実証をshader検査で代替しない。

追加artifact不要、既存MC1.20.1/Forge47.4.0/承認FE2.7.20とEL2.1.19fix等を維持。厳密なEL版gateは設計へ追加しない。製品source/test/build.gradle/Jar/Config/購入gate、FE/EL原物、完成済みhelperは変更0。SAFE DESIGN PROVEN=NO、SOURCE DIMENSION TRANSITION VERIFIED=NO、ownership BLOCKED、R1–R9 NOT RUN、P/T IMPLEMENTATION_PENDING/購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、個別開始条件・可逆クラフト許容仕様・全既存限定PASSを維持。

<a id="fe-shader-compat-execution-tests"></a>
### FE独立shader patch — 実施結果（2026-09-21 13:38 JST）

実装・観測の正本は[共通計画§14.19](MASTERY_IMPLEMENTATION_PREPARATION.md#fe-shader-compat-execution-result)、[総合証拠](../build/verification/fe-uniform-compat-20260921-125321/audit/reviewed-results.json)。12:37の計画/NOT RUNと11:52のcrashは当時の履歴として保持し、現在の採否は本項。

| 段階 | 実結果・証拠 |
|---|---|
| STATIC-S package/task | **PASS**。独立7 taskのみ、patch7 own class/16 entries、別observer11 class/19 entries。製品/FE/EL/MC class・assets・nested・fixture・ExampleMod混入0。refmap/reobf検査、製品build/unit/check・旧suite0 |
| STATIC-S transform | **PASS**。plain54、実FE/EL併用71、external-only32、最終observer併用42 checks。actual MC/FE bytes＋Mixin0.8.5、exact parser1/redirect1/native bridge1/private fallback1、constructor/frame/順序/対象外元命令を確認 |
| STATIC-S gate/negative | **PASS**。非対象7profile各34checksで両Mixinfalse/対象差分0。実entrypoint/pluginのFE/client非読込。call0/2・descriptor・bridge不在・half-applyを拒否、実同priority競合31checksで停止。ゲームのabsent/dedicated起動PASSには拡張しない |
| uniform22 | **STATIC / RUNTIMEとも22/22 PASS**。matrix2/vec4 12/float7/int1、native subtype/UniformType/carrier/baseType/cache/count、active GL型/list-map-getter同参照。全22 active、inactive強制登録0。5shader activeも一致、cosmic_2配列GL名は原GLSLと照合 |
| STARTUP-S1 | **PASS**。5shader consumer/link完走、ModelViewMat getter、title、cast failure0、startup GL20回全0。actual gate CLIENT/1.20.1/47.4.0/承認FE2.7.20hash/active=true。元warningを維持 |
| VISUAL-V1 | **PASS（cosmic GUI限定）**。実ingot通常描画、3sampleのnative renderItem/apply/flush到達、bind242、time1787.71→1798.70→1808.95、finite/type/identity一致。全GL照会38回0、render-thread例外/新規shaderERROR0。item/mask画面確認 |
| 終了・境界 | **PASS**。Screen→title→13:31:27通常Quit、Prism exit0、PID18056終了。world/server/接続0でSave & Quit/再読込非該当。製品229,494bytes/150entries/5C1A716E…DB327、source/build146保全件・6原物・helper・配置9 Jar不変 |

- COMPUTER USE：Prism/新instance、警告表示、title/専用Screen/通常Quit・撮影。AUTOMATED：独立offline build/static/readonly frame・GL・hash・process。HUMAN操作0。native drawの3sampleは3回だけ描画したという意味ではない。
- patch/observerのpack.mcmeta欠落WARNは今回の新規警告、旧FE bladeモデルERROR等は既存。同名shader全体ERROR0や全資源正常を宣言しない。cosmic_2 Sampler2 native警告も保持。全GL errorは取得事実・値込みで保存。
- 準備中compile/fixture/observerの失敗logと補助内修正は§14.19eに記録。期待値・version gate緩和、製品修正、ゲーム値修復、runtime再試行は0。
- 未検証：cosmic_2全用途、全FE item/boss/camera/shader/GPU、TimeStop/実reconnect。既存L2/Trial/coreのPASSは維持し再実行0。

**次の1作業：別の明示承認後、新runで既存§14.15のR1–R9 real reconnect確認を行う。** shader起動/限定GUI描画gateは今回充足。完成済みreconnect helperを作り直さず、旧instance/worldは再利用しない。追加artifact不要。今回の承認はここで終了し、dedicated接続・認証/TCP reconnect・UOM/TimeStop・prepare/Grantは開始しない。

FE6 MobEffect site STATIC AUDITED / NOT IMPLEMENTED、TimeStop production NOT IMPLEMENTED、FE production integration 0 / NOT RUN、P/T IMPLEMENTATION_PENDING・購入停止/SP保護、SAFE DESIGN PROVEN=NO、SOURCE DIMENSION TRANSITION VERIFIED=NO、BLOCKED - TIME STOP SOURCE OWNERSHIP、RC=NO、REAL2CLIENT=BLOCKEDを維持。 shader結果で購入readinessやTimeStop ownershipを昇格しない。既存個別開始条件・可逆クラフトの許容仕様は維持。

<a id="uom-reconnect-preflight-result"></a>
### UOM real reconnect — 新run適用不可で起動前STOP（2026-09-21 14:02 JST）

今回実行は承認済み。ただし指定停止条件「既存外部設定で新runを受け付けずsource変更が必要」に該当。[共通計画§14.20](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-reconnect-preflight-hardcoded-root) / [実Jar根拠](../build/verification/uom-reconnect-20260921-135926/audit/preflight-result.json)。記録ID20260921-135926はaudit-onlyで、server/world/instance/run開始ではない。

| case | 今回の判定 |
|---|---|
| R1 | **NOT RUN**（auth/TCP・S1/S2・movement未到達） |
| R2 | **NOT RUN**（prepare/UOM/E1/G1未到達） |
| R3 | **NOT RUN**（logout/revoke/cache clear未到達） |
| R4 | **NOT RUN**（S3/native global/dimension収束未到達） |
| R5 | **NOT RUN**（old G1/new Connection比較未到達） |
| R6 | **NOT RUN**（vehicle session predicate未到達） |
| R7 | **NOT RUN**（native終了/clean/revoke未到達） |
| R8 | **NOT RUN**（E2/G2未到達） |
| R9 | **NOT RUN**（movement/seal未到達） |

- 原物9 Jar（helper/patch/FHR＋外部6本）hash一致はREAD ONLY照合であり、今回の実ロード/統合PASSではない。旧shader STATIC-S/STARTUP-S1/VISUAL-V1は§14.19の結果を維持し再実行0。
- Audit.RUNは旧IDのcompile-time constant、ROOTは旧絶対パス、started guardは旧server/worldを要求。S2C/client/sessionにもldc埋込み。外部設定なし。無改変Jarで新runへ移せず、旧path/receipt/journalを変更して迂回しない。
- auth/TCP UNVERIFIED（試行0）。helper/patch/source/製品/test/build.gradle/Config/購入gate変更0、compile/reobf/既存suite0、game/server/Prism起動0、外部state変更0。保存・終了は非該当、過去exit0を今回へ転記しない。
- 現行表の旧「shader作成/検証/起動は次回承認待ち」を§14.19完了へ訂正。過去§14.18 NOT RUN、§14.16 crash、既存限定PASSは保持。

**次の1作業：reconnect helperのRUN/ROOT外部設定化と初回書込前の新run限定検証について、最小修正・専用offline compile/reobfの別承認を受ける。** R1–R9の実行自体は今回承認済みだが、利用者指定の「既存設定で新root不可ならsource変更せずSTOP」に該当した。旧helper/Jar/runは保全し、新root側だけで修正する候補。session/Grant/lifecycle設計、製品、shader patchは変更しない。追加外部artifact不要。

FE6 site STATIC AUDITED / NOT IMPLEMENTED、TimeStop production NOT IMPLEMENTED、FE production integration 0 / NOT RUN、両極意IMPLEMENTATION_PENDING・購入停止/SP保護、SAFE DESIGN PROVEN=NO、SOURCE DIMENSION TRANSITION VERIFIED=NO、BLOCKED - TIME STOP SOURCE OWNERSHIP、RC=NO、REAL2CLIENT=BLOCKEDを維持。 残lifecycle境界は共通計画§14.20c、既存の個別開始条件と可逆クラフト許容仕様を維持。


<a id="uom-reconnect-portable-helper-r1-tests"></a>
### UOM real reconnect — 新helper gate完了・R1距離FAIL（2026-09-21 14:40 JST）

**今回：新reconnect helperのRUN/ROOT設定化・限定offline compile/reobf・初回write guard検査を完了。実認証/TCPのS1→S2を実測したが、R1歩行2.796093 blocksが上限2を超えFAILで停止。R2–R9は各NOT RUN**。same UUID/new player・listener・Connection・channelと空cache/Grantなしは確認。停止後約0.303秒のsampleでclient/server距離0、HP20/MAX20/effects空を保持。操作案内が距離上限を具体化していなかった測定準備上の不足であり、製品不具合の証拠ではない。期待値・receiptを変えず、prepare/UOM/native use/Grant0のまま通常Disconnect→保存/stop・exit0→Quit/PID終了・readonly保存照合を完了。[共通計画§14.21](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-reconnect-portable-helper-r1-stop) / [今回の証拠](../build/verification/uom-reconnect-20260921-141541/audit/reviewed-results.json)。製品・旧helper・shader patch・承認6原物不変。SAFE DESIGN PROVEN=NO / ownership BLOCKED継続。

| 項目 | 今回の採否 |
|---|---|
| helper限定build/static/初回write | PASS。compile/reobf1回・repair0・製品task0。負条件7件hard fail、server/client正条件一致。旧RUN/ROOT runtime定数0、非混入確認 |
| 起動/auth/TCP・S1/S2 | LIMITED VERIFIED。title/patch gate適用・cast failure0、online認証同UUID/new実参照、Grantなし/cache空 |
| R1 | **FAIL**。最終水平2.796093109855154>2。停止後約0.303秒でclient/server距離0、HP20維持でも距離条件は不成立 |
| R2 / R3 / R4 | 各NOT RUN。prepare/E1/G1/S3なし。通常session logoutをG1 revoke試験へ転記しない |
| R5 / R6 / R7 / R8 / R9 | 各NOT RUN。oldGrant比較/native終了/newE2/G2/seal未到達 |
| 保存/終了 | 通常Disconnect、全dimension保存、server exit0、GUI Quit/Stopping・両PID不在。readonly NBT HP20/effects空/未取得/SP0/0/pending0一致。再読込0 |

HUMAN歩行1操作。瞬間Wで移動しなかったCOMPUTER USEからの引継ぎ時、Codexの案内が上限を具体化しなかった準備不足。失敗receipt/測定を保持し、歩行の途中区間だけでPASSにしない。製品/旧helper/patch/6原物・購入gate不変、EndingLibrary外部state修復なし。詳細・個別R1–R9採否・hash・実施主体は共通計画§14.21へ集約。

**次の1作業：距離上限を人間の押下時間に依存させない新run用の短い歩行区画（物理的な停止位置を0.05–2 blocks内に配置）を具体化し、別の明示承認後に未完了R1–R9を実行する。** 今回の失敗run/receiptは保全し再利用・リセットしない。helperの設定化/初回write guardは完了済みで再設計不要。新run identity/version/manifestの差替と必要なhelper限定buildだけを行う候補。R1条件、session/Grant/ownership設計、製品・shader patchを変更せず、追加外部artifact不要。

既存shader/Trial/L2/core等の再実行0。SAFE DESIGN PROVEN=NO・ownership BLOCKED、P/T IMPLEMENTATION_PENDING/購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、他個別gate・可逆クラフト許容仕様を維持。

### UOM real reconnect — 短距離区画とR4 native同期STOP（2026-09-21 15:21 JST）

[共通計画§14.22](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-reconnect-bounded-walk-native-sync-stop) / [判定済み証拠](../build/verification/uom-reconnect-20260921-145046/audit/reviewed-results.json)が今回結果の正本。新run20260921-145046、製品/期待値/Grant設計は不変。

| case | 今回結果 |
|---|---|
| R1 | **REAL CLIENT LIMITED PASS**。物理最大1.216552493、HUMAN W実移動1.200027956、両側位置差0。S1→S2同UUID/new実参照 |
| R2 | **REAL CLIENT LIMITED PASS（別補助候補）**。物理最大1.415395418、HUMAN S実移動1.400037433、両側差0。prepare1/G1 epoch1・sequence1/P正常・native server canMove=false、client候補bridgeと実server境界で成立 |
| R3 | **REAL CLIENT LIMITED PASS**。通常Disconnect、G1不可逆失効、client cache消去、native player保存/除去 |
| R4 | **STOP / native同期FAIL**。S3/new参照・空Grant/P保存同期は成立。client global=true/dimension=false/nullが5秒を超え、退出まで45.914366秒継続。SkillPacket1/dimension packet0。UNKNOWNを修復せず拒否維持 |
| R5 | **NOT RUN**（R4停止分岐） |
| R6 | **NOT RUN**（R4停止分岐） |
| R7 | **NOT RUN**（退出後cleanupはcase代替でない） |
| R8 | **NOT RUN**（E2/G2なし） |
| R9 | **NOT RUN**（G2移動/sealなし） |

通常Disconnect→online0 native終了→forceload解除→save-all flush/stop・exit0、client Quit/両PID終了、readonly保存P/M各1/両ON/SP0・103/HP20を確認。製品/原物/旧helper/patch/hash不変。R2のSキー状態はobserver対象外で、HUMAN報告・位置/packetを証拠とし、撮影していない入力表示を補完しない。EndingLibrary state直接修復0、dimension補送0、再prepare/receipt削除/旧world再利用0。詳細を本節へ重複複製しない。

**次の1作業：R4の保存済みnative同期証拠と承認EndingLibraryのlogin経路をREAD ONLYで突き合わせ、globalだけが届きdimensionが収束しない条件、および安全に対応可能な最小範囲を整理する。** R1〜R3・旧suiteは再実行しない。今回runは停止・保存終了済みとして保全し、receipt reset/再prepare/再入場をしない。補助からdimension packetを補送、外部flagを修復、旧epochを再開してPASSへ合わせない。新artifact不足は確認されていない。実装・再試験・製品変更は別承認。

SAFE DESIGN PROVEN=NO / ownership BLOCKED、P/T購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKEDと既存個別gateを維持。今回の結果を製品TimeStop保護PASSへ転記しない。


### R4 READ ONLY診断・独立login同期compatの検証候補（2026-09-21 15:44 JST）

[共通計画§14.23](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-r4-native-login-sync-diagnosis)が今回の原因・全send site・field書込・時系列・候補の正本。**原因A＋補足Bを静的/保存証拠で確定しただけで、新しい試験PASSではない**。R1–R3限定PASS、R4 native同期STOP、R5–R9各NOT RUNを維持。helper/shader packet干渉なし。旧§14.20固定ROOTや認証UNVERIFIEDは現行停止理由から除外し、過去履歴は保全。

以下は候補を承認後に限る検証要件で、今回はすべて**NOT RUN**。patch/helper未作成、compile/build/unit/check/GameTest・ゲーム/server/Prism起動・packet補送0。

| 追加確認する契約 | 合格条件（既存R1–R9の期待値は変更しない） |
|---|---|
| exact hook / optional gate | 正しいLoggedIn overloadのnative Skill send直後だけ。native例外/不在/別版は補完0、client classをdedicated共通入口でロードしない |
| state/recipient/冪等性 | 同じserver threadのlive Setから本人dimensionだけ、joining ConnectionへDimension add1。global/Set/count/capability/原Jar不変、他player配送0、同値二重通知でもnative開始や延長0 |
| lifecycle / source | 再loginごと新参照へ通知、旧参照再利用なし。foreign/非UOMでもnative Setを尊重しGrantと独立。本人非停止dimension/停止なしは補完0、複数dimensionは他要素不変 |
| 順序・終了競合 | ClientboundLogin/level ready→native Skill→Dimension handle0の実順序、null-levelなし。終了直前/直後や切断時に遅延addで停止を復活させない。矛盾stateは修復せず停止 |
| 後続real R4 | 別の明示実行承認後、native global/dimensionの既存5秒収束条件・new S3/NO_GRANT/G1 revokedを同時に満たす。R5–R9へはR4成立時だけ。今回旧runに補送/再入場しない |

既存player17/vehicle16/held stale/source dimension/shader/L2133/core60/Cube49/Invader86/限定実clientは再実行0。新compat検証をそれらの再承認や再計画に置換しない。製品FE6/TimeStopは未実装、SAFE DESIGN PROVEN=NO / SOURCE DIMENSION TRANSITION VERIFIED=NO / ownership BLOCKED / P/T購入停止・SP保護 / RC=NO / REAL2CLIENT=BLOCKEDを維持。

**次の1作業：§14.23の「native loginのglobal送信直後、既存dimension stateを本人へ同期する独立compat候補」の実装・限定検証について利用者承認を受ける。** 現在は設計のみで、patch/helper作成・compile・packet補送・ゲーム起動・R1–R9再試験は開始しない。既存artifactで着手可能、新規外部artifact不要。native state、session/Grant/UNKNOWN、P/T仕様・製品・shader patchを変更せず、R1–R3/既存suiteを未実施へ戻さない。R4以降の実client実行は別途明示承認された範囲に限る。


### 独立login dimension同期compat・real reconnect R4–R9の実施結果（2026-09-21 17:06 JST）

[共通計画§14.24](MASTERY_IMPLEMENTATION_PREPARATION.md#endinglib-login-compat-real-reconnect-result)へexact hook・artifact hash・repair履歴・packet時系列・観測限界を集約。[実測集計](../build/verification/uom-reconnect-20260921-162556/audit/reviewed-results.json) / [R4全条件](../build/verification/uom-reconnect-20260921-162556/audit/R4-readonly-verdict.json)。この結果は独立compatとverification-only候補の限定確認で、製品TimeStop保護/SAFE DESIGNのPASSではない。

| 段階 | 今回の結果 |
|---|---|
| STATIC | **PASS**。対象版/hash/raw shape限定、native call1・注入1、call0/2/descriptor違い拒否、absent/version/hash非対象の変換0、own classのみ/外部state書込0/use0 |
| 独立dedicated A–J | **10/10 PASS**。実native login/EmbeddedChannel、Food Healing不在。global/dimension条件、recipient、foreign、複数Set入力、new login、native end、state不変を確認。real client/auth/TCPの代替ではない |
| 新run R1 / R2 / R3 | 各 **SETUP ONLY**。S1→S2、prepare1・E1/G1、通常Disconnect/G1失効。既存R1–R3 PASSの再加算なし |
| R4 | **REAL CLIENT LIMITED PASS（独立compat込み）**。同UUID/new S3、旧G1失効、NO_GRANT/UNKNOWN、Skill→Dimension各1・handler RETURN1/level非null、0.280956秒でnative global/dimension収束、位置不変/入力0 |
| R5 / R6 | 各 **LIMITED PASS**。旧G1をnew player/common vehicle session predicateへ1回照合しDENY。実vehicle移動/packet replayは未実施 |
| R7 | **REAL CLIENT LIMITED PASS**。native E1完全終了・clean≥5tick・cache空、通常W歩行1.400193485、位置一致 |
| R8 | **LIMITED PASS**。native新E2、S3/epoch2/sequence2のG2発行、native/lease同期成立 |
| R9 | **REAL CLIENT LIMITED PASS**。G2で通常S歩行1.400477775、server境界受理/位置一致、旧G1失効保持 |
| 終了・保存 | **完了**。native終了/revoke/seal、prepare1/start2/end2、通常Disconnect/save-all flush/stop・全dimension保存/exit0、Quit・両PID不在。readonly保存P/M1/両ON/SP0・103/HP20/effects空。再読込なし |

歩行最大値はgeometryごとに2未満、手動押下時間で調整せず衝突壁で制限。4caseのsampled位置収束は全て0.05block/2秒基準内、HP20/effects空。HUMAN=歩行、COMPUTER USE=GUI/接続/退出、AUTOMATED=補助/native trigger/readonly証拠。既存server suiteと過去実clientを再実行しない。161503の環境準備失敗/STATIC途中FAILも保全、期待値の緩和なし。

**次の1作業：今回完了したR4–R9を除いたTimeStop lifecycle残件を、既存証拠に基づくREAD ONLY整理で優先付けし、次の最小検証1単位と必要承認を具体化する。** process/world reload、転送fault/observer欠落、FE goal途中再延長、複数source terminal countdown、client input/render/correction全般、製品設計への写像を区別する。今回は推薦までで終了し、新run/helper/build/ゲーム・製品実装を自動開始しない。R1–R9・旧suiteの再証明は不要。追加外部artifact不足は今回確認されていない。

製品229,494 bytes/150 entries/5C1A716E…90DB327・原物不変、P/T購入停止/SP保護、SAFE DESIGN PROVEN=NO / ownership BLOCKED / SOURCE DIMENSION TRANSITION VERIFIED=NO / RC=NO / REAL2CLIENT=BLOCKEDを維持。FE6/TimeStop製品実装・購入解放へ進まない。


### TimeStop lifecycle D1の過去計画（2026-09-21 21:31 JST時点・現在結果は末尾/共通計画§14.26）

[共通計画§14.25](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-lifecycle-priority-terminal-plan)にA–Gの優先関係と実行条件を集約し、重複したrunbookを作らない。次候補は2 UOMの同dimension・非同時terminal countdown→最後のsource終了だけ。新dedicated/world 1 run、実client/HUMAN不要、追加外部artifact不足なし。

| 受入の焦点 | 次回の条件（今回の試験結果ではない） |
|---|---|
| 先行sourceの自然終了 | native 1→0→use(false)を観測。残UOM>0の停止/帰属・同epoch/Grant保護を継続し、未知sourceの消失として扱わない。処理途中は追加許可不要 |
| 最後のsource/失効 | 最後のnative終了、global=false/Set空/全count0/stack0、通常評価でGrant失効、clean5tick。新epoch再発行・歩行は追加しない |
| 判定の分離 | 不正ALLOW/早すぎるquietは安全性FAIL。正常な既知終了による持続UNKNOWNは機能継続FAIL（安全側DENY）。入力/観測未成立はUNVERIFIED。期待値緩和/台帳修復なしで停止 |
| 終了 | 通常保存/stop/全dimension保存/process終了、readonly保存照合。失敗cleanupは自然終了PASSと別記録。再読込/client起動0 |

**次の1作業：別の実行承認後、[共通計画§14.25 D1](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-lifecycle-priority-terminal-plan)の2 UOM順次terminal countdownだけを、新規dedicated/world 1 runで検証する。** 新root内の別helper・専用offline compile/reobf/非混入確認→native開始各1回/通常tickのsource 2→1→0・Grant失効/clean境界→正常保存/stop/process確認→3文書更新を承認候補とする。今回は設計で停止。UNKNOWN/session/Grant設計を変更する修正、real client、他lifecycle、旧suite/R1–R9再実行、製品実装/購入解放は含めない。UOM/P vehicle方針は利用者未LOCKのまま、D1の前提にしない。

D1成功でもA/B/C/E/F/G、Dの同tick/逆順/FOREIGN/複数dimension等は未証明。R1–R9/player17/vehicle16/held stale/shader/L2133/core60/Cube49/Invader86/migration/旧worldは再実行しない。SAFE DESIGN PROVEN=NO / SOURCE DIMENSION TRANSITION VERIFIED=NO / ownership BLOCKED、FE6/TimeStop製品未実装、P/T IMPLEMENTATION_PENDING・購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、UOM/P vehicle未LOCKを維持。


### TimeStop D1実行結果 — 2026-09-21 21:59 JST（現行）

正本の結果詳細は[共通計画§14.26](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-terminal-d1-result)、[独立証拠照合](../build/verification/uom-terminal-20260921-214412/audit/reviewed-results.json)。旧D1計画は履歴として保持し、その実行承認待ちを現在状態へ戻さない。

| 項目 | 今回結果 |
|---|---|
| scope/準備 | 新run20260921-214412、dedicated/world1、epoch1、AUTOMATEDのみ。fresh P/M1ON・credit/spent103/SP0・HP20、prepare1。native40/80開始各1、既知source2・Grant epoch1/sequence1成立 |
| U1自然terminal | 1→0→use(false) HEAD/RETURNを別観測。U2残41→40・native停止継続/epoch不変だがUNKNOWNが残りEND134でGrant失効。**機能継続FAIL / SAFE-SIDE DENY**、安全性の誤ALLOWとは区別 |
| U2最後の自然終了/clean5tick | 最初の本質的FAILで続行中止、**UNVERIFIED**。cleanupのcount0を自然terminal PASSへ流用しない。sealなし |
| 終了/保存 | U2native cleanup1。globalfalse/Set空/count0,0/stack0。PlayerList remove→save-all flush→stop/全dimension保存/exit0/PID終了。readonly P/M/SP/Health20保持・Grant永続キーなし、再読込なし |
| build/保全 | helper-only compile/reobf成功、機械的repair1回（測定前）、測定後helper修正/再試験0。candidate5source byte同一、製品等413hash不変。製品229,494 bytes/150 entries/5C1A716E…90DB327 |

**次の1作業：D1の保存済み証拠を基に、既知sourceのnative count1→0からuse(false)までを帰属できる最小変更候補をREAD ONLYで設計する。** 未観測の消失・direct変更・FOREIGNを引き続き拒否できる条件を比較し、UNKNOWNの一律解除やGrant再発行で回避しない。今回は機能継続FAILを保全して終了。[§14.26](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-terminal-d1-result)。設計変更の実装・新run再試験・A/B/C/E/F/G・製品写像へは自動で進まない。追加外部artifact不足なし、UOM/P vehicleは未LOCK。

SAFE DESIGN PROVEN=NO / SOURCE DIMENSION TRANSITION VERIFIED=NO / ownership BLOCKED、FE6/TimeStop製品未実装、P/T IMPLEMENTATION_PENDING・購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、vehicle方針未LOCKを維持。R1–R9/compat10/player17/vehicle16/held stale/shader/L2133/core60/Cube49/Invader86/migration/旧実clientの再試験0。その他gateと可逆クラフト許容仕様を維持。


### D1-W次回候補 — 2026-09-21 22:29 JST（READ ONLY設計のみ・実行未承認）

[共通計画§14.27](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-terminal-witness-design)を唯一の詳細設計とする。A/B簡易除外は由来を証明できないため不採用、C exact native terminal witnessを第一候補とする。§14.26のD1 FAIL/未検証/通常終了、原run/source/Jar/証拠は変更しない。

| 必要な受入点 | 次回の検証候補・今回の結果ではない |
|---|---|
| STATIC | 実merged callback/call-site/catchの各1一致、元native callを1回delegate、ref binding/one-shot/fault guard、製品/原Jar非変更。曖昧なtargetや観測欠落のfallbackを認めない |
| W1 | native40/80→U1正常terminalで同Grant/epoch維持→U2最終terminal・通常失効・clean5tick。missing-sourceとlive空の両境界を確認 |
| W2 | known sourceのdirect0でDENY→その後のunexpected use(false)でも回復/再grantなし。native negative。後半を単独unexpected拒否の証明にしない |
| W3 | FOREIGN参加状態でUOMの正規native terminalが起きてもwitness免除なし・DENY。新しい例外と全source照合の組合せ |
| W4 | witness作成後の限定throwを元native catchが処理しても、fault latch/UNKNOWN/DENY・正常commitなし。native値の修復なし |
| 有限契約negative | 独立したunexpected入口、同UUID別ref、stale context、未消費/重複、partial callback/既存UNKNOWN。matcherの実guardを分岐代表で確認し、モデル結果を実world統合PASSへ転記しない |
| 分離/終了 | 計4新規dedicated process/world上限、faultをresetして使い回さない。各結果→区別したcleanup→通常remove/save-all flush/stop/全保存/exit。readonly保存、再読込/real client/HUMANなし |

originはbeforeUseで即削除せず、native RETURNを確認しcallback完了までcommit待ちとする。witness pending中は追加permissionなし。例外/欠落時はUNKNOWNをnative quietで消さない。source/level/server実ref・epoch・exact callback/一回tokenをANDし、既存session/Grant/guard/leaseは緩めない。Grant/epoch/sequence再発行、外部state修復、witness永続化なし。

**次の1作業：別の明示承認後、[§14.27のD1-W](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-terminal-witness-design)だけを、新しいverification helperへ実装・限定offline compile/reobf・静的配線確認・有限negative契約確認・新規dedicated検証へ進める候補。** 正常2→1→0、direct0、FOREIGN、terminal内例外を互いに汚染しない4 process/worldで確認する設計。unexpected use(false)/同UUID別ref/欠落・重複等は限定契約検査と対応native照合で分ける。今回は設計で停止し、実行は未承認。旧D1や既存PASSを再実行せず、製品写像・他lifecycle・購入解放へ広げない。追加外部artifact不足なし、UOM/P vehicle未LOCK。

今回は新run/helper変更/compile/build/ゲーム0、文書以外の製品・既存補助・artifact不変。SAFE DESIGN PROVEN=NO、SOURCE DIMENSION TRANSITION VERIFIED=NO、ownership BLOCKED、FE6/TimeStop製品未実装、P/T IMPLEMENTATION_PENDING・購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、vehicle未LOCKと全個別gateを維持。逆順/同tick/3source等のruntimeやA/B/C/E/F/Gは未証明で、既存PASSを未実施へ戻さない。


### D1-W過去結果 — 2026-09-21 23:03 JST（当時の正本§14.28、現行結果は末尾§14.29参照）

**今回：D1-Wの別helper実装・限定offline compile/reobf、STATIC PASS、有限negative契約10/10。W1正常2→1→0とW2 direct0拒否はLIMITED PASS。W3は検証環境不備でUNVERIFIEDとなりSTOP、W4 NOT RUN**。[共通計画§14.28](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-terminal-witness-result)。W3のCowは`spawn-animals=false`によるnative除去と整合し、入力前からremoved/count0。FOREIGN参加が未成立のため、拒否PASS/危険な誤ALLOWのどちらへも転記しない。開始した3serverは通常保存・exit0/PID終了、製品/旧証拠不変。旧§14.26 FAILと§14.27設計を維持。SAFE DESIGN PROVEN=NO・ownership BLOCKED。

| 受入 | 今回の結果 |
|---|---|
| STATIC/契約 | exact site・native delegate/例外配線/非混入PASS、共有matcherのnegative A–J **10/10**。helper-only offline compile/reobf成功、機械的repair0 |
| W1 | **LIMITED PASS**：native40/80→U1終了でUNKNOWNfalse・同Grant/epoch保持→U2最終自然終了・通常失効/clean5。witness2/2/2 |
| W2 | **LIMITED PASS（negative）**：direct0＋unexpected useでwitness0、fault/UNKNOWN/DENY、quietでも回復/再grant/new epochなし |
| W3 | **UNVERIFIED**：spawn-animals=falseによるCow native除去、入力前removed/count0。FOREIGN参加/自然terminal組合せ未成立。driverのCow生存事前guard不足。誤ALLOW証明/拒否PASSのどちらにも転記しない |
| W4 | **NOT RUN**：W3停止後未作成。固定throwがnative catchを通る実測は残件 |
| 終了/保存 | 3runとも通常remove/save-all flush/stop・全保存/exit0/PID終了。P/M/SP/HP/effects保存一致・witness/Grant非永続キー確認。W3 cleanup1は自然終了証明ではない |

**次の1作業：別承認後、W3の検証環境とCow生存・loaded/count/帰属の事前guardだけを修正し、新規隔離runで未完了W3/W4を実施する。** [共通計画§14.28](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-terminal-witness-result)。新規serverではCowをnativeに保持できる設定（`spawn-animals=true`と生成前/準備前の`doMobSpawning=false`を区別）を採り、native入力前の対象ref/alive/removed/level/cap確認を必須化する候補。正常回復はfalse、安全区画は維持。witness/認可条件を緩めず、W1/W2・旧D1/既存PASSは再試験しない。今回停止したW3の修復/再利用、同run再送、製品実装は行わない。追加外部artifact不足なし、UOM/P vehicle未LOCK。

D1-W全体LIMITED PASSは未付与。製品/原Jar/旧705files不変。既存PASSを再試験せず、SAFE DESIGN PROVEN=NO、SOURCE DIMENSION TRANSITION VERIFIED=NO、ownership BLOCKED、FE6/TimeStop製品未実装、P/T購入停止/SP保護・RC=NO・REAL2CLIENT=BLOCKED・vehicle未LOCKを維持。次回W3はCow保持設定と自然湧き停止を分離し、生存/loaded/count/ref事前guardを満たさなければprepare/native入力前で停止する候補。現在のW3を修復/再利用しない。


<a id="uom-terminal-witness-followup-tests"></a>
### D1-W現行結果 — 2026-09-22 13:42 JST（詳細正本：共通計画§14.29）

**今回：D1-W未完了W3/W4を新run `20260922-132519` の別dedicated/worldで各1回実施し、双方LIMITED PASS。既存W1/W2・STATIC・有限negative契約10/10を再実行せず保持し、D1-W候補の限定検証を完了**。[共通計画§14.29](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-terminal-witness-followup-result) / [独立証拠照合](../build/verification/uom-terminal-witness-followup-20260922-132519/audit/reviewed-results.json)。W3は同Cow positiveのままUOM自然terminal・誤ALLOW/再grant/新epoch0、W4は固定throw/native catch各1・witness1/0/0・cleanup後5 ENDのfault非回復。両server通常保存/stop/exit0、製品/旧証拠1,481files不変。旧§14.26 FAIL・§14.27設計・§14.28環境STOPは履歴保全。**SAFE DESIGN PROVEN=NO、ownership BLOCKEDを維持**。

| case | 現在の判定・今回の実行 | 合格根拠・制限 |
|---|---|---|
| W1 正常2→1→0 | 旧run LIMITED PASS維持・再実行0 | witness2/2/2、同Grant継続→最後のsource終了・clean5。旧証拠§14.28 |
| W2 direct0 / unexpected end | 旧run LIMITED PASS維持・再実行0 | witness0/0/0、quietでもfault/DENY、再grant/新epoch0 |
| W3 FOREIGN＋UOM terminal | **新run LIMITED PASS**、1 process/world | Cow同ref/UUID/level/cap・alive/loaded/count0の2連続END guard。Cow80/UOM38→自然UOM1→0/use(false)、Cow42positive。FOREIGN1・witness0/0/0・UNKNOWN/DENY、誤ALLOW/再grant/新epoch0。40 END/2.038秒 |
| W4 terminal固定throw | **新run LIMITED PASS**、W3照合後の別process/world1 | native setter old1/new0 RETURN→mint→throw1→元catch1、事前期待1/0/0、origin非commit。結果後native cleanup、END139–143の5回quiet/globalfalseでもfault/UNKNOWN/DENY・再grant/新epoch0。49 END/2.435秒 |

[W3証拠](../build/verification/uom-terminal-witness-followup-20260922-132519/W3/audit/reviewed-results.json) / [W4証拠](../build/verification/uom-terminal-witness-followup-20260922-132519/W4/audit/reviewed-results.json) / [差分STATIC](../build/verification/uom-terminal-witness-followup-20260922-132519/audit/static-gate.json)。新helper77,577 bytes/33 entries、version20260922.132519、hash57A4EFE0…FBF05。witness/ownership/guard core byte不変、専用offline compile/reobf成功、repair0。既存full STATIC/finite契約10を再実行しない。測定後のhelper修正・再実行0。

両run：prepare1・P/M1ON・SP0/103・HP20/MAX20/effects空、通常remove/save・save-all flush・stop・全5dimension保存・exit0/PID終了、seal。playerdata/level.datのreadonlyキー照合でwitness/Grant非永続を確認した範囲に限定、world再読込/実client/実認証TCP/HUMAN0。W3 cleanupはRESULT後Cow1回、W4 cleanupは本質結果後UOM1回で、自然終了PASSの代用にしない。製品/原Jar/旧証拠1,481files不変。

**D1-W候補限定検証は完了**。旧W3環境UNVERIFIED/W4 NOT RUN・旧D1 FAILは当時の履歴として保持。**SAFE DESIGN PROVEN=NO、SOURCE DIMENSION TRANSITION VERIFIED=NO、ownership BLOCKED**。FE6/TimeStop製品未実装・P/T購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、vehicle未LOCKを維持。reload/transfer fault/observer loss/FE goal再延長/client全般/production/逆順/同tick/3+source/multi-dimension/性能は未証明。

**次の1作業：process / world reloadで旧Grant・originを誤復元しない条件を、既存証拠からREAD ONLYで整理し、最小dedicated検証1単位を設計する。** [共通計画§14.29](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-terminal-witness-followup-result)。D1-W W1–W4は今回の限定範囲で完了し再試験しない。実process交換と同JVM world reload、native保存値とlive state、一時台帳/sessionの再初期化を区別する。今回は次候補の提示で終了し、新run/helper/compile/再読込/他lifecycle/productionを開始しない。追加外部artifact不足なし、UOM/P vehicle方針は未LOCK。


<a id="uom-process-reload-a1-tests"></a>
### A1 process reload — 2026-09-22 14:45 JST（現行の次候補・DESIGN ONLY）

詳細は[共通計画§14.30](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-process-world-reload-design)だけを正本とする。直前のD1-W現行結果（§14.29）は完了済みとして保持し、その末尾の「設計を行う」は当時の次作業。今回はその設計が完了し、A1実行承認待ちへ進んだ。W1–W4、STATIC、有限10、R1–R9/login compat・旧suiteの再試験0。

| 次回A1の受入項目 | 必要証拠 / 判定（今回は全て未実行） |
|---|---|
| 規模/入力 | 新run1、JVM A/B計2、新world1、同保存worldの通常load1。Aのみprepare1/P・M1ON/SP0・103、UOM native start1/180。real client/HUMAN/TCP/auth試験なし |
| native保存 | capのForgeCaps/ending_library:endinglib_living_cap/TimeStopCount、最終entity NBT、time_stop_saved_data.datのdata/Dimensions、Bのdeserialize前後・live global/Setを別実測。空SavedDataだけでlive状態を推定しない |
| A→B gate | AのG1・known origin成立→normal save/stop/exit0/PID終了→最終disk UOM count>0。count0/対象欠落はUNVERIFIEDでBを開始しない。値修復/再延長/同runやり直しなし |
| cold authority | load前にstatic map/list/frame/witness空・counter0/faultfalseを観測、初回deserialize前に監視配線。Bで旧origin/Grant/session/ref/pending復元0、UUIDだけの昇格0、stale番号認可0、auto ALLOW0 |
| 判定の分離 | 非復活LIMITED PASSとSAFE-SIDE DENY/機能継続未完成を併記可。旧認可再生はSAFETY FAIL、入力/観測/保存不足はUNVERIFIED。native非停止時の通常移動をFH追加ALLOWと誤認しない |
| deserialize/UNKNOWN | 現候補はserver cap deserializeのcount値に関係なくfault。tag/entity/タイミングを記録し、正常player load由来も区別。BでcleanBoundary/fault reset/forgetForReloadを使わない |
| 終了 | Aはmid-stop保存前cleanupなし。Bは結果/seal後だけ必要なnative cleanup→通常remove/save-all flush/stop/exit0→readonly保存照合。各上限/失敗手順は§14.30.6。製品/原物/旧helper不変 |
| 証明対象外 | 同JVM static/world隔離、fault回復、停止途中の保護継続、client lease/input/render/correction、transfer fault、FE goal再延長、多source拡張/性能、production mapping、vehicle方針 |

**次の1作業はA1だけの別実行承認**。新rootのphase-aware補助/readonly observer・限定offline compile/reobf→上記2 phase→通常保存終了・記録が承認候補。追加外部artifact不足なし。今はhelper作成/変更・新run・build/server/client/world再読込0で停止する。SAFE DESIGN PROVEN=NO / SOURCE DIMENSION TRANSITION VERIFIED=NO / BLOCKED - TIME STOP SOURCE OWNERSHIP、FE6/TimeStop製品未実装、P/T IMPLEMENTATION_PENDING・購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、UOM/P vehicle未LOCK、全個別gate/既知許容の可逆クラフト仕様を維持。


<a id="uom-process-reload-a1-completed-tests"></a>
### A1 process reload 現行結果 — 2026-09-22 15:23 JST

**OLD AUTHORITY NON-RESURRECTION LIMITED PASS ＋ SAFE-SIDE DENY**。詳細/識別情報/証拠正本は[共通計画§14.31](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-process-reload-a1-result)、[独立照合](../build/verification/uom-process-reload-20260922-145728/audit/reviewed-results.json)。上記14:45のA1 DESIGN ONLY/承認待ちは当時の履歴であり、今回の新run `20260922-145728` で限定完了。旧D1-W W1–W4・STATIC/有限10・R1–R9・その他suiteは再実行0で保持。

| 受入項目 | 実測・判定 |
|---|---|
| 専用build/STATIC | 新helper20260922.145728、78,631 bytes/37 entries、AABF0A65…41464B。offline compile/reobf成功、測定前機械修正1（LazyOptional supplier）、測定後変更0。8core source不変、class差はRUN literalのみ、製品/外部class非混入 |
| A入力 | 新world1/dedicated A。fresh/P・M1ON/SP0・103をprepare1、native180開始1、baseline5 END。origin1/binding1/G1 issue1、epoch/sequence1、permission=trueで成立 |
| 保存前→A3 | count179/globaltrue/Set overorld→通常保存終了。helper cleanup0。native shutdown自身のuse(false)/count0は保存後変化として記録。最終disk同UOM count179、SavedData data{}/Dimensionsなし、P/M/SP/HP20保存一致、exit0/file close後のgate PASS |
| B cold load | 別PID/heap、初期authority全空・faultfalse。実UOM deserialize HEAD tag179/live0→RETURN179、既存fault1。その後player空tag/count0でfault2。observerは初回load前arm、HEADは既存candidate注入後/native本体前 |
| B 5 END | count178→174、globalfalse/Set空のnative不整合。UNKNOWN/faulttrue、origin/binding/Grant0・matches/permissionfalse。old session/ref/witness/frame/pending利用0、UUID-only/stale認可0、auto issue/ALLOW0。新session1は旧番号を認可根拠にしない |
| seal/終了 | 結果後B native cleanup1でcount0、fault/UNKNOWN維持。両通常save/stop/全dimension保存/exit0/PID終了。B最終P/M/SP/HP20/effects空、source0・SavedData data{}。3rd loadなし |
| 境界 | AUTOMATED / EmbeddedChannel。実client/認証TCP/HUMANなし。native guardDeny=falseを追加認可PASSと扱わない。helper既存core条件・製品/旧2,029files不変、購入/進行SP仕様不変。GUI購入成功ではない |

同JVM隔離、正常保護復帰/UNKNOWN回復、crash、observer loss/transfer fault、FE goal再延長、multi-source拡張/性能、real client、production mapping、vehicle方針は未証明。A1限定PASSで総合gateを解除しない。

**次の1作業：別承認後、同JVM world/server交換におけるstatic authority・terminalFaultの寿命と隔離境界だけをREAD ONLYで整理し、最小検証1単位を設計する。** A1の別JVM非復活は完了済みで再試験しない。Aの通常終了時fault/残stateとBの新規deserialize faultを根拠に、旧authorityの漏れと安全側DENYの残留を分ける候補。UNKNOWN回復方式のLOCK・helper変更/新run・same-JVM実行・production mapping・購入解放は今回未承認で、ここでは開始しない。追加外部artifact不足なし、UOM/P vehicle方針未LOCK。

SAFE DESIGN PROVEN=NO / SOURCE DIMENSION TRANSITION VERIFIED=NO / BLOCKED - TIME STOP SOURCE OWNERSHIP、FE6 MobEffect/TimeStop production未実装、P/T IMPLEMENTATION_PENDING・購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、UOM/P vehicle未LOCK・既存個別gateと可逆クラフト既知許容仕様を維持。


<a id="uom-same-jvm-context-tests"></a>
### SJ1 same-JVM context交換 — 2026-09-22 15:51 JST（現行の次候補・DESIGN ONLY）

詳細正本は[共通計画§14.32](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-same-jvm-context-design)。§14.31/A1現行結果は**OLD AUTHORITY NON-RESURRECTION LIMITED PASS＋SAFE-SIDE DENY**のまま保持する。その末尾の「READ ONLY設計」は今回完了し、SJ1だけの別実行承認待ち。A1・D1-W・R1–R9・既存suite/実clientの再実行0。**下表は受入設計で、実測/PASSではない**。

| SJ1受入項目 | 必要な証拠・区別 |
|---|---|
| 実runtime/規模 | 実client1/process1/JVM1/同ClassLoader、通常Save & Quit→title→同新world再open1。IntegratedServer A/B2・world1/load計2・host login2・論理source1。人工new object・dedicated EmbeddedChannel代替なし |
| A入力 | fresh P/M各1ON・SP0/103、prepare1、native UOM180開始1、valid G1 issue1とclient Lease。初期fault/HP/値不一致をresetで補正しない。歩行/R再試験なし |
| exit gate | 退出直前→logout/serialize/native shutdown→早期title→ServerStopped hook RETURN＋old thread終了＋保存closeのsettled snapshot。isShutdown/titleだけではstopServer完了ではない |
| 同dimension新context | old/new server・level・source/cap・player・両端listener/Connection/channel実ref。Stateはdimension文字列keyなので旧objectをnew currentへ使えるSTATIC RISK。実call-siteのRETURNを観測し、発生ならstate machine SAFETY FAIL |
| fault起源 | engine実ref/fault/reason/faultEvents/呼出し引数/event順序。menu/B constructor前後/enable観測/first deserializeのcandidate直前・直後/native RETURN/first tick/loginを分離。old/both/new/observer不足を判別 |
| Session/Lease | active/grant失効とhistory/issued/serial/sequence残留を分離。Leaseのclear/reset/単なるinvalidを区別。新helperでclient登録1回を確認し、追加readonly observerからvalidを呼んでcacheを消さない |
| B | prepare0/start0/issue0、実loadとnative同期のみ。raw観測後に既存candidate評価を別記録し最大5 END。fault/UNKNOWNを回復せず、数字やsame UUIDだけでold authorityを認可しない |
| 判定 | 旧authority/Stateの誤利用0なら限定非再利用PASS候補。old/new fault由来のDENYは機能面の別判定。単なるstale参照保持は即FAILでない。必須順序/identity欠落はUNVERIFIED。最初の本質FAIL後に不足観測をPASSへ補完しない |
| CLEAN/FAULT最小化 | valid旧G1から通常退出する1遷移のみ。自然な退出faultがあればold-fault持越しも観測。発生しなければFAULT runtimeは未観測、throw注入/W4再演/追加runなし |
| 終了/境界 | seal後必要時だけB native終了cleanup≤1、通常Save & Quit→readonly保存→Quit Game/process終了。A事前cleanup0、3rd load0。observer修復/全map.clear/forgetForReload/UNKNOWN回復なし |
| 時間・主体 | 各上限と失敗終了は§14.32.7。Computer Useが通常GUI、AUTOMATEDが観測・明示native入力、HUMANは実際の認証/入力不可だけ。singleplayer memory接続で足り、auth/TCP再証明不要 |
| 次回承認・artifact | 新verification専用補助/限定offline compile/reobf→新instance/world→上記1遷移→保存終了・3文書。既存製品/承認6原物/shader/login compatを使用、追加外部artifact不足なし。今回は補助作成・起動・試験0 |

STATIC RISKの特定だけでsame-JVM FAIL/PASSは付けない。成功してもfault recovery/正常保護復帰、crash/observer loss/transfer fault、FE goal再延長、multi-source拡張/性能、client input/render/correction、production mapping、vehicle方針を証明しない。cleanup候補は比較のみで未LOCK。

SAFE DESIGN PROVEN=NO / SOURCE DIMENSION TRANSITION VERIFIED=NO / BLOCKED - TIME STOP SOURCE OWNERSHIP、FE6/TimeStop production NOT IMPLEMENTED、P/T IMPLEMENTATION_PENDING・購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、UOM/P vehicle未LOCK・既存個別gate/可逆クラフト既知許容仕様を維持。


### SJ1実行結果 — 2026-09-27 10:30 JST

正本：[共通計画§14.33](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-same-jvm-sj1-result) / [実測集計](../build/verification/uom-same-jvm-20260927-095730/audit/reviewed-results.json)。§14.32設計を変更せず実行した。

| 受入単位 | 今回結果 |
|---|---|
| STATIC/helperのみoffline compile/reobf | PASS、repair1（測定前UI/closing配線のみ）。core判定不変、client登録1、observer余分なvalid0 |
| A valid input | 成立：prepare1/native start1/G1 session1 epoch1 sequence1、native origin/binding・client Lease valid。HP20/MAX20、P/M1 ON・SP0/103 |
| A→title | 通常save/logout/revoke/native終了/Level unload/Stopped hook RETURN、thread TERMINATED・lock解放。faultEvents0→2、old State/origin/binding/history/issued残留、G1 revoked |
| 同JVM B new context | PID24916/同loader/Minecraft実ref、new server/thread/level/source/cap・client refs確認。same source UUID native load count177、B新prepare/start/Grant0。B server player/listener/Connection/channel全実ref対照は早期停止でUNVERIFIED |
| old State隔離 | **SAFETY FAIL**：event5005、native deserialize→fault→unknown→stateで旧overworld State実refをnew level currentへ返す。permission=falseでもPASS/safe DENYにしない |
| fault起源 | **BOTH**：A終了fault2残留＋B source deserialize2→3、player deserialize3→4。同engineと前後順序で分離 |
| 旧認可非利用 | B controlled5 END/matchesは0・未実施。G1 ALLOW/Lease validの観測0、witness consume0、G2なしを全非利用PASSへ拡張しない |
| ClientLease | A valid→expiry→logout/unload reset、B新refsの272samples valid0。extra observer valid呼出0。全client失効分岐PASSではない |
| seal/終了 | 結果seal→B native cleanup1→通常Save & Quit、thread終了/file close、readonly本人/source/SavedData照合→normal Quit/PID終了。3rd load0。HP20/PMon/SP0/103、source1・NoAI/persistent・count0、authority保存keyなし |
| 製品/既存証拠 | 229,494 bytes/150 entries、SHA256 5C1A716EEDE7AEB4449B36EEFFBDDA156176B16B60A1676229224AA3390DB327。保全2,085files差分0、製品build・既存suite0 |

COMPUTER USE＝新world GUI/command/F2/save/Quit、AUTOMATED＝観測/native入力/normal pause、HUMAN0。実client full PASS/SAFE DESIGNへ昇格しない。次回は[固定queue #1](MASTERY_IMPLEMENTATION_PREPARATION.md#v3-fixed-release-completion-queue)の実反例限定修正/再検証であり、SJ1未実施に戻して同helperを反復しない。

### 過去の受入範囲更新 — 2026-09-27 10:30 JST

以下は当時の未実装記録。現在のPam/count完了は§29/30と最新Statusを参照する。

Pam増産と新食義countは**FORMAL RELEASE REQUIRED / NOT IMPLEMENTED / NOT TESTED**。8/27候補履歴を保持し、現行scopeは[SPEC§14](SPEC.md#14-v300-formal-release-required-additions)へ移行した。今回これらのcode/test実装・実行は0。

- Pam：未決定詳細LOCK後、対応版presentの正確なharvest1/log1→harvest2、対象外排除、決定したlog境界、absent時のregistry/recipe/classloading/起動安全・Jar非混入を限定確認。全外部版や未LOCK50品を自動必須にしない。
- count：Nutrition1/4/5/8、満腹20、Satisfaction成功をdeclared分1回。non-food正delta、saturation-only0、food delta二重count0、client二重count0、generic→Root/TrueRoot流入0、HP変換不変。最終threshold決定後に複数threshold/level/SP、long/invalid/overflow atomicity、partial count/custom Config/v2v3移行、sync/save/reload/既存SP二重付与0を検証。**1000はPROVISIONAL**、旧200-food-actions方式のまま正式release可とはしない。
- 成功済みscopeの再試験を無条件前提にせず、[固定9工程](MASTERY_IMPLEMENTATION_PREPARATION.md#v3-fixed-release-completion-queue)の変更に必要な回帰へ絞る。未対応全MOD/trait/性能/全WARN0はNOT TESTED/backlog、実2-client既存BLOCKEDとrelease policyを独断変更しない。

SAFE DESIGN PROVEN=NO / SOURCE DIMENSION TRANSITION VERIFIED=NO / ownership BLOCKED。FE6/TimeStop production NOT IMPLEMENTED、P/T IMPLEMENTATION_PENDING/購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、vehicle未LOCK・個別開始条件・可逆クラフト既知許容仕様を維持。


### SJ1 authority isolation follow-up — 2026-09-27 11:10 JST

正本：[共通計画§14.35](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-same-jvm-isolation-followup-result) / [証拠](../build/verification/uom-same-jvm-isolation-20260927-104834/audit/reviewed-results.json)。run20260927-104834、総合**UNVERIFIED**。新規の製品テストPASSではない。

| 受入項目 | 結果 |
|---|---|
| STATIC/補助offline compile/reobf | PASS、補助repair0、製品task/既存suite0 |
| A valid authority | 成立、prepare1/start1/G1 session1 epoch1 sequence1、実client Lease valid/nativefalse/effective true |
| A通常終了・旧証拠 | 成立、closed old context/State/origin/binding/fault2/history/issued保持、全reset0 |
| B native deserialize State返却 | event3180成立、oldStateSameActualRef=false、actual B server/level一致、engine別 |
| B fault起源 | new context0→1→2。A old fault2は別engineに保持、currentへの継承0 |
| current player/server/State 5 END | 5/5成立、旧Grant/session/origin/binding/witness/callback認可0、epoch/sequenceだけの認可0、G2なし |
| ClientLease | B338samples valid0/ALLOW受信0。client refs new、observerの追加valid呼出0 |
| B source継続観測・cleanup | **UNVERIFIED/未完了**。native server sourceの初回loadは観測、後にclient joinが共有u1を上書き。RESULT sourceはserver0/client level。cleanup誤skip、use(false)0、保存count160 |
| seal・通常終了 | seal済み、A/B通常保存、thread終了/file close、readonly照合、Quit/PID終了。本人HP20/PMon/SP0/103、保存source1。cleanup完了へ補完しない |
| 全体受入 | **UNVERIFIED**。helperの `LIMITED PENDING SAVE` receiptは最終PASSではない。測定後修正/再build/retry/3rd loadなし |

次回承認範囲はhelper sourceのServerLevel/current actual server限定＋独立client観測と不足の限定follow-up。既存候補のauthority/Grant/UNKNOWN/合格条件を変更しない。queue残9、#2未開始。SPEC§3/§14.2の優先関係整理は文書だけで、Pam/count/製品Config実装は0。SAFE DESIGN PROVEN=NO、ownership BLOCKED、RC=NO、P/T購入停止/SP保護、REAL2CLIENT=BLOCKEDを維持。


### queue #1 source分離・残確認 — 2026-09-27 12:23 JST

run `20260927-121020`：[結果正本§14.36](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-same-jvm-source-followup-completed) / [実測・受入照合](../build/verification/uom-same-jvm-isolation-followup-20260927-121020/audit/reviewed-results.json)。**QUEUE #1 SAME-JVM AUTHORITY ISOLATION FOLLOW-UP LIMITED PASS＋SAFE-SIDE DENY**。製品実装/製品保護のPASSではない。

| 受入項目 | 結果 |
|---|---|
| 核心不変/補助build | core source同一、26class byte同一・3classはrun ID定数だけ。補助offline compile/reobf成功・repair0、製品task/既存suite0 |
| A準備/valid/通常退出 | prepare1/start1/G1 session1 epoch1 sequence1・実client Lease有効。HP20/PM1ON/SP0/103。正常save/停止hook RETURN/thread/file close/title settled、旧fault2等を全clearせず保持 |
| B State/fault | oldStateSameActualRef=false・actual B server/level一致。A fault持越し0、B native deserialize new fault0→1→2 |
| B server source | same UUID/new actual ref/count176・native lookup同一refで1回bind。pre-insertion観測とbindを分離。client専用slotからserver slotへの書込0、逆方向0 |
| controlled5 END | authority isolation5/5＋source lookup前後5/5、count166→165→164→163→162。旧State/origin/binding/Grant/session/witness/callback認可0、G2なし |
| client | 実client220samplesでB Lease valid0/ALLOW受信0。独立client source refをserver数値/cleanupへ使用しない。observer追加valid呼出0 |
| seal/cleanup | seal前後server lookup/count162 guard成立→native終了1回→同source count0/globalfalse/Set空/stack0/Grant0/permissionfalse。fault/UNKNOWN保持、native値修復0 |
| 保存/終了 | 通常save→thread終了/file close→readonly本人HP20/PM1ON/SP0/103・sameUUID source count0/NoAI/persistent・authority保存key0→正常Quit/PID終了。新world1/load2、3rd load0 |
| 不変/範囲 | 製品229,494 bytes/150 entries/5C1A716E…90DB327・配置10Jar・3,037files不変。COMPUTER USE＋AUTOMATED、HUMAN0。旧失敗runは再利用/修復なし |

固定queue #1 COMPLETE／残8。次の#2は別承認・未開始。SAFE DESIGN PROVEN=NO、SOURCE DIMENSION TRANSITION VERIFIED=NO、ownership BLOCKED、FE6/TimeStop production未実装、P/T購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、vehicle未LOCK・他の個別gateを維持。


### queue #2 production mapping — 2026-09-27 12:43 JST

**STATIC MAPPING BLOCKER / 製品変更前STOP**。[共通計画§14.37](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-production-mapping-stop)が根拠・再開条件の正本。通常初回goalのsetter180→common useで候補がUNKNOWNとなる点と、空/zero NBTでもcontext terminalFaultが固定される点を、原Jar・現候補source/bytecode・既存reload証拠で照合。新しい動作FAIL/PASSではない。

| 今回の受入範囲 | 判定 |
|---|---|
| mapping / 原物識別 | 12概念以上の写像表、10原物hash一致、元製品229,494 bytes/150 entries保持 |
| production / focused A–R / actual FE・EL / same-JVM client / build・unit・check / core回帰 | **NOT RUN**。上記入口のauthority semantics変更が必要なため、依頼§34のSTOPを適用。compile typo修理ではない |
| 旧queue #1・R1–R9・player17/vehicle16・既存suite | 各限定PASSを維持。今回再実行0、未完了へ戻さない |
| 次の焦点 | 初回goalの正規setter→useと異常/direct setterを混同しない開始観測。empty/zero NBTを正当sourceへ昇格させず、positive/active/malformedは由来喪失としてDENYし、旧authority・faultを復活/clearしない。これは**要設計・未採用**であり合格条件変更済みとは扱わない |

次の承認候補は上記2入口に限る設計・検証と、その根拠に沿うqueue #2再開。全lifecycle/既存suiteの再演を開始条件にしない。追加artifact不要、vehicle valid-P方針未LOCKは別項目。起動0・外部state書込0、save/stop/Quit非該当。製品/gate/protocol6不変、SAFE DESIGN PROVEN=NO、ownership BLOCKED、P/T購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、固定queue残8。

### queue #2 Gate A入口限定結果 — 2026-09-27 13:31 JST

正本：[§14.38](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-entry-gate-a-partial-result) / [実測集計](../build/verification/uom-production-entry-20260927-130322/audit/reviewed-results.json)。新run20260927-130322、**Gate A INCOMPLETE / native起動STOP / production変更0**。

| 受入範囲 | 結果 |
|---|---|
| S1 normal exact initial goal / terminal限定回帰 | 各LIMITED PASS。正常RETURN後commit、UNKNOWN/fault false、epoch1/Grant1。native countdown完了・witness各1・revoke。実client movementではない |
| S2 raw positive / S3 abnormal RETURN / S4 wrong actual ref | 各LIMITED PASS、authority生成なし・DENY。S3は固定throw入力、S4は有限bridge契約 |
| D1 absent / D2 zero / ordinary player empty | 各LIMITED PASS、native pre/post0、fault0/origin0/Grant0。observer開始をload後へ遅らせない |
| D3 positive deserialize | LIMITED PASS、post9/fault/UNKNOWN/DENY、origin/Grant再構築なし |
| D4 wrong type / D5 inconsistent post | D4 UNVERIFIED（FE self-attach→plugin初期化→optional classloadingでworld前exit1）、D5 NOT RUN。分類コードの存在をruntime PASSにしない |
| prior UNKNOWN | STATIC残件：開始HEADの既存reconcileより前にUNKNOWNを保全するguardと負対照が必要。今回正常/負対照9記録のみで全入口契約をPASSにしない |
| build/修理 | 検証補助のみoffline compile/reobf成功。repair1/3：MC継承メソッドowner/mappingと証拠attempt分離。production/test-helper repair各0 |
| 終了 | 6process通常save/stop/exit0（うち初回S1は測定前補助FAIL）、D4はbootstrap exit1で正常保存終了とはしない。全7 PID終了確認。HUMAN0/COMPUTER USE0 |
| 製品/旧証拠 | 新製品Jarなし・229,494 bytes/150 entries/5C1A716E…90DB327不変。製品focused/build/unit/check・60/133/49/86・実client/same-JVM再試験0。queue #1と既存限定PASSは保持 |

次はFE self-attach/plugin起動経路の安全な対処範囲を確認し、prior UNKNOWN guardと未完了D4/D5へ復帰する。SlashBlade追加必須化・外部原物改変・成功するまで同失敗runを再利用する対応は禁止。Gate A成立後の製品工程は承認済みだが、今回は指定native反例STOPに従い進まない。#2 INCOMPLETE/残8、SAFE DESIGN PROVEN=NO、ownership BLOCKED、P/T購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED・vehicle未LOCKを維持。

### queue #2 最終製品限定検証・client起動STOP — 2026-09-27 14:48 JST

正本：[共通計画§14.39](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-production-followup-gate-completed-client-startup-stop) / [実測集計](../build/verification/uom-production-followup-20260927-134126/audit/reviewed-results.json)。旧§14.38の9PASSを保持し、新規Gate A5記録で **ENTRY SEMANTICS GATE A COMPLETE**。旧履歴を当時からPASSへ書き換えない。

| 受入項目 | 今回の証拠・判定 |
|---|---|
| 起動安全性 | 独立Instrumentation bridgeでdedicated限定PASS。原物/TimeStop state修復0、SlashBlade追加/fake/plugin skip0。PrismのASM先行読込/後続module登録衝突は未解消 |
| prior UNKNOWN / D4 / D5 | P0 1、D4 String/negative Int/Long 3、D5 actual post9 1の計5記録PASS。旧P0D5注入順FAIL保全。UNKNOWNを永久fault化しない |
| 最終製品 pure unit | **9ケースPASS**。terminal正常/不正、非source inert0→0。既存単体testsも成功 |
| 最終製品実FE/EL | **19/19**。NORMAL11（exact goal、unowned/親OFF/MOFF、Grant、actual movement、held拒否、再接続、terminal、mixed）、P0 1、DATA5、FOREIGN/vehicle2。原物Forge47.4.0。EmbeddedChannelなので実client/TCPと区別 |
| 製品同JVM旧State非再利用 | owned actual-server/actual-level実装と静的確認済み。**製品runtime NOT RUN**、旧queue #1候補PASSで代替しない |
| 製品positive reload | native positive deserializeでorigin/Grant0/DENY **限定PASS**。**disk保存→再読込はNOT RUN**。D5製品1caseは固定observer契約であり自然故障の再現ではない |
| ClientLease / GUI/HUD/入力 | helper-only compile/reobf PASS。Prism3起動がworld前FAIL、prepare/start/Lease/歩行0、**ゲーム動作UNVERIFIED**。client helperの期待値を実測扱いしない |
| build / 必要回帰 | build/foodHealingUnitTest/check PASS。最終source Forge47.2.0 userdev vanilla60/60・TaCZ60/60。Trial/L2/SW実suite再実行なし、旧PASS保持 |
| Jar | 298,980 bytes/189 entries/D28253FC…6F60F5B6、protocol7。metadata/Mixin/refmap/reobf・非混入PASS。P/T gate/SP source不変 |
| 保存/終了 | 9専用server process中8通常save/stop/exit0、1bootstrap exit1。core2server保存/自動終了。client world0のためSave&Quit非該当、終了コード2の起動失敗を正常Quitとしない。最終対象process0 |

**次回は独立bootstrapとPrismのmodule順序を直したうえで、未実施の製品client入力/Lease・actual same-JVM＋positive保存再読込だけを実行する承認範囲**。既存Gate A14/製品19/unit9/core60、旧R/queue #1を無条件再実行しない。製品変更が必要なら影響テストだけ再実行。新artifact不足なし、valid-P vehicle判断は未LOCKの別境界。

修理：startup2、Gate A helper2、production3、product fixture2、client helper compile0。旧失敗/中間Jar/未反映launch引数の2回目も保全。#2 INCOMPLETE/残8、SAFE DESIGN PROVEN=NO/ownership BLOCKED、FE6未実装、P/T IMPLEMENTATION_PENDING/購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、可逆クラフト既知許容仕様維持。#3未開始。

### queue #2 bootstrap解消・Lease修正／実入力未成立 — 2026-09-27 15:51 JST

正本：[共通計画§14.40](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-client-bootstrap-lease-fix-result) / [今回集計](../build/verification/uom-production-lease-20260927-153334/audit/reviewed-results.json)。旧§14.39の成功・失敗とGate A14/旧19/core60等は当時の対象で維持。

| 受入項目 | 今回の結果 |
|---|---|
| Prism bootstrap | **LIMITED PASS**。JDK-only premain、ASM/Forge/Mixin/MC/FE早期load0、named ASM module後のexact bridge。3 launch中初回manifest欠落exit2、後2回title/world/LocalPlayer到達。外部原物・既存compat不変 |
| heartbeat製品修正 | SessionRegistryだけworld gameTime差から5 server END計数へ。認可/Grant/TTL1000ms不変。compile/build/unit/check PASS。最終Jar **37E300EA…86592E3**、298,958 bytes/189 entries/protocol7 |
| 専用自動 | **4/4 PASS**：initial Grant、frozen gameTime下35回再送、native終端revoke、quiet10ENDで更新なし。旧fixtureは観測priority順FAILを保持しLOWESTへ修理、両試験通常save/stop/exit0。既存全suite再実行なし |
| actual Lease / native | prepare1・goal1。client revision1–36 allow（更新35）、current refs/validtrue/native canMovefalse。native0/globalfalse/Set終了→Grant revoke→revision37 false→client Leaseなし。**限定PASS** |
| input / server移動 / 収束 | W操作1要求、発動中input記録0・XYZ変化0、helper FAIL/ポーズ。押下時刻・長さの実測なし。**UNVERIFIED**、製品拒否やunsafe ALLOWと断定しない。idle packet ALLOWを移動PASSにしない。通常歩行復帰もNOT RUN |
| same-JVM / positive disk | A通常終了で実Context closed/maps0、保存count0。B再読込0、positive用goal0、B判定・positive deserialize・非復活は**NOT RUN**。seal0/cleanup0。以前の候補PASSを転記しない |
| 保存 / 終了 | 両client通常Save&Quit/title/Quit、PID終了、readonly本人/source/lock確認。HP20/P・M各1ON/SP0・103/count0。始動失敗を正常Quitとしない |

**次の承認単位**：初回有効Leaseを通常pauseで観測する補助制御と再開→通常短距離入力の一連操作へ限定修正、新規runで未完了の実入力/native終了後歩行→A positive保存→同JVM B1回→非復活確認/seal/必要時native cleanup1/通常保存Quit。初回入力成立前にcountが終われば、そのrunを再arm/resetしない。既存heartbeat/起動対処/全suiteは理由なく再試験しない。今回の追加起動は上限到達につき未承認として停止。

queue #2 INCOMPLETE／残8、SAFE DESIGN PROVEN=NO／ownership BLOCKED（製品入力・same-JVM・actual positive保存再読込）、SOURCE DIMENSION TRANSITION VERIFIED=NO、P/T pending/購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED・vehicle未LOCK維持。#3へ進まない。

### queue #2最終残確認 — 実pause/resume成立・input UNVERIFIED（2026-09-27 18:19 JST）

正本：[§14.41](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-final-input-pause-result)／[集計](../build/verification/uom-production-final-20260927-180600/audit/reviewed-results.json)。§14.40と既存各suiteのPASS・FAILを保持。今回の合格条件を緩めない。

| 受入項目 | 今回の結果 |
|---|---|
| helper・実行上限 | 新run174200/175400/180600、3 launch。補助の開始前待機/observer順序・pause観測場所だけ修理2 cycle、専用offline compile/reobf計3回成功。製品不変、製品build/unit/check・旧suite再実行0 |
| 新環境/prepare | 各新world生成前GUIでSurvival/Normal・自然湧き/回復false、閉鎖区画・最大正面移動約1.2。各prepare1・P/M1ON・SP0/103・HP20。旧world/receipt流用0 |
| pause / fresh Lease | 最終runで通常Esc→tick1592/count176の停止を500ms超観測。再開後revision2/同epoch・seq1/実validtrue/native canMovefalse/current refs/permissiontrue/count174、INPUT_WINDOW_READY。**LIMITED PASS** |
| 実入力→movement | CU通常W3呼出し、181実client tick全てkeyUp=false/forwardImpulse0。server/client移動0、idle handler9件ALLOWは移動証明でない。**INPUT UNVERIFIED**。movement/収束は未確認で、入力が存在した製品拒否FAILとはしない |
| native end / revoke | E1 goal1、allow rev1–36/revoke37。native count0/globalfalse/dimensionfalse/origin0、witness1/1/1/faultfalse、Grant失効/Lease消去。限定成立。通常W復帰はNOT RUN |
| E2 / B / 正の保存再読込 | 前提の実入力が未成立のためE2 goal0/B0/seal0/cleanup0。new refs/oldState実比較/positive deserialize/旧authority非復活は**NOT RUN**。disk0をpositive試験PASSにしない |
| 正常終了 | 各run通常Save&Quit→titleでthread TERMINATED/Context closed/maps0→Quit/PID終了。readonly本人/source/count0/HP20/取得SPtoggle保持、保存lock解放。途中失敗とログを保全 |

**次の確認対象**：**次の1作業：queue #2の実入力手段を確定し、別承認後に未完了lifecycleだけを新runで実施する。** [§14.41](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-final-input-pause-result)。通常ポーズ・RenderTickでの停止観測・再開fresh Leaseは今回成立したため再設計しない。現Computer Useの`press_key`はkey/chordのみで保持時間指定がなく、今回3回のW呼出しでも実tick入力を得られなかった。単なる同じ短pulse再試行は採らず、対応する通常キー保持手段（利用可能APIで不可なら本人の1操作を別途調整）と`REAL INPUT OBSERVED`の確認を先に成立させる。MC input field/GLFW/packet/位置/外部stateの代入は禁止を維持。成立後のみE1移動→native終了後の通常入力復帰→E2 positive保存→同JVM B1回→非復活/seal/必要時native cleanup1/通常終了へ進む。今回起動3回上限に達したため追加runは今回実行しない。追加外部artifact・新仕様判断の不足なし。旧run再利用/resetや既存suite再演、#3開始はしない。

queue #2 INCOMPLETE/残8、SAFE DESIGN PROVEN=NO、SOURCE DIMENSION TRANSITION VERIFIED=NO、ownership BLOCKED（実入力/移動・通常復帰/same-JVM B/positive reload）、P/T pending・購入停止/SP保護・RC=NO・REAL2CLIENT=BLOCKED・vehicle未LOCK維持。#3未開始。


### queue #2 INPUT CAPABILITY PREFLIGHT — 正式hold APIなし・launch0（2026-09-27 20:10 JST）

現行結果は[共通計画§14.42](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-input-capability-preflight)。直前18:19結果と過去の「次の確認対象」は当時の履歴として保持する。

| 確認項目 | 今回の結果・受入条件 |
|---|---|
| 起動前input API確認 | 公開`PressKeyInput`はwindow/keyのみ。実Sky公開action一覧にもkeyDown/keyUp/duration holdなし。**INPUT METHOD PREFLIGHT: AUTOMATED HOLD NOT AVAILABLE**。API名を推測せず、Minecraft内で短pressを再試行しない |
| 実施主体・起動境界 | AUTOMATED READ ONLY schema/action照合。ゲームCOMPUTER USE/HUMAN入力は未実施。Minecraft/Prism launch0、新run/world/helperなし。製品/helper修理0、compile/build/test0 |
| actual input / movement / restore | 今回NOT RUN/未測定。次回はREAL INPUT READY表示・報告時のHUMAN物理W約1秒だけを用い、実client tickのkey/forward positiveと同時のvalid Lease/native canMovefalse/positive countをreadonly確認してから既定の移動・収束判定へ進む。期待値変更なし |
| E1 pause/fresh Lease/end/revoke | 今回再試験なし。§14.41までの限定PASSを維持 |
| E2 / positive disk / same-JVM B | 今回NOT RUN。epoch/sequence・実保存count・A/B実参照・positive deserialize・旧authority非復活・B permission/Leaseの期待値は従来どおり。未測定値を0件PASSにしない |
| seal / cleanup / final save/Quit/PID | 今回対象runなし。既存終了証拠は維持。次回もraw seal前の外部state修復は禁止、既定の通常保存・終了・readonly照合を要求 |

次の1作業はHUMAN方式での未完了lifecycle確認（次回承認後）。追加artifact不要。#1 COMPLETE／#2 INCOMPLETE／残8、SAFE DESIGN PROVEN=NO、SOURCE DIMENSION TRANSITION VERIFIED=NO、ownership BLOCKED、P/T pending・購入停止/SP保護・RC=NO・REAL2CLIENT=BLOCKED・valid-P vehicle未LOCK維持。#3は開始しない。

### queue #2 HUMAN入力の受け渡し未成立（2026-09-27 20:53 JST）

現行結果：[§14.43](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-human-input-handoff-unverified) / [判定証拠](../build/verification/uom-production-input-final-20260927-203225/audit/reviewed-results.json)。以前の節内の「次」は当時の履歴。今回の合格条件変更・旧PASS再判定なし。

| 受入項目 | 結果 |
|---|---|
| prepare / READY | 新run20260927-203225、prepare1/goal1。通常pause/fresh Lease revision2/count174・native canMovefalseでREADY。限定成立 |
| 本人W / actual input / movement | **HUMAN INPUT NOT PERFORMED / INPUT UNVERIFIED**。本人は前面切替で指示を確認できず。180tick/positive0/server・client移動0。製品movement FAILではない。idle handler39/correction0を移動PASSにしない |
| end/revoke | native count0/global・dimensionfalse、witness1/1/1/faultfalse、Grant/Lease失効を限定確認 |
| restore / E2 / B / positive reload | **NOT RUN**。seal0/cleanup0。count0の保存をpositive検証にしない |
| 終了 / 保全 | 本測定Save&Quit/全保存/titleでthread TERMINATED/Context closed/readonly HP20・SP0/103・P&M ON/source0/Quit/PID1888終了。予期しない2回目起動はfirst-write guardでworld前停止、通常close/PID32968終了。総起動2、本測定1、world再読込0。旧run不変 |
| 変更境界 | helper run/path/version/READY observer・HUDだけ、限定offline compile/reobf成功。製品298958B/189entries/hash37E300EA…86592E3不変、製品build/test0。HUMAN入力0・CU W0 |

次は本人への入力手順の受け渡しと必要操作数だけをREAD ONLY整理し、次回実行範囲を明示する。native180延長、TTL延長、state/位置/input setter、短pulse自動W代用、同run resetは採用しない。通常復帰に追加本人操作が必要なら、1操作限定の承認を自動拡張しない。追加artifact不足は未検出。

**#1 COMPLETE／#2 INCOMPLETE／残8、#3 NOT STARTED**。SAFE DESIGN PROVEN=NO、SOURCE DIMENSION TRANSITION VERIFIED=NO、BLOCKED - TIME STOP SOURCE OWNERSHIP。P/T IMPLEMENTATION_PENDING・購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、valid-P vehicle未LOCK、他の個別開始条件を維持。食料生産の極意による可逆クラフト増加は既知かつ許容仕様・バグ修正対象外。

### queue #2 画面内READYと本人2操作 — DESIGN ONLY（2026-09-27 21:03 JST）

手順正本：[共通計画§14.44](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-human-input-handoff-design)。§14.43のHUMAN未入力/UNVERIFIED・過去PASS/FAILは保持。以下は次回の受入項目であり今回のPASSではない。

| 項目 | 次回の観測/合格・停止条件 |
|---|---|
| 受け渡し | 前面化前に本人が2操作の説明を確認。helper専用の中央HUD帯で「1/2 REAL INPUT READY」「2/2 NORMAL INPUT READY」を別々に表示。chat/Alt+Tab不要。表示開始と実入力は別証拠 |
| A READY | 正しいrun/world/player/source/Jar、prepare/goal成立、UNKNOWN/FOREIGNなし、実valid RETURN/fresh Lease/current refs/session/epoch/seq整合、native false/positive count、fresh observer、安全区画、画面active/キー解放。失効時非表示、同phaseの再要求なし |
| 操作A | 本人W約1秒→解放。readonly keyまたはforward positive、既定server/client移動・actual packet/guard・解放/収束をnative終了前に観測。A表示のみ/idle packetはPASSにしない |
| A終了→B区画 | native終了/revoke/Lease消去を確認。本人を移動させず内側停止壁を必要時だけ1回開き、B進路・client block同期・位置不変/接地/HP/effects/静止・最大距離2未満を読取確認。prepare時から外周の床/側壁/屋根/停止壁を確保。測定値修復なし |
| B READY/操作B | A成功、stage4、native count/global/dimension0/false・G1 revoked/Leaseなし/UNKNOWNなし・native canMove true、解放3tickと安全進路。本人W約1秒→解放。独立したnormalInput/移動/normalConverged/normalMovedを既存閾値で確認。Aのreleaseやrevokeだけでは代用不可 |
| timeout | Aはnative180の自然終了かREADY後15秒watchdogの早い方。B準備30秒、READY後入力待ち30秒、positive後解放/収束10秒上限。native/TTL延長なし、再arm/resetなし。positiveなしはINPUT UNVERIFIED（未操作の本人確認があればHUMAN INPUT NOT PERFORMEDを併記）。positiveありの失敗と区別 |
| 後続と終了 | A/B成立後だけE2→positive serialize/実disk→同JVM同world B1回→deserialize/旧authority非復活→seal/必要時native cleanup1→保存/Quit/PID。timeoutなら未実施へ進まず通常安全終了。表示/入力検出だけでqueue完了にしない |
| 変更境界 | 次回の新root helper限定HUD/readonly operand/timeout/二段階壁・identity/metadata/initと限定offline compile/reobfが承認対象。製品/source/test/build.gradle/Jar/config/protocol/authority/Lease/gate/SP/外部原物不変。今回は3文書とbackupだけ |

次は上記一括範囲と本人W計2回の実行承認。追加artifact/製品仕様判断は不要、認証は実要求時だけ本人対応。#1 COMPLETE／#2 INCOMPLETE／残8、SAFE DESIGN PROVEN=NO、SOURCE DIMENSION TRANSITION VERIFIED=NO、ownership BLOCKED、P/T pending・購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、他gate維持。#3未開始。可逆クラフト増加の既知許容仕様を維持。

### queue #2 別画面配置・READY前timeout（2026-09-27 21:33 JST）

現行結果は[共通計画§14.45](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-dual-display-readiness-timeout) / [判定証拠](../build/verification/uom-production-input-final-20260927-211333/audit/reviewed-results.json)。§14.44手順の承認を得て実行し、期待値は変更していない。

| 受入項目 | 今回の判定 |
|---|---|
| 2画面/window/focus・手順説明 | HUMAN確認済み、MC windowed位置をCU確認。配置確認は入力PASSではない |
| prepare/E1/pause | 各1回、正常fresh/SP0・103/HP20、A最大前進1.199999988block、epoch1/seq1/count180→通常pause count21で503ms安定。限定成立 |
| HUD/input/movement A | READY実render前、goalから15.004秒でwatchdog。160tick/positive0/acceptedMoving0/XYZ不変。**INPUT UNVERIFIED**。HUMAN未操作の断定や製品movement FAILなし |
| resume/end/revoke | resume fresh Lease未確認。停止後count0/false/revokedを観測したが、client failure snapshotからserver判定APIを追加実行するobserver不備を検出、UNKNOWN混入の原因未確定。今回clean terminal PASSなし、旧PASS維持 |
| B/restore/E2/positive disk/same-JVM B/seal/cleanup | NOT RUN。B壁開放0、再読込0、cleanup0。記録リセット/再armなし |
| 正常終了/保全 | 全dimension保存、readonly HP20/SP0・103/P&M ON/source0/transient keyなし、title thread TERMINATED/Context closed、Quit/PID9208不在。今回Prism window閉鎖、既存background PID26748は保持。製品/旧run等309hash不変、helper限定build成功 |

次はpause/resumeの時間予算と失敗observerのREAD ONLY整理。A/B成功前にE2へ進まず、native180/TTL/timeout・入力/移動閾値は維持。新run/helper修正/再起動は今回停止後に追加していない。#1 COMPLETE／#2 INCOMPLETE／残8、#3 NOT STARTED。SAFE DESIGN PROVEN=NO、SOURCE DIMENSION TRANSITION VERIFIED=NO、ownership BLOCKED、P/T pending・購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、既存個別gateを維持。

### queue #2 READY直結順序とobserver境界 — DESIGN ONLY（2026-09-27 21:50 JST）

次回の手順正本は[共通計画§14.46](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-ready-first-thread-boundary-design)。旧§14.44/14.45の「次」は過去の計画/記録。今回は実行0、合格の追加なし。本人確認により前runは**READY表示0・HUMAN W A/Bとも0**。指示が出なかったのであり、押し忘れや製品movement FAILではない。

| 受入項目 | 次回の確認・停止条件（今回は未実行） |
|---|---|
| E1前preflight | 既存別画面windowed/説明・focus、run/world/player/source/Jar、prepare1/canonical、両壁・block同期、安全/HP/effects/静止/W released、observer heartbeat/記録I/Oを先に確認。PREPARING→ARMINGの描画ackもE1前。不一致はgoal0でSTOP |
| pause再演の分離 | 同一製品の§14.41/14.43限定PASSを保持し、入力測定ではpause/resumeを再演しない。pauseStable/revision>pauseRevisionというhelper前提だけをE1初回正規Grant/Lease整合へ置換。初回Leaseをresume試験PASSと表記しない |
| E1→A READY | 正規goal180→自然server tick/Grant→既存packet受信→実canMove/valid RETURN→条件確認→次render。CU Esc/F2/撮影/詳細解析を間に挟まず、helperだけで遷移。初回0.077秒の過去実測を次回の時間保証へ転記しない |
| READY_DISPLAYED_A | draw完了後の単発永続記録。run/world/player/source・wall/monotonic/gameTime・E1経過/count・Lease/session/epoch/seq/revision/期限・current refs/RETURN age・focus/HUD phaseを保存。条件成立だけの記録・人の視認・input PASSと区別 |
| observer | server自然RETURNを所有threadで採り、深いコピーのDTOを公開。clientはclient state・実RETURN・DTOだけを読む。snap/fail/handler等からpermission/reconcile/deny/valid/lookupの追加実行なし。停止snapshotの古さも記録。serverとclientのNULL/未観測をfalse成功へ変換しない |
| 非拒否とpacket | READY時のcurrent permission/matches実結果による非拒否条件と、実packetのguard RETURN/HEAD/RETURN受理・移動を分離。追加API評価で観測を作らず、実packet未観測のPASSなし |
| HUMAN A/B | 画面内A_READY/B_READYで各W約1秒→releaseの2操作のみ。Aの実key/forward・packet・server/client移動/収束・native終端前の解放、Bの独立normalInput/normalMoved/normalConvergedは既存期待値のまま |
| 時計・不足分類 | READY前はgoalから15秒、READY後は最初の実renderから15秒、native180自然終了は従来どおり上限。reset/延長なし。READY未描画と描画済み無入力を別分類、共にpositiveなしはINPUT UNVERIFIED。観測不備を製品FAILへ直結しない。Bの30/30/10秒も維持 |
| 後続・終了 | 両入力成立後のみE2→positive serialize/disk→同JVM B1回→非復活/seal/必要時cleanup1→通常保存/readonly/Quit/PID。STOP時は再arm/reset/修復/再入場なしで通常終了。追加HUMAN操作が必要なら別承認 |

次はこのhelper差分と一括実行の承認。追加artifact不足は未検出。今回は3文書/backupだけ、ゲーム/Prism/server/new run/helper作成/compile/test/W依頼0。#1 COMPLETE／#2 INCOMPLETE／残8、SAFE DESIGN PROVEN=NO、SOURCE DIMENSION TRANSITION VERIFIED=NO、ownership BLOCKED、P/T pending・購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、他gate・既存PASSを維持。

### queue #2 AUTOMATED OS INPUT実行結果（2026-09-27 22:28 JST）

詳細・唯一の現行手順/証拠集約は[共通計画§14.47](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-automated-os-input-a-pass-b-readiness-stop)。旧HUMAN計画は当時の履歴。今回の利用者判断でHUMAN物理W必須を廃止し、Windows通常OS入力→実Minecraft input→packet/guard→server/client移動を採用する。HUMAN INPUT=0、送信成功だけの入力PASS禁止。

| 受入項目 | 新run20260927-215939の結果 |
|---|---|
| helper / dry-run / PID-HWND / focus | PASS。専用MOD限定offline compile/reobf＋別Python SendInput、dry-run keyDown0。対象16148/1444684、windowed別画面・foreground照合 |
| A READY / OS keyDown-keyUp | PASS。E1+131.3433msで描画後receipt、W1002.648ms/各return1/exit0。pause再演0 |
| A actual input / movement / release | REAL CLIENT OS INPUT OBSERVED＋REAL CLIENT LIMITED PASS。positive20tick、1.199999988block、stage3 packet RETURN16/移動受理7/correction0、解放/収束3tick |
| native end/revoke | PASS。count0/global・dimensionfalse/Grant revoked/Leaseなし、witness1/1/1/faultfalse/UNKNOWNfalse |
| B READY | FAIL：helper readiness timeout。通常経路で新しいraw canMove RETURNを観測せず、150ms fresh/true条件が不成立、解放3tick・壁開放・READYへ進まず30秒STOP |
| B OS input / actual input / normal movement | 送信NOT RUN、actual/restore UNVERIFIED。599sample/positive0。OS配送失敗・製品movement FAILではない |
| E2 / positive serialize / positive disk | NOT RUN。保存されたsource0はpositive保存ではない |
| same-JVM B / deserialize / old authority非復活 | NOT RUN。再読込0 |
| seal / cleanup | NOT RUN、各0。追加操作/同run修復なし |
| normal save / readonly / Quit / PID | PASS。HP20/SP0・103/P&M ON/source0、全dimension保存、thread TERMINATED/context closed、Minecraft16148・今回Prism32524終了 |
| 不変・非混入 | PASS。171保護hash不変、元/配置10Jar一致、製品298958B/189entries/hash37E300EA…86592E3/protocol7不変。製品build/既存suite0 |

次は§14.47のB通常経路観測条件を整合し、新runの残工程を承認する。Aを未確認へ戻さず、stale raw falseを通常移動不可と判定しない。停止外の自然分岐/入力処理到達を観測する案は未実装・未実行であり、B移動/収束等の期待値を弱めるものではない。新runに進むまで停止、同run再arm/reset/再入場なし。#1 COMPLETE/#2 INCOMPLETE/残8、SAFE DESIGN=NO/SOURCE DIMENSION=NO/ownership BLOCKED、P/T pending・購入停止/SP保護・RC=NO・REAL2CLIENT=BLOCKED、他gateと既存PASS・可逆クラフト既知許容を維持。

### queue #2 B通常復帰・positive保存／退出helper STOP（2026-09-27 23:02 JST）

現行結果・詳細証拠は[共通計画§14.48](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-os-normal-positive-save-teardown-stop)／[実測集計](../build/verification/uom-production-os-input-20260927-224439/audit/reviewed-results.json)。旧§14.47 A限定PASSを保持、新run20260927-224439のAはB setup prerequisite1回のみ。

| 受入項目 | 今回の結果 |
|---|---|
| B READY | PASS。停止外の自然ClientTick/DTO・count0/global/dimensionfalse/G1 revoked/Grant・Leaseなし/静止・安全区画・block syncで開始。fresh raw canMove=true RETURNは通常分岐で生じず、事前条件から除去。追加API評価/値補正0 |
| OS B / actual input / normal movement | REAL CLIENT LIMITED PASS。W1004.4951ms・positive20tick・server/client1.00block・移動受理6・correction0。packet/自然guard/handlerで確認、OS配送だけのPASSではない |
| release / convergence / native end | PASS。キー解放/水平velocity0/位置差0を3tick。native完全終了/G1失効/UNKNOWNfalse/witness clean。古いA Lease流用なし |
| E2 / serialize / actual positive disk | PASS（限定）。新epoch2/sequence2、runtime179、serialize179/178、通常保存後の実disk178。NBT編集なし |
| same-JVM B / deserialize / non-revival | B再読込・deserialize NOT RUN。旧authority非復活UNVERIFIED。A停止時Context closed/maps0だけをreloadの代用にしない |
| seal / explicit cleanup | NOT RUN/各0。native unloadのuse(false)は自動終了処理として分離 |
| HUD COMPLETE/disabled | PASS。COMPLETE2秒→HUD_DISABLED、同session20frame描画0、画面も検証帯なし。failure後にSTOPPED再表示0。title UNVERIFIED/reload NOT RUNでありstale再表示全ケースPASSとはしない |
| OS終了 | PASS。A/B keyUp・watchdog join・supervisor exit0・target claim閉鎖・helper全PID不在。HUMAN0 |
| normal save / disk | PASS。全dimension保存、HP20/SP0・103/P&M ON/source178、transient keyなし |
| normal Quit / PID | 正常Quit FAIL。退出時helper health guard→world-null PauseScreen→DisconnectクリックでNPE/exit−1。Minecraft/今回Prism/Python PID不在を確認 |
| separation | PASS。製品171hash/配置10Jar不変、製品Jar298958B/189entries/hash37E300EA…86592E3/protocol7、検証HUD等非混入。製品build/旧suite0 |

次は**helperのlogout/stop時health観測とfail画面のlifecycle境界をREAD ONLYで確定**する。live player検証を消して期待値を緩めることと、logout後の無効な参照を検証対象から外すことは区別する。failureの個別operandは今回未採取（保存HP20であり死亡と断定しない）。修正/新runは別承認。B positive後の同runretry/reset/reload/再起動はしていない。B/E2成立を未実施へ戻さない。

#1 COMPLETE/#2 INCOMPLETE/残8、#3 NOT STARTED。SAFE DESIGN PROVEN=NO/SOURCE DIMENSION TRANSITION VERIFIED=NO/ownership BLOCKED、P/T pending・購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKEDと全個別gate維持。可逆クラフト増加は既知許容・バグ修正対象外。

### queue #2 退出監視・同JVM B残工程 — DESIGN ONLY（2026-09-28 18:50 JST）

次回設計正本：[共通計画§14.49](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-teardown-lifecycle-design)。旧§14.48のB通常復帰/E2/actual positive disk/HUD消去と旧A限定PASSは維持。今回は実行0、same-JVM B NOT RUN/非復活UNVERIFIED/seal NOT RUN/normal Quit FAILを更新しない。

| 次回受入 | PASSに必要な追加証拠／失敗境界 |
|---|---|
| health lifecycle | live時はHP20/max20/absorption0/effects空の全operand必須。正常退出tokenと実logout/世代を照合して測定終了。退出前異常はFAIL保持、予期しない参照消失はSTOP。nullだけで成功/無視しない |
| fail UI race | live失敗のclient実行時に現世代/現world/player/open connectionを再確認。teardown/null/title/closedではsetScreen0、receipt/logのみ。queue時はlive・実行時はunloadの境界も観測。観測機会がなければその枝は静的確認止まりとし、自然runの結果を故障注入試験へ転記しない |
| 正常退出・title1 | Save&Quit受理→logout/stop→全保存→thread TERMINATED→TitleScreen。world-null PauseScreen0、HUD title20frame draw0。保存済みHPで失敗operandを補完しない |
| 同JVM B | 同PID/JVM/world再読込1回、native positive deserialize、実参照の新旧分離、旧authority非復活、現Grant/Leaseなし、persisted positive保持、既存5 END。新世代observerの未観測をfalse成功にしない |
| seal・終了 | seal→必要時native cleanup最大1→通常SaveQuit/readonly/title2/Quit/正常exit/PID不在。再prepare/再arm/値修復0、製品・原物不変。HUD disabledでもmachine observerは継続 |

新run内A/B/E2は必要な開始状態のsetupだけ。HUMAN INPUT=0/AUTOMATED OS INPUT、native180/TTL/Lease/Grant/全移動閾値不変。旧run修復・既存suiteの再試験なし。helper作成・限定build・起動は次回承認まで行わない。#2 INCOMPLETE/残8、SAFE DESIGN=NO、SOURCE DIMENSION=NO、ownership BLOCKED、P/T pending/購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED。

### queue #2 lifecycle実測・positive reload／observer STOP（2026-09-28 19:26 JST）

詳細と実測値の正本は[共通計画§14.50](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-lifecycle-title-pass-reload-observer-stop)、[集計](../build/verification/uom-production-os-input-20260928-190153/audit/reviewed-results.json)。新run20260928-190153、HUMAN INPUT0。旧PASS/旧crash FAILは履歴として維持。

| 受入 | 今回結果 |
|---|---|
| helper限定compile/reobf/非混入 | PASS、測定前repair1/2、製品build/旧suite0。製品298958B/189entries/hash37E300EA…86592E3/protocol7不変 |
| setup A/B/E2 | LIMITED PASS。W1005.6369/1002.9504ms、移動1.199999988/1.00blocks、release収束、native end/revoke。E2 epoch2/sequence2、positive179→disk178 |
| lifecycle/TITLE1 | PASS。正常token/退出前health/expected teardown/全保存/thread終了/context closed/session.lock解放、world-null PauseScreen0、HUD20frame draw0、crash0 |
| same PID/JVM positive deserialize | LIMITED PASS。27808/JVM1790590219083、再読込1、同sourceのnative0→178、new actual refsのguard通過。capability actual-ref比較は未到達でUNVERIFIED |
| old authority全非復活・既存5 END | UNVERIFIED。server自然permission false170/true0、現Grant/origin/binding0等を部分観測したが、gen2 client valid RETURN待ちでEND0。native count0時helper guard FAIL、旧authority復活の製品反例とは認定しない |
| HUD reload/title2 count | 画面では検証HUDなし。当時のreceipt不足集計は後続§14.51で訂正：既存HUD-RELOAD-NO-DRAW.jsonは20frames/delta0。TITLE2専用receipt不足はUNVERIFIED、TITLE1から転記しない |
| seal/明示cleanup | NOT RUN/各0。native自然終了0を修復/再実行しない |
| 停止後保存・正常終了 | PASS。final health/HP20/SP0・103/P/M ON/source0/全保存/session.lock解放/実TITLE2/thread終了/context closed、19:21:02 Quit/Stopping!/対象5PID不在。成功用NORMAL_EXIT_ACCEPTED-2/TITLE2_SETTLEDは未発行。Java数値exit code未取得 |

自然に発生しないclient valid RETURNを観測目的で追加呼出しせず、旧genのRETURN/stale matches booleanを流用しない。5 END・positive保持・旧authority非利用の条件を緩和しない。次はこのclient待ち条件と実自然入口のREAD ONLY切り分け。新helper変更/新runは別承認、同run再試行なし。#1 COMPLETE/#2 INCOMPLETE/残8、#3未開始、全gate維持。

### queue #2 gen2自然観測・5 END — READ ONLY / DESIGN ONLY（2026-09-28 19:56 JST）

設計正本は[共通計画§14.51 A〜E](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-gen2-natural-observation-design)。§14.50のsetup A/B/E2/positive disk178/TITLE1/同PID-JVM再読込/native deserialize0→178/停止後正常Quitは維持。今回は新run/helper/compile/build/ゲーム/入力0。旧5 END0・B総合UNVERIFIED・seal0の結果は変えない。

| 残受入 | 次回承認後の証拠・境界 |
|---|---|
| gen2自然入口 | global=falseのnative client上流分岐でcanMove/valid未到達と確定。valid(false)必須を廃し、新gen actual LocalPlayer/level/listener/Connection/channelとLease不在のowner-thread DTOを使用。valid/matches未実行はUNOBSERVEDのまま |
| 5連続END | loaded sourceと本人/clientの自然ready後、製品Runtime END→SessionRegistry.tick→permissionの自然RETURNとreadonly状態をLOWESTで5連続採取。各END新ordinal/current actual refs/既存150ms freshness/positive count必須。詳細5行と到達性分類は§14.51 B。追加製品API呼出し・欠測行の選別/再開始なし |
| current Lease / old authority | present=false/null packet/cache refs0/Lease session・revision・sequence・expiry0、receive coverageとALLOW0、現Grant0/server新session/epoch・origin・binding0。old State/Context/session/Grant/Lease/callback再利用を実map/refs/counterで対応付ける。旧matched=true＋nano0は新RETURN証拠にしない |
| actual capability | gen1 serializeのold actual capとgen2 native deserialize引数cap/entity/level/serverを直ちに比較。自然生成済みnew State/Context/engineは後続bindで追加。identityHashCodeだけでは不足、観測のためlazy生成/source lookup禁止 |
| safe-side fault | 新engineのdeserialize-provenance-loss:POSITIVE/UNKNOWNを保持し、旧fault継承と区別。fault/permission false単独で非復活PASSにしない。人工clear/positive延長/180・TTL変更なし |
| seal / cleanup | §14.51 Eの全証拠＋helper failure0/修復0を確認してseal。成功証拠確定後だけ、既にcount0なら明示cleanup0、positiveなら既存native終了最大1。5 END前の消尽をcleanupで救済しない |
| HUD / successful final teardown | reload20frame draw0は旧runに実receiptがあったため既存証拠確認済み。次runは同処理を継続、TITLE2だけ世代別machine receiptを追加。NORMAL_EXIT_ACCEPTED-2/FINAL-LIVE-HEALTH-2/EXPECTED TEARDOWN/全保存/thread終了/Context closed/maps0/lock解放/TITLE2_SETTLED/draw0/readonly保存/Quit/PIDを別々に照合。STOP後正常Quitを成功seal後終了へ転記しない |

自然permission false170/true0は旧runのcurrent player/server-threadでの累積部分証拠。callerは静的帰属と実測を分け、次回自然RETURNのreadonly caller記録で補う。idle packet/guard falseをmovement成功やmatches falseへ転記しない。製品安全反例はFAIL、欠測/positive窓消尽はUNVERIFIED＋STOP。新runの実行・補助変更は別承認待ち。#1 COMPLETE/#2 INCOMPLETE/残8/#3未開始、SAFE DESIGN=NO/SOURCE DIMENSION=NO、全gate維持。

### queue #2 gen2自然観測helper — 限定実行結果（2026-09-28 20:57 JST）

run `20260928-203401`。[個別判定・原因・次の境界は共通計画§14.52](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-gen2-bind-uninitialized-cache-stop)、[原証拠集計](../build/verification/uom-production-os-input-20260928-203401/audit/reviewed-results.json)。§14.51の受入条件は変更しない。製品/原物/hash不変、旧run保全、HUMAN INPUT=0。

| 受入 | 今回結果 |
|---|---|
| helper / setup | helper限定offline compile/reobf PASS、測定前repair1/2。prepare1、A/B OS入力各1・E2 goalを含む合計2、移動/release/収束 LIMITED PASS。測定後修正/再試行0 |
| 保存positive / TITLE1 / 同JVM | runtime179/serialize179・178/実disk178、TITLE1/正常thread終了/maps0、PID38780の同world reload1 PASS |
| native deserialize / actual cap | 0→178、同UUID/type、old Entity/cap/level/server実`!=`比較PASS。比較をdeserialize直後へ前倒しした証拠を保持 |
| actual State/Context / passive client ready / Lease absent区間 | UNVERIFIED。初期login snapshotの未初期化native cacheでhelper STOP。identityHashCode表示を実比較成功に転記しない |
| END1〜5 / 総合非復活 / same-JVM B | END各NOT RUN・採取0、総合UNVERIFIED。自然permission false408/true0はSTOP後を含む部分観測だけで、current client DTO・actual隔離を束ねた5 ENDの代用不可 |
| seal / 明示cleanup / 成功final teardown | NOT RUN。seal0/cleanup0、STOP後正常退出を成功用teardownへ転記しない |
| STOP後保存/終了 / HUD | readonly HP20・P/M1ON・SP0/103・source0・transient混入0、全保存/lock解放/TITLE2/thread TERMINATED/Context closed/maps0/Quit/PID不在PASS。TITLE1/2各HUD20frames/draw0、reload20frames/draw0。Java数値exit code未取得 |

原因は `login→snap→dimension` の初期化時期を無視したhelper assert。製品安全性FAILとは認定せず、未初期化値をfalseへ置換・lazy生成・permission/valid追加評価で救済しない。次は初期snapshotを受入判定から分離する最小境界のREAD ONLY確認。helper変更/新runは別承認、旧run再試行禁止。#1 COMPLETE/#2 INCOMPLETE/残8/#3未開始、全gate維持。旧§14.50/51のPASS・失敗は過去のまま保全。

### queue #2 gen2 cache境界 — READ ONLY設計（2026-09-28 21:19 JST）

以下は当時の設計記録。client negative証拠の判断待ちは、末尾の§14.54方式LOCKで解消済み。

詳細正本は[共通計画§14.53 A〜E](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-gen2-native-cache-boundary-design)。今回は試験実行ではない。§14.52のhelper build/product separation/A/B/E2 setup/positive disk178/TITLE1/同PID-JVM reload1/native deserialize0→178/Entity・capability・level・server実参照分離/TITLE1・2 HUD/STOP後通常保存・Quit・PID終了を維持。旧false408/true0は部分観測のまま、5 ENDへ転記しない。

| 次回観測境界 | 設計・判定 |
|---|---|
| LOGIN/BINDING | raw値・未初期化理由・run/gen/actual refs/owner thread/nanoを残す診断記録。Boolean/State/Context/engine未生成だけではFAILにしない。受入に流用せず、lazy取得・authority追加評価0 |
| tri-state | UNINITIALIZED / OBSERVED_FALSE / OBSERVED_TRUEを分離。native contextは完成済みでも独立Boolean未観測ならINITIALIZED_UNOBSERVED。未観測のnull/default補完禁止。前2つの未観測状態はREADY/5 END不可 |
| client自然経路の限界 | native context constructor/attachは確認済み。global=falseではnative `isCurrentTS` とlogin packetが短絡される。tickで書かれるglobal&&dimensionの合成false、旧helperのkey!=null表示を独立dimension FALSEへ転記しない。自然RETURNが来るまで無限待機する案も採用しない |
| PASSIVE READY | gen2 current/new actual LocalPlayer・ClientLevel・listener・Connection・channel、正常canonical/health、Lease不在各operand、global/dimension自然観測FALSE、native初期化証拠、gen2時刻を独立照合。server値のclient転記0。bind完了とREADYは別 |
| actual authority隔離 | 自然生成後のState/Context/engineをreadonly field/mapから旧actualと比較。欠けていればPENDING、生成禁止。既存4種actual分離PASSから推定しない |
| controlled END1〜5 | READY nano以後の新しい自然permission RETURN=false＋actual隔離が揃う最初のeligible ENDから連続5。毎END新ordinal/current refs/client DTO・150ms freshness・positive count等§14.51を維持。gap/欠測/消尽でSTOP、取り直し/5行選別なし |
| timeout候補 | gen2開始から必須入口上限5秒。対象deserialize HEAD/server bindの早い方からREADY2秒/END5完了3秒、再bindで延長なし。旧runのbind群約0.923秒以内が根拠。count終端時刻は旧run未観測、178tick=8.9秒は20TPS仮定の参考だけ。count/180/TTL変更0 |
| failure分類 | 期限内未観測=NORMAL BINDING。期限超過/欠測=UNVERIFIED+STOP。予期しない実global/dimension true、Lease/ALLOW復活、旧authority実再利用=SAFETY FAIL。診断不足を製品FAILや成功へ補完しない |

次回差分は§14.53 Dの既存4helperファイル＋必要な非cancel native cache observer/Mixin設定だけ。今回は未作成・未build。client negative証拠として、native constructor/attach確認後の純粋native `isCurrentTS()` readonly照会を許容するかは利用者確認待ちで、今回の禁止条件から勝手に採用しない。追加artifact不足なし。境界確認と限定実行承認後にのみ1新runへ進み、旧runを再使用しない。

seal・cleanup・成功final teardownの受入は§14.51を維持。STOP時はcurrent client参照を再確認して通常pause→Save & Quit→readonly保存→Quit/対象process終了を行い、成功seal後の終了とは別記録。今回は新run/起動/OS入力/helper変更/compile/reobf/build/unit/check/GameTest全0。#1 COMPLETE/#2 INCOMPLETE/残8/#3未開始、SAFE DESIGN PROVEN=NO、SOURCE DIMENSION TRANSITION VERIFIED=NO、ownership BLOCKED、P/T pending/購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、他gate維持。

### queue #2 client独立dimension証拠 — 方式LOCK（2026-09-28 21:32 JST）

**CLIENT NEGATIVE EVIDENCE METHOD = LOCKED**。静的15条件・7命令・actual attach・最小変更一覧・一括順序の正本は[共通計画§14.54](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-client-negative-query-locked)。今回のnative query自体はNOT RUNであり、実client PASSを追加しない。§14.52のsetup/保存/reload/actual分離/HUD/STOP後終了PASSを維持し、false408件を5 ENDへ転記しない。

| 次回の限定受入 | 最終方式 |
|---|---|
| queryの前提 | 自然gen2 ClientLevel/context constructor完了・typed attach実参照一致・current LocalPlayer/level/listener/Connection/channel一致、canonical/health正常、Lease不在、client global観測false。未生成UNINITIALIZEDと生成済みBoolean未観測INITIALIZED_UNOBSERVEDを分離 |
| query最大1回 | `Observer.queryClientDimensionOnce()`内だけでnative `isCurrentTS:()Z` を直接照会。before/afterのactual context/key/global/Lease/packet/session/revision/sequence/expiry/各current refs不変。例外/true/差分でも再queryなし |
| 証拠分類 | READONLY_NATIVE_QUERY_FALSE/TRUE。origin tokenで自分のRETURNをNATURAL_RETURN_FALSE/TRUEへ二重計上しない。自然hookは維持し、自然RETURNやpacket到来をREADY必須にしない。合成flagやhelper null比較でnative結果を代用しない |
| READYと継続DTO | false結果・前後不変・全前提でREADY。固定query nanoと毎回のfresh sample nanoを分離し、同context/同key inputのactual一致をreadonlyに確認。入力/refs変更・矛盾を観測したら失効STOP、結果再生成なし。各ENDの150ms freshnessは新client DTOへ適用 |
| actual隔離/5 END | 自然生成後old/new State/Context/engineの実`!=`、未生成PENDING/生成禁止。READY後の新しい自然permission=false・actual隔離・fresh client DTO・positive count等§14.51を束ね、最初のeligible ENDから連続5。gap/欠測/消尽STOP、選別なし。180/TTL/閾値不変 |
| 判定・終了 | query trueは保存してREADY不可/安全側STOP。queryによるstate変更はFAIL/STOP。共有global等の並行変更とquery副作用を混同しない。正常seal/必要時cleanup最大1/成功final teardown/TITLE2 HUD/Quit・process、失敗時通常退出は既存§14.51どおり |

§14.53 Eの追加判断は解消。追加artifact不要。**次はverification-only helper最小修正＋限定offline compile/reobf＋1新run残工程一括実行の承認待ち**。追加READ ONLY工程を増やさず、今回は実行しない。server lazy getterとauthority系追加callは禁止継続。変更は3文書＋変更前backupのみ、製品/補助/旧証拠不変。#1 COMPLETE/#2 INCOMPLETE/残8/#3 NOT STARTED、SAFE DESIGN PROVEN=NO、SOURCE DIMENSION TRANSITION VERIFIED=NO、ownership BLOCKED、P/T pending/購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKEDと他gateを維持。

### 2026-09-28 22:10 JST — queue #2成功受入（run20260928-214421）

**#1/#2 COMPLETE・残7・#3 NOT STARTED**。[実結果の正本§14.55](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-queue2-completed-readonly-query)へ集約し、旧停止runの条件/FAILを書換えない。helper v20260928.214421のみoffline compile/reobf、製品Jarは298,958 bytes/189 entries/SHA256 `37E300EAA0663FDCDF445317C9F14F3BF13C839B21E4DD5679229B32386592E3`/protocol7のまま。製品build/既存suite再実行0。

- prepare1・A/B OS入力各1・E1/E2 native goal各1 → positive disk178 → TITLE1 → 同PID32192/JVM start1790599794624で同world reload1 → native deserialize0→178。
- old/new Entity/capability/level/server、自然生成State/Context/engine/session、client current refsをactual比較。LOGIN/PENDINGを受入falseへ変換しない。
- Render ownerのnative readonly queryは1回、`READONLY_NATIVE_QUERY_FALSE`、direct RETURN1・自然RETURN二重計上0、前後22項目不変 → PASSIVE READY。fresh DTOと固定query receiptは別時刻で扱う。
- 最初のeligible ENDから**5連続**（server ordinal2–6、source169/168/167/166/165、DTO age3.3005/8.3025/6.9166/5.4834/4.7365ms）。各current actual refs/HP20/canonical正常、Grant0/Lease absent/natural permission false/origin0/binding0、old authority非利用成立。gen2 permission false1170/true0/ALLOW送受信0も別途確認。
- old State/Context/engine/session/Grant/Lease/epoch-sequence/origin-binding/callback非復活を総合確認。seal1、cleanupはnative count自然0のためSKIPPED_ALREADY_ZERO（明示cleanup0）。
- NORMAL_EXIT_ACCEPTED-1/2・全dimension保存・actual server thread TERMINATED・Context/maps閉鎖・readonly disk/session.lock解放・TITLE1/2各20frame HUD draw0を確認。final disk0、Quit/対象5PID終了。旧STOP後の退出ではなく今回の成功final teardown。
- HUMAN INPUT0、測定後repair/retry0、state repair0、追加authority call0。測定前sandbox native DLL/OS窓列挙の制限は承認済み権限付き実行で解消（helper code repair0）。製品/配置/旧証拠差分0。

[照合結果](../build/verification/uom-production-os-input-20260928-214421/audit/reviewed-results.json)、[原journal](../build/verification/uom-production-os-input-20260928-214421/CLIENT/audit/journal.jsonl)、[保存証拠](../build/verification/uom-production-os-input-20260928-214421/audit/disk-FINAL-readonly.json)、[process終了](../build/verification/uom-production-os-input-20260928-214421/audit/process-final.json)。詳細hash/instance/原receipt/限界は共通計画§14.55。今回§28・#3以降・全体gateを解除しない。次は#3 FE6 secondaryの別承認、今回自動開始なし。


### 2026-09-28 22:57 JST — queue #3 FE6 production＋native自動統合

[実結果・変更境界・失敗履歴の正本§14.56](MASTERY_IMPLEMENTATION_PREPARATION.md#fe6-secondary-production-result)。**63/63＋追加8/8 PASS、71実行/70 unique cases**（版gate1重複）。[main](../build/verification/fe6-secondary-20260928-222200/focused-06/fe6-result.json) / [補足](../build/verification/fe6-secondary-20260928-222200/supplement-01/fe6-result.json) / [numeric/変換/最終Jar再照合](../build/verification/fe6-secondary-20260928-222200/audit/final-jar-audit.json)。最終製品 **306,882 bytes /198 entries / SHA-256 `1BBD49E6377DAF0F5C7C0EDED15A61F96261C80A1EA8C03471E634EFD6404A19`**。

| 判定単位 | 今回の成立範囲 |
|---|---|
| 6site | Dream2・Star AOE1・final1・counter1・skull1。全native付与分岐へ到達しP ON追加0、親/極意OFF・取得不足・pending/invalid・別UUID無資格で元duration/amp |
| numeric | DS/FE/negative delta/magic反射/通常skull/爆発のlayer/source/amount/countとON/OFF HP一致。Hard raw8とevent12を区別。効果後消去を新規抑止へ数えない |
| Star | fixture-only RNG0.25到達／0.75非到達、非UOM AOE、native direct UOM skip/非UOM4effect、owner/直撃UUIDのAOE除外 |
| skull | 実UOM発射/正式owner/衝突1、通常Wither/cow/null/別FE owner、Normal/Hard/Easy/Peaceful、native爆発/removed。異levelは述語対照で飛翔転送試験ではない |
| 既存state | 5経路とも既存ban/poison/luckのduration/amp、foreign NBT/modifier、canonical不変。Truth非条件。一般回復上限/全death/loot/全演出までの実測とはしない |
| 外部不在/版 | vanilla/TaCZ各60/60でFE不在ロード成功。2.7.20実ロード、未対応版string gate拒否。未知版実Jarの互換PASSではない |
| build/終了 | compile/build/unit/check、fixture限定build PASS。成功2 dedicatedとcore2 GameTestは通常保存/停止・全dimension保存・exit0。今回Java残存0。既存L2133/Cube49/Invader86/queue2再実行0 |

中途focused01–05のFAIL/NO_RESULTは§14.56の原因区分と原logを保持。後続成功へ書換えない。製品既存class/refmap不変の確認で必要回帰を限定し、旧試験を新Jarの実行件数に足さない。client配送/GUI/HUD/自然boss/秒単位effect経過は未観測。**#3 CLIENT FOLLOW-UP PENDING**：上記「実clientで追加する最小範囲」を別承認で進める。今回client helper作成/起動0、#4購入gate/SP開始0、RC=NO/REAL2CLIENT=BLOCKED等維持。


### 2026-09-28 23:38 JST — queue #3 Dream代表実client完了

[共通計画§14.57](MASTERY_IMPLEMENTATION_PREPARATION.md#fe6-client-completed)へ詳細を集約。run `20260928-231600`、固定製品hash `1BBD49E6377DAF0F5C7C0EDED15A61F96261C80A1EA8C03471E634EFD6404A19`不変。

- GUI生成前の自然湧き/自然回復OFF、Survival/Normal、閉鎖照明区画、fresh HP20/MAX20/delta0・空効果/装備/Curios。prepare1、P/M1両ON、credit/spent103・SP0。通常購入とは別。
- 製品GUI P OFF→ON、M保存ON保持、独立server/client canonical収束PASS。actual UOM Dream1、raw/Hurt/Damage **10.670565**、`fantasy_ending:ds_power`、cancel0、HP **20→9.329435**。2site decision true、通常浄化tick前forward0/Added0・毒/鈍足0。
- LocalPlayer連続3新規sample一致、通常HUD **9.3/20.0**。seal、own actor native cleanup、本人state不変。通常全dimension保存・readonly Health9.329435348510742/DeathTime0/両ON/SP0・103/pendingfalse、source保存不在、title/Quit/対象PID終了。再読込0（必須なし）。HUMAN0、GUI/F2等COMPUTER USE、攻撃/観測AUTOMATED。
- [実測結果](../build/verification/fe6-client-20260928-231600/audit/reviewed-results.json)、[原receipt/journal](../build/verification/fe6-client-20260928-231600/CLIENT/audit/journal.jsonl)、[保存照合](../build/verification/fe6-client-20260928-231600/audit/disk-readonly.json)。helper-only offline2build成功、2回目は測定前の安全読取追加。測定後helper変更/rebuild/retry/repair0、製品/既存suite変更・再実行0。
- BAN_HEALING等は既存native自動PASSを保持し、今回の実client PASSへ転記しない。§14.5/本§27/§14.56に独立BAN実clientを#3必須とする明記がないため追加必須化しない。**#3 COMPLETE／#1/#2維持／残6／#4 NOT STARTED**。次は#4正式readiness/購入SP取引の別承認。P/T pending・購入/SP保護、RC=NO、REAL2CLIENT=BLOCKED・他gate維持。

<a id="legacy-bonus-negative-regression"></a>
## 28. 旧高栄養食品ボーナス廃止 — 正式release受入計画

現行 2026-09-29 10:51 JST：#6の最低指定回帰（実Nutrition50・外部2effect保持・正式耐火tick対照）は§30でPASS。下記は2026-09-28の広い計画/当時のNOT RUN記録であり、N18/19/20全境界・全移行effect matrixまで実施済みとはしない。#8は別承認待ち。

更新: 2026-09-28 18:50 JST。仕様正本は[SPEC§12](SPEC.md#legacy-high-nutrition-retirement)。**RESOLVED / LOCKED / v3.0.0 FORMAL RELEASE REQUIRED**。今回source読取・計画だけ、下記新しい正式回帰は **NOT RUN**。既存managed effectの外部effect保全試験/PASSを取り消さず、その結果をこの新matrixへ転記しない。

| ケース | 開始条件・実経路 | 合格条件 |
|---|---|---|
| N18/N19/N20/N-high | 正常fresh/正常移行済み、対応skill未取得。自身に防御effectを持たない試験食品をNutrition18/19/20/非常に高い正整数で各通常use/Finishへ通す。高値は実装時に具体値を記録 | 旧Resistance/Fire Resistance/その他旧bonus grant=0。高Nutritionを理由に別timer/refresh0。食品自身のeffectと混同しない |
| skill OFF | 耐火/金剛取得済み＋OFF、同Nutrition条件 | 旧bonus0。既存skill effectが残る場合は新規grantと区別し、OFF後の自然期限を勝手に削除しない |
| skill ON | 正式取得＋ON、同栄養条件と非食事対照。managed effectの自然tickも観測 | skill自身の効果/ownershipだけ。Nutrition19を境に別timer/追加延長/二重grantが生じない。Root/True Root等の正式Nutrition仕様は保持 |
| external ownership | 既存Resistance/Fire Resistanceを他origin相当で事前付与。markerなし、より強い/長い、同値置換境界を分離 | 廃止処理によるremove/downgrade/短縮/amplifier低下0。時間の自然経過と短縮を混同しない。既存強外部effect非上書き・skill OFF非削除を維持 |
| migration/load | 旧v2由来か外部由来か証明できない既存effectを保持した、承認済み形式の新規隔離データ。通常deserialize/loginへ | 一括remove0、旧bonus refresh0、通常expire。正規skill有効時は自身のownershipだけ。既存原本world再利用/FOURTH BOOT追加なし |
| pending境界 | 編集済みpending等は既存購入停止/保護を保持。既存旧Lv fallbackとNutrition閾値経路を別分類 | Nutritionによる迂回なし。新LOCKの正式取得条件との不一致が判明したら別記録して最小修正判断へ進み、pendingを強制解除して通さない |

検証時はeffect ID/amplifier/duration/所有marker/発生call-site、取得/ON、Nutrition、tick時刻を対応付ける。単なる最終effect有無だけではrefresh不在を証明しない。生産変更が必要と判明した場合のみ承認された工程で最小修正、必要回帰、最終Jar照合を行う。現時点の残存経路削除は不要という静的結論であり、新規IMPLEMENTED/PASSではない。

固定queueは#2先行。#6食事/count工程の受入へ組込、#8最終build/regression/Jarで正式証拠を回収する候補。#7 Flightは別release blocker：他MOD由来Creative Flight喪失報告を保持し、Mekasuit/Avaritia/Fantasy Ending/他providerのownership保全を将来回帰候補とする。今回Flight修正/再現0、FIXEDとはしない。


<a id="pam-trees-acceptance"></a>
## 29. Pam Trees / vanilla tree-log recipe受入

### 29.0 過去結果 — 2026-09-29 08:29 JST（現行は§29.1）

仕様：[SPEC§14.1](SPEC.md#141-pams-harvestcraft-2---trees-harvest-duplication)。
**IMPLEMENTED / LIMITED AUTOMATED TESTED / #5 PENDING — supplemental environment STOP**。
旧SJ1の未LOCK/NOT IMPLEMENTEDは当時の履歴。現在の版/whitelist/vanilla provenance/logs/形式はLOCK済み。

| 受入項目 | 今回結果 |
|---|---|
| 原物/whitelist | exact hash/MOD ID/1.0.2一致。成熟mapping50/unique50・SPEC差分0。canonical→生成50JSONチェック一致 |
| present全50 | 各RecipeManager選択・shapeless/misc/材料2・assemble同ID/count2・実CraftingMenu PICKUP/消費1+1/二重delivery0。apple/paper/string通常NBTなし成立 |
| log境界 | oak/stripped/wood＋crimson/warped stem/hyphae/stripped全12代表の実item tag、別namespace fixture item minecraft:logs成立 |
| negatives | planks/stick/stone/非tag log名item、実sapling/pamapple block/roastedalmond、carrot、aggregate tag参加の別namespacefruit/crop不成立。追加占有slot不成立。stack count2は通常契約どおり1ずつ消費 |
| FPM | apple未取得2/OFF2/ON4、paper/stringはONでも2。材料二重消費なし。可逆増加許容を維持 |
| transaction | carried60+4→64、shift材料64+64→256/stack≤64・再取得0、満杯inventory+1/native drop3。別playerの4/2・input/result独立 |
| reload | present1回reload後に全50を再度manager/assemble/click/消費で確認。count50・重複0・出力不変。present合計138cases/1436assertions |
| absent | initial/reload全50IDなし・vanilla wheat→bread、103cases/106assertions。registry/parse/class/startup ERROR0 |
| supplemental | 実Crops1.0.3 agave/rice/strawberryの3負対照、native2x2逆配置/消費、ServerRecipeBook全50受理の5cases/66assertions成立。ただしCrops2recipeの未配置Food Core item参照ERRORにより正常ロード総合PASS不可 |
| build/core | compileJava/build/unit/check成功、専用fixture compile/reobf2回成功。vanilla/TaCZ各67/67。fixture件数をcoreへ合算しない |
| 保存/停止 | 新規dedicated3＋GameTest2、通常全dimension保存/stop/exit0・対象PID終了。補足ERROR/world/receipt保全 |
| Jar | 328,075 bytes/250 entries、SHA A76B4C9B5C22DB96CDCD8CDA36F2E8F6C8E0A3FC92405DC5FFA00DFEE9BEF001、protocol7。50標準recipe/全mod_loaded、既存class/Mixin/refmap不変、外部/fixture/ExampleMod非混入 |

証拠：[reviewed-results](../build/verification/pam-trees-20260929-080400/audit/reviewed-results.json)、[present](../build/verification/pam-trees-20260929-080400/present-01/pam-result.json)、
[absent](../build/verification/pam-trees-20260929-080400/absent-01/pam-result.json)、[補足log](../build/verification/pam-trees-20260929-080400/supplement-01/console.log)。
主体はAUTOMATED。実client/JEI UI/認証2client/他版/Botany Pots runtimeはNOT TESTED。

**STOP**：補足Cropsのtorch_cattail/cookingoil_x4_canola_x2がpamhc2foodcore:cookingoilitemを参照してparse ERROR2。
fixtureのPASSとは分離し、最終log監査で停止。原recipe削除・偽item・原Jar修正・推測依存追加・再試験をしない。
次は承認後に補足環境の不足だけ確認する。Trees主検証の成功を未実施へ戻さず、Crops/Food Coreを製品必須依存にしない。
#6 count/旧bonus正式回帰、#7 Flight、その他queueは未開始。RC=NO/REAL2CLIENT=BLOCKEDを維持。


### 29.1 現行：Pam条件付き49＋常時apple/cocoa2 — 2026-09-29 09:40 JST

仕様正本：[SPEC§14.1](SPEC.md#141-pams-harvestcraft-2---trees-harvest-duplication)。
**SPEC LOCKED / IMPLEMENTED / LIMITED TESTED / #5 COMPLETE**。
利用者の正式方針変更：[Compatibility§16](COMPATIBILITY_POLICY.md#16-pams-harvestcraft-2---trees--v300-release-required)。Food Healing ERROR0、必要Pam経路正常、unexpected external ERROR0、exact Food Core1.0.5 hash/recipe/result3件のみnon-blocking。既知3recipeはBROKEN/UNAVAILABLEのまま。別hash・別ID・件数不一致・新ERRORはSTOP。
今回[2026-09-29 09:40 JST再判定](MASTERY_IMPLEMENTATION_PREPARATION.md#pam-foodcore-known-upstream-acceptance)は既存log/receipt/原物/保存終了だけで成立し、ゲーム/build/補足run0。以下の数値は§14.60の既存実行証拠。
§29.0の138/103・absent0・全50条件付きと旧Crops STOPは当時の証拠として維持する。

| 受入 | 最終Jarの既存実測と現行判定 |
|---|---|
| mapping/generation | Pam whitelist50 byte不変、生成49conditional＋2unconditional＝51、旧条件付きapple IDなし、再生成diff0 |
| absent | 146cases/295assertions PASS。49 inactive、apple/cocoa2 active。双方の実RecipeManager match/assemble2/実CraftingMenu取出/1+1消費、通常vanilla craft、reload後も2、ERROR0 |
| present | 150cases/1482assertions PASS。49+2＝51、Pam harvest50/50 coverage。appleはvanilla ID、cocoa追加、reload後も51/結果同一/重複0 |
| FPM | apple未取得/OFF/ON=2/2/4、cocoa=2/2/2。paper/string2。材料は1ずつ、food判定コード変更なし |
| tag/negative | full item logsのvanilla/stripped/wood/Nether12代表＋modded tagged log成立。apple/cocoa各非log4種・extra slot不成立、non-whitelist収穫物排除 |
| transaction | normal64境界、shift64craft→256、追加delivery0、inventory不足drop3。補足carried63へ4取出は拒否/input不消費、carried空で1回4取得 |
| Food Core補足 | 7cases/75assertions成立：cookingoilitem＋旧Crops2recipe存在、agave/rice/strawberry負対照、2x2逆配置、ServerRecipeBook51、carried拒否。当時は自己recipe ERROR3で環境FAIL・runner exit1。今回exact3の出典/hash/resultを確認しknown-upstream非blocking、Food Healing0/unexpected0で現行受入成立 |
| build/core | compileJava/build/unit/check PASS、fixture compile/reobf2回PASS、vanilla/TaCZ各67/67。無関係Trial/L2/FE追加suite再実行なし |
| 保存/終了 | dedicated3＋GameTest2が通常全dimension保存/停止/exit0。停止runも保全。Game exit0と補足runner exit1は別指標 |
| Jar | 328,544 bytes/252 entries/SHA B472C1311FF6FDBA1304279AA7087E378628398E721D086E747BB61A9A0CD39D、protocol7。49/2分離、既存class/Mixin/refmap保持、外部Jar/class/fixture/generator/ExampleMod非混入 |

[総合判定](../build/verification/pam-vanilla-20260929-085041/audit/reviewed-results.json) / [補足log](../build/verification/pam-vanilla-20260929-085041/supplement-01/console.log) /
[Food Core静的適合と限界](../build/verification/pam-vanilla-20260929-085041/audit/foodcore-readonly-qualification.json)。
Food Core1.0.5の既知3recipeは未修正・使用不可。旧無条件ERROR0方針による§14.60 FAILと§14.61 artifact待ちは当時の履歴として保持し、原log・process・runner exit1は変更しない。
[機械分類](../backups/pam-acceptance-reclassification-20260929-093741/reclassification.json)はexact3/唯一の原Jar resource出典/元result、製品と配置物hash、7/75・通常save/stop/exit0を対応付けた。追加artifact待ちは解除、1.0.0切替・新run・runner修正不要。
現行受入で#5 COMPLETE。Food Core全機能/実client/JEI/認証2clientは未試験。#6–#9未開始、#7Flight blocker・全gate・可逆増加許容仕様維持。


<a id="nutrition-count-acceptance"></a>
## 30. Nutrition/Food-Level count・schema5受入 — 2026-09-29 10:51 JST

**AUTOMATED TESTED / queue #6 COMPLETE**。仕様：[SPEC§14.2](SPEC.md#142-nutrition--food-level-based-shokugi-count)。実装・全証拠・途中FAIL：[共通計画§14.63](MASTERY_IMPLEMENTATION_PREPARATION.md#nutrition-count-completed)。

| 受入 | 実測 |
|---|---|
| arithmetic/config/migration unit | Nutrition93 assertions＋既存unit PASS。default2000、旧100/200/250/400→×10、新明示値優先、実TOML pre-Forge→ForgeConfigSpec補完、再load不変。schema4の0/1/50/100/199・型不正/pending・long overflow/atomic拒否 |
| food/native/HP | 1/4/5/8各declared1回。食事use開始を確認（満腹含む）→native finish→Forge Finish。stack/NBT/最終item/container/満腹/高50/Satisfaction成功・失敗、HP×2維持、Root/Diversity/在庫二重0 |
| non-food | 10→15:+5、19→20:+1、full/saturation/decrease0、food同tick/1tick後/8tick後独立増分。+18でRoot蓄積/発動/予約0 |
| crossing/bonus | exact/multiple crossing、LongMAX境界・count/level/SP全部無変更のoverflow拒否。高50で旧Resistance/Fire0、外部amp/duration不変、正式skillはnative tick側だけ |
| core | **既存67＋追加8＝vanilla75/75、TaCZ75/75**。追加8を消して旧件数へ合わせない。userdev最終sourceであり配布Jar直接実clientではない |
| load/sync/disk | schema5 canonical NBT encode/replica一致、reload/clone/respawn/SP重複なし。実v2 Lv2/count35→350/raw35/SP2をlevel/player両payload・別JVM write/read、synthetic正常/不正5cases＋基本2playerの別JVM保持 |
| build/Jar/exit | compileJava/build/foodHealingUnitTest/check PASS。333,375 bytes / 257 entries / SHA-256 134FDE235FCAE2D81E06FE25A5512FE7469A38EE674D05AE18BF793B350232B5、protocol7。reobf/refmap/metadata/非混入PASS、各試験server通常save/stop/exit0・PID終了 |

[acceptance](../build/verification/nutrition-count-20260929-101705/audit/acceptance.log) / [TaCZ](../build/verification/nutrition-count-20260929-101705/audit/tacz-acceptance.log) / [実v2 write](../build/verification/nutrition-count-20260929-101705/audit/legacy-write.log) / [read](../build/verification/nutrition-count-20260929-101705/audit/legacy-read.log) / [synthetic write](../build/verification/nutrition-count-20260929-101705/audit/synthetic-write.log) / [read](../build/verification/nutrition-count-20260929-101705/audit/synthetic-read.log) / [総合](../build/verification/nutrition-count-20260929-101705/audit/reviewed-results.json)。
途中compile型/引数不備とfocus-02の既存fixture空stack再利用FAILは保持。実行主体AUTOMATED、実client/HUMANなし。既存Pam/Trial/L2/FE外部suiteは再実行0。独自backpack/全MOD互換、§28全matrix、旧world再起動は未試験であり今回PASSへ補完しない。次は#7の別承認待ち。RC=NO、REAL2CLIENT/ownership等の既存gateと可逆クラフト増加許容仕様を維持。


<a id="legacy-parity-future-acceptance"></a>
## 31. pre-#8既存skill parity — 当時の正式受入結果（履歴）

本文更新: **2026-09-29 21:42 JST**。**AUTOMATED TESTED / PRE-#8 LEGACY SKILL PARITY FIX COMPLETE**。正式値は[SKILL_TREE_SPEC§1](SKILL_TREE_SPEC.md#1-existing-skills-and-confirmed-sp)、実装・失敗履歴・Jar・終了証拠は[共通計画§14.69](MASTERY_IMPLEMENTATION_PREPARATION.md#legacy-skill-parity-fix-completed)。20:36時点のAUDIT ONLY/候補NOT RUNは[旧文書backup](../backups/legacy-parity-fix-20260929-211107/before/docs/TEST_PLAN.md)と§14.68に保持。過去84/84を当時から95/95だったようには書き換えない。

| 受入項目 | 最終実測・合格条件 |
|---|---|
| 定義/総額 | max3/3/9/1、費用5/5/5・10/20/30・100×9・20。実registry集計1,025/1,821一致。今回他費用不変 |
| 購入/GUI model | 全段階をregistered PurchaseSkillPacketで順番に購入、current→nextだけ、累計995。duplicate/stale/replay/先行Lv/max超過は消費0。unitで1SP不足/exact/Long.MAX/Spent overflow/invalid schema/pendingを拒否。初回ON・上位購入OFF保持。共通row controlsのcurrent/max、next cost、acquired/toggle、insufficient/maxedを全Lvで確認 |
| 保存/同期 | 通常AcquiredSkills levelの全段階NBT roundtrip。登録購入直後の実outbound sync payloadとcanonical一致、圧縮NBT再読込/同期packet encode/decode一致。実client画面・TCPとは区別 |
| 既存v3/移行 | 旧Lv1群・Spent13/Unspent1234/OFFを読込してbyte相当NBT保持、Gathering次購入のみ5SP追加。旧v2 Lv16/19/999から自動取得0、全SP返還/Spent0/backup/exactly once。移行コード不変 |
| Gathering final-list | 未取得/Lv1–3/OFF、base1→2/4/6・base3→6/12/18、64境界分割/NBT/metadata/source不変。対象clay等・非BLOCK/無関係loot不変。GLM既存class不変、独立LootTable再実行/追加spawn方式なし、Player死亡drop非対象 |
| native Fortune | diamond ore＋FortuneIII、同seedの正規LootTable生成を未取得/Lv3で比較。seed1/91は1→6、seed456/9999は**3→18**。生成数をsetter等で作らず実native結果を記録 |
| 耐久cap/入口 | chest/sword/pickaxe/bow/crossbow/shield/fishing rodの7標準item×direct hurt/hurtAndBreak。0/1/2/5/100/安全な大intに対し≤0不変/1→1/>1→1。未取得/OFF・non-damageable・null playerは陰性、NBT保持 |
| 不壊確率/回数 | Lv1 .90F・Lv2 .95F・Lv3 29F/30Fの直前floatはsave、exact equalityはfail。cap ON/OFF・7item・両入口でFH roll **exactly1**。rodの非破損確率比較量5、他は100。破損は別case |
| RNG/順序/enchant | 未取得/OFF/amount0/non-damageable/null player/Unbreakableで余分なFH roll0、不正roll不成功。100→cap1→FH0/1→vanilla enchant0/I/III。armor固有vanilla分岐もfloat/int呼出数で比較、FHがenchantを再実装しない |
| break | 残1/2/100、direct hurtの返却値とnative hurtAndBreakのcallback/stack shrink/count/damage reset/NBTを区別して確認。Forge Item.damageItemの呼出は実変換bytecodeで保持確認 |
| Pursuit | 全Lv0–9でextra数＝Lv、同source/current amount、outgoing高倍率の再適用0、OFF0、元iframe復元。native armor/ResistanceとPlayer50%軽減を通す（original4→2、extra2回の各native減少1、HP20→18）。死亡時1回で中断、意図的fixture例外後もfinally/guard回復 |
| core/build | 新parity unit **320 assertions**＋既存全unit（Flight88等）、compileJava/build/foodHealingUnitTest/check PASS。**vanilla95/95・TaCZ95/95**＝既存84保持＋11追加・削除0 |
| L2限定 | 実Hostility2.5.19 corrosion/erosionのnative攻撃、P既存18条件＋耐久6＝**24/24**。cap1/save0/fail10・73、numeric継続、source/canonical不変。L2完全免疫を要求しない。最終配布Jar直接、Forge47.4.0 |
| artifact/終了 | 350,858 bytes/269 entries、schema5/protocol7。metadata/Mixin/refmap/reobf/非混入PASS。最終3server通常save/stop/exit0、対象残存process0 |

証拠：[build/unit](../build/verification/legacy-parity-fix-20260929-211107/audit/build-final.log) / [vanilla](../build/verification/legacy-parity-fix-20260929-211107/audit/vanilla-final.log) / [TaCZ](../build/verification/legacy-parity-fix-20260929-211107/audit/tacz-final.log) / [L2](../build/verification/legacy-parity-fix-20260929-211107/l2-focused01/l2-result.json) / [監査](../build/verification/legacy-parity-fix-20260929-211107/audit/final-jar-audit.json) / [process](../build/verification/legacy-parity-fix-20260929-211107/audit/process-final.json)。

**限界**：AUTOMATEDのみ、HUMAN INPUT=0。GUI model/server transaction/sync codecを実画面PASSにしない。custom登録modded durability itemの既存安全fixtureがないため、その専用動的caseは未実行。標準入口の汎用実装/vanilla7item/実L2呼出を、energy/customcap/直接setDamageValueや全外部itemへ拡張しない。coreはuserdev最終source、L2は最終配布Jar。旧Flight/FE/Trial/Pam/SW/L2全133/実clientの再試験なし。過去PASS・途中FAILは保全、#8/#9 NOT STARTED。

<a id="quarrying-acceptance"></a>
## 32. Quarrying acceptance — AUTOMATED TESTED

本文更新: **2026-09-29 22:38 JST**。正式仕様[SPEC§21](SPEC.md#quarrying-lock)。同じplayer/toolでstone対deepslateのdestroy progressを比較し、未取得2倍差・ON同等・OFFnative・stone不変。plain diamond pick/EfficiencyV/HasteII/FatigueII通過。

pure RNG exact0/.005直前/.005/.01直前/.01/上側/NaN等、single draw1、exclusive iron/copper/none、Gathering none/OFF/1/2/3のqty1/1/2/4/6を両oreで確認。final listを二度通しても同一harvestで追加0回、native input stack不変/FortuneIII bonus非増幅。

実ServerPlayer.gameMode.destroyBlock→Forge harvest可否→native block removal→Block.playerDestroy→native table/GLM。stone/deepslate各seed64鉄6/seed18銅6＋native drop1、Silk/Fortuneを正規toolに付け比較。wrong tool/Creative/取消でbonus0。Fake/unowned/OFF/無harvest authority/爆発contextはRNG0。cobble/cobbled/polished/granite/diorite/andesite/tuff/diamond ore/deep iron ore、TaCZ実gunsmith tableは非対象。既存Gathering FortuneIII3→18等のparityを維持。

<a id="immovable-acceptance"></a>
## 33. Immovable acceptance — AUTOMATED TESTED

正式仕様[SPEC§22](SPEC.md#immovable-lock)。LivingDamage到達点のmob/projectile/explosion/fire/fall/magic/starvation/genericKill/out-of-world/genericを100→50、OFF/未取得100。Explosion5/Flame35/Kongo10/BaseDR25/Base+High12.5の独立合成、native hurt4でHP20→18。setHealth0/kill/discardはnative死亡/除去を妨げない。

標準direct knockback・Zombie native melee・Player KnockbackII＋ClientboundSetEntityMotionPacket・native Arrow PunchII・native Explosion＋hitPlayers＋ClientboundExplodePacketでON追加0/OFF nativeを確認。事前vector(.12,.23,-.17)を保持し、Player攻撃のnative server vector復元だけをpacket抑止の証拠にしない。通常move .5/jump/gravity/teleportを抑止せず、外部の意図的velocity setterも維持。Flight/abilities/exact provider/Root等の製品classは開始Jarとbyte一致。

実L2 poison native攻撃OFF3/ON1.5、poison付与継続・source/canonical不変。外部独自強制移動NOT GUARANTEED。実client操作/表示や全移動形式の網羅を今回PASSへ含めない。

<a id="skill-cost-rebalance-acceptance"></a>
## 34. Skill cost rebalance — AUTOMATED TESTED

全finite26定義のmaxLevel/段階費用/合計を実registryから確認：**1150/70/250/620/500、総額2590**。repeatable費用は5/10/10/5/20/20/2。全変更段階のexact/不足1/stale/replay/Spent、初回ON/上位OFF保持/通常NBT、new2のpending/invalid schema/不正ID/overflow/LongMAXをunitで確認。新2は登録Purchase/Toggle→実outbound canonical sync、圧縮NBT/Cloneへ接続。通常GUI描画・実client認証TCPはNOT RUN。

repeatable exact/不足1/複数購入/Spent/count/overflowを確認。HighDRは実L2導入版で6条件、未導入gateはcoreで維持。既取得Lv/Spent/未使用/point/toggleへの遡及請求・返金0、次購入だけ新費用。v2全額返還/自動取得0/backup/exactly onceを既存unitで維持しmigration製品classは不変。

最終結果：**新unit602 assertions＋既存parity320/Flight88等全unit PASS、compile/build/unit/check PASS、vanilla109/109・TaCZ109/109（既存95保持＋新14・削除0）、最終配布JarL2限定8/8**。TaCZ10段階各50SPの登録購入と不在拒否も確認。全最終server正常保存/停止/exit0、HUMAN INPUT=0。途中FAIL分類は[共通計画§14.70](MASTERY_IMPLEMENTATION_PREPARATION.md#skill-cost-rebalance-quarrying-immovable-completed)を参照。

配布Jar **360,970 bytes / 275 entries / SHA-256 36A79E98C8E854DA79B82811EBD5DD57FE01CF4F7EDFC93296031DDFD8A265BE**、schema5/protocol7・metadata/refmap/reobf/非混入PASS。coreは最終source/userdev、L2は配布Jar直接。[最終監査](../build/verification/skill-rebalance-20260929-220256/audit/final-jar-audit.json) / [source差分](../build/verification/skill-rebalance-20260929-220256/audit/implementation.diff) / [build](../build/verification/skill-rebalance-20260929-220256/audit/build-final.log) / [unit](../build/verification/skill-rebalance-20260929-220256/audit/unit-final.log) / [vanilla109](../build/verification/skill-rebalance-20260929-220256/audit/vanilla-final.log) / [TaCZ109](../build/verification/skill-rebalance-20260929-220256/audit/tacz-final.log) / [L2限定8](../build/verification/skill-rebalance-20260929-220256/l2-focused01/l2-result.json)。#1–#7 COMPLETE／#8/#9 NOT STARTED／RC=NO、次の利用者指示待ち。


<a id="localization-message-acceptance"></a>
## 35. Player-facing localization / messages — AUTOMATED TESTED

本文更新: **2026-09-29 23:31 JST**。[共通計画§14.71](MASTERY_IMPLEMENTATION_PREPARATION.md#player-facing-localization-cleanup)。言語/表示の受入でありqueue #8ではない。

| 受入 | 最終結果 |
|---|---|
| ja/en | JSON parse・重複key0・全key集合一致、literal call-siteと26skill/7stat動的参照に欠落0。採石/不動name/description両言語。書式引数・重要数値一致、技術語排除。全説明の意味と対象/条件を人手に代わる文章レビューで確認（実画面試験ではない） |
| 用語 | current lang「食義」9→0、現行Config UI説明2→0。日本語成功「購入しました」0、英語成功Purchased0。internal Shokugi/永続ID・キー変更0。legacy translation key削除0 |
| unit | **1,094 assertions PASS**。max1/leveled Lv1/leveled Lv2、翻訳名・実レベル、SP不足・replay・maxed・base成功/不足、失敗時SP/状態不変。既存cost602/parity320/Flight88等もPASS |
| 登録packet | 新2GameTestでskill7要求/base5要求を実dispatch。各結果の実ClientboundSystemChatPacketが1通、失敗時success0、name/levelとcanonical sync一致。Skill1+5+5=11SP、base5+10=15SP、replay/stale/不足/最大到達後は追加消費0 |
| 回帰/終了 | 最終source vanilla **111/111**・承認TaCZ **111/111**、旧109削除0＋2。compileJava/foodHealingUnitTest/build/check PASS。最終2server通常保存・stop・exit0・process終了。real client/HUMAN INPUT=0 |
| Jar | **362,505 bytes / 277 entries / SHA-256 D671E9A0E039CA62AF6CFDBEF6D160082097FF25FDF0823260469D9CE2062D58**。metadata/modId/version/schema5/protocol7、Mixin/refmap/reobf、非混入PASS。coreはuserdev source試験、配布Jarのclient描画PASSとはしない |

[最終監査](../build/verification/localization-20260929-230944/audit/final-jar-audit.json) / [source差分](../build/verification/localization-20260929-230944/audit/implementation.diff) / [build・unit・check](../build/verification/localization-20260929-230944/audit/build-final.log) / [vanilla111](../build/verification/localization-20260929-230944/audit/vanilla-final2.log) / [TaCZ111](../build/verification/localization-20260929-230944/audit/tacz-final2.log) / [終了process](../build/verification/localization-20260929-230944/audit/process-final.json)。中間表記差/修正・中間111は§14.71と原logへ分離。既存external suite・実clientを再試験せず、過去PASSは当時の範囲で維持。finite2590/効果/購入gate/SP保護不変。#8/#9 NOT STARTED、RC=NO。


<a id="release-command-verification-acceptance"></a>
## 36. Release cleanup / command & verification residue acceptance

> 以下はpre-#8当時の記録。後続の正式#8実client受入は[§37](#queue8-final-release-acceptance)を参照。

本文更新: **2026-09-30 00:07 JST**。[共通計画§14.72](MASTERY_IMPLEMENTATION_PREPARATION.md#release-command-verification-cleanup)。今回の静的/自動受入 **PASS**、queue #8の実client受入は **NOT RUN / 要別承認**。

| 今回の自動受入 | 証拠・判定 |
|---|---|
| 全command | before9→after6、A3/B3保持・C3削除・D/E0。実dispatcher path/suggestion/perm0/1/2、消したskill/list/detail/toggle・verificationのparse/execute陰性、SP/toggle/canonical/同期変更0 |
| 保持command | 全mutationはpermission2、本人のみ。malformed/long外/負数/extra target/console拒否、SP overflow拒否。既存SP原子性case維持、setlevel/countの0/37/Long.MAXで他field不変・exact sync1、level/countは正規server値/threshold・sync0。他player不変 |
| GUI機能継続 | Flight/TrueRoot/P/Tを旧command成功からnegative＋登録ToggleSkillPacketへ置換し、元の効果/予約期限/親子toggle/同期/SP条件保持。既存Trial fixtureの1経路も保守（compile/reobfのみ、外部49未再実行） |
| source/Jar | manual/READY HUD0→0、検証補助/FE patch/test metadata/外部class/GameTest/fixture0。2 native TimeStop Observer classは正式機構として保持。全gameplay class byte一致、5Mixin/refmap/schema5/protocol7不変 |
| localization | ja/en各54旧command専用key削除、130保持値不変。**815 assertions**、欠落0、26skill/7stat/食技/Shokugi/成功失敗文言・数値維持。旧1094からの減少内訳は§14.72。その他literal未参照keyは分類して保全 |
| 最終回帰 | compile/build/unit/check PASS、**vanilla113/113＋TaCZ113/113**。111ケース維持＋2、TrueRoot1件改名、削除0。初回P/T旧command3FAIL/遮蔽された爆発fixture1FAILは保全。fixtureの空間準備/到達率assertだけ修正し期待値を弱めていない |
| 保存/成果物 | 最終2server通常保存/停止/exit0、Java process0。Jar **358,640 bytes / 277 entries / SHA-256 4DF4954EC7082DA0276A0614EAEB871971F4A69538983EEED2F688BA4E3B5DEC**。coreは最終source/userdev、配布Jar実画面は未試験 |

[最終監査](../build/verification/release-cleanup-20260929-234805/audit/final-jar-audit.json) / [全command棚卸し](../build/verification/release-cleanup-20260929-234805/audit/command-inventory.json) / [source差分](../build/verification/release-cleanup-20260929-234805/audit/implementation.diff) / [build・unit・check](../build/verification/release-cleanup-20260929-234805/audit/build-final3.log) / [vanilla113](../build/verification/release-cleanup-20260929-234805/audit/vanilla-final2.log) / [TaCZ113](../build/verification/release-cleanup-20260929-234805/audit/tacz-final.log) / [終了process](../build/verification/release-cleanup-20260929-234805/audit/process-final.json)。

### queue #8で追加する正式実client受入（今回は未実施）

1. 利用者が承認した正式配布構成と最終Jarで実clientを起動する。verification helper/fixtureを通常構成へ入れない。
2. タイトル画面へ通常到達する。
3. verification-only metadata/fixture pack由来の警告画面がない（外部MOD全WARN0を意味しない）。
4. helper/debug/test HUDやrun status帯がない。
5. `MANUAL TEST PREPARING`、`ARMING E1`、A/B READY等の人力試験表示がない。
6. `/foodhealing `および`/foodhealing syokugi `の実Brigadier suggestionに削除skill/toggle・prepare/inspect/hit/seal/arm等がない。権限に応じた管理/診断6経路だけを確認。
7. Food Healing GUIが通常playerの取得・説明・ON/OFF・基礎強化の入口として動く。旧chat UIを要求しない。
8. ja/enの食技/Shokugi・26skill/7stat・習得/上昇・拒否理由の表示を確認。
9. 入場した場合は通常保存退出し、正常Quit・log/process終了を確認する。

今回の自動PASSでこれらを実画面PASSへ昇格しない。過去実client/L2/Trial/FE等の限定PASSは当時のまま維持。**#8/#9 NOT STARTED / RC=NO / HUMAN INPUT=0**。


<a id="queue8-final-release-acceptance"></a>
## 37. Queue #8 Final Release Acceptance — 部分PASS / startup BLOCKED

本文更新: **2026-09-30 01:23 JST**。対象run/最終Jar/修正履歴/完全な環境と証拠は[共通計画§14.73](MASTERY_IMPLEMENTATION_PREPARATION.md#queue8-final-release-verification)。**359,310 bytes / 277 entries / SHA-256 DF9F8B0736DC39EBDD2F1DEBA3D15A8348AEE262C85AC9B79F52F1CC62D603BB**。COMPUTER USEによる通常操作、AUTOMATED build/core/readonly、HUMAN INPUT=0。

| 受入 | 最終結果 | 証拠の範囲 |
|---|---|---|
| compileJava / unit / build / check | **PASS** | offline全46task実行。localization819/cost602/parity320/Flight88等、既存全unit維持。有限2590/schema5/protocol7/6command |
| vanilla / approved TaCZ | **114/114 / 114/114** | 既存113削除0＋不在/導入TaCZ基礎の登録購入/同期1ケース。replay/不正世代/overflow/他player不変。coreは最終source/userdev、配布Jar直接試験ではない |
| Profile A title / 警告 / HUD | **REAL CLIENT LIMITED PASS** | 製品だけの最小正式構成、通常title・blocking warningなし・helper HUDなし・numeric20/20 |
| command6 / inventory→S / GUI | **PASS** | 現行6suggestionのみ、旧chat/verification候補なし。level/count/progress/SP/skills/toggle/stat/理由へ到達 |
| 日本語26skill＋7stat | **PASS** | 全行選択・説明/費用/数値/名称、raw key/旧食義/旧名なし。max1/段階Lv2/base習得成功の自然文言を実chat確認 |
| English26skill＋7stat | **PASS** | 正式Language画面切替。全行・Shokugi・Learned/reached Lv.2/increased、数値と意味のja整合 |
| TaCZなしoptional lock / SP | **PASS（修正後）** | AmmoとTaCZ基礎、日英visible/locked・理由・SP消費0・point0。修正前の基礎SP2消費FAILは別world/log/NBT保全 |
| A save/reload | **PASS** | 初回SP984/spent16/defense1/耐火1OFF/満足感2ON→同新world1回通常再読込で保持。最終SP968/spent32/defense2/水中暗視1/採取2追加、HP20。GUI受信表示と通常server保存NBT一致。直接client cap observerなし |
| A Save & Quit→Quit | **PASS** | 2回通常全dimension保存、01:07:30 Stopping!、PID37632終了 |
| Profile B承認FE/EL exact構成 | **FAIL / RELEASE BLOCKER** | FE2.7.20 shaderのUniform→MUniform ClassCastException、title未到達。検証shader補助/fixture/observerなし。正式依存を無断追加しない |
| B verification警告画面なし / 通常Quit | **UNVERIFIED / NOT POSSIBLE** | title前crashのため正常起動/通常Quitへ転記不可。world0、Prism exit−1/process終了を確認 |
| Jar audit | **PASS** | metadata/5Mixin/refmap/reobf/277 entries、配置hash同一、外部class/Jar/helper/GameTest/fixture/ExampleMod混入0。正式TimeStop observer2は保持 |
| 外部full suite | **PRIOR VERIFIED / 今回再実行0** | class/該当resource/refmap・後続の限定provider/耐久/SP回帰を照合。現在のFE formal startup FAILと過去限定gameplay PASSを分離 |

途中の113/113と最終114/114を混ぜない。初回Profile Aの製品不具合、fixed vanilla1FAIL（fixture join通知）は保全。測定前traffic除去のみでresponse1条件・期待値を維持し、最終両core/新A2全体を再実行した。過去§36のNOT RUNは当時の履歴であり、現行実client受入はこの節。

**#8 BLOCKED — SUPPORTED FANTASY ENDING CLIENT STARTUP / #9 NOT STARTED / RC=NO / NOT READY FOR RC DECISION**。次はFE正式配布startup方針の利用者指示待ち。既存A/B/T/C・Trial/L2/FE gameplay、Flight/P/T PURCHASE READYを未実施/PENDINGへ戻さない。実2-clientは別account待ちBLOCKED。全版・全GunPack・全boss・全trait・全性能・WARN0を追加必須にしない。Food Production可逆増加は既知許容仕様・バグ修正対象外。

[最終Jar監査](../build/verification/release-final-client-20260930-002426/audit/final-jar-audit.json) / [source差分](../build/verification/release-final-client-20260930-002426/audit/implementation.diff) / [build・全unit・check](../build/verification/release-final-client-20260930-002426/audit/final-build-fixed2.log) / [vanilla114](../build/verification/release-final-client-20260930-002426/audit/vanilla-fixed2.log) / [TaCZ114](../build/verification/release-final-client-20260930-002426/audit/tacz-fixed.log) / [外部影響照合](../build/verification/release-final-client-20260930-002426/audit/external-impact-final.json) / [実画面記録](../build/verification/release-final-client-20260930-002426/audit/screens/events.jsonl) / [A保存](../build/verification/release-final-client-20260930-002426/audit/A2-final-save.json) / [Bクラッシュ](../build/verification/release-final-client-20260930-002426/audit/B2-crash.txt) / [終了process](../build/verification/release-final-client-20260930-002426/audit/process-final.json)


<a id="builtin-fe-shader-acceptance"></a>
## 38. Built-in FE Shader Compatibility / Profile B Acceptance — COMPLETE

本文更新: **2026-09-30 07:19 JST**。対象は[共通計画§14.74](MASTERY_IMPLEMENTATION_PREPARATION.md#queue8-fe-shader-blocker-resolution)、run20260930-065113、最終Jar **372,242 bytes / 286 entries / SHA-256 8B8A31FA308CD24C4A139D65FEF5818F1A2DABD8587A238D93974F787FFE10DD**。仕様/全入力/手順の重複は共通計画へ集約する。旧§37のB2 FAILを過去からPASSへ書き換えない。

| 受入条件 | 結果と証拠範囲 |
|---|---|
| promotion PRECHECK | **10/10 PASS**、製品編集前。旧patch source/class/config/gate/refmapと§14.19の3段階PASSを照合 |
| exact gate / 変換契約 | **11 profiles / 426 checks PASS**。CLIENT/MC1.20.1/Forge47.4.0/FE2.7.20 exacthashと5組だけ。call1/redirect1/delegate1/private fallback1、constructor/nativefactory維持、意味同一 |
| negative / 不在 | absent/wrong version/hash/MC/Forge/server/metadataは介入0。call0/2・descriptor/bridge/half-apply/competing redirectは拒否。FE/EL物理不在compile/earlyplugin linkage PASS、実不在clientはA3 |
| compileJava / 全unit / build / check | **PASS / exit0**。localization819/cost602/parity320/Flight88/Nutrition93/Mastery153/OPEN143/SW47/P150/T53/TimeStop9/data5000/Ammo10×1000（計10000）等、既存全群削除0 |
| core | **vanilla114/114・approved TaCZ114/114**。最終source/userdev47.2.0、新隔離world、通常save/stop/exit0。配布Jar直接coreとの混同なし |
| impact / 外部full suite | 旧全class/asset/lang・旧5config/refmap mapping byte同一。以前のL2/Trial/Invader/Pam/SW/Ammo/Flight限定証拠保持、今回full suite再実行0 |
| A3製品単体 | **REAL CLIENT LIMITED PASS**。最終配布Jarだけ、47.4.0 title/警告なし/HP20/20/GUI/command6/基礎TaCZ不在lock。SP0/point0、同world通常再読込1回・canonical完全一致・通常保存/Quit/PID終了 |
| A2表示維持 | 既存日英26skill/7stat/全メッセージは旧PASS＋lang/class同一で保持。A3に全件再撮影・全skill再購入を追加しない |
| B3正式FE構成 | **STARTUP PASS**。B2と同外部6原物、Food Healingだけ変更。standalone shader patch/observer/helper/fixture0、title到達/操作中安定、cast0/FH startup ERROR0/half-apply ERROR0 |
| B3通常visual smoke | **PASS**。新Creative Superflat、正規give ingot1、通常inventory/native描画、全面missing texture/render crash/shader fatalなし。内部GL22型/identity/programの新runtime測定は未実施 |
| warning区別 | title前compat/metadata/helper操作要求画面なし。world生成時のnative実験的設定確認は別。外部asset/tag/loot/biome/advancement ERROR15行・既存WARNは保全し、全外部無害とは判定しない |
| B3保存終了 | **PASS**。全5dimension保存→title→Quit、PID6368終了。readonly HP20/schema5/SP0/ingot1、再読込は範囲外 |
| 最終Jar / process | **PASS**。372242 bytes/286 entries、6config/refmap/reobf、A3/B3同hash、禁止混入0。全Java process0、HUMAN INPUT0、製品repair0 |

**#8 COMPLETE / #1–#8 COMPLETE / #9 NOT STARTED / RC=NO / READY FOR RC DECISION**。startup判定以外の[AUDIT F.1](AUDIT_REPORT.md#queue8-release-blocker-matrix)10分類は維持。今回受入後の#9/公開/改名/完全仕様書へ自動で進まない。REAL2CLIENT等の既存別gateと可逆クラフト既知許容仕様は維持。

[判定集計](../build/verification/fe-shader-production-20260930-065113/audit/reviewed-results.json) / [PRECHECK](../build/verification/fe-shader-production-20260930-065113/audit/promotion-precheck.json) / [static426](../build/verification/fe-shader-production-20260930-065113/audit/static-promotion-result.json) / [最終Jar監査](../build/verification/fe-shader-production-20260930-065113/audit/final-jar-audit.json) / [source差分](../build/verification/fe-shader-production-20260930-065113/audit/implementation.diff) / [build・全unit・check](../build/verification/fe-shader-production-20260930-065113/audit/build-final-local.log) / [vanilla114](../build/verification/fe-shader-production-20260930-065113/audit/vanilla-final.log) / [TaCZ114](../build/verification/fe-shader-production-20260930-065113/audit/tacz-final.log) / [実画面](../build/verification/fe-shader-production-20260930-065113/audit/screens/events.jsonl) / [A3保存](../build/verification/fe-shader-production-20260930-065113/audit/A3-final-save.json) / [B3保存](../build/verification/fe-shader-production-20260930-065113/audit/B3-final-save.json) / [終了process](../build/verification/fe-shader-production-20260930-065113/audit/process-final.json)


<a id="queue9-release-static-acceptance"></a>
## 39. Queue #9 release/static/document acceptance

本文更新: **2026-10-03 11:22 JST**。#1–#8 COMPLETE / #9 PARTIAL / RC=YES / LOCAL RELEASE ARTIFACT AND SPECIFICATION COMPLETE / GITHUB UPDATE BLOCKED。
これは既存証拠の照合とrelease包装であり、build/unit/GameTest/実clientを新規実行していない。

| 対象 | 今回の判定 |
|---|---|
| source/configuration freeze | #8 manifestに対してsrc274/build.gradle/gradle.properties/settings差分0 |
| 仕様 coverage | skills26/26、stats7/7（内部9ID）、commands6/6、finite1150/70/250/620/500=2590、repeatable5/10/10/5/20/20/2 |
| 永続/通信/互換 | schema5/protocol7、全保存key/Config、限定MOD/版/hash、未検証の区別を記録 |
| formal Jar | accepted build Jarからbyte copy。372,242 bytes/286 entries/`8B8A31FA308CD24C4A139D65FEF5818F1A2DABD8587A238D93974F787FFE10DD` |
| Jar構造 | metadata/version/modId、6Mixin/refmap/class/reobf、禁止混入0。正式CountObserver/UseObserver2本を保持 |
| RC | 既存11分類の限定証拠と整合、新blockerなし。RC=YES |
| GitHub | 前回（11:22 JST）の履歴: GitHub連携のrelease branch作成が403 Resource not accessible by integrationで拒否。branch/commit/main更新0、再試行0。前回読取時のmainは開始commitと同一。今回のremote状態は未確認。 source commit `NOT CREATED — GitHub integration write denied (403)`。外部Jar/build/world/log/cache/backups非公開 |
| 新規ゲーム試験 | 0。既存114×2/shader426/A3/B3および過去外部PASSを新規実行扱いにしない |

文書機械照合結果とGit tree manifestはlocal `build/release-audit/queue9-20261003-104305/`。公開用結果は[receipt](../release/v3.0.0/RELEASE_RECEIPT.md)。既存11分類・REAL2CLIENT・広いTimeStop等の限界は完全仕様書§31を維持。


### Git CLI publication resume — 2026-10-03 12:44 JST

Git CLI再開preflightで `<LOCAL_PATH>/food-healing-mod-main` は `.git` を持たず、root/remote/branch/HEAD/statusの5確認がすべてexit128 `not a git repository`。**STOP — LOCAL GIT REPOSITORY NOT FOUND**。
#8 freezeはsrc274とbuild3ファイル差分0、正式Jar372,242 bytes/286 entries/既存SHA-256完全一致、ExampleMod0。staged0/commit0/push0/retry0/HUMAN0。認証には未到達。remote URL/HEAD/現在mainは未確認であり、前回11:20 JSTのmain確認を今回の読戻しに転用しない。既存403は過去履歴として保全。
**#1–#8 COMPLETE / #9 PARTIAL / RC=YES / FORMAL RELEASE NOT COMPLETE**。
**次の1作業:** 現在のv3ファイルを保全したまま、このパスを正しい既存Git履歴へ安全に接続する方法の明示承認、または正しい既存checkoutの指定待ち。今回はgit init/clone/fetch/stage/commit/pushを行わず停止。GitHub integration APIへ戻らない。


<a id="queue9-git-publication-readback"></a>
## 40. Queue #9 Git publication / readback acceptance

本文更新: **2026-10-03 13:00 JST**。**#1–#9 COMPLETE / RC=YES / FOOD HEALING RPG v3.0.0 FORMAL RELEASE COMPLETE**。ゲーム試験ではなく公開整合の確認。

GitHub CLI publication: 正規fresh clone `.`、remote `https://github.com/leva3896/food-healing-mod.git`。開始main `59aedf1ccc512c531c73506be38ff9844dbf799b` は不変。branch `codex/release-v3.0.0`、Frozen Source Commit `d40b37d19ed71139dcb749bdf8aef3669ef53ca7` を通常pushし、mainへfast-forward反映、fetch/ls-remote/7ファイルの本文readback一致を確認。既存Git認証で成立、HUMAN INPUT=0、認証再試行0、force0、GitHub integration API0。
公開tree305ファイル（元からcopy302＋既存履歴3）。削除139はobsolete source2、参照TaCZ Jar1、外部MOD展開物136の分類済み対象だけ。元workspaceと履歴は保持。stage428件=add263/modify26/delete139、全copy/index blob一致、意図しない差分0、ExampleMod0、release Jar/生成証拠/credential混入0。最初のstageでignored削除pathが拒否され、tracked削除専用 `add -u` に1回修正して解消。sandbox内の最初のremote読取は接続不可、承認済み権限付き実行で1回再実行し成功。
#8 source274＋build3ファイル・Jar不変。build/test/game/repack0、既存PASSを再実行扱いにしない。tag/Release/binary upload/CurseForge0。今回の文書closureだけを後続commitにし、mainのreadback成功後に元workspaceへ同じ文書だけmirrorする。成果物とGitの識別は[Release Receipt](../release/v3.0.0/RELEASE_RECEIPT.md)を参照。

PASS: source freeze/hash・正式Jar・公開tree非混入・branch/main FF・remote readback。#8や外部suiteの再試験0。旧§39の403/local Git不在は当時の履歴として維持。
