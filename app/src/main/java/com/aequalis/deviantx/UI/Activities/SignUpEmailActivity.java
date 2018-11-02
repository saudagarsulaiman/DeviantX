package com.aequalis.deviantx.UI.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aequalis.deviantx.R;
import com.aequalis.deviantx.ServiceAPIs.AuthenticationApi;
import com.aequalis.deviantx.ServiceAPIs.UserControllerApi;
import com.aequalis.deviantx.Utilities.CONSTANTS;
import com.aequalis.deviantx.Utilities.CommonUtilities;
import com.aequalis.deviantx.Utilities.DeviantXApiClient;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketTimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpEmailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_center_back)
    Toolbar toolbar_center_back;
    @BindView(R.id.btn_continue)
    Button btn_continue;
    @BindView(R.id.edt_usrnm)
    EditText edt_usrnm;
    @BindView(R.id.edt_email)
    EditText edt_email;
    @BindView(R.id.edt_confirm_email)
    EditText edt_confirm_email;
    @BindView(R.id.edt_pswd)
    EditText edt_pswd;
    @BindView(R.id.edt_confirm_pswd)
    EditText edt_confirm_pswd;
    @BindView(R.id.txt_lower_case)
    TextView txt_lower_case;
    @BindView(R.id.txt_upper_case)
    TextView txt_upper_case;
    @BindView(R.id.txt_number)
    TextView txt_number;
    @BindView(R.id.txt_chars)
    TextView txt_chars;
    @BindView(R.id.txt_privacy_policy)
    TextView txt_privacy_policy;

    String s_usrnm, s_email, s_conf_email, s_pswd, s_conf_pswd, regResponsedata, regResponseStatus, regResponseMsg, loginResponseData, loginResponseStatus, loginResponseMsg, loginResponseDataUser, loginResponseDataToken, loginUserId, loginUserEmail, loginUserName, loginUserSeedWord, loginUserPassword, loginUserDate, loginUserSeed, loginUserAdmin;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_email);

        ButterKnife.bind(this);

        sharedPreferences = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        toolbar_center_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonUtilities.isConnectionAvailable(SignUpEmailActivity.this)) {
                    CheckingInputs();
                } else {
                    CommonUtilities.ShowToastMessage(SignUpEmailActivity.this, getResources().getString(R.string.internetconnection));
                }
            }
        });

        edt_pswd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();

                matchingPasswordText(text);

            }
        });


    }

    private void matchingPasswordText(String text) {

        if (text.matches("[a-z]+")) {
            txt_lower_case.setBackground(getResources().getDrawable(R.drawable.rec_green_c2));
            return;
        } /*else {
            txt_lower_case.setBackground(getResources().getDrawable(R.drawable.rec_marred_c2));
        }*/

        if (text.matches("[A-Z]+")) {
            txt_upper_case.setBackground(getResources().getDrawable(R.drawable.rec_green_c2));
            return;
        } /*else {
            txt_upper_case.setBackground(getResources().getDrawable(R.drawable.rec_marred_c2));
        }*/

        if (text.matches("\\d+")) {
            txt_number.setBackground(getResources().getDrawable(R.drawable.rec_green_c2));
            return;
        } /*else {
            txt_number.setBackground(getResources().getDrawable(R.drawable.rec_marred_c2));
        }*/


//        if (text.matches("[a-zA-Z]+")) {
//            txt_upper_case.setBackground(getResources().getDrawable(R.drawable.rec_green_c2));
//            txt_lower_case.setBackground(getResources().getDrawable(R.drawable.rec_green_c2));
//        } else {
//            txt_lower_case.setBackground(getResources().getDrawable(R.drawable.rec_marred_c2));
//            txt_upper_case.setBackground(getResources().getDrawable(R.drawable.rec_marred_c2));
//        }
//        if (text.matches("[a-z0-9]+")) {
//            txt_number.setBackground(getResources().getDrawable(R.drawable.rec_green_c2));
//            txt_lower_case.setBackground(getResources().getDrawable(R.drawable.rec_green_c2));
//        } else {
//            txt_lower_case.setBackground(getResources().getDrawable(R.drawable.rec_marred_c2));
//            txt_number.setBackground(getResources().getDrawable(R.drawable.rec_marred_c2));
//        }
//        if (text.matches("[0-9A-Z]+")) {
//            txt_upper_case.setBackground(getResources().getDrawable(R.drawable.rec_green_c2));
//            txt_number.setBackground(getResources().getDrawable(R.drawable.rec_green_c2));
//        } else {
//            txt_number.setBackground(getResources().getDrawable(R.drawable.rec_marred_c2));
//            txt_upper_case.setBackground(getResources().getDrawable(R.drawable.rec_marred_c2));
//        }
//
//
//        if (text.matches("[0-9a-zA-Z]+")) {
//            txt_number.setBackground(getResources().getDrawable(R.drawable.rec_green_c2));
//            txt_upper_case.setBackground(getResources().getDrawable(R.drawable.rec_green_c2));
//            txt_lower_case.setBackground(getResources().getDrawable(R.drawable.rec_green_c2));
//        } else {
//            txt_lower_case.setBackground(getResources().getDrawable(R.drawable.rec_marred_c2));
//            txt_number.setBackground(getResources().getDrawable(R.drawable.rec_marred_c2));
//            txt_upper_case.setBackground(getResources().getDrawable(R.drawable.rec_marred_c2));
//        }


        if (text.matches("[a-z&&[A-Z]]")) {
            txt_upper_case.setBackground(getResources().getDrawable(R.drawable.rec_green_c2));
            txt_lower_case.setBackground(getResources().getDrawable(R.drawable.rec_green_c2));
            return;
        } /*else {
            txt_lower_case.setBackground(getResources().getDrawable(R.drawable.rec_marred_c2));
            txt_upper_case.setBackground(getResources().getDrawable(R.drawable.rec_marred_c2));
        }*/
        if (text.matches("[a-z&&[\\d+]]")) {
            txt_number.setBackground(getResources().getDrawable(R.drawable.rec_green_c2));
            txt_lower_case.setBackground(getResources().getDrawable(R.drawable.rec_green_c2));
            return;
        } /*else {
            txt_lower_case.setBackground(getResources().getDrawable(R.drawable.rec_marred_c2));
            txt_number.setBackground(getResources().getDrawable(R.drawable.rec_marred_c2));
        }*/
        if (text.matches("[\\d&&[A-Z]]")) {
            txt_upper_case.setBackground(getResources().getDrawable(R.drawable.rec_green_c2));
            txt_number.setBackground(getResources().getDrawable(R.drawable.rec_green_c2));
            return;
        } /*else {
            txt_number.setBackground(getResources().getDrawable(R.drawable.rec_marred_c2));
            txt_upper_case.setBackground(getResources().getDrawable(R.drawable.rec_marred_c2));
        }*/


        if (text.matches("[\\d&&[a-z&&[A-Z]]]")) {
            txt_number.setBackground(getResources().getDrawable(R.drawable.rec_green_c2));
            txt_upper_case.setBackground(getResources().getDrawable(R.drawable.rec_green_c2));
            txt_lower_case.setBackground(getResources().getDrawable(R.drawable.rec_green_c2));
            return;
        } else {
            txt_lower_case.setBackground(getResources().getDrawable(R.drawable.rec_marred_c2));
            txt_number.setBackground(getResources().getDrawable(R.drawable.rec_marred_c2));
            txt_upper_case.setBackground(getResources().getDrawable(R.drawable.rec_marred_c2));
        }


//        if (text.matches("[a-z]+") && text.matches("[A-Z]+")) {
//            txt_upper_case.setBackground(getResources().getDrawable(R.drawable.rec_green_c2));
//            txt_lower_case.setBackground(getResources().getDrawable(R.drawable.rec_green_c2));
//        } else {
//            txt_lower_case.setBackground(getResources().getDrawable(R.drawable.rec_marred_c2));
//            txt_upper_case.setBackground(getResources().getDrawable(R.drawable.rec_marred_c2));
//        }
//        if (text.matches("[a-z]+") && text.matches("\\d+")) {
//            txt_number.setBackground(getResources().getDrawable(R.drawable.rec_green_c2));
//            txt_lower_case.setBackground(getResources().getDrawable(R.drawable.rec_green_c2));
//        } else {
//            txt_lower_case.setBackground(getResources().getDrawable(R.drawable.rec_marred_c2));
//            txt_number.setBackground(getResources().getDrawable(R.drawable.rec_marred_c2));
//        }
//        if (text.matches("\\d+") && text.matches("[A-Z]+")) {
//            txt_upper_case.setBackground(getResources().getDrawable(R.drawable.rec_green_c2));
//            txt_number.setBackground(getResources().getDrawable(R.drawable.rec_green_c2));
//        } else {
//            txt_number.setBackground(getResources().getDrawable(R.drawable.rec_marred_c2));
//            txt_upper_case.setBackground(getResources().getDrawable(R.drawable.rec_marred_c2));
//        }
//
//
//        if (text.matches("\\d+") && text.matches("[a-z]+") && text.matches("[A-Z]+")) {
//            txt_number.setBackground(getResources().getDrawable(R.drawable.rec_green_c2));
//            txt_upper_case.setBackground(getResources().getDrawable(R.drawable.rec_green_c2));
//            txt_lower_case.setBackground(getResources().getDrawable(R.drawable.rec_green_c2));
//        } else {
//            txt_lower_case.setBackground(getResources().getDrawable(R.drawable.rec_marred_c2));
//            txt_number.setBackground(getResources().getDrawable(R.drawable.rec_marred_c2));
//            txt_upper_case.setBackground(getResources().getDrawable(R.drawable.rec_marred_c2));
//        }


//        if (text.contains(getResources().getString(R.string.lower_letters)) && text.contains(getResources().getString(R.string.upper_letters))) {
//            txt_upper_case.setBackground(getResources().getDrawable(R.drawable.rec_green_c2));
//            txt_lower_case.setBackground(getResources().getDrawable(R.drawable.rec_green_c2));
//        } else {
//            txt_lower_case.setBackground(getResources().getDrawable(R.drawable.rec_marred_c2));
//            txt_upper_case.setBackground(getResources().getDrawable(R.drawable.rec_marred_c2));
//        }
//        if (text.contains(getResources().getString(R.string.lower_letters)) && text.contains(getResources().getString(R.string.digits))) {
//            txt_number.setBackground(getResources().getDrawable(R.drawable.rec_green_c2));
//            txt_lower_case.setBackground(getResources().getDrawable(R.drawable.rec_green_c2));
//        } else {
//            txt_lower_case.setBackground(getResources().getDrawable(R.drawable.rec_marred_c2));
//            txt_number.setBackground(getResources().getDrawable(R.drawable.rec_marred_c2));
//        }
//        if (text.contains(getResources().getString(R.string.upper_letters)) && text.contains(getResources().getString(R.string.number))) {
//            txt_upper_case.setBackground(getResources().getDrawable(R.drawable.rec_green_c2));
//            txt_number.setBackground(getResources().getDrawable(R.drawable.rec_green_c2));
//        } else {
//            txt_number.setBackground(getResources().getDrawable(R.drawable.rec_marred_c2));
//            txt_upper_case.setBackground(getResources().getDrawable(R.drawable.rec_marred_c2));
//        }
//
//
//        if (text.contains(getResources().getString(R.string.lower_letters)) && text.contains(getResources().getString(R.string.upper_letters))&&text.contains(getResources().getString(R.string.digits))) {
//            txt_number.setBackground(getResources().getDrawable(R.drawable.rec_green_c2));
//            txt_upper_case.setBackground(getResources().getDrawable(R.drawable.rec_green_c2));
//            txt_lower_case.setBackground(getResources().getDrawable(R.drawable.rec_green_c2));
//        } else {
//            txt_lower_case.setBackground(getResources().getDrawable(R.drawable.rec_marred_c2));
//            txt_number.setBackground(getResources().getDrawable(R.drawable.rec_marred_c2));
//            txt_upper_case.setBackground(getResources().getDrawable(R.drawable.rec_marred_c2));
//        }


        if (text.length() > 7 && text.length() < 26) {
            txt_chars.setBackground(getResources().getDrawable(R.drawable.rec_green_c2));
        } else {
            txt_chars.setBackground(getResources().getDrawable(R.drawable.rec_marred_c2));
        }

    }

    private void CheckingInputs() {
        s_usrnm = edt_usrnm.getText().toString();
        s_email = edt_email.getText().toString();
        s_conf_email = edt_confirm_email.getText().toString();
        s_pswd = edt_pswd.getText().toString();
        s_conf_pswd = edt_confirm_pswd.getText().toString();

        if (!s_usrnm.isEmpty()) {
            if (!s_email.isEmpty()) {
                if (s_email.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,3})$") && s_email.length() >= 8) {
                    if (!s_conf_email.isEmpty()) {
                        if (s_conf_email.equals(s_email)) {
                            if (!s_pswd.isEmpty()) {
                                if (s_pswd.matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{8,25}$")) {
                                    if (!s_conf_pswd.isEmpty()) {
                                        if (s_pswd.equals(s_conf_pswd)) {

                                            CustomDialog(s_usrnm, s_email, s_pswd);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                                        } else {
                                            CommonUtilities.ShowToastMessage(SignUpEmailActivity.this, getResources().getString(R.string.unmatch_pswd));
                                        }
                                    } else {
                                        CommonUtilities.ShowToastMessage(SignUpEmailActivity.this, getResources().getString(R.string.empty_pswd));
                                    }
                                } else {
                                    CommonUtilities.ShowToastMessage(SignUpEmailActivity.this, getResources().getString(R.string.invalid_pswd));
                                }
                            } else {
                                CommonUtilities.ShowToastMessage(SignUpEmailActivity.this, getResources().getString(R.string.empty_pswd));
                            }
                        } else {
                            CommonUtilities.ShowToastMessage(SignUpEmailActivity.this, getResources().getString(R.string.unmatch_email));
                        }
                    } else {
                        CommonUtilities.ShowToastMessage(SignUpEmailActivity.this, getResources().getString(R.string.empty_email));
                    }
                } else {
                    CommonUtilities.ShowToastMessage(SignUpEmailActivity.this, getResources().getString(R.string.invalid_email));
                }
            } else {
                CommonUtilities.ShowToastMessage(SignUpEmailActivity.this, getResources().getString(R.string.empty_email));
            }
        } else {
            CommonUtilities.ShowToastMessage(SignUpEmailActivity.this, getResources().getString(R.string.empty_usrnm));
        }

    }

    private void CustomDialog(final String s_usrnm, final String s_email, final String s_pswd) {
        //                Creating A Custom Dialog Using DialogPlus
        ViewHolder viewHolder = new ViewHolder(R.layout.dialog_terms_conditions);
        final DialogPlus dialog = DialogPlus.newDialog(SignUpEmailActivity.this)
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
        TextView txt_reject = view.findViewById(R.id.txt_reject);
        TextView txt_agree = view.findViewById(R.id.txt_agree);

        txt_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        txt_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisteringAccount(s_email, s_pswd, s_usrnm);
                dialog.dismiss();
            }
        });
//                Displaying DialogPlus
        dialog.show();

    }

    private void RegisteringAccount(final String s_email, final String s_pswd, final String s_usrnm) {
        try {
            JSONObject params = new JSONObject();
            try {
                params.put("password", s_pswd);
                params.put("userName", s_usrnm);
                params.put("email", s_email);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialog = ProgressDialog.show(SignUpEmailActivity.this, "Signing Up", getResources().getString(R.string.please_wait), true);
            UserControllerApi apiService = DeviantXApiClient.getClient().create(UserControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.createAccount(params.toString());
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

                                editor.putString(CONSTANTS.usrnm, s_usrnm);
                                editor.putString(CONSTANTS.email, s_email);
                                editor.putString(CONSTANTS.pswd, s_pswd);
                                editor.commit();

                                Intent intent = new Intent(SignUpEmailActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

//                                loggingInAccount(s_email,s_pswd);

//                                CommonUtilities.ShowToastMessage(SignUpEmailActivity.this, loginResponseMsg);
                                Log.i(CONSTANTS.TAG, "onResponse:\n" + loginResponseMsg);

                            } else {
                                CommonUtilities.ShowToastMessage(SignUpEmailActivity.this, regResponseMsg);
                            }

                        } else {
                            CommonUtilities.ShowToastMessage(SignUpEmailActivity.this, regResponseMsg);
//                            Toast.makeText(getApplicationContext(), responsevalue, Toast.LENGTH_LONG).show();
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(SignUpEmailActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(SignUpEmailActivity.this, getResources().getString(R.string.Timeout));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Timeout), Toast.LENGTH_SHORT).show();
                    } else if (t instanceof java.net.ConnectException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(SignUpEmailActivity.this, getResources().getString(R.string.networkerror));
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.networkerror), Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(SignUpEmailActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception ex) {
            progressDialog.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(SignUpEmailActivity.this, getResources().getString(R.string.errortxt));
//            Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
        }
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
//            progressDialog = ProgressDialog.show(SignUpEmailActivity.this, "Logging In", getResources().getString(R.string.please_wait), true);
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
//                            if (loginResponseStatus.equals("true")) {
//                                loginResponseData = jsonObject.getString("data");
//
//                                JSONObject jsonObjectData = new JSONObject(loginResponseData);
//                                loginResponseDataUser = jsonObjectData.getString("user");
//                                loginResponseDataToken = jsonObjectData.getString("token");
//
//                                editor.putString(CONSTANTS.email, s_email);
//                                editor.putString(CONSTANTS.pswd, s_pswd);
//                                editor.putString(CONSTANTS.token, loginResponseDataToken);
//                                editor.commit();
//
//                                Intent intent = new Intent(SignUpEmailActivity.this, LoginActivity.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
////                                CommonUtilities.ShowToastMessage(SignUpEmailActivity.this, getResources().getString(R.string.login_success));
//
//                            } else {
//                                CommonUtilities.ShowToastMessage(SignUpEmailActivity.this, loginResponseMsg);
//                            }
//
//                        } else {
//                            CommonUtilities.ShowToastMessage(SignUpEmailActivity.this, loginResponseMsg);
////                            Toast.makeText(getApplicationContext(), responsevalue, Toast.LENGTH_LONG).show();
//                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
//                        }
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        progressDialog.dismiss();
//                        CommonUtilities.ShowToastMessage(SignUpEmailActivity.this, getResources().getString(R.string.errortxt));
////                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    if (t instanceof SocketTimeoutException) {
//                        progressDialog.dismiss();
//                        CommonUtilities.ShowToastMessage(SignUpEmailActivity.this, getResources().getString(R.string.Timeout));
////                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Timeout), Toast.LENGTH_SHORT).show();
//                    } else if (t instanceof java.net.ConnectException) {
//                        progressDialog.dismiss();
//                        CommonUtilities.ShowToastMessage(SignUpEmailActivity.this, getResources().getString(R.string.networkerror));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.networkerror), Toast.LENGTH_SHORT).show();
//                    } else {
//                        progressDialog.dismiss();
//                        CommonUtilities.ShowToastMessage(SignUpEmailActivity.this, getResources().getString(R.string.errortxt));
////                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//        } catch (Exception ex) {
//            progressDialog.dismiss();
//            ex.printStackTrace();
//            CommonUtilities.ShowToastMessage(SignUpEmailActivity.this, getResources().getString(R.string.errortxt));
////            Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
//        }
//
//
//    }


}
