package com.cryptowallet.deviantx.UI.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.cryptowallet.deviantx.UI.Services.AllCoinsFetch;
import com.cryptowallet.deviantx.UI.Services.WalletDataFetch;

public class RefreshServiceReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Local_cache", "Receiver Load");
        Intent walletIntent = new Intent(context, WalletDataFetch.class);
        context.startService(walletIntent);
        Intent coinsIntent = new Intent(context, AllCoinsFetch.class);
        context.startService(coinsIntent);
    }
}
