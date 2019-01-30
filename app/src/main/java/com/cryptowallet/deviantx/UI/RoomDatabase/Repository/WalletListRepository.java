package com.cryptowallet.deviantx.UI.RoomDatabase.Repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;


import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.WalletListDB;


import com.cryptowallet.deviantx.UI.RoomDatabase.Database.DeviantXDB;
import com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB.WalletListDao;

import java.util.List;

public class WalletListRepository {

    private WalletListDao walletListDao;
    private LiveData<List<WalletListDB>> walletList;

    public WalletListRepository(Application application) {
        DeviantXDB db = DeviantXDB.getDatabase(application);
        walletListDao = db.walletListDao();
        walletList = walletListDao.getAllWalletList();
    }


    public LiveData<List<WalletListDB>> getAllWalletList() {
        return walletList;
    }

    public void insertWalletList(WalletListDB walletListDB) {
        new insertAsyncTask(walletListDao).execute(walletListDB);
    }

    private static class insertAsyncTask extends AsyncTask<WalletListDB, Void, Void> {

        private WalletListDao mAsyncTaskDao;

        insertAsyncTask(WalletListDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final WalletListDB... params) {
            mAsyncTaskDao.insertWalletList(params[0]);
            return null;
        }
    }

}

