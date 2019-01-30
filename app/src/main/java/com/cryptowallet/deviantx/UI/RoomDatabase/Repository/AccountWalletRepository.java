package com.cryptowallet.deviantx.UI.RoomDatabase.Repository;

import android.app.Application;
import android.os.AsyncTask;

import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.AccountWalletDB;
import com.cryptowallet.deviantx.UI.RoomDatabase.Database.DeviantXDB;
import com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB.AccountWalletDao;

import java.util.List;

public class AccountWalletRepository {

    private AccountWalletDao accountWalletDao;
    private List<AccountWalletDB> accountWalletDB;

    public AccountWalletRepository(Application application) {
        DeviantXDB db = DeviantXDB.getDatabase(application);
        accountWalletDao = db.accountWalletDao();
        accountWalletDB = accountWalletDao.getAllAccountWallet();
    }


    public List<AccountWalletDB> getAllAccountWallet() {
        return accountWalletDB;
    }

    public void insertAccountWallet(AccountWalletDB accountWalletDB) {
        new insertAsyncTask(accountWalletDao).execute(accountWalletDB);
    }

    private static class insertAsyncTask extends AsyncTask<AccountWalletDB, Void, Void> {

        private AccountWalletDao mAsyncTaskDao;

        insertAsyncTask(AccountWalletDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final AccountWalletDB... params) {
            mAsyncTaskDao.insertAccountWallet(params[0]);
            return null;
        }
    }

}
