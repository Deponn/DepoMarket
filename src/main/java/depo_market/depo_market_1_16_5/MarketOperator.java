package depo_market.depo_market_1_16_5;

import depo_market.depo_market_1_16_5.ItemStack.ItemMenuSlot;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * アイテムの値段を保持するオブジェクトを保持し、値段に関する処理を行うオブジェクト。
 */
public class MarketOperator {

    private final Map<String, ItemPrice> ItemDataList;//アイテムの値段を保持するオブジェクトのマップ

    public MarketOperator(ArrayList<ItemMenuSlot> initialPriceList){
        ItemDataList = new HashMap<>();
        for (ItemMenuSlot Item : initialPriceList) {
            this.ItemDataList.put(Item.getEnName(), new ItemPrice(Item.getInitialPrice()));
        }
    }
    public void loadData(Map<String, ItemPrice> data){
        for(String key : data.keySet()) {
            ItemDataList.put(key, data.get(key));
        }
    }
    public Map<String, ItemPrice> getData(){
        return ItemDataList;
    }

    public float getPrice(String nameEn){
        return ItemDataList.getOrDefault(nameEn,new ItemPrice(0f)).getPrice();
    }


    //アイテムの買うとき取引量を記録。値段変動メソッドを呼び出す
    public float buy(String nameEn,int Amount){
        float price = getWholePrice(nameEn,Amount);
        ItemDataList.get(nameEn).addAmountOfBought(Amount);
        return price;
    }

    //アイテムの売るとき取引量を記録。値段変動メソッドを呼び出す
    public float sell(String nameEn,int Amount){
        float price = getWholePrice(nameEn, Amount);
        ItemDataList.get(nameEn).addAmountOfSold(Amount);
        return price;
    }

    //値段の変動を行なわない。
    private float getWholePrice(String nameEn, int Amount){
        ItemPrice itemPrice = ItemDataList.get(nameEn);
        float price = itemPrice.getPrice();
        return price * Amount;
    }
}
