package net.babiran.app.Servic;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetComm {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("commentable_id")
    @Expose
    private Integer commentableId;
    @SerializedName("commentable_type")
    @Expose
    private String commentableType;
    @SerializedName("commented_id")
    @Expose
    private Integer commentedId;
    @SerializedName("commented_type")
    @Expose
    private String commentedType;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("approved")
    @Expose
    private Boolean approved;
    @SerializedName("rate")
    @Expose
    private Integer rate;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCommentableId() {
        return commentableId;
    }

    public void setCommentableId(Integer commentableId) {
        this.commentableId = commentableId;
    }

    public String getCommentableType() {
        return commentableType;
    }

    public void setCommentableType(String commentableType) {
        this.commentableType = commentableType;
    }

    public Integer getCommentedId() {
        return commentedId;
    }

    public void setCommentedId(Integer commentedId) {
        this.commentedId = commentedId;
    }

    public String getCommentedType() {
        return commentedType;
    }

    public void setCommentedType(String commentedType) {
        this.commentedType = commentedType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}