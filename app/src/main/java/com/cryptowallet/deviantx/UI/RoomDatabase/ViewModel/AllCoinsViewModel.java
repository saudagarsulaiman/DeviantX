package com.cryptowallet.deviantx.UI.RoomDatabase.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;


import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.AccountWallet;
import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.AirdropWallet;
import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.AllCoins;
import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.WalletList;

import com.cryptowallet.deviantx.UI.RoomDatabase.Repository.AllCoinsRepository;

import java.util.List;

public class AllCoinsViewModel extends AndroidViewModel {

    private AllCoinsRepository mRepository;
    private LiveData<List<AllCoins>> mList;

    public AllCoinsViewModel(@NonNull Application application) {
        super(application);
        mRepository = new AllCoinsRepository(application);
        mList = mRepository.getAllAllCoins();
    }

    public LiveData<List<AllCoins>> getAllAllCoins() {
        return mList;
    }

    public void insertAllCoins(AllCoins data) {
        mRepository.insertAllCoins(data);
    }


}
