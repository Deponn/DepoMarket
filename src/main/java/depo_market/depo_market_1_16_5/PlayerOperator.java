package depo_market.depo_market_1_16_5;

import depo_market.depo_market_1_16_5.ItemStack.ItemEnchantData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * プレイヤーがメニューを操作するときのクラス。プレイヤーのメニューの状態も保持。わけるべきか微妙。
 */
public class PlayerOperator {
    private final int SLOT_OF_ENCHANT = 0;
    private final int INDEX_OF_MAIN_MENU = -1;
    private final int INDEX_OF_OUT_OF_MENU = -2;
    private final String playerName;
    private final MyOperators operator;
    private final MyProperties properties;
    private Boolean player_is_in_Menu;
    private Integer player_inv_state;
    private float playerMoney;

    //様々なデータを得るためにデータを保持するオブジェクトのコピーをまとめてもらっている。
    public PlayerOperator(Player player, MyOperators operator,MyProperties myProperties) {
        this.operator = operator;
        this.properties = myProperties;
        this.playerName = player.getName();
        this.player_is_in_Menu = false;
        this.player_inv_state = INDEX_OF_OUT_OF_MENU;
        this.playerMoney = 0f;
    }

    public boolean isInMenu() {
        return player_is_in_Menu;
    }

    /**
     * クリックしたスロットごとに処理をおこなうメソッド。アイテム選択メニューではおしたスロット番号がアイテム番号となり記録される。
     * 取引アイテムのリストも番号と連動している。
     */
    public void MenuClick(int ClickedSlot) {
        Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
            //メインメニューのとき。選んだアイテムのサブメニューに行く処理
            if (player_inv_state == INDEX_OF_MAIN_MENU) {
                if (ClickedSlot < operator.dataBaseTradeItem.getMenuSlotList().size()) {
                    if (ClickedSlot == SLOT_OF_ENCHANT) {
                        MakeEnchantMenu();
                    } else {
                        MakeSubMenu(ClickedSlot);
                    }
                } else if (ClickedSlot == 25) {
                    player.sendMessage(Math.round(operator.teamOperator.getTeamMoney(player)) + "円");
                } else if (ClickedSlot == 26) {
                    player.closeInventory();
                }
                //サブメニューのとき。選んだ場合売り買いをし、メニューを更新
            } else if (player_inv_state >= 1) {
                String EnName = operator.dataBaseTradeItem.getMenuSlotList().get(player_inv_state).getEnName();
                Material material = operator.dataBaseTradeItem.getMenuSlotList().get(player_inv_state).getMaterial();
                //50000円以上の取引ができない
                if (ClickedSlot >= 0 & ClickedSlot < operator.dataBaseTradeItem.getTradeAmountList().size()) {
                    int Amount = operator.dataBaseTradeItem.getTradeAmountList().get(ClickedSlot);
                    if (operator.market.getPrice(EnName) * Amount < Const.BOUND_OF_BUY) {
                        BuyItem(material, EnName, Amount);
                    }
                    MakeSubMenu(player_inv_state);
                } else if (ClickedSlot >= 9 & ClickedSlot < operator.dataBaseTradeItem.getTradeAmountList().size() + 9) {
                    int Amount = operator.dataBaseTradeItem.getTradeAmountList().get(ClickedSlot - 9);
                    if (operator.market.getPrice(EnName) * Amount < Const.BOUND_OF_BUY) {
                        SellItem(material, EnName, Amount);
                    }
                    MakeSubMenu(player_inv_state);

                } else if (ClickedSlot == 25) {
                    player.sendMessage(Math.round(operator.teamOperator.getTeamMoney(player)) + "円");
                } else if (ClickedSlot == 26) {
                    MakeMainMenu();
                }
                //エンチャントアイテム取引メニューのとき。選んだ場合売り買いをし、メニューを更新
            } else if (player_inv_state == SLOT_OF_ENCHANT) {
                if (ClickedSlot >= 0 & ClickedSlot < operator.dataBaseTradeItem.getItemEnchantList().size()) {
                    BuyEnchantItem(ClickedSlot);
                    MakeEnchantMenu();
                } else if (ClickedSlot >= 9 & ClickedSlot < operator.dataBaseTradeItem.getItemEnchantList().size() + 9) {
                    SellEnchantItem(ClickedSlot - 9);
                    MakeEnchantMenu();
                } else if (ClickedSlot == 25) {
                    player.sendMessage(Math.round(operator.teamOperator.getTeamMoney(player)) + "円");
                } else if (ClickedSlot == 26) {
                    MakeMainMenu();
                }
            }
        }
    }
    public void MakeMainMenu() {
        Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
            final Inventory MainMenu = MenuMaker.MainMenu(operator.dataBaseTradeItem.getMenuSlotList(), operator.teamOperator.getTeamMoney(player), operator.market);
            player.openInventory((MainMenu));
            player_is_in_Menu = true;
            player_inv_state = INDEX_OF_MAIN_MENU;
        }
    }

    private void MakeSubMenu(int ClickedSlot) {
        Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
            final Material SelectedMaterial = operator.dataBaseTradeItem.getMenuSlotList().get(ClickedSlot).getMaterial();
            final String SelectedJpName = operator.dataBaseTradeItem.getMenuSlotList().get(ClickedSlot).getJpName();
            final String SelectedEnName = operator.dataBaseTradeItem.getMenuSlotList().get(ClickedSlot).getEnName();
            final Inventory submenu = MenuMaker.SubMenu(SelectedMaterial, SelectedJpName, SelectedEnName, operator.dataBaseTradeItem.getTradeAmountList(), operator.teamOperator.getTeamMoney(player), operator.market);
            player.openInventory(submenu);
            player_inv_state = ClickedSlot;
            player_is_in_Menu = true;
        }
    }

    private void MakeEnchantMenu() {
        Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
            final Inventory EnchantMenu = MenuMaker.EnchantMenu(operator.dataBaseTradeItem.getItemEnchantList(), operator.teamOperator.getTeamMoney(player), operator.market);
            player.openInventory(EnchantMenu);
            player_inv_state = SLOT_OF_ENCHANT;
            player_is_in_Menu = true;
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

    //買いの時、買えるか判定して取引処理メソッドを呼ぶ。
    // 取引メソッドは普通アイテム用とエンチャントアイテム用があり、それらは統合された取引メソッドを呼ぶ
    private void BuyItem(Material SelectedMaterial,String SelectedEnName, int tradeAmount) {
        Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
            if (operator.teamOperator.isPlayerInAnyTeam(player)) {
                if (properties.Disadvantage == MoneyDisAd.DisableBuy && operator.teamOperator.getTeamMoney(player) <= 0) {
                    player.sendMessage("お金が足りません");
                } else {
                    if (operator.teamOperator.getTeamMoney(player) >= -Const.BOUND_OF_MONEY) {
                        TradeItem(SelectedMaterial, SelectedEnName, tradeAmount, true);
                        if (properties.Disadvantage  == MoneyDisAd.Health) {
                            operator.teamOperator.setPlayerTeamHealth(player);
                        }
                    } else {
                        player.sendMessage("これ以上借金できません");
                    }

                }
            } else {
                player.sendMessage("チームに入っていません");
            }
        }
    }

    //売りの時、売れるか判定して取引処理メソッドを呼ぶ
    private void SellItem(Material SelectedMaterial,String SelectedEnName, int tradeAmount) {
        Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
            if (operator.teamOperator.isPlayerInAnyTeam(player)) {
                if (operator.teamOperator.getTeamMoney(player) <= Const.BOUND_OF_MONEY * 10) {
                    TradeItem(SelectedMaterial, SelectedEnName, tradeAmount, false);
                    if (properties.Disadvantage  == MoneyDisAd.Health) {
                        operator.teamOperator.setPlayerTeamHealth(player);
                    }
                } else {
                    player.sendMessage("これ以上貯金できません");
                }
            } else {
                player.sendMessage("チームに入っていません");
            }
        }
    }

    // 取引メソッドは普通アイテム用とエンチャントアイテム用があり、それらは統合された取引メソッドを呼ぶ
    private void TradeItem(Material SelectedMaterial,String SelectedEnName, int tradeAmount, boolean isBuy) {
        final ItemStack TradeItemStack = new ItemStack(SelectedMaterial, tradeAmount);
        Trade(isBuy, TradeItemStack, tradeAmount, SelectedEnName);
    }

    //買いの時、買えるか判定して取引処理メソッドを呼ぶ。
    private void BuyEnchantItem(int EnchantIndex) {
        Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
            if (operator.teamOperator.isPlayerInAnyTeam(player)) {
                if (properties.Disadvantage  == MoneyDisAd.DisableBuy && operator.teamOperator.getTeamMoney(player) <= 0) {
                    player.sendMessage("お金が足りません");
                } else {
                    if (operator.teamOperator.getTeamMoney(player) >= -Const.BOUND_OF_MONEY) {
                        TradeEnchantItem(EnchantIndex, true);
                        if (properties.Disadvantage  == MoneyDisAd.Health) {
                            operator.teamOperator.setPlayerTeamHealth(player);
                        }
                    } else {
                        player.sendMessage("これ以上借金できません");
                    }
                }
            } else {
                player.sendMessage("チームに入っていません");
            }
        }
    }

    //売りの時、売れるか判定して取引処理メソッドを呼ぶ
    private void SellEnchantItem(int EnchantIndex) {
        Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
            if (operator.teamOperator.isPlayerInAnyTeam(player)) {
                if (operator.teamOperator.getTeamMoney(player) <= Const.BOUND_OF_MONEY * 10) {
                    TradeEnchantItem(EnchantIndex, false);
                    if (properties.Disadvantage  == MoneyDisAd.Health) {
                        operator.teamOperator.setPlayerTeamHealth(player);
                    }
                } else {
                    player.sendMessage("これ以上貯金できません");
                }
            } else {
                player.sendMessage("チームに入っていません");
            }
        }
    }

    // 取引メソッドは普通アイテム用とエンチャントアイテム用があり、それらは統合された取引メソッドを呼ぶ
    private void TradeEnchantItem(int EnchantIndex, boolean isBuy) {
        final ItemEnchantData Item = operator.dataBaseTradeItem.getItemEnchantList().get(EnchantIndex);
        final Material selectedMaterial = Item.getMaterial();
        final String SelectedEnName = Item.getEnName();
        final int tradeAmount = 1;
        final ItemStack EnchantItemStack = new ItemStack(selectedMaterial, tradeAmount);
        int size = Item.getNumEnchant();
        for (int i = 0; i < size; i++) {
            EnchantItemStack.addEnchantment(Item.getEnchant(i), Item.getLevel(i));
        }
        Trade(isBuy, EnchantItemStack, tradeAmount, SelectedEnName);
    }
    //統合された取引メソッド。アイテムを受け渡し、受け渡しできなかった量を引き、金額を計算。そのお金をチームに加算。
    private void Trade(boolean isBuy, ItemStack TradeItemStack, int tradeAmount, String SelectedEnName) {
        Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
            final Inventory PlayerInventory = player.getInventory();
            HashMap<Integer, ItemStack> remainItem;
            int tradedAmount;
            float earnMoney;
            if (isBuy) {
                remainItem = PlayerInventory.addItem(TradeItemStack);//アイテム受け渡し
                if (remainItem.containsKey(0)) {
                    tradedAmount = tradeAmount - remainItem.get(0).getAmount();//受け渡しできなかった量を引く
                } else {
                    tradedAmount = tradeAmount;
                }
                earnMoney = -operator.market.buy(SelectedEnName, tradedAmount);//金額を計算。値段も変動
            } else {
                remainItem = PlayerInventory.removeItem(TradeItemStack);//アイテム受け渡し
                if (remainItem.containsKey(0)) {
                    tradedAmount = tradeAmount - remainItem.get(0).getAmount();//受け渡しできなかった量を引く
                } else {
                    tradedAmount = tradeAmount;
                }
                earnMoney = operator.market.sell(SelectedEnName, tradedAmount);//金額を計算。値段も変動
            }
            operator.teamOperator.addTeamMoney(player, earnMoney);//お金をチームに加算。
            addPlayerMoney(earnMoney);//お金をプレイヤーに加算。
        }
    }
    public void addPlayerMoney(float money){
        float oldMoney = playerMoney;
        playerMoney = oldMoney + money;
    }

    public float getPlayerMoney(){
        return playerMoney;
    }
}
