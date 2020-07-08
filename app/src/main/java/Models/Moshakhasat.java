package Models;

/**
 * Created by Mohammad on 5/27/2017.
 */

public class Moshakhasat {

    private String value, name;

    public Moshakhasat(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public Moshakhasat() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
