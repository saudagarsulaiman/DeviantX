package com.cryptowallet.deviantx.UI.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.ServiceAPIs.UserControllerApi;
import com.cryptowallet.deviantx.ServiceAPIs.WithdrawControllerApi;
import com.cryptowallet.deviantx.UI.Models.AccountWallet;
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

public class TwoFASendCoinActivity extends AppCompatActivity {

    //    @BindView(R.id.)
//    ;
    @BindView(R.id.toolbar_center_back)
    Toolbar toolbar_center_back;
    @BindView(R.id.edt_google_auth_code)
    EditText edt_google_auth_code;
    @BindView(R.id.btn_submit)
    Button btn_submit;

    AccountWallet selectedAccountWallet;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;

    String regResponseMsg, regResponseStatus, regResponsedata, loginResponseMsg, loginResponseStatus, loginResponseData;

    String send_bal, fiat_bal, str_btcp_address;
    Double ttl_rcv;

    @Override
    protected void onResume() {
        super.onResume();
        myApplication.disableScreenCapture(this);
//        CommonUtilities.serviceStart(TwoFASendCoinActivity.this);
    }

/*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_HOME) {
//            Log.e("home key pressed", "****");
            // write your code here to stop the activity
            CommonUtilities.serviceStop(TwoFASendCoinActivity.this);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
//        Log.e("home key pressed on pause", "****");
        // write your code here to stop your service
        CommonUtilities.serviceStop(TwoFASendCoinActivity.this);
        super.onPause();
    }
*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_fa_send_coin);


        ButterKnife.bind(this);

        sharedPreferences = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        toolbar_center_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Bundle bundle1 = getIntent().getExtras();
        selectedAccountWallet = bundle1.getParcelable(CONSTANTS.selectedAccountWallet);
        send_bal = bundle1.getString(CONSTANTS.send_bal);
        fiat_bal = bundle1.getString(CONSTANTS.fiat_bal);
        ttl_rcv = bundle1.getDouble(CONSTANTS.ttl_rcv);
        str_btcp_address = bundle1.getString(CONSTANTS.address);


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = edt_google_auth_code.getText().toString().trim();
                if (code.length() == 6)
                    Verify2FA(code, selectedAccountWallet, send_bal, fiat_bal, /*fee, */ttl_rcv, str_btcp_address);
                else
                    CommonUtilities.ShowToastMessage(TwoFASendCoinActivity.this, getResources().getString(R.string.invalid_code));
            }
        });


    }

    private void Verify2FA(String code, AccountWallet selectedAccountWallet, String send_bal, String fiat_bal, Double ttl_rcv, String str_btcp_address) {
        try {
            String tkn = sharedPreferences.getString(CONSTANTS.token, "");
            progressDialog = ProgressDialog.show(TwoFASendCoinActivity.this, "", getResources().getString(R.string.please_wait), true);
            UserControllerApi apiService = DeviantXApiClient.getClient().create(UserControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.verify2FA(code, CONSTANTS.DeviantMulti + tkn);
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
                                customDialog(selectedAccountWallet, send_bal, fiat_bal, /*fee, */ttl_rcv, str_btcp_address);

                            } else {
                                CommonUtilities.ShowToastMessage(TwoFASendCoinActivity.this, regResponseMsg);
                            }
                        } else {
                            CommonUtilities.ShowToastMessage(TwoFASendCoinActivity.this, regResponseMsg);
//                            Toast.makeText(getApplicationContext(), responsevalue, Toast.LENGTH_LONG).show();
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(TwoFASendCoinActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(TwoFASendCoinActivity.this, getResources().getString(R.string.Timeout));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Timeout), Toast.LENGTH_SHORT).show();
                    } else if (t instanceof java.net.ConnectException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(TwoFASendCoinActivity.this, getResources().getString(R.string.networkerror));
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.networkerror), Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(TwoFASendCoinActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception ex) {
            progressDialog.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(TwoFASendCoinActivity.this, getResources().getString(R.string.errortxt));
//            Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
        }
    }

    private void customDialog(final AccountWallet selectedAccountWallet, String send_bal, String fiat_bal/*, String fee*/, final Double ttl_rcv, final String toAddress) {
        //                Creating A Custom Dialog Using DialogPlus
        ViewHolder viewHolder = new ViewHolder(R.layout.dialog_send_confirm);
        final DialogPlus dialog = DialogPlus.newDialog(TwoFASendCoinActivity.this)
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
        TextView txt_cancel = view.findViewById(R.id.txt_cancel);
        TextView txt_send = view.findViewById(R.id.txt_send);

        TextView txt_amount_bal = view.findViewById(R.id.txt_amount_bal);
        TextView txt_amount_code = view.findViewById(R.id.txt_amount_code);
        TextView txt_fiat_bal = view.findViewById(R.id.txt_fiat_bal);
        TextView txt_fiat_code = view.findViewById(R.id.txt_fiat_code);
        TextView txt_to_address = view.findViewById(R.id.txt_to_address);
        TextView txt_fee = view.findViewById(R.id.txt_fee);
        TextView txt_fee_code = view.findViewById(R.id.txt_fee_code);
        TextView txt_ttl_receive = view.findViewById(R.id.txt_ttl_receive);
        TextView txt_ttl_receive_code = view.findViewById(R.id.txt_ttl_receive_code);

        txt_amount_bal.setText(send_bal);
        txt_amount_code.setText(selectedAccountWallet/*.getAllCoins()*/.getStr_coin_code());
        txt_fiat_bal.setText(fiat_bal);
//        txt_fiat_code.setText();
        txt_to_address.setText(toAddress);
//        txt_fee.setText(fee);
        txt_fee_code.setText(selectedAccountWallet/*.getAllCoins()*/.getStr_coin_code());
        txt_ttl_receive.setText("" + ttl_rcv);
        txt_ttl_receive_code.setText(selectedAccountWallet/*.getAllCoins()*/.getStr_coin_code());

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        txt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//            Senidng Coins
/*
                SendingCoins(selectedAccountWallet.getStr_data_address(), toAddress, ttl_rcv);
*/
//                SendingCoins(selectedAccountWallet.getStr_coin_name(), toAddress, ttl_rcv);
                String wallet_name = sharedPreferences.getString(CONSTANTS.walletName, "sss");
                SendingCoins(wallet_name, ttl_rcv, toAddress, selectedAccountWallet.getStr_coin_code());
                dialog.dismiss();

            }
        });
//                Displaying DialogPlus
        dialog.show();

    }

    //    private void SendingCoins(String fromAddress, String toAddress, Double amount) {
    private void SendingCoins(String wallet_name, Double amount, String toAddress, String coin_code) {
        try {
            JSONObject params = new JSONObject();
            try {
                params.put("wallet_name", wallet_name);
                params.put("amount", amount);
                params.put("toAddress", toAddress);
                params.put("coin_code", coin_code);
            } catch (JSONException e) {
                e.printStackTrace();
            }
/*
            try {
                params.put("fromAddress", fromAddress);
                Log.e("fromAddress:", fromAddress);
                params.put("toAddress", toAddress);
                params.put("amount", amount);
            } catch (JSONException e) {
                e.printStackTrace();
            }
*/
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            progressDialog = ProgressDialog.show(TwoFASendCoinActivity.this, "", getResources().getString(R.string.please_wait), true);
            WithdrawControllerApi apiService = DeviantXApiClient.getClient().create(WithdrawControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.transferCoins(params.toString(), CONSTANTS.DeviantMulti + token);
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

                                CommonUtilities.ShowToastMessage(TwoFASendCoinActivity.this, loginResponseMsg);
                                Intent serviceIntent = new Intent(getApplicationContext(), WalletDataFetch.class);
                                serviceIntent.putExtra("walletList", true);
                                startService(serviceIntent);
/*
                                CommonUtilities.serviceStart(TwoFASendCoinActivity.this);
*/
//                                finish();
                                Intent intent = new Intent(TwoFASendCoinActivity.this, DashBoardActivity.class);
                                startActivity(intent);

                            } else {
//                                Intent intent = new Intent(TwoFASendCoinActivity.this, DashBoardActivity.class);
//                                startActivity(intent);
                                CommonUtilities.ShowToastMessage(TwoFASendCoinActivity.this, getResources().getString(R.string.transaction) + loginResponseMsg);
                            }

                        } else {
                            CommonUtilities.ShowToastMessage(TwoFASendCoinActivity.this, loginResponseMsg);
//                            Toast.makeText(getApplicationContext(), responsevalue, Toast.LENGTH_LONG).show();
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(TwoFASendCoinActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(TwoFASendCoinActivity.this, getResources().getString(R.string.Timeout));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Timeout), Toast.LENGTH_SHORT).show();
                    } else if (t instanceof java.net.ConnectException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(TwoFASendCoinActivity.this, getResources().getString(R.string.networkerror));
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.networkerror), Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(TwoFASendCoinActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception ex) {
            progressDialog.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(TwoFASendCoinActivity.this, getResources().getString(R.string.errortxt));
//            Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
        }
    }


}
