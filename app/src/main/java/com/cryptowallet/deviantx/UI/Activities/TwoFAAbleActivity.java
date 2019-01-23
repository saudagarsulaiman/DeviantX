package com.cryptowallet.deviantx.UI.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.ServiceAPIs.UserControllerApi;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;
import com.cryptowallet.deviantx.Utilities.DeviantXApiClient;

import org.json.JSONObject;

import java.net.SocketTimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cryptowallet.deviantx.Utilities.MyApplication.myApplication;

public class TwoFAAbleActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_center_back)
    Toolbar toolbar_center_back;
    @BindView(R.id.edt_login_pswd)
    EditText edt_login_pswd;
    @BindView(R.id.edt_google_auth_code)
    EditText edt_google_auth_code;
    @BindView(R.id.btn_submit)
    Button btn_submit;
    @BindView(R.id.txt_ttl_2FA)
    TextView txt_ttl_2FA;
    @BindView(R.id.txt_ttl_note)
    TextView txt_ttl_note;


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String loginResponseMsg, loginResponseStatus, tkn;

    ProgressDialog progressDialog;




    @Override
    protected void onResume() {
        super.onResume();
        myApplication.disableScreenCapture(this);
        CommonUtilities.serviceStart(TwoFAAbleActivity.this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_HOME) {
//            Log.e("home key pressed", "****");
            // write your code here to stop the activity
            CommonUtilities.serviceStop(TwoFAAbleActivity.this);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
//        Log.e("home key pressed on pause", "****");
        // write your code here to stop your service
        CommonUtilities.serviceStop(TwoFAAbleActivity.this);
        super.onPause();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_fa_able);

        ButterKnife.bind(this);

        sharedPreferences = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if (myApplication.get2FA()){
            txt_ttl_2FA.setText(getResources().getString(R.string.disable_2fa));
            txt_ttl_note.setText(getResources().getString(R.string.disable_2fa_note));
        }
        else{
            txt_ttl_2FA.setText(getResources().getString(R.string.enable_2fa));
            txt_ttl_note.setText(getResources().getString(R.string.enable_2fa_note));
        }


        toolbar_center_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login_pswd = edt_login_pswd.getText().toString().trim();
                String google_auth_code = edt_google_auth_code.getText().toString().trim();

                if (!login_pswd.isEmpty()) {
                    if (!google_auth_code.isEmpty()) {
                        if (myApplication.get2FA())
                            disable2FA(login_pswd, google_auth_code);
                        else
                            enable2FA(login_pswd, google_auth_code);
                    } else {
                        CommonUtilities.ShowToastMessage(TwoFAAbleActivity.this, getResources().getString(R.string.empty_google_auth_code));
                    }
                } else {
                    CommonUtilities.ShowToastMessage(TwoFAAbleActivity.this, getResources().getString(R.string.empty_pswd));
                }

               /* Intent intent = new Intent(TwoFAAbleActivity.this, AppSettingsActivity.class);
                startActivity(intent);
                finish();*/
            }
        });

    }

    private void enable2FA(String login_pswd, String google_auth_code) {
        try {
            tkn = sharedPreferences.getString(CONSTANTS.token, "");
            progressDialog = ProgressDialog.show(TwoFAAbleActivity.this, "", getResources().getString(R.string.please_wait), true);
            UserControllerApi apiService = DeviantXApiClient.getClient().create(UserControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.enable2FA(login_pswd, google_auth_code, CONSTANTS.DeviantMulti + tkn);
            apiResponse.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responsevalue = response.body().string();

                        if (!responsevalue.isEmpty() && responsevalue != null) {
                            progressDialog.dismiss();

                            JSONObject jsonObject = new JSONObject(responsevalue);
                            loginResponseMsg = jsonObject.getString("msg");
                            loginResponseStatus = jsonObject.getString("status");
                            if (loginResponseStatus.equals("true")) {
                                editor.putBoolean(CONSTANTS.twoFactorAuth, true);
                                editor.apply();
                                myApplication.set2FA(true);
                                Intent intent = new Intent(TwoFAAbleActivity.this, AppSettingsActivity.class);
                                startActivity(intent);
                                finish();
                                CommonUtilities.ShowToastMessage(TwoFAAbleActivity.this, /*loginResponseMsg*/getResources().getString(R.string.enabled_2fa));
                            } else {
                                editor.putBoolean(CONSTANTS.twoFactorAuth, false);
                                myApplication.set2FA(false);
                                editor.apply();
                                CommonUtilities.ShowToastMessage(TwoFAAbleActivity.this, loginResponseMsg);
                            }
                        } else {
                            CommonUtilities.ShowToastMessage(TwoFAAbleActivity.this, loginResponseMsg);
//                            Toast.makeText(getApplicationContext(), responsevalue, Toast.LENGTH_LONG).show();
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(TwoFAAbleActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(TwoFAAbleActivity.this, getResources().getString(R.string.Timeout));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Timeout), Toast.LENGTH_SHORT).show();
                    } else if (t instanceof java.net.ConnectException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(TwoFAAbleActivity.this, getResources().getString(R.string.networkerror));
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.networkerror), Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(TwoFAAbleActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception ex) {
            progressDialog.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(TwoFAAbleActivity.this, getResources().getString(R.string.errortxt));
//            Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
        }

    }

    private void disable2FA(String login_pswd, String google_auth_code) {
        try {
            tkn = sharedPreferences.getString(CONSTANTS.token, "");
            progressDialog = ProgressDialog.show(TwoFAAbleActivity.this, "", getResources().getString(R.string.please_wait), true);
            UserControllerApi apiService = DeviantXApiClient.getClient().create(UserControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.disable2FA(login_pswd, google_auth_code, CONSTANTS.DeviantMulti + tkn);
            apiResponse.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responsevalue = response.body().string();

                        if (!responsevalue.isEmpty() && responsevalue != null) {
                            progressDialog.dismiss();

                            JSONObject jsonObject = new JSONObject(responsevalue);
                            loginResponseMsg = jsonObject.getString("msg");
                            loginResponseStatus = jsonObject.getString("status");
                            if (loginResponseStatus.equals("true")) {
                                editor.putBoolean(CONSTANTS.twoFactorAuth, false);
                                editor.apply();
                                myApplication.set2FA(false);
                                Intent intent = new Intent(TwoFAAbleActivity.this, AppSettingsActivity.class);
                                startActivity(intent);
                                finish();
                                CommonUtilities.ShowToastMessage(TwoFAAbleActivity.this, /*loginResponseMsg*/getResources().getString(R.string.disabled_2fa));
                            } else {
                                editor.putBoolean(CONSTANTS.twoFactorAuth, true);
                                editor.apply();
                                myApplication.set2FA(true);
                                CommonUtilities.ShowToastMessage(TwoFAAbleActivity.this, loginResponseMsg);
                            }
                        } else {
                            CommonUtilities.ShowToastMessage(TwoFAAbleActivity.this, loginResponseMsg);
//                            Toast.makeText(getApplicationContext(), responsevalue, Toast.LENGTH_LONG).show();
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(TwoFAAbleActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(TwoFAAbleActivity.this, getResources().getString(R.string.Timeout));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Timeout), Toast.LENGTH_SHORT).show();
                    } else if (t instanceof java.net.ConnectException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(TwoFAAbleActivity.this, getResources().getString(R.string.networkerror));
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.networkerror), Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(TwoFAAbleActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception ex) {
            progressDialog.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(TwoFAAbleActivity.this, getResources().getString(R.string.errortxt));
//            Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*Intent intent = new Intent(TwoFAAbleActivity.this, AppSettingsActivity.class);
        startActivity(intent);
        finish();*/
    }
}
