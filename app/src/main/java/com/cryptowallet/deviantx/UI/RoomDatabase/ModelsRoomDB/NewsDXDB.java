package com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "news_dx_table")
public class NewsDXDB {

    @PrimaryKey
    @NonNull
    public int id;

    public String newsDXDB;

    public NewsDXDB(int id, String newsDXDB) {
        this.id = id;
        this.newsDXDB = newsDXDB;
    }

}
