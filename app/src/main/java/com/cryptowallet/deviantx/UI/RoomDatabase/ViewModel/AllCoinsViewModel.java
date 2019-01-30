package com.cryptowallet.deviantx.UI.RoomDatabase.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;


import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.AllCoinsDB;

import com.cryptowallet.deviantx.UI.RoomDatabase.Repository.AllCoinsRepository;

public class AllCoinsViewModel extends AndroidViewModel {

    private AllCoinsRepository mRepository;
    private AllCoinsDB mList;

    public AllCoinsViewModel(@NonNull Application application) {
        super(application);
        mRepository = new AllCoinsRepository(application);
        mList = mRepository.getAllAllCoins();
    }

    public AllCoinsDB getAllAllCoins() {
        return mList;
    }

    public void insertAllCoins(AllCoinsDB data) {
        mRepository.insertAllCoins(data);
    }


}
