package depo_market.depo_market_1_16_5;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandSuggest implements TabCompleter{
    /**
     * コマンドのTAB補完候補を返す
     *
     * @param sender コマンド送信者
     * @param args   引数
     * @return コマンド補完候補
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if(command.getName().equalsIgnoreCase("DpGiveMoney")) {
            return suggest_give_money(sender, args);
        }else if (command.getName().equalsIgnoreCase("DpSetDisadvantage")){
            return suggest_disadvantage(sender, args);
        }else {
            return new ArrayList<String>();
        }
    }
    /**
     * 金をあげたり奪ったりするコマンドのTAB補完候補を返す
     *
     * @param sender コマンド送信者
     * @param args   引数
     * @return コマンド補完候補
     */
    private static List<String> suggest_give_money(CommandSender sender, String[] args) {
        List<String> argsList = Arrays.asList(args);
        if (argsList.size() > 1 && "-amount".equals(argsList.get(argsList.size() - 2))) {
            return Arrays.asList("-10000","0","10000");
        } else if (argsList.size() > 1 && "-team".equals(argsList.get(argsList.size() - 2))) {
            ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
            Scoreboard scoreboard = Objects.requireNonNull(scoreboardManager).getMainScoreboard();
            Object[] teamObjects = scoreboard.getTeams().toArray();
            List<String> teamNames = new ArrayList<>();
            for(Object teamObj : teamObjects){
                Team team = (Team)teamObj;
                teamNames.add(team.getName());
            }
            return teamNames;
        }else {
            return Stream.of("-amount", "-team")
                    .filter(s -> !argsList.contains(s))
                    .collect(Collectors.toList());
        }
    }
    /**
     * デバフを決めるコマンドのTAB補完候補を返す
     *
     * @param sender コマンド送信者
     * @param args   引数
     * @return コマンド補完候補
     */
    private static List<String> suggest_disadvantage(CommandSender sender, String[] args) {
        List<String> argsList = Arrays.asList(args);
        return Stream.of("-disable_buy", "-health","-none")
                .filter(s -> !argsList.contains(s))
                .collect(Collectors.toList());
    }
}
