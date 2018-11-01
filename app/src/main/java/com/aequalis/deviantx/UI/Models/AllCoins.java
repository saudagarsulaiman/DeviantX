package com.aequalis.deviantx.UI.Models;

import android.os.Parcel;
import android.os.Parcelable;


public class AllCoins  implements Parcelable {

    int int_coin_id;
    Double dbl_coin_usdValue;
    String str_coin_name,str_coin_code,str_coin_logo;
    Boolean isSelected =  false;
    public AllCoins(int int_coin_id, String str_coin_name, String str_coin_code, String str_coin_logo, Double dbl_coin_usdValue) {
        this.int_coin_id = int_coin_id;
        this.str_coin_name = str_coin_name;
        this.str_coin_code = str_coin_code;
        this.str_coin_logo = str_coin_logo;
        this.dbl_coin_usdValue = dbl_coin_usdValue;
    }

    public AllCoins() {

    }


    protected AllCoins(Parcel in) {
        int_coin_id = in.readInt();
        if (in.readByte() == 0) {
            dbl_coin_usdValue = null;
        } else {
            dbl_coin_usdValue = in.readDouble();
        }
        str_coin_name = in.readString();
        str_coin_code = in.readString();
        str_coin_logo = in.readString();
        byte tmpIsSelected = in.readByte();
        isSelected = tmpIsSelected == 0 ? null : tmpIsSelected == 1;
    }

    public static final Creator<AllCoins> CREATOR = new Creator<AllCoins>() {
        @Override
        public AllCoins createFromParcel(Parcel in) {
            return new AllCoins(in);
        }

        @Override
        public AllCoins[] newArray(int size) {
            return new AllCoins[size];
        }
    };

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public int getStr_coin_id() {
        return int_coin_id;
    }

    public void setStr_coin_id(int int_coin_id) {
        this.int_coin_id = int_coin_id;
    }

    public String getStr_coin_name() {
        return str_coin_name;
    }

    public void setStr_coin_name(String str_coin_name) {
        this.str_coin_name = str_coin_name;
    }

    public String getStr_coin_code() {
        return str_coin_code;
    }

    public void setStr_coin_code(String str_coin_code) {
        this.str_coin_code = str_coin_code;
    }

    public String getStr_coin_logo() {
        return str_coin_logo;
    }

    public void setStr_coin_logo(String str_coin_logo) {
        this.str_coin_logo = str_coin_logo;
    }

    public Double getStr_coin_usdValue() {
        return dbl_coin_usdValue;
    }

    public void setStr_coin_usdValue(Double dbl_coin_usdValue) {
        this.dbl_coin_usdValue = dbl_coin_usdValue;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(int_coin_id);
        if (dbl_coin_usdValue == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(dbl_coin_usdValue);
        }
        dest.writeString(str_coin_name);
        dest.writeString(str_coin_code);
        dest.writeString(str_coin_logo);
        dest.writeByte((byte) (isSelected == null ? 0 : isSelected ? 1 : 2));
    }
}
