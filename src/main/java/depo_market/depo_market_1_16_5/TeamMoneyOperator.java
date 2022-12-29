package depo_market.depo_market_1_16_5;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class TeamMoneyOperator {

    private final Scoreboard scoreboard;
    private final Map<String, Float> teams = new HashMap<>();

    public TeamMoneyOperator() {
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        this.scoreboard = Objects.requireNonNull(scoreboardManager).getMainScoreboard();
        Object[] teamObjects = scoreboard.getTeams().toArray();
        for (Object teamObj : teamObjects) {
            Team team = (Team) teamObj;
            teams.put(team.getName(), 0f);
        }
    }
    public void Initialize() {
        Object[] teamObjects = scoreboard.getTeams().toArray();
        for (Object teamObj : teamObjects) {
            Team team = (Team) teamObj;
            teams.put(team.getName(), 0f);
        }
    }
    public void LoadTeams(Map<String, Float> LoadData) {
        List<String> keys = new ArrayList<>(LoadData.keySet());
        for(String key : keys){
            teams.put(key,LoadData.get(key));
        }
    }

    public void reLoadTeams() {
        Object[] teamObjects = scoreboard.getTeams().toArray();
        for (Object teamObj : teamObjects) {
            Team team = (Team) teamObj;
            if (!teams.containsKey(team.getName())) {
                teams.put(team.getName(), 0f);
            }
        }
    }
    public boolean PlayerInTeam(Player player){
        return TeamOfPlayer(player) != null;
    }
    public void addTeamMoney(String team, float money) {
        teams.put(team, teams.get(team) + money);
    }
    public void addTeamMoney(Player player, float money) {
        Team myTeam = TeamOfPlayer(player);
        if(myTeam != null) {
            teams.put(myTeam.getName(), teams.get(myTeam.getName()) + money);
        }

    }
    public  Map<String, Float> getData(){
        return teams;
    }
    public float getTeamMoney(Player player){
        Team MyTeam = TeamOfPlayer(player);
        if(MyTeam != null) {
            return teams.get(MyTeam.getName());
        }else {
            return 0;
        }
    }

    public void setTeamHealth(Player player) {
        Team targetTeam = TeamOfPlayer(player);
        if(targetTeam!= null) {
            Set<String> playerSet = targetTeam.getEntries();
            Object[] playerObjects = playerSet.toArray();
            List<String> playerNames = new ArrayList<>();
            for (Object playerObj : playerObjects) {
                playerNames.add((String) playerObj);
            }
            World world = player.getWorld();
            List<Player> players = world.getPlayers();
            for (Player targetPlayer : players) {
                if(playerNames.contains(targetPlayer.getName())){
                    AttributeInstance healthAttribute = targetPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                    Objects.requireNonNull(healthAttribute).setBaseValue(20 * (1 + Math.tanh(getTeamMoney(player))));
                }
            }
        }
    }
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
}
