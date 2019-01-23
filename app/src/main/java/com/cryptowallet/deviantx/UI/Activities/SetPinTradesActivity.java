package com.cryptowallet.deviantx.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cryptowallet.deviantx.Utilities.MyApplication.myApplication;

public class SetPinTradesActivity extends AppCompatActivity {

    @BindView(R.id.edt_pin)
    EditText edt_pin;
    @BindView(R.id.edt_conf_pin)
    EditText edt_conf_pin;
    @BindView(R.id.btn_send)
    Button btn_send;
    @BindView(R.id.toolbar_center_back)
    Toolbar toolbar_center_back;


    @Override
    protected void onResume() {
        super.onResume();
        myApplication.disableScreenCapture(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pin_trades);
        ButterKnife.bind(this);

        toolbar_center_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SetPinTradesActivity.this, ExchangeDashBoardActivity.class);
                intent.putExtra(CONSTANTS.seletedTab, 4);
                startActivity(intent);
            }
        });

    }
}
