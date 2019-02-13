package com.cryptowallet.deviantx.UI.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class PairsList implements Parcelable {

    @SerializedName("Ws_Link")
    String str_Ws_Link;

    @SerializedName("Code")
    String str_Code;

    @SerializedName("Name")
    String str_Name;


    protected PairsList(Parcel in) {
        str_Ws_Link = in.readString();
        str_Code = in.readString();
        str_Name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(str_Ws_Link);
        dest.writeString(str_Code);
        dest.writeString(str_Name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PairsList> CREATOR = new Creator<PairsList>() {
        @Override
        public PairsList createFromParcel(Parcel in) {
            return new PairsList(in);
        }

        @Override
        public PairsList[] newArray(int size) {
            return new PairsList[size];
        }
    };

    public String getStr_Ws_Link() {
        return str_Ws_Link;
    }

    public void setStr_Ws_Link(String str_Ws_Link) {
        this.str_Ws_Link = str_Ws_Link;
    }

    public String getStr_Code() {
        return str_Code;
    }

    public void setStr_Code(String str_Code) {
        this.str_Code = str_Code;
    }

    public String getStr_Name() {
        return str_Name;
    }

    public void setStr_Name(String str_Name) {
        this.str_Name = str_Name;
    }
}
