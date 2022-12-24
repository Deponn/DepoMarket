package depo_market.depo_market_1_16_5;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
        if (command.getName().equalsIgnoreCase("tax")) {
            return suggest_tax(sender, args);
        }else if(command.getName().equalsIgnoreCase("give_money")) {
            return suggest_tax(sender, args);
        }else if (command.getName().equalsIgnoreCase("set_disadvantage")){
            return suggest_disadvantage(sender, args);
        }else {
            return new ArrayList<String>();
        }
    }
    private static List<String> suggest_tax(CommandSender sender, String[] args) {
        List<String> argsList = Arrays.asList(args);
        if (argsList.size() > 1 && "-amount".equals(argsList.get(argsList.size() - 2))) {
            return Arrays.asList("0", "5", "20","100");
        } else if (argsList.size() > 1 && "-team".equals(argsList.get(argsList.size() - 2))) {
            return Arrays.asList("1", "2", "3");
        }else {
            return Stream.of("-amount", "-team")
                    .filter(s -> !argsList.contains(s))
                    .collect(Collectors.toList());
        }
    }
    private static List<String> suggest_disadvantage(CommandSender sender, String[] args) {
        List<String> argsList = Arrays.asList(args);
        return Stream.of("-disable_buy", "-health","-none")
                .filter(s -> !argsList.contains(s))
                .collect(Collectors.toList());
    }
}
