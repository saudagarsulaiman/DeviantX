package com.cryptowallet.deviantx.UI.Models;

import android.support.annotation.NonNull;

import java.util.Calendar;
import java.util.Date;

public class CoinGraph implements Comparable<CoinGraph> {

    public Date time;
    public float high;
    public double open, low, close, change, amplitude;

    public CoinGraph(long time, double open, double high, double low, double close, double change, double amplitude) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        Date d1 = calendar.getTime();
        this.time = d1;
        this.open = open;
        this.high = (float) high;
        this.low = low;
        this.close = close;
        this.change = change;
        this.amplitude = amplitude;
    }
    public CoinGraph(long time, double open, double high, double low, double close) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        Date d1 = calendar.getTime();
        this.time = d1;
        this.open = open;
        this.high = (float) high;
        this.low = low;
        this.close = close;
    }


    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public float getHigh() {
        return high;
    }

    public void setHigh(float high) {
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



   /* public Date getDate() {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(time);
            Date d1 = calendar.getTime();
            return d1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }*/

    @Override
    public int compareTo(@NonNull CoinGraph measure) {
        if (time == null || measure.time == null)
            return 0;
        return time.compareTo(measure.time);
    }
}
