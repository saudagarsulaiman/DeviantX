package com.cryptowallet.deviantx.UI.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.ServiceAPIs.UserControllerApi;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;
import com.cryptowallet.deviantx.Utilities.DeviantXApiClient;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import org.json.JSONObject;

import java.net.SocketTimeoutException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.parse.ManifestInfo.getVersionName;

public class SplashScreenActivity extends AppCompatActivity {

    /*
        ProgressDialog progressDialog;
    */
    String loginResponseMsg, loginResponseStatus, loginResponseData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (CommonUtilities.isConnectionAvailable(SplashScreenActivity.this)) {
            getVersionNameBE();
        } else {
            CommonUtilities.ShowToastMessage(SplashScreenActivity.this, getResources().getString(R.string.internetconnection));
        }

    }

    private void getVersionNameBE() {
        try {
/*
            progressDialog = ProgressDialog.show(SplashScreenActivity.this, "", getResources().getString(R.string.please_wait), true);
*/
            UserControllerApi apiService = DeviantXApiClient.getClient().create(UserControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.getVersionCode();
            apiResponse.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responsevalue = response.body().string();

                        if (!responsevalue.isEmpty() && responsevalue != null) {
/*
                            progressDialog.dismiss();
*/
                            JSONObject jsonObject = new JSONObject(responsevalue);
                            loginResponseMsg = jsonObject.getString("msg");
                            loginResponseStatus = jsonObject.getString("status");
                            if (loginResponseStatus.equals("true")) {
                                loginResponseData = jsonObject.getString("data");
                                if (getVersionName().equals(loginResponseData)) {
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                SharedPreferences prefs = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
                                                SharedPreferences.Editor editor = prefs.edit();

                                                boolean ft = prefs.getBoolean(CONSTANTS.ft, true);

                                                if (ft) {
                                                    editor.clear();
                                                    editor.putBoolean(CONSTANTS.ft, false);
                                                    editor.apply();
                                                }

                                                String token = prefs.getString(CONSTANTS.token, null);
                                                boolean seed = prefs.getBoolean(CONSTANTS.seed, false);
                                                boolean empty_wallet = prefs.getBoolean(CONSTANTS.empty_wallet, false);
                                                boolean status2FA = prefs.getBoolean(CONSTANTS.twoFactorAuth, false);
                                                boolean login2FA = prefs.getBoolean(CONSTANTS.login2FA, false);
                                                boolean app_pin = prefs.getBoolean(CONSTANTS.is_app_pin, false);

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
                                                                if (status2FA) {
                                                                    if (login2FA) {
                                                                        Intent intent = new Intent(SplashScreenActivity.this, DashBoardActivity.class);
                                                                        startActivity(intent);
                                                                        finish();
                                                                    } else {
                                                                        Intent intent = new Intent(SplashScreenActivity.this, WelcomeActivity.class);
                                                                        startActivity(intent);
                                                                        finish();
                                                                    }
                                                                } else {
                                                                    Intent intent = new Intent(SplashScreenActivity.this, WelcomeActivity.class);
                                                                    startActivity(intent);
                                                                    finish();
                                                                }
                                                            }
                                                        } else {
                                                            Intent intent = new Intent(SplashScreenActivity.this, WelcomeActivity.class);
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
                                } else {
                                    displayWarningDialog();
/*
                                    CommonUtilities.ShowToastMessage(SplashScreenActivity.this, "The current version of the app " + getVersionName() + " is no longer supported. Please update the app to the latest version. Thanks!");
*/
                                }
                            } else {
                                CommonUtilities.ShowToastMessage(SplashScreenActivity.this, loginResponseMsg);
                            }
                        } else {
/*
                            progressDialog.dismiss();
*/
                            CommonUtilities.ShowToastMessage(SplashScreenActivity.this, loginResponseMsg);
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
/*
                        progressDialog.dismiss();
*/
                        CommonUtilities.ShowToastMessage(SplashScreenActivity.this, getResources().getString(R.string.errortxt));
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
/*
                        progressDialog.dismiss();
*/
                        CommonUtilities.ShowToastMessage(SplashScreenActivity.this, getResources().getString(R.string.Timeout));
                    } else if (t instanceof java.net.ConnectException) {
/*
                        progressDialog.dismiss();
*/
                        CommonUtilities.ShowToastMessage(SplashScreenActivity.this, getResources().getString(R.string.networkerror));
                    } else {
/*
                        progressDialog.dismiss();
*/
                        CommonUtilities.ShowToastMessage(SplashScreenActivity.this, getResources().getString(R.string.errortxt));
                    }
                }
            });
        } catch (Exception ex) {
/*
            progressDialog.dismiss();
*/
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(SplashScreenActivity.this, getResources().getString(R.string.errortxt));
        }
    }

    private void displayWarningDialog() {
        ViewHolder viewHolder = new ViewHolder(R.layout.dialog_update_app);
        final DialogPlus dialog = DialogPlus.newDialog(SplashScreenActivity.this)
                .setContentHolder(viewHolder)
                .setGravity(Gravity.BOTTOM)
                .setCancelable(false)
                .setInAnimation(R.anim.slide_in_bottom)
                .setOutAnimation(R.anim.slide_out_bottom)
                .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .create();

//                Initializing Widgets
        View view = dialog.getHolderView();

        TextView txt_close = view.findViewById(R.id.txt_close);
        TextView txt_warning = view.findViewById(R.id.txt_warning);

        txt_warning.setText("The current version of the app " + getVersionName() + " is no longer supported. Please update the app to the latest version. Thanks!");
        txt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                onBackPressed();
            }
        });
//                Displaying DialogPlus
        dialog.show();
    }


}
