package com.cryptowallet.deviantx.UI.Services;

import android.app.Activity;
import android.app.IntentService;
import android.app.ProgressDialog;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.ServiceAPIs.CryptoControllerApi;
import com.cryptowallet.deviantx.ServiceAPIs.WalletControllerApi;
import com.cryptowallet.deviantx.UI.Interfaces.WalletUIChangeListener;
import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.AllCoins;
import com.cryptowallet.deviantx.UI.RoomDatabase.Database.DeviantXDB;
import com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB.AccountWalletDao;
import com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB.AllCoinsDao;
import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.AccountWallet;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;
import com.cryptowallet.deviantx.Utilities.DeviantXApiClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cryptowallet.deviantx.UI.RoomDatabase.Database.DeviantXDB.INSTANCE;
import static com.cryptowallet.deviantx.Utilities.MyApplication.myApplication;


public class WalletDataFetch extends IntentService {

    SharedPreferences sharedPreferences;
    DeviantXDB deviantXDB;
    public WalletUIChangeListener walletUIChangeListener;

    public WalletDataFetch() {
        super("WalletDataFetch");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public void onCreate() {
        super.onCreate();
        Log.d("Local_cache", "MyIntentService onCreate() method is invoked.");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Local_cache", "MyIntentService onDestroy() method is invoked.");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        sharedPreferences = getApplicationContext().getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        Log.d("Local_cache", "Service Load");
        if (intent.getStringExtra("walletName") != null) {
            boolean coinDataIsRefresh = intent.getBooleanExtra("isRefresh", true);
            fetchWalletCoins(intent.getStringExtra("walletName"), intent.getIntExtra("walletId", 0), coinDataIsRefresh);
        } else
            invokeWallet(intent.getBooleanExtra("walletList", false));

    }


    private void invokeWallet(boolean isWalletOnly) {
        try {
            sharedPreferences = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            WalletControllerApi apiService = DeviantXApiClient.getClient().create(WalletControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.getAllWallet(CONSTANTS.DeviantMulti + token);
            apiResponse.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responsevalue = response.body().string();

                        if (!responsevalue.isEmpty() && responsevalue != null) {
                            deviantXDB = DeviantXDB.getDatabase(getApplicationContext());
                            new WalletDbAsync(deviantXDB, isWalletOnly).execute(responsevalue);

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
        }

    }

    private void fetchWalletCoins(String walletName, int walletId, boolean isRefresh) {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            CryptoControllerApi apiService = DeviantXApiClient.getClient().create(CryptoControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.getAccountWallet(CONSTANTS.DeviantMulti + token, walletName);
            apiResponse.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responsevalue = response.body().string();
                        if (!responsevalue.isEmpty() && responsevalue != null) {
                            deviantXDB = DeviantXDB.getDatabase(getApplicationContext());
                            AllCoinsDao mDao = deviantXDB.allCoinsDao();
                            AllCoins allCoins = new AllCoins(walletName, responsevalue, walletId);
                            mDao.insertAllCoins(allCoins);
                            if (isRefresh) {
                                walletUIChangeListener = myApplication.getWalletUIChangeListener();
                                if (walletUIChangeListener != null) {
                                    walletUIChangeListener.onWalletCoinUIChanged(walletName);
                                }
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

    private RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {
                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                }
            };

    private class WalletDbAsync extends AsyncTask<String, String, String> {

        private final AccountWalletDao mDao;
        private boolean isWallet = false;

        WalletDbAsync(DeviantXDB db, boolean isWalletOnly) {
            mDao = db.accountWalletDao();
            isWallet = isWalletOnly;
        }

        @Override
        protected String doInBackground(final String... params) {
            mDao.deleteAllAccountWallet();
            AccountWallet accountWallet = new AccountWallet(params[0]);
            mDao.insertAccountWallet(accountWallet);
            if (!isWallet)
                onLoadWalletCoins(params[0]);
            return params[0];
        }

        @Override
        protected void onPostExecute(String response) {
            Log.d("Local_cache", "Service Completed");
            walletUIChangeListener = myApplication.getWalletUIChangeListener();
            if (walletUIChangeListener != null) {
                walletUIChangeListener.onWalletUIChanged(response);
            }
        }
    }

    private void onLoadWalletCoins(String walletNameList) {
        try {
            JSONObject jsonObject = new JSONObject(walletNameList);
            String loginResponseStatus = jsonObject.getString("status");
            if (loginResponseStatus.equals("true")) {
                String loginResponseData = jsonObject.getString("data");
                JSONArray jsonArrayData = new JSONArray(loginResponseData);
                for (int i = 0; i < jsonArrayData.length(); i++) {
                    JSONObject jsonObjectData = jsonArrayData.getJSONObject(i);
                    fetchWalletCoins(jsonObjectData.getString("name"), jsonObjectData.getInt("id"), true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
