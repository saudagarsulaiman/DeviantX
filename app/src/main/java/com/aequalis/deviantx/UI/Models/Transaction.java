package com.aequalis.deviantx.UI.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Transaction implements Parcelable{

    int int_data_id;
    String str_data_txnHash, str_data_toAddress, str_data_txnDate, str_data_cryptoWallet,str_data_icoTokenwallet, str_data_account;
    Double dbl_data_coinValue;
    AllCoins allCoins;


    public Transaction(int int_data_id, String str_data_txnHash, String str_data_toAddress, String str_data_txnDate, String str_data_cryptoWallet, String str_data_icoTokenwallet, String str_data_account, Double dbl_data_coinValue, AllCoins allCoins) {
        this.int_data_id = int_data_id;
        this.str_data_txnHash = str_data_txnHash;
        this.str_data_toAddress = str_data_toAddress;
        this.str_data_txnDate = str_data_txnDate;
        this.str_data_cryptoWallet = str_data_cryptoWallet;
        this.str_data_icoTokenwallet = str_data_icoTokenwallet;
        this.str_data_account = str_data_account;
        this.dbl_data_coinValue = dbl_data_coinValue;
        this.allCoins = allCoins;
    }


    public Transaction() {

    }


    protected Transaction(Parcel in) {
        int_data_id = in.readInt();
        str_data_txnHash = in.readString();
        str_data_toAddress = in.readString();
        str_data_txnDate = in.readString();
        str_data_cryptoWallet = in.readString();
        str_data_icoTokenwallet = in.readString();
        str_data_account = in.readString();
        if (in.readByte() == 0) {
            dbl_data_coinValue = null;
        } else {
            dbl_data_coinValue = in.readDouble();
        }
        allCoins = in.readParcelable(AllCoins.class.getClassLoader());
    }

    public static final Creator<Transaction> CREATOR = new Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel in) {
            return new Transaction(in);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };

    public int getInt_data_id() {
        return int_data_id;
    }

    public void setInt_data_id(int int_data_id) {
        this.int_data_id = int_data_id;
    }

    public String getStr_data_txnHash() {
        return str_data_txnHash;
    }

    public void setStr_data_txnHash(String str_data_txnHash) {
        this.str_data_txnHash = str_data_txnHash;
    }

    public String getStr_data_toAddress() {
        return str_data_toAddress;
    }

    public void setStr_data_toAddress(String str_data_toAddress) {
        this.str_data_toAddress = str_data_toAddress;
    }

    public String getStr_data_txnDate() {
        return str_data_txnDate;
    }

    public void setStr_data_txnDate(String str_data_txnDate) {
        this.str_data_txnDate = str_data_txnDate;
    }

    public String getStr_data_cryptoWallet() {
        return str_data_cryptoWallet;
    }

    public void setStr_data_cryptoWallet(String str_data_cryptoWallet) {
        this.str_data_cryptoWallet = str_data_cryptoWallet;
    }

    public String getStr_data_icoTokenwallet() {
        return str_data_icoTokenwallet;
    }

    public void setStr_data_icoTokenwallet(String str_data_icoTokenwallet) {
        this.str_data_icoTokenwallet = str_data_icoTokenwallet;
    }

    public String getStr_data_account() {
        return str_data_account;
    }

    public void setStr_data_account(String str_data_account) {
        this.str_data_account = str_data_account;
    }

    public Double getdbl_data_coinValue() {
        return dbl_data_coinValue;
    }

    public void setStr_data_coinValue(Double str_data_coinValue) {
        this.dbl_data_coinValue = str_data_coinValue;
    }

    public AllCoins getAllCoins() {
        return allCoins;
    }

    public void setAllCoins(AllCoins allCoins) {
        this.allCoins = allCoins;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(int_data_id);
        dest.writeString(str_data_txnHash);
        dest.writeString(str_data_toAddress);
        dest.writeString(str_data_txnDate);
        dest.writeString(str_data_cryptoWallet);
        dest.writeString(str_data_icoTokenwallet);
        dest.writeString(str_data_account);
        if (dbl_data_coinValue == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(dbl_data_coinValue);
        }
        dest.writeParcelable(allCoins, flags);
    }
}
