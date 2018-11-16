package com.cryptowallet.deviantx.UI.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class CryptoWallet implements Parcelable {
    String str_data_cryptoWallet_address;
    Double dbl_data_cryptoWallet_bal;

    public CryptoWallet(String str_data_cryptoWallet_address, Double dbl_data_cryptoWallet_bal) {
        this.str_data_cryptoWallet_address = str_data_cryptoWallet_address;
        this.dbl_data_cryptoWallet_bal = dbl_data_cryptoWallet_bal;
    }

    protected CryptoWallet(Parcel in) {
        str_data_cryptoWallet_address = in.readString();
        if (in.readByte() == 0) {
            dbl_data_cryptoWallet_bal = null;
        } else {
            dbl_data_cryptoWallet_bal = in.readDouble();
        }
    }

    public static final Creator<CryptoWallet> CREATOR = new Creator<CryptoWallet>() {
        @Override
        public CryptoWallet createFromParcel(Parcel in) {
            return new CryptoWallet(in);
        }

        @Override
        public CryptoWallet[] newArray(int size) {
            return new CryptoWallet[size];
        }
    };

    public String getStr_data_cryptoWallet_address() {
        return str_data_cryptoWallet_address;
    }

    public void setStr_data_cryptoWallet_address(String str_data_cryptoWallet_address) {
        this.str_data_cryptoWallet_address = str_data_cryptoWallet_address;
    }

    public Double getDbl_data_cryptoWallet_bal() {
        return dbl_data_cryptoWallet_bal;
    }

    public void setDbl_data_cryptoWallet_bal(Double dbl_data_cryptoWallet_bal) {
        this.dbl_data_cryptoWallet_bal = dbl_data_cryptoWallet_bal;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(str_data_cryptoWallet_address);
        if (dbl_data_cryptoWallet_bal == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(dbl_data_cryptoWallet_bal);
        }
    }
}
