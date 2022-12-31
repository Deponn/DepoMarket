package depo_market.depo_market_1_16_5;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.util.ArrayList;

/**
 * メニューに表示するアイテムのデータを保持するオブジェクトの拡張。つけるエンチャントをリストとして保持するオブジェクト。
 */
public class ItemEnchantData extends ItemMenuSlot {

    private final ArrayList<Enchantment> enchants = new ArrayList<>();
    private final ArrayList<Integer> EnchantLevels = new ArrayList<>();

    public ItemEnchantData(Material material, String nameEn,String nameJP,  float price) {
        super(material, nameEn,nameJP,  price);
    }

    public void addEnchant(Enchantment enchant, Integer level) {
        enchants.add(enchant);
        EnchantLevels.add(level);
    }

    public int getNumEnchant() {
        return enchants.size();
    }

    public Enchantment getEnchant(int index) {
        return enchants.get(index);
    }

    public Integer getLevel(int index) {
        return EnchantLevels.get(index);
    }

}
