package com.equipshare.concreteapp.model;

/**
 * Created by Jarvis on 31-01-2018.
 */
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response implements Parcelable {
    @SerializedName("rmxId")
    @Expose
    private Integer rmxId;
    @SerializedName("validTill")
    @Expose
    private String validTill;
    @SerializedName("quoteId")
    @Expose
    private Integer quoteId;
    @SerializedName("userId")
    @Expose
    private Integer userId;
    @SerializedName("id")
    @Expose
    private Integer id;

    public Integer getRmxId() {
        return rmxId;
    }

    public void setRmxId(Integer rmxId) {
        this.rmxId = rmxId;
    }

    public String getValidTill() {
        return validTill;
    }

    public void setValidTill(String validTill) {
        this.validTill = validTill;
    }

    public Integer getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(Integer quoteId) {
        this.quoteId = quoteId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.rmxId);
        dest.writeString(this.validTill);
        dest.writeValue(this.quoteId);
        dest.writeValue(this.userId);
        dest.writeValue(this.id);
    }

    public Response() {
    }

    protected Response(Parcel in) {
        this.rmxId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.validTill = in.readString();
        this.quoteId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.userId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<Response> CREATOR = new Parcelable.Creator<Response>() {
        @Override
        public Response createFromParcel(Parcel source) {
            return new Response(source);
        }

        @Override
        public Response[] newArray(int size) {
            return new Response[size];
        }
    };
}
