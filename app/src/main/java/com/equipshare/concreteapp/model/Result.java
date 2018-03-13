package com.equipshare.concreteapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result implements Parcelable {

    @SerializedName("success")
    @Expose
    private Boolean success=false;
    @SerializedName("results")
    @Expose
    private Results results;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("msg")
    @Expose
     private String msg;
    @SerializedName("user")
    @Expose
    private Boolean user;
    @SerializedName("supplierName")
    @Expose
    private String suppname;

    public String getSuppname() {
        return suppname;
    }

    public void setSuppname(String suppname) {
        this.suppname = suppname;
    }

    public Boolean getUser() {
        return user;
    }

    public void setUser(Boolean user) {
        this.user = user;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Results getResults() {
        return results;
    }

    public void setResults(Results results) {
        this.results = results;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public Result() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.success);
        dest.writeParcelable(this.results, flags);
        dest.writeString(this.token);
        dest.writeString(this.msg);
        dest.writeValue(this.user);
        dest.writeString(this.suppname);
    }

    protected Result(Parcel in) {
        this.success = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.results = in.readParcelable(Results.class.getClassLoader());
        this.token = in.readString();
        this.msg = in.readString();
        this.user = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.suppname = in.readString();
    }

    public static final Creator<Result> CREATOR = new Creator<Result>() {
        @Override
        public Result createFromParcel(Parcel source) {
            return new Result(source);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };
}






//package com.equipshare.concreteapp.model;

/**
 * Created by Jarvis on 13-01-2018.
 */
/*import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result implements Parcelable {
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("POs")
    @Expose
    private List<PO> pOs = null;
    @SerializedName("orders")
    @Expose
    private List<Order> orders = null;
    @SerializedName("issues")
    @Expose
    private List<Issue> issues = null;
    @SerializedName("quotes")
    @Expose
    private List<Quote> quotes = null;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("msg")
    @Expose
    private String msg;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public List<PO> getPOs() {
        return pOs;
    }
    public void setPOs(List<PO> pOs) {
        this.pOs = pOs;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<Issue> getIssues() {
        return issues;
    }

    public void setIssues(List<Issue> issues) {
        this.issues = issues;
    }

    public List<Quote> getQuotes() {
        return quotes;
    }

    public void setQuotes(List<Quote> quotes) {
        this.quotes = quotes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.user, flags);
        dest.writeList(this.orders);
        dest.writeList(this.issues);
        dest.writeTypedList(this.quotes);
    }

    public Result() {
    }

    protected Result(Parcel in) {
        this.user = in.readParcelable(User.class.getClassLoader());
        this.orders = new ArrayList<Order>();
        in.readList(this.orders, Order.class.getClassLoader());
        this.issues = new ArrayList<Issue>();
        in.readList(this.issues, Issue.class.getClassLoader());
        this.quotes = in.createTypedArrayList(Quote.CREATOR);
    }

    public static final Parcelable.Creator<Result> CREATOR = new Parcelable.Creator<Result>() {
        @Override
        public Result createFromParcel(Parcel source) {
            return new Result(source);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };

}
*/