package com.cryptowallet.deviantx.UI.Models;

import android.support.annotation.NonNull;

import java.util.Calendar;
import java.util.Date;

public class CandleGraph implements Comparable<CandleGraph> {

    public Date time;
    public float high;
    public double open, low, close, volume;

    public CandleGraph(long time, double open, double high, double low, double close, double volume) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        Date d1 = calendar.getTime();
        this.time = d1;
        this.open = open;
        this.high = (float) high;
        this.low = low;
        this.close = close;
        this.volume = volume;
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
        return volume;
    }

    public void setChange(double volume) {
        this.volume = volume;
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
    public int compareTo(@NonNull CandleGraph measure) {
        if (time == null || measure.time == null)
            return 0;
        return time.compareTo(measure.time);
    }
}
