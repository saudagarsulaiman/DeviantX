package com.cryptowallet.deviantx.UI.RoomDatabase.Repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.AirdropWallet;
import com.cryptowallet.deviantx.UI.RoomDatabase.Database.DeviantXDB;
import com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB.AirdropWalletDao;

import java.util.List;

public class AirdropWalletRepository {

    private AirdropWalletDao airdropWalletDao;
    private AirdropWallet airdropWallet;

    public AirdropWalletRepository(Application application) {
        DeviantXDB db = DeviantXDB.getDatabase(application);
        airdropWalletDao = db.airdropWalletDao();
        airdropWallet = airdropWalletDao.getAllAirdropWallet();
    }


    public AirdropWallet getAllAirdropWallet() {
        return airdropWallet;
    }

    public void insertAirdropWallet(AirdropWallet airdropWallet) {
        new insertAsyncTask(airdropWalletDao).execute(airdropWallet);
    }

    private static class insertAsyncTask extends AsyncTask<AirdropWallet, Void, Void> {

        private AirdropWalletDao mAsyncTaskDao;

        insertAsyncTask(AirdropWalletDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final AirdropWallet... params) {
            mAsyncTaskDao.insertAirdropWallet(params[0]);
            return null;
        }
    }

}
