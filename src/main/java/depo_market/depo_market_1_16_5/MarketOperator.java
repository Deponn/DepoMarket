package depo_market.depo_market_1_16_5;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


import java.util.*;

public class MarketOperator {
    private final int SLOT_OF_ENCHANT = 0;
    private final int INDEX_OF_MAIN_MENU = -1;
    private final int INDEX_OF_OUT_OF_MENU = -2;
    private final String CUSTOMER_NAME = "Depo_Customer";
    private final MarketObject market;
    private final TeamMoneyOperator teamMoneyOperator;
    private final MenuMaker menuMaker;
    private final Map<String, Boolean>player_is_in_Menu = new HashMap<>();
    private final Map<String, Integer> player_inv_state = new HashMap<>();
    private final ArrayList<ItemMenuSlot> MenuSlotList = new ArrayList<>();
    private final ArrayList<Integer> TradeAmountList = new ArrayList<>();
    private final ArrayList<ItemEnchantData> itemEnchantList = new ArrayList<>();
    private final ArrayList<ItemMenuSlot> InitialPriceList = new ArrayList<>();

    public MarketOperator(){

        this.menuMaker = new MenuMaker(27,9);
        this.TradeAmountList.add(1);
        this.TradeAmountList.add(16);
        this.TradeAmountList.add(64);
        this.MenuSlotList.add(new ItemMenuSlot(Material.ENCHANTED_BOOK,"enchant","エンチャされたアイテム",10f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.DIAMOND,"Diamond","ダイヤ",10f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.IRON_INGOT,"Iron","鉄",10f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.NETHERITE_INGOT,"Netherite","ネザーライト",10f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.COAL,"Coal","石炭",10f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.QUARTZ,"Quartz","クオーツ",10f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.CLAY_BALL,"Clay","粘土",10f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.SLIME_BALL,"Slime","スライムボール",10f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.OAK_LOG,"Oak","オーク",10f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.ACACIA_LOG,"Acacia","アカシア",10f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.COBBLESTONE,"CobbleStone","丸石",10f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.SAND,"Sand","砂",10f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.EMERALD,"Emerald","エメラルド",10f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.BREAD,"Bread","パン",10f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.TNT,"TNT","TNT",10f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.ARROW,"Arrow","矢",10f));
        ItemEnchantData eItem;
        eItem = new ItemEnchantData(Material.DIAMOND_PICKAXE,"D_PICK_1","幸運修繕効率強化耐久ピッケル",10f);
        eItem.addEnchant(Enchantment.LOOT_BONUS_BLOCKS,3);
        eItem.addEnchant(Enchantment.MENDING,1);
        eItem.addEnchant(Enchantment.DIG_SPEED,5);
        eItem.addEnchant(Enchantment.DURABILITY,3);
        this.itemEnchantList.add(eItem);
        eItem = new ItemEnchantData(Material.DIAMOND_PICKAXE,"D_PICK_2","シルクタッチ修繕効率強化耐久ピッケル",10f);
        eItem.addEnchant(Enchantment.SILK_TOUCH,1);
        eItem.addEnchant(Enchantment.MENDING,1);
        eItem.addEnchant(Enchantment.DIG_SPEED,5);
        eItem.addEnchant(Enchantment.DURABILITY,3);
        this.itemEnchantList.add(eItem);
        eItem = new ItemEnchantData(Material.FISHING_ROD,"ROD_1","宝釣り修繕入れ食い耐久釣り竿",10f);
        eItem.addEnchant(Enchantment.LUCK,3);
        eItem.addEnchant(Enchantment.MENDING,1);
        eItem.addEnchant(Enchantment.LURE,3);
        eItem.addEnchant(Enchantment.DURABILITY,3);
        this.itemEnchantList.add(eItem);
        for (int i = 1; i < MenuSlotList.size(); i++) {
            InitialPriceList.add(MenuSlotList.get(i));
        }
        InitialPriceList.addAll(itemEnchantList);
        this.market = new MarketObject(InitialPriceList);
        this.teamMoneyOperator = new TeamMoneyOperator();
    }
    public void LoadData(Map<String, Float> teamData,Map<String, ItemPrice> marketData,boolean isRun){
        market.loadData();
        teamMoneyOperator.LoadTeams(teamData);
        if(isRun){
            this.StartMarket();
        }
    }
    public Map<String, ItemPrice> getMarketData(){
        return market.getData();
    }
    public boolean getMarketState(){
        return market.getMarketState();
    }
    public Map<String, Float> getTeamMoneyData(){
        return teamMoneyOperator.getData();
    }
    public boolean InitializeMarket(Player player){
        market.Initialize(player,InitialPriceList);
        return true;
    }
    private void StartMarket(){
        market.StartMarket();
    }
    public boolean StartMarket(Player player){
        market.StartMarket(player);
        return true;
    }
    public boolean StopMarket(Player player){
        market.StopMarket(player);
        return true;
    }
    public boolean PlaceCustomer(Player player){
        Villager Customer = (Villager) player.getWorld().spawnEntity(player.getLocation(), EntityType.VILLAGER);
        Customer.setCustomName("取引商人");
        Customer.addScoreboardTag(CUSTOMER_NAME);
        Customer.setInvulnerable(true);
        Customer.setRemoveWhenFarAway(false);
        return true;
    }
    public boolean KillAllCustomer(){
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getLivingEntities()) {
                if (entity instanceof Villager) {
                    if (entity.getScoreboardTags().contains(CUSTOMER_NAME)) {
                        ((Villager) entity).setHealth(0);
                    }
                }
            }
        }
        return true;
    }
    public boolean Tax(Player player) {
        if (market.getMarketState()) {
            player.sendMessage("徴税します");
        } else {
            player.sendMessage("市場が動いていません");
        }
        return true;
    }
    public boolean GiveMoney(Player player){
        if (market.getMarketState()) {
            player.sendMessage("お金をあげます");
        } else {
            player.sendMessage("市場が動いていません");
        }
        return true;
    }
    public boolean SetDisAdvantage(Player player){
        player.sendMessage("借金しているデバフを決める");
        return true;
    }
    public boolean ReloadTeam(Player player){
        player.sendMessage("チームリロード");
        teamMoneyOperator.reLoadTeams();
        return true;
    }

    public void CustomerClick(Player player, Entity ClickedEntity){
        if(market.getMarketState()) {
            if (ClickedEntity instanceof Villager) {
                Villager ClickedCustomer = (Villager) ClickedEntity;
                if (ClickedCustomer.getScoreboardTags().contains(CUSTOMER_NAME)) {
                    MakeMainMenu(player);
                }
            }
        }
    }
    public boolean isMenu(Player player){
        return player_is_in_Menu.getOrDefault(player.getName(),false);
    }
    public void MenuClick(Player player, ItemStack item, int ClickedSlot) {
        if (item != null) {
            if (!item.getType().isAir()) {
                if (player_inv_state.getOrDefault(player.getName(), INDEX_OF_OUT_OF_MENU) == INDEX_OF_MAIN_MENU) {
                    if (ClickedSlot < MenuSlotList.size()) {
                        if (ClickedSlot == SLOT_OF_ENCHANT) {
                            MakeEnchantMenu(player);
                        } else {
                            MakeSubMenu(player, ClickedSlot);
                        }
                    }
                } else if (player_inv_state.getOrDefault(player.getName(), INDEX_OF_OUT_OF_MENU) >= 1) {
                    if (ClickedSlot >= 0 & ClickedSlot < TradeAmountList.size()) {
                        HashMap<Integer, ItemStack> temp;
                        temp = AddItemToPlayer(player, player_inv_state.get(player.getName()), ClickedSlot);
                        player.sendMessage(temp.get(0).getType().name());
                    } else if (ClickedSlot >= 9 & ClickedSlot < TradeAmountList.size() + 9) {
                        HashMap<Integer, ItemStack> temp;
                        temp = RemoveItemFromPlayer(player, player_inv_state.get(player.getName()), ClickedSlot - 9);
                        player.sendMessage(temp.get(0).getType().name());
                    } else if (ClickedSlot == 26) {
                        MakeMainMenu(player);
                    }
                } else if (player_inv_state.getOrDefault(player.getName(), INDEX_OF_OUT_OF_MENU) == SLOT_OF_ENCHANT) {
                    if (ClickedSlot >= 0 & ClickedSlot < itemEnchantList.size()) {
                        HashMap<Integer, ItemStack> temp;
                        temp = AddEnchantItemToPlayer(player,  ClickedSlot);
                        player.sendMessage(Integer.valueOf(temp.get(0).getAmount()).toString());
                    } else if (ClickedSlot >= 9 & ClickedSlot < itemEnchantList.size() + 9) {
                        HashMap<Integer, ItemStack> temp;
                        temp = RemoveEnchantItemFromPlayer(player,ClickedSlot - 9);
                        player.sendMessage(Integer.valueOf(temp.get(0).getAmount()).toString());
                    } else if (ClickedSlot == 26) {
                        MakeMainMenu(player);
                    }
                }
            }
        }
    }
    public boolean MenuClose(Player player) {
        if (player_is_in_Menu.getOrDefault(player.getName(), false)) {
            player_is_in_Menu.put(player.getName(), false);
            player_inv_state.put(player.getName(), INDEX_OF_OUT_OF_MENU);
            return true;
        }
        return false;
    }
    private void MakeMainMenu(Player player){
        final Inventory MainMenu = menuMaker.MainMenu(MenuSlotList);
        player.openInventory((MainMenu));
        player_is_in_Menu.put(player.getName(), true);
        player_inv_state.put(player.getName(), INDEX_OF_MAIN_MENU);
    }
    private void MakeSubMenu(Player player,int ClickedSlot){
        final Material SelectedMaterial = MenuSlotList.get(ClickedSlot).getMaterial();
        final String SelectedName = MenuSlotList.get(ClickedSlot).getJpName();
        final Inventory submenu = menuMaker.SubMenu(SelectedMaterial,SelectedName,TradeAmountList);
        player.openInventory(submenu);
        player_inv_state.put(player.getName(), ClickedSlot);
        player_is_in_Menu.put(player.getName(), true);
    }
    private void MakeEnchantMenu(Player player){
        final Inventory EnchantMenu = menuMaker.EnchantMenu(itemEnchantList);
        player.openInventory(EnchantMenu);
        player_inv_state.put(player.getName(), SLOT_OF_ENCHANT);
        player_is_in_Menu.put(player.getName(), true);
    }

    private HashMap<Integer,ItemStack> AddItemToPlayer(Player player,int TradeIndex,int AmountIndex){
        final Material SelectedMaterial = MenuSlotList.get(TradeIndex).getMaterial();
        Inventory PlayerInventory = player.getInventory();
        return PlayerInventory.addItem(new ItemStack(SelectedMaterial,TradeAmountList.get(AmountIndex)));
    }

    private HashMap<Integer,ItemStack> RemoveItemFromPlayer(Player player,int TradeIndex,int AmountIndex){
        final Material SelectedMaterial = MenuSlotList.get(TradeIndex).getMaterial();
        Inventory PlayerInventory = player.getInventory();
        return PlayerInventory.removeItem(new ItemStack(SelectedMaterial,TradeAmountList.get(AmountIndex)));
    }

    private HashMap<Integer,ItemStack> AddEnchantItemToPlayer(Player player, int EnchantIndex){
        Inventory PlayerInventory = player.getInventory();
        ItemEnchantData Item = itemEnchantList.get(EnchantIndex);
        ItemStack EnchantItemStack = new ItemStack(Item.getMaterial(),1);
        int size = Item.getNumEnchant();
        for(int i = 0;i < size;i ++){
            EnchantItemStack.addEnchantment(Item.getEnchant(i),Item.getLevel(i));
        }
        return PlayerInventory.addItem(EnchantItemStack);
    }

    private HashMap<Integer,ItemStack> RemoveEnchantItemFromPlayer(Player player, int EnchantIndex){
        Inventory PlayerInventory = player.getInventory();
        ItemEnchantData Item = itemEnchantList.get(EnchantIndex);
        ItemStack EnchantItemStack = new ItemStack(Item.getMaterial(),1);
        int size = Item.getNumEnchant();
        for(int i = 0;i < size;i ++){
            EnchantItemStack.addEnchantment(Item.getEnchant(i),Item.getLevel(i));
        }
        return PlayerInventory.removeItem(EnchantItemStack);
    }
}
