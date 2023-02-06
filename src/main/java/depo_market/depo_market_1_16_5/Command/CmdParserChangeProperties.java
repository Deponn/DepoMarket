package depo_market.depo_market_1_16_5.Command;

import depo_market.depo_market_1_16_5.PropertiesAndConstant.Const;
import depo_market.depo_market_1_16_5.PropertiesAndConstant.MoneyDisAd;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class CmdParserChangeProperties implements CmdParser {

    private final boolean isSuccess;//パース成功したかどうか
    public final String PropertiesName;
    public final String Value;//値

    private CmdParserChangeProperties(boolean isSuccess, String propertiesName, String value) {
        this.isSuccess = isSuccess;
        this.Value = value;
        this.PropertiesName = propertiesName;
    }

    @Override
    public boolean isSuccess() {
        return isSuccess;
    }

    public static CmdParserChangeProperties parse(CommandSender sender, String[] args) {
        List<String> argsList = Arrays.asList(args);
        String DisAd = "-" + Const.DisadvantageProperties;
        String PrMoney = "-" + Const.PrizeMoneyProperties;
        String value;
        if (argsList.contains(DisAd)) {
            // 引数が何番目か取得し、番号を採用する
            int index = argsList.indexOf(DisAd);
            if (index + 1 >= argsList.size()) {
                // 引数の次がなかった場合、エラー
                sender.sendMessage(ChatColor.RED + "値が必要です" + DisAd + "<値>");
                return new CmdParserChangeProperties(false, null, null);
            }
            try {
                // チーム
                value = argsList.get(index + 1);
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "数値が不正です。" + DisAd + "<値>");
                return new CmdParserChangeProperties(false, null, null);
            }
            return new CmdParserChangeProperties(true, Const.DisadvantageProperties, value);
        } else if (argsList.contains(PrMoney)) {
            // 引数が何番目か取得し、番号を採用する
            int index = argsList.indexOf(PrMoney);
            if (index + 1 >= argsList.size()) {
                // 引数の次がなかった場合、エラー
                sender.sendMessage(ChatColor.RED + "値が必要です" + PrMoney + "<値>");
                return new CmdParserChangeProperties(false, null, null);
            }
            try {
                // チーム
                value = argsList.get(index + 1);
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "数値が不正です。" + PrMoney + "<値>");
                return new CmdParserChangeProperties(false, null, null);
            }
            return new CmdParserChangeProperties(true, Const.PrizeMoneyProperties, value);
        }
        return new CmdParserChangeProperties(false, null, null);
    }
}
