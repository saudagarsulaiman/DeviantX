package com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.WalletListDB;

import java.util.List;

@Dao
public interface WalletListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWalletList(WalletListDB walletListDB);

    @Query("DELETE FROM wallet_list_table")
    void deleteAllWalletList();

    @Query("SELECT * from wallet_list_table ORDER BY str_data_name ASC")
    LiveData<List<WalletListDB>> getAllWalletList();

}
