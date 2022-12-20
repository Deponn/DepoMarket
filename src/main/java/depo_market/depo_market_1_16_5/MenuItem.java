package depo_market.depo_market_1_16_5;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MenuItem {

    private final Material material;
    private final int amount;
    private final String name;
    private List<String> lore = Arrays.asList("first line" , "second line");

    public MenuItem(Material material, int amount, String name){
        this.material = material;
        this.amount = amount;
        this.name = name;
    }

    public ItemStack GetItem(){
        final ItemStack item = new ItemStack(material, amount);
        final ItemMeta meta = item.getItemMeta();
        Objects.requireNonNull(meta).setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public void setLore(String first,String second){
        lore = Arrays.asList(first,second);
    }

    public Material getMaterial(){
        return material;
    }
    public String getName(){
        return name;
    }
}
