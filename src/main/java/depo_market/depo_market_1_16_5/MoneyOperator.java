package depo_market.depo_market_1_16_5;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.*;

public interface MoneyOperator {
    //スコアボードに存在するチームを確認。初めて確認されたチームなら所持金を初期値０にする
    public void LoadNewTeams();
    //データをロード、所持金を更新する
    public void LoadData(Map<String, Float> LoadData);
    //データを取得
    public  Map<String, Float> getData();
    //チームにお金を加算
    public void addMoney(String name, float money);
    public void addMoney(Player player, float money);
    //チームのお金を取得
    public float getMoney(Player player);
    public boolean isInAnyTeam(Player player);
    //プレイヤーがいるチームの全員のHPを管理する。チームのすべてのプレイヤーを検索しHPを変更。
    public void setHealth(Player player);
    //すべてのチームの全員のHPを元に戻す
    public void resetAllHealth();
    //すべてのチームの全員のHPを更新
    public void setAllHealth();

    public void ScoreBoardDestroy();
}
