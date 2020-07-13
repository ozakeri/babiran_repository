package Models;

public class EventbusModel {

    private String credit;
    private String color;
    private String color1;
    private int position;
    private boolean isBasket;

    public EventbusModel(String credit) {
        this.credit = credit;
    }

    public EventbusModel(String color1,String color,int position) {
        this.color = color;
        this.color1 = color1;
        this.position = position;
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

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
