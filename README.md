# Depo_Market

チームごとに所持金を記録し、物の売り買いができるようにした。

データをセーブし続けるのでサーバーが落ちても大丈夫

プレイヤーをチームに所属させ、
initialize_market→start_market→place_customerで出てきた商人をクリックすると取引できる。
一回initialize_marketしたら、もうしないこと。データが消えます。Cancel changes
チームが増えたらstop_market→load_new_team→start_market
プラグインを止めるときはstop_market→kill_all_customer
taxおよびgive_moneyで対象のチームと自分のチームとお金のやり取り(強制)



  initialize_market:
    description: マーケットのデータを初期化する
    usage: /initialize_market [自分の名前]
  start_market:
    description: マーケットを開始
    usage: /start_market
    permission: market
  stop_market:
    description: マーケットを止める
    usage: /stop_market
    permission: market
  place_customer:
    description: マーケット窓口を一人分実体化
    usage: /place_customer
    permission: market
  kill_all_customer:
    description: 全マーケット窓口を削除
    usage: /kill_all_customer
    permission: market
  tax:
    description: 徴税
    usage: /tax [チーム名] [金額]
    permission: market
  give_money:
    description: お金をあげる
    usage: /give_money [チーム名] [金額]
    permission: market
  set_disadvantage:
    description: 借金した時のデバフを決める
    usage: /set_disadvantage [デバフ名]
    permission: market
  load_new_team:
    description: チームが新たに作られた時用
    usage: /  reload_team
    permission: market
  Look_teams:
    description: 全チームの所持金を見る
    usage: / Look_teams
