package com.cryptowallet.deviantx.UI.RoomDatabase.Repository;

import android.app.Application;
import android.os.AsyncTask;

import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.AirdropWalletDB;
import com.cryptowallet.deviantx.UI.RoomDatabase.Database.DeviantXDB;
import com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB.AirdropWalletDao;

public class AirdropWalletRepository {

    private AirdropWalletDao airdropWalletDao;
    private AirdropWalletDB airdropWallet;

    public AirdropWalletRepository(Application application) {
        DeviantXDB db = DeviantXDB.getDatabase(application);
        airdropWalletDao = db.airdropWalletDao();
        airdropWallet = airdropWalletDao.getAllAirdropWallet();
    }


    public AirdropWalletDB getAllAirdropWallet() {
        return airdropWallet;
    }

    public void insertAirdropWallet(AirdropWalletDB airdropWallet) {
        new insertAsyncTask(airdropWalletDao).execute(airdropWallet);
    }

    private static class insertAsyncTask extends AsyncTask<AirdropWalletDB, Void, Void> {

        private AirdropWalletDao mAsyncTaskDao;

        insertAsyncTask(AirdropWalletDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final AirdropWalletDB... params) {
            mAsyncTaskDao.insertAirdropWallet(params[0]);
            return null;
        }
    }

}
