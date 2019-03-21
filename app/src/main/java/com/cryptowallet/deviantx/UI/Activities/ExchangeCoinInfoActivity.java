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
import com.cryptowallet.deviantx.ServiceAPIs.ExchangePairControllerApi;
import com.cryptowallet.deviantx.UI.Adapters.MarketDephRAdapter;
import com.cryptowallet.deviantx.UI.Adapters.MarketTradesRAdapter;
import com.cryptowallet.deviantx.UI.Interfaces.CoinPairSelectableListener;
import com.cryptowallet.deviantx.UI.Models.AsksDC;
import com.cryptowallet.deviantx.UI.Models.BidsDC;
import com.cryptowallet.deviantx.UI.Models.CoinPairs;
import com.cryptowallet.deviantx.UI.Models.DepthChartData;
import com.cryptowallet.deviantx.UI.Models.ExcOrders;
import com.cryptowallet.deviantx.UI.Models.ExcOrdersDelete;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;
import com.cryptowallet.deviantx.Utilities.DeviantXApiClient;
import com.cryptowallet.deviantx.Utilities.GsonUtils;
import com.highsoft.highcharts.common.hichartsclasses.HIArea;
import com.highsoft.highcharts.common.hichartsclasses.HIButtonOptions;
import com.highsoft.highcharts.common.hichartsclasses.HIChart;
import com.highsoft.highcharts.common.hichartsclasses.HIExporting;
import com.highsoft.highcharts.common.hichartsclasses.HILabels;
import com.highsoft.highcharts.common.hichartsclasses.HILegend;
import com.highsoft.highcharts.common.hichartsclasses.HIOptions;
import com.highsoft.highcharts.common.hichartsclasses.HIPlotOptions;
import com.highsoft.highcharts.common.hichartsclasses.HISeries;
import com.highsoft.highcharts.common.hichartsclasses.HITitle;
import com.highsoft.highcharts.common.hichartsclasses.HITooltip;
import com.highsoft.highcharts.common.hichartsclasses.HIXAxis;
import com.highsoft.highcharts.common.hichartsclasses.HIYAxis;
import com.highsoft.highcharts.core.HIChartView;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

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

    @BindView(R.id.hc)
    HIChartView chartView;

    @BindView(R.id.txt_no_dc_avail)
    TextView txt_no_dc_avail;

    HIOptions options;

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

    String responseMsg, responseStatus, responseData;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;

    ArrayList<AsksDC> asksDCList;
    ArrayList<BidsDC> bidsDCList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_coin_info);
        ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        myEmail = sharedPreferences.getString(CONSTANTS.email, null);

        options = new HIOptions();


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
        asksDCList = new ArrayList<>();
        bidsDCList = new ArrayList<>();

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
            fetchDepthChartData(coinPairs.getStr_pairCoin(), coinPairs.getStr_exchangeCoin());
            fetchOrdersWS(txt_coin_code.getText().toString().trim());
        } else {
            CommonUtilities.ShowToastMessage(ExchangeCoinInfoActivity.this, getResources().getString(R.string.internetconnection));
        }


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
                    marketDephRAdapter = new MarketDephRAdapter(ExchangeCoinInfoActivity.this, true, bidList, askList, isShort, coinPairSelectableListener, false);
                    rview_bid.setAdapter(marketDephRAdapter);
                    rview_bid.setVisibility(View.VISIBLE);
                    lnr_no_trans_bid.setVisibility(View.GONE);
                } else {
                    rview_bid.setVisibility(View.GONE);
                    lnr_no_trans_bid.setVisibility(View.VISIBLE);
                }

                if (askList.size() > 0) {
                    marketDephRAdapter = new MarketDephRAdapter(ExchangeCoinInfoActivity.this, false, bidList, askList, isShort, coinPairSelectableListener, false);
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
                        marketDephRAdapter = new MarketDephRAdapter(ExchangeCoinInfoActivity.this, true, bidList, askList, isShort, coinPairSelectableListener, false);
                        rview_bid.setAdapter(marketDephRAdapter);
                        rview_bid.setVisibility(View.VISIBLE);
                        lnr_no_trans_bid.setVisibility(View.GONE);
                    } else {
                        rview_bid.setVisibility(View.GONE);
                        lnr_no_trans_bid.setVisibility(View.VISIBLE);
                    }

                    if (askList.size() > 0) {
                        marketDephRAdapter = new MarketDephRAdapter(ExchangeCoinInfoActivity.this, false, bidList, askList, isShort, coinPairSelectableListener, false);
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
                        marketDephRAdapter = new MarketDephRAdapter(ExchangeCoinInfoActivity.this, true, bidList, askList, isShort, coinPairSelectableListener, false);
                        rview_bid.setAdapter(marketDephRAdapter);
                        rview_bid.setVisibility(View.VISIBLE);
                        lnr_no_trans_bid.setVisibility(View.GONE);
                    } else {
                        rview_bid.setVisibility(View.GONE);
                        lnr_no_trans_bid.setVisibility(View.VISIBLE);
                    }

                    if (askList.size() > 0) {
                        marketDephRAdapter = new MarketDephRAdapter(ExchangeCoinInfoActivity.this, false, bidList, askList, isShort, coinPairSelectableListener, false);
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

    private void setDepthChart(ArrayList<AsksDC> asksDCList, ArrayList<BidsDC> bidsDCList) {
//        SETTING CHART TYPE
        HIChart chart = new HIChart();
        chart.setType("area");
        chart.setZoomType("xy");
        options.setChart(chart);


//        SETTING TITLE
        HITitle title = new HITitle();
        title.setText(" ");
        options.setTitle(title);


        //        SETTING X-AXIS
        final HIXAxis hixAxis = new HIXAxis();
        ArrayList xaxis = new ArrayList<>();
        hixAxis.setMinPadding(0);
        hixAxis.setMaxPadding(0);
        ArrayList xPlotLines = new ArrayList();
/*
        xPlotLines.add(
                {
                        "color":'#888',
                "value":0.1523,
                "width":1,
                "label":{
            "text":"Actual price",
                    "rotation":90
        }
        }
        );
*/
        hixAxis.setPlotLines(xPlotLines);
        HITitle title1 = new HITitle();
//        title1.setText("Price");
        title1.setText(null);
        hixAxis.setTitle(title1);
        xaxis.add(hixAxis);
        options.setXAxis(xaxis);


        //        SETTING Y-AXIS
        final HIYAxis hiyAxis = new HIYAxis();
        ArrayList yaxis = new ArrayList();
//a)
        hiyAxis.setLineWidth(1);
        hiyAxis.setGridLineWidth(1);
        HITitle hiTitleyL = new HITitle();
        hiTitleyL.setText(" ");
        hiyAxis.setTitle(hiTitleyL);
        hiyAxis.setTickWidth(1);
        hiyAxis.setTickLength(5);
        hiyAxis.setTickPosition("inside");
        HILabels hiYLabelL = new HILabels();
        hiYLabelL.setAlign("left");
        hiYLabelL.setX(8);
        hiyAxis.setLabels(hiYLabelL);
//b)
        hiyAxis.setOpposite(true);
        hiyAxis.setLinkedTo(0);
        hiyAxis.setLineWidth(1);
        hiyAxis.setGridLineWidth(0);
        HITitle hiTitleyR = new HITitle();
        hiTitleyR.setText(" ");
        hiyAxis.setTitle(hiTitleyR);
        hiyAxis.setTickWidth(1);
        hiyAxis.setTickLength(5);
        hiyAxis.setTickPosition("inside");
        HILabels hiYLabelR = new HILabels();
        hiYLabelR.setAlign("right");
        hiYLabelR.setX(8);
        hiyAxis.setLabels(hiYLabelR);
//        a+b
        yaxis.add(hiyAxis);
        options.setYAxis(yaxis);


        //SETTING LEGEND
        HILegend legend = new HILegend();
        legend.setEnabled(false);
        options.setLegend(legend);


//        SETTING PLOTOPTIONS
        HIPlotOptions hiPlotOptions = new HIPlotOptions();
        HIArea hiArea = new HIArea();
        hiArea.setFillOpacity(0.2);
        hiArea.setLineWidth(1);
        hiArea.setStep("center");
        hiPlotOptions.setArea(hiArea);
        options.setPlotOptions(hiPlotOptions);


//        SETTING TOOLTIP
        HITooltip hiTooltip = new HITooltip();
        hiTooltip.setHeaderFormat("<span style=\"font-size=10px;\">Price: {point.key}</span><br/>");
        hiTooltip.setValueDecimals(2);
        options.setTooltip(hiTooltip);


//        SETTING BUTTON OPTIONS
        HIButtonOptions hiButtonOptions = new HIButtonOptions();
        hiButtonOptions.setEnabled(false);


//        SETTING SERIES
        HISeries hiSeries = new HISeries();
        hiSeries.setName("Bid");
        ArrayList bidData = new ArrayList();
        for (int i = 0; i < bidsDCList.size(); i++) {
            ArrayList bid = new ArrayList();
            bid.add(0, bidsDCList.get(i).getDbl_price());
            bid.add(1, bidsDCList.get(i).getDbl_volume());
            bidData.add(i, bid);
        }
        /*   ArrayList biddataList0 = new ArrayList();
        biddataList0.add(0, 0.1524);
        biddataList0.add(1, 0.948665);
        bidData.add(0, biddataList0);
        ArrayList biddataList1 = new ArrayList();
        biddataList1.add(0, 0.1539);
        biddataList1.add(1, 35.510715);
        bidData.add(1, biddataList1);
        ArrayList biddataList2 = new ArrayList();
        biddataList2.add(0, 0.154);
        biddataList2.add(1, 39.883437);
        bidData.add(2, biddataList2);
        ArrayList biddataList3 = new ArrayList();
        biddataList3.add(0, 0.1541);
        biddataList3.add(1, 40.499661);
        bidData.add(3, biddataList3);
        ArrayList biddataList4 = new ArrayList();
        biddataList4.add(0, 0.1545);
        biddataList4.add(1, 43.262994000000006);
        bidData.add(4, biddataList4);
        ArrayList biddataList5 = new ArrayList();
        biddataList5.add(0, 0.1547);
        biddataList5.add(1, 60.14799400000001);
        bidData.add(5, biddataList5);
        ArrayList biddataList6 = new ArrayList();
        biddataList6.add(0, 0.1553);
        biddataList6.add(1, 60.55018100000001);
        bidData.add(6, biddataList6);
        ArrayList biddataList7 = new ArrayList();
        biddataList7.add(0, 0.1567);
        biddataList7.add(1, 69.46518400000001);
        bidData.add(7, biddataList7);
        ArrayList biddataList8 = new ArrayList();
        biddataList8.add(0, 0.1569);
        biddataList8.add(1, 69.621464);
        bidData.add(8, biddataList8);
        ArrayList biddataList9 = new ArrayList();
        biddataList9.add(0, 0.157);
        biddataList9.add(1, 70.398015);
        bidData.add(9, biddataList9);
        ArrayList biddataList10 = new ArrayList();
        biddataList10.add(0, 0.1574);
        biddataList10.add(1, 70.400197);
        bidData.add(10, biddataList10);
        ArrayList biddataList11 = new ArrayList();
        biddataList11.add(0, 0.1575);
        biddataList11.add(1, 73.199217);
        bidData.add(11, biddataList11);
        ArrayList biddataList12 = new ArrayList();
        biddataList12.add(0, 0.158);
        biddataList12.add(1, 77.700017);
        bidData.add(12, biddataList12);
        ArrayList biddataList13 = new ArrayList();
        biddataList13.add(0, 0.1583);
        biddataList13.add(1, 79.449017);
        bidData.add(13, biddataList13);
        ArrayList biddataList14 = new ArrayList();
        biddataList14.add(0, 0.1588);
        biddataList14.add(1, 79.584064);
        bidData.add(14, biddataList14);
        ArrayList biddataList15 = new ArrayList();
        biddataList15.add(0, 0.159);
        biddataList15.add(1, 80.584064);
        bidData.add(15, biddataList15);
        ArrayList biddataList16 = new ArrayList();
        biddataList16.add(0, 0.16);
        biddataList16.add(1, 81.58156);
        bidData.add(16, biddataList16);
        ArrayList biddataList17 = new ArrayList();
        biddataList17.add(0, 0.1608);
        biddataList17.add(1, 83.38156);
        bidData.add(17, biddataList17);
        ArrayList biddataList18 = new ArrayList();
        biddataList18.add(0, 0.1624);
        biddataList18.add(1, 0.948665);
        bidData.add(18, biddataList18);
        ArrayList biddataList19 = new ArrayList();
        biddataList19.add(0, 0.1628);
        biddataList19.add(1, 0.948665);
        bidData.add(19, biddataList19);
       */
        hiSeries.setData(bidData);
/*
        HIColor hiColor = new HIColor("#03a7a8");
        hiSeries.setColor(hiColor);
*/

        HISeries hiSeriesA = new HISeries();
        hiSeriesA.setName("ask");
        ArrayList askData = new ArrayList();
        for (int i = 0; i < asksDCList.size(); i++) {
            ArrayList ask = new ArrayList();
            ask.add(0, asksDCList.get(i).getDbl_price());
            ask.add(1, asksDCList.get(i).getDbl_volume());
            askData.add(i, ask);
        }

       /*   ArrayList askdataList0 = new ArrayList();
        askdataList0.add(0, 0.000018);
        askdataList0.add(1, 0);
        askData.add(0, askdataList0);
        ArrayList askdataList1 = new ArrayList();
        askdataList1.add(0, 0.000018);
        askdataList1.add(1, 0);
        askData.add(1, askdataList1);
        ArrayList askdataList2 = new ArrayList();
        askdataList2.add(0, 0.000018);
        askdataList2.add(1, 0);
        askData.add(2, askdataList2);
       ArrayList askdataList3 = new ArrayList();
        askdataList3.add(0, 0.1438);
        askdataList3.add(1, 197.33275);
        askData.add(3, askdataList3);
        ArrayList askdataList4 = new ArrayList();
        askdataList4.add(0, 0.1439);
        askdataList4.add(1, 153.677454);
        askData.add(4, askdataList4);
        ArrayList askdataList5 = new ArrayList();
        askdataList5.add(0, 0.144);
        askdataList5.add(1, 146.007722);
        askData.add(5, askdataList5);
        ArrayList askdataList6 = new ArrayList();
        askdataList6.add(0, 0.1442);
        askdataList6.add(1, 82.55212900000001);
        askData.add(6, askdataList6);
        ArrayList askdataList7 = new ArrayList();
        askdataList7.add(0, 0.1443);
        askdataList7.add(1, 59.152814000000006);
        askData.add(7, askdataList7);
        ArrayList askdataList8 = new ArrayList();
        askdataList8.add(0, 0.1444);
        askdataList8.add(1, 57.942260000000005);
        askData.add(8, askdataList8);
        ArrayList askdataList9 = new ArrayList();
        askdataList9.add(0, 0.1445);
        askdataList9.add(1, 57.483850000000004);
        askData.add(9, askdataList9);
        ArrayList askdataList10 = new ArrayList();
        askdataList10.add(0, 0.1446);
        askdataList10.add(1, 52.39210800000001);
        askData.add(10, askdataList10);
        ArrayList askdataList11 = new ArrayList();
        askdataList11.add(0, 0.1447);
        askdataList11.add(1, 51.867208000000005);
        askData.add(11, askdataList11);
        ArrayList askdataList12 = new ArrayList();
        askdataList12.add(0, 0.1448);
        askdataList12.add(1, 44.104697);
        askData.add(12, askdataList12);
        ArrayList askdataList13 = new ArrayList();
        askdataList13.add(0, 0.1449);
        askdataList13.add(1, 40.131217);
        askData.add(13, askdataList13);
        ArrayList askdataList14 = new ArrayList();
        askdataList14.add(0, 0.145);
        askdataList14.add(1, 31.878217);
        askData.add(14, askdataList14);
        ArrayList askdataList15 = new ArrayList();
        askdataList15.add(0, 0.1451);
        askdataList15.add(1, 22.794916999999998);
        askData.add(15, askdataList15);
        ArrayList askdataList16 = new ArrayList();
        askdataList16.add(0, 0.1453);
        askdataList16.add(1, 12.345828999999998);
        askData.add(16, askdataList16);
        ArrayList askdataList17 = new ArrayList();
        askdataList17.add(0, 0.1454);
        askdataList17.add(1, 10.035642);
        askData.add(17, askdataList17);
        ArrayList askdataList18 = new ArrayList();
        askdataList18.add(0, 0.148);
        askdataList18.add(1, 9.326642);
        askData.add(18, askdataList18);
        ArrayList askdataList19 = new ArrayList();
        askdataList19.add(0, 0.1522);
        askdataList19.add(1, 3.76317);
        askData.add(19, askdataList19);*/
        hiSeriesA.setData(askData);
//        HIColor hiColor = new HIColor("#03a7a8");
//        hiSeriesA.setColor(hiColor);

        ArrayList series = new ArrayList();
        series.add(hiSeries);
        series.add(hiSeriesA);

        options.setSeries(series);

        HIExporting hiExporting = new HIExporting();
        hiExporting.setEnabled(false);
        options.setExporting(hiExporting);
        chartView.setOptions(options);


        if (askData.size() == 0 && bidData.size() == 0) {
            txt_no_dc_avail.setVisibility(View.VISIBLE);
            chartView.setVisibility(View.GONE);
        } else {
            txt_no_dc_avail.setVisibility(View.GONE);
            chartView.setVisibility(View.VISIBLE);
        }

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
                                                marketDephRAdapter = new MarketDephRAdapter(ExchangeCoinInfoActivity.this, true, bidList, askList, isShort, coinPairSelectableListener, false);
                                                rview_bid.setAdapter(marketDephRAdapter);
                                                rview_bid.setVisibility(View.VISIBLE);
                                                lnr_no_trans_bid.setVisibility(View.GONE);
                                            } else {
                                                rview_bid.setVisibility(View.GONE);
                                                lnr_no_trans_bid.setVisibility(View.VISIBLE);
                                            }

                                            if (askList.size() > 0) {
                                                marketDephRAdapter = new MarketDephRAdapter(ExchangeCoinInfoActivity.this, false, bidList, askList, isShort, coinPairSelectableListener, false);
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


    //    **************DEPTHCHART FOR ORDERS [ASK/BID]**************
    private void fetchDepthChartData(String pairCoin, String exchangeCoin) {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            ExchangePairControllerApi apiService = DeviantXApiClient.getClient().create(ExchangePairControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.getDepthChart(CONSTANTS.DeviantMulti + token, pairCoin, exchangeCoin);
            apiResponse.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responsevalue = response.body().string();
                        if (!responsevalue.isEmpty() && responsevalue != null) {

                            JSONObject jsonObject = new JSONObject(responsevalue);
                            responseMsg = jsonObject.getString("msg");
                            responseStatus = jsonObject.getString("status");

                            if (responseStatus.equals("true")) {

                                responseData = jsonObject.getString("data");
                                DepthChartData coinsStringArray = GsonUtils.getInstance().fromJson(responseData, DepthChartData.class);
                                asksDCList = new ArrayList<>();
                                bidsDCList = new ArrayList<>();
                                asksDCList = (ArrayList<AsksDC>) coinsStringArray.getList_Asks();
                                bidsDCList = (ArrayList<BidsDC>) coinsStringArray.getList_Bids();
                                setDepthChart(asksDCList, bidsDCList);
//                                chartView.setVisibility(View.VISIBLE);

                            } else {
                                CommonUtilities.ShowToastMessage(ExchangeCoinInfoActivity.this, responseMsg);
                            }

                        } else {
                            CommonUtilities.ShowToastMessage(ExchangeCoinInfoActivity.this, responseMsg);
                        }

                    } catch (Exception e) {
                        CommonUtilities.ShowToastMessage(ExchangeCoinInfoActivity.this, getResources().getString(R.string.errortxt));
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        CommonUtilities.ShowToastMessage(ExchangeCoinInfoActivity.this, getResources().getString(R.string.Timeout));
                    } else if (t instanceof java.net.ConnectException) {
                        CommonUtilities.ShowToastMessage(ExchangeCoinInfoActivity.this, getResources().getString(R.string.networkerror));
                    } else {
                        CommonUtilities.ShowToastMessage(ExchangeCoinInfoActivity.this, getResources().getString(R.string.errortxt));
                    }
                }
            });
        } catch (Exception ex) {
            CommonUtilities.ShowToastMessage(ExchangeCoinInfoActivity.this, getResources().getString(R.string.errortxt));
        }

    }


}
