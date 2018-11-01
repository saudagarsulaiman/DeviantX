package com.aequalis.deviantx.UI.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.aequalis.deviantx.R;
import com.aequalis.deviantx.ServiceAPIs.AuthenticationApi;
import com.aequalis.deviantx.ServiceAPIs.CryptoControllerApi;
import com.aequalis.deviantx.UI.Adapters.WalletCoinsRAdapter;
import com.aequalis.deviantx.UI.Adapters.WalletHistoryRAdapter;
import com.aequalis.deviantx.UI.Models.AccountWallet;
import com.aequalis.deviantx.UI.Models.AllCoins;
import com.aequalis.deviantx.UI.Models.Transaction;
import com.aequalis.deviantx.Utilities.CONSTANTS;
import com.aequalis.deviantx.Utilities.CommonUtilities;
import com.aequalis.deviantx.Utilities.DeviantXApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalletHistoryActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_center_back)
    Toolbar toolbar_center_back;
    @BindView(R.id.img_search_type)
    ImageView img_search_type;
    @BindView(R.id.edt_search)
    EditText edt_search;
    @BindView(R.id.rview_trans_history)
    RecyclerView rview_trans_history;
    @BindView(R.id.lnr_no_trans)
    LinearLayout lnr_no_trans;
    @BindView(R.id.lnr_trans_avail)
    LinearLayout lnr_trans_avail;



    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;

    ArrayList<Transaction> transactions;

    String loginResponseMsg, loginResponseStatus, loginResponseData;

    int int_data_id;
    String str_data_txnHash, str_data_toAddress, str_data_txnDate, str_data_cryptoWallet, str_data_icoTokenwallet, str_data_account;
    Double dbl_data_coinValue;
    AllCoins allCoins;
    int int_coin_id;
    Double dbl_coin_usdValue;
    String str_coin_name, str_coin_code, str_coin_logo;
    String str_data_coin;


    WalletHistoryRAdapter walletHistoryRAdapter;
    LinearLayoutManager layoutManager;

    AccountWallet selectedAccountWallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_history);

        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        selectedAccountWallet = bundle.getParcelable(CONSTANTS.selectedAccountWallet);

        sharedPreferences = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        transactions = new ArrayList<>();
        toolbar_center_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        layoutManager = new LinearLayoutManager(WalletHistoryActivity.this, LinearLayoutManager.VERTICAL, false);
        rview_trans_history.setLayoutManager(layoutManager);
        walletHistoryRAdapter = new WalletHistoryRAdapter(WalletHistoryActivity.this, transactions);
        rview_trans_history.setAdapter(walletHistoryRAdapter);

        
        if (CommonUtilities.isConnectionAvailable(WalletHistoryActivity.this)) {
//            Transaction History
            fetchTransactionHistory();

        } else {
            CommonUtilities.ShowToastMessage(WalletHistoryActivity.this, getResources().getString(R.string.internetconnection));
        }


    }

    private void fetchTransactionHistory() {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            progressDialog = ProgressDialog.show(WalletHistoryActivity.this, "", getResources().getString(R.string.please_wait), true);
            CryptoControllerApi apiService = DeviantXApiClient.getClient().create(CryptoControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.getTransactions(CONSTANTS.DeviantMulti + token);
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
                                if (!loginResponseData.isEmpty()) {
                                    JSONArray jsonArrayData = new JSONArray(loginResponseData);

                                    if (jsonArrayData.length() == 0) {
                                        lnr_trans_avail.setVisibility(View.GONE);
                                        lnr_no_trans.setVisibility(View.VISIBLE);
//                                        CommonUtilities.ShowToastMessage(WalletHistoryActivity.this, getResources().getString(R.string.no_trans_avail));
                                    } else {
                                        lnr_trans_avail.setVisibility(View.VISIBLE);
                                        lnr_no_trans.setVisibility(View.GONE);
                                        for (int i = 0; i < jsonArrayData.length(); i++) {
                                            JSONObject jsonObjectData = jsonArrayData.getJSONObject(i);
                                            try {
                                                int_data_id = jsonObjectData.getInt("id");
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            try {
                                                str_data_txnHash = jsonObjectData.getString("txnHash");
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            try {
                                                str_data_toAddress = jsonObjectData.getString("toAddress");
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            try {
                                                str_data_txnDate = jsonObjectData.getString("txnDate");
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            try {
                                                dbl_data_coinValue = jsonObjectData.getDouble("coinValue");
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                            try {
                                                str_data_account = jsonObjectData.getString("account");
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            try {
                                                str_data_cryptoWallet = jsonObjectData.getString("cryptoWallet");
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            try {
                                                str_data_icoTokenwallet = jsonObjectData.getString("icoTokenwallet");
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                            try {
                                                str_data_coin = jsonObjectData.getString("coin");
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            JSONObject jsonObjectCoins = new JSONObject(str_data_coin);
                                            try {
                                                int_coin_id = jsonObjectCoins.getInt("id");
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
                                            AllCoins allCoins = new AllCoins(int_coin_id, str_coin_name, str_coin_code, str_coin_logo, dbl_coin_usdValue);
                                            transactions.add(new Transaction(int_data_id, str_data_txnHash, str_data_toAddress, str_data_txnDate, str_data_cryptoWallet, str_data_icoTokenwallet, str_data_account, dbl_data_coinValue, allCoins));
                                        }
                                        walletHistoryRAdapter = new WalletHistoryRAdapter(WalletHistoryActivity.this, transactions);
                                        rview_trans_history.setAdapter(walletHistoryRAdapter);
                                    }

                                } else {
                                    CommonUtilities.ShowToastMessage(WalletHistoryActivity.this, getResources().getString(R.string.empty_data));
                                }
                            } else {
                                CommonUtilities.ShowToastMessage(WalletHistoryActivity.this, loginResponseMsg);
                            }

                        } else {
                            CommonUtilities.ShowToastMessage(WalletHistoryActivity.this, loginResponseMsg);
//                            Toast.makeText(getApplicationContext(), responsevalue, Toast.LENGTH_LONG).show();
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(WalletHistoryActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(WalletHistoryActivity.this, getResources().getString(R.string.Timeout));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Timeout), Toast.LENGTH_SHORT).show();
                    } else if (t instanceof java.net.ConnectException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(WalletHistoryActivity.this, getResources().getString(R.string.networkerror));
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.networkerror), Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(WalletHistoryActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception ex) {
            progressDialog.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(WalletHistoryActivity.this, getResources().getString(R.string.errortxt));
//            Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
        }

    }


}
