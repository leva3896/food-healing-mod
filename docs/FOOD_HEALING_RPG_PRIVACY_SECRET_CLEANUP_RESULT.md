# Food Healing RPG
# Public Repository Privacy & Secret Cleanup Result

2026-10-06 JST。Publication前のPrivacy Cleanup監査の履歴snapshot。以下のcurrent/remote/stage判定はこの監査時点の結果であり、後続publicationの成否を表さない。非公開証拠へのリンクはlocal専用。公開版では未公開gameplayの受入履歴とdevelopment candidate詳細を除外した。

## Status

**Primary B: CURRENT TREE CLEAN / NO CRITICAL SECRET DETECTED IN SCANNED SCOPE / PUBLIC REMOTE NEEDS CLEANUP PUBLICATION**。

**Secondary C: HISTORICAL PERSONAL IDENTIFIERS REMAIN**。ローカル公開候補は修正済み。公開default branchと過去commitは未修正。秘密情報の絶対不存在を宣言しない。今回commit/push/history rewrite/releaseは0。次は **Food Healing RPG Privacy Cleanup Publication — reviewed commit + public GitHub update**（利用者の明示承認後）。actual client工程はHOLD。

## Scope

全305 tracked files（303 UTF-8 text、2 binary）の現在内容、非ignoredの将来公開候補、filename、staged状態、all reachable local refsの32 commits/591 unique blobs、commit metadata、origin default/remote heads/tagsを監査。外部secret serviceへのupload0。

working tree docs/旧Result/Status履歴のprivacy redactionは今回の明示許可に従う。既存非公開build reports/worlds/logs/screenshots/backupsはrewrite/delete0。Minecraft/Prism/client/GUI/OS input/gameplay実装/buildは0。

## Baseline

repository `.`、branch `codex/release-v3.0.0`、HEAD `76e5fc4263b3986e6ba7a5c3a9ebc0ad3d40c17e`。intentional dirtyを保持。開始時434 publication candidates、stage0。reset/clean/discard/stash/unstageなし。


## Threat Classification

| 分類 | Severity | 扱い |
|---|---|---|
| A: CRITICAL SECRET | CRITICAL | 値を出力/保存/hash/試用せずpublication STOP |
| B: ACCOUNT / PERSONAL IDENTIFIER | HIGH | 個人account/email/実Player UUIDを匿名化 |
| C: LOCAL ENVIRONMENT IDENTIFIER | MEDIUM | username、home、絶対pathを相対path・placeholderへ |
| D: NETWORK IDENTIFIER | MEDIUM | 不要な識別IP/hostを除去 |
| E: INTENTIONALLY PUBLIC PROJECT IDENTITY | INFO | 公開repo URL・owner/name、MOD ID、Java package等を維持 |

severityがMEDIUMでも公開文書へ放置しない。一般token変数、SHA、version、生成entity UUIDをcredentialと混同しない。

## Secret Read Prohibition

launcher accounts、Microsoft/Minecraft credential DB、browser profile/cookies、Windows credential manager、Git credential helper file、private keys、`.env`、OAuth cacheを読んでいない。既存正常なGit read-only operationと無認証GitHub public APIのみ利用。scannerはsensitive filenameを内容読取前にblockする。tokenの有効性確認・外部送信0。

## Tracked Tree Audit

**最終 --tracked PASS / blocking0**。初回screenはtracked305と非ignored候補129を含む434files。secret形状候補0。個人home/absolute path/account文脈/UUIDを検出し、context review後に文書42filesを匿名化した。

最終は新guard/allowlist/policy/Resultを含む438候補を検査。pattern一致数は複数ruleが同じ行を数える場合があり、個人数ではない。[初回location-only scan](../build/reports/privacy-secret-cleanup/working-tree/before.json) / [最終tracked](../build/reports/privacy-secret-cleanup/tracked/final.json) / [最終候補](../build/reports/privacy-secret-cleanup/working-tree/final.json)。

## Filename Audit

tracked/current publication candidateのfilename自体に残るblocking hit0、privacy理由のrename0。MOD ID・package・resource/registry IDを維持。private evidenceがtrackedに入っていないことを確認。sensitive filename/symlinkは内容を無条件に読まない設計。

## Local Path Findings

文書のWindows user home・Gradle home・Downloads・Prism/launcher・repository/他workspace絶対pathをredact。初回redaction集計はlocal absolute215、home114のrule行一致（重複あり）。repo内pathは相対化し、root単独は `.`。外部MOD filename/bytes/version/SHAは保持。

[redaction台帳](../build/reports/privacy-secret-cleanup/redaction/summary.json)。実username/元絶対pathをこのResultに転載しない。

## Account Identifier Findings

contextから得た2種類の個人identifierをメモリ内だけで追跡し、公開URLを保護した上で反復箇所も匿名化した。アカウント値を台帳やallowlistへ書かず、globalなpackage renameを行っていない。現行レポートでは `<MINECRAFT_ACCOUNT>` 等を使用する。

## UUID Findings

直接のPlayer/本人/account文脈で識別した2種類のprivate UUIDを、文書中の反復を含め `<PLAYER_UUID>` へ置換。sourceの固定UUID集合と重ならず、test fixture変更は不要だった。

生成entity・Mixin session・vanilla/MOD owned modifier・synthetic fixtureは個人UUIDではない。70のreviewed technical UUID行をpath＋全行fingerprintで限定許可。別の値や行へ変更すると再レビューが必要。履歴画像のXMP RDF document UUID84 rule occurrencesもnonpersonalと確認した。

## Email Findings

現在の公開候補textにpersonal email検出0。commit metadataには4 commits/author-email4・committer-email4のreview対象が残る。noreply privacy identityはblockingから除外。値は保存/転載しない。[metadata counts](../build/reports/privacy-secret-cleanup/commit-metadata/summary.json)。

## Network Identifier Findings

現在の公開候補では、識別用public/private IP・machine/remote hostnameのblocking検出0。localhost、loopback、unspecified bindは非secret、MOD versionの点区切りをIPと誤判定しない。network contextによるheuristicであり、全形式のネットワーク識別子の不存在は保証しない。

## Credential / Secret Findings

監査対象のcurrent candidates、32 reachable commitsのtext/commit messages、bounded binary metadata/stringsで **CRITICAL SECRET候補0**。password/API key/PAT/OAuth/session/Bearer/Basic/private-key/GPG/AWS/JWT/Discord形式、quoted JSON assignment等を検査。

初期scanで新Python bytecodeに一致したものはscanner自身の合成self-test文字列だった。実credentialではなく、生成cacheをignoreして公開境界から除外した。外部MOD Jar内部は対象外。秘密値のecho・hash・検証API呼出し0。

## Commit Metadata Audit

**READ ONLY: 32 commits、4 affected commits、16field findings**（author name/email・committer name/email各4）。nameは個人/ローカルidentityの候補として記録し、実名と断定しない。noreply等のprivacy-preserving metadataは除外。

最古affected `3429a97bc74782b6c4ef1afccc606def7085e912`、最新affected `920c15a44828096cf1657961c560cc070e694250`。[hash/fieldのみの台帳](../build/reports/privacy-secret-cleanup/commit-metadata/findings.json)。名前/email値のコピー・Git author設定変更0。

## Git History Audit

**32 commits /591 unique blobs READ ONLY**。初回304rule occurrencesのうち、画像XMP RDF document UUID84をnonpersonalと確認。残るprivacy-related220rule occurrencesは **2 commits**。内訳: absolute path76、home52、account52、UUID review40（重複あり）。

最古affected `d40b37d19ed71139dcb749bdf8aef3669ef53ca7`、最新affected `76e5fc4263b3986e6ba7a5c3a9ebc0ad3d40c17e`。UTF-16 historical text8blobsを補足読込し追加hit0。元scanとcontext reviewを分けて保存。[reviewed history](../build/reports/privacy-secret-cleanup/history/reviewed-summary.json) / [UTF-16補足](../build/reports/privacy-secret-cleanup/history/supplemental-encoding-review.json)。raw blobの一時ファイルdump0、history mutation0。

## Public GitHub Audit

[公開repository](https://github.com/leva3896/food-healing-mod) は無認証GitHub APIでvisibility=public/default=mainを確認。`git ls-remote --symref origin HEAD refs/heads/* refs/tags/*` で7heads/3tags＋HEADを確認した。全10refsのcommitは監査済みreachable集合内。

public main/HEAD=`76e5fc4263b3986e6ba7a5c3a9ebc0ad3d40c17e`。同SHAのlocal immutable tree/blobから内容を照合し、default branchに113rule occurrences/6files、critical0。fetch/remote writeなし。[remote summary](../build/reports/privacy-secret-cleanup/remote/summary.json) / [public metadata](../build/reports/privacy-secret-cleanup/remote/public-metadata.json)。

issues、PR titles/bodies/comments、release title/body/assets、Actions metadata/logs、forks/cachesは **NOT AUDITED**。既存credential設定は読取・変更しない。

## Historical Exposure Boundary

working-tree redactionはGit commitを書き換えない。現在のpublic treeと過去履歴にはlocal/account/UUID情報が残っている。通常のcleanup commitでも過去commit、tag、clone/fork/cacheや別surfaceのコピーは残り得る。元の公開済み露出が消去済みとは記載しない。

## Redactions Applied

**既存文書42files**（旧Result/Status履歴、仕様、互換方針、試験計画、共通計画、release receipt等）。artifact SHA列とPASS/FAIL/NOT TESTED/NOT ACCEPTED markerの並び/数をredaction時に検証。version/MOD filename/test数/時刻/gameplay仕様を意図変更していない。

R1で個人値・pathを置換、R2でroot単独の空inline codeを `.` に補正。repo内Markdownリンクは相対化、外部localリンクはplaceholder表記へ。既存repo-relativeリンクとpublic GitHub URLを維持。[R1](../build/reports/privacy-secret-cleanup/redaction/summary.json) / [R2](../build/reports/privacy-secret-cleanup/redaction/r2-format-repair.json)。

## Placeholder Policy

[PRIVACY_AND_SECRET_HYGIENE.md](PRIVACY_AND_SECRET_HYGIENE.md) に統一。repo-relativeを優先し、user home/Downloads/Gradle/Prism/account/Player/networkには指定placeholderを使用。記録されたcommandのplaceholderは実行時に利用者のlocal値で解決する。実値をhandoffへ戻さない。

## AGENTS Privacy Rule

[AGENTS.md §11](../AGENTS.md#11-mandatory-privacy-and-publication-gate) にmandatory ruleを追加。credential store読取禁止、private/public identityの区別、public成果物への個人値禁止、secret検出時の値非転載/publication STOP、private evidence保護、commit/push/PR/release前scanとdiff privacy reviewを定めた。既存gameplay規則を維持。

## Privacy Guard

[tools/privacy_guard.py](../tools/privacy_guard.py) はPython stdlibのみ、外部dependency/service不要。`--tracked`、`--staged`、`--path`、`--candidates`、`--self-test`、location-only `--json` を用意。blocking=exit1、clean/known-public=exit0。stage/working filesを変更しない。

credential filenameをlazy read前に拒否、real staged blobを検査、personal filenameも値を出力しない。internal token/SHA/version/public URLを単語だけでsecretにしない。UUID例外は [allowlist](../tools/privacy_guard_allowlist.json) のexact line限定。secretへのallowlist適用はない。

history CLI modeは追加せず、今回だけのread-only audit helperを非公開evidence内に保存。guardの通常textはUTF-8、binaryはbounded metadata/ASCII stringsでありOCR/全encodingを保証しない。今回historyのUTF-16は別読込で補完した。未知binary/他encodingを公開する場合は別レビューが必要。

## Privacy Guard Self Tests

**AUTOMATED TESTED /42/42 PASS**。Windows/Unix/macOS/Unicode home、email、PAT/API key、SSH/GPG private-key header、Player UUID/account、JSON credential/hex secret/Bearer/Basic/AWS/JWTはblock。localhost/SHA/synthetic UUID/internal token/public repo URL/MOD ID/package/version/placeholderはallow。sensitive filenameのreader呼出0、出力にcredential値・personal filenameが出ないことも検証。

`python tools/privacy_guard.py --self-test --json`。[全caseの結果](../build/reports/privacy-secret-cleanup/scanner/self-test-final.json)。

## Tracked Scan Final

**PASS /exit0 /305 files /blocking0**。全tracked current text＋2binaryを検査。新規・変更済みpublication candidatesも **438 files /blocking0**。[tracked](../build/reports/privacy-secret-cleanup/tracked/final.json) / [candidates](../build/reports/privacy-secret-cleanup/working-tree/final.json)。Result/Status/policy/guardも最終scan対象に含めた。

## Staged Scan Final

**NOT APPLICABLE（stage0）**。`privacy_guard --staged` はfiles0/blocking0/exit0。stage追加/変更/unstage0。将来のcommitでは実際に公開するstaged blobがPASSである必要がある。[staged](../build/reports/privacy-secret-cleanup/staged/final.json)。

## Gitignore Review

既存build/run/.gradle/IDE/libs/backups/logs/release Jar保護を維持。crash-reports/screenshots/verification/runtime/runtimes、local privacy root、Python bytecode、`.env`、launcher account/credential files、典型private-key filesを最小追加。

private evidence tracked0、代表8pathのignore確認PASS。追跡済み/force-addedファイルはignoreだけでは保護されないためguardも必要。[publication boundary](../build/reports/privacy-secret-cleanup/inventory/publication-boundary.json)。元world/evidence/backupの削除0。

## Product Source Integrity

**src/main Java/resources、Mixin、build.gradle等はbyte不変**。test source変更0。package/MOD/registry/resource/schema/network/save/gameplay behavior変更0。privacy用docs/AGENTS/tools/.gitignoreのみ。[最終integrity](../build/reports/privacy-secret-cleanup/integrity/final.json)。buildは利用者指定により未実行、privacy self-testのみ実行した。

## Final Jar Integrity


**正式v3.0.0 release不変**: 372242bytes/286entries/182classes、SHA256 `8B8A31FA308CD24C4A139D65FEF5818F1A2DABD8587A238D93974F787FFE10DD`。Jar rebuild/repack0。[readonly Jar照合](../build/reports/privacy-secret-cleanup/integrity/jars.json)。

## Public Remote Current State

**PUBLIC DEFAULT BRANCH PRIVACY CLEAN=NO**。現在6filesに113rule occurrencesが残存する。

- docs/CODEX_STATUS.md
- docs/COMPATIBILITY_POLICY.md
- docs/MASTERY_IMPLEMENTATION_PREPARATION.md
- docs/SINGLE_CLIENT_DEDICATED_MANUAL_TEST.md
- docs/TEST_PLAN.md
- release/v3.0.0/RELEASE_RECEIPT.md

**LOCAL PUBLICATION CANDIDATE PRIVACY CLEAN=YES**。今回GitHubは変更していない。

## History Rewrite Requirement

**HISTORICAL PERSONAL IDENTIFIERS REMAIN**。履歴本文2 commits、metadata4 commitsの対応方針は利用者判断待ち。rewriteが必須かは露出範囲とSHA/tag/cloneへの影響を踏まえて別工程で決定する。今回filter-repo/filter-branch/rebase/amend/force push/branch削除/tag変更0。credential検出がないためrotationを必要と断定しない。

## Publication Requirement

次は **Food Healing RPG Privacy Cleanup Publication — reviewed commit + public GitHub update**。利用者の明示承認後、未commitの既存gameplay変更とprivacy修正を区別した公開差分を作り、mandatory scans＋staged privacy reviewを通してから通常commit/pushする。今回のdirty tree全体を無条件に公開してよいという意味ではない。今回commit/push/PR/issue/settings/release write0。

## Security Stops

今回の監査scopeではCRITICAL SECRET候補がなく、credential起因のSECURITY STOPはなし。公開main未修正のためprivacy publicationは未完。actual client工程は引き続きHOLD。

将来public/history credentialを発見した場合は値を転載せず **Security Remediation Decision — credential rotation + history/publication strategy** へ切替える。NO CRITICAL SECRETは今回scan範囲/検出能力内の結果である。

## Changed Files

既存privacy redaction42文書、AGENTS.md、.gitignore。新規2文書（本Result、PRIVACY_AND_SECRET_HYGIENE.md）と2tools（privacy_guard.py、privacy_guard_allowlist.json）。合計48filesを開始時hash manifestから照合。[ファイル一覧](../build/reports/privacy-secret-cleanup/integrity/final.json)。

修正予算: **scanner3/3、redaction3/3、remote preparation1/1、product0**。scanner R1はregex/source構文の誤検出とfixture表記、R2はpersonal filenameを出力へ再掲しない保護、R3はJSON/GPG/Basic/Unicode/bare-account coverage追加。redaction R3は新Resultのslash区切りの説明文がUnix home pathに見える曖昧表記を句読点へ変更した。個人値の再混入ではなく、guardのruleを弱めず文書側で解消。42self-tests PASS。原候補scan・各修正前scannerをprivate evidenceに保持。

## Acceptance

| Gate | 判定 |
|---|---|
| CURRENT TREE /LOCAL PUBLICATION CANDIDATE PRIVACY CLEAN | YES（scan scope内） |
| CRITICAL SECRET DETECTED | NO（scan scope内） |
| PUBLIC DEFAULT BRANCH PRIVACY CLEAN | **NO /PUBLICATION REQUIRED** |
| HISTORICAL PERSONAL IDENTIFIERS REMAIN | **YES** |
| AGENTS /guard /42 self-tests /tracked/candidate scan | PASS |
| STAGED | NOT APPLICABLE /変更0 |
| Product /candidate Jar /formal release | 不変 |
| Commit /push /history rewrite /game runtime /build | 0 |


## Next One Action

**Food Healing RPG Privacy Cleanup Publication — reviewed commit + public GitHub update**。

利用者の明示承認後に実施する。今回開始しない。gameplay工程はprivacy残件の判断までHOLD。

CHATGPT HANDOFF FILES:

- [CODEX_STATUS.md](CODEX_STATUS.md)
- [FOOD_HEALING_RPG_PRIVACY_SECRET_CLEANUP_RESULT.md](FOOD_HEALING_RPG_PRIVACY_SECRET_CLEANUP_RESULT.md)
