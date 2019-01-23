package com.cryptowallet.deviantx.UI.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AppPinActivity extends AppCompatActivity {

    @BindView(R.id.edt_pin)
    EditText edt_pin;
    @BindView(R.id.btn_continue)
    Button btn_continue;


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_pin);

        ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();


        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pin = edt_pin.getText().toString().trim();
                String my_pin = sharedPreferences.getString(CONSTANTS.app_pin, "DeviantX");
                if (pin.equals(my_pin)) {
                    Intent intent = new Intent(AppPinActivity.this, DashBoardActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    CommonUtilities.ShowToastMessage(AppPinActivity.this, getResources().getString(R.string.invalid_pin));
                }
            }
        });


    }
}
