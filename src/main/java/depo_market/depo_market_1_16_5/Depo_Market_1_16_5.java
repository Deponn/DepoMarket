package depo_market.depo_market_1_16_5;

import depo_market.depo_market_1_16_5.Command.*;
import depo_market.depo_market_1_16_5.PropertiesAndConstant.ChangeProperties;
import depo_market.depo_market_1_16_5.PropertiesAndConstant.Const;
import net.md_5.bungee.api.ChatColor;
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

    private PluginOperator Operator;//プラグインの処理を実際に行いデータを保持するオペレーターオブジェクトを保持する変数
    private boolean isEnabledPlugin;
    /**
     * 初期処理とコンフィグに保存したデータをロードする
     */
    @Override
    public void onEnable() {
        isEnabledPlugin = false;
        //イベントを受け取れるようにする。
        getServer().getPluginManager().registerEvents(this, this);
        //コマンドのタブコンプリートを実装
        for(CmdName cmdName : CmdName.values()){
            Objects.requireNonNull(this.getCommand(cmdName.getCmd())).setTabCompleter(new CommandSuggest());
        }
        //プラグインの処理を実際に行いデータを保持するオペレーターオブジェクト実体化
        Operator = new PluginOperator();
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

        //コマンドの有効化無効化処理
        if (cmd.getName().equalsIgnoreCase(CmdName.EnablePlugin.getCmd())) {
            loadData();
            isEnabledPlugin = true;
            if ((sender instanceof Player)) {
                sender.sendMessage("プラグイン有効化");
            }
            return true;
        }else if(cmd.getName().equalsIgnoreCase(CmdName.DisablePlugin.getCmd())) {
            saveData();
            isEnabledPlugin = false;
            if ((sender instanceof Player)) {
                sender.sendMessage("プラグイン無効化");
            }
            return true;
        }
        //コマンドが無効なら処理をしない
        if(isEnabledPlugin) {
            if (cmd.getName().equalsIgnoreCase(CmdName.InitializeMarket.getCmd())) {
                if ((sender instanceof Player)) {
                    Operator.InitializeMarket((Player) sender);
                }else {
                    Operator.InitializeMarket();
                }
                return true;
            } else if (cmd.getName().equalsIgnoreCase(CmdName.StartMarket.getCmd())) {
                if ((sender instanceof Player)) {
                    Operator.StartMarket((Player) sender);
                }else {
                    Operator.StartMarket();
                }
                return true;
            } else if (cmd.getName().equalsIgnoreCase(CmdName.StopMarket.getCmd())) {
                if ((sender instanceof Player)) {
                    Operator.StopMarket((Player) sender);
                }else {
                    Operator.StopMarket();
                }
                saveData();
                return true;
            } else if (cmd.getName().equalsIgnoreCase(CmdName.GiveMoney.getCmd())) {
                //コマンド引数を処理
                CmdParserGiveMoney parser = CmdParserGiveMoney.Parse(sender, args);
                if (!parser.isSuccess()) {
                    // パース失敗
                    return true;
                }
                float amount = parser.amount_of_money; //floatに変換
                Operator.GiveMoney(parser.team_name, amount);
                if ((sender instanceof Player)) {
                    sender.sendMessage(ChatColor.GREEN + parser.team_name + "に" + amount + "円お金をあげました");
                }
                return true;
            } else if (cmd.getName().equalsIgnoreCase(CmdName.SetPointCustomer.getCmd())) {
                CmdParserSetPoint parser = CmdParserSetPoint.parse(sender, args);
                if (!parser.isSuccess()) {
                    // パース失敗
                    return true;
                }
                Operator.PlaceCustomer(parser.X, parser.Y, parser.Z);
                if ((sender instanceof Player)) {
                    sender.sendMessage(ChatColor.GREEN + "商人を" + parser.X + parser.Y + parser.Z + "に置きました");
                }
                return true;
            } else if (cmd.getName().equalsIgnoreCase(CmdName.KillAllCustomer.getCmd())) {
                Operator.KillAllCustomer();
                if ((sender instanceof Player)) {
                    sender.sendMessage(ChatColor.GREEN + "商人を全滅させました。");
                }
                return true;
            }

            // 以降はプレイヤーのみ使えるコマンド
            if (!(sender instanceof Player)) {
                // コマブロやコンソールからの実行の場合
                sender.sendMessage(ChatColor.DARK_GRAY + "このコマンドはプレイヤーのみが使えます。");
                return true;
            }
            Player player = (Player) sender;

            // コマンド処理...
            if (cmd.getName().equalsIgnoreCase(CmdName.PlaceCustomer.getCmd())) {
                Operator.PlaceCustomer(player);
                player.sendMessage("商人設置");
                return true;
            } else if (cmd.getName().equalsIgnoreCase(CmdName.LookTeams.getCmd())) {
                Operator.LookTeams(player);
                return true;
            } else if (cmd.getName().equalsIgnoreCase(CmdName.LookScore.getCmd())) {
                Operator.LookScore(player);
                return true;
            }else if (cmd.getName().equalsIgnoreCase(CmdName.NewTeam.getCmd())) {
                Operator.LoadNewTeams(player);
                return true;
            } else if(cmd.getName().equalsIgnoreCase(CmdName.ChangeProperties.getCmd())){
                CmdParserChangeProperties parser = CmdParserChangeProperties.parse(sender, args);
                if (!parser.isSuccess()) {
                    // パース失敗
                    return true;
                }
                ChangeProperties.PropertiesChange(parser.PropertiesName, parser.Value);
                return true;
            }
        }
        return true;
    }

    //エンティティクリックイベント。商人クリック用
    @EventHandler
    public void onEntityClick(PlayerInteractEntityEvent e) {
        if(isEnabledPlugin) {
            Operator.CustomerClick(e.getPlayer(), e.getRightClicked());
        }
    }
    //インベントリをクリックしたとき、取引メニューの動作を設定
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if(isEnabledPlugin) {
            if (Operator.isMenu((Player) e.getWhoClicked())) {
                e.setCancelled(true);
                Operator.MenuClick((Player) e.getWhoClicked(), e.getCurrentItem(), e.getRawSlot());
            }
        }
    }
    //インベントリを閉じたとき、取引メニュー閉じてるフラグを建てる用、取引メニューを閉じたならコンフィグにセーブ
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if(isEnabledPlugin) {
            boolean saveFlag;
            saveFlag = Operator.MenuClose((Player) e.getPlayer());
            if (saveFlag) {
                saveData();
            }
        }
    }
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e){
        if(isEnabledPlugin) {
            Player player =  e.getEntity();
            Player killer = player.getKiller();
            if (killer != null) {
                Operator.KillEvent(killer, player);
            } else {
                Operator.KillEvent(player);
            }
        }
    }
    //プレイヤーが死んでもHPが減ったままにする
    @EventHandler
    public void onPlayerRevive(PlayerRespawnEvent e){
        if(isEnabledPlugin) {
            Player player = e.getPlayer();
            Operator.setPlayerHealth(player);
        }
    }

    private void loadData(){
        //コンフィグが存在する場合はロードする。リストをそれぞれロードし、マップに変換。順序がそろってる前提
        FileConfiguration configuration = getConfig();
        boolean ConfExist = configuration.contains("Dp_Version");
        if(ConfExist) {
            if (Objects.equals(configuration.getString("Dp_Version"), Const.thisVersion)) {
                boolean isRun = configuration.getBoolean("Dp_isRun");
                List<String> TeamNames = configuration.getStringList("Dp_teams");
                List<Float> TeamMoneys = configuration.getFloatList("Dp_moneys");
                List<String> ItemNames = configuration.getStringList("Dp_Items");
                List<Float> ItemPrices = configuration.getFloatList("Dp_Prices");
                List<Integer> ItemBuy = configuration.getIntegerList("Dp_buy");
                List<Integer> ItemSell = configuration.getIntegerList("Dp_sell");
                Map<String, Float> TeamData = new HashMap<>();
                for (int i = 0; i < TeamNames.size(); i++) {
                    TeamData.put(TeamNames.get(i), TeamMoneys.get(i));
                }
                Map<String, ItemPrice> MarketData = new HashMap<>();
                for (int i = 0; i < ItemNames.size(); i++) {
                    MarketData.put(ItemNames.get(i), new ItemPrice(ItemPrices.get(i), ItemBuy.get(i), ItemSell.get(i)));
                }
                Operator.LoadData(TeamData, MarketData, isRun);
            }
        }
    }

    //コンフィグにセーブ。マップデータを受け取り、そのキーのリストを受け取り、キーにタグ付けられたデータをリスト化していく。キーとデータがそろうはず。
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
        FileConfiguration configuration = getConfig();
        configuration.set("Dp_Version",Const.thisVersion);
        configuration.set("Dp_isRun",MarketState);
        configuration.set("Dp_teams",TeamNames);
        configuration.set("Dp_moneys",TeamMoneys);
        configuration.set("Dp_Items",ItemNames);
        configuration.set("Dp_Prices",ItemPrices);
        configuration.set("Dp_buy",ItemBuy);
        configuration.set("Dp_sell",ItemSell);
        saveConfig();
    }

}

//金額が変わる
//個人でも利用したい
//アイテム変更したい
