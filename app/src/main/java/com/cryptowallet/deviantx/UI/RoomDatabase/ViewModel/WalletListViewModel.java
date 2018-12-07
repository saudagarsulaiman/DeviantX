package com.cryptowallet.deviantx.UI.RoomDatabase.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;


import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.AccountWallet;
import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.AirdropWallet;
import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.AllCoins;
import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.WalletList;

import com.cryptowallet.deviantx.UI.RoomDatabase.Repository.WalletListRepository;

import java.util.List;

public class WalletListViewModel extends AndroidViewModel {

    private WalletListRepository mRepository;
    private LiveData<List<WalletList>> mList;

    public WalletListViewModel(@NonNull Application application) {
        super(application);
        mRepository = new WalletListRepository(application);
        mList = mRepository.getAllWalletList();
    }

    public LiveData<List<WalletList>> getAllWalletList() {
        return mList;
    }

    public void insertWalletList(WalletList data) {
        mRepository.insertWalletList(data);
    }


}
