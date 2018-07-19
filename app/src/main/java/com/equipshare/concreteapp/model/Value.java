package com.equipshare.concreteapp.model;

/**
 * Created by Jarvis on 18-07-2018.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Value implements Parcelable {

    @SerializedName("Quantity")
    @Expose
    private String quantity;
    @SerializedName("Quality")
    @Expose
    private String quality;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("POId")
    @Expose
    private Integer pOId;
    @SerializedName("remQuantity")
    @Expose
    private String remQuantity;

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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Integer getPOId() {
        return pOId;
    }

    public void setPOId(Integer pOId) {
        this.pOId = pOId;
    }

    public String getRemQuantity() {
        return remQuantity;
    }

    public void setRemQuantity(String remQuantity) {
        this.remQuantity = remQuantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.quantity);
        dest.writeString(this.quality);
        dest.writeString(this.price);
        dest.writeValue(this.pOId);
        dest.writeString(this.remQuantity);
    }

    public Value() {
    }

    protected Value(Parcel in) {
        this.quantity = in.readString();
        this.quality = in.readString();
        this.price = in.readString();
        this.pOId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.remQuantity = in.readString();
    }

    public static final Parcelable.Creator<Value> CREATOR = new Parcelable.Creator<Value>() {
        @Override
        public Value createFromParcel(Parcel source) {
            return new Value(source);
        }

        @Override
        public Value[] newArray(int size) {
            return new Value[size];
        }
    };
}