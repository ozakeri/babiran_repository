package Models;

import java.util.Locale;

import static android.R.attr.description;

/**
 * Created by Tohid on 2/7/2017 AD.
 */

public class Category {

    public String id,name,parent_id,icon,slide_image;
    public Category(){

    }
    public Category(String root_id){
        this.id = root_id;
        this.parent_id="-1";
        this.name="ROOT";

    }
    public Category(String id,String name,String parent_id,String icon,String slide_image){
        this.id=id;
        this.name=name;
        this.parent_id=parent_id;
        this.icon = icon ;
        this.slide_image = slide_image ;
    }
}
