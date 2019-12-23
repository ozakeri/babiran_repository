package net.babiran.app.Servic;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GETINGBlog {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("body")
    @Expose
    private String body;
    @SerializedName("image_link")
    @Expose
    private String imageLink;
    @SerializedName("category_id")
    @Expose
    private Integer categoryId;
    @SerializedName("created_at_int")
    @Expose
    private String createdAtInt;
    @SerializedName("updated_at_int")
    @Expose
    private String updatedAtInt;
    @SerializedName("like")
    @Expose
    private int like;

    @SerializedName("titr")
    @Expose
    private String titr;


    public String getTitr() {
        return titr;
    }

    public void setTitr(String titr) {
        this.titr = titr;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    @SerializedName("success")
    @Expose
    private int success;

    public int getSuccess() {
        return success;
    }

    public int getLike() {
        return like;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCreatedAtInt() {
        return createdAtInt;
    }

    public void setCreatedAtInt(String createdAtInt) {
        this.createdAtInt = createdAtInt;
    }

    public String getUpdatedAtInt() {
        return updatedAtInt;
    }

    public void setUpdatedAtInt(String updatedAtInt) {
        this.updatedAtInt = updatedAtInt;
    }

}