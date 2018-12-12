package com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.ExploreCoins;

@Dao
public interface ExploreCoinsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllCoins(ExploreCoins exploreCoins);

    @Query("DELETE FROM explore_coins_table")
    void deleteAllAllCoins();

    @Query("SELECT * from explore_coins_table")
    ExploreCoins getExploreCoins();
}
