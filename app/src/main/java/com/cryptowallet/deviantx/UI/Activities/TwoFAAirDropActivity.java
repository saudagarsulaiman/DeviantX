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
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.ServiceAPIs.AirdropWalletControllerApi;
import com.cryptowallet.deviantx.ServiceAPIs.AuthenticationApi;
import com.cryptowallet.deviantx.ServiceAPIs.UserControllerApi;
import com.cryptowallet.deviantx.UI.Models.AirdropWallet;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;
import com.cryptowallet.deviantx.Utilities.DeviantXApiClient;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cryptowallet.deviantx.Utilities.MyApplication.myApplication;

public class TwoFAAirDropActivity extends AppCompatActivity {

    //    @BindView(R.id.)
//    ;
    @BindView(R.id.toolbar_center_back)
    Toolbar toolbar_center_back;
    @BindView(R.id.edt_google_auth_code)
    EditText edt_google_auth_code;
    @BindView(R.id.btn_submit)
    Button btn_submit;

    ArrayList<AirdropWallet> airdropWalletlist;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;

    String regResponseMsg, regResponseStatus, regResponsedata;

    String walletName, to_address, amount;

    @Override
    protected void onResume() {
        super.onResume();
        myApplication.disableScreenCapture(this);
        CommonUtilities.serviceStart(TwoFAAirDropActivity.this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_HOME) {
//            Log.e("home key pressed", "****");
            // write your code here to stop the activity
            CommonUtilities.serviceStop(TwoFAAirDropActivity.this);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
//        Log.e("home key pressed on pause", "****");
        // write your code here to stop your service
        CommonUtilities.serviceStop(TwoFAAirDropActivity.this);
        super.onPause();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_fa_airdrop);

        ButterKnife.bind(this);
        airdropWalletlist = new ArrayList<>();

        sharedPreferences = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        toolbar_center_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Bundle bundle = getIntent().getExtras();
        walletName = bundle.getString(CONSTANTS.walletName);
        to_address = bundle.getString(CONSTANTS.address);
        amount = bundle.getString(CONSTANTS.amount);
        airdropWalletlist = bundle.getParcelableArrayList(CONSTANTS.selectedAccountWallet);


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = edt_google_auth_code.getText().toString().trim();
                if (code.length() == 6)
                    Verify2FA(code, walletName, to_address, amount, airdropWalletlist);
                else
                    CommonUtilities.ShowToastMessage(TwoFAAirDropActivity.this, getResources().getString(R.string.invalid_code));
            }
        });

    }

    private void Verify2FA(String google_auth_code, String walletName, String to_address, String amount, ArrayList<AirdropWallet> airdropWalletlist) {
        try {
/*
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
*/
            String tkn = sharedPreferences.getString(CONSTANTS.token, "");
            progressDialog = ProgressDialog.show(TwoFAAirDropActivity.this, "", getResources().getString(R.string.please_wait), true);
            UserControllerApi apiService = DeviantXApiClient.getClient().create(UserControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.verify2FA(google_auth_code, CONSTANTS.DeviantMulti + tkn);
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
                                if (walletName.isEmpty())
                                    toAddressDialog(to_address, amount, airdropWalletlist.get(0).getStr_data_ad_address(), airdropWalletlist.get(0).getAllCoins().getStr_coin_code());
                                else
                                    toWalletDialog(walletName, amount, airdropWalletlist.get(0).getStr_data_ad_address(), airdropWalletlist.get(0).getAllCoins().getStr_coin_code());


                            } else {
                                CommonUtilities.ShowToastMessage(TwoFAAirDropActivity.this, regResponseMsg);

                            }
                        } else {
                            CommonUtilities.ShowToastMessage(TwoFAAirDropActivity.this, regResponseMsg);
//                            Toast.makeText(getApplicationContext(), responsevalue, Toast.LENGTH_LONG).show();
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(TwoFAAirDropActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(TwoFAAirDropActivity.this, getResources().getString(R.string.Timeout));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Timeout), Toast.LENGTH_SHORT).show();
                    } else if (t instanceof java.net.ConnectException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(TwoFAAirDropActivity.this, getResources().getString(R.string.networkerror));
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.networkerror), Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(TwoFAAirDropActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception ex) {
            progressDialog.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(TwoFAAirDropActivity.this, getResources().getString(R.string.errortxt));
//            Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
        }
    }


    private void toAddressDialog(String to_address, String amount, String str_data_ad_address, String str_coin_code) {
        //                Creating A Custom Dialog Using DialogPlus
        ViewHolder viewHolder = new ViewHolder(R.layout.dialog_withdraw_confirm);
        final DialogPlus dialog = DialogPlus.newDialog(TwoFAAirDropActivity.this)
                .setContentHolder(viewHolder)
                .setGravity(Gravity.BOTTOM)
                .setCancelable(true)
                .setInAnimation(R.anim.slide_in_bottom)
                .setOutAnimation(R.anim.slide_out_bottom)
                .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .create();

//                Initializing Widgets
        View view = dialog.getHolderView();
        TextView txt_cancel = view.findViewById(R.id.txt_cancel);
        TextView txt_confirm = view.findViewById(R.id.txt_confirm);
        TextView txt_withdraw_amt = view.findViewById(R.id.txt_withdraw_amt);
        TextView txt_withdraw_amt_code = view.findViewById(R.id.txt_withdraw_amt_code);
        TextView txt_fee_amt = view.findViewById(R.id.txt_fee_amt);
        TextView txt_fee_amt_code = view.findViewById(R.id.txt_fee_amt_code);
        TextView txt_address = view.findViewById(R.id.txt_address);
        TextView txt_privacy_policy = view.findViewById(R.id.txt_privacy_policy);

        txt_withdraw_amt.setText(amount);
        txt_withdraw_amt_code.setText(str_coin_code);
//        txt_fee_amt.setText(String.format("%.4f",));
        txt_fee_amt.setText("0.001");
        txt_fee_amt_code.setText(str_coin_code);
        txt_address.setText(to_address);
/*
        txt_privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
*/

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        txt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transferAmountToAddress(to_address, amount, str_data_ad_address);
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    private void transferAmountToAddress(String to_address, String amount, String fromAddress) {
        try {
            JSONObject params = new JSONObject();
            String token = sharedPreferences.getString(CONSTANTS.token, "");
            try {
                params.put("fromAddress", fromAddress);
                params.put("toAddress", to_address);
                params.put("amount", amount);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialog = ProgressDialog.show(TwoFAAirDropActivity.this, "", getResources().getString(R.string.please_wait), true);
            AirdropWalletControllerApi apiService = DeviantXApiClient.getClient().create(AirdropWalletControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.transferToAddress(params.toString(), "DEVIANTMULTI " + token);
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
//                                CommonUtilities.ShowToastMessage(TwoFAAirDropActivity.this, getResources().getString(R.string.login_success));
                                Intent intent = new Intent(TwoFAAirDropActivity.this, DashBoardActivity.class);
                                intent.putExtra(CONSTANTS.seletedTab, 2);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                CommonUtilities.ShowToastMessage(TwoFAAirDropActivity.this, regResponseMsg);
//                                onBackPressed();
//                                Log.i(CONSTANTS.TAG, "onResponse:\n" + loginResponseMsg);
                            } else {
                                CommonUtilities.ShowToastMessage(TwoFAAirDropActivity.this, regResponseMsg);
                            }
                        } else {
                            CommonUtilities.ShowToastMessage(TwoFAAirDropActivity.this, regResponseMsg);
//                            Toast.makeText(getApplicationContext(), responsevalue, Toast.LENGTH_LONG).show();
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(TwoFAAirDropActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(TwoFAAirDropActivity.this, getResources().getString(R.string.Timeout));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Timeout), Toast.LENGTH_SHORT).show();
                    } else if (t instanceof java.net.ConnectException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(TwoFAAirDropActivity.this, getResources().getString(R.string.networkerror));
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.networkerror), Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(TwoFAAirDropActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception ex) {
            progressDialog.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(TwoFAAirDropActivity.this, getResources().getString(R.string.errortxt));
//            Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
        }

    }


    private void toWalletDialog(String walletName, String amount, String fromAddress, String str_coin_code) {
        //                Creating A Custom Dialog Using DialogPlus
        ViewHolder viewHolder = new ViewHolder(R.layout.dialog_withdraw_confirm);
        final DialogPlus dialog = DialogPlus.newDialog(TwoFAAirDropActivity.this)
                .setContentHolder(viewHolder)
                .setGravity(Gravity.BOTTOM)
                .setCancelable(true)
                .setInAnimation(R.anim.slide_in_bottom)
                .setOutAnimation(R.anim.slide_out_bottom)
                .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .create();

//                Initializing Widgets
        View view = dialog.getHolderView();
        TextView txt_cancel = view.findViewById(R.id.txt_cancel);
        TextView txt_confirm = view.findViewById(R.id.txt_confirm);
        TextView txt_withdraw_amt = view.findViewById(R.id.txt_withdraw_amt);
        TextView txt_withdraw_amt_code = view.findViewById(R.id.txt_withdraw_amt_code);
        TextView txt_fee_amt = view.findViewById(R.id.txt_fee_amt);
        TextView txt_fee_amt_code = view.findViewById(R.id.txt_fee_amt_code);
        TextView txt_address = view.findViewById(R.id.txt_address);
        TextView txt_privacy_policy = view.findViewById(R.id.txt_privacy_policy);


        txt_withdraw_amt.setText(amount);
        txt_withdraw_amt_code.setText(str_coin_code);
//        txt_fee_amt.setText(String.format("%.4f",));
        txt_fee_amt.setText("0.001");
        txt_fee_amt_code.setText(str_coin_code);
        txt_address.setText(walletName);

        txt_privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        txt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transferAmountToWallet(fromAddress, walletName, amount, str_coin_code);
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    private void transferAmountToWallet(String fromAddress, String walletName, String amount, String str_coin_code) {
        try {
            JSONObject params = new JSONObject();
            String token = sharedPreferences.getString(CONSTANTS.token, "");
            try {
                params.put("fromAddress", fromAddress);
                params.put("wallet", walletName);
                params.put("amount", amount);
                params.put("coinCode", str_coin_code);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialog = ProgressDialog.show(TwoFAAirDropActivity.this, "", getResources().getString(R.string.please_wait), true);
            AirdropWalletControllerApi apiService = DeviantXApiClient.getClient().create(AirdropWalletControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.transferToWallet(params.toString(), "DEVIANTMULTI " + token);
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
//                                CommonUtilities.ShowToastMessage(TwoFAAirDropActivity.this, getResources().getString(R.string.login_success));
                                Intent intent = new Intent(TwoFAAirDropActivity.this, DashBoardActivity.class);
                                intent.putExtra(CONSTANTS.seletedTab, 2);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                CommonUtilities.ShowToastMessage(TwoFAAirDropActivity.this, regResponseMsg);
//                                onBackPressed();
//                                Log.i(CONSTANTS.TAG, "onResponse:\n" + loginResponseMsg);
                            } else {
                                CommonUtilities.ShowToastMessage(TwoFAAirDropActivity.this, regResponseMsg);
                            }
                        } else {
                            CommonUtilities.ShowToastMessage(TwoFAAirDropActivity.this, regResponseMsg);
//                            Toast.makeText(getApplicationContext(), responsevalue, Toast.LENGTH_LONG).show();
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(TwoFAAirDropActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(TwoFAAirDropActivity.this, getResources().getString(R.string.Timeout));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Timeout), Toast.LENGTH_SHORT).show();
                    } else if (t instanceof java.net.ConnectException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(TwoFAAirDropActivity.this, getResources().getString(R.string.networkerror));
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.networkerror), Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(TwoFAAirDropActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception ex) {
            progressDialog.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(TwoFAAirDropActivity.this, getResources().getString(R.string.errortxt));
//            Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
        }

    }


}
