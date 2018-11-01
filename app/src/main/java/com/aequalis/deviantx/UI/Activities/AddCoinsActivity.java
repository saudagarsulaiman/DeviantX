package com.aequalis.deviantx.UI.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.aequalis.deviantx.R;
import com.aequalis.deviantx.ServiceAPIs.CoinsControllerApi;
import com.aequalis.deviantx.ServiceAPIs.CryptoControllerApi;
import com.aequalis.deviantx.UI.Adapters.CoinsListRAdapter;
import com.aequalis.deviantx.UI.Models.AllCoins;
import com.aequalis.deviantx.UI.Interfaces.CoinSelectableListener;
import com.aequalis.deviantx.Utilities.CONSTANTS;
import com.aequalis.deviantx.Utilities.CommonUtilities;
import com.aequalis.deviantx.Utilities.DeviantXApiClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCoinsActivity extends AppCompatActivity {

    @BindView(R.id.tool)
    Toolbar tool;
    @BindView(R.id.lnr_search)
    LinearLayout lnr_search;
    @BindView(R.id.rview_coins_list)
    RecyclerView rview_coins_list;
    @BindView(R.id.btn_ready)
    Button btn_ready;

    CoinsListRAdapter coinsListRAdapter;
    GridLayoutManager layoutManager;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;

    int int_data_id;
    Double dbl_coin_usdValue;
    
    String loginResponseData, loginResponseStatus, loginResponseMsg,
             str_coin_name, str_coin_code, str_coin_logo;
    
    ArrayList<AllCoins> allCoinsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_coins);

        ButterKnife.bind(this);

        sharedPreferences = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        allCoinsList = new ArrayList<>();

        layoutManager = new GridLayoutManager(AddCoinsActivity.this, 2, GridLayoutManager.VERTICAL, false);
        rview_coins_list.setLayoutManager(layoutManager);

        if (CommonUtilities.isConnectionAvailable(AddCoinsActivity.this)) {
            //GET ALL COINS
            fetchCoins();
        } else {
            CommonUtilities.ShowToastMessage(AddCoinsActivity.this, getResources().getString(R.string.internetconnection));
        }


        lnr_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddCoinsActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });


        tool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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

                            JSONObject jsonObject = new JSONObject(responsevalue);
                            loginResponseMsg = jsonObject.getString("msg");
                            loginResponseStatus = jsonObject.getString("status");

                            if (loginResponseStatus.equals("true")) {
                                loginResponseData = jsonObject.getString("data");
                                JSONArray jsonArrayData = new JSONArray(loginResponseData);
                                for (int i = 0; i < jsonArrayData.length(); i++) {
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
                                    allCoinsList.add(new AllCoins(int_data_id, str_coin_name, str_coin_code, str_coin_logo, dbl_coin_usdValue));
                                }


                                CoinSelectableListener coinSelectableListener = new CoinSelectableListener() {
                                    @Override
                                    public void CoinSelected(ArrayList<AllCoins> allCoinsList) {
                                        btn_ready.setVisibility(View.GONE);


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
                                        final AllCoins selectedCoin = new AllCoins();
                                        for (AllCoins coins : allCoinsList) {
                                            if (coins.getSelected()) {
                                                i++;
                                            }
                                        }
                                        if (i == 0) {
                                            CommonUtilities.ShowToastMessage(AddCoinsActivity.this, getResources().getString(R.string.selected_none));
                                        } else if (i > 1) {
                                            CommonUtilities.ShowToastMessage(AddCoinsActivity.this, getResources().getString(R.string.select_one));
                                        } else {
                                            for (int j = 0; j < allCoinsList.size(); j++) {
                                                if (allCoinsList.get(j).getSelected()) {
                                                    selectedCoin.setStr_coin_code(allCoinsList.get(j).getStr_coin_code());
                                                    btn_ready.setVisibility(View.VISIBLE);
                                                    btn_ready.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            if (CommonUtilities.isConnectionAvailable(AddCoinsActivity.this)) {
                                                                String wallet_name = sharedPreferences.getString(CONSTANTS.walletName, "");
//                                                              Creating Wallet
                                                                CreateWallet(wallet_name, selectedCoin.getStr_coin_code());
                                                            } else {
                                                                CommonUtilities.ShowToastMessage(AddCoinsActivity.this, getResources().getString(R.string.internetconnection));
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        }
                                    }
                                };
                                coinsListRAdapter = new CoinsListRAdapter(AddCoinsActivity.this, allCoinsList, coinSelectableListener);
                                rview_coins_list.setAdapter(coinsListRAdapter);

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

    private void CreateWallet(String wallet_name, String str_coin_code) {

        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            progressDialog = ProgressDialog.show(AddCoinsActivity.this, "", getResources().getString(R.string.please_wait), true);
            CryptoControllerApi apiService = DeviantXApiClient.getClient().create(CryptoControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.createNewWallet(str_coin_code, wallet_name, CONSTANTS.DeviantMulti + token);
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
                                CommonUtilities.ShowToastMessage(AddCoinsActivity.this, getResources().getString(R.string.wallet_added));
                                editor.putString(CONSTANTS.walletName, "");
                                editor.apply();
                                Intent intent = new Intent(AddCoinsActivity.this, DashBoardActivity.class);
                                startActivity(intent);

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


    @Override
    public void onBackPressed() {
        Boolean wallet = sharedPreferences.getBoolean(CONSTANTS.wallet, false);
        if (wallet) {
            Intent intent = new Intent(AddCoinsActivity.this, DashBoardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            editor.putBoolean(CONSTANTS.wallet, false);
            editor.apply();
            startActivity(intent);
        } else {
//            Intent intent = new Intent(AddCoinsActivity.this, DashBoardActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
            super.onBackPressed();
        }

    }


}
