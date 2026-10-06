# Food Healing RPG v3.0.0 — Release Receipt

本文更新 / Publication completion: **2026-10-03 13:00 JST**。Local artifact completion: **2026-10-03 11:22 JST**。

**QUEUE #9 COMPLETE / #1–#9 COMPLETE / RC=YES / FOOD HEALING RPG v3.0.0 FORMAL RELEASE COMPLETE**

## Artifact identity


| Field | Value |
|---|---|
| Formal filename | Food Healing RPG v3.0.0.jar |
| Local artifact | `<LOCAL_PATH>/release/v3.0.0/Food Healing RPG v3.0.0.jar` |
| Accepted source | `build/libs/foodhealing-3.0.0.jar`, Queue #8 run20260930-065113 |
| Bytes / ZIP entries | **372,242 / 286** |
| SHA-256 | `8B8A31FA308CD24C4A139D65FEF5818F1A2DABD8587A238D93974F787FFE10DD` |
| Copy/repack | byte-for-byte COPY; rebuild/repack0 |
| Identity | foodhealing / Food Healing RPG /3.0.0, MC1.20.1 Forge, Java17 |
| Schema / protocol | **5 / 7** |
| Mixin / refmap | six configs / foodhealing.refmap.json / reobf preserved |
| Forbidden contents | 0 external class/Jar, fixture/GameTest/ExampleMod/verification helper/HUD/standalone patch |
| Formal observer exception | CountObserver / UseObserver are product TimeStop mechanisms |


## Source and publication identity

| Field | Value |
|---|---|
| Repository | [leva3896/food-healing-mod](https://github.com/leva3896/food-healing-mod) |
| Read-only original | `<LOCAL_PATH>/` (no Git initialization) |
| Publication checkout | `.` |
| Starting remote main | `59aedf1ccc512c531c73506be38ff9844dbf799b` |
| Release branch | `codex/release-v3.0.0` — normal push and readback successful |
| Frozen v3.0.0 Source Commit | [`d40b37d19ed71139dcb749bdf8aef3669ef53ca7`](https://github.com/leva3896/food-healing-mod/commit/d40b37d19ed71139dcb749bdf8aef3669ef53ca7) |
| Verified main publication SHA (source commit) | `d40b37d19ed71139dcb749bdf8aef3669ef53ca7` — fast-forward from the starting main; fetched/read back |
| Final main HEAD | [`refs/heads/main`](https://github.com/leva3896/food-healing-mod/commits/main) — documentation closure descendant of the frozen commit |
| Closure commit identity | The commit containing this receipt's closure; exact final SHA is recorded in the external publication readback and final report, avoiding a self-referential commit hash |
| Main publication | SUCCESS — native Git CLI, fast-forward only; no force/history rewrite |
| Authentication | Existing Git authentication succeeded noninteractively; no new login or credential inspection |
| RC | YES, within the approved bounded release scope |

GitHub CLI publication: 正規fresh clone `.`、remote `https://github.com/leva3896/food-healing-mod.git`。開始main `59aedf1ccc512c531c73506be38ff9844dbf799b` は不変。branch `codex/release-v3.0.0`、Frozen Source Commit `d40b37d19ed71139dcb749bdf8aef3669ef53ca7` を通常pushし、mainへfast-forward反映、fetch/ls-remote/7ファイルの本文readback一致を確認。既存Git認証で成立、HUMAN INPUT=0、認証再試行0、force0、GitHub integration API0。
公開tree305ファイル（元からcopy302＋既存履歴3）。削除139はobsolete source2、参照TaCZ Jar1、外部MOD展開物136の分類済み対象だけ。元workspaceと履歴は保持。stage428件=add263/modify26/delete139、全copy/index blob一致、意図しない差分0、ExampleMod0、release Jar/生成証拠/credential混入0。最初のstageでignored削除pathが拒否され、tracked削除専用 `add -u` に1回修正して解消。sandbox内の最初のremote読取は接続不可、承認済み権限付き実行で1回再実行し成功。
#8 source274＋build3ファイル・Jar不変。build/test/game/repack0、既存PASSを再実行扱いにしない。tag/Release/binary upload/CurseForge0。今回の文書closureだけを後続commitにし、mainのreadback成功後に元workspaceへ同じ文書だけmirrorする。成果物とGitの識別は本receiptの表を参照。

The source commit identifies the frozen product. The later documentation-only commit closes status; it does not rebuild or modify the product. Final remote main SHA/readback evidence is held outside Git under the publication checkout's `build/release-audit/publication-recovery/`. The receipt does not fabricate its own not-yet-computable commit hash. No new tag, GitHub Release or binary upload is part of this publication.

## Evidence and scope


- Queue #8: [common plan§14.74](../../docs/MASTERY_IMPLEMENTATION_PREPARATION.md#queue8-fe-shader-blocker-resolution), [TEST_PLAN§38](../../docs/TEST_PLAN.md#builtin-fe-shader-acceptance).
- Inherited: local Gradle8.1.1/offline build/unit/check exit0, vanilla/TaCZ114/114 each, STATIC11profiles426, final-Jar A3 absence/GUI/save-reload, B3 supported FE startup/normal ingot visual/save-Quit. Normal process exit was recorded in #8.
- Prior external L2/Trial/FE6/Flight/Pam/SW/Ammo evidence retains its original version/hash/scope. No new full-suite PASS is claimed for a renamed Jar.
- Queue #9: [complete specification](../../docs/FOOD_HEALING_RPG_V3_SPECIFICATION.md), [static acceptance§39](../../docs/TEST_PLAN.md#queue9-release-static-acceptance). 26/26 skills,7/7 stats,6/6 commands,finite2590,costs/schema/protocol/localization/metadata and release-byte identity reconciled.
- Local detailed audit/backup: `build/release-audit/queue9-20261003-104305/` and `backups/queue9-20261003-104305/before/`. These are evidence locations, not public download links.
- Build/game/server/client/Prism/world rerun0; HUMAN INPUT0; external dependency download0; Security scan0. No credentials read or copied.


## Remaining boundaries

REAL2CLIENT=BLOCKED; all TaCZ/gunpack/MOD/GPU/version/performance coverage NOT TESTED.
Broad TimeStop SAFE DESIGN PROVEN=NO / SOURCE DIMENSION TRANSITION VERIFIED=NO / ownership BLOCKED; UOM/P vehicle policy not LOCKED.
Unknown Flight attribution is unavailable. P/T/Flight are purchase-ready with SP protection; Break Realm expansion is not implemented. Other held features/FOURTH BOOT remain separately gated.
Reversible-crafting Food Production growth is intended; unintended death/replay duplication is not.

This receipt records the release event. The complete specification is the maintained v3.x behavior contract.

## Historical publication blockers (resolved by approved fresh-clone route)

<details><summary>Previous receipt: integration403 and missing local Git (preserved)</summary>

# Food Healing RPG v3.0.0 — Release Receipt

本文更新: **2026-10-03 12:44 JST**。Local artifact completion: **2026-10-03 11:22 JST**

**#1–#8 COMPLETE / #9 PARTIAL / RC=YES / LOCAL RELEASE ARTIFACT AND SPECIFICATION COMPLETE / GITHUB UPDATE BLOCKED**

## Artifact identity

| Field | Value |
|---|---|
| Formal filename | Food Healing RPG v3.0.0.jar |
| Local artifact | `<LOCAL_PATH>/release/v3.0.0/Food Healing RPG v3.0.0.jar` |
| Accepted source | `build/libs/foodhealing-3.0.0.jar`, Queue #8 run20260930-065113 |
| Bytes / ZIP entries | **372,242 / 286** |
| SHA-256 | `8B8A31FA308CD24C4A139D65FEF5818F1A2DABD8587A238D93974F787FFE10DD` |
| Copy/repack | byte-for-byte COPY; rebuild/repack0 |
| Identity | foodhealing / Food Healing RPG /3.0.0, MC1.20.1 Forge, Java17 |
| Schema / protocol | **5 / 7** |
| Mixin / refmap | six configs / foodhealing.refmap.json / reobf preserved |
| Forbidden contents | 0 external class/Jar, fixture/GameTest/ExampleMod/verification helper/HUD/standalone patch |
| Formal observer exception | CountObserver / UseObserver are product TimeStop mechanisms |

Current publication blocker: **LOCAL GIT PREFLIGHT BLOCKED**. The Git CLI resume did not reach authentication or any remote operation; see the resume record below.

## Source and RC decision

Repository: [leva3896/food-healing-mod](https://github.com/leva3896/food-healing-mod), default branch main.
Planned release branch: `codex/release-v3.0.0` — NOT CREATED (403).
Starting main: `59aedf1ccc512c531c73506be38ff9844dbf799b` (v2.2.5).
Frozen v3.0.0 source commit: **`NOT CREATED — GitHub integration write denied (403)`**.
No source/release commit exists yet. Last observed remote main at the previous phase (2026-10-03 11:20 JST) was `59aedf1ccc512c531c73506be38ff9844dbf799b`; current remote main was not checked in this Git CLI attempt. A future successful source commit must be recorded here.
前回（11:22 JST）の履歴: GitHub連携のrelease branch作成が403 Resource not accessible by integrationで拒否。branch/commit/main更新0、再試行0。前回読取時のmainは開始commitと同一。今回のremote状態は未確認。

RC=YES applies to the user-approved bounded v3.0.0 scope. All11 [release blocker classifications](../../docs/AUDIT_REPORT.md#queue8-release-blocker-matrix) retain their evidence labels; no new blocker/product change was found.
Source274 files and build.gradle/gradle.properties/settings.gradle hash-match the Queue #8 freeze. Product/gameplay/Config/gates/SP changes0 in Queue #9.
The formal Jar is separate from the source tree. Prior repository contains old reference binaries, but no product-Jar release convention; no new binary distribution or GitHub Release workflow is invented. Existing v2 tags/history remain unchanged; no new tag/Release/CurseForge upload.

## Evidence and scope

- Queue #8: [common plan§14.74](../../docs/MASTERY_IMPLEMENTATION_PREPARATION.md#queue8-fe-shader-blocker-resolution), [TEST_PLAN§38](../../docs/TEST_PLAN.md#builtin-fe-shader-acceptance).
- Inherited: local Gradle8.1.1/offline build/unit/check exit0, vanilla/TaCZ114/114 each, STATIC11profiles426, final-Jar A3 absence/GUI/save-reload, B3 supported FE startup/normal ingot visual/save-Quit. Normal process exit was recorded in #8.
- Prior external L2/Trial/FE6/Flight/Pam/SW/Ammo evidence retains its original version/hash/scope. No new full-suite PASS is claimed for a renamed Jar.
- Queue #9: [complete specification](../../docs/FOOD_HEALING_RPG_V3_SPECIFICATION.md), [static acceptance§39](../../docs/TEST_PLAN.md#queue9-release-static-acceptance). 26/26 skills,7/7 stats,6/6 commands,finite2590,costs/schema/protocol/localization/metadata and release-byte identity reconciled.
- Local detailed audit/backup: `build/release-audit/queue9-20261003-104305/` and `backups/queue9-20261003-104305/before/`. These are evidence locations, not public download links.
- Build/game/server/client/Prism/world rerun0; HUMAN INPUT0; external dependency download0; Security scan0. No credentials read or copied.

## Remaining boundaries

REAL2CLIENT=BLOCKED; all TaCZ/gunpack/MOD/GPU/version/performance coverage NOT TESTED.
Broad TimeStop SAFE DESIGN PROVEN=NO / SOURCE DIMENSION TRANSITION VERIFIED=NO / ownership BLOCKED; UOM/P vehicle policy not LOCKED.
Unknown Flight attribution is unavailable. P/T/Flight are purchase-ready with SP protection; Break Realm expansion is not implemented. Other held features/FOURTH BOOT remain separately gated.
Reversible-crafting Food Production growth is intended; unintended death/replay duplication is not.

This receipt records the release event. The complete specification is the maintained v3.x behavior contract.


## GitHub write blocker

The existing connector could read repository metadata/tree, but `create_branch` was rejected with HTTP403 `Resource not accessible by integration`. Repository account permissions reported push/admin, which does not establish the integration installation's contents-write grant. One write attempt, zero retries. No branch/commit/main/tag/Release mutation succeeded. This is GitHub authorization failure, not an automatic approval-review rejection. No token/password/credential was searched, read or copied; HUMAN INPUT=0.

Final formal release is **NOT COMPLETE** until main contains the frozen source and documents. The local Jar/specification are complete and RC remains YES for the accepted scope. The earlier integration-write-access remedy is historical. The current instruction requires Git CLI only; its missing local repository prerequisite is recorded below.


### Git CLI publication resume — 2026-10-03 12:44 JST

Git CLI再開preflightで `<LOCAL_PATH>/` は `.git` を持たず、root/remote/branch/HEAD/statusの5確認がすべてexit128 `not a git repository`。**STOP — LOCAL GIT REPOSITORY NOT FOUND**。
#8 freezeはsrc274とbuild3ファイル差分0、正式Jar372,242 bytes/286 entries/既存SHA-256完全一致、ExampleMod0。staged0/commit0/push0/retry0/HUMAN0。認証には未到達。remote URL/HEAD/現在mainは未確認であり、前回11:20 JSTのmain確認を今回の読戻しに転用しない。既存403は過去履歴として保全。
**#1–#8 COMPLETE / #9 PARTIAL / RC=YES / FORMAL RELEASE NOT COMPLETE**。
**次の1作業:** 現在のv3ファイルを保全したまま、このパスを正しい既存Git履歴へ安全に接続する方法の明示承認、または正しい既存checkoutの指定待ち。今回はgit init/clone/fetch/stage/commit/pushを行わず停止。GitHub integration APIへ戻らない。

</details>

## Next action

v3.0.0 maintenance / future version work only upon the user's next explicit instruction. No next version, held feature or additional distribution is started.
