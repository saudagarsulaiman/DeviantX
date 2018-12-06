package com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.cryptowallet.deviantx.UI.Models.AirdropWallet;
import com.cryptowallet.deviantx.UI.Models.AllCoins;
import com.cryptowallet.deviantx.UI.Models.WalletList;

import java.util.ArrayList;

@Dao
public interface AirdropWalletDao {

    @Insert
    void insertAirdropWallet(AirdropWallet airdropWallet);

    @Query("DELETE FROM airdrop_wallet_coins_table")
    void deleteAllAirdropWallet();

    @Query("SELECT * from airdrop_wallet_coins_table ORDER BY str_ad_coin_code ASC")
    LiveData<ArrayList<AirdropWallet>> getAllAirdropWallet();

}
