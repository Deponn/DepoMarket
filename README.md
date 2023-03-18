# Depo_Market  

# 概要
チームごとに所持金を記録し、物の売り買いができるようにした。  
データをセーブし続けるのでサーバーが落ちても大丈夫　　
# 詳細
DpEnableMarketPluginでプラグインを有効にし、DpDisableMarketPluginでプラグインを無効化します。  
プレイヤーをチームに所属させ、DpInitializeMarket→DpStartMarket→DpPlaceCustomerで出てきた商人をクリックすると取引できる。アイテムとお金の取引ができます。
お金はチームごとに記録されます。画面右に所持金が表示されます。１ページ目でアイテムを選択し、2ページ目で数量を選びます。上の段が買いで下の段が売りです。  
１ページ目の一個目の本のみクリックするとエンチャントアイテム選択画面に飛びます。同様に2ページ目で数量を選びます。上の段が買いで下の段が売りです。  
プラグインを止めるときはDpStopMarket→DpKillAllCustomerで取引が停止し、村人も全滅します。次回DpInitializeMarketしたときにすべての情報が初期化されます。  
チームに変更があった場合はDpNewTeamを実行してください。
DpGiveMoneyでお金を与えます。負の値を入力することでお金を奪えます。
DpchangePropatiesでプロパティを変更。
DpLookTeamsでチームの所持金一覧を見れます。
DpLookScoreで個人の取引したお金の総量がわかります。お金を使いすぎていたら負の値になります。　　
#  コマンド一覧
  /DpEnableMarketPlugin:プラグインを有効にする  
  /DpDisableMarketPlugin:プラグインを無効にする  
  /DpInitializeMarket:初期化。
  /DpStartMarket:マーケットを開始  
  /DpStopMarket:マーケットを止める  
  /DpPlaceCustomer:マーケット窓口を一人分実体化し、プレイヤーの目の前に出す。    
  /DpPlaceCustomer [X] [Y] [Z] :位置も指定できる。  
  /DpKillAllCustomer:全マーケット窓口を削除  
  /DpGiveMoney -team [チーム名]  -amount [金額] :お金をあげる  
  /DpchangePropaties [プロパティ名] [値] :プロパティを変更。
  /DpLookTeams(プレイヤーのみ使用可能): 全チームの所持金を見る  
  /DpLookScore(プレイヤーのみ使用可能): スコアを見る  
# config
"disadvantage"  :文字列　お金の増減の影響  
"Depo_isRun"    :真偽値　動いているか否か   
"Depo_teams"    :文字列　チーム一覧   
"Depo_moneys"   :float   チームのお金  
"Depo_Items"    :文字列　アイテムの名前(プラグイン側で定義)  
"Depo_Prices"   :float 　アイテムの値段  
"Depo_buy"      :int     アイテムの売られた量  
"Depo_sell"     :int     アイテムの買われた量  

# propaties
Disadvantage:お金の変動によるデバフを決める。  
PrizeMoney:float キル時にもらえるお金  
BoundOfMoney:float 所持金の限界  
PriceMoveRate:float 価格変動率  
ItemDataBase:String 買えるアイテムのセット  
isTeamGame:boolean チームで所持金を共有する  