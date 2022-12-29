package depo_market.depo_market_1_16_5;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class PluginOperator {

    private final String CUSTOMER_NAME = "Depo_Customer";
    private final MarketOperator market;
    private final TeamMoneyOperator teamMoneyOperator;
    private final DataBaseTradeItem dataBaseTradeItem;
    private final MenuMaker menuMaker;
    private final Map<String ,PlayersMenuOperator> playersMenuOperators;
    private String Disadvantage;
    public PluginOperator() {
        this.dataBaseTradeItem = new DataBaseTradeItem();
        this.market = new MarketOperator(dataBaseTradeItem.getInitialPriceList());
        this.teamMoneyOperator = new TeamMoneyOperator();
        this.playersMenuOperators = new HashMap<>();
        this.menuMaker = new MenuMaker(27, 9, market);
        this.Disadvantage = "none";
    }

    public void LoadData(Map<String, Float> teamData, Map<String, ItemPrice> marketData, boolean isRun,String disadvantage) {
        market.loadData(isRun, marketData);
        teamMoneyOperator.LoadTeams(teamData);
        Disadvantage = disadvantage;
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

    public boolean InitializeMarket(Player player) {
        if (!market.getMarketState()) {
            market.Initialize(dataBaseTradeItem.getInitialPriceList());
            teamMoneyOperator.Initialize();
            player.sendMessage("市場を初期化する");
        }else {
            player.sendMessage("市場が動いてる間は初期化できません");
        }
        return true;
    }

    public boolean StartMarket(Player player) {
        market.StartMarket(player);
        return true;
    }

    public boolean StopMarket(Player player) {
        market.StopMarket(player);
        return true;
    }

    public boolean PlaceCustomer(Player player) {
        Villager Customer = (Villager) player.getWorld().spawnEntity(player.getLocation(), EntityType.VILLAGER);
        Customer.setCustomName("取引商人");
        Customer.addScoreboardTag(CUSTOMER_NAME);
        Customer.setInvulnerable(true);
        Customer.setRemoveWhenFarAway(false);
        return true;
    }

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

    public boolean Tax(Player player,String targetTeamName,Float Amount) {
        if (market.getMarketState()) {
            player.sendMessage("徴税します");
            teamMoneyOperator.addTeamMoney(player,Amount);
            teamMoneyOperator.addTeamMoney(targetTeamName,-Amount);
        } else {
            player.sendMessage("市場が動いていません");
        }
        return true;
    }

    public boolean GiveMoney(Player player,String targetTeamName,Float Amount) {
        if (market.getMarketState()) {
            player.sendMessage("お金をあげます");
            teamMoneyOperator.addTeamMoney(player,-Amount);
            teamMoneyOperator.addTeamMoney(targetTeamName,Amount);
        } else {
            player.sendMessage("市場が動いていません");
        }
        return true;
    }

    public boolean SetDisAdvantage(Player player,String disadvantageName) {
        Disadvantage = disadvantageName;
        switch (Disadvantage) {
            case "none":
                player.sendMessage("借金デバフなし");
                break;
            case "health":
                player.sendMessage("借金デバフはHPが減る");
                break;
            case "disable_buy":
                player.sendMessage("借金できない");
        }
        return true;
    }

    public boolean ReloadTeam(Player player) {
        player.sendMessage("チームリロード");
        teamMoneyOperator.reLoadTeams();
        return true;
    }
    public boolean LookTeams(Player player){
        Map<String,Float> teamData = teamMoneyOperator.getData();
        Set<String> teams = teamData.keySet();
        for (String key : teams){
            player.sendMessage(key + teamData.get(key));
        }
        return true;
    }

    public void CustomerClick(Player player, Entity ClickedEntity) {
        if (market.getMarketState()) {
            if (ClickedEntity instanceof Villager) {
                Villager ClickedCustomer = (Villager) ClickedEntity;
                if (ClickedCustomer.getScoreboardTags().contains(CUSTOMER_NAME)) {
                    if(!playersMenuOperators.containsKey(player.getName())) {
                        playersMenuOperators.put(player.getName(),new PlayersMenuOperator(player,market,teamMoneyOperator,dataBaseTradeItem,menuMaker));
                    }
                    playersMenuOperators.get(player.getName()).MakeMainMenu();
                }
            }
        } else {
            player.sendMessage("閉鎖しています");
        }
    }

    public boolean isMenu(Player player) {
        if(playersMenuOperators.containsKey(player.getName())) {
            return playersMenuOperators.get(player.getName()).isMenu();
        }
        return false;
    }
    public void MenuClick(Player player, ItemStack item, int ClickedSlot) {
        if (item != null) {
            if (!item.getType().isAir()) {
                playersMenuOperators.get(player.getName()).MenuClick(ClickedSlot,Disadvantage);
            }
        }
    }

    public boolean MenuClose(Player player) {
        return playersMenuOperators.get(player.getName()).MenuClose();
    }


}
