# Food Healing RPG v3.0.0

Food-driven RPG progression for Minecraft **1.20.1 Forge**. Eat to heal, gain Shokugi (食技) progress and SP, then choose skills and repeatable base upgrades.

**#1–#8 COMPLETE / #9 PARTIAL / RC=YES / LOCAL RELEASE ARTIFACT AND SPECIFICATION COMPLETE / GITHUB UPDATE BLOCKED** — updated 2026-10-03 11:22 JST.

- [Complete v3.x specification](docs/FOOD_HEALING_RPG_V3_SPECIFICATION.md) — all 26 registered skills, seven upgrade families, prices, exact behavior, migration and compatibility limits.
- [Release receipt](release/v3.0.0/RELEASE_RECEIPT.md) — accepted artifact identity and source commit.
- [Current status](docs/CODEX_STATUS.md#現在の要約) and [verification records](docs/TEST_PLAN.md).

## Install and use

Use Java17 and Minecraft1.20.1 Forge; **47.4.0** is the final client/optional-integration profile. The build baseline is47.2.0; this is not a guarantee for every Forge47 release.
Replace the old Food Healing Jar with **Food Healing RPG v3.0.0.jar**. Do not load both versions. Back up worlds and Config before upgrading. No external MOD is a hard Food Healing dependency.

The released artifact is kept separately from the Git source tree. Its canonical local release path is `release/v3.0.0/Food Healing RPG v3.0.0.jar`; this repository does not claim a GitHub binary download or new GitHub Release publication.
Artifact: **372,242 bytes / 286 ZIP entries**, SHA-256 `8B8A31FA308CD24C4A139D65FEF5818F1A2DABD8587A238D93974F787FFE10DD`. It is the unchanged Queue #8 client-verified Jar.

Open the normal inventory and press **S** (rebindable) for skills/base upgrades. Full-hunger food use is supported. Default2000 Nutrition/Food-Level units grant one Shokugi level and one SP. Japanese display uses 食技, English Shokugi; persisted IDs remain stable.

## Scope and limits

Purification Mastery / Truth Mastery and Flight are purchase-ready. Break Realm Mastery remains a visible implementation-pending node. Exact supported paths include L2 six traits, Trial1.4.9 Cube/Invader, FE2.7.20 secondary/limited TimeStop and built-in shader compatibility, TaCZ1.1.7-hotfix2, SuperbWarfare0.8.9.1 gunfire, Pam Trees1.0.2, and the documented Flight providers.
Support is limited to the versions/routes in the specification. REAL2CLIENT remains blocked pending a second account; all gunpacks, unknown flight providers, broad TimeStop proofs, and all-MOD compatibility are not established.
Food Production Mastery growth through reversible crafting is intentional; death-drop/replay/desync duplication is not.

## Source/build

Java17 / local Gradle8.1.1 / Forge official1.20.1 mappings. See [build.gradle](build.gradle) and specification§33 for exact compile-only local artifacts and cache prerequisites. External MOD Jars, extracted third-party content, runtimes, worlds and verification logs are not distributed here. A fresh checkout needs those separately authorized local prerequisites before an offline build; do not invent replacement APIs or dependencies.

For a configured environment: local Gradle with the existing `-g` cache and `--offline build foodHealingUnitTest check`. Tests and product artifacts are separate. Do not rerun games or alter existing worlds merely to read this source.

## Maintenance

Update the complete specification in the same unit as any v3.x implementation change. [AGENTS.md](AGENTS.md) defines the engineering rules. Historical v2 documents and dated PASS/FAIL records are retained as history, not current instructions. License: All Rights Reserved.
