package com.cryptowallet.deviantx.UI.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.ServiceAPIs.UserControllerApi;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;
import com.cryptowallet.deviantx.Utilities.DeviantXApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.net.URLEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TwoFAEnable1Activity extends AppCompatActivity {

    //    @BindView(R.id.)
//            ;
    @BindView(R.id.toolbar_center_back)
    Toolbar toolbar_center_back;
    @BindView(R.id.img_qrcode)
    ImageView img_qrcode;
    @BindView(R.id.img_copy_address)
    ImageView img_copy_address;
    @BindView(R.id.txt_code)
    TextView txt_code;
    @BindView(R.id.btn_next)
    Button btn_next;


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String loginResponseMsg, loginResponseStatus, loginResponseData, twoFACode, tkn;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_fa_enable1);

        ButterKnife.bind(this);

        sharedPreferences = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        tkn = sharedPreferences.getString(CONSTANTS.token, "");
        if (CommonUtilities.isConnectionAvailable(TwoFAEnable1Activity.this)) {
            get2FACode(tkn);
        } else {
            CommonUtilities.ShowToastMessage(TwoFAEnable1Activity.this, getResources().getString(R.string.internetconnection));
        }


        toolbar_center_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        img_copy_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twoFACode = sharedPreferences.getString(CONSTANTS.twoFACode, null);
                if (!twoFACode.isEmpty()) {
                    CommonUtilities.copyToClipboard(TwoFAEnable1Activity.this, twoFACode, " ");
                } else {
                    CommonUtilities.ShowToastMessage(TwoFAEnable1Activity.this, getResources().getString(R.string.failed_copy));
                }
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                twoFACode = sharedPreferences.getString(CONSTANTS.twoFACode, null);
                Intent intent = new Intent(TwoFAEnable1Activity.this, TwoFAEnable2Activity.class);
                startActivity(intent);
//                finish();
            }
        });

    }

    private void get2FACode(String tkn) {
        try {
            progressDialog = ProgressDialog.show(TwoFAEnable1Activity.this, "", getResources().getString(R.string.please_wait), true);
            UserControllerApi apiService = DeviantXApiClient.getClient().create(UserControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.get2FACode(CONSTANTS.DeviantMulti + tkn);
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
                                loginResponseData = jsonObject.getString("data");
                                JSONObject jsonObjectData = new JSONObject(loginResponseData);
                                try {
                                    twoFACode = jsonObjectData.getString("twoFactorSecretCode");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                editor.putString(CONSTANTS.twoFACode, twoFACode);
                                editor.apply();
                                txt_code.setText(twoFACode);
                                String email = sharedPreferences.getString(CONSTANTS.email, "");
//                                String email=sharedPreferences.getString(CONSTANTS.email,"");
                                String query = URLEncoder.encode("otpauth://totp/" + email + "?secret=" + twoFACode + "&issuer=Deviant X");

//                                finish();
//                                CommonUtilities.ShowToastMessage(TwoFAEnable1Activity.this, getResources().getString(R.string.pswd_changed_succcess));
                            } else {
                                CommonUtilities.ShowToastMessage(TwoFAEnable1Activity.this, loginResponseMsg);
                            }


                        } else {
                            CommonUtilities.ShowToastMessage(TwoFAEnable1Activity.this, loginResponseMsg);
//                            Toast.makeText(getApplicationContext(), responsevalue, Toast.LENGTH_LONG).show();
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(TwoFAEnable1Activity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(TwoFAEnable1Activity.this, getResources().getString(R.string.Timeout));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Timeout), Toast.LENGTH_SHORT).show();
                    } else if (t instanceof java.net.ConnectException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(TwoFAEnable1Activity.this, getResources().getString(R.string.networkerror));
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.networkerror), Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(TwoFAEnable1Activity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception ex) {
            progressDialog.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(TwoFAEnable1Activity.this, getResources().getString(R.string.errortxt));
//            Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
        }


    }

}
