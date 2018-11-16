package com.cryptowallet.deviantx.UI.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.UI.Models.AccountWallet;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cryptowallet.deviantx.Utilities.MyApplication.myApplication;

public class ReceiveCoinActivity extends AppCompatActivity {

    //    @BindView(R.id.) ;
    @BindView(R.id.toolbar_center_back)
    Toolbar toolbar_center_back;
    @BindView(R.id.txt_dev_address)
    TextView txt_dev_address;
    @BindView(R.id.img_qrcode)
    ImageView img_qrcode;
    @BindView(R.id.img_copy_address)
    ImageView img_copy_address;
    @BindView(R.id.lnr_down)
    LinearLayout lnr_down;
    @BindView(R.id.btn_share_qrcode)
    Button btn_share_qrcode;
    @BindView(R.id.img_coin_logo)
    ImageView img_coin_logo;
    @BindView(R.id.txt_coin_value)
    TextView txt_coin_value;
    @BindView(R.id.txt_wallet_name)
    TextView txt_wallet_name;
    @BindView(R.id.txt_note_dev_add)
    TextView txt_note_dev_add;
    @BindView(R.id.txt_lbl_coin_add)
    TextView txt_lbl_coin_add;



    AccountWallet selectedAccountWallet;


    @Override
    protected void onResume() {
        super.onResume();
        myApplication.disableScreenCapture(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_coin);

        ButterKnife.bind(this);

        toolbar_center_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        selectedAccountWallet = new AccountWallet();

        Bundle bundle = getIntent().getExtras();
        selectedAccountWallet = bundle.getParcelable(CONSTANTS.selectedAccountWallet);

        txt_dev_address.setText(selectedAccountWallet.getStr_data_address());
        txt_note_dev_add.setText(getResources().getString(R.string.attention_dev_address1)+" "+selectedAccountWallet.getAllCoins().getStr_coin_code()+". "+getResources().getString(R.string.attention_dev_address2));

        Picasso.with(ReceiveCoinActivity.this).load(selectedAccountWallet.getAllCoins().getStr_coin_logo()).into(img_coin_logo);
        txt_coin_value.setText(selectedAccountWallet.getAllCoins().getStr_coin_code());
        txt_wallet_name.setText(selectedAccountWallet.getStr_data_walletName());
        txt_lbl_coin_add.setText(selectedAccountWallet.getAllCoins().getStr_coin_code()+ " Address");

        img_copy_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtilities.copyToClipboard(ReceiveCoinActivity.this, selectedAccountWallet.getStr_data_address(), selectedAccountWallet.getAllCoins().getStr_coin_name());
            }
        });

//           QR Code Generator
        CommonUtilities.qrCodeGenerate(selectedAccountWallet.getStr_data_address(), img_qrcode, ReceiveCoinActivity.this);

        btn_share_qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//           Sharing Address
                CommonUtilities.shareAddress(selectedAccountWallet.getStr_data_address(), ReceiveCoinActivity.this);
            }
        });


    }



}
