package com.cryptowallet.deviantx.UI.RoomDatabase.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.cryptowallet.deviantx.UI.Models.AccountWallet;
import com.cryptowallet.deviantx.UI.RoomDatabase.Repository.AccountWalletRepository;

import java.util.ArrayList;

public class AccountWalletViewModel extends AndroidViewModel {

    private AccountWalletRepository mRepository;
    private LiveData<ArrayList<AccountWallet>> mList;

    public AccountWalletViewModel(@NonNull Application application) {
        super(application);
        mRepository = new AccountWalletRepository(application);
        mList = mRepository.getAllAccountWallet();
    }

    public LiveData<ArrayList<AccountWallet>> getAllAccountWallet() {
        return mList;
    }

    public void insertAccountWallet(AccountWallet data) {
        mRepository.insertAccountWallet(data);
    }


}
