package depo_market.depo_market_1_16_5.PropertiesAndConstant;

import depo_market.depo_market_1_16_5.ItemDataBase.DBInterface;
import depo_market.depo_market_1_16_5.ItemDataBase.DBKojosen;
import depo_market.depo_market_1_16_5.ItemDataBase.DBList;

import java.util.ArrayList;
import java.util.List;

public class Const {
    public static final String CUSTOMER_NAME = "DpCustomer";
    public static final String ScoreBoardName = "DpMoney";
    public static final int RowSlotNum = 9;
    public static final int WholeSlotNum = 27;
    public static final float MONEY_HEALTH = 200000f;
    public static final float BoundOfBuy = 30000f;
    public static final double PriceMoveRateBase = 10000;
    public static final String PropertiesFileName = "DpMarket.properties";
    public static final String VersionPropName =  "Dp_Version";
    public static final String thisVersion = "3.4";
    public static final String DisadvantagePropName = "Disadvantage";
    public static final MoneyDisAd Default_DisAd = MoneyDisAd.Health;
    public static final String PrizeMoneyPropName = "PrizeMoney";
    public static final float Default_PrizeMoney = 5000f;
    public static final String BoundOfMoneyPropName = "BoundOfMoney";
    public static final float Default_BoundOfMoney = 200000f;
    public static final String PriceMoveRatePropName = "PriceMoveRate";
    public static final float Default_PriceMove = 0f;
    public static final String DBItemPropName = "DBItem";
    public static final String Default_DBItem = DBList.Kojosen2.getName();
    public static final String isTeamGamePropName = "isTeamGame";
    public static final boolean Default_isTeamGame = true;


    public static List<String> getPropNames(){
        List<String> PropNames = new ArrayList<>();
        PropNames.add(DisadvantagePropName);
        PropNames.add(PrizeMoneyPropName);
        PropNames.add(BoundOfMoneyPropName);
        PropNames.add(PriceMoveRatePropName);
        PropNames.add(DBItemPropName);
        PropNames.add(isTeamGamePropName);
        return PropNames;
    }

}
