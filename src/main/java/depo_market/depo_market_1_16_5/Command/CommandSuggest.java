package depo_market.depo_market_1_16_5.Command;

import depo_market.depo_market_1_16_5.ItemDataBase.DBDefault;
import depo_market.depo_market_1_16_5.ItemDataBase.DBList;
import depo_market.depo_market_1_16_5.PropertiesAndConstant.Const;
import depo_market.depo_market_1_16_5.PropertiesAndConstant.MoneyDisAd;
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
        if(command.getName().equalsIgnoreCase(CmdName.GiveMoney.getCmd())) {
            return suggest_give_money(sender, args);
        }else if (command.getName().equalsIgnoreCase(CmdName.PlaceCustomer.getCmd())){
                return suggest_PlaceCustomer(sender, args);
        }else if (command.getName().equalsIgnoreCase(CmdName.ChangeProperties.getCmd())){
            return Suggest_Properties(sender, args);
        }else {
            return new ArrayList<>();
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
    private static List<String> suggest_PlaceCustomer(CommandSender sender, String[] args) {
        List<String> argsList = Arrays.asList(args);
        if (argsList.size() <3) {
            return Arrays.asList("1000","0","2000");
        }
        return new ArrayList<>();
    }
    /**
     * プロパティを決めるコマンドのTAB補完候補を返す
     *
     * @param sender コマンド送信者
     * @param args   引数
     * @return コマンド補完候補
     */
    private static List<String> Suggest_Properties(CommandSender sender, String[] args) {
        List<String> argsList = Arrays.asList(args);
        List<String> suggestList = new ArrayList<>();
        String DisAd = "-" + Const.DisadvantagePropName;
        String PrMoney = "-" + Const.PrizeMoneyPropName;
        String BoundMoney = "-" + Const.BoundOfMoneyPropName;
        String PriceMoveRate = "-" + Const.PriceMoveRatePropName;
        String DBItem = "-" + Const.DBItemPropName;
        String isTeamGame = "-" + Const.isTeamGamePropName;
        if (argsList.size() > 1 && DisAd.equals(argsList.get(argsList.size() - 2))) {
            for (MoneyDisAd moneyDisAd : MoneyDisAd.values()) {
                suggestList.add(moneyDisAd.getString());
            }
            return suggestList;
        }else if(argsList.size() > 1 && PrMoney.equals(argsList.get(argsList.size() - 2))) {
            return Arrays.asList("0","1000","5000");
        }else if(argsList.size() > 1 && BoundMoney.equals(argsList.get(argsList.size() - 2))) {
            return Arrays.asList("0","1000","5000");
        }else if(argsList.size() > 1 && PriceMoveRate.equals(argsList.get(argsList.size() - 2))) {
            return Arrays.asList("0","0.5","1");
        }else if(argsList.size() > 1 && DBItem.equals(argsList.get(argsList.size() - 2))) {
            return Arrays.asList(DBList.Kojosen.getName(),DBList.Kojosen2.getName(), DBList.Default.getName());
        }else if(argsList.size() > 1 && isTeamGame.equals(argsList.get(argsList.size() - 2))) {
            return Arrays.asList("true","false");
        }else if(argsList.size() == 1) {
            return Stream.of(DisAd, PrMoney,BoundMoney,PriceMoveRate,DBItem,isTeamGame)
                    .filter(s -> !argsList.contains(s))
                    .collect(Collectors.toList());
        }else {
            return suggestList;
        }
    }
}
