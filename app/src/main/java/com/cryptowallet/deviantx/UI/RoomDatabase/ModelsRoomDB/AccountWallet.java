package com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.cryptowallet.trendchart.DateValue;

import java.util.ArrayList;

@Entity(tableName = "account_wallet_table")
public class AccountWallet{

    @PrimaryKey
    @NonNull
    public String walletDatas;


    public AccountWallet(@NonNull String walletDatas) {
        this.walletDatas = walletDatas;
    }


}
