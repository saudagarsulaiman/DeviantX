package com.cryptowallet.deviantx.UI.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.ServiceAPIs.UserControllerApi;
import com.cryptowallet.deviantx.UI.Services.WalletDataFetch;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;
import com.cryptowallet.deviantx.Utilities.DeviantXApiClient;
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

import static com.cryptowallet.deviantx.Utilities.MyApplication.myApplication;

public class AppSettingsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_center_back)
    Toolbar toolbar_center_back;
    @BindView(R.id.lnr_language)
    LinearLayout lnr_language;
    @BindView(R.id.lnr_screen_lock)
    LinearLayout lnr_screen_lock;
    @BindView(R.id.lnr_rec_phrase)
    LinearLayout lnr_rec_phrase;
    @BindView(R.id.lnr_change_pswd)
    LinearLayout lnr_change_pswd;
    @BindView(R.id.lnr_restore_wallet)
    LinearLayout lnr_restore_wallet;
    @BindView(R.id.scompat_light_mode)
    SwitchCompat scompat_light_mode;
    @BindView(R.id.scompat_privacy)
    SwitchCompat scompat_privacy;
    @BindView(R.id.scompat_hide_bal)
    SwitchCompat scompat_hide_bal;
    @BindView(R.id.lnr_2fa)
    LinearLayout lnr_2fa;
    @BindView(R.id.scompat_2fa)
    SwitchCompat scompat_2fa;
    @BindView(R.id.txt_2FA_status)
    TextView txt_2FA_status;


    String loginResponseMsg, loginResponseStatus, tkn, loginResponseData;

    ProgressDialog progressDialog;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_settings);

        ButterKnife.bind(this);

        sharedPreferences = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        tkn = sharedPreferences.getString(CONSTANTS.token, "");

        if (myApplication.getHideBalance()) {
            scompat_hide_bal.setChecked(true);
            scompat_hide_bal.setBackground(getResources().getDrawable(R.drawable.rec_white_white_c16));
            scompat_hide_bal.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
        } else {
            scompat_hide_bal.setBackground(getResources().getDrawable(R.drawable.rec_white_trans_c16));
            scompat_hide_bal.setChecked(false);
            scompat_hide_bal.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.transparent)));
        }


        if (myApplication.getScreenShot()) {
            scompat_privacy.setChecked(true);
            scompat_privacy.setBackground(getResources().getDrawable(R.drawable.rec_white_white_c16));
            scompat_privacy.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
        } else {
            scompat_privacy.setBackground(getResources().getDrawable(R.drawable.rec_white_trans_c16));
            scompat_privacy.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.transparent)));
            scompat_privacy.setChecked(false);
        }

        if (CommonUtilities.isConnectionAvailable(AppSettingsActivity.this)) {
            get2FAstatus();
        } else {
            CommonUtilities.ShowToastMessage(AppSettingsActivity.this, getResources().getString(R.string.internetconnection));
        }

        if (myApplication.get2FA()) {
            scompat_2fa.setChecked(true);
            scompat_2fa.setBackground(getResources().getDrawable(R.drawable.rec_white_white_c16));
            scompat_2fa.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            txt_2FA_status.setText(getResources().getString(R.string.active));
        } else {
            scompat_2fa.setBackground(getResources().getDrawable(R.drawable.rec_white_trans_c16));
            scompat_2fa.setChecked(false);
            scompat_2fa.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.transparent)));
            txt_2FA_status.setText(getResources().getString(R.string.inactive));
        }

        toolbar_center_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        lnr_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LanguageDialog();
            }
        });

        lnr_change_pswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PasswordDialog(tkn);
            }
        });

        scompat_hide_bal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    scompat_hide_bal.setBackground(getResources().getDrawable(R.drawable.rec_white_white_c16));
                    scompat_hide_bal.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                    editor.putBoolean(CONSTANTS.hideBal, true);
                    editor.apply();
                    myApplication.setHideBalance(true);
                    CommonUtilities.ShowToastMessage(AppSettingsActivity.this, getResources().getString(R.string.hide_bal_active));
                } else {
                    scompat_hide_bal.setBackground(getResources().getDrawable(R.drawable.rec_white_trans_c16));
                    scompat_hide_bal.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.transparent)));
                    editor.putBoolean(CONSTANTS.hideBal, false);
                    editor.apply();
                    myApplication.setHideBalance(false);
                    CommonUtilities.ShowToastMessage(AppSettingsActivity.this, getResources().getString(R.string.hide_bal_inactive));
                }
            }
        });

        scompat_privacy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    scompat_privacy.setBackground(getResources().getDrawable(R.drawable.rec_white_white_c16));
                    scompat_privacy.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                    editor.putBoolean(CONSTANTS.screenshot, true);
                    editor.apply();
                    myApplication.setScreenShot(true);
                    onResume();
//                    CommonUtilities.ShowToastMessage(AppSettingsActivity.this,getResources().getString(R.string.screenshots_active));
                } else {
                    scompat_privacy.setBackground(getResources().getDrawable(R.drawable.rec_white_trans_c16));
                    scompat_privacy.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.transparent)));
                    editor.putBoolean(CONSTANTS.screenshot, false);
                    editor.apply();
                    myApplication.setScreenShot(false);
                    onResume();
//                    CommonUtilities.ShowToastMessage(AppSettingsActivity.this,getResources().getString(R.string.screenshots_inactive));
                }
            }
        });

        lnr_2fa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AppSettingsActivity.this, TwoFAEnable1Activity.class);
                startActivity(intent);
            }
        });

        scompat_2fa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Intent intent = new Intent(AppSettingsActivity.this, TwoFAAbleActivity.class);
                startActivity(intent);
                if (isChecked) {
//                    scompat_2fa.setBackground(getResources().getDrawable(R.drawable.rec_white_white_c16));
//                    scompat_2fa.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
//                    editor.putBoolean(CONSTANTS.twoFactorAuth, true);
//                    editor.apply();
//                    myApplication.set2FA(true);
                    scompat_2fa.setChecked(false);
//                    txt_2FA_status.setText(getResources().getString(R.string.active));
//                    CommonUtilities.ShowToastMessage(AppSettingsActivity.this, getResources().getString(R.string.twoFA_active));
                } else {
//                    scompat_2fa.setBackground(getResources().getDrawable(R.drawable.rec_white_trans_c16));
//                    scompat_2fa.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.transparent)));
//                    editor.putBoolean(CONSTANTS.twoFactorAuth, false);
//                    editor.apply();
//                    myApplication.set2FA(false);
                    scompat_2fa.setChecked(true);
//                    txt_2FA_status.setText(getResources().getString(R.string.inactive));
//                    CommonUtilities.ShowToastMessage(AppSettingsActivity.this, getResources().getString(R.string.twoFA_inactive));
                }
            }
        });

        scompat_light_mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.putBoolean(CONSTANTS.lightmode, true);
                    editor.apply();
                    scompat_light_mode.setBackground(getResources().getDrawable(R.drawable.rec_white_white_c16));
                    scompat_light_mode.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
//                    CommonUtilities.ShowToastMessage(AppSettingsActivity.this,getResources().getString(R.string.nightmode_active));
                } else {
                    editor.putBoolean(CONSTANTS.lightmode, false);
                    editor.apply();
                    scompat_light_mode.setBackground(getResources().getDrawable(R.drawable.rec_white_trans_c16));
                    scompat_light_mode.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.transparent)));
//                    CommonUtilities.ShowToastMessage(AppSettingsActivity.this,getResources().getString(R.string.nightmode_inactive));
                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        myApplication.disableScreenCapture(this);
        myApplication.get2FA();
    }

    private void LanguageDialog() {
        ViewHolder viewHolder = new ViewHolder(R.layout.dialog_languages);
        final DialogPlus dialog = DialogPlus.newDialog(AppSettingsActivity.this)
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
        LinearLayout lnr_sys_lang = view.findViewById(R.id.lnr_sys_lang);
        LinearLayout lnr_english = view.findViewById(R.id.lnr_english);
        LinearLayout lnr_arabic = view.findViewById(R.id.lnr_arabic);
        LinearLayout lnr_chinese_simp = view.findViewById(R.id.lnr_chinese_simp);
        LinearLayout lnr_chinese_trad = view.findViewById(R.id.lnr_chinese_trad);
        LinearLayout lnr_czesh = view.findViewById(R.id.lnr_czesh);
        LinearLayout lnr_dutch = view.findViewById(R.id.lnr_dutch);

        lnr_sys_lang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        lnr_english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        lnr_arabic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        lnr_chinese_simp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        lnr_chinese_trad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        lnr_czesh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        lnr_dutch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


//                Displaying DialogPlus
        dialog.show();

    }


    private void PasswordDialog(final String tkn) {
        ViewHolder viewHolder = new ViewHolder(R.layout.dialog_change_password);
        final DialogPlus dialog = DialogPlus.newDialog(AppSettingsActivity.this)
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

        final EditText edt_old_pswd = view.findViewById(R.id.edt_old_pswd);
        final EditText edt_new_pswd = view.findViewById(R.id.edt_new_pswd);
        final TextView txt_lower_case = view.findViewById(R.id.txt_lower_case);
        final TextView txt_upper_case = view.findViewById(R.id.txt_upper_case);
        final TextView txt_number = view.findViewById(R.id.txt_number);
        final TextView txt_chars = view.findViewById(R.id.txt_chars);
        final EditText edt_confirm_pswd = view.findViewById(R.id.edt_confirm_pswd);
        final ImageView img_center_back = view.findViewById(R.id.img_center_back);

        Button btn_change_pswd = view.findViewById(R.id.btn_change_pswd);


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
//                CommonUtilities.matchingPasswordText(AppSettingsActivity.this, text, txt_lower_case, txt_upper_case, txt_number, txt_chars);
                matchingPasswordText(text, txt_lower_case, txt_upper_case, txt_number, txt_chars);
            }
        });

        btn_change_pswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String old_pswd = edt_old_pswd.getText().toString();
                String new_pswd = edt_new_pswd.getText().toString();
                String conf_pswd = edt_confirm_pswd.getText().toString();
                CheckingInputs(tkn, old_pswd, new_pswd, conf_pswd);
//                dialog.dismiss();
            }
        });
        img_center_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

//                Displaying DialogPlus
        dialog.show();


    }

    private void matchingPasswordText(String text, TextView txt_lower_case, TextView txt_upper_case, TextView txt_number, TextView txt_chars) {
//        if (text.matches("(?=^.{8,25}$)(?=.*\\d)(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$")) {
        if (text.matches("(?=^.{8,25}$)(?=.*\\d)(?![.\\n])(?=.*[A-Z])(?=.*[a-z])(?=.*[@#$%^&+=!*]).*$")) {
            txt_lower_case.setBackground(getResources().getDrawable(R.drawable.rec_green_c2));
            txt_upper_case.setBackground(getResources().getDrawable(R.drawable.rec_green_c2));
            txt_number.setBackground(getResources().getDrawable(R.drawable.rec_green_c2));
            txt_chars.setBackground(getResources().getDrawable(R.drawable.rec_green_c2));
        } else {
//            if (text.matches("(?![.\\n])(?=.*[a-z]).*$+")) {
            if (text.matches("(?![.\\n])(?=.*[@#$%^&+=!*]).*$+")) {
                txt_lower_case.setBackground(getResources().getDrawable(R.drawable.rec_lgreen_c2));
            } else {
                txt_lower_case.setBackground(getResources().getDrawable(R.drawable.rec_gred_c2));
            }
            if (text.matches("(?![.\\n])(?=.*[A-Z]).*$+")) {
                txt_upper_case.setBackground(getResources().getDrawable(R.drawable.rec_lgreen_c2));
            } else {
                txt_upper_case.setBackground(getResources().getDrawable(R.drawable.rec_gred_c2));
            }

            if (text.matches("(?![.\\n])(?=.*\\d).*$+")) {
                txt_number.setBackground(getResources().getDrawable(R.drawable.rec_lgreen_c2));
            } else {
                txt_number.setBackground(getResources().getDrawable(R.drawable.rec_gred_c2));
            }

            if (text.length() > 7 && text.length() < 26) {
                txt_chars.setBackground(getResources().getDrawable(R.drawable.rec_lgreen_c2));
            } else {
                txt_chars.setBackground(getResources().getDrawable(R.drawable.rec_gred_c2));
            }
        }
    }

    private void CheckingInputs(String tkn, String old_pswd, String new_pswd, String conf_pswd) {
        if (!old_pswd.isEmpty()) {
            if (old_pswd.equals(sharedPreferences.getString(CONSTANTS.pswd, ""))) {
                if (!new_pswd.isEmpty()) {
                    if (new_pswd.matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*[@#$%^&+=])(?=\\S+$).{8,25}$")) {
                        if (!conf_pswd.isEmpty()) {
                            if (new_pswd.equals(conf_pswd)) {
                                if (CommonUtilities.isConnectionAvailable(AppSettingsActivity.this)) {
                                    invokeEmailRecovery(tkn, new_pswd);
                                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                                } else {
                                    CommonUtilities.ShowToastMessage(AppSettingsActivity.this, getResources().getString(R.string.internetconnection));
                                }
                            } else {
                                CommonUtilities.ShowToastMessage(AppSettingsActivity.this, getResources().getString(R.string.unmatch_conf_pswd));
                            }
                        } else {
                            CommonUtilities.ShowToastMessage(AppSettingsActivity.this, getResources().getString(R.string.empty_conf_pswd));
                        }
                    } else {
                        CommonUtilities.ShowToastMessage(AppSettingsActivity.this, getResources().getString(R.string.invalid_new_pswd));
                    }
                } else {
                    CommonUtilities.ShowToastMessage(AppSettingsActivity.this, getResources().getString(R.string.empty_new_pswd));
                }

            } else {
                CommonUtilities.ShowToastMessage(AppSettingsActivity.this, getResources().getString(R.string.unmatch_old_pswd));
            }
        } else {
            CommonUtilities.ShowToastMessage(AppSettingsActivity.this, getResources().getString(R.string.empty_old_pswd));
        }

    }

    private void invokeEmailRecovery(String tkn, final String new_pswd) {
        try {
            JSONObject params = new JSONObject();
            try {
                params.put("password", new_pswd);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialog = ProgressDialog.show(AppSettingsActivity.this, "", getResources().getString(R.string.please_wait), true);
            UserControllerApi apiService = DeviantXApiClient.getClient().create(UserControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.updatePassword(params.toString(), CONSTANTS.DeviantMulti + tkn);
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
                                editor.putString(CONSTANTS.pswd, new_pswd);
                                editor.apply();
                                finish();
                                CommonUtilities.ShowToastMessage(AppSettingsActivity.this, getResources().getString(R.string.pswd_changed_succcess));
                            } else {
                                CommonUtilities.ShowToastMessage(AppSettingsActivity.this, loginResponseMsg);
                            }


                        } else {
                            CommonUtilities.ShowToastMessage(AppSettingsActivity.this, loginResponseMsg);
//                            Toast.makeText(getApplicationContext(), responsevalue, Toast.LENGTH_LONG).show();
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(AppSettingsActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(AppSettingsActivity.this, getResources().getString(R.string.Timeout));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Timeout), Toast.LENGTH_SHORT).show();
                    } else if (t instanceof java.net.ConnectException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(AppSettingsActivity.this, getResources().getString(R.string.networkerror));
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.networkerror), Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(AppSettingsActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception ex) {
            progressDialog.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(AppSettingsActivity.this, getResources().getString(R.string.errortxt));
//            Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
               /* Intent intent = new Intent(AppSettingsActivity.this, DashBoardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);*/
    }

    private void get2FAstatus() {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            progressDialog = ProgressDialog.show(AppSettingsActivity.this, "", getResources().getString(R.string.please_wait), true);
            UserControllerApi apiService = DeviantXApiClient.getClient().create(UserControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.get2FAStatus(CONSTANTS.DeviantMulti + token);
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
                                if (loginResponseData.equals("true")) {
                                    myApplication.set2FA(true);
                                    editor.putBoolean(CONSTANTS.twoFactorAuth, true);
                                    editor.putBoolean(CONSTANTS.login2FA, true);
                                    editor.apply();
                                    scompat_2fa.setChecked(true);
                                    scompat_2fa.setBackground(getResources().getDrawable(R.drawable.rec_white_white_c16));
                                    scompat_2fa.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                                    txt_2FA_status.setText(getResources().getString(R.string.active));
                                } else {
                                    myApplication.set2FA(false);
                                    editor.putBoolean(CONSTANTS.twoFactorAuth, false);
                                    editor.putBoolean(CONSTANTS.login2FA, false);
                                    editor.apply();
                                    scompat_2fa.setBackground(getResources().getDrawable(R.drawable.rec_white_trans_c16));
                                    scompat_2fa.setChecked(false);
                                    scompat_2fa.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.transparent)));
                                    txt_2FA_status.setText(getResources().getString(R.string.inactive));
                                }
/*
                                if (myApplication.get2FA()) {
                                    scompat_2fa.setChecked(true);
                                    scompat_2fa.setBackground(getResources().getDrawable(R.drawable.rec_white_white_c16));
                                    scompat_2fa.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                                    txt_2FA_status.setText(getResources().getString(R.string.active));
                                } else {
                                    scompat_2fa.setBackground(getResources().getDrawable(R.drawable.rec_white_trans_c16));
                                    scompat_2fa.setChecked(false);
                                    scompat_2fa.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.transparent)));
                                    txt_2FA_status.setText(getResources().getString(R.string.inactive));
                                }
*/

                            } else {
                                CommonUtilities.ShowToastMessage(AppSettingsActivity.this, loginResponseMsg);
                            }

                        } else {
                            CommonUtilities.ShowToastMessage(AppSettingsActivity.this, loginResponseMsg);
//                            Toast.makeText(getApplicationContext(), responsevalue, Toast.LENGTH_LONG).show();
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(AppSettingsActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(AppSettingsActivity.this, getResources().getString(R.string.Timeout));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Timeout), Toast.LENGTH_SHORT).show();
                    } else if (t instanceof java.net.ConnectException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(AppSettingsActivity.this, getResources().getString(R.string.networkerror));
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.networkerror), Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(AppSettingsActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception ex) {
            progressDialog.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(AppSettingsActivity.this, getResources().getString(R.string.errortxt));
//            Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
        }
    }

}

