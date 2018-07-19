

/**
 * Created by Jarvis on 21-02-2018.
 */
/*
package com.equipshare.concreteapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Results implements Parcelable {

    @SerializedName("issues")
    @Expose
    private List<Issue> issues = null;
    @SerializedName("orders")
    @Expose
    private List<Order> orders = null;
    @SerializedName("quotes")
    @Expose
    private List<Quote> quotes = null;

    public List<Issue> getIssues() {
        return issues;
    }

    public void setIssues(List<Issue> issues) {
        this.issues = issues;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
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
        dest.writeTypedList(this.issues);
        dest.writeTypedList(this.orders);
        dest.writeTypedList(this.quotes);
    }

    public Results() {
    }

    protected Results(Parcel in) {
        this.issues = in.createTypedArrayList(Issue.CREATOR);
        this.orders = in.createTypedArrayList(Order.CREATOR);
        this.quotes = in.createTypedArrayList(Quote.CREATOR);
    }

    public static final Parcelable.Creator<Results> CREATOR = new Parcelable.Creator<Results>() {
        @Override
        public Results createFromParcel(Parcel source) {
            return new Results(source);
        }

        @Override
        public Results[] newArray(int size) {
            return new Results[size];
        }
    };
}*/

package com.equipshare.concreteapp.model;

        import android.os.Parcel;
        import android.os.Parcelable;

        import java.util.List;
        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class Results implements Parcelable {

    @SerializedName("orders")
    @Expose
    private List<Order> orders = null;
    @SerializedName("issues")
    @Expose
    private List<Issue> issues = null;
    @SerializedName("quotes")
    @Expose
    private List<Quote> quotes = null;

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
        dest.writeTypedList(this.orders);
        dest.writeTypedList(this.issues);
        dest.writeTypedList(this.quotes);
    }

    public Results() {
    }

    protected Results(Parcel in) {
        this.orders = in.createTypedArrayList(Order.CREATOR);
        this.issues = in.createTypedArrayList(Issue.CREATOR);
        this.quotes = in.createTypedArrayList(Quote.CREATOR);
    }

    public static final Parcelable.Creator<Results> CREATOR = new Parcelable.Creator<Results>() {
        @Override
        public Results createFromParcel(Parcel source) {
            return new Results(source);
        }

        @Override
        public Results[] newArray(int size) {
            return new Results[size];
        }
    };
}
