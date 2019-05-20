package com.cryptowallet.deviantx.UI.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ExcOrdersDelete implements Parcelable {

    @SerializedName("ask")
    List<ExcOrders> list_ask;

    @SerializedName("bid")
    List<ExcOrders> list_bid;

    @SerializedName("trade")
    List<ExcOrders> list_trade;

    protected ExcOrdersDelete(Parcel in) {
        list_ask = in.createTypedArrayList(ExcOrders.CREATOR);
        list_bid = in.createTypedArrayList(ExcOrders.CREATOR);
        list_trade = in.createTypedArrayList(ExcOrders.CREATOR);
    }

    public static final Creator<ExcOrdersDelete> CREATOR = new Creator<ExcOrdersDelete>() {
        @Override
        public ExcOrdersDelete createFromParcel(Parcel in) {
            return new ExcOrdersDelete(in);
        }

        @Override
        public ExcOrdersDelete[] newArray(int size) {
            return new ExcOrdersDelete[size];
        }
    };

    public List<ExcOrders> getList_ask() {
        return list_ask;
    }

    public void setList_ask(List<ExcOrders> list_ask) {
        this.list_ask = list_ask;
    }

    public List<ExcOrders> getList_bid() {
        return list_bid;
    }

    public void setList_bid(List<ExcOrders> list_bid) {
        this.list_bid = list_bid;
    }

    public List<ExcOrders> getList_trade() {
        return list_trade;
    }

    public void setList_trade(List<ExcOrders> list_trade) {
        this.list_trade = list_trade;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(list_ask);
        dest.writeTypedList(list_bid);
        dest.writeTypedList(list_trade);
    }
}
