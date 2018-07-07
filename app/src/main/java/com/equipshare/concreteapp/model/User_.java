package com.equipshare.concreteapp.model;

/**
 * Created by Jarvis on 21-02-2018.
 */

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User_ implements Parcelable {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("customerSite")
    @Expose
    private List<CustomerSite> customerSite = null;

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

    public List<CustomerSite> getCustomerSite() {
        return customerSite;
    }

    public void setCustomerSite(List<CustomerSite> customerSite) {
        this.customerSite = customerSite;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.success);
        dest.writeParcelable(this.user, flags);
        dest.writeTypedList(this.customerSite);
    }

    public User_() {
    }

    protected User_(Parcel in) {
        this.success = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.user = in.readParcelable(User.class.getClassLoader());
        this.customerSite = in.createTypedArrayList(CustomerSite.CREATOR);
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