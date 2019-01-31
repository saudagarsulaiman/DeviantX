package com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.DividendAirdropsDB;
import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.FeaturedAirdropsDB;

@Dao
public interface DividendAirdropsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDividendAirdrops(DividendAirdropsDB dividendAirdropsDB);

    @Query("DELETE FROM dividend_airdrops_table")
    void deleteAllDividendAirdrops();

    @Query("SELECT * from dividend_airdrops_table")
    DividendAirdropsDB getDividendAirdrops();

}
