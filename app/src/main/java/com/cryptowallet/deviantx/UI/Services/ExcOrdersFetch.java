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

import com.cryptowallet.deviantx.ServiceAPIs.OrderBookControllerApi;
import com.cryptowallet.deviantx.UI.Interfaces.ExcOrdersUIListener;
import com.cryptowallet.deviantx.UI.RoomDatabase.Database.DeviantXDB;
import com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB.ExcOrdersDao;
import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.ExcOrdersDB;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.DeviantXApiClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cryptowallet.deviantx.Utilities.MyApplication.myApplication;


public class ExcOrdersFetch extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    SharedPreferences sharedPreferences;
    ExcOrdersUIListener excOrdersUIListener;
    DeviantXDB deviantXDB;

    public ExcOrdersFetch() {
        super("ExcOrdersFetch");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public void onCreate() {
        super.onCreate();
        Log.d("Local_cache", "MyIntentService onCreate() method is invoked.");
        /*int NOTIFICATION_ID = (int) (System.currentTimeMillis() % 10000);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startForeground(NOTIFICATION_ID, new Notification.Builder(this).build());
            startForeground(1,new Notification());
        }*/
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
        fetchExcOrders();
    }

    private void fetchExcOrders() {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            OrderBookControllerApi apiService = DeviantXApiClient.getClient().create(OrderBookControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.getAllOpen(CONSTANTS.DeviantMulti + token);
            apiResponse.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responsevalue = response.body().string();

                        if (!responsevalue.isEmpty() && responsevalue != null) {
                            deviantXDB = DeviantXDB.getDatabase(getApplicationContext());
                            ExcOrdersDao mDao = deviantXDB.excOrdersDao();
                            ExcOrdersDB excOrdersDB = new ExcOrdersDB(1, responsevalue);
                            mDao.insertExcOrders(excOrdersDB);
                            excOrdersUIListener = myApplication.getExcOrdersUIListener();
                            if (excOrdersUIListener != null) {
                                excOrdersUIListener.onChangedExcOrders(responsevalue);
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
