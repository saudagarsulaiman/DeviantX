package com.cryptowallet.deviantx.UI.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.ServiceAPIs.AuthenticationApi;
import com.cryptowallet.deviantx.ServiceAPIs.UserControllerApi;
import com.cryptowallet.deviantx.ServiceAPIs.WalletControllerApi;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;
import com.cryptowallet.deviantx.Utilities.DeviantXApiClient;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketTimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cryptowallet.deviantx.Utilities.MyApplication.myApplication;

public class LoginActivity extends AppCompatActivity {

    //    @BindView(R.id.) ;
    @BindView(R.id.tool)
    Toolbar toolbar_center_back;
    @BindView(R.id.edt_email)
    EditText edt_email;
    @BindView(R.id.edt_pswd)
    EditText edt_pswd;
    @BindView(R.id.help_txt)
    TextView help_txt;
    @BindView(R.id.txt_seed_recovery)
    TextView txt_seed_recovery;
    @BindView(R.id.btn_login)
    Button btn_login;

    String s_email, s_pswd, loginResponseData, loginResponseStatus, loginResponseMsg, loginResponseDataUser, loginResponseDataToken, loginUserId, loginUserEmail, loginUserName, loginUserSeedWord, loginUserPassword, loginUserDate, loginUserSeed, loginUserAdmin;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        sharedPreferences = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        toolbar_center_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        help_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, AccountRecoveryActivity.class);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonUtilities.isConnectionAvailable(LoginActivity.this)) {
                    CheckingInputs();
                } else {
                    CommonUtilities.ShowToastMessage(LoginActivity.this, getResources().getString(R.string.internetconnection));
                }
            }
        });

        txt_seed_recovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SeedRecoveryActivity.class);
                startActivity(intent);
            }
        });

    }

    private void CheckingInputs() {
        s_email = edt_email.getText().toString();
        s_pswd = edt_pswd.getText().toString();

        if (!s_email.isEmpty()) {
            if (s_email.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,3})$") && s_email.length() >= 8) {
                if (!s_pswd.isEmpty()) {
                    if (s_pswd.matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{8,25}$")) {
                        loggingInAccount(s_email, s_pswd);
                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    } else {
                        CommonUtilities.ShowToastMessage(LoginActivity.this, getResources().getString(R.string.invalid_pswd));
                    }
                } else {
                    CommonUtilities.ShowToastMessage(LoginActivity.this, getResources().getString(R.string.empty_pswd));
                }
            } else {
                CommonUtilities.ShowToastMessage(LoginActivity.this, getResources().getString(R.string.invalid_email));
            }
        } else {
            CommonUtilities.ShowToastMessage(LoginActivity.this, getResources().getString(R.string.empty_email));
        }


    }

    private void loggingInAccount(final String s_email, final String s_pswd) {
        try {
            JSONObject params = new JSONObject();
            try {
                params.put("password", s_pswd);
                params.put("email", s_email);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialog = ProgressDialog.show(LoginActivity.this, "", getResources().getString(R.string.please_wait), true);
            AuthenticationApi apiService = DeviantXApiClient.getClient().create(AuthenticationApi.class);
            Call<ResponseBody> apiResponse = apiService.loginAccount(params.toString());
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

                            if (loginResponseMsg.equals("Email is not yet verified")) {
                                editor.putString(CONSTANTS.email, s_email);
                                editor.putString(CONSTANTS.pswd, s_pswd);
                                editor.apply();

                                ShowTokenDialog();

                            } else {
                                if (loginResponseStatus.equals("true")) {
                                    loginResponseData = jsonObject.getString("data");

                                    JSONObject jsonObjectData = new JSONObject(loginResponseData);
                                    loginResponseDataToken = jsonObjectData.getString("token");

                                    loginResponseDataUser = jsonObjectData.getString("user");

                                    JSONObject jsonObjectDataUser = new JSONObject(loginResponseDataUser);

                                    String username = jsonObjectDataUser.getString("userName");

                                    String seed = jsonObjectDataUser.getString("seed");

                                    if (seed.equals("true")) {
                                        editor.putString(CONSTANTS.usrnm, username);
                                        editor.putString(CONSTANTS.email, s_email);
                                        editor.putString(CONSTANTS.pswd, s_pswd);
                                        editor.putString(CONSTANTS.token, loginResponseDataToken);
                                        editor.putBoolean(CONSTANTS.first_time, false);
                                        editor.putBoolean(CONSTANTS.seed, true);
                                        editor.commit();
//                                        GetWalletsList
                                        invokeWallet();

//                                        Intent intent = new Intent(LoginActivity.this, DashBoardActivity.class);
//                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                        startActivity(intent);
//                                        CommonUtilities.ShowToastMessage(LoginActivity.this, getResources().getString(R.string.login_success));
                                    } else {
                                        editor.putString(CONSTANTS.usrnm, username);
                                        editor.putString(CONSTANTS.email, s_email);
                                        editor.putString(CONSTANTS.pswd, s_pswd);
                                        editor.putString(CONSTANTS.token, loginResponseDataToken);
                                        editor.putBoolean(CONSTANTS.first_time, false);
                                        editor.commit();
                                        Intent intent = new Intent(LoginActivity.this, AddPhraseActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        CommonUtilities.ShowToastMessage(LoginActivity.this, getResources().getString(R.string.login_success));
                                    }
                                } else {
                                    CommonUtilities.ShowToastMessage(LoginActivity.this, loginResponseMsg);
                                }

                            }

                        } else {
                            CommonUtilities.ShowToastMessage(LoginActivity.this, loginResponseMsg);
//                            Toast.makeText(getApplicationContext(), responsevalue, Toast.LENGTH_LONG).show();
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(LoginActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(LoginActivity.this, getResources().getString(R.string.Timeout));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Timeout), Toast.LENGTH_SHORT).show();
                    } else if (t instanceof java.net.ConnectException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(LoginActivity.this, getResources().getString(R.string.networkerror));
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.networkerror), Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(LoginActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception ex) {
            progressDialog.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(LoginActivity.this, getResources().getString(R.string.errortxt));
//            Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
        }


    }

    private void invokeWallet() {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            progressDialog = ProgressDialog.show(LoginActivity.this, "", getResources().getString(R.string.please_wait), true);
            WalletControllerApi apiService = DeviantXApiClient.getClient().create(WalletControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.getAllWallet(CONSTANTS.DeviantMulti + token);
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
                                JSONArray jsonArrayData = new JSONArray(loginResponseData);
                                if (jsonArrayData.length() == 0) {
                                    editor.putBoolean(CONSTANTS.empty_wallet, true);
                                    editor.apply();
                                    Intent intent = new Intent(LoginActivity.this, SetUpWalletActivity.class);
                                    startActivity(intent);
                                    CommonUtilities.ShowToastMessage(LoginActivity.this, getResources().getString(R.string.login_success));
                                } else {
                                    editor.putBoolean(CONSTANTS.empty_wallet, false);
                                    editor.apply();
                                    if (myApplication.get2FA()){
                                        Intent intent = new Intent(LoginActivity.this, TwoFALoginActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        CommonUtilities.ShowToastMessage(LoginActivity.this, getResources().getString(R.string.login_success));
                                    }else {
                                        Intent intent = new Intent(LoginActivity.this, DashBoardActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        CommonUtilities.ShowToastMessage(LoginActivity.this, getResources().getString(R.string.login_success));
                                    }
                                }

                            } else {
                                CommonUtilities.ShowToastMessage(LoginActivity.this, loginResponseMsg);
                            }


                        } else {
                            CommonUtilities.ShowToastMessage(LoginActivity.this, loginResponseMsg);
//                            Toast.makeText(getApplicationContext(), responsevalue, Toast.LENGTH_LONG).show();
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(LoginActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(LoginActivity.this, getResources().getString(R.string.Timeout));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Timeout), Toast.LENGTH_SHORT).show();
                    } else if (t instanceof java.net.ConnectException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(LoginActivity.this, getResources().getString(R.string.networkerror));
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.networkerror), Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(LoginActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception ex) {
            progressDialog.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(LoginActivity.this, getResources().getString(R.string.errortxt));
//            Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
        }


    }

    private void ShowTokenDialog() {

        ViewHolder viewHolder = new ViewHolder(R.layout.dialog_regid_token);
        final DialogPlus dialog = DialogPlus.newDialog(LoginActivity.this)
                .setContentHolder(viewHolder)
                .setGravity(Gravity.BOTTOM)
                .setCancelable(true)
                .setInAnimation(R.anim.slide_in_bottom)
                .setOutAnimation(R.anim.slide_out_bottom)
                .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
//                        .setOnDismissListener(new OnDismissListener() {
//                            @Override
//                            public void onDismiss(DialogPlus dialog) {
//
//                            }
//                        })
//                        .setExpanded(true) // default is false, only works for grid and list
                .create();

//                Initializing Widgets
        View view = dialog.getHolderView();

        final EditText edt_token = view.findViewById(R.id.edt_token);
        Button btn_submit = view.findViewById(R.id.btn_submit);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonUtilities.isConnectionAvailable(LoginActivity.this)) {
                    String tkn = edt_token.getText().toString();
                    if (tkn.isEmpty()) {
                        CommonUtilities.ShowToastMessage(LoginActivity.this, getResources().getString(R.string.empty_field));
                    } else {
                        VerifyingEmail(tkn);
                        dialog.dismiss();
                    }
                } else {
                    CommonUtilities.ShowToastMessage(LoginActivity.this, getResources().getString(R.string.internetconnection));
                }
            }
        });

//                Displaying DialogPlus
        dialog.show();

    }

    private void VerifyingEmail(String tkn) {
        try {
            progressDialog = ProgressDialog.show(LoginActivity.this, "", getResources().getString(R.string.please_wait), true);
            UserControllerApi apiService = DeviantXApiClient.getClient().create(UserControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.verifyToken(tkn);
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

                                loggingInAccount(edt_email.getText().toString(), edt_pswd.getText().toString());
                                //                                Intent intent = new Intent(LoginActivity.this, DashBoardActivity.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//                                CommonUtilities.ShowToastMessage(LoginActivity.this, loginResponseMsg);
                            } else {
                                CommonUtilities.ShowToastMessage(LoginActivity.this, loginResponseMsg);
                            }


                        } else {
                            CommonUtilities.ShowToastMessage(LoginActivity.this, loginResponseMsg);
//                            Toast.makeText(getApplicationContext(), responsevalue, Toast.LENGTH_LONG).show();
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(LoginActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(LoginActivity.this, getResources().getString(R.string.Timeout));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Timeout), Toast.LENGTH_SHORT).show();
                    } else if (t instanceof java.net.ConnectException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(LoginActivity.this, getResources().getString(R.string.networkerror));
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.networkerror), Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(LoginActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception ex) {
            progressDialog.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(LoginActivity.this, getResources().getString(R.string.errortxt));
//            Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
        }

    }


}
