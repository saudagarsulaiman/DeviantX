package com.cryptowallet.deviantx.UI.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class WalletList implements Parcelable {
    int int_data_id;
    double dbl_data_totalBal;
    String str_data_name;


    protected WalletList(Parcel in) {
        int_data_id = in.readInt();
        dbl_data_totalBal = in.readDouble();
        str_data_name = in.readString();
        defaultWallet = in.readByte() != 0;
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

    public boolean isDefaultWallet() {
        return defaultWallet;
    }

    public void setDefaultWallet(boolean defaultWallet) {
        this.defaultWallet = defaultWallet;
    }

    boolean defaultWallet = false;

    public WalletList(int int_data_id, String str_data_name, double dbl_data_totalBal, boolean defaultWallet) {
        this.int_data_id = int_data_id;
        this.dbl_data_totalBal = dbl_data_totalBal;
        this.str_data_name = str_data_name;
        this.defaultWallet = defaultWallet;
    }

    public WalletList(int int_data_id, String str_data_name, double dbl_data_totalBal) {
        this.int_data_id = int_data_id;
        this.dbl_data_totalBal = dbl_data_totalBal;
        this.str_data_name = str_data_name;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(int_data_id);
        dest.writeDouble(dbl_data_totalBal);
        dest.writeString(str_data_name);
        dest.writeByte((byte) (defaultWallet ? 1 : 0));
    }


}
