package com.equipshare.concreteapp.model;

/**
 * Created by Jarvis on 04-01-2018.
 */
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class User implements Parcelable {
    @SerializedName("userId")
    @Expose
    private Integer userId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("custType")
    @Expose
    private String custType;
    @SerializedName("contact")
    @Expose
    private Integer contact;
    @SerializedName("pan")
    @Expose
    private String pan;
    @SerializedName("company")
    @Expose
    private String company;
    @SerializedName("gstin")
    @Expose
    private String gstin;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("userType")
    @Expose
    private String userType;
    @SerializedName("resetPasswordExpire")
    @Expose
    private String resetPasswordExpire;
    @SerializedName("resetPasswordToken")
    @Expose
    private String resetPasswordToken;
    @SerializedName("verified")
    @Expose
    private String verified;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCustType() {
        return custType;
    }

    public void setCustType(String custType) {
        this.custType = custType;
    }

    public Integer getContact() {
        return contact;
    }

    public void setContact(Integer contact) {
        this.contact = contact;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getResetPasswordExpire() {
        return resetPasswordExpire;
    }

    public void setResetPasswordExpire(String resetPasswordExpire) {
        this.resetPasswordExpire = resetPasswordExpire;
    }

    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    public void setResetPasswordToken(String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.userId);
        dest.writeString(this.name);
        dest.writeString(this.email);
        dest.writeString(this.custType);
        dest.writeValue(this.contact);
        dest.writeString(this.pan);
        dest.writeString(this.company);
        dest.writeString(this.gstin);
        dest.writeString(this.password);
        dest.writeString(this.userType);
        dest.writeString(this.resetPasswordExpire);
        dest.writeString(this.resetPasswordToken);
        dest.writeString(this.verified);
    }

    public User() {
    }

    protected User(Parcel in) {
        this.userId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
        this.email = in.readString();
        this.custType = in.readString();
        this.contact = (Integer) in.readValue(Integer.class.getClassLoader());
        this.pan = in.readString();
        this.company = in.readString();
        this.gstin = in.readString();
        this.password = in.readString();
        this.userType = in.readString();
        this.resetPasswordExpire = in.readString();
        this.resetPasswordToken = in.readString();
        this.verified = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
