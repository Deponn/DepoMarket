package depo_market.depo_market_1_16_5;

import depo_market.depo_market_1_16_5.PropertiesAndConstant.Const;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.*;


/**
 * チームごとに所持金を計算する。プレイヤーがチームに所属するかも判定。存在するチームをロードしてチーム名と所持金を記録。
 */
public class TeamOperator {
    private final Scoreboard scoreboard;
    private final Map<String, Float> teams;

    //スコアボードに存在するチームを確認。所持金を初期値０にする
    public TeamOperator() {
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        this.scoreboard = Objects.requireNonNull(scoreboardManager).getMainScoreboard();
        teams = new HashMap<>();
        Object[] teamObjects = scoreboard.getTeams().toArray();
        for (Object teamObj : teamObjects) {
            Team team = (Team) teamObj;
            teams.put(team.getName(), 0f);
        }
        ScoreBoardMake();
        setAllTeamHealth();
    }

    //スコアボードに存在するチームを確認。初めて確認されたチームなら所持金を初期値０にする
    public void LoadNewTeams() {
        Object[] teamObjects = scoreboard.getTeams().toArray();
        for (Object teamObj : teamObjects) {
            Team team = (Team) teamObj;
            if (!teams.containsKey(team.getName())) {
                teams.put(team.getName(), 0f);
            }
        }
    }
    //データをロード、所持金を更新する
    public void LoadData(Map<String, Float> LoadData) {
        List<String> keys = new ArrayList<>(LoadData.keySet());
        for(String key : keys){
            teams.put(key,LoadData.get(key));
        }
    }
    public  Map<String, Float> getData(){
        return teams;
    }

    //チームにお金を加算
    public void addTeamMoney(String team, float money) {
        teams.put(team, teams.get(team) + money);
        ScoreBoardMake(team);
    }
    public void addTeamMoney(Player player, float money) {
        Team myTeam = getJoiningTeam(player);
        if(myTeam != null) {
            teams.put(myTeam.getName(), teams.get(myTeam.getName()) + money);
            ScoreBoardMake(myTeam.getName());
        }
    }
    public float getTeamMoney(Player player){
        Team MyTeam = getJoiningTeam(player);
        if(MyTeam != null) {
            return teams.get(MyTeam.getName());
        }else {
            return 0;
        }
    }
    public float getTeamMoney(Team team){
        if(team != null) {
            return teams.get(team.getName());
        }else {
            return 0;
        }
    }

    public boolean isPlayerInAnyTeam(Player player){
        return getJoiningTeam(player) != null;
    }

    //プレイヤーがチームに所属しているか判定すべてのチームのすべてのメンバーと照合し一致する名前があったらそのチームを返す。
    public Team getJoiningTeam(Player player) {
        Object[] teamObjects = scoreboard.getTeams().toArray();
        for (Object teamObj : teamObjects) {
            Team team = (Team) teamObj;
            if (teams.containsKey(team.getName())) {
                if (team.hasEntry(player.getName())) {
                    return team;
                }
            }
        }
        return null;
    }
    //プレイヤーがいるチームの全員のHPを管理する。チームのすべてのプレイヤーを検索しHPを変更。
    public void setPlayerTeamHealth(Player player) {
        Team targetTeam = getJoiningTeam(player);
        float teamMoney = getTeamMoney(targetTeam);
        if (targetTeam != null) {
            Set<String> playerSet = targetTeam.getEntries();
            Object[] playerObjects = playerSet.toArray();
            for (Object playerObj : playerObjects) {
                String playerName = (String) playerObj;
                Player targetPlayer = Bukkit.getPlayer(playerName);
                if (targetPlayer != null) {
                    changeHealth(targetPlayer,teamMoney);
                }
            }
        }
    }
    //すべてのチームの全員のHPを元に戻す
    public void resetAllTeamHealth() {
        for (String teamName : teams.keySet()) {
            Team team = scoreboard.getTeam(teamName);
            if(team != null) {
                Set<String> playerSet = team.getEntries();
                Object[] playerObjects = playerSet.toArray();
                for (Object playerObj : playerObjects) {
                    String playerName = (String) playerObj;
                    Player targetPlayer = Bukkit.getPlayer(playerName);
                    if (targetPlayer != null) {
                        AttributeInstance healthAttribute = targetPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                        Objects.requireNonNull(healthAttribute).setBaseValue(20);
                    }
                }
            }
        }
    }
    //すべてのチームの全員のHPを更新
    public void setAllTeamHealth() {
        for (String teamName : teams.keySet()) {
            Team team = scoreboard.getTeam(teamName);
            if(team != null) {
                float teamMoney = getTeamMoney(team);
                Set<String> playerSet = team.getEntries();
                Object[] playerObjects = playerSet.toArray();
                for (Object playerObj : playerObjects) {
                    String playerName = (String) playerObj;
                    Player targetPlayer = Bukkit.getPlayer(playerName);
                    if (targetPlayer != null) {
                        changeHealth(targetPlayer,teamMoney);
                    }
                }
            }
        }
    }

    private void changeHealth(Player player, float teamMoney){
        AttributeInstance healthAttribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        //お金の量が0から離れれば離れるほど、HPも増減する。ハイパボリックタンジェントを利用
        Objects.requireNonNull(healthAttribute).setBaseValue(20 * (1 + Math.tanh(teamMoney / Const.MONEY_HEALTH)));
    }


    private void ScoreBoardMake(){
        Objective objective = scoreboard.getObjective(Const.ScoreBoardName);
        if ( objective == null ) {
            objective = scoreboard.registerNewObjective(Const.ScoreBoardName, "dummy","所持金");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        }
    }
    private void ScoreBoardMake(String teamName){
        Objective objective = scoreboard.getObjective(Const.ScoreBoardName);
        if ( objective == null ) {
            objective = scoreboard.registerNewObjective(Const.ScoreBoardName, "dummy","所持金");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        }
        objective.getScore(teamName).setScore(Math.round(teams.get(teamName)));
    }
    public void ScoreBoardDestroy(){
        Objective objective = scoreboard.getObjective(Const.ScoreBoardName);
        if ( objective != null ) {
            objective.unregister();
        }
    }
}
