package com.cryptowallet.deviantx.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.UI.Adapters.ClaimWaysRAdapter;
import com.cryptowallet.deviantx.UI.Models.FeaturedAirdrops;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CoinInfoADAcivity extends AppCompatActivity {

    @BindView(R.id.tool)
    Toolbar tool;
    @BindView(R.id.img_coin_logo)
    ImageView img_coin_logo;
    @BindView(R.id.txt_coin_name)
    TextView txt_coin_name;
    @BindView(R.id.txt_coin_code)
    TextView txt_coin_code;
    @BindView(R.id.lnr_share)
    LinearLayout lnr_share;
    @BindView(R.id.lnr_estimated)
    LinearLayout lnr_estimated;
    @BindView(R.id.txt_estimated)
    TextView txt_estimated;
    @BindView(R.id.lnr_tokens)
    LinearLayout lnr_tokens;
    @BindView(R.id.txt_tokens)
    TextView txt_tokens;
    @BindView(R.id.txt_decription)
    TextView txt_decription;
    @BindView(R.id.rview_enter_ways)
    RecyclerView rview_enter_ways;
    @BindView(R.id.btn_participate)
    Button btn_participate;


    FeaturedAirdrops selectedCoin;

    ClaimWaysRAdapter claimWaysRAdapter;
    LinearLayoutManager linearLayoutManager;

    ArrayList<String> claimsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_info_ad);

        ButterKnife.bind(this);

        tool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Bundle bundle = getIntent().getExtras();
        selectedCoin = bundle.getParcelable(CONSTANTS.selectedCoin);
        claimsList = new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(CoinInfoADAcivity.this, LinearLayoutManager.VERTICAL, false);
        rview_enter_ways.setLayoutManager(linearLayoutManager);

        Picasso.with(CoinInfoADAcivity.this).load(selectedCoin.getStr_coinlogo()).into(img_coin_logo);
        txt_coin_name.setText(selectedCoin.getStr_coinName());
        txt_coin_code.setText(selectedCoin.getStr_coinCode());
        txt_estimated.setText("$" + String.format("%.4f", selectedCoin.getdbl_estimated()) + " Ref");
        txt_tokens.setText("750 " + selectedCoin.getStr_coinCode());


        btn_participate.setVisibility(View.GONE);
        btn_participate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(new Intent(CoinInfoADAcivity.this, AirdropWalletFetch.class));
                    startForegroundService(new Intent(CoinInfoADAcivity.this, FeaturedAirdropsFetch.class));
                } else {
                    startService(new Intent(CoinInfoADAcivity.this, AirdropWalletFetch.class));
                    startService(new Intent(CoinInfoADAcivity.this, FeaturedAirdropsFetch.class));
                }*/
                Intent intent = new Intent(CoinInfoADAcivity.this, DashBoardActivity.class);
                intent.putExtra(CONSTANTS.seletedTab, 2);
                startActivity(intent);
            }
        });

        claimWaysRAdapter = new ClaimWaysRAdapter(CoinInfoADAcivity.this, claimsList);
        rview_enter_ways.setAdapter(claimWaysRAdapter);


    }


}
