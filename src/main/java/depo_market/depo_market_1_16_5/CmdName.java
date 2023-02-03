package depo_market.depo_market_1_16_5;

public enum CmdName {


    EnablePlugin("DpEnableDpPlugin"),
    DisablePlugin("DpDisableDpPlugin"),
    InitializeMarket("DpInitializeMarket"),
    StartMarket("DpStartMarket"),
    StopMarket("DpStopMarket"),
    PlaceCustomer("DpPlaceCustomer"),
    SetPointCustomer("DpSetPointCustomer"),
    KillAllCustomer("DpKillAllCustomer"),
    GiveMoney("DpGiveMoney"),
    SetDisadvantage("DpSetDisadvantage"),
    LookTeams("DpLookTeams"),
    NewTeam("DpNewTeam"),
    LookScore("DpLookScore");

    private final String Command;

    // コンストラクタを定義
    private CmdName(String Command) {
        this.Command = Command;
    }

    // メソッド
    public String getCmd() {
        return this.Command;
    }
}

