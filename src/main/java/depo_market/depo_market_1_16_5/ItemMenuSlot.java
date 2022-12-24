package depo_market.depo_market_1_16_5;

import org.bukkit.Material;


public class ItemMenuSlot extends ItemStackData{

    private final String NameEn;
    private final float InitialPrice;

    public ItemMenuSlot(Material material,  String  nameEN,String nameJP ,float price) {
        super(material, nameJP);
        this.NameEn = nameEN;
        this.InitialPrice = price;
    }
    public String getEnName(){
        return NameEn;
    }
    public float getInitialPrice() {
        return InitialPrice;
    }
}
