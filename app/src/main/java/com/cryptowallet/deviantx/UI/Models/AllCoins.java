package com.cryptowallet.deviantx.UI.Models;

import android.os.Parcel;
import android.os.Parcelable;


public class AllCoins implements Parcelable {

    int int_coin_id, int_coin_rank;
    Double dbl_coin_usdValue, dbl_coin_marketCap, dbl_coin_volume, dbl_coin_24h, dbl_coin_7d, dbl_coin_1m;
    String str_coin_name, str_coin_code, str_coin_logo;
    Boolean isSelected = false;

    protected AllCoins(Parcel in) {
        int_coin_id = in.readInt();
        int_coin_rank = in.readInt();
        if (in.readByte() == 0) {
            dbl_coin_usdValue = null;
        } else {
            dbl_coin_usdValue = in.readDouble();
        }
        if (in.readByte() == 0) {
            dbl_coin_marketCap = null;
        } else {
            dbl_coin_marketCap = in.readDouble();
        }
        if (in.readByte() == 0) {
            dbl_coin_volume = null;
        } else {
            dbl_coin_volume = in.readDouble();
        }
        if (in.readByte() == 0) {
            dbl_coin_24h = null;
        } else {
            dbl_coin_24h = in.readDouble();
        }
        if (in.readByte() == 0) {
            dbl_coin_7d = null;
        } else {
            dbl_coin_7d = in.readDouble();
        }
        if (in.readByte() == 0) {
            dbl_coin_1m = null;
        } else {
            dbl_coin_1m = in.readDouble();
        }
        str_coin_name = in.readString();
        str_coin_code = in.readString();
        str_coin_logo = in.readString();
        byte tmpIsSelected = in.readByte();
        isSelected = tmpIsSelected == 0 ? null : tmpIsSelected == 1;
        byte tmpFav = in.readByte();
        fav = tmpFav == 0 ? null : tmpFav == 1;
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

    public Boolean getFav() {
        return fav;
    }

    public void setFav(Boolean fav) {
        this.fav = fav;
    }



    Boolean fav=false;



    public int getInt_coin_id() {
        return int_coin_id;
    }

    public void setInt_coin_id(int int_coin_id) {
        this.int_coin_id = int_coin_id;
    }

    public int getInt_coin_rank() {
        return int_coin_rank;
    }

    public void setInt_coin_rank(int int_coin_rank) {
        this.int_coin_rank = int_coin_rank;
    }

    public Double getDbl_coin_usdValue() {
        return dbl_coin_usdValue;
    }

    public void setDbl_coin_usdValue(Double dbl_coin_usdValue) {
        this.dbl_coin_usdValue = dbl_coin_usdValue;
    }

    public Double getDbl_coin_marketCap() {
        return dbl_coin_marketCap;
    }

    public void setDbl_coin_marketCap(Double dbl_coin_marketCap) {
        this.dbl_coin_marketCap = dbl_coin_marketCap;
    }

    public Double getDbl_coin_volume() {
        return dbl_coin_volume;
    }

    public void setDbl_coin_volume(Double dbl_coin_volume) {
        this.dbl_coin_volume = dbl_coin_volume;
    }

    public Double getDbl_coin_24h() {
        return dbl_coin_24h;
    }

    public void setDbl_coin_24h(Double dbl_coin_24h) {
        this.dbl_coin_24h = dbl_coin_24h;
    }

    public Double getDbl_coin_7d() {
        return dbl_coin_7d;
    }

    public void setDbl_coin_7d(Double dbl_coin_7d) {
        this.dbl_coin_7d = dbl_coin_7d;
    }

    public Double getDbl_coin_1m() {
        return dbl_coin_1m;
    }

    public void setDbl_coin_1m(Double dbl_coin_1m) {
        this.dbl_coin_1m = dbl_coin_1m;
    }

    public AllCoins(int int_coin_id, String str_coin_name, String str_coin_code, String str_coin_logo, Double dbl_coin_usdValue,int int_coin_rank, Double dbl_coin_marketCap, Double dbl_coin_volume, Double dbl_coin_24h, Double dbl_coin_7d, Double dbl_coin_1m,boolean fav) {
        this.int_coin_id = int_coin_id;
        this.int_coin_rank = int_coin_rank;
        this.dbl_coin_usdValue = dbl_coin_usdValue;
        this.dbl_coin_marketCap = dbl_coin_marketCap;
        this.dbl_coin_volume = dbl_coin_volume;
        this.dbl_coin_24h = dbl_coin_24h;
        this.dbl_coin_7d = dbl_coin_7d;
        this.dbl_coin_1m = dbl_coin_1m;
        this.str_coin_name = str_coin_name;
        this.str_coin_code = str_coin_code;
        this.str_coin_logo = str_coin_logo;
        this.fav=fav;
    }

    public AllCoins(int int_coin_id, String str_coin_name, String str_coin_code, String str_coin_logo, Double dbl_coin_usdValue) {
        this.int_coin_id = int_coin_id;
        this.str_coin_name = str_coin_name;
        this.str_coin_code = str_coin_code;
        this.str_coin_logo = str_coin_logo;
        this.dbl_coin_usdValue = dbl_coin_usdValue;
    }

    public AllCoins() {

    }


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
        dest.writeInt(int_coin_rank);
        if (dbl_coin_usdValue == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(dbl_coin_usdValue);
        }
        if (dbl_coin_marketCap == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(dbl_coin_marketCap);
        }
        if (dbl_coin_volume == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(dbl_coin_volume);
        }
        if (dbl_coin_24h == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(dbl_coin_24h);
        }
        if (dbl_coin_7d == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(dbl_coin_7d);
        }
        if (dbl_coin_1m == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(dbl_coin_1m);
        }
        dest.writeString(str_coin_name);
        dest.writeString(str_coin_code);
        dest.writeString(str_coin_logo);
        dest.writeByte((byte) (isSelected == null ? 0 : isSelected ? 1 : 2));
        dest.writeByte((byte) (fav == null ? 0 : fav ? 1 : 2));
    }
}
