package com.cryptowallet.deviantx.UI.RoomDatabase.Repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.cryptowallet.deviantx.UI.Models.AccountWallet;
import com.cryptowallet.deviantx.UI.RoomDatabase.Database.DeviantXDB;
import com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB.AccountWalletDao;

import java.util.ArrayList;

public class AccountWalletRepository {

    private AccountWalletDao accountWalletDao;
    private LiveData<ArrayList<AccountWallet>> accountWallet;

    public AccountWalletRepository(Application application) {
        DeviantXDB db = DeviantXDB.getDatabase(application);
        accountWalletDao = db.accountWalletDao();
        accountWallet = accountWalletDao.getAllAccountWallet();
    }


    public LiveData<ArrayList<AccountWallet>> getAllAccountWallet() {
        return accountWallet;
    }

    public void insertAccountWallet(AccountWallet accountWallet) {
        new insertAsyncTask(accountWalletDao).execute(accountWallet);
    }

    private static class insertAsyncTask extends AsyncTask<AccountWallet, Void, Void> {

        private AccountWalletDao mAsyncTaskDao;

        insertAsyncTask(AccountWalletDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final AccountWallet... params) {
            mAsyncTaskDao.insertAccountWallet(params[0]);
            return null;
        }
    }

}
