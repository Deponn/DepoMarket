package depo_market.depo_market_1_16_5;

import depo_market.depo_market_1_16_5.PropertiesAndConstant.Const;
import depo_market.depo_market_1_16_5.PropertiesAndConstant.MoneyDisAd;
import depo_market.depo_market_1_16_5.PropertiesAndConstant.MyProperties;
import depo_market.depo_market_1_16_5.PropertiesAndConstant.TempProperties;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.Map;
import java.util.List;

/**
 * 核となるオペレータークラス。
 */
public class PluginOperator {

    private boolean isRun;
    private MyProperties myProperties;
    private MyOperators myOperators;
    private TempProperties tempProperties;
    private boolean isExistingData;

    //必要なオブジェクトを実体化し保持
    public PluginOperator() {
        this.isRun = false;
        this.tempProperties = new TempProperties(MoneyDisAd.Health);
        this.myProperties = new MyProperties(tempProperties);
        this.myOperators = new MyOperators();
        this.isExistingData = false;
    }
    public void InitializeMarket() {
        if (!isRun) {
            myOperators = new MyOperators();
            tempProperties = new TempProperties(MoneyDisAd.Health);
            myProperties = new MyProperties(tempProperties);
            isExistingData = true;
            Bukkit.getLogger().info("市場を初期化しました");
        } else {
            Bukkit.getLogger().info("市場が動いてる間は初期化できません");
        }
    }
    public void InitializeMarket(Player player) {
        if (!isRun) {
            myOperators = new MyOperators();
            tempProperties = new TempProperties(MoneyDisAd.Health);
            myProperties = new MyProperties(tempProperties);
            isExistingData = true;
            player.sendMessage("市場を初期化しました");
        } else {
            player.sendMessage("市場が動いてる間は初期化できません");
        }
    }

    //データをロードする。初期化してからコンフィグにセーブデータがあれば呼ばれる
    public void LoadData(Map<String, Float> teamData, Map<String, ItemPrice> marketData, boolean isRun, MoneyDisAd disadvantage) {
        myOperators.market.loadData(marketData);
        myOperators.teamOperator.LoadData(teamData);
        myOperators.teamOperator.setAllTeamHealth();

        tempProperties = new TempProperties(disadvantage);
        myProperties = new MyProperties(tempProperties);

        this.isRun = isRun;
        isExistingData = true;
    }

    public Map<String, ItemPrice> getMarketData() {
        return myOperators.market.getData();
    }

    public boolean getMarketState() {
        return isRun;
    }

    public MoneyDisAd getDisadvantage() {
        return myProperties.Disadvantage;
    }

    public Map<String, Float> getTeamMoneyData() {
        return myOperators.teamOperator.getData();
    }

    public void StartMarket() {
        if(isExistingData) {
            if(!isRun) {
                myProperties = new MyProperties(tempProperties);
                isRun = true;
                Bukkit.getLogger().info("市場を開きます");
            }
            else {
                Bukkit.getLogger().info("市場はすでに起動中です");
            }
        }else {
            Bukkit.getLogger().info("データが存在しないので初期化してから初めてください");
        }
    }
    public void StartMarket(Player player){
        if(isExistingData) {
            if(!isRun) {
                isRun = true;
                player.sendMessage("市場を開きます");
            }
            else {
                player.sendMessage("市場はすでに起動中です");
            }
        }else {
            player.sendMessage("データが存在しないので初期化してから初めてください");
        }
    }
    public void StopMarket() {
        if(isRun) {
            isRun = false;
            myOperators.teamOperator.resetAllTeamHealth();
            myOperators.teamOperator.ScoreBoardDestroy();
            Bukkit.getLogger().info("市場停止");
        }else {
            Bukkit.getLogger().info("市場はすでに停止です");
        }
    }
    public void StopMarket(Player player) {
        if(isRun) {
            isRun = false;
            myOperators.teamOperator.resetAllTeamHealth();
            myOperators.teamOperator.ScoreBoardDestroy();
            player.sendMessage("市場停止");
        }else {
            player.sendMessage("市場はすでに停止です");
        }
    }
    public void LoadNewTeams(Player player) {
        myOperators.teamOperator.LoadNewTeams();
        player.sendMessage("チームリロードしました");
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
        if (isRun) {
            Bukkit.getLogger().info(targetTeamName+"に"+ Amount + "円だけお金をあげました");
            myOperators.teamOperator.addTeamMoney(targetTeamName, Amount);
        } else {
            Bukkit.getLogger().info("市場が動いていません");
        }
    }

    public void SetDisAdvantage(Player player, MoneyDisAd disadvantageName) {
        if (!isRun) {
            tempProperties.Disadvantage = disadvantageName;
            if (tempProperties.Disadvantage == MoneyDisAd.None) {
                player.sendMessage("借金デバフなしに設定しました。");
            } else if (tempProperties.Disadvantage == MoneyDisAd.Health) {
                player.sendMessage("借金デバフとしてHPが減るようになりました");
            } else if (tempProperties.Disadvantage == MoneyDisAd.DisableBuy) {
                player.sendMessage("借金できないように設定しました");
            }
        } else {
            player.sendMessage("市場を止めてから設定してください");
        }
    }

    //すべてのチームの所持金を確認
    public void LookTeams(Player player) {
        Map<String, Float> teamData = myOperators.teamOperator.getData();
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
        if(isRun) {
            Killer.sendMessage("キルしたのでチームが" + Const.PrizeMoney + "円を獲得しました。");
            KilledPlayer.sendMessage("キルされたので敵チームが" + Const.PrizeMoney + "円を獲得しました");
            myOperators.teamOperator.addTeamMoney(Killer,  Const.PrizeMoney);
            myOperators.playerOperatorMap.get(Killer.getName()).addPlayerMoney(Const.PrizeMoney);
        }
    }
    public void KillEvent(Player KilledPlayer) {
        if (isRun) {
            KilledPlayer.sendMessage("キルではないため、所持金の変動はありません");
        }
    }
    public void LookScore(Player player){
        for (World world : Bukkit.getWorlds()) {
            List<Player> players = world.getPlayers();
            for (Player targetPlayer : players) {
                if (myOperators.playerOperatorMap.containsKey(targetPlayer.getName())) {
                    player.chat(targetPlayer.getName() + ":     " + myOperators.playerOperatorMap.get(targetPlayer.getName()).getPlayerMoney());
                }
            }
        }
    }

    //村人をクリックしたときに商人タグがあれば取引メニューに移行
    public void CustomerClick(Player player, Entity ClickedEntity) {
        if (isRun) {
            if (ClickedEntity instanceof Villager) {
                Villager ClickedCustomer = (Villager) ClickedEntity;
                if (ClickedCustomer.getScoreboardTags().contains(Const.CUSTOMER_NAME)) {
                    if (!myOperators.playerOperatorMap.containsKey(player.getName())) {
                        myOperators.playerOperatorMap.put(player.getName(), new PlayerOperator(player, myOperators,myProperties));
                    }
                    myOperators.playerOperatorMap.get(player.getName()).MakeMainMenu();
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
        if (myOperators.playerOperatorMap.containsKey(player.getName())) {
            return myOperators.playerOperatorMap.get(player.getName()).isInMenu();
        }
        return false;
    }
    //プレイヤーがメニュー状態でインベントリクリックなら呼ばれる。メニュー処理オブジェクトを呼ぶ
    public void MenuClick(Player player, ItemStack item, int ClickedSlot) {
        if (item != null) {
            if (!item.getType().isAir()) {
                myOperators.playerOperatorMap.get(player.getName()).MenuClick(ClickedSlot);
            }
        }
    }

    public boolean MenuClose(Player player) {
        if (myOperators.playerOperatorMap.containsKey(player.getName())) {
            return myOperators.playerOperatorMap.get(player.getName()).MenuClose();
        } else {
            return false;
        }
    }

    //プレイヤーが死んでもHPが減ったままにする
    public void setPlayerHealth(Player player) {
        if(isRun) {
            myOperators.teamOperator.setPlayerTeamHealth(player);
        }
    }
}
