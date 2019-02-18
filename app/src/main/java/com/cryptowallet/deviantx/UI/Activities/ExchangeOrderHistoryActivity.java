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
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.ServiceAPIs.OrderBookControllerApi;
import com.cryptowallet.deviantx.UI.Adapters.ExchangeOrderHistoryRAdapter;
import com.cryptowallet.deviantx.UI.Models.ExcOrders;
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

public class ExchangeOrderHistoryActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_center_back)
    Toolbar toolbar_center_back;
    @BindView(R.id.img_search_type)
    ImageView img_search_type;
    @BindView(R.id.edt_search)
    EditText edt_search;
    @BindView(R.id.rview_order_history)
    RecyclerView rview_order_history;
    @BindView(R.id.lnr_no_trans)
    LinearLayout lnr_no_trans;
    @BindView(R.id.lnr_trans_avail)
    LinearLayout lnr_trans_avail;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;

    String loginResponseMsg, loginResponseStatus, loginResponseData;


    ExchangeOrderHistoryRAdapter exchangeOrderHistoryRAdapter;
    LinearLayoutManager linearLayoutManager;
    ArrayList<ExcOrders> allExcOpenOrders;

    @Override
    protected void onResume() {
        super.onResume();
        myApplication.disableScreenCapture(this);
//        if (walletHistoryRAdapter != null) {
//            walletHistoryRAdapter.setIsHideBalance(myApplication.getHideBalance());
//            walletHistoryRAdapter.notifyDataSetChanged();
//        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_order_history);
        ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        allExcOpenOrders = new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(ExchangeOrderHistoryActivity.this, LinearLayoutManager.VERTICAL, false);
        rview_order_history.setLayoutManager(linearLayoutManager);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
//                onLoadOpenOrders();
                if (CommonUtilities.isConnectionAvailable(ExchangeOrderHistoryActivity.this)) {
                    fetchAllOrders();
                } else {
                    CommonUtilities.ShowToastMessage(ExchangeOrderHistoryActivity.this, getResources().getString(R.string.internetconnection));
                }
            }
        }, 200);

/*
        exchangeOrderHistoryRAdapter = new ExchangeOrderHistoryRAdapter(ExchangeOrderHistoryActivity.this, allExcOpenOrders, false);
        rview_order_history.setAdapter(exchangeOrderHistoryRAdapter);
*/

//        Bundle bundle = getIntent().getExtras();
//        selectedAccountWallet = bundle.getParcelable(CONSTANTS.selectedAccountWallet);

//        transactions = new ArrayList<>();
        toolbar_center_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }


    private void fetchAllOrders() {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
//            progressDialog = ProgressDialog.show(ExchangeOrderHistoryActivity.this, "", getResources().getString(R.string.please_wait), true);
            OrderBookControllerApi apiService = DeviantXApiClient.getClient().create(OrderBookControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.getAll(CONSTANTS.DeviantMulti + token);
            apiResponse.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responsevalue = response.body().string();
//                        progressDialog.dismiss();

                        if (!responsevalue.isEmpty() && responsevalue != null) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        JSONObject jsonObject = new JSONObject(responsevalue);
                                        loginResponseMsg = jsonObject.getString("msg");
                                        loginResponseStatus = jsonObject.getString("status");

                                        if (loginResponseStatus.equals("true")) {
                                            loginResponseData = jsonObject.getString("data");
                                            ExcOrders[] coinsStringArray = GsonUtils.getInstance().fromJson(loginResponseData, ExcOrders[].class);
                                            allExcOpenOrders = new ArrayList<ExcOrders>(Arrays.asList(coinsStringArray));

                                            if (allExcOpenOrders.size() > 0) {
                                                lnr_no_trans.setVisibility(View.GONE);
                                                lnr_trans_avail.setVisibility(View.VISIBLE);
                                                exchangeOrderHistoryRAdapter = new ExchangeOrderHistoryRAdapter(ExchangeOrderHistoryActivity.this, allExcOpenOrders, false);
                                                rview_order_history.setAdapter(exchangeOrderHistoryRAdapter);
                                            } else {
                                                lnr_no_trans.setVisibility(View.VISIBLE);
                                                lnr_trans_avail.setVisibility(View.GONE);
                                            }
                                        } else {
                                            CommonUtilities.ShowToastMessage(ExchangeOrderHistoryActivity.this, loginResponseMsg);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                        } else {
                            CommonUtilities.ShowToastMessage(ExchangeOrderHistoryActivity.this, loginResponseMsg);
//                            Toast.makeText(getApplicationContext(), responsevalue, Toast.LENGTH_LONG).show();
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
//                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(ExchangeOrderHistoryActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
//                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(ExchangeOrderHistoryActivity.this, getResources().getString(R.string.Timeout));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Timeout), Toast.LENGTH_SHORT).show();
                    } else if (t instanceof java.net.ConnectException) {
//                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(ExchangeOrderHistoryActivity.this, getResources().getString(R.string.networkerror));
                    } else {
//                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(ExchangeOrderHistoryActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception ex) {
//            progressDialog.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(ExchangeOrderHistoryActivity.this, getResources().getString(R.string.errortxt));
//            Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
        }

    }


}
