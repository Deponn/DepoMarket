package depo_market.depo_market_1_16_5.ItemDataBase;

import depo_market.depo_market_1_16_5.ItemStack.ItemEnchantData;
import depo_market.depo_market_1_16_5.ItemStack.ItemMenuSlot;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DBKojosen2 implements DBInterface {


    private final ArrayList<ItemMenuSlot> MenuSlotList;//メインメニューのアイテムリスト
    private final ArrayList<Integer> TradeAmountList;//サブメニューに表示する数量リスト
    private final ArrayList<ItemEnchantData> itemEnchantList;//エンチャントアイテムのリスト
    private final ArrayList<ItemMenuSlot> InitialPriceList;//取引アイテムの全種類リスト
    private final Map<String, Float> InitialPriceMap;//取引アイテムの全種類の初期金額マップ
    /**
     * 初期値データやメニューに表示するアイテムのデータを生成するコンストラクタ
     */
    public DBKojosen2(){
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
        this.MenuSlotList.add(new ItemMenuSlot(Material.DIAMOND, "Diamond", "ダイヤ", 3000f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.IRON_INGOT, "Iron", "鉄", 600f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.GOLD_INGOT, "GOLD_INGOT", "金", 1000f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.LAPIS_LAZULI, "LAPIS", "ラピス", 1000f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.REDSTONE, "REDSTONE", "レッドストーン", 300f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.COAL, "Coal", "石炭", 150f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.OAK_LOG, "Oak", "オークの原木", 150f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.COBBLESTONE, "CobbleStone", "丸石", 20f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.SAND, "Sand", "砂", 40f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.DIRT, "DIRT", "土", 20f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.BREAD, "Bread", "パン", 150f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.COOKED_BEEF, "COOKED_BEEF", "肉", 300f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.ARROW, "Arrow", "矢", 50f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.BOW, "BOW", "弓", 1500f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.CROSSBOW, "CROSSBOW", "クロスボウ", 1000f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.LEATHER, "Leather", "革", 100f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.IRON_SWORD, "IRON_SWORD", "鉄剣", 1000f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.SHIELD, "SHIELD", "盾", 1500f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.CHAINMAIL_HELMET, "CHAINMAIL_HELMET", "チェーン鎧頭", 1500f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.CHAINMAIL_CHESTPLATE, "CHAINMAIL_CHESTPLATE", "チェーン鎧上", 2000f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.CHAINMAIL_LEGGINGS, "CHAINMAIL_LEGGINGS", "チェーン鎧下", 2000f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.CHAINMAIL_BOOTS, "CHAINMAIL_BOOTS", "チェーン鎧靴", 1500f));


        ItemEnchantData eItem;
        eItem = new ItemEnchantData(Material.DIAMOND_PICKAXE, "LuckyPICKAXE", "幸運ピッケル", 30000f);
        eItem.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 3);
        this.itemEnchantList.add(eItem);
        eItem = new ItemEnchantData(Material.DIAMOND_PICKAXE, "PoweredPICKAXE", "強化ピッケル", 30000f);
        eItem.addEnchant(Enchantment.DIG_SPEED, 5);
        this.itemEnchantList.add(eItem);
        eItem = new ItemEnchantData(Material.BOW, "PoweredBOW", "火ダメノックバック弓", 50000f);
        eItem.addEnchant(Enchantment.ARROW_DAMAGE, 5);
        eItem.addEnchant(Enchantment.ARROW_FIRE, 1);
        eItem.addEnchant(Enchantment.ARROW_KNOCKBACK, 2);
        this.itemEnchantList.add(eItem);
        eItem = new ItemEnchantData(Material.CROSSBOW, "PoweredCROSSBOW", "強化クロスボウ", 50000f);
        eItem.addEnchant(Enchantment.QUICK_CHARGE, 3);
        eItem.addEnchant(Enchantment.MULTISHOT, 1);
        this.itemEnchantList.add(eItem);
        eItem = new ItemEnchantData(Material.DIAMOND_SWORD, "PoweredDIAMOND_SWORD", "ノックバックダメージ強化ダイヤ剣", 100000f);
        eItem.addEnchant(Enchantment.KNOCKBACK, 2);
        eItem.addEnchant(Enchantment.DAMAGE_ALL, 5);
        this.itemEnchantList.add(eItem);
        eItem = new ItemEnchantData(Material.DIAMOND_HELMET, "PoweredDIAMOND_HELMET", "ダメージ低下ダイヤヘルメット", 100000f);
        eItem.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        this.itemEnchantList.add(eItem);
        eItem = new ItemEnchantData(Material.DIAMOND_CHESTPLATE, "PoweredDIAMOND_CHESTPLATE", "ダメージ低下ダイヤ鎧上", 100000f);
        eItem.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        this.itemEnchantList.add(eItem);
        eItem = new ItemEnchantData(Material.DIAMOND_LEGGINGS, "PoweredDIAMOND_LEGGINGS", "ダメージ低下ダイヤ鎧下", 100000f);
        eItem.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        this.itemEnchantList.add(eItem);
        eItem = new ItemEnchantData(Material.DIAMOND_BOOTS, "PoweredDIAMOND_BOOTS", "落下ダメージ低下ダイヤ靴", 100000f);
        eItem.addEnchant(Enchantment.PROTECTION_FALL, 4);
        this.itemEnchantList.add(eItem);

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
