package com.equipshare.concreteapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Jarvis on 18-07-2018.
 */

public class Datum implements Parcelable {
    @SerializedName("orderId")
    @Expose
    private Integer orderId;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("quality")
    @Expose
    private String quality;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.orderId);
        dest.writeString(this.quantity);
        dest.writeString(this.quality);
    }

    public Datum() {
    }

    protected Datum(Parcel in) {
        this.orderId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.quantity = in.readString();
        this.quality = in.readString();
    }

    public static final Parcelable.Creator<Datum> CREATOR = new Parcelable.Creator<Datum>() {
        @Override
        public Datum createFromParcel(Parcel source) {
            return new Datum(source);
        }

        @Override
        public Datum[] newArray(int size) {
            return new Datum[size];
        }
    };
}
