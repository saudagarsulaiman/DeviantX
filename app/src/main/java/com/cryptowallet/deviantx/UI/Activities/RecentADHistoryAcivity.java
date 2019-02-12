package com.cryptowallet.deviantx.UI.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.ServiceAPIs.UserAirdropControllerApi;
import com.cryptowallet.deviantx.UI.Adapters.RecentADHistoryRAdapter;
import com.cryptowallet.deviantx.UI.Interfaces.AirdropsHistoryUIListener;
import com.cryptowallet.deviantx.UI.Models.AirdropsHistory;
import com.cryptowallet.deviantx.UI.RoomDatabase.Database.DeviantXDB;
import com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB.AirdropsHistoryDao;
import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.AirdropsHistoryDB;
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

public class RecentADHistoryAcivity extends AppCompatActivity {

    @BindView(R.id.toolbar_center_back)
    Toolbar toolbar_center_back;
    @BindView(R.id.edt_search)
    EditText edt_search;
    @BindView(R.id.img_filter)
    ImageView img_filter;
    @BindView(R.id.rview_radh_coins)
    RecyclerView rview_radh_coins;
    @BindView(R.id.lnr_search)
    LinearLayout lnr_search;
    @BindView(R.id.lnr_empty_history)
    LinearLayout lnr_empty_history;

    RecentADHistoryRAdapter recentADHistoryRAdapter;
    LinearLayoutManager layoutManagerVertical;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;

    DeviantXDB deviantXDB;


    String loginResponseData, loginResponseStatus, loginResponseMsg;
    ArrayList<AirdropsHistory> allAirdropsHistory;

    @Override
    protected void onResume() {
        super.onResume();
        myApplication.disableScreenCapture(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_adhistory);

        ButterKnife.bind(this);

        deviantXDB = DeviantXDB.getDatabase(this);
        sharedPreferences = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        allAirdropsHistory = new ArrayList<>();
        layoutManagerVertical = new LinearLayoutManager(RecentADHistoryAcivity.this, LinearLayoutManager.VERTICAL, false);
        rview_radh_coins.setLayoutManager(layoutManagerVertical);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                onLoadAirdropsHistory();
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
                ArrayList<AirdropsHistory> searchCoinsList = new ArrayList<>();
                for (AirdropsHistory coinName : allAirdropsHistory) {
                    if (coinName.getStr_to().toLowerCase().contains(s.toString().toLowerCase())) {
                        searchCoinsList.add(coinName);
                    }
                }
                recentADHistoryRAdapter = new RecentADHistoryRAdapter(RecentADHistoryAcivity.this, searchCoinsList, true);
                rview_radh_coins.setAdapter(recentADHistoryRAdapter);
            }
        });

    }



    //    **************GETTING AIRDROPS HISTORY**************
    private void onLoadAirdropsHistory() {

     runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AirdropsHistoryDao airdropsHistoryDao = deviantXDB.airdropsHistoryDao();
                if ((airdropsHistoryDao.getAirdropsHistory()) != null) {
                    String walletResult = airdropsHistoryDao.getAirdropsHistory().airdropsHistory;
                    updateUIAirdropsHistory(walletResult);
                } else {
                    if (CommonUtilities.isConnectionAvailable(RecentADHistoryAcivity.this)) {
                        fetchCoinsAirdropsHistory();
                    } else {
                        CommonUtilities.ShowToastMessage(RecentADHistoryAcivity.this, getResources().getString(R.string.internetconnection));
                    }
                }
            }
        });

    }

    AirdropsHistoryUIListener airdropsHistoryUIListener = new AirdropsHistoryUIListener() {
        @Override
        public void onChangedAirdropsHistory(String allAirdropsHistory) {
            updateUIAirdropsHistory(allAirdropsHistory);
        }

    };

    private void updateUIAirdropsHistory(String responsevalue) {
        RecentADHistoryAcivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject(responsevalue);
                    loginResponseMsg = jsonObject.getString("msg");
                    loginResponseStatus = jsonObject.getString("status");

                    if (loginResponseStatus.equals("true")) {
                        loginResponseData = jsonObject.getString("data");
                        AirdropsHistory[] coinsStringArray = GsonUtils.getInstance().fromJson(loginResponseData, AirdropsHistory[].class);
                        allAirdropsHistory = new ArrayList<AirdropsHistory>(Arrays.asList(coinsStringArray));

                        ArrayList<AirdropsHistory> airdropsHistoryList = new ArrayList<>();
                        for (AirdropsHistory coinName : allAirdropsHistory) {
                            airdropsHistoryList.add(coinName);
                        }
                        if (airdropsHistoryList.size() > 0) {
                            lnr_empty_history.setVisibility(View.GONE);
                            recentADHistoryRAdapter = new RecentADHistoryRAdapter(RecentADHistoryAcivity.this, airdropsHistoryList, true);
                            rview_radh_coins.setAdapter(recentADHistoryRAdapter);
                        } else {
                            lnr_empty_history.setVisibility(View.VISIBLE);
                        }
                    } else {
                        CommonUtilities.ShowToastMessage(RecentADHistoryAcivity.this, loginResponseMsg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void fetchCoinsAirdropsHistory() {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
//            progressDialog = ProgressDialog.show(RecentADHistoryAcivity.this, "", getResources().getString(R.string.please_wait), true);
            UserAirdropControllerApi apiService = DeviantXApiClient.getClient().create(UserAirdropControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.getADHistory(CONSTANTS.DeviantMulti + token);
            apiResponse.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responsevalue = response.body().string();
//                        progressDialog.dismiss();

                        if (!responsevalue.isEmpty() && responsevalue != null) {
                            updateUIAirdropsHistory(responsevalue);
//                            progressDialog.dismiss();
                            AirdropsHistoryDao mDao = deviantXDB.airdropsHistoryDao();
                            AirdropsHistoryDB airdropsHistoryDB = new AirdropsHistoryDB(1, responsevalue);
                            mDao.insertAirdropsHistory(airdropsHistoryDB);

                        } else {
                            CommonUtilities.ShowToastMessage(RecentADHistoryAcivity.this, loginResponseMsg);
//                            Toast.makeText(getApplicationContext(), responsevalue, Toast.LENGTH_LONG).show();
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
//                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(RecentADHistoryAcivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
//                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(RecentADHistoryAcivity.this, getResources().getString(R.string.Timeout));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Timeout), Toast.LENGTH_SHORT).show();
                    } else if (t instanceof java.net.ConnectException) {
//                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(RecentADHistoryAcivity.this, getResources().getString(R.string.networkerror));
                    } else {
//                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(RecentADHistoryAcivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception ex) {
//            progressDialog.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(RecentADHistoryAcivity.this, getResources().getString(R.string.errortxt));
//            Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
        }

    }



}
