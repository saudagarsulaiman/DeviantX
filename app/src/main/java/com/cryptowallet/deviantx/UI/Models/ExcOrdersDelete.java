package com.cryptowallet.deviantx.UI.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ExcOrdersDelete implements Parcelable {

    @SerializedName("buy")
    List<ExcOrders> list_buy;

    @SerializedName("sell")
    List<ExcOrders> list_sell;

    protected ExcOrdersDelete(Parcel in) {
        list_buy = in.createTypedArrayList(ExcOrders.CREATOR);
        list_sell = in.createTypedArrayList(ExcOrders.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(list_buy);
        dest.writeTypedList(list_sell);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public List<ExcOrders> getList_buy() {
        return list_buy;
    }

    public void setList_buy(List<ExcOrders> list_buy) {
        this.list_buy = list_buy;
    }

    public List<ExcOrders> getList_sell() {
        return list_sell;
    }

    public void setList_sell(List<ExcOrders> list_sell) {
        this.list_sell = list_sell;
    }
}
