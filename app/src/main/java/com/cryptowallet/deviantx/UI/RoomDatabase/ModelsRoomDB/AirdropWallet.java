package com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

@Entity(tableName = "airdrop_wallet_coins_table")
public class AirdropWallet {

    @PrimaryKey
    @NonNull
    String str_ad_coin_code;

    int int_ad_noOfDays, int_ad_data_id, int_ad_coin_id, int_ad_coin_rank;
    String startDate, str_data_ad_address, str_data_ad_privatekey, str_data_ad_passcode, str_data_ad_account,
            str_data_ad_coin, str_ad_coin_name, str_ad_coin_logo, str_ad_coin_chart_data;
    Double dbl_data_ad_balance, dbl_data_ad_balanceInUSD, dbl_ad_coin_usdValue, dbl_ad_coin_marketCap, dbl_ad_coin_volume,
            dbl_ad_coin_1m, dbl_ad_coin_7d, dbl_ad_coin_24h;
    String allCoins;

    public int getInt_ad_noOfDays() {
        return int_ad_noOfDays;
    }

    public void setInt_ad_noOfDays(int int_ad_noOfDays) {
        this.int_ad_noOfDays = int_ad_noOfDays;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public int getInt_ad_data_id() {
        return int_ad_data_id;
    }

    public void setInt_ad_data_id(int int_ad_data_id) {
        this.int_ad_data_id = int_ad_data_id;
    }

    public int getInt_ad_coin_id() {
        return int_ad_coin_id;
    }

    public void setInt_ad_coin_id(int int_ad_coin_id) {
        this.int_ad_coin_id = int_ad_coin_id;
    }

    public int getInt_ad_coin_rank() {
        return int_ad_coin_rank;
    }

    public void setInt_ad_coin_rank(int int_ad_coin_rank) {
        this.int_ad_coin_rank = int_ad_coin_rank;
    }

    public String getStr_data_ad_address() {
        return str_data_ad_address;
    }

    public void setStr_data_ad_address(String str_data_ad_address) {
        this.str_data_ad_address = str_data_ad_address;
    }

    public String getStr_data_ad_privatekey() {
        return str_data_ad_privatekey;
    }

    public void setStr_data_ad_privatekey(String str_data_ad_privatekey) {
        this.str_data_ad_privatekey = str_data_ad_privatekey;
    }

    public String getStr_data_ad_passcode() {
        return str_data_ad_passcode;
    }

    public void setStr_data_ad_passcode(String str_data_ad_passcode) {
        this.str_data_ad_passcode = str_data_ad_passcode;
    }

    public String getStr_data_ad_account() {
        return str_data_ad_account;
    }

    public void setStr_data_ad_account(String str_data_ad_account) {
        this.str_data_ad_account = str_data_ad_account;
    }

    public String getStr_data_ad_coin() {
        return str_data_ad_coin;
    }

    public void setStr_data_ad_coin(String str_data_ad_coin) {
        this.str_data_ad_coin = str_data_ad_coin;
    }

    public String getStr_ad_coin_name() {
        return str_ad_coin_name;
    }

    public void setStr_ad_coin_name(String str_ad_coin_name) {
        this.str_ad_coin_name = str_ad_coin_name;
    }

    public String getStr_ad_coin_code() {
        return str_ad_coin_code;
    }

    public void setStr_ad_coin_code(String str_ad_coin_code) {
        this.str_ad_coin_code = str_ad_coin_code;
    }

    public String getStr_ad_coin_logo() {
        return str_ad_coin_logo;
    }

    public void setStr_ad_coin_logo(String str_ad_coin_logo) {
        this.str_ad_coin_logo = str_ad_coin_logo;
    }

    public String getStr_ad_coin_chart_data() {
        return str_ad_coin_chart_data;
    }

    public void setStr_ad_coin_chart_data(String str_ad_coin_chart_data) {
        this.str_ad_coin_chart_data = str_ad_coin_chart_data;
    }

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

    public Double getDbl_ad_coin_usdValue() {
        return dbl_ad_coin_usdValue;
    }

    public void setDbl_ad_coin_usdValue(Double dbl_ad_coin_usdValue) {
        this.dbl_ad_coin_usdValue = dbl_ad_coin_usdValue;
    }

    public Double getDbl_ad_coin_marketCap() {
        return dbl_ad_coin_marketCap;
    }

    public void setDbl_ad_coin_marketCap(Double dbl_ad_coin_marketCap) {
        this.dbl_ad_coin_marketCap = dbl_ad_coin_marketCap;
    }

    public Double getDbl_ad_coin_volume() {
        return dbl_ad_coin_volume;
    }

    public void setDbl_ad_coin_volume(Double dbl_ad_coin_volume) {
        this.dbl_ad_coin_volume = dbl_ad_coin_volume;
    }

    public Double getDbl_ad_coin_1m() {
        return dbl_ad_coin_1m;
    }

    public void setDbl_ad_coin_1m(Double dbl_ad_coin_1m) {
        this.dbl_ad_coin_1m = dbl_ad_coin_1m;
    }

    public Double getDbl_ad_coin_7d() {
        return dbl_ad_coin_7d;
    }

    public void setDbl_ad_coin_7d(Double dbl_ad_coin_7d) {
        this.dbl_ad_coin_7d = dbl_ad_coin_7d;
    }

    public Double getDbl_ad_coin_24h() {
        return dbl_ad_coin_24h;
    }

    public void setDbl_ad_coin_24h(Double dbl_ad_coin_24h) {
        this.dbl_ad_coin_24h = dbl_ad_coin_24h;
    }

    public String getAllCoins() {
        return allCoins;
    }

    public void setAllCoins(String allCoins) {
        this.allCoins = allCoins;
    }

    public AirdropWallet(@NonNull String startDate, @NonNull int int_ad_data_id, @NonNull String str_data_ad_address, @NonNull String str_data_ad_privatekey, @NonNull String str_data_ad_passcode, @NonNull Double dbl_data_ad_balance, @NonNull Double dbl_data_ad_balanceInUSD, @NonNull String str_data_ad_account, @NonNull int int_ad_noOfDays, @NonNull String allCoins) {
        this.startDate = startDate;
        this.int_ad_data_id = int_ad_data_id;
        this.str_data_ad_address = str_data_ad_address;
        this.str_data_ad_privatekey = str_data_ad_privatekey;
        this.str_data_ad_passcode = str_data_ad_passcode;
        this.str_data_ad_account = str_data_ad_account;
        this.dbl_data_ad_balance = dbl_data_ad_balance;
        this.dbl_data_ad_balanceInUSD = dbl_data_ad_balanceInUSD;
        this.allCoins = allCoins;
        this.int_ad_noOfDays = int_ad_noOfDays;

    }


}
