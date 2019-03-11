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
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.ServiceAPIs.WalletControllerApi;
import com.cryptowallet.deviantx.UI.Adapters.WalletHistoryRAdapter;
import com.cryptowallet.deviantx.UI.Models.AccountWallet;
import com.cryptowallet.deviantx.UI.Models.AllTransactions;
import com.cryptowallet.deviantx.UI.Models.ReceivedHistory;
import com.cryptowallet.deviantx.UI.Models.SentHistory;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;
import com.cryptowallet.deviantx.Utilities.DeviantXApiClient;
import com.cryptowallet.deviantx.Utilities.GsonUtils;

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


    String loginResponseMsg, loginResponseStatus, loginResponseData;


    WalletHistoryRAdapter walletHistoryRAdapter;
    LinearLayoutManager layoutManager;

    AccountWallet selectedAccountWallet;
    String transType;

    //    ArrayList<AllTransactions> allTransactionsList;
    ArrayList<SentHistory> sentHistoriesList, allList;
    ArrayList<ReceivedHistory> receivedHistoriesList;

    @Override
    protected void onResume() {
        super.onResume();
        myApplication.disableScreenCapture(this);
        if (walletHistoryRAdapter != null) {
            walletHistoryRAdapter.setIsHideBalance(myApplication.getHideBalance());
            walletHistoryRAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_history);

        ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

//        allTransactionsList = new ArrayList<>();
        sentHistoriesList = new ArrayList<>();
        receivedHistoriesList = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        selectedAccountWallet = bundle.getParcelable(CONSTANTS.selectedAccountWallet);
        transType = bundle.getString(CONSTANTS.transType);

        toolbar_center_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        layoutManager = new LinearLayoutManager(WalletHistoryActivity.this, LinearLayoutManager.VERTICAL, false);
        rview_trans_history.setLayoutManager(layoutManager);


        if (CommonUtilities.isConnectionAvailable(WalletHistoryActivity.this)) {
            fetchAllHistory(selectedAccountWallet);
/*
            if (transType.equals(CONSTANTS.sent)) {
                fetchSentHistory(selectedAccountWallet);
            } else {
                fetchReceivedHistory(selectedAccountWallet);
            }
*/


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
                if (transType.equals(CONSTANTS.sent)) {
                    ArrayList<SentHistory> searchCoinsList = new ArrayList<>();
                    for (SentHistory senderAddress : sentHistoriesList) {
                        if (senderAddress.getStr_toAddress().toLowerCase().contains(s.toString().toLowerCase())) {
                            searchCoinsList.add(senderAddress);
                        }
                    }
                    walletHistoryRAdapter = new WalletHistoryRAdapter(WalletHistoryActivity.this, searchCoinsList, receivedHistoriesList, true);
                    rview_trans_history.setAdapter(walletHistoryRAdapter);
                } else {
                    ArrayList<ReceivedHistory> searchCoinsList = new ArrayList<>();
                    for (ReceivedHistory senderAddress : receivedHistoriesList) {
                        if (senderAddress.getStr_fromAddress().toLowerCase().contains(s.toString().toLowerCase())) {
                            searchCoinsList.add(senderAddress);
                        }
                    }
                    walletHistoryRAdapter = new WalletHistoryRAdapter(WalletHistoryActivity.this, sentHistoriesList, searchCoinsList, false);
                    rview_trans_history.setAdapter(walletHistoryRAdapter);
                }
/*
//                ArrayList<Transaction> searchCoinsList = new ArrayList<>();
                for (Transaction senderAddress : transactions) {
                    if (senderAddress.getStr_data_toAddress().toLowerCase().contains(s.toString().toLowerCase())) {
                        searchCoinsList.add(senderAddress);
                    }
                }
*/
//                walletHistoryRAdapter = new WalletHistoryRAdapter(WalletHistoryActivity.this, searchCoinsList, selectedAccountWallet);
//                rview_trans_history.setAdapter(walletHistoryRAdapter);
            }
        });

    }
/*

    private void fetchSentHistory(AccountWallet selectedAccountWallet) {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            String wallet_name = sharedPreferences.getString(CONSTANTS.walletName, "sss");
            progressDialog = ProgressDialog.show(WalletHistoryActivity.this, "", getResources().getString(R.string.please_wait), true);
            WithdrawControllerApi apiService = DeviantXApiClient.getClient().create(WithdrawControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.getSentTransactions(CONSTANTS.DeviantMulti + token, wallet_name, selectedAccountWallet.getStr_coin_code());
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

                                        SentHistory[] coinsStringArray = GsonUtils.getInstance().fromJson(loginResponseData, SentHistory[].class);
                                        sentHistoriesList = new ArrayList<SentHistory>(Arrays.asList(coinsStringArray));


*/
/*
                                        ArrayList<Transaction> selectedCoinTransaction = new ArrayList<>();
                                        for (int i = 0; i < sentHistoriesList.size(); i++) {
                                            if (selectedAccountWallet.getStr_coin_name().equals(transactions.get(i).getCryptoWallet().getStr_data_cryptoWallet_address())) {
                                                selectedCoinTransaction.add(transactions.get(i));
                                            }
                                        }
*//*

                                        if (sentHistoriesList.size() > 0) {
                                            walletHistoryRAdapter = new WalletHistoryRAdapter(WalletHistoryActivity.this, sentHistoriesList, receivedHistoriesList, true);
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
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.networkerror), Toast.LENGTH_SHORT).show();
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

    private void fetchReceivedHistory(AccountWallet selectedAccountWallet) {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            String wallet_name = sharedPreferences.getString(CONSTANTS.walletName, "sss");
            progressDialog = ProgressDialog.show(WalletHistoryActivity.this, "", getResources().getString(R.string.please_wait), true);
            CryptoControllerApi apiService = DeviantXApiClient.getClient().create(CryptoControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.getReceivedTransactions(CONSTANTS.DeviantMulti + token, wallet_name, selectedAccountWallet.getStr_coin_code());
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

                                        ReceivedHistory[] coinsStringArray = GsonUtils.getInstance().fromJson(loginResponseData, ReceivedHistory[].class);
                                        receivedHistoriesList = new ArrayList<ReceivedHistory>(Arrays.asList(coinsStringArray));


*/
/*
                                        ArrayList<Transaction> selectedCoinTransaction = new ArrayList<>();
                                        for (int i = 0; i < sentHistoriesList.size(); i++) {
                                            if (selectedAccountWallet.getStr_coin_name().equals(transactions.get(i).getCryptoWallet().getStr_data_cryptoWallet_address())) {
                                                selectedCoinTransaction.add(transactions.get(i));
                                            }
                                        }
*//*

                                        if (receivedHistoriesList.size() > 0) {
                                            walletHistoryRAdapter = new WalletHistoryRAdapter(WalletHistoryActivity.this, sentHistoriesList, receivedHistoriesList, false);
                                            rview_trans_history.setAdapter(walletHistoryRAdapter);
                                            lnr_trans_avail.setVisibility(View.VISIBLE);
                                            lnr_no_trans.setVisibility(View.GONE);
                                            rview_trans_history.setVisibility(View.VISIBLE);
                                        } else {
                                            lnr_no_trans.setVisibility(View.VISIBLE);
                                            lnr_trans_avail.setVisibility(View.GONE);
                                            rview_trans_history.setVisibility(View.GONE);
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
*/

    private void fetchAllHistory(AccountWallet selectedAccountWallet) {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            String wallet_name = sharedPreferences.getString(CONSTANTS.walletName, "sss");
            progressDialog = ProgressDialog.show(WalletHistoryActivity.this, "", getResources().getString(R.string.please_wait), true);
            WalletControllerApi apiService = DeviantXApiClient.getClient().create(WalletControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.getAllHistory(CONSTANTS.DeviantMulti + token, wallet_name, selectedAccountWallet.getStr_coin_code());
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

                                    allList = new ArrayList<>();
                                    sentHistoriesList = new ArrayList<>();
//                                    receivedHistoriesList = new ArrayList<>();
                                    AllTransactions coinsStringArray = GsonUtils.getInstance().fromJson(loginResponseData, AllTransactions.class);
                                    sentHistoriesList = (ArrayList<SentHistory>) coinsStringArray.getList_sent();
                                    allList = (ArrayList<SentHistory>) coinsStringArray.getList_received();


                                    if (sentHistoriesList.size() == 0 && allList.size() == 0) {
                                        lnr_trans_avail.setVisibility(View.GONE);
                                        lnr_no_trans.setVisibility(View.VISIBLE);

                                    } else {
/*
                                        for (int i = 0; i < sentHistoriesList1.size(); i++) {
                                            allHistoryList.add(sentHistoriesList1.get(i));
                                        }
                                        for (int i = 0; i < receivedHistoriesList.size(); i++) {
                                            allHistoryList.add(receivedHistoriesList.get(i));
                                        }
*/
                                        for (int i = 0; i < sentHistoriesList.size(); i++) {
                                            allList.add(sentHistoriesList.get(i));
                                        }
                                        ArrayList<SentHistory> allHistoryList = new ArrayList<>();
                                        for (int i = 0; i < allList.size(); i++) {
                                            if (allList.get(i).getDbl_coinValue() >= 0.0001) {
                                                allHistoryList.add(allList.get(i));
                                            }
                                        }

                                        walletHistoryRAdapter = new WalletHistoryRAdapter(WalletHistoryActivity.this, allHistoryList, receivedHistoriesList, false);
                                        rview_trans_history.setAdapter(walletHistoryRAdapter);
                                        lnr_trans_avail.setVisibility(View.VISIBLE);
                                        lnr_no_trans.setVisibility(View.GONE);
                                    }


                                } else {
                                    CommonUtilities.ShowToastMessage(WalletHistoryActivity.this, getResources().getString(R.string.empty_data));
                                }
                            } else {
                                CommonUtilities.ShowToastMessage(WalletHistoryActivity.this, loginResponseMsg);
                            }

                        } else {
                            CommonUtilities.ShowToastMessage(WalletHistoryActivity.this, loginResponseMsg);
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(WalletHistoryActivity.this, getResources().getString(R.string.errortxt));
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(WalletHistoryActivity.this, getResources().getString(R.string.Timeout));
                    } else if (t instanceof java.net.ConnectException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(WalletHistoryActivity.this, getResources().getString(R.string.networkerror));
                    } else {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(WalletHistoryActivity.this, getResources().getString(R.string.errortxt));
                    }
                }
            });
        } catch (Exception ex) {
            progressDialog.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(WalletHistoryActivity.this, getResources().getString(R.string.errortxt));
        }

    }

}
