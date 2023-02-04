package depo_market.depo_market_1_16_5;

import depo_market.depo_market_1_16_5.Data.DBInterface;
import depo_market.depo_market_1_16_5.Data.DBKojosen;

import java.util.HashMap;
import java.util.Map;

public class MyOperators {

    public final MarketOperator market;
    public final TeamOperator teamOperator;
    public final DBInterface dataBaseTradeItem;
    public final Map<String, PlayerOperator> playerOperatorMap;

    public MyOperators() {
        this.dataBaseTradeItem = new DBKojosen();
        this.market = new MarketOperator(dataBaseTradeItem.getInitialPriceList());
        this.teamOperator = new TeamOperator();
        this.playerOperatorMap = new HashMap<>();
    }
}
