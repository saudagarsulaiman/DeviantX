package com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.cryptowallet.deviantx.UI.Models.AccountWallet;
import com.cryptowallet.deviantx.UI.Models.WalletList;

import java.util.ArrayList;

@Dao
public interface AccountWalletDao {

    @Insert
    void insertAccountWallet(AccountWallet accountWallet);

    @Query("DELETE FROM account_wallet_table")
    void deleteAllAccountWallet();

    @Query("SELECT * from account_wallet_table ORDER BY str_data_walletName ASC")
    LiveData<ArrayList<AccountWallet>> getAllAccountWallet();

}
