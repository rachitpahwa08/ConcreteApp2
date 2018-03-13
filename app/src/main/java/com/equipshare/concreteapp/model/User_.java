package com.equipshare.concreteapp.model;

/**
 * Created by Jarvis on 21-02-2018.
 */
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User_ implements Parcelable {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("user")
    @Expose
    private User user;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.success);
        dest.writeParcelable(this.user, flags);
    }

    public User_() {
    }

    protected User_(Parcel in) {
        this.success = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.user = in.readParcelable(User.class.getClassLoader());
    }

    public static final Parcelable.Creator<User_> CREATOR = new Parcelable.Creator<User_>() {
        @Override
        public User_ createFromParcel(Parcel source) {
            return new User_(source);
        }

        @Override
        public User_[] newArray(int size) {
            return new User_[size];
        }
    };
}
