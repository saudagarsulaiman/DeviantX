package com.cryptowallet.deviantx.UI.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class AirdropWallet implements Parcelable {

    int int_ad_data_id, int_ad_coin_id, int_ad_coin_rank;
    String startDate, str_data_ad_address, str_data_ad_privatekey, str_data_ad_passcode, str_data_ad_account,
            str_data_ad_coin, str_ad_coin_name, str_ad_coin_code, str_ad_coin_logo, str_ad_coin_chart_data;
    Double dbl_data_ad_balance, dbl_data_ad_balanceInUSD, dbl_ad_coin_usdValue, dbl_ad_coin_marketCap, dbl_ad_coin_volume,
            dbl_ad_coin_1m, dbl_ad_coin_7d, dbl_ad_coin_24h;
    AllCoins allCoins;

    protected AirdropWallet(Parcel in) {
        int_ad_data_id = in.readInt();
        int_ad_coin_id = in.readInt();
        int_ad_coin_rank = in.readInt();
        startDate = in.readString();
        str_data_ad_address = in.readString();
        str_data_ad_privatekey = in.readString();
        str_data_ad_passcode = in.readString();
        str_data_ad_account = in.readString();
        str_data_ad_coin = in.readString();
        str_ad_coin_name = in.readString();
        str_ad_coin_code = in.readString();
        str_ad_coin_logo = in.readString();
        str_ad_coin_chart_data = in.readString();
        if (in.readByte() == 0) {
            dbl_data_ad_balance = null;
        } else {
            dbl_data_ad_balance = in.readDouble();
        }
        if (in.readByte() == 0) {
            dbl_data_ad_balanceInUSD = null;
        } else {
            dbl_data_ad_balanceInUSD = in.readDouble();
        }
        if (in.readByte() == 0) {
            dbl_ad_coin_usdValue = null;
        } else {
            dbl_ad_coin_usdValue = in.readDouble();
        }
        if (in.readByte() == 0) {
            dbl_ad_coin_marketCap = null;
        } else {
            dbl_ad_coin_marketCap = in.readDouble();
        }
        if (in.readByte() == 0) {
            dbl_ad_coin_volume = null;
        } else {
            dbl_ad_coin_volume = in.readDouble();
        }
        if (in.readByte() == 0) {
            dbl_ad_coin_1m = null;
        } else {
            dbl_ad_coin_1m = in.readDouble();
        }
        if (in.readByte() == 0) {
            dbl_ad_coin_7d = null;
        } else {
            dbl_ad_coin_7d = in.readDouble();
        }
        if (in.readByte() == 0) {
            dbl_ad_coin_24h = null;
        } else {
            dbl_ad_coin_24h = in.readDouble();
        }
        allCoins = in.readParcelable(AllCoins.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(int_ad_data_id);
        dest.writeInt(int_ad_coin_id);
        dest.writeInt(int_ad_coin_rank);
        dest.writeString(startDate);
        dest.writeString(str_data_ad_address);
        dest.writeString(str_data_ad_privatekey);
        dest.writeString(str_data_ad_passcode);
        dest.writeString(str_data_ad_account);
        dest.writeString(str_data_ad_coin);
        dest.writeString(str_ad_coin_name);
        dest.writeString(str_ad_coin_code);
        dest.writeString(str_ad_coin_logo);
        dest.writeString(str_ad_coin_chart_data);
        if (dbl_data_ad_balance == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(dbl_data_ad_balance);
        }
        if (dbl_data_ad_balanceInUSD == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(dbl_data_ad_balanceInUSD);
        }
        if (dbl_ad_coin_usdValue == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(dbl_ad_coin_usdValue);
        }
        if (dbl_ad_coin_marketCap == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(dbl_ad_coin_marketCap);
        }
        if (dbl_ad_coin_volume == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(dbl_ad_coin_volume);
        }
        if (dbl_ad_coin_1m == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(dbl_ad_coin_1m);
        }
        if (dbl_ad_coin_7d == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(dbl_ad_coin_7d);
        }
        if (dbl_ad_coin_24h == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(dbl_ad_coin_24h);
        }
        dest.writeParcelable(allCoins, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AirdropWallet> CREATOR = new Creator<AirdropWallet>() {
        @Override
        public AirdropWallet createFromParcel(Parcel in) {
            return new AirdropWallet(in);
        }

        @Override
        public AirdropWallet[] newArray(int size) {
            return new AirdropWallet[size];
        }
    };

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public int getInt_ad_data_id() {
        return int_ad_data_id;
    }

    public void setInt_ad_data_id(int int_ad_data_id) {
        this.int_ad_data_id = int_ad_data_id;
    }

    public int getInt_ad_coin_id() {
        return int_ad_coin_id;
    }

    public void setInt_ad_coin_id(int int_ad_coin_id) {
        this.int_ad_coin_id = int_ad_coin_id;
    }

    public int getInt_ad_coin_rank() {
        return int_ad_coin_rank;
    }

    public void setInt_ad_coin_rank(int int_ad_coin_rank) {
        this.int_ad_coin_rank = int_ad_coin_rank;
    }

    public String getStr_data_ad_address() {
        return str_data_ad_address;
    }

    public void setStr_data_ad_address(String str_data_ad_address) {
        this.str_data_ad_address = str_data_ad_address;
    }

    public String getStr_data_ad_privatekey() {
        return str_data_ad_privatekey;
    }

    public void setStr_data_ad_privatekey(String str_data_ad_privatekey) {
        this.str_data_ad_privatekey = str_data_ad_privatekey;
    }

    public String getStr_data_ad_passcode() {
        return str_data_ad_passcode;
    }

    public void setStr_data_ad_passcode(String str_data_ad_passcode) {
        this.str_data_ad_passcode = str_data_ad_passcode;
    }

    public String getStr_data_ad_account() {
        return str_data_ad_account;
    }

    public void setStr_data_ad_account(String str_data_ad_account) {
        this.str_data_ad_account = str_data_ad_account;
    }

    public String getStr_data_ad_coin() {
        return str_data_ad_coin;
    }

    public void setStr_data_ad_coin(String str_data_ad_coin) {
        this.str_data_ad_coin = str_data_ad_coin;
    }

    public String getStr_ad_coin_name() {
        return str_ad_coin_name;
    }

    public void setStr_ad_coin_name(String str_ad_coin_name) {
        this.str_ad_coin_name = str_ad_coin_name;
    }

    public String getStr_ad_coin_code() {
        return str_ad_coin_code;
    }

    public void setStr_ad_coin_code(String str_ad_coin_code) {
        this.str_ad_coin_code = str_ad_coin_code;
    }

    public String getStr_ad_coin_logo() {
        return str_ad_coin_logo;
    }

    public void setStr_ad_coin_logo(String str_ad_coin_logo) {
        this.str_ad_coin_logo = str_ad_coin_logo;
    }

    public String getStr_ad_coin_chart_data() {
        return str_ad_coin_chart_data;
    }

    public void setStr_ad_coin_chart_data(String str_ad_coin_chart_data) {
        this.str_ad_coin_chart_data = str_ad_coin_chart_data;
    }

    public Double getDbl_data_ad_balance() {
        return dbl_data_ad_balance;
    }

    public void setDbl_data_ad_balance(Double dbl_data_ad_balance) {
        this.dbl_data_ad_balance = dbl_data_ad_balance;
    }

    public Double getDbl_data_ad_balanceInUSD() {
        return dbl_data_ad_balanceInUSD;
    }

    public void setDbl_data_ad_balanceInUSD(Double dbl_data_ad_balanceInUSD) {
        this.dbl_data_ad_balanceInUSD = dbl_data_ad_balanceInUSD;
    }

    public Double getDbl_ad_coin_usdValue() {
        return dbl_ad_coin_usdValue;
    }

    public void setDbl_ad_coin_usdValue(Double dbl_ad_coin_usdValue) {
        this.dbl_ad_coin_usdValue = dbl_ad_coin_usdValue;
    }

    public Double getDbl_ad_coin_marketCap() {
        return dbl_ad_coin_marketCap;
    }

    public void setDbl_ad_coin_marketCap(Double dbl_ad_coin_marketCap) {
        this.dbl_ad_coin_marketCap = dbl_ad_coin_marketCap;
    }

    public Double getDbl_ad_coin_volume() {
        return dbl_ad_coin_volume;
    }

    public void setDbl_ad_coin_volume(Double dbl_ad_coin_volume) {
        this.dbl_ad_coin_volume = dbl_ad_coin_volume;
    }

    public Double getDbl_ad_coin_1m() {
        return dbl_ad_coin_1m;
    }

    public void setDbl_ad_coin_1m(Double dbl_ad_coin_1m) {
        this.dbl_ad_coin_1m = dbl_ad_coin_1m;
    }

    public Double getDbl_ad_coin_7d() {
        return dbl_ad_coin_7d;
    }

    public void setDbl_ad_coin_7d(Double dbl_ad_coin_7d) {
        this.dbl_ad_coin_7d = dbl_ad_coin_7d;
    }

    public Double getDbl_ad_coin_24h() {
        return dbl_ad_coin_24h;
    }

    public void setDbl_ad_coin_24h(Double dbl_ad_coin_24h) {
        this.dbl_ad_coin_24h = dbl_ad_coin_24h;
    }

    public AllCoins getAllCoins() {
        return allCoins;
    }

    public void setAllCoins(AllCoins allCoins) {
        this.allCoins = allCoins;
    }

    public AirdropWallet(String startDate, int int_ad_data_id, String str_data_ad_address, String str_data_ad_privatekey, String str_data_ad_passcode, Double dbl_data_ad_balance, Double dbl_data_ad_balanceInUSD, String str_data_ad_account, AllCoins allCoins) {
        this.startDate = startDate;
        this.int_ad_data_id = int_ad_data_id;
        this.str_data_ad_address = str_data_ad_address;
        this.str_data_ad_privatekey = str_data_ad_privatekey;
        this.str_data_ad_passcode = str_data_ad_passcode;
        this.str_data_ad_account = str_data_ad_account;
        this.dbl_data_ad_balance = dbl_data_ad_balance;
        this.dbl_data_ad_balanceInUSD = dbl_data_ad_balanceInUSD;
        this.allCoins = allCoins;

    }


}
