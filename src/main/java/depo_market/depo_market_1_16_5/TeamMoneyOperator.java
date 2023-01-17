package depo_market.depo_market_1_16_5;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.*;


/**
 * チームごとに所持金を計算する。プレイヤーがチームに所属するかも判定。存在するチームをロードしてチーム名と所持金を記録。
 */
public class TeamMoneyOperator {
    final float MONEY_HEALTH = 200000f;
    private final Scoreboard scoreboard;
    private final Map<String, Float> teams = new HashMap<>();
    private final Map<Player,Float> playerMoney = new HashMap<>();

    //スコアボードに存在するチームを確認。所持金を初期値０にする
    public TeamMoneyOperator() {
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        this.scoreboard = Objects.requireNonNull(scoreboardManager).getMainScoreboard();
        Object[] teamObjects = scoreboard.getTeams().toArray();
        for (Object teamObj : teamObjects) {
            Team team = (Team) teamObj;
            teams.put(team.getName(), 0f);
        }
    }

    //スコアボードに存在するチームを確認。所持金を初期値０にする
    public void Initialize() {
        Object[] teamObjects = scoreboard.getTeams().toArray();
        for (Object teamObj : teamObjects) {
            Team team = (Team) teamObj;
            teams.put(team.getName(), 0f);
        }
        List<World> worlds = Bukkit.getWorlds();
        for (World world : worlds){
            List<Player> players = world.getPlayers();
            for (Player player : players){
                playerMoney.put(player,0f);
            }
        }
        ScoreBoardMake();
        setAllTeamHealth(Bukkit.getWorlds());
    }

    //ロード、所持金を更新する
    public void LoadTeams(Map<String, Float> LoadData) {
        List<String> keys = new ArrayList<>(LoadData.keySet());
        for(String key : keys){
            teams.put(key,LoadData.get(key));
        }
    }

    public boolean PlayerInTeam(Player player){
        return TeamOfPlayer(player) != null;
    }

    //チームにお金を加算
    public void addTeamMoney(String team, float money) {
        teams.put(team, teams.get(team) + money);
    }
    public void addTeamMoney(Player player, float money) {
        Team myTeam = TeamOfPlayer(player);
        if(myTeam != null) {
            teams.put(myTeam.getName(), teams.get(myTeam.getName()) + money);
        }

    }
    public float getTeamMoney(Player player){
        Team MyTeam = TeamOfPlayer(player);
        if(MyTeam != null) {
            return teams.get(MyTeam.getName());
        }else {
            return 0;
        }
    }
    public  Map<String, Float> getData(){
        return teams;
    }

    //プレイヤーがいるチームの全員のHPを管理する。すべてのワールドのすべてのプレイヤーを検索したそのチームに所属していたらHPを変更。
    public void setTeamHealth(Player player) {
        Team targetTeam = TeamOfPlayer(player);
        if(targetTeam!= null) {
            Set<String> playerSet = targetTeam.getEntries();
            Object[] playerObjects = playerSet.toArray();
            List<String> playerNames = new ArrayList<>();
            for (Object playerObj : playerObjects) {
                playerNames.add((String) playerObj);
            }
            List<World> worlds = Bukkit.getWorlds();
            for (World world : worlds) {
                List<Player> players = world.getPlayers();
                for (Player targetPlayer : players) {
                    if (playerNames.contains(targetPlayer.getName())) {
                        AttributeInstance healthAttribute = targetPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                        //お金の量が0から離れれば離れるほど、HPも増減する。ハイパボリックタンジェントを利用
                        Objects.requireNonNull(healthAttribute).setBaseValue(20 * (1 + Math.tanh(getTeamMoney(player) / MONEY_HEALTH)));
                    }
                }
            }
        }
    }
    //すべてのチームの全員のHPを元に戻す
    public void resetTeamHealth() {
        List<World> worlds = Bukkit.getWorlds();
        for(World world : worlds) {
            List<Player> players = world.getPlayers();
            for (Player targetPlayer : players) {
                AttributeInstance healthAttribute = targetPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                Objects.requireNonNull(healthAttribute).setBaseValue(20);
            }
        }
    }
    //すべてのチームの全員のHPを更新
    public void setAllTeamHealth(List<World> worlds) {
        for (World world : worlds) {
            List<Player> players = world.getPlayers();
            for (Player targetPlayer : players) {
                AttributeInstance healthAttribute = targetPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                Objects.requireNonNull(healthAttribute).setBaseValue(20 * (1 + Math.tanh(getTeamMoney(targetPlayer) / MONEY_HEALTH)));
            }
        }
    }
    //プレイヤーがチームに所属しているか判定すべてのチームのすべてのメンバーと照合し一致する名前があったらそのチームを返す。
    private Team TeamOfPlayer(Player player) {
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
    public void addPlayerMoney(Player player,float money){
        float oldMoney = playerMoney.getOrDefault(player, 0f);
        playerMoney.put(player,oldMoney + money);
        ScoreBoardMake();
    }

    public float getPlayerMoney(Player player){
        if(playerMoney.containsKey(player)){
            return playerMoney.get(player);
        }
        return 0f;
    }

    private void ScoreBoardMake(){
        Objective objective = scoreboard.getObjective("DpMoney");
        if ( objective == null ) {
            objective = scoreboard.registerNewObjective("DpMoney", "dummy","所持金");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        }
        for(String teamName : teams.keySet()){
            objective.getScore(teamName).setScore(Math.round(teams.get(teamName)));
        }
    }
    public void ScoreBoardDestroy(){
        Objective objective = scoreboard.getObjective("DpMoney");
        if ( objective != null ) {
            objective.unregister();
        }
    }
}
