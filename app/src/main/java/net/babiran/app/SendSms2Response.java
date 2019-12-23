package net.babiran.app;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Alireza on 1/26/2018.
 */

public class SendSms2Response {

    @SerializedName("VerificationCodeId")
    public String verificationCodeId;
    @SerializedName("IsSuccessful")
    public boolean isSuccessful;
    @SerializedName("Message")
    public String message;

}
