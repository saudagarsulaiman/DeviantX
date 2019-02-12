package com.cryptowallet.deviantx.UI.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class FeaturedAirdrops implements Parcelable {

    @SerializedName("airdropAmount")
    Double dbl_airdropAmount;

    @SerializedName("airdropAmountInUSD")
    Double dbl_airdropAmountInUSD;

    @SerializedName("airdropCreatedDate")
    String str_airdropCreatedDate;

    @SerializedName("coinName")
    String str_coinName;

    @SerializedName("coinlogo")
    String str_coinlogo;

    @SerializedName("coinCode")
    String str_coinCode;

    @SerializedName("estimated")
    double dbl_estimated;


    protected FeaturedAirdrops(Parcel in) {
        if (in.readByte() == 0) {
            dbl_airdropAmount = null;
        } else {
            dbl_airdropAmount = in.readDouble();
        }
        if (in.readByte() == 0) {
            dbl_airdropAmountInUSD = null;
        } else {
            dbl_airdropAmountInUSD = in.readDouble();
        }
        str_airdropCreatedDate = in.readString();
        str_coinName = in.readString();
        str_coinlogo = in.readString();
        str_coinCode = in.readString();
        dbl_estimated = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (dbl_airdropAmount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(dbl_airdropAmount);
        }
        if (dbl_airdropAmountInUSD == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(dbl_airdropAmountInUSD);
        }
        dest.writeString(str_airdropCreatedDate);
        dest.writeString(str_coinName);
        dest.writeString(str_coinlogo);
        dest.writeString(str_coinCode);
        dest.writeDouble(dbl_estimated);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FeaturedAirdrops> CREATOR = new Creator<FeaturedAirdrops>() {
        @Override
        public FeaturedAirdrops createFromParcel(Parcel in) {
            return new FeaturedAirdrops(in);
        }

        @Override
        public FeaturedAirdrops[] newArray(int size) {
            return new FeaturedAirdrops[size];
        }
    };

    public Double getDbl_airdropAmount() {
        return dbl_airdropAmount;
    }

    public void setDbl_airdropAmount(Double dbl_airdropAmount) {
        this.dbl_airdropAmount = dbl_airdropAmount;
    }

    public Double getDbl_airdropAmountInUSD() {
        return dbl_airdropAmountInUSD;
    }

    public void setDbl_airdropAmountInUSD(Double dbl_airdropAmountInUSD) {
        this.dbl_airdropAmountInUSD = dbl_airdropAmountInUSD;
    }

    public String getStr_airdropCreatedDate() {
        return str_airdropCreatedDate;
    }

    public void setStr_airdropCreatedDate(String str_airdropCreatedDate) {
        this.str_airdropCreatedDate = str_airdropCreatedDate;
    }

    public String getStr_coinName() {
        return str_coinName;
    }

    public void setStr_coinName(String str_coinName) {
        this.str_coinName = str_coinName;
    }

    public String getStr_coinlogo() {
        return str_coinlogo;
    }

    public void setStr_coinlogo(String str_coinlogo) {
        this.str_coinlogo = str_coinlogo;
    }

    public String getStr_coinCode() {
        return str_coinCode;
    }

    public void setStr_coinCode(String str_coinCode) {
        this.str_coinCode = str_coinCode;
    }

    public Double getdbl_estimated() {
        return dbl_estimated;
    }

    public void setdbl_estimated(Double dbl_estimated) {
        this.dbl_estimated = dbl_estimated;
    }


}
