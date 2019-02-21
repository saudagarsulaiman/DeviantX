package com.cryptowallet.deviantx.UI.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllTransactions implements Parcelable {

    @SerializedName("deposit")
    List<SentHistory> list_received;

    @SerializedName("withdraw")
    List<SentHistory> list_sent;


    protected AllTransactions(Parcel in) {
        list_received = in.createTypedArrayList(SentHistory.CREATOR);
        list_sent = in.createTypedArrayList(SentHistory.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(list_received);
        dest.writeTypedList(list_sent);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AllTransactions> CREATOR = new Creator<AllTransactions>() {
        @Override
        public AllTransactions createFromParcel(Parcel in) {
            return new AllTransactions(in);
        }

        @Override
        public AllTransactions[] newArray(int size) {
            return new AllTransactions[size];
        }
    };

    public List<SentHistory> getList_received() {
        return list_received;
    }

    public void setList_received(List<SentHistory> list_received) {
        this.list_received = list_received;
    }

    public List<SentHistory> getList_sent() {
        return list_sent;
    }

    public void setList_sent(List<SentHistory> list_sent) {
        this.list_sent = list_sent;
    }
}
