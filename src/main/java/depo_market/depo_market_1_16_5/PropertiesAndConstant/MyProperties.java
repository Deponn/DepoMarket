package depo_market.depo_market_1_16_5.PropertiesAndConstant;

import depo_market.depo_market_1_16_5.ItemDataBase.DBInterface;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class MyProperties {

    public final MoneyDisAd Disadvantage;
    public final float PrizeMoney;
    public final float BoundOfMoney;
    public final float PriceMoveRate;
    public final String ItemDataBase;
    public final boolean isTeamGame;

    public MyProperties() {
        File file = new File(Const.PropertiesFileName);
        Properties settings = new Properties();

        try {
            MoneyDisAd disadvantage = Const.Default_DisAd;
            float prizeMoney = Const.Default_PrizeMoney;
            float boundOfMoney = Const.Default_BoundOfMoney;
            float priceMoveRate = Const.Default_PriceMove;
            String itemDataBase = Const.Default_DBItem;
            boolean teamGame = Const.Default_isTeamGame;
            boolean flag = false;
            if (file.exists()) {
                settings = load(file);
                if (Objects.equals(settings.getProperty(Const.VersionPropName), Const.thisVersion)) {
                    disadvantage = MoneyDisAd.getEnum(settings.getProperty(Const.DisadvantagePropName));
                    prizeMoney = Float.parseFloat(settings.getProperty(Const.PrizeMoneyPropName));
                    boundOfMoney = Float.parseFloat(settings.getProperty(Const.BoundOfMoneyPropName));
                    priceMoveRate = Float.parseFloat(settings.getProperty(Const.PriceMoveRatePropName));
                    itemDataBase = settings.getProperty(Const.DBItemPropName);
                    teamGame = Boolean.parseBoolean(settings.getProperty(Const.isTeamGamePropName));
                    flag = true;
                }
            }
            if(!flag){
                settings.setProperty(Const.VersionPropName, Const.thisVersion);
                settings.setProperty(Const.DisadvantagePropName, disadvantage.getString());
                settings.setProperty(Const.PrizeMoneyPropName, String.valueOf(prizeMoney));
                settings.setProperty(Const.BoundOfMoneyPropName, String.valueOf(boundOfMoney));
                settings.setProperty(Const.PriceMoveRatePropName,String.valueOf(priceMoveRate));
                settings.setProperty(Const.DBItemPropName,itemDataBase);
                settings.setProperty(Const.isTeamGamePropName,String.valueOf(teamGame));
                save(file,settings);
            }
            this.Disadvantage = disadvantage;
            this.PrizeMoney = prizeMoney;
            this.BoundOfMoney = boundOfMoney;
            this.PriceMoveRate = priceMoveRate;
            this.ItemDataBase = itemDataBase;
            this.isTeamGame = teamGame;


        } catch (IOException e) {
            Bukkit.getLogger().info(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static void save(File file,Properties settings) throws IOException {
        try (FileOutputStream out = new FileOutputStream(file)) {
            settings.store(out, "Properties");
        }
    }
    private static Properties load(File file) throws IOException {
        // Properties の読み込み
        Properties settings = new Properties();
        try (FileInputStream in = new FileInputStream(file)) {
            settings.load(in);
        }
        return settings;
    }

    public static void PropertiesChange(String PropertiesName , String Value){
        File file = new File(Const.PropertiesFileName);
        Properties settings;
        try {
            if (file.exists()) {
                settings = load(file);
                if (Objects.equals(settings.getProperty(Const.VersionPropName), Const.thisVersion)) {
                    settings.setProperty(PropertiesName,Value);
                    save(file,settings);
                }
            }
        } catch (IOException e) {
            Bukkit.getLogger().info(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
