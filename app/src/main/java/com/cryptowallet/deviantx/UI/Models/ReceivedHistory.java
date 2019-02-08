package com.cryptowallet.deviantx.UI.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ReceivedHistory implements Parcelable {

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

    protected ReceivedHistory(Parcel in) {
        str_coinCode = in.readString();
        str_coinLogo = in.readString();
        dbl_coinValue = in.readDouble();
        str_date = in.readString();
        str_fromAddress = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(str_coinCode);
        dest.writeString(str_coinLogo);
        dest.writeDouble(dbl_coinValue);
        dest.writeString(str_date);
        dest.writeString(str_fromAddress);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ReceivedHistory> CREATOR = new Creator<ReceivedHistory>() {
        @Override
        public ReceivedHistory createFromParcel(Parcel in) {
            return new ReceivedHistory(in);
        }

        @Override
        public ReceivedHistory[] newArray(int size) {
            return new ReceivedHistory[size];
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

}
