package com.cryptowallet.deviantx.UI.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.ServiceAPIs.CryptoControllerApi;
import com.cryptowallet.deviantx.ServiceAPIs.OrderBookControllerApi;
import com.cryptowallet.deviantx.ServiceAPIs.WalletControllerApi;
import com.cryptowallet.deviantx.UI.Activities.ExchangeCoinInfoActivity;
import com.cryptowallet.deviantx.UI.Activities.ExchangeOrderHistoryActivity;
import com.cryptowallet.deviantx.UI.Adapters.ExchangeOrderHistoryRAdapter;
import com.cryptowallet.deviantx.UI.Adapters.MarketDephRAdapter;
import com.cryptowallet.deviantx.UI.Adapters.WalletListAirdropRAdapter;
import com.cryptowallet.deviantx.UI.Interfaces.CoinPairSelectableListener;
import com.cryptowallet.deviantx.UI.Interfaces.ExcOrdersUIListener;
import com.cryptowallet.deviantx.UI.Interfaces.WalletSelectableListener;
import com.cryptowallet.deviantx.UI.Models.AccountWallet;
import com.cryptowallet.deviantx.UI.Models.CoinPairs;
import com.cryptowallet.deviantx.UI.Models.ExcOrders;
import com.cryptowallet.deviantx.UI.Models.ExcOrdersDelete;
import com.cryptowallet.deviantx.UI.Models.WalletList;
import com.cryptowallet.deviantx.UI.RoomDatabase.Database.DeviantXDB;
import com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB.ExcOrdersDao;
import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.ExcOrdersDB;
import com.cryptowallet.deviantx.UI.Services.AirdropWalletFetch;
import com.cryptowallet.deviantx.UI.Services.ExcOrdersFetch;
import com.cryptowallet.deviantx.UI.Services.WalletDataFetch;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;
import com.cryptowallet.deviantx.Utilities.DecimalDigitsInputFilter;
import com.cryptowallet.deviantx.Utilities.DeviantXApiClient;
import com.cryptowallet.deviantx.Utilities.GsonUtils;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.squareup.picasso.Picasso;
import com.yarolegovich.discretescrollview.DSVOrientation;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import org.json.JSONArray;
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
import rx.functions.Action1;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.client.StompClient;
import ua.naiksoftware.stomp.client.StompMessage;

import static android.support.constraint.Constraints.TAG;
import static com.cryptowallet.deviantx.Utilities.MyApplication.myApplication;

public class ExchangeTradeFragment extends Fragment implements DiscreteScrollView.OnItemChangedListener<RecyclerView.ViewHolder> {

    @BindView(R.id.img_chart)
    ImageView img_chart;
    @BindView(R.id.img_history)
    ImageView img_history;
    @BindView(R.id.txt_title)
    TextView txt_title;

    @BindView(R.id.rview_bid)
    RecyclerView rview_bid;
    @BindView(R.id.rview_ask)
    RecyclerView rview_ask;
    @BindView(R.id.lnr_dropdown)
    LinearLayout lnr_dropdown;
    @BindView(R.id.img_dropdown)
    ImageView img_dropdown;

    @BindView(R.id.txt_market)
    TextView txt_market;
    @BindView(R.id.rltv_market)
    RelativeLayout rltv_market;
    @BindView(R.id.rltv_market_view)
    RelativeLayout rltv_market_view;
    @BindView(R.id.txt_limit)
    TextView txt_limit;
    @BindView(R.id.rltv_limit)
    RelativeLayout rltv_limit;
    @BindView(R.id.rltv_limit_view)
    RelativeLayout rltv_limit_view;
    @BindView(R.id.img_limit)
    ImageView img_limit;
    @BindView(R.id.txt_stop_limit)
    TextView txt_stop_limit;
    @BindView(R.id.rltv_stop_limit)
    RelativeLayout rltv_stop_limit;
    @BindView(R.id.rltv_stop_limit_view)
    RelativeLayout rltv_stop_limit_view;
    @BindView(R.id.img_stop_limit)
    ImageView img_stop_limit;
    @BindView(R.id.img_market)
    ImageView img_market;
    /*    @BindView(R.id.lnt_btn_market)
        LinearLayout lnt_btn_market;

        @BindView(R.id.lnt_btn_limit)
        LinearLayout lnt_btn_limit;
    */
    @BindView(R.id.txt_btn_buy)
    TextView txt_btn_buy;
    @BindView(R.id.txt_btn_sell)
    TextView txt_btn_sell;
/*
    @BindView(R.id.txt_btn_limit)
    TextView txt_btn_limit;
    @BindView(R.id.txt_btn_stop)
    TextView txt_btn_stop;
*/

    @BindView(R.id.txt_discount)
    TextView txt_discount;

    @BindView(R.id.lnr_minus_price)
    LinearLayout lnr_minus_price;
    @BindView(R.id.edt_price)
    EditText edt_price;
    @BindView(R.id.txt_code_price)
    TextView txt_code_price;
    @BindView(R.id.lnr_plus_price)
    LinearLayout lnr_plus_price;

    @BindView(R.id.lnr_minus_amount)
    LinearLayout lnr_minus_amount;
    @BindView(R.id.edt_amount)
    EditText edt_amount;
    @BindView(R.id.txt_code_amount)
    TextView txt_code_amount;
    @BindView(R.id.lnr_plus_amount)
    LinearLayout lnr_plus_amount;

    @BindView(R.id.edt_pin)
    EditText edt_pin;
    @BindView(R.id.edt_fees)
    EditText edt_fees;
    @BindView(R.id.txt_total)
    TextView txt_total;
    @BindView(R.id.txt_total_code)
    TextView txt_total_code;


    @BindView(R.id.btn_buy)
    Button btn_buy;
    @BindView(R.id.btn_sell)
    Button btn_sell;
    @BindView(R.id.btn_make_order_limit)
    Button btn_make_order_limit;
    @BindView(R.id.btn_make_order_stop)
    Button btn_make_order_stop;

    @BindView(R.id.rview_order_history)
    RecyclerView rview_order_history;
    @BindView(R.id.lnr_no_trans)
    LinearLayout lnr_no_trans;
    @BindView(R.id.pb)
    ProgressBar pb;
    @BindView(R.id.lnr_no_trans_bid)
    LinearLayout lnr_no_trans_bid;
    @BindView(R.id.lnr_no_trans_ask)
    LinearLayout lnr_no_trans_ask;


    @BindView(R.id.lnr_primary_coin_avail)
    LinearLayout lnr_primary_coin_avail;
    @BindView(R.id.lnr_secondary_coin_avail)
    LinearLayout lnr_secondary_coin_avail;
    @BindView(R.id.txt_wallet_name)
    TextView txt_wallet_name;
    @BindView(R.id.txt_secondary_coin_unavail)
    TextView txt_secondary_coin_unavail;
    @BindView(R.id.txt_primary_coin_unavail)
    TextView txt_primary_coin_unavail;
    @BindView(R.id.img_coin_logo)
    ImageView img_coin_logo;

    @BindView(R.id.txt_pcoin_avail_value)
    TextView txt_pcoin_avail_value;
    @BindView(R.id.txt_pcoin_reserve_value)
    TextView txt_pcoin_reserve_value;
    @BindView(R.id.txt_pcoin_total_value)
    TextView txt_pcoin_total_value;
    @BindView(R.id.txt_scoin_avail_value)
    TextView txt_scoin_avail_value;
    @BindView(R.id.txt_scoin_reserve_value)
    TextView txt_scoin_reserve_value;
    @BindView(R.id.txt_scoin_total_value)
    TextView txt_scoin_total_value;

    @BindView(R.id.lnr_stop_limit)
    LinearLayout lnr_stop_limit;
    @BindView(R.id.lnr_minus_stop_price)
    LinearLayout lnr_minus_stop_price;
    @BindView(R.id.lnr_plus_stop_price)
    LinearLayout lnr_plus_stop_price;
    @BindView(R.id.edt_stop_price)
    EditText edt_stop_price;
    @BindView(R.id.txt_code_stop_price)
    TextView txt_code_stop_price;

    //    Modifications for Multiple Wallets
    @BindView(R.id.lnr_arrow)
    LinearLayout lnr_arrow;
    @BindView(R.id.img_arrow)
    ImageView img_arrow;
    @BindView(R.id.lnr_selected_wallet)
    LinearLayout lnr_selected_wallet;
    @BindView(R.id.item_picker)
    DiscreteScrollView itemPicker;
    WalletListAirdropRAdapter walletListAirdropRAdapter;
    ArrayList<WalletList> walletList;
    private int int_data_walletid;
    private double dbl_data_totalBal;
    private boolean defaultWallet = false;
    String str_data_name;
    WalletSelectableListener walletSelectableListener;
    int selectedCoinId = 0;
    String selectedWalletName = "";
    Double selectedWalletBal = 0.0;


    MarketDephRAdapter marketDephRAdapter;
    LinearLayoutManager linearLayoutManagerDephBid, linearLayoutManagerDephAsk, linearLayoutManagerOrdersHistory;

    boolean isShort;
    ArrayList<ExcOrders> bidList, bid;
    ArrayList<ExcOrders> askList, ask;
    ArrayList<ExcOrders> allExcOpenOrders, /*allExcOrders, */
            totalExcOrders;
    ArrayList<AccountWallet> accountWalletlist;
    ArrayList<ExcOrdersDelete> allExcOrders;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;

    boolean isPCoinAvail = false;
    boolean isSCoinAvail = false;
    boolean isBuy = true, isStopLimit = false, isMarket = true;

    ExchangeOrderHistoryRAdapter exchangeOrderHistoryRAdapter;

    String loginResponseData, loginResponseMsg, loginResponseStatus;
    /*ArrayList<CoinPairs> */ CoinPairs allCoinPairs;

    DeviantXDB deviantXDB;

    View view;

    public static StompClient stompClient;

    String myEmail, wallet_name;
    CoinPairSelectableListener coinPairSelectableListener;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.exchange_trade_fragment, container, false);
        ButterKnife.bind(this, view);
        sharedPreferences = getActivity().getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        deviantXDB = DeviantXDB.getDatabase(getActivity());

        allExcOpenOrders = new ArrayList<>();
        allExcOrders = new ArrayList<>();
        totalExcOrders = new ArrayList<>();
        bidList = new ArrayList<>();
        askList = new ArrayList<>();
        isShort = true;
        bid = new ArrayList<>();
        ask = new ArrayList<>();
        accountWalletlist = new ArrayList<>();
/*
        allCoinPairs = new ArrayList<>();
*/
        walletList = new ArrayList<>();

        myEmail = sharedPreferences.getString(CONSTANTS.email, null);
        wallet_name = sharedPreferences.getString(CONSTANTS.defaultWalletName, null);
        txt_wallet_name.setText(wallet_name);

        itemPicker.setOrientation(DSVOrientation.HORIZONTAL);
        itemPicker.addOnItemChangedListener(this);
        walletListAirdropRAdapter = new WalletListAirdropRAdapter(getActivity(), walletList, walletSelectableListener, true);
        itemPicker.setAdapter(walletListAirdropRAdapter);
        itemPicker.setItemTransitionTimeMillis(150);
        itemPicker.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build());

        if (CommonUtilities.isConnectionAvailable(getActivity())) {
            getAllWallets();
        } else {
            CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.internetconnection));
        }
        walletSelectableListener = new WalletSelectableListener() {
            @Override
            public void WalletSelected(ArrayList<WalletList> selected_allWalletList, int pos) {

                int i = 0;
                //  final WalletListDB selectedWallet = new WalletListDB();
                for (WalletList wallets : selected_allWalletList) {
                    if (wallets.getSelected()) {
                        wallets.setSelected(false);
                        walletListAirdropRAdapter.notifyItemChanged(i);
                    }
                    i++;
                }

                walletListAirdropRAdapter.setWalletValue(!selected_allWalletList.get(pos).getSelected(), pos);

                if (selected_allWalletList.get(pos).getSelected()) {
                    selectedCoinId = selected_allWalletList.get(pos).getInt_data_id();
                    selectedWalletName = selected_allWalletList.get(pos).getStr_data_name();
                    selectedWalletBal = selected_allWalletList.get(pos).getDbl_data_totalBal();

                    txt_wallet_name.setText(selectedWalletName);
                    fetchDefAccWal(selectedWalletName);
                    lnr_selected_wallet.setVisibility(View.GONE);
                } else
                    itemPicker.setVisibility(View.VISIBLE);


            }
        };

        disablePrice();

        lnr_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnr_selected_wallet.setVisibility(View.GONE);
                itemPicker.setVisibility(View.VISIBLE);
            }
        });


   /*final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);*/
        if (isStopLimit)
            lnr_stop_limit.setVisibility(View.VISIBLE);
        else
            lnr_stop_limit.setVisibility(View.GONE);


        Bundle bundle = getActivity().getIntent().getExtras();
        try {
//            allCoinPairs = bundle.getParcelableArrayList(CONSTANTS.selectedCoin);
            allCoinPairs = bundle.getParcelable(CONSTANTS.selectedCoin);
        } catch (Exception e) {
            e.printStackTrace();
        }


        edt_price.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(7)});
        edt_stop_price.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(7)});
        edt_amount.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(3)});


        if (allCoinPairs/*.size() > 0*/ != null) {
            edt_price.setText(String.format("%.6f", allCoinPairs.getDbl_previousValue()));
            txt_code_price.setText(allCoinPairs.getStr_exchangeCoin());
            txt_code_stop_price.setText(allCoinPairs.getStr_exchangeCoin());
            txt_code_amount.setText(allCoinPairs.getStr_pairCoin());
            txt_title.setText(allCoinPairs.getStr_pairCoin() + "/" + allCoinPairs.getStr_exchangeCoin());
            txt_total.setText(String.format("%.6f", allCoinPairs.getDbl_previousValue() * 0)/*+" "+allCoinPairs.getStr_exchangeCoin()*/);
            txt_total_code.setText(allCoinPairs.getStr_exchangeCoin());
        } else {
            edt_price.setText("0.0389");
            txt_code_price.setText("BTC");
            txt_code_stop_price.setText("BTC");
            txt_code_amount.setText("ETH");
            txt_title.setText("ETH/BTC");
            txt_total.setText("0");
            edt_price.setEnabled(false);
            txt_total_code.setText("BTC");
        }
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            public void run() {
//                onLoadOpenOrders();
        if (CommonUtilities.isConnectionAvailable(getActivity())) {
            fetchOpenOrders();
            fetchOrdersWS(txt_title.getText().toString().trim());
            fetchDefAccWal(wallet_name);
            btn_buy.setVisibility(View.VISIBLE);
        } else {
            CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.internetconnection));
        }
//            }
//        }, 200);

        coinPairSelectableListener = new CoinPairSelectableListener() {
            @Override
            public void PairSelected(ArrayList<ExcOrders> excOrdersList, int pos, boolean isBid) {
                String beforeSlash = excOrdersList.get(pos).getStr_coinPair().split("/")[0];
                String afterSlash = excOrdersList.get(pos).getStr_coinPair().split("/")[1];
                txt_code_price.setText(afterSlash);
                txt_code_stop_price.setText(afterSlash);
                txt_code_amount.setText(beforeSlash);
                txt_total_code.setText(afterSlash);

                edt_price.setText(String.format("%.6f", excOrdersList.get(pos).getDbl_price()));
                edt_amount.setText(String.format("%.3f", excOrdersList.get(pos).getDbl_amount()));
                txt_total.setText(String.format("%.6f", excOrdersList.get(pos).getDbl_total()));

/*
                txt_market.setTextColor(getResources().getColor(R.color.yellow));
                rltv_market_view.setVisibility(View.VISIBLE);
                img_market.setImageDrawable(getResources().getDrawable(R.drawable.selected_market));
                txt_limit.setTextColor(getResources().getColor(R.color.white));
                rltv_limit_view.setVisibility(View.GONE);
                img_limit.setImageDrawable(getResources().getDrawable(R.drawable.unselected_limit));
                txt_stop_limit.setTextColor(getResources().getColor(R.color.white));
                rltv_stop_limit_view.setVisibility(View.GONE);
                img_stop_limit.setImageDrawable(getResources().getDrawable(R.drawable.unselected_limit));
*/

                disablePrice();
                if (isBid) {
                    //                    Sell
                    txt_btn_buy.setBackground(getResources().getDrawable(R.drawable.unselected));
                    txt_btn_sell.setBackground(getResources().getDrawable(R.drawable.selected_sell));

                    isBuy = false;
                    if (isPCoinAvail && isSCoinAvail) {
                        buttonsVisiblity();
                        buttonsEnable();
                        if (isMarket)
                            btn_sell.setVisibility(View.VISIBLE);
                        else if (isStopLimit)
                            btn_make_order_stop.setVisibility(View.VISIBLE);
                        else
                            btn_make_order_limit.setVisibility(View.VISIBLE);
                    } else {
                        buttonsDisable();
                    }

                    if (isStopLimit)
                        lnr_stop_limit.setVisibility(View.VISIBLE);
                    else
                        lnr_stop_limit.setVisibility(View.GONE);

                } else {
                    //                   Buy
                    txt_btn_buy.setBackground(getResources().getDrawable(R.drawable.selected_buy));
                    txt_btn_sell.setBackground(getResources().getDrawable(R.drawable.unselected));

                    isBuy = true;
                    buttonsVisiblity();
                    if (isPCoinAvail && isSCoinAvail) {
                        buttonsVisiblity();
                        buttonsEnable();
                        if (isMarket)
                            btn_buy.setVisibility(View.VISIBLE);
                        else if (isStopLimit)
                            btn_make_order_limit.setVisibility(View.VISIBLE);
                        else
                            btn_make_order_stop.setVisibility(View.VISIBLE);
                    } else {
                        buttonsDisable();
                    }

                    if (isStopLimit)
                        lnr_stop_limit.setVisibility(View.VISIBLE);
                    else
                        lnr_stop_limit.setVisibility(View.GONE);

                }


            }
        };

        linearLayoutManagerDephBid = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rview_bid.setLayoutManager(linearLayoutManagerDephBid);
        linearLayoutManagerDephAsk = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rview_ask.setLayoutManager(linearLayoutManagerDephAsk);
        linearLayoutManagerOrdersHistory = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rview_order_history.setLayoutManager(linearLayoutManagerOrdersHistory);


        marketDephRAdapter = new MarketDephRAdapter(getActivity(), true, bidList, askList, isShort, coinPairSelectableListener, true);
        rview_bid.setAdapter(marketDephRAdapter);
        marketDephRAdapter = new MarketDephRAdapter(getActivity(), false, bidList, askList, isShort, coinPairSelectableListener, true);
        rview_ask.setAdapter(marketDephRAdapter);
        exchangeOrderHistoryRAdapter = new ExchangeOrderHistoryRAdapter(getActivity(), allExcOpenOrders, true);
        rview_order_history.setAdapter(exchangeOrderHistoryRAdapter);


        img_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ExchangeOrderHistoryActivity.class);
                startActivity(intent);
            }
        });

        img_chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ExchangeCoinInfoActivity.class);
                Bundle bundle = new Bundle();
                if (allCoinPairs == null) {
                    allCoinPairs = new CoinPairs();
                    allCoinPairs.setStr_pairCoin("ETH");
                    allCoinPairs.setDbl_currentValue(0.0389);
                    allCoinPairs.setDbl_previousValue(0.0389);
                    allCoinPairs.setDbl_twentyFourChange(0.0);
                    allCoinPairs.setDbl_twentyFourChangePercentage(0.0);
                    allCoinPairs.setStr_exchangeCoin("BTC");
                    allCoinPairs.setDbl_volume(151069.0);
                    allCoinPairs.setDbl_twentyFourChangeUsd(0.0);
                    bundle.putParcelable(CONSTANTS.selectedCoin, allCoinPairs);
                } else {
                    bundle.putParcelable(CONSTANTS.selectedCoin, allCoinPairs);
                }
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        lnr_dropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShort) {
                    isShort = false;
                    Picasso.with(getActivity()).load(R.drawable.up_yellow).into(img_dropdown);

                    marketDephRAdapter = new MarketDephRAdapter(getActivity(), true, bidList, askList, isShort, coinPairSelectableListener, true);
                    rview_bid.setAdapter(marketDephRAdapter);
                    marketDephRAdapter = new MarketDephRAdapter(getActivity(), false, bidList, askList, isShort, coinPairSelectableListener, true);
                    rview_ask.setAdapter(marketDephRAdapter);
                } else {
                    isShort = true;
                    Picasso.with(getActivity()).load(R.drawable.down_yellow).into(img_dropdown);

                    marketDephRAdapter = new MarketDephRAdapter(getActivity(), true, bidList, askList, isShort, coinPairSelectableListener, true);
                    rview_bid.setAdapter(marketDephRAdapter);
                    marketDephRAdapter = new MarketDephRAdapter(getActivity(), false, bidList, askList, isShort, coinPairSelectableListener, true);
                    rview_ask.setAdapter(marketDephRAdapter);
                }
            }
        });

        rltv_market.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_market.setTextColor(getResources().getColor(R.color.yellow));
                rltv_market_view.setVisibility(View.VISIBLE);
                img_market.setImageDrawable(getResources().getDrawable(R.drawable.selected_market));
                txt_limit.setTextColor(getResources().getColor(R.color.white));
                rltv_limit_view.setVisibility(View.GONE);
                img_limit.setImageDrawable(getResources().getDrawable(R.drawable.unselected_limit));
                txt_stop_limit.setTextColor(getResources().getColor(R.color.white));
                rltv_stop_limit_view.setVisibility(View.GONE);
                img_stop_limit.setImageDrawable(getResources().getDrawable(R.drawable.unselected_limit));


/*
                lnt_btn_market.setVisibility(View.VISIBLE);
                lnt_btn_limit.setVisibility(View.GONE);
*/

/*
                txt_btn_limit.setBackground(getResources().getDrawable(R.drawable.selected_buy));
                txt_btn_stop.setBackground(getResources().getDrawable(R.drawable.unselected));
*/
                txt_btn_buy.setBackground(getResources().getDrawable(R.drawable.selected_buy));
                txt_btn_sell.setBackground(getResources().getDrawable(R.drawable.unselected));

                isMarket = true;
                isStopLimit = false;
                isBuy = true;
                if (isPCoinAvail && isSCoinAvail) {
                    buttonsEnable();
                    buttonsVisiblity();
                    btn_buy.setVisibility(View.VISIBLE);
                } else {
                    buttonsVisiblity();
                    btn_buy.setVisibility(View.VISIBLE);
                    buttonsDisable();
                }
                disablePrice();

            }
        });

        rltv_limit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_limit.setTextColor(getResources().getColor(R.color.yellow));
                rltv_limit_view.setVisibility(View.VISIBLE);
                img_limit.setImageDrawable(getResources().getDrawable(R.drawable.selected_limit));
                txt_market.setTextColor(getResources().getColor(R.color.white));
                rltv_market_view.setVisibility(View.GONE);
                img_market.setImageDrawable(getResources().getDrawable(R.drawable.unselected_market));
                txt_stop_limit.setTextColor(getResources().getColor(R.color.white));
                rltv_stop_limit_view.setVisibility(View.GONE);
                img_stop_limit.setImageDrawable(getResources().getDrawable(R.drawable.unselected_limit));


/*
                lnt_btn_market.setVisibility(View.GONE);
                lnt_btn_limit.setVisibility(View.VISIBLE);
*/
                txt_btn_buy.setBackground(getResources().getDrawable(R.drawable.selected_buy));
                txt_btn_sell.setBackground(getResources().getDrawable(R.drawable.unselected));

                isBuy = true;
                isMarket = false;
                isStopLimit = false;
                if (isPCoinAvail && isSCoinAvail) {
                    buttonsEnable();
                    buttonsVisiblity();
                    btn_make_order_limit.setVisibility(View.VISIBLE);
                } else {
                    buttonsVisiblity();
                    btn_make_order_limit.setVisibility(View.VISIBLE);
                    buttonsDisable();
                }
                enablePrice();
            }
        });

        rltv_stop_limit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_stop_limit.setTextColor(getResources().getColor(R.color.yellow));
                rltv_stop_limit_view.setVisibility(View.VISIBLE);
                img_stop_limit.setImageDrawable(getResources().getDrawable(R.drawable.selected_limit));
                txt_market.setTextColor(getResources().getColor(R.color.white));
                rltv_market_view.setVisibility(View.GONE);
                img_market.setImageDrawable(getResources().getDrawable(R.drawable.unselected_market));
                txt_limit.setTextColor(getResources().getColor(R.color.white));
                rltv_limit_view.setVisibility(View.GONE);
                img_limit.setImageDrawable(getResources().getDrawable(R.drawable.unselected_limit));

/*
                lnt_btn_market.setVisibility(View.GONE);
                lnt_btn_limit.setVisibility(View.VISIBLE);
*/

                txt_btn_buy.setBackground(getResources().getDrawable(R.drawable.selected_buy));
                txt_btn_sell.setBackground(getResources().getDrawable(R.drawable.unselected));

                isMarket = false;
                isStopLimit = true;
                isBuy = true;
                if (isPCoinAvail && isSCoinAvail) {
                    buttonsEnable();
                    buttonsVisiblity();
                    btn_make_order_stop.setVisibility(View.VISIBLE);
                } else {
                    buttonsVisiblity();
                    btn_make_order_stop.setVisibility(View.VISIBLE);
                    buttonsDisable();
                }
                enablePrice();
                lnr_stop_limit.setVisibility(View.VISIBLE);
            }
        });


        txt_btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_btn_buy.setBackground(getResources().getDrawable(R.drawable.selected_buy));
                txt_btn_sell.setBackground(getResources().getDrawable(R.drawable.unselected));

                disablePrice();
                isBuy = true;

                if (isPCoinAvail && isSCoinAvail) {
                    buttonsVisiblity();
                    buttonsEnable();
                    if (isMarket) {
                        edt_price.setEnabled(false);
                        btn_buy.setVisibility(View.VISIBLE);
                    } else if (isStopLimit) {
                        edt_price.setEnabled(true);
                        btn_make_order_stop.setVisibility(View.VISIBLE);
                    } else {
                        edt_price.setEnabled(true);
                        btn_make_order_limit.setVisibility(View.VISIBLE);
                    }
                } else {
                    buttonsVisiblity();
                    buttonsDisable();
                    if (isMarket) {
                        edt_price.setEnabled(false);
                        btn_buy.setVisibility(View.VISIBLE);
                    } else if (isStopLimit) {
                        edt_price.setEnabled(true);
                        btn_make_order_stop.setVisibility(View.VISIBLE);
                    } else {
                        edt_price.setEnabled(true);
                        btn_make_order_limit.setVisibility(View.VISIBLE);
                    }
                }

                if (isStopLimit)
                    lnr_stop_limit.setVisibility(View.VISIBLE);
                else
                    lnr_stop_limit.setVisibility(View.GONE);


            }
        });

        txt_btn_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_btn_buy.setBackground(getResources().getDrawable(R.drawable.unselected));
                txt_btn_sell.setBackground(getResources().getDrawable(R.drawable.selected_sell));
                isBuy = false;

                disablePrice();
                if (isPCoinAvail && isSCoinAvail) {
                    buttonsEnable();
                    buttonsVisiblity();
                    if (isMarket) {
                        edt_price.setEnabled(false);
                        btn_sell.setVisibility(View.VISIBLE);
                    } else if (isStopLimit) {
                        edt_price.setEnabled(true);
                        btn_make_order_stop.setVisibility(View.VISIBLE);
                    } else {
                        edt_price.setEnabled(true);
                        btn_make_order_limit.setVisibility(View.VISIBLE);
                    }
                } else {
                    buttonsDisable();
                    buttonsVisiblity();
                    if (isMarket) {
                        edt_price.setEnabled(false);
                        btn_sell.setVisibility(View.VISIBLE);
                    } else if (isStopLimit) {
                        edt_price.setEnabled(true);
                        btn_make_order_stop.setVisibility(View.VISIBLE);
                    } else {
                        edt_price.setEnabled(true);
                        btn_make_order_limit.setVisibility(View.VISIBLE);
                    }
                }

                if (isStopLimit)
                    lnr_stop_limit.setVisibility(View.VISIBLE);
                else
                    lnr_stop_limit.setVisibility(View.GONE);

            }
        });

/*
        txt_btn_limit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_btn_limit.setBackground(getResources().getDrawable(R.drawable.selected_buy));
                txt_btn_stop.setBackground(getResources().getDrawable(R.drawable.unselected));

                enablePrice();
                buttonsVisiblity();
                if (isPCoinAvail && isSCoinAvail)
                    btn_make_order_limit.setVisibility(View.VISIBLE);
            }
        });

        txt_btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_btn_limit.setBackground(getResources().getDrawable(R.drawable.unselected));
                txt_btn_stop.setBackground(getResources().getDrawable(R.drawable.selected_sell));

                enablePrice();
                buttonsVisiblity();
                if (isPCoinAvail && isSCoinAvail)
                    btn_make_order_stop.setVisibility(View.VISIBLE);

            }
        });
*/

        lnr_plus_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String edtValue = edt_price.getText().toString().trim();
                Double edtVal = Double.parseDouble(edtValue);
//                Double txtVal = Double.parseDouble(txt_avail_coins.getText().toString().trim());
                edtVal++;
//                if (edtVal > txtVal) {
//                    CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.insufficient_fund));
//                } else {
                edt_price.setText("" + edtVal);
//                }
            }
        });

        lnr_minus_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String edt_value = edt_price.getText().toString().trim();
                Double edtVal = Double.parseDouble(edt_value);
                edtVal--;
                if (edtVal > 0) {
                    edt_price.setText("" + edtVal);
                } else {
                    edt_price.setText("0.0");
                }
            }
        });

        lnr_plus_stop_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String edtValue = edt_stop_price.getText().toString().trim();
                Double edtVal = Double.parseDouble(edtValue);
//                Double txtVal = Double.parseDouble(txt_avail_coins.getText().toString().trim());
                edtVal++;
//                if (edtVal > txtVal) {
//                    CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.insufficient_fund));
//                } else {
                edt_stop_price.setText("" + edtVal);
//                }
            }
        });

        lnr_minus_stop_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String edt_value = edt_stop_price.getText().toString().trim();
                Double edtVal = Double.parseDouble(edt_value);
                edtVal--;
                if (edtVal > 0) {
                    edt_stop_price.setText("" + edtVal);
                } else {
                    edt_stop_price.setText("0.0");
                }
            }
        });

        lnr_plus_amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String edtValue = edt_amount.getText().toString().trim();
                Double edtVal = Double.parseDouble(edtValue);
//                Double txtVal = Double.parseDouble(txt_avail_coins.getText().toString().trim());
                edtVal++;
//                if (edtVal > txtVal) {
//                    CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.insufficient_fund));
//                } else {
                edt_amount.setText("" + edtVal);
//                }
            }
        });

        lnr_minus_amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String edt_value = edt_amount.getText().toString().trim();
                Double edtVal = Double.parseDouble(edt_value);
                edtVal--;
                if (edtVal > 0) {
                    edt_amount.setText("" + edtVal);
                } else {
                    edt_amount.setText("0.0");
                }

            }
        });


        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPCoinAvail && isSCoinAvail) {
                    String coin_pair = txt_title.getText().toString().trim();
                    String type = "buy";
                    if (edt_price.getText().toString().trim() != null) {
                        if (edt_amount.getText().toString().trim() != null) {
                            double price = Double.parseDouble(edt_price.getText().toString().trim());
                            double amount = Double.parseDouble(edt_amount.getText().toString().trim());
                            double total = Double.parseDouble(txt_total.getText().toString().trim())/*0.0*//*price * amount*/;
                            if (price > 0/*.001*/) {
                                if (amount > 0/*.001*/) {
                                    confirmOrderDialog(amount, price, total, type, coin_pair, wallet_name, "market", 0.0);
//                                makeOrder(amount, price, total, type, coin_pair, wallet_name, "market", 0.0);
                                } else {
                                    CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.invalid_amount));
                                }
                            } else {
                                CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.invalid_price));
                            }
                        } else {
                            CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.empty_amount));
                        }
                    } else {
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.empty_price));
                    }
                } else {
                    CommonUtilities.ShowToastMessage(getActivity(), getActivity().getResources().getString(R.string.pls_add_coin) + "\nTo " + getActivity().getResources().getString(R.string.def_wallet));
                }
            }
        });

        btn_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPCoinAvail && isSCoinAvail) {
                    String coin_pair = txt_title.getText().toString().trim();
                    String type = "sell";
                    if (edt_price.getText().toString().trim() != null) {
                        if (edt_amount.getText().toString().trim() != null) {
                            double price = Double.parseDouble(edt_price.getText().toString().trim());
                            double amount = Double.parseDouble(edt_amount.getText().toString().trim());
                            double total = Double.parseDouble(txt_total.getText().toString().trim());
                            if (price > 0/*.001*/) {
                                if (amount > 0/*.001*/) {
                                    confirmOrderDialog(amount, price, total, type, coin_pair, wallet_name, "market", 0.0);
//                                makeOrder(amount, price, total, type, coin_pair, wallet_name, "market", 0.0);
                                } else {
                                    CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.invalid_amount));
                                }
                            } else {
                                CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.invalid_price));
                            }
                        } else {
                            CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.empty_amount));
                        }
                    } else {
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.empty_price));
                    }
                } else {
                    CommonUtilities.ShowToastMessage(getActivity(), getActivity().getResources().getString(R.string.pls_add_coin) + "\nTo " + getActivity().getResources().getString(R.string.def_wallet));
                }
            }
        });

        btn_make_order_limit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPCoinAvail && isSCoinAvail) {
                    String coin_pair = txt_title.getText().toString().trim();
                    String type;
                    if (isBuy)
                        type = "buy";
                    else
                        type = "sell";

                    if (edt_price.getText().toString().trim() != null) {
                        if (edt_amount.getText().toString().trim() != null) {
                            double price = Double.parseDouble(edt_price.getText().toString().trim());
                            double amount = Double.parseDouble(edt_amount.getText().toString().trim());
                            double total = Double.parseDouble(txt_total.getText().toString().trim())/*0.0*//*price * amount*/;
                            if (price > 0/*.001*/) {
                                if (amount > 0/*.001*/) {
                                    confirmOrderDialog(amount, price, total, type, coin_pair, wallet_name, "limit", 0.0);
//                                makeOrder(amount, price, total, type, coin_pair, wallet_name, "limit", 0.0);
                                } else {
                                    CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.invalid_amount));
                                }
                            } else {
                                CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.invalid_price));
                            }
                        } else {
                            CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.empty_amount));
                        }
                    } else {
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.empty_price));
                    }
                } else {
                    CommonUtilities.ShowToastMessage(getActivity(), getActivity().getResources().getString(R.string.pls_add_coin) + "\nTo " + getActivity().getResources().getString(R.string.def_wallet));
                }

            }
        });

        btn_make_order_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPCoinAvail && isSCoinAvail) {
                    String coin_pair = txt_title.getText().toString().trim();
                    String type;
                    if (isBuy)
                        type = "buy";
                    else
                        type = "sell";

                    if (edt_price.getText().toString().trim() != null) {
                        if (edt_amount.getText().toString().trim() != null) {
                            if (edt_stop_price.getText().toString().trim() != null) {
                                double price = Double.parseDouble(edt_price.getText().toString().trim());
                                double amount = Double.parseDouble(edt_amount.getText().toString().trim());
                                double total = Double.parseDouble(txt_total.getText().toString().trim());
                                double stop_price = Double.parseDouble(edt_stop_price.getText().toString().trim());
                                if (price > 0/*.001*/) {
                                    if (amount > 0/*.001*/) {
                                        confirmOrderDialog(amount, price, total, type, coin_pair, wallet_name, "stop_limit", stop_price);
//                                    makeOrder(amount, price, total, type, coin_pair, wallet_name, "stop_limit", stop_price);
                                    } else {
                                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.invalid_amount));
                                    }
                                } else {
                                    CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.invalid_price));
                                }
                            } else {
                                CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.empty_stop_price));
                            }
                        } else {
                            CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.empty_amount));
                        }
                    } else {
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.empty_price));
                    }
                } else {
                    CommonUtilities.ShowToastMessage(getActivity(), getActivity().getResources().getString(R.string.pls_add_coin) + "\nTo " + getActivity().getResources().getString(R.string.def_wallet));
                }
            }
        });

        edt_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String amountTextValue = s.toString();
                if (!amountTextValue.trim().isEmpty()) {
                    try {
                        double price = Double.parseDouble(edt_price.getText().toString().trim());
                        double amount = Double.parseDouble(amountTextValue);
                        txt_total.setText(String.format("%.6f", amount * price));
/*
                        if (amount > 0) {
                            Double finalValue = Double.parseDouble(amountTextValue);
                            avail_bal = dividendAirdrops.getDbl_airdropAmount();
                            if (avail_bal < finalValue) {
                                CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.insufficient_fund));
                                edt_amount.setText("0");
                            }
                        }
*/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    edt_amount.setText("0");
                    txt_total.setText("0");
                    CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.enter_amount));
                }
            }
        });

        edt_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String priceTextValue = s.toString();
                if (!priceTextValue.trim().isEmpty()) {
                    try {
                        double amount = Double.parseDouble(edt_amount.getText().toString().trim());
                        double price = Double.parseDouble(priceTextValue);
                        txt_total.setText(String.format("%.6f", amount * price));
/*
                        if (amount > 0) {
                            Double finalValue = Double.parseDouble(amountTextValue);
                            avail_bal = dividendAirdrops.getDbl_airdropAmount();
                            if (avail_bal < finalValue) {
                                CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.insufficient_fund));
                                edt_amount.setText("0");
                            }
                        }
*/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    edt_price.setText("0");
                    txt_total.setText("0");
                    CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.enter_price));
                }
            }
        });

        return view;
    }

    private void confirmOrderDialog(double amount, double price, double total, String type, String coin_pair, String wallet_name, String market, double stop_price) {
        //                Creating A Custom Dialog Using DialogPlus
        ViewHolder viewHolder = new ViewHolder(R.layout.dialog_make_order);
        final DialogPlus dialog = DialogPlus.newDialog(getActivity())
                .setContentHolder(viewHolder)
                .setGravity(Gravity.BOTTOM)
                .setCancelable(true)
                .setInAnimation(R.anim.slide_in_bottom)
                .setOutAnimation(R.anim.slide_out_bottom)
                .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .create();

//                Initializing Widgets
        View view = dialog.getHolderView();
        TextView txt_cancel = view.findViewById(R.id.txt_cancel);
        TextView txt_confirm = view.findViewById(R.id.txt_confirm);
        TextView txt_amount = view.findViewById(R.id.txt_amount);
        TextView txt_amount_code = view.findViewById(R.id.txt_amount_code);
        TextView txt_price = view.findViewById(R.id.txt_price);
        TextView txt_price_code = view.findViewById(R.id.txt_price_code);
        LinearLayout lnr_stop_price = view.findViewById(R.id.lnr_stop_price);
        TextView txt_stop_price = view.findViewById(R.id.txt_stop_price);
        TextView txt_stop_price_code = view.findViewById(R.id.txt_stop_price_code);
        TextView txt_total = view.findViewById(R.id.txt_total);
        TextView txt_total_code = view.findViewById(R.id.txt_total_code);
        TextView txt_wallet_name = view.findViewById(R.id.txt_wallet_name);

        if (isStopLimit) {
            lnr_stop_price.setVisibility(View.VISIBLE);
            txt_stop_price.setText(String.format("%.6f", stop_price));
            if (allCoinPairs/*.size() > 0*/ != null) {
                txt_amount_code.setText(allCoinPairs.getStr_pairCoin());
                txt_price_code.setText(allCoinPairs.getStr_exchangeCoin());
                txt_stop_price_code.setText(allCoinPairs.getStr_exchangeCoin());
                txt_total_code.setText(allCoinPairs.getStr_exchangeCoin());
            } else {
                txt_amount_code.setText("ETH");
                txt_price_code.setText("BTC");
                txt_stop_price_code.setText("BTC");
                txt_total_code.setText("BTC");

            }
        } else {
            lnr_stop_price.setVisibility(View.GONE);
        }

        if (allCoinPairs/*.size() > 0*/ != null) {
            txt_amount_code.setText(allCoinPairs.getStr_pairCoin());
            txt_price_code.setText(allCoinPairs.getStr_exchangeCoin());
            txt_stop_price_code.setText(allCoinPairs.getStr_exchangeCoin());
            txt_total_code.setText(allCoinPairs.getStr_exchangeCoin());
        } else {
            txt_amount_code.setText("ETH");
            txt_price_code.setText("BTC");
            txt_stop_price_code.setText("BTC");
            txt_total_code.setText("BTC");

        }


        txt_amount.setText(String.format("%.3f", amount));
        txt_price.setText(String.format("%.6f", price));
        txt_total.setText(String.format("%.6f", total));
        txt_wallet_name.setText(wallet_name);

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        txt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeOrder(amount, price, total, type, coin_pair, wallet_name, market, stop_price);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void enablePrice() {
        edt_price.setEnabled(true);
        lnr_minus_price.setEnabled(true);
        lnr_plus_price.setEnabled(true);
        lnr_stop_limit.setVisibility(View.GONE);
    }

    private void disablePrice() {
        edt_price.setEnabled(false);
        lnr_minus_price.setEnabled(false);
        lnr_plus_price.setEnabled(false);
        lnr_stop_limit.setVisibility(View.GONE);
    }


    private void buttonsVisiblity() {
        btn_buy.setVisibility(View.GONE);
        btn_sell.setVisibility(View.GONE);
        btn_make_order_limit.setVisibility(View.GONE);
        btn_make_order_stop.setVisibility(View.GONE);
        lnr_stop_limit.setVisibility(View.GONE);
    }

    private void buttonsDisable() {
/*
        btn_buy.setEnabled(false);
        btn_sell.setEnabled(false);
        btn_make_order_limit.setEnabled(false);
        btn_make_order_stop.setEnabled(false);
        lnr_stop_limit.setEnabled(false);
*/
        btn_buy.setBackground(getActivity().getResources().getDrawable(R.drawable.rec_orange_gradient_lyt));
        btn_sell.setBackground(getActivity().getResources().getDrawable(R.drawable.rec_orange_gradient_lyt));
        btn_make_order_limit.setBackground(getActivity().getResources().getDrawable(R.drawable.rec_orange_gradient_lyt));
        btn_make_order_stop.setBackground(getActivity().getResources().getDrawable(R.drawable.rec_orange_gradient_lyt));
//        lnr_stop_limit.setBackground(getActivity().getResources().getDrawable(R.drawable.rec_orange_gradient_lyt));

//        CommonUtilities.ShowToastMessage(getActivity(),getActivity().getResources().getString(R.string.pls_add_coin)+"\nTo "+getActivity().getResources().getString(R.string.def_wallet));
    }

    private void buttonsEnable() {
        btn_buy.setEnabled(true);
        btn_sell.setEnabled(true);
        btn_make_order_limit.setEnabled(true);
        btn_make_order_stop.setEnabled(true);
        lnr_stop_limit.setEnabled(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        myApplication.setExcOrdersUIListener(excOrdersUIListener);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getActivity().startForegroundService(new Intent(getActivity(), WalletDataFetch.class));
            getActivity().startForegroundService(new Intent(getActivity(), AirdropWalletFetch.class));
            getActivity().startForegroundService(new Intent(getActivity(), ExcOrdersFetch.class));

        } else {
            getActivity().startService(new Intent(getActivity(), WalletDataFetch.class));
            getActivity().startService(new Intent(getActivity(), AirdropWalletFetch.class));
            getActivity().startService(new Intent(getActivity(), ExcOrdersFetch.class));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myApplication.setExcOrdersUIListener(null);
//        stompClient.disconnect();
    }

    @Override
    public void onPause() {
        super.onPause();
//        stompClient.disconnect();
    }


    //    **************WEBSOCKET FOR ORDERS [ASK/BID]**************
    private void fetchOrdersWS(String title_pair) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
//              Main Link
//                stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://142.93.51.57:3323/ws_v2/deviant/websocket");
                stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "wss://deviantx.app/ws_v2/deviant/websocket");
//              Local Link
//                stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://192.168.0.179:3323/ws_v2/deviant/websocket");
//                stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://192.168.0.111:3323/ws_v2/deviant/websocket");
                stompClient.connect();
                Log.e(TAG, "*****Connected " + "*****: /topic/orderbook");

                allExcOrders = new ArrayList<>();
                rview_ask.setVisibility(View.GONE);
                rview_bid.setVisibility(View.GONE);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stompClient.topic("/topic/orderbook").subscribe(new Action1<StompMessage>() {
                            @Override
                            public void call(StompMessage message) {
                                try {
                                    Log.e(TAG, "*****Received " + "*****: /topic/orderbook" + message.getPayload());
                                    ExcOrdersDelete coinsStringArray = GsonUtils.getInstance().fromJson(message.getPayload(), ExcOrdersDelete.class);
                                    //allExcOrders = new ArrayList<ExcOrdersDelete>(Arrays.asList(coinsStringArray));

                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
//                                            stompClient.disconnect();
//                                            Log.e(TAG, "*****DisConnected " + "*****: /topic/orderbook");
                                            pb.setVisibility(View.VISIBLE);

                                            bid = new ArrayList<>();
                                            ask = new ArrayList<>();
                                            bidList = new ArrayList<>();
                                            askList = new ArrayList<>();

                                            bid = (ArrayList<ExcOrders>) coinsStringArray.getList_bid();
                                            ask = (ArrayList<ExcOrders>) coinsStringArray.getList_ask();

                                            for (int i = 0; i < bid.size(); i++) {
//                                                if (!bid.get(i).getStr_user().equals(myEmail)) {
                                                    if (bid.get(i).getStr_coinPair().trim().equals(title_pair))
                                                        bidList.add(bid.get(i));
//                                                }
                                            }
                                            for (int i = 0; i < ask.size(); i++) {
//                                                if (!ask.get(i).getStr_user().equals(myEmail)) {
                                                    if (ask.get(i).getStr_coinPair().trim().equals(title_pair))
                                                        askList.add(ask.get(i));
//                                                }
                                            }

                                            if (bidList.size() > 0) {
                                                marketDephRAdapter = new MarketDephRAdapter(getActivity(), true, bidList, askList, isShort, coinPairSelectableListener, true);
                                                rview_bid.setAdapter(marketDephRAdapter);
                                                rview_bid.setVisibility(View.VISIBLE);
                                                lnr_no_trans_bid.setVisibility(View.GONE);
                                            } else {
                                                rview_bid.setVisibility(View.GONE);
                                                lnr_no_trans_bid.setVisibility(View.VISIBLE);
                                            }

                                            if (askList.size() > 0) {
                                                marketDephRAdapter = new MarketDephRAdapter(getActivity(), false, bidList, askList, isShort, coinPairSelectableListener, true);
                                                rview_ask.setAdapter(marketDephRAdapter);
                                                rview_ask.setVisibility(View.VISIBLE);
                                                lnr_no_trans_ask.setVisibility(View.GONE);
                                            } else {
                                                rview_ask.setVisibility(View.GONE);
                                                lnr_no_trans_ask.setVisibility(View.VISIBLE);
                                            }

                                            pb.setVisibility(View.GONE);
                                        }
                                    });

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });

                    }
                });


            }
        });
    }


    //    **************MAKING ORDER**************
    private void makeOrder(double amount, double price, double total, String type, String coin_pair, String wallet_name, String tx_type, double stop_price) {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            JSONObject params = new JSONObject();
            try {
                params.put("amount", amount);
                params.put("price", price);
                params.put("total", total);
                params.put("type", type);
                params.put("coin_pair", coin_pair);
                params.put("wallet", wallet_name);
                params.put("tx_type", tx_type);
                params.put("stop_price", stop_price);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialog = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.please_wait), true);
            OrderBookControllerApi apiService = DeviantXApiClient.getClient().create(OrderBookControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.getOrder(CONSTANTS.DeviantMulti + token, params.toString());
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

                                fetchDefAccWal(wallet_name);

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    getActivity().startForegroundService(new Intent(getActivity(), WalletDataFetch.class));
                                    getActivity().startForegroundService(new Intent(getActivity(), AirdropWalletFetch.class));
                                    getActivity().startForegroundService(new Intent(getActivity(), ExcOrdersFetch.class));

                                } else {
                                    getActivity().startService(new Intent(getActivity(), WalletDataFetch.class));
                                    getActivity().startService(new Intent(getActivity(), AirdropWalletFetch.class));
                                    getActivity().startService(new Intent(getActivity(), ExcOrdersFetch.class));
                                }
/*
                                Intent serviceIntent = new Intent(getActivity(), ExcOrdersFetch.class);
                                getActivity().startService(serviceIntent);
                                Intent serviceIntent1 = new Intent(getApplicationContext(), WalletDataFetch.class);
                                serviceIntent1.putExtra("walletName", "");
                                getActivity().startService(serviceIntent1);
*/
                                CommonUtilities.ShowToastMessage(getActivity(), loginResponseMsg);

                            } else {
                                CommonUtilities.ShowToastMessage(getActivity(), loginResponseMsg);
                            }
                        } else {
                            progressDialog.dismiss();
                            CommonUtilities.ShowToastMessage(getActivity(), loginResponseMsg);
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }

                    } catch (Exception e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.errortxt));
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.Timeout));
                    } else if (t instanceof java.net.ConnectException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.networkerror));
//                        Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.networkerror), Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.errortxt));
                    }
                }
            });
        } catch (Exception ex) {
            progressDialog.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.errortxt));
//            Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
        }

    }


    //    **************GETTING OPEN ORDERS**************
    private void onLoadOpenOrders() {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ExcOrdersDao excOrdersDao = deviantXDB.excOrdersDao();
                if ((excOrdersDao.getExcOrders()) != null) {
                    String walletResult = excOrdersDao.getExcOrders().excOrders;
                    updateUIOpenOrders(walletResult);
                } else {
                    if (CommonUtilities.isConnectionAvailable(getActivity())) {
                        fetchOpenOrders();
                    } else {
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.internetconnection));
                    }
                }
            }
        });

    }

    ExcOrdersUIListener excOrdersUIListener = new ExcOrdersUIListener() {
        @Override
        public void onChangedExcOrders(String allExcOpenOrderss) {
            updateUIOpenOrders(allExcOpenOrderss);
        }

    };

    private void updateUIOpenOrders(String responsevalue) {
        getActivity().runOnUiThread(new Runnable() {
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

/*
                        ArrayList<ExcOrders> excOrdersDeleteList = new ArrayList<>();
                        for (ExcOrders coinName : allExcOpenOrders) {
                            excOrdersDeleteList.add(coinName);
                        }
*/

                        if (allExcOpenOrders.size() > 0) {
                            lnr_no_trans.setVisibility(View.GONE);
                            rview_order_history.setVisibility(View.VISIBLE);
                            exchangeOrderHistoryRAdapter = new ExchangeOrderHistoryRAdapter(getActivity(), allExcOpenOrders, true);
                            rview_order_history.setAdapter(exchangeOrderHistoryRAdapter);
                        } else {
                            lnr_no_trans.setVisibility(View.VISIBLE);
                            rview_order_history.setVisibility(View.GONE);
                        }
                    } else {
                        CommonUtilities.ShowToastMessage(getActivity(), loginResponseMsg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void fetchOpenOrders() {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            OrderBookControllerApi apiService = DeviantXApiClient.getClient().create(OrderBookControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.getAllOpen(CONSTANTS.DeviantMulti + token);
            apiResponse.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responsevalue = response.body().string();

                        if (!responsevalue.isEmpty() && responsevalue != null) {
                            updateUIOpenOrders(responsevalue);
                            ExcOrdersDao mDao = deviantXDB.excOrdersDao();
                            ExcOrdersDB excOrdersDB = new ExcOrdersDB(1, responsevalue);
                            mDao.insertExcOrders(excOrdersDB);

                        } else {
                            CommonUtilities.ShowToastMessage(getActivity(), loginResponseMsg);
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.errortxt));
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.Timeout));
                    } else if (t instanceof java.net.ConnectException) {
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.networkerror));
                    } else {
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.errortxt));
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.errortxt));
        }

    }


    //    **************GETTING DEFAULT WALLET**************
    private void fetchDefAccWal(String wallet_name) {
        try {
            pb.setVisibility(View.VISIBLE);
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            CryptoControllerApi apiService = DeviantXApiClient.getClient().create(CryptoControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.getAccountWallet(CONSTANTS.DeviantMulti + token, wallet_name);
            apiResponse.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responsevalue = response.body().string();
                        if (!responsevalue.isEmpty() && responsevalue != null) {
                            pb.setVisibility(View.GONE);
                            JSONObject jsonObject = new JSONObject(responsevalue);
                            loginResponseMsg = jsonObject.getString("msg");
                            loginResponseStatus = jsonObject.getString("status");

                            if (loginResponseStatus.equals("true")) {
                                pb.setVisibility(View.GONE);
                                loginResponseData = jsonObject.getString("data");
                                accountWalletlist = new ArrayList<>();
                                AccountWallet[] accountWallets = GsonUtils.getInstance().fromJson(loginResponseData, AccountWallet[].class);
                                accountWalletlist = new ArrayList<AccountWallet>(Arrays.asList(accountWallets));
                                isSCoinAvail = false;
                                isPCoinAvail = false;
                                if (accountWalletlist.size() == 0) {
//                                    buttonsVisiblity();
                                    buttonsDisable();
                                    lnr_primary_coin_avail.setVisibility(View.GONE);
                                    img_coin_logo.setVisibility(View.INVISIBLE);
                                    txt_primary_coin_unavail.setVisibility(View.VISIBLE);
                                } else {
                                    int selected_i = 0;
                                    for (int i = 0; i < accountWalletlist.size(); i++) {
                                        if (accountWalletlist.get(i).getStr_coin_code().trim().equals(txt_code_amount.getText().toString().trim())) {
                                            isPCoinAvail = true;
                                            selected_i = i;
/*
                                            txt_pcoin_avail_value.setText(String.format("%.6f", accountWalletlist.get(i).getStr_data_balance()) + " " + accountWalletlist.get(i).getStr_coin_code());
                                            Picasso.with(getActivity()).load(accountWalletlist.get(i).getStr_coin_logo()).into(img_coin_logo);
*/
                                        }
                                        if (accountWalletlist.get(i).getStr_coin_code().trim().equals(txt_code_price.getText().toString().trim())) {
                                            isSCoinAvail = true;
                                        }
                                    }

                                    if (isPCoinAvail && isSCoinAvail) {
                                        txt_pcoin_avail_value.setText(String.format("%.6f", accountWalletlist.get(selected_i).getStr_data_balance()) + " " + accountWalletlist.get(selected_i).getStr_coin_code());
                                        Picasso.with(getActivity()).load(accountWalletlist.get(selected_i).getStr_coin_logo()).into(img_coin_logo);
                                        buttonsEnable();
                                        lnr_primary_coin_avail.setVisibility(View.VISIBLE);
                                        img_coin_logo.setVisibility(View.VISIBLE);
                                        txt_primary_coin_unavail.setVisibility(View.GONE);
                                    } else {
                                        buttonsDisable();
                                        lnr_primary_coin_avail.setVisibility(View.GONE);
                                        img_coin_logo.setVisibility(View.INVISIBLE);
                                        txt_primary_coin_unavail.setVisibility(View.VISIBLE);
//                                        buttonsVisiblity();
                                    }
                                }
                                lnr_selected_wallet.setVisibility(View.VISIBLE);
                                itemPicker.setVisibility(View.GONE);

                            } else if (loginResponseStatus.equals("401")) {
                                CommonUtilities.sessionExpired(getActivity(), loginResponseMsg);
                            } else {
                                CommonUtilities.ShowToastMessage(getActivity(), loginResponseMsg);
                            }
                        } else {
                            CommonUtilities.ShowToastMessage(getActivity(), loginResponseMsg);
//                            Toast.makeText(getApplicationContext(), responsevalue, Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.errortxt));
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.Timeout));
                    } else if (t instanceof java.net.ConnectException) {
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.networkerror));
                    } else {
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.errortxt));
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.errortxt));
        }

    }


    @Override
    public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {
        if (adapterPosition > -1) {
            wallet_name = walletList.get(adapterPosition).getStr_data_name();
        }
    }

    //    **************GETTING OPEN ORDERS**************
    private void getAllWallets() {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            progressDialog = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.please_wait), true);
            WalletControllerApi apiService = DeviantXApiClient.getClient().create(WalletControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.getAllWallet(CONSTANTS.DeviantMulti + token);
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
//                                int defaultWalletPos = 0;
                                loginResponseData = jsonObject.getString("data");
                                JSONArray jsonArrayData = new JSONArray(loginResponseData);
                                for (int i = 0; i < jsonArrayData.length(); i++) {
                                    JSONObject jsonObjectData = jsonArrayData.getJSONObject(i);
                                    try {
                                        int_data_walletid = jsonObjectData.getInt("id");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        str_data_name = jsonObjectData.getString("name");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        dbl_data_totalBal = jsonObjectData.getDouble("toatalBalance");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        defaultWallet = jsonObjectData.getBoolean("defaultWallet");
                                        if (defaultWallet) {
//                                            defaultWalletPos = i;
                                            editor.putInt(CONSTANTS.defaultWallet, i);
                                            editor.apply();
                                            myApplication.setDefaultWallet(i);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    walletList.add(new WalletList(int_data_walletid, str_data_name, dbl_data_totalBal, defaultWallet));
                                }
                                walletListAirdropRAdapter = new WalletListAirdropRAdapter(getActivity(), walletList, walletSelectableListener, true);
                                itemPicker.setAdapter(walletListAirdropRAdapter);
//                                itemPicker.scrollToPosition(defaultWalletPos);
                                itemPicker.scrollToPosition(myApplication.getDefaultWallet());
                                ArrayList<String> walletNamesList = new ArrayList<>();
                                for (int i = 0; i < walletList.size(); i++) {
                                    walletNamesList.add(walletList.get(i).getStr_data_name());
                                }
//                                spnr_wallets.setAdapter(new SpinnerDaysAdapter(getActivity(), R.layout.spinner_item_days_dropdown, walletNamesList));

                            } else {
                                CommonUtilities.ShowToastMessage(getActivity(), loginResponseMsg);
                            }

                        } else {
                            CommonUtilities.ShowToastMessage(getActivity(), loginResponseMsg);
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.errortxt));
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.Timeout));
                    } else if (t instanceof java.net.ConnectException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.networkerror));
                    } else {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.errortxt));
                    }
                }
            });
        } catch (Exception ex) {
            progressDialog.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.errortxt));
        }

    }

}
