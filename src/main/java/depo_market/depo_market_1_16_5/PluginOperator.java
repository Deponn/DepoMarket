package depo_market.depo_market_1_16_5;

import depo_market.depo_market_1_16_5.PropertiesAndConstant.Const;
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
    private MyOperators myOperators;
    private boolean isExistingData;

    //必要なオブジェクトを実体化し保持
    public PluginOperator() {
        this.isRun = false;
        this.myOperators = new MyOperators();
        this.isExistingData = false;
    }
    public void InitializeMarket() {
        if (!isRun) {
            myOperators = new MyOperators();
            isExistingData = true;
            Bukkit.getLogger().info("市場を初期化しました");
        } else {
            Bukkit.getLogger().info("市場が動いてる間は初期化できません");
        }
    }
    public void InitializeMarket(Player player) {
        if (!isRun) {
            myOperators = new MyOperators();
            isExistingData = true;
            player.sendMessage("市場を初期化しました");
        } else {
            player.sendMessage("市場が動いてる間は初期化できません");
        }
    }

    //データをロードする。初期化してからコンフィグにセーブデータがあれば呼ばれる
    public void LoadData(Map<String, Float> teamData, Map<String, ItemPrice> marketData, boolean isRun) {
        myOperators.market.loadData(marketData);
        myOperators.teamOp.LoadData(teamData);
        myOperators.teamOp.setAllTeamHealth();
        this.isRun = isRun;
        isExistingData = true;
    }

    public Map<String, ItemPrice> getMarketData() {
        return myOperators.market.getData();
    }

    public boolean getMarketState() {
        return isRun;
    }

    public Map<String, Float> getTeamMoneyData() {
        return myOperators.teamOp.getData();
    }

    public void StartMarket() {
        if(isExistingData) {
            if(!isRun) {
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
            myOperators.teamOp.resetAllTeamHealth();
            myOperators.teamOp.ScoreBoardDestroy();
            Bukkit.getLogger().info("市場停止");
        }else {
            Bukkit.getLogger().info("市場はすでに停止です");
        }
    }
    public void StopMarket(Player player) {
        if(isRun) {
            isRun = false;
            myOperators.teamOp.resetAllTeamHealth();
            myOperators.teamOp.ScoreBoardDestroy();
            player.sendMessage("市場停止");
        }else {
            player.sendMessage("市場はすでに停止です");
        }
    }
    public void LoadNewTeams(Player player) {
        myOperators.teamOp.LoadNewTeams();
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
            myOperators.teamOp.addTeamMoney(targetTeamName, Amount);
        } else {
            Bukkit.getLogger().info("市場が動いていません");
        }
    }

    //すべてのチームの所持金を確認
    public void LookTeams(Player player) {
        Map<String, Float> teamData = myOperators.teamOp.getData();
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
            Killer.sendMessage("キルしたのでチームが" + myOperators.prop.PrizeMoney + "円を獲得しました。");
            KilledPlayer.sendMessage("キルされたので敵チームが" + myOperators.prop.PrizeMoney + "円を獲得しました");
            myOperators.teamOp.addTeamMoney(Killer,  myOperators.prop.PrizeMoney);
            myOperators.getPlayerOperator(Killer).addPlayerMoney(myOperators.prop.PrizeMoney);
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
                if (myOperators.getPlayerOperator(player) != null) {
                    player.chat(targetPlayer.getName() + ":     " + myOperators.getPlayerOperator(player).getPlayerMoney());
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
                    myOperators.addPlayerOperator(player);
                    myOperators.getPlayerOperator(player).MakeMainMenu();
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
        if (myOperators.getPlayerOperator(player) != null) {
            return myOperators.getPlayerOperator(player).isInMenu();
        }
        return false;
    }
    //プレイヤーがメニュー状態でインベントリクリックなら呼ばれる。メニュー処理オブジェクトを呼ぶ
    public void MenuClick(Player player, ItemStack item, int ClickedSlot) {
        if (item != null) {
            if (!item.getType().isAir()) {
                myOperators.getPlayerOperator(player).MenuClick(ClickedSlot);
            }
        }
    }

    public boolean MenuClose(Player player) {
        if (myOperators.getPlayerOperator(player) != null) {
            return myOperators.getPlayerOperator(player).MenuClose();
        } else {
            return false;
        }
    }

    //プレイヤーが死んでもHPが減ったままにする
    public void setPlayerHealth(Player player) {
        if(isRun) {
            myOperators.teamOp.setPlayerTeamHealth(player);
        }
    }
}
