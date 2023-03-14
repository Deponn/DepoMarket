package depo_market.depo_market_1_16_5.ItemStack;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * メニューに表示するアイテムのデータを保持するオブジェクト。アイテム種類と日本語の名前とアイテム説明欄を保持
 */
public class ItemStackData {

    private final Material material;
    private final String NameJP;
    private List<String> Lore;

    public ItemStackData(Material material , String nameJP){
        this.material = material;
        this.NameJP = nameJP;
        Lore = Arrays.asList("","");
    }
    public ItemStackData(ItemStackData me){
        this.material = me.getMaterial();
        this.NameJP = me.getJpName();
        Lore = Arrays.asList("","");
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
