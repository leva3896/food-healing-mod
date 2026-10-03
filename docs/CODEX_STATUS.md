# Food Healing RPG v3.0.0 - Codex Status

最終更新: 2026-10-03 12:44 JST

## 現在の要約

**#1–#8 COMPLETE / #9 PARTIAL / RC=YES / LOCAL RELEASE ARTIFACT AND SPECIFICATION COMPLETE / GITHUB UPDATE BLOCKED**。
今回のGit CLI再開はLOCAL GIT PREFLIGHT BLOCKED。詳細は現行要約末尾とreceipt。利用者が明示承認したQueue #9の最終RC判定。現行仕様の正本は[完全仕様書](FOOD_HEALING_RPG_V3_SPECIFICATION.md)、成果物/commit/証拠は[Release Receipt](../release/v3.0.0/RELEASE_RECEIPT.md)。過去のSTOP/次/RC=NOは当時の記録であり現行指示ではない。

### 成果物と完了項目

- 正式Jar: `release/v3.0.0/Food Healing RPG v3.0.0.jar`、**372,242 bytes / 286 entries / SHA-256 8B8A31FA308CD24C4A139D65FEF5818F1A2DABD8587A238D93974F787FFE10DD**。#8検証済みJarとbyte-for-byte同一。build側を保全、再compile/repack/ゲーム試験0。
- source274ファイル・build.gradle/gradle.properties/settings.gradleは#8 freeze manifestと差分0。schema5/protocol7、metadata・6Mixin/refmap/reobf・非混入を正式名側で再読取。製品修正0、purchase/SP/外部state変更0。
- 完全仕様書34章: skills26/26、stats7/7（保存9ID）、commands6/6、有限SP2590、repeatable5/10/10/5/20/20/2、互換・移行・限界・maintenanceを照合。AGENTSにv3.x同時更新ルールを追加。README/changelog/current docsを整合。
- release blocker全11分類は[AUDIT F.1](AUDIT_REPORT.md#queue8-release-blocker-matrix)の限定受入を継承、新blockerなし。RC=YESはそのscope内の判断。広域安全証明や全MOD互換とは別。
- 前回（11:22 JST）の履歴: GitHub連携のrelease branch作成が403 Resource not accessible by integrationで拒否。branch/commit/main更新0、再試行0。前回読取時のmainは開始commitと同一。今回のremote状態は未確認。source commitは `NOT CREATED — GitHub integration write denied (403)`。製品Jar/外部Jar/old展開物/build/world/cache/log/backupsをsource treeへ含めない。GitHub Releases運用は未確認（既存0件）、新publication方式・CurseForge公開なし。旧v2 tags/履歴を保全。

### 仕様・実装・検証の現在一覧

| 項目 | 現行判定・証拠の範囲 |
|---|---|
| queue #1–#7 | COMPLETE維持。TimeStop限定指定受入、FE6、P/T通常購入、Pam51、Nutrition2000、Flight exact route |
| queue #8 | COMPLETE。最終source core vanilla/TaCZ各114/114、全unit/build/check、shader static11profiles/426、配布Jar直接A3/B3限定PASS。正常保存/再読込またはQuit/process終了 |
| P/T・Flight | PURCHASE READY、SP保護維持。P/T optional購入方針A。Break RealmだけIMPLEMENTATION_PENDING |
| L2/Trial | 6ID133・Cube49・Invader86の既存自動統合と限定実client完了を保持。今回再試験0 |
| True Root | 完成予約保持/再ON一度/非再発動、保護実測、activeUntil不変の既存PASS維持 |
| SW/Ammo | 既存通常銃/Ammo限定自動・実client結果保持。全版/全gunpack保証なし |
| Queue #9 | source/仕様/static/Jar実測。既存ゲームPASSを今回新規PASSへ転記しない。HUMAN INPUT=0、Security scan0 |

### 未完了・判断待ち・実行承認待ち

| 分類 | 維持する境界 |
|---|---|
| 今回の必須残件 | Git CLI再開preflightで `<LOCAL_PATH>/food-healing-mod-main` は `.git` を持たず、root/remote/branch/HEAD/statusの5確認がすべてexit128 `not a git repository`。**STOP — LOCAL GIT REPOSITORY NOT FOUND**。 今回指定のGit CLI経路の前提が未成立。旧API403は履歴。 |
| 実2-client | **REAL2CLIENT=BLOCKED — SECOND MINECRAFT ACCOUNT REQUIRED**。自動二者/実1-clientで代替しない |
| TimeStop広域 | **SAFE DESIGN PROVEN=NO / SOURCE DIMENSION TRANSITION VERIFIED=NO / BLOCKED - TIME STOP SOURCE OWNERSHIP**。限定#2完了を戻さない。UOM/P vehicle方針未LOCK |
| Flight一般化 | GENERIC FLIGHT PROVIDER ATTRIBUTION=NOT AVAILABLE。unknown foreign-after/別版/全組合せ未保証 |
| 個別未確認 | True Root部分蓄積/親RootOFF/死亡等の予約、Ammo heat購入時INCONCLUSIVE/厳密shot・内部遷移未観測/hot-gun SKIPPED/dedicated遅延、全MOD/性能/GPU等。完全仕様書§31参照 |
| 対応外 | 他L2 33ID/版、他boss/Hyperlink/Fumetsu/一般敵対MobEffect解除等はoptional backlog・artifact/判断待ち。release必須へ自動昇格しない |
| 個別開始gate | Break Realm expansion NOT IMPLEMENTED、Bulwark未着手、試作型機関弩は完全一致指示まで監査/設計/実装禁止、V3M0908 FOURTH BOOT NOT RUN / NOT AUTHORIZED |

食料生産の極意による可逆クラフト増加は既知許容仕様・バグ修正対象外。死亡drop/replay/desync等の意図しない重複は許容しない。原FAIL/対象Jar/残WARNは履歴保全。

### 次の1作業

**現在のv3ファイルを保全したまま、このパスを正しい既存Git履歴へ安全に接続する方法の明示承認、または正しい既存checkoutの指定待ち。今回はgit init/clone/fetch/stage/commit/pushを行わず停止。GitHub integration APIへ戻らない。**

### Git CLI publication resume — 2026-10-03 12:44 JST

Git CLI再開preflightで `<LOCAL_PATH>/food-healing-mod-main` は `.git` を持たず、root/remote/branch/HEAD/statusの5確認がすべてexit128 `not a git repository`。**STOP — LOCAL GIT REPOSITORY NOT FOUND**。
#8 freezeはsrc274とbuild3ファイル差分0、正式Jar372,242 bytes/286 entries/既存SHA-256完全一致、ExampleMod0。staged0/commit0/push0/retry0/HUMAN0。認証には未到達。remote URL/HEAD/現在mainは未確認であり、前回11:20 JSTのmain確認を今回の読戻しに転用しない。既存403は過去履歴として保全。
**#1–#8 COMPLETE / #9 PARTIAL / RC=YES / FORMAL RELEASE NOT COMPLETE**。
**次の1作業:** 現在のv3ファイルを保全したまま、このパスを正しい既存Git履歴へ安全に接続する方法の明示承認、または正しい既存checkoutの指定待ち。今回はgit init/clone/fetch/stage/commit/pushを行わず停止。GitHub integration APIへ戻らない。

読取証拠: [build/release-audit/queue9-20261003-104305/git-resume-20261003-124416/preflight-and-integrity.json](../build/release-audit/queue9-20261003-104305/git-resume-20261003-124416/preflight-and-integrity.json)。

### 今後の更新ルール

v3.xはsource変更と完全仕様書更新を同じ単位で行う。現在要約と履歴を分離し、完了項目を未完へ戻さない。完了報告はJarと最新Statusを併記する。

## 過去の履歴 — #9開始前の要約（2026-09-30 07:19 JST）

以下は#9前の証拠と当時の指示を保全したもの。現行状態は冒頭のみ。

<details><summary>#8完了時の旧集計（履歴）</summary>


**現行状態の参照先はこの節に一本化する。** 仕様決定、実装、自動試験、実クライアント試験は別の判定である。下部の過去履歴にある「現在」「最新」「次の作業」、停止・PID維持指示は当時の記録であり、現在の作業指示ではない。

### 成果物と今回の作業範囲
- **#8 COMPLETE / #1–#8 COMPLETE / #9 NOT STARTED / RC=NO / READY FOR RC DECISION**：[共通計画§14.74](MASTERY_IMPLEMENTATION_PREPARATION.md#queue8-fe-shader-blocker-resolution) / [TEST_PLAN§38](TEST_PLAN.md#builtin-fe-shader-acceptance) / [blocker全11分類](AUDIT_REPORT.md#queue8-release-blocker-matrix)。承認されたFE2.7.20限定shader互換を本体へ内蔵。PRECHECK10/10・STATIC11 profiles/426 checks・compile/build/全unit/check・vanilla/TaCZ各114/114 PASS。最終配布Jar直接のA3不在起動/GUI/保存再読込と、B3正式FE起動/通常ingot描画/保存終了をPASS。HUMAN INPUT=0。
- **今回の製品変更**：専用client-only FE shader config追加（5→6）と所有package内5 sourceだけ。親constructorのprivate parser呼出1か所をexact gate＋5組のreceiver/resourceへ限定。旧全product class/asset/言語・既存5config/refmap mappingはbyte同一、gameplay/schema5/protocol7/finite2590不変。製品repair cycle0。前§14.73で修正済みのTaCZ不在基礎購入拒否は維持し、A3でもlock/SP0/point0確認。
- **以前の完了範囲**：§14.73 A2の日英各26skill/7stat/成功・拒否と全外部限定PASSを維持。今回lang同一のため全面再撮影・full external suite再実行0。旧B2のtitle前ClassCastException FAILと旧shader補助§14.19は履歴として保持。今回の機械比較は[Jar/source影響照合](../build/verification/fe-shader-production-20260930-065113/audit/final-jar-audit.json)。以下の「過去」は当時の記録であり、現在の次作業は本節末尾のみ。

- **過去§14.52のrun `20260928-203401`：helper限定実装・A/B/E2 setup・positive disk178・TITLE1・同PID/JVM reload1・native deserialize0→178・Entity/capability/level/serverの実参照分離が成立。gen2初期login snapshotでSTOP。** `login→snap→dimension`がまだUNINITIALIZEDのnative cacheにBooleanを要求した検証補助の不備。受動client readyと5 ENDへ未到達（END1〜5各NOT RUN）、旧authority総合非復活/State・Context実比較はUNVERIFIED、seal/明示cleanup0。通常保存・readonly・TITLE2/HUD20frame draw0・Quit/対象5PID終了済み。停止後退出を成功final teardownへ転記しない。製品不変・HUMAN INPUT=0。[過去結果§14.52](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-gen2-bind-uninitialized-cache-stop)。当時は#1 COMPLETE/#2 INCOMPLETE/残8、#3未開始。現行判定は§14.55。
- **過去§14.50の結果：run `20260928-190153` はTITLE1・同PID/JVM positive deserialize成立後、client観測待ちでSTOP。旧runは保全済み。** helperがgen2の自然な`valid(false)` RETURNを待つ間にnative count178が自然に0へ進み、`native positive remains without repair`で停止。5 END=0、旧authority非復活の総合判定UNVERIFIED、seal/明示cleanup各0。停止後は通常保存→TITLE2→Quit、Minecraft/今回Prism/OS補助PID終了。製品不変・HUMAN INPUT=0。 [結果§14.50](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-lifecycle-title-pass-reload-observer-stop)。当時は#1 COMPLETE/#2 INCOMPLETE/残8、#3未開始。現行判定は§14.55。
- **旧高栄養食品ボーナス廃止LOCK維持**：[SPEC§12](SPEC.md#legacy-high-nutrition-retirement)。旧実行経路はABSENT、互換Configキーのみ。今回#6の最低指定回帰は実Nutrition50/外部Resistance・Fire保持/正規耐火tick対照でPASS。[TEST_PLAN§30](TEST_PLAN.md#nutrition-count-acceptance)。§28の全境界matrix/実clientまで実施済みとはしない。移行時の出所不明effect一括remove禁止を維持。

- **旧run20260927-224439の当時の結果**：B/E2/positive disk178成立、退出helper不備→world-null PauseScreen NPEでnormal Quit FAIL。旧証拠・world・helperを保全し再利用なし。本runでTITLE1/通常Quitが成立しても、この旧FAILを書き換えない。[履歴§14.48](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-os-normal-positive-save-teardown-stop)。

最新製品：[foodhealing-3.0.0.jar](../build/libs/foodhealing-3.0.0.jar) **372,242 bytes / 286 entries / SHA-256 8B8A31FA308CD24C4A139D65FEF5818F1A2DABD8587A238D93974F787FFE10DD**、protocol **7**／schema **5**。A3/B3配置・reobf出力・保全Jarと同一hash。metadata/6Mixin/refmap/reobf/非混入PASS。[判定集計](../build/verification/fe-shader-production-20260930-065113/audit/reviewed-results.json) / [PRECHECK](../build/verification/fe-shader-production-20260930-065113/audit/promotion-precheck.json) / [static426](../build/verification/fe-shader-production-20260930-065113/audit/static-promotion-result.json) / [最終Jar監査](../build/verification/fe-shader-production-20260930-065113/audit/final-jar-audit.json) / [source差分](../build/verification/fe-shader-production-20260930-065113/audit/implementation.diff) / [build・全unit・check](../build/verification/fe-shader-production-20260930-065113/audit/build-final-local.log) / [vanilla114](../build/verification/fe-shader-production-20260930-065113/audit/vanilla-final.log) / [TaCZ114](../build/verification/fe-shader-production-20260930-065113/audit/tacz-final.log) / [実画面](../build/verification/fe-shader-production-20260930-065113/audit/screens/events.jsonl) / [A3保存](../build/verification/fe-shader-production-20260930-065113/audit/A3-final-save.json) / [B3保存](../build/verification/fe-shader-production-20260930-065113/audit/B3-final-save.json) / [終了process](../build/verification/fe-shader-production-20260930-065113/audit/process-final.json)

- **queue #1 COMPLETE維持**：[§14.36](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-same-jvm-source-followup-completed)のsame-JVM isolation/source分離/cleanup/正常終了は完了済み。今回再実行しない。

- **正式release必須feature**：tree/log増産は[SPEC§14.1](SPEC.md#141-pams-harvestcraft-2---trees-harvest-duplication)のPam50・49+2=51・absent2/present51を維持し、[§14.62](MASTERY_IMPLEMENTATION_PREPARATION.md#pam-foodcore-known-upstream-acceptance)で**SPEC LOCKED / IMPLEMENTED / LIMITED TESTED / COMPLETE**。Nutrition/Food-Level countは§14.2正式LOCK、§14.63で#6 COMPLETE。

- **過去経緯：14:02：承認済みR1–R9の開始前照合でSTOP。既存reconnect helperのRUN/ROOTが旧runへ固定され、新runを既存外部設定では受け付けないことを実Jar bytecodeで確認**。[共通計画§14.20](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-reconnect-preflight-hardcoded-root) / [照合結果](../build/verification/uom-reconnect-20260921-135926/audit/preflight-result.json)。helper/patch/製品/外部6原物の全9hashは一致。source変更・再compile禁止に従い、新server/world/instance作成・起動前で停止。R1–R9各NOT RUN、auth/TCP UNVERIFIED。§14.19のshader3段階PASSは維持し再試験0。SAFE DESIGN PROVEN=NO / ownership BLOCKEDを維持。

- **過去経緯：13:38：独立FE client shader互換patchを作成し、STATIC-S PASS / STARTUP-S1 PASS / VISUAL-V1 cosmic GUI限定PASS。通常Quit・exit0・PID終了まで完了**。[共通計画§14.19](MASTERY_IMPLEMENTATION_PREPARATION.md#fe-shader-compat-execution-result) / [判定集計](../build/verification/fe-uniform-compat-20260921-125321/audit/reviewed-results.json)。run20260921-125321、5shader consumer/link完走、cosmic22uniform型・GL型・同一object一致、native draw/bind/apply/flushの3sampleとtime進行、GL照会38回すべて0。製品/承認6原物/reconnect helper/hash・gate不変。world/server/接続なし、R1–R9 NOT RUN。追加artifact不要。SAFE DESIGN PROVEN=NO / SOURCE DIMENSION TRANSITION VERIFIED=NO / ownership BLOCKEDを維持。

- **過去経緯：12:11：FE2.7.20 shader起動失敗のREAD ONLY原因切り分け完了（A：MUniform生成/cast契約不整合）**。[共通計画§14.17](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-shader-readonly-diagnosis) / [照合結果](../build/verification/shader-readonly-20260921-115707/reviewed-results.json)。実変換ShaderInstanceのprivate `m_173354_(JsonElement)`が通常Uniformを生成し、FE側の同名public parserへ分岐しない。constructor内ModelViewMat取得→FE bridge/getterのMUniform castで失敗。shader用Mixinは適用済みだが生成差替なし。追加必須artifactなし、他MOD競合/EndingLibrary版不整合/helper原因の証拠なし。修正・再起動・新試験0。R1–R9 NOT RUN、認証/TCP UNVERIFIED、prepare/UOM/native use/Grant0。製品・承認6原物・補助/gate不変、SAFE DESIGN PROVEN=NO / ownership BLOCKED継続。

- **過去経緯：11:52：承認されたreal reconnect補助を作成・限定offline compile/reobf。実clientはFantasy Ending 2.7.20のshader登録で起動失敗し、R1開始前に停止。R1–R9全てNOT RUN**。[実行結果](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-real-reconnect-startup-result) / [判定集計](../build/verification/uom-reconnect-20260921-112109/audit/reviewed-results.json)。新run `20260921-112109`、新server/worldと新Prism instanceだけ使用。serverは指定MODを実ロードし、通常save-all flush/stop・exit0。clientは`MShaderInstance.getUniform:118`で`Uniform -> MUniform`のClassCastException、Prism終了コード−1（正常Quitではない）。実TCP/login・S1/S2/S3・prepare・UOM/native開始・Grant・歩行・sealは0/未確認。R4同期不一致も今回未到達。製品229,494 bytes/150 entries/hash・source/test/build.gradle/gate不変。**SAFE DESIGN PROVEN=NO / ownership BLOCKED継続**。同client再起動・world再読込・外部MOD改変/取得なし。

- **過去経緯：10:27：一般source native dimension転送実到達／UOM NATIVE DIMENSION TRANSFER NOT SUPPORTED**。[共通計画§14.14](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-native-source-dimension-result) / [判定集計](../build/verification/uom-dimension-20260921-101349/audit/reviewed-results.json)。実portalでCowの同UUID/new instance・count80コピー、旧CHANGED_DIMENSION/count0を確認。単独sourceは旧停止終了、UOM残存＋FOREIGN転送はA停止/UNKNOWN継続でheld player/vehicle各1件DENY・XYZ不変。UOM本人の移動は実class両overloadがthis返却のため不成立。**要求全体のSOURCE DIMENSION TRANSITION VERIFIED=NO、SAFE DESIGN PROVEN=NO、ownership BLOCKED継続**。旧nullは履歴維持。新server通常save/stop/exit0、製品/gate不変、既存suite/実client再実行0。

- **過去経緯：22:52：PLAYER + VEHICLE SERVER DENY BOUNDARY VERIFIED（限定）**。[共通計画§14.13](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-vehicle-deny-boundary-verification) / [判定集計](../build/verification/uom-vehicle-deny-20260920-223504/audit/reviewed-results.json)。保持したvehicle packetはFOREIGN後、guardなしBoat2.5625→2.625、guardありXYZ不変。A–I等の16拒否で車両/本人/外部state不変、通常車両・元native true・vanilla ownership/速度/衝突/invalid対照成立。playerは指定連携3件のみ。UOM/P vehicleは未LOCK・対象packet0。**SAFE DESIGN PROVEN=NO、全体ownership BLOCKED継続**。run01の乗員準備FAILを保全し、guard不変のrun02で未完了3対照のみ補足。両server通常save/stop/exit0、製品/Jar/gate不変、旧suite/実client0。

- **過去経緯：22:26：UOM server移動境界をverification-only検証。player最低17枠は限定成立、handleMoveVehicle未被覆の実反例でBLOCKED継続**。[共通計画§14.12](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-server-movement-boundary-verification) / [判定集計](../build/verification/uom-movement-boundary-20260920-220541/audit/reviewed-results.json)。ALLOW中に保持した同packetは、FOREIGN参加後にguardなしX0.5→0.5625、guardあり0.5→0.5。current ALLOW/元native true/通常移動/teleport対照も成立。一方BoatはFOREIGN中も+0.0625、全体のSERVER MOVEMENT BOUNDARY VERIFIED=NO・SAFE DESIGN PROVEN=NO。指定の停止条件でvehicle guard未実装。専用補助のcompile/reobfだけ、01補助FAIL0件を保全し02で測定、両run通常save/stop/exit0。製品/Jar/gate不変、旧suite/実client0。

- **過去経緯：21:47：UOM TimeStopのverification-only所有台帳を新規dedicated 2 runで検証。BLOCKED - TIME STOP SOURCE OWNERSHIP継続**。[共通計画§14.11](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-timestop-ownership-design-verification) / [判定済み結果](../build/verification/uom-timestop-design-20260920-212237/audit/reviewed-results.json)。最低20枠は18 native LIMITED PASS・1台帳喪失相当・1dimension遷移未成立。FOREIGN追加でserver許可/native canMove=false後も移動packetがX+0.0625を反映する反例があり、canMove-only案は不採用。SAFE DESIGN PROVEN/readyではない。補助専用offline compile/reobfのみ、両server通常保存/stop/exit0。01の生成直後tick前提FAILと02の移動未成立を保全。製品/Jar/購入gate不変、FE製品保護統合0/NOT RUN、旧suite/実client再実行0。

- **過去経緯：19:25：問1=B／問2=P=A・T=AをLOCK、本体UOM攻撃経路を静的照合**。P最低範囲とTのL2 6 ID、将来ready化後のoptional購入方針は維持。当時は仕様LOCKのみ・購入停止維持。現在の通常購入は§14.58で完了。

- **過去経緯：17:38：同run `20260920-163342` のHUMAN通常歩行後、安全operandを個別確認してCを1回実測、seal・通常保存/readonly照合・Minecraft正常終了まで完了。C = REAL CLIENT LIMITED PASS**。[共通計画§13.11](MASTERY_IMPLEMENTATION_PREPARATION.md#l2-weakness-wither-client-c-completed-result) / [実測集計](../build/verification/l2-weakness-wither-client-20260920-163342/audit/completed-run-analysis.json)。HP14→11、3OFF、weakness/wither作用0、numeric3・同source空trait/marker2 ID保持、client/HUD11/20。prepare1/hit3、A/B/Tは開始状態用の準備だけで新規PASSへ加算しない。リセット後の新run/prepare/A/B/T/C再送なし。17:29全保存→readonly→17:30 Quit/PID1168終了。旧§13.10のGUI/A/B/T限定PASSと旧4停止run853ファイル・追加0を保全。再読込/範囲外/cleanup/修復なし。

- **過去経緯：20:34：weakness/witherのP本人新規付与防止＋T恒久無効化を限定実装・自動統合完了**。[共通計画§13](MASTERY_IMPLEMENTATION_PREPARATION.md#l2-weakness-wither-implementation)。L2 **133/133**、vanilla/TaCZ各 **60/60**、最終配布Jar Cube **49/49**・Invader **86/86**、build/unit/check成功。全試験通常保存/終了。20:34時点の新2 ID実clientはNOT RUN（後続のGUI/A/B/T・C/seal結果は上記）、購入gateは維持。

- Minecraft **1.20.1** / Forge基準 **47.2.0**（L2/Trialの実配布Jar統合は **47.4.0**） / Food Healing RPG **3.0.0** / MOD ID **foodhealing** / scope frozen / network protocol **7**。
- **過去成果物（2026-09-17対象。現在の配布Jarは冒頭参照）**: [build/libs/foodhealing-3.0.0.jar](../build/libs/foodhealing-3.0.0.jar)、**229,494 bytes /150 entries**。SHA-256 **5C1A716EEDE7AEB4449B36EEFFBDDA156176B16B60A1676229224AA3390DB327**。[現物検査](../build/verification/l2-weakness-wither-20260917-193800/audit/final-jar.json)でmetadata/Mixin/refmap/reobf/非混入と最終L2/Cube/Invader配置同hashを確認。20:34フェーズの最終コード/Jarで指定回帰を実行済み。17:38フェーズは実client補助だけをbuildし、製品再生成・既存自動suite再実行なし。旧3807DECC…Jar229,423 bytes/150 entriesは[開始前保全](../build/verification/l2-weakness-wither-20260917-193800/before/build/libs/foodhealing-3.0.0.jar)。旧95/86/49/core60とL2旧4 ID/侵略者実clientのPASSは当時のJarに限定して保持。8F4A…Cube実client・旧SW/True Rootも過去証拠を維持。
- **過去経緯：21:11：Trial1.4.9侵略者のSoul Protection解除抑止、大小Beam新規Soul作用抑止、侵略者Huge Beamの極大numericだけの例外を限定実装・自動検証完了**。[共通計画§11](MASTERY_IMPLEMENTATION_PREPARATION.md#trial-invader-result)。既存本人/版判定、購入停止、共通数値防御を維持。一般MobEffect敵対解除は該当実処理未特定、架空hookなし。当該フェーズは実client/Prismなし。[今回の記録](#evidence-trial-invader-20260915)。
- **過去経緯：19:45：観測補助だけを限定修正し、新run `20260916-185745` のL2実client確認を完了（REAL CLIENT LIMITED PASS）**。[共通計画§10.11](MASTERY_IMPLEMENTATION_PREPARATION.md#l2-client-completed-result)。prepare1回/SP0・603、通常GUI、native攻撃A/B/C各1回、製品T除去、全OFF・X90block範囲外/seal、同world通常再読込1回・同source再追跡・2回の通常保存・Minecraft正常終了を確認。[実測集計](../build/verification/l2-client-20260916-185745/audit/completed-run-analysis.json)。旧runのA総合UNVERIFIED/補助FAIL・B/T/C未実施は当時の履歴として保全。旧world/補助/証拠は不変。ただし旧instanceの`mmc-pack.json`にキャッシュキー1項目の差分があり、書換主体は未確認（[例外記録](../build/verification/l2-client-20260916-185745/audit/old-instance-metadata-exception.json)）。
- **過去経緯：21:51：新run `20260916-211828` のS2収束/H1 HugeをREAL CLIENT LIMITED PASS、seal・own actor cleanup・通常保存/readonly照合・Minecraft正常終了まで完了**。[共通計画§11.8](MASTERY_IMPLEMENTATION_PREPARATION.md#trial-invader-client-followup-result) / [実測集計](../build/verification/trial-invader-client-20260916-211828/audit/completed-run-analysis.json)。準備用Small1回はPASS件数に足さず、native Invader tick0。旧§11.7のF1/F2/S1 PASS・S2 UNVERIFIED/H1 NOT RUNと失敗証拠を保全。helperのみoffline compile/reobf、製品/購入gate不変。world再読込なし。
- **過去完了：Trial通常Cube確認は完了済み**：新run20260915-153300のA/B/C、両ON復帰時の非治療、seal/保存/同world再読込1回・正常終了は[限定PASSを維持](#evidence-trial-client-safe-20260915)。失敗した前runのA限定PASS・slime事故/死亡保存も保全。今回どちらのworldも再利用・再試験しない。

- **過去経緯：20:13までのGLOCK-17実client限定PASS・通常保存/終了は完了済み**。fresh食義Lv0/Lv1のHP差5.5/11.0、比2.0、通常reload・左クリック各1回の[証拠](#evidence-superbwarfare-client-20260913)を保持し、sessionを再開していない。下表の実client結果はこの完了済みフェーズを含む過去の証拠であり、今回の新規試験ではない。

- **過去経緯：22:31：購入readinessのREAD ONLY棚卸しを完了**。[共通計画§12](MASTERY_IMPLEMENTATION_PREPARATION.md#mastery-readiness-inventory)へ効果別残件・当時のL2登録39 ID（完了4/未対応35）の機構・A〜E分類を集約。固定pending、v3対応ID/版の最終一覧とoptional購入方針の未確定を分離。未対応全てをrelease blockerにしない。当時は実装/試験/ゲーム起動なし。

### 仕様・実装・検証の現在一覧

PASSは各対象Jar/範囲に限定。今回最終core114/114×2、unit819/cost602/parity320/Flight88等、A3 REAL CLIENT LIMITED PASS、B3 STARTUP + NORMAL PRODUCT VISUAL SMOKE PASS。既存A2全表示/外部限定PASSは当時の証拠を保持。server-side fixtureは実認証二者試験ではない。

| 項目 | 仕様決定 | 実装 | 自動試験 | 実クライアント・実環境 |
|---|---|---|---|---|
| 採取/不壊/追撃/防具の極意 | 4技能の段階・対象は§14.69、現在価格は§14.70でLOCK、[正式表](SKILL_TREE_SPEC.md#1-existing-skills-and-confirmed-sp) | **IMPLEMENTED / PARITY FIX COMPLETE**。標準hurt両経路で1回、既存安全化維持 | 当時unit320＋core各95/95・L2耐久24/24。現在coreは114/114。[§14.69](MASTERY_IMPLEMENTATION_PREPARATION.md#legacy-skill-parity-fix-completed) | 今回採取Lv1→2の英語GUI取得/保存を確認。他の効果を全件実client再試験したとはしない |
| Flight | exact-provider/版/hash限定・EL DENY優先LOCK、2SP/Lv1/前提なし | **IMPLEMENTED / PURCHASE READY / #7 COMPLETE**、Mek/Avaritia exact extension COMPLETE。schema5/protocol7・token不変 | unit88、core84/84×2、最終Jar実Avaritia23/23＋EL混在2/2。[§14.67](MASTERY_IMPLEMENTATION_PREPARATION.md#flight-avaritia-exact-provider-completed)。旧Mek28・FE18＋診断4維持 | 今回Flight説明/費用は実画面確認。飛行自体は再実行0。別版/別build・unknown provider・全混在lifecycleは未保証 |
| Nutrition/Food-Level食技count | default2000・Config変更可・比例移行LOCK（SPEC§14.2） | **IMPLEMENTED / #6 COMPLETE**、schema5・atomic進行・food/nonfood原因分離 | Nutrition93 assertions、#6時点core75/75×2（現在core114/114×2）、実v2 count35→350/raw35・別JVM保持PASS。[§14.63](MASTERY_IMPLEMENTATION_PREPARATION.md#nutrition-count-completed) | 今回NOT RUN。独自auto-feed/全MODへ拡張しない |
| 旧v2.2.5からの移行 | OPEN-01/05 **RESOLVED / LOCKED**。1:1 SP全返還・旧skill自動取得なし・raw保持 | 承認済みrespecを実装済み | fixture・別JVM保持・冪等性PASS | [初回migration/save](#evidence-migration-first)、[第二boot冪等性](#evidence-migration-second)、[第三boot限定Gameplay Smoke](#evidence-migration-third)は各記録範囲で完了。全MOD gameplayの保証ではない |
| Ammo Conservation | 既存Lv1〜10・各50SP・倍率/heat抑止LOCKを維持 | **TaCZ1.1.7-hotfix2限定で実装済み**。今回TaCZ専用gate/Adapter/順序変更なし | 今回TaCZ core GameTest **114/114**（既存113保持＋基礎購入optional境界1）。別suite13・Ammo/heat/bolt/reload/replay・別JVM write/readは[9/13の記録](#evidence-superbwarfare-20260913)を維持し、別suiteは今回は再実行なし | [旧15BE…Jarの配布直接](#evidence-direct-jar-20260913)と[9/9](#evidence-ammo-client-final)を保持。**今回JarのTaCZ実clientはNOT RUN**、REAL CLIENT CORE全条件PASSではない |
| 浄化の極意・真実の極意 | OPEN-02/03・Q1/Q2・Truth3取得/3ON・最低対応表・optional P=A/T=A LOCK | **P/T PURCHASE READY**。P100/T500・親取得のみ、親ON不要。異常/重複/SP保護を維持。効果Adapter不変 | [§14.58](MASTERY_IMPLEMENTATION_PREPARATION.md#mastery-production-purchase-ready) 新購入153 assertions、既存取得142/P150/T53、vanilla/TaCZ各67/67。登録packet購入・canonical同期を確認 | 旧Trial/L2/FE限定実client PASSを保持。今回P/Tの説明・費用は実画面確認、P/T自身のマウス購入・効果再試験は対象外。取得fixtureを通常GUI購入成功の根拠にしない |
| Fantasy Ending 終焉の守護者 | P最低必須/T対象外、LOCK維持 | TimeStop #2指定受入COMPLETE（§14.55）。FE6 IMPLEMENTED、購入readinessは§14.58で接続済み | FE6実配布Jar焦点63/63＋追加8/8（70 unique）、build/unit/check、vanilla/TaCZ60/60（§14.56） | #2の指定client/context再読込完了を維持。FE6 Dream単発・GUI/canonical/HP/HUD・seal/cleanup/正常保存終了は§14.57 REAL CLIENT LIMITED PASS、#3 COMPLETE。BAN_HEALING等へ転記しない。旧B2のshader起動FAILは保全。今回B3は内蔵shader互換だけで正式title/通常ingot描画/正常保存Quit PASS（§14.74）。FE戦闘/TimeStop等の再試験ではない |
| Trial Monolith Damage Cube | Q1新規Soul作用抑止・通常hurt維持、Q2双方取得/ONのみLOCK | **IMPLEMENTED（1.4.9の通常/強制加算）**。既存Cube class byteは不変 | **以前の承認Jar＋実Trial49/49**。今回新Jarで再実行なし。[receipt](../build/verification/trial-invader-20260915-203400/final-trial/trial-result.json) | **旧8F4A…JarでREAL CLIENT LIMITED PASS**：[A/B/C・両ON保持・seal/通常再読込](#evidence-trial-client-safe-20260915)。失敗前runも保全。今回Jar実clientはNOT RUN。自然boss/force/別版へ広げない |
| Trial Monolith Invader | [侵略者Hugeだけnumeric例外](COMPATIBILITY_POLICY.md#trial-invader-149)と本人保護はLOCK。一般buff解除は実処理特定時のみ | **LIMITED IMPLEMENTED**：敵対Soul Protection false解除1、大小Beam各Soul2、Huge numeric1のcall-site限定。同levelの正式Invader ownerと正常ServerPlayer両取得/ONのみ | **以前の承認JarでAUTOMATED INTEGRATION TESTED 86/86**。今回新Jarで再実行なし。[最終Jar＋実tick/beam/goal/閾値/対象外/保存](../build/verification/trial-invader-20260915-203400/final-invader/invader-result.json)。途中fixture0件FAIL3 runも保持 | **REAL CLIENT LIMITED PASS（限定5ケースを2 runの証拠で充足）**。旧[§11.7](MASTERY_IMPLEMENTATION_PREPARATION.md#trial-invader-client-partial-result)のF1/F2/S1、新[§11.8](MASTERY_IMPLEMENTATION_PREPARATION.md#trial-invader-client-followup-result)のS2/H1。新runでGUI・server/client2連続収束・HUD17/20、Huge callback/Float.MAX_VALUE redirect各1/hurt0、seal/cleanup/通常保存終了を確認。旧S2停止/cleanup失敗は当時のまま保全。自然boss/Beam描画・一般MobEffect未特定・他攻撃/別版/他player等は未確認。全効果完成ではない。購入readinessは別途§14.58で完了 |
| L2 Hostility 2.5.19 P/T | P本人条件・T3つ取得/ON・±75はLOCK。6 ID：poison/slowness/corrosion/erosion/weakness/wither | **LIMITED IMPLEMENTED**。購入readinessは§14.58で接続済み | **AUTOMATED INTEGRATION TESTED 133/133**。最終配布Jar・Tracker0.4.4、今回は再実行なし | **旧4 IDは§10.11、追加2 IDは旧§13.10のGUI/A/B/T＋今回[§13.11のC/seal・保存終了](MASTERY_IMPLEMENTATION_PREPARATION.md#l2-weakness-wither-client-c-completed-result)でREAL CLIENT LIMITED PASS**。HP14→11/3OFF/作用0・numeric3、server/client/HUD一致。prepare1/hit3、今回A/B/Tは準備のみ。再読込/範囲外は今回対象外、旧4停止run保全 |
| SuperbWarfare専用倍率 | **OPEN-04 RESOLVED / LOCKED**。旧式・経路境界の正本は[COMPATIBILITY_POLICY §14](COMPATIBILITY_POLICY.md#14-superbwarfare-legacy) | **IMPLEMENTED：0.8.9.1の実ProjectileEntity / gunfire系4 DamageTypeに限定しfresh/正常移行済みv3へ接続**。source Playerのcanonical食技Lv、専用→global→Heroics、pending分岐排他。爆発等へ拡張しない | **AUTOMATED INTEGRATION TESTED（限定経路）**。[最終Jar実物による17実攻撃ケース＋別枠3 synthetic negatives](../build/verification/superbwarfare-20260913-190540/actual-server-final/sw-result.json)、焦点unit47 assertions、vanilla/TaCZ57/57。受領・静的確認のみのPASSではない | **9/13当時のJarでREAL CLIENT LIMITED PASS**。[通常GLOCK-17のfresh Lv0/Lv1比較](#evidence-superbwarfare-client-20260913)：通常R reload・腰だめ照準/左クリック・命中・HP差5.5/11.0・GUI/HUD・通常保存終了。実移行済みworld/Lv200・全packet内部・全飛翔frame・別銃/搭乗兵器/全版・dedicatedはNOT TESTED。前回認証ERRORの全般解決は宣言しない |
| True Rootの既存動作 | 20SP・Root Lv5前提・Nutrition18・予約上限1回・ON時のwindow終了再発動、OFF時の新規予約/再発動禁止等は確定 | 通常Root/正当な連続予約の既存処理を維持 | [9/12実装時49/49](#evidence-true-root-implementation)で既存Root/toggle等も回帰。Lv別時間/cooldown・固定15秒窓・予約上限も確認 | [8/27切り分け](#evidence-root-toggle)等の既報範囲でPASSを保持。新仕様の証拠とは分離する |
| True Root完成済み予約の自身ON→OFF・期限終了後の再ON | **RETAIN / LOCKED**。親Root有効時の再ONによる予約1回消費・1回発動もLOCKED。正本は[SPEC section17](SPEC.md#17-true-root-completed-reservation-retention) | **IMPLEMENTED**。完成済みOFF予約保持・GUI packet/command共通処理を維持。今回Root/toggleコード変更なし | **AUTOMATED TESTED**。9/12の[新規8ケースを含む49/49](#evidence-true-root-implementation)、前フェーズ55/55を保持し、21:11のvanilla/TaCZ **60/60**にも既存Root回帰を含む（59/57は過去記録） | [9/12](#evidence-true-root-realclient)の保持・1回消費/300tick発動・消費後非再発動、[9/13](#evidence-true-root-protection-deadline)の **保護実測PASS / activeUntil UNCHANGED**を保持。HUMAN食事＋COMPUTER USE GUI＋AUTOMATED被弾/読取。通常終了済み。全境界/今回配布Jar精密実client/dedicated全条件PASSではない |
| 食料生産の極意 | [可逆クラフト増加は既知の許容仕様](SPEC.md#16-food-production-mastery-reversible-crafting)。死亡drop/replay/desync/意図しない重複付与は許容しない | 既存倍率処理を維持。許容仕様のための抑止コード追加なし | 既存vanilla crafting/overflow/各調理経路のPASSを維持。可逆cycleの新規実行PASSはない | normal/shift、2x2/3x3、OFF/未取得、材料/overflow等の既報vanilla PASSを維持。FD等は未統合 |
| 実1-client dedicated | 実2-clientとは別の試験範囲 | 既存構成による試験完了 | server-side自動fixtureとは別証拠 | [STEP1〜6](#evidence-single-client)の接続・GUI購入/toggle・食事・死亡/再接続保持・正常終了が記録範囲で完了 |

今回compileJava/build/foodHealingUnitTest/check成功、既存全unitとvanilla/TaCZ各114/114・両server保存/stop/exit0。A3はtitle/HUD/GUI/command6/TaCZ不在lock・同world通常再読込1回、B3は正式FE startup/通常ingot描画・通常保存Quit PASS。全Java process残留0。旧件数/途中FAILは履歴に保持。[最終判定・証拠](../build/verification/fe-shader-production-20260930-065113/audit/reviewed-results.json)

### 未完了・判断待ち・実行承認待ち

| 分類 | 残っている項目・必要条件 |
|---|---|
| 次の判断・実行承認待ち | **#8の指定blockerは解消、READY FOR RC DECISION**。#9（正式release判断・成果物仕上げ等）は利用者の次指示待ち。RC=NOのまま自動開始しない。旧B2 FAILは§14.73に保全、A3/B3の完了を残件へ戻さない |
| UOM TimeStopの判定範囲 / 残る全体gate | **queue #2の指定残工程は[§14.55](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-queue2-completed-readonly-query)でCOMPLETE**。READY/5 END/actual isolation/旧authority非復活/seal/成功終了を残件へ戻さない。限定受入の完了と広い設計判定を区別し、SAFE DESIGN PROVEN=NO / SOURCE DIMENSION TRANSITION VERIFIED=NO / ownership BLOCKED、UOM/P vehicle未LOCKは今回変更しない。これらを理由に既存PASSの再試験を自動追加しない |
| Pam's Trees必須feature | **SPEC LOCKED / IMPLEMENTED / LIMITED TESTED / #5 COMPLETE**。既存present150/absent146/core各67・補足7/75/正常保存終了を保持。Food Core1.0.5 exact3は既知upstream defect、非blocking。Food Healing0/unexpected0、追加artifact待ち解除。3recipe自体はBROKEN。[§14.62](MASTERY_IMPLEMENTATION_PREPARATION.md#pam-foodcore-known-upstream-acceptance) |
| True Rootの未検証範囲 | 直前9/13の**保護実測PASS / 発動中切替の絶対期限UNCHANGEDは完了済み**。16秒表示は既存証拠/表示・同期コードを読取調査済み：client時計との差を切上げる機構を確認したが、9/12画面と同時点のclient時計がなく、当時の原因・正確なずれは**未判明**。表示経路は期限を書き換えず、表示以外の新しい影響を示す証拠なし。旧Jarの配布直接基本確認は前フェーズで完了。True Rootの配布Jar精密確認と新仕様dedicated通信は今回対象外/NOT TESTED。保持/1回消費/消費後非再発動を未実施へ戻さない |
| True Rootの追加境界 | 完成前の部分蓄積、親Root自身のOFF、死亡/ログアウト/再起動等の予約方針は今回対象外として未決定を維持し、その境界を変更する際に確認する。**完成済み予約の保持選択と期限終了後の再ON使用時期は判断済み**で、追加質問待ちへ戻さない |
| SuperbWarfare残範囲 | **OPEN-04は解決済み**。SuperbWarfare本体未提供・通常弾のv3接続未着手という停止理由は解除。9/13の通常GLOCK-17最小実client比較は完了。残るのは実clientの別銃/全版・移行済みworld/Lv200・全弾道frame/packet内部・dedicated、搭乗兵器/別projectile/爆発等の範囲外経路。通常弾17ケース・同実clientの2命中をこれらへ転記しない |
| Ammo実clientの残範囲 | **B購入時heat解除 = INCONCLUSIVE / NATURAL COOLDOWN COMPLETED FIRST**、**Cの厳密physical shot計数 = 未観測**、**Eの全bolt/packet/prediction内部遷移 = 未観測**、**hot-gun持替え = SKIPPED**。旧Jarの配布直接Glock17/Lv10最小比較は前フェーズで完了。dedicated通信遅延はNOT TESTED。残る追加実行は別途承認が必要 |
| Ammo実clientの確認済み範囲との境界 | [H](#evidence-ammo-h)は自然過熱360/lock1をGUI pauseで保持→OFF→ON→world復帰・再射撃でheat0/lock0・弾薬保持を画面/保存確認した限定PASS。H事前条件とA/F/GはHUMAN、GUI/click等はCOMPUTER USE。**REAL CLIENT CORE全条件PASSは付与しない**。終了済みsessionの維持/再開指示はない |
| Multiplayer | **REAL 2-CLIENT = BLOCKED - SECOND MINECRAFT ACCOUNT REQUIRED**。実1-client STEP1〜6やserver-side二者fixtureでは代替しない。1-client試験の過去TIMEOUT/途中切断原因は未確定で、接続安定性全般の解決済みとはしない |
| 外部MOD・性能 | Trial1.4.9 Cube/侵略者限定経路とL2 2.5.19の6 IDはForge47.4.0の実Jar自動統合まで完了（新2 IDは旧GUI/A/B/T＋今回C/seal・通常保存終了を限定完了）。Trial47.2.0の本体API不足・他Trial/別版/自然boss/実client force等は既存未確認。L2代表4種の実client A/B/T/C・GUI/HUD/装備・同world保存再読込は§10.11で限定完了。L2の別版実物・L2＋Trial同時構成・別JVM再起動・TCP・長時間/多数entity/player負荷は未試験。AttributeFix/AutoLeveling/Traveler等のartifact待ち、FD/Sophisticated機能統合未試験と、**ALL TACZ / ALL GUNPACK COMPATIBILITY = NOT TESTED**を維持 |
| 移行の残範囲 | **V3M0908 FOURTH BOOT = NOT RUN / NOT AUTHORIZED**。既存原本/ZIP/pristine/R0908/V3M0908/cold copy/過去TaCZ worldを再利用しない。全MOD gameplay/ownership、既知ERRORの全機能影響、historical-wrapper認証組合せ・歴史的byte同一性、過去metadata差分原因は未確認。無効/編集済みpendingデータの修復方針も推測しない |
| Flight完了と他機能の個別開始条件 | Flightは[§14.65](MASTERY_IMPLEMENTATION_PREPARATION.md#flight-exact-provider-completed)の**exact-provider最低正式routeで#7 COMPLETE/PURCHASE READY**。FE胸装備native解除なし、未知provider/別版/全lifecycleは範囲外。承認Mekanism10.4.16原物MekaSuitは[§14.66](MASTERY_IMPLEMENTATION_PREPARATION.md#flight-mekanism-exact-provider-completed)でSUPPORTED追加、artifact待ちは解消。generic未解決という事実と旧喪失報告の原因未特定は維持。Break Realm expansion NOT IMPLEMENTED、Bulwark未着手、試作型機関弩は完全一致開始指示まで監査/設計/test/実装禁止、FOURTH BOOT未承認を維持 |
| 既知の残WARN/リスク | 今回Invaderにも外部entity保存時UUID重複等のWARNが残る（[log](../build/verification/trial-invader-20260915-203400/audit/final-invader.log)）。本人保存/通常停止は確認したが、boss群の長期保存・loot/全挙動は未保証。以下は旧runの残記録。 新runの初回optionsにsimulationDistance=4があり、Minecraftの許容範囲[5:33]外ERRORを記録。測定時のHP/Soul等へ補正せず、生成前/区画guard・3条件・保存保持は成立。[新run原log](../build/verification/trial-client-20260915-153300/audit/latest.log)。既存asset/SynchedEntityData等も全WARN解消とはしない。**今回TrialはForge47.2.0ロードFAIL→47.4.0で限定統合成立**。最終runにもsandbox認証公開鍵ERROR・更新確認接続失敗・WMI/外部SynchedEntityData/config既定補完等のWARNが残る。[今回log](../build/verification/trial-monolith-20260915-125400/audit/actual-server-final.log)。外部Soul強制死のdrop通知[7,0]はhelper対照と一致し、空通知とアイテム重複を区別。全環境問題の解消宣言なし。**前回SuperbWarfare自動serverは認証公開鍵取得がsandbox制限でERROR、当時の記録を保持。9/13の通常Prism singleplayerで同ERRORは再現しなかったが認証/dedicated/二者通信全般の解決・PASSではない**。9/13の実clientにはSWの`vehicle/ac_130h/Minigun_fire.wav` invalid path ERROR1件、既定config補完・library metadata・既存Food Healing試験食model/asset/sound/shader/IPv6等のWARNが残る。[現物log](../build/verification/superbwarfare-client-20260913-194701/audit/game-evidence.log)。GLOCK-17の比較を阻害した証拠はなく、全WARN解消や外部MOD全機能無害とはしない。初回のPatchouli不在によるgrant_manual解析ERRORはlocal Patchouli追加後の最終runで再現せず、初回ログは保持。検証fixtureのpack metadata欠落WARNはfixtureだけ修正。今回もtest-food item model欠落・language Jar metadata/asset/shader/音源/IPv6値等のWARNを記録。Core初回保存は読取snapshotと同時刻のファイル使用中retryから回復、再読込と最終保存一致。lock保持主体は未実測で製品不具合と断定しない。過去のdev WARN/DEBUG exception等も未解決履歴として保持。effect短時間残存・独自menu等の互換性制約を無害/解消済みとはしない。全warning/exception 0や全MOD互換は宣言しない |

**RELEASE CANDIDATE = NO。** Security監査・静的読取だけで上記gateや動作判定を解除しない。

### 残release条件の分類と実行単位

正本の[既存release安全条件（AUDIT_REPORT §F）](AUDIT_REPORT.md)は、起動/optional不在クラッシュ、保存消失・dimensionリセット、owned modifier重複、使用不能なHP HUD、死亡/墓drop増殖、二重回復/count、multiplayer状態漏れ、他MODのflight/effect/modifier剥奪、未解決の破壊的migrationである。既存PASSは各実行範囲で維持し、これらを削除・緩和しない。下記は作業の分類であり、全未確認を新たなrelease必須条件にするものではない。

| 分類 | 実行可能な単位・成立/合格条件 | release上の扱い |
|---|---|---|
| 確定仕様の既存完了 | Trial/L2限定対応、FE指定受入#2/#3、P/T ready#4、Pam#5、Nutrition#6を維持。Flightはexact-provider契約・自己解除/不正拒否/通常購入を[§14.65](MASTERY_IMPLEMENTATION_PREPARATION.md#flight-exact-provider-completed)で完了 | #1–#7 COMPLETE。限定対応を全MOD互換へ拡張しない |
| 最新の自動検証・実client | PRECHECK10/10・static426、全unit/compile/build/check・vanilla/TaCZ各114/114。A3不在回帰、B3正式FE startup/通常item描画、正常保存終了。[§14.74](MASTERY_IMPLEMENTATION_PREPARATION.md#queue8-fe-shader-blocker-resolution) | **#8 COMPLETE**。coreは最終source/userdev、A3/B3は同じ最終配布Jar直接。旧外部full suiteを最終Jar新規PASSへ転記しない |
| 9/13 20:13までに完了した最小実client | GLOCK-17＋Handgun Ammo、fresh Lv0/Lv1、同条件牛への通常左クリック各1回。5.5/11.0のHealth減少、17→16→15発、SP0/未取得・GUI/HUD・通常保存/終了を確認 | 9/13に確認した最終Jar・Forge47.4.0・この銃/条件だけの限定PASS。全SWやTaCZ/Root精密確認、実2-clientへ拡張しない |
| 未実施検証・合格条件 | 実2-clientは別accountによるstate分離/購入・toggle・保存同期確認。外部MODは対象artifact/versionごとに[TEST_PLAN](TEST_PLAN.md)のownership/重複処理/保存等の該当期待値を満たすこと。Ammoの購入時heat、厳密shot、全bolt/packet内部、hot-gun、dedicated遅延は個別未確認を維持 | REAL2CLIENTは既存BLOCKED。個々の未実施ケース・性能/全pack網羅をすべて新必須へ昇格させない。どこまでを今回release必須とするか資料にない範囲は**判断待ち**。全互換未検証表示は維持 |
| 利用者の仕様判断 | OPEN-02/03の取得前提、OPEN-04旧倍率維持・式はLOCK済みで判断待ちから除外。True Root部分蓄積・親RootOFF・死亡/logout/restart予約方針は対象外/未決定。将来の追加前提と既取得者への適用・返金・失効は今回未決定 | 完成済みTrue Root保持/再ON時期、OPEN-02/03/04と今回Q1/Q2を再質問しない。release範囲や将来仕様は利用者の別判断 |
| artifact・account・権限待ち | #5/#6/#7完了。FE/EL・Mekanism・今回Avaritiaの承認原物確認/限定統合完了、今回追加必須artifact不足なし。generic帰属はNOT AVAILABLE | 実2clientの第二account待ち・他の範囲外artifact待ちを分離。#7利用者判断待ちは解決済み |
| 明示的開始保留 | Flight/P/T PURCHASE READY。Break Realm expansion NOT IMPLEMENTED、Bulwark、試作型機関弩完全一致開始条件、FOURTH BOOT NOT AUTHORIZEDを維持。#8 COMPLETE/#9未開始 | 他の保留機能を開始せず、全項目を新たなRC必須条件へ自動追加しない |
| 表示・警告・互換の個別未確認 | 16秒表示：コード機構は確認、当時の原因確定は未判明、表示以外の追加影響を示す証拠なし。既存model/dev WARN、source-modset ERRORの全機能影響、独自menu/effect短時間残存・性能等は範囲ごとに未確認 | WARNの存在だけで製品修正しない。再現条件と安全条件への実害を照合し、release必須か資料にないものは判断待ち |

### LOCK済みの判断と残る実装境界

| 項目 | 正式決定 | 残る作業・再判断しない事項 |
|---|---|---|
| OPEN-02 | 通常浄化Lv1取得だけ、浄化の極意自体100SP、追加前提なし | 旧「複数の高難易度投資が未指定」という停止理由を解除。効果/必要Adapterの未完成は別gate |
| OPEN-03 | 浄化の極意Lv1取得だけ、真実の極意自体500SP、追加前提なし | 親取得とON/OFFを区別。通常浄化3SPを含めた累計を600SPとしない。将来変更・既取得者処理は別決定 |
| OPEN-04 | 旧食義Lv倍率`1 + ShokugiLevel * 1.0`を維持、新SP stat/費用/独自capなし | Lv0/1/200→1/2/201。承認実物の通常弾を照合・v3接続・限定統合完了。全攻撃/別版は未確認。保持/廃止を再質問しない |

### 両極意の限定実装と残る資料

**Q1/Q2は2026-09-15 RESOLVED / LOCKED、第一単位は自動統合まで完了**。旧「通常浄化OFFでも有効」提案は不採用。仕様全文は[Skill Tree §9 Q2](SKILL_TREE_SPEC.md#purification-mastery-activation)と[Compatibility §7 Q1](COMPATIBILITY_POLICY.md#trial-monolith-149)。取得100/500SP・SP安全検証を維持。通常購入は§14.58でready化済み。

- 対象: **The Trial Monolith1.4.9**、原物SHA256 `5EFE4C06…E1A0AD`。[完全hash/依存/新旧差分](MASTERY_IMPLEMENTATION_PREPARATION.md#mastery-artifacts)。旧1.2.8展開物は過去参考、原物未提供という停止理由は解除。
- 実装: Cubeの通常/force加算2hookを維持し、侵略者の敵対flag解除1・大小Beam各Soul2・Huge numeric1を追加（共通計画§11）。既存Soul/HPを修復せず、攻撃entityの保護trueを維持。new schema/timer、共通damage/購入変更なし。
- 証拠: [実49ケース](../build/verification/trial-monolith-20260915-125400/actual-server-final/trial-result.json)、[実ロード2hook各1](../build/verification/trial-monolith-20260915-125400/audit/loaded-callsite-validation.json)。正当numeric death/drop1、非保護Soul強制death/respawnのnative維持、外部既存Soul/保存/他player/mob分離を区別。
- 完了と残り: 旧8F4A…JarのCube A/B/C・seal/再読込は[限定PASSを保持](MASTERY_IMPLEMENTATION_PREPARATION.md#trial-client-safe-result-20260915)。21:11の侵略者86/86とL2 95/95/Cube49/49回帰完了。L2代表4種の実client A/B/T/C・全OFF/範囲外・保存再読込は§10.11限定PASS。侵略者F1/F2/S1＋新run S2/H1・seal/cleanup・通常保存終了は限定完了。一般buff解除未特定・他trait/攻撃/負荷は未確認。これらを今回の購入必須へ広げず、P/Tは§14.58でready化済み。

### True Root実装と通常コードレビュー

正本は[SPEC section17](SPEC.md#17-true-root-completed-reservation-retention)。以下は実装と通常コードレビューの結果であり、Codex Security scanとは別である。

| 経路 | 現在の実装・確認範囲 |
|---|---|
| OFF中の期限終了 | [RootController.advanceState](../src/main/java/com/leva/foodhealing/RootController.java)は、取得済みTrue RootがOFFで完成済み18のときだけ予約を保持する。active期限は通常どおり終了し、保持だけでは保護/再発動しない。部分蓄積、親Root無効時のreset、未取得経路は従来処理を維持するが、未決定の保持/破棄仕様をLOCKした意味ではない |
| GUI packet / command | 両経路は取得検証後にRootController.setSkillDisabledを呼ぶ。実際のTrue Root OFF→ON、取得済み、親Root有効、active期限終了、完成済み18を満たす場合だけ予約を先に0にして既定時間で1回発動。期限中の切替は期限を変更せず、消費後/同一ON要求では再発動しない |
| server・同期・順序 | sender/取得検証・packetのconsumerMainThread・commandの実行主体を維持。時計はserver gameTime。既存syncToClientは状態変更後のcanonical NBTを送信。テストは登録済みpacketのdecode/dispatch、command、送信payload一致、他player不変、期限境界のtick→ON/ON→tickを確認。client申告の予約量/期限は追加していない |
| 保存・copy・他skill | 当時のTrue Root実装ではschema/packet形式を変更せず、今回#6ではRoot値を保全してcount単位のみschema5へ移行。保存/表示だけでは再ON処理を呼ばない。TaCZのpacket有効化副作用を維持し、実artifactによるheat/重複ON等も回帰。通常Root時間/cooldown/固定窓、食事Nutrition、SP/前提・他倍率は変更なし |
| 対象外境界 | 部分蓄積、親Root自身OFF、死亡/ログアウト/再起動の予約gameplay方針は未決定のまま。既存進行/SP/toggleの別JVM回帰や受動copyの一致は、新予約のlifecycle仕様決定ではない。今回の確定範囲には追加判断不要だった |

### 固定release completion queue

**#8 COMPLETE / #1–#8 COMPLETE / #9 NOT STARTED / RC=NO / READY FOR RC DECISION**。正式#8の自動回帰・Jar監査・Profile A受入と、今回承認されたFE shader互換内蔵・A3/B3限定受入は完了。次の利用者指示まで#9へ進まない。実2-client等の別枠gateは維持。

### 次の1作業

**利用者によるRC判断・#9開始範囲の明示指示を待つ。** [§14.74](MASTERY_IMPLEMENTATION_PREPARATION.md#queue8-fe-shader-blocker-resolution) / [§38](TEST_PLAN.md#builtin-fe-shader-acceptance)。#8 COMPLETE、RC=NO。追加試験・GitHub/commit/push/PR・公開・正式Jar名変更・完全仕様書作成は開始しない。

FlightとP/T PURCHASE READY・SP保護、**RC=NO**、REAL2CLIENT=BLOCKED - SECOND MINECRAFT ACCOUNT REQUIRED、SAFE DESIGN PROVEN=NO / SOURCE DIMENSION TRANSITION VERIFIED=NO / TimeStop広域ownership BLOCKED、UOM/P vehicle未LOCKを維持。GENERIC FLIGHT PROVIDER ATTRIBUTION=NOT AVAILABLE、未知foreign-after非保証。Break Realm/Bulwark/試作型機関弩/FOURTH BOOT等へ進まない。可逆クラフト増加は既知許容仕様・バグ修正対象外。

### 今後のStatus更新ルール

- 各フェーズ終了時は、この現在の要約（完了、未完了、判断/承認待ち、次の1作業）を更新してから、日付付きの証拠・変更・検証結果を履歴に残す。
- 解決済み項目を現在の未完了一覧に残さない。旧一覧へ「こちらを優先」という追記を積み重ねず、旧集計は履歴と明示し、現行一覧を直接更新する。
- 仕様決定、実装、自動試験、実client試験と実施主体を区別し、部分的な未確認で確認済み機能全体をNOT RUNへ戻さない。過去FAIL/NOT RUN/対象Jar/環境/証拠は当時の記録として保持する。
- 検査対象は現在の要約・未完了一覧・次の作業の整合性。履歴内のNOT RUN等を一律禁止する文字列検査にしない。OPENは利用者決定なしに解除せず、既存PASSを理由なく再実行しない。
- 今後の実装完了報告は、対象Jarと最新版の正本 `docs/CODEX_STATUS.md` の直接リンク・本文更新日時を併記する。








</details>

## 2026-10-03 11:22 JST — Queue #9 formal release

#1–#8 COMPLETE / #9 PARTIAL / RC=YES / LOCAL RELEASE ARTIFACT AND SPECIFICATION COMPLETE / GITHUB UPDATE BLOCKED。製品差分0、既存11分類を継承してRC=YES。本書の仕様整理/同一Jarコピー/公開source整合であり新ゲーム試験ではない。[完全仕様書](FOOD_HEALING_RPG_V3_SPECIFICATION.md) / [receipt](../release/v3.0.0/RELEASE_RECEIPT.md)。過去11分類/旧FAILを保全。

## 過去の履歴 — 直近フェーズ

以下の停止・次作業・件数は当時の記録。現在の#8 COMPLETE・RC判断待ちは冒頭の現在要約を参照。

## 2026-09-30 07:19 JST — queue #8 FE shader内蔵・A3/B3限定受入完了

run `20260930-065113`。製品変更前PRECHECK10/10後、旧§14.19のexact契約をFood Healing所有client-only互換へ昇格。static426、全unit/build/check・core各114、A3/B3最終同Jar直接PASS、通常保存/Quit・Java process0。旧B2 FAIL/原world/§14.19証拠を保持。修復cycle0/HUMAN0。**#8 COMPLETE / #1–#8 COMPLETE / #9 NOT STARTED / RC=NO / READY FOR RC DECISION**。詳細は[§14.74](MASTERY_IMPLEMENTATION_PREPARATION.md#queue8-fe-shader-blocker-resolution)。

## 2026-09-30 01:23 JST — queue #8 実行・A2完了／正式FE起動BLOCKED

[§14.73](MASTERY_IMPLEMENTATION_PREPARATION.md#queue8-final-release-verification) / [§37](TEST_PLAN.md#queue8-final-release-acceptance)。TaCZ不在基礎購入を最小修正、全unit/build/check・vanilla/TaCZ114/114、正式配布Jar A2日英26skill/7stat・通常購入/同期/1reload/正常保存終了。B2はFE shader ClassCastException・title未到達・exit−1、world0。旧FAILを保全。最終Jar **359,310 bytes / 277 entries / SHA-256 DF9F8B0736DC39EBDD2F1DEBA3D15A8348AEE262C85AC9B79F52F1CC62D603BB**。#8 BLOCKED / #9 NOT STARTED / RC=NO、次はFE正式startup方針の指示待ち。

<a id="pre-queue8-summary-history"></a>
### #8開始前の成果物・完了集計（当時の要約を保全）

以下のREADY/NOT STARTEDは当時の記録。現行は冒頭の現在の要約。

- **PRE-#8 RELEASE CLEANUP — LEGACY COMMAND / VERIFICATION ARTIFACT COMPLETE**：[共通計画§14.72](MASTERY_IMPLEMENTATION_PREPARATION.md#release-command-verification-cleanup) / [TEST_PLAN§36](TEST_PLAN.md#release-command-verification-acceptance)。実行可能command9→6（A管理3/B診断・修復3保持、C旧chat GUI3削除、D/E各0）。旧command専用ja/en各54keyを削除し、現行26skill/7stat・食技/Shokugi表示は不変。検証HUD/helper/FE補助混入0、製品TimeStop内部observerは安全処理として保持。compile/build/unit/check・localization815・**vanilla/TaCZ各113/113（旧111のケースを維持・負対照置換＋新2）**・最終Jar監査/正常保存終了。gameplay class意図しない差分0、finite2590/schema5/protocol7/gate不変。HUMAN INPUT=0、実client未実行。**PRE-#8 PRODUCT CLEANUP COMPLETE / READY FOR USER AUTHORIZATION OF #8 / #8・#9 NOT STARTED / RC=NO**。
- **前工程（2026-09-29 23:31完了）：PRE-#8 DOCUMENTATION / LOCALIZATION / PLAYER-FACING LANGUAGE CLEANUP COMPLETE**：[共通計画§14.71](MASTERY_IMPLEMENTATION_PREPARATION.md#player-facing-localization-cleanup) / [TEST_PLAN§35](TEST_PLAN.md#localization-message-acceptance)。日本語26skill・7base-stat説明と英語対訳、食技表記、習得/上昇メッセージを整理。表示だけの変更で有限2590・費用/効果/内部Shokugi/schema5/protocol7不変。localization unit1,094＋既存全unit、compile/build/unit/check、**vanilla/TaCZ各111/111（旧109保持＋message2・削除0）**、最終Jar監査/正常保存終了。HUMAN INPUT=0、実画面NOT RUN。**READY FOR USER AUTHORIZATION OF #8／#8・#9 NOT STARTED／RC=NO**。
- **前工程（22:38完了）：PRE-#8 SKILL COST REBALANCE + QUARRYING / IMMOVABLE COMPLETE**：[共通計画§14.70](MASTERY_IMPLEMENTATION_PREPARATION.md#skill-cost-rebalance-quarrying-immovable-completed) / [受入§32–34](TEST_PLAN.md#quarrying-acceptance)。全SP新価格をLOCKし、normal1150/Root70/Heroics250/high620/TaCZ500＝**finite2590**（repeatable除外）。採石max1/1SP・不動max1/50SPを実装。新unit602＋既存全unit、compile/build/unit/check、vanilla/TaCZ各109/109（旧95保持＋14）・最終配布JarL2限定8/8、Jar監査/全試験server正常保存終了。既取得者への遡及請求・返金0、v2移行/schema5/protocol7不変。実client/HUMAN INPUT=0。**#1–#7 COMPLETE／#8・#9 NOT STARTED／RC=NO**。次の利用者指示待ち。
- **前工程（当時の価格・成果物）：PRE-#8 LEGACY SKILL PARITY FIX COMPLETE**：[§14.69](MASTERY_IMPLEMENTATION_PREPARATION.md#legacy-skill-parity-fix-completed) / [正式受入§31](TEST_PLAN.md#legacy-parity-future-acceptance)。利用者の2判断をLOCKし、Gathering max3・Unbreaking max3・Pursuit max9とArmorMastery標準耐久全般/direct hurtを実装。unit新320＋既存全unit、build/unit/check、vanilla95/95・TaCZ95/95・実L2耐久限定24/24、最終Jar監査/正常保存終了完了。既存group1,025SP/有限総額1,821SP。**#1–#7 COMPLETE／残2／#8・#9 NOT STARTED／RC=NO**。Flight/P/T PURCHASE READY維持。20:36旧監査と途中FAILは履歴保全。
- **pre-#8 Avaritia exact Infinity Chestplate Flight extension COMPLETE**：[§14.67](MASTERY_IMPLEMENTATION_PREPARATION.md#flight-avaritia-exact-provider-completed)。承認原物4.0.3限定・胸registry readonly Adapter、actual23/23＋EL混在2/2、unit88、vanilla/TaCZ84/84、全4server正常保存終了・最終Jar監査完了。**#1–#7 COMPLETE／残2／#8・#9 NOT STARTED／Flight PURCHASE READY／RC=NO**。原物/既存FE・EL・Mek/policy/token不変、generic帰属NOT AVAILABLE。
- **前フェーズ§14.66：Mekanism / MekaSuit exact-provider extension COMPLETE（当時の結果維持）**：[§14.66](MASTERY_IMPLEMENTATION_PREPARATION.md#flight-mekanism-exact-provider-completed)。承認原物/loader10.4.16限定、public Readyのreadonly Adapter。actual28/28、unit83、vanilla/TaCZ各84/84、正常保存/停止・最終Jar監査完了。**#1–#7 COMPLETE／残2／#8・#9 NOT STARTED／Flight PURCHASE READY／RC=NO**。既存FE/EL契約・DENY優先・generic帰属NOT AVAILABLEは維持。
- **前フェーズ§14.65の完了結果（当時のJar・証拠を維持）**：[共通計画§14.65](MASTERY_IMPLEMENTATION_PREPARATION.md#flight-exact-provider-completed)。exact-provider/version契約とEndingLibrary DENY優先を利用者LOCK。自己false→trueだけのsession token、supported native provider照会・owned解除、invalid/pending拒否、通常2SP/default ONを実装。unit78・vanilla/TaCZ各84/84、最終配布Jar実FE18経路PASS＋胸装備保全/外部解除欠落診断4。全server保存/終了。generic帰属はNOT AVAILABLE、unknown-afterは非保証。#1–#6と過去限定PASSを維持。

## 2026-09-30 00:07 JST — pre-#8 release command / verification artifact cleanup完了

[共通計画§14.72](MASTERY_IMPLEMENTATION_PREPARATION.md#release-command-verification-cleanup)に9→6の全分類、v2 commit比較、obsolete翻訳54/言語、保持observer分類、初回4FAILと最終各113/113、localization815、Jar監査/正常保存終了を記録。最終Jar **358,640 bytes / 277 entries / SHA-256 4DF4954EC7082DA0276A0614EAEB871971F4A69538983EEED2F688BA4E3B5DEC**。開始原物/全旧履歴・検証資産を保持。gameplay/source gate/schema5/protocol7・finite2590不変。実client/#8/#9は未開始、RC=NO。

## 2026-09-29 19:54 JST — pre-#8 Avaritia exact Flight extension完了

[§14.67](MASTERY_IMPLEMENTATION_PREPARATION.md#flight-avaritia-exact-provider-completed)に原物/全writer静的照合、限定実装、actual23＋EL混在2、unit88/core各84、最終Jar監査・全4server通常保存終了を記録。最終Jar **350,807 bytes / 269 entries / SHA-256 C106B5B67F63821289B0BB51F77E165CB7D1E0684F6BFBE228A8DF3E9851F7BC**。Avaritia原物不変、外部state mutation0、native解除→FH再付与後100評価で反復0。旧FE/Mek PASSを保持、#1–#7 COMPLETE・残2・#8/#9未開始・RC=NO。


## 2026-09-29 12:46 JST — queue #7 Flight Gate A・製品変更前STOP

[共通計画§14.64](MASTERY_IMPLEMENTATION_PREPARATION.md#flight-gate-a-attribution-blocked) / [TEST_PLAN§13](TEST_PLAN.md#13-flight)。全製品ability writerはserver tickのmayfly=true 1site、false/flying writer0。OFF自己解除欠落と、未知foreign-afterの同値書込を識別できない共有boolean/API境界を確認。baseline/token/known Adapter/標準API/非解除の比較、vanilla lifecycle、FE/EndingLibrary実物の限定STATIC AUDITEDを記録。報告された外部flight喪失の実行時原因は未確定。#7 BLOCKED - GENERIC FLIGHT PROVIDER ATTRIBUTION、残3、#8/#9未開始。製品333375 bytes/257 entries/SHA134FDE235FCAE2D81E06FE25A5512FE7469A38EE674D05AE18BF793B350232B5/protocol7不変、source/test/build/config/購入gate変更0、build/test/game/新fixture0。3文書だけ更新・開始前backup保存。既存75/75等は前フェーズPASSとして保持。

## 2026-09-29 10:51 JST — queue #6 Nutrition/Food-Level count完了

[§14.63](MASTERY_IMPLEMENTATION_PREPARATION.md#nutrition-count-completed) / [総合照合](../build/verification/nutrition-count-20260929-101705/audit/reviewed-results.json)。default2000、旧custom×10・明示新値優先、schema5比例count移行を実装。実food/native FoodData分離、Satisfaction・Root境界・overflowatomic・旧bonus負対照を確認。Nutrition93 assertions、vanilla/TaCZ75/75、実v2 count35→350/raw35/SP2・synthetic別JVM write/read、build/unit/check PASS。333,375 bytes / 257 entries / SHA-256 134FDE235FCAE2D81E06FE25A5512FE7469A38EE674D05AE18BF793B350232B5、protocol7、非混入/reobf監査済み。途中compile/fixture FAILを保全。通常save/stop/exit0、client/Prism/旧world/外部downloadなし。#1–#6 COMPLETE／残3、#7以降未開始・Flight blocker/RC=NO/他gate維持。

## 2026-09-29 09:40 JST — queue #5正式受入方針変更・COMPLETE

[§14.62](MASTERY_IMPLEMENTATION_PREPARATION.md#pam-foodcore-known-upstream-acceptance)／[機械分類](../backups/pam-acceptance-reclassification-20260929-093741/reclassification.json)。利用者がFood Core1.0.5 exact hashの自己recipe defect3件のみknown-upstream/non-blockingへLOCK。既存log ERROR全3件のrecipe/result/source原Jarを照合し、Food Healing0/unexpected0。既存7/75、present150/absent146、core67×2、通常save/stop/Minecraft exit0・PID終了証拠で#5 COMPLETE。旧ERROR0方針のFAIL/PENDING/runner exit1は変更しない。新run/build/製品変更0。5文書更新、残4、#6–#9未開始で停止。3recipeは使用不可、全Food Core/長時間play保証なし。#7Flight blocker/RC=NO/REAL2CLIENT等維持。

## 2026-09-29 07:49 JST — queue #4 P/T通常購入接続完了

[共通計画§14.58](MASTERY_IMPLEMENTATION_PREPARATION.md#mastery-production-purchase-ready)／[照合結果](../build/verification/mastery-purchase-20260929-073000/audit/reviewed-results.json)。P/T固定pendingだけ解除、P100/T500・前提取得/親OFF・optional購入方針適用。異常取得mapの無変更拒否とLong使用済SP overflow preview/server一致をFoodHealingSkills内で補強。新unit153、既存取得142/P150/T53、core vanilla/TaCZ各67/67、build/unit/check成功。最終Jar307,233 bytes/198 entries/SHA256 C12C046E0A218E1FB159A607F09F9B681B248AB4C10D0DCE3C4650D88F12584F、protocol7。GameTestはMC1.20.1/Forge47.2.0 userdev最終sourceで、配布Jar直接実clientではない。2新規world通常保存/stop/exit0、対象Java残存0。初回sandbox native DLL起動失敗を保全、権限offline実行で解消。#1–#4 COMPLETE、残5、#5未開始。#7Flight bug/RC=NO/REAL2CLIENT等維持。旧3文書・source/Jarは開始前backup保存。

## 2026-09-28 23:38 JST — queue #3 FE6限定実client完了

[正本§14.57](MASTERY_IMPLEMENTATION_PREPARATION.md#fe6-client-completed)／[結果照合](../build/verification/fe6-client-20260928-231600/audit/reviewed-results.json)。run20260928-231600、製品306,882 bytes/198 entries/hash1BBD49E6…不変、helper-only offline build2回とも成功（2回目は測定前inventory/physical安全観測追加）。生成前GUIの湧き/自然回復OFF、Survival/Normal、閉鎖照明区画でfresh確認。prepare1、P OFF→ON・M保存ONとcanonical収束、実UOM Dream1、D/Hurt/Damage10.670565・HP20→9.329435、2site true・forward/add0、LocalPlayer連続3sample一致・通常HUD9.3/20.0。seal→native own actor cleanup→通常全dimension保存→readonly本人/actor不在→title/Quit・Minecraft32180/今回Prism37604終了。開始前から存在するPrism26748（9/27生成）は未操作。HUMAN0、測定後repair/rebuild/retry0。追加BAN実clientを必須化せず#3 COMPLETE、残6、#4未開始。下の22:57記録は当時のclient未実施結果として保持。

## 2026-09-28 22:57 JST — queue #3 FE6 secondary実装・自動統合完了、限定client残

詳細は[共通計画§14.56](MASTERY_IMPLEMENTATION_PREPARATION.md#fe6-secondary-production-result)。6site exact、実ロードredirect2/1/1/1/1、P ON0/無資格元付与、numeric/owner/RNG/既存effect保全成立。focus63/63＋補足8/8、vanilla/TaCZ60/60、build/unit/check PASS。開始Jar298,958→最終306,882 bytes /198 entries / SHA-256 `1BBD49E6377DAF0F5C7C0EDED15A61F96261C80A1EA8C03471E634EFD6404A19`。中途5runのfixture不備/観測不備は原因分離・原FAIL保全、新worldへ分離。製品修正は6site追加のみ、TimeStop/購入/schema不変。通常保存/停止/対象Java終了、実client/Prism/HUMAN0。#1/#2 COMPLETE維持、#3 CLIENT FOLLOW-UP PENDING、#4未開始・残7。

## 2026-09-28 22:10 JST — queue #2指定受入完了、#3未開始

新run `20260928-214421` の実結果は[共通計画§14.55](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-queue2-completed-readonly-query)、[照合結果](../build/verification/uom-production-os-input-20260928-214421/audit/reviewed-results.json)、[原journal](../build/verification/uom-production-os-input-20260928-214421/CLIENT/audit/journal.jsonl)。helper-only変更/限定offline compile-reobf、prepare1・OS入力A/B各1・native goal E1/E2各1、positive disk178、同PID/JVM reload1・native deserialize0→178。actual isolation、readonly query false1/前後22項目不変、READY後5 END（source169→165）、旧authority非復活、sealを成立。cleanupはSKIPPED_ALREADY_ZERO。通常保存2回・readonly照合・TITLE1/2 HUD各20frame draw0・正常Quit・5PID終了。測定後repair/retry0、state repair0、追加authority call0、製品/旧証拠不変。測定前のsandbox native DLL/OS窓列挙不足は承認済み権限付きローカル実行で解消し、helper code修正再試行は0。過去FAIL/STOPを成功へ書換えず保持。変更前3文書は `build/verification/uom-production-os-input-20260928-214421/before/docs/`。#1/#2 COMPLETE、残7、#3未開始で停止。購入/SP・RC等の全体gateは維持。

## 2026-09-28 21:32 JST — client独立dimension証拠方式LOCK（静的確認のみ）

[共通計画§14.54](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-client-negative-query-locked)。承認EndingLibraryの `isCurrentTS:()Z` 全7命令と自然attachの実変換bytecodeを再確認し、利用者の15条件成立により1回readonly query方式をLOCK。自然RETURNとは別ラベル、before/after不変、query receiptとfresh DTOの時刻分離、5 ENDへの接続を確定。同論点の追加READ ONLYなし、次は限定helper修正/1新runの実行承認待ち。今回query/起動/build/helper変更0、既存PASSと全gateを維持。変更前3文書と保護hashは `backups/20260928-212822-client-negative-query-lock-docs/`。

## 2026-09-28 21:19 JST — gen2 cache境界のREAD ONLY設計

[共通計画§14.53](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-gen2-native-cache-boundary-design)に実call-site、旧runの時刻と未観測、tri-state、LOGIN/受入分離、最小helper差分を集約。serverの自然loginによるlazy保存cache生成とclientのconstructor/attachを分離した。global=falseのclientは独立dimension Booleanの自然呼出しが短絡され、次packet保証もないため、待機だけで解消済みとはしない。今回client negative証拠の新方式は未採用。次回承認前に境界を確認する。

変更は3文書のみ、変更前backupは `backups/20260928-211252-gen2-cache-design-docs/`。製品・補助・旧runは読取のみ。保護対象1,232ファイルの前後SHA-256差分0、対象旧run/instanceへの追加0を確認。§14.52の成立済みPASS/STOP後正常終了と過去FAILを保持し、5 END/B総合/seal/成功final teardownは未完了のまま。起動/build/helper変更/入力0、#1 COMPLETE/#2 INCOMPLETE/残8/#3未開始、全gate維持。

## 2026-09-28 20:57 JST — gen2自然観測helper実装・初期cache観測STOP

**新run `20260928-203401`：helper限定実装・A/B/E2 setup・positive disk178・TITLE1・同PID/JVM reload1・native deserialize0→178・Entity/capability/level/serverの実参照分離が成立。gen2初期login snapshotでSTOP。** `login→snap→dimension`がまだUNINITIALIZEDのnative cacheにBooleanを要求した検証補助の不備。受動client readyと5 ENDへ未到達（END1〜5各NOT RUN）、旧authority総合非復活/State・Context実比較はUNVERIFIED、seal/明示cleanup0。通常保存・readonly・TITLE2/HUD20frame draw0・Quit/対象5PID終了済み。停止後退出を成功final teardownへ転記しない。製品不変・HUMAN INPUT=0。[今回結果§14.52](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-gen2-bind-uninitialized-cache-stop)。#1 COMPLETE/#2 INCOMPLETE/残8、#3未開始。

[証拠集計](../build/verification/uom-production-os-input-20260928-203401/audit/reviewed-results.json)、[変更前3文書](../build/verification/uom-production-os-input-20260928-203401/before/docs/)。過去PASS/FAIL、§14.50旧run、製品/外部原物は保全。


## 2026-09-27 21:33 JST — 新runのREADY前timeout

**新run20260927-211333：入力READY前のwatchdogでSTOP / INPUT UNVERIFIED。** 本人の2操作説明・別画面配置確認後、helper限定offline compile/reobf、新instance/world、prepare1/E1 goal1、通常pause安定まで実施。Codexのpause/resume操作往復がE1開始から15秒のREADY前期限内に収まらず、READY/本人入力の証拠0で停止。製品movement FAILや本人の操作忘れとは判定しない。A移動・B通常復帰・E2/positive disk/同JVM B・seal未実施。通常保存/readonly/Quit・PID9208不在を確認。詳細[共通計画§14.45](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-dual-display-readiness-timeout)。**#1 COMPLETE／#2 INCOMPLETE／残8**。

停止後はsource count0を通常保存、HP20・SP0/使用済103・P/M ON・transient保存keyなしをreadonly確認。same-JVM B/positive保存試験へ転記しない。前run/旧worldは保全、製品は不変。


## 過去の履歴

### 2026-09-29 13:35 JST — queue #7 Flight exact-provider完了

利用者のA方式とEL DENY優先LOCKに基づき、自己session token/readonly exact Adapter/不正取得拒否を実装、通常2SP購入をready化。Flight unit78、vanilla84/84・TaCZ84/84、最終配布Jar実FE18経路PASS＋胸装備保全/外部解除なし4診断。最終Jar **344,559 bytes / 265 entries / SHA-256 F53C890F77CED09F55B3DBAF0406BE25DEAF1F72D6EA1B76B218F90AA4D2216C**。途中の補助path/API/対象指定とCurios版照合失敗は保全、全試験server通常保存停止。schema5/protocol7・外部原物不変。詳しくは[共通計画§14.65](MASTERY_IMPLEMENTATION_PREPARATION.md#flight-exact-provider-completed)。#1–#7 COMPLETE/残2、#8/#9未開始、RC=NO。他の既存PASS/gate不変。



### 2026-09-28 19:26 JST — queue #2 lifecycle実装・TITLE1／positive reload成立、observer STOP

**verification-only lifecycle実装・TITLE1正常退出・同PID/JVM再読込・positive deserializeが成立。新run `20260928-190153` は再読込client観測待ちでSTOP。** helperがgen2の自然な`valid(false)` RETURNを待つ間にnative count178が自然に0へ進み、`native positive remains without repair`で停止。5 END=0、旧authority非復活の総合判定UNVERIFIED、seal/明示cleanup各0。停止後は通常保存→TITLE2→Quit、Minecraft/今回Prism/OS補助PID終了。製品不変・HUMAN INPUT=0。 [詳細§14.50](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-lifecycle-title-pass-reload-observer-stop) / [証拠](../build/verification/uom-production-os-input-20260928-190153/audit/reviewed-results.json)。helper-only build成功、repair1/2。製品293保護hash不変、旧run保全。停止後保存・終了済み、再試行なし。


### 2026-09-28 18:50 JST — helper退出設計・旧高栄養bonus廃止LOCK（文書のみ）

[共通計画§14.49](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-teardown-lifecycle-design)で退出監視/fail UI race/rebindと同JVM B残工程を設計。§14.48の保存後world-null PauseScreen NPEをverification lifecycle不備として保持。failure個別operandは未採取、removed由来は静的推定。新run/helper作成/compile/ゲーム/試験0。

[SPEC§12](SPEC.md#legacy-high-nutrition-retirement)へ利用者の旧高栄養bonus廃止・移行effect非一括除去を正式LOCK。現sourceの旧grant/refreshは不在、互換Configキーのみ。新正式受入はTEST_PLAN§28 NOT RUN。Flight外部Creative Flight喪失は別件#7正式blocker。変更はSPEC/SKILL_TREE_SPEC/TEST_PLAN/CODEX_STATUS/共通計画の5文書のみ。保存後照合：製品等＋helper/OS補助/補助Jarの205ファイルhash差分0、製品Jar298958 bytes/189entries/SHA256 37E300EAA0663FDCDF445317C9F14F3BF13C839B21E4DD5679229B32386592E3一致。[変更前backup](../backups/20260928-184352-teardown-legacy-bonus-design-docs/CODEX_STATUS.md)。製品Jar/source/test/build.gradle/helper/OS補助/外部原物/Config/world不変、固定順序1〜9・#2 INCOMPLETE/残8・全gate維持。削除IMPLEMENTED/Flight FIXED/RC readyではない。

### 2026-09-27 23:02 JST — B通常復帰/E2 positive保存成立・helper退出STOP

**B通常入力復帰・E2・actual positive diskが限定成立。保存退出時のhelper lifecycle不備でSTOP、Minecraftはクラッシュ終了。** 新run `20260927-224439`、AUTOMATED OS INPUT B W1004.4951ms／実client positive20tick／server・client移動1.00block／受理移動6件・correction0／解放静止3tick・位置差0。E2 epoch2/sequence2、runtime179→serialize179/178→実disk178。検証HUDはTEST_COMPLETE後2秒でHUD_DISABLED、同session20render frame描画0。通常保存での退出中にhelperのhealth guardが失敗し、fail()がworldなしへPauseScreenを表示。そのDisconnect操作でClientLevel=nullのNPE、exit−1。same-JVM B/旧authority非復活/seal/明示cleanupは未実施、正常QuitはFAIL。旧A限定PASS維持、今回AはB開始準備1回。HUMAN=0。 [詳細§14.48](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-os-normal-positive-save-teardown-stop)／[実測集計](../build/verification/uom-production-os-input-20260927-224439/audit/reviewed-results.json)。


### 2026-09-27 22:28 JST — AUTOMATED OS INPUT A限定PASS／B READY timeout

**AUTOMATED OS INPUT AはREAL CLIENT LIMITED PASS、B準備でSTOP・通常保存/終了済み。** 新run `20260927-215939`：READYはE1開始+131.3433ms、SendInput W1002.648ms、実client positive20tick、server/client移動1.199999988block、受理移動7packet/correction0、release/収束・native end/revoke成立。Bはhelperが停止終了後もfresh native canMove=true RETURNを要求して30秒timeout、READY/OS W0。B移動はUNVERIFIED、E2/positive disk/同JVM B/seal未実施。製品movement FAILとは判定しない。**利用者判断によりqueue #2はAUTOMATED OS INPUTを採用、HUMAN物理Wを必須条件から外す。HUMAN INPUT=0。** [結果・残件§14.47](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-automated-os-input-a-pass-b-readiness-stop)。#1 COMPLETE／#2 INCOMPLETE／残8、#3未開始。

22:20:06全dimension保存、readonly HP20/SP0・使用済103/P&M ON/source count0/transient keyなし。22:20:36 Quit、Minecraft PID16148/今回Prism PID32524不在。prepare1/goal1/再読込0/seal0/cleanup0。製品171hash不変、旧run再利用なし。変更前backupは[本run before/docs](../build/verification/uom-production-os-input-20260927-215939/before/docs/CODEX_STATUS.md)。


### 2026-09-27 21:50 JST — READY先行順序とobserver境界のREAD ONLY設計

[共通計画§14.46](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-ready-first-thread-boundary-design)に原因・次回最小差分を集約。本人が前runのW A/B未実施を確認（READY自体が出なかった）。初回正規Lease/validはpause前に存在し、pauseはfresh Lease生成の必須操作ではない。停止記録の非readonly呼出しはthreadを含むbinding比較へ到達する。今回3文書と変更前backupのみ、ゲーム/helper/build/test/入力依頼0。backup: `backups/20260927-214413-uom-ready-thread-design-docs/`。既存PASSと全gateを維持。

### 2026-09-27 21:03 JST — queue #2 Minecraft内READY/本人2操作のREAD ONLY設計

**READ ONLY手順設計完了／実行承認待ち。** Minecraft画面内のhelper専用HUD帯で段階別READYを伝え、本人のW保持→解放は**計2回（E1中／終了後の通常入力復帰）**とする。2回目の歩行余地を二段階停止壁で確保し、位置setterは使わない。READY predicate・timeout・終了条件・次回一括範囲は[共通計画§14.44](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-human-input-handoff-design)。今回は起動/新run/helper変更/compile/build/test/入力要求0。前run20260927-203225のHUMAN INPUT NOT PERFORMED / INPUT UNVERIFIEDと既存PASSを保持。**#1 COMPLETE／#2 INCOMPLETE／残8**。

通常入力復帰は現行observerがstage4での新しいinput・移動・解放を要求するため2操作とした。既存の左上HUDを中央帯へ改善し、二段階壁・A native終了上限/15秒watchdog・B入力待ち30秒を計画。未実装/未実行でありPASS追加なし。変更前3文書は `backups/20260927-205925-human-input-handoff-design-docs/`。


### 2026-09-27 20:53 JST — queue #2 HUMAN入力未実施、UNVERIFIEDで保存・終了

**HUMAN INPUT NOT PERFORMED / INPUT UNVERIFIED。** 新run `20260927-203225` はprepare1・E1 native goal1、pause→fresh Lease→REAL INPUT READYまで成立。本人回答は「Minecraftが前面に出たため、Codex側の入力タイミング指示をユーザーが確認できなかった」。180 client tickでkey/forward positive0・移動0。入力の受け渡し未成立であり製品movement FAILとはしない。E1自然終了/revoke・通常保存/Quit/PID終了は確認。restore/E2/positive disk/same-JVM B/sealは未実施。補助だけ限定offline compile/reobf、製品不変。**#1 COMPLETE／#2 INCOMPLETE／残8**。[§14.43](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-human-input-handoff-unverified)。

本測定PID1888は正常保存/Quit。終了確認中の予期しない追加起動PID32968はfirst-write guardでロード失敗、world未開始のまま通常閉じ、PID終了。起動主体は未確認。元の保存/log/receiptを保全し、削除・リセットなし。[判定集計](../build/verification/uom-production-input-final-20260927-203225/audit/reviewed-results.json)。変更前3文書は `build/verification/uom-production-input-final-20260927-203225/before/docs/`。


### 2026-09-27 20:10 JST — queue #2入力capability preflight、正式hold APIなし・起動0

公開schema・実Sky actionsをREAD ONLY照合し、**AUTOMATED HOLD NOT AVAILABLE**と判定。利用者指定のno-hold分岐に従いMinecraft/Prism launch0、新run/helper作成0、製品修正0、compile/build/test0で終了。§14.41と既存PASSを保全。新規の実入力・movement・restore・E2/positive disk・B/deserialize/非復活・cleanup/終了はNOT RUN。次回はREAL INPUT READY時だけHUMAN物理W約1秒。[§14.42](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-input-capability-preflight)。変更前3文書は `backups/20260927-201000-input-capability-preflight-docs/` に保全。#1 COMPLETE／#2 INCOMPLETE／残8、既存gate維持。


### 2026-09-27 18:19 JST — queue #2最終残確認、pause/resume成立・実入力UNVERIFIED

新run3件を実施し、最終runの実ポーズ/fresh Leaseを確認。client tick上の通常入力が成立せず、指定のINPUT UNVERIFIEDで停止。E2/Bへ進めず、全3runを通常保存/Quit、readonly本人/source/lock照合、PID終了まで保全した。製品変更0・旧PASS保持・#2 INCOMPLETE/残8。詳細は[§14.41](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-final-input-pause-result)／[集計](../build/verification/uom-production-final-20260927-180600/audit/reviewed-results.json)。3文書の変更前は `build/verification/uom-production-final-20260927-174200/before/docs/` に保存済み。


### 2026-09-27 15:51 JST — queue #2 bootstrap解消・Lease修正、入力未成立で残確認継続

**queue #2：Prism bootstrap解消／製品Lease送信周期の最小修正・限定自動4/4 PASS。実clientはLease受信・更新・native終了/revokeまで限定確認、実入力/移動はUNVERIFIED。** 新run `20260927-151029` と `20260927-153334`。後者はprepare1・exact goal1・allow Lease36件（初回1＋更新35）・revoke1、HP20不変。W操作は要求したが発動中の入力・位置変化を観測できずhelperが停止。今回client起動3回の上限で追加起動せず、両worldを通常保存/Quit・readonly照合・PID終了まで保全。製品same-JVM／positive disk再読込はNOT RUN。**#1 COMPLETE／#2 INCOMPLETE／残8**。[§14.40](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-client-bootstrap-lease-fix-result)。Gate A14・旧製品19/core60は当時のPASS維持。購入停止/SP保護・SAFE DESIGN PROVEN=NO・RC=NOを維持。

最新製品：[foodhealing-3.0.0.jar](../build/libs/foodhealing-3.0.0.jar) **298,958 bytes /189 entries / SHA-256 `37E300EAA0663FDCDF445317C9F14F3BF13C839B21E4DD5679229B32386592E3`**、protocol **7**。現物/配置不変。[今回照合](../build/verification/uom-production-os-input-20260928-203401/audit/reviewed-results.json)：763保護hash差分0・配置10Jar一致（旧run/旧instanceを含む）。検証MOD v20260928.203401（71,717 bytes/27entries）だけ限定offline compile/reobf、測定前repair1/2、測定後repair/retry0。製品build・既存suite0。

詳細・失敗経緯・終了証拠は[共通計画§14.40](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-client-bootstrap-lease-fix-result)／[集計](../build/verification/uom-production-lease-20260927-153334/audit/reviewed-results.json)。旧14:48の3起動FAIL・旧PASSは変更しない。


### 2026-09-27 14:48 JST — queue #2 Gate A完了・製品実装と限定自動／client起動STOP

**queue #2：Gate A LIMITED PASS / COMPLETE、TimeStop製品実装・限定自動検証まで完了、client bootstrapでSTOP**。run `20260927-134126`。旧9記録を維持しprior UNKNOWN・D4の3入力・D5の計5記録を追加。製品focused unit9、実FE/EL専用19ケース、build/foodHealingUnitTest/check、vanilla/TaCZ各60/60 PASS。Prism clientは3起動でworld前に停止し、最終原因は独立agentの早期ASM読込とForgeWrapperのmodule登録衝突。**製品client・製品same-JVM/保存positive再読込は未確認、#1 COMPLETE／#2 INCOMPLETE／残8**。[§14.39](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-production-followup-gate-completed-client-startup-stop)。旧Gate A残件を再開しない。購入停止/SP保護・SAFE DESIGN PROVEN=NO・RC=NOを維持。

最新製品：[foodhealing-3.0.0.jar](../build/libs/foodhealing-3.0.0.jar) **298,958 bytes /189 entries / SHA-256 `37E300EAA0663FDCDF445317C9F14F3BF13C839B21E4DD5679229B32386592E3`**、protocol **7**。現物/配置不変。[今回照合](../build/verification/uom-production-os-input-20260928-203401/audit/reviewed-results.json)：763保護hash差分0・配置10Jar一致（旧run/旧instanceを含む）。検証MOD v20260928.203401（71,717 bytes/27entries）だけ限定offline compile/reobf、測定前repair1/2、測定後repair/retry0。製品build・既存suite0。



### 2026-09-27 13:31 JST — queue #2 Gate A部分成立・FE native起動STOP

**queue #2 Gate Aは部分成立・production前STOP**。run `20260927-130322`：正規初回goal、frameなしpositive拒否、異常RETURN、参照不一致、ABSENT/ZERO・player空tag、positive deserialize拒否とterminal回帰の計9記録PASS。D4はFE内部self-attach失敗→Mixin plugin初期化失敗→optional SlashBlade classloadingでbootstrap exit1、測定UNVERIFIED。D5 NOT RUN。prior UNKNOWNの入口guardにも補助の静的残件あり。**Gate A全体INCOMPLETE／製品変更0／#1 COMPLETE維持／#2 INCOMPLETE／固定queue残8**。[結果・停止根拠§14.38](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-entry-gate-a-partial-result)。購入停止/SP保護・SAFE DESIGN PROVEN=NO・RC=NOを維持。

補助repair1（測定前のMC継承メソッドowner/reobfとattempt証拠分離）、offline compile/reobf成功。worldへ入った6processは通常保存/stop/exit0、D4の1processはworld生成前exit1で正常終了PASSではない。7 PID全終了確認。製品229,494 bytes/150 entries/SHA256 `5C1A716EEDE7AEB4449B36EEFFBDDA156176B16B60A1676229224AA3390DB327`不変。製品build/unit/check・既存回帰・実client0。9限定PASSの対象/限界は§14.38、途中FAILを保全。


### 2026-09-27 12:43 JST — queue #2 mappingのauthority入口差異STOP

**queue #2は写像照合を開始し、製品変更前にSTOP（STATIC MAPPING BLOCKER）**。承認原Jarの通常UOM初回goalはcount180→use(true)の順で、現候補はcommon-use HEADのreconcileによりUNKNOWNとなる。また候補のdeserializeは空NBT/zeroもcontext全体のterminalFaultへ入れる。開始由来・deserializeの認定条件を変えずに常時production化することはできないため、利用者指定のauthority semantics変更時STOPを適用。[根拠と次の範囲](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-production-mapping-stop)。製品変更/build/test/ゲーム起動0、今回の動作FAIL/PASSは付けない。**#1 COMPLETE維持／#2 INCOMPLETE／固定queue残8**。購入停止/SP保護・SAFE DESIGN PROVEN=NO・RC=NOを維持。

### 2026-09-27 12:23 JST — queue #1 source分離・残確認LIMITED PASS

**queue #1 COMPLETE：SAME-JVM AUTHORITY ISOLATION FOLLOW-UP LIMITED PASS＋SAFE-SIDE DENY**。新run `20260927-121020`。候補authority核心source不変、補助のserver/client sourceを分離し、Bの5 ENDすべてでnative lookup同一実ref（前後10観測）・旧authority認可0を確認。cross-write両方向0、seal後native cleanup1回でserver source count162→0・globalfalse・dimension Set空。通常A/B保存、readonly disk count0/本人HP20・P/M ON・SP0/103、Quit/PID終了まで完了。[§14.36](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-same-jvm-source-followup-completed) / [実測集計](../build/verification/uom-same-jvm-isolation-followup-20260927-121020/audit/reviewed-results.json)。旧§14.33 SAFETY FAIL・§14.35 UNVERIFIEDと既存PASSを履歴保全。**固定queue残8、次は#2（未開始・別承認）**。SAFE DESIGN PROVEN=NO / ownership BLOCKED。

補助version20260927.121020、105,081 bytes/46 entries、SHA256 `D52585B51D736056C0761C7BCE8D1D5FFB56272C5D401C53AA6F4C81B76BD097`。repair0、核心26class byte同一・3classはrun ID定数だけ変更。製品229,494 bytes/150 entries/5C1A716E…90DB327・旧証拠含む3,037files不変、SPEC変更なし。COMPUTER USE＋AUTOMATED、HUMAN0。通常12:18:11 Quit、12:18:23 PID24356終了確認。新規製品build/既存suite0、queue #2未開始。


### 2026-09-27 11:10 JST — queue #1 isolation follow-up・総合UNVERIFIED

**queue #1 follow-up：総合UNVERIFIED（補助のserver/client source参照混同）**。run `20260927-104834`。実server/levelでState・engine/faultを隔離し、旧FAILと同じB deserialize経路の `oldStateSameActualRef=false`、旧fault持越し0、5 ENDの旧認可0は実測成立。一方client EntityJoinLevelEventがdriverのserver用source handleを上書きし、継続source観測とseal後cleanupが欠落。native cleanup0、保存source count160。値修復/測定後再build/再試験0、通常A/B保存・Quit/PID終了済み。[共通計画§14.35](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-same-jvm-isolation-followup-result) / [集計](../build/verification/uom-same-jvm-isolation-20260927-104834/audit/reviewed-results.json)。**全run LIMITED PASSへ昇格せず残9工程**。旧SJ1 SAFETY FAILと既存PASSを保持。製品229,494 bytes/150 entries/hash、2,563保全files不変。SAFE DESIGN PROVEN=NO / ownership BLOCKED。

補助version20260927.104834、98,747 bytes/45 entries、SHA256 `FF35639674C7BA0F483A751A9DBDE28B9FF6C395A3A0D7C8237D07D191E6F01F`。補助repair cycle0、製品build/既存suite0。SPEC§3の200は現production/runtime旧baseline、正式releaseでは§14.2へ置換と明記（1000は未LOCK）。購入gate/SP保護、RC=NO、REAL2CLIENT=BLOCKED、個別開始条件を維持。


### 2026-09-27 10:30 JST — SJ1 SAFETY FAIL・正常終了／正式release scope更新

**SJ1実行完了：SAFETY FAIL（same-JVMの旧State current化）**。run `20260927-095730`、1 PID/JVM・A/B各1・新world1/計2 load。A origin/G1/実client Lease成立後、B native deserialize内で同dimensionの旧State実refをnew ServerLevelへ返した（event5005）。old/new faultはBOTH。結果seal→native cleanup1→通常保存/readonly照合→Quit/PID終了完了。[共通計画§14.33](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-same-jvm-sj1-result) / [証拠集計](../build/verification/uom-same-jvm-20260927-095730/audit/reviewed-results.json)。製品229,494 bytes/150 entries/hash不変、2,085保全対象files差分0。**SAFE DESIGN PROVEN=NO / ownership BLOCKED継続**。次はこの実反例のisolation修正＋限定再検証だけ（別承認、今回は未着手）。

**利用者の正式release scope変更**：Pam's Trees木になる収穫物＋原木のshapeless増産とNutrition/Food-Level新食義countは、共に **v3.0.0 FORMAL RELEASE REQUIRED / IMPLEMENTATION PENDING / DETAILS PARTIALLY UNLOCKED**。[SPEC§14.1–14.2](SPEC.md#14-v300-formal-release-required-additions)。Pamの対応版/whitelist/vanilla provenance/log境界/実装形式は未LOCK。count単位・exactly-once・Root分離はLOCK、1000 thresholdはPROVISIONAL、最終閾値/partial count移行/既存Config処理はrelease前の判断待ち。今回製品実装0。旧8/27候補履歴は当時の記録として保持。

### 2026-09-22 15:51 JST — 同JVM static寿命のREAD ONLY解析・SJ1設計

[詳細正本§14.32](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-same-jvm-context-design)。dimension-only State再利用、engine共通fault、history/issued・client cacheの寿命を静的整理。次候補は新client JVM1・integrated A/B・新world1の通常再open1。old fault/new deserialize faultを分ける順序、限定非再利用PASS/安全側DENY/State隔離FAIL/UNVERIFIEDを設計。A1/D1-W/R等のPASSは不変、新run/helper/build/game/再試験0。3文書の更新だけで総合gateを解除していない。


### 2026-09-22 15:23 JST — A1別JVM process reloadの限定完了

新run20260922-145728、A/B各1起動・新world1/通常再読込1。Aの有効G1とdisk179、Bのpositive deserialize・5 END非復活、SAFE-SIDE DENY、両通常保存/exit0を確認。[結果正本§14.31](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-process-reload-a1-result) / [独立証拠照合](../build/verification/uom-process-reload-20260922-145728/audit/reviewed-results.json)。A終了中のnative use(false)/count0は隠さず、保存済count179と区別。測定前補助repair1、製品/旧2,029files不変、既存suite/実client再実行0。§14.30のDESIGN ONLYは当時の履歴として保持。総合安全性/購入gateは解除していない。

### 2026-09-22 13:42 JST — D1-W follow-up W3/W4限定完了

**今回：D1-W未完了W3/W4を新run `20260922-132519` の別dedicated/worldで各1回実施し、双方LIMITED PASS。既存W1/W2・STATIC・有限negative契約10/10を再実行せず保持し、D1-W候補の限定検証を完了**。[共通計画§14.29](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-terminal-witness-followup-result) / [独立証拠照合](../build/verification/uom-terminal-witness-followup-20260922-132519/audit/reviewed-results.json)。W3は同Cow positiveのままUOM自然terminal・誤ALLOW/再grant/新epoch0、W4は固定throw/native catch各1・witness1/0/0・cleanup後5 ENDのfault非回復。両server通常保存/stop/exit0、製品/旧証拠1,481files不変。旧§14.26 FAIL・§14.27設計・§14.28環境STOPは履歴保全。**SAFE DESIGN PROVEN=NO、ownership BLOCKEDを維持**。

新helper `uom_witness_verification /20260922.132519`、77,577 bytes/33 entries、SHA-256 `57A4EFE0BA98E92022188D2D6245607F0E2449B4FC23051D96DF7093B74FBF05`。変更は新rootの環境/Cow guard/run配線・W4終了後観測だけ。核心classはbyte同一、限定offline compile/reobf成功・helper repair0。W3はCow80/UOM38→Cow42/UOM0、witness0/0/0。W4はnative1→0 RETURN→mint→throw→native catch、1/0/0、END139–143 quietでもfault/UNKNOWN/DENY。両run HP20・P/M1ON・SP0/103保存、通常exit0/PID終了。実client/実認証TCP/旧suite/再読込0。変更前3文書は[保全](../build/verification/uom-terminal-witness-followup-20260922-132519/before/docs/)、製品229,494 bytes/150 entries/5C1A716E…90DB327不変。

### 2026-09-21 21:31 JST — TimeStop lifecycle残件のREAD ONLY優先整理

[共通計画§14.25](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-lifecycle-priority-terminal-plan)へA–Gの既知/未確認/依存と次候補D1を集約。選択はepoch終端を確定する2 UOM順次terminal countdown、dedicated1 run候補。実行0・新runtime判定0。§14.11–14.24と全既存PASS/FAILを保持。変更は3文書とその変更前backupだけ、製品/補助/compat/外部原物不変。

### 2026-09-21 17:06 JST — 独立login同期compatとreal reconnect R4–R9限定完了

[共通計画§14.24](MASTERY_IMPLEMENTATION_PREPARATION.md#endinglib-login-compat-real-reconnect-result) / [実測集計](../build/verification/uom-reconnect-20260921-162556/audit/reviewed-results.json)。STATICと新dedicated A–J 10/10を通過後、新run `20260921-162556` でR1–R3をSETUP ONLY、R4–R9を限定確認。R4はnative Skill→Dimension各1・level非null・約0.281秒収束、G1不可逆失効・NO_GRANT/UNKNOWN。R5/R6旧session拒否、R7通常歩行、R8新E2/G2、R9実歩行を確認。prepare1/native開始2/終了2、seal、16:55通常保存/全dimension保存・server exit0・Minecraft通常Quit/両PID終了、readonly保存一致。

独立compat/検証専用補助のみcompile/reobf。最大3 repair cycle内でSTATICのclasspath不足と初回新run `161503` の屋根先行spawn準備不備を修正し、全FAIL/停止証拠を保全。製品229,494 bytes/150 entries/5C1A716E…90DB327、保全対象380ファイル、実配置19Jarは不変。[変更前3文書](../build/verification/endinglib-login-compat-20260921-155452/before/)。旧§14.22/14.23は変更せず、既存suite再実行0。SAFE DESIGN PROVEN=NO、ownership BLOCKED、P/T購入停止/SP保護、RC=NOを維持。

### 2026-09-21 10:56 JST — real reconnect最小手順のREAD ONLY整理

[共通計画§14.15](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-real-reconnect-runbook)へ実MC/Forge logout順序・primary/defensive revoke・session参照・native全終了/new epoch・client cache/入力bridge候補・R1–R9・停止/通常終了を集約。[TEST_PLAN §27](TEST_PLAN.md#uom-reconnect-planned-cases)へ採否とNOT RUNを記録。EndingLibraryのlogin SkillPacketとdimension packetが別である静的懸念を保全し、実再現済みとはしない。外部MOD6本/製品hash、Prism/Java、Windows参照library95、asset3,598存在/size、dedicated参照Jar58を限定確認。新外部artifact不足なし、現認証有効性は未確認・authファイル読取なし。

[変更前保全・読取記録](../build/verification/uom-reconnect-plan-20260921-103304/) / [不変照合](../build/verification/uom-reconnect-plan-20260921-103304/audit/final-invariants.json)。製品source/test/build.gradle/Jar/購入gate不変、新helper/packet作成0、compile/build/test/game/server/Prism起動0。今回の完了は計画と3文書更新だけ。旧§14.11–14.14のPASS/FAIL/NOT RUNと既存L2/Trial/C/sealを維持し、安全性証明・購入readyへ昇格しない。

### 2026-09-21 10:27 JST — source native dimension遷移の限定検証

[§14.14](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-native-source-dimension-result)へ実portal/Forge経路、一般Cow2転送のinstance/UUID/count、UOM native非対応、FOREIGN転送後player/vehicle各1拒否、観測限界を集約。新出口なし対照でPortalInfo null/Forge cancelなしを確認したが、旧runの最終原因を遡及確定しない。総合SOURCE DIMENSION TRANSITION VERIFIED/SAFE DESIGN PROVENはNO、ownership BLOCKED継続。補助専用offline compile/reobf成功、新server通常save/stop/exit0。製品不変・3文書更新のみ。[判定済み証拠](../build/verification/uom-dimension-20260921-101349/audit/reviewed-results.json) / [不変照合](../build/verification/uom-dimension-20260921-101349/audit/final-invariants.json)。


### 2026-09-20 22:50 JST — vehicle専用DENY境界の限定検証完了

[§14.13](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-vehicle-deny-boundary-verification)へ実call flow/限定hook・held payload/identity・A–I・native/vanilla対照・16拒否の全state不変を集約。raw PASS32行＝vehicle20（負対照1含む）/held整合2/passenger1/vanilla6/player連携3、別に未LOCK1/観測1。製品gameplay32件ではない。run01はnative Player優先の乗員順序により準備assertion FAIL、run02は別Playerを先頭に置いて未完了3件だけ確認。guard/ledger compiled bytes同一、両server通常保存/exit0。旧反例/17枠/失敗を保持。製品source/Jar/gate不変。[最終照合](../build/verification/uom-vehicle-deny-20260920-223504/audit/final-invariants.json)。

### 2026-09-20 22:26 JST — server移動境界の限定成立とvehicle反例

[§14.12](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-server-movement-boundary-verification)へ実call-site・17枠・負対照・state不変・vehicleの具体的未被覆を集約。別helperでplayer拒否は成立したが、vehicle packetはFOREIGN/nativefalseでもBoat +0.0625、native positionRiderの明示観測で本人も追随。全体BLOCKEDのまま停止。01は補助readonly save呼出しreobf不整合で0件、02はraw PASS29行（移動26に負対照1を含む/時系列2/速度1）＋vehicle反例1＋観測1。生データ/失敗/専用Jarを保全し、製品効果PASSへ転記しない。両server全dimension通常保存/stop/exit0。製品hash/source/test/build.gradle/gate不変、旧suite/client0。[最終照合](../build/verification/uom-movement-boundary-20260920-220541/audit/final-invariants.json)。

### 2026-09-20 21:47 JST — UOM TimeStop ownership設計fixtureとserver移動権威の反例

新規 `uom-timestop-design-20260920-212237` 内だけに別補助・専用offline buildを作成。common use/count/deserialize observerはreadonly、native状態変更はfixtureの試験入力に分離。最低20枠の18を限定実測、1台帳喪失相当、1source dimension遷移未成立。P候補許可とnative canMoveがfalseでもnative handlerがX=0.5→0.5625を受理したため、最小canMove-only案はFAIL/BLOCKED。詳細・raw PASS除外・旧assertion FAILは[共通計画§14.11](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-timestop-ownership-design-verification)と[review](../build/verification/uom-timestop-design-20260920-212237/audit/reviewed-results.json)。server-01/02は通常save/stop/全dimension保存・Java exit0。製品147基準ファイルの変更は3文書のみ（最終照合参照）、製品Jar/source/test/build.gradle/gate不変、client/Prism・旧suite0。旧C/seal等の完了を維持。「外部実物待ち」の現行行だけを修正し当時の履歴は保持。

### 2026-09-20 21:07 JST — FE依存実ロード・追加静的監査、TimeStop所有関係で停止

[共通計画§14.7–14.10](MASTERY_IMPLEMENTATION_PREPARATION.md#fantasy-ending-dependency-timestop-result)へ6原物hash/metadata、nested実採用、TimeStop call graphと所有情報の限界、Eldritch外部数値経路を記録。追加必須artifact不足なし。[loader原ログ照合](../build/verification/fantasy-ending-20260920-204205/audit/loader-smoke-reviewed.json)：20:44:56 Done→20:47:30通常stop→全dimension保存/exit0。元監視補助のready文字列不一致FAIL/timeout receiptを保持。新規loader worldだけを使用した。

TimeStopはglobal/dimension/countへ統合され、継続source集合が保存/同期されない。候補の一時台帳でも全遷移の完全性未立証。利用者の指定停止条件 **BLOCKED - TIME STOP SOURCE OWNERSHIP** を適用し、広い回避/製品変更なし。新規FE攻撃統合0/NOT RUN、build/unit/check/既存回帰再実行0、実client0。旧133/60/49/86・各client・C/sealを保持。最終不変照合は[final-audit](../build/verification/fantasy-ending-20260920-204205/audit/final-invariants.json)、変更は3文書と新規verification記録のみ。


### 2026-09-20 19:25 JST — 最低対応範囲/optional購入方針LOCK、Fantasy Ending原物の静的監査

[共通計画§12.5](MASTERY_IMPLEMENTATION_PREPARATION.md#mastery-purchase-policy-decisions)へ問1=B／問2=P=A・T=Aを反映。PにFE2.7.20 UOM由来secondaryを必須追加、TはL2 6 ID。将来ready化後はMOD構成だけで購入拒否せず、未対応Adapterは無介入。今回gate/SPは変更しない。

[§14](MASTERY_IMPLEMENTATION_PREPARATION.md#fantasy-ending-uom-readonly)に原物一致（18,417,128 bytes/1,379 entries/E32FD4BA…E5D141）、実攻撃graph・6新規付与site・numeric境界・S/C・TimeStop外部不足・試験案を集約。Star直撃4効果は非UOM分岐と訂正し、正式UOM-owned vanilla WitherSkull WITHERを追加確認。TimeStopはEndingLib原物不足、Eldritch外部処理はIron's Spells不足。未確認を推測で埋めず、FE未実装/試験未実行のまま。

[調査・保全root](../build/verification/fantasy-ending-readonly-20260920-185700)／[最終不変照合](../build/verification/fantasy-ending-readonly-20260920-185700/audit/final-document-validation.json)。3文書と読取記録/バックアップのみ更新。javap原物close失敗は同hash readcopyで再取得、ゲーム/test失敗ではない。旧133/60/49/86、各限定実client・C/seal/保存終了を保持し、再実行0。製品/source/test/build.gradle/Jar/Config不変、Security scan/ネット取得/game/helperなし。


### 2026-09-20 17:57 JST — 購入readinessの残判断を2問へ集約（READ ONLY）

[共通計画§12.5](MASTERY_IMPLEMENTATION_PREPARATION.md#mastery-purchase-policy-decisions)へ正式候補表・未対応33 IDの分類・P/T別optional方針表を集約。最低範囲LOCKと購入処理実装/動作PASSを分離。Hyperlink/Fumetsuの目標は維持し、未受領を自動購入/release blockerにしない。問1/問2は未回答。§12の新2 IDの古い停止表記を§13.10/13.11完了へ訂正し、過去の4停止記録を保持。

[読取・保全記録](../build/verification/mastery-purchase-readiness-readonly-20260920-175400/audit/read-only-inputs.json)。変更は3文書と調査記録/バックアップのみ。製品/source/test/build.gradle/Jar/Config/gate不変、build/test/ゲーム/Prism/server/helper/外部downloadなし。C/seal/保存終了・prepare1/native hit3・既存PASSを維持。

<a id="evidence-l2-weakness-wither-c-20260920"></a>

### 2026-09-20 17:38 JST — 同runの歩行後C限定PASS、seal・通常保存終了

run20260920-163342。旧A/B/T限定PASSを維持し、今回A/B/TはC準備のみ。HUMAN通常歩行後のserver8604/client8603 paused個別snapshot→新規停止2組9093/9094→C直前9829安全確認→9830にC1回、HP14→11/numeric3/対象付与0。17:29:12 seal/prepare1/hit3→17:29:45全保存→readonly本人HP11/3OFF/SP0・603・同source空trait/marker2 ID→17:30:09Quit/17:30:23 PID1168不在。リセット後の同run継続で、二重実行/修復/再読込/cleanupなし。

詳細は[共通計画§13.11](MASTERY_IMPLEMENTATION_PREPARATION.md#l2-weakness-wither-client-c-completed-result)、[集計](../build/verification/l2-weakness-wither-client-20260920-163342/audit/completed-run-analysis.json)、[個別snapshot](../build/verification/l2-weakness-wither-client-20260920-163342/audit/human-walk-post-pause-snapshot.json)、[保存](../build/verification/l2-weakness-wither-client-20260920-163342/audit/final-save-values.json)、[不変照合](../build/verification/l2-weakness-wither-client-20260920-163342/audit/final-invariants.json)。helperのみ44,267 bytes/18 entries/A7B86765…16FB55。製品5C1A716E…DB327不変、旧4run853ファイル不変・追加0、既存suite再実行なし。両購入停止/SP保護/RC=NO/REAL2CLIENT=BLOCKEDを維持。


<a id="evidence-l2-weakness-wither-level0-20260918"></a>

### 2026-09-18 22:16 JST — 新2 ID：level0準備・GUI/A/B/T限定PASS、C前player guard停止

- 新run20260918-213302のsource正式level0初期化をhelperだけ最小修正し、専用offline compile/reobf成功。製品/source/test/gate不変、既存suite再実行なし。[差分](../build/verification/l2-weakness-wither-client-20260918-213302/audit/helper-changes.diff) / [build](../build/verification/l2-weakness-wither-client-20260918-213302/audit/helper-build-01.log) / [実測](../build/verification/l2-weakness-wither-client-20260918-213302/audit/stopped-run-analysis.json)。
- 自然接地3695/3696→NoAI後3697/3698→正式trait後3699/3700の新規snapshot、prepare1/SP0・603、GUI非連動/同期2組を確認。A6133で20→17、B7537で17→14と各対象effect付与/実remove1、T8369でnative remove各1/元sync/client空cap。通常numeric各3・Hurt/Damage各1、damageTypeと3UUIDを分離観測。
- 3OFF収束8954後、通常同床tpの直後9745で`unsafe player position`。C0/sealなし。失敗瞬間player operandが不足し、後時点の保存OnGround0で原因を補完しない。A/B/Tを保持し、全体PASSや製品FAILへ拡張しない。
- 22:07:30.395全保存、readonly保存Health14/3OFF/SP0・603、同sourceHP20/lv0/trait空/marker2 ID保持、22:08:19.144 Stopping!→22:08:23 PID12604不在。再読込/範囲外/cleanup/修復/再試行0。[保存](../build/verification/l2-weakness-wither-client-20260918-213302/audit/final-save-values.json) / [log](../build/verification/l2-weakness-wither-client-20260918-213302/audit/game-final.log)。
- [不変照合](../build/verification/l2-weakness-wither-client-20260918-213302/audit/final-invariants.json)：製品等146、旧証拠304、旧3 run606・追加0、旧instance metadata24件不変。詳細・未確認・承認境界は[共通計画§13.10](MASTERY_IMPLEMENTATION_PREPARATION.md#l2-weakness-wither-client-level0-partial-result)。旧3停止/133・60・49・86/旧実client PASSと全gateを維持。

<a id="evidence-l2-weakness-wither-contact-timeout-20260917"></a>

### 2026-09-17 22:25 JST — 新2 ID実client再試験：自然接地timeout・通常保存終了

- 承認された別run `20260917-220121` で、補助の初期接地待機・個別operand記録だけを修正。helper限定offline compile/reobfは初回成功。製品/購入gate/既存suite不変。[helper差分](../build/verification/l2-weakness-wither-client-20260917-220121/audit/helper-changes.diff) / [実測集計](../build/verification/l2-weakness-wither-client-20260917-220121/audit/stopped-run-analysis.json)。
- prepare1回・SP0/603/P/M ON/T OFF、source2trait各rank1。gameTime2804〜3004の201回＋failure直前同ticksnapshotでOnGround=false、床差0/位置固定/速度0/HP20/非水/非火災/非窒息/外部敵0/foreign damage・heal0。自然接地timeoutによりprepare-completeなし、GUI/A/B/T/C/seal未実施、native攻撃0。NoAIとnative physicsの分岐を実ロード版bytecodeで照合。observerの別の静的不備も未修正で記録。
- 22:17:25.206 All dimensions are saved→保存readonly照合→22:17:55.386 Stopping!→PID11668終了。本人Health20/3取得/SP0・603/P/M ON・T OFF、同sourceHP20/2trait保持/markerなし。再読込・範囲外・cleanup・修復・再試行0。
- [最終不変確認](../build/verification/l2-weakness-wither-client-20260917-220121/audit/final-invariants.json)：製品等146/旧証拠304/前run全198/旧instance metadata20件不変。前runへの追加ファイル0。既存Invader cachedVolatile例外を復元せず保持。詳細と次の承認境界は[§13.8](MASTERY_IMPLEMENTATION_PREPARATION.md#l2-weakness-wither-client-contact-timeout)。過去133/60/49/86・旧実client PASS、購入停止/SP保護・RC=NO/REAL2CLIENT=BLOCKEDを維持。

<a id="evidence-l2-weakness-wither-client-stopped-20260917"></a>

### 2026-09-17 21:50 JST — 新2 ID実client prepare後の補助guard停止・正常保存終了

- 明示実行承認により新run `20260917-211821` を作成。helperのみoffline compile/reobf成功、指定MODの実ロード、生成前GUIの自然湧き/自然回復false・Survival/Normal・構造物OFFと閉鎖石室を確認。
- prepare1回で3取得/SP0・603/P/M ON・T OFF、native2trait各rank1を準備。直後`sourceStable`が`unsafe source position`でFAILED。保存sourceはOnGround0。attack0、A/B/T/C・GUI比較・sealは未実施。停止時の複合guard operandは個別未採取、製品不具合の証拠とはしない。[実測集計](../build/verification/l2-weakness-wither-client-20260917-211821/audit/stopped-run-analysis.json)。
- 通常保存21:37:24（All dimensions are saved）→readonly照合→Quit21:38:05→PID24028終了。本人Health20/DeathTime0/MAX属性20、3取得/SP0・603/P/M ON・T OFF。sourceHP20/NoAI/Persistence/2trait保持、T markerなし。再読込0、修復/再付与/cleanupなし。
- 製品/既存source等146ファイルと旧証拠304ファイルhash不変。旧instance metadata18件中1件にLWJGL cachedVolatile削除の[例外](../build/verification/l2-weakness-wither-client-20260917-211821/audit/old-instance-metadata-exception.json)を検出、書換主体未確認、差分を戻して隠さない。旧world起動なし。3文書の変更前はrun内before/docsへ保全。詳細・helper識別・次回条件は[共通計画§13.7](MASTERY_IMPLEMENTATION_PREPARATION.md#l2-weakness-wither-client-stopped-result)。過去133/60/49/86・旧実client PASSと購入停止/RC=NO等を維持。

<a id="evidence-l2-weakness-wither-client-plan-20260917"></a>

### 2026-09-17 21:14 JST — 新2 IDの限定実client計画完成（実行なし）

- [共通計画§13.6](MASTERY_IMPLEMENTATION_PREPARATION.md#l2-weakness-wither-client-plan)へ取得準備1回・同source2 ID/native3攻撃・ID別観測・GUI/実client収束・停止/保存終了を集約。[TEST_PLAN §18](TEST_PLAN.md#l2-weakness-wither-limited-client-plan)は全ケースNOT RUN。追加価値のない再読込/範囲外・旧試験の再演を含めない。
- Statusの旧「L2代表4種P/Tを実装」を現行6 IDへ修正。旧95/95・実client/失敗記録は当時のまま保持。最新Jar5C1A…DB327・229,494 bytes/150 entries、既知MOD/runtimeの存在を[読取照合](../build/verification/l2-weakness-wither-client-plan-20260917-210626/audit/read-only-inputs.json)。追加artifact不足なし、別helperは次回承認後に新規作成する。
- [変更前3文書](../build/verification/l2-weakness-wither-client-plan-20260917-210626/before/docs/)を保全。今回の変更は3文書と読取記録のみ。製品/source/test/build/Jar/Config/購入gateは変更せず、helper作成・compile・試験・Prism/ゲーム/world起動なし。計画完成で停止、両購入停止/SP保護・RC=NO/REAL2CLIENT=BLOCKED等を維持。

<a id="evidence-l2-weakness-wither-completed-20260917"></a>

### 2026-09-17 20:34 JST — weakness/wither P/T追加・限定自動統合完了

- 19:48 checkpointから明示本文承認を受け続行。製品Adapter1＋自動fixture1の変更で、6 ID契約のL2 **133/133**。旧95のweakness対照は過去契約として残し、新契約だけlevitationへ置換。詳細は[共通計画§13](MASTERY_IMPLEMENTATION_PREPARATION.md#l2-weakness-wither-implementation)。
- build/unit/check成功、vanilla/TaCZ各60/60、最終配布Jar Cube49/49・Invader86/86。旧認証審査拒否・Gradle環境失敗に加え、初回vanillaは60件PASS後に公開鍵取得ERRORでtask FAILを保持。新規試験JVMのlocalhost空公開鍵応答とloader更新確認停止で、製品やERROR検査を変えず解消。認証/TCPのPASSではない。
- 新Jar **229,494 bytes /150 entries / SHA256 5C1A716EEDE7AEB4449B36EEFFBDDA156176B16B60A1676229224AA3390DB327**。最終3実MOD環境と同hash、差分はmanifest/Adapter1class、metadata/Mixin/refmap/reobf/非混入確認。[Jar監査](../build/verification/l2-weakness-wither-20260917-193800/audit/final-jar.json)。
- 全server通常保存/停止・exit0、最終該当Java0。Invader既存UUID等のWARNは維持。[全run](../build/verification/l2-weakness-wither-20260917-193800/audit/test-summary.json) / [process](../build/verification/l2-weakness-wither-20260917-193800/audit/process-final.json)。新2 ID実client NOT RUN、旧実client/95の履歴不変。購入gate/SP/build.gradle/Config/既存artifact不変、他trait未着手、RC=NO。


<a id="evidence-l2-weakness-wither-20260917"></a>

### 2026-09-17 19:48 JST — weakness/witherコード反映、build実行制約で検証待ち

- [共通計画§13](MASTERY_IMPLEMENTATION_PREPARATION.md#l2-weakness-wither-implementation)。2 IDの現物/依存照合、Adapter1＋隔離自動fixture1を変更。旧95の対象外対照を新契約だけlevitationへ置換、旧証拠不変。
- Gradle8.8/plugin解決と8.1.1/native DLLでbuild開始前停止。権限付き実行は自動承認審査拒否2回、本文承認確認待ち。新しいcompile/test PASSなし。
- 製品Jar229,423 bytes/150 entries/3807DECC…76DE6不変、新試験server・実client/Prism起動なし。購入gate/SP/build.gradle/依存不変。既存PASS・途中失敗履歴は保持。



<a id="evidence-mastery-readiness-20260916"></a>

### 2026-09-16 22:31 JST — 両極意readinessのREAD ONLY棚卸し

- [共通計画§12](MASTERY_IMPLEMENTATION_PREPARATION.md#mastery-readiness-inventory)へA〜E、効果別表、L2正式39 IDの機構/cleanup/side、T基盤と対象追加時だけの仕事、購入/release/optional/artifact/判断待ちを分離して記録。
- 固定IMPLEMENTATION_PENDINGが直接の購入拒否原因。正本のsupported対象の最終v3 ID/版表は未LOCKで、未対応35 IDを全て購入/release必須へ昇格しない。Hyperlink/Fumetsuは承認artifact待ち。次候補は既存artifactのweakness/wither、実装開始なし。
- 既存86/49/95/core60、Trial Cube、侵略者旧F1/F2/S1＋新S2/H1/seal/cleanup/保存終了、L2 §10.11のPASSを保持。旧失敗/未実施履歴も保全。
- 調査は現物hash・登録/追加trait/周辺APIのjavapと既存source/証拠読取のみ。製品source/test/build.gradle/Jar/Config/購入gate不変、build/試験/ゲーム/新helper/downloadなし。[保全・読取記録](../build/verification/mastery-readiness-inventory-20260916-221334/audit)。


<a id="evidence-invader-client-20260916"></a>


### 2026-09-16 21:51 JST — 侵略者新run S2/H1・seal/cleanup・通常保存終了を限定完了

- 新run `20260916-211828`。[共通計画§11.8](MASTERY_IMPLEMENTATION_PREPARATION.md#trial-invader-client-followup-result) / [実測集計](../build/verification/trial-invader-client-20260916-211828/audit/completed-run-analysis.json)。旧F1/F2/S1とL2 §10.11は再試験なしで保持。旧S2 UNVERIFIED・H1 NOT RUN・cleanup FAIL/失敗worldも変更しない。
- helperだけに全physical operand記録・100tick/独立10秒deadline・native後の限定自然収束・所有actor setRemoved cleanupを反映し、local offline限定compile/reobf成功。製品build/既存suite再実行なし。
- prepare1回/SP0・103・P/M両ON/Truth・Root未取得、外部flag操作0。準備用Small1回でHP20→18.5/Soul0/flagfalse（新PASS数へ追加しない）。GUI M OFF→S2：Soul0→0.01/HP18.5→17/実hurt・Hurt・Damage各1×1.5、3ticks/147.3694msで2連続収束。GUI M ON→H1：native callback1/Float.MAX_VALUE専用redirect各1/保護true/実hurt・Hurt・Damage・death0、Soul0.01/HP17、3ticks/146.7827msで収束。
- S2のnative後1tickだけonGround=false、位置(.5,80,12.5)・fallDistance0・水火なし・石足場/air feet/head・外部敵/被弾0を同時記録。本人値をsetterで補正せず、次の2tickでstrict接地trueを確認。旧停止原因を遡って確定はしない。
- SEALED：prepare1/preparationSmall1/measurementSmall1/Huge1/nativeTick0。今回所有sourceだけsetRemovedでworldAbsent、本人値不変。21:39:56.568全dimension保存→readonly level.dat/playerdata一致→21:40:32.676Quit Game・PID11644終了。保存HP17/MAX20/Soul約0.01/flagfalse/両ON/SP0・103/DeathTime0、source保存0。world再読込0。[保存](../build/verification/trial-invader-client-20260916-211828/audit/final-save-values.json) / [process](../build/verification/trial-invader-client-20260916-211828/audit/process-final.json)。
- 製品等144・旧証拠231・既知旧world等85・直前旧run等170ファイルは開始前hashと一致。製品Jar229,423 bytes/150 entries/3807DECC…76DE6不変、helper42,544 bytes/22 entries/820DDC31…3F7D0。COMPUTER USEとAUTOMATEDの実測、HUMANゲーム入力0。全機能完成・購入解放/RCへ昇格しない。

<a id="evidence-invader-client-20260916"></a>

### 2026-09-16 21:04 JST — 侵略者限定実client F1/F2/S1完了・S2後停止・正常保存終了

- 新run `20260916-201822`、MC1.20.1/Forge47.4.0/Java17.0.15、製品3807DECC…76DE6＋承認Trial1.4.9＋新補助のみ。[実ロード](../build/verification/trial-invader-client-20260916-201822/audit/runtime.jsonl)。旧3文書は[開始前保全](../build/verification/trial-invader-client-20260916-201822/before/docs/CODEX_STATUS.md)。L2 §10.11・旧試験/失敗履歴を保持。
- helperだけの限定offline compile/reobfを実行。初回起動は補助callback descriptor不足でworld/prepare前にFAIL、helperだけ修正して起動2へ。旧失敗Jar/logも保持。製品build/unit/check/Invader86/Cube49/L295/core60の再実行なし。
- prepare1回、P/M各Lv1・SP0/103・外部flagtrue初期準備1回。F1 true保持/false setter0、F2 GUI親OFFでnative false setter1、S1 Small Soul0・HP20→18.5/numeric1.5は独立server/client収束2回で限定PASS。S2 SmallはSoul0→0.01・HP18.5→17/numeric1.5のserver証拠あり。ただし直後の安全監視FAILでDONEなし、総合UNVERIFIED。H1未実施。[実測集計](../build/verification/trial-invader-client-20260916-201822/audit/stopped-run-analysis.json)。
- 安全監視はonGround/火/水・bubble/fallDistanceのどれが原因か記録不足。次caseを止めpause。cleanup1回もInvaderの`remove` no-opにより失敗し、別方法で強制削除していない。sealなし、actorは隔離worldに残る。[詳細/次の条件](MASTERY_IMPLEMENTATION_PREPARATION.md#trial-invader-client-partial-result)。
- 20:51:15.392全dimension通常保存、20:52:36.447Quit、PID12280終了。保存2箇所はHP17/MAX20/Soul約0.01/flagfalse・P ON/M OFF・SP0/103・DeathTime0。world再読込0回・再利用禁止。[保存値](../build/verification/trial-invader-client-20260916-201822/audit/final-save-values.json) / [Trial値](../build/verification/trial-invader-client-20260916-201822/audit/saved-trial-values.json) / [終了](../build/verification/trial-invader-client-20260916-201822/audit/process-exit.json)。COMPUTER USE=GUI/command/F2/保存終了、AUTOMATED=prepare/native/readonly観測、HUMAN入力なし。
- [製品144・旧証拠231・確認対象旧world等85ファイルの不変](../build/verification/trial-invader-client-20260916-201822/audit/final-integrity.json)。製品Jar再生成/差替え、購入解放、HP/Soul/receipt修復なし。限定PASSを侵略者全5件完了・Huge保護・自然boss・別player/全互換へ広げない。RC=NOと既存個別gateを維持。


<a id="evidence-trial-invader-client-plan-20260916"></a>

### 2026-09-16 20:09 JST — 侵略者限定実client計画の具体化（未実行）

- 19:45の正本から必要範囲をread-only照合。L2新runの完了と失敗履歴を維持し、A/B/T/Cの再計画/再実行なし。侵略者86/86の全PASS/保存記録、実装/bytecode、既知artifactを確認。[読取記録](../build/verification/trial-invader-client-plan-20260916-200036/audit/read-review.json)。
- [共通計画§11.6](MASTERY_IMPLEMENTATION_PREPARATION.md#trial-invader-client-plan)にF1/F2/S1/S2/H1の開始値/GUI/期待Soul・HP・flag、helper予定、readonly観測、停止/正常保存終了を集約。Smallは最大HP20なら1.5（自動fixtureの最大100/数値5を流用しない）、既定Soul0.01。HugeはFloat.MAX_VALUE限定例外と既存Soul非治療を確認する予定。
- Invaderは既定でNoAIを無視、Hugeは自然tickで反復するため、対象sourceだけの補助による進行隔離と未登録Beamの明示activateを提案。自然boss戦/描画の成功とはしない。既存86のowner/他player/force/致死対照は再現せず、world再読込は新規永続状態がないため必須から外した。通常保存/保存値照合/終了は含む。
- 新helper/instance/world未作成、製品source/test/build.gradle/Jar/Config・購入停止不変、build/unit/check/全既存suite・ゲーム/Prism/server起動なし。3文書の[変更前保全](../build/verification/trial-invader-client-plan-20260916-200036/before/docs/CODEX_STATUS.md)。計画完了/実行承認待ちで、実client PASSではない。



<a id="evidence-l2-client-completed-20260916"></a>

### 2026-09-16 19:45 JST — L2新runの観測補助修正・A/B/T/C・保存再読込を限定完了

- 新run `20260916-185745`。旧guts通知の誤集計を新補助だけで修正し、対象ID/case/entity/tick・実remove前後とreturn値を観測。製品/期待値/stop条件は変更なし。[変更差分](../build/verification/l2-client-20260916-185745/audit/helper-source.diff)、[専用offline build](../build/verification/l2-client-20260916-185745/audit/helper-build-01.log)。旧3文書は[変更前保全](../build/verification/l2-client-20260916-185745/before/docs/CODEX_STATUS.md)。
- A HP20→17/剣100/新規付与0、B HP17→14/毒200・鈍足160各1→通常浄化実除去/剣100→110→182、T製品除去と実client空trait cap/HP14・剣182不変、C全OFF HP14→11/追加作用0/剣182。各native攻撃1回・numeric3、A/Bの4trait保持、T後の4trait除去を別判定。詳細と未観測範囲は[共通計画§10.11](MASTERY_IMPLEMENTATION_PREPARATION.md#l2-client-completed-result)。
- prepare1/hit3/SEALED、全OFF・sourceからX90block、通常保存後に同worldを1回だけ再読込し同UUIDを再追跡。SP0/603・3つOFF・HP11/剣182・marker/空traits/対象外sourceデータを保持。[保存前後](../build/verification/l2-client-20260916-185745/audit/final-save-values.json)。19:28:06/19:31:21全dimension保存、19:31:41終了・PID484消滅確認。COMPUTER USE=通常GUI/command/F2/保存終了、AUTOMATED=準備/native呼出し/観測、HUMAN入力なし。
- [製品144ファイル・配置Jar不変](../build/verification/l2-client-20260916-185745/audit/final-integrity-followup.json)。旧run/instanceの基準115件中114件（world/補助/receipt/journal/screenshots/raw log/証拠を含む）は不変。**旧`mmc-pack.json`だけLWJGL cachedVolatile:true削除の差分**、実version等は同一、書換主体未確認・復元操作なし。[例外](../build/verification/l2-client-20260916-185745/audit/old-instance-metadata-exception.json)。事後集計の初回はこの差分で停止した記録を残し、例外を分離して測定照合を完了。旧runのA総合UNVERIFIEDは昇格しない。
- Invader86/Cube49/L295/core60/build/unit/checkの再実行なし。購入停止/SP保護・RC=NO/REAL2CLIENT=BLOCKED等は維持。可逆クラフト増加は既知許容仕様のまま。



<a id="evidence-l2-client-stopped-20260915"></a>

### 2026-09-15 22:57 JST — L2限定実client、A後の観測補助判定FAILで中断・正常終了

- 対象は3807DECC…76DE6の現行製品Jar。実施結果・停止原因・未観測・次の条件は[共通計画§10.10](MASTERY_IMPLEMENTATION_PREPARATION.md#l2-client-stopped-result)へ集約。旧3文書は[変更前保全](../build/verification/l2-client-20260915-220916/before/docs/CODEX_STATUS.md)。既存Invader86/Cube49/L295/core60を再実行していない。
- 新run `20260915-220916` だけを作成。生成前GUIの2gamerule OFF、閉じた石の床/壁/屋根、本人fresh・HP20/防御なし、NoAI Zombieを確認。本人prepare1回（SP0/603・P/M ON/T OFF）、通常GUI親OFF→ONを経てnative攻撃A1回。COMPUTER USEがGUI/command/F2/保存退出、AUTOMATED補助が準備/native呼出し/readonly観測。HUMAN入力なし。
- Aの実測はHP20→17、剣100→100、毒/鈍足の新規付与0、4trait保持。`MobEffectEvent.Remove(foodhealing:guts)`を補助が通常浄化と誤集計しfinishでFAIL。A総合はUNVERIFIED、B/T/C・seal/成功再読込はNOT RUN。failure/途中FIRINGを残し、補助変更/同case再実行なし。[生記録との照合](../build/verification/l2-client-20260915-220916/audit/stopped-run-analysis.json)。
- 22:45:42全dimension通常保存、22:46:11 Quit Game、PID17792終了。[保存NBT](../build/verification/l2-client-20260915-220916/audit/stopped-save-values.json)の両player保存にHP17/SP0・603/剣100/P/M ON/T OFF、同Zombie UUID・4trait保持。これは中断保存の確認であり、全OFF・恒久除去や再読込PASSではない。[製品/既存source/test不変](../build/verification/l2-client-20260915-220916/audit/final-integrity.json)、`.git`なし。
- 両極意購入停止/SP保護、RC=NO、既存開始gateと可逆クラフト既知許容仕様を維持。次は対象外Remove通知を実除去と混同しない補助修正＋新run承認待ち。


<a id="evidence-trial-invader-20260915"></a>

### 2026-09-15 21:11 JST — Trial1.4.9侵略者の限定実装・最終Jar自動統合完了

- 承認は[最新添付全文](../build/verification/trial-invader-20260915-203400/audit/user-request.txt)。20:04のL2計画のみフェーズをこの侵略者実装/隔離自動試験に限り更新。旧Status等は[開始前バックアップ](../build/verification/trial-invader-20260915-203400/before/docs/CODEX_STATUS.md)。実物識別・native呼出し・owner判定・途中失敗・未確認範囲は[共通計画§11](MASTERY_IMPLEMENTATION_PREPARATION.md#trial-invader-result)に集約。
- 実装：正常server本人・P/M取得ON、Invader tickの敵対Soul Protection false解除抑止、実ownerがInvaderの大小Beam新規Soul抑止、Hugeだけ極大hurt1箇所の例外。Small/Cube/他owner/別高damageは元処理。一般MobEffect敵対解除は特定できず架空実装なし。外部trueを付与せず、既存Soul/HP修復なし。
- 最終 **Invader86/86 / Cube49/49 / L2 95/95 / vanilla60/60 / TaCZ60/60 / build・unit・check PASS**。[全run集計](../build/verification/trial-invader-20260915-203400/audit/test-summary.json)。fixtureの引数compile修正と、native対象年齢不足による0件FAIL3 run→70PASS→閾値追加86PASSを保持。製品のバグ修正で期待を緩めたものではない。fixture・直接高damage負対照・通常保存を実clientや86 beam攻撃と混同しない。
- 配布Jar **229,423 bytes /150 entries / 3807DECCBA79A122CD3A6431454788944ACDC72FE62A7A9BDB37C189D1B76DE6**。[現物/3配置検査](../build/verification/trial-invader-20260915-203400/audit/final-jar.json)。旧D6F…既存class bytesは全一致、追加4class、manifest/Trial JSON/refmap差。Trial原物不変・外部/fixture非混入。新fixtureと限定build登録、正本5文書を更新。共通Root/damage/購入/SP/schema/既存testの134fileはhash不変、`.git`なしで[hash/通常差分照合](../build/verification/trial-invader-20260915-203400/audit/source-boundary.json)。
- 全新規試験server通常保存/停止。最終PID32156/36748/5616はexit0、全dimension保存、GameTestも通常終了。外部entity UUID重複等WARNは残す。既存PID29012（18:37開始）や旧world/Prismを操作しない。AUTOMATEDのみ、HUMAN/COMPUTER USEゲーム操作・新download・Security scanなし。
- 次は新Jarを対象に既存L2実client計画§10.9の実行承認待ち。計画・旧Trial実client結果を保持。両購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、全TaCZ/全gunpack未試験、個別開始条件、可逆クラフト既知許容仕様を維持。


<a id="evidence-l2-client-plan-20260915"></a>

### 2026-09-15 20:04 JST — 購入停止を維持したL2限定実client計画（文書のみ）

- [共通計画§10.9](MASTERY_IMPLEMENTATION_PREPARATION.md#l2-client-plan)へ手順/書込境界/合格条件を集約し、[TEST_PLAN §18](TEST_PLAN.md#l2-limited-client-plan)へ今回の判定範囲を記載。共通計画§4の「L2未起動」と現在の次候補を、19:35の実ロード95/95完了・今回の計画策定へ訂正。過去17:57の未起動や途中FAIL、Trial A/B/C・保存再読込の結果は変更しない。
- 現行node3件（通常浄化/浄化の極意/真実の極意）、credit/支出603、通常cap保存/packet同期、Trial補助のonce設計、L2のnative付与/attackと通常浄化END処理を必要範囲で読取。新runの真実OFFでA本人保護、MだけOFFでB実付与/追加wearの対照、製品によるstrip後に3つOFFでC実作用消失を比較する計画。既存95ケースの再実行を計画に含めない。
- 予定値はHP20→17→14→11、剣Damage100→100→182→182。Bの一瞬の付与はserver native処理前後に記録し、通常浄化による同tick後消去と区別。全OFF・範囲外・保存/同world再読込後にtraitを復活させず、対象外データを保持する。これらは**未実測の期待値**で、実clientはNOT RUN。
- [入力現物照合](../build/verification/l2-client-plan-20260915-195200/audit/local-inputs.json)：最終製品D6F5032C…/225,242 bytes/146 entriesと承認MOD5本は19:35hash一致。既知Prism/Java/Forge/metadataは存在。asset index5の旧記録とのhash差は現在metadataのSHA1/size一致を確認、差の発生原因は未確認。新download/認証情報の読取/旧world調査なし。
- [更新前3文書保全](../build/verification/l2-client-plan-20260915-195200/before/docs/CODEX_STATUS.md)、[文書・不変確認](../build/verification/l2-client-plan-20260915-195200/audit/final-review.json)。製品/補助/testコード・build.gradle・製品Jar・購入停止/製品Config無変更。build/test/ゲーム/Prism/server/scan起動なし。別の計画書や新しい実client補助は作成していない。.gitなし、git diff確認済みとはしない。
- 次は§10.9の実行承認待ち。両極意IMPLEMENTATION_PENDING/SP保護・RC=NO・REAL2CLIENT=BLOCKED・その他の個別開始条件と既存PASSを維持。可逆クラフト経由の増加は既知許容仕様・バグ修正対象外、意図しない死亡drop/replay/desync重複は許容しない。

<a id="evidence-l2-implementation-20260915"></a>

### 2026-09-15 19:35 JST — L2代表4 traitのP/T限定実装・最終配布Jar統合

- 承認：poison/slowness/corrosion/erosionのP/T限定Adapter、必要build/fixture、新規隔離自動検証/回帰/成果物/3文書更新。実client/Prism・旧world・download・他機能・購入解放なし。[開始前保全](../build/verification/l2-implementation-20260915-182900/before)、[不変/変更一覧](../build/verification/l2-implementation-20260915-182900/audit/final-review.json)。.gitなし、SHA/保全コピー比較でありgit diffではない。
- Pは実recipientへのeffect追加または選択後performだけを抑止。数値補正・装備選択/RNG、他player/反射先・既存効果/source mapを保持。Tは3つ取得/ON・±75、ID別UUID拘束marker、公開remove/元sync、反復中は除去せず実作用前に抑止。限定詳細とコード一覧は[共通計画§10.8](MASTERY_IMPLEMENTATION_PREPARATION.md#l2-implementation-result)。player schema/SP/purchase/Root/共通damageは不変。
- **最終結果**：build/foodHealingUnitTest/check、vanilla60/60、TaCZ60/60、配布Jar L2 **95/95**、Trial **49/49**。[集計・途中FAIL](../build/verification/l2-implementation-20260915-182900/audit/test-summary.json)。coreは既存59＋新規1、旧59/57/49/41を遡及変更しない。今回全てAUTOMATED、実clientはNOT RUN。
- **実ロード**：MC1.20.1/Forge47.4.0、Hostility2.5.19、Library2.5.3、Complements2.6.1、Curios5.12.0+1.20.1、Patchouli1.20.1-84-FORGE。Tracker0.4.4、Registrate MC1.20-1.3.11、l2serial1.2.2、l2modularblock1.1.0ほか内包の採用は[実receipt](../build/verification/l2-implementation-20260915-182900/final-l2-fixed/l2-result.json)。内包Jar外側重複配置なし。javacに必要な3 APIだけbuild/l2-compile-onlyへ抽出し配布/実modsへ入れない。
- **途中修正**：fixtureの不存在LUCK、login保護猶予、毒免疫Zombie/対象capなしCow、不可視chunkのquery準備を修正。さらに最初のTrial回帰0件FAILでL2 Mixin plugin→gameplay classの早期ロードを検出。純粋なL2HostilityVersionsへ分離し、元Entity/LivingEntity Mixin適用・Trial49件を回復。その後L2/vanilla/TaCZも最終Jar/ソースで再確認。失敗runを消去/再利用しない。
- **保存/同期**：native setTrait→pending/tick・reinit/copyFrom、作用直前抑止、通常player保存再読込、実chunkアンロード後の別entityインスタンス復元、範囲外/OFF後再付与阻止を確認。空traits Compoundの元packet/codec・server EmbeddedChannel送信と他field保全。実画面/TCP、別JVM再起動、全traitや大規模負荷は未確認。
- **Jar**：225,242 bytes /146 entries / SHA256 **D6F5032C3B40A78D2BCCEDB554EE1C302AA3C5367080F93E35184A5A96162D9A**。[現物・非混入・同配置hash](../build/verification/l2-implementation-20260915-182900/audit/final-jar.json)、[実変換hook](../build/verification/l2-implementation-20260915-182900/audit/loaded-hooks.json)。既存製品class/資源entryはbyte不変、Manifest更新＋限定追加。中間F4E78B44…JarのPASSは最終とは区別。
- 全12回の隔離serverは通常保存・終了、最終L2/Trial logにERROR/FATALなし。GameTest2種も通常shutdown/Gradle exit0。[process/log集計](../build/verification/l2-implementation-20260915-182900/audit/test-summary.json)。既存metadata/初期config補完/dev refmap等のWARNを全般解決したとはしない。
- 残り/次：両極意全体の未実装効果とreadiness、L2限定実client等。次候補はその準備計画の具体化を別途承認後に行う。両極意購入停止・SP保護、RC=NO、REAL2CLIENT=BLOCKED、ALL TACZ/ALL GUNPACK=NOT TESTED、Flight/Break Realm/Bulwark/試作型機関弩/FOURTH BOOTの個別gateを維持。**可逆クラフト増加は既知許容仕様・バグ修正対象外**、死亡drop/replay/desync重複は許容しない。


<a id="evidence-l2-dependencies-20260915"></a>

### 2026-09-15 17:57 JST — L2前提2MOD受領・依存内部追加照合（静的調査のみ）

- 承認実物：Library2.5.3 / Complements2.6.1の実パス、MOD ID、Manifest version、size/entries/SHA-256と再帰内包metadataを[現物記録](../build/verification/l2-dependencies-20260915-174200/audit/artifacts.json)へ保存。既存Curios/Patchouli/Forge47.4.0/Hostilityの[限定再照合](../build/verification/l2-dependencies-20260915-174200/audit/dependency-closure.json)を含め、今回の宣言上の必須ファイル不足は解消。ゲーム/loaderは起動していない。
- Damage Tracker：Hostility内包0.4.3の`[0.4.3,)`とComplements内包0.4.4の`[0.4.4,)`を照合。適合候補は0.4.4、実ロード選択は未観測。98 class中97はbyte同一、変更classはL2DamageTypes。必要なAttackCache/DamageModifier等のAPIは同一だが、magic分類tag等に差がある。[API/データ差分](../build/verification/l2-dependencies-20260915-174200/audit/required-api-comparison.json)。内包Jarの外側配置なし。
- GeneralCapability/TagCodec/registry map/同期・EffectUtil/MathHelper/Complements効果登録を追加読取。保存されたtrait mapのID→IntTag形式、空mapの既存map置換、正式remove後の元sync、保護判定を実effect callへ限定する根拠を[共通計画§10.7](MASTERY_IMPLEMENTATION_PREPARATION.md#mastery-l2-dependencies)へ集約。初回4種のファイル/仕様/静的API条件は充足し、実装開始承認待ち。L2起動/実装/自動統合/実clientは未実施。
- 変更前3文書を[保全](../build/verification/l2-dependencies-20260915-174200/before/docs/CODEX_STATUS.md)。Status現在要約/共通計画/TEST_PLANを更新し、古いファイル待ち・依存内読取待ちを現行欄から除いた。前回17:20の調査、Trial失敗runと新run A/B/C・seal/保存再読込、59/59・49/49は当時の履歴として保持、再実行なし。
- 製品/source/test/build.gradle/製品Jar/購入停止/SP保護不変。[前後・文書確認](../build/verification/l2-dependencies-20260915-174200/audit/final-review.json)。実装/補助コード作成、build/test、ゲーム/Prism/server/world操作、scan/downloadなし。RC=NO、他の個別開始条件と、食料生産の極意による可逆クラフト増加は既知かつ許容仕様（バグ修正対象外）を維持。


<a id="evidence-l2-read-20260915"></a>

### 2026-09-15 17:20 JST — L2 Hostility 2.5.19受領・静的調査（起動/実装/試験なし）

承認：新規L2調査・関連文書更新のみ。原物`<LOCAL_DOWNLOADS>/l2hostility-2.5.19.jar`、1,410,582 bytes / 1,300 entries、SHA256 **168665D887B34C5BD79311F0F302531CBED3F1954C43D59C23FF8564DC16C65B**。ID/version/Manifestと提供値一致。受領を実装/統合PASSにしない。

- [原物・内包・既存依存metadata/hash](../build/verification/l2-preparation-20260915-165000/audit/artifacts.json)、[探索範囲](../build/verification/l2-preparation-20260915-165000/audit/search-scope.json)。l2library `[2.5.0,)` / l2complements `[2.6.1,)`は既知限定配置先で未発見。Curios5.12.0+1.20.1/Patchouli1.20.1-84-FORGEは既存実物あり。内包3物を別配置せず、cataclysmはoptionalのまま。
- [共通計画§10](MASTERY_IMPLEMENTATION_PREPARATION.md#mastery-l2-2519)に代表4 IDの登録→実attacker/target→効果→map/保存注釈→公開remove/pending/再付与/元syncを記録。依存側codec/EffectUtil内部は不足として分離。Corrosion/Erosionの装備作用と数値倍率、removeのみではcleanup/恒久性がない点を確認。広域調査や未提供APIの推測はしない。
- 利用者回答③：真実は通常浄化・浄化の極意・真実の極意の**全取得/全ONで新規適用**。どれかOFFなら新規対象への適用停止、既存恒久無効化を復活させず、各toggleを連動変更しない。[Skill Tree §10](SKILL_TREE_SPEC.md#truth-mastery-activation)へLOCK。取得前提/500SPと購入停止を維持、実装承認とはしない。
- 共通計画§2の旧「AのみPASS/B・C未完了」「Trial実client保存未試験」を、§8.10と前フェーズ16:10 StatusのABC・seal/保存再読込完了へ訂正。§8.9の失敗・死亡保存、別JVM/TCP/force/自然boss等の未確認は保持。新しい試験実行なし。
- 変更前4文書を[保全](../build/verification/l2-preparation-20260915-165000/before/docs/CODEX_STATUS.md)。更新はStatus・共通計画・TEST_PLAN §18・Skill Tree §10の発動条件。詳細は[文書差分/不変確認](../build/verification/l2-preparation-20260915-165000/audit/final-review.json)。production/test/source/build.gradle/製品Jar/Config/購入gate無変更。外部Jar/原本world・過去world不操作。Minecraft/Prism/server/build/test/Security scan/downloadなし。



この節以下は日付付き記録と廃止した旧集計である。「現在」「最新」「次の作業」「完全停止」「同一PID維持」等も当時の文脈に限る。過去の起動・操作・停止指示を現在実行してはならない。現行状態・次の作業は[現在の要約](#現在の要約)だけを更新・参照する。


<a id="evidence-trial-client-safe-20260915"></a>

### 2026-09-15 Trial通常Cube限定実client — 新規安全run A/B/C・seal/保存再読込完了

- 更新 **2026-09-15 16:10 JST**。run `20260915-153300`、新規instance `FHR_Trial_Client_20260915-153300` / world `FHR_Trial_20260915_153300`。MC1.20.1 / Forge47.4.0 / Java17.0.15 / Trial1.4.9 / 製品8F4AB9…C3529。[実ロード](../build/verification/trial-client-20260915-153300/audit/runtime.jsonl)、[最終Jar/source/旧run保全照合](../build/verification/trial-client-20260915-153300/audit/artifact-source-final-review.json)。製品/source/test/build.gradle/購入停止は不変、製品build・59/49再実行なし。
- 安全準備: **world生成前**に通常GUIでnaturalRegeneration=false（[15:38:23](../build/verification/trial-client-20260915-153300/audit/screenshots/2026-09-15_15.38.23.png)）とdoMobSpawning=false（[15:38:54](../build/verification/trial-client-20260915-153300/audit/screenshots/2026-09-15_15.38.54.png)）。Survival/Normal、構造物なしflat。入場後この新world内だけに通常`fill … minecraft:glass hollow`で25×25・床/四壁/天井の閉区画1634blockを作成。HP/Soul/能力の変更なし。prepare前は欠損0・32block近傍敵対Mob0・HP20/MAX20/Soul0/fresh、待機中も区画/接近guard、再読込後も欠損0/敵対0。[環境採取](../build/verification/trial-client-20260915-153300/audit/environment.jsonl)。Cow ownerは8block先の同区画内でCubeのnative範囲を妨げない。
- 前run補助を新rootへコピーしrun識別・readonly環境guardだけ追加。独立client observerは同一source、旧allowlist/receipt/journalの流用なし。[補助差分](../build/verification/trial-client-20260915-153300/audit/helper-source.diff)。既存ローカルGradle8.1.1を`--offline --no-daemon -I …/trial-client.init.gradle trialClientVerificationJar`で使用、[task graph](../build/verification/trial-client-20260915-153300/audit/task-graph.txt)確認後[限定build成功](../build/verification/trial-client-20260915-153300/audit/build-helper.log)。`downloadMcpConfig`というtask名もoffline cache利用でありdownload実行の証拠ではない。製品task/既存suiteを含めない。
- 別補助 `foodhealing_trial_client_verification` **1.0.0-run20260915-153300**、21,135 bytes /11 entries /2 classes、SHA256 **538A8B7489A3E6E0D3979C5415073C63151A47B6CFFEF832115B0635398D99C0**。今回instanceのmodsだけに配置し旧49ケースfixtureは不在。製品Jarへの混入なし。補助compile/測定の失敗なし。初回optionsのsimulationDistance=4範囲外ERRORは[原log](../build/verification/trial-client-20260915-153300/audit/latest.log)に保全し、全warning解消とはしない。
- 明示prepare1回、通常浄化Lv1/極意Lv1、検証credit103/spend103、SP0/103。両readiness=false、他skill/HP/Soul/外部flag/pendingを修復しない。[receipt](../build/verification/trial-client-20260915-153300/audit/prepare-receipt.json)。通常GUI購入PASSではない。
- **A/B/C各1回すべてREAL CLIENT LIMITED PASS**。実測/期待のSoulは0→0、0→0.03、0.03→0.06、HPは20→17、17→14、14→11。MAX_HEALTH/getMaxHealth20、各LivingHurt/LivingDamage1回amount3、cube_attack・指定Cow owner・非cancel。実LocalPlayerのcanonical/Soul/HPがそれぞれ **0.0632733 /0.0640446 /0.0891157秒**で一致（許容5秒）。HUD17/14/11のF2と対応。[全数値/観測元](../build/verification/trial-client-20260915-153300/audit/measurement-review.json)。新AはB開始状態を通常攻撃で作る1回。前run A限定PASSを置換しない。
- Bでは通常GUI→製品packet/syncで親だけOFF、極意自身の保存ONを維持。Cは親ON/極意OFF。追加攻撃なしでGUI両ON復帰後もSoul0.06/HP11保持、15:56:16 seal。prepare1/activate3/SEALEDの[永続journal](../build/verification/trial-client-20260915-153300/audit/attack-journal.json)。foreign numeric0、failure/observer-error記録なし。
- 15:56:40通常保存完了→15:57:58に**同じ新worldだけ1回再読込**→prepare/hitなしでGUIとserver/clientの2skill/SP0/103/両ON/Soul0.06/HP11を確認→16:01:28再保存→16:01:52 Quit Game。PID13168終了。[保存前後](../build/verification/trial-client-20260915-153300/audit/first-save-values.json) / [最終保存](../build/verification/trial-client-20260915-153300/audit/final-save-values.json)ではlevel.dat Data.PlayerとUUID.datともSoul0.05999999865889549・Health11・DeathTime0・MAX属性20、canonical一致。receipt/journalは保存/再読込前後byte不変。[終了log抜粋](../build/verification/trial-client-20260915-153300/audit/lifecycle-excerpts.txt)、[process](../build/verification/trial-client-20260915-153300/audit/process-final.json)。HP/Soul補正・再付与・復活なし。
- **COMPUTER USE**: 生成前GUI、区画command、通常GUI toggle、明示command/F2/保存再読込/終了。**AUTOMATED**: 補助準備/実activate/独立readonly観測/保存証拠照合。**HUMAN**: 新しい認証・ゲーム操作依頼なし。自然boss/Cube描画/自然攻撃、force/閾値1/10/強制死、回復頭打ち、他攻撃/別版/別JVM/TCP/実2-clientへPASSを広げない。
- 前runのworld/receipt/journal/補助/証拠は保全、A限定PASSと環境事故/死亡保存も維持。旧runへの再入場/上書きなし。今回完了を両極意全体完成や購入解放にしない。RC=NO、REAL2-CLIENT=BLOCKED、全TaCZ/全gunpack=NOT TESTED、既存開始条件、可逆クラフト既知許容仕様を維持。[共通計画§8.10](MASTERY_IMPLEMENTATION_PREPARATION.md#trial-client-safe-result-20260915)。

<a id="evidence-trial-client-20260915"></a>

### 2026-09-15 Trial通常Cube限定実client — A成立、環境事故で中断

- 更新 **2026-09-15 14:52 JST**。14:10計画の実行承認に基づき、今回だけの FHR_Trial_Client_20260915-142100 / FHR_Trial_20260915_142100 を新規作成。MC1.20.1 / Forge47.4.0 / Java17.0.15 / Trial1.4.9 / 製品Jar8F4AB9…C3529。[runtime](../build/verification/trial-client-20260915-142100/audit/runtime.jsonl)、[artifact/source照合](../build/verification/trial-client-20260915-142100/audit/artifact-source-final-review.json)。
- 補助ID foodhealing_trial_client_verification、version 1.0.0-run20260915-142100、**19,968 bytes /11 entries /SHA256 2888444BA6B17714657B0FB31C802526C2AF6BEE608E06B1397FD278F6279190**。[補助検査](../build/verification/trial-client-20260915-142100/audit/helper-final-jar.json)、[最終専用build](../build/verification/trial-client-20260915-142100/audit/build-helper-3.log)。2classだけの別MODで製品Jarに混入なし。製品source/既存test/build.gradle/Jar/購入gateは変更していない。
- 準備エラーを保存: wrapperが既存Gradleを見つけずdownload試行→通信拒否、取得完了なし。既存ローカルGradleをoffline指定。sandbox native library失敗は許可されたcache利用で解消。dry-runでMixinGradleが製品compileを追加するのをguardが拒否→補助init内だけで除去。補助Jarのclass取り込み漏れを起動前に修正。初回起動は補助javafml要求47.4.0と実language47の不一致でFAIL→world作成前に通常ウィンドウ終了。補助javafmlを47へ修正しForge47.4.0固定は維持。[初回起動log](../build/verification/trial-client-20260915-142100/audit/client-startup-failed.log)。製品や測定期待値は変更していない。
- 14:34:18にlive inspect本人UUIDをbindし、fresh guard後に**prepare1回**。通常浄化/極意Lv1・双方ON、人工credit103/spend103、SP0/103、HP20/最大20/Soul0。serverと実LocalPlayerで両極意readiness=false。通常GUI購入成功ではない。[receipt](../build/verification/trial-client-20260915-142100/audit/prepare-receipt.json)、[binding](../build/verification/trial-client-20260915-142100/audit/uuid-binding.json)。
- **A LIMITED PASS**（14:38:25）: 通常Cube実activate1回、Soul0→0、HP20→17、LivingHurt/LivingDamage各1回、cube_attack/Cow owner/amount3/非cancel。実LocalPlayerがserver afterから**0.084053秒**後に一致。HUD17.0/20.0。[測定照合](../build/verification/trial-client-20260915-142100/audit/measurement-review.json)、[A画面](../build/verification/trial-client-20260915-142100/audit/screenshots/2026-09-15_14.38.34.png)。
- Bは**親OFF/極意ONの通常GUI→server/client同期だけ確認**。SP0/103不変、攻撃前HP17/Soul0。その後14:39:37から自然spawn済みslimeのminecraft:mob_attack amount2が9回発生し、14:39:55死亡。Bは入力途中で送信/発動せず、Cも未実施。[failure](../build/verification/trial-client-20260915-142100/audit/failure.json)、[イベント](../build/verification/trial-client-20260915-142100/audit/damage-events.jsonl)、[死亡画面](../build/verification/trial-client-20260915-142100/audit/screenshots/2026-09-15_14.40.11.png)。初回入場でslimeを把握しながら、mob生成OFFだけで既存mobの隔離を完了しなかったagentの安全準備不足。事故は製品Soul強制死や承認された死亡試験ではない。
- **B/C UNVERIFIED、seal/成功状態の通常再読込 NOT RUN**。[journal](../build/verification/trial-client-20260915-142100/audit/attack-journal.json)はcases[A]/activateCount1、failureで以後のprepare/hit拒否。receipt/journal削除、再付与、HP/Soul修復、respawn、world再読込なし。補助は外部damageを取消してplayerを保護する機能を持たない。
- 死亡画面から通常タイトル復帰を確定。14:40:42全dimension保存→14:41:23 Quit GameのStopping!、**PID34424終了確認済み**。[終了証拠](../build/verification/trial-client-20260915-142100/audit/process-exit.json)、[最終log](../build/verification/trial-client-20260915-142100/audit/client-final.log)。これは失敗時の通常保存/終了で、計画のseal後再読込成功とは区別。
- 正常停止後のlevel.dat Data.Player/UUID.datを読み取り保全。双方で取得2node、SP0/103、親OFF/極意ON、Soul0、Health0。[保存値](../build/verification/trial-client-20260915-142100/audit/saved-values.json)。事故後の死亡状態保存であり、未決定lifecycleの仕様決定や再読込PASSにはしない。
- **COMPUTER USE**: Prism、新world、GUI、command、F2、保存退出、Quit。**AUTOMATED**: 明示prepare/activate、独立server/client readonly observer、事後NBT/hash/ログ照合。**HUMAN**: 今回ゲーム操作/再認証依頼なし。既存world・認証ファイルのコピーなし。
- 残りは安全準備を修正した別runのB/C・seal/通常再読込。59/59・Trial49/49とA限定PASS、両極意購入停止、RC=NO、全既存gate、可逆クラフト増加の許容仕様を維持。

<a id="evidence-trial-client-plan-20260915"></a>

### 2026-09-15 Trial通常Cube実clientの取得準備・最小手順の文書化（未実行）

- 記録日時 **2026-09-15 14:10 JST**、基準Status2026-09-15 13:29。正本 `<LOCAL_PATH>/food-healing-mod-main/docs/CODEX_STATUS.md`。[変更前Status](../build/verification/trial-client-plan-20260915-135500/before/CODEX_STATUS.md) / [変更前共通計画](../build/verification/trial-client-plan-20260915-135500/before/MASTERY_IMPLEMENTATION_PREPARATION.md) / [before manifest](../build/verification/trial-client-plan-20260915-135500/audit/before-manifest.json)。直前までの履歴はbytes保持、全体調査/Status大規模再整理はしない。
- **今回の実施**: AGENTS、現行要約、共通計画§8、TestPlan19、購入/取得/通常GUI-toggle/登録packet-sync/provider保存、前回fixtureの該当部分をREAD ONLY照合。補助作成・build・59/49・ゲーム/Prism/server起動・world操作・Security scan・外部downloadは実施せず。今回の文書化は実client PASSではない。
- **方法**: 製品と別の検証MODで、new run/world/対象UUIDをbindしてprepare1回。正常fresh dataだけを許可し、人工検証credit103→trySpend103→通常浄化/極意Lv1×2・ON、SP0/103。真実/他skillなし。既存setter/trySpend/serialize/sync APIを使い、購入readiness・HP/MAX_HEALTH/Soul/外部flag・pending/schemaを変更しない。PREPARING/PREPAREDの永続receiptで再付与/retryを防ぎ、測定には付与経路を持たせない。通常GUI購入実績ではない。
- **最小3比較**: 既定Soul0.03、MAX_HEALTH20、HP20、Root/軽減/外部免疫なし、自然回復OFFの新規Survival。通常GUIでA両ON/B親だけOFF/C極意だけOFF、実登録Cube.activate各1回・総3。期待Soulは0→0、0→0.03、0.03→0.06、通常数値3でHP20→17→14→11。native選択を通し、Cubeをworldへ登録せず自然tick再発動なし。自然boss/Cube出現の証拠にはしない。再ONは攻撃せずSoul0.06保持を確認しseal。
- **観測の区別**: server/playerと実LocalPlayerでcanonical/Soul/実効HP/最大HPを独立readonly採取、LivingHurt/Damageの回数/source/amountとF2/HUDを対応付ける。NBT HealthはgetHealthを保存するため生内部値と呼ばない。HUD分母20、Soulからの制限値20/19.4/18.8は計算値で、上限への回復頭打ちの実client実測ではない。[source抜粋](../build/verification/trial-client-plan-20260915-135500/audit/health-source-excerpts.txt)。許容差/汚染時停止は[計画§8.4–8.5](MASTERY_IMPLEMENTATION_PREPARATION.md#trial-client-preparation)。
- **通常保存/再読込**: 同新規worldだけSave & Quit→level.dat Data.Playerと存在するUUID.datをreadonly照合→1回再読込（prepare/hitなし）→再Save & Quit→Minecraft終了/log/save/process確認を提案。HP/Soulの補正やsnapshot上書きで一致させない。再入場でprepare1/activate3/封印を維持する計画。
- **環境/不足**: [限定照合](../build/verification/trial-client-plan-20260915-135500/audit/local-inputs.json)で製品Jar205688bytes/131entries/SHA256 `8F4AB9F60C2B0D64CF2902944248261A806759BF57CA308EA4273FBC950C3529`、承認Trial1.4.9原物hash一致、既存Prism/Java17.0.15/Forge47.4.0主要Jar/asset index5、version.json列挙29library存在を確認。認証ファイルの読取/コピーなし。全asset総走査/起動確認はせず、不足が出た場合は正確な項目を報告。次回必要なのは未作成helper/allowlist/new instance/worldと実行承認。47.2.0起動へ戻さない。
- **予定補助と主体**: 新規verification root配下のTrialClientVerification.java、client readonly observer、mods.toml/pack.mcmeta、専用Gradle init script、guard/receipt/ログ。今回は未作成。製品build.gradle/Jarを再生成せず専用fixture taskだけを使う。GUI/command/F2は次回Computer Use、採取/照合はAUTOMATED。人間は認証期限切れ等で必要なら本人ログインのみ、操作不可時は通常GUI操作だけをまとめて依頼。食事/死亡/厳密tick/OBSは不要。
- **保存した文書**: 共通計画§8.1–8.8に詳細を集約、TEST_PLAN19は参照を追加、Status現在欄を「方法具体化済み・実行承認待ち」へ更新。[最終readback/変更一覧](../build/verification/trial-client-plan-20260915-135500/audit/final-review.json)。今回は3文書と読取記録のみ。既存限定実装、59/59・49/49、通常server終了PASSを維持。
- **gate**: Q1/Q2と取得条件はLOCK済みのまま。両極意IMPLEMENTATION_PENDING/SP保護、RC=NO、REAL2CLIENT BLOCKED等と個別開始条件を維持。L2/真実/他Trial/force/閾値死へ作業を拡張しない。可逆クラフト増加は既知の許容仕様・バグ修正対象外、意図しない死亡drop/replay/desync重複は許容しない。

<a id="evidence-trial-monolith-20260915"></a>

### 2026-09-15 浄化の極意 × Trial Monolith1.4.9 Damage Cube限定実装・実配布Jar自動統合

- 記録日時 **2026-09-15 13:29 JST**。開始Status2026-09-13 21:06、正本 `<LOCAL_PATH>/food-healing-mod-main/docs/CODEX_STATUS.md`。[変更前Status](../build/verification/trial-monolith-20260915-125400/before/docs/CODEX_STATUS.md)、[変更前計画](../build/verification/trial-monolith-20260915-125400/before/docs/MASTERY_IMPLEMENTATION_PREPARATION.md)、[before manifest](../build/verification/trial-monolith-20260915-125400/audit/before-manifest.json)。`.git`なし、実ファイル比較を使用。下の9/13以前の履歴bytesは保持。
- **利用者LOCK**: Q1新規Cube Soul蓄積/実効HP制限/派生強制作用だけを防ぎ、別numeric hurtは通す。Q2通常浄化/極意の両取得・両ONのみ。旧親OFF有効提案は不採用。[正本Q1](COMPATIBILITY_POLICY.md#trial-monolith-149) / [正本Q2](SKILL_TREE_SPEC.md#purification-mastery-activation)。双方の購入停止/SP保護、取得100/500SPを維持。
- **受領**: TheTrialMonolith-1.20.1-Forge-1.4.9.jar、IDthe_trial_monolith、loader/Implementation1.4.9、263159bytes/240entries、SHA256 `5EFE4C068F24E611C215A0A20FE9698D5C5F7EFC1476C8E5CEAEA78B8CE1A0AD`。[現物metadata/CRC](../build/verification/trial-monolith-20260915-125400/audit/input-artifact.json)。元Jar未提供の停止理由を解除、原物/参照/配置同hash。Curios optional、外部必須MOD/同梱Jarなし。
- **1.2.8との差・実装**: 1.4.9は高次元かつ外部Soul免疫ならforce(config/10)、それ以外normal(config)。PurificationMasteryController＋optional version plugin＋DamageCube内Redirect2箇所で本人canonicalを判定。数値hurt/motion、helper全体、command/load、他player/mob、Root、共通damage/packetは変更なし。modifyHealth/transformer/Soul値閾値1/10、loadのsetSoulDamageForceを実物再読取。[詳細](MASTERY_IMPLEMENTATION_PREPARATION.md#mastery-trial-route)。
- **依存/初回FAIL**: Forge47.2.0は外部本体registerConfig NoSuchMethodError、world開始前停止。[初回log](../build/verification/trial-monolith-20260915-125400/audit/actual-server-01.log)。手元47.4.0 installer/必要librariesと既存MC SRGからoffline専用server作成、installerのpatched SHA1一致。[準備証拠](../build/verification/trial-monolith-20260915-125400/audit/forge4740-runtime.json)。最小Forge版は未特定。新download、旧Prism/旧world起動、原物/依存要求書換えなし。
- **途中fixture修正**: 02ではCubeのgetPosition(0)と初期位置のずれで対象未選択・motion assertion FAIL。moveTo＋native対象存在assertへ修正。03は非保護Soul死のdrop通知[7,0]を1callbackと仮定してFAIL。実物helper対照も同じ[7,0]で、nonempty1回/計7・native death/respawn一致を検査。空通知を削除/隠蔽しない。正当numeric deathはdeath/drop各1、保護側強制death/drop0の期待を維持。04の48/48後、既定.03＋既存50%軽減を追加した最終49/49で確認。途中ログ/receipt全保存。製品hookの条件やrequire数は緩和していない。
- **最終build/回帰**: [build foodHealingUnitTest check trialMonolithTestJar](../build/verification/trial-monolith-20260915-125400/audit/build-final.log)成功。eligibility150、既存前提142/SW47/境界5000/Ammo単体等。新規core2件は汎用数値/死亡保護へ漏れない合成event、parent toggle保存sync。[vanilla59/59](../build/verification/trial-monolith-20260915-125400/audit/vanilla-final.log) / [承認TaCZ59/59](../build/verification/trial-monolith-20260915-125400/audit/tacz-final.log)、既存57件を削除せず維持。旧57/55/49/41は当時の結果。共通damage/packet変更なしのためTaCZ別13/Ammo実MOD・SW実攻撃/別JVMは今回再実行なし。
- **最終配布Jarによる実統合49/49**: [receipt](../build/verification/trial-monolith-20260915-125400/actual-server-final/trial-result.json)。本物DamageCube.activate/native対象選択47ケース、直接helper対象外1、同一JVM通常player保存再読込1。4強制閾値ケースにhelper対照があるが独立実攻撃数へ合算しない。通常/force各4状態、未取得/不正/pending/移行、Soul0/.25/.95・1/10跨ぎ、二攻撃、他player/mob/owner、registered packet/commandとsync、既存Soul/NBT/modifier保持を確認。Force条件の高次元/外部SoulProtectionとconfig変化はtest-only準備し最後にconfig復元。
- **数値分離**: 原数値5・sourcecube_attack・owner・1回（独立2攻撃は2回）を観測。通常保護でHP100→95、既存50%軽減で100→97.5。RootなしHP2で正当numeric death1/drop7個1回。Soul1/10未満からの新規蓄積を保護し、既存Soulを消さない。外部免疫下はHPに独自作用があるため同一外部flag比較＋event観測を使い、HP非減少だけでnumeric遮断としない。非保護Soul閾値は元death/強制respawnを維持。death後のhurtはnative側でeventに至らないケースをそのまま記録。
- **変換後call-site**: 非改変javaagentがJVM class定義時に採取したclassをjavap。[実ロード検査](../build/verification/trial-monolith-20260915-125400/audit/loaded-callsite-validation.json): normal1/force1、hurt1、後続motion1、各redirect内元helper1。TTMのpriority2147483647/独自BEFORE・AFTER transformerが共存した実測。agentはfixture同様製品へ混入なし。
- **新Jar**: `205,688 bytes / 131 entries / SHA256 8F4AB9F60C2B0D64CF2902944248261A806759BF57CA308EA4273FBC950C3529`。[実物metadata/Mixin/refmap/reobf/非混入/配置一致](../build/verification/trial-monolith-20260915-125400/audit/final-jar.json)。最終Jarの製品classは59件回帰時とbyte一致。直前DC3663C6…Jarは保全。今回実clientはNOT RUN、旧Jarの人力/実clientを新JarPASSへ転記しない。
- **主体/保存/終了**: 全てAUTOMATED、actual dedicated＋本物ServerPlayer/EmbeddedChannel。HUMAN/COMPUTER USE、実client/TCP/Prismなし。47.4.0の02/03/04/finalは通常保存→halt→全dimension保存・exit0、最後PID36108終了。[process確認](../build/verification/trial-monolith-20260915-125400/audit/process-final.json)。01はworld開始前終了で保存対象なし。過去world/原本world/製品Config/schemaを変更しない。初回javap/log表示のパス/encodingエラーは原物同hashコピーと読取処理で対処、元ログを保持。
- **変更一覧/最終照合**: [変更ファイルと原本/リンク/履歴の再読込検査](../build/verification/trial-monolith-20260915-125400/audit/final-review.json) / [実ファイル差分](../build/verification/trial-monolith-20260915-125400/audit/changes.diff)。
- **残WARN/未確認**: dev59件実行にはrefmap読取WARNがあるが配布Jarのrefmapは現物確認済み。最終logにもsandboxのYggdrasil公開鍵ERROR・Forge更新確認接続失敗、WMI/ライブラリmetadata/外部SynchedEntityData/config既定補完WARN等。認証/TCP/全WARN解消はPASSにしない。今回Cube実client/別版/他攻撃・最低Forge版、別JVMの今回Trial保存、両極意全効果/readinessは未完了。L2本体/採用版/依存はartifact待ち。[共通計画](MASTERY_IMPLEMENTATION_PREPARATION.md)。
- **次の候補/gate**: 両購入停止を保った検証データ準備方法を具体化し、別途承認後の新規隔離Trial実client表示/同期確認。L2受領後のtrait調査は独立候補。RC=NO、REAL2CLIENT=BLOCKED、ALL TACZ/ALL GUNPACK=NOT TESTED、Flight/Break Realm/Bulwark/試作型機関弩/FOURTH BOOT等維持。可逆クラフト増加は既知許容仕様、死亡drop等の意図しない増殖は不可。Security scanなし。

<a id="evidence-mastery-preparation-20260913"></a>

### 2026-09-13 両極意の効果実装準備 / 仕様・現行コード・対象artifact・第一単位の整理

- 記録日時: **2026-09-13 21:06 JST**。workspace `<LOCAL_PATH>/food-healing-mod-main`、開始Status **2026-09-13 20:13 JST**と一致。変更前は[完全保全](../build/verification/mastery-preparation-20260913-205500/before/CODEX_STATUS.md)、675,385 bytes / SHA-256 `146E8F2F1BB8F45BEACBFFD3FD12BBC2F3DD384EFFE6C364C81147D98E172F31`。この記録より後の既存履歴はbytesを保持し、過去のFAIL/NOT RUN/実client記録を消していない。
- [共通計画](MASTERY_IMPLEMENTATION_PREPARATION.md)を1ファイル作成。各極意の6列対照表、A確定/B現行/C過去/D提案/E未確定、限定artifact一覧、実在する介入点、第一単位と試験計画を集約。SPEC/Skill Tree/Compatibility Policy/Test Plan/Audit本文を上書きせず、OPEN-02/03のLOCKとIMPLEMENTATION_PENDING/SP保護を維持。
- [限定配置metadata照合](../build/verification/mastery-preparation-20260913-205500/audit/artifact-inventory.json): libs1、run/mods0、既知参照mods104×2、Downloads直下5 Jar。L2 Hostility/Trial/Hyperlink/Fumetsu本体候補は確認範囲で未発見。PC全体/全cache/原本world走査なし。project直下`tmp_monolith`の参考展開物を確認：**The Trial Monolith / the_trial_monolith / 1.2.8**、必須記載MC `[1.20.1,1.21)`・Forge/loader `[47,)`、entrypointのtconstructはoptional判定あり。[metadata/classの実測hash](../build/verification/mastery-preparation-20260913-205500/audit/monolith-reference-manifest.json)。元Jar hash/来歴・正式対象版は未確認。参考展開物を正式Jarとして起動/再包装しない。
- `javap`で実classを再読取。DamageCubeのSoul加算→setSoulDamage→閾値1でonSoulDeath/10でonSoulRemoveと別numeric hurtを確認。onSoulDeathは直接HP・death/drop、ServerPlayerのonSoulRemoveは強制respawn。TTMのLOWEST/receiveCanceled death取消解除、Soul値のhealth/dead計算、NBT/SynchedEntityData保存を確認。強制死helperだけのskipやSoulProtection flag借用では仕様を満たせない。**Q1 Soul加算/HP上限低下の防御範囲、Q2 効果時の自身/親toggle**を元Jar不足と分けた。L2のtrait内部は未提供のため架空のAPI/class/marker名を確定していない。
- 推薦は浄化×Damage Cube一経路の限定Adapter/自動検証。元Jar＋2判断＋開始指示待ちで、購入解放や正式対応全体の縮小ではない。真実の恒久trait除去はL2 artifact受領後の独立調査。今回実装/試験は**NOT RUN**で、新しい動作PASSを記録しない。
- 主体: AUTOMATEDのローカル読取/bytecode/hash/文書整合のみ。HUMAN/COMPUTER USEによるゲーム操作なし。production/test code、購入gate、配布Jar、製品Config/依存設定、ゲーム/worldは変更せず、build/test、client/Prism/server、外部download、Security scanなし。[変更前manifest](../build/verification/mastery-preparation-20260913-205500/audit/before-manifest.json) / [最終照合](../build/verification/mastery-preparation-20260913-205500/audit/final-review.json) / [文書差分](../build/verification/mastery-preparation-20260913-205500/audit/changes.diff)。`.git`なし、git diff実行の主張なし。
- 既存57/57・SW通常GLOCK限定PASS・True Root確認・通常終了を維持。RELEASE CANDIDATE=NO、REAL2CLIENT=BLOCKED、ALL TACZ/ALL GUNPACK=NOT TESTED、Flight/Break Realm/Bulwark/試作型機関弩/FOURTH BOOTの個別gateと可逆クラフト許容仕様を維持。

<a id="evidence-superbwarfare-client-20260913"></a>

### 2026-09-13 最終配布Jar直接ロード / SuperbWarfare GLOCK-17通常射撃の実client限定確認

- 記録日時: **2026-09-13 20:13 JST**。開始checkpointは2026-09-13 19:25 JST、正本 `<LOCAL_PATH>/food-healing-mod-main/docs/CODEX_STATUS.md`。新規証拠root `build/verification/superbwarfare-client-20260913-194701`。[変更前Status](../build/verification/superbwarfare-client-20260913-194701/before/CODEX_STATUS.md)を保全。現在の要約を直接更新し、この節より後の旧履歴本文はbytesを保持した。`.git`なし、git diff実行の主張なし。
- **実行範囲・主体**: COMPUTER USEで既存認証済み検証Prismから専用新規instanceを起動、新規flat/survival/cheats ON world作成、準備用通常command、銃/弾取得、R reload、腰だめcrosshairで胴体へ左クリック各1回、data get、GUI/HUD/F2、Save & Quit→title→Quit Game。AUTOMATEDはローカルJar/bytecode・log・通常終了後の保存・hash/process照合のみ。今回HUMAN射撃/食事なし（既存ログインは前フェーズの利用者操作）。dev runClient、performDamage/API、/damage、弾Entity生成、packet手動送信、攻撃fixture、再build/GameTest/別JVM/Security scanなし。

**実物・実ロード環境**

- [配布Jar現物照合](../build/verification/superbwarfare-client-20260913-194701/audit/final-review.json): `build/libs/foodhealing-3.0.0.jar`、**200,558 bytes / 125 entries / SHA-256 DC3663C68A3DC7D76BCC1D1F844CFCD68CF809113FB1AA892BCAE9049D8D10BC**。配置Jarも同hash、mods.toml/Mixin/refmap・CRCを確認、攻撃fixture/GameTest/ExampleMod/外部Jar非混入。再生成・差替えなし。
- 承認SuperbWarfare原物 `<LOCAL_DOWNLOADS>/superbwarfare-0.8.9.1-hotfix-mc1.20.1-993063bed-all.jar` は **42,162,514 bytes / SHA-256 3AAF4C239BC0FB31F9217927A44D74071D904D1DD03C5308CA3395ECD67D86DC**。loader **0.8.9.1**、Implementation-Version **0.8.9.1-mc1.20.1-993063bed**、hotfixはfilename。原物/配置コピーとも前後hash一致。
- instance: `<LOCAL_PATH>/food-healing-mod-main/build/verification/direct-jar-20260913-114729/launcher/instances/FHR_SW_Client_20260913_194701`、game directoryはその`minecraft`、worldは`saves/FHR_SW_Client_20260913_194701`。以前のCore/TaCZ worldを再開せず、新規1world・integrated server起動1回。Prism10.0.5、**MC1.20.1 / Forge47.4.0 / Microsoft Java17.0.15 / LWJGL3.3.1 / 1280×800 / GUI2 / 日本語**。前回自動serverのForge47.2.0/Java17.0.7との差を記録。既存承認ローカルruntime/assets/librariesを利用し、新downloadなし。アカウント/token/passwordファイルの読取・コピー・手動編集なし。
- mods直下はFood Healing、承認SuperbWarfare、**Kotlin for Forge4.11.0、Patchouli1.20.1-84-FORGE**の4ファイル。[配置/hash](../build/verification/superbwarfare-client-20260913-194701/audit/preparation.json)、[実load記録](../build/verification/superbwarfare-client-20260913-194701/audit/load-evidence.log)。同梱Jarは通常解決され、**Curios5.14.1+1.20.1 / GeckoLib4.4.6 / SimpleBedrockModel2.5.1-forge-mc1.20.1 / Ponder1.0.91 / Flywheel1.0.5 / Cloth Config11.1.106 / MixinExtras0.3.6**を実load（MC/Forge等を含む13 MOD）。Rhino/mclib/mae/kfflang/kfflibは同梱libraryとして区別。Food Healingのロード元は新instanceのmodsでcontainer1個、旧Jar/dev classes/攻撃fixtureとの二重ロードなし。TaCZなし。

**比較設計と観測**

- 銃ID **`superbwarfare:glock_17`**、弾薬 **`superbwarfare:handgun_ammo`**。実物JSON・登録・キーbytecodeで確認。通常`/give`で無改造銃と弾64を取得し、既定SEMI、通常Rキー1回で空0→17発・予備64→47。装填数/薬室/ダメージNBTを直接作っていない。Perks/Attachments空、PropertyOverrideなし、銃Level既定0。発砲でExpは0→約2.75→約8.25、CloseStrike記録は自然に変化したがLevel/強化/Perks/Attachmentsは変化なし。
- [射撃前に固定した期待値](../build/verification/superbwarfare-client-20260913-194701/audit/shot-plan.json): 基礎Damage5.5、Headshot1.5、armor bypass0.15、Handgun減衰開始40blocks、projectile既定`superbwarfare:projectile`・1発。防御0の牛なので15%/85%のsplit合計は基礎Damageに一致し、未強化global/Heroics係数1、食義専用係数1/2により **A5.5 / B11.0、絶対許容差0.01HP**。APIでdamageを発生させた結果ではない。公開メソッドを直接呼んだ前回17件とは別証拠。
- 新規worldだけピースフルで外乱を抑えたsurvival。別牛A/Bを通常summonでNoAI1・PersistenceRequired1・初期/最大HP100・knockback resistance1・armor/absorption/effectsなし、同じ向き90度に準備。A `(0.5,-60,4.5)`、B `(8.5,-60,4.5)`、Playerは各z0.5/同x/y、水平4blocks。通常tpは位置/照準準備（yaw0/pitch11.6）、画面上の側面胴体crosshairから通常左クリック1回。ADS長押しは行わず、腰だめ照準の限定確認。射撃開始～Health読取完了にHP/被弾量を書き換えていない。
- 食義はfresh Lv0、Bだけ既存`/foodhealing syokugi setlevel 1`で準備。両方SP0/spent0/技能未取得/BaseStats空/pending0、他攻撃技能・装備なしをGUIとcanonical read-only commandで照合。HP HUD20/20、Lv0→1のHUD、通常inventory→SのGUI、ステータス購入0を表示確認。

| 条件 | 食義/専用係数 | 標的Health 前→後 | 実HP差 / 期待値 | 弾数 前→後 / 予備 | 判定 |
|---|---|---|---|---|---|
| A / 牛sw_a | Lv0 / 1倍 | 100.0→94.5 | 5.5 / 5.5 | 17→16 / 47 | **PASS（この1命中）** |
| B / 牛sw_b | Lv1 / 2倍 | 100.0→89.0 | 11.0 / 11.0 | 16→15 / 47 | **PASS（この1命中）** |

- 識別: A UUID `[901366012,-1363983229,-1358565997,342289516]`、B `[1194419246,589516394,-1536013067,882564253]`。同一銃UUID `[993069764,-1218952549,-1871115136,1030831554]`。通常クリック直後の赤い被弾表示・hit marker・1発減少をCOMPUTER USE screenshotで観測、正確なHealthを通常server `data get`で取得。A Health読取20:00:44.899、B20:03:52.489（証拠は[chat log](../build/verification/superbwarfare-client-20260913-194701/audit/chat-evidence.log)）。最終HP差比2.0。銃/標的UUID・HPの対応は[終了後保存](../build/verification/superbwarfare-client-20260913-194701/audit/save-evidence.json)でも照合済み。無効射撃0、追加射撃/再試行なし。UI準備時の入力検出・screenshotId再取得は再観測で回復し、射撃結果の無効試行ではない。
- F2は8枚。19:58:23 Lv0 GUI、19:58:39未強化stat、20:00:10 A前、20:00:58 A後、20:03:24 B前、20:04:00 B後、20:04:45 Lv1 GUI、20:05:28 title。[実ファイル一覧/hash](../build/verification/superbwarfare-client-20260913-194701/audit/final-review.json)。一部chat数値はHUDと重なるため、正確なHP値はchat logと保存値を用いた。全飛翔frame/packet内部/発射音を採取したとはしない。

**終了・変更範囲・残る条件**

- **通常終了**: Save & Quit後20:05:12.966 `Stopping server / Saving players / Saving worlds`、20:05:13.604 `All dimensions are saved`、titleを観測してQuit Game、20:05:38.671 `Stopping!`。[ゲーム証拠log](../build/verification/superbwarfare-client-20260913-194701/audit/game-evidence.log)。[20:06:18のCIM](../build/verification/superbwarfare-client-20260913-194701/audit/process-final.json)で今回instanceのJava process **0**、Minecraft windowなし。CIMはsandbox内アクセス拒否後、承認済み範囲の読み取りとして実行し成功。強制終了/crashなし。検証Prism自体は既存のまま残した。
- 保存処理中にworldファイルを開かず、終了後にlevel.dat/単一player.datをauditへコピーして読取。canonical/Inventoryが双方一致、食義1/SP0/未取得・銃15/予備47。標的2体が存在する既知のentity chunk(0,0)だけ読み、A94.5/B89.0・位置/NoAI一致。既存world/cacheの総走査なし。
- client latest.logはERROR1件: **`superbwarfare:sounds/vehicle/ac_130h/Minigun_fire.wav` invalid path, ignoring**。WARN260行の大半は新instanceの既定config生成/補完。library mods.toml・既存Food Healing試験食model・asset/sound/shader/IPv6 WARNも保持。今回GLOCK-17のHP比較を阻害する製品不具合は確認されず、無関係なasset修正をしていない。前回sandbox認証key ERRORは今回再現せず、認証通信全般/dedicated/二者PASSへ拡張しない。
- **製品code/test code/Jar/依存/schema/migration/product Config変更なし、build/test再実行なし**。126対象ファイルの開始hash比較はStatus以外不変、既存Core/TaCZ instance.cfg/mmc-pack4ファイルも不変。変更は正本Status、今回のaudit/backup、および新instanceにゲームが通常生成したConfig/save/log/F2に限定。前回57/57・17実攻撃＋3 synthetic、以前55/49/41件・FAIL/NOT RUNは当時の記録を維持。
- **RELEASE CANDIDATE = NO / REAL 2-CLIENT = BLOCKED / ALL TACZ・ALL GUNPACK COMPATIBILITY = NOT TESTED**。今回のfresh GLOCK-17/Lv0/Lv1以外の全SW武器/全版/搭乗兵器/爆発/別弾/実移行済みworld/専用倍率Lv200実client/全packet/全飛翔frame/dedicated遅延は未確認。両極意効果未完成/購入停止、True Root部分蓄積・親RootOFF・死亡/logout/restart境界、Flight ownership/購入停止、Break Realm expansion NOT IMPLEMENTED、Bulwark、試作型機関弩完全一致gate、FOURTH BOOT NOT AUTHORIZEDを維持。食料生産の極意の可逆クラフト増加は既知かつ許容仕様・バグ修正対象外。死亡drop/replay/desync等の意図しない重複は許容しない。
- 次の候補は残release範囲・優先順位・実行対象の選定。今回の通常銃最小確認は完了し、別の未承認作業へ進めていない。

<a id="evidence-superbwarfare-20260913"></a>

### 2026-09-13 SuperbWarfare実物照合・v3通常弾接続・隔離自動検証完了

- 記録日時: **2026-09-13 19:25 JST**。正本: `<LOCAL_PATH>/food-healing-mod-main/docs/CODEX_STATUS.md`。開始前Statusは2026-09-13 18:21 JST。[変更前の保全](../build/verification/superbwarfare-20260913-190540/before/docs/CODEX_STATUS.md)と[全対象manifest](../build/verification/superbwarfare-20260913-190540/audit/before-manifest.json)、[変更差分](../build/verification/superbwarfare-20260913-190540/audit/changes.diff)あり。`.git`なし。git diffを実行済みとは記録しない。
- 承認範囲: 実物・旧bytecode照合、optional最小接続、fresh disposable server/GameTest、自動検証、Jar再生成・測定、Status更新。実client/Prism/人力操作なし。原本world・過去world・通常MOD構成・product Config・schema/migration変更・外部download・Security scanなし。旧server配下はForge librariesを読取参照しただけで、旧worldは起動していない。

**入力・依存・静的照合**

- 実物: `<LOCAL_DOWNLOADS>/superbwarfare-0.8.9.1-hotfix-mc1.20.1-993063bed-all.jar`。**42,162,514 bytes / 7,292 outer entries**、SHA-256 **3AAF4C239BC0FB31F9217927A44D74071D904D1DD03C5308CA3395ECD67D86DC**。MOD ID superbwarfare、loader version **0.8.9.1**、Implementation-Version **0.8.9.1-mc1.20.1-993063bed**。hotfixはfilenameにのみ含まれる。終了時にも原物hash不変を再確認。[/mnt/dataではなくWindows実在パスの測定・再帰metadata/CRC](../build/verification/superbwarfare-20260913-190540/audit/input-metadata.json)。同梱Jarを一律展開・別途追加していない。
- KFF要件 `kotlinforforge [4.11.0,)`。既存 `build/verification/source-world-boot-20260906-172148/game/mods/kotlinforforge-4.11.0-all.jar` は **7,193,768 bytes / EF988F86D170AF499D147EA8A5C34C99DBD6D8F4A5AD236A4B43818DC4470A10**。コピーのみ。既存libs・2か所の指定参照MOD直下・Downloads直下の候補名・Gradle cacheの該当group名だけ確認し、PC/cache総走査なし。
- 最終runでloaderが実際に選択したMOD版: **KFF4.11.0 / SimpleBedrockModel2.5.1-forge-mc1.20.1 / GeckoLib4.4.6 / Curios5.14.1+1.20.1 / Cloth Config11.1.106 / Ponder1.0.91 / Flywheel1.0.5 / MixinExtras0.3.6 / Patchouli1.20.1-84-FORGE**。Rhino1.8.1-SNAPSHOT・mclib20・mae1.1.4・kfflang/kfflib4.11.0はMOD一覧とは別のnested libraryで、metadataと最終debug.logのdiscovery/platform記録を照合。[最終入力hash](../build/verification/superbwarfare-20260913-190540/audit/actual-server-final-inputs.json) / [実load一覧](../build/verification/superbwarfare-20260913-190540/actual-server-final/sw-result.json) / [debug.log](../build/verification/superbwarfare-20260913-190540/actual-server-final/logs/debug.log)。外部既存Curios5.12やCloth11.1.136を混ぜて選択版と誤認しない。
- 初回は必須依存が揃った状態で実攻撃テスト20件成功。ただしoptional Patchouli不在で `superbwarfare:grant_manual` がunknown `patchouli:guide_book` を参照するERROR。既存local Patchouli84（SHA **E883F33AE0E5EB128B36E145072027E620E9992E24809DC07BF4E7AC195B9519**）を最終新規環境だけへ追加し、このERRORの非再現を確認。必須化・Jar書換え・新downloadなし。
- 旧FHR2.2.5 bytecodeは[前フェーズの現物確認記録](../build/verification/open-decisions-20260913-175749/audit/superbwarfare-reference-review.json)と[javap](../build/verification/open-decisions-20260913-175749/audit/v225-DamageEventHandler.javap.txt)を再読。新実物の[javap ProjectileEntity](../build/verification/superbwarfare-20260913-190540/audit/ProjectileEntity.javap.txt)、[DamageTypes](../build/verification/superbwarfare-20260913-190540/audit/ModDamageTypes.javap.txt)、[DamageHandler](../build/verification/superbwarfare-20260913-190540/audit/DamageHandler.javap.txt)と照合。`performDamage → source factory → forceHurt/doDamage → Entity.hurt → Forge LivingHurtEvent`。通常/headshot/absoluteのdirect=実弾Entity、source=getOwner。所有者非Player/nullならFHR倍率なし。forceHurtのfallbackは静的読取のみで、今回の通常hurt成功例を強制傷害fallback全般のPASSにしない。

**実装・テスト境界**

- 正本[COMPATIBILITY_POLICY §14](COMPATIBILITY_POLICY.md#14-superbwarfare-legacy)に対応範囲・旧判定との差を集約。新 `SuperbWarfareCompat` はloader0.8.9.1・実ProjectileEntity class・`superbwarfare:projectile`・gunfire系4 DamageTypeに限定。外部classを直接参照せず、unknown版/対象外には専用倍率を付けない。版文字列gateはhash照合そのものではないため、同版別artifactの互換保証はしない。
- 新v3枝はsource Playerのcanonical食義Lvを読み、専用→global→Heroics。TaCZのglobal→TaCZ/Ammo→Heroicsは変更なし。pendingは旧global→validated legacy倍率→Heroics枝のみ、v3枝との二重適用なし。旧contains/現namespaceの広い条件は同値と扱わず、未確認の爆発/別弾/搭乗兵器を新v3へ接続していない。schema/raw/Root/購入/SP/既存再帰guardと数値安全処理を維持。
- production差分は `DamageEventHandler` と新Adapterのみ。unit・新2 GameTest、別 `src/superbwarfareTest` fixture、build.gradleの独立test source/Jar/reobf、関連docsを更新。[ファイル一覧・最終レビュー](../build/verification/superbwarfare-20260913-190540/audit/final-review.json)。配布Jarの旧版との差はmanifest・DamageEventHandler.class・新Adapter.classだけ。
- 実MOD試験は登録済み実Entityを生成し、実物のpublic `performDamage` を呼ぶ。Player capabilityをfixture準備し、LOWEST eventでamount/source/direct/attackerを観測、Cowの実HP減少も比較。**実弾の飛翔・銃入力・弾道衝突・射撃packet・client prediction・搭乗兵器・全武器を実行した試験ではない**。

| 実行・結果 | 証拠・範囲 |
|---|---|
| 焦点unit/build/check **PASS** | [focused-build.log](../build/verification/superbwarfare-20260913-190540/audit/focused-build.log)。SuperbWarfare47 assertions、既存6群142/data-boundary5000/Ammo10000等。Lv0/1/200/int境界/Long.MAX、canonical/正常移行/raw保持/不正pending、numeric safety。式試験だけを実統合としない |
| vanilla **57/57 PASS** + build/unit/check **PASS** | [final-vanilla-build.log](../build/verification/superbwarfare-20260913-190540/audit/final-vanilla-build.log)、新規 `gametest-sw-20260913-190540-vanilla`。既存55+新2、optional SuperbWarfare不在。37s。ERROR/FATAL0 |
| TaCZ **57/57 PASS** +別13/Ammo/write **PASS** | [final-tacz-write.log](../build/verification/superbwarfare-20260913-190540/audit/final-tacz-write.log)、新規 `restart-tacz-sw-20260913-190540` とそのGameTest。実承認TaCZ1.1.7-hotfix2、heat A–M・bolt/burst/reload/replay/respawn・canonical回帰。write PID23328。53s。ERROR/FATAL0 |
| TaCZ別JVM read **PASS** | [final-tacz-read.log](../build/verification/superbwarfare-20260913-190540/audit/final-tacz-read.log)。PID8132≠23328、再seedなし、別13/Ammoも成功。fixture pack metadata修正の独立Jar再生成を同コマンドで実施。34s。ERROR/FATAL0 |
| 実SuperbWarfare初回 **17実攻撃＋3 synthetic negative PASS** | [初回log](../build/verification/superbwarfare-20260913-190540/audit/actual-server-01.log) / [receipt](../build/verification/superbwarfare-20260913-190540/actual-server/sw-result.json)、新規actual-server、PID30112。製品Jar DB6891A0…80B8Aは最終Jarとmanifestだけ違い、全class/resourceは同一。Patchouli不在ERROR・sandbox認証key ERROR・fixture pack WARNは残した |
| 最終配布Jar＋実SuperbWarfare **17実攻撃＋3 synthetic negative PASS** | [最終log](../build/verification/superbwarfare-20260913-190540/audit/actual-server-final.log) / [receipt](../build/verification/superbwarfare-20260913-190540/actual-server-final/sw-result.json)、さらに新規actual-server-final、PID5768。Lv0/1/200 fresh/移行済み6件、headshot/absolute/split/headshot split4件、global/Heroics/TaCZ非漏洩1件、別Player/null/nonPlayer3件、long1件、不正/編集pending2件。別枠synthetic3件はexplosion/projectile_hit/custom_explosion非適用 |

- 操作主体はすべて **AUTOMATED**。COMPUTER USE/HUMAN試験なし。初回のassertion FAILや製品不具合はなし。最終runではfixture pack metadataとPatchouliを補充し20ケースを最終Jarで再確認した。全試験の[抽出証拠・hash・ERROR・保存行](../build/verification/superbwarfare-20260913-190540/audit/test-evidence.json)を保全。
- 最終実MOD runのERRORは `Yggdrasil Key Fetcher` の公開鍵取得がsandboxで `Permission denied` となった1件。認証accountや接続を試していないので**認証通信PASSなし**。Patchouliマニュアル解析ERRORとfixture pack WARNは最終ではなし。起動既定Config生成・FML library metadata・WMI/terminal等のWARNはログに保持。外部MOD全機能・全warning解消は宣言しない。
- コマンド: 既存Gradle8.1.1 + `-g <LOCAL_GRADLE_HOME> --offline --console=plain`。vanillaは `-PfoodHealingGameTestRun=sw-20260913-190540-vanilla runGameTestServer build foodHealingUnitTest check`、TaCZは `-PfoodHealingTaczIntegration=true -PfoodHealingRestartRun=tacz-sw-20260913-190540 -PfoodHealingRestartPhase=write/read -PfoodHealingRestartEulaAccepted=true` とwrite時 `runGameTestServer runServer`、read時 `runServer superbwarfareTestJar`。実MODは[準備helper](../build/verification/superbwarfare-20260913-190540/audit/prepare_server.py) / [起動helper](../build/verification/superbwarfare-20260913-190540/audit/run_actual_server.py)でJava17 +既存Forge47.2.0 libraries読取、loopback/port0/RCON&query OFF、各新規world。試験server以外を起動していない。

**成果物・終了・残gate**

- 最終 `build/libs/foodhealing-3.0.0.jar`: **200,558 bytes / 125 entries / SHA-256 DC3663C68A3DC7D76BCC1D1F844CFCD68CF809113FB1AA892BCAE9049D8D10BC**。CRC正常、MOD ID/version、Mixin target class/refmap存在、外部MOD/同梱library/test fixture/ExampleMod非混入、reobf outputと最終actual-server配置Jarのhash一致。[final-jar.json](../build/verification/superbwarfare-20260913-190540/audit/final-jar.json)。metadata/Mixin/refmapをSWのために変更していない。実client NOT RUN。
- 各GameTest/serverは **通常saveEverything/Stopping server/全dimension保存→プロセス終了**。実MOD PID30112/5768はexit0、TaCZ write23328/read8132も通常終了。最終CIMは成功し今回PID/固有run名に一致するprocessなし。[process-final.json](../build/verification/superbwarfare-20260913-190540/audit/process-final.json)。今回worldの再利用はTaCZの明示したwrite→readだけで、過去world・原本・Prism再開なし。
- **RELEASE CANDIDATE = NO / REAL 2-CLIENT = BLOCKED / ALL TACZ・ALL GUNPACK COMPATIBILITY = NOT TESTED**。新Jar実client・True Root未決定境界・両極意効果未完成/購入停止・Flight ownership/購入停止・Break Realm expansion NOT IMPLEMENTED・Bulwark未開始・試作型機関弩完全一致gate・V3M0908 FOURTH BOOT NOT RUN/NOT AUTHORIZEDを維持。OPEN-02/03/04は既にLOCK済みで戻さない。食料生産の極意の可逆クラフト増加は既知かつ許容された仕様、バグ修正対象外。死亡drop/replay/desync等の意図しない重複は許容しない。
- 次の候補は**承認後の最終Jar＋SuperbWarfareによる新規隔離実client最小射撃確認**。今回残る「本体未提供」「通常弾接続未着手」の停止理由はない。範囲外経路や認証・全互換を自動PASSにしない。

<a id="evidence-open-decisions-20260913"></a>

### 2026-09-13 OPEN-02/03取得前提・OPEN-04倍率維持LOCK / 前提実装・自動55/55 / SuperbWarfare接続のみBLOCKED

- **開始・保全**: 正本14:23 JST・実workspace `<LOCAL_PATH>/food-healing-mod-main` を照合。新root `build/verification/open-decisions-20260913-175749/` の `before/` に対象src/docs/ルール/build設定/Jar等122ファイルを保全。[開始manifest](../build/verification/open-decisions-20260913-175749/audit/before-manifest.json)。.gitなし、Git差分の主張なし。原本/過去world・Prism未起動。旧配布Jar **196,761 bytes / 123 entries / 15BE0512CF2602FC399F518BFA919460F26BB4F74EF8DDF4DC5AC589C44300FB**を保存。
- **利用者決定**: OPEN-02/03は取得条件 **RESOLVED / LOCKED**、OPEN-04は旧倍率維持と式 **RESOLVED / LOCKED**。正本はSKILL_TREE_SPEC §9–10・COMPATIBILITY_POLICY §14、SPECは参照で反映。AUDIT_REPORTの現在OPEN索引とCODEX_LOOP_PROMPTの古い未確定記述を必要箇所だけ更新。旧選択の履歴を残し、取得前提のLOCKを効果完成や実統合PASSにしない。
- **実装範囲**: FoodHealingSkillsの両nodeは既存ID/requiredLv1・cost100/500のdata-drivenリストを維持しopenPrerequisites=false。両極意をIMPLEMENTATION_PENDINGに加え、効果未完成なのにSPだけ消費することを防止。新しいread-only `purchaseStatus` をserver transactionとGUIで共有。未知ID/移行pending/未確定定義（将来用）/stale/max、前提、SP、対応版optional MOD、実装readinessを区別。前提/SPが不足すればその理由、揃っても両極意は未readyで拒否。SP支出は従来trySpendSkillPointsの会計上限保護付きでserverだけが実行し、expectedLevelによる重複防止を維持。packet形式/protocol6/sender・consumerMainThreadは変更なし。
- **GUIと管理経路**: 費用と前提の既存表示は確定リストを使い、ボタンとtooltipは共有判定を使用。PurchaseSkillPacketの返答とen/ja翻訳を前提不足/SP不足/効果・Adapter未ready/対応TaCZ1.1.7-hotfix2不足等へ分離。旧OPEN判断待ちを両極意の購入不可理由にしない。既存管理commandはSP準備・詳細・toggleで、購入/強制skill付与経路は追加しない。未取得の両極意を詳細/toggleで取得させないことを登録command経由で確認。効果時の親依存・toggle規則、schema/migration/保存形式は変更なし。
- **SuperbWarfareの確認済み証拠**: [旧実Jar照合](../build/verification/open-decisions-20260913-175749/audit/superbwarfare-reference-review.json)、[v2.2.5 DamageEventHandler bytecode](../build/verification/open-decisions-20260913-175749/audit/v225-DamageEventHandler.javap.txt)。旧FHRは `source-world-boot-20260906-172148/game/mods/foodhealing-2.2.5.jar`、**80,331 bytes / SHA256 5BAC83E55419D96849823D7A7BB0933E01600564E13739992E43782C925A6A8C**、metadataで2.2.5確認。追撃再帰guard→source Player→そのcapability level、level>0・directEntity class名にsuperbwarfareを含む経路へ専用倍率1回。旧local順はTaCZ専用→SuperbWarfare専用→global→Heroics。任意の爆発/搭乗兵器・全owner対応は証明していない。
- **SuperbWarfareの不足・非実装**: libs1Jarと既存の承認source runtime参照2箇所（各104Jar）のmods.toml/IDを必要範囲で読取し、SuperbWarfare本体artifactなし。正確な対象version・entity registry/type・Player source帰属・他MOD event合成は未確認。現v3はglobal→namespace別専用→Heroicsで、SuperbWarfareはpending legacyだけに適用されるため、fresh/移行済v3への接続は不足。DamageEventHandler/LegacyProjectileCompatは今回未変更。仮class/event/Mixinで実装せず **BLOCKED - TARGET ARTIFACT / ROUTE VALIDATION REQUIRED**。数式のみの新規unitや実統合PASSは記録しない。式・保持選択はLOCK済みで再質問しない。

| 自動検証 | 実行結果・証拠 |
|---|---|
| 焦点unit | [corrected log](../build/verification/open-decisions-20260913-175749/audit/focused-unit-corrected.log): **6群 / 142 assertions PASS**。2nodeの費用/前提、親OFF、前提とreadinessの分離、SP不足/不正/stale/max/会計overflow無消費、複数前提AND・必要Lv、readyなTrue Heroics購入100SPを別確認、en/ja理由を確認 |
| 最終build/unit/check＋vanilla | [log](../build/verification/open-decisions-20260913-175749/audit/final-vanilla-build.log): **exit0 / BUILD SUCCESSFUL 39s**、build/foodHealingUnitTest/check、GameTest **55/55**。既存49＋新規6を削減せず実行。unitには既存migration・data-boundary5000・Ammo等を含む。標準test taskの件数と独自main harnessを混同しない |
| TaCZ最終GameTest・統合write | [log](../build/verification/open-decisions-20260913-175749/audit/final-tacz-write.log): **exit0 / BUILD SUCCESSFUL 53s**、承認TaCZ1.1.7-hotfix2、GameTest **55/55**、別suite **13ケース**、Ammo/heat A–M/closed・open・manual bolt/burst/reload/replay/respawn等の既存回帰PASS、write PID4572 |
| 別JVM read | [log](../build/verification/open-decisions-20260913-175749/audit/final-tacz-read.log): **exit0 / BUILD SUCCESSFUL 31s**、read PID2184≠4572、既存2player canonical保持・Ammo Lv10/SP17/spent61/OFF/chamber・otherHeat123等一致。read時に初期状態を再注入しない。13ケース等の既存fixtureもreadフェーズで成功 |

- **新規GameTest6件**: 両nodeの前提成立/未ready拒否各1、各nodeの登録packet不正・重複拒否/同期・他player不変各1、readyな既存True Heroics nodeの親OFF購入1回と同期/再送拒否1、既存管理commandが未ready nodeを取得させない1。テストfixture内で親取得を準備するが、製品readinessを変更して両極意を購入させていない。前提判定PASSと効果購入成功を区別する。
- **実行方法と隔離**: 既存local `<LOCAL_GRADLE_HOME>/wrapper/dists/gradle-8.1.1-bin/9wiye5v2saajue4irfo8ybqfp/gradle-8.1.1/bin/gradle.bat -g <LOCAL_GRADLE_HOME> --offline --console=plain`、JAVA_HOMEはAdoptium17.0.7。焦点は検証rootの `-I audit/focused.init.gradle verifyOpenDecisions`。vanillaは `-PfoodHealingGameTestRun=open-decisions-final-20260913-175749 build foodHealingUnitTest check runGameTestServer`。TaCZは `-PfoodHealingRestartRun=tacz-open-decisions-20260913-175749 -PfoodHealingTaczIntegration=true -PfoodHealingRestartEulaAccepted=true` とwriteの `runGameTestServer runServer`、別readの `runServer`。作業worldは新規 `gametest-open-decisions-final-20260913-175749` と `restart-tacz-open-decisions-20260913-175749` 内のみ。旧world/通常run/worldは使わない。保存・停止の[集約証拠](../build/verification/open-decisions-20260913-175749/audit/test-evidence.json)と新しいtoken/phase/PASS receiptを保持。
- **途中失敗と訂正**: 初回focusedは既存user cache指定不足でoffline plugin解決失敗、明示cacheのsandbox実行はnative DLL読込失敗。その後の承認済み実行で検証用taskのworkingDirが二重に連結されCreateProcess267（[診断](../build/verification/open-decisions-20260913-175749/audit/focused-unit-diagnostic.log)）。検証initのみproject.fileで絶対解決するよう訂正し焦点PASS。compile/製品assertのFAILや試験弱体化ではない。metadata readerは別MODの非UTF8コメントで一度停止し、ASCII modId照合に必要な読取と置換有無の記録へ修正。初回process CIM照会のaccess deniedは不在証拠にせずUNVERIFIEDで保存し、後続の成功照会で確認。失敗logを削除しない。
- **通常終了と限界**: vanillaは18:11:22 Game test server shutting down、TaCZ writeは18:13:08、readは18:14:15に全dimension保存・config unloadまで完了。全Gradle exit0、[成功したprocess照会](../build/verification/open-decisions-20260913-175749/audit/process-final.json)で今回server PID/labelなし。強制停止なし。最後の3実行logはERROR/FATAL0だが、既存Forge設定補完・language Jar/refmap警告・DEBUG例外やGradle deprecationを全解消したとはしない。今回は実client/HUMANゲーム操作なし。新Jarは **REAL CLIENT NOT RUN**。
- **成果物・変更監査**: 新Jar **198,117 bytes / 124 entries / SHA256 `2BA3F97F1F1092CAABE8161983DA9AA92F1029D365B56C7AC3AC1E683610CEF0`**。metadata3.0.0、必須Mixin/refmap、最新reobf出力hash一致。追加1entryは購入結果のenum switch用の生成classで、test-only/fixture/外部Jar/ExampleModなし。旧/newJar内のdamage/Root等の非変更を確認。[実ファイル差分](../build/verification/open-decisions-20260913-175749/audit/changes.diff)・[最終照合](../build/verification/open-decisions-20260913-175749/audit/final-review.json)。変更は購入判定/GUI/packet返答・翻訳、関連test、正本文書/Status/関連する旧ルール参照、Jarと検証生成物。build設定/依存/製品Config/schema/migrationは不変。
- **レビューとgate**: 通常コードレビューでserver主導・SP保護・共有preview・旧data保持・optional境界を確認。公開API追加調査不要でContext7未使用。Codex Security scanは未実施：このworkspaceは.gitなしでimmutable diff/provider baselineなし、scan用Git作成/外部送信/権限変更は行わない。通常差分レビューをSecurity scanとしない。OPEN-02/03/04の今回LOCK以外、True Root未決定境界、Flight購入停止、Break Realm expansion NOT IMPLEMENTED、Bulwark、試作型機関弩完全一致開始条件、FOURTH BOOT NOT AUTHORIZED、REAL 2-CLIENT BLOCKED、全TaCZ/全gunpack・dedicated遅延NOT TESTEDを維持。可逆craft増加は既知許容仕様・バグ修正対象外、死亡drop/replay/desync等は許容しない。**RELEASE CANDIDATE = NO**。

<a id="evidence-direct-jar-20260913"></a>

### 2026-09-13 最新配布Jar直接Core/TaCZ限定確認・残release整理 / 両構成完了・通常保存終了

- **対象と保全**: 11:05正本と実配布Jarを照合して新root `build/verification/direct-jar-20260913-114729/` を作成。開始Status全文/配布Jarは `before/`、12:00認証待ち時点のStatusは `before/CODEX_STATUS-auth-pending.md` に保全。FHR **196,761 bytes / 123 entries / SHA256 `15BE0512CF2602FC399F518BFA919460F26BB4F74EF8DDF4DC5AC589C44300FB`**、TaCZ **52,426,062 bytes / SHA256 `FC5F1DAB09AFD5399604DB0F60845D41D46CF015C158CBC12E7EF716CF6BDB46`**、実manifest **1.1.7-hotfix2**。[最終Jar](../build/verification/direct-jar-20260913-114729/audit/final-jar.json)と[保存・ロード・保全の最終照合](../build/verification/direct-jar-20260913-114729/audit/final-review.json)で現物から取得。元/各instance配置Jar hash一致、再生成なし。
- **隔離と実環境**: 既存許可listの配布runtimeだけ **4179 files / 974,615,788 bytes** を新portable Prismへコピーし、2instanceで共通runtimeを1組利用。旧instance/world/launcher設定/accounts/token/passwordはコピーしない。コピー元の指定4179ファイルは終了後もsize/hash/mtime不変。MC **1.20.1 / Forge47.4.0 / LWJGL3.3.1 / Microsoft Java17.0.15+6-LTS / Prism10.0.5 / CURRENT ForgeWrapperprism-2026-08-01**。FHR Forge[47,)・既承認実環境の組合せで、開発47.2.0と区別。実起動logのバージョンとロード元を照合。新instance window **1280×800 / GUI Scale2**、Java自動download/detection OFF、local Java、512〜4096MiB、初期fml.versionCheck=false。製品/普段の設定は変更せず、必要な既定configは新instance内で通常生成。
- **準備中の経緯**: 最初のartifact readerはTaCZのversionをmods.toml literalと想定して停止したが、`${file.jarVersion}` とmanifestを照合するよう読取helperだけ訂正し、その後コピー成功。12:00時点はPrism初期wizard完了・認証待ちで両Minecraft **NOT RUN**、world未生成だった。当時の未実施を遡ってPASSにしない。その後HUMANが通常Microsoft認証を完了し、Leva9846表示・未起動との報告を受けて続行。Core起動後の最初のnative window操作は承認待ちtimeoutとなり、約1時間26分はタイトルで待機。再観測・通常activateで操作可能となった。認証回避/権限設定変更/既存world再開なし。
- **主体**: HUMANは通常MSA認証のみ。以後の起動、world作成、既存command、GUI購入/toggle、R/G/左click、F2、通常保存・1回再読込・Minecraft終了は **COMPUTER USE**。**AUTOMATED** は配布物hash/ZIP・log・通常保存NBTのread-only比較であり、build/GameTest/49件/suite/別JVM試験の新規PASSではない。observer/fixture/開発classesをruntimeへ追加せず、HP/Root状態/弾薬数/薬室/heatを直接編集していない。

| 構成 | 実行・観測と判定 |
|---|---|
| 第1 Core | `FHR_Direct_Core_20260913/minecraft/mods/foodhealing-3.0.0.jar` の通常ロードをdebug.logで確認、MOD画面/valid mod IDsはminecraft/forge/foodhealingの3件。新world `FHR Direct Core 20260913`、Survival/Peaceful/cheats ON/Superflat/structures OFF。HP20/20 HUDとE→S GUIを確認。既存 `/foodhealing syokugi setskillpoint 10`、Fire Resistance Mastery Lv1を通常購入（1SP）、ON表示からOFFへ切替。初回保存・**1回だけの通常再読込**・最終保存でSP9/spent1/Fire1 OFFのtyped FoodHealing canonicalが全一致、再読込GUIも一致。Root active/reservation0・EatCount0・inventory空。**配布Jar基本GUI/通常保存の限定PASS** |
| 第2 TaCZ | 別 `FHR_Direct_TaCZ_20260913` / 新world `FHR Direct TaCZ 20260913`、同world設定。通常mod IDsはminecraft/forge/foodhealing/tacz/mixinextras（同梱0.3.6）の5件。FHR/TaCZ両Jarの実modsロード元・version一致。既存50SP command後、通常GUIでAmmo Lv1→10を各5SPで購入、SP50→0/spent50、Lv10 ON→OFF表示を確認。実artifactでGlock17/9mm・stack60・mag17・closed_bolt・SEMIを確認し、通常giveはGunId/AmmoIdの識別のみ。GでSEMI選択、Rで通常reload、ON/OFF各1回の通常左click入力。下表のHUD/保存一致を確認。**配布Jar＋承認TaCZの最小比較は限定PASS**。strict physical bullet/entity数や独立server shot traceは取得せず、heat・全内部遷移等のPASSに拡張しない |

TaCZの通常pause保存比較（絶対gameTimeは実保存値。GUI操作・pauseの現実時間をtickへ換算しない）：

| 段階 / snapshot | gameTime | Ammo Lv10 | HUD装填 / 予備 | 保存mag / chamber / reserve |
|---|---:|---|---|---|
| `tacz-loaded-before-on-shot` | 3400 | ON | 17 / 43 | 16 / 1 / 43 |
| `tacz-after-on-shot` | 4051 | ON | 17 / 43 | 16 / 1 / 43 |
| `tacz-after-off-shot` | 5015 | OFF | 16 / 43 | 15 / 1 / 43 |
| `tacz-final-closed` | 5016 | OFF | 終了後の保存照合 | 15 / 1 / 43 |

- **証拠入口**: [操作・経緯](../build/verification/direct-jar-20260913-114729/audit/session-notes.md)、[Coreロード証拠](../build/verification/direct-jar-20260913-114729/audit/core-load-evidence.log)、[Coreゲームlog](../build/verification/direct-jar-20260913-114729/audit/core-game-evidence.log)、[TaCZロード証拠](../build/verification/direct-jar-20260913-114729/audit/tacz-load-evidence.log)、[TaCZゲームlog](../build/verification/direct-jar-20260913-114729/audit/tacz-game-evidence.log)。snapshotは同audit内 `<stage>-snapshot.json` とread-only copy `<stage>-level.dat`。最終通常level.datとplayerdataのForgeCaps/Inventory一致も確認。F2は各新instance `minecraft/screenshots/`。Core 04.54.08/04.55.50（GUI保存前/再読込）、TaCZ14.06.14（Lv10 ON）、14.07.03（reload）、14.07.55（ON後）、14.11.30（OFF）、14.12.07（OFF後）。**Coreのlog/F2時計はUTCでJSTへ+9h、TaCZはJST**。snapshot/OS時刻と混同しない。
- **初回Core保存のretryを保持**: 13:54:40.342–.345 JST、level.dat→level.dat_oldのWindows「他process使用中」によるrename失敗6ERROR・retry0/10〜2/10。同時にread-only snapshotを実行しており、読取handleとの重なりが疑われるがlock保持PIDは未測定。同.346に全dimension保存完了へ回復。その後1回再読込のGUI/canonical一致、最終通常保存のERROR0を確認。以後snapshotは通常pause保存から約10秒以上待つか終了後に取得。保存失敗の経緯を消さず、製品不具合/保存一般の完全無欠とは判定しない。`core-gui-before-save` はGUI pause中の未flush旧SP10を採った基準値で、購入後不一致/同期不具合の証拠ではない。
- **通常保存・終了**: Coreは13:56:22から最終Save & Quit、13:56:23.292全dimension保存、13:56:44.265 `Stopping!`、13:56:59にはPID8800消失。TaCZは14:12:33.809 Save & Quit、14:12:34.669全dimension保存、14:12:56.040 `Stopping!`、14:13:11にはPID21552消失。両タイトル→Quit GameをCOMPUTER USEで実行し、window消失・最終level/playerdata確認、強制終了なし。[後続process確認](../build/verification/direct-jar-20260913-114729/audit/post-shutdown-process.json)も該当Java不在。Prismだけが残り、Minecraft session維持/再開指示なし。
- **WARN分類・製品修正判定**: latest.logはCore ERROR6/FATAL0/WARN57（上記retryを含む）、TaCZ ERROR0/FATAL0/WARN125。新configの既定値補完、language Jar mods.toml/union asset、test-food model、vanilla sound/shader、IPv6値解釈の警告を保持。今回ロード/GUI/限定保存・操作の阻害や必須Mixin/classloading失敗は再現せず、全警告解消/全互換保証とも扱わない。確定仕様の製品不具合は再現せず、production/test/Jar修正・再buildなし。
- **表示差の読取調査**: [結果](../build/verification/direct-jar-20260913-114729/audit/display-review.json)。FoodHealingScreenはserver由来deadlineからclient gameTimeを引き20ticksで切上げ、RootControllerはserver now+300で発動/終了を判定。表示経路から期限への書込なし。9/12 F2の16秒とbefore3549→deadline3849（300server ticks）は実証済み。ただしその瞬間のclient時計は未保存、9/13既存observerlogにも301〜320残tickの該当0件。当時の原因/正確なずれは **未判明**。コード上の時計差/切上げ機構は確認済みだが、それを歴史的原因の実測としない。表示以外の新たな影響を示す証拠なし、直前の保護PASS/絶対期限UNCHANGEDを維持。追加食事/保護試験なし。
- **残release・境界**: 現在要約に既存安全条件、未実装、未検証と合格条件、仕様判断、artifact/account待ち、明示開始保留、表示/警告の分類を更新。OPEN-02/03前提・OPEN-04倍率の選択肢/影響を正本に沿って提示したが未LOCK。必須が資料にない網羅/性能項目は判断待ち。B購入時heat INCONCLUSIVE、C厳密physical shot/E全内部遷移未観測、hot-gun SKIPPED、dedicated遅延/全TaCZ/全gunpack NOT TESTED。部分蓄積/親Root OFF/death/logout/restart予約未決定、REAL 2-CLIENT BLOCKED、FOURTH BOOT NOT RUN/NOT AUTHORIZED、Flight/Break Realm/Bulwark/試作型機関弩の全gate維持。可逆craft増加は既知かつ許容された仕様でバグ修正対象外、死亡drop/replay/desync/意図しない重複まで許容しない。**RC=NO**。
- **変更範囲**: 既存baseline112ファイルの実測で変更はこのStatusのみ。製品code/test/Jar/Config/schema/migration/依存不変。追加物は新verification rootの準備・証拠と新instanceの通常生成物。原本/過去worldは開かず、対象copy元以外の大規模走査なし。.gitなし、git diff確認済みとはしない。49/49等の既存成功を再実行せず保持。公開API調査不要でContext7未使用。Codex Security scanも未実施（新production差分なし・.gitbaselineなし、scan目的のGit作成/外部送信/権限変更なし）。通常read-onlyレビューとSecurity scanを区別する。

<a id="evidence-true-root-protection-deadline"></a>

### 2026-09-13 True Root保護実測PASS・activeUntil絶対期限UNCHANGED / 新規隔離実client・通常終了

- **今回の範囲**: 9/12 23:17 JSTの正本から、残っていた保護の判別実測と発動中OFF→ONの絶対期限だけを追加確認。昨日のOFF保持/1回消費/消費後非再発動・自動49/49は維持。既存session/worldを再開せず、専用root `build/verification/true-root-protection-deadline-20260913-065752/` と新規world `client/saves/TrueRoot_Protection_20260913` を使用。Minecraft1.20.1/Forge47.2.0/Adoptium17.0.7/外部MODなし、開発ソース出力runClient。配布Jar直接/Prism/dedicated試験ではない。
- **事前の判別方法**: SPEC section17・TEST_PLAN・RootController・DamageEventHandler・実際のHP下限/lifecycle処理を必要範囲だけ読取。現行active保護はHP1側でdamageを抑えるため、HP19へdamage1では判別不能。防具/absorption/他防御skillなしのHP20へgeneric19.5を用い、非発動ならHP0.5、発動中ならHP1.0という両方非致死の差を採用。damage入力/handler後の量も観測し、後からのHP復元と混同しない。Root/HP/NBT/時計の直接設定、親RootOFF、死亡、未決定境界の試験はない。
- **隔離した観測手段**: invocation専用Gradle initと`rootObservation` sourceSetからread-only observerをrunClientだけへ追加。server thread上の既存getter/canonical NBT、同期client、HP/armor/absorption/effects、LivingHurt入力/LivingDamage後を記録する。setter・イベント改変・Root呼出しによる発動・Mixin/reflection・製品debug追加なし。通常pause保存のlevel.datとF2を併用。observer class出力は専用root内、最終製品Jarには非混入。ブロックの実行条件はNBT読取だけで、Rootを作る処理ではない。
- **操作主体**: HUMANは通常の食事2個→GUIを2回と、初回の手入力damage→GUIを1回。COMPUTER USEは新規Survival/Normal/cheatsON/Superflat/structuresOFF world作成、既存29SP command・食料付与・通常GUI Root5/True1購入/toggle、非発動control、検証ブロック配置/解除、通常保存/終了。自然回復/自然mob spawnはこのworld作成時OFF。AUTOMATEDは検証専用ブロックの既存damage command1回とread-onlyログ/snapshot比較。GameTest49/49等を再実行した意味ではない。
- **タイミング不足の記録を保持**: 最初のHUMAN damageはgame3464、準備したactiveUntil3171の後だったことを実イベントlogで確認。HP0.5になった試行を製品FAIL/保護PASSへ置換しない。同じ手入力手順を繰り返さず、使い捨てworld内のverification-only repeating command blockへ変更した。HP20・専用tag・非zero期限の読取条件で待機し、tagは食事/元window終了後に付与。実被弾が期限前だったことは別途observerで確認する。HP20 guardにより被弾後の再処理なし。ブロック構成・正確なcommandは[試験計画](../build/verification/true-root-protection-deadline-20260913-065752/audit/plan.md)に保持。

| 今回の項目 | 判定・実際の証拠 |
|---|---|
| A True Root発動中の保護実測 | **PASS（generic19.5/HP1下限保護の範囲）**。非発動controlはgame1825、HP20→0.5、入力19.5/handler後19.5。通常食事で準備した予約をGUI再ONで使用しgame5717にdeadline6017/予約0。直後game5718<6017の実被弾はHP20/入力19.5→handler後19.0、uncanceled、rootActivetrue、armor0/absorption0/effectsなし。game5939の通常pause保存とserver/client観測でHP1.0、deadline6017/Guts V78を確認。3件目の被弾のみがactive probeで、全体の被弾件数はcontrol・遅延した人間試行・自動probeの3件 |
| B 発動中OFF→ONの絶対activeUntil | **UNCHANGED確認**。通常GUI pauseでserver gameTime5939/client5938、切替前ON・OFF後・ON後のserver/client canonical deadlineがすべて**6017**。親RootON/予約0/HP1/SP0・spent29/取得Root5・True1も維持。発動5717から222tick経過しており、全300tickへリセットなら6239になるため判別可能。途中OFFと最終ONを実観測し、残時間同一を要求する検査ではなく絶対期限を比較。対象は今回の実client GUI packet経路で、dedicated/通信遅延全般の確認ではない |

- **主要証拠**: [測定値比較全文](../build/verification/true-root-protection-deadline-20260913-065752/audit/measurement-review.json)、[操作と結果の記録](../build/verification/true-root-protection-deadline-20260913-065752/audit/session-notes.md)、[runClientログ](../build/verification/true-root-protection-deadline-20260913-065752/audit/runClient.log)、[最終照合](../build/verification/true-root-protection-deadline-20260913-065752/audit/final-review.json)。`client/logs/latest.log`の被弾はcontrol250–251、遅延試行5186–5187、active6695–6696行。`audit/deadline-{before-off,after-off,after-on}-observation.json`に各段階のcanonical NBTを保存。`automatic-probe-after-damage-snapshot.json`とraw level.datは通常pause保存を読取。F2 `2026-09-13_10.58.08.png`はactive4s/予約0/18/HP1を表示するが、絶対期限判定はcanonical値に基づく。
- **保存・終了**: 専用tag解除、ブロックauto0を通常commandで確認後、期限/予約0・HP1・効果なしのgame7794からSave and Quit。11:00:06 JSTにSaving players/worlds/All dimensions are saved、タイトル表示後11:00:19にQuit Game/Stopping!。runClient session45790は**exit0 / BUILD SUCCESSFUL in3h59m24s、12tasks（3 executed/9 up-to-date）**。長い実時間は通常pauseでの人間応答待ちを含み、長時間負荷試験ではない。Minecraftウィンドウ消失・PID10248不在を11:00:35に確認、強制終了なし。[終了後保存](../build/verification/true-root-protection-deadline-20260913-065752/audit/after-normal-shutdown-snapshot.json)はgame7795/HP1/active0/reserved0/cooldown6017/Root5+True1ON/SP0/spent29/EatCount4。予約を残すlogout/restartは試していない。
- **修正・成果物・保全**: 確定仕様の不具合は今回再現せず、production/test修正・Jar再生成・build/check/GameTest/別JVM回帰は行っていない。起動時は既存cache/Gradle8.1.1/--offline、observerの専用compileのみ。製品source出力はUP-TO-DATE。開始前Status全文は同root`before/CODEX_STATUS.md`へ保存。Status編集前に112/112対象ファイルのsize/hash一致を確認。[最終Jar実測](../build/verification/true-root-protection-deadline-20260913-065752/audit/final-jar.json)は**196,761 bytes/123 entries/SHA256 15BE0512CF2602FC399F518BFA919460F26BB4F74EF8DDF4DC5AC589C44300FB**で変更なし・observer非混入。変更はStatusと新規専用rootの検証支援/証拠/通常ゲーム生成物だけ。`.git`なし、git diff確認済みとはしない。
- **残事項/gate**: ERROR0/FATAL0/WARN53（runClientログのレベル集計）。既存test-food model/dev/refmap/config/shader/DEBUG exception/非推奨等はログに残し、全WARN解消や外部通信0とはしない。昨日のGUI16秒/server300tick表示差は原因未確認のまま。配布Jar直接・新仕様dedicated通信、OPEN-02/03/04、部分蓄積/親RootOFF/lifecycle未決定、REAL 2-CLIENT BLOCKED、ALL TACZ/ALL GUNPACK COMPATIBILITY NOT TESTED、Flight購入停止・Break Realm expansion NOT IMPLEMENTED・Bulwark・試作型機関弩完全一致開始条件・FOURTH BOOT NOT AUTHORIZED、**RC=NO**を維持。食料生産の可逆craft増加は既知の許容仕様。9/12の限定結果・過去FAIL/NOT RUN/49/49・41/41等を当時の履歴として保持し、今回完了した2点だけ現在の未完了から除いた。

<a id="evidence-true-root-realclient"></a>

### 2026-09-12 True Root新仕様・新規隔離実client / 限定確認・未観測を分離 / 正常終了

- **実行環境・範囲**: 22:12起動〜23:11通常終了（JST）。Minecraft1.20.1 / Forge47.2.0 / Adoptium17.0.7、開発ソース出力のrunClient。外部MODなし、画面3 mods loaded。配布Jar直接/Prism/dedicated試験ではない。新規root `build/verification/true-root-realclient-20260912-221016/` の `client/saves/TrueRoot_20260912` だけを通常UIで新規作成。Survival/Normal/cheats ON/Superflat/structures OFF、naturalRegeneration=false・doMobSpawning=falseを作成画面で指定。他の防御skill・装備・base購入なし、親Root5は一貫してON。
- **準備・実行**: 既存ローカルGradle8.1.1を `--offline --console=plain -I .../audit/client-isolation.init.gradle runClient` で使用。compileJava/processResources等はUP-TO-DATE。新規専用configのversionCheck=false、製品設定/依存/OS/認証設定変更・新規download・Security scanなし。初回準備helperのrunClient task解決失敗は `prepare.log` に保持し、遅延task登録に対応して `prepare-corrected.log` で準備成功。ゲーム不具合のFAILではない。
- **操作主体**: COMPUTER USEでworld作成、既存 `/foodhealing syokugi setskillpoint 29`、通常GUI購入（Root計9SP＋True Root20SP）、toggle、画面確認、give/damageの準備、通常保存・終了。HUMANにはツールに長押しAPIがないため通常食事2個→GUIを2回、時間内操作のため既存damageコマンド→GUIを2回依頼。食料は実registryでNutrition25を確認した `foodhealing:normal_test_food`、合計4個。初回C終了後にのみBの不足確認用2個を追加。予約/NBT/時計/APIの直接変更はしていない。AUTOMATEDは今回新規実行していない。
- **証拠入口**: [全操作記録・最終訂正](../build/verification/true-root-realclient-20260912-221016/audit/session-notes.md)、[runClientログ](../build/verification/true-root-realclient-20260912-221016/audit/runClient.log)、[最終照合](../build/verification/true-root-realclient-20260912-221016/audit/final-review.json)。以下のsnapshotは同rootの `audit/<stage>-snapshot.json` と通常保存時の `<stage>-level.dat`。gameTimeを比較し、pause中の現実時間を経過tickとして数えていない。

| 項目 | 判定・実測の範囲 |
|---|---|
| A 完成済みOFF予約保持 | **状態遷移の限定PASS**。`a-human-food-paused`: game1899/activeUntil2144/reserved18/Guts V245。GUIでTrue RootだけOFF→world経過→`a-off-after-expiry`: game2632/active0/reserved18/効果なし。OFF中の再発動なし、元期限終了後も完成済み18を保持。HP20→19のdamage1は保護消失を判別する試験にはならず、その実測は未確認 |
| B 期限終了後再ON | **予約消費・発動状態の限定PASS、保護実測は未確認**。`b-before-enable`: game3549/active0/reserved18/HP19。通常GUI再ON後、`b-active-before-c`: game3725/activeUntil3849/reserved0/Guts V124/HP19。期限3849=3549+300tick（15秒）。SP0/spent29・取得Root5/True1・他skill不変。直後のpause GUIは実際に16秒/予約0と表示し、15秒表示だったとは記録しない。丸め/client時計差は可能性に留め、原因は未確認 |
| C 発動中と消費後の切替 | **一部確認、厳密な期限不変は未観測**。game3725のpause中ON→OFF→ON後もGUI7秒/予約0で、全時間を再充填した表示なし。game3958では既にactive0/cooldown3849で、全時間リセットなら期限4025となる点とも区別。切替直後かつ期限内のactiveUntilは保存できず、微小な延長もないと実client証拠だけでは断言しない。食事追加なしで期限後再びOFF→ON→`c-expired-retoggle-final`: game4230/active0/reserved0/効果なし/EatCount2。消費済み予約の再発動・複製/再充填なしは限定PASS |
| B 保護確認の2試行 | **確認手順不足 / UNVERIFIED、仕様FAILではない**。初回は残124tickから操作し最終game3958/HP18/active0で、有効期間内の被弾を証明できなかった。再準備では新規予約18を保持してgame7195で再ON、途中のCodexによるworld復帰を省き全300tickを人間操作へ渡したが、`b2-human-damage-paused`: game7508/HP18/active0/cooldown7495。被弾時のgameTimeは未記録で、保存時が期限後というだけで被弾も期限後と断定しない |

- **判定の訂正**: 現行 `DamageEventHandler.onLivingDamage` はHP1を下回る側でclamp/cancelし、HP19へのdamage1は発動中でも減少し得る。したがって当初想定した「HPが減らなければ保護PASS」という確認方法は不十分だった。OFF controlも同様に判別不能。これはコードの読取結果であり、新たな仕様LOCKではない。両試行を保護PASSや保持・再ON実装の再現FAILへ置き換えず、死亡試験・第三の同手順は行わなかった。初期の暫定判断は操作記録内に残し、末尾で明示訂正した。
- **画面証拠**: `client/screenshots/` の22.20.22（購入）、22.30.51（予約18）、22.32.56（True OFF/Root ON）、22.38.26（再ON16秒/予約0）、22.41.19（発動中再切替後7秒）、22.49.43（消費後0）、23.02.00（B再準備後の再ON）、23.10.38（最終0）。画面だけで保護実測済みとはしていない。
- **正常保存・終了**: `final-before-save-quit` game7866でactive0/reserved0/効果なし/HP18を確認し、COMPUTER USEでSave and Quit to Title。23:11:10–11にSaving players/worlds/All dimensions are saved、タイトル画面を確認してQuit Game、23:11:30にStopping!。runClientの実行session23041は**exit0 / BUILD SUCCESSFUL in 59m23s**、11 actionable tasks（3 executed/8 up-to-date）。Minecraftウィンドウ消失を確認。強制終了なし。[通常終了後の保存値](../build/verification/true-root-realclient-20260912-221016/audit/after-normal-shutdown-snapshot.json)はgame7867/active0/reserved0/HP18/SP0/spent29/Root5・True1有効/EatCount4。予約を残すlogout/restart境界は試験していない。
- **保全・変更**: 開始前Status全文を同root `before/CODEX_STATUS.md` に保全。`post-client-manifest.json` は開始manifest109ファイルすべてsize/hash一致（Status更新前）を確認。Jarは[現物測定](../build/verification/true-root-realclient-20260912-221016/audit/final-jar.json)で196,761 bytes/123 entries/SHA-256 `15BE0512CF2602FC399F518BFA919460F26BB4F74EF8DDF4DC5AC589C44300FB` のまま。production/test/Jar修正なし、build/check/GameTest/別JVM試験再実行なし。変更はこのStatusと新規専用rootの準備・証拠・通常ゲーム生成物。原本/過去worldは不使用。Git diffや全PC/cache総走査を行ったとはしない。
- **ログの限界・gate**: runClientログにERROR/FATALレベル記録は見つからなかったが、初期config補完、language Jar mods.toml/refmap/asset、test-food model、shader警告、Netty DEBUG例外、Realms SignedJWT INFO等は保持。全警告解消/外部通信0とは判定しない。49/49・別suite13・別JVM成功は前の実装時の結果を維持。未決定の部分蓄積/親Root OFF/lifecycle、OPEN-02/03/04、実2-client BLOCKED、Flight/Break Realm/Bulwark/試作型機関弩/FOURTH BOOTのgate、全TaCZ/全gunpack未検証、可逆クラフトの許容仕様、**RC=NO**を維持する。

<a id="evidence-true-root-implementation"></a>

### 2026-09-12 True Root完成済み予約保持・再ON実装 / AUTOMATED TESTED

- **承認・基準**: 利用者はproduction/test変更、新規隔離GameTest/検証server、build/unit/check、TaCZ関連回帰、必要な別JVM保持、Jar再生成、Status更新を一括承認。20:44整理済みStatusと旧Jar `AB9B75DC97C095774B68A416D66A23E5795B635CC4473CF1E46D2383A946BB36` / 196,263 bytes / 123 entriesを開始backupへ保存。過去の文書のみ・完全停止・同一PID指示を持ち越さず、原本/過去worldは開かなかった。
- **実装**: RootControllerは取得済みTrue Root OFFの完成済み18だけを期限終了後も保持。GUI ToggleSkillPacketと既存commandは共通のserver操作処理を使用し、実際のOFF→ON・親有効・期限終了・完成済みを検証して、予約消費→既定時間発動の順で処理する。現在の期限をリセット/延長せず、同一ON、消費後の再切替、次tickによる二重発動を防止。同期は既存canonical NBT経路。部分蓄積・親OFF・lifecycleの新仕様、保存schema、新packet、依存、GUI、製品Config、migration変更なし。
- **変更対象**: production `RootController.java` / `network/ToggleSkillPacket.java` / `command/FoodHealingCommands.java`、新規 `FoodHealingTrueRootGameTests.java`、SPEC/SKILL_TREE_SPEC/TEST_PLAN/CODEX_STATUS、再生成Jar。専用証拠root内のbackup/helper/log/manifestと、新規試験rootの生成物も保存。その他ソース・既存テストは変更なし。
- **新規8ケース**: OFF保持と保護終了、実packet再ON/同期/他player不変、command整合/SP不変、残り100tickのactive期限不変と正当な再予約、tick/ON両順序、未取得/親無効/予約なし/同一ON/他skill、通常Root各Lv時間/cooldown/固定窓、受動NBT/copy/decodeの非発動。別モデルではなくproduction処理とForge登録packet/commandを通す。時間準備はfixtureの引数/期限だけで、world時計は変更していない。

| 実行 | 結果・証拠（専用証拠root内） |
|---|---|
| wrapper初回baseline | `baseline-build.log`: wrapperが実行環境のGradle配置を見つけられずdownloadを試行し、Permission deniedで起動前失敗。distribution取得は成功していない。未修正のゲーム試験FAILではない |
| ローカルbaseline build | `baseline-local-build.log`: 確認済みGradle8.1.1/Adoptium17.0.7/既存cacheを明示して--offline、21:03成功。foodHealingUnitTestも実行。UP-TO-DATEのcompile/標準testを新規実行として数えない |
| RED（production未修正） | `red-gametest.log`: **45/49、必要4件FAIL**。OFF期限終了の予約消去、packet/command再ON、境界順序の不足を検出。既存41件は削除せず、失敗logを保持 |
| 修正後green | `green-vanilla.log`: **49/49 PASS**、build/unit/check成功。production修正後、期待値/必要件数/Mixin必須条件を弱めず成功 |
| 最終vanilla | `final-vanilla.log`: **49/49 PASS**（21:11:40）。同tickでも期限リセットを検出できる残時間fixtureと、既にONの要求の非発動を補強して再確認。build/unit/check成功、exit0 |
| 最終TaCZ + write | `final-tacz-write.log`: TaCZ1.1.7-hotfix2 GameTest **49/49 PASS**（21:12:46）。別suite既存damage13ケース、heat A〜M、ammo/bolt/burst/reload/replay、respawn、write成功（21:13:08）、exit0。13ケース等をGameTest49へ合算しない |
| 別JVM read | `final-tacz-read.log`: 既存damage13ケース・Ammo関連・respawn/read成功（21:14:31）、exit0。write PID30360とread PID22520は別で、phase/token/statusを含む新規成功receiptを確認。両serverは通常保存・終了済み |

- **実行コマンド**: 既存ローカル `<LOCAL_GRADLE_HOME>\wrapper\dists\gradle-8.1.1-bin\9wiye5v2saajue4irfo8ybqfp\gradle-8.1.1\bin\gradle.bat -g <LOCAL_GRADLE_HOME>` に共通で `--offline --console=plain`。最終vanillaは `build foodHealingUnitTest check runGameTestServer -PfoodHealingGameTestRun=true-root-final-20260912-210154`。TaCZ/writeは `build foodHealingUnitTest check runGameTestServer runServer -PfoodHealingTaczIntegration=true -PfoodHealingRestartPhase=write -PfoodHealingRestartRun=tacz-true-root-final-20260912-210154 -PfoodHealingRestartEulaAccepted=true`。readは同じTaCZ/run/EULA引数で `runServer -PfoodHealingRestartPhase=read`。既存EULA条件とloopback/ephemeral portを維持。
- **隔離root**: `build/verification/gametest-true-root-{red,green,final}-20260912-210154/` と `build/verification/restart-tacz-true-root-final-20260912-210154/`（内側gametestを含む）だけを新規生成して使用。readは今回writeの続きであり、以前の検証worldを再利用していない。実client/Prism/R0908/V3M0908/pristine/cold copy/FOURTH BOOT起動なし。
- **環境上の失敗・通信**: wrapper失敗は確認済みローカルGradleを直接指定して解消した。Gradle/MOD/依存の新規download成功はないが、Forge自動version checkのHTTP応答が初期試験logに存在し、「外部通信0」とは記録しない。read前に今回専用rootの既存 `config/fml.toml` の `versionCheck` だけをtrue→falseに変更し、変更前を証拠へ保存。readの更新照会は0件。製品Config/OS/Firewall/認証設定は変更していない。
- **log/WARN**: 最終vanilla/TaCZ-write/TaCZ-readのcommand logはERROR/FATAL各0、WARNは順に38/166/7。fresh Forge/TaCZ Config default生成、language-provider metadata、dev refmap/union assets等を保持。Netty任意reflection/環境fallback、Mixin出力directory等のDEBUG記録とGradle非推奨通知があるため、全warning/exception 0とはしない。REDの4ERRORとwrapper失敗も消していない。中間補助集計のPowerShell構文エラーは修正して再集計し、ゲームの不具合やPASSへ数えていない。
- **最終Jar**: `build/libs/foodhealing-3.0.0.jar` / **SHA-256 `15BE0512CF2602FC399F518BFA919460F26BB4F74EF8DDF4DC5AC589C44300FB` / 196,761 bytes / 123 entries**。metadata FHR3.0.0/MOD ID foodhealing、required Mixin/defaultRequire1/refmapを確認。GameTest/test-only class/fixture/world/NBT/外部MOD Jar/ExampleMod混入なし。Jar内のRootController新処理とpacket/command双方からの呼出しをjavapで確認。実client試験済みJarとはしない。
- **レビュー/補助ツール**: 通常コードレビューでserver権威・消費順・同期・他skill/TaCZ副作用・未決定経路不変更を確認。Context7は公開APIの追加調査が不要で未使用。Codex Security diffスキル/実tool schema/差分列挙scriptはGit baselineを要求し、本領域に.gitがないためscan未実行。scanのためのrepo作成・外部送信/接続・権限変更は行わず、通常レビューをSecurity scan完了へ置き換えていない。
- **判定・残gate**: 限定仕様LOCK / IMPLEMENTED / AUTOMATED TESTED、**新仕様実client = NOT RUN**。部分蓄積/親OFF/lifecycleは未決定、今回の確定範囲には追加質問不要。OPEN-02/03/04、REAL 2-CLIENT BLOCKED、Flight購入停止、Break Realm expansion、試作型機関弩完全一致開始条件、Bulwark、全TaCZ/全gunpack・通信遅延未検証、FOURTH BOOT NOT AUTHORIZED、RELEASE CANDIDATE = NOを維持。食料生産の可逆craft許容仕様を封鎖せず、死亡drop等の既存回帰も保持。
- **証拠**: `build/verification/true-root-retain-20260912-210154/` のbefore/after backup・manifest、実行log、Jar entry/bytecode、通常コードレビュー、最終確認。原本world/Prism/cacheの大規模保全走査は追加せず、今回の対象と使用artifact/出力に限定した。

### 2026-09-12 True Root RETAIN決定記録・Status再構成（文書のみ）

- 利用者が完成済み1回分のTrue Root自身ON→OFFをRETAIN / LOCKEDと決定。追加質問への回答で、元window終了後は親Root有効時の再ONで保持予約を1回だけ消費して発動し、消費後の再切替では再発動せず、発動中期限もリセット/延長しないと決定した。SPEC section17を正本に、SKILL_TREE_SPEC/TEST_PLANは参照と試験範囲を更新。RootController・toggle・tick・同期・保存の読取で期限終了時の無条件予約消去と再ON発動経路の不足を特定した。実装変更・動作適合PASSはない。
- 現在の一覧を一本化し、旧「第三boot未実施」は9/8第三boot完了記録、旧「新Adapter実client全体NOT RUN」は9/9 22:04終了記録、旧TaCZ38件の集計は9/9最終41/41、旧実1-client途中状態は9/5 STEP1〜6終了記録に基づいて整理した。過去本文のNOT RUNを当時のPASSに書き換えていない。
- OPEN-01/05の既存LOCKと今回の限定RETAIN・期限終了後再ONを判断済みへ分離し、OPEN-02/03/04とTrue Rootの他の対象外境界は未決定。食料生産の可逆クラフト許容仕様・全hard gateを維持。
- 証拠root: `build/verification/status-retain-20260912-203117/`。変更前4文書のbackup、before/after manifest、履歴再配置の可逆な変換記録、静的文書確認を保存。既存履歴本文は見出し階層/履歴ラベル/参照anchor以外を保持する。production/test/Jar/Config/依存/world変更なし。build/test・ゲーム起動・download・Context7照会・Security scanは今回実行しない。

### 旧「現在の状態」集計（2026-09-12 20:07時点の履歴）

> 履歴の集計です。現在の判定・未完了・次の作業は[現在の要約](#現在の要約)を参照してください。

- 9/12 開発再開・正本再同期: **READ-ONLY CHECKPOINT REVIEW + FOOD PRODUCTION LOCK DOCUMENTATION / COMPLETE**。AGENTS/CODEX_LOOP_PROMPT/SPEC/SKILL_TREE_SPEC/COMPATIBILITY_POLICY/TEST_PLANと本Statusの冒頭・最新終了checkpoint・残gateを照合。9/9 22:04の実client正常保存・終了・最終監査を現在地として継承し、古いAmmo未実装/実client NOT RUN/第三boot未実施記録へ巻き戻していない。MC1.20.1/Forge47.2.0系/FHR3.0.0/MOD ID foodhealing/scope frozen、protocol6を維持。local配布JarはSHA-256 `AB9B75DC97C095774B68A416D66A23E5795B635CC4473CF1E46D2383A946BB36` / 196,263 bytes / 123 entriesと一致。`.git`なしを再確認し、Git履歴・branch・diffによる判断はしていない。
- **新規LOCK / KNOWN AND ALLOWED BEHAVIOR (2026-09-12利用者決定)**: 「食料生産の極意による可逆クラフト経由の増加は既知かつ許容された仕様。バグ修正対象外」。SPEC section16を正本とし、SKILL_TREE_SPEC section3とTEST_PLAN section9にも境界を追記した。乾燥した昆布等の既存適格出力に通常倍率が適用される圧縮/展開cycleの増加を封鎖しない。逆レシピblacklist、増加防止provenance、reverseだけ倍率無効、恣意的食材除外、相殺consume/refundは追加しない。食料適格性・4SP・x2・1craftの材料消費は変更なし。死亡drop、inventory desync、packet replay、ammo/reload、duplicate event、同一craft resultの意図しない二重付与、無関係な説明不能増殖は引き続きBUG。
- **限定ソース確認 / STATIC REVIEW ONLY**: FoodProductionTransactions/CraftingMenuMixin/ResultSlotMixinと既存Food Production GameTest、LootEventHandler/GatheringLootModifierとPlayer death drop回帰を確認した。確認したクラフト経路に可逆レシピ由来の抑止分岐はなく、Player死亡drop除外も存在する。新LOCKのためのproduction修正は不要と判断した。これは実クラフト・packet・grave MODの新規動作PASSではなく、既存fixtureや全経路の再検証でもない。

#### 2026-09-12 残release blockerの分類（旧履歴の未完了表示より優先）

| 分類 | 現在の扱い |
|---|---|
| A. 本当に残るrelease条件 | 実2-client、未完成Flight ownership、Purification/Truthの未確定前提と未実装外部Adapter、未検証の外部MOD機能/所有権/重複処理、必要な実client・dedicated通信検証。既知のsource-modset ERROR影響や全MOD gameplay、長時間/多数player性能も未検証で、RELEASE CANDIDATE = NO。 |
| B. 完了・理由なく再試験しない | 最新build/foodHealingUnitTest/check、vanilla41/41・TaCZ41/41、TaCZ既存13ケースとAmmo/heat/bolt/reload/別JVMの限定自動PASS。OPEN-01/05実装とmigration/別JVM、実v3初回・第二boot冪等性・第三boot限定smoke、実1-client dedicated STEP1〜6、BUG-01〜05、GUI/HUD、Root/Heroics、vanilla craftingの既報手動PASSを元の範囲で維持。 |
| C. 判断・artifact・account・開始gate待ち | OPEN-02/03/04、True Root予約作成後OFF時discard/retain、Flight購入gate。AttributeFix/L2系/Auto Leveling/Traveler's BackpackはTARGET MOD ARTIFACT REQUIRED。FD1.20.1-1.2.10とSophisticated Backpacks3.23.14.1233/Core1.2.49.962はlocal Jar確認済みだが正式機能統合は未実施で、不存在扱いにしない。実2-clientはSECOND MINECRAFT ACCOUNT REQUIRED。Break Realm expansionはSPECIFIED / NOT IMPLEMENTED、Bulwarkは着手しない。試作型機関弩は完全一致の利用者開始指示まで監査/API調査/設計/test/実装すべて禁止。 |
| D. 今回安全に自動で進めたもの | 新規LOCKの正本・試験判定整合と、限定ソース・文書差分・hash/size/mtimeの静的確認のみ。今回の最新Status再分類では、追加判断不要で必要性まで確認できるproduction修正や、既存自動PASSの再実行だけで解除できるgateは特定していない。既存test-food model欠落WARNも外観やregistry変更を推測して修正しない。 |
| E. 人力・利用者判断・実行承認が必要 | 上記OPEN/Flightの方式決定、TaCZ Bの購入時heat解除・Cの厳密physical shot計数・Eの全bolt/packet/prediction内部遷移・hot-gun active持替え、配布Jar直接Prism/遅延通信等の未確認範囲。人力・world試験は事前承認が必要。原本/ZIP/pristine/R0908/V3M0908/cold copy/過去TaCZ worldを再利用しない。V3M0908 FOURTH BOOT = NOT RUN / NOT AUTHORIZED。 |

- **Ammo実client最終判定を維持**: A/F/GとHのHUMAN操作、通常GUI/click/reload等のCOMPUTER USEを分離。Hの自然過熱360/lock1をGUI pauseで保持→OFF→ON→world復帰/再射撃のheat0/lock0・弾薬保持は画面/保存で観測した限定PASS。B購入時解除はINCONCLUSIVE / NATURAL COOLDOWN COMPLETED FIRST、C/Eの個別未観測は未観測、hot-gun持替えはSKIPPEDのまま。REAL CLIENT CORE全条件PASSを付与しない。9/9 sessionは終了済み。ALL TACZ/ALL GUNPACK COMPATIBILITYとDEDICATED NETWORK LATENCYはNOT TESTED、REAL 2-CLIENTはBLOCKED。
- **今回の検証・変更範囲**: 変更はSPEC.md/SKILL_TREE_SPEC.md/TEST_PLAN.md/CODEX_STATUS.mdと今回専用証拠rootのみ。3仕様・試験文書は追記前本文の完全保持、UTF-8/LF維持と追加部分の整形を静的確認。production・テストソース103 files、既存build設定・承認Jar等は変更していない。文書だけの更新でゲーム動作変更がないため、build/unit/check/GameTest/別JVM/実client/dedicatedは再実行していない。Minecraft/Prism/world起動、外部MOD download、NBT修復、UUID操作なし。既存自動PASSを今回の新規PASSに置き換えず、可逆cycleの新規実行PASSも付けない。migration/compatibilityの動作変更はない。
- **補助ツール**: 今回は公開API/signature/lifecycleの調査や対象production変更を行っていないため、Context7追加照会・Codex Security scanは未実施。今後はユーザーLOCK→最新正本→承認実artifact→version対応Context7→Codex Security静的監査→一般知識の順を維持。Securityは大きな変更、packet/sync、NBT/capability/migration、adapter、Mixin/reflection/hook変更後およびRC判定前の補助とし、各runtime testの代替にしない。指摘はfinding/severity/該当箇所/成立性/false positive/LOCKとの衝突/修正影響を整理して利用者承認を待ち、自動修正・既存gate解除をしない。
- **証拠・途中確認失敗**: `build/verification/resume-lock-20260912-200327/`に4文書のbefore copy、123 filesのbefore manifest、document-review等を保存。初回manifest生成は存在しないUnix `gradlew`を列挙したため停止し、実在するgradlew.batを含む集合へ訂正した（文書編集前）。初回文書整形検査は既存Markdown hard-break空白を対象にしたため停止し、既存本文を保持したまま追加部分のみ検査するreaderへ訂正してPASS。両履歴はpreparation-history/document-review-first-failedに保持し、ゲーム/build/test FAILやproduction修正とは扱わない。hash保全の主張は今回manifest対象に限定し、全外部worldの追加総走査PASSは主張しない。
- **次の作業 / 停止境界**: 今回の新規LOCK記録は完了。次に扱う未確定仕様または実行承認が必要な検証項目について利用者指示を待つ。OPENを確定せず、Flight購入解放・Break Realm expansion・Bulwark・試作型機関弩・追加world試験へ進まない。以下の9/9以前の時系列記録と失敗履歴は保持する。

<a id="evidence-ammo-client-final"></a>

- 9/9 Ammo Conservation実client終了: **NORMAL SAVE + CLIENT SHUTDOWN + FINAL STATE/LOG/PRESERVATION REVIEW / PASS**。TEST H後、当初runbookどおり通常Save & Quit→main menu→Quit Gameを実行。21:57:03.077にSaving players、21:57:03.822にAll dimensions are saved、21:57:22.144にMinecraft `Stopping!`を確認。今回のMinecraft/runClient Java PID33680/24300は終了、runClientは21:57:27に`BUILD SUCCESSFUL in 1h 37m 45s`、Gradle daemonのSuccess返却も確認。session28263は完了後に再poll不可となったため、daemon logの該当時刻を終了証拠として保存した。**Minecraft Java残留0 / javaw0、java全体は既存Gradle daemon PID25520（19:04:57開始）1件のみ**であり、java全体0とは記録しない。強制kill・Prism操作・再起動・追加world試験なし。
- 最終保存照合: `audit/final-normal-shutdown-snapshot.json`と`final-review.json`（22:00/22:02）で、H事後からInventory・型付きForgeCaps・Health20・food20・selected slot0・UUIDは不変、level.dat内Playerと唯一のplayerdataも一致。Ammo ConservationのみLv10/ON、未使用SP0/使用済50、他skill/baseなし、schema4/pending0/Root runtime0/Diversity空・bonus0。Minigun heat0/lock0・308予備59（11+48）、Glock mag17/chamber1・9mm41、M700 mag4/chamber1・30_06予備31を維持。他銃の変更や終了時弾薬増減なし、銃ammo/heatをFood Healing capabilityへ保存していない。最終latest367行/debug1559行、H事後からの14/58行は通常切断・保存・registry凍結復帰・config unload・Stoppingのみ。**ERROR/FATAL0、新規Food Healing/TaCZ/network/packet/capability/Mixin例外0、crash report0**。既存dev WARN124、test-food model WARN、Netty optional reflection DEBUG例外、dev Realms認証INFOは消去/無害認定せず履歴を維持。最終監査helperの初回UTF-8読取はGradle集約logの非UTF-8 bytesで失敗し、ASCII終了行だけのbyte抽出へ修正してexit0（ゲームのFAILやコード修正ではない）。
- 終了後保全: `audit/preserve.py after-normal-shutdown`は22:03にexit0。`after-normal-shutdown-*`の集合/SHA/size/mtime照合で、元instance/world/ZIP/pristine/source runtime/source104 Jar/shared metadata・cache・libraries・assets、2.2.5比較instance/R0908、v3 migration/third-save instance/cold copy、昨日TaCZ client、本体src・通常run・build設定・承認FHR/TaCZ Jarに開始baselineから追加差分0。変更は本Status、専用auditの読取helper/証拠、Minecraft自身の今回専用保存/log/F2等に限定。本体Java/Mixin/resource/Config/仕様/build/test変更なし、build/unit/check/GameTest再実行なし、既存自動PASSを維持。今回の`runClient`成功を自動回帰の再実行PASSへは置き換えない。**今回承認された実client sessionを終了して完全停止。REAL CLIENT CORE総合PASSは未判定範囲があるため付与しない**。B購入時過熱解除INCONCLUSIVE、Cの物理shot厳密計数・Eの全bolt/packet/prediction内部遷移の未観測、任意hot-gun持替えSKIPPEDを維持。Hは下記の画面/保存で確認した範囲のPASSのみ。ALL TACZ/GUNPACK COMPATIBILITY = NOT TESTED / DEDICATED NETWORK LATENCY = NOT TESTED / REAL 2-CLIENT = BLOCKED / RELEASE CANDIDATE = NO。次回は本記録を確認し利用者の次の指示を待つ。追加実装・再試験は開始しない。

<a id="evidence-ammo-h"></a>

- 9/9 TEST H: **OFF→ON TRANSITION + POST-TRANSITION MINIGUN FIRE / HUMAN PRECONDITION + COMPUTER USE / PASS (観測範囲限定)**。利用者はLv10 OFFで自然過熱/射撃停止後、release→直後E→S（途中F2/Escなし）。21:48:30のGUIによる通常pause保存を確認し、21:50:13 `test-h-human-direct-gui-before-toggle-snapshot.json`と21:53:42 `test-h-at-off-button-before-toggle-snapshot.json`はMinigun HeatAmount360/OverHeated1、308弾239→59（11+48）、Lv10/OFF/SP0/Spent50。他銃/他進行不変。GUIを一度も閉じず表示scrollのみでOFFボタンへ移動、F2 `21.53.41.png`後に通常左click1回でON、GUIでON同期とF2 `21.54.19.png`を確認した。通常GUIのpauseを継続したままON操作へ到達しており、Bの「GUI入場前に自然冷却完了」とは区別する。
- H事後: GUI→Inventory→world復帰時にOVERHEATなし/heat0.0%/残弾059。通常左click1回で排莢と反動、heat0.0%/残弾059不変を画面観測、F2 `21.55.13.png`、21:55:25通常pause保存。21:55:26 `test-h-on-after-one-input-snapshot.json`はMinigun HeatAmount0/OverHeated0、308弾59不変、DisabledSkills空（ON）、Lv10/SP0/Spent50維持、他2銃tagと予備弾（Glock17/chamber1・9mm41、M7004/chamber1・30_06予備31）、他progression不変。latest346→353/debug1490→1501行の追加はF2/通常pause保存/ID mapのみ、新規ERROR/FATAL/network/packet/capability/Mixin/critical exceptionなし、crash-reportsなし。**通常GUI切替前の過熱保存・GUI ON同期・復帰後の解除/再射撃/保存整合を確認した限定PASS**。銃のlive heapやtoggle処理瞬間のheat値、ms単位の解除latency、正常化APIの正確な呼出回数は今回直接計測していない。既存one-shot自動fixture PASSと実画面/保存の確認を分離する。
- 今回実clientフェーズ終盤: A/F/Gの長押しとH事前自然過熱はHUMAN、それ以外のGUI購入/toggle・通常click/reload・H切替後再射撃はCOMPUTER USE。B購入時heat解除はINCONCLUSIVEを維持、Cの5 input=5物理shot厳密計数、Eの全bolt/packet/prediction内部遷移は個別未観測のままで、**REAL CLIENT CORE全条件PASSへは拡張しない**。任意のheat済み別銃active持替えは **SKIPPED / NOT RELIABLY REPRODUCIBLE IN THIS REAL-CLIENT SESSION**（別銃の自然過熱作成と冷却前切替が追加で必要、直接heat生成禁止を維持）。新たな射撃/追加human試験やコード修正は行わず、親runbookの正常Save & Quit→Minecraft終了→最終保存/log/保全監査へ進む。終了処理はこの時点では未完了。今回本体Java/Mixin/resource/Config/仕様/build/test変更なし、build/unit/check/GameTest再実行なし。ALL TACZ/GUNPACK COMPATIBILITY = NOT TESTED / DEDICATED NETWORK LATENCY = NOT TESTED / REAL 2-CLIENT = BLOCKED / RELEASE CANDIDATE = NO。

- 9/9 TEST H handoff前保全: 21:44に`audit/preserve.py before-test-h`がexit0で完了。`before-test-h-*`manifestで原本instance/world/ZIP/pristine/source runtime/source104 Jar/shared metadata・cache・libraries・assets、2.2.5比較instance/R0908、v3 migration/third-save instance、cold copy、昨日TaCZ client、本体src・通常run・build設定・承認FHR/TaCZ Jarに開始baselineから追加差分0。人間へ下記H用の再過熱→直後GUI entryだけを依頼し、同じMinecraft process/world/pauseを維持。今回の追加射撃/toggleはまだ0、H解除・全core判定は保留。ALL TACZ/GUNPACK COMPATIBILITY = NOT TESTED / DEDICATED NETWORK LATENCY = NOT TESTED / REAL 2-CLIENT = BLOCKED / RELEASE CANDIDATE = NO。

- 9/9 TEST H入力前: **PREPARED / WAITING FOR HUMAN FRESH OVERHEAT + DIRECT GUI ENTRY / TRANSITION NOT YET TESTED**。同process/sessionでpauseを解除すると、OFFのままGのOVERHEATが通常冷却で消えたことを画面確認（ON操作なし。Hの解除PASSではない）。21:39:42/21:40:05に既存通常`/give @s tacz:ammo{AmmoId:"tacz:308"} 100`を各1回だけ実行し、308弾39→239。F2 `21.40.11.png`と21:40:24の`audit/test-h-refilled-before-human-snapshot.json`はHUD239/heat0、Minigun HeatAmount0/OverHeated0、Ammo Conservation Lv10/OFF/SP0/Spent50、他2銃・予備弾・他進行はG後と同じ。latest342/debug1484行でERROR/FATAL・critical exception0、crash-reportsなし。Java群24300/25520/33680・runClient session28263を維持、21:40:23の通常pause保存で待機。次は人間が**Esc復帰→Minigunを通常長押し→OVERHEAT/射撃停止でrelease→間にF2/Escを挟まず直ちにE→S→Food Healing GUIを開いたまま操作停止**。まだtoggle/購入はしない。`FoodHealingScreen extends Screen`にisPauseScreen overrideはなく、対象Forge47.2.0 mapped sourceのScreen.isPauseScreenはtrue。Eの通常inventoryはpauseしないので、E→Sを続けて行う。GUI到達の実log/通常保存でheat>0/lock1が残ることを確認し、残っていなければINCONCLUSIVEを維持して無理に解除PASSへ進めない。この再過熱はG再判定ではなくH transitionの事前条件作成。mouse holdと短い画面遷移が必要なため人間へこの準備操作だけを依頼し、直接API/NBT/clock変更は行わない。

- 9/9 TEST G: **LV10 OFF / NATIVE MINIGUN HEAT + AMMO CONSUMPTION RETURN / HUMAN / MANUAL OPERATED / PASS (今回条件限定)**。利用者は同process/worldで通常左clickを長押しし、OVERHEATと射撃停止を確認、release→F2→Esc。F2 `2026-09-09_21.31.42.png`に赤いOVERHEAT/残弾039、21:31:44の通常pause保存、21:33:19の`audit/test-g-minigun-off-after-human-snapshot.json`にMinigun `HeatAmount=360.0 / OverHeated=1`を確認。開始前heat0/lock0から実射で生成され、308弾219→39（12+27）、180発分消費。Ammo Conservation Lv10/OFF・DisabledSkillsは同skillのみ、SP0/Spent50、Glock17/chamber1・9mm41、M7004/chamber1・30_06予備31、他skill/base/Root/Diversity/移行pending空/0は維持。latest328→333行/debug1464→1473行の差分はF2/通常pause・保存/ID map DEBUGのみ、新規ERROR/FATAL・network/packet/capability/Mixin例外なし。これは当該default MinigunのOFF時通常heat/消費復帰だけのPASSであり、ALL TACZ/GUNPACKやTEST Hへ拡張しない。
- 9/9 TEST H準備方針: **NOT YET TESTED / SAME SESSION RETAINED**。G終了時の過熱証拠は保存済み。実TaCZ bytecodeの`ModernKineticGunItem.tickLocked`は`System.currentTimeMillis - heatTimestamp`を冷却に使い、default Minigunはmax360/cooling_multiplier5/over_heat_time3000ms。長いEsc pauseは再開後の冷却時間を巻き戻さないため、現在の保存heat360だけで「ON直前まで過熱が残る」と推定しない。通常GUIは20段階のscrollを要し、Bと同じ自然冷却先行を避けるため、同sessionの通常giveで必要弾を補充し、**H用に自然再過熱→直後E→SでFood Healing GUIへ入り、購入/toggleせず待機**する短い人間操作を準備する。GUI到達時の通常pause保存にheat/lockが残っているか改めて照合後にのみON操作へ進む。直接heat/NBT/API変更、時間/Config/GUI実装変更なし。まだH解除PASS・core総合PASSではない。変更は本Status/専用audit証拠とゲーム自身の専用保存/F2のみ、本体コード/仕様/Config・build/unit/check/GameTest変更/再実行なし。

- 9/9 TEST G準備: **LV10 OFF / ZERO HEAT + UNLOCKED BASELINE / COMPUTER USE CONFIRMED / WAITING FOR HUMAN**。TEST F照合後、同じMinigunを装備したままE→S通常GUIのAmmo Conservation ONボタンを1回押してOFF。F2 `21.22.53.png`にOwned Lv10/10・OFF・SP0/Spent50、world画面に残弾219・heat0.0%を確認してEsc pause。21:23:21の`audit/test-g-minigun-off-before-human-snapshot.json`はDisabledSkillsに`foodhealing:tacz_ammo_conservation`のみ、Minigun HeatAmount0/OverHeated0、308弾219、他2銃/予備弾/進行値はTEST F後と同じ。過去heatの復元なしという開始状態だけを確認し、**OFFでの新規heat上昇/OVERHEAT/射撃lock/弾消費はまだ未試験**。latest328行/debug1464行でERROR/FATAL・critical exception0。同じMinecraft process/world/session28263のMinigun装備pauseを維持し、次は人間がEsc復帰→左click長押し→OVERHEAT/射撃停止確認→release→F2→Escを行う。G結果受領前にHへ進めない。本体Java/Mixin/Config/resource/仕様/build/test変更なし、build/unit/check/GameTest再実行なし。変更は本Statusと専用audit snapshot、ゲーム自身の専用world保存/F2のみ。ALL TACZ/GUNPACK COMPATIBILITY = NOT TESTED / DEDICATED NETWORK LATENCY = NOT TESTED / REAL 2-CLIENT = BLOCKED / RELEASE CANDIDATE = NO。

- 9/9 TEST F: **MINIGUN LV10 ON / 100% AMMO CONSERVATION + HEAT IMMUNITY / HUMAN / MANUAL OPERATED / PASS (今回条件限定)**。利用者が同じprocess/worldで通常左clickを数秒長押しし、連射継続・308弾非消費・heat不増加・OVERHEAT/過熱停止なしを報告。F2 `2026-09-09_21.15.26.png`で弾道/排莢表示、HUD219、heat0.0%、HP20/20、21:15:28のpause保存を確認。21:18:06の`audit/test-f-minigun-on-after-human-snapshot.json`は開始前21:09:43の記録対象player値と完全一致: 308弾48/48/48/48/27=219、Minigun HeatAmount0/OverHeated0、Glock17/chamber1・9mm41、M7004/chamber1・30_06予備31、Ammo ConservationのみLv10/ON/SP0/Spent50、他skill/base/Root/Diversity/移行pendingは空/0。latest306→319行、debug1432→1451行の差分はF2/pause/正常保存INFO・DEBUGのみ、新規ERROR/FATAL/network/packet/capability/Mixin例外0、crash-reportsなし。同じJava群24300/25520/33680を維持。最初の追加log readerはFile.ReadAllLinesの共有modeで使用中fileを読めず失敗したが、読み取り専用Get-Content(932)で差分を取得できた（ゲーム不具合/修正ではない）。**Lv10での同時成立に限定し、全Lv個別heat試験・ALL TACZ/GUNPACK・dedicated latency・core総合PASSへ拡張しない**。Bの購入時解除INCONCLUSIVE、C/Eの個別未観測範囲は維持。次は同sessionで通常GUI OFF→G事前snapshot→人間の通常過熱試験。本体コード/仕様/Config変更・自動test再実行なし。

- 9/9 TEST F handoff前の保全: `audit/preserve.py before-test-f`はexit0。`before-test-f-*`証拠で元instance13,351 files、shared metadata31/cache4,384/libraries453/assets12,049、元world/ZIP/pristine/source runtime16,750/source104 Jar、2.2.5 control4,564 files（R0908含む）、v3 migration/third-save instance/cold copy/昨日TaCZ client、本体src・通常run・build設定・承認Jarに開始baselineから追加差分0。現在log全体の追加照合でも、既記録のdev model/reflection/Realms/初回Config WARN/DEBUG等以外に新しいERROR/FATAL・network/packet/capability/Mixin例外なし。TEST F入力前の同じMinigun装備pauseを維持して、人間へEscで復帰→数秒だけ通常連射→release→F2→Escの1試験操作だけを依頼する。**今回はOVERHEATを待つ試験ではなく、Lv10 ON時に残弾219とheat0相当を保つかを見る。F/G/H・最終保存終了・core総合判定は保留**。

- 9/9 TEST E: **M700 ON / TWO NORMAL INPUTS + HUD/SAVED AMMO RETENTION / COMPUTER USE / PASS (観測範囲限定)**。承認default packで30_06弾stack36・manual_action・弾倉5・native bolt 0.85sを再確認。通常give36発、通常R reload後の`test-e-m700-before-shot-snapshot.json`はmagazine4/chamber1/予備31。通常左clickを間隔を空けて2回入力し、それぞれ地面の着弾表示、2回目で銃の姿勢変化とその後の通常姿勢復帰、HUD005/0031不変を確認した。F2 `21.08.00.png`、21:08:14の`test-e-m700-after-two-inputs-snapshot.json`もmagazine4/chamber1/予備31。他2銃/他進行は不変、Ammo ConservationのみLv10/ON/SP0/Spent50、latest296行/debug1420行にERROR/FATAL・critical exception0。**ボルトの全animation frame、物理projectileの厳密な重複なし計数、TaczBoltStatePacketの各encode/decode/slot/ID/pending遷移は通常logとpause保存だけでは個別観測できていない**。実入力後の継続射撃・表示/保存整合の観測と、既存自動packet/bolt PASSを分離し、この段階ではcore全体PASSにしない。専用boltキー・reloadによる途中救済・直接API/NBT変更なし。
- 9/9 TEST F準備: **MINIGUN LV10 ON / WAITING FOR HUMAN HELD INPUT**。同process/sessionで308弾を通常give100発x2追加し、slot0 Minigunを通常装備してpause。最初のgive200はゲームの`Can't give more than 100`で拒否された履歴を残す（Food Healing FAILではない）。21:09:43の`test-f-minigun-on-before-human-snapshot.json`で308弾48/48/48/48/27=219、HUD219、HeatAmount0/OverHeated0、他2銃/9mm41/30_06予備31・全Food Healing進行不変を確認。latest306行/debug1432行にERROR/FATAL・critical exception0。**Fの連射・GのOFF過熱・HのOFF→ON解除はまだ未実施**。Computer Useにはmouse hold/release APIがないため、同じpause状態から人間の数秒連射→release→F2→Escだけを待つ。変更は本Statusと専用audit証拠/Minecraft自身の専用保存・F2のみ。本体Java/Mixin/resource/Config/仕様/build/dependency/test変更なし、build/unit/check/GameTest再実行なし、既存自動PASSを維持。runClient session28263は意図的に継続し、正常保存終了/最終core判定は後続。ALL TACZ/GUNPACK COMPATIBILITY = NOT TESTED / DEDICATED NETWORK LATENCY = NOT TESTED / REAL 2-CLIENT = BLOCKED / RELEASE CANDIDATE = NO。

- 9/9 TEST D: **GLOCK OFF FIRE + TACTICAL RELOAD / COMPUTER USE / PASS**。通常GUIのLv10 ONボタン1回でOFF、同Glockを通常左click1回。HUD17→16・予備43、20:58:43の`test-d-glock-off-after-one-shot-snapshot.json`はmagazine15/chamber1/予備43、DisabledSkillsにAmmo Conservationのみを確認。通常R reloadのアニメーションと完了後HUD18/41を観測、20:59:30の`test-d-glock-off-after-reload-snapshot.json`はmagazine17/chamber1/予備41。総弾数は給付60から実OFF射撃1発のみ減った59で、reload前後の合計59は不変。他2銃/他進行/SP0・Spent50は不変、latest281行/debug1399行にERROR/FATAL・critical exception0。Cで空銃reload、Dで薬室保持tactical reloadの通常経路を確認した限定結果。E〜H/core判定は未実施、コード/仕様/Config変更なし・自動回帰の再実行なし。

- 9/9 Lv10購入・TEST C途中: **COMPUTER USE / NORMAL LV10 PURCHASE / PASS; GLOCK ON HUD + SAVED AMMO RETENTION / PASS (観測範囲限定)**。Lv1から通常GUIで9回購入し、Lv2〜10で未使用SP40/35/30/25/20/15/10/5/0、使用済10/15/20/25/30/35/40/45/50を各画面確認。最終Lv10/10・ON・購入ボタンなし、他skill未取得。既存giveで9mm弾60発を準備し、Glockの通常R reloadによりHUD17・予備43、20:50:44の`test-c-glock-reloaded-before-shot-snapshot.json`でmagazine16/chamber1/予備43を確認。通常左clickを5回入力した後もHUD17/43、20:54:44の`test-c-glock-after-five-inputs-snapshot.json`も同値で、他銃/Root/Diversity/進行不変、latest267行/debug1377行にERROR/FATAL・critical exception0。画面の銃/視点変化は観測したが、音・全muzzle frame・物理projectile生成数は今回のsnapshot/通常logから厳密には測定できていないため、**5 input = 5 physical shotの計数PASSには拡張しない**。DのOFF通常消費/reload、E〜Hおよび最終core判定はまだ未実施。本体コード/仕様/Config変更・build/unit/check/GameTest再実行なし、同runClient/sessionを維持して続行。

- 9/9 TEST B購入・後続1発: **COMPUTER USE / NORMAL LV1 PURCHASE + POST-PURCHASE FIRE / PASS (限定)**。通常GUIで購入1回、Lv1/10・SP45/Spent5・default ONを確認しF2 `20.43.51.png`。通常左click1回でMinigun排莢/反動・HUD20→19、heat0.0%を観測しF2 `20.44.29.png`。20:44:39の`test-b-lv1-after-shot-snapshot.json`でAmmo Conservation Lv1のみ/DisabledSkills空/未使用45/使用済5、Minigun heat0/lock0、308弾11+8=19、他2銃・他progression不変を照合。latest242行/debug1344行でERROR/FATAL・critical exception0。Lv1は10%節約なのでこの1発の消費は正常であり、節約確率の実測PASSにはしない。**購入による既存過熱解除は下記INCONCLUSIVEのまま**。次は残り9回の通常購入→Lv10実射検証、まだLv10/C〜H/core最終PASSではない。

- 9/9 TEST A: **UNOWNED MINIGUN OVERHEAT / HUMAN MANUAL OPERATED / PASS (限定baseline)**。利用者が通常左click長押しでOVERHEAT/射撃停止を確認し、release→F2→Esc pause。F2 `20.38.50.png`にもOVERHEATと残弾020、20:40:10の読み取りsnapshot `test-a-human-overheat-snapshot.json`にもMinigun `HeatAmount=340.1109924316406 / OverHeated=1`、308弾200→20（12+8）を確認。Glock/M700のtagは不変、skill未取得/SP50/Spent0/HP20/food20維持。latest231行/debug1329行のERROR/FATAL・critical exception追加0。**弾薬節約の効果PASSではなく、未取得時TaCZ通常過熱のbaseline**。
- 9/9 TEST B事前: COMPUTER USEで同processのpause解除→E→S。20:40:38の`test-b-gui-before-purchase-snapshot.json`はまだ未取得/SP50/Spent0のままMinigun `HeatAmount=0 / OverHeated=0`で、GUI移動中に自然冷却が先行したことを確認。**購入transitionの過熱解除 = INCONCLUSIVE / NATURAL COOLDOWN COMPLETED FIRST**。この範囲をFAIL/解除PASSとはせず、通常Lv1購入とその後の実射を続け、後のTEST Hを別判定する。TEST B購入自体はこの時点で未実施、C〜Hも未実施。変更は本Statusと専用auditの新規読み取りsnapshotのみ、本体コード/仕様/Config変更・build/unit/check/GameTest再実行なし。誤読したF2名の初回readはfile-not-foundで止まり、実directory列挙後に上記実F2を確認した（ゲームやデータに変更なし）。

- 9/9 Ammo Conservation実client途中: **NEW ISOLATED CLIENT + UNOWNED GUI READY-GATE / COMPUTER USE OBSERVED / TEST A WAITING FOR HUMAN HELD INPUT / CORE VERDICT PENDING**。20:19:46に1回だけ隔離runClientを起動、main menu→GUIから新規`TaczAmmo_20260909`（superflat/survival/peaceful/cheats ON/構造物OFF）を作成し、20:21:54 Dev login。実logのMC1.20.1/Forge47.2.0/Adoptium Java17.0.7/FHR3.0.0/TaCZ1.1.7-hotfix2とproduction protocol6を照合、4つのFood Healing TaCZ Mixin適用記録を確認。配布Jar直接Prism試験・dedicated試験ではない。**実射0 / 購入0 / toggle操作0**。
- 9/9 準備操作・保存照合: 20:22:21に既存`/foodhealing syokugi setskillpoint 50`を実行し、E→S通常GUIでLv0/count0/200/SP50/Spent0、TaCZ Ammo Conservationの`Not acquired`と有効な`Next Lv +5 SP`を確認。F2 `20.25.40`を保存、購入せず閉じた。20:26:28〜20:28:12に通常giveで空Minigun AUTO（slot0）、空Glock17 SEMI（slot1）、空M700 SEMI（slot2）と308弾100発×2を準備。銃NBT指定は既存GunId/FireModeのみで、ammo/chamber/heat/lock指定なし。308弾はslot3〜7に48/48/48/48/8、合計200でHUD200と一致。9mm/30_06弾はまだ付与していない。F2 `20.28.21`後、20:28:30 `Saving and pausing game...`。`audit/minigun-before-human-snapshot.json`でschema4/未使用50/使用済0/skill・base・toggle空/pending0/Lv0/count0/Root runtime0/Diversity空・bonus0/HP20・food20/selected slot0を確認。全3銃tagはGunId/FireModeのみ、今回のMinigun heatはまだ実生成していない。
- 9/9 途中監査: `audit/preparation-review.json`はdefault pack2,939 filesが承認TaCZ Jarと完全一致・追加pack0、latest226行/debug1322行のERROR0/FATAL0/WARN124、新規NPE/CME/ClassCast/required Mixin/dependency失敗0を記録。dev refmap WARN、初回Config生成、既存test foodモデル2件未登録、vanilla shader/sound、Netty任意reflection fast-path不可DEBUG stack2件、開発session Realms認証不可INFOは別記して維持し、全exception0とは扱わない。監査reader初回はnative `tacz-pre.toml`を想定に含めずassert停止したため、その222bytes/SHA `410BA74DC6CB6C070DB25C8FCFC0657B02A0523A3A25722DA8E6F4310215D133`が昨日の同Configとbyte完全一致することを確認し、**readerのみ**既存生成物を分類して再実行成功。ゲーム不具合の修正やConfig変更ではない。`paused-before-human-*`で元instance/world/ZIP/pristine/R0908/source104 Jar/shared runtime/cache/source2.2.5比較instance/v3第三保存instance/cold copy/昨日TaCZ client/src/通常run/build設定/承認成果物に開始baselineから追加差分0。
- 9/9 再開地点: **TEST A / NOT YET FIRED / WAITING FOR HUMAN**。同じMinecraft window3672352・runClient session28263・Java群24300/25520/33680を維持（25520は試験前からのGradle daemon）。Minecraftはslot0のMinigunを持ったゲームメニューpause。Computer Useには左mouse hold/release APIがないため、代替native injectionやAPI射撃は行わず、人間がEscで戻り、左クリックを長押ししてOVERHEAT/射撃停止を確認したら離し、F2を1回押してEsc pauseする操作だけを依頼する。人間報告後に同じsessionでAの画面/NBT/logを確認し、Bの通常GUI Lv1購入へ進む。購入前に通常冷却が終わればINCONCLUSIVEとして記録し、後のOFF→ON確認を優先。**B〜H・任意持替え・最終保存終了・最終core PASSは未実施**。今回変更は本Statusと専用audit helper4本/生成証拠、Minecraft自身の新規専用world/config/log/F2だけ。本体Java/Mixin/resource/仕様/恒久Config/build/dependency/testは変更なし、build/unit/check/GameTest再実行なし（直前PASS維持）、runClientのcompileJava/processResourcesはUP-TO-DATE。ALL TACZ/GUNPACK COMPATIBILITY = NOT TESTED / DEDICATED NETWORK LATENCY = NOT TESTED / REAL 2-CLIENT = BLOCKED / RELEASE CANDIDATE = NO。V3M0908 FOURTH BOOT・既存world再利用・他作業には進んでいない。証拠root `build/verification/restart-tacz-ammo-realclient-20260909-201123`。

- 9/9 Ammo Conservation実client事前準備: **REAL CLIENT CORE GAMEPLAY / PREFLIGHT PASS / NOT YET LAUNCHED**。最新利用者承認により、TaCZ1.1.7-hotfix2・production source protocol6の新規隔離`runClient + integrated server`だけを開始する。FHR Jar SHA `AB9B75DC97C095774B68A416D66A23E5795B635CC4473CF1E46D2383A946BB36` / 196,263 bytes / 123 entries、TaCZ SHA `FC5F1DAB09AFD5399604DB0F60845D41D46CF015C158CBC12E7EF716CF6BDB46` / 52,426,062 bytesを再確認。MC1.20.1/Forge47.2.0/Adoptium Java17.0.7、既存opt-inと今回限定init scriptでworking directoryを`build/verification/restart-tacz-ammo-realclient-20260909-201123/client`へ限定。`inspectApprovedTaczClient --offline`はexit0/BUILD SUCCESSFUL 2s、server/restart fixture・Jar再buildなし。元instance13,351/shared runtime/cache/source104 Jar/world/ZIP/pristine/R0908/source2.2.5比較instance4,564/v3 migration instance/cold copyの既存baseline追加差分0、昨日TaCZ client・src・通常run・build設定の新規保全manifest保存。新worldはGUIから`TaczAmmo_20260909`を作り、既存commandで50SP、未取得GUI→通常Minigun overheatの順。**配布Jar直接Prism試験とは異なる**。変更は今回auditの`client-isolation.init.gradle`/`preserve.py`/`capture.py`・生成manifestと本Statusのみ、本体コード/仕様/Config変更なし。build/unit/check/GameTestは今回再実行せず直前の自動PASSを維持。長押しAPI欠如時は人間入力だけを依頼し、まだREAL CLIENT PASSは付けない。V3M0908 FOURTH BOOT/昨日world再利用/専用server/他MOD試験は未実施。ALL TACZ/GUNPACK COMPATIBILITY = NOT TESTED / DEDICATED NETWORK LATENCY = NOT TESTED / REAL 2-CLIENT = BLOCKED / RELEASE CANDIDATE = NO。下記9/9完了は実装・自動試験の前段履歴。

- **9/9最終判定: AMMO CONSERVATION / IMPLEMENTED + AUTOMATED INTEGRATION TESTED（TaCZ 1.1.7-hotfix2限定）**。利用者LOCKを反映し、active gunの有効化時one-shot heat/lock正常化、射撃時非消費抽選、manual bolt維持、exact-version/ready購入gateを実装。最終build/unit/check、vanilla GameTest **41/41**、TaCZ GameTest **41/41**、既存13 damage integration、新Ammo/heat A〜M/packet/reload/bolt/respawn、別JVM write/readはPASS。既存damage倍率は変更していない。以下の「未完成/判断待ち/最終回帰中」は解決前の時系列履歴として残す。
- 最終成果物はSHA `AB9B75DC97C095774B68A416D66A23E5795B635CC4473CF1E46D2383A946BB36` / **196,263 bytes / 123 entries**。配布物から既存GameTest class9件を除外したが、開発GameTest41件は維持。TaCZ class/Jar、新規test/restart fixture、world/NBT、ExampleMod混入0、metadata3.0.0/required Mixin/refmap確認。新S2C mechanical-cycle packetに伴いprotocol6となるためclient/serverは同じ更新buildが必要。
- **承認範囲完了・完全停止**。REAL CLIENT AMMO CONSERVATION = NOT RUN / ALL TACZ COMPATIBILITY = NOT TESTED / ALL GUNPACK COMPATIBILITY = NOT TESTED / RELEASE CANDIDATE = NO。9/8の実銃baseline PASSは新Adapterの実client PASSに流用しない。V3M0908 FOURTH BOOT、昨日world再起動、他version/custom pack、Flight/OPEN-02/03/04/True Root未LOCK/Break Realm/試作型機関弩/Bulwark/他MOD統合は実施していない。

- 9/9成果物監査中: 最終ゲーム回帰は`verified2-tacz-write/read`（別PID14788/6644）および`verified2-vanilla`で全件PASS/ERROR0/FATAL0。Jar監査では既存main GameTest class9件の同梱を検出したため、開発GameTest41件を残したまま配布Jar対象からだけ除外する。初回監査readerはZIP directoryを削除fileと誤認し、JSON DateTimeとISO文字列のUTC/local比較差で全保護fileを誤検出した。実hash/size/UTC ticks一致を確認し、readerのUTC比較とdirectory除外を修正。初回結果は`final-audit-first-failed.json`へ保持し、原本restoreやmtime変更は行わない。最終成果物は再build/監査後に確定する。

- 9/9最終回帰中の試験環境FAIL: `verified-tacz-write.log`でbuild/unit/checkとTaCZ GameTest41/41は成立したが、新規dedicated fixtureの弾薬Stack上限assertで停止（success receiptなし/exit1）。実default packの308弾はstack_size48であり、試験側が64と仮定していた。実APIのmaxStackSizeに従う予備弾分割へ修正し、通常/tactical reloadも同じ合法Stack経路を使用する。またfresh TaCZ GameTestのserver.properties未生成ERRORを確認し、既存の隔離properties準備をopt-in TaCZにも適用、GameTestのERROR/FATAL時はGradle exit0でも失敗させる検査を追加。以前のFAIL/ERRORは削除せず保持。先行`final-tacz-write/read.log`は別JVM保持PASSだったが、最後の追加respawn回帰を含む最終判定はこの再実行後とする。実client未起動。

- 9/9全遷移中間回帰: `ammo-full1.log`はheat A〜M・購入10x5SP・closed/open/pellet/cancel/emptyまでPASS後、manual bolt開始でFAIL。原因はfixtureがTaCZの相対`shootTimestamp`を-1としていて、起動直後の実cooldownが未経過だったこと。fixtureだけ-5000へ変更しnative shoot/draw cooldown0もassertした`ammo-full2.log`で**heat A〜M、manual empty/full magazine、native bolt completion、OFF復帰、通常/tactical reload総弾数保持、native shoot timestamp replay、既存13件 PASS**、19:33:52検証完了/exit0/BUILD SUCCESSFUL20s。判定は専用server実API/実MOD integrationで、client描画・予測操作の実試験ではない。購入gateはexact1.1.7-hotfix2＋server必須target解決＋Adapter初期化完了に限定して解放。新規pure unitは10段階10,000sample/異常値/loaderなしgateを追加。次は全vanilla/TaCZ GameTest、別JVM保持、client hook bytecode/成果物監査。実clientは引き続きNOT RUN。

- 9/9 Adapter中間回帰: **HEAT TRANSITION RED → INITIAL AUTOMATED INTEGRATION PASS / FULL REGRESSION IN PROGRESS**。実TaCZ `GunDrawEvent`と`ModernKineticGunScriptAPI.lambda$shootOnce$2`の消費・heat call siteを使用する限定Adapter/Mixinを追加。最初のfixtureはLazyOptional呼出し誤りでcompile FAIL（修正）、その後意図したheat未解除REDを記録（`heat-red-runtime.log`、fresh success receiptなし/exit1）。heat hook追加後`heat-green.log` PASS。弾薬hook初期回帰`ammo-initial.log`では既存13件＋heat/closed bolt/manual chamber PASS、writer PID7400、19:24:43保存・正常終了、exit0/BUILD SUCCESSFUL21s。`adapter-build.log` build/unit/check PASS。手動ボルトの待機状態を通常tick早期returnで解除しないよう、native bolt完了callbackだけに限定。変更は`compat/`・`mixin/tacz/`・購入/toggle/専用sync packet・任意Mixin plugin/設定・local compileOnly・taczTest。購入gateの正式解放条件と全A〜M/弾薬/restart回帰はまだ検証中、**実client未起動/全TaCZ互換PASSではない**。証拠root `build/verification/tacz-ammo-20260909-190319`、以前の停止・RED履歴は保持。

- 9/9 heat transition正式決定受領: **PRE-IMPLEMENTATION SPEC QUESTION / RESOLVED / LOCKED / ADAPTER AUDIT IN PROGRESS**。取得default ON・OFF→ON・ON中に過熱済み対応銃へ持ち替えたtransitionで、そのactive gunだけ既存heat/lockを一度正常化。ON中は新規heatなし、OFF時に過去heatを復元せず以後TaCZ baselineへ戻す。toggleによる即解除は正式に許容。`SPEC.md`10.8/`SKILL_TREE_SPEC.md`11/`COMPATIBILITY_POLICY.md`4/`TEST_PLAN.md`16へ反映し、以下の判断待ちは解決前の履歴として保持。実Jarのserver `GunDrawEvent`による対象銃通知を確認、残るshot/bolt/reload/predictionの監査中で、Adapter/gateはまだ変更していない。
- 9/9再開baseline: 非破壊backup `build/verification/tacz-ammo-20260909-190319/pre-change.zip`（src/docs/build設定/旧成果物、world含まず）を作成。最初のsandbox wrapper実行は既存Gradle cacheを利用できずdistribution取得時Permission deniedとなった（`baseline.log`）。既存cacheを許可された環境で`--offline`利用して再実行し、**build / foodHealingUnitTest / check / vanilla GameTest 41/41 PASS**、19:05:38正常停止、exit0 / BUILD SUCCESSFUL 42s。隔離`gametest-ammo-baseline-20260909-190319`のみ使用、通常run/worldや昨日のtest world未起動。log `baseline-local-cache.log`。TaCZ SHAは承認値FC5F...DB46一致。これは実装前baselineであり新Adapter PASSではない。REAL CLIENT AMMO CONSERVATION = NOT RUN / RELEASE CANDIDATE = NO。
- 9/9 Ammo Conservation実装承認後の事前監査: **SPEC CONFLICT / USER DECISION REQUIRED / PRE-IMPLEMENTATION STOP**。今回の停止理由は数値表の矛盾や再現済み不具合ではなく、**既存heat/overheat lockを持つ銃で取得・OFF→ON・装備した際の扱いが未定義**であること。正本のLv1〜10/各5SP/節約10〜100%/専用damage x1.05〜1.50/Lv1から過熱無効を確認したが、通常冷却まで既存heat/lockを保持するか、効果有効化時に限定解除するかは決められない。実Jarではclient/serverの過熱判定がshot処理より先にあり、heat加算だけ止めても既存lockは残る。利用者の今回指示section0/18に従い、仕様判断前のAdapter/RED test/購入gate変更を開始していない。9/8の実銃baseline PASSと既存自動PASSは元の範囲で維持。詳細は下記9/9フェーズと`build/verification/tacz-ammo-spec-audit-20260909-185457/REVIEW.md`。**REAL CLIENT AMMO CONSERVATION = NOT RUN / ALL TACZ COMPATIBILITY = NOT TESTED / ALL GUNPACK COMPATIBILITY = NOT TESTED / RELEASE CANDIDATE = NO**。
- 9/8 TaCZ実client試験完了: **TaCZ 1.1.7-hotfix2 / REAL CLIENT BASIC FIRE + RELOAD + NORMAL AUTO-BOLT RECHAMBER + HEAT LOCK/COOLDOWN RECOVERY / INTEGRATION TESTED / PASS**。対象は承認default packのGlock17・M700・M134 Minigunと新規使い捨てworldだけ。Glock/M700/冷却後復帰操作は**COMPUTER USE**、連続射撃による過熱表示・射撃停止は**HUMAN / MANUAL TESTED / PASS**として分離する。実射/通常reloadでの弾数・薬室・在庫・HUDとpause-save/最終保存値が一致した。M700は通常`tickAutoBolt`によるfire→rechamber→次弾発射の範囲であり、専用boltキーやアニメーション全フレーム測定ではない。**以下のPARTIAL/HEAT PENDING/人間入力待ちは各時点の履歴で、今回承認範囲は終了**。
- 9/8 heat最終結果: 人間の22:17:45 F2に`OVERHEAT`/残弾20、保存値に`HeatAmount=310.16741943359375`/`OverHeated=1`を確認。308弾200→20の180発消費はheat max360/per-shot2と整合。pause解除後の通常tick冷却で0.0%表示、22:25:24の通常pause保存で`HeatAmount=0.0`/`OverHeated=0`/残弾20を確認。その後通常左click1回で発射・一時0.6%表示・20→19を観測し、最終保存もslot9=11/slot10=8、heat0/lock0。**連続180発＋復帰1発＝181発消費、refund/複製なし**。F2計10枚（冷却`22.25.14`、復帰`22.25.57`）を保持。heat開始時刻の厳密記録はなく、人間F2が過熱終了点の証拠。長いpauseとwall-clock経過を挟むため、連続unpause状態の冷却所要時間や3000msで完全冷却という数値PASSにはしない。heat/lock/NBT直接設定・API射撃・custom input injectionは一切していない。
- 9/8 保存後照合: `audit/final-review.json`で9 checkpointのFood Healing **typed canonical完全一致**を確認。schema4/Lv0/count0/未使用SP0/使用済SP0/skills-base-toggle空/Root runtime0/Diversity空・bonus0、HP20/20・food20。最終`level.dat`と唯一のplayerdataでも対象player値・Inventory・ForgeCaps一致。Glock magazine17+薬室1/9mm予備45、M700 magazine2+薬室1/30_06予備5、Minigun AUTO/heat0/lock0/308予備19。TaCZ synced capabilityは保存時空list。これは今回の通常銃操作による進行の説明不能な変化なしの確認であり、Food Healing skill効果の追加試験や全TaCZ stateの永久保持保証ではない。今回world25 filesの最終hash manifestも保存した。
- 9/8 正常終了・log: **22:26:20 Save & Quit→22:26:21.520 All dimensions are saved→22:26:36.198 Stopping!**、runClient session25598はexit0 / **BUILD SUCCESSFUL in 34m 4s**（11 tasks:3実行/8 up-to-date）。22:27:05 Java/javaw0件を確認、強制killなし。実MC1.20.1/Forge47.2.0/Adoptium Java17.0.7/FHR3.0.0/TaCZ1.1.7-hotfix2/MixinExtras0.3.6、**開発main出力による実行で配布Jar直接起動とは区別**。latest283行/debug1435行はそれぞれERROR0/FATAL0/WARN124、world login以後の例外0、必須Mixin/dependency/classloading/registry失敗・NPE/CME・crash report/JVM crash0。起動時のdev refmap WARN、`.mixin.out`清掃DEBUG、初回Config default生成、Forge language provider metadata/union URL、test food2モデル欠落、vanilla sound/shader、IPv6 property WARNを保持。Nettyの任意reflection fast-path不可DEBUG stack2件とdev sessionのRealms認証不可INFO1件も隠さず別記し、全exception0とはしない。これらを修正済みとは扱わず、今回の銃操作例外と分離する。
- 9/8 最終保全・変更範囲: `audit/after-*`で元Prism/元instance13,351 files・world/ZIP/pristine/R0908・source2.2.5比較instance4,564 files・source104 Jar/shared runtime/cache・v3 migration instance4,472 files/第三保存・第二保存cold181 filesすべて追加差分0（集合/SHA/size/mtime）。通常`run`/`src`/build.gradle/gradle.properties不変、TaCZ/FHR Jarは承認SHAと一致。default pack2,939 filesは承認Jarと全hash一致、追加pack0。今回turnの変更は**本Status、今回auditの`final-review.py`と新規読み取りsnapshot/manifest/集計結果**、Minecraft自身による専用world/log/F2通常保存だけ。最終集計初回はCP932ログをUTF-8 strictで読んでUnicodeDecodeErrorとなったため、実byteのCP932確認後に監査readerのみ修正し再実行PASS。ゲーム/test FAILやlog書換ではない。Java/Mixin/本体Config/resource/仕様/dependency/恒久build設定・既存test変更なし、build/unit/check/GameTest/既存TaCZ13ケース再実行なし（過去41/41等を維持）。**Ammo Conservation未実装・購入gate維持、V3M0908 FOURTH BOOT = NOT RUN / NOT AUTHORIZED、ALL TACZ / GUNPACK / MOD GAMEPLAY COMPATIBILITY = NOT TESTED、RELEASE CANDIDATE = NO**。承認試験を完了して停止し、Adapter/次試験へ自動進行しない。証拠root: `build/verification/restart-tacz-realclient-20260908-213957`。
- 9/8 TaCZ heat人間結果受領: **MINIGUN OVERHEAT INDICATION + FIRING LOCK / MANUAL TESTED / PASS / COOLDOWN RECOVERY PENDING**。利用者が同じsessionで左mouse長押し、過熱表示と射撃停止、release→F2→Esc pauseを報告。`2026-09-08_22.17.45.png`に`OVERHEAT`と残弾20を確認。22:21:18の読み取り`audit/minigun-human-overheat-snapshot.json`で308弾200→20（slot9=12/slot10=8、180発消費）、`HeatAmount=310.16741943359375` / `OverHeated=1`、他銃とFood Healing canonical不変、latest/debug ERROR/FATAL/critical exception0を確認。180発はdefault heat max360/per-shot2と整合し、直接heat設定・弾数書換・API射撃なし。停止後の冷却進行を含む保存値であり、peak heatの厳密実測や冷却所要時間PASSへ拡張しない。同じMinecraft window9898514はpause継続、次は通常冷却・射撃復帰と正常保存終了の限定確認。最終総合判定/終了後保全はまだ未完了。以下のheat未実施は前段履歴。
- 9/8 TaCZ実client人間入力待ち: **TaCZ 1.1.7-hotfix2 / REAL CLIENT BASIC FIRE + RELOAD + NORMAL AUTO-BOLT RECHAMBER / PARTIAL PASS / HEAT PENDING**。Glockの1発実射/標的hit/実R reloadに加え、M700を実Rで5発装填（予備10→5）、22:05:02と22:05:38の実左clickで各1発を発射し、HUD5→4→3を確認。初弾は静止Cowへhitして通常死亡/drop/XP描画、2発目は地面方向。pause-save NBTは初弾後magazine3+薬室1、次弾後magazine2+薬室1、予備5不変。実Jarの`LocalPlayerBolt.tickAutoBolt`は通常client tickからmanual-action銃をrechamberする経路であり、今回は実射→通常auto-bolt→次弾実射の成立を確認した範囲。専用boltキー操作や0.85秒アニメーション全フレームの厳密測定PASSではない。Glockはmagazine17+薬室1/9mm予備45のまま。Food Healing schema4/Lv0/count0/SP0/used0/skill-base-toggle空/Diversity空/Root runtime0/HP20/20は5つのpause-saveで不変、latest/debug ERROR/FATAL/critical exception0。
- 9/8 heat準備checkpoint: 標準`M134 Minigun`をhotbar6へ装備（GunId `tacz:minigun` / AUTO）、308弾200発（48×4+8）を準備し、**Minigun実射0・heat/lock直接設定0**で22:09:17頃Esc pause。同じMinecraft window9898514、Java群11644/12728/31868とrunClient exec session25598を保持。`/give`400発は通常の100上限で拒否され付与0、その後100発×2で200発となったことを保存NBT確認。この準備上限拒否はTaCZ/FHR実射FAILではない。標準heatはmax360/per-shot2、連続約180発が閾値の目安。cooling delay500ms・overheat delay3000msで、3000msを完全冷却時間とは扱わない。**HEAT REAL CLIENT = NOT YET TESTED / WAITING FOR HUMAN HELD INPUT**。Computer Useはhold/release APIがないため代替native injectionは使わない。人間がEscで戻り、左mouseを約10秒（約180発目安）長押しし、過熱表示/射撃停止で離し、可能ならF2、直後Esc pauseする入力だけを依頼する。人間報告後に同じsessionのheat/lock/残弾/logを確認し、通常cooldown後の復帰を確認してからSave & Quit→Quit→保存後監査へ進む。**最終正常終了・最終総合PASSは未完了**。
- 9/8 TaCZ途中保全: `audit/paused-before-human-*`で元instance13,351・元world/ZIP/pristine/R0908/source2.2.5比較instance4,564・v3 migration instance/第三boot保存world・cold copy・source104 Jar/shared runtime/cacheに追加差分0。`src`/通常`run`/build.gradle/gradle.properties/承認TaCZ Jar/FHR成果物も開始時から不変。TaCZが今回run内へ通常exportしたdefault pack2,939 filesは承認Jar内packと全file hash/集合一致、追加/custom packなし。`audit/partial-review.json`の保存弾数・Food Healing不変・log照合はPASS（実client入力の証拠補強であり既存13ケース自動fixtureの再実行ではない）。変更ファイルは本Statusと今回audit内の`pack-audit.ps1`、`client-isolation.init.gradle`、`preserve.py`、`capture.py`、`partial-review.py`および生成manifest/log/読み取りsnapshot。F2は今回client/screenshots内の7枚。Java/Mixin/本体resource/Config/仕様/dependency/build設定の恒久変更なし、build/unit/check/GameTest再実行なし、41/41・既存TaCZ13ケース・旧world第三bootの過去PASS不変。**Ammo Conservation未実装/gate維持、V3M0908 FOURTH BOOT NOT RUN / NOT AUTHORIZED、ALL TACZ/GUNPACK/MOD COMPATIBILITYおよびRELEASE CANDIDATEのPASSではない**。
- 9/8 TaCZ実client途中: **BASIC GLOCK FIRE + RELOAD / REAL CLIENT (COMPUTER USE) + PAUSE-SAVE NBT CORROBORATED / PARTIAL PASS / TEST CONTINUES**。同じ1回のrunClientで21:52:42起動、実MC1.20.1/Forge47.2.0/Adoptium Java17.0.7/FHR3.0.0/TaCZ1.1.7-hotfix2をlog確認、main menu到達。21:55:53に新規`TaczRealClient_20260908`へDev login。worldは今回run内だけのsuperflat/survival、準備としてcheats許可・peaceful、空Glock/9mm64と静止Cowを準備した。NBT指定は銃/弾種IDと正規fire modeのみ、magazine/heat値指定なし。21:57:58実Rで空reload→HUD17/予備47、21:59:31実左click1発→Cow赤色被弾反応/発射動作/HUD16、通常Esc pause-saveでmagazine15+薬室1/予備43+4=47を確認。22:01:56実Rでtactical reload→HUD18/予備45、pause-save NBT magazine17+薬室1/予備41+4=45と一致。全64発中63発残存、射撃1発消費、reloadで2発だけ移動。Food Healing schema4/Lv0/count0/SP0/used0/skills-base-toggle空/Diversity空/Root runtime0保持。latest/debugのERROR/FATAL/critical exception0。開発refmap・初回Config生成・test food model未登録等のWARNは隠さず別扱い。F2 4枚と`audit/glock-first-shot-snapshot.json`、`glock-reloaded-snapshot.json`を保持。**bolt/heat/最終保存終了は未完了**。Minecraftは同じprocessでpause、旧world未起動、Ammo Conservation gate維持。次はM700、その後Minigun heat。長押しAPIがない場合は人間のその入力だけを依頼する。
- 9/8 TaCZ実client起動前: **TaCZ 1.1.7-hotfix2 / REAL CLIENT BASIC GUN OPERATION / PREFLIGHT PASS / NOT YET LAUNCHED**。利用者が旧worldとは独立した使い捨てworldでの通常fire/reload/bolt/heatだけを承認。`libs/tacz-1.20.1.jar` SHA `FC5F1DAB09AFD5399604DB0F60845D41D46CF015C158CBC12E7EF716CF6BDB46`、FHR配布Jar SHA `2074EF581795164F54C032A8B8EEC6FEF2DD7BB9799DB057E04E2ABA8E84F90B`を再確認。default pack実47 gun dataからGlock17 (17発/9mm/closed bolt)、M700 (5発/30_06/manual action)、Minigun (308/inventory reload/heat max360・per-shot2)を選定。既存`foodHealingTaczIntegration=true`、`foodHealingRestartPhase=write`、`foodHealingRestartRun=tacz-realclient-20260908-213957`を再利用し、監査用の一時init scriptのみでclient working directoryを`build/verification/restart-tacz-realclient-20260908-213957/client`へ限定。server/restart fixtureは呼ばず、通常run/worldは使用しない。開発runtimeはmain出力をロードするため、配布Jarそのものの実行試験とは区別する。元instance/ZIP/world/pristine/R0908/source2.2.5/v3比較instance/cold backup/MOD/shared runtime保全差分0。`inspectApprovedTaczClient --offline --no-daemon`は最終exit0 / BUILD SUCCESSFUL 7s。先行したsandbox wrapperキャッシュ権限失敗、監査scriptのtask生成前取得失敗、JavaExec.workingDirの実行前値を検査した失敗はpreflight内の履歴として保持。ForgeGradle実bytecodeでexec時にrunConfigからworkingDirが設定されることを確認し、runConfig検証に修正してPASS。Minecraft起動はまだ0回。変更は監査rootと本Statusのみ、Java/Mixin/build.gradle/dependency/Config/仕様/既存test変更なし、build/unit/check/GameTestは再実行していない。既存13ケース自動TaCZ PASS・vanilla41/41・旧world第三boot PASSは不変。**Ammo Conservation未実装/gate維持、V3M0908 FOURTH BOOT NOT RUN / NOT AUTHORIZED**。証拠: `build/verification/restart-tacz-realclient-20260908-213957/audit`。
- 9/8 第三boot完了: **REAL V3 MIGRATED WORLD / THIRD BOOT / LIMITED REAL-CLIENT PLAYABILITY SMOKE / PASS WITH RECORDED GAMEPLAY DELTAS**。同じ`V3M0908`で人間が既存在庫の昆布1食・室内約30秒の移動、CodexがE→S GUI・返還SPによるRoot Lv1通常購入1回・ON→OFF→ON各1回を実施した混合主体の限定試験。保存後schema4/Lv2/count36/未使用1/使用済1/Root Lv1 ON、Root runtime0、raw backup/Diversity28/3/10保持、Inventoryはslot8の昆布33→32以外typed一致。21:14:24通常保存、21:14:48 `Stopping!`、21:28:13 Java/javaw0、同じ隔離Prism PID25172のみ継続。ERROR250は第二bootのfeature/full stack一致、新規重大ERROR/必須Mixin/dependency/FATAL/crash0、既知NightConfig例外再発。原本/ZIP/pristine/R0908/source2.2.5比較instance/source104 Jar/shared runtime/v3成果物/cold backup追加差分0。37保存証拠条件成立。ジャンプは人間報告のみ（保存統計2384→2384）で数値PASSにせず、移動中の全フレーム・rubber-band/freezeの連続観測も未実施と区別する。任意interactionはSKIPPED。**FOURTH BOOT = NOT RUN / NOT AUTHORIZED、ALL MOD GAMEPLAY COMPATIBILITY = NOT TESTED、RELEASE CANDIDATE = NO**。今回の承認範囲は終了し完全停止。以下の未完了/pause/操作待ちは当時の履歴。
- 9/8 第三boot通常終了: **THIRD BOOT / NORMAL SAVE + EXIT COMPLETE / POST-SAVE REVIEW IN PROGRESS / FINAL VERDICT PENDING**。人間から室内約30秒の歩行/視点移動/ジャンプ数回/短いsprint、追加食事なし・Portal/機械未操作・Esc pauseの報告を受領。同じJava PID27180から21:14:24通常Save & Quit、Saving players/worlds・All dimensions are saved、21:14:48 `Stopping!`、21:15:06 Java/javaw0を確認。同じ隔離Prism PID25172は継続。保存後canonicalはschema4/Lv2/count36/未使用1/使用済1/Root Lv1 ON、Root runtime0、Diversity28/3/10、LegacyV2Backup不変。slot単位の在庫、時間/乱数/CarryOn同期tickなどの差分を読み取り確認中。最終ERROR250は第二bootのfeature/full stack一致、FATAL/必須Mixin/dependency/新規重大ERROR/crash0、原本/比較instance/cold backup追加差分0。コード・Config・仕様変更/自動回帰の再実行なし。**FOURTH BOOT = NOT RUN / NOT AUTHORIZED**。以下の操作待ちは当時の履歴として保持する。
- 9/8 第三boot食事・購入・toggle完了: **THIRD BOOT IN PROGRESS / FOOD + GUI ACTIONS COMPLETE / WAITING FOR BOUNDED MOVEMENT / FINAL VERDICT PENDING**。同じJava PID27180/同じworld sessionのまま、人間による昆布1食（33→32/count35→36/Lv2/HP50/50）報告を受領し、実画面・21:00:19の食事log1件で照合。21:05:02にCodexが通常GUIからRoot Lv1を1回購入し、未使用2→1/使用済0→1/Lv1/5/default ONを確認。ON→OFF→ONも各1回、最終ON/SP1/使用済1/count36を画面確認・F2保存。**食事はHUMAN、購入/toggleはCOMPUTER USE**で区別し、保存後canonicalのPASSはまだ付けない。ERROR250は第二bootのfeature/full stack一致、新規ERROR/必須Mixin/dependency/FATAL/crash0。原本/比較instance/cold backup追加差分0。21:06:45ゲームメニューpause、再起動/切断/追加食事なし。残る所定の短い移動確認だけは長押しAPI欠如のため人間支援が必要。その後に通常Save & Quit/終了/保存後監査。**FOURTH BOOT = NOT RUN / NOT AUTHORIZED**。
- 9/8 第三boot途中: **THIRD BOOT / LIMITED REAL-CLIENT PLAYABILITY SMOKE / PAUSED - HELD INPUT NOT AVAILABLE / NOT PASS**。20:48:36に同じ隔離Prismから1回通常起動（Java PID27180）、20:50:27 integrated server、20:50:39 <MINECRAFT_ACCOUNT> login、V3M0908のworld/HUD描画を確認。E→SでGUIが開きLv2/count35/未使用2/使用済0を実画面確認、native F2でworld/GUIの2枚を保存。短い後退等の離散入力は行ったが、APIにhold/release/durationがなく継続歩行・sprint・ジャンプ成立・視点移動は十分に検証できていない。食事に適する既存在庫の乾燥昆布はあるが長押しできず未消費（33個/count35）、Root購入0/toggle0。設定変更・代替native入力・ゲームコマンドは使わず、20:53:24頃Escのゲームメニューでpause。同じJava/Prism/world sessionを維持し、食事を購入前に行う順序を守るため人間の長押し操作支援待ち。ERROR250は第二bootとfeature/full stack一致、FATAL/必須Mixin/dependency/新規ERROR/crash0、既知NightConfig例外再発。途中原本/比較instance/cold backup差分0。**第三bootは未完了、保存後監査未実行、MANUAL PASSは付与しない。FOURTH BOOT = NOT RUN / NOT AUTHORIZED**。下記に証拠と再開地点を記録。
- 9/8 第三boot起動前: **REAL V3 MIGRATED WORLD / THIRD BOOT / LIMITED REAL-CLIENT PLAYABILITY SMOKE / PREFLIGHT PASS / NOT YET LAUNCHED**。利用者が同じ`V3M0908`の短時間Gameplay Smokeを1回だけ承認。第二保存後world181 files・v3 instance4,463 filesの集合/SHA/size/mtimeと全typed保存値が一致。原本/ZIP/pristine/R0908/source2.2.5 control/承認metadata/source104 Jar/shared runtime/v3成果物の追加差分0。`build/verification/real-v3-playability-20260908-204540/cold-secondboot-V3M0908`へcold backup181 filesを作成し完全一致、saves外に未起動で保持。<MINECRAFT_ACCOUNT> / MSA / 準備完了をUI確認。同じ隔離Prism PID25172を維持。予定は近距離移動・E/S GUI・既食の安価な食料1個（Root購入前）・Root Lv1を1SPで通常購入・ON→OFF→ON・安全な任意interaction・通常保存終了。**REAL CLIENT / COMPUTER-OPERATED**として記録し、利用者のMANUAL PASSにはしない。今回変更は監査helper/manifest/cold copy/Statusのみ、コード/Config/仕様変更なし、build/unit/check/GameTest再実行なし。**FOURTH BOOT = NOT RUN / NOT AUTHORIZED**。以下の第三boot未承認は前段checkpoint当時の履歴。
- 9/8 v3第二boot完了: **REAL V3 MIGRATION WORLD / SECOND BOOT + RELOAD + NORMAL SAVE / IDEMPOTENCY PASS**。初回保存済みの同じ`V3M0908`を既存Java PID31244から1回だけ再読込し、world描画・<MINECRAFT_ACCOUNT> login・通常保存・20:19:37 `Stopping!`・Java/javaw0を確認。canonical全typed状態が初回保存時と完全一致、schema4/Lv2/count35/未使用2/使用済0、skill/base/toggle空、Root runtime0、pending=false、LegacyV2Backup元raw完全一致、Diversity28/3/10保持。**SP再返還0（2→2）**。元UUID/唯一のplayerdata/Inventory/XP/HP/対象外部保存データを確認。時間・list順序・通常乱数4step等の差分は下記へ記録。ERROR250は初回bootのfeature/full stack一致、FATAL/必須Mixin・dependency/新規重大ERROR/crash0、既知NightConfig例外再発。原本/ZIP/pristine/R0908/source2.2.5比較instance/承認metadata/source104 Jar/shared runtime/v3成果物の追加差分0。監査28条件成立、**THIRD BOOT = NOT RUN / NOT AUTHORIZED**。Minecraft終了済み、同じ隔離Prism PID25172のみ継続。本承認範囲を完了し完全停止。全MOD gameplay互換・historical-wrapper認証組合せ・release candidateのPASSではない。以下の第二boot待ち/未完了は各時点の履歴。
- 9/8 v3第二boot通常保存終了: **SECOND BOOT + NORMAL SAVE COMPLETE / POST-SAVE AUDIT IN PROGRESS / FINAL VERDICT PENDING**。人間のmain menu screenshot受領後、Computer UseのJS接続だけを再初期化して画面取得が復旧。Minecraft/Prism再起動なし、同じJava PID31244から一覧1件の保存済み`V3M0908`を1回だけloadし、20:18:28 integrated server、20:18:39 <MINECRAFT_ACCOUNT> login、world/chunk描画を確認。ゲームプレイ操作なし。今回Firewall画面は観測されず、Firewall設定を操作していない。20:19:16に通常Save & Quit、players/worlds保存・All dimensions savedを確認し、20:19:37.758 `Stopping!`、20:19:38 Java/javaw0。Food Healingのcanonical/LegacyV2Backup/Diversity全typed状態は初回保存時と完全一致、元UUID一致、SP2→2/再返還0。最終log ERROR250は初回feature一致、新規重大ERROR/必須Mixin/dependency/FATAL/crash0。原本/source2.2.5比較instance/承認metadata/ZIP/pristine/R0908/MOD/shared runtimeの終了後差分0。追加NBT/MCA/外部保存範囲の比較を継続中で、まだ最終PASSとはしない。**THIRD BOOT = NOT RUN / NOT AUTHORIZED**。以下のUI待ち記録は当時の履歴。
- 9/8 20:05 UI再取得: 利用者の前面表示後、指定どおり5秒待機して同じMinecraft window1180330を再取得したが、`foreground window did not report a process id`が継続。返却windowからの再試行も失敗したため、クリック・キー送信・world読込は行っていない。Java PID31244/start19:43:14とPrism PID25172/start17:23:40は同一。ログの実116 ID/version一致、ERROR16は初回起動baseline、FATAL/必須Mixin・dependency/新規重大ERROR/crash0、world load開始logなし。**SECOND WORLD BOOT = AUTHORIZED / NOT YET RUN / UI OBSERVATION BLOCKED**を維持し、同一プロセスを保持。次は現在のMinecraft画面のスクリーンショット1枚を人間から受領し、実画面を確認する。再起動や再launchは要求しない。証拠: `build/verification/real-v3-secondboot-20260908-193700/ui-retry-blocked-logs.json`。
- 9/8 v3第二boot途中停止: **PAUSED - COMPUTER USE WINDOW CAPTURE FAILED / WORLD NOT OPENED / NOT PASS**。19:43:14に同じ隔離PrismからMinecraftを1回だけ起動（Java PID31244）。実MC1.20.1/Forge47.4.0/Microsoft Java17.0.15/FHR3.0.0/116 ID-version一致、Wrapper実classpath `prism-2026-08-01`を確認。19:43:55 TitleScreen初期化logはあるが、画面取得が`foreground window did not report a process id`で失敗し、返却ウィンドウの再取得でも復旧せず、world一覧/選択/読込操作は0回。起動ERROR16は初回baselineとfeature/full stack一致、FATAL/必須Mixin・dependency失敗/新規重大ERROR/crash0、既知ModernFix/NightConfig例外再発。V3M0908全181 filesは不変。**同じMinecraft PID31244を維持して画面確認待ち。再起動・再launchしない。** 第二world bootは承認済みだが未実行、保存後冪等性判定は未実施。第三bootは未承認。これはFood Healing不具合の確定ではなく、UI操作ツールによる観測不能の停止。詳細: `build/verification/real-v3-secondboot-20260908-193700/ui-checkpoint.json`。
- 9/8 v3第二boot起動前: **SECOND BOOT / RELOAD / PREFLIGHT PASS / NOT YET LAUNCHED**。利用者が既存保存済み`V3M0908`の2回目bootを1回だけ明示承認。初回保存後181 world filesとv3 instance全4,461 filesの集合/SHA/size/mtimeが完全一致、typed保存状態・schema4/Lv2/count35/未使用2/使用済0/skill-base-toggle空/Root runtime0/pending=false・LegacyV2Backup元raw完全一致・Diversity28/3/10を再確認。原本/ZIP/pristine/R0908/source2.2.5比較instance4,564 files/承認済みmetadata/source104 Jar/shared runtime/cache/v3成果物の追加差分0。既存隔離Prism PID25172を維持し、GUIでLeva9846 / MSA / 準備完了を確認。新world copy0、コード/Config/仕様変更なし、build/unit/check/GameTestは再実行不要。証拠: `build/verification/real-v3-secondboot-20260908-193700`。今回は再読込・通常保存・前後比較のみ、**THIRD BOOT = NOT RUN / NOT AUTHORIZED**。以下の第二boot未承認記録は当時の履歴として保持する。
- 9/8 実v3初回migration終了: **REAL V3 MIGRATION WORLD TEST / AUTHENTICATED ORIGINAL-ACCOUNT / CURRENT-WRAPPER / FIRST BOOT + NORMAL SAVE / PASS WITH RECORDED SOURCE BASELINE DIFFERENCES**。人間のFirewallキャンセル後、同じJava PID26304・同じ1回の`V3M0908` loadを継続し、world描画→通常Save & Quit→19:01:09 `Stopping!`→Java/javaw0を確認。保存NBTは元UUID一致、schema4/Lv2/count35/未使用2/使用済0、skill/base/toggle空、Root runtime0、pending=false、旧Shokugi compoundのtyped完全backup、Diversity28/3/10保持。Inventory/XP/HP/位置・対象外部保存データを確認し、時間/list順序等の差分は下記へ記録。最終ERROR250は起動16 + world234のsource baseline対応、旧FHR max-cap ERROR1件消失は期待version差。必須Mixin/dependency・FATAL・新規重大ERROR/crash0、既知NightConfig例外再発。原本/ZIP/pristine/R0908/2.2.5比較instanceと承認済みmetadata/source104 Jar/shared runtimeの追加差分0。今回copy181 filesを保持して完全停止、隔離Prism PID25172は継続。**V3 SECOND-BOOT / RELOAD = NOT RUN / NOT AUTHORIZED**。全MOD gameplay互換・historical-wrapper認証組合せ・release candidateのPASSではない。以下のNOT RUN/未承認/Firewall待ち記録は各時点の履歴として維持する。
- 9/8 実v3初回migration途中: **FIRST WORLD LOAD IN PROGRESS / WAITING FOR HUMAN FIREWALL CANCEL / NOT PASS**。18:52:43に既存隔離Prismからv3を1回通常起動（Java PID26304）、main menu→一覧1件の`V3M0908`を18:54:13に1回だけ読込開始。実116 ID/version一致、Wrapper `prism-2026-08-01`を今回classpathの選択情報で確認。18:54:17 integrated server開始、18:54:25 MTR `0.0.0.0:8888` listener開始後、Windows Firewall確認画面を観測したためCodexは操作せず人間の「キャンセル」待ち。途中logのERROR247件はsource-world baseline feature一致、FATAL/必須Mixin/dependency失敗/新規重大ERROR/crash検出0（途中観測であり最終PASSではない）。既知ModernFix/NightConfig例外再発。**同じMinecraft process/同じworld loadを維持し、再起動・再loadしない。** player UUID・world描画・正常保存・終了後NBT/原本照合は未完了、第二boot未承認。証拠: `build/verification/real-v3-firstboot-20260908-185000/ui-checkpoint.json`。
- 9/8 実v3初回migration準備: **FIRST BOOT + NORMAL SAVE + POST-SAVE AUDIT / PREPARED / NOT YET LAUNCHED**。利用者が今回1回だけ明示承認。稼働中の隔離Prism PID25172を継続使用し、未起動pristineから新`V3M0908`へextended-path copyを作成、181 filesの集合/SHA/size/mtime完全一致、typed旧Food Healing Lv2/count35/Disabled空・Diversity28/3/10を確認。原本/ZIP/pristine/R0908/2.2.5比較instance4,564 files/承認済みmetadata/shared runtime/source104 Jarと指定v3成果物に追加差分0。v3 instanceは104 Jar/version不変、savesは今回copy1件のみ。監査root: `build/verification/real-v3-firstboot-20260908-185000`。**2回目boot/reloadは未承認**。以下のworld未承認記録は前段worldless時点の履歴として維持する。
- 9/8 v3 source-modset worldless完了: **V3.0.0 CURRENT-WRAPPER SOURCE-MODSET / ISOLATED PRISM / WORLDLESS SMOKE / PASS**。既存隔離Prism PID25172を再起動せず、新v3 instanceを1回だけ通常起動。正規MSA <MINECRAFT_ACCOUNT>、実MC1.20.1 / Forge47.4.0 / Microsoft Java17.0.15 / Food Healing3.0.0 / CURRENT Wrapper prism-2026-08-01、想定116 ID/version一致、104 Jar（Food Healingだけ指定SHAへ変更）を確認。main menu到達、ERROR16はsource baselineのlogger/message/stack一致、旧max health cap ERROR1件消失は想定FHR version差。NEW/UNEXPLAINED重大ERROR・FATAL・必須Mixin/dependency/registry失敗・crash0。既知ModernFix/NightConfig例外は再発し無害とはしない。18:03:17 `Stopping!`、Java/javaw0、原本/2.2.5承認済み新metadata baseline/R0908等の追加差分0、world生成/持込0。隔離Prismを開いたまま停止。**REAL V3 MIGRATION WORLD TEST = NOT RUN / V3 OLD-WORLD BOOT = NOT RUN**。全MOD機能互換・実world移行・release readyを意味しない。
- 9/8 v3 worldless継続承認・起動前: **SOURCE 2.2.5 CONTROL METADATA DIFFERENCE / RECORDED AND USER-ACCEPTED AS NEW COMPARISON BASELINE**。18:00:10 JSTに利用者が承認した現在の2metadata SHA/size/mtimeを新baselineとして記録。過去差分は未解明・無害未判定・未restoreの履歴として維持する。前回停止から原本/2.2.5 instance/R0908等の追加差分0、新v3は指定SHA/104 Jar（他103同一）/world0。開いている隔離Prism PID25172を継続使用し、再起動・一覧再読込なし。今回は新v3 instanceのWORLDLESS SMOKE1回だけ承認、world起動は未承認。**REAL V3 MIGRATION WORLD TEST = NOT RUN / V3 OLD-WORLD BOOT = NOT RUN**。
- 9/8 v3 worldless起動直前停止: **STOPPED BEFORE MINECRAFT LAUNCH - SOURCE 2.2.5 CONTROL INSTANCE METADATA CHANGED / NOT PASS**。新v3 instance作成とコピー後の保全は成立したが、新folderを認識させる隔離Prism通常再表示の後、比較基準2.2.5 instanceの`instance.cfg`にmtimeのみ更新、`mmc-pack.json`に`org.lwjgl3.cachedVolatile: true`の削除（36 bytes）を検出。不変条件を満たさないためMinecraft起動0回で停止し、修復・restore・新baseline承認の推定はしない。元通常Prism/元world/ZIP/pristine/R0908/source104 Jar/shared runtimeには追加変更0。新instance/world0を保持、Java/javaw0、隔離Prism PID25172のみ継続。**V3 WORLDLESS SMOKE = NOT RUN / REAL V3 MIGRATION WORLD TEST = NOT RUN / V3 OLD-WORLD BOOT = NOT RUN**。既存41/41等の自動PASS・2.2.5 source試験の過去PASSは変更しない。継続条件の利用者判断待ちで停止。
- 9/8 v3 SOURCE-MODSET WORLDLESS準備: **PREPARED / NOT YET LAUNCHED**。利用者が明示承認した新規隔離instance `FHR_V3_Worldless_20260908_171804`を作成。MC1.20.1 / Forge47.4.0 / Microsoft Java17.0.15、104 top-level Jarのうち103はsourceと同一、Food Healingのみ指定SHA `2074EF581795164F54C032A8B8EEC6FEF2DD7BB9799DB057E04E2ABA8E84F90B`の3.0.0へ変更。worldコピー0、saves未作成。元2.2.5検証instance4,564 files・原本/shared runtime等は準備前後差分0。実GUIでLeva9846 / MSA / 準備完了を確認。新folder検出のため隔離Launcherだけを通常再表示する段階で、Minecraft未起動。**REAL V3 MIGRATION WORLD TEST = NOT RUN / V3 OLD-WORLD BOOT = NOT RUN（未承認）**。詳細証拠は`build/verification/v3-worldless-20260908-171804`。
- 9/8正式決定migrationフェーズ完了: **OPEN-01 / OPEN-05 = RESOLVED / LOCKED / IMPLEMENTED / AUTOMATED FIXTURE TESTED**。旧Lv1:1の全SP返還・旧skill自動取得なし・raw完全保持を実装。最終build/unit/check・vanilla **41/41 PASS**、synthetic旧ディスク5ケース+既存fresh/legacyの別JVM、実抽出capability2種の別JVM write/save/stop/readはPASS。実payloadはLv2/count35、未使用2/使用済0、取得skill/base stat/toggle空、Root runtime0、Diversity28/3/10、raw型付き完全一致、pending解除を確認。新規fixtureは配布Jarへ混入なし。**今回承認された実装/自動検証/成果物監査は完了し、ここで完全停止する。REAL V3 MIGRATION WORLD TEST = NOT RUN / V3 OLD-WORLD BOOT = NOT RUN（未承認）**。旧world/Prism/source2.2.5 Jarを変更・起動していない。既存MANUAL PASS・実2-client BLOCKED・他OPEN/gateは維持し、リリース候補とはしない。以下の過去OPEN/未実装記録は当時の履歴。
- 9/8 migration決定・変更前監査: **OPEN-01 / OPEN-05 = RESOLVED / LOCKED（2026-09-08利用者決定）**。有効な旧Lvを1:1で未使用SP返還、使用済0、旧skill自動取得なしのrespec。実装・自動fixture・成果物監査だけ承認、実worldへのv3差し替え/bootは未承認。下記の過去OPEN記録は当時の履歴として保持する。別LOCKED migrationとの衝突は確認されず、schema4/raw backup/pending/purchase/login/clone/syncを再監査。変更前build/unit/check・vanilla GameTest **39/39 PASS**、16:40:27 `BUILD SUCCESSFUL`/exit0。初回sandbox Gradle起動はキャッシュ権限制約でtask開始前FAIL、既存cache権限でoffline再実行してPASS（コードFAILではない）。Adoptium17.0.7 / Gradle8.1.1 / Forge47.2.0。バックアップ: `backups/migration-decision-20260908-164019`（src/docs/build設定等、manifest付き）。コード変更前、実装/変更後回帰は未完了。**REAL V3 MIGRATION WORLD TEST = NOT RUN / V3 OLD-WORLD BOOT = NOT RUN**。Prism/旧world/原本/ZIP/pristine/source2.2.5 Jar未操作。
- 全体状態: **IN PROGRESS - リリース候補ではない**
- 9/8 正規元アカウント・CURRENT Wrapper world retest: **REAL V2.2.5 WORLD COPY / AUTHENTICATED ORIGINAL-ACCOUNT / CURRENT-WRAPPER RETEST / PASS WITH RECORDED HISTORICAL BASELINE ERRORS**。人間のFirewall「キャンセル」後、同じ1回の`R0908`読込を継続し、world描画・Food Healing Lv2/count35表示、実UUID `<PLAYER_UUID>`、Food Healing全typed capability保持、記録対象の外部UUID data保持を確認。Inventory/XP/Score/HP不変、既存effectsの経過時間・list順序・Rotation等の差分は記録して修復しない。ERROR251件は起動17 + world-load234（231 HISTORICAL BASELINE MATCH / 3 PARTIAL MATCH）、NEW/UNEXPLAINED 0、FATAL/必須Mixin・dependency・registry全体失敗/crash 0。ProjectE Checker例外は今回非再発。12:26:19全dimension保存完了、12:26:47 `Stopping!`、12:26:48 Java/javaw 0件。原本/ZIP/pristine/元metadata/shared runtime・104 Jarの追加変更0。Minecraft終了済み、隔離Prismのみ保持。旧停止履歴・baseline ERRORの未解決状態を消さず、今回の限定load/save以外へ拡張しない。**HISTORICAL-WRAPPER AUTHENTICATED COMBINATION = NOT TESTED / V3 MIGRATION = NOT RUN / NOT AUTHORIZED**。今回の作業を終了し停止。
- 9/8 正規MSA・隔離CURRENT Wrapperの1回限定smoke: **AUTHENTICATED CURRENT-WRAPPER SOURCE RUNTIME CONTROL / ISOLATED PRISM ROOT / WORLDLESS SMOKE / PASS WITH RECORDED BASELINE DIFFERENCES**。`<MINECRAFT_ACCOUNT> / MSA / 準備完了`確認後、通常Prism起動1回でMC1.20.1 / Forge47.4.0 / Microsoft Java17.0.15 / Food Healing2.2.5、runtime **116 ID/version一致・不一致0・追加0**、104 top-level MOD Jar hash不変、main menu表示を確認。今回Prism実起動logのJVM classpathが`ForgeWrapper-prism-2026-08-01.jar`を参照することを確認した（準備metadataのみの推測ではない）。ERROR17件は旧smokeとlogger/message/resource/exception/stack一致、旧Iron Furnaces通信ERROR1件は今回更新確認成功によるnetwork条件差、FATAL severity/必須Mixin・dependency・registry失敗/crash reportは0。ModernFix/NightConfigの既知ConcurrentModificationExceptionは再発し無害とはしない。11:31:27 JSTにMinecraft「終了」→`Stopping!`、11:31:49にJava/javaw 0件。原本保全差分0、world持込/生成/接続0（空のsaves directoryのみ通常生成）。隔離Prismは開いたまま停止する。**HISTORICAL-WRAPPER AUTHENTICATED COMBINATION = NOT TESTED / ORIGINAL-UUID WORLD RETEST = NOT RUN / V3 MIGRATION = NOT RUN / NOT AUTHORIZED**。
- 9/8 人間MSA認証後pre-flight: **HUMAN MSA AUTHENTICATION / <MINECRAFT_ACCOUNT> / PASS**。利用者が通常Microsoft認証を完了し、Codexも隔離Prismのアカウント一覧で`<MINECRAFT_ACCOUNT> / MSA / 準備完了`を確認した。account file/token等は読まず、認証・再認証を代行していない。原本instance/metadata/world/ZIP/pristine/MOD/shared runtime/cacheは認証前snapshotから内容・size・mtimeの追加変更0、元launcher直下`prismlauncher.cfg`も承認済み新baselineと一致。**AUTHENTICATED CURRENT-WRAPPER SOURCE CONTROL / READY FOR WORLDLESS SMOKE**は起動前準備に限定する。10:47:19 JST、隔離Prism PID26340のみ継続、Java/javaw 0件。**WORLDLESS SMOKE = NOT RUN / HISTORICAL-WRAPPER AUTHENTICATED COMBINATION = NOT TESTED / ORIGINAL-UUID WORLD RETEST = NOT RUN / V3 MIGRATION = NOT RUN / NOT AUTHORIZED**。利用者へ結果を返し、Minecraft起動前で停止する。
- 9/8 セットアップ完了後の再確認: **ISOLATED PRISM REOPEN / PREFLIGHT PASS / WAITING FOR HUMAN MSA AUTHENTICATION**。人間の「完了」操作後、隔離Prism 10.0.5の実GUI/accessibilityで検証instance `FHR Source Current Wrapper Control 20260906 195153`1件だけ、元`1.20.1`/他instance非表示、右上「アカウント」、総プレイ時間0秒を確認。元instance/metadata/world/ZIP/pristine/MOD/shared runtime/cacheと、明示承認済み新baselineの元launcher直下`prismlauncher.cfg`に内容・size・mtimeの追加変更0。既存mtime差分は原因不明・無害未判定の履歴として残す。10:35:17 JST、隔離Prism PID26340のみ継続、Java/javaw 0件。人間MSA認証前で停止し、Minecraftは起動しない。**MSA AUTHENTICATION = NOT RUN / WORLDLESS SMOKE = NOT RUN / HISTORICAL-WRAPPER AUTHENTICATED COMBINATION = NOT TESTED / ORIGINAL-UUID WORLD RETEST = NOT RUN / V3 MIGRATION = NOT RUN / NOT AUTHORIZED**。
- 9/8 隔離Prism再開pre-flight: **STOPPED BEFORE REOPEN - ORIGINAL LAUNCHER CONFIG MTIME BASELINE DIFFERENCE / USER CONFIRMATION REQUIRED**。明示承認された隔離launcherを起動する前の読み取り確認で、元launcher直下`prismlauncher.cfg`のmtimeだけが9/6終了baselineから9/7 17:57へ更新済みと判明。hash/sizeは同一、原因未確認。元instance.cfg/mmc-pack.json・元instance全13,351 files・world/ZIP/pristine/MOD/shared runtime/cache・元metacacheは不変。検証instance/104 Jar/world持込0も維持。今回はPrism/Java起動0回で、**ISOLATED PRISM REOPEN / PREFLIGHT PASSは未成立**。9/6の`ISOLATION PRE-FLIGHT = PASS / SESSION CLOSED`はその当時のcheckpointとして維持し、今回の差分を新たな隔離失敗やコード不具合とは断定しない。**MSA AUTHENTICATION = NOT RUN / WORLDLESS SMOKE = NOT RUN / ORIGINAL-UUID WORLD RETEST = NOT RUN / V3 MIGRATION = NOT RUN / NOT AUTHORIZED**。
- 9/6 本日の終了checkpoint: **ISOLATION PRE-FLIGHT = PASS / SESSION CLOSED**。今回専用の隔離Prismだけを通常終了し、21:43:19 JSTにprocess終了、21:43:45に対象processなし・Java/javaw 0件を確認。元Prismは操作せず維持。終了後も元instance.cfg/mmc-pack.jsonのhash・size・mtime、元instance全13,351 files/共有runtime/cache、world/ZIP/pristine/MODは今回baselineから差分0。検証instanceと104 MOD Jarを保持、world持込0。**MSA AUTHENTICATION = NOT RUN / NEXT SESSION / WORLDLESS SMOKE = NOT RUN / NEXT SESSION**。本日は完全停止。次回は隔離Prism起動→人間のMicrosoft正規認証→`<MINECRAFT_ACCOUNT> / MSA`確認→原本保全確認→承認済みworldless smokeの順。前回metadata変更履歴は解決済みにしない。**HISTORICAL-WRAPPER AUTHENTICATED COMBINATION = NOT TESTED / ORIGINAL-UUID WORLD RETEST = NOT RUN / V3 MIGRATION = NOT RUN / NOT AUTHORIZED**。
- 9/6 CURRENT-WRAPPER CONTROL再開: **STOPPED - ORIGINAL INSTANCE METADATA WRITE DETECTED / WORLDLESS SMOKE NOT RUN / NOT PASS**。本文承認に基づき新instanceを作成し、104 Jarと許可3設定のsource hash一致を確認した。Prism一覧へ反映するためlauncher本体だけを通常終了・再表示した際、元`instances/1.20.1/instance.cfg`のmtime更新と元`mmc-pack.json`の内容変更を検出したため、Minecraft起動前に中止。元cfgは同一hash、mmc-packはLWJGLの`cachedVolatile: true`削除だけを静的比較で確認。原本全体不変とはしない。world/ZIP/pristine/前回boot/MOD/game configは不変、20:05:06 JST Java0件。元設定のrestore・追加起動は行わず停止。**HISTORICAL-WRAPPER AUTHENTICATED COMBINATION = NOT TESTED / ORIGINAL-UUID WORLD RETEST = NOT RUN / V3 MIGRATION = NOT RUN / NOT AUTHORIZED**。
- 9/6 CURRENT-WRAPPER SOURCE CONTROL準備: **BLOCKED - INSTANCE CREATION DENIED BY PERMISSION REVIEW / WORLDLESS SMOKE NOT RUN**。最新添付依頼に基づく別枠の認証済みcurrent Wrapper試験を準備したが、workspace外の新Prism instance作成は権限レビューが前回の静的監査禁止を理由に2回拒否。実行前拒否のため新instance未作成、Minecraft起動0回。104 MOD Jarは既存隔離コピーからsource hash一致で準備可能だが、設定3ファイルは過去起動後の差分があり、原本から当該3ファイルだけを読み取りコピーする許可も確認中。原本instance.cfgを新試験の基準にしていない。既存UUID照合は維持。**HISTORICAL-WRAPPER AUTHENTICATED COMBINATION = NOT TESTED / ORIGINAL-UUID WORLD RETEST = NOT RUN / V3 MIGRATION = NOT RUN / NOT AUTHORIZED**。
- 9/6 identity/Wrapper静的追確認: **ORIGINAL ACCOUNT UUID STATICALLY CORROBORATED**。9/5 online-mode serverの初回/再接続で、`User Authenticator`がLeva9846を`<PLAYER_UUID>`と確定しlogin/joinした既存logを照合。現在の未起動runtimeの認証試験とは分離する。旧hash一致のinstance.cfg copyは探索範囲内0件で、完全差分/原因は未確定。旧Wrapperはlocalに存在しclone側custom component候補を整理したが、優先順位/更新/共有cache書込隔離の根拠が不足し、**BLOCKED - AUTHENTICATED HISTORICAL-WRAPPER ISOLATED LAUNCH PATH NOT STATICALLY ESTABLISHED**。READYへ昇格しない。今回の原本保全照合18,196件は差分0（前回cfg差分を復元/解決したものではない）。**ORIGINAL-UUID RETEST = NOT RUN / V3 MIGRATION = NOT RUN / NOT AUTHORIZED**。
- 9/6 元UUID再試験pre-flight: **BLOCKED - ORIGINAL ACCOUNT UUID / AUTHENTICATED RUNTIME IDENTITY REQUIRED / WORLD NOT OPENED**。利用者の「新しい隔離copy・正規元identity・通常outboundで1回」の条件付き承認を受領したが、Prism UIの`<MINECRAFT_ACCOUNT> / MSA / 準備完了`だけでは今回使う認証済みruntime UUIDを確認できず、旧Wrapperへの安全な認証済み起動経路も未確立。Minecraft起動0回、boot copy作成前に停止した。さらに元`instances/1.20.1/instance.cfg`が前回保護台帳から変化（18:28:40、原因未確定）しており、**SOURCE INSTANCE CONFIG DIFFERENCE / NEEDS REVIEW**。world/ZIP/pristine/MOD/runtimeは不変だが、instance全体不変とはしない。**V3 MIGRATION = NOT RUN / NOT AUTHORIZED**。
- 9/6 full boot停止理由の追加静的レビュー: **STATIC REVIEW COMPLETE / NO RETEST / USER DIRECTION REQUIRED**。ProjectEはHIGH_ALCHEMIST参加表示用UUID一覧取得がHTTP(S) loopback proxyへの接続で失敗した経路をJar/JDK/保存logから特定。残る234 ERRORは保存時のsource-worldセッションに対して**HISTORICAL BASELINE MATCH 231 / PARTIAL MATCH 3 / NEW・UNEXPLAINED 0**。過去の存在を無害・修正済みとは判定しない。試験UUIDによる別保存・外部ownership未確認、通常load/save時の本1冊・XP20追加等の差分、MTR既存wildcard listener/Firewall確認事項を分離した。**前回full bootのNOT PASSは維持、Minecraft/world再起動なし、通信変更なし。V3 MIGRATION = NOT RUN / NOT AUTHORIZED**。
- 9/6実v2.2.5 worldコピーfull boot: **FULL BOOT ATTEMPTED / STOPPED - WORLD LOAD ERROR REVIEW REQUIRED / NOT PASS**。明示承認された新規`build/verification/source-world-boot-20260906-172148`でworld選択、integrated server起動、player spawn・world描画には到達したが、ProjectE UUID CheckerのFATALマーカー付きERRORとsmokeにないworld読込ERRORを検出したため、修復/再試行せず通常保存・終了した。17:37:03 JST exit0、17:40の追確認でprocess残留なし。Food Healingの指定値・型付きcapabilityは保存前後一致。原本world/ZIP/Prism設定/MOD等とpristine copyは不変。全体PASS・全MOD正常とはしない。**V3 MIGRATION = NOT RUN / NOT AUTHORIZED**。詳細は下記終了監査。
- 9/6 source runtime隔離再現（full boot前の履歴）: **SOURCE V2.2.5 RUNTIME REPRODUCTION / SMOKE PASS（worldなしの起動・終了に限定、既存ERRORあり）**。MC1.20.1 / Forge47.4.0 / Microsoft Java17.0.15 / 旧Wrapper`prism-2025-12-07` / Food Healing2.2.5でmain menu表示、source全116 ID/version一致、通常終了exit0を確認。初回の隔離デスクトップ上の通常終了不成立と限定process停止は失敗履歴として残し、可視デスクトップの再実行と分離する。原本不変、world持込0。**この時点ではFULL REAL-WORLD BOOT = NOT RUN / WAITING FOR EXPLICIT USER APPROVALだったが、その後の承認・試行結果は上記へ分離。V3 MIGRATION = NOT RUN**。元MOD群の非致命ERROR・config監視thread例外、歴史的byte同一性未証明は残り、無条件の安全宣言・release candidate化はしない。
- 対象: Minecraft 1.20.1 / Forge 47.2.0系 / Food Healing RPG v3.0.0
- 最新成果物: `build/libs/foodhealing-3.0.0.jar`
- 最新成果物確認時刻: 2026-09-09 19:58 JST
- 最新成果物SHA-256: `AB9B75DC97C095774B68A416D66A23E5795B635CC4473CF1E46D2383A946BB36`（196,263 bytes / 123 entries）。9/8の`2074EF...F90B`（240,357 bytes / 119 entries）は過去試験の対象identityとして各履歴に保持。
- 9/6 source version provenance追確認: **STATIC RECONCILED / FULL WORLD BOOT NOT RUN**。Pamの`0.0NONE`はJar内Manifest配置と元Forge/Javaの読み取り経路によるfallbackと特定。既存113件のmetadata一致を維持し、Minecraft/Forge 2件とPam 1件を別根拠で照合した。保存当日log対LoadingModListは**116一致 / 0不一致 / 0表記由来未確定**。ただし保存時Jar hash台帳は見つからず、全116件の歴史的byte同一性やfull migrationのPASSではない。ForgeWrapperの現キャッシュと旧起動記録にも版差があり、full bootの既存環境gate・利用者の明示承認待ちは維持。コード/構成/原本を変更せず静的確認のみで停止する。
- 9/6実旧world監査: **provenance確認・実Food Healing NBT保持・実capability fixtureの別JVM保存再読込はPASS**。原本/コピーZIP、保護world 1,440 files、展開181 filesはhash/mtime不変。test-only追加後build/unit/check/vanilla39件PASS。full world bootは**NOT RUN / BLOCKED - SOURCE MODSET / VERSION ENVIRONMENT REQUIRED**、OPEN-01/05は未決定。実旧backup未提供という旧BLOCKEDだけを解消し、full migration・実2-client・外部MOD統合は解禁/代替PASSしない。今回の指定範囲は完了して停止する。
- 9/6再開監査: 既存TEST_PLANに残るEnd gateway経路をGameTestで補完し、vanilla **39/39 PASS**。Gameplay修正なし。9/5手動試験Jarとの差分はP0 GameTest関連3 classとmanifestのみで、その他class/resourceはbyte一致。過去MANUAL PASSは元のJar・条件の履歴として維持する。現在の未完了gateから、それ以上に独立して安全に進める修正対象は今回の監査では特定しておらず、追加人間試験を要求せず停止する。
- Git状態: この作業環境には `.git` がないため、branch、commit、diffによる履歴確認はできない。
- `OPEN-01 / OPEN-05`は2026-09-08利用者決定でRESOLVED / LOCKED。残るOPEN-02/03/04とTrue Root予約済み途中OFFは未決定、関連gateを維持する。
- 安定化完了後の追加仕様候補2件を文書化したが、`NOT LOCKED / NOT IMPLEMENTED`であり、現在の実装・試験優先順位には加えていない。
- 既存確定スキルBreak Realmの外部高難度MOD Adapter方針を`BREAK REALM ADAPTER EXPANSION SPECIFIED / NOT IMPLEMENTED`として文書化した。現在のvanilla release blocker解消後まで実装しない。
- 実2-clientは`BLOCKED - SECOND MINECRAFT ACCOUNT REQUIRED`。別枠の**`MANUAL SINGLE-CLIENT DEDICATED INTEGRATION TEST / STEP 1〜6 / COMPLETE - PASS WITH RECORDED SCOPE AND CONNECTION HISTORY`**。今回の正規1-client/専用serverで接続・GUI同期、根性Lv1購入/OFF、リンゴ1食による進行/SP同期、死亡Respawn、正式IPv4再接続後の保持、正常終了・最終log確認を完了した。serverは18:31:39に保存完了・終了コード0、clientは人間の通常終了後に18:35:57 JSTの`Stopping!`、18:37:51に両process終了を確認。client終了前基準以降のlogに新規ERROR/FATAL・該当例外・crash reportなし。**本日の作業は終了し、再起動・新しい試験/実装は行わない。** IPv6別接続先refusedは正式試験外、初回14:32 TIMEOUTと18:21の途中IPv4切断は原因未確定履歴を維持し、完全解決や実2-client代替PASSとはしない。全体は引き続きIN PROGRESSでリリース候補ではない。

### 日付付きフェーズ履歴

> 履歴の集計です。現在の判定・未完了・次の作業は[現在の要約](#現在の要約)を参照してください。

<a id="evidence-ammo-automated"></a>

#### 2026-09-09 Ammo Conservation / LOCK解決後の実装・自動統合 / COMPLETE

- **仕様**: `foodhealing:tacz_ammo_conservation`、Lv1〜10/各5SP/計50SP、節約率10〜100%、専用damage x1.05〜1.50、Lv1から過熱無効を維持。今回決定された取得default ON・OFF→ON・ON中active gun持ち替え時の既存heat/lock一度解除をSPEC10.8/SKILL_TREE_SPEC11/COMPATIBILITY_POLICY4/TEST_PLAN16へLOCKEDとして反映。OFF時は過去heatを復元せず以後の標準heat lifecycleへ戻す。今回のheat判断待ちだけ解決し、他OPEN/未LOCKは変更なし。
- **実Jar/call path**: 承認TaCZ 1.1.7-hotfix2 / SHA `FC5F1DAB09AFD5399604DB0F60845D41D46CF015C158CBC12E7EF716CF6BDB46` / 52,426,062 bytesを使用。`GunDrawEvent`のserver側current gunとmainhand同一性でactive transitionを検出。`ModernKineticGunScriptAPI.lambda$shootOnce$2`はGunFire取消後、実弾薬消費1回、heat、pellet生成という順。ここだけでserver RNG判定し、失敗/OFFは元`reduceAmmoOnce`へ委譲、成功時は弾を減らさない。発射後refund、tick restore、全inventory scan、custom NBT修復はなし。heatはTaCZ公開API `setHeatAmount` / `setOverheatLocked`で対象銃だけ正常化し、射撃時は同methodのheat branchと`handleShootHeat`を有効中だけ抑止する。既存damage/hit倍率pipelineは不変。
- **manual-action / sync**: 保持した実弾はTaCZ薬室に残し、Food Healing側は弱参照の一時mechanical-cycle flagだけを持つ。`LivingEntityBolt`の開始可否/最後の保持弾のavailabilityを限定補助し、TaCZ通常bolt timing/完了callbackを通す。実薬室が既に装填済みなのでnative完了は2発目を装填しない。`LivingEntityShoot`はpending中の再射撃と同一baseTimestampの既受理timestamp再送を拒否。client `LocalPlayerBolt`はS2C slot/gun ID/pending通知だけを参照し、弾薬・inventoryは変更しない。heat/弾薬はvanilla canonical slot packetと通常inventory同期で通知。protocol **5→6**。client専用hookは承認Jarの実bytecodeでchamber2/magazine1/inventory1 callsiteを確認したが、**CLIENT RUNTIME / prediction表示はNOT RUN**。
- **gate/optional/data**: ModList/FMLLoadingModListの正確なversion文字列とAdapter初期化完了で購入可否を決める。1.1.7-hotfix2以外へ範囲を拡張しない。mandatoryなserver Mixin target3件を解決してからreadyにする。不在はOPTIONAL_MOD_MISSING、非対応/未readyはIMPLEMENTATION_PENDINGでSPを消費しない。TaCZ参照はcompat/taczとversion条件付きMixinへ隔離、local compileOnly、runtimeは既存opt-inだけ、外部Jar同梱なし。vanilla41件で不在起動・購入拒否/SP不変PASS。非対応versionの実Jarは今回ロードせず、exact-string分岐の静的監査のみ。保存schema/legacy migration計算変更なし、既存canonical skill/SP/toggleを使用し、銃のammo/heatをFood Healing capabilityへ保存しない。
- **RED→修正履歴**: `heat-red.log`はfixtureのLazyOptional引数誤りによるcompile FAIL、修正後`heat-red-runtime.log`で狙いどおり未実装heat解除FAILを確認。`heat-green`→`ammo-initial`で段階PASS。`ammo-full1`はfixtureの相対shootTimestamp=-1が実cooldown経過前だったためmanual bolt開始FAIL、-5000/実cooldown0 assertへ修正し`ammo-full2`でPASS。`verified-tacz-write`では実308弾stack_size48に対してfixture64を要求してFAIL、実maxStackSizeによる分割へ修正。fresh opt-in GameTestのserver.properties不足ERRORも設定準備条件の漏れを修正し、ERROR/FATAL検査を追加。これらの失敗log/receiptなし/意図的fixture停止によるFATAL・crash履歴は削除しない。
- **新規回帰内容**: pure unit10段階×1000 midpoint=10,000sample、threshold直前/境界、NaN/Infinity/負数/範囲外Lv、不在gate。実TaCZ固定seedで1物理shot=1抽選、10pelletも1回、6-shot burstは6回、空銃/取消/非消費shotは0回、既存hit/penetration split/Pre取消は追加抽選0。Lv1の300shot stressで保存/消費両方発生（fixture大magazine1000→729。標準Glock容量の主張ではない）。closed/open/manual、最後の薬室、magazine0/5、通常bolt完了/OFF復帰、inventory ammo、通常/tactical reloadの総数保持、native shoot timestamp replayを確認。heat A〜Mは購入10×5SP・購入再送無消費、重複ON/同じdrawで再解除なし、他銃全NBT不変、ON200shot熱0/弾数不変、OFF180shotで360/lock→native cooldown→再射撃1発消費。外部MOD射撃/装填APIとtest専用合成playerを使用した自動試験であり実2-clientではない。
- **保持回帰**: 新Ammo fixtureは通常PlayerList/PlayerDataStorageのsave/readを使用。Lv10/未使用17/使用済61/OFF、食義Lv1234567890123/count456、M700 magazine5+chamber、非装備Minigun heat123/lockを別JVMで照合。実PlayerList.respawnでplayer再生成/capability clone/全HP復帰もPASS。専用world内のfixture API設定だけで、利用者のNBT/worldを編集していない。
- **最終TaCZ command**: `.\gradlew.bat build foodHealingUnitTest check runGameTestServer runServer -PfoodHealingTaczIntegration=true -PfoodHealingRestartPhase=write -PfoodHealingRestartRun=tacz-ammo-verified2-20260909-190319 -PfoodHealingRestartEulaAccepted=true --offline --console=plain`。`verified2-tacz-write.log`、build/unit/check PASS、TaCZ GameTest41/41（19:53:01）、既存13件＋新回帰＋respawn/write PASS（19:53:23）、正常保存19:53:24、exit0 / BUILD SUCCESSFUL59s。write receipt `58391129-28a3-4f6e-810b-e173b663a6c3`、PID14788。
- **最終read command**: 同じopt-in/root/EULA引数で`runServer -PfoodHealingRestartPhase=read`。`verified2-tacz-read.log`、既存13件＋新Ammo＋respawn/read PASS、19:54:17完了/19:54:18保存終了、exit0 / BUILD SUCCESSFUL31s。receipt `550632c6-81c0-434d-8eba-8684c45825b6`、PID6644。token/phase/statusを検証し、write/read別PID・両process終了を確認。Gradle exit0単独では判定していない。
- **最終vanilla command**: `.\gradlew.bat build foodHealingUnitTest check runGameTestServer -PfoodHealingGameTestRun=ammo-final-verified2-20260909-190319 --offline --console=plain`。配布GameTest除外後の`verified2-vanilla.log`でbuild/unit/checkと**41/41 PASS**（19:57:48）、通常終了/exit0 / BUILD SUCCESSFUL25s。既存件数を減らさず、optional absenceの既存1件をready境界まで拡張。TaCZ専用新caseは別のtaczTest suiteでありvanilla件数へ合算しない。
- **log/crash**: 最終3 command logとdedicated/TaCZ GameTest/vanilla GameTestのlatest/debugはERROR0/FATAL0、必須Mixin/dependency/classloading/registry失敗なし、最終2root crash report/JVM crash0。TaCZ write全command WARN166（fresh GameTest83＋server83）、read7、最終vanilla7。Forge language-provider mods.toml4件、dev refmap1件、union assets URL2件、fresh config default生成を分類して保持。consoleのterminal機能WARN、Gradle9非互換deprecated-feature通知、Netty任意reflection fast-path不可のDEBUG例外2種も残るため「warning/exception全0」ではない。required=true/defaultRequire1と各hook require1/2を維持。実Minecraft clientは起動せず、残るJava PID25520は既存Gradle daemonで、試験server PIDは残っていない。
- **成果物監査**: 初回Jarに既存main GameTest class9件が入っていたため、build.gradleのJar excludeだけで除外し、テストは維持。中間Jar `DECA1FA3...F957F66F`/258,376 bytes/132 entriesから、最終`AB9B75DC97C095774B68A416D66A23E5795B635CC4473CF1E46D2383A946BB36`/196,263 bytes/123 entriesへ更新。TaCZ class/Jar、test-only/restart fixture、外部world/NBT、ExampleModなし。metadata3.0.0、required Mixin/refmapあり。除外後のvanilla41件PASSを再確認。TaCZ write/readのproduction classと最終Jarのproduction classは同じ最終ソースであり、最後の差分は配布対象除外だけ。
- **保全/監査reader**: 開始backup `pre-change.zip`あり。監査初回はZIP directory entryを削除fileと誤表示し、ConvertFrom-JsonのDateTimeとISO文字列castのUTC/local差で全mtimeを誤検出。hash/bytesとUTC ticks比較で原因確認後、readerだけ修正。`final-audit-first-failed.json`を残して再実行`final-audit.json`/exit0。通常run1,323 files、昨日TaCZ world25 filesは集合/SHA/size/UTC mtime差分0、TaCZ Jarも不変。元Prism/world/ZIP/pristine/R0908/V3M0908は未起動・未編集で、今回その全rootの追加総走査PASSまでは主張しない。
- **変更ファイル**: mainの`FoodHealingMod`/`FoodHealingSkills`/既存optional absence GameTest、`compat/AmmoConservationChance`/`TaczAmmoCompatibility`/`TaczMixinPlugin`/`compat/tacz/TaczAmmoAdapter`/`compat/tacz/client/ClientTaczBoltState`、`mixin/tacz`4 class、`network/PacketHandler`/`PurchaseSkillPacket`/`ToggleSkillPacket`/`TaczBoltStatePacket`、`foodhealing.mixins.json`、`build.gradle`。testの既存`ShokugiDataUnitTest`/`TaczIntegrationVerification`、新`AmmoConservationRegression`/`TaczAmmoConservationVerification`/`TaczAmmoRestartVerification`/`TaczAmmoTestPlayer`/`TaczClientHookBytecodeVerification`。文書はSPEC/SKILL_TREE_SPEC/COMPATIBILITY_POLICY/TEST_PLAN/CODEX_STATUS、新監査rootのreader/log/evidence。全30ソース・文書変更の一覧は`final-audit.json.changedFiles`。既存FoodHealing damage、Root/Heroics、Food Production、HUD、Satisfaction等は改変していない。
- **残る実client範囲と停止**: 実GUI購入gate・SP/toggle、熱済み銃の取得/ON/持ち替え時HUD/lock反映、Glock/M700/Minigunの保持弾表示・通常reload・auto-bolt予測/アニメーション・遅延通信での整合は別途明示承認後のREAL CLIENT試験待ち。今回のS2C canonical payload照合やclient bytecode検査は、実client handler/画面/入力タイミングの実行結果に代替しない。今は追加人間操作を要求せず完全停止。**REAL CLIENT AMMO CONSERVATION = NOT RUN / ALL TACZ COMPATIBILITY = NOT TESTED / ALL GUNPACK COMPATIBILITY = NOT TESTED / RELEASE CANDIDATE = NO / V3M0908 FOURTH BOOT = NOT RUN / NOT AUTHORIZED**。

#### 2026-09-09 Ammo Conservation / TaCZ 1.1.7-hotfix2 実装前監査 / USER DECISION REQUIRED

- **承認範囲**: 9/9の利用者指示で弾薬非消費/overheat Adapter、購入gate、自動統合、build/artifact確認までの実装開始を承認された。ただし同指示section0/18の「heat/toggle等の正式仕様が不足する場合は実装前停止」を適用し、今回は静的監査で停止。実銃client再試験・昨日のworld再起動・V3M0908 FOURTH BOOT・他version/custom pack/他機能は許可範囲に加えていない。
- **確定事項**: `SKILL_TREE_SPEC.md` section11、`SPEC.md` section10.7/11、`COMPATIBILITY_POLICY.md` section4、`TEST_PLAN.md` section16と実codeを照合。IDは`foodhealing:tacz_ammo_conservation`、Lv1〜10、各5SP/計50SP、節約率10/20/30/40/50/60/70/80/90/100%、専用damage x1.05/1.10/1.15/1.20/1.25/1.30/1.35/1.40/1.45/1.50、Lv1から対象過熱ゲージ無効。既存damageは`getEnabledLevel`を参照し、toggleはserver playerのcanonical `DisabledSkills`更新と既存syncを使用。TaCZ base +0.01x/SPや非TaCZ skillから独立。数値変更・新しいtoggle追加は不要で、実装済みdamage部分は変更していない。
- **停止理由**: 正本には、銃に既存heat/`OverHeated=true`がある状態でスキルを取得/ONにした場合、またはON中に過熱済み銃を装備した場合の状態遷移がない。既存heat/lockを通常冷却まで残す方式と、有効化時点で限定的に解除する方式はプレイヤーが観測する結果が異なる。どちらも今回LOCKしない。毎tick heat/lock reset、lockを残したserver射撃許可、client HUDだけの偽装、NBT直接書換で回避しない。**この期待結果はunit/GameTestでは選択できない仕様判断であり、追加の人力銃試験を要求するものではない**。
- **実物の根拠**: 承認Jar `libs/tacz-1.20.1.jar`のSHA `FC5F1DAB09AFD5399604DB0F60845D41D46CF015C158CBC12E7EF716CF6BDB46`/52,426,062 bytes、metadata1.1.7-hotfix2を再確認。`javap -p -c`で`LivingEntityShoot.shoot`のoffset524 `isOverheatLocked`→532〜535 `OVERHEATED` returnがserver `GunShootEvent`（610）より先であることを確認。`LocalPlayerShoot.shoot`にも421の独立check→438〜441 returnがあり、連続射撃側にもlock checkがある。`handleShootHeat`は新規加算/上限/lockを所有し、これだけを抑止しても既存lockは解除されない。`ModernKineticGunItem.defaultTickHeat`→`tickLocked`は通常の遅延・冷却後、heatが0に達した時にlock解除する。今回の不足はこの実経路と正本の照合で特定したもので、client再起動や試射による推測ではない。
- **弾薬監査の到達点**: `reduceAmmoOnce`のmanual-action/closed-bolt/open-bolt、薬室/magazine/inventory分岐を確認。manual-actionでは薬室clearもこの処理が所有するため、method全体を単純cancelして成功扱いにするhookは採用しない。consume→refundやtick restoreも採用していない。**全reload/script/prediction/packet replayのcall path監査・hook選定は完了扱いにせず、仕様決定後に継続**。ammo exactly-once、bolt/reload総数保存、heat同期の新規回帰も未実装。
- **購入gate/optional境界**: `FoodHealingSkills`のTaCZ不在`OPTIONAL_MOD_MISSING`、導入時`IMPLEMENTATION_PENDING`を維持。対応version+Adapter readyでのみ解除する設計はまだ実装しておらず、version gateや新AdapterのTaCZ不在classloading PASSを今回新規に主張しない。
- **build / unit / check / vanilla GameTest / TaCZ GameTest / integration / restart**: 今回はすべて**NOT RUN**。仕様不足時の実装前停止なのでRED test追加も行わない。既存vanilla41/41、TaCZ13 integration cases、別JVM、9/8実銃baselineの過去PASSは日時・対象を維持し、今回AdapterのPASSへ転用しない。runClient/runServer/Minecraft起動なし。Java/javawは18:57:02の読み取りsnapshotで0件。
- **成果物・保全**: 既存FHR JarはSHA `2074EF581795164F54C032A8B8EEC6FEF2DD7BB9799DB057E04E2ABA8E84F90B`、240,357 bytes、119 entriesのまま（再buildではなくidentity確認）。9/8最終台帳に対し`src`88 files、通常`run`1,323 files、昨日のTaCZ test world25 filesは集合/SHA/size差分0、build.gradle/gradle.properties/TaCZ Jar/FHR JarもSHA/size一致。今回、外部原本全rootのmtime総再走査は行っていないため、その追加監査PASSは主張しない。元Prism/world/ZIP/pristine/R0908/V3M0908は未起動・未編集・未コピー。
- **変更ファイル**: `docs/CODEX_STATUS.md`と新監査rootの`inspect.ps1`/`static-evidence.json`/`REVIEW.md`/更新前Status backupだけ。production Java/Mixin/Config/resource/dependency/build設定/既存test/正本仕様変更0。新監査scriptはread-onlyで対象を比較し、結果は新rootだけへ保存する。外部Jar/classを配布物へコピーしていない。
- **次の作業**: 利用者による「既存heat/lockがある時の効果有効化」の仕様決定を待つ。その後だけ残る実call path監査→最小hook/RED test→Adapter/gate→vanilla/TaCZ/別JVM/成果物監査を今回承認範囲内で再開する。現時点は**AMMO CONSERVATION / NOT IMPLEMENTED / USER DECISION REQUIRED**。**REAL CLIENT AMMO CONSERVATION = NOT RUN、ALL TACZ COMPATIBILITY = NOT TESTED、ALL GUNPACK COMPATIBILITY = NOT TESTED、RELEASE CANDIDATE = NO**。他のOPEN/保留機能へ移らず停止する。

#### 2026-09-08 migration最終回帰・別JVM・成果物監査 / COMPLETE

- **今回の範囲完了**。OPEN-01/05は利用者の正式決定を根拠に解決し、推測ではない。実装の正本は`docs/SPEC.md` section11。原本/ZIP/pristine/R0908/元Prism/隔離Prism/source2.2.5 runtimeを操作していない。実worldをserverへ渡さず、既存抽出済みlevel.dat/playerdataの読み取りコピーからFood Healing capabilityだけをvanilla fixtureへ入れた。元抽出2ファイルはhash/size/mtime不変、コピーもhash一致。
- **追加で発見した不正入力境界と修正履歴**: 初回41件PASS後、型不正Lvの`getLong`正規化値が既存legacy効果fallbackへ渡り得ることを発見。unitは`invalid input inferred legacy skill effect variant=3`で再現FAIL（`logs/migration-invalid-effect-red-20260908.log`）。raw完全保持/pending購入停止を維持し、未検証rawから旧効果計算用Lv/countを推測しないよう同じ`ShokugiData`だけで修正。正常legacy/fresh schema4の数式・効果は変更していない。同一rawの再送では後続購入を巻き戻さないguardも追加し、単体/GameTestで固定した。
- **最終build / foodHealingUnitTest / check / vanilla runGameTestServer = PASS**。command: `./gradlew.bat build foodHealingUnitTest check runGameTestServer -PfoodHealingGameTestRun=migration-final-safe-20260908 --offline --console=plain`。17:03:35に全**41/41** required PASS、通常停止、exit0 / BUILD SUCCESSFUL 40s。既存39件+test-only migration2件。log: `logs/migration-final-safe-regression-20260908.log`。unitは正常long7境界/count4境界/旧sublevel7組合せ/不正14ケース/pending/購入2・3・4SP/backup・replay保持と、既存5,000境界取引/1,000legacy往復をPASS。Gradle標準JUnitは従来どおり実対象0で、custom unit PASSと区別する。
- **最終synthetic別JVM = PASS**。`runServer -PfoodHealingRestartPhase=write`→`read`、`-PfoodHealingRestartRun=respec-safe-20260908 -PfoodHealingLegacyDisk=true -PfoodHealingRestartEulaAccepted=true --offline --console=plain`。専用root `build/verification/restart-respec-safe-20260908`。PID14860（17:05:52）→PID30396（17:06:10）、両exit0 / fresh・legacy2名と旧ディスク5ケースをPASS。正常保存/全dimension保存/通常停止を確認。Inventory/EnderItems/ForgeData/foreign modifier/Diversity/raw保持、正Lvの1:1返還、正常Lv0完了、不正入力pending保持を検証。log: `logs/migration-safe-synthetic-write-20260908.log` / `migration-safe-synthetic-read-20260908.log`。これは実2-client認証/人間操作ではない。
- **実抽出capability別JVM = PASS**。最終root `build/verification/real-v225-migration-20260908-170400`。`foodHealingRealLegacyAudit -PfoodHealingRealLegacyRun=20260908-170400`（offline）で116 source IDs/provenance、level/playerの同一実Food Healing payload、各100回メモリ往復を確認。その後同run IDの`runServer` write→read（承認済みEULA、offline）を実行。PID25212（17:05:15）→PID29220（17:05:34）、各exit0、成功receiptと通常保存停止を確認。log: `logs/migration-safe-real-audit-20260908.log` / `migration-safe-real-write-20260908.log` / `migration-safe-real-read-20260908.log`。原本worldのForge47.4.0/MOD構成をv3で起動した試験ではなく、Forge47.2.0 vanillaのcapability fixtureに限定する。
- **実payloadの独立NBT終了監査**: Lv2/count35、未使用SP2/使用済0、AcquiredSkills空、BaseStats空、DisabledSkills空、Root全runtime値0、pending=false。LegacyV2Backupは元compoundと**型付き完全一致**。Diversity All28/Current3/bonus10を保持。write/read時のcanonical capsは一致し再返還なし。2種payload SHAは`1DE57C89C90BD5172C034BED522EC600101790F71B9D23AD7C374F084FA56533`。配布外のbounded read-only NBT parserでも保存後値を別確認した。
- **成果物監査PASS**: 最終Jar 119 entries / 240,357 bytes / SHA `2074EF581795164F54C032A8B8EEC6FEF2DD7BB9799DB057E04E2ABA8E84F90B`。バックアップJarからのentry追加/削除0、byte差分は`ShokugiData.class`と`META-INF/MANIFEST.MF`のみ。新規test-only fixture/実旧world NBT/外部Jar・class/ExampleMod混入0。`required=true`、defaultRequire1、全Mixin class/refmap/JSON構文を確認し、Mixin設定/refmapはbaselineとbyte一致。metadata3.0.0維持。途中artifact SHA `CFF1E305117D3BDE99F20F0D43EFC0DDB90863BD686F4A14643A0CB49E6E10BA`は追加不正効果guard前の履歴で、最終成果物ではない。
- **終了log/process**: 最終build/GameTest・実capability/synthetic全write/readでERROR/FATAL 0。必須Mixin/起動/dependency失敗なし。既存Netty optional Unsafe DEBUG例外、JarJar情報なしDEBUG、Forge初回テストconfig生成、Gradle9 deprecation/端末warningは記録し黙殺・require=0化しない。17:09のfixture各PIDは残留0。残るJava PID22588は`gradlew --status`で**Gradle8.1.1 IDLE daemon**と確認し、Minecraft残留と混同しない。runClient/Prism/Minecraft client起動なし。
- **変更ファイル一覧**: productionは`src/main/java/com/leva/foodhealing/capability/ShokugiData.java`のみ。unitは`src/test/java/com/leva/foodhealing/{ShokugiDataUnitTest,DataBoundaryRegression,LegacyRespecRegression}.java`。test-onlyは`src/restartTest/java/com/leva/foodhealing/{RestartPersistenceVerification,LegacyDiskVerification,RealLegacyCapabilityVerification,LegacyMigrationGameTests}.java`。新規はLegacyRespecRegression/LegacyMigrationGameTests。`build.gradle`はGameTestへtest-only source追加だけ。文書は`CODEX_LOOP_PROMPT.md`、`docs/{SPEC,SKILL_TREE_SPEC,AUDIT_REPORT,TEST_PLAN,CODEX_STATUS}.md`。Config/resource/Mixin/依存/既存skill倍率・SP価格/Root18・15秒・予約制限は未変更。
- 証拠: 最終rootの`audit/final-artifact-and-nbt.json`、`final-log-summary.json`、`input-manifest.json`、`disk-*-write/read.json`、`changed-tracked-files.json`。検証スクリプト/JSONはbuild配下のみで配布対象外。先行run `real-v225-migration-20260908-165300`と`restart-respec-20260908-1652`/`restart-respec-final-20260908`のPASS、初回unit/compile FAILも消していない。
- **残件/次**: 実旧worldへのv3差し替え/bootは別途利用者の明示承認待ち。無効・編集済みpendingの合算/置換は未推定でreview待ち。OPEN-02/03/04、True Root予約済み途中OFF、実2-client BLOCKED、外部MOD統合/未完成Adapter、sourceの既知ERROR・metadata未解明履歴は維持。既存MANUAL PASSを再試験へ戻さず、追加人間操作を要求しない。今回の作業は完了して停止し、TaCZ実銃/Flight/Ammo Conservation/Break Realm/試作型機関弩へ進まない。**REAL V3 MIGRATION WORLD TEST = NOT RUN / V3 OLD-WORLD BOOT = NOT RUN**。

#### 2026-09-08 OPEN-01/05正式決定・migration実装フェーズ

- 正式決定を`docs/SPEC.md` section11、`AUDIT_REPORT.md`、`SKILL_TREE_SPEC.md`、`TEST_PLAN.md`、`CODEX_LOOP_PROMPT.md`へ反映。**OPEN-01 / OPEN-05 = RESOLVED / LOCKED**。旧Lv L/count CをL/Cのまま未使用SP L・使用済0へ全返還し、skill/base stat/toggleはfresh、Root runtimeは生成しない。旧sublevelからnodeを自動取得せず、通常購入cost2/3/4を維持する。OPEN-02/03/04とTrue Root予約済み途中OFFは未決定。
- Production変更は`capability/ShokugiData.java`のみ。schema4・既存pending・raw backupを再利用し、正常unversioned旧入力を代入によって1回返還。正常schema4は再返還せず、backupは成功後も完全保持。未知schema/負値/欠損/浮動小数/型違いはraw保持・SPを推測生成せずpendingで購入停止。既存pending schema4はrawとのlevel/count一致・未使用/使用済0・skill/base statなしの場合だけ完了し、編集済みscaffoldを上書きしない（未対応としてreview待ち）。別のLOCKED legacy conversionとの衝突なし。
- 回帰追加: `src/test/.../LegacyRespecRegression.java`（正常long7境界、旧sublevel組合せ7、異常12、pending scaffold、raw defensive copy・再deserialize・購入2/3/4・再返還なし）、`ShokugiDataUnitTest`/`DataBoundaryRegression`期待値更新。`src/restartTest/.../LegacyMigrationGameTests.java`は実Player NBT/clone/respawn/syncと通常購入/replayの2件。`build.gradle`はGameTest runに配布対象外restartTest source setを追加し、新規fixtureをproduction Jarへ混ぜない。
- 失敗履歴: 新規unitは変更前コードで`respec state mismatch`を検出（`logs/migration-decision-red-20260908.log`）。実装後unitはPASSしたが、新GameTestの`IFoodDiversityData.serializeNBT()`呼出しが存在しないAPIのためcompile FAIL（`logs/migration-decision-unit-20260908.log`）。Player通常saveのForgeCaps比較へ修正し、次の全回帰でPASS。既存機能や数式変更で回避していない。
- **build / foodHealingUnitTest / check PASS、vanilla GameTest 41/41 PASS**（16:48:29、16:48:32通常保存終了、exit0 / BUILD SUCCESSFUL）。旧39件を維持してmigration2件追加。log: `logs/migration-decision-regression-20260908.log`、隔離root: `build/verification/gametest-migration-respec-20260908-1648`。unit固定seed5,000境界とlegacy1,000往復も維持。ERROR/FATALなし。既存Netty optional Unsafe DEBUG例外・JarJar metadataなしDEBUG・Gradle9 deprecation等は記録し、必須Mixin失敗と混同しない。
- 次: 既存restart fixtureとsynthetic旧ディスクfixture、抽出済み実capabilityの新rootでのwrite/save/stop/別JVM read、artifact混入/hash監査。まだ変更後の別JVM/実payload fixture結果は未記録。Prism/Minecraft client起動なし、元world/ZIP/pristine/R0908/source runtime未操作。**REAL V3 MIGRATION WORLD TEST = NOT RUN / V3 OLD-WORLD BOOT = NOT RUN**。

| フェーズ | 状態 | 概要 |
| --- | --- | --- |
| 1. Baseline build / audit | 完了 | 仕様、旧実装、`AUDIT_REPORT.md` を照合し、Priority 0/1を優先して修正。 |
| 2. Data / migration / persistence / sync | 承認済みrespec自動検証・実copy初回/第二/第三boot限定PASS | long化、安定ID、同期、raw退避、SP管理command。OPEN-01/05の旧Lv1:1返還をfixture/別JVMと実copyで確認。初回migration、第二boot冪等性、第三bootの返還SP通常購入・toggle・1食後保存を各承認範囲でPASS。全MOD保存互換性や任意環境の安全性へ拡張しない。 |
| 3. GUI / keybind / HP HUD | GUI entry三境界・複数Lv GUI・HUD追加手動PASS | 通常/Creative Inventoryの`S`起動と他containerの誤起動なしを`MANUAL TESTED / PASS`。HP HUDは通常/複数行chat、chat非表示、`T`/`/`入力中、GUI Scale Auto/1/2/3/4で表示・可読性・画面内配置を手動PASS。修正前FAILは解決履歴として保持。 |
| 4. Food healing / AlwaysEat / Diversity | Satisfaction・食事credit後続回復の限定手動PASS | 既報の満腹時食事、HP/食義/Diversity exactly-once、Satisfactionの手動PASSを維持。食事後の独立した非食事Food Level +1でHP +2を追加手動確認。元不具合の厳密なtick境界は既存GameTest PASSと分離。 |
| 5. Base stats / damage pipeline | Heroics極端値2境界数値手動PASS・統合試験待ち | 所有modifierの冪等再構築とrespawn/dimension現在HP復元を自動回帰済み。Heroics OFF時のvanilla Armor上限維持と、Lv5 ON時の極端Armorでdamageが負値/0へ崩壊しないことを報告条件で数値手動PASS。外部Attribute MODは未試験。 |
| 6. Existing skills / Root / Heroics | 通常Heroics Lv1〜5・True Heroics x64数値手動PASS | 通常Heroics Lv1〜5のoutgoing、専用DR、Armor/Toughness x2/x4/x8/x16/x32を実クライアントのvanilla damage pipelineで数値手動PASS。True Heroics x64もbase 0.25/0.25、HP70%、既知の専用DR99%との合成理論値で数値手動確認済み。True Root / True Heroicsの既存手動PASSと自動試験履歴を維持する。 |
| 7. Food production / loot / durability | vanilla crafting追加操作手動PASS | Food Production Masteryの3x3 normal/shift click、2x2 inventory crafting、toggle OFF、未取得、材料1recipe分消費、Stack上限/overflowを`MANUAL TESTED / PASS`。furnace/smoker/campfireは自動試験と分離し、外部MOD統合は未試験。 |
| 8. Optional compatibility adapters | 部分実装・Break Realm設計のみ確定 | optional classの直接参照を除去し、互換処理を分離中。Break Realm Adapter expansionは`SPECIFIED / NOT IMPLEMENTED`で、対象Jar/source/API監査と実MOD統合試験は未実施。 |
| 9. Full regression / release candidate | 実1-client dedicated・実copy限定試験完了・リリース候補ではない | 9/8最終vanilla GameTest41件/別JVM/実抽出fixture PASSを維持し、今回再実行なし。TaCZ 1.1.7-hotfix2導入38件/限定13ケースは9/5の範囲のまま。実1-client STEP1〜6、実copy初回/第二/第三bootの限定結果を分離。接続未解明履歴、実2-player、OPEN、全MOD統合・保留実装は未完了。 |

### 過去の完了・試験記録

> 履歴の集計です。現在の判定・未完了・次の作業は[現在の要約](#現在の要約)を参照してください。

<a id="evidence-migration-third"></a>

#### 2026-09-08 第三boot / 限定Gameplay Smoke・通常保存・最終監査完了

- **限定結果**: **REAL V3 MIGRATED WORLD / THIRD BOOT / LIMITED REAL-CLIENT PLAYABILITY SMOKE / PASS WITH RECORDED GAMEPLAY DELTAS**。実client試験だが主体は混合で、食事/短い移動はHUMAN、GUI購入/toggle/通常終了はCOMPUTER USE。利用者が操作していない部分をMANUAL TESTEDへ昇格しない。下記は第二保存後baselineとの比較であり、migration fixtureや全MOD機能検証とは別。
- **証拠root**: `build/verification/real-v3-playability-20260908-204540`。`verdict.json`の37条件が成立。これは保存済み証拠の照合で、37件の新規GameTestを実行した意味ではない。`saved-comparison.json`、`supplemental-state.json`、`random-sequence-review.json`、`carryon-tick-review.json`、`actions.json`、各manifestに前後値と未解決範囲を保持する。
- **実runtimeと起動回数**: 正規MSA <MINECRAFT_ACCOUNT>、元UUID `<PLAYER_UUID>`、MC1.20.1/Forge47.4.0/Microsoft Java17.0.15/Food Healing3.0.0。実WrapperはPrism log490行のclasspath由来`prism-2026-08-01`、104 top-level Jar/想定116 ID-version一致、指定v3 SHA `2074EF581795164F54C032A8B8EEC6FEF2DD7BB9799DB057E04E2ABA8E84F90B`。Java PID27180/start20:48:36.5660256、integrated server20:50:27.838、login20:50:39.208。1回の起動/1回のworld loadだけで、操作支援待ちの間も同じsessionを維持した。account file/token/完全command line未読取。

##### 操作と視覚証拠

| 操作 | 時刻・主体・観測範囲 |
| --- | --- |
| world/HUD・通常Inventory→FHR GUI | 20:51 world描画、20:52 E→SをCodex操作。Lv2/count35/1000/未使用2/使用済0。表示1000は既存source設定で、今回Config/閾値変更なし。 |
| 既登録food1個 | 人間が乾燥昆布1個をRoot購入前に食べた。21:00:19.690 server log1件、21:02画面照合。33→32、count35→36、Lv2、HP50/50。満腹時食事成立。`Healed 2.0 HP`は計算量で、満タンのため実HP増加は0。追加食事なし。 |
| Root Lv1通常購入 | 21:05:02.769 CodexのGUI購入1回。未使用2→1、使用済0→1、Lv1/5/default ON。返還SPを使用し、command/NBT/debug補正なし。他skill/次Lv購入なし。 |
| ON→OFF→ON | 21:05:26頃OFF、21:05:46頃ON、各通常クリック1回。画面でOFF/ONとSP不変を確認、最終ON。 |
| 通常移動 | 人間が室内約30秒の前後左右歩行/視点/ジャンプ数回/短いsprintを報告しEsc pause。21:14:13報告checkpoint、21:14:14画面保存。Portal/機械・追加食事未操作。Codexが連続して全移動を観測した試験ではない。 |
| 任意interaction | **SKIPPED / NO CLEAR SAFE NEARBY TARGET SELECTED**。対象を探し回らず、戦闘/設置回収/機械/外部MOD固有操作を追加していない。 |

- **F2画像7枚**: 対象instanceの`minecraft/screenshots/2026-09-08_20.51.10.png`（world/HUD）、`20.52.52`（初期GUI）、`21.02.13`（食後）、`21.05.16`（購入後ON）、`21.05.34`（OFF）、`21.05.57`（最終ON）、`21.14.14`（移動後pause）。各略時刻は同じ`2026-09-08_<時刻>.png`形式。全file hashを終了後target manifestに記録。
- **移動の裏付けと限界**: 保存statsはwalk +7,005cm、sprint +1,853cm、最終位置は(-31.5,63,48.5)→(-34.28041174155593,63,48.18537584949149)、Rotationも変化。これは累積移動距離で、開始点からの最終変位は約2.8block。全移動経路の最大半径は計測しておらず、室内という人間報告と区別する。ジャンプは人間が操作したとの報告だが、`minecraft:jump`は2384→2384で増加なし。**ジャンプ回数の数値検証PASS・全フレームのrubber-band/freeze非発生確認へ拡張しない**。原因断定や追加人間試験要求はしない。

##### 保存後canonical・外部data

- **Food Healing**: schema4、Lv long2/count long36、未使用long1/使用済long1、取得skillは`foodhealing:guts` Lv int1の1件のみ、BaseStats空、DisabledSkills空（最終ON）、Root累積/予約/期限/active/cooldown0、pending byte0。第二保存時との差分は1食のcount+1、購入1SPの未使用-1/使用済+1/Root追加だけで、全typed期待値に一致。SP合計2のまま、再返還なし。
- **raw/Diversity**: LegacyV2Backupは第二保存時および元v2.2.5 compoundと型付き完全一致、旧Lv2/count35をそのまま保持。DiversityはAll28/Current3/MaxHealthBonus10・全typed履歴一致。昆布は既登録のため新種追加なし。新しいmigrationやraw再計算を行っていない。
- **player/Inventory/属性**: 元UUID一致、唯一のplayerdataとlevel.dat Playerが全typed一致。Inventoryは**slot8の乾燥昆布Count byte33→32だけ**、他slot/item/NBT/装備/EnderItemsは完全一致。HP50、FoodLevel20、XP Lv71/XpP0.10395006090402603/XpTotal13417/Score13417、selected8不変。12属性は順序以外一致し、max HPのbase20 + Food Diversity10 + 他MOD20を保持。foodExhaustion0.11278649419546127→1.965786337852478は移動後の差分として記録。
- **時間/effect**: play_time/time_since_death/time_since_restは+5,311ticks（約265.55秒のactive時間）、leave_game+1、total_world_time+28,501。人間支援待ちpauseを含むwall時間をactive試験時間と混同しない。火炎耐性8532→3221で-5311、残り3409だった水中呼吸/暗視は期限経過後消失、他effect値不変。warden ticks1305→6617、他warden値不変。overworld/他5dimensionのraid Tick218117→223494、他typed fields不変。Chunk Loadersは同UUIDのactivity時刻1788866356063→1788869664248のみ。
- **Carry Onの差分**: `CarryOnData.tick` int0→3799だけが変わり、type=INVALID/keyPressed0/selected8は不変。現在実在する`carryon-forge-1.20.1-2.1.2.7.jar`のbytecodeで`CarryOnDataManager.setCarryData`がPlayerの`f_19797_`をtickへ格納して同期することを確認し、既存MC1.20.1 mappingの119071行で`tickCount`に対応すると照合。同期用tick差として分類し、carry中item/所有者消失とはしない。実際の呼出元/時刻やCarry On操作互換を検証した意味ではない。Jarをcopy/改変せず、必要箇所のみ読み取り参照。
- **乱数差分**: `data/random_sequences.dat`はglow_squid/skeleton/zombieのsource値だけ変更。既存ローカルMC1.20.1 source参照の通常Xoroshiro遷移16/10/4stepで各保存後値に完全一致、他typed fields一致。**EXPECTED NORMAL RUNTIME DIFFERENCE / SAVED RNG ADVANCED**。誰がいつ乱数を呼んだかは計測していない。source参照は開発cacheのmapped47.2.0であり、実runtime47.4.0を変更した意味ではない。
- **既監査外部保存範囲**: 18 capabilityは15 typed一致、2 list順序のみ（ProjectE knowledge/solcarrot）、1期待Shokugi差。ProjectE EMC41809152/knowledge/bags、ProjectExpansion、TaCZ、Curios、TConstruct、SlashBlade、Flux、mcjty、Mekanism等を保持。Sophisticated Backpacks、Flux SavedData、RFToolsDimensions、JourneyMap settings、WorldUUID、dimension capabilities、cosmetic armor、advancementsも対象範囲で保持。25 NBTとMCA内record22,161→22,158をbounded decodeしerror0、元UUID参照7件は同じ場所/型、旧試験UUID参照0。record総数変化から全entity意味論を検証したとはせず、全外部MOD機能・間接/opaque ownershipの網羅PASSにも拡張しない。

##### ログ・正常終了・保全

- **操作別log**: `actions.json`にGUI/食事/購入/toggle/移動後/終了の時刻を保持。latest/debugともERROR250/WARN385/FATAL0、250件すべて第二bootとlogger/message/resource/exception/主要stack/full normalized stack一致で**STARTUP / WORLD BASELINE MATCH**。食事INFO1件・購入成功表示は**GAMEPLAY ACTION RELATED**、pause保存/MTR catch-up等のINFOと上記保存差分は**EXPECTED NORMAL RUNTIME DIFFERENCE**。新規重大ERROR/説明不能ERROR・Food Healing deserialize/save/network/GUI購入例外・必須Mixin/dependency/registry全体失敗・crash reportは検出0。
- **既知異常は維持**: ModernFix/NightConfig `ConcurrentModificationException`は第二bootと14行stack一致で再発。Yuushya/TicEX/SlashBlade/Mekanism等250件も無害・修正済み扱いにしない。6/30比較231一致/3 PARTIALの履歴も維持。ProjectE UUID Checker例外は非再発。今回Firewall画面は観測されず、rule/proxy/OS設定操作なし。
- **通常終了**: 21:14:24.247 Save & Quitによる通常切断、21:14:24.354 MTR listener停止、21:14:24.365 Stopping server/Saving players/Saving worlds、21:14:24.721 All dimensions are saved。main menuへ戻り通常「終了」、21:14:48.640 `Stopping!`。21:15:06および21:28:13 Java/javaw0を確認。同じ隔離Prism PID25172/start17:23:40のみ継続。強制killなし、OS exit codeは独立取得していないためexit0とは記録しない。
- **原本/cold保全**: 元instance13,351/world181/ZIP/pristine181/旧copy186/source runtime16,750、shared meta31/cache4,384/libraries453/assets12,049/root2、source104 Jar、source2.2.5 control4,564 files（R0908含む）/承認metadata、指定v3成果物のfile集合/SHA/size/mtime追加差分0。cold-secondboot181 filesも完全不変・未起動。元metadataの過去差分は未解明の履歴のまま、restoreしない。
- **今回保存された範囲**: V3M0908は181→181 files、追加/削除0、content変更28/mtimeのみ42。v3 instanceは4,463→4,472 files、F2画像7+log archive2追加、削除0、既存content変更63/mtimeのみ239。world/cache/map/log/instance利用時間等の通常生成物であり、runtime/libraries/assets/MOD本体は不変。原本変更や手動Config編集と分離する。
- **変更/検証/停止**: `docs/CODEX_STATUS.md`、今回audit root内のread-only解析helper/JSON/manifest/cold backup、対象world/instanceの通常生成物だけ。Java/Mixin/resource/Config/仕様/build設定/テストソース変更なし、build/unit/check/GameTest/runServer再実行なし、既存41/41・別JVM等の自動PASS不変。**FOURTH BOOT = NOT RUN / NOT AUTHORIZED**。ALL MOD GAMEPLAY COMPATIBILITY = NOT TESTED、HISTORICAL-WRAPPER AUTHENTICATED COMBINATION = NOT TESTED、RELEASE CANDIDATE = NO。OPEN-02/03/04/True Root未LOCK、実2-client BLOCKEDと別機能gateは維持。承認された第三bootだけを完了し、追加試験・実装へ進まず完全停止。

#### 2026-09-08 第三boot継続 / 人間の1食・通常GUI購入・toggle照合

- **同一session**: 前段pauseからJava PID27180/start20:48:36.5660256、隔離Prism PID25172/start17:23:40を維持。launch/load追加0、world再作成0、command/Config/NBT操作0。人間は乾燥昆布を1個だけ食べ、追加購入等なしでEsc pauseしたと報告。
- **食事の限定判定**: `HUMAN-OPERATED FOOD ACTION / MANUAL TESTED / PASS`は、昆布33→32・count35→36・Lv2維持・HP50/50維持の報告範囲のみ。21:02:07頃の実画面でも32個/count36/HP50/50を照合。server log21:00:19.690にNutrition1の食事処理1件あり。`Healed 2.0 HP`は計算量のlog表記で、実Health増加の証拠にはしない（満タン）。Diversity/canonical/Inventory全体の保存後照合はMinecraft完全終了後まで保留。Root購入より前に1食、追加食事なし。
- **通常GUI取引**: 21:02にE→Sで開き、Lv2/count36/1000・未使用2・使用済0を確認。一覧を根性までscrollし、21:05:02.769購入成功表示。Codexの通常GUIクリック1回で根性Lv1/5/default ON、未使用1/使用済1。次Lv購入や他skill操作なし。21:05:26頃にON→OFF、21:05:46頃にOFF→ONをそれぞれ1回。各結果は実画面、前後log時刻、native F2で確認。最終Lv1/5/ON/SP1/使用済1/count36。**REAL CLIENT / COMPUTER-OPERATED GUI PURCHASE + TOGGLE / OBSERVED PASS**であり、人間MANUAL PASSや保存後canonical PASSではない。
- **視覚証拠**: `minecraft/screenshots/2026-09-08_21.02.13.png`（食後）、`2026-09-08_21.05.16.png`（購入/default ON）、`2026-09-08_21.05.34.png`（OFF）、`2026-09-08_21.05.57.png`（最終ON）。前段2枚と合わせ6枚を保持。今回4枚のSHA/size/mtimeは`purchase-paused-checkpoint.json`へ記録。
- **操作log差分**: 食後・GUI前・購入前後・OFF/ON前後はいずれもERROR250のまま。21:06:47のlatest/debug ERROR250/WARN385/FATAL0、全250件は第二bootbaselineのfeature/full stack一致、新規ERROR・Food Healing/capability/GUI購入例外・必須Mixin/dependency/crash0。既知ModernFix/NightConfig例外は維持。pause解除時のMTR simulation catch-upと通常pause保存INFOは操作時刻に沿うruntime差として記録し、修復していない。食事実処理1件、購入成功表示1件。
- **原本/cold保全**: `purchase-paused-protection.json`で元world/ZIP/pristine/旧copy/元通常Prism/source104 Jar/shared runtime/cacheの追加差分0、source2.2.5 control4,564 filesと承認metadata/v3成果物も不変。cold181 filesのfile集合/SHA/size/mtime差分0。稼働中V3M0908の保存後NBT比較は未実行。
- **pauseと残作業**: 21:06:45.127 Escゲームメニューでpause確認。同じsession維持。所定の操作A（前後左右歩行・視点・数回jump・短いsprint）は前段の離散入力だけでは十分に確認できず、既存GameTest/NBT比較では実clientの継続キー入力・rubber-band・freezeを代替観測できない。Computer Useに長押しAPIがないため、この30秒程度だけ人間支援を要求する。開始室内、portal/機械には触れず、追加食事/購入/戦闘なしで実施し、Esc pauseへ戻す。任意interactionは安全な通常container等を探し回らず**SKIPPED / NO CLEAR SAFE NEARBY TARGET SELECTED**。移動結果受領後、通常保存・終了・canonical/raw/Diversity/外部data/原本保全の最終監査へ続行する。
- **変更/検証**: Statusと監査rootの読取checkpoint helper/JSON、通常runtime log/save、F2画像のみ。scroll初回は必須scrollX引数不足で入力前validation拒否となり、再観測後scrollX=0で通常scrollした（ゲームFAILではない）。Java/Mixin/resource/Config/仕様/build変更0、build/unit/check/GameTest再実行不要・既存自動PASS不変。**THIRD BOOT FINAL VERDICT PENDING; FOURTH BOOT = NOT RUN / NOT AUTHORIZED**。全MOD互換/リリース候補へ昇格しない。

#### 2026-09-08 第三boot限定Gameplay Smoke / 長押し入力制約で途中pause

- **証拠/backup**: `build/verification/real-v3-playability-20260908-204540`。第二保存時のworld181 filesと全typed状態、v3 instance4,463 filesを照合し差分0。cold copyは同rootの`cold-secondboot-V3M0908`で、file集合/SHA/size/mtime完全一致、saves外・未起動。起動前とpause後のcold/original/source2.2.5保全は成立。worldを再作成・pristineを再コピー・NBT編集していない。
- **実runtime**: MC1.20.1/Forge47.4.0/Microsoft Java17.0.15/Food Healing3.0.0、指定v3 SHA `2074EF581795164F54C032A8B8EEC6FEF2DD7BB9799DB057E04E2ABA8E84F90B`、104 Jarと実116 ID/version一致。Prism今回log490行の実classpathから`prism-2026-08-01`を抽出確認。アカウントUI <MINECRAFT_ACCOUNT>/MSA/準備完了確認済み、秘密ファイル/完全command line未読取。既存Prism PID25172/start17:23:40を継続し、Java PID27180/start20:48:36.5660256を1回だけ起動。再起動なし。
- **world/操作時刻**: 20:50:22から一覧1件のV3M0908を1回load、20:50:27.838 integrated server、20:50:35.836 MTR listener、20:50:39.208 <MINECRAFT_ACCOUNT> login。20:50:56 world/HP50/50/食義Lv2-count35を確認。今回Firewall画面は観測されず、Firewall設定操作なし。20:51:10 F2 world証拠。20:51:20頃～20:52:23に`s/a/d/space/Control_L+w/space`の離散入力を1回ずつ送信し、後退による小さい描画差を観測。継続歩行・sprint・ジャンプ成立・視点移動の十分な証拠ではないため操作Aの完全PASSにはしない。portal/機械へのclick、combat、block、item操作なし。
- **GUI**: 20:52:32 Eで通常Inventory、20:52:43 SでFood Healing GUI。実画面はLv2・食義35/1000・SP2・使用済0。分母1000は実source環境の表示として記録し、この試験でConfigを変更していない（200へ補正しない）。20:52:52 F2でGUI証拠保存。今回の確認主体はComputer Useであり、既存MANUAL PASSを上書き・拡張しない。
- **食事/購入未実施**: 既保存Inventory slot8の乾燥昆布33個はAllEatenFoods登録済みで、安価なvanilla食料として適格。breadも存在。`FoodDiversityData.addEatenFood`はAll既登録ならCurrentを増やさないことを読取確認。食事操作はまだ0、count35維持、Root通常購入0/toggle0。適切な食料がないことを理由としたSKIPではない。
- **入力blocker**: 提供Computer Useの正式APIはキー押下/クリック/左dragのみで、食事の右ボタン長押しや継続移動のhold/release/duration指定がない。未定義API・native自作入力・Config/keybind変更で回避していない。食事をRoot購入前に行う指定を維持し、GUI→Inventory→通常画面を順に閉じ、20:53:24頃Escで安全なゲームメニューpauseへ移行。root購入を先行せず、同じ第三boot sessionで人間の長押し操作支援を待つ。これはFood Healingの不具合確定ではない。
- **途中log**: `actions.json`のworld描画/移動前後/Inventory/GUI前後はERROR250のままで新規ERROR0。20:54:35時点latest/debugともERROR250、WARN385、FATAL0。第二bootのlogger/message/resource/exception/主要stack/full normalized stackに250件一致し、`STARTUP / WORLD BASELINE MATCH`。ModernFix/NightConfig ConcurrentModificationExceptionは既知stackで再発、無害・修正済み扱いなし。必須Mixin/dependency/ModLoadingException/新規重大ERROR/crash検出0。最終ログではなく途中観測である。
- **保全/未完了**: pause後の元world/ZIP/pristine/R0908/source2.2.5 control4,564 files/承認metadata/source104 Jar/元通常Prism/shared runtime/v3成果物の追加差分0。cold181 filesも完全一致。稼働中V3M0908の保存後NBT読取はまだ行っていない。正常Save & Quit/Stopping!/Java0/保存後canonical・外部data差分の最終監査は未実行。
- **視覚証拠**: target instance `minecraft/screenshots/2026-09-08_20.51.10.png`（world/HUD）と`2026-09-08_20.52.52.png`（GUI）。SHA/size/mtimeを`input-paused-checkpoint.json`へ記録。アカウント画面や認証情報の保存なし。
- **変更/検証**: Status、新audit helper/JSON/cold copy、Minecraft通常生成物とF2画像のみ。checkpoint helper初回実行は同名module import衝突でFAILし、今回helper内のimportを固有名へ限定して再実行PASS。ゲームコード・データ読書き処理の変更ではない。Java/Mixin/resource/Config/仕様/build設定変更0、build/unit/check/GameTest再実行0、既存41/41等は維持。
- **再開地点**: 同じJava PID27180・同じworld sessionのpause状態。まず人間支援で視点をMining Portal以外の安全な黒曜石へ向け、選択中の乾燥昆布を1個だけ通常食事し、直ちにEscでpauseして結果を返してもらう。count35→36/33→32の結果受領・実画面照合後、Codexが同じsessionでGUI通常Root Lv1購入とON→OFF→ONへ続行する。継続移動等の未確認範囲を完全PASSにせず、支援実施部分は主体を区別する。**THIRD BOOT IN PROGRESS / NOT PASS; FOURTH BOOT = NOT RUN / NOT AUTHORIZED**。新機能/別MOD試験/OPEN判断へ進まない。

<a id="evidence-migration-second"></a>

#### 2026-09-08 同じV3M0908の第二boot・再読込・通常保存 / IDEMPOTENCY PASS

- **対象と境界**: `FHR_V3_Worldless_20260908_171804/minecraft/saves/V3M0908`の初回migration保存済み状態をそのまま1回再読込。新world copy0、元world/pristine/R0908未起動、Jar差し替え/UUID入力/NBT編集0。承認は2回目bootのみであり、3回目bootは未承認。
- **起動前照合**: 初回保存後world181 files、v3 instance4,461 filesの集合/SHA/size/mtimeと全取得typed stateが一致。起動前canonical/raw/Diversity期待値一致、原本/比較環境の差分0。詳細は下段pre-flight履歴と`build/verification/real-v3-secondboot-20260908-193700/preflight.json`。
- **UI停止からの回復**: 19:43:14起動のJava PID31244を維持したまま、人間のmain menu screenshotを受領。Computer Use JS kernelだけをリセットして画面取得が復旧した。Minecraft/Prism再起動・instance一覧再読込なし。以前の`foreground window did not report a process id`はツール側の観測失敗履歴として残し、Food Healing不具合とはしない。
- **実runtime**: 正規MSA `<MINECRAFT_ACCOUNT> / MSA / 準備完了`は起動前UI確認済み。実logはMC1.20.1 / Forge47.4.0 / Microsoft Java17.0.15 / Food Healing3.0.0、runtime116 ID/version一致・追加不一致0。104 top-level Jarと指定v3 SHA `2074EF581795164F54C032A8B8EEC6FEF2DD7BB9799DB057E04E2ABA8E84F90B`を前後確認。今回Prism log425行の実classpathからCURRENT Wrapper `prism-2026-08-01`を確認。account/token/password/完全command line未読取。
- **1回load**: 一覧には`V3M0908`1件だけを確認し、選択→通常Playを1回。20:18:28.239 integrated server開始、20:18:39.298 `<MINECRAFT_ACCOUNT>` login、world/chunk描画とFood Healing表示を観測。移動・食事・combat・craft・item/block/container・購入・toggle・command・TaCZ・機械操作0。描画確認後Escでpauseし、通常保存へ進んだ。MTR `0.0.0.0:8888`は20:18:36.025開始したが、今回の観測ではFirewall確認画面は表示されず、rule/通信設定操作なし。
- **正常保存/終了**: 20:19:16に「セーブしてタイトルへ戻る」、20:19:16.203 Stopping server、同.204 Saving players/worlds、同.715 All dimensions are saved。タイトルの「終了」で20:19:37.758 `Stopping!`。20:19:38.825および20:27:05.564にJava/javaw0、元からの隔離Prism PID25172/start17:23:40のみ継続。強制killなし。OS終了コードは独立取得していないためexit0とはせず、通常経路/log/process消滅を根拠とする。

##### 初回保存後と第二保存後の型付き比較

- **canonical冪等性**: Food Healing全compoundがtyped完全一致。`FoodHealingDataVersion` int4、Lv/count/SPはlongで2/35/2/0、AcquiredSkills/DisabledSkills空List、BaseStats空Compound、Root累積/予約/各期限0、pending byte0。SP再返還delta=0、2→4なし、spent生成なし、旧skill/base/toggle自動取得なし、pending再設定なし。`LegacyV2Backup`は初回保存後および元v2.2.5 rawとtag型を含め完全一致し、内容上の再生成/上書き・消失はない。実内部呼出回数を計測した試験ではなく、実reload後canonical保持と既存fixtureを分離して記録する。
- **Diversity/identity**: All28 / Current3 / MaxHealthBonus10、全typed履歴を維持。level.dat Playerと唯一のplayerdataは元UUID `<PLAYER_UUID>`でtyped一致。別UUID playerdata生成なし。
- **player/ownership**: Inventory全slot/item/NBT、EnderItems、選択slot8、XP Lv71 / XpP0.10395006090402603 / XpTotal13417 / Score13417、Health50、FoodLevel20、Pos(-31.5,63,48.5)、Rotation、advancements保持。18 capability中16件typed一致、ProjectE knowledgeとsolcarrot foodの2件は内容同一・list順序差。属性12件も順序以外同一で、max-health base20 + Food Diversity10 + 他MOD所有20を維持。item追加/消失0。
- **外部保存範囲**: ProjectE EMC `41809152`/knowledge/alch bags、ProjectExpansion book locations、TaCZ synced、Curios、TConstruct、SlashBlade各cap、Flux player/SavedData、mcjty preferences、Mekanism radiation、Sophisticated Backpacks SavedData、RFToolsDimensions、JourneyMap settings、WorldUUID、dimension capabilities、cosmetic armorを確認。全外部MOD gameplay/間接ownership/opaque dataの網羅PASSではない。
- **時間/list差分**: 水中呼吸/暗視3720→3409、火炎耐性8843→8532（各-311、他effect値同一）。play_time/time_since_death/time_since_rest +311、leave_game12→13、total_world_time246222→246957（+735）、warden ticks_since_last_warning993→1305、他warden値同一。overworldと他5dimensionのraid Tick217742→218117（+375）、他typed fields同一。Chunk Loadersは同UUIDのactivity時刻1788861641106→1788866356063のみ。これらを無害一般化・自動修復せず具体的差分として保持。
- **追加SavedData差分の確認**: `data/random_sequences.dat`の`minecraft:entities/zombie.source`だけが変化し、他typed fieldsは一致。既存ローカルMC1.20.1 mapped sourceのRandomSequences/RandomSequence/Xoroshiro128PlusPlusを読み取り、初回保存乱数状態から通常遷移4stepで第二保存状態へ完全一致することをメモリ内計算で確認。`EXPECTED NORMAL RELOAD DIFFERENCE / SAVED RNG ADVANCED`と分類。実際の呼出元/タイミングは計測しておらず断定しない。参照sourceは開発cacheの47.2.0 mapped archiveであり実runtime47.4.0を変更していない。source hashと根拠は`random-sequence-review.json`。
- **bounded拡張読取**: 初回保存時と第二保存時で25 NBT、MCA内record22,159→22,161、decode error0。元UUID参照7件は同じ場所/型、旧試験UUID参照0。cosmetic armorは初回/第二保存とも保全済みpristineとtyped一致することから保持を確認。各dimensionの比較基準は初回保存時の既存typed evidenceを使用し、新copyは作っていない。MTR settings6件/region/entity等の通常保存byte差も台帳に記録し、内容全体の無害性や全機能互換は主張しない。
- **ファイル集合**: V3M0908は181→181、追加/削除0、content変更27、mtimeのみ42。監査rootに`boot-before/after-manifest.json`、`boot-before/after-state.json`、`saved-comparison.json`、`idempotency-checks.json`、`supplemental-state.json`を保存。正常保存された今回worldをそのまま保持。

##### ログ・保全・最終判定

- **ERROR比較**: latest/debugともERROR250 = startup16 + world234、WARN385、FATAL0。初回v3 boot250件すべてがlogger/message/resource/exception/主要stack/full normalized stackで**FIRST-BOOT BASELINE MATCH**、欠落/NEW重大ERROR0。保存データの時間/順序/乱数差分は上記EXPECTED NORMAL RELOAD DIFFERENCEとして分離。既存ERRORがあるためclean logとはしない。6/30比較の231一致/3 PARTIAL履歴も昇格・削除しない。
- **停止条件**: Food Healing deserialize/migration/classloading例外、新しい必須Mixin ApplyError/TransformerError/InjectionError、ModLoadingException、mandatory dependency不足、registry/datapack全体失敗、world保存失敗、crash reportを検出せず。ModernFix/NightConfig ConcurrentModificationExceptionは初回と14行stack一致で再発、未解決。既存Yuushya/TicEX/SlashBlade/Mekanism等ERRORも無害・修正済み扱いにしない。ProjectE UUID Checker例外は非再発。MTR listenerは20:19:16.170通常停止。
- **原本保全**: 元instance13,351 files、元world181、ZIP/pristine181、過去copy186、source runtime16,750、shared meta31/cache4,384/libraries453/assets12,049/root2、source104 Jarの追加/削除/SHA/size/mtime差分0。source2.2.5 control instance4,564 files（R0908含む）と承認metadata、v3成果物も一致。元通常Prism/元metadata restore・編集・起動なし。過去metadata差分原因を解明済みにはしない。
- **隔離runtime側の差分**: v3 instance4,461→4,463 files、log archive2件追加、削除0、既存content変更51/mtimeのみ248。worldの27 content変更を含み、instance.cfgの利用時間、log/cache、JourneyMap map、usercache、worldedit properties、`.mixin.out`診断出力等の通常生成物を台帳化。MOD Jar/runtime/libraries/assets本体は不変で、これらを原本変更や手動Config変更と混同しない。
- **変更と検証**: `docs/CODEX_STATUS.md`、今回audit rootの読取helper/JSON、V3M0908とv3 instanceの正常runtime生成物のみ。Java/Mixin/resource/Config/仕様/build設定の手動変更なし。build/unit/check/GameTestは再実行なし、既存41/41・別JVM・抽出capability fixture PASSを維持。新たなゲーム内試験ケースを作らず、保存済み証拠の28条件を照合した（`verdict.json`: allChecks=true）。
- **限定結果**: **REAL V3 MIGRATION WORLD / SECOND BOOT + RELOAD + NORMAL SAVE / IDEMPOTENCY PASS**。**THIRD BOOT = NOT RUN / NOT AUTHORIZED**。ALL MOD GAMEPLAY COMPATIBILITY = NOT TESTED、HISTORICAL-WRAPPER AUTHENTICATED COMBINATION = NOT TESTED、RELEASE CANDIDATE = NO。実2-client BLOCKED、OPEN-02/03/04/True Root未LOCK、TaCZ/Flight/Ammo/Break Realm/試作型機関弩のgateは維持。今回承認範囲は完了、同じ隔離Prismは開いたまま完全停止し、追加試験へ進まない。

#### 2026-09-08 v3第二boot pre-flight・UI観測不能による一時停止

- **承認範囲**: 初回migration済みの同じ`V3M0908`を1回再読込・通常保存して、初回保存後との冪等性を確認する。新copy0、pristine/R0908/元worldは開かない。3回目boot未承認。
- **起動前**: 保存済みworld181 filesとv3 instance4,461 filesの集合/SHA/size/mtime、typed保存状態が初回終了時baselineと完全一致。canonical schema4/Lv2/count35/未使用2/使用済0・skill/base/toggle空・Root runtime0・pending=false、Diversity28/3/10、LegacyV2Backupは初回保存時および元v2.2.5 rawと型付き完全一致。元world/ZIP/pristine/R0908/2.2.5比較instance4,564 files/承認metadata/source104 Jar/元Prism/shared runtime/cache/v3成果物は起動前追加差分0。
- **操作**: 既存隔離Prism PID25172を継続し、アカウントUIの`<MINECRAFT_ACCOUNT> / MSA / 準備完了`を読み取り確認。設定変更・認証操作なしで戻り、選択中のv3を1回通常起動。Java PID31244、開始19:43:14.3439548 JST、対象runtimeのjavaw.exeを確認。account file/token/完全command lineは読まない。
- **途中log**: 実116 ID/version一致・FHR3.0.0・MC1.20.1・Forge47.4.0・Microsoft Java17.0.15。今回Prism log425行のclasspathからWrapper `prism-2026-08-01`のみを抽出確認。19:43:55.754 TitleScreen初期化、19:43:57.759 startup完了のlog。19:45:11観測時ERROR16はFIRST-BOOT BASELINE MATCHで、logger/message/resource/exception/主要stackと正規化full stackも一致。world未読込なので初回world-load234件は未発生であり、消失・改善扱いにしない。FATAL/ModLoadingException/必須dependency/Mixin/新規重大ERROR/crash0。ModernFix/NightConfig ConcurrentModificationExceptionは再発し、無害扱いしない。
- **停止原因**: MinecraftウィンドウID1180330は一覧に存在するが、Computer Useが`foreground window did not report a process id`を返し、ウィンドウ再取得後も画面観測不可。main menu視覚確認、Singleplayer/world選択、world load、player login、正常保存/終了、2回目保存後NBT比較はいずれも未実施。world181 filesの再照合は差分0。Minecraftの不具合・migration FAILとは断定しない。
- **起動後の保全再照合**: `ui-blocked-protection.json`で元instance13,351/元world181/ZIP/pristine181/過去copy186/source runtime16,750/shared meta31/cache4,384/libraries453/assets12,049/root2/source104 Jarすべて追加・削除・SHA/size/mtime差分0。source2.2.5比較instance4,564 files（R0908を含む）と承認metadata、v3成果物も完全一致。19:48:13に同じMinecraft PID31244とPrism PID25172だけが維持されていることを確認し、終了・再起動は行っていない。世界の2回目loadを開始する前の保全確認であり、reload/save PASSではない。
- **変更ファイル/検証**: `docs/CODEX_STATUS.md`と監査root `build/verification/real-v3-secondboot-20260908-193700`の読み取り監査helper/manifest/JSONのみ。Java/Mixin/Config/resource/仕様/build設定変更なし。build/unit/check/GameTest/runServerは今回再実行なし、既存PASS不変。Minecraftの通常runtime生成物は原本と分離。
- **次の操作**: 人間が現在のMinecraftウィンドウを前面に表示するだけ。worldはまだ開かず、同一PIDの画面取得が回復してから承認済みの1回のV3M0908 loadへ続行する。Minecraft/Prism再起動禁止。**SECOND WORLD BOOT = AUTHORIZED / NOT YET RUN / UI OBSERVATION BLOCKED; THIRD BOOT = NOT RUN / NOT AUTHORIZED**。他試験・機能・OPENへ進まない。
- **20:05継続結果**: 利用者の前面表示＋5秒待機後も上記UI取得エラーが再発した。world操作0・同一Java/Prism・runtime116一致・起動ERROR16/新規重大ERROR0を読み取り再確認。画面確認はログだけでは代替できないため、次の最小人間操作を「現在のMinecraft画面のスクリーンショット1枚を提供（worldは開かない）」へ変更。変更はStatusと監査JSONのみ、コード/仕様/Config変更・build/unit/check/GameTest再実行なし。これはmigrationのFAIL確定でも第二bootのPASSでもない。

<a id="evidence-migration-first"></a>

#### 2026-09-08 実v3初回migration・通常保存・終了後監査 / 限定PASS

- **承認範囲**: 未起動pristineから新しい`V3M0908`を作成し、正規MSA/元UUIDで初回v3 bootを1回、通常保存・終了、保存後監査まで。2回目boot/reload・購入・toggle・食事・移動・その他gameplayは行っていない。既存隔離Prism PID25172を終了/再起動/一覧再読込せず使用した。
- **証拠root**: `build/verification/real-v3-firstboot-20260908-185000`。`evidence.py`、`post_save.py`、`finish.py`はこのrootだけへ報告を書き出す監査補助。既存bounded read-only NBT reader・保存比較器を再利用し、NBT編集/再serialize・UUID rename・playerdata merge・修復なし。原本/比較instance側のコードや設定変更なし。
- **boot用copy**: `build/verification/isolated-prism-control-20260906-211429/launcher/instances/FHR_V3_Worldless_20260908_171804/minecraft/saves/V3M0908`。pristineからextended-path `copy2`で作成し、181 filesの集合/SHA-256/size/mtime完全一致、link/junctionなし。R0908/元world/ZIPを起動元にしていない。v3 saves一覧はこの1件だけ。
- **起動前保全**: 元instance13,351 files、元world181、ZIP、pristine181、旧試験copy186、source runtime16,750参照、元共有meta31/cache4,384/libraries453/assets12,049、元launcher設定2件、source104 Jarを照合し追加差分0。2.2.5比較instance4,564 files（R0908含む）と利用者承認済みmetadata SHA/size/mtimeも一致。過去metadata差分の原因・無害性・restoreを新たに断定しない。
- **実runtime**: MC1.20.1 / Forge47.4.0 / Microsoft Java17.0.15 / Food Healing3.0.0、104 top-level Jar、想定116 ID/version一致（不一致/追加0）。v3 Jar SHA-256 `2074EF581795164F54C032A8B8EEC6FEF2DD7BB9799DB057E04E2ABA8E84F90B`。他103 Jar不変・2.2.5との重複なし。CURRENT Wrapperは今回のPrism実classpath行360の`prism-2026-08-01`で確認。account file/token/password/認証command line全文の記録・抽出なし。
- **起動/終了経路**: 18:52:43通常起動、Java PID26304開始18:52:44.345。main menuを確認後、18:54:13 `V3M0908`を1回読込。18:54:17.871 integrated server開始、18:54:29.302 `<MINECRAFT_ACCOUNT>` login。18:54:25.958 MTRの既存`0.0.0.0:8888`開始に伴うFirewall画面はCodex未操作、人間がキャンセル（rule変更なしとの報告）して同processを維持した。復帰後world/chunkとFood Healing食義表示を観測した。ゲームmenuのpause状態で、今回GUI購入/SP表示の追加試験はしない。
- **正常保存・終了**: 19:00:41.019「セーブしてタイトルへ戻る」、19:00:41.268 Stopping server、同.269 Saving players/worlds、19:00:41.767 All dimensions are saved。タイトルから「終了」、19:01:09.596 `Stopping!`。19:01:10および19:04:06にJava/javaw0、同じ隔離Prismのみ継続を確認。OS exit codeは独立取得していないためexit0とは記載せず、通常終了経路/log/process消滅で判定する。再起動・再login・再loadなし。

##### 保存NBT・移行の照合範囲

- **元identity**: 保存後level.dat Player UUIDと唯一のplayerdataが`<PLAYER_UUID>`。level.dat Playerと同UUID playerdataはtyped完全一致。別UUID playerdata生成なし。
- **migration**: `FoodHealingDataVersion` int4、`ShokugiLevel` long2、`EatCount` long35、`UnspentSkillPoints` long2、`SpentSkillPoints` long0。AcquiredSkills/DisabledSkills空ListTag、BaseStats空CompoundTag、Root累積/期限/active/cooldown/予約0、`LegacyMigrationPending` byte0。`LegacyV2Backup`は開始前Shokugi compoundとtag型を含め完全一致（旧Lv/countはint型のままbackup）。旧skill自動取得/旧費用推定なし。`migration-checks.json`に全期待値と実typed値を保持。
- **Diversity**: All28 / Current3 / MaxHealthBonus10、履歴の全typed compoundが一致。Food Healing以外を含む18 player capabilityは、15件typed一致、2件（ProjectE knowledge / solcarrot food）はlist順序だけ、1件（Shokugi）が上記期待migration差。順序非依存比較は補助判定であり元typed差分も保存した。
- **player**: Inventoryの全slot/item/NBTと選択slot8、XP Lv71 / XpP0.10395006090402603 / XpTotal13417 / Score13417、Health50、FoodLevel20、Pos(-31.5,63,48.5)、Rotation、advancementsは一致。item追加/消失なし。属性12件はlist順序以外同一で、max-health base20 + Food Diversity10 + 他MODの`Health Gained from Trying New Foods`20を保持した。
- **外部保存範囲**: ProjectE EMC `41809152`、knowledge（順序以外）/alch bags、ProjectExpansion book locations、TaCZ synced、Curios、TConstruct、SlashBlade、Flux player、mcjty preferences、Mekanism radiation等を保持。Sophisticated Backpacks SavedData、Flux Networks data、RFToolsDimensions、JourneyMap player settings、WorldUUID、各dimension capabilities、cosmetic armorもtyped保持。自動給餌/銃/機械等の機能互換PASSではない。
- **記録した差分**: 水中呼吸/暗視3737→3720、火炎耐性8860→8843で各duration -17、他effect値同一。play_time/time_since_death/time_since_rest +17、leave_game +1、total_world_time +7435、warden ticks_since_last_warning975→993。Chunk Loadersは同UUIDのactivity時刻のみ更新。overworldと他5dimensionのraid Tick217660→217742（+82）、他typed fields同一。list順序/時刻経過を差分として残し、自動修復や無害一般化をしない。
- **拡張読取**: pristine/保存後各25 NBT、region/entity/POI内record22,153→22,159をbounded decode、decode error0。元UUID参照7件が同じ場所/型で保持、旧試験UUID参照0。MTR settings6件やregion/entity等の通常保存content差も台帳に残し、opaque/間接ownershipや全chunk意味論を網羅した互換PASSとはしない。
- **world集合**: 保存前181→保存後181、追加/削除0、content変更26、mtimeのみ42。他は不変。`boot-before/after-manifest.json`、`saved-comparison.json`、`supplemental-state.json`に型付き比較・全差分を保持し、今回copyは保存状態のまま残す。

##### 最終ログ・原本保全・停止

- **ERROR**: latest/debugともERROR250、FATAL0、WARN386。起動16 + world-load234。250件すべてlogger/message/resource/exception/主要stack・full normalized stackで9/8 source2.2.5 world baselineと対応。旧`Failed to extend max health cap: maxValue`1件消失だけが`EXPECTED FOODHEALING VERSION / MIGRATION DIFFERENCE`。CURRENT V3固有/NEW・UNEXPLAINED重大ERROR0。
- world234のうち6/30歴史比較では231一致/3 PARTIALだった履歴を維持。今回source2.2.5との一致によって3件を完全な歴史一致へ昇格しない（`ticex:revival_spellbook`、`ticex:revival_spellbook_irons`、`yuushya:yuushya_guidebook`）。Yuushya/TicEX/SlashBlade/Mekanism/recipe/tag等の既存ERRORを無害・修正済みとはしない。
- **停止条件監査**: ModLoadingException、必須dependency/required Mixin ApplyError・TransformerError・InjectionError、registry/datapack全体失敗、新規Food Healing deserialize/migration/classloading例外、保存失敗、crash reportを検出せず。ModernFix/NightConfig `ConcurrentModificationException`は14行stackがsource2.2.5と一致して再発、未解決として保持。ProjectE UUID Checker例外は非再発。MTR listenerは通常停止した。
- **終了後保全**: 原本world/ZIP/pristine/R0908/2.2.5比較instance/承認metadata/元通常Prism/source104 Jar/shared runtime/cache追加差分0。指定v3成果物・installed Jar・isolated runtime/library/MODも不変。新v3 instanceのJourneyMap32 files、log archive2、username/usercache2の新規生成と、当該instanceのmetadata/log/cache/一部設定の通常更新を原本変更と分離（copy準備後4,425→4,461 files、追加36/削除0）。原本・pristine・R0908を修復/restoreしていない。
- **監査補助の注意**: PowerShell JSON表示は`version`/`Version`の大小文字重複で一度変換拒否されたため、`-AsHashtable`で再読取した。ゲーム/NBTの破損やmigration FAILではなく、保存値を変更せず元キーを維持した。主比較はPythonのcase-sensitive構造で実施した。
- **変更と検証**: 変更は今回copy/v3 instanceの通常生成物、監査root、`docs/CODEX_STATUS.md`のみ。Java/Mixin/resource/Configの手動変更・build設定・MOD差し替えなし。build/unit/check/GameTestは今回再実行せず、既存41/41・別JVM・実抽出fixture PASSをそのまま維持。監査補助の最終21条件が成立（`verdict.json`）。
- **最終限定判定**: **REAL V3 MIGRATION WORLD TEST / AUTHENTICATED ORIGINAL-ACCOUNT / CURRENT-WRAPPER / FIRST BOOT + NORMAL SAVE / PASS WITH RECORDED SOURCE BASELINE DIFFERENCES**。**V3 SECOND-BOOT / RELOAD = NOT RUN / NOT AUTHORIZED**、ALL MOD GAMEPLAY COMPATIBILITY = NOT TESTED、HISTORICAL-WRAPPER AUTHENTICATED COMBINATION = NOT TESTED、RELEASE CANDIDATE = NO。今回copyを保持して完全停止し、次の明示承認を待つ。OPEN-02/03/04/True Root未LOCK、実2-client BLOCKED、TaCZ/Flight/Ammo/Break Realm/試作型機関弩のgateは未変更。

#### 2026-09-08 v3 CURRENT-WRAPPER SOURCE-MODSET WORLDLESS SMOKE完了

- 利用者承認の1回限定worldless smokeを完了。既存の隔離Prism **PID25172 / 17:23:40起動**を継続使用し、今回Launcher終了・再起動・一覧更新の再表示・元通常Prism操作なし。検証instanceは`FHR V3 Source Modset Worldless 20260908 171804` / `FHR_V3_Worldless_20260908_171804`。新規worldコピーも作成しない。
- 起動直前の実GUIで**<MINECRAFT_ACCOUNT> / MSA / 準備完了**、新v3 instance選択を確認。account file/password/access token/refresh token/client token/認証command line全文は取得せず、再認証やOffline Account操作なし。Prism実起動logは今回の開始前261行より後のWrapper tokenだけを限定抽出した。

| 実観測 | 今回の結果 |
| --- | --- |
| 起動 | 18:01:48.384 JSTに通常「起動」を1回。新instanceのJava PID27916は18:01:51.9442595開始。 |
| Minecraft / Forge / Java | 実latest.log 2行Microsoft17.0.15、235〜236行MC1.20.1 / Forge47.4.0。起動前のJava実`-version`もMicrosoft17.0.15+6-LTS。 |
| Food Healing | 実LoadingModList `foodhealing 3.0.0`、MOD/common/client setup完了log。配布元と新instanceのJar SHAは`2074EF581795164F54C032A8B8EEC6FEF2DD7BB9799DB057E04E2ABA8E84F90B`一致。2.2.5 Jar同居なし。 |
| CURRENT Wrapper | 今回`launcher/logs/PrismLauncher-0.log` **295行の実JVM classpath**が`ForgeWrapper-prism-2026-08-01.jar`を使用。準備metadataだけの推測ではない。historical Wrapperを強制していない。 |
| MOD構成 | **想定116 ID/version全一致 / 不一致0 / 追加0**。source116からFood Healingだけ3.0.0へ変更した期待集合と照合。104 top-level Jarは起動前後hash一致、Food Healing以外103はsource同一。source TaCZ1.1.8-hotfix等を任意versionへ変更していない。 |
| main menu | 18:02:30 TitleScreen初期化、18:02:32起動所要40.713秒のlog。18:03:00頃に実画面のMC1.20.1 / Forge47.4.0 / 116 MODと「終了」を確認。Singleplayer/world一覧/Multiplayer/Realms未操作。 |
| 正常終了 | 18:03:17.459にMinecraft「終了」、latest.log **1132行18:03:17.544 `Stopping!`**。18:03:20のprocess確認でMinecraftなし、18:04:09.468にJava/javaw0を記録。隔離Prismのみ継続。強制killなし。 |

- Minecraft本体exit codeを独立取得したとは記録しない。Prism log278行のJava checker exit0は本体とは別であり、**通常Quit・Stopping!・対象process消失**を正常終了の根拠とする。

##### ERROR分類と未解決baseline

- latest/debugとも**ERROR16 / FATAL severity0**。元2.2.5 CURRENT worldlessの17 ERRORに対してlogger/message/resource/exception/stackを比較し、game directoryとobject identityだけを正規化した全stack一致で分類した。単純件数比較ではない。

| 分類 | 結果 |
| --- | --- |
| SOURCE BASELINE MATCH | **16件**。Yuushya mixin minVersion未記載1、Yuushya model5、TicEX model4、SlashBlade model5、Mekanism inputInputSlot1。logger/message/正規化stack全一致。無害・解決済みとは扱わない。 |
| EXPECTED FOODHEALING VERSION DIFFERENCE | 旧2.2.5の`[FoodHealing] Failed to extend max health cap: maxValue` **1件は消失**。旧Reflection cap拡張エラーを新v3へ期待しない。 |
| NEW / UNEXPLAINED | 現在のERRORで**0件**。全MOD gameplay・未ロードworldの安全性を証明するものではない。 |

- **ModLoadingException / mandatory dependency失敗 / required Mixin ApplyError・TransformerError・InjectionError / registry startup failure / classloading致命例外 / crash report = 0**。Yuushyaの既存minVersion記述ERRORは必須Mixin適用失敗と混同しない。新しいFood Healing例外は検出なし。
- **ModernFix / NightConfig ConcurrentModificationExceptionは再発**。latest168〜181行の14行stackをsource2.2.5の保存済みruntime logと照合し全一致。`NightConfigWatchThrottler.java:37` / `FileWatcher.java:162`。INFO/STDERR経由なのでERROR16とは別件であり、main menu到達を妨げなかった範囲だけの観測。config監視の無害性・全機能への非影響は断定しない。
- **WARN186件を保持**。Forge library mods.toml/asset scheme等、FHR `super_food`/`normal_test_food`の既知model欠落warning、初回生成ConfigのCorrecting warningを含む。握り潰し・修復はしない。新instanceのFHR common Configは通常読込でtextが4692→4998 bytesとなったが、sourceとのTOML key/value比較は追加/削除/値変更0。ゲーム仕様やConfig値を手動変更したものではない。

##### 終了後保全と限定判定

- **今回承認された2.2.5 metadata新baselineから追加変更0**。instance.cfg SHA/1930 bytes/mtime17:23:40.6436390、mmc-pack.json SHA/945 bytes/mtime17:23:45.7044788を維持。source2.2.5 instance全4,564 filesの集合/hash/size/mtimeも追加・削除・変更0。以前の36-byte差分・mtime差分は、利用者承認済み比較基準の履歴として維持し、原因完全解明・無害・restore済みへ変更しない。
- **原本保全**: 元通常Prism instance13,351、sharedMeta31/cache4,384/libraries453/assets12,049、元instance.cfg/mmc-pack.json、承認済み元root prismlauncher.cfg、元world181/ZIP/pristine181/旧boot186/source runtime16,750は終了後も差分0。R0908 181 filesとsource104 Jarも不変。元config/defaultconfigs/resourcepacks/Java/cacheを変更せず、v3配布元Jarも指定SHAを維持。
- **新v3 instanceだけの通常生成**: 824→4,244 files、追加3,420/削除0/既存変更92（内容変更3、mtimeのみ89）。内容変更は新instance自身のinstance.cfg/mmc-pack.jsonとfoodhealing-common.toml。追加はTaCZ通常local resource export3,324、JourneyMap89、Mixin出力3、logs2、icon1、Patchouli data1。既存MOD JarとJavaの内容変更なし。TaCZ自動resource exportは通常起動logにあり、実銃試験・Adapter追加・MOD更新とはしない。
- **world持込・生成・接続0**。起動前savesなし、起動後は空のsaves directoryのみ通常生成。world folder/level.dat/region/playerdata/session.lock/.mca/.mcrなし。integrated serverやworld/server接続logも0。空directoryは隠さずworld生成と区別する。
- 証拠root: `build/verification/v3-worldless-20260908-171804/smoke-20260908-175735`。`approved-source-metadata-baseline.json`、`before/launching/after-protection.json`、各original/source-instance台帳、`before/after-target-manifest.json`・`after-target-diff.json`、`startup-logs.json`/`final-logs.json`、`after-launcher-selected.json`、`generated-files-and-selected-logs.json`、`process-after.json`、`ui-and-verdict.json`。初回監査分類は旧messageの`[FoodHealing]`prefixを含めず「NOT REPEATED / REVIEW REQUIRED」としたが、実messageを確認して最終分類だけ修正し、途中reportは保持。ゲーム実装を変えてPASSにしたものではない。
- 変更は監査helper/証拠・Statusと新instanceの通常起動生成物のみ。**build / unit / check / GameTest = NOT RUN（今回再実行不要）**。ゲームJava/Mixin/resource/Config値/build/dependency/tests変更なし、MOD/Launcher download/update/replaceなし、proxy/Firewall/router変更なし。既存41/41・別JVM・実抽出capability等の自動PASSと既報MANUAL PASSは維持。
- 結論: **V3.0.0 CURRENT-WRAPPER SOURCE-MODSET / ISOLATED PRISM / WORLDLESS SMOKE / PASS**。確認範囲はこのsource MOD version組合せでのclient起動/終了のみ。TaCZ正式試験対象1.1.7-hotfix2の実銃、他MOD機能相互作用、旧world保存/移行、全MOD互換性、release readinessへ拡張しない。隔離Prismを開いたまま、ここで停止する。
- **REAL V3 MIGRATION WORLD TEST = NOT RUN**。
- **V3 OLD-WORLD BOOT = NOT RUN**。
- **HISTORICAL-WRAPPER AUTHENTICATED COMBINATION = NOT TESTED**。実worldのv3 migrationは未承認。

#### 2026-09-08 2.2.5比較metadata新baselineの利用者承認・v3起動前再確認

- 利用者から前回2metadata差分を新比較baselineとして受け入れる明示承認を受領。18:00:10.658 JSTに現在値が前回停止時記録と完全一致することを確認し、`build/verification/v3-worldless-20260908-171804/smoke-20260908-175735/approved-source-metadata-baseline.json`へ記録した。過去SHA/size/mtimeと差分は削除せず、metadata/mtimeのrestore/editはしない。
- 新baseline: source2.2.5 `instance.cfg` SHA `A87A87A0D11061265F1C949013CC6DE600193A4DAFE8907E5650F84265256A90` / 1930 bytes / mtime17:23:40.6436390 JST（epoch ns1788855820643639000）、`mmc-pack.json` SHA `313DCBAB3CA09A22498DD5AE92F0E9E5BA4E670A3ECCA57BD531704F112EEBC3` / 945 bytes / mtime17:23:45.7044788 JST（epoch ns1788855825704478800）。**原因完全解明済み・無害・restore済みではない**。
- 原本/共有cache/runtime等の既存保護台帳に追加差分0。source2.2.5 instance4,564 filesは前回停止時から差分0、R0908/元world/ZIP/pristine/source104 Jar不変。新v3 targetは104 top-level Jar、Food Healingだけ3.0.0指定SHA、他103はsource一致、2.2.5との同居なし。MC1.20.1/Forge47.4.0/LWJGL3.3.1 metadata、Microsoft Java17.0.15+6-LTSの実`-version`を確認。world/saves/level.dat/playerdata/region/session.lock0。
- 隔離Prismは前回から同一PID25172（17:23:40起動）、新v3 instance選択済み。今回Launcher終了/再起動/一覧更新操作なし。account store/secret/認証引数全文を読み取らず、既存アカウントの状態表示だけを確認して、許可された1回のworldless起動へ進む。これはまだsmoke PASSではない。
- 変更は監査helper/証拠とStatusだけ。build/unit/check/GameTest再実行なし、既存41/41等のPASSは維持。**REAL V3 MIGRATION WORLD TEST = NOT RUN / V3 OLD-WORLD BOOT = NOT RUN**。

#### 2026-09-08 v3 worldless pre-flight停止・比較基準metadata差分

- **WORLDLESS SMOKE = NOT RUN / PREFLIGHT NOT PASS**。起動前コピー後と隔離Launcher通常終了後は、比較基準2.2.5 instance4,564 filesの集合/hash/size/mtime差分0。新folderが一覧に反映されないため、隔離Prismだけを通常終了し、17:23:40.505 JSTに同じ隔離executableを通常再表示（PID25172）。GUIには旧比較instanceと新`FHR V3 Source Modset Worldless 20260908 171804`の2件だけを確認し、新instanceを単一選択したが、**「起動」は一度も押していない**。元通常Prismを操作せず、account再認証/更新操作もなし。
- 再表示後の保全監査が**2.2.5検証instance metadataの差分を検出してFAIL停止**。これはFood Healingのruntime FAILではなく、比較基準instanceの不変要件を満たさないpre-flight blocker。2.2.5検証instanceへのmanual editは行っていないが、同じ隔離Launcherの再表示に伴う書込みを検出した事実を残す。原本保全すべてPASSとは記録しない。

| 2.2.5検証instance内のfile | 差分 |
| --- | --- |
| `instance.cfg` | SHA `A87A87A0D11061265F1C949013CC6DE600193A4DAFE8907E5650F84265256A90` / 1930 bytesは同一。mtimeのみ12:26:48.5623659→17:23:40.6436390 JST。内容差分0。 |
| `mmc-pack.json` | 981→945 bytes、SHA `8C408637F203A2417A49793A448C405E76A68278FD3B05885B8DE5E84FA9CDD8`→`313DCBAB3CA09A22498DD5AE92F0E9E5BA4E670A3ECCA57BD531704F112EEBC3`。mtime12:08:12.2280479→17:23:45.7044788 JST。`org.lwjgl3` componentの`cachedVolatile: true`行だけ削除。MC1.20.1/Forge47.4.0/Java等のversion変更はなし。 |

- **完全差分の根拠**: 現在のmmc-pack bytesへ、起動前に読み取った`cachedVolatile`行をメモリ上だけで再挿入すると、開始前SHA/sizeと完全一致した。他のbyte差分0を確認。これは監査用のメモリ比較であり、元fileのrestore/edit・mtime復元を行っていない。Launcherの再表示前後に発生したことは証拠で限定できるが、内部の書込み実装経路までは追跡していない。**無害・原因完全解明済み・修復済みとはしない**。
- **それ以外の保全**: 2.2.5検証instanceは追加/削除0、上記以外4,562 files不変。R0908 181 files、source MOD directory206 files（うちtop-level Jar104）、config308/resourcepacks6/Java403を保持。元通常Prism instance13,351、sharedMeta31/cache4,384/libraries453/assets12,049、元instance.cfg/mmc-pack.json、承認済み元root prismlauncher.cfg、元world181/ZIP/pristine181/旧boot186/source runtime16,750は最終読み取り監査でも差分0。元root cfgの過去mtime差分は別の未解明履歴として維持する。
- 新instanceは`build/verification/isolated-prism-control-20260906-211429/launcher/instances/FHR_V3_Worldless_20260908_171804`に保持。104 Jarの最終hashはコピー計画と一致し、Food Healingは指定3.0.0 SHAのみ、他103 Jar同一。world/level.dat/region/playerdata/session.lock0、saves未作成。Prism再表示による新instance metadata正規化は`final-target-diff.json`へ分離して記録。world生成・持込・コピー0。
- MSAは再表示前の実アカウント一覧で`<MINECRAFT_ACCOUNT> / MSA / 準備完了`を確認、再表示後もLeva9846表示。account file/password/token/認証command line全文は読んでいない。Java `-version`はMicrosoft17.0.15+6-LTS、MC/Forgeはmetadata1.20.1/47.4.0、FHRはJar metadata3.0.0までの確認。**今回の実runtime LoadingModList116/実CURRENT Wrapper/main menu/ERROR比較/必須Mixin・dependency・registry/crash判定/Stopping!は未実施**。準備情報や以前の2.2.5 PASSで代替しない。
- **process**: 前段のプロジェクトIDLE Gradle daemon1件は通常`--stop`で停止済み（exit0）。最終確認Java/javaw0、隔離Prism PID25172のみ保持。Minecraftは未起動なのでQuit/Stopping!を捏造せずNOT APPLICABLE。元Prismは起動・終了していない。強制killなし。
- 証拠root: `build/verification/v3-worldless-20260908-171804`。`before/preflight/launcher-closed/launcher-reopened/final-*-snapshot/manifest/protection.json`、`*-source-instance-diff.json`、`copy-plan.json`、`preparation.json`、`target-before-manifest.json`、`final-target-manifest.json`/`final-target-diff.json`、`stop-review.json`、`final-processes.json`。`prepare.py`/`stop_review.py`は検証専用で、ゲームコード・配布Jarへ追加していない。metadata差分検出時の監査exit1は記録し、成功へ握り潰さない。
- **build/unit/check/GameTest = NOT RUN（今回不要）**。直前の41/41・別JVM・実capability fixture・成果物監査のPASSは維持。今回の変更は新instance/監査helper・証拠/Statusと、通常隔離Launcherが上記へ行った書込みだけ。ゲームJava/Mixin/resource/Config/build/dependency/testの変更なし、MOD/Launcher download/update/replaceなし、network/proxy/Firewall変更なし。
- **次回**: この比較基準metadata差分の扱いと、既存2.2.5 instanceを今後変更しない起動方法について、利用者判断・明示承認を受けてから継続する。今回は元fileの復元、保全baseline更新、追加Launcher構築、再起動、Minecraft起動へ進まず停止。OPEN-02/03/04/True Root未LOCK・他機能gate/実2-client BLOCKEDを維持。
- **REAL V3 MIGRATION WORLD TEST = NOT RUN / V3 OLD-WORLD BOOT = NOT RUN**。world migrationは未承認。source2.2.5 CURRENT試験の過去PASSはその当時の結果として保持し、今回をV3互換性PASS/全MOD互換性PASS/リリース候補としない。

#### 2026-09-08 v3 source-modset worldless準備

- 既存source2.2.5 instanceの必要なMOD/config/defaultconfigs/resourcepacks/options、Java、component metadataだけを読み取りコピーし、新instanceを作成。旧instance.cfgは参照のみ、新cfgのname/JavaPath/プレイ時刻だけを新instance用に設定。元通常Prismのmetadataはclone元に使用せず、saves/R0908/pristine/元world/usercache/logs等はコピーしない。2.2.5/3.0.0 Jar同居なし、他103 JarはSHA/size一致、コピー823 filesを照合。instanceは既存完全隔離launcher内の独立directoryであり、通常Prismは操作しない。
- 最初のコピー計画検証は秘密file除外判定がJDK配布物`jmxremote.password.template`と`pkcs11cryptotoken.md`にも一致して停止。新instance作成・コピー前のhelper判定であり、Minecraft FAILではない。配布template/legal textの2pathだけを明示区別し、account/password/token file禁止は維持して再検証。原本の編集・復元なし。
- 起動前`prepare.py before/preflight`で元instance13,351、sharedMeta31/cache4,384/libraries453/assets12,049、承認済み元launcher cfg、元world181/ZIP/pristine181/以前boot186/source runtime16,750の差分0。既存2.2.5 instance全4,564 files（R0908含む）の集合/hash/size/mtimeも差分0。v3 Jarは指定SHA一致。GUIで既存MSAの準備完了を読むだけとし、account file/token/認証引数全文は取得しない。
- コピーしたJavaの実`-version`はMicrosoft17.0.15+6-LTS。前段のIDLE Gradle daemon1件は`gradlew.bat --offline --stop`で通常停止し、build/testは再実行しない。Prismが新folderを一覧に反映していないため、隔離launcherだけを通常終了・再表示してから再確認する。**実Minecraft起動・worldless判定はまだ未実施**。
- 変更は新検証instance・監査helper/manifest・Statusのみ。ゲームコード/Mixin/resource/build設定/dependency/testsの変更なし。既存自動41/41・別JVM・実capability fixture PASSは再実行/書換なし、OPEN-02/03/04/True Root未LOCK・他機能gateを維持する。

#### 2026-09-08 正規元アカウント・CURRENT Wrapper旧world load/save完了

- 1回限定承認に基づく試験を完了。使用環境は**MC1.20.1 / Forge47.4.0 / Microsoft Java17.0.15 / Food Healing2.2.5 / CURRENT ForgeWrapper prism-2026-08-01**。実LoadingModListはsource116 ID/version全一致、不一致/追加0、104 top-level MOD Jarは開始前後hash一致。Wrapperは今回Prism実起動classpathの595行のversion tokenで照合し、metadataだけの推測ではない。Food Healing Jar SHA `5BAC83E55419D96849823D7A7BB0933E01600564E13739992E43782C925A6A8C`、Wrapper SHA `2BE07E9BFD5BD237BEC7511163B06123A055F4697BEE398683C8668C9B37C83E`。
- boot対象は隔離instanceの`minecraft/saves/R0908`のみ。未起動pristineから新規コピーした181 filesの集合/SHA/size/mtime完全一致を確認済み。元world/ZIP/pristine/過去bootは起動せず、今回のbootコピーだけを通常保存した。コピー工程のパス長失敗と完成までの履歴は下記に維持する。
- **Firewall**: 12:10のOpenJDK確認画面ではCodexは操作せず待機。利用者から「キャンセル、rule追加/変更なし」の報告を受け、同じMinecraft process・同じworld loadを継続。system proxy/Firewall/router/port forwardingを変更せず、閉鎖proxyなしの通常outbound条件。再起動・再loadなし。
- **描画/操作**: キャンセル後の最初の画面取得ではInventoryが既に開いていた（開かれた原因・操作主体は推測しない）。CodexはEscapeで閉じ、既存構造物/chunk、Food Healing2.2.5の食養Lv2・count35/1000、EMC表示を実画面で確認後、ESC→セーブしてタイトルへ戻る→Minecraft「終了」のみ操作。移動/食事/戦闘/クラフト/item移動/commandは実施していない。Firewall人間待ちによりload開始から保存まで約16分となり、最小操作確認以外に放置性能・長時間安定性試験を追加したものではない。

| 観測/終了 | 証拠（JST） |
| --- | --- |
| world load要求 | 12:10:05.642、`R0908`を1回だけ選択。 |
| integrated server / player login | 12:10:10.534開始、12:10:21.547 <MINECRAFT_ACCOUNT> login。 |
| 実UUID | debug.log 16603行、12:10:21.811 Jade同期対象が`<MINECRAFT_ACCOUNT> (<PLAYER_UUID>)`。保存後level.dat Player.UUID・同UUIDのplayerdata.datが一致し、両Player全typed NBTも一致。別UUIDのplayerdata新規生成0。 |
| MTR lifecycle | 12:10:18.378 `0.0.0.0:8888`開始、12:26:19.350同connector停止。historical source挙動として記録し、公開安全性/全MTR機能PASSとはしない。 |
| 正常保存 | 12:26:19.152 Save/Quit操作。12:26:19.363 `Stopping server` / `Saving players` / `Saving worlds`、12:26:19.850 `All dimensions are saved`。タイトルへ復帰。 |
| 正常終了 | 12:26:47.518「終了」、12:26:47.578 `Stopping!`。12:26:48.687 PID26276消失・java/javaw 0件、隔離Prism PID26340のみ継続。本体exit codeは別取得しておらず、PrismのJava checker exit0をMinecraft本体exit0とは扱わない。 |

##### 保存data照合

- **Food Healing全capabilityのtyped NBTは完全一致**（未知field含む）。ShokugiLevel2 / EatCount35 / DisabledSkills空 / AllEatenFoods28 / CurrentEatenFoods3 / MaxHealthBonus10保持。NBTの編集・正規化・UUID rename/merge・raw補正は一切なし。
- 実playerの18 capability中16は全typed一致。`projecte:knowledge`と`solcarrot:food`の2件はraw typed比較ではlist順序差あり。補助比較で型・要素・多重度を維持した順序差だけであることを記録し、raw一致とはしない。ProjectE `transmutationEmc="41809152"`、knowledge内容、alch bags、ProjectExpansion book locationsは保持。
- TaCZ synced entity data、Flux player、mcjtylib preferences、Mekanism radiation、Curios inventory、TConstruct persistent data、SlashBlade各capability、Iron Furnaces、Polymorphも保持。source TaCZは**1.1.8-hotfix**の保存保持確認であり、v3正式対象1.1.7-hotfix2の実銃/heat/給弾等へ代替PASSしない。
- `data/sophisticatedbackpacks.dat`、`fluxnetworksdata.dat`、`RFToolsDimensions.dat`、`JMPlayerSettings.dat`、`WorldUUID.dat`、各dimensionの`capabilities.dat`はtyped一致。Sophisticated/Core storageの今回保存data、Flux/RFTools/Mekanism/JourneyMapの記録済み範囲で別player化・初期化を検出しない。Chunk Loadersは元UUID1件を維持し、最終活動timeのみ更新。cosmetic armor全typed dataも一致。
- MCA/NBTの補足読取はpristine **22,153 chunk records/25 NBT files**、保存後 **22,160/25**、decode failure0。元UUID参照7箇所は同一file/pathに維持（JourneyMap、Chunk Loaders、mining dimension村人Gossips Target、Player UUID等）、以前の試験UUID参照0。これは既知UUIDの型付き検索範囲であり、間接参照/全外部MOD gameplay/opaque MTR payload全意味の安全性を網羅証明するものではない。
- **Inventory全typed NBT・選択slot8は一致、item追加/消失0**。前回試験UUIDの`materials_and_you`1冊追加、XP/Score+20は今回非再発。XpLevel71 / XpP0.10395006090402603 / XpTotal13417 / Score13417 / Health50 / FoodLevel20 / Pos(-31.5,63,48.5)を保持。advancementsも内容一致。
- **通常load/save差分の記録**: Attributesは保存list順序のみでNameごとのBase/Modifiers全一致。水中呼吸/暗視3737→1181、火炎耐性8860→6304、すべてduration -2556でplay_time +2556と一致し、効果ID/amplifier/その他fieldは保持。前回にもeffects経過・list順序差が存在したが、今回はその当時の数値そのものとの一致ではない。
- Rotationは(69.30040740966797,31.349992752075195)→(61.050323486328125,32.39999008178711)へ変化。過去にもRotation差はあったが今回の具体的な原因は断定せず保持する。warden warning elapsed、各dimension raid Tick（+2618）、play/time_since_death/time_since_rest、leave_game（+1）、total_world_time、zombie random sequenceも更新。raid内容/IDは保持。これらを人力操作や破損の証拠とは推測せず、全差分を保存する。
- world file集合は**181→181 / 追加0 / 削除0 / 変更70**（内容変更28、mtimeのみ42）。region/entities/poi、MTR settings6、level/player、timer/stats等の通常保存出力をboot側だけに記録。DataPacks設定は一致、datapack削除/safe mode/registry修復は行っていない。MTRのopaque settings内容差は台帳に残し、全内部fieldの意味を確認済みとはしない。

##### ERROR/保全/最終判定

- latest/debug最終ERRORは**251件**。起動時17件は既存smoke一致。world-load由来234件は既存6/30比較の**231 HISTORICAL BASELINE MATCH / 3 PARTIAL MATCH / NEW・UNEXPLAINED0**を維持する。今回→前回bootではlogger/message/resource/exception type/主要stackと各message件数が全251件一致（launcher/Wrapper末尾だけを除外）。旧3 partialは`ticex:revival_spellbook` tool定義、`ticex:revival_spellbook_irons` layout、`yuushya:yuushya_guidebook`で、6/30との差を解消済みにしない。raw stack全行一致とも扱わず、記録済み既知ERRORを無害と断定しない。
- **FATAL severity0 / ModLoadingException0 / mandatory dependency失敗0 / required Mixin適用失敗0 / registry・datapack全体失敗0 / player capability読込・保存失敗0 / crash report0**。既存の個別recipe/tag/model ERRORと区別する。
- ModernFix/NightConfig `ConcurrentModificationException`は再発し、旧smokeのexception/全14行stackと一致。修正・無害化とはしない。**ProjectE UUID CheckerのConnection refused/Checker例外は今回非再発**。通常outboundでの結果差として記録し、HTTP response内容やHigh Alchemist一覧の取得成功は直接観測していないため断定しない。EMC/knowledge保持は別の保存data証拠。Iron Furnacesは今回もupdate check完了、MOD更新操作なし。
- **原本追加変更0**: 元instance13,351/sharedMeta31/sharedCache4,384/sharedLibraries453/sharedAssets12,049、元instance.cfg/mmc-pack.json、承認済みroot prismlauncher.cfg、元world/ZIP/pristine/過去boot、source runtime16,750、元mods/config/defaultconfigs/resourcepacksの集合/hash/size/mtime不変。root cfgは承認済みSHA `E124B846B8CB91E936A7E8C1A01BD39FC481B834582B83F485295F3CE272F54A` / 4655 bytes / 9/7 17:57:47.3770247 JSTを維持。過去mtime差の原因未解明履歴は残す。v3 JarもSHA `943F10BDEE8CD59C5B43DA12A215F7A64B32BB0CB63BDD69E24BBC0669E95EA4`で不変。
- 隔離側の前smoke後台帳からの差は追加219（boot181 + JourneyMap/skin/identity cache/TaCZ通常export等38）、削除0、既存変更3524（内容8、mtimeのみ3516）。MOD104 Jar・Java runtime・libraries・launcher executableは不変。TaCZが通常起動で追加exportしたsound2件は**同じlocal Jar内entryとhash一致**、通常export log/backupもあり、Jar更新/別version取得ではない。これは実銃試験ではない。アカウントfile/tokenは台帳の内容・hash対象から除外し、Prism実起動logも必要なWrapper token以外の認証引数全文を記録しない。
- 証拠root: `build/verification/original-account-retest-20260908-120155`。`ui-checkpoint.json`/`ui-completion.json`、`boot-before/after-state.json`、`saved-comparison.json`、`supplemental-state.json`、`final-logs.json`、`selected-runtime-evidence.json`、`launcher-runtime-provenance.json`、`after-protection.json`、`isolated-after-manifest.json`、`isolated-changes.json`、`process-after-quit.json`、`verdict.json`。原本とpristineを保持し、bootを自動修復・元へ書戻していない。
- 変更は上記監査helper/証拠・新boot・通常隔離runtime出力・`docs/CODEX_STATUS.md`のみ。Java/Mixin/resource/gameplay/Config/仕様/build/dependency/testsの手動変更なし。**build/unit/check/GameTest = NOT RUN**（source2.2.5の限定load/save試験であり今回不要）。外部MOD相互作用全般やv3互換/移行へPASSを拡張せず、既存MANUAL PASS、実2-client BLOCKED、OPEN、保留実装のgateは維持。
- 結論: **REAL V2.2.5 WORLD COPY / AUTHENTICATED ORIGINAL-ACCOUNT / CURRENT-WRAPPER RETEST / PASS WITH RECORDED HISTORICAL BASELINE ERRORS**。前回の試験UUID/historical-wrapper boot停止はその当時の**NOT PASS履歴**として維持。今回のみの正規元identity/CURRENT環境load/save完了であり、historical byte完全再現ではない。次段階は利用者判断・明示承認待ちとして停止する。
- **HISTORICAL-WRAPPER AUTHENTICATED COMBINATION = NOT TESTED**。
- **V3 MIGRATION = NOT RUN / NOT AUTHORIZED**。

#### 2026-09-08 正規元アカウントworld retest・起動前準備

- 今回の明示承認は既存隔離Prism / CURRENT Wrapperでのsource2.2.5新規worldコピーの1回load/saveのみ。v3差し替え・migrationは未承認。
- 原本instance13,351 / sharedMeta31 / sharedCache4,384 / sharedLibraries453 / sharedAssets12,049、承認済み元launcher cfg、元world181 / ZIP / pristine181 / 旧boot186、source runtime16,750 filesに前回smoke終了から内容・size・mtimeの追加変更0。104 MOD Jarもsource hash一致。
- コピー作成時、長い隔離pathと既存datapack名により通常Win32パスでWinError206/3が発生。Minecraft起動前のコピー工程の失敗であり、world load失敗ではない。最初の不完全コピーは監査root内に退避して保持。短い新規保存名`R0908`とWindows extended pathのコピーAPIで、未起動の今回コピーだけを完成し、181 filesの集合/SHA/size/mtime完全一致を確認した。OS設定・NBT・datapack名・pristineは変更していない。
- 新boot: `build/verification/isolated-prism-control-20260906-211429/launcher/instances/FHR_Source_CurrentWrapper_20260906_195153/minecraft/saves/R0908`。saves内はこの1件のみ。開始時typed NBTは食義Lv2/count35、DisabledSkills空、Diversity All28/Current3/bonus10。14 typed/JSON保存fileを読取記録。
- Prism GUIで`<MINECRAFT_ACCOUNT> / MSA / 準備完了`、唯一の検証instanceを再確認。MC1.20.1/Forge47.4.0 metadata、Microsoft Java17.0.15の実`-version`確認。account file/token・認証引数全文は読まず、設定変更・認証代行なし。
- 証拠: `build/verification/original-account-retest-20260908-120155`の`before-*-manifest.json`/`before-protection.json`/`copy-comparison.json`/`boot-before-state.json`/`preparation.json`。ここまでMinecraft/world未起動。これから許可された1回の通常起動を行い、最終判定は終了後に記録する。**V3 MIGRATION = NOT RUN / NOT AUTHORIZED**。

##### 同日12:10 world load途中checkpoint

- 通常Prism「起動」12:08:06.746 JST、隔離Java PID26276開始12:08:07.911。実MC1.20.1 / Forge47.4.0 / Microsoft Java17.0.15、Food Healing2.2.5、116 IDs全一致を今回logで確認。CURRENT Wrapperは今回Prism実JVM classpathの`PrismLauncher-0.log`595行からversion token `prism-2026-08-01`だけ抽出（前回531行と分離、認証引数全文は記録しない）。main menu表示を確認し、Singleplayer一覧に保存先`R0908`のコピー1件だけを確認。
- **world load要求1回: 12:10:05.642 JST**。12:10:10.534 integrated server開始、12:10:18.378 MTRの既知listener `0.0.0.0:8888`開始、12:10:21.547 <MINECRAFT_ACCOUNT> loginを確認。これはまだworld描画・正規元UUID使用・データ保持のPASSではない。
- 12:10:29の暫定log: ERROR248、FATAL severity0、必須Mixin/dependency/registry全体失敗の検出0、crash report0。途中時点では既存feature照合246 HISTORICAL BASELINE MATCH / 2 PARTIAL MATCH、新規未説明ERROR0。最終件数・234件との照合は通常保存終了後に行うため、完了結果として扱わない。ModernFix/NightConfigの既知ConcurrentModificationExceptionは再発。ProjectE結果も最終確認待ち。
- Minecraft上にWindows FirewallのOpenJDK Platform binary許可/キャンセル画面を検出。**Firewall ruleを追加しないため人間へ「キャンセル」を依頼し、Codexはダイアログを操作せず停止・待機**。world描画確認、最小待機、ESC→Save/Quit→Minecraft終了は人間キャンセル後にのみ継続する。ゲームプレイ操作0。許可済み1回のloadを繰り返さない。
- `ui-checkpoint.json`、`world-loading-logs.json`へ途中証拠を保存。今回変更は監査helper/証拠・新boot copy・Statusと通常隔離runtime出力のみ。ゲームコード/仕様/Configの手動変更なし、build/unit/check/GameTest再実行なし。
- **次の1操作: Windows Firewallダイアログの「キャンセル」（人間）**。その後、world継続可否を観測し、許可済みload/saveの終了監査へ戻る。現時点**NOT PASS**、新しいworld試験やmigrationへは進まない。

#### 2026-09-08 AUTHENTICATED CURRENT-WRAPPER WORLDLESS SMOKE・1回限定完了

- 利用者が今回明示承認したworldless smokeだけを、既に開いている完全隔離Prismの`FHR Source Current Wrapper Control 20260906 195153`で1回実施した。起動直前に実GUIの`<MINECRAFT_ACCOUNT> / MSA / 準備完了`、唯一のinstance、world持込0、104 Jar hash、MC/Forge設定、Java実行ファイルの`-version`、Food Healing Jar metadataを確認。認証後pre-flightから原本に追加変更0を確認してから起動した。**アカウント認証代行・account file/token読取・認証command line全文取得は行っていない**。
- 操作・process時刻（JST）: **11:00:02.776**に通常「起動」を1回、**11:00:08.935**に隔離Java PID31780開始。実logで11:01:01にTitleScreen初期化、11:01:03に`Game took 54.328 seconds to start`。Minecraft画面取得のComputer Use承認timeoutと続くcommand承認待ちによりmain menuでの待機が長引いたが、再起動・world操作はしない。画面取得経路回復後にmain menuを実画面で確認し、**11:31:27.641**にMinecraftの「終了」を押した。**11:31:27.703 `Stopping!` / 11:31:49 Java/javaw 0件**。強制killなし。Prismログ中のJava checker exit0はMinecraft本体の終了コードと混同せず、本体exit codeは独立取得なし、通常Quit/Stopping/process終了を根拠とする。

| 今回の実観測 | 結果・根拠 |
| --- | --- |
| MSA | 起動前アカウント一覧`<MINECRAFT_ACCOUNT> / MSA / 準備完了`、今回latest.log 195行`Setting user: <MINECRAFT_ACCOUNT>`。UUID手入力・Offline Accountなし。 |
| Minecraft / Forge | `1.20.1 / 47.4.0`。latest.log 235〜236行、LoadingModList 112/89行、main menu表示。 |
| Java | Microsoft `17.0.15+6-LTS` / Microsoft-11369865。隔離実行ファイル`-version`と今回latest.log 2行の`17.0.15 by Microsoft`一致。 |
| CURRENT ForgeWrapper | **`prism-2026-08-01`**。今回`launcher/logs/PrismLauncher-0.log`531行の実起動JVM classpath内Jar参照を限定抽出。456行には同versionのlibrary検証記録も存在する。runtime選択の根拠は実起動logであり、準備metadataだけから判定していない。認証引数全文は保存・出力しない。 |
| Food Healing | `2.2.5`（latest.log 88行）、Jar SHA `5BAC83E55419D96849823D7A7BB0933E01600564E13739992E43782C925A6A8C`。v3への差し替えなし。 |
| MOD構成 | 104 top-level Jarの前後hash一致。実LoadingModListはsource **116一致 / 0不一致 / 0追加**。Pam foodextendedのruntime表記`0.0NONE`も既存source判定と一致し、MOD更新で合わせていない。 |
| main menu | Minecraft 1.20.1 / Forge 47.4.0 / 116 MODを実画面で確認。Singleplayer/world一覧/Multiplayer/Realmsは開かず、server接続・world作成・コピー・ゲームプレイなし。 |
| 停止条件 | latest/debugともERROR17、FATAL severity0。ModLoadingException・mandatory dependency failure・required Mixin ApplyError/TransformerError/InjectionError・registry startup failureの該当0、crash report0。 |
| 正常終了 | latest.log 1132行`[08Sep2026 02:31:27.703] ... Stopping!`（今回logはUTC表記、JSTは11:31:27）。その後MinecraftウィンドウとPID31780が消失、Java/javaw 0件。隔離Prism PID26340のみ継続。 |

- **ERROR分類（件数だけで判定しない）**: 旧historical-wrapperの`source-runtime-20260906-165001/game-visible/logs/latest.log`と今回latest.logを比較し、logger・message/resource ID・exception type・stack全体（game directoryとobject identityを正規化）で以下を記録した。古いERRORを無害・修正済みとはしない。

| 分類 | 結果 |
| --- | --- |
| HISTORICAL BASELINE MATCH | **現在ERROR17件すべて一致**。Yuushya mixin minVersion未記載1 / Yuushya model5 / TicEX model4 / SlashBlade model5 / Mekanism inputInputSlot1 / Food Healing2.2.5 max health cap extension失敗1。 |
| EXPECTED NETWORK-CONDITION DIFFERENCE | 旧Iron Furnaces `Update Check failed! / ConnectException` 1件は今回非再発。今回はlatest.log 228行`Update Check done!`、更新案内のみ。旧閉鎖proxyと今回通常outboundの条件差として記録し、MOD修正・更新とはしない。 |
| CURRENT-WRAPPER DIFFERENCE | 実採用Wrapperは旧`prism-2025-12-07`ではなく今回`prism-2026-08-01`。この差が新規ERRORを発生させた証拠は今回0件。historical authenticated combinationや歴史的byte完全再現へ拡張しない。 |
| NEW / UNEXPLAINED | 比較対象の現在ERRORでは**0件**。全機能正常や既存ERRORの安全性保証ではない。 |

- **ModernFix/NightConfig thread例外は再発**。今回latest.log 168〜181行、debug.log 2394〜2407行の`ConcurrentModificationException`、`NightConfigWatchThrottler.java:37`、`FileWatcher.java:162`を確認。旧Thread-0に対し今回はThread-1だが、exception messageとstackの14行は一致。INFO/STDERR経由なのでERROR17件とは別枠。main menu到達を妨げなかった事実だけを記録し、config監視の無害判定・修復はしない。
- **network**: 起動前の隔離Prism `ProxyType=None`、instance側`OverrideJavaArgs=true / JvmArgs=`。過去の閉鎖HTTP/HTTPS proxy `127.0.0.1:1`を設定せず、通常Prism/Minecraft通信条件で実施。system proxy/Firewall/router/port forwardingは変更なし。update checkerの通信は観測したが、MOD/launcher update操作は実施しない。
- **原本保全 / 追加変更0**: 起動前後の元instance13,351 / sharedMeta31 / sharedCache4,384 / sharedLibraries453 / sharedAssets12,049 files、元shared metacache、元instance.cfg/mmc-pack.jsonは集合・hash・size・mtime(ns)一致。元launcher直下`prismlauncher.cfg`は承認済みSHA `E124B846B8CB91E936A7E8C1A01BD39FC481B834582B83F485295F3CE272F54A` / 4655 bytes / mtime9/7 17:57:47.3770247 JSTのまま。過去のmtime差分は原因未解明履歴を維持する。元world181/pristine181/前回boot186 files、元ZIP、runtime source16,750 files、元mods/config/defaultconfigs/resourcepacks、前回boot log、v3 Jarも不変。
- **隔離側の通常生成を原本変更と分離**: 非秘密file台帳で追加3,438 / 削除0 / 変更212。うち内容変更5は隔離instance.cfg/mmc-pack.json、隔離meta/index.json・metacache・prismlauncher.cfgで、残る207はhash/size同一のmtime更新。構成のcached fields、Java検出/lastLaunchTime等を含む正規化を記録。隔離libraries内の追加22 filesにはForge生成client-split/srg JarとPrism標準LWJGL別architecture natives等があり、既存libraryの内容変更0、104 MOD Jarの変更・追加・削除0。sourceの原本library/cacheには変更0。隔離Wrapper JarのSHAは起動前後とも`2BE07E9BFD5BD237BEC7511163B06123A055F4697BEE398683C8668C9B37C83E`、29,800 bytesで一致する。
- **world生成/持込0**: Minecraft起動により空の`minecraft/saves` directoryだけが作成された。配下0件で、world folder/level.dat/level.dat_old/region/playerdata/session.lock/.mca/.mcrは0。integrated server、level準備、chunk保存、server接続のlog証拠も0。空directoryの存在を隠したり削除したりせず、実world生成とは区別する。
- 証拠root: `build/verification/isolated-prism-control-20260906-211429/smoke-20260908-105409`。`preflight.json`、`after-normal-quit-log-review.json`（current/old ERRORとstack）、`final-classification.json`、`runtime-116-mods.json`、`top-level-104-jars.json`、`selected-runtime-wrapper-tokens.json`、`ui-and-shutdown.json`、`process-after-quit.json`、`final-original-diff.json`、`final-preservation.json`、`isolated-after-manifest.json`、`isolated-file-change-summary.json`へ記録。実logは隔離instanceの`minecraft/logs/latest.log`/`debug.log`を保持。account fileは内容・hash・コピーから除外し、Prism logはwrapper/終了関連の必要部分だけを選択抽出、認証情報や全文command lineは記録しない。
- 変更は監査helper/証拠と`docs/CODEX_STATUS.md`、通常起動が生成した隔離root内データのみ。原本/ゲームJava/Mixin/resource/Config/仕様/build/dependency/testsを手動変更していない。**build / unit / check / GameTest = NOT RUN（今回はsource2.2.5の隔離smokeのみ）**。外部MODの起動以外の相互作用、旧world data読込、v3互換性、migration、release readinessへPASSを拡張しない。実2-client BLOCKED/OPEN/保留実装は維持。
- 結論: **AUTHENTICATED CURRENT-WRAPPER SOURCE RUNTIME CONTROL / ISOLATED PRISM ROOT / WORLDLESS SMOKE / PASS WITH RECORDED BASELINE DIFFERENCES**。今回許可の1回を完了して停止。worldless smokeの再実行やworld試験へ自動で進まない。
- **HISTORICAL-WRAPPER AUTHENTICATED COMBINATION = NOT TESTED**。
- **ORIGINAL-UUID WORLD RETEST = NOT RUN**。
- **V3 MIGRATION = NOT RUN / NOT AUTHORIZED**。

#### 2026-09-08 人間Microsoft認証完了・認証後pre-flight

- 利用者の通常Microsoft認証完了報告を受領。対象は`<LOCAL_PATH>/food-healing-mod-main\build\verification\isolated-prism-control-20260906-211429\launcher\prismlauncher.exe`のみ。Codexはメイン画面の`<MINECRAFT_ACCOUNT>`表示に加え、既存アカウント一覧の**ユーザー名Leva9846 / タイプMSA / 状態準備完了**を実GUI/accessibilityで照合し、**HUMAN MSA AUTHENTICATION / <MINECRAFT_ACCOUNT> / PASS**と判定した。閲覧後は設定を変更せずEscapeでメイン画面へ戻した。Microsoft/Offlineアカウント追加、再読み込み、デフォルト切替、再認証、更新、instance起動は操作していない。
- 原本保全: 認証直前`control-reopen-20260908-103027-confirm.json`と認証後`control-post-msa-20260908-104351.json`を直接比較。元instance13,351 / sharedMeta31 / sharedCache4,384 / sharedLibraries453 / sharedAssets12,049 filesは、追加・削除・SHA-256・size・mtime(ns)変更0。元shared `metacache`も不変。結果は`post-msa-20260908-104351-approved-baseline-comparison.json`の全group差分0と`authorizedLauncherConfigUnchanged=true`で記録した。
- 原本3metadataを10:47:19 JSTに再取得し、以下の値を維持した。元launcher直下cfgの9/7既存mtime差分は利用者承認済み新baselineに対する照合であり、過去の差分原因解明・無害判定・restoreを意味しない。

| 原本file | SHA-256 | size | mtime JST |
| --- | --- | --- | --- |
| `instances/1.20.1/instance.cfg` | `F7CE76548CA5A7535BDB51C3031BD69BD6120661F4CFD07F09CCAEABC64B2DEF` | 2677 bytes | 2026-09-06 20:01:29.8709466 |
| `instances/1.20.1/mmc-pack.json` | `313DCBAB3CA09A22498DD5AE92F0E9E5BA4E670A3ECCA57BD531704F112EEBC3` | 945 bytes | 2026-09-06 20:01:34.9006503 |
| `prismlauncher.cfg` | `E124B846B8CB91E936A7E8C1A01BD39FC481B834582B83F485295F3CE272F54A` | 4655 bytes | 2026-09-07 17:57:47.3770247 |

- 元world181 / pristine181 / 前回boot186 filesは集合・hash・size・mtime変更0。元ZIPはSHA `B83E28255F90BEB356253AB60A5B12D9C8DBFB5E631C12309C38962A62AC5C7B`で不変。runtime source16,750 files、元mods/config/defaultconfigs/resourcepacks、前回boot log、v3 Jarも不変。今回worldコピー・起動・NBT編集は0。
- 隔離側は検証instance `FHR_Source_CurrentWrapper_20260906_195153`1件、104 top-level MOD Jarのhash一致、world持込0を維持。認証前の非秘密file集合との比較では**隔離launcher直下**`prismlauncher.cfg`の変更と`prismlauncher_update.cfg`追加を観測した。原本側差分とは分離し、これだけでlauncher更新実行や不具合とは断定しない。コピー済みlauncher executable・instance metadata・MOD/runtimeは不変、実UIはPrism 10.0.5。隔離`accounts.json`は探索で名前が見えるだけで、内容読取・hash計算・コピーから除外し、launcher log、password/access token/refresh token/client token/完全command lineも読んでいない。
- **10:47:19 JST**のprocess確認は隔離Prism PID26340のみ、元Prismなし、Java/javaw 0件。メイン一覧は検証instance1件、総プレイ時間0秒で、Minecraftは未起動。**AUTHENTICATED CURRENT-WRAPPER SOURCE CONTROL / READY FOR WORLDLESS SMOKE**とするが、これは人間認証済みUI・既存検証環境・原本保全のpre-flight完了だけで、Minecraft内のruntime認証UUID、実際に採用されたWrapper、116 ID/versionの実起動確認、worldless smokeのPASSではない。
- 証拠は`build/verification/isolated-prism-control-20260906-211429`内の`control-post-msa-20260908-104351.json`/`...-diff.json`、`post-msa-20260908-104351-approved-baseline-comparison.json`、`post-msa-20260908-104351-final-original-metadata.json`、`launcher-post-msa-20260908-104351.json`/`...-diff.json`、`post-msa-20260908-104351-isolated-file-diff.json`、`preservation-post-msa-20260908-104351.json`、`process-post-msa-20260908-104351.json`。直前の保全結果・Statusも別名で保持した。
- 今回の変更は監査記録と`docs/CODEX_STATUS.md`のみ。人間認証による隔離root内の更新は上記へ分離。原本/Java/Mixin/resource/Config/仕様/build/dependency/testsは未変更。**build / unit / check / GameTest = NOT RUN**。今回は結果を返してMinecraft起動前で一旦停止し、worldless smokeへ自動では進まない。既存manual/integration PASS、実2-client BLOCKED、OPEN、保留実装のgateは変更しない。
- **WORLDLESS SMOKE = NOT RUN**。
- **HISTORICAL-WRAPPER AUTHENTICATED COMBINATION = NOT TESTED**。
- **ORIGINAL-UUID WORLD RETEST = NOT RUN**。
- **V3 MIGRATION = NOT RUN / NOT AUTHORIZED**。

#### 2026-09-08 セットアップ完了後の原本保全再確認・認証前待機

- 利用者から隔離Prismクイックセットアップの「完了」操作、検証instance1件、未認証・Minecraft未起動・更新未実施の報告を受領。Codexも対象processのメイン画面を読み取り確認し、`FHR Source Current Wrapper Control 20260906 195153`だけが一覧に存在し、元`1.20.1`および他instanceがないことを実GUI/accessibilityで確認した。右上は「アカウント」、総プレイ時間は0秒。認証操作・instance選択/起動・更新操作は行わない。
- **ISOLATED PRISM REOPEN / PREFLIGHT PASS / WAITING FOR HUMAN MSA AUTHENTICATION**。対象は既存の`<LOCAL_PATH>/food-healing-mod-main\build\verification\isolated-prism-control-20260906-211429\launcher\prismlauncher.exe`のみ。10:35:17 JSTのprocess確認で隔離Prism PID26340（10:23:55起動）のみ継続、元Prismなし、Java/javaw 0件。隔離Prismは終了せずメイン画面のまま人間操作待ちにする。
- 原本照合は同audit rootの`control-reopen-20260908-102215-before.json`対`control-reopen-20260908-103027-confirm.json`で実施。元instance13,351 / sharedMeta31 / sharedCache4,384 / sharedLibraries453 / sharedAssets12,049 filesの集合・SHA-256・size・mtime(ns)は追加変更0。元shared `metacache`も不変。元`instance.cfg`と`mmc-pack.json`のhash/size/mtimeは直前フェーズ記載値を維持する。
- 元launcher直下`prismlauncher.cfg`は承認済み`reopen-20260908-102215-authorized-baseline.json`と一致: SHA `E124B846B8CB91E936A7E8C1A01BD39FC481B834582B83F485295F3CE272F54A` / 4655 bytes / mtime **2026-09-07 17:57:47.3770247 JST**。今回再起動・セットアップ完了後の追加mtime更新も0。9/6 baselineからの既存mtime差分は比較履歴に残り、原因解明済み・無害・復元済みとはしない。他の保護baselineは変更しない。
- 10:34:38 JST開始の保全監査でも、元world181 / pristine181 / 前回boot186 files、元ZIP、runtime source16,750 files、元mods/config/defaultconfigs/resourcepacks、前回boot logs、v3 Jarは不変。元ZIP SHA `B83E28255F90BEB356253AB60A5B12D9C8DBFB5E631C12309C38962A62AC5C7B`を維持。検証instance1件・104 top-level MOD Jar hash一致・world持込0。world/NBTのコピー・編集・起動はしていない。
- 証拠は同audit rootの`reopen-20260908-103027-approved-baseline-comparison.json`（全group差分0 / `authorizedLauncherConfigUnchanged=true`）、`control-reopen-20260908-103027-confirm.json`/`...-diff.json`、`launcher-reopen-20260908-103027-confirm.json`/`...-diff.json`、`preservation-main-confirm-20260908-103027.json`、`process-main-confirm-20260908-103027.json`。直前の保全結果とStatusも`preservation-before-main-confirm-20260908-103027.json`、`CODEX_STATUS-before-main-confirm-20260908-103027.md`へ保持した。account file/password/token/完全command lineは読んでいない。
- 変更は監査記録と`docs/CODEX_STATUS.md`のみ。Java/Mixin/resource/Config/仕様/build/dependency/tests/原本を変更せず、**build / unit / check / GameTest = NOT RUN**。今回PASSは隔離Prism再表示と保全pre-flightだけで、MSA認証、source runtime起動、旧world、migration、外部MOD互換性のPASSへ拡張しない。次は人間のMicrosoft認証待ちで停止し、今回自動で後続試験に進まない。
- **MSA AUTHENTICATION = NOT RUN**。
- **WORLDLESS SMOKE = NOT RUN**。
- **HISTORICAL-WRAPPER AUTHENTICATED COMBINATION = NOT TESTED**。
- **ORIGINAL-UUID WORLD RETEST = NOT RUN**。
- **V3 MIGRATION = NOT RUN / NOT AUTHORIZED**。

#### 2026-09-08 mtime差分の継続承認・新baseline記録・隔離Prism起動

- 利用者から、元launcher直下`prismlauncher.cfg`のhash/size不変・mtimeのみ9/7更新という既存差分を記録したうえで、隔離Prism再起動へ進む明示承認を受領した。**原因解明・無害判定・元fileのrestore/editは行わない**。10:12の停止履歴と9/6のcheckpointは保持する。
- 起動前10:22:51 JSTに元cfgの現在値を再取得し、承認対象の値と完全一致することを確認して、**今回の新baseline**として`reopen-20260908-102215-authorized-baseline.json`へ保存した。対象は`<LOCAL_DOWNLOADS>\PrismLauncher-Windows-MinGW-w64-Portable-9.1\prismlauncher.cfg`のみで、他の保護baselineは変えていない。

| 新baseline対象 | 値 |
| --- | --- |
| SHA-256 | `E124B846B8CB91E936A7E8C1A01BD39FC481B834582B83F485295F3CE272F54A` |
| size | 4655 bytes |
| mtime | 2026-09-07 17:57:47.3770247 JST / epoch ns `1788771467377024700` |

- 起動対象は`<LOCAL_PATH>/food-healing-mod-main\build\verification\isolated-prism-control-20260906-211429\launcher\prismlauncher.exe`だけ。通常起動で**PID26340 / 開始10:23:55.9877836 JST / Prism 10.0.5**を確認。元Prism/元instanceは起動・終了・再表示・操作していない。更新、認証、Minecraft起動、MOD変更、proxy/Firewall変更はしていない。
- 起動した隔離Prismは、メイン画面の前に**「Microsoftアカウントを追加」クイックセットアップ画面を再表示**した。実画面とaccessibilityで「Microsoftアカウントを追加する」「完了」を確認したが、認証画面の操作は代行せず、人間へ**アカウント追加をせず「完了」だけを押す**よう引き継いだ。これはMinecraftクラッシュやMOD不具合の検出ではなく、メイン一覧がまだ表示されていない状態。設定を変更して画面を回避したり、アカウントを追加したりしない。
- 起動前後の保全を、今回直前snapshot `control-reopen-20260908-102215-before.json`と`...-after.json`で直接比較。元instance13,351 / sharedMeta31 / sharedCache4,384 / sharedLibraries453 / sharedAssets12,049 filesは追加・削除・hash/size/mtime変更0。元shared `metacache`および**新baseline化した元`prismlauncher.cfg`もhash/size/mtime追加変更0**。比較結果は`reopen-20260908-102215-approved-baseline-comparison.json`。旧baselineとの差分表示には承認済みmtime差分が残り、それを削除・隠していない。
- 元`instances/1.20.1/instance.cfg`はSHA `F7CE76548CA5A7535BDB51C3031BD69BD6120661F4CFD07F09CCAEABC64B2DEF` / 2677 bytes / mtime9/6 20:01:29.8709466 JST、`mmc-pack.json`はSHA `313DCBAB3CA09A22498DD5AE92F0E9E5BA4E670A3ECCA57BD531704F112EEBC3` / 945 bytes / mtime9/6 20:01:34.9006503 JSTのまま。元world181 / pristine181 / 前回boot186 files、元ZIP（SHA `B83E28255F90BEB356253AB60A5B12D9C8DBFB5E631C12309C38962A62AC5C7B`）、runtime source16,750files、元mods/config/defaultconfigs/resourcepacks、過去boot log、v3 Jarも不変。world持込・起動・NBT編集なし。
- 検証instanceはfile集合上`FHR_Source_CurrentWrapper_20260906_195153`1件だけ、104 top-level MOD Jarはhash一致、world持込0のまま。`launcher-reopen-20260908-102215-before.json`/`...-after.json`に記録。コピー済みfileの変更・削除0、隔離launcher logのローテーションを観測したが内容は読まず、account file/password/token/完全command lineも取得していない。**今回再起動後のGUI一覧1件確認はセットアップ完了待ち**で、9/6の表示確認だけで代替PASSにしない。
- **10:25:15 JST Java/javaw 0件**、Prismは隔離PID26340だけ。`process-reopen-20260908-102215-after.json`へ記録。原本保全確認は今回新baselineの範囲でPASSだが、利用者指定の最終状態`ISOLATED PRISM REOPEN / PREFLIGHT PASS / WAITING FOR HUMAN MSA AUTHENTICATION`にはGUI一覧確認が残るため、現時点では昇格しない。
- 変更範囲は監査記録・Statusと、隔離Prism通常起動が生成する隔離root内ファイルのみ。元file/既存Java/Mixin/resource/Config/仕様/build/dependency/testsは変更なし。**build / unit / check / GameTest = NOT RUN**。次は人間の「完了」操作後にGUI一覧1件・元instance非表示・原本保全を確認し、認証前待機へ進む。今回Minecraft/worldless smokeは開始しない。
- **MSA AUTHENTICATION = NOT RUN**。
- **WORLDLESS SMOKE = NOT RUN**。
- **HISTORICAL-WRAPPER AUTHENTICATED COMBINATION = NOT TESTED**。
- **ORIGINAL-UUID WORLD RETEST = NOT RUN**。
- **V3 MIGRATION = NOT RUN / NOT AUTHORIZED**。

#### 2026-09-08 隔離Prism再起動前のbaseline照合・mtime差分で停止

- 今回の依頼は9/6の終了checkpointから、既存の完全隔離`build/verification/isolated-prism-control-20260906-211429/launcher/prismlauncher.exe`だけを再起動し、人間MSA認証前で待機すること。実日付は9/8、参照した最終checkpointは9/6 21:45 JST。前回のPASSを無理由に再試験へ戻さず、指定された起動前保全照合のみを実施した。
- **起動前の差分1件: 元Prism直下`<LOCAL_DOWNLOADS>\PrismLauncher-Windows-MinGW-w64-Portable-9.1\prismlauncher.cfg`のmtime変更**。元`instances/1.20.1/instance.cfg`とは別file。9/6終了台帳`control-day-close.json`との直接比較で、4655 bytesとSHA-256 `E124B846B8CB91E936A7E8C1A01BD39FC481B834582B83F485295F3CE272F54A`は完全一致するが、mtimeは**2026-09-06 20:01:29.8749471 JST → 2026-09-07 17:57:47.3770247 JST**。今回の再起動前に既に存在した差分であり、今回のPrism操作で発生したものではない。書込元・原因は未確認で、通常終了等による保存だと推測確定しない。
- 元instanceの保全は成立: `instance.cfg` SHA `F7CE76548CA5A7535BDB51C3031BD69BD6120661F4CFD07F09CCAEABC64B2DEF` / 2677 bytes / mtime9/6 20:01:29.8709466、`mmc-pack.json` SHA `313DCBAB3CA09A22498DD5AE92F0E9E5BA4E670A3ECCA57BD531704F112EEBC3` / 945 bytes / mtime9/6 20:01:34.9006503は、hash・size・mtime(ns)すべて同一。元instance13,351 / sharedMeta31 / sharedCache4,384 / sharedLibraries453 / sharedAssets12,049 filesの追加・削除・変更0。元shared `metacache`も不変。**元root全体差分0とは記録しない**。
- world保全も成立: 元world181 / pristine181 / 前回boot186 filesの集合・hash・size・mtime不変。元ZIPはSHA `B83E28255F90BEB356253AB60A5B12D9C8DBFB5E631C12309C38962A62AC5C7B`で不変。runtime source16,750files、元mods/config/defaultconfigs/resourcepacks、過去boot log、v3 Jarも不変。新規worldコピーやNBT読込/編集・起動は行っていない。
- 検証rootは前回終了時の`launcher-day-close.json`と今回の読取結果を直接比較し、秘密情報/log等を除いた**4,705 filesの集合・hash・size・mtime差分0**。検証instanceは`FHR_Source_CurrentWrapper_20260906_195153`1件、104 top-level MOD Jar hash一致、world持込0。削除・修復・metadata復元・構成変更なし。
- **10:08:32 JSTの起動前process確認はPrism/Java/javawすべて0件**。今回の原本差分を無視して「再起動pre-flight PASS」にせず、隔離Prismも起動前で停止した。元Prismの起動/終了/再表示/操作0、隔離Prism起動0、GUI一覧の今回再表示確認はNOT RUN。9/6の一覧1件確認と`ISOLATION PRE-FLIGHT = PASS / SESSION CLOSED`は履歴として維持する。Minecraft/認証/launcher更新は行っていない。
- 読取証拠は同audit rootの`reopen-20260908-baseline-difference.json`、`control-reopen-20260908-before.json`/`control-reopen-20260908-before-diff.json`、`launcher-reopen-20260908-before.json`、`process-reopen-20260908-before.json`、`final-preservation.json`。9/6最終保全記録は`preservation-day-close-baseline-for-reopen-20260908.json`、Statusは`CODEX_STATUS-before-reopen-20260908.md`へ保持した。account file/password/token/command line全体は読んでいない。
- 変更は監査記録とStatusのみ。Java/Mixin/resource/Config/仕様/build/dependency/既存testsは未変更。**build / unit / check / GameTest = NOT RUN**。mtimeを旧値へ戻したり、新baselineへ勝手に置換したりしない。
- 次: この**内容不変・mtimeのみの既存差分**を記録したうえで隔離Prism再起動へ進んでよいか、利用者へ確認する。今回の再起動を実行済み/PASSとはしない。承認後も予定は隔離Prismだけの再起動→検証instance1件のGUI確認→原本保全照合→人間MSA認証前の待機まで。今回はworldless smokeへ進まない。
- **MSA AUTHENTICATION = NOT RUN**。
- **WORLDLESS SMOKE = NOT RUN**。
- **HISTORICAL-WRAPPER AUTHENTICATED COMBINATION = NOT TESTED**。
- **ORIGINAL-UUID WORLD RETEST = NOT RUN**。
- **V3 MIGRATION = NOT RUN / NOT AUTHORIZED**。

#### 2026-09-06 本日の終了checkpoint・隔離Prism通常終了

- 利用者の終了指示により、**ISOLATION PRE-FLIGHT = PASS**を本日の最終checkpointとして維持し、Microsoft認証・Minecraft起動・WORLDLESS SMOKEを行わず終了処理だけを実施した。既存PASSの範囲を拡張せず、元metadata書込検出等の過去履歴も保持する。
- 終了対象は`<LOCAL_PATH>/food-healing-mod-main\build\verification\isolated-prism-control-20260906-211429\launcher\prismlauncher.exe`（PID37424）だけ。実行pathと検証instance1件のメイン画面を再確認して、通常のウィンドウ終了操作`Alt+F4`を送った。強制kill/API停止は使用していない。**21:43:19 JSTにread-only process waitで終了を確認**。数値exit codeは取得結果nullのため、exit0とは断定しない。
- **21:43:45 JST: isolated Prism processなし / Java・javaw 0件**。元Prism PID35948は開始20:01:29のまま生存し、元launcher・元instanceへの操作、終了、再起動、restore/editはしていない。証拠は同audit rootの`launcher-normal-shutdown.json`、`process-day-close.json`。process command lineや認証秘密情報は取得していない。
- 終了後、元metadataは今回開始前baselineと次の値がすべて一致した。byte/hashだけでなくsize・mtime(ns)も不変。これは前回別フェーズのmetadata変更を修復・取消した意味ではない。

| 元metadata | baselineと同一の終了後値 |
| --- | --- |
| `instance.cfg` | SHA `F7CE76548CA5A7535BDB51C3031BD69BD6120661F4CFD07F09CCAEABC64B2DEF` / 2677 bytes / mtime 20:01:29.8709466 JST |
| `mmc-pack.json` | SHA `313DCBAB3CA09A22498DD5AE92F0E9E5BA4E670A3ECCA57BD531704F112EEBC3` / 945 bytes / mtime 20:01:34.9006503 JST |

- 終了後の読み取り保全照合: 元instance13,351 / sharedMeta31 / sharedCache4,384 / sharedLibraries453 / sharedAssets12,049 filesは追加・削除・hash/size/mtime変更0。元shared `metacache`/`prismlauncher.cfg`も不変。元world181 / pristine181 / 前回boot186 files、元ZIP（SHA `B83E28255F90BEB356253AB60A5B12D9C8DBFB5E631C12309C38962A62AC5C7B`）、既存runtime source16,750files、元mods/config/defaultconfigs/resourcepacks、過去boot log、v3 Jarも不変。`control-day-close.json`/`control-day-close-diff.json`/`final-preservation.json`に記録。直前の保全記録は`preservation-before-day-close.json`へ保持した。
- **検証instanceは削除せず保持**。新portable root内には`FHR_Source_CurrentWrapper_20260906_195153`1件のみ。104 top-level MOD Jarのhash一致、world/saves/region/playerdata/level.dat/session.lock持込0を終了後も確認。`launcher-day-close.json`/`launcher-day-close-diff.json`はコピー済みfileの変更・削除0を示す。新launcher自身の設定/cache/翻訳追加は前段からの履歴を維持し、原本変更と混同しない。
- 今回の変更は終了確認の監査記録とStatusのみ。Java/Mixin/resource/Config/仕様/dependency/build設定/既存テスト変更なし。**build / unit / check / GameTest = NOT RUN**。Minecraftは本日この隔離環境で一度も起動していないため、main menu・実Wrapper・runtime116・Minecraft終了log/ERROR確認等をPASSへ昇格しない。
- **MSA AUTHENTICATION = NOT RUN / NEXT SESSION**。
- **WORLDLESS SMOKE = NOT RUN / NEXT SESSION**。
- **HISTORICAL-WRAPPER AUTHENTICATED COMBINATION = NOT TESTED**。
- **ORIGINAL-UUID WORLD RETEST = NOT RUN**。
- **V3 MIGRATION = NOT RUN / NOT AUTHORIZED**。
- 次回再開地点: 最新Statusを確認し、**隔離Prismを起動 → Microsoft正規認証を人間が行う → `<MINECRAFT_ACCOUNT> / MSA`をUIで確認 → 原本保全確認 → 承認済みworldless smoke**。元Prismを起動元にせず、account store/tokenコピー・UUID入力・world持込/起動・historical Wrapper強制はしない。本日はこの記録後に完全停止し、追加試験・実装・別監査へ進まない。

#### 2026-09-06 隔離Prismメイン画面pre-flight完了・人間MSA認証待ち

- **ISOLATION PRE-FLIGHT / PASS**。利用者から、新しい隔離Prismの初期設定「完了」を押し、検証instance1件だけが表示されたとの報告を受領。Minecraft未起動・Microsoftアカウント未追加・Prism更新未実施という報告と分離して、Codexも隔離processの実メイン画面を読み取り確認した。認証操作・起動・設定変更は行っていない。
- 対象rootは`<LOCAL_PATH>/food-healing-mod-main\build\verification\isolated-prism-control-20260906-211429\launcher`。PID37424、開始21:19:24、当該rootの`prismlauncher.exe`、file/product version10.0.5.0を確認。GUI一覧には`FHR Source Current Wrapper Control 20260906 195153`の1項目のみ、元`1.20.1`や他instanceは表示されない。総プレイ時間0秒。右上はアカウント名ではなく「アカウント」表示で、今回の`<MINECRAFT_ACCOUNT> / MSA`確認はまだ行っていない。ニュース欄のRelease 11.1.0表示を更新適用とは扱わず、更新ボタンも押していない。
- 新rootのinstance directoryも`FHR_Source_CurrentWrapper_20260906_195153`1件のみ。MC1.20.1 / Forge47.4.0 / LWJGL3.3.1の通常component、JavaPathは新root内Microsoft Java17.0.15のコピーを参照し、追加JVM引数は空のまま。104 top-level MOD Jarすべてコピー計画のhashと一致、world/saves/region/playerdata/level.dat/session.lock持込なし。Food Healing2.2.5とcurrent Wrapperの準備済みartifactは前項の値を維持するが、**実使用Wrapper・runtime116 ID/version・main menu・Minecraft ERROR/FATAL/Mixin/dependencyは未観測/NOT RUN**。
- 原本metadata再照合は次のとおりで、**byteだけでなくsize・mtime(ns)も開始前と完全一致**。前回別フェーズのmetadata書込や過去の未解明差分をrestore/修正済みとはしない。

| 元metadata | 今回開始前からの同一値 | 判定 |
| --- | --- | --- |
| `instance.cfg` | SHA `F7CE76548CA5A7535BDB51C3031BD69BD6120661F4CFD07F09CCAEABC64B2DEF` / 2677 bytes / mtime 20:01:29.8709466 JST | 変更なし |
| `mmc-pack.json` | SHA `313DCBAB3CA09A22498DD5AE92F0E9E5BA4E670A3ECCA57BD531704F112EEBC3` / 945 bytes / mtime 20:01:34.9006503 JST | 変更なし |

- 元instance13,351 / sharedMeta31 / sharedCache4,384 / sharedLibraries453 / sharedAssets12,049 filesについて追加・削除・hash/size/mtime変更0。元shared `metacache`/`prismlauncher.cfg`も同一。元world181 / pristine181 / 前回boot186 files、元ZIP（SHA `B83E28255F90BEB356253AB60A5B12D9C8DBFB5E631C12309C38962A62AC5C7B`）、runtime source16,750files、元mods/config/defaultconfigs/resourcepacks、過去boot log、v3 Jarも不変。元Prism PID35948は操作せず維持。元rootのOS全read traceは取得していないが、新launcherの実一覧・新root内保存・原本不変がそろった範囲でisolation pre-flightをPASSとした。
- 新launcher側は起動前台帳との比較で`metacache`/`prismlauncher.cfg`/翻訳index・日本語qmの追加のみ（前のsetup段階でも記録済み）、コピー済みfileの変更・削除0。launcher logは秘密情報回避のため今回内容を読まず、account file/token/passwordも未読・未コピー・未入力。21:36:01 JST **Java/javaw 0件**、Minecraft終了確認ではなく未起動状態。Prismは人間認証用に開いたまま。
- 証拠: 同audit rootの`launcher-main-preflight.json`/`launcher-main-preflight-diff.json`、`control-main-preflight.json`/`control-main-preflight-diff.json`、`final-preservation.json`、`process-preflight.json`。前段の保全/process記録は`preservation-before-main-preflight.json`/`process-before-human-setup-completion.json`へ保持した。Statusも今回更新前のcopyを保存。
- 変更範囲は監査記録とStatusのみ。既存Java/Mixin/resource/Config/dependency/build設定/仕様/テストは変更なし。**build / unit / check / GameTest / Minecraft smoke = NOT RUN**。過去MANUAL PASS、実2-client BLOCKED、OPEN等は変更しない。
- **次の人間操作: この隔離Prismの「アカウント」から通常のMicrosoft認証を行う**。password/tokenをチャットへ送らず、完了後に`<MINECRAFT_ACCOUNT> / MSA`表示になったことだけを報告する。Minecraftはまだ起動せず待機。Codexは認証情報を操作せず、報告後にUI表示と原本保全を確認し、条件成立時だけ承認済みworldless smokeへ進む。
- **HISTORICAL-WRAPPER AUTHENTICATED COMBINATION = NOT TESTED**。
- **ORIGINAL-UUID WORLD RETEST = NOT RUN**。
- **V3 MIGRATION = NOT RUN / NOT AUTHORIZED**。

#### 2026-09-06 完全隔離portable Prismの準備・初期設定待ち

- 最新添付依頼`<LOCAL_PATH>/pasted-text.txt`の条件付き承認により、監査root `build/verification/isolated-prism-control-20260906-211429`、**isolated Prism root `<LOCAL_PATH>/food-healing-mod-main\build\verification\isolated-prism-control-20260906-211429\launcher`**を新規作成した。今回の許可はcurrent-wrapper/worldless controlだけ。world/full boot/v3 migrationは実行していない。
- 隔離方法は既存local Prism 10.0.5 executable/supportのportableコピー。元配布物の`portable.txt`には、同fileが実行ファイルのrootに存在するとデータをそのrootへ保存する旨の説明がある。`--dir`はlocal binaryにapplication root指定の説明があるものの、共有process/保存先を含む隔離をそれだけでは確証できず、許可されたportable方式を選択。元Prismのexe再起動・再表示・終了・UI操作はしていない。新process PID37424の実行pathと新root内の初期生成fileを確認した。
- コピー元instanceは前回作成済み未起動`FHR_Source_CurrentWrapper_20260906_195153`のみ。元`instances/1.20.1`、元instance.cfg/mmc-pack.json、旧instances一覧、元launcher cfg/metacache/account store、world/ZIP/pristine/boot worldはコピーしていない。新root内instanceディレクトリは1件のみ。コピー先の`instance.cfg`のJavaPathだけを新rootの`runtime/java/bin/javaw.exe`へ変更し、元/前回instanceには書き戻していない。custom componentなし、historical Wrapper強制なし。
- local distribution manifestとMC1.20.1/Forge47.4.0/LWJGL3.3.1の標準component metadata、Forge installerのlibrary宣言、MC asset index5から必要ファイルを選別。**4,702 files / 1,449,257,629 bytesをコピーしSHA-256一致、必要local artifact不足0**。MOD/cache一括流用やMODのdownload/update/replaceは行っていない。prepared instanceのsource snapshotに一致する924filesと新規cfg/packを含み、104 top-level MOD Jarはすべて一致。world持込0、Minecraft logs/crash-reports生成なし。詳細hashは`isolated-copy-plan.json`、完了記録は`copy-completed.json`。
- 準備条件はMC1.20.1 / Forge47.4.0 / Microsoft Java17.0.15 / Food Healing2.2.5。Javaのrelease値とコピーhashを確認。Food Healing SHA-256は`5BAC83E55419D96849823D7A7BB0933E01600564E13739992E43782C925A6A8C`。標準Forge metadataが参照するCURRENT Wrapperは`prism-2026-08-01`、local Jar SHA-256 `2BE07E9BFD5BD237BEC7511163B06123A055F4697BEE398683C8668C9B37C83E`。**これらは準備済みartifactの値であり、実使用Wrapperとruntime LoadingModList 116 ID/versionの照合はNOT RUN**。
- 21:19:24 JSTに隔離Prism本体だけが起動。新windowは`Prism Launcher クイック セットアップ - Prism Launcher 10.0.5`で、言語日本語・標準外観の初期設定を進めた。Microsoftアカウント追加ページに到達したところで認証画面操作を引き継いだ。**人間には今はアカウント追加をせず「完了」だけを押してもらい、その後に検証instanceだけがGUI一覧へ現れるか確認する**。一覧の実表示はまだ未確認。新launcherへ元rootを参照する設定は持ち込んでいないが、OS file-access traceで全readを観測したとは主張しない。Codexによる原本の読み取り保全監査と、新launcherのinstance列挙を区別する。
- **MSA UI状態: 未認証/人間操作待ち。<MINECRAFT_ACCOUNT> / MSAは今回の新launcherでは未確認**。元account store/token/passwordは読取・コピー・入力していない。account再認証やMinecraft起動を代行していない。過去の9/5認証済みUUID照合はその範囲で維持し、今回のruntime認証PASSへ代替しない。
- 起動後の保全: 元instance13,351 / sharedMeta31 / sharedCache4,384 / sharedLibraries453 / sharedAssets12,049 filesの集合・hash・size・mtimeは開始前と一致。元shared `metacache`/`prismlauncher.cfg`も不変。元world181 / pristine181 / 前回boot186 files、元ZIP、既存runtime source16,750files、元mods/config/defaultconfigs/resourcepacks、過去boot log、v3成果物も不変。元ZIP SHAは`B83E28255F90BEB356253AB60A5B12D9C8DBFB5E631C12309C38962A62AC5C7B`。証拠は`control-before.json`/`control-after-diff.json`、`initial-preservation.json`/`final-preservation.json`、`launcher-setup.json`。**今回の差分0は、前回検出した元metadata変更の復元・解決を意味しない**。

| 元metadata | 今回開始前・隔離launcher起動後の同一値 |
| --- | --- |
| `instance.cfg` | 2677 bytes / SHA `F7CE76548CA5A7535BDB51C3031BD69BD6120661F4CFD07F09CCAEABC64B2DEF` / mtime 20:01:29.8709466 JST（ns台帳も一致） |
| `mmc-pack.json` | 945 bytes / SHA `313DCBAB3CA09A22498DD5AE92F0E9E5BA4E670A3ECCA57BD531704F112EEBC3` / mtime 20:01:34.9006503 JST（ns台帳も一致） |

- 新launcher自身の初期生成は`metacache`、`prismlauncher.cfg`、`translations/index_v2.json`、`translations/mmc_ja.qm`、新rootの`logs/PrismLauncher-0.log`（確認時0bytes）。起動前に存在したコピー済みファイルの変更/削除0。global OS proxy/Firewall/router/port forwarding変更なし、閉鎖proxy追加なし。元Prism PID35948は操作せず継続中。21:22:05 JSTに**Java/javaw 0件**、隔離launcher PID37424は初期設定で人間待ち。`process-preflight.json`はPID/path/startのみで、command line/tokenを取得していない。
- **main menu / 実Wrapper / runtime116 / Minecraft ERROR・FATAL・必須Mixin・dependency・registry・crash / Stopping・正常exit = NOT RUN / 未観測**。Minecraft未起動なのでsmoke PASSやERROR0件PASSとしない。launcherも人間引継ぎのため未終了。
- 監査helper初回のコピー前検査は、Java同梱ライセンス`pkcs11cryptotoken.md`の文字列tokenを認証storeと誤認して停止。既存許可Javaの当該licenseだけを明示例外とし、秘密ファイル拒否を維持して計画/コピーを再実行した。ゲーム側の不具合やテストFAILではない。CIMによるprocess一覧読取はアクセス拒否されたため、command lineを含まない`Get-Process`に切替えた。権限/OS設定は変更していない。
- 変更範囲: 新audit root・その下のisolated launcherとコピーinstance、Statusのみ。Food Healing Java/Mixin/resource/gameplay/既存Config/dependency/build/仕様/既存testsは変更なし。**build / unit / check / GameTest = NOT RUN**。既存の自動/手動PASSを変更しない。
- 次: 人間が新Prism初期設定の「完了」を押した後、GUIに検証instanceだけが存在することと原本metadata不変を再確認。isolation pre-flightが成立してから、人間が通常のMicrosoft認証を行い、UIの`<MINECRAFT_ACCOUNT> / MSA`だけを確認する。その条件を満たす場合のみ、承認済みWORLDLESS SMOKEを実施して通常終了する。world試験や別機能へは進まない。
- **HISTORICAL-WRAPPER AUTHENTICATED COMBINATION = NOT TESTED**。
- **ORIGINAL-UUID WORLD RETEST = NOT RUN**。
- **V3 MIGRATION = NOT RUN / NOT AUTHORIZED**。

#### 2026-09-06 CURRENT-WRAPPER CONTROL起動前停止・最終保全確認

- **AUTHENTICATED CURRENT-WRAPPER SOURCE RUNTIME CONTROL / WORLDLESS SMOKE = NOT RUN / NOT PASS**。新instance作成と3設定コピーの明示承認に基づき準備したが、利用者指定の「original instanceへの書込みが発生したら停止」に該当した。Minecraft main menuには未到達。クラッシュ、MOD/dependency/Mixin失敗が原因と判定したものではない。
- 新instanceは`<LOCAL_DOWNLOADS>\PrismLauncher-Windows-MinGW-w64-Portable-9.1\instances\FHR_Source_CurrentWrapper_20260906_195153`（表示名`FHR Source Current Wrapper Control 20260906 195153`）。監査rootは`build/verification/current-wrapper-control-20260906-195153`。world/saves/level.dat/regionは未配置。元instance.cfg/mmc-pack.jsonをコピー・基準利用せず、custom componentや旧Wrapper強制もない。
- 準備したMC1.20.1 / Forge47.4.0 / Microsoft Java17.0.15 / Food Healing2.2.5、104 top-level MOD Jarは保存済みsource台帳に対応する。コピー924 filesすべてsource hash一致、元から読み取りコピーしたのは明示許可の`embeddium-fingerprint.json`/`embeddium-options.json`/`fml.toml`だけ。Food Healing2.2.5 SHA-256 `5BAC83E55419D96849823D7A7BB0933E01600564E13739992E43782C925A6A8C`。**116 ID/versionは既存source証拠の範囲を維持し、今回runtimeでの一致件数は未観測**。全Jarの現在hashは`copy-plan.json`および`stop-review.json`のjarManifestに記録。
- 19:57:45のPrism通常CLI起動要求（新instance ID・Leva9846指定）はMinecraft起動に至らず、Java/log生成0件。稼働中Prism一覧に新instanceがなかったため、launcher本体と既に停止済み旧instanceのconsole windowを通常終了し、20:01:29にPrism本体だけ再表示。新instanceの一覧表示には到達したが、その後の保全確認で停止条件を検出。新instanceの選択入力はgeometry unavailableで成立せず、起動ボタンは押していない。元instanceを起動したりゲーム操作したりしていない。
- **重要: CodexによるPrism通常終了・再表示に伴い、元instance metadataが再保存されたことを検出した**。事前/事後hash・mtimeとlauncher開始時刻は一致するが、OSのfile-write traceは取得していないため内部の書込callerまでは確定していない。元設定を手動編集・restoreせず、未説明の旧cfg差分を修復したことにもせず、その場で以降の起動を中止した。

| 元instanceの対象 | 今回開始前 | 今回停止後 | 差分 |
| --- | --- | --- | --- |
| `instance.cfg` | 2677 bytes / SHA `F7CE76548CA5A7535BDB51C3031BD69BD6120661F4CFD07F09CCAEABC64B2DEF` / mtime18:28:40.543 | 同一size/SHA / mtime20:01:29.870 | byte不変、mtime更新。以前の旧hashとの差分未解明は別件 |
| `mmc-pack.json` | 981 bytes / SHA `8C408637F203A2417A49793A448C405E76A68278FD3B05885B8DE5E84FA9CDD8` / mtime12:59:22.675 | 945 bytes / SHA `313DCBAB3CA09A22498DD5AE92F0E9E5BA4E670A3ECCA57BD531704F112EEBC3` / mtime20:01:34.900 | `/components/0/cachedVolatile: true`削除。MC1.20.1/Forge47.4.0/LWJGL3.3.1は不変 |

- 元mmc-packの旧内容は先行静的監査`historical-wrapper-path-review.json`内のparsed evidenceからメモリ内で再構成し、**981 bytes/保護hashと完全一致**を確認できた。現在との構造差分は上記1keyのみ。原本へ書き戻していない。「cache keyだけなので無害」と推測して停止条件を解除しない。
- UI上の選択accountはLeva9846 / MSA / 準備完了。設定画面は変更せずキャンセル。account file、token/password、完全command lineを取得していない。Prismは通常network条件で再表示し、Minecraft用loopback proxyは追加していない。system proxy/Firewall/router/port forwarding変更なし、MOD download/update/replaceなし。今回はMinecraft認証sessionの実起動成立をPASSにしない。
- **実Wrapper version、main menu、MOD loading、ERROR/FATAL/Mixin/dependency、historical smokeとの差分、Minecraft正常Stopping/exitはすべてNOT RUN / 未観測**。新Minecraft latest/debug log・crash reportは生成されていないが、それをstartup成功やERROR0件PASSとしない。Java/javawは20:05:06 JSTで0件。Prism本体PID35948は開いたまま、追加終了/再起動をしていない。
- 原本instance全13,351 filesの集合は同じで、変更は上記2 filesだけ。元world181、pristine181、前回boot186 filesは集合/hash/size/mtime不変。元ZIPも不変（SHA `B83E28255F90BEB356253AB60A5B12D9C8DBFB5E631C12309C38962A62AC5C7B`）。既存runtime source16,750 files、元mods/config/defaultconfigs/resourcepacks、旧Wrapper artifact、過去boot log、新instanceコピー924 files/104 Jar、v3成果物hashも不変。**元instance全体が不変という表現は使用しない**。
- 共有Prism metadata31/cache4,384/libraries453/assets12,049 filesは追加/削除/変更0。共有`metacache`は2,033,328→2,033,329 bytes（hash変更）、`prismlauncher.cfg`は4655 bytesのままhash/mtime変更。launcher logの番号ローテーションも観測。これらの通常launcher保存と、停止対象の元instance変更を分離し、隠していない。証拠は`control-before.json`/`control-prelaunch.json`/`control-after.json`/`control-after-diff.json`/`stop-review.json`/`final-preservation.json`/`process-final.json`。
- 変更範囲: 新instance、新audit root、Status。上記のPrism通常動作による元/共有metadata保存も別途明記。Food Healing Java/Mixin/resource/gameplay/Config/dependency/build/既存tests/仕様は未変更、**build / unit / check / GameTest = NOT RUN**。既存自動/手動PASSを変更しない。
- 次回は元instanceを列挙・再保存させずに使えるlauncher隔離方法と、このmetadata差分の取扱いを確認する必要がある。token/account fileコピーや原本復元を勝手に行わず、今回のworldless起動を再開済みとしない。新instanceは未起動の準備物として保持し、無断削除しない。これ以上の調査/起動は今回は行わない。
- **HISTORICAL-WRAPPER AUTHENTICATED COMBINATION = NOT TESTED**。
- **ORIGINAL-UUID WORLD RETEST = NOT RUN**。
- **V3 MIGRATION = NOT RUN / NOT AUTHORIZED**。world-copy試験、OPEN決定、他機能実装へ進まず停止。

#### 2026-09-06 CURRENT-WRAPPER CONTROLの本文承認・新instance準備

- 新完全独立Prism instance作成と原本の設定3ファイルのみの読み取りコピーを、利用者がチャット本文で明示承認。前回の権限停止履歴は保持し、今回の許可はworldless controlだけ。world/旧UUID再試験/v3 migrationは未承認のまま。
- audit: `build/verification/current-wrapper-control-20260906-195153`。新instance: `<LOCAL_DOWNLOADS>\PrismLauncher-Windows-MinGW-w64-Portable-9.1\instances\FHR_Source_CurrentWrapper_20260906_195153`。19:54:41 JST作成、924 filesをコピー。104 top-level Jarはsource保存hash一致、承認3設定もsource hash一致。元instance.cfg/mmc-pack.jsonは使わず、新規最小cfgと通常MC/Forge/LWJGL componentのみ。custom componentなし。world持込0、Java releaseは17.0.15。
- 準備script初回はJEIの`config/jei/world/.../bookmarks.ini`を実worldと誤認してコピー前に停止（新instance未作成）。許可済みconfigと実saveを区別するallowlistへ監査helperだけを修正し、再実行成功。実world/account/level.dat/region/session.lock禁止は維持した。
- Prism UIで選択Leva9846、MSA、準備完了を確認し、設定変更せずキャンセルした。account file/token/passwordは未読。既存launcherは維持。CLI `--help`確認用の別processは出力なく終了しなかったため当該probeだけを停止（Minecraftではない）。local binaryの通常`--dir`/`--launch`/`--profile`経路を確認。
- この準備段階ではMinecraft未起動、main menu/実Wrapper/116 runtime照合/終了結果は未判定。build/unit/check/GameTestはNOT RUN。次は当該新instanceのみを正規Prism経由で起動し、worldを開かずlog確認と通常終了を行う。

#### 2026-09-06 CURRENT-WRAPPER SOURCE CONTROLの準備・権限停止

- 最新の利用者添付`92da4b35-e98a-4383-aad2-4b3f4830f5fa/pasted-text.txt`は、historical Wrapper再現と別枠の**AUTHENTICATED CURRENT-WRAPPER SOURCE RUNTIME CONTROL / WORLDLESS SMOKE**を依頼。世界の持込/起動、server接続、v3差替え/migrationは引き続き禁止。UUIDは既存の9/5認証済み証拠を再調査せず維持した。
- 新audit root: `build/verification/current-wrapper-control-20260906-190419`。Status事前backup、保全台帳、sourceコピー候補manifest、設定3ファイルの差分、未実行の専用instance準備script/templateのみ。元Prism instanceをclone元として直接使用していない。
- 新Prism instance**予定**: `<LOCAL_DOWNLOADS>\PrismLauncher-Windows-MinGW-w64-Portable-9.1\instances\FHR_Source_CurrentWrapper_20260906_190419`、表示名予定`FHR Source Current Wrapper Control 20260906`。**19:11:29 JST確認でdirectoryは存在しない**。audit配下の`instance.cfg`/`mmc-pack.json`は新規templateであり、Prismへの配置は未実行。既存元cfgや他instanceからのcloneではない。
- 使用予定はMC1.20.1 / Forge47.4.0 / Microsoft Java17.0.15 / Food Healing2.2.5 / source116 ID-version / 104 top-level MOD Jar。既存source runtime/world-boot隔離コピーと保存済みhash台帳を比較し、**104 Jarはすべてsource hash一致のコピー元を確保**。world、旧boot world、pristineはコピー計画から除外した。**実起動logの116一致数・実Wrapper versionはNOT RUN / 未観測**であり、現cacheのprism-2026-08-01指定を使用実績として記録しない。
- new instanceは通常component（MC/Forge/LWJGL）のみの予定で、custom componentや旧Wrapper強制はない。Javaは既存隔離Javaから新instanceのruntime/javaへコピーする計画、proxy引数は追加しない。未解明の元instance.cfgを基準にせず新規の最小設定を用意した。
- **設定一致の未解決点**: `config/embeddium-fingerprint.json`、`config/embeddium-options.json`、`config/fml.toml`は、既存3隔離コピーのいずれもsource hashに一致しない。JSON/TOMLの読み取り比較で、fingerprintのp/s/t/u、Embeddiumの通知2項目（source true→copy false）、FMLのearlyWindowWidth/Height（source854x480→copy1100x700）が異なる。単なる整形差分ではない。今回のcopied source設定として黙って採用していない。
- 上記3ファイルだけを原本から読み取りコピーしてよいか、利用者へ確認中。元instance全体・元instance.cfgのclone禁止を勝手に緩めず、3ファイルのcopyも未実行。原本の設定値を編集/復元したり、過去隔離copyを修正したりしていない。`copy-plan.json`の`canPrepare=false`、`three-config-comparison.json`に差分を残した。
- **権限レビュー停止**: 新instanceだけへ既存隔離コピーを配置する`prepare_instance.ps1`をworkspace外書込の承認付きで要求したが、CreateProcess前に拒否。最新添付の承認箇所（5〜8/64/114行）を読み直して同じ操作の再審査を行ったが、「添付文書だけでは前回のclone作成禁止を解除できない」として再度拒否された。scriptは一度も実行されていない。UI操作や別shell等で回避しない。**新instance作成についてチャット本文での利用者承認が必要**。これはFood Healingの起動crashやsmoke FAILではなく、実行権限による準備BLOCKED。
- Prismは既存process/window一覧の読み取り確認だけ。今回の選択account `<MINECRAFT_ACCOUNT> / MSA`のUI再確認、認証操作、Minecraft起動には未到達。前回のUI観察と9/5の認証済みUUID証拠を維持するが、今回のMSA sessionを確認済みとはしない。account file/token/password、process command line全体を読み出していない。
- **main menu / MOD loading / dependency / Mixin / ERROR・FATAL / historical smoke差分 / 正常Stopping・exit = NOT RUN**。今回のMinecraft logはないため新規ERROR0件PASSとはしない。system proxy/Firewall/router/port forwarding変更なし、MOD download/update/replaceなし。Prism/Minecraftの通常network条件は今回未起動の予定条件のみ。
- 原本instance全体13,351 filesとshared metadata31/cache4,384/libraries453/assets12,049 filesの前後照合は、すべて追加/削除/変更0件。shared metacacheとprismlauncher.cfgもhash/size/mtime不変。19:13:31 JSTの最終保全照合で元world181/pristine181/前回boot186 files、元ZIP、既存runtime source16,750 files、104 MOD Jar、以前のboot logも不変。証拠はaudit配下の`control-after-diff.json`と`final-preservation.json`。元ZIP SHA-256は`B83E28255F90BEB356253AB60A5B12D9C8DBFB5E631C12309C38962A62AC5C7B`、v3 Jarは既存`943F10BDEE8CD59C5B43DA12A215F7A64B32BB0CB63BDD69E24BBC0669E95EA4`のまま。今回の差分0は、元instance.cfgの以前の未解明差分を修復/解決した意味ではない。
- **build / unit / check / GameTest = NOT RUN**。Gameplay/Java/Mixin/resource/既存Config/dependency/仕様/既存testは未変更。audit用helper/template/scriptとStatusだけを追加・更新した。19:17:11 JSTの最終確認でも新instanceは未作成、Java/javaw 0件。Status事前backupとの差分は今回記録の追加と更新時刻/次作業3の更新のみで、過去の試験結果行を削除していない。
- 次回は利用者からチャット本文で新instance作成と、必要な設定3ファイルだけの原本読み取りコピーの可否を確認した後、承認範囲内でこのworldless controlだけを再開する。未承認の間は再試行/回避をしない。既存MANUAL PASS、historical Wrapper gate、元cfg差分未解明、OPEN-01〜05、実2-client BLOCKED、別機能保留を維持。
- **HISTORICAL-WRAPPER AUTHENTICATED COMBINATION = NOT TESTED**。
- **ORIGINAL-UUID WORLD RETEST = NOT RUN**。
- **V3 MIGRATION = NOT RUN / NOT AUTHORIZED**。world-copy試験や別作業へ進まず停止。

#### 2026-09-06 ORIGINAL-UUID pre-flight blockerの静的追確認

- 今回の許可範囲は既存local evidenceの静的監査のみ。Minecraft/world/Prism instanceは起動せず、Prism UI操作・再認証・clone作成も行っていない。過去の条件付きboot承認を今回の起動許可へ読み替えない。新規audit rootは`build/verification/identity-wrapper-static-20260906-184610`。Status以外の既存project/原本への変更なし。

##### 1. 正規UUIDの過去認証証拠: MATCH

- **ORIGINAL ACCOUNT UUID / AUTHENTICATED HISTORICAL SERVER EVIDENCE = MATCH**、**ORIGINAL ACCOUNT UUID STATICALLY CORROBORATED**。対象serverは`build/verification/single-client-dedicated-20260905-141051/server`。既存手動試験のonline-mode=true記録に加え、現在の`server.properties`34行も`online-mode=true`、47〜48行は`127.0.0.1:25575`。今回変更していない。以下はusernameからのUUID計算やNBT上のprofileではなく、serverの`User Authenticator`が確定したUUIDと、その後の正常login/joinの実log。

| 2026-09-05 JST | server latest.log | server debug.log | 観測 |
| --- | --- | --- | --- |
| 14:50:42.270 | 95行 | 645行 | User Authenticator #1: Leva9846のUUIDを`<PLAYER_UUID>`と確定 |
| 14:50:43.660 / .742 | 96 / 97行 | 742 / 744行 | IPv4 loopbackからlogged in、joined the game |
| 18:22:50.578 | 175行 | 908行 | User Authenticator #2: 再接続でも同一UUIDを確定 |
| 18:22:51.998 / 52.003 | 176 / 177行 | 1005 / 1007行 | 正式IPv4接続先へ再login/join |

- 証拠は上記serverの`logs/latest.log`（SHA-256 `0D6DB43C3B4874C9FE68A45CE7209113E1FBFF234C583E4E27AE64CBA553195C`）と`logs/debug.log`（`FC38077B381BBF1B72074254D4227D079F53F7F43CA82771475ADEC7165298C1`）。抽出行番号/時刻/UUIDは新auditの`historical-identity.json`へ保存した。生logの日本語日付部分には文字化けがあり、9/5の試験記録と保存logの時刻を併用している。
- これにより「Leva9846の正規MSA接続が旧world UUIDと一致するか」の過去認証証拠は成立。**今後のcloneがそのsessionを実際に受け取ったこと、現在のsessionの有効性、旧Wrapper隔離起動成功は未試験**。既存STEP 1〜6 PASSの範囲や接続失敗履歴は拡張/変更しない。account file、password、access/client/refresh tokenを読み出し・コピーしていない。process command line全体も取得していない。

##### 2. instance.cfg: 旧内容完全復元不能（今回の探索範囲）

- projectの`build`/`backups`、Prismの`backup_9.1-`/`backup_9.4-`/`instances`/`prism_launcher_update_release`を対象に`instance.cfg*`とZIP内の該当entryを探索。source runtime/provenance/world bootの既存コピー・保護manifestも照合した。直接候補5 files、ZIP中央directory 225件（該当cfg entry 0、読取エラー0）、**旧hash一致copy 0件**。ZIPを展開・書換えず、account storeを読んでいない。探索台帳は`cfg-copy-search.json`。任意名の全ファイルや未確認の外部backupまで不存在と断言しない。
- 旧hash `F0E6DE273AAE4D5C551A1F4D7922BCD32E5CE1419AD607E256DF90685067F1B6` / 3,229 bytesに対し、現在は`F7CE76548CA5A7535BDB51C3031BD69BD6120661F4CFD07F09CCAEABC64B2DEF` / 2,677 bytes。**552 bytes減少**。現在はUTF-8として有効・BOMなし・CRLF63個・bare LF0、General section52 keys。旧raw本文がないため、encoding/改行/順序が変更されたかは不明。
- 保存済み`source-provenance-20260906-163349/forge-runtime-metadata.json`の選択済み10 Java項目では、`JvmArgs`だけが空entryから欠落へ変化。`JavaPath`、`OverrideJavaLocation=true`、`OverrideJavaArgs=false`、`OverrideMemory=true`、Java17.0.15/Microsoft/64bit、memory 16384/7680は一致。`mmc-pack.json`の全componentsも以前の構造と一致。
- **完全な追加key/削除key/値変更/順序差分は作成不能**。単一の`JvmArgs=`行削除だけでは552 bytes減少を説明できないが、他の意味的変更があったと断定する証拠もない。残りが既定値省略・UI状態・並べ替え・改行等のどれかを推測で決めない。`cfg-difference-limits.json`に確認可能な項目と未確認を分離した。
- `PrismLauncher-0.log`の該当する後半にはelapsed19770秒付近のJavaInstallList等のQt model reset warningがあるが、cfg保存・変更keyを示す記録は見つからなかった（`cfg-log-evidence.json`）。前回UI参照とmtime18:28:40が重なることは既知だが、**書込主体・全変更内容・因果は未証明**。`SOURCE INSTANCE CONFIG DIFFERENCE / CAUSE UNCONFIRMED / NEEDS REVIEW`を維持し、無害な自動正規化・利用者操作とも断定しない。restore/editは実施していない。

##### 3. 認証済みPrism + historical Wrapper: 候補のみ、READY未成立

- 実物の`meta/net.minecraftforge/47.4.0.json`は`uid=net.minecraftforge`、`version=47.4.0`だが、`libraries`内のWrapperは`io.github.zekerzhayard:ForgeWrapper:prism-2026-08-01`を指定。**Forge versionだけを47.4.0へ固定しても旧Wrapper固定にはならない**。現在のcacheが選ぶ既定値は新Wrapperだが、Prismがcustom指定を必ず上書きすることは未証明。
- `PrismLauncher-3.log`265行にはhistorical `ForgeWrapper/prism-2025-12-07/ForgeWrapper-prism-2025-12-07.jar`の参照があり、保存済み6/30環境との対応調査を再照合。`PrismLauncher-1.log`841/871行の9/5環境には新Wrapper参照がある。認証引数を含む起動command全体は保存せず、Wrapper path断片だけを抽出した。
- 旧Wrapperは既存local artifactとして実在。29,731 bytes、SHA-1 `4c4653d80409e7e968d3e3209196ffae778b7b4e`、SHA-256 `11F5790BD1C8A757E38FEAEFFF181F14DC11BD941B7953E3678C02157EE1D83E`。所在は元Prism `libraries/io/github/zekerzhayard/ForgeWrapper/prism-2025-12-07/ForgeWrapper-prism-2025-12-07.jar`。元/既存copyとも未変更。全116 artifactの歴史的byte同一性証明ではない。
- 現Prism10.0.5実行fileの静的文字列/symbolに`Component::customize`、`Component::isCustom`、`PackProfile::patchesPattern`、`ComponentUpdateTask`、`patches`、`cachedVersion`、`MMC-hint`、`localPath`が存在する。custom component機構の存在は裏付けられるが、**文字列/symbolだけではlocal file優先順位・update時の分岐・path解決・書込先は証明できない**。調べた5 instanceに既存`patches/*.json`はなく、同条件でcustom old Wrapperを使った認証済み起動logも未発見。local source/分岐実装の証拠不足を残す。
- **未実施のclone側設計候補**: `mmc-pack.json`のMC1.20.1/Forge47.4.0/LWJGL3.3.1は維持。clone内`patches/net.minecraftforge.json`をcustom Forge componentとする方式を候補にし、Wrapper libraryの`name`と対応する`downloads.artifact`のpath/hash/size等だけを旧実物へ整合させる。**このpathの採用順序や必要custom flagは未検証であり、確定手順ではない**。`mainClass=io.github.zekerzhayard.forgewrapper.installer.Main`、他library/mavenFiles/Forge引数は維持する。変更済みJSONやcloneは作っていない。
- Forge metadataの`minecraftArguments`には`${auth_player_name}`、`${auth_uuid}`、`${auth_access_token}`、`${user_type}`が存在する。これは**未展開template**の確認でありtoken読取りではない。候補でもPrism自身の通常session展開を維持し、手入力UUID・token抽出・Wrapper直起動を使わない。Javaはclone側`OverrideJavaLocation=true`と`JavaPath`でMicrosoft17.0.15の隔離copyを指定する構成が必要で、元cfgへ変更を加える候補ではない。
- **残る技術gate**: custom componentのremote metadataに対する優先/更新規則、library競合時の旧Wrapper選択、Prism/Forgeのlibraries/assets/生成物/metadata cacheへの書込先隔離をlocal実装等で確認する必要がある。通常のinstance cloneでgame directoryが分離されても共有cacheの不変までは保証されない。別launcher root方式も、既存正規sessionをaccount storeコピー/再認証なしで使えると仮定できない。`MMC-hint`の値を推測して隔離成功とはしない。
- 判定: **BLOCKED - AUTHENTICATED HISTORICAL-WRAPPER ISOLATED LAUNCH PATH NOT STATICALLY ESTABLISHED**。`AUTHENTICATED HISTORICAL-WRAPPER ISOLATED LAUNCH PATH / READY FOR USER APPROVAL`は**未付与**。「必ず新Wrapperへ置換される」「旧Wrapperでは起動不能」のいずれも断定しない。元instance編集は候補に含めないが、元cacheへ副作用なく起動できる完成手順は今回確立していない。詳細は`historical-wrapper-path-review.json`、`prism-binary-strings.json`。

##### 保全・検証結果・次の作業

- 今回の開始/終了保護台帳18,196 filesはSHA-256/size/mtime/列挙対象の差分0（`protected-before.json`/`protected-after.json`/`protected-diff.json`）。現在の元instance.cfg、mmc-pack、元mods/config/defaultconfigs/resourcepacks、Java/libraries/assetsの既存runtime参照集合、Food Healing Jarを変更していない。前回以前のcfg差分を消したという意味ではない。自動更新等で増え得る全launcher cacheの全世界的な不変を保証するものでもない。
- 別の既存manifest照合でも原本world181 / pristine181 / 旧boot186のfile集合/hash/size/mtime差分0、元ZIP不変、runtime source16,750 files差分0、copy MOD104件の増減・変更0、旧boot log4本不変。`final-preservation.json`へ保存。新しいboot worldは作っていない。
- audit helperの初回保護台帳作成は秘密情報候補名の拒否guardで停止した（Java同梱license `pkcs11cryptotoken.md`の名前に反応、対象fileを読む前）。該当名を除外して再実行し成功。これは監査helperの失敗→修正履歴で、Minecraft/build/GameTestのFAILではない。後続の既存runtime manifest照合は非秘密のJava licenseも含む。
- **build / unit / check / GameTest / runClient / runServer = NOT RUN**。Gameplay/Java/Mixin/Config/resource/dependency/既存test/仕様は未変更。18:56:01および18:59:35 JSTのprocess確認でJava/javaw 0件。Prism UIを開かず、既存Prismを起動/終了/再認証していない。network/proxy/Firewall/router変更、外部downloadなし。Status差分は新規記録と現在の概要/次作業だけで、過去のPASS/FAIL履歴に削除なし。
- 次回は今回のUUID照合を再試験要求へ戻さず、未確認のPrism custom component優先/更新/書込隔離に関するlocal source等の証拠と、旧cfg preimageまたは未解明差分を踏まえた原本保護方針を確認する。合成GameTest/unitはこのPrism実装・実session経路を証明できず、だからといってworldや人間ゲームプレイ試験を追加要求しない。今回は静的監査のみで停止。
- **ORIGINAL-UUID RETEST = NOT RUN**。**V3 MIGRATION = NOT RUN / NOT AUTHORIZED**。既存MANUAL PASS、旧full bootのNOT PASS、実2-client BLOCKED、OPEN-01〜05、別機能の保留gateを維持する。

#### 2026-09-06 元UUID・通常outbound再試験のpre-flight停止

- **BLOCKED - ORIGINAL ACCOUNT UUID / AUTHENTICATED RUNTIME IDENTITY REQUIRED**。今回の利用者指示は、pristine由来の新しい隔離boot copyを元UUIDで1回だけ通常load/saveする条件付き承認。通常outbound継承、既知baseline ERRORの比較方法、Firewall dialog時の人間キャンセル方針も承認済みとして受領した。以前の「これらの方針自体が未承認」という状態とは区別する。ただしidentity確認失敗時の即停止条件を満たしたため、**今回のMinecraft/full boot試行は0回 / WORLD NOT OPENED**。前回の試験UUIDによるboot copyは再利用せず、新しいruntime/world copyの作成にも進んでいない。
- 最新Status、AGENTS/CODEX_LOOP、元instanceの`mmc-pack.json`、保存済みsource runtime/Wrapper証拠を読み直した。使用予定はMC1.20.1 / Forge47.4.0 / Microsoft Java17.0.15 / `prism-2025-12-07` / source116 ID/version・104 top-level MOD Jar / Food Healing2.2.5。これは今回の**予定条件**であり、起動logで再検証済みとはしない。MODを追加/削除/更新せず、v3 Jarへ差し替えていない。

##### identityの確認範囲と未成立条件

- 起動済みPrism Launcher10.0.5のメイン画面・アカウント一覧を参照し、選択表示のplayer名`<MINECRAFT_ACCOUNT>`、種別`MSA`、状態`準備完了`を確認した。Offline Accountは選択/追加していない。認証画面の操作、アカウント再読み込み、設定値の変更・OK/適用は行わず、参照した設定画面はEscapeでキャンセルして閉じた。UI要素指定のキャンセルは一度cached element不在となったため再観測後にEscapeを使用し、閉鎖を確認した。
- 必須UUIDは`<PLAYER_UUID>`。確認できた一覧にはUUIDが表示されず、**verified runtime UUID = 未確認 / UUID一致 = NOT CONFIRMED**。これはUUID不一致を検出したという意味ではない。MSA準備完了というUI状態、旧world内のUUID、過去の認証済み試験を今回起動するruntime identityの完全一致の代用にはしていない。
- 前回のsource smoke/full boot用helperは保存済みargfileを使うWrapper直起動であり、正規Prism認証sessionの引渡し機構ではない。旧Wrapperの実在と以前の起動成立だけでは今回の認証条件を満たさない。現在のPrism側の過去起動記録には`prism-2026-08-01`もあるため、認証済みPrismを起動すれば旧Wrapperになるとは仮定しない。**現在の認証済みsessionを秘密情報に触れず、独立root内の旧Wrapperへ渡す起動方法は今回未確立**。技術的に永久に不可能と判断したものではないが、未確認のままworldへ進まない。
- account file全体/部分、password、access token/client token等を読み出し・コピー・保存していない。process command line全体も取得していない。期待UUIDをargfileへ手入力した偽装起動、NBT編集、playerdata rename/merge、元instance起動は行っていない。人間へ秘密情報の提供も求めていない。

##### 原本保護照合とinstance.cfg差分

- 新規**audit専用**root: `build/verification/source-original-identity-preflight-20260906-183124`。`preflight-report.json`、`final-preservation.json`、`protected-review.json`、Status変更前backup、読み取り照合helperのみ。実world/runtimeを持ち込んだ再試験rootではない。
- 元world181 files、前回未起動pristine181 files、前回保存後boot186 filesは、前回manifestからfile集合/SHA-256/size/mtimeすべて差分0。元ZIP SHA `B83E28255F90BEB356253AB60A5B12D9C8DBFB5E631C12309C38962A62AC5C7B`も不変。runtime source16,750 files、元mods/config/defaultconfigs/resourcepacks集合、copy側104 MOD Jarは不変。保護world台帳1,440件も差分0。
- **保護台帳598件のうち1件だけ差分**: 元Prism `instances/1.20.1/instance.cfg`。前回は3,229 bytes / SHA `F0E6DE273AAE4D5C551A1F4D7922BCD32E5CE1419AD607E256DF90685067F1B6` / mtime12:59:46.640976、現在は2,677 bytes / SHA `F7CE76548CA5A7535BDB51C3031BD69BD6120661F4CFD07F09CCAEABC64B2DEF` / mtime18:28:40.543182 JST。**SOURCE INSTANCE CONFIG DIFFERENCE / CAUSE UNCONFIRMED**。Prism UI参照中と時刻が重なるため、UI参照に伴う自動保存の可能性も排除できない。事前からの変更・人間操作・無害な整形と断定しない。元instance全体が不変という必須条件は現時点では成立していない。
- 以前保存した選択済みJava metadataとの比較では、`JvmArgs`が空文字の明示entryからentry欠落へ変化。他の比較対象（Java17.0.15/Microsoft、JavaPath、memory、Override指定等）は一致し、`mmc-pack.json`のcomponentsも一致。ただし旧`instance.cfg`の全内容preimageを今回保持していないため、これが全差分だとはしない。空entry欠落の意味も推測で同等扱いせず、元cfgを自動復元/編集していない。
- この差分はFood Healingのworld data破損を示すものとは未判定。原本world/ZIP/pristine/MODの不変と、instance設定の保護条件未達を別々に記録する。今回のaudit helperは自身の新規audit root以外へ書き込まない。

##### 起動結果・次の条件

| 項目 | 今回の結果 |
| --- | --- |
| 正規player名 / UI種別・状態 | <MINECRAFT_ACCOUNT> / MSA / 準備完了（UI観察のみ） |
| 起動前runtime UUID完全一致 | NOT CONFIRMED / pre-flight BLOCKED |
| 新規Minecraft起動 / world load / gameplay | NOT RUN / 0回 |
| network条件 | 通常outbound継承は条件付き承認済み。ただし新process未起動、旧proxy argfileは未変更。system proxy/Firewall/router変更なし。 |
| ProjectE Checker / 既知234 ERROR再比較 / 新規ERROR数 | NOT RUN / 今回のlogなし。新規0件PASSとは記録しない。 |
| MTR listener / Firewall dialog | 新規runtime未起動。今回のMTR/Firewall動作は未試験。 |
| Food Healing / ProjectE EMC・knowledge / 外部UUID data / Inventory・XP前後 | 新規load/saveなし、今回の前後比較はNOT RUN。過去の値・静的比較履歴は維持。 |
| 正常save / integrated server stop / client exit | NOT RUN（起動していないため） |
| process残留 | 18:34および最終18:39:48 JST照合でJava/javaw 0件。既存Prism Launcherは終了/再起動していない。元instance.cfgの最終hashも上記の観測差分値から追加変化なし。 |
| build / unit / check / GameTest | NOT RUN。ゲームコード・構成・test変更なし。 |

- 変更対象はStatusと新規audit rootのみを意図して実施。上記元instance.cfgの観測差分は例外として明示し、隠したり巻き戻したりしない。Java/Mixin/resource/Food Healing Config/仕様/dependency/build/既存testは変更なし。既存MANUAL PASS、前回full bootのNOT PASS、実2-client BLOCKED、OPEN-01〜05を変更しない。
- 次回必要な条件は、**秘密情報を扱わずに確認できる正規元UUIDと、旧Wrapper条件・隔離rootを保つ認証済み起動経路**、および**元instance.cfg差分の原因・保護方針の確認**。usernameだけの確認やsynthetic/unit/GameTestでは実アカウントsessionを証明できない。今回は追加のゲーム内人間試験を要求せず、指定されたpre-flight停止条件に従って終了する。
- **ORIGINAL-UUID RETEST = NOT RUN / BLOCKED AT PREFLIGHT**。再試験の条件付き承認を未承認扱いには戻さないが、未成立のidentity/protection条件を飛ばして開始しない。**V3 MIGRATION = NOT RUN / NOT AUTHORIZED**。新しい実装・別MOD作業へ進まず停止。

#### 2026-09-06 実v2.2.5 full boot停止理由の静的レビューのみ

- **STATIC REVIEW COMPLETE / NO RETEST / USER DIRECTION REQUIRED**。最新Status、AGENTS/CODEX_LOOP、前回保存済みauditと実Jar/JDK bytecode、6/30 log、pristine/boot保存NBTを読み取り照合した。新規証拠rootは`build/verification/source-stop-review-20260906-174718`。Minecraft/worldは起動せず、外部URLへの接続、proxy/Firewall変更、MOD更新/削除/置換、NBT/UUID編集、v3差し替えは一切行っていない。以下は静的な原因分類であり、再試験・修復成功ではない。

##### 1. ProjectE UUID Checker: PROXY CONNECTION FAILURE / AUXILIARY JOIN-MESSAGE FETCH

- 対象実物は`ProjectE-1.20.1-PE1.0.1.jar`。`ThreadCheckUUID.run()`の固定URLは`https://raw.githubusercontent.com/sinkillerj/ProjectE/mc1.14.x/haUUID.txt`、hostは`raw.githubusercontent.com`、HTTPS既定port443。1.20.1 Jar内にもこのbranch名が実在する。URLを推測したりアクセスしたりしていない。`URL.openStream()`で一覧を読む処理で、今回確認したコードはplayer UUID/EMCをquery/bodyへ送らない。
- 成功時は先頭行を読み、後続の空行以外を`###UUID`またはEOFまで収集し、static `PECore.uuids`へ追加する。その消費先は`PlayerEvents.onHighAlchemistJoin`のplayer UUID照合と、`PELang.HIGH_ALCHEMIST`の装飾付き参加chat broadcast。認証・EMC・知識・world破損検査ではない。104 top-level Jar内41,387 classの定数参照走査で、このfieldへの直接参照候補はProjectEの`PECore`/`ThreadCheckUUID`/`PlayerEvents`だけ。reflection/dynamic/nested利用まで不在を証明したものではない。
- 今回の`ConnectException`はbytecode17の`openStream()`で起き、一覧追加bytecode106以降に到達していない。`IOException` catchでFATAL marker付きERRORをlogし、`hasRunServer=true`としてthreadがreturnする。`PECore.serverStarting`は`Thread.start()`後にreturnし、join待ち/server停止/JVM終了はしない。**この失敗経路にworld/player NBT、EMC、capabilityの更新・初期化はない**。影響は一覧取得失敗による該当playerの特別な参加表示の欠落であり、「全ProjectE機能が正常・影響が一切ない」とは拡張しない。保存後EMCも41,809,152のままだったことを別のNBT証拠で確認。
- proxy因果の根拠: 保存済み`source-world-boot-20260906-172148/launch-args.txt`に`java.net.useSystemProxies=false`、`http.proxyHost=127.0.0.1`/`http.proxyPort=1`、`https.proxyHost=127.0.0.1`/`https.proxyPort=1`。同梱Java17.0.15の`conf/net.properties`は`http.nonProxyHosts=localhost|127.*|[::1]`で対象hostは除外されない。JDK `DefaultProxySelector`のHTTPS→`https.proxyHost/Port`選択と、`HttpClient.openServer()`のHTTP proxy分岐をbytecodeで照合。**実stackの`HttpClient.openServer(HttpClient.java:633)`は`proxy.address()`→`privilegedOpenServer()`のbytecode93〜104に対応**し、直結分岐ではない。保存引数・実stack・JDK実装から、閉じたloopback proxyへのTCP接続拒否で説明できる。通信capture/再接続はしておらず、GitHub停止・DNS障害や外部hostへの実到達を推定しない。
- 6/30元`latest.log`/`debug.log`には同Checker例外、URL、明示的成功/通信記録は見つからなかった。元/隔離ProjectE configはhash一致、`debugLogging=false`で、調べたconfigにUUID Checker設定はない。コードに明示的成功logがないため、**6/30の取得成功はNOT PROVEN**。現在の拒否設定でのみERRORが観測された事実と分離する。
- 前回の停止判断は利用者の停止条件に従った履歴として維持する。これは**ERROR severity + FATAL marker**であり、JVM FATAL crashではない。今回の静的分類だけで前回full bootをPASSに訂正しない。

##### 2. world load追加234 ERROR: セッションを限定した厳密比較

- 6/30元logには3つのworldセッションがある。対象は第2セッション（前回保存完了行2456の後〜行4186、終了22:04:15.115）。元world `LastPlayed=22:04:15.067`と対応し、第3セッションは別の新規worldなので除外した。初期の全3セッション合算比較は件数が膨らむため採用せず、`log-comparison-all-sessions-initial.json`として残した。
- ProjectEを除く追加234件それぞれについてlogger、完全message、resource ID集合、exception型、順序付きstack method、同一logger/messageの出現件数を比較。時刻/thread/stackのJar注記・source行番号の違いは比較項目と生継続行を分けて保持した。**231件は上記項目・件数だけでなく生のstack継続行も一致**。残り3件はlogger/resource/exception/件数と主要失敗部分は一致するが、全stackが異なるため保守的にPARTIALとした。

| ERROR分類 | 件数 | HISTORICAL BASELINE MATCH | PARTIAL MATCH | NEW / UNEXPLAINED |
| --- | --- | --- | --- | --- |
| TagLoader missing reference | 4 | 4 | 0 | 0 |
| ForgeHooks loot parse | 4 | 4 | 0 | 0 |
| RecipeManager parse | 204 | 204 | 0 | 0 |
| ServerAdvancementManager parse | 14 | 14 | 0 | 0 |
| TConstruct/TicEX material stats | 3 | 3 | 0 | 0 |
| TicEX tool definition | 1 | 0 | 1 | 0 |
| TicEX station slot layout | 1 | 0 | 1 | 0 |
| JEI ingredient filter | 2 | 2 | 0 | 0 |
| Patchouli book | 1 | 0 | 1 | 0 |
| 合計（ProjectEの別1件を含めない） | 234 | 231 | 3 | 0 |

- PARTIAL 3件: `ticex:revival_spellbook` tool（boot2380 / 元3093、`JsonSyntaxException`）、`ticex:revival_spellbook_irons` slot layout（2463 / 3176、`JsonSyntaxException`）、`yuushya:yuushya_guidebook`（2832 / 3478、`RuntimeException`）。3件とも先頭10 method frameは一致。前2件は旧worldリストdouble-clickと今回の選択worldを開くbuttonのUI呼出し経路が異なる。全3件で旧Prismの`StandardLauncher.launch`/`EntryPoint.listen`/`EntryPoint.main`の末尾が今回のWrapper直起動にはない。未登録resource等の失敗原因が消えたものではない。
- `HISTORICAL BASELINE MATCH`は**再現した既存ERROR**の分類のみ。recipe/loot/advancement/book等の欠落影響・全MOD互換をPASSにしない。全253 ERRORのうちsmoke由来18件、上記234件、ProjectE新規1件の区別を維持する。

##### 3. 元UUIDと試験UUID: 保存保持とownership確認範囲の分離

- 元`<PLAYER_UUID>`と試験`00000000-0000-0000-0000-000000000001`を区別し、pristine/保存後bootのlevel Player、両UUIDのplayerdata、外部SavedData/JSON、region/entity/POI NBTを読んだ。非空MCA内レコードは前22,153/後22,136件を読み取り、これは読解対象数であり全chunkの状態一致を意味しない。各側22個の0byte region fileは無内容として分類し、破損とは断定しない。初期scannerが`chunk-slot-32/Position=[0,1]`を試験UUID long-arrayと誤検出した1件は、実型IntArrayのchunk座標と照合して除外した。未解読のMTR独自31byte設定6件はhash差分だけを保存し、網羅的な所有情報不存在の証明とはしない。
- 実playerの18 capabilityは**16件が型付きNBT一致、2件はlist順のみ変化**。`projecte:knowledge`はknowledge36件の同一multiset、EMC文字列41,809,152不変。`solcarrot:food`はfoodList28件の同一multiset。Food Healing両capabilityは完全一致（Lv2/count35/DisabledSkills空/All28/Current3/bonus10）。残る一致capabilityはCurios inventory、Flux player、Iron Furnacesのfurnaces_list/show_config、mcjtylib preferences、Mekanism radiation、Polymorph player_recipe_data、ProjectE alch_bags、ProjectExpansion alchemical_book_locations、SlashBlade concentration/inputstate/mobeffect、TaCZ synced_entity_data、TConstruct persistent_data。

| 対象 | 実data / 実bytecodeの確認 | 試験UUIDでは未確認の範囲 |
| --- | --- | --- |
| ProjectE / ProjectExpansion | `knowledge`/EMC保持、alch_bagsとalchemical_book_locationsは空のまま。`TransmutationOffline`はUUID-keyed cacheとUUID別playerfileのForgeCaps knowledge読込を持つ。Checkerの参加表示処理とは別系統。 | 元UUIDを使うoffline EMC/knowledge API、所有者照合、非空alchemical bag/book位置の操作。EMC数値一致だけでは元identityの再現にならない。 |
| TaCZ source **1.1.8-hotfix** | 実cap `tacz:synced_entity_data=[]`は不変。`DataHolderCapabilityProvider`はsave対象のClassKey/DataKey/Valueをplayer capabilityへ保存、join/cloneでentityごとに同期する。収納内`AmmoId=tacz:22wmr`の32発も保存保持。 | 元player entity/UUIDでの同期や実銃動作は未試験。v3側正式対象1.1.7-hotfix2の統合結果へ混同しない。今回は保存経路だけの静的監査。 |
| Sophisticated Backpacks/Core/Storage | `sophisticatedbackpacks.dat`に27slot収納1件・非空item24slot・access log1件があり、型付き値は前後一致。BackpackStorage/Wrapperの`contentsUuid`、Storage側ItemContentsStorageのUUIDは**収納identity**でplayer UUIDではない。AccessLogRecordはbackpackUuidとplayerNameを保存し、今回の旧name記録はそのまま。Storage独立SavedDataは対象worldに存在しない。 | 元playerの持物/Curiosから同じ収納へ到達できるか、名前付きaccess log、Storageや自動給餌等の実操作は未確認。全UUIDをplayer UUIDとして書換えてはいけない。 |
| Flux Networks7.2.1.15 | player cap不変、`fluxnetworksdata.dat`はnetworks空。実`FluxNetwork`のowner/members、`NetworkMember.playerUUID`、getPlayerAccessはplayer UUID照合。 | 非空networkのowner/member/security権限・wirelessアクセス。空network保存の一致で引継ぎPASSにしない。 |
| RFTools / mcjtylib8.0.6 | preferences cap不変、`RFToolsDimensions.dat`のdimensions空。GenericTileEntityはowner名/`ownerId` UUID/securityChannelを保存する。対象2 UUIDへのchunk内一致は未検出。 | 元UUID所有machine/dimension/securityの操作。未検出は間接所有も含めた不存在の証明ではない。 |
| Mekanism10.4.16.80 | radiation cap不変。TileComponentSecurityは`componentSecurity/owner`、SecurityFrequencyはownerとtrusted UUID、FrequencyManagerはowner別管理/保存名を使う。対象worldには独立frequency/security SavedDataを確認できず、対象2 UUIDの該当chunk記録も未検出。 | owner/private/trusted frequency・machine利用。放射線capの一致から所有権までPASSにはしない。 |
| その他実player/外部data | 元UUIDのplayerdataはbyte不変のまま、試験UUIDのplayerdata/advancement/stats/.cosarmorが別生成。JourneyMapのJMPlayerSettingsには旧entryを残し新UUID entry追加。Chunk Loaders active_playersのplayer UUIDは旧→試験へ変化。jamd miningのvillager Gossips Targetは旧UUIDのまま。 | 元playerのadvancement報酬/統計/外観装備、player別chunk活性、地図設定、村人評価など。level.dat Playerのcap保持だけでは旧UUID依存状態を網羅しない。 |

- **新たに記録する前回bootの保存差分（今回の変更ではない）**: Inventoryの既存slot値は保持されたが、slot31へ`tconstruct:materials_and_you`1冊追加。XpTotal/Scoreは13,417→13,437（+20）、XpLevel71は同じ。ActiveEffects/Attributes/Rotation等にも差分があり、`player-capability-comparison.json`へ保存した。ゲームプレイ操作をしなくても通常load/saveによる変更はある。**本/XPの発生原因をUUID差と断定していない**。これらを自動復元せず、旧player全状態一致とはしない。
- `STATIC AUDITED - SELECTED SAVE/UUID PATHS ONLY`。すべてのMOD内部のUUID利用、間接参照、独自binary保存を網羅した監査でも、外部MODのAUTOMATED/MANUAL INTEGRATION PASSでもない。変更済みboot worldをpristineへ戻したり今後の基準に流用したりしない。

##### 4. MTR / Firewall: SOURCE FEATURE / HISTORICAL LISTENER MATCH

- `MTR-forge-4.0.0-beta-11+1.20.1.jar`の`Init`→`Main`→`Webserver`を照合。server開始callbackがworld save配下`mtr`とdimension群を渡してsimulation dataを読み、正のwebserverPortでJettyのmap/OBA用HTTP serverを開始する通常経路。Webserverはconnectorのportを設定するがhostを限定せず、現在の元/隔離`config/mtr.json`はhash一致、`webserverPort=8888`/`useThreadedSimulation=false`。MODの通常設定・実装であり、Food Healingが開くserverではない。
- 6/30元latest.logにも`0.0.0.0:8888`が3回記録され、対象の第2source-worldセッションは**21:54:17.661（3338行）に起動、22:04:14.810（4155行）に停止**。第1・第3セッションにも同型の起動/停止がある。debugにはIPv6 wildcard socketの記録もあるため、`0.0.0.0`というJetty表示だけでIPv4 loopback限定とはしない。
- MTR world simulation読込とHTTP開始は同じserver lifecycleに属するが、HTTP開始logは全world data正常性や地図API正常性の証明ではない。通常stopでwebserverも閉じるコードと前回終了logを照合した。元worldの`mtr`保存dataに今回の静的作業から変更は加えていない。
- **FIREWALL DIALOG MAY RECUR / USER ACTION UNCONFIRMED**。前回は新しい隔離Java実行pathで実際にWindows許可dialogを観測しており、再試験時も出る可能性がある。OS rule/profileや前回の人間の選択は未確認なので、許可済み・再表示しない・完全に閉鎖済みとは推測しない。今回はFirewall rule/OS設定/port/proxyを一切変更せず、listenerへのアクセス試験もしていない。

##### 保存保護・次回再試験条件

- `final-preservation.json`/`protected-review.json`: 原本world181、pristine181、前回保存後boot186のfile集合/hash/size/mtimeは前回終了監査から差分0。元ZIP SHA `B83E28255F90BEB356253AB60A5B12D9C8DBFB5E631C12309C38962A62AC5C7B`は不変。runtime source16,750 files、元mods/config/defaultconfigs/resourcepacksの集合、copy側104 top-level MOD Jarも不変。Prism設定等598件/保護world1,440件の既存台帳も差分0（重複を加算しない）。前回bootの4 logはhash不変、該当source Minecraft process残留0。原本/boot/pristineへの書込みなし。
- 変更対象は**docs/CODEX_STATUS.mdと新規静的audit rootのhelper/JSON証拠のみ**。Status変更前backupを同rootへ保存。主要証拠は`bytecode/`（Jar/class SHA付き）、`jdk/`、`log-comparison-detailed.json`（234件全比較）、`supporting-evidence.json`、`saved-state-reviewed.json`。初期比較/単純UUID走査とレビューによる訂正も消さず保持する。Java/Mixin/resource/Config/dependency/build/仕様/既存testは変更なし。**build/unit/check/GameTest/runClient/runServer/再full bootはNOT RUN**、既存の自動・人間PASSは変更なし。
- 次回再試験には**別の明示承認**が必要。今回の結果を踏まえ、(1)同じ閉じたproxyのProjectE一覧取得失敗をどの試験範囲・停止条件で扱うか（通信許可へ自動変更しない）、(2)元UUID/元player identityでどの経路・範囲を確認するか（NBT/UUID書換えで偽装せず、認証済み元アカウント等の条件を事前確認）、(3)既存234 ERRORを含むsource baselineの影響・許容範囲、(4)MTR wildcard listenerとFirewall dialogの扱い、を利用者と確認する。歴史的Jar byte同一性は未証明のまま。再試験する場合も原本/pristine保全、新規boot copy、同じsource version、差分比較と停止条件を維持する。
- **BLOCKED - RETEST SCOPE / PLAYER IDENTITY / NETWORK POLICY USER DIRECTION REQUIRED**。静的レビューだけでは元player ownershipやworld機能の安全性を確定できず、前回full bootは**ATTEMPTED / STOPPED / NOT PASS**のまま。今回を再試験承認と解釈しない。**V3 MIGRATION = NOT RUN / NOT AUTHORIZED**。OPEN-01〜05、実2-client BLOCKED、未提供artifact/保留機能のgateを変更せず、ここで停止する。

#### 2026-09-06 実v2.2.5 worldコピーfull boot・停止条件・保存後監査

- **総合判定: REAL V2.2.5 WORLD COPY / FULL BOOT ATTEMPTED / STOPPED - WORLD LOAD ERROR REVIEW REQUIRED / NOT PASS**。world選択・ロード・integrated server起動・player spawn・chunk/world表示には到達。停止条件となる新規logを検出したため、ゲームプレイ/修復/再試行はせず、ESC→セーブしてタイトルへ戻る→Minecraftの「終了」で終了した。world描画成立やexit0を根拠にエラーを無害扱いしない。
- 使用環境は前回smokeと同じMC1.20.1 / Forge47.4.0 / Microsoft Java17.0.15+6-LTS / ForgeWrapper `prism-2025-12-07` / Food Healing2.2.5。起動log対source LoadingModListは**116 ID/version一致・差分0**、104 top-level MOD Jar。現在hashは前回表および本rootの`audit/runtime-manifest.json`と同一で、Food Healing2.2.5 SHAは`5BAC83E55419D96849823D7A7BB0933E01600564E13739992E43782C925A6A8C`。version変更、MOD削除/置換/download、Forge downgrade、v3差し替えなし。歴史的全Jar byte同一性は引き続き未証明。
- 今回の独立rootは`<LOCAL_PATH>/food-healing-mod-main\build\verification\source-world-boot-20260906-172148`。起動したworldは`game/saves/圧倒的物量で大都市を作るMinecraft`だけ。`pristine-world/圧倒的物量で大都市を作るMinecraft`は一度も起動せず、今後の承認された試験用に保持する。原本/ZIP→pristine→bootの開始前manifestと、終了後manifestを別fileで保存した。

##### 起動・保存・終了の観測範囲

- 17:28:44.166 JSTにPID32748を可視desktopで起動。17:29:22 main menu、起動完了40.81秒。唯一のboot用worldを通常選択し、17:30:29.276 integrated server開始、17:30:29.903 overworld spawn region準備、17:30:41.165 player login at `(-31.5, 63.0, 48.5)`を確認。終了経路へ進む際の画面でもworld、配置済みblock/torch、旧食義Lv2/count35表示を確認した。移動・戦闘・クラフト・item操作・command実行はしていない。
- 停止条件検出後、Windows Firewallの許可確認が画面操作を遮った。Codexはセキュリティ選択・rule/config変更を行わず、人間へ「キャンセル」のみを依頼。その後dialogが消えたことは確認したが、人間から選択内容の明示報告はないため、**人間の選択やFirewall設定全体の不変性は未確認**。許可済みと推測せず、起動や試験を続行せず通常終了へ進んだ。
- MTR内蔵HTTP serverが`0.0.0.0:8888`で起動したこともlogへ記録。CodexによるLAN公開/port forwarding/Firewall変更ではないが、将来の旧環境再試験時のnetwork確認事項として残す。17:33:50.647 `Stopped ServerConnector ... :8888`、17:40終了後のLISTEN照合でも8888残留なし。
- 17:33:50.672 `Stopping server`、同.673 `Saving players` / `Saving worlds`、17:33:51.250 **`All dimensions are saved`**。overworld/Nether/End/jamd各dimensionの通常保存を確認。17:33:51.365 TitleScreenへ復帰、17:37:01.668 `Stopping!`、**17:37:03.026 JST process exit0 / processReaped=true**。17:40:34の追確認でも当該rootのJava processは0件。通常save/stop開始以降の新規ERROR/FATALは0件、crash report生成0件。

##### ERROR / exception差分と停止理由

- `game/logs/latest.log`のERRORは**253件（worldless smoke 18件から+235件）**。smokeの18件は同じmessage/countで再発。Yuushya `minVersion`1件、Yuushya/TicEX/SlashBlade model14件、Mekanism `inputInputSlot`1件、Food Healing2.2.5 max health cap extension1件、Iron Furnaces update check1件。発生位置は引き続きworld選択前。ModernFix/NightConfig FileWatcherの`ConcurrentModificationException`もworld前に1件、同じ主要stackで再発した。無害/修正済みとはしない。
- smokeにない235件の内訳: TagLoaderのmissing reference4、loot table parse4、recipe parse204、advancement parse14、TicEX material stat3/tool definition1/slot layout1、JEI ingredient2、Patchouli guidebook1、ProjectE UUID Checker1。詳細message/行番号/smoke・6/30過去logとの照合は`audit/log-comparison.json`へ保存。recipe/loot/tag等はworld datapack読込段階、JEI/Patchouli等はplayer入場後に発生した。
- 例: 利用者datapackの`minecraft:village/has_smith`、TaCZ Turrets/MC Pitan Libのtag参照不足、BotanyPotsOrePlanting/PackAPunch recipe、Yuushya/Easy Mob Farm loot、Sophisticatedのchipped advancement、TicEX未登録material/stat、Yuushya guidebook。**235件中234件のERROR先頭messageは6/30元worldログにも存在**するが、stack・影響まで完全一致/安全と証明したものではない。datapackをsafe modeで無効化したり、missing item/tag/MODを削除して整合させたりしていない。
- **停止理由となる新規1件**: latest.log:2614、17:30:31.534、`[ProjectE UUID Checker Server/ERROR] [moze_intel.projecte.PECore/FATAL]: Caught exception in UUID Checker thread!`。stackは`java.net.ConnectException: Connection refused: connect`→`URL.openStream`→`ThreadCheckUUID.run(ThreadCheckUUID.java:26)`。厳密にはlog severity ERROR、loggerのFATAL markerであり、**severity FATAL 0件 / FATAL marker付きERROR 1件**。JVM crashやFood Healing capability例外と混同しないが、利用者の停止条件として扱った。
- 同じsmoke用のprocess限定HTTP(S) proxy `127.0.0.1:1`（外部取得を閉じる設定）を維持しており、ProjectE接続拒否はその通信制約と整合する。ただし接続対象・全原因は今回確定しておらず、通信許可/設定変更・再試行・MOD修正で解消したとはしない。6/30元ログにはこのmessageはなく、次の調査範囲/通信方針は利用者判断待ち。
- ModLoadingException、mandatory dependency欠落、必須Mixin適用失敗、全体registry/datapack load失敗、Food Healing capability/playerdata deserialize例外、world保存失敗は今回logで未検出。ただし上記個別data読込ERRORは存在するため、datapack/全MOD保存dataが完全正常というPASSにはしない。Food Healingは17:30:41.491に`Restored 10 max health bonus`を記録。既存上限拡張ERRORと2種の旧item model欠落warningはそのまま残る。

##### Food Healing capability・保存データの前後比較

| 項目 | 起動前 | 正常保存後 | 判定範囲 |
| --- | --- | --- | --- |
| Shokugi Level / EatCount | 2 / 35 | 2 / 35 | 一致 |
| DisabledSkills | 空 | 空 | 一致 |
| Food Diversity All / Current | 28 / 3 | 28 / 3 | 食品ID集合・tag型を含め一致 |
| MaxHealthBonus | 10 | 10 | 一致 |
| Food Healing全capability | 型付き基準NBT | 完全一致 | level.dat Playerと保存playerdataで照合 |
| Health / Food Level / Pos | 50 / 20 / (-31.5,63.0,48.5) | 同一 | 観察範囲のみ |

- **読込identityの制限**: 前回source smokeと同じ試験名`SourceRuntimeSmoke` / UUID `00000000-0000-0000-0000-000000000001`によるlocal単独起動。正規アカウントによる接続/identity移行試験ではない。vanilla単独ownerの`level.dat/Player`経路から元Food Healing dataを読み込んだ結果を確認したが、保存後のlevel Player UUIDは元`<PLAYER_UUID>`から試験UUIDへ変化し、boot copyだけに新UUIDのplayerdata/advancement/stats等5 filesが生成された。旧UUIDのplayerdataは残存しbyte不変、両UUIDのFood Healing capabilityとも元値と一致。NBT直接編集はしていない。外部MODのUUID依存ownership・全capabilityの同一性まで保証しない。
- boot worldは**181→186 files、追加5 / 削除0 / 既存65変更**（byte変更26、mtimeのみ39）。level.dat/level.dat_old、chunk/entity/POI、外部MOD保存data、serverconfig等の通常load/saveによる変更を`audit/final-integrity.json`に全path/hash付きで残した。変更値を手動復元せず、boot copyをpristineとして再利用しない。datapackのenabled/disabled記録は前後一致。
- 元world・pristineはそれぞれ181 filesの集合/hash/size/mtimeすべて不変。元ZIPもSHA `B83E28255F90BEB356253AB60A5B12D9C8DBFB5E631C12309C38962A62AC5C7B`/size/mtime不変。コピー元runtime **16,750 files**すべてhash/size/mtime不変、元mods/config/defaultconfigs/resourcepacksのfile集合も差分0。元Prism設定等の保護台帳598件、既存world保護台帳1,440件も差分0（重複あり、加算しない）。copy側104 MOD Jarもhash/file集合不変。

##### 証拠・変更範囲・次段階

- 証拠は本rootの`audit/original-before.json`/`original-after.json`、`pristine-before.json`/`pristine-after.json`、`boot-before.json`/`boot-after.json`、`capability-before.json`/`capability-after.json`/`capability-comparison.json`、`runtime-manifest.json`、`process-start.json`/`process-exit.json`、`log-comparison.json`、`final-integrity.json`、`supplemental-review.json`。生logは`game/logs/latest.log`/`debug.log`、`logs/stdout.log`/`stderr.log`。日本語logはCP932として確認し、元byte/hashを保存。
- 変更対象は**本Statusと新規隔離rootだけ**。copy/manifestと準備・起動・read-only NBT/終了後監査helper、runtimeによるboot用保存fileを記録。ゲームJava/Mixin/resource/Config/dependency/build設定/仕様/既存テストは未変更、**build/unit/check/GameTest/Gradle runClient/runServerはNOT RUN**。既存自動39件・各MANUAL PASSはそのまま。v3成果物SHAも既存値のまま。
- **次段階: BLOCKED - WORLD LOAD ERROR REVIEW / USER DIRECTION REQUIRED**。描画・指定Food Healing data保持・通常保存/終了は観測できたが、全体full bootをPASSにはしない。新規ProjectE例外、world data読込ERROR、試験UUIDによる外部ownership未確認、MTR listener/Firewall確認事項を利用者へ提示し、無断修復・通信設定変更・再試験は行わない。**V3 MIGRATION = NOT RUN / NOT AUTHORIZED**、Food Healing3.0.0互換・全外部MOD機能・歴史的116 Jar完全同一性は未確認。OPEN-01〜05、実2-client BLOCKED、保留機能は変更せず、ここで停止する。

#### 2026-09-06 実v2.2.5 full boot用の原本保護・独立環境準備

- root: `<LOCAL_PATH>/food-healing-mod-main\build\verification\source-world-boot-20260906-172148`。`pristine-world/圧倒的物量で大都市を作るMinecraft`を保全用、`game/saves/圧倒的物量で大都市を作るMinecraft`を起動・保存用として分離した。runtime/pristine/bootはいずれも新規の実copyで、原本を参照するlink/junctionを使用しない。
- 開始前の元world全181 filesのhash/size/mtime/file集合、元ZIPのhash/size/mtimeを`audit/original-before.json`へ記録。ZIP全fileと元worldがSHA-256一致することを確認後、元world→pristine→bootの順でcopyし、`pristine-before.json`/`boot-before.json`へmanifestを保存。pristineとbootの初期値は元worldと完全一致。
- `audit/capability-before.json`: level.dat内Playerと既存playerdataのFood Healing NBTを読み取りのみで確認。両方ともShokugi Lv2/EatCount35/DisabledSkills空、Food Diversity All28/Current3/MaxHealthBonus10。tag型と食品ID集合も保存し、起動後比較の基準にする。NBT編集/再シリアライズは行っていない。
- 前回copy-manifestのsourceを現在hash再照合して16,750 filesを新規runtimeへcopy。MC1.20.1、Forge47.4.0、Microsoft17.0.15、Wrapper`prism-2025-12-07`、104 top-level Jar/source116 ID、Food Healing2.2.5を維持。MOD version変更・download・削除なし。旧world固有datapack/serverconfig/外部保存dataもworld全体copyに含めており、safe modeで省略しない。
- 起動は前回可視desktop成功時のWrapper直起動方式で、home/tmp/game/assets/librariesすべて新root内。前回同様に認証済み接続試験ではない。local Minecraft/Forge `PlayerList`の単独プレイヤー読込がWorldDataのPlayer tagを使用する経路を静的確認した。実際の読込/保存後の同一性はこれから照合し、推測でPASSにしない。
- 変更はStatusと新rootのcopy/manifest/read-only NBT監査/起動helperのみ。開発Java/Mixin/Config/resource/build/test/仕様は未変更、build/unit/check/GameTestは今回NOT RUN。17:27準備完了時点でfull bootはまだNOT RUN。次はmain menu→唯一のboot用world選択。停止条件発生時は修復/再試行せず記録して正常終了可能な経路で停止する。V3 MIGRATIONは未承認のまま。

#### 2026-09-06 source v2.2.5 worldless runtime smoke・終了確認

- **判定: SOURCE V2.2.5 RUNTIME REPRODUCTION / SMOKE PASS WITH RECORDED BASELINE ERRORS**。利用者承認のworldなし起動だけを実施。main menuにMinecraft1.20.1 / Forge47.4.0 / 116 MOD読込の表示を画面で確認し、world一覧・Singleplayer・Multiplayerへ入らず「終了」を押した。GUI/ゲーム内効果のMANUAL PASSではなく、Codexによるsource起動smokeの結果。
- 環境: Microsoft Java17.0.15+6-LTS、LWJGL3.3.1、ForgeWrapper`prism-2025-12-07`。旧`PrismLauncher-3.log:265`のclasspathが対応根拠。現Wrapperへ代用せず、元Prismを起動/編集せず直接旧Wrapper mainから起動。現GPU/driver等まで保存当日と完全再現した主張はしない。
- MOD構成: 元instance由来**104 top-level Jar**。両試行の起動logがsource LoadingModListに対して**116 ID/version一致 / 0不足・不一致 / 0追加ID**。Food Healingは`2.2.5`、Pam Extendedは元環境同様に`0.0NONE`でロードされた。これはmetadata/runtime対応であり、**EXACT HISTORICAL BYTE REPRODUCTIONではない**。MODを削除・置換・更新せず、v3 Jarを入れていない。
- 隔離root: `<LOCAL_PATH>/food-healing-mod-main\build\verification\source-runtime-20260906-165001`。初回`game`と再実行`game-visible`を分離。再実行側も元のgame関連fileをsource hash照合後に新規copyし、初回のlog/config履歴を上書きしていない。Java/libraries/assetsは同root内の同じlocal copyを使用。どちらにもworld/NBTを持ち込まず、`level.dat`/region/session.lock/保存worldは0件、integrated server開始logも0件。

##### 起動・終了履歴

- 初回PID32828: 17:00:45起動、TitleScreen初期化/49.574秒の起動完了log。実行ツールの隔離デスクトップ側で画面取得不可。17:04:14/17:04:50の通常window close要求ではJVMが終了せず、**NORMAL SHUTDOWN NOT COMPLETED**。worldを持たない当該PID/pathを照合し、17:05:27に限定停止、17:05:28回収（exit4294967295、helper exit1）。これを正常終了やMOD crashと偽らず、初回記録をそのまま保持。
- 可視デスクトップ再実行PID26204: 17:07:07起動、17:07:47 TitleScreen初期化、41.802秒で起動完了。main menuの画面確認後「終了」を選択し、**17:09:29.946 `Stopping!`、17:09:30.903 JST exit0 / processReaped=true**。17:12:35のprocess照合で隔離Java残留0件。source version・MOD・gameplayへの修正を挟んだ再実行ではなく、desktop起動方法だけの修正。
- build / unit / check / GameTest / Gradle runClient / dedicated runServer: **NOT RUN**。今回の起動はlocal source runtimeを直接実行し、v3開発コード/テストを変更していないため、既存39件PASS等を再実行/更新していない。

##### ERROR / Mixin / dependency記録

- 可視再実行のlatest.logは**ERROR 18件、FATAL 0件**。ERROR17件のmessageは保存当日6/30のworld読込前ログにも一致する。内訳はYuushyaのMixin `minVersion`未指定1件、Yuushya/TicEX/SlashBladeモデル読込14件、Mekanismの`inputInputSlot` invalid field1件、旧Food Healingの`Failed to extend max health cap: maxValue`1件。**エラーなし、修正済み、機能正常とは扱わない**。最後の上限拡張失敗は旧2.2.5のsource側結果であり、v3実装へ今回変更を加えていない。
- 残るERROR1件はIron Furnaces update check失敗。外部HTTP(S)を閉じたprocess専用loopback proxyで実行した際の接続失敗を記録。初回のみYggdrasil public key取得失敗もありERROR19件だった。認証・更新service正常性は試験範囲外で、失敗を消すために通信許可やversion変更はしていない。
- **ERROR level以外も確認**: `Thread-0`の`ConcurrentModificationException`（ModLauncher `LaunchPluginHandler` / ModernFix `NightConfigWatchThrottler` / NightConfig `FileWatcher`）をSTDERR/INFO経由で両試行に観測。6/30 source latest.log:168〜181と同じstackがあり、既存thread例外として残す。main menuへ進めたことだけでconfig監視の正常性・無害性を保証しない。
- Mixinの必須適用失敗（MixinApplyError/TransformerError/InjectionError）、ModLoadingException、mandatory dependency欠落、registry load失敗、crash reportの生成は今回の両試行で未検出。ただしYuushya設定ERROR、optional target class/refmap等のwarningは存在する。resourceモデル・texture/sound・driver workaround・Forge library metadata等のwarningも`audit/log-review.json`/生logへ残す。world専用registry/datapack/recipeロードは**未試験**。
- 隔離configには起動時の正規化（例: TicEX `avaritia.condensingDropProbability`）が記録された。元configを手動修正せず、原本はhash/mtime不変。Forge installer処理はlocal既存生成物を`Cache Hit`で利用。不足artifact取得、MOD download/auto update、必須Mixinの抑止設定は行っていない。

##### Current artifact SHA-256

| Artifact | SHA-256（現在値。保存当日hashではない） |
| --- | --- |
| Minecraft1.20.1 client | `56B71336D2B4FDFFD197F56595B0DA93E32A946F78F382A299B8F4B92758BB0F` |
| Forge47.4.0 installer | `F3F57465E2CBDC328193F77BA7F0C94CB64405F31ED556753D9275E8BDC06E2C` |
| Forge47.4.0 client | `010ADC332F19B05FB24383954EC7A88694666216E480A6824FF0CBC4740C3C66` |
| Forge47.4.0 universal | `2B0E97B22FD4D043DBBCC8E0A44EF0D58915689ED9224E4CD28D5C4976A16BCC` |
| ForgeWrapper prism-2025-12-07 | `11F5790BD1C8A757E38FEAEFFF181F14DC11BD941B7953E3678C02157EE1D83E` |
| Microsoft17.0.15 java.exe | `A06ADD401CA018695571B64E271C7FD6AE47FF3F188E1E0D97FF638967477D6C` |
| foodhealing-2.2.5.jar | `5BAC83E55419D96849823D7A7BB0933E01600564E13739992E43782C925A6A8C` |
| pamhc2foodextended-1.20.4-1.0.1.jar | `3CEAD8BEBF7BF4B0D690EE3887DA58A1941C92D197826BE1F54BBAE942E408BD` |

- 全copyのsource/path/現在hash/size/mtimeは`audit/copy-manifest.json`（SHA `FF78765C49FCFD58B12CE8CDEE1E4FC39CE835083CD271A5EBB75B02F245AEBE`）、再実行game copyは`audit/visible-copy-manifest.json`（`1B62978F538A5D995771DA10A8E09F80750CB7109CE73F046AB52E5F835728C8`）。これらは検証root内の非配布資料であり、外部JarをFood Healing配布物にbundleしていない。

##### 原本保全・残条件・停止

- 17:12 JSTの終了後監査: コピー元**16,750 filesすべてhash/length/mtime不変**。libraries/assets/Java/mods/config/defaultconfigs/resourcepacksのfile集合も不変。既存保護台帳**598件と1,440件とも差分0**（重複を含む別台帳、単純加算しない）。元Prism instance設定/元mods/config/options/元world/元ZIP、repo既存worldを変更していない。
- 終了後のworld file集合追確認: 原本world181件、`run/world`1204件、既存手動world28件、前日dedicated world27件は追加/削除0。ZIP SHAは従来の`B83E28255F90BEB356253AB60A5B12D9C8DBFB5E631C12309C38962A62AC5C7B`。両隔離copyのmods Jarも起動後byte不変。v3成果物は`943F10BDEE8CD59C5B43DA12A215F7A64B32BB0CB63BDD69E24BBC0669E95EA4`のまま。
- 証拠: 上記manifest、`audit/preparation.json`、`audit/log-review.json`、`audit/integrity-after.json`、`audit/world-file-set-review.json`、`audit/process-*.json`/`visible-process-*.json`。生logは`game/logs`と`game-visible/logs`、console出力は`logs/console-*`と`logs/visible-*`。初回失敗も消去していない。
- 変更対象: `docs/CODEX_STATUS.md`と隔離rootのみ。準備/起動/終了後監査helper、artifact copy、起動時生成file、manifest/logを保存。元instance/原本data、v3 Java/Mixin/resource/Config/dependency/build設定/仕様書は未変更。OPEN-01〜05・NOT LOCKED、実2-client BLOCKED、既存MANUAL PASSを維持。
- **次段階の判定**: source version対応とworldなしruntime起動の前提は整った。旧worldコピーによる**v2.2.5のままの**full bootを検討できるが、現時点では**WAITING FOR EXPLICIT USER APPROVAL**であり、今すぐ起動してよい状態ではない。将来承認された場合も専用world copyの完全性・datapack/serverconfig/外部保存data保持を確認し、上記baseline ERRORを前提に前後差分を監査する必要がある。保存当日の全Jar byte同一性は未証明で、world安全性の保証ではない。
- **FULL REAL-WORLD BOOT = NOT RUN / V3 MIGRATION = NOT RUN**。v3 migrationは別承認・OPEN-01/05等の別gate。今回のsmoke PASSを旧world boot/migration/全MODゲーム内互換性PASSへ拡張せず、再起動・追加実装・追加試験へ進まず停止する。

#### 2026-09-06 source v2.2.5 runtime隔離準備

- 利用者承認範囲: Minecraft1.20.1 / Forge47.4.0 / Microsoft Java17.0.15 / ForgeWrapper`prism-2025-12-07` / Food Healing2.2.5をlocal artifactから独立copyし、worldなしの起動確認と正常終了まで。`FULL REAL-WORLD BOOT = NOT RUN`、`V3 MIGRATION = NOT RUN`。全116 artifactの歴史的byte同一性は未証明のまま。
- 隔離root: `build/verification/source-runtime-20260906-165001`、game directory: 同`game`。16,750 files / 4,513,456,547 bytesをcopyし、各fileをsource/destination SHA-256で照合した。top-level MOD Jarは104個（source LoadingModList116 IDとは別の数え方）。world/savesは0件、Food Healing3.0.0は入れていない。
- Wrapper採用根拠: 保存当日前後の`PrismLauncher-3.log:265`のclasspathを使用（95 entries、現在の`prism-2026-08-01`を含まない）。Prism EntryPointを経由せず、同Wrapperの`io.github.zekerzhayard.forgewrapper.installer.Main`を直接呼ぶlaunch helperを用意。Java/native/library/assetはすべて存在確認済みのlocal copy、installer metadataで参照するlocal libraryの不足0件。Prism本体・元instance設定は変更しない。
- 起動の差異/制約: home/tmpも隔離root内へ設定し、外部HTTP/HTTPSはprocess専用の閉じたloopback proxyへ向ける。account認証情報を読まず、worldless smoke専用identityを使用する。認証済み接続・online service試験ではなく、network由来エラーが出た場合もlogへ記録する。元config/defaultconfigs/options/resourcepacks等はcopyのみで原本への書込は行わない。
- 証拠: 同rootの`audit/preparation.json`、`audit/copy-manifest.json`、`launch-args.txt`。current artifact hashはmanifestに全件記録。変更はこのStatusと隔離root内の準備/起動helper・copy・manifestだけ。Java/Mixin/resource/Config/build設定の開発変更なし。
- build/unit/check/GameTest: **NOT RUN（今回の依頼範囲外、開発コード変更なし）**。source runtime起動は次段階で実施。既存manual/automated PASS、実2-client BLOCKED、OPEN/NOT LOCKED、保留実装を変更しない。

#### 2026-09-06 source modset / Pam version provenance静的追確認

- 範囲: 元Prism `instances/1.20.1`のmetadata/config/log、local Pam Jar、既存監査資料、Forge47.4.0/SecureJarHandler/元Javaのbytecodeを読み取りのみで確認。監査root: `build/verification/source-provenance-20260906-163349`。旧world起動、NBT編集、mod差し替え、download、Java/Mixin/Config/resource/build設定変更は行っていない。
- **前回記載の精度訂正（履歴は下段に維持）**: local Pamのmods.tomlは`version="1.0.1"`の直書きではなく、実際には`version="${file.jarVersion}"`。`1.0.1`はZIPから直接読んだManifestの`Implementation-Version`であり、前回metadata readerによる置換結果だった。直読Manifest値とForge loaderが実際に取得する値を同一視していたことが、静的照合の残差の原因。
- 現物 `pamhc2foodextended-1.20.4-1.0.1.jar`の最初の物理ZIP local headerは`.cache/`、Manifestは**3160番目 / offset 1735569**に1個だけ存在する。Manifest自体の欠落ではなく、Jar先頭配置と読取方式の問題。誰がいつこの配置にしたかは証明していない。Jarの並べ替え/修復は行わない。
- **`0.0NONE`の生成経路を静的特定**: 元Java17.0.15の`JarInputStream`は先頭（任意の`META-INF/`を除く）がManifestでなければconstructorでManifestを取得しない。local SecureJarHandler2.1.10の`cpw.mods.jarhandling.impl.Jar`はJar入力にこのstreamを使用し、未取得時はdefault Manifestへ戻る。Forge47.4.0の`ModFile`は`Implementation-Version`未取得時に`0.0NONE`を`jarVersion`へ設定し、`${file.jarVersion}`へ供給する。したがって現物の配置・metadataからこの表示値を説明できる。MODの正式release番号を`0.0NONE`と選び直したり、`1.0.1`へ変更したりしていない。
- **LoadingModList独自のversion変換ではない**: Forge47.4.0 `ForgeHooks.writeAdditionalLevelSaveData`は`ModList`の`IModInfo.getVersion()`を文字列化して`ModVersion`へ保存する。既存`debug.log:206,597`に当該Jar名と`versions {0.0NONE}`、`latest.log:120`にも同値があり、NBT保存前からloader側でこの値だった。
- 保存当日の対応: world `LastPlayed=1782824655067`は**2026-06-30 22:04:15.067 JST**。元`debug.log:25435〜25441`の対象world名と保存/id-map収集時刻が一致する。起動は同日21:51、Minecraft1.20.1、Forge47.4.0、Microsoft Java17.0.15と記録。CP932ログをそのencodingで読み、world名も照合した。
- 過去ログ189ファイルを静的検索し、Pamのversionを明記した該当行は16件で全て`0.0NONE`。元instanceのcrash reportにも2024-11-28以降、同Jar名/同値の記録9行がある（既存crashを今回のFAILや修正済みに変更しない）。同名Jarは元instance、`All`、`Levaさん鯖戦闘鯖`の3か所で現在SHA-256が一致: `3CEAD8BEBF7BF4B0D690EE3887DA58A1941C92D197826BE1F54BBAE942E408BD`。他instance内のmetadata参照はPamに限定し、変更していない。
- **証拠の限界**: 同一保存sessionのJar名/version記録、現物の読取経路、現在の3コピーhash一致は確認したが、保存当日のJar SHA-256台帳/対応するmods backupは今回のローカル探索で見つからなかった。mtimeや同名だけでは過去との全byte同一性を証明できない。`0.0NONE`の由来不明は解消する一方、歴史的artifact完全一致は`NOT PROVEN`として分離する。将来それを厳密な開始条件とする場合は、当時のhash/backup等が別途必要。架空のoriginal version/artifactを要求・選択しない。

##### 116件の照合範囲

| 比較対象 | 一致 | 不一致 | 不確定 / 別扱い |
| --- | ---: | ---: | --- |
| 前回の直接metadata比較（履歴を維持） | 113 | 1（Pam直読値との差） | 2（Minecraft/Forge runtime、mods Jar比較外） |
| 今回のlocal artifact静的対応整理 | 116（既存113 + runtime2 + Pam loader表記1） | 0（説明不能なversion差） | 0（version表記の由来について） |
| 保存当日起動log対source LoadingModList | 116 | 0 | 0 |
| 保存当日と現在のJar byte同一性 | 全116件を証明した数としては計上しない | 判定しない | 保存当日hash台帳なし。metadata/log一致とは別の未証明事項 |

- 既存105 top-level + 56 nested、計161 Jar記録の現在hashを再計算し、**161/161が前回監査値のまま**。既存113一致判定を壊したり、未試験MOD互換性PASSへ拡張したりしない。logの見出しは`Loading 115 mods`だが、実列挙のnested行も含めたsource ID比較は116件一致。初回追確認parserの106件はnested10行の読み落としだったため、`reviewed-evidence.json`で明示的に修正し、10 MOD不存在とは記録しない。

##### Forge47.4.0元環境と残条件

- 元`mmc-pack.json`: Minecraft1.20.1 / Forge47.4.0 / LWJGL3.3.1。instance overrideのJavaは`java/java-runtime-gamma`のMicrosoft17.0.15/64bitで、local `release`と保存当日logも一致。現開発Java17.0.7/Forge47.2.0での既存PASSをこの元環境のPASSへ移さない。
- local Minecraft client Jar、Forge47.4.0 client/installer/universal Jar、fmlloader等を確認。現Prism Forge47.4.0 metadataに列挙された30 libraryはすべてlocalに存在し、そのキャッシュ記載SHA-1と一致。ただしこれは**現キャッシュの整合**であり、assets/native/config/datapackを含む保存当日の全構成・起動成功を保証しない。
- **残る再現上の注意**: 6/30前後の`PrismLauncher-3.log:265`および6/24の`PrismLauncher-4.log`はForgeWrapper `prism-2025-12-07`。現在のForge metadata（9/6更新）は`prism-2026-08-01`を参照する。旧Wrapperもlocalに存在（SHA `11F5790BD1C8A757E38FEAEFFF181F14DC11BD941B7953E3678C02157EE1D83E`）するが、今回はcache/instanceを編集せず、採用launcher/library構成を勝手に確定しない。元環境再現の際はこの差を含む独立環境の構成固定と利用者の明示承認が必要。
- full bootの既存`BLOCKED - SOURCE MODSET / VERSION ENVIRONMENT REQUIRED`は、上記のversion表記残差解消と区別して維持する。元world/serverconfig/外部保存data/利用者datapackを欠いたvanilla起動、mods削除、47.2.0へのdowngradeで代替しない。v3を47.4.0元構成へ加えた互換性・full migrationは**NOT TESTED**。OPEN-01/05も未決定のまま。

##### 証拠・保全・終了

- 証拠: 監査rootの`pam-artifacts.json`、`historical-log-evidence.json`、`loader-bytecode-evidence.json`、`reviewed-evidence.json`、`prior-inventory-integrity.json`、`forge-runtime-metadata.json`、`launcher-wrapper-review.json`。外部Jar/classをコピー・配布物へ同梱せず、metadataと必要な静的disassembly/ログ抜粋だけを保存した。
- 静的読取ツール上の注意も維持: 最初の`javap -classpath`は出力後のZipFS closeでAccessDeniedとなったため、書込権限を上げずread-only `jar:file:` URLへ切替えてexit0を確認。最初の`--system`指定でもhost Java17.0.7のbootstrap classが表示されたので、元17.0.15の`javap.exe`で再確認した（class hash/bytecodeをreviewed証拠へ保存）。MOD起動失敗や統合試験結果ではない。
- 16:42 JST時点で原本world/ZIP、元mods/config/defaultconfigs/options、Prism instance設定/Forge metadataの**598 filesはhash/length/mtime不変**。原本ZIPは既知の`B83E28255F90BEB356253AB60A5B12D9C8DBFB5E631C12309C38962A62AC5C7B`を維持。`protected-before.xml`/`protected-after.xml`/`integrity-after.json`に記録。16:45の最終確認で同対象のfile集合も不変、前回保護world 1,440 filesもhash/length/mtime不変（`final-safety-review.json`）。NBT/原本/ZIP/mods/Prism設定への書込なし。
- 変更対象は`docs/CODEX_STATUS.md`と隔離監査root内の読み取り用Python/JSON/XML/Status退避だけ。ゲーム実装・仕様・build設定・既存監査結果は未変更。既存成果物SHAも上記最新値のまま。
- **build / unit / check / GameTest / runClient / runServer / full world boot: 今回はすべてNOT RUN**。依頼どおり静的確認のみ。前回の自動39件/実capability保持/手動PASS、実2-client BLOCKED、OPEN/NOT LOCKEDをそのまま維持する。
- 次の利用者判断: この静的証拠と歴史的hash未証明を踏まえ、将来の47.4.0独立検証環境の採用構成・開始を明示承認するか。今回は承認を推定せず停止。新しい人力再試験を要求せず、full migration・他MOD統合・保留機能へ進まない。

#### 2026-09-06 full world事前静的監査・最終回帰・原本保全フェーズ

- **FULL REAL-WORLD BOOT / NOT RUN / BLOCKED - SOURCE MODSET / VERSION ENVIRONMENT REQUIRED**。source LoadingModList全116件に対して、Prism元instanceのmods、repo libs/run/modsの計105 top-level Jarと56 nested Jarをmetadataのみ読み取り照合した（外部コードのロード・実行・コピー・統合試験なし）。113 mod ID/versionが一致。Minecraft/Forgeの2 runtime項目はmods Jar一覧とは別にPrism `mmc-pack.json`の1.20.1/47.4.0とlocal Forge client/installer/universal Jarを確認した。
- 残る不一致: source `pamhc2foodextended=0.0NONE`に対しlocal `pamhc2foodextended-1.20.4-1.0.1.jar`のmods.tomlは`1.0.1`。filenameだけでMC互換性を断定しない（declared MC rangeは`[1.20,)`、Forgeは`[40,)`）が、保存時artifactと同一と証明できず、完全modset再現とは扱わない。採用versionの推測/置換/downloadは行わない。
- `itreallyhitsthespot-1.0.1.jar`のdescriptionに非UTF-8 byteがあり、最初の静的parserはdecode error。ID/version部分がASCIIで`itreallyhitsthespot=1.0.1`であることを生byte表示と構造化TOML再読で確認した。replacement decodeは監査内だけで、Jarは未変更。loader動作のPASSとはせずencoding注意を残す。初回`local-mod-inventory.json`と追確認`local-mod-inventory-reviewed.json`の両履歴を保持。
- 現v3の開発/自動fixtureはForge47.2.0、元world/Prismは47.4.0。v3 mods.tomlのForge/loader `[47,)`、MC `[1.20.1,1.21)`は47.4.0を宣言範囲に含むが、実際の47.4.0互換性やdowngrade安全性の証明ではない。
- `audit/world-dependencies.json`へlevel.datのregistry等のresource参照、実Inventory/EnderItems参照、全18 capability key、保存data/datapack一覧を記録。level.dat参照は登録済み一覧も含むため全項目が配置済みとは主張しない。実playerにはFarmer's Delight、ProjectE、TaCZ、TConstruct等のitem/capability、worldにはFlux Networks/RFTools/Sophisticated等の保存dataと利用者datapackが存在し、これらを欠いたworld起動/削除置換はしない。chunk本体の全block/entity走査やfull registry reloadは未実施。
- 新たにlocal artifactの存在/versionを確認したFarmer's Delight1.20.1-1.2.10（SHA `32404E9BF43FB8B8B1602E7D561A3831117D6B167C2B77B82DFB1515D08744F0`）、Sophisticated Backpacks3.23.14.1233（`D89DA79F7E2FB1503524B16BC64446A0FC9765177F13FCAF2D11B8E9CC6669A3`）とCore1.2.49.962は**METADATA IDENTIFIED / NOT INTEGRATION TESTED**。Adapter内部監査は今回行わない。元worldのTaCZ1.1.8-hotfix（`9ED8ADA1283ED7A793A70CC1B51C4A340F367CE84707E1A7B8CF21EE3D288D77`）も現正式試験対象1.1.7-hotfix2とは別。既存13ケースPASSを流用しない。
- 最終自動回帰: `foodHealingRealLegacyAudit build foodHealingUnitTest check runGameTestServer -PfoodHealingRealLegacyRun=20260906-132241 -PfoodHealingRealLegacyAuditMode=dependencies -PfoodHealingGameTestRun=real-v225-final-20260906-1334 --offline --console=plain`、48秒・exit0。**build/unit/check PASS、vanilla GameTest39/39 PASS**。custom unit 5,000境界取引PASS、標準JUnitは従来の0件と分離する。log: 隔離rootの`logs/final-regression.log`。
- ERROR/FATAL・必須Mixin適用失敗なし。既存開発環境のrefmap-read warning、language Jar mods.toml warning、union assets、初期Config生成、terminal/APT/deprecation warningは記録を維持。新規GT world初期化時のCan't keep up 3435ms/68ticksを観測し、長期性能PASSとはしない。Jarにrefmapが存在し、Mixin `required=true/defaultRequire=1`を維持していることも確認。
- phase指定なしのreal opt-in `runServer`は起動前にexit1で拒否されることを確認（`logs/expected-missing-phase-rejection.log`）。これは意図した**SAFETY GUARD TEST / PASS**であり、gameplay/migration/build regressionのFAILではない。既存のfresh-token receipt/別PID検査も維持した。
- 最終保全（13:36 JST）: 原本/コピーともSHA `B83E28255F90BEB356253AB60A5B12D9C8DBFB5E631C12309C38962A62AC5C7B`、原本mtime不変、保護world計1,440 filesと展開181 filesのfile集合/hash/mtime不変。`audit/integrity-after.json`・`protected-after.xml`に保存。元Prism設定/mods/options、run/saves、昨日の専用worldを編集/起動していない。
- 成果物監査: Jarの97 file entriesを変更前baseline Jarと比較し、差はbuild時刻を含むmanifestのみ。全class/resourceはbyte一致。実world/NBT/UUID、restart/legacy test classの混入なし、ExampleModなし。`audit/artifact-review.json`に記録。Gameplay/Mixin/Config/resource/dependency変更なし。
- 変更ファイル: `build.gradle`（test-only opt-in/task/隔離gate）、`src/restartTest/java/com/leva/foodhealing/RealLegacyCapabilityVerification.java`（追加）、同`RestartPersistenceVerification.java`（test-only呼出し）、`docs/TEST_PLAN.md`（再現手順/範囲）、`docs/CODEX_STATUS.md`。隔離root内に監査用Python metadata reader・JSON/XML/backup/fixture/logを生成したが、配布対象外。
- runClientなし。write/read/GTはすべて正常終了し、13:37 JSTのprocess照合でもMinecraft/fixture JVMは存在せずGradle daemonのみ。実2-clientは**BLOCKED - SECOND MINECRAFT ACCOUNT REQUIRED**、OPEN-01/05その他未決定・保留機能はそのまま。
- 次の安全な作業: 今回A〜Gは完了。full bootには不一致artifact/元構成の根拠、version環境の明示承認が必要で、今回は起動しない。OPEN換算や手動操作の代替不能境界は残るが、今回のcapability保持は自動検証で完結しており追加人間操作は不要。新規実装・外部統合・TaCZ射撃・Break Realm・試作型機関弩へ進まず停止する。

#### 2026-09-06 実legacy capability別JVM保存・再読込フェーズ

- **REAL LEGACY CAPABILITY FIXTURE / PASS**: 実level.dat/playerdataから抽出した2 payloadを別々のvanilla Player NBT fixtureにそのまま注入し、`PlayerList.placeNewPlayer`→capability deserialize→PlayerDataStorage保存→正常server停止→別JVM再読込を通した。元worldのInventory/他MOD/capabilityを削って起動したのではなく、全く新しいvanilla fixtureでFood Healing payloadだけを検証した。
- write JVM PID35288、read JVM PID5040。両方19秒・exit0・BUILD SUCCESSFUL、通常`Stopping server`、player保存、`All dimensions are saved`、fresh success receiptを確認。log: 隔離rootの`logs/disk-write.log`、`logs/disk-read.log`。ERROR/FATALなし。
- 両JVMでraw backup/Lv2/count35/SP0/skill・base・toggle非生成/Diversity All28・Current3・bonus10/所有modifier20回冪等再構築を確認。read時にplayerdataを初期化・上書きseedしていない。元payload SHAは両由来とも`1DE57C89C90BD5172C034BED522EC600101790F71B9D23AD7C374F084FA56533`。入力/保存後capability/PID/生成fixture UUIDは`audit/disk-*.json`に記録。
- 既存fresh/legacyの別JVM回帰も併走PASS。これは実ユーザーworld全体、認証済みclient、実2-player、外部MOD統合のPASSではない。runClientなし、試験serverはすべて停止済み。OPEN-01/05は未決定。
- 次はfull world起動前のMOD/version/registry依存の静的整理、変更後build/unit/check/39件GameTest、原本・保護world非変更確認。追加はtest-only/手順記録の範囲で、gameplayは変更しない。

#### 2026-09-06 実v2.2.5 provenance・実NBT保持フェーズ

- **REAL V2.2.5 WORLD PROVENANCE CONFIRMED**: `LevelName=圧倒的物量で大都市を作るMinecraft`、Minecraft1.20.1/DataVersion3465、Forge47.4.0、foodhealing2.2.5、UUID `<PLAYER_UUID>`を実NBTで確認。LoadingModList全116件と元NBT・元file hashを隔離rootの`audit/provenance.json`へ保存した。
- **REAL V2.2.5 FOOD HEALING NBT AUDITED / PASS**: level.dat内Playerとplayerdataの両capabilityは完全一致。Shokugi旧int Lv2/count35/DisabledSkills空、Diversity All28/Current3/MaxHealthBonus10を確認。未知の旧Shokugi追加fieldは今回の実payloadにはなく、raw全体一致と既存synthetic未知field回帰を分離する。
- 実payloadを値の手作業再構築なしに`fixture/caps-level.dat`と`caps-player.dat`へ抽出。player側SHA-256 `1DE57C89C90BD5172C034BED522EC600101790F71B9D23AD7C374F084FA56533`。両入力を各100回serialize/deserializeし、schema4、LegacyV2Backup完全保持、Lv/count保持、legacy pending、未使用/使用済SP0、acquired/base空、toggle空、Root状態0、Diversity両履歴/bonus保持を確認。外部NBT変更の参照漏れもなし。
- test-only変更: `RealLegacyCapabilityVerification.java`をrestartTest source setへ追加、既存restart harnessにopt-in呼出し、`build.gradle`へtimestamp専用入力/taskと隔離server directory分岐を追加。通常runClient/runServer、gameplay、Mixin、resource、Config、dependencyは変更していない。実ユーザーNBTはbuild配下のみ、配布対象外。
- `foodHealingRealLegacyAudit -PfoodHealingRealLegacyRun=20260906-132241 --offline --console=plain`: compile/audit **PASS**、7秒・exit0。logは隔離rootの`logs/real-audit.log`。build/unit/check/GameTestは変更前baseline PASS、変更後全回帰は後続で実施する。
- 別JVMは次フェーズ。full world bootは**NOT RUN**。元Forge47.4.0と現47.2.0を同一環境と扱わない。OPEN-01/05、実2-client BLOCKED、未実施外部MOD統合は維持。次は実capabilityだけをvanilla PlayerDataStorage fixtureへ入れ、正常保存・別JVM再読込を検証する。

#### 2026-09-06 実v2.2.5 ZIP受領・隔離・baselineフェーズ

- 最新利用者指示により実旧worldの非破壊監査を開始。原本はPrism `instances/1.20.1/minecraft/saves/圧倒的物量で大都市を作るMinecraft.zip`。SHA-256 `B83E28255F90BEB356253AB60A5B12D9C8DBFB5E631C12309C38962A62AC5C7B`が指定値と一致した。
- 新規隔離root: `build/verification/real-v225-migration-20260906-132241`。`source-copy/original.zip`へbyte copyし同hashを確認、path traversal/重複path/symlinkを検査してコピーだけを`extracted-readonly`へ展開（181 files）。原本・通常worldは起動していない。
- `audit/protected-before.xml`に元Prism world、run/world、run/saves、昨日の専用server worldの計1,440 filesのhash/mtimeを保存。展開物181 filesも別manifestへ記録した。ソースbackupは`audit/project-source-before.zip`、SHA-256 `F8AA5307AA1B996C19B8518B89F4A7F539EEFCAFE674CF7D42789D1EAB2E806E`。
- 変更前baseline: `build foodHealingUnitTest check runGameTestServer -PfoodHealingGameTestRun=real-v225-baseline-20260906-1322 --offline --console=plain`、48秒・exit0・build/unit/check PASS・vanilla **39/39 PASS**。unitの5,000境界取引もPASS。新規GameTest環境は別の`build/verification/gametest-real-v225-baseline-20260906-1322`のみ。
- log: 隔離rootの`logs/baseline.log`。ERROR/FATALなし。既存開発環境のterminal/APT/Forge初期config/deprecation warningは成功扱いで隠さず維持する。
- provenance/NBT保持/実capability別JVMはこの時点では未判定。full world bootはNOT RUN、OPEN-01/05と実2-client BLOCKEDは維持。変更は本Statusのみ、runClient/通常runServerなし。次は実NBTの静的照合とtest-only opt-in検証。

#### 2026-09-06 End gateway自動回帰完了・残gate整理

- **AUTOMATED TESTED / PASS**: `endGatewayAndPearlPreservePlayerStateWithoutRespawn`を既存P0 GameTestへ追加。実End内の隔離座標にgatewayを配置し、vanilla `teleportTick`の直接player転送とEnder Pearl owner転送を各1回実行。known exact exitを使用し、遠方島の自動生成や実ユーザーworldを対象にしない。cooldown中の再呼出しも行い、各legで位置packetが1つだけ、credits packetが0であることを確認した。
- 同じServerPlayer/Capability参照、完全なShokugi NBT（Lv30億・未使用SP50億・使用済SP・count・Root/True Root/Heroics/True Heroics・toggle・全base stat取得数・Root累積/期限）、Food Diversity、所有modifier各1つ、他所有modifierの同一性、HP63/100、ダイヤ7個のNBT付きstackを保持。vanillaによるpearl消費も確認。終了時に試験gatewayの元blockを復元し、fixture player/channelを解放する。未LOCKの予約済みTrue Rootを途中OFFする仕様は試験していない。
- 原因/修正区分: **既存試験の経路不足を補完しただけで、ゲーム側の不具合再現・Gameplay修正はない**。実装と同じ計算式をコピーするテストではなく、MinecraftのBlockEntity tickと転送packet、転送前後の保存データを照合した。手動PASSには昇格しない。server-side保持はこの自動経路で確認できるため、人間に同じ検証を要求しない。
- 最終検証: `./gradlew.bat build foodHealingUnitTest check runGameTestServer -PfoodHealingGameTestRun=gateway-20260906-1215 --offline --console=plain`。**build / unit / check PASS、long境界5000件PASS、GameTest 39/39 PASS、exit0 / BUILD SUCCESSFUL 43s**。12:15:27にgateway証跡`FOODHEALING_END_GATEWAY_PASS legs=2 direct=true pearl=true cooldown=true canonical=true hp=63/100`と全39件PASS。log: `logs/gateway-regression-20260906.log`、隔離先`build/verification/gametest-gateway-20260906-1215`。
- latest/debugにERROR/FATAL・必須Mixin適用失敗なし。初期化直後の`Can't keep up! ... 3236ms or 64 ticks behind`が1件あり、baselineの同段階の遅延警告とともに保持する。生成Config既定値補完、Gradle非推奨、既存API非推奨等のwarningを隠して無警告PASSとはしない。テストの正否と長期性能判定は別であり、この実行をTPS/大規模負荷のPASSにしない。
- 手動変更4ファイル: `src/main/java/com/leva/foodhealing/FoodHealingPriorityZeroGameTests.java`（回帰のみ）、`docs/TEST_PLAN.md`（gateway試験範囲と実1-client完了記録）、`docs/SINGLE_CLIENT_DEDICATED_MANUAL_TEST.md`（接続待ち/全項目未実施という古い案内を9/5完了履歴へ整合）、`docs/CODEX_STATUS.md`。runbookの操作条件・期待値・認証要件は不変。Java gameplay/Config/resource/Mixin/dependency/build設定/確定仕様の変更なし。
- Jar内容比較: 9/5実1-client serverの保存済みJarに対して119 entriesを比較。差分は`META-INF/MANIFEST.MF`、`FoodHealingPriorityZeroGameTests.class`とその既存nested class2個のみ。entry追加/削除なし、他の全class/resourceはbyte一致。新Jar hashは上部参照。Prism/serverの手動試験Jarは差し替えていない。既存world3範囲1259ファイルは開始時hashと一致した。
- runClient/Prism/通常dedicated runServerは未起動。今回のGameTestのみ終了まで確認し、実1-client/実2-client/外部MODの新規試験結果を付けていない。TaCZ 1.1.7-hotfix2 Jarは既承認SHA-256のまま、限定13ケースとTaCZ GameTest38件は9/5の自動PASSとして分離維持する。
- 最終読取確認: backupとの手動変更差分は上記4ファイルだけ。保護worldのファイル集合にも追加/削除なし。12:20:39のprocess照合はGradle daemon PID24248のみで、GameTest/Minecraft client/dedicated serverは終了している。sandboxのprocess照会は権限拒否だったため、読取権限で再確認した（raw command line/認証情報の出力・process操作なし）。

##### 残項目と自動試験では代替できない証拠

| 残項目 | 現在の扱い / 自動化の限界 |
| --- | --- |
| 実v2.2.5 world | **BLOCKED - REAL V2.2.5 WORLD COPY REQUIRED**。既存174件のv3 NBT再保存とsynthetic legacy5種の別JVM保存再読込はPASS済みだが、fixtureから実旧worldの由来・未知の旧データを作り出せない。由来確認できる停止済みcopyと対象MOD/versionが必要。未提供を代替/解除しない。 |
| 実2-client | **BLOCKED - SECOND MINECRAFT ACCOUNT REQUIRED**。server-side 2-player packet/replay/scopeはPASS済みだが、2つの正規clientが同時に受信・適用する実動作の証拠ではない。1-client完了、fake/offline/同一アカウントで代用せず、追加購入や協力者を要求しない。 |
| AttributeFix / L2系 / Auto Leveling / Traveler's Backpack / Sophisticated Backpacks / Farmer's Delight等 | **BLOCKED - TARGET MOD ARTIFACT REQUIRED**。正式なMC1.20.1 Forge向けversion/Jar/sourceが未提供。vanilla fixtureや所有modifier模擬値では各MOD固有event/API/保存形式を実行できない。versionを選んで取得したり、STATICをINTEGRATIONへ昇格しない。 |
| TaCZ残範囲 / Flight / 未完成Adapter | TaCZ既承認版の実bullet hit等13ケースはPASS済み。fixtureは射撃入力packet、client prediction、reload/bolt/heatの一連の挙動を通しておらず、現在のPASSを拡張しない。将来server-side packet試験を追加しても、それだけではclientの入力/適用/表示を観測した証拠にはならない。未完成Adapter実装は今回開始禁止。Flightも他所有者を識別できる仕組み/API未確定のまま。今回は手動試験を依頼せずgate維持。 |
| OPEN / NOT LOCKED | **BLOCKED BY USER DECISION**。OPEN-01旧SP移行、02/03追加前提、04SuperbWarfare、05旧sub-level mapping、True Root予約後OFF時の破棄/保持。unit/NBT比較は決定済み仕様の検証であり、期待値を利用者に代わって決められない。 |
| 未解明接続履歴 | 14:32 TIMEOUTと18:21途中切断は原因未確定。既存logには失敗時の原因を断定する証拠がなく、成功再試行のthread観測では埋められない。修正済みにせず、再現だけを目的とした人間再接続を要求しない。IPv6誤接続は正式試験外として分離。 |
| 長期/大規模性能 | 既存6000tick/30保存の限定soakはPASS済みだが、24時間/大量player/大型MODpackの結果へ拡張しない。負荷と対象MODpackを特定すれば自動計測可能な部分はある。今回は未提供構成や負荷を推測して新規ベンチマークを作らない。 |
| 将来候補 / 許可待ち | Break Realm・試作型機関弩・Pam's Trees複製・Nutrition count候補は既存gateを維持。試作型機関弩は監査も実施せず、明示的開始指示待ち。 |

- 既存test food2種のmodel欠落warningは未修正の履歴を維持。外観の推測・registry削除による解消は行わない。
- **監査終了判断**: 今回特定した独立した自動検証不足（End gateway）は補完済み。他のcurrent release gateは資料不足・利用者の仕様決定・禁止された実装/将来対象・実client固有証拠の不足で、現在の条件下で安全に着手すべき追加修正は特定できなかった。「念のため」の新規人力試験や既存PASSのやり直しは要求せず停止する。全体は引き続き**IN PROGRESS - リリース候補ではない**。次回は資料/仕様判断/開始許可に実際の更新があるか確認し、元の優先順位を維持して再開する。

#### 2026-09-06 再開監査・変更前baseline

- 最新Status全1299行、AGENTS、CODEX_LOOP_PROMPT、既存仕様/TEST_PLAN/互換方針/手順書、現build設定・Mixin設定・購入gate・既存GameTest/unit/restart/TaCZ fixtureと実行logを照合した。9/5の実1-client dedicated STEP 1〜6、既存MANUAL PASS、初回TIMEOUT/18:21未解明切断/IPv6誤接続の履歴は維持する。過去の「本日終了」は9/5の記録であり、本日は利用者の再開指示に基づく監査だけから開始した。
- 自動化可能な既存の未充足範囲を1件確認: `TEST_PLAN.md` §2のEnd gateway。既存P0試験はdimension transfer/respawnとEnd出口portal→credits応答を扱うが、gateway固有の`TheEndGatewayBlockEntity.teleportEntity`（同一dimensionの`teleportToWithTicket`、Ender Pearl owner転送、cooldown）を直接通していない。Forge 47.2.0のローカルmapped sourcesで確認済み。**テスト範囲不足であり、Food Healingの不具合が再現したという判定ではない。** この確定済みライフサイクル要件だけを隔離GameTestで補い、人間の再試験は要求しない。
- 非破壊backup: `backups/20260906-121023-gateway-regression/project-source.zip`、SHA-256 `BC8041A636E8982EEE7F9253D207F85D10AFD86E1A6EFBA82EA5745EF8560D78`。同directoryの`protected-world-hashes.xml`へ`run/world`、`run/saves`、昨日の専用dedicated worldの1259ファイルのhashを記録。既存worldをbaselineに使用しない。
- 変更前コマンド: `./gradlew.bat build foodHealingUnitTest check runGameTestServer -PfoodHealingGameTestRun=resume-baseline-20260906-1210 --offline`。最初はsandbox内のwrapper取得が`SocketException: Permission denied`でGradle task開始前に停止（`logs/resume-baseline-20260906.log`）。環境権限制約として分離し、既存cacheへアクセス可能な権限で再実行した。
- 再実行はJava17.0.7 / Gradle8.1.1 / Forge47.2.0、**build / foodHealingUnitTest / check PASS、GameTest 38/38 PASS、exit0 / BUILD SUCCESSFUL 45s**。long境界5000件PASS。log: `logs/resume-baseline-verified-20260906.log`。隔離先`build/verification/gametest-resume-baseline-20260906-1210`のみ使用。12:11:47に起動直後の`Can't keep up! ... 2185ms or 43 ticks behind`が1件あり、無警告とは扱わない。新規Config生成の既定値補完等のWARNを保持、latest.logにERROR/FATAL・必須Mixin失敗なし。性能全体PASSにはしない。
- このフェーズの手動編集はStatusのみ。runClient/通常dedicated server/Prism未起動、手動試験・外部MOD試験未実施。実旧world、実2-client、未提供MOD artifact、OPEN/NOT LOCKED、保留Adapter/Break Realm/試作型機関弩のgateを解除しない。次は上記gateway回帰を追加して検証する。

<a id="evidence-single-client"></a>

#### 2026-09-05 実1-client dedicated / STEP 6最終確認PASS・本日終了

- **MANUAL SINGLE-CLIENT DEDICATED INTEGRATION TEST / STEP 6 / NORMAL SHUTDOWN + FINAL LOG REVIEW / PASS**。人間がPrism側Minecraftを通常終了し、ウィンドウが完全に閉じたとの報告を受領。**18:37:51 JST**の読取確認で既存client PID23360とserver PID28580はいずれも存在しない。新規起動・server再起動・再接続・強制終了は行っていない。
- client logは前回記録した終了前基準のbyte位置から**追記差分だけ**を読み取った。`latest.log`: 25448 bytes/146行 → 25540 bytes/147行、`debug.log`: 155287 bytes/1003行 → 155379 bytes/1004行。両方の増分は各92 bytes・1行で、**`[05Sep2026 09:35:57.949] [Render thread/INFO] [net.minecraft.client.Minecraft/]: Stopping!`**だけ。既存の9時間差によりJSTでは**18:35:57.949**の終了記録として照合した。
- この差分に新規ERROR/FATAL、Food Healingのnetwork/sync/capability例外、必須Mixin適用失敗はない。Prism専用Minecraft directoryに`crash-reports`は存在せず、crash report生成も確認されなかった。人間の通常終了報告、`Stopping!`、process終了、終了時異常なしを合わせてclient正常終了と判断する。clientの数値exit codeは取得していないため、client exit code0とは記録しない。
- server側は既記録の通常`stop`、player/world保存・全dimension保存完了（18:31:39.876）、console終了コード0、終了時log異常なしを使用した。再起動・保存試験のやり直しはしていない。専用worldと両側logはそのまま保持する。
- STEP 6のPASSは**今回の正常終了と終了時logレビュー**に限定する。clientの過去IPv6誤接続refused 2件、初回14:32 TIMEOUT、18:21の原因未確定IPv4途中切断、既存起動時warning/DEBUG出力の履歴は残す。接続問題が修正済み・完全解決、全ログが最初から無警告・無エラーだったとは扱わない。
- 変更ファイルは**`docs/CODEX_STATUS.md`のみ**。コード/Config/仕様/resource/Mixin/build設定未変更、build/unit/check/GameTest再実行なし。今回の最終確認はprocess/log/crash reportの読み取りのみで、client/serverや他worldを操作していない。既存の自動PASSと過去手動PASSを変更しない。

#### 2026-09-05 実1-client dedicated / STEP 1〜6完了範囲

**MANUAL SINGLE-CLIENT DEDICATED INTEGRATION TEST / COMPLETE - PASS WITH RECORDED SCOPE AND CONNECTION HISTORY**。Minecraft1.20.1 / Forge47.2.0 / Java17、Food Healing RPG3.0.0のみ、正規MSA認証Prism client1つ、online-mode=trueの専用serverで実施した。各段階の詳細・前後値・server canonical照合は以下の既存履歴に保持する。

| STEP | 完了範囲 | 判定 |
| --- | --- | --- |
| 1 | 正式IPv4での認証接続・world描画・GUI初期表示、既存setskillpoint5のserver→client同期 | MANUAL TESTED / PASS |
| 2 | 根性Lv1を通常GUI購入（未使用5→4/使用済0→1）、ON→OFF、GUI再表示保持とcanonical照合 | MANUAL TESTED / PASS |
| 3 | 満腹時リンゴ1食、HP10→18、食義Lv0/count199→Lv1/count0、未使用4→5/使用済1不変、リンゴ消費、Diversity初回1種/bonus0、Root OFF保持 | MANUAL TESTED / PASS |
| 4 | 通常kill後に人間Respawn、HP20/20と食義/SP/根性Lv1 OFF/Diversity保持 | MANUAL TESTED / PASS |
| 5 | 正常切断後、同じ認証client/アカウントで正式IPv4へ再接続し、STEP 4後の進行・取得・OFF・Diversity保持 | MANUAL TESTED / PASS |
| 6 | 人間の正常切断/client通常終了、専用server通常stop・保存・終了コード0、両process終了・終了時log確認 | PASS |

- 本試験を実2-player同時操作/相互同期/PvP/drop増殖、実旧world移行、server processを跨ぐ手動restart、TaCZ/他MOD統合、全接続試行成功の証明へ拡張しない。**実2-clientは`BLOCKED - SECOND MINECRAFT ACCOUNT REQUIRED`のまま**。既存の自動GameTest/別JVMrestart試験とも区別する。
- 全体は**IN PROGRESS - リリース候補ではない**。実v2.2.5 world提供待ち、実2-client制約、外部MOD統合、OPEN/NOT LOCKEDと未完成機能のgateは維持する。本日の作業はこの記録で完全停止し、TaCZ実銃・外部MOD・OPEN検討・Break Realm・試作型機関弩・新しいデバッグフェーズを開始しない。
- 次回はまず最新Statusから残release gateと提供物/利用者判断待ちを整理する。既存優先順の実v2.2.5 world移行について、由来確認可能な停止済みbackupと対象MOD/versionの提供状況を確認し、未提供ならBLOCKEDを維持する。既存PASSの無理由な再試験や未決定SP換算はしない。接続未解明履歴も独立して保持し、本日は追加調査しない。

#### 2026-09-05 実1-client dedicated / STEP 6専用server正常終了・client終了待ち

- **STEP 6 / SERVER NORMAL SHUTDOWN VERIFIED / CLIENT EXIT + FINAL CLIENT LOG REVIEW PENDING**。人間から正常切断・Minecraftがサーバー一覧画面・再接続なしの報告と終了続行指示を受領。専用server logで**18:30:03.171〜172 JST**の`Disconnected` / `left the game`、consoleの`list`で**18:31:34.168**に接続0/1名を確認した。
- 対象は`<LOCAL_PATH>/food-healing-mod-main\build\verification\single-client-dedicated-20260905-141051\server`の既存console session94552のみ。**18:31:39.068**に通常`stop`を1回送り、**18:31:39.119**の`Saving players` / `Saving worlds`、overworld/nether/endの保存、**18:31:39.876**の各`All chunks are saved`と`All dimensions are saved`を確認。強制終了・kill・再起動は行っていない。
- console session94552は**終了コード0**で完了。**18:31:55 JST**のprocess確認でserver PID28580は存在しない。通常配布Forge serverの終了結果であり、Gradleの`BUILD SUCCESSFUL`とは表記しない。今回build/unit/check/GameTest/runClient/runServerの再実行なし。
- 保存ファイルの読取確認: 専用worldの`level.dat`は18:31:39.870更新、対象UUIDのplayerdata `.dat`は正常切断時の18:30:03.174更新。通常保存のログ・ファイル存在/更新時刻を確認したもので、NBT直接編集やworld再起動による再検証はしていない。worldとログは保存したまま維持する。
- server終了時log確認 **18:32:33 JST**: `latest.log`の184〜197行、`debug.log`の1018〜1034行（今回の18:30:03切断から末尾まで）を確認。新規ERROR/FATAL、network/sync/capability例外、Mixin失敗を検出しなかった。debug末尾は18:31:39.887のserver config unloadで、終了時エラーではない。
- Prism Minecraft client PID23360（14:24:11開始）は**18:31:55時点で稼働中**。人間の次の操作は**Prism側Minecraftを通常終了することだけ**。clientへの強制終了・自動UI操作・再接続は行わない。終了前log基準は`latest.log`146行/25448 bytes、`debug.log`1003行/155287 bytes。人間の終了報告後に末尾差分・process終了を確認し、その時点でSTEP 6全体の`NORMAL SHUTDOWN + FINAL LOG REVIEW / PASS`可否を判定する。まだ全体PASSは付けない。
- 編集ファイルは**`docs/CODEX_STATUS.md`のみ**。ゲーム側は今回の専用serverの通常保存・終了だけ。コード/Config/仕様/resource/Mixin/build設定未変更、追加テストなし、他world/Prism他instanceは操作していない。IPv6誤接続refused、18:21の原因未確定IPv4途中切断、初回14:32 TIMEOUT、既存起動時warning/DEBUG出力は履歴として維持し、修正済み・完全解決とは扱わない。
- 本日の残作業はclient通常終了の確認・最終log確認・Status更新だけ。完了後は完全停止し、TaCZ実銃試験、外部MOD、OPEN、Break Realm、試作型機関弩、新しいデバッグフェーズへ進まない。

#### 2026-09-05 実1-client dedicated / STEP 5切断・再接続保持手動PASS

- **MANUAL SINGLE-CLIENT DEDICATED INTEGRATION TEST / STEP 5 / DISCONNECT + RECONNECT STATE RETENTION / MANUAL TESTED / PASS**: 人間が正常切断後、正式接続先`127.0.0.1:25575`へ同じ正規Prism clientで再接続し、world描画とHP20/20、食義Lv1/count0/200、SP5/使用済1、根性取得Lv1/5 OFFを確認したとの報告を受領。追加購入・toggle・食事は未実施。
- 専用server latest/debug.log: **18:19:51.886〜887 JST**に`Disconnected`と`left the game`。**18:22:50.578**に既存と同じplayer UUIDの認証、**18:22:50.672**にmodlist `[minecraft, forge, foodhealing]`と`foodhealing:main` protocol5 `ACCEPTED`、**18:22:51.921**にhandshake完了、**18:22:51.998〜52.003**にlogin/joinを確認。client latest.logの**09:22:51.969**にもmodded server接続完了がある。client log表記はserver JSTより9時間前として照合した。
- server canonical照合 **18:25:10.121 JST**: Health **20.0f** / 最終Max Health **20.0**、`ShokugiLevel: 1L`、`EatCount: 0L`、`UnspentSkillPoints: 5L`、`SpentSkillPoints: 1L`、`AcquiredSkills: [{Level: 1, Id: "foodhealing:guts"}]`、`DisabledSkills: ["foodhealing:guts"]`。18:15:56の切断前基準と人間の再接続後表示の双方に一致した。
- Diversityは`CurrentEatenFoods: ["minecraft:apple"]`、`AllEatenFoods: ["minecraft:apple"]`の各1種、`MaxHealthBonus: 0`を保持。Inventoryは`[]`、Root累積/予約/各期限は全て0、base statsは空、他skill/True Rootは未取得のまま。`list`でLeva9846の1/1接続を確認した。
- PASSは今回の**実dedicated server + 正規1-clientでの正常切断・正式IPv4接続先への再接続後の進行/取得/toggle保持**に限定する。server processを跨ぐrestart、実2-client、外部MOD、全接続試行の無失敗を証明するものではない。既存の過去手動PASS・自動PASSは変更しない。
- **INVALID / WRONG ENDPOINT - NOT A FOOD HEALING FAIL**: 人間報告のLAN一覧のIPv6項目への誤接続を正式試験から分離する。client latest/debug.logの**09:20:50.089 / 09:21:32.712（JST18:20:50 / 18:21:32）**に別IPv6宛て`Connection refused`のERROR各1件を確認した。ERRORを削除・握り潰さず記録するが、正式な`127.0.0.1:25575`のFood Healing失敗へ読み替えない。IPv6の公開アドレスは本記録へ転記しない。
- **OBSERVED / INCOMPLETE IPV4 LOGIN - CAUSE UNDETERMINED**: 上記IPv6拒否とは別に、client **09:21:00.449**の正式IPv4接続開始、server **18:21:00.458**の接続開始と**18:21:27.580**の`id=<null> ... lost connection: Disconnected`がある。この試行には認証完了/参加logがなく、手動cancelかtimeoutか等の原因はlogから確定できない。IPv6誤接続と混同せず、Food Healing不具合や初回TIMEOUT原因の再現とも断定しない。最終18:22の再接続・保持PASSと分けて未解明履歴として残し、本日は追加再現試験しない。

#### 2026-09-05 実1-client dedicated / STEP 6終了前log確認・正常終了待ち

- **STEP 6 / PRE-SHUTDOWN LOG REVIEW COMPLETE WITH NOTES / NORMAL SHUTDOWN PENDING**。18:26時点までの専用serverおよび確認済みPrism専用instanceの`logs/latest.log`と`logs/debug.log`を読み取り確認。上記IPv6宛てrefusedのERROR2件は両client logに記録されており、両logの重複を4試行とは数えない。server側にERROR/FATAL、両側にFATAL、Food Healingのnetwork/sync/capability例外・必須Mixin適用失敗は今回の検索範囲で検出していない。18:22の正常再接続後にも該当例外は見つかっていない。
- 起動時の記録は別に保持: 両debug.logに`.mixin.out`清掃のDEBUGメッセージとNetty機能検出時の`Reflective setAccessible(true) disabled` / `jdk.internal.misc.Unsafe ... unavailable`のDEBUG stack traceがある。これらを「ログに例外が一切ない」とは表現せず、今回の接続拒否・Food Healing Mixin適用失敗の証拠とも扱わない。clientの既知`super_food` / `normal_test_food` model欠落WARNは未修正のまま。今回これらを理由にcode/resource/JVM設定を変更しない。
- 両専用directoryに`crash-reports`は存在しない。正式IPv4の途中未完了接続と初回TIMEOUTは上記の原因未確定履歴を維持し、接続問題の完全解決やrelease readyを宣言しない。
- 次の人間操作は**ゲームメニューから正常にサーバーを切断することだけ**。再接続・追加試験はしない。切断を確認後、Codexが今回の専用consoleへ通常`stop`を送ってplayer/world保存・process終了を確認する。Prism Minecraft clientも通常終了し、終了時刻・両側log差分・終了結果を追加記録するまでSTEP 6は完了にしない。現時点ではserver/client停止・再起動・kickは行っていない。
- 変更ファイルは**`docs/CODEX_STATUS.md`のみ**。consoleは読取queryのみで、コード/Config/仕様/resource/Mixin/build/テスト変更なし、build/unit/check/GameTest/追加runClient/runServerなし。他world/Prism他instanceに触れず、OPEN/NOT LOCKED・TaCZ・外部MOD・新しい試験フェーズには進まない。本日の残作業はこの終了処理だけ。

#### 2026-09-05 実1-client dedicated / STEP 4死亡・Respawn保持手動PASS

- **MANUAL SINGLE-CLIENT DEDICATED INTEGRATION TEST / STEP 4 / DEATH + RESPAWN STATE RETENTION / MANUAL TESTED / PASS**: 人間が死亡画面の「リスポーン」を1回押し、HP20/20、食義Lv1/count0/200、未使用SP5/使用済1、根性取得Lv1/5・toggle OFFを確認したとの報告を受領。追加購入・toggle・食事・再接続は未実施。
- server照合 **18:15:56 JST**: 今回の専用server console session94552から`data get entity <MINECRAFT_ACCOUNT> Health`、`attribute <MINECRAFT_ACCOUNT> minecraft:generic.max_health get`、`data get entity <MINECRAFT_ACCOUNT> ForgeCaps`、Inventory、`list`を読み取り実行。Health **20.0f** / 最終Max Health **20.0**、`ShokugiLevel: 1L`、`EatCount: 0L`、`UnspentSkillPoints: 5L`、`SpentSkillPoints: 1L`、`AcquiredSkills: [{Level: 1, Id: "foodhealing:guts"}]`、`DisabledSkills: ["foodhealing:guts"]`で、人間のRespawn後表示と一致した。
- Food Diversityは`CurrentEatenFoods: ["minecraft:apple"]`、`AllEatenFoods: ["minecraft:apple"]`の各1種、`MaxHealthBonus: 0`をserver canonicalで確認。死亡前から保持されている。Inventoryは`[]`、Root累積・予約・累積期限・発動期限・cooldown期限は全て0、base statsは空、True Root/他skillは未取得のまま。接続はLeva9846の1/1名。
- 17:50:40の通常kill・Health0と、その後の人間Respawn・server再照合が成立したためSTEP 4をPASSと判定する。範囲は**実dedicated server + 正規Prism clientでの通常死亡後の最終最大HPまでの回復と、今回の進行・取得・toggle・Diversity保持**。HP bonusは今回0のため増加済み最大HPの再試験とはせず、Root効果そのもの、PvP/player死亡drop増殖、再接続、実2-client、外部MODへ拡張しない。過去の死亡Respawn/Root手動PASSと既存自動PASSは維持する。
- **STEP 5 / PREPARED / NEEDS MANUAL DISCONNECT / RECONNECT TEST**: 今回受領したGUI値と18:15:56のserver値を切断前基準として記録。HP20/20、食義Lv1/count0、未使用SP5/使用済1、根性Lv1 OFF、Diversityリンゴ1種/HP bonus0。次の人間操作は**ゲームメニューからサーバーを切断することだけ**。結果を受領してから、同じ認証済みPrism client・同じアカウント・同じserverへの再接続を案内する。Codexによるkick/切断・server再起動はせず、STEP 5はまだPASSにしない。
- 変更ファイルは**`docs/CODEX_STATUS.md`のみ**。今回のconsole操作は読取queryだけで、データ書換え・追加killなし。コード/Config/仕様/resource/Mixin/build/テスト未変更、build/unit/check/GameTest再実行なし、runClient/runServerの追加起動なし。現在server/clientは接続維持し、停止・再起動・他world操作はしていない。STEP 6の終了時両側log確認・正常終了は未実施。
- 初回TIMEOUTの原因未特定・同条件再試行で非再現の履歴、実2-client BLOCKED、外部MOD未確認、OPEN/NOT LOCKEDとrelease gateは維持する。現在状況・未試験一覧・次の作業を更新し、過去の各段階の待機記録は当時の履歴として残す。

#### 2026-09-05 実1-client dedicated / STEP 3食事・進行・SP同期手動PASS

- **MANUAL SINGLE-CLIENT DEDICATED INTEGRATION TEST / STEP 3 / FOOD HEALING + SHOKUGI PROGRESSION + SP SYNC / MANUAL TESTED / PASS**: 人間がFood Level20で準備済みリンゴ1個だけを食べ、HP10/20→18/20、食義Lv0/count199→Lv1/count0/200、未使用SP4→5、使用済SP1不変、根性Lv1/5 OFF保持、リンゴ消費を確認したとの報告を受領。追加食事・toggle・購入・死亡操作はこの報告時点で未実施。
- 食事log **17:45:11.431**: Nutrition4による`Healed 8.0 HP`、**17:45:11.433**: `minecraft:apple / Unique foods: 1/5`を専用server latest.logで確認。食前は17:40:09の記録（HP10/20、Food Level20、Lv0/count199、SP4/使用済1、リンゴ1個、Diversity空）を使用した。
- server照合 **17:50:13 JST**: `data get entity <MINECRAFT_ACCOUNT> Health`は**18.0f**、最終Max Health attributeは**20.0**。ForgeCapsは`ShokugiLevel: 1L`、`EatCount: 0L`、`UnspentSkillPoints: 5L`、`SpentSkillPoints: 1L`、`foodhealing:guts` Level1、`DisabledSkills: ["foodhealing:guts"]`で、人間の食後GUI結果と一致した。
- Inventoryは`[]`でリンゴ残存なし。Diversityは`CurrentEatenFoods: ["minecraft:apple"]`、`AllEatenFoods: ["minecraft:apple"]`の各1件のみ、`MaxHealthBonus: 0`。初回1種登録と5種未満のbonus0を確認した。
- Rootの`RootAccumulatedNutrition`、`RootReservedNutrition`、`RootAccumulationDeadline`、`RootActiveUntil`、`RootCooldownUntil`は全て0。OFFの根性に意図しない累積・予約・期限は発生していない。base statsは空、他skill/True Rootは未取得のまま。
- PASSは**実dedicated server + 正規Prism clientでの満腹時食事、HP回復、食義level-up、SP付与、Diversity初回登録、Root OFF維持の同期**に限定する。過去の食事/Root手動PASSを再試験対象へ戻さず、あらゆるtick境界・自動給餌・外部MOD・実2-clientまで拡張しない。既存GameTestの自動PASSとも分離する。

#### 2026-09-05 実1-client dedicated / STEP 4通常死亡・人間Respawn待ち

- **STEP 4 / DEATH EXECUTED / NEEDS MANUAL RESPAWN TEST**。runbookどおり食後の進行・toggleを先にserver出力へ記録し、Root Lv1 OFF、True Root未取得、Root active期限0、Inventory空、`keepInventory=false`と接続1/1名を確認してから次へ進めた。
- 対象は今回の`build/verification/single-client-dedicated-20260905-141051/server`に接続するLeva9846だけ。実行前に説明し、**17:50:40.021**にconsole session94552から`kill <MINECRAFT_ACCOUNT>`を1回実行。`<MINECRAFT_ACCOUNT> was killed`、`Killed <MINECRAFT_ACCOUNT>`、直後の読取`Health: 0.0f`を確認した。通常vanilla死亡経路であり、NBT編集・entity強制削除・HP上書き・player切断はしていない。
- 死亡前基準: 最終最大HP20、食義Lv1/count0、未使用SP5/使用済SP1、根性Lv1 OFF、Diversityリンゴ1種/HP bonus0、Inventory空。これはRespawn後の測定値ではない。根性効果そのものの再試験やPvP/player死亡drop増殖の証明とは扱わない。
- 次の人間操作は**死亡画面の「リスポーン」を1回押すことだけ**。期待値は最終最大HPまで回復（今回20/20）、SP/食義/根性取得Lv/OFF/Diversityの保持。人間のRespawnと表示結果を受領し、server canonicalを再照合するまでSTEP 4をPASSにしない。再接続試験や追加購入・食事にはまだ進まない。
- 今回のファイル変更は**`docs/CODEX_STATUS.md`のみ**。ゲーム側操作は指定専用playerへの通常killと読取queryのみ。コード/Config/仕様/resource/Mixin/build/テスト変更なし、build/unit/check/GameTest再実行なし。server/clientの停止・再起動・切断、他world操作なし。Normal/昼/晴れ固定・自然回復OFFを維持する。
- STEP 1〜3と過去の各手動/自動PASS、初回TIMEOUTの原因未特定・同条件非再現履歴、実2-client BLOCKED、外部MOD未確認、OPEN/NOT LOCKED、保留機能とrelease gateは維持する。

#### 2026-09-05 実1-client dedicated / STEP 2再表示保持・全体手動PASS

- **MANUAL SINGLE-CLIENT DEDICATED INTEGRATION TEST / STEP 2 / GUI REOPEN STATE RETENTION / MANUAL TESTED / PASS**: 人間がGUIを閉じて通常InventoryをEで開き、SでFood Healing GUIを再表示。前後とも根性取得Lv1/5・toggle OFF・未使用SP4・使用済SP1・食義Lv0/0/200が同一との報告を受領した。追加購入、toggle再操作、食事、他スキル購入はしていない。
- 17:39:26のserver読取でも`foodhealing:guts` Lv1、`DisabledSkills: ["foodhealing:guts"]`、未使用4/使用済1、Shokugi Lv0/count0を再確認。GUI再表示でcanonical状態の変化はなく、人間報告と整合する。
- **MANUAL SINGLE-CLIENT DEDICATED INTEGRATION TEST / STEP 2 / MANUAL TESTED / PASS**: 通常GUI購入、SP/取得Lvのserver照合、ON→OFF操作、DisabledSkillsのserver照合、GUI開き直し保持が全て成立したため、runbookのSTEP 2全体をPASSと判定する。STEP 1 PASSと個々のSTEP 2結果も履歴として維持する。
- これは今回のdedicatedでの購入/toggle/同期/同一接続内GUI再表示保持のPASS。Root効果自体の過去手動PASSを再試験へ戻さず、disconnect/reconnectやserver restartの保持まで拡張しない。STEP 3以降は未試験のまま。

#### 2026-09-05 実1-client dedicated / STEP 3食事・進行の食前準備

- **TEST PREPARATION / COMPLETE - STEP 3 / PREPARED / NEEDS MANUAL TEST**。runbookの既存setcount199・リンゴNutrition4・HP+8・Lv/SP各+1という期待値を再確認。対象は`build/verification/single-client-dedicated-20260905-141051/server`の接続済みLeva9846のみで、他worldへ適用しない。
- 17:39:26の準備前読取: HP20/最終最大HP20、Food Level20、Inventory空、Shokugi Lv0/count0、未使用SP4/使用済SP1、根性Lv1 OFF、True Root/他skill未取得、Food Diversity履歴空/bonus0、base stats空。Normalと既存`naturalRegeneration=false`を確認。専用serverのFood Healing ConfigはhealMultiplier2.0、必要count200、Diversity必要5種のままで変更しない。初期値の不一致はなかった。
- 使用値を事前に説明し、**17:40:08**にconsole session94552から`execute as <MINECRAFT_ACCOUNT> run foodhealing syokugi setcount 199`、`damage <MINECRAFT_ACCOUNT> 10 minecraft:generic`、`give <MINECRAFT_ACCOUNT> minecraft:apple 1`を各1回実行。既存canonical commandと通常vanilla commandだけを使い、セーブNBTの直接編集、skill強制取得、SP再設定はしていない。
- **17:40:09 食前server値**: HP **10.0/20.0**、Food Level **20**、食義 **Lv0/count199**、未使用SP **4**、使用済SP **1**、根性Lv1とOFF登録を保持。Inventoryはslot0の`minecraft:apple` **1個**だけ。Food Diversity履歴/最大HP bonusは0、Root累積/予約/期限等も0。HP不足を用意しただけで、まだ食事処理やHP回復は発生していない。
- 次の人間操作は**渡したリンゴを1個だけ食べること**。満腹度20でも既存AlwaysEat仕様で食べられる。食後期待値はHP **18/20**（Nutrition4による+8）、食義 **Lv1/count0/200**、未使用SP **5**、使用済SP **1**不変、Food Diversityに初回`minecraft:apple`が1件反映（5種未満のためHP bonusは0）。RootはOFFのまま。この期待値を実測/PASSとして先行記録しない。
- 人間の食事結果受領後にserver canonical/Health/Inventory/Food Diversityと照合し、STEP 3を判定する。追加食事・Root発動試験・Lv2購入・他skill購入は案内しない。次のdeath試験にはまだ進まない。
- 今回のファイル変更は**`docs/CODEX_STATUS.md`のみ**。専用playerへの上記食前準備command以外のゲームデータ操作なし。コード/Config/仕様/resource/Mixin/build/テスト変更なし、build/unit/check/GameTest再実行なし。server/clientを停止・再起動・切断せず、Normal/昼/晴れ固定と自然回復OFFを維持する。
- 初回TIMEOUTの原因未特定・同条件非再現履歴、既存自動/手動PASS、実2-client BLOCKED、外部MOD未確認、OPEN/NOT LOCKED、保留機能とrelease gateは維持する。

#### 2026-09-05 実1-client dedicated / STEP 2 Root toggle OFF手動PASS

- **MANUAL SINGLE-CLIENT DEDICATED INTEGRATION TEST / STEP 2 / ROOT TOGGLE OFF / MANUAL TESTED / PASS**: 人間が根性Lv1のONボタンを1回だけ押してOFFへ変更し、GUIの根性取得Lv1/5・toggle OFF・SP4・使用済1を確認したとの報告を受領。Lv2購入・食事・他スキル購入・GUI開き直しは未実施。
- server照合 **17:32:01 JST**: 今回の専用server console session94552で`data get entity <MINECRAFT_ACCOUNT> ForgeCaps`を読み取り、`AcquiredSkills: [{Level: 1, Id: "foodhealing:guts"}]`、`UnspentSkillPoints: 4L`、`SpentSkillPoints: 1L`、**`DisabledSkills: ["foodhealing:guts"]`**を確認。指定4項目がGUI報告と一致した。`list`でLeva9846の1/1接続を確認。
- Shokugi Lv/count0、base stats空、Food Diversity履歴空/HP bonus0、Root累積・予約・期限等0も前回と同じ。根性の取得Lvを消さずにtoggle OFFとなり、追加購入/消費・食義進行は確認されていない。True Rootは未取得。
- 今回のPASSはdedicatedのGUI toggle操作とserver canonical状態の一致に限定する。**STEP 2のGUI再表示後の保持はNOT YET TESTED**であり、STEP 2全体や後続食事/死亡/再接続保持までPASSにしない。既存Root効果の手動PASSも無理由に再試験へ戻さない。
- 次の人間操作は**Food Healing GUIを一度閉じ、`E → S`で開き直すことだけ**。再表示の期待値は根性Lv1/5・OFF・SP4・使用済1。結果受領前には保持PASSにしない。追加購入・toggle再操作・食事はまだ行わない。
- 変更ファイルは**`docs/CODEX_STATUS.md`のみ**。consoleは`data get`と`list`の読取だけで、Codexによるtoggle/SP/skill書換えなし。コード/Config/仕様/resource/Mixin/build/テスト未変更、build/unit/check/GameTest再実行なし。server/clientの停止・再起動・切断、他world操作なし。
- STEP 1/通常購入PASS、初回TIMEOUTの原因未特定・同条件非再現履歴、既存自動/手動PASS、実2-client BLOCKED、外部MOD未確認、OPEN/NOT LOCKED、専用worldの安全設定とrelease gateは維持する。

#### 2026-09-05 実1-client dedicated / STEP 2 Root Lv1通常購入手動PASS

- **MANUAL SINGLE-CLIENT DEDICATED INTEGRATION TEST / STEP 2 / ROOT LV1 NORMAL PURCHASE / MANUAL TESTED / PASS**: 人間がGUIから根性Lv1を1回だけ通常購入。購入前はLv0/食義0/200/未使用SP5/使用済SP0/根性未取得、購入後はLv0/食義0/200/未使用SP4/使用済SP1/根性取得Lv1/5、次Lv +1 SP、toggle ONと表示されたとの報告を受領した。
- server照合 **15:42:53 JST**: 今回の専用server console session94552で`data get entity <MINECRAFT_ACCOUNT> ForgeCaps`を読み取り、`UnspentSkillPoints: 4L`、`SpentSkillPoints: 1L`、`AcquiredSkills: [{Level: 1, Id: "foodhealing:guts"}]`を確認。`foodhealing:guts`は既存SkillIds/日本語定義でRoot（根性）の正式保存IDであり、推測した`foodhealing:root`等へ読み替えない。GUIの購入結果とcanonical値が一致した。
- `DisabledSkills: []`で根性の購入直後ON表示と整合。Shokugi Lv/countは0、base stats空、Food Diversity履歴空/HP bonus0、Root累積/予約/期限等0を維持。他スキル取得はなく、True Rootも未取得。`list`で接続済みLeva9846の1/1名を確認した。
- 人間はLv2購入、toggle操作、食事、他スキル購入をまだ行っていない。今回のPASSは1 SPの通常購入/取得/表示同期に限定し、**STEP 2のtoggle・GUI再表示保持はNOT YET TESTED**。過去のRoot効果自体の手動PASSを再試験へ戻さず、dedicated統合に必要な最小操作だけを続ける。
- 次の人間操作は**根性のONボタンを1回押してOFFにすることだけ**。追加購入・食事はまだ行わない。toggle結果を受領してserver状態と照合した後、runbookのGUI開き直しによる保持確認を別の一操作として案内する。期待値は根性Lv1のままOFF、未使用SP4/使用済SP1不変だが、まだ確認済みにはしない。
- 変更ファイルは**`docs/CODEX_STATUS.md`のみ**。console操作は読取queryのみで、Codexによる購入/skill/toggle/SP書換え、server/client再起動・切断なし。コード/Config/仕様/resource/Mixin/build/テストは変更せず、build/unit/check/GameTest再実行なし。既存自動試験結果は維持する。
- STEP 1 PASS、初回TIMEOUTの原因未特定・同条件非再現履歴、実2-client BLOCKED、外部MOD未確認、OPEN/NOT LOCKED、専用worldのNormal/昼/晴れ固定、release gateは維持する。

#### 2026-09-05 実1-client dedicated / STEP 1手動PASS・STEP 2通常購入へ

- **MANUAL SINGLE-CLIENT DEDICATED INTEGRATION TEST / STEP 1 / MANUAL TESTED / PASS**: 人間がFood Healing RPG GUI上部の`Lv 0 / 食義 0/200 / SP 5 / 使用済 0`を確認したとの結果を受領。前フェーズのconsole読取`UnspentSkillPoints=5 / SpentSkillPoints=0`、Shokugi Lv0/count0と一致。既存setskillpointによるserver→client反映を人間のGUI確認と照合できたため、STEP 1のPARTIAL PASSから完了へ更新した。
- 判定範囲は今回の認証済みPrism 1-client + 独立dedicated serverにおける正規接続/初期表示/少量SP更新同期。人間報告では購入・toggle・食事はまだ未操作。これらやSTEP 2〜6、実2-client、外部MOD統合までPASSにしない。既存GameTest等の自動PASSと今回の手動PASSを分離する。
- 初回接続の**TIMEOUT / 原因未特定 / 同条件再試行で非再現**を維持する。初回の失敗履歴を消さず、コード修正による解決や恒久解決とは扱わない。接続の再試験を無理由に追加しない。
- runbook STEP 2と`SKILL_TREE_SPEC.md`の根性Lv1費用**1 SP**を再確認。次はGUI通常購入のRoot Lv1だけを1回対象とし、購入前の未使用5/使用済0から**未使用4/使用済1/根性Lv1/5**となることが期待値。これは未実行の期待値で、購入成功の記録ではない。
- 次の人間操作は**「スキル」タブで根性Lv1（1 SP）を1回だけ購入すること**。toggleや追加レベル購入・食事はまだ行わず、購入結果を受領してからrunbookの次の一操作へ進む。過去のRoot効果・Heroics・HUD・crafting等の手動PASSを再試験対象へ戻さず、dedicatedの購入/同期確認に必要な最小範囲に限定する。
- 変更ファイルは**`docs/CODEX_STATUS.md`のみ**。コード/Config/仕様/resource/Mixin/build/既存テスト変更なし。今回console commandやSP再設定、購入代行、server/client再起動・切断は行っていない。build/unit/check/GameTest再実行なし、既存結果は維持。専用worldのNormal/昼/晴れ固定も変更していない。
- 実2-clientの`BLOCKED - SECOND MINECRAFT ACCOUNT REQUIRED`、実旧world/外部artifact待ち、OPEN/NOT LOCKED、保留機能gate、リリース候補ではない判定は維持する。

#### 2026-09-05 実1-client dedicated / Status初期表示PASS・少量SP同期準備

- **MANUAL TESTED / PASS（人間報告範囲）**: Statusタブで食義Lv0/count0/200、Food Diversity0、Root累積0/18、各base stat購入回数0を確認したとの報告を受領。購入・toggle・食事は未操作。報告されていないSP表示やskill一覧全体まで手動PASSへ拡張しない。
- 実行前に使用値を**未使用SP = 5**と明示。最新Status、runbook STEP 1、既存commandとGUI共通summaryを読み取り確認した。`setskillpoint`はcanonical APIの`setUnspentSkillPoints(amount)`と`CapabilityEvents.syncToClient(player)`を呼ぶ既存経路であり、setlevelや加算commandへ置き換えていない。
- 対象は今回の専用server `build/verification/single-client-dedicated-20260905-141051/server`、console session94552の接続済みLeva9846だけ。15:07:06の`list`で1/1名、`data get entity <MINECRAFT_ACCOUNT> ForgeCaps`で初期未使用SP0/使用済SP0とその他進行値を再確認。想定外の値の上書きはない。
- **15:07:18 JST**にconsoleから`execute as <MINECRAFT_ACCOUNT> run foodhealing syokugi setskillpoint 5`を1回実行し、`[FoodHealing] Set unspent skill points to 5.`の成功応答を確認。consoleの権限を使用し、playerへのOP付与、NBT直接編集、skill強制取得は行っていない。
- **SERVER CANONICAL CHECK / PASS**: 実行直後の`data get entity <MINECRAFT_ACCOUNT> ForgeCaps`で`UnspentSkillPoints: 5L`、`SpentSkillPoints: 0L`。前後のForgeCaps全文比較で変更は未使用SPの0L→5Lだけ。Shokugi Lv/count0、取得skill/disabled toggle空、base stats空、Food Diversity履歴空/HP bonus0、Root予約等0を保持。これは今回consoleで読み取ったserver値で、GameTestやclient表示の自動PASSではない。
- GUI側は`FoodHealingScreen.renderSummary`が現在のclient capabilityを描画する既存実装。日本語summaryは`Lv 0  食義 0/200  SP 5  使用済 0`が期待値で、Status/スキルの両タブに共通。**SP更新のclient適用・見た目はNEEDS MANUAL TEST**のまま。成功応答や同期呼出しだけでSTEP 1全体をPASSにはしない。
- 変更範囲: 許可された専用playerのcanonical未使用SP設定と**`docs/CODEX_STATUS.md`**。コード/Config/仕様/resource/Mixin/build/テスト変更なし、build/unit/check/GameTest再実行なし。server/clientの再起動・切断、環境固定の変更、他world操作なし。既存自動PASS、実2-client BLOCKED、外部MOD未確認、OPEN/NOT LOCKED、release gateを維持する。
- 次の人間確認は**現在のFood Healing GUI上部の共通summaryで`SP 5 / 使用済 0`を確認することだけ**。購入・toggle・食事はまだ操作しない。確認結果を受領してからSTEP 1判定と次の通常購入へ進む。

#### 2026-09-05 実1-client dedicated / 試験用worldの安全確保

- **TEST ENVIRONMENT PREPARATION / COMPLETE（Food Healing機能のPASSではない）**: 実1-client dedicated試験用worldの安全確保として、昼/晴れ固定、自然湧き停止、既存hostile除去用の一時Peaceful後Normal復帰を実施した。STEP 1のGUI/SP同期は未確認のままで、既存PARTIAL PASSを昇格しない。
- 対象は既存console session94552が接続する`<LOCAL_PATH>/food-healing-mod-main\build\verification\single-client-dedicated-20260905-141051\server`の新規`world`だけ。稼働server/clientを再起動・切断せず、consoleの`list`で操作前後ともLeva9846の1/1接続を確認した。`run/saves`、人間用world、他verification world、Prismの他worldへ操作していない。
- 事前確認14:59:13: difficulty Normal、`naturalRegeneration=false`、`doMobSpawning=false`。同じForge47.2.0/MC1.20.1のローカルsourceで、PlayerのPeaceful時HP/満腹度回復がnaturalRegenerationを条件とすることを確認。この既存falseは変更していない。`data get entity`の読取でFood Healingを含むForgeCaps全体とHealth/foodLevel/foodSaturationLevel/foodExhaustionLevel/Inventoryを記録し、データ書換えはしていない。
- 15:00:10 console操作: `time set day`（1000）、`gamerule doDaylightCycle false`、`weather clear`、`gamerule doWeatherCycle false`。`doMobSpawning`は既にfalseのため設定し直さずqueryで維持を確認した。
- 一時Peaceful: **15:00:10.822 → 15:00:13.921、gametime55320 → 55382（62 server ticks）**。その間、vanillaのPeaceful despawn処理でtick対象の既存hostileを掃除する時間を確保した。個体数の全world調査や未読込chunkの強制loadは行っていない。`finally`経路で`difficulty normal`を送り、直後の`difficulty`queryでも**Normal**を確認。Peacefulは準備中だけで、正式試験difficultyはNormalのまま。
- 最終状態15:00:13/15:00:24: **difficulty=normal、昼（daytime1000で固定）、doDaylightCycle=false、晴れ（weather clear成功応答）、doWeatherCycle=false、doMobSpawning=false**。naturalRegenerationも既存falseを維持。天候・難易度を変更する追加操作は行っていない。
- 前後比較（14:59:13と15:00:24、server latest.logに保存）: ForgeCapsの完全な出力が一致。Shokugi Lv0/count0、未使用SP0/使用済SP0、取得skill空、disabled toggle空、BaseStats空、Food Diversity履歴空/最大HP bonus0、Root予約・累積・期限等0を保持。Health20.0、Food Level20、Saturation5.0、Exhaustion0.0、Inventory空も全て一致。能力値を後から復元・補正して一致させたものではない。
- 変更範囲は上記専用worldに対するconsole環境操作と**`docs/CODEX_STATUS.md`**。Food Healing Config、skill/SP/HP/Food Level/capability/Inventory、Java/Mixin/resource/network/build設定/テスト/仕様への変更なし。worldは通常server管理下のままで、NBTファイルを直接編集せず、saveの初期化・削除もしていない。
- build/unit/check/GameTest/runClient/runServer再実行なし。実2-client BLOCKED、外部MOD未確認、OPEN/NOT LOCKED、初回接続TIMEOUTの原因未特定履歴、release gateを維持する。server/clientは終了せず接続状態を保持する。
- 次の人間操作は既存予定の**`E → S`でFood Healing RPG GUIを開くことだけ**。購入・toggle・食事はまだ操作せず、初期表示確認後にSTEP 1のSP同期へ進む。

#### 2026-09-05 実1-client dedicated / 再接続成功・STEP 1部分PASS

- 人間報告: 同じPrism clientから再接続に成功し、server内のworld描画、Food Healing RPGのHP HUDと食義表示を確認。移動・購入・toggle・食事等はまだ操作していない。**MANUAL SINGLE-CLIENT DEDICATED INTEGRATION TEST / 接続・WORLD表示・HP HUD/食義表示 / MANUAL TESTED / PASS**を報告範囲で記録する。HUD数値とcanonical値の全項目一致やGUI操作まで拡張しない。
- STEP 1判定: **PARTIAL PASS / GUI・SP SYNC NOT YET TESTED**。正本`SINGLE_CLIENT_DEDICATED_MANUAL_TEST.md`のSTEP 1は正規接続だけでなく、新規playerのGUI Lv/count/SP/取得状態記録と、既存setskillpointによるserver→client更新確認も含む。これらが未実施のためSTEP 1全体はまだPASSにしない。STEP 2〜6、購入/toggle/食事/death/保持は未試験のまま。
- 採取結果: `build/verification/single-client-dedicated-20260905-141051/diagnostic-reconnect-20260905-1445`の`state.json`は**14:50:41.475検出→14:51:26.658採取完了 / Failure=null**。両側Thread.printを3回ずつ、合計6件全てexit0（個別timeoutなし）。helper PID26656は採取終了、server PID28580/client PID23360は元の開始時刻のまま稼働継続。採取完了そのものはゲーム試験PASSとは分離する。
- server側の接続証拠: **14:50:42.270** `User Authenticator #1`で正規player UUID確定、**14:50:42.334** MOD一覧`[minecraft, forge, foodhealing]`受入れ、**14:50:42.335** `foodhealing:main` protocol `5`をACCEPTED、**14:50:43.573** `Handshake complete!`、**14:50:43.660** login、**14:50:43.742** `<MINECRAFT_ACCOUNT> joined the game`。serverの`online-mode=true`は変更していない。
- client側証拠（log時刻はJSTより9時間前）: **05:50:42.323** `foodhealing:main` protocol `5`をACCEPTED、registry/config同期のack後、**05:50:43.626** `Connected to a modded server.`。server/clientのchannel・MOD negotiation成立と人間のworld描画報告が一致する。Food Healingの全canonical項目のGUI同期PASSとはまだ扱わない。
- socket: 採取中14:50:42.199〜14:51:26.649の42 snapshotで`127.0.0.1:25575`とclient側`127.0.0.1:52666`の双方向Establishedを確認。14:53のOS再照会でもserver PID28580/client PID23360のEstablishedとserver Listenを確認し、切断や再起動はしていない。
- thread: 初回snapshotにclientの`YggdrasilMinecraftSessionService.joinServer` HTTP応答待ちとsocial block-list取得が見られ、その後認証完了・loginへ進行。10秒/20秒snapshotに同じ認証待ちstackやdeadlockは見られない。正常接続時の一時的HTTP待ちであり、**初回失敗の原因を認証サービス遅延と断定する証拠ではない**。
- ログ判定: 採取区間および14:53の両側latest/debug再確認（server14:50:41/client05:50:41以降）に、新規WARN/ERROR/FATAL、Mixin/network例外、認証失敗、channel/version mismatch、timeout/disconnectなし。起動時の既知warningや初回TIMEOUT履歴は消さない。STEP 6の試験終了時ログ確認は未実施で、この時点の成功範囲と分離する。
- 非変更確認: 採取前後の元/server/client Jar、server.properties、専用JVM引数、Prism instance.cfg/mmc-pack.jsonは全て同hash。Jar3者は`0ED44141581547511787E7562A555D46C9047D52B6FEF6DD338F49AC3A2F9E93`。接続成功のための設定修正・再起動・Jar交換・認証回避はしていない。
- 初回失敗: **CONNECTION FAILED - TIMEOUT / 原因未特定 / 同条件再試行で非再現**。初回の分類Cを過去の事実として保持し、最初から成功・コード修正により解決・恒久解決とは記録しない。再発時には同様に接続中の証拠を確認する。
- 今回の手動記録変更は**`docs/CODEX_STATUS.md`のみ**。採取ファイルは前フェーズhelperの生成記録。Java/Mixin/resource/Config/build/テスト/仕様の変更、server consoleでのSP付与等の操作なし。build/unit/check/GameTest/runClient/runServer再実行なし。既存自動PASS、実2-client BLOCKED、外部MOD未確認、OPEN/NOT LOCKED、release gateは維持する。
- 次の人間操作は**`E`で通常Inventoryを開き、`S`でFood Healing RPG GUIを開くことだけ**。購入・toggleはまだ操作せず、次に初期表示を確認してからSTEP 1の少量SP同期へ進む。server/clientは終了しない。

#### 2026-09-05 実1-client dedicated / 再接続中の一回限り診断を準備

- 利用者から再接続準備完了、server/clientを維持し設定変更・再起動なしとの報告を受領。14:45 JSTにserver PID28580（14:13:39開始）、Prism client PID23360（14:24:11開始）、IPv4 `127.0.0.1:25575` LISTENを再確認。同一Java実体と開始時刻を採取前にも検証する。
- 状態は引き続き**MANUAL SINGLE-CLIENT DEDICATED INTEGRATION TEST / STEP 1 / CONNECTION FAILED - TIMEOUT / INVESTIGATING**。先行接続は分類C、原因未特定。今回の準備成功は接続・認証・ゲーム内操作のPASSではない。
- 採取専用directory: `<LOCAL_PATH>/food-healing-mod-main\build\verification\single-client-dedicated-20260905-141051\diagnostic-reconnect-20260905-1445`。同directoryだけに一時診断`observe.ps1`と生成記録を置いた。Java/Mixin/network実装・build設定・Config・既存テスト・仕様は変更していない。
- **DIAGNOSTIC ARMED**: 非表示の採取helper PID26656、14:47:44開始。接続待ち期限は**2026-09-05 14:57:44 JST**。次の1回の接続ログまたは25575のEstablished socketを検出すると45秒間採取し、helperだけを終了する。期限内に接続がなければ`EXPIRED / NO CONNECTION OBSERVED`で終了し、ゲーム試験FAIL/PASSへ置き換えない。Minecraftの停止・再起動・自動接続は行わない。
- 採取範囲: 両側latest/debugの開始時EOF以降だけ（読取共有で追記を監視）、対象portのTCP状態を約1秒間隔、接続検出付近/約10秒/約20秒のJDK17 `Thread.print`を各server/client最大3回。jcmdは低頻度の診断attachであり、無負荷・完全無干渉とは扱わず、実採取時刻と終了結果を残す。個別jcmdが8秒を超えた場合も停止対象は新規jcmd helperだけで、Minecraft PIDや子processを停止しない。
- 生成物は`events.jsonl`、`state.json`、`server-threads-1..3.txt`、`client-threads-1..3.txt`、helper stdout/stderr。認証token・account保存データ・全起動引数・packet payloadは収集しない。ログ内のcredentialらしい行は採取記録から除外する。Firewall/router/online-mode/server-ip/portへの操作、TCP接続probeはなし。
- 起動前に元/server/client JarのSHA-256が全て既存値`0ED44141581547511787E7562A555D46C9047D52B6FEF6DD338F49AC3A2F9E93`と一致。server.properties/JVM引数/Prism専用instance metadataと合わせて採取前後hashを記録する。採取前listener/PID/log読取を確認し、PowerShell parser error0、helper state=ARMED / Failure=null / stderr空を確認した。採取完了・採取後hash比較はまだ未実施。
- 変更: 上記新規診断用ファイルと`docs/CODEX_STATUS.md`のみ。build/unit/check/GameTestは診断準備のため再実行なし。runClient/runServer再起動なし、既存自動・手動PASS、実2-client BLOCKED、外部MOD未確認、OPEN/NOT LOCKED、将来機能gateは維持する。
- 次の人間操作は**同じ`127.0.0.1:25575`へ今から1回だけ再接続すること**。採取完了後に結果を照合するまで設定変更・再起動・後続ゲーム内試験へ進まない。今回のhelperを永続監視や自動再試行には使用しない。

#### 2026-09-05 実1-client dedicated / STEP 1接続TIMEOUT・読み取り診断

- 判定: **MANUAL SINGLE-CLIENT DEDICATED INTEGRATION TEST / STEP 1 / CONNECTION FAILED - TIMEOUT / INVESTIGATING**。人間から、正規MSA認証Prism client（MC1.20.1 / Forge47.2.0 / Java17.0.7 / Food Healing3.0.0のみ）でDirect Connect `127.0.0.1:25575`が「サーバーへの接続に失敗しました / タイムアウトしました」となった報告を受領。clientは起動済みであり、先行SERVER READY時点の「client未起動」は過去の状態として残す。認証成功・GUI同期・後続STEPはPASSにしない。
- process/socket確認: 14:34および14:40 JSTにserver **PID28580**（14:13:39開始）が生存。client **PID23360**（14:24:11開始）も生存。OSのTCP照会で**LISTEN / LocalAddress 127.0.0.1 / LocalPort25575 / PID28580**、IPv4 loopbackの待受を確認した。25575のIPv6待受なし、最終snapshotには接続中socketなし。listenerのRemoteAddress `0.0.0.0:0`は公開bindを意味せず、LocalAddressは127.0.0.1。権限付き読み取り照会を使用し、接続probe・server consoleへの操作は行っていない。
- server log: `<LOCAL_PATH>/food-healing-mod-main\build\verification\single-client-dedicated-20260905-141051\server\logs\latest.log`と`debug.log`。debugの**14:32:04.444**に`Netty Server IO #1 / FMLHANDSHAKE: Starting new modded impl connection. Found 22 messages to dispatch.`、**14:32:34.326**に`ServerLoginPacketListenerImpl`が`<MINECRAFT_ACCOUNT> / 127.0.0.1:57970 / lost connection: Disconnected`を記録。latestにも後者がある。専用の`connection accepted`行はないが、これらで今回のTCP/初期login到達を確認できる。14:34の定期world保存も続いている。
- client log実在path: `<LOCAL_DOWNLOADS>\PrismLauncher-Windows-MinGW-w64-Portable-9.1\instances\Food Healing RPG v3 Dedicated Test\minecraft\logs\latest.log`および`debug.log`。**05:32:03.979**に`Connecting to 127.0.0.1, 25575`、debugの**05:32:04.170**にIPv4認識、**05:32:04.286**に`Netty Client IO #0 / FMLHANDSHAKE: Starting new vanilla impl connection.`。client記録はserver/JSTより9時間前の表示で、同じ接続試行として対応づけた。clientのこの初期化行だけでvanilla client/Forge未導入とは判断しない（起動logにForge47.2.0とFood Healingの初期化あり）。
- 到達段階: serverの22メッセージはdispatch準備の記録で、全件交換完了ではない。認証完了/UUID確定、Forge MOD一覧交換完了、channel照合完了、player joinは確認できない。server切断時のprofile `id=<null>`だけでMSA不正・未所有・認証失敗とは断定しない。約30秒の経過と人間のTIMEOUT表示は一致するが、client log自体に明示的なtimeout例外は出ていない。
- エラー照合: 両側latest/debugでERROR/FATAL、connection refused、明示的認証失敗、channel/version mismatch、network/Mixin例外は確認されなかった。serverの`Disconnected`以外に原因を示す接続失敗stack traceなし。起動時の`.mixin.out`清掃DEBUGは両側にあり、先行監査のexporter IOException捕捉と同種の記録として残す。clientには既知test foodモデル欠損・shader/sound等の起動warningもあるが、今回の接続原因とは断定せず変更しない。
- 補助診断: 再起動・設定変更なしで、14:38〜14:39頃にJDK17の`jcmd <PID> Thread.print`を両側へ実行（exit0）。切断後snapshotのserver threadはtick待機、server/client Nettyはselector待機、client Render threadはframe待機。採取範囲に明確なdeadlockや認証HTTP待ちstackは見られない。**切断後の状態なので、接続中の一時的停止を否定する証拠にはしない**。認証token・account保存データ・client全起動引数は収集していない。
- 14:39再照合: 元`build/libs/foodhealing-3.0.0.jar`、専用serverの`mods`、Prism専用instanceの`mods`は全て起動前と同じSHA-256 **`0ED44141581547511787E7562A555D46C9047D52B6FEF6DD338F49AC3A2F9E93`**。server.propertiesは`server-ip=127.0.0.1`、`server-port=25575`、`online-mode=true`を読み取り確認。Jar/設定変更なし。
- 分類: **C. TCP接続は届いたが認証/Forge handshake前後で停止**。A（未待受）/B（TCP未到達）ではない。D（Food Healing/network例外確認）を裏付ける記録はなく、Food Healingの不具合とも認証不良ともまだ断定しない。具体的な停止処理は**未特定**。
- 変更ファイル: **`docs/CODEX_STATUS.md`のみ**。server/clientの停止・再起動、online-mode/server-ip/port/Firewall/routerの変更、Jar交換、コード/Config/Mixin/仕様/テスト変更なし。build/unit/check/GameTest/runClient/runServerの再実行なし。既存自動・手動PASS、実2-client BLOCKED、外部MOD未確認、OPEN/NOT LOCKED、保留機能のgateは維持する。
- 次の人間操作は**再接続ボタンを押す前に「再接続の準備ができました」と伝えることだけ**。次フェーズで同じ接続先・同じ設定のまま、接続待ち中の短時間のlog/socket/thread状態を採取できるようにしてから1回再試行する。今回は再接続を代行せず、原因未特定の設定修正や後続ゲーム内試験へ進まない。

#### 2026-09-05 実1-client dedicated / SERVER READY・人間接続待ち

- 利用者からPrism専用instance **Food Healing RPG v3 Dedicated Test** の準備完了、正規MSA認証済み（Offline Accountではない）、Minecraft未起動の報告とserver起動指示を受領。認証情報は読み取らず、認証済みは人間報告として記録する。実server認証接続・GUI・購入・toggle・食事・死亡・再接続はまだ未実施。
- AGENTS、最新Status、正本SINGLE_CLIENT_DEDICATED_MANUAL_TEST、TEST_PLAN、build.gradle/run構成・成果物を再確認。通常Gradle `runServer`は`run`を使い、既存restart opt-inはfixture/自動停止を伴うため使用しなかった。**build.gradle変更なし**で公式Forgeの独立した通常配布serverを新規専用rootに準備した。
- 実在を確認したPrism instance管理directory: `<LOCAL_DOWNLOADS>\PrismLauncher-Windows-MinGW-w64-Portable-9.1\instances\Food Healing RPG v3 Dedicated Test`。Minecraft実行directoryはその中の`minecraft`。`instance.cfg`のnameと`mmc-pack.json`によりMinecraft 1.20.1 / Forge 47.2.0を確認。JavaPathは`<LOCAL_PATH>/Program Files/Eclipse Adoptium/jdk-17.0.7.7-hotspot/bin/javaw.exe`、`OverrideJavaLocation=true`。Prism共通設定ではなくinstance固有のJava 17設定を確認した。
- Prismの`minecraft/mods`は`foodhealing-3.0.0.jar`1個のみ（236,977 bytes）。coremods/resourcepacks/shaderpacks/texturepacks/savesは空。instanceの作成・rename・clone・設定変更・Jar置換・起動・world作成はしていない。他instanceには変更なし。
- 新規専用root: **`<LOCAL_PATH>/food-healing-mod-main\build\verification\single-client-dedicated-20260905-141051`**。server working directory: **同rootの`server`**。未使用rootを作成し、その`server/world`だけを新規生成。`run/world`、`run/saves`、人間用FHR_v3_Manual_20260905、Prism world、他verification worldは使用・コピー・変更していない。既定`run/world/level.dat`は08:38:58のまま。
- Forge入手元: [Forge公式Minecraft 1.20.1配布一覧](http<LOCAL_PATH>/files.minecraftforge.net/net/minecraftforge/forge/index_1.20.1.html)で指定47.2.0を確認し、公式Mavenのinstallerを専用rootに取得。公開SHA-1 `ded43dd18b3a1dd5098b114c28432224d72bd9f7`と一致。installer SHA-256は`BC2A0F7B161A2D8284DF3D603F7F2B22313B246F026AD77511CBD35BCD01CAAC`。`--help`確認後、Java 17で`--installServer .`を専用server内から実行し、通常runtime準備成功・exit0。installerが選んだForge/Mojang依存はchecksum検証付きで当該server内だけに配置。外部MODは追加せず、配布成果物へ同梱しない。installer logは`server/forge-1.20.1-47.2.0-installer.jar.log`。
- 元成果物・serverの`mods/foodhealing-3.0.0.jar`・Prismの同名Jarは3者ともSHA-256 **`0ED44141581547511787E7562A555D46C9047D52B6FEF6DD338F49AC3A2F9E93`**。Statusの最新hashとも一致。Jar内metadataとserver実読込logでFood Healing RPG 3.0.0を確認した。古いJarへの巻戻し・再buildはしていない。
- 起動: Java実体`<LOCAL_PATH>/Program Files\Eclipse Adoptium\jdk-17.0.7.7-hotspot\bin\java.exe`へ`@user_jvm_args.txt @libraries/net/minecraftforge/forge/1.20.1-47.2.0/win_args.txt nogui`を渡した。Forge installer生成の正規引数（`--launchTarget forgeserver`）を使用。専用JVM memoryはXms1G/Xmx3G。dev/restart/TaCZ fixture・自動player生成・自動停止なし、追加MODはFood Healingだけ。
- 14:13:41起動開始、14:13:48にForge47.2.0 / MC1.20.1とFoodHealing初期化完了、**14:14:04 `Done (12.715s)! For help, type "help"`**。稼働Java PID **28580**、操作用exec session **94552**。Gradle経由ではないためrunServerのBUILD SUCCESSFUL/終了判定はなく、現に起動中のserverで接続待ちを確認した。終了させず保持する。
- `server-ip=127.0.0.1`、`server-port=25575`、`online-mode=true`、`max-players=1`、survival/normal、`level-name=world`、RCON/query=false、op-permission-level=2。EULA同意は当該新規server内のみ`eula=true`。起動直前にport確認し、実bind成功後もOS照会で**127.0.0.1:25575 / PID28580**の待受を確認。制限付きOS照会はAccess deniedとなったため、権限付きの読み取り照会で確認し直した。0.0.0.0公開待受・router/port forwarding/firewall変更・offline化は行っていない。
- 14:14:18にこの新規worldのconsoleへ`gamerule naturalRegeneration false`、`gamerule keepInventory false`、`gamerule doMobSpawning false`を実行し、3件の応答を確認。`list`は**0 of a max of 1 players online**。serverが自動生成したFood Healing設定もhealMultiplier2.0、level requirement200を確認し、既存のゲームConfig値・スキル仕様は変更していない。
- 起動後に新規作成された`server/logs/latest.log`と`debug.log`を確認: **ERROR/FATAL 0、Mixin適用失敗0**。Food Healing初期化/registry/world load/network channel/capabilityの起動失敗なし。8個のserver側Mixin適用を実logで確認。これは人間の実ゲーム効果PASSではない。
- warning/DEBUGを隠蔽しない: 新規Configのdefault生成、Forge内部Jarのmods.tomlなし、union assets URL、terminal機能制限、指定Forgeの更新通知を記録。DEBUGのJarJar metadataなしは埋込依存なしの探索結果。`Error cleaning class output directory: .mixin.out`はMixin exporterのIOException捕捉→DEBUG出力であることを同梱Mixin bytecodeで確認し、その後の実Mixin適用・Done到達と区別した。必須Mixin設定緩和やwarningを理由としたコード変更は行っていない。
- 変更範囲: 新規専用root内のserver runtime/配置Jar/eula.txt/server.properties/user_jvm_args.txtと通常起動による新規world/Config/log生成、`docs/CODEX_STATUS.md`、runbookとTEST_PLANの現フェーズ表示。Java/Mixin/gameplay/network/resource/元Config/build.gradle/テスト/仕様/OPENは未変更。build/unit/check/GameTestは今回は未実行で既存PASSを維持。PrismからMinecraftを起動していない。
- 判定: **MANUAL SINGLE-CLIENT DEDICATED INTEGRATION TEST / SERVER READY / HUMAN CONNECTION WAITING**。実2-clientは**BLOCKED - SECOND MINECRAFT ACCOUNT REQUIRED**。認証接続・6段階の手動試験はNOT YET TESTED。次の人間操作はPrismの**Food Healing RPG v3 Dedicated Testを起動することだけ**。server接続以降は一段階ずつ案内する。OPEN/NOT LOCKED/Flight/Ammo Conservation/Break Realm/追加候補/試作型機関弩のgateは全て維持する。
- 14:19:16の最終console確認でも0/1名・server稼働継続、14:19:18時点で両起動logのERROR/FATALは0。`src`全ファイル、build設定、主要仕様、MULTIPLAYER手順、元JarおよびStatusの既存自動/手動試験結果のdigestが前回文書準備前の値と一致することを確認した。serverを停止せず人間待ちで作業を終了する。

#### 2026-09-05 実1-client dedicated手順を認証済みPrism Launcherへ変更

- 利用者は普段Prism Launcherのみを使用するため、正式client launcherをPrismへ変更した。公式Minecraft Launcherに新規Minecraft 1.20.1 / Forge 47.2.0環境は作らない。目的は認証済み正規1-client + dedicated server統合のまま、6段階とFood Healing期待値・ゲーム仕様は不変。
- 最新Status、SINGLE_CLIENT_DEDICATED_MANUAL_TEST、MULTIPLAYER_MANUAL_TEST、TEST_PLANと現build/run構成を再確認した。Minecraft 1.20.1 / Forge 47.2.0 / Java toolchain 17、開発JavaはTemurin 17.0.7+7。`runServer`既定は`run`、restart opt-inはfixture付き、TaCZは明示opt-inのみ。これらを変更・起動していない。
- 13:38の元Jar SHA-256は`0ED44141581547511787E7562A555D46C9047D52B6FEF6DD338F49AC3A2F9E93`で既存成果物と一致。開始時にはこの固定値を盲信せず、その時点の最新`build/libs/foodhealing-3.0.0.jar`とserver/client配置Jarを再照合し、Prism側の古いJar・重複・他MOD混入を確認する。今回コピー・配置はしていない。
- ローカルの既存Prism process PID19048と`<LOCAL_DOWNLOADS>\PrismLauncher-Windows-MinGW-w64-Portable-9.1\prismlauncher.exe`を読み取り確認。実行ファイルProduct/File versionは10.0.5.0で、folder名の9.1から現在のUIを推測しない。Prismは今回起動したものではない。UI・正規認証状態・専用instance・そのJava/pathは未確認で、手順のUI操作名は一般化した。account保存内容は読んでいない。
- **MANUAL SINGLE-CLIENT DEDICATED INTEGRATION TEST / PREPARED / NOT YET TESTED**を維持。正規Microsoft / Minecraftアカウントとして認証済みのPrism clientだけを許可し、Offline Account、認証回避、`online-mode=false`、同一アカウント二重接続、fake playerを禁止。認証失敗時もofflineへ切り替えない。
- 普段のPrism instance、本体共通設定、world/config/optionsは変更・流用しない。既存の完全独立検証instanceは条件監査後のみ利用可、なければ新規専用instanceを用意する。Minecraft 1.20.1 / Forge 47.2.0 / Java 17 / Food Healingのみ、TaCZ・他MOD・utility MOD・shader・追加resource packなし。
- 先行手順の専用root配下`client`を公式Launcherのgame directoryにする前提は撤回。正式client保存先はPrism専用instanceの実行directoryとし、存在確認後にinstance管理directoryとの関係も記録する。**実instance pathは未確認・未登録**。serverは従来の`build/verification/single-client-dedicated-<timestamp>/server`、loopback/候補25575/online-mode=true/max-players=1/RCON・query無効などを維持する。
- Client logは確認済みPrism専用instance内の`logs/latest.log`、必要時`logs/debug.log`と`crash-reports`。server logは専用server内。終了時に両側のERROR/FATAL/Mixin/network・sync/disconnect/capability errorを確認する計画であり、今回のログ試験PASSではない。
- 変更: `docs/SINGLE_CLIENT_DEDICATED_MANUAL_TEST.md`、`docs/TEST_PLAN.md`の別枠参照、`docs/CODEX_STATUS.md`のみ。Java/Mixin/gameplay/Config/resource/network/build設定/既存テストは変更なし。build/unit/check/GameTest再実行なし、server/Minecraft client起動・接続・ゲーム内試験なし。既存自動・手動PASSは維持する。
- 文書更新後の確認: `src`全ファイル、build設定、主要仕様、MULTIPLAYER手順、配布JarのSHA-256は更新前と一致。Status内の既存Build/Test結果（自動・手動・FAIL履歴）のdigestも不変。6段階・数値期待値・server設定予定を維持し、旧client directory前提を除去した。これは文書整合性確認であり新たなゲーム試験PASSではない。
- 実2-clientは引き続き**BLOCKED - SECOND MINECRAFT ACCOUNT REQUIRED**。Prism利用による昇格なし。他のrelease gate、OPEN / NOT LOCKED、Break Realm・試作型機関弩等の保留状態も変更しない。
- 次の人間操作はPrism Launcherのウィンドウを開く/前面に出すことだけ。以後は一操作ずつ専用instance準備を案内し、開始指示前にMinecraft/serverを起動しない。

#### 2026-09-05 実2-clientのアカウント制約・実1-client dedicated手順準備

- 利用者の正規Minecraft Java Editionアカウントは1つのみで、2つ目は用意できない。実2-clientは**BLOCKED - SECOND MINECRAFT ACCOUNT REQUIRED**へ変更し、未確認を維持する。先行Legacy phase 4/5の「2つの正規アカウントで開始可能」は当時の準備履歴であり、最新条件では実施不能。追加アカウント購入や外部協力者を必須要求にしない。
- `online-mode=false`、同一アカウント二重接続、fake/offline playerで代替しない。既存server-side 2-player capability / packet / replayの自動PASSを実2-client PASSへ昇格しない。
- 別項目 **MANUAL SINGLE-CLIENT DEDICATED INTEGRATION TEST / PREPARED (手順のみ) / NOT YET MANUAL INTEGRATION TESTED** を新規手順書へ切り出した。正規接続、GUI/server同期、通常購入、toggle、食事・進行、death/respawn、disconnect/reconnect、進行・toggle保持、両側ERROR/FATAL確認を最小範囲とする。2人間の分離・同時操作・PvP・相手側同期は対象外。
- 専用root予定は`<LOCAL_PATH>/food-healing-mod-main\build\verification\single-client-dedicated-<timestamp>`、その中の`server`と`client`へ隔離。`127.0.0.1`、未使用port候補25575、`online-mode=true`、`max-players=1`、RCON/query無効を起動時の設定予定として記録した。directory・設定ファイルはまだ作成せず、既存worldや設定は未変更。
- 人間の最初の準備は所有アカウントのLauncherログインとMinecraft 1.20.1 / Forge 47.2.0の独立profile有無の確認だけ。認証情報は共有不要。開始指示後に専用game directoryと接続先を案内する。未認証dev `runClient`を正規接続と扱わない。
- 既存`runServer`の既定directoryは`run`、restart opt-inは自動fixture/停止を伴うため、そのまま手動環境へ流用しない。起動時に隔離・fixtureなしの起動方法を確認する。この段階でserver/client起動、接続操作、EULA/Config書込みを行っていない。
- 変更ファイル: `docs/MULTIPLAYER_MANUAL_TEST.md`（既存手順を保留参考として保持）、新規`docs/SINGLE_CLIENT_DEDICATED_MANUAL_TEST.md`、`docs/CODEX_STATUS.md`のみ。Java/Mixin/resource/Config/build設定/仕様/テストは未変更。
- build/unit/check/GameTest: 文書準備のみのため再実行なし。既存vanilla38/38、TaCZ限定38/38・13ケース、保存fixture等の自動結果と既存手動PASSは不変。runClient/runServer: 今回未実行。外部MOD・実旧world・OPEN / NOT LOCKED・release gateも維持し、リリース候補とはしない。
- 次の作業: 人間の準備と開始指示を受けてから実1-client専用環境を起動する。実2-clientはアカウント制約で保留し、他の独立した残gateの優先順位を変更しない。今回は起動せず文書化で停止する。

#### 2026-09-05 Legacy phase 5 / 最終再回帰・元データ非変更・残gate

- 実v2.2.5由来を確認できたworldはなし。`BLOCKED - REAL V2.2.5 WORLD COPY REQUIRED`。探索・監査はrepo内に限定し、複数のv3候補から任意選択して実旧worldと扱っていない。実旧worldコピー元/コピー先は未確定、current v3による実旧world起動・移行完全PASSはなし。
- runtime修正は`ShokugiData.java`のraw退避条件1箇所のみ。旧Lv0/欠損等でも未知legacy dataを保存する。OPEN-01 SP換算、OPEN-05 sublevel変換、既存スキル・倍率・食事・Root・Heroics・Config・Mixin・resourceは未変更。修正前unit FAILを残し、5境界unit→隔離GameTest→旧形式ディスク5ケース/別JVM PASSへ進めた。
- 最終vanilla build/unit/check/隔離GameTestはphase4の**PASS / 38/38 / 33s**。既存自動試験の件数・範囲を手動結果と混同しない。標準JUnit0件とカスタムunitの成功も分離する。
- TaCZ正式対象1.1.7-hotfix2の既存13ケースと別JVMreadを再PASS（PID27724、11:38:34）。続けて導入時GameTestも11:39:03に**38/38 PASS**。`logs/legacy-phase5-final-tacz-20260905.log`、exit0 / BUILD SUCCESSFUL in 1m 3s、ERROR/FATALなし・新規成功receiptあり。ammo非消費/overheat/通常購入/実client操作へPASSを拡張せず、新Adapterは追加していない。
- 配布Jar監査: 119 entry、JSON/mcmeta7個parse、refmapあり、必須Mixinのrequired/defaultRequire維持。新旧restart/audit/TaCZ fixture、ExampleMod、外部Jar/classの混入なし。最新JarのSHA-256は冒頭に更新した。
- 非破壊確認: 人間用`run/saves/FHR_v3_Manual_20260905`は開始時と同じ28ファイル、全SHA-256一致。元development-worlds.zipとTaCZ参考Jarのhashも不変。既定`run/world/level.dat`の更新時刻は08:38:58のまま。今回の全自動world書込みは`build/verification`配下のみ。
- source snapshot比較では以下9ファイルのみ変更/追加、元ファイル消失なし: `ShokugiData.java`、`DataBoundaryRegression.java`、`RestartPersistenceVerification.java`、新規`LegacyDiskVerification.java`、新規`LegacyArchiveAudit.java`、`build.gradle`、`docs/TEST_PLAN.md`、新規`docs/MULTIPLAYER_MANUAL_TEST.md`、この記録。依存追加・外部Jar改変なし。
- 11:39のprocess確認で残るJavaは開始前からのGradle daemon PID32044のみ。全試験serverは正常停止し、実行sessionを回収済み。runClientの起動・人間worldの操作・終了・削除は行っていない。
- 残る独立gate: 実旧world提供、OPEN-01/05（換算/対応づけ）、実2-client・外部MOD操作、正式artifact、OPEN-02/03/04とTrue Root予約途中OFF判断、未完成ownership/Adapter。現在提供済みの資料と確定仕様で実行した範囲に未解決の通常自動FAILはない。人間操作や仕様決定を代行せず、この時点で自律作業を停止する。
- 次の人間作業は、v2.2.5で実使用したと確認できる停止済みworld backupと使用MOD/version情報の提供。その項目が待ちでも、実2-client試験は準備済みrunbookから別途開始可能。未提供artifactの任意download、Break Realm実装、試作型機関弩、候補count変更には着手しない。**短期手動回帰と追加保存安全性は前進したが、v3.0.0はまだリリース候補ではない。**

#### 2026-09-05 Legacy phase 4 / v3 snapshot安全性・実2-client手順準備

- **AUTOMATED SNAPSHOT TESTED / PASS（メモリ内のみ）**: 既存backupのplayerdata166件とlevel.dat/old内Player8件、計174個のschema4 Shokugi NBTを各20回deserialize/serialize。元NBTと完全一致し、元の入力object/ZIP byteも不変。実旧worldを起動した試験ではない。`logs/legacy-phase4-v3-snapshots-20260905.log`、BUILD SUCCESSFUL in 3s。
- `LegacyArchiveAudit.java`に上記検証を追加し、日本語pathはASCII escapeで記録してconsole encodingによる曖昧さを除いた。旧schemaを見つけたときはv3往復PASSへ含めず、provenanceはschemaだけで証明しない設計を維持。
- 実2-client手順を`docs/MULTIPLAYER_MANUAL_TEST.md`に準備し、`TEST_PLAN.md`から参照。専用directory、online-mode=true/loopback/未使用port、2つの正規アカウント、SP20/10、Root1/2・Food Production・Slaughter、count199、相手不変、死亡drop、再接続を7段階で確認する。起動・人間操作・手動PASSはまだ行っていない。
- 最終vanilla `build foodHealingUnitTest check runGameTestServer -PfoodHealingGameTestRun=legacy-final-20260905-1136 --offline`: **PASS / 38/38 / exit0 / BUILD SUCCESSFUL in 33s**。11:37:12に全required PASS、`logs/legacy-phase4-final-vanilla-20260905.log`。unit5,000境界と新legacy5ケースを含む。ERROR/FATALなし。ForgeのJarJar metadataなし等のDEBUG/既知開発warningは失敗隠蔽せず区別する。
- 変更: archive test utility、実2-client runbook、TEST_PLAN参照、この記録。追加runtime変更なし。dedicatedはphase3の5ケースwrite/readと既存2player PASSを維持、runClient未実行。次は承認済みTaCZの既存限定回帰、fixture配布除外、source/world hash、未解決gateの最終確認。

#### 2026-09-05 Legacy phase 3 / synthetic旧playerdataの実ディスク・別JVM保持

- `AUTOMATED FIXTURE TESTED / PASS`（実旧world移行ではない）。隔離先`build/verification/restart-legacy-disk-20260905-1130`で、正Lv1000、Lv0、負Lv、文字列Lv、Lv欠損の5入力を新規fixtureとして生成。入力は`synthetic-legacy-source-0..4.dat`、実PlayerDataStorage用コピーは同環境の`world/playerdata`だけ。ユーザーworld/backup ZIPのNBTは変更していない。
- 初期fixtureはvanilla Player保存NBTに明示的な旧形式capabilityを入れた試験入力で、実v2.2.5 provenanceを偽装しない。通常`placeNewPlayer`→capability読込→保存→stopを通し、別JVMのreadでは入力再初期化なし。
- write PID36304（11:30:10 JST）、read PID29516（11:31:32 JST）で各5ケースPASS。raw backup/未知nested field・int array/count199/ゼロSP/未変換skills/base statsを保持。Inventory/EnderItems/ForgeDataとFood Diversityは入力・保存後NBT比較、foreign Armor7保持、owned modifier20回再構築の非重複、元fixture byte不変も確認した。
- `build foodHealingUnitTest check runServer`（write）はPASS / BUILD SUCCESSFUL in 23s、別JVM `runServer`（read）はPASS / 17s。各回の新しいtoken/phase/PIDの成功receiptとERROR/FATALなし、通常server停止を確認。既存fresh/legacy2playerのrestart回帰もPASS。ログ`logs/legacy-phase3-disk-write-20260905.log`、`logs/legacy-phase3-disk-read-20260905.log`。GameTestはphase2の38/38を維持、runClient未実行。
- 変更: test-only `LegacyDiskVerification.java`、`RestartPersistenceVerification.java`の明示opt-in呼出し、`build.gradle`の`foodHealingLegacyDisk=true` gate、この記録。fixtureは配布Jarに入れず、通常serverで自動実行しない。既存成功receipt/ERROR/FATAL検査を維持した。
- 実旧worldは引き続き提供待ち、OPEN-01換算とOPEN-05のsublevel対応づけは判定していない。次は既存v3バックアップのメモリ内canonical往復、実2-player最小手順、最終build/隔離GameTest・成果物・人間world非変更確認へ進む。実2-clientや外部MOD手動PASSへ昇格しない。

#### 2026-09-05 Legacy phase 2 / Lv0・欠損legacy raw backup最小修正

- 原因: `ShokugiData.deserializeNBT`のraw退避条件が正Lv等のlegacy効果gateに依存。Lv0でも未知field・未LOCK sublevelを保全すべきため、既存pending条件に加えて旧schemaのNBTも丸ごとraw退避するよう変更した。既存raw backupがある場合の優先保持、schema4、SP/skills/base stats/toggle、legacy効果gateは未変更。OPEN-01/05の換算・対応づけはしていない。
- **AUTOMATED FIXTURE TESTED / PASS**: Lv0、負Lv、文字列Lv、Lv欠損、schema3/pending=falseの5ケース。未知のnested data/int arrayとcount199がrawで保持され、10回再読込後も外部参照変更が漏れず、SP/skill/base statを生成しない。修正前FAILはLegacy phase 1のログに保持する。
- `build foodHealingUnitTest check runGameTestServer -PfoodHealingGameTestRun=legacy-20260905-1127 --offline`: **PASS / 38/38 / exit0 / BUILD SUCCESSFUL in 36s**。`logs/legacy-phase2-green-20260905.log`、11:27:02に全required38件PASS、ERROR/FATALなし。GameTestは`build/verification/gametest-legacy-20260905-1127`のみを使用した。
- 変更: `ShokugiData.java`の条件1箇所とコメント、`DataBoundaryRegression.java`、隔離/監査用Gradle設定と`LegacyArchiveAudit.java`、この記録。runtimeの戦闘・食事・手動PASS機能は未変更。通常runClient/runServerの設定も維持。
- dedicated restartは次フェーズ、runClient/実旧world試験は未実施。実旧worldは`BLOCKED - REAL V2.2.5 WORLD COPY REQUIRED`、OPEN-01/05は利用者決定待ち、外部MOD/実2-clientは未試験。次はfixture生成時から明示的に旧形式のplayerdataを用意し、実PlayerDataStorage login/saveと別JVM再読込を検証する。

#### 2026-09-05 Legacy phase 1 / 安全な候補探索・baseline・raw保持FAIL再現

- 最新Status、AGENTS、Core/Skill/Test/Compatibility/Audit/Bug Fix/Loop文書と保存経路・既存restart fixtureを再確認。完了した短期手動PASSは維持した。process照会ではFood Healing client/serverなし（残るJavaはGradle daemonのみ）。人間worldを起動・コピー・編集していない。
- 変更前`build foodHealingUnitTest check --offline`: PASS / exit0 / BUILD SUCCESSFUL in 7s。`logs/legacy-phase0-baseline-20260905.log`。既存GameTest38/38と別JVMrestart PASSは履歴として維持し、今回はまだ再実行していない。
- 非破壊source snapshot: `backups/20260905-112116-legacy-audit/project-source.zip`、SHA-256 `8BDC1C3F530734B565D5C4CF2F830C2EF0647DF2E3258232EBA91C68686010EA`。人間worldはコピーせず、28ファイルのhashを同directoryの`human-world-hashes.xml`へ記録した。
- 探索範囲はrepoのbackups/run保存領域のみ。現行`run/saves/FHR_v3_Manual_20260905`は今回のv3手動world、`run/world`は既存GameTest領域。外部Downloads/ユーザーworldを広域探索していない。
- 読取元: `backups/20260905-070927-baseline/development-worlds.zip`。既存手動world `テスト`、`テスト (1)`、`新規ワールド`、`新規ワールド (1)`とGameTest `world`をNBT監査。level.dat/old計10件のLoadingModListは全てfoodhealing 3.0.0、playerdata166件は全てschema4/LegacyV2Backupなし。v2.2.5由来を確認できず、任意候補を実旧worldに選んでいない。
- **BLOCKED - REAL V2.2.5 WORLD COPY REQUIRED**: 実使用versionの根拠がある停止済み旧world backupが必要。実旧world隔離コピー先は未作成、実旧world起動・移行PASSなし。既存ZIPは読み取り専用で監査前後のSHA-256 `6BFF4E2830F0515F065247875C7AE5DE4EA4C21BE1C0B84092488309BA6F76DB`一致。
- 変更: test-only `LegacyArchiveAudit.java`とGradle監査task、任意`foodHealingGameTestRun`指定時だけ`build/verification/gametest-<id>`を使う隔離設定、`DataBoundaryRegression.java`の追加回帰、この記録。初回監査はPowerShellの未引用property分割でTask '.zip'エラー、引用修正後は監査成功。初回の過大registry出力はLoadingModList要約へ限定。ログの日本語表示一部にencoding乱れがあるが、NBT解析・version/count/hash判定は成功した。
- **AUTOMATED FIXTURE FAIL（修正前）**: Lv0/欠損legacy fieldを対象とした新回帰で`zero/missing legacy level lost raw backup variant=0`。raw退避が`legacyMigrationPending`（正のLv等）に依存し、旧Lv0では未知データが保存されない。`logs/legacy-phase1-archive-and-red-20260905.log`。既存の実worldに不具合が起きたとの主張ではない。
- 次の自動作業: raw退避条件だけを最小修正し、unit/build/check/隔離GameTest、synthetic legacyの実PlayerDataStorageログインと別JVM再保存を検証する。OPEN-01換算/OPEN-05対応づけは決めず、外部MOD/実2-clientは未試験を維持する。

#### 2026-09-05 Heroics極端値2境界・実クライアント数値手動PASS

- 利用者からvanilla実クライアントの追加回帰結果を受領。以下の2項目をそれぞれ`MANUAL NUMERIC TESTED / PASS`として記録する。
- **MANUAL NUMERIC TESTED / PASS - 非発動時のvanilla Armor入力・上限維持**: Heroics Lv5 OFF / True Heroics OFF / Root OFF、防具なし、base Armor40、Toughness0、最大HP1000。`/attribute @s minecraft:generic.armor get`はvanilla上限どおり30.0。`/damage @s 100 minecraft:mob_attack`の実測Healthは1000→924.0f（実damage76）。報告条件でFood Healingが非発動時のvanilla Armor入力・上限を迂回していないことを確認した。
- **MANUAL NUMERIC TESTED / PASS - 発動中の極端Armorで完全無敵化しない**: Heroics Lv5 ON / True Heroics OFF / Root OFF、防具なし、base Armor15、Toughness0、HP400/1000でHeroics発動。`/damage @s 1000 minecraft:mob_attack`の実測Healthは400→300.0f（実damage100）。報告条件で極端なeffective Armorでもdamageが負値/0へ崩壊せず、完全無敵化しないことを確認した。
- Phase 3の修正前FAIL→原因→最小修正→GameTest自動PASSの履歴を保持し、今回の2件の実クライアント数値PASSを別途追加する。既存GameTestの`AUTOMATED TESTED / PASS`や件数は変更しない。
- 手動PASSは上記のtoggle・Armor/Toughness・HP・damage source/量の観察範囲とする。全極端値、他damage source、未取得状態の手動再試験、外部Attribute MODとの統合へ推測で拡張しない。
- 変更ファイル: `docs/CODEX_STATUS.md`のみ。コード・仕様・Config・resource・自動テストは変更していない。build/unit/check/GameTestは記録のみのため再実行せず、既存結果を維持する。Minecraftの操作・終了や追加試験は行っていない。
- 次の作業: 今回予定のHeroics極端値2境界の短い人間回帰は完了。既存順序どおり、次回Status確認後にコピーした旧worldのOPEN非依存保持試験、実2-player等の残項目を整理する。今回は着手せず、未実施の統合試験・OPEN / NOT LOCKED・実装gateとリリース候補ではない判定を維持する。

#### 2026-09-05 True Heroics Armor/Toughness x64実クライアント数値手動PASS

- 利用者から実クライアント数値分離結果を受領。Root OFF、`naturalRegeneration=false`、防具なし、最大HP1000、現在HP700、Heroics Lv5・True Heroics取得済み、base Armor/Toughness 0.25/0.25で実施した。
- OFF基準: Heroics OFF / True Heroics OFFで`/damage @s 20 minecraft:mob_attack`を実行し、Health 700→680.0を確認した。
- ON比較: HP700へ戻し、Heroics ON / True Heroics ON、HP70%で通常Heroicsの40%閾値には達せずTrue Heroicsのみ閾値発動する条件とした。同じ`/damage @s 20 minecraft:mob_attack`でHealth 700→699.9013fを確認した。
- **MANUAL NUMERIC TESTED / PASS**: base 0.25/0.25 x64 = effective Armor/Toughness 16/16と、既知のTrue Heroics専用DR99%を合成した理論値に実測Healthが一致した。今回の数値切り分けにより、True Heroics Armor/Toughness x64の実クライアント手動未確認を解消する。DRを無効化して測定した結果や`/attribute`表示値の直接確認とは扱わない。
- 既存GameTestの`AUTOMATED TESTED / PASS`はそのまま維持し、今回の実クライアント数値PASSを別の履歴として追加する。2026-08-28のCreeper合成防御PASSと当時の`MANUAL ISOLATION NOT YET TESTED`も当時の確認範囲として保持する。
- 変更ファイル: `docs/CODEX_STATUS.md`のみ。コード・仕様・Config・resource・自動テストは変更していない。build/unit/check/GameTestは記録のみのため再実行せず、既存結果を維持する。Minecraftの操作・終了や追加試験は行っていない。
- 次の作業: 既存予定のHeroics極端値境界の短い人間回帰。Root固定窓・食事credit等の既存PASS、未実施の外部MOD統合、OPEN / NOT LOCKED、残作業の優先順は維持する。

#### 2026-09-05 食事credit・後続独立Food Level増加の限定手動PASS

- 利用者から実クライアント報告を受領。Root OFF、`naturalRegeneration=false`、初期Food Level 12、HP 20.0/40.0で実施した。
- リンゴ1個（Nutrition 4）を食べ、Food Level 12→16、HP 20.0→28.0。食事によるHP +8を確認した。
- その後Saturationを0.0まで消費し、Food Level 15、HP 28.0の状態から`/effect give @s minecraft:saturation 1 0 true`を実行。報告された結果はFood Level 15→16、HP 28.0→30.0だった。
- **MANUAL TESTED / PASS（上記観察範囲）**: 非食事由来の独立したFood Level +1に対しFood Healing +2 HPが正常に発生し、先行した食事由来creditによって後続回復が抑止されなかった。
- 元不具合の厳密なtick境界再現は既存GameTestの`AUTOMATED TESTED / PASS`を維持する。今回の手動報告を同一tick/直後snapshot等の厳密再現、全タイミング、外部MOD互換の手動PASSへ拡張しない。Phase 1の修正前FAIL→原因→修正→自動PASSの履歴も保持する。
- 変更ファイル: `docs/CODEX_STATUS.md`のみ。コード・仕様・Config・resource・自動テストは変更していない。build/unit/check/GameTestは記録のみのため再実行せず、既存結果を維持する。Minecraftの操作・終了や次の試験は行っていない。
- 次の作業: 既存予定のTrue Heroics x64手動分離とHeroics極端値の短い人間回帰。Root固定15秒窓の既存手動PASS、OPEN / NOT LOCKED、他項目の試験状態・優先順位は維持する。

#### 2026-09-05 Root固定15秒窓・実クライアント手動回帰PASS

- 利用者からの実クライアント報告を受領。条件はRoot Lv5 ON / True Root OFF、各食事のNutritionは6。1食目の完了を0秒とし、2食目を約12秒、3食目を約26秒で完了した。
- 結果: 3食目完了時にもRootは発動しなかった。最初の対象食事からの固定15秒窓が後続食事で延長されないことを、**MANUAL TESTED / PASS**として記録する。予定例の0/14/28秒ではなく、実測報告の0/約12/約26秒を試験条件とする。
- Phase 8の修正前unit FAIL→固定窓修正→自動PASSに、今回の手動PASSを追加した。従来の履歴は保持し、15秒ちょうどの境界、True Rootの未LOCK予約扱い、他の未確認項目へ手動PASSを拡張しない。
- 変更ファイル: `docs/CODEX_STATUS.md`のみ。コード・仕様・Config・resource・自動テストは変更していない。build/unit/check/GameTestは記録のみのため再実行せず、既存の自動結果を維持する。
- Minecraftの終了操作は行っていない。今回の作業は報告の記録だけで、次の試験・実装は開始していない。次の手動候補は既存予定のTrue Heroics x64分離等であり、結果受領までは未確認のまま。

#### 2026-09-05 Phase 14 / 最終再検証・残gate整理

- 最終`build foodHealingUnitTest check runGameTestServer --offline`は08:38:56 JST、**全task成功 / vanilla GameTest 38/38 / exit0 / BUILD SUCCESSFUL in 25s**。`logs/phase14-final-all-20260905.log`。unitは固定seed5,000境界を含め成功（今回90ms）、標準JUnitの実件数0とは分離。通常成功runにERROR/FATALなし。
- TaCZ 1.1.7-hotfix2の最終read PID9048、08:37:54 JST、13実統合ケースと2player保存保持・新規成功receiptがPASS。`logs/phase14-final-tacz-read-20260905.log`、exit0 / BUILD SUCCESSFUL in 31s。TaCZ導入GameTestもPhase 10で38/38。1.0.3や実client射撃、弾薬非消費/過熱、別gunpackへPASSを拡張しない。
- 配布Jarは119 entry、JSON/mcmeta7個parse、Mixin9個とrefmapを確認。required=true/defaultRequire=1維持、restart/TaCZ専用fixture、外部Jar/class、ExampleModの混入なし。TaCZ参照JarのSHA-256は承認時と一致。runtimeのdefault pack展開は隔離build下のみで配布対象外。
- baseline source snapshotとのhash比較: runtime変更は`FoodHealingBaseStats.java`、`FoodHealingHandler.java`、`FoodHealingTransactions.java`、`RootController.java`、`mixin/LivingEntityMixin.java`。既存回帰変更はcombat/optional/P0 GameTestsと`ShokugiDataUnitTest.java`。追加は`DataBoundaryRegression.java`、`FoodHealingNetworkGameTests.java`、restart/TaCZ fixture各1。ほか`build.gradle`、`docs/SKILL_TREE_SPEC.md`、`docs/TEST_PLAN.md`、この記録の計17ファイル。baselineにあった対象sourceの消失なし。元の非破壊backupは維持。
- 原因→修正の総括: 食事creditの後続原因への持越し、DR購入世代飽和、正damageのdouble underflow、Heroics非発動時のvanilla属性入力迂回、極端Armorでの負damage化、食事ごとのRoot期限延長、通常server試験のfalse-greenを是正。初回FAIL/fixture誤設定/意図的負試験は各ログとフェーズに保持。既存倍率/SP/200 count/進行schemaを変更していない。Root固定窓だけは利用者の明示回答に基づく文書化・修正。
- 自動化範囲を拡張: 31→38 GameTests、long/NBT/packet replay、2,000回modifier再構築、実End portal/credits応答、別JVM保存読込、6,000tick/30保存のsoakを2回、optional absent、指定TaCZ実server統合。これらは実world移行/実2-player/長期MODpack性能/視覚・操作PASSではない。
- 08:39 JSTのprocess照会でforgeclient/forgeserver/forgegametestserverはいずれも残っていない。全実行sessionを回収し、通常成功runは正常停止済み。意図的負試験の隔離crashと正常停止を区別する。実ユーザーworldの起動/初期化/直接編集、project外server設定、公開/push/uploadは行っていない。
- 次段階のgate: Root固定窓等の人間回帰、True Heroics x64手動分離、実2-player、正式外部artifact、OPEN決定。Flightはvanillaの単一mayfly booleanから同時所有者を証明できず、対象providerの所有API確認なしに解除処理を追加しない。TaCZ非消費/過熱Adapterも、現在の命中fixtureでは発射・reload・barrel・client prediction/heat表示を証明できず、通常操作のbaselineと専用検証が必要。未完成機能を今回の安定化PASSで購入可能にしない。
- 現時点の判定: **自動安定化・限定統合の検証範囲は拡大したが、v3.0.0はリリース候補ではない**。今回の確認済み範囲に未解決の通常自動試験FAILはない。新規機能の実装を始めず、下記の人間/資料/仕様gateを明記して継続先を引き継ぐ。

#### 2026-09-05 Phase 13 / dedicated試験のfalse-green防止

- Minecraft/Forge実sourceではserver threadの例外を捕捉して停止するため、正常でないfixtureでもJavaExecがexit0となり得た。専用の空`restart-negative-no-writer-20260905-0830`でreadを要求する意図的負試験により、`FOODHEALING_RESTART_FAILED`/FATALにもかかわらず旧GradleがBUILD SUCCESSFULとする問題を再現（`logs/phase13-harness-negative-before-20260905.log`）。これは試験ハーネスの不具合であり、ユーザーworldのcrashではない。
- 修正: 実行ごとのUUID tokenを渡し、fixture全assert完了時だけphase/PID/PASSの新規receiptを生成する。Gradleは自身のtoken/phaseのreceiptを要求し、最新server logのERROR/FATALも検査する。古いPASS流用、途中crash、成功logだけの誤認を防ぐ。通常runServerには専用propertyなしなら影響なし。
- 初回compileはsoak event methodからchecked IOExceptionを再throwする宣言不足でFAIL（`phase13-harness-negative-after-20260905.log`）。test-only署名を訂正後、同じ負試験を再実行し、期待どおり`runServer FAILED / exit1 / success receiptなし`となった（`phase13-harness-negative-verified-20260905.log`）。意図的負試験の期待失敗を確認したのであり、未解決FAILを無視したわけではない。
- 正常系は別隔離先`restart-receipts-20260905-0830`でwrite PID34820（08:30:16）、read PID3216（08:31:25）ともPASS/exit0。その後soak PID20148は08:32:03〜08:37:03、6000 tick/30回保存/2player、299998ms、成功receiptを含めPASS/exit0 / BUILD SUCCESSFUL in 5m 17s。heap観測0.69〜0.99GB程度、ERROR/FATAL/Can't keep upなし。長期性能PASSへの拡張はしない。
- ログ: `phase13-harness-write-20260905.log`、`phase13-harness-read-20260905.log`、`phase13-harness-soak-20260905.log`。変更: `build.gradle`、`RestartPersistenceVerification.java`、この記録のみ。gameplay/保存schema/Mixin/skill仕様は未変更。最後に全build/unit/check/GameTestとTaCZ正常receiptを再確認する。

#### 2026-09-05 Phase 12 / optional未導入の再回帰

- TaCZ opt-in propertyなしの通常構成へ戻し、`build foodHealingUnitTest check runGameTestServer --offline`を再実行。08:26:29 JST、exit0 / BUILD SUCCESSFUL in 24s、**38/38 PASS**。`logs/phase12-final-vanilla-20260905.log`。
- TaCZ未導入の購入拒否とSP非消費を再確認し、導入時38/38と混同せず両構成をPASSとした。runClient未実行。gameplay/仕様の追加変更なし。
- 後続監査で通常dedicated serverが内部例外を捕捉してGradle exit0となり得る点を発見。今回までの成功は全て明示PASS logとERROR/FATAL有無も確認済みで取り消さないが、将来の誤判定防止のためtest harnessの成功receiptを追加する。

#### 2026-09-05 Phase 11 / TaCZ実銃弾のPlayer境界・非TaCZ弾薬分離

- `TaczIntegrationVerification.java`だけを拡張し、実TaCZ弾丸でRoot active時はHP4→1、生存かつ死亡drop0回を確認。RootをOFFにした次の致死hitでは通常死亡し、Slaughter取得attackerでもLivingDropsEventは1回、所持ダイヤ7個は7個のまま。テストが生成したdrop entityだけを後処理し、通常death flowを迂回していない。
- Satisfaction3/Gathering10/Unbreaking10/Armor Mastery1のfixtureで、実`ModernKineticGunScriptAPI.reduceAmmoOnce()`が成功しmagazine17→16を確認。非TaCZスキルが弾薬節約を行わない境界のみPASS。Ammo Conservationの未実装非消費効果や全bolt方式の互換性をPASSとはしない。
- **AUTOMATED INTEGRATION TESTED / PASS**: TaCZ 1.1.7-hotfix2、命中/取消/倍率/Player/弾薬分離の計13ケース。既存2playerの保存読込も再PASS。PID34416、08:24:30 JST、`FOODHEALING_TACZ_DAMAGE_PASS cases=13`、`FOODHEALING_TACZ_PLAYER_PASS rootProtected=true deathEvents=1 diamonds=7`、normal stop / exit0 / BUILD SUCCESSFUL in 31s。
- ログ: `logs/phase11-tacz-player-20260905.log`。ERROR/FATALなし。変更ファイルはoptional test fixtureとこの記録のみ。build/unit/check/GameTestはPhase 10のTaCZ導入38/38を維持し、次にvanilla構成で最終再実行する。
- runClientのTaCZ導入試験、実銃操作、射撃network/flight、heat gauge、custom gunpack、他MOD組合せは未試験。通常購入が停止しているAmmo Conservationの非消費/過熱Adapterは引き続き未完成。変更は既存互換境界の試験支援であり、前提未決定skillや新規Adapterを有効化していない。

#### 2026-09-05 Phase 10 / 承認済みTaCZ 1.1.7-hotfix2限定統合

- 利用者が正式対象に指定した`libs/tacz-1.20.1.jar`のみ使用。SHA-256をGradle起動時に照合し、version違いでは拒否する。`foodHealingTaczIntegration=true`かつ隔離run IDの明示時だけローカルJarをForgeGradleでdeobfuscateしてruntimeへ加える。Downloads 1.0.3は対象外、任意Jar downloadなし。
- TaCZ自身による通常初期化で隔離run内の`tacz`/`tacz_backup`へdefault packが展開される。これはテスト実行時の生成物で、Food Healingのsource/resourcesへ取り込まず配布物へ含めない。参照Jarそのものは未改変。
- `AUTOMATED INTEGRATION TESTED / PASS - 起動/別JVM保存読込`: 隔離先`build/verification/restart-tacz-20260905-0810`、write PID10332（08:07:46）、read PID25868（08:17:46）。実TaCZ version確認、fresh/legacyの2player保持、normal stop / exit0 / BUILD SUCCESSFUL。ログ`phase10-tacz-write-20260905.log`、`phase10-tacz-damage-20260905.log`。
- `AUTOMATED INTEGRATION TESTED / PASS - 実弾丸命中10ケース`: 実Jarのdefault Glock index/dataと公開cache API、`EntityKineticBullet.onHitEntity`を使用。body hit 7、global x2で14、TaCZ基礎x2合成28、Heroics Lv5合成140/OFF28、既存Ammo skillのdamage部分42/OFF28、装甲貫通分割の合計28、TaCZ Pre取消0、その後28を確認。各Pre eventは1回。これは飛翔/照準/発射入力ではなく実命中処理の試験。
- TaCZは同じ弾丸を非貫通/貫通の2成分でhurtする実装。両成分の合計を測定し、片方を誤って重複として消していない。銃弾専用倍率はLivingHurtで1回ずつ適用される。
- 初回TaCZ導入GameTestは37 PASS / 1 FAIL。原因は未導入専用fixtureの`!isLoaded(tacz)`前提であり、gameplay failureではない。テストを未導入=`OPTIONAL_MOD_MISSING`、導入済み=`IMPLEMENTATION_PENDING`の双方でlevel/未使用SP/使用済みSP不変を検証する形へ変更。スキップや空PASSにはしていない。初回隔離GameTestではserver.properties未生成のERRORもあり、Minecraftが生成した後の再実行では解消した。
- 修正後`build foodHealingUnitTest check runGameTestServer`は**PASS / 38/38**（TaCZ導入）。`logs/phase10-tacz-regression-fixed-20260905.log`。初回FAILは`phase10-tacz-regression-20260905.log`に保持。専用GameTest working directoryを使用し、通常runのworldは不使用。
- 変更: `build.gradle`、`RestartPersistenceVerification.java`、新規`src/taczTest/java/com/leva/foodhealing/TaczIntegrationVerification.java`、`FoodHealingOptionalAbsenceGameTests.java`、この記録。通常main compileへTaCZ直接依存なし。optional test source setは通常起動・配布Jarに含めない。Java gameplay/Mixin/保存schema/skill値は変更していない。
- `STATIC AUDITED / 未実装`: `ModernKineticGunScriptAPI.reduceAmmoOnce`はbolt方式、barrel、magazine、inventory ammoごとの消費を所有し、`handleShootHeat`はheat加算とlockを所有する。公開setterの存在だけを根拠にconsume/restoreや毎tick解除を実装しない。Ammo Conservationの非消費/過熱Adapterは引き続き`IMPLEMENTATION_PENDING`で購入拒否。damage部分のfixture取得はテスト初期化であり、通常購入成功や弾薬節約PASSではない。
- runClient/実銃操作/GUI/heat gauge/他gunpack/他MOD組合せは`NOT YET INTEGRATION TESTED`。次はTaCZ実命中時のPlayer保護と死亡drop境界、vanilla再回帰、最終記録を進める。OPEN/Break Realm/crossbowのgateを維持。

#### 2026-09-05 Phase 9 / client smoke・artifact・外部資料inventory

- 最新vanilla `runClient`: 08:03:41起動、Food Healing client setup/resource reload/OpenAL/atlas初期化まで到達。worldを開かず、今回のforgeclient PID10260だけに通常window close要求を送り、08:04:35に`Stopping!`、Gradle exit0 / `BUILD SUCCESSFUL in 59s`。`logs/phase9-client-smoke-20260905.log`。GUI/HUD/キー操作のmanual PASS追加なし。
- 未解決warningを明示: `super_food`と`normal_test_food`のitem model JSONが元から存在せず、clientでFileNotFound warning。既存registryを削除したり、見た目を独断で置換したりしていない。Forge dev refmap、union URL、vanilla goat sound2件等のwarningも分類し、ERROR/FATAL/必須Mixin失敗はなかった。
- 配布Jar静的監査PASS: JSON/mcmeta7個をparse、9個のMixin classと対応refmap entryを確認。required=true/defaultRequire=1を維持。restart fixture、ExampleMod、外部MOD class/Jarの混入なし。dev時refmap warningと配布Jarのrefmap欠落を混同しない。
- post-soak再読込: PID27156、08:03:03 JST、`FOODHEALING_RESTART_PASS phase=read`、通常終了・BUILD SUCCESSFUL（`logs/phase8-post-soak-read-20260905.log`）。Root修正版でも、6,000tick/30回保存後のデータ保持を確認した。
- TaCZローカル資料: `libs/tacz-1.20.1.jar`のmanifestは**1.1.7-hotfix2**、modId=tacz、SHA-256 `FC5F1DAB09AFD5399604DB0F60845D41D46CF015C158CBC12E7EF716CF6BDB46`。利用者がこのversionを今回の正式統合対象として明示承認した。Downloadsの1.0.3は今回は対象外。
- TaCZは`PARTIAL STATIC AUDIT`: 実Jarの公開GunShootEvent/GunFireEventのlogical side、EntityHurtByGunEvent、IGunOperator、GunHeatDataのAPI存在を確認。内部実行・ammo非消費・overheat・銃撃の互換PASSにはまだしない。Jar/sourceをrepoへ追加コピー/同梱していない。
- `tmp_monolith/META-INF/mods.toml`はthe_trial_monolith 1.2.8を示す参考資料だが、boss機構の監査・Break Realm実装は開始していない。`run/mods`は空。libs/run/mods/Downloadsの対象名Jar検索では、AttributeFix/L2/Auto Leveling/Traveler's/Sophisticated/Farmer's Delight等の対象artifactは未確認。Minecraft1.20.1 Forge用の採用versionとJar/sourceが必要で、`BLOCKED - TARGET MOD ARTIFACT REQUIRED`。
- 本フェーズ変更はこの記録のみ。build/unit/check/GameTestは直前の38/38成功を維持。次は明示承認されたTaCZだけを隔離した起動試験へ進める。全manual/他MOD/OPENの結果は維持する。

#### 2026-09-05 Phase 8 / 利用者決定に基づくRoot固定窓

- 調査で、従来codeが食事ごとに蓄積deadlineを300 tick延長することを発見。15秒固定窓/移動窓のどちらかを推測せず問い合わせ、利用者から「最初の対象食事から15秒の固定窓、窓内の食事は期限を延ばさず累積、超過時reset、次食事で再開始、発動時余剰持越しなし」と明示決定を受領した。移動窓は採用しない。
- 修正前unitは`later food must not extend the fixed window`でFAIL（`logs/phase8-root-window-red-20260905.log`）。最初の食事だけがdeadlineを作る限定修正を実施。expiry時のcanonical変更もsync用changed flagへ反映し、次tick以降は重複syncしない。
- 回帰: Nutrition6をtick100/390/680で摂っても発動しない。最初のdeadline400は変わらず、tick680で新窓980を開始。tick980の追加12は境界内として発動、Lv5終了1280、蓄積と余剰は0。食事なしのexpiryも1回だけ変更通知する。
- `build foodHealingUnitTest check runGameTestServer`: **PASS / 38/38**、`logs/phase8-root-window-green-20260905.log`。既存True Root予約/親toggle回帰もPASS。変更: `RootController.java`、`ShokugiDataUnitTest.java`、`docs/SKILL_TREE_SPEC.md`、`docs/TEST_PLAN.md`、この記録。
- 仕様書の追加LOCKは上記の利用者決定だけ。Root Nutrition18、各Lv時間/SP、True Root予約1回、途中OFF時の既存予約扱い（未LOCK）は変更していない。既存進行schema/値も変更なし。既に途中まで蓄積中の保存deadlineは読み替えず、自然終了後の新windowから固定窓を開始する。
- 過去のRoot/True Root manual PASSは当時の試験履歴として維持。今回の期限非延長は`AUTOMATED TESTED`で、修正版の人間確認は未実施。次の短い手動回帰はRoot Lv5 ON/True Root OFFでNutrition6の食事完了を約0/14/28秒に配置し、3食目で発動しないこと。

#### 2026-09-05 Phase 7 / 隔離dedicated server 6,000 tick soak

- `restartTest`専用fixtureにsoak modeを追加。既存の隔離worldを実dedicated serverで6,000 tick運転し、2playerのcanonical dataとforeign/owned modifierを周期確認。200tickごとの通常PlayerDataStorage保存を30回実施した。synthetic connectionは正規keepalive応答を返し、timeout機構は無効化していない。
- `AUTOMATED TESTED / PASS`: PID25284、07:52:11→07:57:11 JST、実測300004ms、`FOODHEALING_SOAK_PASS ticks=6000 saves=30 players=2`。通常stop、Gradle exit0 / `BUILD SUCCESSFUL in 5m 18s`。`logs/phase7-soak-20260905.log`。ERROR/FATAL/Can't keep upなし。
- sampled heap usedは約1.23GB→1.48GB→最終0.70GB。GC前後を含む観測値であり、メモリリーク不存在や長時間TPSを保証するベンチマークではない。大規模MODpack・24時間等の運用試験は未実施。
- 変更: `build.gradle`、`RestartPersistenceVerification.java`、この記録。`logs/phase7-soak-build-20260905.log`でbuild/unit/check PASS。GameTestはPhase 6の38/38を維持し、その後Phase 8でも再PASS。runClient未実行。外部MOD未導入。
- 実world/proj外serverには触れず、隔離serverは正常終了済み。次作業は現行codeで再読込・client smoke・成果物監査の最終確認。

#### 2026-09-05 Phase 6 / End portal・credits応答経路

- Minecraft/Forge実sourceの`EndPortalBlock.entityInside`、`ServerPlayer.changeDimension`、`ServerGamePacketListenerImpl.handleClientCommand(PERFORM_RESPAWN)`を照合し、既存のteleporter/直接respawn fixtureに欠けていた実経路を追加した。
- `endPortalCreditsResponsePreservesPartialHealthAndProgress`: End内のテストportal block→wonGame→WIN_GAME packetを1回受信→client応答相当のPERFORM_RESPAWN→Overworldの新ServerPlayerを確認。HP63/100、SP/取得/toggle/Food Diversity保持、重複応答で再respawn・進行変更なしをPASS。portal位置の旧blockをfinallyで復元する。
- 変更: `FoodHealingPriorityZeroGameTests.java`、この記録。runtimeコード/仕様/移行変更なし。`build foodHealingUnitTest check runGameTestServer`: **PASS / 38/38**、`logs/phase6-credits-20260905.log`。ERROR/FATALなし。
- server-side portal/credits handshakeは`AUTOMATED TESTED`。credits画面の描画/人間操作は追加試験しておらず、既報End帰還のmanual PASSと分離する。runClient未実行、外部MOD未導入。
- 次作業: 隔離serverの6,000実tick限定soakと成果物/静的互換監査。長時間modpack運用の代替PASSにはしない。

#### 2026-09-05 Phase 5 / packet・long・反復保存・modifier負荷

- serverbound購入/toggleは`PLAY_TO_SERVER`とForge `consumerMainThread`で登録され、senderのcanonical capabilityだけを変更することを実sourceで確認した。新規GameTestは実SimpleChannel encode/decode、NetworkHooks dispatch、handlerを通す。contextの偽造・private reflectionは使用しない。
- `AUTOMATED TESTED / PASS`: 同一skill/base stat購入とtoggle stateを各100回再送しても購入は各1回、SPは合計2のみ消費。負の/極大expected generation、不明ID、未取得toggleでcanonical data不変。player AのpacketがBのdata/syncに漏れず、Long.MAX_VALUEの同期snapshotも不変。
- `DataBoundaryRegression`: 固定seed F0032026の5,000件でBigIntegerを使った独立SP判定、atomic拒否、long保存を確認。legacy raw backupを1,000回保存読込して外部からのNBT変更と隔離され、SPを生成しない。NaN/Infinity/負数/型違いNBTの非負long境界と正規化の安定性、129文字ID、truncated payload、過長VarLongの拒否もPASS。
- `repeatedExtremeRebuildsPreserveCanonicalDataAndForeignOwnership`: 0/1/Long.MAX_VALUE取得数とON/OFFで2,000回再構築、100回Player NBT save/load。canonical不変、owned modifier非重複、foreign Double.MAX_VALUE保持、実効値finiteを確認。今回ローカル実測52ms、unit境界114ms。限定fixtureの測定であり、実server TPSや長時間リーク未発生の保証ではない。
- 初回unit FAILはShokugiSyncPacketがConfig未ロードのunit環境で生成されたため（`logs/phase5-boundaries-20260905.log`）。該当snapshot試験をGameTestへ移し、runtimeコードを変えずに解決。`phase5-boundaries-fixed-20260905.log`で36/36、負荷追加後`logs/phase5-stress-20260905.log`で**build/unit/check PASS、GameTest 37/37**。ERROR/FATALなし。
- 変更: `src/test/java/com/leva/foodhealing/DataBoundaryRegression.java`、`ShokugiDataUnitTest.java`、`FoodHealingNetworkGameTests.java`、`FoodHealingPriorityZeroGameTests.java`、この記録。保存仕様・gameplay実装変更なし。runClient未実行、runServerはPhase 4結果を維持。
- 人間/外部MODの結果は追加しない。実2-player、長時間modpack稼働、未提供artifactは引き続き未検証。次作業は配布Jar/resource/Mixin/optional artifactの静的監査と、安全に自動化できる残境界の確認。

#### 2026-09-05 Phase 4 / 通常dedicated server別JVM保存・再起動

- 利用者のEULA同意と試験専用起動許可に基づき、`build/verification/restart-20260905-0735`だけに`eula=true`とloopback/自動portのserver設定を生成した。`run/world`、`run/saves`、外部server設定は使用・変更していない。
- 配布Jarに含めない`restartTest` source setと`RestartPersistenceVerification.java`を追加。通常`runServer`のPlayerList login、PlayerDataStorage保存、正常stopを使い、writeとreadを別JVMで実施した。テスト用canonical APIで初期値を作成し、保存ファイルの直接編集でPASSさせていない。
- fresh v3とlegacy fixtureの2playerについて、long食義/SP、used SP、全base stat取得数、Root/True Root/Heroics/True Heroics level/toggle、Food Diversity、item数とNBT、legacy raw backupを検証。読込側は初期値再設定なし。所有modifierの10回冪等再構築、foreign modifier保持、同期packet送出も確認した。
- 初回fixture compileは`LazyOptional.orElseThrow`のsupplier不足2件でFAIL（`logs/phase4-compile-20260905.log`）。テストコードを訂正し、`phase4-compile-fixed-20260905.log`でbuild/unit/check PASS。
- `AUTOMATED TESTED / PASS`: write PID11272（07:33:44 JST）、read PID23604（07:34:24）、再read PID23768（07:35:19）。全回`FOODHEALING_RESTART_PASS`、通常停止、Gradle exit 0 / `BUILD SUCCESSFUL`。ログは`logs/phase4-restart-write-20260905.log`、`phase4-restart-read-20260905.log`、`phase4-restart-read2-20260905.log`。稼働中の試験serverなし。
- 最終`build foodHealingUnitTest check runGameTestServer`: **PASS / 35/35**、`logs/phase4-final-20260905.log`。既知開発warningに加え、初回Forge serverconfigのdefault生成warningを確認。ERROR/FATALなし。Jar内にrestart fixtureがないこととrefmap存在を確認した。
- 変更: `build.gradle`、`src/restartTest/java/com/leva/foodhealing/RestartPersistenceVerification.java`、この記録。通常runServerは専用property指定なしなら従来どおり。追加dependency/保存schema変更なし。
- 制限: EmbeddedChannelによるserver-side fixtureであり、実2-player操作・client適用・認証handshake・実v2.2.5 world移行・長時間稼働のPASSではない。OPEN-01換算、True Heroics x64手動分離、外部MOD統合は未確認のまま。runClient未実行。
- 次作業: malformed packet/NBT、反復保存・transaction・modifier負荷、artifact/optional不在監査を続ける。Break Realm/crossbowのgateは維持。

#### 2026-09-05 Phase 3 / Heroics非発動・極端Armor境界

- 現行MixinがHeroics非発動時もvanillaから渡されたArmor/Toughnessを無視し、未取得でも属性上限を迂回していた。非発動/OFF/未取得では元の`CombatRules`入力をそのまま使うよう限定した。
- active時のeffective Armorが100を超え、大damageが入るとvanilla clampのmin（Armor*0.2）がmax（20）を超え、負damage→後続層で0となり得た。vanilla `MAX_ARMOR / ARMOR_PROTECTION_DIVIDER`の吸収上限を結果にも適用し、専用DRをその後に通す。skill倍率/費用、属性所有者、global属性上限は未変更。
- 追加GameTest2件の初期fixtureはGameTest worldがEasyでmob_attackが難易度補正される点を取りこぼしてFAILした。診断ログ`phase3-diagnostic-20260905.log`で実測を確認し、難易度補正のないvanilla playerAttackへ固定した。初期FAILだけをcode不具合の証拠とはしない。
- fixture訂正後、修正前コードを再検証: `phase3-corrected-red-20260905.log`で33 PASS / 2 FAIL。未取得Armor40の100damageはHP932（vanilla期待924）、Lv5 effective Armor480の1000damageはHP400不変（期待300）を確認した。
- 最終`build foodHealingUnitTest check runGameTestServer`: **PASS / 35/35**、`logs/phase3-final-20260905.log`。以前のHeroics/Player死亡dropを含む回帰PASS。変更: `LivingEntityMixin.java`、`FoodHealingCombatGameTests.java`、この記録。
- 既報の手動PASS履歴は維持。今回変更した極端入力とOFF時vanilla境界は`AUTOMATED TESTED`のみで、人間再試験は未実施。True Heroics x64手動分離も未確認。
- 次作業: 隔離した通常dedicated serverの別プロセス永続化fixture。利用者から試験専用環境に限るEULA同意・起動許可を受領済み。その他のworld/server設定には触れない。

#### 2026-09-05 Phase 2 / 購入世代・DR数値境界

- 新規unit FAIL: linear DR 99とtranscendenceの合計が`Long.MAX_VALUE`へ飽和すると、expectedPurchasesが変わらず同じrequestを再購入できた。合計世代が最大のときは既存`NUMERIC_LIMIT`でSP消費前に拒否する。上限手前の最後の購入成功と、その後の全canonical NBT不変を回帰固定した。
- 新規GameTest FAIL: normal/high-difficultyの両transcendenceを306以上にすると、2段目のdouble乗算がゼロへunderflowし、正のdamageが完全無効になった。既存の正値floor方針をdouble underflowにも適用し、明示的な倍率0は0のまま保持する。
- 変更: `FoodHealingBaseStats.java`、`ShokugiDataUnitTest.java`、`FoodHealingCombatGameTests.java`、この記録。SPコスト、各skill倍率、DR段階式は未変更。保存形式/移行変更なし。
- 新規回帰: `validatesSaturatedPurchaseGeneration`、finite/subnormal/極大値の20組の乗算、`extremeIndependentReductionsNeverUnderflowToImmunity`（306/307/1000/Long.MAX_VALUE）。Player死亡dropの既存回帰も再PASS。
- 修正前ログ: `logs/phase2-red-unit-20260905.log`（unit FAIL）、`logs/phase2-red-game-20260905.log`（32 PASS / 1 FAIL）。修正後`build foodHealingUnitTest check runGameTestServer`: **PASS / 33/33**、26秒、`logs/phase2-green-20260905.log`。既知warning以外のERROR/FATALなし。
- runClient/runServer未実行。float Healthの微小damageが画面上で見えるとの主張はしない。外部MOD導入結果/手動結果は未追加。
- 次作業: malformed/legacy NBT、packet、反復保存・極端modifier、別プロセス永続化を検証する。通常dedicatedのEULA同意ファイルがないため利用者確認待ちとし、同意不要の独立検証を続ける。

#### 2026-09-05 Phase 1 / 食事控除の観測境界

- 再現FAIL: 食事でFood Level 10→14、次のsnapshotまでに11へ低下すると、未照合credit 3が残り、後の独立した+2 Food Levelの回復まで抑止された。新規GameTestだけFAIL（31 PASS / 1 FAIL、`logs/phase1-red-20260905.log`）。
- 原因: creditは完了済みのFood Level変化なのに、部分観測後も将来のgainに使える予約として残っていた。次snapshotで正/ゼロ/負deltaのいずれでも使い切り、後の観測へ持ち越さない最小修正を行った。Nutrition、SP、Root、Satisfactionの確定値は未変更。
- Forge 47.2.0実sourceの`LivingEntity.releaseUsingItem`は両logical sideでStop eventを発火する。共有static mapをclient Stopが消せる経路をserver-side guardで閉じた（静的監査による修正、実client競合再現PASSとはしない）。
- 変更: `FoodHealingTransactions.java`、`FoodHealingHandler.java`、`FoodHealingPriorityZeroGameTests.java`、`ShokugiDataUnitTest.java`、この記録。新規回帰`foodHungerCreditCannotSuppressLaterIndependentGains`はsnapshot 11/10/9の各境界を確認する。
- `build foodHealingUnitTest check runGameTestServer`: **PASS / GameTest 32/32**、27秒。`logs/phase1-green-20260905.log`。ERROR/FATALなし。既知開発環境warningはbaselineと同系統。runClient/runServer未実行、外部MOD統合/手動結果は未追加。
- 次作業: long購入世代・多重DR underflowを再現し、numeric safetyに限定して修正する。OPENおよび実MOD依存の未確認は維持。

#### 2026-09-05 自律安定化 baseline / 再監査フェーズ

- AGENTS、CODEX_LOOP_PROMPT、Status全文、docs配下の既存仕様・監査・試験計画と現コードを再確認した。過去の手動PASSは維持し、crossbow候補の監査・実装は開始しない。
- `.git`なし。非破壊backup: `backups/20260905-070927-baseline/project-source.zip`（src/docs/Gradle/root設定、SHA-256 `2C5CDBFC43570EF1378507A1DEE98F679A7CA95F23F756C3B4AA18739602B14F`）。開発world/saves/configも別の`development-worlds.zip`へ保存（SHA-256 `6BFF4E2830F0515F065247875C7AE5DE4EA4C21BE1C0B84092488309BA6F76DB`）。生成cache/buildおよび外部MOD参考資料はソースbackupへ含めない。
- 環境: Windows 11 amd64、Adoptium Java 17.0.7、Gradle 8.1.1、Forge 47.2.0。最初のsandbox内wrapperはdistribution download権限制約で失敗。同一codeを既存user cache環境で再実行して成功し、code FAILとは区別した。
- 変更前`build foodHealingUnitTest check --console=plain`: **PASS**（20秒）。`runGameTestServer`: **31/31 PASS**（22秒）。ログ: `logs/baseline-20260905-build.log`、`logs/baseline-20260905-gametest.log`。
- 警告を分類: Gradle9向けdeprecation、非対話terminal、Forge language providerのmods.tomlなし、開発時refmap warning、union resource URL。必須Mixinは適用され、ERROR/FATALなし。成果物refmapは後続artifact監査で別途確認する。warningを抑止する設定変更はしない。
- 外部環境: `run/mods`は空。`libs/tacz-1.20.1.jar`と既存`tmp_monolith`参考資料は存在するが、version/内容監査も実導入もこのbaselineでは行っていない。外部互換PASSにはしない。
- 変更ファイル: この記録のみ（加えて非破壊backupと実行ログ）。runClient/runServerは未実行。manual結果は未追加。
- 次作業: P0食事控除の観測区間境界・client/server状態分離、P1購入世代のlong境界、永続化/再起動fixture、数値/packet/resource/optional不在監査。OPEN依存・実client・実MOD必要項目は独立して保留する。

#### 2026-09-03 試作型機関弩5SP版の将来仕様書受領・保留登録

- **DEFERRED / NOT IMPLEMENTED / WAITING FOR USER START APPROVAL**: 「試作型機関弩」5SP版を含むクロスボウ改修の将来実装用仕様書を受領した。参照元の実ファイルは[Food_Healing_RPG_Codex_Crossbow_Spec.md](<LOCAL_DOWNLOADS>/Food_Healing_RPG_Codex_Crossbow_Spec.md)（文書版1.2、2026-09-03）。依頼文中の末尾`(1)`付きファイル名ではなく、添付として確認できたこの実ファイルを参照する。
- 利用者の2026-09-03最新指示が添付MD第0章の実装開始指示に優先する。現在のv3.0.0のデバッグ・安定化・回帰確認・必要な手動試験がすべて完了し、かつ利用者が後日、明示的に「試作型機関弩の実装を開始してください」と指示するまで、監査・実装フェーズへ進まない。デバッグ完了だけでは自動着手しない。
- 開始条件を満たした時点で初めてMD第3章「実装前の現状監査」から開始し、第16章の段階0→1→2…の順に、一段階ずつbuild・自動試験・実クライアント試験を挟む。今回の受領は改訂案や未確定事項の採用・LOCKを意味しない。
- 現行の確定仕様、release blocker、手動試験、「次の作業」の内容と優先順位を変更しない。クロスボウ機能を現在のデバッグへ混ぜず、v3.0.0のリリース候補判定や収録を先行決定しない。`OPEN` / `NOT LOCKED`も未変更。
- 変更ファイル: `docs/CODEX_STATUS.md`のみ（受領・保留の記録）。Java、Mixin、resource、Config、dependency、build設定、GameTest、既存テスト、仕様本文、添付MDは変更していない。Build/Test: 文書記録のみのため実行せず、既存の自動・手動・統合試験結果を維持する。

#### 2026-09-01 通常Heroics Armor/Toughness Lv1〜5実クライアント数値手動PASSフェーズ

- 試験source切り分け: 最初の`minecraft:generic` 100 damageはArmor 5 / Toughness 1でも100がそのまま通った。これはHeroics FAILではなく、Armor/Toughness数値確認に不適切なsourceのため`INVALID / TEST SOURCE UNSUITABLE`とする。以後、HP 40%調整には`minecraft:generic`、Armor/Toughnessの実damage確認には`minecraft:mob_attack`を使用した。
- OFF基準: 最大HP 1000、現在HP 400、Armor 20、Toughness 2、Heroics未取得で`minecraft:mob_attack` 100 damageを与え、HP 400.0→316.0、実damage 84.0を確認した。Armor/Toughnessがvanilla damage pipelineで有効な基準を確立した。
- `MANUAL NUMERIC TESTED / PASS`: Heroics Lv1。base Armor/Toughness 10/10、仕様倍x2のeffective 20/20、専用DR 10%、`minecraft:mob_attack` 20でHP 400.0→394.34286となり、理論値と一致した。
- `MANUAL NUMERIC TESTED / PASS`: Heroics Lv2。base 4/4、仕様倍x4のeffective 16/16、専用DR 20%、input 20でHP 400.0→392.10666となり、理論値約392.1067と一致した。
- `MANUAL NUMERIC TESTED / PASS`: Heroics Lv3。base 2/2、仕様倍x8のeffective 16/16、専用DR 30%、input 20でHP 400.0→393.09332となり、理論値約393.0933と一致した。
- `MANUAL NUMERIC TESTED / PASS`: Heroics Lv4。base 1/1、仕様倍x16のeffective 16/16、専用DR 40%、input 20でHP 400.0→394.08となり、理論値約394.08と一致した。
- `MANUAL NUMERIC TESTED / PASS`: Heroics Lv5。base 0.5/0.5、仕様倍x32のeffective 16/16、専用DR 50%、input 20でHP 400.0→395.06668となり、理論値約395.0667と一致した。
- HUDの393.09332→393.1、395.06668→395.1は小数第1位表示への丸めであり、server healthとの同期不良とは扱わない。
- 実装はvanilla attribute表示値自体を倍率値へ書き換える方式ではなく、damage calculation時の`CombatRules`経路へFood Healing側のeffective Armor/Toughnessを渡す。そのため`/attribute ... get`ではなく、今回の実damage結果を数値判定根拠とする。
- 変更ファイル: `docs/CODEX_STATUS.md`のみ。Java、Mixin、resource、Config、GameTest、dependency、仕様書、`OPEN` / `NOT LOCKED`、Break Realm、外部MOD Adapterは変更していない。
- Build/Test: 手動結果の記録のみのため追加実行していない。直前のbuild/check/unit成功とForge GameTest 31/31を維持する。
- `./gradlew.bat runClient`: 2026-09-01 20:55 JSTに起動した手動試験用clientは、21:52 JSTにMinecraftの通常停止処理を完了した。Gradleも`BUILD SUCCESSFUL in 57m 6s`で正常終了し、現在Minecraft clientと`runClient`は稼働していない。

#### 2026-08-31 Food Production Mastery vanilla追加手動PASSフェーズ

- `MANUAL TESTED / PASS`: Food Production Mastery ONの3x3作業台で小麦3個から結果欄にパン2個が表示され、通常左クリックでパン2個を取得し、小麦は3個だけ消費した。
- `MANUAL TESTED / PASS`: 3x3作業台の小麦4+4+4個をShift+左クリックし、4 recipe分の小麦12個を消費してパン8個を取得した。
- `MANUAL TESTED / PASS`: Player Inventoryの2x2 craftingでカボチ1、砂糖1、卵1からパンプキンパイ2個が結果欄に表示され、通常左クリックで2個を取得し、各材料は1個ずつ消費した。
- `MANUAL TESTED / PASS`: skill取得済みでtoggle OFFの場合、小麦3→パンの結果欄と取得量は1個、材料消費は小麦3個の通常結果だった。
- `MANUAL TESTED / PASS`: 新規test worldでskill未取得の場合、小麦3→パンの結果欄と取得量は1個、材料消費は小麦3個の通常結果だった。
- `MANUAL TESTED / PASS`: Food Production Mastery ONでクッキー60個stackを保持し、小麦2+カカオ豆1のrecipe結果欄に16個が表示された。Shift+左クリック後は既存stack 60→64、別の空slot 12、総数76個となり、64上限超過、overflow消失、二重増殖は発生しなかった。
- 以上により、今回予定していたnormal click、Shift-click、3x3、2x2、toggle OFF、未取得、正しい材料消費、Stack上限/overflowのvanilla追加手動試験は完了。
- 外部MODは未導入。Farmer's Delight、addon recipe、独自container/menu、multiplayer共有machineは`NOT YET INTEGRATION TESTED`のままで、今回の手動PASSに含めない。
- 変更ファイル: `docs/CODEX_STATUS.md`のみ。Java、Mixin、resource、Config、GameTest、仕様書は変更していない。
- Build/Test: 手動結果の記録のみのため追加実行していない。直前のbuild/check/unit成功とForge GameTest 31/31を維持する。
- `./gradlew.bat runClient`: 2026-08-31 22:36 JSTに起動したFood Production Mastery手動試験用clientは、試験完了後の23:20 JSTにMinecraftの通常停止処理を完了した。Gradleも`BUILD SUCCESSFUL in 44m 35s`で正常終了し、現在Minecraft clientと`runClient`は稼働していない。

#### 2026-08-31 HP HUD追加vanilla手動PASSフェーズ

- `MANUAL TESTED / PASS`: Chat設定を「非表示」にしても、数値HP HUD、食義Lv/count、Heroics発動表示が正常に表示された。
- `MANUAL TESTED / PASS`: `T`でchat入力画面を開いた状態でも、HP HUDがchatより前景に表示され、半透明背景を維持しつつ`HP: current / max`を明確に読めた。
- `MANUAL TESTED / PASS`: `/`でcommand入力画面を開いた状態でもHP HUDを正常に読め、位置の大きな移動やchat背景への埋没はなかった。
- `MANUAL TESTED / PASS`: GUI ScaleをAuto、1、2、3、4の順に変更し、全ScaleでHP HUDが画面内に収まり、文字/背景の欠け、異常な位置ずれ、HUD消失がないことを確認した。食義Lv/countとHeroics表示も正常にscaleした。
- 既報の通常chatログおよび複数行chat表示中のPASSと合わせ、HP HUDのvanilla追加手動試験は完了とする。修正前のchat重なりFAILは解決履歴として保持する。
- 変更ファイル: `docs/CODEX_STATUS.md`のみ。Java、Mixin、resource、Config、GameTest、仕様は変更していない。
- Build/Test: 手動結果の記録のみのため再実行していない。直前のbuild/check/unit成功とForge GameTest 31/31を維持する。
- `./gradlew.bat runClient`: HUD手動試験に使用したclientは2026-08-31 22:31 JSTに正常終了し、Gradleも`BUILD SUCCESSFUL in 18m 27s`で完了した。Food Production Mastery手動試験用に22:36 JSTに再起動したclientも、上記Food Productionフェーズのとおり23:20 JSTに正常終了済み。

#### 2026-08-31 GUI entry point Creative Inventory修正・回帰フェーズ

- `MANUAL TESTED / PASS`: Creative以外の通常game modeで`E`→通常player inventory→デフォルト`S`によりFood Healing RPG GUIが正常に開いた。
- `MANUAL TESTED / PASS`: Blast Furnace、Smoker、Grindstone、Smithing Table、Lectern、Barrel、Hopper、Dropper、Dispenser、Chest、Furnace、Crafting Tableで`S`を押してもFood Healing GUIが誤って開かないことを実クライアントで確認した。
- `MANUAL FAILED / NEEDS FIX`として受領: Creative Modeのplayer inventoryでは同じ`S`キーでFood Healing GUIが開かなかった。これは修正前clientの観察事実として保持し、下記再試験により解決済みとする。
- 原因監査: Minecraft / Forge 1.20.1の実classでは`InventoryScreen` と`CreativeModeInventoryScreen`がいずれも`EffectRenderingInventoryScreen`を個別に継承する兄弟classだった。旧コードは`instanceof InventoryScreen`だけを許可し、Creativeだけを対象外にしていた。
- 修正: `FoodHealingGuiEntryPolicy`を追加し、`InventoryScreen`または`CreativeModeInventoryScreen`の派生classだけを明示許可するようにした。汎用`AbstractContainerScreen`やMenu所有画面の一括許可には変更していない。
- 新規回帰: `FoodHealingGuiEntryPolicyRegression`で通常InventoryとCreative Inventoryをallowed、手動対象containerに対応するvanilla実Screen classをdeniedとして固定した。client-only入力の実動作も下記の手動再試験で確認済み。
- 変更ファイル: `ClientScreenEvents.java`、`FoodHealingGuiEntryPolicy.java`、`FoodHealingGuiEntryPolicyRegression.java`、`ShokugiDataUnitTest.java`、`docs/CODEX_STATUS.md`。Javaはclient GUI entryとその回帰だけで、Mixin、resource、Config、GameTest、他仕様は変更していない。
- Build/Test: 変更前`./gradlew.bat build`成功。修正後`./gradlew.bat foodHealingUnitTest`成功、`./gradlew.bat build`成功、`./gradlew.bat check`成功、`./gradlew.bat runGameTestServer`は`All 31 required tests passed`で成功。最初のsandbox内baseline実行はGradle distributionの外部download禁止で失敗したが、同一codeを既存user cache環境で再実行し成功している。
- セーブ/移行影響: なし。server canonical data、network、skill、damage、crafting、HUDは変更していない。optional MOD統合状態も更新しない。
- `MANUAL TESTED / PASS`: 修正後clientでCreative Mode→Creative Inventory→デフォルト`S`によりFood Healing RPG GUIが正常に開くことを実クライアントで確認した。これによりCreative entryの修正前FAILは解決済み。
- GUI entry point最終手動結果: 通常player Inventory + `S`、Creative Inventory + `S`、および指定12種containerでの誤起動なしの三境界をすべて`MANUAL TESTED / PASS`とする。
- `./gradlew.bat runClient`: 2026-08-31 22:02 JSTに修正後開発clientを起動し、上記手動再試験を実施。22:09 JSTにMinecraftは正常終了し、Gradleも`BUILD SUCCESSFUL in 6m 19s`で完了した。crash/fatal errorはない。
- `./gradlew.bat runClient`: 次の手動試験継続用として2026-08-31 22:13 JSTに再起動した。Food Healing client setup、resource reload、OpenAL、texture atlas初期化まで正常完了し、clientは現在稼働中で人間操作待ち。
- Build/Test: 今回は手動結果の記録のみで、Java、Mixin、resource、Config、GameTest、仕様を変更せず、自動テストも再実行していない。直前のbuild/check/unit成功とGameTest 31/31を維持する。

#### 2026-08-30 通常Heroics Lv1〜5数値手動PASSフェーズ

- `MANUAL TESTED / PASS`: 新規test worldでHP 1000のCow、無enchant鉄の剣、OFF時基準damage 6の同一条件を使用し、Heroics Lv1〜5 ON時のoutgoing damageが順に12 / 15 / 18 / 24 / 30となることを確認した。仕様値x2.0 / x2.5 / x3.0 / x4.0 / x5.0と全て一致した。
- `MANUAL TESTED / PASS`: Lv1→Lv5の実測が12→15→18→24→30となり、前Lvとの累積乗算ではなく、現在Lvのfinal effectだけが適用されることを確認した。
- `MANUAL TESTED / PASS`: Armor 0、防具なし、Root系OFF、自然回復OFF、他防御効果なし、HP 8.0/20.0で`/damage @s 5 minecraft:generic`を使用した。OFF時は各回8.0→3.0、ON時はLv1〜5の順に8.0→3.5 / 4.0 / 4.5 / 5.0 / 5.5となり、実damage 4.5 / 4.0 / 3.5 / 3.0 / 2.5、専用DR 10% / 20% / 30% / 40% / 50%と全て一致した。
- `MANUAL TESTED / PASS`: toggle OFF時にdedicated DRが解除され、damage 5.0の通常結果に戻ることを確認した。
- 手動確認範囲はoutgoing倍率と専用DRに限定する。Armor/ToughnessはLv1〜4の個別手動PASSへ推測更新せず、既報のLv5 x32手動PASSとLv1〜5 GameTestの`AUTOMATED TESTED / PASS`を分離して維持する。
- `./gradlew.bat runClient`: 2026-08-30 12:56 JSTに起動し、上記手動試験後の13:46 JSTにMinecraftが正常終了した。Gradleも`BUILD SUCCESSFUL in 49m 50s`で完了し、crash/fatal errorはない。現在clientは動作していない。
- 変更ファイル: `docs/CODEX_STATUS.md`のみ。Java、Mixin、resource、Config、GameTest、仕様書は変更していない。
- Build/Test: 手動結果の記録のみのため、別途`build`、`foodHealingUnitTest`、`check`、`runGameTestServer`は再実行していない。直前の各成功とForge GameTest 31/31を書き換えない。
- 次の作業: 通常Inventoryのデフォルト`S`キー、HP HUDのchat非表示・`T`入力・`/`入力・GUI Scale変更、その後Food Productionの追加操作を手動確認する。

#### 2026-08-29 Break Realm Adapter expansion文書化フェーズ

- 既存確定スキルBreak Realmを、Playerの正当な最終damageを対象MOD所有の防御gateへ通すスキルとして詳細化した。instant kill、damage増幅、Player適用、強制削除、通常death/loot/XP/advancement迂回は禁止のまま維持した。
- `BreakRealmAdapter`相当のoptional-safe interface/registry、所有mechanismの限定解除、try/finally相当の復元、source/attacker/target/original/legitimate final amount保持、最大1回fallback、recursion guard、Pursuitとhurt iframeの分離、exactly-onceを仕様化した。
- 正式な対応対象としてL2 Hostility、Trial Monolith、Hyperlink/Fumetsu Wither、Draconic Evolution/Chaos Guardian、Bloodbath/Bloodbath Godzilla、公式MODpack採用の都市伝説・高難度bossを記録した。各versionのJar/source/API監査前はclass、registry ID、trait、NBT、capability、ProcedureをLOCKしない。
- BloodbathはMCreator生成経路の可能性を前提に、entity class、tick/hurt/death Procedure、heal、invulnerability reset、death cancel、revival/respawn、capability/NBTをreference-only Jar監査してから専用Adapterを設計する方針とした。Jarのcopy/bundle/redistributionは禁止。
- Break Realmは「このPlayerのlegitimate damageを通す」、Truth Masteryは「対応trait/affixを恒久nullifyする」と境界を明記した。
- unsupported targetは通常damageへ戻し、危険なgeneric killへfallbackしない。rate-limit diagnosticsはmod/entity ID、source、result、HP before/after、観測可能なcancel情報だけを記録し、combat stateを変更しない。
- generic GameTest fixtureと、外部MODごとの`STATIC AUDITED`→`AUTOMATED INTEGRATION TESTED`→`MANUAL INTEGRATION TESTED`の分離基準を`TEST_PLAN.md`へ追加した。現時点ではいずれの対象MODも新たに監査・統合試験していない。
- 状態: **BREAK REALM ADAPTER EXPANSION SPECIFIED / NOT IMPLEMENTED**。現在のHeroics Lv1〜4、HUD/Sキー、Food Production追加手動試験を中断せず、完了後に実装フェーズを検討する。
- 変更ファイル: `docs/SKILL_TREE_SPEC.md`、`docs/SPEC.md`、`docs/COMPATIBILITY_POLICY.md`、`docs/TEST_PLAN.md`、`docs/CODEX_STATUS.md`。
- 実装変更: **なし**。Java、Mixin、dependency、resource、Config、GameTest codeを変更していない。
- Build/Test: 文書化のみのため再実行していない。直前の`build`、`foodHealingUnitTest`、`check`成功およびForge GameTest 31/31成功を書き換えない。
- 次の作業: 文末の既存3項目を同じ順序で維持し、vanilla手動試験を優先する。

#### 2026-08-28 True Heroics・再ログイン保持手動PASSフェーズ

- `MANUAL TESTED / PASS`: Heroics Lv5前提、必要100 SP表示、購入時の未使用SP 459→359、使用済みSP 41→141、True Heroics Lv1/1取得表示、ON/OFF切替を実クライアントで確認した。
- `MANUAL TESTED / PASS`: HP約71.5%でTrue Heroics OFF時は非発動、同じHP割合でON時は発動した。server canonical toggleと80%閾値に沿った実効果切替を確認した。
- `MANUAL TESTED / PASS`: 通常2.0 damageの窒息がTrue Heroics ON時に約0.02 damageとなり、Heroics専用DR 99%を確認した。
- `MANUAL TESTED / PASS`: 基礎Armor 5、防具なし、Normal、通常Creeperほぼゼロ距離爆発でOFF時は死亡し、ON時はHP約14→13.9で生存した。True Heroicsの実防御効果はPASSだが、DR 99%と同時発動するためArmor/Toughness x64単独倍率の厳密な手動確認とは扱わない。
- `MANUAL TESTED / PASS`: 鉄の剣で同条件のCowを攻撃し、OFF時1000→994の6 damage、ON時1000→880の120 damageを確認した。120÷6=20によりoutgoing damage x20を手動数値確認した。
- `MANUAL TESTED / PASS`: Root / True Root OFF、True Heroics ONで`/kill`を実行し正常に死亡した。True Heroics単独ではdirect-death protectionを持たない。
- `AUTOMATED TESTED / PASS - MANUAL ISOLATION NOT YET TESTED`: True Heroics Armor/Toughness x64は既存GameTestで確認済み。実クライアントのCreeper試験は99% DRとの合成結果であり、x64単独値の手動検証済みとはしない。
- `MANUAL TESTED / PASS`: タイトル画面へ戻って同じworldへ再ログインし、未使用SP 358、使用済みSP 142、Root Lv5/5 OFF、True Root Lv1/1 OFF、Heroics Lv5/5 OFF、True Heroics Lv1/1 OFFがすべて保持された。管理commandで用意した未使用SPを購入後に残した値、使用済みSP、両上位skillの取得状態とtoggleの永続化を確認した。
- `./gradlew.bat runClient`: 2026-08-28 21:02 JSTに起動成功。手動試験と再ログイン確認後の22:19 JSTにMinecraftを正常終了し、Gradleも`BUILD SUCCESSFUL in 1h 17m 5s`で完了した。crash/fatal errorはない。
- 変更ファイル: `docs/CODEX_STATUS.md`。このフェーズではコード、仕様、resource、GameTestを変更していない。
- Build/Test: 手動結果の記録のみのため`build`、`foodHealingUnitTest`、`check`、`runGameTestServer`は再実行していない。直前の各成功およびForge GameTest 31/31成功を維持する。
- 次の作業: Heroics Lv1〜4の個別実効値、通常Inventoryのデフォルト`S`キー、HP HUDの`T`/`/`入力中とGUI Scale変更、Food Productionの追加操作をvanilla実クライアントで確認する。

<a id="evidence-root-toggle"></a>

#### 2026-08-27 True Root toggle切り分け手動PASSフェーズ

- `MANUAL TESTED / PASS`: Root Lv5 ON / True Root OFFでNutrition合計18により通常Rootを発動し、その15秒中にさらにNutrition合計18を取得してもTrue Root固有予約は作成されず、終了後に自動再発動しないことを実クライアントで確認した。
- 既報のRoot OFF / True Root ON時のRoot非発動、およびRoot ON / True Root ON時のactive中Nutrition 18の1回予約・15秒終了直後の1回再発動と合わせ、通常Rootとの依存関係、True Root OFF境界、True Root ON時の確定動作を`MANUAL TESTED / PASS`とした。
- 前回の「True Root OFFでも発動した」という報告は、通常Root Lv5がONで両効果を切り分けられていなかったため、`INVALID / AMBIGUOUS TEST - Root ON状態との切り分け不足`の履歴を維持する。確定不具合や修正前コードの故障根拠にはしない。
- 変更ファイル: `docs/CODEX_STATUS.md`。このフェーズではコード、仕様、resource、自動テストを変更していない。
- Build/Test: 手動結果の記録のみのため再実行していない。直前の`build`、`foodHealingUnitTest`、`check`成功およびForge GameTest 31/31成功を維持する。
- 次の作業: vanilla clientを再起動し、True Heroicsの購入、SP、toggle OFF/ON、確定効果を手動確認する。

#### 2026-08-27 True Root toggle手動結果訂正フェーズ

- 前回の「True Root OFFでも効果が発動した」という結果は、通常Root Lv5がONのままで通常Rootの発動とTrue Root固有予約の発動を切り分けられていなかったため、`INVALID / AMBIGUOUS TEST - Root ON状態との切り分け不足`へ訂正した。確定したtoggle不具合や修正前コードの故障根拠として扱わない。
- `MANUAL TESTED / PASS`: Root OFF / True Root ONではRoot効果自体が発動しないことを実クライアントで確認した。True Rootは無効な通常Rootを無視して単独発動していない。
- 上記PASSは親となる通常RootのOFF境界だけを対象とする。Root ON / True Root OFFでactive中Nutrition 18が予約されず、終了後に再発動しないことは未確認であり、True Root固有予約機能のtoggle切り分けは継続する。
- 既存のtoggle横断監査、canonical enabled判定の統一、True Root / True Heroicsを含むGameTest 31/31は、不具合修正済みの証明ではなく回帰防止のための判定統一・テスト強化として維持する。
- 変更ファイル: `docs/CODEX_STATUS.md`。この訂正フェーズではJava、resource、GameTestを変更していない。
- Build/Test: 文書訂正のみのため再実行していない。直前の`build`、`foodHealingUnitTest`、`check`成功およびForge GameTest 31/31成功を維持する。
- 手動試験に使用した`runClient`は2026-08-27 19:50 JSTに正常終了し、Gradleも`BUILD SUCCESSFUL`で完了した。crash/fatal errorはない。
- 次の作業: 文末の手順1、2の順で、Root ON / True Root OFF時の予約なしと、Root ON / True Root ON時の1回予約・1回再発動を切り分けて確認する。

#### 2026-08-27 True Root toggle再試験用client起動フェーズ

- `./gradlew.bat runClient`でtoggle横断監査・判定統一後のForge 1.20.1開発クライアントを起動した。Food Healing client setup、resource reload、OpenAL、sound engine、texture atlas初期化まで正常に到達し、fatal error/crashはない。
- 起動後は人間の手動操作に使用し、2026-08-27 19:50 JSTに正常終了した。Minecraft/Gradleプロセスは現在動作していない。
- 起動成功だけを根拠にTrue RootまたはTrue Heroicsを手動PASSへ変更していない。True Rootの評価は、その後受領した切り分け結果に基づいて上記訂正フェーズで更新した。
- 変更ファイル: `docs/CODEX_STATUS.md`。この起動フェーズではゲーム本体の追加変更なし。
- 直前結果: `build`、`foodHealingUnitTest`、`check`は成功、Forge GameTestは31/31成功。
- 次の作業: 文末の手順1、2に従い、True Root OFF時の予約なし・再発動なしと、ONへ戻した後の1回予約・1回再発動を実クライアントで確認する。

#### 2026-08-27 skill toggle横断監査・判定統一・回帰強化フェーズ

- `MANUAL TESTED / PASS`: `/foodhealing syokugi setskillpoint 500`、`addskillpoint 25`による525、`setskillpoint 0`、負数のBrigadier拒否、GUI即時反映を実クライアントで確認した。再ログイン保持は別項目として未確認のまま残す。
- `MANUAL TESTED / PASS`: Root Lv5前提、True Root 20 SP購入、未使用SP 20減少、取得表示、GUI上のON/OFF操作を確認した。
- `MANUAL TESTED / PASS`: True Root ON時の「active中にNutrition 18を1回分予約し、現在window終了直後に1回だけ再発動」ロジックを確認した。
- 当時の「True Root OFFでも効果が発動した」という報告は、その後の追加情報により通常Root Lv5がONの曖昧な試験だったと判明した。現在は`INVALID / AMBIGUOUS TEST - Root ON状態との切り分け不足`であり、この報告から修正前コードのtoggle不良を断定しない。
- 効果処理を横断監査し、取得levelとdisabledを個別に組み合わせていた箇所をserver canonicalな`FoodHealingSkills.getEnabledLevel`へ統一した。これは予防的な判定統一として扱う。Root、True Root、Heroics、True Heroics、Satisfaction、Fire Resistance、Water/Night Vision、Fast Eating、Acrobatics、Flame Blessing、Explosion Resistance、Gathering、Unbreaking、Armor Mastery、Pursuit、Food Production、Slaughter、Purification、Kongo、Flightの効果入口を確認した。
- `ToggleSkillPacket`は受信したserver playerのcanonical capabilityだけを更新し、clientへ同期した後、network contextを明示的にhandledとするよう整理した。
- 新規GameTest `trueRootToggleControlsReservationWithoutDisablingNormalRoot`を追加した。通常Root OFF時の非発動、True Root取得済み+OFF時の新規予約なし・終了時再発動なし、通常Root Lv5維持、OFF→ON後の予約と1回再発動、再度OFF時の新規予約なしを検証する。
- 既存GameTestへTrue Heroics取得済み+OFF時のoutgoing x20、Armor/Toughness x64、専用DR 99%の全無効化を追加した。Gathering、Slaughter、Armor Mastery、Explosion Resistance、Purification、Kongo、Flightの不足していたOFF境界も追加した。既存のSatisfaction、Fire Resistance、Fast Eating、Acrobatics、Flame Blessing、Food Production、Unbreaking、Pursuit、normal HeroicsのOFF回帰は維持している。
- TaCZ Ammo Conservationは未導入環境で購入不可・SP非消費を維持し、TaCZ固有classや実機能をこの監査のためにロードしていない。TaCZ導入時は引き続き`NOT YET INTEGRATION TESTED`。
- 仕様確認事項: True Root ON中に予約済みNutritionを作成した後、active window途中でOFFにした場合、その既存予約を破棄するか再ONまで保持するかは仕様書に定義がない。今回はどちらにもLOCKせず、OFF中の新規予約禁止・OFFのまま終了した場合の再発動禁止・通常Root維持だけを回帰条件として固定した。
- 変更ファイル: `FoodHealingSkills.java`、`RootController.java`、`DamageEventHandler.java`、`DurabilityTransactions.java`、`SatisfactionTransactions.java`、`GatheringLootModifier.java`、`ToggleSkillPacket.java`、`FoodHealingCombatGameTests.java`、`FoodHealingExistingSkillsGameTests.java`、`FoodHealingPriorityZeroGameTests.java`、`docs/TEST_PLAN.md`、`docs/CODEX_STATUS.md`。
- `./gradlew.bat build`: **成功**。`./gradlew.bat foodHealingUnitTest`: **成功**。`./gradlew.bat check`: **成功**。
- `./gradlew.bat runGameTestServer`: **31/31成功**。`All 31 required tests passed`を確認した。
- 次の作業: 更新済みvanilla clientで通常RootとTrue Rootのtoggleを明示的に切り分けて再試験する。その結果受領までTrue Heroicsの人間側試験は保留する。

#### 2026-08-27 安定化完了後の追加仕様候補・Pam's Trees Jar静的監査フェーズ

- `SPEC.md`へ、現在の安定化・vanilla手動検証・Priority 0・データ安全性・既存回帰・optional統合確認の完了後にだけ再検討する`NOT LOCKED / NOT IMPLEMENTED`候補節を追加した。現行LOCKED仕様を上書きしない。
- 候補1として「Pam's HarvestCraft 2 - Trees樹木収穫物1個＋標準tagの原木1個→同収穫物2個」のShapeless Crafting案を記録した。対象whitelist、原木tag、paper/string/appleのprovenance、target別JSONかcustom serializerかは未決定のまま残した。
- 参考`pamhc2trees-1.20-1.0.2.jar`（SHA-256 `69E0C722C786B78AEA299E7FAF27991E6533A671A66F281FB5CED279EB31B0DD`）を読み取り専用で静的監査した。MOD IDは`pamhc2trees`、成熟tree block→直接収穫物は50対応、`forge:fruits`は29件、`forge:nuts`は9件、`forge:crops`は47件で、aggregate tag単独では安全かつ完全に対象を特定できない。
- Jar上の例外として、apple treeは`minecraft:apple`、paperbarkは`minecraft:paper`、spiderwebは`minecraft:string`を成熟lootとして返し、mapleの実registry IDは`pamhc2trees:maplesyrupitem`である。古いlocalization/asset名からregistry IDを推測しない注意を記録した。
- `COMPATIBILITY_POLICY.md`へPam's Treesをoptionalのまま扱う将来候補を追記した。未導入時resource-load安全性、直接class参照禁止、mod-loaded条件、専用whitelist候補、version別再監査、Jar非同梱を明記した。
- 候補2として、食事はdeclared Nutrition、非食事Food Level増加はactual positive deltaを食義countへexactly onceで加算する案を`SPEC.md`へ記録した。満腹時食事、Satisfaction成功、将来のauto-feed、saturation-only除外、generic増加をRootへ流さない境界も記録した。
- 新count方式の初期balance候補を`1000 count = 食義Lv+1 = SP+1`としたが、明確に`NOT LOCKED`とし、500-750/1000/1500/2000等を実modpack playtest後に判断する。現行の200 countとConfigは変更していない。
- 変更ファイル: `docs/SPEC.md`、`docs/COMPATIBILITY_POLICY.md`、`docs/CODEX_STATUS.md`。
- 実装変更: **なし**。Java、Mixin、recipe JSON、dependency、Config、Shokugi transaction、Root/True Root、Satisfaction、Food Healing、backpack compat、配布物は変更していない。参考Jarの内容をrepoへコピー・同梱していない。
- Build/Test: 文書と読み取り専用監査のみのため、このフェーズではGradle build/testを実行していない。直前の確定結果（build、`foodHealingUnitTest`、`check`成功、GameTest 30/30成功）を書き換えない。
- Optional compatibility: Pam's Treesは**STATIC REFERENCE AUDITED / NOT INTEGRATION TESTED**。MODを導入したclient/server/recipe/Botany Pots試験は未実施であり、互換性確認済みとは扱わない。
- `OPEN-01`〜`OPEN-05`は未変更。追加候補を理由に新しいOPENを推測決定していない。
- 次の作業: 文末の既存5項目を同じ文面・順序で維持し、skillpoint commandとTrue Root / True Heroicsを含む現在のvanilla手動試験を優先する。

#### 2026-08-25 skillpointコマンドGameTest・完了記録フェーズ

- `./gradlew.bat runGameTestServer`: **30/30成功**。新規`skillPointAdminCommandsAreAtomicScopedAndSynced`を含め、`All 30 required tests passed`を確認した。
- 新規回帰は実server command dispatcherと接続済み`ServerPlayer`を使用し、set 500/0、add 500、permission level 2、負数・不正文字列・long範囲外拒否、`Long.MAX_VALUE + 1`のatomic overflow拒否、非対象canonical dataとFood Diversityの不変、`setlevel`のSP非連動、成功時のserver→client packet送信を検証した。
- commandが更新する`UnspentSkillPoints`は既存のcanonical NBT serialize/deserializeおよびPlayer NBT round-trip対象であり、command専用の一時NBTやclient-only stateは追加していない。
- 最終結果: `./gradlew.bat build`、`./gradlew.bat foodHealingUnitTest`、`./gradlew.bat check`、`./gradlew.bat runGameTestServer`はいずれも**成功**。初回buildの新規test fixture定数名不一致は修正済み。
- 変更ファイル: `FoodHealingCommands.java`、`FoodHealingCommandGameTests.java`、`en_us.json`、`ja_jp.json`、`docs/CODEX_STATUS.md`。
- 成果物: `build/libs/foodhealing-3.0.0.jar`、SHA-256 `B7241D7F5DBDBE860976B1EB3513BCE91B3B8671EC7AE0626992F6BC86A54170`。
- `NOT YET TESTED / NEEDS MANUAL TEST`: 新規commandの実クライアントGUI即時反映・再ログイン保持、およびTrue Root / True HeroicsのGUI購入と実効果。自動結果だけでは手動PASSへ変更しない。
- 次の作業: 下記「次の作業」の手順でvanilla実クライアントを手動確認し、結果を`MANUAL TESTED`または`MANUAL FAILED`として受領する。

#### 2026-08-25 skillpointコマンド単体・check検証フェーズ

- `./gradlew.bat foodHealingUnitTest`: **成功**。
- `./gradlew.bat check`: **成功**。`check`が依存する`foodHealingUnitTest`も再度成功した。
- 変更ファイル: `docs/CODEX_STATUS.md`。この検証フェーズでゲーム本体の追加変更なし。
- 未解決: 実サーバーcommand dispatcher、permission、接続playerへのsync packetを含む新規GameTestは次フェーズで実行する。True Root / True Heroicsは手動試験待ちを維持する。
- 次の作業: `./gradlew.bat runGameTestServer`を実行し、新規command回帰を含む全GameTest結果を確認する。

#### 2026-08-25 skillpoint検証コマンド実装・buildフェーズ

- permission level 2以上のplayer用に`/foodhealing syokugi setskillpoint <amount>`と`addskillpoint <amount>`を追加した。setは未使用SPを指定値へ置換し、addは現在値へ加算する。
- Brigadierの`LongArgumentType.longArg(0L)`で負数とlong範囲外入力を実行前に拒否する。addは加算前にlong overflowを検査し、overflow時はcanonical dataを変更せずsyncもしない。
- 成功時だけ`IShokugiData#setUnspentSkillPoints`でcanonical未使用SPを更新し、既存の`CapabilityEvents.syncToClient`で即時同期する。`setlevel`のSP非連動処理は変更していない。
- 実コマンド登録・接続済み`ServerPlayer`を使うGameTestを追加した。set 500/0、add 500、used SP・skill level・toggle・食義Lv/count・base stat・Food Diversity不変、`setlevel`非連動、permission、負数、不正文字列、long範囲外、add overflowのatomic rejection、成功時syncを対象とする。
- 変更ファイル: `FoodHealingCommands.java`、`FoodHealingCommandGameTests.java`、`en_us.json`、`ja_jp.json`、`docs/CODEX_STATUS.md`。
- 初回`./gradlew.bat build`は新規テストfixtureのskill ID定数名2件が不一致で`compileJava`失敗。既存の確定ID`GUTS`へ修正後、再実行は**成功**。build内の`foodHealingUnitTest`も成功した。
- True Root / True Heroics本体の仕様・効果・購入条件は変更しておらず、`NOT YET TESTED / NEEDS MANUAL TEST`を維持する。
- 次の作業: `foodHealingUnitTest`、`check`を個別実行し、`runGameTestServer`で新規command回帰を含む全GameTestを検証する。

#### 2026-08-25 HP HUD手動PASS・skillpoint検証コマンド要求受領フェーズ

- `MANUAL TESTED / PASS`: chat前景描画へ修正した数値HP HUDを、通常chatログおよび複数行chat表示中に実クライアントで確認した。HUDは元の基準位置付近を維持し、chat背景の下へ隠れず、`HP: current / max`を明確に読める。
- 実クライアント上で希望された視覚順`chat → HP HUD背景 → HP文字`が成立したため、BUG-02の現行判定を`MANUAL FAILED / NEEDS MANUAL RETEST`から`MANUAL TESTED / PASS`へ更新した。
- `/foodhealing syokugi setlevel`は食義Lvのみを変更し、SPを付与しない既存仕様を維持する。True Root / True Heroicsの通常GUI購入試験を支援するため、未使用SPだけを操作する`setskillpoint` / `addskillpoint`を次フェーズで追加する。
- True Root / True Heroicsは、コマンド実装や自動テストを理由に手動PASSへ変更せず、引き続き`NOT YET TESTED / NEEDS MANUAL TEST`とする。
- 変更ファイル: `docs/CODEX_STATUS.md`。この受領フェーズではゲーム本体の変更なし。
- 自動テスト: この受領フェーズでは未実行。直前結果はbuild、`foodHealingUnitTest`、`check`成功、Forge GameTest 29/29成功。
- 次の作業: canonical Shokugi data APIと既存command/sync経路を再利用して`setskillpoint` / `addskillpoint`を実装し、不変条件、負数、long overflow、atomic rejection、client syncの回帰テストを追加する。

#### 2026-08-25 HP HUD前景修正版vanilla client起動準備フェーズ

- `./gradlew.bat runClient`でchat前景描画修正版を起動し、Food Healing client setup、resource reload、sound engine開始まで到達した。クライアントは手動再試験のため操作待ちで継続中。
- `RegisterGuiOverlaysEvent`のFood Healing overlay登録はmod event bus初期化を通過し、重複登録、`Error rendering overlay`、fatal error、crashはない。
- 既知のテスト用2 item model不足warningと開発用Realms認証warningは残るが、今回のHUD描画経路を停止していない。
- 変更ファイル: `docs/CODEX_STATUS.md`。この起動準備フェーズでゲーム本体の追加変更なし。
- 直前の`./gradlew.bat build`、`foodHealingUnitTest`、`check`は成功し、Forge GameTestは29/29成功。
- `NEEDS MANUAL RETEST`: chat非表示、通常chatログ、`T`入力、`/`入力、GUI Scale変更。結果受領まで`MANUAL FAILED / NEEDS MANUAL RETEST`を維持する。
- 次の作業: 起動中clientで上記5状態を人間が確認し、結果を記録する。

#### 2026-08-25 HP数値HUD chat前景描画修正フェーズ

- Forge 47.2.0公式sourceを監査した。`ForgeGui#render`は全overlay後に`RenderGuiEvent.Post`を発火するが、vanilla `ChatComponent`は背景をZ=50、文字をZ=100で描画する。従来HUDは後からZ=0で描いたため、イベント順は後でもdepth上chatに負けていた。
- HUD用途の正式な`RegisterGuiOverlaysEvent`へ移し、`registerAbove(VanillaGuiOverlay.CHAT_PANEL.id(), ...)`でFood Healing HUDをchat overlay直後へ登録した。汎用`RenderGuiEvent.Post`購読は削除した。
- Food Healing HUD全体をvanillaがGUI前景に使用するZ=200でpush/popして描画する。chat背景/文字の既知Z=50/100に対する標準的な前景層で、極端なZ値やdepth clearは使用しない。
- `GameRenderer`はHUD後にdepth clearしてから`ChatScreen`等を描くが、GUI bufferの最終flushまでHUD頂点は同じ描画系に保持される。chat input/command screenにもHUDを再描画する二重event方式は採用せず、一つの登録overlayに統一した。
- HUDの基準座標、Armor補正、Config X/Y offset、画面内clamp、半透明背景、outline/shadowは維持した。chatの座標、scale、visibility、入力欄を変更しない。
- 変更ファイル: `ClientModEvents.java`、`HealthDisplayOverlay.java`、`docs/CODEX_STATUS.md`。
- `./gradlew.bat build`: **成功**。`foodHealingUnitTest`、`check`も成功。
- `./gradlew.bat runGameTestServer`: **29/29成功**。Heroics Lv5実効防御回帰を含む既存全件がPASS。
- `NEEDS MANUAL RETEST`: chat非表示、通常chatログ、`T`入力、`/`入力、GUI Scale変更。前回手動FAILを成功へ推測更新しない。
- 次の作業: 更新済みvanilla clientを起動し、上記5状態の人間による視覚再試験を行う。

#### 2026-08-25 HUD/Heroics追加手動再試験結果受領フェーズ

- `MANUAL TESTED / PASS`: Heroics Lv5 Armor/Toughness ×32。最大HP 28、爆発前HP 9.7、Food Healing基礎Armor 15、Normal、未帯電クリーパー至近爆発で、爆発後HP 5.4となり生存した。実被damageは約4.3 HPで、修正前のほぼ同条件での死亡から改善した。
- `MANUAL TESTED / PASS`: Heroics Lv5専用DR 50%は、既報の水中窒息damage 2 HP→1 HPを維持する。Armor/Toughness PASSとは別の検証結果として記録する。
- Heroics outgoing ×5.0は体感上の上昇のみで、厳密な手動数値検証済みとはしない。
- `MANUAL FAILED / NEEDS FIX`: HP数値HUD。chat表示による大きな上方移動は解消したが、固定位置HUDがchat背景の下へ覆われ、上端の一部しか見えず数値を判別できない。
- HUDの確定要求は、基準位置をchat有無で大きく変えず、chat自体を移動・非表示化せず、描画結果を`chat → HP HUD背景 → HP文字`の順にすること。極端な固定Z値ではなくForge 1.20.1の正しいGUI描画イベント／順序を調査する。
- 変更ファイル: `docs/CODEX_STATUS.md`。この受領フェーズではゲーム本体の追加変更なし。
- 直前の自動結果は`./gradlew.bat build`、`foodHealingUnitTest`、`check`成功、Forge GameTest 29/29成功。今回の手動結果と自動結果を混同しない。
- 次の作業: Minecraft/Forge 1.20.1の`Gui`、chat、`Screen`描画順とForge event発火位置を監査し、HUDだけをchat後に描く最小修正を行う。

#### 2026-08-25 HUD/Heroics修正版vanilla client起動準備フェーズ

- `./gradlew.bat runClient`で更新済みForge開発クライアントを起動し、Food Healing client setup、resource reload、sound engine開始まで到達した。クライアントは手動再試験のため操作待ちで継続中。
- `run/logs/debug.log`で`LivingEntityMixin`が実`LivingEntity`へ適用されたことを確認した。HUD overlayのclient setupも完了している。
- `foodhealing:super_food`と`foodhealing:normal_test_food`のmodel不足warning、および開発アカウントのRealms認証warningは残る。今回のHUD/Heroics経路を停止するcrashやfatal errorはない。
- このフェーズで確定したのは起動成功まで。HUD視覚品質とHeroics実ダメージ結果は、人間の操作結果を受領するまで`MANUAL TESTED / PASS`へ変更しない。
- 変更ファイル: `docs/CODEX_STATUS.md`。ゲーム本体の追加変更なし。
- 直前の`./gradlew.bat build`、`foodHealingUnitTest`、`check`は成功し、`./gradlew.bat runGameTestServer`は29/29成功。
- 次の作業: 起動中clientでHUDとHeroics Armor/Toughnessの絞り込み手動再試験を行う。

#### 2026-08-25 HP数値HUD固定位置・可読性再修正フェーズ

- chat受信後10秒や`ChatScreen`表示中にHUDをchat領域上端まで移動する処理を削除した。chatの表示状態によってHP HUDの基準座標は変わらず、vanilla heart位置、Armor分の既存補正、Config X/Y offsetを維持する。
- HUDは既存の`RenderGuiEvent.Post`で最終描画し、局所的な半透明背景、1px境界、4方向文字outline、shadowを追加した。Minecraft chatの座標、scale、表示状態は変更しない。
- GUI Scaleや極端なConfig offsetに対する画面内clampは維持した。unit回帰を、基準座標が変わらないことと上下左右clampの確認へ更新した。
- 変更ファイル: `HealthDisplayOverlay.java`、`HealthHudLayout.java`、`HealthHudLayoutRegression.java`、`docs/CODEX_STATUS.md`。
- 初回`./gradlew.bat build`はtest helperの削除漏れで`compileTestJava`が1件FAIL。helperを復元後、`./gradlew.bat build`は**成功**し、`foodHealingUnitTest`と`check`も成功した。
- 修正後の`./gradlew.bat runGameTestServer`は**29/29成功**。
- `NEEDS MANUAL RETEST`: chat非表示、通常chat、chat入力、command入力、GUI Scale変更時に、HUDが大きく移動せずHP文字を読めること。視覚結果を受領するまで既存の`PARTIAL PASS`を完全PASSへ変更しない。
- 次の作業: 更新済みvanilla clientでHUDとHeroics Armor/Toughnessを手動再試験する。

#### 2026-08-25 Heroics Armor/Toughness実効倍率修正フェーズ

- 原因は、Heroics倍率をvanilla `Attributes.ARMOR` / `ARMOR_TOUGHNESS`の`MULTIPLY_TOTAL` modifierとして付与していたため、attribute sanitization上限でLv5の実効×32がdamage計算へ届かなかったことだった。
- Food Healing所有の旧Heroics Armor/Toughness modifierを毎tick冪等に除去し、`LivingEntity#getDamageAfterArmorAbsorb`内のvanilla `CombatRules`呼び出しに限って、server権威capabilityから計算した実効Armor/Toughnessを渡すよう修正した。reflectionやvanilla属性上限の全体変更は行っていない。
- 倍率元はbase valueと全modifier operationから再構築し、Food Healing基礎防御、装備、他所有者modifierを含める。除外・削除するのはFood Healingの旧安定UUIDだけで、他所有者modifierを上書き・削除しない。
- 新規回帰 `heroicsEffectiveArmorScalesOffThroughLevelFiveAndReapplies`: 元Armor 20/Toughness 2に対し、OFF 20/2、Lv1 40/4、Lv2 80/8、Lv3 160/16、Lv4 320/32、Lv5 640/64を確認。toggle OFF、HP 40%超過で解除し、40%以下への復帰で再適用されることも確認した。
- 新規回帰 `heroicsLegacyModifiersAreRemovedIdempotentlyAndForeignModifiersRemain`: 旧所有modifierの除外・反復除去、外部UUID modifier保持、True Heroics ×64を確認した。
- 新規回帰 `heroicsLevelFiveEffectiveArmorSurvivesReportedCreeperDamage`: 最大HP 28、現在HP 9.9、基礎Armor 15、Heroics Lv5、実Creeper explosion `DamageSource`の49 damageを通し、生存を確認した。修正前はこの再現試験だけがFAILした。
- 初回Mixin方式の`@ModifyArgs`はForge 47.2.0実行時に`Args$1` classloading失敗を起こしたため、同じ単一`CombatRules`呼び出しへの`@Redirect`へ変更した。変更後は専用GameTestサーバーが正常起動した。
- 変更ファイル: `HeroicsController.java`、`ShokugiTickHandler.java`、`LivingEntityMixin.java`、`foodhealing.mixins.json`、`FoodHealingCombatGameTests.java`、`docs/CODEX_STATUS.md`。
- `./gradlew.bat build`: 実装修正後に成功。`foodHealingUnitTest`と`check`も成功。
- `./gradlew.bat runGameTestServer`: **29/29成功**。実Creeper explosion経路とOFF/Lv1〜5実効値回帰を含む。
- `NEEDS MANUAL RETEST`: 報告時と同条件の実クリーパー爆発、Lv1〜5/toggle/HP閾値の実クライアント再確認。Heroics専用DR 50%は既存の`MANUAL TESTED / PASS`を維持し、outgoing ×5.0は厳密な手動数値確認済みとはしない。
- 次の作業: HP数値HUDの大きなchat退避を廃止し、基準位置を保った背景・文字shadow/outline・最終描画で可読性を確保する。

#### 2026-08-25 Heroics Armor/Toughness再現回帰フェーズ

- 実クライアント報告を数値化し、最大HP 28、現在HP 9.9、Food Healing基礎Armor 15、Heroics Lv5、防具適用damage 49を実`Player.hurt`経路へ通すGameTestを追加した。
- 修正前の`./gradlew.bat runGameTestServer`は28件中27件PASS、1件FAIL。失敗は追加した`heroicsLevelFiveEffectiveArmorSurvivesReportedCreeperDamage`のみで、報告どおりLv5で死亡した。
- 原因調査: 現実装はvanilla `Attributes.ARMOR` / `ARMOR_TOUGHNESS`へ`MULTIPLY_TOTAL`を追加するが、属性自体のsanitization上限によりLv5の実効×32をdamage計算へ渡せない。DR 50%の処理は別layerで、この失敗はDR手動PASSを否定しない。
- 変更ファイル: `FoodHealingCombatGameTests.java`、`docs/CODEX_STATUS.md`。
- 次の作業: 他所有者のattributeを変更せず、vanilla armor計算が参照する正当な元Armor/Toughnessに確定倍率を適用する。OFF/Lv1〜5、toggle、threshold解除/再適用、冪等性を追加回帰する。

#### 2026-08-25 追加vanilla実クライアント結果受領フェーズ

- `MANUAL TESTED / PASS`: Food Production Mastery取得時、実作業台の小麦3→パンがクリック前の結果欄で最初から2個と表示された。normal/shift clickと材料消費量の個別手動結果は含まない。
- `MANUAL TESTED / PASS`: RootはLv1からLv5まで購入でき、最大Lv未満で次Lv購入とON/OFFが同時表示された。Root終了後の`/kill`は正常に死亡し、強制死亡耐性の残留は確認されなかった。
- `MANUAL TESTED / PASS`: SatisfactionはLv1からLv3まで購入でき、Lv3効果の実発動を確認した。保存成功時にStackが一度減って後から戻る見た目は解消し、最初から消費されない表示になった。
- `MANUAL TESTED / PASS`: End帰還ポータル後の現在HPは移動前値を保持した。死亡RespawnではFood Healingの最大HP増加を含む最終最大HPまで全回復した。
- `MANUAL TESTED / PASS`: Heroics Lv5発動中の水中窒息damageが2 HPから1 HPに減少し、Heroics専用DR 50%を実クライアントで確認した。
- `PARTIAL PASS / UX再修正待ち`: チャットによる数値HP HUDの完全非表示は解消したが、チャット表示時にHUDが画面上部へ大きく移動する現在の回避方式は最終UXとして未完了。元の基準位置付近で、背景・shadow/outline・描画層を使って両方を読める構成へ再調整する。
- `MANUAL FAILED / NEEDS INVESTIGATION`: Heroics Lv5、現在HP 9.9/28、Food Healing基礎Armor 15、Normal、未帯電クリーパー至近爆発で死亡した。この結果だけで故障と断定せず、Armor/Toughness ×32のserver-side実値とdamage経路を調査する。Heroics DR 50%のPASSとは分離する。
- Heroics outgoing ×5.0は体感上の上昇のみで、手動数値検証済みとは記録しない。
- 変更ファイル: `docs/CODEX_STATUS.md`。この受領フェーズではゲーム本体をまだ変更していない。
- 直前の自動結果はbuild成功、unit/check成功、GameTest 27/27成功のまま。今回の手動結果で自動成功を上書きしない。
- 次の作業: Heroics OFF/Lv1〜5のserver-side Armor/Toughnessとtoggle/threshold再適用を固定する回帰テストを追加し、実装とvanilla damage pipelineを修正する。続けてHUD UXを元位置付近の描画へ再調整する。

#### 2026-08-25 修正版vanilla実クライアント起動準備フェーズ

- `./gradlew.bat runClient`でForge開発クライアントを起動し、メインメニューで操作待ちの状態まで到達した。クライアントプロセスは手動再試験のため継続中。
- 起動ログ上で`ClientItemStackMixin`、`CraftingMenuMixin`を含む対象Mixinの適用とFood Healing client setup完了を確認した。
- 起動時にテスト用itemとみられる`foodhealing:super_food`、`foodhealing:normal_test_food`のmodel不足warningがある。今回の5件の修正経路を停止させるerror/crashはない。
- この段階で確定したのは「起動成功」までであり、BUG-01からBUG-05の画面表示・操作結果は`MANUAL TESTED`へ更新していない。
- 変更ファイル: `docs/CODEX_STATUS.md`。ゲーム本体の追加変更なし。
- 直前の`./gradlew.bat build`は成功、`foodHealingUnitTest`と`check`は成功、`./gradlew.bat runGameTestServer`は27/27成功。
- 次の作業: ユーザー操作でBUG-01からBUG-05と追加未確認項目を再試験し、観察値を`MANUAL TESTED`または`MANUAL FAILED`として記録する。

#### 2026-08-25 BUG-05 respawn/dimension現在HP修正フェーズ

- 原因はEnd帰還時のvanilla `restoreFrom(..., true)`が旧現在HPをコピーする時点では新Playerの最大HPが20で、そこでclampされた後にFood Healingの最大HP modifierだけが再構築されていたことだった。
- 非死亡clone時だけ移動前現在HPをreplacement Playerの一時namespaced dataへ保持し、低優先度`PlayerRespawnEvent`でFood Healing所有modifierを再構築した後に、最終最大HPでclampして復元する`PlayerHealthLifecycle`を追加した。一時dataは復元時に必ず削除する。
- 死亡Respawn時はFood Healingおよび他所有者が再構築した最終最大HPまで回復する。通常dimension移動は同じPlayerの現在HPを維持し、最終最大HPを超える場合だけclampする。
- Food Healingは自分の安定UUID modifierだけを再構築し、外部max-health modifierを削除・上書き・再生成しない。
- 変更ファイル: `PlayerHealthLifecycle.java`、`CapabilityEvents.java`、`FoodHealingPriorityZeroGameTests.java`、`CODEX_STATUS.md`。
- 新規回帰: 実`PlayerList.respawn`で死亡後100/100、Nether/Overworld/End移動63/100、End帰還再生成後63/100、外部所有modifier +68の保持を確認した。別のclone回帰で移動前63を帰還後の最終最大HP32へclampすることを確認した。
- `./gradlew.bat runGameTestServer`は27/27成功、`./gradlew.bat build`は`foodHealingUnitTest`と`check`を含め成功した。
- `NOT YET TESTED`: 実クライアントのEnd出口ポータル帰還後現在HPと、死亡Respawn後の最終最大HP回復。

#### 2026-08-25 BUG-04 Food Production CraftingMenu修正フェーズ

- 従来GameTestが結果slotを手動で事前設定しており、vanillaのrecipe assembleとclient slot syncを通していなかったため、実クライアントとの差を検出できていなかった。
- `CraftingRecipe#assemble`の戻り値をFood Productionの正式result Stackへ変換するよう`CraftingMenuMixin`を修正した。これにより`ResultContainer`、server remote slot、`ClientboundContainerSetSlotPacket`が同じ倍化Stackを使用する。完成後のInventory追加配布方式は使用していない。
- 変更ファイル: `CraftingMenuMixin.java`、`FoodHealingFoodProductionGameTests.java`、`CODEX_STATUS.md`。
- 新規回帰: 実`CraftingMenu`の小麦3→パン2、通常クリック、Shiftクリック、材料1レシピ分消費、スキルOFF/未取得時パン1、実`InventoryMenu`の2x2パンプキンパイ2。既存回帰でStack上限64とoverflow、NBT保持も継続確認した。
- 最初の実CraftingMenu回帰は期待どおり失敗し、注入位置修正後に`./gradlew.bat runGameTestServer`が26/26成功した。拡張後も26/26成功し、`./gradlew.bat build`も成功した。
- `NOT YET TESTED`: 実クライアントの3x3/2x2結果欄、通常/Shiftクリック、OFF/未取得、材料消費、Stack上限付近の再試験。

#### 2026-08-25 BUG-03 Satisfaction player-facing非消費修正フェーズ

- 原因はserverが元Stackを保持する一方、clientがvanilla消費を先に予測し、後続server inventory syncで元に戻っていたことだった。
- 食べ始めにserverで1回だけSatisfaction判定を行い、ItemStack count/NBT/capabilityを含むsnapshotと保存decisionをclientへ送る`SatisfactionTransactions`と`SatisfactionDecisionPacket`を追加した。network protocolは4から5へ更新した。
- client専用`ClientItemStackMixin`がfinish前にserver decisionを1回だけ消費し、保存成功時はcopyに食事効果を適用しつつ元Stackを減らさない。serverも同じdecisionを使うため二重rollはない。
- 保存失敗decisionもclientへ送って古い保存状態を上書きし、使用中断、logout、clone/dimension、server stopで一時decisionをclearする。
- 通常のuse-startを迂回する外部自動給餌はserver fallbackで1回の判定を行うが、client predictionには専用Adapterが必要。実MOD未導入のため`NOT YET TESTED`。
- 変更ファイル: `SatisfactionTransactions.java`、`SatisfactionDecisionPacket.java`、`ClientSatisfactionState.java`、`ClientItemStackMixin.java`、`ItemStackMixin.java`、`FoodHealingHandler.java`、`HungerChangeHandler.java`、`PacketHandler.java`、`foodhealing.mixins.json`、`FoodHealingExistingSkillsGameTests.java`、`ClientSatisfactionStateRegression.java`、`ShokugiDataUnitTest.java`。
- 検証中の軽量unit runnerはMinecraft registry/game version未初期化で2回失敗し、fixtureを正常起動順に修正した。修正後の`./gradlew.bat build`は成功、`./gradlew.bat runGameTestServer`は25/25成功。
- 新規回帰: packet ItemStack/NBT snapshot、無関係food非一致、decision exactly-once、失敗decision、cancel clear、server事前rollでのStew保存/通常container返却/toggle OFF乱数非消費。
- `NOT YET TESTED`: vanilla実クライアントで乾燥した昆布などを食べ、保存成功時にslot countが一度も減って見えないこと。

#### 2026-08-25 BUG-02 HP HUDチャット退避修正フェーズ

- 数値HP HUDの背景矩形とchat矩形が交差する場合だけ、HUDをchat領域の上へ退避させる配置計算を追加した。描画順でchatを覆う方式は使用していない。
- `ClientChatReceivedEvent`後のvanilla表示時間10秒と`ChatScreen`入力中を対象にし、chat非表示設定では退避しない。chat width/height/scaleはMinecraftの公開APIからGUI-scaled pixelに変換する。
- 既存の`overlayOffsetX/Y`適用後に画面内clampするため、解像度、GUI Scale、極端なoffsetでHUDが画面外へ出ない。
- 変更ファイル: `HealthDisplayOverlay.java`、`HealthHudLayout.java`、`HealthHudLayoutRegression.java`、`ShokugiDataUnitTest.java`。
- 初回buildはMinecraft `Util`のimport誤りで1回失敗した。import修正後の`./gradlew.bat build`は成功し、`foodHealingUnitTest`でchat非表示、非交差、交差時退避、画面内clamp、focused chat fallbackを回帰固定した。
- `NOT YET TESTED`: 実クライアントでchat非表示、通常chat、chat入力、command入力、GUI Scale変更時の見た目。

#### 2026-08-25 BUG-01 複数LvスキルGUI修正フェーズ

- スキル行の「次Lv購入」とON/OFF toggleを独立させ、取得済みで`currentLevel < maxLevel`の間は両ボタンを同時表示するよう修正した。
- 未取得は購入のみ、最大Lvはtoggleのみ、legacy effective skillはOPEN移行を進めずtoggleのみとした。
- SPコストは`FoodHealingSkills.SkillDefinition.levelCosts()`から取得し、GUIに別値をハードコードしていない。
- ツールチップのhover領域をボタン左端までに制限し、購入/toggleと重ならないようにした。
- 変更ファイル: `FoodHealingScreen.java`、`SkillRowControls.java`、`SkillRowControlsRegression.java`、`ShokugiDataUnitTest.java`、`ja_jp.json`、`en_us.json`。
- `AUTOMATED TESTED`: `./gradlew.bat build` 成功。`foodHealingUnitTest`で未取得、Lv1/5、Lv4/5、Lv5/5、legacy effectiveのボタン表示状態を回帰固定した。
- `NOT YET TESTED`: 実クライアント上でRoot/Heroics/SatisfactionをLv2以降へ購入する再試験。

#### 2026-08-25 vanilla実クライアント手動試験フェーズ

- `MANUAL TESTED`: Food Healing RPG GUIの起動、スキル一覧、SP、取得状態の基本表示を確認した。
- `MANUAL TESTED`: 満腹度最大での食事、食事HP回復、食義count/level/SP進行、Food Diversityの新規食品発見を確認した。HP回復、食義、Food Diversityは1回の食事で1回だけ処理された。
- `MANUAL TESTED`: Root Lv1が発動し、発動中のvanilla `/kill`で死亡せず生存することを確認した。Root終了後の`/kill`は未確認。
- `MANUAL TESTED`: Heroics Lv1がHP40%以下で発動し、HUDに「火事場発動中」が表示されることを確認した。
- `MANUAL TESTED`: 死亡後にFood Healingの最大HPと取得済みスキルが保持されることを確認した。死亡Respawn後の現在HPは未確認。
- `MANUAL TESTED`: End帰還ポータルからOverworldへ戻った後も、最大HP増加と取得済みスキルが保持されることを確認した。
- `MANUAL TESTED`: タイトル画面へ戻って同じワールドへ再ログイン後、最大HP、SP、取得済みスキル、Food Diversityが保持されることを確認した。
- `MANUAL FAILED` BUG-01: 複数LvスキルはLv1購入後に購入ボタンがON/OFFボタンへ置換され、Root、Heroics、SatisfactionのLv2以降を購入できない。
- `MANUAL FAILED` BUG-02: 数値HP HUDがチャットまたはコマンド出力と重なり、読めなくなる。
- `MANUAL FAILED` BUG-03: Satisfaction成功時に食料が一度消費されたように見え、後から戻る。自動試験の最終stack一致だけではplayer-facing仕様を満たしていない。
- `MANUAL FAILED` BUG-04: Food Production Mastery取得中でも通常作業台の小麦3→パン結果欄が1個のままで、実clientの`CraftingMenu`経路で結果Stack×2が反映されない。
- `MANUAL FAILED` BUG-05: End帰還後、最大HPとスキルは保持されるが、現在HPがvanilla初期値の20になる。非死亡dimension移動では移動前の現在HPを最終最大HPでclampして保持する必要がある。
- 外部MODを導入した試験は行っておらず、互換性は引き続き`NOT YET TESTED`。

#### 2026-08-24 Priority 0 GameTestフェーズ

- Forge GameTestを0件から4件へ増やし、専用サーバーで全件合格した。
- clone、通常respawn、End帰還respawn、Overworld/Nether/End dimensionイベント後に、Shokugi/SP/skills/toggles/base stats/Root状態とFood Diversity履歴が保持されることを検証した。
- base defense、base max health、Food Diversity max healthを5回再構築し、Food Healing所有UUIDが各1件だけ存在し、外部armor modifierが保持されることを検証した。
- 通常hungerとfull hungerの食事で、declared NutritionによるHP回復、Shokugi、Food Diversityがexactly-onceになることを検証した。
- 食事直後の正当なnon-food Food Level増加が抑止されず、food由来Food Level増加だけがgeneric hunger経路から控除されることを検証した。
- Slaughterがpassive mob dropを増幅する状態でも、Playerを対象とする`LivingDropsEvent`のitem entity数と総item数が変化しないことを検証した。
- GameTestの接続なしServerPlayerで判明した無条件cast/同期境界を修正し、食事のゲーム状態処理をサーバーPlayer共通、Root通知と同期をServerPlayer限定にした。

#### 2026-08-24 Food Production vanillaフェーズ

- Food Production Masteryの4 SP購入停止を解除し、server-side購入で1 level付与・4 SP消費になる単体テストを追加した。
- 2x2/3x3 craftingのresult stack自体を2倍化し、normal click、shift-click、output 1→2、4→8、base 40→visible 64 + overflow 16、NBT保持、材料1回消費をGameTestで検証した。
- overflowは結果slot transaction内で受け渡し、結果確定後に完成品を複製する旧方式を使用していない。
- furnace/smokerのmanual extractionとshift-clickを2倍化し、unskilled playerは1倍、hopper extractionは共有出力を変更せず1倍になることを検証した。
- campfireはplayerによる手動配置時のserver-side適用可否をslot単位で保持し、完成drop transactionだけを2倍化した。slot状態をBlockEntity NBTへ保存し、再読込後の2倍、unskilled/non-player投入の1倍を検証した。
- Farmer's Delightは依存MODを導入した統合試験を行っていないため、Food Productionの外部互換部分は `NOT YET TESTED` のまま維持した。
- 初回GameTestではMixin package内interfaceの直接参照による起動失敗を検出し、通常packageへ移動して解消した。2x2試験では接続なしmockのvanilla packet同期失敗を検出し、test-only EmbeddedChannelを先に初期化するServerPlayer harnessへ修正した。

#### 2026-08-24 optional MOD未導入フェーズ

- optional classの直接参照、optional dependency宣言、危険なAttribute reflectionを再監査し、TaCZ関連の実行時判定が`ModList`とregistry namespaceに隔離されていることを確認した。
- TaCZなしの専用GameTestサーバーで起動・ワールド読込を再確認した。
- TaCZ未導入時のAmmo Conservation購入をサーバー側で`OPTIONAL_MOD_MISSING`として拒否し、skill level、未使用SP、使用済みSPのいずれも変更しないGameTestを追加した。
- TaCZ Base Outgoing Damageを未導入時に購入不可とするかは確定仕様に明記されていないため、推測による挙動変更を行わなかった。
- TaCZ導入時、AttributeFix、backpack、Farmer's Delightなどの実MOD統合試験は行っておらず、`NOT YET TESTED`のままとした。

#### 2026-08-24 Root / Heroics回帰フェーズ

- Heroicsのdamage、DR、Armor/Toughness、HUDで分散していた発動判定と最終倍率を`HeroicsController`へ集約した。
- Armor/ToughnessとHUDがfloat除算誤差によりHPちょうど40%/80%で発動しない可能性を修正し、直接の`health <= maxHealth * ratio`比較へ統一した。
- RootがNutrition 18で発動し、active中だけ致死damageをHP 1へ制限し、HP 1でのdamageとdeath eventをcancelし、直接HP 0をserver tickでHP 1へ戻すことをGameTest化した。
- Root期限切れ後はdeath protectionが残らないことを確認した。
- Heroics Lv1-5のoutgoing x2/x2.5/x3/x4/x5、DR 10/20/30/40/50%、True Heroicsのx20/DR 99%をexact thresholdで検証した。各levelは累積乗算せず最終値として適用される。
- True Heroics単独ではdirect-death protectionを付与しないことを検証した。
- Heroics所有Armor/Toughness modifierを5回再構築しても各UUIDが1件だけで、非Food Healing modifierを保持し、threshold外ではFood Healing所有分だけを解除することを検証した。

#### 2026-08-24 high-difficulty確定部分フェーズ

- L2 HostilityとAuto Levelingがともに未導入の場合、High-Difficulty Base Damage Reduction購入を`OPTIONAL_MOD_MISSING`で拒否し、upgrade count、未使用SP、使用済みSPを変更しないGameTestを追加した。
- Normal Base DR 50%とHigh-Difficulty DR 50%が25% remainingへ乗算合成され、加算による100% immunityにならないことを検証した。
- 両DR 99%およびHigh-Difficulty post-99 transcendenceでも、イベント上のdamageが正値を維持することを検証した。
- `run/mods`にL2 Hostility、Auto Leveling、Trial Monolithなどの統合対象が存在しないことを確認した。導入時の動作は`NOT YET TESTED`。
- Break Realmは外部MODごとのinvulnerability/revival ownershipを安全に解除するAdapterなしでは仕様を満たせないため、汎用`invulnerableTime=0`やinfinite damage、`discard()`による推測実装を行わず、購入停止を維持した。

#### 2026-08-24 lifecycle実経路拡張フェーズ

- Player entity NBTを`ForgeCaps`込みで保存し、新しいPlayerへloadしてlogin再構築を通すsave/reload相当GameTestを追加した。
- NBT round-trip後もShokugi level/count/SP、skills/toggles、base stats、Root state、Food Diversity履歴/HPが一致し、Food Healing所有modifierが各1件、外部modifierが保持されることを確認した。
- 接続済みServerPlayerを`PlayerList.respawn`で実際に置換し、本番Clone/Respawnイベント経路でcanonical dataとowned modifierが保持されることを確認した。
- 固定test teleporterを使用した実`changeDimension`でOverworld→Nether→Overworldを通し、canonical dataとowned modifierが保持されることを確認した。
- 初回buildではmapping上`Entity.saveWithoutId`がvoidであるAPI差異を検出し、返値に依存しないNBT内容検証へ修正した。修正後にbuildと全GameTestを再実行した。
- 実End dimension往復とEnd帰還フラグ付きrespawnは自動化済み。End出口ポータルblock/credits遷移、プロセスを跨ぐdedicated server restart、既存v2.2.5 playerdata読込は引き続き手動/統合試験が必要。

#### 2026-08-24 Gathering / Unbreaking / Pursuit fresh-v3フェーズ

- `OPEN-05`は旧sub-levelの移行規則に限定され、fresh v3の単一node、費用、効果は確定済みと再確認した。legacy dataは従来どおり`LEGACY_MIGRATION_PENDING`で購入を停止する。
- fresh v3でGathering 2 SP、Unbreaking 3 SP、Pursuit 4 SPの購入を解禁し、それぞれ1 level付与・正確なSP消費を単体テスト化した。
- Gatheringがvanilla clayのfinal lootを4→8へ変換し、loot tableを独立再実行しないGlobal Loot Modifier経路をGameTestで確認した。
- durability計算を`DurabilityTransactions`へ分離し、Unbreaking Lv1の90%境界、toggle無効、Armor Masteryのarmor transaction最大1、tool非適用、non-damageable item非適用を検証した。
- Pursuitがfresh v3で追加hitを1回だけ発生させ、その間だけvanilla iframeを解除し、targetの元の`invulnerableTime`をfinallyで復元することを検証した。toggle無効時は追加hitも状態変更もない。
- 初回GameTestではCow最大HP 10に対して20を期待したtest fixture誤りでPursuit 1件が失敗した。最大HP基準へ修正後、buildと全21件を再実行して成功した。
- 旧Gathering/Unbreaking/Pursuitの内部sub-levelをv3表示・移行データへ最終確定する作業は`OPEN-05`のままで、今回決定していない。

#### 2026-08-24 durability乱数副作用回帰フェーズ

- `ItemStackMixin`がUnbreaking未取得時にも`random.nextFloat()`を先行評価し、vanillaの乱数列を不要に消費する回帰を監査で発見した。
- `DurabilityTransactions`へ`RandomSource`を遅延評価で渡し、Unbreakingの適用が確定した場合だけ乱数を1回取得するよう修正した。
- Unbreaking未取得、Armor Masteryのみ、toggle無効、対象外itemではFood Healingが耐久度判定用乱数を消費しないことをGameTestへ追加した。
- Flightなど`IMPLEMENTATION_PENDING`のskill購入がskill level、未使用SP、使用済みSPを変更しない単体テストを追加した。
- 修正後に`./gradlew.bat build`と`./gradlew.bat runGameTestServer`を実行し、buildおよびGameTest 21/21が成功した。

#### 2026-08-24 effect所有権 / Food Diversity数値安全フェーズ

- Fire Resistance、Water Breathing、Night Vision、Resistanceについて、Food Healingより強い外部由来effectを常時skillが上書きせず、skill無効化時にも削除しないGameTestを追加した。
- Purificationがharmful effectを除去しつつ、beneficial effectを保持することを同じGameTestで確認した。これはvanilla effectによるownership境界の`AUTOMATED TESTED`であり、外部MOD統合試験ではない。
- Food Diversityのcanonical max-health bonusを非負化し、加算を`Integer.MAX_VALUE`で飽和させ、負数入力で進行度を減らさないようにした。
- 負数NBT、加算overflow、負数加算を単体テスト化した。
- `./gradlew.bat build`と`./gradlew.bat runGameTestServer`を実行し、buildおよびGameTest 22/22が成功した。

#### 2026-08-24 End lifecycle実経路拡張フェーズ

- 接続済みServerPlayerの実`changeDimension`経路をOverworld→End→Overworldへ拡張した。
- End帰還フラグ付きの実`PlayerList.respawn(player, true)`でPlayer entityを再生成し、Shokugi/SP/skills/toggles/base stats/Root state、Food Diversity、Food Healing所有modifierが保持されることを確認した。
- 既存の通常death respawn、Nether往復と合わせ、Forgeの実dimension/respawnイベント経路を同一GameTestで通した。
- `./gradlew.bat build`と`./gradlew.bat runGameTestServer`を再実行し、buildおよびGameTest 22/22が成功した。
- End出口ポータルblockとcredits遷移そのものは操作していないため、そこまでを自動試験済みとは扱わない。

#### 2026-08-24 既存skillイベント回帰フェーズ

- Fast Eatingが32 tickのfood useを16 tickへ半減し、toggle無効時は32 tickを保持することをGameTest化した。
- Acrobaticsがfall damageだけをcancelし、Fire Resistanceがfire damageだけをcancelすることをtoggle境界込みで確認した。
- Explosion Resistanceがdamageを正確に10%へ、Flame Blessingがburning中だけ正確に70%へ変換することを確認した。
- `./gradlew.bat build`と`./gradlew.bat runGameTestServer`を実行し、buildおよびGameTest 23/23が成功した。

#### 2026-08-24 Satisfaction実食事経路回帰フェーズ

- 接続済みServerPlayerでvanillaの`ItemStack.finishUsingItem`を実行し、Satisfaction Lv3のMixin実経路をGameTest化した。
- 成功時はMushroom Stewの同一ItemStackインスタンス、NBT、countが保持され、Food Levelはdeclared Nutrition分だけ一度増加することを確認した。
- 失敗時はvanillaどおりBowlへ変化し、Food Levelは同じく一度だけ増加することを確認した。成功・失敗ともFood Healingの判定用乱数消費は1回だけである。
- Satisfaction無効時はfoodを通常消費し、Food Healingが保存判定用乱数を消費しないことを確認した。
- `./gradlew.bat build`と`./gradlew.bat runGameTestServer`を実行し、buildおよびGameTest 24/24が成功した。

#### 2026-08-24 repeatable base-stat取引回帰フェーズ

- 7種のrepeatable base statが確定仕様どおり1/1/1/2/20/1/1 SPであることを単体テストで固定した。
- Base Defense、Base Max HP、Recovery、Base Outgoing、TaCZ Base Outgoingの購入がそれぞれcanonical countだけを1増やし、合計25 SPを正確に未使用から使用済みへ移すことを確認した。
- Recovery x1.25、Base Outgoing x1.10、TaCZ Base Outgoing x1.01の1 point式を検証した。TaCZ本体を導入した統合試験ではない。
- Base Damage Reductionの99回目がlinear 99に入り、100回目からinteger transcendence stageへ入ることを確認した。
- transcendence countが`Long.MAX_VALUE`の場合は`NUMERIC_LIMIT`で拒否し、SPと使用済みSPを変更しないこと、RecoveryのSP不足時も取引全体が不変であることを確認した。
- `./gradlew.bat build`で`foodHealingUnitTest`を含むbuildが成功し、`./gradlew.bat runGameTestServer`も24/24が再成功した。

#### 2026-08-24 Food Diversity同期境界フェーズ

- `FoodDiversitySyncPacket`が構築時のcanonical NBTを参照のまま保持していたため、送信前の後続変更がpacket内容へ混入し得る境界を修正した。現在は構築時にNBTを防御的copyする。
- wire上のnull NBTを空の`CompoundTag`へ正規化し、client capabilityのdeserializeへnullを渡さないようにした。
- packet registrationの`PLAY_TO_CLIENT`指定に加え、handler自身もclient受信側だけで適用する方向検査を追加した。
- 送信元NBTの後続変更に影響されないことと、null payloadの再encodeが空NBTになることを単体テスト化した。
- `./gradlew.bat build`で`foodHealingUnitTest`を含むbuildが成功し、`./gradlew.bat runGameTestServer`も24/24が再成功した。実clientを使ったmultiplayer同期は引き続き`NOT YET TESTED`。

#### 2026-08-24 skill前提data-driven化フェーズ

- `SkillDefinition`の単一前提ID/levelを不変の`SkillRequirement`リストへ置き換え、複数前提を定義追加だけで扱える構造にした。
- server-side購入判定とclient UIの購入可否判定の両方を前提リストのall-matchに統一し、tooltipも複数の確定前提を表示できるようにした。
- Purification Masteryは通常Purification Lv1、Truth MasteryはPurification Mastery Lv1という仕様書上の必須前提だけを記録した。`OPEN-02`/`OPEN-03`の追加nodeは決定せず、`openPrerequisites=true`による購入停止を維持した。
- True Rootなど既存の確定前提とPurification/TruthのOPEN状態を単体テストで固定した。
- 汎用の強制死保護は正当な数値damage死と安全に区別できず、Truthのtrait除去も所有MODのAPI/Adapterが必要なため、推測実装は行っていない。
- `./gradlew.bat build`で`foodHealingUnitTest`を含むbuildが成功し、`./gradlew.bat runGameTestServer`も24/24が再成功した。

#### 2026-08-24 multiplayer player-scope回帰フェーズ

- 別UUID・別Connection・別EmbeddedChannelの接続済みServerPlayer 2人を同時に参加させるPriority 0 GameTestを追加した。
- player Aのserver-side skill購入がAの未使用/使用済みSPとskill levelだけを変更し、player Bの進行を変更しないことを確認した。
- Aのskill toggleがBの同toggleに波及せず、Aの食事によるShokugiとFood DiversityもBへ漏れないことを確認した。
- A宛てに明示送信したShokugi/Food Diversityの2 packetがAのchannelだけに出力され、Bのchannelには出力されないことを確認した。
- `./gradlew.bat build`と`./gradlew.bat runGameTestServer`を実行し、buildおよびGameTest 25/25が成功した。これはserver/EmbeddedChannelの`AUTOMATED TESTED`であり、2台の実clientを使ったmultiplayer統合試験ではない。

#### 2026-08-24 実Player death drop非増殖フェーズ

- 既存の`LivingDropsEvent`所有境界試験に加え、接続済みServerPlayerのinventoryにDiamond 7個を入れ、Slaughter所有playerを攻撃元とする実`ServerPlayer.die`経路を追加した。
- 通常のplayer death処理後にworldへspawnしたDiamondが正確に7個で、Food Healingが増殖も消失もさせないことを確認した。
- `./gradlew.bat build`と`./gradlew.bat runGameTestServer`を実行し、buildおよびGameTest 25/25が再成功した。grave/tombstone MODの作成・回収経路は実MOD未導入のため`NOT YET TESTED`。

#### Priority 0 / データ安全性

- TaCZをハード依存させる直接compile参照と危険なMixinを除去した。
- Food Healing所有のAttributeModifierだけを安定UUIDで冪等に再構築する方式へ変更した。
- clone、respawn、dimension遷移で進行データを保持・再適用する処理を整備した。
- `mayfly=false` の一律設定を避け、Food Healing以外が付与した飛行能力を破壊しない方針へ変更した。
- Food Healingが管理するeffectだけを追跡し、他MODのeffectを条件一致だけで削除しないようにした。
- 食事処理をサーバー側の単一トランザクションへ集約し、回復、食義、Food Diversityの二重処理を抑止した。
- Gatheringのloot再実行・再spawn方式を廃止し、final lootへのmodifier方式へ移行した。
- 旧保存データは生データをバックアップして保持し、不明な換算でSPを付与しないようにした。

#### Priority 1 / 内部整合性

- Shokugi level、count、SPを`long`基準へ移行した。
- SP購入処理をサーバー側で検証し、負数、overflow、重複購入を拒否するようにした。
- スキルIDとbase stat IDを表示文字列から安定したResourceLocationへ移行した。
- skill toggle、Food Diversity、進行データのserver-to-client同期を追加・整理した。
- 数値計算でNaN、Infinity、範囲外値を受け入れない検証を追加した。

#### 確定済み機能

- Skill Tree画面、キー割り当て、数値HP HUDを実装した。
- Base stats、Root、確定済みHeroics/high-difficulty skillのサーバー側処理を追加した。
- AlwaysEatをItem/ItemStack経路へ統合し、旧イベント方式を削除した。
- Food Production Masteryのvanilla crafting、furnace、smoker、campfire結果トランザクションを実装し、購入可能にした。
- Gathering loot modifier、legacy projectile互換層、管理コマンドを追加・更新した。

### 旧集計：主な変更ファイル

> 履歴の集計です。現在の判定・未完了・次の作業は[現在の要約](#現在の要約)を参照してください。

#### ビルド・メタデータ・リソース

- `build.gradle`
- `src/gametest/resources/empty.snbt`
- `gradle.properties`
- `src/main/resources/META-INF/mods.toml`
- `src/main/resources/foodhealing.mixins.json`
- `src/main/resources/assets/foodhealing/lang/en_us.json`
- `src/main/resources/assets/foodhealing/lang/ja_jp.json`
- `src/main/resources/data/foodhealing/loot_modifiers/gathering.json`
- `src/main/resources/data/forge/loot_modifiers/global_loot_modifiers.json`

#### 進行・永続化・同期

- `src/main/java/com/leva/foodhealing/capability/IShokugiData.java`
- `src/main/java/com/leva/foodhealing/capability/ShokugiData.java`
- `src/main/java/com/leva/foodhealing/capability/FoodDiversityData.java`
- `src/main/java/com/leva/foodhealing/capability/CapabilityEvents.java`
- `src/main/java/com/leva/foodhealing/skill/FoodHealingSkillIds.java`
- `src/main/java/com/leva/foodhealing/skill/FoodHealingSkills.java`
- `src/main/java/com/leva/foodhealing/stat/FoodHealingBaseStatIds.java`
- `src/main/java/com/leva/foodhealing/stat/FoodHealingBaseStats.java`
- `src/main/java/com/leva/foodhealing/network/PacketHandler.java`
- `src/main/java/com/leva/foodhealing/network/ShokugiSyncPacket.java`
- `src/main/java/com/leva/foodhealing/network/FoodDiversitySyncPacket.java`
- `src/main/java/com/leva/foodhealing/network/PurchaseSkillPacket.java`
- `src/main/java/com/leva/foodhealing/network/PurchaseBaseStatPacket.java`
- `src/main/java/com/leva/foodhealing/network/ToggleSkillPacket.java`

#### クライアント・UI

- `src/main/java/com/leva/foodhealing/client/ClientFoodHealingState.java`
- `src/main/java/com/leva/foodhealing/client/FoodHealingKeyMappings.java`
- `src/main/java/com/leva/foodhealing/client/ClientModEvents.java`
- `src/main/java/com/leva/foodhealing/client/ClientScreenEvents.java`
- `src/main/java/com/leva/foodhealing/client/FoodHealingScreen.java`
- `src/main/java/com/leva/foodhealing/client/HealthDisplayOverlay.java`
- `src/main/java/com/leva/foodhealing/client/HealthHudLayout.java`

#### ゲームプレイ・互換処理

- `src/main/java/com/leva/foodhealing/event/FoodHealingHandler.java`
- `src/main/java/com/leva/foodhealing/event/FoodHealingTransactions.java`
- `src/main/java/com/leva/foodhealing/event/HungerChangeHandler.java`
- `src/main/java/com/leva/foodhealing/event/FoodDiversityHandler.java`
- `src/main/java/com/leva/foodhealing/event/DamageEventHandler.java`
- `src/main/java/com/leva/foodhealing/event/RootController.java`
- `src/main/java/com/leva/foodhealing/HeroicsController.java`
- `src/main/java/com/leva/foodhealing/ShokugiTickHandler.java`
- `src/main/java/com/leva/foodhealing/event/FoodProductionTransactions.java`
- `src/main/java/com/leva/foodhealing/DurabilityTransactions.java`
- `src/main/java/com/leva/foodhealing/event/LootEventHandler.java`
- `src/main/java/com/leva/foodhealing/compat/LegacyProjectileCompat.java`
- `src/main/java/com/leva/foodhealing/loot/GatheringLootModifier.java`
- `src/main/java/com/leva/foodhealing/command/FoodHealingCommands.java`

#### Mixins

- `src/main/java/com/leva/foodhealing/mixin/ItemMixin.java`
- `src/main/java/com/leva/foodhealing/mixin/ItemStackMixin.java`
- `src/main/java/com/leva/foodhealing/mixin/CraftingMenuMixin.java`
- `src/main/java/com/leva/foodhealing/mixin/AbstractFurnaceMenuMixin.java`
- `src/main/java/com/leva/foodhealing/mixin/FurnaceResultSlotMixin.java`
- `src/main/java/com/leva/foodhealing/mixin/ResultSlotMixin.java`
- `src/main/java/com/leva/foodhealing/mixin/CampfireBlockEntityMixin.java`
- `src/main/java/com/leva/foodhealing/mixin/LivingEntityMixin.java`

#### Food Production

- `src/main/java/com/leva/foodhealing/FoodProductionTransactions.java`
- `src/main/java/com/leva/foodhealing/FoodHealingSkills.java`
- `src/main/java/com/leva/foodhealing/extension/FoodProductionResultSlotExtension.java`
- `src/main/java/com/leva/foodhealing/extension/CampfireFoodProductionExtension.java`

#### テスト

- `src/test/java/com/leva/foodhealing/ShokugiDataUnitTest.java`
- `src/test/java/com/leva/foodhealing/DataBoundaryRegression.java`
- `src/main/java/com/leva/foodhealing/FoodHealingNetworkGameTests.java`
- `src/restartTest/java/com/leva/foodhealing/RestartPersistenceVerification.java`（配布対象外）
- `src/taczTest/java/com/leva/foodhealing/TaczIntegrationVerification.java`（明示opt-inのみ・配布対象外）
- `src/main/java/com/leva/foodhealing/FoodHealingPriorityZeroGameTests.java`
- `src/main/java/com/leva/foodhealing/FoodHealingFoodProductionGameTests.java`
- `src/main/java/com/leva/foodhealing/FoodHealingOptionalAbsenceGameTests.java`
- `src/main/java/com/leva/foodhealing/FoodHealingCombatGameTests.java`
- `src/main/java/com/leva/foodhealing/FoodHealingExistingSkillsGameTests.java`

#### 削除した旧実装

- `src/main/java/com/leva/foodhealing/event/AlwaysEatHandler.java`
- `src/main/java/com/leva/foodhealing/mixin/TaCZGunScriptMixin.java`

### 旧集計：Build / Test結果

> 履歴の集計です。現在の判定・未完了・次の作業は[現在の要約](#現在の要約)を参照してください。

#### AUTOMATED TESTED

- `./gradlew.bat build`: **成功**
- `foodHealingUnitTest`: **成功**
- GUI entry policy回帰: **成功**。`InventoryScreen` / `CreativeModeInventoryScreen`のみ許可し、Blast Furnace、Smoker、Grindstone、Smithing、Lectern、Container、Hopper、Dispenser、Furnace、Craftingの各Screen classを拒否。
- `check`: **成功**。カスタム単体テストを含む。
- Gradle標準JUnit `test`: 実行対象0件。現在の主要単体テストは`foodHealingUnitTest`経由。
- Forge GameTest: 最新vanilla **41/41成功**（9/8承認済みmigration用のtest-only2件を既存39件へ追加）。9/6の39/39、従来31/31・38/38は当時の履歴として維持。TaCZ導入の38/38は9/5の限定範囲のままで、今回の41件へ昇格しない。
- unit追加境界: BigInteger参照の5,000取引、legacy1,000往復、long/NBT/codec異常値、Root固定15秒window、最大購入世代のatomic拒否をPASS。
- `AUTOMATED FIXTURE TESTED / PASS`: Lv0/負Lv/型違い/欠損/旧schemaのraw backup保持5ケースを追加。別JVM用の旧形式ディスクfixture5ケースでもlogin/save/read後のraw・未知field・Inventory/EnderItems/ForgeData・Diversity・foreign modifier保持とゼロSPを確認。実v2.2.5 world移行とは別の結果。
- `AUTOMATED SNAPSHOT TESTED / PASS`: 元development backupの174個のschema4 Shokugi NBTを各20回メモリ内往復し、元データと完全一致。worldは起動していない。
- 通常dedicated server: fresh/legacy fixture各1名を実PlayerDataStorageで保存し、別JVM再起動で再読込。6,000実tick/30回保存の限定soakもPASS。これは本番world移行、実2-player操作、長時間modpack性能の代替ではない。
- skillpoint command回帰: **成功**。set 500/0、add、used SP・skill level・toggle・食義Lv/count・base stat・Food Diversity不変、permission、負数・不正入力・long overflowのatomic rejection、`setlevel`非連動、成功時client sync packetを確認した。
- durability非適用時のvanilla乱数列非消費と、実装保留skill購入時のSP非消費: **成功**
- Food Production単体購入テスト: **成功**。4 SP消費、1 level付与。
- JSON resource parse: **成功**
- production codeのoptional直接class参照、全体能力上書き、必須Mixinのrequire=0化なし。TaCZ直接API参照は明示opt-inの配布対象外fixtureだけに隔離。Flight所有権方式は未完成のまま購入停止しているため、実装完了という意味ではない。

#### INTEGRATION TESTED

- `./gradlew.bat runClient`: 2026-09-05 09:33にRoot手動回帰用clientを起動し、今回の固定窓手動PASSを受領した。今回の記録作業では終了操作を行っていない。先行する08:03:41〜08:04:35の自動smokeは通常終了（exit0 / BUILD SUCCESSFUL）しており、手動結果とは分離する。旧test food2種のmodel欠落warningは未解決のまま。
- `./gradlew.bat runGameTestServer`: **成功**
- TaCZ未導入シナリオ: dedicated server起動、ワールド読込、Ammo Conservation購入拒否、SP非消費を確認した。これはTaCZ導入時の互換性確認ではない。
- `CraftingMenuMixin`、`ResultSlotMixin`、`AbstractFurnaceMenuMixin`、`FurnaceResultSlotMixin`、`CampfireBlockEntityMixin`、`ItemStackMixin`などの対象Mixinが起動時に適用されることを確認した。
- Global Loot Modifierが読み込まれることを確認した。
- 9/5はvanilla/TaCZ導入それぞれで`All 38 required tests passed`。9/6はvanillaのみ`All 39 required tests passed`を確認した。TaCZ導入の39件は未実行。
- TaCZ 1.1.7-hotfix2のみ: 既存13 damage integration/別JVMに加え、9/9 **Ammo Conservation / AUTOMATED INTEGRATION TESTED**。closed/open/manual/burst、1shot抽選、heat A〜M、reload/bolt、購入/toggle/respawn/別JVM保持を上記専用fixtureで限定PASS。9/8の実銃baselineは別の実client証拠。新Adapterの実射撃入力/予測/HUD/全custom packは未試験、1.0.3/1.1.8-hotfixを今回の結果で互換PASSにしない。
- 自動確認範囲: synthetic/actual respawn、Nether/End dimension transfer、End帰還フラグ付きrespawn、Player NBT round-trip、canonical data、owned modifier冪等性、食事exactly-once、Satisfactionの成功/失敗/無効時の実`finishUsingItem`経路、syntheticと実`ServerPlayer.die`のPlayer death drop非増殖、2-player server capability/sync scope、skillpoint管理command、2x2/3x3 crafting、overflow、furnace/smoker/campfire、hopper automation x1、Root/True Root OFF/ON、Heroics/True Heroics OFF/ON、Heroics実効Armor/Toughness OFF/Lv1〜5・toggle・HP閾値・旧所有modifier除去・実Creeper explosion、高難度DR、optional MOD未導入購入ゲート、Gathering/Slaughter final loot、durability scope、Pursuit iframe復元、effect所有権。

#### MANUAL TESTED

- `MANUAL NUMERIC TESTED / PASS`: Heroics非発動時のvanilla Armor上限維持。Lv5 OFF / True Heroics OFF / Root OFF、防具なし、base Armor40/Toughness0、最大HP1000で`/attribute @s minecraft:generic.armor get`は30.0。`minecraft:mob_attack` 100でHealth1000→924.0fを確認した（2026-09-05利用者報告）。
- `MANUAL NUMERIC TESTED / PASS`: Heroics発動中の極端Armor境界。Lv5 ON / True Heroics OFF / Root OFF、防具なし、base Armor15/Toughness0、HP400/1000で`minecraft:mob_attack` 1000を受け、Health400→300.0f。報告条件でdamageの負値/0への崩壊と完全無敵化がないことを確認した。既存GameTest自動PASSと分離する（2026-09-05利用者報告）。
- `MANUAL TESTED / PASS（限定範囲）`: 食事credit後の独立した非食事回復。Root OFF / naturalRegeneration=falseでリンゴNutrition4によりFood Level 12→16・HP20.0→28.0、その後Saturation0.0・Food Level15からsaturation effectでFood Level15→16・HP28.0→30.0を確認。元不具合の厳密なtick境界は既存GameTest PASSのみと分離する（2026-09-05利用者報告）。
- `MANUAL TESTED / PASS`: Root固定15秒窓の期限非延長。Root Lv5 ON / True Root OFFでNutrition6の食事を0/約12/約26秒に完了し、3食目でもRootが発動しないことを実クライアントで確認した（2026-09-05利用者報告）。
- vanilla実クライアント: GUI起動と基本表示、満腹時食事、HP回復、食義/SP進行、Food Diversityを確認。食事系3処理は重複なし。
- vanilla実クライアント: Root Lv1発動と発動中の`/kill`生存、Heroics Lv1の40%閾値発動とHUD表示を確認。
- vanilla実クライアント: 死亡後の最大HP/スキル、End帰還後の最大HP/スキル、ログアウト→再ログイン後の最大HP/SP/スキル/Food Diversity保持を確認。
- `MANUAL TESTED / PASS`: Root Lv1→5購入、次Lv購入とON/OFFの同時表示、Root終了後の`/kill`による正常死亡。
- `MANUAL TESTED / PASS`: Satisfaction Lv1→3購入、Lv3効果発動、保存成功時のplayer-facing非消費表示。
- `MANUAL TESTED / PASS`: Food Production Masteryの実作業台結果欄で小麦3→パン2。
- `MANUAL TESTED / PASS`: Food Production Mastery vanilla crafting追加操作。3x3 normal clickで小麦3→パン2と材料3消費、3x3 Shift-clickで4 recipe分の小麦12→パン8、2x2 inventory craftingでカボチ/砂糖/卵各1→パンプキンパイ2を確認した。
- `MANUAL TESTED / PASS`: Food Production Masteryのtoggle OFFと未取得はどちらも小麦3→パン1の通常結果。ON時のクッキー16個Shift-clickは既存60→64+別slot 12となり、Stack上限、overflow保全、二重増殖なしを確認した。
- `MANUAL TESTED / PASS`: End帰還時の移動前現在HP保持、死亡Respawn時の最終最大HPまでの全回復。
- `MANUAL TESTED / PASS`: Heroics Lv5専用DR 50%。水中窒息damageで通常2 HP減少に対し発動中1 HP減少を確認。Armor/Toughness ×32とoutgoing ×5.0の数値確認はこのPASSに含めない。
- `MANUAL TESTED / PASS`: Heroics Lv5 Armor/Toughness ×32。最大HP 28、爆発前HP 9.7、基礎Armor 15、Normalの未帯電クリーパー至近爆発後もHP 5.4で生存した。outgoing ×5.0の数値確認は含めない。
- `MANUAL TESTED / PASS`: 通常Heroics Lv1〜5 outgoing damage。OFF時基準6に対しON時は12 / 15 / 18 / 24 / 30となり、x2.0 / x2.5 / x3.0 / x4.0 / x5.0を手動数値確認した。各Lvは累積乗算ではなく現在Lvのfinal effectである。
- `MANUAL TESTED / PASS`: 通常Heroics Lv1〜5専用DR。5.0 damageに対する実damageは4.5 / 4.0 / 3.5 / 3.0 / 2.5で、10% / 20% / 30% / 40% / 50%と一致した。toggle OFF時は専用DRが解除され、5.0 damageに戻ることも確認した。Armor/Toughnessの個別手動結果はこのPASSに含めない。
- `MANUAL NUMERIC TESTED / PASS`: 通常Heroics Armor/Toughness Lv1〜5。`minecraft:mob_attack`のvanilla damage pipelineで、x2 / x4 / x8 / x16 / x32の各effective Armor/Toughnessと各Lv専用DRを合成した実測HPが理論値と一致した。Lv1〜4の従来の手動未確認は解消し、自動試験結果と分離した実クライアント数値PASSとする。
- `MANUAL TESTED / PASS`: BUG-02数値HP HUD。通常chatログおよび複数行chat表示中も元の基準位置付近を維持し、chat背景より前面の背景・文字として`HP: current / max`を明確に読めることを実クライアントで確認した。
- `MANUAL TESTED / PASS`: BUG-02数値HP HUD追加条件。chat非表示、`T`でchat入力中、`/`でcommand入力中、GUI Scale Auto/1/2/3/4で、可読性、前景表示、基準位置、画面内収納、食義Lv/countとHeroics表示のscaleを確認した。既報の通常/複数行chatと合わせ、vanilla HUD追加手動試験は完了。
- `MANUAL TESTED / PASS`: skillpoint管理command。`setskillpoint 500`、`addskillpoint 25`→525、`setskillpoint 0`、負数拒否、GUI即時反映を確認した。
- `MANUAL TESTED / PASS`: True RootのRoot Lv5前提、20 SP購入、SP 20消費、取得表示、およびON時の1回分予約→終了直後再発動ロジック。
- `MANUAL TESTED / PASS`: Root OFF / True Root ONではRoot効果自体が発動しない。True Rootが無効な通常Rootを無視して単独発動しない親スキル境界を確認した。
- `MANUAL TESTED / PASS`: Root ON / True Root OFFでは、通常Root発動中に追加Nutrition 18を取得してもTrue Root固有予約が作成されず、15秒終了後に自動再発動しない。Root ON / True Root ONの1回予約・1回再発動と合わせ、True Root toggleの切り分けを完了した。
- `MANUAL TESTED / PASS`: True HeroicsのHeroics Lv5前提、100 SP表示・消費、取得Lv1/1表示、toggle操作、HP約71.5%でのOFF非発動/ON発動。
- `MANUAL TESTED / PASS`: True Heroics専用DR 99%。窒息damageが2.0から約0.02へ低下した。
- `MANUAL TESTED / PASS`: True Heroics outgoing x20。鉄の剣による同条件damageがOFF 6、ON 120となった。
- `MANUAL TESTED / PASS`: True Heroicsの実防御効果。基礎Armor 5・防具なし・Normalの通常Creeperほぼゼロ距離爆発でOFF時死亡、ON時HP約14→13.9で生存した。Armor/Toughness x64単独倍率の手動確認には含めない。
- `MANUAL NUMERIC TESTED / PASS`: True Heroics Armor/Toughness x64。Root OFF、naturalRegeneration=false、防具なし、base 0.25/0.25、HP700/1000で`minecraft:mob_attack` 20を比較し、両Heroics OFFはHealth700→680.0、両ONは700→699.9013f。通常Heroicsが閾値非発動のHP70%で、effective 16/16と既知の専用DR99%の合成理論値に一致した（2026-09-05利用者報告）。既存GameTestの自動PASSと分離して記録する。
- `MANUAL TESTED / PASS`: Root / True Root OFF、True Heroics ON時の`/kill`で正常死亡し、True Heroics単独にdirect-death protectionがないことを確認した。
- `MANUAL TESTED / PASS`: 再ログイン後も未使用SP 358、使用済みSP 142、Root Lv5/5 OFF、True Root Lv1/1 OFF、Heroics Lv5/5 OFF、True Heroics Lv1/1 OFFを保持した。
- `MANUAL TESTED / PASS`: 通常player inventoryを開いた状態のデフォルト`S`キーからFood Healing RPG GUIが開くことを確認した。
- `MANUAL TESTED / PASS`: 修正後のCreative Inventoryを開いた状態のデフォルト`S`キーからFood Healing RPG GUIが開くことを確認した。
- `MANUAL TESTED / PASS`: Blast Furnace、Smoker、Grindstone、Smithing Table、Lectern、Barrel、Hopper、Dropper、Dispenser、Chest、Furnace、Crafting Tableの各container画面で`S`を押してもFood Healing GUIが開かないことを確認した。

#### INVALID / AMBIGUOUS TEST

- True Root toggle OFF旧報告: 通常Root Lv5がONのまま観察しており、通常Rootの発動とTrue Root固有予約を切り分けられていなかった。`INVALID / AMBIGUOUS TEST - Root ON状態との切り分け不足`へ訂正し、確定FAIL・修正前不具合の根拠から除外した。
- Heroics Armor/Toughness予備試験: `minecraft:generic`はArmor 5 / Toughness 1でも100 damageがそのまま通り、今回のArmor/Toughness確認に不適切だったため`INVALID / TEST SOURCE UNSUITABLE`。Heroics FAILに含めず、数値判定は`minecraft:mob_attack`結果を使用した。

#### MANUAL FAILED

- BUG-01旧失敗: 複数Lv購入不可。2026-08-25のRoot Lv1→5およびSatisfaction Lv1→3再試験で解消を`MANUAL TESTED / PASS`。
- BUG-02旧失敗: chat/commandでHUDが完全に読めなかった。途中の上方退避版は`PARTIAL PASS`だったが、chat前景描画修正後の通常/複数行chatおよび追加四条件で`MANUAL TESTED / PASS`を確認し、解決済み。
- BUG-03旧失敗: Satisfactionの可視consume→restore。2026-08-25再試験で解消を`MANUAL TESTED / PASS`。
- BUG-04旧失敗: 作業台結果欄がパン1。2026-08-25に結果欄パン2を再試験し、2026-08-31にnormal/shift click、2x2/3x3、OFF/未取得、材料消費、overflowを`MANUAL TESTED / PASS`としたため、予定済みvanilla crafting範囲で解決済み。
- BUG-05旧失敗: End帰還後の現在HP20。2026-08-25再試験でEnd帰還保持と死亡Respawn全回復を`MANUAL TESTED / PASS`。
- BUG-02追加失敗: 固定位置化後にHP HUDがchat背景の下へ描画され、数値がほぼ読めなかった。Forgeのchat直後overlay＋vanilla標準前景Zへ修正し、2026-08-25の実クライアント再試験で`MANUAL TESTED / PASS`へ解決済み。
- Creative Inventory `S`起動の修正前失敗履歴: vanilla実クライアントでFood Healing GUIが開かず、`MANUAL FAILED / NEEDS FIX`だった。原因特定・修正・自動回帰に続き、修正後の人間再試験で`MANUAL TESTED / PASS`を確認したため解決済み。

#### MANUAL FAILED / NEEDS INVESTIGATION

- Heroics Lv5 Armor/Toughness ×32旧失敗: Food Healing基礎Armor 15、HP 9.9/28、Normalの未帯電クリーパー至近爆発で死亡。attribute上限の修正後、爆発前HP 9.7→爆発後HP 5.4で生存したため、2026-08-25追加再試験で`MANUAL TESTED / PASS`へ解決済み。

#### 旧集計：NOT YET TESTED

> 履歴の集計です。現在の判定・未完了・次の作業は[現在の要約](#現在の要約)を参照してください。

- CURRENT-WRAPPER SOURCE CONTROLの9/6停止（元instance metadata書込み検出）は履歴として維持。その後の完全隔離rootによる9/8正規MSA worldless smoke、正規元UUIDによるsource load/save、実v3初回migration/save、同じV3M0908の第二boot/reload/saveは**各記録範囲でPASS**。今回の原本追加変更0は過去metadata/mtime差分原因の解明を意味しない。未確認は**HISTORICAL-WRAPPER AUTHENTICATED COMBINATION / EXACT HISTORICAL BYTE REPRODUCTION / 全MOD GAMEPLAY COMPATIBILITY**等であり、完了したCURRENT試験を再実施待ちへ戻さない。第三bootはNOT RUN / NOT AUTHORIZEDで、必要性や承認を自動推定しない。
- 食事creditの後続独立回復は上記報告範囲で手動PASSだが、元不具合の厳密なtick境界は手動再現済みとはせず既存GameTest PASSと分離する。Root固定窓の期限非延長と、Heroics極端値2境界の報告範囲の手動PASSも維持する。
- 9/6提供ZIPのprovenance/実NBT保持/旧raw-only別JVMfixture、9/8 source2.2.5 load/save、自動respec fixture、実v3初回migrationの通常保存・post-save auditに加え、**SECOND BOOT + RELOAD + NORMAL SAVE / IDEMPOTENCY PASS**を確認。実v3 canonicalのLv2/count35/未使用2/使用済0/raw型付き完全保持、SP再返還0を維持した。全MOD gameplay/ownership網羅・歴史的byte同一性・認証済みhistorical Wrapper組合せ・元metadata過去差分原因は未確認のまま。過去NOT PASS・UI観測停止・既知ERRORの履歴は消さない。
- `BLOCKED - SECOND MINECRAFT ACCOUNT REQUIRED`: 実2-playerによる購入、toggle、食事、死亡、再接続。正規アカウント1つのみで今回は実施不能。`docs/MULTIPLAYER_MANUAL_TEST.md`の2-client手順は保留参考。第2アカウント購入や協力者を必須要求にせず、offline/fake/同一アカウント二重接続で代替しない。server-side capability/packet/scope/replay自動PASSは実2-client PASSではない。
- 実1-client dedicatedの**STEP 1〜6は記録範囲で完了/PASS**。正常終了・最終log差分・process終了も確認済みで、同手順の再試験待ちではない。ただし正式接続先での初回14:32 TIMEOUTと18:21の認証完了前切断の原因は未確定で、接続安定性全般の解決済みとはしない。IPv6別接続先refusedは正式試験外として分離する。実2-client・外部MOD・手動server restartへ範囲を拡張しない。
- AttributeFix、L2系、Auto Leveling、Traveler's Backpackは`BLOCKED - TARGET MOD ARTIFACT REQUIRED`を維持。Sophisticated Backpacks3.23.14.1233/Core1.2.49.962とFarmer's Delight1.20.1-1.2.10は9/6の元Prism instance調査でlocal Jarを確認したため、artifact不存在とはしない。Sophisticatedのsource保存UUID経路だけは今回**STATIC AUDITED - SELECTED SAVE/UUID PATHS ONLY**、Farmer's Delightは**METADATA IDENTIFIED**。どちらも正式機能互換/自動給餌等は**NOT INTEGRATION TESTED**で、source保存保持からv3互換性を推測しない。
- TaCZ 1.1.7-hotfix2の新Ammo Conservation Adapterを用いた実client操作/予測/HUD同期、custom gunpack/他version/他addon統合。9/8のAdapter実装前baselineと9/9の自動回帰を、この未実施範囲へ拡張しない。
- Break Realm対象のL2 Hostility、Trial Monolith、Hyperlink/Fumetsu Wither、Draconic Evolution/Chaos Guardian、Bloodbath/Bloodbath Godzilla、その他公式MODpack高難度bossは`SPECIFIED / NOT IMPLEMENTED / NOT YET STATIC AUDITED / NOT YET INTEGRATION TESTED`。対象versionのJar/source/API監査前は具体的内部IDを未確定とする。
- 大量進行値/極端modifier/long境界は今回の限定自動fixtureでPASS。長時間・大量player・大型MODpackでのTPS、GC、長期メモリ、実負荷性能は未試験で、5分soakを長時間安定性PASSとはしない。

### 旧集計：未解決問題

> 履歴の集計です。現在の判定・未完了・次の作業は[現在の要約](#現在の要約)を参照してください。

#### RESOLVED / LOCKED - 2026-09-08利用者決定

- `OPEN-01`: 正常legacy Lv L/count Cを保持し、未使用SP L / 使用済0の全返還respec。旧skill自動取得/費用控除なし、raw backup永続保持。以前のraw-only/SP0保留は履歴として残す。
- `OPEN-05`: Gathering/Unbreaking/Pursuit旧sublevelを自動変換しない。旧情報はrawへ完全保持し、返還SPからfresh Lv1 nodeを通常cost2/3/4で購入。効果・toggle・倍率は未変更。

#### 旧集計：OPEN - 仕様決定待ち

> 履歴の集計です。現在の判定・未完了・次の作業は[現在の要約](#現在の要約)を参照してください。

- `OPEN-02`: Purification Masteryの正式な前提スキル。
- `OPEN-03`: Truth Masteryの正式な前提スキル。
- `OPEN-04`: SuperbWarfare互換時の正式な倍率・計算規則。
- 確認事項（未LOCK）: True Root ON中に作成済みの予約がある状態で途中OFFした場合、既存予約を破棄するか、再ONまで保持するか。今回の監査では決定していない。

#### 旧集計：RELEASE BLOCKER / 実装・検証待ち

> 履歴の集計です。現在の判定・未完了・次の作業は[現在の要約](#現在の要約)を参照してください。

- 実2-clientは`BLOCKED - SECOND MINECRAFT ACCOUNT REQUIRED`。アカウント制約は回避せず、検証未完了として保持する。別枠の実1-client dedicated試験はSTEP 1〜6を記録範囲で完了したが、実2-clientのrelease gateを代替解除しない。
- 実v2.2.5 backup/provenance/capability保持/別JVMfixture、認証済みCURRENT source load/save、実v3初回migration・正常保存、第二boot冪等性に加え、**同じV3M0908の第三boot限定Gameplay Smokeも9/8に保存差分付き限定PASS**。人間の食事/室内移動とCodexのGUI購入/toggleを分離し、ジャンプ数値・全移動の連続視覚観測は未確認。既知ERRORの全機能影響・全MOD ownership/gameplay・歴史的byte同一性・認証済みhistorical Wrapper組合せは未確認。過去試験UUID NOT PASS・metadata差分未解明を維持し、全MOD互換や任意環境の安全性へ拡張しない。第四boot未承認。
- BUG-01〜05は報告範囲で`MANUAL TESTED / PASS`。BUG-02は通常/複数行chat、chat非表示、`T`/`/`入力中、GUI Scale Auto/1/2/3/4のvanilla追加手動試験を完了した。BUG-04はFood Productionのnormal/shift click、2x2/3x3、OFF/未取得、材料消費、Stack上限/overflowを手動PASSとし、今回予定のvanilla crafting追加確認を完了した。
- GUI entryは通常Inventory + `S`、Creative Inventory + `S`、他containerの誤起動なしの三境界をすべて実クライアントで`MANUAL TESTED / PASS`。Creativeの修正前手動FAILは解決履歴として保持する。
- 通常Heroics Lv1〜5のoutgoing x2.0 / x2.5 / x3.0 / x4.0 / x5.0、専用DR 10% / 20% / 30% / 40% / 50%、非累積final effect、toggle OFF時の専用DR解除は実クライアント数値試験でPASS。Armor/Toughnessも`minecraft:mob_attack`の実damageでLv1 x2 / Lv2 x4 / Lv3 x8 / Lv4 x16 / Lv5 x32が理論値と一致し、`MANUAL NUMERIC TESTED / PASS`。
- True Rootは親Root依存と予約toggleを、True Heroicsは購入、toggle/80%閾値、99% DR、x20 outgoing、実Creeper防御、direct-death非保護を実クライアント確認し、`MANUAL TESTED / PASS`。未使用/使用済みSP、True Root / True Heroics取得状態とtoggleの再ログイン保持もPASS。True Heroics Armor/Toughness x64も2026-09-05のbase 0.25/0.25・HP70%での実damage数値切り分けにより`MANUAL NUMERIC TESTED / PASS`。既存の自動PASSおよび旧Creeper合成防御PASSとは別の確認履歴とする。
- Food Healing所有flightを、安全に識別・解除する完全なownership方式が未完成。関連購入は安全側で停止中。
- Purification/Truthの前提モデルはdata-driven化済みだが、`OPEN-02`、`OPEN-03`の追加前提が未決定のため購入は停止中。強制死と正当な数値damage死を区別するAdapter、対応trait所有MODの安全なAPI/Adapterも未実装。
- 負値/型不正/未対応schemaや、rawと一致しない編集済みpending schema4は安全側で移行保留。これらの自動修復・v3編集値との合算/置換規則は推測しない。正常legacyのOPEN-01/05 gateとは分離する。
- Food Production Masteryのvanilla craftingは自動試験に加え、normal/shift click、2x2/3x3、OFF/未取得、材料消費、Stack上限/overflowを実クライアント手動PASS。Farmer's Delight標準Cooking Pot、addon recipe、container output、multiplayer共有machineは実MOD統合試験待ちのまま。
- Break Realm Adapter expansionは`SPECIFIED / NOT IMPLEMENTED`で既存gate維持。TaCZ Ammo Conservationの9/9 heat判断待ちは利用者決定でRESOLVED / LOCKEDとなり、Adapterは**IMPLEMENTED + AUTOMATED INTEGRATION TESTED**。exact1.1.7-hotfix2＋readyのみ購入可能、Lv/SP/倍率不変。新Adapterの実client操作/heat HUD/予測はまだNOT RUN。過去PRE-IMPLEMENTATION STOPの理由と記録はフェーズ履歴に保持する。
- optional MODはTaCZ指定versionの限定自動統合と9/8実銃baselineだけ実施済み。他MODのclassloading/ownership/event重複、TaCZ新Adapter client/prediction/他packは未検証。
- End出口portal block→WIN_GAME→PERFORM_RESPAWNのserver-side経路と別JVMdedicated restartは自動化済み。credits画面の実表示/操作、実2-playerの再接続は未実施。実world初回v3移行/saveと同じcopyの2回目boot/reload/saveは9/8にそれぞれ限定PASS。
- `super_food` / `normal_test_food`の既存item model欠落warning。registry削除や外観の推測置換はしていない。通常food/既報GUI/HUDの手動FAILとは混同しない。

### 旧集計：互換性リスク

> 履歴の集計です。現在の判定・未完了・次の作業は[現在の要約](#現在の要約)を参照してください。

- Food Healing管理effectはtoggle解除後に短い残存時間があり得る。外部effectを誤削除しないことを優先した設計で、最大約12秒で自然終了する。
- Crafting/Furnace/CampfireのMixinはvanilla経路に適用済みだが、独自menuや独自result slotを持つMODは専用Adapterまたは統合試験が必要。
- 正常legacyは承認済み1:1全返還を行い、旧rawを完全保持する。無効/曖昧な入力はpendingを維持。実旧worldの隔離copyで初回v3 migration/saveと同じcopyの2回目boot/reload/saveの冪等性は各承認範囲で検証済み。fixture/この2回の保存結果から全MOD保存互換性・任意環境の再起動安全性へ拡張しない。
- コンパイル成功は外部MOD互換性の保証ではない。TaCZの指定version/限定fixtureのみ`AUTOMATED INTEGRATION TESTED`とし、他のversion/組合せ/未実施動作は`NOT YET TESTED`を維持する。

### 旧集計：次の作業（現在の実行指示ではない）

> 履歴の集計です。現在の判定・未完了・次の作業は[現在の要約](#現在の要約)を参照してください。

1. 次回開始時に最新の`docs/CODEX_STATUS.md`を再確認し、残る`RELEASE BLOCKER`、`NOT YET TESTED`、外部MOD統合、`OPEN` / `NOT LOCKED`項目を整理してから優先作業を再開する。`OPEN` / `NOT LOCKED`は推測で決定しない。
2. **完了 / MANUAL TESTED / PASS**: Root固定窓の期限非延長。Root Lv5 ON / True Root OFF、Nutrition6、食事完了0/約12/約26秒で3食目も非発動を確認した。今回の短い手動回帰は完了とし、次は項目3の既存予定へ進む。
3. OPEN-01/05のrespec実装/fixture/別JVM/成果物監査、v3 worldless smoke、初回migration/save、第二boot冪等性に続き、**同じV3M0908のTHIRD BOOT / LIMITED REAL-CLIENT PLAYABILITY SMOKE / PASS WITH RECORDED GAMEPLAY DELTASを9/8に記録して完了**。返還SPによるRoot Lv1通常購入・ON→OFF→ON・1食count35→36・人間の室内移動を主体別に記録し、保存後未使用1/使用済1/Root ON/raw/Diversity保持・原本/cold保全を確認。Minecraftは通常終了済み、同じ隔離Prismのみ継続。**承認範囲終了、完全停止。FOURTH BOOT = NOT RUN / NOT AUTHORIZED。追加copy・4回目起動・別試験へ自動進行しない。** 既存MANUAL PASS・実1-client STEP1〜6を再試験待ちへ戻さない。過去NOT PASS、既知ERROR/thread例外、metadata差分未解明、historical-wrapper authenticated combination NOT TESTED、実2-client BLOCKEDを維持。原本/pristine/R0908/source2.2.5比較instance/承認metadata/source runtime/第二保存cold backupを保持し、次の利用者指示を待つ。TaCZ実銃・未完成Adapter・保留機能には今回着手しない。
4. **9/8の別途承認でTaCZ 1.1.7-hotfix2 default pack実銃fire/reload/通常auto-bolt/heat lock・冷却後復帰を限定INTEGRATION TESTED / PASSとして完了**。既存13ケース自動PASSとは別証拠で、主体はCOMPUTER USE＋過熱連射HUMAN。通常保存/終了・原本保全も完了し、今回の承認範囲を終了して停止する。全gunpack/全TaCZ互換・Ammo Conservation実装完了へ拡張せず、Adapter実装や追加試験には新しい利用者指示なしで進まない。上記項目3の「TaCZへ着手しない」は第三boot承認当時の境界として保持。他MODは正式version/Jar/source提供後に静的監査→自動統合→手動統合を分離する。Flight ownershipやAmmo Conservation未完成Adapterの購入gateは今回のbaseline PASSだけで解除しない。**9/9追記: 新しい明示承認を受領したが、Ammo Conservationの既存heat/lockに対する有効化時の扱いが未定義のため実装前停止。次はこの仕様判断だけを待ち、決定後に実call path監査と承認済み自動実装/検証へ戻る。新しい実client試験はまだ開始しない。**
5. OPEN-02/03/04、True Root予約済み途中OFFの扱いは利用者決定待ち。OPEN-01/05はRESOLVED / LOCKED。Break Realmは既存gateを維持。crossbowは全デバッグ後も`DEFERRED / NOT IMPLEMENTED / WAITING FOR USER START APPROVAL`であり自動開始しない。

**9/9終了checkpoint（上の日時付き旧予定を更新する現在地）**: 項目4末尾のheat仕様判断待ちは今回の明示決定で解決し、承認されたAdapter実装・全自動回帰・別JVM・成果物監査まで完了した。次は新しい利用者指示を待つ。実client Ammo Conservationは未承認/NOT RUNのまま自動起動せず、既存MANUAL PASSを再試験へ戻さない。実2-client BLOCKED、OPEN-02/03/04、True Root未LOCK、他MOD artifact/統合gateを解除しない。V3M0908 FOURTH BOOT、Flight、Break Realm、試作型機関弩、Bulwark、他MODへ進まず完全停止。

**9/9 22:04終了checkpoint（直前の自動試験終了時点より後の現在地）**: 追加の明示承認を受け、TaCZ1.1.7-hotfix2限定の新規隔離runClientでAmmo Conservation通常購入・射撃・reload・heatを主体別に確認した。TEST HはGUI pause保存に自然過熱360/lock1が残ることを確認後、通常OFF→ON→再射撃→heat0/lock0・弾数/他銃保持を画面/保存で確認した限定PASS。正常保存・Minecraft終了・runClient BUILD SUCCESSFUL・最終NBT/log照合・原本保全まで完了。Bの購入時過熱解除はINCONCLUSIVE、Cの厳密shot計数とEのbolt/packet/prediction内部の未観測範囲、任意hot-gun持替えSKIPPEDを残し、REAL CLIENT CORE全条件PASSやALL TACZ/GUNPACKへ拡張しない。次は最新Statusを確認して利用者の指示を待つ。追加human試験・実装・world起動は自動開始しない。既存自動/手動PASS、実2-client BLOCKED、OPEN-02/03/04、True Root未LOCK、他MOD/gate・配布Jar直接Prism未試験・dedicated latency未試験を維持。V3M0908 FOURTH BOOT、Flight、Break Realm、試作型機関弩、Bulwark、他MODへ進まず完全停止。


<a id="evidence-l2-ww-initialization-20260918"></a>

### 2026-09-18 21:12 JST — weakness/wither新runの初期化不一致と通常終了

run20260918-204657。helper限定offline compile/reobf成功、40,428 bytes/18 entries/34BAFA60…76E9。初期通常AI→自然接地→NoAI固定の手順とobserver帰属キーを修正したが、旧`reinit(z,1,false)`を残したためL2 lv1/MAX・HP20.6となり、gameTime4037の最初のsource snapshotで停止。prepare1・本人3取得/SP0・603/P/M ON・T OFF、trait付与0/NoAI切替0/native hit0。GUI/A/B/T/C/sealはNOT RUN、製品効果FAILではない。個別operand/原因/実施主体/全証拠は[共通計画§13.9](MASTERY_IMPLEMENTATION_PREPARATION.md#l2-weakness-wither-client-initialization-mismatch)。

21:00:22.883全保存、保存canonical2系統一致/本人HP20・source lv1/HP20.6をreadonly照合、21:01:13.246 Quit、21:01:14.299 PID27036不在。範囲外/再読込/cleanup/修復/再試行0。[実測集計](../build/verification/l2-weakness-wither-client-20260918-204657/audit/stopped-run-analysis.json)、[不変照合](../build/verification/l2-weakness-wither-client-20260918-204657/audit/final-invariants.json)。製品146件/旧証拠304件/旧2 run398件/旧instance metadata22件不変。製品Jar5C1A716E…DB327不変、既存PASSとgate維持。今回の失敗runを修正せず保全し、次は別runの初期level0準備に限定する承認待ち。


<a id="evidence-uom-reconnect-startup-20260921"></a>
### 2026-09-21 11:52 JST — UOM real reconnect実行：client shader起動失敗でR1前停止

**今回：承認されたreal reconnect補助を作成・限定offline compile/reobf。実clientはFantasy Ending 2.7.20のshader登録で起動失敗し、R1開始前に停止。R1–R9全てNOT RUN**。[実行結果](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-real-reconnect-startup-result) / [判定集計](../build/verification/uom-reconnect-20260921-112109/audit/reviewed-results.json)。新run `20260921-112109`、新server/worldと新Prism instanceだけ使用。serverは指定MODを実ロードし、通常save-all flush/stop・exit0。clientは`MShaderInstance.getUniform:118`で`Uniform -> MUniform`のClassCastException、Prism終了コード−1（正常Quitではない）。実TCP/login・S1/S2/S3・prepare・UOM/native開始・Grant・歩行・sealは0/未確認。R4同期不一致も今回未到達。製品229,494 bytes/150 entries/hash・source/test/build.gradle/gate不変。**SAFE DESIGN PROVEN=NO / ownership BLOCKED継続**。同client再起動・world再読込・外部MOD改変/取得なし。

補助初回compile3件の記述ミスと初回server Mixin適用失敗を保全し、world生成前に補助だけ修正。最終補助58,256 bytes/35 entries、SHA256 `D83C8A675EEF543AAF5D0D12ABFF8EA8AFD089603571E76F3DC6652F58DDA9ED`。これは製品Jarではない。[専用compile03](../build/verification/uom-reconnect-20260921-112109/audit/compile-03.log) / [補助監査](../build/verification/uom-reconnect-20260921-112109/audit/helper-audit.json) / [client crash](../build/verification/uom-reconnect-20260921-112109/audit/client-startup-failure/crash-2026-09-21_11.43.26-client.txt) / [server終了](../build/verification/uom-reconnect-20260921-112109/audit/server-process-02.json) / [process不在](../build/verification/uom-reconnect-20260921-112109/audit/process-after.json)。製品/既存ソース等144ファイルSHA一致、文書変更前は3文書を含む147ファイル一致。変更前文書は新run beforeに保全。実施主体はAUTOMATED準備・観測＋COMPUTER USEのPrism通常起動、HUMAN操作0。既存suite再実行0、既存PASSと全gateを維持。


### 2026-09-21 12:11 JST — FE client shader READ ONLY原因切り分け

**今回：FE2.7.20 shader起動失敗のREAD ONLY原因切り分け完了（A：MUniform生成/cast契約不整合）**。[共通計画§14.17](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-shader-readonly-diagnosis) / [照合結果](../build/verification/shader-readonly-20260921-115707/reviewed-results.json)。実変換ShaderInstanceのprivate `m_173354_(JsonElement)`が通常Uniformを生成し、FE側の同名public parserへ分岐しない。constructor内ModelViewMat取得→FE bridge/getterのMUniform castで失敗。shader用Mixinは適用済みだが生成差替なし。追加必須artifactなし、他MOD競合/EndingLibrary版不整合/helper原因の証拠なし。修正・再起動・新試験0。R1–R9 NOT RUN、認証/TCP UNVERIFIED、prepare/UOM/native use/Grant0。製品・承認6原物・補助/gate不変、SAFE DESIGN PROVEN=NO / ownership BLOCKED継続。

変更前3文書は[今回before](../build/verification/shader-readonly-20260921-115707/before/docs/)へ保全。調査用ZIP/class抽出・javap・hash照合と文書更新だけで、helper追加/修正や起動/build/testは0。詳細は共通計画§14.17へ集約。


<a id="evidence-fe-shader-design-20260921"></a>
## 過去の履歴 — 2026-09-21 12:37 JST / FE独立shader patch設計

- 承認範囲：READ ONLY設計・介入点・検証条件と3文書更新のみ。§14.17の原因Aを前提とし、constructor安全性/Mixin0.8.5の該当規則/早期版判定API/native FE登録・描画入口を限定読取。原因の再診断、patch/fixture/observer source作成・compile/適用・client/server/Prism/world起動・download・R1–R9は0。
- 推薦B：親ResourceLocation constructorのparser call1か所を、plain bridge経由でFE native public parserへ接続。MC1.20.1/Forge47.4.0/FE2.7.20原物・2 receiver型/5既存shader組を限定。親private parserは対象外fallbackとして保全。Aのint型だけのfactory置換/Cの後再構築/Dのglobal公開化は不採用。
- [共通計画§14.18](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-shader-compat-design)にexact descriptor・require/expect/allow/priority/remap・constructor/list/map/location/type・optional/dedicated・rollback・22uniform検査を記録。[TEST_PLAN §27](TEST_PLAN.md#fe-shader-compat-planned-tests)へSTATIC-S/STARTUP-S1/VISUAL-V1を計画。全NOT RUN。ARTIFACT REQUIRED=NO、実装/起動は次回承認待ち。
- 記録：[読取と保全](../build/verification/shader-patch-design-20260921-121944/)、[設計根拠](../build/verification/shader-patch-design-20260921-121944/design-evidence.json)、[保存後照合](../build/verification/shader-patch-design-20260921-121944/final-checks.json)。変更前3文書をbefore/docsへ保全。git未配置のためgit diff実施とは記録しない。
- 製品229,494 bytes/150 entries/SHA256 `5C1A716EEDE7AEB4449B36EEFFBDDA156176B16B60A1676229224AA3390DB327`、reconnect helper58,256 bytes/35 entries/D83C8A67…DDA9EDと承認6原物不変。既存source/test/build.gradle/Config/購入gate変更0、新Jar0。旧crash・R1–R9 NOT RUN/認証TCP UNVERIFIED、既存全限定PASSを維持。
- 設計完了は修復/起動/TimeStop安全性PASSではない。SAFE DESIGN PROVEN=NO、SOURCE DIMENSION TRANSITION VERIFIED=NO、ownership BLOCKED、P/T pending/購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、他個別gateと可逆クラフト増加の既知許容仕様を維持して終了。

<a id="evidence-fe-shader-execution-20260921"></a>
## 過去の履歴 — 2026-09-21 13:38 JST / 独立FE shader patch・限定実client完了

**今回：独立FE client shader互換patchを作成し、STATIC-S PASS / STARTUP-S1 PASS / VISUAL-V1 cosmic GUI限定PASS。通常Quit・exit0・PID終了まで完了**。[共通計画§14.19](MASTERY_IMPLEMENTATION_PREPARATION.md#fe-shader-compat-execution-result) / [判定集計](../build/verification/fe-uniform-compat-20260921-125321/audit/reviewed-results.json)。run20260921-125321、5shader consumer/link完走、cosmic22uniform型・GL型・同一object一致、native draw/bind/apply/flushの3sampleとtime進行、GL照会38回すべて0。製品/承認6原物/reconnect helper/hash・gate不変。world/server/接続なし、R1–R9 NOT RUN。追加artifact不要。SAFE DESIGN PROVEN=NO / SOURCE DIMENSION TRANSITION VERIFIED=NO / ownership BLOCKEDを維持。

- 独立patch `fe_uniform_compat` / `0.1.0-verification.20260921.125321`：14,270 bytes /16 entries、SHA256 `BD2322818FDC65F4F20FD8332200CD6AED02F00487E3E1D8868D2865AD4B58D0`。別observer19,267 bytes/19 entries。製品へ混入0。
- COMPUTER USE：既存Prismの一覧再読込、新instance起動、metadata警告からtitle、専用Screen、title→Quit、終了コード0確認。AUTOMATED：専用offline build/static、readonly stage/GL/3frame・hash/process照合。HUMAN操作0。world作成/server/旧run起動0。
- 13:31:27 JST `Stopping!`、Prism exit0、PID18056終了。source141ファイルを含む保全対象146件不変、承認原物10件・配置9本一致。新instanceのMOD既定Config生成はnative処理、既存/製品Configやcosmic renderer設定の編集0。製品229,494 bytes/150 entries/5C1A716E…DB327を維持。
- patch/observerのpack.mcmeta欠落WARN、旧FE bladeモデルERROR、旧asset/sound等のWARN、旧crash後に初到達したcosmic_2 Sampler2警告を隠さず記録。新規shader ERROR0であり全ERROR/WARN0ではない。compile/fixture準備失敗と修正の詳細は§14.19、全log保全。ゲーム内repair/再起動なし。
- 変更前3文書は[before/docs](../build/verification/fe-uniform-compat-20260921-125321/before/docs/)へ保全。§14.16/17/18、旧自動suite・限定実client PASSを履歴として保持。git未配置のためgit diff確認とは記録しない。
- FE6 MobEffect site STATIC AUDITED / NOT IMPLEMENTED、TimeStop production NOT IMPLEMENTED、FE production integration 0 / NOT RUN、P/T IMPLEMENTATION_PENDING・購入停止/SP保護、SAFE DESIGN PROVEN=NO、SOURCE DIMENSION TRANSITION VERIFIED=NO、BLOCKED - TIME STOP SOURCE OWNERSHIP、RC=NO、REAL2CLIENT=BLOCKEDを維持。 個別開始条件と食料生産の極意による可逆クラフト増加の既知許容仕様・バグ修正対象外を維持。**次の1作業：別の明示承認後、新runで既存§14.15のR1–R9 real reconnect確認を行う。** shader起動/限定GUI描画gateは今回充足。完成済みreconnect helperを作り直さず、旧instance/worldは再利用しない。追加artifact不要。今回の承認はここで終了し、dedicated接続・認証/TCP reconnect・UOM/TimeStop・prepare/Grantは開始しない。

<a id="evidence-uom-reconnect-preflight-20260921"></a>
## 過去の履歴 — 2026-09-21 14:02 JST / 新run再利用可否の開始前照合でSTOP

**今回：承認済みR1–R9の開始前照合でSTOP。既存reconnect helperのRUN/ROOTが旧runへ固定され、新runを既存外部設定では受け付けないことを実Jar bytecodeで確認**。[共通計画§14.20](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-reconnect-preflight-hardcoded-root) / [照合結果](../build/verification/uom-reconnect-20260921-135926/audit/preflight-result.json)。helper/patch/製品/外部6原物の全9hashは一致。source変更・再compile禁止に従い、新server/world/instance作成・起動前で停止。R1–R9各NOT RUN、auth/TCP UNVERIFIED。§14.19のshader3段階PASSは維持し再試験0。SAFE DESIGN PROVEN=NO / ownership BLOCKEDを維持。

- 記録ID `20260921-135926` は[preflight専用記録root](../build/verification/uom-reconnect-20260921-135926/)であり、実行run開始ではない。新instance/server/world、case FIRING/DONE、prepare、S1/S2/S3、G1/G2、native use、歩行、接続、通常save/stop/Quitはすべて未実施/非該当。実ロードMOD結果は今回なし、原物識別/hash照合と分ける。
- `Audit.RUN`はcompile-time constant、`Audit.ROOT`は旧絶対パス。server/world realpath・Jar照合先・receipt/journalをそこへ束縛。client lease受理・S2C run・session記録にも旧IDがldcで埋込み。既存System property/env/config入口なし。sourceと実Jar両方で確認。コピー先/cwd変更では解決せず、旧path alias/junction/反射/bytecode改変を使わない。
- 前回「完成済みhelperをそのまま新runへ使える」と案内した点は不正確。§14.19 shader検証結果は正しく保持し、今回新たに判明したhelper可搬性不足を別blockerとして記録。ユーザー指定の停止条件であり、OS/自動承認拒否や認証失敗ではない。
- 共通計画§2「FE 終焉の守護者」不足欄を§14.19完了＋今回helper停止へ訂正。過去§14.18の未実装/NOT RUN、旧crash/失敗/PASSを維持。変更前3文書は[before/docs](../build/verification/uom-reconnect-20260921-135926/before/docs/)へ保全。
- **次の1作業：reconnect helperのRUN/ROOT外部設定化と初回書込前の新run限定検証について、最小修正・専用offline compile/reobfの別承認を受ける。** R1–R9の実行自体は今回承認済みだが、利用者指定の「既存設定で新root不可ならsource変更せずSTOP」に該当した。旧helper/Jar/runは保全し、新root側だけで修正する候補。session/Grant/lifecycle設計、製品、shader patchは変更しない。追加外部artifact不要。 FE6 site STATIC AUDITED / NOT IMPLEMENTED、TimeStop production NOT IMPLEMENTED、FE production integration 0 / NOT RUN、両極意IMPLEMENTATION_PENDING・購入停止/SP保護、SAFE DESIGN PROVEN=NO、SOURCE DIMENSION TRANSITION VERIFIED=NO、BLOCKED - TIME STOP SOURCE OWNERSHIP、RC=NO、REAL2CLIENT=BLOCKEDを維持。 個別gate、食料生産の極意による可逆クラフト増加の既知許容仕様・バグ修正対象外を維持。


### 2026-09-21 14:40 JST — reconnect新helper完了・R1距離guard STOP

**今回：新reconnect helperのRUN/ROOT設定化・限定offline compile/reobf・初回write guard検査を完了。実認証/TCPのS1→S2を実測したが、R1歩行2.796093 blocksが上限2を超えFAILで停止。R2–R9は各NOT RUN**。same UUID/new player・listener・Connection・channelと空cache/Grantなしは確認。停止後約0.303秒のsampleでclient/server距離0、HP20/MAX20/effects空を保持。操作案内が距離上限を具体化していなかった測定準備上の不足であり、製品不具合の証拠ではない。期待値・receiptを変えず、prepare/UOM/native use/Grant0のまま通常Disconnect→保存/stop・exit0→Quit/PID終了・readonly保存照合を完了。[共通計画§14.21](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-reconnect-portable-helper-r1-stop) / [今回の証拠](../build/verification/uom-reconnect-20260921-141541/audit/reviewed-results.json)。製品・旧helper・shader patch・承認6原物不変。SAFE DESIGN PROVEN=NO / ownership BLOCKED継続。

- helper version20260921.141541、63,693 bytes/37 entries/SHA256 `CBA3052E6BD034F7FC3E84D99B04AF5515999430F3608A4017E7C04ACAE6594A`。専用compile/reobf1回成功、repair cycle0、途中build FAIL0。製品build/旧suite0。
- 保全206ファイル・9原物・配置17 Jarはhash不変。S1/S2はnative logoutで失効、G1/G2なし、S3/R4未到達。server PID29136/client PID28196終了。通常保存NBTはHP20・effects空・未取得・SP0/0・pending0。
- **次の1作業：距離上限を人間の押下時間に依存させない新run用の短い歩行区画（物理的な停止位置を0.05–2 blocks内に配置）を具体化し、別の明示承認後に未完了R1–R9を実行する。** 今回の失敗run/receiptは保全し再利用・リセットしない。helperの設定化/初回write guardは完了済みで再設計不要。新run identity/version/manifestの差替と必要なhelper限定buildだけを行う候補。R1条件、session/Grant/ownership設計、製品・shader patchを変更せず、追加外部artifact不要。
- P/T IMPLEMENTATION_PENDING・購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、全既存限定PASS/個別開始条件、食料生産の極意による可逆クラフト増加の既知許容仕様・バグ修正対象外を維持。

### 2026-09-21 15:21 JST — 短距離区画でR1〜R3成立・R4 native同期STOP

結果・個別R採否・終了証拠は[共通計画§14.22](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-reconnect-bounded-walk-native-sync-stop)へ集約。旧run20260921-141541の距離FAILを変更せず、新runのR1/R2は物理境界内で成立。R4で正本の停止分岐に従い、後続caseを実行していない。3文書は正常終了・readonly照合後に今回一度だけ更新。

### 2026-09-21 15:44 JST — R4 READ ONLY原因切り分け

- **今回：R4のREAD ONLY原因切り分け完了。主因A＝EndingLibrary2.1.19fixのloginはglobalだけ送信しdimension同期を欠く。補足B＝dimension送信はnative use開始/終了だけで、S3滞在中は呼出し0**。[共通計画§14.23](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-r4-native-login-sync-diagnosis)。実Jar全send site・field書込・実ロード後classと保存ログを照合し、helper/shader干渉・client level未準備を今回の原因から除外。R1–R3 LIMITED PASS / R4 native同期STOP / R5–R9各NOT RUNを維持。独立compatは設計候補のみ、実装・補送・起動・再試験0、製品/原物/旧run不変。
- native send siteはSkill3・Dimension2、field書込3。S3のuse0/Dimension処理0と一致。wire送信時刻とclient constructor瞬間は未計測として分離。[照合証拠](../build/verification/uom-r4-readonly-20260921-153506/diagnosis-evidence.json)。
- 現在の未完了表の旧§14.20行をR4 STOPへ訂正。過去履歴・R1–R3 PASS・全既存suiteを保持。独立compatは設計のみ、今回のprotected380ファイル不変。
- **次の1作業：§14.23の「native loginのglobal送信直後、既存dimension stateを本人へ同期する独立compat候補」の実装・限定検証について利用者承認を受ける。** 現在は設計のみで、patch/helper作成・compile・packet補送・ゲーム起動・R1–R9再試験は開始しない。既存artifactで着手可能、新規外部artifact不要。native state、session/Grant/UNKNOWN、P/T仕様・製品・shader patchを変更せず、R1–R3/既存suiteを未実施へ戻さない。R4以降の実client実行は別途明示承認された範囲に限る。


### 2026-09-21 21:59 JST — D1 2 UOM自然terminal・機能継続FAIL

**今回：D1を新規dedicated/world 1 runで実行し、U1の自然terminalで「機能継続FAIL / SAFE-SIDE DENY」を実測。通常保存・stop・exit0まで完了**。[共通計画§14.26](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-terminal-d1-result) / [証拠照合](../build/verification/uom-terminal-20260921-214412/audit/reviewed-results.json)。run20260921-214412、開始count40/80、U1の1→0→native use(false)入口でUNKNOWN化。U2はcount40・global/dimension停止継続、epoch1のまま、Grant sequence1は同tick ENDで失効。追加ALLOW/再発行なし。U2自然terminal・自然clean5tickはFAIL時停止によりUNVERIFIED、cleanupの0をPASSへ転記しない。helperのみoffline compile/reobf、機械的repair1回、製品/原Jar/旧helper/compat・購入gate不変。§14.24と旧PASSを維持。SAFE DESIGN PROVEN=NO / ownership BLOCKED。

**次の1作業：D1の保存済み証拠を基に、既知sourceのnative count1→0からuse(false)までを帰属できる最小変更候補をREAD ONLYで設計する。** 未観測の消失・direct変更・FOREIGNを引き続き拒否できる条件を比較し、UNKNOWNの一律解除やGrant再発行で回避しない。今回は機能継続FAILを保全して終了。[§14.26](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-terminal-d1-result)。設計変更の実装・新run再試験・A/B/C/E/F/G・製品写像へは自動で進まない。追加外部artifact不足なし、UOM/P vehicleは未LOCK。


### 2026-09-21 22:29 JST — D1 terminal帰属のREAD ONLY設計

**今回：D1の保存済み反例をREAD ONLY解析し、A/B簡易除外案を退け、C「exact native terminalの一回限りtransient witness」を最小変更候補として設計。未実装・未検証**。[共通計画§14.27](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-terminal-witness-design)。同objectの開始時binding、native callback内の1→0実確認、対応use(false)の一回消費、RETURN後commit、例外/欠落時fail-closedを条件とする。最終sourceのlive空判定・quietによるUNKNOWN消去にも限定guardが必要。次回候補はD1-Wという1単位だけ。§14.26の機能継続FAIL / SAFE-SIDE DENY、U2自然終了/clean5tick UNVERIFIED、cleanup/通常保存・exit0は変更しない。新run/helper変更/compile/ゲーム/製品実装0、SAFE DESIGN PROVEN=NO・ownership BLOCKED。


### D1-W限定検証とW3環境STOP — 2026-09-21 23:03 JST

**今回：D1-Wの別helper実装・限定offline compile/reobf、STATIC PASS、有限negative契約10/10。W1正常2→1→0とW2 direct0拒否はLIMITED PASS。W3は検証環境不備でUNVERIFIEDとなりSTOP、W4 NOT RUN**。[共通計画§14.28](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-terminal-witness-result)。W3のCowは`spawn-animals=false`によるnative除去と整合し、入力前からremoved/count0。FOREIGN参加が未成立のため、拒否PASS/危険な誤ALLOWのどちらへも転記しない。開始した3serverは通常保存・exit0/PID終了、製品/旧証拠不変。旧§14.26 FAILと§14.27設計を維持。SAFE DESIGN PROVEN=NO・ownership BLOCKED。

[集計/証拠](../build/verification/uom-terminal-witness-20260921-224127/audit/reviewed-results.json)。helper `uom_witness_verification` /20260921.224127、75,138 bytes/33 entries、SHA-256 `D66F840F9C1E77812B62E880175058A48B1002EC41877825B6834821FD560A8D`。witness作成/消費/commitはW1=2/2/2、W2=0/0/0、W3=0/0/0、W4未実施。regrant/new epoch0、機械的repair0（native DLL権限エラーはコードcompile前・既存offline権限実行へ移行）。P/M1ON・SP0/使用済103・HP20/effects空を3保存で照合、witness/Grant保存キーなし。3process終了、旧705files不変。real client/既存suite/製品build0。

**次の1作業：別承認後、W3の検証環境とCow生存・loaded/count/帰属の事前guardだけを修正し、新規隔離runで未完了W3/W4を実施する。** [共通計画§14.28](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-terminal-witness-result)。新規serverではCowをnativeに保持できる設定（`spawn-animals=true`と生成前/準備前の`doMobSpawning=false`を区別）を採り、native入力前の対象ref/alive/removed/level/cap確認を必須化する候補。正常回復はfalse、安全区画は維持。witness/認可条件を緩めず、W1/W2・旧D1/既存PASSは再試験しない。今回停止したW3の修復/再利用、同run再送、製品実装は行わない。追加外部artifact不足なし、UOM/P vehicle未LOCK。


### 2026-09-22 13:42 JST — D1-W W3/W4完了記録の保持

**今回：D1-W未完了W3/W4を新run `20260922-132519` の別dedicated/worldで各1回実施し、双方LIMITED PASS。既存W1/W2・STATIC・有限negative契約10/10を再実行せず保持し、D1-W候補の限定検証を完了**。[共通計画§14.29](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-terminal-witness-followup-result) / [独立証拠照合](../build/verification/uom-terminal-witness-followup-20260922-132519/audit/reviewed-results.json)。W3は同Cow positiveのままUOM自然terminal・誤ALLOW/再grant/新epoch0、W4は固定throw/native catch各1・witness1/0/0・cleanup後5 ENDのfault非回復。両server通常保存/stop/exit0、製品/旧証拠1,481files不変。旧§14.26 FAIL・§14.27設計・§14.28環境STOPは履歴保全。**SAFE DESIGN PROVEN=NO、ownership BLOCKEDを維持**。

### 2026-09-22 14:45 JST — process/world reload READ ONLY設計

**今回：process交換と同JVM world reloadを分けたREAD ONLY設計を完了**。[共通計画§14.30](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-process-world-reload-design)へ23状態の保存/寿命、native count/global/Set、旧認可非復活条件を集約。次候補はA1：新規world1・別JVM2 phaseのdedicated検証だけ。native positive復元からorigin/Grantを再構築しないことを確認する設計で、まだ未実行。現deserializeはcountに関係なくfaultを立てるため、安全側DENYと機能継続未完成を分ける。**D1-W STATIC/有限10・W1–W4 LIMITED PASS、R1–R9限定結果は保持・再試験0。SAFE DESIGN PROVEN=NO / ownership BLOCKED継続**。新run/helper/compile/ゲーム/製品変更0、追加外部artifact不足なし。

詳細状態表・A/B比較・A1 runbookと判定は共通計画§14.30を正本とする。新しい実行結果/PASSは追加していない。文書3件だけを最終更新し、製品/原物/候補helper/旧証拠は変更しない。

### 2026-09-29 08:29 JST — queue #5 Pam実装・主検証完了／補足環境STOP

現行集計は冒頭、証拠は[共通計画§14.59](MASTERY_IMPLEMENTATION_PREPARATION.md#pam-trees-50-recipes-result)。Trees138/absent103/core67×2成功を保持。補足Cropsのmissing Food Coreによるparse ERROR2で#5 PENDING・残5。全server通常終了、旧world/実client/外部download/#6開始0。変更前5文書・src・build.gradle・旧Jarは[before](../build/verification/pam-trees-20260929-080400/before/)に保全。

### 2026-09-29 09:04 JST — #5常時apple/cocoa・Pam49／Food Core補足artifact待ち

[§14.60](MASTERY_IMPLEMENTATION_PREPARATION.md#pam-vanilla-51-recipes-result)。canonical50を維持し49条件付き＋2常時を実装。present150/absent146/core67×2成功、補足7assert成立。旧Crops ERROR2解消、新Food Core自身のrecipe ERROR3で#5 PENDING。全5server通常保存終了、旧world/実client/download/#6開始0。開始前保全と証拠は新rootに保存。

### 過去の履歴 — 2026-09-29 22:38 JST pre-#8 SP再調整・採石/不動

run `skill-rebalance-20260929-220256`、finite2590、new unit602/既存parity320、vanilla109/TaCZ109/L2限定8、Jar **360,970 bytes / 275 entries / SHA-256 36A79E98C8E854DA79B82811EBD5DD57FE01CF4F7EDFC93296031DDFD8A265BE**。正常保存/停止、HUMAN INPUT0、#8/#9未開始。実装・FAIL保全・検証境界は[共通計画§14.70](MASTERY_IMPLEMENTATION_PREPARATION.md#skill-cost-rebalance-quarrying-immovable-completed)、現在一覧も同時更新済み。
