package com.cryptowallet.deviantx.UI.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ECTCreatedTransactionData implements Parcelable {

    @SerializedName("amountExpectedFrom")
    String str_amountExpectedFrom;
    @SerializedName("amountExpectedTo")
    String str_amountExpectedTo;
    @SerializedName("currencyFrom")
    String str_currencyFrom;
    @SerializedName("currencyTo")
    String str_currencyTo;
    @SerializedName("date")
    long long_date;
    @SerializedName("email")
    String str_email;
    @SerializedName("id")
    String str_id;
    @SerializedName("payinAddress")
    String str_payinAddress;
    @SerializedName("payoutAddress")
    String str_payoutAddress;
    @SerializedName("status")
    String str_status;
    @SerializedName("txnFee")
    String str_txnFee;
    @SerializedName("txnId")
    String str_txnId;

    protected ECTCreatedTransactionData(Parcel in) {
        str_amountExpectedFrom = in.readString();
        str_amountExpectedTo = in.readString();
        str_currencyFrom = in.readString();
        str_currencyTo = in.readString();
        long_date = in.readLong();
        str_email = in.readString();
        str_id = in.readString();
        str_payinAddress = in.readString();
        str_payoutAddress = in.readString();
        str_status = in.readString();
        str_txnFee = in.readString();
        str_txnId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(str_amountExpectedFrom);
        dest.writeString(str_amountExpectedTo);
        dest.writeString(str_currencyFrom);
        dest.writeString(str_currencyTo);
        dest.writeLong(long_date);
        dest.writeString(str_email);
        dest.writeString(str_id);
        dest.writeString(str_payinAddress);
        dest.writeString(str_payoutAddress);
        dest.writeString(str_status);
        dest.writeString(str_txnFee);
        dest.writeString(str_txnId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ECTCreatedTransactionData> CREATOR = new Creator<ECTCreatedTransactionData>() {
        @Override
        public ECTCreatedTransactionData createFromParcel(Parcel in) {
            return new ECTCreatedTransactionData(in);
        }

        @Override
        public ECTCreatedTransactionData[] newArray(int size) {
            return new ECTCreatedTransactionData[size];
        }
    };

    public String getStr_amountExpectedFrom() {
        return str_amountExpectedFrom;
    }

    public void setStr_amountExpectedFrom(String str_amountExpectedFrom) {
        this.str_amountExpectedFrom = str_amountExpectedFrom;
    }

    public String getStr_amountExpectedTo() {
        return str_amountExpectedTo;
    }

    public void setStr_amountExpectedTo(String str_amountExpectedTo) {
        this.str_amountExpectedTo = str_amountExpectedTo;
    }

    public String getStr_currencyFrom() {
        return str_currencyFrom;
    }

    public void setStr_currencyFrom(String str_currencyFrom) {
        this.str_currencyFrom = str_currencyFrom;
    }

    public String getStr_currencyTo() {
        return str_currencyTo;
    }

    public void setStr_currencyTo(String str_currencyTo) {
        this.str_currencyTo = str_currencyTo;
    }

    public long getLong_date() {
        return long_date;
    }

    public void setLong_date(long long_date) {
        this.long_date = long_date;
    }

    public String getStr_email() {
        return str_email;
    }

    public void setStr_email(String str_email) {
        this.str_email = str_email;
    }

    public String getStr_id() {
        return str_id;
    }

    public void setStr_id(String str_id) {
        this.str_id = str_id;
    }

    public String getStr_payinAddress() {
        return str_payinAddress;
    }

    public void setStr_payinAddress(String str_payinAddress) {
        this.str_payinAddress = str_payinAddress;
    }

    public String getStr_payoutAddress() {
        return str_payoutAddress;
    }

    public void setStr_payoutAddress(String str_payoutAddress) {
        this.str_payoutAddress = str_payoutAddress;
    }

    public String getStr_status() {
        return str_status;
    }

    public void setStr_status(String str_status) {
        this.str_status = str_status;
    }

    public String getStr_txnFee() {
        return str_txnFee;
    }

    public void setStr_txnFee(String str_txnFee) {
        this.str_txnFee = str_txnFee;
    }

    public String getStr_txnId() {
        return str_txnId;
    }

    public void setStr_txnId(String str_txnId) {
        this.str_txnId = str_txnId;
    }
}
