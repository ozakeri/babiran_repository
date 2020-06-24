package Models;

public class EventbusModel {

    private String credit;
    private String color;
    private String color1;
    private boolean isBasket;

    public EventbusModel(String credit) {
        this.credit = credit;
    }

    public EventbusModel(String color1,String color) {
        this.color = color;
        this.color1 = color1;
    }

    public EventbusModel(boolean isBasket) {
        this.isBasket = isBasket;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public boolean isBasket() {
        return isBasket;
    }

    public void setBasket(boolean basket) {
        isBasket = basket;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
