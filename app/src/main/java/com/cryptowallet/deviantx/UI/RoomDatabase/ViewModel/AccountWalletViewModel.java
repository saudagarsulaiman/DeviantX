package com.cryptowallet.deviantx.UI.RoomDatabase.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.AccountWalletDB;
import com.cryptowallet.deviantx.UI.RoomDatabase.Repository.AccountWalletRepository;

import java.util.List;

public class AccountWalletViewModel extends AndroidViewModel {

    private AccountWalletRepository mRepository;
    private List<AccountWalletDB> mList;

    public AccountWalletViewModel(@NonNull Application application) {
        super(application);
        mRepository = new AccountWalletRepository(application);
        mList = mRepository.getAllAccountWallet();
    }

    public List<AccountWalletDB> getAllAccountWallet() {
        return mList;
    }

    public void insertAccountWallet(AccountWalletDB data) {
        mRepository.insertAccountWallet(data);
    }


}
