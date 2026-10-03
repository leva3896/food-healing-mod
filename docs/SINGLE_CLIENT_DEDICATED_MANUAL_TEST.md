# Food Healing RPG v3.0.0 - 実1-client dedicated手動統合試験

Test category: **MANUAL SINGLE-CLIENT DEDICATED INTEGRATION TEST**

Status: **STEP 1〜6 / COMPLETE - PASS WITH RECORDED SCOPE AND CONNECTION HISTORY**

2026-09-06記録整合: 正規Minecraft Java Editionアカウント1つと**Prism Launcher**で、2026-09-05に下記STEP 1〜6を各記録範囲で完了した。試験rootは`<LOCAL_PATH>/food-healing-mod-main\build\verification\single-client-dedicated-20260905-141051`、正式接続先は`127.0.0.1:25575`。serverは通常stop・保存完了・exit0、clientも人間による通常終了と最終log差分・process終了を確認済み。現在は両方停止している。詳細・前後値・実Prism path・終了証跡は[CODEX_STATUS.md](CODEX_STATUS.md)の9/5各STEP記録を正本とする。

初回14:32 TIMEOUTと18:21の途中IPv4切断は原因未確定、IPv6別接続先refusedは正式試験外の履歴として残す。今回の完了を全接続試行の成功や接続問題の修正済み判定へ拡張しない。以下は実施済みrunbookの参考手順であり、再起動・再試験の指示ではない。

実2-clientは[MULTIPLAYER_MANUAL_TEST.md](MULTIPLAYER_MANUAL_TEST.md)の`BLOCKED - SECOND MINECRAFT ACCOUNT REQUIRED`を維持する。本試験は2人間の分離・同時操作・相手側同期・PvP死亡dropの証明ではなく、既存自動PASSやintegrated server上の手動PASSとも別に記録する。

## 人間が最初に行う準備

以下は準備時の参考手順。今回の準備・試験・終了は完了済みで、**人間に新たな操作を要求しない**。別の未検証範囲を試す必要が生じた場合のみ、既存PASSと区別して対象・理由を先に確認する。

1. Prismで選択するアカウントが、Minecraft Java Editionを所有する**正規Microsoft / Minecraftアカウントとして認証済み**であることを確認する。パスワード、access token、認証情報をCodexへ渡さない。未認証なら人間がPrismの正規認証手順でログインする。
2. 完全に独立した検証用instanceが既にあれば、下記の条件と実際のdirectoryを監査してから再利用可否を判断する。普段遊んでいるinstanceは対象外。条件を満たすものがなければ、他instanceの複製ではなく新規instanceを作り、Minecraft 1.20.1とForge 47.2.0を指定する。既存の普段用world/config/optionsは流用しない。
3. 専用instanceで使用するJavaが17であることを確認する。必要な設定はその検証用instanceだけに限定し、Prism本体の共通設定や他instanceのJava設定を変更しない。
4. Prismから専用instanceのファイル保存先を確認する。実在するinstance directoryとMinecraft実行directory（異なる場合は両方）を開始時に確認して`CODEX_STATUS.md`へ記録する。ここでは内部pathやfolder名を推測で固定しない。
5. 確認した専用instanceの`mods`に最新の`<LOCAL_PATH>/food-healing-mod-main\build\libs\foodhealing-3.0.0.jar`だけを配置する。Codexが開始時に元Jar・server配置Jar・client配置JarのSHA-256一致を確認する。古いFood Healing Jarや重複Jarが残っていれば先へ進まず、専用instance内であることを確認して整理する。今回はコピー・削除を実行しない。

Prismの操作名はUI表記を推測せず一般化している。今回の専用instance path・Minecraft/Forge/Java設定・配置Jarはfilesystemから読み取り確認した。MSA認証済みとの人間報告に加え、STEP 1で本serverへの正規認証接続とGUI同期を確認済み。account保存ファイルを調査・変更したことを意味しない。

正式試験では**PrismのOffline Account、認証回避、`online-mode=false`、同一アカウント二重接続、fake playerを禁止**する。未認証の開発`runClient`も代用にしない。認証に失敗したら原因を記録して停止し、認証済みの正規接続ができるまでPASSにしない。第2アカウント購入や協力者は不要。

## Prism専用instanceの監査条件

- Minecraft 1.20.1 / Forge 47.2.0 / Java 17。開発環境のJava 17確認だけでPrism instanceのJavaも確認済みと扱わない。
- 正規認証済みアカウント1つ、Minecraft client 1つ。専用instanceだけを使用する。
- 追加MODはFood Healing RPG v3.0.0のみ。TaCZ、その他外部MOD、client utility MODは入れない。shaderと追加resource packも使用せず、vanilla標準表示とする。
- 普段用instanceからworld/config/optionsをコピーせず、独立したinstance directoryを使う。条件の不明な既存instanceを無理に書き換えて合わせない。
- 最新Jarは開始時の`build/libs/foodhealing-3.0.0.jar`を基準にし、同じファイル名でもhash照合を省略しない。server/client両方で古いJar・重複・他MOD混入がないことを確認する。
- instance本体と実行directoryの存在・独立性を確認するまでclient pathを`CODEX_STATUS.md`へ実在済みとして登録しない。Prism本体の保存先や他instanceは変更しない。

## Codexの起動準備手順

専用root: `<LOCAL_PATH>/food-healing-mod-main\build\verification\single-client-dedicated-<timestamp>`。`<timestamp>`は開始時の未使用タイムスタンプに置換し、既存環境を上書きしない。

| 用途 | 場所・予定 |
| --- | --- |
| Server working directory | `server`。新規worldは`server/world`。既存worldのコピー・初期化はしない。 |
| 正規client working directory | Prism Launcher内に作成したFood Healing RPG専用instanceのMinecraft実行directory。既存の完全独立検証instanceを使う場合も監査必須。実pathは開始時に存在確認して記録し、server専用root配下へ固定しない。 |
| Server log | `server/logs/latest.log`、必要時`debug.log`、`crash-reports`。起動console出力も専用rootに保存する。 |
| Client log | Prism専用instanceの確認済みMinecraft実行directory内の`logs/latest.log`、必要なら`logs/debug.log`、`crash-reports`。instance管理directory直下と決めつけず、実在する保存先を確認する。 |
| MOD構成 | Minecraft 1.20.1 / Forge 47.2.0 / Java 17、両側に同一の最新`build/libs/foodhealing-3.0.0.jar`。起動時にSHA-256を照合・記録する。TaCZその他の外部MODなし。 |

Server設定予定（まだ書き込まない）:

| 設定 | 値 |
| --- | --- |
| `server-ip` | `127.0.0.1`（同一PC内だけ） |
| `server-port` | `25575`候補。起動直前に未使用を確認し、競合時は別の未使用portを案内する。 |
| `online-mode` | `true`（認証必須、失敗時も変更しない） |
| `max-players` | `1` |
| `gamemode` / `difficulty` | `survival` / `normal` |
| `level-name` | `world`（上記新規server directory内だけ） |
| `enable-rcon` / `enable-query` | `false` / `false` |
| `op-permission-level` | `2`。playerへのop付与は原則不要、試験準備commandはserver consoleで行う。 |
| `eula.txt` | 既存の利用者同意に基づき、この試験用環境だけで`eula=true`。 |

- 公開待受・port forwarding・OS/firewall変更を行わない。認証サービスへの接続が成立しなければ理由を記録して停止し、offline接続へ切り替えない。
- Food Healingの現行Config（200 count、回復1 unit=2HP、既存SP費用）を維持する。起動時に両側の適用値を確認し、今回のためにゲーム仕様やConfig値を変更しない。
- world作成後にserver consoleで`naturalRegeneration=false`、`keepInventory=false`、`doMobSpawning=false`とし、安全な場所で試験する。既存worldへ実行しない。
- 現在のGradle `runServer`は既定working directoryが`run`で、既存restart opt-inはfixture実行・自動停止を伴う。どちらもそのまま本試験へ流用しない。今回はForge 47.2.0公式installerの`--installServer`で新規server内に通常配布runtimeを用意し、同installer生成`win_args.txt`をJava 17へ渡して`forgeserver`を起動した。配布Jarを`server/mods`から読み込み、dev classpath・restart/TaCZ fixture・自動player生成・自動停止を使用しない。`build.gradle`は未変更。詳細はStatusの実行記録を参照し、稼働中に重複起動しない。
- `run/world`、`run/saves`（人間用`FHR_v3_Manual_20260905`を含む）、他の`build/verification`試験worldやユーザーworldを使用・変更しない。専用環境も結果保存前に`clean`等で消さない。

## 実施した最小手順（STEP 1〜6完了）

9/5は各行の結果を一段階ずつ確認し、全6段階を記録範囲内で完了した。以下の手順自体を新たな証拠とはせず、実際の人間報告・canonical照合・logはStatusを参照する。

| 順 | 操作・確認範囲 |
| --- | --- |
| 1. 正規接続・GUI同期 | 開始指示後、Prism専用instanceから正規認証済みclient 1つを起動し、案内された`127.0.0.1:<port>`へ接続。新規playerのLv/count/SP/取得状態を記録。Codexが既存`setskillpoint`で未使用SPを少量設定し、GUIへ反映されることを確認する。 |
| 2. 購入・toggle | GUIの通常経路でRoot Lv1を購入。表示費用どおりの未使用SP減少、使用済みSP増加、取得Lvを記録。ON→OFFを操作し、GUIを開き直して保持を確認。以後RootはOFF、True Rootは未取得とする。 |
| 3. 食事・進行 | consoleの既存canonical `setcount`で199へ調整し、自然回復なし・HP不足でリンゴ（Nutrition 4）を1個食べる。HP +8（上限内）、count199→0、Lv +1、未使用SP +1、使用済みSP不変、初回リンゴのFood Diversity反映をserver出力とGUIで確認。HPなどの準備値は食前に記録する。セーブNBTを直接編集しない。 |
| 4. death / respawn | 食後の進行・toggleを記録してから、Root OFFのままconsoleの`kill <player名>`で死亡。人間がRespawnし、最終最大HPまでの回復、SP/取得Lv/count/toggle/Food Diversity保持を確認する。PvP/drop増殖試験の代わりにはしない。 |
| 5. disconnect / reconnect | 切断前のGUI値を記録し、人間が切断後、同じ認証済みclient・同じアカウントで同じserverへ再接続。同じplayerとして進行・取得・toggleが保持されることを確認する。server再起動の手動確認は本手順には含めない。 |
| 6. ログ・正常終了 | Codexが専用serverとPrism専用instance両側の該当時間のERROR、FATAL、Mixin error、network/sync exception、disconnect exception、capability errorを確認。人間の操作結果と照合してから記録。終了指示後はclient切断→server consoleの`stop`→保存完了・終了結果を確認し、worldとログを残す。 |

初期値が想定と異なる場合やFAIL時は、値を上書きして先へ進まず原因を記録する。GUIだけでserver保持を、server logだけでclient適用を成功扱いにしない。認証情報は共有ログから除く。

## 判定と範囲

- 各行を`NOT YET TESTED` / `MANUAL SINGLE-CLIENT DEDICATED INTEGRATION TEST / PASS` / `FAIL` / `BLOCKED`で別記し、操作時刻・前後値・使用Jar hash・ログを`CODEX_STATUS.md`へ記録する。
- 9/5の実施範囲は正規接続/GUI同期、Root Lv1購入/OFF/再表示、満腹時リンゴ1食のHP/食義/SP/Diversity同期、死亡Respawn、正式IPv4再接続後の保持、正常終了と最終log確認。通常配布Forge serverとPrism clientを使用した。別JVMの手動server restart、実2-client相互分離、外部MOD互換、実旧world移行のPASSではない。
- 本試験でPASSしても実2-clientはBLOCKEDのまま。2-player自動PASS、既存vanilla手動PASS、外部MOD未統合、実旧world待ち、OPEN / NOT LOCKEDとrelease判定は変更しない。
