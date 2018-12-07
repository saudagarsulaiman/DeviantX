package com.cryptowallet.deviantx.UI.RoomDatabase.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.AccountWallet;
import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.AirdropWallet;
import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.AllCoins;
import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.WalletList;
import com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB.AccountWalletDao;
import com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB.AirdropWalletDao;
import com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB.AllCoinsDao;
import com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB.WalletListDao;


@Database(entities = {WalletList.class, AccountWallet.class, AllCoins.class, AirdropWallet.class}, version = 1)
public abstract class DeviantXDB extends RoomDatabase {

    public abstract WalletListDao walletListDao();

    public abstract AccountWalletDao accountWalletDao();

    public abstract AllCoinsDao allCoinsDao();

    public abstract AirdropWalletDao airdropWalletDao();

    private static volatile DeviantXDB INSTANCE;

    public static DeviantXDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (DeviantXDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            DeviantXDB.class, "deviantx_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}