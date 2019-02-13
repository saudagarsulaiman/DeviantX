package com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.PairsListDB;

@Dao
public interface PairsListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPairsList(PairsListDB PairsListDB);

    @Query("DELETE FROM pairs_list_table")
    void deleteAllPairsList();

    @Query("SELECT * from pairs_list_table")
    PairsListDB getAllPairsList();

}
