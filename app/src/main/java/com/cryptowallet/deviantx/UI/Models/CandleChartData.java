package com.cryptowallet.deviantx.UI.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CandleChartData implements Parcelable {

    @SerializedName("t")
    ArrayList<String>
            /*long */long_time;
    @SerializedName("o")
    ArrayList<String>
            /*double */dbl_open;
    @SerializedName("h")
    ArrayList<String>
            /*double */dbl_high;
    @SerializedName("l")
    ArrayList<String>
            /*double */dbl_low;
    @SerializedName("c")
    ArrayList<String>
            /*double */dbl_close;
    @SerializedName("v")
    ArrayList<String>
            /*double */dbl_volume;
    @SerializedName("s")
    String status;

    public CandleChartData(Parcel in) {
        long_time = in.createStringArrayList();
        dbl_open = in.createStringArrayList();
        dbl_high = in.createStringArrayList();
        dbl_low = in.createStringArrayList();
        dbl_close = in.createStringArrayList();
        dbl_volume = in.createStringArrayList();
        status = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(long_time);
        dest.writeStringList(dbl_open);
        dest.writeStringList(dbl_high);
        dest.writeStringList(dbl_low);
        dest.writeStringList(dbl_close);
        dest.writeStringList(dbl_volume);
        dest.writeString(status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CandleChartData> CREATOR = new Creator<CandleChartData>() {
        @Override
        public CandleChartData createFromParcel(Parcel in) {
            return new CandleChartData(in);
        }

        @Override
        public CandleChartData[] newArray(int size) {
            return new CandleChartData[size];
        }
    };

    public ArrayList<String> getLong_time() {
        return long_time;
    }

    public void setLong_time(ArrayList<String> long_time) {
        this.long_time = long_time;
    }

    public ArrayList<String> getDbl_open() {
        return dbl_open;
    }

    public void setDbl_open(ArrayList<String> dbl_open) {
        this.dbl_open = dbl_open;
    }

    public ArrayList<String> getDbl_high() {
        return dbl_high;
    }

    public void setDbl_high(ArrayList<String> dbl_high) {
        this.dbl_high = dbl_high;
    }

    public ArrayList<String> getDbl_low() {
        return dbl_low;
    }

    public void setDbl_low(ArrayList<String> dbl_low) {
        this.dbl_low = dbl_low;
    }

    public ArrayList<String> getDbl_close() {
        return dbl_close;
    }

    public void setDbl_close(ArrayList<String> dbl_close) {
        this.dbl_close = dbl_close;
    }

    public ArrayList<String> getDbl_volume() {
        return dbl_volume;
    }

    public void setDbl_volume(ArrayList<String> dbl_volume) {
        this.dbl_volume = dbl_volume;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public CandleChartData(ArrayList<String> long_time, ArrayList<String> dbl_open, ArrayList<String> dbl_high, ArrayList<String> dbl_low, ArrayList<String> dbl_close, ArrayList<String> dbl_volume) {
        this.long_time = long_time;
        this.dbl_open = dbl_open;
        this.dbl_high = dbl_high;
        this.dbl_low = dbl_low;
        this.dbl_close = dbl_close;
        this.dbl_volume = dbl_volume;
    }
}
