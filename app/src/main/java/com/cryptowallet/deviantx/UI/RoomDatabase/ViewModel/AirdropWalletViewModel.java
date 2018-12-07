package com.cryptowallet.deviantx.UI.RoomDatabase.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;


import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.AccountWallet;
import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.AirdropWallet;
import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.AllCoins;
import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.WalletList;

import com.cryptowallet.deviantx.UI.RoomDatabase.Repository.AirdropWalletRepository;

import java.util.List;

public class AirdropWalletViewModel extends AndroidViewModel {

    private AirdropWalletRepository mRepository;
    private LiveData<List<AirdropWallet>> mList;

    public AirdropWalletViewModel(@NonNull Application application) {
        super(application);
        mRepository = new AirdropWalletRepository(application);
        mList = mRepository.getAllAirdropWallet();
    }

    public LiveData<List<AirdropWallet>> getAllAirdropWallet() {
        return mList;
    }

    public void insertAirdropWallet(AirdropWallet data) {
        mRepository.insertAirdropWallet(data);
    }


}
