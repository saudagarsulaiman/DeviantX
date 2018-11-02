package com.aequalis.deviantx.UI.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
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

    Handler handler;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    Boolean first_time = true;
    String email = "email", pswd = "pswd", usrnm = "usrnm", tkn = "tkn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        SharedPreferences prefs = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

//        first_time = sharedPreferences.getBoolean(CONSTANTS.first_time, true);
        try {
            email = sharedPreferences.getString(CONSTANTS.email, "email");
            usrnm = sharedPreferences.getString(CONSTANTS.usrnm, "usrnm");
            pswd = sharedPreferences.getString(CONSTANTS.pswd, "pswd");
            tkn = sharedPreferences.getString(CONSTANTS.pswd, "tkn");
        } catch (Exception e) {
            e.printStackTrace();
        }
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                try {
//                if (first_time) {
//                Intent intent = new Intent(SplashScreenActivity.this, WelcomeActivity.class);
//                startActivity(intent);
//                    editor.putBoolean(CONSTANTS.first_time, false);
//                    editor.apply();
//                } else {
//                    String email = sharedPreferences.getString(CONSTANTS.email, " ");
//                    String usrnm = sharedPreferences.getString(CONSTANTS.usrnm, " ");
//                    String pswd = sharedPreferences.getString(CONSTANTS.pswd, " ");
//                if (!email.trim().isEmpty() && !usrnm.trim().isEmpty() && !pswd.trim().isEmpty()) {
                if (!email.equals("email") && !usrnm.equals("usrnm") && !pswd.equals("pswd")) {
                    try {
                        email = sharedPreferences.getString(CONSTANTS.email, "email");
                        usrnm = sharedPreferences.getString(CONSTANTS.usrnm, "usrnm");
                        pswd = sharedPreferences.getString(CONSTANTS.pswd, "pswd");
                        tkn = sharedPreferences.getString(CONSTANTS.pswd, "tkn");
                        Intent intent = new Intent(SplashScreenActivity.this, DashBoardActivity.class);
                        startActivity(intent);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Intent intent = new Intent(SplashScreenActivity.this, WelcomeActivity.class);
                    startActivity(intent);
                }
//                }
                finish();

//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        }, 3000);


    }

//    private void loggingInAccount(final String s_email, final String s_pswd) {
//        try {
//            JSONObject params = new JSONObject();
//            try {
//                params.put("password", s_pswd);
//                params.put("email", s_email);
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            progressDialog = ProgressDialog.show(LoginActivity.this, "", getResources().getString(R.string.please_wait), true);
//            AuthenticationApi apiService = DeviantXApiClient.getClient().create(AuthenticationApi.class);
//            Call<ResponseBody> apiResponse = apiService.loginAccount(params.toString());
//            apiResponse.enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                    try {
//                        String responsevalue = response.body().string();
//
//                        if (!responsevalue.isEmpty() && responsevalue != null) {
//                            progressDialog.dismiss();
//
//                            JSONObject jsonObject = new JSONObject(responsevalue);
//                            loginResponseMsg = jsonObject.getString("msg");
//                            loginResponseStatus = jsonObject.getString("status");
//
//                            if (loginResponseMsg.equals("Email is not yet verified")) {
//                                editor.putString(CONSTANTS.email, s_email);
//                                editor.putString(CONSTANTS.pswd, s_pswd);
//                                editor.apply();
//
//                                ShowTokenDialog();
//
//                            } else {
//                                if (loginResponseStatus.equals("true")) {
//                                    loginResponseData = jsonObject.getString("data");
//
//                                    JSONObject jsonObjectData = new JSONObject(loginResponseData);
//                                    loginResponseDataToken = jsonObjectData.getString("token");
//
//                                    loginResponseDataUser = jsonObjectData.getString("user");
//
//                                    JSONObject jsonObjectDataUser = new JSONObject(loginResponseDataUser);
//
//                                    String username= jsonObjectDataUser.getString("userName");
//
//                                    editor.putString(CONSTANTS.usrnm,username);
//                                    editor.putString(CONSTANTS.email, s_email);
//                                    editor.putString(CONSTANTS.pswd, s_pswd);
//                                    editor.putString(CONSTANTS.token, loginResponseDataToken);
//                                    editor.commit();
//
//                                    Intent intent = new Intent(LoginActivity.this, DashBoardActivity.class);
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    startActivity(intent);
//                                    CommonUtilities.ShowToastMessage(LoginActivity.this, getResources().getString(R.string.login_success));
//
//                                } else {
//                                    CommonUtilities.ShowToastMessage(LoginActivity.this, loginResponseMsg);
//                                }
//
//                            }
//
//                        } else {
//                            CommonUtilities.ShowToastMessage(LoginActivity.this, loginResponseMsg);
////                            Toast.makeText(getApplicationContext(), responsevalue, Toast.LENGTH_LONG).show();
//                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
//                        }
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        progressDialog.dismiss();
//                        CommonUtilities.ShowToastMessage(LoginActivity.this, getResources().getString(R.string.errortxt));
////                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    if (t instanceof SocketTimeoutException) {
//                        progressDialog.dismiss();
//                        CommonUtilities.ShowToastMessage(LoginActivity.this, getResources().getString(R.string.Timeout));
////                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Timeout), Toast.LENGTH_SHORT).show();
//                    } else if (t instanceof java.net.ConnectException) {
//                        progressDialog.dismiss();
//                        CommonUtilities.ShowToastMessage(LoginActivity.this, getResources().getString(R.string.networkerror));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.networkerror), Toast.LENGTH_SHORT).show();
//                    } else {
//                        progressDialog.dismiss();
//                        CommonUtilities.ShowToastMessage(LoginActivity.this, getResources().getString(R.string.errortxt));
////                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//        } catch (Exception ex) {
//            progressDialog.dismiss();
//            ex.printStackTrace();
//            CommonUtilities.ShowToastMessage(LoginActivity.this, getResources().getString(R.string.errortxt));
////            Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
//        }
//
//
//    }

}
