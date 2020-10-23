package Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Mohammad on 5/27/2017.
 */

public class TimeList implements Parcelable {

    public String id;
    public String text;

    public TimeList(String id, String text) {
        this.id = id;
        this.text = text;
    }

    protected TimeList(Parcel in) {
        id = in.readString();
        text = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(text);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TimeList> CREATOR = new Creator<TimeList>() {
        @Override
        public TimeList createFromParcel(Parcel in) {
            return new TimeList(in);
        }

        @Override
        public TimeList[] newArray(int size) {
            return new TimeList[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
