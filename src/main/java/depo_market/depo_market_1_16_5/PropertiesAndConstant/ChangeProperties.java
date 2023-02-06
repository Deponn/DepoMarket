package depo_market.depo_market_1_16_5.PropertiesAndConstant;

import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class ChangeProperties {

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
        MoneyDisAd disadvantage;
        float PrizeMoney;
        File file = new File(Const.PropertiesFileName);
        Properties settings;
        try {
            if (file.exists()) {
                settings = load(file);
                if (Objects.equals(settings.getProperty("Dp_Version"), Const.thisVersion)) {
                    disadvantage = MoneyDisAd.getEnum(settings.getProperty(Const.DisadvantageProperties));
                    PrizeMoney = Float.parseFloat(settings.getProperty(Const.PrizeMoneyProperties));

                    settings.setProperty("Dp_Version", Const.thisVersion);
                    settings.setProperty(Const.DisadvantageProperties, disadvantage.getString());
                    settings.setProperty(Const.PrizeMoneyProperties, String.valueOf(PrizeMoney));

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
