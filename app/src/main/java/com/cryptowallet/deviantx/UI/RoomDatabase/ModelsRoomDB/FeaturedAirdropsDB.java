package com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "featured_airdrops_table")
public class FeaturedAirdropsDB {

    @PrimaryKey
    @NonNull
    public int id;

    public String featuredAirdrops;

    public FeaturedAirdropsDB(int id, String featuredAirdrops) {
        this.id = id;
        this.featuredAirdrops = featuredAirdrops;
    }


}
