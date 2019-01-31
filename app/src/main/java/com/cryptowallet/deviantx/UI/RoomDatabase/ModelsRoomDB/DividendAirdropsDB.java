package com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "dividend_airdrops_table")
public class DividendAirdropsDB {

    @PrimaryKey
    @NonNull
    public int id;

    public String diviendAirdrops;

    public DividendAirdropsDB(int id, String diviendAirdrops) {
        this.id = id;
        this.diviendAirdrops = diviendAirdrops;
    }


}
