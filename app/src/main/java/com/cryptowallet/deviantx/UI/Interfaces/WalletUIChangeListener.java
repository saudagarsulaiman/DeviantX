package com.cryptowallet.deviantx.UI.Interfaces;

public interface WalletUIChangeListener {

    void onWalletUIChanged(String wallets,Boolean isDefaultWallet);
    void onWalletCoinUIChanged(String allCoins);
}
