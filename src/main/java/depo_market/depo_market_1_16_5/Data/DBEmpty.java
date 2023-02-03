package depo_market.depo_market_1_16_5.Data;

import depo_market.depo_market_1_16_5.ItemStack.ItemEnchantData;
import depo_market.depo_market_1_16_5.ItemStack.ItemMenuSlot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DBEmpty implements DBInterface{

    private final ArrayList<ItemMenuSlot> MenuSlotList;//メインメニューのアイテムリスト
    private final ArrayList<Integer> TradeAmountList;//サブメニューに表示する数量リスト
    private final ArrayList<ItemEnchantData> itemEnchantList;//エンチャントアイテムのリスト
    private final ArrayList<ItemMenuSlot> InitialPriceList;//取引アイテムの全種類リスト
    private final Map<String, Float> InitialPriceMap;//取引アイテムの全種類の初期金額マップ

    public DBEmpty(){
        MenuSlotList = new ArrayList<>();//メインメニューのアイテムリスト
        TradeAmountList = new ArrayList<>();//サブメニューに表示する数量リスト
        itemEnchantList = new ArrayList<>();//エンチャントアイテムのリスト
        InitialPriceList = new ArrayList<>();//取引アイテムの全種類リスト
        InitialPriceMap = new HashMap<>();//取引アイテムの全種類の初期金額マップ

        for (int i = 1; i < MenuSlotList.size(); i++) {
            InitialPriceList.add(MenuSlotList.get(i));
        }
        InitialPriceList.addAll(itemEnchantList);
        for(ItemMenuSlot item : InitialPriceList){
            InitialPriceMap.put(item.getEnName(),item.getInitialPrice());
        }
    }

    /*
     * それぞれ、保持しているデータを返すメソッド
     */
    public ArrayList<ItemMenuSlot> getMenuSlotList(){
        return MenuSlotList;
    }
    public ArrayList<Integer> getTradeAmountList(){
        return TradeAmountList;
    }
    public ArrayList<ItemEnchantData> getItemEnchantList(){
        return itemEnchantList;
    }
    public ArrayList<ItemMenuSlot> getInitialPriceList(){
        return InitialPriceList;
    }
    public float getInitialPrice(String nameEn){
        return InitialPriceMap.get(nameEn);
    }
}
