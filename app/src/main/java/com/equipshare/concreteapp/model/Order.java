package com.equipshare.concreteapp.model;

/**
 * Created by Jarvis on 21-01-2018.
 */
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Order implements Parcelable {
    @SerializedName("orderId")
    @Expose
    private Integer orderId;
    @SerializedName("generationDate")
    @Expose
    private String generationDate;
    @SerializedName("requiredByDate")
    @Expose
    private String requiredByDate;
    @SerializedName("requestedBy")
    @Expose
    private String requestedBy;
    @SerializedName("requestedById")
    @Expose
    private Integer requestedById;
    @SerializedName("supplierId")
    @Expose
    private Integer supplierId;
    @SerializedName("companyName")
    @Expose
    private String companyName;
    @SerializedName("customerSite")
    @Expose
    private String customerSite;
    @SerializedName("POId")
    @Expose
    private Integer pOId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("statusDate")
    @Expose
    private String statusDate;
    @SerializedName("statusDesc")
    @Expose
    private String statusDesc;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getGenerationDate() {
        return generationDate;
    }

    public void setGenerationDate(String generationDate) {
        this.generationDate = generationDate;
    }

    public String getRequiredByDate() {
        return requiredByDate;
    }

    public void setRequiredByDate(String requiredByDate) {
        this.requiredByDate = requiredByDate;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }

    public Integer getRequestedById() {
        return requestedById;
    }

    public void setRequestedById(Integer requestedById) {
        this.requestedById = requestedById;
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCustomerSite() {
        return customerSite;
    }

    public void setCustomerSite(String customerSite) {
        this.customerSite = customerSite;
    }

    public Integer getPOId() {
        return pOId;
    }

    public void setPOId(Integer pOId) {
        this.pOId = pOId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(String statusDate) {
        this.statusDate = statusDate;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public Order() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.orderId);
        dest.writeString(this.generationDate);
        dest.writeString(this.requiredByDate);
        dest.writeString(this.requestedBy);
        dest.writeValue(this.requestedById);
        dest.writeValue(this.supplierId);
        dest.writeString(this.companyName);
        dest.writeString(this.customerSite);
        dest.writeValue(this.pOId);
        dest.writeString(this.status);
        dest.writeString(this.statusDate);
        dest.writeString(this.statusDesc);
        dest.writeList(this.data);
    }

    protected Order(Parcel in) {
        this.orderId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.generationDate = in.readString();
        this.requiredByDate = in.readString();
        this.requestedBy = in.readString();
        this.requestedById = (Integer) in.readValue(Integer.class.getClassLoader());
        this.supplierId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.companyName = in.readString();
        this.customerSite = in.readString();
        this.pOId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.status = in.readString();
        this.statusDate = in.readString();
        this.statusDesc = in.readString();
        this.data = new ArrayList<Datum>();
        in.readList(this.data, Datum.class.getClassLoader());
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel source) {
            return new Order(source);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };
}
