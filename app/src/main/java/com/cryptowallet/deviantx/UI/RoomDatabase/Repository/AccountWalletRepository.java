package com.cryptowallet.deviantx.UI.RoomDatabase.Repository;

import android.app.Application;
import android.os.AsyncTask;

import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.AccountWallet;
import com.cryptowallet.deviantx.UI.RoomDatabase.Database.DeviantXDB;
import com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB.AccountWalletDao;

import java.util.List;

public class AccountWalletRepository {

    private AccountWalletDao accountWalletDao;
    private List<AccountWallet> accountWallet;

    public AccountWalletRepository(Application application) {
        DeviantXDB db = DeviantXDB.getDatabase(application);
        accountWalletDao = db.accountWalletDao();
        accountWallet = accountWalletDao.getAllAccountWallet();
    }


    public List<AccountWallet> getAllAccountWallet() {
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
