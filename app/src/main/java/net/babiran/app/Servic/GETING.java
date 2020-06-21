package net.babiran.app.Servic;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GETING {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("parent_id")
    @Expose
    private Integer parentId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("is_deleted")
    @Expose
    private Integer isDeleted;
    @SerializedName("created_at_int")
    @Expose
    private Integer createdAtInt;
    @SerializedName("updated_at_int")
    @Expose
    private Integer updatedAtInt;
    @SerializedName("parent_name")
    @Expose
    private String parentName;

    @SerializedName("has_child")
    @Expose
    private String hasChild;

    @SerializedName("icon")
    @Expose
    private String icon;


    public String getHasChild() {
        return hasChild;
    }

    public void setHasChild(String hasChild) {
        this.hasChild = hasChild;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getCreatedAtInt() {
        return createdAtInt;
    }

    public void setCreatedAtInt(Integer createdAtInt) {
        this.createdAtInt = createdAtInt;
    }

    public Integer getUpdatedAtInt() {
        return updatedAtInt;
    }

    public void setUpdatedAtInt(Integer updatedAtInt) {
        this.updatedAtInt = updatedAtInt;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}


