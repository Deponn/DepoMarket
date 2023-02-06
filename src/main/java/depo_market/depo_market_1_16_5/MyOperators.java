package depo_market.depo_market_1_16_5;

import depo_market.depo_market_1_16_5.ItemDataBase.DBInterface;
import depo_market.depo_market_1_16_5.ItemDataBase.DBKojosen;
import depo_market.depo_market_1_16_5.PropertiesAndConstant.MyProperties;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MyOperators {

    public final MarketOperator market;
    public final TeamOperator teamOp;
    public final DBInterface DBTradeItem;
    private final Map<UUID, PlayerOperator> playerOperatorMap;
    public final MyProperties prop;

    public MyOperators() {
        this.DBTradeItem = new DBKojosen();
        this.market = new MarketOperator(DBTradeItem.getInitialPriceList());
        this.teamOp = new TeamOperator();
        this.playerOperatorMap = new HashMap<>();
        this.prop = new MyProperties();
    }

    public void addPlayerOperator(Player player){
        UUID playerID = player.getUniqueId();
        if (!playerOperatorMap.containsKey(playerID)) {
            playerOperatorMap.put(playerID, new PlayerOperator(playerID, this));
        }
    }
    public PlayerOperator getPlayerOperator(Player player){
        if (playerOperatorMap.containsKey(player.getUniqueId())) {
            return playerOperatorMap.get(player.getUniqueId());
        }
        return null;
    }
}
