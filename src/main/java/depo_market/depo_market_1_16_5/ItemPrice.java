package depo_market.depo_market_1_16_5;

/**
 * アイテムの値段。取引量を保持するオブジェクト。
 */
public class ItemPrice {
    private float Price;
    private int AmountOfSold;
    private int AmountOfBought;


    public ItemPrice(float price){
        this.Price = price;
        this.AmountOfSold = 1;
        this.AmountOfBought = 1;
    }
    public ItemPrice(float price,int amountOfBought,int amountOfSold){
        this.Price = price;
        this.AmountOfSold = amountOfSold;
        this.AmountOfBought = amountOfBought;
    }
    public void addAmountOfSold(int Amount){
        AmountOfSold += Amount;
    }
    public void addAmountOfBought(int Amount){
        AmountOfBought += Amount;
    }
    public void SetPrice(float price){
        Price = price;
    }
    public float getPrice(){
        return Price;
    }
    public int getAmountOfSold(){
        return AmountOfSold;
    }
    public int getAmountOfBought(){
        return AmountOfBought;
    }
}
