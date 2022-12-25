package depo_market.depo_market_1_16_5;

import org.bukkit.Bukkit;
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
        Object[] teamObjects = scoreboard.getTeams().toArray();
        for (Object teamObj : teamObjects) {
            Team team = (Team) teamObj;
            if (teams.containsKey(team.getName())) {
                Object[] members = team.getEntries().toArray();
                for (Object member : members) {
                    if (player.getName().equals(member)) {
                        return true;
                    }
                }

            }
        }
        return false;
    }
    public void setTeamMoney(Player player, float money) {
        Object[] teamObjects = scoreboard.getTeams().toArray();
        for (Object teamObj : teamObjects) {
            Team team = (Team) teamObj;
            if (teams.containsKey(team.getName())) {
                Object[] members = team.getEntries().toArray();
                for (Object member : members) {
                    if (player.getName().equals(member)) {
                        teams.put(team.getName(), money);
                    }
                }

            }
        }
    }
    public void addTeamMoney(Player player, float money) {
        Object[] teamObjects = scoreboard.getTeams().toArray();
        for (Object teamObj : teamObjects) {
            Team team = (Team) teamObj;
            if (teams.containsKey(team.getName())) {
                Object[] members = team.getEntries().toArray();
                for (Object member : members) {
                    if (player.getName().equals(member)) {
                        teams.put(team.getName(), teams.get(team.getName()) + money);
                    }
                }

            }
        }
    }
    public  Map<String, Float> getData(){
        return teams;
    }
    public float getTeamMoney(Player player){
        Object[] teamObjects = scoreboard.getTeams().toArray();
        for (Object teamObj : teamObjects) {
            Team team = (Team) teamObj;
            if (teams.containsKey(team.getName())) {
                Object[] members = team.getEntries().toArray();
                for (Object member : members) {
                    if (player.getName().equals(member)) {
                        return teams.get(team.getName());
                    }
                }

            }
        }
        return 0;
    }

}
