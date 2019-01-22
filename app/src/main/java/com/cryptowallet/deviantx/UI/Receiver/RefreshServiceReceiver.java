package com.cryptowallet.deviantx.UI.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.cryptowallet.deviantx.UI.Services.AirdropWalletFetch;
import com.cryptowallet.deviantx.UI.Services.AllCoinsFetch;
import com.cryptowallet.deviantx.UI.Services.WalletDataFetch;

public class RefreshServiceReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Local_cache", "Receiver Load");
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(new Intent(context, WalletDataFetch.class));
                context.startForegroundService(new Intent(context, AllCoinsFetch.class));
                context.startForegroundService(new Intent(context, AirdropWalletFetch.class));
            } else {
                context.startService(new Intent(context, WalletDataFetch.class));
                context.startService(new Intent(context, AllCoinsFetch.class));
                context.startService(new Intent(context, AirdropWalletFetch.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        Intent walletIntent = new Intent(context, WalletDataFetch.class);
//        context.startService(walletIntent);
//        Intent coinsIntent = new Intent(context, AllCoinsFetch.class);
//        context.startService(coinsIntent);
//        Intent airdropIntent = new Intent(context, AirdropWalletFetch.class);
//        context.startService(airdropIntent);
    }
}
