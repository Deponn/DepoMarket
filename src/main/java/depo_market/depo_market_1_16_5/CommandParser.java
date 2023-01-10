package depo_market.depo_market_1_16_5;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

/**
 * コマンドを処理するパーサークラス
 */
public class CommandParser {

    public final boolean isSuccess;//パース成功したかどうか
    public int amount_of_money = 0;//金額
    public String team_name = "none";//相手のチーム名
    public String disadvantage = "none";//デバフの種類
    public int X;
    public int Y;
    public int Z;


    // オブジェクト生成。パースが成功しているかを受け取る
    private CommandParser(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    /**
     * お金をあげたり奪ったりするコマンドをパースする
     *
     * @param sender コマンド送信者
     * @param args   引数
     * @return コマンド補完候補
     */
    public static CommandParser parse_give_money(CommandSender sender, String[] args) {
        List<String> argsList = Arrays.asList(args);

        String team_name = "none";
        int amount_of_money = 0;

        if (argsList.contains("-team")) {
            // 引数が何番目か取得し、番号を採用する
            int index = argsList.indexOf("-team");
            if (index + 1 >= argsList.size()) {
                // 引数の次がなかった場合、エラー
                sender.sendMessage(ChatColor.RED + "数値が必要です。 -team <数字>");
                return new CommandParser(false);
            }
            try {
                // チーム
                team_name = argsList.get(index + 1);
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "数値が不正です。 -team <数字>");
                return new CommandParser(false);
            }
        }else {
            return new CommandParser(false);
        }
        if (argsList.contains("-amount")) {
            // 引数が何番目か取得し、番号を採用する
            int index = argsList.indexOf("-amount");
            if (index + 1 >= argsList.size()) {
                // 引数の次がなかった場合、エラー
                sender.sendMessage(ChatColor.RED + "数値が必要です。 -amount <数字>");
                return new CommandParser(false);
            }
            try {
                // 金額
                amount_of_money = Integer.parseInt(argsList.get(index + 1));
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "数値が不正です。 -amount <数字>");
                return new CommandParser(false);
            }
        }else {
            return new CommandParser(false);
        }
        CommandParser Me = new CommandParser(true);
        Me.team_name = team_name;
        Me.amount_of_money = amount_of_money;
        return Me;
    }
    /**
     * デバフコマンドをパースする
     *
     * @param sender コマンド送信者
     * @param args   引数
     * @return コマンド補完候補
     */
    public static CommandParser parse_disadvantage(CommandSender sender, String[] args) {
        List<String> argsList = Arrays.asList(args);
        String disadvantage;
        if (argsList.contains("-disable_buy")) {
            disadvantage = "disable_buy";
        }else if(argsList.contains("-health")){
            disadvantage = "health";
        }else if(argsList.contains("-none")){
            disadvantage = "none";
        }else {
            sender.sendMessage(ChatColor.RED + "設定を決めてください -disable_buy -health -none");
            return new CommandParser(false);
        }
        CommandParser Me = new CommandParser(true);
        Me.disadvantage = disadvantage;
        return Me;
    }
    public static CommandParser parse_SetPointCustomer(CommandSender sender, String[] args) {
        List<String> argsList = Arrays.asList(args);
        int x;
        int y;
        int z;
        if (argsList.size() != 3) {
            // 引数の次がなかった場合、エラー
            sender.sendMessage(ChatColor.RED + "数値が必要です。 <X> <Y> <Z>");
            return new CommandParser(false);
        }
        try {
            // 金額
            x = Integer.parseInt(argsList.get(0));
            y = Integer.parseInt(argsList.get(1));
            z = Integer.parseInt(argsList.get(2));
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "数値が不正です。 <X> <Y> <Z>");
            return new CommandParser(false);
        }
        CommandParser Me = new CommandParser(true);
        Me.X = x;
        Me.Y = y;
        Me.Z = z;
        return Me;
    }
}
