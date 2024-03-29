package depo_market.depo_market_1_16_5;

import depo_market.depo_market_1_16_5.ItemStack.ItemEnchantData;
import depo_market.depo_market_1_16_5.ItemStack.ItemMenuSlot;
import depo_market.depo_market_1_16_5.ItemStack.ItemStackData;
import depo_market.depo_market_1_16_5.ItemStack.ItemSubMenuSlot;
import depo_market.depo_market_1_16_5.PropertiesAndConstant.Const;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;

/**
 * メニューを作成するオブジェクト。
 */
public class MenuMaker {

    private static final Material NoneMaterial = Material.GLASS_PANE;
    private static final Material BackMaterial = Material.BOOK;
    private static final Material CheckMaterial = Material.CHEST;

    //メインメニュー生成。取引するアイテムを選ぶためのもの。取引アイテムを並べてあとは押せないガラス。所持金額。戻る。
    // 押したアイテムは押したスロット番号で処理。アイテムにカーソルを合わせると値段が表示
    public static Inventory MainMenu(ArrayList<ItemMenuSlot> MenuSlots, float Money, MarketOperator market){
        final Inventory MainMenu = Bukkit.createInventory(null, Const.WholeSlotNum, "取引メニュー");
        final int MenuSlotsNum = MenuSlots.size();
        final ArrayList<ItemStackData> ItemMenuSlotList =  new ArrayList<>();
        ItemStackData itemStackData;
        for (ItemMenuSlot item : MenuSlots){
            itemStackData = new ItemStackData(item);
            itemStackData.setLore("値段", Math.round(market.getPrice(item.getEnName())) + "円");
            ItemMenuSlotList.add(itemStackData);
        }
        for (int i = MenuSlotsNum; i < Const.WholeSlotNum - 2; i++) {
            ItemMenuSlotList.add(new ItemStackData(NoneMaterial, "押せません"));
        }
        itemStackData = new ItemSubMenuSlot(CheckMaterial, "チームの所持金",1);
        itemStackData.setLore("チームの所持金", Math.round(Money) + "円");
        ItemMenuSlotList.add(itemStackData);
        itemStackData = new ItemSubMenuSlot(BackMaterial, "戻る",1);
        ItemMenuSlotList.add(itemStackData);
        int i = 0;
        for (ItemStackData itemMenuSlot : ItemMenuSlotList) {
            MainMenu.setItem(i, itemMenuSlot.getItem());
            i++;
        }
        return MainMenu;
    }

    //サブ生成。取引するアイテムの量を選ぶためのもの。一段目が買い二段目売り。所持金額。戻る。押したアイテムは押したスロットで
    public static Inventory SubMenu(Material material, String nameJp, String nameEn,ArrayList<Integer> TradeAmountList,float Money,MarketOperator market){
        final Inventory submenu = Bukkit.createInventory(null, Const.WholeSlotNum, "取引メニュー");
        final ArrayList<ItemSubMenuSlot> subItemMenuSlotList = new ArrayList<>();
        ItemSubMenuSlot Item;
        int counter = 0;
        for(Integer Amount : TradeAmountList) {
            Item = new ItemSubMenuSlot(material, nameJp,Amount);
            Item.setLore("買う", "価格:" + Math.round(market.getPrice(nameEn) * Amount) + "円");
            if(Amount == 1 || Math.round(market.getPrice(nameEn) * Amount) < Const.BoundOfBuy) {
                subItemMenuSlotList.add(Item);
                counter += 1;
            }
        }
        for (int i = counter; i< Const.RowSlotNum; i++) {
            subItemMenuSlotList.add(new ItemSubMenuSlot(NoneMaterial, "押せません",1));
        }
        counter = 0;
        for(Integer Amount : TradeAmountList) {
            Item = new ItemSubMenuSlot(material, nameJp,Amount);
            Item.setLore("売る", "価格:" + Math.round(market.getPrice(nameEn) * Amount) + "円");
            if(Amount == 1 || Math.round(market.getPrice(nameEn) * Amount) < Const.BoundOfBuy) {
                subItemMenuSlotList.add(Item);
                counter = counter + 1;
            }
        }
        for (int i = counter + Const.RowSlotNum; i< Const.WholeSlotNum - 2; i++) {
            subItemMenuSlotList.add(new ItemSubMenuSlot(NoneMaterial, "押せません",1));
        }
        Item = new ItemSubMenuSlot(CheckMaterial, "チームの所持金",1);
        Item.setLore("チームの所持金", Math.round(Money) + "円");
        subItemMenuSlotList.add(Item);

        Item = new ItemSubMenuSlot(BackMaterial, "戻る",1);
        subItemMenuSlotList.add(Item);

        int i = 0;
        for (ItemStackData itemMenuSlot : subItemMenuSlotList) {
            submenu.setItem(i, itemMenuSlot.getItem());
            i++;
        }
        return submenu;
    }

    //サブ生成。エンチャントアイテム選ぶためのものを別に生成。一段目が買い二段目売り。所持金額。戻る。押したアイテムは押したスロットで
    public static Inventory EnchantMenu(ArrayList<ItemEnchantData> itemEnchantList, float Money, MarketOperator market){
        final Inventory EnchantMenu = Bukkit.createInventory(null, Const.WholeSlotNum, "エンチャントアイテム取引メニュー");
        final ArrayList<ItemStackData> enchantItemMenuSlotList = new ArrayList<>();
        ItemStackData Item;
        for(ItemEnchantData Item_Enchant : itemEnchantList) {
            Item = new ItemStackData(Item_Enchant.getMaterial(),  Item_Enchant.getJpName());
            Item.setLore("買う", "価格:" + Math.round(market.getPrice(Item_Enchant.getEnName()) )+ "円" );
            enchantItemMenuSlotList.add(Item);
        }
        for (int i = itemEnchantList.size(); i< Const.RowSlotNum; i++) {
            enchantItemMenuSlotList.add(new ItemStackData(NoneMaterial, "押せません"));
        }
        for(ItemEnchantData Item_Enchant : itemEnchantList) {
            Item = new ItemStackData(Item_Enchant.getMaterial(),  Item_Enchant.getJpName());
            Item.setLore("売る", "価格:" + Math.round(market.getPrice(Item_Enchant.getEnName())) + "円");
            enchantItemMenuSlotList.add(Item);
        }
        for (int i = itemEnchantList.size() + Const.RowSlotNum; i< Const.WholeSlotNum - 2; i++) {
            enchantItemMenuSlotList.add(new ItemStackData(NoneMaterial, "押せません"));
        }
        Item = new ItemSubMenuSlot(CheckMaterial, "チームの所持金",1);
        Item.setLore("チームの所持金", Math.round(Money) + "円");
        enchantItemMenuSlotList.add(Item);
        Item = new ItemSubMenuSlot(BackMaterial, "戻る",1);
        enchantItemMenuSlotList.add(Item);
        int i = 0;
        for (ItemStackData itemMenuSlot : enchantItemMenuSlotList) {
            EnchantMenu.setItem(i, itemMenuSlot.getItem());
            i++;
        }
        return EnchantMenu;
    }

}
