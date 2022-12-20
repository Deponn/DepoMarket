package depo_market.depo_market_1_16_5;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.util.ArrayList;

public class EnchantItem {
    private final ArrayList<Enchantment> enchants;
    private final String Name;
    private final ArrayList<Integer> EnchantLevels;
    private final Material material;
    public EnchantItem(Material material, String name){
        this.material = material;
        this.Name = name;
        this.enchants = new ArrayList<>();
        this.EnchantLevels = new ArrayList<>();
    }
    public void addEnchant(Enchantment enchant,Integer level){
        enchants.add(enchant);
        EnchantLevels.add(level);
    }
    public int getNumEnchant(){
        return enchants.size();
    }
    public Material getMaterial(){
        return material;
    }
    public String getName(){
        return Name;
    }
    public Enchantment getEnchant(int index){
        return enchants.get(index);
    }

    public Integer getLevel(int index){
        return EnchantLevels.get(index);
    }
}
