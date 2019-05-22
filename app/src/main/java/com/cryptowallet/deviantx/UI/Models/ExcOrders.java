package com.cryptowallet.deviantx.UI.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ExcOrders implements Parcelable {

    @SerializedName("orderId")
    String str_orderId;
    @SerializedName("price")
    double dbl_price;
    @SerializedName("amount")
    double dbl_amount;
    @SerializedName("total")
    double dbl_total;
    @SerializedName("executedAmount")
    double dbl_executedAmount;
    @SerializedName("executedVolume")
    double dbl_executedVolume;
    @SerializedName("orderType")
    String str_orderType;
    @SerializedName("orderStatus")
    String str_orderStatus;
    @SerializedName("user")
    String str_user;
    @SerializedName("coinPair")
    String str_coinPair;
    @SerializedName("createdAt")
    String str_createdAt;
    @SerializedName("endedAt")
    String str_endedAt;


    protected ExcOrders(Parcel in) {
        str_orderId = in.readString();
        dbl_price = in.readDouble();
        dbl_amount = in.readDouble();
        dbl_total = in.readDouble();
        dbl_executedAmount = in.readDouble();
        dbl_executedVolume = in.readDouble();
        str_orderType = in.readString();
        str_orderStatus = in.readString();
        str_user = in.readString();
        str_coinPair = in.readString();
        str_createdAt = in.readString();
        str_endedAt = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(str_orderId);
        dest.writeDouble(dbl_price);
        dest.writeDouble(dbl_amount);
        dest.writeDouble(dbl_total);
        dest.writeDouble(dbl_executedAmount);
        dest.writeDouble(dbl_executedVolume);
        dest.writeString(str_orderType);
        dest.writeString(str_orderStatus);
        dest.writeString(str_user);
        dest.writeString(str_coinPair);
        dest.writeString(str_createdAt);
        dest.writeString(str_endedAt);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ExcOrders> CREATOR = new Creator<ExcOrders>() {
        @Override
        public ExcOrders createFromParcel(Parcel in) {
            return new ExcOrders(in);
        }

        @Override
        public ExcOrders[] newArray(int size) {
            return new ExcOrders[size];
        }
    };

    public String getStr_orderId() {
        return str_orderId;
    }

    public void setStr_orderId(String str_orderId) {
        this.str_orderId = str_orderId;
    }

    public double getDbl_price() {
        return dbl_price;
    }

    public void setDbl_price(double dbl_price) {
        this.dbl_price = dbl_price;
    }

    public double getDbl_amount() {
        return dbl_amount;
    }

    public void setDbl_amount(double dbl_amount) {
        this.dbl_amount = dbl_amount;
    }

    public double getDbl_total() {
        return dbl_total;
    }

    public void setDbl_total(double dbl_total) {
        this.dbl_total = dbl_total;
    }

    public double getDbl_executedAmount() {
        return dbl_executedAmount;
    }

    public void setDbl_executedAmount(double dbl_executedAmount) {
        this.dbl_executedAmount = dbl_executedAmount;
    }

    public double getDbl_executedVolume() {
        return dbl_executedVolume;
    }

    public void setDbl_executedVolume(double dbl_executedVolume) {
        this.dbl_executedVolume = dbl_executedVolume;
    }

    public String getStr_orderType() {
        return str_orderType;
    }

    public void setStr_orderType(String str_orderType) {
        this.str_orderType = str_orderType;
    }

    public String getStr_orderStatus() {
        return str_orderStatus;
    }

    public void setStr_orderStatus(String str_orderStatus) {
        this.str_orderStatus = str_orderStatus;
    }

    public String getStr_user() {
        return str_user;
    }

    public void setStr_user(String str_user) {
        this.str_user = str_user;
    }

    public String getStr_coinPair() {
        return str_coinPair;
    }

    public void setStr_coinPair(String str_coinPair) {
        this.str_coinPair = str_coinPair;
    }

    public String getStr_createdAt() {
        return str_createdAt;
    }

    public void setStr_createdAt(String str_createdAt) {
        this.str_createdAt = str_createdAt;
    }

    public String getStr_endedAt() {
        return str_endedAt;
    }

    public void setStr_endedAt(String str_endedAt) {
        this.str_endedAt = str_endedAt;
    }
}
