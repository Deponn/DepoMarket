package depo_market.depo_market_1_16_5;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;

/**
 * メニューを作成するオブジェクト。
 */
public class MenuMaker {
    private final int WholeSlotNum;
    private final int RowSlotNum;
    private final MarketOperator market;
    private final Material NoneMaterial = Material.GLASS_PANE;
    private final Material BackMaterial = Material.BOOK;
    private final Material CheckMaterial = Material.CHEST;
    public MenuMaker(int wholeSlotNum, int rowSlotNum, MarketOperator market) {
        this.WholeSlotNum = wholeSlotNum;
        this.RowSlotNum = rowSlotNum;
        this.market = market;
    }
    //メインメニュー生成。取引するアイテムを選ぶためのもの。取引アイテムを並べてあとは押せないガラス。所持金額。戻る。
    // 押したアイテムは押したスロット番号で処理。アイテムにカーソルを合わせると値段が表示
    public Inventory MainMenu(ArrayList<ItemMenuSlot> MenuSlots,float Money){
        final Inventory MainMenu = Bukkit.createInventory(null, WholeSlotNum, "取引メニュー");
        final int MenuSlotsNum = MenuSlots.size();
        final ArrayList<ItemStackData> ItemMenuSlotList =  new ArrayList<>();
        ItemStackData itemStackData;
        for (ItemMenuSlot item : MenuSlots){
            itemStackData = new ItemStackData(item);
            itemStackData.setLore("値段", Math.round(market.getPrice(item.getEnName())) + "円");
            ItemMenuSlotList.add(itemStackData);
        }
        for (int i = MenuSlotsNum; i < WholeSlotNum - 2; i++) {
            ItemMenuSlotList.add(new ItemStackData(NoneMaterial, "押せません"));
        }
        itemStackData = new ItemSubMenuSlot(CheckMaterial, "所持金",1);
        itemStackData.setLore("所持金", Math.round(Money) + "円");
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
    public Inventory SubMenu(Material material, String nameJp, String nameEn,ArrayList<Integer> TradeAmountList,float Money){
        final Inventory submenu = Bukkit.createInventory(null, WholeSlotNum, "取引メニュー");
        final ArrayList<ItemSubMenuSlot> subItemMenuSlotList = new ArrayList<>();
        ItemSubMenuSlot Item;
        int counter = 0;
        for(Integer Amount : TradeAmountList) {
            Item = new ItemSubMenuSlot(material, nameJp,Amount);
            Item.setLore("買う", "参考価格:" + Math.round(market.getPrice(nameEn) * Amount) + "円");
            if(Amount == 1 || Math.round(market.getPrice(nameEn) * Amount) < 50000) {
                subItemMenuSlotList.add(Item);
                counter += 1;
            }
        }
        for (int i = counter;i<RowSlotNum;i++) {
            subItemMenuSlotList.add(new ItemSubMenuSlot(NoneMaterial, "押せません",1));
        }
        counter = 0;
        for(Integer Amount : TradeAmountList) {
            Item = new ItemSubMenuSlot(material, nameJp,Amount);
            Item.setLore("売る", "参考価格:" + Math.round(market.getPrice(nameEn) * Amount) + "円");
            if(Amount == 1 || Math.round(market.getPrice(nameEn) * Amount) < 50000) {
                subItemMenuSlotList.add(Item);
                counter = counter + 1;
            }
        }
        for (int i = counter + RowSlotNum;i<WholeSlotNum - 2;i++) {
            subItemMenuSlotList.add(new ItemSubMenuSlot(NoneMaterial, "押せません",1));
        }
        Item = new ItemSubMenuSlot(CheckMaterial, "所持金",1);
        Item.setLore("所持金", Math.round(Money) + "円");
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
    public  Inventory EnchantMenu(ArrayList<ItemEnchantData> itemEnchantList,float Money){
        final Inventory EnchantMenu = Bukkit.createInventory(null, WholeSlotNum, "エンチャントアイテム取引メニュー");
        final ArrayList<ItemStackData> enchantItemMenuSlotList = new ArrayList<>();
        ItemStackData Item;
        for(ItemEnchantData Item_Enchant : itemEnchantList) {
            Item = new ItemStackData(Item_Enchant.getMaterial(),  Item_Enchant.getJpName());
            Item.setLore("買う", "参考価格:" + Math.round(market.getPrice(Item_Enchant.getEnName()) )+ "円" );
            enchantItemMenuSlotList.add(Item);
        }
        for (int i = itemEnchantList.size(); i<RowSlotNum; i++) {
            enchantItemMenuSlotList.add(new ItemStackData(NoneMaterial, "押せません"));
        }
        for(ItemEnchantData Item_Enchant : itemEnchantList) {
            Item = new ItemStackData(Item_Enchant.getMaterial(),  Item_Enchant.getJpName());
            Item.setLore("売る", "参考価格:" + Math.round(market.getPrice(Item_Enchant.getEnName())) + "円");
            enchantItemMenuSlotList.add(Item);
        }
        for (int i = itemEnchantList.size() + RowSlotNum; i<WholeSlotNum - 2; i++) {
            enchantItemMenuSlotList.add(new ItemStackData(NoneMaterial, "押せません"));
        }
        Item = new ItemSubMenuSlot(CheckMaterial, "所持金",1);
        Item.setLore("所持金", Math.round(Money) + "円");
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
