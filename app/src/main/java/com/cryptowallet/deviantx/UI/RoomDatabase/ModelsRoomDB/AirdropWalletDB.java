package com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

@Entity(tableName = "airdrop_wallet_coins_table")
public class AirdropWalletDB {
    @PrimaryKey
    @NonNull
    public int id;

    public String airdropWallet;

    public AirdropWalletDB(int id, String airdropWallet) {
        this.id = id;
        this.airdropWallet= airdropWallet;
    }
}
