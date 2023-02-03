package depo_market.depo_market_1_16_5.Command;

import depo_market.depo_market_1_16_5.MoneyDisAd;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class CmdParserDisAd implements CmdParser{

    public final boolean isSuccess;//パース成功したかどうか

    public final MoneyDisAd disadvantage;//デバフの種類

    private CmdParserDisAd(boolean isSuccess,MoneyDisAd disadvantage){
        this.isSuccess = isSuccess;
        this.disadvantage = disadvantage;
    }

    @Override
    public boolean isSuccess() {
        return isSuccess;
    }

    public static CmdParserDisAd parse(CommandSender sender, String[] args){
        List<String> argsList = Arrays.asList(args);
        MoneyDisAd disadvantage;
        if (argsList.contains("-" + MoneyDisAd.DisableBuy.getString())) {
            disadvantage = MoneyDisAd.DisableBuy;
        }else if(argsList.contains("-" + MoneyDisAd.Health.getString())){
            disadvantage = MoneyDisAd.Health;
        }else if(argsList.contains("-" + MoneyDisAd.None.getString())){
            disadvantage = MoneyDisAd.None;
        }else {
            sender.sendMessage(ChatColor.RED + "設定を入力してください");
            return new CmdParserDisAd(false,null);
        }
        return new CmdParserDisAd(true,disadvantage);
    }
}
