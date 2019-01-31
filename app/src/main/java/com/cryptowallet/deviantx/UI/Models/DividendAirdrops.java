package com.cryptowallet.deviantx.UI.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class DividendAirdrops implements Parcelable {

    @SerializedName("airdropAmount")
    Double dbl_airdropAmount;

    @SerializedName("airdropAmountInUSD")
    Double dbl_airdropAmountInUSD;

    @SerializedName("coinCode")
    String str_coinCode;

    @SerializedName("coinName")
    String str_coinName;

    @SerializedName("coinlogo")
    String str_coinlogo;

    @SerializedName("id")
    int int_id;

    protected DividendAirdrops(Parcel in) {
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
        str_coinCode = in.readString();
        str_coinName = in.readString();
        str_coinlogo = in.readString();
        int_id = in.readInt();
    }

    public static final Creator<DividendAirdrops> CREATOR = new Creator<DividendAirdrops>() {
        @Override
        public DividendAirdrops createFromParcel(Parcel in) {
            return new DividendAirdrops(in);
        }

        @Override
        public DividendAirdrops[] newArray(int size) {
            return new DividendAirdrops[size];
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

    public String getStr_coinCode() {
        return str_coinCode;
    }

    public void setStr_coinCode(String str_coinCode) {
        this.str_coinCode = str_coinCode;
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

    public int getInt_id() {
        return int_id;
    }

    public void setInt_id(int int_id) {
        this.int_id = int_id;
    }

    @Override
    public int describeContents() {
        return 0;
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
        dest.writeString(str_coinCode);
        dest.writeString(str_coinName);
        dest.writeString(str_coinlogo);
        dest.writeInt(int_id);
    }
}
