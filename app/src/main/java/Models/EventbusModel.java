package Models;

public class EventbusModel {

    private String credit;
    private boolean isBasket;

    public EventbusModel(String credit) {
        this.credit = credit;
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
}
