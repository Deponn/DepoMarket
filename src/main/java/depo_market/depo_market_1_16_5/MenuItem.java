package depo_market.depo_market_1_16_5;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Objects;

public class MenuItem {

    private final Material material;
    private final int amount;
    private final String name;
    private final List<String> lore;

    public MenuItem(Material material, int amount, String name, List<String> lore){
        this.material = material;
        this.amount = amount;
        this.name = name;
        this.lore = lore;
    }

    public ItemStack GetItem(){
        final ItemStack item = new ItemStack(material, amount);
        final ItemMeta meta = item.getItemMeta();
        Objects.requireNonNull(meta).setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}
