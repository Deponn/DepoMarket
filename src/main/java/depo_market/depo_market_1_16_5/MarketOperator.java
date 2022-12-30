package depo_market.depo_market_1_16_5;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MarketOperator {

    private boolean market_run_flag = false;
    private Map<String, ItemPrice> ItemDataList;
    public MarketOperator(ArrayList<ItemMenuSlot> initialPriceList){
        this.ItemDataList = new HashMap<>();
        for(ItemMenuSlot Item : initialPriceList){
            this.ItemDataList.put(Item.getEnName(),new ItemPrice(Item.getInitialPrice()));
        }
    }
    public void loadData(boolean isRun, Map<String, ItemPrice> data){
        market_run_flag = isRun;
        for(String key : data.keySet()) {
            ItemDataList.put(key, data.get(key));
        }
    }
    public void Initialize(ArrayList<ItemMenuSlot> initialPriceList) {
        ItemDataList = new HashMap<>();
        for (ItemMenuSlot Item : initialPriceList) {
            this.ItemDataList.put(Item.getEnName(), new ItemPrice(Item.getInitialPrice()));
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
    public float getPrice(String nameEn){
        return ItemDataList.getOrDefault(nameEn,new ItemPrice(0f)).getPrice();
    }
    public boolean getMarketState(){
        return market_run_flag;
    }
    public float buy(String nameEn,int Amount){
        ItemDataList.get(nameEn).addAmountOfBought(Amount);
        ChangePrice(nameEn,Amount);
        float price = ItemDataList.get(nameEn).getPrice();
        return price * Amount;
    }
    public float sell(String nameEn,int Amount){
        ItemDataList.get(nameEn).addAmountOfSold(Amount);
        ChangePrice(nameEn, - Amount);
        float price = ItemDataList.get(nameEn).getPrice();
        return price * Amount;
    }
    public Map<String, ItemPrice> getData(){
        return ItemDataList;
    }

    private void ChangePrice(String nameEn,int Amount){
        float newPrice;
        ItemPrice itemPrice = ItemDataList.get(nameEn);
        float price = itemPrice.getPrice();
        int difference = Math.abs(itemPrice.getAmountOfBought() - itemPrice.getAmountOfSold());
        int sum = itemPrice.getAmountOfBought() + itemPrice.getAmountOfSold() + 1;
        newPrice = (float) (price * (1 + ( 1 / 128000.0 ) * Amount * price * Math.sqrt((double)difference / (double)sum)));
        itemPrice.SetPrice(newPrice);
    }
}
