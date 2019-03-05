package com.cryptowallet.deviantx.UI.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    SharedPreferences prefs = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
                    String token = prefs.getString(CONSTANTS.token, null);
                    boolean seed = prefs.getBoolean(CONSTANTS.seed, false);
                    boolean empty_wallet = prefs.getBoolean(CONSTANTS.empty_wallet, false);
                    boolean status2FA = prefs.getBoolean(CONSTANTS.twoFactorAuth, false);
                    boolean login2FA = prefs.getBoolean(CONSTANTS.login2FA, false);
                    boolean app_pin = prefs.getBoolean(CONSTANTS.is_app_pin, false);
                   /* String fcm_id = prefs.getString(CONSTANTS.reg_ID, null);
                    if (fcm_id == null) {
                        MyFirebaseInstanceIDService.getToken(SplashScreenActivity.this);
                        String id = prefs.getString(CONSTANTS.reg_ID, null);
                        editor.putString("redId", id);
                        editor.apply();
                    }*/
                    if (app_pin) {
                        Intent intent = new Intent(SplashScreenActivity.this, AppPinActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        if (token != null) {
                            if (seed) {
                                if (empty_wallet) {
                                    Intent intent = new Intent(SplashScreenActivity.this, WelcomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    if (!status2FA /*&& !login2FA*/) {
                                        Intent intent = new Intent(SplashScreenActivity.this, WelcomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Intent intent = new Intent(SplashScreenActivity.this, DashBoardActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            } else {
                                Intent intent = new Intent(SplashScreenActivity.this, WelcomeActivity.class);
//                            CommonUtilities.ShowToastMessage(SplashScreenActivity.this, getResources().getString(R.string.please_add_seed));
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            Intent intent = new Intent(SplashScreenActivity.this, WelcomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                } catch (NullPointerException e) {
                    Intent intent = new Intent(SplashScreenActivity.this, WelcomeActivity.class);
                    startActivity(intent);
                    finish();
                    e.printStackTrace();
                }
            }
        }, 2000);


    }


}
