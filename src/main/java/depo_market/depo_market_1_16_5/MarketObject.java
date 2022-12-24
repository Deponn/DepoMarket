package depo_market.depo_market_1_16_5;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MarketObject {

    private boolean market_run_flag = false;
    private Map<String, ItemPrice> ItemDataList;
    public MarketObject(ArrayList<ItemMenuSlot> initialPriceList){
        this.ItemDataList = new HashMap<>();
        for(ItemMenuSlot Item : initialPriceList){
            this.ItemDataList.put(Item.getEnName(),new ItemPrice(Item.getInitialPrice()));
        }
    }
    public void loadData(){

    }
    public void Initialize(Player player,ArrayList<ItemMenuSlot> initialPriceList){
        if (!market_run_flag) {
            ItemDataList = new HashMap<>();
            for(ItemMenuSlot Item : initialPriceList){
                this.ItemDataList.put(Item.getEnName(),new ItemPrice(Item.getInitialPrice()));
            }
            player.sendMessage("市場を初期化する");
        }else {
            player.sendMessage("市場が動いてる間は初期化できません");
        }
    }
    public void StartMarket() {
        if (!market_run_flag) {
            market_run_flag = true;
        }
    }
    public void StartMarket(Player player) {
        if (!market_run_flag) {
            player.sendMessage("市場開始");
            market_run_flag = true;
        } else {
            player.sendMessage("市場はすでに起動中です");
        }
    }
    public void StopMarket(Player player){
        if(market_run_flag) {
            market_run_flag = false;
            player.chat("市場停止");
        }else {
            player.sendMessage("市場はすでに停止です");
        }
    }

    public boolean getMarketState(){
        return market_run_flag;
    }
    public float buy(Material material,int Amount){
        float money = 0f;
        return money;
    }
    public float sell(Material material,int Amount){
        float money = 0f;
        return money;
    }
    public Map<String, ItemPrice> getData(){
        return ItemDataList;
    }


}
