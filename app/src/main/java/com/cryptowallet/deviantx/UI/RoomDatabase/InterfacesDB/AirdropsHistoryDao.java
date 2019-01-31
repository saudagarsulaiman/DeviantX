package com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.AirdropsHistoryDB;

@Dao
public interface AirdropsHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAirdropsHistory(AirdropsHistoryDB airdropsHistoryDB);

    @Query("DELETE FROM airdrops_history_table")
    void deleteAllAirdropsHistory();

    @Query("SELECT * from airdrops_history_table")
    AirdropsHistoryDB getAirdropsHistory();

}
