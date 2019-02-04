package com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.HeaderBannerDB;

@Dao
public interface HeaderBannerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertHeaderBanner(HeaderBannerDB headerBannerDB);

    @Query("DELETE FROM header_banner_table")
    void deleteAllHeaderBanner();

    @Query("SELECT * from header_banner_table")
    HeaderBannerDB getHeaderBanner();

}
