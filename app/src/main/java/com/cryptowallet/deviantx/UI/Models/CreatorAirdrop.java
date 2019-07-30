package com.cryptowallet.deviantx.UI.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class CreatorAirdrop implements Parcelable {

    @SerializedName("id")
    int int_id;

    @SerializedName("airdropAmount")
    Double dbl_airdropAmount;

    @SerializedName("airdropAmountInUSD")
    Double dbl_airdropAmountInUSD;

    @SerializedName("airdropCreatedDate")
    String str_airdropCreatedDate;

    @SerializedName("coinName")
    String str_coinName;

    @SerializedName("coinlogo")
    String str_coinlogo;

    @SerializedName("coinCode")
    String str_coinCode;

    @SerializedName("estimated")
    String str_estimated;

    @SerializedName("email")
    String str_email;

    @SerializedName("status")
    String str_status;

    @SerializedName("isCancel")
    String isCancel;


    protected CreatorAirdrop(Parcel in) {
        int_id = in.readInt();
        if (in.readByte() == 0) {
            dbl_airdropAmount = null;
        } else {
            dbl_airdropAmount = in.readDouble();
        }
        if (in.readByte() == 0) {
            dbl_airdropAmountInUSD = null;
        } else {
            dbl_airdropAmountInUSD = in.readDouble();
        }
        str_airdropCreatedDate = in.readString();
        str_coinName = in.readString();
        str_coinlogo = in.readString();
        str_coinCode = in.readString();
        str_estimated = in.readString();
        str_email = in.readString();
        str_status = in.readString();
        isCancel = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(int_id);
        if (dbl_airdropAmount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(dbl_airdropAmount);
        }
        if (dbl_airdropAmountInUSD == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(dbl_airdropAmountInUSD);
        }
        dest.writeString(str_airdropCreatedDate);
        dest.writeString(str_coinName);
        dest.writeString(str_coinlogo);
        dest.writeString(str_coinCode);
        dest.writeString(str_estimated);
        dest.writeString(str_email);
        dest.writeString(str_status);
        dest.writeString(isCancel);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CreatorAirdrop> CREATOR = new Creator<CreatorAirdrop>() {
        @Override
        public CreatorAirdrop createFromParcel(Parcel in) {
            return new CreatorAirdrop(in);
        }

        @Override
        public CreatorAirdrop[] newArray(int size) {
            return new CreatorAirdrop[size];
        }
    };

    public int getInt_id() {
        return int_id;
    }

    public void setInt_id(int int_id) {
        this.int_id = int_id;
    }

    public Double getDbl_airdropAmount() {
        return dbl_airdropAmount;
    }

    public void setDbl_airdropAmount(Double dbl_airdropAmount) {
        this.dbl_airdropAmount = dbl_airdropAmount;
    }

    public Double getDbl_airdropAmountInUSD() {
        return dbl_airdropAmountInUSD;
    }

    public void setDbl_airdropAmountInUSD(Double dbl_airdropAmountInUSD) {
        this.dbl_airdropAmountInUSD = dbl_airdropAmountInUSD;
    }

    public String getStr_airdropCreatedDate() {
        return str_airdropCreatedDate;
    }

    public void setStr_airdropCreatedDate(String str_airdropCreatedDate) {
        this.str_airdropCreatedDate = str_airdropCreatedDate;
    }

    public String getStr_coinName() {
        return str_coinName;
    }

    public void setStr_coinName(String str_coinName) {
        this.str_coinName = str_coinName;
    }

    public String getStr_coinlogo() {
        return str_coinlogo;
    }

    public void setStr_coinlogo(String str_coinlogo) {
        this.str_coinlogo = str_coinlogo;
    }

    public String getStr_coinCode() {
        return str_coinCode;
    }

    public void setStr_coinCode(String str_coinCode) {
        this.str_coinCode = str_coinCode;
    }

    public String getStr_estimated() {
        return str_estimated;
    }

    public void setStr_estimated(String str_estimated) {
        this.str_estimated = str_estimated;
    }

    public String getStr_email() {
        return str_email;
    }

    public void setStr_email(String str_email) {
        this.str_email = str_email;
    }

    public String getStr_status() {
        return str_status;
    }

    public void setStr_status(String str_status) {
        this.str_status = str_status;
    }

    public String getIsCancel() {
        return isCancel;
    }

    public void setIsCancel(String isCancel) {
        this.isCancel = isCancel;
    }
}
