package com.cryptowallet.deviantx.UI.RoomDatabase.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.AccountWallet;
import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.AirdropWallet;
import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.AllCoins;
import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.WalletList;
import com.cryptowallet.deviantx.UI.RoomDatabase.Repository.AccountWalletRepository;

import java.util.List;
import java.util.List;

public class AccountWalletViewModel extends AndroidViewModel {

    private AccountWalletRepository mRepository;
    private LiveData<List<AccountWallet>> mList;

    public AccountWalletViewModel(@NonNull Application application) {
        super(application);
        mRepository = new AccountWalletRepository(application);
        mList = mRepository.getAllAccountWallet();
    }

    public LiveData<List<AccountWallet>> getAllAccountWallet() {
        return mList;
    }

    public void insertAccountWallet(AccountWallet data) {
        mRepository.insertAccountWallet(data);
    }


}
