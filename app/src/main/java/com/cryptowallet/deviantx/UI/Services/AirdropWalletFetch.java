package com.cryptowallet.deviantx.UI.Services;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;

import com.cryptowallet.deviantx.ServiceAPIs.AirdropWalletControllerApi;
import com.cryptowallet.deviantx.UI.Interfaces.AirdropWalletUIListener;
import com.cryptowallet.deviantx.UI.RoomDatabase.Database.DeviantXDB;
import com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB.AirdropWalletDao;
import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.AirdropWallet;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.DeviantXApiClient;

import androidx.annotation.Nullable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cryptowallet.deviantx.Utilities.MyApplication.myApplication;


public class AirdropWalletFetch extends IntentService {


    SharedPreferences sharedPreferences;
    AirdropWalletUIListener airdropWalletUIListener;
    DeviantXDB deviantXDB;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
//     * @param name Used to name the worker thread, important only for debugging.
     */
    public AirdropWalletFetch() {
        super("AirdropWalletFetch");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        sharedPreferences = getApplicationContext().getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        fetchAirdropWallet();
    }

    private void fetchAirdropWallet() {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            AirdropWalletControllerApi apiService = DeviantXApiClient.getClient().create(AirdropWalletControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.getAirdropWallet(CONSTANTS.DeviantMulti + token);
            apiResponse.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responsevalue = response.body().string();
                        if (!responsevalue.isEmpty() && responsevalue != null) {
                            deviantXDB = DeviantXDB.getDatabase(getApplicationContext());
                            AirdropWalletDao mDao = deviantXDB.airdropWalletDao();
                            AirdropWallet airdropWallet = new AirdropWallet(1, responsevalue);
                            mDao.insertAirdropWallet(airdropWallet);
                            airdropWalletUIListener= myApplication.getAirdropWalletUIListener();
                            if (airdropWalletUIListener != null) {
                                airdropWalletUIListener.onChangedAirdropWallet(responsevalue);
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
