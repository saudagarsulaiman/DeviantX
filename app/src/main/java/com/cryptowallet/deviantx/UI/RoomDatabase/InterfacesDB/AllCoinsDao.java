package com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.cryptowallet.deviantx.UI.Models.AccountWallet;
import com.cryptowallet.deviantx.UI.Models.AllCoins;
import com.cryptowallet.deviantx.UI.Models.WalletList;

import java.util.ArrayList;

@Dao
public interface AllCoinsDao {

    @Insert
    void insertAllCoins(AllCoins allCoins);

    @Query("DELETE FROM all_coins_table")
    void deleteAllAllCoins();

    @Query("SELECT * from all_coins_table ORDER BY str_coin_code ASC")
    LiveData<ArrayList<AllCoins>> getAllAllCoins();

}
