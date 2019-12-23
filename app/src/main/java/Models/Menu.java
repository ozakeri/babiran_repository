package Models;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Mohammad on 5/31/2017.
 */

public class Menu {

   String name ;

    int image_id ;
    public Menu(String name , int id){
        this.name = name ;
        this.image_id =id ;
    }
    public String getName() {
        return name;
    }
    public int getImage() {
        return image_id;
    }

}
