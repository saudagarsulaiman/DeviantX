package com.cryptowallet.deviantx.UI.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class BidsDC implements Parcelable {

    @SerializedName("price")
    double dbl_price;

    @SerializedName("volume")
    double dbl_volume;


    protected BidsDC(Parcel in) {
        dbl_price = in.readDouble();
        dbl_volume = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(dbl_price);
        dest.writeDouble(dbl_volume);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AsksDC> CREATOR = new Creator<AsksDC>() {
        @Override
        public AsksDC createFromParcel(Parcel in) {
            return new AsksDC(in);
        }

        @Override
        public AsksDC[] newArray(int size) {
            return new AsksDC[size];
        }
    };

    public double getDbl_price() {
        return dbl_price;
    }

    public void setDbl_price(double dbl_price) {
        this.dbl_price = dbl_price;
    }

    public double getDbl_volume() {
        return dbl_volume;
    }

    public void setDbl_volume(double dbl_volume) {
        this.dbl_volume = dbl_volume;
    }
}
