package com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "header_banner_table")
public class HeaderBannerDB {

    @PrimaryKey
    @NonNull
    public int id;

    public String headerBannerDB;

    public HeaderBannerDB(int id, String headerBannerDB) {
        this.id = id;
        this.headerBannerDB = headerBannerDB;
    }

}
