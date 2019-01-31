package com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "airdrops_history_table")
public class AirdropsHistoryDB {

    @PrimaryKey
    @NonNull
    public int id;

    public String airdropsHistory;

    public AirdropsHistoryDB(int id, String airdropsHistory) {
        this.id = id;
        this.airdropsHistory = airdropsHistory;
    }


}
