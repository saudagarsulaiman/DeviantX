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

import com.cryptowallet.deviantx.ServiceAPIs.ExchangePairControllerApi;
import com.cryptowallet.deviantx.UI.Interfaces.PairsListUIListener;
import com.cryptowallet.deviantx.UI.RoomDatabase.Database.DeviantXDB;
import com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB.PairsListDao;
import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.PairsListDB;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.DeviantXApiClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cryptowallet.deviantx.Utilities.MyApplication.myApplication;


public class PairsListFetch extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    SharedPreferences sharedPreferences;
    DeviantXDB deviantXDB;
    PairsListUIListener PairsListUIListener;

    public PairsListFetch() {
        super("PairsListFetch");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public void onCreate() {
        super.onCreate();
        Log.d("Local_cache", "MyIntentService onCreate() method is invoked.");
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

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Local_cache", "MyIntentService onDestroy() method is invoked.");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        sharedPreferences = getApplicationContext().getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        fetchPairsList();
    }

    private void fetchPairsList() {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            ExchangePairControllerApi apiService = DeviantXApiClient.getClient().create(ExchangePairControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.getPairsList(/*CONSTANTS.DeviantMulti + token*/);
            apiResponse.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responsevalue = response.body().string();

                        if (!responsevalue.isEmpty() && responsevalue != null) {
                            deviantXDB = DeviantXDB.getDatabase(getApplicationContext());
                            PairsListDao mDao = deviantXDB.pairsListDao();
                            PairsListDB PairsListDB = new PairsListDB(1, responsevalue);
                            mDao.insertPairsList(PairsListDB);
                            PairsListUIListener = myApplication.getPairsListUIListener();
                            if (PairsListUIListener != null) {
                                PairsListUIListener.onChangedPairsList(responsevalue);
                            }

                        } else {

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
