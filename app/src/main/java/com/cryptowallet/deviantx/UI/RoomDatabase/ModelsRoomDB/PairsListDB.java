package com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "pairs_list_table")
public class PairsListDB {

    @PrimaryKey
    @NonNull
    public int id;

    public String pairsList;

    public PairsListDB(int id, String pairsList) {
        this.id = id;
        this.pairsList = pairsList;
    }


}
