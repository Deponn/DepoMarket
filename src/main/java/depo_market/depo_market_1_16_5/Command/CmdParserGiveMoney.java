package depo_market.depo_market_1_16_5.Command;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class CmdParserGiveMoney implements CmdParser{
    public final boolean isSuccess;//パース成功したかどうか
    public final Integer amount_of_money;//金額
    public final String team_name;//相手のチーム名

    private CmdParserGiveMoney(boolean isSuccess,Integer amount_of_money,String team_name){
        this.isSuccess = isSuccess;
        this.amount_of_money = amount_of_money;
        this.team_name = team_name;
    }

    @Override
    public boolean isSuccess() {
        return isSuccess;
    }

    public static CmdParserGiveMoney Parse(CommandSender sender, String[] args) {
        List<String> argsList = Arrays.asList(args);
        String team_name = "none";
        int amount_of_money = 0;

        if (argsList.contains("-team")) {
            // 引数が何番目か取得し、番号を採用する
            int index = argsList.indexOf("-team");
            if (index + 1 >= argsList.size()) {
                // 引数の次がなかった場合、エラー
                sender.sendMessage(ChatColor.RED + "数値が必要です。 -team <数字>");
                return new CmdParserGiveMoney(false,null,null);
            }
            try {
                // チーム
                team_name = argsList.get(index + 1);
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "数値が不正です。 -team <数字>");
                return new CmdParserGiveMoney(false,null,null);
            }
        }else {
            return new CmdParserGiveMoney(false,null,null);
        }
        if (argsList.contains("-amount")) {
            // 引数が何番目か取得し、番号を採用する
            int index = argsList.indexOf("-amount");
            if (index + 1 >= argsList.size()) {
                // 引数の次がなかった場合、エラー
                sender.sendMessage(ChatColor.RED + "数値が必要です。 -amount <数字>");
                return new CmdParserGiveMoney(false,null,null);
            }
            try {
                // 金額
                amount_of_money = Integer.parseInt(argsList.get(index + 1));
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "数値が不正です。 -amount <数字>");
                return new CmdParserGiveMoney(false,null,null);
            }
        }else {
            return new CmdParserGiveMoney(false,null,null);
        }

        return new CmdParserGiveMoney(true,amount_of_money,team_name);
    }
}
