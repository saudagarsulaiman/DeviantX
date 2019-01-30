package com.cryptowallet.deviantx.UI.RoomDatabase.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB.ExploreCoinsDao;
import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.AccountWalletDB;
import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.AirdropWalletDB;
import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.AllCoinsDB;
import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.ExploreCoinsDB;
import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.WalletListDB;
import com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB.AccountWalletDao;
import com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB.AirdropWalletDao;
import com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB.AllCoinsDao;
import com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB.WalletListDao;


@Database(entities = {WalletListDB.class, AccountWalletDB.class, AllCoinsDB.class, AirdropWalletDB.class,ExploreCoinsDB.class}, version = 1)
public abstract class DeviantXDB extends RoomDatabase {

    public abstract WalletListDao walletListDao();

    public abstract AccountWalletDao accountWalletDao();

    public abstract AllCoinsDao allCoinsDao();

    public abstract ExploreCoinsDao exploreCoinsDao();

    public abstract AirdropWalletDao airdropWalletDao();

    public static volatile DeviantXDB INSTANCE;

    public static DeviantXDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (DeviantXDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            DeviantXDB.class, "deviantx_database")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
