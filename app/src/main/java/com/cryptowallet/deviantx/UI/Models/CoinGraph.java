package com.cryptowallet.deviantx.UI.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class CoinGraph implements Parcelable {

    long time;
    double open, high, low, close, change, amplitude;

    public CoinGraph(long time, double open, double high, double low, double close, double change, double amplitude) {
        this.time = time;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.change = change;
        this.amplitude = amplitude;
    }

    protected CoinGraph(Parcel in) {
        time = in.readLong();
        open = in.readDouble();
        high = in.readDouble();
        low = in.readDouble();
        close = in.readDouble();
        change = in.readDouble();
        amplitude = in.readDouble();
    }

    public static final Creator<CoinGraph> CREATOR = new Creator<CoinGraph>() {
        @Override
        public CoinGraph createFromParcel(Parcel in) {
            return new CoinGraph(in);
        }

        @Override
        public CoinGraph[] newArray(int size) {
            return new CoinGraph[size];
        }
    };

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public double getAmplitude() {
        return amplitude;
    }

    public void setAmplitude(double amplitude) {
        this.amplitude = amplitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(time);
        dest.writeDouble(open);
        dest.writeDouble(high);
        dest.writeDouble(low);
        dest.writeDouble(close);
        dest.writeDouble(change);
        dest.writeDouble(amplitude);
    }
}
