package com.cryptowallet.deviantx.UI.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.ServiceAPIs.CoinsControllerApi;
import com.cryptowallet.deviantx.ServiceAPIs.CryptoControllerApi;
import com.cryptowallet.deviantx.ServiceAPIs.UserAirdropControllerApi;
import com.cryptowallet.deviantx.UI.Adapters.DividendADHorizantalRAdapter;
import com.cryptowallet.deviantx.UI.Adapters.DividendADVerticalRAdapter;
import com.cryptowallet.deviantx.UI.Interfaces.CoinSelectableListener;
import com.cryptowallet.deviantx.UI.Interfaces.DividendAirdropsUIListener;
import com.cryptowallet.deviantx.UI.Models.AllCoins;
import com.cryptowallet.deviantx.UI.Models.DividendAirdrops;
import com.cryptowallet.deviantx.UI.RoomDatabase.Database.DeviantXDB;
import com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB.DividendAirdropsDao;
import com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB.ExploreCoinsDao;
import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.DividendAirdropsDB;
import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.ExploreCoinsDB;
import com.cryptowallet.deviantx.UI.Services.WalletDataFetch;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;
import com.cryptowallet.deviantx.Utilities.DeviantXApiClient;
import com.cryptowallet.deviantx.Utilities.GsonUtils;

import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cryptowallet.deviantx.Utilities.MyApplication.myApplication;

public class DividendADListActivity extends AppCompatActivity {


    @BindView(R.id.toolbar_center_back)
    Toolbar toolbar_center_back;
    @BindView(R.id.lnr_search)
    LinearLayout lnr_search;
    @BindView(R.id.rview_div_coins_list)
    RecyclerView rview_div_coins_list;
    @BindView(R.id.edt_search)
    EditText edt_search;
    @BindView(R.id.lnr_empty_div_coins)
    LinearLayout lnr_empty_div_coins;


    DividendADVerticalRAdapter dividendADVerticalRAdapter;
    GridLayoutManager layoutManager;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;
    DeviantXDB deviantXDB;


    String loginResponseData, loginResponseStatus, loginResponseMsg;
    ArrayList<DividendAirdrops> allDividendAirdrops;

    @Override
    protected void onResume() {
        super.onResume();
        myApplication.disableScreenCapture(this);
//        CommonUtilities.serviceStart(DividendADListActivity.this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dividend_ad_list);

        ButterKnife.bind(this);
        deviantXDB = DeviantXDB.getDatabase(this);
        sharedPreferences = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        allDividendAirdrops = new ArrayList<>();

        layoutManager = new GridLayoutManager(DividendADListActivity.this, 2, GridLayoutManager.VERTICAL, false);
        rview_div_coins_list.setLayoutManager(layoutManager);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                onLoadDividendAirdrops();
            }
        }, 100);


        toolbar_center_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ArrayList<DividendAirdrops> searchCoinsList = new ArrayList<>();
                for (DividendAirdrops coinName : allDividendAirdrops) {
                    if (coinName.getStr_coinName().toLowerCase().contains(s.toString().toLowerCase())) {
                        searchCoinsList.add(coinName);
                    }
                }
                dividendADVerticalRAdapter = new DividendADVerticalRAdapter(DividendADListActivity.this, searchCoinsList, true);
                rview_div_coins_list.setAdapter(dividendADVerticalRAdapter);
            }
        });
        
    }


    //    **************GETTING DIVIDEND AIRDROPS**************
    private void onLoadDividendAirdrops() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DividendAirdropsDao dividendAirdropsDao = deviantXDB.dividendAirdropsDao();
                if ((dividendAirdropsDao.getDividendAirdrops()) != null) {
                    String walletResult = dividendAirdropsDao.getDividendAirdrops().diviendAirdrops;
                    updateUIDividendAirdrops(walletResult);
                } else {
                    if (CommonUtilities.isConnectionAvailable(DividendADListActivity.this)) {
                        fetchCoinsDividendAirdrops();
                    } else {
                        CommonUtilities.ShowToastMessage(DividendADListActivity.this, getResources().getString(R.string.internetconnection));
                    }
                }
            }
        });

    }

    DividendAirdropsUIListener dividendAirdropsUIListener = new DividendAirdropsUIListener() {
        @Override
        public void onChangedDividendAirdrops(String allDividendAirdrops) {
            updateUIDividendAirdrops(allDividendAirdrops);
        }

    };

    private void updateUIDividendAirdrops(String responsevalue) {
        DividendADListActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject(responsevalue);
                    loginResponseMsg = jsonObject.getString("msg");
                    loginResponseStatus = jsonObject.getString("status");

                    if (loginResponseStatus.equals("true")) {
                        loginResponseData = jsonObject.getString("data");
                        DividendAirdrops[] coinsStringArray = GsonUtils.getInstance().fromJson(loginResponseData, DividendAirdrops[].class);
                        allDividendAirdrops = new ArrayList<DividendAirdrops>(Arrays.asList(coinsStringArray));

                        ArrayList<DividendAirdrops> dividendCoinsList = new ArrayList<>();
                        for (DividendAirdrops coinName : allDividendAirdrops) {
                            dividendCoinsList.add(coinName);
                        }
                        if (dividendCoinsList.size() > 0) {
                            lnr_empty_div_coins.setVisibility(View.GONE);
                            dividendADVerticalRAdapter= new DividendADVerticalRAdapter(DividendADListActivity.this, dividendCoinsList, false);
                            rview_div_coins_list.setAdapter(dividendADVerticalRAdapter);
                        } else {
                            lnr_empty_div_coins.setVisibility(View.VISIBLE);
                        }
                    } else {
                        CommonUtilities.ShowToastMessage(DividendADListActivity.this, loginResponseMsg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void fetchCoinsDividendAirdrops() {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
//            progressDialog = ProgressDialog.show(DividendADListActivity.this, "", getResources().getString(R.string.please_wait), true);
            UserAirdropControllerApi apiService = DeviantXApiClient.getClient().create(UserAirdropControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.getClaimADAmount(CONSTANTS.DeviantMulti + token);
            apiResponse.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responsevalue = response.body().string();
//                        progressDialog.dismiss();

                        if (!responsevalue.isEmpty() && responsevalue != null) {
                            updateUIDividendAirdrops(responsevalue);
//                            progressDialog.dismiss();
                            DividendAirdropsDao mDao = deviantXDB.dividendAirdropsDao();
                            DividendAirdropsDB dividendAirdropsDB = new DividendAirdropsDB(1, responsevalue);
                            mDao.insertDividendAirdrops(dividendAirdropsDB);
                        } else {
                            CommonUtilities.ShowToastMessage(DividendADListActivity.this, loginResponseMsg);
//                            Toast.makeText(getApplicationContext(), responsevalue, Toast.LENGTH_LONG).show();
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
//                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(DividendADListActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
//                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(DividendADListActivity.this, getResources().getString(R.string.Timeout));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Timeout), Toast.LENGTH_SHORT).show();
                    } else if (t instanceof java.net.ConnectException) {
//                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(DividendADListActivity.this, getResources().getString(R.string.networkerror));
                    } else {
//                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(DividendADListActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception ex) {
//            progressDialog.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(DividendADListActivity.this, getResources().getString(R.string.errortxt));
//            Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
        }

    }


}
