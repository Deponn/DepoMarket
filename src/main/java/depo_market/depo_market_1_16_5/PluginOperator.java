package depo_market.depo_market_1_16_5;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


import java.util.*;

public class PluginOperator {
    private final int SLOT_OF_ENCHANT = 0;
    private final int INDEX_OF_MAIN_MENU = -1;
    private final int INDEX_OF_OUT_OF_MENU = -2;
    private final String CUSTOMER_NAME = "Depo_Customer";
    private final MarketOperator market;
    private final TeamMoneyOperator teamMoneyOperator;
    private final MenuMaker menuMaker;
    private final Map<String, Boolean>player_is_in_Menu = new HashMap<>();
    private final Map<String, Integer> player_inv_state = new HashMap<>();
    private final ArrayList<ItemMenuSlot> MenuSlotList = new ArrayList<>();
    private final ArrayList<Integer> TradeAmountList = new ArrayList<>();
    private final ArrayList<ItemEnchantData> itemEnchantList = new ArrayList<>();
    private final ArrayList<ItemMenuSlot> InitialPriceList = new ArrayList<>();

    public PluginOperator(){
        this.TradeAmountList.add(1);
        this.TradeAmountList.add(16);
        this.TradeAmountList.add(64);
        this.MenuSlotList.add(new ItemMenuSlot(Material.ENCHANTED_BOOK,"enchant","エンチャされたアイテム",1000f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.DIAMOND,"Diamond","ダイヤ",1000f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.IRON_INGOT,"Iron","鉄",1000f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.NETHERITE_INGOT,"Netherite","ネザーライト",1000f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.COAL,"Coal","石炭",1000f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.QUARTZ,"Quartz","クオーツ",1000f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.CLAY_BALL,"Clay","粘土",1000f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.SLIME_BALL,"Slime","スライムボール",1000f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.OAK_LOG,"Oak","オーク",1000f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.ACACIA_LOG,"Acacia","アカシア",1000f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.COBBLESTONE,"CobbleStone","丸石",1000f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.SAND,"Sand","砂",1000f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.EMERALD,"Emerald","エメラルド",1000f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.BREAD,"Bread","パン",1000f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.TNT,"TNT","TNT",1000f));
        this.MenuSlotList.add(new ItemMenuSlot(Material.ARROW,"Arrow","矢",1000f));
        ItemEnchantData eItem;
        eItem = new ItemEnchantData(Material.DIAMOND_PICKAXE,"D_PICK_1","幸運修繕効率強化耐久ピッケル",1000f);
        eItem.addEnchant(Enchantment.LOOT_BONUS_BLOCKS,3);
        eItem.addEnchant(Enchantment.MENDING,1);
        eItem.addEnchant(Enchantment.DIG_SPEED,5);
        eItem.addEnchant(Enchantment.DURABILITY,3);
        this.itemEnchantList.add(eItem);
        eItem = new ItemEnchantData(Material.DIAMOND_PICKAXE,"D_PICK_2","シルクタッチ修繕効率強化耐久ピッケル",1000f);
        eItem.addEnchant(Enchantment.SILK_TOUCH,1);
        eItem.addEnchant(Enchantment.MENDING,1);
        eItem.addEnchant(Enchantment.DIG_SPEED,5);
        eItem.addEnchant(Enchantment.DURABILITY,3);
        this.itemEnchantList.add(eItem);
        eItem = new ItemEnchantData(Material.FISHING_ROD,"ROD_1","宝釣り修繕入れ食い耐久釣り竿",1000f);
        eItem.addEnchant(Enchantment.LUCK,3);
        eItem.addEnchant(Enchantment.MENDING,1);
        eItem.addEnchant(Enchantment.LURE,3);
        eItem.addEnchant(Enchantment.DURABILITY,3);
        this.itemEnchantList.add(eItem);
        for (int i = 1; i < MenuSlotList.size(); i++) {
            InitialPriceList.add(MenuSlotList.get(i));
        }
        InitialPriceList.addAll(itemEnchantList);
        this.market = new MarketOperator(InitialPriceList);
        this.teamMoneyOperator = new TeamMoneyOperator();
        this.menuMaker = new MenuMaker(27,9,market);
    }
    public void LoadData(Map<String, Float> teamData,Map<String, ItemPrice> marketData,boolean isRun){
        market.loadData(isRun,marketData);
        teamMoneyOperator.LoadTeams(teamData);
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
        }else {
            player.sendMessage("閉鎖しています");
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
                        } else{
                            MakeSubMenu(player, ClickedSlot);
                        }
                    }else if (ClickedSlot == 25) {
                        player.sendMessage(String.valueOf(teamMoneyOperator.getTeamMoney(player)));
                    }else if (ClickedSlot == 26) {
                        player.closeInventory();
                    }
                } else if (player_inv_state.getOrDefault(player.getName(), INDEX_OF_OUT_OF_MENU) >= 1) {
                    if (ClickedSlot >= 0 & ClickedSlot < TradeAmountList.size()) {
                        BuyItem(player, player_inv_state.get(player.getName()), ClickedSlot);
                        MakeSubMenu(player,player_inv_state.get(player.getName()));
                    } else if (ClickedSlot >= 9 & ClickedSlot < TradeAmountList.size() + 9) {
                        SellItem(player, player_inv_state.get(player.getName()), ClickedSlot - 9);
                        MakeSubMenu(player,player_inv_state.get(player.getName()));
                    } else if (ClickedSlot == 25) {
                        player.sendMessage(String.valueOf(teamMoneyOperator.getTeamMoney(player)));
                    } else if (ClickedSlot == 26) {
                        MakeMainMenu(player);
                    }
                } else if (player_inv_state.getOrDefault(player.getName(), INDEX_OF_OUT_OF_MENU) == SLOT_OF_ENCHANT) {
                    if (ClickedSlot >= 0 & ClickedSlot < itemEnchantList.size()) {
                        BuyEnchantItem(player,  ClickedSlot);
                        MakeSubMenu(player,player_inv_state.get(player.getName()));
                    } else if (ClickedSlot >= 9 & ClickedSlot < itemEnchantList.size() + 9) {
                        SellEnchantItem(player,ClickedSlot - 9);
                        MakeSubMenu(player,player_inv_state.get(player.getName()));
                    } else if (ClickedSlot == 25) {
                        player.sendMessage(String.valueOf(teamMoneyOperator.getTeamMoney(player)));
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
        final String SelectedJpName = MenuSlotList.get(ClickedSlot).getJpName();
        final String SelectedEnName = MenuSlotList.get(ClickedSlot).getEnName();
        final Inventory submenu = menuMaker.SubMenu(SelectedMaterial,SelectedJpName,SelectedEnName,TradeAmountList);
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

    private void BuyItem(Player player, int TradeIndex, int AmountIndex){
        final Material SelectedMaterial = MenuSlotList.get(TradeIndex).getMaterial();
        final String SelectedEnName = MenuSlotList.get(TradeIndex).getEnName();
        final int BuyAmount = TradeAmountList.get(AmountIndex);
        final Inventory PlayerInventory = player.getInventory();
        HashMap<Integer,ItemStack> aaa;
        aaa = PlayerInventory.addItem(new ItemStack(SelectedMaterial, BuyAmount));
        int BoughtAmount;
        if (aaa.containsKey(0)) {
            BoughtAmount = BuyAmount - aaa.get(0).getAmount();
        }else {
            BoughtAmount = BuyAmount;
        }
        float Money = market.buy(SelectedEnName,BoughtAmount);
        teamMoneyOperator.addTeamMoney(player, - Money);
    }

    private void SellItem(Player player, int TradeIndex, int AmountIndex){
        final Material SelectedMaterial = MenuSlotList.get(TradeIndex).getMaterial();
        final String SelectedEnName = MenuSlotList.get(TradeIndex).getEnName();
        final int SellAmount = TradeAmountList.get(AmountIndex);
        final Inventory PlayerInventory = player.getInventory();
        HashMap<Integer,ItemStack> aaa;
        aaa = PlayerInventory.removeItem(new ItemStack(SelectedMaterial, SellAmount));
        int SoledAmount;
        if (aaa.containsKey(0)) {
            SoledAmount = SellAmount - aaa.get(0).getAmount();
        }else {
            SoledAmount = SellAmount;
        }
        float Money = market.sell(SelectedEnName,SoledAmount);
        teamMoneyOperator.addTeamMoney(player, Money);
    }

    private void BuyEnchantItem(Player player, int EnchantIndex){
        ItemEnchantData Item = itemEnchantList.get(EnchantIndex);
        final String SelectedEnName = Item.getEnName();
        final int buyAmount = 1;
        final Inventory PlayerInventory = player.getInventory();
        HashMap<Integer,ItemStack> aaa;
        ItemStack EnchantItemStack = new ItemStack(Item.getMaterial(),buyAmount);
        int size = Item.getNumEnchant();
        for(int i = 0;i < size;i ++){
            EnchantItemStack.addEnchantment(Item.getEnchant(i),Item.getLevel(i));
        }
        aaa = PlayerInventory.addItem(EnchantItemStack);
        int BoughtAmount;
        if (aaa.containsKey(0)) {
            BoughtAmount = buyAmount - aaa.get(0).getAmount();
        }else {
            BoughtAmount = buyAmount;
        }
        float Money = market.buy(SelectedEnName,BoughtAmount);
        teamMoneyOperator.addTeamMoney(player, - Money);
    }

    private void SellEnchantItem(Player player, int EnchantIndex){
        ItemEnchantData Item = itemEnchantList.get(EnchantIndex);
        final String SelectedEnName = Item.getEnName();
        final int sellAmount = 1;
        Inventory PlayerInventory = player.getInventory();
        HashMap<Integer,ItemStack> aaa;
        ItemStack EnchantItemStack = new ItemStack(Item.getMaterial(),sellAmount);
        int size = Item.getNumEnchant();
        for(int i = 0;i < size;i ++){
            EnchantItemStack.addEnchantment(Item.getEnchant(i),Item.getLevel(i));
        }
        aaa = PlayerInventory.removeItem(EnchantItemStack);
        int SoledAmount;
        if (aaa.containsKey(0)) {
            SoledAmount = sellAmount - aaa.get(0).getAmount();
        }else {
            SoledAmount = sellAmount;
        }
        float Money = market.sell(SelectedEnName,SoledAmount);
        teamMoneyOperator.addTeamMoney(player, Money);
    }
}
