package depo_market.depo_market_1_16_5.Data;

import depo_market.depo_market_1_16_5.ItemStack.ItemEnchantData;
import depo_market.depo_market_1_16_5.ItemStack.ItemMenuSlot;

import java.util.ArrayList;

public interface DBInterface {

    /*
     * それぞれ、保持しているデータを返すメソッド
     */
    ArrayList<ItemMenuSlot> getMenuSlotList();
    ArrayList<Integer> getTradeAmountList();
    ArrayList<ItemEnchantData> getItemEnchantList();
    ArrayList<ItemMenuSlot> getInitialPriceList();
    float getInitialPrice(String nameEn);
}
