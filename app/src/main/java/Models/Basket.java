package Models;

/**
 * Created by Mohammad on 7/25/2017.
 */

public class Basket {
    String product_id , count,colorName,colorCode ;
    public Basket(String product_id,String count){
        this.product_id = product_id ;
        this.count = count ;
    }

    public Basket() {
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }
}
