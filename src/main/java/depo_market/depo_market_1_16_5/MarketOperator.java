package depo_market.depo_market_1_16_5;

import depo_market.depo_market_1_16_5.ItemStack.ItemMenuSlot;
import depo_market.depo_market_1_16_5.PropertiesAndConstant.Const;

import java.util.HashMap;
import java.util.Map;

/**
 * アイテムの値段を保持するオブジェクトを保持し、値段に関する処理を行うオブジェクト。
 */
public class MarketOperator {

    private final Map<String, ItemPrice> ItemDataList;//アイテムの値段を保持するオブジェクトのマップ
    private final MyOperators operators;

    public MarketOperator(MyOperators myOperators){
        this.operators = myOperators;
        ItemDataList = new HashMap<>();
        for (ItemMenuSlot Item : operators.DBTradeItem.getInitialPriceList()) {
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
        float price;
        if(operators.prop.PriceMoveRate == 0) {
            price = getWholePrice(nameEn, Amount);
        }else {
            price = ChangePrice(nameEn, Amount,true);
        }
        ItemDataList.get(nameEn).addAmountOfBought(Amount);
        return price;
    }

    //アイテムの売るとき取引量を記録。値段変動メソッドを呼び出す
    public float sell(String nameEn,int Amount){
        float price;
        if(operators.prop.PriceMoveRate == 0) {
            price = getWholePrice(nameEn, Amount);
        }else {
            price = ChangePrice(nameEn, Amount,false);
        }
        ItemDataList.get(nameEn).addAmountOfSold(Amount);
        return price;
    }

    //値段の変動を行なわない。
    private float getWholePrice(String nameEn, int Amount){
        ItemPrice itemPrice = ItemDataList.get(nameEn);
        float price = itemPrice.getPrice();
        return price * Amount;
    }

    //値段の変動を行う。変動した値段ごとに足していき、最終的な取引金額を返す。
    private float ChangePrice(String nameEn,int Amount, boolean isBuy){
        float initialPrice = operators.DBTradeItem.getInitialPrice(nameEn);
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
        //値段を取引ひとつごとに更新。
        for (int i = 0; i < Amount; i++) {
            difference = difference + buy;
            sum = sum + 1;
            //値段は勝ったら増えて売ったら減る。売買が偏るほど変動が激しく、取引量が多いほど変動が少ない。初期金額が多いほど貴重と考えて変動が多い。
            price =  price * (float)Math.exp(
                    operators.prop.PriceMoveRate * ( 1 / Const.PriceMoveRateBase) * Math.sqrt(initialPrice) * buy * Math.sqrt(
                            (double)Math.abs(difference) / (double)(sum)));
            WholePrice = WholePrice + price;
        }
        itemPrice.SetPrice(price);
        return WholePrice;
    }
}
