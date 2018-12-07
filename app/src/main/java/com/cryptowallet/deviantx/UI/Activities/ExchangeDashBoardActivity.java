package com.cryptowallet.deviantx.UI.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;

public class ExchangeDashBoardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_dashboard);
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ExchangeDashBoardActivity.this, DashBoardActivity.class);
        intent.putExtra(CONSTANTS.seletedTab, 0);
        startActivity(intent);
    }


}
