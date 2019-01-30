package com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

@Entity(tableName = "all_coins_table")
public class AllCoinsDB {
    public String walletName;
    public String coinsList;

    @PrimaryKey
    @NonNull
    public int walletId;

    public AllCoinsDB(String walletName, String coinsList, int walletId) {
        this.walletName = walletName;
        this.coinsList = coinsList;
        this.walletId = walletId;
    }
}
