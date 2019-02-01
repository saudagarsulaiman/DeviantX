package com.cryptowallet.deviantx.UI.Models;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class NewsDX implements Parcelable {


    @SerializedName("content")
    String str_content;

    @SerializedName("head")
    String str_head;

    @SerializedName("id")
    int int_id;

    @SerializedName("newsPanelType")
    String str_newsPanelType;


    protected NewsDX(Parcel in) {
        str_content = in.readString();
        str_head = in.readString();
        int_id = in.readInt();
        str_newsPanelType = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(str_content);
        dest.writeString(str_head);
        dest.writeInt(int_id);
        dest.writeString(str_newsPanelType);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NewsDX> CREATOR = new Creator<NewsDX>() {
        @Override
        public NewsDX createFromParcel(Parcel in) {
            return new NewsDX(in);
        }

        @Override
        public NewsDX[] newArray(int size) {
            return new NewsDX[size];
        }
    };

    public String getStr_content() {
        return str_content;
    }

    public void setStr_content(String str_content) {
        this.str_content = str_content;
    }

    public String getStr_head() {
        return str_head;
    }

    public void setStr_head(String str_head) {
        this.str_head = str_head;
    }

    public int getInt_id() {
        return int_id;
    }

    public void setInt_id(int int_id) {
        this.int_id = int_id;
    }

    public String getStr_newsPanelType() {
        return str_newsPanelType;
    }

    public void setStr_newsPanelType(String str_newsPanelType) {
        this.str_newsPanelType = str_newsPanelType;
    }
}
