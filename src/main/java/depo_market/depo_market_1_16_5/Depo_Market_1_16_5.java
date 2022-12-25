package depo_market.depo_market_1_16_5;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public final class Depo_Market_1_16_5 extends JavaPlugin implements Listener{

    PluginOperator Operator;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        Objects.requireNonNull(this.getCommand("tax")).setTabCompleter(new CommandSuggest());
        Objects.requireNonNull(this.getCommand("give_money")).setTabCompleter(new CommandSuggest());
        Objects.requireNonNull(this.getCommand("set_disadvantage")).setTabCompleter(new CommandSuggest());
        Operator = new PluginOperator();
        FileConfiguration configuration = getConfig();
        boolean ConfExist = configuration.contains("Depo_isRun");
        if(ConfExist){
            boolean isRun = configuration.getBoolean("Depo_isRun");
            List<String> TeamNames = configuration.getStringList("Depo_teams");
            List<Float> TeamMoneys = configuration.getFloatList("Depo_moneys");
            List<String> ItemNames = configuration.getStringList("Depo_Items");
            List<Float> ItemPrices = configuration.getFloatList("Depo_Prices");
            List<Integer> ItemBuy = configuration.getIntegerList("Depo_buy");
            List<Integer> ItemSell = configuration.getIntegerList("Depo_sell");
            Map<String,Float> TeamData = new HashMap<>();
            for (int i = 0; i < TeamNames.size(); i++) {
                TeamData.put(TeamNames.get(i),TeamMoneys.get(i));
            }
            Map<String,ItemPrice> MarketData = new HashMap<>();
            for (int i = 0; i < ItemNames.size(); i++) {
                MarketData.put(ItemNames.get(i),new ItemPrice(ItemPrices.get(i),ItemBuy.get(i),ItemSell.get(i)));
            }
            Operator.LoadData(TeamData,MarketData,isRun);
        }
        getLogger().info("Depo_Marketが有効化されました。");
    }

    @Override
    public void onDisable() {
        getLogger().info("Depo_Marketが無効化されました。");
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        // プレイヤーがコマンドを投入した際の処理...
        if (!(sender instanceof Player)) {
            // コマブロやコンソールからの実行の場合
            sender.sendMessage(ChatColor.DARK_GRAY + "このコマンドはプレイヤーのみが使えます。");
            return true;
        }
        Player player = (Player) sender;

        // コマンド処理...
        if (cmd.getName().equalsIgnoreCase("initialize_market")) {
            return Operator.InitializeMarket(player);

        } else if (cmd.getName().equalsIgnoreCase("start_market")) {
            return Operator.StartMarket(player);

        } else if (cmd.getName().equalsIgnoreCase("stop_market")) {
            return Operator.StopMarket(player);

        } else if (cmd.getName().equalsIgnoreCase("place_customer")) {
            return Operator.PlaceCustomer(player);

        } else if (cmd.getName().equalsIgnoreCase("kill_all_customer")) {
            return Operator.KillAllCustomer();

        } else if (cmd.getName().equalsIgnoreCase("tax")) {
            //コマンド引数を処理
            CommandParser parser = CommandParser.parse_tax(sender, args);
            if (!parser.isSuccess) {
                // パース失敗
                return true;
            }
            return Operator.Tax(player);
        } else if (cmd.getName().equalsIgnoreCase("give_money")) {
            //コマンド引数を処理
            CommandParser parser = CommandParser.parse_tax(sender, args);
            if (!parser.isSuccess) {
                // パース失敗
                return true;
            }
            return Operator.GiveMoney(player);
        } else if (cmd.getName().equalsIgnoreCase("set_disadvantage")) {
            //コマンド引数を処理
            CommandParser parser = CommandParser.parse_disadvantage(sender, args);
            if (!parser.isSuccess) {
                // パース失敗
                return true;
            }
            return Operator.SetDisAdvantage(player);

        } else if (cmd.getName().equalsIgnoreCase("reload_team")) {
            return Operator.ReloadTeam(player);
        }
        return true;
    }

    @EventHandler
    public void onEntityClick(PlayerInteractEntityEvent e) {
        Operator.CustomerClick(e.getPlayer(),e.getRightClicked());
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (Operator.isMenu((Player) e.getWhoClicked())) {
            e.setCancelled(true);
            Operator.MenuClick((Player) e.getWhoClicked(),e.getCurrentItem(),e.getRawSlot());
        }
    }
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        boolean saveFlag;
        saveFlag = Operator.MenuClose((Player) e.getPlayer());
        if(saveFlag){
            getLogger().info("セーブ");
            saveData();
        }
    }


    public void saveData(){
        Map<String, ItemPrice> MarketData = Operator.getMarketData();
        Map<String, Float> teamData = Operator.getTeamMoneyData();
        boolean MarketState = Operator.getMarketState();
        List<String> TeamNames = new ArrayList<>(teamData.keySet());
        List<Float> TeamMoneys = new ArrayList<>();
        for(String team : TeamNames){
            TeamMoneys.add(teamData.get(team));
        }
        List<String> ItemNames = new ArrayList<>(MarketData.keySet());
        List<Float> ItemPrices = new ArrayList<>();
        List<Integer> ItemBuy = new ArrayList<>();
        List<Integer> ItemSell = new ArrayList<>();
        for(String item : ItemNames){
            ItemPrices.add(MarketData.get(item).getPrice());
            ItemBuy.add(MarketData.get(item).getAmountOfBought());
            ItemSell.add(MarketData.get(item).getAmountOfSold());
        }
        FileConfiguration configuration = getConfig();;
        configuration.set("Depo_isRun",MarketState);
        configuration.set("Depo_teams",TeamNames);
        configuration.set("Depo_moneys",TeamMoneys);
        configuration.set("Depo_Items",ItemNames);
        configuration.set("Depo_Prices",ItemPrices);
        configuration.set("Depo_buy",ItemBuy);
        configuration.set("Depo_sell",ItemSell);
        saveConfig();
    }

}

// destroyにわける、市場作る、金動かす、セーブ機能