package depo_market.depo_market_1_16_5.PropertiesAndConstant;

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
    public MyProperties() {
        MoneyDisAd disadvantage = Const.Default_DisAd;
        float prizeMoney = Const.Default_PrizeMoney;
        File file = new File(Const.PropertiesFileName);
        Properties settings = new Properties();
        try {
            if (file.exists()) {
                settings = load(file);
                if (Objects.equals(settings.getProperty("Dp_Version"), Const.thisVersion)) {
                    disadvantage = MoneyDisAd.getEnum(settings.getProperty(Const.DisadvantageProperties));
                    prizeMoney = Float.parseFloat(settings.getProperty(Const.PrizeMoneyProperties));
                }
            }
            this.Disadvantage = disadvantage;
            this.PrizeMoney = prizeMoney;
            settings.setProperty("Dp_Version", Const.thisVersion);
            settings.setProperty(Const.DisadvantageProperties, Disadvantage.getString());
            settings.setProperty(Const.PrizeMoneyProperties, String.valueOf(PrizeMoney));
            save(file,settings);
        } catch (IOException e) {
            Bukkit.getLogger().info(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void save(File file,Properties settings) throws IOException {
        try (FileOutputStream out = new FileOutputStream(file)) {
            settings.store(out, "Properties");
        }
    }
    private Properties load(File file) throws IOException {
        // Properties の読み込み
        Properties settings = new Properties();
        try (FileInputStream in = new FileInputStream(file)) {
            settings.load(in);
        }
        return settings;
    }
}
