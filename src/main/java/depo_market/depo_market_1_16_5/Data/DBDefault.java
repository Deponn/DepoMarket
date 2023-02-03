package depo_market.depo_market_1_16_5.Data;

import depo_market.depo_market_1_16_5.ItemStack.ItemEnchantData;
import depo_market.depo_market_1_16_5.ItemStack.ItemMenuSlot;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DBDefault implements DBInterface{
    private final ArrayList<ItemMenuSlot> MenuSlotList;//メインメニューのアイテムリスト
    private final ArrayList<Integer> TradeAmountList;//サブメニューに表示する数量リスト
    private final ArrayList<ItemEnchantData> itemEnchantList;//エンチャントアイテムのリスト
    private final ArrayList<ItemMenuSlot> InitialPriceList;//取引アイテムの全種類リスト
    private final Map<String, Float> InitialPriceMap;//取引アイテムの全種類の初期金額マップ

    /**
     * 初期値データやメニューに表示するアイテムのデータを生成するコンストラクタ
     */
    public DBDefault(){
        MenuSlotList = new ArrayList<>();//メインメニューのアイテムリスト
        TradeAmountList = new ArrayList<>();//サブメニューに表示する数量リスト
        itemEnchantList = new ArrayList<>();//エンチャントアイテムのリスト
        InitialPriceList = new ArrayList<>();//取引アイテムの全種類リスト
        InitialPriceMap = new HashMap<>();//取引アイテムの全種類の初期金額マップ

        this.TradeAmountList.add(1);
        this.TradeAmountList.add(4);
        this.TradeAmountList.add(16);
        this.TradeAmountList.add(64);
        this.MenuSlotList.add(new ItemMenuSlot(Material.ENCHANTED_BOOK, "enchant", "エンチャントされたアイテム", 0f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.DIAMOND, "Diamond", "ダイヤ", 2560f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.IRON_INGOT, "Iron", "鉄", 640f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.NETHERITE_INGOT, "Netherite", "ネザーライト", 10000f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.COAL, "Coal", "石炭", 160f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.QUARTZ, "Quartz", "クオーツ", 160f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.CLAY_BALL, "Clay", "粘土", 320f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.SLIME_BALL, "Slime", "スライムボール", 160f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.OAK_LOG, "Oak", "オークの原木", 160f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.ACACIA_LOG, "Acacia", "アカシアの原木", 160f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.BIRCH_LOG, "BIRCH", "白樺の原木", 160f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.SPRUCE_LOG, "SPRUCE", "トウヒの原木", 160f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.DARK_OAK_LOG, "DARK_OAK", "ダークオークの原木", 160f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.JUNGLE_LOG, "JUNGLE", "ジャングルの原木", 160f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.COBBLESTONE, "CobbleStone", "丸石", 20f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.SAND, "Sand", "砂", 40f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.EMERALD, "Emerald", "エメラルド", 2560f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.BREAD, "Bread", "パン", 160f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.TNT, "TNT", "TNT", 1280f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.ARROW, "Arrow", "矢", 160f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.LEATHER, "Leather", "革", 320f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.CROSSBOW, "CROSSBOW", "クロスボウ", 2560f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.BEDROCK, "BEDROCK", "岩盤", 10000f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.ENDER_EYE, "ENDER_EYE", "エンダーアイ", 10000f));
        ItemEnchantData eItem;
        eItem = new ItemEnchantData(Material.DIAMOND_PICKAXE, "D_PICK_1", "幸運修繕効率強化耐久ピッケル", 20000f);
        eItem.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 3);
        eItem.addEnchant(Enchantment.MENDING, 1);
        eItem.addEnchant(Enchantment.DIG_SPEED, 5);
        eItem.addEnchant(Enchantment.DURABILITY, 3);
        this.itemEnchantList.add(eItem);
        eItem = new ItemEnchantData(Material.DIAMOND_PICKAXE, "D_PICK_2", "シルクタッチ修繕効率強化耐久ピッケル", 20000f);
        eItem.addEnchant(Enchantment.SILK_TOUCH, 1);
        eItem.addEnchant(Enchantment.MENDING, 1);
        eItem.addEnchant(Enchantment.DIG_SPEED, 5);
        eItem.addEnchant(Enchantment.DURABILITY, 3);
        this.itemEnchantList.add(eItem);
        eItem = new ItemEnchantData(Material.FISHING_ROD, "ROD_1", "宝釣り修繕入れ食い耐久釣り竿", 20000f);
        eItem.addEnchant(Enchantment.LUCK, 3);
        eItem.addEnchant(Enchantment.MENDING, 1);
        eItem.addEnchant(Enchantment.LURE, 3);
        eItem.addEnchant(Enchantment.DURABILITY, 3);
        this.itemEnchantList.add(eItem);


        for (int i = 1; i < MenuSlotList.size(); i++) {
            InitialPriceList.add(MenuSlotList.get(i));
        }
        InitialPriceList.addAll(itemEnchantList);
        for(ItemMenuSlot item : InitialPriceList){
            InitialPriceMap.put(item.getEnName(),item.getInitialPrice());
        }
    }
    /**
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
