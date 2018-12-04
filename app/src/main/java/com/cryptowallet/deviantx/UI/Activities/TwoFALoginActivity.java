package com.cryptowallet.deviantx.UI.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.ServiceAPIs.AuthenticationApi;
import com.cryptowallet.deviantx.ServiceAPIs.UserControllerApi;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;
import com.cryptowallet.deviantx.Utilities.DeviantXApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketTimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TwoFALoginActivity extends AppCompatActivity {

    //    @BindView(R.id.)
//    ;
    @BindView(R.id.toolbar_center_back)
    Toolbar toolbar_center_back;
    @BindView(R.id.edt_google_auth_code)
    EditText edt_google_auth_code;
    @BindView(R.id.btn_submit)
    Button btn_submit;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;

    String regResponseMsg, regResponseStatus, regResponsedata;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_fa_login);

        ButterKnife.bind(this);

        sharedPreferences = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();


        toolbar_center_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String google_auth_code = edt_google_auth_code.getText().toString();
                if (!google_auth_code.isEmpty()) {
                    login2FA(google_auth_code);
                } else {
                    CommonUtilities.ShowToastMessage(TwoFALoginActivity.this, getResources().getString(R.string.empty_google_auth_code));
                }

            }
        });
    }


    private void login2FA(String google_auth_code) {
        try {
            JSONObject params = new JSONObject();
            String s_email = sharedPreferences.getString(CONSTANTS.email, "");
            String s_pswd = sharedPreferences.getString(CONSTANTS.pswd, "");
            try {
                params.put("totp", google_auth_code);
                params.put("password", s_pswd);
                params.put("email", s_email);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialog = ProgressDialog.show(TwoFALoginActivity.this, " ", getResources().getString(R.string.please_wait), true);
            AuthenticationApi apiService = DeviantXApiClient.getClient().create(AuthenticationApi.class);
            Call<ResponseBody> apiResponse = apiService.Login2FA(params.toString());
            apiResponse.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responsevalue = response.body().string();

                        if (!responsevalue.isEmpty() && responsevalue != null) {
                            progressDialog.dismiss();

                            JSONObject jsonObject = new JSONObject(responsevalue);
                            regResponseMsg = jsonObject.getString("msg");
                            regResponseStatus = jsonObject.getString("status");
                            if (regResponseStatus.equals("true")) {
                                regResponsedata = jsonObject.getString("data");
                                CommonUtilities.ShowToastMessage(TwoFALoginActivity.this, getResources().getString(R.string.login_success));
                                Intent intent = new Intent(TwoFALoginActivity.this, DashBoardActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
//                                Log.i(CONSTANTS.TAG, "onResponse:\n" + loginResponseMsg);
                            } else {
                                CommonUtilities.ShowToastMessage(TwoFALoginActivity.this, regResponseMsg);
                            }
                        } else {
                            CommonUtilities.ShowToastMessage(TwoFALoginActivity.this, regResponseMsg);
//                            Toast.makeText(getApplicationContext(), responsevalue, Toast.LENGTH_LONG).show();
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(TwoFALoginActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(TwoFALoginActivity.this, getResources().getString(R.string.Timeout));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Timeout), Toast.LENGTH_SHORT).show();
                    } else if (t instanceof java.net.ConnectException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(TwoFALoginActivity.this, getResources().getString(R.string.networkerror));
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.networkerror), Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(TwoFALoginActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception ex) {
            progressDialog.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(TwoFALoginActivity.this, getResources().getString(R.string.errortxt));
//            Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
        }
    }


}