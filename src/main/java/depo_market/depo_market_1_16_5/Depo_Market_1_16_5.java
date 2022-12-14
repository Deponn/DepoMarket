package depo_market.depo_market_1_16_5;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public final class Depo_Market_1_16_5 extends JavaPlugin {

    private int market;
    private boolean market_run_flag = false;
    private boolean market_exist_flag = false;

    @Override
    public void onEnable() {
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
            if (!market_run_flag) {
                if (!market_exist_flag) {
                    market = 1;
                    market_exist_flag = true;
                }
                market_run_flag = true;
            }else {
                getLogger().info("市場はすでに起動中です");
                return true;
            }

        } else if (cmd.getName().equalsIgnoreCase("stop_market")) {
            //コマンド引数を処理
            CommandParser parser = CommandParser.parse_stop_market(sender, args);
            if (!parser.isSuccess) {
                // パース失敗
                return true;
            }
            if(market_run_flag) {
                market_run_flag = false;
                if (market_exist_flag) {
                    if (parser.delete_all) {
                        market = 0;
                        market_exist_flag = false;
                    }
                }else{
                    getLogger().info("予期しないバグが発生して市場の生成されないまま動いていました。");
                    return true;
                }
            }else{
                if (market_exist_flag){
                        if (parser.delete_all) {
                            market = 0;
                            market_exist_flag = false;
                        }else {
                            getLogger().info("市場はすでに停止です");
                            return true;
                        }
                }else{
                    getLogger().info("市場はすでに停止です");
                    return true;
                }
            }

        } else if (cmd.getName().equalsIgnoreCase("place_market")) {
            //コマンド引数を処理
            CommandParser parser = CommandParser.parse_place_market(sender, args);
            if (!parser.isSuccess) {
                // パース失敗
                return true;
            }
            getLogger().info("商人を置きます");

        } else if (cmd.getName().equalsIgnoreCase("tax")) {
            //コマンド引数を処理
            CommandParser parser = CommandParser.parse_tax(sender, args);
            if (!parser.isSuccess) {
                // パース失敗
                return true;
            }
            if (market_run_flag) {
                getLogger().info("徴税します");
            }else {
                getLogger().info("市場が動いていません");
            }
        }
        return true;
    }
}
