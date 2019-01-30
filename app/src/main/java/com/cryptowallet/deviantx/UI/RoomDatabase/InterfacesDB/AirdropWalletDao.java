package com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.AirdropWalletDB;

@Dao
public interface AirdropWalletDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAirdropWallet(AirdropWalletDB airdropWallet);

    @Query("DELETE FROM airdrop_wallet_coins_table")
    void deleteAllAirdropWallet();

    @Query("SELECT * from airdrop_wallet_coins_table")
    AirdropWalletDB getAllAirdropWallet();

}
