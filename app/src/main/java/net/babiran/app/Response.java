package net.babiran.app;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Alireza on 1/24/2017.
 */

public class Response {

    @SerializedName("status")
    String status;
    @SerializedName("message")
    String message;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public boolean statusIsOk() {
        return status.equals("ok");
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
