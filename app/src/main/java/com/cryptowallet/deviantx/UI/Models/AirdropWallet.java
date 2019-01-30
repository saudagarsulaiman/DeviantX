package com.cryptowallet.deviantx.UI.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class AirdropWallet implements Parcelable {

    @SerializedName("balance")
    Double dbl_data_ad_balance;

    @SerializedName("balanceInUSD")
    Double dbl_data_ad_balanceInUSD;

    @SerializedName("airdropStatus")
    String str_airdropStatus;

    @SerializedName("airdropStartDate")
    String startDate;

    @SerializedName("noOfDays")
    int int_ad_noOfDays;

    @SerializedName("coinName")
    String str_ad_coin_name;

    @SerializedName("coinlogo")
    String str_ad_coin_logo;

    @SerializedName("coinCode")
    String str_ad_coin_code;

    protected AirdropWallet(Parcel in) {
        if (in.readByte() == 0) {
            dbl_data_ad_balance = null;
        } else {
            dbl_data_ad_balance = in.readDouble();
        }
        if (in.readByte() == 0) {
            dbl_data_ad_balanceInUSD = null;
        } else {
            dbl_data_ad_balanceInUSD = in.readDouble();
        }
        str_airdropStatus = in.readString();
        startDate = in.readString();
        int_ad_noOfDays = in.readInt();
        str_ad_coin_name = in.readString();
        str_ad_coin_logo = in.readString();
        str_ad_coin_code = in.readString();
    }

    public static final Creator<AirdropWallet> CREATOR = new Creator<AirdropWallet>() {
        @Override
        public AirdropWallet createFromParcel(Parcel in) {
            return new AirdropWallet(in);
        }

        @Override
        public AirdropWallet[] newArray(int size) {
            return new AirdropWallet[size];
        }
    };

    public Double getDbl_data_ad_balance() {
        return dbl_data_ad_balance;
    }

    public void setDbl_data_ad_balance(Double dbl_data_ad_balance) {
        this.dbl_data_ad_balance = dbl_data_ad_balance;
    }

    public Double getDbl_data_ad_balanceInUSD() {
        return dbl_data_ad_balanceInUSD;
    }

    public void setDbl_data_ad_balanceInUSD(Double dbl_data_ad_balanceInUSD) {
        this.dbl_data_ad_balanceInUSD = dbl_data_ad_balanceInUSD;
    }

    public String getStr_airdropStatus() {
        return str_airdropStatus;
    }

    public void setStr_airdropStatus(String str_airdropStatus) {
        this.str_airdropStatus = str_airdropStatus;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public int getInt_ad_noOfDays() {
        return int_ad_noOfDays;
    }

    public void setInt_ad_noOfDays(int int_ad_noOfDays) {
        this.int_ad_noOfDays = int_ad_noOfDays;
    }

    public String getStr_ad_coin_name() {
        return str_ad_coin_name;
    }

    public void setStr_ad_coin_name(String str_ad_coin_name) {
        this.str_ad_coin_name = str_ad_coin_name;
    }

    public String getStr_ad_coin_logo() {
        return str_ad_coin_logo;
    }

    public void setStr_ad_coin_logo(String str_ad_coin_logo) {
        this.str_ad_coin_logo = str_ad_coin_logo;
    }

    public String getStr_ad_coin_code() {
        return str_ad_coin_code;
    }

    public void setStr_ad_coin_code(String str_ad_coin_code) {
        this.str_ad_coin_code = str_ad_coin_code;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (dbl_data_ad_balance == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(dbl_data_ad_balance);
        }
        if (dbl_data_ad_balanceInUSD == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(dbl_data_ad_balanceInUSD);
        }
        dest.writeString(str_airdropStatus);
        dest.writeString(startDate);
        dest.writeInt(int_ad_noOfDays);
        dest.writeString(str_ad_coin_name);
        dest.writeString(str_ad_coin_logo);
        dest.writeString(str_ad_coin_code);
    }
}
