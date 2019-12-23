package net.babiran.app.Servic;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by amin on 3/4/2017.
 */
public class MyModel
{

    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("user")
    @Expose
    private Fetching user;

    public Integer getSuccess() {
        return success;
    }

    public Fetching getUser() {
        return user;
    }
}
