package com.cryptowallet.deviantx.UI.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.ServiceAPIs.CryptoControllerApi;
import com.cryptowallet.deviantx.UI.Adapters.WalletHistoryRAdapter;
import com.cryptowallet.deviantx.UI.Models.AccountWallet;
import com.cryptowallet.deviantx.UI.Models.AllCoins;
import com.cryptowallet.deviantx.UI.Models.CryptoWallet;
import com.cryptowallet.deviantx.UI.Models.Transaction;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;
import com.cryptowallet.deviantx.Utilities.DeviantXApiClient;

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

import static com.cryptowallet.deviantx.Utilities.MyApplication.myApplication;

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
    String str_data_txnHash, str_data_toAddress, str_data_category, str_data_txnDate, str_data_cryptoWallet, str_data_icoTokenwallet, str_data_account, str_data_cryptoWallet_address;
    Double dbl_data_coinValue, dbl_data_cryptoWallet_bal;
    AllCoins allCoins;
    int int_coin_id, int_coin_rank;
    Double dbl_coin_usdValue, dbl_coin_marketCap, dbl_coin_volume, dbl_coin_24h, dbl_coin_7d, dbl_coin_1m;
    String str_coin_name, str_coin_code, str_coin_logo;
    String str_data_coin;


    WalletHistoryRAdapter walletHistoryRAdapter;
    LinearLayoutManager layoutManager;

    AccountWallet selectedAccountWallet;


    @Override
    protected void onResume() {
        super.onResume();
        myApplication.disableScreenCapture(this);
        if (walletHistoryRAdapter != null) {
            walletHistoryRAdapter.setIsHideBalance(myApplication.getHideBalance());
            walletHistoryRAdapter.notifyDataSetChanged();
        }
//        CommonUtilities.serviceStart(WalletHistoryActivity.this);
    }

   /* @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_HOME) {
//            Log.e("home key pressed", "****");
            // write your code here to stop the activity
            CommonUtilities.serviceStop(WalletHistoryActivity.this);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
//        Log.e("home key pressed on pause", "****");
        // write your code here to stop your service
        CommonUtilities.serviceStop(WalletHistoryActivity.this);
        super.onPause();
    }*/


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
        walletHistoryRAdapter = new WalletHistoryRAdapter(WalletHistoryActivity.this, transactions, selectedAccountWallet);
        rview_trans_history.setAdapter(walletHistoryRAdapter);


        if (CommonUtilities.isConnectionAvailable(WalletHistoryActivity.this)) {
//            Transaction History
            fetchTransactionHistory(selectedAccountWallet);

        } else {
            CommonUtilities.ShowToastMessage(WalletHistoryActivity.this, getResources().getString(R.string.internetconnection));
        }

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ArrayList<Transaction> searchCoinsList = new ArrayList<>();
                for (Transaction senderAddress : transactions) {
                    if (senderAddress.getStr_data_toAddress().toLowerCase().contains(s.toString().toLowerCase())) {
                        searchCoinsList.add(senderAddress);
                    }
                }
                walletHistoryRAdapter = new WalletHistoryRAdapter(WalletHistoryActivity.this, searchCoinsList, selectedAccountWallet);
                rview_trans_history.setAdapter(walletHistoryRAdapter);
            }
        });

    }

    private void fetchTransactionHistory(final AccountWallet selectedAccountWallet) {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            progressDialog = ProgressDialog.show(WalletHistoryActivity.this, "", getResources().getString(R.string.please_wait), true);
            CryptoControllerApi apiService = DeviantXApiClient.getClient().create(CryptoControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.getTransactions(CONSTANTS.DeviantMulti + token, selectedAccountWallet.getStr_data_address());
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
                                                str_data_category = jsonObjectData.getString("category");
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
                                                str_data_icoTokenwallet = jsonObjectData.getString("icoTokenwallet");
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            try {
                                                str_data_account = jsonObjectData.getString("account");
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

                                            try {
                                                str_data_cryptoWallet = jsonObjectData.getString("cryptoWallet");
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            if (!str_data_cryptoWallet.isEmpty()) {
                                                JSONObject jsonObjectcrypto = new JSONObject(str_data_cryptoWallet);
                                                try {
                                                    str_data_cryptoWallet_address = jsonObjectcrypto.getString("address");
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                                try {
                                                    dbl_data_cryptoWallet_bal = jsonObjectcrypto.getDouble("balance");
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            CryptoWallet cryptoWallet = new CryptoWallet(str_data_cryptoWallet_address, dbl_data_cryptoWallet_bal);
                                            AllCoins allCoins = new AllCoins(int_coin_id, str_coin_name, str_coin_code, str_coin_logo, dbl_coin_usdValue, int_coin_rank, dbl_coin_marketCap, dbl_coin_volume, dbl_coin_24h, dbl_coin_7d, dbl_coin_1m);
                                            transactions.add(new Transaction(int_data_id, str_data_txnHash, str_data_toAddress, str_data_txnDate, str_data_cryptoWallet, str_data_icoTokenwallet, str_data_account, dbl_data_coinValue, str_data_category, allCoins, cryptoWallet));
                                        }
                                        ArrayList<Transaction> selectedCoinTransaction = new ArrayList<>();
                                        for (int i = 0; i < transactions.size(); i++) {
                                            if (selectedAccountWallet.getStr_data_address().equals(transactions.get(i).getCryptoWallet().getStr_data_cryptoWallet_address())) {
                                                selectedCoinTransaction.add(transactions.get(i));
                                            }
                                        }
                                        if (selectedCoinTransaction.size() > 0) {
                                            walletHistoryRAdapter = new WalletHistoryRAdapter(WalletHistoryActivity.this, selectedCoinTransaction, selectedAccountWallet);
                                            rview_trans_history.setAdapter(walletHistoryRAdapter);
                                            lnr_trans_avail.setVisibility(View.VISIBLE);
                                            lnr_no_trans.setVisibility(View.GONE);
                                        } else {
                                            lnr_no_trans.setVisibility(View.VISIBLE);
                                            lnr_trans_avail.setVisibility(View.GONE);
                                        }

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
