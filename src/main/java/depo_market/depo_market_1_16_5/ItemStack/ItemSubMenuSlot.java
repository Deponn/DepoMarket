package depo_market.depo_market_1_16_5.ItemStack;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

/**
 * メニューに表示するアイテムのデータを保持するオブジェクトの拡張。アイテムの量も保持
 */
public class ItemSubMenuSlot extends ItemStackData {

    private final int Amount;

    public ItemSubMenuSlot(Material material, String nameJP , int amount) {
        super(material, nameJP);
        this.Amount = amount;
    }
    public ItemStack getItem(){
        final ItemStack item = new ItemStack(super.getMaterial(), Amount);
        final ItemMeta meta = item.getItemMeta();
        Objects.requireNonNull(meta).setDisplayName(super.getJpName());
        meta.setLore(super.getLore());
        item.setItemMeta(meta);
        return item;
    }
}
