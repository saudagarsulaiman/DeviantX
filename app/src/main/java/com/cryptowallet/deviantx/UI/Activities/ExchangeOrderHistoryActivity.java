package com.cryptowallet.deviantx.UI.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.UI.Adapters.ExchangeOrderHistoryRAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cryptowallet.deviantx.Utilities.MyApplication.myApplication;

public class ExchangeOrderHistoryActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_center_back)
    Toolbar toolbar_center_back;
    @BindView(R.id.img_search_type)
    ImageView img_search_type;
    @BindView(R.id.edt_search)
    EditText edt_search;
    @BindView(R.id.rview_order_history)
    RecyclerView rview_order_history;
    @BindView(R.id.lnr_no_trans)
    LinearLayout lnr_no_trans;
    @BindView(R.id.lnr_trans_avail)
    LinearLayout lnr_trans_avail;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;


    ExchangeOrderHistoryRAdapter exchangeOrderHistoryRAdapter;
    LinearLayoutManager linearLayoutManager;


    @Override
    protected void onResume() {
        super.onResume();
        myApplication.disableScreenCapture(this);
//        if (walletHistoryRAdapter != null) {
//            walletHistoryRAdapter.setIsHideBalance(myApplication.getHideBalance());
//            walletHistoryRAdapter.notifyDataSetChanged();
//        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_order_history);
        ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        linearLayoutManager = new LinearLayoutManager(ExchangeOrderHistoryActivity.this, LinearLayoutManager.VERTICAL, false);
        rview_order_history.setLayoutManager(linearLayoutManager);

        exchangeOrderHistoryRAdapter = new ExchangeOrderHistoryRAdapter(ExchangeOrderHistoryActivity.this,false);
        rview_order_history.setAdapter(exchangeOrderHistoryRAdapter);

//        Bundle bundle = getIntent().getExtras();
//        selectedAccountWallet = bundle.getParcelable(CONSTANTS.selectedAccountWallet);

//        transactions = new ArrayList<>();
        toolbar_center_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }
}
