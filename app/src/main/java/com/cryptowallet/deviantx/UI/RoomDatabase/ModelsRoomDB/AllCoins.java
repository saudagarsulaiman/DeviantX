package com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

@Entity(tableName = "all_coins_table")
public class AllCoins  {

    @PrimaryKey
    @NonNull
    String str_coin_code;

    int int_coin_id, int_coin_rank;
    Double dbl_coin_usdValue, dbl_coin_marketCap, dbl_coin_volume, dbl_coin_24h, dbl_coin_7d, dbl_coin_1m;
    String str_coin_name, str_coin_logo, str_coin_chart_data;
    Boolean isSelected = false;

    public Boolean getFav() {
        return fav;
    }

    public void setFav(Boolean fav) {
        this.fav = fav;
    }


    Boolean fav = false;


    public int getInt_coin_id() {
        return int_coin_id;
    }

    public void setInt_coin_id(int int_coin_id) {
        this.int_coin_id = int_coin_id;
    }

    public int getInt_coin_rank() {
        return int_coin_rank;
    }

    public void setInt_coin_rank(int int_coin_rank) {
        this.int_coin_rank = int_coin_rank;
    }

    public Double getDbl_coin_usdValue() {
        return dbl_coin_usdValue;
    }

    public void setDbl_coin_usdValue(Double dbl_coin_usdValue) {
        this.dbl_coin_usdValue = dbl_coin_usdValue;
    }

    public Double getDbl_coin_marketCap() {
        return dbl_coin_marketCap;
    }

    public void setDbl_coin_marketCap(Double dbl_coin_marketCap) {
        this.dbl_coin_marketCap = dbl_coin_marketCap;
    }

    public Double getDbl_coin_volume() {
        return dbl_coin_volume;
    }

    public void setDbl_coin_volume(Double dbl_coin_volume) {
        this.dbl_coin_volume = dbl_coin_volume;
    }

    public Double getDbl_coin_24h() {
        return dbl_coin_24h;
    }

    public void setDbl_coin_24h(Double dbl_coin_24h) {
        this.dbl_coin_24h = dbl_coin_24h;
    }

    public Double getDbl_coin_7d() {
        return dbl_coin_7d;
    }

    public void setDbl_coin_7d(Double dbl_coin_7d) {
        this.dbl_coin_7d = dbl_coin_7d;
    }

    public Double getDbl_coin_1m() {
        return dbl_coin_1m;
    }

    public void setDbl_coin_1m(Double dbl_coin_1m) {
        this.dbl_coin_1m = dbl_coin_1m;
    }


    public AllCoins() {
    }

    public AllCoins(@NonNull int int_coin_id, @NonNull  String str_coin_name, @NonNull  String str_coin_code, @NonNull  String str_coin_logo, @NonNull  Double dbl_coin_usdValue, @NonNull  int int_coin_rank, @NonNull Double dbl_coin_marketCap, @NonNull Double dbl_coin_volume, @NonNull Double dbl_coin_24h, @NonNull Double dbl_coin_7d, @NonNull Double dbl_coin_1m, @NonNull boolean isFav, @NonNull String str_coin_chart_data) {
        this.int_coin_id = int_coin_id;
        this.int_coin_rank = int_coin_rank;
        this.dbl_coin_usdValue = dbl_coin_usdValue;
        this.dbl_coin_marketCap = dbl_coin_marketCap;
        this.dbl_coin_volume = dbl_coin_volume;
        this.dbl_coin_24h = dbl_coin_24h;
        this.dbl_coin_7d = dbl_coin_7d;
        this.dbl_coin_1m = dbl_coin_1m;
        this.str_coin_name = str_coin_name;
        this.str_coin_code = str_coin_code;
        this.str_coin_logo = str_coin_logo;
        this.fav = isFav;
        this.str_coin_chart_data = str_coin_chart_data;
    }

//    public AllCoins(int int_coin_id, String str_coin_name, String str_coin_code, String str_coin_logo, Double dbl_coin_usdValue, int int_coin_rank, Double dbl_coin_marketCap, Double dbl_coin_volume, Double dbl_coin_24h, Double dbl_coin_7d, Double dbl_coin_1m, boolean fav) {
//        this.int_coin_id = int_coin_id;
//        this.int_coin_rank = int_coin_rank;
//        this.dbl_coin_usdValue = dbl_coin_usdValue;
//        this.dbl_coin_marketCap = dbl_coin_marketCap;
//        this.dbl_coin_volume = dbl_coin_volume;
//        this.dbl_coin_24h = dbl_coin_24h;
//        this.dbl_coin_7d = dbl_coin_7d;
//        this.dbl_coin_1m = dbl_coin_1m;
//        this.str_coin_name = str_coin_name;
//        this.str_coin_code = str_coin_code;
//        this.str_coin_logo = str_coin_logo;
//        this.fav = fav;
//    }

/*
    public AllCoins(int int_coin_id, String str_coin_name, String str_coin_code, String str_coin_logo, Double dbl_coin_usdValue, int int_coin_rank, Double dbl_coin_marketCap, Double dbl_coin_volume, Double dbl_coin_24h, Double dbl_coin_7d, Double dbl_coin_1m, String str_coin_chart_data) {
        this.int_coin_id = int_coin_id;
        this.str_coin_name = str_coin_name;
        this.str_coin_code = str_coin_code;
        this.str_coin_logo = str_coin_logo;
        this.dbl_coin_usdValue = dbl_coin_usdValue;
        this.int_coin_rank = int_coin_rank;
        this.dbl_coin_marketCap = dbl_coin_marketCap;
        this.dbl_coin_volume = dbl_coin_volume;
        this.dbl_coin_24h = dbl_coin_24h;
        this.dbl_coin_7d = dbl_coin_7d;
        this.dbl_coin_1m = dbl_coin_1m;
        this.str_coin_chart_data = str_coin_chart_data;
    }
*/

   /* @Ignore
    public AllCoins(int int_coin_id, String str_coin_name, String str_coin_code, String str_coin_logo, Double dbl_coin_usdValue, int int_coin_rank, Double dbl_coin_marketCap, Double dbl_coin_volume, Double dbl_coin_24h, Double dbl_coin_7d, Double dbl_coin_1m) {
        this.int_coin_id = int_coin_id;
        this.str_coin_name = str_coin_name;
        this.str_coin_code = str_coin_code;
        this.str_coin_logo = str_coin_logo;
        this.dbl_coin_usdValue = dbl_coin_usdValue;
        this.int_coin_rank = int_coin_rank;
        this.dbl_coin_marketCap = dbl_coin_marketCap;
        this.dbl_coin_volume = dbl_coin_volume;
        this.dbl_coin_24h = dbl_coin_24h;
        this.dbl_coin_7d = dbl_coin_7d;
        this.dbl_coin_1m = dbl_coin_1m;
//        this.str_coin_chart_data = str_coin_chart_data;
    }*/

/*
    public AllCoins() {

    }
*/

    public String getStr_coin_chart_data() {
        return str_coin_chart_data;
    }

    public void setStr_coin_chart_data(String str_coin_chart_data) {
        this.str_coin_chart_data = str_coin_chart_data;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public int getStr_coin_id() {
        return int_coin_id;
    }

    public void setStr_coin_id(int int_coin_id) {
        this.int_coin_id = int_coin_id;
    }

    public String getStr_coin_name() {
        return str_coin_name;
    }

    public void setStr_coin_name(String str_coin_name) {
        this.str_coin_name = str_coin_name;
    }

    public String getStr_coin_code() {
        return str_coin_code;
    }

    public void setStr_coin_code(String str_coin_code) {
        this.str_coin_code = str_coin_code;
    }

    public String getStr_coin_logo() {
        return str_coin_logo;
    }

    public void setStr_coin_logo(String str_coin_logo) {
        this.str_coin_logo = str_coin_logo;
    }

    public Double getStr_coin_usdValue() {
        return dbl_coin_usdValue;
    }

    public void setStr_coin_usdValue(Double dbl_coin_usdValue) {
        this.dbl_coin_usdValue = dbl_coin_usdValue;
    }


}
