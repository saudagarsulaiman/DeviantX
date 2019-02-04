package com.cryptowallet.deviantx.UI.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class HeaderBanner  implements Parcelable {

    @SerializedName("image")
    String str_image;
    @SerializedName("id")
    int int_coin_id;
    @SerializedName("logo")
    String str_coin_logo;
    @SerializedName("usdValue")
    double dbl_coin_usdValue;
    @SerializedName("rank")
    int int_coin_rank;
    @SerializedName("marketCap")
    double dbl_coin_marketCap;
    @SerializedName("volume")
    double dbl_coin_volume;
    @SerializedName("changeOneDay")
    double dbl_coin_changeOneDay;
    @SerializedName("changeSevenDays")
    double dbl_coin_changeSevenDays;
    @SerializedName("changeOneMonth")
    double dbl_coin_changeOneMonth;
    @SerializedName("chartData")
    String str_coin_chartData;
    @SerializedName("dailyChartData")
    String str_coin_dailyChartData;
    @SerializedName("isFeatureCoin")
    String str_coin_isFeatureCoin;

    protected HeaderBanner(Parcel in) {
        str_image = in.readString();
        int_coin_id = in.readInt();
        str_coin_logo = in.readString();
        dbl_coin_usdValue = in.readDouble();
        int_coin_rank = in.readInt();
        dbl_coin_marketCap = in.readDouble();
        dbl_coin_volume = in.readDouble();
        dbl_coin_changeOneDay = in.readDouble();
        dbl_coin_changeSevenDays = in.readDouble();
        dbl_coin_changeOneMonth = in.readDouble();
        str_coin_chartData = in.readString();
        str_coin_dailyChartData = in.readString();
        str_coin_isFeatureCoin = in.readString();
    }

    public static final Creator<HeaderBanner> CREATOR = new Creator<HeaderBanner>() {
        @Override
        public HeaderBanner createFromParcel(Parcel in) {
            return new HeaderBanner(in);
        }

        @Override
        public HeaderBanner[] newArray(int size) {
            return new HeaderBanner[size];
        }
    };

    public String getStr_image() {
        return str_image;
    }

    public void setStr_image(String str_image) {
        this.str_image = str_image;
    }

    public int getInt_coin_id() {
        return int_coin_id;
    }

    public void setInt_coin_id(int int_coin_id) {
        this.int_coin_id = int_coin_id;
    }

    public String getStr_coin_logo() {
        return str_coin_logo;
    }

    public void setStr_coin_logo(String str_coin_logo) {
        this.str_coin_logo = str_coin_logo;
    }

    public double getDbl_coin_usdValue() {
        return dbl_coin_usdValue;
    }

    public void setDbl_coin_usdValue(double dbl_coin_usdValue) {
        this.dbl_coin_usdValue = dbl_coin_usdValue;
    }

    public int getInt_coin_rank() {
        return int_coin_rank;
    }

    public void setInt_coin_rank(int int_coin_rank) {
        this.int_coin_rank = int_coin_rank;
    }

    public double getDbl_coin_marketCap() {
        return dbl_coin_marketCap;
    }

    public void setDbl_coin_marketCap(double dbl_coin_marketCap) {
        this.dbl_coin_marketCap = dbl_coin_marketCap;
    }

    public double getDbl_coin_volume() {
        return dbl_coin_volume;
    }

    public void setDbl_coin_volume(double dbl_coin_volume) {
        this.dbl_coin_volume = dbl_coin_volume;
    }

    public double getDbl_coin_changeOneDay() {
        return dbl_coin_changeOneDay;
    }

    public void setDbl_coin_changeOneDay(double dbl_coin_changeOneDay) {
        this.dbl_coin_changeOneDay = dbl_coin_changeOneDay;
    }

    public double getDbl_coin_changeSevenDays() {
        return dbl_coin_changeSevenDays;
    }

    public void setDbl_coin_changeSevenDays(double dbl_coin_changeSevenDays) {
        this.dbl_coin_changeSevenDays = dbl_coin_changeSevenDays;
    }

    public double getDbl_coin_changeOneMonth() {
        return dbl_coin_changeOneMonth;
    }

    public void setDbl_coin_changeOneMonth(double dbl_coin_changeOneMonth) {
        this.dbl_coin_changeOneMonth = dbl_coin_changeOneMonth;
    }

    public String getStr_coin_chartData() {
        return str_coin_chartData;
    }

    public void setStr_coin_chartData(String str_coin_chartData) {
        this.str_coin_chartData = str_coin_chartData;
    }

    public String getStr_coin_dailyChartData() {
        return str_coin_dailyChartData;
    }

    public void setStr_coin_dailyChartData(String str_coin_dailyChartData) {
        this.str_coin_dailyChartData = str_coin_dailyChartData;
    }

    public String getStr_coin_isFeatureCoin() {
        return str_coin_isFeatureCoin;
    }

    public void setStr_coin_isFeatureCoin(String str_coin_isFeatureCoin) {
        this.str_coin_isFeatureCoin = str_coin_isFeatureCoin;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(str_image);
        dest.writeInt(int_coin_id);
        dest.writeString(str_coin_logo);
        dest.writeDouble(dbl_coin_usdValue);
        dest.writeInt(int_coin_rank);
        dest.writeDouble(dbl_coin_marketCap);
        dest.writeDouble(dbl_coin_volume);
        dest.writeDouble(dbl_coin_changeOneDay);
        dest.writeDouble(dbl_coin_changeSevenDays);
        dest.writeDouble(dbl_coin_changeOneMonth);
        dest.writeString(str_coin_chartData);
        dest.writeString(str_coin_dailyChartData);
        dest.writeString(str_coin_isFeatureCoin);
    }
}
