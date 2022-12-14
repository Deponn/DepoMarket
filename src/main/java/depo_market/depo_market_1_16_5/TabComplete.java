package depo_market.depo_market_1_16_5;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

public class TabComplete implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("stop_market")) {
            return CommandParser.suggest_stop_market(sender, args);
        } else if (command.getName().equalsIgnoreCase("tax")) {
            return CommandParser.suggest_tax(sender, args);
        }else {
            return CommandParser.suggest_none(sender, args);
        }
    }
}
