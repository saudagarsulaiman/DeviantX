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
import android.widget.LinearLayout;
import android.widget.TextView;

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
    @BindView(R.id.txt_pin)
    TextView txt_pin;
    @BindView(R.id.edt_old_pin)
    EditText edt_old_pin;
    @BindView(R.id.lnr_reset_pin)
    LinearLayout lnr_reset_pin;

    String avail_pin;
    boolean isPin;

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

        sharedPreferences = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        avail_pin = sharedPreferences.getString(CONSTANTS.app_pin, "DeviantX");

        if (avail_pin.equals("DeviantX")) {
            lnr_reset_pin.setVisibility(View.GONE);
            txt_pin.setText(getResources().getString(R.string.set_pin));
            btn_set_pin.setText(getResources().getString(R.string.set_pin));
        } else {
            lnr_reset_pin.setVisibility(View.VISIBLE);
            txt_pin.setText(getResources().getString(R.string.reset_pin));
            btn_set_pin.setText(getResources().getString(R.string.update));
        }


        btn_set_pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String new_pin = edt_pin.getText().toString().trim();
                String conf_pin = edt_conf_pin.getText().toString().trim();
                String old_pin = edt_old_pin.getText().toString().trim();
                if (avail_pin.equals("DeviantX")) {
                    setPIN(new_pin, conf_pin);
                } else {
                    resetPIN(avail_pin, old_pin, new_pin, conf_pin);
                }
            }
        });

    }

    private void resetPIN(String avail_pin, String old_pin, String new_pin, String conf_pin) {

        if (!old_pin.isEmpty()) {
            if (!new_pin.isEmpty()) {
                if (!conf_pin.isEmpty()) {
                    if (new_pin.length() == 4) {
                        if (old_pin.equals(avail_pin)) {
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
                                CommonUtilities.ShowToastMessage(SetAppPinActivity.this, getResources().getString(R.string.unmatch_new_pin));
                            }
                        } else {
                            CommonUtilities.ShowToastMessage(SetAppPinActivity.this, getResources().getString(R.string.unmatch_old_pin));
                        }
                    } else {
                        CommonUtilities.ShowToastMessage(SetAppPinActivity.this, getResources().getString(R.string.invalid_pin));
                    }
                } else {
                    CommonUtilities.ShowToastMessage(SetAppPinActivity.this, getResources().getString(R.string.empty_conf_pin));
                }
            } else {
                CommonUtilities.ShowToastMessage(SetAppPinActivity.this, getResources().getString(R.string.empty_new_pin));
            }
        } else {
            CommonUtilities.ShowToastMessage(SetAppPinActivity.this, getResources().getString(R.string.empty_old_pin));
        }
    }

    private void setPIN(String new_pin, String conf_pin) {
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SetAppPinActivity.this, AppSettingsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}


