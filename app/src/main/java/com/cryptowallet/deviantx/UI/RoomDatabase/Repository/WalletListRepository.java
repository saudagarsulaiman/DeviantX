package com.cryptowallet.deviantx.UI.RoomDatabase.Repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;


import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.AccountWallet;
import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.AirdropWallet;
import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.AllCoins;
import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.WalletList;


import com.cryptowallet.deviantx.UI.RoomDatabase.Database.DeviantXDB;
import com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB.AccountWalletDao;
import com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB.WalletListDao;

import java.util.List;
import java.util.List;

public class WalletListRepository {

    private WalletListDao walletListDao;
    private LiveData<List<WalletList>> walletList;

    public WalletListRepository(Application application) {
        DeviantXDB db = DeviantXDB.getDatabase(application);
        walletListDao = db.walletListDao();
        walletList = walletListDao.getAllWalletList();
    }


    public LiveData<List<WalletList>> getAllWalletList() {
        return walletList;
    }

    public void insertWalletList(WalletList walletList) {
        new insertAsyncTask(walletListDao).execute(walletList);
    }

    private static class insertAsyncTask extends AsyncTask<WalletList, Void, Void> {

        private WalletListDao mAsyncTaskDao;

        insertAsyncTask(WalletListDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final WalletList... params) {
            mAsyncTaskDao.insertWalletList(params[0]);
            return null;
        }
    }

}

