package net.babiran.app;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Alireza on 1/26/2018.
 */

public class GetTokenResponse {

    @SerializedName("TokenKey")
    public String tokenKey;
    @SerializedName("IsSuccessful")
    public boolean isSuccessful;
    @SerializedName("Message")
    public String message;

}
