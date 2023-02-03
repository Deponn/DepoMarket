package depo_market.depo_market_1_16_5;

public enum MoneyDisAdvantage {
    DisableBuy("disable_buy"),
    Health("health"),
    None("none");

    private final String Name;

    // コンストラクタを定義
    private MoneyDisAdvantage(String name) {
        this.Name = name;
    }

    // メソッド
    public String getString() {
        return this.Name;
    }

}
