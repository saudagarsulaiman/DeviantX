package com.cryptowallet.deviantx.UI.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.ServiceAPIs.CoinsControllerApi;
import com.cryptowallet.deviantx.ServiceAPIs.CryptoControllerApi;
import com.cryptowallet.deviantx.UI.Adapters.AddCoinsRAdapter;
import com.cryptowallet.deviantx.UI.Models.AllCoins;
import com.cryptowallet.deviantx.UI.Interfaces.CoinSelectableListener;
import com.cryptowallet.deviantx.UI.RoomDatabase.Database.DeviantXDB;
import com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB.ExploreCoinsDao;
import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.ExploreCoins;
import com.cryptowallet.deviantx.UI.Services.WalletDataFetch;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;
import com.cryptowallet.deviantx.Utilities.DeviantXApiClient;
import com.cryptowallet.deviantx.Utilities.GsonUtils;

import org.json.JSONArray;
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

public class AddCoinsActivity extends AppCompatActivity {

    @BindView(R.id.tool)
    Toolbar tool;
    @BindView(R.id.lnr_search)
    LinearLayout lnr_search;
    @BindView(R.id.rview_coins_list)
    RecyclerView rview_coins_list;
    @BindView(R.id.btn_ready)
    Button btn_ready;
    @BindView(R.id.edt_search)
    EditText edt_search;

    AddCoinsRAdapter addCoinsRAdapter;
    GridLayoutManager layoutManager;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;

    int int_data_id, int_coin_rank;
    Double dbl_coin_usdValue, dbl_coin_marketCap, dbl_coin_volume, dbl_coin_24h, dbl_coin_7d, dbl_coin_1m;
    ;

    String loginResponseData, loginResponseStatus, loginResponseMsg,
            str_coin_name, str_coin_code, str_coin_logo;

    ArrayList<AllCoins> allCoinsList;
    CoinSelectableListener coinSelectableListener;
    int selectedCoinId = 0;
    DeviantXDB deviantXDB;

    @Override
    protected void onResume() {
        super.onResume();
        myApplication.disableScreenCapture(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_coins);

        ButterKnife.bind(this);
        deviantXDB = DeviantXDB.getDatabase(this);
        sharedPreferences = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        allCoinsList = new ArrayList<>();

        layoutManager = new GridLayoutManager(AddCoinsActivity.this, 2, GridLayoutManager.VERTICAL, false);
        rview_coins_list.setLayoutManager(layoutManager);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                onLoadAllCoins();
            }
        }, 100);


        tool.setOnClickListener(new View.OnClickListener() {
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
                ArrayList<AllCoins> searchCoinsList = new ArrayList<>();
                for (AllCoins coinName : allCoinsList) {
                    if (coinName.getStr_coin_name().toLowerCase().contains(s.toString().toLowerCase())) {
                        if (coinName.getInt_coin_id() == selectedCoinId)
                            coinName.setSelected(true);
                        else
                            coinName.setSelected(false);
                        searchCoinsList.add(coinName);
                    }
                }
                addCoinsRAdapter = new AddCoinsRAdapter(AddCoinsActivity.this, searchCoinsList, coinSelectableListener);
                rview_coins_list.setAdapter(addCoinsRAdapter);
                addCoinsRAdapter.notifyDataSetChanged();
            }
        });


        coinSelectableListener = new CoinSelectableListener() {
            @Override
            public void CoinSelected(ArrayList<AllCoins> selected_allCoinsList, int pos) {
                // btn_ready.setVisibility(View.GONE);
                // allCoinsList = selected_allCoinsList;
//                                        for (final AllCoins coins : allCoinsList) {
//                                            if (coins.getSelected()) {
//                                                btn_ready.setVisibility(View.VISIBLE);
//                                                btn_ready.setOnClickListener(new View.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(View v) {
//                                                        if (CommonUtilities.isConnectionAvailable(AddCoinsActivity.this)) {
//                                                            String wallet_name = sharedPreferences.getString(CONSTANTS.walletName, "");
////                                                            Creating Wallet
//                                                            CreateWallet(wallet_name, coins.getStr_coin_code());
//
//                                                        } else {
//                                                            CommonUtilities.ShowToastMessage(AddCoinsActivity.this, getResources().getString(R.string.internetconnection));
//                                                        }
//                                                    }
//
//                                                });
//                                                break;
//                                            }
//                                        }


                int i = 0;
                //  final AllCoins selectedCoin = new AllCoins();
                for (AllCoins coins : selected_allCoinsList) {
                    if (coins.getSelected()) {
                        coins.setSelected(false);
                        addCoinsRAdapter.notifyItemChanged(i);
                    }
                    i++;
                }
                addCoinsRAdapter.setCoinValue(!selected_allCoinsList.get(pos).getSelected(), pos);
                if (selected_allCoinsList.get(pos).getSelected()) {
                    selectedCoinId = selected_allCoinsList.get(pos).getInt_coin_id();
                    btn_ready.setVisibility(View.VISIBLE);
                } else
                    btn_ready.setVisibility(View.GONE);

                btn_ready.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (CommonUtilities.isConnectionAvailable(AddCoinsActivity.this)) {
                            String wallet_name = sharedPreferences.getString(CONSTANTS.walletName, " ");
//                                                              Creating Wallet
                            CreateWallet(selected_allCoinsList.get(pos).getStr_coin_name(), selected_allCoinsList.get(pos).getStr_coin_code(), wallet_name);
                        } else {
                            CommonUtilities.ShowToastMessage(AddCoinsActivity.this, getResources().getString(R.string.internetconnection));
                        }
                    }
                });
                                       /* if (i == 0) {
                                            CommonUtilities.ShowToastMessage(AddCoinsActivity.this, getResources().getString(R.string.selected_none));
                                        } else if (i > 1) {
                                            CommonUtilities.ShowToastMessage(AddCoinsActivity.this, getResources().getString(R.string.select_one));
                                        } else {
                                            for (int j = 0; j < selected_allCoinsList.size(); j++) {
                                                if (selected_allCoinsList.get(j).getSelected()) {
                                                    selectedCoin.setStr_coin_code(selected_allCoinsList.get(j).getStr_coin_code());
                                                    selectedCoin.setStr_coin_name(selected_allCoinsList.get(j).getStr_coin_name());
                                                    btn_ready.setVisibility(View.VISIBLE);
                                                    btn_ready.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            if (CommonUtilities.isConnectionAvailable(AddCoinsActivity.this)) {
                                                                String wallet_name = sharedPreferences.getString(CONSTANTS.walletName, "sss");
//                                                              Creating Wallet
                                                                CreateWallet(selectedCoin.getStr_coin_name(), selectedCoin.getStr_coin_code(),wallet_name);
                                                            } else {
                                                                CommonUtilities.ShowToastMessage(AddCoinsActivity.this, getResources().getString(R.string.internetconnection));
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        }*/
            }
        };


    }

    private void onLoadAllCoins() {

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ExploreCoinsDao exploreCoinsDao = deviantXDB.exploreCoinsDao();
                if ((exploreCoinsDao.getExploreCoins()) != null) {
                    String walletResult = exploreCoinsDao.getExploreCoins().exploreCoins;
                    updateUI(walletResult);
                } else {
                    if (CommonUtilities.isConnectionAvailable(getApplicationContext())) {
                        fetchCoins();
                    } else {
                        CommonUtilities.ShowToastMessage(getApplicationContext(), getResources().getString(R.string.internetconnection));
                    }
                }
            }
        });

    }

    private void updateUI(String responsevalue) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject(responsevalue);
                   // loginResponseMsg = jsonObject.getString("msg");
                  //  loginResponseStatus = jsonObject.getString("status");

                    if (true) {
                        loginResponseData = jsonObject.getString("data");
                        //JSONArray jsonArrayData = new JSONArray(loginResponseData);
                        AllCoins[] coinsStringArray = GsonUtils.getInstance().fromJson(loginResponseData, AllCoins[].class);

                        allCoinsList = new ArrayList<AllCoins>(Arrays.asList(coinsStringArray));
                        /*for (int i = 0; i < jsonArrayData.length(); i++) {
                            JSONObject jsonObjectCoins = jsonArrayData.getJSONObject(i);

                            try {
                                int_data_id = jsonObjectCoins.getInt("id");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                str_coin_name = jsonObjectCoins.getString("name");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                str_coin_code = jsonObjectCoins.getString("code");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                str_coin_logo = jsonObjectCoins.getString("logo");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                dbl_coin_usdValue = jsonObjectCoins.getDouble("usdValue");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                int_coin_rank = jsonObjectCoins.getInt("rank");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                dbl_coin_marketCap = jsonObjectCoins.getDouble("marketCap");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                dbl_coin_volume = jsonObjectCoins.getDouble("volume");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                dbl_coin_24h = jsonObjectCoins.getDouble("change24H");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                dbl_coin_7d = jsonObjectCoins.getDouble("change7D");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                dbl_coin_1m = jsonObjectCoins.getDouble("change1M");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            allCoinsList.add(new AllCoins(int_data_id, str_coin_name, str_coin_code, str_coin_logo, dbl_coin_usdValue, int_coin_rank, dbl_coin_marketCap, dbl_coin_volume, dbl_coin_24h, dbl_coin_7d, dbl_coin_1m));
                        }*/


                        addCoinsRAdapter = new AddCoinsRAdapter(AddCoinsActivity.this, allCoinsList, coinSelectableListener);
                        rview_coins_list.setAdapter(addCoinsRAdapter);
                        addCoinsRAdapter.notifyDataSetChanged();
                    } else {
                        CommonUtilities.ShowToastMessage(AddCoinsActivity.this, loginResponseMsg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void fetchCoins() {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            progressDialog = ProgressDialog.show(AddCoinsActivity.this, "", getResources().getString(R.string.please_wait), true);
            CoinsControllerApi apiService = DeviantXApiClient.getClient().create(CoinsControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.getAllCoins(CONSTANTS.DeviantMulti + token);
            apiResponse.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responsevalue = response.body().string();

                        if (!responsevalue.isEmpty() && responsevalue != null) {
                            progressDialog.dismiss();
                            updateUI(responsevalue);
                            ExploreCoinsDao mDao = deviantXDB.exploreCoinsDao();
                            ExploreCoins exploreCoins = new ExploreCoins(1, responsevalue);
                            mDao.insertAllCoins(exploreCoins);

                        } else {
                            CommonUtilities.ShowToastMessage(AddCoinsActivity.this, loginResponseMsg);
//                            Toast.makeText(getApplicationContext(), responsevalue, Toast.LENGTH_LONG).show();
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(AddCoinsActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(AddCoinsActivity.this, getResources().getString(R.string.Timeout));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Timeout), Toast.LENGTH_SHORT).show();
                    } else if (t instanceof java.net.ConnectException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(AddCoinsActivity.this, getResources().getString(R.string.networkerror));
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.networkerror), Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(AddCoinsActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception ex) {
            progressDialog.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(AddCoinsActivity.this, getResources().getString(R.string.errortxt));
//            Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
        }

    }

    private void CreateWallet(String coin_name, String str_coin_code, String wallet_name) {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            progressDialog = ProgressDialog.show(AddCoinsActivity.this, "", getResources().getString(R.string.please_wait), true);
            CryptoControllerApi apiService = DeviantXApiClient.getClient().create(CryptoControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.createWalletCoin(CONSTANTS.DeviantMulti + token, str_coin_code, coin_name, wallet_name);
            apiResponse.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responsevalue = response.body().string();

                        if (!responsevalue.isEmpty() && responsevalue != null) {
                            progressDialog.dismiss();

                            JSONObject jsonObject = new JSONObject(responsevalue);
                            loginResponseMsg = jsonObject.getString("msg");
                            loginResponseStatus = jsonObject.getString("status");

                            if (loginResponseStatus.equals("true")) {
                                loginResponseData = jsonObject.getString("data");
                                CommonUtilities.ShowToastMessage(AddCoinsActivity.this, getResources().getString(R.string.coin_added));
                                Intent serviceIntent = new Intent(getApplicationContext(), WalletDataFetch.class);
                                int wallet_id = sharedPreferences.getInt(CONSTANTS.walletId, 0);
                                serviceIntent.putExtra("walletName", wallet_name);
                                serviceIntent.putExtra("walletId", wallet_id);
                                startService(serviceIntent);
                                onBackPressed();

                            } else {
                                CommonUtilities.ShowToastMessage(AddCoinsActivity.this, loginResponseMsg);
                            }

                        } else {
                            CommonUtilities.ShowToastMessage(AddCoinsActivity.this, loginResponseMsg);
//                            Toast.makeText(getApplicationContext(), responsevalue, Toast.LENGTH_LONG).show();
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(AddCoinsActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(AddCoinsActivity.this, getResources().getString(R.string.Timeout));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Timeout), Toast.LENGTH_SHORT).show();
                    } else if (t instanceof java.net.ConnectException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(AddCoinsActivity.this, getResources().getString(R.string.networkerror));
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.networkerror), Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(AddCoinsActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception ex) {
            progressDialog.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(AddCoinsActivity.this, getResources().getString(R.string.errortxt));
//            Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
        }

    }


}
