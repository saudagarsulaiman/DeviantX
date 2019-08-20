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
import com.cryptowallet.deviantx.ServiceAPIs.OrderBookControllerApi;
import com.cryptowallet.deviantx.UI.Adapters.ExchangeOrderHistoryRAdapter;
import com.cryptowallet.deviantx.UI.Models.ExcOrders;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;
import com.cryptowallet.deviantx.Utilities.DeviantXApiClient;
import com.cryptowallet.deviantx.Utilities.GsonUtils;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
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
    @BindView(R.id.lnr_filter)
    LinearLayout lnr_filter;
    @BindView(R.id.txt_filterName)
    TextView txt_filterName;


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;

    String loginResponseMsg, loginResponseStatus, loginResponseData;


    ExchangeOrderHistoryRAdapter exchangeOrderHistoryRAdapter;
    LinearLayoutManager linearLayoutManager;
    ArrayList<ExcOrders> allExcOrder, allOpenOrders, allCancelledOrders, allExecuted̥Orders, allPendingOrders;

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
        allExcOrder = new ArrayList<>();
        allOpenOrders = new ArrayList<>();
        allCancelledOrders = new ArrayList<>();
        allExecuted̥Orders = new ArrayList<>();
        allPendingOrders = new ArrayList<>();

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
        exchangeOrderHistoryRAdapter = new ExchangeOrderHistoryRAdapter(ExchangeOrderHistoryActivity.this, allExcOrder, false);
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

        lnr_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterDialog();
            }
        });

    }

    private void showFilterDialog() {
        ViewHolder viewHolder = new ViewHolder(R.layout.dialog_filter_exe);
        final DialogPlus dialog = DialogPlus.newDialog(ExchangeOrderHistoryActivity.this)
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
        LinearLayout lnr_cancelled = view.findViewById(R.id.lnr_cancelled);
        ImageView img_open_pending_orders = view.findViewById(R.id.img_open_pending_orders);
        ImageView img_executed_orders = view.findViewById(R.id.img_executed_orders);
        ImageView img_pending_orders = view.findViewById(R.id.img_pending_orders);
        ImageView img_cancelled_orders = view.findViewById(R.id.img_cancelled_orders);
        ImageView img_all_orders = view.findViewById(R.id.img_all_orders);
        ImageView img_center_back = view.findViewById(R.id.img_center_back);

        String selectedFilter = sharedPreferences.getString(CONSTANTS.selFilter, getResources().getText(R.string.all_orders).toString());

        if (selectedFilter.equals(getResources().getText(R.string.open_partial_orders).toString())) {
            Picasso.with(ExchangeOrderHistoryActivity.this).load(R.drawable.selected_circle).into(img_open_pending_orders);
            Picasso.with(ExchangeOrderHistoryActivity.this).load(R.drawable.unselected_circle).into(img_executed_orders);
            Picasso.with(ExchangeOrderHistoryActivity.this).load(R.drawable.unselected_circle).into(img_pending_orders);
            Picasso.with(ExchangeOrderHistoryActivity.this).load(R.drawable.unselected_circle).into(img_cancelled_orders);
            Picasso.with(ExchangeOrderHistoryActivity.this).load(R.drawable.unselected_circle).into(img_all_orders);
        } else if (selectedFilter.equals(getResources().getText(R.string.executed_orders).toString())) {
            Picasso.with(ExchangeOrderHistoryActivity.this).load(R.drawable.unselected_circle).into(img_open_pending_orders);
            Picasso.with(ExchangeOrderHistoryActivity.this).load(R.drawable.selected_circle).into(img_executed_orders);
            Picasso.with(ExchangeOrderHistoryActivity.this).load(R.drawable.unselected_circle).into(img_pending_orders);
            Picasso.with(ExchangeOrderHistoryActivity.this).load(R.drawable.unselected_circle).into(img_cancelled_orders);
            Picasso.with(ExchangeOrderHistoryActivity.this).load(R.drawable.unselected_circle).into(img_all_orders);
        } else if (selectedFilter.equals(getResources().getText(R.string.pending_orders).toString())) {
            Picasso.with(ExchangeOrderHistoryActivity.this).load(R.drawable.unselected_circle).into(img_open_pending_orders);
            Picasso.with(ExchangeOrderHistoryActivity.this).load(R.drawable.unselected_circle).into(img_executed_orders);
            Picasso.with(ExchangeOrderHistoryActivity.this).load(R.drawable.selected_circle).into(img_pending_orders);
            Picasso.with(ExchangeOrderHistoryActivity.this).load(R.drawable.unselected_circle).into(img_cancelled_orders);
            Picasso.with(ExchangeOrderHistoryActivity.this).load(R.drawable.unselected_circle).into(img_all_orders);
        } else if (selectedFilter.equals(getResources().getText(R.string.cancelled_orders).toString())) {
            Picasso.with(ExchangeOrderHistoryActivity.this).load(R.drawable.unselected_circle).into(img_open_pending_orders);
            Picasso.with(ExchangeOrderHistoryActivity.this).load(R.drawable.unselected_circle).into(img_executed_orders);
            Picasso.with(ExchangeOrderHistoryActivity.this).load(R.drawable.unselected_circle).into(img_pending_orders);
            Picasso.with(ExchangeOrderHistoryActivity.this).load(R.drawable.selected_circle).into(img_cancelled_orders);
            Picasso.with(ExchangeOrderHistoryActivity.this).load(R.drawable.unselected_circle).into(img_all_orders);
        } else if (selectedFilter.equals(getResources().getText(R.string.all_orders).toString())) {
            Picasso.with(ExchangeOrderHistoryActivity.this).load(R.drawable.unselected_circle).into(img_open_pending_orders);
            Picasso.with(ExchangeOrderHistoryActivity.this).load(R.drawable.unselected_circle).into(img_executed_orders);
            Picasso.with(ExchangeOrderHistoryActivity.this).load(R.drawable.unselected_circle).into(img_pending_orders);
            Picasso.with(ExchangeOrderHistoryActivity.this).load(R.drawable.unselected_circle).into(img_cancelled_orders);
            Picasso.with(ExchangeOrderHistoryActivity.this).load(R.drawable.selected_circle).into(img_all_orders);
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
                editor.putString(CONSTANTS.selFilter, getResources().getText(R.string.open_partial_orders).toString());
                editor.apply();
                Picasso.with(ExchangeOrderHistoryActivity.this).load(R.drawable.selected_circle).into(img_open_pending_orders);
                Picasso.with(ExchangeOrderHistoryActivity.this).load(R.drawable.unselected_circle).into(img_executed_orders);
                Picasso.with(ExchangeOrderHistoryActivity.this).load(R.drawable.unselected_circle).into(img_pending_orders);
                Picasso.with(ExchangeOrderHistoryActivity.this).load(R.drawable.unselected_circle).into(img_cancelled_orders);
                Picasso.with(ExchangeOrderHistoryActivity.this).load(R.drawable.unselected_circle).into(img_all_orders);
                txt_filterName.setText(getResources().getText(R.string.open_partial_orders));
                if (allOpenOrders.size() > 0) {
                    lnr_no_trans.setVisibility(View.GONE);
                    lnr_trans_avail.setVisibility(View.VISIBLE);
                    exchangeOrderHistoryRAdapter = new ExchangeOrderHistoryRAdapter(ExchangeOrderHistoryActivity.this, allOpenOrders, false);
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
                editor.putString(CONSTANTS.selFilter, getResources().getText(R.string.executed_orders).toString());
                editor.apply();
                Picasso.with(ExchangeOrderHistoryActivity.this).load(R.drawable.unselected_circle).into(img_open_pending_orders);
                Picasso.with(ExchangeOrderHistoryActivity.this).load(R.drawable.selected_circle).into(img_executed_orders);
                Picasso.with(ExchangeOrderHistoryActivity.this).load(R.drawable.unselected_circle).into(img_pending_orders);
                Picasso.with(ExchangeOrderHistoryActivity.this).load(R.drawable.unselected_circle).into(img_cancelled_orders);
                Picasso.with(ExchangeOrderHistoryActivity.this).load(R.drawable.unselected_circle).into(img_all_orders);
                txt_filterName.setText(getResources().getText(R.string.executed_orders));
                if (allExecuted̥Orders.size() > 0) {
                    lnr_no_trans.setVisibility(View.GONE);
                    lnr_trans_avail.setVisibility(View.VISIBLE);
                    exchangeOrderHistoryRAdapter = new ExchangeOrderHistoryRAdapter(ExchangeOrderHistoryActivity.this, allExecuted̥Orders, false);
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
                editor.putString(CONSTANTS.selFilter, getResources().getText(R.string.pending_orders).toString());
                editor.apply();
                Picasso.with(ExchangeOrderHistoryActivity.this).load(R.drawable.unselected_circle).into(img_open_pending_orders);
                Picasso.with(ExchangeOrderHistoryActivity.this).load(R.drawable.unselected_circle).into(img_executed_orders);
                Picasso.with(ExchangeOrderHistoryActivity.this).load(R.drawable.selected_circle).into(img_pending_orders);
                Picasso.with(ExchangeOrderHistoryActivity.this).load(R.drawable.unselected_circle).into(img_cancelled_orders);
                Picasso.with(ExchangeOrderHistoryActivity.this).load(R.drawable.unselected_circle).into(img_all_orders);
                txt_filterName.setText(getResources().getText(R.string.pending_orders));
                if (allPendingOrders.size() > 0) {
                    lnr_no_trans.setVisibility(View.GONE);
                    lnr_trans_avail.setVisibility(View.VISIBLE);
                    exchangeOrderHistoryRAdapter = new ExchangeOrderHistoryRAdapter(ExchangeOrderHistoryActivity.this, allPendingOrders, false);
                    rview_order_history.setAdapter(exchangeOrderHistoryRAdapter);
                } else {
                    lnr_no_trans.setVisibility(View.VISIBLE);
                    lnr_trans_avail.setVisibility(View.GONE);
                }
                dialog.dismiss();
            }
        });

        lnr_cancelled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString(CONSTANTS.selFilter, getResources().getText(R.string.cancelled_orders).toString());
                editor.apply();
                Picasso.with(ExchangeOrderHistoryActivity.this).load(R.drawable.unselected_circle).into(img_open_pending_orders);
                Picasso.with(ExchangeOrderHistoryActivity.this).load(R.drawable.unselected_circle).into(img_executed_orders);
                Picasso.with(ExchangeOrderHistoryActivity.this).load(R.drawable.unselected_circle).into(img_pending_orders);
                Picasso.with(ExchangeOrderHistoryActivity.this).load(R.drawable.selected_circle).into(img_cancelled_orders);
                Picasso.with(ExchangeOrderHistoryActivity.this).load(R.drawable.unselected_circle).into(img_all_orders);
                txt_filterName.setText(getResources().getText(R.string.cancelled_orders));
                if (allCancelledOrders.size() > 0) {
                    lnr_no_trans.setVisibility(View.GONE);
                    lnr_trans_avail.setVisibility(View.VISIBLE);
                    exchangeOrderHistoryRAdapter = new ExchangeOrderHistoryRAdapter(ExchangeOrderHistoryActivity.this, allCancelledOrders, false);
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
                editor.putString(CONSTANTS.selFilter, getResources().getText(R.string.all_orders).toString());
                editor.apply();
                Picasso.with(ExchangeOrderHistoryActivity.this).load(R.drawable.unselected_circle).into(img_open_pending_orders);
                Picasso.with(ExchangeOrderHistoryActivity.this).load(R.drawable.unselected_circle).into(img_executed_orders);
                Picasso.with(ExchangeOrderHistoryActivity.this).load(R.drawable.unselected_circle).into(img_pending_orders);
                Picasso.with(ExchangeOrderHistoryActivity.this).load(R.drawable.unselected_circle).into(img_cancelled_orders);
                Picasso.with(ExchangeOrderHistoryActivity.this).load(R.drawable.selected_circle).into(img_all_orders);
                txt_filterName.setText(getResources().getText(R.string.all_orders));
                if (allExcOrder.size() > 0) {
                    lnr_no_trans.setVisibility(View.GONE);
                    lnr_trans_avail.setVisibility(View.VISIBLE);
                    exchangeOrderHistoryRAdapter = new ExchangeOrderHistoryRAdapter(ExchangeOrderHistoryActivity.this, allExcOrder, false);
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
            String coin_pair = sharedPreferences.getString(CONSTANTS.selCP, "ETH/BTC");
            JSONObject params = new JSONObject();
            try {
                params.put("coin_pair", coin_pair);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            OrderBookControllerApi apiService = DeviantXApiClient.getClient().create(OrderBookControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.getAll(CONSTANTS.DeviantMulti + token, params.toString());
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
                                            ExcOrders[] coinsStringArray = GsonUtils.getInstance().fromJson(loginResponseData, ExcOrders[].class);
                                            allExcOrder = new ArrayList<ExcOrders>(Arrays.asList(coinsStringArray));

                                            if (allExcOrder.size() > 0) {
                                                allOpenOrders = new ArrayList<>();
                                                allCancelledOrders = new ArrayList<>();
                                                allExecuted̥Orders = new ArrayList<>();
                                                allPendingOrders = new ArrayList<>();

                                                for (int i = 0; i < allExcOrder.size(); i++) {
                                                    if (allExcOrder.get(i).getStr_orderStatus().equals("partially_executed")) {
                                                        allOpenOrders.add(allExcOrder.get(i));
                                                    } else if (allExcOrder.get(i).getStr_orderStatus().equals("pending")) {
                                                        allPendingOrders.add(allExcOrder.get(i));
                                                    } else if (allExcOrder.get(i).getStr_orderStatus().equals("open")) {
                                                        allOpenOrders.add(allExcOrder.get(i));
                                                    } else if (allExcOrder.get(i).getStr_orderStatus().equals("cancelled")) {
                                                        allCancelledOrders.add(allExcOrder.get(i));
                                                    } else if (allExcOrder.get(i).getStr_orderStatus().equals("executed")) {
                                                        allExecuted̥Orders.add(allExcOrder.get(i));
                                                    }
                                                }


                                                if (allExcOrder.size() > 0) {
                                                    txt_filterName.setText(getResources().getText(R.string.all_orders));
                                                    lnr_no_trans.setVisibility(View.GONE);
                                                    lnr_trans_avail.setVisibility(View.VISIBLE);
                                                    exchangeOrderHistoryRAdapter = new ExchangeOrderHistoryRAdapter(ExchangeOrderHistoryActivity.this, allExcOrder, false);
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
                                            CommonUtilities.ShowToastMessage(ExchangeOrderHistoryActivity.this, loginResponseMsg);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                        } else {
                            CommonUtilities.ShowToastMessage(ExchangeOrderHistoryActivity.this, loginResponseMsg);
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        CommonUtilities.ShowToastMessage(ExchangeOrderHistoryActivity.this, getResources().getString(R.string.errortxt));
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        CommonUtilities.ShowToastMessage(ExchangeOrderHistoryActivity.this, getResources().getString(R.string.Timeout));
                    } else if (t instanceof java.net.ConnectException) {
                        CommonUtilities.ShowToastMessage(ExchangeOrderHistoryActivity.this, getResources().getString(R.string.networkerror));
                    } else {
                        CommonUtilities.ShowToastMessage(ExchangeOrderHistoryActivity.this, getResources().getString(R.string.errortxt));
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(ExchangeOrderHistoryActivity.this, getResources().getString(R.string.errortxt));
        }

    }


}
