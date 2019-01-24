package com.cryptowallet.deviantx.UI.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cryptowallet.deviantx.Utilities.MyApplication.myApplication;

public class SetAppPinActivity extends AppCompatActivity {

    @BindView(R.id.edt_pin)
    EditText edt_pin;
    @BindView(R.id.edt_conf_pin)
    EditText edt_conf_pin;
    @BindView(R.id.btn_set_pin)
    Button btn_set_pin;
    @BindView(R.id.toolbar_center_back)
    Toolbar toolbar_center_back;


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onResume() {
        super.onResume();
        myApplication.disableScreenCapture(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_app_pin);

        ButterKnife.bind(this);

        sharedPreferences = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();


        toolbar_center_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
               /* Intent intent = new Intent(SetAppPinActivity.this, AppSettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();*/
            }
        });

        btn_set_pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String new_pin = edt_pin.getText().toString().trim();
                String conf_pin = edt_conf_pin.getText().toString().trim();
                if (!new_pin.isEmpty()) {
                    if (!conf_pin.isEmpty()) {
                        if (new_pin.length() == 4) {
                            if (new_pin.equals(conf_pin)) {
                                editor.putString(CONSTANTS.app_pin, new_pin);
                                editor.putBoolean(CONSTANTS.is_app_pin, true);
                                editor.apply();
                                myApplication.setAppPin(true);
                                CommonUtilities.ShowToastMessage(SetAppPinActivity.this, getResources().getString(R.string.pin_active));
                                Intent intent = new Intent(SetAppPinActivity.this, AppSettingsActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                CommonUtilities.ShowToastMessage(SetAppPinActivity.this, getResources().getString(R.string.unmatch_pin));
                            }
                        } else {
                            CommonUtilities.ShowToastMessage(SetAppPinActivity.this, getResources().getString(R.string.invalid_pin));
                        }
                    } else {
                        CommonUtilities.ShowToastMessage(SetAppPinActivity.this, getResources().getString(R.string.empty_conf_pin));
                    }
                } else {
                    CommonUtilities.ShowToastMessage(SetAppPinActivity.this, getResources().getString(R.string.empty_pin));
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SetAppPinActivity.this, AppSettingsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}


