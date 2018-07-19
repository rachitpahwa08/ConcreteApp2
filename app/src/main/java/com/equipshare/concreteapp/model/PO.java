package com.equipshare.concreteapp.model;

/**
 * Created by Jarvis on 23-01-2018.
 */
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PO implements Parcelable {
    @SerializedName("generationDate")
    @Expose
    private String generationDate;
    @SerializedName("validTill")
    @Expose
    private String validTill;
    @SerializedName("customerSite")
    @Expose
    private String customerSite;
    @SerializedName("requestedBy")
    @Expose
    private String requestedBy;
    @SerializedName("requestedById")
    @Expose
    private Integer requestedById;
    @SerializedName("supplierId")
    @Expose
    private Integer supplierId;
    @SerializedName("requestedByCompany")
    @Expose
    private String requestedByCompany;
    @SerializedName("confirmedBySupplier")
    @Expose
    private String confirmedBySupplier;
    @SerializedName("POId")
    @Expose
    private Integer pOId;
    @SerializedName("deletedByContractor")
    @Expose
    private String deletedByContractor;
    @SerializedName("values")
    @Expose
    private List<Value> values = null;

    public String getGenerationDate() {
        return generationDate;
    }

    public void setGenerationDate(String generationDate) {
        this.generationDate = generationDate;
    }

    public String getValidTill() {
        return validTill;
    }

    public void setValidTill(String validTill) {
        this.validTill = validTill;
    }

    public String getCustomerSite() {
        return customerSite;
    }

    public void setCustomerSite(String customerSite) {
        this.customerSite = customerSite;
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

    public String getRequestedByCompany() {
        return requestedByCompany;
    }

    public void setRequestedByCompany(String requestedByCompany) {
        this.requestedByCompany = requestedByCompany;
    }

    public String getConfirmedBySupplier() {
        return confirmedBySupplier;
    }

    public void setConfirmedBySupplier(String confirmedBySupplier) {
        this.confirmedBySupplier = confirmedBySupplier;
    }

    public Integer getPOId() {
        return pOId;
    }

    public void setPOId(Integer pOId) {
        this.pOId = pOId;
    }

    public String getDeletedByContractor() {
        return deletedByContractor;
    }

    public void setDeletedByContractor(String deletedByContractor) {
        this.deletedByContractor = deletedByContractor;
    }

    public List<Value> getValues() {
        return values;
    }

    public void setValues(List<Value> values) {
        this.values = values;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.generationDate);
        dest.writeString(this.validTill);
        dest.writeString(this.customerSite);
        dest.writeString(this.requestedBy);
        dest.writeValue(this.requestedById);
        dest.writeValue(this.supplierId);
        dest.writeString(this.requestedByCompany);
        dest.writeString(this.confirmedBySupplier);
        dest.writeValue(this.pOId);
        dest.writeString(this.deletedByContractor);
        dest.writeList(this.values);
    }

    public PO() {
    }

    protected PO(Parcel in) {
        this.generationDate = in.readString();
        this.validTill = in.readString();
        this.customerSite = in.readString();
        this.requestedBy = in.readString();
        this.requestedById = (Integer) in.readValue(Integer.class.getClassLoader());
        this.supplierId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.requestedByCompany = in.readString();
        this.confirmedBySupplier = in.readString();
        this.pOId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.deletedByContractor = in.readString();
        this.values = new ArrayList<Value>();
        in.readList(this.values, Value.class.getClassLoader());
    }

    public static final Parcelable.Creator<PO> CREATOR = new Parcelable.Creator<PO>() {
        @Override
        public PO createFromParcel(Parcel source) {
            return new PO(source);
        }

        @Override
        public PO[] newArray(int size) {
            return new PO[size];
        }
    };
}
