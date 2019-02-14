package com.cryptowallet.deviantx.UI.Services;

import android.app.Activity;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.cryptowallet.deviantx.UI.Interfaces.CoinPairsUIListener;
import com.cryptowallet.deviantx.UI.Models.CoinPairs;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.GsonUtils;

import java.util.ArrayList;
import java.util.Arrays;

import rx.functions.Action1;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.client.StompClient;
import ua.naiksoftware.stomp.client.StompMessage;

import static com.cryptowallet.deviantx.Utilities.CONSTANTS.TAG;
import static com.cryptowallet.deviantx.Utilities.MyApplication.myApplication;


public class CoinPairsFetch extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    SharedPreferences sharedPreferences;
    private StompClient stompClient;
    ArrayList<CoinPairs> allCoinPairs;
    CoinPairsUIListener CoinPairsUIListener;

    public CoinPairsFetch() {
        super("CoinPairsFetch");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public void onCreate() {
        super.onCreate();
        Log.d("Local_cache", "MyIntentService onCreate() method is invoked.");
        allCoinPairs = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build();

            startForeground(1, notification);
        }

        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://142.93.51.57:3323/deviant/websocket");
        stompClient.connect();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Local_cache", "MyIntentService onDestroy() method is invoked.");
//        stompClient.disconnect();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        sharedPreferences = getApplicationContext().getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);

        String selectedCoinName = intent.getStringExtra(CONSTANTS.selectedCoinName);
        if (selectedCoinName != null)
            fetchWebsocket(selectedCoinName);
        else
            fetchWebsocket("BTC");
    }

    private void fetchWebsocket(String selectedCoinName) {
        try {
          /*  if (!stompClient.isConnected())
                stompClient.disconnect();
            stompClient.connect();*/
            stompClient.topic("/topic/exchange_pair/" + selectedCoinName).subscribe(new Action1<StompMessage>() {
                @Override
                public void call(StompMessage message) {
                    allCoinPairs = new ArrayList<>();
                    Log.e(TAG, "*****Received " + selectedCoinName + "*****: " + message.getPayload());
                    CoinPairs[] coinsStringArray = GsonUtils.getInstance().fromJson(message.getPayload(), CoinPairs[].class);
                    allCoinPairs = new ArrayList<CoinPairs>(Arrays.asList(coinsStringArray));

//                    Log.e(TAG, "*****allCoinPairs " + allCoinPairs);

                    CoinPairsUIListener = myApplication.getCoinPairsUIListener();
                    if (CoinPairsUIListener != null) {
                        CoinPairsUIListener.onChangedCoinPairs(selectedCoinName, allCoinPairs);
                    }

                }
            });
            stompClient.disconnect();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
