package depo_market.depo_market_1_16_5;

import depo_market.depo_market_1_16_5.Data.Const;
import depo_market.depo_market_1_16_5.Data.DBInterface;

import depo_market.depo_market_1_16_5.Data.DBKojosen;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

/**
 * 核となるオペレータークラス。
 */
public class PluginOperator {

    public final MarketOperator market;
    public final TeamMoneyOperator teamMoneyOperator;
    public final DBInterface dataBaseTradeItem;
    private final Map<String, PlayersMenuOperator> playersMenuOperators;
    private MoneyDisAd Disadvantage;
    private boolean existData;

    //必要なオブジェクトを実体化し保持
    public PluginOperator() {
        this.dataBaseTradeItem = new DBKojosen();
        this.market = new MarketOperator(dataBaseTradeItem.getInitialPriceList());
        this.teamMoneyOperator = new TeamMoneyOperator();
        this.playersMenuOperators = new HashMap<>();
        this.Disadvantage = MoneyDisAd.Health;
        this.existData = false;
    }
    //データをロードする。初期化してからコンフィグにセーブデータがあれば呼ばれる
    public void LoadData(Map<String, Float> teamData, Map<String, ItemPrice> marketData, boolean isRun, MoneyDisAd disadvantage, List<World> world) {
        market.loadData(isRun, marketData);
        teamMoneyOperator.LoadTeams(teamData);
        Disadvantage = disadvantage;
        teamMoneyOperator.setAllTeamHealth(world);
        existData = true;
    }

    public Map<String, ItemPrice> getMarketData() {
        return market.getData();
    }

    public boolean getMarketState() {
        return market.isMarketRun();
    }

    public MoneyDisAd getDisadvantage() {
        return Disadvantage;
    }

    public Map<String, Float> getTeamMoneyData() {
        return teamMoneyOperator.getData();
    }

    public void InitializeMarket() {
        if (!market.isMarketRun()) {
            market.Initialize(dataBaseTradeItem.getInitialPriceList());
            teamMoneyOperator.Initialize();
            playersMenuOperators.clear();
            existData = true;
            Bukkit.getLogger().info("市場を初期化しました");
        } else {
            Bukkit.getLogger().info("市場が動いてる間は初期化できません");
        }
    }

    public void StartMarket() {
        if(existData) {
            market.StartMarket();
        }else {
            Bukkit.getLogger().info("データが存在しないので初期化してから初めてください");
        }
    }

    public void StopMarket() {
        market.StopMarket();
        teamMoneyOperator.resetTeamHealth();
        teamMoneyOperator.ScoreBoardDestroy();
    }

    public void LoadNewTeams(Player player) {
        player.sendMessage("チームリロードしました");
        teamMoneyOperator.LoadNewTeams();
    }
    //商人として村人を生む。村人にはタグ付けして管理。
    public void PlaceCustomer(Player player) {
        Villager Customer = (Villager) player.getWorld().spawnEntity(player.getLocation(), EntityType.VILLAGER);
        Customer.setCustomName("取引商人");
        Customer.addScoreboardTag(Const.CUSTOMER_NAME);
        Customer.setInvulnerable(true);
        Customer.setRemoveWhenFarAway(false);
    }
    public void PlaceCustomer(int X,int Y ,int Z) {
        Villager Customer = (Villager) Bukkit.getWorlds().get(0).spawnEntity(new Location(Bukkit.getWorlds().get(0), X,Y,Z), EntityType.VILLAGER);
        Customer.setCustomName("取引商人");
        Customer.addScoreboardTag(Const.CUSTOMER_NAME);
        Customer.setInvulnerable(true);
        Customer.setRemoveWhenFarAway(false);
    }
    //タグ付けされた村人を殺す
    public void KillAllCustomer() {
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getLivingEntities()) {
                if (entity instanceof Villager) {
                    if (entity.getScoreboardTags().contains(Const.CUSTOMER_NAME)) {
                        ((Villager) entity).setHealth(0);
                    }
                }
            }
        }
    }

    public void GiveMoney( String targetTeamName, Float Amount) {
        if (market.isMarketRun()) {
            Bukkit.getLogger().info(targetTeamName+"に"+ Amount + "円だけお金をあげました");
            teamMoneyOperator.addTeamMoney(targetTeamName, Amount);
        } else {
            Bukkit.getLogger().info("市場が動いていません");
        }
    }

    public void SetDisAdvantage(Player player, MoneyDisAd disadvantageName) {
        if (!market.isMarketRun()) {
            Disadvantage = disadvantageName;
            if (Disadvantage == MoneyDisAd.None) {
                player.sendMessage("借金デバフなしに設定しました");
            } else if (Disadvantage == MoneyDisAd.Health) {
                player.sendMessage("借金デバフとしてHPが減るようになりました");
            } else if (Disadvantage == MoneyDisAd.DisableBuy) {
                player.sendMessage("借金できないように設定しました");
            }
        } else {
            player.sendMessage("市場を止めてから設定してください");
        }
    }

    //すべてのチームの所持金を確認
    public void LookTeams(Player player) {
        Map<String, Float> teamData = teamMoneyOperator.getData();
        Set<String> teams = teamData.keySet();
        for (String key : teams) {
            if (Math.round(teamData.get(key)) < 0) {
                player.sendMessage(ChatColor.RED + key + ":" + "     " + Math.round(teamData.get(key)) + "円");
            }else {
                player.sendMessage(key + ":" + "     " + Math.round(teamData.get(key)) + "円");
            }
        }
    }
    public void KillEvent(Player Killer,Player KilledPlayer) {
        if(market.isMarketRun()) {
            Killer.sendMessage("キルしたのでチームが" + Const.PrizeMoney + "円を獲得しました。");
            KilledPlayer.sendMessage("キルされたので敵チームが" + Const.PrizeMoney + "円を獲得しました");
            teamMoneyOperator.addTeamMoney(Killer,  Const.PrizeMoney);
            teamMoneyOperator.addPlayerMoney(Killer,  Const.PrizeMoney);
        }
    }
    public void KillEvent(Player KilledPlayer) {
        if (market.isMarketRun()) {
            KilledPlayer.sendMessage("キルではないため、所持金の変動はありません");
        }
    }
    public void LookScore(Player player){
        for (World world : Bukkit.getWorlds()) {
            List<Player> players = world.getPlayers();
            for (Player targetPlayer : players){
                player.chat(targetPlayer.getName() + ":     " + teamMoneyOperator.getPlayerMoney(targetPlayer));
            }
        }
    }

    //村人をクリックしたときに商人タグがあれば取引メニューに移行
    public void CustomerClick(Player player, Entity ClickedEntity) {
        if (market.isMarketRun()) {
            if (ClickedEntity instanceof Villager) {
                Villager ClickedCustomer = (Villager) ClickedEntity;
                if (ClickedCustomer.getScoreboardTags().contains(Const.CUSTOMER_NAME)) {
                    if (!playersMenuOperators.containsKey(player.getName())) {
                        playersMenuOperators.put(player.getName(), new PlayersMenuOperator(player, this));
                    }
                    playersMenuOperators.get(player.getName()).setPlayer(player);
                    playersMenuOperators.get(player.getName()).MakeMainMenu();
                }
            }
        } else {
            if (ClickedEntity instanceof Villager) {
                Villager ClickedCustomer = (Villager) ClickedEntity;
                if (ClickedCustomer.getScoreboardTags().contains(Const.CUSTOMER_NAME)) {
                    player.sendMessage("市場が閉鎖しているため取引できません");
                }
            }
        }
    }

    //プレイヤーがメニュー状態なら真を返す。返された側は真ならアイテムをインベントリから取れないようにする。
    public boolean isMenu(Player player) {
        if (playersMenuOperators.containsKey(player.getName())) {
            return playersMenuOperators.get(player.getName()).isMenu();
        }
        return false;
    }
    //プレイヤーがメニュー状態でインベントリクリックなら呼ばれる。メニュー処理オブジェクトを呼ぶ
    public void MenuClick(Player player, ItemStack item, int ClickedSlot) {
        if (item != null) {
            if (!item.getType().isAir()) {
                playersMenuOperators.get(player.getName()).MenuClick(ClickedSlot);
            }
        }
    }

    public boolean MenuClose(Player player) {
        if (playersMenuOperators.containsKey(player.getName())) {
            return playersMenuOperators.get(player.getName()).MenuClose();
        } else {
            return false;
        }
    }

    //プレイヤーが死んでもHPが減ったままにする
    public void setPlayerHealth(Player player) {
        if(market.isMarketRun()) {
            teamMoneyOperator.setTeamHealth(player);
        }
    }
}
