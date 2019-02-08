package com.cryptowallet.deviantx.UI.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class SentHistory implements Parcelable {

    @SerializedName("coinCode")
    String str_coinCode;

    @SerializedName("coinLogo")
    String str_coinLogo;

    @SerializedName("coinValue")
    double dbl_coinValue;

    @SerializedName("date")
    String str_date;

    @SerializedName("fromAddress")
    String str_fromAddress;

    @SerializedName("toAddress")
    String str_toAddress;

    @SerializedName("txHash")
    String str_txHash;

    protected SentHistory(Parcel in) {
        str_coinCode = in.readString();
        str_coinLogo = in.readString();
        dbl_coinValue = in.readDouble();
        str_date = in.readString();
        str_fromAddress = in.readString();
        str_toAddress = in.readString();
        str_txHash = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(str_coinCode);
        dest.writeString(str_coinLogo);
        dest.writeDouble(dbl_coinValue);
        dest.writeString(str_date);
        dest.writeString(str_fromAddress);
        dest.writeString(str_toAddress);
        dest.writeString(str_txHash);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SentHistory> CREATOR = new Creator<SentHistory>() {
        @Override
        public SentHistory createFromParcel(Parcel in) {
            return new SentHistory(in);
        }

        @Override
        public SentHistory[] newArray(int size) {
            return new SentHistory[size];
        }
    };

    public String getStr_coinCode() {
        return str_coinCode;
    }

    public void setStr_coinCode(String str_coinCode) {
        this.str_coinCode = str_coinCode;
    }

    public String getStr_coinLogo() {
        return str_coinLogo;
    }

    public void setStr_coinLogo(String str_coinLogo) {
        this.str_coinLogo = str_coinLogo;
    }

    public double getDbl_coinValue() {
        return dbl_coinValue;
    }

    public void setDbl_coinValue(double dbl_coinValue) {
        this.dbl_coinValue = dbl_coinValue;
    }

    public String getStr_date() {
        return str_date;
    }

    public void setStr_date(String str_date) {
        this.str_date = str_date;
    }

    public String getStr_fromAddress() {
        return str_fromAddress;
    }

    public void setStr_fromAddress(String str_fromAddress) {
        this.str_fromAddress = str_fromAddress;
    }

    public String getStr_toAddress() {
        return str_toAddress;
    }

    public void setStr_toAddress(String str_toAddress) {
        this.str_toAddress = str_toAddress;
    }

    public String getStr_txHash() {
        return str_txHash;
    }

    public void setStr_txHash(String str_txHash) {
        this.str_txHash = str_txHash;
    }
}
