# 浄化の極意・真実の極意 — 共通実装計画

最終更新: 2026-10-03 12:44 JST
現在のpublication残件: LOCAL GIT PREFLIGHT BLOCKED（§14.75末尾）。
状態: **#1–#8 COMPLETE / #9 PARTIAL / RC=YES / LOCAL RELEASE ARTIFACT AND SPECIFICATION COMPLETE / GITHUB UPDATE BLOCKED**。現行更新は[§14.75](#queue9-formal-release)。全仕様は[完全仕様書](FOOD_HEALING_RPG_V3_SPECIFICATION.md)、現在集計は[Status](CODEX_STATUS.md#現在の要約)。旧節のSTOP/次/購入停止は当時の履歴であり、現在はFlight/P/T PURCHASE READY・SP保護。今回source/build/game変更0。

## 1. 根拠と判定の区別

- A 確定仕様: [Skill Tree §9 Q2](SKILL_TREE_SPEC.md#purification-mastery-activation)、[Compatibility §7 Q1](COMPATIBILITY_POLICY.md#trial-monolith-149)。2026-09-15の利用者決定で両方 **RESOLVED / LOCKED**。取得100/500SPと効果時条件は別。
- B 現行実装: §3・§10.8・§11。Cube、侵略者の保護解除/大小Beam、L2旧4種＋weakness/witherの6 ID P/T限定Adapter（§13）。通常浄化のtick後消去・Rootを代用にしない。
- C 実物・試験: §4–7はTrial初回、§8.10は当時の限定実client、§10.8は19:35のL2/Trial自動統合、§11は21:11の侵略者追加と最終Jar回帰。対象Jarと実施範囲を分ける。 §10.10は旧L2実client中断run、§10.11は新runの限定実client完了結果。
- D 後続計画 / E 不足: 旧4 IDは§10.11、新2 IDは旧§13.10 GUI/A/B/T＋§13.11 C/seal/通常保存終了、侵略者は§11.7/11.8で限定完了。購入最低範囲・optional方針は[§12.5](#mastery-purchase-policy-decisions)でLOCK済み。新規P必須のFEは[§14.7–14.10](#fantasy-ending-dependency-timestop-result)で依存実ロード/追加静的照合済み。外部実物待ちは解除、TimeStopは§14.39の製品写像/限定自動に加え、§14.55でqueue #2の製品client/context再読込指定受入を完了。FE6は§14.56で実装/自動統合、§14.57でDream代表実clientが完了。購入ready接続は§14.58で完了。
- 9/13の読取だけの準備・旧1.2.8提案は§9の**過去履歴**。その時点のNOT RUNを過去からPASSへ書き換えない。

## 2. 効果と現在の不足

現行readinessの分類・効果別残件は[§12](#mastery-readiness-inventory)、採用済みweakness/witherの自動検証完了は[§13](#l2-weakness-wither-implementation)。以下は実装基盤の要約であり、未対応全てを購入/release必須とは扱わない。

### 浄化の極意

| 効果の項目 | 確定した内容 | 根拠 | 現行実装 | 不足 | 利用者判断 |
|---|---|---|---|---|---|
| 取得・購入 | 通常浄化Lv1取得、当該node100SP、追加前提なし。親ONは購入条件でない | Skill Tree §9 Acquisition | server/GUI共通判定、100SP PURCHASE READY、SP保護 | 今回の購入受入残件なし（§14.58） | OPEN-02 LOCK済み、再質問しない |
| 効果時の本人条件 | 両node取得・両方ON、正常server canonicalのみ。親OFFは極意設定を変更しない | Skill Tree §9 Q2 | PurificationMasteryController、対象ServerPlayer capability参照 | 他の効果入口への接続はその実装時 | Q2 LOCK済み、旧親ON不要案は不採用 |
| Damage Cube Soul作用 | 新規蓄積・実質HP上限低下・派生強制作用を抑止、既存Soulを治療しない | Compatibility §7 Q1 | 1.4.9の通常/強制加算の限定2hook。実統合済み | 通常Cube実client A/B/C・seal/保存/再読込は§8.10限定PASS。force/別版/他攻撃等は未確認 | Q1 LOCK済み |
| 正当な数値ダメージ | 独立hurtは既存防御へ通す。侵略者Huge Beamだけ利用者が明示した例外 | Skill Tree §9、Compatibility §7 | Cube/Smallのnumericを保持、侵略者Hugeの1 call-siteだけ抑止（§11） | 他攻撃へ数値閾値や共通DamageTypeで広げない | 巨大ビーム例外LOCK済み |
| 敵対Trait/Affix | 本人に対する対応敵対効果を無効化、mobのtraitは消さない | Skill Tree §9、Compatibility §5 | **L2 6 ID P/Tを限定実装・133件自動統合**。旧4 IDの95件は当時の契約として保持。tick後除去を代用にしない | 4種実clientは§10.11限定完了。追加2 IDは133/133自動検証完了・旧GUI/A/B/T＋今回C/seal・通常保存終了を限定完了（§13.11）、残33 IDは最低範囲外のbacklog、機構と購入契約は§12 | 本人条件LOCK、追加質問不要 |
| その他強制死/除去 | 攻撃固有の強制層・確認済み敵対解除を本人だけ保護 | Skill Tree §9、Compatibility §7–8 | Cubeに加え侵略者のSoul加算/既存Soul Protection解除/巨大numericを§11で実装 | 他攻撃候補C/E・Hyperlink/Fumetsuは最低範囲外のartifact待ち。一般MobEffect解除は実処理未特定で仮想必須hookなし（§12） | 侵略者は今回追加承認済み、他機能gateは維持 |
| FE 終焉の守護者 | P最低必須/T対象外LOCK | §12.5・§14 | TimeStopは§14.55のqueue #2指定受入COMPLETE。FE6は§14.56製品実装/焦点自動統合完了 | FE6 Dream限定実clientは§14.57で完了。通常購入ready/SPは§14.58で完了。広いTimeStop gateは未証明のまま | valid UOM/P vehicle未LOCK維持 |
| 保存・同期・外部状態 | 本人canonical、外部owned state保全、optional不在安全 | SPEC §11・13、Compatibility §1 | canonical schema/保存処理は不変。TimeStopのS2C Lease追加/protocol7は§14.39。通常player保存再読込・既存Soul/NBT/modifier保持、登録packet sync一致 | Trialの同world通常実client保存/再読込は§8.10限定PASS。別JVM/TCPは未試験 | True Root lifecycle等へ推定を広げない |

### 真実の極意

| 効果の項目 | 確定した内容 | 根拠の文書・節 | 現行実装 | 不足 | 利用者判断が必要か |
|---|---|---|---|---|---|
| 取得・購入 | 浄化の極意Lv1取得、当該node500 SP、追加前提なし。取得と親ONは別 | Skill Tree §10 Acquisition、Test §26 | 共通取得判定・500SP PURCHASE READY | 今回の購入受入残件なし（§14.58） | 再判断不要 |
| 範囲・発動 | player中心X/Y/Z各±75 AABB、出現/進入/teleport/player移動 | Skill Tree §10 Area/Trigger | TruthMasteryController＋L2のjoin/作用前/段階的20tick補完、player範囲query | XYZ全境界は95内で自動確認済み。§10.11は実client範囲内/X90保持を確認。大規模性能は未測定（§12.4） | 再判断不要、全block走査なし |
| 除去/無効化対象 | 対応高難度traitを恒久strip/nullify | Skill Tree §10、Compat §5 | L2の6 IDでowned marker＋公開remove＋元syncを133/133検証済み | 新2 IDのC/sealは§13.11で限定完了。他trait/属性trait等は未対応 | 今回4種の再判断不要 |
| 対象外・他player | supported mobの無関係なHP/属性/AI/level/loot/capを一括変更しない | Skill Tree §10、Compat §1・5 | 自動検証済み6 ID、実clientは旧4 ID完了・新2 IDもGUI/A/B/T/C限定確認。他trait/データ・他player/反射先の既存保全を維持 | 全boss/全MODや属性trait除去へ未拡張 | 曖昧な後続traitだけ実物照合後に判断 |
| 恒久性・再適用 | 範囲外/OFF後も維持、冪等、chunk保存/読込 | Skill Tree §10、Test §21 | mobUUID拘束ID別marker、init/copyFrom/clearPending/実作用前guard、実chunk再読込後も抑止。§10.11で実client全OFF/範囲外・同world通常再読込も保持 | 別JVM再起動/未対応版は未検証 | 対象4種の単なるmarkerだけの実装ではない |
| ON/OFF・親依存 | 3つ取得/ON時だけ新規無効化、既存無効化維持・toggle非連動、購入時ON条件なし | Skill Tree §10 | 効果述語・全8組合せ・packet同期を検証 | §10.11の通常GUI/表示を限定確認。実client全8組合せではない | LOCK済みを変更しない |
| 安全な実装・未対応 | optional/版限定・所有データのみ変更 | Skill Tree §10 Safe、Compat §1–2・5 | L2HostilityVersionsはゲームclass非依存、狭いMixin/Adapter。L2不在のvanilla/TaCZ/Trial回帰成功 | 別版実物の起動は未試験、版拒否は単体検査 | 架空API/依存や全map消去なし |
| 性能・複数player | 毎tick全151³走査なし、重複適用は冪等 | Skill Tree §10 Performance、Test §21 | loaded-entity queryは移動中最短5tick/静止20tick、mob補完20tickに分散。重複player範囲確認 | 多数entity/playerの候補数・TPS・長時間実測は未実施 | 具体的な間隔は実装上の選択、LOCK変更なし |

## 3. 現行Food Healingの実装境界

| 箇所 | 実装・変更 | 維持した条件 |
|---|---|---|
| PurificationMasteryController | canonical acquired levelが双方1、双方ON、pendingでないことをreadonly判定 | client申告、legacy fallback、不正過大levelから保護しない。取得/設定/SPは書かない |
| compat/TrialMonolithCompatibility / TrialMonolithMixinPlugin | 対象ServerPlayer本人判定、loader ID/versionがthe_trial_monolith/1.4.9のときだけMixin | 外部classを通常controller/pluginで無条件ロードしない。不在/未対応版は無介入 |
| mixin/trialmonolith/TrialMonolithDamageCubeMixin | 実lambda内の通常/強制add呼出しを各require=expect=allow=1でRedirect | 非保護時は選択された元callを1回。lambda/hurt/motionを取消しない |
| build.gradle / 専用Mixin JSON | 提供実物のcompileOnly、専用設定、別trialMonolithTest/trialInvaderTest source set・reobf fixture | 配布Jarへ外部Jar/fixtureを入れない。通常runtime必須依存化しない |
| TruthMasteryController / compat/l2hostility / 専用Mixin | §13の現行6 ID（poison/slowness/corrosion/erosion/weakness/wither）限定本人保護・範囲/恒久無効化、optional/version guard。§10.8の4 ID結果は履歴として保持 | Pは数値補正/RNGを残す、Tは当該trait数値補正を消す。所有UUID/IDだけmarker/remove、反復安全、元sync |
| TrialInvaderCompatibility / TrialInvaderProtection・BeamSoul・HugeBeam Mixin | §11の侵略者tick/beam呼出しに限定。server本人canonicalとbeam native ownerを都度判定 | 共通helper/数値防御/全死亡取消なし、既存CubeとL2のclass byte不変 |
| 既存Skill購入・Root・DamageEventHandler・packet・capability/schema | production変更なし | 両極意購入停止、Root期限、既存数値防御、server同期/保存、TaCZ/SWの分岐を維持 |

## 4. 対象MOD・artifact

<a id="mastery-artifacts"></a>

承認原物: `<LOCAL_DOWNLOADS>/CODEX_TRIAL_MONOLITH_SEND_SET/TheTrialMonolith-1.20.1-Forge-1.4.9.jar`。
ID `the_trial_monolith`、mods.toml / Implementation-Version **1.4.9**、**263,159 bytes / 240 entries**、
SHA-256 **5EFE4C068F24E611C215A0A20FE9698D5C5F7EFC1476C8E5CEAEA78B8CE1A0AD**。
[実測・metadata/CRC](../build/verification/trial-monolith-20260915-125400/audit/input-artifact.json)。原物・libs参照コピー・試験配置は同hash。改変/再包装なし。

- 宣言: javafml/Forge `[47,)`、MC `[1.20.1,1.21)`、Curios optional `[5.0,)`、必須外部MOD/同梱Jarなし。
- 実行: **MC1.20.1 / Forge47.4.0 / Java17.0.7**。Curios/TConstruct不在で実統合成立。依存宣言だけで47.2.0対応とはしない。
- **Forge47.2.0起動はFAIL**: Trial本体が`FMLJavaModLoadingContext.registerConfig(Type,IConfigSpec)`を呼びNoSuchMethodError。Food Healing hook不一致ではない。最小対応Forge版は未特定で、47.4.0を実測成立版とする。
- 手元の47.4.0 installerと必要ローカルlibrariesのみで専用runtimeを準備。公式installer内server.lzmaを既存MC SRGへ適用し、生成server SHA1が指定値 `62f6102e4b5d61a10281410cf5957a876a127e6b` と一致。[offline準備証拠](../build/verification/trial-monolith-20260915-125400/audit/forge4740-runtime.json)。新download/旧instance起動/旧world再利用なし。
**L2 Hostility本体2.5.19は受領済み・Forge47.4.0で実ロード/4種限定自動統合95/95完了**（[§10.8](#l2-implementation-result)、実採用Tracker0.4.4）。識別/必須依存は[§10.1](#mastery-l2-2519)、依存内部の静的照合履歴は[§10.7](#mastery-l2-dependencies)。最小構成に判明したファイル不足はない。L2実clientは新run [§10.11](#l2-client-completed-result)でA/B/T/C・全OFF/範囲外・保存再読込/正常終了を限定完了。旧中断は§10.10の履歴として保全。Hyperlink/Fumetsu等の未提供状態は維持。
- `tmp_monolith` 1.2.8は旧参考展開物のみ。元Jar未確認という**今回Trialの停止理由は解除**し、1.2.8へ戻さない。

## 5. 1.4.9の実物と実ロードの照合

<a id="mastery-trial-route"></a>

[静的DamageCube bytecode](../build/verification/trial-monolith-20260915-125400/audit/DamageCubeEntity.javap.txt)では、`activate()`が4×4×4の範囲とnative predicateで対象列挙。
高次元cubeかつ外部Soul免疫対象ならforce加算(config/10)、それ以外なら通常加算(config)を選ぶ。
その後`max(3,maxHealth*0.05)`のcube_attack hurtとmotion resetが続く。旧1.2.8の通常add1箇所だけという前提を破棄した。

- [EntityHelper](../build/verification/trial-monolith-20260915-125400/audit/EntityHelper.javap.txt): normal setterは外部免疫ならreturn、force setterは旧値<1→新値>=1でonSoulDeath、旧値<10→新値>=10でonSoulRemove。直接HP=-Infinity/death/drop、ServerPlayerのforced respawnを含む。
- [EntityMethods](../build/verification/trial-monolith-20260915-125400/audit/EntityMethods.javap.txt): 1.4.9は`modifyHealth`等へ変更。Soul>0の実効HP上限、Soul>=1のdead/HP0、SoulProtectionによるhealth変更を区別。
- [GenericTransformer](../build/verification/trial-monolith-20260915-125400/audit/GenericTransformer.javap.txt) / [launch plugin](../build/verification/trial-monolith-20260915-125400/audit/TheTrialMonolithPlugin.javap.txt): BEFORE/AFTER変換でhealth/dead等の呼出し/本体を接続。Mixin priority2147483647の宣言だけで成功扱いにしない。
- [EntityMixin](../build/verification/trial-monolith-20260915-125400/audit/EntityMixin.javap.txt): cached field/SynchedEntityData、NBT SoulDamage/SoulProtection等。**loadはsetSoulDamageForce**。旧1.2.8のnormal setterという記録を流用しない。製品Adapterはload/helper全体へ介入しない。
- 実JVM class定義時に、変換を行わない検証専用javaagentで採取。[変換後classのjavap](../build/verification/trial-monolith-20260915-125400/audit/loaded-cube-final.javap.txt)と[機械検査](../build/verification/trial-monolith-20260915-125400/audit/loaded-callsite-validation.json)で通常hook1・強制hook1・hurt1・後続motion1、各redirect内の元helper1を確認。製品Jarにagentなし。

## 6. 2026-09-15 13:29までの実装・試験の結果（過去履歴、今回の回帰は§10.8）

すべて **AUTOMATED**。本物のserver player＋EmbeddedChannelによる隔離試験であり、実client、認証/TCP、実2-clientではない。

| 段階 | 結果・証拠 | 限界 |
|---|---|---|
| 単体/data | eligibility150 assertions、既存前提6群142・SW47・境界5000・Ammo10000等PASS。[最終build](../build/verification/trial-monolith-20260915-125400/audit/build-final.log) | 数式/データ検査を実MOD攻撃PASSにしない |
| vanilla/TaCZ回帰 | 各 **59/59 = 既存57＋新規2**。[vanilla](../build/verification/trial-monolith-20260915-125400/audit/vanilla-final.log) / [TaCZ](../build/verification/trial-monolith-20260915-125400/audit/tacz-final.log)。build/unit/check成功 | 新規2件はcore合成eventとtoggle/sync。別13/Ammo実MOD・SW実射撃は今回再実行なし。共通damage/packet変更なし |
| 最終配布Jar＋実Trial | **49/49**: 実activateを含む47ケース、直接helper対象外1、通常保存再読込1。[receipt](../build/verification/trial-monolith-20260915-125400/actual-server-final/trial-result.json) | 4つの強制閾値ケース内のhelper対照は独立した実攻撃ケース数へ足さない |
| 数値とSoul分離 | 通常/強制各4ON/OFF、未取得/不正/pending、fresh/正常移行、Soul0/.25/.95・加算1.1/10.1、native threshold1/10、二攻撃、他player/mob/owner、packet/command次回攻撃、既定.03＋既存50%軽減、正当numeric death/drop | Force側の外部免疫はHPに影響するため同じflag同士で比較しevent5×1/2とsourceを別記録。HPが減らないだけでhurt遮断としない |
| 保存・非対象 | parentOFF/masteryON、SP/取得、Soul.25、foreign NBT/permanent modifierの通常PlayerList保存→新ServerPlayer再読込保持。直接normal/force helperは保護中も変更なし | 同一JVMの通常再読込。別JVM/実clientのTrial同期保存は未試験。既存True Root未決定境界へ展開しない |
| 終了 | 全起動JVM終了確認。47.4.0試験は通常saveEverything→halt→Saving players/worlds/全dimension保存。最終PID36108 exit0。[process](../build/verification/trial-monolith-20260915-125400/audit/process-final.json) | 初回47.2.0はworld開始前ロード失敗で保存対象なし |

## 7. 第一実装単位・合格条件と配布物

<a id="mastery-first-unit"></a>

13:29までの実装フェーズで承認された第一単位は**実装と自動検証完了**。合格条件は[TEST_PLAN §19](TEST_PLAN.md#19-trial-monolith)。
当時の配布Jar [保全したfoodhealing-3.0.0.jar](../build/verification/l2-implementation-20260915-182900/before/build/libs/foodhealing-3.0.0.jar): **205,688 bytes / 131 entries**、
SHA-256 **8F4AB9F60C2B0D64CF2902944248261A806759BF57CA308EA4273FBC950C3529**。[現物検査](../build/verification/trial-monolith-20260915-125400/audit/final-jar.json)。metadata/Mixin/refmap/reobf、外部class/Jar/fixture/GameTests/ExampleMod非混入、最終server配置同hash、回帰時と製品class byte一致を確認。

途中FAIL/修正は削除しない:

1. 47.2.0で外部本体API不足。47.4.0の実在runtimeで解消、原物/製品の要求版を書き換えていない。
2. 初回fixtureはCube current座標だけ設定し`getPosition(0)`が旧位置を参照、対象外となりmotion assertion FAIL。fixtureをmoveToで初期化し、native範囲にplayerがいるassertを追加。製品hook変更なし。
3. 非保護強制死に対しdrop callback常に1回としたfixture期待がFAIL。実物helper対照も **[7,0]** で一致。空の追加通知を隠さず、nonempty1回/合計7個・death/respawn同等を検査。正当numeric deathは厳密にdeath1/drop1、保護時は強制death/drop0の条件を維持。
4. 最初のjavapはDownloadsのアクセス/文字encodingに失敗し、同hashのworkspace参照コピーで再取得。初回server logの表示にもencodingエラーがあり原logを保持。製品の不具合修正ではない。

dev59件にはrefmap読取WARNが残るが、配布Jarのrefmapは現物検査済み。最終runにもsandboxの認証公開鍵取得ERROR、Forge更新確認の接続失敗、WMI/metadata/SynchedEntityData/config既定補完WARN等が残る。[全log](../build/verification/trial-monolith-20260915-125400/audit/actual-server-final.log)。攻撃/保存試験は成立したが認証接続・全warning解消をPASSにしない。

## 8. 残る不足と次の作業

<a id="mastery-blockers"></a>

| 分類 | 現在の状態・次の単位 |
|---|---|
| 解決済み | Trial原物受領、1.4.9採用、Q1/Q2、限定実装開始承認、依存準備、今回の自動統合。これらを再質問/資料待ちへ戻さない |
| 限定実client完了 | §8.9の安全準備修正は利用者承認を受け新runで実施。通常Cube A/B/C各1回・両ON非治療・seal/保存再読込1回・正常終了は限定PASS（§8.10）。前runのA限定PASS/中断worldは保全、再利用なし |
| 後続artifact/承認待ち | L2代表4種実clientは§10.11で限定完了。侵略者は§11.7のF1/F2/S1＋§11.8のS2/H1・seal/cleanup/通常保存終了まで限定完了。残件の必須性/候補/artifact待ちは§12で分離 |
| 効果全体・readiness | 固定pendingとv3対応表未確定が購入停止の現在境界。未対応候補の機構・B/C/E分類は§12。両極意IMPLEMENTATION_PENDING/SP保護を解除しない |
| 未確認 | 実clientのforce/閾値死/回復頭打ち/自然boss攻撃、別版、最低動作Forge版、他攻撃/全boss・全MODpack、今回Trialの別JVM/TCP保存同期。Truth効果時条件は今回利用者回答でLOCK済み（Skill Tree §10）。Trial完了判定とは別 |

<a id="trial-client-preparation"></a>

### 8.1 推薦方法と承認境界

**推薦: 最終製品Jarをそのままロードし、別の検証専用MODに用意する明示的な一度だけのprepare操作で、新規worldの指定player本人の取得データを準備する。**
通常GUI購入の実績ではなく「取得済み状態での効果/表示/同期確認」である。製品readinessを変更せず、真実の極意や他skillは付与しない。
§8.1–8.8は**14:10の承認済み基準手順**。初回中断は§8.9、安全準備修正の承認と新runの完了結果は§8.10を参照。失敗runは終了済み・再利用なし。下記期待値と各runの実測証拠を区別する。

根拠となる既存実装:

- `FoodHealingSkills.IMPLEMENTATION_PENDING`には両極意が残る。通常浄化Lv1は3SP、極意Lv1は100SP。`SkillRowControls.forLevels`は取得済み最大Lv1でもtoggleを表示するため、購入停止を外す必要はない。
- `ShokugiProvider.SHOKUGI_CAPA`からserver本人の`IShokugiData`を取得できる。実在する`addUnspentSkillPoints` / `trySpendSkillPoints` / `setSkillLevel` / `setSkillDisabled`を使い、`FoodHealingSkills.tryPurchase`による通常購入と混同しない。
- `ShokugiData.serializeNBT` / providerがcanonical保存を担う。provider IDは`foodhealing:shokugi_data`、通常保存では`ForgeCaps`内。同データは`FoodHealingDataVersion=4`、`AcquiredSkills`の`Id`/`Level`、`DisabledSkills`、longのSP値を使う。
- 通常GUIは`ToggleSkillPacket`を送信。serverの登録済み`consumerMainThread`→取得検証→`RootController.setSkillDisabled`→`CapabilityEvents.syncToClient`を通る。syncは製品`ShokugiSyncPacket`でclient capabilityへ反映される。測定用MODでtoggle packetを代送・client値を上書きしない。
- 直前fixtureのデータ作成APIとactual Cube生成を参考にするが、**freshデータ丸ごとのdeserialize、HP100への設定、外部Soul setter/flag操作、invulnerableTimeのゼロ化は今回の実client補助へ移植しない**。

### 8.2 一度だけの取得データ準備

1. 新規instance/worldを作った記録と、run ID・instance実パス・world実パス・対象Jar hashを`run-allowlist.json`へ記録する。初期状態はUNBOUNDで、観測以外は不可。最初のreadonly inspectが返す現在playerのUUID・表示名を確認して、その**1 UUID**だけにbindする。認証ファイル/保存tokenからUUIDを抽出しない。
   補助の設定先は新instance専用のJVM property `-Dfoodhealing.trialclient.verificationRoot=<新規verification rootの絶対パス>`で明示し、その直下のallowlistだけを読む。propertyなし・path/world不一致なら付与/攻撃不可。古いinstanceのJVM設定や製品Configを変更しない。
2. prepare commandはserver thread上で、integrated server、指定real path内の新規world、overworld、接続player1人、bindしたUUID、Survival、Jar/version一致を検証。runごとに別ディレクトリを新規作成し、既存instance/worldへ上書きしない。旧worldを「fresh」と称して使わない。
3. 対象capabilityが存在し、schema4、pending=false、取得/disabled/base stats空、ShokugiLevel/EatCount/legacy値/Root予約・期限/SPがfreshの0であることを**読取で確認**。未知schema、pending、既取得、非zero進行などは中止・記録し、フラグ解除/reset/修復しない。
4. `prepare-receipt.json`をCREATE_NEWでPREPARINGとして先に確保する。ファイルが存在すれば、PREPARING/失敗状態も含め再prepareを拒否する。同一UUID/再入場/再起動で再付与しない。途中失敗は測定に進まず、状態を保存して報告する。
5. 検証用SP credit **103**を明示して`addUnspentSkillPoints(103)`→`trySpendSkillPoints(103)`を1回実行。取得を`foodhealing:purification=1`、`foodhealing:purification_mastery=1`にし、双方ON。これは人工の検証用creditであり、食事等で稼いだSPや通常購入の証明ではない。
6. 完了値は **UnspentSkillPoints=0L / SpentSkillPoints=103L / 両node Lv1 / DisabledSkills=[]**。費用103は実定義3+100と照合して固定。ShokugiLevel/EatCount/BaseStats/Root/他capability/inventory/外部状態は変更しない。真実は未取得。HP/MAX_HEALTH/吸収/SoulDamage/SoulProtection/SoulBypass等は一切書かない。
7. 前後canonical差分を取得・SPだけへ限定検査し、製品`CapabilityEvents.syncToClient(player)`を1回呼ぶ。PREPARED receiptにbefore/after・credit/spend・UUID・world・時刻・初期化回数1を記録。通常保存/読込はprovider/Forge/Minecraftの既存経路へ任せる。

prepare操作はlogin/tick handlerから呼ばない。攻撃・観測commandには取得/SP setterへの経路を持たせない。永続receiptにより次の通常再読込でも一度だけの条件を維持する。再同期のためにHP/Soulを修復したり、capabilityを空データで置き換えたりしない。

### 8.3 本物のCubeを安全に1回ずつ発動させる方法

- 新規flat/Survival/cheats ON、難易度Normal。**world生成前に通常GUIのゲームルールで`naturalRegeneration=false`、`doMobSpawning=false`を設定し記録**する。入場後は床/四壁/天井の閉区画と周辺敵対Mob不在を確認し、待機/GUI中の接近も防いでからprepareする（今回の実施構造は§8.10）。必要なら新world専用の通常commandで昼/晴天へ固定。製品Config・Trialの既定Soul量は変更しない。平地に静止、他damage・炎/水/落下/効果・防具・吸収・Root/軽減skillなし、実MAX_HEALTH20・初期HP20・Soul0を確認する。違えば初期化で合わせず中止。
- 攻撃者は検証用の通常Cow1体を、対象playerから8block離れた安全な平地へ一度だけ生成してNoAIにする。UUIDを記録し、それをcube ownerに使う。他Player攻撃倍率との混同を避ける。試験player/攻撃位置は固定し、owner以外の無関係entityがnative攻撃範囲にいれば発動しない。
- 既存fixtureと同じく登録済みEntityTypeから**実`DamageCubeEntity`**を生成。`moveTo`で現在位置と`getPosition(0)`が参照する前座標を両方合わせ、実`setOwner`と`setHighDimensional(false)`を使う。攻撃対象の外部免疫flagは既定falseを確認するだけ。`TrialMonolithConfig.damageCubeSoulDamage==0.03F`を確認し、書き換えない。
- **検証commandがserver threadで実`activate()`を1回呼ぶ**。そのnative範囲検索・predicate・Soul分岐・hurt・motionを通す。helperへの直接Soul加算や`/damage`では代用しない。自然spawnしたboss/Cubeのtickによる攻撃ではなく、**実clientに接続した検証用トリガーによる本物の攻撃処理**と明記する。
- Cube自身はworldへ登録しないため、tickによる遅延再発動/連続攻撃は発生しない。Cube entityの描画・自然なbossの生成/照準/タイミングは今回の確認外。音/粒子等のactivate内の処理はそのまま通す。
- 実装予定command名は`/foodhealing_trial_verify inspect`、`prepare <run-token>`、`hit A|B|C`、`seal`。**今回別補助に実装したfixture command**で製品commandではない。各hitには期待toggle/Soul/HP・対象UUID/worldを再確認し、A→B→Cの各ID1回、総activate3回まで。受理IDを実行前に永続journalへ記録し、再送/再入場で再実行しない。
- 前のvanilla無敵時間が残れば、明示的に受理した1件だけを自然に期限が過ぎるまで待機する（最大60server ticks、wall timeoutも設定）。`invulnerableTime`や時計は変更せず、人間へtick計測を要求しない。状態変化/timeout/想定外イベントは中止し、攻撃の自動retryはしない。
- 最悪でも保護なし通常Cube3回のSoul増分は最大約0.09で、閾値1未満、通常数値は計9。準備条件ならHP20から安全域に残る。各発動前のHP予測と**HP>8、Soul<=0.06、外部免疫false**を再検査し、予期しないダメージ/値なら次の攻撃を拒否する。死亡防止のためにdamage取消やHP修復を入れない。

### 8.4 観測する値と意味

| 観測値 | 実在する読み取り元・記録方法 | 解釈と制限 |
|---|---|---|
| server/client canonical | それぞれのplayerのShokugi capability.serializeNBT | 両node/disabled/SPを比較。client observerはserverの値をclient値として報告しない |
| server/client Soul | それぞれのEntityに実`EntityHelper.getSoulDamage`を呼ぶ | 1.4.9のcached field/SynchedEntityDataを読むだけ。外部通常syncの到達をclient側でも確認。setter禁止 |
| server実効HP H | 対象ServerPlayer.getHealth()、攻撃直前/実activate直後/次tick安定値 | Trialの実変換下の戻り値。未変換の内部生Healthと呼ばない |
| client実効HP | 実LocalPlayer.getHealth() | client threadで独立採取。Health packet/外部entity dataの収束とHUDを比較 |
| 最大HP M | getAttributeValue(Attributes.MAX_HEALTH)とgetMaxHealth() | 今回は20。Soulの潜在的なHP上限とは別の値 |
| 通常numeric | readonly LivingHurtEvent/LivingDamageEvent observerのamount・source・owner・回数・cancel状態 | 各hitでcube_attack、各event1回、amount3を期待。他damageを検出したら比較汚染として中止。observerはsetAmount/setCanceledを呼ばない |
| 保存NBT Health | 正常終了後のlevel.datのData.Playerと、存在すればplayerdata/UUID.dat | vanillaの保存はgetHealth()からHealthを書き、Trialの変換も関与する。NBT値を未変換の生Healthと断定しない |
| HUD | 通常画面/F2とHealthDisplayOverlay | client getHealth/getMaxHealthを表示し、1000未満は小数1桁。分母20がSoul上限19.4/18.8に変わることは期待しない |

根拠: [1.20.1保存/HP getterのsource抜粋](../build/verification/trial-client-plan-20260915-135500/audit/health-source-excerpts.txt)、1.4.9 `EntityMethods.modifyHealth`/GenericTransformer/EntityMixin、`HealthDisplayOverlay`。source cacheは既存Forge47.2.0 mappedだが、ゲーム実行は47.4.0だけ。1.4.9実物はhashで前回と同一確認済み。

外部免疫false/Soul<1の場合、`modifyHealth`の制限値は`M*(1-Soul)`。0/0.03/0.06なら20/19.4/18.8だが、これは**実Soulから計算する制限値**。今回HPはそれより低く、回復して上限へ到達させないので、19.4/18.8まで回復が頭打ちになることを実client実測PASSとは呼ばない。新規Soulの抑止/非抑止・独立numeric・表示/同期を今回の限定判定とする。

製品GUIにSoul数値表示はないため、server/client各JSONLへのreadonly採取を補う。client専用observerはClientTick ENDで値が変わったとき＋低頻度に記録し、LocalPlayerだけを読む。run/caseの対応は時刻・gameTime・caseラベルで追い、playerデータの注入・新しい保護判定packetは作らない。閉じたGUIからworldへ戻った後にclient/serverが収束したことを確認してF2を撮る。収束待ちは機械観測（最大5秒、タイムアウトはUNVERIFIED）とし、同一tick・人間の秒数計測は要求しない。保存画面だけでは攻撃前後のevent回数は分からないのでログと分離する。

### 8.5 最小実client操作順と合格条件

準備・各snapshot・攻撃command・GUI・F2はComputer Useで操作する計画。厳密なtick操作、食事長押し、OBS、人間の死亡試験は不要。

| 手順 | GUI/操作 | serverで成立させる条件 | 期待Soul（前→後） | 期待実効HP（前→後）/HUD |
|---|---|---|---|---|
| 準備 | 新規instance/world、安全設定→inspect/UUID bind→prepare1回→通常GUI | 取得Lv1×2、ON×2、未使用0/使用103、M20、H20、Soul0、軽減/Rootなし | 0のまま | 20、HUD20.0/20.0 |
| A | 両ONを通常GUIで確認→閉じる→hit A→HUD/F2 | server/client canonical両ON。通常Cube1回 | 0→0 | 20→17、HUD17.0/20.0 |
| B | **通常浄化だけOFF**→GUIを閉じる→inspect→hit B→HUD/F2 | server親OFF・極意ON、SP/取得不変。通常Cube1回 | 0→0.03 | 17→14、HUD14.0/20.0 |
| C | 通常浄化をONへ戻し、**極意だけOFF**→閉じる→inspect→hit C→HUD/F2 | server親ON・極意OFF、SP/取得不変。通常Cube1回 | 0.03→0.06 | 14→11、HUD11.0/20.0 |
| 封印/保持 | 極意を通常GUIでONへ戻す→閉じる→inspect→seal | 両ON、activate総数3で封印。追加攻撃/再付与なし | **0.06を保持** | 11を保持。再ONでSoul治療/HP修復しない |

HP/Soul初期値を各caseでリセットせず、同じplayerで連続比較する。A→B→Cの各手順はチャットへの逐次報告で分割せず、GUI状態の収束確認まで一連で行える。

- Soulの差の許容誤差 **1e-5**、HP/amount/MAX_HEALTHは **1e-3**。HUDは実client HPの小数1桁丸め（±0.05相当）と一致し、内部floatの完全一致を求めない。
- 各caseのLivingHurtとLivingDamageが各1回、source `the_trial_monolith:cube_attack`、ownerが準備したCow、amount3、非cancel。Soulは上表、HPは各3低下。型/値・外部flag・他damage等の前提が崩れたら自動的に期待値を緩めない。
- GUI toggle→server canonical→実攻撃結果→clientのcanonical/Soul/HP→通常HUD/F2を対応付ける。親OFFでも極意自身の保存されたON値が変わらないことをBで確認。
- 待機/GUI/再ON/保存でprepare回数1、取得Lv1×2、SP0/103が不変。Soulの自動初期化/治療はなし。helperのwrite経路はprepareと明示hitだけに限定して事前に静的確認する。
- PASSは「実clientに接続した検証トリガーによる通常Cubeの3条件・表示/同期・取得準備/保存の限定確認」。自然boss攻撃、全49ケース、force、Soul閾値1/10、強制死/respawn、購入成立、上限までの回復挙動、全TrialへのPASSにしない。

### 8.6 保存・再読込・終了

1. C後は両ONへ戻しseal。snapshotの最終期待はSoul0.06、実効HP11、最大HP20、取得2node Lv1、DisabledSkills空、SP0/103、Root/他skillなし。
2. 通常Save & Quitでtitleへ戻る。閉じた新規worldのlevel.dat/UUID.datをreadonly保全・比較。singleplayer ownerは`PlayerList.load`でworld Data.Playerを優先し得るため、UUID.datだけを正本と決めつけない。両者がある場合は保存時刻と対象UUID・canonical/Soul/Healthの整合を確認する。違いは書換えて合わせず記録。
3. **今回新規作成した同じworldだけを1回通常再読込**し、prepare/hitを呼ばず、通常GUI・client/server snapshotで上記値の保持を確認。永続receiptはprepare1/activate3/SEALEDのまま。これが別JVM persistence試験や既存True Root lifecycle決定の代替ではない。
4. 再び通常Save & Quit→Minecraft終了。log、最終保存、当該Java process終了を確認。Prismの認証情報は触らず、本番/過去worldへ移動しない。失敗時も安全に通常終了して部分結果と原因を残す。
5. 製品/配置Jarのhash一致と補助Jar非混入、source/購入gate不変を確認し、Statusと本計画へ実施主体・PASS/FAIL/UNVERIFIED・保存終了結果を反映する。

### 8.7 検証専用補助ファイル（今回作成済み）

前runの実体は保全済みの build/verification/trial-client-20260915-142100/。新runは build/verification/trial-client-20260915-153300/ 配下に必要sourceだけをコピーし、run識別とreadonly区画/接近guardを追加した（§8.10）。製品src/main/既存test source/build.gradleは無変更。

| 予定相対パス | 役割 |
|---|---|
| `fixture/src/main/java/com/leva/foodhealing/verification/clienttrial/TrialClientVerification.java` | 別MOD登録、readonly inspect、明示prepare1回、world/UUID/状態guard、A/B/C各activate1回と封印、server event観測/receipt。常時付与・HP/Soul setterなし |
| `fixture/src/main/java/com/leva/foodhealing/verification/clienttrial/TrialClientObservation.java` | client側の実LocalPlayer/capability/Soul/HPのreadonly観測。Dist.CLIENTへ隔離し、値変化/低頻度のJSONL記録だけ |
| `fixture/src/main/resources/META-INF/mods.toml` / `pack.mcmeta` | 検証用の別MOD ID `foodhealing_trial_client_verification`、対象FHR3.0.0/Trial1.4.9と実行Forge47.4.0を宣言。製品metadataを編集しない |
| `trial-client.init.gradle` | 既存ローカルGradle/ForgeGradleへ検証用source set・専用Jar/reobf taskだけを一時追加。compile classpathは既存Minecraft/Forgeのmapped cacheと実FHR Jar・Trial参照。main.output依存を付けない。既存build.gradleは無変更 |
| `run-allowlist.json` / `audit/prepare-receipt.json` / `audit/attack-journal.json` | 実path・UUID bind・hash・一度だけの準備/各case消費/封印。PREPARINGやFIRINGで失敗しても自動retryなし。tokenは検証run識別用で認証情報ではない |
| `audit/server-snapshots.jsonl` / `audit/client-snapshots.jsonl` / `audit/damage-events.jsonl` | 実測値の観測元・時刻/gameTime・case対応、イベント回数/amount。値をゲームへ返して補正しない |

検証専用Jarは同runの`artifacts/foodhealing-trial-client-verification.jar`へ出力し、**新規instanceのmodsだけ**へ配置。最終Food Healing Jarはコピー前後hash固定。専用task graphを先に確認し、製品`jar/reobfJar/build`や既存suiteを含めない。製品再生成につながる場合は検証用build設定だけを修正してから実行。`--offline`で必要なcache不足があれば正確なファイルを報告し、downloadや製品変更へ進まない。

補助Jarのclass/metadataを別出力で検査し、製品Jar/sourceが不変であることを前後manifestで確認。49ケースの既存dedicated fixture Jarは実clientへ配置しない（自動試験/停止処理を誤作動させない）。helperの作成・compile/reobf・guard確認は今回実施。初回補助の修正経緯は§8.9、新runの限定変更は§8.10を参照。製品ビルド/59/49は実行していない。

### 8.8 実行環境・不足・次の承認で進める範囲

[今回の限定read-only配置照合](../build/verification/trial-client-plan-20260915-135500/audit/local-inputs.json):

- 既存検証Prism: `<LOCAL_PATH>/food-healing-mod-main/build/verification/direct-jar-20260913-114729/launcher/prismlauncher.exe`。
- 同launcher内のlocal Java releaseは **17.0.15**、Minecraft1.20.1/Forge47.4.0/LWJGL3.3.1の既存component metadata、client/universal/installer Jar、asset index5を確認。Forge version.jsonの29ライブラリは既知libraries内に全て存在。全asset objectの総走査や起動検証はしていない。
- Food Healing最終Jarは今回再測定して **205,688 bytes /131 entries /SHA256 8F4AB9F60C2B0D64CF2902944248261A806759BF57CA308EA4273FBC950C3529**。Trial原物も承認1.4.9/既知hash一致。再提供不要、Q1/Q2再判断不要。Curios等のoptional MOD追加や新downloadは予定しない。
- 新規instance `FHR_Trial_Client_<runId>`を同launcher配下へ作り、modsはFHR原物コピー・Trial原物コピー・上記別fixtureのみ。既存world/instanceをコピーしない。共有local runtime/libraries/assetsと非認証component情報を利用する。既存認証済みアカウントはPrism自身に使用させ、認証ファイルの読取/コピーなし。
- 補助Jar/allowlistと新規instance/worldは作成・使用済み。初回のB送信前環境事故/死亡（§8.9）を保全したうえで、安全な別runのA/B/C・seal/再読込を完了（§8.10）。購入停止を変更せず取得済み状態の効果を確認した。中断runの再付与/再試験なし。
- 今回は既存Prism認証のまま起動し、再認証/人間のゲーム操作依頼なし。Computer UseでGUI/command/F2/終了を実施。本人認証が必要なら本人へ依頼し、認証情報の読取/コピーや自動ログインはしない。
- 追加のruntime/asset欠損、対象Jar差異、fresh不成立、観測値の不一致は該当段階で中止し、実測と不足を報告。補助実装の通常エラーは承認された補助内で修正できるが、製品不具合・新仕様判断は記録して別承認を待つ。既存59/49の再実行、force/別攻撃/他機能の追加はしない。


<a id="trial-client-result-20260915"></a>

### 8.9 前runの実client結果と中断（2026-09-15 14:52 JST・保全履歴）

**総合: 未完了 / 環境安全準備の失敗で停止。A限定PASSは保持。**
詳細は[Status今回履歴](CODEX_STATUS.md#evidence-trial-client-20260915)、数値は[measurement-review.json](../build/verification/trial-client-20260915-142100/audit/measurement-review.json)。

| 段階 | 実測・判定 |
|---|---|
| 取得準備 | PREPARED count1、2node Lv1、人工credit103/spend103、SP0/103。fresh/HP20/Soul0確認。両極意readiness=false。GUI購入PASSではない |
| A両ON | LIMITED PASS。Soul0→0、HP20→17、最大/attribute20。hurt/damage各1回amount3、cube_attack/Cow owner/非cancel。実LocalPlayerは0.084053秒後に一致、HUD17.0/20.0 |
| B親だけOFF | GUI→server/clientの親OFF/極意ON同期だけ一致、SP不変、攻撃前HP17/Soul0。その後slime被弾。Cube Bは送信/実行せずUNVERIFIED。期待0→0.03/17→14は未測定 |
| C極意だけOFF | NOT RUN / UNVERIFIED。期待0.03→0.06/14→11は未測定 |
| seal/再ON保持/再読込 | NOT RUN。journal cases[A]/activateCount1とfailureを保持。SEALEDへの書換え、再付与、HP/Soul修復、respawn、再入場なし |
| 失敗時保存/終了 | 死亡画面から通常タイトル復帰。14:40:42全dimension保存、14:41:23 Quit Game、PID34424終了。level.dat Data.Player/UUID.datは取得2node・SP0/103・親OFF/極意ON・Soul0・Health0。成功時の通常再読込PASSではない |

事故は14:39:37からのminecraft:mob_attack amount2×9回で、画面/logはslimeによる死亡を示す。入場時の自然spawn済みslimeを確認していたが、agentがmob生成OFFだけで安全準備を完了扱いにしたのが不足。既存mobは消えず、発動時の4block内チェックだけでは待機中の接近も防げなかった。製品Soul強制死や未決定lifecycleの意図的試験ではない。事故を消したり、成立済みAを未実施へ戻したりしない。

**当時の次回提案（下記は14:52の記録。その後の利用者承認により§8.10で実施済み）**: 別run/別新worldの生成前に通常GUIのゲームルールでmob生成と自然回復をOFFにする。入場後は既存敵対mob不在と、待機中にも侵入されない安全な測定区画を確認してからprepareする。HP/Soulや防御を強化して合わせない。失敗world/journal/receiptは保全し再利用しない。A限定PASSは保持し、新runでB/Cへ到達するためのA準備攻撃は必要理由のある別証拠として扱う。残るB/C・seal/保存/再読込を追加承認後に進める。59/49再実行やQ1/Q2再質問、他機能へ広げない。

<a id="trial-client-safe-result-20260915"></a>

### 8.10 安全準備修正後の新run実績（2026-09-15 16:10 JST）

**REAL CLIENT LIMITED PASS — 通常Cube 3条件・両ON非治療・seal/保存再読込・正常終了を完了。**
run `20260915-153300`、instance `FHR_Trial_Client_20260915-153300`、world `FHR_Trial_20260915_153300`。
対象は同じ製品Jar8F4AB9…C3529 / MC1.20.1 / Forge47.4.0 / Trial1.4.9 / Java17.0.15。
§8.9のA限定PASS・失敗worldは保全し、旧run再利用なし。旧verification root内のreceipt/journal/補助/証拠57ファイルは開始前manifestとhash不変である。

安全準備は生成前GUIで2ゲームルールOFFを記録してから作成。入場後、通常fillで中心[3,-60,1]の25×25閉ガラス区画（床y-61・四壁・天井y-56、1634block）を準備した。初回inspect/prepare前/再読込後の欠損0・32block内敵対0、HP20/MAX20/Soul0/freshから開始。Cow ownerは8block先、Cube範囲と競合しない。prepare後はreadonly区画/接近確認を20server ticksごと・各操作前に行い、侵入/外部numericなら失敗停止する。ゲームのdamage取消・回復・Soul修復は追加していない。

| 段階 | 実測・判定 |
|---|---|
| prepare | 明示1回、通常浄化Lv1/極意Lv1、credit103/spend103、SP0/103。HP/Soul/外部flag/不正pending修復なし、readiness両false。通常GUI購入の証拠ではない |
| A 両ON | 1回の基準準備として実activate。期待/実測 Soul0→0、HP20→17。client収束0.0632733秒、HUD17.0/20.0。限定PASS |
| B 親OFF/極意ON | 通常GUI→製品同期。極意自身ON維持。期待/実測 Soul0→0.03、HP17→14。client収束0.0640446秒、HUD14.0/20.0。限定PASS |
| C 親ON/極意OFF | 通常GUI→製品同期。期待/実測 Soul0.03→0.06、HP14→11。client収束0.0891157秒、HUD11.0/20.0。限定PASS |
| 共通numeric/同期 | MAX属性/getMaxHealth20、Hurt/Damage各1回×3、amount3、cube_attack/指定Cow owner、非cancel。canonical/SP/外部flagもclient/server一致。foreign numeric0。§8.5許容差と5秒条件を満たす |
| 両ON復帰/seal | 通常GUIで両ON、追加攻撃なしでSoul0.06/HP11保持。15:56:16 SEALED、prepare1/activate3。再ONによる治療なし |
| 保存/再読込 | 15:56:40全dimension保存、15:57:58同新worldを1回通常再読込。prepare/hitなしで取得2node Lv1/SP0/103/両ON/Soul0.06/HP11、GUI・server/client一致。receipt/journal byte不変 |
| 最終保存/終了 | 16:01:28全dimension保存、16:01:52 Quit Game、PID13168終了。両保存のlevel.dat Data.Player/UUID.datはSoul0.05999999865889549・Health11・DeathTime0・MAX属性20、canonical一致 |

証拠の正本: [measurement-review.json](../build/verification/trial-client-20260915-153300/audit/measurement-review.json)、[生成前GUI/区画を含むStatus履歴](CODEX_STATUS.md#evidence-trial-client-safe-20260915)、[初回保存](../build/verification/trial-client-20260915-153300/audit/first-save-values.json)、[最終保存](../build/verification/trial-client-20260915-153300/audit/final-save-values.json)、[終了log](../build/verification/trial-client-20260915-153300/audit/lifecycle-excerpts.txt)、[process](../build/verification/trial-client-20260915-153300/audit/process-final.json)。数値は未変換内部HPと呼ばず、§8.4の意味で扱う。

補助は§8.7の構造を継承しrun識別とreadonly安全guardだけ変更、client observerは同一source。[差分](../build/verification/trial-client-20260915-153300/audit/helper-source.diff)。新allowlist/receipt/journalを独立作成。ローカルGradle8.1.1のoffline専用taskだけcompile/reobf成功（[graph](../build/verification/trial-client-20260915-153300/audit/task-graph.txt) / [log](../build/verification/trial-client-20260915-153300/audit/build-helper.log)）。新補助MOD version `1.0.0-run20260915-153300`、21,135 bytes/11 entries/2 classes、SHA256 `538A8B7489A3E6E0D3979C5415073C63151A47B6CFFEF832115B0635398D99C0`。製品Jar/source/既存test/build.gradle/Trial原物は不変、旧49fixture非配置、製品build/59/49再実行なし。[前後照合](../build/verification/trial-client-20260915-153300/audit/artifact-source-final-review.json)。初回optionsのsimulationDistance=4が範囲外ERRORになった記録を残す。補助compile失敗・攻撃失敗・値補正はなし、全WARN解消とはしない。

Computer UseがGUI/command/F2/保存再読込/終了、AUTOMATEDが明示prepare/実activate/独立readonly採取・証拠照合を担当。HUMAN操作/再認証依頼なし。限定3条件のPASSを自然boss/Cube描画/force/閾値死/回復頭打ち/別攻撃/全版/別JVM/TCP/実2-clientへ拡張しない。
この単位は終了。両極意全体の購入停止・RC=NOは維持し、次の対象/実行範囲は利用者指定待ち。L2/真実/別Trial攻撃へ自動では進まない。L2を次に選ぶ場合は§4の正式対象版と実依存artifactが必要。同じ計画や3条件試験を理由なく再作成/再実行しない。
後続参照：上記は16:10時点の履歴。受領・仕様LOCKは§10.1–10.7、今回のL24種実装・自動統合完了は§10.8。

## 9. 過去の準備履歴と維持gate

2026-09-13 21:06時点はREAD ONLY調査だけで、Trial1.2.8展開物・元Jar未確認、Q1/Q2未決定、効果未実装だった。
「Q2: 自身ONなら通常浄化OFFでも有効」は**当時の未承認提案であり、2026-09-15利用者により不採用**。
旧1.2.8の通常add1箇所案も今回の1.4.9には適用しない。[変更前計画全文](../build/verification/trial-monolith-20260915-125400/before/docs/MASTERY_IMPLEMENTATION_PREPARATION.md)と[当時Status記録](CODEX_STATUS.md#evidence-mastery-preparation-20260913)を保全。

既存57/57・SW GLOCK-17限定実client・True Root確認済み範囲・移行3bootの証拠を当時の範囲で保持。
RC=NO、REAL 2-CLIENT=BLOCKED、ALL TACZ/ALL GUNPACK=NOT TESTED、Flight/Break Realm/Bulwark/試作型機関弩/FOURTH BOOT等の個別開始条件を維持。
食料生産の極意の可逆クラフト増加は既知かつ許容仕様でバグ修正対象外。死亡drop/replay/desync等の意図しない重複は許容しない。

<a id="mastery-l2-2519"></a>

## 10. L2 Hostility 2.5.19 — 実物調査・限定実装・自動統合

実装結果更新: **2026-09-15 19:35 JST**。4種のAdapter/build/fixture/新規隔離自動検証を承認され、**限定実装・最終Jar95/95まで完了**。受領/静的読取は§10.1–10.7、実装と実測は§10.8。当時の実clientはNOT RUN。後続§10.9の旧中断は§10.10、新runの現行結果は[§10.11の限定実client完了](#l2-client-completed-result)。両購入停止・Trial旧実client証拠を維持。

### 10.1 原物、依存と確認範囲

原物: `<LOCAL_DOWNLOADS>/l2hostility-2.5.19.jar`。
**1,410,582 bytes / 1,300 entries**、SHA-256 **168665D887B34C5BD79311F0F302531CBED3F1954C43D59C23FF8564DC16C65B**。
ID `l2hostility`、displayName `L2 Hostility`、mods.toml `${file.jarVersion}`、Manifest `Implementation-Version: 2.5.19`。提供値と現物が一致。ファイル名からloader版を推測していない。
[現物metadata/hash（同梱を含む）](../build/verification/l2-preparation-20260915-165000/audit/artifacts.json)、[限定探索先](../build/verification/l2-preparation-20260915-165000/audit/search-scope.json)。libs、run/mods、既知のsource-world-boot/source-runtimeの直下mods、Downloads直下の関連Jarだけ。元world/Prism設定/認証/広域cacheは探索・変更しない。

| 必須依存 | 要求 | 今回の現物確認 | そのファイルの必須依存 |
|---|---|---|---|
| l2library | `[2.5.0,)` | `<LOCAL_DOWNLOADS>/l2library-2.5.3.jar`、ID l2library / Manifest **2.5.3**、915,071 bytes /312 entries、SHA **C36A9C5C93B6114C15AEA11D084600A8ED230F179F1B70D6222633A50F77FC18** | Forge `[46.0.0,)`、MC `[1.20,1.21)`。内包7候補とその内包1候補は§10.7 |
| l2complements | `[2.6.1,)` | `<LOCAL_DOWNLOADS>/l2complements-2.6.1.jar`、ID l2complements / Manifest **2.6.1**、1,211,184 bytes /1,538 entries、SHA **2AAE9D5FC4286F2952939F9EDDA636AD85D6A9451C960D1C2ECE6732B8862E43** | Forge `[47.1.0,)`、MC **`[1.20.1,1.20.2)`**、l2library `[2.5.0,)`。Damage Tracker **0.4.4**内包。Hostility/Curse of Pandoraはここからはoptional |
| curios | `[5.3.4,)` | 既存`curios-forge-5.12.0+1.20.1.jar`、ID curios / Manifest 5.12.0+1.20.1、394,715 bytes、SHA **E2EDACD8DD16FB4172B517A2FA1B642DFFF45B6E94712E468ED5B8F7D673AA03** | Forge `[46,)`、MC `[1.20,1.21)`。追加必須MODなし、同梱Jarなし |
| patchouli | `[1.20.1-81-FORGE,)` | 既存`Patchouli-1.20.1-84-FORGE.jar`、ID patchouli / Manifest 1.20.1-84-FORGE、642,506 bytes、SHA **E883F33AE0E5EB128B36E145072027E620E9992E24809DC07BF4E7AC195B9519** | Forge `[47.1.3,)`、MC `[1.20.1,1.21)`。追加必須MODなし、同梱Jarなし |

既存2MODは `<LOCAL_PATH>/food-healing-mod-main/build/verification/source-world-boot-20260906-172148/game/mods/` と `source-runtime-20260906-165001/game/mods/` の双方で同hashを再確認。**再提供不要。今回の新規隔離modsへのコピーのみ、原物は不変**。本体はForge `[47.1.0,)`、MC `[1.20.1,1.21)`、javafml loader `[46,)`。cataclysm `[2.02,)` はoptionalで不足必須物に含めない。

| Hostility本体内包（JarJar、追加候補は§10.7） | 現物 / SHA-256 | 内部宣言 |
|---|---|---|
| l2damagetracker 0.4.3 | 171,571 bytes / **DF1752AAE0894DC44F3492C6CB17D556D21ED822F26F10A0B7DA086870730A18** | Forge `[47.1.0,)`、MC `[1.20,1.21)`、l2library `[2.4.0-a0,)` 必須。本体の2.5.0以上がより強い条件 |
| l2modularblock 1.1.0 | 40,624 bytes / **2C4AE37C1EF827EE90FC58F760A0F0DB4348886620B95E5CE1C0B4988758CEF6** | mods.tomlなし、Manifest `FMLModType: GAMELIBRARY`。独立MOD IDがあるとは扱わない |
| mob_weapon_api 0.2.13 | 180,797 bytes / **BC4A727B49A4E6B3813CD3060B2A23ED6C7794FF2A2F0F628282258F62A9A982** | Forge `[47.1.3,)`、MC `[1.20,1.21)` 必須。cataclysm `[2.62,)` とl2complements `[2.6.1,)` はこの内包物からはoptional |

3内包にさらに内包Jarはない。JarJar rangesはそれぞれ`[0.4.3,)` / `[1.1.0,)` / `[0.2.13,)`。配布物の内包候補であって**実行時に選択された版ではない**。外側へ重複配置しない。既知のForge47.4.0 client/universalを[現物再確認](../build/verification/l2-preparation-20260915-165000/audit/runtime-presence.json)し、読めた宣言はMC1.20.1/47.4.0で満たす。前提2MOD自身の要件は§10.7で確認済み。両方のTracker要求範囲に適合する同梱候補は0.4.4。今回の実ロードは§10.8で確認し、Trackerは0.4.4を採用した。

### 10.2 登録・帰属・代表traitの実処理

原文bytecodeは[一覧](../build/verification/l2-preparation-20260915-165000/audit/l2-class-index.txt)と`audit/bytecode/*.txt`に保存。`javap`だけで読取、class初期化/ゲーム起動なし。DownloadsのJar直接読取ではZipFS終了時AccessDeniedが出たため、必要classのbytesだけを一時ファイルとして再読取し、[再読取exit0を記録](../build/verification/l2-preparation-20260915-165000/audit/javap-review.json)。原物hash不変、依存をmodsへ展開したものではない。

登録は`init.registrate.LHTraits`→`LHRegistrate.regTrait` / `TraitBuilder.register`（namespace l2hostility）。traitは`MobTraitCap`の`LinkedHashMap<MobTrait,Integer> traits`に保持。`LHAttackListener.onHurt(AttackCache,ItemStack)`が**実attacker**のcapを取得して`traitEvent`を呼び、受け手は`AttackCache.getAttackTarget()`。前フェーズで同梱0.4.3のgetterを確認し、今回§10.7で0.4.4の当該API classがbyte同一と追加照合。見た目のmob名・DamageSource名だけで本人/所有者を推定しない。

今回実装した対象は次の**4 IDだけ**。P=浄化の本人保護、T=真実の恒久無効化。クラス名の共通prefixは `dev.xkmc.l2hostility.`。

| ID | 実class/methodと作用 | Pの実装境界 / Tの対応 |
|---|---|---|
| `l2hostility:poison` | `content.traits.base.TargetEffectTrait.postHurtImpl(int,LivingEntity attacker,LivingEntity target)`。LHTraits `lambda$static$81/$80`がPOISONのMobEffectInstanceを生成、duration=`poisonTime*rank`。実際の追加は`EffectUtil.addEffect(target,instance,AddReason.NONE,attacker)` | 同メソッド内の実addEffect呼出しだけで、登録ID・実受け手ServerPlayer・既存P条件を確認して新規付与を抑止。Reflectionリング経由で別mobへ付く元処理を消さない。Tはこのtraitのmap除去＋marked sourceからの再実行抑止 |
| `l2hostility:slowness` | 同TargetEffectTrait。LHTraits `lambda$static$78/$77`、SLOWNESS、duration=`slowTime`、amplifierはrankをそのまま渡す（rank-1と推測しない） | 同上。ポーション全般や有益効果を対象にしない |
| `l2hostility:corrosion` | `content.traits.highlevel.CorrosionTrait.onHurtTarget`→`SlotIterateDamageTrait.process`→`CorrosionTrait.perform`→`DurabilityEater.corrosion`。既存耐久消耗量×corrosionDurabilityを整数化し追加消耗。対象装備不足時は別に`1+corrosionDamage*rank*(rank-count)`のhurt倍率を追加 | **process内の装備選択後perform呼出しだけ**を保護本人について止める。processの装備選択/個数/RNG/不足時numeric倍率、元hurtを保持。Tはtrait自体を除去/無効化するため当該traitの数値ボーナスも消えるが、通常攻撃・全体level scalingは保持 |
| `l2hostility:erosion` | `ErosionTrait.onHurtTarget`→同process→`ErosionTrait.perform`→`DurabilityEater.erosion`。最大耐久−現在消耗の残耐久×erosionDurabilityを整数化し消耗。装備不足時の別hurt倍率もある | corrosionと同じ境界でerosion呼出しだけ本人保護。Tはこのtraitだけを恒久無効化 |

[登録bytecode](../build/verification/l2-preparation-20260915-165000/audit/bytecode/init.registrate.LHTraits.verbose.txt)、[TargetEffect](../build/verification/l2-preparation-20260915-165000/audit/bytecode/content.traits.base.TargetEffectTrait.txt)、[Corrosion](../build/verification/l2-preparation-20260915-165000/audit/bytecode/content.traits.highlevel.CorrosionTrait.txt)、[Erosion](../build/verification/l2-preparation-20260915-165000/audit/bytecode/content.traits.highlevel.ErosionTrait.txt)、[共通slot選択](../build/verification/l2-preparation-20260915-165000/audit/bytecode/content.traits.highlevel.SlotIterateDamageTrait.txt)、[耐久作用](../build/verification/l2-preparation-20260915-165000/audit/bytecode/content.item.traits.DurabilityEater.txt)。4種のinitialize/postInitはMobTraitのno-opを継承し、mobへの独自attribute/dataの取得後処理は確認されない。**既に受け手に付いた別sourceの効果や消耗を治療する案ではない**。

追加読取で判明した境界（初回の4 IDへ混ぜない）:

- `l2hostility:pulling` / `repelling`：`PushPullTrait.tick`はserverのLivingEntityとclientのlocal Playerを別々に列挙してpushする。serverだけ抑止して実client保護完成としない。対象1人のpushだけを抑止する将来案であり、tick全体取消は他playerへ影響する。Repellingの`onAttackedByOthers`は別のprojectile防御で、浄化の本人保護へ混ぜない。
- `l2hostility:dispell`（IDはdispellingではない）/`dementor`は装備・DamageSource状態・mob側耐性等の複合処理。`killer_aura`には実数値hurtとtrait効果の別呼出しがある。`ragnarok`等も今回は支持完了とせず後続。これらの全trait/全boss調査を4種の開始条件にしない。
- `l2hostility:speedy` / `tank`の`AttributeTrait.initialize`→`TraitManager.addAttribute`は名前から`l2library.MathHelper.getUUIDFromString`を使いmodifierを作る。単純removeでは後処理を呼ばない。UUID算法は今回§10.7で実物確認済み。ただしそれだけで属性traitの安全なcleanup実装・動作確認とはしない。tank_*やspeedy、独立の`hostility_health`、他MODのmodifierを一括削除しない。これらは今回のstrip allowlist外。

### 10.3 保存、正式な解除、再付与の落とし穴

[MobTraitCap本体](../build/verification/l2-preparation-20260915-165000/audit/bytecode/content.capability.mob.MobTraitCap.txt)、[保存注釈](../build/verification/l2-preparation-20260915-165000/audit/bytecode/content.capability.mob.MobTraitCap.verbose.txt)、[公式command](../build/verification/l2-preparation-20260915-165000/audit/bytecode/content.command.LHMobCommands.txt)を根拠にする。

1. capability holderキーは`l2hostility:traits`。対象述語はWHITELISTまたはEnemyかつ非BLACKLIST。Food Healing側でもsupported Mobに限定しPlayer/arbitrary livingへcapを強制注入しない。`traits/stage/lv`等には`@SerialField(toClient=true)`、`data`にはSerialFieldがある。継承先`GeneralCapabilityTemplate`/`GeneralCapabilityHolder`、`TagCodec`/registry map codecは今回§10.7で確認済み。trait payloadは実field名`traits`のCompound、登録ID→IntTag rank。この通常形式を今回§10.8で実保存・再読込確認した。**部分NBTの手注入を正式removeの代用にしない**。
2. 自然付与は`MobTraitCap.init`→`TraitManager.fill`→`TraitGenerator.generate/setRank`の**map直接更新**→各initialize。tickはPOST_INITとpostInitを扱う。公式command/wandの`setTrait`はpendingへ積むだけ。`clearPending(entity)`はmapへ反映→initialize/postInit→rank0をremoveし、tickで必要時syncする。`copyFrom`にもmap直接更新がある。**setTraitだけのhookでは全再付与を止められない**。
3. 公開`removeTrait(trait)`は存在するが、非ticking時はmap.removeだけ、ticking中はsetTrait(trait,0)へ遅延する。存在しないtraitのpending付与はこのremoveで消せない。公式commandRemoveTraitもこれを呼ぶだけ。全trait共通の安全なcleanup/恒久禁止APIとは扱わない。`deinit/reinit/traits.clear`や`TraitManager.fill`を除去手段にすると無関係データ・scaling・HP再設定まで巻き込み得るので使わない。
4. 同期は`syncToClient`→`MobCapSyncToClient`→`TagCodec.toTag(...toClient)`→tracking players。`CapabilityEvents.onStartTracking`もsyncToPlayer。clientは`ClientCapHandler.handle`→fromTag。公開removeを外から呼ぶだけでは即syncは保証されず、変更確定時に元syncを呼ぶ必要がある。依存codec/配送先/main-thread dispatchは今回§10.7で静的確認済み。空mapの元payload/codecと実保存再読込は§10.8で限定確認。

### 10.4 本人保護と恒久無効化の実装境界

**P（浄化）**は既存`PurificationMasteryController.isEnabled`と本人server canonicalを使う。両取得/ON・pending否定・未知/不正データ拒否、親OFF時の極意保存ON保持を維持。元mobのtrait/map/data/属性は変更せず、他player・非保護本人には元の作用を1回。広いMobEffectEvent取消・tick後消去・LHAttackListener全取消を使わない。Pの4種に新たな仕様質問は不要。

**T（真実）**の効果時条件は[Skill Tree §10](SKILL_TREE_SPEC.md#truth-mastery-activation)で**3つすべて取得/ONにLOCK**。どれかOFFなら新規適用を止め、既存無効化と各保存toggleは維持。範囲/恒久性/対象外はSkill Tree §10を変更しない。今回のstrip/nullifyはPと同じ4 IDだけ。

- serverで対象mobの固有persistent dataにFood Healing所有のID別無効化記録を持ち、安全なmap反復外で4 IDだけ公開removeし、元syncへ接続する。既存stage/lv/data/HP/通常属性/AI/loot/所有関係は保持。対象外のtrait、別MODの能力や有益効果を消さない。
- marker単体では不十分。init/copyFrom/clearPending後の再照合と、**効果実行直前のmarker判定**を併用し、公開setTrait/command・自然再生成・同mobへの再付与後も作用を戻さない。pending全体の破棄やcapability全消去はしない。Corrosion/ErosionはonHurtTargetのtrait固有処理と継承postHurtImpl経路、TargetEffectは実addEffectまでを押さえる。具体的なMixin descriptor/呼出し数を実装時に固定して変換結果を確認する。
- 4 IDは独自mob modifierを持たないため、初回はspeedy/tank等のcleanupを必要とするtraitを対象にしない。永続記録とmap減少、元の通常attack/scaling維持、再付与後の実作用不成立が合格条件。保存は既存entity保存を利用する案で、player schemaを変えない。確認済みのnative map codecと元syncを使い、保存/同期payloadの欠落・部分再注入による他field変更を避ける（§10.7）。
- spawn/entry/teleport/player移動をserverの候補集合＋低頻度再照合で扱う。block総走査/全entity毎tick走査/範囲外chunkの強制loadなし。±75境界・重複player範囲・再入場は冪等。新しいmobへの継承を勝手に追加せず、そのmobの範囲条件を別に評価する。

### 10.5 今回の実装箇所・合格条件

**初回4 traitのみ、購入停止を維持したoptional Adapter＋隔離自動検証を実施完了**。実装開始承認待ちは解消。仕様・依存調査をやり直す必要はない。旧17:57時点の予定は開始前コピーに保全した。

- `TruthMasteryController.java`：LOCK済み3つ取得/ON、±75のinclusive位置判定。
- `compat/l2hostility/`：`L2HostilityAdapter`、`L2HostilityCompatibility`、`L2HostilityVersions`、`L2HostilityMixinPlugin`、`L2MobTraitAccess`。外部参照の隔離、pure版判定、ID別永続無効化と範囲入口。
- `mixin/l2hostility/`：`TargetEffectTraitMixin`、`SlotIterateDamageTraitMixin`、`EquipmentDamageTraitMixin`、`MobTraitCapMixin`。専用JSONと既存refmapへ接続。
- `build.gradle`：実物compileOnlyと別l2HostilityTest source set/reobf。javacに必要なRegistrate/l2serial/Trackerのみ内包hash照合後build/l2-compile-onlyへ抽出、実modsや製品へ同梱しない。
- `src/l2HostilityTest/`に別fixture、`TruthMasteryRegression`と既存unit入口、`FoodHealingPrerequisiteGameTests`へ新規1件。既存59ケースを削除しない。

[TEST_PLAN §18](TEST_PLAN.md#l2-2519-focused-plan)を合否基準に、実登録traitの正式付与→本物のZombie通常attack/event/tickを使用。最終95件の内訳と回帰・限界は§10.8。購入/費用/Root/共通damage/schema/packet/旧Trial classの変更なし。

### 10.6 残る不足を分離

| 分類 | 現在地 / 次の候補 |
|---|---|
| **ファイル待ち：今回の最小構成はなし** | 承認本体/依存で実ロード成立。原物要求の書換え・新download・内包の外側重複配置なし |
| **依存内部読取・今回の限定実装：完了** | §10.7の調査を§10.8の実装・実attack/保存同期へ接続済み。再度資料待ちへ戻さない |
| **仕様判断待ち：今回4種に追加なし** | 取得100/500SP、P本人条件、T3つ取得/ONと恒久性、各保存toggle非連動を変更しない |
| **次の開始承認待ち** | L2新run A/B/T/C・保存再読込は§10.11完了。侵略者限定実clientは§11.7の旧結果と§11.8のS2/H1・seal/cleanup/保存終了で限定完了。§12.6の2 IDは採用承認・コード反映済み、§13で自動検証完了、新2 ID実clientは別工程。他trait/他MOD/購入解放へ進まない |
| **未確認** | 自然AI/持続DOT・全装備/全toggle/境界全軸、TCP/実2-client、別JVM再起動、未対応版実物、L2＋Trial同時構成、全trait/全boss、属性trait cleanup、多数entity/player/TPS/長時間。§10.11の4種実client/GUI/HUD/保存確認とは分離 |

最新Jar/現在状態はCODEX_STATUS現在の要約、限定実clientの識別と証拠は§10.11。§10.8は限定自動統合の履歴。両極意全体のIMPLEMENTATION_PENDING/SP保護、RC=NO、他の個別開始条件を維持。

<a id="mastery-l2-dependencies"></a>

### 10.7 前提MOD2版の受領・依存内部追加照合（17:57時点の履歴）

**この節は17:57時点の静的確認履歴。実装/自動統合は§10.8、現行の限定実clientは§10.11。**

更新: **2026-09-15 17:57 JST**。追加受領した2版は§10.1の実パス・ID・Manifest version・SHA-256で固定する。両方のmods.tomlは`${file.jarVersion}`で、loader採用版をファイル名だけから決めない。[実測metadata/全内包hash](../build/verification/l2-dependencies-20260915-174200/audit/artifacts.json)、[既知Curios/Patchouli/Forge/本体の限定再照合](../build/verification/l2-dependencies-20260915-174200/audit/dependency-closure.json)。今回は新download、Jar再包装・mods配置、クラス初期化、ゲーム/build/testなし。前回のL2本体調査とTrial試験は再実行していない。

**依存の結論:** 最初の4 traitについて必要なファイル・確定仕様・静的API確認は揃った。起動・実装・統合PASSではなく、§10.5の実装開始承認が残る。起動時のModList/JarJar実採用版は将来の隔離runで記録する。

| 内包元 / 候補 | JarJar要求範囲 | 追加照合した要件・扱い |
|---|---|---|
| Library / Registrate MC1.20-1.3.11 | `[MC1.20,MC1.21)` | mods.toml/Manifest versionなし、版はJarJar metadata。外側へ追加不要 |
| Library / l2serial 1.2.2 | `[1.2.2,)` | mods.tomlなし。保存/packet codecの実装を読取 |
| Library / l2modularblock 1.1.0 | `[1.1.0,)` | Hostility内包とbyte/hash同一、GAMELIBRARY。2重配置しない |
| Library / l2tabs 0.3.3 | `[0.3.3,)` | Forge `[46.0.0,)`、MC `[1.20,1.21)`、Library `[2.4.0,)`必須、Curios optional |
| Library / l2screentracker 0.1.4、l2itemselector 0.1.9 | 各`[0.1.4,)`、`[0.1.9,)` | Forge `[46.0.0,)`、MC `[1.20,1.21)`、Library `[2.4.0-a0,)`必須 |
| Library / mixinextras-forge 0.2.0-beta.8 | `[0.2.0-beta.8,)` | MOD ID mixinextras。同版MixinExtrasをさらに内包、追加必須MOD宣言なし。二段目も独立配置しない |
| Complements / l2damagetracker 0.4.4 | **`[0.4.4,)`** | 172,221 bytes /163 entries、SHA **1F77C9A835B1D3C5E01F1E36B8DF982F7B475F8438D56C835C5EE2583E3A9469**。Forge `[47.1.0,)`、MC `[1.20,1.21)`、Library `[2.4.0-a0,)`必須 |

Complementsの必須MC範囲はHostilityより狭い`[1.20.1,1.20.2)`で、今回のMC1.20.1は適合する。Hostilityと内包mob_weapon_apiのcataclysm、Complementsのcurseofpandora等のoptionalを必須追加物にしない。内包ごとのID/Manifest/JarJar版・要求・hashを上記JSONに保存した。外側4必須MODとその内部宣言を追った範囲で、追加不足ファイルはない。

**Damage Tracker 0.4.3 → 0.4.4:** Hostilityの`[0.4.3,)`とComplementsの`[0.4.4,)`の共通要求は`[0.4.4,)`。提供された候補のうち0.4.4だけが満たすため、次回の最小構成では**0.4.4が選択候補**。まだ実ロードしておらず、採用実測とは記録しない。
98 classをbyte比較し、変更classは`init.data.L2DamageTypes`だけ。`AttackCache`、`DamageModifier`、関連attack listener/event/accumulator等、必要なAPI classは同一（getter/addHurtModifier等のdescriptor/実装を含む）。ただしJar全体が同じではない。`is_magic`データはIrons Spellbooksのoptional tag参照をvoid/poisonからeldritch/natureへ変更し、TravelOptics等のoptional参照17件を追加している。新必須MODではなく、今回4 traitのAPI調査結果を無効にしないが、0.4.3の全挙動を0.4.4へ転記しない。[全entry差分](../build/verification/l2-dependencies-20260915-174200/audit/damage-tracker-entry-diff.json) / [API・magic tag差分](../build/verification/l2-dependencies-20260915-174200/audit/required-api-comparison.json) / [変更class](../build/verification/l2-dependencies-20260915-174200/audit/damage-types-bytecode.diff.txt)。

**保存・同期（静的に追跡した処理）:**

- `GeneralCapabilityHolder.isProper`は実capabilityの存在確認、`get`はresolve→`GeneralCapabilityTemplate.check`（getThisを返すだけ）。attachはholderの対象class/述語を通る。未対応mobへ新capabilityを注入する案ではない。
- `GeneralCapabilitySerializer.serializeNBT`はTagCodec.toTag、deserializeは既存handlerへTagCodec.fromTagを適用する。field名は実field名。Libraryの`L2Registrate.newRegistry`は`RLClassHandler`を登録し、trait keyはregistry ResourceLocationの文字列、rankはIntTagとなる。`MapCodec`の通常形式は`traits: {"l2hostility:poison": 1, ...}`のCompound。未知ID/不正NBTを修復する保証ではなく、Food Healingから手編集しない。
- `TagContext.deserializeEfficientMap`は**既存mapをclearしてから**キーを復元するため、通常serializerが出した空`traits` Compoundで既存mapも空になる。`traits`自体の欠落と空Compoundは別。generic codecには`@OnInject`後処理があるが、確認済みMobTraitCapの注釈にそのhookはなく、依存Template.checkも初期化・除去・syncを行わない。partial fromTagでは欠落primitiveの既定化も起こり得るため、部分NBT注入を4 IDだけの解除手段にしない。
- `BasePacketHandler.toTrackingPlayers`は`TRACKING_ENTITY_AND_SELF`、登録は`consumerMainThread`。既存MobCapSyncToClientはtoClient対象のCompoundTag、clientは同codecで読込む。**公開remove→変更確定→元sync**を使い、trait全消去時の空map反映、無関係field保全、再tracking/保存再読込は将来の試験で確認する。配送・実client一致は今回は未観測。

[Serializer](../build/verification/l2-dependencies-20260915-174200/audit/bytecode/dev.xkmc.l2library.capability.entity.GeneralCapabilitySerializer.txt) / [registry登録](../build/verification/l2-dependencies-20260915-174200/audit/bytecode/dev.xkmc.l2library.base.L2Registrate.txt) / [registry codec](../build/verification/l2-dependencies-20260915-174200/audit/bytecode/dev.xkmc.l2serial.serialization.custom_handler.RLClassHandler.txt) / [MapCodec](../build/verification/l2-dependencies-20260915-174200/audit/bytecode/dev.xkmc.l2serial.serialization.generic_types.MapCodec.txt) / [TagContext](../build/verification/l2-dependencies-20260915-174200/audit/bytecode/dev.xkmc.l2serial.serialization.unified_processor.TagContext.txt) / [UnifiedCodec](../build/verification/l2-dependencies-20260915-174200/audit/bytecode/dev.xkmc.l2serial.serialization.unified_processor.UnifiedCodec.txt) / [配送](../build/verification/l2-dependencies-20260915-174200/audit/bytecode/dev.xkmc.l2serial.network.BasePacketHandler.txt)。

**EffectUtil・補助API:** `EffectUtil.addEffect`はreceiver/source同一ならSELF、ForceEffectならFORCEへreasonを切替え、instanceを作ってThreadLocal reasonを設定する。通常poison/slownessはLivingEntity.addEffect、instantは即時作用、ForceEffectは別forceAddEffectへ分岐する。force側は`ForceAddEffectEvent`のDENY確認後にAdded通知・active effect map更新/通常callbackを行う。通常の適用可否eventだけで全branchを抑止できるとは扱わない。ForceAddEffectEventにはtrait ID/attacker引数がなく、広い取消は今回案に使わない。§10.2の**Hostilityの実add呼出し直前**で本人/4 IDを判定すれば、抑止時にEffectUtil内部を書き換える必要がなく、対象外の元callを1回通せる。MathHelper.getUUIDFromStringは`Random((long)name.hashCode())`のnextLong2回からUUIDを作る。UUID.nameUUIDFromBytesとは異なるが、speedy/tankは初回対象外のまま。

Complementsの`LCEffects`登録も照合：`l2complements:flame`=FlameEffect、`frozen`=IceEffect、`armor_reduce`=ArmorReduceEffect、`stone_cage`、`curse`、`bleed`、`cleanse`等。`armor_reduce`というeffectを`l2hostility:corrosion`というtraitと混同しない。初回poison/slownessはvanilla効果であり、Complementsの他effect/force作用を今回の対応範囲へ追加しない。[EffectUtil](../build/verification/l2-dependencies-20260915-174200/audit/bytecode/dev.xkmc.l2library.base.effects.EffectUtil.txt) / [MathHelper](../build/verification/l2-dependencies-20260915-174200/audit/bytecode/dev.xkmc.l2library.util.math.MathHelper.txt) / [effect登録](../build/verification/l2-dependencies-20260915-174200/audit/bytecode/dev.xkmc.l2complements.init.registrate.LCEffects.txt)。

次回は§10.5/TEST_PLAN §18を実装・隔離自動検証する承認があればそこから開始可能。実ロード時に別候補や未知のAPI不一致が出た場合は記録し、要求version書換え・偽classで合わせない。今回の追加照合をL2起動/効果実装/試験PASSや両極意購入解放に昇格しない。[今回の不変・文書照合](../build/verification/l2-dependencies-20260915-174200/audit/final-review.json)。

<a id="l2-implementation-result"></a>

### 10.8 2026-09-15 19:35 JST — 代表4 trait限定実装・自動統合の実施結果

**LIMITED IMPLEMENTED / AUTOMATED INTEGRATION TESTED。実clientはNOT RUN、両購入停止とRC=NOを維持。** 作業root `build/verification/l2-implementation-20260915-182900`。新規server/worldだけを使用し、原本/過去world・Prism・認証情報・製品Config・LOCK条件には触れていない。Q1/Q2や他機能を再判断しない。

**実ロードと成果物**

- MC1.20.1 / Forge47.4.0 / Java17.0.7、Hostility2.5.19、Library2.5.3、Complements2.6.1、Curios5.12.0+1.20.1、Patchouli1.20.1-84-FORGE。[配置全hash](../build/verification/l2-implementation-20260915-182900/audit/final-l2-fixed-inputs.json)、[ModList/実resource origin](../build/verification/l2-implementation-20260915-182900/final-l2-fixed/l2-result.json)。Outer7 Jar=製品＋別fixture＋本体＋前提4MOD。
- 同梱実採用：**Tracker0.4.4**（Complements由来）、mob_weapon_api0.2.13、l2tabs0.3.3、l2screentracker0.1.4、l2itemselector0.1.9、mixinextras0.2.0-beta.8。ModListにないGAMELIBRARY等は実class resourceで**Registrate MC1.20-1.3.11 / l2serial1.2.2（Library内）、l2modularblock1.1.0（Hostility内）**を確認。Registrateのloader内部0.0NONEとJarJar版を混同しない。同版のさらに内側のMixinExtras候補も外側へ配置していない。
- [最終配布Jar](../build/libs/foodhealing-3.0.0.jar)：**225,242 bytes /146 entries / SHA256 D6F5032C3B40A78D2BCCEDB554EE1C302AA3C5367080F93E35184A5A96162D9A**。L2/Trial最終配置と同hash。既存製品class/資源はbyte不変、ManifestのMixinConfigs更新＋限定追加。[metadata/refmap/非混入](../build/verification/l2-implementation-20260915-182900/audit/final-jar.json)、[新規・変更一覧と保全比較](../build/verification/l2-implementation-20260915-182900/audit/final-review.json)。.gitなし、git diff確認済みとはしない。
- [実変換済みhook検査](../build/verification/l2-implementation-20260915-182900/audit/loaded-hooks.json)：TargetEffectの元2 addEffect site、Slotの選択後performとpostHurt、Corrosion/Erosion各onHurt、MobTraitCapのinit/copy/pending/tick/反復guardを確認。required injection countを有効化。外部/fixture/GameTests/ExampleModは製品へ入らない。

**製品処理**

- P：実Mob attackerの登録ID/cap rank、実recipient ServerPlayer、正常server本人canonicalを確認。poison/slownessは元addEffect直前で新規付与だけを止める。Corrosion/Erosionはprocessで選択済みslotへのperformを止め、装備count・選択RNG・不足時numeric倍率を保持。他player/反射先・既存有益/有害effect、source trait mapを変更しない。
- T：新規適用は3つ取得/ONのalive server player中心位置±75。`foodhealing:truth_l2`にVersion1・MobUUID・対象ID別booleanを保持。既存の正rank4 IDだけ記録し、公開remove→元syncで除去。対象外map/pending/dataをclearしない。4種はinitialize/postInit no-opで独自modifierのcleanup不要。属性traitは未対応。
- map反復中やnative ticking中は記録だけ先に作り、4種の実作用前guardで遮断、安全なtick境界でremoveする。Corrosion/Erosionのtrait数値ボーナスもTでは止める。範囲外/OFF後の同Mob再付与もguardする。UUID不一致markerを新Mobへ適用しない。
- EntityJoin、init/copyFrom/clearPendingの後、実作用前、mobごとに分散した20tick補完、playerのloaded-entity query（移動最短5tick・静止20tick）で包含を確認。block走査/毎tick全entity総走査/製品からのchunk強制ロードなし。多数entity負荷や候補件数/TPSは未計測、性能PASSを付けない。

**95ケースの内訳（実登録trait/通常attack経路と補助検査を区別）**

| 群 | 件数 | 実測内容 |
|---|---:|---|
| P資格/付与直後 | 36 | 4 ID×9状態。両ON、各OFF/両OFF、各未取得、過大level、pending、未来schema。poison/slownessはtick消去前に観測。Pはsource/canonical不変 |
| 装備条件/RNG | 20 | corrosion/erosion×5装備条件×P ON/OFF。なし/新品/消耗品/2slot/Unbreakable。Pの追加wear0、通常HP損失と次RNGを対照一致 |
| 既存効果/他player・反射 | 2 | 既存poisonとregen、対象外weakness、他player保全。実Reflection ring＋通常Zombie攻撃→毒を受け付けるL2対象Pillagerへ元の毒が届く |
| 購入停止 | 2 | 登録済み購入packet、1000SPでも両node IMPLEMENTATION_PENDING、取得/SP/canonical不変。fixtureの取得準備をGUI購入成功にしない |
| T toggle/不正 | 14 | 全8toggle＋6未取得/不正/pending状態。受け手のPを無効にした対照、marker/mapだけでなく通常攻撃の実作用・numericを確認 |
| 範囲/包含 | 14 | X/Y/Z±75と75.001の12、player移動/重複範囲1、native entity全保存読込→join1。位置進入はteleportTo/移動＋native cap tick補完、自然歩行AIの映像試験ではない |
| 非ゼロlevel | 1 | native setLevel5、T除去後もlevel/属性/AI・傷ついたsourceHP16を保持。無trait同levelとHP損失 **3.300003**で一致 |
| 再付与/反復 | 2 | setTrait→pending、reinit、copyFrom、OFF/範囲外、別Mob/UUID不一致、反復中4件を安全に保持し後から除去 |
| 同期/player保存 | 2 | 元MobCapSyncToClient/TagCodecの空traits Compoundでmirrorをclear、元syncToPlayerの送信をEmbeddedChannelで確認。3toggleの製品packet/sync一致。通常PlayerList保存→新ServerPlayerでcanonical再読込 |
| chunk保存/復元 | 2 | native chunkアンロードをentity不在で確認、通常ロードで別インスタンスに復元。UUID/marker/native cap/HP/foreign modifier/対象外weakness保持、再付与後もpoison遮断・weakness作用維持 |

[最終95件receipt](../build/verification/l2-implementation-20260915-182900/final-l2-fixed/l2-result.json) / [実行log](../build/verification/l2-implementation-20260915-182900/audit/final-l2-fixed.log)。同期のmirrorはserver-side codec検査で実client画面ではない。player/移動eventはfixtureが用意し、自然Mob spawn抽選/歩行AIやGUI購入を成功扱いにしない。

**回帰・途中FAIL・終了**

- [build/foodHealingUnitTest/check](../build/verification/l2-implementation-20260915-182900/audit/plugin-fix-build.log)成功。新Truth **53 assertions**、浄化150、既存前提142・SW47・境界5000・Ammo10000等保持。[vanilla60/60](../build/verification/l2-implementation-20260915-182900/audit/final-vanilla-fixed.log) / [TaCZ60/60](../build/verification/l2-implementation-20260915-182900/audit/final-tacz-fixed.log)は既存59＋新規Truth toggle/sync1、Forge47.2.0 userdevの最終source確認。L2不在で成立。
- 同じ最終配布Jar＋実Trial1.4.9、L2不在の新規Forge47.4.0で[49/49](../build/verification/l2-implementation-20260915-182900/final-trial-fixed/trial-result.json)。既存Trial source/期待値は無変更。実activate47＋helper対象外1＋通常保存再読込1の分類は前フェーズと同じ。
- focused-01はfixtureZombieにLUCKがなく0件FAIL→FOLLOW_RANGEを使う。focused-02はnative login保護中のattack拒否0件FAIL→fixture準備で通常tick65回を進め、isInvulnerableTo否定を事前assert。保護を製品から無効にしない。
- focused-03は57件後、毒免疫Zombieへの反射期待でFAIL。reflection-04はCowにL2 capがなく準備FAIL。非アンデッドEnemyのPillagerへ変更し、canBeAffected・query可視性をassertしてreflection-05限定PASS。製品条件は変更なし。
- focused-06は86件後、queryに見えないfixture遠方chunkのMobで移動確認FAIL。ロード済み位置のMobへplayerが進入する準備と可視性assertへ修正。truth-07で36件PASS。初期のGradle defaultcache/native DLLとsourceSet設定の失敗logも保持し、既存-g cache/offline手順と設定を修正。
- 中間Jar F4E78B44…はL2 95/95・core各60を通ったが、最初のTrial回帰`final-trial`が0件FAIL（準備Soul0.2が0）。新L2 pluginからgameplay classを参照したため、Mixin準備中にEntity/LivingEntityが早期loadされ元Mixinが欠落した。`L2HostilityVersions`へ純粋String版判定を分離して解消。[適用前後の3Mixin証拠](../build/verification/l2-implementation-20260915-182900/audit/test-summary.json)。Trial fixture/既存製品classを修正せず49/49回復後、L2/vanilla/TaCZも最終版で再確認。中間成功を最終結果へ流用しない。
- [全run/log/process集計](../build/verification/l2-implementation-20260915-182900/audit/test-summary.json)：12回の独立serverを全て通常saveEverything/halt→全dimension保存→exit0で終了。失敗run/world/receiptは削除・再利用なし。最終L2 PID7980/Trial PID8740、両log ERROR/FATALなし。GameTestも通常shutdown/Gradle成功。外部metadata、初期config既定補完、dev refmap等のWARNを一括解決したとはしない。

**19:35時点の未確認/次候補（履歴）**：両極意全体のreadiness、対象外trait/属性cleanup・別Trial攻撃、L2実client/HUD/TCP/実2-client、別版実物の起動、別JVM L2再起動、L2＋Trial同時環境、自然歩行/全boss/全pack/大規模負荷。未対応版は純粋version判定拒否を単体確認しただけで、別版Jarによる起動成功ではない。当時の次候補だった実client計画は後続§10.9で策定済み、実行は未承認。自動試験を再作成する工程に戻さない。両購入停止/RC=NOと既存個別gate、可逆クラフト増加の既知許容仕様を維持。

<a id="l2-client-plan"></a>

### 10.9 L2代表4特性の限定実client計画（2026-09-15 20:04 JST）

**承認済み計画 / 新runの限定実client完了**。20:04の計画と21:11の製品引継ぎは承認済み。旧runのA後中断は[§10.10](#l2-client-stopped-result)、観測補助だけを修正した新runの完了結果は[§10.11](#l2-client-completed-result)。下記は手順の正本として保持し、実施判定は§10.11へ一本化する。停止runを再開しない。

#### 対象・環境と限定読取の根拠

- 製品は今回の§11最終Jar **229,423 bytes /150 entries / 3807DECCBA79A122CD3A6431454788944ACDC72FE62A7A9BDB37C189D1B76DE6** を引き継ぐ。計画策定時のD6F5032C…D9Aは旧基準として保全。既存L2/core/Cubeのclass byteは不変、追加4 classはTrial版限定hookでL2単独時は不適用。このJarのL2 95/95・Cube49/49・core各60/60回帰を§11で確認済み。次回準備時に現物hashと追加差分だけ再照合し、古いJarへ巻き戻さない。本体/依存はHostility2.5.19、Library2.5.3、Complements2.6.1、Curios5.12.0+1.20.1、Patchouli1.20.1-84-FORGE、MC1.20.1/Forge47.4.0。Trial/TaCZ/別traitを同時に追加せず、内包候補の重複配置なし。起動後にTracker0.4.4等の実採用を記録する。
- [今回の限定ファイル照合](../build/verification/l2-client-plan-20260915-195200/audit/local-inputs.json)で製品と5つの外部Jar、既知Prism・Java17.0.15・Forge client/universal/installer・MC/Forge metadataの存在を確認。MOD/製品hashは19:35の証拠と一致。asset index5は旧記録とSHA256が異なるが、現物のSHA1/sizeは現在のMC1.20.1 metadataの宣言と一致する。差の発生原因は未確認、旧版へ戻さない。全asset object総走査や今回の起動確認ではない。
- 新規 `build/verification/l2-client-<newRunId>/` と、§8.8の既存検証Prism配下の新規 `FHR_L2_Client_<newRunId>` を使う。共有runtime/libraries/assetsのみ利用。旧instance/world・receipt/journal/allowlist、95件fixture、認証ファイルをコピーしない。既知最小構成のファイル不足はない。実行時に欠損が出た場合だけ実パスと必要物を示し、downloadや別版への更新はしない。
- 準備方式の参照はTrial新runの `TrialClientVerification.prepare/guard`、`TrialClientObservation`、専用init/task graph。L2側は既存 `src/l2HostilityTest/.../L2HostilityVerification.java` の **native付与・攻撃APIだけ** を参考にする。旧fixtureのHP100化、invulnerableTime代入、RNG seed固定、discard、全95ケースを持ち込まない。
- 現行の `FoodHealingSkillIds` / `FoodHealingSkills` / `ShokugiData` / `FoodHealingScreen` / `ToggleSkillPacket` / `CapabilityEvents`、`ShokugiTickHandler.onPlayerTick`、§10.2–10.3の実L2 bytecodeを必要範囲で再照合。通常浄化は **server PlayerTick END / NORMAL** に有害効果を除去する。Corrosion/Erosionのoverrideは各onHurtTarget内のprocessで消耗を起こし、通常の本経路で各1回。EffectUtilやremoveを補助が代行しない。

#### 一度だけの取得準備と書込境界

1. 新しいrun ID/token、解決済み実worldパス、本人UUID、製品/外部/補助Jar hashをallowlistへ固定する。UUIDは今回の本人ログイン後にゲーム内inspectから取得し、旧receiptや認証情報から流用しない。新規worldの正常fresh canonicalが `new ShokugiData().serializeNBT()` と一致し、invalid/pendingなし、Root/軽減/他skillなしを確認。不一致は初期化で直さず停止する。
2. 明示 `/fhrl2verify prepare <token>` **1回だけ**。先に外部receiptをCREATE_NEWで `PREPARING / prepareCount=1` とし、途中失敗・再送・再入場でも再実行を拒否する。server本人の既存capabilityへ `addUnspentSkillPoints(603)` → `trySpendSkillPoints(603)` → 下記3nodeを `setSkillLevel(id,1)`、`setSkillDisabled` で初期設定。未使用0・使用済603（通常浄化3＋浄化の極意100＋真実の極意500）。credit/支出/前後NBTを記録する。
3. 準備するのは `foodhealing:purification` **ON**、`foodhealing:purification_mastery` **ON**、`foodhealing:truth_mastery` **OFF**。通常形式の `AcquiredSkills[{Id,Level}]`、`UnspentSkillPoints`、`SpentSkillPoints`、`DisabledSkills`だけが意図した差分。schema/食義/EatCount/BaseStats/Root/legacyを保存前後で比較し、それらや不正/pendingを修復しない。
4. 既存 `CapabilityEvents.syncToClient` → `ShokugiSyncPacket` と通常provider保存へ引き継ぐ。以降のtoggleは **FoodHealingScreenのON/OFFボタン → ToggleSkillPacket → server取得検証/RootController.setSkillDisabled → 通常sync** のみ。補助のtoggle代行なし。GUIの取得Lv・SP・各ON/OFFとclient/server canonicalを対応付ける。親だけOFF→ONを攻撃なしで1往復確認し、Mの保存ONとTの保存OFFを維持してAへ戻す。
5. 両極意の製品readiness/購入拒否・SP保護は無変更。このデータ準備は **取得済み状態の効果試験** であり、603SPを通常GUIで支払い購入できたという証拠ではない。prepare完了後、login/tick/inspect/hit/保存再読込から取得/SP/初期装備/traitを再注入する経路を作らない。

#### 安全区画・本物のsourceと装備の初期準備

- **生成前の通常GUI** でSurvival・Normal・cheats ON、`doMobSpawning=false`、`naturalRegeneration=false`を設定して画面記録後、使い捨てworldを生成する。入場後に通常gamerule照会とserver snapshotで反映を確認。平坦な足場、閉じた壁、十分な頭上空間と不透明な屋根の測定室・後述の退避室を新world内に通常建築commandで用意する。日光によるZombie燃焼、落下/窒息/水/炎、近隣Mobの接近を防ぐ。試験Mob生成前に室内・周囲を確認し、待機中にも侵入/無関係な被弾がないことを確認する。自然湧き停止だけでは完了にしない。
- 本人はHP20・最大HP20・absorption0・armor/toughness0・効果なし・防御/Root/Curios装備なし。invulnerable/creative/HP上限変更を使用しない。login直後の通常保護時間とhurt無敵時間は自然に終了するまで待つ。値を0へ代入しない。food/exhaustionも読み、長い移動・食事・空腹ダメージを避ける。
- `EntityType.ZOMBIE` の新しい1体を床上に生成し、NoAI・PersistenceRequiredで自動攻撃/移動/despawnを防ぐ（無敵/NoGravityは不要）。空のsource装備、非燃焼、targetなしを確認。既存fixtureと同じ公開 `MobTraitCap.reinit(mob,1,false)` を **新sourceの準備時だけ1回** 使用し、自然cap tickで初期化終了・lv0・traits空・攻撃属性3を確認する。native初期化による属性構成はこの準備内に限定し、以後reinit/HP修復をしない。想定外のlevel/traitは消去して合わせない。
- 正式登録 `LHTraits.POISON/SLOWNESS/CORROSION/EROSION.get()` をその順で各rank1、公開 `setTrait` でpendingへ追加し、自然tickのclearPending/initialize/postInitと元syncを待つ。native LinkedHashMapの順序も同順であること、server/clientに4 ID rank1があることを準備条件とする。手書きmap/NBTで代用せず、準備完了後の再付与・特性修復なし。TはOFFなので準備したsourceを先にstripしない。
- 本人の空mainhandへ **無enchant・非Unbreakableのdiamond_swordを1本、Damage=100 / MaxDamage=1561** としてprepare時だけ置く。他の手/防具/Curiosは空、既存inventoryを消さない。この剣では殴る/採掘/使用しない。受け手の手持ち剣は通常の無防具被弾では摩耗しないため、追加消耗を裸のHP damageと分離できる。初期残耐久1461、sourceは無手武器のまま。
- source UUID/HP/MAX_HEALTH/全属性値・modifier/NoAI/PersistenceRequired/装備、L2通常capのtraits以外の保存field、他capと準備時の補助所有sentinelをbaseline化する。Tで変化を許すのは4 traitと製品owned markerだけ。4種以外のtraitは追加しない。無関係なtick時計/位置等の自然な変化と、保存すべきlevel/data/drop flags等を分けて比較し、entity NBT全文のbyte一致を合格条件にしない。

#### 3攻撃と観測順（P=通常浄化、M=浄化の極意、T=真実の極意）

**native `Zombie.doHurtTarget(real ServerPlayer)` を各条件1回、合計3回だけ**。helperの明示hit commandは予約を受け付け、次のserver本人 **PlayerTick END / HIGHEST** でguard再確認後に1回呼ぶ。同じtickのNORMALで製品の通常浄化が動く前に、native attack return時の効果/耐久/HPを記録する。これは通常浄化の即時比較を可能にし、次の毒tickへ持ち越さないための検証用実行順制御である。製品handlerの優先度を変更せず、helperから効果remove/tick/HP setterは呼ばない。自然AIの攻撃間隔や毒の持続を実画面で測ったとは記録しない。

各hit前にreceiptへFIRINGを永続記録し、攻撃後DONEとする。途中失敗は同じcase再試行不可。native source/damageイベントと実行順の観測が揃わなければ次のhitへ進まない。待機/GUI中はNoAIで攻撃0。knockbackは通常どおりで、必要なら新world内の安全な立ち位置へ通常 `/tp` で戻し、距離と非接触を再確認する。HP等は戻さない。

| 段階・通常GUI操作 | native攻撃 / HP期待 | 効果・剣Damage・traitの期待と合格条件 |
|---|---|---|
| 準備・親設定非連動 | 攻撃なし、HP20 | 3node Lv1 / SP0・603。親のみOFF→ON中もM ON/T OFF不変。A前にP ON/M ON/T OFF、source4 ID rank1、剣100、markerなし |
| **A 本人保護**：P ON / M ON / T OFF | 1回、20→17 | poison/slownessの新規add各0、attack return時もなし。corrosion/erosion追加perform各0、剣100のまま。4 traitとsource対象外データは不変。Tによるstripで保護を偽装していない |
| **B 後からの浄化との対照**：GUIでMだけOFF、P ON/T OFF | 1回、17→14 | actual新規POISON duration200/amplifier0、SLOWNESS duration160/amplifier1を各1回、attack return時に存在。以後同tickの通常浄化で除去され、END後はなし。corrosion1回で100→110（floor100×0.1=10）、続くerosion1回で110→182（floor(1561−110)×0.05=72）。source4 traitは存続。剣の通常摩耗0、追加消耗82 |
| **T 適用**：MをON、次にTをON。3つONで範囲内待機 | 攻撃なし、HP14 / 剣182を保持 | 製品の通常範囲/tick処理だけでnative capの4 IDが消え、server marker `foodhealing:truth_l2` のVersion1・Mob=同UUID・Traits=4 ID trueが成立。元L2 sync後client capも空。既存消耗を治療しない |
| **C 恒久無効化の対照**：T OFF→M OFF→P OFF、3つOFFを確認 | 1回、14→11 | **受け手のP/Mによる保護・tick消去なし**で新規poison/slowness各0、追加wear0、剣182を保持。既に4 IDが除去された同sourceから通常数値攻撃3だけ成立。marker/対象外データ保持 |
| **OFF・範囲外・seal**：3つOFFのまま安全な退避室へ | 追加攻撃なし、HP11 / 剣182 | source中心からX方向90blockの安全な退避位置で±75外をserver座標で確認。sourceが読める間のtraits空/marker保持と、移動前後のcanonical・装備を記録しseal。prepare1/hit3/SEALED |

- 本表の期待値は **予定値**。既存config実測は `poisonTime=200, slowTime=160, corrosionDurability=0.1, erosionDurability=0.05, damageFactor=0.02, exponentialDamage=false`（§10.8最終runの `config/l2_configs/l2hostility-common.toml`）。次回新instanceで同既定値・lv0・rank1・slot1・native trait順を読取確認してから使う。違いはConfig/期待値の自動変更で合わせない。
- 攻撃ごとにLivingHurt/LivingDamage各1、非cancel、source `minecraft:mob_attack`、direct/attacker=束縛したsource、amount3。slot count1≥rank1のためCorrosion/Erosionの装備不足numeric倍率は1。HP/amount/MAX_HEALTH許容差1e-3、ID/SP/rank/回数/剣Damage/markerは完全一致、HUDはclient HPの小数1桁丸めと一致（±0.05相当）。Bの追加poison tick被弾は許容しない。原処理の順序が想定と違えば記録し停止する。
- GUI→server/client canonicalとHP/装備/capの収束は補助が観測する。上限は動作中server100tick/壁時計10秒（ポーズ中を除く）、最後の連続2snapshot一致を要求。非同期clientの一瞬の差だけでFAILにせず、期限超過や差の固定はUNVERIFIED/FAILを分けて停止。人間に秒数/tick計測を要求しない。
- 瞬間的な効果は **server側EffectUtil呼出し前後・MobEffectEvent.Added/Remove・attack return・通常浄化後** の読み取り記録で判別する。Aでaddがないことだけでなく、Bで同じ観測器が実add2件を記録する正の対照を必須とする。clientは実LocalPlayerの収束したeffects/HP/装備と実L2 capを別記録し、通常GUI/HUD・Inventoryの剣tooltip（F3+H）・L2が表示する対象情報をF2で対応付ける。
- Bのadd/removeが同tickに処理され、毒/鈍足iconがrender frameに現れない場合がある。**未撮影の一瞬のiconを目視PASSにしない**。瞬間付与はserver実測、clientは受信後の収束値と通常画面を判定範囲とする。nativeのtrait表示が画面に出ない場合もclient cap一致と目視表示を分け、補助のoverlayを製品HUDの代用品にしない。
- markerはserver側entity persistent dataで、L2のnative client payloadとは別。clientへ独自にmarkerを転送して「元同期済み」としない。空trait mapは実client側capで確認し、server-side codec mirrorを実client結果へ流用しない。

#### 必要な補助・ファイル設計（20:04時点の予定。旧補助は§10.10、新補助の限定修正/実行は§10.11）

`fixture/src/main/java/com/leva/foodhealing/verification/clientl2/` を基点とする別MOD `foodhealing_l2_client_verification`。製品への取り込みは禁止する。

| 予定ファイル | 役割・許される書込 |
|---|---|
| `L2ClientVerification.java` | run/world/UUID/hash guard、inspect、明示prepareの取得603SPとsource/剣初期準備1回、A/B/C各1回のnative attack予約、seal。ゲーム状態の初期準備以外は攻撃を起こすだけ。失敗後の再付与/修復/勝手な次case実行なし |
| `L2ClientServerObservation.java` | 実event・canonical/HP/effects/装備・Mob cap/marker・native config・周辺安全状態をread-only記録。server ENDの前後順も記録。異常時はjournalをFAILEDとして次の攻撃を拒否し、pause/通常終了へ通知。damage取消・setAmount・回復・remove/setTraitはしない |
| `L2ClientObservation.java` | Dist.CLIENTで実LocalPlayerと追跡中sourceのnative cap/装備/HP/effectsをread-only記録。取得は製品packet、mob capは元MobCapSyncToClient/TagCodec、HP/装備は通常同期に従う。server値をclient値として転記しない |
| `mixin/EffectUtilObservationMixin.java`、`mixin/DurabilityObservationMixin.java` | 対象world/person/caseに限定した **非cancellable HEAD/RETURN観測**。前者は実 `EffectUtil.addEffect`、後者は `DurabilityEater.corrosion/erosion` の実呼出し・slot・前後Damageを記録するだけ。引数/戻り値/保護判定を変更せず、製品Redirectと競合するRedirectは追加しない |
| `fixture/src/main/resources/META-INF/mods.toml`、`pack.mcmeta`、専用Mixin JSON | 補助だけの識別/依存/観測hook。ゲームclassをMixin準備pluginからloadしない。生産用Mixin設定を編集しない |
| `l2-client.init.gradle` | 既存ローカルGradle/既存cacheのoffline方式で補助専用source set・Jar/reobfのみ。既存main compile classpathと実製品Jar、承認L2 APIを参照し、main.output/taskへ依存しない。製品build.gradle・依存版は不変 |
| `run-allowlist.json`、`audit/prepare-receipt.json`、`audit/case-journal.json`、server/client JSONL・read-only照合記録 | 旧runから作り直した識別・永続once記録・生観測。receipt削除/上書きで再試行可能に戻さない。失敗/正常終了をともに保全 |

次回、補助だけのtask graph/dry-runを先に確認する。許可は専用compile/resources/Jar/reobfと既存cacheのmapping処理だけ。`compileJava/classes/jar/reobfJar/build/check/test/foodHealingUnitTest/runGameTestServer` や既存fixture taskを検出したら停止。MixinGradleの暗黙main依存も切り離し、補助hookのremap/非cancellable性と出力entryを検査する。wrapperのdownloadへ戻らない。補助Jarは今回instanceのmodsにのみ配置、外側MODは製品＋本体＋前提4MOD＋別補助の7本。旧Trial補助・95件fixture・外部内包Jarを追加しない。

#### 保存・再読込・正常終了と中断

1. C後の3つOFFを保ち、上表の範囲外観測後にseal。sourceが距離によりclient追跡/loaded entity一覧から消えた場合、その状態を「traits空」と判定しない。最後のloaded snapshot、通常保存、および次の通常再追跡を区別する。強制chunkロードを追加しない。
2. 通常Save & Quitでtitleへ戻り、保存終了log後に対象worldだけをreadonly保全・照合。`level.dat/Data.Player` とUUID.dat（存在するもの）を実際の読込優先/UUIDとともに確認。player canonical3node/SP0・603/3つOFF、Health11・MAX_HEALTH20の根拠、Inventoryの剣Damage182、sourceの通常保存entity/cap/UUID/markerと対象外データを確認。通常のentity region保存を読み、試験用NBTで置換しない。
3. **この新worldだけ1回通常再読込**。prepare/hit/source生成/trait再付与はしない。3つOFFのまま退避位置の本人状態とreceiptを確認後、安全な元室へ通常 `/tp` で戻り、同sourceのserver復元とnative client再追跡を観測する。近づいてもTはOFFなので、再読込時の復活をTの再適用で隠せない。4 IDなし・marker同UUID/4 ID保持、SP/取得/toggle/HP/剣/対象外data保持を要求する。
4. 再度通常Save & Quit→Quit Game。最終保存/log、今回Minecraft PIDの終了、製品/配置Jar同hashと製品source/test/build/購入停止不変を照合。prepare1/hit3/SEALED・失敗なしのreceiptと画面/logを3文書に反映する。同JVMでのworld再読込であり、別JVM永続試験・実2-clientとは別判定。
5. 無関係なdamage/heal・敵対Mob侵入・source燃焼/変質・HP/effect/耐久/traitの想定外・prepare/hit途中失敗があれば、**次の攻撃を止め、すぐ通常pause/退出**。補助で値を補正せず、失敗run/receiptを残して可能な範囲で正常終了する。通常の補助compile/準備不備は次回承認された補助内で修正可だが、測定途中のreset/同case再試行/製品修正/新仕様判断は別途扱う。

#### 操作分担・次の承認範囲と判定の限界

- Computer Useスキルの `@oai/sky` と `node_repl` の利用経路を確認。GUIの生成前設定・切替・command・F3+H/F2・保存終了は利用可能ならCodexが操作する。20:04の計画時点ではUI操作/起動を未実施だったが、旧中断runの実操作は§10.10、新runの完了は§10.11に記録。人間への依頼は再認証/OS権限が実際に必要になった場合、または実行できない入力だけ。必要なら一連操作・停止位置・報告文をまとめる。食事長押し/厳密tick/死亡試験/OBSは不要。
- **承認後に実行済みの範囲**：別補助作成・限定offline compile/reobf/guard確認→新規instance/world/生成前安全設定→本人prepare1回→通常GUIとnative attack3回→全OFF/範囲外/seal→通常保存/同world再読込1回/再追跡→正常終了/証拠照合/3文書更新。20:04当時は承認待ち、旧中断は§10.10、新run完了は§10.11。計画や完了済み試験を再作成する工程へ戻さない。
- 合否はA本人新規付与/追加消耗保護、B実作用の正の対照と通常浄化後消去、T正式除去/native client同期、C全OFFの実作用消失、OFF/範囲外/保存再読込の恒久性・対象外保全、GUI/HUD/装備表示、終了を個別に **PASS / FAIL / UNVERIFIED** とする。未撮影/未観測をPASSへ補完しない。
- この3攻撃は **実client本人＋検証専用明示トリガーによるnative L2攻撃**。自然spawn抽選/自然AI攻撃、poison持続/DOTやslowness移動速度、全toggle組合せ、別player/反射、全装備/不足時倍率/Unbreaking、境界全軸/明示再付与、性能、別JVM/TCP/実2-client、他trait/他MOD/L2＋Trial同時構成は今回は再測定しない。既存95件の限定自動PASSと別の判定とする。
- 両極意IMPLEMENTATION_PENDING・購入停止/SP保護、RC=NO、他機能の個別開始条件を維持。食料生産の極意による可逆クラフト経由の増加は既知かつ許容仕様・バグ修正対象外。死亡drop/replay/desync等の意図しない重複へ許容を広げない。


<a id="l2-client-stopped-result"></a>

### 10.10 L2限定実clientの実行・A後の補助判定FAIL（2026-09-15 22:57 JST・過去履歴）

後続の別runの結果は[§10.11](#l2-client-completed-result)。以下のA総合UNVERIFIED・当時のNOT RUNはそのまま保持する。

**PARTIAL / STOPPED・製品不具合の再現とは判定しない**。今回利用者が§10.9の補助作成・offline build・新instance/world・3攻撃と保存再読込を承認。測定途中失敗時の停止条件に従い、Aのfinish失敗でB以降を止めた。停止world/receipt/補助は保全し、再付与・再試行・HP/効果/耐久/trait修復・製品変更なし。未完了をPASSにしない。既存95/60/49/86の自動PASSと旧Trial実clientは維持する。

#### 対象・補助・環境

- run/root: [`build/verification/l2-client-20260915-220916/`](../build/verification/l2-client-20260915-220916/)。新instance `FHR_L2_Client_20260915-220916`、新world `FHR_L2_20260915_220916`。本人UUID `<PLAYER_UUID>` は今回のゲーム内inspectから限定し、認証ファイルは読取/コピーしていない。旧world・receiptを流用しない。
- 製品 **229,423 bytes /150 entries / SHA256 3807DECCBA79A122CD3A6431454788944ACDC72FE62A7A9BDB37C189D1B76DE6** は開始/終了と新mods配置で不変。[final-integrity](../build/verification/l2-client-20260915-220916/audit/final-integrity.json)の既存source/test/build等に差分・追加sourceなし、`.git`なし。両極意readiness/購入停止のguardを維持し、製品Config/依存版を編集していない。
- [実ロード](../build/verification/l2-client-20260915-220916/audit/runtime.jsonl): MC1.20.1 / Forge47.4.0 / Java17.0.15、Hostility2.5.19 / Library2.5.3 / Complements2.6.1 / Curios5.12.0+1.20.1 / Patchouli1.20.1-84-FORGE。内包採用はTracker **0.4.4**、mob_weapon_api0.2.13、l2screentracker0.1.4、l2tabs0.3.3、l2itemselector0.1.9、mixinextras0.2.0-beta.8。外側7Jarのみ。Trial/TaCZ/旧補助/95fixture/内包Jarの重複配置なし。今回の起動に必要な不足・追加認証・新downloadなし。
- 新別MOD `foodhealing_l2_client_verification:1.0`、**31,003 bytes /16 entries / SHA256 1F5D01FA564D67689C74E20F3F55E9DA13DEF910BE563574BC627EFA6B19146B**。[専用init](../build/verification/l2-client-20260915-220916/l2-client.init.gradle)と[graph](../build/verification/l2-client-20260915-220916/audit/task-graph.txt)、[offline限定build](../build/verification/l2-client-20260915-220916/audit/helper-build-01.log)は成功。専用compile/resources/classes/Jar/reobfとcached mappingのみ。製品build・既存suiteは実行していない。[配置/entry検査](../build/verification/l2-client-20260915-220916/audit/prelaunch.json)。
- 補助は本人/run/world/hashと正常freshをguard、prepare先行CREATE_NEW、各case FIRINGのCREATE_NEWで再実行禁止。source正式reinit/setTraitは初期準備だけ。Observerは非cancellable HEAD/RETURNとイベント読取のみ。今回native攻撃1回、DONE/SEALEDなし。補助作成時の通常準備scriptのパスキー整合修正はゲーム起動前であり、攻撃後は補助も修正/rebuildしていない。
- 生成前GUIでSurvival/Normalと `doMobSpawning=false` / `naturalRegeneration=false` を設定。[生成前F2（両OFF）](../build/verification/direct-jar-20260913-114729/launcher/instances/FHR_L2_Client_20260915-220916/.minecraft/screenshots/2026-09-15_22.28.39.png)。入場後の通常照会と最終保存でも両falseを確認。測定室中心X0と退避室X90、各x/z±8・y79–84の石hollow区画、閉じた床/壁/不透明屋根・内部照明。834個のshell/周囲敵Mob/床/非燃焼/非窒息等をprepare前guardで確認。待機中に外部damage/heal、侵入、source燃焼の記録なし。NoAI/PersistenceRequiredの新Zombie1体、HP20/attack3/level0・4rank1。無敵化/ピースフル/HP補正なし。

#### 準備・実測・判定

[prepare receipt](../build/verification/l2-client-20260915-220916/audit/prepare-receipt.json)と[prepare complete](../build/verification/l2-client-20260915-220916/audit/prepare-complete.json)：本人3node各Lv1、credit603/支出603・未使用0、P/M ON・T OFF、sword Damage100/Max1561・無enchant/非Unbreakable。通常GUI購入の成功ではない。GUIでPのみOFF→ONを1往復し、server/client canonicalでM ON/T OFFの非連動を確認。攻撃前の2連続同期記録は[convergence](../build/verification/l2-client-20260915-220916/audit/convergence.jsonl)。

| 項目 | 実測・期待値との関係 | 判定 |
|---|---|---|
| A P/M ON・T OFF | native Zombie.doHurtTarget 1回。server return HP20→17、max20、効果なし、sword100。hurt/damage各1、mob_attack・amount3・同source帰属、取消なし。EffectUtil/Added/corrosion/erosion各0。source4trait rank1保持・Truth markerなし。期待値と一致 | 観測値は一致。**A総合UNVERIFIED（補助finish FAIL）** |
| B M OFFの正対照 | 攻撃・GUI切替未実施。17→14/毒鈍足即時付与→通常浄化後消去/100→110→182は今回未観測 | **NOT RUN / UNVERIFIED** |
| T適用 | M/TのON切替、製品remove/marker/client空capの観測未実施 | **NOT RUN / UNVERIFIED** |
| C全OFF | 全OFF切替・攻撃未実施。HP14→11/剣182/特性作用消失は今回未観測 | **NOT RUN / UNVERIFIED** |
| 恒久性・範囲外・seal/再読込 | 未実施。sourceが消えたことを空traitsと判定していない。停止後は新規再入場なし | **NOT RUN / UNVERIFIED** |
| GUI/HP HUD/装備/同期 | P OFF→ONとSP0/603・取得表示、M/T canonical非連動を確認。server native returnと独立したactual-client値にHP17/max20/sword100/4traitの一致、HUD17.0/20.0と剣の表示。停止後の正式収束receiptは未作成 | **部分確認**。M/Tのボタン切替・正確な耐久tooltip・Bの瞬間iconは未観測 |
| 中断保存・終了 | 通常Save & Quit→Quit Game、保存値・log・PID終了確認 | **PASS（中断runの正常保存/終了のみ）** |

生証拠と切り分けは[stopped-run-analysis.json](../build/verification/l2-client-20260915-220916/audit/stopped-run-analysis.json)、元 `A-FIRING.json` / `attack-return.jsonl` / `native-events.jsonl` / `client-snapshots.jsonl` に保存。Aの観測値をB/T/Cや全L2対応のPASSへ拡張しない。native攻撃は補助の明示トリガーであり、自然AI戦闘ではない。

#### 停止原因と次回に必要な限定修正

22:45:07、補助 `L2ClientServerObservation.finish` が `count("removed")==0` を要求して停止。攻撃tickには**対象外の `MobEffectEvent.Remove(foodhealing:guts)` が1件**あった。通常浄化の毒/鈍足除去とは別である。現行 `RootController.updateVisualEffect` は非発動時にgutsのremoveEffectを要求し、使用した[Forge47.4.0実bytecode](../build/verification/l2-client-20260915-220916/audit/LivingEntity-47.4.0-javap.txt)の `LivingEntity.m_21195_` は存在/除去結果の確認前にRemoveイベントをpostする。通知だけでは効果の存在・実除去を意味しない。補助が全IDの通知を通常浄化の除去数としたことが原因で、Rootや浄化の製品不具合を示す証拠ではない。

次の新runでは補助observerだけで、**毒/鈍足の対象ID・case/tick・除去要求前の実instance有無・前後の効果一覧を区別して集計**する。対象外通知は生logへ残し、実除去成功へ数えない。数値3、HP遷移、4trait、耐久100→110→182、native呼出し回数、失敗時停止は緩めない。停止runの補助/Jar/receipt/worldは変更・再開せず、補助修正と新run試験の承認後に§10.9を引き継ぐ。今回追加仕様判断・ファイル提供は不要。

#### 保存・終了・未観測

- 22:45:17通常pause、22:45:41 Save & Quit、22:45:42.170 **All dimensions are saved**、22:46:11.327 **Stopping!**。[原logの保全](../build/verification/l2-client-20260915-220916/audit/latest.log)、[PID17792終了](../build/verification/l2-client-20260915-220916/audit/process-exit.json)。強制kill/復活/保存値書換えなし。
- [保存read-only照合](../build/verification/l2-client-20260915-220916/audit/stopped-save-values.json)：`level.dat/Data.Player` と本人UUID.datのcanonical一致、両方Health17/DeathTime0、SP0/603、P/M ON・T OFF、剣100、効果なし、MAX_HEALTH基底20。対象entity regionの同sourceUUID `1736de77-d878-4e55-9eea-3020dc3f3d3c` はHealth20/NoAI/PersistenceRequired、4trait・元cap/markerなしを保持。通常保存確認だけで、再読込やTruth恒久性の成功とはしない。
- COMPUTER USE：生成前設定・通常GUI/command/F2/退出。AUTOMATED：once準備・native攻撃呼出し・独立server/client観測・停止判定・保存/Jar解析。HUMAN：追加操作なし。最終判定は期待値の転記ではなく生証拠との照合による。
- 起動logのoptional GeckoLib class警告、既存試験食model等のWARNは保全し、全警告解消とはしない。今回の停止は上記observer assertion。製品修正/既存suite再実行/購入解放なし。L2＋Trial同時構成・侵略者実client・他trait・自然戦闘/全装備/95ケース全再現/別JVM・実2-client等は今回対象外。両極意購入停止・SP保護、RC=NO、既存gateと可逆クラフト既知許容仕様を維持。

<a id="l2-client-completed-result"></a>

### 10.11 L2新run・観測補助修正後の限定実client完了（2026-09-16 19:45 JST）

**REAL CLIENT LIMITED PASS**。旧run `20260915-220916` を再開せず、新run **`20260916-185745`** で§10.9の承認範囲を実行した。旧A総合UNVERIFIED/補助FAILを昇格させない。製品不具合の再現・製品修正・購入解放はなし。[利用者依頼](../build/verification/l2-client-20260916-185745/audit/user-request.txt)、[実測照合](../build/verification/l2-client-20260916-185745/audit/completed-run-analysis.json)。

#### 対象・新補助・不変境界

- Food Healing **229,423 bytes /150 entries /SHA256 `3807DECCBA79A122CD3A6431454788944ACDC72FE62A7A9BDB37C189D1B76DE6`**。新instance配置も同hash、配布物の再生成なし。MC1.20.1/Forge47.4.0/Java17.0.15、L2 Hostility2.5.19/Library2.5.3/Complements2.6.1/Curios5.12.0+1.20.1/Patchouli1.20.1-84-FORGE。[実ロード](../build/verification/l2-client-20260916-185745/audit/runtime.jsonl)はTracker **0.4.4**、mob_weapon_api0.2.13、l2screentracker0.1.4、l2tabs0.3.3、l2itemselector0.1.9、mixinextras0.2.0-beta.8。外側mods7本だけ、Trial/TaCZ/旧補助/95fixture/内包重複なし。
- 新verification root `build/verification/l2-client-20260916-185745`、instance `build/verification/direct-jar-20260913-114729/launcher/instances/FHR_L2_Client_20260916-185745`、新world `FHR_L2_20260916_185745`。旧world/receipt/journalを流用せず、新allowlistを実path・run・Jar・本人UUIDへ拘束。
- 新補助 `artifacts/foodhealing-l2-client-verification.jar`：**33,309 bytes /17 entries /SHA256 `BAF84079C835880E7F6EA6464611B35F6695306367BF63B900D4030EBB24D106`**。別MOD `foodhealing_l2_client_verification` 1.0。旧31,003 bytes/16 entriesの補助は変更なし。
- [旧補助との差分](../build/verification/l2-client-20260916-185745/audit/helper-source.diff)は4ファイル：driverのrun ID、`L2ClientServerObservation.java`の対象ID/entity/case/tick・実remove前後集計、新規`RemoveEffectObservationMixin.java`、そのJSON登録。Mixinは実Forge `removeEffect`のHEAD/RETURN読取のみ、取消/return値変更/ゲームstate書込なし。prepare/native攻撃/数値/耐久/trait/toggle/sync/失敗時停止条件は緩和していない。
- 事前[task graph](../build/verification/l2-client-20260916-185745/audit/task-graph.txt)を確認して既存ローカルGradleでoffline限定compile/resources/Jar/reobf成功（11秒）。Mixin補助taskはSKIPPED、downloadMcpConfigはoffline cache処理、extractSrg/createMcpToSrgはUP-TO-DATE。[build log](../build/verification/l2-client-20260916-185745/audit/helper-build-01.log)。製品compileJava/build/check/unit/GameTestや95fixtureを実行せず、Invader86/Cube49/L295/core60/build/unit/check既存PASSは過去のまま保持。

#### 安全準備・一度だけの取得

- 生成前GUIでSurvival/Normal・自然湧きfalse/自然回復false、structures OFF。入場後の通常commandと最終保存でも2gameruleを確認。[生成前画面](../build/verification/direct-jar-20260913-114729/launcher/instances/FHR_L2_Client_20260916-185745/.minecraft/screenshots/2026-09-16_19.03.53.png)。閉じた石の床/壁/不透明屋根の測定室とX90退避室を新worldに構築。床y79/空間y80–83/屋根y84、上部照明、834 shell blockと周囲敵対Mob・足元/水火/窒息/侵入をguard。自然湧きOFFだけで判定しない。
- 本人UUID `<PLAYER_UUID>`、正常fresh・HP/MAX20、armor/absorption0、効果/Root/他skillなしを読取確認。prepare **1回だけ**：`foodhealing:purification` / `foodhealing:purification_mastery` / `foodhealing:truth_mastery` 各Lv1、検証credit603/支出603/未使用0、P/M ON・T OFF。[receipt](../build/verification/l2-client-20260916-185745/audit/prepare-receipt.json)。通常GUI購入成功ではない。HP等の修復なし。
- 新NoAI/Persistent Zombie UUID **`dd2d5d03-c159-4f71-9235-8adc86f77d1e`**、level0/HP20/attack3、[2.5,80,0.5]。正式API/通常postInit/syncでpoison/slowness/corrosion/erosion各rank1を順序どおり準備。無enchant/non-Unbreakable diamond_swordは初期Damage100/Max1561。通常GUI P OFF→ONでMの保存ON・Tの保存OFFが非連動であることを独立server/client canonicalで確認。待機中の自動攻撃・無関係なdamage/healなし。

#### 実測・判定（すべて新runの証拠）

| 単位 | 実測・証拠 | 判定 |
|---|---|---|
| A P/M ON・T OFF | 19:12:39 JST /tick9659。native1回、HP20→17、毒/鈍足のEffectUtil/Added/対象remove各0。wear0・剣100。source4trait保持、markerなし。[A events](../build/verification/l2-client-20260916-185745/audit/A-events.json) / [DONE](../build/verification/l2-client-20260916-185745/audit/A-DONE.json) | LIMITED PASS |
| B P ON・M/T OFF | 19:16:24 /tick11273。native1回、HP17→14。毒200tick amp0/鈍足160tick amp1を各1回付与、attack return時に両方存在。その後同tickの通常浄化で実除去。corrosion100→110、erosion110→182。source4trait保持。[B events](../build/verification/l2-client-20260916-185745/audit/B-events.json) / [DONE](../build/verification/l2-client-20260916-185745/audit/B-DONE.json) | LIMITED PASS・observer正の対照成立 |
| T GUI M ON→T ON | 19:19:41 /tick12454。製品の通常範囲処理・正式remove・元L2 syncで同sourceの4traitなし。server所有markerと**実client**空trait capを別観測。HP14/剣182を治療しない。[truth](../build/verification/l2-client-20260916-185745/audit/truth-applied.json) / [独立同期収束](../build/verification/l2-client-20260916-185745/audit/convergence.jsonl) | LIMITED PASS |
| C GUI T OFF→M OFF→P OFF | 19:26:17 /tick17994。全OFFの本人へnative1回、HP14→11、毒/鈍足の新規付与/対象remove各0、wear0/剣182。source空traits/marker保持。[C events](../build/verification/l2-client-20260916-185745/audit/C-events.json) / [DONE](../build/verification/l2-client-20260916-185745/audit/C-DONE.json) | LIMITED PASS |
| 全OFF/範囲外/seal | 19:27:49。本人[92.5,80,0.5]・source[2.5,80,0.5]、X差90で±75外。prepare1/hit3/SEALED。[seal](../build/verification/l2-client-20260916-185745/audit/seal.json)。90block時点でsourceはclientにも残っており、unload済みとは主張しない | LIMITED PASS |

- A/B/Cは各1回、native `Zombie.doHurtTarget`合計3回。全ケースのLivingHurt/LivingDamageは各1、`minecraft:mob_attack`・amount3・取消false、attacker/directは同source。防具なしの通常被弾による手持ち剣消耗0と、Bのtrait由来追加10/72を分離。C前のnative knockback後は通常`/tp`で安全な測定位置へ戻しただけで、HP/効果/耐久/traitは変更しない。
- Bの各対象にEffectUtil HEAD/Added/RETURN各1、remove HEADの実instanceあり→Remove通知時もあり/取消false→RETURN true/実instanceなし→通常浄化後effects空を記録。[attack return](../build/verification/l2-client-20260916-185745/audit/attack-return.jsonl)、[全native raw](../build/verification/l2-client-20260916-185745/audit/native-events.jsonl)。対象外`foodhealing:guts`は実instanceなし・return falseの要求としてrawに残し、対象件数へ加算しない。Remove通知だけを実除去と判定しない。
- server/clientのcanonical・取得/SP/toggle・HP/MAX・剣・対象trait mapを独立観測し2連続収束を確認。**server markerや非同期fieldをclient同一値と主張しない**（clientのpersistent markerは空で正常、dropRate/PersistenceRequired等もnative client payload範囲外）。対象外cap/attributes保全はserver初期値・保存・再読込間で照合。
- HUDはA17.0/20.0、B/T14.0/20.0、C/再読込11.0/20.0。advanced tooltipはA残1461/1561、B/C/再読込残1379/1561。[A](../build/verification/direct-jar-20260913-114729/launcher/instances/FHR_L2_Client_20260916-185745/.minecraft/screenshots/2026-09-16_19.12.56.png) / [B剣](../build/verification/direct-jar-20260913-114729/launcher/instances/FHR_L2_Client_20260916-185745/.minecraft/screenshots/2026-09-16_19.17.13.png) / [T](../build/verification/direct-jar-20260913-114729/launcher/instances/FHR_L2_Client_20260916-185745/.minecraft/screenshots/2026-09-16_19.20.11.png) / [C](../build/verification/direct-jar-20260913-114729/launcher/instances/FHR_L2_Client_20260916-185745/.minecraft/screenshots/2026-09-16_19.26.27.png)。L2の4表示はA/Bに存在、T後/再追跡で消失。Bの一瞬の毒/鈍足iconは目視撮影しておらず、server即時付与・実client収束後空という観測に限定する。

#### 保存・通常再読込1回・正常終了

- 1回目Save & Quit：**19:28:06.404**にAll dimensions are saved。保存と競合しない時点でlevel.datと本人playerdata、source entity regionをread-only解析/新auditへ保全。[first-save](../build/verification/l2-client-20260916-185745/audit/first-save-values.json)。HP11/DeathTime0、SP0/603、3node/3つOFF、剣182、同Zombie HP20/NoAI/Persistent・空traits/markerを確認。
- 同じ新worldだけを**1回通常再読込**。prepare/hit/source再生成/trait再付与なし。全OFFのまま通常`/tp`で測定室へ戻り、同UUIDを再追跡。canonical/HP11/剣182・実client空trait cap、server marker/対象外stableCap/attributes保持を[再読込snapshot](../build/verification/l2-client-20260916-185745/audit/reloaded-retracked.json)で確認。TはOFFなので再除去で復活を隠していない。[再追跡HUD](../build/verification/direct-jar-20260913-114729/launcher/instances/FHR_L2_Client_20260916-185745/.minecraft/screenshots/2026-09-16_19.30.33.png) / [耐久](../build/verification/direct-jar-20260913-114729/launcher/instances/FHR_L2_Client_20260916-185745/.minecraft/screenshots/2026-09-16_19.30.55.png)。
- 2回目Save & Quit：**19:31:21.263**に全dimension保存。Quit Game **19:31:41.049**、PID484終了を19:31:54に確認。[process](../build/verification/l2-client-20260916-185745/audit/process-exit.json)。[final-save](../build/verification/l2-client-20260916-185745/audit/final-save-values.json)の取得/SP/toggle・Health/剣・source ForgeCaps/ForgeDataとreceipt hashは最初の保存と一致、prepare1/hit3/SEALEDを保持。ゲーム内測定failure/command errorなし。

#### 不変確認・例外・残り

- [製品/source/test/build.gradle等144件と配置7Jarは不変](../build/verification/l2-client-20260916-185745/audit/final-integrity-followup.json)。製品購入gate/SP保護・製品Config・配布Jar変更なし、補助は別Jarだけ。`.git`なしのためhash/実ファイル差分で確認し、git diff実施とはしない。
- **旧run/instanceの基準115件中114件は不変**。旧world/補助Jar/receipt/journal/screenshots/raw log/保存/証拠に差分なし。ただし旧instance `mmc-pack.json`は19:01:15に更新され、SHAが`8C408637F203A2417A49793A448C405E76A68278FD3B05885B8DE5E84FA9CDD8`→`313DCBAB3CA09A22498DD5AE92F0E9E5BA4E670A3ECCA57BD531704F112EEBC3`。旧hashと一致する新instanceのmetadata実物を比較すると、LWJGLの`cachedVolatile:true`削除だけで、version等の他JSON値は同一。[例外証拠](../build/verification/l2-client-20260916-185745/audit/old-instance-metadata-exception.json)。Prismによるcache更新は推測、書換主体は未観測。復元/旧world起動はしない。**旧instance全体不変とは報告しない**。
- 事後照合script初回はこの旧metadata差分でassert停止（[初回integrity](../build/verification/l2-client-20260916-185745/audit/final-integrity.json)）。ゲーム内guard失敗とは別。差分を明示して製品・旧ゲーム証拠・新測定を独立判定した。ゲーム期待値/guard/receiptを緩めたり再実行したりしていない。[WARN361行](../build/verification/l2-client-20260916-185745/audit/warnings.log)にはoptional GeckoLib class/既存asset/refmap/config既定補完等を保持、全WARN解消を宣言しない。
- 実施主体：**COMPUTER USE**=生成前設定/通常GUI/command/F2/SaveQuit/再読込/QuitGame、**AUTOMATED**=once準備/native明示攻撃/独立readonly観測/保存照合、**HUMAN**=入力依頼なし。認証情報読取/コピー・新downloadなし。
- 未観測：自然AI戦闘/毒持続DOT/歩行速度、一瞬の毒鈍足icon、全装備/全toggle/全軸境界/明示再付与・全95再現、別player/負荷/別JVM/TCP/実2-client、他trait/侵略者実client/L2＋Trial同時構成/別版。次候補は侵略者限定実clientの準備具体化で別途承認待ち。L2限定単位を完了し、他機能へ自動で進まない。
- 両極意全体IMPLEMENTATION_PENDING/購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、他個別gateを維持。食料生産の極意による可逆クラフト増加は既知かつ許容、死亡drop/replay/desync等の意図しない重複は許容しない。

<a id="trial-invader-result"></a>

## 11. Trial Monolith 1.4.9 — 侵略者の限定追加と最終自動検証

更新: **2026-09-15 21:11 JST / LIMITED IMPLEMENTED・AUTOMATED INTEGRATION TESTED / 実client NOT RUN**。
最新[利用者依頼](../build/verification/trial-invader-20260915-203400/audit/user-request.txt)が、旧フェーズの別Trial攻撃保留をこの侵略者対応だけ更新した。仕様正本は[Skill Tree §9](SKILL_TREE_SPEC.md#purification-mastery-activation)と[Compatibility §7の攻撃限定例外](COMPATIBILITY_POLICY.md#trial-invader-149)。Q1/Q2、100SP、本人条件、Root・購入停止は変更しない。

### 11.1 実物・呼出し・所有者を確認した範囲

承認Trialは§4と同じ1.4.9、263,159 bytes /240 entries / SHA256 `5EFE4C068F24E611C215A0A20FE9698D5C5F7EFC1476C8E5CEAEA78B8CE1A0AD`。原物とlibsコピーの[再測定](../build/verification/trial-invader-20260915-203400/audit/inputs.json)、[終了時の不変確認](../build/verification/trial-invader-20260915-203400/audit/final-jar.json)を実施。以下のclassは `io.github.kosianodangoo.trialmonolith.common.entity` 配下（Invaderは `invadermonolith` 配下）。

| 実処理 | 実物から確認した呼出し・意味 | 製品介入 |
|---|---|---|
| InvaderMonolithEntity.tick → lambda$tick$2(Entity) | native範囲/DEFAULT_PREDICATEで対象列挙、hasDimensionalCore=falseならsetSoulProtected(Entity,boolean)をfalseで1回。対象のtickCount>=100もnative条件 | 同call-siteのみ、正常server本人が両取得/ONならfalse要求を通さない。既存falseをtrueにしない |
| SmallBeamEntity.lambda$activate$0(Level,Entity) | native rayTrace128・幅1、ordinary addSoulDamage(Entity,float) / highDimensionalかつ外部免疫時のaddSoulDamageForce(Entity,float)各1。後続hurtはmax(1.5,maxHealth×.05) | 元ownerが同levelの正式登録Invader、本人条件成立時だけ新規Soul加算を抑止。通常hurt・motion保持 |
| HugeBeamEntity.lambda$activate$0(Level,Entity) | native rayTrace128・幅8、同じSoul2呼出し、laserAttack(level,getOwner())でEntity.hurt(DamageSource,float)へFloat.MAX_VALUEを渡す1呼出し | Soul2箇所とnumeric1箇所を別hookにする。numericはこのowner/本人/call-siteのみ例外。共通type/amountフィルタなし |
| owner生成・保存解決 | OP Small/Huge goalはsetOwner(Invader)と攻撃entityへのsetSoulProtected(true)。AbstractDelayedTraceableEntityはOwner UUID保存、load後はServerLevel.getEntity(UUID)からLivingEntityへ解決 | 攻撃側のtrue設定は保持。null/未解決・player/Cow/別Trial Monolith ownerへ広げない。生成goalとUUID load後の実activateも試験 |

[静的照合集計](../build/verification/trial-invader-20260915-203400/audit/static-review.json)、同directoryのInvader/Beam/goal/EntityHelper/EntityMethodsのjavapを参照。`require/expect/allow=1`で各call-siteを拘束し、[Mixin exportとrefmap/版判定検査](../build/verification/trial-invader-20260915-203400/audit/mixin-validation.json)を実攻撃結果と併用。Hugeのvanilla hurtだけrefmapで`m_6469_`へ変換する。exportはMixin段階の出力であり、後段transformerをすべて捕捉したという意味ではない。

**一般MobEffectの敵対解除は未特定**。Invader、OP大小/ランダム/周囲beam・Cube生成goal、両Beam、基底、EntityHelperの読取では、実際に特定できた解除はSoul Protection falseだけ。一般解除の不存在や全buff耐性を宣言せず、架空hook・常時再付与なし。通常Luckの維持、自然期限、通常remove、対象外の正当なflag解除を対照確認した。参考動画は取得失敗（Cache miss）で未視聴、版/内容を証拠にしていない。

### 11.2 実装と不変境界

- 新規productionは `compat/TrialInvaderCompatibility.java` と `mixin/trialmonolith/TrialInvaderProtectionMixin.java` / `TrialInvaderBeamSoulMixin.java` / `TrialInvaderHugeBeamMixin.java` の4 class、既存Trial Mixin JSONへの3登録だけ。既存1.4.9版判定pluginと本人eligibilityを再利用。版判定はgameplay classへ早期linkせず、external classは対応Mixin経由だけ。
- 共通DamageEventHandler・Root・P/Truth条件・購入/SP・packet/schema/migration・Cube/L2/SW/TaCZ既存classは変更なし。[134既存sourceのhash不変と差分](../build/verification/trial-invader-20260915-203400/audit/source-boundary.json)。`.git`がないためgit diff確認とは記録しない。
- 新規 `src/trialInvaderTest` と専用sourceSet/Jar/reobf taskを追加し、checkでcompileを確認。fixtureは本人準備時だけcanonical/HP/Soul/flag/configを設定し、測定中の修復なし。製品Jarへ混入しない。native Small/Huge goal呼出しのtimer/照準準備はfixtureに限定、自然boss戦・描画・client操作の確認ではない。
- 部分蓄積、親Root OFF、死亡等のTrue Root未決定境界、Flight等の開始条件、SP100/500と両極意購入停止を維持。保存schema変更・データ移行なし。

### 11.3 結果と証拠（すべてAUTOMATED）

| 実行単位 | 実件数・合格内容 | 証拠 |
|---|---|---|
| 新規Invader fixture | **86/86**。native tick18、大小/ordinary-force/9状態36、Soul閾値対照16、別owner8、実goal生成2、owner UUID保存解決1、同ray二者1、非Player1、独立高damage負対照1、buff/通常解除1、購入拒否/同期/通常保存再読込1 | [最終receipt](../build/verification/trial-invader-20260915-203400/final-invader/invader-result.json)・[log](../build/verification/trial-invader-20260915-203400/audit/final-invader.log) |
| 既存Cube回帰 | **49/49**、元のケース/sourceを変更せず最終配布Jar＋実Trialで実行 | [receipt](../build/verification/trial-invader-20260915-203400/final-trial/trial-result.json) |
| 既存L2回帰 | **95/95**、別構成の最終配布Jar＋実L2/依存、Tracker0.4.4実採用、Trial不在 | [receipt](../build/verification/trial-invader-20260915-203400/final-l2/l2-result.json) |
| vanilla/TaCZ core | 各 **60/60**、新規専用world。既存60ケースを削除・変更せず、追加86とは別集計。Trial不在のcore/購入/Root/toggleも維持 | [vanilla log](../build/verification/trial-invader-20260915-203400/audit/final-vanilla.log)・[TaCZ log](../build/verification/trial-invader-20260915-203400/audit/final-tacz.log) |
| build/unit/check | **PASS**。P150、Truth53、前提142、SW47、data境界5000、Ammo各Lv/10000標本等。専用fixtureもcompile/reobf | [最終build](../build/verification/trial-invader-20260915-203400/audit/final-build.log) |

86件には実attack以外のtick/保存/負対照も含むため、86回すべてをbeam実攻撃と呼ばない。EmbeddedChannelは実ServerPlayerと登録packet/syncの自動照合であり、実client/HUD/TCP・認証二者の代替ではない。共通damage/packetに変更がないためSW17、TaCZ別13/Ammo実MOD、別JVM suiteは今回再実行なし。59/60/95/49の過去記録は当時のまま残す。

代表実測（MAX_HEALTH=100、既存Soul=.2、Root/Truth未取得）:

- Huge・両ON・外部免疫false: Soul `.2→.2`、実効HP `80→80`、numeric/death `0/0`。極意OFFはSoul `.2→.3`、Float.MAX_VALUE numeric1、laser_attack deathでHP0。外部免疫やRootだけによる成功ではない。
- Small・両ON・ordinary: Soul `.2→.2`、HP `80→75`、Hurt/最終LivingDamage `5×1`。force時は外部Soul Protectionにより実効HP `80→80`でも、同じ最終numeric `5×1` が通る。Health保存値・実効HP・MAX_HEALTHを同一視しない。
- Soul追加1.1/10.1の大小/ordinary-force: 保護時は旧.2を保持し強制death/respawnなし。極意OFF対照は新規Soul1.3/10.3と元soul_damage death、10側のみplayer置換。独立numericを一般的に無効化しない。
- 非侵略者owner・同DamageTypeの別直接hurt・他player/非Playerは元numeric/死亡を保持。侵略者が生成したbeam自身のSoul Protection=trueは維持。

### 11.4 途中失敗・修正・終了

1. ローカルGradleの通常sandboxではnative DLL/cacheアクセス不可。承認済みoffline実行権限で解消。最初のfixture compileはPurchaseSkillPacketの引数不足でFAIL、実signatureに合わせ修正。
2. focused-01/02/03は最初の対象列挙前に **0件/FAIL**。native predicateのtickCount>=100を満たしておらず、player.tickだけではserverが持つ経過tickを進められなかった。`ServerLevel.tickNonPassenger`で110回の通常server処理を準備時に行い、年齢/predicate/対象列挙assertを維持。focused-04 **70/70**、その後閾値16件追加の最終 **86/86**。3つの失敗world/receipt/logを削除せず通常終了。HP/Soul/guardを弱めて成功させていない。
3. force側の外部免疫は実効HPへ介入するため、Smallの数値維持をLivingHurtだけでなく最終LivingDamage5×1でも確認。fixtureの観測方法の修正であり、製品HP修復や小beam数値取消を加えていない。製品実装に起因する統合FAILは今回確認されなかった。
4. 自動承認レビューが旧「計画のみ」制限を理由に一度拒否。最新添付の実装/試験承認を明示して同じ限定操作が承認された。[拒否原記録](../build/verification/trial-invader-20260915-203400/audit/approval-review-rejection.json)を保持し、現在の阻害要因にはしない。
5. 最終Invader PID32156・Cube36748・L2 5616はexit0、saveEverything→halt→通常Saving players/worlds→全dimension保存を確認。GameTestも通常保存/停止完了。[全run集計](../build/verification/trial-invader-20260915-203400/audit/test-summary.json)。今回と無関係な既存Java PID29012（開始18:37）は操作していない。

最終Invader logには外部Invader/攻撃entityの保存時UUID重複WARN等が残る。構成別config既定補完・metadata/SynchedEntityData・dev refmap等も全WARN解消としない。限定攻撃/本人保存試験は成立したが、侵略者entity群の長期保存・自然boss進行・loot・全挙動の保証へ広げない。Security scanのPASSで代替したものではない（今回Codex Security scan未実施）。

### 11.5 最終成果物と次の引継ぎ

[配布Jar](../build/libs/foodhealing-3.0.0.jar): **229,423 bytes /150 entries**、SHA-256 **3807DECCBA79A122CD3A6431454788944ACDC72FE62A7A9BDB37C189D1B76DE6**。
[現物検査](../build/verification/trial-invader-20260915-203400/audit/final-jar.json): metadata3.0.0、3つのMixin config・refmap、CRC、外部Jar/class・fixture/GameTests/ExampleMod非混入、3配布Jar試験配置の同hashを確認。既存class bytesは全一致、追加4 classとmanifest/Trial JSON/refmapだけのJar差分。旧D6F…Jarは[保全](../build/verification/trial-invader-20260915-203400/before/build/libs/foodhealing-3.0.0.jar)。独立fixtureは21,942 bytes /10 entries、SHA256 `B8336DAA9BF76DFFC32965E5962B661A835FDEDE0B547EC9A69E6D67CD97B8A2`。

21:11時点の次候補だった§10.9のL2実clientは、旧runの中断（§10.10）を保全し、[新runの§10.11で限定完了](#l2-client-completed-result)。侵略者実clientは[§11.7](#trial-invader-client-partial-result)のF1/F2/S1と[§11.8](#trial-invader-client-followup-result)のS2収束/H1/seal/cleanup・通常保存終了で限定完了。他owner/一般buff解除未特定・他攻撃/別版/同時L2＋Trial・全boss/負荷は別残件。

<a id="trial-invader-client-plan"></a>

### 11.6 侵略者限定実clientの最小計画（2026-09-16 20:09 JST・承認前の計画履歴）

**以下は20:09当時の承認前計画を保全した履歴。** その後に利用者の実行承認を受け、新helper/build/起動とF1〜S2を実施した結果は[§11.7](#trial-invader-client-partial-result)。本節の「今回は未作成」「次回承認」「NOT RUN」は当時の状態であり、現在の禁止/未実施判定ではない。特にcleanupの`remove`案は実物のno-opにより失敗しており、次回へそのまま流用しない。中断後の新runは後続承認を受け[§11.8](#trial-invader-client-followup-result)で完了。本節の承認待ちは当時の状態。§10.11の完了は維持。

#### 11.6.1 根拠・構成・追加で確認する価値

- 根拠は§11.1–11.5、[既存86/86 receipt](../build/verification/trial-invader-20260915-203400/final-invader/invader-result.json)、その実fixture `src/trialInvaderTest/.../TrialInvaderVerification.java`、現在の4 production classと承認Trialの保存済みbytecode。86件の全PASS/通常保存要求を読み取り確認しただけで、suiteを再実行していない。[今回の受領物/既存証拠照合](../build/verification/trial-invader-client-plan-20260916-200036/audit/read-review.json)。
- 製品は **229,423 bytes /150 entries /SHA256 3807DECCBA79A122CD3A6431454788944ACDC72FE62A7A9BDB37C189D1B76DE6**。Trialは **1.4.9 /263,159 bytes /240 entries /SHA256 5EFE4C068F24E611C215A0A20FE9698D5C5F7EFC1476C8E5CEAEA78B8CE1A0AD**。libsと承認原物を再測定し一致。MC1.20.1/Forge47.4.0/既存Java17.0.15・検証Prismを使用する計画。47.2.0へ戻さない。
- 外側modsは製品・Trial・新しい別補助の3本だけ。L2/TaCZ/SW、旧Cube/L2補助、自動86fixtureは入れない。TrialのCuriosはoptionalで、今回装備/Dimensional Coreを使わないため追加不要。既知Prism/runtime/Forge client・universal/asset index/対象Jarは存在。**追加提供が必要と判明した外部artifactはなし**。全asset再走査/認証file読取・コピー/新downloadなし。将来の起動時に実欠損が出れば、そのファイルだけを報告して停止する。
- 86件はserver fixture＋EmbeddedChannel。今回の追加価値は「実LocalPlayerへ届くcanonical/Soul/flag/HPとGUI/HUD」「通常GUIのP/M切替が次のnative作用へ反映」「外部免疫false・Root/Truth未取得でのHuge例外」。自然boss戦、boss/Beam描画・通常AI/goal生成は今回の合格対象にしない。

#### 11.6.2 一度だけの取得・外部flag初期準備

**P=通常浄化、M=浄化の極意。Truthは取得も条件追加もしない。**

1. 新run `build/verification/trial-invader-client-<runId>/` と新instance `FHR_Invader_Client_<runId>`、新使い捨てworldだけを用意。allowlistへrun/world実path・本人UUID・製品/Trial/helper hashを拘束。旧world・receipt/journal・認証fileを流用しない。
2. 正常fresh（既存schema正常/pending=false、取得/SP/Root蓄積なし）、HP/getMaxHealth/MAX_HEALTH属性20、Soul0、外部flag/免疫/bypass/Core=false、防御/吸収/効果/装備なし、Survival/Normalを読む。本人tickCount>=100は通常経過をhelperが待ち、tickCountやHP等を直接作らない。不一致の修復なし。
3. 明示prepareを1回。Trial実clientの準備処理を必要部分だけ参考に、`IShokugiData.addUnspentSkillPoints(103)`→`trySpendSkillPoints(103)`、`setSkillLevel(foodhealing:purification,1)` / `setSkillLevel(foodhealing:purification_mastery,1)`、両保存toggle ON、`CapabilityEvents.syncToClient`。credit103/支出103/未使用0、その他canonical不変。購入gate/実際の購入処理を開かず、**GUI購入成功ではない**。
4. 同prepare内で **本人の既存falseのSoul Protectionを、実API `EntityHelper.setSoulProtected(player,true)` により1回だけtrueへ準備する案**を採用。これはF1/F2の外部flag初期値であり、製品が付与した保護とは記録しない。前後のSoul0・Health20を確認。Soul/HP/MAX setter、Dimensional Core、無敵/creative/防具/効果追加は使わない。
5. prepare前にCREATE_NEWのPREPARING receiptを書き、途中失敗/再送/再入場で再付与しない。以後、helperは本人の取得/SP/flag/HP/Soulを書かない。flag true→falseはF2の**本物の侵略者処理だけ**で起こし、F2後にfalse/免疫falseを独立server/clientで確認してBeamへ進む。F2でfalseにならなければ手動clearで合わせず停止する。

#### 11.6.3 安全環境とnative処理の回数制御

- 生成前GUIで **Survival / Normal / cheats ON / doMobSpawning=false / naturalRegeneration=false**、不要な構造物なし。入場後のgamerule/通常保存でも確認。閉じた床・壁・不透明屋根・照明の本人測定室、接触しないsource区画と退避区画を新world内の通常commandで作る。落下/水火/窒息/敵対Mob侵入をguardする。測定中にHP等を戻さない。
- **重要な実物上の制約**：Trial既定 `invaderMonolithBypassNoAI=true`。Invaderの `m_21525_()`/`m_21557_(boolean)` は単なるNoAI指定を無視し得る。またnative tickの本人flag解除はAI goal外にある。Hugeは `shouldContinue()=true` で、world登録するとdelay後に複数tickでactivateする。**囲い＋NoAIだけ、あるいはBeamをspawnして放置する方法は採用しない**。
- 推薦方式は、**検証補助の対象sourceだけの実行制御**と独立readonly observerを分離する。登録factoryで新規Invader1体を生成し、allowlist UUID・新world・run markerで限定。本人とは離れた固定位置（例：本人[0.5,80,12.5]、source[20.5,80,0.5]）へ置き、以後動かさない。
- `ScopedInvaderScheduleMixin`（予定）は、そのsourceの自動tickをserver/clientとも実行させず、F1/F2の明示permit中だけserver側の**native Invader.tick本体を各1回**通す。permit中は対象sourceの `isNoAi / m_21525_()` の結果を検証用にtrueとしてgoal進行を止める。これは **製品の効果ではなく、boss自律進行の試験用隔離**。Config・goal/攻撃内容・native targets/predicate・lambdaのflag解除・保護述語を変更しない。自動client Invader tick/自然戦闘はこの計画では未検証と明記する。
- 生logで「明示native tick2回、その他のtick本体0回、goal由来のBeam/Cube生成0回」を要求する。permit制御やNoAI隔離が一致しないなら本人に試す前に停止。tick取消自体をflag保護PASSへ数えず、各Fでnative targetsに本人が含まれ、`lambda$tick$2`へ到達した証拠を要求する。
- 各Beamは実登録Small/Huge factoryから作る **world未登録の一時インスタンス**。既存86の `setOwner`→通常NBT save/load→`getOwner`方式で、loaded BeamのOwner UUIDがlevel内の同Invader実体へnative解決されることを要求する。架空owner/class/直接Soul加算や `/damage` を代用にしない。Beamは通常次元 `highDimensional=false`、例：[0.5,80,0.5]・yaw/pitch=0、実ray方向+Z上に本人。原実装の128距離・Small幅1/Huge幅8・predicateを保持し、`activate()`を各caseのserver処理で1回だけ呼ぶ。world未登録なので自然tick/再activate/Beam描画はない。
- 実rayとInvader128範囲をreadonly確認し、許可source以外の対象候補は本人だけ。壁がnative rayを止めるとは仮定しない。以前位置/向きと現位置の差にもguardし、実callbackの対象UUID・回数を確認する。Small間はhurtTime/invulnerableTimeの自然収束をhelperが待つ（書換えない）。人間にtick計測を要求しない。
- seal/中断時はpermitを閉じ、測定snapshotを先に保存する。次いで**このhelperが生成したsourceだけ**を、Trial実物にある `EntityHelper.setBypassProtection(source,true)` と `source.remove(DISCARDED)` により終了処理し、world一覧からの消滅を確認する計画。本人のflag/HP/Soul/取得に触れない。侵略者は元からSoulProtected=trueのため、単なるdiscardが成功すると仮定しない。これは使い捨て試験actorの片付けだけで、Break Realm実装・自然boss撃破/lootの証拠ではない。生成主体/UUID不明のentityは削除しない。cleanup不成立なら隔離を維持して通常退出し、再開せず記録する。

#### 11.6.4 推薦5ケース・開始状態・期待値

**全てPLANNED / NOT RUN。** 予測は実物から計算した値で、合否は次回の実測で決める。Small既定Soul量 **0.01**、Huge **0.1**、HP最大20。Smallのnumericは `max(1.5,20×0.05)=1.5`。86件の最大HP100/数値5や試験専用Soul0.1への書換えは流用しない。Configを期待値に合わせて変更しない。

| case / 操作順 | GUIと開始条件（Soul / 実効HP / flag） | native処理と期待結果 | 実clientで追加する判定 |
|---|---|---|---|
| F1 既存flag保持 | prepare後P ON/M ON、Truth未取得。0 /20 /true | 明示Invader.tick1回、本人がnative targets/解除lambdaへ到達。flag true→true、Soul0/HP20、hurt/death0。本人false setter実呼出0 | 外部flagの元同期・server/client一致。HP不変だけでは保護成功としない |
| F2 親OFF負対照 | GUIでPだけOFF。M保存ONを保持。0 /20 /true | native tick1回、元false setterが本人へ1回。flag true→false、Soul0/HP20、hurt0。補助からclearしない | GUI非連動/正対照。両側で外部免疫falseへ収束しなければBeamへ進まない |
| S1 Small保護 | GUI PをONへ戻し両ON。0 /20 /false、Core/免疫なし | Small.activate1回、本人callback1。Soul0→0、numeric1.5がHurt/最終LivingDamage各1、HP20→18.5、flagfalse。sourceは同Invader | P/M条件が実GUIから反映、新規Soul抑止と独立numericを別観測 |
| S2 Mastery OFF負対照 | GUI MだけOFF、P保存ON。0 /18.5 /false | Small.activate1回、Soul0→0.01、numeric1.5各1、HP18.5→17、flagfalse | native Soul作用が戻る実client正対照。通常浄化がSoulを消すと期待しない |
| H1 Huge保護 | GUI M ONで両ON。0.01 /17 /false、Truth/Rootなし、hurt時間収束 | Huge.activate1回、native本人callback1。Soul0.01→0.01、元Float.MAX_VALUE call-siteを製品例外で抑止、実hurt/最終LivingDamage/death0、HP17、flagfalse | false免疫・正常canonicalの実playerで例外成立。既存Soul0.01を治療しない |

F1→F2→S1→S2→H1のみ、**prepare1 / native Invader tick2 / Small.activate2 / Huge.activate1**。全ケースの対象UUID/owner/次元、追加呼出0を照合する。H1はS1/S2でnative命中と観測器の正対照が成立し、最新GUI同期と正常保護述語が一致してから許可する。HugeのOFF・他owner・非保護playerへの攻撃は行わない。保護経路に不具合があればHugeは致死となり得るため、無敵/回復による安全偽装はせず、予期しないhurt/死亡をFAILとして停止・保存する。死亡を成功条件にはしない。

H1後は追加attack/flag操作をせず両ONのまま、Soul0.01・HP17・flagfalse・MAX20・SP0/103・2node・Truth0を保持してseal。再ONでSoulを治療しなかったことはこの値で限定確認する。

#### 11.6.5 readonly観測・許容差・合格条件

- 通常GUIが製品 `ToggleSkillPacket` / server取得検証 / `CapabilityEvents.syncToClient` を通ったことを、独立したserver本人と実LocalPlayerのcanonical（2node Lv1、SP0/103、P/M保存toggle、Truth0、pending/schema/Root）で確認。helperからtoggle packetを代送してGUI成功にしない。
- 両側の `EntityHelper.getSoulDamage/isSoulProtected/isImmuneToSoulDamage/hasDimensionalCore/shouldBypassProtection`、`getHealth/getMaxHealth/MAX_HEALTH属性`、効果/armor/absorption/位置/UUID、画面名をcase/run/gameTimeとともに記録。client値をserver snapshotのコピーで作らない。検証observerはゲームstateを一切書かない。
- **値の意味**：SoulはTrialの蓄積値、外部flagはMobEffectではない。flagtrueならTrialはgetHealthへ介入するため、F1のHP不変をnumeric取消の証拠にしない。Beamはflagfalse/Soul<1で実効HPとイベントを比較。Soul0.01時の計算上限は20×0.99=19.8だが、属性MAXは20のまま、実効HP17/HUD17.0/20.0が予定。19.8までの回復頭打ちは未試験。保存NBT Healthは保存経路の値として別欄へ記録し、「生のHP」や属性最大値と同一視しない。
- readonly probeでInvader tick/解除lambdaの入退場・本人false setter呼出、Small/Huge.activate入退場・native本人callback、`TrialInvaderCompatibility.blocksBeam`の実呼出結果、Soul normal/forceの実setter/add呼出を記録。保護判定・return値・amount・event取消・HP/flag/Soulをobserverから変更しない。
- Smallはdamage source **the_trial_monolith:laser_attack**、native ownerとDamageSourceのgetEntity/getDirectEntity（実値）、Hurt/最終LivingDamageの回数/amount/取消を記録。ownerはGUI名でなく実class/type/UUID/level、BeamのOwner NBTとgetOwner解決の一致で判定する。Hugeは本人callback到達を要求し、単なる射線外/不命中の「0 damage」をPASSにしない。
- Hugeの元引数Float.MAX_VALUEと製品redirectの通過は、既存Mixin exportにある `foodhealing$hugeBeamNumeric(Entity,DamageSource,float)` 実体をreadonlyで観測する予定。合成methodのprefix（旧exportでは `redirect$zbb000$`）を固定公開API扱いせず、次回の限定compile/起動準備でsuffix＋descriptor・実ターゲット1件を照合する。観測hookが成立しなければ攻撃前停止。期待値は **元引数Float.MAX_VALUE / 製品redirect return false / 実hurt呼出0**であって「amountを0に書き換えた」ではない。
- Soul許容 **1e-5**、HP/MAX/amount **1e-3**、flag/canonical/UUID/回数は完全一致。server/clientの期待値2連続一致を要求、通常進行中の収束上限100 server tick（5秒相当）/壁時計10秒。pause時間はtick待ちへ数えず、失敗なら進めない。HUD小数1桁は±0.05、画面はGUI/flag観測後・S1の18.5/20.0・S2/H1の17.0/20.0をF2で保存する。flag/Soulの数値表示が通常画面にない部分はobserver証拠であり、目視PASSへ補完しない。
- 未来の記録はF1/F2/S1/S2/H1、GUI、client同期/HUD、正常保存/終了を個別PASS/FAIL/UNVERIFIEDで報告する。今回の計画文書更新は試験PASSではない。

#### 11.6.6 作成予定の別補助・書込境界（今回は未作成）

新verification root内の `fixture/src/main/java/com/leva/foodhealing/verification/clientinvader/` を予定。旧helperは参考読取だけで、旧receipt/journal/allowlistや86fixtureを丸ごと使わない。

| 予定ファイル | 役割・許容処理 |
|---|---|
| TrialInvaderClientVerification.java | run/world/hash/本人/phase guard、CREATE_NEW prepare1、credit103/2nodeと初期flagtrue1回、正常sync、seal/FAILED journal。再login/tickで再注入しない |
| TrialInvaderClientDriver.java | 対象Invader1体の準備、明示permit tick2、3個の実Beam生成/Owner native解決/activate各1、終了時の自分のactorだけのcleanup。測定中に本人値を修復しない |
| TrialInvaderServerObservation.java / TrialInvaderClientObservation.java | 独立readonly snapshot、event/probe、周囲安全、case/tick/UUID/回数の突合せと異常通知。書込先はaudit/journalのみ |
| mixin/ScopedInvaderScheduleMixin.java | allowlist sourceだけの自動tick停止とpermit中のNoAI隔離。本人/他entityへ作用しない。native解除lambda/Beam/hurt/Soul/保護述語を取消さない |
| mixin/InvaderObservationMixin.java / BeamObservationMixin.java / EligibilityObservationMixin.java | native境界/実redirect/述語のread-only probe。cancel/setReturnValue/対象値setterなし。schedule制御と区別 |
| META-INF/mods.toml、別Mixin JSON、pack.mcmeta、専用init Gradle | 別MODとfixture出力だけ。製品Jar・build.gradle・Config・購入gateを変更しない |
| allowlist / journal / receipt / snapshot / 保存解析記録 | 初期化1回・各case再送拒否、失敗保全。artifact/hash/actor UUID・native回数を証拠化 |

次回は旧成功ローカルGradle/offline手順を使い、専用task graphを先に確認。許容はhelper compile/resources/Jar/reobfと既存offline cache処理のみ。製品compileJava/classes/jar/reobfJar/build/check/unit/GameTest/Invader86/Cube49/L295/core60を巻き込むなら停止。補助Jar hash/size/entries・実Mixin対象/書込経路を検査し、新instance modsだけへ配置する。observerとscheduleの役割分離、flag準備1回・以降helper本人書込0をレビューしてから測定する。

#### 11.6.7 86件に任せる項目・保存再読込の要否

| 範囲 | 今回の分担・理由 |
|---|---|
| native flagの9状態×true/false、大小/ordinary-force×9状態、未取得/pending/schema/overlevel | 既存86の18＋36件を保持。実clientではGUI親OFFとM OFFの代表だけ。未取得/不正データを本人へ注入し直さない |
| force・Soul閾値1/10・強制死/respawn | 既存16件等でnative維持済み。実clientに死亡試験を追加せず、普通の低Soul域を測る |
| Huge OFF・cow/別boss/player/null owner・非Player・他player・同laser直接hurt | 既存8 owner＋同ray二者＋非Player＋直接hurt対照に任せる。本人の致死比較、第二account要求や擬似二者を追加しない。実clientで他player非漏洩を今回PASSへ昇格しない |
| native goal生成/攻撃entity自身flag・全boss/Beam描画 | goal2件は既存自動証拠。今回native明示triggerは自律goal/自然boss戦や描画の証拠ではない |
| 一般MobEffectの敵対解除 | 未特定のまま。架空hook/架空試験なし、外部flagとは分離 |
| **通常world再読込** | **今回の必須ケースから外す（0回）**。侵略者追加はcall-site限定でschema/packet/恒久marker追加なし。86にowner UUID save/load/native解決と通常PlayerList保存再読込、既存Cube/L2に実clientの保存系証拠がある。今回の目的は現在のGUI・攻撃・client表示で、新しい永続状態を再読込で証明する必要がない。既存証拠を今回の侵略者world再読込PASSへ転記もしない |

正常保存自体は必須。H1後に両ON/Truth0、Soul0.01・flagfalse・実効HP17・MAX20、SP0/103、prepare1/nativeTick2/Small2/Huge1を確認→seal/actor cleanup→**通常Save & Quit→全dimension保存完了→readonly保存照合→Quit Game→process終了**。level.datの本人とplayerdata（存在する保存先）、Health/Trial SoulDamage・SoulProtection/canonical、GameRules、receipt/SEALEDを照合する。再読込はせず、worldは保全して再利用しない。schema/保存実装に別変更が発見された場合だけ、この判断を見直し、自動で追加reloadしない。

#### 11.6.8 停止条件・次回承認の一括範囲

- freshness、hash/version/config、外部flag/免疫、NoAI/permit隔離、native target/owner/回数、同期・HP/Soul・numericが不一致なら次のcaseへ進まない。無関係な被弾/回復、侵入/燃焼/落下、想定外Beam生成、prepare途中失敗/再送、観測器矛盾も即停止。
- まずpermitを閉じて新たな作用を止め、通常pause/退出を優先する。対象actorの安全終了と証拠を保全し、可能な範囲で通常保存/ゲーム終了。HP/flag/Soul/receipt修復、同case再試行、復活・再入場による続行、guard/期待値緩和はしない。製品不具合は補助不備と分けて報告し、自動修正しない。
- **次回この計画を承認すれば進める範囲**：別補助作成・限定offline compile/reobf/書込・schedule・probe検査→新instance/world・生成前安全設定→本人prepare1（外部flag初期true1回を含む）→通常GUIの5ケース/native tick2＋activate3→両ON保持/seal/actor cleanup→通常保存/readonly照合/終了→製品・既存run不変照合→3文書更新。routine工程ごとの再承認は不要とする案。通常GUI/command/F2/終了はComputer Use、本人認証/OS権限/実行できない入力だけ人間へまとめて依頼する。現在の認証を読取保証せず、必要になった時点で本人に依頼する。
- 追加仕様質問はなし。追加外部Jarの不足なし。**残る開始条件はこの具体的な検証方式の実行承認**であり、今はhelper作成・build・起動へ進まない。製品修正/購入解放、自然boss戦、実client force/致死対照、全86再現、他L2 trait/L2＋Trial、Flight/Break Realm/Bulwark/試作型機関弩/FOURTH BOOTは含めない。
- 両極意IMPLEMENTATION_PENDING/購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、その他既存個別gateを維持。食料生産の極意による可逆クラフト増加は既知かつ許容された仕様、死亡drop/replay/desync等の意図しない重複へは拡張しない。


<a id="trial-invader-client-partial-result"></a>

### 11.7 侵略者限定実clientの部分実施・安全監視停止（2026-09-16 21:04 JST）

**F1/F2/S1 REAL CLIENT LIMITED PASS。S2はserver期待値成立、client収束完了判定前に停止したため総合UNVERIFIED。H1 NOT RUN。** 既存86/49/95/60、L2 §10.11を再実行せず保持。全5ケース完了/侵略者全体PASSとはしない。[集計と生証拠参照](../build/verification/trial-invader-client-20260916-201822/audit/stopped-run-analysis.json)。

#### 対象・build・書込境界

- run `20260916-201822`、新instance `FHR_Invader_Client_20260916-201822`、新world `FHR_Invader_20260916_201822`。本人 `<PLAYER_UUID>`、source `adcbb522-f4af-48c0-bb19-df768719b83b`。実world path/本人/run/製品・Trial・helper hashをallowlistへ限定。旧world/receiptを流用していない。
- [実ロード](../build/verification/trial-invader-client-20260916-201822/audit/runtime.jsonl)：MC1.20.1/Forge47.4.0/Java17.0.15、Trial1.4.9、Food Healing3.0.0、新別MOD `foodhealing_invader_client_verification` 1.0。外側modsはこの3本のみ。L2/TaCZ/SW/旧補助/86fixtureなし。
- 製品 **229,423 bytes/150 entries**、SHA256 **3807DECCBA79A122CD3A6431454788944ACDC72FE62A7A9BDB37C189D1B76DE6**。Trial **263,159 bytes/240 entries**、SHA256 **5EFE4C068F24E611C215A0A20FE9698D5C5F7EFC1476C8E5CEAEA78B8CE1A0AD**。新補助最終 **39,759 bytes/22 entries**、SHA256 **51614EA3BDB29E099947A491CB9A2F76C08D40DCEA9B3B0C8582500875E7F15E**。[原物/配置照合](../build/verification/trial-invader-client-20260916-201822/audit/final-integrity.json)。
- 許可した変更は新verification rootの別補助/専用init、新instance/world、audit、今回3文書だけ。prepareはP/M各Lv1・credit103/spend103/unused0・初期外部flagtrue1回と元sync。購入処理を通した成功ではない。prepare後の本人取得/SP/toggle/HP/MAX/Soul/flagは補助から修復・再設定していない。observerはreadonly、schedule隔離はown sourceだけ。cleanupのsource bypass設定は本人保護と別。[事前境界レビュー](../build/verification/trial-invader-client-20260916-201822/audit/boundary-review.json)。
- 成功した専用[task graph](../build/verification/trial-invader-client-20260916-201822/audit/task-graph.txt)は補助compile/resources/classes/jar/reobfと既存offline cacheのMCP/SRG処理だけ。製品compileJava/jar/reobfJar/build/unit/check/GameTest/各既存suiteなし。最初のdry-runはGradle user-home参照不足、次はsandbox native DLL制限で失敗し、正しい既存user-home/offlineと許可された実行で解消。downloadなし。
- [初回補助build](../build/verification/trial-invader-client-20260916-201822/audit/helper-build-01.log)後の起動はBeam callback観測descriptorの`Level`引数不足で失敗（world/prepare前）。[失敗log](../build/verification/trial-invader-client-20260916-201822/audit/launch-01-failed.log)・crash・39,733 bytesの旧補助を保全。検証補助だけ修正し[build02](../build/verification/trial-invader-client-20260916-201822/audit/helper-build-02.log)、起動2で下記測定。測定停止後のhelper再修正/再build/同case再試行なし。

#### 安全準備・取得1回・GUI

- 生成前GUIでSurvival/Normal/cheats ON、自然湧きfalse・自然回復false、Superflat/構造物OFFを記録。入場後guardと通常保存のGameRulesでも反映を確認。石の床/壁/不透明屋根・照明、source区画との壁、退避区画を新world内だけに準備。sourceの高さに合わせ上部照明を取り除いたが石屋根は維持。本人位置(.5,80,12.5)、source(20.5,80,.5)、周囲の外部Enemyなし/落下・火水・窒息なしをprepare前に確認。
- 正常fresh・HP/MAX/属性20・Soul0/flagfalse・Root/Truth/防御/吸収/効果なし、既定Small Soul0.01/Huge0.1、Trial bypassNoAI=trueを読取確認。20:40:28に明示prepare1回、P/M ON・SP0/103・外部flagtrueへ準備。[receipt](../build/verification/trial-invader-client-20260916-201822/audit/prepare-receipt.json) / [完了](../build/verification/trial-invader-client-20260916-201822/audit/prepare-complete.json)。再付与・取得再注入なし。
- GUIは通常Food Healing画面→製品packet→server canonical/元sync。F2前にPだけOFF（M保存ON）、S1前にP ON、S2前にMだけOFF（P保存ON）。Truthは未取得。helperによるtoggle代送なし。10枚のF2画像の実path/hashは集計に記録。通常画面にないSoul/flagはreadonly観測であり目視成功とは書かない。

#### 各ケースの実測と合否

| case | 実条件・native到達 | 実測（期待と一致した範囲） | 判定・証拠 |
|---|---|---|---|
| F1 | P/M ON、native tick1、targetsに実本人・解除lambda1 | flag true→true、false setter0、Soul0/HP20/MAX20、hurt/death0 | **REAL CLIENT LIMITED PASS**。[DONE](../build/verification/trial-invader-client-20260916-201822/audit/F1-DONE.json)。server/client2連続、2ticks/93.126ms |
| F2 | GUI P OFF/M保存ON、native tick1、targets本人・lambda1 | 元false setter1、flag true→false、Soul0/HP20/MAX20、hurt0 | **REAL CLIENT LIMITED PASS**。[DONE](../build/verification/trial-invader-client-20260916-201822/audit/F2-DONE.json)。2連続、3ticks/145.622ms、外部免疫も両側false |
| S1 | GUI P ON/M ON、Small.activate1/native本人callback1、実Invader owner | 新規Soul加算0、Soul0、実hurt/Hurt/final Damage各1・amount1.5/uncancelled、HP20→18.5、flagfalse | **REAL CLIENT LIMITED PASS**。[DONE](../build/verification/trial-invader-client-20260916-201822/audit/S1-DONE.json)。2連続、2ticks/83.491ms、HUD18.5/20.0 |
| S2 | GUI M OFF/P保存ON、Small.activate1/native本人callback1、同owner | native Soul加算1×0.01、Soul0→0.01、実hurt/Hurt/Damage各1×1.5、HP18.5→17、flagfalse/MAX20 | **server期待値成立・総合UNVERIFIED**。[server結果](../build/verification/trial-invader-client-20260916-201822/audit/server-results.jsonl)。直後の安全監視FAILでS2-DONEなし。停止後の実client値とHUD17.0/20.0は一致したが、所定の収束完了へ補完しない |
| H1 | 未要求・Huge.activate0 | Float.MAX_VALUE実到達・本人callback・製品redirect抑止は未観測 | **NOT RUN / UNVERIFIED**。Hugeの0damage/保護PASSではない |

- F1/F2でnative tick累計2、permit外本体0・goal由来生成0を[記録](../build/verification/trial-invader-client-20260916-201822/audit/native-targets.jsonl)、S1/S2実行前も同条件guardを通過。Small2/Huge0。最終seal回数receiptは未作成なので「予定全回数の完了」とはしない。停止時permit閉鎖・自動tick隔離を維持。
- Small2回とも実registered factory→setOwner→native NBT save/load→同Invader getOwner解決。[owner](../build/verification/trial-invader-client-20260916-201822/audit/owner-resolution.jsonl) / [ray/本人だけのpreflight](../build/verification/trial-invader-client-20260916-201822/audit/beam-preflight.jsonl)。highDimensional=false、world未登録、native callback各1。sourceは`the_trial_monolith:laser_attack`、DamageSourceのentity/directEntityは両方とも同Invader UUID。手作りsource・直接Soul加算・/damageではない。
- Hugeの変換後suffix/descriptorを実ロードで1件照合し、return値不変の観測hookを確認：[実変換](../build/verification/trial-invader-client-20260916-201822/audit/huge-hook-transformation.json)。しかし**H1未実行のため元Float.MAX_VALUE call-siteの実到達は0回/未検証**。hook準備完了を保護実測PASSとしない。
- 許容差はSoul1e-5、HP/MAX/amount1e-3、flag/canonical/UUID/回数完全一致。完了3ケースの実収束は100tick/壁時計10秒以内。ただし補助コードは100tick上限だけで独立した10秒deadlineを実装していなかった。結果の実測時間を示し、この補助不足は次runで修正する。S2は停止後snapshotを遡ってDONE化しない。

#### 停止原因・不足・次回条件

1. **20:50:02.004 S2直後の`physical safety`停止**。[failure](../build/verification/trial-invader-client-20260916-201822/audit/failure.json)。`onGround && !isOnFire && !isInWaterOrBubble && fallDistance==0`の複合条件が失敗し、どの値が原因か当該tickに未記録。位置とHP/Soulは期待内、後続保存はOnGround1/FallDistance0/DeathTime0だったが、それで失敗瞬間を確定できない。native攻撃直後の一時状態という可能性は未立証。製品不具合や実侵入と断定しない。次caseを止め、permit閉鎖→通常pauseを優先した。
2. **中断後のown actor cleanup1回がFAIL**。[前後](../build/verification/trial-invader-client-20260916-201822/audit/cleanup.jsonl)。本人canonical/HP/Soul/flag不変。補助は計画どおりsource bypass=true→`remove(DISCARDED)`を呼んだが、承認TrialのInvaderは`remove/m_142687_`を即returnへoverrideしている。native kill側は`setRemoved/m_142467_`を使用する。原因は計画/補助のcleanup method選択で、Food Healingの製品修正対象とは判断していない。別methodを試して無理に消さず隔離のまま通常退出。sourceは保存に1体残る。
3. **次は別新runの承認待ち**。補助だけに失敗直前の各物理値/velocity/block/phase/tickのreadonly記録、独立壁時計guard、所有者限定のnative cleanup経路修正を用意する。危険とnative一時状態を判別できることを測定再開条件にし、guard削除/HP等の補正で成功させない。今回は新helper修正/再起動なし。F1/F2/S1は完了を維持し、S2収束/H1/seal/正常cleanupが残る。追加artifact/新仕様判断の既知不足はない。

#### 通常保存・終了・不変・判定の限界

- **seal NOT RUN、cleanup FAIL、通常保存/ゲーム終了は完了**。20:51:15.392 `All dimensions are saved`、20:52:36.447 `Stopping!`、[PID12280終了](../build/verification/trial-invader-client-20260916-201822/audit/process-exit.json)。[終了log](../build/verification/trial-invader-client-20260916-201822/audit/launch-02-final.log)。通常Save & Quit後に保存競合なしでコピーをreadonly解析。
- level.dat本人とplayerdata双方：**HP17/MAX属性20、Soul0.009999999776482582（約0.01）、SoulProtection=false、P ON/M OFF、P/M Lv1、SP0/103、Truth/Root0、pending=false、DeathTime0**。[通常保存](../build/verification/trial-invader-client-20260916-201822/audit/final-save-values.json) / [Trial値](../build/verification/trial-invader-client-20260916-201822/audit/saved-trial-values.json)。成功予定の両ON/SEALEDへ書き換えていない。prepare1/Small2/Huge0、SEALEDなし、source UUID1体を保全。world再読込0、再利用なし。今回の保存一致は成功sealやworld再読込PASSではない。
- [開始前とのhash比較](../build/verification/trial-invader-client-20260916-201822/audit/final-integrity.json)：製品/source/test/build.gradle/Config等144件、旧証拠231件、限定確認した旧world/instance等85件は全不変。製品/Trial原物と配置Jar一致。現在のL2 §10.11本文も不変。PC全体/旧world総走査を行った意味ではない。
- COMPUTER USE：通常GUI/command/F2/pause/保存退出/Quit。AUTOMATED：一度だけの準備、native trigger、独立server/実client readonly観測、保存/証拠解析。HUMAN：今回ゲーム入力依頼なし。
- 自然boss戦・自動client boss tick・Beam描画、force/閾値死/Huge OFF、他owner/他player/全86再現、一般MobEffect敵対解除、回復上限・別版/全MOD/実2-clientは今回未確認。86の該当結果を維持するが実clientへ転記しない。既存asset/optional class等の警告も原logへ残し、全WARN解消とはしない。
- 製品/既存test/購入gate/SP保護に変更なし。両極意IMPLEMENTATION_PENDING、RC=NO、REAL2CLIENT=BLOCKED、他個別開始条件を維持。食料生産の極意の可逆クラフト増加は既知許容仕様・バグ修正対象外。

両極意購入停止/SP保護、RC=NO、REAL 2-CLIENT=BLOCKED、ALL TACZ/ALL GUNPACK=NOT TESTED、Flight/Break Realm/Bulwark/FOURTH BOOT/試作型機関弩等の個別条件を維持。食料生産の極意の可逆クラフト増加は既知かつ許容された仕様・バグ修正対象外。死亡drop等の意図しない重複は許容しない。

<a id="trial-invader-client-followup-result"></a>

### 11.8 新runのS2収束・H1 Huge・seal/cleanup・正常終了（2026-09-16 21:51 JST）

**S2 / H1 = REAL CLIENT LIMITED PASS。** [新runの集計と全生証拠への参照](../build/verification/trial-invader-client-20260916-211828/audit/completed-run-analysis.json)。旧run `20260916-201822` のF1/F2/S1 PASSを維持し、旧S2は総合UNVERIFIED・旧H1はNOT RUNのまま。旧DONEの事後作成・旧world再利用なし。本節は新run `20260916-211828` 自身の証拠。5ケースを同一runで完了したとは記録しない。

#### 対象・補助だけの限定修正

- MC1.20.1 / Forge47.4.0 / Java17.0.15 / Trial1.4.9。製品 **229,423 bytes /150 entries / SHA256 `3807DECCBA79A122CD3A6431454788944ACDC72FE62A7A9BDB37C189D1B76DE6`**、Trial **263,159 bytes /240 entries / `5EFE4C068F24E611C215A0A20FE9698D5C5F7EFC1476C8E5CEAEA78B8CE1A0AD`**。配置先の同hashを再測定。[実ロード](../build/verification/trial-invader-client-20260916-211828/audit/runtime.jsonl)。外側modsは製品/Trial/今回helperの3本だけ。
- 新root `build/verification/trial-invader-client-20260916-211828`、instance `FHR_Invader_Client_20260916-211828`、world `FHR_Invader_20260916_211828`。本人UUID `<PLAYER_UUID>`、実path/run/hash/新tokenを限定。旧allowlist/receipt/journalは流用しない。
- helper **42,544 bytes /22 entries / SHA256 `820DDC31357365A3A81C83A2D4E41A7D7777EC35026476E2B58A09226983F7D0`**。`TrialInvaderClientVerification`へ判定前/直後の各物理operand、限定自然収束、独立wall watchdog、準備用Smallと測定の分類を追加。Driverのown cleanupを`setRemoved`へ修正。ServerObservationは準備用分類とHuge保護判定の非空確認を追加。readonly LocalPlayer observerを維持。[差分](../build/verification/trial-invader-client-20260916-211828/audit/helper-source.diff) / [書込境界](../build/verification/trial-invader-client-20260916-211828/audit/boundary-review.json)。測定開始後の補助修正はない。
- 既存local Gradle8.1.1・`--offline --no-daemon -I .../invader-client.init.gradle invaderClientVerificationJar`のみ成功。[task graph](../build/verification/trial-invader-client-20260916-211828/audit/task-graph.txt) / [build](../build/verification/trial-invader-client-20260916-211828/audit/helper-build-01.log)。最初のsandbox native DLL初期化失敗は[dry-run](../build/verification/trial-invader-client-20260916-211828/audit/build-dry-run.log)へ保全し、同じlocal/offlineコマンドの権限実行で解消。mapping taskはoffline cache、downloadなし。製品jar/reobfJar/build/unit/check、86/49/95/core60は実行しない。build toolchain Java17.0.7とゲームJava17.0.15は区別する。
- **100 server ticksと10秒monotonic wall-clockの両上限**。独立25ms watchdogがdeadline超過時FAILED/permit閉鎖、各phaseとDONE直前も検査し、DONE作成と期限判定を同じ同期領域で処理。pauseはtick進行へ加算せず、壁時計は止めない。[期限記録](../build/verification/trial-invader-client-20260916-211828/audit/deadlines.jsonl)。今回の実収束は両上限内。意図的な期限超過の負試験は未実施であり、過去F1/F2/S1を新wall guardの試験PASSへ転記しない。

#### 新規安全環境・preparation-only

- 生成前GUIでSurvival/Normal・`doMobSpawning=false`・`naturalRegeneration=false`、structures OFF。F2 `21.26.02/31/54`。入場後に通常fillで石床/壁/不透明屋根（x-8..28,y79..86,z-8..24）、source隔壁x10・退避室・照明を作り、本人を(.5,80,12.5)へ通常tp。取得前にguardが閉鎖全周/足場/周囲140blockの外部敵0/ルール/HP20/MAX20/Soul0/freshを確認。F2 `21.32.41`。位置合わせは測定前の1回だけ。
- prepare1回、P/M各Lv1、credit103/spent103/unspent0、両ON、Truth/Root未取得。[receipt](../build/verification/trial-invader-client-20260916-211828/audit/prepare-receipt.json)。通常GUI購入成功ではない。**新runでは外部flagを一切初期設定せずfreshのfalseを保持**。Invader tick/F1/F2は実行0。
- **PREP_SMALL1回はpreparation-only**。実registered Small→Owner NBT save/load→同Invader解決→native ray/本人callback1。Soul0/flagfalseを保持してHP20→18.5、actualHurt/Hurt/Damage各1×1.5。3ticks/146.8906msで2連続収束、[準備完了receipt](../build/verification/trial-invader-client-20260916-211828/audit/PREP_SMALL-DONE.json)のstatusは`PREPARATION_COMPLETE_NOT_TEST_PASS`。旧S1の再証明や新PASS件数ではない。HP/Soul/flag/velocity/fallDistanceをhelper setterで補正しない。

#### 新runの実測

| case | 開始条件・native経路 | 実測 | 収束・判定 |
|---|---|---|---|
| S2 | 通常GUIでMだけOFF、P ON、Truth/Root未取得、SP0/103、HP18.5/MAX20/Soul0/flagfalse、免疫/Core/bypassなし、hurt/invuln0、厳密physical安定 | Small.activate1/本人callback1、SoulAdd1×0.01、Soul0→0.01、HP18.5→17。actualHurt/LivingHurt/LivingDamage各1×1.5 uncancelled、owner/direct同Invader、source `the_trial_monolith:laser_attack` | **REAL CLIENT LIMITED PASS**。gameTime8564→8567、3ticks/147.3694ms、厳密安全＋server/client canonical/Soul/flag/HP/MAX2連続一致。[S2-DONE](../build/verification/trial-invader-client-20260916-211828/audit/S2-DONE.json) |
| H1 | S2-DONE後、通常GUI M ONで両ON。HP17/MAX20/Soul0.01/flagfalse、Truth/Root未取得、外部免疫/Core/bypassfalse、armor/absorption0、hurt/invuln0、厳密physical安定 | registered Huge（world未登録）→Owner NBT save/load/native owner解決→実ray/本人callback1。activate1、Float.MAX_VALUE call-site→専用redirect Enter/Return各1（3.4028235E38）、native作用内保護判定true、returnfalse。実Entity.hurt/LivingHurt/LivingDamage/death0。Soul0.01/HP17/flagfalse維持 | **REAL CLIENT LIMITED PASS**。gameTime9798→9801、3ticks/146.7827ms、2連続一致。[H1-DONE](../build/verification/trial-invader-client-20260916-211828/audit/H1-DONE.json) / [probes](../build/verification/trial-invader-client-20260916-211828/audit/server-results.jsonl) |

- H1実ロードで`HugeBeamEntity`の製品専用redirect **descriptor `(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/damagesource/DamageSource;F)Z` を1件**、return site1・戻り値保持を確認。[実変換](../build/verification/trial-invader-client-20260916-211828/audit/huge-hook-transformation.json)。合成suffixを固定API扱いしない。保護判定記録3件はpreflight1＋実Beam内Soul/numeric各1であり、攻撃3回ではない。射線外/callback0による無傷ではない。
- 共通source UUID `1f19a126-7f75-4ff8-9841-b6211dcddb6e`。Owner NBTから同sourceへ解決。[owner](../build/verification/trial-invader-client-20260916-211828/audit/owner-resolution.jsonl) / [ray事前確認](../build/verification/trial-invader-client-20260916-211828/audit/beam-preflight.jsonl)。Smallは準備1＋測定1、Huge測定1、native Invader tick0、permit外本体0/goal生成0。helperは製品保護を代行せず、自動攻撃の隔離だけを所有sourceに限定。
- COMPUTER USEは通常GUI/command/F2/保存/Quit、AUTOMATEDはprepare/native triggerと独立ServerPlayer/実LocalPlayerのreadonly観測・保存解析。HUMANゲーム入力依頼0。GUI M OFFはF2 `21.35.08`、S2 HUD17.0/20.0は`21.35.54`、M ONは`21.38.01`、H1 HUD17.0/20.0は`21.38.53`。Soul/flagは通常HUD値でなくobserver証拠。[画像path/hash一覧](../build/verification/trial-invader-client-20260916-211828/audit/completed-run-analysis.json)。

#### physical safetyのoperandと自然収束

判定前にrun/case/phase・gameTime・UTC時刻/monotonic nanos・本人UUIDと全operandを[同一snapshot](../build/verification/trial-invader-client-20260916-211828/audit/physical-snapshots.jsonl)へ保存。下表は新run S2の実測。旧runの失敗瞬間を遡って確定したものではない。

| 時点 | gameTime | position / deltaMovement | onGround | hurtTime / invulnerableTime |
|---|---|---|---|---|
| native直前 |8564|(.5,80,12.5) / (0,-0.0784000015258789,0)|true|0 /0|
| native return直後 |8564|同位置 / (0,0,0)|true|10 /20|
| 次tick・待機 |8565|同位置 / (0,-0.0784000015258789,0)|false|9 /19|
| strict復帰1 |8566|同位置 / (0,-0.0784000015258789,0)|true|8 /18|
| strict復帰2・DONE |8567|同位置 / (0,-0.0784000015258789,0)|true|7 /17|

- 全上記snapshot：isOnFire=false/fireTicks=-20/isInWater=false/isInWaterOrBubble=false/fallDistance0、below=stone・feet/head=air、周辺foreign hostile0・foreignDamageCount0・foreignEventCount0、effects[]/armor0/absorption0/MAX20/flagfalse。HP/Soulはnative前18.5/0→後17/0.01。HP/MAX・Soul・flags・SP/canonicalも同時記録。
- PREP_SMALLとH1も同じ位置/足場・危険なしで、次tickだけonGround=false、その後2tick連続trueへ復帰。H1は9798→9799(false)→9800/9801(true)、hurt/invulnは全0。実Trialの後続motion reset直後にvelocity0を観測し、位置/落下距離不変の接地flag一過性を判別した。自然物理の復帰を待っただけで、helperの本人位置/velocity/HP等setterなし。[待機記録](../build/verification/trial-invader-client-20260916-211828/audit/natural-settle.jsonl)。
- 限定待ちは「native直前strict安定・直後100ticks/10秒内・完全な足場・位置範囲・水火/落下/侵入なし・水平速度<.001/垂直<.1」のみ。完了には元のstrict onGround=true連続2回を要求。原因不明/実危険/期限超過を通すguard削除なし。

#### seal・own cleanup・通常保存/終了

- [SEALED](../build/verification/trial-invader-client-20260916-211828/audit/seal.json)：prepare1、preparationSmall1、measurementSmall1、Huge1、nativeTick0。P/M Lv1両ON、SP0/103、Truth/Root0、Soul0.01/flagfalse/HP17/MAX20。新runの未完了caseなし。
- 旧失敗の`remove(DISCARDED)` no-opを保持。今回だけのsource UUID・run marker・world・helper ownershipを照合し、Trial native kill側と同じ **bypass(source,true)→setRemoved(DISCARDED)** を別helperから1回実行。外部Jar/製品へcleanup変更なし。[前後](../build/verification/trial-invader-client-20260916-211828/audit/cleanup.jsonl)：removed=true/worldAbsent=true、本人canonical/HP17/Soul0.01/flagfalse/位置は同一。無関係entityの削除0。正常保存のsource regionにも同UUID0件。
- 通常Save & Quit後 **21:39:56.568 All dimensions are saved**、競合なしで対象保存をコピーしreadonly解析。[保存2箇所](../build/verification/trial-invader-client-20260916-211828/audit/final-save-values.json)：P/M各Lv1/両ON、Truth/Rootなし、SP0/103、Health17、MAX属性20、Soul **0.009999999776482582**、SoulProtection=false、DeathTime0、両GameRules=false、Survival/Normal、receipt1/SEALEDあり。保存値上書きなし。**world再読込0**。
- **21:40:32.676 Quit Game / Stopping!、PID11644終了**。[process](../build/verification/trial-invader-client-20260916-211828/audit/process-final.json) / [最終log](../build/verification/trial-invader-client-20260916-211828/audit/game-final.log)。正常終了のため強制killなし。新world/補助/証拠も以後保全する。

#### 不変・残る範囲

- [開始前hashとの比較](../build/verification/trial-invader-client-20260916-211828/audit/completed-run-analysis.json)：製品source/test/build.gradle/Jar/Config等144、旧証拠231、既知旧world/instance85、直前旧run/instance170ファイルすべて不変。集合に重複はあり、PC全体の監査件数ではない。旧§11.7・L2 §10.11の本文とTEST_PLAN §18も保全。git差分確認ではない。
- 限定helper compile/実client/readonly解析のみ。製品修正・Jar再生成・既存suite再実行・Security scan・download・認証ファイル読取なし。製品不具合を示す不一致は今回なし。新runの既定Config補完/asset/optional class/IPv6等WARNは原logへ保持し、全WARN解消とはしない。
- 自然boss/Beam描画・force/閾値死/Huge OFF本人死亡、他owner/他player実client、全86実画面、一般MobEffect敵対解除未特定、L2＋Trial、他trait/別版/全体readinessは未確認。既存86の該当結果を保持するが実clientへ転記しない。今回の予定限定確認は終了し、次の対象と実行範囲は利用者指定待ち。
- 両極意IMPLEMENTATION_PENDING/購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、その他の開始条件を維持。食料生産の極意の可逆クラフト増加は既知許容仕様・バグ修正対象外。

<a id="mastery-readiness-inventory"></a>

## 12. 購入readinessの棚卸し（9/16静的調査を維持、9/20完了反映・購入方針は§12.5）

### 12.1 結論と分類の根拠

**開始時の直接停止原因はP/Tの固定IMPLEMENTATION_PENDING登録だった。2026-09-29 §14.58で2 IDだけ解除しPURCHASE READYへ接続済み。** purchaseStatusは前提/正常データ/SPを検証し、optional構成だけではP/Tを拒否しない。効果条件とTaCZ専用契約は独立して維持する。

正本の既存効果要件は[Skill Tree §9–10](SKILL_TREE_SPEC.md#9-purification-mastery--浄化の極意)、[Compatibility §5/7/8](COMPATIBILITY_POLICY.md)、[TEST_PLAN §18/20/21](TEST_PLAN.md)。**2026-09-20の利用者回答で、v3最低対応表とoptional購入方針は[§12.5](#mastery-purchase-policy-decisions)にLOCKした。** Pは既存Trial4経路＋L2 6 ID＋FE2.7.20の指定boss由来secondary作用、TはL2 6 ID。広い将来互換目標は削除せず、全登録ID/全版を自動release必須にしない。

| 分類 | 今回の適用 | 購入解放 / releaseとの関係 |
|---|---|---|
| A：確定した完成条件 | LOCKした最低範囲の本人保護/恒久無効化と対象外保全。**PのFE2.7.20 UOM対応は必須、TimeStop #2指定受入完了・FE6実装/自動＋限定client完了（§14.57）**。効果/必要Adapter未ready時のSP保護、最終的な購入成功/拒否/正確支出の検証 | PはFE最低範囲の実装＋必要検証前にready化しない。Tも回答だけでgate解除しない。通常購入成功の実装/検証は§14.58で完了 |
| B：artifact不足 | FE最小構成の不足は§14.7で解消。Hyperlink/Fumetsuは最低範囲外の未受領 | FEの指定受入は§14.55–14.57で完了、広いTimeStop判定は別gate。受領/loader smokeを効果統合PASSにしない |
| C：追加互換候補 | 最低範囲外のL2 33 ID、他版/他攻撃、他Affix、Hyperlink/Fumetsu、将来のT他MOD拡張 | optional backlog。今回最低必須ではない。個別採用・仕様境界は後続承認で扱う |
| D：限定完了 | §8.10 Cube、§10.11 L2旧4 ID、§13.10/13.11新2 ID GUI/A/B/T/C・seal/通常保存終了、§11.7/11.8侵略者5ケース。自動L2 133/Cube49/Invader86/core60 | **残件へ戻さない**。今回再実行0。各当時Jar/限定範囲のPASSを保持 |
| E：採用後に必要となる個別判断 | 将来Dementor/Dispellを追加する際の貫通境界、T拡張時の既存外部state等 | 最低対応表とoptional方針は解決済み。FEの実物不足は解消済みで、指定受入も完了 |

未対応33 IDを全て必須にする判断待ちは解消し、今回の最低集合外として管理する。Pに追加されたFEの最低必須受入は§14.55–14.57で完了し、TimeStopを未確認のまま「全特殊作用対応完了」としない。具体的な静的確認済み経路と未確認境界は§14。

### 12.2 浄化の極意：効果別の現在表

未対応L2 33 IDは§12.5の最低範囲外。下表のEは将来そのIDを追加採用する際の効果境界であり、現在の購入方針の未決定ではない。

P=通常浄化/浄化の極意の両取得・ON、T=3skillの全取得・ON。下表の自動/実clientは既存証拠。L2行の版はHostility2.5.19＋Library2.5.3＋Complements2.6.1＋Tracker0.4.4、Trialは1.4.9。`未`はその未対応効果の試験未実施で、既存4種の未実施化ではない。

| 効果 / 対象版 | 実装・現Adapter | 自動試験 | 実client | 足りない処理 | artifact / 仕様判断 | 購入必須残件か / 将来対応の可能性 |
|---|---|---|---|---|---|---|
| Trial Damage Cube | D・TrialMonolithCompatibility / DamageCubeMixin、通常/force Soul加算2 site | 最終Jar49/49 | §8.10通常A/B/C・保存再読込PASS（当時8F4A…Jar） | 承認限定範囲なし | 不足なし / Q1 LOCK | 完了。force実画面等は既存自動と分離、追加必須化しない |
| Trial Invader flag解除 | D・TrialInvaderProtectionMixin、本人へのfalse要求だけ抑止 | Invader86内 | §11.7 F1/F2 PASS | 限定範囲なし | 不足なし / LOCK | 完了。false→true機能は追加しない |
| Trial Small Beam | D・TrialInvaderBeamSoulMixin＋native owner判定 | Invader86内 | 旧S1＋新S2 PASS | 限定範囲なし | 不足なし / LOCK | 完了。通常numericは通す |
| Trial Huge Beam | D・上記＋Huge専用numeric redirect | Invader86内 | 新H1 PASS | 限定範囲なし | 不足なし / Float.MAX_VALUE例外LOCK | 完了。Huge以外のnumeric遮断へ広げない |
| L2 poison | D・L2HostilityAdapter＋TargetEffectTraitMixin | 95/95内 | §10.11 A/B/T/C限定PASS | 限定範囲なし | 不足なし | 完了。tick後消去だけの代用ではない |
| L2 slowness | D・同上 | 95/95内 | 同上 | 同上 | 不足なし | 完了 |
| L2 corrosion | D・SlotIterateDamageTraitMixin / EquipmentDamageTraitMixin | 95/95内 | §10.11 wear100→110を分離観測 | 同上 | 不足なし | 完了。通常wear/不足時numericはPで保持 |
| L2 erosion | D・同上 | 95/95内 | §10.11 wear110→182を分離観測 | 同上 | 不足なし | 完了 |
| L2 weakness / wither | **D・LIMITED IMPLEMENTED**、既存TargetEffectのID判定/T pruneへ追加 | **133/133**の新契約（§13） | **旧§13.10 GUI/A/B/T＋§13.11 C/seal・通常保存終了 REAL CLIENT LIMITED PASS** | 承認限定確認は完了。実client再読込/範囲外は今回対象外で、133のnative保存回帰と分離 | artifact充足 / 採用済み2 IDの再判断不要。最低範囲/方針は§12.5でLOCK済み | 完了範囲を残件へ戻さない。旧95/95は旧契約のまま |
| FE2.7.20 UOM由来secondary | **D・限定実装済み**、TimeStop＋FE6 | §14.56 native自動70 unique、TimeStop既存自動 | §14.55指定受入・§14.57Dream代表限定PASS | 今回の最低受入残件なし。広いTimeStop gateは別 | artifact充足 / 最低必須LOCK | §14.58購入ready。Tへ追加しない |
| L2他の敵対ポーション：blindness / nausea / levitation / soul_burner / freezing / cursed | 未・明示allowlist外 | 未 | 未 | ID採用、T remove集合、個別付与/反射/再付与の検証 | 既存artifact充足 / C：最低範囲外 | 追加候補。今回最低範囲外 |
| L2 drain | 未・専用hookなし | 未 | 未 | 本人buff除去/有害効果延長の入口、numeric保持、T派生trait境界 | 充足 / E：対応範囲・T派生state | Skill Treeの将来目標を維持。今回最低範囲外 |
| L2 dispell | 未・専用hookなし | 未 | 未 | enchant一時無効化抑止、source状態との分離 | 充足 / E：BYPASS_MAGIC境界 | 同上。一般MobEffect解除ではない |
| L2 dementor | 未・専用hookなし | 未 | 未 | target別の防御貫通境界、Tでmob耐性も無効化 | 充足 / E：BYPASS_ARMORと正当numeric | 同上。数値ダメージを全取消しない |
| L2 killer_aura | 未（既存4種の末端P hookは別） | このtick経路は未 | 未 | trait副作用と独立hurtの切分け、T tick/副作用入口 | 充足 / C：最低範囲外 | 明記候補。独立numericをPで残す、既存4種の通常攻撃PASSをauraへ転記しない |
| L2 ragnarok | 未・専用hookなし | 未 | 未 | 遅延seal予約・実行と装備/Curios同期の保全 | 充足 / E：遅延処理/既存sealの境界 | 明記候補。装備復元・全state消去を推定しない |
| L2 pulling | 未・既存AdapterはServerPlayer限定 | 未 | 未 | 本人pushだけをserver/client双方で抑止、T同期後のclient作用停止 | 充足 / C：最低範囲外 | 明記候補。serverだけの実装では完成しない |
| L2 repelling | 未・同上 | 未 | 未 | pushの両side保護。mob側projectile防御は別 | 充足 / C：最低範囲外 | 同上。mob trait削除をP保護の代用にしない |
| L2 fiery / gravity / moonwalk / arena等 | 未・§12.3の別機構 | 未 | 未 | 点火、強制effect更新/移動、建築制限等を個別照合 | 充足 / C：最低範囲外。追加時は有益作用境界E | 「その他supported」に関係し得る候補。全registry必須とはしない |
| Hyperlinkのdirect kill / HP0 / death bypass等 | 未・専用Adapterなし | 未 | 未 | 実攻撃/通常numericの分離、対象本人だけの入口 | **B：承認Jar/版/依存未提供** / 今回最低範囲外、追加時の版指定が必要 | 仕様上の目標、現時点はartifact待ち。完了とみなさずrelease全体必須とも断定しない |
| Fumetsu Witherの同強制作用 | 未・専用Adapterなし | 未 | 未 | 同上。名称からclass/MOD IDを作らない | **B：該当攻撃を含む承認Jar/版未提供** / 今回最低範囲外 | Hyperlinkと別Jarが必要かもmetadata照合待ち。2本必須と推測しない |
| Trialの一般MobEffect敵対解除 | 実処理未特定・hookなし | その解除の試験なし | 全buff耐性PASSなし | 存在/経路が判明した場合だけ検討 | artifactは受領済み、未特定はartifact不足ではない | **C：条件付き調査backlog**。仮想の必須Adapterを作らない |
| その他MODの直接kill/HP0/強制死/death bypass/攻撃としてのremove | 特定経路以外なし。controllerは条件判定だけ | 全般未 | 全般未 | 具体的な所有者/攻撃siteと安全な介入可能性 | C/E：対象artifact/攻撃の選定 | 正本の「安全に介入可能な範囲」。世界中のkill/removeを止めるAにはしない |

### 12.3 L2の正式登録と未対応traitの機構分類

実物の`LHTraits`の登録呼出しは**39 ID（限定自動検証済み6（実clientも各限定範囲完了：§10.11/13.10/13.11）＋未対応33）**。`trait`はregistry、`potion_trait`はtagでありtrait IDではない。正式IDは `dispell` / `blindness` / `nausea` / `regenerate` / `teleport` / `counter_strike`（field/class名をIDに変換しない）。以下は全39の所属を列挙した**静的分類**で、全traitの実統合・全派生処理監査の完了ではない。

証拠：[現物hash](../build/verification/mastery-readiness-inventory-20260916-221334/audit/artifacts.json)、[全39 ID→field/class一覧](../build/verification/mastery-readiness-inventory-20260916-221334/audit/registry-inventory.json)、[registry bytecode](../build/verification/mastery-readiness-inventory-20260916-221334/audit/bytecode/dev.xkmc.l2hostility.init.registrate.LHTraits.txt)、[trait/caller読取](../build/verification/mastery-readiness-inventory-20260916-221334/audit/bytecode)。原物ZIPを読み必要class bytesを調査用に保存しjavapで逆アセンブル、class実行/ゲーム起動なし。過去§10.2/10.7の登録→付与→保存/codec/sync調査を再利用。新しい外部Jar展開をmodsへ配置していない。

クラスprefixは `dev.xkmc.l2hostility.content.traits.`。S=server効果、C=client独立作用、C表示=同期された値/演出（権威処理ではない）。P候補とT候補は**v3必須IDの決定ではない**。全行で永久性に単なるmap removeだけは不十分で、ID別marker・再付与対策・native sync/保存が共通で必要。

| 正式ID（全てl2hostility:） | 実class/method・本人直接作用 / numericの分離 | side / 機構 | P / T候補 | removeの限界・cleanup / 保存同期上の特殊性 | 4trait方式の再利用 |
|---|---|---|---|---|---|
| poison / slowness / corrosion / erosion | §10.2/10.8 | S、C同期 / effect・equipment | **D・P/T完了** | 独自mob modifierなし、既存marker/remove/sync/実作用guardで恒久性検証済み | 実装済み、再調査/再試験不要 |
| pulling | `legendary.PushPullTrait.tick`→対象ごとのpush。PullingTrait.getRange/getStrength。本人直接movement、独立hurtなし | **S＋C(local Player)** / movement | P本人push、T source作用 | 過去の速度を巻戻さず新pushを止める。server cap除去だけではclient旧capの作用窓を説明できない | marker/native sync基盤のみ再用。両sideの適切な本人判定・実作用guard追加が必要 |
| repelling | 同tick＋`RepellingTrait.onAttackedByOthers`のprojectile拒否 | **S＋C** push、server防御 / movement＋damage gate | P pushのみ候補、Tはtrait所有防御も候補 | pushとmob自身のprojectile耐性を分離。client cap更新/再付与直後を確認 | 同上。tick全体取消は他playerへ漏れるため不可 |
| dispell | `legendary.DispellTrait.postHurtImpl`→EnchantmentDisabler.disableEnchantment。`onCreateSource`でBYPASS_MAGIC、`onDamaged`でmobの魔法軽減 | S、C装備/tooltip同期 / equipment＋DamageSource | P新規装備作用、貫通部分はE。T全trait作用 | `l2hostility_enchantment`/originalEnchantments/startTimeを装備に保存、tickStackが復元。removeでは既に無効化した装備は戻らない。元の復元を壊さない | marker基盤は可。装備site・source作成・耐性・再付与guardは新規、全effect取消は不可 |
| dementor | `legendary.DementorTrait.onCreateSource`でBYPASS_ARMOR、`modifyBonusDamage`のfactor、`onDamaged`で非magicの非線形軽減。MobEffect解除なし | S / DamageSource・numeric耐性 | P境界E、T source trait候補 | 数値量と貫通属性を分離。Tracker0.4.4 CreateSourceEventはattacker/directを持つが被害者fieldなし。そこで全player分を一律変更不可 | marker基盤のみ。targetが判る攻撃文脈・source/耐性別guardが必要 |
| killer_aura | `legendary.KillerAuraTrait.tick`→各受け手TraitEffectCache/traitEvent(postHurtPlayer)→別の`hurt(killer_aura,damage*rank)`。modifyBonusDamageも別 | S効果、C粒子 / 副作用＋numeric | P副作用のみ候補（**numeric維持**）、T tickとtrait補正 | map除去と再付与前guard、元粒子packetとcap同期の役割を区別。既存4種hookがあってもaura全体検証済みではない | 基盤可、TargetEffectだけでは全postHurt経路を覆わない |
| ragnarok | `legendary.RagnarokTrait.postHurtImpl`→schedule→sealItems、CurioCompat.getItemAccess→EntitySlotAccess.modify→SealedItem.sealItem | S予約/装備変更、C inventory / equipment | P新規seal、T source作用 | sealTime/sealedItem入りの別stackへ置換。受け手だけをcaptureした予約はmap除去後にも実行され得る。旧sealの自動治療を推定しない | 新規予約/実行時ownership文脈・失効条件の設計が必要。map除去のみ不可 |
| speedy | `base.AttributeTrait.initialize`→TraitManager.addAttribute、`speedy`の移動速度MULTIPLY_TOTAL | S初期化、C属性同期 / source attribute | Pの本人直接作用ではない。T候補 | **modifier cleanup必要**。native UUID算法でspeedyだけ除去、再initialize前/後と保存復元を検証 | 現4 IDのcleanupなし方式はそのまま不可 |
| tank | 同initialize、`tank_health` MULTIPLY_TOTAL、`tank_armor` ADDITION、`tank_tough` ADDITION | S、C属性同期 / source HP・armor・toughness | P直接対象でない。T候補 | **3つの所有modifierだけcleanup**。hostility_health/他MOD属性は保全。maxHP縮小時の現在HPの扱いはE、回復/基礎HP変更を推定しない | 同上、属性専用の冪等cleanup・native属性syncが必要 |
| drain | `highlevel.DrainTrait.onHurtTarget`の有害effect数によるnumeric倍率、postHurtImplでbeneficial解除＋EffectBooster.boostTrait。postInitでpotion tagから別trait追加 | S、C effect同期 / MobEffect＋numeric＋派生trait | P新規解除/延長を防ぎnumeric維持。T候補 | DRAIN_IGNIRE tag等の元除外を維持。既に追加されたpotion traitに由来markerなし、Tで勝手に全部消さない。numeric補正はP/Tで扱いが異なる | 末端/再付与guard追加。派生traitをどこまで対象化するかE |
| weakness | `base.TargetEffectTrait.postHurtImpl`、LHTraits factory WEAKNESS、duration=weakTime、amplifier=rank−1 | S付与、C effect/属性表示 / MobEffect | P/T採用済み・§13自動PASS | mob独自modifierなし。既に受けた効果の治療とは別。通常attack numericは残す | **§13で拡張完了**：ID判定/remove集合＋既存2site/再付与経路を自動検証、旧GUI/A/B/T＋§13.11 C/sealの実clientも限定完了 |
| wither | 同クラス、WITHER、duration=witherTime、amplifier=rank−1 | S付与/後続DOT、C表示 / MobEffect | P新付与/T採用済み・§13自動PASS | 新規wither由来DOTを防ぐことと、元攻撃numeric取消を混同しない。既存witherを消さない | 同上 |
| blindness | 同クラス、BLINDNESS、blindTime*rank | S付与、C視覚 / MobEffect | P/T候補 | client描画は通常effect同期。本人全effectを消すcleanupなし | 同じ入口は再用候補、視覚確認は追加ID採用後 |
| nausea | 同クラス、CONFUSION、confusionTime*rank | S付与、C視覚 / MobEffect | P/T候補 | IDはnausea。既存他由来effectの非消去 | 同上 |
| levitation | 同クラス、LEVITATION、levitationTime*rank | S付与、通常effectのS/C移動 / MobEffect | P/T候補 | native新規付与防止。既存浮遊・速度をsetterで補正しない | 入口再用候補、落下等の安全確認は採用時 |
| soul_burner | 同クラス、LCEffects.FLAME、soulBurnerTime・rank−1 | S付与、外部effectのsideは追加照合 / MobEffect | P/T候補 | EffectUtilのForceEffect分岐/外部effect内作用は未統合、Trial SoulDamageと別物 | 同入口候補、外部effect作用の追加確認必要 |
| freezing | 同クラス、LCEffects.ICE、freezingTime*rank | 同上 / 外部MobEffect | P/T候補 | 既存ICEの修復/解除を推定しない | 同上 |
| cursed | 同クラス、LCEffects.CURSE、curseTime*rank | 同上 / 外部MobEffect | P/T候補 | CURSEの回復等への作用を名前で確定しない | 同上 |
| gravity | `common.GravityTrait`はAuraEffectTrait継承、tickで効果refresh。onDamagedで攻撃者へ下向きpush、ServerPlayerの同期flag | S effect/push、C通常同期/粒子 / effect＋movement | P本人作用/T候補 | aura refresh FORCEと反撃pushは別site。既存重力effect/modifierの由来を区別 | 既存TargetEffect hookでは不足、aura/反撃追加 |
| moonwalk | `common.AuraEffectTrait.tick`、登録LHEffects.MOONWALKをrefresh | S付与、C effect/粒子 / 重力effect | Pは敵対/有益境界E、T候補 | 低重力を全対象に一律有害と決めない。既に付いたeffectの所有者問題 | aura専用経路が必要 |
| arena | `highlevel.ArenaTrait`→AuraEffectTrait、ANTIBUILD。onAttackedByOthers/onDamagedは受け手effect等でmob耐性 | S、C effect/表示 / 建築制限＋防御 | P本人付与/T候補 | 建築eventの下流・元除去/期限は別、mob防御だけをPで消さない | auraと防御の別guard。全体未統合 |
| fiery | `common.FieryTrait.onHurtTarget/onAttackedByOthers`がDamageSourceのdirectEntityへ点火、SelfEffectTrait継承の耐火＋fire attack拒否。説明文だけでattack targetへ付くと仮定しない | S戦闘/自己effect、C火表示 / fire＋source耐性 | P点火/T候補 | 実fire damage/元numericと新点火を分離。既存火や他由来耐火を消さない | TargetEffect方式だけでは不可 |
| protection | `base.SelfEffectTrait.tick`で自分へRESISTANCE refresh | S、C effect / source buff | P本人作用でない、T候補 | remove後に既存effectが残る。発生元識別なしの耐性effect全消去は不可 | 既存4種方式だけでは即時無効化を保証できない |
| invisible | `common.InvisibleTrait`→SelfEffectTraitで不可視、postInitで装備へSHULKER_ARMOR enchant追加 | S、C表示 / source buff・equipment | P直接対象でない、T候補 | 不可視残存＋装備に追加したenchantの所有権、元enchant保全が必要 | 専用cleanup/論理抑止の検討が必要 |
| regenerate | `common.RegenTrait.tick`で周期的heal | S / source HP | P直接対象でない、T候補 | 過去healを巻戻さない。今後のheal停止・再付与guard | 基盤再用＋tick guard。回復済HP削減なし |
| adaptive | `common.AdaptingTrait.onDamaged`→cap dataのmemory/adaptionとdamage modifier | S / source耐性・保存data | P直接対象でない、T候補 | map removeはdataを消さない。再付与でも効かせないguard、他cap data保全 | 基盤再用＋耐性入口、専用data cleanupの要否を分離 |
| reflect | `common.ReflectTrait.onHurtByOthers`→schedule→別hurt、modifyBonusDamage | S / numeric反射 | Pは正当numericを残す。T候補 | 予約済みhurt/source文脈と新しい発動停止は別 | tickではなくevent/予約guard必要 |
| shulker / grenade | `common.ShulkerTrait.tick`→HostilityBullet生成、Data.uuid/tickCount。grenadeは同class別BulletType | S生成、C飛翔/表示 / projectile・numeric | Pは弾の非numeric作用特定後、T候補 | 既発射entityの扱いをmap除去で解決したとしない。Bullet下流全動作は今回未調査 | 専用追跡/作用境界必要、次単位に含めない |
| teleport | `goals.EnderTrait.tick/onAttackedByOthers`でsource自身のteleport/回避 | S、C位置同期 / source移動・防御 | P直接作用でない、T候補 | source位置を戻さずtrait由来の将来移動だけ止める。通常AI/teleportは保全 | 基盤＋tick/防御guard、全AI削除不可 |
| counter_strike | `goals.CounterStrikeTrait.onHurtByOthers/tick`、Data.cooldown/duration/strikeIdと反撃target状態 | native cap tick経由、side完全追跡は残る / AI・保存data | Pの普通攻撃は残す、T候補 | 既設定target/goalの所有権、予約された反撃との境界 | 4種方式だけでは不十分、通常AI全消去不可 |
| growth | `highlevel.GrowthTrait.tick/postInit/onDeath`、Slime size変更/REGEN追加、death経路 | native cap tick＋server death / size・attribute・派生trait | P直接作用でない、T候補 | 肥大済size/属性/派生REGENの由来、死亡時処理を別に確認。全side下流は未追跡 | 専用境界必要、Slimeを初期sizeへ戻す仕様を作らない |
| split | `highlevel.SplitTrait.onDeath/add/inherited`、新entity生成・copyCap・半level等 | S、C spawn / death・別entity | P直接作用でない、T候補 | 新UUID/子への恒久marker継承は別仕様。既存子を削除しない | 同mob限定markerを無条件継承しない。追加判断が必要 |
| undying | `legendary.UndyingTrait.onDeath`でheal/setHealth・death取消、SPLIT分岐/元packet | S、C演出 / source死亡防止 | P本人作用でない、T候補 | supported trait無効化とBreak Realmの攻撃側bypassは別。death/drop flow保全 | T採用はE、Break Realmの開始条件を解除しない |
| reprint | `highlevel.ReprintTrait.onHurtTarget`→ReprintHandler、source装備へenchant複写/VOID_TOUCH、別numeric倍率 | S、C equipment / source装備・numeric | P数値攻撃は維持、複写はE。T候補 | 既複写enchant/元装備の所有権と保存が必要。removeでは複写済装備が戻らない | 装備専用cleanup/論理抑止設計が必要 |
| master | `legendary.MasterTrait.getConfig`、`MobTraitCap.tick`がMASTERを見てMasterData.tick、client MASTERSへ登録 | S＋C表示 / minion関係・cap | P直接作用未特定、T候補 | MasterData/MinionDataと既召喚子の所有関係は未監査。map除去だけの全cleanup保証なし | 基盤のみ。召喚済mob削除やcap全消去を提案しない |

**重点境界：** DispellのEnchantmentDisablerは装備NBTを変更して後に復元する。Ragnarokは装備自体をsealed stackへ置換する。Drainは実beneficial解除を持つが、これをTrialの架空解除hookの根拠にはしない。Dementor/Dispellのsource作成段階には被害者がいないため、既存ServerPlayer述語をその場で呼べば完了とは言えない。Speedy/TankのUUIDは§10.7で照合した`MathHelper.getUUIDFromString(name)`で、単なるUUID.nameUUIDFromBytesではない。`removeTrait`はmap削除/queued rank0でありmodifier/装備/effectの後処理を呼ばない。

### 12.4 真実の極意：完成済み基盤と対象追加時の仕事

| 要件 | 現在の証拠/判定 | 残る範囲・購入readinessとの関係 |
|---|---|---|
| ±75 inclusive XYZ、spawn/join・進入/teleport・player移動 | **D・6 ID自動**。133内で±75/75.001全12境界（旧95の4 ID結果も保持）、player移動/重複範囲、通常保存join。実clientは範囲内適用/X90退出を限定確認 | 全境界を実画面で再演する条件を追加しない |
| 3skill取得/ON、正常canonical、toggle非連動 | **D**。6 ID全8組合せ等の自動133＋旧4 ID実GUI A/B/T/C＋新2 IDの旧GUI/A/B/T・§13.11 C | 条件の再設計/再質問なし |
| ID別恒久marker、native remove/native sync、再付与抑止 | **D・6 ID自動**。init/copyFrom/pending/tickと作用前guard、反復安全、6 IDの各限定実client空trait cap・新2 ID Cで3OFF作用0 | 新IDはid/prune集合への明示採用とその作用入口を接続する。markerだけでは追加対象完成にならない |
| 対象外保全 | **D**。source level/HP/foreign modifier保持。旧95はweakness、新133はlevitationを対象外対照として保全 | 旧契約を遡及変更せず、新契約の対象外assert/保存後native作用を維持 |
| 属性modifier cleanup | 6 IDは独自mob modifierなしなので不足ではない | Speedy/Tank等を**対象に追加する時だけ**新たに必要。基本属性やhostility_healthを全削除しない |
| client独立作用の無効化 | 6 IDのserver入口・native capを自動確認。実clientは旧4 IDに加えて新2 IDもGUI/server/client・C作用0を確認。一瞬のB icon目視PASSではない | Pulling/Repelling追加なら両sideのpush抑止と同期中の窓が必要。4 IDの同期PASSを未実施へ戻さない |
| 保存/再読込・OFF/範囲外恒久性 | **D・6 ID自動**。133内のnative chunk unload/reload（旧95も保持）、§10.11の実client同world通常再読込/再追跡・全OFF保持 | 別JVM/他版/派生別UUIDは別範囲。今回の2 IDは必要な自動回帰を実施。既存実clientは再試験なし |
| 多数entity性能 | 5tick移動/20tick静止query・mob20tick分散・毎tick全領域scanなしは実コード確認。大規模候補数/TPS/長時間は**未測定** | Skill Tree性能要件は維持。Test §21のthousandsは**if feasible**。必須台数/TPS閾値未LOCKを新Aへしない。ID追加時だけの問題ではなく共通性能の残確認 |
| 購入可能な状態への接続 | **D・§14.58でPURCHASE READY**。registered packet通常購入・exact debit・拒否時不変・canonical syncを確認 | 今回の受入完了。取得fixtureを購入成功扱いせず実際のserver transactionを通した |

<a id="mastery-purchase-policy-decisions"></a>

### 12.5 購入readinessの最低対応範囲・optional購入方針（2026-09-20 19:25 JST・RESOLVED / LOCKED）

**利用者回答：問1=B、問2=P=A / T=A。** 本節を今回の決定の参照先とする。仕様LOCKと実装ready・通常購入成功・release承認は別。9/20時点は固定pending・購入停止を維持した履歴。現在は§14.58で正式ready化しSP保護を維持。9/20の決定時はproduction/test/build/Jar/Config変更・起動0。後続実装結果は下記最低対応表と§14.55–14.58を参照。

#### 12.5.1 LOCKした最低対応表と実装状態

P＝浄化の極意、T＝真実の極意。**P最低範囲はTrial下記4経路＋L2 2.5.19の6 ID＋Fantasy Ending2.7.20の指定boss由来secondary作用。T最低範囲はL2 6 ID。** Pは本人への作用防止、Tは恒久strip/nullifyで、同じ対応集合にはしない。Tは将来L2以外へ拡張予定だが、今回FEへTを追加しない。

| MOD ID / version | 正式ID・限定効果 | P | T | 完了証拠 |
|---|---|---|---|---|
| `the_trial_monolith` **1.4.9** | entity `the_trial_monolith:damage_cube`：native activateの通常/force Soul加算抑止、独立cube_attack numeric保持 | 実装済み | 対応なし | Cube49/49、§8.10通常A/B/C・保存再読込限定PASS |
| 同上 | entity `the_trial_monolith:invader_monolith`：tickのSoul Protection=false要求だけ抑止 | 実装済み | 対応なし | Invader86/86内、F1/F2限定PASS。false→trueにしない。MobEffectではない |
| 同上 | entity `the_trial_monolith:small_beam`：同levelの正式Invader owner時の通常/force Soul加算抑止 | 実装済み | 対応なし | Invader86内、S1/S2限定PASS。独立numeric保持 |
| 同上 | entity `the_trial_monolith:huge_beam`：同owner条件のSoul加算抑止＋LOCK済みFloat.MAX_VALUE hurt例外 | 実装済み | 対応なし | Invader86内、H1限定PASS。他owner/攻撃へ拡張しない |
| `l2hostility` **2.5.19** | trait `l2hostility:poison` | 新規本人付与防止 | 恒久無効化 | 133/133の6 ID契約、旧4 IDの§10.11実client限定PASS |
| 同上 | trait `l2hostility:slowness` | 同上 | 同上 | 同上 |
| 同上 | trait `l2hostility:corrosion` | 追加耐久消耗防止 | 同上 | 同上。通常wear/独立numeric保持 |
| 同上 | trait `l2hostility:erosion` | 同上 | 同上 | 同上 |
| 同上 | trait `l2hostility:weakness` | 新規本人付与防止 | 同上 | 133/133＋旧§13.10 GUI/A/B/T・§13.11 C/seal/通常保存終了限定PASS |
| 同上 | trait `l2hostility:wither` | 新規付与とそこから生じるDOT防止 | 同上 | 同上。元攻撃numeric保持、既存witherを治療しない |
| **fantasy_ending 2.7.20** | entity **fantasy_ending:ultimate_order_manager**（終焉の守護者）本人/正式owner付き攻撃Entity由来の敵対secondary作用一式 | **最低必須LOCK・限定実装/自動完了** | **今回最低範囲外** | TimeStop §14.55指定受入完了、FE6 §14.56焦点自動・§14.57限定実client完了。外部実物不足なし |

Trial entity IDは[実物registry読取](../build/verification/trial-invader-20260915-203400/audit/TrialMonolithEntities.txt)、L2は[現Adapter](../src/main/java/com/leva/foodhealing/compat/l2hostility/L2HostilityAdapter.java)・[登録39 ID](../build/verification/mastery-readiness-inventory-20260916-221334/audit/registry-inventory.json)と照合した。SoulDamage/SoulProtectionに架空のMobEffect IDを付けない。

- 基準はMC1.20.1 / Forge47.4.0。L2の実コードgateは **Hostility2.5.19 / Library2.5.3 / Complements2.6.1 / Tracker0.4.4の完全一致**、Trialは **the_trial_monolith 1.4.9完全一致**。実検証Curios5.12.0+1.20.1/Patchouli1.20.1-84-FORGEを保持するが、この2版をL2の4版gateに含むと誤記しない。同梱Trackerの外側への重複配置なし。
- **9/20照合時の過去製品（現行JarはStatus冒頭）**は **229,494 bytes /150 entries / SHA-256 `5C1A716EEDE7AEB4449B36EEFFBDDA156176B16B60A1676229224AA3390DB327`**。[現物読取](../build/verification/mastery-purchase-readiness-readonly-20260920-175400/audit/read-only-inputs.json)。codeのversion判定は同版別byte artifact全ての実証ではない。Cube/旧L2/Invader実clientは各当時JarのPASSを保持し、最新Jarへ新規実client PASSを転記しない。最新JarのL2 133/Cube49/Invader86自動結果と新2 ID実client完了も維持。

#### 12.5.2 現行購入判定（2026-09-29 §14.58でready化）

[FoodHealingSkills](../src/main/java/com/leva/foodhealing/FoodHealingSkills.java)の固定pendingからP/Tだけを解除。Flight/Break Realmは維持。正常canonical・取得前提・P100/T500を検証後、成功時だけ正確支出・Lv1取得。親OFFも購入可能、親toggle不変。P/Tに導入MOD・対応版数・食義Lvの追加購入条件なし。異常取得ID/範囲外LvはSTALE_REQUESTで無変更拒否、pending/future schemaは既存拒否、使用済SP Long overflowもpreview/server一致で拒否する。

[FoodHealingScreen](../src/main/java/com/leva/foodhealing/client/FoodHealingScreen.java)は同じread-only purchaseStatusを使用。登録済み購入packetのdecode/authoritative transaction/canonical syncを§14.58で検証。既存TaCZ専用optional/readiness契約は不変。効果発動条件・Adapter version gateとは独立。新しい情報UIは作成していない。

#### 12.5.3 optional購入方針 — P=A / T=A LOCK

下表は**§14.58でready化した現在**、前提取得・SP・正常canonical等が揃った場合。購入をMOD構成だけで拒否しない。未対応Adapterのeffect適用は引き続き無効。Loaderの必須依存欠落による起動不能を購入方針で救済する意味ではない。

| MOD構成 | P購入 | T購入 | 効果・既存データ |
|---|---|---|---|
| 対応MOD未導入 | 可 | 可 | 有効先0。無条件外部classロードなし |
| 採用MODの未対応版しかない | 可 | 可 | 未対応版には介入しない |
| 対応版あり | 可 | 可 | 各極意が採用した対応先だけ作用 |
| 対応版＋別の採用MODの未対応版混在 | 可 | 可 | 対応側だけ作用、未対応側の存在を購入拒否理由にしない |

例えばTrial1.4.9だけならPにはTrialの効果先、Tには効果先0だが両方購入可。Trial対応＋L2未対応でも同じ。L2対応＋Trial未対応ならP/TともL2だけ作用する。FE対応の有無はTの効果先を増やさない。TのP取得前提・500SP、Pの通常浄化取得前提・100SPは不変で、購入時ON条件を追加しない。

既取得者の没収・自動返金・toggle書換えはしない。混在の購入可否LOCKは、その構成のロード/全gameplay互換PASSではない。L2の未対応33 IDが存在しても、対応版Adapterの6 IDを無効化する理由にはしない。

#### 12.5.4 最低範囲外・artifact待ち・releaseとの分離

§12.3の登録39 IDのうち採用6以外の33は**今回LOCKしたv3購入最低範囲外のoptional backlog**。既存artifactは充足し、未対応33＝artifact不足33/release blocker33ではない。将来互換目標は維持する。

| 群 | ID（全てl2hostility:、重複なし、計33） | 追加採用時の課題 |
|---|---|---|
| 正本名指し7 | `drain`, `dementor`, `dispell`, `killer_aura`, `ragnarok`, `pulling`, `repelling` | 貫通/numeric境界、遅延state、S/C push等。今は必須追加しない |
| 敵対ポーション6 | `blindness`, `nausea`, `levitation`, `soul_burner`, `freezing`, `cursed` | 同じ入口でも個別作用/再付与検証が必要 |
| 本人作用/境界追加5 | `fiery`, `gravity`, `moonwalk`, `arena`, `reprint` | numeric/有益作用/装備所有権との境界 |
| 主にsource能力15 | `tank`, `speedy`, `protection`, `invisible`, `regenerate`, `adaptive`, `reflect`, `shulker`, `grenade`, `growth`, `split`, `counter_strike`, `undying`, `teleport`, `master` | T追加なら属性modifier/既存state/派生entity等の専用対処。Pへmob能力消去を流用しない |

| 別枠 | 現在の扱い |
|---|---|
| **Fantasy Ending2.7.20 UOM** | **P最低必須・限定実装済み**。TimeStop #2は§14.55指定受入完了、FE6は§14.56自動・§14.57限定client完了。広いTimeStop所有関係gateを別に維持。Tには追加しない |
| Hyperlink / Fumetsu | Compatibility §8/Test §20の将来目標を維持するが今回最低必須外。承認artifact/版/必要依存未提供・専用Adapter未実装。Fumetsuが別Jarかも未確定で2本必須と推測しない |
| 他L2版/他Trial攻撃・版/他boss・MOD | optional拡張候補。全MOD/全版互換を自動購入条件にしない |
| 一般MobEffect敵対解除 | Trial1.4.9の該当実処理未特定。Soul Protection flagとは別。仮想hookや必須試験を作らない |
| 性能・同時構成 | 大規模TPS/長時間・L2＋Trial同時構成は未実測。既存性能安全要件を維持し、全網羅を新必須にしない。購入方針の境界確認とは別 |
| v3全体release | [Statusのrelease分類](CODEX_STATUS.md#残release条件の分類と実行単位)・AUDIT §Fを維持。RC=NO。Flight ownership、REAL2CLIENT、Break Realm、Bulwark、試作型機関弩、FOURTH BOOTは個別gateのまま、P/T購入条件へ混ぜない |

#### 12.5.5 現在の購入受入と次の単位

問1/問2・OPEN-02/03/04・Q1/Q2・Truth3取得/3ON・100/500SP・Huge例外はLOCK維持。§14.58で通常購入接続と今回指定の自動受入を完了し、#4 COMPLETE／残5。現行queue #5は§14.60で49+2実装/主検証完了・Food Core補足artifact待ち、残5。Trial/L2/FEの過去限定PASSを再試験せず保持。RC=NO、REAL2CLIENT=BLOCKED、他個別gateと可逆クラフト既知許容仕様を維持。

### 12.6 2026-09-16の推薦単位（後続の採用・実装状態は§13）

**後続状態：2026-09-17の添付依頼とチャット本文で採用・実行を承認。§13で新2 IDの限定自動統合・指定回帰・新Jar確認まで完了。以下は2026-09-16の推薦根拠として保持し、実client/購入解放は別承認とする。**

**L2 2.5.19のTargetEffect 2 ID：`l2hostility:weakness` / `l2hostility:wither`についてP本人新規付与防止＋T恒久無効化を追加する単位を推薦する。** 同じクラス・元2 addEffect site・modifier cleanup不要という小さなまとまりで、正本の敵対ポーション要件の対応を2件増やせる。購入ready全体の成立は別で、これだけでgateを開けない。

- artifact：既存L2/Library/Complements/Tracker0.4.4/Curios/Patchouli/Forge47.4.0のみ。新download/本体再提供不要。
- 仕様判断：P両取得/ON・T3つ取得/ON、本人限定、numeric保持、既存効果の非治療、同mob恒久性は既定。**当時必要だったこの2 IDの採用承認は充足済み（§13）**。Dementor等の別境界を先に決める必要はない。v3最終対応表Eをこの推薦で確定しない。
- 予定production箇所：`compat/l2hostility/L2HostilityAdapter.java`のidentity→IDとprune集合。既存`TargetEffectTraitMixin`の2site条件は再利用候補、`MobTraitCapMixin`/TruthMasteryController/保存schema/packet/購入gateの変更は原則不要。新しい架空Mixin targetは不要。
- 必要な自動確認：実登録/native攻撃でP ONと各OFF・未取得/pending/不正・別player/反射、通常浄化のtick消去前の付与観測、元numericとRNG/既存有益・他由来有害効果保全。witherは元hitと新規DOTを分離。T全OFF対照で実作用消失、再付与・範囲・native sync/保存保持。hook数/optional不在/最終Jar非混入を確認し、変更時に必要なbuild/unit/check・既存回帰を行う（今回は実行しない）。
- 既存95の対象外weakness対照は**旧4 ID契約の成功履歴として保持**。将来の拡張ではweaknessの意図した除去を新期待値にし、対象外保全ケースは未採用`levitation`等へ移して試験目的/件数を落とさない。期待値を弱めて通す変更ではなく、明示された対応範囲の変更を記録する。
- 実client：自動後に新2 IDのGUI/効果同期を限定確認する価値がある。事前observerで付与直後を測り、厳密tickを人間へ要求しない。既存4種A/B/T/C、Trialの再演を前提にしない。**全既存95の実画面再現やwither死亡試験は不要**。今回helper/試験手順の詳細再作成・起動は行わない。
- readinessへの寄与：現在allowlist外で通っている2つの敵対ポーションについて実装/証拠を追加できる。残る購入blockerは§12.5。次回はこの調査の読み直しからではなく、採用承認された2 IDの実装差分から開始できる。

2026-09-16棚卸し時の変更は本節とStatus/Testの現在参照、読取audit/保全のみ。production/test/build.gradle/Jar/Config・購入gate/SP保護不変。ゲーム/server/試験/compile/新helper/外部download/Security scanなし。両極意IMPLEMENTATION_PENDING、RC=NO、REAL2CLIENT=BLOCKED、個別開始条件と食料生産の極意の可逆クラフト増加の既知許容仕様を維持する。


<a id="l2-weakness-wither-implementation"></a>

## 13. L2 weakness / wither追加 — 限定実装・自動統合完了（2026-09-17 20:34 JST）

### 13.1 承認・対象と実物

[添付依頼](../build/verification/l2-weakness-wither-20260917-193800/audit/user-request.txt)に続き、利用者がチャット本文で既存Gradle8.1.1/cache/native DLLへの権限付きoffline実行を明示承認。新規隔離server/world・自動fixtureのみ使用し、client/Prism・旧world・購入解放は実行しない。

承認Hostility2.5.19/Library2.5.3/Complements2.6.1/Curios5.12.0+1.20.1/Patchouli1.20.1-84-FORGEを使用。最終L2 receiptで**Tracker0.4.4・Forge47.4.0**の実採用を確認。同梱物は外側へ重複配置なし。[現物hash/metadata](../build/verification/l2-weakness-wither-20260917-193800/audit/inputs.json)、[実ロードと全結果](../build/verification/l2-weakness-wither-20260917-193800/final-l2/l2-result.json)。Trial1.4.9原物も不変。vanilla/TaCZ GameTestは既存dev基準Forge47.2.0、実配布Jar L2/Cube/InvaderはForge47.4.0として区別する。

### 13.2 実装境界・変更ファイル

- 製品変更は`src/main/java/com/leva/foodhealing/compat/l2hostility/L2HostilityAdapter.java`のidentity→IDとprune集合だけ。現行対象は **poison / slowness / corrosion / erosion / weakness / wither** の6 ID。
- 実登録`LHTraits.WEAKNESS/WITHER`は`TargetEffectTrait.postHurtImpl`→通常受け手/反射先の`EffectUtil.addEffect`各1箇所。weakTime/witherTime・amplifier=rank−1。TargetEffectTraitに独自initializeなし、基底MobTrait.initialize/postInitはno-opで、source modifier cleanup不要。Pは新規付与だけを止め、既存effectや通常numericを変更しない。
- Tは既存UUID拘束・ID別marker、native remove/sync、init/copyFrom/clearPending/作用前guard・範囲/20tick補完を再用。schema/packet/版判定plugin/他trait/購入/SP/build.gradleは変更なし。[コード差分・境界](../build/verification/l2-weakness-wither-20260917-193800/audit/source-boundary.json)、[diff](../build/verification/l2-weakness-wither-20260917-193800/audit/source-review.diff)。
- 自動fixtureは`src/l2HostilityTest/java/com/leva/foodhealing/verification/L2HostilityVerification.java`を拡張。新しい独立fixture Jarを新serverにのみ配置。実client helperなし。
- **旧95/95のweakness対象外保全は旧4 ID契約の成功履歴のまま**。新契約は実登録`levitation`（TargetEffect・levitationTime×rank）へ対象外対照を置換し、native付与・保存後作用と対象外data保持を継続。ケース削除/期待値緩和なし。

### 13.3 最終自動結果と観測

| 検証 | 実測結果・範囲 | 証拠 |
|---|---|---|
| build / foodHealingUnitTest / check | **PASS**。compileJava/reobfJar・独立fixture compile/reobf成功。P150、Truth53、前提142、SW47、data境界5000、Ammo等の既存unitを保持 | [build-03.log](../build/verification/l2-weakness-wither-20260917-193800/audit/build-03.log) |
| L2 expanded | **133/133 PASS**。焦点runも133/133、最終runは下記配布Jarの同hash | [final-l2 receipt](../build/verification/l2-weakness-wither-20260917-193800/final-l2/l2-result.json) |
| vanilla GameTest | **60/60 PASS / Gradle成功**、L2不在 | [log](../build/verification/l2-weakness-wither-20260917-193800/audit/final-vanilla-offline.log) |
| approved TaCZ GameTest | **60/60 PASS / Gradle成功**、TaCZ1.1.7-hotfix2、L2不在 | [log](../build/verification/l2-weakness-wither-20260917-193800/audit/final-tacz-offline.log) |
| Trial Cube | **49/49 PASS**、最終配布Jar直接 | [receipt](../build/verification/l2-weakness-wither-20260917-193800/final-cube/trial-result.json) |
| Trial Invader | **86/86 PASS**、最終配布Jar直接 | [receipt](../build/verification/l2-weakness-wither-20260917-193800/final-invader/invader-result.json) |

133件は旧95の試験目的を保持＋38：P新2×9=18、既存effect強弱保全2×2=4、通常浄化前後2、反射新2、T無資格新2×6=12。全内訳はP54・装備20・既存/他player/対象外1・新effect保全/通常浄化6・反射3・購入拒否2・T43・native sync/player保存/chunk unload/reload各1。[集計](../build/verification/l2-weakness-wither-20260917-193800/audit/test-summary.json)。6 ID集合を使うT範囲/同期/恒久性ケースも拡張したが、水増しして別ケース数に数えない。

- P新2 ID：保護ONは即時effectなし・Added0、parent/M OFF等の負対照はAdded1。元mob攻撃のnumericイベントは1回・source `mob`、保護による数値取消なし。native Zombie.doHurtTarget→L2 eventを使い、通常浄化tick前に測定。M OFFでは付与を観測してから通常PlayerTick ENDで消去されることを別確認。
- 既存の同種weak/strong effect、他sourceのblindness、beneficial regenerationのNBTを変更しない。他playerは1回付与、反射ringからPillagerへのnative付与も各1回。witherの長時間DOT/自然戦闘/死亡を測定範囲に加えない。
- T全8toggle、未取得/overlevel/pending/future-schema、6 ID marker/native remove、同tick実作用guard、±75/75.001 XYZ、player進入/重複、init/copyFrom/clearPending、OFF/範囲外・別Mob/UUID非漏洩を確認。native空map packetのcodec/mirror、通常player保存、実chunk unload→別entity instanceの再ロード、6 markerとlevitation/foreign属性等の保持を確認。EmbeddedChannel/mirrorは実client/TCPの代替ではない。
- Mixin exportのTargetEffect2、Cube2、Invader flag1/Small2/Huge3 call-siteとpluginの早期gameplay参照なしを確認。[実ロードhook照合](../build/verification/l2-weakness-wither-20260917-193800/audit/mixin-validation.json)。

### 13.4 途中失敗・環境修正・正常終了

19:48時点はコードのみ反映：Gradle8.8/cache選択のfoojay解決失敗→既存8.1.1制限環境のnative DLL失敗→権限審査拒否2回。利用者のチャット本文での承認後、同じ既存offline手順でbuild成功。[旧拒否記録](../build/verification/l2-weakness-wither-20260917-193800/audit/approval-review-rejection.json)、build-01/02.logを保持。製品/fixtureのcompile失敗やgameplay期待値FAILは今回なし。

初回vanillaは60件自体PASS後、外部取得をしないためloopback拒否へ向けたAuthlib公開鍵参照がERRORとなり、既存GradleのERROR検査がFAIL。[初回log](../build/verification/l2-weakness-wither-20260917-193800/audit/final-vanilla.log)。チェックを緩めず、新規検証JVMだけのlocalhost HTTP応答`/publickeys`（空のprofilePropertyKeys/playerCertificateKeys）で完結させ、新しいworldで再実行して成功。他pathは503・認証情報なし・外部取得なし。これは認証/TCP/実2-clientの試験ではない。

Forge更新確認用の最初のJVM propertyは実効せず、初回L2でloopback proxy接続拒否WARNを記録。その後は**新しい試験rootのloader config/fml.tomlだけ**でversionCheck=false。製品Config/既存instanceは不変。HTTP(S)もloopback proxyに限定。[実行環境設定](../build/verification/l2-weakness-wither-20260917-193800/audit/offline-runtime-policy.json)。

全試験serverで通常save/stop・全dimension保存とexit0を確認。最終L2 PID28912/Cube30500/Invader3368は終了。vanilla/TaCZもGameTest shutdown・Gradle終了確認、今回runに一致するJava processは最終確認0。[全run集計](../build/verification/l2-weakness-wither-20260917-193800/audit/test-summary.json)、[最終process](../build/verification/l2-weakness-wither-20260917-193800/audit/process-final.json)。公開鍵localhost serverも各run終了時に停止。Invader既存UUID重複等のWARNは残り、全WARN解消や自然boss保存全般PASSとはしない。

### 13.5 最終成果物・残る範囲

`build/libs/foodhealing-3.0.0.jar`：**229,494 bytes /150 entries / SHA-256 `5C1A716EEDE7AEB4449B36EEFFBDDA156176B16B60A1676229224AA3390DB327`**。

最終L2/Cube/Invader配置と完全一致。metadata・Mixin configs/refmap/reobf・CRC・外部Jar/class/fixture/GameTest/ExampleMod非混入を確認。旧Jarとのentry集合は同じで、差分はmanifest timestampとAdapter1 classだけ。[現物監査](../build/verification/l2-weakness-wither-20260917-193800/audit/final-jar.json)。試験後の再生成/差替えなしで、実際に試験したこのJarを最終配布物とする。元L2/依存/Trial/TaCZのhashは不変。

20:34時点の新2 ID実clientは **NOT RUN**。後続の計画と旧4停止履歴は§13.6–13.10へ保全。現在は旧§13.10のGUI/A/B/Tと[§13.11のC/seal・通常保存終了](#l2-weakness-wither-client-c-completed-result)でREAL CLIENT LIMITED PASS。今回A/B/TはC準備のみで新規PASS件数に加算しない。既存4 ID/Trial実clientを再計画しない。両極意IMPLEMENTATION_PENDING/購入停止/SP保護、§12.5の購入解放範囲E、RC=NO、REAL2CLIENT=BLOCKED、個別開始条件を維持。

<a id="l2-weakness-wither-client-plan"></a>

### 13.6 weakness/witherだけの限定実client計画（2026-09-17 21:14 JSTの計画履歴・現在実測は§13.11）

**以下は21:14時点の計画。後続の明示承認で補助を作成・起動し、実測は§13.7へ分離した。計画中の「未作成/次回承認」は当時の状態であり、現在の実行状況ではない。** §13.1–13.5の実装・133/133・指定回帰・正常終了は完了済みのまま。旧4 IDの§10.11、Trial Cube/侵略者も再試験しない。本節は効果ケースの手順正本。初期source準備の旧reinit(level1)/NoAI順序は履歴として残し、現在は利用者承認を反映した§13.10のlevel0/自然接地→NoAI安定→trait付与を参照する。現在の完了結果と次作業候補は§13.11。

#### 13.6.1 対象・実物・読取根拠

- 製品は§13.5の **229,494 bytes /150 entries / SHA-256 `5C1A716EEDE7AEB4449B36EEFFBDDA156176B16B60A1676229224AA3390DB327`**。現物を再測定して一致。旧3807…Jarへ戻さない。MC1.20.1 / Forge47.4.0 / Hostility2.5.19 / Library2.5.3 / Complements2.6.1 / Curios5.12.0+1.20.1 / Patchouli1.20.1-84-FORGE、L2単独構成を用いる。
- [今回の限定読取記録](../build/verification/l2-weakness-wither-client-plan-20260917-210626/audit/read-only-inputs.json)：5つの外部Jar、既知Prism/Java17 runtime・Forge client/universal/installer・MC/Forge metadata・asset indexの存在を確認。外部Jarは既存hashと一致。9/15の記録からMC metadata/asset indexは変化しているが、現物同士のSHA1/sizeは一致し、変更原因は未確認のまま保存する。全assets/PC/旧worldの総走査・認証情報読取なし。追加MOD artifactの再提供・downloadは不要。実行時に新たな欠損が判明した箇所だけ停止して示す。
- 参照した実装：[TargetEffectTraitMixin](../src/main/java/com/leva/foodhealing/mixin/l2hostility/TargetEffectTraitMixin.java)、[L2HostilityAdapter](../src/main/java/com/leva/foodhealing/compat/l2hostility/L2HostilityAdapter.java)、[ShokugiTickHandler](../src/main/java/com/leva/foodhealing/ShokugiTickHandler.java)、[ToggleSkillPacket](../src/main/java/com/leva/foodhealing/network/ToggleSkillPacket.java)。2 IDとも実 `TargetEffectTrait.postHurtImpl → EffectUtil.addEffect`。P保護は通常浄化・極意の両取得/ONで成立し、Tは条件に含まない。Tの新規適用には3つ取得/ONを要する。
- [最終133件receipt](../build/verification/l2-weakness-wither-20260917-193800/final-l2/l2-result.json)の各ID通常浄化対照は、付与1・numeric3・tick前あり/後なし。6 IDのnative chunk保存復元/再付与抑止もPASS。既存 [実client driver](../build/verification/l2-client-20260916-185745/fixture/src/main/java/com/leva/foodhealing/verification/clientl2/L2ClientVerification.java) と [修正済みobserver](../build/verification/l2-client-20260916-185745/fixture/src/main/java/com/leva/foodhealing/verification/clientl2/L2ClientServerObservation.java) のEND優先度・実remove観測だけを設計に再用し、旧world/allowlist/receipt/journalは流用しない。

#### 13.6.2 最小単位の選択・今回追加する価値

**同じ新Zombie1体にweakness/wither各rank1を載せ、A/B/C各1回、合計native攻撃3回＋攻撃なしのT適用を推薦する。** 効果IDは `minecraft:weakness` / `minecraft:wither`、trait IDは `l2hostility:weakness` / `l2hostility:wither` と区別する。

| 方式 | 証拠の明瞭性・安全性 | 判断 |
|---|---|---|
| 2 IDを同sourceに付与、3攻撃 | 攻撃numericは共有1回。各effect IDについてHEAD/RETURN/Added/実instance/実removeを別々に測るため、片方だけ成功しても検出できる。HP20→17→14→11。source/位置/GUI/取得準備を共通化できる | **推薦**。2 IDは同経路で独立したMobEffect。追加wear用の剣・旧4traitは不要 |
| 1 IDずつ別source/ケース | 単独原因の切り分けは容易だが、同じ比較で計6攻撃・source2体を要する。同一無回復playerなら最終HP2となり、別world/本人準備なら作業が増える | 今回不採用。失敗時に自動的に分割再試行せず、証拠を残し次の承認で切り分ける |

新規価値は **最新配布Jarでの製品GUI→server canonical→実LocalPlayer、実画面HP、各新IDの実作用と実client cap収束**。全資格組合せ・反射・他player・既存effectの強弱保全・対象外効果・再付与/lifecycle/XYZ境界等は既存133件に任せる。既存同種/他source効果を注入する追加実clientケースは行わない。理由は新2 IDの強弱保全・blindness/beneficial保持が自動で確認済みで、新たなclient専用処理がないこと、P ONでは**通常浄化が既存HARMFULを本来除去する**ため、画面の消失だけでは極意の非治療を判別できないこと。通常浄化の既存挙動を止めて保全成功を演出しない。T時のHP不変は観測するが、それだけで全既存effectの非治療実client PASSとはしない。

#### 13.6.3 新規環境・一度だけの準備

1. 次回承認後に `build/verification/l2-weakness-wither-client-<newRunId>/` と既存検証Prism配下の新instance/new worldだけを作る。既存共有runtime/assets/librariesを利用し、旧instance/worldを起動/コピーしない。外側modsは製品＋前提5Jar＋新別補助の7本。Trial/TaCZ/旧helper/133fixture/内包ライブラリを追加しない。実ロードでTracker0.4.4等を記録する。認証情報の読取/コピー、認証成功を代行するstubは使わない。
2. **生成前GUI**：Survival / Normal / cheats ON / `doMobSpawning=false` / `naturalRegeneration=false`、平坦な安全配置・structures OFFを記録。入場後にもserver照会で一致を確認。新world内に通常建築commandで床y79・空間y80–83・不透明屋根y84、x/z±8の閉じた石の測定室・照明を用意する。周辺敵対Mob、侵入、日光燃焼、落下/窒息/水/炎を実地・readonly guardで確認する。今回はX90退避室・範囲外移動を作業へ追加しない。
3. 本人は正常fresh canonical、HP/MAX/最大HP属性20、armor/toughness/absorption0、全装備/Curios空・効果なし・Root/他skillなし。food>6、creative/invulnerable=false。login保護/hurt無敵時間は自然に終了させる。新worldの実パス/run/token・本人UUID（ゲーム内inspectから取得）・対象Jar群hashを固定し、違いをHP/effect/pending等のsetterで直さない。
4. 明示prepare **1回だけ**：書込前にCREATE_NEW receiptをPREPARING/prepareCount1にする。通常capabilityの `addUnspentSkillPoints(603)` → `trySpendSkillPoints(603)` → `setSkillLevel(id,1)` で `foodhealing:purification` / `foodhealing:purification_mastery` / `foodhealing:truth_mastery` を準備。初期はP/M ON・T OFF（`setSkillDisabled`）。未使用0/使用済603、取得前提と支出内訳3+100+500を記録する。`AcquiredSkills` / `UnspentSkillPoints` / `SpentSkillPoints` / `DisabledSkills`以外のcanonicalはfreshから不変を要求する。
5. 同prepareの初期化として新 `EntityType.ZOMBIE` 1体を[2.5,80,0.5]へ生成、NoAI/PersistenceRequired、空装備・targetなし。公開 `MobTraitCap.reinit(mob,1,false)` を準備時のみ1回、自然tickでlv0/traits空/HP20/attack3を確認後、正式 `LHTraits.WEAKNESS.get()` → `WITHER.get()` をこの順に `setTrait(trait,1)`。native pending→initialize/postInit/元sync完了を待つ。手書きmap/NBT・補助によるsync payload代作はしない。本人は[0.5,80,0.5]の床上で、sourceと非接触・同level/近距離を確認。剣や他traitを用意しない。
6. 起動した新環境で既定 `weakTime=200` / `witherTime=200`、rank1のamplifier0、damageFactor0.02/exponentialDamage=falseをreadonly確認（根拠：[最終統合のconfig](../build/verification/l2-weakness-wither-20260917-193800/final-l2/config/l2_configs/l2hostility-common.toml)）。source UUID・全属性/modifier・装備・native capのlevel/data/drop等をbaseline記録。意図した初期状態でなければ設定/期待値を変更せず停止する。
7. `CapabilityEvents.syncToClient → ShokugiSyncPacket`、通常provider保存へ引き継ぐ。prepare以後のGUIは `FoodHealingScreen → ToggleSkillPacket → server取得検証/RootController.setSkillDisabled → 通常sync` のみ。**通常GUI購入成功ではない**。以後の取得/SP/trait再注入・HP/効果/属性修復なし。自然初期化完了の待機はprepareの一部として一度だけ管理し、再入場で再開/再付与しない。

#### 13.6.4 操作・ケース別期待値（実測前）

P=通常浄化、M=浄化の極意、T=真実の極意。表のHPはserver実効値・実LocalPlayer・HUDの期待で、すべてMAX20。各段階の開始effectは空、待機中の攻撃0。sourceは全行で同UUID。

| 段階 / GUI操作・開始P/M/T | source開始→終了 | HP開始→終了 / native回数・numeric | 2 IDそれぞれの即時付与と通常浄化前後 | 合格の追加条件 |
|---|---|---|---|---|
| 準備収束：PだけOFF→ONを1往復、最終ON/ON/OFF | weakness/wither各rank1を保持、markerなし | 20→20 / 0 | 両effectなし | 各切替でserver/実client canonical一致。Mの保存ON/Tの保存OFFは非連動 |
| **A 本人保護**：ON/ON/OFF | 2trait保持、markerなし | **20→17 / 1 / mob_attack3** | 各EffectUtil HEAD/RETURN0・Added0、attack returnにもなし、対象実remove0、通常浄化後もなし | sourceが残るのでT除去による偽装なし。実client追跡cap各rank1・HP17/20 |
| **B 実付与の正対照**：GUIでMだけOFF、ON/OFF/OFF | 2trait保持、markerなし | **17→14 / 1 / mob_attack3** | **各HEAD1/RETURN1/Added1、attack returnに200tick・amp0が存在**。同tickの通常浄化remove前に実instanceあり、Remove通知1/非cancel、remove RETURN true1、後は空 | 各IDを別集計、片方の2回付与は不合格。Wither DOT0。実client最終effects空/HP14/20 |
| **T適用**：GUI M ON→T ON、ON/ON/ONで範囲内待機 | 製品が2traitをnative remove、client capも空へ | **14→14 / 0** | 初めから両effectなし、補助による消去なし | server marker Version1/Mob=同UUID/Traits=今回2 ID true、native remove各1、元syncと実client空capを別記録。HP・source対象外データを治療/変更しない |
| **C 受け手保護なし**：GUI T OFF→M OFF→P OFF、OFF/OFF/OFF | 空cap/marker保持 | **14→11 / 1 / mob_attack3** | 各HEAD/RETURN/Added/対象実remove0、前後空 | P/M保護も通常浄化の後除去もない状態で、新規作用なし。sourceは消失せず同UUIDを実clientで追跡 |
| **seal**：3つOFFのまま | 空cap/2 ID marker保持 | **11→11 / 0** | 空を保持 | prepare1/hit3/SEALED、SP0/603、3nodeLv1/3OFFを確認。追加攻撃/範囲外移動/再読込なし |

各hitは専用commandで予約し、次の本人 **server PlayerTick END/HIGHEST** でguard再確認→FIRINGをCREATE_NEW→native `Zombie.doHurtTarget(real ServerPlayer)`を1回だけ呼ぶ。returnで即時実instanceを記録し、同eventの **製品NORMAL** が通常浄化、**observer LOWEST** が除去結果を記録する。これは旧成功runと同じ実行順で、次のLivingEntity effect tickへWitherを持ち越さず、手動tick操作なしで数値3と後段除去を分離する。製品handlerの優先度変更・補助からのPurification handler/event再実行・removeEffect・Wither tick抑止はしない。自然AI攻撃/Wither持続DPSの試験とは記録しない。

native return=true、LivingHurt/LivingDamageは各1・非cancel・amount3、DamageTypeキー **`minecraft:mob_attack`**、attacker/direct=同sourceUUID。`DamageSource.getMsgId()`の `mob` とregistry keyを区別する。HP差3を期待し、Wither等の別damage・回復が混じれば差し引いて合格へ補正しない。攻撃間のhurt保護終了・距離を確認し、knockback後は必要時のみ通常 `/tp` で同じ安全な床へ戻す。HP/effectは戻さない。

#### 13.6.5 readonly観測・収束・停止

| 観測境界 | 記録と判定 |
|---|---|
| EffectUtil.addEffect HEAD/RETURN | 非cancellable観測。run/world/本人UUID/case/gameTime/sourceUUID/**effect ID**・duration/amp・実effect一覧。Aは製品Redirectにより呼出し0、Bは各ID1が正対照。Cはsourceからtrait自体が消えて0。攻撃成立/source capも併記し、呼出し0だけで保護PASSにしない |
| MobEffectEvent.Added | 各対象IDの実instance・旧instance/由来情報（取得できる範囲）を記録。Bで各1。全effectイベント総数だけで判定せず、対象外は別記録 |
| remove要求前 / Remove通知 / remove RETURN | 実 `LivingEntity.removeEffect` HEADの当該instance、通知時instanceとcancel、return boolean/後instanceを同case/entity/tick/IDで対応。**通知だけでは実除去ではない**。`foodhealing:guts`等の対象外通知はrawに残すが対象2 IDの除去数へ入れない |
| attack return / END LOWEST | 同tickでBの「両instanceあり」→製品通常浄化後「なし」を直接対応。予定durationから経過時間を推測して付与を補完しない。弱体化の攻撃力変化やWither DOTの長期測定は不要 |
| LivingHurt/LivingDamage/Heal | 本人とsource、種類/amount/cancel/前後HP、case・tickを対応付ける。準備後の全damage/healを監視し、攻撃3以外の被弾や回復を検出。観測側のcancel/setAmount/healは禁止 |
| Tのnative remove / sync | readonly HEAD/RETURNで対象MobTraitCapの `removeTrait` 対象ID・前後map、`syncToClient` 対象sourceとpayload生成元状態を記録。Tで各ID remove1、元sync送信1以上と送信後の実client cap空を対応。元の通常同期の重複回数を変更しない。補助からremove/mark/syncを代行しない |
| server canonical / 実LocalPlayer / 実client source | 独立したserver/client snapshotにorigin・採取連番/時刻・side・UUID・同stageを付ける。取得3node/SP/DisabledSkills、HP/MAX/最大HP属性、effects、sourceの実native cap/属性を比較。server所有markerはserver/保存側の別証拠で、clientへ独自転送しない |
| 画面 | 通常GUIの取得Lv/SP/3toggle、HUD20→17→14→11/MAX20、sourceの実表示が確認できた部分をF2へ対応。Bのadd/removeは同tickなので一瞬のeffect iconは必須にせず、未撮影を目視PASSへ補完しない。補助overlayを製品表示の証拠にしない |

HP/amount/最大HP属性は許容差1e-3、ID/rank/SP/回数/toggle/markerは完全一致、HUD小数1桁丸めは±0.05相当。各段階は動作中server100tickまたは壁時計10秒の先に到達した方を収束上限とし、通常ポーズ中を除く。**新しい実client snapshotが更新された連続2組**でcanonical・HP/MAX・effects・同UUID cap一致を要求し、古いcached snapshotの2回参照を一致に数えない。観測時刻/gameTime自体の完全一致や自然に進む時計fieldのbyte一致は要求しない。source untracked/nullは「空cap」でなくUNVERIFIED、復帰待ちで期限を越えれば停止する。

停止条件：run/実path/UUID/hash不一致、fresh不成立、不正/pending、想定外の取得/装備/初期trait/config、外部Mob侵入/燃焼/危険床、予期しないHP/effect/属性/trait変化、Wither DOT、攻撃失敗/多重化、observer欠落/順序違い、同期期限超過、途中prepare/hit失敗。**次の攻撃を拒否し、通常pause→Save & Quitを優先**。receipt/journalはFAILEDを保持し、削除・再付与・期待値緩和・同case再実行はしない。正確な反証はFAIL、観測不足だけならUNVERIFIED。危険状態のまま報告作成を続けない。製品不具合が疑われれば今回の将来helper範囲で修正せず報告する。

#### 13.6.6 次回作成する別補助と書込境界（全て未作成）

新root内 `fixture/src/main/java/com/leva/foodhealing/verification/clientl2ww/`、別MOD ID `foodhealing_l2_ww_client_verification` を予定する。§10.11の小さなprepare/guard/observer設計を基にし、133ケースfixtureや旧4trait/装備試験を丸ごと持ち込まない。

| 予定ファイル | 役割・許される書込 |
|---|---|
| `L2WwClientVerification.java` | 新run guard、inspect、本人603SP/3nodeと新source2traitのprepare1回、HIGHESTでA/B/C予約各1回、seal。初期sourceのNoAI/Persistence・識別sentinelのみ補助所有。prepare完了後は取得/SP/HP/effect/traitへ書かずnative攻撃を起こすだけ。sealはjournalだけ |
| `L2WwServerObservation.java` | 上表のserver実event・段階前後・環境・native configを読取、readonly判定から失敗journal/次操作拒否。保護効果を代行しない |
| `L2WwClientObservation.java` | Dist.CLIENTで実LocalPlayer/追跡sourceを独立読取、画面/pausedとsnapshotの新規性を記録。server mirrorや新S2C packetなし |
| `mixin/EffectUtilObservationMixin.java` / `RemoveEffectObservationMixin.java` / `MobTraitCapObservationMixin.java` | 実メソッドHEAD/RETURNの読取のみ。後者はnative remove/syncの呼出し証拠。非cancellable、引数/return不変、製品Redirectと競合するRedirectなし。旧durability observerは不要 |
| `fixture/src/main/resources/META-INF/mods.toml` / `pack.mcmeta` / 専用Mixin JSON | 補助識別・限定依存・観測hookのみ。製品Mixin/refmap/pluginを編集せず、gameplay classの早期loadなし |
| `l2-ww-client.init.gradle` / `run-allowlist.json` / `audit/*` | 補助だけのsource set/compile/resources/Jar/reobf、immutable入力とonce receipt/journal/JSONL/F2/保存照合。旧receipt/allowlistコピーなし |

次回は既存Gradle8.1.1/既存 `-g` cache/`--offline` の専用task graphを**実行前**に確認。main `compileJava/classes/jar/reobfJar/build/check/test/foodHealingUnitTest`・既存fixture/suiteを巻き込まない。製品Jarをclasspathの実fileとして参照し、main.output依存を作らない。補助Jarのentry/refmap/reobf/hashを検査し新instanceのmodsだけへ置く。製品Jar/ソース/test/build.gradle/Config/schema/packet/購入gateは不変。新download不要、通常compile準備エラーの修正は将来承認された補助内だけに限定する。

#### 13.6.7 保存・終了、今回繰り返さないこと、次回の一括範囲

**同world再読込は推薦しない（0回）。範囲外移動/再追跡も不要。** 新2 IDは同じmarker/remove/sync/persistenceを使い、133件で6 IDのnative保存・別entity instance復元・同UUID・再付与抑止を確認済み。§10.11には旧4 IDの実client全OFF/X90/同world通常再読込/同UUID再追跡の証拠がある。新しいID固有の保存schemaやclient復元処理の変更はないため、同じ手順の再演より今回の実LocalPlayer/2 ID元syncに絞る。今回を新2 IDの実client再読込PASSと表記しない。

1. Cと収束後、3つOFFのまま測定室でseal（prepare1/native3/各DONE/SEALED）。sourceは同UUIDで追跡中・2traitなし、server marker2 ID・対象外データ保持。sourceの削除/kill/再生成は不要。
2. **通常Save & Quit** → titleへ戻り保存完了log確認 → 競合しない時点で対象worldのみreadonly保存照合。`level.dat/Data.Player` と本人UUID.dat（存在するものと実際の読込優先を区別）、Health11/DeathTime0/MAX_HEALTH属性根拠20、3取得Lv1/SP0・603/3つOFF/対象effectsなし。entity regionの同Zombie UUID/NoAI/PersistenceRequired/Health20・native capの2trait不在、Version1/Mob/2 ID marker・対象外cap/attributesを確認。Health・実効HP・最大HP属性・HUDは別欄に記録し、NBT Healthだけで実client一致としない。
3. **worldへ再入場せずQuit Game** → log/通常保存/今回Minecraft PID終了を確認。製品/配置Jar同hash、製品source/購入停止不変、receiptを照合し、A/B/T/C・GUI/HUD/元同期・保存/終了を個別にPASS/FAIL/UNVERIFIEDで3文書へ記録する。保存値を上書きして合わせない。失敗時も可能な通常終了を行い、部分証拠を残す。
4. 次回承認の一括範囲：**別補助作成/限定offline compile・reobf/guard検査 → 新instance/worldと安全区画 → prepare1回 → GUI/実native3攻撃・T適用・独立観測 → 全OFF/seal → 通常保存/readonly照合 → Quit/process確認 → 3文書更新**。自動133/core60/Cube49/Invader86、旧実client、別trait、購入解放は含めない。

通常GUI/command/F2/保存終了は利用可能なComputer UseでCodexが実行し、native予約/観測はAUTOMATEDと明記する。HUMANは実行時に必要となった本人再認証・OS権限・実行できない入力だけ。現時点で追加artifactや仕様判断の不足はなく、必要なのは**本計画の実行承認**とその後に作る別補助。秒数/tick計測、瞬間icon撮影、食事長押し、死亡試験、OBSを要求しない。両極意IMPLEMENTATION_PENDING/購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、既存個別開始条件・可逆クラフト増加の既知許容仕様を維持する。


<a id="l2-weakness-wither-client-stopped-result"></a>

### 13.7 新2 ID実client — prepare後の補助guard停止・通常保存終了（2026-09-17 21:50 JST）

**run `20260917-211821`：PREPARATION GUARD FAILED。A/B/T/CはNOT RUN / UNVERIFIED。製品効果FAILではない。** [利用者承認](../build/verification/l2-weakness-wither-client-20260917-211821/audit/user-request.txt) / [実測集計](../build/verification/l2-weakness-wither-client-20260917-211821/audit/stopped-run-analysis.json)。失敗後の再付与・再試行・guard緩和・HP/effect/trait/SP修復なし。GUI/command/F2/保存終了はCOMPUTER USE、prepare/native初期同期/観測/保存解析はAUTOMATED、HUMANゲーム操作なし。

#### 対象・別helper・限定build

- 製品・配置物とも **229,494 bytes /150 entries / SHA-256 `5C1A716EEDE7AEB4449B36EEFFBDDA156176B16B60A1676229224AA3390DB327`**。製品再生成なし。
- 別MOD `foodhealing_l2_ww_client_verification` 1.0、**34,529 bytes /17 entries / SHA-256 `7C8D2F41659E601FAD35EC2258E291F3A209B943A37B023EC65AE1E4341D5F95`**。新rootのdriver/client/server observer＋3個の非cancellable Mixin・専用initのみ作成。旧helper binary/allowlist/receipt/worldは流用しない。entry/CRC/Mixin/reobf/書込境界・非混入は[prelaunch](../build/verification/l2-weakness-wither-client-20260917-211821/audit/prelaunch.json)と[reobf javap](../build/verification/l2-weakness-wither-client-20260917-211821/audit/helper-reobf-javap.txt)。補助修正や再buildによる失敗隠蔽なし。
- 既存Gradle8.1.1 / JDK17.0.7 / 指定cache / `--offline --no-daemon`。実行前dry-runと[task graph](../build/verification/l2-weakness-wither-client-20260917-211821/audit/task-graph.txt)は専用compile/resources/classes/Jar/reobf＋既存MCP/SRG補助だけ。`downloadMcpConfig`というtask名は含むがoffline cache使用。製品compileJava/classes/jar/reobfJar/build/check/test/unit、旧fixture/GameTestは含まない。[helper-build-01.log](../build/verification/l2-weakness-wither-client-20260917-211821/audit/helper-build-01.log) **BUILD SUCCESSFUL、初回成功**。
- [実ロード](../build/verification/l2-weakness-wither-client-20260917-211821/audit/runtime.jsonl)：MC1.20.1 / Forge47.4.0 / Hostility2.5.19 / Library2.5.3 / Complements2.6.1 / Curios5.12.0+1.20.1 / Patchouli1.20.1-84-FORGE、内包Tracker **0.4.4**。既存Prism10.0.5/Java17.0.15、本人profileのoffline起動。認証情報読取/コピー・新downloadなし、オンライン再認証PASSではない。Trial/TaCZ/旧fixture/内包Jar外置きなし。新instanceのloader versionCheck=falseのみ、製品Config不変。
- 初回Prism起動操作は自動承認審査が「明示承認なし」と拒否したが、依頼本文の既存承認を示して**同一操作**の再審査を受け許可された。[記録](../build/verification/l2-weakness-wither-client-20260917-211821/audit/approval-review-record.json)。現在の停止原因とは別。起動logには既存optional GeckoLib/refmap/描画等WARNが残り、全WARN解消とはしない。

#### 安全準備・停止の実測

- 新instance `FHR_L2_WW_Client_20260917-211821`、新world `FHR_L2_WW_20260917_211821`。生成前GUIでSurvival/Normal/cheats ON、構造物OFF、doMobSpawning=false / naturalRegeneration=falseを設定しF2保存（[画面](../build/verification/l2-weakness-wither-client-20260917-211821/audit/screenshots/)）。通常コマンドでx/z±8・y79〜84の石殻、内部照明、床上(0.5,80,0.5)へ移動。prepare前guardは石殻全境界・近隣敵対Mobなし・本人接地/非燃焼/非水/非窒息/非落下、正常fresh/HP20/MAX20/無装備/防御0/Curios空/login保護終了を確認。保存gameruleも両false・Normal/Survival。
- [prepare receipt](../build/verification/l2-weakness-wither-client-20260917-211821/audit/prepare-receipt.json) **1回、credit603/spent603/unspent0**。P/M/T各Lv1、初期P/M ON・T OFF。通常GUI購入成功ではない。sourceUUID `4ea2053c-e96c-4a09-a5f1-3088f91eebca`、NoAI/Persistence、空装備、level0/HP20/attack3、正式setTraitでweakness→wither各rank1。weakTime/witherTime200・factor0.02/exponential=falseを[実config](../build/verification/l2-weakness-wither-client-20260917-211821/audit/native-config.json)で確認。
- gameTime7046で[prepare-complete](../build/verification/l2-weakness-wither-client-20260917-211821/audit/prepare-complete.json)を作成後、同tickのLOWEST monitorの`sourceStable`が **`unsafe source position`** で停止（[failure](../build/verification/l2-weakness-wither-client-20260917-211821/audit/failure.json)、21:36:53.783）。これは `onGround && !isInWaterOrBubble && !isInWall` の複合guard。停止瞬間の各operandは別々に記録されていない。保存sourceは元位置(2.5,80,0.5)、OnGround0、Motion0、NoAI1、HP20。**NoAI sourceの生成後接地を待たずREADY扱いした補助の準備判定不備を示唆**する。厳密な瞬間operandや本体不具合まで断定しない。
- `prepare-complete`というファイル名だけで安全準備PASSへ昇格しない。後続のGUI/attackへ進まず、通常pause→Save & Quit。guard・期待値・receiptを変更せず、source/HP/effect/取得の修復なし。

| 項目 | 実測 / 判定 |
|---|---|
| 取得・native source準備 | 1回の取得/SPと2trait付与は確認。実client canonical/同source2traitの一致sample60件も保存されたが、FAIL後を含むため規定収束PASSとはしない |
| GUI P OFF→ON / A / B / T適用 / C | **NOT RUN / UNVERIFIED**。native hit0、FIRING/DONEなし、HP20維持。期待17/14/11を実測へ転記しない |
| ID別effect・numeric | weakness/wither各EffectUtil/Added/実remove観測0、Hurt/Damage/Heal0。ただし攻撃自体0のため保護成功の証拠ではない。gutsのremove通知292組（実instanceなし/return false）は別rawログ |
| native remove / sync / actual cap | remove0。PREPARING中の元sync HEAD/RETURN各3のみ。rawのcase=T固定ラベルはT適用を意味しない。実client capには2traitが残り、空cap/markerを未確認 |
| HUD / source表示 | COMPUTER USEでHP20.0/20.0、Weakener I / Withering Iを確認。攻撃後HUD・瞬間effect iconは未観測 |
| 全OFF / seal | **NOT RUN**。P/M ON・T OFFでFAILEDを保全。SEALEDへ変更しない |
| 通常保存 / 終了 | 停止状態の通常保存・readonly照合・Quit/process終了を確認。成功状態の保存・再読込PASSではない |

#### 保存・不変確認・次の単位

- 21:37:24.515 **All dimensions are saved** → 競合のないtitleで[保存値](../build/verification/l2-weakness-wither-client-20260917-211821/audit/final-save-values.json)をreadonly照合。level.dat/Data.Playerと本人UUID.datはcanonical一致、Health20/DeathTime0/MAX_HEALTH属性20、3取得/SP0・603、P/M ON・T OFF、effects/Inventory空。同source1体、Health20/NoAI/Persistence、2trait各rank1、T markerなし、属性/capは記録のまま。source cleanup/killなし、保存書換えなし。
- 21:38:05.823 **Stopping!**、[PID24028終了](../build/verification/l2-weakness-wither-client-20260917-211821/audit/process-final.json)。強制killなし、OS exit codeそのものは未採取。world再読込0、範囲外0。ログ原本は[game-final.log](../build/verification/l2-weakness-wither-client-20260917-211821/audit/game-final.log)、失敗world/receipt/journalを保持。
- 製品/source/test/build/AGENTS等 **146ファイル不変**、旧証拠 **304ファイル不変**。ただし旧instance metadata18件中1件、`FHR_Invader_Client_20260916-211828/mmc-pack.json` のLWJGL `cachedVolatile:true`削除を検出。版は不変、書換主体は未確認、旧instance/worldの起動なし。[差分例外](../build/verification/l2-weakness-wither-client-20260917-211821/audit/old-instance-metadata-exception.json)。旧worldを総走査したhash保証ではなく、操作範囲と既存証拠hashの確認として区別する。
- **次は別新runでの再試験承認後、source初期接地の成立手順とoperand証拠だけを補助内で修正する。** 実collisionによる接地確定を初期生成中に済ませ、prepare-completeより前に各安全条件を採取する案。OnGround/HP/effect/traitの直接setterによる補正、guard削除/緩和、失敗run再利用はしない。今回その修正・再起動は行わない。追加MOD artifact/新仕様判断は不要。
- その後の範囲は既存§13.6のGUI/3攻撃/T・全OFF/seal・通常保存/readonly・Quitまでで変えない。旧133/core60/Cube49/Invader86・旧4 ID/Trial実client PASSを維持し再実行なし。両極意IMPLEMENTATION_PENDING/購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKEDと個別gateを維持。可逆クラフト増加は既知許容仕様・バグ修正対象外。


<a id="l2-weakness-wither-client-contact-timeout"></a>

### 13.8 別新run — 自然接地の有限待機timeout・通常保存終了（2026-09-17 22:25 JST）

**run `20260917-220121`：PREPARATION GUARD FAILED / NATURAL CONTACT TIMEOUT。GUI・A/B/T/C・sealはNOT RUN / UNVERIFIED。製品効果FAILではない。** [今回の明示承認](../build/verification/l2-weakness-wither-client-20260917-220121/audit/user-request.txt) / [実測集計](../build/verification/l2-weakness-wither-client-20260917-220121/audit/stopped-run-analysis.json)。旧§13.7のrun `20260917-211821` は変更・再開・修復せず保全した。以下は今回の実測であり、旧runの失敗瞬間を遡及推定しない。

#### 補助差分・限定build・実ロード

- 新root `build/verification/l2-weakness-wither-client-20260917-220121`、新instance `FHR_L2_WW_Client_20260917-220121`、新world `FHR_L2_WW_20260917_220121`。別MOD `foodhealing_l2_ww_client_verification` 1.0、**36,862 bytes /17 entries / SHA-256 `F340BD03D372BC3854CBFC21855AF583FEE01A98AB9CB1CA7A19D5B6135438B1`**。配置同hash、CRC/entry/Mixin/reobf・非混入は[prelaunch](../build/verification/l2-weakness-wither-client-20260917-220121/audit/prelaunch.json)。製品は**229,494 bytes /150 entries / `5C1A716EEDE7AEB4449B36EEFFBDDA156176B16B60A1676229224AA3390DB327`**のまま、配置も同hash。
- [旧helperとの差分](../build/verification/l2-weakness-wither-client-20260917-220121/audit/helper-changes.diff)はrun拘束、新prepareの最大200tick/稼働monotonic10秒受動待機、READY/prepare-complete前の実onGround確認、sourceStable/failure前を含む個別readonly operand、foreign damage/healカウンタ。初期生成・取得/SP・正式trait付与・測定期待値・既存guardは維持。OnGround/位置/速度/fallDistance/HP/effect/trait/capの修復、NoGravity/無敵化、追加move/travel/tick呼出し・AI切替なし。client observerと3つの非cancellable Mixinは旧sourceのまま。
- 既存Gradle8.1.1/JDK17.0.7/指定cache/`--offline --no-daemon`。実行前dry-run後、[限定build](../build/verification/l2-weakness-wither-client-20260917-220121/audit/helper-build-01.log) **初回BUILD SUCCESSFUL（11秒）**。[task graph](../build/verification/l2-weakness-wither-client-20260917-220121/audit/task-graph.txt)：`addMixinsToL2WwClientVerificationJar`（SKIPPED）、`compileL2WwClientVerificationJava`、専用resources/classes/Jar、`downloadMcpConfig`（offline cache）、`extractSrg`、`createMcpToSrg`、`reobfL2WwClientVerificationJar`。製品compile/jar/reobfJar/build/unit/check・旧fixture/GameTestを巻き込まず、新downloadなし。
- [実ロード](../build/verification/l2-weakness-wither-client-20260917-220121/audit/runtime.jsonl)：MC1.20.1/Forge47.4.0/Hostility2.5.19/Library2.5.3/Complements2.6.1/Curios5.12.0+1.20.1/Patchouli1.20.1-84-FORGE。内包選択版Tracker**0.4.4**、mob_weapon_api0.2.13、l2screentracker0.1.4/l2tabs0.3.3/l2itemselector0.1.9、mixinextras0.2.0-beta.8。Trial/TaCZ/旧fixtureなし、内包Jar外置きなし。
- 既存Prism10.0.5/Java17.0.15、本人profileによるoffline起動。認証情報読取/コピーなし、オンライン再認証PASSではない。新loaderのversionCheck=false・既存offline手順を使用。Prism一覧はF5で再読込。先行`--help`照会が戻らなかったため、その補助process PID3792だけを同定して停止した（Minecraftは起動していない照会）。ゲームPID11668は後述の通常Quitで終了し、強制killしていない。

#### 安全環境・1回準備・接地operand

- 生成前GUIでSurvival/Normal・cheats ON、Superflat/構造物OFF、**doMobSpawning=false / naturalRegeneration=false**。F2と[画面証拠](../build/verification/l2-weakness-wither-client-20260917-220121/audit/screenshots/)を保存。通常commandでx/z±8、y79〜84の連続した石の床/壁/不透明屋根、内部照明、本人(0.5,80,0.5)。prepare前の既存guardで石殻全境界・32block内外部敵なし・本人接地/水火窒息落下なし、正常fresh/HP20/MAX20/無防御・無装備/Curios空・login保護終了を確認。保存gameruleも両false/Normal/Survival。測定中の区画修復や値補正なし。
- 本人UUID `<PLAYER_UUID>` は新worldのinspect実測から新allowlistへ拘束。[prepare receipt](../build/verification/l2-weakness-wither-client-20260917-220121/audit/prepare-receipt.json) **1回**、credit603/spent603/unspent0、P/M/T各Lv1、P/M ON・T OFF。通常GUI購入成功ではない。sourceUUID **`b2e7b3c8-7382-4f4a-94e2-55f575cb3d42`** 1体、NoAI/Persistence、正式setTraitをweakness→wither各rank1で1回準備。再付与なし。
- [source-safety.jsonl](../build/verification/l2-weakness-wither-client-20260917-220121/audit/source-safety.jsonl)：gameTime **2804〜3004、201回**のprepare tick観測（最初から最後までmonotonic **9.9733866秒**）＋failure確定直前の同tick観測1回。201回目の上限判定で停止し、22:16:57.1947467のfailureをCREATE_NEW保存。`prepare-complete.json`/`source-contact-confirmed.json`/`source-baseline.json`は**未作成**。取得準備の実行とPREPARED成功を分離する。

| failure直前の同一snapshot項目 | 実測 |
|---|---|
| run / phase / gameTime | 20260917-220121 / WAITING_NATURAL_CONTACT / 3004 |
| wall-clock / monotonic | 2026-09-17T13:16:57.193241700Z（22:16:57 JST） / 164672490474600ns |
| position / deltaMovement | (2.5,80.0,0.5) / (0,0,0)。全201回同値 |
| onGround / fallDistance | **false /0**。全観測でonGround=false |
| water / waterOrBubble / inWall | false /false /false |
| isOnFire / fireTicks | false /-1 |
| below / feet / head | (2,79,0)stone /air /air |
| bboxと床 | min=(2.199999988,80,0.199999988)、max=(2.800000012,81.950000048,0.800000012)、床上端80、差0、noCollision=true |
| HP/MAX / NoAI / PersistenceRequired | 20/20 /true /true |
| NoGravity / invulnerable | false /false |
| L2 level / initialized / ranks | 0 /true /weakness1・wither1（gameTime2805以降） |
| Truth marker / 外部敵対Mob | なし /空 |
| foreign damage / heal | 0 /0 |
| 実config | weakTime200 /witherTime200、factor0.02・exponential=false（[readonlyコピー](../build/verification/l2-weakness-wither-client-20260917-220121/audit/l2hostility-common-readonly.toml)） |

床接触の幾何値とonGround flagは別物であり、前者だけでguardを通さなかった。[実ロード版のnative bytecode](../build/verification/l2-weakness-wither-client-20260917-220121/audit/native-contact-readonly.txt)で、`Mob.isEffectiveAi`はNoAI時false、`Entity.isControlledByLocalInstance`は非騎乗時にそれを参照、`LivingEntity.travel`はfalse時に通常の移動physics分岐を省くことを確認。**生成直後からNoAIのまま受動待機する方式では接地flagが成立しなかった**。保存OnGround0からの後付け推定ではなく、その瞬間のreadonly値を根拠にする。製品P/Tの作用は攻撃未実施で評価できない。

#### 実施・未実施と観測の境界

| 項目 | 今回結果 |
|---|---|
| prepare / PREPARED | prepare実行1、3取得/SP整合と正式2trait準備まで実施。接地timeoutのため**PREPARED未成立** |
| GUI P OFF→ON / A / B / T / C | **各NOT RUN / UNVERIFIED**。native hit0、FIRING/DONE receiptなし、攻撃returnなし |
| ID別event | weakness/wither各EffectUtil HEAD/RETURN・Added・remove HEAD/通知/RETURNは**観測0**。攻撃0のため保護PASSにしない。gutsは381組、実instanceなし・RETURN false、対象2 ID実removeへ数えない |
| numeric / Heal / HP / HUD | Hurt0・Damage0・Heal0。本人server fresh/保存Health20、実LocalPlayer HP/MAX20、画面20.0/20.0。17/14/11やnumeric3は未観測 |
| T native remove / native L2 sync | remove0、markerなし。準備時sync HEAD/RETURN各3だけ。rawのcase=Tは準備phaseの便宜ラベルであり、T適用の証拠にしない |
| actual client cap / canonical | 実LocalPlayerの3取得/SP0・603/P/M ON・T OFFと、同source2trait各rank1・画面Weakener I/Withering Iを確認。GUI packet切替・ケースごとの100tick/10秒/新規2連続snapshot収束は未実施。server markerをclientへ直接複製しない |
| 全OFF / seal / 恒久性 | **NOT RUN**。FAILEDのままP/M ON・T OFFを保存。範囲外/再読込/cleanupなし |
| 実施主体 | GUI/command/F2/通常保存終了=**COMPUTER USE**。prepare/正式trait準備/readonly観測/保存解析=**AUTOMATED**。HUMANゲーム操作なし |

#### 保存・終了・不変・次の単位

- timeout後は次操作へ進まず22:17:06通常ポーズ、22:17:24 Save & Quit、22:17:25.206 **All dimensions are saved**。保存後titleで[保存値readonly照合](../build/verification/l2-weakness-wither-client-20260917-220121/audit/final-save-values.json)：level.dat/Data.Playerと本人UUID.datはcanonical一致、Health20/DeathTime0/MAX属性20、3取得/SP0・603/P/M ON・T OFF、effects/Inventory空。同source1体、HP20/NoAI/Persistence、2trait各rank1、T markerなし。修復・cleanupなし。
- worldへ再入場せず22:17:55.386 **Stopping!**、[22:17:56 PID11668終了確認](../build/verification/l2-weakness-wither-client-20260917-220121/audit/process-after-quit.json)。通常保存/終了は完了、OS exit code自体は未採取。[最終log](../build/verification/l2-weakness-wither-client-20260917-220121/audit/game-final.log)。world再読込0、範囲外0、再試行0。
- [最終hash照合](../build/verification/l2-weakness-wither-client-20260917-220121/audit/final-invariants.json)：製品/source/test/build/AGENTS等**146件不変**、旧証拠**304件不変**、前run root＋instance/world全**198件不変・追加ファイル0**、旧instance metadata**20件不変**。旧Invader cachedVolatile差分は今回開始時の状態を維持し、復元して隠していない。PC全体/他の旧world総走査やgit diff実施とはしない。
- **別の静的補助不備**：[readonly記録](../build/verification/l2-weakness-wither-client-20260917-220121/audit/read-only-findings.json)。`L2WwServerObservation.record`の`source`キーはhurt/damageが入れたDamageTypeをsource UUIDで上書きし、`assertImmediate`は同キーを`minecraft:mob_attack`と比較している。攻撃未実施で今回のtimeout原因ではない。今回承認は接地準備・operand記録のみなので、この既存不備を修正したり攻撃して証明したりしていない。将来修正では`damageType`/`sourceUUID`を分離し、numeric/attributionの期待値を維持する必要がある。
- **次の1単位（未承認）**：初期接地方式を変更する補助限定準備。候補は「本人と物理隔離した初期生成中に通常physicsで自然接地→NoAI固定→測定開始」。今回の生成時NoAI固定から変更になるため、その初期工程と上記observer記録修正を別途承認してから別新runで行う。測定中NoAI・各guard・期待値は維持し、OnGround/位置/速度/fallDistance/HP等setter補正、待機延長だけの反復、失敗run再利用をしない。追加MOD artifact不足はない。両失敗runを保全し、承認なしで新helper/起動へ進まない。
- 既存133/133・vanilla/TaCZ各60/60・Cube49/49・Invader86/86・旧4 ID/Trial実client PASSを維持し、今回は再実行なし。両極意IMPLEMENTATION_PENDING/購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、他機能の個別gateを維持。食料生産の極意の可逆クラフト増加は既知許容仕様・バグ修正対象外。


<a id="l2-weakness-wither-client-initialization-mismatch"></a>

### 13.9 新run — native物理の準備変更後、L2初期level/HP不一致で停止（2026-09-18 21:12 JST）

**run `20260918-204657`：PREPARATION GUARD FAILED / SOURCE INITIALIZATION MISMATCH。GUI/A/B/T/C/sealはNOT RUN / UNVERIFIED。通常保存・readonly照合・Minecraft終了は完了。** [明示承認](../build/verification/l2-weakness-wither-client-20260918-204657/audit/user-request.txt) / [実測集計](../build/verification/l2-weakness-wither-client-20260918-204657/audit/stopped-run-analysis.json) / [失敗原本](../build/verification/l2-weakness-wither-client-20260918-204657/audit/failure.json)。旧§13.7/§13.8の停止runを再開・修復せず、今回も準備不一致の時点で停止した。製品効果FAILとは判定しない。

#### helper・限定build・native処理

- 新root `build/verification/l2-weakness-wither-client-20260918-204657`、新instance `FHR_L2_WW_Client_20260918-204657`、新world `FHR_L2_WW_20260918_204657`。新allowlist/receipt/journal、本人UUID `<PLAYER_UUID>`。別MOD `foodhealing_l2_ww_client_verification` 1.0、**40,428 bytes /18 entries / SHA-256 `34BAFA60B584FA9582BABB85AB16453488E6DF525DF07CDACACE078158C476E9`**。配置同hash、CRC/entry/Mixin/reobf/非混入は[prelaunch](../build/verification/l2-weakness-wither-client-20260918-204657/audit/prelaunch.json)。製品/配置は**229,494 bytes /150 entries / `5C1A716EEDE7AEB4449B36EEFFBDDA156176B16B60A1676229224AA3390DB327`**のまま。
- [旧helperとの差分](../build/verification/l2-weakness-wither-client-20260918-204657/audit/helper-changes.diff)：初期通常AIのsource専用閉鎖室、自然接地2連続→NoAI固定1回→正式2trait付与→別の2連続snapshotという準備順、個別operand拡充、独立200server tick/monotonic wall10秒watchdog、FAILED時の通常pause用`L2WwSafetyPause`追加。wall監視は別daemonが50ms間隔でraw `System.nanoTime`を確認し、ポーズを除外しない。backgroundは最後のserver snapshotだけを保存し、live entityを別threadから読まない。client/server observerはreadonly。直接onGround/速度/位置/fallDistance/HP修復、NoGravity/無敵化なし。
- observerの`source`キー競合を修正し、`damageType`/`sourceUUID`/`directEntityUUID`/`attackerUUID`を分離。各native攻撃でHurt1/Damage1、mob_attack/3/同source/uncancelledを要求する判定へ修正済み。**実攻撃は0なので新フィールドの実攻撃観測は未検証**。準備native syncのcaseもPREPARATIONへ分離した。失敗後はhelperを追加修正していない。
- 既存local Gradle8.1.1/JDK17.0.7/cache、`--offline --no-daemon`でdry-run確認後、[限定build](../build/verification/l2-weakness-wither-client-20260918-204657/audit/helper-build-01.log) **初回BUILD SUCCESSFUL（11秒）**。[task graph](../build/verification/l2-weakness-wither-client-20260918-204657/audit/task-graph.txt)：専用addMixins（SKIPPED）/compile/resources/classes/Jar、`downloadMcpConfig`（offline cache）/extractSrg/createMcpToSrg/専用reobf。製品compileJava/classes/jar/reobfJar/build/check/test/unit/GameTest・既存fixtureは含まない。新downloadなし。
- [実47.4.0 native bytecodeと読取根拠](../build/verification/l2-weakness-wither-client-20260918-204657/audit/native-physics-readonly.md)：LivingEntity.tick→aiStep→travel。Mob.isEffectiveAiはserverかつ!NoAI、非騎乗Entity.isControlledByLocalInstanceもこれを参照し、travelの通常physicsを制御する。Forge ENTITY_GRAVITY .08、Entity.moveの下向き衝突→setOnGroundWithKnownMovementで接地flag更新。**NoAIはgoalだけでなくこのphysics経路も止める**。実call path確認後に初期AIを使用したが、今回「接地→NoAI後安定」まで成功した証拠はない。
- 実ロードはMC1.20.1/Forge47.4.0、L2 Hostility2.5.19/Library2.5.3/Complements2.6.1、Curios5.12.0+1.20.1、Patchouli1.20.1-84-FORGE。内包選択版Tracker0.4.4、mob_weapon_api0.2.13、l2screentracker0.1.4/l2tabs0.3.3/l2itemselector0.1.9、mixinextras0.2.0-beta.8。[runtime](../build/verification/l2-weakness-wither-client-20260918-204657/audit/runtime.jsonl)。Trial/TaCZ/旧fixtureや内包Jarの外置きなし。Prism10.0.5の既存本人profileでoffline起動、認証情報の読取/コピーなし。オンライン認証PASSではない。

#### 安全準備・全operand・停止原因

- 生成前GUIでSurvival/Normal、cheats ON、構造物OFF、doMobSpawning=false/naturalRegeneration=falseを設定・F2記録。[screenshots](../build/verification/l2-weakness-wither-client-20260918-204657/audit/screenshots/)。通常commandで外殻x/z±8・y79〜84の石床/壁/不透明屋根、さらにsource専用石殻x1〜3/z−1〜1/y79〜83（内側x2/z0/y80〜82）を作成。本人(0.5,80,0.5)とは固体壁で隔離、境界全block/内部空間/余分なentityなしをprepare前に確認。停止までsource室を開放していない。保存gameruleも両false/Survival/Normal。
- fresh本人HP20/MAX20・防御/装備/吸収/effects/Root/他取得/pendingなしを確認し、[prepare](../build/verification/l2-weakness-wither-client-20260918-204657/audit/prepare-receipt.json) **1回**。3node各Lv1・credit603/spent603/unspent0、P/M ON・T OFFを通常syncした。**GUI購入成功ではない。** source生成1、trait-request0、NoAI-transition0。prepare-complete/接地成立/ケースreceipt/SEALEDは未作成。
- 新規source snapshotは**1回**（[raw](../build/verification/l2-weakness-wither-client-20260918-204657/audit/source-safety.jsonl)）。failure内の同値は`CACHED_NOT_NEW`であり2回目に数えない。

| 最初の同一snapshot項目 | 実測 |
|---|---|
| run / phase / gameTime | 20260918-204657 / PREPARING /4037 |
| UTC / monotonic | 2026-09-18T11:59:54.943756700Z /246451589207400ns |
| source UUID / dimension | 97a04a10-4b9a-420b-a3a8-d62043b0ef2d /minecraft:overworld |
| position / deltaMovement | (2.5,80,0.5) /(0,−0.0784000015258789,0) |
| onGround / fallDistance | false /0。自然接地2連続は未到達 |
| water / waterOrBubble / inWall | false /false /false |
| isOnFire / fireTicks | false /−1 |
| below / feet / head | (2,79,0)stone /air /air |
| bbox / floor | min=(2.199999988079071,80,0.19999998807907104)、max=(2.800000011920929,81.95000004768372,0.800000011920929)。床上端80、差0、noCollision=true |
| HP / MAX / attack | **20.600000381469727 /20.6 /3**。HP/MAX期待20に不一致 |
| NoAI / PersistenceRequired | **false /true**。NoAI固定前 |
| NoGravity / invulnerable | false /false |
| target / effects / armor / equipment | null /空 /2（Zombie native値）/全slot空 |
| L2 level / initialized / ranks | **1 /true /weakness0・wither0**。期待level0に不一致、trait付与前 |
| Truth marker / externalHostiles | なし /空 |
| foreign damage / heal | 0 /0 |
| weakTime / witherTime | 200 /200。rank1/amp0の攻撃は未実施 |

**20:59:54.946761700 JST相当のfailure（原本UTC11:59:54.946761700Z）**は`source HP:20.600000381469727 !=20.0`。自動通常pause→保存終了へ進み、値を直さなかった。[原因のreadonly照合](../build/verification/l2-weakness-wither-client-20260918-204657/audit/read-only-findings.md)：補助の初期NoAIを外した際に`cap(z).reinit(z,1,false)`を残したことが準備不備。L2`MobTraitCap.init`はallowNoAI=false/NoAI=trueならlv0 bypassだが、今回は通常`TraitManager.fill`へ進みlv1。実config healthFactor=.03と`TraitManager.scale`によるhostility_health/MULTIPLY_TOTAL .03、fill末尾のnative setHealth(MAX)を実bytecodeで確認し、保存modifier/HP20.6とも一致した。製品P/Tの失敗や外部LivingHealEventと混同しない。

通常physicsの速度更新は観測したが、接地2連続/NoAI後2連続は**UNVERIFIED**。後の保存OnGround1を新規連続snapshotへ代用しない。watchdog実装は限定build済みだが、今回の停止はそのtimeoutより先のHP guardであり、両watchdogの発火成功を実測したとはしない。

#### 実施結果と観測の限界

| 項目 | 今回の判定・実測 |
|---|---|
| prepare | 実行1・本人取得/SP成功。source初期値不一致で**PREPARED未成立** |
| GUI P OFF→ON / A / B / T / C | **すべてNOT RUN / UNVERIFIED**、native hit0。期待HP17/14/11・攻撃後HUDは未観測 |
| weakness ID events | EffectUtil HEAD/RETURN、Added、remove HEAD/通知/RETURN、attack-return実instance **すべて0/未実施**。保護PASSではない |
| wither ID events | 同上 **すべて0/未実施**。DOT0も攻撃未実施のため抑止PASSではない |
| numeric / Heal / attribution | LivingHurt0/Damage0/Heal0。分離したdamageType/各UUIDのnative攻撃レコードなし。通常数値3は未検証 |
| HP/HUD / actual client | 本人server/保存/実LocalPlayer HP20/MAX20、画面20.0/20.0。実client snapshot140件、取得3つ/SP0・603/P/M ON・T OFFを受信。通常GUI切替・ケース2連続収束のPASSではない |
| native remove / 元L2 sync / actual cap | T remove0・markerなし。準備sync HEAD/RETURN各2、case=PREPARATION。実client source lv1/traits空/HP20.6。**未付与の空capをT除去成功にしない**。clientのPersistenceRequired/persistent dataはserver保存値と同一同期とは限らない |
| 対象外Remove | foodhealing:guts HEAD/通知/RETURN各2、前後実instanceなし/RETURN false。対象2 IDの実removeへ数えない |
| seal / 全OFF / 範囲外 / 再読込 / cleanup | seal・全OFFは未実施。範囲外0、再読込0、cleanup0。FAILED/P/M ON・T OFFを保存 |
| 実施主体 | GUI/command/F2/Save & Quit/Quit=**COMPUTER USE**。guard/prepare/native初期化/readonly観測/保存解析=**AUTOMATED**。HUMANゲーム操作なし |

#### 保存・終了・不変と次の1単位

- 停止直後は通常pause、F2 `2026-09-18_21.00.11.png`、Save & Quit後 **21:00:22.883 All dimensions are saved**。titleで[保存readonly](../build/verification/l2-weakness-wither-client-20260918-204657/audit/final-save-values.json)：level.dat/Data.Playerと本人UUID.datのcanonical一致。HP20/DeathTime0/MAX属性20、3取得/SP0・603/P/M ON・T OFF、effects/Inventory空。同source UUID1体、HP20.600000381469727、baseMAX20＋hostility_health .03、lv1/traits空、T markerなし、PersistenceRequired1、NoAIタグなし（false）、OnGround1。保存値へ上書きなし。
- worldへ再入場せず **21:01:13.246 Stopping!**、[21:01:14.299 PID27036不在](../build/verification/l2-weakness-wither-client-20260918-204657/audit/process-after-quit.json)を確認。[最終log](../build/verification/l2-weakness-wither-client-20260918-204657/audit/game-final.log)。OS exit code自体は未採取。CIMのprocess照会は権限拒否だったが、Get-Processで該当PID終了を確認した。Minecraft強制killなし。
- [不変照合](../build/verification/l2-weakness-wither-client-20260918-204657/audit/final-invariants.json)：製品/source/test/build/AGENTS等**146件**、旧証拠**304件**、旧2 runのroot＋instance/world **398件・追加0**、旧instance metadata **22件**すべて不変。製品/配置hash同一。`.git`なしのためgit diff確認済みとはしない。外部download・既存suite再試験・製品Config/gate変更なし。
- **次の1単位（新run実行は未承認）**：初期生成の一度だけの正式L2 level0準備を整合させるhelper限定修正。候補は`reinit(z,0,false)`だが、Config/PlayerDifficulty等を含むlv0/HP20の成立は未実測で、成功を約束しない。新run承認後にだけ実施し、自然接地2回→NoAI→trait付与→安定2回、独立200tick/wall10秒、observerの数値/帰属条件を維持する。今回sourceへの再init/HP・cap修復、旧run再利用、guard緩和は行わない。artifact不足なし。今回までの3失敗runを残し、同じlevel1初期化を繰り返さない。
- 新2 IDのGUI/A/B/T/C/sealは未確認のまま。133/133・vanilla/TaCZ60/60・Cube49/49・Invader86/86、旧4 ID/Trial限定実clientのPASSは対象当時の範囲で維持。両極意IMPLEMENTATION_PENDING/購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、他機能の個別gate、可逆クラフト増加の既知許容仕様を維持する。


<a id="l2-weakness-wither-client-level0-partial-result"></a>

### 13.10 新run — level0準備・GUI/A/B/T限定PASS、C前player安全guard停止（2026-09-18 22:16 JST）

**run `20260918-213302`：GUI/A/B/TはREAL CLIENT LIMITED PASS、C/sealはNOT RUN / UNVERIFIED。全体はSTOPPED / HELPER PLAYER SAFETY GUARD。通常保存・readonly照合・Minecraft正常終了は完了。** [承認](../build/verification/l2-weakness-wither-client-20260918-213302/audit/user-request.txt) / [実測集計](../build/verification/l2-weakness-wither-client-20260918-213302/audit/stopped-run-analysis.json) / [停止原本](../build/verification/l2-weakness-wither-client-20260918-213302/audit/failure.json)。前3失敗runは一切再利用・修復せず保全。今回の停止もreceiptを消して続行しない。製品P/T不具合を示す証拠はない。

#### 正式level0経路・補助・限定build

- [native読取根拠](../build/verification/l2-weakness-wither-client-20260918-213302/audit/level0-readonly.md)：`MobTraitCap.reinit(z,0,false)` → deinit → initの専用RegionalDifficultyModifierがcollector.base=0。falseはsetFullChanceしない。通常ChunkDifficulty callbackを置き換え、entity/structure/default設定とPlayerDifficulty寄与を通す。今回entityConfigなし、playerDifficulty0/ADD_SCALE0を実測guard。collectorのscale/variance/count等既定0・factor1、clampにmin1強制なしを確認して採用した。
- `TraitManager.fill → scale` はlevel0でも **hostility_health/MULTIPLY_TOTAL/amount0**（UUID `6fdc2572-9466-eea7-2db8-12770387f3d5`）をnative付与する。これは想定済みの無効果modifierとして保持。**modifier全不在と偽記録せず、非zero/無関係modifierは拒否**。fill末尾とINIT→POST_INITの通常初期化にnative `setHealth(MAX)` があるが、helperはHP修復しない。base/effective MAX20、HP20、attack3、attack modifier空を確認した。
- [helper最小差分](../build/verification/l2-weakness-wither-client-20260918-213302/audit/helper-changes.diff)：新run拘束、reinitの初期1回だけlevel0化、即時attribute/level guardと詳細読取、**NoAI後2新規snapshotをtrait付与前に分離し、trait後も別2組**。既存通常physics・固体隔離室・独立200tick/monotonic wall10秒・readonly/noncancellable observer・damageType/3UUID分離は維持。測定中のHP/effect/modifier/cap/物理flag修復、取得再注入、製品処理代行なし。停止後は補助を変更/再buildしていない。
- 別MOD `foodhealing_l2_ww_client_verification` 1.0：**41,850 bytes /18 entries / SHA-256 `BD6264C753CB990D109D6D21C93DF432149EAFFDF8D97644F2FF47EA8282E049`**。配置同hash、CRC/Mixin/reobf/非混入を[prelaunch](../build/verification/l2-weakness-wither-client-20260918-213302/audit/prelaunch.json)と[最終検査](../build/verification/l2-weakness-wither-client-20260918-213302/audit/final-invariants.json)で確認。
- local Gradle8.1.1＋既存cache＋`--offline`＋専用initで[初回build成功](../build/verification/l2-weakness-wither-client-20260918-213302/audit/helper-build-01.log)。[task graph](../build/verification/l2-weakness-wither-client-20260918-213302/audit/task-graph.txt)は `addMixinsToL2WwClientVerificationJar(SKIPPED) → compileL2WwClientVerificationJava → processL2WwClientVerificationResources → l2WwClientVerificationClasses → l2WwClientVerificationJar → downloadMcpConfig(offline既存cache) → extractSrg → createMcpToSrg → reobfL2WwClientVerificationJar`。製品compileJava/classes/jar/reobfJar/build/check/test/unit/GameTest/既存fixtureは含めない。新規依存取得なし。
- 製品/配置は **229,494 bytes /150 entries / SHA-256 `5C1A716EEDE7AEB4449B36EEFFBDDA156176B16B60A1676229224AA3390DB327`** 不変。実ロードMC1.20.1/Forge47.4.0/L2H2.5.19/Library2.5.3/Complements2.6.1/**Tracker0.4.4**/Curios5.12.0+1.20.1/Patchouli1.20.1-84-FORGEは[runtime](../build/verification/l2-weakness-wither-client-20260918-213302/audit/runtime.jsonl)。Trial/TaCZ/旧helper/133fixtureなし。Prism既存offline profile Leva9846をGUI選択して起動し、Microsoftの新規認証成功とは記録しない。認証ファイルの読取・コピーなし。

#### 新規環境・prepareと物理operand

- 新root `build/verification/l2-weakness-wither-client-20260918-213302`、新instance `FHR_L2_WW_Client_20260918-213302`、新world `FHR_L2_WW_20260918_213302`。本人 `<PLAYER_UUID>`、source `0264cdf6-8cbe-4209-827c-33d91c48a9ae`。新allowlist/receipt/audit、prepare1回/credit603/spent603/unspent0、P/M/T各Lv1・初期P/M ON/T OFF。取得済み状態の準備であり**GUI購入成功ではない**。正常fresh/HP20/MAX20/防御・吸収・effect・Rootなし/pendingなしを先に確認。
- 生成前GUIでSurvival/Normal/cheats ON、superflat/structures OFF、doMobSpawning=false/naturalRegeneration=falseを設定しF2保存。通常fillで床y79/屋根y84/x,z±8の閉鎖石室、source専用石室x1..3/y79..83/z−1..1、照明を準備。本人とsourceは固体壁で物理隔離、敵接近/日光/水火/窒息/落下を防ぐ。保存GameRulesも一致。[F2](../build/verification/l2-weakness-wither-client-20260918-213302/audit/screenshots/) / [保存値](../build/verification/l2-weakness-wither-client-20260918-213302/audit/final-save-values.json)。
- 正式level0初期化1回はgameTime3693、直後level0/baseMAX20/effectiveMAX20/HP20/attack3/traits空/markerなし、NoAI false・NoGravity false・invulnerable false・target null/foreign damage/heal0。[初期値](../build/verification/l2-weakness-wither-client-20260918-213302/audit/source-level0-confirmed.json)。

| 新規source観測段階 | gameTime | onGround / NoAI / traits | 全段階で保持した値 |
|---|---|---|---|
| 自然接地2組 | 3695,3696 | true / false / 空 | level0、HP/MAX20、attack3、floor差0、fall0、水/bubble/壁/火false、target null、foreign damage/heal0 |
| NoAI固定後の別2組（切替は3696の1回） | 3697,3698 | true / true / 空 | 同上。traitはまだ付与しない |
| native setTrait後の別2組 | 3699,3700 | true / true / weakness1→wither1の順 | 同上、markerなし。weakTime/witherTime各200。3700でPREPARED |

位置(2.5,80,0.5)、BBox(2.199999988079071,80,0.19999998807907104)〜(2.800000011920929,81.95000004768372,0.800000011920929)、below(2,79,0)=stone/feet・head=air、floorTop80/bbox差0/noCollision=true。自然接地時vy−0.0784000015258789、NoAI後3698はvy−0.07529536146545411であり、**速度0だったと補完しない**。いずれも通常physicsの値でguard内、NoAI後の待機では0へ減衰。全operandは[source-safety](../build/verification/l2-weakness-wither-client-20260918-213302/audit/source-safety.jsonl)、各2組のreceiptを参照。sourceの通常Zombie armor2は本人armor0と区別する。

#### 実GUI・native攻撃・ID別event

| 単位 | 実測 | 判定 |
|---|---|---|
| GUI P OFF→ON | 通常GUI→製品ToggleSkillPacket→server canonical→actual LocalPlayer。OFF収束4809、ON5464。M保存ON/T保存OFF非連動、各新規client2組 | LIMITED PASS |
| A PM ON/T OFF | tick6133、native1回、HP20→17。weakness/wither各EffectUtil HEAD/RETURN・Added・対象remove各0、source2trait保持/markerなし | REAL CLIENT LIMITED PASS |
| B P ON/M・T OFF | tick7537、native1回、HP17→14。各ID **HEAD1→Added1→RETURN1、duration200/amp0**。attack return時に両実instanceあり。同tick製品通常浄化で各 **remove HEAD実instanceあり1→Remove通知uncancelled1→RETURN true1/後なし** | REAL CLIENT LIMITED PASS |
| T GUI M ON→T ON | tick8369、攻撃0、製品Truthによるnative remove各1→元L2 sync HEAD/RETURN各1。server Version1/Mob=同UUID/Traits2 ID=true。actual client cap空、8379で新規2組収束、HP14維持 | REAL CLIENT LIMITED PASS |
| C前GUI T OFF→M OFF→P OFF | 8954で3OFF/canonical/HP14/空effects/同source空capの2組収束。marker保持 | GUI/静的状態の限定確認。**C攻撃の証拠ではない** |
| C / seal | C-FIRING/DONE・seal receiptなし、native hit合計2。期待HP11は未観測 | NOT RUN / UNVERIFIED |

A/B各 **Hurt1・Damage1・amount3・uncancelled・damageType=minecraft:mob_attack**。**sourceUUID/directEntityUUID/attackerUUIDを独立キーで記録し全て同source UUID**。全raw Hurt2/Damage2/Heal0、Wither DOT0。対象2 IDのremove数に`foodhealing:guts`の実instanceなし/RETURN falseのraw通知を混ぜない。[A events](../build/verification/l2-weakness-wither-client-20260918-213302/audit/A-events.json) / [B events](../build/verification/l2-weakness-wither-client-20260918-213302/audit/B-events.json) / [attack return](../build/verification/l2-weakness-wither-client-20260918-213302/audit/attack-return.jsonl) / [native cap](../build/verification/l2-weakness-wither-client-20260918-213302/audit/native-cap-events.jsonl)。

GUI/F2のHP20→17→14/MAX20、SP0/使用済603、M/T ON/OFF、P OFF、独立server/client canonicalとcap収束を対応付けた。[convergence](../build/verification/l2-weakness-wither-client-20260918-213302/audit/convergence.jsonl) / [2組の原記録](../build/verification/l2-weakness-wither-client-20260918-213302/audit/convergence-samples.jsonl)。Bの一瞬のweakness/wither iconは未撮影・目視PASSなし。即時存在はserver実測、実clientは受信/収束後effects空の確認。clientのserver-only persistent marker/PersistenceRequired/dropRateをserver保存値と同一と要求しない。

#### C前停止の事実・不足・保存終了

- Aのnative knockback後本人x−1.4886287、B後x−3.4772574、同source x2.5との距離が次hitの4未満guardを超えるため、§13.6.4で許容した通常 `/tp @s 0.5 80 0.5 -90 0` をC前に1回実行した。**22:07:12.507のtp直後、gameTime9745/22:07:12.530にhelper post/monitorが `unsafe player position`**。同22:07:12.577に自動通常pause。Cを予約/実行せず、guard変更・待機後再開・receipt削除はしない。
- 該当guardはplayerのonGround/非火/非水bubble/fall0/空気満量/非壁/足元stoneのAND。**失敗瞬間のplayer各operandは未記録**。failureのcached値はsource側であり、本人の判定根拠へ転用しない。後の保存では本人Pos(0.5,80,.5)/OnGround0/速度Y−.0784/fall0/Fire−20/Air300/Health14だが、後時点なので「停止瞬間の原因はOnGround」と確定しない。source全operandは安全なまま。製品P/T不具合の再現ではなく、位置復帰操作とhelperのplayer guard/観測の不足を残す。
- **22:07:30.395 All dimensions are saved**後、titleのままreadonly保存照合。level.dat/Data.Playerと本人UUID.datのcanonical一致、HP14/DeathTime0/MAX属性20、3nodeLv1/SP0・603/3OFF/effects・Inventory空。同source1体/HP20/baseMAX20＋native amount0/lv0/空traits/marker2 ID、対象外stable capを保持。[保存](../build/verification/l2-weakness-wither-client-20260918-213302/audit/final-save-values.json)。
- 再入場せず **22:08:19.144 Stopping!**、[22:08:23.229 PID12604不在](../build/verification/l2-weakness-wither-client-20260918-213302/audit/process-exit.json)。OS exit code自体は未採取。[最終log](../build/verification/l2-weakness-wither-client-20260918-213302/audit/game-final.log)。world再読込0、範囲外0、cleanup/kill0、再試行/修復0。seal完了や成功HP11の保存と偽記録しない。
- [不変照合](../build/verification/l2-weakness-wither-client-20260918-213302/audit/final-invariants.json)：製品/source/test/build/AGENTS等146件、旧証拠304件、旧3 runのroot＋instance/world606件・追加0、旧instance metadata24件すべて不変。製品/配置・外部入力Jar hash一致。`.git`なし、git diff確認済みとはしない。過去cachedVolatile例外も開始時から変更しない。
- 実施主体：通常GUI/command/F2/保存Quitは **COMPUTER USE**、prepare/native明示attack・readonly observer/保存解析は **AUTOMATED**。HUMANゲーム操作なし。自然AI戦闘・自然boss・製品GUI購入成功・新2 ID再読込/全OFF後攻撃成功は未確認。
- **次の1単位は別承認待ち**：player側の全安全operand記録と、通常歩行で同じ床の測定距離へ戻り自然接地を確認する操作を整合させる補助限定準備。今回のsource初期化/接地/observerの成功部分、A/B/T限定PASSを維持。guard/期待値を緩めず、OnGround/HP/速度等setter修復や失敗run再利用はしない。別新runでC/sealの残確認へ進むには新しい実行承認が必要。artifact不足・追加仕様質問は現時点なし。
- 既存L2133/133・vanilla/TaCZ60/60・Cube49/49・Invader86/86・旧4 ID/Trial実client PASSを再実行せず保持。両極意IMPLEMENTATION_PENDING/購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、全個別gate、可逆クラフト増加の既知許容仕様を維持する。


<a id="l2-weakness-wither-client-c-completed-result"></a>

### 13.11 同run継続 — 通常歩行後の個別安全確認、C限定PASS・seal・通常保存終了（2026-09-20 17:38 JST）

**新規測定C = REAL CLIENT LIMITED PASS。** run `20260920-163342` のprepare/A/B/T成立後、HUMAN通常歩行・ポーズから同runを継続した。Cを17:21:43 JSTに1回実行し、seal・通常保存のreadonly照合・Quit Game/PID終了まで完了。リセット後の利用者メッセージ到着直前にCは送信済みであり、その事実とreceiptを照合して再送しなかった。旧§13.10のGUI/A/B/T限定PASSは維持し、今回のA/B/TはCの開始状態を作る **PREPARATION ONLY**、新規PASS件数へ重複加算しない。旧4停止run/失敗証拠は保全。以下は今回の実測であり、§13.6–13.10の当時の未実施/停止指示は現在の指示ではない。

#### 対象・補助の最小変更と実行境界

- run root: `build/verification/l2-weakness-wither-client-20260920-163342`。instance `FHR_L2_WW_Client_20260920-163342`、world `FHR_L2_WW_20260920_163342`。本人UUID `<PLAYER_UUID>`、source `6dac2c03-f5b6-40f4-99e1-554f1285f008`、Minecraft PID1168。リセット後に新run/prepare/A/B/T/再入場を開始していない。
- 製品 **229,494 bytes /150 entries / SHA-256 `5C1A716EEDE7AEB4449B36EEFFBDDA156176B16B60A1676229224AA3390DB327`**、製品・配置Jar一致。[不変照合](../build/verification/l2-weakness-wither-client-20260920-163342/audit/final-invariants.json)。MC1.20.1 / Forge47.4.0 / Hostility2.5.19 / Library2.5.3 / Complements2.6.1 / Curios5.12.0+1.20.1 / Patchouli1.20.1-84-FORGE、実ロードTracker **0.4.4**。[runtime](../build/verification/l2-weakness-wither-client-20260920-163342/audit/runtime.jsonl)。L2単独、Trial/TaCZ/旧補助/自動fixtureなし。
- 補助だけ **44,267 bytes /18 entries / SHA-256 `A7B86765FB9FB1F770622A1D2743B4D159A50EC2E956D334D398C931C416FB55`**。新rootの `L2WwClientVerification.java` / `L2WwServerObservation.java` にplayer全operandのreadonly記録、明示walk-start/stopと停止後の新規2組・C直前fresh確認、A/B/Tの準備ラベルを追加。run/path/UUID/hash拘束・一度限りreceipt・元安全guard/期待値を維持。[差分](../build/verification/l2-weakness-wither-client-20260920-163342/audit/helper-changes.diff)。HP/effect/position/velocity/onGround修復・保護代行・trait再注入なし。測定開始後の補助変更なし。
- 既存ローカルGradle8.1.1、既存`-g` cache、`--offline --no-daemon`、専用initで補助のみcompile/reobf。[task graph](../build/verification/l2-weakness-wither-client-20260920-163342/audit/task-graph-dry-run-escalated.txt) / [11秒BUILD SUCCESSFUL](../build/verification/l2-weakness-wither-client-20260920-163342/audit/helper-build-01.log)。helper compile/resources/classes/Jar/reobfと既存MCP/SRG cache処理だけ。`downloadMcpConfig`というtask名は既存cache/offlineであり外部取得ではない。初回dry-runのnative-platform.dll読取失敗は[ログ](../build/verification/l2-weakness-wither-client-20260920-163342/audit/task-graph-dry-run.txt)に残し、承認済みローカル権限で成功。製品build/unit/check/GameTestや133/60/49/86の再実行なし。
- helper CRC/Mixin/reobf/非混入・配置hashは[prelaunch](../build/verification/l2-weakness-wither-client-20260920-163342/audit/prelaunch.json)で確認。製品/source/test/build.gradle/購入gate/製品Config/依存版は変更なし。新helperは新instance専用で、配布Jarへ混入していない。

#### 安全準備とHUMAN歩行後の個別snapshot

- 生成前GUIでSurvival/Normal、自然湧き・自然回復false、structures OFFを設定・F2記録。床壁不透明屋根を閉じた区画とsource用固体隔離室を準備。初回prepare前の通常tp1回と、今回のC前の通常歩行を区別する。C前にtp/物理setterは使用していない。fresh HP20/MAX20・防御/吸収/effectsなしを読取確認。
- 正式`reinit(z,0,false)`1回、native自然接地2組→NoAI後2組→正式weakness1/wither1付与後2組で、tick4089にprepare完了。lv0・HP20/base/effectiveMAX20・attack3、native `hostility_health` amount0を保持。本人P/M/T各Lv1、検証credit603/支出603・SP0、P/M ON/T OFF。通常GUI購入成功ではない。[prepare receipt](../build/verification/l2-weakness-wither-client-20260920-163342/audit/prepare-receipt.json) / [完了](../build/verification/l2-weakness-wither-client-20260920-163342/audit/prepare-complete.json)。
- Computer UseのW単発では必要移動ができず、HUMANへ向きを変えず通常前進→停止→Escを一連で依頼し、本人が「歩いて停止し、ポーズしました」と報告。x−3.477257391461131→0.1631912405658219、y80/z0.5。[歩行中48位置の原記録抽出](../build/verification/l2-weakness-wither-client-20260920-163342/audit/human-walk-position-samples.json)。人間へ食事・秒数/tick計測・一瞬のicon撮影は要求していない。
- **歩行後の個別snapshot**：[server tick8604 / client8603・17:17:27 JST](../build/verification/l2-weakness-wither-client-20260920-163342/audit/human-walk-post-pause-snapshot.json)。actual clientはPauseScreen/paused=true、同PID1168、HP14/MAX20・3OFF・effects空・source空traits、server側marker2 ID保持。これはC前に記録されたraw行を終了後に個別ファイルへ抽出したもので、終了後に新たなlive測定を行った意味ではない。case journalに相当するprepare/A/B/CのFIRING/DONE・truth-applied各receiptを保全し、C前の時点と後続C-DONEを混同しない。

| C前operand | 歩行停止後・新規2組・C予約直前の確認 |
|---|---|
| 位置/距離 | `(0.1631912405658219,80,0.5)`、source `(2.5,80,0.5)`、距離 **2.3368087** |
| 接地/衝突 | onGround=true、fallDistance0、石床topY80、bbox minY−floorTop0、feet/head air、noCollision=true |
| 水/窒息/火/空気 | water/bubble/wall/fireすべてfalse、fireTicks−20、air300/300 |
| 速度 | `(0,−0.0784000015258789,0)`。水平静止を確認。接地時の負のY速度を隠さず、全成分0とは記録しない |
| 本人 | HP14、base/effective/max attribute20、armor/toughness/absorption0、effects空、hurtTime/invulnerableTime0、creative/invulnerable/abilityInvulnerable=false、P/M/T3OFF |
| source/外乱 | 同UUID、lv0/HP20/MAX20/attack3/NoAI、空trait、Truth markerVersion1/weakness+witherの2 ID、外部敵対Mob空、foreignDamage/Heal0 |

`walk-stop`後の **tick9093/9094** は新しい連続2組で、過去の2組を使い回していない。[walk-stability](../build/verification/l2-weakness-wither-client-20260920-163342/audit/walk-stability.jsonl) / [walk-ready](../build/verification/l2-weakness-wither-client-20260920-163342/audit/walk-ready.json)。C予約直前 **tick9829** でも同じoperandを再確認し、次tick9830のnative攻撃前guardを通過。[C-position-ready](../build/verification/l2-weakness-wither-client-20260920-163342/audit/C-position-ready.json)。歩行・停止・比較中にHP/effect/物理flagを補正していない。guard停止/timeout/failure.jsonなし。

#### 準備A/B/Tと新規測定Cの結果

| 段階 | 実測 | 判定・位置付け |
|---|---|---|
| PREPARATION_A / tick4869 | P/M ON・T OFF、native1、HP20→17。weakness/wither EffectUtil HEAD/RETURN・Added・対象remove各0、source2trait/markerなし | C開始状態用の準備成立。旧A PASSを維持、新規PASSへ加算しない |
| PREPARATION_B / tick6099 | M OFF・P ON/T OFF、native1、HP17→14。各200tick/amp0、HEAD1/Added1/RETURN1、attack returnで実instanceあり→同tick通常浄化のremove HEAD/uncancelled通知/RETURN true各1→空 | 準備成立。瞬間付与と後消去をserver実測。Wither DOT0 |
| PREPARATION_T / tick6631 | GUI M ON→T ON、追加攻撃0。製品native remove各1→元L2 sync HEAD/RETURN各1、server marker2 ID、actual client空cap新規2組。HP14保持 | 準備成立。補助は除去を代行していない。旧T PASSを維持 |
| 3OFF同期 | GUI T→M→P OFF、server7103/client7102・sequence15265で2組収束。HP14/effects空/source空trait | C前提成立。リセット後にtoggle/A/B/Tをやり直していない |
| **C / tick9830 / 17:21:43 JST** | **3OFFの同source native1、HP14→11。Hurt1/Damage1・各amount3・uncancelled。対象2 IDのHEAD/Added/RETURN/remove各0、Wither DOT0、effects空・source空trait/marker保持** | **REAL CLIENT LIMITED PASS**。通常浄化/P保護がOFFでも、T無効化済みsourceの対象作用が戻らない |
| client/HUD | C後server9838/client9837・sequence44727の新規2組でcanonical/HP11/MAX20/effects空/同source空cap一致。F2 **17.25.34**、seal後 **17.29.25** はHUD11/20 | 実client受信後の値と画面を確認。server-only marker・dropRate・PersistenceRequiredをclientへ同値要求しない |

[A events](../build/verification/l2-weakness-wither-client-20260920-163342/audit/A-events.json) / [B events](../build/verification/l2-weakness-wither-client-20260920-163342/audit/B-events.json) / [T native events](../build/verification/l2-weakness-wither-client-20260920-163342/audit/native-cap-events.jsonl) / [C events](../build/verification/l2-weakness-wither-client-20260920-163342/audit/C-events.json) / [C-DONE](../build/verification/l2-weakness-wither-client-20260920-163342/audit/C-DONE.json) / [convergence](../build/verification/l2-weakness-wither-client-20260920-163342/audit/convergence.jsonl) / [集計](../build/verification/l2-weakness-wither-client-20260920-163342/audit/completed-run-analysis.json)。A/B/CのdamageTypeは全て **minecraft:mob_attack**、各eventの **sourceUUID / directEntityUUID / attackerUUID** を独立して記録し同source `6dac2c03-f5b6-40f4-99e1-554f1285f008`と一致。全native Hurt3/Damage3/Heal0。`foodhealing:guts`のinstanceなし/RETURNfalseのraw通知をweakness/wither実removeへ数えない。Bの一瞬のiconは未撮影・目視PASSなし。COMPUTER USE=GUI/command/F2、HUMAN=通常歩行、AUTOMATED=明示native攻撃・readonly観測/照合。

#### seal・通常保存・終了と残る範囲

- **17:29:12.678 JST / tick14819** seal成立、prepare1/native hit3（準備A/B各1＋測定C1）、SEALED。[seal](../build/verification/l2-weakness-wither-client-20260920-163342/audit/seal.json)。C後の通常knockbackでx−1.8254374551647436/80/0.5・source距離4.3254375となり自然安定。HP11のまま終了し、戻すtp/修復なし。
- 通常Save & Quit、**17:29:45.188 All dimensions are saved**後、titleのまま保存をreadonly照合。level.dat/Data.Playerと本人UUID.datのcanonical一致、HP11/DeathTime0・MAX属性20、3nodeLv1/SP0・603/3OFF/effects・Inventory空。同source1体/HP20/lv0/NoAI/空trait/marker2 ID・native amount0・対象外stable cap保持。[保存値](../build/verification/l2-weakness-wither-client-20260920-163342/audit/final-save-values.json)。保存内容の上書きなし。
- **17:30:09.506 Stopping!**、**17:30:23.384 PID1168不在**。[最終log](../build/verification/l2-weakness-wither-client-20260920-163342/audit/game-final.log) / [process確認](../build/verification/l2-weakness-wither-client-20260920-163342/audit/process-exit.json)。OS exit code自体は未採取。world再読込/範囲外/cleanup/kill/復活/修復/二重prepare/二重攻撃はいずれも0。新runの保存再読込PASSとはしない。
- [不変照合](../build/verification/l2-weakness-wither-client-20260920-163342/audit/final-invariants.json)：製品/source/test/build/AGENTS等146件、旧証拠304件、旧4 runのroot＋instance/world **853件不変・追加0**、旧instance metadata24件不変、外部入力/製品/配置Jar hash一致。旧失敗runを消さず保全。`.git`なし、git diff確認済みとはしない。
- 旧4 IDの§10.11・Trial Cube/侵略者実client、133/133・vanilla/TaCZ60/60・Cube49/Invader86は当時のPASSを保持し、今回は再実行なし。自然AI戦闘/瞬間icon目視/新2 IDの実client再読込/他trait/他版/TCP/多数entity等へ結果を拡張しない。既存133のnative chunk保存再読込と今回の通常保存readonlyは別証拠。
- **次の1作業候補**：§12.5の購入解放対象ID/版・optional不在/未対応版方針を、今回C/seal完了を反映して利用者判断用に絞るREAD ONLY整理。Cや旧A/B/Tの再試験を残件にしない。新実装・購入解放・別試験は開始していない。両極意IMPLEMENTATION_PENDING/購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、他機能の個別開始条件を維持。食料生産の極意の可逆クラフト増加は既知許容仕様・バグ修正対象外。

<a id="fantasy-ending-uom-readonly"></a>

## 14. Fantasy Ending 2.7.20 — 終焉の守護者のREAD ONLY artifact監査（2026-09-20 19:25 JST）

**以下§14.1–14.6は19:25時点の監査・提案履歴。現在結果は[§14.7–14.10](#fantasy-ending-dependency-timestop-result)。**

**当時の状態：本体受領・一致確認／本体で追える攻撃入口と分岐の静的照合済み。Food Healing対応は未実装・試験未実行。外部EndingLib/Iron's Spells境界はARTIFACT REQUIREDであり、全特殊作用の監査完了・対応完了とはしない。** 購入仕様のLOCKは§12.5。今回の承認は文書・読取記録までで、production/test/build.gradle/Jar/Config/購入gate/SP処理不変。既存133/60/49/86・全限定実client PASSを維持し、再実行0。

### 14.1 原物・依存・調査証拠

原物：`<LOCAL_DOWNLOADS>/fantasy_ending-1.20.1-2.7.20-all.jar`。**18,417,128 bytes /1,379 entries /SHA-256 E32FD4BA2E48FCF2C84F71AA07155C63D6BFA2178BB840899307DA0DA5E5D141**。mods.tomlのMOD ID=`fantasy_ending`、version=`2.7.20`、Manifest Implementation-Version=`1.20.1-2.7.20`が全て一致。`TargetRegister`で`fantasy_ending:ultimate_order_manager`→`UomWither`、`fantasy_ending:star`→`StarEntity`のfactoryを確認。日本語の呼称「終焉の守護者」は利用者指定で、判定キーには使わない。

[現物記録](../build/verification/fantasy-ending-readonly-20260920-185700/audit/artifact.json)／[mods.toml](../build/verification/fantasy-ending-readonly-20260920-185700/audit/META-INF_mods.toml)／[同梱metadata](../build/verification/fantasy-ending-readonly-20260920-185700/audit/META-INF_jarjar_metadata.json)。
調査rootは`build/verification/fantasy-ending-readonly-20260920-185700`。新しいゲームrun/instance/worldではない。変更前3文書を`before/docs`へ保全。原物と同hashの調査用readcopyを使用し、外部classの初期化をしないjavap静的逆アセンブルのみ実施した。Downloads原物の最初のjavapは出力後close時にAccessDenied/exit4となったため、原物を変更せずreadcopyへ切り替えてexit0を確認。追加出力時の文字encoding失敗もUTF-8で再取得した。ゲーム起動・compile/test失敗ではない。

| 依存・候補 | 実宣言/実参照 | 現物・現在の判定 |
|---|---|---|
| JavaFML / Forge / Minecraft | loader `[47,)`、forge `[47,)`、MC `[1.20.1,1.21)`、BOTH | 既存MC1.20.1/Forge47.4.0を将来候補とする。今回ロード未実施 |
| Curios | mandatory `[5.4.5,)` BOTH | 既知参照modsの **5.12.0+1.20.1**をmetadata/Manifest確認。SHA256 `E2EDACD8DD16FB4172B517A2FA1B642DFFF45B6E94712E468ED5B8F7D673AA03`。範囲を満たす |
| Iron's Spells (`irons_spellbooks`) | mandatory **`[1.20.1-3.15.3,)`** BOTH | 既知配置では未発見。**ARTIFACT REQUIRED**。受領後に自身の必須依存も照合する。下限を満たす版全ての実証や具体的採用版の決定はしていない |
| EndingLib提供artifact | `com.mega.endinglib.util.time.TimeStopUtils/TimeStopEntityData`、Mixin pluginの親`util.mixin.ApplyCheckMixinConfigPlugin`、`mixin.accessor.AccessorLivingEntity/AccessorSynchedEntityData`等を実参照 | 本体/同梱Jarにも、限定確認した配置にもclassなし。**ARTIFACT REQUIRED**。mods.tomlにEndingLibのID/要求版宣言なし。必要なのはFE2.7.20と整合するこれらのclass/APIを含む実Jar。正確なファイル名・版は未確認で、架空の最低版を作らない。時間停止以外のロード/HP処理にも関係する |
| GeckoLib | `UomWither`がGeckoLib APIを直接参照するが、本体mods.tomlに要求版なし | 参照modsのTaCZ Turrets内に4.8.3、承認SW原物内に4.4.6を発見。独立したFE用採用artifact/互換性は未確認。取り出し・配置・無関係な親MOD導入なし。Iron's Spells自身の要求と合わせて後続で決める |
| 同梱2 Jar | mixinextras-forge **0.4.1**／要求`[0.4.1,)`、agent-any **1.0**／要求`[1.0,)` | 本体のJarJar候補。外側へ重複配置しない。実ロード選択版は未観測 |
| optional宣言 | slashblade `[0.1.1,)`、goety_revelation `[2.3,)`、l2artifacts `[0.0,)` | mandatory=false。今回の必須追加物へ昇格しない |

[限定配置一覧](../build/verification/fantasy-ending-readonly-20260920-185700/audit/local-dependency-inventory.json)はlibs、run/mods、既知source-world-boot/source-runtimeのmods、Downloads直下のみ。外側Jarと内側候補の計383 ZIP調査項目（同じ原物のコピーを含む）でmetadataと提供class名を確認。PC全体・認証・world・全cache走査なし。別MOD同梱の候補と、今回起動時に選択される版を混同しない。

### 14.2 実攻撃graphと帰属

略記UOM=`com.mega.uom.common.entity.boss.uom.UomWither`、Star=`com.mega.uom.common.entity.StarEntity`。SRG原文は[bytecode](../build/verification/fantasy-ending-readonly-20260920-185700/audit/bytecode)、既存MC1.20.1 mappingsによる可読名置換は[mapped](../build/verification/fantasy-ending-readonly-20260920-185700/audit/mapped)。offsetは原物の命令offset。71 classの静的出力、[状態参照index](../build/verification/fantasy-ending-readonly-20260920-185700/audit/state-write-search-index.json)、[定数pool参照index](../build/verification/fantasy-ending-readonly-20260920-185700/audit/constant-pool-reference-index.json)を保存した。参照検索件数を試験件数/PASSにしない。

| 入口→到達先 | 帰属・side・結論 |
|---|---|
| UOM.registerGoals→BaseEntity.registerGoalsのRangedAttackGoal→UOM.performRangedAttack→`_performRangedAttack` | UOMをconstructor ownerに渡し、`setOwner(this)`した **vanilla WitherSkullを6発**生成。Starと別。server AI経路 |
| UOM.customServerAiStep→BaseEntity.customServerAiStep→左右頭のBaseEntity.performRangedAttack | 同じ正式owner付きvanilla WitherSkull。通常発射/危険頭蓋・爆発numericを維持。side-headも漏らさない |
| BaseEntity.attackSelector→StarMagicAttackGoal.tick→UOM.starMagicAttack→starAttack | UOM-owned Star/SeekStar生成。接近時には別途DS numeric・motion/hurtMarked同期がある。これを効果防止で消さない |
| UOM.customServerAiStep→MultiStarAttackGoal.tick→starMagicAttack | 同じStar系列へ集約。`SeekStarEntity`は探索挙動の差で、独立したeffect付与overrideなし |
| Star.tick→onHit→onHitEntity または周辺AOE | owner=getOwner、直接hit対象とAOE対象を別処理。AOEは直接hitの同UUIDとownerを除外。server処理 |
| DreamShadowBeamAttackGoal.tick→UOM.dreamShadowBeam | server側から呼ぶ。正常hurt成功時の2効果のみ副作用候補 |
| SplashAttackGoal.tick / lambda$tick$0/$1 | target/周辺のFE numeric・EntityActuallyHurt・HP delta・押し出しとmotion packet、演出。WitherBoss向けfreeze **DamageSource**分岐はplayer凍結flagではない。MobEffect付与なし |
| EldritchBeamAttackGoal.tick→eldSpell | UOMをownerとしてIron's Spellsの`EldritchBlastVisualEntity`を生成。`DamageSources.applyDamage`＋EntityActuallyHurt＋負HP delta。FE本体内に新MobEffect付与なし。外部visual/damage実装はIron's Spells原物待ちで、class名だけで純演出と断定しない |
| UOM.hurt→actuallyHurt（m_6475_） | powered時のattacker PlayerへHARM。別途30% numeric反射。正当な受傷計算/反射を保持 |
| UOM.baseTick→finalSkillAttack | server、finalSkillComing中・残時間1460以下、method側61以上/tickCount≥2。生存targetのBAN_HEALINGとFE/delta numeric、死亡後のHP整合処理を分離 |
| customServerAiStep / updateSkill→TimeStopSkillGoal.timeStop | serverでUOM自身のtimeStopCount=180→use(true,UOM)。被害playerの選別/停止処理はEndingLibへ |
| UOM$UomEntityData.set→timeStopSkillGoal.timeStop | serverでUOMの計算後HP≤2、attackTime<200、targetあり、最終技前に発動する別入口。attackTime=480。AI goal経由だけを監査して終わらない |
| summonEffect、phase/final render packet、UOM自身のdodge/移動/回復/無効effect map | summon時lightningはvisualOnly=true。本人被害ではないboss自己状態と演出を保持。通常Numeric death/lootはEntityActuallyHurtへ到達する |

### 14.3 敵対secondary候補と最小call-site

**本体から確定できた新規MobEffect防止候補は下表FE-P1〜P5の計6 addEffect命令。FE-P6は外部未確認。** 全て対象本人ServerPlayer、正常canonical/非pending、通常浄化Lv1＋極意Lv1取得/双方ONが条件。Truthは条件に含めない。UOMは登録EntityTypeとの同一性を確認し、投射物は同levelの正式getOwnerを確認する。名前・namespace・DamageTypeだけで帰属を作らない。

| ID | 実method / offset（addEffect） | 実効果・条件 | 防ぐ箇所 / 保持するもの |
|---|---|---|---|
| FE-P1 | UOM.dreamShadowBeam(Entity) **79/97** | `minecraft:slowness`40tick amp4、`minecraft:poison`100tick amp3。LivingEntityへのDS hurtがtrueの場合 | 2回の新規付与だけ。DS入力 **10+randomFloat×5**、事前invulnerableTime処理、beam演出を保持。実HP差は既存防御等の結果を別観測 |
| FE-P2 | Star.onHit(HitResult) / m_6532_ **313** | 周辺LivingEntity（owner/直撃同UUIDを除外）、未exploded/server、Math.random()<0.5で `fantasy_ending:ban_healing`60tick amp7。難易度条件なし | **ownerがUOMの時だけ**付与抑止。後続DS hurt＋UOM向け追加FE EntityActuallyHurt、爆発/通常motionを維持。Star全体・method全体をcancelしない |
| FE-P3 | UOM.finalSkillAttack() **340** | 実範囲選別後の生存LivingEntity、creative/spectator除外。BAN_HEALING20tick amp6 | 新規付与だけ。負HP delta、FE入力の反復、setHealth、死亡処理を保つ。通常HP20で安全な人力陰性試験とは決めない |
| FE-P4 | UOM.actuallyHurt(DamageSource,float) / m_6475_ **334** | customInvulnerableTime≤0、server/最終技前、受傷除外条件通過後 **isPowered()**、source.getEntityがPlayer。HARM=`minecraft:instant_damage`1tick amp2 | 当該Playerへの付与だけ。後続magic反射numeric（その時点の計算済みamount×0.3）を維持。isPoweredは専用dimension又はHP≤maxHP/2+50で、secondPhase booleanだけではない |
| FE-P5 | **vanilla WitherSkull.onHitEntity / m_5790_ 188** | 上記UOM発射の正式owner、hurt成功/LivingEntity、Normal:WITHER200tick amp1、Hard:800tick amp1。Easy/Peacefulは当該付与なし | 本人かつ正式UOM ownerの1命令だけ。元witherSkull hurt入力8、owner heal/enchantment、爆発numericを維持。他Wither/他ownerへ免疫を漏らさない |
| FE-P6 | TimeStopSkillGoal.timeStop **14/19**→外部 | `TimeStopEntityData.setTimeStopCount(LivingEntity,180)`、`TimeStopUtils.use(boolean,LivingEntity)` | **ARTIFACT REQUIRED**。ここでbossのuse全体をcancelすると他playerも救済してしまうため不可。EndingLibの対象別判定・S/C tick/input/packet・解除/複数停止者の所有関係が分かるまでhookを確定しない |

**開始候補との差異：UOM-owned Starの直撃4効果は到達しない。** Star.onHitEntityはoffset72–118のUOM owner分岐でFE EntityActuallyHurt0を行い、**goto436**で4効果を飛ばす。4効果の実値は、非UOM ownerかつhurt成功、Normal/HardでWITHER200/800tick amp1、WEAKNESS150/600tick amp1、BLINDNESS100/400tick amp1、INSTANT_DAMAGE1tick amp1。Easy/Peacefulはなし。offset321/350/379/404。これらは他playerの武器/魔法にも使われる**保全すべき負対照**であり、UOM最低対応の4hookに数えない。SeekStarもこの分岐を継承する。

BAN_HEALINGは`BanHealingMobEffect`上は**NEUTRAL**。実作用は`SyncEntityDataMixin.set`で対象health data ID、Float/Double、正のhealth increaseに対して
`old + (requested-old) * clamp(1-(amp+1)*0.1,0,1)`。
amp7は約20%通過（80%抑制）、amp6は約30%通過（70%抑制）。health index未取得時はmapから一時退避→context reload→元effectを戻す分岐もある。ここをglobalに改変せず、上記2つの由来確認済み新規付与入口で防ぐ。既存の別source BAN_HEALINGのduration/amplifier/回復阻害を治療しない。このMixinにはside guardがないため、client描画値とserver確定値を同一視せず、将来実測する。

### 14.4 対象外・数値境界・未確認事項

- **正当numericを維持**：DS=`fantasy_ending:ds_power`、FE=`fantasy_ending:fe_power`の実ResourceKeyを確認したが、両方ともplayer武器等でも使われるのでglobal免疫キーにしない。StarのUOM直撃FE、AOEのDS/FE、Dream Beam、Splash、Eldritch、反射、finalの負delta/HP書換えを保持する。
- **raw HealthだけをHPとしない**：EntityASMUtil.special_getHealthは条件により`min(raw,maxHealth+FE healthDelta)`を返す。healthDeltaはSynchedEntityDataで保持し、別に通常Health、maxHealth attribute、isDead/context、HUDがある。finalは生存playerに `-3-maxHealth×0.002` のaddDeltaを2回行う経路を持つ。これは利用者が今回保持指定した直接HP/numeric側であり、Trial Soul上限保護を転用しない。HP減少・頭打ちだけで「MobEffect防止FAIL」「全damage免疫PASS」を決めない。
- invulnerableTime=0、hurtTime、noActionTime、sleep解除、combat/armor/death/loot等は実攻撃処理に含まれる。付随数値攻撃の成立を変えるため、6つのaddEffect抑止と一括cancelしない。EntityActuallyHurtの外部Accessor実装は不足として残し、数値層の完全な実動作を静的結果で保証しない。
- movement：StarMagic/Splashのvelocity変更・ServerPlayer motion packetを確認。通常knockback/motionはLOCKに従い保持。UOM.dodge/最終技の固定位置はboss自身。TimeStopの非MobEffect controlはこれらとは別。
- **Health Reverse/Overwhelming**：直接のUOM/Star攻撃付与はなし。ただしEntityActuallyHurt→createWitherRoseにはUOM kill後のTwistedFlower系生成という間接経路がある。`TwistedFlowerBlock.entityInside`は所有者を持たずWITHER＋OVERWHELMINGを付与する環境接触、HealthReverseは花の効果指定/別弓・自己spell等で参照。**「同MODだから全否定」も「Jarに全く経路なし」も誤り**。現契約の本人/正式owner付き攻撃Entity帰属を持たない花接触は今回Adapter対象に追加しない。花の由来を後付けで推定/marker化しない。
- `ShockwaveStarEntity`の独自爆発演出にはBallLightning/MagicFireball等への外部呼出しがあるが、実生成参照はVoidCollapsingBow側。UOM.starAttackが生成するのはStar/SeekStarで、ShockwaveをUOM攻撃として追加しない。`DreamShadowArrow`も今回のUOM生成入口に接続されていない。プレイヤー武器・SlashBlade・花・別bossの既存効果を保持。
- UOM/Star/到達goalの本人作用候補をaddEffect/forced/remove/map、health/SynchedEntityData/delta、TimeStop/movement、inventory/cooldown/capability/persistent/abilities/modifier、teleport/fire/freeze/food/exhaustion/killで照合。**本体内の確認した入口では、上記以外の独立したplayerの取得/SP/持物/food/abilities/modifier書換えや敵対effect解除は確認しなかった**。boss自身のmap・projectileのexploded・外部numeric内部・武器専用処理と区別。EndingLib/Iron's Spellsの未読取部分へこの不存在判定を拡張しない。
- 時間停止のS/C影響、停止中のGUI/packetが受理されるか、重複停止者、解除と保存値、Iron's Spells外部attack/visual内部、未確定依存の実ロード版、FE+Food Healing実変換後のcall-site/HP同期は未確認。FE内time系LevelExpandedContextのtickHeadは空で、EndingLibの代わりの実装ではない。global時間停止の取消・他player救済になる回避案は採用しない。

### 14.5 実装候補・自動検証・最小実client案（19:25当時の未承認案／後続承認は§14.7、未実行）

**推薦する次の1実装単位：UOM由来の新規MobEffect付与6 call-siteだけの本人保護。** FE-P1–P5は同じ「呼出し元/正式owner＋server本人条件」でまとまり、numeric経路を変更せず進められる。TimeStopは別単位/外部調査待ち、ただしP全体readyには残る。先に不足artifactの受領/追加照合が必要であり、現状のまま実ロード・native統合可能とはしない。今回実装開始は未承認。

予定差分は`compat/fantasyending/FantasyEndingCompatibility.java`（共通本人条件と登録source/owner）、`FantasyEndingMixinPlugin.java`（不在/未対応版無効）、`mixin/fantasyending/{UomWitherMixin,StarEntityMixin,UomOwnedWitherSkullMixin}.java`（実call-site限定）、その専用Mixin json/refmap設定と焦点試験。名前は予定で未作成。vanilla WitherSkull用もFE承認版/正式UOM owner/本人だけに絞る。PControllerの条件を弱めず、共通LivingEntity.addEffect/SyncEntityData/全damage/eventをcancelしない。戻り値が後続numericの分岐を変えないことを実bytecodeで確認する。必要な依存版が分かる前に架空gateを作らない。購入gateとT効果はこの単位に含めない。

自動試験の受入条件は[TEST_PLANのFE節](TEST_PLAN.md#fantasy-ending-uom-tests)へ集約。各native入口で有効本人だけ新規付与0、親OFF/極意OFF/未取得/pending/他player/mob/他owner/版不一致は元処理、numeric/death/既存effect保持を確認する。Star直撃のUOM分岐0 effectと非UOM4効果を別対照にする。Math.random分岐は補助が製品期待値を変更せず、有限反復で「付与分岐に入った」観測と不成立を分ける。試験用RNG制御が必要なら別fixture内の手段を明示し、実clientの自然分岐と混同しない。式計算/偽DamageSourceだけをnative統合PASSにしない。

**実client最小案（依存・native自動結果を得てから詳細実行承認）**：
1. 新instance/world、MC1.20.1/Forge47.4.0/採用済みFEと必要依存だけ。生成前の自然湧き/自然回復OFF、Survival/Normal、閉鎖安全区画。別検証補助のrun/world/hash/UUID限定・prepare1回で通常浄化Lv1＋極意Lv1のみ、credit103/支出103/SP0。Truthなし。HP20/MAX20、FE delta0、既存効果/防御/Rootなしを**読取**確認。boss自然戦・全goalを走らせず、専用actorの明示native呼出しだけを制御する。製品保護の代行/HP修復/既存effect消去は禁止。
2. **GUI切替とDream Beam単発の代表確認を第一候補**とする。親だけOFF→ONで極意保存ON不変、server/client canonical収束を確認し、両ONでUOM.dreamShadowBeam本人1回。native入力10≤D<15、hurt成功・slowness/poison呼出し抑止、通常numericが通りHP/HUDが減ることを同一timestampで観測。既存防御後のHP差は実自動基準と比較し、事前に10〜15を最終HP差と決めない。FEの最大入力/変換後がHP20から非致死になることを自動結果で確認できなければ**人間へ発動させない**。
3. 実client陰性対照は安全性確認後だけ、別freshの本人条件で単発を計画する。通常浄化が残る陰性では「即時付与→tick後消去」をobserverで捕捉し、人間へ瞬間icon撮影を要求しない。既存のdamage/effectをsetterで消して同じrunを再利用しない。陰性やAOE/final/反撃の全再現は自動側を基本とし、最大HP20で安全に出来る根拠のない連続攻撃・反撃HARM死亡試験を実client必須にしない。
4. BAN_HEALINGと時間停止はそれぞれ同期機構が別のため、Dreamだけで全作用実client PASSにしない。回復阻害は実native付与/既存effect保全と通常回復経路の分離観測を自動で先行し、安全な少数clientケースを後続確定。TimeStopはEndingLib実物で本人除外と他者/全体停止の区別が設計できるまで**UNVERIFIED**。bossのuseを丸ごと止めて画面が動くことをPASSにしない。
5. 観測はreadonly：server/client canonical、skill toggle、実effect ID/duration/amp、追加試行と実追加・通常浄化除去の順、raw Health・getHealth・MAX attribute・FE delta・HUD、source/owner UUID/registry、damage source/amount/回数、時刻。無関係被弾・回復・actor暴走・想定外の値/同期不収束なら次発を止め通常pause/退出、receipt/journal保持。
6. seal→own actorの安全停止/cleanup→通常Save & Quit→保存競合しない時点のreadonly NBT/log→Quit Game/process終了。新規永続stateを作らない6付与入口の確認では、通常保存値照合を基本とし、**同world再読込を自動で必須追加しない**。既存BAN_HEALINGやFE deltaの保存を治療して合わせない。TimeStopの保存/終了条件は実物判明後に別途評価。

### 14.6 19:25時点の不足と停止位置（現行は§14.10）

- **ファイル待ち**：FE2.7.20対応EndingLib実Jar（上記class/API、正確版未確認）、Iron's Spells `irons_spellbooks [1.20.1-3.15.3,)` を満たす承認版＋そのmetadata上の必要依存。GeckoLibは既存同梱候補を発見済みだがFE構成として未採用、勝手な展開/別版取得なし。
- **仕様判断**：§12.5の2問は解決済み。6つの新規付与を守りnumericを保持する範囲には再質問不要。ownerを持たない花/武器やnumericそのものの追加免疫は今回採用しない。EndingLib読取後に本人だけ安全に除外できない具体的制約が判明した場合に限り、差・影響を提示する。
- **実装開始承認待ち**：§14.5の1単位。今回コード・helper・Mixin作成/compile/build/test/game起動は0。PはFE最低範囲の実装＋必要検証完了前にready化しない。Tも回答だけでgate解除しない。
- **維持**：両極意IMPLEMENTATION_PENDING/購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、他機能の個別開始条件。旧Trial/L2/weakness/witherの全限定PASS、run20260920-163342 C/seal/通常保存終了・prepare1/hit3、旧停止証拠を変更しない。可逆クラフト増加は既知許容仕様・バグ修正対象外。



<a id="fantasy-ending-dependency-timestop-result"></a>

### 14.7 承認6 artifact・依存closure・isolated loader結果（2026-09-20 21:07 JST）

**受領/STATIC AUDITED、dependency専用dedicated loader smokeは限定PASS。FE P AdapterはNOT IMPLEMENTED、native攻撃/TimeStop統合はNOT RUN。停止判定は§14.10。** 本フェーズの利用者は実装・自動検証まで承認したが、TimeStop本人限定方式の安全性を立証できなければ停止する条件も指定した。§14.1–14.6の実物不足・未承認は19:25当時の履歴であり、今回の継続を止める理由へ再利用しない。

原物は全て `<LOCAL_DOWNLOADS>/` の下記ファイル。サイズ/entries/Manifest/mods.toml/SHA-256を再測定し、利用者指定値と全6個一致。原物不変。[全文metadata・nested情報](../build/verification/fantasy-ending-20260920-204205/audit/input-artifacts.json)。

| 実artifact | size / entries | loader ID/version | SHA-256 |
|---|---|---|---|
| fantasy_ending-1.20.1-2.7.20-all.jar | 18,417,128 / 1,379 | fantasy_ending / 2.7.20 | E32FD4BA2E48FCF2C84F71AA07155C63D6BFA2178BB840899307DA0DA5E5D141 |
| EndingLibrary-1.20.1-2.1.19fix-all.jar | 2,409,255 / 891 | ending_library / 2.1.19fix | 0E29AF51DABD4E0EA8A315C1886E61F3ABEE62045F196EB6913D7F4DBA6CDC34 |
| irons_spellbooks-1.20.1-3.16.3.jar | 15,570,397 / 4,663 | irons_spellbooks / 1.20.1-3.16.3 | 54B5AAA52887C38F570FBD820288171234D8541C17A5E953F9271189FDD480FB |
| curios-forge-5.14.1+1.20.1.jar | 398,066 / 242 | curios / 5.14.1+1.20.1 | 1E817919A35B37CF30524AAEC73F0CA5130452F23F168F844854DF282EB8E51F |
| geckolib-forge-1.20.1-4.8.2.jar | 1,038,979 / 444 | geckolib / 4.8.2 | A2E4BCC986CE360F4E85D545A86B04C8C9350543EBD17069B6A44C0A8939FFDC |
| irons_lib-1.20.1-2.1.0.jar | 428,969 / 299 | irons_lib / 1.20.1-2.1.0 | DB4C7CC853C0FFB1D36A0F19AF22C224A8608B2A55C747ADC70FB44455BAF097 |

MC1.20.1/Forge47.4.0を使用。FEのForge[47,)/MC[1.20.1,1.21)/Curios[5.4.5,)/Spells[1.20.1-3.15.3,)、SpellsのForge[47.4.0,)/PlayerAnimator[1.0.2-rc1+1.20,)/GeckoLib[1.20.1:4.8.2,)/Curios[5.14.1+1.20.1,)/IronLib[1.20.1-2,1.20.1-3)、IronLibのForge47.4.0+/同GeckoLib、EndingLibのForge[47,)/MC[1.20.1,1.21)を照合。**この最小構成の追加必須artifact不足は見つからなかった**。FEが参照するEndingLibの35 class全てに提供元あり（[class closure](../build/verification/fantasy-ending-20260920-204205/audit/endinglib-class-closure.json)）。任意ModernUI/SlashBlade/GoetyRevelation/L2Artifactsは追加せず、全ての遅延攻撃APIの実行保証とは区別する。

新規 `fantasy-ending-20260920-204205/loader-smoke` のみ。外側modsは承認6個＋baseline Food Healing7個、nestedを外側へ展開/二重配置していない。実採用は以下（[loader原ログとの照合](../build/verification/fantasy-ending-20260920-204205/audit/loader-smoke-reviewed.json)）。

| nested resource | 実採用・区別 |
|---|---|
| playerAnimator-658587-4587214.jar | ending_library内、MOD playeranimator **1.0.2-rc1+1.20**。JarJar artifactVersionの**4587214**とは別 |
| mixinextras-forge-0.4.1.jar / MixinExtras-0.4.1.jar | mod mixinextras **0.4.1**。class-loadではForge pluginがFE内、bootstrapがEndingLib内のさらに内側resourceからロード。内側automatic module MixinExtrasのloader表記は**0.0NONE** |
| mclib-20.jar | GeckoLib内、JarJar artifact **20**。loaderのautomatic module mclibは**0.0NONE** |
| agent-any-1.0.jar | FE内、JarJar artifact **1.0**。loaderのautomatic module agent.anyは**0.0NONE** |

20:44:30起動/PID4076、20:44:56 `Done`、20:47:30通常`stop`、20:47:31全dimension保存/Java exit0。**監視補助は `] Done (` を検索し、実ログ `]: Done (` と不一致のためtimeout/FAILを出した**。180秒timeoutで通常stopを送信したので、即時ready検出成功/save-all成功とは書かない。[元FAIL/終了receipt](../build/verification/fantasy-ending-20260920-204205/audit/loader-smoke-process.json)は上書きせず、原ログの起動成功と通常保存/終了に基づくloader限定判定を別記録。再起動/再試験なし。

更新照会を無効化しremote HTTP(S)を127.0.0.1:9へ固定。IronLibのPatreon照会はConnectExceptionで遮断され、ダウンロード成功なし。localhostの空公開鍵応答は試験用で実認証PASSではない。FE agentの自己attach、optional OdamaneHalo警告、WMI AccessDenied、既定config補完等のログは保持し、全WARN解消とはしない。製品Config・既存world/instanceには介入しない。

### 14.8 EndingLibrary 2.1.19fix TimeStop — 実call graphと所有情報の限界

証拠は[全6 artifactの参照class索引](../build/verification/fantasy-ending-20260920-204205/audit/time-stop-all-artifact-reference-index.json)、[実javap出力](../build/verification/fantasy-ending-20260920-204205/audit/bytecode/)。可読版のMCP名変換は表示だけで、元bytecode/Jarを変更しない。以下は**STATIC AUDITED**、native TimeStopの実行試験ではない。

| 部分 | 実処理・介入時に維持する条件 |
|---|---|
| UOM開始 | `TimeStopSkillGoal.timeStop(UomWither)`はserverでUOMのcount=180→`TimeStopUtils.use(true,UOM)`。P以外のplayerも止めるため、このuse全体取消は不可 |
| use / 延長 | 5引数版`use(Z,LivingEntity,Z,I,Z)`へ集約。enableTSチェック、server限定。global `isTimeStop`とdimension Setを変更し、SkillPacket(source entity ID)＋DimensionPacketをallへ送る。開始countはmax(既存count,duration)。解除では同levelの他alive/count>0を探索し、あれば自分のcountだけ0にして**packet送信前にearly return**可能 |
| server側識別 | global boolean、dimension Set、entity別countしかない。正式なactive-source UUID/開始epoch/終了理由の集合はない。count>0はnative終了判定にも使うが、公開setter・保存読込でも作られ、開始呼出しと一対一の証明ではない |
| 本人のcanMove | `TimeStopUtils.canMove(Entity)`はcreative/spectator、または`TimeStopEntityData.canMove`→Living capabilityの`TIME_STOP_CAN_MOVE`。`setTimeStopCount`がcount>0からflagも更新。Pのために偽count/flagを渡すと外部停止者/権限を兼ねてしまうので採用不可 |
| entity tick / 入力 | `ServerLevelExpandedContext.tickHead`は停止levelで元tickをcancelし、canMove entityのみ手動tick。`LevelMixin.guardEntityTick`、CommonEventHandlerのattack/interact、EndingLibのIron AbstractSpellMixinもcanMove判定。global world time/neighbor更新は停止維持 |
| client | `TimeStopSkillPacket.handle0`→Wrapped.enableはsource IDを本人向け音判定に使い、継続中のsource集合として保持しない。TSDimensionSynchedPacketはadd/remove dimensionのみ。ClientLevelExpandedContextも停止dimensionのみ。`MinecraftMixin.runTick_modifyPartial`は別timerからcanMove entityを手動tickし、GUI/入力/renderの主要箇所もcanMoveを参照。sound/particle/world time等はglobal判定で、本人権限の代わりに全体を戻してはいけない |
| 別stopper / 複数player | FE自身の`TimeStopSpell`、`Triggers.TimestopTrigger`→C2S TimeStopUtilsPacket、EndingLib command/APIも同じuseを使う。単なるFE namespace/最後の開始packet/UOM存在だけではUOM停止専用にならない。複数source時は停止集合と各終了を追う必要がある |
| 通常終了 | LivingEntityMixin.tickがserver countを減算し、0でuse(false)。EntityMixin.setRemovedもcount>0ならuse(false)、levelにplayerがいない場合はglobal flagをtrueへ書く追加分岐あり。ServerLevelExpandedContextにもsource不在時のglobal解除分岐があるため、useの入退場だけを全状態遷移とは扱わない |
| lifecycle | player dimension changeはcount0/use(false)、死亡LOWESTはuse(false)、logoutはcount0と本人への停止終了packet、loginはsource ID **-1**の開始packet。追跡範囲外/再入場で元sourceをclient IDだけから復元できるとはしない |
| capability sync | `TIME_STOP_CAN_MOVE`はdefineWithoutSerializationの同期data。setValueでdirty→ServerEntityMixinのpackData/SeenBy packet、client側assignValues。count自体とowner集合の同期ではない。既存foreign flag/countを変更・没収しない |
| 保存 | Living capabilityは`TimeStopCount`保存、deserializeでcount>0からflag再構成。source provenance/epochは保存されない。TimeStopSavedDataはdimension Setを読むが、実save bytecodeは構築したListTagを引数CompoundTagへputしていない。これは静的所見で、保存バグを今回再現・修正したという意味ではない |

### 14.9 Iron's Spells 1.20.1-3.16.3 — Eldritch追加照合

`EldritchBeamAttackGoal.eldSpell`の実生成descriptorと実物`EldritchBlastVisualEntity`コンストラクタが一致。コンストラクタは位置/距離/向きを設定、tickはclient煙particleと寿命後のdiscard、spawn dataは距離のみ、保存対象外。引数UOMは向きに使われ、visual内に継続ownerを保存する構造ではない。FEの同class Mixinに追加処理はなかった。

実damageは`causeDeathDsDamage(UOM)`の`EasyDamageSource extends DamageSource`で、Ironの`SpellDamageSource`ではない。`DamageSources.applyDamage`のoffset135–141から元`Entity.hurt(source,amount)`へ入り、spell専用effect/fire/iframe/NoKnockback分岐を通らない。postHitEffectsのspell専用分岐も同じ型条件で非到達。UOMはIMagicSummonでなく、召喚damage補正にも該当しない。別の既存knockback stateを初期化する案は採らない。

`HP*0.02 + random*4 + 8`、FE actuallyHurtの`random*4+6`、負Health delta `-HP*0.02-8-random*4`は元numericとして保持する契約。`MagicManager.spawnParticles`は各server playerへ通常particle送信を行う経路で、被害者のmagic/cooldownを変更する呼出しではない。**この実経路の確認範囲では追加secondaryなし**。全Iron's Spells攻撃や任意event subscriberへの保証ではなく、native Eldritch実統合はNOT RUN。

### 14.10 21:07時点の停止位置と再開条件（現行は§14.14）

**BLOCKED - TIME STOP SOURCE OWNERSHIP**。artifact不足やloader失敗ではない。受領・loader成功と、P本人限定TimeStop対応の成立を分ける。

現行APIだけの「canMoveがfalseならPでtrue」では停止源を限定できない。最後のsource packetを記録するだけでは、UOM開始前の別stopper、途中の競合、early-return解除、loginのsource=-1、保存から復元したcountを区別できない。開始/終了をFood Healingの一時台帳へ記録しserver発の本人許可を同期する案も検討したが、**公開count setter/deserialize/use以外のglobal変更を含む全状態遷移と再接続を覆う所有情報の完全性を立証できていない**。不明状態で無条件許可すれば他sourceからも解放し、全て拒否すれば必要なUOM本人保護を満たしたとはいえない。

これは安全実装が将来も不可能という数学的断定ではなく、**今回実物から安全に成立すると証明した実装方式がない**という停止判定。単にowner fieldがないだけで永久不可能と結論しない。global解除、偽count、外部capability/NBT書換え、他player解除、clientだけ免疫という代替は実装しない。6 MobEffect siteも今回未実装のまま保全し、FE完全対応/ready化しない。

次の1作業は、今回のcall graphを前提とした**source別の一時所有台帳・server→client許可/失効の完全性の設計検証**。開始成功/途中例外、直接count変更/復元、複数dimension/source、logout/death/再接続、原global/dimension/capability不変について、未知状態をどう判別しUOM本人条件と両立するかを証明する。証明できるまではproduction接続しない。既存の一括実装承認を「通常工程の再承認待ち」へ戻さず、仕様を緩める判断や新artifactが本当に必要と判明した場合だけ別途提示する。受領済みJarの再提供やloader smoke再実行は不要。

今回：6site/TimeStop production差分0、FE gameplay統合**0件/NOT RUN**、build/unit/check/既存回帰**再実行0**、Jar再生成なし、実client/Prismなし。旧L2 133/133・core60/60×2・Cube49/49・Invader86/86と全既存限定client PASSは保持。baseline229,494 bytes/150 entries/5C1A716E…DB327は不変。両極意IMPLEMENTATION_PENDING/購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、個別開始条件・既知許容の可逆クラフト仕様を維持する。


<a id="uom-timestop-ownership-design-verification"></a>

### 14.11 UOM TimeStop source ownership — verification-only設計検証（2026-09-20 21:47 JST）

この節の結果・次単位は21:47時点の記録。source dimensionの新規後続結果は[§14.14](#uom-native-source-dimension-result)を参照し、当時のnull/NOT RUNは変更しない。後続のplayer結果は[§14.12](#uom-server-movement-boundary-verification)、現在の限定DENY判定と停止位置は[§14.13](#uom-vehicle-deny-boundary-verification)。

**BLOCKED - TIME STOP SOURCE OWNERSHIP 継続。SAFE DESIGN PROVEN / READY FOR PRODUCTION IMPLEMENTATIONではない。** 今回はowner field不在を理由に止めず、readonly observer＋一時台帳候補を作り、新規dedicated 2 runでnative遷移を測定した。台帳の観測済みALLOWは全てUOMだけであったが、**FOREIGN追加でserver許可がfalseになった後もnative移動packetを受理する反例**があり、`canMove`だけへの接続案は採用できない。製品のFE Adapterは未実装なので、既存製品の回帰FAILとは区別する。

対象は§14.7の承認6 artifactそのまま、Forge47.4.0、Food Healing **229,494 bytes/150 entries/5C1A716EEDE7AEB4449B36EEFFBDDA156176B16B60A1676229224AA3390DB327**。新root [uom-timestop-design-20260920-212237](../build/verification/uom-timestop-design-20260920-212237/) のみ。旧world/loaderを再起動していない。既存loader smoke・L2/TrialのPASSをやり直していない。

証拠の正本：[判定済みmatrix/反例](../build/verification/uom-timestop-design-20260920-212237/audit/reviewed-results.json)、[server-01原結果](../build/verification/uom-timestop-design-20260920-212237/server-01/ownership-result.json)、[server-02原結果](../build/verification/uom-timestop-design-20260920-212237/server-02/ownership-result.json)、[observer外部書込監査](../build/verification/uom-timestop-design-20260920-212237/audit/observer-write-audit.json)。原resultのPASS行を無条件に採用しない。特に02のdimension移動行は後述の理由で除外する。

#### active sourceの実物上の定義

1. **停止成立状態**はnative `global isTimeStop && dimension Setに当該levelあり`。countだけではglobal/dimensionを開始しない。SavedDataだけ/globalだけ/出所不明の既存停止はUNKNOWN。全sourceが消えた後でもnative状態が即時解除される保証はなく、「source空だから自由に動ける」としない。
2. **現在解除を妨げるsource**は、native `use(false)`が列挙する同levelのloaded LivingEntityで、source自身以外・alive・count>0を満たすentity。native検索は原点AABBを3.0E7拡張、getterはロード済みindexを参照し強制chunk loadではない。既に停止中なら、直接setterだけでpositiveになったFOREIGNもこの条件を満たし、停止維持へ関与し得る。正式な開始者と同義ではない。
3. **許可に使える証明済みsource**は上記の実状態と、観測したcommon5引数useの成功・UUID・正式registry type・dimension/epochが一致するものだけ。UOM存在や最新packet source IDでは代用しない。UOM型以外はFOREIGN。unloaded/dead/removedはnativeのalive loaded述語から外れるが、消失を観測できない/保存から復元した/停止だけ残る場合はUNKNOWNを残す。
4. native終了は、他alive positive sourceがあれば自分のcount0だけでearly return（reset=trueの場合）。本fixtureではFOREIGN終了→UOM継続、UOM1終了→UOM2継続を確認した。reset=falseやcount==0到達と複数sourceの同時順序まで一括PASSにはしない。LivingEntityのnative tickは減算→0でuse(false)。UOMは生成直後の一定期間super.tickを飛ばすため、生成直後の1 tickを終了証拠にしてはいけない。
5. `setRemoved`のnative hookはpositive countならuse(false)、levelにplayerなしならglobal=trueを戻す分岐もある。player death/dimension/logout/loginは§14.8の別経路。SavedDataとcapability NBTに元source epochはない。ロード済み集合だけからunloaded/過去sourceの不存在を証明することはできない。

#### 一時台帳候補・UNKNOWN・完全性の限界

[Ownership.java](../build/verification/uom-timestop-design-20260920-212237/fixture/src/main/java/verification/uom/Ownership.java)はdimensionごとのepoch/known UUID→type、useの入退場frame、UNKNOWN理由を持つ。journalには前後global/dimension、全loaded sourceのUUID/type/count/alive/removed/native canMove、reset/duration、early-returnの実述語を保存。capability `setTimeStopCount`/`customDeserializeNBT`のHEAD、Forge death/leave/login/logout/dimension eventを観測する。**early returnの判定は実述語と前後値によるもので、bytecode branch専用counterではない**。

`ALLOW = 生存する本人ServerPlayerのserver canonical P有効 AND global/dimension停止一致 AND source非空 AND 全て観測済み正式UOM AND FOREIGN=0 AND UNKNOWN=false AND use処理中でない`。Pは既存PurificationMasteryController（通常浄化＋極意Lv1/ON、pending不可）を読み、Truthは条件にしない。未取得の別playerとMastery OFFを拒否した。invalid canonical全変種・全OFFの再試験は今回していない。

不明な開始、use例外/途中/stack不整合、停止中の新規direct count、count増加、deserialize、既知sourceの説明不能な消失、空集合、global/dimension矛盾は拒否。FOREIGN追加は次の同server-thread判定で拒否。UNKNOWNはゼロsetterだけでは解除せず、native非停止を実際に観測した後の新epochを基準にする。観測済み既知sourceの単調減少は新ownerを作らないが、terminal decrement→use(false)の入退場で取りこぼしなく管理する設計は追加精査が必要。

loaded reconciliationは、停止中の本人判定で`ServerLevel.getAllEntities()`をreadonly参照する。native検索より広いloaded集合を使うため拒否側に保守的。**過去provenanceの復元手段ではない**。台帳喪失後はloaded UOMだけでもUNKNOWN/DENYとなった。見えないsourceを不存在と推測しない。prototypeは比較のため1判定O(L)（同dimensionのloaded entity数）を複数回読み、全world常時scan/forced loadはしない。productionでのevent世代dirty cache/低頻度reconcileは未採用・性能未証明である。

9個のALLOW snapshotについて、native停止一致・UNKNOWN=false・positive/alive loaded source非空・全正式UOMを機械照合した。これは**有限観測の不変条件確認であり、全入口/全lifecycleの完全性証明ではない**。native source移動は今回実際には成立せず、実client/restartも未実行。FE goalで停止中にcountを直接180へ増やす再延長は、現在prototypeではdirect changeとしてUNKNOWNになる可能性があり、common use延長のPASSで代用しない。未知を許可へ緩める変更はしていない。

#### 再接続方針・server/client権威

| 候補 | 今回の評価 |
|---|---|
| A: 不明からloaded集合だけで再構築 | 不採用。新規台帳に既存native UOM停止を見せても由来は復元できない。active positive集合は整合確認用のみ |
| B: 既存epochは拒否、次の完全観測された新停止で再開 | 最も小さい候補。利用者が今回認めたfail-closed方針。独自永続state不要。ただしprototypeのlogout UUID拒否は保守的に残る実装なので、次epochの新sessionでの安全な再許可は未実装/未検証。これだけでreadyにしない |
| C: 永続source ledger | 必要性は立証されず、採用しない。schema/migration/EndingLib NBTへ追加0 |

送信候補は本人UUID/connection-session識別＋dimension key＋epoch/generation＋monotonic sequence＋allowed＋短いlease。server canonical/source台帳だけで発行し、clientはauthorityにしない。disconnect/world/dimension変更、new epoch、明示revokeでclearし、停止中world gameTimeではなく進むmonotonic時計で期限を設ける。元native停止状態とも照合する。順序逆転した古いseq、別connection/dimension、失われたrevokeの無期限trueを拒否する**cacheモデル**を検証したが、packet登録・実client handler・実client移動は未実装/NOT RUN。

Forge47.4.0実classの`SimpleChannel.MessageBuilder.consumerMainThread -> NetworkEvent.Context.enqueueWork -> BlockableEventLoop`、MC `Connection.sendPacket -> EventLoop -> writeAndFlush`を[bytecode](../build/verification/uom-timestop-design-20260920-212237/audit/bytecode/)で確認。同一connection/送出threadからのpacket順序とmain-thread処理を使えるが、**serverのrevokeと逆方向の既に送られた移動入力を原子的に同期する機構ではない**。TCPの再送/順序性だけでstale permission問題を解決したとはしない。seq/leaseモデルは障害注入モデルであり、実packet loss/TCP試験ではない。

#### canMoveの網羅性と実測した不成立遷移

| 経路 | 確認した収束/限界 |
|---|---|
| server entity tick | ServerLevelExpandedContextのmanual entity tick、LevelMixin.guardEntityTickはcanMoveへ収束。停止中のworld time/neighbor/chunk挙動全体は解除しない |
| server attack/interact | EndingLib CommonEventHandler.TimeStopEventsのattackと各PlayerInteractEventはcanMoveを参照 |
| Iron spell | EndingLib AbstractSpellMixinのcast可否にcanMove判定あり。全MOD独自入力の保証ではない |
| client tick/input | MinecraftMixinの別timer・local player/各entityのtickと主要入力段にcanMove。globalに依存する処理も残るので単一return変更で全機能を保証しない |
| render/timer | entity partial、camera/hand等にcanMoveあり。world/sound/particle/timeなどglobalに従う部分は保持。実client描画/操作はNOT RUN |
| **server movement packet** | **canMoveへ収束しない**。通常`ServerGamePacketListenerImpl.handleMovePlayer`はthread/数値/距離/衝突等を検証後に位置へ反映。停止entity tickを止めても、このhandlerは止まらない |

**具体的反例**（server-01 `server-movement-after-revoke`）：UOM停止でP候補ALLOW → FOREIGNのnative use(true) → 同server-threadでP許可=false、native canMove=falseを確認 → 有効な`ServerboundMovePlayerPacket.Pos`をnative handlerへ1回配送 → **X=0.5→0.5625（+0.0625）**。先にclientへ届いたtrueが失効前に送った入力でも起こり得る受理順序であり、client→serverの移動について最小canMove介入だけではJとFOREIGN停止維持を保証できない。実測はEmbeddedChannelのServerPlayer/native handlerで、実client接続/映像のPASSではない。

元canMove=true（creative/spectator/正当な外部count/flag）をfalseにする案は不採用。今回はcanMove return overrideもnetwork許可送信も設置していない。readonly observerのcompiled bytecodeは外部flag/count/dimension/NBTへの書込呼出し・外部field store **0**。native use/count/deserialize/SavedData追加削除は**fixtureの試験入力**であり、製品の保護処理やtrackerによる状態修復ではない。

#### 最低20枠の結果と未確認

| # | 遷移 | 結果・厳密な範囲 |
|---|---|---|
| 1 | UOM単独開始 | native LIMITED PASS。common useと実FE TimeStopSkillGoalのcount180→useを別記録 |
| 2 | UOM延長 | common5引数useのmax延長 LIMITED PASS。goalの途中direct増加再延長は未確認 |
| 3 | 通常終了 | native use(false)と実UOM tickの180→0をLIMITED PASS。生成期間込み184 native ticks |
| 4 | FOREIGN単独 | DENY LIMITED PASS |
| 5 | UOM→FOREIGN | 次のserver判定でDENY LIMITED PASS。移動受理の別反例あり |
| 6 | FOREIGN→UOM | DENY LIMITED PASS |
| 7 | UOM複数 | ALLOW候補 LIMITED PASS |
| 8 | UOM1終了/UOM2継続 | 明示native use(false)でALLOW候補維持。terminal tickの全交錯は対象外 |
| 9 | FOREIGN終了/UOM継続 | 実count0・残source照合後のみALLOW候補 LIMITED PASS |
| 10 | UOM終了/FOREIGN継続 | DENY LIMITED PASS |
| 11 | disable early return | source自身0、global/dimension維持 LIMITED PASS。分岐専用counterなし |
| 12 | direct count positive/zero | UNKNOWN/DENY、zeroだけで回復しない LIMITED PASS |
| 13 | setRemoved/unload | 明示DISCARDED/UNLOADED_TO_CHUNKでLIMITED PASS。実chunk eviction全体ではない |
| 14 | source death | FOREIGN Cowのnative致死damage/death後DENY LIMITED PASS。UOM独自death phaseは未確認 |
| 15 | source dimension移動 | **NOT RUN（遷移未到達）**。native API呼出しはnull、old source未removed/count80。raw PASSは「移動しなかった状態のDENY」だけなので移動PASSから除外 |
| 16 | player logout/login | actual PlayerList remove/placeNewPlayer＋保存読込/native eventで拒否をLIMITED PASS。認証/実client接続は未試験 |
| 17 | source/player再接続相当 | **台帳喪失相当のみ**：既存native epoch中に台帳を新規化→UNKNOWN/DENY。実process restart/world reload未実施 |
| 18 | positive deserialize | 実capability customDeserializeNBTでUNKNOWN/DENY LIMITED PASS |
| 19 | dimension Setだけ | 実SavedData public APIによる入力でUNKNOWN/DENY LIMITED PASS |
| 20 | global/dimension不一致 | 実native stateの不一致でUNKNOWN/DENY LIMITED PASS |

20枠中、**18枠は上表範囲のnative LIMITED PASS、1枠は喪失相当モデルのみ、1枠は遷移未成立**。付随するP canonical/他player/leaseを含め採用PASS assertion行は**28**。raw 01はPASS24/観測1/NOT RUN2と途中assertion FAIL、raw 02はPASS5/観測2、そのうち移動未成立の1 PASS行を除外した。28/28 native gameplay PASSとは書かない。別にcanMove-only設計のserver移動権威は**FAIL**。製品FE保護integrationは依然 **0/NOT RUN**。

native移動のnullはraw結果で確認。基本MC経路では入口PortalInfoを解決できないとnullを返す（[Entity bytecode](../build/verification/uom-timestop-design-20260920-212237/audit/mapped/net.minecraft.world.entity.Entity.txt)）。本runは出口portalを準備しておらず、この分岐との整合はあるが、Forge/外部eventを含むnullの最終原因を専用observerで確定はしていない。結果合わせのsetter/強制移動で遷移を偽装していない。

#### 補助FAIL・通常終了・変更境界

- root内専用sourceSet/Gradle initのみ。dry graphでcompileJava/jar/reobfJar/build/unit/check/既存suiteを拒否し、helper専用compile/reobfを2回実行。通常sandboxのnative-platform.dll初期化失敗は保存し、既存local Gradle8.1.1/cacheの権限付きoffline実行で成功。`downloadMcpConfig` task名はgraphにあるがoffline既存cache使用、新規依存/外部downloadなし。
- 01の最後のcountdown確認は「新品UOMを1 tick」というfixture前提誤りでassertion FAIL。原source/Jar/result/logを保全。補助だけを補足し、02では実goal→通常native tickを上限付き実行、**184 ticksで180→0**。元countやUOMの年齢をsetterで合わせていない。01の既完了行を目的なく全再実行していない。
- [server-01/process.json](../build/verification/uom-timestop-design-20260920-212237/server-01/process.json)：21:30:15開始/Java PID25856、21:30:45通常save-all flush/stop・全dimension保存、21:30:46 exit0。[server-02/process.json](../build/verification/uom-timestop-design-20260920-212237/server-02/process.json)：21:34:05開始/PID26952、21:34:35–36通常保存/stop、21:34:37 exit0。process receiptのPASSは終了手続き判定であり、01のfixture FAILを消さない。PID25856は後にsmartscreenへ再利用されたため、PID番号だけの存在判定を元Java存続とはしない（[readback](../build/verification/uom-timestop-design-20260920-212237/audit/process-readback.json)）。
- 新規8外側mods＝承認6個＋不変の製品＋別補助。nested二重配置なし。補助01:24,050 bytes/15 entries、SHA256 8203DD5565B544C7B8AC6B9E1E65FBF97621E24E7CFFE8CF411D97F40D5E3722、02は[実測receipt](../build/verification/uom-timestop-design-20260920-212237/audit/helper-02.json)。補助に外部class/Jar/ExampleMod/製品classの同梱なし、helperは製品Jarへ入れていない。
- 製品source/test/build.gradle/Jar/Config/schema/network/purchase gate変更0、build/unit/check/既存L2・Trial・core suite実行0、client/Prism0。両極意IMPLEMENTATION_PENDING・購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、その他個別開始条件と可逆クラフトの既知許容仕様を維持。

#### 次の1単位（今回は提案まで・追加hookを採用しない）

**失効後のnative移動packet受理を防ぐ、server側の最小入力境界のverification-only設計検証**。今の再現を負対照とし、Food Healingが発行した一時許可と接続session/epochをserverの移動受理時にも照合する案を検証する。`handleMovePlayer`を最低の候補とし、搭乗時の別handler・native true権限の保全・失効/teleport待機/通常移動への影響は必要範囲で切り分ける。単にclient leaseを短くして安全としない。これはEndingLibのglobal/count/capability解除や永久台帳ではなく、**canMoveの外にあるserver受理経路の追加guard案**であり、今回実装していない。

この境界が成立した後に、未到達source移動と再接続/新epoch再許可、FE goal再延長、複数source terminal countdownを既存fixtureへ補足して完全性を確定する。既存matrix全部/loader/過去L2/Trialを再計画しない。新artifact/独自永続schema/仕様緩和が必須と判明したわけではない。SAFE DESIGN PROVEN前にFE6siteやTimeStopをproductionへ接続しない。


<a id="uom-server-movement-boundary-verification"></a>
### 14.12 UOM TimeStop — server movement boundary verification-only結果（2026-09-20 22:26 JST）

以下の未被覆/次単位/判定は22:26時点の履歴。結果を変更せず保持し、後続のvehicle DENY境界の実施結果は[§14.13](#uom-vehicle-deny-boundary-verification)。

**BLOCKED - TIME STOP SOURCE OWNERSHIP 継続。SERVER MOVEMENT BOUNDARY VERIFIED = NO（handleMoveVehicle未被覆）、SAFE DESIGN PROVEN = NO。** playerの非搭乗`handleMovePlayer`の最低17枠は限定成立したが、別のnative vehicle handlerからFOREIGN停止中の座標更新が成立した。利用者§17の停止条件に従い、vehicle guard・他入力hook・production実装へは進めていない。既存§14.11の18 native限定/1喪失相当/1移動未成立とcanMove-only FAILはそのまま保持する。

証拠入口：[判定集計](../build/verification/uom-movement-boundary-20260920-220541/audit/reviewed-results.json)、[02全生データ](../build/verification/uom-movement-boundary-20260920-220541/server-02/movement-result.json)、[実ロードclass](../build/verification/uom-movement-boundary-20260920-220541/audit/bytecode/ServerGamePacketListenerImpl-runtime02.txt)、[Forge47.4.0実classの関連method](../build/verification/uom-movement-boundary-20260920-220541/audit/bytecode/movement-selected.txt)。全測定は**AUTOMATED・新規dedicated＋EmbeddedChannel ServerPlayer/native packet handler**。実client/TCP/認証/reconnect/映像を測った結果ではない。旧loader smoke/18枠全体/製品suiteの再試験0。

#### 実経路と選んだ境界

Forge47.4.0 server Jarの`ServerGamePacketListenerImpl.handleMovePlayer`（SRG `m_7185_`）は、thread check→invalid値→awaiting teleport→passenger/sleep→packet頻度・距離/速度→旧bbox→jump→`ServerPlayer.move`→衝突/moved-wrongly→`absMoveTo`→floating/落下/onGround/lastGoodの順。別途ackは正しいteleport IDを照合しawaitingを解消する。

候補Aを採用した**検証補助だけ**のMixinは、速度検査後の最初の`ServerPlayer.getBoundingBox()`直前（原class offset677、runtime callback offset694）で判断し、DENYならこのpacketの残りの移動処理をskipする。jump/move/fall mutationより前、thread/invalid/teleport待機/riding/速度の既存処理より後であり、HEAD取消ではない。検証範囲の正常ALLOWは元の衝突・落下・lastGood処理へそのまま進む。passengerのplayer packet分岐は座標を現位置のまま回転更新し、この非搭乗境界に来ない。vehicle座標更新は後述の別handlerである。

候補B（毎回native teleport補正）はawaiting ID/待機状態・送信を増やすため今回は採用せず、独自tp/resetなし。skip方式の**実client表示のずれ・補正収束は未検証**。拒否側を止めた事実からclient操作可能/完全免疫まで推定しない。

guardは当該dimensionのnative停止がなければ素通し、停止中でも元`canMove(player)=true`なら素通し。native falseの場合だけ、現在のServerPlayerオブジェクト・listener・**実Connectionオブジェクト/接続有効**・dimension・epoch・current sequence・allowed/nonrevokedと、P canonical/生存・global/dimension一致・非空の全UOM source集合・FOREIGN0/UNKNOWNfalse/processingなしを再照合する。packetへ新フィールドは追加しない。permissionはserver内の一時Grant、正しさは受信時に再評価し、UUID単独や古いclient leaseには依存しない。session/epoch/sequence不一致の入力は**補助のtransientモデルへの障害注入＋実handler**であり、real reconnectではない。

#### 最低17枠と対照

全て02の生データcase名へ対応する。`+0.0625`はXの実差分、`0`はXYZ完全不変。

| # | case・条件 | 結果（player handler限定） |
|---|---|---|
| 1 | no TimeStop | +0.0625、通常受理 |
| 2 | UOM単独/P/current permission | +0.0625、candidate ALLOWで元handlerへ到達 |
| 3 | Mastery OFF / 親Purification OFFを別入力 | 各0、拒否 |
| 4 | P/M未取得player | 0、拒否 |
| 5 | 別playerへ元本人Grantを流用 | 0、拒否。個別identityだけの切分けは#15も参照 |
| 6 | FOREIGN単独 | 0、拒否 |
| 7 | ALLOW時にpacket作成/保持→FOREIGN開始→DENY→同packet配送 | X0.5→0.5、拒否。下記負対照あり |
| 8 | FOREIGN→UOM | 0、拒否 |
| 9 | UOM＋FOREIGN混在 | 0、拒否 |
| 10 | FOREIGN終了、UOMのみの完全な観測を確認、新Grant/新packet | +0.0625、受理 |
| 11 | native TimeStop終了 | +0.0625、guardの特別許可なしで通常受理 |
| 12 | UNKNOWN | 0、拒否 |
| 13 | 台帳喪失相当 | 0、拒否。実process restartではない |
| 14 | epoch不一致 | 0、拒否。他条件のcurrent ALLOWを確認 |
| 15 | 同UUID/同player/listenerだが保存GrantのConnection不一致 | 0、拒否。認証済み再接続ではない |
| 16 | Grant dimension不一致 | 0、拒否。entity次元遷移試験ではない |
| 17 | old sequence / revokedを別入力 | 各0、拒否 |

**stale packet反例の厳密比較**：unguarded対照でもguardedでもT1のUOM/P ALLOW時に実`ServerboundMovePlayerPacket.Pos`を生成して保持。T2 native FOREIGN `use(true)`、T3 source再計算でDENY/native false、T4にT1と同じオブジェクトを配送。前後identity・packet X0.5625が同じことを照合し、payloadを書換えていない。guard無効の負対照は**X0.5→0.5625**、有効は**0.5→0.5**。raw対照のPASSは「反例再現成功」であってcanMove-only安全PASSではない。

追加対照：processing中は0、FOREIGNがあるままcreative/spectator/本人をnative use(true) sourceにした正当canMove=trueの3条件は各+0.0625。Food HealingのFOREIGN判定から元native trueを奪っていない。server正式teleport→awaiting中に旧packetでは位置不変/guard呼出し0→実ack→新valid packetで+0.0625/guard1。速度過大packetはguardより先に元handlerの補正を受け、位置不変/awaiting設定/guard0。teleport待機の動作をFood Healingが代行していない。

拒否packetで**位置/previous/firstGood/lastGood/fallDistance/onGround/velocity/collision/awaiting/floating counter/接続/HPが不変**。元handlerの`receivedMovePacketCount`と`awaitingTeleportTime`の通常bookkeepingだけは上流で進み得るので不変条件から明示分離した（値は全保存）。元global/dimension SavedData・各entityのcount/nativecanMove/EndingLib living capability serialized NBT/persistent NBTは、26 player移動assertionの前後で一致。観測関数はread serializationのみ、修復setterなし。[compiled書込監査](../build/verification/uom-movement-boundary-20260920-220541/audit/guard-readonly-audit.json)でobserver/guardの外部field store・外部setter/use/deserialize呼出し0。fixtureのnative use/終了、取得準備、mode切替、障害モデル入力とは分離する。

rawは**PASS29行・vehicle COUNTEREXAMPLE1行・追加観測1行**。PASS29には移動assertion26（unguarded反例再現1を含む）、timeline整合2、速度補正1を含む。17枠＋付随対照として評価し、29 gameplay PASSや既存18枠の新規再証明にしない。長時間floating/kick、全collision/fall/車種、client補正収束は未確認。

#### vehicleで成立した未被覆反例と停止

`handleMoveVehicle`（`m_5659_`）は独立handlerで、thread/数値検査→root vehicle≠player・controllingPassenger一致・lastVehicle一致→距離/速度→bbox/collision→`Entity.move`→`absMoveTo`→補正/vehicle lastGoodへ進む。**native canMove/TimeStopを参照しない**。EndingLibのentity tick停止とは別の到達経路である。

実Boatへnative startRiding、connection tickで元lastVehicleを準備し、UOM/P ALLOW時のvalid vehicle packetをnative decoderで作成/保持した。FOREIGN native use後、本人/BoatともcanMove=false、server許可DENY、同controllingPassengerのまま配送すると、**Boat X2.5625→2.625（+0.0625）**、vehicleLastGoodも更新。player guard呼出し0、外部TimeStop状態不変。snapshotでは乗車時のawaiting teleportも残っていたが、vehicle handlerはその待機を検査せず受理した。対照を隠す再実行はしていない。

handler直後のplayer Xはまだ2.5625。追加で**native `boat.positionRider(player)`を明示1回**呼ぶと本人も2.625へ追随した。これはnative passenger配置への到達の補足観測であり、実client・自動通常tickでの乗車移動PASSとは呼ばない。位置setterで結果を作っていない。既に車両位置のFOREIGN bypassは成立したため、それ以上の実装/試験は利用者の停止条件どおり終了。

**次の1単位候補**はこの`handleMoveVehicle`の安全なDENY境界だけ。元ownership/速度検査後、最初の`Entity.getBoundingBox()`/climbable reset/moveより前を候補に、FOREIGN/UNKNOWN/失効時のvalid packetを止め、非停止・元の正当canMoveとvehicle ownership/補正を保全できるかを別途verification-onlyで確認する。全packet取消はしない。**UOM/Pだけの車両ALLOWは未LOCKであり今回採用しない**。それを追加する段階でのみ、P免疫へ搭乗物移動を含めるかの利用者判断が必要。FOREIGN側DENYの検証案自体は車両免疫の新仕様を決めずに切り分けられる。新artifact不足なし。永続ledger/schema/network protocol変更が必須と分かったわけではない。

#### 補助失敗・成果物・正常終了

- 新規root `uom-movement-boundary-20260920-220541`、初期保全[before](../build/verification/uom-movement-boundary-20260920-220541/before/)。旧run/world/receipt変更・再利用なし。製品は229,494 bytes/150 entries/SHA256 `5C1A716EEDE7AEB4449B36EEFFBDDA156176B16B60A1676229224AA3390DB327`不変。外側modsは承認6＋製品＋今回補助の8、nestedを外側へ展開/重複追加しない。
- compile01は補助が存在しないgetConnection getterを呼んだエラー。実public connection field参照へ修正。compile02は成功したがserver01はreadonly SavedData.saveの外部継承method reobf不整合で**0ケース・補助FAIL**。元source/Jar/logを保全し、SavedData基底型経由の既存saveへ修正してcompile03成功、**新world server02**で測定。期待値/guardを緩めず、失敗結果を削除していない。静的packet classがForge patched Jar側にないread errorはMC srg Jarで補足した。
- [補助02現物](../build/verification/uom-movement-boundary-20260920-220541/audit/helper-02.json)：30,825 bytes/17 entries、SHA256 `E6F12F23FE1EEEBC3C8338BD939C53AA4BB8750BDC774BE18C6933B90C491073`。外部class/Jar・製品class/ExampleMod非混入。製品へ補助を入れていない。専用[task graph](../build/verification/uom-movement-boundary-20260920-220541/audit/task-graph.txt)で製品compileJava/jar/reobfJar/build/unit/check/既存suiteを拒否。既存Gradle8.1.1/cache・offlineのみ。Gradle既定値が専用sourceSet/reobfの一時出力を`build/resources`/`build/tmp`/`build/reobfUomMovementVerificationJar`へ生成したため、終了後に今回所有の4ディレクトリだけをパス検証・hash照合して[verification配下へ保全移動](../build/verification/uom-movement-boundary-20260920-220541/audit/gradle-generated/relocation.json)。製品の生成先/設定は変更していない。
- [server01 process](../build/verification/uom-movement-boundary-20260920-220541/server-01/process.json)：22:17:08開始/PID27616、22:17:25–26 save-all flush/stop/全dimension保存、22:17:27 exit0。[server02 process](../build/verification/uom-movement-boundary-20260920-220541/server-02/process.json)：22:18:47開始/PID880、22:19:05–06保存/stop/全dimension保存、22:19:06 exit0。終了PASSと01補助FAILは別判定。EndingLib停止は終了時global/dimfalse、全観測source count0。UOM独自discard制限でcount0のactorが残った事実もraw snapshotに保持し、全actor削除成功とは書かない。使い捨てworldは停止済みで再起動しない。
- source/test/build.gradle/製品Jar/製品Mixin/protocol6/Config/schema/migration/purchase gate変更0。専用補助のみ。build/unit/check/既存L2/Trial/core/loader smoke/実client再実行0。FE6 site STATIC AUDITED/NOT IMPLEMENTED、TimeStop production NOT IMPLEMENTED、製品FE保護integration0/NOT RUN。IMPLEMENTATION_PENDING/購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、既存C/seal等のPASS・他個別開始条件・可逆クラフト増加の既知許容仕様を維持。

source dimension/reconnect/process restart/FE goal再延長/複数source末尾tick/client/productionへの続行はなし。今回の停止理由は**実在する未被覆vehicle handler**であり、一般的な再承認待ちやartifact不足ではない。


<a id="uom-vehicle-deny-boundary-verification"></a>
### 14.13 UOM TimeStop — vehicle専用DENY境界のverification-only結果（2026-09-20 22:50 JST）

この節の次作業・未着手表記は当時の履歴。source dimensionの後続結果・現在の次作業は[§14.14](#uom-native-source-dimension-result)。

**PLAYER + VEHICLE SERVER DENY BOUNDARY VERIFIED（LIMITED）**。§14.12で実測したvehicle未被覆は、今回の専用DENY候補で限定解消した。**SAFE DESIGN PROVEN = NO、全体のBLOCKED - TIME STOP SOURCE OWNERSHIPは継続**。source lifecycle等の完全性と製品への写像は後続であり、製品TimeStop免疫完成/購入readyへ昇格しない。**UOM単独/P有効のvehicle動作はUNDECIDED / NOT LOCKED**。

証拠：[判定集計](../build/verification/uom-vehicle-deny-20260920-223504/audit/reviewed-results.json)、[run01原結果](../build/verification/uom-vehicle-deny-20260920-223504/server-01/vehicle-result.json)、[run02未完了対照の補足](../build/verification/uom-vehicle-deny-20260920-223504/server-02/vehicle-result.json)、[変換後vehicle handler](../build/verification/uom-vehicle-deny-20260920-223504/audit/bytecode/vehicle-handler-runtime02.txt)、[guard compiled bytes同一性](../build/verification/uom-vehicle-deny-20260920-223504/audit/guard-build-equivalence.json)。全てAUTOMATED・新規専用dedicated・EmbeddedChannel/native handlerによる限定検証。認証済みreal reconnect/TCP/実client/tick描画/通常操作のPASSではない。

#### 実call flow・最小hook

MC1.20.1/Forge47.4.0の`ServerGamePacketListenerImpl.handleMoveVehicle`（SRG `m_5659_`）は、thread check→invalid値→root vehicle≠player→controllingPassenger一致→lastVehicle一致→距離/速度→旧bbox/noCollision→climbable fall reset→`Entity.move`→moved-wrongly検査→`absMoveTo`→衝突再検査/元位置補正→vehicle floating/lastGood更新。前回の変換後実class・今回の変換後実classの元handler本体にEndingLib `canMove`直接参照はない。今回追加した別helper callbackだけが許可状態を参照する。

今回の`VehicleBoundary`は**最初のEntity.getBoundingBox直前**（元offset316、今回runtime callback333/bbox345）へ1箇所だけInject。所有・数値・速度検査より後、climbable reset/move/座標mutationより前で、DENYなら当該vehicle処理の残りだけskipする。HEAD取消/全packet取消/positionRider取消ではない。独自tp、座標・速度・HP・外部flagのresetなし。vanilla拒否をFood Healingが成功へ変更する仕組みはない。

#### 判定範囲と未決定分岐

- 当該dimensionにnative TimeStopがなければ無介入。native `canMove(player)=true`ならFood HealingのFOREIGN/UNKNOWN/古いGrantより優先して元処理へ通す。
- 停止中/native falseの場合だけ、共有するtransient GrantのServerPlayer/listener/Connection実identity・接続有効性・dimension/epoch/current sequence・allowed/nonrevokedと、canonical P・非空の全UOM集合/FOREIGN0/UNKNOWNfalse/processingなし・global/dim一致を再評価し、不成立ならDENY。
- 検証専用コードの完全current UOM/P分岐は**UNDECIDED_NOT_LOCKED**として「追加介入なし」を返す未採用分岐であり、車両ALLOW仕様を決めたものではない。**この分岐でvehicle packetを配送した件数0、PASS/FAIL判定0**。安全DENYの検証にこの仕様決定は不要だった。実際にUOM/P vehicle動作を製品へ提供/制限する段階でのみ利用者判断が必要。
- EndingLib `TimeStopUtils.canMove(Entity)`はPlayer creative/spectatorをtrue、その他LivingEntityはcapabilityを参照し、**非LivingEntityは常にfalse**。Boat個別のliving TimeStop権限はない。そのため今回のnative true対照は**操縦playerの権限を奪わない**ことを確認し、Boatのnative falseを書き換えていない。§14.12のplayer spectator対照は保持、今回のvehicle spectator操縦は必須化せず未試験。

#### vehicle実測とplayer連携

| 対象 | 期待・実測 | 判定・範囲 |
|---|---|---|
| unguarded held反例 | T1 UOM/P候補で作成・保持したvalid packet→T2 FOREIGN native use→T3 DENY/native false→T4同packet。Boat **2.5625→2.625** | 旧反例の再現。raw PASSは再現成功であって安全PASSではない |
| guarded held | 同じ時系列、Boat **XYZ完全不変：2.5625/-59/0.5のまま** | LIMITED PASS。payload byte列/packet identity/Boat/player/Connection不変、controller維持。guard1/player guard0 |
| A FOREIGN単独 / B UOM＋FOREIGN | 各XYZ変化0 | LIMITED PASS |
| C UNKNOWN / D台帳喪失相当 | 各XYZ変化0 | transient model＋実handler。restartを意味しない |
| E revoked / F epoch / G Connection-session不一致 / H dimension / I old sequence | 全てXYZ変化0 | LIMITED PASS。real reconnect/TCP/次元遷移ではない |
| canonical Pの親OFF / Mastery OFF、processing中 | 各XYZ変化0 | LIMITED PASS。normal canonicalの効果時判定、製品purchase gate変更なし |
| source集合空 / global-dimension不一致 | 各XYZ変化0 | native count0でもglobal/dimが残る入力と、global=false/dimSet=trueの入力を準備。guardの前後で外部値不変。global=true/当該dim非停止は無介入規則の側であり同一視しない |
| 実Connection無効 | XYZ変化0 | run02でUOM/current Pはtrueのままnative disconnectしたConnectionへ遅延呼出し。identity=falseだけで拒否。ネットワークを経た再配送ではない |
| TimeStopなしの正当Boat | X+0.0625 | guard1・NATIVE_UNCHANGED、元controller/lastVehicle/valid packetを通す |
| FOREIGN中creative / native self-source権限 | 各X+0.0625 | 元player canMove=trueを維持。Boat native falseのまま。Food Healing独自ALLOWではない |
| native passenger配置 | DENY後にpositionRiderを明示1回、Boat/本人XYZ不変 | LIMITED PASS。通常client/tick/render/操作は未観測 |
| player連携3件 | no-stop +0.0625、current UOM/P +0.0625、ALLOW時の保持packetをFOREIGN後配送して0 | 既存guard維持。旧17枠全部の再試験/新vehicle件数への加算なし |

20 vehicle packet assertion中**16拒否**は全て、Boat position/previous/oldPosition/vehicleFirstGood/LastGood/velocity/onGround/fallDistance/bbox/collision/rotation/portal/removed/alive、player位置/乗車関係/controller/velocity/onGround/fallDistance/HP/connectionとその関連snapshotが**全項目一致**。このhook前のvehicle経路では、今回の拒否入力で変わる上流bookkeepingもなかった。比較対象を復元setterで合わせていない。元global・dimension SavedData Set・各LivingEntityのcount/nativecanMove/living capability serialized NBT/persistent NBTは20 vehicle assertion全て前後一致。Boatのpersistent NBT/native canMoveも個別記録。[guard/observer compiled readonly監査](../build/verification/uom-vehicle-deny-20260920-223504/audit/guard-readonly-audit.json)に外部field store/setter/use/deserialize呼出し0を保存。native use/count/Set等への変更は、明記したfixture入力・正常終了処理だけである。

#### vanilla拒否の独立対照

| 元handlerの条件 | 今回の実測 |
|---|---|
| root vehicleがplayer自身 | 元分岐で無視、移動0/vehicle guard0 |
| lastVehicle不一致 | 正常native乗車後、connection tick未実施の新Boatを送信。移動0/guard0 |
| controllingPassenger不一致 | run02で別ServerPlayerが第1乗員、送信者が第2乗員、lastVehicleは同Boatのまま。全状態不変/guard0 |
| invalid numeric | 実NaN packetをnative handlerへ配送し元disconnect作動。移動0/guard0。正当packetのpayloadを不正化してDENY成功とした試験とは別枠 |
| 過大速度 | X+1000のpacketに元ClientboundMoveVehiclePacket補正、位置不変/guard0 |
| collision/moved-wrongly | 新worldの実STONE壁へ向かうpacket。開始bbox無衝突/移動先衝突をreadonly確認し、元move→検査→補正で元位置に戻りnative correction送信。guard1だがno-stopで無介入、外部state不変 |

**raw PASS32行**＝vehicle assertion20（unguarded反例再現1を含む）＋held identity確認2＋passenger配置1＋vanilla拒否6＋player連携3。別にUNDECIDED1/準備operand観測1、run01途中fixture assertion FAILを保持。32製品gameplay PASSではなく、旧18枠/旧player17枠を加算していない。車種全体・長時間floating/fall/遅延・任意MOD独自入力を網羅した保証ではない。今回の範囲で追加の別handler反例は見つかっていないが、全server入力の不存在を証明したとはしない。

#### 補助修正・通常終了・変更境界

- 2回の専用offline compile/reobfはいずれも成功。run01は29 PASS/1 UNDECIDEDの後、操縦者不一致の**準備assertion**で停止。Cow→Playerの順序では、MC `Entity.addPassenger`がserver上のPlayerを非Player第1乗員より前へ挿入するため、意図した不一致にならなかった（[実bytecode](../build/verification/uom-vehicle-deny-20260920-223504/audit/bytecode/Entity.txt)）。元失敗source/Jar/result/logを保全し、run02はnativeの**2 Player乗員**で準備し直して、未完了のcontroller/Connection無効/invalid numericの3対照だけ実行。guard・ledger・player boundaryのcompiled bytesは01/02で完全同一。既完了matrixの一括再実行や期待値緩和なし。
- [helper01](../build/verification/uom-vehicle-deny-20260920-223504/audit/helper-01.json)：38,559 bytes/19 entries、SHA256 `D5BFB9158967740115A32AAF2A797E98609C201CEF4CCE26310C972C87AB7B1D`。[helper02](../build/verification/uom-vehicle-deny-20260920-223504/audit/helper-02.json)：39,477 bytes/19 entries、SHA256 `E6E6786C1F2FDE638D0ADC4749F3754B3BDEBC9EF986EA5FE96C09AE650716B5`。外部class/Jar・製品class/ExampleMod非混入。製品には追加しない。
- 専用initの`beforeProject`で、このGradle invocationのbuildDirectoryを今回root内`gradle-work`へ固定。[graph](../build/verification/uom-vehicle-deny-20260920-223504/audit/task-graph.txt)は製品compileJava/jar/reobfJar/build/unit/check/既存suiteを拒否。前回と違いsourceSet/reobf一時出力も最初からverification配下、製品build.gradleは不変。既存Gradle8.1.1/cache・offlineだけ、承認6物＋製品＋別helperの8外側mods。同梱依存の二重配置・外部downloadなし。
- [run01 process](../build/verification/uom-vehicle-deny-20260920-223504/server-01/process.json)：22:42:10開始/PID14304、22:42:29通常save-all flush/stop・全dimension保存、22:42:30 exit0。[run02 process](../build/verification/uom-vehicle-deny-20260920-223504/server-02/process.json)：22:45:11開始/PID27028、22:45:29–30通常保存/stop・全dimension保存、22:45:31 exit0。終了PASSはrun01準備FAILを消さない。worldは全て新規、過去runへ再入場/修復なし。終了時はnative global/dim停止なし、残存UOM actorのcount0をraw snapshotへ保持し、強制削除成功とは書かない。
- 製品229,494 bytes/150 entries、SHA256 `5C1A716EEDE7AEB4449B36EEFFBDDA156176B16B60A1676229224AA3390DB327`不変。source/test/build.gradle/製品Mixin/network protocol6/Config/schema/migration/purchase gate不変。製品build/unit/check/旧L2/Trial/core/loader smoke/実client0。FE6 site STATIC AUDITED/NOT IMPLEMENTED、TimeStop production NOT IMPLEMENTED、製品FE効果integration0/NOT RUN。両極意IMPLEMENTATION_PENDING/購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、既存C/seal等のPASS・他個別開始条件・可逆クラフト増加の既知許容仕様を維持。

#### 次の1作業候補と残判断

**§14.11で未到達だったsource dimension遷移を実際に成立させ、遷移時の台帳失効/DENYを確認するverification-only補足**を次の1単位候補とする。入口/出口のnative要件を満たす新規検証環境で、null呼出しを成功扱いせず、setterで移動を偽装しない。今回この作業は未着手。player17/vehicle A–I/既存18/loader smoke全体を再計画しない。

real reconnect、process/world reload、新epoch再許可、FE goal途中再延長、複数source terminal countdown、実client input/render/correction、productionへの写像は別工程として残す。UOM/P vehicle動作の仕様判断は未LOCKのまま分離し、今回の安全DENY判定の前提にしない。新artifact、独自永久台帳/schema、EndingLib改変、製品protocol変更が必須と判明したわけではない。**今回は限定判定と文書更新で終了し、production/実clientへ自動続行しない。**


<a id="uom-native-source-dimension-result"></a>
### 14.14 EndingLibrary source native dimension遷移 — verification-only結果（2026-09-21 10:27 JST）

**一般LivingEntityのnative transferは実到達確認。UOM NATIVE DIMENSION TRANSFER NOT SUPPORTED。要求全体のSOURCE DIMENSION TRANSITION VERIFIED = NO、SAFE DESIGN PROVEN = NO、BLOCKED - TIME STOP SOURCE OWNERSHIP継続。** UOMが消えた後のstale Grant拒否を、FOREIGN sourceの転送で代替したとは扱わない。旧§14.11のnull / NOT RUNは当時の結果として保持し、以下を後続の新規証拠とする。player/vehicle DENY境界の既存限定PASSも取り消さない。

[判定済み集計](../build/verification/uom-dimension-20260921-101349/audit/reviewed-results.json) / [native raw](../build/verification/uom-dimension-20260921-101349/server-01/dimension-result.json) / [実クラス抜粋索引](../build/verification/uom-dimension-20260921-101349/audit/static-method-index.json)。新規root `uom-dimension-20260921-101349`、新規dedicated `server-01` 1回のみ。MC1.20.1 / Forge47.4.0、承認FE2.7.20 / EndingLibrary2.1.19fix＋既存依存、製品229,494 bytes/150 entries/SHA256 `5C1A716EEDE7AEB4449B36EEFFBDDA156176B16B60A1676229224AA3390DB327`、別補助。AUTOMATED / EmbeddedChannelであり、client/TCP/認証/自然portal歩行の試験ではない。

#### native経路と旧nullの切り分け

- Forge patched `Entity.changeDimension(ServerLevel)` → 対象levelの**native PortalForcer** → Forge overload / `ForgeHooks.onTravelToDimension` → `EntityTravelToDimensionEvent` → native `getPortalInfo` / `findDimensionEntryPoint` / `getExitPortal` / POI解決 → `placeEntity`のvanilla callback → `EntityType.create` → `restoreFrom` → native配置・`ServerLevel.addDuringTeleport` → 旧`removeAfterChangingDimensions` / `setRemoved(CHANGED_DIMENSION)`。補助は転送をredirect/cancelせず、custom ITeleporterも用いない。
- 入口/出口は両方 `PortalForcer.createPortal`、入口blockは実`NetherPortalBlock.entityInside`を明示呼出し。入口(0,-60,0)、出口POI(0,87,1)、native PortalInfo位置(0.5,87,1.5)を実測。fixtureによる初期配置とportal刺激であり、自然歩行の再現とは記録しない。補助によるlevel/dimension field変更、Dimension NBT変更、別entity手動spawnでの転送代用はない。
- 新runで出口作成前の負対照1回：Forge eventはLOWEST観測時cancel=false、その後native PortalInfo=null、API=null、Cowは旧levelに残存。その後出口生成/解決を済ませた同Cowはnative転送成功。**今回のnull原因は出口POI/PortalInfo未解決まで確認**。この準備対照はcount0で、旧§14.11のcount80 runを再起動/再実行したものではない。旧runも出口未準備との整合は強いが、当時のForge最終分岐自体は未観測のため、過去原因を確定したとはしない。
- UOM実classは `com.mega.uom.common.entity.boss.uom.UomWither extends BaseEntity extends Monster`。UomWitherの両`changeDimension` overloadは**無条件にthisを返すだけ**、`canChangeDimensions()`はfalse（BaseEntityもfalse）。実呼出し2種類とも同object/同Overworld/count80のまま、Nether lookupなし、Forge travel event0/base transfer observer0。**nonnull returnだけでは成功でない**。AI phaseを変更したりsuper経路を強制したりせず、別のnative制限として確定。

#### 転送・capability・台帳の実測

| 対象 | 実測 | 判定 |
|---|---|---|
| A：Cow単独source | global=true / A=true / B=false、count80/canMove=true、originsに実Cow UUID/type、UNKNOWN=false | native既知sourceの準備成立。CowはUOMではないのでP独自ALLOW対象ではない |
| B：一般Cow転送 | UUID `100d6a9f-7571-427c-b09b-2f30550972e3` 維持、object **686954639→880117650**、entityID **2→13**、Overworld→Nether。旧lookup=null、新lookup=戻り値instance | **GENERAL SOURCE NATIVE TRANSFER CONFIRMED（限定）** |
| native copy/remove | 新instanceはrestoreFrom前に別UUID/count0、後に旧UUID/count80。EndingLib `customDeserializeNBT`へ`{TimeStopCount:80}`が入り、native canMove=true。旧instanceはCHANGED_DIMENSION / removed=true / isAlive=false、HP10保持、count0/capability`{}` | 旧objectの移動/死亡試験ではなくnative再生成。reviveを呼んで旧objectを移した証拠はない。今回のbase経路にもrevive呼出しなし |
| C：単独転送後のA | global=false / A=false / B=false。old origins/positiveLoaded空、ALLOW=false。quiescent既知のためUNKNOWN=false | native停止そのものが終了。**通常no-stop movementを拒否したとはしない**。既存無介入規則を保持 |
| D：単独転送後のB | 新source count80 / capability`{TimeStopCount:80}` / canMove=true、Bは非停止。origins空、UNKNOWN=true (`positive-count-without-native-dimension-stop`) | countコピーをsource provenance/停止開始/許可に読み替えない |
| UOM＋FOREIGN Cow転送 | Cow UUID `8b5b3f99-d12d-483c-ab00-fb2f2c0ccc8c`、object **248530958→1571276838**、ID **17→18**。旧count0、新count80。global=true / A=true / B=falseを維持 | AのUOMが残るため、EndingLib disableのother-source early return。複数source末尾countdown試験は行っていない |
| 同転送後の台帳 | AはCow originを除外、UOMのみloadedだがUNKNOWN=true/ALLOW=false。Bはglobal/dimension不一致でUNKNOWN=true、origins空 | 「残ったUOMらしい集合」から失効済み候補を再許可しない |

EndingLibrary native `setRemoved` hookはcount>0ならnative `use(false, source)`を呼ぶ。copyはその前なので新count80/旧count0となる。native `customDeserializeNBT`がcountからcanMoveを設定するのを観測し、補助は値を直していない。native `restoreFrom`自身が複製用NBTのDimension keyを除去するが、**fixtureがNBTを書き換えたものではない**。persistent NBTは両Cowとも前後`{}`、HP10を維持。capability client同期・描画は今回の対象外。

観測上の区別：rawの`setRemoved-head/return`という補助ラベルは実際には **Entity.remove (`m_142687_`)** を対象にしており、転送の直接`setRemoved` (`m_142467_`)とは別。転送のCHANGED_DIMENSIONは転送戻り後の実state・旧/new lookupと[実runtime bytecode](../build/verification/uom-dimension-20260921-101349/audit/bytecode/Entity-runtime.txt)で確認した。存在しないcallback記録を補完しない。EndingLibraryの本当のsetRemoved hookはruntimeに存在する。

#### stale player / vehicleの最低連携

UOM単独・P有効・UNKNOWN=falseで、player/vehicle各Grantをepoch2/sequence1・2として保持 → FOREIGN Cowをnative useで追加 → そのCowをnative転送 → **同Grant/同packet**を旧Aで配送。UOM自身は旧Aに残る。FOREIGNが去った後でも転送観測で台帳をUNKNOWNに保ち、現在集合だけを見てALLOWへ戻さない。

| 対象 | 実測 | 範囲 |
|---|---|---|
| player1件 | guard1、X delta0 / XYZ不変、比較state不変（元received/time bookkeeping別）、外部native snapshot前後一致 | LIMITED PASS |
| vehicle1件 | vehicle guard1、DENY、Boat/本人XYZ・比較全state不変、payload同一、外部native snapshot一致 | LIMITED PASS。理由ownership=false、identity/dimension/epoch/sequence/liveは全てtrue |
| UOM本人の転送後 | native転送非対応のため、そのsource消失後という入力は成立しない | **NOT RUN / NATIVE NOT SUPPORTED**。上のFOREIGN連携を同じ証拠へ転記しない |

既存MovementGuard/VehicleGuardと2境界Mixinのcompiled bytesは§14.13最終helperと完全一致（[比較](../build/verification/uom-dimension-20260921-101349/audit/guard-equivalence.json)）。UOM/P vehicle ALLOW/DENYはUNDECIDED / NOT LOCKEDのまま。既存17枠/16拒否等の全再実行なし。

#### verification補助の境界・保存観測の限界

- 追加は非取消transfer/PortalInfo/restore/removeの観測と、一時ledger origins失効/UNKNOWN化。転送先にpositive countがあるのにglobal/dimension非停止でもquiescentと見なさない判定を、**今回rootのOwnership候補だけ**へ追加。native count/global/Set/canMove/NBT/health/dimension/player canonicalをobserver/guardから変更する命令なし。[compiled監査](../build/verification/uom-dimension-20260921-101349/audit/guard-readonly-audit.json)。native use、初期player検証credit103/取得、actor/portal準備、native転送・正常終了はfixture刺激に限定し、製品効果/GUI購入成功ではない。
- 一時台帳はUUIDだけでは不十分。同UUIDがnew object/entityID/別dimensionへコピーされるので、dimension＋object/session identity＋epochとnative startの出所を区別する必要がある。今回observerは戻りでold originを失効させ、new originは推測しない。productionの新永続schema/台帳を実装した意味ではない。
- `TimeStopSavedData.save(new CompoundTag())`出力は常に`{}`だった。[原物bytecode](../build/verification/uom-dimension-20260921-101349/audit/bytecode/TimeStopSavedData.txt)はDimensions用ListTagを作成するが、戻りCompoundTagへ追加していない。したがって**空NBTを空dimension Setの証拠にしない**。A/Bはnative `andSameDimension`が実Set.containsを返した値。全Setそのものの独立snapshotやrestart永続性は未確認で、EndingLibraryを修正しない。guard前後の`{}`一致だけから全Set不変を証明したとも言わない。
- helper **44,191 bytes /21 entries / SHA256 `47D9F74B152EFF0AD76B6F3C902D3F0BCABAE64684835A3EDA8202A99A0125D1`**。[識別/非混入](../build/verification/uom-dimension-20260921-101349/audit/helper-01.json)。mod ID `uom_dimension_verification` /version20260921.101349。displayNameには前helper由来のvehicle表記が残るが、配置物のID/hash/sourceは上記dimension専用で識別する。外部class/Jar・製品class混入なし。通常権限の初回dry-runはnative DLL初期化不可、承認済み権限の既存Gradle/cache/offlineでdry-run→専用compile/reobf成功。製品task禁止graph・出力先verification内を確認。fixture測定FAILなし。
- [process](../build/verification/uom-dimension-20260921-101349/server-01/process.json)：10:20:51開始/PID8344、10:21:14–15通常save-all flush/stop・全dimension保存、10:21:16 exit0、後続process不在確認。native停止は最終global/A=false、残存UOM count0。UOMを強制削除したとはしない。再起動/再読込なし。旧run/原本worldへ未接触。ローカル公開鍵stub/EmbeddedChannelであり、実認証の証明ではない。更新照会等はoffline/proxy設定で取得せず、外部MODのPatreon照会ConnectExceptionもlogに保持。
- [最終不変確認](../build/verification/uom-dimension-20260921-101349/audit/final-invariants.json)：製品source/test/build.gradle/Jar/gate/Config/schema/protocol不変、変更はverification新規資料と今回3文書のみ。既存L2133/core60/Cube49/Invader86/loader/実clientは再実行0。FE6site STATIC AUDITED / NOT IMPLEMENTED、TimeStop production NOT IMPLEMENTED、製品FE効果integration0/NOT RUN。両極意IMPLEMENTATION_PENDING/購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、全既存限定PASS/C/sealと個別開始条件・可逆クラフト増加の既知許容仕様を維持。

#### 残確認と次の1作業

一般source転送の未到達は解消、承認UOMの通常native transferは非対応と確定した。**要求のUOM消失後stale許可まで含む総合SOURCE DIMENSION TRANSITION VERIFIEDは宣言しない**。転送exception/observer欠落等の全fault injection、real reconnect、新session/epoch再許可、process/world reload、FE goal再延長、複数source terminal countdown、client input/render/correction、production写像は残る。

次の1作業候補は、**real reconnect後の旧Grant失効・新session/epoch再許可を確認する最小手順のREAD ONLY整理**。既存EmbeddedChannel remove/placeを実接続と混同せず、必要な接続環境と次回実行範囲を具体化する。これは未着手・別承認であり、今回は新たなlifecycle試験/productionへ続行しない。UOM/P車両方針も今回決めない。新artifact不足は今回なし。

上記は10:27時点の次候補。手順の静的整理は後続§14.15で完了し、実行は引き続き未承認・NOT RUN。

<a id="uom-real-reconnect-runbook"></a>
### 14.15 UOM real reconnect — 最小実client / dedicated runbook（2026-09-21 10:56 JST、READ ONLY計画）

この節の「次回」「未作成」「未承認」は10:56時点の計画履歴。手順・期待値は維持し、最新の実行・停止結果は[§14.22](#uom-reconnect-bounded-walk-native-sync-stop)。旧§14.16以降の失敗履歴も保全。

**2026-09-21の計画のみ。R1–R9は全てNOT RUN。新helper/source/packetの作成、compile、server/client/Prism/worldの起動は行っていない。** 次回承認で実施する候補を以下に固定する。製品TimeStop本人免疫は未実装であり、ここでいうGrantは別MOD内のverification-only候補。製品の効果・購入readinessのPASSではない。

#### a. 読取根拠と今回追加する価値

調査・変更前保全：[uom-reconnect-plan-20260921-103304](../build/verification/uom-reconnect-plan-20260921-103304/)。実MC/Forgeのpatched server/clientと承認EndingLibraryを`javap -p -c`で読んだ[bytecode](../build/verification/uom-reconnect-plan-20260921-103304/audit/bytecode/)、[環境照合](../build/verification/uom-reconnect-plan-20260921-103304/audit/environment-inventory.json)を根拠とする。ゲームクラスを実行するテストではない。

- **既存で完了**：§14.11のremove/place・Connection差替え・leaseモデル、§14.12/13の実handler player17枠/vehicle16拒否・held stale、§14.14の一般source native転送/UOM native非対応/FOREIGN転送後の各1拒否。これらは実TCP・正規認証・client入力の証明ではないが、未実施へ戻さず、今回のR1–R9へ全件再計画しない。
- **real connectionで追加**：正規login、socket/channel close→listener logout、同UUIDの新オブジェクト生成、client level/network teardown、旧許可cache消去、新login時の空cache、実S2C grant/revoke、実入力・server位置受理/client収束。1正規account/1 clientで行い、REAL2CLIENTの代替にしない。
- **新たに見つけた実行上の制約**：EndingLibraryのlogin時TimeStop同期はglobalとdimensionが別（後述h）。次回はここを実測する。R4で不一致なら停止し、R5–R9まで成立したと補完しない。

#### b. native切断順序・thread・最小revoke入口

MC1.20.1/Forge47.4.0の通常dedicated接続についての静的順序は次のとおり。OS/Netty closeとserver tickの時刻間隔自体は未実測。

1. socket切断によりNettyの`Connection.channelInactive`→`disconnect`、または先行する`disconnect`がchannelをclose。ここはnetwork thread等で起き得る。閉鎖時点から`isConnected()==false`を許可条件に反映する。
2. 通常のserver main thread上の`ServerConnectionListener.tick`が閉じたConnectionを取り除いて`handleDisconnection`を呼ぶ。`Connection.tick`にも補助経路がある。`handleDisconnection`は`disconnectionHandled=true`を先に立て、現在のpacket listenerの`onDisconnect`を一度通知する。
3. `ServerGamePacketListenerImpl.onDisconnect`→`player.disconnect()`（disconnected flag、passenger解除等）→`PlayerList.remove(player)`。
4. **`PlayerList.remove`の冒頭で`ForgeEventFactory.firePlayerLoggedOut`→Forge `PlayerLoggedOutEvent`を同期post**。player保存・level除去より前。主なrevoke入口はこのeventのHIGHEST handlerとする。event classそのものが別threadへ移すわけではなく、この通常経路ではserver thread。次回は全入口でthread名・`server.isSameThread()`も記録する。
5. player保存、乗物関連の後処理、`ServerLevel.removePlayerImmediately(...UNLOADED_WITH_PLAYER)`→`ServerPlayer.remove`、player list/boss event/UUID map等の除去へ進む。UUID map除去にも「現在登録playerが同じ実objectか」の比較がある。**搭乗中logoutでは乗物保存/除去が加わるため、次回本人は非搭乗のまま**。
6. 再loginは`getPlayerForLogin`でnew `ServerPlayer`、`placeNewPlayer`でnew `ServerGamePacketListenerImpl`をnew `Connection`へ接続し、後半でForge `PlayerLoggedInEvent`。same UUIDをold sessionへ結び直さない。

**primary**：PlayerLoggedOutEventで該当sessionを不可逆revokedにし、active grant/listener sequenceの参照を除去。監査用の旧Grant値は残すが許可検索へ戻さない。別handlerが途中で失敗してもclosed Connectionでは既にDENYになる設計。

**defensive cleanup**：全movement判定時の`isConnected / player.hasDisconnected / current listener / removed`チェック、onDisconnect入口のreadonly記録、Connection終了検出、entity/level removal・server停止で同sessionだけ冪等失効。channel close callbackを使う場合はown sessionのatomic失効印とserver-thread cleanup予約までで、Netty threadからentity/level/canonicalを書かない。level除去だけをprimaryにしない。異常thread/入口欠落/失効漏れは測定停止し、後から強制clearして成功にしない。

#### c. session / Grant候補と旧補助から必要な変更

sessionは`(run ID, server内単調session番号, ServerPlayer参照, listener参照, Connection参照, channel参照)`で識別する。**認可の比較は実参照`==`**。UUID/entity ID/`System.identityHashCode`は監査値であり、hash衝突やID再利用があるため単独のsession keyにしない。channelは実private fieldの読み取りだけで得る。読み取れなければUNKNOWN/停止。run/session番号は認証tokenではなく、Minecraft認証はnative経路へ任せる。

Grantは上記sessionに加えdimension、epoch、単調grant sequence、allowed/revokedを持つ。serverごとのsequenceは再loginでリセットせず、R8でR2より大きい値になる。失効済みGrantの復活・コピー・NBT保存は禁止。旧Grantを比較用に保持する場合もimmutable監査snapshot/失効session参照だけで、active mapへ戻さない。

既存`Ownership.revokedPlayers`はUUIDの集合で、logout後の同UUIDを恒久拒否するモデル。`MovementGuard.grants`はplayer identity mapだが、旧モデルの`invalidate`による置換は本物logout cleanupの実装ではない。**次回別rootの候補はsession単位に置き換える必要がある**。UUID banをlogin時に消すだけ、旧recordをnew playerへ付け替えるだけでは不可。旧補助・world・証拠は変更しない。

新loginは必ずNO_GRANT/allowed=false。停止中の途中参加sessionには`freshEpochRequired=true`を付け、global ledgerが以前からknownでもsessionとしてUNKNOWN/DENYを維持する。logは`ledgerUNKNOWN`と`sessionUNKNOWN`を分け、最終UNKNOWNはそのOR。新sessionの不明を他player全員の許可へ一律転写しない。loaded UOM、count、global/dimension flag、P取得保存だけでは再許可しない。native player logoutのcount操作等でglobal ledgerもUNKNOWNになる場合は、その事実を記録し勝手に解除しない。

R7でnative全終了を観測：global=false、native dimension Set空、対象UOM count0、他の正count source0、処理中use stack0をserverの連続5 tickで確認。この観測で一時ledgerをclean boundaryへ戻し、途中参加sessionに「次の完全観測epochを待つ」資格だけを与える。**停止なしの通常movementはGrant不要**。R8の正式UOM開始を全入口から観測し、UNKNOWN=false、known active非空/全UOM、FOREIGN0、P正常、identity有効の全条件成立後のみnew epoch/new sequenceのGrantを1回発行。途中epochのscanによる復元はしない。

#### d. client transient cache・実入力のための別補助

現補助はserver-onlyの候補計算/packet guardで、real clientの入力許可cache・配送は未作成。次回は別検証namespaceのS2Cのみでserver許可を通知し、必要ならcurrent connectionに紐づくhello/ackを相関用に用いる。C2Sにsource/許可/epochの決定権を与えない。**Food Healing protocol6・製品packet・EndingLibrary Jarは変更しない**。

client recordはlocal Connection/listener/level実参照とserver run/session、dimension、epoch、grant sequence、message revision、短い期限に束縛。enqueue前の受信Connectionを捕捉し、適用時にcurrentと一致しないqueued旧packetは捨てる。new loginは空cacheから開始し、同UUIDの旧recordを復元しない。

| clear入口 | 候補の処理 |
|---|---|
| 通常Disconnect / socket終了 | Forge `ClientPlayerNetworkEvent.LoggingOut`でown cache消去。nullable player/connectionを許容。閉じたconnectionのqueryも直ちに不許可 |
| level unload / dimension変更 | `Minecraft.clearLevel/setLevel`由来のForge `LevelEvent.Unload`とlevel identity不一致で消去。旧level recordを持ち越さない |
| login / player clone | `LoggingIn`/`Clone`で旧binding消去、新bindingのserver通知待ち。Pの通常同期だけでは許可しない |
| new epoch / explicit revoke | 旧epochを消去、最新session/revisionのみ適用。revoke後の古いallowed packetを受理しない |
| 欠落・期限切れ | 許可refreshは候補値100ms間隔、受信後1000msで期限切れ。server自身の単調wall clockとclientの単調wall clockを各側で使用し、止まるgameTimeや両PC時計の絶対一致に依存しない。観測不能/通信停滞時は許可しない |

heartbeatのmessage revisionとGrant発行sequenceは分け、refreshのたびに「新Grant発行」と数えない。期限判定はquery時にも行い、停止でclient tickが止まっても古いtrueが残らない。serverは毎movement packetで現在のidentity/epoch/P/source/UNKNOWNを再照合し、client cacheを最終authorityにしない。切断直前revoke S2Cは配送保証できないため、server失効・send・client receive・local clearを別記録にする。R7の生きた接続でexplicit revokeの実受信も確認する。

**readonly observerだけではR2/R9の実入力は起きない**。EndingLibraryの`MinecraftMixin`はnative `TimeStopUtils.canMove(localPlayer)`でclient input/entity tickを制限する。次回別helperには、current LocalPlayerだけ、native result=falseかつcurrent lease有効の時だけclientで許可するverification-only入力bridge候補も必要。元native trueは保持、server側のnative canMoveは変更しない（serverもtrueにすると既存guardのnative例外へ逃げるため不可）。外部capability/count/global/dimension Set/NBTへ書き込まない。これは**観測ではなく候補介入**として独立log・Mixin設定に分け、natural immunityやproduction実装済みとは記録しない。採用予定は`TimeStopUtils.canMove(Entity)`のclient限定RETURN候補で、実対象class/descriptorは読取済み。client input/render全体の安全証明はこの短い歩行だけでは完了しない。

readonly observerはnative flag/count/canMove、候補effective canMove、client位置/受信packet/GUI/HUDを別項目にする。Grant packetからEndingLibrary flag/dimensionを設定したり、client位置をsetter/独自teleportで補正する実装は含めない。古いpacket再注入・loss全matrixの再実行も含めない。

#### e. 環境・一度だけの準備・作成予定artifact

対象はMC1.20.1/Forge47.4.0、Food Healing `229,494 bytes / 150 entries / SHA-256 5C1A716EEDE7AEB4449B36EEFFBDDA156176B16B60A1676229224AA3390DB327`。次回も現物差分があれば上書きせず停止。FE2.7.20、EndingLibrary2.1.19fix、Iron's Spells3.16.3、Curios5.14.1+1.20.1、GeckoLib4.8.2、Iron's Lib2.1.0の6原物は`build/verification/fantasy-ending-20260920-204205/audit/artifacts/`の承認hashと再一致。同梱物の二重配置なし。Trial/L2/TaCZ/旧helper/旧fixtureを混ぜない。

- **外部artifact追加不足なし（静的照合範囲）**：既知Prism `build/verification/direct-jar-20260913-114729/launcher/prismlauncher.exe`、同rootのJava17/runtime、Minecraft/Forge/LWJGL metadata参照Windows library95件は存在・metadata hash一致。asset index5のSHA1一致、参照3,598 objectは存在/size一致（全object hash再計算ではない）。dedicated `build/verification/trial-monolith-20260915-125400/runtime-forge4740/forge-args.txt`の参照Jar58件も存在。新構成の起動PASSではない。
- helperを次回だけ作成する新rootは`build/verification/uom-reconnect-<新run ID>/`。`helper/src/main/java/verification/uom/reconnect/`の`ReconnectVerification`（run/path/hash/本人と一度prepare・case進行）、`SessionRegistry`/`TransientGrant`（lifecycle不可逆失効）、`Ownership`（既存ledger必要部分のみ）、`MovementGuard`/`VehicleGuard`（各既存handler境界と共通のsession predicate）、`ClientLease`/`VerificationNetwork`/client-only入力bridge、`ReadonlyObservers`を予定。resourcesに独立MOD metadata/Mixin/refmap、rootに専用Gradle init・runner・receipt/journal/allowlist。**今回これらは未作成**。P取得補助は旧Trialのfresh/103SP/receipt構造だけ参考にし、旧attack fixtureを持ち込まない。
- 次回ローカルGradle8.1.1、既存`-g <LOCAL_GRADLE_HOME> --offline --no-daemon`、新rootの専用compile/reobf taskだけ。task graphから製品jar/reobfJar/build/通常testを除外し、出力も新root内。補助Jarのmetadata/refmap/client分離を確認し、新serverと新instanceだけに同じ補助Jarを置く。製品Jar再生成なし。
- 新dedicatedは`online-mode=true`、loopback `127.0.0.1`の空きport、1 clientだけ。既存Prismの正規accountを通常UIで使う。過去にはoffline profile実行もあるため**現在の認証有効性は未確認**。認証UIが出た時だけ本人入力。token/account/passwordファイルを読取・コピーしない。通常Microsoft/Minecraft認証通信は必要で、次回実行範囲に含める。既存自動fixtureのfake login・localhost公開鍵応答・認証を遮断するproxy設定は持ち込まない。外部artifact downloadとは分離し、認証失敗をofflineへ切替えて回避しない。
- 新worldだけを生成。本人入場前にconsoleで`doMobSpawning=false`、`naturalRegeneration=false`、Survival/Normalを設定し応答・保存値を確認。安全な不透明屋根/床/壁の歩行区画と別のUOM隔離室を作り、待機中の侵入・落下・窒息・火等なしを確認。旧instance/world/receipt/journalは再利用しない。
- 初回S1はfresh確認のみ。R1の通常再接続後S2で本人UUID/path/run/Jarを束縛し、明示prepare1回：`ShokugiProvider`の正常fresh canonicalへ`addUnspentSkillPoints(103)`→`trySpendSkillPoints(103)`→`foodhealing:purification`/`foodhealing:purification_mastery`各Lv1・disabled=false→`CapabilityEvents.syncToClient`。credit103/使用済103/未使用0。Truth取得0、他取得/Rootなし。GUI購入成功ではない。PREPARING receiptを変更前にcreate-newし、失敗・再入場でも再実行禁止。再接続は通常player保存/読込/同期だけで引継ぎ、login/tickから再付与しない。
- HP20/MAX20・effects空・native player count0/canMove=false・正常canonicalをread-only確認。HP/flag/effect/位置/onGroundを修復しない。通常GUIは両node Lv1/ON・SP表示の確認だけで、P OFF試験は今回増やさない。
- sourceは新規正式`fantasy_ending:ultimate_order_manager`1体のみ、初期NoAI/PersistenceRequired、targetなし・封鎖室。NoAIだけでFE独自tickが止まるとは仮定せず、安定待機20 server tickでtarget/攻撃/移動/未知useがないことを確認。自然boss戦・FE goalの再現ではない。source/測定区画chunkを新world内の通常forceloadで維持し、唯一playerが切断中もunloadしない。HP増加/無敵/age/count修復・UOM native移動は使わない。
- native開始は明示予約で`TimeStopUtils.use(true, source, true, 12000, false)`を各epoch1回（最大2回）。10分相当の固定durationは人間にtick競争を要求しないための検証入力で、FE goal既定時間/再延長のPASSではない。countを止めたり途中補充しない。終了はnative `use(false, source, true, 0, false)`、外部flag/setterで作らない。R4前に自然終了/unloadしたら旧epoch継続条件未成立として停止し、都合よくepochを作り直さない。

次回の**未実装の補助command予定**はserver console限定の`uom_reconnect prepare <本人UUID>`、`begin R2`、`probe R5`、`probe R6`、`end R7`、`begin R8`、`finish`（最後のnative終了/seal）、`observe <label>`（採取のみ）。全て同じroot command下のサブcommandとする。run/実world path/本人/Jar/現在phaseを照合し、prepareと各begin/probe/endは書込前にFIRINGをcreate-new記録、成功時だけDONE。失敗・再送・wrong phaseは拒否しreceiptを消さない。loginで自動begin/prepareしない。R3/R4のlogout/loginが本当に完了したeventを次phaseの条件にし、チャットの「再接続しました」だけで進めない。通常操作が停止中に妨げられてもplayer側commandやsetterへ迂回しない。

#### f. 全段階共通のreadonly証拠・合否

server：wall clock/thread/server tick、player UUID/entity ID/実参照の一致比較・identityHashCode、listener/Connection/channel各参照・open/active、server session番号、dimension、native global/Set contains/全Set copy、UOM UUID/type/count/alive/removed/loaded、known UOM/FOREIGN数、処理中stack、current epoch、Grant epoch/sequence/allowed/revoked、global ledgerとsessionのUNKNOWN/理由、P acquired/disabled/pending、HP/MAX/effects、XYZ/previous/lastGood/velocity/fall/onGround/awaiting teleport/floatingを記録。

client：Connection/listener/level/localplayer identity、cache空/許可/失効/期限/revision、受信grant/revoke/packet元binding、native TimeStop global/dimension/context flag・native/effective canMove、P canonical/GUI、HP/HUD、XYZ/input/補正packet、画面時刻。clientのcap countやNBTをserverと同じと仮定せず同期有無を明記。native server Setのserialized save`{}`は§14.14の実物制限があるため空Setの証明に使わない。値の取得用serializationは一時tagのみで元stateを書き換えない。

session参照はS1≠S2≠S3、UUIDは同一を期待。entity IDやidentityHashCodeの数値差自体は合格の必須条件にせず、参照比較を採用。GrantはG1 revoked後復活0、G2.session=S3、G2.epoch>E1、G2.sequence>G1。

通常movementケースは安全区画で短いW入力を1回ずつ。入力前後の実`handleMovePlayer`受理と水平移動0.05–2block、停止後2秒以内のserver/client位置差≤0.05blockを限定合格基準にする。HP20/MAX20/effects空を維持、未知teleport/継続drift/壁貫通なし。入力が短すぎてmovement packet0なら「未入力/UNVERIFIED」で、observerのsetterで動かさない。観測区間の始点はserver受信で機械採取し、人間にtickを測らせない。client/server native状態・P同期は5秒以内、2連続sample（100ms以上離す）一致を待ち、timeoutで停止。描画全frame・全遅延・全input/renderのPASSへ拡張しない。

#### g. 最小R1–R9（最大3 login・2 reconnect・2 native開始）

| case | 開始状態・通常操作 | readonly期待値・合格条件 |
|---|---|---|
| R1 | 正規accountでS1接続、停止なし。通常Disconnect→logout完了確認→同account再接続S2、短い歩行 | global/dim=false、Grantなしでも正常movement。same UUID/new player/listener/Connection/channel/空cache。freshデータを修復せずS2でprepare1回・GUI照合 |
| R2 | S2、正常P、source1体、安全準備済。native E1開始を全入口観測→G1一度発行→歩行 | native global/dim=true、knownUOM1/FOREIGN0/UNKNOWNfalse、server本人native canMove=false、G1.current。実S2C後client候補true・位置受理/収束。server/client/P/HP不一致なし |
| R3 | R2のG1/session/E1/sequenceを保持記録、本人非搭乗のまま通常Disconnect | 実channel close→onDisconnect→LoggedOut→保存/removal。G1不可逆revoked、active grantと旧listener参照なし、client cache空。native UOMのE1は同じまま。revoke送信と受信の有無は別判定 |
| R4 | serverのlogout完了を先に確認し同正規accountをS3として再接続。まだ歩かない | UUID同一、全session参照新規、Pは通常保存同期で保持、NO_GRANT、途中E1はsessionUNKNOWN/DENY。旧cache復元なし。**hのnative同期が不一致ならここで停止** |
| R5 | R4が収束したS3/E1/UNKNOWN。監査用G1を新Connectionとの比較へ一度だけ渡す | 実new player/listener/Connectionに対する同じidentity/session guardのpredicateがfalse。current grant mapへG1をinstallしない。実packet跨session再注入0、位置不変。旧17枠をやり直さない |
| R6 | 同じS3/UNKNOWN、旧session G1をvehicle認可境界の共通session predicateへ1回 | old session不一致/DENY。vehicle packet配送0・搭乗0・vehicle ALLOW採否0。旧16拒否はそのまま保持し、新real Connectionとのidentity確認1件とだけ記録 |
| R7 | E1のnative use(false)を一度。client level ready時に行う | count0/global=false/全Set空・use stack0の連続5 tick、client native停止なし・G1はrevokedのまま・explicit revoke受信/cacheなし、Grant不要で通常歩行復帰 |
| R8 | S3でclean boundary成立後、同sourceのnative E2を最初から観測 | known非空全UOM/FOREIGN0/UNKNOWNfalse/P正常/identity有効の時だけG2発行。E2>E1、新sequence、S3に束縛。native global/dimensionとclient同期一致 |
| R9 | G2のS2C受信後、短い通常歩行 | 実packetはS3/G2で受理・位置収束。G1.revoked=trueのまま、旧許可の復活ではない。HP20/effects空維持後native終了・sealへ |

R5/R6はpure guard照合をreal sessionの実objectで行う。synthetic packet replayの成功、実vehicle移動、車両免疫の仕様決定とは呼ばない。R4の停止入力確認でnative clientが入力を抑えpacket0だった場合も「server handlerがpacketを拒否した」とは数えない。主目的は不可逆失効とnew epoch再許可の区別。

#### h. R4に関するnative実物の懸念と停止分岐

EndingLibrary2.1.19fix `CommonEventHandler$TimeStopEvents.onPlayerLeave(PlayerLoggedOutEvent)`は、停止dimensionの退出player countをnativeに0へし、**そのplayer宛だけ**`TimeStopSkillPacket(false,false,playerId)`を送る。UOM/sourceのcount/globalをhelperが修復してよい根拠ではない。`onPlayerLeave(PlayerLoggedInEvent)`の別overloadは停止中loginに`TimeStopSkillPacket(true,false,-1)`を送る。

後者はclient `TimeStopUtilsWrapped.enable`でglobal=trueを設定するが、`currentTimeStopDimension`を設定しない。new ClientLevelの同field初期値はnull。承認Jarのfield参照は`ClientLevelExpandedContext`と`TSDimensionSynchedPacket`のみで、login handlerからdimension packet送信はない。`andSameDimension(clientLevel)`はこのfieldの非null判定である。**再接続直後global=true/dimension=falseとなる懸念を静的に確認したが、real再現結果ではない。** 他のnative同期が実測で届くかをR4のpacket/flag観測で分離する。届かず5秒収束条件を満たさなければR4のidentity/空Grantまでの部分結果を残し、R5–R9をNOT RUNとして通常終了。補助からdimension packetを補送、EndingLibrary flagを書換、old epochをrestartして成功へ合わせる案は含めない。

両native TimeStop packet handlerにはclient level=null時の`System.exit(-1)`分岐もある。新native開始/明示終了はclient level ready確認後、または完全logoutしてserver online player0になってからに限定し、handshake/level teardown中に補助が送信を発生させない。native logout自身のpacket競合が起きた場合は停止証拠として残し、外部handlerを改変しない。

#### i. 停止・終了・主体・次回承認範囲

identity/observer欠落、認証/reconnect失敗、P不一致、clean boundary後もUNKNOWN解除不能、source由来不明、UOM攻撃/移動/未知use、client/server native state不一致、stale許可残存、予期しない移動/被弾/回復/死亡で次caseへ進まない。R4で予定しているUNKNOWNそのものは期待値だが、不明理由やnative同期不一致を隠さない。receipt/journal削除、再prepare、HP/effect/位置/count/Grantの手修復、同case再送は不可。

**dedicatedではEscはserverをpauseしない**。異常時は証拠snapshotを採取して速やかに通常Disconnect、退出を確認する。成功時はR9後native終了→own transient失効・seal→通常Disconnect。失敗時のnative終了はlevel readyで安全に行える時、または完全logout/online0後だけ。終了がnativeに成立しなければそのまま失敗記録を保全し、通常server stopへ進む。外部stateを修復して「clean」と記録しない。

serverは`save-all flush`→保存完了→`stop`→全dimension保存/正常exitを確認。clientはtitleのQuit Game、該当client/server PID終了（commandline/tokenの収集不要）、Prismに戻る。保存と競合しない時点で本人canonicalのP/M各1・両ON/SP0・103/HP20・準備receipt1、Grant永続化なしをread-only照合し、log/case journal/両側snapshot/F2/hashを集約。**今回のworldは再起動/再読込しない**。認証再接続でのnative player保存読込とprocess/world reloadを区別する。own forceloadは通常終了準備で解除、外部sourceの生命値等を初期化しない。

COMPUTER USE：次回Prism新instance、通常account選択、接続/Disconnect/再接続、GUI/F2、短い歩行入力が可能なら実行、Quit。HUMAN：実際に認証が要求された場合と、利用可能なComputer Useで歩行入力が成立しない場合だけ。後者は安全区画で短い通常歩行→停止を一括依頼し、厳密tick/戦闘/食事長押しは要求しない。AUTOMATED：限定helper compile/reobf、初期一度prepare/native trigger、server/session guard候補、独立readonly observer、証拠照合/save-stop/process/hash記録。認証値を補助へ渡さない。

**次の1作業**：このrunbookの実行承認後、新rootの別補助作成・専用offline compile/reobf・Jar検査→新Prism instance/新dedicated world→1正規accountで条件が成立するR1–R9→native終了/sealまたは停止分岐→通常保存/両process終了→3文書更新。通常認証通信とローカル接続は含むが、外部download・旧world/run・旧suite再実行・production改変は含まない。追加外部artifact要求なし、**未作成の専用helperと実行承認・通常認証の成立**が必要。今回の計画完成後に実行を開始しない。

成功してもprocess/world reload、FE goal途中再延長、複数source terminal countdown、transfer fault/observer欠落、client input/render/correction全般、productionへの写像は別判定。**PLAYER + VEHICLE SERVER DENY BOUNDARY VERIFIED（LIMITED）、GENERAL SOURCE NATIVE TRANSFER CONFIRMED（LIMITED）、UOM NATIVE DIMENSION TRANSFER NOT SUPPORTEDを維持。SOURCE DIMENSION TRANSITION VERIFIED=NO / SAFE DESIGN PROVEN=NO / BLOCKED - TIME STOP SOURCE OWNERSHIP。** FE6site STATIC AUDITED/NOT IMPLEMENTED、TimeStop production NOT IMPLEMENTED、FE製品統合0/NOT RUN、両極意IMPLEMENTATION_PENDING/購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、UOM/P車両方針UNDECIDEDを維持。他機能の個別gate・旧L2/Trial等のPASS・食料生産の極意の可逆クラフト増加の既知許容仕様は変更しない。


<a id="uom-real-reconnect-startup-result"></a>
### 14.16 UOM real reconnect — 初回client起動失敗（2026-09-21 11:52 JST）

利用者の今回承認で§14.15を実行。**BLOCKED - REAL CLIENT STARTUP / FANTASY ENDING SHADER**。[判定集計](../build/verification/uom-reconnect-20260921-112109/audit/reviewed-results.json)、[実物/配置hash](../build/verification/uom-reconnect-20260921-112109/audit/final-placed-hashes.json)、[server実ロード](../build/verification/uom-reconnect-20260921-112109/audit/runtime-server.jsonl)。§14.15の手順を作り直した結果ではなく、承認済み起動準備と実client起動の結果である。

#### a. 成果物・境界・補助修正

- 新root `build/verification/uom-reconnect-20260921-112109`。新server/world、新instance `FHR_UOM_Reconnect_20260921-112109`。既存world/旧fixture/Trial/L2/TaCZなし。原物7本と配置hash一致。製品 **229,494 bytes/150 entries/SHA256 5C1A716EEDE7AEB4449B36EEFFBDDA156176B16B60A1676229224AA3390DB327**を変更していない。
- 別helper `uom_reconnect_verification` version `20260921.112109`、**58,256 bytes/35 entries/SHA256 D83C8A675EEF543AAF5D0D12ABFF8EA8AFD089603571E76F3DC6652F58DDA9ED**。[source](../build/verification/uom-reconnect-20260921-112109/helper/src/main/java/verification/uom/)、[専用init](../build/verification/uom-reconnect-20260921-112109/reconnect.init.gradle)、[Jar](../build/verification/uom-reconnect-20260921-112109/artifacts/uom-reconnect-verification.jar)、[compiled監査](../build/verification/uom-reconnect-20260921-112109/audit/helper-audit.json)。`SessionRegistry.Grant`が予定TransientGrantの役割を持つ。session実参照・不可逆失効、S2C lease/current client context、client-only input bridge、独立readonly native observer、console限定一度prepare/case receiptを実装したが、実sessionのPASSではない。
- 最初のsandbox Gradleはnative-platform.dllアクセス失敗。既承認の権限付きローカルGradle8.1.1/既存cache/`--offline --no-daemon`で専用graphを確認。製品compileJava/jar/reobfJar/build/check/unit/既存suiteなし。graphの`downloadMcpConfig`はofflineの既存cache処理であり新artifact取得なし。初回compileはdecoder long数・Difficulty import・LazyOptional supplierの3ミス。helper内だけ修正しcompile02成功。
- 初回server PID18772はPlayerObserverが継承されたremoveをServerPlayer宣言メソッドとして指定してMixin適用FAIL/exit1。**world生成/接続/prepare前**。実Player.removeへobserverを分離してcompile03/reobf成功。旧Jar・compile01・起動01ログ保全。requireを0へ緩めず、製品/外部MODを変更していない。client leaseは非本人entityのcanMove照会で本人cacheを消さないよう起動前に修正。外部stateへsetter/storeせず、native useは明示case/cleanupに限定し実呼出し0。

#### b. 起動・停止原因と実施範囲

server起動02/PID28708はMC1.20.1/Forge47.4.0、FE2.7.20、EndingLibrary2.1.19fix、Iron's Spells `1.20.1-3.16.3`、Curios5.14.1+1.20.1、GeckoLib4.8.2、Iron's Lib `1.20.1-2.1.0`、Food Healing3.0.0、当該helperをロード。同梱playeranimator1.0.2-rc1+1.20・MixinExtras0.4.1も実ロード、外側へ重複追加なし。

新serverはonline-mode=true/127.0.0.1:52292/max1/Survival/Normal。入場前に自然湧きfalse・自然回復false・spawnRadius0を通常commandで設定/再読取し、床・四方壁とUOM隔離室、chunk forceloadを準備。敵entity検査は0。**client未入場のため本人HP/MAX/fresh/physical safetyや区画の実画面確認は未実施**。通常spawnに干渉しない床/側壁までの段階で止まり、歩行室屋根を含む測定前最終安全確認は未完了。安全準備全PASSとはしない。

COMPUTER USEで新instanceのみ選択してPrism通常起動。Prism表示は`Launched instance in online mode`、選択account <MINECRAFT_ACCOUNT>。認証ファイル/token/passwordの読取・コピー、自動認証入力、offline/fake authなし。ただし**実dedicated login/TCPは0で認証成立はUNVERIFIED**。本人再認証ダイアログや人間操作は発生しなかった。

**11:43:26、title/接続前のRegisterShadersEventでclientクラッシュ**：[crash](../build/verification/uom-reconnect-20260921-112109/audit/client-startup-failure/crash-2026-09-21_11.43.26-client.txt)、[client log](../build/verification/uom-reconnect-20260921-112109/audit/client-startup-failure/latest.log)。`com.mojang.blaze3d.shaders.Uniform`を`com.mega.uom.client.render.shader.core.MUniform`へcastできない。native FE `MShaderInstance.getUniform:118`→ShaderInstance constructor→CosmicShaderInstance→CosmicItemShaders.onRegisterShaders→ClientModEvents。補助の初回server Mixinエラーとは別の停止理由。例外地点は確認できたが、Uniform生成元/他native変換との競合/必要版までは未判定。shader patch・登録skip・依存差替・描画Config変更・client再起動を行わず停止した。

#### c. R1–R9の実判定

| case | 今回 | 到達範囲・未観測 |
|---|---|---|
| R1 | **NOT RUN** | client起動失敗。S1/S2・auth/TCP・logout/new identity・歩行未実施 |
| R2 | **NOT RUN** | prepare0、UOM生成0、E1/G1/lease0 |
| R3 | **NOT RUN** | G1なし。実native disconnect順序/失効を未観測 |
| R4 | **NOT RUN** | S3なし。global/dimension同期の静的懸念は今回再現・解消とも未確認 |
| R5 | **NOT RUN** | oldG1/new Connection比較0 |
| R6 | **NOT RUN** | vehicle pure predicate比較0、搭乗/packet0、車両方針未LOCK維持 |
| R7 | **NOT RUN** | native停止開始0、終了・clean boundary・通常歩行未測定 |
| R8 | **NOT RUN** | E2/G2/sequence未発行 |
| R9 | **NOT RUN** | client入力/packet/位置収束未測定、sealなし |

R4不一致を観測したという記録へ置き換えない。G1/G2のsession/epoch/sequenceは**不存在**、空cache/revokeのreal lifecycleもUNVERIFIED。prepare/FIRING/DONE/SEALED receiptなし、playerdata0。HP/position/onGround/EndingLibrary値修復・packet補送・epoch作り直し・同case再送なし。既存17枠/16拒否・一般source転送・L2/Trial/core suiteの追加実行0。

#### d. 保存・終了・不変確認

11:44:09 `list` online0→中止記録→`save-all flush`全dimension保存完了、11:44:49通常`stop`、11:44:50 server exit0。[console](../build/verification/uom-reconnect-20260921-112109/audit/server-console-02.log) / [process](../build/verification/uom-reconnect-20260921-112109/audit/server-process-02.json)。UOM/native停止を開始していないためcleanup useは0。終了後の[readonly level.dat照合](../build/verification/uom-reconnect-20260921-112109/audit/saved-state-readonly.json)でもSurvival/Normal・自然湧きfalse/自然回復false/spawnRadius0・playerdata0を確認。[最終不変/リンク検査](../build/verification/uom-reconnect-20260921-112109/audit/final-checks.json)は既存src全141ファイルの追加削除/内容変更なし、baselineの変更は許可された3文書だけ。clientはPrism画面で**終了コード−1**、通常Quit未実施/クラッシュ終了と区別。[process不在](../build/verification/uom-reconnect-20260921-112109/audit/process-after.json)、[UI観測記録](../build/verification/uom-reconnect-20260921-112109/audit/ui-observations.md)。サーバーworld再読込0、原本/過去worldへのアクセスなし。

製品source/既存test/AGENTS/build.gradle/製品Jar等144ファイルSHA一致、文書更新前は3文書を含む147ファイル一致。[変更前比較](../build/verification/uom-reconnect-20260921-112109/audit/pre-document-unchanged.json)。製品Config/gate/protocol6/schema/migration無変更。別helper以外のproductionや既存試験buildなし。.git不在、git diffを実施済みとはしない。

#### e. 次の1単位

**承認原物のclient shader初期化をREAD ONLY切り分ける**。今回crash/既存実Jar/実変換ShaderInstanceから、MUniform生成・登録とEndingLibrary変換の関係を限定確認する。新artifact・別版・外部修正が必要かはまだ断定しない。実clientがtitleまで正常起動できる根拠と必要変更の承認が揃うまでは、新run/再起動/既存world再利用を始めない。完成したreconnect helper/計画は再利用候補として保全し、認可ロジックを最初から作り直す計画にはしない。

**SAFE DESIGN PROVEN=NO / SOURCE DIMENSION TRANSITION VERIFIED=NO / BLOCKED - TIME STOP SOURCE OWNERSHIP**。FE6site STATIC AUDITED/NOT IMPLEMENTED、TimeStop production未実装、製品FE統合0。既存player/vehicle DENY限定・一般source native転送・UOM native非対応、L2/Trial/C/sealのPASSを維持。P/T IMPLEMENTATION_PENDING・購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、Flight/Break Realm/Bulwark/試作型機関弩/FOURTH BOOT等の開始条件は不変。可逆クラフト増加は既知許容仕様・バグ修正対象外。


<a id="uom-shader-readonly-diagnosis"></a>
### 14.17 FE2.7.20 client shader — READ ONLY原因切り分け（2026-09-21 12:11 JST）

**結論：A（Fantasy Ending配布実装のMUniform生成/cast契約不整合）を直接原因として確認。** 親のprivate parserをFEの同名public methodがoverrideできない。通常Uniformを生成した後、overrideされるpublic getterのbridgeだけがFEへ入りcast失敗する。これはTimeStop ownership試験の失敗ではない。対象構成での実装契約の不一致であり、Forge47.4.0固有の回帰や「別版なら直る」までは証明していない。

今回の承認は保存済み証拠/原物の読取と3文書更新のみ。ゲーム・Prism・server/world・build/testを起動せず、外部MOD/Config/製品/補助を変更していない。旧run `20260921-112109`のclient crash/正常server終了・途中補助失敗も保全。§14.16は当時の実行履歴で、同節eのREAD ONLY切り分けは本節で完了した。

#### a. 証拠・原物・取得限界

- [照合結果](../build/verification/shader-readonly-20260921-115707/reviewed-results.json)、[承認原物とnested全体のsize/entries/SHA](../build/verification/shader-readonly-20260921-115707/inventory.json)、[対象class origin/CRC/SHA](../build/verification/shader-readonly-20260921-115707/shader-class-providers.json)、[原物/既存export class索引](../build/verification/shader-readonly-20260921-115707/bytecode-index.json)、[参照検索/追加disassembly](../build/verification/shader-readonly-20260921-115707/detail-index.json)、[Mixin/AT/services/metadata原文](../build/verification/shader-readonly-20260921-115707/metadata.json)。調査用PythonはZIP読取・hash・javap・文書更新だけで、実client補助ではない。
- 承認6本の原物とserver/client配置copy、製品、専用補助を再hash照合。FE `E32FD4BA…E5D141`、EndingLibrary `0E29AF51…6CDC34`、Iron's Spells `54B5AAA5…480FB`、Curios `1E817919…8E51F`、GeckoLib `A2E4BCC9…9FFDC`、Iron's Lib `DB4C7CC8…5BAF097`一致。フル値はinventory。補助58,256 bytes/35 entries/`D83C8A675EEF543AAF5D0D12ABFF8EA8AFD089603571E76F3DC6652F58DDA9ED`不変。
- 既存client `.mixin.out/class`のShaderInstance（11:43:21、SHA `1FD5107E062A19D5CBD4126EBDC339453C5012ECC4E9E1C0B8EBAB340B4A73DF`）とUniform（11:43:22、SHA `9BE2E68AA0C7A673D240F41E98F65AF3AE8034617A7F0AA139FD1E2858DB9F59`）、両Accessorを読取。これは**前runのclient起動時のMixin export**であり、全後段transform後のheap/classdumpを新採取したものではない。MShaderInstance自身のexportは存在しないため、原物bytecode＋crash stackのoriginで照合した。
- `ShaderInstance`のparser/getterは、MC1.20.1/Forge47.4.0で使ったlocal SRG classとexportで命令・可視性が一致（CP番号/debug table除外）。getter/mapのobjectをheap取得したわけではなく、生成命令・map経路・実ClassCastExceptionの型で確定する。生存JVMのresource URL取得は未実施。
- [最終不変照合](../build/verification/shader-readonly-20260921-115707/final-checks.json)：既存src141ファイルの追加削除/内容変更なし、AGENTS/build.gradle/製品Jarを含む144ファイルSHA一致。baseline変更は許可された3文書のみ。製品229,494 bytes/150 entries/SHA `5C1A716EEDE7AEB4449B36EEFFBDDA156176B16B60A1676229224AA3390DB327`不変。[readonly process確認](../build/verification/shader-readonly-20260921-115707/process-readonly.json)でJava0件。.git不在、git diff実施の主張なし。今回の調査証拠は別ディレクトリへ保存し、旧runを修復/再利用していない。

#### b. 最小call chainとUniform生成

`ClientHandler$ClientModEvents.onRegisterShaders`（RegisterShadersEvent）→ `CosmicItemShaders.onRegisterShaders:50` → `CosmicShaderInstance.create:21 / <init>:16` → `MShaderInstance.<init>:29` → `ShaderInstance.<init>:193` → FE bridge `m_173348_:24` → `getUniform:118` → **Uniform→MUniform ClassCastException**。11:43:26、title/初回接続より前。[旧crash](../build/verification/uom-reconnect-20260921-112109/audit/client-startup-failure/crash-2026-09-21_11.43.26-client.txt)。

| 実経路 | bytecodeで確認した処理 |
|---|---|
| 親constructor JSON入力 | ResourceProvider/ResourceLocation/VertexFormat版constructor。uniforms配列各要素にoffset416 `invokevirtual ShaderInstance.m_173354_(JsonElement)V`。命令名がinvokevirtualでも**解決先methodがprivate**のため子の同名methodはoverride先にならない |
| 実生成 | 親の`private m_173354_` offset240 `new Uniform`、253 `<init>(String,int,int,Shader)`。336–342で`f_173331_:List<Uniform>`へadd。新しいMUniform factoryは呼ばない |
| map登録 | constructor offset582→`m_173366_()`でnative uniform locationを解決。location≠−1ならmap `f_173333_:Map<String,Uniform>`へ**同一object**をput（253）。list/mapはGuavaのnewArrayList/newHashMapで生成、MUniform専用collectionではない |
| 取得/失敗 | constructor offset659の`m_173348_("ModelViewMat")`が最初の取得。FE public bridge（ACC_BRIDGE/ACC_SYNTHETIC）→`getUniform(String):MUniform`→`invokespecial ShaderInstance.m_173348_`→map.get→offset5 `checkcast MUniform`。ModelViewMatはJSON/GLSLに実在し、nullでなく通常Uniformだったことが例外と一致 |
| 差分の重要点 | 親parser/getterの生成/取得命令はexport前後で一致。MUniform化されたmapを別MODがUniformへ戻した証拠はない |

#### c. FEが意図する専用factoryと適用状況

`MShaderInstance extends ShaderInstance`、`CosmicShaderInstance extends MShaderInstance`。FEはgetterだけでなく**public `m_173354_(JsonElement)V`**も持つ。name/type/count/valuesを読み、`UniformType.parse`→offset260 `MUniform.makeUniform(String,UniformType,int,Shader)`→値をparseして`glUniformI/F/D`→FE Accessorの`getUniforms().add`まで実装される。MUniformはUniformのsubclassで、carrierに応じIntUniform/FloatUniform/DoubleUniformをnewする。constructorはsuper後にapplyCallbacksを初期化するだけで、親constructor前後に通常Uniformを変換/置換する処理はない。**このparserは親privateと接続していない**。別のvirtual createUniform factory overrideもない。

| 提供元・対象 | 宣言と実適用 | 生成経路との関係 |
|---|---|---|
| FE `AccessorShaderInstance` | `mixins.uom.json` client、required=true、config priority=2147483647、defaultRequire=1。listとblendのAccessor。独自version条件/Inject require/expectなし。debug914–916のうち916と実exportで適用確認 | List<Uniform>を取得するだけ。parserのvisibility変更/NEW差替なし |
| EndingLibrary `accessor.AccessorShaderInstance` | client/required、priority未指定、defaultRequire=1。debug914とexportで確認 | FE側と同じList<Uniform>/blendのAccessor。MUniform factoryではない |
| EndingLibrary `advanced.client.ShaderInstanceMixin` | client/required、priority未指定、defaultRequire=1、Inject require/expect個別指定なし。target `<init>(ResourceProvider,ResourceLocation,VertexFormat)V` の **RETURN**。debug915＋exportにhandler | `_ProgramTime`/LevelModelViewMat/LevelProjMat/CameraPosをgetterで取得する。注入呼出offset845は失敗offset659より後で、今回そこへ未到達 |
| EndingLibrary `accessor.AccessorUniform` | client/required、debug1198＋Uniform exportで適用確認 | uniform fieldアクセス。生成classを変えない |
| FE Access Transformer | Uniform buffer public-fやUniform `*()`等はある。ShaderInstance/`m_173354_`/parseUniformNodeをpublic/protectedへ変える宣言なし | **不足しているoverride契約を成立させない** |
| FE/EL coremod・launch plugin・reflection | FE FeClassProcessor（FeMapping/InscriptionTableScreen）、LivingEntityCheckTransformer/SoftGetHealthClassVisitor、EL Normal/Bugfix/MillisTime/Annotation processors・plugin builderを読取。Mixin pluginのpre/postApplyは空、共通ApplyCheckはannotationによる採否 | parserを公開化する処理、new UniformをMUniformへ置換する処理、該当mapのMUniform化は確認されない。反射/agentの存在だけをshader競合としない |
| Iron's Spells / GeckoLib / Iron's Lib / Curios / Forge | Mixin JSON・AT・class参照・Forge coremod定義を確認。Spellsのoverlay、GeckoのAutoGlowingTexture等はshader利用側。ShaderInstance/Uniformへの追加Mixinは今回debugにない | このcastを生んだ変換競合の証拠なし。ForgeのResourceLocation constructor/event経路は使用されているが、parserのprivate契約を変えない |

FE/EL shader対象Mixinには共通ApplyCheckのModDepends/DevEnv等を指定する独自annotationなし。**「宣言されている必須factory Mixinが失敗/不適用だった」とは確認できない**。Dとして別依存を要求する根拠はない。FE/ELはshaderへ両方介入しているが、対象が同じだけでは競合成立ではない。

#### d. class origin / nested / loader

| class | 単一提供元 | CRC32 / SHA-256 |
|---|---|---|
| MShaderInstance | FE2.7.20 outer | CF2F989A / ECFDC24E3F0066C1FE7620530282A37B69BC65F9422D6F6AB73E48D7C4FABAEC |
| MUniform | FE2.7.20 outer | 6B178B92 / 8C7AC8F7516423CCF29C5DCE9AD9F3C0A81E411F79EAFE661C54B8772ED3D980 |
| ShaderInstance | local MC client SRG | 5F012502 / 407719D04AA7594C2FCCD385FC661CD14E46785D9BCD35DA4566C86C88A24A3E |
| Uniform | local MC client SRG | 74C88D44 / 53CA75B09588167D5C52B8A995AD10A8460339FB3C628C90BAE48A8D0FA6D50F |

実crashのcode-sourceはFE `…jar%23166!/`、MC `…srg.jar%23172!/`、module `fantasy_ending@2.7.20`/`minecraft@1.20.1`、同じTRANSFORMER loader。EndingLibrary由来のMShaderInstanceではない。アーカイブorigin/resource-entryとruntime stackのcode-sourceは区別して記録。

- EL nested PlayerAnimator `1.0.2-rc1+1.20`（181,437 bytes、SHA `90D9965C…52691`）を実ロード。FE nested agent-any1.0（8,168 bytes、`D0ADDEFE…0B526`）はloader module名agent.any/version0.0NONEとして選択。Gecko nested mclib-20（54,407 bytes、`74FAC6A1…7B949`）はmclib/0.0NONE。
- FE/ELのnested mixinextras-forge0.4.1は両候補とも183,573 bytes/SHA `9D48CB0A…E3066`、その内側MixinExtras0.4.1も195,894 bytes/`D13C480D…68374`でbyte同一。debugでMixinExtras候補2→選択、service0.4.1初期化確認。どちらの同一bytesを選んだかouter originまでは選択行だけで断定しない。
- 上記nested（さらに内側も含む）に対象4classの重複なし。outerへの展開/重複配置はしていない。nested存在だけをE/Cとしない。

#### e. Cosmic resourceと型の区別

登録対象は`fantasy_ending:cosmic`のみ（cosmic_2は今回の登録callではない）。[JSON](../build/verification/shader-readonly-20260921-115707/resources/assets/fantasy_ending/shaders/core/cosmic.json) / [vertex](../build/verification/shader-readonly-20260921-115707/resources/assets/fantasy_ending/shaders/core/cosmic.vsh) / [fragment](../build/verification/shader-readonly-20260921-115707/resources/assets/fantasy_ending/shaders/core/cosmic.fsh)。GLSL150、vertex/fragment実在。minecraft light.glsl/fog.glslのimportも既存client-extra Jar内に存在。22 uniformの完全なname/type/count/values/customFieldsはreviewed-resultsとJSONに保存。

| uniform名 | type / count | JSON values |
|---|---|---|
| ModelViewMat, ProjMat | matrix4x4 /16 | 各[1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1] |
| ColorModulator | vec4 /4 | [1,1,1,1] |
| FogStart, FogEnd | float /1 | 順に[0], [1] |
| FogColor | vec4 /4 | [0,0,0,0] |
| FogShape | int /1 | [0] |
| time, yaw, pitch, externalScale, opacity | float /1 | 全て[0] |
| cosmicuvs, cosmicuvs1, cosmicuvs2, cosmicuvs3, cosmicuvs4, cosmicuvs5, cosmicuvs6, cosmicuvs7, cosmicuvs8, cosmicuvs9 | vec4 /4 | 各[0]（単値をcount分に展開する形式） |

uniform要素の独自追加fieldなし。samplerはSampler0/2とCosmicSampler0–9、attributesはPosition/Color/UV0/UV2/Normal。JSONは破損していないが、**vec4はFE専用UniformType.parseの語彙**。実vanilla `Uniform.m_85629_(String)`はint/float/matrixだけでvec4→−1。親parserはこの−1を拒否せずcount補正してint系列のUniformにしてしまうため、単なる「通常Uniformでも同じ意味で有効なresource」ではない。GLSL側vec4との意味の不一致はfactoryへ入らない問題の一部で、JSONの欠損が今回の直接例外ではない。

constructorはJSON/vertex/fragment/link/location解決を通過した後のModelViewMat getterで落ちている。Cosmicについてそれ以前の欠損/JSON解析/compile失敗は旧logで確認されない。他shaderのInSize/Alpha/Sampler2警告やmodel欠損は別記録として保持する。**今回GLSLを再compileしたわけでも、全shader/resource PASSでもない**。cast回避だけで正常動作とする案は採らない。

#### f. version・EndingLibrary・helper責任範囲

FE/EL metadataはjavafml `[47,)`、MC `[1.20.1,1.21)`。FEはCurios `[5.4.5,)` / Iron's Spells `[1.20.1-3.15.3,)`を要求し現物が満たす。承認6本のmandatory closure成立を維持し、今回**ARTIFACT REQUIREDなし**。ELの具体版がこのparserを公開化するという宣言や、修正済み別版を示すJar内readme/changelogは確認されない。metadata適合とshader実装互換は別。47.2.0へ戻す、EL/Spells等を更新する根拠にはしない。

EndingLibraryはTimeStopだけでなく上記shader/描画支援も提供する。ただしFE MUniform factoryの提供元ではなく、今回の直接例外はELのRETURN handlerより先。API不存在/NoSuchMethod/必須transform失敗ではない。

補助Jar35entryのMixin JSON/refmap・class constant pool・既存sourceを確認。ShaderInstance/Uniform/MUniform/RegisterShadersEvent/RenderSystem/ResourcePack/PackRepository参照0、assets0。**ClientLeaseのRenderTickEvent.END購読はある**（スナップショット、自己lease/cache整合管理、native canMove観測）。従って「render関連0」とは書かないが、shader生成・登録・resource・GL描画pipelineを変更する処理0。helper削除で直るという対照起動は未実施/主張しない。**helper buildは成功したがFE native client startupが先に落ちた**、を維持する。

#### g. A–I分類、修正候補、次の1単位

| 分類 | 今回の判定 |
|---|---|
| A | **直接原因を確認**。FEの専用public parserと親private parserが接続しないのにgetterはMUniformを必須とする |
| B | FE/EL版/API不整合を主因にする証拠なし |
| C | shaderへの複数Mixinは存在するが、factory置換の競合/上書きの証拠なし |
| D | 公開化/置換の必要性はあるが、原物に該当transform宣言を確認できない。「要求されたMixinの適用失敗」とはしない |
| E | 対象class重複/wrong originの証拠なし |
| F | 専用vec4語彙は存在。欠損/JSON破損がこのcastを起こした証拠なし。factory不接続の付随不整合として記録 |
| G | 現MC/Forgeとのvisibility契約不一致は確認。Forge47.4.0固有回帰・別Forgeで改善は未確認。Aと独立の第二主因にしない |
| H | helperのshader介入/原因証拠なし |
| I | MShaderInstanceの実heap/exportなし、修正後起動の成否は未確認。ただし直接の生成/cast経路は既存証拠で特定できた |

修正候補は**分類のみ**：①FE版変更＝修正版artifact/互換証拠なし、②EL版変更・③特定依存版変更＝今回根拠なし、④追加artifact＝不要、⑤Food Healing外の独立互換patch＝契約不整合を直す候補、⑥検証専用client workaround＝登録skip/cast回避だけではnative clientの証明にならず推奨しない、⑦FE本体側の正規修正＝生成/取得契約を合わせる根本対処候補、⑧現構成＝前runでtitle前起動FAIL、未解消。いずれも実装・適用・版変更・JSON/priority/Config変更をしていない。

**次の1作業：Food Healingとは独立したclient互換patchの最小設計・検証条件を、別承認の下で具体化する。対象はFEのMUniform生成経路と親ShaderInstanceのprivate parserの契約不整合。今回は候補分類までで、patch作成/適用・版変更・起動・R1–R9は開始しない。** 完成済みreconnect helper・R1–R9計画を再作成しない。同run/worldは再利用しない。修正の方式/範囲と実行承認が揃うまで新runも作らない。

R1–R9 NOT RUN / auth・TCP UNVERIFIED / prepare・UOM・native use・Grant0。SAFE DESIGN PROVEN=NO / SOURCE DIMENSION TRANSITION VERIFIED=NO / ownership BLOCKED。player17/vehicle16/held stale/一般source転送/UOM native非対応/L2133/core60/Cube49/Invader86/既存限定実clientの判定を変更しない。P/T IMPLEMENTATION_PENDING・購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、全個別開始条件・可逆クラフト増加の既知許容仕様を維持。

<a id="uom-shader-compat-design"></a>
### 14.18. 独立FE client shader互換patch — 設計のみ（2026-09-21 12:37 JST）

**DESIGN COMPLETE / PATCH NOT IMPLEMENTED / STATIC FIXTURE・STARTUP-S1・VISUAL-V1 NOT RUN。** §14.17の原因Aを前提として介入点・constructor安全性・検証条件だけを追加照合した。原因診断のやり直しでも修復成功でもない。§14.16のcrash、R1–R9 NOT RUN、認証/TCP UNVERIFIED、prepare/UOM/native use/Grant0は変えない。今回の読取記録・既存class disassembly・変更前3文書は[専用記録](../build/verification/shader-patch-design-20260921-121944/)に保全した。

#### a. 推薦と対象境界

**B：親ShaderInstanceのResourceLocation constructor内、uniform JSON parser呼出し1か所だけをredirectし、許可したFE receiver/resourceの組合せだけFE本来のpublic parserへ渡す。** 親private parserの可視性・実装を変更せず、native FE factory、parse、list追加、親link/location/map、FE getter/upload/onApplyを利用する。JSON/resource・getterの型・Config・登録順は変更しない。

| runtime receiverの完全一致名 | 許可するshader ResourceLocation | 実登録元 |
|---|---|---|
| `com.mega.uom.client.render.shader.cosmic.CosmicShaderInstance` | `fantasy_ending:cosmic` | `CosmicItemShaders.onRegisterShaders` |
| 同上 | `fantasy_ending:cosmic_2` | `VanillaCosmicShaders.onRegisterShaders` |
| `com.mega.uom.client.render.shader.core.MShaderInstance` | `fantasy_ending:hash`、`fantasy_ending:rendertype_light_beacon_beam`、`fantasy_ending:rendertype_cil_particle` | `ModShaders.onRegisterShaders` |

旧crashのcallはcosmicであり、cosmic_2だったと書き換えない。今回追加で読んだ既存登録5件は同じ2 classのconstructor契約を使う。既知の次の登録で同じ障害を残さないため、この5組だけを設計上の対象とする。任意の派生class、名前が似たshader、同namespace全体、Iron's Spells/GeckoLib/vanilla shaderは対象にしない。`instanceof MShaderInstance`単独で判断しない。

名称候補は独立MOD ID **`fe_uniform_compat`**、独立namespace/package `compat.feuniform`。配布方針・名称の最終LOCKはしない。Food Healing、`uom_reconnect_verification`のsource/Jar/build/namespaceには入れない。

#### b. A–D比較

共通のdescriptor：親/FE parserは `(Lcom/google/gson/JsonElement;)V`。親constructorは下記c、Uniform constructorは `(Ljava/lang/String;IILcom/mojang/blaze3d/shaders/Shader;)V`。FE factoryは `MUniform.makeUniform(String,UniformType,int,Shader):MUniform`、descriptor `(Ljava/lang/String;Lcom/mega/uom/client/render/shader/core/UniformType;ILcom/mojang/blaze3d/shaders/Shader;)Lcom/mega/uom/client/render/shader/core/MUniform;`。

| 案 | exact介入・順序 | list/map/location・値・callback/link | 漏洩/optional/競合/rollback・判定 |
|---|---|---|---|
| A NEW置換 | 親 `ShaderInstance.m_173354_`内 `NEW com/mojang/blaze3d/shaders/Uniform`と上記constructorのredirect | list追加前には間に合うが、既に親のtype解析でvec4の意味を失っている。name/int/count/ShaderだけからUniformTypeを一意復元できず、その後の親setterもFE parserのI/F/D分岐と同一でない。listの型だけ直しても初期値/upload契約を保証しない | 独自型復元・locals捕捉・setter差替まで増やす必要があり最小でない。親parser全利用者への判定/競合が増える。gateと独立Jar撤去は可能でも**不採用** |
| B parse call-site | 親ResourceLocation constructorの `INVOKE ShaderInstance.m_173354_`1か所。parent field初期化後、link・map・getter前 | 未加工JsonElementをnative FE parserへ1回。native factory/cache/list追加→親link/location/mapが同objectを使う。applyCallbacksはconstructor後に通常初期化。sampler/-1/warningは親のまま | exact版・origin・2型×5resource・call数で限定。早期FE class参照を避けるbridgeを使う。単一redirect競合は隠さず停止。独立Jar撤去で戻せる。**推薦、変換実証は次回** |
| C 後再構築 | FE `MShaderInstance.<init>`/`CosmicShaderInstance.<init>`のsuper復帰後（descriptorは親と同じ）へのinject | super内getterで既にcrashするためRETURN/復帰直後では遅い。super前はthis未初期化で親listもない。親getter直前へ移すとlink/location済みの通常Uniformを作り直し、native buffer解放/型/初期値/map/locationを二重管理する | constructor途中でGL/list等へ広く介入しFE型限定だけでは安全にならない。後段のEL RETURN handler等とも競合。撤去可能だが**不採用** |
| D1 private公開化 | 親 `m_173354_(JsonElement)`のaccess flag変更（AT等）でFE public同名をvirtual overrideにする | factory経路が繋がる可能性はあるが、private contractを全ShaderInstanceへ変更。link前という順序だけでは対象外の意味保全を証明できない | ATは通常のconditional Mixinと異なりFE absent/unsupported時の無介入が難しい。原Jar改変は禁止。**不採用** |
| D2 parser HEAD分岐/overwrite | 親 `m_173354_` HEAD cancellable injectまたはoverwrite | JsonElement情報は残るがconstructor以外のcallも対象となる。native/対象外分岐の再入・cancel管理が必要 | Bより広い。対象外の親parser byteを保持できるBを優先。**不採用** |

cast削除、`getUniform`をUniformへ戻す、登録skip、Cosmic描画OFFは候補外。vec4を含む型/carrier/初期値を復元しないためで、ClassCastException0だけでは合格しない。

#### c. exact Mixin設計（まだsourceは作らない）

| 項目 | 設計値 |
|---|---|
| target | `net.minecraft.client.renderer.ShaderInstance` |
| method | `<init>(Lnet/minecraft/server/packs/resources/ResourceProvider;Lnet/minecraft/resources/ResourceLocation;Lcom/mojang/blaze3d/vertex/VertexFormat;)V`。String引数overloadを別にinjectしない |
| injection / at | `@Redirect` / `@At(value="INVOKE", target="Lnet/minecraft/client/renderer/ShaderInstance;m_173354_(Lcom/google/gson/JsonElement;)V", ordinal=0)`（上記は配布SRG表記） |
| require / expect / allow | **1 / 1 / 1**、config `required=true` / `defaultRequire=1`（不在/対象外はpluginで両方skip）。expectの実検査には次回fixtureで`mixin.debug.countInjections=true`も使用。ordinal指定前にexact call総数=1を検査し、2個あってもordinal0だけ合ったとして通さない |
| priority | **1000**。競合時に上げて押し勝たない。対応profileで未適用・二重redirect・構造差異は診断を残して停止 |
| remap | MC target/method/at/親fallback shadowは **true**。source側Mojmap `parseUniformNode`から最終refmapのSRG `m_173354_`へ一致を確認。FE所有public同名method/FE target/独自bridgeは **false**。SRG名を誤って再remapしない |
| handler署名 | instance `void route(ShaderInstance receiver, JsonElement node, ResourceProvider provider, ResourceLocation location, VertexFormat format)`。元invokeのreceiver+引数にconstructor引数を続ける。receiverがthisと同一であることを必須とする |
| original fallback | target外は親の **private** `parseUniformNode(JsonElement)`をprivate `@Shadow`経由で1回だけ呼ぶ。親parser本体は変更しないため再帰redirectしない。例外はnativeに伝播する |
| eligible branch | aの2型/5組とgateが一致した場合だけplain bridgeへcast→native FE public parserを1回。bridge不在は不整合として停止し、通常Uniformへfallbackしない |

早期classloadingを避けるため、親側handlerのclass定数・署名・`instanceof`にFE型を置かない。`receiver.getClass().getName()`の完全一致＋constructorの実ResourceLocationで絞り、独立のplain Java interface `NativeUniformParserBridge`（仮）だけを使う。

もう1つのMixinは `@Pseudo @Mixin(targets="com.mega.uom.client.render.shader.core.MShaderInstance", remap=false, priority=1000)`。public `m_173354_(JsonElement)`を`@Shadow(remap=false)`し、plain interfaceの固有名 `fecp$parseUniform(JsonElement)`からそのnative methodへ渡す。**新fieldなし、constructor injectなし、getter変更なし**。このMixinにはinjectorがないためat/ordinal/require/expect/allowは非該当、shadowのname/descriptor一致が必須。interface/shadow/bridgeの最終bytecodeでcall ownerがFE MShaderInstanceであることを検査する。親fallback ownerはShaderInstanceかつprivateのまま。反射やparent methodのpublic化を代用しない。

Mixin0.8.5実物の`RedirectInjector.injectAtInvoke`、`Injector.checkTargetForNode`、`Target.findDelegateInitNode`を読取確認した。元constructorの対象callはObject初期化後で、未初期化thisを使う位置ではない。同版はdelegate init後のinstance invoke redirectとenclosing引数captureを扱う。**今回transform/compileして成立を実証したわけではない**。frame/アクセス/引数capture/bridge解決の最終成立はSTATIC-Sで必須確認する。

#### d. constructor・factory安全性と順序

1. 親constructorでsampler/list/map等のfieldが初期化済み。既存実exportのoffset416のparser callはJSON走査中で、program link/location/map/getterより前。byte offset固定のinjectはせず、descriptorと順序を検査する。
2. FE public parserはJSONのname/type/count/values、static parse helpers、`UniformType.parse`、`MUniform.makeUniform`、作成したuniform、FE Accessorの親uniform listだけを使う。MShaderInstanceの`applyCallbacks`やCosmic固有fieldを読まない。
3. factoryの基底Uniform初期化はbuffer/location/dirty/parentを設定する。`Uniform.m_85642_`からparent `m_108957_`を呼ぶが、実親実装はdirty boolean設定のみ。MShaderInstanceはこのmethodをoverrideせず、applyCallbacksを実行しない。MUniformは親の一時bufferを自身のnative実装どおり解放し、自身のtype/entryを初期化する。patchから独自buffer操作は加えない。
4. `glUniformI/F/D`という名前でも、parser段階の実装はentryのcache・transpose・dirty更新である。実GL送信は後の`IntUniform/FloatUniform/DoubleUniform.flush`。constructor途中にGL uploadや`apply/onApply`を強制しない。
5. 1回のfactory生成objectが親listへ1回追加され、親がlink後にそのobjectのlocationを解決する。active location>=0だけ親mapへ同じ参照を登録する。location=-1はnative warning/非登録/null getterのまま。全22個がmapへ入ると一律要求せず、active uniformではlist/map/getterの参照同一とMUniformを要求する。
6. 親のModelViewMat getterはmap構築後。ここでFE bridge/getUniformがMUniformを得る。super復帰後にMShaderInstance.applyCallbacks、続いてCosmic固有状態が通常初期化される。登録consumerがその後に`onApply`や必要uniformを設定する。shader compile/link、sampler、getter、EL RETURN handler、native uploadの順序を変更しない。

従って既存bytecodeに基づく**constructor中の未初期化subclass field参照は見つからない**。これは推薦根拠であり、未実施のMixin変換・GL動作・競合まで安全PASSとしない。

#### e. cosmic 22 uniformの意味を保つ検査表

原JSON/値の正本は§14.17e。同じJsonElementをnative parserへ渡し、`values:[0]`のcount展開もnativeに任せる。以下の各nameを個別recordに展開して22件を欠落なく照合する。

| names / 件数 | JSON type/count | UniformType / carrier | 生成subtype・値cache | link後GL型 / native flush |
|---|---|---|---|---|
| ModelViewMat、ProjMat /2 | matrix4x4/16 | MAT4 / MATRIX | MUniform$FloatUniform、float16・identity、transpose=false | GL_FLOAT_MAT4(0x8B5C) / glUniformMatrix4fv |
| ColorModulator、FogColor、cosmicuvs、cosmicuvs1〜9 /12 | vec4/4 | VEC4 / FLOAT | MUniform$FloatUniform、float4。ColorModulatorは1×4、FogColorと各uvは0×4 | GL_FLOAT_VEC4(0x8B52) / glUniform4fv |
| FogStart、FogEnd、time、yaw、pitch、externalScale、opacity /7 | float/1 | FLOAT / FLOAT | MUniform$FloatUniform、float1。FogEnd=1、他=0 | GL_FLOAT(0x1406) / glUniform1fv |
| FogShape /1 | int/1 | INT / INT | MUniform$IntUniform、int1=0 | GL_INT(0x1404) / glUniform1iv |

記録欄：shader resource、name、元JSON type/count/values、生成class、MUniform subtype/UniformType/carrier/size、基底vanilla type整数、cache/transpose、location、GL active type、list/map/getter object identity。GLenumと基底vanilla type整数は別物。生成直後の初期値とnative callback/描画が更新した値も分ける。GL compilerがinactiveにしたuniformは-1/元warningを記録し、locationを書き換えない。ModelViewMatおよびFE登録consumerが要求するuniformの欠落はFAIL。

#### f. client-only / optional / version / conflict

- common側MOD entrypointはno-op。pluginとplain bridgeを含め、FE/client classへの直接参照をcommonのfield/署名/static初期化へ置かない。2つのMixinはJSONの`client`配下だけ。client側のplain bridgeに共通Gson/ChainedJsonException型を使うことと、dedicatedでFE/Minecraft client classをloadすることは区別する。
- early pluginは実在する `FMLLoader.getDist()`、`versionInfo().mcVersion()/forgeVersion()`、`getLoadingModList().getMods()/getModFileById()`、`IModInfo.getVersion()`、`IModFile.getFilePath()/findResource()`でmetadata/bytesだけを読む。`Class.forName`/FE field access/Minecraft singleton/late ModListを起動条件確認に使わない。metadata未確定/nullは介入不可として両方false、途中で片方だけ有効へ変えない。
- gateは **CLIENT / MC1.20.1 / Forge47.4.0 / fantasy_ending2.7.20 / 承認FE原物SHA E32FD4BA2E48FCF2C84F71AA07155C63D6BFA2178BB840899307DA0DA5E5D141**。実選択originの構造（2class/parser/factory/5登録とresource）をpreflightする。未導入・未知版・異なる原物・対象外sideは**両Mixinとも適用false**。unsupportedは元の挙動へ戻すfail-openであり、元のcrashまで解消したとはしない。
- 対応profileではpreApply時のcall総数=1/field初期化順、postApplyの変換結果を必須検査。対応版なのに別transformerが対象callを変更/占有した場合は診断して停止。priority上書き・require0化・例外握り潰しで通さない。他MOD側Mixinとの併用はfixtureと実起動で確認する。
- EndingLibraryは直接原因/factory提供元でないため**EL2.1.19fixを厳密な適用gateへ追加しない**。初回検証は既存承認EL2.1.19fixを不変で使い、実ロード版/transformを記録。他EL版の互換性を保証した意味ではない。
- dedicatedへ誤配置されてもcommon entrypointだけで、client mixin/FE bridgeを適用・loadしない設計。`displayTest`はhandshake表示条件に過ぎず、これだけをclient-only保証にしない。optional FEのmetadataに厳密2.7.20依存制約を置き、未知版をForge自体が拒否する設計にはしない。FHR/ELをこのpatchの新規必須依存にしない。
- client停止後に独立patch Jarと別observerだけを除去すればpatch前へ戻せる。元FE/EL・shader resource・world/saveを復元編集する必要はない。撤去は元のshader障害も戻すため「通常起動へ復旧」とは呼ばない。

#### g. 次回作成予定の境界とSTATIC-S（今回すべて未作成/NOT RUN）

新verification root内の独立projectだけに、仮の `FeUniformCompat.java`（no-op entry）、`FeUniformGatePlugin.java`（gate/構造検査）、`NativeUniformParserBridge.java`、`ShaderConstructorParseMixin.java`、`MShaderParserBridgeMixin.java`、mods.toml/Mixin JSON/refmap/専用build設定を作る案。Food Healingのsource/build.gradle/出力taskやreconnect helperを再利用して変更しない。既存local Gradle8.1.1/cache/runtimeをofflineで使う。必要classは既存承認Jarをcompile-only参照し、同梱しない。

別のverification-only source set/projectに静的fixtureと観測用MOD/画面を置き、patch配布候補Jarにtest/observer/画面を混入させない。既存R1–R9 helperはそのまま。補助をまだ作っていないことと、外部artifact不足は別である。

| STATIC-S検査 | 必須条件・限界 |
|---|---|
| task/packaging | 専用compile/reobf/fixtureだけのtask graph。FHR build/reobfJar/既存suiteを含まない。Jar classは上記own allowlistだけ、FHR/FE/EL/MC class・shader assets・nested依存・ExampleMod・observer0。metadata/Mixin/refmap・原物/製品hash照合 |
| exact transform | 既存MC/FE class bytesとlocal Mixin0.8.5で、ゲーム起動せず隔離transform。ASM検査、constructor frame/uninitialized-this、1 call差替・1 native FE bridge・1 private fallback・name/remap/owner/descriptorを検査。実際のFE/EL shader関連Mixinを併用したexportも比較 |
| 対象外保全 | 元private parser・link/location/map/getter/sampler等のmethod命令列/意味を保持し、差分は許可constructor call-site＋own handler/bridgeだけ。親class全体のbytesはredirectで変わるため「class丸ごと同hash」とはしない。target外分岐は元private call1回、vanilla/他MOD型・未知subclass・型/resource不一致の経路を確認 |
| gate | FE absent/unsupported・別hash・dedicated・MC/Forge対象外で両Mixin不適用、patchによるShaderInstance差分0。early metadata未取得をfalseとして記録。FE/client型loadを拒否する監視classloaderでもcommon entry/pluginのno-linkを検査。stub FEで実起動PASSを代用しない |
| fail条件 | exact call0/2、bridge/descriptor変更、他redirect占有、半適用、postApply差異を検出し停止するnegative検査。競合を黙って片側無効にしない |
| resource/型 | 原JSON22件→eの期待表とnativefactory/parse/flush bytecodeの対応を検査。JSON/GLSL・承認Jarは編集0。staticのみではGL link/location/描画をPASSにしない |

実行するstatic fixtureは**次回実装物**であり、今回のjavapによる既存bytecode読取をそのPASSへ転記しない。GL依存classの実constructorを無理にheadlessで生成したり、偽GL/偽FEでstartup成立を主張しない。

#### h. STARTUP-S1 — reconnect前の独立gate（NOT RUN）

次回明示承認後、STATIC-Sが全成立したpatch候補と独立readonly observerを、新しい検証Prism instanceへ配置。承認6原物・FHR最終Jar・既存local runtimeをそのまま使う。shader登録をskipせずnative resource reload/登録に任せる。**この段階ではdedicated接続/R1もworld作成も行わない。**

合格条件：title到達、ClassCastException/MUniform cast failure0、native cosmic/cosmic_2/上記MShader3登録のconsumer完走、対象shader compile/link成功、ModelViewMat getter成功、22 uniformの生成型/初期値・active list/map identity/GL型がeと一致、他shaderに新規ERRORなし。旧InSize/Alpha/Sampler2等のwarningは旧logと区別し、単にlog文字列0を要求して削除しない。MC/Forge/FE/EL/patch/FHRとnested実ロード版・原物/配置hashを記録する。

observerは生成直後/親map完了/登録consumer後/実upload後を別recordにし、既存field/list/cacheを読取る。uniform型/値/location/登録/例外/GL programを修復・置換・強制bindしない。既存GL debug出力とlink/active-uniformのreadonly queryを対応付ける。エラーqueueを消費する`glGetError`を無記録で追加しない。GL error観測が不足する環境では「GL error0」を推測せずUNVERIFIEDとし、次へ進まない。future観測実装で既存debug出力を遮断・消去しない。

失敗・想定外版/構造・新規ERRORならそのgateで止める。可能ならtitleのQuit Gameで通常終了しprocess不在/hashを記録。起動crashの場合は正常Quitとは書かない。再起動・別版・Config OFF・observerによる補正へ自動切替しない。

#### i. VISUAL-V1 — native cosmic描画の別判定（NOT RUN）

最小案は**worldなしの検証専用Screenで、実ItemStack `fantasy_ending:fantasy_ending_ingot`を通常GuiGraphics/ItemRendererに渡す**。原item modelは `fantasy_ending:cosmic_mask_loader`＋既存mask textureを指定する。実 `ItemRendererMixin` → `CosmicBakedModel.renderItem` → `renderWrappedCosmic` / `CosmicItemShaders.updateShaderData(GUI)` → `COSMIC_RENDER_TYPE`・native buffer flushを通す。独自quad/shader/GL uniform setterで見た目を模造しない。itemをplayerへ付与せず、UOM/装備使用・world/login・TimeStop・P/T stateは作らない。

GUI分岐のnative `updateShaderData`はplayer参照前にGUI用scaleへ分岐し、time/yaw/pitch/scale/opacityとCosmic iconを通常更新する。実bytecode上でplayer不在に対応するため、この経路を推薦する。ただしScreenでの最終成立は未実測。元Configのcosmic rendererがONであることを読取確認し、OFFならONへ書き換えて続行せず停止する。

実行時は画面を開き、単なる固定textureではなくnative cosmic draw/bind/apply/flushが到達したことをobserverで確認。自動で離れた3 frame（およそ0/1/2秒、操作の厳密計時不要）を記録し、timeが進む、値が有限、uv/cacheとGL active型に矛盾なし、GL error/render-thread exception/新規shader ERROR0、item/maskが欠損表示や全面破綻でないことを画面と対応付ける。固定GUI yaw/pitchは変化を要求しない。最初の初期値0と描画後opacity=1等は別記録。未撮影の見た目を目視PASSへ補完しない。

この判定はcosmicの限定GUI描画だけ。cosmic_2の全用途、全FEアイテム/自然boss/全camera/他shader/全GPUを保証しない。native画面経路やGL error観測が成立しなければUNVERIFIEDで通常画面へ戻りQuit、world作成で自動代替しない。Screen/observerは別検証MODだけで、patch候補の配布機能にしない。

#### j. 終了・R1–R9との関係・次の1単位

STARTUP-S1/VISUAL-V1はそれぞれ独立採否。両方の必要証拠が揃った後だけ、新runで既存§14.15のreconnect計画を使える。**その実行承認はshader patchの作成承認と分けて確認し、今回も次回patch実装だけでも自動開始しない。** 既存helper作り直し、旧run/world再利用、旧suite/R1–R9を静的PASSで代用することはしない。shader解消はTimeStop所有関係/再login同期の安全性証明ではない。

このshader単位はworldを作らないためSave & Quit/保存再読込は非該当。検証画面→title→Quit Game、log/observer/対象process終了・原物/製品/配置hashを照合する。R1–R9を将来実行する際のnative停止終了・通常server保存/stop・client Quitは既存§14.15のまま。全失敗記録を保全する。

**追加artifact：不要（ARTIFACT REQUIRED=NO）。次の1作業：本設計を承認後、独立patch＋専用offline static fixture/readonly observerを作成し限定compile/reobf/Jar検査へ進む。STARTUP-S1/VISUAL-V1の起動も承認範囲に含まれる場合だけ、新instanceで順に確認し正常終了まで進める。** 現在はsource/fixture作成0、compile/transform試験0、ゲーム/server/Prism起動0、ネット取得0。配布方針の判断や新ゲーム仕様の決定は不要で、今回は設計完成で停止する。

製品229,494 bytes/150 entries/SHA256 `5C1A716EEDE7AEB4449B36EEFFBDDA156176B16B60A1676229224AA3390DB327`、完成済みreconnect helper58,256 bytes/35 entries/SHA256 `D83C8A675EEF543AAF5D0D12ABFF8EA8AFD089603571E76F3DC6652F58DDA9ED`を維持。製品source/test/build.gradle/Jar/Config/network/schema/購入gate・FE/EL原物を変更しない。SAFE DESIGN PROVEN=NO、SOURCE DIMENSION TRANSITION VERIFIED=NO、ownership BLOCKED、両極意IMPLEMENTATION_PENDING/購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、個別開始条件と既存PASSを維持。可逆クラフト増加は既知許容仕様・バグ修正対象外。

<a id="fe-shader-compat-execution-result"></a>
### 14.19 独立FE shader patch — 実装・STATIC-S・限定実client結果（2026-09-21 13:38 JST）

**STATIC-S PASS / STARTUP-S1 PASS / VISUAL-V1 PASS（cosmic GUI限定）。** 最新利用者本文の実装・限定compile/reobf・static・条件付き新client・終了承認で実施。§14.18は12:37時点の設計記録として保持する。実結果の[判定集計](../build/verification/fe-uniform-compat-20260921-125321/audit/reviewed-results.json)、[static](../build/verification/fe-uniform-compat-20260921-125321/audit/static-s-result.json)、[startup・22件](../build/verification/fe-uniform-compat-20260921-125321/audit/startup-s1-result.json)、[visual](../build/verification/fe-uniform-compat-20260921-125321/audit/visual-v1-result.json)へ参照を一本化。旧§14.16 crash、§14.17原因A、R1–R9未実施は書き換えない。

#### a. 境界・成果物・実ロード

- 新root `build/verification/fe-uniform-compat-20260921-125321`。patch、fixture、observerは独立source/resources、専用Gradle project。製品projectをincludeしない。旧reconnect helperへ混入せず、今回instanceにも配置0。
- patch MOD ID `fe_uniform_compat` / version `0.1.0-verification.20260921.125321`。**14,270 bytes /16 entries /SHA256 `BD2322818FDC65F4F20FD8332200CD6AED02F00487E3E1D8868D2865AD4B58D0`**。[Jar](../build/verification/fe-uniform-compat-20260921-125321/artifacts/fe-uniform-compat.jar)。`compat.feuniform`内6 Java source/7 class：entrypoint、gate plugin、plain bridge、PatchContract/Profile、2 Mixin。非混入検査でFHR/FE/EL/MC class・shader assets・nested dependency・fixture/observer/ExampleMod0。
- 別readonly observer `fe_uniform_observer` / `20260921.125321`：**19,267 bytes /19 entries /SHA256 `160CE2696C892847668ABEE28D0ABF1D00E716E6F14FD08023ACE08CAB700E7C`**。`verification.feuniform.observer`の11 class。別のstatic fixtureはgameへ配置しない。
- 独立taskはcompilePatch/patchDevJar/reobfPatch/compileFixture/compileObserver/observerDevJar/reobfObserverだけ。既存local Gradle8.1.1・Java17・cache・`--offline`・local FART1.0.6。製品compileJava/jar/reobfJar/build/unit/check・既存suite0。compile SDKは既存47.2.0 mapped cache、transform/runtime照合は実47.4.0 class・SRG/refmap。
- 新Prism `FHR_FE_Uniform_20260921-125321`へ指定9 Jarだけ。MC1.20.1/Forge47.4.0/Java17.0.15 Microsoft、FE2.7.20/EL2.1.19fix/Spells1.20.1-3.16.3/Curios5.14.1+1.20.1/GeckoLib4.8.2/Iron's Lib1.20.1-2.1.0/FHR3.0.0と別2MOD。[実ロード記録](../build/verification/fe-uniform-compat-20260921-125321/audit/loaded-mods.json)。Mixin0.8.5、実MixinExtras service0.4.1、playerAnimator1.0.2-rc1+1.20。loaderが同梱5依存を選択、mclib-20/agent-any-1.0/MixinExtras等のlibrary扱い`0.0NONE`はmods版宣言と区別してdebug.logへ保持。同梱を外側へ追加0。
- 製品229,494 bytes/150 entries/SHA256 `5C1A716EEDE7AEB4449B36EEFFBDDA156176B16B60A1676229224AA3390DB327`、承認FE `E32FD4BA2E48FCF2C84F71AA07155C63D6BFA2178BB840899307DA0DA5E5D141`、EL `0E29AF51DABD4E0EA8A315C1886E61F3ABEE62045F196EB6913D7F4DBA6CDC34`を再測定・配置照合。source141件を含む保全146件、6原物・既存reconnect helper・元runtime artifact不変。製品Config/購入gate/schema/network/shader資源/依存版変更0、外部download指示0。

#### b. exact patchとSTATIC-S

親target `net.minecraft.client.renderer.ShaderInstance`の `<init>(Lnet/minecraft/server/packs/resources/ResourceProvider;Lnet/minecraft/resources/ResourceLocation;Lcom/mojang/blaze3d/vertex/VertexFormat;)V` 内 `ShaderInstance.m_173354_(Lcom/google/gson/JsonElement;)V` 1か所だけをredirect。ordinal0、require/expect/allow各1、priority1000。named source＋実SRG refmap/reobfを照合。§14.18のexact receiver/resource5組のみnative FEへ渡し、未知型/別shaderは親private parser1回。parent parser本体/constructorの残処理を保全。

`@Pseudo`のFE `com.mega.uom.client.render.shader.core.MShaderInstance` Mixinはpublic `m_173354_(JsonElement)`をshadowし、plain `NativeUniformParserBridge.fecp$parseUniform`から1回委譲。新field/constructor hook/getter改変なし。親handlerはFE class literal/signatureを持たない。

| STATIC-S項目 | 実結果 |
|---|---|
| 正常transform | plain54 checks、実FE/EL元Mixin併用71 checks。実Mixin0.8.5隔離host＋ASM検証。call1/redirect1/bridge1/private fallback1、owner/name/descriptor、delegate/list初期化→parse→link/map/getter順、frame/stack・未初期化thisを検査 |
| 対象外保全 | external-only32 checksとの比較。元methodの命令/分岐/例外範囲を比較。再計算frame/debug情報とMixin sessionUUIDだけ別扱い、変更plumbingを限定してconstructor比較 |
| gate | absent/別FE版/別hash/dedicated/別MC/別Forge/metadataなしの7profile各34 checks、両Mixin false・patch由来対象byte差分0。実entrypoint/pluginをFE/client禁止classloaderでも照合。実ゲームのabsent/dedicated起動PASSとはしない |
| negative | call0/2、descriptor変更、bridge不在、half-applyを拒否。実競合redirect31 checksで同priority競合を拒否、priority引上げ0 |
| observer併用 | 最終observerによるobserved-03の42 checks PASS。実FE/EL/patch/observerの対象と注入点を併用。GL現在programのreadonly追加後に再確認 |
| 22uniform | 原JSONとnative UniformType enum・factory bytecodeの22件PASS。これは実GLと分離し、次項でruntime22件を確認 |

実clientのgate logは [Prism抽出証拠](../build/verification/fe-uniform-compat-20260921-125321/audit/prism-console-facts.json)：`CLIENT / MC1.20.1 / Forge47.4.0 / FE2.7.20 / 承認SHA / active=true`。EL厳密版gate追加0。実変換2classも[client/exports](../build/verification/fe-uniform-compat-20260921-125321/audit/client/exports/)へ保全。

#### c. STARTUP-S1 — PASS

13:27起動、title到達（[画面](../build/verification/fe-uniform-compat-20260921-125321/audit/screens/startup-title.png)）、Uniform→MUniform ClassCastException0。cosmic/cosmic_2/hash/light_beacon_beam/cil_particleの5 consumer、linkStatus1・ModelViewMat native getter成功。生成直後→親constructor RETURN（map/location後、consumer前）→consumer RETURNを別記録。

cosmic **22/22**：matrix4x4×2=`FloatUniform/MAT4/MATRIX/GL_FLOAT_MAT4`、vec4×12=`FloatUniform/VEC4/FLOAT/GL_FLOAT_VEC4`、float×7=`FloatUniform/FLOAT/FLOAT/GL_FLOAT`、int×1=`IntUniform/INT/INT/GL_INT`。baseVanillaType/cache/count/transposeも別欄。22件すべてactiveでlist/map/native getterが同一object、parseからも同一object。vec4をintへ変換していない。5shaderの全active型も一致。cosmic_2のnative `mat2 cosmicuvs[cosmiccount]` はGL登録名`cosmicuvs[0]`/type35674、読取集計で配列名を対応付ける（native値/locationの修復なし）。

startup段階GL照会20回は全0。`glGetError`消費の事実・全返却値をobserver.jsonlへ記録。新規shader ERROR0。旧FE blade modelのJsonSyntaxException、asset/model/sound/Union URI警告は旧runと照合し保持。旧crash後に初到達したcosmic_2 Sampler2警告もnative inactiveとして保持。patch/observerのpack.mcmeta欠落WARN2件は新規のpackage metadata警告で、画面確認後メインメニューへ戻った。全WARN/ERROR0や全外部MOD正常化とはしない。

#### d. VISUAL-V1 — cosmic GUI限定PASS

Config `enable_cosmic_renderer=true`をreadonly確認（変更0）。world/playerなし。別Screenは実 `fantasy_ending:fantasy_ending_ingot` ItemStackを通常GuiGraphics/ItemRendererへ渡すだけ。native ItemRendererMixin→CosmicBakedModel.renderItem/renderWrappedCosmic→updateShaderData(GUI)→COSMIC_RENDER_TYPE/flushの既存経路。独自quad/shader/uniform setter0。

| sample（JST） | native time | apply時shader/current program | native renderItem/apply / MUniform.flush累計 | 判定 |
|---|---:|---|---|---|
| 13:30:55.116 | 1787.71 | 242 /242 | 1 /1 /22 | PASS |
| 13:30:56.134 | 1798.70 | 242 /242 | 62 /62 /632 | PASS |
| 13:30:57.156 | 1808.95 | 242 /242 | 123 /123 /1242 | PASS |

各sample間1秒以上、通常描画はその間も継続。3件ともnative apply後にbind・有限cache・GL型/同参照を確認。flush後currentProgram0も記録し「未bind」と混同しない。最終GL照会はstartup込み38回、全0。render-thread例外/新規shader ERROR0。COMPUTER USEの[最初の画面](../build/verification/fe-uniform-compat-20260921-125321/audit/screens/visual-frame-first.png) / [3sample後](../build/verification/fe-uniform-compat-20260921-125321/audit/screens/visual-three-samples.png)でingot形状と色付きmaskを確認、missing textureの市松や全面破綻なし。固定GUI yaw/pitchの変化は要求しない。

cosmic_2全用途/全FE item/自然boss/全camera/shader/GPU/TimeStopは未検証。HUMANの目視報告は0で、画面判定はCOMPUTER USE、内部値はAUTOMATED readonly観測。source/canonical/HP/Soul等を作成・修復していない。

#### e. 終了・準備中の失敗・次の作業

Screen→title→13:31:27.639 `Stopping!`→[Prism exit0画面](../build/verification/fe-uniform-compat-20260921-125321/audit/screens/prism-exit0.png)→PID18056不在。worldなし、Save & Quit/保存再読込は非該当。新server/dedicated接続/UOM/TimeStop/prepare/Grant/R1–R9は0。[最終hash/process照合](../build/verification/fe-uniform-compat-20260921-125321/audit/reviewed-results.json)。同client再起動なし。

準備中の失敗は削除しない：最初のsandbox native DLL拒否は承認済み権限付きlocal offlineで解消。FART継承classpath不足、fixtureの旧ASM混入、比較のframe/debug/Mixin sessionUUID、競合negativeの例外分類を補助内だけ修正。observerのconstructor途中`@Inject`がMixin0.8.5に拒否されたため親constructor RETURN（map後・consumer前）へ移動。absence-linkageの独立loaderコンテキストを補正。最終限定build/各staticはPASS。runtime集計reader初回のcosmic_2 array名照合不足は、既存GL snapshot＋原GLSLの配列宣言から対応付け、runtime観測コード/値を変更・ゲーム再試験していない。これらは製品修正ではない。

**次の1作業：別の明示承認後、新runで既存§14.15のR1–R9 real reconnect確認を行う。** shader起動/限定GUI描画gateは今回充足。完成済みreconnect helperを作り直さず、旧instance/worldは再利用しない。追加artifact不要。今回の承認はここで終了し、dedicated接続・認証/TCP reconnect・UOM/TimeStop・prepare/Grantは開始しない。

FE6 MobEffect site STATIC AUDITED / NOT IMPLEMENTED、TimeStop production NOT IMPLEMENTED、FE production integration 0 / NOT RUN、P/T IMPLEMENTATION_PENDING・購入停止/SP保護、SAFE DESIGN PROVEN=NO、SOURCE DIMENSION TRANSITION VERIFIED=NO、BLOCKED - TIME STOP SOURCE OWNERSHIP、RC=NO、REAL2CLIENT=BLOCKEDを維持。 shaderの限定解消はownership/再login安全性の証明ではない。既存Trial/L2/core/Invader86/Cube49/L2133等のPASSは維持し、再実行0。Flight/Break Realm/Bulwark/試作型機関弩/FOURTH BOOT等の個別gate、食料生産の極意による可逆クラフト増加の既知許容仕様・バグ修正対象外を維持する。

<a id="uom-reconnect-preflight-hardcoded-root"></a>
### 14.20 承認R1–R9 — 既存helperの新run制約で起動前STOP（2026-09-21 14:02 JST）

今回利用者は既存helper＋既存shader patchを変更/再compileせず、新runでR1–R9を実行することを承認した。一方、**「既存helperが新run/rootを既存外部設定で受け付けずsource変更が必要なら、勝手に改造せずSTOP」**を明示。開始前のREAD ONLY照合でこの条件に該当した。新たな設計比較・lifecycle再調査・旧試験再実行ではない。

記録ID `20260921-135926` / [preflight-result.json](../build/verification/uom-reconnect-20260921-135926/audit/preflight-result.json)。rootにはauditと変更前3文書だけを作成し、**新instance/server/world/実行sessionは未作成**。既存旧world/run/receipt/journal・補助・shader検証instanceは再利用・修復・起動していない。

#### a. 実物照合と具体的停止根拠

[9 Jar現物照合](../build/verification/uom-reconnect-20260921-135926/audit/artifact-checks.json)：FHR 229,494 bytes/150 entries/`5C1A716EEDE7AEB4449B36EEFFBDDA156176B16B60A1676229224AA3390DB327`、helper 58,256 bytes/35 entries/`D83C8A675EEF543AAF5D0D12ABFF8EA8AFD089603571E76F3DC6652F58DDA9ED`、patch 14,270 bytes/16 entries/`BD2322818FDC65F4F20FD8332200CD6AED02F00487E3E1D8868D2865AD4B58D0`、承認FE/EL/Spells/Curios/GeckoLib/Iron's Libの6原物は全て期待hash一致。新規不足artifactなし。実ロードは今回行っていない。

| 実箇所 | READ ONLYで確認した事実 | 新runへの影響 |
|---|---|---|
| `verification.uom.Audit` source7–8行 / [実bytecode](../build/verification/uom-reconnect-20260921-135926/audit/bytecode/Audit.txt) | `RUN="20260921-112109"`はstatic final String ConstantValue。ROOTのstatic initializerは旧rootの絶対文字列を直接Path.of | cwd/Jarコピー先/JVM起動dirを変更しても新rootにならない。System property/env/config読取なし |
| `ReconnectVerification.started` source30行 / [実bytecode](../build/verification/uom-reconnect-20260921-135926/audit/bytecode/ReconnectVerification.txt) | dedicated server realpath=`Audit.ROOT/server`、world realpath=`Audit.ROOT/server/world`を要求。approved-artifacts・配置Jarも旧rootを参照 | 新server/worldは`Wrong server directory`または`Wrong world`で拒否する。実際には起動して確認せずbytecodeで特定 |
| `Audit.log` / `Audit.once` | `ROOT/audit`へAPPEND／CREATE_NEW | 起動を試すと旧証拠へ追記する危険があり、起動前STOP。旧receipt/journal削除や差し替えをしない |
| [ClientLease](../build/verification/uom-reconnect-20260921-135926/audit/bytecode/ClientLease.txt) / [VerificationNetwork](../build/verification/uom-reconnect-20260921-135926/audit/bytecode/VerificationNetwork.txt) / [SessionRegistry](../build/verification/uom-reconnect-20260921-135926/audit/bytecode/SessionRegistry.txt) | `ldc "20260921-112109"`をそれぞれ確認 | server lease送信・client受理・session監査のrun IDも固定。Audit classだけの置換やROOTだけの迂回では整合しない |

全helper source内に`System.getProperty`/`System.getenv`/ForgeConfigSpec/registerConfigによる切替入口なし。今回はjavap/ZIP/hash/source読取だけで、helper classをロード実行していない。旧pathを指すjunction/symlink、反射、agent、bytecode差替、旧root再利用でguardを迂回しない。

patch metadataは`displayTest="IGNORE_ALL_VERSION"`、no-op common entrypoint、Mixin JSONはclient2件のみ。既存§14.19のdedicated非適用静的結果を維持。metadata上、同一MOD listのためserverへ追加必須とする根拠はないが、実handshakeはNOT RUN。今回patch配置・game起動0、旧shader PASSをやり直していない。

#### b. 今回の個別判定

| case | 今回 | 未到達の内容 |
|---|---|---|
| R1 | **NOT RUN** | 正規auth/TCP・S1→S2・new session参照・通常歩行 |
| R2 | **NOT RUN** | prepare・UOM・E1/G1・S2C/input/server移動 |
| R3 | **NOT RUN** | native logout順序/thread・G1不可逆失効/cache clear |
| R4 | **NOT RUN** | S3/P保存同期・global/dimension5秒収束・UNKNOWN/DENY |
| R5 | **NOT RUN** | old G1/new実Connection predicate |
| R6 | **NOT RUN** | vehicle共通session predicate（搭乗/packetも0） |
| R7 | **NOT RUN** | native終了/clean boundary/revoke/通常歩行 |
| R8 | **NOT RUN** | E2/new epoch/G2/new sequence |
| R9 | **NOT RUN** | G2実movement・収束・native終了/seal |

auth/TCPは**UNVERIFIED（試行0、認証失敗ではない）**。S1/S2/S3・G1/G2・epoch/sequenceの実値なし。EndingLibrary state書込0、prepare/Grant/native use/移動0。新processを起動していないため通常save/stop/Quit・process終了は**非該当**。既存§14.19のexit0・PID18056終了を今回の実行終了へ転記しない。HUMAN/COMPUTER USE操作0、AUTOMATEDはread-only照合と文書更新だけ。

#### c. 再開に必要な最小変更と境界（未実施・別承認）

前回「完成済みhelperをそのまま新runへ使える」と案内した点は誤りだった。helperは旧run専用として作成され、可搬な外部設定を持っていない。これはshader patchの不具合や追加外部artifact不足とは別である。

必要な次候補は**新root側のhelperにrun/root外部入力を設け、最初のaudit書込より前に新規run・実パス・対象束縛を検証する最小修正**。中心は`Audit.java`のRUN/ROOT初期化。入力欠落時に旧rootへfallbackしない。既存server/world/hash・一度prepare・phase・receipt制限は維持する。`ClientLease`/`VerificationNetwork`/`SessionRegistry`等の旧RUN埋込みを解消するため、専用helper一式の限定compile/reobf・新artifact識別/hash確認も必要。**この方針は修正候補の記録で、source/metadata/build/Jarへ未反映**。

session/Grant/epoch/不可逆失効・R1–R9期待値・外部state非改変を作り直す承認は求めない。製品/通常test/build.gradle/protocol6/Config/FE・EL原物/既存shader patch/購入gateは変更対象外。旧helper source/Jar/runも変更せず、新root側だけで作業する候補。最小修正と限定再compileが別承認された後、保全/guard/非混入確認を経て、今回承認された新環境のR1–R9実行へ戻る。

**次の1作業：reconnect helperのRUN/ROOT外部設定化と初回書込前の新run限定検証について、最小修正・専用offline compile/reobfの別承認を受ける。** R1–R9の実行自体は今回承認済みだが、利用者指定の「既存設定で新root不可ならsource変更せずSTOP」に該当した。旧helper/Jar/runは保全し、新root側だけで修正する候補。session/Grant/lifecycle設計、製品、shader patchは変更しない。追加外部artifact不要。

FE6 site STATIC AUDITED / NOT IMPLEMENTED、TimeStop production NOT IMPLEMENTED、FE production integration 0 / NOT RUN、両極意IMPLEMENTATION_PENDING・購入停止/SP保護、SAFE DESIGN PROVEN=NO、SOURCE DIMENSION TRANSITION VERIFIED=NO、BLOCKED - TIME STOP SOURCE OWNERSHIP、RC=NO、REAL2CLIENT=BLOCKEDを維持。 既存PLAYER+VEHICLE DENY LIMITED / GENERAL SOURCE NATIVE TRANSFER LIMITED / UOM NATIVE DIMENSION TRANSFER NOT SUPPORTED・Trial/L2等のPASSを維持。未完了はR1–R9、process/world reload、FE goal途中再延長、multiple-source terminal countdown、transfer fault/observer欠落、client input/render/correction追加境界、production写像、UOM/P車両方針。Flight/Break Realm/Bulwark/試作型機関弩/FOURTH BOOT等は開始せず、可逆クラフト増加の既知許容仕様を維持。


<a id="uom-reconnect-portable-helper-r1-stop"></a>
### 14.21 新helper設定化・real reconnect R1の距離guard停止（2026-09-21 14:40 JST）

最新利用者本文のhelper限定修正・offline compile/reobf・R1–R9一括実行承認で実施。§14.20の起動前STOPは当時の履歴で、RUN/ROOT問題は本節で解消した。3文書は中間修正時には書き換えず、今回の本質的停止・正常終了・証拠照合後に一度だけ更新。

**R1 = FAIL（歩行距離guard）、R2–R9 = 各NOT RUN。** 製品・TimeStop設計の反例ではない。[結果正本](../build/verification/uom-reconnect-20260921-141541/audit/reviewed-results.json)、[歩行解析](../build/verification/uom-reconnect-20260921-141541/audit/movement-R1-analysis.json)、[元measured](../build/verification/uom-reconnect-20260921-141541/audit/move-R1-measured.json)、[FAILURE](../build/verification/uom-reconnect-20260921-141541/audit/FAILURE.json)。失敗を解除してprepareへ進むには期待値変更または失敗caseの再試行が必要となるため、禁止条件を維持して停止した。

#### a. 新run・helperと初回write guard

- run `20260921-141541`、ROOT `<LOCAL_PATH>/food-healing-mod-main/build/verification/uom-reconnect-20260921-141541`、新instance `FHR_UOM_Reconnect_20260921-141541`。旧run/source/Jar/audit/receipt/worldは再利用・変更しない。
- 必須JVM property `foodhealing.uomReconnect.runId` / `foodhealing.uomReconnect.root`、役割`foodhealing.uomReconnect.role=server|client`。`Audit.RUN/ROOT`は`RunContext.load()`のruntime値。Network/Lease/Sessionも同一値を参照し、old fallback/cwd/Jar位置からの推測なし。
- Audit初期化時、最初のlog/onceより先にrun形式・新run manifest・helper metadata version・canonical realpath・server/world/audit/artifacts・instance identity・役割/cwd整合・原物と配置全hash・正確なmods集合を検証。旧rootは対応manifest/versionがなく拒否。新helper metadata版とrun identityも拘束するため、後続runでは新identity/version/manifestを用意する。sourceにruntime RUN固定値を再導入しない。
- [static gate](../build/verification/uom-reconnect-20260921-141541/audit/helper-static-gate.json)：27 own class、FHR/FE/EL class・nested Jar・ExampleMod0、全classの旧RUN/旧ROOT runtime定数0。missing両方/片方、blank、wrong/old root、traversalの7条件を実行してwrite前hard fail。旧audit不変。server/client両役割の[配置正条件](../build/verification/uom-reconnect-20260921-141541/audit/deployment-gate.json)も書込なしでPASS、実起動時も同run検証成立。
- helper **version20260921.141541 / 63,693 bytes / 37 entries / SHA256 `CBA3052E6BD034F7FC3E84D99B04AF5515999430F3608A4017E7C04ACAE6594A`**。旧D83C…helperは保全。変更はAudit、新RunContextと識別resource/metadata、専用init/runner等の新root配線のみ。session/Grant/epoch/不可逆失効/UNKNOWN/境界/Leaseの意味は変更0。
- local Gradle8.1.1/cache/`--offline`のdry-runで製品・既存試験task0を確認。compile/resources/helper Jar/reobfと既存mapping処理だけ成功。compile試行1、repair cycle0、途中compile/reobf FAIL0。mapping task名downloadMcpConfigは既存offline cache解決で、新downloadなし。[実log](../build/verification/uom-reconnect-20260921-141541/audit/gradle-build-01.log)。

#### b. 実ロード・安全準備・shader開始条件

MC1.20.1 / Forge47.4.0、FHR3.0.0、FE2.7.20、EndingLibrary2.1.19fix、Iron's Spells1.20.1-3.16.3、Curios5.14.1+1.20.1、GeckoLib4.8.2、Iron's Lib2.1.0、新helperをserverへ配置・実ロード。内包playeranimator1.0.2-rc1+1.20/mixinextras0.4.1は追加配置しない。[実ロード](../build/verification/uom-reconnect-20260921-141541/audit/runtime-server.jsonl) / [原物と配置hash](../build/verification/uom-reconnect-20260921-141541/audit/preservation-after.json)。clientだけ不変fe_uniform_compat0.1.0-verification.20260921.125321を追加。patchなしserverとのhandshake/実接続成立、旧observer/fixtureは混入0。

新serverは`online-mode=true`、`127.0.0.1:59002`、max1。接続前にSurvival/Normal、doMobSpawning=false/naturalRegeneration=false、石床・周囲壁・UOM隔離室とentity不在を確認。S1 native spawn後に歩行室の天井を閉じ、S2前に天井照明を整備。位置setter/teleport/HP修復なし。S1/S2ともHP20/MAX20/effects空、onGround、fall0。

clientは通常titleへ到達、既存patchの両gate対象Mixinが実適用（gate activeの根拠）、Uniform→MUniform ClassCastException0。pack.mcmeta警告・既知FE bladeモデルERRORは保持。shader STATIC/STARTUP/VISUAL suiteは再実行せず、今回通常起動条件だけを確認。

#### c. R1実測と後続未実施

| case | 判定・証拠 |
|---|---|
| R1 | **FAIL：水平2.796093109855154 blocks > 上限2**。before(0.5,-60,0.5)→after(0.509893502,-60,3.296075607)。key-release後sampleから約0.303秒で停止後のserver/client差0を観測。位置同期部分は成立してもcase全体をPASSへ昇格しない |
| R2 | NOT RUN。prepare/UOM/E1/G1すべて0 |
| R3 | NOT RUN。TimeStop/Grant中のlogout未実施。通常S1/S2 logoutは下記の限定証拠 |
| R4 | NOT RUN。S3未接続、global/dimensionのmid-E1同期懸念は未再現・未否定 |
| R5 | NOT RUN。oldG1/new-session認可比較なし |
| R6 | NOT RUN。vehicle predicate/deliveryなし |
| R7 | NOT RUN。native E1終了/quiet/歩行なし |
| R8 | NOT RUN。E2/G2なし |
| R9 | NOT RUN。G2歩行/sealなし |

正規accountのUser AuthenticatorでS1/S2同UUID、real loopback TCP/別portを確認。S1→S2でhelper内の実参照比較を通過し、player/listener/Connection/channel各new object、新session1→2。clientもnew level/listener/Connection/player、空cache・Grantなし。S1/S2のnative channelInactive→handleDisconnection→onDisconnect→player.disconnect→PlayerList.remove→LoggedOut失効→save/removeを記録。**G1/G2は発行0でepoch/sequence/session非該当**。通常session失効をR3のG1失効PASSへ転記しない。

COMPUTER USEの瞬間Wは移動0。その後HUMANは依頼どおり短く歩行・停止したと報告。実snapshotでは最終水平距離が2.796093となった。Codexの案内が数値上限を明示せず、物理的な停止位置もない準備不足を記録する。利用者の誤操作・製品不具合とは断定しない。途中0.05–2の区間だけを切り出してPASSにせず、位置修復・再arm・receipt削除・期待値変更0。

#### d. 保存・終了と境界

14:33:10通常Disconnect→online0→cleanup確認（sourceなし/native use0）→14:33:26 save-all flush全dimension保存→14:33:44 stop→14:33:45 server exit0。clientはtitleから14:33:35通常Quit/Stopping!。serverPID29136・clientPID28196とも不在。[process](../build/verification/uom-reconnect-20260921-141541/audit/process-final.json)。終了後readonly保存NBTでHP20/effects空/未取得/SP0/0/pending0/最終位置一致、gamerule2つfalseを確認。[保存照合](../build/verification/uom-reconnect-20260921-141541/audit/readonly-save.json)。保存再読込・process再起動0。

AUTOMATEDはhelper build/static・native readonly snapshot/packet/lifecycle/hash/NBT照合、COMPUTER USEは通常Prism/GUI接続・切断/Quit、HUMANは今回の短距離歩行1操作。EndingLibraryのglobal/dimension/count/immunity/capability、player/UOM HP/位置のhelper setter変更0（native試験入力も未到達）。製品source/test/build.gradle・購入gate/protocol6/Config不変、206保全ファイル・9原物・配置17 Jarのhash一致。既存の全限定PASSを維持。

**次の1作業：距離上限を人間の押下時間に依存させない新run用の短い歩行区画（物理的な停止位置を0.05–2 blocks内に配置）を具体化し、別の明示承認後に未完了R1–R9を実行する。** 今回の失敗run/receiptは保全し再利用・リセットしない。helperの設定化/初回write guardは完了済みで再設計不要。新run identity/version/manifestの差替と必要なhelper限定buildだけを行う候補。R1条件、session/Grant/ownership設計、製品・shader patchを変更せず、追加外部artifact不要。

FE6site/TimeStop製品未実装、SAFE DESIGN PROVEN=NO、SOURCE DIMENSION TRANSITION VERIFIED=NO、BLOCKED - TIME STOP SOURCE OWNERSHIP、P/T IMPLEMENTATION_PENDING・購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKEDを維持。Flight/Break Realm/Bulwark/試作型機関弩/FOURTH BOOT等は開始せず、可逆クラフト増加の既知許容仕様・バグ修正対象外も維持。

<a id="uom-reconnect-bounded-walk-native-sync-stop"></a>
### 14.22 物理的な短距離区画・R1〜R3成立とR4 native同期STOP（2026-09-21 15:21 JST）

利用者承認の新run **20260921-145046**。旧run20260921-141541のworld/receipt/helper/証拠は再利用・resetせず保全。[結果正本](../build/verification/uom-reconnect-20260921-145046/audit/reviewed-results.json)。R1–R9の意味・期待値、session/Grant/epoch/sequence/UNKNOWN/ownership/DENY境界は変更しない。3文書は本質的STOP・正常終了後に一度だけ更新した。

#### a. 区画・補助・環境

- 幅1×奥行き2 blocksの内部（X0..1、Z0..2）、床Y=-61・側壁/停止壁・屋根Y=-55。native spawnで本人(0.5,-60,0.5)、S1入場後に屋根を閉じ、全歩行前に封鎖済み。通常server準備commandのみ、位置setter/teleport/HP修復0。Survival/Normal、doMobSpawning=false/naturalRegeneration=false、別UOM室、侵入/落下/窒息等なし。
- 新`Corridor.inspect`はreadonly。serverのnative AABB約0.6×1.8、full collision74 block・内部air10 block・他entity不在・地面/向きから許容中心域X[0.300000012,0.699999988]、Z[0.300000012,1.699999988]を確認。各開始点から四隅までの最大距離を計算し、停止壁まで≥0.05/max<2を要求。**R1最大1.216552493、前壁まで1.199999988 / R2最大1.415395418、後壁まで1.399999976 blocks**。持続入力・横ずれでも上限を超えない。R1はHUMAN W、R2は同じ向きの通常S後退で区画を往復し、座標再配置なし。R7/R9の区画観測は未実行。
- helper **version20260921.145046 / 67,220 bytes / 38 entries / SHA256 `70745F382F5626277761B57E474C630F498BC3D5AC527067FF799B154167AA56`**。[package gate](../build/verification/uom-reconnect-20260921-145046/audit/helper-static-gate.json)。新identity/resourceとCorridor、arm前のgeometry採取だけ。旧helperとのclass差はCorridor/ReconnectVerificationだけで、RunContext/first-write guard/session/Grant/ownership/input bridge/observersはbyte不変。製品/外部class・nested Jar混入0。専用offline compile/reobf1回成功、製品task/既存suite0、compile修正0。後処理Pythonのmixed session型フィルタだけ1修正（初回集計AttributeErrorを保持、ゲーム状態・helper Jar不変）。
- MC1.20.1 / Forge47.4.0、製品Jar229,494 bytes/150 entries/`5C1A716E…90DB327`、FE2.7.20、EndingLibrary2.1.19fix、承認依存4本とclient-only既存fe_uniform_compatを使用。shader既存suite再実行0。server PID25152 / client PID32776、loopback127.0.0.1:62681、online-mode=true。3回のnative User Authenticator・実TCP loginを記録し、authファイル読取/コピー・新downloadなし。

#### b. 個別結果

| case | 判定 | 今回の実測 |
|---|---|---|
| R1 | **REAL CLIENT LIMITED PASS** | 同UUIDのS1→S2でplayer/listener/Connection/channelすべてnew実参照、空cache/Grantなし。通常W移動1.200027956、停止sampleでclient/server差0、HP20/effects空。[歩行解析](../build/verification/uom-reconnect-20260921-145046/audit/movement-R1-analysis.json) |
| R2 | **REAL CLIENT LIMITED PASS（verification-only候補）** | prepare1回、P/M各Lv1/ON・SP0/使用済103をGUI/F2とserver/clientで確認。UOM1体native E1、G1 epoch1/sequence1、FOREIGN0/UNKNOWNfalse。server native canMove=falseを保持、client native false→候補effective true。native global/dimension＋leaseは発行後0.04822秒、0.15040秒の別sampleでも一致。通常S移動1.400037433・両側差0、G1のserver境界ALLOW/native falseを確認。[歩行解析](../build/verification/uom-reconnect-20260921-145046/audit/movement-R2-analysis.json) |
| R3 | **REAL CLIENT LIMITED PASS** | E1継続中の通常Disconnect→closed Connection→server-thread logoutでG1不可逆失効/session失効→通常player保存/除去。client LoggingOutでhadLease=trueから消去、LevelUnload後空cache |
| R4 | **STOP / native同期FAIL、identity・NO_GRANTは限定成立** | 同UUIDのS3/new全参照、P/M保存同期一致、G1復活0、sessionUNKNOWN=true、ledgerUNKNOWN=true（deserialize-no-provenance）。server global/dimension=true、client global=true/dimension=false/currentTimeStopDimension=nullが5秒以内に収束せず、退出まで45.914366秒継続。SkillPacket RETURN1、S3のTSDimensionSynchedPacket RETURN0。歩行入力0。[同期解析](../build/verification/uom-reconnect-20260921-145046/audit/R4-native-sync-analysis.json) |
| R5 | **NOT RUN** | R4停止分岐、new Connectionへのold G1 player predicate probe未実行 |
| R6 | **NOT RUN** | 同vehicle predicate probe未実行、既存vehicle16は維持 |
| R7 | **NOT RUN** | clean boundary・revoke受信・通常歩行のcase未実行。退出後の終了cleanupをR7 PASSへ代用しない |
| R8 | **NOT RUN** | E2/G2未発行 |
| R9 | **NOT RUN** | G2歩行/seal未実行 |

R1/R2の収束値0は記録sampleでの差であり、全frame遅延0を意味しない。R2のS key-down自体は既存observer項目外で、HUMAN報告・client実位置変化・server実packet受理・壁到達後の位置一致を対応付けた。歩行合格幅0.05–2、位置差≤0.05/2秒は変更しない。GUI購入成功・製品TimeStop実装PASSではない。

#### c. 停止・保存・不変

- R4の既知native同期STOP条件に従いabortをjournalへ記録。runtime FAILURE receiptは発生しておらず、停止は[case journal](../build/verification/uom-reconnect-20260921-145046/audit/cases.jsonl)で識別する。packet補送・flag/dimension/count修復・期待値緩和・epoch再開・同case再送なし。
- S3通常Disconnect/保存を確認後online0でnative `use(false)`、count0/global=false/dimension停止falseを記録。own forceload解除、15:15:38 save-all flush、15:15:58全dimension保存/stop・exit0。15:15:49 client Quit/Stopping、両PID終了。sealなしを成功sealとしない。
- [readonly保存照合](../build/verification/uom-reconnect-20260921-145046/audit/readonly-save.json)：P/M各Lv1・両ON、SP0/使用済103、pending0、HP20/effects空、最終XYZ保持。Grantをcanonicalへ保存せず、再prepare0/world再読込0。EndingLibrary stateへの直接書換0（承認native開始/終了は各1回）。
- [不変照合](../build/verification/uom-reconnect-20260921-145046/audit/preservation-after.json)：保全対象291ファイル、原物9、配置Jar17すべてhash一致。製品source/test/build.gradle/Jar/protocol/schema/購入gateとshader compat意味、FE/EL原Jar不変。既存player17/vehicle16/held stale/dimension/shader/L2133/core60/Cube49/Invader86/限定実clientは再試験0。
- 主体：**HUMAN** R1/R2歩行各1操作、**COMPUTER USE** 新Prism接続・GUI/F2・通常Disconnect/Quit、**AUTOMATED** helper限定build/prepare/native trigger/独立readonly観測/保存hash照合。

**次の1作業：R4の保存済みnative同期証拠と承認EndingLibraryのlogin経路をREAD ONLYで突き合わせ、globalだけが届きdimensionが収束しない条件、および安全に対応可能な最小範囲を整理する。** R1〜R3・旧suiteは再実行しない。今回runは停止・保存終了済みとして保全し、receipt reset/再prepare/再入場をしない。補助からdimension packetを補送、外部flagを修復、旧epochを再開してPASSへ合わせない。新artifact不足は確認されていない。実装・再試験・製品変更は別承認。

**SAFE DESIGN PROVEN=NO / SOURCE DIMENSION TRANSITION VERIFIED=NO / BLOCKED - TIME STOP SOURCE OWNERSHIP**。両極意IMPLEMENTATION_PENDING・購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、全個別開始条件を維持。FE6 MobEffect/TimeStop productionへ進まない。食料生産の極意の可逆クラフト増加は既知かつ許容仕様・バグ修正対象外。


<a id="uom-r4-native-login-sync-diagnosis"></a>
### 14.23 R4 native login同期 — READ ONLY原因確定と最小compat候補（2026-09-21 15:44 JST）

**新run・ゲーム起動・再試験・補送・patch実装なし。主因A、補足B。** §14.22のR1–R3限定PASS、R4 native同期STOP、R5–R9各NOT RUNを維持する。旧§14.20の固定ROOT、shader、旧距離FAILは現在の停止理由ではない。

#### a. 根拠・全send site

[解析証拠](../build/verification/uom-r4-readonly-20260921-153506/diagnosis-evidence.json)／[全参照索引](../build/verification/uom-r4-readonly-20260921-153506/reference-index.json)／[実bytecode](../build/verification/uom-r4-readonly-20260921-153506/bytecode/)／[時系列照合](../build/verification/uom-r4-readonly-20260921-153506/timeline-correlation.json)。承認6原物＋製品＋使用helper＋shader patchの外側9Jarと内側7Jar、計3,704 classをpacket/field参照で索引化し、該当classを実Jarからjavap。reflection用文字列も含む参照検索で、未ロードの別版・PC全体へ範囲を広げていない。EndingLibraryは`ending_library`、mods.toml/Manifestとも`2.1.19fix`、2,409,255 bytes、SHA256 `0E29AF51DABD4E0EA8A315C1886E61F3ABEE62045F196EB6913D7F4DBA6CDC34`。

| packet | 全native送信箇所（実bytecode offset） | 条件・recipient |
|---|---|---|
| TimeStopSkillPacket | `CommonEventHandler$TimeStopEvents.onPlayerLeave(PlayerLoggedInEvent)` @36 | global=trueかつserver `andSameDimension(player.level)`。`(true,false,-1)`を`PacketHandler.sendToPlayer`で加入本人へ。名前はLeaveだが引数はLoggedIn。通常優先度のSubscribeEvent |
| 同上 | 同class `onPlayerLeave(PlayerLoggedOutEvent)` @50 | 同条件で退出本人のcountを0にし`(false,false,playerId)`を退出本人へ。UOM count/Setをここでは消さない。閉じた接続への送信成功を仮定しない |
| 同上 | `TimeStopUtils.use(Z,LivingEntity,Z,I,Z)` @200 | serverのnative開始/終了がearly-returnせず進んだ場合。global/sound/sourceIdを`sendToAll` |
| TSDimensionSynchedPacket | 同`use` true分岐 @235 | `(remove=empty-path, add=source.dimension)`を`sendToAll` |
| 同上 | 同`use` false分岐 @267 | `(remove=source.dimension, add=empty-path)`を`sendToAll` |

`PacketHandler`内の残る2packet参照はcodec/handler登録で、追加send siteではない。`sendToPlayer`はnative SimpleChannel＋PacketDistributor.PLAYER、`sendToAll`はALL。decodeがpacketをnewする箇所も送信ではない。FE/依存/製品/補助/patch内に別の2packet送信元なし。

**S3のexact flow**：`PlayerList.placeNewPlayer`がClientboundLoginPacketを先に送信（bytecode @445）、後段`ForgeEventFactory.firePlayerLoggedIn` @1164→上記native LoggedIn handler→Skill `(true,false,-1)`→client `handle`のenqueueWork→`handle0`→`TimeStopUtilsWrapped.enable(-1,false)`でclient global=true。`enable`は音声制御/globalのみでdimensionを設定しない。

#### b. client fieldと後発経路

| 経路 | 実処理・R4への意味 |
|---|---|
| new ClientLevel | native ClientLevelMixin constructor injectionが新`ClientLevelExpandedContext`を作る。constructor @7で`currentTimeStopDimension=null`。旧levelの値を持ち越さない |
| Dimension packet | `handle`がmain/render threadへenqueue。`handle0`はadd側IDのpath非空・current client levelのResourceKeyと同一の場合だけ@71で設定し、その後remove側が一致すれば@113でnull。両側同一IDならremoveが勝つため候補で両方へ同じIDを渡さない |
| andSameDimension | serverはlive `TimeStopSavedData.dimensions.contains(level.dimension.location)`。clientは`ClientLevelExpandedContext.isCurrentTS()`＝field非nullだけ。別にglobalをANDする呼出し側がある。`contextFlag`もこのclient判定から更新 |
| global disable | `TimeStopUtilsWrapped.disable()`はglobal=false＋音声復帰のみ。dimension clearはしない。明示field書込はconstructorと上記packetの計3箇所。level unloadはcontext自体の破棄、new levelで再初期化 |
| login / level load / tracking | nativeのdimension snapshot再送・request/response・遅延retry・定期refreshなし。capability/tracking同期をdimension同期と混同しない。field/packet全参照とcontext差替え参照も照合済み |
| dimension change | `TimeStopEvents.dimensionChangeEvent(EntityTravelToDimensionEvent)`は停止中のplayer移動先が別ならcount0＋`use(false,player)`。移動先の停止Setを加入playerへ同期する経路ではない。他sourceが正count/aliveならuseの終了分岐はearly-returnしpacket0 |
| tick / removal / death | LivingEntityMixinはcountを減らし0到達時`use(false)`。EntityMixinは正count source除去時`use(false)`。player deathも条件付きuse(false)。ServerLevelExpandedContextの`Util.getMillis()%60000==0`判定は正count/alive sourceがいない時の終了処理であり、継続中のdimension再送ではない |
| 明示API / command / FE | TimeStopAPI、TimeStopCommand、FE TimeStopSkillGoal/TimeStopUtilsPacket等はnative useへ委譲。新たな開始/終了が成立すればDimension packetを送れるが、loginの欠落を自動回復する専用経路ではない。S3ではuse呼出し0 |

両packetの`handle0`にはclient level=nullで`System.exit(-1)`のnative分岐がある。今回はSkill HEAD時に新levelが存在し正常RETURN、Dimension HEAD自体が0。新levelの初期化後にhelperがfieldを消したという証拠はなく、実ロード後ClientLevelではFEとEndingLibraryの同名context fieldもdescriptorが別で混線していない。

#### c. 保存済み実測との時系列（JST）

| 時刻 | 保存証拠 |
|---|---|
| 15:10:10.123657 use RETURN | S2/E1開始。その直後Skill HEAD/RETURN .132167/.133167、Dimension HEAD/RETURN .134672をclientで記録。observer/channel/handlerが同じrunで正常に働いた対照 |
| 15:13:54.450631～.481829 | S2 LoggingOutでG1 cache消去、server revoke・通常保存/remove。旧Grantの復活なし |
| 15:14:29.749949 / .750947 | S3/new session login。server global=true、Set=`[minecraft:overworld]`、dimension=true、UOM count6808、本人count0、NO_GRANT/UNKNOWN |
| 15:14:29.806850 | client LoggingIn。MC実bytecodeはClientLevel構築→setLevel→firePlayerLogin順。constructor実時刻は未計測で、このevent以前に生成済みという上限だけを言える |
| 15:14:30.059700 | Skill HEAD→RETURN（別nano値）、level145999264、native global false→true。server送信/Netty受信の個別時刻は未計測で、この時刻をwire receiveやconstructor時刻とは記録しない |
| 15:14:30.066801～15:15:15.981167 | 439 client sample、同level、全てglobal=true/dimension=false/field=null、45.914366秒。S3のDimension HEAD/RETURNとも0。対応467 server sampleはglobal/dimension=true、Set同一。5秒STOP条件維持 |
| 15:15:16.028345～.075408 | S3通常Disconnect、server logout直前もUOM count5881、本人count0。保存/remove完了。native useはS3滞在中0回 |
| 15:15:38.607054 | online0後の承認native終了use RETURN、UOM count5433→0、global/dimension=false。退出後のcleanupでありR7 PASSではない。既存通常save/stop/exit0・Quit/PID終了を維持 |

send側のnative packet専用wire tapは旧runにない。ただし全送信経路が上表に閉じ、S3中use0・login handlerはSkillのみであることと実client適用ログが一致するため、**「送信済みDimensionを受信処理だけが失った」Cを採る根拠はない**。constructorの厳密な瞬間やネットワーク遅延全般まで計測済みとはしない。

#### d. helper / shader除外と原因分類

- 使用helper `20260921.145046` / SHA256 `70745F382F5626277761B57E474C630F498BC3D5AC527067FF799B154167AA56`の全own classとMixin設定、実client `.mixin.out`の両packetを照合。handle0のHEAD/RETURNは非cancellable CallbackInfo(false)で`ClientLease.nativePacket`へ記録するだけ。元のenqueue/handler分岐・field書込を保持する。
- ClientLeaseのfield参照はgetfieldのみ。LoggingIn/Out/Unloadが消すのはown lease/binding。独自channelは`uom_reconnect_verification`でEndingLibrary channelを置換しない。ConnectionObserverはchannelInactive/handleDisconnectionの記録のみ。movement/vehicleの既存cancelとclient canMove bridgeは別methodであり、nativeS2Cのdrop/順序変更に使わない。
- fe_uniform_compat SHA256 `BD2322818FDC65F4F20FD8332200CD6AED02F00487E3E1D8868D2865AD4B58D0`はclient shader constructor parser/FE MShader bridgeの2Mixinのみ。実Jar、gate/plugin、実ロードlogにTimeStop/network介入なし。S2両packet受信も対照として維持。
- **主因A**：native loginがglobalだけ送り、新levelのdimension初期nullを埋めない。**補足B**：別のnative use開始/終了送信は存在するが、S3中はtriggerなし。これは同じ欠落が継続する理由で、独立した二重故障Fではない。C/D/Eを今回の原因とする証拠なし、原因自体はG（不足）ではない。未計測のwire/constructor瞬間は上記の観測限界として分離。

#### e. 最小安全compat候補（設計のみ・未承認/未実装）

Food Healing/既存helper/shaderとは独立した**server側login同期補完patch**を候補とする。既存native packetで現在の停止dimensionを加入本人へ伝えるだけで、global/Set/count/capability/EndingLibrary原Jarへ直接書かず、native useを再発火しない。client field反映はnative handler自身に任せる。P/UOM条件を持ち込まず、Food HealingのGrant/epoch/UNKNOWNを読まない・変えない。

| 設計点 | 最小候補と守る境界 |
|---|---|
| exact入口 | `CommonEventHandler$TimeStopEvents.onPlayerLeave(Lnet/minecraftforge/event/entity/player/PlayerEvent$PlayerLoggedInEvent;)V`内の`PacketHandler.sendToPlayer(Ljava/lang/Object;Lnet/minecraft/server/level/ServerPlayer;)V`直後、非cancellableの狭いAFTER injection（require/expect/allow=1、remap=false候補）。native Skillの呼出しが正常に戻った時だけ。単なるRETURNや遅延tickでcatch経路/別stateを拾わない。Forge LOWEST listenerも可能だが、native send完了の直接根拠と例外境界を確保する本候補を優先 |
| recipient/state | server threadでcurrent ServerPlayer/listener/Connectionの有効性と同dimensionを再確認。native global=trueかつ既存server contextのlive dimensionsが本人dimensionを含む場合だけ、`new TSDimensionSynchedPacket(new ResourceLocation(""), player.dimension.location)`を**そのplayerのみ**へnative `PacketHandler.sendToPlayer`。既存global判定で既に初期化されたnative SavedDataを読む。snapshotのための新規readOrCreate/Set変更/能力修復は禁止 |
| client ready/order | 通常MCのClientboundLogin→ClientLevel構築/setLevel、native Skill→補完Dimensionを同じConnectionで順序付き送信。両native handlerはenqueueWork。任意sleep・独自network queue/channel・client setterを追加しない。null-level native exitがあるため、次回は順序/実handle0時のlevelを静的＋限定統合で必ず確認。今回の観測だけで全遅延条件PASSにはしない |
| idempotency/repeated login | 同一dimensionのaddは同じResourceKey代入で冪等。count/globalを再開始しない。UUID永続済みフラグを設けず毎回のnew playerへ一度。旧Connectionを保存して遅延再送しない |
| foreign / non-UOM | source種別/P取得に依存せずnative Setを尊重。foreign停止も同じnative同期に限り扱う。免疫/Grantを与える機能ではない |
| multiple dimensions | client fieldはSetではなく現在level用1値。server全Setをclientへ押し込まず本人dimensionだけ判定。別dimensionのSet要素やglobalの意味を改めない。global=trueでも本人dimensionが非停止ならnative login条件不成立につき補完0 |
| 終了直前login | 同じserver thread・native send直後にその時点のstateを確認しqueue保存しない。停止終了後ならadd0、開始同期後のnative end/removeは後続packetとして届く順序を維持。global/Set矛盾は補完で修復せず停止/記録。server外thread・state遷移競合は別の実測条件 |
| optional/exact gate | `ending_library`不在はtarget/外部classを解決せずno-op。mods.toml/Manifest `2.1.19fix`＋承認SHA256/対象method形一致を要求。別版は適用しない。既存原物改変/依存必須化/新版downloadなし |
| dedicated/client | common入口/gateはMinecraft client class参照を持たない。専用Mixin/adapterをpresence判定後のみ解決。server送信はnativeが既に使用するpacket APIだけ。clientには既存native packet登録/handlerを利用し、新client state/protocolを作らない。dedicated linkageとintegrated-server側の誤適用は次回限定検証 |
| 主なリスク | exact版への注入、native level=null exit、終了/切断/level切替の順序、他patchとの重複、native global/複数dimension自体の既存不整合。補完を理由に既存STOPを緩めず、TimeStop全体やSOURCE DIMENSION TRANSITIONの修正済みとしない |

**追加外部artifact不要**。新たに作成する独立patch/専用検証物は実装承認後の成果物であり、今回は未作成。既存artifactだけで入口/packet/guardを設計できる。検証要件はTEST_PLAN §27末尾へ集約。

#### f. 今回の終了境界

変更前3文書を[backup](../build/verification/uom-r4-readonly-20260921-153506/backup/)へ保全し、解析完了後のみ3文書を一度更新。保全対象380ファイルのhash一致（製品source/test/build.gradle/Jar、原物、旧helper/証拠を含む）。現物製品は229,494 bytes/150 entries/SHA256 `5C1A716EEDE7AEB4449B36EEFFBDDA156176B16B60A1676229224AA3390DB327`。旧PID25152/32776は今回も不在、ゲーム起動・新save/stop試行なし。旧通常終了を今回実行と混同しない。

**次の1作業：§14.23の「native loginのglobal送信直後、既存dimension stateを本人へ同期する独立compat候補」の実装・限定検証について利用者承認を受ける。** 現在は設計のみで、patch/helper作成・compile・packet補送・ゲーム起動・R1–R9再試験は開始しない。既存artifactで着手可能、新規外部artifact不要。native state、session/Grant/UNKNOWN、P/T仕様・製品・shader patchを変更せず、R1–R3/既存suiteを未実施へ戻さない。R4以降の実client実行は別途明示承認された範囲に限る。

**SAFE DESIGN PROVEN=NO / SOURCE DIMENSION TRANSITION VERIFIED=NO / BLOCKED - TIME STOP SOURCE OWNERSHIP**、P/T IMPLEMENTATION_PENDING・購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKEDと全個別開始条件を維持。FE6 MobEffect/TimeStop production、Flight/Break Realm/Bulwark/試作型機関弩/FOURTH BOOT等へ進まない。食料生産の極意の可逆クラフト増加は既知許容仕様・バグ修正対象外。


<a id="endinglib-login-compat-real-reconnect-result"></a>
### 14.24 独立EndingLibrary login dimension同期compat — STATIC / dedicated / real R4–R9限定完了（2026-09-21 17:06 JST）

**LOGIN DIMENSION SYNC COMPAT VERIFIED / REAL RECONNECT R4–R9 VERIFIED（LIMITED）**。[real結果正本](../build/verification/uom-reconnect-20260921-162556/audit/reviewed-results.json)。今回のチャット承認で§14.23の設計を独立実装した。旧§14.22/14.23のSTOP・原因・当時の次作業は履歴のまま保全し、旧runへ補送/再入場しない。新runのR1–R3はR4到達用SETUP ONLYで、既存PASSの再証明・追加件数とはしない。

#### a. 独立成果物・exact介入・不変条件

| 成果物 | 実測識別 |
|---|---|
| [login同期compat](../build/verification/endinglib-login-compat-20260921-155452/artifacts/endinglib-login-sync-compat.jar) | MOD ID `endinglib_login_sync_compat`、version `0.1.0-verification.20260921.155452`、**11,100 bytes /13 entries**、SHA256 `C8B948B30770A67D46CE3A8B09D4D7BE08E0E27A9548DC50518787C74FE58441` |
| [real検証helper](../build/verification/uom-reconnect-20260921-162556/artifacts/uom-reconnect-verification.jar) | `uom_reconnect_verification` / `20260921.162556`、**75,518 bytes /43 entries**、SHA256 `B0818EDAE11EAA27B0458641B22CD3119761B5794EF40CA966CF8E05E42BCF75` |
| Food Healing製品 | **229,494 bytes /150 entries**、SHA256 `5C1A716EEDE7AEB4449B36EEFFBDDA156176B16B60A1676229224AA3390DB327`、再生成なし |

対象はMC1.20.1 / Forge47.4.0、`ending_library` **2.1.19fix / SHA256 `0E29AF51DABD4E0EA8A315C1886E61F3ABEE62045F196EB6913D7F4DBA6CDC34`**。metadata/version/hash・raw method形が全一致した場合だけMixinを適用。不在/別版/別hash/形不一致は無介入。新依存取得なし、EL/FE原Jar・shader patch不変。

exact targetは`com.mega.endinglib.common.eventhandler.CommonEventHandler$TimeStopEvents.onPlayerLeave(Lnet/minecraftforge/event/entity/player/PlayerEvent$PlayerLoggedInEvent;)V`。native `PacketHandler.sendToPlayer(Ljava/lang/Object;Lnet/minecraft/server/level/ServerPlayer;)V`、**ordinal0 / Shift.AFTER / require=expect=allow=1 / priority1000 / remap=false**。native Skill送信が正常に戻った直後だけ、server thread/current player/listener/Connection/native global/live dimension Setを読取り、既存`TSDimensionSynchedPacket(ResourceLocation,ResourceLocation)`のremove=`new ResourceLocation("")`（実packet値`minecraft:`）、add=本人dimensionを本人へ1回送る。client反映は元native handler。P/UOM/Grantを条件にせず、独自channel/queue/timer、永続送信済みflag、old Connection保持なし。

compat自身のnative global/Set/count/capability/HP/位置/Grant書込・native use呼出しは0。[STATIC](../build/verification/endinglib-login-compat-20260921-155452/audit/static-summary.json)のbytecode検査、dedicated before/after、[real R4 before/after完全一致](../build/verification/uom-reconnect-20260921-162556/audit/R4-readonly-verdict.json)で確認。**検証入力としてのnative開始/終了と自然countdownは進行しており、run全体の外部stateが時間を跨いで不変という意味ではない。** helperもnative fieldの修復・position setter・追加同期packetを行わない。独立compatを製品Jarに混入しない。

#### b. STATICと専用dedicated A–J

STATICはenabled/absent/version/hashの4mode PASS。native Skill call0/2・descriptor不一致を拒否、実Mixin変換で注入1・正常send後、非対象時変換0、ASM検証、外部/client早期class定義0、Jar own6classesのみ・製品/EL/FE/shader/helper/ExampleMod/同梱Jar0を確認。専用offline compile/reobfのみ、製品build/既存suiteは実行しない。

別新worldの[専用fixture結果](../build/verification/endinglib-login-compat-20260921-155452/server-01/login-result.json)は **10/10 PASS**。実`PlayerList.placeNewPlayer`/native LoggedInを通すが、**EmbeddedChannel、Food Healing不在、実client/実auth/TCPではない**。

| case | 限定結果 |
|---|---|
| A / B | global=false / global=trueかつ本人dimension Set外 → 補完0 |
| C / D | native Skill→Dimension、加入本人だけ1。他player配送0 |
| E | native Cow sourceでも成立。P/UOM/Food Healing前提なし |
| F | 複数dimension Setの本人だけ送信、別要素不変。fixtureがnative addTsDimensionで構成した入力で、自然な複数source lifecycle PASSではない |
| G | 同UUID new player/listener/Connection/channelへ最大1、旧接続再利用0 |
| H | native end/remove順序を維持、終了後loginのadd0 |
| I / J | global/Set/count/capability/HP/位置等のbefore/after不変、compat由来use0 |

[専用server終了](../build/verification/endinglib-login-compat-20260921-155452/server-01/process.json)：save-all flush/stop、全dimension保存、PID27624 exit0。最終fixture Jarは16,739 bytes/13 entries、SHA256 `B5ECBB13B1150C010D5D90ADD3D3F2E5B4A1C0EB10659A1314A99D1E750B273C`でreal instanceへ未配置。全遅延・切断競合・integrated server・自然複数source terminal条件の包括PASSにはしない。

#### c. 新real run・identity・R1–R9

新run **20260921-162556**、[環境](../build/verification/uom-reconnect-20260921-162556/audit/environment.json)、正規Minecraft account / `online-mode=true` / loopback TCP `127.0.0.1:62080`、same UUID。旧world/receipt不使用。専用Prism instanceに製品＋承認外部6原物＋新helper＋既存client shader＋独立login compat、専用自動fixture/L2/TaCZ/Trialなし。

| identity | S1 | S2 | S3 |
|---|---|---|---|
| ServerPlayer | 707159630 | 1145245656 | 1345694842 |
| listener | 308355423 | 1338946861 | 113825727 |
| Connection | 206080761 | 1379027233 | 944984647 |
| channel | 1111554433 | 1179458803 | 2076419252 |

値はidentityHashCodeの観測値。helperは数値差だけでなく実object参照の非再利用をguardした。本人UUID/P canonicalは通常保存/同期で保持。

| case | 今回の判定と実測 |
|---|---|
| R1 | **SETUP ONLY**。S1→S2通常再接続、HUMAN W **1.072747991 blocks**、Grantなし、HP20。旧R1 PASS維持 |
| R2 | **SETUP ONLY**。prepare1、P/M各Lv1/ON・SP0/使用済103をGUI/F2と両側で照合。E1/G1 **S2/epoch1/sequence1**、HUMAN S **1.272748847**。native server canMove=false、clientのverification-only lease/input bridge有効。旧R2 PASS維持 |
| R3 | **SETUP ONLY**。通常Disconnect→channel close/LoggedOut→G1不可逆失効→保存/remove、client cache空。旧R3 PASS維持 |
| R4 | **REAL CLIENT LIMITED PASS（独立login compat込み）**。new S3、NO_GRANT/UNKNOWN/DENY・G1復活0。native Skill→Dimension各1、level非null、global/dimension=trueへ約**0.280956秒**で収束。native use0/位置不変・移動入力なし |
| R5 | **LIMITED PASS**。old G1とnew player session predicateの1回照合でDENY、packet再注入0 |
| R6 | **LIMITED PASS**。共通vehicle session predicateでもDENY、搭乗/vehicle packet0。車両ALLOWの実測ではない |
| R7 | **REAL CLIENT LIMITED PASS**。native E1終了、global=false/Set空/count0/stack0・5tick以上clean、cache空、HUMAN W **1.400193485**の通常歩行復帰 |
| R8 | **LIMITED PASS**。clean後のnative E2を最初から観測し、G2 **S3/epoch2/sequence2**。client native停止＋leaseは約0.042591/0.142927秒の2sampleで一致 |
| R9 | **REAL CLIENT LIMITED PASS**。G2の実movementをserver受理、HUMAN S **1.400477775**、旧G1失効維持、HP20/effects空 |

[歩行解析](../build/verification/uom-reconnect-20260921-162556/audit/movement-final-analysis.json)：4caseともsampled位置差0、終点sample後のserver一致はR1 0.041521 / R2 0.009510 / R7 0.010025 / R9 0.003502秒（全frameや厳密key-release時刻ではない）。Sキー状態そのものは既存snapshot対象外で、HUMAN報告・実位置・server packet受理で対応付ける。撮影していない入力iconを補完しない。

物理区画の最大水平移動距離は**R1 1.216552492 / R2 1.288562950 / R7 1.414321366 / R9 1.417573166 blocks**。native AABB、full collision74/air10、中心域X[0.300000012,0.699999988]・Z[0.300000012,1.699999988]、屋根/床/壁と周辺無関係damageなしを各arm時readonly確認。距離は押下時間で制御せず、位置setter/teleport0。自然湧き/自然回復OFF、Normal/Survivalを維持。

#### d. R4 packet順序・native state

[R4全条件照合](../build/verification/uom-reconnect-20260921-162556/audit/R4-readonly-verdict.json) / [server trace](../build/verification/uom-reconnect-20260921-162556/audit/login-sync-server.jsonl) / [client trace](../build/verification/uom-reconnect-20260921-162556/audit/login-sync-client.jsonl)。

- server S3 login **16:46:17.533897** → native Skill send RETURN **.538900** → compat Dimension send RETURN **.547410**。同じ本人・listener/Connectionへ各1。compat HEAD/RETURNでglobal/Set/count/capability/canonical/HP/位置/Grant等の全観測state一致。
- client `ClientboundLogin RETURN` **.555926**（new level1285855580）→ Skill HEAD/RETURN **.813851** → Dimension HEAD/RETURN **.814853**。両native handlerはRender thread/level非null、Dimension add=`minecraft:overworld`、RETURN1。global=true/dimension=true、currentTimeStopDimensionはcurrent level。2sample収束はloginから0.286460/0.391710秒。
- S3 current Grant/cacheなし、session/ledger UNKNOWN、native/effective canMove=false、実player boundaryもDENY。旧G1再発行0、R4滞在中native use0。native packet観測点の5移動keyは全false。通常login直後の一時座標8.5/65/8.5→native配置packetは歩行/補助setterと区別し、配置後の継続driftなし。
- 受信後のnative状態・source count継続と、compatによる書込0を分離。native Skill/Dimension handler自身によるclient反映は本来の同期処理であり、helper field修復ではない。

#### e. 修正履歴・終了・不変と残件

[repair記録](../build/verification/endinglib-login-compat-20260921-155452/audit/repair-cycles.json)：**3 cycle**。①STATIC01のForge metadata不足等をclasspath/FART hierarchy・記録処理だけ修正、②STATIC02で不足したForge event metadataを既存47.4.0 universalから参照、③初回新run `161503` の「屋根を先に作ったためS1が屋根上へnative spawn」という準備不備を保全・通常終了し、新run162556で屋根をS1正常spawn後に閉鎖。期待値/製品/ownership設計は変更なし。161503はHP20・prepare0/R1測定0/E1/G1なし、修復再利用なし。初回sandboxのnative/cache・認証通信制限は承認範囲のローカル実行へ移し、失敗logも保持。正規authを偽装・offlineへ切替えていない。

real helper変更は新artifact allowlistと独立readonly login/packet observer、run metadata。145046からsession/ownership/movement/vehicle/lease/network/corridor/進行処理のclass byteは保持（[検査](../build/verification/uom-reconnect-20260921-162556/audit/helper-static-gate.json)）。原物/製品/通常test/build.gradle/schema/protocol/購入gate不変。

**16:54:47 native E2終了・transient revoke・SEALED（prepare1/start2）→16:55:05通常Disconnect/保存/remove→16:55:17 save-all flush→16:55:34 stop/全dimension保存→16:55:35 server exit0→16:55:58 Minecraft通常Quit**。[process確認](../build/verification/uom-reconnect-20260921-162556/audit/process-final.json)、PID26880/33420とも不在。readonly保存はHP20/effects空・P/M各1/両ON・SP0/使用済103・通常位置を保持（[保存照合](../build/verification/uom-reconnect-20260921-162556/audit/readonly-save.json)）。world再読込/再起動は未実施。sourceのlive count0/全Set空は保存前observerで確認し、native Setの保存形式から推測しない。

[保全照合](../build/verification/uom-reconnect-20260921-162556/audit/preservation-before-docs.json)：**380ファイル一致・配置19Jar一致**。3文書は[変更前backup](../build/verification/endinglib-login-compat-20260921-155452/before/)を照合後、終了時に一度だけ更新。既存player17/vehicle16/held stale/source dimension/shader/L2133/core60/Cube49/Invader86/旧限定実clientの再実行0。全WARN解消の主張はしない（外部Patreon等の遮断された接続WARNは原logに保持）。

主体：**HUMAN** 4回の通常歩行、**COMPUTER USE** Prism/通常接続・Disconnect・GUI/F2・Quit、**AUTOMATED** 別補助compile/reobf・prepare/native trigger・readonly観測と証拠/保存/hash照合。

**次の1作業：今回完了したR4–R9を除いたTimeStop lifecycle残件を、既存証拠に基づくREAD ONLY整理で優先付けし、次の最小検証1単位と必要承認を具体化する。** process/world reload、転送fault/observer欠落、FE goal途中再延長、複数source terminal countdown、client input/render/correction全般、製品設計への写像を区別する。今回は推薦までで終了し、新run/helper/build/ゲーム・製品実装を自動開始しない。R1–R9・旧suiteの再証明は不要。追加外部artifact不足は今回確認されていない。

**SAFE DESIGN PROVEN=NO / SOURCE DIMENSION TRANSITION VERIFIED=NO / BLOCKED - TIME STOP SOURCE OWNERSHIP**を維持。process/world reload、転送fault/observer欠落、FE goal再延長、複数source terminal countdown、client input/render/correction全般、製品への写像は残件、UOM/P vehicle方針は未LOCK。P/T IMPLEMENTATION_PENDING・購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、Flight/Break Realm/Bulwark/試作型機関弩/FOURTH BOOTの個別gateを維持。FE6/TimeStop productionへ未着手。食料生産の極意の可逆クラフト増加は既知許容仕様・バグ修正対象外。


<a id="uom-lifecycle-priority-terminal-plan"></a>
### 14.25 TimeStop lifecycle残件の優先関係と次の最小単位（2026-09-21 21:31 JST、READ ONLY / DESIGN ONLY）

**次候補はDの一部「同一dimension・既知UOM 2体の非同時terminal countdown→最後のsource終了」だけ。未実行・次回承認待ち。** §14.11–14.24は当時の証拠・計画として変更しない。§14.24の独立login同期compat STATIC/dedicated10/10、real R4–R9、seal/保存/終了、旧R1–R3 PASSは完了のまま。今回は新run/helper/compile/build/test/ゲーム起動0、既存source/artifactの読取と3文書更新のみ。

#### a. A–Gの既知・未確認・優先関係

| 残件 | 既存証拠で確認済み | 未確認とSAFE DESIGNへの具体的リスク | 優先・他残件との依存 |
|---|---|---|---|
| A process / world reload | §14.11の台帳喪失相当・positive deserializeはDENY。§14.24は通常保存と同processのnew sessionまで | 実process交換/通常world reloadでnative count・global・live Set・一時台帳/sessionがどう初期化されるか。保存されたcountや同UUID/同番号から旧権限を誤復元する危険。SavedDataの空NBTはlive Set空の証拠ではない | **Dの後の候補**。再起動直後のDENY自体はDと独立に調べられるが、回復側の「完全終了→次epoch」はDの終了判定に依存。process交換と同JVM world reloadを同一視しない |
| B transfer fault / observer loss | §14.14一般Cow native転送/コピーとFOREIGN後DENY、UOM native転送非対応。§14.11 direct/deserialize/喪失モデルは拒否 | native例外・HEADだけ/RETURN欠落・観測が丸ごと欠落する条件。古いorigin/processing/Grantを有効と見なす危険。ログ欠損と認可observer欠損は別 | **Dの後の候補**。異常直後DENYの検査は独立可能だが、異常後の再許可を評価する基準はD/Aに依存。架空UOM転送成功を作らない。有限fault注入を全observer完全性証明にしない |
| C FE TimeStopSkillGoal途中再延長 | §14.11旧候補でnative goal初回開始/単独終了、common useのmax延長。実goalはcount180設定→use(true) | 停止中のgoal再入場とGrant失効の実時系列。現行count observerはuse外の増加をUNKNOWNへするため、正常なUOM再延長で本人保護を失う可能性。逆に「UOM型なら増加を許す」緩和はFOREIGN/未観測経路の誤許可を招く | **Dと独立する通常入口の必須残件**。今回Dはgoalを動かさずnative common useだけで準備する。Cの誤拒否根拠は静的に既知で、同じcommon-use延長の再試験は不要。新たな帰属範囲を設計・測定する際もUNKNOWNを勝手に緩めない |
| D 複数source terminal countdown | §14.11 UOM複数、明示use(false)の片側終了/early return、単独UOMのnative 180→0。§14.24明示終了後clean5tick・次epoch | **native 1→0がuse(false)より先**のとき、終了sourceを「説明不能な消失」と誤認する可能性。逆に、残sourceがあるのにquiet/新epochへ進むとownershipの境界を失う。複数sourceの最後の終了を実測していない | **今回選ぶ最優先の最小1単位**。active集合→残source→空集合というepoch終端の基準で、A/B/C後の回復・Eのrevoke・Fの写像に共通。全Dを一括網羅しない |
| E client input / render / correction | §14.22/24通常歩行、lease/native同期、失効、位置収束。shaderの独立限定PASS | attack/interact、各入力、camera/hand/partial tick、DENY後の予測ずれ/補正、長時間floating等。serverは拒否してもclientが動ける表示/操作不整合が残り得る | server provenance/lifecycleを先行。D/C等の正しい許可・失効が前提。既存歩行やshaderを再証明せず、未被覆入力だけ後で選ぶ |
| F verification-only→production写像 | controllerのP条件、候補台帳/実session/境界/leaseは限定検証済み。製品TimeStop未実装 | fixture専用のprepare/手動issue/scan/観測を製品へそのまま移せない。自動issue/revoke、optional/version/classloading、例外、性能、全入口の適用に未証明がある | **後回し**。A–Dの安全根拠、必要E、Gの製品方針が揃う前に実装へ移さない。既存schemeの安全性を製品へ転記しない |
| G UOM/P vehicle方針 | §14.13 FOREIGN/UNKNOWN等のvehicle16拒否、§14.24 R6のnew session拒否 | UOM単独/P有効時に搭乗物移動を許すか/拒否するかは未LOCK。本人保護をvehicle免疫へ拡張すると未承認仕様になる | **利用者判断待ち・Dと独立**。必要な段階でALLOW/DENY方針を確認する。今回packet0/採否0、既存の未採用分岐を正式ALLOWと見なさない |

| 残件 | 既存artifactで可能か／専用dedicatedで足りる範囲 | real clientの必要性 | 新仕様判断／追加artifact |
|---|---|---|---|
| A | 可。新規worldを使う2 process等でserver初期化・保存/読込・失効を検証可能（今回の次単位には含めない） | serverの旧許可非復元だけなら不要。client cache/levelを含む保証は別途必要 | 既定fail-closedの確認なら仕様判断不要。native保存矛盾を修復する方針は別承認/判断。新外部artifact不足なし |
| B | 可。限定した転送/例外/観測欠落モデル＋native server経路。モデルの入口・欠落方法は事前に固定する必要あり | server DENY/stack/失効には不要。転送後のclient state/packet loss全般は別途 | UNKNOWN維持の確認に新仕様不要。見失ったownerの復元・再許可緩和は不可。追加外部artifact不足なし |
| C | 可。実FE goalの明示呼出し＋native server tick/既存候補比較 | count・帰属・server許可の判定はdedicatedで足りる。再延長時の画面/lease連携は別 | native経路の観測は新仕様不要。帰属observer/UNKNOWN設計を変えるなら別設計承認。新外部artifact不足なし |
| D | **可。新規dedicated/world 1 run、2 sourceの1停止epochで足りる** | **今回の最小単位には不要** | **新しい利用者仕様判断なし**。observer/driver未作成は外部artifact不足とは別 |
| E | server handler/packet部分だけdedicated可能 | 入力/描画/予測補正の結論には必要 | 本人保護の新しい作用範囲を追加するなら判断待ち。現範囲の追加外部artifact不足なし |
| F | ソース対応付けは静的に可能。実装後は専用統合＋製品回帰が必要 | 製品としての操作保証は必要なEと分担 | mapping実装は別承認、G等未LOCKを解決せず採用しない。現時点で新artifact必須の根拠なし |
| G | 方針整理に新artifact不要。拒否境界は既存証拠で十分 | ALLOW採用なら搭乗実挙動は後で実client確認。今回不要 | **利用者判断必要**。Dを止める前提にはしない |

優先は「Dの上記小範囲 → 結果を踏まえてA/B/Cの次の1単位を再選定 → 必要E → F」。これはA/Bの安全側拒否をDなしで検査できないという意味でも、Cの既知課題を解消済みとする意味でもない。**終了条件が曖昧なまま、reload/fault後にいつUNKNOWNを解除できるかの複合試験へ広げない**ことが選定理由。Gは並行する仕様判断であり、Codexは決定しない。

#### b. 今回追加で照合した静的根拠（新しいruntime PASS/FAILではない）

- [実LivingEntityMixin.tick](../build/verification/fantasy-ending-20260920-204205/audit/bytecode/com.mega.endinglib.mixin.time.LivingEntityMixin.txt)はcountを1減らしてから、0なら2引数use(false)へ入る。[実TimeStopUtils](../build/verification/fantasy-ending-20260920-204205/audit/bytecode/com.mega.endinglib.util.time.TimeStopUtils.txt)のcommon5引数へreset=trueで委譲し、他alive positive sourceがあればearly returnする。**明示use(false)へ入ってからcountを0にする既存試験と、入口で既に0になっているterminal tickは異なる**。
- [現行Ownership](../build/verification/uom-reconnect-20260921-162556/helper/src/main/java/verification/uom/Ownership.java)はbeforeUse冒頭にreconcile、loadedはcount>0だけ、origin削除はafterUse。したがって先行sourceのterminal入口で `source-disappeared-without-observed-end` を残し得る。最後のnative非停止/positive0でquietへ戻る処理はある。これは**安全側の誤拒否を予測するコード上の根拠**で、実サーバーの複数source順序・Grant失効時刻を測った結果ではない。推測でFAIL/PASSを登録しない。
- [実TimeStopSkillGoal.timeStop](../build/verification/fantasy-ending-20260920-204205/audit/bytecode/com.mega.uom.common.entity.goals.TimeStopSkillGoal.txt)はcount180をuseより先に設定する。停止中の増加が現行 `Ownership.count` の `direct-count-change` へ入る静的根拠はある。初回goalの§14.11 PASSは当時の台帳に限定し、後続候補の全goal入口へ無条件に転記しない。今回のDはこの入口を追加実行せず、Cを解決したとも扱わない。
- [native SavedData.save](../build/verification/uom-dimension-20260921-101349/audit/bytecode/TimeStopSavedData.txt)がListTagを戻りNBTへ格納しない事実を保持。reloadで何が復帰するかはAの未確認であり、空保存からlive Set空/安全なcold restartを推定しない。

#### c. 次に承認する候補D1 — 2 UOMの順次terminal終了、1 run / 1 epoch

**目的：** native countdownによる「既知source 2→1→0」の各境界で、残sourceの帰属、UNKNOWN、Grant失効、clean boundaryが整合するかを証明/反証する。保護継続と不明時の安全側拒否を別判定にし、未知をALLOWへ緩めない。D全体・SAFE DESIGNを一度に証明する計画ではない。

| 項目 | 具体的な設計（全て次回承認後、今回は未実施） |
|---|---|
| artifact / 環境 | MC1.20.1 / Forge47.4.0、FE2.7.20 / EndingLibrary2.1.19fix / Spells3.16.3 / Curios5.14.1+1.20.1 / GeckoLib4.8.2 / Iron's Lib2.1.0、FHR229,494 bytes/150 entries/5C1A716E…90DB327。§14.24の承認hashを使用。原物7本・既存compat2本・reconnect helper1本の10hash、既知runtime6 path・dedicated参照58 Jarの存在を今回再確認。**追加外部artifact不足なし**。新補助は未作成。client shader/login同期試験は不要で、旧補助・自動fixtureを丸ごと配置しない |
| 新規helperの役割 | 新root `build/verification/uom-terminal-<new ID>/` 内にだけ、`TerminalCountdownVerification.java`（限定driver/receipt）、`TerminalReadonlyObserver.java`と非取消observer（count/terminal useの順序、native/ledger/sessionの別snapshot）、独立metadata/Mixin、専用offline Gradle initを作る候補。現行Ownership/SessionRegistry/MovementGuardの意味・判定順を変えず必要部分だけ新補助へ参照/移植し、差分確認。旧reconnect補助は不変 |
| code / task境界 | scopeはdriver・記録・専用配線だけ。既存local Gradle8.1.1/cache/offlineで限定compile/reobf。製品compileJava/jar/reobfJar/build/unit/check/GameTestをtask graphから除外、own classのみ、製品混入0。**terminalを正当化する新frame/UNKNOWN clear/Grant再発行の修正は、この測定承認に含めない** |
| run / human | **新dedicated process/world 1 run・1停止epochだけ**。実client/Prism/TCP/auth/HUMAN操作なし。native PlayerListのEmbeddedChannel subject 1名でserver側実sessionを用意し、実認証とは記録しない。既存world/receipt不使用。失敗後にreset・別runで成功を取り直さない |
| 初期準備 | Survival/Normal、自然湧き/自然回復false、封鎖床/壁/屋根・source区画、同dimensionでloadedの正式UOM 2体（別UUID）、NoAI/Persistent/targetなし。spawn期間を通常tickで経過し、無関係damage/自発useなしを観測。本人fresh/HP20/MAX20/effects空/count0。検証credit103・P/M各Lv1ON/SP0・使用済103を1回だけ準備し通常canonical経路へ、Truthなし・購入成功とは別。準備失敗は修復しない |
| native入力 / 開始 | native global=false/live Set空/全source count0/use stack0を連続5 server tick確認。同一server-thread準備段で **use(true,U1,true,40,false)、use(true,U2,true,80,false)を各1回**。U1/U2の同epoch・known UOM・FOREIGN0/UNKNOWNfalse/P有効/current sessionを確認してGrant1回。初期開始はDの準備で、旧開始PASSへ件数加算しない |
| countdown | 以降は**通常server tick/EndingLibraryのnative entity tickだけ**。helperからextra entity.tick、count/age setter、時間freeze、延長、明示use(false)を測定入力へ追加しない。U1の実1→0→native use(false)を追い、U2>0であることを確認。成立した場合だけU2の自然1→0まで続ける。通常tickで到達しなければ試験入力未成立として停止 |
| readonly観測 | run/process/thread/server tickと単調時刻、source UUID/type/object/dimension/alive/removed、count HEAD/RETURN、native use HEAD/各RETURN・reset/duration、global/live Set/native canMove、origins/UNKNOWN reason/quiet/stack/epoch、session/current Grant/revoked/sequence、P canonical/HP/effectsを別欄に保存。**observerはpermission/reconcileを呼ばず、途中状態を判定のために変えない**。既存認可判定・revokeはdriverの明示した通常server-END評価で実行し、passive snapshotと区別する |
| 検査点 | ①2source安定、②U1のcount0直後/use HEAD、③U1のnative use RETURNと同tick END、④U2最後のuse RETURN、⑤clean5tick。途中use stack内で追加許可を要求しない。安定点では既存MovementGuardのpredicateも記録するが、移動packet/held stale/vehicleを再配送しない。全比較の参照値は記録済みnative operandを独立readerで照合 |
| PASS | 入口帰属とnative順序を全て観測。U1終了後U2>0の間、global/dimension停止は継続、U1だけoriginから外れ、U2の既知性・同epoch/同Grantによる本人許可を保持する。新epoch/再発行/旧Grant復活0。最後のnative終了後は全source0/global=false/Set空/stack0、通常tick評価でGrant失効、clean5tickで追加許可なし・native通常movementに介入しない。HP20/effects空/外部修復0、通常保存終了まで成立 |
| FAIL / 区別 | 残sourceがあるのにquiet/新epoch/誤ALLOW、失効Grantを認可、native状態改変は**安全性FAIL**。完全観測されたU1終了だけでUNKNOWNが残り、UOM/Pの保護が継続不能なら**候補の機能継続FAIL（安全側DENY）**。後者を安全性の誤許可と混同せず、また全体PASSへ丸めない。どちらも次段階停止。期待をUNKNOWN許容へ弱めない |
| UNVERIFIED | observer欠落/順序不明、U1終了時U2も0、準備不成立、通常tickがcountdownへ到達しない、予定外source/goal発動等では目的条件未成立。**開始後600 server tickまたは60秒wall-clockの先着**を観測上限とし、無期限待機・setterで到達させない。未到達の最後のsource/保存後再許可は観測済みにしない |
| STOP | 上記FAIL/UNVERIFIED、新artifact/版差、外部native矛盾、製品/LOCK条件/session・Grant・UNKNOWN設計の変更が必要な場合。新しい仕様・方式をその場で採用しない。単なる補助compile/記録typoは未測定段階の専用範囲で修正可能だが、測定失敗をresetしてやり直さない |
| 外部state書込境界 | fixtureの初期区画・actor・本人取得準備、上記native開始2回、失敗時安全cleanupだけ。終了時は必要な場合にのみnative use(false)をcleanupとして区別。observer/認可候補はEL global/Set/count/capability、HP/位置/age/NBTを一切修復しない。native countdown/use自身の変化まで「不変」と呼ばない |
| 保存 / 終了 | 成功時は自然終了を観測済みなので追加endなし。seal・own一時状態の通常失効・subject通常remove/保存→save-all flush→stop→全dimension保存/exit/process終了を照合。失敗時も記録後、安全ならnative cleanup、不可なら値を修復せず通常stop。終了後だけ保存をreadonly確認（P/SP/HP/receipt、Grant非永続化）。**再読込・client Quitは非該当**。cleanupで0になった値を自然終了PASSへ流用しない |

**成功しても証明しないもの：** 同tick同時終了・逆順・FOREIGN混在・複数dimension・reset=false・3体以上・自然boss AI全体、Cのgoal再延長、Aのprocess/world reload、Bのfault完全性、Eのreal client、Fのproduction readiness、Gのvehicle ALLOW/DENY。新epoch発行/歩行はR8/R9の既存証拠を使い、このD1に追加しない。静的に予測される誤拒否が再現した場合は、その証拠から次の最小設計変更を別承認へ切り分ける。

#### d. 再試験不要・次回承認・現在判定

独立login compatのSTATIC/A–J10/10、R1–R9、player17/vehicle16/held stale、一般source転送成功/UOM非対応、shader3段階、L2133/core60/Cube49/Invader86、migration/旧限定実clientは再証明不要。D1は**既存の「明示終了」と未測定の「複数source terminal tick」を区別する**ための追加1単位である。

**次回承認範囲候補：D1専用の新root/別helper・限定offline compile/reobf/非混入確認→新dedicated/world 1 runの上記測定→成功またはSTOP後の通常保存/終了・証拠照合→3文書更新。** 追加外部artifactなし、real client/本人操作なし。購入/製品/既存helper/compat/原Jarの変更、UNKNOWN設計修正、他残件実行は含めない。今回はその承認を受けたとは扱わず設計完成で停止する。

**SAFE DESIGN PROVEN=NO / SOURCE DIMENSION TRANSITION VERIFIED=NO / BLOCKED - TIME STOP SOURCE OWNERSHIP**。FE6 MobEffect/TimeStop production NOT IMPLEMENTED、P/T IMPLEMENTATION_PENDING・購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、UOM/P vehicle未LOCK、他機能の個別開始条件を維持。食料生産の極意の可逆クラフト増加は既知許容仕様・バグ修正対象外。


<a id="uom-terminal-d1-result"></a>
### 14.26 D1：2 UOMの非同時native terminal — 機能継続FAIL / SAFE-SIDE DENY（2026-09-21 21:59 JST）

利用者の実行承認で§14.25 D1だけを実施。§14.11–14.25は各時点の履歴/計画として保持し、過去の「次」「未承認」を現行指示にしない。**D1全体PASSではない**。§14.24のSTATIC/dedicated10/10・R4–R9、旧R1–R3や他suite/実clientは再実行0で既存結果を維持する。

- run **20260921-214412**、新root `build/verification/uom-terminal-20260921-214412/`、新dedicated PID18812/新world各1・停止epoch1だけ。全てAUTOMATED、EmbeddedChannelのnative PlayerList subject1名。実client/Prism/HUMAN/実認証TCP/移動・vehicle packet/再読込なし。
- 実ロード：MC1.20.1 / Forge47.4.0 / FE2.7.20 / EndingLibrary2.1.19fix / Spells3.16.3 / Curios5.14.1+1.20.1 / GeckoLib4.8.2 / Iron's Lib2.1.0 / FHR3.0.0。nested playeranimator1.0.2-rc1+1.20とmixinextras0.4.1も記録。外側modsは承認7原物＋今回helper8本だけ、nested重複配置/新downloadなし。[ロード版/配置hash](../build/verification/uom-terminal-20260921-214412/audit/loaded-artifacts.json)。shader/login compat・旧reconnect helperは配置しない。
- 専用helper **20260921.214412、50,480 bytes /25 entries**、SHA-256 **94125F1747662C14F7CF4103DADFD74AAAE85F09EA12637814A8E17772DBB413**。[検査](../build/verification/uom-terminal-20260921-214412/audit/helper-jar.json)。Ownership/SessionRegistry/MovementGuardと既存lease codec/clientクラスの計5sourceは旧候補とbyte同一。新driver/独立passive observer/非取消Mixinだけ追加、UNKNOWN/session/Grant/epoch/sequence/protocolを変更しない。
- buildは既存Gradle8.1.1/cache `--offline` の専用compile/reobfのみ。最初はnative DLLアクセスでコードcompile前停止、承認範囲の権限実行へ移行。次に補助のLazyOptional supplier・difficulty getter・listener引数のcompileエラー計5を**repair cycle1回**で修正し成功。測定後helper変更/再build/再試験0。通常product build/unit/check/GameTest等はtask graph外。`downloadMcpConfig`名のtaskもoffline cache使用。[task graph](../build/verification/uom-terminal-20260921-214412/audit/task-graph.txt) / [成功log](../build/verification/uom-terminal-20260921-214412/audit/compile-03.log)。
- Survival/Normal、自然湧き/自然回復false、本人とsourceの床壁屋根付き別室。新UOM2体は同dimension・別UUID・NoAI/Persistent/targetなし。通常tickでspawn期間を待ち、本人fresh/HP20/MAX20/effects空を確認してprepare1回、P/M Lv1ON・検証credit/spent103/SP0、Truthなし。GUI購入成功ではない。開始前native globalfalse/Set空/count0/stack0をEND90–94の連続5tick確認し、同threadでnative開始40/80を各1回、Grant epoch1/sequence1を1回発行。

| 観測点 | U1 / U2 count | native停止・台帳 | Grant/判定 |
|---|---|---|---|
| 2 source安定・END94 | 40 / 80 | globaltrue、Set={overworld}、known2/FOREIGN0、UNKNOWNfalse、epoch1/stack0 | 同session1のGrant1、permissiontrue、guard DENYfalse |
| U1のnative setter RETURN | 0 / 41 | U1 1→0、UNKNOWNfalse、origin2/stack0 | 追加の認可評価なし |
| use(false) HEAD前→候補beforeUse後 | 0 / 41 | reset=true/duration180/soundtrue。reconcileで `source-disappeared-without-observed-end:43006704-873b-4934-8674-1bdc893741ae`、UNKNOWNtrue、stack1 | 入口で新epoch/Grant追加なし |
| native use RETURN→候補afterUse | 0 / 41 | native early-return、global/Set停止継続。U1originだけ除去・U2origin保持、stack0、UNKNOWNが残る | Grantはまだ未失効、処理途中の追加認可なし |
| 同frame END134 | 0 / 40 | U2が通常1減少、globaltrue/Set={overworld}、quietfalse/UNKNOWNtrue/epoch1/stack0 | permissionfalse/guard DENYtrue、Grant1失効、再発行0。**機能継続FAIL / SAFE-SIDE DENY** |
| U2自然terminal・自然clean5tick | 未到達 | 最初の本質的FAILで測定終了 | **UNVERIFIED**。sealなし |

証拠：[全snapshot](../build/verification/uom-terminal-20260921-214412/audit/snapshots.jsonl)、[不変のruntime RESULT](../build/verification/uom-terminal-20260921-214412/audit/D1-RESULT.json)、[独立読取照合/行番号付き順序](../build/verification/uom-terminal-20260921-214412/audit/reviewed-results.json)。auditの`tick`はEND callbackの連番なので、END133後〜END134前のnative callbackは133、次ENDは134と記録される。**同server thread・単調時刻と隣接ENDにより順序を照合**し、停止で固定されたgameTime94をtick時計に使わない。runtime原記録は書き換えていない。

判定の理由：HP20/MAX20/effects空・P正常canonical・current session有効・U2positive/同originは維持。FOREIGN0、早すぎるnative停止解除/quiet/epoch変更/UNKNOWNの誤ALLOW/失効Grantの認可は観測していない。**正常な既知terminalの認識に失敗する機能継続上の反例**であり、製品TimeStop不具合や危険な誤ALLOWと同一視しない。測定はEND40回分、60秒/600tick上限内。count/age setter、extra entity.tick、延長、明示end、native state修復を測定入力へ使っていない（native自身のsetterは観測対象）。

**終了：** RESULT保全後だけU2へnative use(false,true,0,false)をcleanupとして1回。globalfalse/Set空/U1,U2 count0/stack0/origins空、Grant失効を確認したが、これは**自然U2終了やclean5tickのPASSではない**。subject通常PlayerList remove/保存→21:52:13 save-all flush/stop→21:52:14全5dimension保存・Java exit0/PID終了。[process](../build/verification/uom-terminal-20260921-214412/server/process.json) / [独立PID確認](../build/verification/uom-terminal-20260921-214412/audit/process-readback.json)。再起動/再読込/client Quitは非該当。

保存後readonlyでP/M各1・ON、Truthなし、SP0/使用済103、Health20/effects空、pendingfalseを確認。playerdata/level.datに候補Grant/epoch/ownership永続キーなし。[保存照合](../build/verification/uom-terminal-20260921-214412/audit/readonly-save.json)。有限の保存照合をprocess/world reloadのPASSにしない。loaderの既定Config補完・外部recipe/advancement/接続失敗WARN等は原logに保持し、全WARN解消とはしない。

**保全：** product source/test/build.gradle/Jar/gate、承認原Jar、旧helper/compat等の開始時413files hash一致、今回配置8本は原物と一致。[保全確認](../build/verification/uom-terminal-20260921-214412/audit/preservation-after.json)。製品は229,494 bytes/150 entries、SHA-256 **5C1A716EEDE7AEB4449B36EEFFBDDA156176B16B60A1676229224AA3390DB327** のまま。変更は今回新rootと最後の3文書のみ。

**次の1作業：D1の保存済み証拠を基に、既知sourceのnative count1→0からuse(false)までを帰属できる最小変更候補をREAD ONLYで設計する。** 未観測の消失・direct変更・FOREIGNを引き続き拒否できる条件を比較し、UNKNOWNの一律解除やGrant再発行で回避しない。今回は機能継続FAILを保全して終了。[§14.26](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-terminal-d1-result)。設計変更の実装・新run再試験・A/B/C/E/F/G・製品写像へは自動で進まない。追加外部artifact不足なし、UOM/P vehicleは未LOCK。

**SAFE DESIGN PROVEN=NO / SOURCE DIMENSION TRANSITION VERIFIED=NO / BLOCKED - TIME STOP SOURCE OWNERSHIP**。FE6 MobEffect/TimeStop production NOT IMPLEMENTED、P/T IMPLEMENTATION_PENDING・購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、UOM/P vehicle未LOCKと既存個別gateを維持。食料生産の極意の可逆クラフト増加は既知許容仕様・バグ修正対象外。


<a id="uom-terminal-witness-design"></a>
### 14.27 正規native terminalの帰属 — READ ONLY候補比較・D1-W設計（2026-09-21 22:29 JST）

**推奨はCの限定案。STATIC DESIGN ONLY、未実装・未検証・次回実行未承認。** §14.26の実測FAILと原記録は一切変更せず、D1を再現し直していない。今回は既存bytecode/変換後class/候補source/保存済み証拠の読取と最後の3文書更新のみ。以下の「追加/変更」は将来の別verification helperに対する候補であり、今回作成したコードではない。

#### a. 原因とexact native siteの成立性

- [D1実測照合](../build/verification/uom-terminal-20260921-214412/audit/reviewed-results.json)、snapshot254–260/263–265：U1 count1→0の後、common `beforeUse`冒頭のreconcileがpositive-only loadedからU1を除外。一方origin削除はafterUseなので、正常な同一sourceの終了を「説明不能な消失」と認識する。nativeはU2 positiveゆえearly returnし、U2 origin/global/epochは正常だが、UNKNOWNが残ってEND134にGrant1失効。これは機能継続FAIL / SAFE-SIDE DENYで、危険な誤ALLOWの証拠ではない。
- [原物LivingEntityMixin.tick](../build/verification/fantasy-ending-20260920-204205/audit/bytecode/com.mega.endinglib.mixin.time.LivingEntityMixin.txt)：server・count>0分岐で `setTimeStopCount(e,getCount(e)-1)`（offset45）→再読取≤0→2引数`use(false,e)`（57）。try範囲16–60、catch Throwable→printStackTrace。**methodの通常RETURNだけでは内部で飲み込まれた例外を識別できない。**
- [D1の実変換LivingEntity.class](../build/verification/uom-terminal-20260921-214412/server/.mixin.out/class/net/minecraft/world/entity/LivingEntity.class)をjavapでREAD ONLY確認。対応するmerged handlerは今回の出力では`handler$zeo004$tick(CallbackInfo)V`、native setter43・use55、catch範囲17–58。`MixinMerged.mixin=com.mega.endinglib.mixin.time.LivingEntityMixin`、priority1000も実在。**生成名zeo004を別runの固定targetにしない**。
- [TimeStopUtils実物](../build/verification/fantasy-ending-20260920-204205/audit/bytecode/com.mega.endinglib.util.time.TimeStopUtils.txt)：2引数→3引数reset=true→common5引数`(false,e,true,180,true)`。他alive positive sourceがあれば本人count0設定→early return、最後はglobalfalse/dimension削除→packet→count0。cap setterは`TimeStopEntityData`の実lambdaを通る。よって**数値1→0とuseの引数だけではcallerを証明できず、exact callback/call-siteの囲みが必要**。
- 追加で設計上必要と判明した点：現行originsは`Map<UUID,String>`でobjectを記録していない。最後のsourceではmissing除外だけ直しても`live.isEmpty()→stopped-without-source`でUNKNOWNになる。また現行quiet分岐はUNKNOWNを消してoriginsを空にするため、partial callback後にnativeだけ静止しても正常終了扱いしてはいけない。

#### b. A/B/Cの比較

A＝beforeUseで「current sourceのcount0」を一時除外。B＝use(false)入口の既知originだけで正常終了扱い。C＝exact branchで実証した一回限りwitness。各案を単純に実装した場合の比較で、A/BへC相当の証拠を後付けして同一視しない。

| 比較軸 | A：current count0除外 | B：既知origin＋use入口 | C：exact branch witness |
|---|---|---|---|
| 変更箇所 | beforeUse/reconcileの除外引数 | beforeUseのorigin判定/順序 | native site観測、transient witness、ref binding、限定reconcile/commit/fault guard |
| 正常terminalの根拠/runtime | D1のcurrent=0は観測できるが由来は証明不能 | D1の既知originは証明できるが終了原因は不明 | D1で実在するnative decrement→use分岐とcall chainを根拠にできる。witness自体のruntime成立は未確認 |
| 誤ALLOWリスク | direct0後の呼出しを隠す | arbitrary0/想定外endを既知というだけで受理 | source/ref/site/epoch/順序/一回性とfault guardが全部必要。欠落時は採用せずDENY |
| 誤DENYリスク | 最終live空・sticky UNKNOWNは別途残る | 同左。先行UNKNOWNの解消にもならない | shape drift/observer欠落では意図的にDENY。native goal再延長等は解決しない |
| direct count変更 | nativeと同値のdirect0を区別不可 | direct0→use(false)を区別不可 | exact decrement context＋setter HEAD/RETURN実値以外はwitnessを作らない |
| FOREIGN | 型/全source照合を追加しても終了由来は不明 | currentだけ既知なら他FOREIGNを見落とす | currentと残sourceすべての既知UOM帰属を確認。FOREIGNの参加/変化は例外扱いしない |
| exception/partial | 一時除外や先行削除が残り得る | RETURN未到達でも正常扱いしやすい | native catch内通知＋外側finally、未完了token→fault latchでUNKNOWN/DENY |
| 同tick複数source | currentだけの例外を安全に合成できる根拠がない | originの削除順だけに依存 | source-ref keyed token＋callback stack。別sourceの逐次処理は別token、nestedは拒否 |
| 同UUID別object | UUID/keyだけでは漏れる | UUID originだけでは漏れる | 開始時ref bindingと同じobject/level/serverを要求、terminal時にUUIDから再構築しない |
| reload | 除外が一時的でも古いoriginを信用する問題が残る | 同左 | witness/binding/faultはメモリのみ。旧tokenをNBTから復元せず、reload安全性全体はA残件 |
| 実装量 | 小だが証拠不足 | 小だが証拠不足 | 中。exact branch・一時記録・commitに限定、永続ledger/新protocolなし |
| 次回の検証容易性 | D1だけなら通してしまい得る。negativeで由来不足が出る | 同左 | native順序とtoken状態を独立に比較できる。静的site保証＋小さいnegative契約が必要 |
| 採否 | **不採用** | **不採用** | **第一候補。安全性は設計上の条件であり、PROVENではない** |

#### c. 推奨C：exact native callback内だけの一回証拠

**配線候補を具体化：** 別helperの`NativeTerminalSitePlugin`で、実在するMixin0.8.5の`IMixinConfigPlugin.postApply(...ClassNode...)`を使う限定変換を検討する。ELのpriority1000適用後に、上記MixinMerged owner/descriptor、server分岐、get/decrement/set/get/use、native catch構造が**各1か所だけ完全一致**することを確認して囲む。単なる`LivingEntity.tick HEAD`、setter全体、stacktrace文字列、生成名のprefix検索だけでwitnessを作らない。0/multiple match・順序/版/hash差は開始前STOP、無加工で認可候補を有効化するfallbackなし。

囲みはcallbackのentry/exit・既存decrement呼出し・既存use呼出し・native catchを対象とし、**元callを同引数で必ず1回だけdelegate**する。native branch/control flow/例外処理/packet順をコピー置換しない。count/global/Set/capability/entity/HP/位置/native canMoveを書き換えず、追加のuse(false)も発行しない。MixinExtras0.4.1のWrapOperation/WrapMethodは承認EL内のさらに内側Jarに実在することを確認したが、それだけでmerged methodへ安全に適用できると断定しない。第一案は構造検査付き限定plugin配線、実装時の変換後class照合が必須。ASM/Mixinは既存runtimeにあり、新外部依存は不要。

**保存しない情報：**

| 一時記録 | 必要な内容と寿命 |
|---|---|
| OriginBinding | 既存use(true)の正常RETURNで、そのoriginに対応するsource実ref・UUID・type・ServerLevel実ref/ID・server実ref・epochを記録。既存UUID/type台帳への照合用であり、別の所有権を付与しない。終了/台帳破棄時に破棄。既存originに後からUUID検索でrefを補充しない |
| CallbackFrame | exact callback invocation object/token、固定call-site identity（承認artifact＋構造）、server thread実ref、開始server-tick ordinal・nano/order、entry depth・異常フラグ。callback外では無効 |
| TerminalWitness | source/cap実ref、UUID、level実ref/ID、server実ref、epoch、既存Binding実ref、old1/new0、作成時known UOM/FOREIGN0/UNKNOWNfalse、callback/token、独自witness sequence、作成tick/nano/order、state・一回消費状態。Grant sequenceとは別でwireへ送らない |
| 格納 | `IdentityHashMap<LivingEntity,...>`等のsource-ref keyed構造＋server-thread callback stack。global単一slotやUUID-only mapは使わない。重複同sourceは置換せずfault。永続化・再読込・packet復元なし |

**手順候補（まだコードではない）：**

1. exact native callback entryを観測する。現在known origin/Binding同一ref・alive/非removed・同level/epoch、native停止整合、全現在sourceのUOM帰属、pre-existing UNKNOWNなしを確認。binding欠損/duplicate UUIDや別objectへのすり替わりは拒否。
2. exact decrement callでoldCount=1、実引数0を確認して短いdecrement contextをarmする。元setterを1回実行し、実capのHEAD/RETURN ack・同cap/source・実newCount=0・例外なしを確認して初めてwitnessを作る。一般getterの既定0やsetter要求値だけで成功にしない。非terminalのnative n→n−1もこの実site contextで識別し、use外の無帰属direct変更（減少も含む）をnative countdown扱いしない。native use内の既存開始/終了setter契約は別に維持。
3. 直後の**同じcallback内のexact2引数use call**からのみdelegation frameを開く。common5引数HEADで、same ref/UUID/level/server/epoch/thread/callback・未消費token、`false,true,180,true`、expected depth、interval内の想定外変更なし、UNKNOWNなし、FOREIGNなしを再照合し、witnessを1回消費して対応use frameへ移す。引数が一致するだけの別callerは不可。ここでoriginは削除しない。
4. internal reconcileは当該frameに限り、証拠のある1 sourceの「positive集合から消えてoriginに残る」差を説明済みと扱う。残sourceの全照合はそのまま行う。witnessをpositive source集合へ偽装追加しない。**最後のsourceでlive空の場合も、全残originがその正当pending終了で説明できるときだけ`stopped-without-source`を処理中扱いにする。** quietへは移らず、追加のpermissionは必ずfalse。未説明の空集合は従来どおりUNKNOWN。
5. native common RETURNで`RETURN_SEEN`を記録し、2引数delegate RETURN・callback正常完了まで対応frame/一時provenanceを保持する。既知UOM残存時はglobal/dimension継続・本人count0・他source不変、最後はglobalfalse/対象dimension停止なし・live空等のnative postconditionを確認。native catch通過/RETURN欠落がなければ、**afterUseの正常完了を条件にcallback exitで当該origin/Bindingだけcommit削除**し通常reconcileへ戻す。callback内のquiet分岐でorigins全削除/UNKNOWN clearを先行させない。
6. 先行U1だけ終了した安定点は同epoch・同Grantを維持。最後の正常終了では既存の通常評価でGrant失効・clean5tickへ。issue/reissue/sequence増加/epoch更新/revoke→即再grantはこの機構から一切行わない。

**origin削除タイミングの比較：** beforeUse即削除は不採用。native実行前にprovenanceを失い、例外・wrong early-return・partial終了を成功に見せる。afterUse側のRETURN後削除を採用候補とするが、common RETURNだけを最終commitにせず、上記のcallback正常完了まで保留する。これは例外時の巻戻し/再注入を避けるための短いcommit待ちであり、originを早く消してD1だけ通す順序変更ではない。

#### d. failure semantics・既存認可との整合

- witnessの有効期限は**同じserver-thread callbackの直後の対応callまで**。callback exitまたは次のserver-ENDが先に来たら失効。wall-clock幅を設けて別callから回収しない。gameTimeは停止するため期限時計に使わず、server-tick ordinal/monotonic orderは照合・監査に使う。
- 作成後use未到達、missing/double-consume、同source重複、ref/UUID/level/epoch/thread/call-chain不一致、nested/unexpected use、direct0/deserialize、remove理由不明、FOREIGN参加、形状/observer mismatch、内部catch/例外/partial callbackでは**faultを先に記録→tokenを無効化→UNKNOWN=true/permission=false**。other sourceを消したりoriginを修復しない。Grantは既存revoke経路で失効し、再発行なし。
- 例外監視は元catch Throwable内部の通知と外側try/finallyの両方。元例外をそのままnativeへ渡し、追加のnative操作で成功形に合わせない。callback RETURNだけ、時間が過ぎたこと、ログが出たことを成功証拠にしない。pending witness/call frameがある間の再入permissionはfalseで、新しい認可を作らない。
- **一時的terminalFault latchが必要**：現行reconcileの「native非停止ならunknown=false/origins.clear」に先行して検査し、terminal failure後はnativeが0/quietになっても自動clearしない。run中にlatchをresetしない・quietで回復させない・新epochを発行しない。失敗runはSTOP/通常終了する。この限定failure barrierは設計上の追加で、今回実装済みでも、任意UNKNOWNの回復方針をLOCKしたものでもない。
- 完全に欠落した観測を後から推測してwitnessを作らない。静的適用数/descriptor/branch gate不成立は候補自体を開始しない。runtimeは各段階のackとEND時の未消費frame検出を必要とし、他のobserver/fault全般の完全性証明へ拡張しない。
- sourceの帰属証拠はplayer sessionの許可ではない。R1–R9のServerPlayer/listener/Connection/channel同一性、freshEpochRequired、disconnect不可逆revoke、old session拒否、Grant.matches、P条件を全て引き続きANDする。witnessはこれらを更新しないためstale session/epochへ漏らさない。player/vehicle guard、login sync compat、client lease/期限・wireは変更しない。native canMove=true時の既存通常経路や未LOCKのvehicle ALLOW方針を新たに決定しない。

#### e. 複数sourceと対象外境界

| 順序 | 設計上の扱い（未実測） |
|---|---|
| U1先行・U2 positive | U1のtokenだけ消費/commit。U2のref/帰属・global/epochと既存Grantを維持 |
| U2最後 | pending中はlive空を説明するが許可は作らない。native正常RETURN/exit後だけorigin空・quiet、既存Grant失効 |
| 逆順 | U1/U2というラベルや最初のUUIDに依存せず同じref-keyed規則。独立runtime証明は未実施 |
| 同tick別source | 通常の同thread逐次callbackは各tokenの作成→消費→commitを独立に完了。tick番号だけで同一視しない。別sourceの未観測count0を1枚のtokenで説明しない。再入/nestedならfail closed |
| 3 source以上 | 同じ局所規則で終了1体以外を全照合。単一slot上書きなし。有限mapの正常終了/失敗無効化で保持を終える。多数entity性能は未証明 |

process/world reload、転送障害全般、FE goal再延長、client input/render/correction、production mapping、vehicle方針は別残件。今回の案をそれらの解決やSAFE DESIGNへ転記しない。

#### f. 次回候補は1単位「D1-W：terminal帰属＋新しい境界のnegative」だけ

必要予定ファイルは**新root内だけ**の`TerminalWitness`/`TerminalWitnessBridge`/`NativeTerminalSitePlugin`、Ownershipの限定拡張、Count/Use observer・専用driver/metadata/init。旧D1 helper/source/Jar/receiptを変更しない。製品source/test/build.gradle/Jar、FE/EL原物、compat2種、session/Grant/wireは不変。実装を承認された場合も、まずSTATICで各site1一致・元call回数/例外伝播・非混入を確認し、不成立ならゲーム測定前STOP。

必要な受入点を理由で選ぶ。D1の元FAIL再証明ではなく、**変更後候補の証拠**を採る。

| 点 | 次回案・合格条件 | 採る理由/証拠の種類 |
|---|---|---|
| W1 正常U1/U2 terminal | 新しい候補で40/80をnative開始各1。U1後もUNKNOWNfalse・同Grant/epoch、U2最後はnative終端・Grant失効・clean5tick。count/age/extra tick/延長入力なし | D1反例の解消と、従来未到達の最後のsourceで別のempty判定を誤らないことを1epochで確認。native integration |
| W2 direct0＋unexpected end | 別clean runのknown sourceへ**試験入力としてのみ**exact branch外からcount0を1回設定。witness0/即DENY、その後native shapeが同じuse(false)を1回呼んでもUNKNOWN/旧Grant失効を維持 | A/B案が誤認する組合せを実経路で排除。後半は回復しない検査で、単独unexpected検査とは区別。native negative integration。測定値修復ではない |
| W3 FOREIGN＋native terminal | 別clean runでUOM既知開始後にnative Cow sourceを追加し、UOMの自然terminalを観測。FOREIGN0条件不成立でwitness免除なし、許可/再発行0 | 他source照合をCの例外で飛ばしていないことを確認。既存FOREIGN単独試験の繰返しとは異なる組合せ |
| W4 witness中のnative例外 | 別clean runでexact terminalのsetter成功後〜対応use完了前の1点に、検証専用throwを1回。元catchを通り、fault latch/UNKNOWN/DENY・token無効、正常commitなし | nativeがThrowableを飲み込む新経路の実配線を検査。例外後quietで回復させない。helperの試験用throwであり通常native反例と区別 |
| 独立契約negative | 実装予定の同じmatcher/状態遷移を使い、①directなしのunexpected use(false)、②同UUID別ref、③stale epoch/level/thread、④未消費exit/重複消費/同source重複、⑤RETURN ack欠落・pre-existing UNKNOWNを分離した有限入力表で拒否確認。各拒否理由が想定のguardから出ることを検査 | 新しい証拠のidentity/一回性/部分観測を最小の分岐代表で確認。実worldの別object/転送/全fault再現PASSとは呼ばない。既存session拒否等の全matrixは再実行しない |

**計4つの新規dedicated process/worldを上限とする1実装・検証単位**。W1の完了と、W2–W4の期待されたDENYを別記録する。1つのfaultで汚染された台帳をresetして次のnegativeへ使わないため分離する。新run開始前に契約表と入力点を固定し、各world/subject/receiptは使い捨て・準備1回。W2の後半だけは同じfaultを保ったままの非回復確認であり、正常な初期状態からの単独unexpected拒否は契約検査側で別証明。新しいrunの無制限追加は禁止。

Survival/Normal、自然湧き/回復false、封鎖区画、fresh P/M1ON・credit/spent103/SP0・HP20/effects空、EmbeddedChannel subjectを引き継ぐ。各native測定は600 server tick/60秒先着上限。W2のdirect入力とW4のthrow以外はnative count/時間/HP等のfixture修復なし。失敗/配線欠落/想定外native結果・製品/LOCK変更要求はSTOP、期待値緩和や成功取り直しなし。予期したnegative DENYはそのcaseの限定合格で、native lifecycle全体PASSではない。

終了は各runで結果保全→必要時だけ区別したnative cleanup→subject通常remove/save→save-all flush→stop/全dimension保存/exit確認。保存をreadonly確認しwitness/Grant非永続を点検するが、reloadは行わない。real client/HUMAN/Prism/実TCP認証は不要。W1の自然終端とnegative後cleanupは合算しない。逆順/同tick/3UOM runtime・多dimension・全fault/performanceは次回も未証明のまま残す。

**追加外部artifact不足なし**：承認7原物、Forge47.4.0、既存Gradle/cache/ASM/Mixinで設計対象は揃う。hookの適用可否・変換後配線は未検証であり、必要artifactが本当に増えた場合だけ別途停止する。R1–R9/login compat10/player17/vehicle16/shader/L2133/core60/Cube49/Invader86/migration/既存実clientは再試験しない。

**次の1作業：別の明示承認後、[§14.27のD1-W](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-terminal-witness-design)だけを、新しいverification helperへ実装・限定offline compile/reobf・静的配線確認・有限negative契約確認・新規dedicated検証へ進める候補。** 正常2→1→0、direct0、FOREIGN、terminal内例外を互いに汚染しない4 process/worldで確認する設計。unexpected use(false)/同UUID別ref/欠落・重複等は限定契約検査と対応native照合で分ける。今回は設計で停止し、実行は未承認。旧D1や既存PASSを再実行せず、製品写像・他lifecycle・購入解放へ広げない。追加外部artifact不足なし、UOM/P vehicle未LOCK。

**SAFE DESIGN PROVEN=NO / SOURCE DIMENSION TRANSITION VERIFIED=NO / BLOCKED - TIME STOP SOURCE OWNERSHIP**。FE6/TimeStop production NOT IMPLEMENTED、P/T IMPLEMENTATION_PENDING・購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、UOM/P vehicle未LOCKと既存個別gateを維持。食料生産の極意の可逆クラフト増加は既知許容仕様・バグ修正対象外。


<a id="uom-terminal-witness-result"></a>
### 14.28 D1-W：exact terminal witness候補の限定検証 — W1/W2 PASS・W3環境STOP（2026-09-21 23:03 JST）

§14.27への明示実行承認により別helperを実装。**D1-W全体LIMITED PASSにはしない**。§14.26の旧FAIL/cleanupと§14.27の設計本文は保全し、その古い「次/未承認」を現在指示へ戻さない。run `20260921-224127`、新規dedicated/worldはW1/W2/W3の3つだけ。W4はSTOP後未作成。すべてAUTOMATED、EmbeddedChannel本人、real client/Prism/HUMAN/実認証TCP/再読込0。

#### 実装・STATIC・契約

- helper **uom_witness_verification /20260921.224127、75,138 bytes /33 entries**、SHA-256 **D66F840F9C1E77812B62E880175058A48B1002EC41877825B6834821FD560A8D**。[STATIC/非混入](../build/verification/uom-terminal-witness-20260921-224127/audit/static-gate.json)。既存Gradle8.1.1/cacheのoffline専用compile/reobf成功、製品build/testなし。最初のnative-platform.dllアクセスエラーはcompile前、権限付きローカルoffline実行で成功。helper修正repair **0回**、測定後helper変更/再build **0回**。
- source実ref keyedのOriginBinding/CallbackFrame/TerminalWitnessとmemory-only fault latchを追加。成功use(true) RETURNでbind、exact decrementの実cap HEAD/RETURN確認→一回mint/consume→common/2arg RETURNとcallback正常exit後に当該originだけcommit。pending中は追加permissionなし、faultはquietでも解除しない。SessionRegistry/MovementGuard/既存wire/ClientLeaseの4sourceは旧D1とbyte同一。witnessからGrant/epoch/sequence発行・native状態修復・保存/送信0。
- 対象EL **2.1.19fix**、原物hash **0E29AF51DABD4E0EA8A315C1886E61F3ABEE62045F196EB6913D7F4DBA6CDC34**。`net.minecraft.world.entity.LivingEntity`へmergeされた`com.mega.endinglib.mixin.time.LivingEntityMixin.tick(CallbackInfo)V`をorigin annotation＋命令構造で特定。generated handler名固定なし。server条件/get/subtract/set/get/terminal use/catchはexact1、元setter/use各1delegate、native catch通知/outer exception/normal exit配線を静的照合。既存実変換classへの同じtransformerとASM BasicVerifierはPASS、0/重複/descriptor差の3構造negativeは拒否。3起動時も各1適用を確認。**W4未実施なのでnative catch実通過は未検証**。
- 同じruntime matcher/遷移によるA–J契約は **10/10 DENY/FAULT**。[契約結果](../build/verification/uom-terminal-witness-20260921-224127/audit/contracts.txt)。unexpected入口、同UUID別ref、stale epoch、wrong level/thread、未消費exit、double consume、duplicate witness、RETURN欠落、既存UNKNOWN。実dimension transfer/reload/全faultのPASSではない。

#### 実測結果

| case | native実測と判定 | witness 作成/消費/commit |
|---|---|---|
| W1 | **LIMITED PASS**。40/80開始・epoch1/Grant sequence1。U1 native1→0→use(false) early return→commit後、U2=40、UNKNOWNfalse、同Grant/epoch維持。U2も自然1→0→commit、globalfalse/Set空/count0,0/origin・binding・frame・witness空/faultfalse、通常Grant失効・clean5tick。84 END tick/約4.186秒 | **2/2/2** |
| W2 | **LIMITED PASS（negative）**。known UOMへのfixture direct0入力1回でfault/UNKNOWN/DENY・Grant失効、witness0。続くunexpected2arg use(false)1回でも再許可/再epochなし。native quietを5tick観測してもfaultを保持。8 END tick/約0.391秒 | **0/0/0** |
| W3 | **UNVERIFIED / 環境準備FAIL**。END96のnegative入力前からCowがalivefalse/removedtrue/age0/count0。native use(true,80)を呼んだがFOREIGN positiveは成立せず、END97 `FOREIGN did not deny`でSTOP。実positiveはUOM1体(count37)だけ、UNKNOWNfalse/Grantあり。**有効FOREIGNの誤ALLOWとは判定しない**。UOM terminalとの組合せは未到達 | **0/0/0** |
| W4 | **NOT RUN**。W3停止後は新process/worldを作らず、固定throw/native catch/fault非回復は未実測 | 未実施 |

[W1独立照合](../build/verification/uom-terminal-witness-20260921-224127/W1/audit/reviewed-results.json) / [W2](../build/verification/uom-terminal-witness-20260921-224127/W2/audit/reviewed-results.json) / [W3停止照合](../build/verification/uom-terminal-witness-20260921-224127/W3/audit/reviewed-results.json)。原RESULT・snapshot・journal/receiptを改変しない。audit tickは直前END連番で、native callbackは同server thread/nano順で照合。停止中gameTimeを期限時計に使わない。再grant/new epochは各run **0**、初期epoch1/sequence1発行だけ。

#### W3停止理由と次回に限定する修正候補

旧D1はUOMだけだったが、そのrunnerの`spawn-animals=false`をCowを必要とするW3へ引き継いだ。実runtime `ServerLevel.m_143342_`は`MinecraftServer.m_6998_()==false`かつAnimal/WaterAnimalでtrueを返し、tick側でnative discardへ進む。`DedicatedServer.m_6998_`が読む`f_139731_`は同classの診断文字列でspawn-animalsと対応。[実変換bytecode](../build/verification/uom-terminal-witness-20260921-224127/audit/runtime-removal-bytecode.txt) / [dedicated bytecode](../build/verification/uom-terminal-witness-20260921-224127/audit/runtime-dedicated-bytecode-02.txt)。実保存設定false、Cow leave記録、入力前removedと整合する。除去時の直接stack/RemovalReason enumは未記録なので、そこまでの実測証明とはしない。

加えてdriverはUOM/本人だけを待機中検査し、Cowのalive/removed/loaded/capをprepare/native入力の必須guardにしていなかった。**検証環境と事前guardの不備であり、製品やwitnessのFOREIGN認可反例ではない**。測定開始後のhelper変更→同case取り直しは禁止のため、このrunは復活/再生成/resetせず保全し、W4も開始せず停止した。期待値変更なし。

**次の1作業：別承認後、W3の検証環境とCow生存・loaded/count/帰属の事前guardだけを修正し、新規隔離runで未完了W3/W4を実施する。** [共通計画§14.28](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-terminal-witness-result)。新規serverではCowをnativeに保持できる設定（`spawn-animals=true`と生成前/準備前の`doMobSpawning=false`を区別）を採り、native入力前の対象ref/alive/removed/level/cap確認を必須化する候補。正常回復はfalse、安全区画は維持。witness/認可条件を緩めず、W1/W2・旧D1/既存PASSは再試験しない。今回停止したW3の修復/再利用、同run再送、製品実装は行わない。追加外部artifact不足なし、UOM/P vehicle未LOCK。

#### 正常終了・保存・保全

| run | 終了 | readonly保存 | cleanupの扱い |
|---|---|---|---|
| W1 / PID29420 | 22:53:01 exit0・全5dimension保存・PID終了 | P/M1ON、SP0/使用済103、Health20/effects空 | native自然終端、追加cleanup0、sealあり |
| W2 / PID7192 | 22:53:53 exit0・全5dimension保存・PID終了 | 同上、witness/Grant永続キーなし | negative入力のuse後quiet、追加cleanup0、negative sealあり |
| W3 / PID14060 | 22:55:53 exit0・全5dimension保存・PID終了 | 同上、witness/Grant永続キーなし | RESULT後だけUOM count37へnative cleanup1、globalfalse/Set空/count0・Grant失効。自然terminal PASSへ流用しない。sealなし |

全runでsubject通常remove/save→save-all flush→stopを実施。再起動/再読込なし、Quit Gameはclient未起動で非該当。HP/位置/count/global/Setの修復0（W2の明示negative count0は試験入力として別記）。承認7原物＋helperだけを配置、nestedを外側重複配置せず、実ロード版は各reviewへ記録。ローカル公開鍵stubは検証transport用、本人認証PASSではない。外部Patreon接続失敗・既定Config補完等のWARNはログに保持、全WARN解消宣言なし。

製品 **229,494 bytes/150 entries、5C1A716EEDE7AEB4449B36EEFFBDDA156176B16B60A1676229224AA3390DB327** 不変。製品source/test/build.gradle/原Jar/旧D1/compat/reconnect等 **705files hash不変**、今回3配置のhashも一致。[保全](../build/verification/uom-terminal-witness-20260921-224127/audit/preservation-after.json)。変更前3文書は新root `before/docs/`に保全、最後に1回だけ更新。既存全suite/実client再試験0。

**SAFE DESIGN PROVEN=NO / SOURCE DIMENSION TRANSITION VERIFIED=NO / BLOCKED - TIME STOP SOURCE OWNERSHIP**。process/world reload、transfer fault/observer loss、FE goal途中再延長、client input/render/correction、production mapping、vehicle方針、逆順/同tick/3+source/multi-dimension/全fault/性能は未証明。FE6 MobEffect/TimeStop production NOT IMPLEMENTED、P/T IMPLEMENTATION_PENDING・購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、全個別開始条件を維持。食料生産の極意の可逆クラフト増加は既知許容仕様・バグ修正対象外。

<a id="uom-terminal-witness-followup-result"></a>
### 14.29 D1-W follow-up：W3 FOREIGN／W4 terminal例外 — LIMITED PASS（2026-09-22 13:42 JST）

**今回：D1-W未完了W3/W4を新run `20260922-132519` の別dedicated/worldで各1回実施し、双方LIMITED PASS。既存W1/W2・STATIC・有限negative契約10/10を再実行せず保持し、D1-W候補の限定検証を完了**。[共通計画§14.29](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-terminal-witness-followup-result) / [独立証拠照合](../build/verification/uom-terminal-witness-followup-20260922-132519/audit/reviewed-results.json)。W3は同Cow positiveのままUOM自然terminal・誤ALLOW/再grant/新epoch0、W4は固定throw/native catch各1・witness1/0/0・cleanup後5 ENDのfault非回復。両server通常保存/stop/exit0、製品/旧証拠1,481files不変。旧§14.26 FAIL・§14.27設計・§14.28環境STOPは履歴保全。**SAFE DESIGN PROVEN=NO、ownership BLOCKEDを維持**。

#### 対象・変更境界

新root `build/verification/uom-terminal-witness-followup-20260922-132519/`。W3/W4は別process・別新world、全てAUTOMATED／EmbeddedChannel本人。MC1.20.1、Forge47.4.0、FE2.7.20、EndingLibrary2.1.19fix、Spells3.16.3、Curios5.14.1+1.20.1、GeckoLib4.8.2、Iron's Lib2.1.0（nested playeranimator1.0.2-rc1+1.20／mixinextras0.4.1）を実ロード。原物7本＋別helperのみ、既存world/client/Prism/実認証TCP/外部download/再読込なし。

- helper **uom_witness_verification /20260922.132519、77,577 bytes /33 entries**、SHA-256 **57A4EFE0BA98E92022188D2D6245607F0E2449B4FC23051D96DF7093B74FBF05**。[差分STATIC・class hash](../build/verification/uom-terminal-witness-followup-20260922-132519/audit/static-gate.json)。変更したJavaは`Audit.java`（run/root/W3・W4限定）と`TerminalCountdownVerification.java`（UOM1体、Cow保持ref/UUID・native入力前2連続END guard、測定中guard、W4本質結果とcleanup後5 ENDの分離）の2本。その他はnewroot専用runnerのspawn-animals、Gradle initのpath、mods.toml version、監査reader。W4固定throwは旧bridgeをそのまま使用。
- Java **14/16本byte同一**。class **19/24同一**、うちTerminalWitness/Bridge/NativeTerminalSitePlugin/Ownership/MovementGuardと各nestedの核心byte/hash不変。SessionRegistry/VerificationNetwork/ClientLeaseの3classはinlined run IDだけの差で、run文字列を正規化するとbyte同一（sourceは完全同一）。Grant/epoch/UNKNOWN/guard/wire/lease判定意味の変更0、製品/原Jar混入0。
- 既存local Gradle8.1.1/cache、`--offline`専用task graphに製品jar/reobfJar/build/unit/checkなし。[compile成功](../build/verification/uom-terminal-witness-followup-20260922-132519/audit/compile-01.log)。sandbox native-platform.dllアクセス失敗はcompile前の環境エラー、承認範囲の権限付きofflineで成功。**helper repair0・測定開始後のhelper変更/build0**。旧full STATIC/有限contract10を再実行せず差分だけ照合。両実起動でもexact native site各1、元setter/use delegate・catch wiring不変。

#### W3：Cow保持・実FOREIGNとUOM自然terminal

`spawn-animals=true`、準備前gamerule `doMobSpawning=false` / `naturalRegeneration=false`、Survival/Normal・封鎖区画。CowはNoAI/PersistenceRequired、invulnerable/NoGravity/偽entityではない。guard128 snapshotで同ref/UUID/ServerLevel/lookup、alive/loaded/capability/entity参照、not removed/removalReason null、Cow/nonUOMを照合。入力直前のEND95/96を含む2連続END成立・count0。[事前guard](../build/verification/uom-terminal-witness-followup-20260922-132519/W3/audit/COW-PREFLIGHT.json)。本人freshからprepare1・credit103/使用済103/SP0・P/M1ON・HP20/MAX20/effects空。

| 点 | 実測 | 判定 |
|---|---|---|
| UOM開始 | native use(true,40)1、count40、epoch1/sequence1、UNKNOWNfalse/FOREIGN0、permission=true | W3の準備成立 |
| FOREIGN追加 | 同Cowへnative use(true,80)1。Cow **80** / UOM **38**、FOREIGN1、即permission=false/Grant revoke、同ref alive/loaded | FOREIGN成立 |
| UOM自然terminal | 通常native tickのみ40→…→1→0、exact native use(false)1。結果END134でUOM **0** / Cow **42**、Cow同ref alive/loaded/positive、global/dimension停止継続 | **W3 LIMITED PASS** |
| 認可 | FOREIGN成立後ALLOW0、失効Grant認可0、再grant0、新epoch0、witness **0/0/0**、terminalFault/UNKNOWNtrue・DENY継続 | FOREIGNをwitnessで迂回しない |

[W3独立照合](../build/verification/uom-terminal-witness-followup-20260922-132519/W3/audit/reviewed-results.json)。Cow ref `808571823`、UUID `dd712562-ae79-4806-96e1-8165e3ca0206`。測定40 END/約2.038秒。測定中count/age setter・extra tick・duration延長・明示終了・native/FOREIGN state修復0。native use(false)内部のcount0→0はHEAD/RETURN間の元処理として区別（事後readerでこの既存resetの順序照合を追加、runtime再実行/期待値緩和なし）。RESULT後だけCow42へnative cleanup1、UOMへの追加end0。cleanupを自然terminal PASSへ算入しない。

#### W4：固定throw・native catch・quietでもfaultを保持

W3独立照合LIMITED PASS後だけ、別world/processで開始。fresh本人/P/M/SP/HP条件は同じ、UOM-only native40開始、epoch1/Grant sequence1・FOREIGN0/UNKNOWNfalse。自然countdownのsetter **old1/new0 HEAD→正常RETURN**、witness **MINT→W4_FIXED_THROW→CATCH**の同thread/nano順を実測。[W4照合](../build/verification/uom-terminal-witness-followup-20260922-132519/W4/audit/reviewed-results.json) / [witness順序](../build/verification/uom-terminal-witness-followup-20260922-132519/W4/audit/witness.jsonl)。固定throwはmint後・exactUse前、**1回**。元EndingLibraryのThrowable catch通知とprintStackTraceへ**1回**到達、native callbackは戻り、後続END観測へ進んだ。

- 事前STATIC期待どおり **create/consume/commit = 1/0/0**。通常terminal use完了0、origin/bindingを正常終了としてcommitせず保持、pending frame/witnessは0。Grant失効、terminalFault=true / UNKNOWN=true / permission=false、再grant0・新epoch0。
- END138で[本質結果](../build/verification/uom-terminal-witness-followup-20260922-132519/W4/audit/W4-SUBSTANTIVE-RESULT.json)を先に保存（count0だがnative global=true）。その後だけUOMへnative use(false) cleanup1を実行。**cleanup後の別END139/140/141/142/143で5連続**、global=false / live Set空 / count0 / live source空でもfault/UNKNOWNtrue・permissionfalse・失効Grant・epoch1/sequence1を保持。[5 END証拠](../build/verification/uom-terminal-witness-followup-20260922-132519/W4/audit/post-cleanup-end.jsonl)。quietを理由にfault clearしない。**W4 LIMITED PASS**、測定全体49 END/約2.435秒。EndingLibrary stateのhelperによる修復0、元native開始/countdown/cleanupによる変化とは区別する。

#### 保存・終了・今回の判定境界

| run | 通常終了・process | readonly保存 |
|---|---|---|
| W3 / PID4692 | 13:34:24 exit0、subject通常remove/save→save-all flush→stop・全5dimension保存・PID終了、sealあり | P/M1ON、未使用SP0/使用済103、Health20/effects空、playerdata/level.datにwitness/Grant等永続キーなし |
| W4 / PID8984 | 13:38:19 exit0、同じ正常終了手順・全5dimension保存・PID終了、sealあり | 同上。再読込なし、保存対象を限定したreadonly照合であり全world/再起動保証ではない |

既存asset/tag/loot/Patreon接続失敗等のERROR/WARNとW4予定例外をログに保全し、全WARN解消とはしない。Quit Gameはclient未起動で非該当。製品 **229,494 bytes /150 entries / SHA-256 5C1A716EEDE7AEB4449B36EEFFBDDA156176B16B60A1676229224AA3390DB327**、source/test/build.gradle/原Jar/compat/reconnect/旧D1-W等 **1,481files hash不変、旧D1-Wへの追加file0**。[保全照合](../build/verification/uom-terminal-witness-followup-20260922-132519/audit/preservation-after.json)。製品build/test、W1/W2/finite10/旧suite/実clientは再実行0。変更前3文書は[before/docs](../build/verification/uom-terminal-witness-followup-20260922-132519/before/docs/)、完了後に1回だけ更新。

**D1-W VERIFICATION CANDIDATE LIMITED PASS**（既存W1/W2＋今回W3/W4の限定証拠を合成）。§14.26旧FAIL・§14.27設計・§14.28旧環境STOPは本文を保持し、その時点の未実施を過去からPASSへ書き換えない。今回も**SAFE DESIGN PROVEN=NO / SOURCE DIMENSION TRANSITION VERIFIED=NO / BLOCKED - TIME STOP SOURCE OWNERSHIP**。process/world reload、transfer fault/observer loss、FE goal再延長、client input/render/correction、production mapping、逆順/同tick/3+source/multi-dimension/性能、vehicle方針は未証明/未LOCKのまま。

**次の1作業：process / world reloadで旧Grant・originを誤復元しない条件を、既存証拠からREAD ONLYで整理し、最小dedicated検証1単位を設計する。** [共通計画§14.29](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-terminal-witness-followup-result)。D1-W W1–W4は今回の限定範囲で完了し再試験しない。実process交換と同JVM world reload、native保存値とlive state、一時台帳/sessionの再初期化を区別する。今回は次候補の提示で終了し、新run/helper/compile/再読込/他lifecycle/productionを開始しない。追加外部artifact不足なし、UOM/P vehicle方針は未LOCK。

FE6 MobEffect/TimeStop production NOT IMPLEMENTED、P/T IMPLEMENTATION_PENDING・購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、他の個別開始条件を維持。食料生産の極意の可逆クラフト増加は既知許容仕様・バグ修正対象外。



<a id="uom-process-world-reload-design"></a>
### 14.30 process交換 / same-JVM world reloadのREAD ONLY設計（2026-09-22 14:45 JST）

**DESIGN ONLY / 未実行。次の1単位はA1「別JVM 2-phase dedicatedによる、停止途中の通常保存・読込と旧認可の非復活」**。新run・helper作成/変更・compile・server/client/world起動0。§14.29までのD1-W STATIC/有限契約10/10・W1–W4 LIMITED PASS、§14.24のR1–R9限定結果を保持し、再実行/新しいPASS加算をしない。本節だけを新しい詳細設計とし、過去節の当時の「次」「STOP」を現行指示へ戻さない。

#### 14.30.1 根拠と実際の保存・lifecycle経路

今回の読取根拠（すべて既存物。新しい解析dump/helperは作成していない）：

- 候補authority：[Ownership](../build/verification/uom-terminal-witness-followup-20260922-132519/helper/src/main/java/verification/uom/Ownership.java)、[SessionRegistry](../build/verification/uom-terminal-witness-followup-20260922-132519/helper/src/main/java/verification/uom/SessionRegistry.java)、[TerminalWitness](../build/verification/uom-terminal-witness-followup-20260922-132519/helper/src/main/java/verification/uom/TerminalWitness.java)、[Bridge](../build/verification/uom-terminal-witness-followup-20260922-132519/helper/src/main/java/verification/uom/TerminalWitnessBridge.java)、[CountObserver](../build/verification/uom-terminal-witness-followup-20260922-132519/helper/src/main/java/verification/uom/mixin/CountObserver.java)、[既存driver](../build/verification/uom-terminal-witness-followup-20260922-132519/helper/src/main/java/verification/uom/TerminalCountdownVerification.java)。製品TimeStop実装ではない。
- native保存：[Living capability](../build/verification/fantasy-ending-20260920-204205/audit/bytecode/com.mega.endinglib.common.capability.EndingLibraryLivingCapability.txt)、[EntitySyncCapabilityBase](../build/verification/fantasy-ending-20260920-204205/audit/bytecode/com.mega.endinglib.api.capability.EntitySyncCapabilityBase.txt)、[ELCapabilityManager](../build/verification/fantasy-ending-20260920-204205/audit/bytecode/com.mega.endinglib.api.capability.ELCapabilityManager.txt)、[TimeStopSavedData](../build/verification/fantasy-ending-20260920-204205/audit/bytecode/com.mega.endinglib.common.data.TimeStopSavedData.txt)、[ServerExpandedContext](../build/verification/fantasy-ending-20260920-204205/audit/bytecode/com.mega.endinglib.util.mixin.level.ServerExpandedContext.txt)、[ServerLevelExpandedContext](../build/verification/fantasy-ending-20260920-204205/audit/bytecode/com.mega.endinglib.util.mixin.level.ServerLevelExpandedContext.txt)、[TimeStopUtils](../build/verification/fantasy-ending-20260920-204205/audit/bytecode/com.mega.endinglib.util.time.TimeStopUtils.txt)。
- 実Forge変換後：[Entity.class](../build/verification/uom-terminal-witness-followup-20260922-132519/W3/server/.mixin.out/class/net/minecraft/world/entity/Entity.class)。今回javapでsaveWithoutId/loadのForgeCaps→serializeCaps/deserializeCapsを確認。既存Forge47.4.0 universal/server JarのCapabilityDispatcher、SavedData、DedicatedServer、ServerLifecycleHooks、ReloadCommandもstdout読取のみ。
- Food Healing：[ShokugiData](../src/main/java/com/leva/foodhealing/capability/ShokugiData.java)、[ShokugiProvider](../src/main/java/com/leva/foodhealing/capability/ShokugiProvider.java)、[CapabilityEvents](../src/main/java/com/leva/foodhealing/capability/CapabilityEvents.java)。P/M取得・toggle・SPはcanonicalとしてserialize/deserializeされ、login時は再構築/同期される。Grant/origin等の保存・復元はない。
- 同JVMの実入口：[Minecraft mapped bytecode](../build/verification/uom-reconnect-plan-20260921-103304/audit/bytecode/net.minecraft.client.Minecraft-mapped.txt)。通常退出のclearLevelは接続close・client LevelEvent.Unload・旧IntegratedServer停止待ちを行う。次のdoWorldLoadはMinecraftServer.spinで新IntegratedServerを作り、memory connectionを開く。client JVM/classloaderを終了させない通常menu→world再入場が実在する。ServerLevelだけでなくserver objectも替わる経路であり、「同一server objectのworldだけ交換」まで証明するものではない。
- [login sync compat](../build/verification/endinglib-login-compat-20260921-155452/patch/src/compat/ellogin/LoginSync.java)はlogin時のlocal変数から現在のnative Setを本人へ送るだけ。GatePluginのactiveはloader profileキャッシュ。[shader compat source](../build/verification/fe-uniform-compat-20260921-125321/patch/src/compat/feuniform/)はuniform生成契約のみ。どちらもorigin/Grant復元経路ではない。

**EndingLibrary2.1.19fixの保存3要素は同じ寿命ではない。**

| 要素 | 実経路と判明したこと | 読込後に断定しないこと |
|---|---|---|
| source count | attach時にentity refを設定→Forge CapabilityDispatcher→EntityのForgeCaps→ending_library:endinglib_living_cap→TimeStopCount。customSerializeNBTは非0ならintを書込む。EntitySyncCapabilityBaseがcustom保存を呼ぶ。customDeserializeNBTはintをfieldへ直接代入し、count>0からnative canMoveを設定する。native use(true)は呼ばず、帰属台帳も作らない | 正のcountは「保存されていたnative値」であって、Food Healingが開始を観測した証明ではない。custom syncData/readSyncDataは空で、countのNBT復元とcanMoveのSynchedCapabilityData/network経路を同一視しない |
| global | TimeStopUtils.isTimeStopはstatic volatile boolean。明示初期化のないJVM初期値false。native use、除去、level tick等がlive値を変更する。NBT serializerなし | 新JVM初期falseを、world読込完了後の実値としない。同JVMではstaticが存続し得る。除去時などのnative書込も観測が必要 |
| dimension Set | TimeStopSavedData constructorは空Set。readOrCreate(server)はoverworld DimensionDataStorageのtime_stop_saved_dataをload/生成。create(tag)はDimensions内のidを読む。ServerExpandedContextがserverごとにlazy cacheする。saveはListTagを作るが戻りtagへputしない。SavedDataの通常保存はその戻り値をdata内へ格納するため、新しい通常保存ではdataにDimensionsが出力されない | 空の保存tagだけで次のlive Setを断定しない。同じserverのcache、別serverでのload、途中のnative use、client同期は別経路。lazy getterの初回呼出自体がload/cache生成であり、純粋なfield snapshotとは区別する |

sourceの通常保存先は当該dimensionのentity region内NBT、playerはplayerdata内NBT、SavedDataはoverworldのdata/time_stop_saved_data.dat。最終保存に対象UUIDが実在することはA1の実測条件とし、未実行のentity保存成功を先取りしない。UOMの独自goal残時間等も通常entity保存され得るが、それをorigin/epoch再構築の根拠にしない。

確認したload経路に「positive countを走査してglobal/Setと既知originを再構築する処理」はない。ServerLevelExpandedContext.tickHeadは既にglobal/当該Setが停止中の場合に入り、周期条件下でsource不在を終了させる経路であり、cold loadの3値整合復元ではない。EntityMixin.setRemovedはnative use(false)等を呼び、player不在ではglobalをtrueへ戻す分岐もある。したがって正常stop時の保存/除去順序も記録し、最終disk countと次processの実値を測定する。保存前snapshotから最終保存内容を推定しない。

Forge DedicatedServerの実bytecodeではhandleServerAboutToStart→loadLevel、handleServerStartingはloadLevel後。次回の読込observerは前者のevent以前/内で配線・有効化を完了する候補。現driverの「actor生成後tick10でOwnership.enabled=true」をBへ丸ごと流用すると最初のdeserializeを見逃す。通常 /reload はPackRepository/WorldDataからMinecraftServerのresource reloadを呼ぶもので、ServerLevel再生成の試験入口ではない。確認できた同JVMの自然入口は前述のintegrated通常退出/再入場であり、dedicatedの架空reload APIやforgetForReloadの呼出しで代用しない。

#### 14.30.2 persistent / transient 23項目

「A後」は完全に新しいJVM、「B後」は同JVMのnative world/server交換。非永続判定は上記serializer経路および候補source全体の書込/呼出しから行い、保存ファイルの文字列検索だけに依存しない。audit/receiptは測定記録でありauthorityの復元入力にしない。「破棄」は旧contextでの認可利用を失効させる必要性を表し、nativeデータをFood Healingが消去する許可ではない。

| state | owner/class | storage location | NBT / SavedData / network | A後 | B後 | same UUID / new object | 正常復元すべきか | 必ず破棄/失効すべきか | 不明時の安全動作 |
|---|---|---|---|---|---|---|---|---|---|
| 1 source count | EL LivingCapability | entity cap field | ForgeCaps NBTあり。canMove同期は別 | tagからnative復元 | 新capはload、旧cap ref残留と区別 | 値は復元可、旧帰属ではない | native責任の保存値は尊重 | FHによる値消去は禁止 | count/帰属不整合はUNKNOWN・追加許可なし |
| 2 global flag | TimeStopUtils | static volatile | 永続なし、native Skill packet経路あり | JVM初期false→native推移 | static残留し得る | UUIDと無関係 | 値の手動復元なし | FHによる強制resetなし | Set/countと別に実測、矛盾はUNKNOWN |
| 3 dimension Set | TimeStopSavedData / ServerExpandedContext | per-server cache＋overworld storage | SavedData。ただしDimensions put欠落。native Dimension packet | 新cache/load | 同server cacheは残り得る。新serverは別cache | dimension名一致だけで同contextとしない | native loadを尊重 | 旧cache参照を新worldの認可へ転用しない | cache未生成/未観測は不明、空と断定しない |
| 4 UOM UUID/object/type/dimension | native Entity / ServerLevel | entity field＋登録/level | UUID/type/Pos等は通常NBT、runtime entity IDは別 | 新objectをnative load | 新object/new levelか実ref照合 | UUID/type一致≠ref同一 | UUID等のnative値のみ | 旧objectの認可利用不可 | 未load/重複/異refは許可しない |
| 5 P/M取得/toggle/SP | ShokugiData | player cap | ForgeCaps canonical＋ShokugiSyncPacket | 正常load/sync | 正常load/sync | 同UUID playerへ進行のみ復元 | はい、正常canonicalのみ | 進行/SPは破棄しない | malformed/pendingを修復せず既存保護 |
| 6 origin ledger | Ownership.State.origins | static states、dimension文字列key | なし。journal出力のみ | 空 | static mapが残り得る | UUID keyだけでは旧originが残る危険。binding AND必須 | いいえ | 旧originの再認可不可 | deserialize/type/P ONだけでknownへ昇格しない |
| 7 OriginBinding | TerminalWitness.engine.bindings | static engine内IdentityHashMap | なし | 空 | 強参照が残り得る | source/level/server/thread ref＋epoch一致が必要 | いいえ | 旧scope binding失効必須 | 欠落/別refはUNKNOWN |
| 8 CallbackFrame | engine.frames / stack | source ref→frame、call stack/token | なし | 空 | 終了欠落時に残り得る | UUIDによる再関連付け不可 | いいえ | 旧callback利用不可 | pending/欠落はfault/DENY |
| 9 TerminalWitness | engine.witnesses | source ref→frame、一回消費 | なし | 空 | static engineに残る可能性 | ref/context/token完全一致が必要 | いいえ | 未消費でも新contextへ持越し不可 | 新worldで消費しない |
| 10 terminalFault | static engine | engine共通latch＋reason | なし | 初期false、その後native観測から新fault | world/server横断で残り得る | UUIDと無関係 | 旧faultを別scopeへ復元しない | 同scopeでは勝手にclear不可。別scope隔離は未実装 | 当該scope DENY、無条件resetで回避しない |
| 11 UNKNOWN | Ownership.State | static dimension state、初期true | lease DTOに値を送るが永続なし | state生成時true | 旧state残留し得る | UUID一致でclear不可 | 旧true/falseを権威として復元しない | 旧knownを引継がない | native不一致/観測欠落/loaded positive無帰属→UNKNOWN |
| 12 quiet / processing | State.quiet、Ownership.calls、engine.stack | static state/deque | なし | false/空 | 残留し得る | 同UUIDと無関係 | いいえ | 旧quietや途中stackを新scopeへ引継がない | faultなしの実quietのみ。欠落はDENY |
| 13 epoch | Ownership.nextEpoch / State.epoch | static long / state long | lease DTOに番号、永続なし | 0。後で数値が再使用され得る | counter/stateが残り得る | 数値一致≠同epoch authority | 旧epochを無条件再開しない | stale epochを認可根拠にしない | fresh observed start・current scopeなしなら不可 |
| 14 Grant | SessionRegistry.Grant | grants IdentityHashMap、issued history | object保存なし。lease DTOは別物 | 空 | revoke後もissuedに旧objectが残る | current session/ref/map/epochのAND | いいえ | 旧Grant不可逆失効 | G1をUUID/NBTから再installしない |
| 15 sequence | SessionRegistry.sequences | static long | lease DTOのみ | 0から再使用可 | counter継続し得る | 数値一致に認可力なし | 旧番号から許可を復元しない | stale番号の認可利用不可 | current Grantなしでは拒否 |
| 16 server/player session | SessionRegistry | static active/grants/history/issued、serial | NBTなし、番号はDTO | 全map/list空、serial0 | history強参照等が残る。全world shutdown cleanupなし | loginで新Session。既存Sessionはserver/level実refを直接保持せずdimension文字列 | いいえ | 旧session認可不可 | current refsを要求。server世代境界は未証明 |
| 17 ServerPlayer ref | Session.player / active | object ref | 進行NBTは別、ref永続なし | 新object | 強参照残留可能 | UUID同一でも別ref | いいえ | 旧ref認可不可 | current PlayerList/memberとの対応確認 |
| 18 listener ref | Session.listener | object ref | 永続なし | 新object | history等の旧ref残留可能 | player.connectionとの==が必要 | いいえ | 旧listener不可 | 現接続と不一致なら拒否 |
| 19 Connection ref | Session.connection | object ref | 接続自体は永続なし | 新object | 旧closed ref残留可能 | listener.connectionとの== | いいえ | 旧Connection不可 | closed/違うrefは拒否 |
| 20 channel ref | Session.channel | Netty channel object | 永続なし | 新channel | 旧closed ref残留可能 | 同番号/hashではなく実ref | いいえ | 旧channel不可 | current connected channel不明なら拒否 |
| 21 ClientLease | ClientLease | client static lease/connection/listener/level/player refs・期限 | DTO受信のみ。NBTなし | clientも再起動なら空。serverだけ再起動してもclientは別寿命 | logout/login/clone/client unloadのresetとvalidのref検証あり | 旧leaseをnew refsへbind不可 | いいえ | 旧lease失効が必要 | mismatch/期限切れで不許可。dedicated A1ではclient未観測 |
| 22 login sync compat | LoginSync / LoginGatePlugin | handler local refs、plugin.activeのみcache | 元native Dimension packet。独自永続なし | loader gate再評価 | gate bool継続可、gameplay cacheなし | loginごとcurrent listener/playerを照合 | loader判定のみ可 | 古いplayer stateを転用する経路なし | profile不一致は適用なし、Grantとは無関係 |
| 23 shader compat | FeUniformGatePlugin / parser bridge | loader gate・shader/uniform object | gameplay NBT/Grant/ownershipなし | loader/shader再生成 | loader/描画object寿命は別 | player UUIDに依存しない | 描画資源の通常lifecycleのみ | gameplay権威として使うものなし | shader stateをTimeStop認可の根拠にしない |

ClientLeaseの実client根拠は[§14.24で使ったreconnect source](../build/verification/uom-reconnect-20260921-162556/helper/src/main/java/verification/uom/ClientLease.java)と既存R結果。D1-W内のcopyはdedicatedではclient subscriberを実証していないため、同copyの存在からclientの再起動PASSを作らない。

#### 14.30.3 cold-start / UNKNOWN / resurrectionの境界

**現候補と既存R/D1-Wが要求する安全条件（今回の新gameplay仕様LOCKではない）**：

- objectの認可contextはUUIDやruntime entity IDだけではない。process、MinecraftServer ref、ServerLevel ref、source ref、thread、epoch、現Session/player/listener/Connection/channelを区別する。TerminalWitness.sameはsource/level/server/threadを==で照合する。SessionRegistry側の現チェックに明示server世代tokenはないため、同JVM全体の安全性を補完済みとは言わない。
- player canonicalの取得/ON/SPからGrantを再構築するserializer/decoderはない。native cap loadもoriginを作らない。完全JVM交換では旧Java objectへ参照できない一方、同JVMのstates/history/issued/static engineは残留可能。既存ref guardは誤bindを拒否するが、world lifecycle全体のcleanupまで検証済みではない。
- epoch/sequence/sessionの番号はprocessごとのlongであり、別processで同じ数値になり得る。数値衝突それ自体をFAILにせず、番号だけで旧G1がnew playerへ再install/認可されることをFAILにする。process A/B間のidentityHashCode差やPID番号差だけをnew object証明に使わない。PID＋開始時刻＋完全exit＋別bootのallocation観測を組合せ、同heap内だけ実==を使う。
- witnessはsourceごとのmapでsingle slotではないが、engineとterminalFaultはstaticでscope共通。fault()はwitnessesを消すがbindings/framesの全clearではない。さらにquiet reconciliationはengine.bindings全体をclearする。複数world/serverの隔離はD1-W有限契約/同worldのquiet5から証明できない。未消費witness/pending callbackの新world消費を許さず、旧faultを別worldへ持ち込む問題も別に残す。
- **現在のOwnership.deserializeはenabledかつserver LivingEntity capなら、tag/countに関係なくdeserialize-no-provenance faultを立てる。positiveだけを検査する実装ではない。** CountObserverはcustomDeserializeNBTのHEADで呼ぶ。Bではplayerの空count cap等でもfaultになり得る。該当event・tag・entity・時点を記録し、「復元されたpositive stopだけが原因」と誤記しない。quietになってもterminalFaultは解除しない。
- UNKNOWN開始条件：loaded positiveにorigin/bindingなし、global/Set不一致、source消失、callback欠落、deserialize fault等。world load完了、同UUID/UOM型、P/M ON、positive countだけでclearしない。source未loadを「sourceなし/quiet」と取り違えない。
- UNKNOWN解除は開始と別問題。faultなしなら既存reconcileの実quiet（native global/Set false、loaded positiveなし、callbackなし）→R7のclean境界→R8の新native開始という証拠がある。ただし候補のfault優先returnはquietでは解除しない。load由来faultをいつ/どのscopeで回復するかは未設計・未実装。新JVMで初期falseなのは旧fault解除操作ではなく新object生成である。
- **追加仕様/設計判断として残すもの**：停止途中のcold loadで保護継続をどこまで要求するか、その回復条件とserver/world scopeの失効方式、将来productionの世代識別。今回は安全側DENYを観測するための判断不要。UOM/P vehicle方針も未LOCKのままで、A1の前提にしない。旧Grant/originの永続化・UUIDで復元する案は採らない。機能継続FAILを通すためのUNKNOWN/fault resetは行わない。

#### 14.30.4 A/B比較と優先理由

A/Bは本節の2種類のreload。下表の「後続B/C/E/F」は§14.25のtransfer fault/observer loss、FE goal再延長、client input/render/correction、production mappingを指す。

| 比較点 | A：完全process交換 | B：同JVM native world/server交換 |
|---|---|---|
| 実runtime入口 | dedicated通常save/stop/exit→新Java起動→同じlevel-name/pathをload。明確 | 実clientの通常Save & Quit→menu→同world再入場。明確。dedicated world-only交換入口は未特定 |
| 証明対象 | diskからnative値だけ復元された時、旧authorityを再生しない | classloader/static/ref残留下でcontextを隔離できるか |
| old ref | A終了でheapが別。監査上の文字列IDは旧refではない | engine/history/cacheが旧server/level/playerを強参照し得る |
| static state | JVM初期化で空/0/false | 残留。明示cleanup/世代scopeは未証明 |
| native保存 | count NBT、SavedData実出力、load後global/Setを個別測定 | 同じ保存＋static global/古いserver cacheの影響も重なる |
| Grant非復活 | serializer/importなし・B初期map空・自動発行0・permission/matchesを観測 | 同heap旧G1を保持したままnew refsへ誤認可しないことまで実比較可能 |
| origin非復活 | 正のnative count復元でもledger/binding空を実測 | UUID map残留とnew ref bindingの拒否を実測する必要 |
| UNKNOWN評価 | native loadと候補faultの因果を分離しやすい | global/cache残留・client共有staticも分離が必要 |
| helper | phase別driver＋読込前observer。coreは変えない | integrated lifecycle/old refsを追う別driverが必要 |
| dedicatedだけ | 可。既存EmbeddedChannel/native PlayerList方式 | 確認済み自然入口では不可 |
| real client | 不要。ClientLease継続は証明対象外 | 必要。通常menu/reopen経路 |
| run/process/world | 1新run・2 phase/2 JVM・新world1・通常再読込1 | 将来別run・client JVM1内の2 server session、world1。今回選ばない |
| artifact追加 | 外部不足なし。検証専用helperは未作成 | 既存外部物あり。検証専用helperは別途必要 |
| 仕様判断 | 非復活/安全側観測には不要。継続回復方針は別 | scope設計を勝手に実装しない。vehicle判断は別 |
| 後続B/C/E/Fへの価値 | native永続と候補非永続の基準を先に確定。fault loss/再延長/production境界の上流 | Aを基準にstatic残留だけを絞れる。client lifecycle/productionの次段階 |

**A1を先にする理由**：単に容易だからではなく、読み戻されたnative停止値にauthorityの出所がないという上流条件を、static残留やclient描画から切り離して反証可能にできるため。同じ情報はdedicatedで取得できる。Aの結果だけでBのstatic隔離が成功したとはしない。

#### 14.30.5 推奨1単位 A1 — 次回承認候補、今回未実行

**対象/規模**：新verification rootの1 run、dedicated JVM A/B計2、Aで新規生成する使い捨てworld1、その同じ保存worldをBで通常再読込1回。過去world/runを使わない。AUTOMATEDのみ、real client/Prism/HUMAN入力/認証/TCP確認なし。目的はOLD AUTHORITY NON-RESURRECTIONの限定確認であり、reload後の保護継続やfresh E2/G2発行を試験しない。

**artifact**：MC1.20.1/Forge47.4.0/local Java17、Food Healing229,494 bytes/150 entries/SHA256 5C1A716EEDE7AEB4449B36EEFFBDDA156176B16B60A1676229224AA3390DB327、Fantasy Ending2.7.20（E32FD4BA…E5D141）、EndingLibrary2.1.19fix（0E29AF51…CDC34）、既存Iron's Spells3.16.3/Curios5.14.1+1.20.1/GeckoLib4.8.2/IronLib2.1.0と承認済み同梱物。外部原物は[既存artifact配置](../build/verification/fantasy-ending-20260920-204205/audit/artifacts/)と§14.7–14.10の完全hash/版を使用し、新download/原Jar変更0。login compat/shaderは不変を照合するが、A1でclient同期/描画を再証明せず、dedicatedに不要なclient patchを新規追加しない。D1-W core参照は§14.29 helper77,577 bytes/33 entries/hash57A4EFE0BA98E92022188D2D6245607F0E2449B4FC23051D96DF7093B74FBF05。旧helperは変更/起動しない。

**次回だけ作る補助**：新root内のReloadVerification.java（A/B phase制御、one-shot準備/通常入力）、ReloadReadonlyObserver.java（class初期値・native serialize/deserialize HEAD/RETURN・load/stop snapshot）、必要な観測Mixin、専用Gradle init、run-two-phase.ps1（offline限定compile/reobf・通常console保存/停止・PID/readonly NBT/hash照合）を予定。既存Ownership/SessionRegistry/TerminalWitness/Bridge/MovementGuardの認可・UNKNOWN・fault・epoch・sequenceロジックはそのまま。旧driver/observerへの参照配線だけを新driverへ移す。通常test/build.gradle/製品Jarを巻き込まないtask graphと非混入を次回開始前に確認する。既存95/133等fixtureを持ち込まない。

- run realpath・world realpath・phase A/B・本人UUID・source UUID・対象全Jar hashを限定。first-write receiptは新runに作り、A準備/開始/保存移行、B初回load、結果/終了を別append-only記録にする。phase BはA終了と保存digest照合後に一度だけ許可。receiptはguard/監査入力であってGrant/originのimportデータではない。
- A/BともServerAboutToStartEventで、loadLevel前のhook存在と候補enabled有効化を記録する。これは検証監視の配線でありnative値を変更しない。初回deserializeより後から観測を有効にして成功を装わない。native getter初回のcache生成はnative load eventとして記録し、readonly snapshot内でquiet/fault/mapを変更しない。
- 初期snapshotではclass生成の初期値を記録：states/calls/journal、engine bindings/frames/witnesses/stack、SessionRegistry active/grants/history/issuedは空、nextEpoch/serial/sequences/token/counters0、terminalFault=false。State未生成はABSENTと表示し、値を取るためにcomputeIfAbsentしない。通常処理でStateができたらepoch0/UNKNOWNtrue/quietfalse。native global初期false、SavedData cache未生成はUNINITIALIZEDであってSet空ではない。
- 独立readonly observerと、候補reconcile/session評価という一時状態を更新する処理を別ログにする。observerからknown/quiet/UNKNOWN/fault/Grant/外部stateを書き換えない。旧forgetForReload、全map.clear、SessionRegistry.cleanBoundaryのB load時呼出し、B prepare/traitや取得再付与は禁止。

| 段階 | 一度だけ行う入力/通常経路 | 必須snapshot・判定境界 |
|---|---|---|
| A0 fresh準備 | 生成前/初回測定前にdoMobSpawning=false、naturalRegeneration=false、Normal/Survival。閉じた安全区画、sourceと本人を隔離。既存native PlayerList＋EmbeddedChannel方式の対象本人1、NoAI/PersistenceRequiredのUOM1（fantasy_ending:ultimate_order_manager）。Aのみ安全な初期配置 | native count0/globalfalse/Set空、source ref/UUID/type/cap/level/alive一致、他positive0、HP20/MAX20/effects空、防御/移行pendingなし。正常fresh本人へP/M各Lv1/ON・credit/spent103/SP0をprepare1回。T不要。通常購入PASSではない。既存clean5 END後にcurrent sessionを確立 |
| A1 有効な開始状態 | TimeStopUtils.use(true,UOM,true,180,false)を1回。実native use/数tickのcountdownだけを通す | observed origin1/binding1、native count>0/global・当該Set true、UNKNOWNfalse、faultfalse、current G1 issue1・matches/permission true、epoch/sequenceとcurrent ref一式。frames/witnesses/stackはEND時0。これはcold load前提の成立でありD1-Wやplayer17再PASSではない |
| A2 通常保存/終了 | A1が成立した直後に通常save-all flush→stop。sourceへのnative end/cleanupを先に呼ばない。標準command dispatcher/consoleを使い、停止countをsetter/時間停止補正で保たない | 保存直前、native serializer、通常logout/revoke、server stop/level close、exit0/PID終了を順序記録。AのGrant失効とnative除去時書込は隠さない。出力確定後にのみoffline NBTを読む |
| A3 disk gate | worldを変更せず対象entity/player/SavedDataを読み、B前の保存snapshot/hashを監査へ保全 | 対象UOM同UUID/typeの最終保存でTimeStopCount>0、NoAI等と本人canonicalを確認。SavedData data/Dimensions有無を実測。対象なし/count0/保存失敗なら停止途中入力未成立＝UNVERIFIED、Bへ進まず修復/再試行しない |
| B0 新JVM/load | Aのexit・ファイル閉鎖後、同じworld実パスを別Java processで通常起動。新しい補助heap初期値を先に観測。native loaderによるUOM cap deserialize/EntityJoinを通す | PID＋開始時刻/boot label/classloader/server/level/object識別。deserialize HEADのtag値、RETURNのcount/native canMove、global/Set/cache生成とsource loaded状態を別々に記録。Aのrecordをsource/Grantへinstallしない |
| B1 本人/authority評価 | 同じ本人UUIDを新Connection/EmbeddedChannel/native PlayerListで通常login。playerdataの通常load/syncのみ、prepare0/新use(true)0/Grant issue0。sourceはAのspawn周辺で通常chunk loadさせ、再生成しない | 同UUID、新ServerPlayer/listener/Connection/channel、正常P/M1ON/SP0・103。loaded sourceは同UUID/typeだが新cap/entity/level/server ref。candidate reconcile/permission・current Grant/matches・native movement条件を5連続END記録。origin/binding/旧Grant/witness/frame復元0、epoch0、sequence0、自動ALLOW0 |
| B2 結果/cleanup/終了 | 本質結果を先に確定しseal。必要時だけ同sourceへnative use(false) cleanup1回を別記録。player通常remove→save-all flush→stop→exit0 | cleanup前後count/global/Set、fault/UNKNOWNを修復しない。保存/全dimension終了・process不在、最終player/source/SavedData readonly照合。B後再読込0、製品/原物/既存補助hash不変 |

180はこの検証だけのnative入力長で、停止時間の製品仕様を変更しない。Aの通常保存/停止は速やかに自動化し、count自然終了を遅らせるための更新禁止/再延長はしない。保存時点と終了後NBTが異なることもあるため、A3が唯一のdisk入力gate。source未load/観測漏れをcount0に置換しない。B1の5 ENDは「loaded sourceと本人が揃った後」だが、先行deserialize/最初のtickからの連続記録も必須。そこでpositive復元を捉えられなければUNVERIFIED。

**identity/旧認可の観測**：A/Bの比較用UUID・数値・hashは監査記録のみ。別heapに旧G1 Java objectを再生成して「旧G1」と呼ばない。B初期map空と実serializer/import不在、通常load後のorigin/binding/Grantなし、issue呼出0、current predicate非許可で限定非復活を判定する。数値session1がA/Bで重なってもよい。Bで旧session/object/refを使った処理0、旧pending/witness消費0、UUIDだけのknown昇格0、stale epoch/sequence認可0をログ＋call pathで照合する。actual replay packet/old DTO送信やclient lease残留はこの単位では未実施。

**native復元結果の分類（期待値を先に作らない）**：

| Bの実値 | 候補authorityの期待 | 意味 |
|---|---|---|
| positive count、global/Setとも停止 | origin/bindingなし、UNKNOWN/DENY、G1なし | native停止復元が成立しても出所を復元しない。安全側だが保護継続は未完成 |
| positive count、global/Setとも非停止 | UNKNOWN、追加許可なし | native3値の不整合。global/Setを補正しない |
| globalと当該Setが不一致、または停止なのにsource未確定 | UNKNOWN、追加許可なし | 同期/load/観測の不一致。自動known化しない |
| count0/非停止までnativeで推移 | Grant0を維持。faultがあればUNKNOWNのまま | 最初のpositive復元証拠と自然推移を分離。未観測のpositiveを推定しない |
| source未load/複数同UUID/必要snapshot欠落 | 判定入力なし、追加許可なし | UNVERIFIED/STOP。空mapを安全なnative quietとみなさない |

ここでDENYはFood Healing候補による**追加TimeStop保護を認可しない**こと。nativeが非停止または元canMove=trueのため通常移動を通すことまでSAFETY FAILとしない。MovementGuard.denyのbooleanだけでは判定せず、stopped/nativeCan/permission/current Grant/matchesを分離する。native不整合による停止挙動の変化を製品の保護成功へ転記しない。

#### 14.30.6 判定・終了・未証明範囲

| 判定 | A1の条件 |
|---|---|
| LIMITED PASS：旧authority非復活 | Aのvalid G1成立と最終disk positive、完全exit→Bのnative positive deserializeが実証され、旧Grant/origin/session/ref/Connection/listener/channel/witness/frame/pending callbackの復元・利用0、UUIDだけの昇格0、stale番号認可0、reload自動ALLOW0。全入力/observer/hash・正常保存終了が揃う。native3値は実値分類で付記 |
| SAFE-SIDE DENY | native停止に対しprovenanceが失われUNKNOWN/permissionfalse/Grantなし。誤ALLOWのFAILではない。上の限定非復活PASSと併記可能だが、機能継続PASSやSAFE DESIGNへ昇格しない。empty cap由来deserialize faultも原因を別記 |
| SAFETY FAIL | 旧認可のnew context利用、UUID/count/P ONのみでknown/Grant再生、stale session/epoch/sequenceでpermissiontrue、未消費witnessのload後消費、old listener/channelへ再関連付け。最初の発生で測定を止める |
| UNVERIFIED | Aの有効G1未成立、最終disk count0/対象不在、native load未到達、早期observer欠落、Bまでに追えないcount変化、保存/hash/process終了未確認。安全側拒否を観測しただけでinput成立を補完しない |
| STOP | production/LOCK/authority/UNKNOWN/session/protocol変更が必要、新artifact/本人権限が必要、保存破損・native本質反例、helperだけで安全に観測不能。count/global/Setの予測不一致だけでは勝手に修復せず上の分類で記録。phase Bを開始できないA3不成立もSTOP |

startupの上限は各process180秒、actor/読込対応付けはserver ready後60秒、測定は開始後600 ENDまたは60秒の先着、通常save/stop/exit待ちは60秒。timeoutでreceiptを消した再送やA/Bの追加runはしない。正常停止を優先し、hangで止まれなければ「正常終了未確認」と具体的なPID/状態を報告して停止する（自動強制kill・保存修復なし）。

A2は停止途中を保存するため、意図的に測定sourceの事前cleanupをしない。A失敗でBを始めない場合、まだserverが稼働し操作可能なら結果保全後だけnative終了→通常保存/stopを行い、cleanup後保存をmid-stop証拠に使わない。既に終了したAをcleanupのため再起動しない。Bのcleanupも本質観測と区別し、native count/global/Set/canMove/NBT・HPをdirect setterやtag編集で一致させない。観測/guardは読み取り、candidate一時stateの通常更新と準備1回だけが補助側書込。製品/外部原Jar・Config・gate変更0。

成功しても、同JVM static漏れ/別worldへのfault隔離、停止途中の継続保護・fault回復、transfer fault/observer loss、FE TimeStopSkillGoal再延長、逆順/同tick/3+source/多dimension/performance、client継続processのClientLease/input/render/correction、production mapping、vehicle方針は未証明。process crash/強制終了・terminal callbackの途中killは通常stopとは別で未試験。D1-WやRの既存PASSは保持し、再試験しない。

**次回の承認対象はA1のみ**：新root内のphase-aware補助/readonly observer・専用offline compile/reobf/非混入確認→新dedicated/worldでA準備1回/native start1回/通常保存終了→最終保存gate→別JVM Bで同world通常load1回・認可非復活観測→結果/seal/必要なnative cleanup/通常保存stop/exit照合→3文書更新。今回その承認・実行は行っていない。追加外部artifact不足なし。SAFE DESIGN PROVEN=NO / SOURCE DIMENSION TRANSITION VERIFIED=NO / BLOCKED - TIME STOP SOURCE OWNERSHIP、FE6 MobEffect/TimeStop production NOT IMPLEMENTED、P/T IMPLEMENTATION_PENDING・購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、vehicle未LOCKと他個別開始条件を維持。食料生産の極意の可逆クラフト増加は既知許容仕様・バグ修正対象外。


<a id="uom-process-reload-a1-result"></a>
### 14.31 A1 別JVM 2-phase — OLD AUTHORITY NON-RESURRECTION LIMITED PASS（2026-09-22 15:23 JST）

**OLD AUTHORITY NON-RESURRECTION LIMITED PASS ＋ SAFE-SIDE DENY**。新run **20260922-145728**、新world1・専用JVM A/B各1、同保存world通常load1だけ。AUTOMATED / dedicated / EmbeddedChannelであり、real client・HUMAN・実認証TCP・same-JVM reloadではない。[独立照合](../build/verification/uom-process-reload-20260922-145728/audit/reviewed-results.json)。§14.30 DESIGN ONLYは当時の設計履歴として全文保持し、A1の未実行/承認待ちはこの結果で解消。D1-W STATIC/有限10/W1–W4・R1–R9・他既存suiteの再実行0。

#### 構成・差分境界

- MC1.20.1 / Forge47.4.0 / Java17.0.7、実ロードFE2.7.20、EndingLibrary2.1.19fix、Iron's Spells1.20.1-3.16.3、Curios5.14.1+1.20.1、GeckoLib4.8.2、Iron's Lib1.20.1-2.1.0。nested PlayerAnimator1.0.2-rc1+1.20 / MixinExtras0.4.1をloader記録と照合。承認6原物＋製品＋新helperの8外側Jar、nested重複配置/新downloadなし。[入力hash](../build/verification/uom-process-reload-20260922-145728/server/inputs.json) / [実ロードlog](../build/verification/uom-process-reload-20260922-145728/PhaseB/logs/debug.log)。公開鍵要求はloopbackの検証用空応答、外部HTTP(S)は接続不可proxy。実認証成功の証拠ではない。
- 新helper `uom_reload_verification` **20260922.145728、78,631 bytes /37 entries、SHA-256 AABF0A658B33DD6A89EA92D51DF2112E47099B932C1B95FF8931B2AE3941464B**。[差分STATIC](../build/verification/uom-process-reload-20260922-145728/audit/static-gate.json)。§14.29のOwnership/SessionRegistry/TerminalWitness/Bridge/MovementGuard等8core source byte一致、16core classは13byte一致＋3はRUN文字列差のみ、refmap一致。新phase driver・readonly cap/SavedData observer・run配線だけを追加/変更。classesはverification/uomのみ、製品/FE/EL/ExampleMod混入0。
- local Gradle8.1.1・既存cache・offlineの補助専用compile/reobf成功。[task graph](../build/verification/uom-process-reload-20260922-145728/audit/task-graph.txt) / [最終compile](../build/verification/uom-process-reload-20260922-145728/audit/compile-02.log)。製品compileJava/jar/reobfJar/build/check/unit/GameTestなし。測定前**repair1**：新driver2箇所のLazyOptional.orElseThrowへsupplier追加、[初回FAIL](../build/verification/uom-process-reload-20260922-145728/audit/compile-01.log)も保持。最初のsandbox native DLLアクセス失敗は承認済み権限付きlocal実行で解消し、外部取得なし。測定開始後のhelper変更/再試験0。
- Aで生成時doMobSpawning=false/naturalRegeneration=false、Survival/Normal、閉鎖区画、fresh HP20/MAX20/effects空を確認。prepare1回だけP/M各Lv1 ON、credit/spent103・SP0、Truthなし。通常GUI購入成功ではない。UOM1体をNoAI/PersistenceRequiredで配置し、native stop前5連続ENDのcount0/globalfalse/Set空、origin/binding/frame/witness/stack0・faultfalseを確認。[baseline](../build/verification/uom-process-reload-20260922-145728/PhaseA/audit/baseline.jsonl)。

#### A → disk gate → B の実測

| 段階 | 実値・判定 | 主な証拠 |
|---|---|---|
| JVM A | PID **12088**、15:08:09.830486開始→15:08:32.602983終了JST、exit0。UOM native use(true, reset=true,180,false)1回、prepare1 | [process](../build/verification/uom-process-reload-20260922-145728/PhaseA/audit/process.json)、[入力](../build/verification/uom-process-reload-20260922-145728/PhaseA/audit/native-input.jsonl) |
| A保存直前 | origin1/binding1/G1発行1、epoch1/sequence1/session1、matches/permission=true、UNKNOWN/fault=false、frame/witness/stack0。count**179**/global=true/Set={minecraft:overworld} | [正本snapshot](../build/verification/uom-process-reload-20260922-145728/PhaseA/audit/A-PRE-SAVE.json) |
| A通常終了の変化 | helperの事前cleanup **0**。UOM serialize179の後、native shutdown自身がuse(false,duration180)/count179→0。player通常removeもcount0→0を通知し、候補faultが生じた。旧sessionはlogoutで失効。これらを隠さず、最終disk値とは分離 | [cap serialize](../build/verification/uom-process-reload-20260922-145728/PhaseA/audit/native-cap.jsonl)、[count](../build/verification/uom-process-reload-20260922-145728/PhaseA/audit/native-count.jsonl)、[lifecycle](../build/verification/uom-process-reload-20260922-145728/PhaseA/audit/lifecycle.jsonl) |
| A3 disk gate | A exit/PID終了・file close後、同UUID/typeのentity1、ForgeCaps/ending_library:endinglib_living_cap/**TimeStopCount179**。P/M1ON・SP0/103・HP20・effects空。SavedData実fileは **{data:{},DataVersion:3465}、Dimensions keyなし**。gate成立後だけB開始 | [disk gate A](../build/verification/uom-process-reload-20260922-145728/audit/disk-gate-A.json)、[file close](../build/verification/uom-process-reload-20260922-145728/audit/A-files-closed.json) |
| JVM B初期値 | PID **28220**、15:09:46.501057開始JST。旧state/calls/journal/origin binding/frame/witness/stack/session/grant/history/issued/epoch/sequenceは空/0、fault=false。native cacheは空Setではなく**UNINITIALIZED** | [heap初期](../build/verification/uom-process-reload-20260922-145728/PhaseB/audit/HEAP-INITIAL.json)、[load前arm](../build/verification/uom-process-reload-20260922-145728/PhaseB/audit/OBSERVER-ARMED-BEFORE-LOAD.json) |
| B native deserialize | UOM HEAD入力tag179・live count0→RETURN count**179**、native source canMove=true。既存candidate HEAD注入でfaultEvents1。次にplayer空tag{}・count0がdeserializeされfaultEvents2。passive HEADは既存fault注入後/native本体前であり、fault前を観測したとはしない | [実deserialize](../build/verification/uom-process-reload-20260922-145728/PhaseB/audit/native-cap.jsonl) |
| B loaded後5連続END | ticks2–6、count **178/177/176/175/174**。global=false、cache PRESENT・live Set空、dimensionInSet=false。origin/binding/Grant0、epoch0/sequence0、UNKNOWN=true/fault=true、matches/permission=false。native player canMove=false、MovementGuard.deny=false | [readonly観測](../build/verification/uom-process-reload-20260922-145728/PhaseB/audit/observations.jsonl)、[candidate評価](../build/verification/uom-process-reload-20260922-145728/PhaseB/audit/evaluations.jsonl) |
| B終了 | 結果/seal後だけnative cleanup1、count174→0。fault/UNKNOWNをclearせずGrant0を保持。15:10:06.109707 JST exit0、通常全dimension保存・PID終了。最終disk同UOM count0、P/M1ON/SP0/103/HP20/effects空、SavedData data{}を照合 | [seal](../build/verification/uom-process-reload-20260922-145728/PhaseB/audit/SEALED.json)、[cleanup](../build/verification/uom-process-reload-20260922-145728/PhaseB/audit/CLEANUP-DONE.json)、[process](../build/verification/uom-process-reload-20260922-145728/PhaseB/audit/process.json)、[disk B](../build/verification/uom-process-reload-20260922-145728/audit/disk-gate-B.json) |

**native不整合と追加認可を分離**：Bは「count positive / global false / Set false」の実値であり、native停止全体が正常復元したとはしない。SavedDataはAのlive Setにoverworldがある時点でもserialize tag{}だったことを直接観測。BのSet空は実load/live照会で確認した値で、空fileからの推定ではない。guardDeny=falseはdimension非停止条件によるもので、Food Healing追加ALLOW成功でも、実client歩行PASSでもない。5 ENDのpermission=false/Grant0が今回の認可判定。停止途中の正常保護復帰は未証明。

#### 旧認可の非復活・identity

UOM UUID **8b998d22-62a4-4661-a179-ad9a50a6b637**、本人UUID **<PLAYER_UUID>**はA/B一致。A終了後に別PID/別heap Bを開始し、sourceはnative deserialize、本人は通常PlayerList loadで生成。B内のlookup/current source/cap/entity/level/serverおよびsessionのplayer/listener/Connection/channelは実参照の整合guardを通過。identityHashCodeはラベルに限り、cross-processの==証明に使わない。Bのsession番号1は新しいsessionであり、Aのsession1を復元したものではない。A receiptから読むのはsource UUIDの照合metadataだけでauthority importなし。

| Bにおける禁止事象 | 観測件数 |
|---|---:|
| old origin / OriginBinding / G1復元 | 各0 |
| old session・player/listener/Connection/channel参照利用 | 各0 |
| old witness / CallbackFrame / pending callback復元・利用 | 各0 |
| UUID-only known昇格、stale epoch/sequence認可 | 各0 |
| automatic Grant発行 / automatic追加ALLOW | 各0 |

別heap初期値・実deserialize・5連続END評価・core意味不変・authority importなしを合わせた限定判定であり、counter0だけを根拠にしない。Bにprepare/新native開始/再spawn/再grant/fault reset/全map.clear/NBT修復なし。saved player/source/SavedDataのauthorityキー確認は[両disk gate](../build/verification/uom-process-reload-20260922-145728/audit/disk-gate-B.json)の対象範囲に限定。

#### 保全・残件・次の1作業

両server通常save-all flush/stop・全5dimension保存・exit0/PID終了、[終了確認](../build/verification/uom-process-reload-20260922-145728/audit/process-final.json) / [B file close](../build/verification/uom-process-reload-20260922-145728/audit/B-files-closed.json)。Aの補助cleanup0、Bの結果後cleanup1を区別。3rd load0、実client/Prism起動0でQuit Gameは非該当。既存asset/tag/loot/Patreon接続失敗等のWARN/ERRORを原logへ保持し、全WARN解消とはしない。

Food Healing製品 **229,494 bytes/150 entries、SHA-256 5C1A716EEDE7AEB4449B36EEFFBDDA156176B16B60A1676229224AA3390DB327** 不変。source/test/build.gradle・製品/原Jar・compat/reconnect/D1-W/旧証拠を含む **2,029files hash一致、旧§14.29rootへの追加/削除0、配置8Jar同hash**。[保全](../build/verification/uom-process-reload-20260922-145728/audit/preservation-after.json)。変更は新verification rootと最後の3文書だけ、変更前3文書は[before/docs](../build/verification/uom-process-reload-20260922-145728/before/docs/)へ保全。独立集計時の「A use(false)全件0」という過剰な検査を、利用者§19どおり「補助cleanup0/native shutdown変化は記録」へ修正して照合した。runtime/helper/期待認可条件・保存値の変更/再実行ではない。

成功しても**same-JVM static残留・world/server/fault隔離、reload後保護復帰/UNKNOWN回復、crash/kill、observer loss/transfer fault、FE goal再延長、逆順/同tick/3+source/多dimension/性能、real client lease/input/render/correction、production mapping、vehicle方針**は未証明。SAFE DESIGN PROVEN=NO、SOURCE DIMENSION TRANSITION VERIFIED=NO、BLOCKED - TIME STOP SOURCE OWNERSHIP。FE6 MobEffect/TimeStop production NOT IMPLEMENTED、P/T IMPLEMENTATION_PENDING・購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、UOM/P vehicle未LOCKと全個別gateを維持。可逆クラフト増加は既知許容仕様・バグ修正対象外。

**次の1作業：別承認後、同JVM world/server交換におけるstatic authority・terminalFaultの寿命と隔離境界だけをREAD ONLYで整理し、最小検証1単位を設計する。** A1の別JVM非復活は完了済みで再試験しない。Aの通常終了時fault/残stateとBの新規deserialize faultを根拠に、旧authorityの漏れと安全側DENYの残留を分ける候補。UNKNOWN回復方式のLOCK・helper変更/新run・same-JVM実行・production mapping・購入解放は今回未承認で、ここでは開始しない。追加外部artifact不足なし、UOM/P vehicle方針未LOCK。

<a id="uom-same-jvm-context-design"></a>
### 14.32 Same-JVM server/world交換 — static寿命のREAD ONLY解析・SJ1設計（2026-09-22 15:51 JST）

**DESIGN ONLY / SJ1実行未承認・未実行**。今回はソース・既存bytecode・保存済み証拠の読取と最後の3文書更新だけ。新run/root/helper/compile/build/game/Prism/reloadは0。§14.31 A1の **OLD AUTHORITY NON-RESURRECTION LIMITED PASS ＋ SAFE-SIDE DENY**、D1-WとR1–R9の限定PASSを保持する。本節が現行の次候補であり、§14.29–14.31末尾の「次」は当時の履歴。

#### 14.32.1 根拠・実runtime経路

現行候補は[A1 helper source](../build/verification/uom-process-reload-20260922-145728/helper/src/main/java/verification/uom/)のOwnership / SessionRegistry / TerminalWitness / TerminalWitnessBridge / ClientLease / VerificationNetworkとdriverを読取。client配線は[実reconnect helper](../build/verification/uom-reconnect-20260921-162556/helper/src/main/java/verification/uom/)を参照した。変更・再compile・再試験はしていない。

Minecraftの名称と処理は既存[ mapped bytecode一式](../build/verification/uom-reconnect-plan-20260921-103304/audit/bytecode/)とローカル47.2.0 mapped sourcesで追い、対象47.4.0の既存client patched / client SRG / Forge universal Jarをjavapで追加照合した。47.2.0のコードだけを47.4.0の実行結果と扱わない。参照runtimeは既存[launcher libraries](../build/verification/direct-jar-20260913-114729/launcher/libraries/)。以下はSTATIC確認で、今回runtime実測ではない。

| 段階 | 実在する呼出し・順序 | 次回観測上の注意 |
|---|---|---|
| 通常Save & Quit | PauseScreen.onDisconnect → ClientLevel.disconnect → Connection.disconnectでchannel close → Minecraft.clearLevel(SAVING_LEVEL) → TitleScreen | 通常singleplayer退出にエラー用DisconnectedScreenは必須でない。予期しない切断ではClientPacketListener.onDisconnect側のDisconnectedScreen経路を別記録 |
| client解除 | clearLevelはpending taskを落としlistener.close、旧singleplayerServerをlocalへ退避してMinecraft側をnull化、Forge client LoggingOut、client LevelEvent.Unload、旧server.isShutdown待ち、handleClientLevelClosing、level/playerをnull化 | listener.close自体は主にclient level/telemetryの終了。network closeは先行するClientLevel.disconnect。client/server thread間の全イベント順序は固定と仮定しない |
| host切断 | server listener.onDisconnect → PlayerList.remove。先頭付近のPlayerLoggedOutEvent → player保存・UNLOADED_WITH_PLAYER等 → owner切断によるIntegratedServer.halt(false) | 同playerへのlogout/revoke、旧Connection/channelのcloseを実refで追う。fixtureによるPlayerList.remove直接呼出しで代用しない |
| server停止 | haltはrunning=false。runServerループ終了 → ServerStopping / expectServerStopped → finallyでstopped=true → stopServer：network stop、player保存/removeAll、saveAllChunks、各server LevelEvent.Unload → level/resources/storage close → handleServerStopped → onServerExit / thread終了 | **isShutdownが返すstopped=trueはstopServer完了より先**。title到達・isShutdownだけで保存完了と判断しない |
| Forge停止境界 | handleServerStoppedはServerStoppedEventをpostした後にcurrentServer=null / LogicalSidedProviderのserver参照解除 | Event内の旧currentServerだけを漏洩と判定しない。hook RETURNと実server thread終了を追加観測する |
| 通常同world open | GUIでWorldOpenFlows経由のMinecraft.doWorldLoad → MinecraftServer.spinのfactory → new IntegratedServer → 新Server Thread.start | spin内部ではconstructor完了後にthreadがstartする。constructor前/RETURN、ServerAboutToStartを別の観測点にできる |
| 初期化・world | IntegratedServer.initServer → handleServerAboutToStart → loadLevel / createLevelsでnew ServerLevel、LevelEvent.Load → handleServerStarting → ServerStarted | 架空のIntegratedServer.startServer APIを作らない。native entity deserializeはServerStartedより前にも生じる |
| 新接続・本人 | startMemoryChannel → Connection.connectToLocalServer、新client Connection/LocalChannelとserver child Connection → MemoryServerHandshakeListener / ServerLoginPacketListenerImpl → getPlayerForLoginのnew ServerPlayer → placeNewPlayerのnew ServerGamePacketListenerImpl・通常NBT load・LoggedIn | dedicatedのEmbeddedChannelを持ち込まない。実memory channelの両端は別object。player NBT loadとsource loadの順序は観測する |
| 新client context | ClientboundLoginの処理でnew ClientLevel、new LocalPlayer、LoggingIn等 | 同一UUID・同一dimension ID・同一保存worldでも旧server/level/player/listener/Connection/channelとは別context |

次回の同一性：PID＋process start time＋JVM起動識別子、Minecraft singleton、helper/EndingLibrary各ClassとClassLoaderの実参照が不変であること。旧新IntegratedServer/MinecraftServer、Server Thread、各ServerLevel、source/cap、ServerPlayer/server listener/server Connection/channel、ClientLevel/LocalPlayer/client listener/client Connection/channelは対応ごとに実参照比較。identityHashCodeはログラベルだけで、等値証明に使わない。

**EndingLibrary固有の注意**：実MinecraftMixin.runTick_modifyPartialはclient levelがnullのとき共有TimeStopUtils.isTimeStopとClientContext.isTimeStop_andSameDimensionをfalseへ戻す。same-JVMではこれがserver側から見えるglobalにも作用し得る。SavedDataは実serverのServerExpandedContextに属する別cacheである。A1の「serialize179→native shutdown use(false)→live0」やdisk data{}は履歴事実として保持し、integrated終了順・disk/live/global/Setを同じになると仮定しない。nativeの変更とhelperによる修復を区別する。

#### 14.32.2 Static寿命表（今回の結論はSTATIC RISK）

S=static、I*=static singleton/mapから保持されるinstance。ref欄の「間接」はentity→level→server等のstrong連鎖。exit/menu/loadは通常callback成立時を含む静的評価で、今回の実測ではない。UUID-only=「キー/保存identityがUUIDだけ」ならその範囲を明記。共通記号SR=SessionRegistry、TW=TerminalWitness、TB=TerminalWitnessBridge。

| # / state | owner/class | S / instance | key | old server/world直接ref | old entity/player ref | UUID-onlyか | 既存clear/remove callback | exitで確実消滅? | menu残留 | new load残留 | new context再利用条件 | fail-closed影響 | 具体的risk |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| 1 Ownership.states | Ownership | S map / I* State | dimension文字列だけ | 無 | 無 | 否・dimensionのみ | forgetForReloadは存在するがlifecycleから呼ばれない。quietは内容clearのみ | いいえ | あり | あり | 同じdimensionのstate(l) | unknown/epoch/reason持越し | **new ServerLevelに旧Stateを返せるSTATIC RISK** |
| 2 Ownership.calls | Ownership.Frame | S deque / I* | stack順・entity実ref | 間接 | LivingEntity強参照 | 否 | afterUseの対応pop | 正常対応時空、退出一括保証なし | 途中frameならあり | あり得る | peek/entity一致を使うcallback | mismatch→UNKNOWN | 古いframe/level保持・nested干渉 |
| 3 Ownership.journal | Ownership | S list | append順 | 現rowに無 | 現rowに無 | 否・値snapshot | lifecycle clearなし | いいえ | あり | あり | 証拠出力のみ、認可に読まない | 直接なし | 増加・旧新ログ混同。現在のrowは値/String/UUID等でlive refではない |
| 4 Ownership.nextEpoch | Ownership | S long | JVM共通 | 無 | 無 | 否 | clearなし | いいえ | あり | あり | 次のbeforeUse開始時にincrement | 連番継続自体はDENY根拠でない | 数字だけをgeneration扱いすると誤認可 |
| 5 Origin ledger | Ownership.State.origins | I* map | UUID→type文字列 | 無 | 無 | **source識別はUUID＋type、実refなし** | 正当新start/quietでclear。lifecycleはUNKNOWNにするだけ | 保証なし | あり得る | あり得る | 同dimensionの旧Stateを経由 | binding不一致はUNKNOWN | 同UUID再loadへの由来誤移植。別binding検査を省けない |
| 6 OriginBinding | TB.engine.bindings / TW.OriginBinding | I* IdentityHashMap | 実source ref | Identityがlevel/server/threadを直接保持 | source強参照 | 否 | 正常terminal commitでremove、quiet reconcileで全bindings.clear | 保証なし | あり得る | あり得る | exact Identity一致のみ | new refなら一致せずDENY/fault | strong保持・quietの全context一括clear・wrong fault |
| 7 CallbackFrame | TW.frames / CallbackFrame | I* map | source実ref | Identity内に有 | source/cap/binding | 否 | 対応exitでremove | 正常対応時空、stop一括なし | 途中ならあり | あり得る | exact frame/token/stack照合 | pendingでDENY | callback持越し・old cap保持 |
| 8 terminal witness | TW.witnesses | I* map | source実ref→同CallbackFrame | Identity内に有 | 有 | 否 | exitでremove、fault時clear（consumeはflagのみ） | 保証なし | あり得る | あり得る | exact source/level/server/thread/epoch/token | mismatch不消費/fault | 不消費でも旧ref保持、他context faultで消失 |
| 9 terminalFault / reason / faultEvents | TB.engine / TW | I* latch | engine全体1個 | 値自体に無 | 無 | 否 | **解除処理なし**。faultはtrue、初回reason維持、event数++ | いいえ | あり | あり | 全reconcileが同latchを見る | world跨ぎDENY | old latch残留とnew fault増分の混同・機能停止 |
| 10 callback stack | TW.stack | I* deque | CallbackFrame順 | frame経由 | 有 | 否 | 対応exit pop | 正常時空、stopclearなし | 途中ならあり | あり得る | top/token照合 | mismatch/fault | nested callbackとold contextの干渉 |
| 11 callback token counter | TW.tokens | I* long | engine共通 | 無 | 無 | 否 | clearなし | いいえ | あり | あり | 新enter increment | 値だけでは許可しない | 数字の再利用/epoch混同、generationではない |
| 12 SR.active | SessionRegistry | S IdentityHashMap | ServerPlayer実ref | player経由 | key/valueとも有 | 否 | 登録済logout→Ownership.lifecycle→revoke→remove | logout正常時remove。独立ServerStopped掃除なし | callback失敗ならあり | あり得る | new playerでは別key、loginはactive空を要求 | staleならlogin拒否 | false DENY・旧player保持、callback欠落 |
| 13 SR.grants | SessionRegistry | S IdentityHashMap | Session実ref | session→player経由 | 有 | 否 | revokeGrantでremove＋Grant.revoked=true | 正常logoutでactive分remove | 失敗時あり | あり得る | matchesはcurrent session＋全実ref＋epoch | revoked/mismatchで拒否 | map残留と失効済みを区別 |
| 14 SR.history | SessionRegistry | S list | append順 | 間接 | Session経由 | 否 | **clearなし** | いいえ | あり | あり | login時の旧新ref/UUID比較 | 同account以外はfixture guard停止 | 全旧player/listener/connection保持。診断履歴≠active認可 |
| 15 SR.issued | SessionRegistry | S list | append順 | 間接 | Grant→Session経由 | 否 | **clearなし**。grant失効flagは変化 | いいえ | あり | あり | 記録・個別old G照合、許可mapではない | revokedを尊重 | 旧G/全session強参照。size非0自体はFAILでない |
| 16 Session serial | SR.serial | S long | JVM共通 | 無 | 無 | 否 | clearなし | いいえ | あり | あり | new Sessionで++ | 直接なし | 番号だけでsession同一視しない |
| 17 Grant sequence | SR.sequences | S long | JVM共通 | 無 | 無 | 否 | clearなし | いいえ | あり | あり | issue時++ | 直接なし | epoch/sequenceだけの認可は不可 |
| 18 ServerPlayer refs | Session.player、旧driver | I* / S handle | Session/driver field | player経由 | **直接** | 否 | activeから除去してもhistory/issuedは保持 | いいえ | あり | あり | identityはcurrent player実refを要求 | mismatch拒否 | heap保持。driver保持と認可registry保持を区別 |
| 19 listener refs | Session.listener / ClientLease.listener | I* / S | session/client context | player/level経由 | 間接 | 否 | server履歴は残る。client resetでnull | server側いいえ | あり | あり | current listener実ref | mismatch拒否 | 旧listener→player→world強参照 |
| 20 Connection refs | Session.connection / ClientLease.connection | I* / S | endpoint実ref | listener経由 | 間接 | 否 | close≠参照解除。client reset、server履歴残留 | いいえ | あり | あり | connected＋current ref | closed/mismatch拒否 | 旧channel/listenerへの保持。closeだけで消滅としない |
| 21 channel refs | Session.channel / Connection | I* | Channel実ref | pipeline経由の可能性 | pipeline経由 | 否 | network close、履歴clearなし | いいえ | あり | あり | current channel==old検査 | closed/mismatch拒否 | closed channelもstrong保持 |
| 22 ClientLease | ClientLease | S DTO＋context refs | run/UUID/dim/session/epoch/revision＋実ref | ClientLevel直接 | LocalPlayer直接 | 否 | LoggingOut/In/Clone/client Unload/render mismatch→reset。valid/receiveの一部→clear | callback登録・到達次第 | clearだけならrefs残る | callback不足ならあり | 現Connection/listener/level/player全実ref一致・期限等 | old refsではinvalid、明示resetと別 | stale lease、登録漏れ、observerがclearして隠す危険 |
| 23 login sync gate/cache | LoginGatePlugin.active / LoginSync | **activeはplugin instance Boolean**、定数S | loader/artifact gate | gameplay refなし | 無 | 否 | loader寿命。LoginSyncはcallback localだけ | gateは残る | あり | あり | 同版で同native packet送信 | gate不適合ならpatch不活性 | gameplay authorityではない。native SavedData cacheはactual server側 |
| 24 shader gate/static | FeUniformGatePlugin.active / PatchContract | activeはplugin instance Boolean、契約定数S | loader/版/hash | gameplay refなし | 無 | 否 | resource/loader寿命 | gateは残る | あり | あり | shader作成時の型契約 | gameplay認可に影響しない | GL/resource寿命をauthority leakに混同しない |
| 25 terminal engine自身 | TB.engine | **S final singleton** | helper classloader1個 | 各map経由 | 各map経由 | 否 | engine交換/stop resetなし | いいえ | 同object | 同object | 全worldのcallback | 共通faultで全DENY | context未分離。object残留自体と誤用を分ける |
| 26 Ownership.enabled / network登録 | Ownership / VerificationNetwork | S flag / S channel | JVM・protocol | 登録自体は無 | 無 | 否 | A1はAboutでenabled=true。initはMOD constructor | 値/登録残る | あり | あり | Bで再init/false→trueしない | 誤った再armは証拠欠落 | Observer開始とcandidate有効化を同一視しない |

**重点結論**：Stateはserver/level refを持たず、state(l)はdimensionだけでcomputeIfAbsentする。したがって旧Stateのepoch/origins/UNKNOWNが残ったまま同IDのnew ServerLevelから取得され得る。これは**STATIC RISK**でありsame-JVM実測済みとは書かない。terminal binding/witnessのIdentityはsource実ref・UUID/type・level/server/thread実ref・epochを照合するので、same UUIDだけでは旧witnessを通常consumeできない。しかしbindings/frames/stackのstrong保持、faultによるwitnessのみclear、quiet時の全bindings.clear、engine全体のlatchが残るため、memory保持・false DENY・wrong fault・context干渉は別残件。

#### 14.32.3 ClientLease・session境界と観測callback

ClientLease.clearはlease=null/expiry0だけ、resetはさらにConnection/listener/ClientLevel/LocalPlayer=null、session/revision0。disconnectそのものに直接clear注入があるわけではなく、通常LoggingOut/client Unload、100ms間隔のRenderTick ENDでのcontext変化検知、次loginでresetする。receiveは送信origin Connection不一致をdropし、新contextならreset後bind、新epoch・revoke/UNKNOWNではclear。validの実ref不一致/timeoutもclearするが、単なる「predicateがfalse」と全参照解除を同一視しない。

A1同梱ClientLeaseのEventBusSubscriber modidはuom_reconnect_verificationのまま、A1本体はuom_reload_verificationである。A1はdedicatedなのでclient callback実登録PASSを持たない。**将来SJ1の新helperだけ**で、同じイベント処理をclient Distで一度だけ明示登録する等のverification metadata配線を確認する。旧helperを変更せず、二重登録もしない。既存renderはvalid/nativeMoveも呼ぶため完全readonlyではない。これを既存cache失効処理として前後観測し、追加readonly observerがvalidを呼んでclearを促す方式は採用しない。nativeProbeの一時切替も監視側の無操作snapshotとは分離する。

SR.logoutでactive/remove・Grant失効はあるがhistory/issued/serial/sequencesは残る。same UUID/new playerはnew Sessionで、historyとのref不一致guardを通す。Bでold activeが残ってlogin guardに拒否されたらそのまま記録し、active.clearで直さない。認可に使えないことと、参照がmemoryに残ることは別判定。単一本人UUIDのhistory制約はfixture固有で、productionの複数player方針に採用しない。

観測候補はServerAboutToStart、ServerStarting/Started、ServerStopping/Stopped（hook RETURNも）、LevelEvent.Load/Unload、PlayerLoggedIn/Out、server listener disconnect/Connection close、client LoggingIn/Out/Clone/Unload、clearLevel前後、doWorldLoadのserver factory前/constructor後、ClientboundLogin前後、native cap deserializeとterminal callback。**どこかのcallbackでclearする方針を今回LOCKしない**。server stateはserver thread、client stateはclient threadで値へコピーし、event sequence/thread内順序とconstructor-before-thread-start・old-thread-joinの順序を使う。稼働中のHashMapを別threadから走査して順序証明としない。

#### 14.32.4 old fault / new faultを分ける証拠順

各sampleにevent番号・時刻・thread実ref・phase・engine実ref・fault値/初回reason/faultEvents・今回fault引数・State実ref/epoch/origins/UNKNOWN、frames/witness/stack/tokenを含める。reasonは初回fault後に変化しないため、reason一致だけでは起源を決めない。

| 順序点 | 必須観測 |
|---|---|
| A baseline / valid G1 / 退出直前 | clean時値、valid origin/binding/G1/lease、その後の最終fault/reason/event数と各実ref |
| A logout・native serialize/use(false)・stop | diskへ渡したtagとその後live変化を別記録。終了中faultが生じた正確なcall-site/引数/増分 |
| title到達時 / old thread終了後 | 早期menu sampleと保存close後のsettled sampleを分離。engine同一性・残maps・old lease clear/reset・old refs |
| B doWorldLoad factory前 / new server constructor後 | 旧serverを完全終了した時点の残stateと、thread開始前のnew server identity。ここでfault既存ならB deserialize原因ではない |
| B AboutToStart / candidate enable境界 | observerはMOD初期化時からarm済み。Aだけ既定のenabled=trueへ移行し前後記録。Bではenabledを変更せず値を観測（再enable操作0） |
| 各first deserialize直前 / HEAD / RETURN | native本体のHEADと、candidate Ownership.deserializeがfaultを立てる**前・後**を区別。sourceだけでなくplayer空tag/count0も記録 |
| B first server tick / first source load / login | 実server/level/source/player生成の順を記録。first tickがplayer loadより前でも順序を捏造しない |
| B current evaluation / client receive | まずraw read、次に既存candidate評価の前後、new session/lease/Connectionとの対応。旧Stateを返した実call-siteを特定 |

A1のpassive HEADは既存candidate注入後だったため、SJ1では新helperの同じdeserialize delegateの直前・直後に受動記録を置き、Ownership.deserializeを同じ引数で**1回だけ**呼ぶ。native RETURNも別記録し、Mixin priorityの推測だけで「fault前を観測」としない。delegate/hookの意味やfault条件は変更しない。State getterの実呼出しも入口new levelとRETURN objectを受動記録し、観測目的だけでstate()/computeIfAbsentを呼ばない。

分類：同engineでmenu/B生成前からtrueならOLD FAULT SURVIVAL。B直前false→そのnative deserializeのcandidate呼出しでtrueならNEW FAULT CREATION。既存true＋新call/faultEvents増分ならBOTH。旧contextでfalseのままならOLD-FAULTケース未到達。順序欠落・engine対応不明ならOBSERVER INSUFFICIENT。新deserializeでfaultとなるだけならold leakと呼ばない。旧faultの持越しでDENYはCROSS-WORLD FUNCTIONAL DENYであり、誤ALLOWと別の結果。

#### 14.32.5 Cleanup候補の比較のみ

| 候補 | 利点候補 | 未解決条件・今回は採用しない理由 |
|---|---|---|
| A ServerStopped全clear | 単純に旧mapを失わせられる | 旧fault証拠を消す、postの時点はcurrentServer解除前、遅延callback/別contextを巻き込む、pending stackの意味破壊。全map.clearを推奨しない |
| B generation token | 新旧contextを識別しやすい | 全発行/評価/packet/terminal/cleanupに同じgenerationを渡す必要。tokenだけではstrong ref残留を解決しない。既存protocol/session設計変更になり得る |
| C MinecraftServer実ref keyed registry | 実runtime contextへ結び付く | cleanupの時期・同server内level交換・遅延callbackを要検討。WeakHashMapだけでもvalue→key強参照があれば回収できない |
| D world/level generation scope | 同dimension IDの別levelを区別 | server-global native flag、複数dimension/sourceの終端整合を別途扱う必要 |
| E server実ref＋dimension＋epoch | server交換と停止episodeを分けられる | 同server内new Levelも必要ならlevel実refを含める。全engine/session/clientへ境界を揃えず部分採用すると不整合 |

推薦は**現状をclearせずSJ1で寿命・旧Stateの実利用を観測すること**だけ。B/C/E等のcleanup実装選択、UNKNOWN/fault回復、cold provenance再構築、new Grant再発行、production mappingは後続の別判断。今回仕様LOCKなし。


#### 14.32.6 次の最小1単位 SJ1 — 通常singleplayerの同world再open

**選定理由**：new JVM A1では消えていたclassloader staticが、実IntegratedServer交換を跨いでどう使われるかが上流の未証明条件。dedicated内で人工new objectを作る試験ではclient Lease・title中のnative global変更・実memory connectionの再生成を扱えない。よって**実client / integrated、1回のcontext交換**を選ぶ。Rのauth/TCP、歩行・描画・correctionを再試験する目的ではない。

CLEANとFAULTを独立2 runにはしない。Aのclean origin/G1を入力として通常終了を1回観測し、その終了でfaultが生じれば同じ遷移でold-fault survivalも判別できる。**自然にfaultが発生しなかった場合はFAULT持越しは未観測として残す**。故意のthrow/fault、W4反復、2個目worldを足さない。静的にはlatch解除なしを確認済みだが、これでFAULTのruntime PASSを代用しない。最小単位の目的は旧認可不使用と起源判別であり、あらゆるfault回復を証明することではない。

| 数・識別 | SJ1で承認を求める上限 |
|---|---|
| run ID / root | 将来の新ID YYYYMMDD-HHMMSS、build/verification/uom-same-jvm-そのID/。**今回はID発行/root作成なし** |
| process / JVM / classloader / client | Minecraft process1 / JVM1 / 同classloader / client1。launcher processは試験JVMに数えない |
| integrated server / world / load | IntegratedServer A/B計2・Server Thread各1。新使い捨てworld1、初回生成load1＋同world通常再open1＝計2。別world/旧world/3rd loadなし |
| login / 本人 | 正式ゲームprofileの同UUIDでhost login2。A/BのServerPlayer/LocalPlayer/session/両端connectionはnew実object。fabricated/FakePlayer/EmbeddedChannelなし |
| source | 論理UOM1、Aで新規生成1。Bは同UUID/typeの保存entityをnative loadしてnew refにする。追加spawn/再注入0 |
| prepare / skills / SP | Aだけ1回、正常fresh本人に通常浄化Lv1＋浄化の極意Lv1 ON、credit/spent103・未使用0。Truth未取得、購入gate維持。通常GUI購入証拠ではない |
| native開始 / Grant | Aで既存native TimeStopUtils.use(true, UOM, true, 180, false)を1回、valid ownership時G1 issue1。epoch/sequenceは実測。B native start0 / prepare0 / issue0 / G2なし |
| 終了 | A通常Save & Quit、B通常Save & Quit、最後Quit Game1。helperの結果後native cleanupはBで必要時だけ1回以下。A保存前cleanup0 |

**artifact/runtime**：MC1.20.1 / Forge47.4.0 / 既存Java17.0.7、FE2.7.20・EndingLibrary2.1.19fix・Iron's Spells1.20.1-3.16.3・Curios5.14.1+1.20.1・GeckoLib4.8.2・Iron's Lib1.20.1-2.1.0（§14.31の承認6原物、nestedは重複配置しない）、現在Food Healing配布Jar。同節[inputs.json](../build/verification/uom-process-reload-20260922-145728/server/inputs.json)と現物hashを次回配線時にも照合する。既存shader/login compatはそのままコピー使用し、既存fixture/旧helperは入れない。

- 製品Jarは今回も **229,494 bytes /150 entries / SHA-256 5C1A716EEDE7AEB4449B36EEFFBDDA156176B16B60A1676229224AA3390DB327**。
- [shader compat](../build/verification/fe-uniform-compat-20260921-125321/artifacts/fe-uniform-compat.jar)：14,270 bytes、SHA-256 BD2322818FDC65F4F20FD8332200CD6AED02F00487E3E1D8868D2865AD4B58D0。
- [login sync compat](../build/verification/endinglib-login-compat-20260921-155452/artifacts/endinglib-login-sync-compat.jar)：11,100 bytes、SHA-256 C8B948B30770A67D46CE3A8B09D4D7BE08E0E27A9548DC50518787C74FE58441。
- **追加外部artifact不足なし**。新SJ1 helperは承認後に作成する検証成果物で、外部MOD待ちではない。local Gradle8.1.1 / cache / offlineによる専用compile/reobfだけを次回候補とする。製品build/既存suiteは含めない。
- 既存Prism実行ファイル/ローカルlibrariesを使う**新instance**を推奨。Prism自体は証明要件ではないが、実績ある起動経路を使い旧instanceを変更しない。singleplayer memory Connectionで十分で、正規auth/TCPを再証明する必要はない。通常の既存認証profile（launcherが許すoffline起動も可）を利用し、accounts.json/token等を読取・コピーせず、profile偽装もしない。launcherが本人再認証を実際に要求する場合だけHUMAN/STOP。ネットワーク取得・新downloadは含まない。

**次回のhelper予定ファイルと境界（今回は全て未作成）**

| 新root内の予定 | 役割・禁止事項 |
|---|---|
| helper/.../SameJvmVerification.java、Audit/phase receipt | A/B control planeと明示prepare/start各1回、native lifecycleによるA→B進行。A1の両phaseで空heapを要求するdriverを丸ごと流用しない。Bは残stateを観測し、counter/mapを初期化しない |
| helper/.../SameJvmReadonlyObserver.java | 24項目・engine/native/server/client/phaseのraw snapshot。current map get・既存field読取だけ。Ownership.state/reconcile/permission、ClientLease.valid、lazy SavedData getterを観測のために呼ばず、未生成はABSENT/UNINITIALIZED |
| helper/.../mixin/SameJvmLifecycleObserver.java・NativeDeserializeObserver.java等 | constructor前後、native close/load/tick/deserializeの受動HEAD/RETURN、既存candidate fault delegate前後。cancel/値書換なし。対象descriptor/mappingは既存実bytecodeで照合 |
| 新helper内の既存candidateコピー・client配線 | Ownership/TerminalWitness/SessionRegistry/MovementGuard/networkの判定意味は保持。exact native callback/witnessの既存siteを維持。ClientLeaseは既存event behaviorをclientで一度登録し、既存cache失効の前後を記録する。init/re登録をworldごとに繰り返さない |
| 専用init.gradle / resources / manifest | 新run/path/UUID/product/原物hashをguard、専用task graph・offline compile/reobf・refmap/非混入を確認。旧helper/compat/source/build.gradleを編集しない |

観測用identity台帳はWeakReferenceと値labelを使用し、observer自身による旧server/worldのstrong保持を増やさない。candidateのhistory/binding等が保持する実refと、driver制御用の一時handleを別欄へ記録する。強制GC/heap dumpは不要、memory回収完了をPASS条件にしない。比較対象が回収済み/参照対応不能ならその比較は明記し、identityHashCodeだけで補完しない。

**具体的手順**

1. 次回承認後だけ新root/instance/補助を作成。起動前STATICでdelegate1回・observer無書込・旧core条件不変・client event登録1回・compile task graph・Jar非混入を確認する。新worldの生成前GUIでSurvival/Normal、doMobSpawning=false/naturalRegeneration=false。安全な閉鎖区画、本人HP20/MAX20/effects空/無関係damageなし。UOM NoAI/PersistenceRequiredを本人と接触しない保存可能位置へ初回配置。UI/準備に通常の安全配置を使っても、測定後座標/HP/native count/flag/SetをsetterやNBTで合わせない。
2. Aの初回native loadからobserverをarm。正常freshと自然quiet、faultfalse、origin/binding/frame/witness/stack/G空を確認する。必要ならAの新sessionに対する既存の初回clean boundaryだけを同条件で通す（fault回復ではない）。取得prepare1、native開始1、G1 issue1、既存network/同期経路でclientのvalid Lease受信を観測。取得/SP/HPとold context refsを記録する。first-write journalは再送拒否。初回native load時点でfault等が成立しなければclearで合わせずUNVERIFIED/STOP。
3. clientでcurrent valid leaseを観測したら通常のPauseScreenをclient threadへ依頼する方法を準備する。ゲームの通常pause以外のtick凍結はしない。Computer Useが通常Save & Quitを実行し、人間の秒数/tick操作に依存しない。pause前の有効leaseとlogout/resetを別記録する（pause中の期限経過は正常、expiryを延長しない）。Aのvalid入力成立前にcountが自然終了した場合は入力未成立として止め、180を再開始/延長しない。
4. clearLevel/native serialize/use(false)/logout/stopの順序を記録。titleの早期snapshot、ServerStopped hook RETURN＋old Server Thread.isAlive=false＋保存file close/lock解放を確認したsettled snapshotを取得。sample時だけold mapを走査し、稼働中server stateをrender threadから読む競合を避ける。**Aの保存後disk値とlive終了値を別に残すが、disk179/positiveを再び必須入力にしない**。A1のdisk/cold非復活を再演する試験ではない。
5. 同PID/JVM/classloader・同titleのまま、通常GUIで今回worldを1回再open。factory直前、new IntegratedServer constructor直後、About、level load、各native deserializeのcandidate前/後/RETURN、first tick、new loginを順に観測。Bでcandidate enableを書換えず、prepare/native start/Grant発行/authority importを行わない。old Stateをnew levelのcurrentとして返す呼出しがあれば、その場でSAFETY FAIL（state machine隔離）を確定し、後続条件を未観測として終了へ進む。
6. 本質FAILがなければ、new player/sourceの実ref対応とraw snapshotを取った後、既存のcurrent candidate評価をserver threadで最大5連続END確認する。評価はreconcile等を呼び得るためreadonly snapshotとは別ログ。old G1とnew listenerのmatches、旧witness消費/旧callback再開、epoch/sequence、history/issued残留、client cache/actual validの経路を対照する。Bへ新ALLOW leaseを自動発行しない。新Grant0でもnativeが非停止なら普通に動けることがあるため、通常移動とFood Healing追加認可を混同しない。歩行・R試験は追加しない。
7. 結果をsealしてからだけ、残active UOMがある場合は既存native use(false)による終了cleanupを最大1回、結果後操作として記録できる。fault/UNKNOWN/State/Leaseをhelperが修復するcleanupは禁止。通常Save & Quit→titleで保存/old B thread終了を照合、今回worldの本人/source/SavedDataをreadonlyで確認→Quit Game→process終了を確認する。3rd loadはしない。

**外部stateの書込境界**：初回の新world環境/本人取得/source配置、Aの明示native開始1、通常ゲーム処理によるcount/flag/Set/同期/保存、結果後必要時のnative終了だけ。observerはEndingLibrary state、HP、effect、cap/native NBT、位置、dimensionを修復しない。native自身のshutdown/client-no-levelによる書換えは原因付きで記録する。observerがlazy cacheを生成したりfaultをresetして問題を隠すことも不可。source/controllerの新contextへ旧authorityを注入しない。

#### 14.32.7 判定・停止・終了

| 判定 | SJ1の条件 |
|---|---|
| OLD CONTEXT AUTHORITY NON-REUSE LIMITED PASS | 同JVM/同loader・実server交換と必須順序が成立。old origin適用0、old Grantによるpermission true0、old session/new Connection誤一致0、old binding再bind0、old witness consume/旧callback継続0、旧epoch/sequenceのみの認可0、old ClientLeaseのnew refsでvalid0。旧Stateを同dimensionだけでnew currentに用いないことも必要。Bで全条件へ到達し5 ENDを観測し、正常保存終了を別照合 |
| SAFE-SIDE DENY | 誤利用がなく、new deserialize fault等によりUNKNOWN=true/permission=false。限定非再利用PASSと併記可。old fault残留ならCROSS-WORLD FUNCTIONAL DENY、new/both/観測不足を分離 |
| SAFETY FAIL | old G1/UUID origin/binding/session/witness/epoch番号/leaseの誤利用、**same dimension IDだけでold Stateをnew current扱い**、old fault/frame/stackを根拠にnative stateを補正、helperの全clear/修復による隠蔽。permission=falseでも旧State current化はstate machine隔離FAILであり、単なるsafe DENYへ格下げしない |
| 保持・leakリスクの記録 | history/issued/bindings等のstale strong ref、engine/token残留だけは即SAFETY FAILにしない。使用の有無・revoke状態・false DENY・memory保持を別報告。GC回収/長期性能を証明しない |
| UNVERIFIED | process再起動/同loader不明、A valid入力不成立、旧server終了不明、new server実生成不明、必須snapshot欠落（特にfault前/first deserialize前）、identity対応不足、shader crash/observer不備、予期しない自動load等。人工resetで隠した場合は上記FAILも併記 |
| timeout | 初回client起動180秒、各server load/login180秒、A valid入力待ち最大600 ENDまたは60秒、B current観測最大600 ENDまたは60秒、各通常保存/旧thread終了60秒。基準成立前の無限待機/再runはせず、到達項目と未到達を保存。UI待機中も環境安全を維持 |
| STOP | production/LOCK/protocol/session/Grant/UNKNOWN設計変更が必要、新artifact不足、本人再認証、native反例、helperで安全な観測不可、本質FAIL/無関係被弾。強制修復や次caseで覆わない |

本質FAIL時は最初の時点を凍結記録し、**native callbackの途中でthrow/cancelして終了順を変えず、戻った後に通常pause・Save & Quitへ進む**。B loginまで到達しなかった場合、new player/session/Leaseの未到達はUNVERIFIEDとして残し、全項目PASSにしない。保存がtimeout/例外なら可能な通常終了を試み、killやprocess再起動を自動代替せず終了未確認を報告する。A終了未確認ならBを開かない。新runのreceipt/journalを削除してやり直さない。

HUMANの予定操作0。world作成/command/Save & Quit/再open/QuitはComputer Use、timing-dependentな観測/phase制御はAUTOMATED。認証や入力不可が実際に起きた場合だけ必要な1操作を依頼。認証資格情報は観測対象外。実行主体・native処理・verification操作をログで分離する。

#### 14.32.8 未証明・次回承認範囲

SJ1に成功しても、UNKNOWN/fault回復、reload後正常保護の継続、cold stop provenance再構築/new Grant policy、故意のold FAULTが自然に生じなかった場合のcross-world fault実測、同server内level-only交換、crash/kill/observer loss/transfer fault、FE goal再延長、多sourceの未検証順序/性能、real input/render/correction、production mapping、UOM/P vehicle方針は未証明。A1/D1-W/R・login/shader・player17/vehicle16・source dimension・L2 133/core60/Cube49/Invader86/migration/既存実clientを再実行しない。

次回ユーザーが承認すべき1範囲は、**SJ1専用新rootのhelper/受動observer作成と限定offline compile/reobf・STATIC確認→新Prism instance/新world→A prepare/native start/G1各1→通常Save & Quit→同processの同world再open1→B旧認可不使用/残fault観測→seal・必要な結果後native cleanup→通常保存/Quit/終了・証拠照合→3文書更新**。旧helper/compat/製品/原Jar・通常test/build.gradle/gateは不変。cleanup戦略の実装・production mapping・購入解放・UNKNOWN回復はこの範囲に含めない。今回その実行承認を推定せず、設計完成で停止する。

**SAFE DESIGN PROVEN=NO / SOURCE DIMENSION TRANSITION VERIFIED=NO / BLOCKED - TIME STOP SOURCE OWNERSHIP**。FE6 MobEffect / TimeStop production NOT IMPLEMENTED、P/T IMPLEMENTATION_PENDING・購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、UOM/P vehicle未LOCK・既存個別開始条件を維持。食料生産の極意による可逆クラフト増加は既知許容仕様・バグ修正対象外。


<a id="uom-same-jvm-sj1-result"></a>

### 14.33 SJ1 same-JVM実行結果 — 旧State current化のSAFETY FAIL（2026-09-27 10:30 JST）

§14.32の設計を今回明示承認された範囲で実行した。過去§14.11–14.32の次作業・STOPは当時の履歴として保持する。A1/D1-W/R、player17/vehicle16、L2 133/core60/Cube49/Invader86、既存限定実clientは再実行0・既存判定不変。

**対象・証拠**：run `20260927-095730`、[root](../build/verification/uom-same-jvm-20260927-095730/) / [reviewed-results.json](../build/verification/uom-same-jvm-20260927-095730/audit/reviewed-results.json) / [全時系列](../build/verification/uom-same-jvm-20260927-095730/audit/ordered-events.json)。MC1.20.1 / Forge47.4.0 / 実Prism runtime Java17.0.15（Gradle用17.0.7と区別）。FE2.7.20、EndingLibrary2.1.19fixほか承認6原物と既存shader/login compatは[inputs](../build/verification/uom-same-jvm-20260927-095730/audit/inputs.json)の同hash。実ロードnested PlayerAnimator1.0.2-rc1+1.20 / MixinExtras0.4.1、外側重複配置なし。新規外部artifact取得0、認証ファイル読取/コピー0。native更新確認等の通常launcher/MOD挙動を新downloadの承認にはしない。

**補助・build**：別MOD `uom_samejvm_verification` version `20260927.095730`、94,023 bytes /44 entries、SHA-256 `81160A41AF8B2B17F89E427D1606C74DE44A93C1160E8C6A527928BB254805E8`。[STATIC](../build/verification/uom-same-jvm-20260927-095730/audit/static-gate.json)で既存8core判定不変、deserialize delegate1、client登録1、observerによる余分なvalid/permission呼出0・非混入を確認。専用offline compile/reobf成功、製品build/既存test0。初回native-platform.dllアクセス失敗と自動承認レビュー拒否は測定前の環境記録として保全し、最新の明示実行承認と専用task graphを再提示した同一コマンドが承認され成功。verification repair **1 cycle**：終了中に初期HP検査を走らせないclosing条件と、ロード画面終了後の通常pause配線だけを修正して再compile。State key/authority/Grant/Session/UNKNOWN/witness/Lease意味・期待値変更0、測定後helper変更0。

**規模・主体**：Minecraft PID **24916**、JVM start `2026-09-27T01:11:37.917Z`、同Minecraft singleton・helper/library classloader・engineを実ref比較で確認。1 process/client/JVM、IntegratedServer A/B2、新world1、load2、同host UUID login2。A prepare1/native start1/G1 issue1/UOM初回spawn1、B prepare/start/issue0・G2なし・追加spawn0・3rd load0。UI/command/F2/保存/Quit＝COMPUTER USE、観測/native入力/normal pause＝AUTOMATED、HUMAN0。singleplayer memory connectionでありRの正規auth/TCP再試験ではない。

| 段階 | 実測・判定 |
|---|---|
| 生成前/準備 | 新instance/worldのみ。GUIでSurvival/Normal、doMobSpawning=false、naturalRegeneration=false、Superflat/構造物OFFを設定しF2記録。入場後rules読取、閉じた石床/壁/屋根、安全なNoAI/PersistenceRequired UOM1。本人HP20/MAX20/effects空、P/M各1 ON、Truth0、SP0/spent103、pendingfalse。取得prepareはGUI購入成功ではない |
| A valid | `TimeStopUtils.use(true,UOM,true,180,false)`1。origin/binding1、session1/epoch1/sequence1のG1、permission成立。実client Lease valid=true、nativeCanMove=false/effectiveCanMove=true、canonical一致。[A server/client receipts](../build/verification/uom-same-jvm-20260927-095730/audit/A-VALID-CLIENT.json)。成立後通常pause、pause中の1秒expiryを延長しない |
| A退出順序 | pre-exit event4522/4523 → logout4531–4534/revoke → native serialize/stop → use(false)4551–4562 → Level unload4563–4567 → ServerStopped4570 → hook RETURN4571 → early title4574 → settled4575。hook RETURNだけで完了扱いせず、thread TERMINATED/生存false＋session.lock解放＋level.dat読取を確認 |
| A settled static/native | old State ref383751088、epoch1/origin1/binding1、history/issued各1（revoked）、active/grants0、serial/sequence1、frames/witness/stack0。engine faulttrue/faultEvents2。native source終了count0/Set空/globalfalse。A serializeのcount177とBでのnative復元177を確認（A保存値を終了live0へ読み替えない）。[title A](../build/verification/uom-same-jvm-20260927-095730/audit/TITLE-A-SETTLED.json) |
| Bの実context | server1423351928→543661246、thread2136311569→398571464、level1924553992→125722236、source1015896242→480443524、cap1695124176→893261066。これらはlabelだけでなく旧refとの==/!=を記録。same logical UOM UUIDのnative load。client LocalPlayer/level/listener/Connectionも実ref差を観測。ServerPlayerは同profile再loginとnew labelを記録したが、下記早期FAILによりB server listener/Connection/channelを含む全実ref比較は**UNVERIFIED**（hash値だけで補完しない） |
| 最初の本質反例 | **event5005：OLD_STATE_CURRENT**。native cap deserialize→Ownership.deserialize→TerminalWitnessBridge.fault→Ownership.unknown→Ownership.stateで、同`minecraft:overworld`文字列だけによりold State実refをnew ServerLevelへ返した。oldStateSameActualRef=true、oldServerSame=false、oldLevelSame=false。[call-site](../build/verification/uom-same-jvm-20260927-095730/audit/state-access.jsonl)。**SAFETY FAIL**、permission falseでもSAFE-SIDE DENYへ格下げしない。全clear/UNKNOWN修復0 |
| old/new fault | **BOTH**。A clean0、A logoutのdirect-count-changeで0→1、native終了unexpected-useで1→2。settled/factory/constructor/About/first candidate前も同engine/faulttrue/events2。B source deserialize前event5002→fault前後5003/5004で2→3→candidate後5006→native HEAD5007/RETURN5008。続くplayer空NBTで3→4。reasonは最初のdirect-count-changeを保持するため、reasonだけで新faultを見落とさない。[fault log](../build/verification/uom-same-jvm-20260927-095730/audit/fault.jsonl) |
| 旧authority不使用の限界 | old State current化は最初の1 siteを記録。旧G1のB ALLOW/old Lease validは観測0、witness consume0、旧callback継続観測0、G2なし。old origin/bindingは残留しnew objectへのrebindなし。ただし**Bのcontrolled permission/matches 5 ENDは0（本質FAILで未実施）**。全9非再利用条件のPASSや全経路利用0を推定しない。history等strong ref残留だけを別のSAFETY FAILにはしない |
| ClientLease | Aで有効→pause中expiryによるclear→LoggingOut/LevelUnloadでrefs/session/revision reset。B LoggingIn後新refs、Bの272 client samplesでleaseValid0、ALLOW受信0。余分なobserver valid呼出0。predicate mismatch/explicit revokeの全経路を追加PASSにしない |
| seal・結果後操作 | [RESULT](../build/verification/uom-same-jvm-20260927-095730/audit/RESULT.json) / [SEALED](../build/verification/uom-same-jvm-20260927-095730/audit/SEALED.json)を保存後、Bで残count177をnative use(false)1回で終了。observerによるEndingLibrary count/flag/Set/HP/位置/NBTのsetter修復0、fault/UNKNOWN/State修復0。原Jar不変。正常loadのcallbackは途中cancel/throwせず完走し、通常pauseへ |
| 保存・終了 | A/Bとも通常Save & Quit、両thread TERMINATED/hook RETURN/file close確認。[B title](../build/verification/uom-same-jvm-20260927-095730/audit/TITLE-B-SETTLED.json)、[readonly disk](../build/verification/uom-same-jvm-20260927-095730/audit/disk-B-readonly.json)は本人HP20/SP0/103/PMon、source同UUID1・NoAI/persistent、native count keyなし=0、SavedData Dimensionsなし=空。authority持込keyなし。10:21:56 JST normal Quit/Stopping!、[PID終了確認](../build/verification/uom-same-jvm-20260927-095730/audit/process-exit.json)。強制kill0、exit codeは独立取得していないのでexit0と書かない |

**不変**：[2,085files照合](../build/verification/uom-same-jvm-20260927-095730/audit/preservation-after.json)差分0。Food Healing product/配置同hash `5C1A716EEDE7AEB4449B36EEFFBDDA156176B16B60A1676229224AA3390DB327`、229,494 bytes/150 entries。製品src/main・通常test・build.gradle・gate/config/protocolと旧helper/compat/原物を変更していない。製品TimeStop未実装のため、今回の反例は**verification-only設計の隔離不備**であり製品既存機能の新FAILとはしない。旧world/原本world再利用0。

**結論**：SJ1はSAFETY FAILを保存して正常終了。後続の5 END/全identityを未到達として保全し、測定をやり直さない。次は下記queue #1の実反例修正＋限定再検証だけ。SAFE DESIGN PROVEN=NO / SOURCE DIMENSION TRANSITION VERIFIED=NO / BLOCKED - TIME STOP SOURCE OWNERSHIP、FE6/TimeStop production NOT IMPLEMENTED、P/T IMPLEMENTATION_PENDING/購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、UOM/P vehicle未LOCKを維持。

<a id="v3-fixed-release-completion-queue"></a>

### 14.34 正式release scope決定と固定主要工程（2026-09-27 10:30 JST）

Pam's Trees増産とNutrition/Food-Level新食義countを **v3.0.0 FORMAL RELEASE REQUIRED** へ昇格。実装未着手は維持し、候補扱いで正式releaseから外さない。仕様全文は[SPEC§14.1–14.2](SPEC.md#14-v300-formal-release-required-additions)、optional境界は[COMPATIBILITY§16](COMPATIBILITY_POLICY.md#16-pams-harvestcraft-2---trees--v300-release-required)へ集約。旧8/27は当時NOT LOCKEDの履歴。

| 必須feature | 今回LOCK | 実装前の残判断 |
|---|---|---|
| Pam's Trees | 対応版のeligible direct harvest1＋eligible log1→同harvest2、shapeless。optional未導入安全・外部Jar非同梱 | exact対応版policy、最終whitelist、apple/paper/stringとprovenance不能ID、logs/logs_that_burn/narrower、Nether stems/modded logs、個別JSON/custom serializer。既存1.0.2静的50候補/実registry/tag限界を保持、今回の判断で全50を許可しない |
| 新食義count | 1 Nutrition/FoodLevel unit=count1。foodは満腹/Satisfaction成功含むdeclared Nutritionを1回、non-foodは正の実delta、saturationのみ0。同foodのdeltaを二重加算しない。server権威、generic増加はRoot/TrueRootへ入れずHP変換不変 | 最終level/SP thresholdは**RELEASE-BLOCKING USER DECISION**（1000はPROVISIONAL）。旧partial EatCount、custom threshold Config、v2/v3移行を無断変換しない。long/invalid/overflow atomicity/一括複数thresholdとSP/同期保存の設計が必要。SJ1中のproduction200は不変だが旧食事回数方式のまま正式release不可 |

**固定release completion queue：#1 COMPLETE／残8主要工程（2026-09-27 12:23 JST、§14.36）**。9工程の順序・scopeは不変。旧SJ1の実反例と前follow-upのUNVERIFIEDは§14.33/14.35に保全。下記はこのターン後の実行承認ではない。

1. **COMPLETE — same-JVM authority isolation限定follow-up。** §14.36でserver/client source分離、A valid→B新contextの5 END非再利用・server native lookup同一実ref・cleanup1・disk0・通常終了を確認。既存authority candidate核心不変。総合SAFE DESIGN/正常保護復帰へ昇格しない。
2. UOM TimeStop production mappingを確定・実装。未LOCK UOM/P vehicle方針が製品境界を左右する箇所だけ利用者判断を得る。
3. Fantasy Ending UOMの6 MobEffect secondary siteをproduction実装＋必要な限定統合。
4. 浄化/真実の正式purchase readiness接続＋通常SP transaction確認。
5. Pamの未決定詳細LOCK→実装→optional absent/present限定検証。
6. 新countのbalance/data migration判断LOCK→実装→exactly-once/保存/移行/threshold検証。
7. Flight ownershipの既存release blockerを完成（今回開始しない）。
8. 最終build/unit/check＋必要な既存回帰だけ＋最終Jar監査。
9. RC判定／v3.0.0正式release。

**RELEASE COMPLETION MODE**：正式blockerは既存正本の本来のrelease安全条件＋今回の必須featureに限定。新しい未検証項目は実安全反例/データ破壊/起動不能/必須機能未実装を示さない限りNOT TESTED / POST-RELEASE BACKLOGへ分離し、全MOD版/gunpack/boss/trait/performance/WARN0を自動必須化しない。transfer fault/observer loss、goal再延長、多source未網羅、render/correction等の未証明は残すが、全項目追加試験をqueue #1/#2の無条件前提にしない。REAL2CLIENTはSECOND ACCOUNT REQUIREDの既存BLOCKEDとして別枠維持、既存release policyを独断で免除/変更しない。

Break Realm expansion/Bulwark/FOURTH BOOT/最低範囲外Hyperlink・Fumetsu/全L2/全TaCZ/未LOCK True Rootは今回着手しない。試作型機関弩は完全一致の開始指示まで監査・調査・設計・test・実装禁止。食料生産の極意による可逆クラフト増加は既知許容仕様・バグ修正対象外。


<a id="uom-same-jvm-isolation-followup-result"></a>
### 14.35 queue #1 same-JVM authority isolation follow-up — 2026-09-27 11:10 JST

**総合UNVERIFIED。** verification candidateのisolation修正と主要な非再利用観測は成立したが、測定後の保存照合でhelperのsource参照混同が確定した。自動receiptの `LIMITED PENDING SAVE` を最終PASSへ転記しない。旧§14.33 run20260927-095730のSAFETY FAILは改変せず保持。本runを修復・再入場・同case再試験せず、productionへの接続0。

根拠：[実測集計](../build/verification/uom-same-jvm-isolation-20260927-104834/audit/reviewed-results.json) / [helper不備](../build/verification/uom-same-jvm-isolation-20260927-104834/audit/observer-gap.json) / [イベント順序](../build/verification/uom-same-jvm-isolation-20260927-104834/audit/ordered-events.json) / [STATIC](../build/verification/uom-same-jvm-isolation-20260927-104834/audit/static-gate.json)。run rootは `build/verification/uom-same-jvm-isolation-20260927-104834/`。

| 単位 | 実測・境界 |
|---|---|
| candidate変更 | 新AuthorityContextのIdentityHashMapをactual MinecraftServerでkey化し、その配下のStateはactual ServerLevel key＋bound server/level。engine/fault、calls/frame/witness lookupを同contextへ配線。dimension-only lookupと未使用forgetForReloadを除去。Stopped hook RETURNでclosed化、旧context/map/fault/history/issued/epoch/sequenceを消去しない。閉じたcontextからの認可/継続を拒否。既存の観測quiescence/new native episode処理の意味は不変 |
| STATIC/build | SessionRegistry・ClientLease・MovementGuard・VehicleGuard・VerificationNetwork・TerminalWitness・native site plugin・全既存Mixin sourceはbyte同一。native target/deserialize委譲/witness site・P/M predicate・外部write site不変。helperだけoffline compile/reobf成功、製品task/旧suiteなし。sandbox初回native DLL読込失敗は承認済み権限付きローカルoffline実行へ切替。補助repair cycle0（UI scroll引数訂正1回、source/build修正cycleではない） |
| helper識別 | version20260927.104834、98,747 bytes、45 entries、SHA256 `FF35639674C7BA0F483A751A9DBDE28B9FF6C395A3A0D7C8237D07D191E6F01F`。verification/uom以外のclass/ExampleMod/製品class混入0 |
| 規模・主体 | PID27892/start2026-09-27T01:57:06.533Z、同Minecraft/JVM/helper・library loader。A/B各1、新world1/load2/host login2。COMPUTER USE=生成前gamerules/Survival Normal/superflat/command/F2/save/Quit、AUTOMATED=prepare/native入力/観測、HUMAN0。auth/TCP再試験なし、通常singleplayer memory接続 |
| A成立 | HP20/MAX20/effects空、P/M Lv1 ON・Tなし・SP0/spent103/pendingfalse、prepare1、閉じた区画、native UOM開始180を1回、origin/binding各1、G1 session1/epoch1/sequence1。実client Lease valid=true/nativeCanMove=false/effectiveCanMove=true |
| A通常終了 | logout/revoke/native終了/unload/stopped hook RETURN/thread TERMINATED/lock解放/title settled。旧State216547031・engine171789082・context2068749450・origin/binding各1を保持。fault0→2（direct-count-change/unexpected-use）、closed=true。history/issued各1、active/grants空、sequence1維持。A cleanupなし |
| B旧FAILと同じcall chain | native cap deserialize→Ownership.deserialize→fault→unknown→state、event3180：State1213665408 != old216547031（実ref比較false）、bound actual server=1418863004 / level1802970113と一致。dimension同名でも旧State/engineをcurrentへ返さない。identityHashはラベル、等値判定はactual ref |
| B fault | event3178 PREはnew engineのfaultEvents0/false、A old engineはfault2/trueのまま。source deserializeでB0→1、player deserializeで1→2。**NEW FAULT CREATION**、A faultのcurrent継承0。B UNKNOWN/permissionfalse/Grant0を維持。数値2の一致を同latch持越しとしない |
| B主要5 END | 連続5回、actual B server/levelのcurrent State、旧State同一false、origin適用0/binding再bind0、old G1 matchesfalse/permissionfalse/session誤一致false、old witness消費delta0/callback token継続delta0/current frame/calls0、issued1/G2なし。B session2/new player/listener/Connection/channel、旧epoch1/sequence1は残るが認可0。client338samples valid0、B ALLOW受信0。observerの追加valid呼出0 |
| helper観測不備 | server native loadはevent3184/3189でsource1485406040/count176/actual B所属を観測。後のevent3238 Render threadのclient EntityJoinLevelEventが `join→sourceLoaded` を通り共有u1をclient source1739876636へ上書き。server==0/client level932102776/count0。「oldと違う」だけではcurrent B所属を証明しない。各END/RESULTのsource欄もこのclient handleで、source継続観測はUNVERIFIED。candidate permission/Stateはactual ServerPlayer→ServerLevelから独立lookupなので前行の実測を消さないが、全run PASSへ拡張しない |
| seal・cleanup | RESULT/SEALEDはprepare1/nativeStart1/Bends5、測定時FIRST-FAILUREなし。ただしclient handle count0/native globalfalseでcleanup条件を誤skip。native B use(false)0、CLEANUP-FIRING/DONEなし。保存source count160が残存。cleanup完了/外部state0化とは記録しない。read-only照合のnativeCleanupZero=falseも保全 |
| 保存・終了 | B通常Save & Quit、stopped hook RETURN/thread TERMINATED/lock解放/level readable、readonly本人/source/SavedData照合。HP20/PMon/SP0/103/pendingfalse、同UUID source1/NoAI/persistent/count160、authority保存keyなし。native liveと保存を同値と推定しない。11:04:19 JST normal Quit/Stopping!、11:05:27 PID終了確認。独立exit code未取得。3rd load/修復/killなし |
| 不変 | 製品229,494 bytes/150 entries/hash `5C1A716EEDE7AEB4449B36EEFFBDDA156176B16B60A1676229224AA3390DB327`、配置同hash。旧SJ1 root/world/helper/証拠を含む2,563files差分0。EndingLibrary/FE/shader/login compat原Jar・製品source/test/build.gradle/protocol/gate/config不変。EndingLibraryへ追加setter/count/Set/NBT修復0 |

**停止理由と次**：本質測定開始後にhelper logicを修正して同caseをやり直さないという今回の境界に従う。source混同は製品FAILではない。旧State reuse修正を未着手へ戻さず、次回はhelperのServerLevel＋actual current serverによるsource限定、独立client source観測、各B END/cleanup前のnative lookup同一ref確認だけを直す。既存authority/期待値は維持し、新runで不足を限定確認する承認待ち。追加外部artifactなし。固定queue残9、#2を開始しない。

SPEC§3の旧200 LOCKは履歴/現在runtime baselineとして保持し、正式releaseでは§14.2の新単位へ置換予定と明記。1000 PROVISIONAL・production200/Config/migration不変。Pam/countは正式release必須・未実装のまま。

SAFE DESIGN PROVEN=NO / SOURCE DIMENSION TRANSITION VERIFIED=NO / BLOCKED - TIME STOP SOURCE OWNERSHIP、FE6/TimeStop production NOT IMPLEMENTED、P/T IMPLEMENTATION_PENDING・購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、vehicle未LOCK・既存個別gate、可逆クラフト増加の既知許容仕様を維持。既存A1/D1-W/R/player17/vehicle16/held stale/shader/L2/core/Cube/Invader/移行等の再実行0。


<a id="uom-same-jvm-source-followup-completed"></a>
### 14.36 queue #1 server/client source分離・残確認 — 2026-09-27 12:23 JST

**QUEUE #1 SAME-JVM AUTHORITY ISOLATION FOLLOW-UP LIMITED PASS＋SAFE-SIDE DENY**。新run `20260927-121020`、1 Minecraft/PID/JVM/classloader/client、IntegratedServer A/B各1、新world1/load2/login2。旧§14.33 SAFETY FAILと§14.35 UNVERIFIEDを改変せず、旧world/helper/receiptへの操作0・3rd load0。今回の根拠は[独立照合](../build/verification/uom-same-jvm-isolation-followup-20260927-121020/audit/reviewed-results.json)、[イベント順](../build/verification/uom-same-jvm-isolation-followup-20260927-121020/audit/ordered-events.json)、[STATIC/核心hash](../build/verification/uom-same-jvm-isolation-followup-20260927-121020/audit/static-gate.json)、[補助差分](../build/verification/uom-same-jvm-isolation-followup-20260927-121020/audit/helper.diff)。

| 単位 | 今回実測・限定判定 |
|---|---|
| helper/境界 | `uom_samejvm_verification` version20260927.121020、105,081 bytes/46 entries、SHA256 `D52585B51D736056C0761C7BCE8D1D5FFB56272C5D401C53AA6F4C81B76BD097`。別source slot/readonly観測/cleanup driver配線だけ変更。offline専用compile/reobf成功、製品task・既存suite0、repair cycle0。初回sandbox native DLL読込失敗は承認済み権限付き同local Gradle/cache・offlineへ切替。測定後補助変更/retry0 |
| candidate不変 | AuthorityContext/Ownership/engine-fault/calls-frame-witness/closed拒否/SessionRegistry/Grant/UNKNOWN/MovementGuard/VehicleGuard/ClientLease/TerminalWitness/NativeTerminalSitePlugin・既存Mixin source同一。核心関連29class中26完全byte一致、ClientLease/SessionRegistry/VerificationNetworkの3classは同長run ID定数だけ差、判定byteの変更なし。外部Jar/製品protocol等不変 |
| source分離 | private serverSourceはserver thread限定、event実ServerLevel/actual current server/UUID/type/alive/非removedを確認。deserialize/joinはnative挿入前（event2363/2368、lookup false）なので読取候補として記録し、native lookupへ現れた同actual refを1回bind。client joinはprivate clientObservedSourceとimmutable readonly DTOのみ。clientからserver slotへのfallbackなし。wrong-side試行はguard停止、cross-write両方向0 |
| A準備/有効性 | 生成前GUIでSurvival/Normal・自然湧き/自然回復OFF、閉じた石区画。prepare1回、HP20/MAX20/effects空、P/M Lv1 ON・Truthなし・SP0/spent103/pendingfalse。source NoAI/persistent/count0からnative use(true,UOM,true,180,false)1回、origin/binding各1、G1 session1/epoch1/sequence1。実client Lease valid/nativeCanMovefalse/effectiveCanMovetrue。GUI購入成功ではない |
| A退出 | 通常Save & Quit→logout/revoke/serialize/native終了/unload/Stopped hook RETURN/thread TERMINATED/file close/title settled。旧State1753292325/context368407437/engine2036189988、origin/binding各1・fault2・history/issued1を保持、active/grants空。A cleanup0、全clear0 |
| B native deserialize | 同PID24356・同loader。event2359でState1162357709、oldStateSameActualRef=false、bound server1652251078/level1527409968のactual ref一致。event2357のnew engine fault0からnative deserializeで0→1→2、A old fault2は別engineに保持。old/new faultの数値一致を同latch持越しとしない |
| B source初期bind | same logical UUID `a6da8ec4-7b78-450e-a137-977f0cb425cc`、new server ref246197057/cap839187288、native lookup同一ref、count176、bind1。独立client ref1159652513は別欄で観測しserver判定に使用しない。identityHashはラベル、合否はactual ref `==` |
| B controlled5 END | event2421/2429/2436/2443/2451。各PRE/POST lookup同一ref＝5/5（10観測）、server count166/165/164/163/162、同B server/level・UUID/type/alive/loaded。old State current化0/origin適用0/binding再bind0/old G1認可0/session誤一致0/witness consume増加0/callback continuation0/stale epoch-sequenceのみの認可0、G2なし。B session2・new player/listener/Connection/channel。client220samples valid0/ALLOW受信0、observer追加valid呼出0 |
| seal/native cleanup | PRE_SEALとPOST_SEAL_PRE_CLEANUPのserver native lookup同一ref/count162でguard成立。RESULT/SEALED後、正式 `TimeStopUtils.use(false,cleanupSource,true,0,false)` **1回**。RETURNも同server source、count0/globalfalse/dimension Set空/processing stack0/currentGrant0/permissionfalse。faulttrue・UNKNOWNtrue・faultEvents2を保持。外部count/flag/Set/NBT/HP/位置等のsetter修復0。通常native処理による変更と区別 |
| disk/終了 | B通常Save & Quit、hook RETURN/thread TERMINATED/session.lock解放/level readable後に[readonly保存照合](../build/verification/uom-same-jvm-isolation-followup-20260927-121020/audit/disk-B-readonly.json)。本人HP20/P/M各1ON/SP0/103/pendingfalse/effects空、sameUUID source1/NoAI/persistent/count keyなし=0、authority独自保存key0。12:17:47全dimension保存、12:18:11正常Quit、12:18:23 PID24356終了確認。独立exit code未取得。force kill/再入場0 |
| 不変/主体 | Food Healing229,494 bytes/150 entries、SHA256 `5C1A716EEDE7AEB4449B36EEFFBDDA156176B16B60A1676229224AA3390DB327`。配置10Jar一致・[旧証拠含む3,037files差分0](../build/verification/uom-same-jvm-isolation-followup-20260927-121020/audit/preservation-after.json)。SPEC含む製品source/test/build.gradle/原物/shader/login compat/gate不変。COMPUTER USE＝GUI/command/F2/save/Quit、AUTOMATED＝準備/native入力/readonly観測、HUMAN0。singleplayer memory接続でauth/TCPの再試験ではない |

**queue #1 COMPLETE、固定queue残8。次は#2 UOM TimeStop production mapping/implementation（別承認、今回未開始）。** 必要だった同JVMの隔離・server source継続・cleanup証拠は揃い、既存の限定証拠を使うproduction写像へ進める。これは正常保護復帰/fault recoveryや全lifecycleの総合安全証明ではない。SAFE DESIGN PROVEN=NO / SOURCE DIMENSION TRANSITION VERIFIED=NO / BLOCKED - TIME STOP SOURCE OWNERSHIP、FE6/TimeStop production NOT IMPLEMENTED、P/T IMPLEMENTATION_PENDING・購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、UOM/P vehicle未LOCKを維持する。

既存A1/D1-W/R/player17/vehicle16/held stale/source dimension/shader/L2/core/Cube/Invader/migration/既存実clientは再実行0。Pam/countは正式release必須・未実装、1000 threshold PROVISIONAL、200はhistorical/current baseline・正式releaseで§14.2へ置換予定という現行仕様を維持。別機能へ進まず、可逆クラフト増加は既知許容仕様・バグ修正対象外。


<a id="uom-production-mapping-stop"></a>
### 14.37 queue #2 production mapping — authority入口差異により製品変更前STOP（2026-09-27 12:43 JST）

**queue #2は写像照合を開始し、製品変更前にSTOP（STATIC MAPPING BLOCKER）**。承認原Jarの通常UOM初回goalはcount180→use(true)の順で、現候補はcommon-use HEADのreconcileによりUNKNOWNとなる。また候補のdeserializeは空NBT/zeroもcontext全体のterminalFaultへ入れる。開始由来・deserializeの認定条件を変えずに常時production化することはできないため、利用者指定のauthority semantics変更時STOPを適用。[根拠と次の範囲](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-production-mapping-stop)。製品変更/build/test/ゲーム起動0、今回の動作FAIL/PASSは付けない。**#1 COMPLETE維持／#2 INCOMPLETE／固定queue残8**。購入停止/SP保護・SAFE DESIGN PROVEN=NO・RC=NOを維持。

監査root `uom-production-20260927-123538` は読取・写像記録用であり、game/server runではない。[mapping表](../build/verification/uom-production-20260927-123538/audit/mapping.md) / [静的根拠の全文](../build/verification/uom-production-20260927-123538/audit/mapping-blockers.md) / [原物再照合](../build/verification/uom-production-20260927-123538/audit/inputs-verified.json)。旧§14.33 FAIL・§14.35 UNVERIFIED・§14.36 PASS、R1–R9/各既存suiteは全て保持し再実行0。

| 点 | 今回確認した事実・必要な境界 |
|---|---|
| 正式対象 | FE2.7.20 SHA256 `E32FD4BA2E48FCF2C84F71AA07155C63D6BFA2178BB840899307DA0DA5E5D141`、EL2.1.19fix SHA256 `0E29AF51DABD4E0EA8A315C1886E61F3ABEE62045F196EB6913D7F4DBA6CDC34`。既存依存/compat/製品/helperを含む10原物が保存済みmanifestと一致。追加artifact不足なし |
| 初回goal入口 | 原物 `TimeStopSkillGoal.timeStop(UomWither)` のoffset14 setter180→offset19 use(true)。候補はcommon5引数useだけを囲み、そのHEADのreconcileが `positive-count-without-native-dimension-stop` → unknown=true/quiet=false。新epoch条件quietを満たさず、RETURNのorigin/binding追加でもUNKNOWNは消えない。**通常初回開始の静的経路不一致**であり、未網羅の途中再延長全ケースを新必須化したものではない |
| deserialize入口 | candidate `Ownership.deserialize` はenabled/server LivingEntityならtag内容によらずfault。nativeはTimeStopCount整数keyがある時だけ読込。既存§14.31/36の空player tagによるB新fault証拠も一致。候補のcontext隔離PASSは正常zero-load後の新規保護を証明していない。empty/zeroの観測とpositive/active/malformedによる由来喪失をどう区別するか、意味を変えずにコピーできない |
| 停止根拠 | 今回依頼§34の「verified authority semanticsを変える必要」への到達。単なるcompile/import/refmap/fixture修理ではない。静的論証であり、production/native gameplayを新規実測したFAILではない |
| 写像済み設計案 | actual MinecraftServer所有context＋actual ServerLevel State、static永久履歴なし、transient origin/epoch/witness/fault/session/Grant/Lease、検証driver除外。独立login compat維持・Food Healingからnative同期二重送信なし。protocol7/S2C追加は**予定だけ**、製品はprotocol6のまま |
| vehicle | valid UOM/P vehicle ALLOWは未LOCK。今回の停止原因ではなく、勝手にALLOW/DENYの最終仕様へ確定しない。invalid/FOREIGN/UNKNOWNの既存限定DENY証拠を維持 |
| 実行・修理 | production追加/変更file0、compile/build/unit/check0、focused product/actual FE・EL integration/core回帰0、client/server/world起動0、helper作成0、implementation/helper repair各0。既存vanilla/TaCZ60、L2133、Cube49、Invader86は当時の結果であり今回PASSへ転記しない |
| 成果物・終了 | 新Jarなし。既存製品229,494 bytes/150 entries/SHA256 `5C1A716EEDE7AEB4449B36EEFFBDDA156176B16B60A1676229224AA3390DB327`。今回のnon-inclusion/reobf新build監査は非該当。外部state書込0、起動したserver/clientなしのためsave/stop/Quit非該当。保全は[照合結果](../build/verification/uom-production-20260927-123538/audit/preservation-and-readback.json)へ記録 |

**次の1作業**：この2入口だけについて、exact native初回goal開始の由来観測（setter前～use正常RETURN）と、zero/absent読込対positive/active/malformed読込の扱いを限定設計・検証し、その根拠を得てqueue #2のproduction写像・指定focused検証へ戻る。新しい認定/回復方式は今回未採用。任意のpositive UOMをknown化、UNKNOWN/faultの自動clear、native値修復、観測開始をload後へ遅らせる代用は禁止。車両方針・P条件/費用・FE6等を同時に再決定しない。

**queue #2 INCOMPLETE／残8、ownership BLOCKEDの具体因は上記2入口**。SAFE DESIGN PROVEN=NO / SOURCE DIMENSION TRANSITION VERIFIED=NO、FE6/TimeStop production NOT IMPLEMENTED、P/T IMPLEMENTATION_PENDING/購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED・既存個別gateを維持。Pam/count正式release必須・1000 PROVISIONAL/200現baseline、可逆クラフト増加の既知許容仕様も不変。#3へ進まない。

<a id="uom-entry-gate-a-partial-result"></a>
## 14.38 queue #2 Gate A入口限定検証・native起動STOP（2026-09-27 13:31 JST）

**queue #2 Gate Aは部分成立・production前STOP**。run `20260927-130322`：正規初回goal、frameなしpositive拒否、異常RETURN、参照不一致、ABSENT/ZERO・player空tag、positive deserialize拒否とterminal回帰の計9記録PASS。D4はFE内部self-attach失敗→Mixin plugin初期化失敗→optional SlashBlade classloadingでbootstrap exit1、測定UNVERIFIED。D5 NOT RUN。prior UNKNOWNの入口guardにも補助の静的残件あり。**Gate A全体INCOMPLETE／製品変更0／#1 COMPLETE維持／#2 INCOMPLETE／固定queue残8**。[結果・停止根拠§14.38](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-entry-gate-a-partial-result)。購入停止/SP保護・SAFE DESIGN PROVEN=NO・RC=NOを維持。

新root [uom-production-entry-20260927-130322](../build/verification/uom-production-entry-20260927-130322/) の補助だけを作成。[判定集計](../build/verification/uom-production-entry-20260927-130322/audit/reviewed-results.json) / [起動停止の根拠](../build/verification/uom-production-entry-20260927-130322/audit/startup-diagnosis.md)。§14.37の当時STOPと旧queue #1/R/D1-W/A1のPASS・失敗履歴は保持し、旧world再利用0。

| 入口・受入項目 | 今回の実測・限定判定 |
|---|---|
| STATIC exact goal | 同hash原物を再javap。exact `timeStop(UomWither)`、setter180→use2→common5、server分岐/no catchを構造検査。native末端callbackの構造照合も1箇所一致 |
| initial witness | 実source/server/level/UUID/type/thread/context・invocation token/orderを一時frameへ保持。setter前0、stageごとの同cap HEAD/RETURN、global/Set、other sourceを確認し、common/delegate/goal正常RETURN後だけepoch/origin/bindingをcommit。frame中のreconcile deferに限定し、native値を書換えて補正しない |
| S1 | **LIMITED PASS**。native exact goal 0→180、global/dimension true、epoch1、origin/binding1、UNKNOWN=false/fault=false、P/M準備本人にGrant session1/sequence1、server movement判定ALLOW。EmbeddedChannel/handler predicateであり実client入力・TCPのPASSではない |
| S2 | **LIMITED PASS**。raw setter positive＋common use、exact start frameなし→UNKNOWN/DENY、origin/Grant0 |
| S3 | **LIMITED PASS**。検証専用common RETURN例外注入→fault/UNKNOWN/DENY、未commit/frame破棄。自然発生例外の再現とは区別 |
| S4 | **LIMITED PASS**。実entity参照を違えたbridge呼出しを拒否し、wrong entityへsetterを委譲しない。有限bridge契約でありnative goalがその誤参照を自然生成した試験ではない |
| D1 / ordinary player empty | **各LIMITED PASS**。native customDeserializeNBT(empty)、実pre/post0、faultEvents0・origin/Grant0。load前からobserver有効。Player一般例外としてsafe化しない |
| D2 | **LIMITED PASS**。exact IntTag0/pre/post0→inert、fault0/epoch0/origin0/Grant0 |
| D3 | **LIMITED PASS**。native positive IntTag9→post9、fault/UNKNOWN/DENY。由来/旧authority再構築0 |
| D4 / D5 | D4 **UNVERIFIED**：起動失敗でwrong-type入力前。D5 **NOT RUN**。negative/Long overflowの分類コードも未実行なのでPASSへ補完しない |
| terminal限定回帰 | **LIMITED PASS**。S1のnative180→0、witness created/consumed/committed各1、globalfalse/Setなし・origin0・Grant revoke・通常移動判定復帰。旧W全matrixの再演ではない |
| 総合Gate A | **INCOMPLETE / production前STOP**。9記録PASSは上記範囲。prior UNKNOWNを開始HEADで保持する条件は静的残件あり：`InitialStart.enter` が既存quiet reconcileを先に呼ぶため、その解除経路を許さない修正/限定負対照が必要。新仕様判断ではなく承認済み入口条件への適合残件 |

### 起動STOPと機械修理の区別

- 最初のS1は測定前の補助 `UomWither.setPos/getUUID` inherited-owner reobf不備。repair **1/3**でMC基底型経由へ修正し、旧world/source/Jar/receiptを `repair01-before` とS1に保全。新S1-1以降で実測。task graphは補助compile/resources/jar/reobfだけ、製品task/旧suiteを巻き込まない。
- D4-1は**FE内部self-attach IOException → Instrumentation null → native Mixin plugin初期化失敗 → optional SlashBlade用ItemStackMixin適用 → ItemSlashBlade ClassNotFound → bootstrap exit1**。同8配置Jar/hashで先行5成功worldと一致。任意SlashBladeの不在は二次例外であり、必須追加物にしない。self-attachの間欠性・安全な対処方法は未確認。補助に偽class/外部plugin回避/Instrumentation修復を追加せず、指定の実native反例STOPを適用した。
- D4にprepare/input/normal saveはない。S1測定前失敗＋成功5worldの計6processは通常save-all flush/stop、全dimension保存、exit0。D4はexit1。7 PID全終了を[再確認](../build/verification/uom-production-entry-20260927-130322/audit/process-check.json)。HUMAN0/COMPUTER USE0、AUTOMATEDのみ、client/Prism起動0。

### 成果物・production境界・次

補助version `20260927.130322`、**76,055 bytes /42 entries / SHA256 `780277500087CE999367FBE2923E7C471DC01646A71D7C60AE6F3B7022110721`**。独立補助のclassはverification/uomのみ、外部Jar・ExampleMod・製品class非混入。修理後2回目のoffline compile/reobf成功。製品実装repair0/test helper repair0。

production mappingは§14.37表を基礎として保持、**製品変更file0**。actual AuthorityContext/原点binding/terminalFault/Session/Grantは今回verification候補だけ。ClientLease/S2C/protocol7/製品movement等は未実装、protocol6維持。製品focused・actual product FE/EL・same-JVM/reload・build/unit/check・core/外部回帰は今回NOT RUN。既存60/133/49/86等へ新PASSを加算しない。valid-P vehicleは未LOCK、今回停止原因ではない。

製品Jar現物 **229,494 bytes /150 entries / SHA256 `5C1A716EEDE7AEB4449B36EEFFBDDA156176B16B60A1676229224AA3390DB327`**。再生成なし、source/test/build.gradle/Config/購入gate不変。今回の製品reobf新実行はなし、既存Jarの非混入/hash再照合のみ。外部TimeStop stateへのobserver修復0、test native入力・結果確定後の通常native終了を区別する。

**固定queue #2：FE self-attach/plugin初期化の起動失敗への対処範囲を確定し、外部原物・TimeStop stateを修復しない起動方法からGate A残件へ復帰する。** [§14.38](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-entry-gate-a-partial-result)。SlashBladeを追加必須へ昇格しない。prior UNKNOWNをreconcile前に保持する補助修正・限定負対照と未完了D4/D5を残す。新仕様判断ではなく既承認の安全条件への適合であり、成立済み9記録や旧PASSを全再試験へ戻さない。Gate A成立後のqueue #2 production・focused実統合・必要build/回帰/Jar監査は承認済みだが、今回の実native起動反例を解消する対応は別途範囲確認が必要。#3は開始しない。

SAFE DESIGN PROVEN=NO / SOURCE DIMENSION TRANSITION VERIFIED=NO / BLOCKED - TIME STOP SOURCE OWNERSHIP。P/T IMPLEMENTATION_PENDING/購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、vehicle未LOCKを維持。Pam/count正式release必須、1000 PROVISIONAL/200現baseline、可逆クラフト増加の既知許容仕様、その他個別開始gate不変。§14.11–14.37は当時の履歴として残す。


<a id="uom-production-followup-gate-completed-client-startup-stop"></a>
## 14.39 queue #2 Gate A完了・製品限定実装／Prism bootstrap STOP（2026-09-27 14:48 JST）

**queue #2：Gate A LIMITED PASS / COMPLETE、TimeStop製品実装・限定自動検証まで完了、client bootstrapでSTOP**。run `20260927-134126`。旧9記録を維持しprior UNKNOWN・D4の3入力・D5の計5記録を追加。製品focused unit9、実FE/EL専用19ケース、build/foodHealingUnitTest/check、vanilla/TaCZ各60/60 PASS。Prism clientは3起動でworld前に停止し、最終原因は独立agentの早期ASM読込とForgeWrapperのmodule登録衝突。**製品client・製品same-JVM/保存positive再読込は未確認、#1 COMPLETE／#2 INCOMPLETE／残8**。[§14.39](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-production-followup-gate-completed-client-startup-stop)。旧Gate A残件を再開しない。購入停止/SP保護・SAFE DESIGN PROVEN=NO・RC=NOを維持。

根拠：[集計](../build/verification/uom-production-followup-20260927-134126/audit/reviewed-results.json) / [Gate A](../build/verification/uom-production-followup-20260927-134126/audit/gate-a-review.json) / [製品diff・変更一覧](../build/verification/uom-production-followup-20260927-134126/audit/production-and-tests.diff) / [Jar監査](../build/verification/uom-production-followup-20260927-134126/audit/final-jar-audit.json) / [process終了](../build/verification/uom-production-followup-20260927-134126/audit/process-final.json)。§14.38以前の成功/失敗は当時の履歴として保持。旧Gate A396ファイルhash一致、旧world再利用0。

### 起動原因・独立対処・側別の限界

- 旧成功S1-1/S2-1/S3-1/S4-1/D3-1と旧D4-1は同Java17.0.7・同runtime args hash・同8mod hash。case/runパス、PID、local公開鍵stub port、時刻は異なる。親process・temp・環境全体の当時snapshotはなく、差を復元して断定しない。[比較](../build/verification/uom-production-followup-20260927-134126/audit/startup-comparison.json)。
- FE実 `MinecraftInstHandler.getCurrent` は自身のPIDへ `VirtualMachine.attach`。JDK17はsaved propertyから初期化したprimitive boolean `ALLOW_ATTACH_SELF` で拒否する。FE `InstrumentationHelper.allowAttachSelf` がこのboolean fieldへ `Unsafe.putObject(Boolean)` を使う型不一致を確認。旧D4の直接原因はself-attach拒否→Instrumentation null→plugin失敗。**成功/失敗を分けた全環境条件やUnsafeの具体的なbit結果までは未確定**。単なるSlashBlade不足・Food Healing原因としない。
- native同梱agentはpremain時に即Class.forNameするためForgeの対象class生成前には受領先を解決できない。採用した独立startup bridgeはJVMが渡すInstrumentationを保持し、exact FE hash/manifest/method contract/code sourceに限り、native `getInstrumentation` の入口からnative `agent(String,Instrumentation)` へ渡す。native plugin・Mixin選別自体はそのまま実行。gameplay/count/Set/NBT書込0、原Jar変更0、SlashBlade追加/fake class/plugin skip各0。
- **dedicated STARTUP REMEDY LIMITED PASS**：nonnull Instrumentation、native FE plugin、Mixin準備、world、実D4入力、既知非対象ERRORだけ、通常save/stopまで確認。対処候補1、対処起動2回（初回bridge classloader参照FAIL→bootstrap検索へのJDK-only receiver分離で成功）。その後Gate A機能測定2process、製品測定5process。対処だけの回数と全起動を混同しない。
- independent agent `fe-instrumentation-bootstrap.jar` 5,849 bytes/hash `C62A38503BBD2EF5FEE5EB581AF11159EECF495FE88CDE41A5A8B73B70D6F739`、bridge1,571/hash `5E07D459BDE9936AAC2F4731F8074F7F8CA3BBC56CBC033DEC1D9EE50935393C`。D4-2時点と後続では再包装timestampだけのhash差を保全。**製品内蔵ではない。今回確認したdedicated運用の外部起動条件であり、全配布環境で不要/必要とは断定しない。**
- **client STARTUP FAIL**：Microsoft Java17.0.15＋Prism既存runtime、認証済み表示/online launch、原物10配置。1回目はSecureJarHandlerの `java.lang.invoke` inaccessible（world前exit2）。2回目は外部編集した引数がPrismの保持設定へ反映されず旧引数で起動、world/receiptなし（終了コードの個別保全は未完了）。GUIで標準 `--add-opens=java.base/java.lang.invoke=ALL-UNNAMED` を実反映した3回目は、**premainで読込済みのASM packageをForgeWrapper ModuleUtil.addModulesが再登録し `already in the unnamed module`**、PID2556/exit2。原物や製品を変更して回避しない。[エラー抜粋](../build/verification/uom-production-followup-20260927-134126/audit/client-startup-03-ui-excerpt.txt)。3回で追加起動STOP。world/prepare/goal/Lease/歩行0、ゲーム内正常Quit/保存は非該当、該当process終了を確認。

### Gate Aと製品への写像

| 項目 | 今回の結果・限界 |
|---|---|
| prior UNKNOWN | reconcile前に現StateのUNKNOWNを読み、trueならtrusted frameへ入らない。P0実exact goalはnative count180/global+dimension trueになるがepoch/origin/binding/Grant0、permissionfalse、UNKNOWNtrue、terminalFaultfalse。UNKNOWNを恒久fault化する新仕様は追加しない |
| D4 | String、negative Int、Long overflow representation各1。native pre/postは0→0、0→-1、-1→-1。元tag維持、faultEvents増加、UNKNOWN/DENY、origin/Grant0。LongをIntへ正規化しない |
| D5 | exact Int0/pre0に対し、検証入力をobserver RETURN判定直前へ置きactual post9を観測。INCONSISTENT→fault/UNKNOWN/DENY。旧P0D5-1は注入priorityの誤りで判定後に9になったfixture FAILを保全、P0 PASSは維持 |
| Gate A総合 | **旧9＋新5＝14記録でLIMITED PASS / COMPLETE**。今回14全件を再実行したという意味ではない |
| AuthorityContext | actual MinecraftServerのowned field、その配下actual ServerLevel identityでState。static server履歴なし、close/revokeあり。**製品same-JVM実再読込はclient起動前STOPによりNOT RUN**。queue #1候補の既存PASSは維持 |
| InitialStart / deserialize | exact FE2.7.20 goal setter前から正常RETURNまでだけcommit。ABSENT/ZEROはinert、POSITIVEはprovenance喪失、MALFORMED/INCONSISTENTはfault/UNKNOWN/DENY。positiveの実native deserializeで旧origin/Grant0を確認したが、今回の製品disk保存→再起動の代用とはしない |
| terminal witness | exact native callback 1→0→matching use(false)だけcommit。最終製品でcreated/consumed/committed各1、fault0、nativeglobal/dimensionfalse、origin0、Grant revoke。純unitでもorder/ref/partial/fault拒否を限定確認 |
| Session / Grant | actual player/listener/Connection/channel/context/session/epoch/sequenceへ拘束。same UUIDの新実object再接続で旧Grant拒否。EmbeddedChannelによる専用server測定であり実TCP/clientではない |
| Lease / network | S2C owning clientのみ、protocol6→7・追加packet。session/epoch/sequence/revision/expiry、disconnect/unload/newrefs/revoke/timeoutで無効。製品client側の動作確認は**UNVERIFIED**、実2clientへ拡張しない |
| movement / vehicle | native player handlerでvalid +0.25、親OFF/revokedではXYZ不変。FOREIGN/UNKNOWNのnative vehicle packetも拒否。**valid UOM/P vehicle ALLOWは未LOCK**、native挙動へ委譲し新ALLOWを決めない |
| optional | exact FE2.7.20＋EL2.1.19fixのID/version/hash、狭い構造契約だけ有効。FE/EL不在のvanilla/TaCZ実起動PASS。未対応版は静的exact gateで無介入を確認、別版実物を新たに起動したPASSではない |

製品変更32fileの完全一覧とhashはJar監査に集約。主な追加は `compat/EndingLibraryCompatibility`、`compat/endinglibrary/`、`mixin/endinglibrary/`、`client/TimeStopClientCompatibility`・`TimeStopClientLease`、`network/TimeStopLeasePacket`、専用Mixin JSON。既存変更はFoodHealingMod/PacketHandler/build.gradle/単体test入口。verification driver/receipt/run ID・独立bootstrap/shader/login patchは製品へ混入しない。schema/既存canonical/SP/購入gate/Root/Trial/L2/SWの効果処理は不変。

### 実測・修理・終了

| 検証 | 最終結果と証拠 |
|---|---|
| compile / build / foodHealingUnitTest / check | **PASS**。[最終log](../build/verification/uom-production-followup-20260927-134126/audit/product-build-02.log)。純TimeStop unit **9ケース**、既存P150/Truth53/前提142/SW47/data5000/Ammo等もPASS。assembleで既存fixture Jar taskは実行されるが、外部GameTestを実行した意味ではない |
| actual FE/EL final product | **19/19**：NORMAL11・P0 1・DATA5・FOREIGN/vehicle2。全4成功runの配置製品hashが下記最終hash一致。[集計](../build/verification/uom-production-followup-20260927-134126/audit/reviewed-results.json)。DATA内D5は固定observer HEAD/RETURN契約であり自然deserializeの二重統合PASSではない。Gate Aの実D5とは区別 |
| core回帰 | 最終sourceのForge47.2.0 userdev vanilla **60/60**、承認TaCZ **60/60**。protocol/初期化変更の影響確認、両方正常保存・自動shutdown/exit0。[vanilla](../build/verification/uom-production-followup-20260927-134126/audit/final-vanilla.log) / [TaCZ](../build/verification/uom-production-followup-20260927-134126/audit/final-tacz.log)。実FE/EL製品Jar測定はForge47.4.0。製品Jar直接のGameTestとは称さない |
| 途中の製品FAIL | NORMAL-1でnative logoutが非source playerへ行うcount0→0をfalse faultにした。未登録・未束縛・frameなし・旧新0だけをinertにする最小修正、UNKNOWN/正count/既source検証は維持。最終NORMAL-2でreconnect→terminalまでPASS。旧失敗Jar/log/receipt保全 |
| 修理回数 | startup機械修正2（bootstrap receiver classloader、client standard module open）、Gate A helper2（case配線、D5判定前注入）、production3（dangling else、import/mapping、native inert0→0）、製品fixture2（LazyOptional supplier、new E2前quiet tick）、client helper compile修正0。監査parser1はmanifest折返し/外部remap=falseの正しい読取であり製品修正ではない |
| 今回非実施 | Trial/L2/SWの実統合、旧R1–R9/player17/vehicle16/shader/login/A1/migration全matrix、旧world、人力操作。旧PASSは当時の対象Jar/範囲で保持し、新Jarへ転記しない |
| 終了 | Gate/製品の9dedicated process中、8は測定結果にかかわらず通常save-all flush/stop・全dimension保存・exit0、D4-1はbootstrap exit1/worldなし。core2processも保存/自動shutdown。client3は起動失敗で通常Quitとは記録しない。14:41の対象process0・client world0・receipt0を記録。HUMAN0、AUTOMATED＋Prism設定/起動のCOMPUTER USE |

最新製品：[foodhealing-3.0.0.jar](../build/libs/foodhealing-3.0.0.jar) **298,980 bytes /189 entries / SHA-256 `D28253FC54660AFA5D783F29A54851D6768030235090C7CC7D0B4CAB6F60F5B6`**、protocol **7**。dedicated限定検証済み候補でありclient完了/RCではない。[Jar監査](../build/verification/uom-production-followup-20260927-134126/audit/final-jar-audit.json)。独立startup agent/bridgeは製品Jarに含まない。

metadata/modid/version・Mixin構成/refmap・MC参照reobfを確認。ExampleMod/verification/helper/FE/EL/SlashBlade class・外部Jar各0。購入gate source hash不変、FE/EL含む承認外部原物と独立compat/旧helper計9artifact不変。git repositoryなし、git diff確認とは記録しない。

**固定queue #2の残確認：独立Instrumentation bootstrapのASM読込をPrism ForgeWrapperのmodule登録順へ適合させ、限定client入力/Leaseと製品same-JVM・positive保存再読込だけを完了する。** [§14.39](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-production-followup-gate-completed-client-startup-stop)。client起動3回で今回の試行を終了したため、追加修正・起動は次の承認範囲。新artifact不足・新ゲーム仕様判断は確認されていない。既存原物/製品authorityを修復せず、完了済みGate A14記録・製品19ケース・unit9・core60を理由なく再試験しない。独立bootstrapだけの修正なら製品Jar再生成は不要。#3へ進まない。

**SAFE DESIGN PROVEN=NO / SOURCE DIMENSION TRANSITION VERIFIED=NO / BLOCKED - TIME STOP SOURCE OWNERSHIP**。旧入口未証明は解消し、現在の不足は製品client・actual context/reload確認。TimeStop production **IMPLEMENTED / LIMITED AUTOMATED TESTED / CLIENT UNVERIFIED**、FE6はNOT IMPLEMENTED。P/T IMPLEMENTATION_PENDING・購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、vehicle未LOCK。Pam/count正式release必須・1000 PROVISIONAL/production200 baseline、可逆クラフト増加は既知許容・バグ修正対象外、他個別gateを維持。#3未開始。

<a id="uom-client-bootstrap-lease-fix-result"></a>
## 14.40 queue #2 bootstrap解消・Lease再送修正／実入力未成立（2026-09-27 15:51 JST）

**queue #2：Prism bootstrap解消／製品Lease送信周期の最小修正・限定自動4/4 PASS。実clientはLease受信・更新・native終了/revokeまで限定確認、実入力/移動はUNVERIFIED。** 新run `20260927-151029` と `20260927-153334`。後者はprepare1・exact goal1・allow Lease36件（初回1＋更新35）・revoke1、HP20不変。W操作は要求したが発動中の入力・位置変化を観測できずhelperが停止。今回client起動3回の上限で追加起動せず、両worldを通常保存/Quit・readonly照合・PID終了まで保全。製品same-JVM／positive disk再読込はNOT RUN。**#1 COMPLETE／#2 INCOMPLETE／残8**。[§14.40](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-client-bootstrap-lease-fix-result)。Gate A14・旧製品19/core60は当時のPASS維持。購入停止/SP保護・SAFE DESIGN PROVEN=NO・RC=NOを維持。

根拠：[集計](../build/verification/uom-production-lease-20260927-153334/audit/reviewed-results.json) / [製品変更diff](../build/verification/uom-production-lease-20260927-153334/audit/product-change.diff) / [Jar](../build/verification/uom-production-lease-20260927-153334/audit/final-jar-audit.json) / [実client記録](../build/verification/uom-production-lease-20260927-153334/audit/client-reviewed.json) / [保存読取](../build/verification/uom-production-lease-20260927-153334/audit/final-disk-readonly.json) / [process](../build/verification/uom-production-lease-20260927-153334/audit/process-final.json)。このdiffは保存sourceとの比較でありgit diffではない。§14.39以前と今回の失敗runは履歴として保全。

### 起動と最小修正

- JDK17.0.15実物のsaved-property readとFEのprimitive booleanへのUnsafe.putObjectを照合。startup propertyだけではFE自身の後続書込問題を避ける根拠がないため、agent継続。premainはJDK-onlyでInstrumentation保持・JDK-only bridgeだけ、ASM処理をnamed module登録後のexact FE対象へ遅延。元getInstrumentation本体・native agent受領・plugin/Mixin選別を残す。broad opens/export・module検査skip・fake SlashBlade・原物再包装なし。
- agent version **20260927.151029.2**（premain表示20260927.151029）、8,834 bytes/5 entries、SHA `44E2FE8579271B5A8778C65A45C4E7E143C5337572B4A116C8E612A7A6831782`。bridge1,571 bytes/2 entries、SHA `B4E07884F29C062544D353A9B19742A0B916A491A039753AB36D38C80767C7BE`。JDK-only以外のclass非混入。起動記録：[3回目前](../build/verification/uom-production-lease-20260927-153334/audit/launch-03-before.json) / [実classload順](../build/verification/uom-production-lease-20260927-153334/audit/bootstrap-events-18660.log)。premain完了時ASM/Forge/cpw/Mixin/MC/FE各0、最初のASMはnamed `org.objectweb.asm`、その後MODULES_READY→exact CONTRACT_APPLIED。
- 今回client **3 launch**。1回目PID17272はagent manifestのCan-Retransform-Classes欠落でexit2/world0。元機構に必要なmanifest項目を補正。2回目PID31056はtitle/world/LocalPlayer到達しbootstrap LIMITED PASS、旧製品D282…で初回Lease後の更新欠落を観測。3回目PID18660は修正版37E…＋同working agentで到達。外部ResourcePackInfo/既知任意target WARNは残り、全WARN0とはしない。追加起動はしない。
- **製品不具合**：EndingLibrary ServerLevelMixin.tickTimeがglobal+dimension停止中のgameTime進行を取消す。旧SessionRegistryはその時刻差で再送し、初回1秒Leaseが更新なしに失効。`leaseTicks`でserver ENDを5回計数する処理だけへ変更。送信時0へ戻す。permission/Grant/session/epoch/sequence/revision/expiry1000ms/protocol7/UNKNOWN/外部stateは変更なし。[旧client証拠](../build/verification/uom-production-remaining-20260927-151029/audit/client-reviewed.json)。
- compile/build/foodHealingUnitTest/check PASS（[log](../build/verification/uom-production-lease-20260927-153334/audit/product-build.log)）。直接影響するnative dedicated **4/4 PASS**：初回Grant、world gameTime95固定中35回更新、native終端revoke、後続10ENDで更新/Grant復活0。[fixture](../build/verification/uom-production-lease-20260927-153334/PRODUCT-LEASE-2/audit/RESULT.json)。初回fixtureはNORMAL priorityで製品END前のGrantを読んだ観測順FAIL。LOWESTへ修正して新worldで確認し、前回FAILは保持。両server通常save-all flush/stop・exit0。既存19/core60等の全suiteは再演しない。

### 実clientの成立範囲と未確認

| 項目 | 実測・判定 |
|---|---|
| 環境 / 準備 | MC1.20.1/Forge47.4.0/Java17.0.15、FE2.7.20/EL2.1.19fix＋承認依存6原物・既存shader/login compat。新instance/world、生成前Survival/Normal・doMobSpawning=false・naturalRegeneration=false、屋根/壁付き区画。prepare1、credit/spent103・SP0・P/M各1 ON・Truthなし・HP20/effects空。通常GUI購入の証明ではない |
| 初回UOM / server | exact TimeStopSkillGoal.timeStop1回、count180→native0。server903244289/level898292359/context187395022/State458617013、player1169513146/listener1217614855/Connection1428144760/channel1198472511。origin/binding1、epoch1/sequence1/session1、permissiontrue、movementDenyfalse |
| 実client Lease | LocalPlayer70518662/Connection1278416571/listener1295844041。owning ref一致、epoch1/sequence1/revision1–36、初回1＋更新35、expiry1000ms。元のactual valid()呼出しのtrue RETURNとnative canMove=falseを別観測。observerからLease発行/延長/valid呼出しの追加なし。**Lease delivery LIMITED PASS** |
| 実入力 / movement | Computer Useで通常W押下を1操作要求したが、180tick中のforward/input観測0・XYZ変化0、helperが`real input/lease/movement observed`でFAILし通常ポーズ。Sky内部の押下時刻/保持時間は記録しておらず、入力遅延と短pulse未samplingを断定分離できない。製品による移動拒否/安全反例の証拠ではない。実入力/正方向移動/移動後収束は**UNVERIFIED**。idle位置packetのhandler到達/denyfalseを移動PASSへ転記しない。correction観測0は無移動範囲のみ |
| native end / revoke | count0/globalfalse/dimensionfalse/origin0、witness created/consumed/committed各1/fault0、Grant revoke。client revision37 allowedfalse受信、Lease消去。**限定PASS**。終了後の通常歩行復帰はNOT RUN |
| A終了 / disk | 通常Save&Quit→title。実Context closed=true、states/sessions/grants各0をServerStopped hookで観測（hook内threadはRUNNABLE、titleでのTERMINATED直接snapshotは未取得）。全dimension保存、readonly lock解放と本人/source通常保存一致。native disk count **0**、positive保存ではない。最後のprocess終了確認とtitle時のthread直接観測を区別 |
| same-JVM B / positive reload | A2のpositive用goal0、world再読込0、B refs/oldStateSameActualRef/旧authority利用/positive deserialize/復活件数/B permission・Leaseすべて**NOT RUN**。未実行を0件PASSにしない。seal0、native cleanup0（自然終了済み）。旧queue #1候補結果の転記なし |
| 最終終了 / 主体 | 両client通常Save&Quit・Quit、PID31056/18660終了、保存count0・HP20・SP0/103・取得/toggle保持。dedicated PID3668/8528も通常save/stop/exit0。client起動失敗PID17272はexit2で通常Quitとはしない。HUMAN0 / COMPUTER USE=GUI・command・W要求・終了 / AUTOMATED=prepare/native trigger/readonly観測/自動fixture |

client補助version20260927.153334、26,889 bytes/15 entries、SHA `E7EDB7D5D45B03F375B3BE825CBCC60F017BBEB7CC5A74E443E313DB053DAD6D`。別Jarのみ。client補助の追加修正はreadonly title観測を実thread TERMINATED後に待つ条件、gameTime/送信revision/canonical/correction観測、null-safe deserializeと新run配線。製品authorityや外部count/flag/Set/NBTは書かない。positive/B用コードの存在を未実行結果へ加算しない。

最新製品：[foodhealing-3.0.0.jar](../build/libs/foodhealing-3.0.0.jar) **298,958 bytes /189 entries / SHA-256 `37E300EAA0663FDCDF445317C9F14F3BF13C839B21E4DD5679229B32386592E3`**、protocol **7**。[Jar監査](../build/verification/uom-production-lease-20260927-153334/audit/final-jar-audit.json)。今回変更はSessionRegistryの再送計時だけ。独立agent/bridge・client補助は非同梱。client移動/same-JVM/positive再読込未確認のためRCではない。

metadata/Mixin/refmap/reobf/非混入確認。Jar内差分はmanifestとSessionRegistry関連3classだけ、Grantの命令列は一致（source行番号差）。通常test/build.gradle/購入gate/Config/protocolは不変、承認外部6原物・shader/login compatのhash一致。旧19/core60は当時のJar結果を維持。Food Healing全source不変とは記録しない。

**固定queue #2の残確認：通常入力を180 native tickの中で確実に観測できる補助制御へ限定修正し、新規runで実入力→native終了/revoke→通常入力復帰→同JVM A→title→B 1回＋実positive disk再読込を完了する。** [§14.40](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-client-bootstrap-lease-fix-result)。候補は初回Lease観測直後の通常PauseScreenと、再開から短い通常前進までの一連操作。pause中のLease期限切れを延長せず、再開後の製品heartbeatを使う。値・期待値・authority semanticsを変えず、人間の厳密tick操作を要求しない。今回起動3回に達したため追加起動は次の承認が必要。失敗runの再利用/reset/receipt削除なし。bootstrap/Lease専用4ケース/Gate A/旧19/core60等を理由なく再演せず、#3は開始しない。追加外部artifact・新仕様判断の不足なし。

これはauthority semanticsの再設計・新artifact不足・native安全反例によるSTOPではなく、**実入力未成立＋今回client起動3回上限**。失敗guard/receiptを消して続行しない。queue #2 INCOMPLETE、残8、**BLOCKED - TIME STOP SOURCE OWNERSHIP（残る具体条件：製品入力/same-JVM/actual positive disk reload）**。SAFE DESIGN PROVEN=NO / SOURCE DIMENSION TRANSITION VERIFIED=NO、P/T IMPLEMENTATION_PENDING/購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、valid-P vehicle未LOCK、FE6未着手。他個別gateと可逆クラフト増加の既知許容仕様を維持。

<a id="uom-final-input-pause-result"></a>
## 14.41 queue #2最終残確認 — pause/resume成立・実入力UNVERIFIED（2026-09-27 18:19 JST）

**#2 INCOMPLETE／固定残8。** 実通常入力をclient tickで観測できず、利用者指定のSTOP条件と3 launch上限に従った。製品による移動拒否の実証ではない。§14.40のbootstrap/Lease修正・4/4、Gate A・旧製品19/core60等は当時のPASSを保持し、再実行しない。

根拠：[全run集計](../build/verification/uom-production-final-20260927-180600/audit/reviewed-results.json)／[最終client実測](../build/verification/uom-production-final-20260927-180600/audit/client-reviewed.json)／[CU呼出し時刻](../build/verification/uom-production-final-20260927-180600/audit/computer-use-input.json)／[保存読取](../build/verification/uom-production-final-20260927-180600/audit/disk-final-readonly.json)／[全PID・lock](../build/verification/uom-production-final-20260927-180600/audit/process-final.json)。生journal/receiptは各runの `CLIENT/audit/` に残す。

| 今回のrun / helper | 実施結果・修理経緯 |
|---|---|
| 20260927-174200 / v20260927.174200 / PID25060 | 1回目。入力前のtool間遅延で180tickが終了し、W未送信。INPUT UNVERIFIED。SHA `7840D0317E32C65F43460E18BDB6D665940FF5A330FD2D644EADAB1F8EDD7FCA`、33,245 bytes/16 entries。保存/終了保全 |
| 20260927-175400 / v20260927.175400 / PID30980 | 修理1：native開始前quiet待機40→600 END、native RETURN観測をcancellable製品bridgeより先のpriority400へ。180 native tickは変更なし。CU通常Escでcount177停止。ClientTickは実pause中に止まるため、pause完了観測へ進めずW未送信。通常Esc再開後native終端、INPUT UNVERIFIEDで保存/終了。SHA `EB486952C360D47DF1D1B7348616591FD73A7C636F962C0A8E82EAC7A47F6A47`。pause中Save&Quitのmouse操作で退出しなかった事実はあるが、原因/製品bugは未確定 |
| 20260927-180600 / v20260927.180600 / PID21652 | 修理2：pauseのreadonly観測をRenderTickへ。最終補助33,563 bytes/16 entries、SHA `EBA43A0851D90B3E59BE11DE398D16B2456BC139FAAD71C361273B8CD4B0A3E2`。実pause→再開fresh Lease成立後、CU通常Wを有限3呼出し。181 client tickで前進入力0のままnative終端。INPUT UNVERIFIED、製品movement FAILとは判定しない |

helper初回作成＋修理2 cycleの計3回、専用offline compile/reobf各SUCCESS。task graphは専用source set/reobfのみ（cached MCP taskを含みnetwork取得なし）、製品compile/build/unit/check/GameTest0。既存working agent v20260927.151029.2・8,834 bytesとbridge1,571 bytesは§14.40のhash一致のまま再利用。各runの `bootstrap-events-<PID>.log` にPREMAIN_END→named FIRST_ASM→MODULES_READY→CONTRACT_APPLIEDを確認、bootstrap再設計/修理なし。

| 最終runの受入項目 | 実測・判定 |
|---|---|
| 環境・prepare | 新instance/world、MC1.20.1/Forge47.4.0/FE2.7.20/EL2.1.19fix・承認依存。生成前GUIでSurvival/Normal、自然湧き/自然回復false。壁/屋根付き区画、本人初期座標[4.5,-60,-0.5]はsetter未使用。正面最大移動1.199999988079071 blocks。prepare1・credit/spent103・SP0・P/M各1ON・Truthなし・HP20/effects空。通常GUI購入の証明ではない |
| E1 / actual A refs | exact native UOM goal1、count180、epoch1/sequence1/session1。server396892741、level1413731817、Context1916166990、State537165819、player2122629504、listener996995914、Connection1073015061、channel922717951。参照の数値表示をB actual ref比較PASSにしない |
| 初回Lease / pause | revision1/current本人refs/実valid RETURN true。CU EscでPauseScreen、server tick1592/count176の不変を504,195,700 nsのreadonly観測で確認。**pause限定PASS**。停止中のTTL延長・renewなし |
| resume fresh Lease | CU Esc再開、revision2 > pause revision1、同epoch1/sequence1、current refs/validtrue、native canMovefalse、source count174/globaltrue/dimensiontrue/server permissiontrue。screen none/pausedfalseでINPUT_WINDOW_READY。**fresh Lease限定PASS** |
| actual input / movement | 通常W API呼出し3回。各呼出し開始/完了を記録したが、43–44msはAPI往復時間であり実キー保持長ではない。181 client tick全てkeyUp false/forwardImpulse0、REAL-INPUT-OBSERVEDなし。server位置変化0、client位置変化0。idle packetのhandler9件はdenyfalseだがacceptedMoving0。correction0も無移動範囲のみ。**INPUT / movement / 移動後収束 UNVERIFIED** |
| E1 native end/revoke | count0/globalfalse/dimensionfalse/origin0、witness created/consumed/committed各1・faultfalse、Grant失効。allow revision1–36（初回1＋更新35）、revoke37 allowedfalse→ClientLeaseなし。**限定PASS**。通常W復帰はINPUT成立前STOPのためNOT RUN |
| E2 / positive save | E2 exact goal0、new epoch/sequence未測定。通常最終Save&Quit時count0、実disk count0（正の保存試行ではない）。**positive disk UNVERIFIED、E2 NOT RUN**。値修復/NBT編集/native途中終了による見せかけなし |
| A終了 | 通常Save&Quit、実titleでA thread TERMINATED/alivefalse、Context closed=true、states/sessions/grants各0、Leaseなし。同Minecraft/PID21652/JVM start1790500038742。all dimensions saved・readonly file lock解放 |
| same-JVM B / positive deserialize | B起動0。B server/level/Context/State、oldStateSameActualRef、deserialize pre/post、旧origin/binding/Grant/session/witness利用、新Grant再構築/B permission/Leaseはいずれも**NOT RUN**。未測定を0件PASSにしない |
| seal / cleanup / 最終終了 | seal0、B native cleanup0・cleanup後state未測定。E1自然終了後の通常保存→title→Quit、PID21652終了。3run全て同様に通常保存/Quit・count0/HP20/SP0/103/取得toggle保持・lock解放を確認。旧run再入場0・receipt削除0 |

**入力の限界**：現Sky `PressKeyInput`は`window/key`のみで、継続押下のduration/keyDown/keyUpは公開APIにない。今回の通常キー操作が実client tickへ届かなかった原因を、pulse長不足・polling位相・native入力経路のどれかと断定できる証拠はない。observerはキー/入力/stateを書かず、valid追加呼出し/packet生成/位置・velocity setter0。瞬間入力を観測済みに補完せず、正のinputなしで製品movement反例とも判定しない。

最新製品：[foodhealing-3.0.0.jar](../build/libs/foodhealing-3.0.0.jar) **298,958 bytes /189 entries / SHA-256 `37E300EAA0663FDCDF445317C9F14F3BF13C839B21E4DD5679229B32386592E3`**、protocol **7**。[今回の現物照合](../build/verification/uom-production-final-20260927-180600/audit/final-jar-audit.json)。今回は製品source/test/build.gradle/Jar・購入gateを変更せず、製品build/test再実行0。独立bootstrap/bridge・検証補助は非同梱。前フェーズのLease修正・4/4等は当時のPASSを保持。

実施主体：**COMPUTER USE**=新instance起動/world GUI/prepare・arm command/Esc/W呼出し/Save&Quit/Quit、**AUTOMATED**=一度の準備・exact native trigger・readonly観測/集計、**HUMAN=0**。製品source/test/build.gradle・直前旧run/worldを含む開始時snapshot928ファイルはhash差分0。3つの失敗runも保全し、証拠の追記以外は書き換えない。製品修正cycle0、既存suite再実行0。

**次の1作業：queue #2の実入力手段を確定し、別承認後に未完了lifecycleだけを新runで実施する。** [§14.41](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-final-input-pause-result)。通常ポーズ・RenderTickでの停止観測・再開fresh Leaseは今回成立したため再設計しない。現Computer Useの`press_key`はkey/chordのみで保持時間指定がなく、今回3回のW呼出しでも実tick入力を得られなかった。単なる同じ短pulse再試行は採らず、対応する通常キー保持手段（利用可能APIで不可なら本人の1操作を別途調整）と`REAL INPUT OBSERVED`の確認を先に成立させる。MC input field/GLFW/packet/位置/外部stateの代入は禁止を維持。成立後のみE1移動→native終了後の通常入力復帰→E2 positive保存→同JVM B1回→非復活/seal/必要時native cleanup1/通常終了へ進む。今回起動3回上限に達したため追加runは今回実行しない。追加外部artifact・新仕様判断の不足なし。旧run再利用/resetや既存suite再演、#3開始はしない。

**BLOCKED - TIME STOP SOURCE OWNERSHIP（製品actual input/移動・通常復帰、same-JVM B、actual positive disk reloadが残る）**。SAFE DESIGN PROVEN=NO / SOURCE DIMENSION TRANSITION VERIFIED=NO、P/T IMPLEMENTATION_PENDING・購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、valid-P vehicle未LOCK、FE6 NOT IMPLEMENTED維持。Pam/new Shokugi/Flight・他開始gateは変更せず、#3へ進まない。可逆クラフト増加の既知許容仕様・バグ修正対象外を維持。


<a id="uom-input-capability-preflight"></a>
## 14.42 queue #2 INPUT CAPABILITY PREFLIGHT — 正式hold APIなし（2026-09-27 20:10 JST）

**INPUT METHOD PREFLIGHT: AUTOMATED HOLD NOT AVAILABLE。** 起動前に正式APIだけを確認した結果であり、Food Healingのmovement FAILではない。§14.39–14.41と[前run集計](../build/verification/uom-production-final-20260927-180600/audit/reviewed-results.json)の限定PASS・未確認・失敗履歴を保持する。

### 正式APIの確認結果と採用方法

- 現行[Computer Use API定義](<USER_HOME>/.codex/plugins/cache/openai-bundled/computer-use/26.924.22138/docs/api.md)の`PressKeyInput = { key: string; window: Window; }`にはduration/hold・down/up指定がない。
- 実際にロードされた`@oai/sky`の公開action名をREAD ONLY列挙した。`target=windows`、`activate_window, click, drag, get_window, get_window_state, launch_app, list_apps, list_windows, perform_secondary_action, press_key, scroll, set_value, type_text`。別keyDown/keyUp/hold actionはなく、現在callableなtool一覧にも正式OS hold機能なし。現在の`cua_repl` schemaはnative computer APIs disabledであり、browser APIをMinecraft入力へ転用しない。
- **AUTOMATED**は今回のschema/action照合だけ。OSキー送信・COMPUTER USEによるゲーム操作・**HUMAN INPUTはいずれも未実施**。保持時間・key tick数は未測定。短press再試行、未公開API推測、MC input/GLFW/packet/位置setter、外部入力注入は使用しない。
- 採用する次回方式は**HUMAN物理W保持**。依頼は「次回、画面に『REAL INPUT READY / Wを押してください』と表示・報告した時だけ、物理キーボードのWを約1秒押し続けて離してください」。今は起動も入力依頼の実行も行わない。次回もreadonly実client tickで`forwardKey=true`または`forwardImpulse>0`を確認するまでは入力/移動PASSにしない。

### 今回の実施境界・結果

| 項目 | 今回の結果 |
|---|---|
| launch / run / helper | Minecraft0・Prism0。新run/instance/worldなし。helper作成・変更・compile/reobf0。旧runのworld/receipt/journal未操作 |
| 製品・補助の現物 | 製品SHA-256 `37E300EAA0663FDCDF445317C9F14F3BF13C839B21E4DD5679229B32386592E3`を再測定して一致（既存298,958 bytes/189 entries/protocol7）。既存helper v20260927.180600、SHA-256 `EBA43A0851D90B3E59BE11DE398D16B2456BC139FAAD71C361273B8CD4B0A3E2`も一致。今回使用した新helperではない。product/helper repair各0、製品source/test/build.gradle/Jar/Config・購入gate変更0、build/test0 |
| E1 / pause / fresh Lease / native end / revoke | 今回はすべてNOT RUN。§14.41までの成立を維持し、今回の新規PASSへ加算しない |
| keyDown / hold / actual input / movement / restore | 実操作・測定なし。key/forward positive tick、packet件数、guard、server/client移動量・収束はNOT RUN/未測定。前runの0値を今回の値へ転記しない |
| E2 / positive保存 / A終了 | exact goal、epoch/sequence、退出前count、serialize positive、実disk count、A State/server/level実参照・thread/titleはNOT RUN。positive disk UNVERIFIEDを維持 |
| same-JVM B / deserialize / authority | PID/JVM継続、B実参照、oldStateSameActualRef、positive deserialize pre/post、旧origin/binding/Grant/session利用数、新Grant、B permission/LeaseはNOT RUN/未測定。未測定を非復活0件PASSにしない |
| seal / cleanup / final save/Quit/PID | 今回の対象runなし、NOT RUN/対象なし。正常終了処理が必要な新processを起動していない。既存runの通常終了証拠は保持 |

### 次の1作業と維持するgate

**次の1作業：queue #2の未完了lifecycleを、次回の承認後にHUMAN通常W保持で確認する。** [§14.42](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-input-capability-preflight)。自動hold手段の有無は確認済みであり、同じ短い`press_key("W")`の再試行は行わない。次回は`REAL INPUT READY / Wを押してください`の表示・報告時だけ、本人が物理Wを約1秒保持して離す。本人操作を事実として確認し、readonly client tickで入力成立を観測してから移動を判定する。その後は承認範囲のE1移動→通常入力復帰→E2 positive保存→同JVM B1回→非復活/seal/必要時native cleanup1→通常保存/Quit/PID終了へ進む。今回は利用者指定のno-hold分岐で起動せず終了。追加artifact・新仕様判断は不要。製品権威/期待値/外部stateの変更、旧run再利用、既存suite再試験、#3開始はしない。

今回止める理由は**正式AUTOMATED HOLD API unavailable**だけ。追加artifact・本人再認証・新仕様判断の不足は検出していない。前runのpause/fresh Lease方式を再設計せず、次回のHUMAN操作を実施済みとも扱わない。

**#1 COMPLETE／#2 INCOMPLETE／残8**。SAFE DESIGN PROVEN=NO、SOURCE DIMENSION TRANSITION VERIFIED=NO、BLOCKED - TIME STOP SOURCE OWNERSHIP、P/T IMPLEMENTATION_PENDING・購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、valid-P vehicle未LOCK。FE6等#3以降を開始しない。食料生産の極意による可逆クラフト増加は既知かつ許容仕様・バグ修正対象外を維持。変更は本節・Status現在欄・TEST_PLAN該当箇所だけで、旧§14.41本文は保持。

<a id="uom-human-input-handoff-unverified"></a>
## 14.43 queue #2 HUMAN入力未実施 — handoff未成立（2026-09-27 20:53 JST）

**HUMAN INPUT NOT PERFORMED / INPUT UNVERIFIED。** 新run `20260927-203225` はprepare1・E1 native goal1、pause→fresh Lease→REAL INPUT READYまで成立。本人回答は「Minecraftが前面に出たため、Codex側の入力タイミング指示をユーザーが確認できなかった」。180 client tickでkey/forward positive0・移動0。入力の受け渡し未成立であり製品movement FAILとはしない。E1自然終了/revoke・通常保存/Quit/PID終了は確認。restore/E2/positive disk/same-JVM B/sealは未実施。補助だけ限定offline compile/reobf、製品不変。**#1 COMPLETE／#2 INCOMPLETE／残8**。[§14.43](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-human-input-handoff-unverified)。

証拠：[集計](../build/verification/uom-production-input-final-20260927-203225/audit/reviewed-results.json)、[本測定のreadonly保存](../build/verification/uom-production-input-final-20260927-203225/audit/disk-final-readonly.json)、[最終process確認](../build/verification/uom-production-input-final-20260927-203225/audit/closure-final.json)。旧§14.41/14.42と各suiteの結果は当時の証拠として保持。

| 項目 | 今回の実測・判定 |
|---|---|
| 環境/補助 | run20260927-203225、instance `FHR_UF_20260927-203225`、world `UP-20260927-203225`。MC1.20.1/Forge47.4.0・承認FE/EL/依存/既存compat、外側10Jar。新helper v20260927.203225、34,154 bytes/16 entries、SHA-256 `F5A03F05B3F40D3BB728E2A6410E520E214099A31F508ACACEAEB88C2349A73B`。run/path/versionとreadonly READY記録/HUDだけ変更 |
| build境界 | sandboxのnative DLLアクセス失敗後、承認済みlocal Gradle8.1.1/-g既存cache/--offlineでtask graph確認・補助compile/reobf成功。製品jar/reobfJar/build・既存suite0、測定後補助修正0。原Jar/bridge/bootstrap不変 |
| 安全準備 | 生成前GUIでSurvival/Normal、doMobSpawning=false/naturalRegeneration=false。閉鎖歩行区画・source別室、最大正面移動1.199999988079071 blocks。prepare1、HP20/MAX20/effects空、P/M各Lv1ON、SP0/使用済103、Truthなし。位置/state setterで測定値を修復しない |
| E1/pause/fresh Lease | exact native goal1/count180/epoch1/sequence1/session1。通常Escでcount177を503,198,100ns不変観測。再開fresh revision2・current refs/validtrue/native canMovefalse/permissiontrue/movementDenyfalse/count174でREAL INPUT READY、画面表示も確認。限定成立 |
| HUMAN / actual input | 本人確認 **HUMAN INPUT NOT PERFORMED / INPUT UNVERIFIED**。「Minecraftが前面に出たため、Codex側の入力タイミング指示をユーザーが確認できなかった」。READYから最終sample8.6538837秒、180client tick中key/forward positive0。自動W送信0。人間が入力したことにも製品が入力を拒否したことにもしない |
| movement/correction | server/client移動0、acceptedMoving0。move-send/handler HEAD各39はidleを含みE1移動成功件数でない。correction0も無移動範囲。**movement/収束 UNVERIFIED**。helperのFAILURE receiptは入力未確認によるguard停止であり製品FAILでない |
| native end/revoke | count0/globalfalse/dimensionfalse/origin0、witness created/consumed/committed各1・faultfalse、Grant0。allow36/revoke1（revision37）、client Leaseなし。**LIMITED PASS** |
| 残lifecycle | 通常入力復帰/E2 goal/new epoch/sequence/positive disk/same-JVM B/positive deserialize/非復活は**NOT RUN**。seal0/native cleanup0。今回の保存count0をpositive保存PASSにしない |
| 本測定終了 | 通常Save&Quitで全5dimension保存、titleでserver thread TERMINATED・Context closed/maps0・Leaseなし。readonly diskはHP20/SP0・103/P&M ON/sourcecount0、保存lock解放。通常Quit・PID1888終了。新world入場1回、B再読込0 |
| 予期しない追加起動 | 本測定終了後、launcher確認中20:45:30にPID32968起動を検出。開始主体は未確認。first-write `FileAlreadyExistsException`でmod構築停止、integrated server/world開始logなし。receipt削除/再prepare/再測定なし。通常OS close→20:50:20.904 Stopping!→PID32968終了、今回開いたPrism33884も終了。既存background Prism26748は操作せず。観測Minecraft起動合計2＝本測定1＋起動段階で停止1 |
| 不変確認 | 製品298,958 bytes/189 entries/SHA-256 `37E300EAA0663FDCDF445317C9F14F3BF13C839B21E4DD5679229B32386592E3`、protocol7。開始時保全249ファイル・配置10Jar hash差分0。production/test/build.gradle/購入gate/SP保護/外部native state/期待値不変、製品build/test再実行0 |

実施主体：**HUMAN**＝入力未実施の本人回答のみ。**COMPUTER USE**＝通常GUI/command/ポーズ/保存/終了（W0）。**AUTOMATED**＝検証補助のprepare/native trigger/readonly observer・限定build/集計。本文の期待値を実測へ補完しない。

**次の1作業：queue #2のHUMAN入力の受け渡しだけをREAD ONLYで具体化する。** [§14.43](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-human-input-handoff-unverified)。ゲームを前面にする前に本人が一連手順を確認でき、ゲーム内READYを認識して物理Wを実行できる手順を決める。native180・Lease/権威・合格条件は変更しない。今回のWは未実施であり、同runの再arm/再入場/receipt削除や短い自動Wで再試行しない。通常入力復帰の独立確認に追加の本人操作が必要なら、今回の「1操作のみ」の承認を拡張せず次回の実行範囲として明示する。手順と必要な本人操作数を合意してから、新runの未完了lifecycle実行を承認対象とする。追加artifact/再認証/新仕様の不足は検出していない。今回は追加起動・#3へ進まない。

**#1 COMPLETE／#2 INCOMPLETE／残8、#3 NOT STARTED**。SAFE DESIGN PROVEN=NO、SOURCE DIMENSION TRANSITION VERIFIED=NO、BLOCKED - TIME STOP SOURCE OWNERSHIP。P/T IMPLEMENTATION_PENDING・購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、valid-P vehicle未LOCK、他の個別開始条件を維持。食料生産の極意による可逆クラフト増加は既知かつ許容仕様・バグ修正対象外。

<a id="uom-human-input-handoff-design"></a>
## 14.44 queue #2 Minecraft画面内の入力合図とHUMAN 2操作（2026-09-27 21:03 JST）

**DESIGN ONLY / 未実装・未実行。次回は本人の「Wを約1秒保持→離す」を計2回とする。** §14.43のrun20260927-203225はHUMAN INPUT NOT PERFORMED / INPUT UNVERIFIEDのまま保全し、再arm/reset/再入場しない。今回は3文書と変更前backupだけ更新し、新run/helper/compile/reobf/ゲーム/試験/入力依頼を開始していない。

### 読取根拠と現行helperとの差

- [InputAudit.java](../build/verification/uom-production-input-final-20260927-203225/client-helper/src/main/java/verification/client/InputAudit.java) `client` stage3/4、`hud`：既に`RenderGuiEvent.Post`と`GuiGraphics.drawString`で左上(8,24)にREADYを描く処理がある。「前runはチャット以外に表示なし」ではない。前runで人間がその合図を認識した証拠はなく、本人回答は指示を確認できなかった。次回は大きく識別できる中央帯・段階番号・事前説明を組み合わせる。
- [ProductClientProbe.java](../build/verification/uom-production-input-final-20260927-203225/client-helper/src/main/java/verification/client/ProductClientProbe.java) `prepare/arm/tick/positive`：E1の移動・解放後収束後にstage4へ移る。stage4の新しい`normalInput`、`normalConverged`、serverの`normalX/Z`からの移動が必要。`positive()`は`normalMoved`なしではE2を開始できない。
- [Observer.java](../build/verification/uom-production-input-final-20260927-203225/client-helper/src/main/java/verification/client/Observer.java)：readonly key/forward・native/valid RETURN・packet HEAD/RETURN・correction・保存を記録。表示から製品`valid`等を追加呼出しして観測を作らず、実呼出しのRETURNとserver END snapshotを読む。
- API実在確認は既存MC1.20.1のlocal source Jar `<LOCAL_GRADLE_HOME>/caches/forge_gradle/minecraft_user_repo/net/minecraftforge/forge/1.20.1-47.2.0_mapped_official_1.20.1/forge-1.20.1-47.2.0_mapped_official_1.20.1-sources.jar` 内で実施。これは既存helperのcompile classpath版であり、runtime Forge47.4.0のsourceと取り違えない。採用するRenderGuiEvent.Post/getGuiGraphics/drawStringは前runの47.4.0で描画実績あり。新しい表示配置の実画面確認は次回に残る。外部取得なし。

### 表示候補の比較

すべてhelperだけで実装可能で製品Jar変更不要。共通の開始条件は下記READY predicate、終了条件は入力段階終了/条件喪失/timeout/disconnect。表示をPASS証拠にはしない。server発信だけではclient Leaseの新鮮さを証明できないため、serverメッセージ単独でREADYを決定しない。

| 候補・実在API | 表示側・state境界 | 終了/古い表示の扱い・評価 |
|---|---|---|
| chat：`Minecraft.gui.getChat().addMessage(Component)`、server `ServerPlayer.displayClientMessage(Component,false)` | clientまたはserver→client。chat履歴のみでgameplayを書かないが、chatを開かせると入力が奪われる | 過去READYが履歴に残る。run/段階付きでも古い合図を再読でき、即時取消に不向き。事前案内/結果記録に限定、実行合図には不採用 |
| action bar：`Gui.setOverlayMessage(Component,false)`、server `displayClientMessage(Component,true)` | client推奨、server経由も可。GUI overlay fieldsを変更。画面を開かず入力/TimeStop権威へ介入なし | 実sourceでは60 GUI tick表示。失効時に自分の文面を取り下げる管理が必要。他MODと同じslotを上書きし得る。古い表示・時間停止中のGUI tickへの依存から第一候補にしない |
| title/subtitle：`Gui.setTitle/setSubtitle/setTimes/clear` | client。title fields/timerだけ、screenやpauseは変更しない | 中央で見やすいが共有slot/clearは他のtitleにも作用。実条件喪失時の消去と旧run破棄が必要。通常timerだけに任せない。代替候補 |
| boss bar：`ServerBossEvent(Component, BossBarColor, BossBarOverlay)`、`addPlayer/removeAllPlayers` | server→対象client。別UUIDの表示stateとpacketだけ。darken/music/fogを有効にしない | run固有eventを自分だけremove、disconnect/server stop時破棄。client READY判定との同期が余分に必要。本人1人の合図には過剰 |
| **推薦：helper専用HUD帯** `RenderGuiEvent.Post.getGuiGraphics()`、`GuiGraphics.fill(int,int,int,int,int)` / `drawString(Font,String,int,int,int,boolean)`、`guiWidth/guiHeight` | **client renderだけ**。黒背景＋中央上部の2〜3行、段階番号/文言/短run ID。色だけに依存しない。共有title/chat slotを使わず、入力・native state・Leaseを書かない | 毎frame、最新の不変snapshotとclient条件が有効なときだけ描く。無効時は描かず次frameで消える。persistent GUI message/timerを残さない。world/player/connection/run identity変化・B再読込・終了で表示状態破棄。新runに旧receiptを読んで復元しない |

表示には新しいScreenを開かず、setScreen/pause・key state/GLFW/packet生成・位置/velocity setter・TimeStop count/TTL更新を使わない。文字長は画面幅に合わせて折り返し、F1等でHUD非表示ならREADYを開始しない。描画イベント実行は「描画処理を呼んだ」証拠であり、本人の認知や実入力の代わりではない。停止時の通常ポーズは表示と分離した既存安全終了処理だけ。

### 次回の正式な本人手順（今回は操作不要）

Minecraftを前面にする**前**に以下を共有し、本人が2操作の説明を確認して画面を見る準備ができた返答を得てから進める。この返答はW実測には数えない。以後、チャット確認/Alt+Tabを要求しない。

1. 画面の「待機・まだ操作しない」の間は触らない。Codexが準備と通常pause/resumeを担当する。
2. **「1/2 REAL INPUT READY — Wを約1秒押して離す」**がMinecraft画面中央上部に出たときだけ、Wを約1秒保持して離す。マウスや他のキーは触らない。
3. 入力検出後は「入力検出・約1秒でWを離す」、解放後は「1/2終了・触らず待つ」へ切り替える。表示の再点灯を新しい入力依頼にしない。
4. **「2/2 NORMAL INPUT READY — Wを約1秒押して離す」**が出たときだけ、もう一度Wを約1秒保持して離す。
5. 「2/2終了・以後操作不要」または「入力確認を停止・操作不要」になったら何もせず待つ。通常保存・再読込1回・終了はCodexが担当する。

**物理操作A/Bの計2回が必要**。Aのkey releaseはE1中の収束条件であり、終了後の`normalInput`と新しい`normalX/Z`からの移動を満たさない。無入力/Lease消去だけを通常歩行復帰へ昇格しない。再認証など実際の外部要因が出た場合は別途停止・本人対応であり、予定の歩行操作に隠して追加しない。

### 2回ともWで測定できる安全区画

現行`prepare()`は前壁が固定で、Aで壁に達するとBのWによる移動量が0になる。2回目を後退キーへ勝手に変更せず、次回承認範囲に**verification world内の二段階停止壁**を含める。

- prepare時に幅1blockの床/両側壁/不透明屋根/外周停止壁を前方へ1block長く確保し、その内側にA用停止壁を置く。Aでは従来同様、collision boxから最大水平可動距離0.05以上2未満（目標0.5〜1.5）をreadonlyで確認。sourceの閉鎖区画と分離。
- A成功・native終了/revoke・キー解放・停止を確認した後だけ、Bのforward余地を調べる。既存壁で0.5〜1.5block確保できるならそのまま。足りない場合だけ、外周壁が完成していることを確認してA用の内壁を1回開く。`Level.setBlockAndUpdate`は現行prepareで実在使用済み。今回実行しない。
- 壁変更はserver側の検証準備だけ。player bboxと重ならない所有blockに限定し、床/屋根/側壁は維持する。block更新のclient反映、本人位置不変・onGround・HP/effects・水平静止・server/client一致を読取確認してからB表示。B終了までそれ以外の区画変更なし。
- Bも実bboxから前進余地・横方向の最大変位を含め最大水平距離2未満を再計算する。条件が成立しなければBを表示せず停止。壁の位置で測定余地を作り、本人をtp/setterで戻さず、既存`normalX/Z`を動かして合格に合わせない。両段階の既存movement閾値を変更しない。

### READYの開始・終了・記録条件

**共通**：run/property/実game path・world path・初回world context・本人UUID・sourceUUID/実参照・対象Jar hash一致、prepare receipt成立、helper failed=false、canonical P/M Lv1ON/SP0・103/正常データ、HP20/MAX20/effects空、安全床/壁/進路・接地・no unrelated damage、対象window active/screenなし/非pause/HUD可視。client/server snapshotは当該run/段階/世代で、server END heartbeatとclient observerが150ms以内に更新、記録I/O失敗なし。clientとserverの参照を別スレッドから直接操作せず、serverが作るimmutable snapshotを読む。未設定/取得不能/古いsnapshotなら非READY。

**A開始**：E1 exact native goal1成立、expected UOM sourceがpositiveでnative global/dimension true。既存pauseStable→resume fresh Lease、実native RETURN=false/実valid RETURN=true（各150ms以内）、clientのcurrent player/listener/connection/**level**実参照一致、server current session/Grantとleaseのplayer/dimension/session/epoch/sequence一致・revision>pauseRevision。server UNKNOWN=false、terminalFault=false、origin/bindingが予定sourceのみでFOREIGNなし、permission=true/movementDeny=false。現在helperのsnapshotはorigin件数中心なので、次回はID/type/actual source参照とheartbeatをreadonly記録へ追加する。表示から権威APIを追加実行して状態を作らない。キー解放/forward0の開始状態を確認し、READY表示前の押下をAの成功へ流用しない。

**B開始**：Aの入力/移動/解放/既定収束が成立してstage4、E1 count0/globalfalse/dimensionfalse/origin0、witness1/1/1/faultfalse、old G1 revoked/現在Grantなし・client Lease消去、UNKNOWN/foreignなし。実native canMove RETURN=trueと通常movement guard非拒否を観測。E1中のheld keyを引き継がず、release/forward0・静止を連続3client tick確認。上記区画/同期確認後だけB表示。Aのvalid LeaseをBに要求しない（Bはnative終了後の通常入力を試す）。

**終了**：positiveを観測したら当該「Wを押す」表示を一度で閉じ、解放案内→待機表示にする。finished/timeout/failed/identity変更/条件喪失/画面非active時はREADYを即時非表示、同じ段階の再試行を促す表示は出さない。disconnect・title・same-JVM BではA/B READYを再表示しない。ready-recorded等の単発guardは新run限定、旧receipt/journalのresetなし。

**証拠**：各段階のCONDITIONS_READYと最初の実renderを別記録（run/phase/nano/clientTick/serverTick/source/identity/epoch/seq/revision/条件operand）。その後のreadonly `forwardKey==true OR forwardImpulse>0`、解放、client/server delta、actual packet/guard、correction、既定収束を個別判定する。画面表示や本人申告だけでinput/movement PASSにしない。画面内表示の視認性・2段階壁は次回の実行前確認対象であり今回PASSではない。

### Timeoutと停止分類

- A：最初のREADY実renderから待つが、**E1 native180は一切延長せず自然終了を絶対上限**とする。native終了の方が先ならその時点で締切。追加のwall-clock watchdog上限15秒（最初のREADYから、pause/focus変更でresetしない）でobserver停止/進行停止も検出する。READYに至らない場合もE1開始から15秒で準備/観測未成立として停止する。前runの8.65秒は実測値であり次回の固定待機時間とはしない。
- B：native終了後の区画/同期準備はstage4開始から30秒上限。READY実render後、入力待ちは30秒上限。positive後の解放・収束は10秒上限。時計はmonotonicで、再表示/reset/再armにより延長しない。Aは解放・収束もnative終了前という既存条件を維持。
- positiveなしtimeout：**INPUT UNVERIFIED**。本人未操作と確認できた場合は **HUMAN INPUT NOT PERFORMED / INPUT UNVERIFIED**。申告不明や「押した」との申告がある場合はHUMAN REPORTを別保存し、観測0だけで人間未操作を事実認定しない。製品movement FAILへ置換しない。
- positiveありで動かない/収束しない場合は入力なしtimeoutと分け、既定のmeasurement FAIL/UNVERIFIEDとして原因証拠を保存。READY前の誤入力、key解放なし、identity/authority/safety/observer不整合は条件を緩めずSTOP。
- STOP時は合図を消し「操作不要」、次のgoal/B/sealへ進まず通常ポーズ→Save&Quit→readonly保存→Quit→PID確認。native途中終了のためのstate修復/再arm/receipt削除なし。watchdogによる途中保存値はそのまま記録し、未実施positive試験のPASSにしない。render/game自体が固まる場合はwatchdog実行まで保証せず、安全終了不能として別途報告する。

### 次回承認範囲と変更予定

**追加helper変更が必要、今回は未変更。** 新しいverification rootに限り、`InputAudit.java`の中央帯/2段階表示/条件・単発guard/timeout、`ProductClientProbe.java`のrun配線・二段階壁・readonly source/heartbeat、安全停止記録を最小修正する。必要なら`Observer.java`のreadonly fieldsだけ補足。新runのmods.toml/version/専用init配線を更新し、限定offline compile/reobfと非混入/task graph確認。製品source/Jar/test/build.gradle・protocol・SessionRegistry・Lease/authority/movement guard・gate/SP・FE/EL原物・shader/login compatを変更しない。判定閾値やnative180、通常入力復帰の意味を緩和しない。

次回承認する一括範囲は、上記helper限定変更/compile→新instance/world/安全区画→事前説明確認→prepare1→E1 goal1/pause/fresh Lease→Minecraft内A READY/本人W1回/actual input・移動→native終了/revoke→B区画とREADY/本人W1回/通常入力復帰→E2→positive serialize→通常Save&Quit・actual positive disk確認→**同Minecraft/JVMで同world Bを1回**→deserialize/旧authority非復活→seal→必要時だけnative cleanup1→通常保存/readonly→Quit/PID終了→3文書更新。通常復帰が未成立ならE2へ進めない。#3以降や旧suite再実行は含めない。

既存artifact・local Gradle/runtime/認証配置は充足、**追加artifact・新製品仕様判断は不要と判断**。今回は認証接続を試していないため次回の有効性までは保証せず、実際の再認証要求時のみ本人へ依頼する。本人2操作と壁の検証用準備を含む上記実行範囲は**別途承認待ち**。現在のrunを再使用しない。

**#1 COMPLETE／#2 INCOMPLETE／残8、#3 NOT STARTED。SAFE DESIGN PROVEN=NO、SOURCE DIMENSION TRANSITION VERIFIED=NO、ownership BLOCKED、P/T IMPLEMENTATION_PENDING・購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、valid-P vehicle未LOCK**を維持。食料生産の極意の可逆クラフト増加は既知許容仕様・バグ修正対象外。

<a id="uom-dual-display-readiness-timeout"></a>
## 14.45 queue #2 別画面配置後のREADY前timeout（2026-09-27 21:33 JST）

**承認された実行を行い、A READY前にINPUT UNVERIFIEDで停止。製品movement FAILではない。** 旧§14.43/14.44は当時の結果/設計として保持。今回のrun/worldは再arm/reset/再入場しない。

- run `20260927-211333`、MC1.20.1/Forge47.4.0、FE2.7.20/EL2.1.19fix、製品298,958B/189entries/hash `37E300EAA0663FDCDF445317C9F14F3BF13C839B21E4DD5679229B32386592E3`。新helper **v20260927.211333 /39,592B/16entries/hash `6D31EF0A9FB2A801DDAD0C3C5B5A6249DAA4B2FE6330BF4FE6CCACF14B3DFC16`**。
- helperだけInputAudit中央HUD/READY/timeout、二段階壁・run配線、readonly operand/deserialize HEAD記録を追加。専用offline task graph→compile/reobf成功、製品task/既存suite0。旧bootstrap/bridge/shader/login compatは不変。[build](../build/verification/uom-production-input-final-20260927-211333/audit/helper-build.log) / [task graph](../build/verification/uom-production-input-final-20260927-211333/audit/client-task-graph.txt)。
- 生成前GUIでSurvival/Normal/cheatsON、doMobSpawning=false/naturalRegeneration=false、Superflat/structuresOFF。prepareによる閉鎖区画、A前進上限**1.199999988blocks**。HP20/MAX20/effects空、SP0/使用済103・P/M Lv1ON。通常GUI購入成功ではない。B壁開放0。

| 項目・実施主体 | 実測/判定 |
|---|---|
| HUMAN手順・配置 | 2回の合図で操作できる説明確認後、本人が「別画面に配置済み。両方が同時に見える」と回答。環境準備でありW測定に数えない。windowed origin(2112,126)/1282×832、focus後も位置不変。monitor番号/構成の直接取得とは区別 |
| COMPUTER USE | 新instance起動1、world生成1、prepare1/arm1、Escによる通常pause/resume、Save&Quit/Quit。W自動入力0、手動追加キー依頼0、Alt+Tab依頼0 |
| AUTOMATED E1/pause | exact goal1、source UUID `5a5a86db-d12c-41e7-914b-e64c4598ea27`、session1/epoch1/G1 sequence1、count180開始。pauseStable=503,112,700ns、count21/serverTick5104で安定。prepare/goal/pauseのみ限定成立 |
| READY/HUMAN A/input/movement | **INPUT UNVERIFIED**。HUDはTEST PREPARING→INPUT CHECK STOPPEDの2状態だけ。REAL-INPUT-READY/INPUT_WINDOW_READY/実render/INPUT-OBSERVEDなし。観測160client tickのkey/forward positive0、acceptedMoving0、XYZ不変。本人の実W操作報告は今回取得していないためHUMAN INPUT NOT PERFORMEDとは断定しない |
| 停止原因 | 21:28:00 goal→21:28:07通常pause→21:28:08 stable→21:28:15 watchdog。goal RETURNから15.0044781秒。Codexのtool往復を含むpause/resume手順がREADY前15秒予算を超え、resume前に停止。READYの条件を満たしたまま本人が押さなかったという結果ではない |
| resume/end/revoke | resume fresh Leaseは未確認。停止後にnative false呼出し、count0/globalfalse/dimensionfalse/oldGrant revoked/Lease消去を観測したが、下記observer混入があるためclean terminal witness PASSを付与しない。旧runの正常終了/revoke限定PASSは維持 |
| HUMAN B/通常復帰 | NOT RUN、normalInput/normalMoved/normalConvergedなし。B用壁開放0 |
| E2/positive disk/same-JVM B/旧authority非復活 | NOT RUN。今回のcount0保存とtitle Context closedはpositive reloadの代用ではない |
| seal/cleanup | NOT RUN/0。FAILURE/receipt/journalを保全、同run再試行なし |
| save/readonly/Quit | 21:28:40 All dimensions are saved。HP20/SP0・103/P&M ON/source count0、transient保存keyなし。title thread TERMINATED/Context closed/states・sessions・grants0/Leaseなし。21:29:06 Stopping!、PID9208不在。Prism対象windowも正常close。既存background Prism PID26748（14:36開始）は残存し、全Prism process終了とは記録しない |

**observer制約を分離する。** `InputAudit.client→deadline→ProductClientProbe.fail→snap()`はclient threadから`Ownership.permission→reconcile`と`MovementGuard.deny`を追加実行する。純粋なreadonly snapshotではなく、server thread限定のidentity値もfalseになる。この経路は停止記録時に到達し、停止後最初のserver snapshotにはUNKNOWN/terminalFault(`pre-existing-unknown`)がある。原因帰属には観測の混入があるため、製品不具合・clean native endのどちらも断定しない。今回は停止後にhelper/製品を改変せず保全した。次の修正案ではclient側failureは既に発行済みのimmutable server snapshotを記録し、serverの評価/状態変更APIを呼ばないことが必要。

証拠：[判定集計](../build/verification/uom-production-input-final-20260927-211333/audit/reviewed-results.json)、[FAILURE](../build/verification/uom-production-input-final-20260927-211333/CLIENT/audit/FAILURE.json)、[raw journal](../build/verification/uom-production-input-final-20260927-211333/CLIENT/audit/journal.jsonl)、[readonly保存](../build/verification/uom-production-input-final-20260927-211333/audit/disk-abort-readonly.json)、[終了log](../build/verification/uom-production-input-final-20260927-211333/audit/client-final.log)、[process](../build/verification/uom-production-input-final-20260927-211333/audit/process-final.json)。変更前3文書はrun内before/docsに保全。製品・通常test/build.gradle・過去run等の309保護ファイルはhash差分0、配置10Jarは全source/target一致。raw state/HP/input/位置setter修復0、製品build/test0、新download0。

次の1作業は**pause/resume操作時間とclient失敗observerの境界のREAD ONLY整理**。今回の2画面配置をやり直す計画にはせず、native180/Lease TTL/15秒上限を延長しない手順、helper-onlyの最小差分、再試行の範囲を明確にする。次runを自動開始しない。追加artifact不足は未検出。

**#1 COMPLETE／#2 INCOMPLETE／残8、#3 NOT STARTED。SAFE DESIGN PROVEN=NO / SOURCE DIMENSION TRANSITION VERIFIED=NO / BLOCKED - TIME STOP SOURCE OWNERSHIP。P/T IMPLEMENTATION_PENDING・購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、UOM/P vehicle未LOCK**。既存PASSと個別開始条件、食料生産の極意の可逆クラフト増加＝既知許容仕様/バグ修正対象外を維持。

<a id="uom-ready-first-thread-boundary-design"></a>
## 14.46 queue #2 READY直結順序・observer責務分離（2026-09-27 21:50 JST）

**READ ONLY / DESIGN ONLY。以下は次回の承認対象であり、helper未変更・未build・未実行。** §14.45のrunは保全。今回の本人確認で**READY表示0／HUMAN W A=0・B=0**を確定する。押し忘れではなくW指示そのものが表示されなかった。input/movementはUNVERIFIEDであり製品FAILではない。§14.45の「本人報告未取得」は当時の記録、今回の追補と区別する。

### 原因と実物根拠

[InputAudit.java](../build/verification/uom-production-input-final-20260927-211333/client-helper/src/main/java/verification/client/InputAudit.java) `client()`は`pauseStable`と`revision > pauseRevision`が揃うまで`windowReady`を立てない。`deadline()`はREADY前には`goalNano`から15秒を測り、ClientTick/drawでチェックする。通常PauseScreenでもwall clockは進む。E1後のCU pause→読取→resumeが、この予算を消費した。

| E1 GOAL-RETURNのmonotonic時刻を0とした証拠 | 実測 |
|---|---|
| [INPUT_PAUSE_READY](../build/verification/uom-production-input-final-20260927-211333/CLIENT/audit/INPUT_PAUSE_READY.json)の実valid RETURN | **+0.0768457秒**。pause前/screen none、正規Lease revision1/session1/epoch1/sequence1、actual valid=true/native canMove=false、count179、server permission=true/UNKNOWN=false/予定sourceのみ |
| [pauseStable](../build/verification/uom-production-input-final-20260927-211333/CLIENT/audit/INPUT-PAUSE-STABLE.json) | server停止snapshot +7.9474165秒、stable記録側client時刻 +8.4545137秒。count21で503ms安定 |
| [FAILURE](../build/verification/uom-production-input-final-20260927-211333/CLIENT/audit/FAILURE.json) | +15.0044781秒。READY未表示、resume前にA readiness/input watchdog |
| [journal](../build/verification/uom-production-input-final-20260927-211333/CLIENT/audit/journal.jsonl) | HUDはPREPARING→STOPPEDだけ。160client tickの`forwardKey`/`input[0]`（forwardImpulse）positive0。再開後のnative false RETURNは+20.157294秒で、READY窓の成功ではない |

**初回fresh Leaseの発生自体にpauseは不要。** 製品[TimeStopRuntime.tick](../src/main/java/com/leva/foodhealing/compat/endinglibrary/TimeStopRuntime.java)→[SessionRegistry.tick/send](../src/main/java/com/leva/foodhealing/compat/endinglibrary/SessionRegistry.java)がserver ENDで正規Grantを発行し、初回送信・以後5authority tickごとの送信を行う。[TimeStopLeasePacket.handle](../src/main/java/com/leva/foodhealing/network/TimeStopLeasePacket.java)はenqueueWorkでclient受信処理へ渡し、[TimeStopClientLease.receive/valid](../src/main/java/com/leva/foodhealing/client/TimeStopClientLease.java)が現行refs/期限を検証する。製品[ClientInputBridge](../src/main/java/com/leva/foodhealing/mixin/endinglibrary/ClientInputBridge.java)による**実際の**valid呼出しRETURNが上記0.077秒の記録にある。pauseを作らずに得られた実証拠であり、次回のREADY表示時間保証とはしない。

### pause/resumeの分類と採用案

| 分類 | 判断 |
|---|---|
| A：製品pause/resume挙動の検証 | 意義はあるが、同じ製品hashの§14.41（revision1→2/count174）・§14.43で限定PASS済み。次回の本人入力測定で再証明する必要はない。旧PASSは保持 |
| B：fresh Leaseを作るために必要 | **該当しない**。上記の正規初回受信/実valid証拠と送信実装で否定できる。helperからLeaseを作らない |
| C：過去の試験手順だけの前提 | **今回の推薦**。E1中のpause/resume再演を入力測定から外し、既存証拠をpreflightで参照する。quiet時にpauseしても「停止中TTL失効→再開fresh」の再証明にはならないため、別の事前pause試験も追加しない |
| D：helper内で通常pause/resumeを自動化 | 既存`Minecraft.execute/setScreen(PauseScreen)`とRenderTick安定観測は実在するが、正規pauseでもTTL・native残時間・監視予算を消費する。今回の最小案には採用しない。fake pause/時刻停止/TTL・count変更は不可 |

次回の**helper測定前提**から`pauseStable/pausedEpoch/pausedSequence/revision>pauseRevision`を外し、E1で実発行された新Grantと正規受信Leaseの一致へ置換する。これは旧pause試験の合格条件を変更するものではない。freshness/current refs/authority/native false/入力・移動の期待値は維持し、E1の初回Leaseをresume後Leaseと記録しない。productへの変更は不要。この手順差分を含めて次回承認を受ける。

### 開始順序とcritical path

1. **E1前に完了**：既存の本人2操作説明、別モニターwindowed配置（focusしてもCodexを覆わない）、instance/run/world/本人/source/Jar照合、新world安全条件、prepare1/取得SP同期、前後二段階壁のserver幾何とclient block一致、HP/effects・接地・静止・W released、observer heartbeat/正常記録I/Oを確認。対象外W入力・人間の追加クリックを必要とする状態では開始しない。
2. world入場後からhelper専用HUDに**MANUAL TEST PREPARING／まだWを押さない**。通常`arm`コマンドは予約だけにし、chatを閉じた後に**ARMING E1／まだ操作しない**の実描画を記録。既存600tickの予約待機はE1前なのでAの15秒へ含めない。待機後もrun/refsに結び付いたfreshなclient準備snapshot（focus/screen none/HUD可視/キー解放）とserver安全snapshotが一致する場合だけ、一度だけgoalを呼ぶ。不一致ならE1前STOP、黙って再予約しない。事前表示はE1計測入力の合図ではない。
3. **最後にE1**：server threadで`TimeStopSkillGoal.timeStop`を既定180で1回。直前に`goalNano`を1回記録し、以後書き換えない。prepare/予約待ちからタイマーを走らせず、E1後の新しい準備処理も足さない。
4. **E1後の最小経路**：正規server tickによるGrant/permission結果→既存packetの正規client受信→既存canMove/validの実RETURN→ClientTickで全READY operand照合→次のRenderGuiでA READY描画/単発証拠。この間のCU pause/resume、F2、スクリーンショット取得、chat確認、ファイルpoll→モデル判断の往復は**0**。Codexの次のツール呼出しを待たずhelperが進む。詳細ログ解析はA/B終了後またはrun終了後へ回す。
5. `PREPARING → ARMING → A_READY → A_INPUT → A_RELEASED → B_PREPARING → B_READY`を画面だけで区別。A/Bの合図はそれぞれ1回。native終了/revokeとB安全進路・block同期の後だけB_READY。A/Bが両方成立した後のE2/positive保存・同JVM B・seal/必要時cleanup・通常終了は既存範囲を維持する。

critical pathは**通常server/client tickと受信・次renderだけ**になる。前runの初回Lease/valid 0.077秒はこの案の根拠でありSLOではない。新observerのmatches RETURNは初回Grant作成の次の自然tick等で揃うため、全READYには数tickを要し得る。低TPS/描画停止/欠けた証拠を補正せずSTOPする。「必ず表示できる」と未実行で保証せず、CodexのGUI遅延が窓を使う構造だけを除去する。

時計はmonotonic。**READY前15秒はE1開始から、READY後15秒は最初のREADY renderから、native180の自然終了は従来どおり絶対上限**。READY成立で用いる時計を区別し、再描画/失焦点/ポーズでreset・延長しない。native count/Lease TTLは不変。B準備30秒・B READY入力待ち30秒・positive後解放/収束10秒も維持。

### READY_DISPLAYED_A：描画と入力の独立checkpoint

`READY_CONDITIONS_A`と**`READY_DISPLAYED_A.json`**を分け、後者はA_READYの文字描画呼出しが正常に完了した最初のframeで1回だけCREATE_NEWする。現helperは`READY-A-FIRST-RENDER`を実drawStringより前に記録しているため、次回は順序を直す。フレームごとの重複書込なし。短い1レコードとし、詳細ログ出力を先行させない。書込失敗はINPUT READY成功とせず証拠不備STOP。

- run・正規化済みinstance/world path（事前検証した文字列）・本人/source UUID、wall clock＋timezone、monotonic時刻、E1開始からの経過、server/client gameTimeと各snapshot取得時刻・age。
- native count/global/dimension、UNKNOWN/foreign/witness、正規Lease/session/epoch/sequence/revision/expiry、実valid/native RETURNの値とage、server判定RETURNの由来・age。
- client current player/listener/connection/levelの実`==`比較結果と識別値、server threadが確認したsession/Grant/current source identity、focus/window active/HUD visible/screen none/key released、`phase=A_READY`・frame番号・draw完了。

これが示すのは**helperの描画経路到達**であり、人の視認・物理scanout・入力PASSではない。本人の認知は入力実測/必要時の本人報告と区別。表示中もfresh snapshotとclient条件を再確認して無効表示を消し、古いREADYを持続させない。A inputの受理はこのcheckpoint後の実`keyUp/forwardImpulse`positiveに限定する。

### observerのthread boundary：確認した経路と次回責務

現helperの[ProductClientProbe.fail/snap](../build/verification/uom-production-input-final-20260927-211333/client-helper/src/main/java/verification/client/ProductClientProbe.java)は呼出threadを問わずlive serverを参照する。clientの`InputAudit.client→deadline→catch→fail`（drawの例外経路も同様）から次へ到達する。

`snap → Ownership.permission → reconcile → TerminalWitnessBridge.identity(source) → TerminalWitness.same`

[identity](../src/main/java/com/leva/foodhealing/compat/endinglibrary/TerminalWitnessBridge.java)は`Thread.currentThread()`を含み、[same](../src/main/java/com/leva/foodhealing/compat/endinglibrary/TerminalWitness.java)はbinding作成時のthreadとの同一性を比較する。[reconcile](../src/main/java/com/leva/foodhealing/compat/endinglibrary/Ownership.java)は不一致時に`unknown("positive-source-binding-mismatch")`を書く。render threadから正のsourceを評価すればserver bindingと一致しない。次のnative callbackは`TerminalWitness.enter`で`pre-existing-unknown`へ進み得る。`SessionRegistry.identity`もserver thread必須なので、clientから得たfalseをserver失効の実測と扱えない。

さらに`snap`はUNKNOWNを**判定呼出しの前に**コピーするため、FAILUREの`unknown=false`はこの経路の非書込を証明しない。`MovementGuard.deny→TimeStopUtils.canMove/Ownership.permission`も追加評価し、`sourceActual`は`ServerLevel.getEntity`を読む。通常server側の`snap`、`Observer.handled`、stage判定にも追加permission/deny呼出しがあり、単にclientのcatchだけ直せば観測全体が受動的になるわけではない。`TimeStopClientLease.valid`自体にも期限切れ時のclearがあるため、client観測目的で追加呼出ししない。

**静的にUNKNOWNを生む経路は確認できたが、前runの最初のUNKNOWN書込call stackは未取得**。停止前snapshotのfalseと停止後serverの`pre-existing-unknown`はこの経路と整合する。製品authority FAILへ昇格せず、旧結果を修正してPASSにも戻さない。

| 所有thread/処理 | 次回の境界（未実装） |
|---|---|
| serverの実処理RETURN | helper専用observerで`Ownership.permission(ServerPlayer)`、`SessionRegistry.matches(Grant,listener)`、`MovementGuard.deny(listener)`の**既存呼出し**RETURNだけを取得。server thread/対象player/listener/source世代を確認し、値・実引数identity・時刻をhelperへコピーする。メソッドを自分で呼び直さず、返値を変更しない |
| server END snapshot | 既存`TimeStopRuntime`の自然評価後、helper ENDで結果を集約。live source lookup/保存/HP/SP/区画・ledger field読取はこのthread限定。未生成stateを作る`AuthorityContext.state/computeIfAbsent`へ観測から入らない。maps/sets/positionsは深くコピーし、Java entity/Session/Grant等の可変参照をclientへ渡さず、不可変DTOをvolatile/atomicに公開 |
| READYの非拒否条件 | 自然なserver tickのpermission=true＋matches=trueを**同じcurrent Grant/session/epoch/refs**へ結び、150ms以内のsnapshotへ収める。`MovementGuard.deny = stopped && !nativeCanMove && !(permission && matches)`なので、この2つがtrueなら非拒否条件を読取で確認できる。これはactual packet guard RETURNと別ラベル。実移動PASSには同packetの自然guard結果・HEAD/RETURN・受理/位置差・correctionを引き続き必須にし、未観測denyをfalseと捏造しない |
| client tick/render | 自スレッドのplayer/input/HUD/Lease fieldと、既存`LeaseObserver`/`NativeMoveObserver`が記録した自然RETURNを読む。`valid`/`canMove`を追加実行しない。serverには公開DTOだけでアクセスし、getEntity/permission/reconcile/identity/ledger APIを呼ばない。null・不明・staleはNOT READY、0/false成功へ変換しない |
| Connection/packet observer | `Observer.sent`をserver correction側とclient movement側へ分離。server送信callbackから`Minecraft.getInstance()/client()`を読まず、外側threadの名前だけで所有を推測しない。適切な既存callback上でpacketの不変値とidentityを採り、違うthreadならlive値を評価せず観測不足として記録。handler guard RETURNの対応が取れないpacketをaccepted PASSに数えない |
| failure/停止・title/reload | 最初の失敗はhelper atomic latchで1回だけ確定し、理由・発生thread・最後に公開済みのserver DTO（元timestamp/age付き）とclient所有値だけを保存。serverが止まってもsnapshotを新鮮に見せず、server評価taskを発行して観測値を作らない。停止HUD＋既存通常PauseScreenのみ。server正常終了値はserver eventで公開し、title側はそれを読む。再読込時はhelper観測世代を分け、過去DTOで現行READYを成立させない |

server/clientの各公開snapshotは取得時刻を保持した一組として扱い、mapと別の`serverNano`を別々に更新して新旧を混在させない。既存の`unmodifiableMap`だけでは内部collectionsやlive objectの不変性は保証されない。読み取り失敗で再帰的に`snap()`を呼ぶfallbackは禁止。所有確認のためのplain ref比較と実際に記録された判定結果を区別し、観測不足のAPIを手動実行して埋めない。

### helper最小差分・本人操作・停止分類

次回は**新しい承認済みrootだけ**に次の差分を作る。今回はこれらのファイルを作成/編集していない。

| ファイル | 役割 |
|---|---|
| `ProductClientProbe.java` | armのE1前preflight/一度だけの開始、server-owned snapshot、原子的failure latch/停止snapshot、既存stage評価を受動結果へ接続。native goal/二段階壁/prepare回数・期待値を変更しない |
| `InputAudit.java` | PREPARING/ARMING表示と準備ack、pause前提をE1の正規Lease/実RETURNへ置換、時計の明確化、READY_DISPLAYED_Aを描画後に単発記録。既存input/移動/解放/収束とB条件は維持 |
| `Observer.java`、必要な既存packet/lease observer | server/client記録を分離、permission/deny/valid追加評価を除去、実RETURN/current refs/age・null/世代を記録。window・client動作はclient threadのみ |
| 新規helper `mixin/AuthorityResultObserver.java`・`mixin/GrantMatchObserver.java`・`mixin/GuardResultObserver.java` | 上記実在メソッドのRETURNを受動記録。server authorityを代行せず、Mixinはhelper Jarだけへ登録 |
| `uom_product_observers.mixins.json`、`META-INF/mods.toml`、専用init | observer登録と新run/version/path配線。製品source/test/build.gradle/Jar・既存compat/外部原Jar・protocol/schema/gate/SP不変 |

**HUMAN A/Bは各1回だけ**：A_READYでW約1秒→離す、B_READYで同じ操作。それ以外は操作不要、合図はMinecraft内で完結する。既存別画面配置方針を継承し、追加クリック/Alt+Tab/Esc/3回目Wは予定しない。再認証・配置修復・手動focusが実際に必要ならE1前STOPし、理由を示してその操作だけ別途承認を求める。

| 停止/不足 | 判定 |
|---|---|
| E1前preflight不成立 | PREPARATION BLOCKED、goal0。準備失敗をinput FAILにしない |
| READY_DISPLAYED_Aなしでnative終端/READY前15秒 | **READY NOT DISPLAYED / INPUT UNVERIFIED**。本人の押し忘れとしない。条件の不足operandとageを記録 |
| READY描画あり・positiveなしで締切 | **READY DISPLAYED / INPUT UNVERIFIED**。本人未操作と本人確認できた場合だけHUMAN INPUT NOT PERFORMEDを追補 |
| positiveあり・移動/解放/収束が不成立 | 実測範囲のFAIL/UNVERIFIEDを分け、期待値を緩めない |
| observer例外・stale/identity/authority喪失 | OBSERVER/SAFETY STOP。clientからserver再評価や状態修復をせず、記録と通常退出。製品FAILかは汚染のない証拠に基づき別判定 |

成功時だけA→native終了/revoke→B通常復帰→E2 positive serialize/actual disk→同Minecraft/JVMで同新world B1回→deserialize/旧authority非復活→seal/必要時native cleanup1→通常保存/readonly/Quit/PID確認へ進む。失敗時は合図停止・通常ポーズ→Save&Quit→readonly→Quit/PID確認、同run再arm/reset/再入場なし。再試行を今回のREAD ONLYから開始しない。

**追加artifact不足なし。次回承認は、上記helper限定変更/限定offline compile・reobf、新instance/world、本人W2回、成立範囲の後続lifecycleと通常終了・3文書更新の一括範囲。** 自動化・物理表示の全障害を解決済みとはせず、次runのREADY_DISPLAYED_A/入力実測で検証する。

今回の変更は3文書と[変更前backup](../backups/20260927-214413-uom-ready-thread-design-docs/CODEX_STATUS.md)だけ。**#1 COMPLETE／#2 INCOMPLETE／残8／#3 NOT STARTED、SAFE DESIGN PROVEN=NO、SOURCE DIMENSION TRANSITION VERIFIED=NO、ownership BLOCKED、P/T IMPLEMENTATION_PENDING・購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED**を維持。native180/TTL/15秒、authority/session/Grant/UNKNOWN・movement期待値、全個別開始条件、可逆クラフト増加の既知許容仕様は不変。

<a id="uom-automated-os-input-a-pass-b-readiness-stop"></a>
## 14.47 queue #2 AUTOMATED OS INPUT A成立・B READY観測timeout（2026-09-27 22:28 JST）

**今回の利用者LOCK：queue #2の通常入力証明はAUTOMATED OS INPUTを採用し、HUMAN物理Wを必須条件から外す。** OS送信だけを合格にせず、実Minecraft input→packet→guard→server/client移動→release/収束が必要。HUMAN INPUT=0。旧§14.44–14.46のHUMAN計画と前runのREADY0/W A0/B0は当時の履歴として保持する。

### 対象・変更・preflight

- 新run **20260927-215939**、MC1.20.1/Forge47.4.0/FE2.7.20/EndingLibrary2.1.19fix。新instance/worldのみ、既存認証Prism/local Java。Minecraft PID **16148**（creation filetime134349882915163199）、HWND **1444684**/GLFW30、first-write/run/instance/java実pathを結合し一意確認。[集計](../build/verification/uom-production-os-input-20260927-215939/audit/reviewed-results.json)。製品298,958B/189entries/hash `37E300EAA0663FDCDF445317C9F14F3BF13C839B21E4DD5679229B32386592E3`/protocol7不変。
- 検証MOD **v20260927.215939、45,737B/20entries、SHA256 `9AE69827ABE80F4508C0A77E4B35E6A0A0D432732C8328ED3645FB8EE8774895`**。[専用task graph](../build/verification/uom-production-os-input-20260927-215939/audit/client-task-graph.txt)、[限定offline compile/reobf成功](../build/verification/uom-production-os-input-20260927-215939/audit/helper-build-final.log)。product build/test task0。`downloadMcpConfig`というtask名は既存offline cache解決であり、新規downloadはしていない。標準sandboxのnative DLL不可は権限付き既存local手順で解消。
- 別[os_input.py](../build/verification/uom-production-os-input-20260927-215939/os-input-helper/os_input.py)は既存Python ctypes/Win32 SendInput。hash **`FBBB3E40B162E99D29FAB28EDD8EAB5A9FCC33482285D0CF51DD11DFA90BA8A0`**。hold-key/release-key、1000ms限定、PID creation/Java/instance/HWND/class/thread/focus、pre-keyUp、finally、1.35秒watchdog、caller2.2秒timeout/異常release、phase一回claimを実装。監督をE1前に起動し、描画後のREADY receiptだけを受け付けた。[識別](../build/verification/uom-production-os-input-20260927-215939/os-input-helper/artifact-identity.json)。異常全種類の動的fault injection試験はしておらず、全failsafe実試験PASSとは言わない。
- 入力前不備を保全：sandboxではwindow列挙/SendInput不可（keyDown0）。ローカル権限実行へ変更。helperのtarget/receipt辞書重複キー修正、配置判定修正（最大化Codexの不可視8px枠でmonitor全体の交差判定が過剰。実際に置くwindow矩形の非重複で判定）の2修正群。A positive後のhelper修正/入力retryなし。期待値/native180/TTL/timeoutは変更なし。
- [dry-run](../build/verification/uom-production-os-input-20260927-215939/os-input-helper/DRY-RUN.json)はPID/window/foreground/focus/1000ms/記録/keyUp return1、**keyDown0**。実window矩形[2232,96,3528,935]、Codex[-8,-8,1928,1040]と非重複、windowed、HUMAN配置操作0。生成前GUIでSurvival/Normal/cheatsON/Superflat/structuresOFF/doMobSpawning=false/naturalRegeneration=false。prepare1、HP20/effects空/P&M Lv1 ON/SP0・使用済103。通常GUI購入の証拠ではない。
- 通常初期spawn(2.5,-60,5.5)を動かさず閉鎖区画を構築。player幅を含むA最大前進 **1.199999988079071**、横方向余裕0.4を加えた保守的水平上限 **1.264912 blocks未満**。block sync/静止/キー解放とPREPARING/ARMING・observer heartbeatをE1前確認。arm待機はhelper内100tickへ短縮、E1後の15秒/native180とは別。E1後READY前pause/resume/F2/CU往復0。
- serverの自然permission/matches/deny RETURNを非cancel observerで記録。snap/failureから追加permission/deny/valid呼出しを除去、server-owned snapshotを深いコピー/不変DTOでclientへ公開。停止時も最後のtimestampを保持。実packetのguard RETURNをREADY時の非拒否推定と別に記録。EndingLibrary native stateを直接setterで修復せず、native goal1回と通常native終端だけ。

### 個別結果

| 項目 | 判定・実測 |
|---|---|
| OS helper / client helper build | **PASS**（Python script/static ABI40bytes、検証MOD限定compile/reobf）。製品には非混入 |
| dry-run / target PID-HWND / focus | **PASS**。dry-runはkeyDown0。A前/後foreground=focus=1444684 |
| A READY | **PASS**。E1開始 **+131.3433ms**、count178、session1/epoch1/sequence1/revision1、UNKNOWN=false/単一UOM binding、実valid=true/raw native canMove=false。drawString完了後CREATE_NEW [READY_DISPLAYED_A](../build/verification/uom-production-os-input-20260927-215939/CLIENT/audit/READY_DISPLAYED_A.json) |
| A OS keyDown/keyUp | **PASS / AUTOMATED OS INPUT**。W1回 **1002.648ms**、SendInput各return1、exit0。[HOLD-A-DONE](../build/verification/uom-production-os-input-20260927-215939/os-input-helper/HOLD-A-DONE.json)。READYからkeyDown約95ms |
| A actual Minecraft input | **PASS / REAL CLIENT OS INPUT OBSERVED**。forwardKey=true・forwardImpulse=1、**20 positive tick/最大連続20**。HUMAN INPUT=0 |
| A movement / packet / guard | **REAL CLIENT LIMITED PASS**。server/client Z5.5→6.699999988、距離1.199999988。stage3 handler RETURN16件、移動受理7件、自然guard deny=falseを各packetで確認、correction0。idle packetだけのPASSではない |
| A release / convergence | **PASS**。forwardKey=false/input0、水平velocity0、server/client差0、3tick安定をcount154で確認。[収束](../build/verification/uom-production-os-input-20260927-215939/CLIENT/audit/INPUT-MOVEMENT-CONVERGED.json) |
| native end / revoke | **PASS**。count0/global false/dimension false、G1 revoked/current Grant0、client Leaseなし、origins/bindings0、witness created/consumed/committed=1/1/1・fault=false、UNKNOWN=false |
| B READY | **FAIL（helper readiness timeout）**。stage4開始から30秒で停止。normalReleasedTicksが進まず、wallOpened0/normalPathReady=false/READY_DISPLAYED_Bなし |
| B OS keyDown/keyUp | **NOT RUN**。B claim/keydown0、A input再利用なし。supervisorはFAILURE検出で停止しkeyUp、さらに通常退出前にrelease-key |
| B actual normal input / movement / restore | **UNVERIFIED**。599client samples、positive0。製品normal movement FAILではない |
| E2 / positive serialize / actual positive disk | **NOT RUN**。goal合計1。通常終端count0の保存をpositive保存成功にしない |
| same-JVM B / deserialize / old authority non-revival | **NOT RUN**。world context1、通常再読込0。既存queue #1等のPASSを今回製品の証拠に転記しない |
| seal / cleanup | **NOT RUN**、各0。FAILURE/receiptを消さず同run再送/修復なし |
| final normal save / readonly | **PASS**。22:20:06 All dimensions saved、HP20/SP0・103/P&M ON/source count0、保存transient keyなし。[保存照合](../build/verification/uom-production-os-input-20260927-215939/audit/disk-final-readonly.json) |
| Quit / process | **PASS**。22:20:36 Stopping!、titleでserver thread TERMINATED/context closed/maps0、Minecraft16148・今回Prism32524不在。既存background26748は操作対象外で保持 |
| product/helper separation | **PASS**。製品source/resources/test/build.gradle/Jar等171保護hash差分0、配置10Jarの元/配置hash一致、製品Jarにverification/OS helperなし。product build/既存suite再実行0 |

### B停止の原因・次の1作業

[InputAudit.client stage4](../build/verification/uom-production-os-input-20260927-215939/client-helper/src/main/java/verification/client/InputAudit.java)の`ordinary`は`nativeMove && now-nativeAt<150ms`を必須にしている。しかしBの599client観測で新しいLocalPlayer native RETURNは0。最終観測はA終了直前のraw falseで、停止直前age **29.9673954秒**。Leaseなし/global・dimension false/HP20/静止/キー解放/UNKNOWN falseが揃っていても、ordinary=false→解放3tickなし→内壁開放なし→READYなし、という依存で30秒に達した。[FAILURE](../build/verification/uom-production-os-input-20260927-215939/CLIENT/audit/FAILURE.json)。OS入力の配送失敗でも、実入力後のB movement FAILでもない。

実ロード済み[Minecraft bytecode](../build/verification/uom-production-os-input-20260927-215939/audit/Minecraft-transformed.javap.txt) `handler$zkd000$runTick_modifyPartial`はglobal falseならbytecode offset70→834へ分岐し、停止中ループのoffset470 `TimeStopUtils.canMove(LocalPlayer)`を通らない。この実体とRETURNが更新されない実測は一致する。**stale falseを「通常移動不可」と読んではいけない**。raw canMoveは停止中免疫判定であり、通常tickの実行/入力許可の観測と同一視しない。他すべての呼出siteを網羅した主張ではない。

次の最小1作業は、**B通常tick/入力経路の受動観測を、実物の停止外分岐へ合わせるhelper-only修正案の確定と、新runの未完了B→E2/positive保存/同JVM Bの実行承認**。global/dimension false＋count0＋current Grant/Leaseなし＋G1失効・UNKNOWN false、通常client tick/入力処理の自然到達、解放/静止/区画/同期を観測し、Bの独立OS input・実packet/guard・server/client移動/収束は引き続き必須。native canMoveを観測目的で呼び足す、古い値をtrueへ補正する、未観測をPASSにする案は不可。元のnative true前提の誤対応を明示して受入条件と整合を確認し、今回勝手に変更/再試験しない。製品/authority/Lease期待値は変更しない。

追加artifact不足・HUMAN入力要件なし。A PASSは維持し、新runでB開始状態を作るAが必要なら準備1回として区別する。**このrunは保存して終了済み、再利用/reset/再入場禁止**。B入力が成立しなかったため#2完了条件未達、**#1 COMPLETE／#2 INCOMPLETE／残8／#3 NOT STARTED**。SAFE DESIGN PROVEN=NO、SOURCE DIMENSION TRANSITION VERIFIED=NO、BLOCKED - TIME STOP SOURCE OWNERSHIP、P/T IMPLEMENTATION_PENDING・購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、UOM/P vehicle未LOCKと個別開始条件を維持。可逆クラフト増加は既知許容仕様・バグ修正対象外。

<a id="uom-os-normal-positive-save-teardown-stop"></a>
## 14.48 queue #2 B通常復帰・E2 positive保存成立／退出helper STOP（2026-09-27 23:02 JST）

**B通常入力復帰・E2・actual positive diskが限定成立。保存退出時のhelper lifecycle不備でSTOP、Minecraftはクラッシュ終了。** 新run `20260927-224439`、AUTOMATED OS INPUT B W1004.4951ms／実client positive20tick／server・client移動1.00block／受理移動6件・correction0／解放静止3tick・位置差0。E2 epoch2/sequence2、runtime179→serialize179/178→実disk178。検証HUDはTEST_COMPLETE後2秒でHUD_DISABLED、同session20render frame描画0。通常保存での退出中にhelperのhealth guardが失敗し、fail()がworldなしへPauseScreenを表示。そのDisconnect操作でClientLevel=nullのNPE、exit−1。same-JVM B/旧authority非復活/seal/明示cleanupは未実施、正常QuitはFAIL。旧A限定PASS維持、今回AはB開始準備1回。HUMAN=0。

### 対象・限定変更

run **20260927-224439**、新instance/worldだけ、MC1.20.1/Forge47.4.0/FE2.7.20/EndingLibrary2.1.19fix。Minecraft PID30004・creationFileTime134349904922333282・HWND2951774/GLFW30をfirst-write/instance/実Java pathへ結合。別画面windowed配置・foreground/focus・dry-run keyDown0を自動確認。生成前GUIでSurvival/Normal/Superflat/structuresOFF/doMobSpawning=false/naturalRegeneration=false、prepare時にも反映/正常freshを確認。閉鎖区画、本人位置setter0、A前進上限1.199999988block、B内壁開放1回後前進上限1.00block（横余裕を含め2未満）。prepare1/credit103・支出103/P&M Lv1 ON、GUI購入成功ではない。

製品 **298,958 bytes/189 entries/SHA-256 `37E300EAA0663FDCDF445317C9F14F3BF13C839B21E4DD5679229B32386592E3`/protocol7** 不変。検証MOD **v20260927.224439、49,381 bytes/21entries、SHA-256 `4F439D6BAE078063F8858E4FAA1C2CA9F2E261DD6FBF1231C21141E1D207C2FF`**。[集計](../build/verification/uom-production-os-input-20260927-224439/audit/reviewed-results.json)／[helper差分](../build/verification/uom-production-os-input-20260927-224439/audit/helper-final.patch)／[限定build](../build/verification/uom-production-os-input-20260927-224439/audit/helper-build-final.log)。既存local Gradle/cache・offline専用compile/reobfのみ成功、製品build・既存suite0。171保護ファイルhash差分0、配置10Jarと元artifact全hash一致、製品へverification/HUD/OS helper非混入。FE/EndingLibrary原物・shader/login compat不変。

- **Bの事前証拠と事後証拠を分離**：stage4のfresh raw canMove=true要求だけを外し、自然ClientTick END＋fresh server DTO、native count0/global・dimensionfalse、G1 revoked/Grant・Leaseなし/origins・bindings0/UNKNOWNfalse/witness clean、キー解放/静止3tick/位置一致、安全区画/block同期/HP20、同run/PID/window/focus/OS監督READYを使用。canMove/permission/reconcile/deny/source lookupの観測用追加実行0。raw RETURNのstale falseは補正せず記録。実入力後のactual packet/自然guard RETURN/handler受理・server/client移動・解放後静止3tickは必須のまま。B_READYだけではPASSにしない。
- **HUD**：別VerificationHudだけに一方向lifecycle。COMPLETE/STOP最長2秒→HUD_DISABLED、unload/title/ref/run/reload/sealで永続無効化、disk復元なし。observerは独立。今回COMPLETEで消去済みのため後のfailもSTOPPEDを再表示しない。製品HealthDisplayOverlayは変更0。
- **OS helper**：PID creation/HWND/class/thread/run/focus、約1000ms W各phase一度、finally keyUp/1.35秒watchdog/2.2秒caller timeoutを継承。各送信で対象focusを再照合、他windowへrelease送信しない。target閉鎖/子PID/watchdog joinを追加記録。初期helper修正1群、限定compile/reobf1回、測定後repair/retry0。HUMAN0。

### 個別判定（製品全体のPASSへ拡張しない）

| 項目 | 実測・結果 |
|---|---|
| 旧A／今回A | 旧§14.47 REAL CLIENT LIMITED PASS維持。今回AはB setup prerequisite1回のみ：E1+115.0236ms READY、positive20tick、1.199999988block、受理移動7件/correction0、native終端・G1失効成立 |
| B READY | **PASS**。内壁開放1回・位置不変・安全B通路/block同期、count0/global・dimensionfalse/Leaseなし・UNKNOWNfalse、自然release/静止を確認 |
| B OS input | **PASS / AUTOMATED OS INPUT**。W **1004.4951ms**、down/up各SendInput return1・child exit0、focus同HWND。送信成功だけの判定ではない。[HOLD-B](../build/verification/uom-production-os-input-20260927-224439/os-input-helper/HOLD-B-DONE.json) |
| B actual input / 通常移動 | **REAL CLIENT LIMITED PASS**。forwardKey true/input1を20tick、server/client Z−7.3000000119→−6.3000000119（**1.00block**）、stage4 handler RETURN43件中移動受理増分6件、実guard非拒否、correction0 |
| B release / stationary convergence | **PASS**。key false/input0/水平velocity0・位置差0を3tick。[収束](../build/verification/uom-production-os-input-20260927-224439/CLIENT/audit/NORMAL-INPUT-CONVERGED.json)。新しいraw canMove=true RETURNはなく、自然normal movement受理で証明 |
| native end / revoke | **PASS**。count0/global・dimensionfalse、old Grant revoked/current0、Leaseなし、origins/bindings0、witness1/1/1/faultfalse/UNKNOWNfalse。A→B間の終端確認 |
| E2 | **PASS（限定）**。native exact goal180、新epoch2/sequence2/session1、permission true/単一正規source/current Lease、runtime179・UNKNOWNfalse。[E2](../build/verification/uom-production-os-input-20260927-224439/CLIENT/audit/POSITIVE-SAVE-READY.json)。snapshot movementDeny=trueは古いmatches観測による派生値であり実packet拒否ではない。E2で移動試験を追加しない |
| positive serialize / write / actual disk | **PASS**。通常pause saveのnative serialize live/tag179、通常Save&Quit時178。全dimension保存後、同source UUIDのregion実disk **178**をreadonly確認。[positive disk](../build/verification/uom-production-os-input-20260927-224439/audit/disk-A-positive.json)。runtime・serialize・保存要求・diskを分離、NBT外部編集0 |
| same-JVM B / deserialize | **NOT RUN**。context1/reopen0、退出helper failure後は再入場しない |
| old authority非復活 | **UNVERIFIED（reload）**。A終了時Context closed/states・sessions・grants0は観測したが、再読込非復活の代用にはしない |
| seal / 明示native cleanup | **NOT RUN / 各0**。保存後のnative unload由来use(false)は観測、helper cleanup実施とは数えない |
| HUD TEST_COMPLETE → DISABLED | **PASS**。22:52:30.478 JSTにCOMPLETEから無効化、[HUD_DISABLED](../build/verification/uom-production-os-input-20260927-224439/CLIENT/audit/HUD_DISABLED.json)。2秒表示後、[同session20render frame描画0](../build/verification/uom-production-os-input-20260927-224439/CLIENT/audit/HUD_DISABLED-FRAMES.json)、CU画面も検証帯消失・正式HP表示維持 |
| title/reload HUD | title **UNVERIFIED**、reload **NOT RUN**。退出failureでtitle完了receiptなし。観測した範囲のstale再表示0を、未実施reload全体へ転記しない |
| OS helper後始末 | **PASS**。A/B keyUp済み/残keyDownなし、両watchdog join、supervisor exit0/target claim closed、各helper PID不在。別windowへ追加keyUpなし |
| normal save / readonly | **PASS**。22:54:31 logout/stop、22:54:32.235全dimension保存。実disk HP20/SP0・103/P&M ON/source178/transient keyなし。保存後に値を書き換えていない |
| normal Quit / process | **正常Quit FAIL**。22:55:09 world-null PauseScreen Disconnect操作でNPE、Prism exit−1。Minecraft PID30004は不在、今回Prism3828を通常closeし不在、既存background26748は対象外。thread TERMINATED/title完全終了は今回未採取。プロセス不在を正常Quitへ置換しない |
| 製品分離・既存証拠 | **PASS**。171保護hash/10配置Jar不変。製品build/test0、既存run reset/旧world再利用0。旧A・L2/Trial等の限定PASS維持 |

### STOPの原因と限界

1. E2通常pause中はLeaseが自然失効した。EndingLibrary `ClientEventHandler.disableMouseEventWhenTimeStopping(ScreenEvent.MouseButtonPressed.Pre)`が停止中かつraw canMove=falseでマウスをcancelする実bytecodeと一致。クリック/フォーカスなしEnterではSave&Quitへ進まず、**通常Tab→Shift+Tab→Enter**で保存開始。Lease期限・native countを延長/修復していない。
2. 22:54:31のlogout時、helper `ProductClientProbe.tick`は`player != null`だけで`health()`を継続し、[FAILURE](../build/verification/uom-production-os-input-20260927-224439/CLIENT/audit/FAILURE.json) `HP/effect invariant`を記録。ガードはisAlive/HP/maxHP/absorption/effectsの複合条件で、**どの個別operandがfalseだったかは未採取**。正常保存HP20・無被弾記録・退出順序から、removed playerのisAlive検査が有力という静的推定に留める。製品HP不具合/死亡と断定しない。
3. `fail()`がworld/player有無を確認せずclientへ`setScreen(new PauseScreen(true))`をqueueする。server停止・level消失後にworldなしのPauseScreenが残り、Codexの通常Disconnectクリックで`PauseScreen.java:100`のClientLevel null例外。[crash](../build/verification/uom-production-os-input-20260927-224439/audit/crash-2026-09-27_22.55.09-client.txt)／[UI経緯](../build/verification/uom-production-os-input-20260927-224439/audit/stop-ui-evidence.json)。これは今回の終了FAILであり、隠さず保存する。

同runでfail latch解除/receipt消去/再入力/再arm・B後修正/再起動なし。B READY/HUDの今回修正は成立したが、新しく露呈した退出lifecycle不備を期待値緩和で回避しない。**同JVM B・seal・正常終了が未達なのでqueue #2はINCOMPLETE、残8。**

**次の1作業：検証補助のlogout/stop時health観測とfail画面のlifecycle境界をREAD ONLYで確定し、未完了の同JVM positive再読込・非復活・seal・正常終了へ進む最小修正/実行範囲をまとめる。** B READY/実通常移動/E2/positive disk/HUD同session消去は§14.48で限定成立済みであり再証明を目的に繰り返さない。今回runはfail latch・crashを保全して終了、再利用/reset/receipt削除/再入場なし。B positive後の同run修正・入力retry・追加新runなし。次のlifecycle補助修正/compile/起動は別承認。製品/native/TTL/閾値・authority/Lease設計は変更せず、退出済み参照とworld-null画面を安全に扱う。HUMAN Wへ戻さず、#3へ進まない。

次回は新たな外部artifact/HUMAN入力を要求せず、helper退出観測境界・world-null時の安全終了方式と必要な新run範囲を先に確定する。今回成功したB/E2を再証明する全再試験を自動必須化しない。変更前[3文書backup](../build/verification/uom-production-os-input-20260927-224439/before/docs/CODEX_STATUS.md)を保全。SAFE DESIGN PROVEN=NO、SOURCE DIMENSION TRANSITION VERIFIED=NO、BLOCKED - TIME STOP SOURCE OWNERSHIP、P/T IMPLEMENTATION_PENDING・購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、UOM/P vehicle未LOCK・全個別開始条件を維持。#3 NOT STARTED。可逆クラフト増加は既知許容仕様・バグ修正対象外。

<a id="uom-teardown-lifecycle-design"></a>
## 14.49 queue #2 退出監視・world-null画面境界 — READ ONLY / DESIGN ONLY

更新: 2026-09-28 18:50 JST。実装・helper変更・compile・起動・新runは0。§14.48のA/B通常復帰/E2/actual positive disk178/HUD同session消去を維持し、製品TimeStop/movementの新FAILにしない。same-JVM B NOT RUN、旧authority非復活UNVERIFIED、seal NOT RUN、normal Quit FAILを維持する。今回完了したのは次回の最小差分設計と[SPEC§12](SPEC.md#legacy-high-nutrition-retirement)の新LOCK/静的監査だけ。

### 実物で確認した退出順序と証拠限界

証拠rootは[旧run20260927-224439](../build/verification/uom-production-os-input-20260927-224439/)。`client-helper/src/main/java/verification/client/`のProductClientProbe/InputAudit/VerificationHud/Observer/AuthorityAudit、CLIENT/audit/FAILURE.json、audit/client-final.log、audit/crash-2026-09-27_22.55.09-client.txtを照合した。

| 実経路 | 読取で確認したこと |
|---|---|
| `ProductClientProbe.tick/health` | END・!failed・player!=nullだけでhealthを実行。logout/removed/connection closed/context世代の除外なし。isAlive/HP20/maxHP20/absorption0/effects空の複合guard |
| `InputAudit.client/common/deadline` | client tickはnullならreturn、commonは現context1/identity/fresh DTO等を照合するが退出latchなし。render/deadlineとserver guardを一貫して閉じる仕組みがない |
| 通常Save & Quit | 実47.4.0 client Jarの`PauseScreen.m_261092_()V`（mapped `onDisconnect()`）はClientLevel.disconnect→Minecraft.clearLevel(Screen)→TitleScreen。押下前/単なるEscは退出受理ではない |
| server logout/removal | 同Jar `PlayerList.m_11286_(ServerPlayer)`（remove）は先頭でForge `firePlayerLoggedOut`、保存後`ServerLevel.removePlayerImmediately(...UNLOADED_WITH_PLAYER)`。removed前にserver側の最終health operandを採れる。LivingEntity.isAliveは!isRemovedかつHP>0 |
| client logout/unload/null | 保存済み実変換Minecraft bytecodeの`m_91320_(Screen)`（clearLevel）はfirePlayerLogout→gameMode null→待機画面→LevelEvent.Unload→server停止待ち→ClientLevel null→LocalPlayer null。LoggingOutはnullable引数を持つため、初回起動中のnull通知を対象退出と誤認しない |
| 実log | 22:54:31.457 lost connection/left、.463 singleplayer stop、.506 Saving players/worlds、22:54:32.235全dimension保存。SERVER_STOPPED時点のthread RUNNABLEはイベント内であり終了の代用ではない |
| health failure | FAILUREのcached DTOはHP20/effects空/source179の退出前snapshot。個別failure operandは未採取。removed→isAlive=falseは静的に整合する有力原因だが、実operand確定ではない |
| `fail()`→PauseScreen | serverからclient.executeへ無条件queue。実行時のworld/current connection再確認なし。退出後worldなしのPauseScreenが残った |
| crash | Disconnect操作はClientLevelへnull-checkなしで呼ぶ。実crashはPauseScreen.java:100、mouseClicked NPE。正常Quit FAILを保持し、プロセス不在を正常終了へ読み替えない |

native照合元は既存launcher `libraries/net/minecraftforge/forge/1.20.1-47.4.0/forge-1.20.1-47.4.0-client.jar`のreadonly javapと、[実変換Minecraft記録](../build/verification/uom-production-os-input-20260927-215939/audit/Minecraft-transformed.javap.txt)。mapped名とForgeイベントsignatureは既存47.2.0 compile cacheのsourcesで補足し、実runtime47.4.0と混同しない。マウスcancelは既存EndingLibraryの仕様であり、通常Tab/Shift+Tab/Enterで退出できた経路を維持する。TTL/Leaseを延長しない。

### 次回の最小helper差分（未作成・別承認）

基準は上記旧runのsource。新run内へ必要なhelperだけを複製し、旧receipt/journal/source/Jarは不変。以下の状態は**verification lifecycleだけ**であり、製品session/Grant/epoch/sequenceとは別。

| 予定ファイル・メソッド | 最小変更案 |
|---|---|
| 新 `verification/client/ProbeLifecycle.java` | context generationごとの `BINDING → LIVE_MEASUREMENT → TEARDOWN_STARTED → TITLE_SETTLED`、失敗terminal latch、owner-thread別immutable DTO、正常退出受理tokenを管理。teardown後に同世代LIVEへ戻さない。pause中もworldが有効ならLIVEのまま |
| 新 `verification/client/mixin/PauseScreenLifecycleObserver.java`＋既存observer mixins JSON | 実在`PauseScreen.onDisconnect()V` HEAD（runtime m_261092_）を非cancel観測。現在PauseScreen/world/player/listener/connection/run世代と、positive保存準備完了またはseal後終了準備を照合し、Save&Quit受理の一度限りtokenを出す。早すぎる退出はABORT扱い。native呼出しを代行/変更しない。descriptor/reobf照合必須 |
| `ProductClientProbe.login/about` | context2はTITLE1完了＋actual positive disk証拠＋同PID/JVM/world/UUID＋!failedからだけ新observer世代をbind。旧参照・receiptは証拠として保持。初回起動のnullable logoutはbind前として記録。prepare/arm再実行不可 |
| 新client LoggingOut/LevelEvent.Unload、server PlayerLoggedOut/ServerStopping/ServerStopped listener | イベントの実server/world/player/connectionが対象世代に一致する時だけlatch。server logout入口はremoved前にhealth各operand/canonicalを独立採取し、HP変化等があればFAILを残す。後続removed/null参照へhealthを再適用しない。clientはclient field、serverはserver fieldを所有threadで読む |
| `ProductClientProbe.tick/health/canonical/snap/stopping/stopped` | live predicateを満たす現playerだけHP20/max20/absorption0/effects空の既存guardへ通す。guard実行と失敗分類に世代/teardown状態を再照合。退出後はゲーム測定を止め、context closed/maps/保存/停止thread等のreadonly終了観測だけを継続。snapを無条件に旧level/playerへ実行しない |
| `ProductClientProbe.fail`およびpositive/same-JVM BのPauseScreen要求 | failure receiptには各operand、採取thread/時刻/世代、最後のlive DTOとそのage、退出理由を分離。clientへqueueした画面要求は**実行直前**に世代・live refs・connection open・teardownなしを再判定。live失敗だけPauseScreen可。退出中/null/title/closed connectionならreceipt/log＋HUD_DISABLEDのみ、setScreenなし。古いqueueが新worldをpauseすることも禁止 |
| `InputAudit.client/common/deadline/serverEnd/displayed` | LIVEかつ現世代のみREADY/deadline/input測定。teardown/failureで再READY/再arm不可、OS ready再発行なし。server DTOを世代付き深いcopyへ。reloadはA/B入力を再開せずreadonly observerのみ |
| `Observer/AuthorityAudit`の自然RETURN記録・`ProductClientProbe.render` | 新世代への参照一致で分類し、旧RETURNを現状態の証拠に使わない。旧値は履歴として保存し、新RETURN未観測はUNKNOWN/UNVERIFIED。permission/valid/reconcile等を観測目的で追加実行しない。HUD無効でもtitle/reload/current refs/native deserialize/非復活観測を継続 |
| `VerificationHud.update/frame` | teardownで一方向DISABLED。context2でも再enableなし。既存20frame draw0をtitle/reloadで採る。旧same-session PASSは維持。観測停止をHUD無効化と混同しない |

**LIVE predicate**：測定がactive、現run/world実パス/UUID/context世代が一致、serverのplayer list・level所属/実entity参照がcurrent、player未removed、listener/Connection/channelがcurrent/open、client側LocalPlayer/ClientLevel/listener/connectionがbind一致、終了latchなし。各sideが所有threadで採取し、cross-threadはimmutable receipt/tokenだけ共有する。connectionやworldがなくなったので健康だったと判定することは禁止。

**TEARDOWN分類**：正常Save&Quit受理tokenと一致するlogout/unload/stoppingでEXPECTED。対応tokenなしのconnection loss、player removal、world null、titleへの急遷移はUNEXPECTED_LIFECYCLE_LOSSとしてSTOP＋無効化する。正常停止イベントは遅いhealth誤検査を止める理由にはなるが、正常終了PASSの根拠を自動で与えない。KILLED/HP不一致・退出直前の失敗はteardownで消さない。latchより前に検出した異常のreceiptを後から上書きしない。最終live operandが採れなければUNVERIFIED、保存値で補完しない。

### 次回1新runの順序・合格/停止条件

1. 新identity/instance/world、既存製品298958B/189entries/hash37E300EA…86592E3と原物を照合。上記helperだけ修正・限定offline compile/reobf/非混入確認。新source/receipt first-write、旧run再利用0。Human入力0、AUTOMATED OS INPUTを維持。
2. 既存条件の安全区画・prepare1→A/B通常復帰→E2/native180/epoch2/sequence2を**positive保存の開始状態を作るsetupだけ**として通る。旧PASSを未実施へ戻さず、閾値/TTL/Lease/native180を緩めない。
3. 通常Save&Quit受理→正規logout/stop→全保存→server thread TERMINATED/context closed→**クラッシュなくTITLE1**。world-null PauseScreen生成0、HUD disabled/title描画0。競合しない時点でactual positive diskをreadonly照合。disk178は旧実測値であり、新runを178へ補正しない。正規positive保存・deserialize一致という既存条件を維持。
4. 同Minecraft/JVM/PIDの同worldを1回だけ通常再読込。new server/thread/level/player/source/listener/connection/channel/context/state/engine、同UUID、positive native deserializeを実測。旧context closed/旧Grant revoked/旧session・Lease・epoch・sequence再利用なし、現authorityなし、旧callback継続なし、persisted positive保持を既存5 END条件で確認。observer未採取falseを「拒否PASS」にしない。prepare/goal/修復再実行0。
5. seal→必要なら既存native cleanup最大1→count/global/dimension通常終了→HUD_DISABLED保持→通常Save&Quit→readonly保存→TITLE2→Quit Game→正常exitとPID消滅。OS key release/watchdog/child終了、製品不変を確認する。

PASSは各receipt/実参照/deserialize/authority/保存/UI/終了が揃った範囲だけ。UNVERIFIEDは観測・identity・タイトル到達・個別operand・actual disk等の証拠不足。予期しない被弾/効果/参照喪失、旧authority復活、persisted positive消失、helper失敗、world-null画面、保存/終了異常で測定STOP・可能な通常終了・証拠保全。receipt削除/値補正/同run再arm/期待値緩和なし。製品・外部native・LOCK仕様変更、新artifactや本人認証が必要なら別承認。追加artifact不足は今回未検出。

これはhelper修正案でありFIXEDではない。成功しても全TimeStop lifecycle/全MOD/vehicle方針/queue #3以降を証明しない。EndingLibrary state・product authorityへの直接setter/外部NBT修復なし。既存観測済みnative unloadのuse(false)と明示cleanupを分離する。

### release queueと新LOCKの配置

旧高栄養bonus廃止の仕様/静的表は[SPEC§12](SPEC.md#legacy-high-nutrition-retirement)、正式回帰未実行は[TEST_PLAN§28](TEST_PLAN.md#legacy-bonus-negative-regression)へ集約。残存実行経路がないため、今回新たな削除実装は不要という静的結論。#2完了後、#6の食事/count工程で関連受入・必要時のみ修正、#8で最終Jar回帰という配置候補とし、固定1〜9の順序を変えない。Flightの外部Creative Flight喪失報告は別件の#7正式release blocker。Mekasuit/Avaritia/Fantasy Ending/他providerは将来の回帰候補で、各artifact充足/原因確定を今回主張しない。

#1 COMPLETE / #2 INCOMPLETE / 残8 / #3 NOT STARTED。SAFE DESIGN PROVEN=NO / SOURCE DIMENSION TRANSITION VERIFIED=NO / ownership BLOCKED、P/T IMPLEMENTATION_PENDING・購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、UOM/P vehicle未LOCK・個別gate維持。可逆クラフト増加は既知許容・バグ修正対象外。次の1作業は、本節のhelper最小差分＋新run残工程一括実行の承認後に着手すること。今回は停止。

<a id="uom-lifecycle-title-pass-reload-observer-stop"></a>
## 14.50. queue #2 lifecycle限定実装・TITLE1成立／same-JVM B observer STOP

更新: 2026-09-28 19:26 JST。**verification-only lifecycle実装・TITLE1正常退出・同PID/JVM再読込・positive deserializeが成立。新run `20260928-190153` は再読込client観測待ちでSTOP。** helperがgen2の自然な`valid(false)` RETURNを待つ間にnative count178が自然に0へ進み、`native positive remains without repair`で停止。5 END=0、旧authority非復活の総合判定UNVERIFIED、seal/明示cleanup各0。停止後は通常保存→TITLE2→Quit、Minecraft/今回Prism/OS補助PID終了。製品不変・HUMAN INPUT=0。

[実測集計](../build/verification/uom-production-os-input-20260928-190153/audit/reviewed-results.json) / [終了PID](../build/verification/uom-production-os-input-20260928-190153/audit/process-final.json)。新root `build/verification/uom-production-os-input-20260928-190153`、instance `FHR_UF_20260928-190153`、world `UP-20260928-190153`。旧run20260927-224439は再利用/改変なし。A/B/E2はpositive保存の開始状態を作るsetup各1回で、旧PASS再証明へ加算しない。

### 補助・成果物の分離

- `ProbeLifecycle`、非cancelの`PauseScreenLifecycleObserver`、既存ProductClientProbe/InputAudit/Observer/AuthorityAuditとMixin登録を**新root内だけ**変更。owner-thread参照照合、世代、正常退出token、removed後health回避、実行直前UI guard、HUD一方向無効化を実装。
- local Gradle8.1.1/cache `--offline` の補助専用task graphでcompile/reobf成功。初回Java pattern型エラーを測定前repair cycle **1/2**で修正、TITLE thread barrierの待機も整合。build01失敗ログ保全、build02成功。測定開始後のrepair/rebuild/retry=0。監査JSON集計の文字コード読取修正はゲーム補助変更ではない。
- 補助v**20260928.190153**、**62,401 bytes /25 entries / SHA-256 `7A32379024B85539BCDE8DF7EB62FD399ECF56F4AAF070FE3AAFDEB2C95B2A16`**。verification classのみ、製品/外部class/ExampleMod混入なし。runtime `PauseScreen.m_261092_()V`を実照合。
- 製品 **298,958 bytes /189 entries /SHA-256 `37E300EAA0663FDCDF445317C9F14F3BF13C839B21E4DD5679229B32386592E3` /protocol7**不変。製品等＋旧run＋SPEC/SKILLの293保護hash差分0、配置10Jar hash一致。製品source/test/build.gradle/Config/原物/gate/SP/schema変更・製品build・既存suite再実行0。3文書の変更前backupは同root `before/docs`。

### 個別結果

| 項目 | 実測・判定 |
|---|---|
| 新環境/prepare | PASS。生成前GUIでSurvival/Normal/Superflat/structures OFF/doMobSpawning=false/naturalRegeneration=false。fresh/HP20/max20/absorption0/effects空からprepare1、P/M各Lv1 ON/Truth未取得/SP0・spent103。通常GUI購入証拠ではない。閉区画/二段階壁、player座標修復0 |
| setup A/B・OS入力 | LIMITED PASS。W各1回、A1005.6369ms/B1002.9504ms、移動A1.199999988/B1.00 blocks。actual input/packet/受理移動/release収束、correction0、native end/G1 revoke。HUMAN0。PID/HWND/focus/creation identity・finally keyUp/watchdog join/child終了/target claim close記録 |
| E2 / positive serialize / disk | PASS。epoch2/sequence2、新Grant、native goal180→runtime179、serialize179/178、通常保存後actual disk178。NBT書換/178への補正なし |
| TITLE1 lifecycle | PASS。NORMAL_EXIT_ACCEPTED-1→expected logout/unload/stopping/stopped、退出入口alive=true/removed=false/HP20/max20/absorption0/effects空。全dimension保存/thread TERMINATED/context closed/maps0/session.lock readonly exclusive open成功。world-null PauseScreen0、HUD20frame draw delta0、crashなし |
| same PID/JVM/world再読込 | PASS。PID27808/JVM start1790590219083、同world/UUIDを1回だけ通常再読込。prepare/goal/input再送なし。server/player/level/listener/connection/channel/context/state/engine、client player/level/listener/connection/channelの新世代bind。実参照比較の到達範囲とhashCode表示を区別 |
| positive native deserialize | LIMITED PASS。同source `4d8892d1-7c0d-4e47-b370-3bb8fafdcf0b`、native HEAD count0/tag178→RETURN count178、各1回。後続countは自然減少。capability旧新actual-ref比較は5 END後の位置にあり**未到達/UNVERIFIED** |
| server側の非復活部分観測 | 現generationの自然permission false累積170/true0（失敗直前DTO）。session2/grant0/epoch0/origin0/binding0、oldRevoked=true、新engine `deserialize-provenance-loss:POSITIVE`、global/dimension=false。旧actual refs不一致/旧context closed/旧Grant revokeのguard通過。これは5 ENDの全入口・旧Lease等を含む総合PASSではない |
| client観測・5 END | **UNVERIFIED / helper STOP**。gen2 client送信snapshot10・server packet RETURN11はあるが、client新valid RETURN freshness0のまま。`reloadFalseValid>0`待ちでB-CLIENT-REFS未作成、controlled END0。count178→0の自然終了時にpositive guard FAIL。旧authority復活の反例・製品damage/movement FAILは確認されていない |
| HUD / UI | TITLE1のmachine draw0成立。reload/停止後TITLE2の画面で検証HUD再表示なし。当時は両箇所の専用machine receipt不足と集計したが、[§14.51のREAD ONLY訂正](#uom-gen2-natural-observation-design)でreload20frame draw0の既存receiptを確認。TITLE2専用不足は維持。live failureのPauseScreen要求はgen2/current refs/safe=true、world-null画面事故・stale世代UIの発生証拠なし。race/fault injection未実行 |
| seal / cleanup | NOT RUN、各0。native countは自然に0、helperによる値補正/再付与/goal/cleanupなし。native unload由来use(false)と自然terminal use(false)をjournalに保持 |
| STOP後保存/終了 | PASS（停止処理限定）。19:20:36通常SaveQuit/全保存、最終live health全条件成立、disk source0/HP20/SP0・103/P/M ON、session.lock解放、TITLE-2-SETTLEDでthread TERMINATED/context closed/maps0。19:21:02 Quit Game/Stopping!、Minecraft27808・Prism8460・Python7612/38368/17772不在。Java数値exit code未採取、exit0と捏造しない |
| successful final teardown | sealなしのためNORMAL_EXIT_ACCEPTED-2/成功用TITLE2_SETTLED未発行。STOP後の正常保存/実TitleScreen/Quitと、seal後成功lifecycleは別判定 |

### STOP原因と残境界

本runの失敗は `ProductClientProbe.render` の `reloadFalseValid==0`待ち→`Observer.reloadClientObserved=false`→server側が5 ENDを開始せずnative positive消尽、という**観測条件の未充足**。自然RETURN未観測をfalse PASSへ補完しない。製品`ClientInputBridge.bridge`はclient native canMove RETURN=false時だけvalidを評価し、`MovementGuard.deny`は停止dimensionでない場合短絡する。global/dimension=falseの今回reload経路で、観測のため追加評価はしていない。欠落の詳細なnative call-site到達切り分けは次のREAD ONLY単位へ残す。

`AuthorityAudit`のgen2 bind後 `matches=true`表示は旧boolean残存だがtimestamp0/listener0であり、新世代RETURNではない。current matches PASSの根拠にしない。旧callback/session/Lease/epoch/sequenceの全非利用、capability実参照比較、client自然入口、5 END/sealは不足があり総合UNVERIFIED。guardを消して成功へ合わせない。

**次の1作業：§14.50の保存済み証拠から、同JVM Bのclient観測待ちをREAD ONLYで切り分け、自然に発生する入口だけで5 END・現Lease不在・旧authority非利用を観測する最小差分を確定する。** gen2ではglobal/dimension=false、自然なvalid RETURN未取得。製品ClientInputBridgeはnative canMove=falseの場合だけvalidへ到達するため、観測目的のvalid/permission追加呼出し・native値修復・TTL/180/期待値変更はしない。今回runの修復/retry/再入場なし。次のhelper修正・新run実行は別承認。TITLE1/positive deserialize/正常Quitを未実施へ戻さず、queue #3以降へ進まない。

#1 COMPLETE / #2 INCOMPLETE / 残8 / #3 NOT STARTED。SAFE DESIGN PROVEN=NO / SOURCE DIMENSION TRANSITION VERIFIED=NO / ownership BLOCKED、P/T IMPLEMENTATION_PENDING・購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、UOM/P vehicle未LOCK・個別gate維持。旧高栄養bonusの§28正式回帰NOT RUN、Flight外部Creative Flight喪失は#7正式release blockerのまま。可逆クラフト増加は既知許容・バグ修正対象外。


<a id="uom-gen2-natural-observation-design"></a>
## 14.51. queue #2 gen2自然入口のREAD ONLY照合・5 END最小差分設計

更新: 2026-09-28 19:56 JST。**READ ONLY / DESIGN ONLY、実行0。** §14.50のsetup A/B/E2、positive disk178、TITLE1、同PID/JVM再読込、native deserialize 0→178、停止後通常Quitは維持。5 END=0／same-JVM B・旧authority総合非復活UNVERIFIED／seal0の当時結果は変更しない。今回は未到達理由と次回の観測設計を確定しただけである。

調査対象はrun `20260928-190153` の[集計](../build/verification/uom-production-os-input-20260928-190153/audit/reviewed-results.json)、[raw journal](../build/verification/uom-production-os-input-20260928-190153/CLIENT/audit/journal.jsonl)、[helper source](../build/verification/uom-production-os-input-20260928-190153/client-helper/src/main/java/verification/client)、現製品source、および当該instanceの保存済み `.minecraft/.mixin.out/class`。bytecodeは同instance `FHR_UF_20260928-190153` 内の変換後 `Minecraft`、`TimeStopUtils`、`TimeStopClientLease`、描画/packet関連classをjavapで読取。classを実行・再変換していない。旧audit scriptの再実行・raw証拠の上書きなし。

### A. STOP原因・自然call flowの確定

| 順序・実箇所 | 実制御フローとgen2への帰結 |
|---|---|
| native client入口 | 変換後 `Minecraft.handler$zkd000$runTick_modifyPartial` はglobalを読む。offset67–70の `ifeq 834` によりglobal=falseなら停止専用tick分岐を通らず、offset470の本人 `TimeStopUtils.canMove` へ到達しない。global=trueでもdimension/paused等の先行条件がある |
| 他の自然client入口 | 保存済み `GameRenderer` bobView/bobHurt、`ItemInHandRenderer.renderHandsWithItems`、`LevelRenderer.modifyEntityPartialTicks` 等もglobal/dimensionの先行分岐を持つ。今回のcurrent LocalPlayerについてgen2 bind後nativeReturnNano=0。未知の全MOD入口まで網羅した主張ではない |
| native canMove本体 | Playerのcreative/spectator、またはLivingEntityのnative capability判定。**canMove本体の先頭にglobal判定があるのではない**。その呼出元が省略する。今回「native true RETURNで止まった」とは記録しない |
| 製品 `ClientInputBridge.bridge` | native canMove RETURNへ注入。変換後 `TimeStopUtils.handler$zzf000$bridge`：client側か判定→native結果trueならoffset20 `ifne 38`で終了→falseだけoffset24 `TimeStopClientLease.valid`→valid trueのみ元結果をtrueにする。server側はvalidを呼ばない |
| valid / Lease参照 | validが呼ばれた場合だけ、lease null/本人以外→false、実参照/期限不一致→clearしてfalse、続いてpacket allowed/session/dimension/global/P条件。今回その入口自体に到達していない。Lease不在snapshotとvalid(false)実行は別証拠 |
| observer配置 | 保存済み `TimeStopClientLease.valid` の3 RETURNすべてに `handler$znc000$valid → Observer.valid` がある（offset35/123/243）。nativeMove observerも製品bridgeより前に存在。hook未適用・false早期RETURNの取り逃しを原因とする証拠なし。`Observer.valid` のjournalはstage/value変化時だけなので、ログ行0単独で未実行と断定せず、gen2 counter・bind後timestamp0と合わせる |
| helperの待ち | `ProductClientProbe.render` の `reloadFalseValid==0` でreturn→reloadClientObserved=false→server ENDはbEndsを増やさずreturn。native178→0を待ち潰してpositive guardでSTOP。製品のdeserialize/Lease/movement FAILではない |

分類は **A：上流native/global短絡、その結果B：当該本人のclient input bridge未到達**。C（valid内部の早期RETURN）は起きていればobserverに届くため今回原因ではない。D（hook位置不備）を示す証拠なし。正規保存count>0は、global/dimension停止やruntime許可の復元を意味しない。

**HUD証拠の訂正**：旧集計・§14.50の「reload machine receipt不足」は証拠一覧の漏れ。[HUD-RELOAD-NO-DRAW.json](../build/verification/uom-production-os-input-20260928-190153/CLIENT/audit/HUD-RELOAD-NO-DRAW.json) に同run/PID27808、2026-09-28 19:20:15.093 JST、SAME_JVM_RELOAD、20frames、draw delta0、renderEnabled=falseが実在する。reload draw0は既存証拠確認済みであり新試験ではない。`HUD-TITLE-NO-DRAW.json` は19:19:12のTITLE1だけ。TITLE2専用machine receipt不足は維持し、旧raw JSON/旧FAILは書き換えない。

### B. 5 END契約と自然到達性

§14.30 B1、§14.32.6–.7、§14.36でいう5 ENDは、**loaded sourceと本人が揃った後の連続5回のserver END**。5種類のAPI/RETURNではない。旧verification候補の明示reconcile/permission/matches評価を、製品を使う今回のobserverから再実行しない。先行deserialize・初期tickからの記録も維持する。

以下の共通条件を固定する。S＝同PID/JVM/run/world、gen2 actual server/thread/level/player/listener/connection/channel、同source UUID/typeかつ新entity/cap、正常canonical/health、native count>0。O＝下表Cの旧authority非利用、現Grant/Leaseなし、epoch/origin/binding0、新engineのPOSITIVE fault/UNKNOWN、自然permission=false。C＝client owner threadで採った新gen actual refs・Lease不在・native flagsのimmutable DTO。同世代のfreshnessは既存150msを維持し、欠測をfalseに変えない。

| END | 自然trigger／server・client | 観測する実call/RETURN・call-site | current generation条件 | old authority条件 | 自然到達性 |
|---|---|---|---|---|---|
| 1 | source/player/client準備後の最初のserver END。clientは自然Render END | 製品 `TimeStopRuntime.tick(END) → SessionRegistry.tick → Ownership.permission` RETURN後、helper LOWESTで受動採取 | S＋C。gen2 bind後の同tick自然RETURNを結ぶ | O。新旧actual-ref比較を既に保存 | NATURALLY REACHABLE |
| 2 | END1直後の次server END、client DTO継続 | 同経路の**新しい**permission RETURNとreadonly状態 | S＋C、同tick ordinal+1 | O、旧counter/binding差分0 | NATURALLY REACHABLE |
| 3 | 次server END | 同経路。前のRETURNを再利用しない | S＋C、ordinal+1 | O、現Lease/Grantなし継続 | NATURALLY REACHABLE |
| 4 | 次server END | 同経路、packetがあれば別の自然記録を対応付ける | S＋C、ordinal+1 | O、旧session/epoch/sequence再利用なし | NATURALLY REACHABLE |
| 5 | 次server END | 同経路、5行を確定して測定完了の通常pauseを要求 | S＋C、ordinal+1、count>0 | O、deserialize1/再prepare0/再goal0/修復0 | NATURALLY REACHABLE |

server END ordinalは実event単位で採番し、停止するworld gameTimeを連続tickの代用にしない。開始後の欠測・gap・不一致はSTOPし、都合のよい5行の選別/カウンタ再開始をしない。clientの5回のvalid RETURNや5packet送信を要求しない。

| 付随入口 | このgen2での分類／扱い |
|---|---|
| native deserialize HEAD/RETURN | NATURALLY REACHABLE・1回。count0→保存positiveの実値を記録。5回呼ぶものではない |
| 製品Runtime END内reconcile/permission | NATURALLY REACHABLE。製品が呼ぶ処理だけ。permissionは内部でreconcileするためobserverからの追加評価は状態を変え得る |
| SessionRegistry.matches | この状態ではNOT NATURALLY REACHABLE。tick内の `!allowed || ...` はallowed=falseで短絡。Grant0も確認。RETURNを人工生成しない |
| MovementGuard.deny | CONDITIONAL（実packet時）。dimension=falseで即false、内部canMove/permission/matchesはNOT NATURALLY REACHABLE |
| client native canMove→valid | 上記global=falseの停止専用分岐ではNOT NATURALLY REACHABLE。0回をfalse RETURN成功へ変換しない |
| client Lease受信 | CONDITIONAL。新ALLOWが届かないことを受信hookの有効な観測区間とserver送信/Grant状態に対応付ける |
| Render ENDのrefs/Lease/HUD読取 | NATURALLY REACHABLE。gameplay呼出しなしでDTOを採る。HUD描画はdisabledのまま |

**受入方法の不整合を修正する設計**：安全要件は旧authority非利用・現許可不在・実参照隔離・positive保持と連続5 ENDであり、不在のAPI RETURNではない。`reloadFalseValid>0`必須を新genのpassive client receiptへ置き換える。製品期待値・native180/TTL/許可semanticsは変更しない。この設計だけで旧runの5 END/B/sealをPASSへ昇格しない。

### C. 既存部分証拠と旧authority非復活の受入証拠

§14.50のgen2自然permissionは失敗直前の累積DTOでfalse170/true0。`AuthorityAudit.permissionReturn` はcurrent ProductClientProbe.playerとactual server owner thread・generationを検査して加算していた。rawは170行のcall stack記録ではない。**caller帰属はsource/bytecodeからの推定を含む**：Runtime ENDが毎tick SessionRegistry.tick→permissionを呼び、自然move packetのguardはdimension=falseでその枝に入らない。170件すべてのcallerを実測済みと書かない。次回RETURNでreadonly stackのcaller種別・server END ordinal・generation・actual player/listener/context/session refsを一体記録する。

当該DTOはsession2、Grant0、epoch0、origin0、binding0、新engine terminalFault/UNKNOWN、global/dimension=falseと対応する。serverの新login session2は新しい接続識別であってGrant/ALLOWではない。client Lease側session0とは役割が違う。これはその期間の本人permission true不在を支持するが、全入口・旧callback・cap新旧・client Lease全区間・5 END・seal・将来の全lifecycle安全性を単独で証明しない。

| authority要素 | §14.50で既にある部分証拠 | 次回必要な自然証拠・観測方法（追加製品callなし） |
|---|---|---|
| State | new state ref/epoch0/origin0、old state非currentのguard通過 | 各ENDで既に生成済みcontext.statesの実key/valueをreadonly確認。new level→new state、oldStateを含まない。未生成ならABSENT、computeIfAbsent禁止 |
| Context / engine | old context closed、new context/engine refs、old maps0 | actual serverの既存 `foodhealing$authority` fieldから読む。new context!=old、所属server一致、closed旧map/frames/stack/witness/bindingsのまま。lazy context getterで作らない |
| Grant | oldGrant/saveGrant revoked、現Grant0 | 既存sessions/grants mapの本人entryと旧Grant実refを5 END確認。自然send/receiveのALLOW0も対応。旧G1/新listenerへmatchesを追加実行しない |
| session | 新server/player/listener/connection/channel、session2 | natural loginの既存Sessionを読み、old Sessionと不同一・現refsに所属。旧sessionをcurrent entryへrebind0。番号だけの不一致で済ませない |
| Lease | gen2 client packet snapshotでpresent=false/packet=null/session・revision・sequence・expires0/cache refs0 | bind後の自然Render ENDから5 END末までcurrent client refsとLease fieldsを継続記録。receive hookの観測coverage・ALLOW0・revision非増加を記録。保存0を復元値と推定しない |
| epoch / sequence | server epoch0/現Grantなし、client sequence0、旧E2は2/2 | old値によるgrant/send/receive/許可化0、上記自然RETURNと実map/packetを対応。JVM全体のSERIAL/SEQUENCEが過去値を保持すること自体は旧許可利用ではない。全static counter0は要求しない |
| origin / binding | source origin0/engine binding0 | 既存mapをreadonlyで採取、oldUUIDのorigin復元/old entityやnew entityへの旧binding再注入0。native countの正規保存とは分離 |
| callback / witness | oldEngine.consumedがbaseline不変というguard通過 | closed old engineのcreated/consumed/committed・frame/stack/witness/bindingのbaselineと各END差分、自然callback observerの対象実refを保存。旧continuation0を区間付きで確認。未発生hookに合成RETURNを足さない |
| old authorityをnewへ再bind | new refs・origin/binding0で部分支持 | State/Context/session/Grant/callbackの上記entry・refsと自然RETURNを同ENDに束ねる。静的設計だけで実測PASSにしない |
| 正規native保存 vs runtime authority | HEAD actual0/tag178→RETURN178、同UUID/type | native deserialize引数cap/entityを捕捉し新旧実refを比較。positiveを保持したままfault/UNKNOWN・許可不在が両立することを5 END確認 |

既存clientの `actualPlayerMatch=false / actualConnectionMatch=false` は**Lease内部のnull参照と実player/connectionが一致しない**という値である。現clientそのものが無効という意味ではない。bind前のpacket snapshotに残ったgen1 native/valid timestampもgen2証拠から除外する。

**AuthorityAudit stale値**：`bindGeneration()` はpermissionPlayer/matchGrant/matchListenerをnull、permissionAt/matchAtとcounterを0にするが、`permitted`/`matched`をresetしない。matchesReturnには独立generation guardもない。旧matched=true＋nano0/listener0はUNOBSERVED。次回はenum相当のUNOBSERVED／OBSERVED_TRUE／OBSERVED_FALSEと、generation・monotonic時刻・ordinal・actual refs・callerをひとつのimmutable recordにする。bindで旧recordをhistoryへ移し現recordを空にする。未観測booleanをfalseへ初期化してPASSにする修正はしない。

**自然packetの限界**：gen2 client move snapshot10、server HEAD/guard/RETURN11、actualDeny=false。`MovementGuard.deny` はdimension=falseでnative canMove/permission/matchesを呼ばずfalse。これは通常packet処理に旧authorityが必要なかったことをsourceと対応付ける補助証拠であり、「旧Grant matches=false実測」ではない。acceptedMovingはgen1の13から増えていない。idle packetをgen2 movement成功、client input bridge成功、停止下ALLOW成功へ転記しない。

**観測不能部分の明示**：このgen2で呼ばれないoldGrant×newListenerのmatches RETURN、client valid(false) RETURNは追加callなしでは取れない。代わりにその入口の未到達、旧許可実体不在、現map/refs、自然permission/packetの記録で**今回の自然runの非利用**を判定する。人工的な旧packet注入への拒否や全call-siteの反例探索を今回新必須にしない。observer断絶や既存fieldにアクセス不能ならUNVERIFIED/STOPであり、absenceを捏造しない。

### D. 最小verification-only差分（次回承認後。今回は未作成）

変更先は**新verification rootのclient-helperだけ**。旧runのsource/Jar/receipt/journalは保全。下記は既存 `src/main/java/verification/client/` 配下のファイル単位。authority／native／packetを書き換える新hookは不要。

| ファイル・メソッド | 次回の最小差分 |
|---|---|
| `ProductClientProbe.java: render` | gen2のreloadFalseValid待ちと同assertだけを受動readyへ変更。通常login後のcurrent actual client binding/canonical/health、Lease不在/refs0、global/dimensionfalseを自然Render ENDで採取。gen1timestampは不可。valid/native RETURNは参考の条件付き観測のまま |
| `ProductClientProbe.java: tick / snap` | sourceは自然deserializeの引数から捕捉した実entity/capを使う。既存生成済みcontext/state/engine/sessionのreadonly viewを使い、gen2証拠生成のためのsource lookup・lazy getter・permission等評価を足さない。LOWEST ENDで直前の製品Runtime ENDの自然RETURNとfresh client DTOを照合して連続5 END。不足時の再count開始なし |
| `Observer.java: serialize / deserializeHead / deserialize / client / lease / receive` | gen1 serializeで実old cap/entityを保持。gen2 deserialize RETURNで新cap/entity/level/serverを**直ちに実==/!=比較して記録**、後続bind時にnew State/Context/engineを追加照合する二段階receipt。identityHashCodeは表示だけ。client DTOはowner thread採取・世代/refs/timestamp/ordinal付き、ログ間引きと受信総counterを分離。native/dimensionの既存field/cacheが未生成ならUNINITIALIZED（falseではない）。新source検索やcache生成なし |
| `AuthorityAudit.java: bindGeneration / permissionReturn / matchesReturn / snapshot` | stale booleanを廃し上記一体recordへ。既存非cancel RETURN hookのactual引数・thread・generation・実refに限定し、自然callerをreadonly stackで分類する。permissionの自然RETURNを1 ENDに1記録として結び、重複tick再使用なし。matches未到達はUNOBSERVEDのまま。製品 `decision/permission/matches/reconcile` を観測のため呼ばない |
| `ProbeLifecycle.java: serverIdentity / client binding`、`InputAudit.java: bindGeneration / gen2 snapshot` | gen2のreadonly viewを共有し、既存context/State fieldを読むだけにする。owner threadでactual==/!=を比較し不変DTOをpublish、他threadはDTOだけ読む。bind前の旧last-return/health/Lease証拠を新genへ流用しない。正常退出token/health/owner/ref guardは維持 |
| `VerificationHud.java: frame / proof` | disabledは一方向のまま。既に存在するreload20frame draw0処理は維持。titleProof/titleFramesをTITLE1/TITLE2別の世代キーに分け、各20frame/draw差分を一度だけ記録。TITLE2をTITLE1一回限りbooleanで取り逃さない。再enable/再描画なし |
| `ProductClientProbe.java: seal / cleanup` | 下記Eの必要receiptをseal前に集約。cleanupはseal後count>0の時だけ既存native終了最大1、既に0なら呼出0のSKIPPED_ALREADY_ZERO receipt。非正規fault reset/許可clear/HP修復を行わない |

既存 `mixin/AuthorityResultObserver.java` / `GrantMatchObserver.java` / `LeaseObserver.java` / `DeserializeObserver.java` の非cancel RETURN hookは原則再利用。新しい製品call-siteやMixin targetを捏造しない。helper version/run/path/manifestのみ新runへ限定更新、compile/reobf・task graph/非混入確認は次回承認対象で今回は0。

**cap比較の前倒し**：deserialize時にplayer未bindでもcap/entity/server/levelは実引数に存在するので即比較可能。旧cap未取得・null・同一なら記録欠落/隔離不成立としてSTOP。State/Context/engineが自然にはまだ無ければPENDINGと記録し、製品Runtimeの通常生成後にfieldから比較する。比較のため生成しない。5 END完了まで待つ理由はない。

**時間窓**：旧raw nanoでdeserialize RETURN 413175163953800→server bind413175851910700（約0.688s）→client bind413176018497400（約0.855s）→native count0のuse(false)413184464618600（約9.301s）。client bind後約8.446sあった。20TPSなら5 ENDは約0.25sで収まる候補だが、lagやpauseを無視して保証しない。最初の自然readyから直ちに5 ENDを採取し、存在しないRETURN/HUD20frame/UI往復を前置しない。150msのfreshness・native180/TTLを変えず、positive消尽前に完了できなければUNVERIFIED/STOP。count列は実値を記録し178固定や毎tick必ず−1という新条件は足さない。source値の停止/延長/再設定なし。

### E. 次回1新runの順序・seal・終了

1. 新root/新instance/worldだけ。既存artifact/hashを照合し、上記helper差分の限定offline compile/reobf・非混入確認。旧run再利用0。HUMAN INPUT=0、初回安全区画・prepare1、A/Bの既存AUTOMATED OS INPUT各1、E2 goalを含むnative goal合計2は**positive保存前提を作るsetupのみ**。旧PASS再証明や別suiteは加えない。
2. 正規native positive→通常saveでactual positive diskを確認→既存NORMAL_EXIT_ACCEPTED-1、FINAL-LIVE-HEALTH-1、EXPECTED TEARDOWN、TITLE1_SETTLED、thread終了/context closed/maps0/lock解放/HUD draw0。失敗をNBT修復や再試行で合わせない。
3. 同PID/JVM/loader/worldを通常reload1。prepare/goal/inputの再送0。先行deserializeからobserver継続、同source UUID/type・tag/actual positive・新cap/entity実参照を直ちに記録。gen2 actual server/player/client refsとcanonical/healthを通常loadで確認。
4. 新gen passive client readyと自然server permission RETURNが揃う最初のENDから5連続END。表B/S/C/Oと表Cの区間証拠を保存する。受信coverageを確保し、Lease/Grantがない、old/new map/refs/counterに旧再利用がないことを確認。valid/matches未実行のままでよい。HUD reload20frameは並行/完了後pause中に採り、positive窓の前提にしない。
5. 5 END確定後に通常pauseし、**sealの全条件**を照合：同PID/JVM、deserialize1/positive入力、実new refs/cap比較、旧State/Context非current、旧Grant/session/Lease/origin/binding/epoch/sequence/callback非利用、現Grant/Leaseなし、自然permission false・true0、sessionは新接続識別だけ、POSITIVE fault/UNKNOWNと整合、5連続END、正常health/canonical、helper failure0/repair0/追加authority call0。必要証拠が一つでも欠ければseal不可。readonly確認だけで旧§14.50を成功へ変えない。
6. seal後にnative countを読取。自然に0なら明示cleanup0（global/dimensionも自然終了、意味の不整合はSTOP）。positive残存時だけ同source実refへ既存許可 `TimeStopUtils.use(false, source, true, 0, false)` 最大1を**測定後操作**として記録しnative結果0を確認。途中でpositive消尽して5 END未完了なら失敗のままであり、cleanupで成功を作らない。
7. 通常Save & Quit→成功用NORMAL_EXIT_ACCEPTED-2→退出入口FINAL-LIVE-HEALTH-2→実logout/unload/stopping/stopped→全dimension保存→thread TERMINATED/context closed/states・sessions・grants等0→TITLE2_SETTLED、session.lock解放→競合しないreadonly保存照合（本人P/M1ON/SP0・103/HP20、source実保存、transient authority非保存）。HUD-TITLE2-NO-DRAWは20frames/delta0の専用receipt。期待退出tokenなしのSTOP後通常保存をこの成功列へ転記しない。
8. Quit Game/Stopping log・該当Minecraft/今回Prism/OS補助process終了、keyUp/watchdog/claim閉鎖、製品/配置/原物/旧証拠hash不変を照合。数値exit code未取得なら未取得と書き、exit0を補完しない。3rd load/再prepare/再goal/receipt消去はしない。成功時または本質STOP時に3文書を1回更新。

`deserialize-provenance-loss:POSITIVE` はnative正規保存の復元からruntime provenanceを生成しない設計上のsafe-side faultである。`EntryDeserialize.head`でPOSITIVE分類時に発生、RETURNはnative値一致を確認するがfaultを解除しない。`Ownership.reconcile`もterminalFaultをquietより先に処理する。新engineのこの理由と旧engine継承を区別し、UNKNOWNをfalseへ修復しない。自然count0になってもこのfaultが残ることと、old authority revivalは別。5 ENDで新faultを理由に誤利用を見逃してはならず、permission=falseだけでsealしない。

**判定境界**：自然許可true/旧実体再利用/予期しないALLOW・Lease復活/参照混同は安全性FAILとして停止。5 END欠測、positive消尽、必須actual-ref/DTO/receipt不足はUNVERIFIED＋helper STOP。観測hook/field不整合はverification問題として記録し、測定中のguard緩和・同runリセット不可。製品/LOCK/authority/TTL/native仕様の変更が必要なら別承認で停止。製品source/Jar、原EndingLibrary/Fantasy Ending、shader/login compat/protocol/gateは一切変更しない。追加artifact不足は今回の局所設計では認められず、既存物を使用する（次回実物照合は必要）。

今回は3文書と変更前backupのみ。起動/新run/helper変更/compile/reobf/build/unit/check/GameTest/OS入力は全0。#1 COMPLETE / #2 INCOMPLETE / 残8 / #3 NOT STARTED、SAFE DESIGN PROVEN=NO / SOURCE DIMENSION TRANSITION VERIFIED=NO / ownership BLOCKED、P/T IMPLEMENTATION_PENDING・購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、vehicle未LOCKを維持。Flightは#7正式blocker、旧高栄養bonus§28 NOT RUN、可逆クラフト増加は既知許容・バグ修正対象外。**次の1作業は本節のverification-only最小差分と1新run残工程一括実行について利用者承認を受けること。今回は設計完了で停止する。**


<a id="uom-gen2-bind-uninitialized-cache-stop"></a>
## 14.52 queue #2 — gen2自然観測の限定実装・初期cache観測STOP

更新: 2026-09-28 20:57 JST。§14.51と今回の明示承認に基づく **verification-only最小実装＋1新run**。新run `20260928-203401`、instance `FHR_UF_20260928-203401`、world `UP-20260928-203401`。旧run20260928-190153のworld/helper/receipt/instanceを保全し再利用0。[判定・実測集計](../build/verification/uom-production-os-input-20260928-203401/audit/reviewed-results.json)、[原journal](../build/verification/uom-production-os-input-20260928-203401/CLIENT/audit/journal.jsonl)。手順・期待値は§14.51を維持し重複定義しない。

### 実装・build境界

新root `build/verification/uom-production-os-input-20260928-203401/` のhelper内だけを変更。`ProductClientProbe`（raw gen2 state/自然END/cleanup）、`Observer`（受動DTO・deserialize直後実比較）、`AuthorityAudit`（UNOBSERVEDと世代/実refs/caller/ordinal）、`ProbeLifecycle`（raw既存engine）、`InputAudit`（新gen観測時刻）、`VerificationHud`（TITLE世代別receipt）、既存`GrantMatchObserver`（非cancel send入口観測）とmetadata/run配線。authorityを追加評価するcall-siteは加えず、製品/原EndingLibrary/Fantasy Ending/shader・login compat/TTL/native180不変。helperのgen2新方式は実装したが、全動的成功ではない。

local Gradle8.1.1・既存cache・offlineのhelper専用task graphを確認。製品compile/jar/reobfJar/build/unit/check/GameTestを巻き込まず限定compile/reobf成功。初回compileは括弧1個のsyntax不備、測定前repair **1/2** で成功。初回sandbox native DLL拒否は同じoffline権限付き実行で解消。OS bind初回もsandboxでwindow列挙不成立（TARGET未作成、keyDown0）、承認済み権限付きbind成功後にdry-run/測定へ進んだ。失敗logは保持、権限不足を製品FAILにしない。

helper v20260928.203401 = **71,717 bytes /27 entries / SHA-256 `FDCBC861766D54756222ED99532FA1626A91B99A410D0F008BED60B9712404A2`**。verification classだけで製品/外部class・外部Jar/ExampleMod混入0。製品は **298,958 bytes /189 entries /37E300EAA0663FDCDF445317C9F14F3BF13C839B21E4DD5679229B32386592E3 /protocol7**。前後763保護hash差分0、配置10Jar一致。製品source/test/build.gradle/Config/購入gate不変。測定後repair/build/retry0。

### 今回の個別結果

| 項目 | 判定・実測 |
|---|---|
| helper build / product separation | PASS（測定前syntax repair1）。上記hash/非混入確認 |
| 安全準備 / prepare | PASS。生成前GUIでSurvival/Normal/Superflat/structures OFF・自然湧き/回復OFF。閉鎖区画最大前方1.199999988blocks、prepare1・P/M Lv1 ON・Truth未取得・credit/spent103・SP0・HP20/effects空。通常GUI購入成功ではない |
| setup A | LIMITED PASS。AUTOMATED OS INPUT W1006.2757ms、通常Minecraft input/packet/移動/release/収束。server最大1.199999988blocks。旧PASSの再加算ではなく保存状態の前提 |
| setup B | LIMITED PASS。OS W1005.91ms、通常復帰/実入力/移動/release/収束。最終Z4.699999988、A終了Z3.699999988から1block。HUMAN INPUT=0、A/B各1、watchdog終了/keyUp/claim close |
| E2 / positive disk | LIMITED PASS / PASS。native goal合計2、E2 epoch2/sequence2/session1、runtime179、serialize179/178、実disk178。NBT修復なし |
| TITLE1 | PASS。NORMAL_EXIT_ACCEPTED-1/FINAL-LIVE-HEALTH-1/全保存、thread TERMINATED、Context closed/maps0、HUD20frame draw0。`TITLE1_SETTLED.server.thread`はstopped時の古いRUNNABLE DTO、実終了は`TITLE-1-SETTLED.thread=TERMINATED/threadAlive=false`で別確認 |
| same PID/JVM reload | PASS。PID38780/JVM1790595727821のまま、同worldを通常reload1。再prepare/goal/input0 |
| native deserialize / actual capability | PASS。同UUID `d427676a-1fe5-447b-aabd-367c04194bc2`・UOM、actual0→178。deserialize RETURNで実`!=`比較：Entity/capability/level/serverすべて旧と不同一。cap表示ID2056671056→231928719、表示hashだけの推定ではない |
| State/Context/engine実比較 | UNVERIFIED。新genの表示refsは記録されたが、`isolation()`の旧との実比較・全非利用確認は未到達。cap分離から転記しない |
| passive client ready / Lease absent区間 | UNVERIFIED。current server/client bindは記録されたが、terminal後に受動readyへ進まず。valid/matches未到達をfalse成功にしない |
| END1 / END2 / END3 / END4 / END5 | 各NOT RUN、B-CONTROLLED-END=0。後続の自然permission408 RETURNはすべてfalse/true0でcaller/ordinal付きだが、client DTO/隔離を束ねた5 ENDの代用にはしない |
| old State / Context / session / Grant / Lease非復活 | 各UNVERIFIED（総合受入区間なし）。新許可復活の製品反例とは認定しない |
| old epoch・sequence / origin・binding / callback非利用 | 各UNVERIFIED。closed old engine baselineは保存済み、5 END差分比較未到達 |
| same-JVM B overall / seal / explicit cleanup | UNVERIFIED＋helper STOP / NOT RUN0 / NOT RUN0。native自然終了とexplicit cleanupを混同しない |
| final disk | STOP後readonly PASS。HP20・P/M各1 ON・SP0/spent103、source自然0、transient key混入0、gamerules保持、session.lock解放 |
| TITLE2 / TITLE2 HUD | STOP後通常退出PASS、HUD専用receipt20frames/draw0/renderEnabled=false。thread TERMINATED/closed/maps0。成功用NORMAL_EXIT_ACCEPTED-2/TITLE2_SETTLEDは未発行 |
| Quit / process | STOP後正常Quit PASS。20:52:32.919 Stopping!、Minecraft38780/今回Prism31540/supervisor27392/child7352・9252不在。OS childは各exit0・watchdog false。Java/Prism数値exit codeはUNOBSERVED |
| successful sealed final teardown | NOT RUN。STOP後正常終了をseal成功後の終了へ転記しない |

### STOP原因と次の境界

[FAILURE.json](../build/verification/uom-production-os-input-20260928-203401/CLIENT/audit/FAILURE.json) = `IllegalStateException: native dimension cache not initialized`、generation2/BINDING/Server thread。実装の `ProductClientProbe.login → log(LOGIN,snap()) → dimension()` が、まだ自然初期化されていない `ServerExpandedContext.timeStopSavedData` を読み、`rawDimension=UNINITIALIZED`にBooleanを要求した。§14.51で意図した「未初期化は未観測」の扱いが初期snapshotまで一貫しておらず、**今回のverification helperの不備**。製品仕様・authority不具合が確定したという意味ではない。

deserialize実参照比較はこのSTOPより前に完了した。後続のnative tick/通常保存は続いたが、5 ENDやclient readyの証拠へ後付けしない。失敗検出後は通常pause→Save & Quit→readonly→Quit。receipt/failure/reset、count/HP/Lease/Grant修復、測定後helper修正、同run再試行、新run追加はすべて0。

**[§14.52](MASTERY_IMPLEMENTATION_PREPARATION.md#uom-gen2-bind-uninitialized-cache-stop)で特定したgen2初期snapshotのUNINITIALIZED扱いについて、READ ONLYで最小修正境界を確定する。** login時の記録を受入判定から分離し、未初期化をfalseに変換せず、nativeが自然に初期化した値だけを5 ENDの判定へ使う必要がある。今回は測定後repair/rebuild/retryを実施しない。次のhelper変更・1新runは別承認であり、旧run/receipt/worldは再利用しない。追加authority評価、cache生成、native値修復、期待値緩和、#3開始は行わない。

追加artifact不足なし。§14.50の部分PASS/STOP・§14.51設計と過去全PASSは当時の範囲で保持。**#1 COMPLETE/#2 INCOMPLETE/残8/#3 NOT STARTED**。SAFE DESIGN PROVEN=NO、SOURCE DIMENSION TRANSITION VERIFIED=NO、ownership BLOCKED、vehicle未LOCK、P/T IMPLEMENTATION_PENDING・購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKEDを維持。Flight #7、旧高栄養bonus §28 NOT RUN、他個別開始条件不変。可逆クラフト増加は既知許容仕様・バグ修正対象外。

<a id="uom-gen2-native-cache-boundary-design"></a>
## 14.53 queue #2 — gen2 native cache初期化境界のREAD ONLY設計

更新: 2026-09-28 21:19 JST。**設計のみ・helper未変更/未build・起動/入力0**。変更前3文書は `backups/20260928-211252-gen2-cache-design-docs/` に保全。§14.52のrun/receipt/worldは不変。受入全体・seal・正常退出列は§14.51、前runの結果は§14.52を参照し、ここでは初期snapshotの最小修正境界だけを定める。

### A. 確定原因と証拠の限界

`ProductClientProbe.login → log("LOGIN", snap()) → dimension()` が、serverの `ServerExpandedContext.timeStopSavedData == null` に対してBooleanを要求した **verification helper initialization bug**。deserialize/ownership/製品安全性FAIL、Lease漏れ、dimension真偽の証拠ではない。LOGIN記録自体は残し、受入assertを外す。ログの `FAILURE.serverDto` と `lastClient` はgen1、`lastServer` はgen2であるため、同じJSON内でも世代を混ぜない。

読取根拠：[原journal](../build/verification/uom-production-os-input-20260928-203401/CLIENT/audit/journal.jsonl)、[FAILURE](../build/verification/uom-production-os-input-20260928-203401/CLIENT/audit/FAILURE.json)、[結果集計](../build/verification/uom-production-os-input-20260928-203401/audit/reviewed-results.json)、同run `client-helper/src/main/java/verification/client/`、現製品 `TimeStopRuntime/SessionRegistry/Ownership/MovementGuard/TimeStopClientLease`。EndingLibrary **2.1.19fix**・FE **2.7.20**の承認原Jarを静的disassembleし、同instance `.minecraft/.mixin.out/class` の実変換 `ClientLevel`・`TimeStopUtils`・native loginイベントも照合した。`javap`はクラスの実行/ゲーム起動ではない。

| 実call-site | 初期化・更新と今回の観測境界 |
|---|---|
| `ServerExpandedContext.<init>` / `getTimeStopSavedData()` | constructorはcacheをnullにする。getterはnull時だけ `TimeStopSavedData.readOrCreate(server)` → overworldのDimensionDataStorage読込/生成 → field格納。**helperからgetterを呼ばない**。通常の`update()`は別のEndingLibrarySavedDataを扱い、このcacheの初期化保証ではない |
| `TimeStopRuntime.login → SessionRegistry.login → Session.<init>` | `freshEpochRequired = Ownership.global() || Ownership.stopped(level)`。global=falseなら `stopped → TimeStopUtils.andSameDimension(serverLevel) → native lazy getter` に自然到達する。guard成立時の通常loginが具体的な初期化入口。別の自然入口は移動packetの `MovementGuard.deny` 先頭。同メソッドを観測目的では呼ばない |
| `Ownership.reconcile/permission` | POSITIVE deserialize faultの短絡が先にある。自然permission=falseだけではdimension cache生成を証明しない。false408/true0は従来どおり部分観測 |
| `ClientLevel.<init> → handler$zkg000$init → new EndingLibrary ClientLevelExpandedContext → endinglib$setECData` | 実変換constructor内でnative contextをattach。context constructorは `currentTimeStopDimension=null` を明示設定する。clientにserverの`timeStopSavedData`はない。実変換classにはFE側の同名fieldもあるため、field名だけでなく**EndingLibraryの完全型descriptor**で識別する |
| `TSDimensionSynchedPacket.handle → enqueueWork → handle0` | client threadで、current dimensionに一致するaddをkeyへ、removeをnullへ書く。enqueueだけを適用完了にしない。自然handle0 RETURNを観測可能だが、packetを要求/補送しない |
| `ClientLevelExpandedContext.isCurrentTS()` | 実bytecodeはkey非null→true/null→false。これはnativeの意味である。しかし今回の明示禁止に従い、helperの `raw field != null` をそのままOBSERVED_FALSEへ昇格させない。追加呼出しによるBoolean生成も本設計では採用しない |
| `ClientLevelMixin.tick → ClientLevelExpandedContext.tickHead` | global=trueの時だけ `andSameDimension → isCurrentTS` を呼ぶ。global=falseなら `ClientContext.isTimeStop_andSameDimension=false` を直接書く。後者は**globalとの合成結果**であり、独立したdimension=falseの証拠に代用しない |
| `TimeStopUtils.isTimeStop` / native global packet | static volatile primitive boolean。integrated環境では同JVMの共有fieldであり、独立したserver/clientコピーではない。各owner threadで現値を別々のgen2 DTOへ読取記録し、server DTOをclientへ転記しない。`TimeStopSkillPacket.handle0 → TimeStopUtilsWrapped.enable/disable` が自然true/false更新経路 |
| native `MinecraftMixin.runTick_modifyPartial` | level=nullかつglobal=trueのTITLE側経路でnative自身がglobal/合成client flagをfalseへ戻す。gen2で新たなglobal packetが必須とは限らない。古い観測DTOは使わず、新しい現在field読取時刻を付ける |
| native login `CommonEventHandler$TimeStopEvents.onPlayerLeave(PlayerLoggedInEvent)` とlogin compat | メソッド名はleaveだがlogin overload。global=falseならnativeのglobal packet送信もcompatのdimension packet送信も行わない。**「login後、次のpacketで必ず初期化される」という設計は不成立** |

**残る観測上の制約**：serverの初期化入口とclient contextの生成/attachは特定できた。一方、clientの独立dimension Booleanはglobal=falseで自然評価が短絡される。対象原Jar内の実呼出しも照合し、FEのgray post-effect `canUse` もglobal=falseで短絡する。旧helperのclient `dimension=false` は `key != null` による表示で、native Boolean RETURNの観測ではない。厳密な今回の禁止条件では、native contextが完成済みでも、独立dimension Booleanは未観測として残り得る。待機だけでREADYへ到達できるとは認定しない。

### B. 記録・tri-state・受入を分離

最低3状態に、物理的初期化済みとBoolean未観測を混同しない補助状態を設ける。

| 状態 | LOGIN記録 | helper FAIL | PASSIVE READY | 5 END使用 |
|---|---|---|---|---|
| UNINITIALIZED | YES、欠けているfield/context/refと理由を記録 | deadline内はNO | NO | NO |
| INITIALIZED_UNOBSERVED | YES、native constructor/attach済みだが独立Boolean未観測 | deadline内はNO | NO | NO |
| OBSERVED_FALSE | YES、side/根拠call-site/時刻/actual refs付き | 単独ではNO | 全他条件成立時のみYES | 各ENDの全条件成立時のみYES |
| OBSERVED_TRUE | YES | gen2 quiet受入の期待に反すればSAFETY FAIL/STOP | NO | NO |

`UNINITIALIZED`は「false」ではなく「受入に使えない」。null/default値補完禁止。clientの初期化済み空keyも `rawKey:null / initialized:true / boolean:UNOBSERVED` と保持し、勝手にFALSEへ置換しない。UNKNOWN authorityとcache未初期化は別分類。

**LOGIN/BINDING DTO**：run/generation/lifecycle/owner-thread/nano/current actual refs、rawGlobal/rawDimension、cache/contextの存在・完全型・native初期化証拠、未観測理由だけを診断記録する。State/Context/engineのfieldが未生成ならPENDING。`Map.of`のnull拒否やsnapshot連鎖によるassert/NPEも避ける。記録のためのlazy取得/生成・authority評価はしない。

**ACCEPTANCE DTO**：自然生成/自然評価済みの同gen2だけを使用する。clientはactual LocalPlayer/ClientLevel/listener/Connection/channelの現在一致・旧との実`!=`、run/PID/JVM、canonical正常（P/M1 ONのみ・SP0/spent103・pendingなし）、HP20/max20/absorption0/effects空、Lease独立不在、global/dimension各OBSERVED_FALSE、native初期化根拠、gen2 owner-thread timestampを要求。Lease不在はpresent=false、packet=null、session/revision/sequence/expiry0、cached refs0、ALLOW受信0を個別確認。Lease不在だけでcache条件を代替しない。binding完了はREADYではない。

gen2開始時、旧DTO/Boolean/packet/時刻/valid結果はhistoryへ移す。current観測stateを未観測へ戻すのは**helper内だけ**で、native fieldを初期化しない。constructor/attachがhelper client bindより先に完了した観測は、一時保管したactual refを後からcurrentと照合する。同genを示す数字だけでは採用せず、actual refs・開始時刻・owner threadが揃って初めて採用する。

### C. 実時系列・race・timeout候補

基準 `t0=418658260877500ns` はgen2 native deserialize HEAD。下記は原journalの記録時刻で、実処理開始時刻との同一視はしない。

| t0から | 観測 |
|---|---|
| +0.614ms未満 | CAP実比較、native deserialize RETURN0→178 |
| +789.432ms | SERVER-LIFECYCLE-BIND。gen2のContext/State/engine表示refsあり、実比較は未到達 |
| 約+790.512ms | FAILURE pause要求のissuedNano。cache未初期化assertの直後。例外そのものの正確な発生nanoは記録なし |
| +792.844ms | 最初の自然permission=false RETURN |
| +911.682ms / +912.510ms | client / server packet付随snapshotの初回false表示。clientは旧raw-null判定で、native Boolean証拠ではない。serverは実cacheが非nullになった読取証拠 |
| +922.322ms | CLIENT-LIFECYCLE-BIND。既にterminalでREADYへ進まない |
| 約+21.132sまで | 自然permission false累計408/true0。STOP後を含む |
| count消尽 | **正確な時刻は未観測**。`Observer.nativeUse`がhelperの未bind `source` をfilterし、gen2終端receiptがない。最終disk0は消尽時刻を証明しない |

server cache初期化は失敗時のnullと+912.510msの非null読取の間。自然loginと通常packetの実call-siteは上記Aで特定したが、旧runでどのgetter呼出しが最初の書込を行ったか・正確なnanoは未記録で、推測で確定しない。client context attachと独立Boolean評価の時刻も別問題。

- **順序B**（deserialize→server bind→server cache init→client bind）は今回の証拠に整合する。cacheの厳密な書込時刻ではなく上記区間による判定。
- **順序A**（server bind→対象source deserialize→client bind→cache init）はこのrunでは観測されていない。source chunk/entity読込とplayer loginは別経路であり、全worldで順序固定とは断定しない。各入口でpending recordを照合するが、Aを実測済みにしない。
- **順序C**：helper bindよりnative client constructor/attachが先なのは実変換constructorで確認済み。したがって「current ClientLevel完成後もcontextがなく、次tickがcontextを生成する」というCは採用しない。**Boolean観測がbind後に来る**Cは条件付きで可能。ただしquiet時の次tickは合成falseだけ、packetは送信されないため、独立dimension Booleanへ自動収束するという前提を置かない。

期限候補は **gen2開始から初期必須入口を待つ上限5秒、最初の対象deserialize HEADまたは対象server bindの早い方からREADY上限2秒**。両方未到達なら5秒でSTOP、途中のbindで期限を延長しない。今回bind群が約0.923秒以内だったことに対し、2秒は約1秒の余裕を持たせる。5 END完走は同じ基準から3秒以内、かつ毎END count>0を必須とする。178tickは20TPSなら8.9秒という**目安だけ**で、このrunの消尽実測値ではない。実count消尽/必要refs消失/欠測が先なら即STOP。lagがあっても180/TTL/countを停止・延長しない。期限変更で再試行しない。

deadline内の未観測はNORMAL BINDING/PENDING、期限切れはUNVERIFIED/STOP。自然falseへ収束していないという理由だけで製品SAFETY FAILにしない。自然true/Lease復活/ALLOW/旧authority実再利用は別のSAFETY FAIL。BINDING中にもwatchdogが動く必要があり、LIVEへの昇格をwatchdogの前提にしない。

### D. 5 ENDと次回最小差分

自然生成済みState/Context/engineを既存field/mapから読み、旧actual objectと比較する。PENDING時は待機し、生成のための `of/state/current/get` 等は呼ばない。Entity/capability/level/server分離の§14.52 PASSは保持し、State等の未完了へ転記しない。

**開始**：PASSIVE READYの最初の成立nanoを固定。その後に発生した同gen2/current playerの `TimeStopRuntime END → SessionRegistry.tick → Ownership.permission` 自然RETURN=falseと、全actual隔離条件が揃う最初のeligible server ENDをEND1とする。BEGIN/LOGINやUNINITIALIZED期間のEND、READY以前のRETURNは除外する。LOWEST観測時に既存150ms client freshness、owner-thread DTO、native positive count、旧engine不変/現Grant0/Lease0/ALLOW0等の§14.51条件を全確認する。

**継続**：END2〜5はserver END ordinalが直前+1で、新しい自然RETURNと新しいcurrent client DTOを対応付ける。freshnessだけを根拠に古いRETURNを再使用しない。gap/欠測/positive消尽/actual refs変化/再UNINITIALIZEDならSTOP、選別・カウント巻戻し・新しい5行の採り直しなし。§14.52の408falseを先取りのEND1〜5へ足さない。

| 次回変更予定（新verification root内だけ） | 最小内容 |
|---|---|
| `ProductClientProbe.java`：`login/snap/rawDimension/dimension/tick/isolation` | LOGIN診断と受入DTOを分離。null-safe readonly参照＋観測state/理由。gen2 tickはpending確認→actual isolation→READY後最初のeligible END。countはdeserializeで捕捉した実capから読む。既存source lookup/authority関数は追加しない |
| `ProbeLifecycle.java`：`begin/bindServer/serverIdentity/pollClient/promote` | actual binding完了と測定READYを別フラグへ。gen2の観測はBINDINGでも進める。client bindを1回だけ固定し、READY待ちで旧/new比較を繰り返して自己比較にしない。各入口の順序に依存せずcurrent参照を照合。BINDINGのtimeout停止もcurrent client refs再検証後の通常pause/退出へ。TITLE/world-null/stale UI抑止は維持 |
| `Observer.java`：`client/passiveReady/deserialize/nativeUse` | 世代/時刻/side/完全型/actual refs付きimmutable DTO。未観測はready=false/PENDING、真の不一致だけFAIL。gen2 native source観測はdeserialize済みloaded Entity/capのactual一致で結び、主probe source bind前の消尽も取り逃さない。count/HP/native state書込なし |
| `InputAudit.java`：`bindGeneration/serverEnd` | gen1入力経路を維持。gen2では旧server DTOをhistoryと分離し、診断DTOと受入DTOを混ぜない。gen2 cache待ちにvalid/native canMove RETURNを必須追加しない。gen2 OS入力は0 |
| 必要な受動Mixin：`NativeCacheServerObserver.java` / `NativeCacheClientObserver.java` とhelper Mixin設定 | server `getTimeStopSavedData` の自然HEAD/RETURNでnull→実dataのwriter/caller/nanoを記録（呼ばない・戻り値を変えない）。client `ClientLevelExpandedContext.<init>` RETURN、`tickHead` RETURN、`isCurrentTS`自然RETURNを記録。contextとattach先は現在のtyped fieldで実参照照合。packet handle0は実際に追加のprovenanceが必要な場合だけreadonly RETURN観測。constructor完了や合成falseを独立FALSEにしない |

記録型/immutable DTOは既存Observer内へ集約し、追加frameworkは作らない。Mixinは非cancel・非redirect・引数/戻り値/外部field不変。hook内から同じ観測対象getterを再呼出ししない。`permission/valid/matches/reconcile/decision/MovementGuard.deny/native canMove/source lookup/lazy getter`の追加呼出し、packet補送、state修復は禁止。HUD/TITLE処理、authority/Lease/Grant/epoch/sequence、schema/protocol/native180/TTL/移動閾値/購入gateは変更しない。今回上記ファイルは**作成も変更もしていない**。

### E. 次に必要な判断・実行境界

**履歴注記：以下は21:19時点の未決事項。条件付き利用者承認と静的再確認により、§14.54で方式をLOCK済み。現行の判断待ちとして復活させない。**

**修正可能なhelper境界は特定済みだが、厳密な現条件でclient独立dimension FALSEを自然に得られる保証はない。新runを待機だけで成功させる計画にはしない。** 今回は `initialized context + null key` をnegativeへ読み替える案、純粋native `isCurrentTS()` をobserverから追加照会する案、合成falseで代用する案をいずれも採用していない。前2案も今回の「null→false禁止／自然評価だけ」という境界を利用者と確認せず通さない。合成false代用は独立dimensionを証明しないため推奨しない。

次の1作業は **clientのnegative証拠について、native constructor完了・attachのactual一致を証明した上で、副作用のないnative `isCurrentTS()` のreadonly照会を許容するかの確認**。これはlazy生成/authority評価ではないことをbytecodeで確認済みだが、自然RETURNとは別の実施主体として記録する必要がある。許容しない場合は現境界を維持し、自然RETURN未到達はUNVERIFIEDのまま。仕様/受入の変更をCodexだけで確定しない。

追加artifact不足なし。次回は上記境界とverification-only変更・限定compile/reobf・1新runの承認が揃ってから、既存§14.51の残工程を実行する。今回の設計をsame-JVM B/5 END/seal/成功final teardown PASSへ昇格しない。旧A/B/E2 setup、positive disk178、TITLE1、同PID/JVM reload1、deserialize0→178、4種actual separation、TITLE1/2 HUD、STOP後保存/Quit/PID終了は限定PASSのまま保持する。

**#1 COMPLETE / #2 INCOMPLETE / 残8 / #3 NOT STARTED**。SAFE DESIGN PROVEN=NO、SOURCE DIMENSION TRANSITION VERIFIED=NO、ownership BLOCKED、P/T IMPLEMENTATION_PENDING・購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、vehicle未LOCKと他gateを維持。Food Healing製品/通常test/build.gradle/Config/原Jar/旧run不変。新run/helper実装/compile/reobf/build/unit/check/GameTest/ゲーム/server/Prism/OS入力は全0。食料生産の可逆クラフト増加は既知許容・バグ修正対象外。

<a id="uom-client-negative-query-locked"></a>
## 14.54 queue #2 — client独立dimension negative証拠方式LOCK

更新: 2026-09-28 21:32 JST。**READ ONLY / DESIGN FINALIZATION ONLY**。今回の条件付き利用者判断に対し、下記15条件を実bytecodeで再確認し、**CLIENT NEGATIVE EVIDENCE METHOD = LOCKED**。これはverification-onlyの証拠取得方式の決定であり、製品仕様変更・実測PASS・実行承認ではない。§14.53 Eの判断待ちは解消。同じ論点の追加READ ONLYフェーズを作らず、次は限定実行の承認待ちとする。

### A. native predicateの静的採用判定

対象は `com/mega/endinglib/util/mixin/level/ClientLevelExpandedContext.isCurrentTS:()Z`。EndingLibrary **2.1.19fix** 原物/旧instance配置のSHA-256を再測定し、共に `0E29AF51DABD4E0EA8A315C1886E61F3ABEE62045F196EB6913D7F4DBA6CDC34`。承認原Jarは `build/verification/fantasy-ending-20260920-204205/audit/artifacts/EndingLibrary-1.20.1-2.1.19fix-all.jar`。`javap -p -c -s`による命令列は以下の全7命令。

```text
0: aload_0
1: getfield currentTimeStopDimension:Lnet/minecraft/resources/ResourceKey;
4: ifnull 11
7: iconst_1
8: goto 12
11: iconst_0
12: ireturn
```

| 利用者の確認条件 | 静的結果・根拠 |
|---|---|
| 1 field読取のみ / 2 非null判定Boolean返却のみ | 成立。唯一のデータ入力はreceiverの上記field、出力はprimitive boolean |
| 3 field書込0 / 4 object生成0 | 成立。putfield/putstatic/new/newarray等なし |
| 5 lazy生成0 / 6 SavedData読込・生成0 | 成立。invoke命令0、fieldアクセスのためのgetterも呼ばない |
| 7 packet送信0 / 8 event発火0 / 9 authority評価0 | 成立。invoke0・外部state参照0 |
| 10 Grant/Lease/session操作0 / 11 count操作0 | 成立。これらへのfieldアクセス/呼出しなし |
| 12 global/dimension変更0 / 13 callback登録・消費0 | 成立。dimension keyの読取だけ、global読取さえない |
| 14 外部副作用0 | 成立。logger呼出しも0。分岐と返却だけ、monitor/例外生成/動的呼出しなし |
| 15 owner thread上の安全性 | 成立する呼出し条件を限定。自然生成・attach済みの非null実contextを、Minecraft client owner threadで同期的に照会する。`getClass()==ClientLevelExpandedContext.class`と完全型fieldを照合し、未確認subclassへのvirtual dispatchを避ける。caller側が生成/attach/authorityを行わない |

旧runの実変換 `ClientLevel.<init>` は `handler$zkg000$init → new EndingLibrary ClientLevelExpandedContext → endinglib$setECData`、setterはEndingLibrary型のfieldへ格納する。FEにも同名fieldがあるため名前だけで選ばない。実変換 `TimeStopUtils.andSameDimension` のclient分岐も上記native `isCurrentTS:()Z` を呼ぶことを再確認。ただしhelperはこのwrapperやstatic `get()`を使用せず、捕捉した実contextを直接照会する。

`ClientLevelExpandedContext` 自身のexport済み変換classは旧runに存在しないため、それを確認済みとはしない。原物の実methodを確認し、配置10Jar中のMixin関連class参照を限定照合した結果、該当参照はEL/FEのClientLevel attach実装で、このpredicateを変更する既存Mixinは見つからなかった。次回追加するobserverは非cancel・戻り値非変更・検証用記録のみとし、通常のhelper非混入/静的照合でその境界を確認する。今回未知の外部版まで許容しない。

### B. 1回照会の前提・ラベル・不変確認

前提は同run/gen2の自然ClientLevel生成、native context constructor RETURN、current ClientLevelへのattach、constructor receiver == typed field == query receiver、context.clientLevel == current ClientLevel、current LocalPlayer/listener/Connection/channelのactual一致。旧gen実体でないことも実参照比較する。canonical/healthは§14.51を維持し、Lease独立不在・client threadで読んだglobal OBSERVED_FALSEを先に要求する。未生成/未attachはUNINITIALIZED、attach済みでもBoolean証拠なしはINITIALIZED_UNOBSERVEDであり、この間は照会しない。

**呼出し場所は `Observer.queryClientDimensionOnce()`（予定名）の1箇所だけ**。run/gen2に対し最大1回、失敗/例外/true/後続欠測でも再照会しない。前提成立後にhelper内の一度限りのclaimを取り、before DTO→直接 `context.isCurrentTS()` →after DTOを同client threadで、途中にtask yield/packet dispatch/製品callを挟まず採取する。claim/証拠管理はhelper内だけで、native stateを書き換えない。

証拠種別は **READONLY_NATIVE_QUERY_FALSE / READONLY_NATIVE_QUERY_TRUE**。自然に生じた呼出しは **NATURAL_RETURN_FALSE / NATURAL_RETURN_TRUE** として引き続き別記録する。query専用のthread-local origin token（run/gen/query ID/actual receiver）をtry/finallyで囲み、同じRETURN hookがdirect queryをNATURALへ二重計上しないようにする。該当token一致時だけdirectと分類し、自然hook自体は削除しない。hookからqueryを呼ばず再帰も作らない。

各query receiptにrun/gen・開始終了timestamp/nano・thread名とclient owner-thread確認・method owner/name/descriptor・query origin=`verification-only helper`・actual refs比較結果・結果・side-effect auditを保存。identityHashCodeは表示用だけ。次をbefore/afterそれぞれ採取し、**実参照/primitive値の不変**を確認する。

- actual context / attach field / currentTimeStopDimension field（raw nullもそのまま記録）。
- globalの実primitive値、Lease実参照/present、保持packet実参照/値、session/revision/sequence/expiry、Lease cached refs。
- current player/level/listener/Connection/channel、run/gen。取得/SP/toggle/healthの既存受入条件も維持。

false返却かつ全不変ならOBSERVED_FALSE＋上記directラベルを発行。trueならOBSERVED_TRUEとして同operand一式を保全しREADY不可・安全側STOP。変化/例外/参照不一致もSTOPして再照会しない。特にglobalはintegrated JVM共有fieldであり、前後差が出てもqueryの書込と推定せず、並行native更新の可能性を区別して記録する。**queryによる変更が認められれば即FAIL/STOP**。今回の静的採用判定は、次回実測の前後不変PASSを先取りしない。

### C. PASSIVE READYと5 ENDへの接続

最終READY条件は、gen2/current actual client refs・正常canonical/health・Lease独立不在・client global観測false・native constructor完了/attach actual一致・**1回のREADONLY_NATIVE_QUERY_FALSEと前後不変**・旧gen証拠流用0・fresh owner-thread DTO。自然RETURNやdimension packetの受信はREADY必須ではない。global=false短絡を正常境界として扱い、global&&dimensionの合成falseで独立dimensionを代用しない。nullからhelperが結果を補完することもない。

**1回照会と5 ENDのfreshnessを分離**：query receiptの取得nanoは固定し、後続DTOの時刻で上書きしない。各client sampleでquery時と同じactual context/attach/field inputであることをreadonlyに照合し、その時点のglobal・Lease・refs・canonical/healthを新しいimmutable DTOへ採取する。全7命令が同じfieldだけに依存するため、同じ入力が維持される区間で同じquery証拠を参照できる。これは毎frame照会や自然RETURNではない。field/refs変化・照合不能・矛盾する自然RETURNを観測したら証拠を失効させSTOP、再query/false補完なし。

READY後、自然生成済みnew State/Context/engineをreadonly field/mapから旧actualと比較し、実`!=`を要求。未生成ならPENDING、生成getter禁止。READY以後の自然permission RETURN=falseとactual isolation等が揃った最初のeligible server ENDから5連続採取。各ENDは§14.51の150ms freshness・新しい自然RETURN/current DTO・positive count・旧authority非利用を維持する。query receiptの古さとfresh DTOの古さを混同しない。gap/欠測/positive消尽はUNVERIFIED/STOP、5行選別/再開始なし。§14.53 Cの期限候補は維持し、存在しない自然RETURNを待って時間を消費しない。native180/TTL/閾値変更0。

### D. 最終差分・次run順序

§14.53 Dの差分を次の範囲で最終確定。**今回は一切実装していない**。

| 次回対象 | 内容 |
|---|---|
| ProductClientProbe.java | LOGIN診断/受入snapshot分離、未生成PENDING、READY→actual隔離→eligible END1〜5 |
| ProbeLifecycle.java | bindingとREADY分離、constructor/attach観測との順序非依存照合、gen2の一度限りbind、既存安全退出 |
| Observer.java | 上記小関数1箇所、query claim/origin token/before-after/固定receipt、自然RETURN別記録、各sampleの入力同一性・fresh DTO |
| InputAudit.java | gen2 current DTOを旧historyと分離、1回query receiptへの参照。gen1入力設計は維持 |
| NativeCacheServerObserver.java / NativeCacheClientObserver.java（verification-only予定）とMixin config | §14.53 Dの受動初期化・constructor/自然RETURN観測。direct起因RETURNを自然へ誤計上しない。非cancel/戻り値不変。server lazy getterは観測だけで呼ばない |

今回許容した追加callはclient `isCurrentTS:()Z` 1回だけ。server `getTimeStopSavedData`、permission/valid/matches/reconcile/decision/MovementGuard.deny/native canMove/source lookup/TimeStop use/packet resend/Grant・Lease生成へ一般化しない。§14.51で既に定めたsetup/成功後cleanupの既存工程と、観測目的の追加callは禁止範囲を区別する。

**次回、別承認後の一括順序**：helper最小修正→限定offline compile/reobf→1新run→A/B/E2 setup→positive disk→TITLE1→same PID/JVM reload→positive deserialize/Entity・cap actual separation→gen2 natural context/attach→1回readonly query→PASSIVE READY→State/Context/engine actual comparison→5連続END→旧authority総合非復活→seal→必要時cleanup最大1→successful final teardown→TITLE2 HUD draw0→Quit/PID終了。setupは新保存状態の前提であり、過去PASSの取り直しへ加算しない。実際のnative入口順の違いはpendingで束ね、固定順を作るためにnative stateを操作しない。

成功/STOP後の通常保存・readonly照合・終了と3文書更新は既存§14.51に従う。旧run/world/receiptは再利用しない。追加artifact不足・追加仕様判断なし。**次の1作業＝verification-only helper最小修正＋限定offline compile/reobf＋1新run残工程一括実行の利用者承認待ち**。同論点の追加READ ONLYを新設しない。

変更前backup: `backups/20260928-212822-client-negative-query-lock-docs/`。§14.52の既存限定PASS・失敗・停止後終了と過去結果は保持。今回のquery実行0、helper変更/compile/reobf/build/test/GameTest/ゲーム/Prism/server/world/OS入力0。製品/source/test/build.gradle/Config/Jar/原MOD/旧証拠不変。#1 COMPLETE / #2 INCOMPLETE / 残8 / #3 NOT STARTED、SAFE DESIGN PROVEN=NO、SOURCE DIMENSION TRANSITION VERIFIED=NO、ownership BLOCKED、P/T pending/購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、他工程/個別gate維持。可逆クラフト増加は既知許容仕様・バグ修正対象外。

<a id="uom-queue2-completed-readonly-query"></a>
## 14.55 queue #2 — readonly query・5 END・旧authority非復活・成功final teardown完了

更新: **2026-09-28 22:10 JST**。利用者の明示実行承認により§14.51/§14.54の条件を変更せず、**queue #2 COMPLETE／#1 COMPLETE／残7／#3 NOT STARTED**。旧§14.48/14.50/14.52のSTOP/FAILと限定PASSを保持し、新runの成功とは区別する。

### 対象・helper・不変確認

- 新run/root: `20260928-214421` / `build/verification/uom-production-os-input-20260928-214421/`。instance `build/verification/direct-jar-20260913-114729/launcher/instances/FHR_UF_20260928-214421`、world `.minecraft/saves/UP-20260928-214421`。旧world/receipt再利用なし。
- 製品 `foodhealing-3.0.0.jar`: **298,958 bytes /189 entries / protocol7 / SHA256 37E300EAA0663FDCDF445317C9F14F3BF13C839B21E4DD5679229B32386592E3**。開始前/build後/終了後不変、配置10Jar一致、保護1,232ファイル差分0、旧run/旧instance追加0。
- 別helper **v20260928.214421 /81,355 bytes /31 entries / SHA256 5BA6971021B5489FEB38B7C903EB5EF46C51326A36E4F8AF23E8E52430525BAB**。新rootのProductClientProbe/ProbeLifecycle/Observer/InputAudit、NativeCacheClientObserver/NativeCacheServerObserverとMixin config・run配線のみ変更。LOGIN診断とREADYを分離、自然constructor/typed attachをactual比較、専用query一度限り、direct RETURNと自然RETURNをorigin tokenで分離、fresh DTO/5 END/退出観測を接続。追加framework・製品class/外部MOD class/ExampleMod混入なし。
- task graph確認後、既存local Gradle8.1.1・既存cache・offlineでhelper compile/resources/Jar/reobf＋cached mappingのみ実行、初回compile/reobf成功。製品jar/reobfJar/build/check/test/unit/GameTestは0。測定前sandboxのnative-platform.dllアクセスとOS窓列挙に失敗した記録を保存し、承認済みローカル権限付き実行で解消。helper code repair0、測定後repair/rebuild/retry0。

### 実測・判定

| 受入項目 | 今回の実測と判定 |
|---|---|
| 安全準備 / 実施主体 | 生成前GUIでSurvival/Normal、doMobSpawning=false・naturalRegeneration=false、閉じた区画。HP20・effects空・prepare1/SP0/spent103。AUTOMATED OS INPUT A/B各1（W約1008/1007ms、key-up確認）、HUMAN INPUT=0。安全区画の前方距離1.199999988079071 blocks。setter/teleportでmovementを作らない |
| A/B/E2 setup | A実client/server movement・Lease成立、B通常movement復帰成立。新positive保存の前提として各1回、過去PASSの再証明へ加算しない。native E1/E2各1、G1 epoch/sequence1/1、E2のG2 2/2。再読込後prepare/OS input/goal再実行0 |
| positive保存 / TITLE1 | 実disk count178（期待値固定でなく自然値）。NORMAL_EXIT_ACCEPTED-1、final live HP20、all dimensions saved、Context closed/maps0、server thread TERMINATED、session.lock読取exclusive成功。TITLE1 world/player null、20frame HUD draw0、world-null PauseScreen0 |
| same-JVM reload / deserialize | Minecraft PID32192、JVM start1790599794624を維持し、同world再読込1。native deserialize0→178を1回観測。Entity/capability/ServerLevel/server actual ref分離。自然生成されたState/Context/engine/sessionとclient listener/Connection/channel等もactual比較。生成目的のlazy getterなし |
| client negative query | gen2自然constructor/typed attach actual一致、current refs・正常canonical/HP20・Lease absent・global false後、Render ownerから専用`isCurrentTS()`を1回。**READONLY_NATIVE_QUERY_FALSE**、queryId1/direct RETURN1、自然RETURNへ二重計上0。context/key/global/Lease/packet/session/revision/sequence/expiry/current refs等22項目前後不変。null/UNOBSERVEDをfalseへ変換しない |
| PASSIVE READY | 上記query receipt＋gen2 current immutable DTOで成立。queryの時刻とDTO freshnessを分離。自然valid/nativeCanMoveの未観測を否定証拠へ代用せず、自然RETURN待ちもしない |
| 旧authority総合非復活 | 下表5連続ENDとsealでold State/Context/engine current化なし、old session再bindなし、Grant revoked、Lease absent、old epoch/sequence ALLOW0、origin/binding0、old callback continuation0。gen2全体の自然permission false1170、unexpected true0、ALLOW送受信0（1170を5 ENDの代用にしない） |
| seal / cleanup | failure receiptなし、測定後repair/retry0、state repair0、追加authority call0でseal1。cleanup確認時native countは自然に0となっており **SKIPPED_ALREADY_ZERO**、明示cleanup0。cleanupで非復活成功を作らない |
| successful final teardown | NORMAL_EXIT_ACCEPTED-2、final live HP20/effects空/正常canonical、通常Save & Quit、全dimension保存、Context closed/states/sessions/grants0、server thread TERMINATED。readonly final disk native count0・canonical保持・transient authority永続化なし・session.lock解放。TITLE2 20frame HUD draw0/world-null PauseScreen0/crash0。Quit GameのStopping! 22:00:05、Minecraft32192/今回Prism10812/OS supervisor38412/children37860・31056終了。OS key release確認。numeric process exit codeは未採取 |

| 連続END | server END ordinal | positive source count | fresh client DTO age(ms) | natural permission / Grant / Lease |
|---|---:|---:|---:|---|
| END1 | 2 | 169 | 3.3005 | false /0/absent |
| END2 | 3 | 168 | 8.3025 | false /0/absent |
| END3 | 4 | 167 | 6.9166 | false /0/absent |
| END4 | 5 | 166 | 5.4834 | false /0/absent |
| END5 | 6 | 165 | 4.7365 | false /0/absent |

READY成立後の最初のeligible ENDから採取し、gap/選び直し/欠測なし。各ENDのcurrent actual refs・canonical/HP正常・origin0/binding0を確認。reload後の`deserialize-provenance-loss:POSITIVE` / UNKNOWNはpersisted native positiveをruntime authorityへ復活させない安全側判定。old engineの`unexpected-use` terminal faultは旧close時baseline（faultEvents1/created-consumed-committed各1）のまま不変であり、gen2の新たなcallback実行と混同しない。TITLE HUD receipt内のserver値は過去snapshotであり、thread終了判定は別の`TITLE-1-SETTLED.json`/`TITLE-2-SETTLED.json`のactual `threadAlive=false` / TERMINATEDを使う。

### 証拠・残範囲

[照合結果](../build/verification/uom-production-os-input-20260928-214421/audit/reviewed-results.json)、[原journal](../build/verification/uom-production-os-input-20260928-214421/CLIENT/audit/journal.jsonl)、[helper build](../build/verification/uom-production-os-input-20260928-214421/audit/helper-build-1.log)、[task graph](../build/verification/uom-production-os-input-20260928-214421/audit/client-task-graph.txt)、[query](../build/verification/uom-production-os-input-20260928-214421/CLIENT/audit/READONLY_NATIVE_QUERY_FALSE.json)、[READY](../build/verification/uom-production-os-input-20260928-214421/CLIENT/audit/PASSIVE-READY.json)、[seal](../build/verification/uom-production-os-input-20260928-214421/CLIENT/audit/SEALED.json)、[positive disk](../build/verification/uom-production-os-input-20260928-214421/audit/disk-A-positive.json)、[final disk](../build/verification/uom-production-os-input-20260928-214421/audit/disk-FINAL-readonly.json)、[終了process](../build/verification/uom-production-os-input-20260928-214421/audit/process-final.json)。変更前3文書は新root `before/docs/` に保全。UI操作はCOMPUTER USE、WだけAUTOMATED OS INPUT、HUMAN INPUT=0。

今回の明示20受入条件を全成立としqueue #2をCOMPLETEへ更新。**次の1作業はqueue #3 FE6 secondaryの別承認。今回は開始0で停止**。追加artifact不足なし。同論点の追加READ ONLYや既存suite再実行は不要。SAFE DESIGN PROVEN=NO / SOURCE DIMENSION TRANSITION VERIFIED=NO / ownership BLOCKEDの広い判定、UOM/P vehicle未LOCK、FE6 NOT IMPLEMENTED、P/T IMPLEMENTATION_PENDING・購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、Flight他provider喪失の#7正式release blocker・その他個別開始gateは維持。今回の限定受入を全機構/全MOD互換の証明へ拡張しない。食料生産の可逆クラフト増加は既知許容仕様・バグ修正対象外。


<a id="fe6-secondary-production-result"></a>
## 14.56 queue #3 — FE6 secondary製品実装・実FE焦点自動統合

更新: **2026-09-28 22:57 JST**。明示承認されたproduction＋automated phaseを完了。**#1/#2 COMPLETE維持、#3 IMPLEMENTED / AUTOMATED INTEGRATION TESTED / CLIENT FOLLOW-UP PENDING、残7、#4 NOT STARTED**。FE6はFE-P1–P5の計6 addEffect命令で、旧FE-P6 TimeStopは今回対象外。§14.55の指定受入・広いTimeStop未証明gateを変更しない。

### 実物・実装・変更境界

- [承認6原物の再照合](../build/verification/fe6-secondary-20260928-222200/audit/inputs.json)は§14.7のpath/size/hash/versionと一致。原Jarは変更0。Forge47.4.0のpatched vanilla WitherSkullを含む[exact site](../build/verification/fe6-secondary-20260928-222200/audit/exact-sites.json)は既存bytecodeと一致。UOM Dream79/97、Star AOE313、final340、counter334、skull188。初回に読んだunpatched vanilla SRG（offset189）は試験runtimeではなく、正しい47.4.0 server原物へ照合し直した。
- 新規製品: `compat/fantasyending/{FantasyEndingVersions,FantasyEndingMixinPlugin,FantasyEndingCompatibility}.java`、`mixin/fantasyending/{UomWitherMixin,StarEntityMixin,UomOwnedWitherSkullMixin}.java`、`foodhealing.fantasyending.mixins.json`。build.gradleへ専用config/sourceSet/Jar/reobfを追加。既存production/test sourceは変更0、共通damage/P controller/購入/TimeStop/protocol7/schemaは不変。新しい永続state・移行処理なし。
- version gateはloader metadataの**fantasy_ending=2.7.20完全一致**だけ。source実class＋正式登録`ultimate_order_manager`のEntityType同一性、正式projectile owner、same level、ServerPlayer capabilityの既存P述語を要求。Truth取得/ON不要。原callは対象外で1回、対象の新規付与だけfalse。method/damage/event全cancel・既存effect消去なし。
- [最終Jar/変換監査](../build/verification/fe6-secondary-20260928-222200/audit/final-jar-audit.json)：実ロードredirect数 **Dream2/final1/counter1/Star1/skull1**。require/expect/allow固定、skull refmap/reobf成立。旧Jar全既存class・既存Mixin JSON・全既存refmap項目一致。差分は新FE6class/configとManifest/refmap追加のみ。L2/Trial既存133/49/86はこの境界から再実行不要と判断し、旧対象JarのPASSを保持する。新Jarで再試験したとは記録しない。

### 焦点結果とnumeric

| 経路 | P ON実測 / native無資格対照 | 保持したnumeric（Normal・試験初期HP1000） |
|---|---|---|
| Dream | site2→追加0。OFF等ではslowness40/amp4、poison100/amp3 | seed12345のDS入力11.809015、Forge hurt同値、HP988.191。10+random×5の原式不変 |
| Star AOE | UOM owner＋fixture RNG0.25でsite1へ到達・追加0。無資格はban_healing60/amp7 | DS19、FE0 9.5、HP962。native爆発/lifecycleを実行 |
| final | site1→追加0。無資格はban_healing20/amp6 | FE0 0.1×3、negative delta−5×2、HP989.40015 |
| powered counter | site1→追加0。無資格はinstant_damage1/amp2 | magic FE入口1.8000001=10×0.6×0.3、HP998.2。FE→FE0の入れ子観測を攻撃2回と数えない |
| UOM WitherSkull | Normal/Hard site1→追加0。無資格はwither200/800・amp1 | raw hurt8、爆発raw15。Normalイベント8/7、HP985。Hardイベント12/10.5、HP977.5。rawと難易度/無敵窓適用後eventを分離 |

- 各5経路で親OFF/極意OFF/親未取得/極意未取得/pending/invalid/別UUID無資格playerの7対照。ON/OFFのsource・amount・layer・回数・負delta・HP結果一致を[保存証拠の再照合](../build/verification/fe6-secondary-20260928-222200/audit/final-jar-audit.json)で確認。付与直後に観測し、通常浄化tick後消去を保護成功へ数えない。native入口呼出しの自動試験であり自然boss戦/実clientではない。
- Star RNGは**別fixtureのnative Math.random callだけ**0.25/0.75へ固定。0.75ではsite0なので抑止件数にしない。cow owner AOEは元banを維持。UOM直撃4effectはnative skip、cow ownerのNormal/Hardは4effect、Easyはnative0。full onHitで直撃UUIDのAOE除外、owner本人の除外も確認。
- skullはUOM/通常Wither/cow/null/別FE entity ownerを分離。Easy/Peacefulはnative付与0、Peaceful hurt不成立をP抑止と混同しない。実UOM `_performRangedAttack` が生成した1 skullのowner同一性→native衝突→hurt8→抑止/removedも追加確認。異levelは実Entityを使った**共通述語対照**であり飛翔/転送試験ではない。非player Cow Dreamは元2付与、非powered UOM反撃はnative branch0。
- 全5保護入口で既存ban_healing900/amp9・poison900/amp5・beneficial luck・foreign NBT・movement modifier・本人canonicalを保全。数値damageやnative source処理を凍結する方式ではない。owner heal/enchant/death/loot全組合せ・演出/実client同期・回復上限等の全網羅PASSには拡張しない。
- main [focused-06結果](../build/verification/fe6-secondary-20260928-222200/focused-06/fe6-result.json) **63/63**、追加[supplement-01結果](../build/verification/fe6-secondary-20260928-222200/supplement-01/fe6-result.json) **8/8**。計71実行、metadata gate1件の重複を除き**70 unique cases（native/述語69＋版判定1）**。製品Jarは両runとも下記同一hash。fixtureは各run inputsで区別、最終fixture24,692 bytes/18 entries/SHA256 `980E3B7F05A625B40E058C1D3A839E007EB4AFA0915896E71F27EA57AC9CF885`。
- 実ロードMC1.20.1/Forge47.4.0、FE2.7.20/EndingLibrary2.1.19fix/Irons Spells1.20.1-3.16.3/Curios5.14.1+1.20.1/GeckoLib4.8.2/IronsLib1.20.1-2.1.0、同梱mixinextras0.4.1/playeranimator1.0.2-rc1+1.20。同梱物の外側重複配置なし。playerは自動ServerPlayer＋EmbeddedChannel、real client/TCP認証の証拠ではない。

### build・回帰・失敗履歴・終了

- local Gradle8.1.1・Java17.0.7・既存cache・offline。`compileJava`、`build foodHealingUnitTest check fantasyEndingTestJar` [成功](../build/verification/fe6-secondary-20260928-222200/audit/build-02.log)。unit内P150 assertions/T53/OPEN142/SW47/TimeStop9/data-boundary5000ほか既存群を維持。fixtureの最終追加compile/reobfは[build09](../build/verification/fe6-secondary-20260928-222200/audit/fixture-build-09.log)成功。製品sourceは初回build成功後に変更なし。
- [vanilla](../build/verification/fe6-secondary-20260928-222200/audit/vanilla-final.log) **60/60**、[承認TaCZ1.1.7-hotfix2](../build/verification/fe6-secondary-20260928-222200/audit/tacz-final.log) **60/60**。既存Forge47.2.0 userdev GameTest経路の最終source回帰で、配布Jar直接試験とは区別する。FE不在classloading/config起動成功。未知版はpure version gateの拒否確認で、未提供の未知版Jar実ロードPASSとはしない。
- 途中失敗は全保全：focused-01はfixture本体がMixin package内にあり起動前IllegalClassLoadError（0件・world通常保存なし・PID終了）。02はplayer初期spawn invulnerability（1完了）、03はUOM native setHealthのclampでpowered準備未到達（25完了）、04はnative setOwner(null)が元ownerを消さない準備不備（43完了）、05はHard補正後eventをraw8と比較したobserver不備（44完了）。02–05は各通常保存/停止・exit0。修正はfixtureのpackage分離/自然player tick110/native有界powered準備/最初からnull-owner生成/raw hurt observerに限定し、製品・期待値は変更しない。FAILを成功へ改記せず新worldへ分離した。
- compile初回のsandbox native DLL制限、fixture LazyOptional supplier不足も原logを保持。前者は承認済み権限実行、後者はfixture修正で解消。終了照会のsandbox拒否は不完全証拠として不採用、成功した[process照会](../build/verification/fe6-secondary-20260928-222200/audit/final-processes.json)により今回Java残存0。ゲームclient/Prism/HUMAN入力/外部download/Security scanは0。
- 成功2runとcore2runは通常save/stop・全dimension保存・process exit0。失敗world/receiptをresetせず保存。原/過去world再利用なし。新fixtureは測定前の専用取得・HP1000条件を用いる自動試験であり、通常GUI購入成功/人間向けHP安全試験ではない。

### 最終成果物と次の1作業

[製品Jar](../build/libs/foodhealing-3.0.0.jar)：**306,882 bytes /198 entries / SHA-256 `1BBD49E6377DAF0F5C7C0EDED15A61F96261C80A1EA8C03471E634EFD6404A19`**、MOD ID foodhealing/version3.0.0、protocol7。mods.toml/Manifest/Mixin/refmap/reobf・外部class/Jar/fixture/ExampleMod非混入確認。[変更前backup](../build/verification/fe6-secondary-20260928-222200/before)（3文書・全既存src・build.gradle・開始Jar）。`.git`なし、hash/ZIP/実class比較でありgit diff確認ではない。

**次の1作業＝§14.5/TEST_PLAN§27のFE6限定実client（Dream単発＋通常GUI/canonical/HP/HUD収束）の安全条件・実行承認。** 自動fixtureは実client配送/表示を証明しないため#3 COMPLETEへ昇格しない。HP1000の結果をそのままHP20安全証拠にせず、非致死・最大入力を確認してから限定実行する。BAN_HEALINGの実clientはDreamへ転記せず、安全な追加観測要否を同承認範囲で明示する。新artifact不足なし、追加client helper作成/起動は今回0。#4通常購入/SP取引は未開始。

P/T IMPLEMENTATION_PENDING・購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、SAFE DESIGN PROVEN=NO / SOURCE DIMENSION TRANSITION VERIFIED=NO / ownership BLOCKED、vehicle未LOCK・全個別開始条件を維持。食料生産の可逆クラフト増加は既知許容仕様・バグ修正対象外。


<a id="fe6-client-completed"></a>
## 14.57 queue #3 — Dream単発・GUI/同期/HUD限定実client完了

更新: **2026-09-28 23:38 JST**。新run **20260928-231600**、**REAL CLIENT LIMITED PASS / queue #3 COMPLETE**。#1/#2維持、残6、#4 NOT STARTED。§14.56の既存自動結果（63/63＋8/8、70 unique、core各60、build/unit/check）を再実行・別結果へ転記していない。

### 成果物・安全準備と実施主体

- 製品: `build/libs/foodhealing-3.0.0.jar` **306,882 bytes /198 entries /SHA-256 `1BBD49E6377DAF0F5C7C0EDED15A61F96261C80A1EA8C03471E634EFD6404A19` /protocol7**。開始・配置・終了で一致。全src（既存test含む）/build.gradle差分0、製品Config/schema/protocol/購入gate変更0。
- 新helper `fe6_client_probe` v`20260928-231600`: **21,626 bytes /14 entries /SHA-256 `BE1AFD9E8B4C48D128CA433577D1B3AC738FB8564093AB9FA0FB9DC0E49FE6CB`**。[Jar/非混入](../build/verification/fe6-client-20260928-231600/audit/helper-audit.json)。Gradle8.1.1/local cache/offline、専用task graphのみ。build01/02とも成功、02は測定前のvanilla/Curios空装備・受動physical guard追加。測定後変更/rebuild/retry0。製品build/既存suite0。
- MC1.20.1 /Forge47.4.0、承認FE2.7.20・EndingLib2.1.19fix・Iron Spells3.16.3・Curios5.14.1+1.20.1・Gecko4.8.2・Iron Lib2.1.0。原物hash照合済み、外側mods9本、nested重複なし。[配置全hash](../build/verification/fe6-client-20260928-231600/audit/client-inputs.json)。旧TimeStop/login/Trial/L2/自動fixtureなし。
- shader compatは既存v`0.1.0-verification.20260921.125321`、SHA `BD2322818FDC65F4F20FD8332200CD6AED02F00487E3E1D8868D2865AD4B58D0`を不変使用。既存FE起動Instrumentation bootstrap/bridgeも不変使用（mods外JVM agent、旧queue2測定helperではない）。shader再試験0。既存patchのResourcePackInfo警告は保持し、patch変更なしでタイトル/worldへ正常到達。全WARN解消を意味しない。
- 新instance `FHR_FE6_20260928-231600`、world `FE6-20260928-231600`、[実パスと配線](../build/verification/fe6-client-20260928-231600/audit/instance.json)。既存world再利用0。COMPUTER USEで生成前GUIをSurvival/Normal/cheats ON、湧き/自然回復OFF、flat/構造物OFF。今回worldだけの床・壁・屋根・照明、準備前の通常tp配置後、周辺敵対0/危険0をguard確認。測定後座標/HP/effect修復0。
- [安全preflight](../build/verification/fe6-client-20260928-231600/audit/safety-preflight.md)：承認実bytecodeのnative入力 **10≤D<15**、hurt1回、Player owner倍率/projectile層対象外、本人防御/装備なし。HP20から **5<HP≤10** の境界を確認してから発動。旧HP1000結果だけを安全根拠にしない。actual UOMの専用UUID tick containment＋NoAIで他skill/AIを停止し、Dreamだけ明示native呼出し。保護処理を補助で代行しない。

### 同一runの実測

| 単位 | 実測・証拠 | 判定 |
|---|---|---|
| fresh / prepare | HP20/MAX20/delta0、装備/Curios/effects/skills空・正常canonical。CREATE_NEW receipt先行、credit103/支出103、P/M各Lv1、両ON、SP0/spent103、Truth/Rootなし。prepare1 | PASS。通常GUI購入証拠ではない |
| 通常GUI | inventory→Sで製品GUI。PだけOFF→ON各1回。Mの保存ON不変、server/render各owner threadの独立canonical一致。GUIがpauseする間はtick収束を捏造せず、閉じて自然同期後にreceipt | PASS |
| actual Dream | registered `fantasy_ending:ultimate_order_manager`、source UUID `9a91c982-4714-4a61-a960-42c992f456ca`、同ServerLevel、実`dreamShadowBeam(real ServerPlayer)`1回 | PASS |
| numeric | raw **10.670565**、`fantasy_ending:ds_power`、HURT/DAMAGE各1・双方10.670565/canceled=false。HP **20→9.329435**、MAX20、delta0 | PASS。数値攻撃を残す |
| secondary | native RETURN直後、通常浄化tick前にDream2site protected=true/true、実addEffect forward0/Added0、毒/鈍足新規0 | PASS。後消去を抑止と誤認していない |
| client/HUD | 新規LocalPlayer連続3snapshot（serial5554/5555/5556）でHP/MAX/effects/canonical/SP一致。通常FoodHealing HUD **9.3/20.0**、F2 23:33:37 | PASS。一瞬のicon目視判定ではない |
| seal/cleanup | prepare1/Dream1/extra0/repair0/FAILED0、SEALED。同所有actorだけ`UomWither.trulyKill()`1回、removed=true、本人HP/effect/canonical前後不変。native自身のloot/XPは発生、追加攻撃でない | PASS |
| 保存/終了 | 23:35通常Save & Quit、全5dimension保存・SERVER_STOPPED。保存Health **9.329435348510742**、DeathTime0、P/M1両ON・SP0/103・pendingfalse、所有actor不在。title→23:35:47 Quit、Minecraft32180/今回Prism37604終了 | PASS。再読込0（新永続stateなしで必須でない） |

[照合結果](../build/verification/fe6-client-20260928-231600/audit/reviewed-results.json)／[原journalとreceipt](../build/verification/fe6-client-20260928-231600/CLIENT/audit/journal.jsonl)／[保存照合](../build/verification/fe6-client-20260928-231600/audit/disk-readonly.json)／[終了log](../build/verification/fe6-client-20260928-231600/audit/client-final.log)／[終了PID照合](../build/verification/fe6-client-20260928-231600/audit/process-after.json)／[F2画像](../build/verification/fe6-client-20260928-231600/screenshots/2026-09-28_23.33.37.png)。9/27生成の既存背景Prism26748は今回起動processではなく未操作。helperはMinecraft内の別MODで独立試験processなし。HUMAN gameplay入力0、GUI/F2/コマンド/save/quitはCOMPUTER USE、native攻撃/取得/観測はAUTOMATED。

### 受入範囲・次の1作業

**追加BAN_HEALING実clientを#3必須にしない。** §14.5はDream/GUI/同期/HUDを代表候補とし、BANの別機構をDreamへ転記しない条件である。TEST_PLAN§27と§14.56にも独立BAN実clientを#3 release必須とする明記はない。既存native自動の全6site/既存effect保全/numericに今回の代表clientを追加し、利用者指定受入を満たしたため#3 COMPLETE。Star/final BAN・counter HARM・skull WITHERの**実clientは未実施のまま**。自然boss戦・全描画/全MOD保証へ広げない。新たな陰性2発目、再読込、念のための試験を足さない。

**次の1作業はqueue #4正式readiness接続・通常購入/SP取引の別承認。今回は#4 NOT STARTEDで停止。** P/T IMPLEMENTATION_PENDING/購入停止/SP保護、RC=NO、REAL2CLIENT=BLOCKED、SAFE DESIGN PROVEN=NO/SOURCE DIMENSION TRANSITION VERIFIED=NO/ownership BLOCKED、vehicle未LOCK、他個別gateを維持。食料生産の可逆クラフト増加は既知許容・バグ修正対象外。過去§14.55/14.56と旧FAIL/STOPは不変。変更前3文書は `build/verification/fe6-client-20260928-231600/before/` に保存済み。


<a id="mastery-production-purchase-ready"></a>
## 14.58 queue #4 — P/T production purchase readiness・通常購入/SP取引完了

更新: **2026-09-29 07:49 JST**。run **20260929-073000**。**#1–#4 COMPLETE／残5／#5 NOT STARTED**。[照合結果](../build/verification/mastery-purchase-20260929-073000/audit/reviewed-results.json)。最低効果範囲は§12.5/§14.55–14.57で充足済みとして受け入れ、再監査・実攻撃・実clientを追加していない。

### 最小変更と契約

- production変更は[FoodHealingSkills.java](../src/main/java/com/leva/foodhealing/FoodHealingSkills.java)だけ。固定pendingからP/Tのみ除外。Flight/Break Realm・TaCZ専用gate不変。P100/T500、前提は通常浄化Lv1/P極意Lv1、親ON/食義Lv/外部MOD導入を要求しない。fresh全購入合計は3＋100＋500＝603。
- P/T購入時の異常取得map（未知ID/上限超過Lv）をread-only STALE_REQUEST拒否。pending/future schemaの既存拒否不変。malformed親Levelは既存deserializeで未取得となりPREREQUISITE_MISSING、購入時に修復しない。raw不正NBT保全・schema変更の新機能ではない。
- 使用済SPのoverflowを支出前だけでなく共通previewでもINSUFFICIENT_SPに揃えた。既存atomic debitのLong境界を保持。拒否時canonical全体不変、既取得者の没収/返金/level/toggle変更なし。効果controller/Adapter/Mixin・network/schema/Config/build.gradle変更0。
- test変更: 既存FoodHealingPrerequisiteGameTests（配布除外）とOpenPrerequisiteRegression/PurificationMasteryRegressionの旧固定pending期待を、今回承認されたready契約へ更新。case削除0。新MasteryPurchaseRegression＋新core7ケース。購入本体は登録packet→production transaction。前提/拒否用データと期待snapshotだけfixtureで準備し、playerへの直接取得注入を購入成功扱いしない。

### 実施結果と境界

| 対象 | 結果 |
|---|---|
| P100 / T500 | exact debit・Lv1・default ON・親/他skill不変。登録packetで成功時canonical同期1回 |
| 親OFF / 前提不足 / SP | 親OFFでも購入、親OFF保持。未取得拒否、99/499拒否・100/500成功、食義Lv0の603 chain成功 |
| stale/duplicate/invalid | 0再送・既取得・負/極値expected・未知ID拒否、二重支出/Lv2/他player変更なし |
| canonical / Long / 旧所有 | pending/future・異常Lv/ID拒否、正確Long上限成功/overflow拒否、保存canonical roundtrip・既取得全OFF保持 |
| optional matrix | 導入0/未対応版/対応版/混在相当の**pure契約fixture4構成**、P/T×親ON/OFFで購入成功。実L2/FE version関数は未対応false、対応true。Trial loader plugin/全Adapter class byte不変。4外部構成の実ロード/gameplay PASSではない |
| preview/server / packet | 同一purchaseStatusの読取不変・結果一致、registered SimpleChannel encode/decode/NetworkHooks→server→outbound canonical payload一致。他playerへの同期0。実client GUIマウス購入/LocalPlayer/TCP配送は今回未実施 |
| unit | 新購入 **153 assertions**、既存取得 **142**、P **150**、T **53**。既存TimeStop9・SW47・data-boundary5000・Ammo10000 samples等もbuild既定unit内で成功 |
| GameTest | **vanilla67/67・TaCZ67/67**。旧60ケースを維持し7追加、MC1.20.1/Forge47.2.0 userdev最終source。TaCZ1.1.7-hotfix2、固定approved hashをbuild gateで確認。外部Trial/L2/FE実攻撃suite再実行0 |

[build-02](../build/verification/mastery-purchase-20260929-073000/audit/build-02.log) compileJava/build/foodHealingUnitTest/check SUCCESS。local Gradle8.1.1/Java17.0.7/cache/offline。build既定依存により既存fixtureのcompile/reobfは行われるが、外部攻撃suiteの起動は0。初回[build-01](../build/verification/mastery-purchase-20260929-073000/audit/build-01.log)はsandbox native-platform.dllロード前失敗、承認された権限実行で解消。compile/test失敗なし。Jar監査スクリプトの文字列/内部class差分想定だけを修正し、製品・期待値の追加修正はしていない。

[vanilla log](../build/verification/mastery-purchase-20260929-073000/audit/vanilla-01.log)／[TaCZ log](../build/verification/mastery-purchase-20260929-073000/audit/tacz-01.log)。両新規GameTest worldは通常Stopping server→Saving players/worlds→All dimensions saved→終了exit0。[process照会](../build/verification/mastery-purchase-20260929-073000/audit/final-processes.json)対象Java残存0。localhost空公開鍵応答だけ使用し外部取得0、認証成功とは扱わない。既存default Config生成/asset/Gradle WARNは残るが両run ERROR/FATAL0。旧world/Prism/client/HUMAN操作0。

### 最終Jar・次の1作業

[foodhealing-3.0.0.jar](../build/libs/foodhealing-3.0.0.jar)：**307,233 bytes /198 entries /SHA-256 C12C046E0A218E1FB159A607F09F9B681B248AB4C10D0DCE3C4650D88F12584F /protocol7**。[監査](../build/verification/mastery-purchase-20260929-073000/audit/final-jar-audit.json)でManifest/mods.toml・5 Mixin config/全class存在・refmap/reobfを確認。ZIP変更はFoodHealingSkills familyとManifestだけ、追加/削除entry0。外部class/Jar/fixture/GameTest/ExampleMod混入0、全Adapter/Mixinとprotocol classは旧Jarと同byte。新Jarを実外部攻撃/実clientで再試験したとは記録しない。既存PASSは当時の対象Jarのまま保持。

開始前の全src・3文書等・build.gradle・旧Jarは build/verification/mastery-purchase-20260929-073000/before/ に保全。diffはbackup比較でありgit diffではない。**次は別承認後のqueue #5 Pam詳細LOCK/実装/限定検証。今回は#5未開始で停止。** P/T PURCHASE READY、SP安全検証維持。#7他provider Creative Flight喪失の正式blockerは未着手。RC=NO/REAL2CLIENT=BLOCKED、広いSAFE DESIGN PROVEN=NO/SOURCE DIMENSION TRANSITION VERIFIED=NO/ownership BLOCKED・vehicle未LOCK、他個別gate・可逆クラフト既知許容仕様を維持。


<a id="pam-trees-50-recipes-result"></a>
### 14.59 queue #5 Pam Trees — 50標準recipe実装／補足環境STOP（2026-09-29 08:29 JST）

**#5 PENDING／#1–#4 COMPLETE／残5／#6 NOT STARTED**。今回の仕様判断はすべてLOCK。
正本：[SPEC§14.1](SPEC.md#141-pams-harvestcraft-2---trees-harvest-duplication)、
[COMPATIBILITY§16](COMPATIBILITY_POLICY.md#16-pams-harvestcraft-2---trees--v300-release-required)。
§14.34等の当時の未LOCK/NOT STARTEDは履歴として保持。

実行root：build/verification/pam-trees-20260929-080400/。
[総合照合・STOP](../build/verification/pam-trees-20260929-080400/audit/reviewed-results.json) / [原物50mapping](../build/verification/pam-trees-20260929-080400/audit/pam-original-audit.json) /
[変更前保全](../build/verification/pam-trees-20260929-080400/before/)。
DownloadsのTrees1.0.2は777,303 bytes・指定SHA一致。成熟mapping50/unique50はSPEC snapshotと差分0。

| 単位 | 実測結果・証拠 |
|---|---|
| production | 単一[canonical JSON](../tools/data/pam_trees_1_0_2_harvests.json)＋[local generator](../tools/generate_pam_tree_recipes.py)→50標準shapeless。全mod_loaded・item minecraft:logs・base2。製品Java/通常test/build.gradle変更0 |
| Trees present | Forge47.4.0 / Trees1.0.2 / 最終配布Jar直接。[138cases/1436assertions PASS](../build/verification/pam-trees-20260929-080400/present-01/pam-result.json)。initial50＋reload50の各manager match/assemble/実CraftingMenu PICKUP・1+1消費・再delivery0。apple/paper/stringは通常NBTなし入力 |
| log/negative | vanilla12item（oak/log/wood/stripped＋Nether全stem/hyphae/stripped代表）と別namespace tagged log成立。planks/stick/stone/非tag log名item、sapling/tree block/roastedalmond/carrot/別namespace果実crop、追加占有slot不成立。fixture果実/cropはaggregate tag参加 |
| FPM/transaction | apple未取得2/OFF2/ON4、paper/string ONでも2。carried60+4=64、shift材料64+64→256/各stack≤64・再取得0。inventory63→64＋native drop3。独立2 ServerPlayerは4対2・相互汚染0。実認証2clientではない |
| absent | [103cases/106assertions PASS](../build/verification/pam-trees-20260929-080400/absent-01/pam-result.json)。initial/reloadで全50ID不在、通常wheat→bread成立。Trees present/absentのERROR0 |
| supplemental | [5cases/66assertions成立](../build/verification/pam-trees-20260929-080400/supplement-01/pam-result.json)：実Crops1.0.3 agave/rice/strawberry非適用、2x2逆配置/消費、ServerRecipeBook全50受理。ただし下記環境STOPによりrun全体の正常ロードPASSは不可 |
| build/core | [compileJava/build/unit/check成功](../build/verification/pam-trees-20260929-080400/audit/build-01.log)。独立fixture compile/reobf2回成功、[task graph](../build/verification/pam-trees-20260929-080400/audit/fixture-task-graph.txt)に製品build/既存suiteなし。最終source Forge47.2.0 userdevの[vanilla67/67](../build/verification/pam-trees-20260929-080400/audit/vanilla-final.log)・[承認TaCZ67/67](../build/verification/pam-trees-20260929-080400/audit/tacz-final.log)。coreを配布Jar直接試験と混同しない |
| 終了 | 新規dedicated3はsave-all flush→stop・全dimension保存・exit0。core2serverも自動通常保存/終了exit0。[対象5PID終了](../build/verification/pam-trees-20260929-080400/audit/final-processes.json)。停止runのworld/log/receiptも保全 |
| Jar | **328,075 bytes/250 entries/SHA-256 A76B4C9B5C22DB96CDCD8CDA36F2E8F6C8E0A3FC92405DC5FFA00DFEE9BEF001**。protocol7、50recipe＋2directory追加、既存entry差分はManifestのみ。全既存class/Mixin/refmap byte同一、reobf成功、外部Jar/class・fixture/generator/ExampleMod非混入。[監査](../build/verification/pam-trees-20260929-080400/audit/final-jar-audit.json) |

**STOP理由（製品不具合とは断定しない）**：要求された「無関係Pam crop」の実物負対照を補うため、
既知参照配置のCrops1.0.3を新supplementへ読み取りコピーした。過去manifestと同hash
81F743457E1CDD9A3061E23BE094920562FD1C57CF29E78CF77A71C1F27DA4F0。
同Jarのpamhc2crops:torch_cattailとpamhc2crops:cookingoil_x4_canola_x2が未配置の
pamhc2foodcore:cookingoilitemを参照し、[log161/199](../build/verification/pam-trees-20260929-080400/supplement-01/console.log)でparse ERROR2。
fixtureの5case PASSは環境ERRORを検査しておらず、終了後のread-onlyログ監査のERROR-free検査失敗で発見。
receiptを総合PASSへ転記せず利用者§48のmissing registry STOPを適用。検出時は全server正常終了済み。
Food Core追加・原Jar/recipe変更・再試験・期待値緩和0。Food Core/Cropsを製品の必須依存へ追加しない。

**次の1作業**：補足負対照環境のFood Core参照不足をローカルartifact/metadataで切り分け、
承認後にその不足確認だけを完了する。Trees138/absent103/core67×2は完了済みとして維持し、理由なく再実行しない。
実client/JEI UI/Botany Pots/全Pam版/認証REAL2CLIENTはNOT TESTED。#6以降未開始。
P/T PURCHASE READY・SP保護、RC=NO、REAL2CLIENT=BLOCKED、#7 Flight正式blocker、
広いSAFE DESIGN/ownership gate・vehicle未LOCKを維持。可逆クラフト増加は既知許容仕様・バグ修正対象外。


<a id="pam-vanilla-51-recipes-result"></a>
### 14.60 queue #5 vanilla apple/cocoa＋Pam49 — 2026-09-29 09:04 JST

**#5 PENDING / EXTERNAL SUPPLEMENT ARTIFACT REQUIRED、#1–#4 COMPLETE／残5／#6 NOT STARTED**。
今回の新仕様は[SPEC§14.1](SPEC.md#141-pams-harvestcraft-2---trees-harvest-duplication)へLOCK。
§14.59の旧50条件付き/present138/absent103/core67×2と旧STOPは当時の結果として保全。旧world/helper/receipt再利用0。

開始Jarは328,075 bytes/250 entries、SHA A76B4C9B5C22DB96CDCD8CDA36F2E8F6C8E0A3FC92405DC5FFA00DFEE9BEF001一致。
[開始前backup](../build/verification/pam-vanilla-20260929-085041/before/)はsrc/tools/5文書/build.gradle/Jar。
今回root：build/verification/pam-vanilla-20260929-085041/、[総合判定](../build/verification/pam-vanilla-20260929-085041/audit/reviewed-results.json)。

| 項目 | 今回の実測 |
|---|---|
| 生成/製品差分 | Pam canonical50 byte不変。generatorを49条件付き＋2常時へ変更、旧apple JSON削除・新apple/cocoa JSON追加。決定的再生成diff0。既存Java/test/build.gradle変更0。[generation](../build/verification/pam-vanilla-20260929-085041/audit/generation.json) / [差分](../build/verification/pam-vanilla-20260929-085041/audit/source-boundary.json) |
| Pam present | Forge47.4.0、Trees1.0.2、最終配布Jar直接。[150cases/1482assertions PASS](../build/verification/pam-vanilla-20260929-085041/present-01/pam-result.json)。initial/reload各51active・Pam conditional49・旧apple IDなし。direct harvest50/50＋cocoaを実RecipeManager/assemble/CraftingMenu PICKUP・1+1消費・再delivery0で確認 |
| Pam absent | 最終配布Jar直接。[146cases/295assertions PASS](../build/verification/pam-vanilla-20260929-085041/absent-01/pam-result.json)。initial/reload各2active、49inactive。apple/cocoa双方の実クラフト・通常wheat→bread、parse/missing registry/class/startup ERROR0 |
| tag/negative | oak/stripped/wood/Nether stems/hyphae/stripped計12member・別namespace tagged log成立。apple/cocoa＋planks/stick/stone/log名非tagitem・extra slot不成立。non-whitelist収穫物も不成立 |
| FPM/transaction | apple未取得/OFF/ON=2/2/4、cocoa=2/2/2。paper/string2維持。normal60+4=64、shift材料64+64→256/stack≤64・再delivery0、inventory+1/native drop3・独立2playerを変更後Jarで確認。補足ではcarried63にresult4の取出を拒否しinput不消費、carried空なら4を1回取得 |
| Food Core READ ONLY | [適合調査](../build/verification/pam-vanilla-20260929-085041/audit/foodcore-readonly-qualification.json)。Crops1.0.3にFood Core依存/range宣言なし、2recipeのID直接参照が実要件。既知ローカルFood Core1.0.5はMC [1.20,)/Forge・loader [40,)・追加必須MODなし。cookingoilitem正式登録確認 |
| supplement | Trees+Crops1.0.3+Food Core1.0.5+最終製品+独立fixture。[7cases/75assertions成立](../build/verification/pam-vanilla-20260929-085041/supplement-01/pam-result.json)：旧2recipe実ロード、crop3負対照、2x2逆配置、ServerRecipeBook51、carried不足拒否。ただし下記ERROR3によりrun全体はFAIL/STOP |
| build/core | [compileJava/build/foodHealingUnitTest/check SUCCESS](../build/verification/pam-vanilla-20260929-085041/audit/build-01.log)。専用fixture限定compile/reobf2回成功。[vanilla67/67](../build/verification/pam-vanilla-20260929-085041/audit/vanilla-final.log)・[承認TaCZ67/67](../build/verification/pam-vanilla-20260929-085041/audit/tacz-final.log)は最終source Forge47.2.0 userdev。Trial/L2/FE別suite再実行0 |
| 終了 | 新規dedicated3はsave-all flush→stop/全dimension保存/exit0、core2も通常保存終了exit0。対象[dedicated3＋launcher2 PID終了](../build/verification/pam-vanilla-20260929-085041/audit/final-processes.json)。旧/今回の停止world・log・receipt保全 |
| 最終Jar | **328,544 bytes/252 entries/SHA-256 B472C1311FF6FDBA1304279AA7087E378628398E721D086E747BB61A9A0CD39D**。protocol7、49conditional＋2unconditional。既存Java/Mixin/refmap byte同一、metadata不変・Manifest更新。custom serializer/外部class・Jar/fixture/generator/ExampleMod非混入。[監査](../build/verification/pam-vanilla-20260929-085041/audit/final-jar-audit.json) |

**残るSTOPは旧Crops不足とは別**。旧2レシピは今回nativeにロードされ、cookingoilitem不在は解消。
しかしローカルFood Core原物（911,489 bytes、hash **1F18655D5EEEA99EECCF0D9D458DBF88D6B59C1A9F00C17E322DB8D55A54ED5B**）が、
pamhc2foodcore:melonpieitem / honeymuffinitem / caramelcupcakeitem を自身のrecipe resultで参照し、実registry不在。
[console.log161/194/227](../build/verification/pam-vanilla-20260929-085041/supplement-01/console.log)でERROR3。[Registration bytecode](../build/verification/pam-vanilla-20260929-085041/audit/FoodCore-Registration.txt)にも同3登録なし。
fixtureの7case結果と独立したlog-error検査でrunner exit1、Minecraft自体の正常終了exit0とは区別。
原Jar/recipe削除・修正、偽registry、期待値緩和、再試験、外部download0。

候補探索はDownloads直下・libs・既知参照mods/過去manifestのみ。今回確認できたFood Core候補は1.0.5だけで、
metadataは適合してもERROR0を満たす実物とは認定できない。**不足：MC1.20.1/Forge47.4.0でcookingoilitemを供給し、
自己recipeのmissing registryもないFood Core artifact。Cropsに要求version rangeはなく、具体的な代替版番号は未確定**。
Crops/Food Coreを製品必須依存にはしない。

**次の1作業：この補足条件を満たすFood Core原物の受領・照合後、補足だけを新runで確認する。**
今回のpresent150/absent146/core67×2を旧仕様へ戻したり理由なく再試験しない。#6 count/legacy bonus正式回帰以降は未開始。
実client/JEI UI/REAL2CLIENT/Food Core全機能は未試験、RC=NO・#7 Flight blocker・P/T購入/SP保護・他gate維持。
可逆クラフト増加は既知許容仕様・バグ修正対象外。

<a id="pam-foodcore-local-recheck"></a>
### 14.61 queue #5 Food Core限定再探索 — 2026-09-29 09:16 JST

**#5 PENDING / EXTERNAL SUPPLEMENT ARTIFACT REQUIRED／#1–#4 COMPLETE／残5／#6 NOT STARTED**。
[今回の探索・identity・実行0記録](../backups/pam-foodcore-local-check-20260929-091519/audit.json)。Downloads直下、libs、repository run/mods、既知source-world-bootのgame/mods、直前supplementのmodsを非再帰で確認。既知current-wrapper-control 2件のcopy-plan.jsonからPam原物参照先も照合。旧world・認証領域・全ディスク/cache探索なし。

Food Coreは既知原物と直前配置コピーの2パス、**distinct artifact1／新候補0**。
原物：build/verification/source-world-boot-20260906-172148/game/mods/pamhc2foodcore-1.20.4-1.0.5.jar。
両コピーとも911,489 bytes、SHA-256 **1F18655D5EEEA99EECCF0D9D458DBF88D6B59C1A9F00C17E322DB8D55A54ED5B**。
実metadataはMOD ID pamhc2foodcore、Manifest1.0.5、javafml/Forge [40,)、Minecraft [1.20,)、追加必須MODなし、nested Jarなし。ファイル名から互換を推定しない。

既存§14.60証拠のcookingoilitem正式登録・Crops2recipe load成立は維持。一方melonpieitem / honeymuffinitem / caramelcupcakeitemの自己recipe output未登録ERROR3も維持し、同hashを適合候補として採用しない。全recipe静的照合と新補足runは新候補がある場合だけの工程で、今回は対象なし。旧7cases/75assertions成立と環境FAILを変更しない。

**今回ゲーム/server/build/fixture compile/補足run/generator/download各0**。新run・helper・world作成なし、save/stop/process検証の新実績なし。旧正常保存終了の証拠は保持。製品328,544 bytes/252 entries/SHA-256 **B472C1311FF6FDBA1304279AA7087E378628398E721D086E747BB61A9A0CD39D**、protocol7。開始・終了hash一致と保護対象source/test/resources/tools/build設定/config不変を照合。

**必要物**：MC1.20.1/Forge47.4.0で使用でき、pamhc2foodcore:cookingoilitemを正式登録し、自己recipeのmissing registry output ERRORがないFood Core原物。CropsにはFood Core要求version range宣言がなく、代替版番号は未確定。受領後にmetadata・全own recipe result登録・ingredient/依存充足を照合し、全採用条件成立時だけ補足7case/ERROR0/通常保存終了を新runで確認する。原Jar修正・偽item・recipe overrideで代用しない。

Pam canonical50、製品49+2=51、absent2/present51、apple2/2/4・cocoa2/2/2と§14.60主検証PASSを維持。SPEC§14.1とCOMPATIBILITY_POLICY§16は内容変更不要のため不変。#6以降未開始、#7 Creative Flight喪失release blocker、P/T PURCHASE READY・SP保護、RC=NO、REAL2CLIENT=BLOCKED・他gate維持。可逆クラフト増加は既知許容仕様・バグ修正対象外。

<a id="pam-foodcore-known-upstream-acceptance"></a>
### 14.62 queue #5 既知upstream defect受入・既存証拠再判定 — 2026-09-29 09:40 JST

**#1–#5 COMPLETE／残4／#6–#9 NOT STARTED**。今回の明示利用者決定により、Food Core1.0.5のexact3既知不具合を、Food Healing由来ERROR0・必要Pam経路成立とは分離する。[正式acceptance](COMPATIBILITY_POLICY.md#16-pams-harvestcraft-2---trees--v300-release-required) / [仕様](SPEC.md#141-pams-harvestcraft-2---trees-harvest-duplication)。旧§14.60/14.61のERROR0方針と当時のFAIL/PENDING、runner exit1は過去履歴のまま保全。本節は新試験PASSではなく、方針変更後の既存証拠による完了判定。

[機械分類・原物照合・原証拠hash](../backups/pam-acceptance-reclassification-20260929-093741/reclassification.json)。既存supplement-01のconsole.log全体を列挙し、ERROR/FATALイベントは**ERROR3/FATAL0、Food Healing0、unexpected0**。

| log行 | Food Core自身のrecipe ID | missing result/item |
|---|---|---|
|161|pamhc2foodcore:melonpieitem|pamhc2foodcore:melonpieitem|
|194|pamhc2foodcore:honeymuffinitem|pamhc2foodcore:honeymuffinitem|
|227|pamhc2foodcore:caramelcupcakeitem_x4|pamhc2foodcore:caramelcupcakeitem|

各JSONの唯一の出典が配置Food Core原Jarで、resultも上記と一致。Food Healingやfixtureによる同resource overrideなし。採用Food CoreはMOD ID pamhc2foodcore/Manifest1.0.5/911,489 bytes/SHA **1F18655D5EEEA99EECCF0D9D458DBF88D6B59C1A9F00C17E322DB8D55A54ED5B**、javafml/Forge[40,)、MC[1.20,)、追加必須MOD・nestedなし。原物・配置物・当時inputs記録をhash一致確認。1.0.0への切替なし。3recipeは**UNAVAILABLE / BROKEN IN THIS UPSTREAM ARTIFACT**であり修正済みとはしない。

既存証拠を維持：present150/150（1482 assertions）、absent146/146（295）、Pam harvest50・51active/absent2・旧apple0・reload/実CraftingMenu/1+1消費/tag/negative成立。補足**7/7・75assertions**：native cookingoilitem/旧Crops2recipe、agave/rice/strawberry負対照、2x2逆配置、ServerRecipeBook51、carried63+4拒否/input不消費→空なら4を1回取得。vanilla/TaCZ各67/67、build/unit/checkも既存PASS。件数を今回の実行数へ加算しない。

補足log297でstartup完了、305–310で通常save/all dimensions、312–321でstop/全保存。process.jsonでJava PID38568 exit0/exited、final-processes.jsonで終了。旧runner exit1は全ERROR拒否ポリシーの結果であり書換えない。新方針で既知3件だけNON-BLOCKINGと分類し、必要経路と正常終了を確認できるため**新run不要・ゲーム/server/build/fixture compile/generator/download0**。runner/helper変更0。

製品は**328,544 bytes/252 entries/SHA B472C1311FF6FDBA1304279AA7087E378628398E721D086E747BB61A9A0CD39D／protocol7**。開始・終了hash、source/test/resources/tools/build設定/config、外部原物・配置Jarと旧証拠不変。製品Java/recipe/依存/workaround追加0。Pam canonical50、49+2=51、apple2/2/4・cocoa2/2/2・paper/string2不変。

**次の1作業はqueue #6 Nutrition/Food-Level countの別プロンプト待ち**。今回threshold/migration/旧bonus回帰へ着手しない。#7他provider Creative Flight喪失FORMAL RELEASE BLOCKER、#8/#9未開始。RC=NO、REAL2CLIENT=BLOCKED、SAFE DESIGN PROVEN=NO、SOURCE DIMENSION TRANSITION VERIFIED=NO、ownership BLOCKED、UOM/P vehicle未LOCK・他開始gate維持。可逆クラフト増加は既知許容仕様・バグ修正対象外。Food Core全機能/長時間play/client UI/JEIの完全互換を宣言しない。


<a id="nutrition-count-completed"></a>
### 14.63 queue #6 Nutrition/Food-Level count — 2026-09-29 10:51 JST

**#6 COMPLETE／#1–#6 COMPLETE／残3／#7–#9 NOT STARTED**。利用者LOCKの正本は[SPEC§14.2](SPEC.md#142-nutrition--food-level-based-shokugi-count)。1000 provisionalは不採用、default2000をConfig変更可能に実装。主体はAUTOMATED、MC1.20.1/Forge47.2.0 userdev。client/Prism/原本・過去world起動なし、外部downloadなし。

#### 実装と移行境界

- 成功したserver Finishでdeclared Nutritionを1回加算。Forge実sourceではItemStack/Item.finishUsingItem→Player.eat→FoodData.eat(Item,ItemStack,LivingEntity)→eat(int,float)→Finish。この食事由来のeat呼出だけをtry/finally scopeで識別し、generic加算から除外。非食事はFoodData.eat(int,float)/setFoodLevelの実正deltaを即時観測する。5tick予約/近接tick推測は撤去。旧FoodHealingTransactionsの公開入口は互換用no-op、旧純粋計算のunitは履歴回帰だけでruntime予約なし。
- 満腹・Satisfactionもdeclared値1回。回復はunits×2×Recoveryの既存式、Root/True Rootへはfood Nutritionだけ。非食事delta18でRoot蓄積/発動/予約0。食品固有effectは維持。
- `NutritionProgress`がlongのcount+units、level+gained、unspent+gainedをcheckedで全計算後、`ShokugiData.addNutritionUnits`が3値をcommit。invalid/pending/overflowは全progression無変更。食事・HP回復等は独立して成立。通常countは0≤count<T。Tを既存count以下へ後日変更した場合も勝手にmodulo/返金/追加SPせず更新拒否し、管理者による別途確認が必要。
- Config `general.shokugiNutritionLevelUpRequirement` default2000 (long)、`general.shokugiCountModelVersion=1`、初回effective legacy値を`general.legacyFoodActionThreshold`へ保存。旧`general.shokugiLevelUpRequirement`はdeprecated移行入力のみ。ForgeConfigSpecがdefault補完/保存する前に元TOMLを読む。新key明示値優先、なければ旧値×10。元file保全＋同directory atomic replace、model1は再変換0。不正Configは原fileを残しplayer load前に停止。旧player NBTにhistorical thresholdが無い限界を維持し、過去値を捏造しない。
- schema4→**5**で正常旧countをfloor(C×Tnew/Told)へ1回変換。v3既取得/level/SP/Root等を保全。v2は既存1:1 respecのままcanonical countだけ変換、raw backupのC不変。不正partial/type/provenance/overflowはpending＋raw保持、旧v2へのSP誤返還なし。schema5 reload/clone/syncで再変換0。Configとplayerのversionは別寿命。
- GUI/HUD/管理コマンドは新threshold。NBT envelopeを維持しthresholdをlongで運ぶ。packet登録/decoderの構造は不変、protocol7維持。外部private field直書き・独自auto-feedはこの通常API経路の検証から全互換へ拡張しない。

#### 今回の実測

| 項目 | 最終結果・証拠 |
|---|---|
| build/unit/check | [acceptance.log](../build/verification/nutrition-count-20260929-101705/audit/acceptance.log) **PASS**。Nutrition焦点93 assertions（旧custom100/200/250/400、新明示4000、×10再発なし、Forge補完後も値保持、型不正/pending/overflow等）＋既存unit全件。旧67を削除せず追加8 |
| vanilla / TaCZ | **75/75 / 75/75 PASS**。最終source：[vanilla](../build/verification/nutrition-count-20260929-101705/audit/acceptance.log) / [TaCZ](../build/verification/nutrition-count-20260929-101705/audit/tacz-acceptance.log)。承認TaCZ1.1.7-hotfix2、追加実射撃suite0。これは配布Jar直接の外部攻撃/client試験ではない |
| food | native use開始成立→ItemStack.finishUsingItem→Forge Finish。Nutrition1/4/5/8、満腹8、stack3→2/NBT保持、最後のstew→bowl、Satisfaction成功/通常消費対照。count/heal/diversity/Root/inventory二重0 |
| non-food | 10→15:+5、19→20:+1、20→20/saturation/decrease:0。同tick/1tick後/8tick後の独立増分を保持。+18でcount18/回復36・Root0 |
| progression | 1999+1→1level/count0/SP1、1995+8→1/3/1、1990+4010→3/0/3、long境界。level/SP overflowでbefore canonical同一、食事消費・回復は成立 |
| 高Nutrition旧bonus | 実登録SUPER_FOOD Nutrition50で新規Resistance/Fire Resistance0。外部Resistance amp4/duration1234とFire amp2/duration2345を短縮/降格/削除0。正式耐火取得ONは通常tickだけで有効。§28全matrix/実clientの全実施とは区別 |
| schema4 | count0/1/50/100/199→0/10/500/1000/1990、3回reload不変。Level/SP/skill/toggle/Rootを含むNBTのcount/schema以外一致。型不正・pending拒否/raw保持 |
| 実v2 capability | 新root `real-v225-migration-20260929-103400`。保存済み実v2由来level/player両payloadだけを新規vanilla templateへ取り込み、**Lv2/count35→Lv2/count350/SP2/使用済0/raw35**。Diversity All28/Current3/bonus10保持。[write](../build/verification/nutrition-count-20260929-101705/audit/legacy-write.log) / [別JVM read](../build/verification/nutrition-count-20260929-101705/audit/legacy-read.log)。旧world boot/FOURTH BOOTではない |
| disk safety | [synthetic write](../build/verification/nutrition-count-20260929-101705/audit/synthetic-write.log) / [read](../build/verification/nutrition-count-20260929-101705/audit/synthetic-read.log)。正常/不正5入力＋fresh/legacy基本2player、raw・inventory/foreign modifier保全。正常count199→1990、不正側はpending原値保持。通常save/stop/別PID確認 |
| 最終Jar | **333,375 bytes / 257 entries / SHA-256 134FDE235FCAE2D81E06FE25A5512FE7469A38EE674D05AE18BF793B350232B5** / protocol7。[監査](../build/verification/nutrition-count-20260929-101705/audit/final-jar-audit.json)：metadata/reobf/Mixin/refmap成立、fixture/external Jar/class/ExampleMod混入0、無関係Adapter/recipe115 entry byte同一 |

初回compileのlong表示型、追加GameTestのLazyOptional引数不足、既存食事fixtureの消費済みstack再利用を修正。最後の問題はfocus-02の74/75 FAILとして保全し、期待値を緩めず毎回fresh stackへ変更。focus-03で75/75、最終は使用開始assertionまで含む上表で確認。途中FAILを削除しない。

今回の全試験serverは通常保存/stop/process終了。GameTestの失敗runも通常終了し保全。[総合照合](../build/verification/nutrition-count-20260929-101705/audit/reviewed-results.json) / [変更一覧と開始前hash](../build/verification/nutrition-count-20260929-101705/audit/changed-files.json)。`.git`なし、Git diff確認とは呼ばず開始前backupとの比較。build.gradle/依存版/他compat原Jar/購入gate/SP購入条件は不変。今回の製品変更はcount/Config/移行/表示/食品原因識別のみ。

#### 次の1作業

**queue #7 Flight ownershipの明示承認待ち**。他provider Creative Flight喪失FORMAL RELEASE BLOCKERを維持し、今回は開始しない。#8/#9未開始、RC=NO、REAL2CLIENT=BLOCKED - SECOND MINECRAFT ACCOUNT REQUIRED、SAFE DESIGN PROVEN=NO、SOURCE DIMENSION TRANSITION VERIFIED=NO、ownership BLOCKED、UOM/P vehicle未LOCK。P/T PURCHASE READY・SP保護、#1–#5既存PASS、個別開始gate維持。食料生産の極意の可逆クラフト増加は既知許容仕様・バグ修正対象外。Backpack専用統合や全MOD互換は未実施であり#6の自動追加必須にしない。


<a id="flight-gate-a-attribution-blocked"></a>

> Gate Aのgeneric識別不能という技術的結果は現行のまま維持。対応契約と実装の現在値は後続[§14.65](#flight-exact-provider-completed)。下記のSTOP/購入停止/判断待ちはGate A実施時点の記録。
### 14.64 queue #7 Creative Flight ownership — Gate A READ ONLY — 2026-09-29 12:46 JST

**BLOCKED - GENERIC FLIGHT PROVIDER ATTRIBUTION。製品変更前STOP。** #1–#6 COMPLETE／残3、#8/#9 NOT STARTED。Food Healing単独の正常解除と、未知のforeign-after付与を絶対に解除しない条件を、現在の共有boolean/APIから同時に証明できない。以下はSTATIC AUDITEDであり、Flight動作PASS/ownership solvedではない。新run/fixture/build/test/client/server/downloadなし。

#### 現物と全writer / call-site

開始・終了製品は **333,375 bytes / 257 entries / SHA-256 134FDE235FCAE2D81E06FE25A5512FE7469A38EE674D05AE18BF793B350232B5 / protocol7**。schema5維持。[Jar監査](../backups/flight-gate-a-20260929-123454/product-static-audit.json)でmetadata/Mixin5設定/refmap JSON、flight reobf参照、external class/nested Jar/fixture/ExampleMod混入0を照合。Jar全byte不変のため§14.63の最終成果物を継続使用し、新しいbuild PASSへ転記しない。

`src/main`全体のabilities/flight/creative/spectator/反射・Mixin関連検索と、下記入口→canonical→tickを追跡。[配布class参照全件](../backups/flight-gate-a-20260929-123454/product-ability-references.json)も能力参照はShokugiTickHandlerだけ。[javap](../backups/flight-gate-a-20260929-123454/bytecode/ShokugiTickHandler.txt)で唯一のPUTFIELDが`Abilities.f_35936_=true`であることを確認。テスト用writerは製品writerへ混ぜない。

| source / method | trigger・条件・call-site | mayfly / flying / packet / ownership |
|---|---|---|
| `ShokugiTickHandler.java:27–57` / onPlayerTick | FoodHealingMod:108で登録、PlayerTick END・server側・本人cap有り。FoodHealingSkills.isEnabled(FLIGHT)かつmayfly=false | **製品の唯一の直接writer：mayfly=true**。flying/speed書込0、直後onUpdateAbilities1呼出。既にtrueなら書込/送信0。Creative/Spectator専用分岐なし。OFF枝は何もしない。所有token/外部provider照会なし |
| `FoodHealingScreen.addSkillControls` → ToggleSkillPacket.handle / `FoodHealingCommands.executeToggle` → RootController.setSkillDisabled | GUIはeffectiveLevel>0でtoggle表示。serverは本人sender/effectiveLevel確認、RootController:39→ShokugiData.setSkillDisabled。FlightではTrue Root専用分岐に入らない。canonical sync後に上記tickが評価 | abilities直接書込0。OFFで即時/次tick revokeも0。他player宛処理なし。canonical packetはability packetではない |
| PurchaseSkillPacket.handle → FoodHealingSkills.tryPurchase/purchaseStatus | SP/前提/expectedLevel/pending/readinessをserver検証。FlightはIMPLEMENTATION_PENDINGで購入拒否、SP不消費。成功一般経路は取得map更新＋canonical sync | abilities書込0。TaCZ専用onEnabledはFlightに適用しない |
| ShokugiProvider.deserializeNBT → ShokugiData.deserializeNBT / copyFrom | 保存load・schema/count移行・disabled/acquired復元。正常v2は既存respecで取得map空。将来schema/不正legacyはfallback抑止。current schemaの取得mapとpendingは別 | abilities/token保存0。**effect predicate自体にはpending/妥当levelの拒否がない**。current-schemaでFlight取得値>0を保持したpending入力等はgetEffectiveLevelの先行returnへ到達し得る。正常購入停止と効果安全を同一視しない。静的に判明した残件、今回再現/修正0 |
| CapabilityEvents.onPlayerCloned / onPlayerLoggedIn / onEntityJoinLevel / onPlayerChangedDimension / onPlayerRespawn | cloneは旧cap→新cap.copyFrom、owned属性/HP処理＋sync。login/join/dimension/respawnもowned属性/HP＋sync、その後通常tick | abilities書込0、Flight lifecycle/token処理なし。FoodDiversity/PlayerHealthLifecycleにもability writerなし |
| HungerChangeHandler logout/clone/dimension/server-stop、DamageEventHandler death、TimeStopRuntimeのsession lifecycle | count取引cleanup、Root death処理、別TimeStop sessionの失効等。各呼出先を能力参照検索と照合 | flight authorityを保持/解除する処理なし。これらをFlight ownershipと流用しない。TimeStop検証/変更なし |
| client ShokugiSyncPacket / HUD、compat / Mixin / command全体 | clientはcanonical NBT復元・表示。反射は既存Connection等の別用途。ability fieldへの反射/ASM writerなし | client製品ability writer0、追加writer0 |
| FoodHealingExistingSkillsGameTests:171–185（配布除外） | fixtureでFlight取得/OFF、mayfly=falseを準備しtick後非付与assert | **試験準備writer**。既付与→OFF後解除やforeign-afterの証拠ではない。今回再実行0 |

書込回数は上表の**静的site数**であり実測packet countではない。通常true維持時の無条件毎tick送信はないが、他writerが毎tickfalseにすれば次tick再grant/再送になり得る。現在source/配布JarにFood Healing由来false writerはなく、AUDIT_REPORT B8の旧実装所見を現在の犯人としない。利用者が報告したforeign flight喪失の実行時原因は**未確定**。一方、FH単独grant後OFFで自己解除しない欠落は現sourceで確認できる（lifecycleによるnative resetまで永続し得る）。

Flightの実IDは **foodhealing:flight**（FoodHealingSkillIds:24、旧alias「飛翔/hisho/flight」）。FoodHealingSkills:169のcost配列は`[2]`、最大Lv1、2SP、取得前提なし、toggle可能、disabled集合に無ければON。defaultは未取得なので無効果。purchase readinessはIMPLEMENTATION_PENDINGのまま。効果は`!disabled && effectiveLevel>0`で、取得mapの正値優先、未取得時のみpending legacy level≥30のfallback。購入readinessは効果predicateに含まれず、不正/pending保護は今後の必須受入として残る。balance/取得/SPは変更0。

#### native authority / lifecycle（実source）

使用した実sourceはMC1.20.1 / **Forge47.2.0 mapped official**のローカルsources.jar（現core基準）。[path/hash](../backups/flight-gate-a-20260929-123454/inputs.json) / [抜粋保存](../backups/flight-gate-a-20260929-123454/native-source/Abilities.java)。別Forge版を今回検証済みとはしない。

| 境界 | 実sourceと結論 |
|---|---|
| 初期/保存 | Abilities:5–42はpublic mayfly/flying boolean（new時false）、NBT abilities.mayfly/flyingへ保存・load。provider ID/refcount/付与者/期限なし。Player:746/778からload/save。NBTにtrueがあっても所有根拠にはならない |
| GameMode | GameType:53–70：Creative mayfly=true（flyingは既存値）、Spectator mayfly/flying=true、Survival/Adventureは双方false。ServerPlayerGameMode.setGameModeForPlayerが呼出し、changeGameMode/onUpdateAbilitiesで同期。正規GameMode authorityは識別可能だがforeign grantは表さない |
| login / server再起動 | PlayerList.placeNewPlayer:157でload→176でServerPlayer.loadGameTypes→GameMode abilities適用→186でability packet。SurvivalではNBT由来trueだけがそのまま確定するわけではない。新sessionに旧transient tokenなし、persistent tokenを再利用してfalseを書く根拠にもならない |
| death/respawn・End帰還 | PlayerList.respawn:440で**新ServerPlayer**。ServerPlayer.restoreFrom:1122–1159はGameMode再設定・ability sync・Forge Clone（Food Healing cap copy）、abilitiesオブジェクトの丸ごとcopyなし。End帰還にもrespawn(true)入口がある。旧instanceのtokenを転用不可 |
| 通常dimension | ServerPlayer.changeDimension:720–780のForge teleporterは**同instance**を要求（別entity戻りは例外）。abilitiesを保持してpacket送信、ChangedDimension event。End→Overworld credits枝は上記respawn経路と区別。直接setterでlifecycleを偽装しない |
| client/server sync | ServerPlayer.onUpdateAbilities:1230がClientboundPlayerAbilitiesPacketを送る。ClientPacketListener:1730は受信booleanを適用。ServerGamePacketListenerImpl:1666はclient flying要求を`requested && server.mayfly`に制限。packetにprovider情報なし、clientは所有権証明元ではない |
| Forge標準機構 | ローカルForge Java **664 files**の関連検索＋IForgePlayer、ForgeCapabilities、PlayerFlyableFallEventを照合：[検索記録](../backups/flight-gate-a-20260929-123454/forge-ability-search.json)。built-in flight provider/refcount APIなし。FlyableFallは落下通知、一般Capabilityは他MODを自動登録しない。public booleanの同値PUTFIELDを必ず通知する標準hookなし |

#### 候補A–Eの評価（実装案採用0）

| 候補 | foreign-before | foreign-after | foreign先解除 | FH-only解除 |
|---|---|---|---|---|
| A baseline snapshotだけ | trueを保持可能。ただし付与者を特定した意味ではない | FH前falseなら、後からのtrue重複を識別不能 | foreignがtrueを残す/別providerが残る場合に古いbaselineでは判別不能 | baseline=falseなら解除できるがafterを奪い得る |
| B FH-owned transient token | 自分がfalse→trueを書いた事実だけ記録可能 | tokenは排他所有権ではなく、後からのforeignを検知しない | 他providerの消失はtokenから分からない | tokenだけでfalseにするとafter破壊、保留するとleak |
| C exact known-provider readonly Adapter＋自己token | nativeの現在predicateを照会できるproviderに限り候補 | **照会対象providerに限る**。未知providerは識別不能 | 全対象の正常解除・再grant条件を実物から追えば限定候補 | 未知provider不在を保証しない限り一般解除は証明不可 |
| D vanilla/Forge標準所有API | 上記版には該当APIなし | 同左 | 同左 | 同左。新独自APIをfixtureだけに通知させても実互換証明ではない |
| E conservative non-revoke（現状相当） | FHは奪わない | FHは奪わない | foreignも残す場合は残存。外部解除でfalseならFH有効tickが再grant | **不成立：FH自身が解除しない**。正式修正に採用しない |

| 候補 | relog | respawn | dimension | Creative/Spectator | server restart |
|---|---|---|---|---|---|
| A baseline | 前sessionの値は無効 | 新instanceで再取得必要 | 同instanceでもforeign変化を捕捉できない | 正規GameModeで別保護が必要 | 保存baselineは現ownerを証明しない |
| B transient token | 捨てて再評価が必要 | 旧player tokenを移植不可 | 引継だけではforeign-after不明 | mode変更後stale token解除禁止 | 失われたtokenをNBT trueから再構築不可 |
| C known Adapter | 各provider login順序を検証 | native clone/再付与を検証 | 正規predicateと移動順序を検証 | vanilla authorityを別に優先 | 外部の正式保存/再付与を照会。未知問題は全境界で残る |
| D 標準API | 利用可能なものなし | 同左 | 同左 | GameModeは分かるがprovider APIではない | 同左 |
| E 非解除 | native survival再設定で消える場合があっても自己解除の代用不可 | native新instance初期化に依存 | 同instanceではleak継続し得る | FH由来の解除0 | 起動で直ることを合格条件にしない |

識別不能の具体例：H1=`false→FH true→FH OFF`はfalseが必要。H2=`false→FH true→unknown foreign true→FH OFF`はtrueが必要。H2のforeignが必須通知のないpublic booleanへ同値を書き、独自stateを保持すれば、FHのboolean/baseline/token/vanilla packet観測はH1と同一にできる。`flying`、tick順序、NBT追加では欠けた情報を復元できない。全foreign書込を捕捉する未実証のglobal instrumentationや、次tickに他MODが戻す前提を安全方式として導入しない。

#### 承認済みprovider実物の限定STATIC AUDITED

- `fantasy_ending` **2.7.20**（18,417,128 bytes / SHA E32FD4BA2E48FCF2C84F71AA07155C63D6BFA2178BB840899307DA0DA5E5D141）：[metadata/hash](../backups/flight-gate-a-20260929-123454/fantasy_ending-1.20.1-2.7.20-all.jar.identity.json) / [bytecode](../backups/flight-gate-a-20260929-123454/bytecode/FantasyEnding-flight.txt)。FantasyEndingArmorItem.onArmorTickの**実CHEST分岐**がmayfly=true（この枝はside限定なし・直接packetなし）。TheDomainOfFadeCurio.curioTickはserver Playerへtrue＋onUpdateAbilities、onUnequipは非Creative/非Spectatorにmayfly/flying=false＋同期。これは実class/処理を読んだ結果で、名前/armor一般からの推定ではない。外部false writerの存在は確認したが、利用者の実喪失と同一原因である証拠はない。
- `ending_library` **2.1.19fix**（2,409,255 bytes / SHA 0E29AF51DABD4E0EA8A315C1886E61F3ABEE62045F196EB6913D7F4DBA6CDC34）：[metadata/hash](../backups/flight-gate-a-20260929-123454/EndingLibrary-1.20.1-2.1.19fix-all.jar.identity.json) / [bytecode](../backups/flight-gate-a-20260929-123454/bytecode/EndingLibrary-flight.txt)。EndingLibraryPlayerCapability.getAbilityMayfly/getAbilityFlyingは自MOD overrideの-1/0/+1（false/default/true）。modifyAbilitiesは非0をbooleanへ適用し、ServerPlayer/GameMode/LocalPlayerのMixinから呼ばれる。readonly query候補はあるが、全provider集合でもrefcountでもない。negative overrideとの優先関係も未検証で、Food Healingからこのstateを書き換えない。
- exact predicate/Curios slot・armor装備実経路を限定Adapterで読む余地はある。ただしabsence-safe classloading、native解除/同期/lifecycleは追加設計・統合が必要、**Adapter可否の候補でありSUPPORTED/PASSではない**。既知版をカバーしてもunknown foreign-after保証は得られない。
- Mekanism/Mekasuit、Avaritiaは`libs`・明示された既知artifact配置先・Downloads直下の限定確認では原物未確認。PC総走査/downloadなし。対象providerを選ぶ場合だけexact版の承認artifactが必要であり、受領だけでgeneric問題は解消しない。

#### STOP・次の判断

採用architectureなし。Flight購入停止/IMPLEMENTATION_PENDING・SP保護を維持。FH-only revoke、before/after、解除順序、GameMode、native lifecycle、他player、不正/pending、optional不在・送信回数の**今回の実行証拠は0**。§14.63のbuild/unit/vanilla75/TaCZ75や旧外部suite PASSをFlight検証へ転記しない。[TEST_PLAN§13](TEST_PLAN.md#13-flight)に受入を集約。

次の1作業は、利用者による**#7の対応契約/扱いの判断**。選択に必要なのは、(1) exact provider/版に対応範囲を限定する方針へ変更し、そのartifact/APIから個別可否を詰める、または(2) genericの絶対条件を維持してFlightをBLOCKEDのまま保留する（v3から延期するならrelease範囲も別途明示LOCK）、の違い。現条件のまま限定Adapterをgeneric完成にしたり、永久leak許容へ変更しない。自動で次queueへ進まない。

3文書以外の製品/source/test/build設定/Config/外部Jarは変更なし。開始前3文書backupと[保護対象照合](../backups/flight-gate-a-20260929-123454/verification.json)を保存。今回はgame/serverを起動しておらずsave/stop/process終了の新実績はN/A。#7 FORMAL RELEASE BLOCKER、RC=NO、REAL2CLIENT=BLOCKED - SECOND MINECRAFT ACCOUNT REQUIRED、SAFE DESIGN PROVEN=NO、SOURCE DIMENSION TRANSITION VERIFIED=NO、TimeStop ownership BLOCKED、UOM/P vehicle未LOCKを維持。P/T PURCHASE READY、他の開始gate、可逆クラフト増加は既知許容仕様・バグ修正対象外も不変。


<a id="flight-exact-provider-completed"></a>
### 14.65. queue #7 Flight exact-provider実装・自動統合完了 — 2026-09-29 13:35 JST

**#1–#7 COMPLETE／残2／#8・#9 NOT STARTED／Flight PURCHASE READY／RC=NO。** 利用者はA（exact provider/version限定）を正式選択し、追加回答でEndingLibraryの明示DENY優先をLOCK。§14.64の「unknown foreign-afterは標準booleanから識別不能」というGate A結果を契約変更の根拠として維持し、generic解決とはしない。契約正本は[SPEC§18](SPEC.md#flight-exact-provider-contract)、経路・原物・制約は[COMPATIBILITY_POLICY§18](COMPATIBILITY_POLICY.md#flight-exact-providers)。同じ仕様全文をこの節へ複製しない。

開始Jar：333,375 bytes /257 entries /134FDE235FCAE2D81E06FE25A5512FE7469A38EE674D05AE18BF793B350232B5。開始前5文書・source・build.gradle・Jarを[backup](../backups/flight-exact-policy-20260929-125743/before)へ保全。新しい検証rootは`build/verification/flight-exact-20260929-125743`。

#### 実装と不変条件

- `ShokugiTickHandler`のmayfly=trueだけの処理を、`FlightController` / `FlightGrantPolicy` / `FlightAuthority`へ置換。ServerPlayer identityの一時token、current provider評価、自己付与だけの解除、変更時だけ同期、logout/clone/server-stop cleanup。
- `compat/flight`のexact AdapterはEL overrideと実FE装備/Curios機能slotをreadonly照会。ALLOWとDENYが併存し得るためpositive/deny/観測可否を別に持つ。DENY中のFH新規/再付与0、解除後再評価、Creative/Spectator非書換え。
- `ShokugiData`はFlight取得のLevel型/範囲/重複を検証。不正entryは既存AcquiredSkillsの同形式でraw保持し、save/copy後に正当Lv1へ化けない。`IShokugiData`へreadonly判定を追加し、purchaseも不正拒否。schema5/protocol7維持、token永続化0、他skill/移行式変更0。
- 焦点/実provider成立後、`FoodHealingSkills`のFlight固定pendingのみ解除。2SP/Lv1/前提なし/default ON、既存GUI/server purchase・toggle/command→次END評価を確認。P/T READY・Break Realm pending維持。
- `build.gradle`へ既存承認Curios5.14.1のcompileOnly参照を1行追加。外部MOD必須化なし。独立fixtureは新verification rootだけ、旧FE6/Trial/L2 suiteを実行しない。

#### 最終証拠（中間Jarから転記しない）

| 項目 | 実施主体・結果・証拠 |
|---|---|
| compileJava/build/foodHealingUnitTest/check | **AUTOMATED PASS**。[最終log](../build/verification/flight-exact-20260929-125743/audit/final-build-01.log)。Flight unit **78 assertions**＋既存unit維持 |
| core vanilla / approved TaCZ | MC1.20.1/Forge47.2.0 userdev最終source、各 **84/84 PASS**（旧75＋Flight9）。[vanilla](../build/verification/flight-exact-20260929-125743/audit/vanilla-final.log) / [TaCZ](../build/verification/flight-exact-20260929-125743/audit/tacz-final.log)。配布Jar直接clientの結果ではない |
| 追加Flight9 GameTest | 正規purchase packet/GUI toggle packet/管理command・2SP・replay不変・次END反映、own/foreign-before、invalid/pending、mode往復、同instance dimension、death/End-return replacement、logout/save/new login、他player/無関係abilities保全。登録済み通信decode/dispatchであり実認証TCP/人力GUIではない |
| 同期 | FHのfalse→true/owned revoke各1packet、steady ON/OFF100評価・DENY50評価で追加FH packet0。provider自身のCurio毎tickpacketをFH spamへ混同しない |
| actual FE Flight integration | MC1.20.1/Forge47.4.0、最終配布Jar直接、**22/22指定観測成立**：[結果](../build/verification/flight-exact-20260929-125743/integration-final/flight-result.json)。内訳は**18経路PASS＋4胸装備保全/外部native解除なしの診断**。完全なArmor解除22/22とは書かない |
| 外部非書換え | 実provider評価でEL cap・equipment・Curios・canonicalの前後一致 **350 checks**。別player不変。正式command/equip/unequipによる試験入力と、製品readonly queryを分離 |
| 任意MOD不在 | vanilla core起動・grant/revoke/lifecycle成功。metadata mismatch equality拒否。別版実Jar/実client absenceの新規起動は未実施 |
| 最終Jar | **344,559 bytes / 265 entries / SHA-256 F53C890F77CED09F55B3DBAF0406BE25DEAF1F72D6EA1B76B218F90AA4D2216C**、protocol7/schema5。[監査](../build/verification/flight-exact-20260929-125743/audit/final-jar-audit.json)：metadata/reobf、全5 Mixin config/refmap不変、既存Adapter/Mixin/network class byte同一、外部Jar/class・fixture・GameTests・ExampleMod混入0。実FE最終mods内コピーhash同一 |
| 保存/停止 | 今回の全7新規試験server（FE4、core3、失敗run含む）で通常保存/停止・exit0。最終FEは[process](../build/verification/flight-exact-20260929-125743/integration-final/process.json)、coreは各process記録。[最終該当Java process0](../build/verification/flight-exact-20260929-125743/audit/process-final.json)。client/Prism起動0、QuitはN/A |

独立fixture `foodhealing_flight_verification` 20260929.125743：15,637 bytes /11 entries /SHA256 626444CE25F10196D9CD7AE46A839A3FB630871E7738ADADDF11E62859515696。native装備tick、Curios正式装備とnative tick/unequip、実`endinglib abilities`を使用し、manual mayfly fixtureだけをexternal PASSへしない。新規run以外のworldは再利用なし。

#### 途中の停止・修正・限界

1. 最初のGradleはsandbox native DLLで起動前失敗。承認済みlocal8.1.1/cache/offline権限実行で解消。
2. fixture init相対パスがNO-SOURCEとなった出力は試験PASSにしない。project.fileへ修正し、実source/metadata/classの存在を確認。Curiosの誤ったserializeNBT呼出は実在writeTagへ修正。
3. `integration-focus-01`はCuriosを旧L2構成5.12.0と取り違え、実ロード5.14.1との版照合で**FAIL、0測定**。原物を交換せず、既存承認FE構成にgate/API参照を合わせた。
4. `integration-focus-02`はCurio4＋Armor4観測後、EL native commandのUUID対象指定がplayers引数に拒否され**FAIL**。補助の対象指定を実player名へ修正し、製品/期待値は弱めていない。
5. `integration-focus-03`は22観測成立した中間Jar。ready/通常購入追加後、`integration-final`で最終Jarの22観測を取り直した。旧FAIL・log・world保全。
6. 胸装備は取り外し後もmayflyを残すnative実装であり、Food Healingが外部stateを修復しない。最低正式解除routeはCurio。全FE Flight lifecycle完成・全MOD互換とは表現しない。
7. 最終FE logには既存外部tag参照欠落、flat generatorのbiome fallback、optional class警告等が残る。対象item/Curios slot/command・入力/出力は成立したが、外部全環境ERROR0とはしない。新download/認証情報読取0。localhost公開鍵stubは実アカウント認証ではない。

変更一覧・filesystem比較（.gitなし）は[implementation.diff](../build/verification/flight-exact-20260929-125743/audit/implementation.diff) / [監査](../build/verification/flight-exact-20260929-125743/audit/final-jar-audit.json)。製品変更は今回Flightと必要な取得安全化だけ。Pam/L2133/Cube49/Invader86/FE6/TimeStop別suite、過去実client、原本world、#8/#9の実行0。

次の1作業は **queue #8 最終build/必要回帰/Jarの承認範囲確認**。今回#7のPASSを再試験理由へ戻さず、残2として停止。RC=NO、REAL2CLIENT=BLOCKED - SECOND MINECRAFT ACCOUNT REQUIRED、SAFE DESIGN PROVEN=NO、SOURCE DIMENSION TRANSITION VERIFIED=NO、TimeStop広域ownership BLOCKED、UOM/P vehicle未LOCK・他機能gateは維持。食料生産の可逆クラフト増加は既知許容仕様・バグ修正対象外。


<a id="flight-mekanism-exact-provider-completed"></a>
### 14.66 pre-#8 Mekanism / MekaSuit exact-provider extension — 2026-09-29 19:12 JST

**MEKANISM FLIGHT EXACT-PROVIDER EXTENSION COMPLETE**。#1–#7 COMPLETE／残2／#8・#9 NOT STARTED／Flight PURCHASE READY／RC=NO。 §14.65の既存最低契約は完了のまま維持。GENERIC FLIGHT PROVIDER ATTRIBUTION=NOT AVAILABLE。対応原物・正式routeの正本は[COMPATIBILITY_POLICY§18](COMPATIBILITY_POLICY.md#flight-exact-providers)、契約は[SPEC§18](SPEC.md#flight-exact-provider-contract)。今回#8/#9、Avaritia、他保留機能へ進まない。

#### Gate M・最小実装

- 開始製品344,559 bytes/265 entries/F53C890F…16Cと受領Mekanismを独立実測して全期待一致。[inputs](../build/verification/flight-mekanism-20260929-184800/audit/inputs.json)。5文書・source/test/build設定・開始Jarを[backup](../backups/flight-mekanism-20260929-184800/before)保全。新root `build/verification/flight-mekanism-20260929-184800`、新worldだけ使用。
- [Gate M静的照合](../build/verification/flight-mekanism-20260929-184800/audit/gate-m.json)と[全Jar constant-pool候補一覧](../build/verification/flight-mekanism-20260929-184800/audit/flight-constant-pool-inventory.json)：実`PlayerState.updateClientServerFlight`だけが対象mayfly/flyingを書きServerPlayerへ能力packetを送る。`updateFlightInfo`はreadyまたはnative flying GameModeで`hadFlightItem=true`、失効時に一度解除後falseへ戻す。`wasFlyingAllowed/wasFlying`は前tick値であり装備前baselineではない。dimensionは`reapplyServerSideOnly`、logoutは`clearPlayerServerSideOnly`でUUID entryを除去、server reset(false)でmap clear。respawn callbackはFlight mapを直接消さず、native clone後のcurrent装備で再評価する。
- query経路は`getCompound→getDataValue→getDataMapIfPresent`の既存tag読取、temporary Module/config生成、energy比較。getter名だけで判断せず、save/setter/extract/packetを呼ばないことをbytecodeで確認。実測前後不変は下表。`isGravitationalModulationOn`はready AND flyingなので採用しない。
- `compat/flight/MekanismFlightGate`（MOD ID・10.4.16・承認hash、初回だけstream hash）と`MekanismFlightProvider`（exact chest＋public Ready）を追加し、`FlightProviders`へ分岐1つ追加。compileOnly参照1行、FlightRegressionへidentity境界5 assertions追加。製品からprivate map Reflection・外部setter・writer抑制Mixinを追加しない。
- **FlightController/FlightGrantPolicy/FlightAuthority、既存FE/EL Adapter、購入gate、ShokugiTickHandler、network/schemaはbyte同一**。新token/永続field/独自DENY/装備修復なし。EL明示DENY優先を維持。配布classの既存変更はFlightProvidersだけ。[filesystem差分](../build/verification/flight-mekanism-20260929-184800/audit/implementation.diff)。.gitなし、git diff実施とは記録しない。
- 実登録listener一覧は両者NORMAL、server ENDでFH FlightController→Mekanism CommonPlayerTickHandlerの順。全Shokugi priority変更0。実EVENT_BUSを通し、失効時のnative false後、次のFH評価で必要なtrueを付与する。

#### 最終配布Jarのactual統合結果

全てAUTOMATED。MC1.20.1/Forge47.4.0/Mekanism10.4.16/FH3.0.0/独立補助だけの実ロード。実client/TCP認証・人間操作ではない。別fixtureの正式addModule/toggleEnabled/equipment、有限energy API、実PlayerTick END/PlayerStateを使用し、手作りmayflyだけの外部PASS0。[28件の結果・全snapshot・writer列](../build/verification/flight-mekanism-20260929-184800/integration-focus01/flight-result.json)。ファイル名focus01は開始時のrun名であり、後述の最終Jarと同一hashを確認した結果。

| 群 | 件数 | 観測結果 |
|---|---:|---|
| Meka-only grant / revoke | 2 | native false→true→false、FH tokenなし。moduleOFF直後のstale mayfly=trueでもpredicate=false |
| Meka-before / FH-before / provider-first-OFF / FH-first-OFF | 4 | FH OFFはMeka保持＋token clear、後のMeka disableはnative解除。provider-firstはnative解除1回→FH再付与1回、次のsteady100で能力write/packet0 |
| module disable / chest remove × FH OFF/ON | 4 | 正式入力後native解除、FH OFFでは終了、FH ONなら自己再付与。各steady100で反復0、外部装備修復0 |
| energy | 3 | native既定必要量1000、有限2000→1000→0を合法flight入力/native消費で実測。1000はready、999は非ready。枯渇native解除、FH補充0。boost/Jetpackは対象外 |
| Creative / Spectator × FH OFF/ON | 4 | Survival→native mode→Survival、mode中FH token clear/非書換え、current Mekaで再評価、steady40で反復0 |
| native dimension × FH OFF/ON | 2 | 同ServerPlayer Nether/Overworld往復、Meka authority・装備・現在stateを保持、FH外部修復0 |
| death-respawn / normal save-logout-new login | 2 | native PlayerList replacementで旧FH tokenなし・keepInventory=false chest不継承を確認。別caseで保存/logout→同UUID新playerにchest/module/energy/canonical復元、Mek旧map entry除去、current predicate再評価 |
| unowned / OFF / pending / future / invalid level / malformed | 6 | 新規FH grant0、Meka trueをFHから修復0、native provider失効後も不正canonicalから再付与0 |
| another player | 1 | AのFH toggleでBのabilities/inventory/装備/module/energy/Mekstate不変、逆方向も不変 |
| normal save/stop | 件数へ加算しない | 自動統合server通常save→halt/stop、全dimension保存、exit0、process終了 |

readonly queryとFH evaluateの前後照合 **1032 checks**（inventory内の装備/module/energy/無関係energy module、Mek map、canonical）。製品由来の外部mutation0。observerのprivate map参照はverification-only/getだけ。native入力・energy消費・native state更新と区別する。能力同期呼出の観測stack別は **FH19 / Mek43 / vanilla・other16**。これは全packet総数ではなくinstrumented ServerPlayerの`onUpdateAbilities`呼出数。native respawn replacementはsubclass observerを継承しないため、その後はsnapshotで判定しwriter総数へ補完しない。steady groupは実outbound能力packetも0。

#### build・回帰・最終成果物・終了

- localGradle8.1.1/cache/Java17、`--offline`。compileJava/[build・foodHealingUnitTest・check](../build/verification/flight-mekanism-20260929-184800/audit/fixture-focus02.log) PASS、Flight unit **83 assertions**（既存78＋identity5）。正規positive/DENY/UNKNOWN、token、取得順・OFF順・native modesは既存焦点unit維持。
- [vanilla](../build/verification/flight-mekanism-20260929-184800/audit/vanilla-final.log) **84/84**、[approved TaCZ](../build/verification/flight-mekanism-20260929-184800/audit/tacz-final.log) **84/84**（Forge47.2.0 userdev最終source、既存case削除0、Mekanismなし）。[別JVM class linkage](../build/verification/flight-mekanism-20260929-184800/audit/absence-linkage.log)：Mekanism classの物理的不在を確認して5共通classのload/signature解決PASS。実client起動の代用PASSにはしない。
- 共通FlightController/Policyと既存FE/EL class byte同一、既存policy unit回帰成立のため実FE22を重複実行しない。EL DENY＋Meka positiveの混在実MODは未実行。既存L2133/Cube49/Invader86/FE6/TimeStop/Pam/移行/旧実clientを再実行しない。
- 最終製品 **347,673 bytes / 267 entries / SHA-256 9E14D382192CC500E963CA4ACAEC0BF4FF866E816C75730B2731BF05A51144A4**、protocol **7** / schema **5**。[最終監査](../build/verification/flight-mekanism-20260929-184800/audit/final-jar-audit.json)：実統合mods内コピーと一致、metadata不変、全5 Mixin config/refmap不変・対象classあり、reobf確認、Mekanism/他外部class/Jar・fixture・GameTests・ExampleMod混入0。試験後製品再生成0、中間Jar PASS転記0。
- 独立補助 `foodhealing_mekanism_verification` **20260929.184800**、21,920 bytes/14 entries/SHA256 **99C78491EB0E899E67F7975425B02C287361A8C11133840B32AC9806415A0F83**。製品非混入。[開始入力](../build/verification/flight-mekanism-20260929-184800/integration-focus01/inputs.json)。補助compile初回は継承元server fieldと外側instance fieldの曖昧参照でFAIL、outer class明示だけで修正し再compile成功。旧log保全、期待値変更・case削除0。game測定の失敗runなし。
- 実Mek1＋core2の全3試験serverで通常保存/停止、全dimensions saved、exit0、[対象Java残存0](../build/verification/flight-mekanism-20260929-184800/audit/process-final.json)。client/Prism起動0・Quit=N/A。原Mekanism/hash不変、外部download0。localhost空公開鍵stubは実アカウント認証ではない。新worldの既定config補完・Forgeライブラリmetadata等WARNは残るがMek統合log ERROR/FATAL0。

正式claim：**Mekanism 10.4.16 exact approved artifact MekaSuit Flight SUPPORTED**（COMPATIBILITY_POLICY§18のhash・route・限定lifecycle）。実client、別JVM restart、keepInventory全設定、全provider同時構成、別build/別版、Jetpack/boostは未保証。追加artifact不足なし。#7最低契約を戻さず、pre-#8 extensionを完了して停止。次の1作業は利用者の次指示による#8の実行範囲確認であり、今回は#8/#9未開始。REAL2CLIENT=BLOCKED、RC=NO、他開始gate維持。可逆クラフト増加は既知許容仕様・バグ修正対象外。


<a id="flight-avaritia-exact-provider-completed"></a>
### 14.67 pre-#8 Avaritia 4.0.3 exact-provider extension — 2026-09-29 19:54 JST

**AVARITIA FLIGHT EXACT-PROVIDER EXTENSION COMPLETE**。**#1–#7 COMPLETE／残2／#8・#9 NOT STARTED／Flight PURCHASE READY／RC=NO**。既存FE/EL・Mekanismの実装/当時のPASSと§14.65/14.66は保持し、再実行0。GENERIC FLIGHT PROVIDER ATTRIBUTION=NOT AVAILABLE。契約は[SPEC§18](SPEC.md#flight-exact-provider-contract)、版・正式route・保証範囲は[COMPATIBILITY_POLICY§18](COMPATIBILITY_POLICY.md#flight-exact-providers)。今回#8/#9は開始していない。

#### 原物・Gate Avaritia・実装境界

- 開始製品347,673 bytes/267 entries/9E14D382…44A4と承認Avaritiaの全期待hash/size/entriesを独立照合。[入力・metadata](../build/verification/flight-avaritia-20260929-193800/audit/inputs.json)。変更前5文書・source/test/build設定・製品は[backup](../backups/flight-avaritia-20260929-193800/before)保全。新しいverification root `flight-avaritia-20260929-193800`、今回作成worldだけ使用。
- **Avaritia 4.0.3 / `avaritia` / 579,372 bytes / 537 entries / SHA-256 48F23CEA99D1D2E6CED4215B9FE3F9F45641B2CD5650C8725A3EE75B500483BB**。javafml/Forge `[47,)`。Minecraft範囲の原文は **`[1.20.1,]`**（提供説明の`[1.20.1,)`との表記差を保存）。必須はForge/MCだけ、同梱Jar0、新規download0。実ロードForge47.4.0/MC1.20.1で成立。原Jar改変0。
- [全Jar定数参照一覧](../build/verification/flight-avaritia-20260929-193800/audit/flight-constant-pool-inventory.json)と[Gate記録](../build/verification/flight-avaritia-20260929-193800/audit/gate-avaritia.json)。能力値writerは`AvaritiaEventHandler.updateFly(Player,boolean,boolean)`に集約。ArmorModel/BowFovMixinのflying参照は読取だけ。正式`@EventBusSubscriber`/`@SubscribeEvent` LivingTick、CHEST非empty・`instanceof InfinityArmorItem`、通常装備の正式ID **`avaritia:infinity_chestplate`**。**胸単独**で成立し、full set/energy/config条件なし。別Infinity部位をコマンド等でCHESTへ強制配置する異常slot・第三者subclassまで同値とは主張しない。
- 製品は`AvaritiaFlightGate`（id/版/原fileSHAを初回照合）＋`AvaritiaFlightProvider`（vanilla CHESTとForge registry IDをreadonly照会）＋dispatcher分岐1つだけ。版/hash不一致はinactive、読取例外は既存queryのUNKNOWNで非破壊。Avaritia compileOnly/class linkage/Reflection/Mixin追加0。新DENY・外部state/setter・装備修復0。
- FlightController/GrantPolicy/Authority、既存FE/EL/Mekanism classes、Shokugi全tick priority、build.gradle、購入条件、network/schemaは不変。既存配布classの変更はdispatcherだけ。unitにidentity境界5 assertions追加、既存83を保持。[filesystem差分](../build/verification/flight-avaritia-20260929-193800/audit/implementation.diff)（.gitなし）。

#### native writer / Info inventory

| trigger | mayfly / flying / sync | native Info |
|---|---|---|
| LivingTick：胸またはCreative/Spectatorで`item=false` | mayflyがfalseならtrue、flyingは既存値を保持、updateAbilities1呼出 | item=true。itemは実装上modeも含むbranch記録であり、FHのtokenではない |
| LivingTick：mode→非mode（胸あり）、または前allowed=true/現在mayfly=false | trueとInfo.flyingを適用、同期 | mode/allowed/flyingを現在値へ更新 |
| LivingTick：胸なし・通常mode・item=true | mayflyがtrueならmayfly/flyingともfalse、同期 | item=false。次tick以降の同状態は解除を反復しない |
| PlayerChangedDimensionEvent | strip後、旧allowedまたはflyingがtrueなら同Infoを再登録し両値を同期 | same UUID/sideの保存参照をnativeが再利用 |
| PlayerRespawn / LoggedIn / LoggedOut | callback自体はability書込なし | stripでentry削除。次LivingTickで現在装備から再作成 |

map keyは`UUID文字列 + "|" + isClientSide`。modeは前回Creative/Spectator、allowed/flyingは前tickのpermission/active状態であり装備前baselineではない。Infoは保存NBTではない。製品はmap/Infoを参照せず、検証補助のReflectionはgetのみ。

#### 実配布Jarによる限定自動統合

全て**AUTOMATED dedicated**。独立fixtureが実ServerPlayerの`doTick→Player.tick→LivingEntity.tick`を駆動し、正式Forge LivingTickを自然発火。`updateFly`直呼び・手動mayfly注入・合成LivingTick送信0。readonly high/low listenerで **Player START→Living HIGH→Avaritia処理→Living LOW→Player END HIGH→FH→END LOW** を実測。実client/認証TCP/通常画面飛行の代用ではない。

| 群 | 件数 | 実測結果 |
|---|---:|---|
| Avaritia-only grant/revoke | 2 | 胸のみでnative付与、取り外しnative解除、FH tokenなし |
| before/after、provider-first/FH-first OFF | 4 | FH OFFはmayfly/flyingを保持してtoken clear、外部state不変。provider-firstはnative false1回→同正常ENDのFH true1回。その後100評価で追加write/能力packet0 |
| 胸解除 × FH OFF/ON | 2 | OFFなら両false、ONなら自己再付与、各steady100でping-pong0 |
| Creative/Spectator × FH OFF/ON | 4 | native mode往復、mode中FH非書換え・tokenなし、胸状態に従う復帰 |
| dimension × FH OFF/ON | 2 | native Nether/Overworld往復、同ServerPlayer・装備保持、stale解除なし |
| respawn / logout-save-new login | 2 | `PlayerList.respawn(old,false)`の新instance/keepInventory=false胸なし・旧token/map非継承、現canonicalから再評価。別caseで通常保存/remove→同UUID新playerに胸/canonical復元しnative tickで再成立。実死亡攻撃・別JVM restart全般とは区別 |
| unowned/OFF/pending/future/invalid-level/malformed | 6 | FH新規grant0、外部true修復0、native胸解除後も不正から再付与0 |
| 別player双方向 | 1 | A toggleでBのabilities/equipment/Info不変、逆も不変 |
| **単独構成小計** | **23/23 PASS** | [全case・snapshot・caller・順序](../build/verification/flight-avaritia-20260929-193800/integration-focus01/flight-result.json) |
| EL DENY＋胸 / DENY中胸解除 | **2/2 PASS** | 承認EL2.1.19fixを実ロード、正規`endinglib abilities <name> mayfly false`。FH grant/regrant0、Avaritia native writerを抑止0、外部非修復。[結果](../build/verification/flight-avaritia-20260929-193800/integration-el01/flight-result.json) |

readonly query/FH evaluation前後の装備・inventory・Info・canonical一致 **828＋103＝931 checks**。native入力/更新とは区別。TrackedPlayerの能力sync呼出stackは単独 **FH13 / Avaritia38 / vanilla・other16**、EL混在 **FH0 / Avaritia2 / vanilla・other1**。これは全packet総数ではない。native respawn replacementはsubclass観測を継承せず、その後はsnapshotとpacket drainで確認。steady groupでは実outbound能力packetも0。観測上のFH外部mutation0。ユーザー報告の飛行不能原因をFHへ帰属する証拠ではなく、今回限定構成での共存成立。

#### 最終検証・成果物・保存終了

- compileJava/build/foodHealingUnitTest/check **PASS**、Flight **88 assertions**。[log](../build/verification/flight-avaritia-20260929-193800/audit/build-focus01.log)。独立fixture限定compile/reobf成功。通常buildの既存task依存は従来どおり、旧外部integration suite起動なし。
- core **vanilla84/84・approved TaCZ84/84 PASS**（Forge47.2.0 userdev最終source、既存case削除0）：[vanilla](../build/verification/flight-avaritia-20260929-193800/audit/vanilla-final.log) / [TaCZ](../build/verification/flight-avaritia-20260929-193800/audit/tacz-final.log)。実Avaritia統合はForge47.4.0・最終配布Jar直接。両者を区別。
- Avaritia/Mekanismの物理的不在を確認した別JVM共通/追加 **7 class load/signature PASS**：[log](../build/verification/flight-avaritia-20260929-193800/audit/absence-linkage.log)。core不在起動・purchase/ON/OFFも成功。既存FE22/Mek28は過去証拠保持、共通policy/provider class不変のため再実行0。
- 最終製品 **350,807 bytes / 269 entries / SHA-256 C106B5B67F63821289B0BB51F77E165CB7D1E0684F6BFBE228A8DF3E9851F7BC**、**protocol7/schema5**。[監査](../build/verification/flight-avaritia-20260929-193800/audit/final-jar-audit.json)：実2構成modsコピーhash一致、試験後の製品再生成0。metadata/5 Mixin config/refmap不変、全対象class存在、reobf、Avaritia/Mek/他外部class/Jar・fixture・GameTests・ExampleMod非混入。
- 補助`foodhealing_avaritia_verification` **20260929.193800**、**22,273 bytes / 14 entries / SHA-256 6E653DB960FCFD86E183330EF668F31C8F8ABF0079BB08440842C6FC8463A82C**。通常build.gradleにはsource setを追加せず、新root専用initのみ。
- **全4試験server（Avaritia1・EL混在1・core2）通常save→stop→all dimensions saved→exit0**、[残存process0](../build/verification/flight-avaritia-20260929-193800/audit/process-final.json)。Minecraft client/Prism起動0、Quit=N/A。原本/過去world非利用。localhost空公開鍵stubは実本人認証ではない。今回game/compile失敗run0。初期のcache bytecode読取はsandbox AccessDenied後、承認済み権限読取で成立（試験FAILではない）。
- Avaritia原物の任意IC2/Botania tag参照と未登録flower/potato loot参照のERRORを原logに保持。起動/胸Flightケースは成立したが、全resource/Infinity Armor能力の互換PASSとはしない。追加mandatory不足なし・新download0。

正式claim：**Avaritia 4.0.3 exact approved artifact Infinity Chestplate Flight SUPPORTED**。実client・全provider同時・別JVM restart・全keepInventory・異常slot・別版/別hash/別fork・Infinity Armor全能力は未保証。次の1作業は利用者の次指示による **queue #8の実行範囲確認**。今回はextensionを完了して停止。RC=NO、REAL2CLIENT=BLOCKED、他の個別gateを維持。可逆クラフト増加は既知許容仕様・バグ修正対象外。


<a id="legacy-existing-skill-parity-audit"></a>
### 14.68 pre-#8 v2.2.5 existing-skill parity audit — 2026-09-29 20:36 JST

**PRE-#8 LEGACY EXISTING-SKILL PARITY AUDIT COMPLETE / PRE-#8 LEGACY SKILL PARITY FIX REQUIRED**。#1–#7 COMPLETE・残2、Avaritia/FE/Mekの限定完了とFlight/P/T PURCHASE READYを維持。**監査→利用者判断→必要な修正→#8→#9**へ順序を更新し、今回は#8/#9 NOT STARTED。

- [全17技能・4層matrix/call-site/判定](LEGACY_SKILL_PARITY_AUDIT.md)。旧commit `59aedf1ccc512c531c73506be38ff9844dbf799b`をmetadata2.2.5/foodhealing・Git blob35件で照合。25 Java、17 toggle群/31段階/効果参照19、[全検索証拠](../build/verification/legacy-parity-20260929-202500/audit/legacy-mechanical-inventory.json)。同梱仕様はv2.2.0表記の履歴で、actualと分けた。
- 採取×4/×6・不壊95%/29⁄30・追撃extra2～9は式が残っても正常購入で到達不可。**LIKELY_UNINTENTIONAL_REGRESSION／fresh構造判断待ち**。migrationのno-auto-acquire/全返還/LegacyV2Backup/exactly onceは別LOCKのまま。
- **ArmorMastery ArmorItem限定はCONFIRMED V3 NARROWING REGRESSION**。標準damageable全般・0/1不変/>1→1を利用者LOCKとして[SPEC§19](SPEC.md#legacy-armor-mastery-parity-lock)へ記録。現在の不壊はtoolにも効く。hurtAndBreak外direct hurtのFH迂回も区別した。修正未実施。
- 旧/現の接続済み経路は**ArmorMastery→FH不壊→vanilla Unbreaking**。[source/Jar14 class・native順序](../build/verification/legacy-parity-20260929-202500/audit/source-jar-static-comparison.json)一致。旧L2専用完全無効hookのcode証拠なし。旧lang説明を現P極意へ付け替えない。
- 炎は燃焼中**全incoming×.70**を維持。early skill、屠殺、food満足感、Root/Heroics、global/SWを個別分類。TaCZ分離・safe loot/food・ownership・iframe復元・高Nutrition廃止等は意図的再設計であり巻戻しなし。
- [後続受入候補](TEST_PLAN.md#legacy-parity-future-acceptance)はNOT RUN。既存tool非適用assert/84PASSを新LOCK適合としない。旧PASS・失敗履歴の削除/書換えなし。
- 必要判断は[監査§6 E](LEGACY_SKILL_PARITY_AUDIT.md#parity-decisions)の**3技能の購入構造/費用**と**不壊の正式標準耐久対象**のみ。ArmorMastery方向は再質問しない。43SP/839SP/maxLevelは不変、変更時のみ再計算。
- 変更前7文書を[backup](../backups/legacy-parity-20260929-202500/before)保全。production/test/resources/build.gradle/config/購入gate/移行/world変更0、build/unit/GameTest/client/server/外部統合0、Security scan0。公開commitのsource取得は承認された読取調査だけ。製品 **350,807 bytes/269 entries/C106B5B67F63821289B0BB51F77E165CB7D1E0684F6BFBE228A8DF3E9851F7BC**、protocol7/schema5不変。

次の1作業は利用者の2判断を記録し、LOCK済みArmorMasteryを含む最小parity fix範囲を確定すること。今回fixを自動開始しない。RC=NO/REAL2CLIENT=BLOCKED、他の個別開始条件を維持。可逆クラフト増加は既知許容・修正対象外。


<a id="legacy-skill-parity-fix-completed"></a>
### 14.69 pre-#8 legacy skill parity fix — 2026-09-29 21:42 JST

**PRE-#8 LEGACY SKILL PARITY FIX COMPLETE**。直前§14.68は20:36の監査履歴として保持。今回の利用者明示決定で判断2問を解決し、4技能だけをLOCK・実装・指定自動回帰した。**#1–#7 COMPLETE／残2／#8・#9 NOT STARTED／RC=NO**。Flight/P/T PURCHASE READY・既存SP保護を維持。

#### 確定範囲・実装

正式値は[SKILL_TREE_SPEC§1](SKILL_TREE_SPEC.md#1-existing-skills-and-confirmed-sp)、詳細は[SPEC§19–20](SPEC.md#legacy-armor-mastery-parity-lock)。Gathering3段階、Unbreaking3段階、Pursuit9段階を同一IDの既存transactionへ接続し、防具の極意を20SP/標準damageable全般へ修正。**実定義再計算：既存group1,025／有限総額1,821（+982）**。他skill費用は本工程で不変・後日の明示調整は可能。

- production Javaは`FoodHealingSkills`の4定義、`DurabilityTransactions`のArmorItem限定解除、`ItemStackMixin`の共通hurt HEAD引数調整のみ。ja_jp/en_usは4技能の説明のみ。Gathering GLM・Pursuit native hurt/guard自体の書換えなし。
- `hurtAndBreak`内Redirectを除去し、共通`hurt(int,RandomSource,ServerPlayer)`で1回調整。Forge Item.damageItem → ArmorMastery → FH Unbreaking → vanilla Unbreaking → damage/breakの順を維持。実変換後ItemStackでhook1箇所、callback/enchant存続、refmapのSRG`m_220157_`を確認。標準入口を迂回するenergy/customcap/直接setDamageValueは対象外。L2全免疫とはしない。
- generic GUI/registered purchase/canonical保存・syncを利用。schema5/protocol7不変。既存v3 Lv1/Spent13/OFFの読込で差額補正0、次Gathering購入だけ5SP追加（Spent18）。v2 Lv16/19/999は全返還・Spent0・取得空・backup永久保持・exactly onceを維持。
- 新GameTests11とunit320を追加し、旧tool5→5を今回LOCKの5→1へ更新。旧移行試験の購入費fixtureも新価格へ合わせ、既存ケース削除0。[全変更差分](../build/verification/legacy-parity-fix-20260929-211107/audit/implementation.diff)。build.gradle/製品Config/他skill・provider・protocol/migration codeは不変。

#### 最終コード/Jarで実行した受入

| 区分 | 結果と証拠 |
|---|---|
| compile/build/unit/check | **PASS**、新parity **320 assertions**＋既存全unit（Flight88等）。[最終log](../build/verification/legacy-parity-fix-20260929-211107/audit/build-final.log)。320は新suite件数で、全unit総数ではない |
| vanilla | **95/95**＝既存84＋追加11、削除0。[log](../build/verification/legacy-parity-fix-20260929-211107/audit/vanilla-final.log) |
| approved TaCZ1.1.7-hotfix2 | **95/95**、Ammo等の共通回帰を保持。[log](../build/verification/legacy-parity-fix-20260929-211107/audit/tacz-final.log) |
| 実L2耐久限定 | **24/24**＝corrosion/erosion各9既存P条件＋各3標準耐久比較。[result](../build/verification/legacy-parity-fix-20260929-211107/l2-focused01/l2-result.json)。cap1/save0/fail10または73。native攻撃維持、P/T効果/条件変更なし |
| 保存・終了 | 最終3serverすべて通常全dimension save→stop→exit0、[process終了・残存0](../build/verification/legacy-parity-fix-20260929-211107/audit/process-final.json)。途中FAIL serverも保存停止し、非zero試験終了は履歴保持 |

coreはForge47.2.0 userdevの最終source。L2はForge47.4.0と**最終配布Jarそのもの**、Hostility2.5.19/Library2.5.3/Complements2.6.1/Curios5.12.0+1.20.1/Patchouli1.20.1-84-FORGE。nested DamageTracker実ロード0.4.4、外側重複配置なし。独立fixture`foodhealing_l2_verification`/20260929.parityを専用initでcompile/reobfし、full133・Trial/FE/Mek/Avaritia/Pam/SW等の外部suiteは再実行していない。主体AUTOMATED、HUMAN INPUT=0、client/Prism起動0、Quit=N/A。既存実clientは当時の限定PASSを保持。

[TEST_PLAN§31](TEST_PLAN.md#legacy-parity-future-acceptance)に受入詳細を集約。native FortuneIIIでbase3→Lv3 18、全段階購入/実outbound canonical一致、7標準item両入口/閾値equality/vanilla enchant/破損、Pursuit1～9/native防御/Player target/死亡/例外finallyを確認。custom登録modded item専用fixtureは既存にないため未実行。汎用hookと実L2からの標準呼出確認を全外部item互換へ拡張しない。GUIはmodel/transaction/codecの自動確認で実画面PASSではない。

#### 途中失敗と修正の記録

- `build-focus01`：sandbox native-platform.dllアクセス拒否、Gradle本体開始前。既存cacheを使う承認済み権限/offline実行へ切替。
- `build-focus02`：fixtureのprivate LootTable overload/BASE_ATTACK名不備でcompile FAIL。実在public overload/BASE_OUTGOING_DAMAGEへ修正。`build-focus03`成功。
- `vanilla-focus01`：旧価格の移行fixture、64耐久rodへ100を与えて破損も起こす確率試験、GameTestのPvP無効の3FAIL。価格fixtureを更新、rodの確率試験は非破損量5に分離（破損は別case）。
- `vanilla-focus02`：Player targetのPvP診断FAILを保全。該当native防御caseだけPvPを有効にしてfinallyで元値へ復元し、最終95/95成立。期待するHP18・hit2・iframe11は変更なし。初回production patch後の追加修正は試験/fixtureのみ、期待値緩和やケース削除なし。

各途中log/process/worldは[run root](../build/verification/legacy-parity-fix-20260929-211107)に保全。read-only bytecode監査側のSRG名称照合/inner class debug差分判定も実物に合わせ修正し、製品修正とは混同しない。

#### 成果物・次工程

開始製品 **350,807 bytes /269 entries /C106B5B67F63821289B0BB51F77E165CB7D1E0684F6BFBE228A8DF3E9851F7BC**、source/test/docsとともに[変更前保全](../backups/legacy-parity-fix-20260929-211107/before)。
最終[foodhealing-3.0.0.jar](../build/libs/foodhealing-3.0.0.jar) **350,858 bytes /269 entries /SHA-256 E24CAC325229A4AC341AB7D715C0A61A8070984B209107CCA11B40B50CCF2B8B**、version3.0.0/modId foodhealing/schema5/protocol7。[最終監査](../build/verification/legacy-parity-fix-20260929-211107/audit/final-jar-audit.json)：metadata/5 Mixin config・全対象class、正しいrefmap、reobf出力一致、外部class/Jar・fixture/GameTest・ExampleMod混入0。配置L2 Jar一致・承認外部原物不変。3 production class（＋debug差分のnested class4）以外の製品classはbyte一致。

Root/True Root/Heroics/P/T/Flight/Ammo/Break Realm/Nutrition/FoodProduction/Pam/SW/provider/migration/protocolは本工程で変更なし。可逆クラフト増加は既知許容仕様・バグ修正対象外。REAL2CLIENT=BLOCKED、他個別開始条件を維持。**次の1作業は、利用者が他skillのSP調整を続けるか、queue #8の実行範囲を指示するのを待つこと。ここでは#8/#9を自動開始しない。**

<a id="skill-cost-rebalance-quarrying-immovable-completed"></a>
### 14.70 pre-#8 skill rebalance + Quarrying / Immovable — COMPLETE

本文更新: **2026-09-29 22:38 JST**。run `skill-rebalance-20260929-220256`、**AUTOMATED TESTED / HUMAN INPUT=0**。今回の明示承認だけで新2skillをscopeへ追加し再frozen。価格/合計正本は[Skill Tree](SKILL_TREE_SPEC.md)、効果正本は[SPEC§21/22](SPEC.md#quarrying-lock)、受入は[TEST_PLAN§32–34](TEST_PLAN.md#quarrying-acceptance)。

| 項目 | 今回の結果 |
|---|---|
| LOCK・合計 | normal1150 / Root70 / Heroics250 / high620 / TaCZ500 = **2590**。repeatable7費用は5/10/10/5/20/20/2、効果値不変 |
| production変更 | registry/ID/BaseStats、DamageEventHandler末尾の独立×.5、QuarryingController/ImmovableMasteryController、FoodHealingMod登録、Gathering GLMのbonus合流、native採掘/矢/爆発の3Mixinとconfig/refmap、ja/en。合計14 production files。generic GUI位置はGathering/Kongo隣 |
| 移行・保護 | 既存取得Lv/SP/Spent/point/toggleの遡及補正0、v2移行コードbyte同一、自動取得0/全額refund/backup/exactly once維持。schema5/protocol7。Flight/Root/Heroics/P/T/旧耐久等の未変更classは開始Jarとbyte一致 |
| Quarrying | BreakSpeed hardness差だけ補正。成功したnative normal harvest内のGLMでFH double1回、iron/copper各.5%、qty1/2/4/6。Silk/Fortune native drop維持、wrong-tool/Creative/取消/Fake/爆発/対象外陰性 |
| native採掘証拠 | 名前付きnative random_sequenceを使いseed64→iron6、seed18→copper6。stone/deepslate各native drop1を保持。通常ServerPlayer.gameMode.destroyBlockを実行、テストからbonusを付与していない |
| Immovable | direct knockback/Mob melee/Player enchant motion packet/矢Punch/爆発server＋client packet。既存vector保持、OFF/unownedはnative追加。numeric源matrix・各DR合成・native HP20→18・direct death/removal陰性・move/jump/gravity/tp陰性 |
| unit/core | 新602 assertions＋既存parity320/Flight88等全unit PASS。compile/build/unit/check PASS。**既存95削除0＋新14＝vanilla109/109、TaCZ109/109**。coreはuserdev最終source、実clientではない |
| 実L2 focused | 承認Hostility2.5.19/Library2.5.3/Complements2.6.1・Forge47.4.0、最終配布Jar直接 **8/8**。HighDR10SP exact/repeated/stale/不足/long境界6、native poison攻撃OFF3/ON1.5の2。poison/source/canonical不変、P/T効果や前回耐久24を再試験しない |
| 保存・終了 | 最終vanilla/TaCZ/L2全serverのnormal save/stop/exit0。過去world/原本/Prism/clientは起動0。外部download0 |
| 成果物 | [foodhealing-3.0.0.jar](../build/libs/foodhealing-3.0.0.jar) **360,970 bytes / 275 entries / SHA-256 36A79E98C8E854DA79B82811EBD5DD57FE01CF4F7EDFC93296031DDFD8A265BE**。MOD ID foodhealing/version3.0.0、5Mixin configs、既存refmap保持＋新3。配布にfixture/GameTest/外部class/Jar/ExampleMod混入0 |

[最終監査](../build/verification/skill-rebalance-20260929-220256/audit/final-jar-audit.json) / [source差分](../build/verification/skill-rebalance-20260929-220256/audit/implementation.diff) / [build](../build/verification/skill-rebalance-20260929-220256/audit/build-final.log) / [unit](../build/verification/skill-rebalance-20260929-220256/audit/unit-final.log) / [vanilla109](../build/verification/skill-rebalance-20260929-220256/audit/vanilla-final.log) / [TaCZ109](../build/verification/skill-rebalance-20260929-220256/audit/tacz-final.log) / [L2限定8](../build/verification/skill-rebalance-20260929-220256/l2-focused01/l2-result.json)。
開始原物は350,858 bytes/269 entries/E24CAC325229A4AC341AB7D715C0A61A8070984B209107CCA11B40B50CCF2B8B。[変更前274file backup](../backups/skill-rebalance-20260929-220256/before)を保持。.git不在につきfilesystem差分でありgit diff確認とはしない。

**途中FAILを保全**：unit01は旧5SP準備が新DR10未満。compile02/03は新fixtureのAPI参照/ラムダ捕捉不備。vanilla01はclone接続未設定とnative RNG源不一致、vanilla02はclone解消・RNGのみ残。実tableの名前付きrandom_sequenceへ合わせてvanilla03の109/109成立、対象外MOD/爆発context陰性追加後に最終vanilla/TaCZ109を実施。確率/期待値/ケース削除なし、試験を通すためのproduction再変更なし。原logは同run audit内へ保存。

**境界**：standard Minecraft/Forge knockback対応。外部custom velocity/teleport/cap/packetはNOT GUARANTEED。実client画面/全MOD移動・Elytra/vehicle/portal等の全動的網羅は未実施で、既存class不変＋限定移動陰性を証拠とする。通常migrationの既存unitを維持したが原本world/別JVM移行suiteを今回再実行しない。Trial/Invader/FE6/TimeStop/Mek/Avaritia/Pam/SW全外部suite・L2全133・旧実clientは再実行0。build taskが既存fixtureをcompile/reobfしたことと、それらの実統合suite実行は別。

**次の1作業**：次の利用者指示を待つ。#1–#7 COMPLETE／#8・#9 NOT STARTED／RC=NO、Flight/P/T PURCHASE READY・SP保護を維持。Break Realm expansion/Bulwark/試作型機関弩/FOURTH BOOTへ進まない。食料生産の極意による可逆クラフト増加は既知許容仕様・バグ修正対象外、死亡drop/replay等の不正重複は別。


<a id="player-facing-localization-cleanup"></a>
### 14.71 pre-#8 documentation / localization / player-facing language — COMPLETE

本文更新: **2026-09-29 23:31 JST**。run `localization-20260929-230944`。AUTOMATEDのみ、HUMAN INPUT=0。用語/公開時の要求は[SPEC§23](SPEC.md#player-facing-terminology-release)、受入は[TEST_PLAN§35](TEST_PLAN.md#localization-message-acceptance)を正本とする。§14.69–70以前の価格・件数・当時の失敗は履歴として保全。

| 項目 | 完了内容・証拠の範囲 |
|---|---|
| 日本語/英語 | 全26skill・7base-statの説明を一般player向けに変更。ja更新108key、en更新101key（各新9keyを含む）。旧互換keyも維持して現行の説明へ揃え、旧Lv17防具完全免疫等の誤った説明を撤去。Rootの時間/条件、Heroics各Lv、Ammo全10段階と過熱を日英同じ数値で明示 |
| 食技 | lang中の旧「食義」9→0。現在の設定UI説明2→0（合計current表示11→0）。GUI/HUD/level/count/管理command/level-upを更新。内部Shokugi class/method/key/resource IDはrename0。v2移行入力の廃止済みConfig説明と内部コメント、過去証拠の旧表記は保全 |
| 成功/失敗 | 正式transaction成功後だけ `%sを習得した！` / `%sがLv%sになった！` / `%sが上昇した！`、英語Learned/reached Lv./increased。実canonical levelと翻訳名を渡す。message helperは表示専用、SP処理/取得/toggle/同期/encode/decode変更0。失敗は理由別、登録packet実送信で結果1通・失敗時成功0を確認 |
| 名称/表示補正 | generic GUI/commandが参照する採石・不動の`.name`を追加（既存key削除0）。commandの通常一覧/詳細/toggle成功は翻訳名を使用し内部IDを本文へ表示しない。command引数/クリック操作は既存IDのまま。Configは現在UI説明2文字列だけ、key/default/range/移行入力/保存ファイル変更0 |
| 数値/意味 | finite2590、1150/70/250/620/500、repeatable5/10/10/5/20/20/2、効果倍率/順序不変。HighDRは実装が攻撃元MODで限定しないため「L2/Auto Leveling導入時に上げられる追加軽減」と正確に説明。旧価格の記載をゲーム側の値に合わせただけ |
| 自動検証 | localization unit **1,094 assertions**＋既存全unit、compileJava/build/foodHealingUnitTest/check PASS。vanilla/TaCZ各 **111/111**＝既存109保持＋登録packetのskill/base-message2、削除0。初回/上位/不足/stale/duplicate/maxed、翻訳名/レベル/結果1通/canonical syncを実測。GUI描画やclientスクリーンショットは未実施 |
| 最終成果物 | [foodhealing-3.0.0.jar](../build/libs/foodhealing-3.0.0.jar) **362,505 bytes / 277 entries / SHA-256 D671E9A0E039CA62AF6CFDBEF6D160082097FF25FDF0823260469D9CE2062D58**。version3.0.0/modId foodhealing/schema5/protocol7。5Mixin config/refmap不変、reobf一致、外部Jar/class・fixture/GameTest・ExampleMod混入0 |
| 不変/終了 | 表示変更対象以外の製品classは開始Jarとbyte一致。controller/cost/gate/SP/migration/network登録・schema変更0。最終2serverは通常save/stop/exit0、process終了。中間vanilla/TaCZ111も正常終了し記録保持。Trial/Invader/fullL2/FE/TimeStop/Mek/Avaritia/Pam/SWの再実行0。Prism/実client/旧world起動0 |

[最終監査](../build/verification/localization-20260929-230944/audit/final-jar-audit.json) / [source差分](../build/verification/localization-20260929-230944/audit/implementation.diff) / [build・unit・check](../build/verification/localization-20260929-230944/audit/build-final.log) / [vanilla111](../build/verification/localization-20260929-230944/audit/vanilla-final2.log) / [TaCZ111](../build/verification/localization-20260929-230944/audit/tacz-final2.log) / [終了process](../build/verification/localization-20260929-230944/audit/process-final.json)。開始製品 **360,970 bytes /275 entries /36A79E98C8E854DA79B82811EBD5DD57FE01CF4F7EDFC93296031DDFD8A265BE** と275fileを[backup](../backups/localization-20260929-230944/before)へ保全。.git不在、filesystem差分で検査。

**途中結果**：focus01は日英数値検査が日本語「10分の1」と英語divide by10の表記差を検出。focus02はDoubles/Triplesの数値文字欠如を検出。意味/期待値を変えず英語を1/10・2・3へ揃えfocus03成功。中間sourceでcore111/111後、現在のConfig UI説明2箇所・command本文を最終整理し、build-finalと新world final2各111/111で再確認。途中logは消去せず、最終結果と分離。

**現行文書整理**：StatusのAmmo各5→50SP、今回95という古い集計を前工程109/今回111へ接続、旧SP調整待ちを完了へ。TEST§16各50SP、Skill Tree§11 TaCZ基礎2SPと§13新2ID、Audit A2の2000単位を訂正。Skill Treeに残るTrue Root未実client/P/T購入停止の現在表記は、既存完了証拠へ更新。旧§31の95・1,025/1,821、LEGACY監査§1–7、日付付き旧5SP/食義/STOP/FAILを履歴維持。候補の分類は[文書監査](../build/verification/localization-20260929-230944/audit/docs-classification.json)。

**次の1作業**：**READY FOR USER AUTHORIZATION OF #8**。#1–#7 COMPLETE／#8・#9 NOT STARTED／RC=NO。Flight/P/T PURCHASE READY・SP保護、REAL2CLIENT=BLOCKED、広域TimeStop未証明・個別開始条件を維持。GitHub/commit/push/PR/release・正式Jar名変更・完全仕様書作成は今回は0。食料生産の極意による可逆クラフト増加は既知許容仕様・バグ修正対象外。


<a id="release-command-verification-cleanup"></a>
### 14.72 pre-#8 release cleanup — LEGACY COMMAND / VERIFICATION ARTIFACT COMPLETE

本文更新: **2026-09-30 00:07 JST**。run `release-cleanup-20260929-234805`。AUTOMATEDのみ、HUMAN INPUT=0、Prism/client起動0。本節はcleanup差分の受入であり固定queue #8ではない。開始Jar362,505 bytes/277 entries/D671E9A0E039CA62AF6CFDBEF6D160082097FF25FDF0823260469D9CE2062D58を現物照合し、[278file backup](../backups/release-cleanup-20260929-234805/before)へ保全。.git不在につきfilesystem差分。

#### 全production command分類

`RegisterCommandsEvent → FoodHealingCommands.onRegisterCommands`だけが登録入口。構造node `/foodhealing`、`/foodhealing syokugi`は実行処理なし。以下はその下の**全9実行経路**で、prefixは `/foodhealing syokugi `。全経路は実行者本人だけ（target引数なし、consoleはplayer無しで拒否）。旧基準commit `59aedf1ccc512c531c73506be38ff9844dbf799b`のcached sourceをgit blob SHA1とSHA256の既存identityへ再照合。旧7経路→開始時9→最終6。

| path（引数） | permission | 分類/処置 | canonical変更・同期 | 現行release用途 / GUIとの関係 | 専用翻訳・旧由来 |
|---|---:|---|---|---|---|
| `level` | 0 | **B KEEP** | 読取のみ、同期0 | server側の食技Lv診断/管理値のreadback。GUIは同期された表示 | `command.foodhealing.level`、v2由来 |
| `count` | 0 | **B KEEP** | count/設定threshold読取、同期0 | server側Nutrition進行度の診断。GUIのprogressと照合可 | `.count`、v2由来（旧固定1000→現configured threshold） |
| `toggle <skillId>` | 0 | **C REMOVE** | 旧toggle/Root共通処理＋同期 | 通常本人操作の旧chat GUIのみ。remote target/admin override/repair機能・正式accessibility契約なし。現GUI→登録ToggleSkillPacketを保持 | `.toggle.*`・state、v2の表示名/閾値経路の後継 |
| `skill` | 0 | **C REMOVE** | 読取/旧detailへのchat link、同期0 | GUIの取得一覧へ置換済み | `.skills.*`、v2由来 |
| `skill <skillId>` | 0 | **C REMOVE** | 読取/旧toggleへのchat link、同期0 | GUIの詳細/説明/toggleへ置換済み | `.detail.*`・state、v2由来 |
| `setlevel <level>` | 2 | **A KEEP** | ShokugiLevelだけ変更＋即時同期 | 食技Lvの管理修正。現在も数値/倍率の独立canonical。SP付与・技能自動取得をしない、GUIにraw上書きなし | `.setlevel.success`、v2 int0..1000→現long |
| `setcount <count>` | 2 | **B KEEP** | EatCountだけ変更＋即時同期 | 管理者による進行countの修正。移行pending/error解除・SP再計算は行わない、GUIにraw上書きなし | `.setcount.success`、v2 int0..1000000→現long |
| `setskillpoint <amount>` | 2 | **A KEEP** | 未使用SPだけ変更＋即時同期 | 管理用credit設定。使用済/取得/基礎値を変えない | `.setskillpoint.success`、v3追加 |
| `addskillpoint <amount>` | 2 | **A KEEP** | 未使用SPだけ加算＋同期、overflow拒否時0 | 管理用追加credit。Long overflowで部分書込/同期0 | `.addskillpoint.success/.overflow`、v3追加 |

保持mutation引数は従来どおりlong **0..Long.MAX_VALUE**。setcountはraw管理値であり自動threshold正規化を追加しない。CのStringArg・chat Click/Hover・専用メソッド/importだけ削除。A3/B3保持、C3削除、**D0/E0**。FoodHealingCommandsは存続。GUI/packetから旧command呼出し0、Root/能力/alias/移行処理は不変。`FoodHealingSkillIds`の旧日本語save aliasは削除しない。全12項目（call-site/test/引数/旧由来含む）の機械棚卸しは[JSON](../build/verification/release-cleanup-20260929-234805/audit/command-inventory.json)。

#### 検証残存・翻訳の分類

- 指定manual/READY/run表示は製品source/resources/generated/Jarで**before0→after0**。外部の検証補助でありHUD削除patchは不要。正式command treeにprepare/inspect/hit/seal/arm等0。FE uniform compat/observer/reconnect補助・test metadata・nested Jar・外部class・GameTest/fixture/ExampleMod混入0。既存dev/test assetsと失敗world/historyは削除0。
- raw名称に`Observer`を持つ製品classは**2**存在する：`CountObserver` / `UseObserver`はTimeStopのnative count/use/deserializeをownershipへ接続する正式Mixinであり、検証observerではない。`InitialStart`の`initial observer`例外もfaultを記録する内部安全診断。変更0/byte一致。一般語まで消して0を装わない。英語migration拒否文の`verification`も正規メッセージとして保持。
- ja/enそれぞれ**184→130key、54削除**（旧title23/desc17＋旧chat command12＋state2）。保持130keyの値は全て前工程と同一。現行26skill/7statのname/description・食技/Shokugi・習得/上昇表示/数値/書式一致・missing key0。詳細は[削除key](../build/verification/release-cleanup-20260929-234805/audit/removed-translation-keys.json)。save alias/resource ID変更0。
- literal参照のない旧汎用key4件（itemGroup.food_healing_tab、purchase.failed、purchase_blocked、shokugi_levelup_skill）は削除command専用ではないため保持/分類。item/effect/skill/statの動的参照と混同しない。既存test-food等は指定範囲外で変更0。

#### 自動結果・修正・終了

| 項目 | 結果 |
|---|---|
| command dispatcher | 実登録tree6経路、permission0/1/2、suggestionにobsolete/verification0、parse/execute拒否、SP/toggle/Root予約/全canonical不変。保持4mutationの権限・負数/非数/overflow/余分target・console拒否、本人/他player分離。level/countはserver実値＋threshold出力/同期0、setterはcanonical一致のsync1 |
| 既存command試験 | Flight/TrueRoot/P/Tの旧toggle成功を、未登録負対照＋登録GUI packetへ置換。取得/SP/親子設定/保護条件/active期限/同期の元期待値を維持。既存111ケースは削除0（TrueRoot1件のみ目的に合わせ改名）＋新2＝**113**。現行Trial fixtureの親toggleも同方式へ保守しcompile/reobfのみ、49ケース再実行0。旧case ID/攻撃期待値は保持 |
| build/unit | ローカルGradle8.1.1・既存cache・offline。compileJava/foodHealingUnitTest/build/check **PASS**。localization **815 assertions**＝旧1094−削除key依存270−旧call-site14＋未残存検査5。全required coverage維持。cost602/parity320/Flight88ほか既存全unitもPASS |
| core | 最終production/core sourceで **vanilla113/113・TaCZ113/113**。TaCZ1.1.7-hotfix2実ロード。認証はローカルempty keysに限定、real TCP/画面試験ではない。外部全suiteの再実行0 |
| 初回FAIL保全 | vanilla初回：動的組立の旧P/T command3件＋爆発fixture1件FAIL。前者は正式negative/GUI packetへ置換。後者は固定y100の周辺保存32点が全石。[readonly地形証拠](../build/verification/release-cleanup-20260929-234805/audit/failed-blast-site.json)。新fixtureはy250空間を用意しnative exposure=1を事前assert、元HP/vector/map/packet期待値は不変。初回の実exposure値自体は未記録で事後推定と区別 |
| 正常終了 | 最終vanilla/TaCZは通常save・全dimension保存・stop・GameTest shutdown・Gradle exit0。初回FAIL serverも通常保存停止/親process終了（test exit1）を保全。最後のCIMでJava/javaw0。既存Prism PID26748は操作/終了せず、本工程の起動0 |
| final Jar | [foodhealing-3.0.0.jar](../build/libs/foodhealing-3.0.0.jar) **358,640 bytes / 277 entries / SHA-256 4DF4954EC7082DA0276A0614EAEB871971F4A69538983EEED2F688BA4E3B5DEC**。version3.0.0/modId foodhealing/schema5/protocol7。5Mixin configs/refmap byte不変、reobf一致。差分entryはcommand.class・ja/en・Manifestのみ、**gameplay class意図しない差分0** |

[最終監査](../build/verification/release-cleanup-20260929-234805/audit/final-jar-audit.json) / [全command棚卸し](../build/verification/release-cleanup-20260929-234805/audit/command-inventory.json) / [source差分](../build/verification/release-cleanup-20260929-234805/audit/implementation.diff) / [build・unit・check](../build/verification/release-cleanup-20260929-234805/audit/build-final3.log) / [vanilla113](../build/verification/release-cleanup-20260929-234805/audit/vanilla-final2.log) / [TaCZ113](../build/verification/release-cleanup-20260929-234805/audit/tacz-final.log) / [終了process](../build/verification/release-cleanup-20260929-234805/audit/process-final.json)。

製品変更はcommand1file＋lang2fileのみ。testはcore5file＋localization unit1＋既存Trial fixture1。controller/compat/cost/gate/SP挙動/Config/migration/network/内部Shokugi/build.gradleは不変。finite2590＝1150/70/250/620/500、repeatable5/10/10/5/20/20/2維持。coreはuserdev、最終配布Jarの実client未実行。追加全suiteをPASSへ転記しない。

**PRE-#8 PRODUCT CLEANUP COMPLETE / READY FOR USER AUTHORIZATION OF #8**。#8実clientの未実施受入は[TEST_PLAN§36](TEST_PLAN.md#release-command-verification-acceptance)へ集約。#1–#7 COMPLETE、#8/#9 NOT STARTED、RC=NO、Flight/P/T PURCHASE READY・SP保護、REAL2CLIENT=BLOCKED、広域TimeStop/vehicle・他個別開始gate維持。GitHub/commit/push/PR/release・Jar改名・完全仕様書作成0。食料生産の可逆クラフト増加は既知許容仕様・バグ修正対象外。**次の1作業は#8の利用者承認待ち。本工程では開始しない。**


<a id="queue8-final-release-verification"></a>
### 14.73 Queue #8 Final Release Verification — BLOCKED

本文更新: **2026-09-30 01:23 JST**。run `release-final-client-20260930-002426`。利用者の正式#8承認に基づく実行でありpre-#8ではない。**#1–#7 COMPLETE / #8 BLOCKED — SUPPORTED FANTASY ENDING CLIENT STARTUP / #9 NOT STARTED / RC=NO / NOT READY FOR RC DECISION**。Profile A・自動回帰・Jar監査は完了、Profile Bはタイトル前crash。過去の限定FE gameplay/client PASSを取り消さず、正式構成のstartup受入と区別する。

#### 開始・修正・最終build

開始Jarは指定どおり358,640 bytes/277 entries、SHA-256 `4DF4954EC7082DA0276A0614EAEB871971F4A69538983EEED2F688BA4E3B5DEC`。285fileを[開始前backup](../backups/release-final-client-20260930-002426/before)・[hash一覧](../build/verification/release-final-client-20260930-002426/audit/before.json)へ保全。.gitなし、git diff確認という記録はしない。

初回Profile Aで、TaCZ不在なのに**TaCZ基礎攻撃力を購入できSP2を消費**する製品不具合を実画面/保存で検出（SP984→982、spent16→18、point1）。既存Skill Tree§11・今回受入§52に従い、`FoodHealingBaseStats`のserver購入と`FoodHealingScreen`の表示を共通のpresence判定へ接続し、`PurchaseMessages`とja/en各1keyへ専用理由を追加。既存TaCZ版/Ammo gate・効果倍率・2SP・既取得point・他skill/P/T/Flight readyには変更なし。原FAILのworld/画面/log/NBTを保全し通常保存/終了後、新instance/world A2で最初から確認した。

新GameTest `taczBasePurchaseOptionalBoundaryAndRegisteredSync` は実ModListの不在/承認TaCZ導入を分岐し、登録packet・canonical同期・重複/不正世代・不足/overflow・他player保全を検査。既存113件削除0＋1＝114。初回fixed vanillaは第二fixture login通知を購入返信に数えた**fixture FAIL**（113 PASS/1 FAIL）。測定前のjoin通知だけdrainし、期待値・応答1通条件を維持して再実行。旧単体のTaCZ購入陽性は依存ありpredicateを明示し、依存なし拒否/完全canonical不変を追加した。これらの途中FAILは[原log](../build/verification/release-final-client-20260930-002426/audit/vanilla-fixed.log)・[初回client log](../build/verification/release-final-client-20260930-002426/audit/failed-A-latest.log)へ保存。

| 最終実行 | 結果・範囲 |
|---|---|
| compileJava / foodHealingUnitTest / build / check | **全PASS、exit0、46 tasks executed**。local Gradle8.1.1、既存cache、`--offline --no-daemon --rerun-tasks`。正確なargvは[process記録](../build/verification/release-final-client-20260930-002426/audit/final-build-fixed2-process.json)。build.gradle/gradle.properties/AGENTS不変。新規dependency/downloadなし |
| unit | localization **819**（815＋新理由key日英4assertions）、cost602、parity320、Flight88、Nutrition93、Mastery購入153、OPEN143、SW47、P150/T53、TimeStop9、data境界5000、Ammo10level×10000等、既存全unit PASS |
| 最終vanilla / 承認TaCZ1.1.7-hotfix2 | **114/114 / 114/114**。最終sourceのuserdev GameTest、同じsourceから生成した配布Jarの実clientは下記A2/B2。coreを配布Jar直接の全外部統合と表現しない |
| 保全・終了 | 初回build/core、修正build、fixture FAILを消さず保全。3 build processと5 GameTest processは終了（FAIL1はexit1、他exit0）。5serverすべて通常save/stop logあり、最終2serverは全dimensions保存→停止→exit0 |

#### external evidenceの影響判定

[class/関連resource/refmap照合](../build/verification/release-final-client-20260930-002426/audit/external-impact-final.json)は各証拠Jarとの選択経路比較。旧Jar全体や推移的な共通core全てがbyte同一という意味ではない。#8開始Jar→最終Jarのclass差分はBaseStats/GUI/Messageの3本体＋innerだけ。全compat/Mixin/controller/packet schemaはbyte同一。今回の影響はTaCZ基礎購入入口であり、実導入/不在の新GameTestと最終core114、A2日英GUIで確認した。

| 保持する既存証拠 | 最後の該当PASSとの照合・再実行不要の根拠 |
|---|---|
| L2 133、Cube49、Invader86 | 各限定Adapter/controller/Mixin・該当config/refmapはbyte同一。後続の共通耐久/SP/不動変更には実L2焦点24/8があり、その経路も同一。今回は各full suite再実行0 |
| FE6 63＋補足8（70 unique）、TimeStop #2限定client | FE6の7 class、TimeStopの36 classと該当Mixin/refmapは同一。過去clientのshader/login検証補助入り条件を保全し、今回helperなしstartup PASSへ転記しない |
| exact Flight FE/EL18＋診断4、Mek28、Avaritia23＋EL2 | 旧FE/EL・Mekからの差分は後続provider追加/registry。後続Mek28/Avaritia23＋EL2で検証済み、現行11 Flight classは最後のAvaritia証拠と同一。native predicate・policy/controllerを今回変更していない |
| Pam present150 / absent146 | harvest/crafting等9 class・全recipe resource同一。既知Food Core1.0.5 exact3 ERRORの非blocking受入§14.62を維持 |
| SuperbWarfare17 native＋3 synthetic、TaCZ別suite/Ammo | SW2互換class、TaCZ10 Adapter/Mixin等は同一。TaCZ比較baselineは保存済みexpanded-L2工程の製品Jarであり、そのL2実行をTaCZ試験と混同しない。Ammo/射撃/damage不変、今回TaCZ core114は実ロードして実行 |

これらは **PRIOR VERIFIED / CURRENT ROUTE BYTE-IDENTICAL（上記の後続変更履歴を除く）**。今回full external suite再実行0、最終配布Jarで新たに133/49/86等がPASSしたとは書かない。実際に追加した外部client検証はProfile Bで、結果はFAIL。

#### Profile A2 — 最終配布Jarの実client

MC1.20.1 / Forge47.4.0 / Microsoft OpenJDK17.0.15 / Prism10.0.5 / 正常認証Leva9846。`FHR_RELEASE_A2_20260930-002426`、新規`Release-A2-20260930-002426`、正式modsは最終製品1本だけ（loader表示MC/Forge/FHの3）。新規Survival/Normal/cheats/Superflat・structures OFF、生成前GUIで自然湧き/自然回復OFF。HP20/20、無関係actorなし。既存worldや初回FAIL worldを流用しない。**COMPUTER USE操作＋AUTOMATED readonly保存照合、HUMAN INPUT=0**。資格情報ファイル読取/コピーなし。

| 実画面/保存受入 | 実測・判定 |
|---|---|
| title / 警告 / HUD | タイトル通常到達、操作要求のmetadata/helper警告画面なし。numeric HP20/20とcount0/2000が読める。verification HUD/run帯なし。初回のvanilla WASD tutorial toastは通常ゲーム表示であり検証HUDではない |
| command / 入口 | `/foodhealing syokugi `実suggestion6：level/count/setlevel/setcount/setskillpoint/addskillpoint。旧skill/toggle・prepare/inspect/hit/seal/armなし。通常inventory→既定SでGUI、level/count/progress/SP/取得/toggle/stat/理由へ到達 |
| 日本語 | **26skill＋7stat全件**を選択、名称/説明/費用/数値/理由を確認。raw key/旧食義/旧名なし。耐火Lv1、満足感Lv1→2、基礎防御力1をGUI取得。チャット「耐火の心得を習得した！」「満足感がLv2になった！」「基礎防御力が上昇した！」を記録。耐火OFF |
| ja→通常保存・再読込1回 | admin初期SP1000のみ。初回保存SP984/spent16、耐火1OFF・満足感2ON・defense1、HP20/schema5/pending0。00:58:36全dimensions保存。正式Language UIでEnglishへ変更後、同world通常再読込1回でGUI/HUD/値保持 |
| English | **26skill＋7stat全件**。Shokugi/raw keyなし、jaと効果/数値一致。Water and Night Vision Lv1、Gathering Lv1→2、Base Defense1→2をGUI取得。「Learned Water and Night Vision Mastery!」「Gathering Mastery reached Lv. 2!」「Base Defense increased!」を実chat確認 |
| optional理由 | 日英ともAmmo/TaCZ基礎がvisible/locked、クリックでSP/point変化0。基礎「TaCZ導入時のみ利用可能。SPは消費されません。」/「Available only with TaCZ installed. No SP was spent.」。通常購入/refusalの画面とserver保存canonicalを照合、client内部cap直接observerは使用していない |
| 最終保存・終了 | SP968/spent32、defense2、耐火1OFF・水中暗視1ON・満足感2ON・採取2ON、TaCZ基礎point0、食技Lv/count0、HP20をreadonly確認。01:07:04全dimensions保存→title→01:07:30 Quit/Stopping!、client PID37632終了。**REAL CLIENT LIMITED PASS** |

画面証拠は[events](../build/verification/release-final-client-20260930-002426/audit/screens/events.jsonl)のA2-ja-01..26/A2-en-01..26・stat01..07、success/refusal/reload/final-titleと対応するPNG。保存は[A2初回](../build/verification/release-final-client-20260930-002426/audit/A2-first-save.json)/[A2最終](../build/verification/release-final-client-20260930-002426/audit/A2-final-save.json)、logは[A2](../build/verification/release-final-client-20260930-002426/audit/A2-latest.log)。両極意/Flightの効果を今回実画面で再証明したとはしない。

#### Profile B2 — 正式構成の起動FAIL

`FHR_RELEASE_B2_20260930-002426`、同MC/Forge/Java/Prism・同最終製品Jar。承認原物FE2.7.20/EndingLibrary2.1.19fix＋Curios5.14.1+1.20.1 / GeckoLib4.8.2 / Iron's Spellbooks3.16.3 / Iron's Lib2.1.0。各版・size・SHA-256・実配置先は[入力manifest](../build/verification/release-final-client-20260930-002426/audit/client-instances-fixed.json)のB2。FE SHA `E32FD4BA2E48FCF2C84F71AA07155C63D6BFA2178BB840899307DA0DA5E5D141`、EL SHA `0E29AF51DABD4E0EA8A315C1886E61F3ABEE62045F196EB6913D7F4DBA6CDC34`。nested PlayerAnimator等は重複配置なし。shader compatは正式配布LOCKがないため**未配置**、observer/fixture/旧helperも0。

01:08:13、Fantasy Ending shader登録中に **`Uniform cannot be cast to MUniform`**、`MShaderInstance.getUniform:118`→constructor29→CosmicShaderInstance16→CosmicItemShaders50→ClientHandler425。**title未到達 / startup FAIL**。警告画面の正常通過/不在はUNVERIFIEDで、log WARNだけの問題に縮小しない。Food Healingはloader DONE、例外の直接発生箇所は外部FE。完全な原因再監査はせず旧§14.17の同系統失敗と分けて今回実再現を記録する。

world作成/入場0、保存対象worldなし。通常Quitはcrashのため**NOT POSSIBLE**。Prism画面exit−1、Minecraft process終了を確認。外部Jar改変/新patch正式追加/依存版変更/再起動による回避なし。利用者§61に従い **#8 BLOCKED — SUPPORTED FANTASY ENDING CLIENT STARTUP**。 [crash](../build/verification/release-final-client-20260930-002426/audit/B2-crash.txt) / [log](../build/verification/release-final-client-20260930-002426/audit/B2-latest.log) / [exit−1画面](../build/verification/release-final-client-20260930-002426/audit/screens/204-B2-exitcode.png)。

#### 最終成果物・gate・次の1作業

[foodhealing-3.0.0.jar](../build/libs/foodhealing-3.0.0.jar) **359,310 bytes / 277 entries / SHA-256 DF9F8B0736DC39EBDD2F1DEBA3D15A8348AEE262C85AC9B79F52F1CC62D603BB**。A2/B2配置・reobfJar/output・archiveとhash一致。modId foodhealing/version3.0.0/javafml47、Forge[47,)/MC[1.20.1,1.21)、pack15、5Mixin/refmap実class解決/reobf PASS。外部Jar/class・fixture/GameTest・ExampleMod・検証HUD/helper/FE patch混入0。正式CountObserver/UseObserverの2本は保全。command6、finite2590＝1150/70/250/620/500、repeatable5/10/10/5/20/20/2、schema5/protocol7不変。原物/製品Config/購入readiness/SP保護・Food Production可逆クラフト許容仕様不変。source最終hash一覧を監査rootへ保存するが、#8全PASS済みのrelease freezeとは扱わない。

release blocker全11分類は[AUDIT_REPORT F.1](AUDIT_REPORT.md#queue8-release-blocker-matrix)。今回Minecraft client/server/Gradleは全て終了、既存Prism/無関係processは強制終了しない。Security scanなし。GitHub/commit/push/PR/release/正式Jar名変更/完全仕様書作成0。

**次の1作業＝Profile BのFE shader起動失敗に対する正式配布構成/修正方針の利用者指示待ち。** 検証専用patchを自動的にrelease依存へ昇格しない。A2/最終自動回帰の完了を残件へ戻さず、変更影響が出た範囲のみ次承認で再確認する。#9は開始しない。Flight/P/T PURCHASE READY、REAL2CLIENT=BLOCKED、広域TimeStop未証明・vehicle未LOCK・他個別開始条件を維持。

[最終Jar監査](../build/verification/release-final-client-20260930-002426/audit/final-jar-audit.json) / [source差分](../build/verification/release-final-client-20260930-002426/audit/implementation.diff) / [build・全unit・check](../build/verification/release-final-client-20260930-002426/audit/final-build-fixed2.log) / [vanilla114](../build/verification/release-final-client-20260930-002426/audit/vanilla-fixed2.log) / [TaCZ114](../build/verification/release-final-client-20260930-002426/audit/tacz-fixed.log) / [外部影響照合](../build/verification/release-final-client-20260930-002426/audit/external-impact-final.json) / [実画面記録](../build/verification/release-final-client-20260930-002426/audit/screens/events.jsonl) / [A保存](../build/verification/release-final-client-20260930-002426/audit/A2-final-save.json) / [Bクラッシュ](../build/verification/release-final-client-20260930-002426/audit/B2-crash.txt) / [終了process](../build/verification/release-final-client-20260930-002426/audit/process-final.json)


<a id="queue8-fe-shader-blocker-resolution"></a>
### 14.74 Queue #8 FE Shader Blocker Resolution — BUILT-IN / A3・B3 COMPLETE

本文更新: **2026-09-30 07:19 JST**。run `20260930-065113`、**#8 COMPLETE / #1–#8 COMPLETE / #9 NOT STARTED / RC=NO / READY FOR RC DECISION**。今回の明示承認はFE2.7.20 shader startup blockerの正式内蔵と最終受入まで。§14.16–14.19、§14.73の初回A/fixture FAIL・B2 startup FAILは当時の証拠として保全。本節が現在の#8結果であり、過去節のSTOP/次作業を再開しない。

#### 製品変更前照合と昇格境界

開始Jar **359,310 bytes / 277 entries / DF9F8B0736DC39EBDD2F1DEBA3D15A8348AEE262C85AC9B79F52F1CC62D603BB** を現物照合し、[変更前backup](../backups/fe-shader-production-20260930-065113/before)へ保全。.gitなし、差分はhash/ZIP entry/difflib比較である。旧§14.19 patch（14,270 bytes、SHA-256 `BD2322818FDC65F4F20FD8332200CD6AED02F00487E3E1D8868D2865AD4B58D0`）のsource・dev/reobf class・config/plugin/refmap・STATIC-S/STARTUP-S1/VISUAL-V1を再照合。**製品編集前PRECHECK10/10 PASS**：実call1、current target/FE原物一致、既存Mixin非重複、optional/client隔離、負gate、observer非依存、asset編集不要。

| 変更production file（7） | 役割 |
|---|---|
| `compat/feuniform/FeUniformGatePlugin.java` | early loaderの実metadata/原Jar SHA、client/exact gate、適用前後ASM契約。判定logは一度だけDEBUG |
| `compat/feuniform/PatchContract.java` | MC1.20.1 / Forge47.4.0 / FE2.7.20 exact SHA、receiver/resource完全一致、呼出数/初期化順序/private性検査 |
| `compat/feuniform/NativeUniformParserBridge.java` | FH所有interface。common→FE typed hard referenceなし |
| `compat/feuniform/mixin/ShaderConstructorParseMixin.java` | ShaderInstance ResourceLocation constructorのprivate uniform parser call1だけredirect。require/expect/allow=1、priority1000 |
| `compat/feuniform/mixin/MShaderParserBridgeMixin.java` | string target＋@Pseudo、FE本来のpublic/native parserへ1回delegate |
| `src/main/resources/foodhealing.feuniform.mixins.json` | 専用client list2、専用plugin、既存refmap。旧5configを変更せず6本目 |
| `build.gradle` | 上記config登録1行だけ。依存・既存task/test設定不変 |

Java pathのprefixは `src/main/java/com/leva/foodhealing/`。package/owner/prefixとlog level等を除いて旧5sourceの意味は一致（target/descriptor/ordinal/priority/gate/allowlist/delegate/fallback/exception policy）。旧MOD entrypoint/observer/GL測定/verification画面/特別rendererは昇格しない。

正式gate/allowlistの正本は[Compatibility §19](COMPATIBILITY_POLICY.md#fe-uniform-built-in)。FE SHA `E32FD4BA2E48FCF2C84F71AA07155C63D6BFA2178BB840899307DA0DA5E5D141`。CLIENTかつMC1.20.1/Forge47.4.0/FE2.7.20/原物hash一致のみ介入。EL exactは試験構成の照合値であり新gateにはしない。親private `m_173354_(JsonElement)V` 本体/visibilityは維持し、対象5組だけnative FE parser/factoryへ、他shaderは元private parserを1回。asset/GLSL/外部Jar/Config書換0、schema/network/gameplay/購入readiness変更0。

#### 自動検証・影響照合

| 対象 | 実施結果 |
|---|---|
| STATIC-PROMOTION | **11 profiles / 426 checks PASS**。plain54、external-only32、combined71、absent/version/hash/server/MC/Forge/metadata各34、competing redirect31。実Forge/FE classへの実Mixin変換、ASM解析。call0/2、descriptor、bridge欠落、half applyは拒否。対応外は介入0。static fixtureは製品外 |
| FE物理不在 | classpathからFE/ELを除外してsourcecompile＋実early plugin linkage PASS。外部class不足の読込0。実client不在起動は別途A3でPASS |
| native visual契約 | 5 consumer/22 uniform/native factory/ModelViewMat契約は旧§14.19＋今回意味同一/静的照合を維持。今回GL observerによる22型・内部identity/current program測定は**未実施** |
| compileJava / foodHealingUnitTest / build / check | **PASS / exit0 / 46 tasks executed**。既存local Gradle8.1.1・cache・offline。正確なargvは[process](../build/verification/fe-shader-production-20260930-065113/audit/build-final-local-process.json)。最初のsandbox native-platform.dllアクセス失敗は保全し、承認範囲のlocal権限実行1回で成功。外部download0 |
| 全unit | localization819、cost602、parity320、Flight88、Nutrition93、Mastery購入153、OPEN143、SW47、P150/T53、TimeStop9、data境界5000、Ammo10level×1000＝計10000/invalid/absent、legacy respec各群すべてPASS。既存case削除0 |
| 最終source core | **vanilla114/114 / 承認TaCZ1.1.7-hotfix2 114/114**。新規隔離userdev server/world、両通常保存/stop/exit0（06:56:28 / 06:58:52）。coreはForge47.2.0 userdevであり、配布Jar直接の全外部統合とはしない |
| 既存外部証拠 | 開始Jarから既存全product class/asset/lang byte同一、旧5Mixin config・既存refmap mapping同一。§14.73の経路照合/後続回帰を継承。L2/Trial/Invader/Mek/Avaritia/Pam/SW/Ammo full suite再実行0。過去PASSを今回Jar新規PASSへ転記しない |

初回PRECHECK読取scriptのdev class/reobf class比較を対応dev archiveへ訂正、保存読取のJSON大小文字キー（Version/version）はcase-sensitive化、native日本語item名に照合readerを合わせた。いずれも読取/検証scriptの訂正であり、製品repair cycleは**0**。期待値/guard/既存caseの緩和なし。

#### 最終配布Jar直接 A3 / B3

MC1.20.1 / Forge47.4.0 / Microsoft Java17.0.15 / Prism10.0.5、既存正常認証を通常利用。**COMPUTER USE通常GUI/input＋AUTOMATED readonly照合、HUMAN INPUT=0**。credential読取/コピー・再認証・新downloadなし。新instance/worldのみ、原本/過去world不使用。生成前GUIで自然湧き/自然回復OFF、structures OFFを記録し保存値で確認。verification MOD/standalone shader patch/observer/fixture/GameTest Jarは両profileとも0。

| Profile | 操作・実測・判定 |
|---|---|
| A3 `FHR_RELEASE_A3_20260930-065113` | 最終製品Jar1本。title到達/操作要求警告なし/FE classloading crash0。新Survival/Normal Superflat、numeric HP20/20・count0/2000・SP0、通常inventory→S GUI |
| A3 command / TaCZ不在 | 正式 `/foodhealing syokugi ` suggestion6（level/count/setlevel/setcount/setskillpoint/addskillpoint）、実行せず閉じる。TaCZ基礎nodeはvisible/locked、日本語理由表示、disabled clickでpoint0/SP0保持。A2全26skill/7stat/Ammo等はlang/class同一により維持、今回全面再撮影なし |
| A3 保存/再読込/終了 | 07:02:25通常全dimension保存→同新world1回再読込→HUD20/20→07:03:20通常保存→07:03:38 Quit。PID24872終了。before/after canonical完全一致：schema5/pending0、未使用/使用済SP0、取得/disabled/stat空、食技count/level0、HP20。**REAL CLIENT LIMITED PASS** |
| B3 `FHR_RELEASE_B3_20260930-065113` | 同最終製品＋FE2.7.20 / EL2.1.19fix / Curios5.14.1+1.20.1 / GeckoLib4.8.2 / Iron's Spellbooks3.16.3 / Iron's Lib2.1.0。B2から外部6原物の版/hash/内容変更0。nested PlayerAnimator1.0.2-rc1+1.20、MixinExtras0.4.1はloader選択、重複配置なし。[全size/hash/配置](../build/verification/fe-shader-production-20260930-065113/audit/client-instances.json) / [実ロード](../build/verification/fe-shader-production-20260930-065113/audit/B3-loaded-mods.json) |
| B3 startup | 07:04:19以降通常title、20秒以上＋通常UI操作中も安定。実debugに内蔵2Mixin適用。**Uniform→MUniform cast0 / Mixin apply failure0 / FH startup ERROR0**、helper/metadata/compat由来のblocking warning画面なし |
| B3 world / item | 新Creative/Normal Superflat worldへ07:06:48入場。通常 `/give @s fantasy_ending:fantasy_ending_ingot 1` を1回。hotbar/手持ち/通常inventoryでnative色付きitem描画、全面missing texture・render crash・shader compile/link fatalなし。**NORMAL PRODUCT VISUAL SMOKE PASS**。特別renderer/内部observerなし |
| B3 native確認画面 / 残log | 新world生成時の標準「実験的設定」確認は表示・通常GUI承認。title前compat警告と区別。ERRORログ15行（FE model1、superflat biome fallback1、tag10、Iron's loot2、FE advancement1）を保全。asset/sound/inactive sampler等のWARNも残る。今回startup/ingot描画を阻害せず、外部全機能無害/WARN0/ERROR0とはしない |
| B3 保存/終了 | 07:12:26通常Save & Quit・全5dimension保存→title→Quit、07:13:03のPID6368消滅確認。readonly保存：Creative、HP20、schema5/pending0/SP0、native ingot1。**STARTUP + NORMAL PRODUCT VISUAL SMOKE PASS**。再読込はB3範囲外 |

EL照合hash `0E29AF51DABD4E0EA8A315C1886E61F3ABEE62045F196EB6913D7F4DBA6CDC34`。client logs/画面/通常保存はすべてrun auditへ保全。[A3残log分類](../build/verification/fe-shader-production-20260930-065113/audit/A3-warnings-errors.json) / [B3残log分類](../build/verification/fe-shader-production-20260930-065113/audit/B3-warnings-errors.json)。native Configの新world既定生成は通常処理であり、FE設定を改造して回避していない。

#### 最終成果物・gate

[foodhealing-3.0.0.jar](../build/libs/foodhealing-3.0.0.jar) **372,242 bytes / 286 entries / SHA-256 8B8A31FA308CD24C4A139D65FEF5818F1A2DABD8587A238D93974F787FFE10DD**。A3/B3配置・reobf/output・archiveとhash同一。modId foodhealing/version3.0.0・schema5/protocol7・finite2590、repeatable5/10/10/5/20/20/2不変。**6 Mixin config**（既存5＋feuniform）、refmap/reobf・既存mapping保全PASS。new class6/config1/ZIP directory2＝9 entries追加、既存entry変更はmanifest/refmapのみ。外部class/Jar・standalone patch・verification observer/fixture/GameTest/ExampleMod/画面混入0。既存TimeStopの正式CountObserver/UseObserverは製品機構として維持。

[AUDIT F.1](AUDIT_REPORT.md#queue8-release-blocker-matrix)のstartupだけ **FINAL #8 RUN PASS — supported FE2.7.20 formal client startup** へ更新。他10分類は保持。Java/client/server/Gradle process残留0。既存Prism/無関係processは強制終了しない。新規依存取得、Security scan、GitHub/commit/push/PR/公開、正式Jar名変更、完全仕様書作成0。

**#8 COMPLETE / #1–#8 COMPLETE / #9 NOT STARTED / RC=NO / READY FOR RC DECISION**。次の1作業は**利用者のRC判断・#9開始範囲の明示指示待ち**。P/T・Flight PURCHASE READY/SP保護、REAL2CLIENT=BLOCKED、広域TimeStop SAFE DESIGN PROVEN=NO / SOURCE DIMENSION TRANSITION VERIFIED=NO / ownership BLOCKED、UOM/P vehicle未LOCK、他個別開始条件は維持。可逆クラフト増加は既知許容仕様・バグ修正対象外。全MOD/版/GPU互換や未確認gameplayを今回PASSに含めない。

[判定集計](../build/verification/fe-shader-production-20260930-065113/audit/reviewed-results.json) / [PRECHECK](../build/verification/fe-shader-production-20260930-065113/audit/promotion-precheck.json) / [static426](../build/verification/fe-shader-production-20260930-065113/audit/static-promotion-result.json) / [最終Jar監査](../build/verification/fe-shader-production-20260930-065113/audit/final-jar-audit.json) / [source差分](../build/verification/fe-shader-production-20260930-065113/audit/implementation.diff) / [build・全unit・check](../build/verification/fe-shader-production-20260930-065113/audit/build-final-local.log) / [vanilla114](../build/verification/fe-shader-production-20260930-065113/audit/vanilla-final.log) / [TaCZ114](../build/verification/fe-shader-production-20260930-065113/audit/tacz-final.log) / [実画面](../build/verification/fe-shader-production-20260930-065113/audit/screens/events.jsonl) / [A3保存](../build/verification/fe-shader-production-20260930-065113/audit/A3-final-save.json) / [B3保存](../build/verification/fe-shader-production-20260930-065113/audit/B3-final-save.json) / [終了process](../build/verification/fe-shader-production-20260930-065113/audit/process-final.json)


<a id="queue9-formal-release"></a>
## 14.75 Queue #9 — formal v3.0.0 release

本文更新: **2026-10-03 11:22 JST**。**#1–#8 COMPLETE / #9 PARTIAL / RC=YES / LOCAL RELEASE ARTIFACT AND SPECIFICATION COMPLETE / GITHUB UPDATE BLOCKED**。
完全仕様の正本は[FOOD_HEALING_RPG_V3_SPECIFICATION.md](FOOD_HEALING_RPG_V3_SPECIFICATION.md)へ統合。34章、26skill/7stat/6command、2590SP、schema5/protocol7、限定互換と未保証をsource/LOCKへ照合した。
#8後のsource274・build設定はhash差分0。accepted Jarを正式名へbyte copyし、372,242 bytes/286 entries/SHA-256 `8B8A31FA308CD24C4A139D65FEF5818F1A2DABD8587A238D93974F787FFE10DD`、metadata/6Mixin/refmap/reobf/非混入を再読取。build/game/外部suite再実行0、HUMAN0。
RC判断はAUDIT F.1の11分類継承＋今回整合確認でYES。旧L2/Trial/TimeStop/FE6/Flight/Pam/Ammo等のPASSを新規試験へ転記しない。原FAIL/NOT RUN/旧hashを保持。
前回（11:22 JST）の履歴: GitHub連携のrelease branch作成が403 Resource not accessible by integrationで拒否。branch/commit/main更新0、再試行0。前回読取時のmainは開始commitと同一。今回のremote状態は未確認。 Source commit `NOT CREATED — GitHub integration write denied (403)`。GitHub Release新方式/CurseForgeなし、正式Jarはlocal release artifactとsource treeを分離。[receipt](../release/v3.0.0/RELEASE_RECEIPT.md)。
REAL2CLIENT=BLOCKED、広いTimeStop SAFE DESIGN PROVEN=NO/source dimension NO/ownership BLOCKED、vehicle未LOCK、未知Flight attribution不可、その他個別gateを維持。
**前回11:22時点の次作業（履歴）:** GitHub連携の書込権限待ち。今回の現行作業は下記Git CLI再開記録。


### Git CLI publication resume — 2026-10-03 12:44 JST

Git CLI再開preflightで `<LOCAL_PATH>/food-healing-mod-main` は `.git` を持たず、root/remote/branch/HEAD/statusの5確認がすべてexit128 `not a git repository`。**STOP — LOCAL GIT REPOSITORY NOT FOUND**。
#8 freezeはsrc274とbuild3ファイル差分0、正式Jar372,242 bytes/286 entries/既存SHA-256完全一致、ExampleMod0。staged0/commit0/push0/retry0/HUMAN0。認証には未到達。remote URL/HEAD/現在mainは未確認であり、前回11:20 JSTのmain確認を今回の読戻しに転用しない。既存403は過去履歴として保全。
**#1–#8 COMPLETE / #9 PARTIAL / RC=YES / FORMAL RELEASE NOT COMPLETE**。
**次の1作業:** 現在のv3ファイルを保全したまま、このパスを正しい既存Git履歴へ安全に接続する方法の明示承認、または正しい既存checkoutの指定待ち。今回はgit init/clone/fetch/stage/commit/pushを行わず停止。GitHub integration APIへ戻らない。
