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

public class AccountRecoveryActivity extends AppCompatActivity {


    @BindView(R.id.toolbar_center_back)
    Toolbar toolbar_center_back;
    @BindView(R.id.edt_email)
    EditText edt_email;
    @BindView(R.id.btn_send)
    Button btn_send;

    String email, loginResponseMsg, loginResponseStatus;

    ProgressDialog progressDialog;
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
        setContentView(R.layout.activity_account_recovery);

        ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();


        toolbar_center_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = edt_email.getText().toString().trim();
                if (!email.isEmpty()) {
                    if (email.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,3})$") && email.length() >= 8) {
                        if (CommonUtilities.isConnectionAvailable(AccountRecoveryActivity.this)) {
                            emailRecovery(email);
                        } else {
                            CommonUtilities.ShowToastMessage(AccountRecoveryActivity.this, getResources().getString(R.string.internetconnection));
                        }
                    } else {
                        CommonUtilities.ShowToastMessage(AccountRecoveryActivity.this, getResources().getString(R.string.invalid_email));
                    }
                } else {
                    CommonUtilities.ShowToastMessage(AccountRecoveryActivity.this, getResources().getString(R.string.empty_email));
                }
            }
        });
    }

    private void emailRecovery(final String email) {
        try {
            progressDialog = ProgressDialog.show(AccountRecoveryActivity.this, "", getResources().getString(R.string.please_wait), true);
            UserControllerApi apiService = DeviantXApiClient.getClient().create(UserControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.resetEmail(email);
            apiResponse.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responsevalue = response.body().string();

                        if (!responsevalue.isEmpty() && responsevalue != null) {
                            progressDialog.dismiss();
                            JSONObject jsonObject = new JSONObject(responsevalue);
                            loginResponseMsg = jsonObject.getString("msg");
                            boolean loginResponseStatus = jsonObject.getBoolean("status");
                            if (loginResponseStatus) {

//                                showPasswordDialog();

                                Intent intent = new Intent(AccountRecoveryActivity.this, RecoveryConfirmActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString(CONSTANTS.email,email);
                                editor.putString(CONSTANTS.email,email);
                                editor.apply();
                                intent.putExtras(bundle);

//                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                CommonUtilities.ShowToastMessage(AccountRecoveryActivity.this, getResources().getString(R.string.check_email));
                            } else {
                                CommonUtilities.ShowToastMessage(AccountRecoveryActivity.this, loginResponseMsg);
                            }

                        } else {
                            CommonUtilities.ShowToastMessage(AccountRecoveryActivity.this, loginResponseMsg);
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(AccountRecoveryActivity.this, getResources().getString(R.string.errortxt));
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(AccountRecoveryActivity.this, getResources().getString(R.string.Timeout));
                    } else if (t instanceof java.net.ConnectException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(AccountRecoveryActivity.this, getResources().getString(R.string.networkerror));
                    } else {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(AccountRecoveryActivity.this, getResources().getString(R.string.errortxt));
                    }
                }
            });
        } catch (Exception ex) {
            progressDialog.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(AccountRecoveryActivity.this, getResources().getString(R.string.errortxt));
        }

    }

//    private void showPasswordDialog() {
//        ViewHolder viewHolder = new ViewHolder(R.layout.dialog_frgt_pswd);
//        final DialogPlus dialog = DialogPlus.newDialog(AccountRecoveryActivity.this)
//                .setContentHolder(viewHolder)
//                .setGravity(Gravity.BOTTOM)
//                .setCancelable(true)
//                .setInAnimation(R.anim.slide_in_bottom)
//                .setOutAnimation(R.anim.slide_out_bottom)
//                .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)
//                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
//                .create();
//
////                Initializing Widgets
//        View view = dialog.getHolderView();
//
//        final EditText edt_token = view.findViewById(R.id.edt_token);
//        final EditText edt_new_pswd = view.findViewById(R.id.edt_new_pswd);
//        final EditText edt_confirm_pswd = view.findViewById(R.id.edt_confirm_pswd);
//
//        Button btn_change_pswd  = view.findViewById(R.id.btn_change_pswd);
//
//
//        btn_change_pswd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (CommonUtilities.isConnectionAvailable(AccountRecoveryActivity.this)) {
//                    String tkn = edt_token.getText().toString();
//                    String new_pswd = edt_new_pswd.getText().toString();
//                    String conf_pswd = edt_confirm_pswd.getText().toString();
//                    CheckingInputs(tkn, new_pswd, conf_pswd);
//                } else {
//                    CommonUtilities.ShowToastMessage(AccountRecoveryActivity.this, getResources().getString(R.string.internetconnection));
//                    dialog.dismiss();
//                }
//            }
//        });
//
////                Displaying DialogPlus
//        dialog.show();
//
//    }

//    private void CheckingInputs(String tkn, String new_pswd, String conf_pswd) {
//        if (!tkn.isEmpty()) {
//            if (!new_pswd.isEmpty()) {
//                if (new_pswd.matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{8,25}$")) {
//                    if (!conf_pswd.isEmpty()) {
//                        if (new_pswd.equals(conf_pswd)) {
//                            invokeEmailRecovery(tkn, new_pswd);
//                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
//                            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//                        } else {
//                            CommonUtilities.ShowToastMessage(AccountRecoveryActivity.this, getResources().getString(R.string.unmatch_pswd));
//                        }
//                    } else {
//                        CommonUtilities.ShowToastMessage(AccountRecoveryActivity.this, getResources().getString(R.string.empty_pswd));
//                    }
//                } else {
//                    CommonUtilities.ShowToastMessage(AccountRecoveryActivity.this, getResources().getString(R.string.invalid_pswd));
//                }
//            } else {
//                CommonUtilities.ShowToastMessage(AccountRecoveryActivity.this, getResources().getString(R.string.empty_pswd));
//            }
//        } else {
//            CommonUtilities.ShowToastMessage(AccountRecoveryActivity.this, getResources().getString(R.string.empty_token));
//        }
//
//    }

//    private void invokeEmailRecovery(String tkn, final String new_pswd) {
//
//        try {
//            progressDialog = ProgressDialog.show(AccountRecoveryActivity.this, "", getResources().getString(R.string.please_wait), true);
//            UserControllerApi apiService = DeviantXApiClient.getClient().create(UserControllerApi.class);
//            Call<ResponseBody> apiResponse = apiService.recoverEmailPassword(new_pswd, tkn);
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
//                            if (loginResponseStatus.equals("true")) {
//                                editor.putString(CONSTANTS.pswd, new_pswd);
//                                editor.apply();
//                                Intent intent = new Intent(AccountRecoveryActivity.this, DashBoardActivity.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
////                                CommonUtilities.ShowToastMessage(AccountRecoveryActivity.this, loginResponseMsg);
//                            } else {
//                                CommonUtilities.ShowToastMessage(AccountRecoveryActivity.this, loginResponseMsg);
//                            }
//
//
//                        } else {
//                            CommonUtilities.ShowToastMessage(AccountRecoveryActivity.this, loginResponseMsg);
////                            Toast.makeText(getApplicationContext(), responsevalue, Toast.LENGTH_LONG).show();
//                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
//                        }
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        progressDialog.dismiss();
//                        CommonUtilities.ShowToastMessage(AccountRecoveryActivity.this, getResources().getString(R.string.errortxt));
////                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    if (t instanceof SocketTimeoutException) {
//                        progressDialog.dismiss();
//                        CommonUtilities.ShowToastMessage(AccountRecoveryActivity.this, getResources().getString(R.string.Timeout));
////                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Timeout), Toast.LENGTH_SHORT).show();
//                    } else if (t instanceof java.net.ConnectException) {
//                        progressDialog.dismiss();
//                        CommonUtilities.ShowToastMessage(AccountRecoveryActivity.this, getResources().getString(R.string.networkerror));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.networkerror), Toast.LENGTH_SHORT).show();
//                    } else {
//                        progressDialog.dismiss();
//                        CommonUtilities.ShowToastMessage(AccountRecoveryActivity.this, getResources().getString(R.string.errortxt));
////                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//        } catch (Exception ex) {
//            progressDialog.dismiss();
//            ex.printStackTrace();
//            CommonUtilities.ShowToastMessage(AccountRecoveryActivity.this, getResources().getString(R.string.errortxt));
////            Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
//        }
//
//
//    }


}


