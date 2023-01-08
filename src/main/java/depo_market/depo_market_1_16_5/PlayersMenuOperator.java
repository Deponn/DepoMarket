package depo_market.depo_market_1_16_5;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

/**
 * プレイヤーがメニューを操作するときのクラス。プレイヤーのメニューの状態も保持。わけるべきか微妙。
 */
public class PlayersMenuOperator {
    private final int SLOT_OF_ENCHANT = 0;
    private final int INDEX_OF_MAIN_MENU = -1;
    private final int INDEX_OF_OUT_OF_MENU = -2;
    private final float BOUND_OF_MONEY = 500000;
    private final Player player;
    private final PluginOperator operator;
    private Boolean player_is_in_Menu;
    private Integer player_inv_state;

    //様々なデータを得るためにデータを保持するオブジェクトのコピーをまとめてもらっている。
    public PlayersMenuOperator(Player thisPlayer, PluginOperator operator) {
        this.operator = operator;
        this.player = thisPlayer;
        this.player_is_in_Menu = false;
        this.player_inv_state = INDEX_OF_OUT_OF_MENU;
    }

    public boolean isMenu() {
        return player_is_in_Menu;
    }

    /**
     * クリックしたスロットごとに処理をおこなうメソッド。アイテム選択メニューではおしたスロット番号がアイテム番号となり記録される。
     * 取引アイテムのリストも番号と連動している。
     */
    public void MenuClick(int ClickedSlot, String disadvantage) {

        //メインメニューのとき。選んだアイテムのサブメニューに行く処理
        if (player_inv_state == INDEX_OF_MAIN_MENU) {
            if (ClickedSlot < operator.dataBaseTradeItem.getMenuSlotList().size()) {
                if (ClickedSlot == SLOT_OF_ENCHANT) {
                    MakeEnchantMenu();
                } else {
                    MakeSubMenu(ClickedSlot);
                }
            } else if (ClickedSlot == 25) {
                player.sendMessage(Math.round(operator.teamMoneyOperator.getTeamMoney(player)) + "円");
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
                if (operator.market.getPrice(EnName) * Amount < 30000) {
                    BuyItem(material,EnName,Amount, disadvantage);
                }
                MakeSubMenu(player_inv_state);
            } else if (ClickedSlot >= 9 & ClickedSlot <  operator.dataBaseTradeItem.getTradeAmountList().size() + 9) {
                int Amount = operator.dataBaseTradeItem.getTradeAmountList().get(ClickedSlot - 9);
                if (operator.market.getPrice(EnName) * Amount < 30000) {
                    SellItem(material,EnName,Amount, disadvantage);
                }
                MakeSubMenu(player_inv_state);

            } else if (ClickedSlot == 25) {
                player.sendMessage(Math.round(operator.teamMoneyOperator.getTeamMoney(player)) + "円");
            } else if (ClickedSlot == 26) {
                MakeMainMenu();
            }
            //エンチャントアイテム取引メニューのとき。選んだ場合売り買いをし、メニューを更新
        } else if (player_inv_state == SLOT_OF_ENCHANT) {
            if (ClickedSlot >= 0 & ClickedSlot < operator.dataBaseTradeItem.getItemEnchantList().size()) {
                BuyEnchantItem(ClickedSlot, disadvantage);
                MakeEnchantMenu();
            } else if (ClickedSlot >= 9 & ClickedSlot < operator.dataBaseTradeItem.getItemEnchantList().size() + 9) {
                SellEnchantItem(ClickedSlot - 9, disadvantage);
                MakeEnchantMenu();
            } else if (ClickedSlot == 25) {
                player.sendMessage(Math.round(operator.teamMoneyOperator.getTeamMoney(player)) + "円");
            } else if (ClickedSlot == 26) {
                MakeMainMenu();
            }
        }
    }
    public void MakeMainMenu() {
        final Inventory MainMenu = operator.menuMaker.MainMenu(operator.dataBaseTradeItem.getMenuSlotList(), operator.teamMoneyOperator.getTeamMoney(player));
        player.openInventory((MainMenu));
        player_is_in_Menu = true;
        player_inv_state = INDEX_OF_MAIN_MENU;
    }

    private void MakeSubMenu(int ClickedSlot) {
        final Material SelectedMaterial = operator.dataBaseTradeItem.getMenuSlotList().get(ClickedSlot).getMaterial();
        final String SelectedJpName = operator.dataBaseTradeItem.getMenuSlotList().get(ClickedSlot).getJpName();
        final String SelectedEnName = operator.dataBaseTradeItem.getMenuSlotList().get(ClickedSlot).getEnName();
        final Inventory submenu = operator.menuMaker.SubMenu(SelectedMaterial, SelectedJpName, SelectedEnName, operator.dataBaseTradeItem.getTradeAmountList(), operator.teamMoneyOperator.getTeamMoney(player));
        player.openInventory(submenu);
        player_inv_state = ClickedSlot;
        player_is_in_Menu = true;
    }

    private void MakeEnchantMenu() {
        final Inventory EnchantMenu = operator.menuMaker.EnchantMenu(operator.dataBaseTradeItem.getItemEnchantList(), operator.teamMoneyOperator.getTeamMoney(player));
        player.openInventory(EnchantMenu);
        player_inv_state = SLOT_OF_ENCHANT;
        player_is_in_Menu = true;
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
    private void BuyItem(Material SelectedMaterial,String SelectedEnName, int tradeAmount, String disadvantage) {
        if (operator.teamMoneyOperator.PlayerInTeam(player)) {
            if (disadvantage.equals("disable_buy") && operator.teamMoneyOperator.getTeamMoney(player) <= 0) {
                player.sendMessage("お金が足りません");
            } else {
                if (operator.teamMoneyOperator.getTeamMoney(player) >= -BOUND_OF_MONEY) {
                    TradeItem(SelectedMaterial,SelectedEnName,tradeAmount, true);
                    if (disadvantage.equals("health")) {
                        operator.teamMoneyOperator.setTeamHealth(player);
                    }
                } else {
                    player.sendMessage("これ以上借金できません");
                }

            }
        } else {
            player.sendMessage("チームに入っていません");
        }
    }

    //売りの時、売れるか判定して取引処理メソッドを呼ぶ
    private void SellItem(Material SelectedMaterial,String SelectedEnName, int tradeAmount, String disadvantage) {
        if (operator.teamMoneyOperator.PlayerInTeam(player)) {
            if (operator.teamMoneyOperator.getTeamMoney(player) <= BOUND_OF_MONEY) {
                TradeItem(SelectedMaterial,SelectedEnName,tradeAmount,false);
                if (disadvantage.equals("health")) {
                    operator.teamMoneyOperator.setTeamHealth(player);
                }
            } else {
                player.sendMessage("これ以上貯金できません");
            }
        } else {
            player.sendMessage("チームに入っていません");
        }
    }

    // 取引メソッドは普通アイテム用とエンチャントアイテム用があり、それらは統合された取引メソッドを呼ぶ
    private void TradeItem(Material SelectedMaterial,String SelectedEnName, int tradeAmount, boolean isBuy) {
        final ItemStack TradeItemStack = new ItemStack(SelectedMaterial, tradeAmount);
        Trade(isBuy, TradeItemStack, tradeAmount, SelectedEnName);
    }

    //買いの時、買えるか判定して取引処理メソッドを呼ぶ。
    private void BuyEnchantItem(int EnchantIndex, String disadvantage) {
        if (operator.teamMoneyOperator.PlayerInTeam(player)) {
            if (disadvantage.equals("disable_buy") && operator.teamMoneyOperator.getTeamMoney(player) <= 0) {
                player.sendMessage("お金が足りません");
            } else {
                if (operator.teamMoneyOperator.getTeamMoney(player) >= -BOUND_OF_MONEY) {
                    TradeEnchantItem(EnchantIndex, true);
                    if (disadvantage.equals("health")) {
                        operator.teamMoneyOperator.setTeamHealth(player);
                    }
                } else {
                    player.sendMessage("これ以上借金できません");
                }
            }
        } else {
            player.sendMessage("チームに入っていません");
        }
    }

    //売りの時、売れるか判定して取引処理メソッドを呼ぶ
    private void SellEnchantItem(int EnchantIndex, String disadvantage) {
        if (operator.teamMoneyOperator.PlayerInTeam(player)) {
            if (operator.teamMoneyOperator.getTeamMoney(player) <= BOUND_OF_MONEY) {
                TradeEnchantItem(EnchantIndex, false);
                if (disadvantage.equals("health")) {
                    operator.teamMoneyOperator.setTeamHealth(player);
                }
            } else {
                player.sendMessage("これ以上貯金できません");
            }
        } else {
            player.sendMessage("チームに入っていません");
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
            earnMoney = - operator.market.buy(SelectedEnName, tradedAmount);//金額を計算。値段も変動
        } else {
            remainItem = PlayerInventory.removeItem(TradeItemStack);//アイテム受け渡し
            if (remainItem.containsKey(0)) {
                tradedAmount = tradeAmount - remainItem.get(0).getAmount();//受け渡しできなかった量を引く
            } else {
                tradedAmount = tradeAmount;
            }
            earnMoney = operator.market.sell(SelectedEnName, tradedAmount);//金額を計算。値段も変動
        }
        operator.teamMoneyOperator.addTeamMoney(player, earnMoney);//お金をチームに加算。
        operator.teamMoneyOperator.addPlayerMoney(player,earnMoney);//お金をプレイヤーに加算。
    }
}
