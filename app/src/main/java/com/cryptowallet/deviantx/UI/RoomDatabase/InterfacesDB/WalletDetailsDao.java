package com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.WalletDetailsDB;

import java.util.List;

@Dao
public interface WalletDetailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWalletDetails(WalletDetailsDB walletDetailsDB);

    @Query("DELETE FROM wallet_details_table")
    void deleteAllWalletDetails();

    @Query("SELECT * from wallet_details_table")
    WalletDetailsDB getAllWalletDetails();

}
