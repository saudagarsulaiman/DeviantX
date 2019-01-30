package com.cryptowallet.deviantx.UI.RoomDatabase.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;


import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.AirdropWalletDB;

import com.cryptowallet.deviantx.UI.RoomDatabase.Repository.AirdropWalletRepository;

public class AirdropWalletViewModel extends AndroidViewModel {

    private AirdropWalletRepository mRepository;
    private AirdropWalletDB mList;

    public AirdropWalletViewModel(@NonNull Application application) {
        super(application);
        mRepository = new AirdropWalletRepository(application);
        mList = mRepository.getAllAirdropWallet();
    }

    public AirdropWalletDB getAllAirdropWallet() {
        return mList;
    }

    public void insertAirdropWallet(AirdropWalletDB data) {
        mRepository.insertAirdropWallet(data);
    }


}
