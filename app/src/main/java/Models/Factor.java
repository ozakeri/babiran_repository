package Models;

import java.util.ArrayList;

/**
 * Created by Tohid on 2/9/2017 AD.
 */

public class Factor {

    public String id,dis_price,free_price,type,date,state,piyek,address;
    public ArrayList<Product> products ;
    public Factor(String piyk,String state,String id, String dis_price,String free_price,String type, String date,ArrayList<Product> products,String address){
        this.id = id;
        this.dis_price = dis_price;
        this.free_price = free_price ;
        this.type = type ;
        this.date = date;
        this.products = products ;
        this.state = state ;
        this.piyek=piyk;
        this.address = address;
    }
}
