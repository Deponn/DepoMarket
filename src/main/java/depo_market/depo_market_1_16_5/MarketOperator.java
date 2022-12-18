package depo_market.depo_market_1_16_5;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Arrays;
import java.util.Objects;
import java.util.Map;
import java.util.HashMap;

public class MarketOperator {

    private final Depo_Market_1_16_5 plugin;
    private MarketObject market;
    private boolean market_run_flag = false;
    private boolean market_exist_flag = false;
    private final Map<String, Integer>player_inventory = new HashMap<>();;
    private final Inventory menu;

    public MarketOperator(Depo_Market_1_16_5 plugin){
        this.plugin = plugin;

        this.menu = Bukkit.createInventory(null, 27, "trade_menu");

        final ItemStack item = new ItemStack(Material.DIAMOND_SWORD, 1);
        final ItemMeta meta = item.getItemMeta();

        Objects.requireNonNull(meta).setDisplayName("ダイヤモンド剣");
        meta.setLore(Arrays.asList("First line","Second line"));
        item.setItemMeta(meta);
        this.menu.addItem(item);
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
                player_inventory.put(player.getName(),1);
                player.openInventory(menu);
            }
        }
    }
    public boolean isMenu(Player player){
        return player_inventory.get(player.getName()) == 1 || player_inventory.get(player.getName()) == 2;
    }
    public void MenuClick(Player player, ItemStack item, int slot){
        if (item != null) {
            if(!item.getType().isAir()){
                if(player_inventory.get(player.getName()) == 1 ) {
                    player_inventory.put(player.getName(), 2);
                    player.sendMessage("1クリックしました:" + slot);
                } else if (player_inventory.get(player.getName()) == 2) {
                    player.sendMessage("2クリックしました:" + slot);
                }
            }
        }
    }
    public void MenuClose(Player player) {
        player_inventory.remove(player.getName());
    }
}
