package depo_market.depo_market_1_16_5;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;

import java.util.List;
import java.util.ArrayList;

public final class Depo_Market_1_16_5 extends JavaPlugin implements TabCompleter,Listener{

    MarketOperator Operator;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        Operator = new MarketOperator(this);
        getLogger().info("Depo_Marketが有効化されました。");
    }

    @Override
    public void onDisable() {
        getLogger().info("Depo_Marketが無効化されました。");
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        // プレイヤーがコマンドを投入した際の処理...
        if (!(sender instanceof Player)) {
            // コマブロやコンソールからの実行の場合
            sender.sendMessage(ChatColor.DARK_GRAY + "このコマンドはプレイヤーのみが使えます。");
            return true;
        }
        Player player = (Player) sender;

        // コマンド処理...
        if (cmd.getName().equalsIgnoreCase("start_market")) {
            //コマンド引数を処理
            CommandParser parser = CommandParser.parse_start_market(sender, args);
            if (!parser.isSuccess) {
                // パース失敗
                return true;
            }
            return Operator.StartMarket(player);
        } else if (cmd.getName().equalsIgnoreCase("stop_market")) {
            //コマンド引数を処理
            CommandParser parser = CommandParser.parse_stop_market(sender, args);
            if (!parser.isSuccess) {
                // パース失敗
                return true;
            }
            return Operator.StopMarket(player,parser.delete_all);
        } else if (cmd.getName().equalsIgnoreCase("place_market")) {
            //コマンド引数を処理
            CommandParser parser = CommandParser.parse_place_market(sender, args);
            if (!parser.isSuccess) {
                // パース失敗
                return true;
            }
            return Operator.PlaceMarket(player);
        } else if (cmd.getName().equalsIgnoreCase("tax")) {
            //コマンド引数を処理
            CommandParser parser = CommandParser.parse_tax(sender, args);
            if (!parser.isSuccess) {
                // パース失敗
                return true;
            }
            return Operator.Tax(player);
        }
        return true;
    }

    @EventHandler
    public void onEntityClick(PlayerInteractEntityEvent e) {
        Operator.CustomerClick(e.getPlayer(),e.getRightClicked());
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (Operator.isMenu((Player) e.getWhoClicked())) {
            e.setCancelled(true);
            Operator.MenuClick((Player) e.getWhoClicked(),e.getCurrentItem(),e.getRawSlot());
        }
    }
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e){
        Operator.MenuClose((Player) e.getPlayer());
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("stop_market")) {
            return CommandParser.suggest_stop_market(sender, args);
        } else if (command.getName().equalsIgnoreCase("tax")) {
            return CommandParser.suggest_tax(sender, args);
        }else {
            return new ArrayList<String>();
        }
    }

}
