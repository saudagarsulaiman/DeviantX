package com.cryptowallet.deviantx.UI.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cryptowallet.deviantx.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CoinInfoADAcivity extends AppCompatActivity {

    @BindView(R.id.tool)
    Toolbar tool;
    @BindView(R.id.img_coin_logo)
    ImageView img_coin_logo;
    @BindView(R.id.txt_coin_name)
    TextView txt_coin_name;
    @BindView(R.id.txt_coin_value)
    TextView txt_coin_value;
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


    }


}
