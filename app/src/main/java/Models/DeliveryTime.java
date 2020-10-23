package Models;


import java.util.ArrayList;

/**
 * Created by Mohammad on 5/27/2017.
 */

public class DeliveryTime {
    public String date;
    public ArrayList<TimeList> timeLists;

    public DeliveryTime(String date, ArrayList<TimeList> timeLists) {
        this.date = date;
        this.timeLists = timeLists;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<TimeList> getTimeLists() {
        return timeLists;
    }

    public void setTimeLists(ArrayList<TimeList> timeLists) {
        this.timeLists = timeLists;
    }
}
