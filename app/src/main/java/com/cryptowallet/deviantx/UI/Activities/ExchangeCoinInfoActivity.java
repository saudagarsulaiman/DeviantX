package com.cryptowallet.deviantx.UI.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.UI.Adapters.MarketTradesRAdapter;
import com.cryptowallet.deviantx.UI.Models.CoinPairs;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

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


    MarketTradesRAdapter marketTradesRAdapter;
    LinearLayoutManager linearLayoutManagerDeph, linearLayoutManagerTrades;

    ArrayList<String> tradesList;
    CoinPairs coinPairsList;

    boolean isShort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_coin_info);
        ButterKnife.bind(this);
        img_exc_fav.setVisibility(View.VISIBLE);
        toolbar_center_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tradesList = new ArrayList<>();
        isShort = true;

        Bundle bundle = getIntent().getExtras();
        coinPairsList = bundle.getParcelable(CONSTANTS.selectedCoin);

//        linearLayoutManagerDeph = new LinearLayoutManager(ExchangeCoinInfoActivity.this, LinearLayoutManager.VERTICAL, false);
//        rview_mrkt_deph.setLayoutManager(linearLayoutManagerDeph);
        linearLayoutManagerTrades = new LinearLayoutManager(ExchangeCoinInfoActivity.this, LinearLayoutManager.VERTICAL, false);
        rview_mrkt_trades.setLayoutManager(linearLayoutManagerTrades);

        marketTradesRAdapter = new MarketTradesRAdapter(ExchangeCoinInfoActivity.this, tradesList, isShort);
        rview_mrkt_trades.setAdapter(marketTradesRAdapter);

        rltv_mrkt_deph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_mrkt_deph.setTextColor(getResources().getColor(R.color.yellow));
                rltv_mrkt_deph_view.setVisibility(View.VISIBLE);
                txt_mrkt_trades.setTextColor(getResources().getColor(R.color.white));
                rltv_mrkt_trades_view.setVisibility(View.GONE);

                lnr_mrkt_trades_data.setVisibility(View.GONE);

                isShort = true;
                Picasso.with(ExchangeCoinInfoActivity.this).load(R.drawable.down_yellow).into(img_dropdown);
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

                isShort = true;
                Picasso.with(ExchangeCoinInfoActivity.this).load(R.drawable.down_yellow).into(img_dropdown);
                marketTradesRAdapter = new MarketTradesRAdapter(ExchangeCoinInfoActivity.this, tradesList, isShort);
                rview_mrkt_trades.setAdapter(marketTradesRAdapter);
            }
        });


        lnr_dropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShort) {
                    isShort = false;
                    Picasso.with(ExchangeCoinInfoActivity.this).load(R.drawable.up_yellow).into(img_dropdown);
                    marketTradesRAdapter = new MarketTradesRAdapter(ExchangeCoinInfoActivity.this, tradesList, isShort);
                    rview_mrkt_trades.setAdapter(marketTradesRAdapter);

                } else {
                    isShort = true;
                    Picasso.with(ExchangeCoinInfoActivity.this).load(R.drawable.down_yellow).into(img_dropdown);
                    marketTradesRAdapter = new MarketTradesRAdapter(ExchangeCoinInfoActivity.this, tradesList, isShort);
                    rview_mrkt_trades.setAdapter(marketTradesRAdapter);

                }
            }
        });


    }
}
