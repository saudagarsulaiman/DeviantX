package com.cryptowallet.deviantx.UI.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class AllCoins implements Parcelable {


    @SerializedName("coinCode")
    String str_coin_code;

/*
    @SerializedName("id")
    int int_coin_id;
*/

    @SerializedName("coinName")
    String str_coin_name;

    @SerializedName("logo")
    String str_coin_logo;

    @SerializedName("usdValue")
    Double dbl_coin_usdValue;

    @SerializedName("rank")
    int int_coin_rank;

    @SerializedName("marketCap")
    Double dbl_coin_marketCap;

    @SerializedName("volume")
    Double dbl_coin_volume;

    @SerializedName("changeOneDay")
    Double dbl_coin_24h;

    @SerializedName("changeSevenDays")
    Double dbl_coin_7d;

    @SerializedName("changeOneMonth")
    Double dbl_coin_1m;

    @SerializedName("isFeatureCoin")
    String str_isFeatureCoin;

    @SerializedName("chartData")
    String str_coin_chart_data;

    @SerializedName("dailyChartData")
    String str_coin_daily_chart_data;

    @SerializedName("apiId")
    String str_apiId;

    @SerializedName("minimumWithdrawl")
    Double dbl_minimumWithdrawl;

    @SerializedName("isToken")
    boolean bool_isToken;

    Boolean isSelected = false;

    protected AllCoins(Parcel in) {
        str_coin_code = in.readString();
        str_coin_name = in.readString();
        str_coin_logo = in.readString();
        if (in.readByte() == 0) {
            dbl_coin_usdValue = null;
        } else {
            dbl_coin_usdValue = in.readDouble();
        }
        int_coin_rank = in.readInt();
        if (in.readByte() == 0) {
            dbl_coin_marketCap = null;
        } else {
            dbl_coin_marketCap = in.readDouble();
        }
        if (in.readByte() == 0) {
            dbl_coin_volume = null;
        } else {
            dbl_coin_volume = in.readDouble();
        }
        if (in.readByte() == 0) {
            dbl_coin_24h = null;
        } else {
            dbl_coin_24h = in.readDouble();
        }
        if (in.readByte() == 0) {
            dbl_coin_7d = null;
        } else {
            dbl_coin_7d = in.readDouble();
        }
        if (in.readByte() == 0) {
            dbl_coin_1m = null;
        } else {
            dbl_coin_1m = in.readDouble();
        }
        str_isFeatureCoin = in.readString();
        str_coin_chart_data = in.readString();
        str_coin_daily_chart_data = in.readString();
        str_apiId = in.readString();
        if (in.readByte() == 0) {
            dbl_minimumWithdrawl = null;
        } else {
            dbl_minimumWithdrawl = in.readDouble();
        }
        bool_isToken = in.readByte() != 0;
        byte tmpIsSelected = in.readByte();
        isSelected = tmpIsSelected == 0 ? null : tmpIsSelected == 1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(str_coin_code);
        dest.writeString(str_coin_name);
        dest.writeString(str_coin_logo);
        if (dbl_coin_usdValue == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(dbl_coin_usdValue);
        }
        dest.writeInt(int_coin_rank);
        if (dbl_coin_marketCap == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(dbl_coin_marketCap);
        }
        if (dbl_coin_volume == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(dbl_coin_volume);
        }
        if (dbl_coin_24h == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(dbl_coin_24h);
        }
        if (dbl_coin_7d == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(dbl_coin_7d);
        }
        if (dbl_coin_1m == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(dbl_coin_1m);
        }
        dest.writeString(str_isFeatureCoin);
        dest.writeString(str_coin_chart_data);
        dest.writeString(str_coin_daily_chart_data);
        dest.writeString(str_apiId);
        if (dbl_minimumWithdrawl == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(dbl_minimumWithdrawl);
        }
        dest.writeByte((byte) (bool_isToken ? 1 : 0));
        dest.writeByte((byte) (isSelected == null ? 0 : isSelected ? 1 : 2));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AllCoins> CREATOR = new Creator<AllCoins>() {
        @Override
        public AllCoins createFromParcel(Parcel in) {
            return new AllCoins(in);
        }

        @Override
        public AllCoins[] newArray(int size) {
            return new AllCoins[size];
        }
    };

    public String getStr_coin_code() {
        return str_coin_code;
    }

    public void setStr_coin_code(String str_coin_code) {
        this.str_coin_code = str_coin_code;
    }

    public String getStr_coin_name() {
        return str_coin_name;
    }

    public void setStr_coin_name(String str_coin_name) {
        this.str_coin_name = str_coin_name;
    }

    public String getStr_coin_logo() {
        return str_coin_logo;
    }

    public void setStr_coin_logo(String str_coin_logo) {
        this.str_coin_logo = str_coin_logo;
    }

    public Double getDbl_coin_usdValue() {
        return dbl_coin_usdValue;
    }

    public void setDbl_coin_usdValue(Double dbl_coin_usdValue) {
        this.dbl_coin_usdValue = dbl_coin_usdValue;
    }

    public int getInt_coin_rank() {
        return int_coin_rank;
    }

    public void setInt_coin_rank(int int_coin_rank) {
        this.int_coin_rank = int_coin_rank;
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

    public String getStr_isFeatureCoin() {
        return str_isFeatureCoin;
    }

    public void setStr_isFeatureCoin(String str_isFeatureCoin) {
        this.str_isFeatureCoin = str_isFeatureCoin;
    }

    public String getStr_coin_chart_data() {
        return str_coin_chart_data;
    }

    public void setStr_coin_chart_data(String str_coin_chart_data) {
        this.str_coin_chart_data = str_coin_chart_data;
    }

    public String getStr_coin_daily_chart_data() {
        return str_coin_daily_chart_data;
    }

    public void setStr_coin_daily_chart_data(String str_coin_daily_chart_data) {
        this.str_coin_daily_chart_data = str_coin_daily_chart_data;
    }

    public String getStr_apiId() {
        return str_apiId;
    }

    public void setStr_apiId(String str_apiId) {
        this.str_apiId = str_apiId;
    }

    public Double getDbl_minimumWithdrawl() {
        return dbl_minimumWithdrawl;
    }

    public void setDbl_minimumWithdrawl(Double dbl_minimumWithdrawl) {
        this.dbl_minimumWithdrawl = dbl_minimumWithdrawl;
    }

    public boolean isBool_isToken() {
        return bool_isToken;
    }

    public void setBool_isToken(boolean bool_isToken) {
        this.bool_isToken = bool_isToken;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }
}
