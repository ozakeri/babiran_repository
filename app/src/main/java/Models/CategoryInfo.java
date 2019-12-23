package Models;

/**
 * Created by D on 5/11/2018.
 */

public class CategoryInfo
{
    String id,parent_id,work_time,service_area,minimum_order,image;
    public CategoryInfo(String root_id){
        this.id = root_id;
        this.parent_id="-1";

    }

    public CategoryInfo(String id, String parent_id, String work_time, String service_area, String minimum_order, String image) {
        this.id = id;
        this.parent_id = parent_id;
        this.work_time = work_time;
        this.service_area = service_area;
        this.minimum_order = minimum_order;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public String getParent_id() {
        return parent_id;
    }

    public String getWork_time() {
        return work_time;
    }

    public String getService_area() {
        return service_area;
    }

    public String getMinimum_order() {
        return minimum_order;
    }

    public String getImage() {
        return image;
    }
}
