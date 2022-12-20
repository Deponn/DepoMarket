package depo_market.depo_market_1_16_5;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.*;

public class MarketOperator {

    private final int InventoryBoxNum = 27;
    private final Depo_Market_1_16_5 plugin;
    private MarketObject market;
    private boolean market_run_flag = false;
    private boolean market_exist_flag = false;

    private final Map<String, Boolean>ItemList = new HashMap<>();
    private final Map<String, Boolean>player_is_in_Menu = new HashMap<>();
    private final Map<String, Integer>player_select_slot = new HashMap<>();
    private final ArrayList<MenuItem> MenuItemList = new ArrayList<>();

    public MarketOperator(Depo_Market_1_16_5 plugin){
        this.plugin = plugin;

        this.MenuItemList.add(new MenuItem(Material.DIAMOND,1,"ダイヤ"));
        this.MenuItemList.add(new MenuItem(Material.IRON_INGOT,1,"鉄"));
        this.MenuItemList.add(new MenuItem(Material.STONE_BRICKS,1,"丸石"));
        this.MenuItemList.add(new MenuItem(Material.SAND,1,"砂"));
        this.MenuItemList.add(new MenuItem(Material.OAK_WOOD,1,"オーク"));
        this.MenuItemList.add(new MenuItem(Material.BREAD,1,"パン"));
        this.MenuItemList.add(new MenuItem(Material.QUARTZ,1,"クオーツ"));
    }
    public boolean StartMarket(Player player){
        if (!market_run_flag) {
            if (!market_exist_flag) {
                market = new MarketObject();
                market_exist_flag = true;
                player.chat("市場実体化");
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
                    market_exist_flag = false;
                    player.sendMessage("市場破壊");
                }
            }else{
                player.sendMessage("予期しないバグが発生して市場の生成されないまま動いていました。");
            }
        }else{
            if (market_exist_flag){
                if (delete_all) {
                    market_exist_flag = false;
                    player.sendMessage("市場破壊");
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
        Customer.setMetadata("Customer", new FixedMetadataValue(plugin,true));
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

    public void CustomerClick(Player player, Entity ClickedEntity){
        if(ClickedEntity instanceof Villager) {
            Villager ClickedCustomer = (Villager)ClickedEntity;
            if(ClickedCustomer.hasMetadata("Customer") && ClickedCustomer.getMetadata("Customer").get(0).asBoolean()) {
                player_is_in_Menu.put(player.getName(), true);
                player_select_slot.put(player.getName(), -1);
                final Inventory MainMenu = Bukkit.createInventory(null, InventoryBoxNum, "trade_menu");
                for (MenuItem menuItem : MenuItemList) {
                    MainMenu.addItem(menuItem.GetItem());
                }
                player.openInventory((MainMenu));
            }
        }
    }
    public boolean isMenu(Player player){
        return player_is_in_Menu.getOrDefault(player.getName(),false);
    }
    public void MenuClick(Player player, ItemStack item, int slot){
        if (item != null) {
            if(!item.getType().isAir()){
                if(player_select_slot.getOrDefault(player.getName(),-2) == -1) {
                    final Material SelectedMaterial = MenuItemList.get(slot).getMaterial();
                    final String SelectedName = MenuItemList.get(slot).getName();
                    final Inventory submenu =  Bukkit.createInventory(null, InventoryBoxNum, "trade_menu");
                    final ArrayList<MenuItem> SubMenuItemList = new ArrayList<>();
                    SubMenuItemList.add(new MenuItem(SelectedMaterial,1,SelectedName));
                    for (MenuItem menuItem : SubMenuItemList) {
                        menuItem.setLore("aaa","aaa");
                        submenu.addItem(menuItem.GetItem());
                    }
                    player.openInventory(submenu);
                    player_select_slot.put(player.getName(), slot);
                    player_is_in_Menu.put(player.getName(),true);
                } else if (player_select_slot.getOrDefault(player.getName(),-2) >= 0) {
                    player.sendMessage("クリックしました:" + slot);
                }
            }
        }
    }
    public void MenuClose(Player player) {
        player_is_in_Menu.put(player.getName(),false);
        player_select_slot.put(player.getName(),-2);
        player.sendMessage("インベントリ解放");
    }

}
