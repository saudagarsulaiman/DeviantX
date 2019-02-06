package com.cryptowallet.deviantx.UI.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.cryptowallet.trendchart.DateValue;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class WalletDetails implements Parcelable {

    @SerializedName("walletName")
    String str_data_walletName;

    @SerializedName("values")
    List<AccountWallet> str_data_values;

    protected WalletDetails(Parcel in) {
        str_data_walletName = in.readString();
        str_data_values = in.createTypedArrayList(AccountWallet.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(str_data_walletName);
        dest.writeTypedList(str_data_values);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WalletDetails> CREATOR = new Creator<WalletDetails>() {
        @Override
        public WalletDetails createFromParcel(Parcel in) {
            return new WalletDetails(in);
        }

        @Override
        public WalletDetails[] newArray(int size) {
            return new WalletDetails[size];
        }
    };

    public String getStr_data_walletName() {
        return str_data_walletName;
    }

    public void setStr_data_walletName(String str_data_walletName) {
        this.str_data_walletName = str_data_walletName;
    }

    public List<AccountWallet> getStr_data_values() {
        return str_data_values;
    }

    public void setStr_data_values(List<AccountWallet> str_data_values) {
        this.str_data_values = str_data_values;
    }
}
