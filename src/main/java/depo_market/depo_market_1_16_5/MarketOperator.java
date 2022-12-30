package depo_market.depo_market_1_16_5;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MarketOperator {

    private boolean market_run_flag = false;
    private Map<String, ItemPrice> ItemDataList;
    private final DataBaseTradeItem dataBaseTradeItem;
    public MarketOperator(ArrayList<ItemMenuSlot> initialPriceList){
        this.ItemDataList = new HashMap<>();
        this.dataBaseTradeItem = new DataBaseTradeItem();
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
        float price = ChangePrice(nameEn,Amount, true);
        ItemDataList.get(nameEn).addAmountOfBought(Amount);
        return price;
    }
    public float sell(String nameEn,int Amount){
        float price = ChangePrice(nameEn, Amount, false);
        ItemDataList.get(nameEn).addAmountOfSold(Amount);
        return price;
    }
    public Map<String, ItemPrice> getData(){
        return ItemDataList;
    }

    private float ChangePrice(String nameEn,int Amount, boolean isBuy){
        float initialPrice = dataBaseTradeItem.getInitialPrice(nameEn);
        int buy;
        if(isBuy) {
            buy = 1;
        }else{
            buy = -1;
        }
        ItemPrice itemPrice = ItemDataList.get(nameEn);
        float price = itemPrice.getPrice();
        int difference = itemPrice.getAmountOfBought() - itemPrice.getAmountOfSold();
        int sum = itemPrice.getAmountOfBought() + itemPrice.getAmountOfSold();
        float WholePrice = 0;
        for (int i = 0; i < Amount; i++) {
            difference = difference + buy;
            sum = sum + 1;
            price =  price * (float)Math.exp(( 1 / 30000.0 ) * initialPrice * buy * Math.sqrt((double)Math.abs(difference) / (double)(sum)));
                    WholePrice = WholePrice + price;
        }
        itemPrice.SetPrice(price);
        return WholePrice;
    }
}
