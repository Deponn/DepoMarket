package depo_market.depo_market_1_16_5;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class PlayersMenuOperator {
    private final int SLOT_OF_ENCHANT = 0;
    private final int INDEX_OF_MAIN_MENU = -1;
    private final int INDEX_OF_OUT_OF_MENU = -2;
    private final Player player;
    private final MarketOperator market;
    private final TeamMoneyOperator teamMoneyOperator;
    private final DataBaseTradeItem dataBaseTradeItem;
    private final MenuMaker menuMaker;
    private Boolean player_is_in_Menu;
    private Integer player_inv_state;

    public PlayersMenuOperator(Player thisPlayer, MarketOperator Market, TeamMoneyOperator teamOperator, DataBaseTradeItem dataBase, MenuMaker maker) {
        this.market = Market;
        this.teamMoneyOperator = teamOperator;
        this.dataBaseTradeItem = dataBase;
        this.menuMaker = maker;
        this.player = thisPlayer;
        this.player_is_in_Menu = false;
        this.player_inv_state = INDEX_OF_OUT_OF_MENU;
    }

    public boolean isMenu() {
        return player_is_in_Menu;
    }

    public void MakeMainMenu() {
        final Inventory MainMenu = menuMaker.MainMenu(dataBaseTradeItem.getMenuSlotList(), teamMoneyOperator.getTeamMoney(player));
        player.openInventory((MainMenu));
        player_is_in_Menu = true;
        player_inv_state = INDEX_OF_MAIN_MENU;
    }

    private void MakeSubMenu(int ClickedSlot) {
        final Material SelectedMaterial = dataBaseTradeItem.getMenuSlotList().get(ClickedSlot).getMaterial();
        final String SelectedJpName = dataBaseTradeItem.getMenuSlotList().get(ClickedSlot).getJpName();
        final String SelectedEnName = dataBaseTradeItem.getMenuSlotList().get(ClickedSlot).getEnName();
        final Inventory submenu = menuMaker.SubMenu(SelectedMaterial, SelectedJpName, SelectedEnName, dataBaseTradeItem.getTradeAmountList(), teamMoneyOperator.getTeamMoney(player));
        player.openInventory(submenu);
        player_inv_state = ClickedSlot;
        player_is_in_Menu = true;
    }

    private void MakeEnchantMenu() {
        final Inventory EnchantMenu = menuMaker.EnchantMenu(dataBaseTradeItem.getItemEnchantList(), teamMoneyOperator.getTeamMoney(player));
        player.openInventory(EnchantMenu);
        player_inv_state = SLOT_OF_ENCHANT;
        player_is_in_Menu = true;
    }

    public void MenuClick(int ClickedSlot, String disadvantage) {

        if (player_inv_state == INDEX_OF_MAIN_MENU) {
            if (ClickedSlot < dataBaseTradeItem.getMenuSlotList().size()) {
                if (ClickedSlot == SLOT_OF_ENCHANT) {
                    MakeEnchantMenu();
                } else {
                    MakeSubMenu(ClickedSlot);
                }
            } else if (ClickedSlot == 25) {
                player.sendMessage(String.valueOf(teamMoneyOperator.getTeamMoney(player)));
            } else if (ClickedSlot == 26) {
                player.closeInventory();
            }
        } else if (player_inv_state >= 1) {
            if (ClickedSlot >= 0 & ClickedSlot < dataBaseTradeItem.getTradeAmountList().size()) {
                BuyItem(player_inv_state, ClickedSlot, disadvantage);
                MakeSubMenu(player_inv_state);
            } else if (ClickedSlot >= 9 & ClickedSlot < dataBaseTradeItem.getTradeAmountList().size() + 9) {
                SellItem(player_inv_state, ClickedSlot - 9, disadvantage);
                MakeSubMenu(player_inv_state);
            } else if (ClickedSlot == 25) {
                player.sendMessage(String.valueOf(teamMoneyOperator.getTeamMoney(player)));
            } else if (ClickedSlot == 26) {
                MakeMainMenu();
            }
        } else if (player_inv_state == SLOT_OF_ENCHANT) {
            if (ClickedSlot >= 0 & ClickedSlot < dataBaseTradeItem.getItemEnchantList().size()) {
                BuyEnchantItem(ClickedSlot,disadvantage);
                MakeSubMenu(player_inv_state);
            } else if (ClickedSlot >= 9 & ClickedSlot < dataBaseTradeItem.getItemEnchantList().size() + 9) {
                SellEnchantItem(ClickedSlot - 9, disadvantage);
                MakeSubMenu(player_inv_state);
            } else if (ClickedSlot == 25) {
                player.sendMessage(String.valueOf(teamMoneyOperator.getTeamMoney(player)));
            } else if (ClickedSlot == 26) {
                MakeMainMenu();
            }
        }
    }

    public boolean MenuClose() {
        if (player_is_in_Menu) {
            player_is_in_Menu = false;
            player_inv_state = INDEX_OF_OUT_OF_MENU;
            return true;
        }
        return false;
    }

    private void BuyItem(int TradeIndex, int AmountIndex, String disadvantage) {
        if (teamMoneyOperator.PlayerInTeam(player)) {
            if (disadvantage.equals("disable_buy") && teamMoneyOperator.getTeamMoney(player) <= 0) {
                player.sendMessage("お金が足りません");
            } else {
                TradeItem(TradeIndex, AmountIndex, true);
                if(disadvantage.equals("health")){
                    teamMoneyOperator.setTeamHealth(player);
                }
            }
        } else {
            player.sendMessage("チームに入っていません");
        }
    }

    private void SellItem(int TradeIndex, int AmountIndex, String disadvantage) {
        if (teamMoneyOperator.PlayerInTeam(player)) {
            TradeItem(TradeIndex, AmountIndex, false);
            if(disadvantage.equals("health")){
                teamMoneyOperator.setTeamHealth(player);
            }
        } else {
            player.sendMessage("チームに入っていません");
        }
    }

    private void TradeItem(int TradeIndex, int AmountIndex, boolean isBuy) {
            final Material SelectedMaterial = dataBaseTradeItem.getMenuSlotList().get(TradeIndex).getMaterial();
            final String SelectedEnName = dataBaseTradeItem.getMenuSlotList().get(TradeIndex).getEnName();
            final int tradeAmount = dataBaseTradeItem.getTradeAmountList().get(AmountIndex);
            final ItemStack TradeItemStack = new ItemStack(SelectedMaterial, tradeAmount);
            Trade(isBuy, TradeItemStack,tradeAmount,SelectedEnName);
    }

    private void BuyEnchantItem(int EnchantIndex,String disadvantage) {
        if (teamMoneyOperator.PlayerInTeam(player)) {
            if (disadvantage.equals("disable_buy") && teamMoneyOperator.getTeamMoney(player) <= 0) {
                player.sendMessage("お金が足りません");
            } else {
                TradeEnchantItem(EnchantIndex,true);
                if(disadvantage.equals("health")){
                    teamMoneyOperator.setTeamHealth(player);
                }
            }
        } else {
            player.sendMessage("チームに入っていません");
        }
    }

    private void SellEnchantItem(int EnchantIndex, String disadvantage) {
        if (teamMoneyOperator.PlayerInTeam(player)) {
            TradeEnchantItem(EnchantIndex,false);
            if(disadvantage.equals("health")){
                teamMoneyOperator.setTeamHealth(player);
            }
        } else {
            player.sendMessage("チームに入っていません");
        }
    }

    private void TradeEnchantItem(int EnchantIndex, boolean isBuy) {
            final ItemEnchantData Item = dataBaseTradeItem.getItemEnchantList().get(EnchantIndex);
            final Material selectedMaterial = Item.getMaterial();
            final String SelectedEnName = Item.getEnName();
            final int tradeAmount = 1;
            final ItemStack EnchantItemStack = new ItemStack(selectedMaterial, tradeAmount);
            int size = Item.getNumEnchant();
            for (int i = 0; i < size; i++) {
                EnchantItemStack.addEnchantment(Item.getEnchant(i), Item.getLevel(i));
            }
            Trade(isBuy,EnchantItemStack,tradeAmount,SelectedEnName);
    }

    private void Trade(boolean isBuy,ItemStack TradeItemStack,int tradeAmount,String SelectedEnName){
        final Inventory PlayerInventory = player.getInventory();
        HashMap<Integer, ItemStack> aaa;
        int tradedAmount;
        float earnMoney;
        if (isBuy) {
            aaa = PlayerInventory.addItem(TradeItemStack);
            if (aaa.containsKey(0)) {
                tradedAmount = tradeAmount - aaa.get(0).getAmount();
            } else {
                tradedAmount = tradeAmount;
            }
            earnMoney = - market.buy(SelectedEnName, tradedAmount);
        }else {
            aaa = PlayerInventory.removeItem(TradeItemStack);
            if (aaa.containsKey(0)) {
                tradedAmount = tradeAmount - aaa.get(0).getAmount();
            } else {
                tradedAmount = tradeAmount;
            }
            earnMoney = market.sell(SelectedEnName, tradedAmount);
        }
        teamMoneyOperator.addTeamMoney(player, earnMoney);
    }

}
