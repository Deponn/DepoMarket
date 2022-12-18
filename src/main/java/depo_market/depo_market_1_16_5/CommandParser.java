package depo_market.depo_market_1_16_5;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * コマンドを処理するパーサークラス
 */
public class CommandParser {

    public final boolean isSuccess;//パース成功したかどうか
    public boolean delete_all = false;//データを全削除するかどうか
    public int amount_of_money = 0;//金額
    public int team_name = 0;//相手のチーム名


    // オブジェクト生成。パースが成功しているかを受け取る
    private CommandParser(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    /**
     * コマンドのTAB補完候補を返す
     *
     * @param sender コマンド送信者
     * @param args   引数
     * @return コマンド補完候補
     */
    public static List<String> suggest_stop_market(CommandSender sender, String[] args) {
        List<String> argsList = Arrays.asList(args);
        return Stream.of("delete","all")
                    .filter(s -> !argsList.contains(s))
                    .collect(Collectors.toList());
    }
    public static List<String> suggest_tax(CommandSender sender, String[] args) {
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

    /**
     * コマンドをパースする
     *
     * @param sender コマンド送信者
     * @param args   引数
     * @return コマンド補完候補
     */
    public static CommandParser parse_start_market(CommandSender sender, String[] args) {
        return new CommandParser(true);
    }
    public static CommandParser parse_stop_market(CommandSender sender, String[] args) {
        boolean delete_data = false;
        List<String> argsList = Arrays.asList(args);

        if (argsList.contains("delete")) {
            if (argsList.contains("all")) {
                delete_data = true;
            }else {
                sender.sendMessage(ChatColor.RED + "deleteとallの両方が必要です");
                return new CommandParser(false);
            }
        } else if (argsList.contains("all")) {
            sender.sendMessage(ChatColor.RED + "deleteとallの両方が必要です");
            return new CommandParser(false);
        }
        CommandParser Me = new CommandParser((true));
        Me.delete_all = delete_data;
        return Me;
    }
    public static CommandParser parse_place_market(CommandSender sender, String[] args) {
        return new CommandParser(true);
    }
    public static CommandParser parse_tax(CommandSender sender, String[] args) {
        List<String> argsList = Arrays.asList(args);

        int team_name = 0;
        int amount_of_money = 0;

        if (argsList.contains("-team")) {
            // 引数が何番目か取得し、若い番号を採用する
            int index = argsList.indexOf("-team");
            if (index + 1 >= argsList.size()) {
                // 引数の次がなかった場合、エラー
                sender.sendMessage(ChatColor.RED + "数値が必要です。 -team <数字>");
                return new CommandParser(false);
            }
            try {
                // チーム番号
                team_name = Integer.parseInt(argsList.get(index + 1));
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "数値が不正です。 -team <数字>");
                return new CommandParser(false);
            }
        }else {
            return new CommandParser(false);
        }
        if (argsList.contains("-amount")) {
            // 引数が何番目か取得し、若い番号を採用する
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
}
