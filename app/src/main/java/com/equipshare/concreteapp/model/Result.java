package com.equipshare.concreteapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

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
     @SerializedName("cities")
     @Expose
     private List<City> cities = null;

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

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
        dest.writeList(this.cities);
    }

    protected Result(Parcel in) {
        this.success = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.results = in.readParcelable(Results.class.getClassLoader());
        this.token = in.readString();
        this.msg = in.readString();
        this.user = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.suppname = in.readString();
        this.cities = new ArrayList<City>();
        in.readList(this.cities, City.class.getClassLoader());
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






