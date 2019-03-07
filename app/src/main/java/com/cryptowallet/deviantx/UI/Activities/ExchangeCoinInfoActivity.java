package com.cryptowallet.deviantx.UI.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.UI.Adapters.MarketDephRAdapter;
import com.cryptowallet.deviantx.UI.Adapters.MarketTradesRAdapter;
import com.cryptowallet.deviantx.UI.Interfaces.CoinPairSelectableListener;
import com.cryptowallet.deviantx.UI.Models.CoinPairs;
import com.cryptowallet.deviantx.UI.Models.ExcOrders;
import com.cryptowallet.deviantx.UI.Models.ExcOrdersDelete;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;
import com.cryptowallet.deviantx.Utilities.GsonUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.client.StompClient;
import ua.naiksoftware.stomp.client.StompMessage;

import static android.support.constraint.Constraints.TAG;

public class ExchangeCoinInfoActivity extends AppCompatActivity {


    @BindView(R.id.toolbar_center_back)
    Toolbar toolbar_center_back;
    @BindView(R.id.img_exc_fav)
    ImageView img_exc_fav;
    @BindView(R.id.txt_coin_code)
    TextView txt_coin_code;
    @BindView(R.id.txt_coin_vol)
    TextView txt_coin_vol;
    @BindView(R.id.txt_coin_bal_usd)
    TextView txt_coin_bal_usd;
    @BindView(R.id.txt_coin_bal)
    TextView txt_coin_bal;
    @BindView(R.id.txt_coin_per)
    TextView txt_coin_per;
    @BindView(R.id.txt_coin_high)
    TextView txt_coin_high;
    @BindView(R.id.txt_coin_low)
    TextView txt_coin_low;
    @BindView(R.id.txt_mrkt_deph)
    TextView txt_mrkt_deph;
    @BindView(R.id.rltv_mrkt_deph)
    RelativeLayout rltv_mrkt_deph;
    @BindView(R.id.rltv_mrkt_deph_view)
    RelativeLayout rltv_mrkt_deph_view;
    @BindView(R.id.txt_mrkt_trades)
    TextView txt_mrkt_trades;
    @BindView(R.id.rltv_mrkt_trades)
    RelativeLayout rltv_mrkt_trades;
    @BindView(R.id.rltv_mrkt_trades_view)
    RelativeLayout rltv_mrkt_trades_view;

    @BindView(R.id.lnr_mrkt_trades_data)
    LinearLayout lnr_mrkt_trades_data;
    @BindView(R.id.rview_mrkt_trades)
    RecyclerView rview_mrkt_trades;
    @BindView(R.id.lnr_dropdown)
    LinearLayout lnr_dropdown;
    @BindView(R.id.img_dropdown)
    ImageView img_dropdown;

    @BindView(R.id.txt_buy)
    TextView txt_buy;
    @BindView(R.id.txt_sell)
    TextView txt_sell;

    @BindView(R.id.lnr_mrkt_deph_data)
    LinearLayout lnr_mrkt_deph_data;
    @BindView(R.id.rview_bid)
    RecyclerView rview_bid;
    @BindView(R.id.rview_ask)
    RecyclerView rview_ask;
    @BindView(R.id.pb)
    ProgressBar pb;
    @BindView(R.id.lnr_no_trans_bid)
    LinearLayout lnr_no_trans_bid;
    @BindView(R.id.lnr_no_trans_ask)
    LinearLayout lnr_no_trans_ask;
    @BindView(R.id.lnr_no_trans_trades)
    LinearLayout lnr_no_trans_trades;


    MarketTradesRAdapter marketTradesRAdapter;
    MarketDephRAdapter marketDephRAdapter;
    LinearLayoutManager linearLayoutManagerDephBid, linearLayoutManagerDephAsk, linearLayoutManagerTrades;

    ArrayList<String> tradesList;
    ArrayList<ExcOrders> bidList, bid;
    ArrayList<ExcOrders> askList, ask;
    ArrayList<ExcOrders> allList;
    CoinPairs coinPairs;

    boolean isShort, isBid;
    CoinPairSelectableListener coinPairSelectableListener;

    private StompClient stompClient;
    ArrayList<ExcOrdersDelete> allExcOrders;
    String myEmail;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_coin_info);
        ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        myEmail = sharedPreferences.getString(CONSTANTS.email, null);

        img_exc_fav.setVisibility(View.VISIBLE);
        toolbar_center_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        allExcOrders = new ArrayList<>();
        tradesList = new ArrayList<>();
        bidList = new ArrayList<>();
        askList = new ArrayList<>();
        allList = new ArrayList<>();
        isShort = true;

        Bundle bundle = getIntent().getExtras();
        coinPairs = bundle.getParcelable(CONSTANTS.selectedCoin);

        txt_coin_code.setText(coinPairs.getStr_pairCoin() + "/" + coinPairs.getStr_exchangeCoin());
        txt_coin_vol.setText(String.format("%.4f", coinPairs.getDbl_volume()) + coinPairs.getStr_exchangeCoin());

        linearLayoutManagerTrades = new LinearLayoutManager(ExchangeCoinInfoActivity.this, LinearLayoutManager.VERTICAL, false);
        rview_mrkt_trades.setLayoutManager(linearLayoutManagerTrades);
        linearLayoutManagerDephBid = new LinearLayoutManager(ExchangeCoinInfoActivity.this, LinearLayoutManager.VERTICAL, false);
        rview_bid.setLayoutManager(linearLayoutManagerDephBid);
        linearLayoutManagerDephAsk = new LinearLayoutManager(ExchangeCoinInfoActivity.this, LinearLayoutManager.VERTICAL, false);
        rview_ask.setLayoutManager(linearLayoutManagerDephAsk);

        if (CommonUtilities.isConnectionAvailable(ExchangeCoinInfoActivity.this)) {
            fetchOrdersWS(txt_coin_code.getText().toString().trim());
        } else {
            CommonUtilities.ShowToastMessage(ExchangeCoinInfoActivity.this, getResources().getString(R.string.internetconnection));
        }
/*
        marketTradesRAdapter = new MarketTradesRAdapter(ExchangeCoinInfoActivity.this, tradesList, isShort);
        rview_mrkt_trades.setAdapter(marketTradesRAdapter);

        marketDephRAdapter = new MarketDephRAdapter(ExchangeCoinInfoActivity.this, true, bidList, askList, isShort, coinPairSelectableListener);
        rview_bid.setAdapter(marketDephRAdapter);
        marketDephRAdapter = new MarketDephRAdapter(ExchangeCoinInfoActivity.this, false, bidList, askList, isShort, coinPairSelectableListener);
        rview_ask.setAdapter(marketDephRAdapter);
*/

        rltv_mrkt_deph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_mrkt_deph.setTextColor(getResources().getColor(R.color.yellow));
                rltv_mrkt_deph_view.setVisibility(View.VISIBLE);
                txt_mrkt_trades.setTextColor(getResources().getColor(R.color.white));
                rltv_mrkt_trades_view.setVisibility(View.GONE);

                lnr_mrkt_trades_data.setVisibility(View.GONE);
                lnr_mrkt_deph_data.setVisibility(View.VISIBLE);

                isShort = true;
                Picasso.with(ExchangeCoinInfoActivity.this).load(R.drawable.down_yellow).into(img_dropdown);


                if (bidList.size() > 0) {
                    marketDephRAdapter = new MarketDephRAdapter(ExchangeCoinInfoActivity.this, true, bidList, askList, isShort, coinPairSelectableListener);
                    rview_bid.setAdapter(marketDephRAdapter);
                    rview_bid.setVisibility(View.VISIBLE);
                    lnr_no_trans_bid.setVisibility(View.GONE);
                } else {
                    rview_bid.setVisibility(View.GONE);
                    lnr_no_trans_bid.setVisibility(View.VISIBLE);
                }

                if (askList.size() > 0) {
                    marketDephRAdapter = new MarketDephRAdapter(ExchangeCoinInfoActivity.this, false, bidList, askList, isShort, coinPairSelectableListener);
                    rview_ask.setAdapter(marketDephRAdapter);
                    rview_ask.setVisibility(View.VISIBLE);
                    lnr_no_trans_ask.setVisibility(View.GONE);
                } else {
                    rview_ask.setVisibility(View.GONE);
                    lnr_no_trans_ask.setVisibility(View.VISIBLE);
                }

            }
        });

        rltv_mrkt_trades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_mrkt_trades.setTextColor(getResources().getColor(R.color.yellow));
                rltv_mrkt_trades_view.setVisibility(View.VISIBLE);
                txt_mrkt_deph.setTextColor(getResources().getColor(R.color.white));
                rltv_mrkt_deph_view.setVisibility(View.GONE);

                lnr_mrkt_trades_data.setVisibility(View.VISIBLE);
                lnr_mrkt_deph_data.setVisibility(View.GONE);

                isShort = true;
                Picasso.with(ExchangeCoinInfoActivity.this).load(R.drawable.down_yellow).into(img_dropdown);

//                marketTradesRAdapter = new MarketTradesRAdapter(ExchangeCoinInfoActivity.this, tradesList, isShort);
                if (allList.size() > 0) {
                    lnr_no_trans_trades.setVisibility(View.GONE);
                    marketTradesRAdapter = new MarketTradesRAdapter(ExchangeCoinInfoActivity.this, bidList, askList, allList, isShort);
                    rview_mrkt_trades.setAdapter(marketTradesRAdapter);
                } else {
                    lnr_no_trans_trades.setVisibility(View.VISIBLE);
                }

            }
        });


        lnr_dropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShort) {
                    isShort = false;
                    Picasso.with(ExchangeCoinInfoActivity.this).load(R.drawable.up_yellow).into(img_dropdown);


//                    marketTradesRAdapter = new MarketTradesRAdapter(ExchangeCoinInfoActivity.this, tradesList, isShort);
                    if (allList.size() > 0) {
                        lnr_no_trans_trades.setVisibility(View.GONE);
                        marketTradesRAdapter = new MarketTradesRAdapter(ExchangeCoinInfoActivity.this, bidList, askList, allList, isShort);
                        rview_mrkt_trades.setAdapter(marketTradesRAdapter);
                    } else {
                        lnr_no_trans_trades.setVisibility(View.VISIBLE);
                    }

                    if (bidList.size() > 0) {
                        marketDephRAdapter = new MarketDephRAdapter(ExchangeCoinInfoActivity.this, true, bidList, askList, isShort, coinPairSelectableListener);
                        rview_bid.setAdapter(marketDephRAdapter);
                        rview_bid.setVisibility(View.VISIBLE);
                        lnr_no_trans_bid.setVisibility(View.GONE);
                    } else {
                        rview_bid.setVisibility(View.GONE);
                        lnr_no_trans_bid.setVisibility(View.VISIBLE);
                    }

                    if (askList.size() > 0) {
                        marketDephRAdapter = new MarketDephRAdapter(ExchangeCoinInfoActivity.this, false, bidList, askList, isShort, coinPairSelectableListener);
                        rview_ask.setAdapter(marketDephRAdapter);
                        rview_ask.setVisibility(View.VISIBLE);
                        lnr_no_trans_ask.setVisibility(View.GONE);
                    } else {
                        rview_ask.setVisibility(View.GONE);
                        lnr_no_trans_ask.setVisibility(View.VISIBLE);
                    }

                } else {
                    isShort = true;
                    Picasso.with(ExchangeCoinInfoActivity.this).load(R.drawable.down_yellow).into(img_dropdown);


//                    marketTradesRAdapter = new MarketTradesRAdapter(ExchangeCoinInfoActivity.this, tradesList, isShort);
                    if (allList.size() > 0) {
                        lnr_no_trans_trades.setVisibility(View.GONE);
                        marketTradesRAdapter = new MarketTradesRAdapter(ExchangeCoinInfoActivity.this, bidList, askList, allList, isShort);
                        rview_mrkt_trades.setAdapter(marketTradesRAdapter);
                    } else {
                        lnr_no_trans_trades.setVisibility(View.VISIBLE);
                    }

                    if (bidList.size() > 0) {
                        marketDephRAdapter = new MarketDephRAdapter(ExchangeCoinInfoActivity.this, true, bidList, askList, isShort, coinPairSelectableListener);
                        rview_bid.setAdapter(marketDephRAdapter);
                        rview_bid.setVisibility(View.VISIBLE);
                        lnr_no_trans_bid.setVisibility(View.GONE);
                    } else {
                        rview_bid.setVisibility(View.GONE);
                        lnr_no_trans_bid.setVisibility(View.VISIBLE);
                    }

                    if (askList.size() > 0) {
                        marketDephRAdapter = new MarketDephRAdapter(ExchangeCoinInfoActivity.this, false, bidList, askList, isShort, coinPairSelectableListener);
                        rview_ask.setAdapter(marketDephRAdapter);
                        rview_ask.setVisibility(View.VISIBLE);
                        lnr_no_trans_ask.setVisibility(View.GONE);
                    } else {
                        rview_ask.setVisibility(View.GONE);
                        lnr_no_trans_ask.setVisibility(View.VISIBLE);
                    }

                }
            }
        });

        txt_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_buy.setBackground(getResources().getDrawable(R.drawable.selected_buy));
                txt_sell.setBackground(getResources().getDrawable(R.drawable.unselected));

                Intent intent = new Intent(ExchangeCoinInfoActivity.this, ExchangeDashBoardActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(CONSTANTS.selectedCoin, coinPairs);
                intent.putExtras(bundle);
                intent.putExtra(CONSTANTS.seletedTab, 3);
                startActivity(intent);
                stompClient.disconnect();
            }
        });

        txt_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_buy.setBackground(getResources().getDrawable(R.drawable.unselected));
                txt_sell.setBackground(getResources().getDrawable(R.drawable.selected_sell));

                Intent intent = new Intent(ExchangeCoinInfoActivity.this, ExchangeDashBoardActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(CONSTANTS.selectedCoin, coinPairs);
                intent.putExtras(bundle);
                intent.putExtra(CONSTANTS.seletedTab, 3);
                startActivity(intent);
                stompClient.disconnect();
            }
        });

    }


    //    **************WEBSOCKET FOR ORDERS [ASK/BID]**************
    private void fetchOrdersWS(String title_pair) {
        runOnUiThread(new Runnable() {
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

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stompClient.topic("/topic/orderbook").subscribe(new Action1<StompMessage>() {
                            @Override
                            public void call(StompMessage message) {
                                try {
                                    Log.e(TAG, "*****Received " + "*****: /topic/orderbook" + message.getPayload());
                                    ExcOrdersDelete coinsStringArray = GsonUtils.getInstance().fromJson(message.getPayload(), ExcOrdersDelete.class);
                                    //allExcOrders = new ArrayList<ExcOrdersDelete>(Arrays.asList(coinsStringArray));

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
//                                            stompClient.disconnect();
//                                            Log.e(TAG, "*****DisConnected " + "*****: /topic/orderbook");
                                            pb.setVisibility(View.VISIBLE);

                                            bid = new ArrayList<>();
                                            ask = new ArrayList<>();
                                            bidList = new ArrayList<>();
                                            askList = new ArrayList<>();
                                            allList = new ArrayList<>();

                                            bid = (ArrayList<ExcOrders>) coinsStringArray.getList_bid();
                                            ask = (ArrayList<ExcOrders>) coinsStringArray.getList_ask();

                                            for (int i = 0; i < bid.size(); i++) {
                                                if (!bid.get(i).getStr_user().equals(myEmail)) {
                                                    if (bid.get(i).getStr_coinPair().trim().equals(title_pair)) {
                                                        bidList.add(bid.get(i));
                                                        allList.add(bid.get(i));
                                                    }
                                                }
                                            }
                                            for (int i = 0; i < ask.size(); i++) {
                                                if (!ask.get(i).getStr_user().equals(myEmail)) {
                                                    if (ask.get(i).getStr_coinPair().trim().equals(title_pair)) {
                                                        askList.add(ask.get(i));
                                                        allList.add(ask.get(i));
                                                    }
                                                }
                                            }

                                            if (bidList.size() > 0) {
                                                marketDephRAdapter = new MarketDephRAdapter(ExchangeCoinInfoActivity.this, true, bidList, askList, isShort, coinPairSelectableListener);
                                                rview_bid.setAdapter(marketDephRAdapter);
                                                rview_bid.setVisibility(View.VISIBLE);
                                                lnr_no_trans_bid.setVisibility(View.GONE);
                                            } else {
                                                rview_bid.setVisibility(View.GONE);
                                                lnr_no_trans_bid.setVisibility(View.VISIBLE);
                                            }

                                            if (askList.size() > 0) {
                                                marketDephRAdapter = new MarketDephRAdapter(ExchangeCoinInfoActivity.this, false, bidList, askList, isShort, coinPairSelectableListener);
                                                rview_ask.setAdapter(marketDephRAdapter);
                                                rview_ask.setVisibility(View.VISIBLE);
                                                lnr_no_trans_ask.setVisibility(View.GONE);
                                            } else {
                                                rview_ask.setVisibility(View.GONE);
                                                lnr_no_trans_ask.setVisibility(View.VISIBLE);
                                            }


                                            if (allList.size() > 0) {
                                                lnr_no_trans_trades.setVisibility(View.GONE);
                                                marketTradesRAdapter = new MarketTradesRAdapter(ExchangeCoinInfoActivity.this, bidList, askList, allList, isShort);
                                                rview_mrkt_trades.setAdapter(marketTradesRAdapter);
                                            } else {
                                                lnr_no_trans_trades.setVisibility(View.VISIBLE);
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

}
