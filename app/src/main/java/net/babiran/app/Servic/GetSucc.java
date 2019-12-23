package net.babiran.app.Servic;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetSucc {

    @SerializedName("success")
    @Expose
    private Integer success;

    @SerializedName("like")
    @Expose
    private Integer like;

    public Integer getLike() {
        return like;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

}