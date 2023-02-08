package depo_market.depo_market_1_16_5;

import depo_market.depo_market_1_16_5.PropertiesAndConstant.Const;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.*;

public class PersonalMoneyOperator extends TeamOperator{
    private final Scoreboard scoreboard;
    private final Map<String, Float> personsMoney;

    //スコアボードに存在するチームを確認。所持金を初期値０にする
    public PersonalMoneyOperator() {
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        this.scoreboard = Objects.requireNonNull(scoreboardManager).getMainScoreboard();
        personsMoney = new HashMap<>();
        List<World> worlds = Bukkit.getWorlds();
        for (World world : worlds){
            List<Player> players = world.getPlayers();
            for (Player player : players){
                personsMoney.put(player.getName(),0f);
            }
        }
        ScoreBoardMake();
        setAllTeamHealth();
    }

    //スコアボードに存在するチームを確認。初めて確認されたチームなら所持金を初期値０にする
    public void LoadNewTeams() {
        List<World> worlds = Bukkit.getWorlds();
        for (World world : worlds){
            List<Player> players = world.getPlayers();
            for (Player player : players){
                if(!personsMoney.containsKey(player.getName())) {
                    personsMoney.put(player.getName(), 0f);
                }
            }
        }
    }
    //データをロード、所持金を更新する
    public void LoadData(Map<String, Float> LoadData) {
        List<String> keys = new ArrayList<>(LoadData.keySet());
        for(String key : keys){
            personsMoney.put(key,LoadData.get(key));
        }
    }
    public  Map<String, Float> getData(){
        return personsMoney;
    }

    //チームにお金を加算
    public void addTeamMoney(String playerName, float money) {
        if(isPlayerInAnyTeam(playerName)) {
            personsMoney.put(playerName, personsMoney.get(playerName) + money);
            ScoreBoardMake(playerName);
        }
    }
    public void addTeamMoney(Player player, float money) {
        if(isPlayerInAnyTeam(player.getName())) {
            personsMoney.put(player.getName(), personsMoney.get(player.getName()) + money);
            ScoreBoardMake(player.getName());
        }
    }
    public float getTeamMoney(Player player){
        if(isPlayerInAnyTeam(player.getName())) {
            return personsMoney.get(player.getName());
        }else {
            return 0;
        }
    }
    public float getTeamMoney(String playerName){
        if(isPlayerInAnyTeam(playerName)) {
            return personsMoney.get(playerName);
        }else {
            return 0;
        }
    }

    public boolean isPlayerInAnyTeam(String playerName){
        return personsMoney.containsKey(playerName);
    }

    //プレイヤーがいるチームの全員のHPを管理する。チームのすべてのプレイヤーを検索しHPを変更。
    public void setPlayerTeamHealth(Player player) {
        if(isPlayerInAnyTeam(player.getName())) {
            changeHealth(player, personsMoney.get(player.getName()));
        }
    }
    //すべてのチームの全員のHPを元に戻す
    public void resetAllTeamHealth() {
        List<World> worlds = Bukkit.getWorlds();
        for (World world : worlds){
            List<Player> players = world.getPlayers();
            for (Player player : players){
                AttributeInstance healthAttribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                Objects.requireNonNull(healthAttribute).setBaseValue(20);
            }
        }
    }
    //すべてのチームの全員のHPを更新
    public void setAllTeamHealth() {
        List<World> worlds = Bukkit.getWorlds();
        for (World world : worlds){
            List<Player> players = world.getPlayers();
            for (Player player : players){
                if(isPlayerInAnyTeam(player)) {
                    changeHealth(player, personsMoney.get(player.getName()));
                }
            }
        }
    }

    private void changeHealth(Player player, float money){
        AttributeInstance healthAttribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        //お金の量が0から離れれば離れるほど、HPも増減する。ハイパボリックタンジェントを利用
        Objects.requireNonNull(healthAttribute).setBaseValue(20 * (1 + Math.tanh(money / Const.MONEY_HEALTH)));
    }


    private void ScoreBoardMake(){
        Objective objective = scoreboard.getObjective(Const.ScoreBoardName);
        if ( objective == null ) {
            objective = scoreboard.registerNewObjective(Const.ScoreBoardName, "dummy","所持金");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        }
    }
    private void ScoreBoardMake(String playerName){
        Objective objective = scoreboard.getObjective(Const.ScoreBoardName);
        if ( objective == null ) {
            objective = scoreboard.registerNewObjective(Const.ScoreBoardName, "dummy","所持金");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        }
        objective.getScore(playerName).setScore(Math.round(personsMoney.get(playerName)));
    }
    public void ScoreBoardDestroy(){
        Objective objective = scoreboard.getObjective(Const.ScoreBoardName);
        if ( objective != null ) {
            objective.unregister();
        }
    }
}
