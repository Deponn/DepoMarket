package depo_market.depo_market_1_16_5;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public final class Depo_Market_1_16_5 extends JavaPlugin implements Listener{

    PluginOperator Operator;//プラグインの処理を実際に行いデータを保持するオペレーターオブジェクトを保持する変数

    /**
     * 初期処理とコンフィグに保存したデータをロードする
     */
    @Override
    public void onEnable() {
        //イベントを受け取れるようにする。
        getServer().getPluginManager().registerEvents(this, this);
        //コマンドのタブコンプリートを実装
        Objects.requireNonNull(this.getCommand("DpGiveMoney")).setTabCompleter(new CommandSuggest());
        Objects.requireNonNull(this.getCommand("DpSetDisadvantage")).setTabCompleter(new CommandSuggest());
        //プラグインの処理を実際に行いデータを保持するオペレーターオブジェクト実体化
        Operator = new PluginOperator();
        //コンフィグが存在する場合はロードする。リストをそれぞれロードし、マップに変換。順序がそろってる前提
        FileConfiguration configuration = getConfig();
        boolean ConfExist = configuration.contains("Depo_isRun");
        if(ConfExist){
            boolean isRun = configuration.getBoolean("Depo_isRun");
            String disadvantage = configuration.getString("disadvantage");
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
            Operator.LoadData(TeamData,MarketData,isRun,disadvantage, Bukkit.getWorlds());
        }
        getLogger().info("Depo_Marketが有効化されました。");
    }

    @Override
    public void onDisable() {
        getLogger().info("Depo_Marketが無効化されました。");
    }

    /**
     * コマンド処理、コマンドごとに分岐してオペレーターに投げる
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

        if (cmd.getName().equalsIgnoreCase("DpStartMarket")) {
            return Operator.StartMarket();

        } else if (cmd.getName().equalsIgnoreCase("DpStopMarket")) {
            boolean flag = Operator.StopMarket();
            saveData();
            return flag;
        }else if (cmd.getName().equalsIgnoreCase("DpGiveMoney")) {
            //コマンド引数を処理
            CommandParser parser = CommandParser.parse_give_money(sender, args);
            if (!parser.isSuccess) {
                // パース失敗
                return true;
            }
            float amount = parser.amount_of_money;
            return Operator.GiveMoney(parser.team_name, amount);
        }

        // プレイヤーがコマンドを投入した際の処理...
        if (!(sender instanceof Player)) {
            // コマブロやコンソールからの実行の場合
            sender.sendMessage(ChatColor.DARK_GRAY + "このコマンドはプレイヤーのみが使えます。");
            return true;
        }
        Player player = (Player) sender;

        // コマンド処理...
        if (cmd.getName().equalsIgnoreCase("DpPlaceCustomer")) {
            return Operator.PlaceCustomer(player);

        } else if (cmd.getName().equalsIgnoreCase("DpKillAllCustomer")) {
            return Operator.KillAllCustomer();

        }else if (cmd.getName().equalsIgnoreCase("DpSetDisadvantage")) {
            //コマンド引数を処理
            CommandParser parser = CommandParser.parse_disadvantage(sender, args);
            if (!parser.isSuccess) {
                // パース失敗
                return true;
            }
            return Operator.SetDisAdvantage(player,parser.disadvantage);

        }else if (cmd.getName().equalsIgnoreCase("DpLoadNewTeams")) {
            return Operator.LookTeams(player);
        }
        else if (cmd.getName().equalsIgnoreCase("DpLookScore")) {
            return Operator.LookScore(player);
        }
        return true;
    }

    //エンティティクリックイベント。商人クリック用
    @EventHandler
    public void onEntityClick(PlayerInteractEntityEvent e) {
        Operator.CustomerClick(e.getPlayer(),e.getRightClicked());
    }
    //インベントリをクリックしたとき、取引メニューの動作を設定
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (Operator.isMenu((Player) e.getWhoClicked())) {
            e.setCancelled(true);
            Operator.MenuClick((Player) e.getWhoClicked(),e.getCurrentItem(),e.getRawSlot());
        }
    }
    //インベントリを閉じたとき、取引メニュー閉じてるフラグを建てる用、取引メニューを閉じたならコンフィグにセーブ
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        boolean saveFlag;
        saveFlag = Operator.MenuClose((Player) e.getPlayer());
        if(saveFlag){
            saveData();
        }
    }
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e){
        Player player = (Player) e.getEntity();
        Player killer = player.getKiller();
        if(killer != null) {
            Operator.KillEvent(killer, player);
        }else{
            Operator.KillEvent(player);
        }
    }
    //プレイヤーが死んでもHPが減ったままにする
    @EventHandler
    public void onPlayerRevive(PlayerRespawnEvent e){
        Player player = e.getPlayer();
        Operator.setPlayerHealth(player);
    }

    //コンフィグにセーブ。マップデータを受け取り、そのキーのリストを受け取り、キーにタグ付けられたデータをリスト化していく。キーとデータがそろうはず。
    public void saveData(){
        Map<String, ItemPrice> MarketData = Operator.getMarketData();
        Map<String, Float> teamData = Operator.getTeamMoneyData();
        boolean MarketState = Operator.getMarketState();
        String disadvantage = Operator.getDisadvantageName();
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
        configuration.set("disadvantage",disadvantage);
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