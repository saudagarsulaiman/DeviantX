package com.cryptowallet.deviantx.UI.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.UI.Adapters.MyWalletListRAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cryptowallet.deviantx.Utilities.MyApplication.myApplication;

public class WalletListActivity extends AppCompatActivity {


    @BindView(R.id.toolbar_center_back)
    Toolbar toolbar_center_back;
    @BindView(R.id.rview_my_walletlist)
    RecyclerView rview_my_walletlist;
    @BindView(R.id.btn_create_wallet)
    Button btn_create_wallet;


    LinearLayoutManager linearLayoutManager;
    MyWalletListRAdapter myWalletListRAdapter;

    @Override
    protected void onResume() {
        super.onResume();
        myApplication.disableScreenCapture(this);
    }


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;

    private String loginResponseData, loginResponseStatus, loginResponseMsg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_list);

        ButterKnife.bind(this);

        sharedPreferences = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        toolbar_center_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        linearLayoutManager = new LinearLayoutManager(WalletListActivity.this, LinearLayoutManager.VERTICAL, false);
        rview_my_walletlist.setLayoutManager(linearLayoutManager);

        myWalletListRAdapter = new MyWalletListRAdapter(WalletListActivity.this);
        rview_my_walletlist.setAdapter(myWalletListRAdapter);

        btn_create_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WalletListActivity.this, SetUpWalletActivity.class);
                startActivity(intent);
            }
        });

    }


}
