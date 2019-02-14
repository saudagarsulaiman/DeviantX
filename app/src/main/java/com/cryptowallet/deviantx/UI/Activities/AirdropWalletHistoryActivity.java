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
import com.cryptowallet.deviantx.ServiceAPIs.AirdropWalletControllerApi;
import com.cryptowallet.deviantx.UI.Adapters.AirdropWalletHistoryRAdapter;
import com.cryptowallet.deviantx.UI.Models.AirdropWalletTransaction;
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

public class AirdropWalletHistoryActivity extends AppCompatActivity {

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

    ArrayList<AirdropWalletTransaction> transactions;
    LinearLayoutManager layoutManager;

    AirdropWalletHistoryRAdapter walletHistoryRAdapter;

    LinearLayoutManager linearLayoutManager;


    String loginResponseMsg, loginResponseStatus, loginResponseData;

    @Override
    protected void onResume() {
        super.onResume();
        myApplication.disableScreenCapture(this);
        if (walletHistoryRAdapter != null) {
            walletHistoryRAdapter.setIsHideBalance(myApplication.getHideBalance());
            walletHistoryRAdapter.notifyDataSetChanged();
        }
//        CommonUtilities.serviceStart(AirdropWalletHistoryActivity.this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airdrop_wallet_history);

        ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        toolbar_center_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        linearLayoutManager = new LinearLayoutManager(AirdropWalletHistoryActivity.this,LinearLayoutManager.VERTICAL,false);
        rview_trans_history.setLayoutManager(linearLayoutManager);

        transactions = new ArrayList<>();


        if (CommonUtilities.isConnectionAvailable(AirdropWalletHistoryActivity.this)) {
            fetchReceivedHistory();
        } else {
            CommonUtilities.ShowToastMessage(AirdropWalletHistoryActivity.this, getResources().getString(R.string.internetconnection));
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
                ArrayList<AirdropWalletTransaction> searchCoinsList = new ArrayList<>();
                for (AirdropWalletTransaction senderAddress : transactions) {
                    if (senderAddress.getStr_coinCode().toLowerCase().contains(s.toString().toLowerCase())) {
                        searchCoinsList.add(senderAddress);
                    }
                }
                walletHistoryRAdapter = new AirdropWalletHistoryRAdapter(AirdropWalletHistoryActivity.this, searchCoinsList);
                rview_trans_history.setAdapter(walletHistoryRAdapter);
            }
        });
    }

    private void fetchReceivedHistory() {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            progressDialog = ProgressDialog.show(AirdropWalletHistoryActivity.this, "", getResources().getString(R.string.please_wait), true);
            AirdropWalletControllerApi apiService = DeviantXApiClient.getClient().create(AirdropWalletControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.getAirdropWalletHistory(CONSTANTS.DeviantMulti + token);
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
//                                        CommonUtilities.ShowToastMessage(AirdropWalletHistoryActivity.this, getResources().getString(R.string.no_trans_avail));
                                    } else {
                                        lnr_trans_avail.setVisibility(View.VISIBLE);
                                        lnr_no_trans.setVisibility(View.GONE);

                                        AirdropWalletTransaction[] coinsStringArray = GsonUtils.getInstance().fromJson(loginResponseData, AirdropWalletTransaction[].class);
                                        transactions = new ArrayList<AirdropWalletTransaction>(Arrays.asList(coinsStringArray));

                                        if (transactions.size() > 0) {
                                            walletHistoryRAdapter = new AirdropWalletHistoryRAdapter(AirdropWalletHistoryActivity.this, transactions);
                                            rview_trans_history.setAdapter(walletHistoryRAdapter);
                                            lnr_trans_avail.setVisibility(View.VISIBLE);
                                            lnr_no_trans.setVisibility(View.GONE);
                                        } else {
                                            lnr_no_trans.setVisibility(View.VISIBLE);
                                            lnr_trans_avail.setVisibility(View.GONE);
                                        }

                                    }

                                } else {
                                    CommonUtilities.ShowToastMessage(AirdropWalletHistoryActivity.this, getResources().getString(R.string.empty_data));
                                }
                            } else {
                                CommonUtilities.ShowToastMessage(AirdropWalletHistoryActivity.this, loginResponseMsg);
                            }

                        } else {
                            CommonUtilities.ShowToastMessage(AirdropWalletHistoryActivity.this, loginResponseMsg);
//                            Toast.makeText(getApplicationContext(), responsevalue, Toast.LENGTH_LONG).show();
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(AirdropWalletHistoryActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(AirdropWalletHistoryActivity.this, getResources().getString(R.string.Timeout));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Timeout), Toast.LENGTH_SHORT).show();
                    } else if (t instanceof java.net.ConnectException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(AirdropWalletHistoryActivity.this, getResources().getString(R.string.networkerror));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.networkerror), Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(AirdropWalletHistoryActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception ex) {
            progressDialog.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(AirdropWalletHistoryActivity.this, getResources().getString(R.string.errortxt));
//            Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
        }

    }


}
