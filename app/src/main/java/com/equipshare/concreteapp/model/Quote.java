package com.equipshare.concreteapp.model;

/**
 * Created by Jarvis on 21-01-2018.
 */
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Quote implements Parcelable {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("quality")
    @Expose
    private String quality;
    @SerializedName("customerSite")
    @Expose
    private String customerSite;
    @SerializedName("generationDate")
    @Expose
    private String generationDate;
    @SerializedName("requiredDate")
    @Expose
    private String requiredDate;
    @SerializedName("requestedBy")
    @Expose
    private String requestedBy;
    @SerializedName("requestedById")
    @Expose
    private String requestedById;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("requestedByCompany")
    @Expose
    private String requestedByCompany;
    @SerializedName("responses")
    @Expose
    private List<Response> responses = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRequestedByCompany() {
        return requestedByCompany;
    }

    public void setRequestedByCompany(String requestedByCompany) {
        this.requestedByCompany = requestedByCompany;
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

    public String getCustomerSite() {
        return customerSite;
    }

    public void setCustomerSite(String customerSite) {
        this.customerSite = customerSite;
    }

    public String getGenerationDate() {
        return generationDate;
    }

    public void setGenerationDate(String generationDate) {
        this.generationDate = generationDate;
    }

    public String getRequiredDate() {
        return requiredDate;
    }

    public void setRequiredDate(String requiredDate) {
        this.requiredDate = requiredDate;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }

    public String getRequestedById() {
        return requestedById;
    }

    public void setRequestedById(String requestedById) {
        this.requestedById = requestedById;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public List<Response> getResponses() {
        return responses;
    }

    public void setResponses(List<Response> responses) {
        this.responses = responses;
    }


    public Quote() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.quantity);
        dest.writeString(this.quality);
        dest.writeString(this.customerSite);
        dest.writeString(this.generationDate);
        dest.writeString(this.requiredDate);
        dest.writeString(this.requestedBy);
        dest.writeString(this.requestedById);
        dest.writeValue(this.v);
        dest.writeString(this.requestedByCompany);
        dest.writeTypedList(this.responses);
    }

    protected Quote(Parcel in) {
        this.id = in.readString();
        this.quantity = in.readString();
        this.quality = in.readString();
        this.customerSite = in.readString();
        this.generationDate = in.readString();
        this.requiredDate = in.readString();
        this.requestedBy = in.readString();
        this.requestedById = in.readString();
        this.v = (Integer) in.readValue(Integer.class.getClassLoader());
        this.requestedByCompany = in.readString();
        this.responses = in.createTypedArrayList(Response.CREATOR);
    }

    public static final Creator<Quote> CREATOR = new Creator<Quote>() {
        @Override
        public Quote createFromParcel(Parcel source) {
            return new Quote(source);
        }

        @Override
        public Quote[] newArray(int size) {
            return new Quote[size];
        }
    };
}
