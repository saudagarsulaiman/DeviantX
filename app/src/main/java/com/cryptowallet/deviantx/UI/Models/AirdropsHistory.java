package com.cryptowallet.deviantx.UI.Models;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

@SuppressLint("ParcelCreator")
public class AirdropsHistory implements Parcelable {

    @SerializedName("airdropAmountInUSD")
    Double dbl_airdropAmountInUSD;

    @SerializedName("amount")
    Double dbl_amount;

    @SerializedName("category")
    String str_category;

    @SerializedName("coinCode")
    String str_coinCode;

    @SerializedName("coinName")
    String str_coinName;

    @SerializedName("coinlogo")
    String str_coinlogo;

    @SerializedName("refNo")
    String str_refNo;

    @SerializedName("to")
    String str_to;

    @SerializedName("txnDate")
    String str_txnDate;

    protected AirdropsHistory(Parcel in) {
        if (in.readByte() == 0) {
            dbl_airdropAmountInUSD = null;
        } else {
            dbl_airdropAmountInUSD = in.readDouble();
        }
        if (in.readByte() == 0) {
            dbl_amount = null;
        } else {
            dbl_amount = in.readDouble();
        }
        str_category = in.readString();
        str_coinCode = in.readString();
        str_coinName = in.readString();
        str_coinlogo = in.readString();
        str_refNo = in.readString();
        str_to = in.readString();
        str_txnDate = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (dbl_airdropAmountInUSD == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(dbl_airdropAmountInUSD);
        }
        if (dbl_amount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(dbl_amount);
        }
        dest.writeString(str_category);
        dest.writeString(str_coinCode);
        dest.writeString(str_coinName);
        dest.writeString(str_coinlogo);
        dest.writeString(str_refNo);
        dest.writeString(str_to);
        dest.writeString(str_txnDate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AirdropsHistory> CREATOR = new Creator<AirdropsHistory>() {
        @Override
        public AirdropsHistory createFromParcel(Parcel in) {
            return new AirdropsHistory(in);
        }

        @Override
        public AirdropsHistory[] newArray(int size) {
            return new AirdropsHistory[size];
        }
    };

    public Double getDbl_airdropAmountInUSD() {
        return dbl_airdropAmountInUSD;
    }

    public void setDbl_airdropAmountInUSD(Double dbl_airdropAmountInUSD) {
        this.dbl_airdropAmountInUSD = dbl_airdropAmountInUSD;
    }

    public Double getDbl_amount() {
        return dbl_amount;
    }

    public void setDbl_amount(Double dbl_amount) {
        this.dbl_amount = dbl_amount;
    }

    public String getStr_category() {
        return str_category;
    }

    public void setStr_category(String str_category) {
        this.str_category = str_category;
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

    public String getStr_refNo() {
        return str_refNo;
    }

    public void setStr_refNo(String str_refNo) {
        this.str_refNo = str_refNo;
    }

    public String getStr_to() {
        return str_to;
    }

    public void setStr_to(String str_to) {
        this.str_to = str_to;
    }

    public String getStr_txnDate() {
        return str_txnDate;
    }

    public void setStr_txnDate(String str_txnDate) {
        this.str_txnDate = str_txnDate;
    }
}
