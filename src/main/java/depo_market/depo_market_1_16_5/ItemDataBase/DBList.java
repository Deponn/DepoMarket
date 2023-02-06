package depo_market.depo_market_1_16_5.ItemDataBase;

import javax.naming.Name;
import java.util.Objects;

public enum DBList {
    Kojosen("Kojosen"),
    Default("Default");

    private final String Name;
    DBList(String name) {
        this.Name = name;
    }

     public String getName(){
        return this.Name;
     }

     public static DBInterface getItemDataBase(String name){
        if(Objects.equals(name, DBList.Default.Name)){
            return new DBDefault();
        }else if(Objects.equals(name, DBList.Kojosen.Name)){
            return new DBKojosen();
        }else {
            return new DBEmpty();
        }
     }
}
