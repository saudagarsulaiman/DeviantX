package com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.cryptowallet.trendchart.DateValue;

import java.util.ArrayList;

@Entity(tableName = "account_wallet_table")
public class AccountWallet{

    @PrimaryKey
    @NonNull
    String str_data_walletName;
    int int_data_id;
    String str_data_address, str_data_privatekey, str_data_passcode, str_data_account;
    Double str_data_balance, str_data_balanceInUSD, str_data_balanceInINR;
    String allCoins;

    public int getInt_data_id() {
        return int_data_id;
    }

    public void setInt_data_id(int int_data_id) {
        this.int_data_id = int_data_id;
    }

    public Double getHighValue() {
        return highValue;
    }

    public void setHighValue(Double highValue) {
        this.highValue = highValue;
    }


    Double highValue = 0.0;


    public String getResponseList() {
        return responseList;
    }

    public void setResponseList(String responseList) {
        this.responseList = responseList;
    }

//    ArrayList<DateValue> responseList = new ArrayList<>();

    String responseList;

    public AccountWallet(@NonNull int int_data_id, @NonNull String str_data_address, @NonNull String str_data_walletName, @NonNull String str_data_privatekey, @NonNull String str_data_passcode, @NonNull Double str_data_balance, @NonNull Double str_data_balanceInUSD, @NonNull Double str_data_balanceInINR, @NonNull String str_data_account, @NonNull String allCoins) {
        this.int_data_id = int_data_id;
        this.str_data_address = str_data_address;
        this.str_data_walletName = str_data_walletName;
        this.str_data_privatekey = str_data_privatekey;
        this.str_data_passcode = str_data_passcode;
        this.str_data_balance = str_data_balance;
        this.str_data_balanceInUSD = str_data_balanceInUSD;
        this.str_data_balanceInINR = str_data_balanceInINR;
        this.str_data_account = str_data_account;
        this.allCoins = allCoins;
    }

/*
    public AccountWallet() {

    }
*/


    public int getStr_data_id() {
        return int_data_id;
    }

    public void setStr_data_id(int int_data_id) {
        this.int_data_id = int_data_id;
    }

    public String getStr_data_address() {
        return str_data_address;
    }

    public void setStr_data_address(String str_data_address) {
        this.str_data_address = str_data_address;
    }

    public String getStr_data_walletName() {
        return str_data_walletName;
    }

    public void setStr_data_walletName(String str_data_walletName) {
        this.str_data_walletName = str_data_walletName;
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

    public String getStr_data_account() {
        return str_data_account;
    }

    public void setStr_data_account(String str_data_account) {
        this.str_data_account = str_data_account;
    }

    public String getAllCoins() {
        return allCoins;
    }

    public void setAllCoins(String allCoins) {
        this.allCoins = allCoins;
    }



}
