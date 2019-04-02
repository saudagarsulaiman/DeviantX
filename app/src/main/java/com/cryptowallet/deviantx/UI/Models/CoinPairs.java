package com.cryptowallet.deviantx.UI.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class CoinPairs implements Parcelable {

    @SerializedName("id")
    int int_id;

    @SerializedName("pairCoin")
    String str_pairCoin;

    @SerializedName("currentValue")
    double dbl_currentValue;

    @SerializedName("previousValue")
    double dbl_previousValue;

    @SerializedName("twentyFourChange")
    double dbl_twentyFourChange;

    @SerializedName("twentyFourChangePercentage")
    double dbl_twentyFourChangePercentage;

    @SerializedName("status")
    String str_status;

    @SerializedName("exCoin")
    String str_exchangeCoin;

    @SerializedName("volume")
    double dbl_volume;

    @SerializedName("changeUsd")
    double dbl_twentyFourChangeUsd;

    public CoinPairs() {
    }

    protected CoinPairs(Parcel in) {
        int_id = in.readInt();
        str_exchangeCoin = in.readString();
        str_pairCoin = in.readString();
        dbl_currentValue = in.readDouble();
        dbl_previousValue = in.readDouble();
        dbl_twentyFourChange = in.readDouble();
        dbl_twentyFourChangeUsd = in.readDouble();
        dbl_twentyFourChangePercentage = in.readDouble();
        str_status = in.readString();
        dbl_volume = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(int_id);
        dest.writeString(str_exchangeCoin);
        dest.writeString(str_pairCoin);
        dest.writeDouble(dbl_currentValue);
        dest.writeDouble(dbl_previousValue);
        dest.writeDouble(dbl_twentyFourChange);
        dest.writeDouble(dbl_twentyFourChangeUsd);
        dest.writeDouble(dbl_twentyFourChangePercentage);
        dest.writeString(str_status);
        dest.writeDouble(dbl_volume);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CoinPairs> CREATOR = new Creator<CoinPairs>() {
        @Override
        public CoinPairs createFromParcel(Parcel in) {
            return new CoinPairs(in);
        }

        @Override
        public CoinPairs[] newArray(int size) {
            return new CoinPairs[size];
        }
    };

    public int getInt_id() {
        return int_id;
    }

    public void setInt_id(int int_id) {
        this.int_id = int_id;
    }

    public String getStr_exchangeCoin() {
        return str_exchangeCoin;
    }

    public void setStr_exchangeCoin(String str_exchangeCoin) {
        this.str_exchangeCoin = str_exchangeCoin;
    }

    public String getStr_pairCoin() {
        return str_pairCoin;
    }

    public void setStr_pairCoin(String str_pairCoin) {
        this.str_pairCoin = str_pairCoin;
    }

    public double getDbl_currentValue() {
        return dbl_currentValue;
    }

    public void setDbl_currentValue(double dbl_currentValue) {
        this.dbl_currentValue = dbl_currentValue;
    }

    public double getDbl_previousValue() {
        return dbl_previousValue;
    }

    public void setDbl_previousValue(double dbl_previousValue) {
        this.dbl_previousValue = dbl_previousValue;
    }

    public double getDbl_twentyFourChange() {
        return dbl_twentyFourChange;
    }

    public void setDbl_twentyFourChange(double dbl_twentyFourChange) {
        this.dbl_twentyFourChange = dbl_twentyFourChange;
    }

    public double getDbl_twentyFourChangeUsd() {
        return dbl_twentyFourChangeUsd;
    }

    public void setDbl_twentyFourChangeUsd(double dbl_twentyFourChangeUsd) {
        this.dbl_twentyFourChangeUsd = dbl_twentyFourChangeUsd;
    }

    public double getDbl_twentyFourChangePercentage() {
        return dbl_twentyFourChangePercentage;
    }

    public void setDbl_twentyFourChangePercentage(double dbl_twentyFourChangePercentage) {
        this.dbl_twentyFourChangePercentage = dbl_twentyFourChangePercentage;
    }

    public String getStr_status() {
        return str_status;
    }

    public void setStr_status(String str_status) {
        this.str_status = str_status;
    }

    public double getDbl_volume() {
        return dbl_volume;
    }

    public void setDbl_volume(double dbl_volume) {
        this.dbl_volume = dbl_volume;
    }
}
