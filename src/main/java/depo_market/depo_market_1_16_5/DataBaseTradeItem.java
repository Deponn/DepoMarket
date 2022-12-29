package depo_market.depo_market_1_16_5;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.util.ArrayList;

public class DataBaseTradeItem {
    private final ArrayList<ItemMenuSlot> MenuSlotList = new ArrayList<>();
    private final ArrayList<Integer> TradeAmountList = new ArrayList<>();
    private final ArrayList<ItemEnchantData> itemEnchantList = new ArrayList<>();
    private final ArrayList<ItemMenuSlot> InitialPriceList = new ArrayList<>();

    public DataBaseTradeItem(){
        this.TradeAmountList.add(1);
        this.TradeAmountList.add(16);
        this.TradeAmountList.add(64);
        this.MenuSlotList.add(new ItemMenuSlot(Material.ENCHANTED_BOOK, "enchant", "エンチャされたアイテム", 1000f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.DIAMOND, "Diamond", "ダイヤ", 1000f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.IRON_INGOT, "Iron", "鉄", 1000f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.NETHERITE_INGOT, "Netherite", "ネザーライト", 1000f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.COAL, "Coal", "石炭", 1000f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.QUARTZ, "Quartz", "クオーツ", 1000f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.CLAY_BALL, "Clay", "粘土", 1000f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.SLIME_BALL, "Slime", "スライムボール", 1000f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.OAK_LOG, "Oak", "オーク", 1000f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.ACACIA_LOG, "Acacia", "アカシア", 1000f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.COBBLESTONE, "CobbleStone", "丸石", 1000f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.SAND, "Sand", "砂", 1000f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.EMERALD, "Emerald", "エメラルド", 1000f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.BREAD, "Bread", "パン", 1000f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.TNT, "TNT", "TNT", 1000f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.ARROW, "Arrow", "矢", 1000f));
        ItemEnchantData eItem;
        eItem = new ItemEnchantData(Material.DIAMOND_PICKAXE, "D_PICK_1", "幸運修繕効率強化耐久ピッケル", 1000f);
        eItem.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 3);
        eItem.addEnchant(Enchantment.MENDING, 1);
        eItem.addEnchant(Enchantment.DIG_SPEED, 5);
        eItem.addEnchant(Enchantment.DURABILITY, 3);
        this.itemEnchantList.add(eItem);
        eItem = new ItemEnchantData(Material.DIAMOND_PICKAXE, "D_PICK_2", "シルクタッチ修繕効率強化耐久ピッケル", 1000f);
        eItem.addEnchant(Enchantment.SILK_TOUCH, 1);
        eItem.addEnchant(Enchantment.MENDING, 1);
        eItem.addEnchant(Enchantment.DIG_SPEED, 5);
        eItem.addEnchant(Enchantment.DURABILITY, 3);
        this.itemEnchantList.add(eItem);
        eItem = new ItemEnchantData(Material.FISHING_ROD, "ROD_1", "宝釣り修繕入れ食い耐久釣り竿", 1000f);
        eItem.addEnchant(Enchantment.LUCK, 3);
        eItem.addEnchant(Enchantment.MENDING, 1);
        eItem.addEnchant(Enchantment.LURE, 3);
        eItem.addEnchant(Enchantment.DURABILITY, 3);
        this.itemEnchantList.add(eItem);
        for (int i = 1; i < MenuSlotList.size(); i++) {
            InitialPriceList.add(MenuSlotList.get(i));
        }
        InitialPriceList.addAll(itemEnchantList);
    }
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

}
