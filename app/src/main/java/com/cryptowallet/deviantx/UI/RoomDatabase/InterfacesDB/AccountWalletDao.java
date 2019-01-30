package com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.AccountWalletDB;

import java.util.List;

@Dao
public interface AccountWalletDao {

    @Insert
    void insertAccountWallet(AccountWalletDB accountWalletDB);

    @Query("DELETE FROM account_wallet_table")
    void deleteAllAccountWallet();

    @Query("SELECT * from account_wallet_table")
    List<AccountWalletDB> getAllAccountWallet();

}
