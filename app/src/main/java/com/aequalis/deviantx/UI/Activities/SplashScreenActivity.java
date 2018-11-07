package com.aequalis.deviantx.UI.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.aequalis.deviantx.R;
import com.aequalis.deviantx.ServiceAPIs.AuthenticationApi;
import com.aequalis.deviantx.Utilities.CONSTANTS;
import com.aequalis.deviantx.Utilities.CommonUtilities;
import com.aequalis.deviantx.Utilities.DeviantXApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketTimeoutException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                    if (token != null) {
                        if (seed) {
                            Intent intent = new Intent(SplashScreenActivity.this, DashBoardActivity.class);
                            startActivity(intent);
                            finish();
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
