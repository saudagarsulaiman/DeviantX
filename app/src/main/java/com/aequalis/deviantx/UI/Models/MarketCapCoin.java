package com.aequalis.deviantx.UI.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class MarketCapCoin implements Parcelable {
    int int_data_id, int_data_rank;
    String str_data_name, str_data_symbol, str_data_website_slug, str_data_last_updated;
    Double str_data_circulating_supply, str_data_total_supply, str_data_max_supply;
    CoinsUSD coinsUSD;

    public MarketCapCoin(int int_data_id, int int_data_rank, String str_data_name, String str_data_symbol, String str_data_website_slug, String str_data_last_updated, Double str_data_circulating_supply, Double str_data_total_supply, Double str_data_max_supply, CoinsUSD coinsUSD) {
        this.int_data_id = int_data_id;
        this.int_data_rank = int_data_rank;
        this.str_data_name = str_data_name;
        this.str_data_symbol = str_data_symbol;
        this.str_data_website_slug = str_data_website_slug;
        this.str_data_last_updated = str_data_last_updated;
        this.str_data_circulating_supply = str_data_circulating_supply;
        this.str_data_total_supply = str_data_total_supply;
        this.str_data_max_supply = str_data_max_supply;
        this.coinsUSD = coinsUSD;
    }

    protected MarketCapCoin(Parcel in) {
        int_data_id = in.readInt();
        int_data_rank = in.readInt();
        str_data_name = in.readString();
        str_data_symbol = in.readString();
        str_data_website_slug = in.readString();
        str_data_last_updated = in.readString();
        if (in.readByte() == 0) {
            str_data_circulating_supply = null;
        } else {
            str_data_circulating_supply = in.readDouble();
        }
        if (in.readByte() == 0) {
            str_data_total_supply = null;
        } else {
            str_data_total_supply = in.readDouble();
        }
        if (in.readByte() == 0) {
            str_data_max_supply = null;
        } else {
            str_data_max_supply = in.readDouble();
        }
        coinsUSD = in.readParcelable(CoinsUSD.class.getClassLoader());
    }

    public static final Creator<MarketCapCoin> CREATOR = new Creator<MarketCapCoin>() {
        @Override
        public MarketCapCoin createFromParcel(Parcel in) {
            return new MarketCapCoin(in);
        }

        @Override
        public MarketCapCoin[] newArray(int size) {
            return new MarketCapCoin[size];
        }
    };

    public MarketCapCoin() {

    }

    public int getInt_data_id() {
        return int_data_id;
    }

    public void setInt_data_id(int int_data_id) {
        this.int_data_id = int_data_id;
    }

    public int getInt_data_rank() {
        return int_data_rank;
    }

    public void setInt_data_rank(int int_data_rank) {
        this.int_data_rank = int_data_rank;
    }

    public String getStr_data_name() {
        return str_data_name;
    }

    public void setStr_data_name(String str_data_name) {
        this.str_data_name = str_data_name;
    }

    public String getStr_data_symbol() {
        return str_data_symbol;
    }

    public void setStr_data_symbol(String str_data_symbol) {
        this.str_data_symbol = str_data_symbol;
    }

    public String getStr_data_website_slug() {
        return str_data_website_slug;
    }

    public void setStr_data_website_slug(String str_data_website_slug) {
        this.str_data_website_slug = str_data_website_slug;
    }

    public String getStr_data_last_updated() {
        return str_data_last_updated;
    }

    public void setStr_data_last_updated(String str_data_last_updated) {
        this.str_data_last_updated = str_data_last_updated;
    }

    public Double getStr_data_circulating_supply() {
        return str_data_circulating_supply;
    }

    public void setStr_data_circulating_supply(Double str_data_circulating_supply) {
        this.str_data_circulating_supply = str_data_circulating_supply;
    }

    public Double getStr_data_total_supply() {
        return str_data_total_supply;
    }

    public void setStr_data_total_supply(Double str_data_total_supply) {
        this.str_data_total_supply = str_data_total_supply;
    }

    public Double getStr_data_max_supply() {
        return str_data_max_supply;
    }

    public void setStr_data_max_supply(Double str_data_max_supply) {
        this.str_data_max_supply = str_data_max_supply;
    }

    public CoinsUSD getCoinsUSD() {
        return coinsUSD;
    }

    public void setCoinsUSD(CoinsUSD coinsUSD) {
        this.coinsUSD = coinsUSD;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(int_data_id);
        dest.writeInt(int_data_rank);
        dest.writeString(str_data_name);
        dest.writeString(str_data_symbol);
        dest.writeString(str_data_website_slug);
        dest.writeString(str_data_last_updated);
        if (str_data_circulating_supply == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(str_data_circulating_supply);
        }
        if (str_data_total_supply == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(str_data_total_supply);
        }
        if (str_data_max_supply == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(str_data_max_supply);
        }
        dest.writeParcelable(coinsUSD, flags);
    }
}
