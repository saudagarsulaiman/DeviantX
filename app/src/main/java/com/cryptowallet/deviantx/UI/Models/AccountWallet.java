package com.cryptowallet.deviantx.UI.Models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.cryptowallet.trendchart.DateValue;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AccountWallet implements Parcelable {

    @SerializedName("walletName")
    String str_data_walletName;

    protected AccountWallet(Parcel in) {
        str_data_walletName = in.readString();
        int_data_id = in.readInt();
        str_data_address = in.readString();
        str_data_privatekey = in.readString();
        str_data_passcode = in.readString();
        str_data_account = in.readString();
        if (in.readByte() == 0) {
            str_data_balance = null;
        } else {
            str_data_balance = in.readDouble();
        }
        if (in.readByte() == 0) {
            str_data_balanceInUSD = null;
        } else {
            str_data_balanceInUSD = in.readDouble();
        }
        if (in.readByte() == 0) {
            str_data_balanceInINR = null;
        } else {
            str_data_balanceInINR = in.readDouble();
        }
        allCoins = in.readParcelable(AllCoins.class.getClassLoader());
        byte tmpIsFav = in.readByte();
        isFav = tmpIsFav == 0 ? null : tmpIsFav == 1;
        if (in.readByte() == 0) {
            highValue = null;
        } else {
            highValue = in.readDouble();
        }
    }

    public static final Creator<AccountWallet> CREATOR = new Creator<AccountWallet>() {
        @Override
        public AccountWallet createFromParcel(Parcel in) {
            return new AccountWallet(in);
        }

        @Override
        public AccountWallet[] newArray(int size) {
            return new AccountWallet[size];
        }
    };

    public String getStr_data_walletName() {
        return str_data_walletName;
    }

    public void setStr_data_walletName(String str_data_walletName) {
        this.str_data_walletName = str_data_walletName;
    }

    public int getInt_data_id() {
        return int_data_id;
    }

    public void setInt_data_id(int int_data_id) {
        this.int_data_id = int_data_id;
    }

    public String getStr_data_address() {
        return str_data_address;
    }

    public void setStr_data_address(String str_data_address) {
        this.str_data_address = str_data_address;
    }

    public String getStr_data_privatekey() {
        return str_data_privatekey;
    }

    public void setStr_data_privatekey(String str_data_privatekey) {
        this.str_data_privatekey = str_data_privatekey;
    }

    public String getStr_data_passcode() {
        return str_data_passcode;
    }

    public void setStr_data_passcode(String str_data_passcode) {
        this.str_data_passcode = str_data_passcode;
    }

    public String getStr_data_account() {
        return str_data_account;
    }

    public void setStr_data_account(String str_data_account) {
        this.str_data_account = str_data_account;
    }

    public Double getStr_data_balance() {
        return str_data_balance;
    }

    public void setStr_data_balance(Double str_data_balance) {
        this.str_data_balance = str_data_balance;
    }

    public Double getStr_data_balanceInUSD() {
        return str_data_balanceInUSD;
    }

    public void setStr_data_balanceInUSD(Double str_data_balanceInUSD) {
        this.str_data_balanceInUSD = str_data_balanceInUSD;
    }

    public Double getStr_data_balanceInINR() {
        return str_data_balanceInINR;
    }

    public void setStr_data_balanceInINR(Double str_data_balanceInINR) {
        this.str_data_balanceInINR = str_data_balanceInINR;
    }

    public AllCoins getAllCoins() {
        return allCoins;
    }

    public void setAllCoins(AllCoins allCoins) {
        this.allCoins = allCoins;
    }

    public Boolean getFav() {
        return isFav;
    }

    public void setFav(Boolean fav) {
        isFav = fav;
    }

    public Double getHighValue() {
        return highValue;
    }

    public void setHighValue(Double highValue) {
        this.highValue = highValue;
    }

    public ArrayList<DateValue> getResponseList() {
        return responseList;
    }

    public void setResponseList(ArrayList<DateValue> responseList) {
        this.responseList = responseList;
    }

    @SerializedName("id")
    int int_data_id;

    @SerializedName("address")
    String str_data_address;

    @SerializedName("privatekey")
    String str_data_privatekey;

    @SerializedName("passcode")
    String str_data_passcode;

    @SerializedName("account")
    String str_data_account;

    @SerializedName("balance")
    Double str_data_balance;

    @SerializedName("balanceInUSD")
    Double str_data_balanceInUSD;

    @SerializedName("balanceInINR")
    Double str_data_balanceInINR;

    @SerializedName("coin")
    AllCoins allCoins;

    @SerializedName("fav")
    Boolean isFav;

    Double highValue = 0.0;


    ArrayList<DateValue> responseList = new ArrayList<>();


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(str_data_walletName);
        dest.writeInt(int_data_id);
        dest.writeString(str_data_address);
        dest.writeString(str_data_privatekey);
        dest.writeString(str_data_passcode);
        dest.writeString(str_data_account);
        if (str_data_balance == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(str_data_balance);
        }
        if (str_data_balanceInUSD == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(str_data_balanceInUSD);
        }
        if (str_data_balanceInINR == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(str_data_balanceInINR);
        }
        dest.writeParcelable(allCoins, flags);
        dest.writeByte((byte) (isFav == null ? 0 : isFav ? 1 : 2));
        if (highValue == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(highValue);
        }
    }
}
