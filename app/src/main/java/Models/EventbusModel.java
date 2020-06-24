package Models;

public class EventbusModel {

    private String credit;
    private boolean isBasket;
    private String color;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }


    public EventbusModel(boolean isBasket) {
        this.isBasket = isBasket;
    }

    public String getCredit() {
        return credit;
    }

    public boolean isBasket() {
        return isBasket;
    }

}
