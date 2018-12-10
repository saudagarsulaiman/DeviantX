package com.cryptowallet.deviantx.UI.Interfaces;

import com.cryptowallet.deviantx.UI.Models.WalletList;

import java.util.ArrayList;

public interface WalletSelectableListener {
    public void WalletSelected(ArrayList<WalletList> allWalletsList, int pos);
}
