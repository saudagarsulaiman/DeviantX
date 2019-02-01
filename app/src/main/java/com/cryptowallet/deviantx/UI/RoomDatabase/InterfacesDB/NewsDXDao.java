package com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.NewsDXDB;

@Dao
public interface NewsDXDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNewsDX(NewsDXDB newsDXDB);

    @Query("DELETE FROM news_dx_table")
    void deleteAllNewsDX();

    @Query("SELECT * from news_dx_table")
    NewsDXDB getNewsDX();

}
