# Food Healing RPG v3.0.0 - 実2-client最小手動試験

Status: **BLOCKED - SECOND MINECRAFT ACCOUNT REQUIRED**

2026-09-05条件変更: 利用者が所有する正規Minecraft Java Editionアカウントは1つのみで、2つ目は用意できないため、今回は実2-client試験を実施しない。以下の2-client手順は保留中の参考として残す。アカウント追加購入や外部協力者の確保を必須要求にしない。

- `online-mode=false`、同一アカウントの二重接続、fake/offline playerを代替にしない。
- 既存server-side 2-player capability / packet / replayの自動PASSを実2-client PASSへ昇格しない。
- 別項目の[実1-client dedicated手順](SINGLE_CLIENT_DEDICATED_MANUAL_TEST.md)を準備する。その結果がPASSでも、実2-clientの分離・同時操作・相手側同期は未確認のまま。

既存のserver-side packet/scope/replay、synthetic connection、別JVM保持の自動PASSとは別の試験。
今回は手順の準備のみ。人間へ直ちに参加を求めず、サーバー・clientを自動起動しない。
既報のGUI/HUD/Root/Heroics/食事単独試験を繰り返す目的ではなく、実2-client間の分離・同期を確認する。

## 起動前準備（保留中の2-client参考手順）

- Minecraft 1.20.1 / Forge 47.2.0 / 同じFood Healing v3 JarとConfigの2 clientを使用し、JarのSHA-256を記録する。
- 別々の有効なMinecraftアカウントを使用。通常の開発runClientの同一UUID/未認証接続を実2-playerの代用にしない。
- 新しい`build/verification/multiplayer-<timestamp>`を専用working directoryとする。通常runServer既定の`run/world`や人間用`run/saves`を使用しない。自動停止するrestart fixtureも使用しない。
- この段階では専用サーバーを起動していない。開始時に既存Gradle run設定を確認し、専用directoryへ明示的に分離してから起動する。
- 試験用EULA同意は許可済み。新規の試験環境に限り`eula=true`。既存の実運用サーバー設定は変更しない。
- 同一PCでの試験を基本に`server-ip=127.0.0.1`、未使用port（候補25575を起動前に確認）、`online-mode=true`、`max-players=2`、`difficulty=normal`、`pvp=true`、`enable-rcon=false`、`enable-query=false`、`op-permission-level=2`。公開待受やOS firewall変更はしない。別PCが必要なら接続方式を利用者と確認する。
- Food Healingは現在の200 count、回復1 unit=2HP、スキル費用のまま。他MODなし。`naturalRegeneration=false`、`keepInventory=false`、`doMobSpawning=false`、安全な平地で開始する。
- 新規player A/Bの初期Lv/count/used SPが0、未取得、Food Diversity未発見であることをGUIで確認。異なる場合は保存データを改造/消去せず原因を調べる。
- 初期化はserver consoleで実名に置き換えて`execute as A run foodhealing syokugi setskillpoint 20`、Bは10。両者に`setcount 199`。commandは既存のcanonical APIを使い、NBTを直接編集しない。両clientをopにする必要はない。

## 最短の操作順

各行を一つずつ確認する。FAILが出たら同じ条件の記録を先に取り、後続操作で値を上書きしない。

| 順 | 人間の操作 | 期待する値・相手側の確認 |
| --- | --- | --- |
| 1. 購入 | AはGUIからRoot Lv1、Food Production、Slaughterを購入。BはRoot Lv1→Lv2を購入。 | A: unused13/used7、Root1、Food Production1、Slaughter1。B: unused8/used2、Root2、Food Production/Slaughter未取得。購入ボタン連打でも同Lvの二重消費なし。相手に取得・SP変化が漏れない。 |
| 2. toggle | AのRootだけOFFにして両GUIを開き直す。次にBのRootもOFF。 | AだけOFFの間BはONのまま。最後は両Root OFF、SP/取得Lvは行1のまま。True Root/True Heroics等は取得しない。 |
| 3. 食事A | 満腹度20の両者へconsoleから`execute as A run damage @s 10 minecraft:generic`（Bも同様）。リンゴ各1個を渡し、Aだけ先に1個食べる。 | A: HP10→18、count199→0、Lv0→1、unused13→14、used7維持、Food Diversityのリンゴを1種類だけ追加。B: HP10/count199/Lv0/unused8/used2のまま。RootはOFF。 |
| 4. 食事B | Bもリンゴを1個食べ、両GUIの値を記録。 | B: HP10→18、count199→0、Lv1、unused8→9/used2維持、リンゴ1種類。Aの値は行3のまま。server consoleの`data get entity <実名> Health`と画面値を必要時照合する。 |
| 5. player別craft | A/Bへ小麦3個ずつ渡し、同じ作業台を順番に使う。 | Aの結果はパン2、Bはパン1。各材料3個だけ消費。Aの個人スキルが共有作業台経由でBへ漏れない。 |
| 6. 死亡 | Bの所持ダイヤ7個を記録。他の物はチェストへ預ける。両Root OFFのまま、Slaughterを持つAが無エンチャント鉄剣でBを倒す。 | 通常death/respawnが成立。Aが回収するダイヤは合計7、増殖なし。Bは最終最大HPまで回復し、Root2 OFF、Lv1/count0、unused9/used2、Food Diversityを保持。Aの進行値も維持。 |
| 7. 再接続 | Aだけ切断→再接続、次にBも同様。最後に両GUIを記録。 | A unused14/used7、B unused9/used2、各Lv/count/取得/toggle/リンゴ履歴を保持。片方の切断が残るplayerへ影響しない。 |

この手順ではRootを発動させない。異なるRoot取得Lvとtoggleを持つplayer同士の分離を確認する。
Purification/Truth、Flight、Ammo Conservation等の保留購入を試すためにgateを解除しない。
TaCZ・外部MOD・旧world移行・OPEN-01/05は本試験のPASSに含めない。

## 結果とログ

- 各行にPASS/FAIL/未実施を付け、両playerの実名または識別用A/B、時刻、操作前後値を記録する。
- FAIL時は専用serverの`logs/latest.log`・`debug.log`、両clientの同時刻ログ、関連する`crash-reports`、両画面を回収する。token等の認証情報は共有ログから除く。
- server consoleの出力とGUIを区別し、client適用の確認前に自動PASSから昇格しない。
- 終了は両client切断→server consoleの`stop`→保存完了/正常終了を確認。再現用worldは保持し、削除して問題を隠さない。
- 現在は上記アカウント不足のBLOCKEDを維持する。将来実2-clientで実施可能になっても、実操作結果を`CODEX_STATUS.md`へ記録するまでは`NOT YET MANUAL INTEGRATION TESTED`であり、自動試験や1-client試験で代替しない。
