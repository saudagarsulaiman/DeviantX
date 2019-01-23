package com.cryptowallet.deviantx.UI.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cryptowallet.deviantx.R;
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

public class RecoveryConfirmActivity extends AppCompatActivity {

    @BindView(R.id.img_center_back)
    ImageView img_center_back;
    @BindView(R.id.edt_new_pswd)
    EditText edt_new_pswd;
    @BindView(R.id.edt_confirm_pswd)
    EditText edt_confirm_pswd;
    @BindView(R.id.btn_change_pswd)
    Button btn_change_pswd;
    @BindView(R.id.edt_token)
    EditText edt_token;

    @BindView(R.id.txt_lower_case)
    TextView txt_lower_case;
    @BindView(R.id.txt_upper_case)
    TextView txt_upper_case;
    @BindView(R.id.txt_number)
    TextView txt_number;
    @BindView(R.id.txt_chars)
    TextView txt_chars;


    String tkn, new_pswd, conf_pswd;

    String loginResponseMsg, loginResponseStatus;

    ProgressDialog progressDialog;
//    SharedPreferences sharedPreferences;
//    SharedPreferences.Editor editor;

//    String email;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery_confirm);

        ButterKnife.bind(this);

//        sharedPreferences = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
//        editor = sharedPreferences.edit();

//        Bundle bundle= getIntent().getExtras();
//        email = bundle.getString(CONSTANTS.email);
//        email = sharedPreferences.getString(CONSTANTS.email,"");

        btn_change_pswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tkn = edt_token.getText().toString().trim();
                String new_pswd = edt_new_pswd.getText().toString().trim();
                String conf_pswd = edt_confirm_pswd.getText().toString().trim();
                CheckingInputs(tkn, new_pswd, conf_pswd/*,email*/);
            }
        });


        img_center_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        edt_new_pswd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                CommonUtilities.matchingPasswordText(RecoveryConfirmActivity.this,text,txt_lower_case,txt_upper_case,txt_number,txt_chars);

            }
        });


    }


    private void CheckingInputs(String tkn, String new_pswd, String conf_pswd/*, String email*/) {
        if (!tkn.isEmpty()) {
            if (!new_pswd.isEmpty()) {
                if (new_pswd.matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{8,25}$")) {
                    if (!conf_pswd.isEmpty()) {
                        if (new_pswd.equals(conf_pswd)) {
                            if (CommonUtilities.isConnectionAvailable(RecoveryConfirmActivity.this)) {
                                invokeEmailRecovery(tkn, new_pswd/*, email*/);
                                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                            } else {
                                CommonUtilities.ShowToastMessage(RecoveryConfirmActivity.this, getResources().getString(R.string.internetconnection));
                            }
                        } else {
                            CommonUtilities.ShowToastMessage(RecoveryConfirmActivity.this, getResources().getString(R.string.unmatch_pswd));
                        }
                    } else {
                        CommonUtilities.ShowToastMessage(RecoveryConfirmActivity.this, getResources().getString(R.string.empty_pswd));
                    }
                } else {
                    CommonUtilities.ShowToastMessage(RecoveryConfirmActivity.this, getResources().getString(R.string.invalid_pswd));
                }
            } else {
                CommonUtilities.ShowToastMessage(RecoveryConfirmActivity.this, getResources().getString(R.string.empty_pswd));
            }
        } else {
            CommonUtilities.ShowToastMessage(RecoveryConfirmActivity.this, getResources().getString(R.string.empty_token));
        }

    }

    private void invokeEmailRecovery(String tkn, final String new_pswd/*, final String email*/) {
        try {
            JSONObject params = new JSONObject();
            try {
                params.put("password", new_pswd);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialog = ProgressDialog.show(RecoveryConfirmActivity.this, "", getResources().getString(R.string.please_wait), true);
            UserControllerApi apiService = DeviantXApiClient.getClient().create(UserControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.recoverEmailPassword(params.toString(), tkn);
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
//                                editor.putString(CONSTANTS.email,email);
//                                editor.putString(CONSTANTS.pswd, new_pswd);
//                                editor.apply();
                                Intent intent = new Intent(RecoveryConfirmActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                CommonUtilities.ShowToastMessage(RecoveryConfirmActivity.this, getResources().getString(R.string.pswd_changed_succcess));
                            } else {
                                CommonUtilities.ShowToastMessage(RecoveryConfirmActivity.this, loginResponseMsg);
                            }


                        } else {
                            CommonUtilities.ShowToastMessage(RecoveryConfirmActivity.this, loginResponseMsg);
//                            Toast.makeText(getApplicationContext(), responsevalue, Toast.LENGTH_LONG).show();
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(RecoveryConfirmActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(RecoveryConfirmActivity.this, getResources().getString(R.string.Timeout));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Timeout), Toast.LENGTH_SHORT).show();
                    } else if (t instanceof java.net.ConnectException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(RecoveryConfirmActivity.this, getResources().getString(R.string.networkerror));
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.networkerror), Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(RecoveryConfirmActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception ex) {
            progressDialog.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(RecoveryConfirmActivity.this, getResources().getString(R.string.errortxt));
//            Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
        }


    }


}
