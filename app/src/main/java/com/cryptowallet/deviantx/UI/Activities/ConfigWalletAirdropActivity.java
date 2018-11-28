package com.cryptowallet.deviantx.UI.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.UI.Models.AirdropWallet;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cryptowallet.deviantx.Utilities.MyApplication.myApplication;

public class ConfigWalletAirdropActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_center_back)
    Toolbar toolbar_center_back;
    @BindView(R.id.img_coin_logo)
    ImageView img_coin_logo;
    @BindView(R.id.txt_coin_code)
    TextView txt_coin_code;
    @BindView(R.id.txt_coin_name)
    TextView txt_coin_name;
    @BindView(R.id.txt_coin_usd_value)
    TextView txt_coin_usd_value;
    @BindView(R.id.img_qrcode)
    ImageView img_qrcode;
    @BindView(R.id.img_copy_address)
    ImageView img_copy_address;
    @BindView(R.id.txt_address)
    TextView txt_address;
    @BindView(R.id.btn_share_qrcode)
    Button btn_share_qrcode;

    AirdropWallet selected_coin;
    String address;
    ArrayList<AirdropWallet> selectedAccountWallet;

    @Override
    protected void onResume() {
        super.onResume();
        myApplication.disableScreenCapture(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_wallet_airdrop);

        ButterKnife.bind(this);

        selectedAccountWallet = new ArrayList<>();
        toolbar_center_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Bundle bundle = getIntent().getExtras();
//        selected_coin = bundle.getParcelable(CONSTANTS.selectedCoin);
        selectedAccountWallet = bundle.getParcelableArrayList(CONSTANTS.selectedAccountWallet);

        txt_address.setText(selectedAccountWallet.get(0).getStr_data_ad_address());
//           QR Code Generator
        CommonUtilities.qrCodeGenerate(selectedAccountWallet.get(0).getStr_data_ad_address(), img_qrcode, ConfigWalletAirdropActivity.this);
//        txt_coin_usd_value.setText(String.format("%.4f", ));
        img_copy_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtilities.copyToClipboard(ConfigWalletAirdropActivity.this, selectedAccountWallet.get(0).getStr_data_ad_address(), selectedAccountWallet.get(0).getAllCoins().getStr_coin_name());
            }
        });
        btn_share_qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//           Sharing Address
                CommonUtilities.shareAddress(selectedAccountWallet.get(0).getStr_data_ad_address(), ConfigWalletAirdropActivity.this);
            }
        });

    }


}
