package com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.AirdropWallet;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface AirdropWalletDao {

    @Insert
    void insertAirdropWallet(AirdropWallet airdropWallet);

    @Query("DELETE FROM airdrop_wallet_coins_table")
    void deleteAllAirdropWallet();

    @Query("SELECT * from airdrop_wallet_coins_table ORDER BY str_ad_coin_code ASC")
    LiveData<List<AirdropWallet>> getAllAirdropWallet();

}
