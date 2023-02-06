package depo_market.depo_market_1_16_5.Command;

import depo_market.depo_market_1_16_5.PropertiesAndConstant.Const;
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
        List<String> Props = Const.getPropNames();
        for (String prop : Props){
            String value;
            String name = "-" + prop;
            if (argsList.contains(name)) {
                // 引数が何番目か取得し、番号を採用する
                int index = argsList.indexOf(name);
                if (index + 1 >= argsList.size()) {
                    // 引数の次がなかった場合、エラー
                    sender.sendMessage(ChatColor.RED + "値が必要です" + name + "<値>");
                    return new CmdParserChangeProperties(false, null, null);
                }
                try {
                    // チーム
                    value = argsList.get(index + 1);
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + "数値が不正です。" + name + "<値>");
                    return new CmdParserChangeProperties(false, null, null);
                }
                return new CmdParserChangeProperties(true, prop, value);
            }
        }
        return new CmdParserChangeProperties(false, null, null);
    }
}
