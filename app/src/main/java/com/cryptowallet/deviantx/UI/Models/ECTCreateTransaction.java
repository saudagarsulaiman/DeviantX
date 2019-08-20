package com.cryptowallet.deviantx.UI.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ECTCreateTransaction implements Parcelable {

    @SerializedName("amountExpectedTo")
    String str_amountExpectedTo;
    @SerializedName("changellyFee")
    String str_changellyFee;
    @SerializedName("id")
    String str_id;
    @SerializedName("amountExpectedFrom")
    String str_amountExpectedFrom;
    @SerializedName("currencyTo")
    String str_currencyTo;
    @SerializedName("payoutAddress")
    String str_payoutAddress;
    @SerializedName("currencyFrom")
    String str_currencyFrom;
    @SerializedName("payinAddress")
    String str_payinAddress;
    @SerializedName("status")
    String str_status;

    protected ECTCreateTransaction(Parcel in) {
        str_amountExpectedTo = in.readString();
        str_changellyFee = in.readString();
        str_id = in.readString();
        str_amountExpectedFrom = in.readString();
        str_currencyTo = in.readString();
        str_payoutAddress = in.readString();
        str_currencyFrom = in.readString();
        str_payinAddress = in.readString();
        str_status = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(str_amountExpectedTo);
        dest.writeString(str_changellyFee);
        dest.writeString(str_id);
        dest.writeString(str_amountExpectedFrom);
        dest.writeString(str_currencyTo);
        dest.writeString(str_payoutAddress);
        dest.writeString(str_currencyFrom);
        dest.writeString(str_payinAddress);
        dest.writeString(str_status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ECTCreateTransaction> CREATOR = new Creator<ECTCreateTransaction>() {
        @Override
        public ECTCreateTransaction createFromParcel(Parcel in) {
            return new ECTCreateTransaction(in);
        }

        @Override
        public ECTCreateTransaction[] newArray(int size) {
            return new ECTCreateTransaction[size];
        }
    };

    public String getStr_amountExpectedTo() {
        return str_amountExpectedTo;
    }

    public void setStr_amountExpectedTo(String str_amountExpectedTo) {
        this.str_amountExpectedTo = str_amountExpectedTo;
    }

    public String getStr_changellyFee() {
        return str_changellyFee;
    }

    public void setStr_changellyFee(String str_changellyFee) {
        this.str_changellyFee = str_changellyFee;
    }

    public String getStr_id() {
        return str_id;
    }

    public void setStr_id(String str_id) {
        this.str_id = str_id;
    }

    public String getStr_amountExpectedFrom() {
        return str_amountExpectedFrom;
    }

    public void setStr_amountExpectedFrom(String str_amountExpectedFrom) {
        this.str_amountExpectedFrom = str_amountExpectedFrom;
    }

    public String getStr_currencyTo() {
        return str_currencyTo;
    }

    public void setStr_currencyTo(String str_currencyTo) {
        this.str_currencyTo = str_currencyTo;
    }

    public String getStr_payoutAddress() {
        return str_payoutAddress;
    }

    public void setStr_payoutAddress(String str_payoutAddress) {
        this.str_payoutAddress = str_payoutAddress;
    }

    public String getStr_currencyFrom() {
        return str_currencyFrom;
    }

    public void setStr_currencyFrom(String str_currencyFrom) {
        this.str_currencyFrom = str_currencyFrom;
    }

    public String getStr_payinAddress() {
        return str_payinAddress;
    }

    public void setStr_payinAddress(String str_payinAddress) {
        this.str_payinAddress = str_payinAddress;
    }

    public String getStr_status() {
        return str_status;
    }

    public void setStr_status(String str_status) {
        this.str_status = str_status;
    }
}
