package com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "wallet_details_table")
public class WalletDetailsDB {

    @PrimaryKey
    @NonNull
    public int id;

    public String walletDetails;

    public WalletDetailsDB(int id, String walletDetails) {
        this.id = id;
        this.walletDetails = walletDetails;
    }

}
