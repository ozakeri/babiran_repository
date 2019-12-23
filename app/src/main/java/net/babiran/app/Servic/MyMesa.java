package net.babiran.app.Servic;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyMesa {

    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("factor_id")
    @Expose
    private Integer factorId;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("RefId")
    @Expose
    private String refId;

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public Integer getFactorId() {
        return factorId;
    }

    public void setFactorId(Integer factorId) {
        this.factorId = factorId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

}