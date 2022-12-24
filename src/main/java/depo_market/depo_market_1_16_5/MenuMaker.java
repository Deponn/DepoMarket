package depo_market.depo_market_1_16_5;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;

public class MenuMaker {
    private final int WholeSlotNum;
    private final int RowSlotNum;
    private final Material NoneMaterial = Material.GLASS_PANE;
    public MenuMaker(int wholeSlotNum, int rowSlotNum) {
        this.WholeSlotNum = wholeSlotNum;
        this.RowSlotNum = rowSlotNum;
    }
    public Inventory MainMenu(ArrayList<ItemMenuSlot> MenuSlots){
        final Inventory MainMenu = Bukkit.createInventory(null, WholeSlotNum, "trade_menu");
        final int MenuSlotsNum = MenuSlots.size();
        final ArrayList<ItemStackData> MainMenuSlots =  new ArrayList<>(MenuSlots);
        for (int i = MenuSlotsNum; i<WholeSlotNum; i++) {
            MainMenuSlots.add(new ItemStackData(NoneMaterial, "押せない"));
        }
        int i = 0;
        for (ItemStackData itemMenuSlot : MainMenuSlots) {
            itemMenuSlot.setLore("aaa", "aaa");
            MainMenu.setItem(i, itemMenuSlot.getItem());
            i++;
        }
        return MainMenu;
    }
    public Inventory SubMenu(Material material, String name,ArrayList<Integer> TradeAmountList){
        final Inventory submenu = Bukkit.createInventory(null, WholeSlotNum, "trade_menu");
        final ArrayList<ItemSubMenuSlot> subItemMenuSlotList = new ArrayList<>();
        for(Integer Amount : TradeAmountList) {
            subItemMenuSlotList.add(new ItemSubMenuSlot(material, name,Amount));
        }
        for (int i = TradeAmountList.size();i<RowSlotNum;i++) {
            subItemMenuSlotList.add(new ItemSubMenuSlot(NoneMaterial, "押せない",1));
        }
        for(Integer Amount : TradeAmountList) {
            subItemMenuSlotList.add(new ItemSubMenuSlot(material, name,Amount));
        }
        for (int i = TradeAmountList.size() + RowSlotNum;i<WholeSlotNum - 1;i++) {
            subItemMenuSlotList.add(new ItemSubMenuSlot(NoneMaterial, "押せない",1));
        }
        subItemMenuSlotList.add(new ItemSubMenuSlot(Material.CHEST, "戻る",1));
        int i = 0;
        for (ItemStackData itemMenuSlot : subItemMenuSlotList) {
            itemMenuSlot.setLore("aaa", "aaa");
            submenu.setItem(i, itemMenuSlot.getItem());
            i++;
        }
        return submenu;
    }

    public  Inventory EnchantMenu(ArrayList<ItemEnchantData> itemEnchantList){
        final Inventory EnchantMenu = Bukkit.createInventory(null, WholeSlotNum, "trade_menu");
        final ArrayList<ItemStackData> enchantItemMenuSlotList = new ArrayList<>();
        for(ItemStackData Item_Enchant : itemEnchantList) {
            enchantItemMenuSlotList.add(new ItemStackData(Item_Enchant.getMaterial(),  Item_Enchant.getJpName()));
        }
        for (int i = itemEnchantList.size(); i<RowSlotNum; i++) {
            enchantItemMenuSlotList.add(new ItemStackData(NoneMaterial, "押せない"));
        }
        for(ItemStackData Item_Enchant : itemEnchantList) {
            enchantItemMenuSlotList.add(new ItemStackData(Item_Enchant.getMaterial(),  Item_Enchant.getJpName()));
        }
        for (int i = itemEnchantList.size() + RowSlotNum; i<WholeSlotNum - 1; i++) {
            enchantItemMenuSlotList.add(new ItemStackData(NoneMaterial, "押せない"));
        }
        enchantItemMenuSlotList.add(new ItemStackData(Material.CHEST, "戻る"));
        int i = 0;
        for (ItemStackData itemMenuSlot : enchantItemMenuSlotList) {
            itemMenuSlot.setLore("aaa", "aaa");
            EnchantMenu.setItem(i, itemMenuSlot.getItem());
            i++;
        }
        return EnchantMenu;
    }

}
