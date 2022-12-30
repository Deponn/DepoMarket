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
        this.MenuSlotList.add(new ItemMenuSlot(Material.ENCHANTED_BOOK, "enchant", "エンチャントされたアイテム", 0f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.DIAMOND, "Diamond", "ダイヤ", 256f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.IRON_INGOT, "Iron", "鉄", 64f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.NETHERITE_INGOT, "Netherite", "ネザーライト", 1000f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.COAL, "Coal", "石炭", 16f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.QUARTZ, "Quartz", "クオーツ", 16f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.CLAY_BALL, "Clay", "粘土", 32f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.SLIME_BALL, "Slime", "スライムボール", 16f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.OAK_LOG, "Oak", "オークの原木", 16f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.ACACIA_LOG, "Acacia", "アカシアの原木", 16f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.BIRCH_LOG, "BIRCH", "白樺の原木", 16f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.SPRUCE_LOG, "SPRUCE", "トウヒの原木", 16f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.DARK_OAK_LOG, "DARK_OAK", "ダークオークの原木", 16f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.JUNGLE_LOG, "JUNGLE", "ジャングルの原木", 16f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.COBBLESTONE, "CobbleStone", "丸石", 2f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.SAND, "Sand", "砂", 4f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.EMERALD, "Emerald", "エメラルド", 256f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.BREAD, "Bread", "パン", 16f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.TNT, "TNT", "TNT", 128f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.ARROW, "Arrow", "矢", 16f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.LEATHER, "Leather", "革", 32f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.SADDLE, "Saddle", "鞍", 256f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.BEDROCK, "BEDROCK", "岩盤", 1000f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.ENDER_EYE, "ENDER_EYE", "エンダーアイ", 1000f));
        ItemEnchantData eItem;
        eItem = new ItemEnchantData(Material.DIAMOND_PICKAXE, "D_PICK_1", "幸運修繕効率強化耐久ピッケル", 2000f);
        eItem.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 3);
        eItem.addEnchant(Enchantment.MENDING, 1);
        eItem.addEnchant(Enchantment.DIG_SPEED, 5);
        eItem.addEnchant(Enchantment.DURABILITY, 3);
        this.itemEnchantList.add(eItem);
        eItem = new ItemEnchantData(Material.DIAMOND_PICKAXE, "D_PICK_2", "シルクタッチ修繕効率強化耐久ピッケル", 2000f);
        eItem.addEnchant(Enchantment.SILK_TOUCH, 1);
        eItem.addEnchant(Enchantment.MENDING, 1);
        eItem.addEnchant(Enchantment.DIG_SPEED, 5);
        eItem.addEnchant(Enchantment.DURABILITY, 3);
        this.itemEnchantList.add(eItem);
        eItem = new ItemEnchantData(Material.FISHING_ROD, "ROD_1", "宝釣り修繕入れ食い耐久釣り竿", 2000f);
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
