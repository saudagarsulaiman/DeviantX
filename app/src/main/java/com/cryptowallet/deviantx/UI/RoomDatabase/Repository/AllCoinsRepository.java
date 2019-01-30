package com.cryptowallet.deviantx.UI.RoomDatabase.Repository;

import android.app.Application;
import android.os.AsyncTask;


import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.AllCoinsDB;
import com.cryptowallet.deviantx.UI.RoomDatabase.Database.DeviantXDB;
import com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB.AllCoinsDao;

public class AllCoinsRepository {

    private AllCoinsDao allCoinsDao;
    private AllCoinsDB allCoinsDB;

    public AllCoinsRepository(Application application) {
        DeviantXDB db = DeviantXDB.getDatabase(application);
        allCoinsDao = db.allCoinsDao();
        allCoinsDB = allCoinsDao.getAllAllCoins("");
    }


    public AllCoinsDB getAllAllCoins() {
        return allCoinsDB;
    }

    public void insertAllCoins(AllCoinsDB allCoinsDB) {
        new insertAsyncTask(allCoinsDao).execute(allCoinsDB);
    }

    private static class insertAsyncTask extends AsyncTask<AllCoinsDB, Void, Void> {

        private AllCoinsDao mAsyncTaskDao;

        insertAsyncTask(AllCoinsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final AllCoinsDB... params) {
            mAsyncTaskDao.insertAllCoins(params[0]);
            return null;
        }
    }

}
