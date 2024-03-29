package depo_market.depo_market_1_16_5.PropertiesAndConstant;

public enum MoneyDisAd {
    DisableBuy("disable_buy"),
    Health("health"),
    None("none");

    private final String Name;

    // コンストラクタを定義
    MoneyDisAd(String name) {
        this.Name = name;
    }

    // メソッド
    public String getString() {
        return this.Name;
    }

    public static MoneyDisAd getEnum(String name){
        for(MoneyDisAd moneyDisAd : MoneyDisAd.values()){
            if(moneyDisAd.getString().equals(name)){
                return moneyDisAd;
            }
        }
        return null;
    }
}
