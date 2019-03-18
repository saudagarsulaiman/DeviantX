package com.cryptowallet.deviantx.UI.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DepthChartData implements Parcelable {
    @SerializedName("Asks")
    List<AsksDC> list_Asks;

    @SerializedName("Bids")
    List<BidsDC> list_Bids;

    protected DepthChartData(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DepthChartData> CREATOR = new Creator<DepthChartData>() {
        @Override
        public DepthChartData createFromParcel(Parcel in) {
            return new DepthChartData(in);
        }

        @Override
        public DepthChartData[] newArray(int size) {
            return new DepthChartData[size];
        }
    };

    public List<AsksDC> getList_Asks() {
        return list_Asks;
    }

    public void setList_Asks(List<AsksDC> list_Asks) {
        this.list_Asks = list_Asks;
    }

    public List<BidsDC> getList_Bids() {
        return list_Bids;
    }

    public void setList_Bids(List<BidsDC> list_Bids) {
        this.list_Bids = list_Bids;
    }
}
