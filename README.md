# Depo_Market  

# 概要
チームごとに所持金を記録し、物の売り買いができるようにした。  
データをセーブし続けるのでサーバーが落ちても大丈夫　　
# 詳細
EnableDpPluginでプラグインを有効にし、DisableDpPluginでプラグインを無効化します。  
プレイヤーをチームに所属させ、DpStartMarket→DpPlaceCustomerで出てきた商人をクリックすると取引できる。アイテムとお金の取引ができます。
お金はチームごとに記録されます。画面右に所持金が表示されます。１ページ目でアイテムを選択し、2ページ目で数量を選びます。上の段が買いで下の段が売りです。
１ページ目の一個目の本のみクリックするとエンチャントアイテム選択画面に飛びます。同様に2ページ目で数量を選びます。上の段が買いで下の段が売りです。　　
プラグインを止めるときはDpStopMarket→DpKillAllCustomerで取引が停止し、村人も全滅します。次回DpStartMarketしたときにすべての情報が初期化されます。  
DpSetPointCustomerで座標指定して商人を設置できます。  
DpGiveMoneyでお金を与えます。負の値を入力することでお金を奪えます。
DpSetDisadvantageで所持金の量でHPが変化するようにできます。  
DpLookTeamsでチームの所持金一覧を見れます。
DpLookScoreで個人の取引したお金の総量がわかります。お金を使いすぎていたら負の値になります。　　
#  コマンド一覧
  /EnableDpPlugin:プラグインを有効にする  
  /DisableDpPlugin:プラグインを無効にする  
  /DpStartMarket:マーケットを開始  
  /DpStopMarket:マーケットを止める  
  /DpPlaceCustomer(プレイヤーのみ使用可能):マーケット窓口を一人分実体化  
  /DpSetPointCustomer [X] [Y] [Z] :マーケット窓口を一人分実体化  
  /DpKillAllCustomer:全マーケット窓口を削除  
  /DpGiveMoney -team [チーム名]  -amount [金額] :お金をあげる  
  /DpSetDisadvantage [-disable_buy or -health or -none](プレイヤーのみ使用可能): 借金した時のデバフを決める  
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

