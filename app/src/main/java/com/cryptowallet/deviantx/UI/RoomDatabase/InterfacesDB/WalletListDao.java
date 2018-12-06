package com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.cryptowallet.deviantx.UI.Models.WalletList;

import java.lang.reflect.Array;
import java.util.ArrayList;

@Dao
public interface WalletListDao {

    @Insert
    void insertWalletList(WalletList walletList);

    @Query("DELETE FROM wallet_list_table")
    void deleteAllWalletList();

    @Query("SELECT * from wallet_list_table ORDER BY str_data_name ASC")
    LiveData<ArrayList<WalletList>> getAllWalletList();

}
