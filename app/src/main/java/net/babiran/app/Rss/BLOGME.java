package net.babiran.app.Rss;

public class BLOGME {
    String id, titr, image_link, category_id, created_at_int;

    public BLOGME(String id, String titr, String image_link, String category_id,String created_at_int) {
        this.id = id;
        this.titr = titr;
        this.image_link = image_link;
        this.category_id = category_id;
        this.created_at_int = created_at_int;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitr() {
        return titr;
    }

    public void setTitr(String titr) {
        this.titr = titr;
    }

    public String getImage_link() {
        return image_link;
    }

    public void setImage_link(String image_link) {
        this.image_link = image_link;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCreated_at_int() {
        return created_at_int;
    }

    public void setCreated_at_int(String created_at_int) {
        this.created_at_int = created_at_int;
    }
}
