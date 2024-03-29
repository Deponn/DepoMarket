package depo_market.depo_market_1_16_5.Command;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

/**
 * コマンドを処理するパーサークラス
 */
public class CmdParserSetPoint implements CmdParser {

    private final boolean isSuccess;//パース成功したかどうか
    public final Boolean noXYZ;
    public final Integer X;
    public final Integer Y;
    public final Integer Z;


    // オブジェクト生成。パースが成功しているかを受け取る
    private CmdParserSetPoint(boolean isSuccess,Boolean noXYZ ,Integer X,Integer Y,Integer Z) {
        this.isSuccess = isSuccess;
        this.noXYZ = noXYZ;
        this.X = X;
        this.Y = Y;
        this.Z = Z;
    }

    @Override
    public boolean isSuccess() {
        return isSuccess;
    }

    public static CmdParserSetPoint parse(CommandSender sender, String[] args) {
        List<String> argsList = Arrays.asList(args);
        int x;
        int y;
        int z;
        if(argsList.size() == 0){
            return new CmdParserSetPoint(true,true,null,null,null);
        }
        if (argsList.size() != 3) {
            // 引数の次がなかった場合、エラー
            sender.sendMessage(ChatColor.RED + "数値が必要です。 <X> <Y> <Z>");
            return new CmdParserSetPoint(false,false,null,null,null);
        }
        try {
            // 座標
            x = Integer.parseInt(argsList.get(0));
            y = Integer.parseInt(argsList.get(1));
            z = Integer.parseInt(argsList.get(2));
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "数値が不正です。 <X> <Y> <Z>");
            return new CmdParserSetPoint(false,false,null,null,null);
        }
        return new CmdParserSetPoint(true,false,x,y,z);
    }
}
