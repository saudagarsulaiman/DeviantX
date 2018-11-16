package com.cryptowallet.deviantx.UI.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class CoinsUSD implements Parcelable {
    String str_data_usd_1h, str_data_usd_24h, str_data_usd_7d;
    Double str_data_usd_price,str_data_usd_volume_24h,str_data_usd_market_cap;

    public CoinsUSD() {
    }

    public CoinsUSD(String str_data_usd_1h, String str_data_usd_24h, String str_data_usd_7d, Double str_data_usd_price, Double str_data_usd_volume_24h, Double str_data_usd_market_cap) {
        this.str_data_usd_1h = str_data_usd_1h;
        this.str_data_usd_24h = str_data_usd_24h;
        this.str_data_usd_7d = str_data_usd_7d;
        this.str_data_usd_price = str_data_usd_price;
        this.str_data_usd_volume_24h = str_data_usd_volume_24h;
        this.str_data_usd_market_cap = str_data_usd_market_cap;
    }

    protected CoinsUSD(Parcel in) {
        str_data_usd_1h = in.readString();
        str_data_usd_24h = in.readString();
        str_data_usd_7d = in.readString();
        if (in.readByte() == 0) {
            str_data_usd_price = null;
        } else {
            str_data_usd_price = in.readDouble();
        }
        if (in.readByte() == 0) {
            str_data_usd_volume_24h = null;
        } else {
            str_data_usd_volume_24h = in.readDouble();
        }
        if (in.readByte() == 0) {
            str_data_usd_market_cap = null;
        } else {
            str_data_usd_market_cap = in.readDouble();
        }
    }

    public static final Creator<CoinsUSD> CREATOR = new Creator<CoinsUSD>() {
        @Override
        public CoinsUSD createFromParcel(Parcel in) {
            return new CoinsUSD(in);
        }

        @Override
        public CoinsUSD[] newArray(int size) {
            return new CoinsUSD[size];
        }
    };

    public String getStr_data_usd_1h() {
        return str_data_usd_1h;
    }

    public void setStr_data_usd_1h(String str_data_usd_1h) {
        this.str_data_usd_1h = str_data_usd_1h;
    }

    public String getStr_data_usd_24h() {
        return str_data_usd_24h;
    }

    public void setStr_data_usd_24h(String str_data_usd_24h) {
        this.str_data_usd_24h = str_data_usd_24h;
    }

    public String getStr_data_usd_7d() {
        return str_data_usd_7d;
    }

    public void setStr_data_usd_7d(String str_data_usd_7d) {
        this.str_data_usd_7d = str_data_usd_7d;
    }

    public Double getStr_data_usd_price() {
        return str_data_usd_price;
    }

    public void setStr_data_usd_price(Double str_data_usd_price) {
        this.str_data_usd_price = str_data_usd_price;
    }

    public Double getStr_data_usd_volume_24h() {
        return str_data_usd_volume_24h;
    }

    public void setStr_data_usd_volume_24h(Double str_data_usd_volume_24h) {
        this.str_data_usd_volume_24h = str_data_usd_volume_24h;
    }

    public Double getStr_data_usd_market_cap() {
        return str_data_usd_market_cap;
    }

    public void setStr_data_usd_market_cap(Double str_data_usd_market_cap) {
        this.str_data_usd_market_cap = str_data_usd_market_cap;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(str_data_usd_1h);
        dest.writeString(str_data_usd_24h);
        dest.writeString(str_data_usd_7d);
        if (str_data_usd_price == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(str_data_usd_price);
        }
        if (str_data_usd_volume_24h == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(str_data_usd_volume_24h);
        }
        if (str_data_usd_market_cap == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(str_data_usd_market_cap);
        }
    }
}
