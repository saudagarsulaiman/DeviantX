package com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.AllCoins;

@Dao
public interface AllCoinsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllCoins(AllCoins allCoins);

    @Query("DELETE FROM all_coins_table")
    void deleteAllAllCoins();

    @Query("SELECT * from all_coins_table WHERE walletName=:walletNameValue")
    AllCoins getAllAllCoins(String walletNameValue);

}
