package depo_market.depo_market_1_16_5;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;


import java.util.*;

public class MarketOperator {

    private final int NUM_OF_INV_BOX = 27;
    private final int INDEX_ENCHANT = 0;
    private final int INDEX_OF_MAIN_MENU = -1;
    private final int INDEX_OF_SUB_MENU = -2;
    private final Depo_Market_1_16_5 plugin;
    private MarketObject market;
    private final ScoreboardManager scoreboardManager;
    private final Scoreboard scoreboard;
    private Object[] teams;
    private boolean market_run_flag = false;
    private boolean market_exist_flag = false;
    private final Map<String, Boolean>player_is_in_Menu = new HashMap<>();
    private final Map<String, Integer> player_inv_state = new HashMap<>();
    private final ArrayList<MenuItem> MenuItemList = new ArrayList<>();
    private final ArrayList<Integer> TradeAmountList = new ArrayList<>();
    private final ArrayList<EnchantItem> EnchantMenuList = new ArrayList<>();

    public MarketOperator(Depo_Market_1_16_5 plugin){
        this.plugin = plugin;
        this.scoreboardManager = Bukkit.getScoreboardManager();
        this.scoreboard = Objects.requireNonNull(scoreboardManager).getMainScoreboard();
        this.teams = scoreboard.getTeams().toArray();
        this.TradeAmountList.add(1);
        this.TradeAmountList.add(16);
        this.TradeAmountList.add(64);
        this.MenuItemList.add(new MenuItem(Material.ENCHANTED_BOOK,1,"エンチャされたアイテム"));
        this.MenuItemList.add(new MenuItem(Material.DIAMOND,1,"ダイヤ"));
        this.MenuItemList.add(new MenuItem(Material.IRON_INGOT,1,"鉄"));
        this.MenuItemList.add(new MenuItem(Material.NETHERITE_INGOT,1,"ネザーライト"));
        this.MenuItemList.add(new MenuItem(Material.COAL,1,"石炭"));
        this.MenuItemList.add(new MenuItem(Material.QUARTZ,1,"クオーツ"));
        this.MenuItemList.add(new MenuItem(Material.CLAY_BALL,1,"粘土"));
        this.MenuItemList.add(new MenuItem(Material.SLIME_BALL,1,"スライムボール"));
        this.MenuItemList.add(new MenuItem(Material.OAK_LOG,1,"オーク"));
        this.MenuItemList.add(new MenuItem(Material.ACACIA_LOG,1,"アカシア"));
        this.MenuItemList.add(new MenuItem(Material.COBBLESTONE,1,"丸石"));
        this.MenuItemList.add(new MenuItem(Material.SAND,1,"砂"));
        this.MenuItemList.add(new MenuItem(Material.EMERALD,1,"エメラルド"));
        this.MenuItemList.add(new MenuItem(Material.BREAD,1,"パン"));
        this.MenuItemList.add(new MenuItem(Material.TNT,1,"TNT"));
        this.MenuItemList.add(new MenuItem(Material.ARROW,1,"矢"));
        EnchantItem eItem;
        eItem = new EnchantItem(Material.DIAMOND_PICKAXE,"幸運修繕効率強化耐久ピッケル");
        eItem.addEnchant(Enchantment.LOOT_BONUS_BLOCKS,3);
        eItem.addEnchant(Enchantment.MENDING,1);
        eItem.addEnchant(Enchantment.DIG_SPEED,5);
        eItem.addEnchant(Enchantment.DURABILITY,3);
        this.EnchantMenuList.add(eItem);
        eItem = new EnchantItem(Material.DIAMOND_PICKAXE,"シルクタッチ修繕効率強化耐久ピッケル");
        eItem.addEnchant(Enchantment.SILK_TOUCH,1);
        eItem.addEnchant(Enchantment.MENDING,1);
        eItem.addEnchant(Enchantment.DIG_SPEED,5);
        eItem.addEnchant(Enchantment.DURABILITY,3);
        this.EnchantMenuList.add(eItem);
        eItem = new EnchantItem(Material.FISHING_ROD,"宝釣り修繕入れ食い耐久釣り竿");
        eItem.addEnchant(Enchantment.LUCK,3);
        eItem.addEnchant(Enchantment.MENDING,1);
        eItem.addEnchant(Enchantment.LURE,3);
        eItem.addEnchant(Enchantment.DURABILITY,3);
        this.EnchantMenuList.add(eItem);
    }
    public boolean StartMarket(Player player){
        if (!market_run_flag) {
            if (!market_exist_flag) {
                market = new MarketObject(0);
                market_exist_flag = true;
                player.sendMessage("市場実体化");
                teams = scoreboard.getTeams().toArray();
                for (Object teamObj : teams) {
                    Team team = (Team)teamObj;
                    player.sendMessage(team.toString());
                    Object[]  members= team.getEntries().toArray();
                    for (Object member : members){
                        if(player.getName().equals(member)){
                            player.sendMessage(team.toString());
                        }
                    }
                }
            }
            market_run_flag = true;
            player.sendMessage("市場開始");
        }else {
            player.sendMessage("市場はすでに起動中です");
        }
        return true;
    }
    public boolean StopMarket(Player player,boolean delete_all){
        if(market_run_flag) {
            market_run_flag = false;
            player.chat("市場停止");
            if (market_exist_flag) {
                if (delete_all) {
                    DestroyMarket(player);
                }
            }else{
                player.sendMessage("予期しないバグが発生して市場の生成されないまま動いていました。");
            }
        }else{
            if (market_exist_flag){
                if (delete_all) {
                    DestroyMarket(player);
                }else {
                    player.sendMessage("市場はすでに停止です");
                }
            }else{
                player.sendMessage("市場がありません");
            }
        }
        return true;
    }
    public boolean PlaceMarket(Player player){
        player.chat("商人を置きます");
        Villager Customer = (Villager) player.getWorld().spawnEntity(player.getLocation(), EntityType.VILLAGER);
        Customer.setCustomName("取引商人");
        Customer.addScoreboardTag("Customer");
        Customer.setInvulnerable(true);
        Customer.setRemoveWhenFarAway(false);
        return true;
    }
    public boolean Tax(Player player) {
        if (market_run_flag) {
            player.sendMessage("徴税します");
        } else {
            player.sendMessage("市場が動いていません");
        }
        return true;
    }

    public void DestroyMarket(Player player){
        market_exist_flag = false;
        player.sendMessage("市場破壊");
        player.sendMessage("hhh");
        for (World world : Bukkit.getWorlds()) {
            player.sendMessage("aaa");
            for (Entity entity : world.getLivingEntities()) {
                if (entity instanceof Villager) {
                    player.sendMessage("ccc");
                    if (entity.getScoreboardTags().contains("Customer")) {
                        player.sendMessage("ddd");
                        ((Villager) entity).setHealth(0);
                        player.sendMessage("eee");
                    }
                }
            }
        }
        player.sendMessage("fff");
    }

    public void CustomerClick(Player player, Entity ClickedEntity){
        if(ClickedEntity instanceof Villager) {
            Villager ClickedCustomer = (Villager)ClickedEntity;
            if(ClickedCustomer.getScoreboardTags().contains("Customer")) {
                MakeMainMenu(player);
            }
        }
    }
    public boolean isMenu(Player player){
        return player_is_in_Menu.getOrDefault(player.getName(),false);
    }
    public void MenuClick(Player player, ItemStack item, int ClickedSlot) {
        if (item != null) {
            if (!item.getType().isAir()) {
                if (player_inv_state.getOrDefault(player.getName(), INDEX_OF_SUB_MENU) == INDEX_OF_MAIN_MENU) {
                    if (ClickedSlot < MenuItemList.size()) {
                        if (ClickedSlot == INDEX_ENCHANT) {
                            MakeEnchantMenu(player);
                        } else {
                            MakeSubMenu(player, ClickedSlot);
                        }
                    }
                } else if (player_inv_state.getOrDefault(player.getName(), INDEX_OF_SUB_MENU) >= 1) {
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
                } else if (player_inv_state.getOrDefault(player.getName(), INDEX_OF_SUB_MENU) == INDEX_ENCHANT) {
                    if (ClickedSlot >= 0 & ClickedSlot < EnchantMenuList.size()) {
                        HashMap<Integer, ItemStack> temp;
                        temp = AddEnchantItemToPlayer(player,  ClickedSlot);
                        player.sendMessage(Integer.valueOf(temp.get(0).getAmount()).toString());
                    } else if (ClickedSlot >= 9 & ClickedSlot < EnchantMenuList.size() + 9) {
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
    public void MenuClose(Player player) {
        if(player_is_in_Menu.getOrDefault(player.getName(),false) ){
            player_is_in_Menu.put(player.getName(), false);
            player_inv_state.put(player.getName(), INDEX_OF_SUB_MENU);
        }
    }

    private void MakeMainMenu(Player player){
        final Inventory MainMenu = Bukkit.createInventory(null, NUM_OF_INV_BOX, "trade_menu");
        for (MenuItem menuItem : MenuItemList) {
            MainMenu.addItem(menuItem.GetItem());
        }
        player.openInventory((MainMenu));
        player_is_in_Menu.put(player.getName(), true);
        player_inv_state.put(player.getName(), INDEX_OF_MAIN_MENU);
    }

    private void MakeSubMenu(Player player,int ClickedSlot){
        final Material SelectedMaterial = MenuItemList.get(ClickedSlot).getMaterial();
        final String SelectedName = MenuItemList.get(ClickedSlot).getName();
        final Inventory submenu = Bukkit.createInventory(null, NUM_OF_INV_BOX, "trade_menu");
        final ArrayList<MenuItem> SubMenuItemList = new ArrayList<>();
        for(Integer Amount : TradeAmountList) {
            SubMenuItemList.add(new MenuItem(SelectedMaterial, Amount, SelectedName));
        }
        for (int i = TradeAmountList.size();i<9;i++) {
            SubMenuItemList.add(new MenuItem(Material.GLASS_PANE, 1, "押せない"));
        }
        for(Integer Amount : TradeAmountList) {
            SubMenuItemList.add(new MenuItem(SelectedMaterial, Amount, SelectedName));
        }
        for (int i = TradeAmountList.size() + 9;i<26;i++) {
            SubMenuItemList.add(new MenuItem(Material.GLASS_PANE, 1, "押せない"));
        }
        SubMenuItemList.add(new MenuItem(Material.CHEST, 1, "戻る"));
        int i = 0;
        for (MenuItem menuItem : SubMenuItemList) {
            menuItem.setLore("aaa", "aaa");
            submenu.setItem(i,menuItem.GetItem());
            i++;
        }
        player.openInventory(submenu);
        player_inv_state.put(player.getName(), ClickedSlot);
        player_is_in_Menu.put(player.getName(), true);
    }
    private void MakeEnchantMenu(Player player){

        final Inventory EnchantMenu = Bukkit.createInventory(null, NUM_OF_INV_BOX, "trade_menu");
        final ArrayList<MenuItem> EnchantMenuItemList = new ArrayList<>();
        for(EnchantItem EnchantItem : EnchantMenuList) {
            EnchantMenuItemList.add(new MenuItem(EnchantItem.getMaterial(), 1, EnchantItem.getName()));
        }
        for (int i = EnchantMenuList.size();i<9;i++) {
            EnchantMenuItemList.add(new MenuItem(Material.GLASS_PANE, 1, "押せない"));
        }
        for(EnchantItem EnchantItem : EnchantMenuList) {
            EnchantMenuItemList.add(new MenuItem(EnchantItem.getMaterial(), 1, EnchantItem.getName()));
        }
        for (int i = EnchantMenuList.size() + 9 ;i<26;i++) {
            EnchantMenuItemList.add(new MenuItem(Material.GLASS_PANE, 1, "押せない"));
        }
        EnchantMenuItemList.add(new MenuItem(Material.CHEST, 1, "戻る"));
        int i = 0;
        for (MenuItem menuItem : EnchantMenuItemList) {
            menuItem.setLore("aaa", "aaa");
            EnchantMenu.setItem(i,menuItem.GetItem());
            i++;
        }
        player.openInventory(EnchantMenu);
        player_inv_state.put(player.getName(), INDEX_ENCHANT);
        player_is_in_Menu.put(player.getName(), true);
    }

    private HashMap<Integer,ItemStack> AddItemToPlayer(Player player,int TradeIndex,int AmountIndex){
        final Material SelectedMaterial = MenuItemList.get(TradeIndex).getMaterial();
        Inventory PlayerInventory = player.getInventory();
        return PlayerInventory.addItem(new ItemStack(SelectedMaterial,TradeAmountList.get(AmountIndex)));
    }

    private HashMap<Integer,ItemStack> RemoveItemFromPlayer(Player player,int TradeIndex,int AmountIndex){
        final Material SelectedMaterial = MenuItemList.get(TradeIndex).getMaterial();
        Inventory PlayerInventory = player.getInventory();
        return PlayerInventory.removeItem(new ItemStack(SelectedMaterial,TradeAmountList.get(AmountIndex)));
    }

    private HashMap<Integer,ItemStack> AddEnchantItemToPlayer(Player player, int EnchantIndex){
        Inventory PlayerInventory = player.getInventory();
        EnchantItem Item = EnchantMenuList.get(EnchantIndex);
        ItemStack EnchantItemStack = new ItemStack(Item.getMaterial(),1);
        int size = Item.getNumEnchant();
        for(int i = 0;i < size;i ++){
            EnchantItemStack.addEnchantment(Item.getEnchant(i),Item.getLevel(i));
        }
        return PlayerInventory.addItem(EnchantItemStack);
    }

    private HashMap<Integer,ItemStack> RemoveEnchantItemFromPlayer(Player player, int EnchantIndex){
        Inventory PlayerInventory = player.getInventory();
        EnchantItem Item = EnchantMenuList.get(EnchantIndex);
        ItemStack EnchantItemStack = new ItemStack(Item.getMaterial(),1);
        int size = Item.getNumEnchant();
        for(int i = 0;i < size;i ++){
            EnchantItemStack.addEnchantment(Item.getEnchant(i),Item.getLevel(i));
        }
        return PlayerInventory.removeItem(EnchantItemStack);
    }
}
