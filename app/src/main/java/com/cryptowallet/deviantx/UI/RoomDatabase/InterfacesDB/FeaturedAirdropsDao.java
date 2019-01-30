package com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.FeaturedAirdropsDB;

@Dao
public interface FeaturedAirdropsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFeaturedAirdrops(FeaturedAirdropsDB featuredAirdropsDB);

    @Query("DELETE FROM featured_airdrops_table")
    void deleteAllFeaturedAirdrops();

    @Query("SELECT * from featured_airdrops_table")
    FeaturedAirdropsDB getFeaturedAirdrops();

}
