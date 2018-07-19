package com.equipshare.concreteapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class POget implements Parcelable {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("data")
    @Expose
    private List<PO> data = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<PO> getData() {
        return data;
    }

    public void setData(List<PO> data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.success);
        dest.writeTypedList(this.data);
    }

    public POget() {
    }

    protected POget(Parcel in) {
        this.success = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.data = in.createTypedArrayList(PO.CREATOR);
    }

    public static final Parcelable.Creator<POget> CREATOR = new Parcelable.Creator<POget>() {
        @Override
        public POget createFromParcel(Parcel source) {
            return new POget(source);
        }

        @Override
        public POget[] newArray(int size) {
            return new POget[size];
        }
    };
}