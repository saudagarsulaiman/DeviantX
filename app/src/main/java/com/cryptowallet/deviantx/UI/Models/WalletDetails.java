package com.cryptowallet.deviantx.UI.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.cryptowallet.trendchart.DateValue;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class WalletDetails implements Parcelable {

    @SerializedName("walletName")
    String str_data_walletName;

    @SerializedName("id")
    int int_data_id;

    @SerializedName("balance")
    Double str_data_balance;

    @SerializedName("reservedBalance")
    Double str_data_reservedBalance;

    @SerializedName("balanceInUSD")
    Double str_data_balanceInUSD;

    @SerializedName("coinCode")
    String str_coin_code;

    @SerializedName("coinName")
    String str_coin_name;

    @SerializedName("logo")
    String str_coin_logo;

    @SerializedName("usdValue")
    Double dbl_coin_usdValue;

    @SerializedName("rank")
    int int_coin_rank;

    @SerializedName("marketCap")
    Double dbl_coin_marketCap;

    @SerializedName("volume")
    Double dbl_coin_volume;

    @SerializedName("changeOneDay")
    Double dbl_coin_24h;

    @SerializedName("changeSevenDays")
    Double dbl_coin_7d;

    @SerializedName("changeOneMonth")
    Double dbl_coin_1m;

    @SerializedName("dailyChartData")
    String str_coin_daily_chart_data;

    @SerializedName("isFeatureCoin")
    String str_coin_isFeatureCoin;

    @SerializedName("fav")
    Boolean isFav;


    protected WalletDetails(Parcel in) {
        str_data_walletName = in.readString();
        int_data_id = in.readInt();
        if (in.readByte() == 0) {
            str_data_balance = null;
        } else {
            str_data_balance = in.readDouble();
        }
        if (in.readByte() == 0) {
            str_data_reservedBalance = null;
        } else {
            str_data_reservedBalance = in.readDouble();
        }
        if (in.readByte() == 0) {
            str_data_balanceInUSD = null;
        } else {
            str_data_balanceInUSD = in.readDouble();
        }
        str_coin_code = in.readString();
        str_coin_name = in.readString();
        str_coin_logo = in.readString();
        if (in.readByte() == 0) {
            dbl_coin_usdValue = null;
        } else {
            dbl_coin_usdValue = in.readDouble();
        }
        int_coin_rank = in.readInt();
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
        str_coin_daily_chart_data = in.readString();
        str_coin_isFeatureCoin = in.readString();
        byte tmpIsFav = in.readByte();
        isFav = tmpIsFav == 0 ? null : tmpIsFav == 1;
        if (in.readByte() == 0) {
            highValue = null;
        } else {
            highValue = in.readDouble();
        }
    }

    public static final Creator<WalletDetails> CREATOR = new Creator<WalletDetails>() {
        @Override
        public WalletDetails createFromParcel(Parcel in) {
            return new WalletDetails(in);
        }

        @Override
        public WalletDetails[] newArray(int size) {
            return new WalletDetails[size];
        }
    };

    /* @SerializedName("address")
                String str_data_address;

                @SerializedName("privatekey")
                String str_data_privatekey;

                @SerializedName("passcode")
                String str_data_passcode;

                @SerializedName("account")
                String str_data_account;

                @SerializedName("balanceInINR")
                Double str_data_balanceInINR;

                @SerializedName("coin")
                AllCoinsDB allCoins;
            */
 /*   protected AccountWalletDB(Parcel in) {
        str_data_walletName = in.readString();
        int_data_id = in.readInt();
        str_data_address = in.readString();
        str_data_privatekey = in.readString();
        str_data_passcode = in.readString();
        str_data_account = in.readString();
        if (in.readByte() == 0) {
            str_data_balance = null;
        } else {
            str_data_balance = in.readDouble();
        }
        if (in.readByte() == 0) {
            str_data_balanceInUSD = null;
        } else {
            str_data_balanceInUSD = in.readDouble();
        }
        if (in.readByte() == 0) {
            str_data_balanceInINR = null;
        } else {
            str_data_balanceInINR = in.readDouble();
        }
        allCoins = in.readParcelable(AllCoinsDB.class.getClassLoader());
        byte tmpIsFav = in.readByte();
        isFav = tmpIsFav == 0 ? null : tmpIsFav == 1;
        if (in.readByte() == 0) {
            highValue = null;
        } else {
            highValue = in.readDouble();
        }
    }

    public static final Creator<AccountWalletDB> CREATOR = new Creator<AccountWalletDB>() {
        @Override
        public AccountWalletDB createFromParcel(Parcel in) {
            return new AccountWalletDB(in);
        }

        @Override
        public AccountWalletDB[] newArray(int size) {
            return new AccountWalletDB[size];
        }
    };
*/
    public String getStr_data_walletName() {
        return str_data_walletName;
    }

    public void setStr_data_walletName(String str_data_walletName) {
        this.str_data_walletName = str_data_walletName;
    }

    public int getInt_data_id() {
        return int_data_id;
    }

    public void setInt_data_id(int int_data_id) {
        this.int_data_id = int_data_id;
    }

    public Double getStr_data_balance() {
        return str_data_balance;
    }

    public void setStr_data_balance(Double str_data_balance) {
        this.str_data_balance = str_data_balance;
    }

    public Double getStr_data_balanceInUSD() {
        return str_data_balanceInUSD;
    }

    public void setStr_data_balanceInUSD(Double str_data_balanceInUSD) {
        this.str_data_balanceInUSD = str_data_balanceInUSD;
    }

    public Boolean getFav() {
        return isFav;
    }

    public void setFav(Boolean fav) {
        isFav = fav;
    }

    public Double getStr_data_reservedBalance() {
        return str_data_reservedBalance;
    }

    public void setStr_data_reservedBalance(Double str_data_reservedBalance) {
        this.str_data_reservedBalance = str_data_reservedBalance;
    }

    public String getStr_coin_code() {
        return str_coin_code;
    }

    public void setStr_coin_code(String str_coin_code) {
        this.str_coin_code = str_coin_code;
    }

    public String getStr_coin_name() {
        return str_coin_name;
    }

    public void setStr_coin_name(String str_coin_name) {
        this.str_coin_name = str_coin_name;
    }

    public String getStr_coin_logo() {
        return str_coin_logo;
    }

    public void setStr_coin_logo(String str_coin_logo) {
        this.str_coin_logo = str_coin_logo;
    }

    public Double getDbl_coin_usdValue() {
        return dbl_coin_usdValue;
    }

    public void setDbl_coin_usdValue(Double dbl_coin_usdValue) {
        this.dbl_coin_usdValue = dbl_coin_usdValue;
    }

    public int getInt_coin_rank() {
        return int_coin_rank;
    }

    public void setInt_coin_rank(int int_coin_rank) {
        this.int_coin_rank = int_coin_rank;
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

    public String getStr_coin_daily_chart_data() {
        return str_coin_daily_chart_data;
    }

    public void setStr_coin_daily_chart_data(String str_coin_daily_chart_data) {
        this.str_coin_daily_chart_data = str_coin_daily_chart_data;
    }

    public String getStr_coin_isFeatureCoin() {
        return str_coin_isFeatureCoin;
    }

    public void setStr_coin_isFeatureCoin(String str_coin_isFeatureCoin) {
        this.str_coin_isFeatureCoin = str_coin_isFeatureCoin;
    }

    Double highValue = 0.0;

    ArrayList<DateValue> responseList = new ArrayList<>();

    public Double getHighValue() {
        return highValue;
    }

    public void setHighValue(Double highValue) {
        this.highValue = highValue;
    }

    public ArrayList<DateValue> getResponseList() {
        return responseList;
    }

    public void setResponseList(ArrayList<DateValue> responseList) {
        this.responseList = responseList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(str_data_walletName);
        dest.writeInt(int_data_id);
        if (str_data_balance == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(str_data_balance);
        }
        if (str_data_reservedBalance == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(str_data_reservedBalance);
        }
        if (str_data_balanceInUSD == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(str_data_balanceInUSD);
        }
        dest.writeString(str_coin_code);
        dest.writeString(str_coin_name);
        dest.writeString(str_coin_logo);
        if (dbl_coin_usdValue == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(dbl_coin_usdValue);
        }
        dest.writeInt(int_coin_rank);
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
        dest.writeString(str_coin_daily_chart_data);
        dest.writeString(str_coin_isFeatureCoin);
        dest.writeByte((byte) (isFav == null ? 0 : isFav ? 1 : 2));
        if (highValue == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(highValue);
        }
    }


/*
    public String getStr_data_address() {
        return str_data_address;
    }

    public void setStr_data_address(String str_data_address) {
        this.str_data_address = str_data_address;
    }

    public String getStr_data_privatekey() {
        return str_data_privatekey;
    }

    public void setStr_data_privatekey(String str_data_privatekey) {
        this.str_data_privatekey = str_data_privatekey;
    }

    public String getStr_data_passcode() {
        return str_data_passcode;
    }

    public void setStr_data_passcode(String str_data_passcode) {
        this.str_data_passcode = str_data_passcode;
    }

    public String getStr_data_account() {
        return str_data_account;
    }

    public void setStr_data_account(String str_data_account) {
        this.str_data_account = str_data_account;
    }

   public Double getStr_data_balanceInINR() {
        return str_data_balanceInINR;
    }

    public void setStr_data_balanceInINR(Double str_data_balanceInINR) {
        this.str_data_balanceInINR = str_data_balanceInINR;
    }

    public AllCoinsDB getAllCoins() {
        return allCoins;
    }

    public void setAllCoins(AllCoinsDB allCoins) {
        this.allCoins = allCoins;
    }


   @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(str_data_walletName);
        dest.writeInt(int_data_id);
        dest.writeString(str_data_address);
        dest.writeString(str_data_privatekey);
        dest.writeString(str_data_passcode);
        dest.writeString(str_data_account);
        if (str_data_balance == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(str_data_balance);
        }
        if (str_data_balanceInUSD == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(str_data_balanceInUSD);
        }
        if (str_data_balanceInINR == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(str_data_balanceInINR);
        }
        dest.writeParcelable(allCoins, flags);
        dest.writeByte((byte) (isFav == null ? 0 : isFav ? 1 : 2));
        if (highValue == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(highValue);
        }
    }*/

}
