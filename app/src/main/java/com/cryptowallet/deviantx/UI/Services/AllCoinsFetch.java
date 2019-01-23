package com.cryptowallet.deviantx.UI.Services;

import android.app.Activity;
import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import com.cryptowallet.deviantx.ServiceAPIs.CoinsControllerApi;
import com.cryptowallet.deviantx.UI.Interfaces.AllCoinsUIListener;
import com.cryptowallet.deviantx.UI.RoomDatabase.Database.DeviantXDB;
import com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB.ExploreCoinsDao;
import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.ExploreCoins;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.DeviantXApiClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cryptowallet.deviantx.Utilities.MyApplication.myApplication;


public class AllCoinsFetch extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    SharedPreferences sharedPreferences;
    AllCoinsUIListener allCoinsUIListener;
    DeviantXDB deviantXDB;

    public AllCoinsFetch() {
        super("AllCoinsFetch");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public void onCreate() {
        super.onCreate();
        Log.d("Local_cache", "MyIntentService onCreate() method is invoked.");
        int NOTIFICATION_ID = (int) (System.currentTimeMillis() % 10000);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(NOTIFICATION_ID, new Notification.Builder(this).build());
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
        fetchCoins();
    }

    private void fetchCoins() {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            CoinsControllerApi apiService = DeviantXApiClient.getClient().create(CoinsControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.getAllCoins(CONSTANTS.DeviantMulti + token);
            apiResponse.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responsevalue = response.body().string();

                        if (!responsevalue.isEmpty() && responsevalue != null) {
                            deviantXDB = DeviantXDB.getDatabase(getApplicationContext());
                            ExploreCoinsDao mDao = deviantXDB.exploreCoinsDao();
                            ExploreCoins exploreCoins = new ExploreCoins(1, responsevalue);
                            mDao.insertAllCoins(exploreCoins);
                            allCoinsUIListener = myApplication.getAllCoinsUIListener();
                            if (allCoinsUIListener != null) {
                                allCoinsUIListener.onChangedAllCoins(responsevalue);
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
