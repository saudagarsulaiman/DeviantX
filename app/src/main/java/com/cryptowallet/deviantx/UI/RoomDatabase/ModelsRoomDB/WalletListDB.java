package com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.cryptowallet.trendchart.DateValue;

import java.util.ArrayList;

@Entity(tableName = "wallet_list_table")
public class WalletListDB {
    int int_data_id;
    double dbl_data_totalBal;

    @PrimaryKey
    @NonNull
    String str_data_name;


    public boolean isDefaultWallet() {
        return defaultWallet;
    }

    public void setDefaultWallet(boolean defaultWallet) {
        this.defaultWallet = defaultWallet;
    }

    boolean defaultWallet = false;

    public WalletListDB(@NonNull int int_data_id, @NonNull String str_data_name, @NonNull double dbl_data_totalBal, @NonNull boolean defaultWallet) {
        this.int_data_id = int_data_id;
        this.dbl_data_totalBal = dbl_data_totalBal;
        this.str_data_name = str_data_name;
        this.defaultWallet = defaultWallet;
    }
/*

    public WalletListDB(int int_data_id, String str_data_name, double dbl_data_totalBal) {
        this.int_data_id = int_data_id;
        this.dbl_data_totalBal = dbl_data_totalBal;
        this.str_data_name = str_data_name;
    }

*/

    public int getInt_data_id() {
        return int_data_id;
    }

    public void setInt_data_id(int int_data_id) {
        this.int_data_id = int_data_id;
    }

    public double getDbl_data_totalBal() {
        return dbl_data_totalBal;
    }

    public void setDbl_data_totalBal(double dbl_data_totalBal) {
        this.dbl_data_totalBal = dbl_data_totalBal;
    }

    public String getStr_data_name() {
        return str_data_name;
    }

    public void setStr_data_name(String str_data_name) {
        this.str_data_name = str_data_name;
    }


//    ArrayList<DateValue> responseList = new ArrayList<>();
String responseList;

    Double highValue = 0.0;

    public void setResponseList(String responseList) {
        this.responseList = responseList;
    }

    public String getResponseList() {
        return responseList;
    }

    public void setHighValue(Double highValue) {
        this.highValue = highValue;
    }

    public Double getHighValue() {
        return highValue;
    }

}
