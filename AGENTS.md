# AGENTS.md — Food Healing RPG v3.0.0

## 0. Mission

Food Healing v2.2.5 を、Minecraft 1.20.1 Forge 向け **Food Healing RPG v3.0.0** へ安全に大規模改修する。

最優先は「機能数」ではなく **互換性・セーブ安全性・サーバー権威・再現可能なテスト**。

## 1. Frozen scope

**LOCKED:** 2026-08-24 時点で新規ゲーム要素の追加は一旦凍結。

- この文書群に存在しない新スキル、新ステータス、新互換MOD、新UI機能を勝手に追加しない。
- 仕様に必要な内部クラス、Adapter、テスト、移行コード、デバッグ支援は追加してよい。
- 既存仕様に曖昧点がある場合は `AUDIT_REPORT.md` の `OPEN` を確認する。
- `OPEN` を推測で確定しない。
- ただし、OPEN が特定機能だけに関係する場合、他の確定部分の実装は止めない。

## 2. Product identity

- Display name: **Food Healing RPG**
- Mod ID: **`foodhealing`** を維持
- Target version: **v3.0.0**
- Minecraft: **1.20.1**
- Forge: v2.2.5 の 47.2.0 系を基準に、必要な範囲で互換性を維持
- 既存ワールド・他MOD・既存セーブへの破壊的変更を最小化

## 3. Priority order

### Priority 0 — compatibility / data safety
以下を他の機能より先に直す。

- 起動クラッシュ
- Optional MOD 未導入時クラッシュ
- セーブデータ消失
- End/Dimension/Respawn での進行度リセット
- AttributeModifier 重複
- 他MODの Attribute / Ability / Capability / Effect を上書き・削除
- HP HUD 消失・10ハートへの意図しない復帰
- 二重回復 / 二重食義 / 二重Food Diversity
- プレイヤー死亡ドロップ増殖
- multiplayer client/server 不一致

### Priority 1 — serious internal bugs

- SP消失
- SP二重取得 / スキル二重購入
- 負数・overflow・NaN・Infinity
- Nutrition予約/クールダウン二重処理
- skill toggle の同期不整合

### Priority 2 — frozen feature implementation

- Skill Tree
- Base stats
- Root/Heroics/high-difficulty skills
- food-production rewrite
- optional compatibility adapters

### Priority 3 — UI/QoL polish

## 4. Engineering rules

### MUST

- Server is authoritative for gameplay state.
- Client is presentation/input only unless Forge/Minecraft architecture requires otherwise.
- Persistent canonical data = progression/upgrades, NOT current AttributeModifier values.
- Use stable Food Healing-owned UUID/resource IDs for own modifiers.
- Rebuild own modifiers idempotently.
- Remove/update ONLY Food Healing-owned modifiers.
- Use immutable resource IDs for skills, e.g. `foodhealing:flight`; localized display names are not save keys.
- Use `long` or safer representation for Shokugi level/count/SP where unlimited progression may exceed int.
- Validate every SP transaction server-side.
- Optional-mod classes must not be loaded when the mod is absent.
- Run Gradle build after meaningful changes.
- Add automated tests/GameTests/unit tests where practical.
- Mark external-mod behavior not exercised in a real integration run as `NEEDS MANUAL/INTEGRATION TEST`.

### MUST NOT

- Do not globally mutate private vanilla Attribute limits by Reflection.
- Do not overwrite another mod's complete Attribute/Ability/Capability state.
- Do not set `mayfly=false` just because Food Healing flight is disabled.
- Do not identify own effects only by amplifier/duration and then remove arbitrary matching effects.
- Do not use display strings as persistent IDs.
- Do not use “food consumed → later refund copy” for Satisfaction.
- Do not use “craft completed → give copied bonus item” for Food Production Mastery.
- Do not re-run LootTable and spawn copies for Gathering if final-loot modification can be used.
- Do not scan a 151×151×151 area every tick for Truth Mastery.
- Do not `discard()`/direct-delete bosses to implement Break Realm.
- Do not globally cancel all damage or all death processing to implement Root/Purification Mastery.
- Do not make TaCZ a hard dependency.
- Do not couple ammo conservation to Satisfaction/Gathering/Unbreaking/Armor Mastery or any non-TaCZ skill.
- Do not silently remove legacy behavior found in v2.2.5; compare against `AUDIT_REPORT.md`.

## 5. Preferred mechanisms

Prefer, in order:

1. Vanilla/Forge official event and data mechanisms
2. Public APIs/interfaces of optional mods
3. Capability/Attachment/owned persistent data
4. Dedicated compatibility Adapter
5. Narrowly-scoped Mixin only when no safer mechanism exists

Avoid global Mixin/reflection/tick overwrites unless demonstrably necessary.

## 6. Required implementation phases

1. Baseline build + repository audit
2. Data model / migration scaffolding / persistence / network sync
3. GUI / keybind / numeric HP HUD
4. Food healing transaction & dedup / AlwaysEat / Food Diversity
5. Base stats & damage pipeline
6. Existing skills + Root + Heroics
7. Food production / loot / durability rewrites
8. Optional compatibility adapters
9. Full regression / integration / release candidate

Do not merge a risky optional compatibility hack into core code merely to finish a phase.

## 7. Build loop

For every meaningful unit of work:

1. Inspect relevant existing code.
2. State the intended change and affected invariants.
3. Implement the smallest coherent patch.
4. Run formatting/static checks if available.
5. Run `gradlew.bat build` on Windows or `./gradlew build` on Unix.
6. Run relevant automated tests.
7. Analyze failures.
8. Fix and repeat until build/tests pass or a genuine external/spec blocker remains.
9. Record manual integration tests still required.

## 8. Completion report for each phase

Report:

- changed files
- behavior implemented/fixed
- migration implications
- build/test commands and results
- external mods actually tested vs not tested
- remaining `OPEN` / `RELEASE BLOCKER`
- any compatibility risk introduced

## 9. No false completion

Do not say “compatible with X” merely because the project compiles.

Use:

- `AUTOMATED TESTED`
- `MANUAL TESTED`
- `INTEGRATION TESTED`
- `NOT YET TESTED`

as appropriate.


## 10. v3.x specification maintenance

The authoritative v3.x specification is [docs/FOOD_HEALING_RPG_V3_SPECIFICATION.md](docs/FOOD_HEALING_RPG_V3_SPECIFICATION.md).
For every v3.x behavior, cost, prerequisite, compatibility, persistence, protocol, or limitation change,
update that specification in the same work unit as the implementation. Code-only completion is not completion.
Keep current status and historical evidence separate; do not promote untested behavior to PASS.
For a future major version, create its own specification and retain the v3.x document and evidence.

## 11. Mandatory privacy and publication gate

- Never open account/credential stores, launcher accounts, browser cookies, private keys, `.env`, or auth caches to find credentials. Block sensitive filenames without reading contents.
- Never put private account names, real Player UUIDs, personal email, user/machine identifiers, or absolute local paths into public docs, code, reports, or handoffs. Use repo-relative paths or the placeholders in [PRIVACY_AND_SECRET_HYGIENE.md](docs/PRIVACY_AND_SECRET_HYGIENE.md), including `<MINECRAFT_ACCOUNT>`, `<PLAYER_UUID>`, and `<LOCAL_PRISM_INSTANCE>` for client evidence.
- Keep intentionally public repository identity, `foodhealing`, Java packages, and reviewed synthetic/owned UUIDs intact. Never rename product identities merely for privacy.
- On a suspected credential, do not echo, hash, copy, test, or transmit its value. Record only rule/type, file, line, surface, and severity; stop publication. Public/history credentials require a security remediation decision.
- Keep build reports, logs, worlds, screenshots, backups, and local launchers/runtimes private and ignored; never force-add them. Preserve local evidence rather than deleting it.
- Before every commit, push, PR, or release, run `python tools/privacy_guard.py --self-test`, `--tracked`, `--candidates`, and `--staged`. Any blocking hit or unreadable scope stops publication. Separately review the planned working/staged diff for privacy. Do not alter the stage to obtain PASS.
- A clean working tree does not clean public branches or Git history. Remote writes and history rewriting require explicit user authorization. See the privacy policy for scope and limitations.
