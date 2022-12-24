package depo_market.depo_market_1_16_5;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ItemStackData {

    private final Material material;
    private final String NameJP;
    private List<String> Lore;

    public ItemStackData(Material material , String nameJP){
        this.material = material;
        this.NameJP = nameJP;
        Lore = Arrays.asList("None","None");
    }
    public void setLore(String text1,String text2) {
        Lore = Arrays.asList(text1, text2);
    }
    public ItemStack getItem(){
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();
        Objects.requireNonNull(meta).setDisplayName(NameJP);
        meta.setLore(Lore);
        item.setItemMeta(meta);
        return item;
    }
    public Material getMaterial(){
        return material;
    }
    public String getJpName(){
        return NameJP;
    }
    public List<String> getLore(){
        return Lore;
    }
}
