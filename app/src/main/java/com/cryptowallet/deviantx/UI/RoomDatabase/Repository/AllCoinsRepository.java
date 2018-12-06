package com.cryptowallet.deviantx.UI.RoomDatabase.Repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.cryptowallet.deviantx.UI.Models.AllCoins;
import com.cryptowallet.deviantx.UI.RoomDatabase.Database.DeviantXDB;
import com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB.AllCoinsDao;

import java.util.ArrayList;

public class AllCoinsRepository {

    private AllCoinsDao allCoinsDao;
    private LiveData<ArrayList<AllCoins>> allCoins;

    public AllCoinsRepository(Application application) {
        DeviantXDB db = DeviantXDB.getDatabase(application);
        allCoinsDao = db.allCoinsDao();
        allCoins = allCoinsDao.getAllAllCoins();
    }


    public LiveData<ArrayList<AllCoins>> getAllAllCoins() {
        return allCoins;
    }

    public void insertAllCoins(AllCoins allCoins) {
        new insertAsyncTask(allCoinsDao).execute(allCoins);
    }

    private static class insertAsyncTask extends AsyncTask<AllCoins, Void, Void> {

        private AllCoinsDao mAsyncTaskDao;

        insertAsyncTask(AllCoinsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final AllCoins... params) {
            mAsyncTaskDao.insertAllCoins(params[0]);
            return null;
        }
    }

}
