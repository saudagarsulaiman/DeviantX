package com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "explore_coins_table")
public class ExploreCoins {

    @PrimaryKey
    @NonNull
    public int id;

    public String exploreCoins;

    public ExploreCoins(int id, String exploreCoins) {
        this.id = id;
        this.exploreCoins = exploreCoins;
    }


}
