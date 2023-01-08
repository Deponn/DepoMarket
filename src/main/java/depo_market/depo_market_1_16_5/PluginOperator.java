package depo_market.depo_market_1_16_5;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * 核となるオペレータークラス。
 */
public class PluginOperator {

    private final String CUSTOMER_NAME = "Depo_Customer";
    public final MarketOperator market;
    public final TeamMoneyOperator teamMoneyOperator;
    public final DataBaseTradeItem dataBaseTradeItem;
    public final MenuMaker menuMaker;
    private final Map<String, PlayersMenuOperator> playersMenuOperators;
    private String Disadvantage;
    private boolean existData;

    //必要なオブジェクトを実体化し保持
    public PluginOperator() {
        this.dataBaseTradeItem = new DataBaseTradeItem();
        this.market = new MarketOperator(dataBaseTradeItem.getInitialPriceList());
        this.teamMoneyOperator = new TeamMoneyOperator();
        this.playersMenuOperators = new HashMap<>();
        this.menuMaker = new MenuMaker(27, 9, market);
        this.Disadvantage = "health";
        this.existData = false;
    }
    //データをロードする。初期化してからコンフィグにセーブデータがあれば呼ばれる
    public void LoadData(Map<String, Float> teamData, Map<String, ItemPrice> marketData, boolean isRun, String disadvantage, List<World> world) {
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
        return market.getMarketState();
    }

    public String getDisadvantageName() {
        return Disadvantage;
    }

    public Map<String, Float> getTeamMoneyData() {
        return teamMoneyOperator.getData();
    }

    private void InitializeMarket() {
        if (!market.getMarketState()) {
            market.Initialize(dataBaseTradeItem.getInitialPriceList());
            teamMoneyOperator.Initialize();
            existData = true;
            Bukkit.getLogger().info("市場を初期化しました");
        } else {
            Bukkit.getLogger().info("市場が動いてる間は初期化できません");
        }
    }

    public boolean StartMarket() {
        InitializeMarket();
        if(existData) {
            market.StartMarket();
        }else {
            Bukkit.getLogger().info("データが存在しないので初期化してから初めてください");
        }
        return true;
    }

    public boolean StopMarket() {
        market.StopMarket();
        teamMoneyOperator.resetTeamHealth();
        return true;
    }

    //商人として村人を生む。村人にはタグ付けして管理。
    public boolean PlaceCustomer(Player player) {
        Villager Customer = (Villager) player.getWorld().spawnEntity(player.getLocation(), EntityType.VILLAGER);
        Customer.setCustomName("取引商人");
        Customer.addScoreboardTag(CUSTOMER_NAME);
        Customer.setInvulnerable(true);
        Customer.setRemoveWhenFarAway(false);
        return true;
    }
    //タグ付けされた村人を殺す
    public boolean KillAllCustomer() {
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

    public boolean GiveMoney( String targetTeamName, Float Amount) {
        if (market.getMarketState()) {
            Bukkit.getLogger().info(targetTeamName+"に"+ Amount + "円だけお金をあげました");
            teamMoneyOperator.addTeamMoney(targetTeamName, Amount);
        } else {
            Bukkit.getLogger().info("市場が動いていません");
        }
        return true;
    }

    public boolean SetDisAdvantage(Player player, String disadvantageName) {
        if (!market.getMarketState()) {
            Disadvantage = disadvantageName;
            switch (Disadvantage) {
                case "none":
                    player.sendMessage("借金デバフなしに設定しました");
                    break;
                case "health":
                    player.sendMessage("借金デバフとしてHPが減るようになりました");
                    break;
                case "disable_buy":
                    player.sendMessage("借金できないように設定しました");
            }
        } else {
            player.sendMessage("市場を止めてから設定してください");
        }
        return true;
    }

    //すべてのチームの所持金を確認
    public boolean LookTeams(Player player) {
        Map<String, Float> teamData = teamMoneyOperator.getData();
        Set<String> teams = teamData.keySet();
        for (String key : teams) {
            if (Math.round(teamData.get(key)) < 0) {
                player.sendMessage(ChatColor.RED + key + ":" + "     " + Math.round(teamData.get(key)) + "円");
            }else {
                player.sendMessage(key + ":" + "     " + Math.round(teamData.get(key)) + "円");
            }
        }
        return true;
    }
    public void KillEvent(Player Killer,Player KilledPlayer) {
        Killer.sendMessage("キルしたので1000獲得");
        KilledPlayer.sendMessage("キルされたので1000失った");
        teamMoneyOperator.addTeamMoney(Killer, 1000);
        teamMoneyOperator.addPlayerMoney(Killer, 1000);
        teamMoneyOperator.addTeamMoney(KilledPlayer, -1000);
        teamMoneyOperator.addPlayerMoney(KilledPlayer, -1000);
    }
    public void KillEvent(Player KilledPlayer) {
        KilledPlayer.sendMessage("死んでしまったため1000失った");
        teamMoneyOperator.addTeamMoney(KilledPlayer, -1000);
        teamMoneyOperator.addPlayerMoney(KilledPlayer, -1000);
    }
    public boolean LookScore(Player player){
        for (World world : Bukkit.getWorlds()) {
            List<Player> players = world.getPlayers();
            for (Player targetPlayer : players){
                player.chat(targetPlayer.getName() + ":     " + teamMoneyOperator.getPlayerMoney(targetPlayer));
            }
        }
        return true;
    }

    //村人をクリックしたときに商人タグがあれば取引メニューに移行
    public void CustomerClick(Player player, Entity ClickedEntity) {
        if (market.getMarketState()) {
            if (ClickedEntity instanceof Villager) {
                Villager ClickedCustomer = (Villager) ClickedEntity;
                if (ClickedCustomer.getScoreboardTags().contains(CUSTOMER_NAME)) {
                    if (!playersMenuOperators.containsKey(player.getName())) {
                        playersMenuOperators.put(player.getName(), new PlayersMenuOperator(player, this));
                    }
                    playersMenuOperators.get(player.getName()).MakeMainMenu();
                }
            }
        } else {
            player.sendMessage("市場が閉鎖しているため取引できません");
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
                playersMenuOperators.get(player.getName()).MenuClick(ClickedSlot, Disadvantage);
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
        teamMoneyOperator.setTeamHealth(player);
    }
}
