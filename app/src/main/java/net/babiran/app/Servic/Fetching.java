package net.babiran.app.Servic;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by amin on 12/23/2016.
 */
public class Fetching
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private Object name;
    @SerializedName("phone1")
    @Expose
    private Object phone1;
    @SerializedName("phone2")
    @Expose
    private Object phone2;
    @SerializedName("address")
    @Expose
    private Object address;
    @SerializedName("address2")
    @Expose
    private Object address2;
    @SerializedName("address3")
    @Expose
    private Object address3;
    @SerializedName("email")
    @Expose
    private Object email;
    @SerializedName("balance")
    @Expose
    private Integer balance;
    @SerializedName("code")
    @Expose
    private Object code;
    @SerializedName("is_active")
    @Expose
    private Integer isActive;
    @SerializedName("is_deleted")
    @Expose
    private Integer isDeleted;
    @SerializedName("created_at_jalali")
    @Expose
    private String createdAtJalali;
    @SerializedName("updated_at_jalali")
    @Expose
    private String updatedAtJalali;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;



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

    public Integer getFactorId() {
        return factorId;
    }

    public String getUrl() {
        return url;
    }

    public String getRefId() {
        return refId;
    }

    public Integer getId() {
        return id;
    }

    public Object getName() {
        return name;
    }

    public Object getPhone1() {
        return phone1;
    }

    public Object getPhone2() {
        return phone2;
    }

    public Object getAddress() {
        return address;
    }

    public Object getAddress2() {
        return address2;
    }

    public Object getAddress3() {
        return address3;
    }

    public Object getEmail() {
        return email;
    }

    public Integer getBalance() {
        return balance;
    }

    public Object getCode() {
        return code;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public String getCreatedAtJalali() {
        return createdAtJalali;
    }

    public String getUpdatedAtJalali() {
        return updatedAtJalali;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}
