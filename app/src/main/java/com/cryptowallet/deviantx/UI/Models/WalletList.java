package com.cryptowallet.deviantx.UI.Models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.cryptowallet.trendchart.DateValue;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class WalletList implements Parcelable {

    @SerializedName("id")
    int int_data_id;
    @SerializedName("toatalBalance")
    double dbl_data_totalBal;
    @SerializedName("name")
    String str_data_name;
    @SerializedName("defaultWallet")
    boolean defaultWallet;

    protected WalletList(Parcel in) {
        int_data_id = in.readInt();
        dbl_data_totalBal = in.readDouble();
        str_data_name = in.readString();
        byte tmpIsSelected = in.readByte();
        isSelected = tmpIsSelected == 0 ? null : tmpIsSelected == 1;
        defaultWallet = in.readByte() != 0;
        if (in.readByte() == 0) {
            highValue = null;
        } else {
            highValue = in.readDouble();
        }
    }

    public static final Creator<WalletList> CREATOR = new Creator<WalletList>() {
        @Override
        public WalletList createFromParcel(Parcel in) {
            return new WalletList(in);
        }

        @Override
        public WalletList[] newArray(int size) {
            return new WalletList[size];
        }
    };

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    Boolean isSelected = false;


    public boolean isDefaultWallet() {
        return defaultWallet;
    }

    public void setDefaultWallet(boolean defaultWallet) {
        this.defaultWallet = defaultWallet;
    }


    public WalletList(int int_data_id, String str_data_name, double dbl_data_totalBal, boolean defaultWallet) {
        this.int_data_id = int_data_id;
        this.dbl_data_totalBal = dbl_data_totalBal;
        this.str_data_name = str_data_name;
        this.defaultWallet = defaultWallet;
    }

    public int getInt_data_id() {
        return int_data_id;
    }

    public void setInt_data_id(int int_data_id) {
        this.int_data_id = int_data_id;
    }

    public double getDbl_data_totalBal() {
        return dbl_data_totalBal;
    }

    public void setDbl_data_totalBal(double dbl_data_totalBal) {
        this.dbl_data_totalBal = dbl_data_totalBal;
    }

    public String getStr_data_name() {
        return str_data_name;
    }

    public void setStr_data_name(String str_data_name) {
        this.str_data_name = str_data_name;
    }


    ArrayList<DateValue> responseList = new ArrayList<>();

    Double highValue = 0.0;

    public void setResponseList(ArrayList<DateValue> responseList) {
        this.responseList = responseList;
    }

    public ArrayList<DateValue> getResponseList() {
        return responseList;
    }

    public void setHighValue(Double highValue) {
        this.highValue = highValue;
    }

    public Double getHighValue() {
        return highValue;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(int_data_id);
        dest.writeDouble(dbl_data_totalBal);
        dest.writeString(str_data_name);
        dest.writeByte((byte) (isSelected == null ? 0 : isSelected ? 1 : 2));
        dest.writeByte((byte) (defaultWallet ? 1 : 0));
        if (highValue == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(highValue);
        }
    }
}
