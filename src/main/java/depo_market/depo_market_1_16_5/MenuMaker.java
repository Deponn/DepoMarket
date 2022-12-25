package depo_market.depo_market_1_16_5;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;

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
    public Inventory MainMenu(ArrayList<ItemMenuSlot> MenuSlots){
        final Inventory MainMenu = Bukkit.createInventory(null, WholeSlotNum, "trade_menu");
        final int MenuSlotsNum = MenuSlots.size();
        final ArrayList<ItemStackData> ItemMenuSlotList =  new ArrayList<>();
        for (ItemMenuSlot item : MenuSlots){
            ItemStackData itemStackData = new ItemStackData(item);
            itemStackData.setLore("値段",String.valueOf(market.getPrice(item.getEnName())));
            ItemMenuSlotList.add(itemStackData);
        }
        for (int i = MenuSlotsNum; i < WholeSlotNum - 2; i++) {
            ItemMenuSlotList.add(new ItemStackData(NoneMaterial, "押せない"));
        }
        ItemMenuSlotList.add(new ItemSubMenuSlot(CheckMaterial, "所持金",1));
        ItemMenuSlotList.add(new ItemSubMenuSlot(BackMaterial, "戻る",1));
        int i = 0;
        for (ItemStackData itemMenuSlot : ItemMenuSlotList) {
            MainMenu.setItem(i, itemMenuSlot.getItem());
            i++;
        }
        return MainMenu;
    }
    public Inventory SubMenu(Material material, String nameJp, String nameEn,ArrayList<Integer> TradeAmountList){
        final Inventory submenu = Bukkit.createInventory(null, WholeSlotNum, "trade_menu");
        final ArrayList<ItemSubMenuSlot> subItemMenuSlotList = new ArrayList<>();
        for(Integer Amount : TradeAmountList) {
            ItemSubMenuSlot Item = new ItemSubMenuSlot(material, nameJp,Amount);
            Item.setLore("値段",String.valueOf(market.getPrice(nameEn) * Amount));
            subItemMenuSlotList.add(Item);
        }
        for (int i = TradeAmountList.size();i<RowSlotNum;i++) {
            subItemMenuSlotList.add(new ItemSubMenuSlot(NoneMaterial, "押せない",1));
        }
        for(Integer Amount : TradeAmountList) {
            ItemSubMenuSlot Item = new ItemSubMenuSlot(material, nameJp,Amount);
            Item.setLore("値段",String.valueOf(market.getPrice(nameEn) * Amount));
            subItemMenuSlotList.add(Item);
        }
        for (int i = TradeAmountList.size() + RowSlotNum;i<WholeSlotNum - 2;i++) {
            subItemMenuSlotList.add(new ItemSubMenuSlot(NoneMaterial, "押せない",1));
        }
        subItemMenuSlotList.add(new ItemSubMenuSlot(CheckMaterial, "所持金",1));
        subItemMenuSlotList.add(new ItemSubMenuSlot(BackMaterial, "戻る",1));
        int i = 0;
        for (ItemStackData itemMenuSlot : subItemMenuSlotList) {
            submenu.setItem(i, itemMenuSlot.getItem());
            i++;
        }
        return submenu;
    }

    public  Inventory EnchantMenu(ArrayList<ItemEnchantData> itemEnchantList){
        final Inventory EnchantMenu = Bukkit.createInventory(null, WholeSlotNum, "trade_menu");
        final ArrayList<ItemStackData> enchantItemMenuSlotList = new ArrayList<>();
        for(ItemEnchantData Item_Enchant : itemEnchantList) {
            ItemStackData Item = new ItemStackData(Item_Enchant.getMaterial(),  Item_Enchant.getJpName());
            Item.setLore("値段",String.valueOf(market.getPrice(Item_Enchant.getEnName())) );
            enchantItemMenuSlotList.add(Item);
        }
        for (int i = itemEnchantList.size(); i<RowSlotNum; i++) {
            enchantItemMenuSlotList.add(new ItemStackData(NoneMaterial, "押せない"));
        }
        for(ItemEnchantData Item_Enchant : itemEnchantList) {
            ItemStackData Item = new ItemStackData(Item_Enchant.getMaterial(),  Item_Enchant.getJpName());
            Item.setLore("値段",String.valueOf(market.getPrice(Item_Enchant.getEnName())) );
            enchantItemMenuSlotList.add(Item);
        }
        for (int i = itemEnchantList.size() + RowSlotNum; i<WholeSlotNum - 2; i++) {
            enchantItemMenuSlotList.add(new ItemStackData(NoneMaterial, "押せない"));
        }
        enchantItemMenuSlotList.add(new ItemSubMenuSlot(CheckMaterial, "所持金",1));
        enchantItemMenuSlotList.add(new ItemSubMenuSlot(BackMaterial, "戻る",1));
        int i = 0;
        for (ItemStackData itemMenuSlot : enchantItemMenuSlotList) {
            EnchantMenu.setItem(i, itemMenuSlot.getItem());
            i++;
        }
        return EnchantMenu;
    }

}
