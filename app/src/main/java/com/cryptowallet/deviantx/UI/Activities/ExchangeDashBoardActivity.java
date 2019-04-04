package com.cryptowallet.deviantx.UI.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.UI.Fragments.ExchangeDashboardFragment;
import com.cryptowallet.deviantx.UI.Fragments.ExchangeFundsFragment;
import com.cryptowallet.deviantx.UI.Fragments.ExchangeMarketFragment;
import com.cryptowallet.deviantx.UI.Fragments.ExchangeSettingsFragment;
import com.cryptowallet.deviantx.UI.Fragments.ExchangeTradeFragment;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cryptowallet.deviantx.Utilities.MyApplication.myApplication;

public class ExchangeDashBoardActivity extends AppCompatActivity {

    //    @BindView(R.id.)
//    ;
    @BindView(R.id.lnr_bnv)
    LinearLayout lnr_bnv;
    @BindView(R.id.lnr_back)
    LinearLayout lnr_back;
    //    @BindView(R.id.lnr_home)
//    LinearLayout lnr_home;
//    @BindView(R.id.lnr_trade)
//    LinearLayout lnr_trade;
//    @BindView(R.id.lnr_funds)
//    LinearLayout lnr_funds;
//    @BindView(R.id.lnr_market)
//    LinearLayout lnr_market;
//    @BindView(R.id.lnr_settings)
//    LinearLayout lnr_settings;
    @BindView(R.id.img_home)
    ImageView img_home;
    @BindView(R.id.img_trade)
    ImageView img_trade;
    @BindView(R.id.img_funds)
    ImageView img_funds;
    @BindView(R.id.img_market)
    ImageView img_market;
    @BindView(R.id.img_settings)
    ImageView img_settings;


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    FragmentManager supportFragmentManager;

    @Override
    protected void onResume() {
        super.onResume();
        myApplication.disableScreenCapture(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_dashboard);

        ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        supportFragmentManager = getSupportFragmentManager();

        lnr_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        BtmClickEvents();
        int selectedTab = (getIntent().getIntExtra(CONSTANTS.seletedTab, 0));
        setCurrentTabFragment(selectedTab);
//        setCurrentTabFragment(0);


    }

    private void BtmClickEvents() {

        img_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentTabFragment(0);
               /* img_home.setBackground(getResources().getDrawable(R.drawable.cir_menublue));
                img_funds.setBackground(getResources().getDrawable(R.color.transparent));
                img_market.setBackground(getResources().getDrawable(R.color.transparent));
                img_settings.setBackground(getResources().getDrawable(R.color.transparent));
                img_trade.setBackground(getResources().getDrawable(R.color.transparent));*/
            }
        });


        img_market.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentTabFragment(1);
                /*img_market.setBackground(getResources().getDrawable(R.drawable.cir_menublue));
                img_funds.setBackground(getResources().getDrawable(R.color.transparent));
                img_home.setBackground(getResources().getDrawable(R.color.transparent));
                img_settings.setBackground(getResources().getDrawable(R.color.transparent));
                img_trade.setBackground(getResources().getDrawable(R.color.transparent));*/
            }
        });

        img_funds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentTabFragment(3);
                /*img_funds.setBackground(getResources().getDrawable(R.drawable.cir_menublue));
                img_home.setBackground(getResources().getDrawable(R.color.transparent));
                img_market.setBackground(getResources().getDrawable(R.color.transparent));
                img_settings.setBackground(getResources().getDrawable(R.color.transparent));
                img_trade.setBackground(getResources().getDrawable(R.color.transparent));*/
            }
        });

        img_trade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentTabFragment(2);
               /* img_trade.setBackground(getResources().getDrawable(R.drawable.cir_menublue));
                img_funds.setBackground(getResources().getDrawable(R.color.transparent));
                img_market.setBackground(getResources().getDrawable(R.color.transparent));
                img_settings.setBackground(getResources().getDrawable(R.color.transparent));
                img_home.setBackground(getResources().getDrawable(R.color.transparent));*/
            }
        });


        img_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentTabFragment(4);
                /*img_settings.setBackground(getResources().getDrawable(R.drawable.cir_menublue));
                img_funds.setBackground(getResources().getDrawable(R.color.transparent));
                img_market.setBackground(getResources().getDrawable(R.color.transparent));
                img_home.setBackground(getResources().getDrawable(R.color.transparent));
                img_trade.setBackground(getResources().getDrawable(R.color.transparent));*/
            }
        });

    }

    private void setCurrentTabFragment(int tabPosition) {
        switch (tabPosition) {
            case 0:
                img_home.setBackground(getResources().getDrawable(R.drawable.cir_menublue));
                img_funds.setBackground(getResources().getDrawable(R.color.transparent));
                img_market.setBackground(getResources().getDrawable(R.color.transparent));
                img_settings.setBackground(getResources().getDrawable(R.color.transparent));
                img_trade.setBackground(getResources().getDrawable(R.color.transparent));
                replaceFragment(new ExchangeDashboardFragment());
//                showFragment(new ExchangeDashboardFragment());
                break;
            case 1:
                img_trade.setBackground(getResources().getDrawable(R.color.transparent));
                img_funds.setBackground(getResources().getDrawable(R.color.transparent));
                img_market.setBackground(getResources().getDrawable(R.drawable.cir_menublue));
                img_settings.setBackground(getResources().getDrawable(R.color.transparent));
                img_home.setBackground(getResources().getDrawable(R.color.transparent));
                replaceFragment(new ExchangeMarketFragment());
//                showFragment(new ExchangeTradeFragment());
                break;
            case 3:
                img_funds.setBackground(getResources().getDrawable(R.drawable.cir_menublue));
                img_home.setBackground(getResources().getDrawable(R.color.transparent));
                img_market.setBackground(getResources().getDrawable(R.color.transparent));
                img_settings.setBackground(getResources().getDrawable(R.color.transparent));
                img_trade.setBackground(getResources().getDrawable(R.color.transparent));
                replaceFragment(new ExchangeFundsFragment());
//                showFragment(new ExchangeFundsFragment());
                break;
            case 2:
                img_market.setBackground(getResources().getDrawable(R.color.transparent));
                img_funds.setBackground(getResources().getDrawable(R.color.transparent));
                img_home.setBackground(getResources().getDrawable(R.color.transparent));
                img_settings.setBackground(getResources().getDrawable(R.color.transparent));
                img_trade.setBackground(getResources().getDrawable(R.drawable.cir_menublue));
                replaceFragment(new ExchangeTradeFragment());
//                showFragment(new ExchangeMarketFragment());
                break;
            case 4:
                img_settings.setBackground(getResources().getDrawable(R.drawable.cir_menublue));
                img_funds.setBackground(getResources().getDrawable(R.color.transparent));
                img_market.setBackground(getResources().getDrawable(R.color.transparent));
                img_home.setBackground(getResources().getDrawable(R.color.transparent));
                img_trade.setBackground(getResources().getDrawable(R.color.transparent));
                replaceFragment(new ExchangeSettingsFragment());
//                showFragment(new ExchangeSettingsFragment());
                break;
        }
    }


    public void replaceFragment(Fragment fragment) {
        Fragment f1 = supportFragmentManager.findFragmentByTag(fragment.getClass().getName());

        if (f1 == null) {
            f1 = fragment;
            supportFragmentManager.beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .add(R.id.frame_container, f1, fragment.getClass().getName())
                    .addToBackStack(fragment.getClass().getName()).commit();
        } else {
            FragmentTransaction transaction = supportFragmentManager.beginTransaction();
            ExchangeDashboardFragment dashboardFragment = (ExchangeDashboardFragment) supportFragmentManager.findFragmentByTag(ExchangeDashboardFragment.class.getName());
            ExchangeMarketFragment marketFragment = (ExchangeMarketFragment) supportFragmentManager.findFragmentByTag(ExchangeMarketFragment.class.getName());
            ExchangeFundsFragment fundsFragment = (ExchangeFundsFragment) supportFragmentManager.findFragmentByTag(ExchangeFundsFragment.class.getName());
            ExchangeTradeFragment tradeFragment = (ExchangeTradeFragment) supportFragmentManager.findFragmentByTag(ExchangeTradeFragment.class.getName());
            ExchangeSettingsFragment settingsFragment = (ExchangeSettingsFragment) supportFragmentManager.findFragmentByTag(ExchangeSettingsFragment.class.getName());

            if (dashboardFragment != null)
                if (dashboardFragment != f1)
                    transaction.hide(dashboardFragment);

            if (marketFragment != null)
                if (marketFragment != f1)
                    transaction.hide(marketFragment);

            if (fundsFragment != null)
                if (fundsFragment != f1)
                    transaction.hide(fundsFragment);

            if (tradeFragment != null)
                if (tradeFragment != f1)
                    transaction.hide(tradeFragment);

            if (settingsFragment != null)
                if (settingsFragment != f1)
                    transaction.hide(settingsFragment);

            transaction.show(f1);
            transaction.commit();
            // supportFragmentManager.beginTransaction()
            //.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            //  .replace(R.id.frame_container, f1, fragment.getClass().getName())
            // .addToBackStack(fragment.getClass().getName()).commit();
        }


    }

/*
    public void showFragment(Fragment fragment*/
    /*, int position*//*
) {
        FragmentTransaction mTransactiont = getSupportFragmentManager().beginTransaction();

        mTransactiont.replace(R.id.frame_container, fragment, fragment.getClass().getName());
        mTransactiont.commit();
    }
*/

    @Override
    public void onBackPressed() {
        if (ExchangeDashboardFragment.stompClient != null)
            if (ExchangeDashboardFragment.stompClient.isConnected())
                ExchangeDashboardFragment.stompClient.disconnect();

        if (ExchangeMarketFragment.stompClient != null)
            if (ExchangeMarketFragment.stompClient.isConnected())
                ExchangeMarketFragment.stompClient.disconnect();

        if (ExchangeTradeFragment.stompClient != null)
            if (ExchangeTradeFragment.stompClient.isConnected())
                ExchangeTradeFragment.stompClient.disconnect();

        Log.e("STOMPCLIENT", "DISCONNECTED ON BACKPRESSED");
        Intent intent = new Intent(ExchangeDashBoardActivity.this, DashBoardActivity.class);
        startActivity(intent);
    }

}
