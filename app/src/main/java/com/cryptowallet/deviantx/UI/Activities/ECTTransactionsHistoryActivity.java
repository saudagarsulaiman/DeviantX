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
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.ServiceAPIs.ChangellyControllerApi;
import com.cryptowallet.deviantx.UI.Adapters.ECTCreatedTransHistoryRAdapter;
import com.cryptowallet.deviantx.UI.Models.ECTCreatedTransactionData;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;
import com.cryptowallet.deviantx.Utilities.DeviantXApiClient;
import com.cryptowallet.deviantx.Utilities.GsonUtils;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.squareup.picasso.Picasso;

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

public class ECTTransactionsHistoryActivity extends AppCompatActivity {

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
    @BindView(R.id.lnr_filter)
    LinearLayout lnr_filter;
    @BindView(R.id.txt_filterName)
    TextView txt_filterName;


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;

    String loginResponseMsg, loginResponseStatus, loginResponseData;


    ECTCreatedTransHistoryRAdapter exchangeOrderHistoryRAdapter;
    LinearLayoutManager linearLayoutManager;
    ArrayList<ECTCreatedTransactionData> allTransOrders, allNewOrders, allSuccessfulOrders, allWaitingOrders;

    String selectedFilter = "All Orders";

    @Override
    protected void onResume() {
        super.onResume();
        myApplication.disableScreenCapture(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecttransactions_history);
        ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        allTransOrders = new ArrayList<>();
        allNewOrders = new ArrayList<>();
        allSuccessfulOrders = new ArrayList<>();
        allWaitingOrders = new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(ECTTransactionsHistoryActivity.this, LinearLayoutManager.VERTICAL, false);
        rview_order_history.setLayoutManager(linearLayoutManager);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
//                onLoadOpenOrders();
                if (CommonUtilities.isConnectionAvailable(ECTTransactionsHistoryActivity.this)) {
                    fetchAllOrders();
                } else {
                    CommonUtilities.ShowToastMessage(ECTTransactionsHistoryActivity.this, getResources().getString(R.string.internetconnection));
                }
            }
        }, 200);

        toolbar_center_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        lnr_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterDialog();
            }
        });
    }


    private void showFilterDialog() {
        ViewHolder viewHolder = new ViewHolder(R.layout.dialog_filter_ect);
        final DialogPlus dialog = DialogPlus.newDialog(ECTTransactionsHistoryActivity.this)
                .setContentHolder(viewHolder)
                .setGravity(Gravity.BOTTOM)
                .setCancelable(false)
                .setInAnimation(R.anim.slide_in_bottom)
                .setOutAnimation(R.anim.slide_out_bottom)
                .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .create();

//                Initializing Widgets
        View view = dialog.getHolderView();
        LinearLayout lnr_open_partial = view.findViewById(R.id.lnr_open_partial);
        LinearLayout lnr_executed = view.findViewById(R.id.lnr_executed);
        LinearLayout lnr_all_orders = view.findViewById(R.id.lnr_all_orders);
        LinearLayout lnr_pending = view.findViewById(R.id.lnr_pending);
        ImageView img_open_pending_orders = view.findViewById(R.id.img_open_pending_orders);
        ImageView img_executed_orders = view.findViewById(R.id.img_executed_orders);
        ImageView img_pending_orders = view.findViewById(R.id.img_pending_orders);
        ImageView img_all_orders = view.findViewById(R.id.img_all_orders);
        ImageView img_center_back = view.findViewById(R.id.img_center_back);

/*
        String selectedFilter = sharedPreferences.getString(CONSTANTS.selECTFilter, getResources().getText(R.string.all_orders).toString());
*/

        if (selectedFilter.equals(getResources().getText(R.string.new_orders).toString())) {
            Picasso.with(ECTTransactionsHistoryActivity.this).load(R.drawable.selected_circle).into(img_open_pending_orders);
            Picasso.with(ECTTransactionsHistoryActivity.this).load(R.drawable.unselected_circle).into(img_executed_orders);
            Picasso.with(ECTTransactionsHistoryActivity.this).load(R.drawable.unselected_circle).into(img_pending_orders);
            Picasso.with(ECTTransactionsHistoryActivity.this).load(R.drawable.unselected_circle).into(img_all_orders);
        } else if (selectedFilter.equals(getResources().getText(R.string.executed_orders).toString())) {
            Picasso.with(ECTTransactionsHistoryActivity.this).load(R.drawable.unselected_circle).into(img_open_pending_orders);
            Picasso.with(ECTTransactionsHistoryActivity.this).load(R.drawable.selected_circle).into(img_executed_orders);
            Picasso.with(ECTTransactionsHistoryActivity.this).load(R.drawable.unselected_circle).into(img_pending_orders);
            Picasso.with(ECTTransactionsHistoryActivity.this).load(R.drawable.unselected_circle).into(img_all_orders);
        } else if (selectedFilter.equals(getResources().getText(R.string.pending_orders).toString())) {
            Picasso.with(ECTTransactionsHistoryActivity.this).load(R.drawable.unselected_circle).into(img_open_pending_orders);
            Picasso.with(ECTTransactionsHistoryActivity.this).load(R.drawable.unselected_circle).into(img_executed_orders);
            Picasso.with(ECTTransactionsHistoryActivity.this).load(R.drawable.selected_circle).into(img_pending_orders);
            Picasso.with(ECTTransactionsHistoryActivity.this).load(R.drawable.unselected_circle).into(img_all_orders);
        } else if (selectedFilter.equals(getResources().getText(R.string.all_orders).toString())) {
            Picasso.with(ECTTransactionsHistoryActivity.this).load(R.drawable.unselected_circle).into(img_open_pending_orders);
            Picasso.with(ECTTransactionsHistoryActivity.this).load(R.drawable.unselected_circle).into(img_executed_orders);
            Picasso.with(ECTTransactionsHistoryActivity.this).load(R.drawable.unselected_circle).into(img_pending_orders);
            Picasso.with(ECTTransactionsHistoryActivity.this).load(R.drawable.selected_circle).into(img_all_orders);
        }

        img_center_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        lnr_open_partial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedFilter = getResources().getText(R.string.new_orders).toString();
                Picasso.with(ECTTransactionsHistoryActivity.this).load(R.drawable.selected_circle).into(img_open_pending_orders);
                Picasso.with(ECTTransactionsHistoryActivity.this).load(R.drawable.unselected_circle).into(img_executed_orders);
                Picasso.with(ECTTransactionsHistoryActivity.this).load(R.drawable.unselected_circle).into(img_pending_orders);
                Picasso.with(ECTTransactionsHistoryActivity.this).load(R.drawable.unselected_circle).into(img_all_orders);
                txt_filterName.setText(getResources().getText(R.string.new_orders));
                if (allNewOrders.size() > 0) {
                    lnr_no_trans.setVisibility(View.GONE);
                    lnr_trans_avail.setVisibility(View.VISIBLE);
                    exchangeOrderHistoryRAdapter = new ECTCreatedTransHistoryRAdapter(ECTTransactionsHistoryActivity.this, allNewOrders);
                    rview_order_history.setAdapter(exchangeOrderHistoryRAdapter);
                } else {
                    lnr_no_trans.setVisibility(View.VISIBLE);
                    lnr_trans_avail.setVisibility(View.GONE);
                }
                dialog.dismiss();
            }
        });

        lnr_executed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedFilter = getResources().getText(R.string.executed_orders).toString();
                Picasso.with(ECTTransactionsHistoryActivity.this).load(R.drawable.unselected_circle).into(img_open_pending_orders);
                Picasso.with(ECTTransactionsHistoryActivity.this).load(R.drawable.selected_circle).into(img_executed_orders);
                Picasso.with(ECTTransactionsHistoryActivity.this).load(R.drawable.unselected_circle).into(img_pending_orders);
                Picasso.with(ECTTransactionsHistoryActivity.this).load(R.drawable.unselected_circle).into(img_all_orders);
                txt_filterName.setText(getResources().getText(R.string.executed_orders));
                if (allSuccessfulOrders.size() > 0) {
                    lnr_no_trans.setVisibility(View.GONE);
                    lnr_trans_avail.setVisibility(View.VISIBLE);
                    exchangeOrderHistoryRAdapter = new ECTCreatedTransHistoryRAdapter(ECTTransactionsHistoryActivity.this, allSuccessfulOrders);
                    rview_order_history.setAdapter(exchangeOrderHistoryRAdapter);
                } else {
                    lnr_no_trans.setVisibility(View.VISIBLE);
                    lnr_trans_avail.setVisibility(View.GONE);
                }
                dialog.dismiss();
            }
        });

        lnr_pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedFilter = getResources().getText(R.string.pending_orders).toString();
                Picasso.with(ECTTransactionsHistoryActivity.this).load(R.drawable.unselected_circle).into(img_open_pending_orders);
                Picasso.with(ECTTransactionsHistoryActivity.this).load(R.drawable.unselected_circle).into(img_executed_orders);
                Picasso.with(ECTTransactionsHistoryActivity.this).load(R.drawable.selected_circle).into(img_pending_orders);
                Picasso.with(ECTTransactionsHistoryActivity.this).load(R.drawable.unselected_circle).into(img_all_orders);
                txt_filterName.setText(getResources().getText(R.string.pending_orders));
                if (allWaitingOrders.size() > 0) {
                    lnr_no_trans.setVisibility(View.GONE);
                    lnr_trans_avail.setVisibility(View.VISIBLE);
                    exchangeOrderHistoryRAdapter = new ECTCreatedTransHistoryRAdapter(ECTTransactionsHistoryActivity.this, allWaitingOrders);
                    rview_order_history.setAdapter(exchangeOrderHistoryRAdapter);
                } else {
                    lnr_no_trans.setVisibility(View.VISIBLE);
                    lnr_trans_avail.setVisibility(View.GONE);
                }
                dialog.dismiss();
            }
        });

        lnr_all_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedFilter = getResources().getText(R.string.all_orders).toString();
                Picasso.with(ECTTransactionsHistoryActivity.this).load(R.drawable.unselected_circle).into(img_open_pending_orders);
                Picasso.with(ECTTransactionsHistoryActivity.this).load(R.drawable.unselected_circle).into(img_executed_orders);
                Picasso.with(ECTTransactionsHistoryActivity.this).load(R.drawable.unselected_circle).into(img_pending_orders);
                Picasso.with(ECTTransactionsHistoryActivity.this).load(R.drawable.selected_circle).into(img_all_orders);
                txt_filterName.setText(getResources().getText(R.string.all_orders));
                if (allTransOrders.size() > 0) {
                    lnr_no_trans.setVisibility(View.GONE);
                    lnr_trans_avail.setVisibility(View.VISIBLE);
                    exchangeOrderHistoryRAdapter = new ECTCreatedTransHistoryRAdapter(ECTTransactionsHistoryActivity.this, allTransOrders);
                    rview_order_history.setAdapter(exchangeOrderHistoryRAdapter);
                } else {
                    lnr_no_trans.setVisibility(View.VISIBLE);
                    lnr_trans_avail.setVisibility(View.GONE);
                }
                dialog.dismiss();
            }
        });

//                Displaying DialogPlus
        dialog.show();

    }

    private void fetchAllOrders() {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            ChangellyControllerApi apiService = DeviantXApiClient.getClient().create(ChangellyControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.getAllTransactions(CONSTANTS.DeviantMulti + token);
            apiResponse.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responsevalue = response.body().string();
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
                                            ECTCreatedTransactionData[] ectCreatedTransactionData = GsonUtils.getInstance().fromJson(loginResponseData, ECTCreatedTransactionData[].class);
                                            allTransOrders = new ArrayList<ECTCreatedTransactionData>(Arrays.asList(ectCreatedTransactionData));

                                            if (allTransOrders.size() > 0) {
                                                allNewOrders = new ArrayList<>();
                                                allWaitingOrders = new ArrayList<>();
                                                allSuccessfulOrders = new ArrayList<>();

                                                for (int i = 0; i < allTransOrders.size(); i++) {
                                                    if (allTransOrders.get(i).getStr_status().equals("new")) {
                                                        allNewOrders.add(allTransOrders.get(i));
                                                    } else if (allTransOrders.get(i).getStr_status().equals("waiting")) {
                                                        allWaitingOrders.add(allTransOrders.get(i));
                                                    } else /*if (allTransOrders.get(i).getStr_orderStatus().equals("open"))*/ {
                                                        allSuccessfulOrders.add(allTransOrders.get(i));
                                                    }
                                                }

                                                if (allTransOrders.size() > 0) {
                                                    txt_filterName.setText(getResources().getText(R.string.all_orders));
                                                    lnr_no_trans.setVisibility(View.GONE);
                                                    lnr_trans_avail.setVisibility(View.VISIBLE);
                                                    exchangeOrderHistoryRAdapter = new ECTCreatedTransHistoryRAdapter(ECTTransactionsHistoryActivity.this, allTransOrders);
                                                    rview_order_history.setAdapter(exchangeOrderHistoryRAdapter);
                                                } else {
                                                    lnr_no_trans.setVisibility(View.VISIBLE);
                                                    lnr_trans_avail.setVisibility(View.GONE);
                                                }
                                            } else {
                                                lnr_no_trans.setVisibility(View.VISIBLE);
                                                lnr_trans_avail.setVisibility(View.GONE);
                                            }
                                        } else {
                                            CommonUtilities.ShowToastMessage(ECTTransactionsHistoryActivity.this, loginResponseMsg);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                        } else {
                            CommonUtilities.ShowToastMessage(ECTTransactionsHistoryActivity.this, loginResponseMsg);
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        CommonUtilities.ShowToastMessage(ECTTransactionsHistoryActivity.this, getResources().getString(R.string.errortxt));
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        CommonUtilities.ShowToastMessage(ECTTransactionsHistoryActivity.this, getResources().getString(R.string.Timeout));
                    } else if (t instanceof java.net.ConnectException) {
                        CommonUtilities.ShowToastMessage(ECTTransactionsHistoryActivity.this, getResources().getString(R.string.networkerror));
                    } else {
                        CommonUtilities.ShowToastMessage(ECTTransactionsHistoryActivity.this, getResources().getString(R.string.errortxt));
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(ECTTransactionsHistoryActivity.this, getResources().getString(R.string.errortxt));
        }

    }


}
