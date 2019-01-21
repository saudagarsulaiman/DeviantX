package com.cryptowallet.deviantx.UI.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.ServiceAPIs.CryptoControllerApi;
import com.cryptowallet.deviantx.ServiceAPIs.USDValues;
import com.cryptowallet.deviantx.ServiceAPIs.WithdrawControllerApi;
import com.cryptowallet.deviantx.UI.Models.AccountWallet;
import com.cryptowallet.deviantx.UI.Models.USDValue;
import com.cryptowallet.deviantx.UI.Services.WalletDataFetch;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;
import com.cryptowallet.deviantx.Utilities.DeviantXApiClient;
import com.google.zxing.Result;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cryptowallet.deviantx.Utilities.MyApplication.myApplication;

public class SendCoinActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    //    @BindView(R.id.) ;
    @BindView(R.id.toolbar_center_back)
    Toolbar toolbar_center_back;
    @BindView(R.id.lnr_down)
    LinearLayout lnr_down;
    @BindView(R.id.txt_avail_bal)
    TextView txt_avail_bal;
    @BindView(R.id.edt_btcp_address)
    EditText edt_btcp_address;
    @BindView(R.id.img_scanner)
    ImageView img_scanner;
    @BindView(R.id.edt_amount_bal)
    EditText edt_amount_bal;
    @BindView(R.id.txt_amount_code)
    TextView txt_amount_code;
    @BindView(R.id.edt_fiat_bal)
    EditText edt_fiat_bal;
    @BindView(R.id.txt_fiat_code)
    TextView txt_fiat_code;
    @BindView(R.id.spnr_fees)
    Spinner spnr_fees;
    @BindView(R.id.btn_send)
    Button btn_send;
    @BindView(R.id.img_coin_logo)
    ImageView img_coin_logo;
    @BindView(R.id.txt_coin_value)
    TextView txt_coin_value;
    @BindView(R.id.txt_wallet_name)
    TextView txt_wallet_name;
    @BindView(R.id.scan_qr)
    ZXingScannerView mScannerView;
    @BindView(R.id.scan_view)
    RelativeLayout mScannerLayout;
    @BindView(R.id.txt_coin_usd_value)
    TextView txt_coin_usd_value;
    @BindView(R.id.txt_percentage)
    TextView txt_percentage;


    Double usdCoinValue = 0.0;
    AccountWallet selectedAccountWallet;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;

    String loginResponseMsg, loginResponseStatus, loginResponseData;

    Boolean isEditFiat = false, isEditAmount = false;



/*
    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }
*/

    public static int PERMISSION_ALL = 1;
    public static String[] PERMISSIONS = {Manifest.permission.CAMERA};

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_coin);

        ButterKnife.bind(this);

        sharedPreferences = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();


//        selectedAccountWallet = new AccountWallet();
        Bundle bundle = getIntent().getExtras();
        selectedAccountWallet = bundle.getParcelable(CONSTANTS.selectedAccountWallet);


        if (CommonUtilities.isConnectionAvailable(SendCoinActivity.this)) {

            String coinCode = selectedAccountWallet/*.getAllCoins()*/.getStr_coin_code();

            if (coinCode.equals("FDN")) {
                usdCoinValue = 0.1;
                editor.putString(CONSTANTS.usdValue, String.valueOf(usdCoinValue));
                editor.apply();
            } else if (coinCode.equals("ROCO")) {
                usdCoinValue = 0.2;
                editor.putString(CONSTANTS.usdValue, String.valueOf(usdCoinValue));
                editor.apply();
            } else {
                convertCoinValue(coinCode, "USD");
            }

            toolbar_center_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

            edt_btcp_address.setHint("\t\t" + selectedAccountWallet/*.getAllCoins()*/.getStr_coin_code() + " " + getResources().getString(R.string.address));

            txt_avail_bal.setText("" + String.format("%.4f", selectedAccountWallet.getStr_data_balance()));
            txt_amount_code.setText(selectedAccountWallet/*.getAllCoins()*/.getStr_coin_code());

            Picasso.with(SendCoinActivity.this).load(selectedAccountWallet/*.getAllCoins()*/.getStr_coin_logo()).into(img_coin_logo);
            txt_coin_value.setText(selectedAccountWallet/*.getAllCoins()*/.getStr_coin_code());
            txt_wallet_name.setText(selectedAccountWallet/*.getAllCoins()*/.getStr_coin_name());


            txt_coin_usd_value.setText("$ " + String.format("%.4f", selectedAccountWallet/*.getAllCoins().getStr_coin_usdValue()*/.getDbl_coin_usdValue()) + " USD");
            DecimalFormat rank = new DecimalFormat("0.00");
            if (selectedAccountWallet/*.getAllCoins()*/.getDbl_coin_24h() < 0) {
                txt_percentage.setText("" + rank.format(selectedAccountWallet/*.getAllCoins()*/.getDbl_coin_24h()) + "%");
                txt_percentage.setTextColor(getResources().getColor(R.color.google_red));
            } else {
                txt_percentage.setText("+" + rank.format(selectedAccountWallet/*.getAllCoins()*/.getDbl_coin_24h()) + "%");
                txt_percentage.setTextColor(getResources().getColor(R.color.green));
            }

            edt_amount_bal.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    convertAmountToCoin(s.toString().trim());
                }
            });

            edt_fiat_bal.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    convertCoinToAmount(s.toString().trim());
                }
            });

            edt_amount_bal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    isEditAmount = b;
                }
            });

            edt_fiat_bal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    isEditFiat = b;
                }
            });
            btn_send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!edt_amount_bal.getText().toString().isEmpty()) {
                        try {
                            if (Double.parseDouble(edt_amount_bal.getText().toString().trim()) > 0) {
                                String send_bal = edt_amount_bal.getText().toString();
                                String fiat_bal = edt_fiat_bal.getText().toString();
//                                String fee = "0.0001";
                                Double ttl_rcv = Double.parseDouble(send_bal)/* - Double.parseDouble(fee)*/;

                                String str_btcp_address = edt_btcp_address.getText().toString();

                                if (!str_btcp_address.isEmpty() && !fiat_bal.isEmpty() && !send_bal.isEmpty()) {
//                            if (Double.parseDouble(fiat_bal) < selectedAccountWallet.getStr_data_balanceInUSD() && Double.parseDouble(send_bal) < selectedAccountWallet.getStr_data_balance()) {
                                    if (myApplication.get2FA()) {
                                        Intent intent = new Intent(SendCoinActivity.this, TwoFASendCoinActivity.class);
                                        Bundle bundle1 = new Bundle();
                                        bundle1.putParcelable(CONSTANTS.selectedAccountWallet, selectedAccountWallet);
                                        bundle1.putString(CONSTANTS.send_bal, send_bal);
                                        bundle1.putString(CONSTANTS.fiat_bal, fiat_bal);
                                        bundle1.putDouble(CONSTANTS.ttl_rcv, ttl_rcv);
                                        bundle1.putString(CONSTANTS.address, str_btcp_address);
                                        intent.putExtras(bundle1);
                                        startActivity(intent);
//                                        finish();
                                    } else {
                                        customDialog(selectedAccountWallet, send_bal, fiat_bal, /*fee, */ttl_rcv, str_btcp_address);
                                    }
//                            } else {
//                                CommonUtilities.ShowToastMessage(SendCoinActivity.this, getResources().getString(R.string.insufficient_fund));
//                            }
                                } else {
                                    CommonUtilities.ShowToastMessage(SendCoinActivity.this, getResources().getString(R.string.enter_every_detail));
                                }
                            } else {
                                CommonUtilities.ShowToastMessage(SendCoinActivity.this, getResources().getString(R.string.enter_amount));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        CommonUtilities.ShowToastMessage(SendCoinActivity.this, getResources().getString(R.string.enter_amount));
                    }
                }
            });

            img_scanner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   /* Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                    intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                    startActivityForResult(intent, 0);*/
                    if (hasPermissions(SendCoinActivity.this, PERMISSIONS)) {
                        mScannerLayout.setVisibility(View.VISIBLE);
                        mScannerView.setResultHandler(SendCoinActivity.this); // Register ourselves as a handler for scan results.<br />
                        mScannerView.startCamera();
                    } else {
                        ActivityCompat.requestPermissions(SendCoinActivity.this, PERMISSIONS, PERMISSION_ALL);
                    }
                }
            });

        } else {
            CommonUtilities.ShowToastMessage(SendCoinActivity.this, getResources().getString(R.string.internetconnection));
            onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mScannerLayout.setVisibility(View.VISIBLE);
            mScannerView.setResultHandler(SendCoinActivity.this); // Register ourselves as a handler for scan results.<br />
            mScannerView.startCamera();
        } else {
            CommonUtilities.ShowToastMessage(SendCoinActivity.this, getResources().getString(R.string.cam_per_failed));
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }


    private void convertAmountToCoin(String amountTextValue) {
        if (isEditAmount) {
            if (!amountTextValue.trim().isEmpty()) {
                try {
                    if (Double.parseDouble(amountTextValue) != 0) {
                        Double finalValue = Double.parseDouble(amountTextValue);
                        if (selectedAccountWallet.getStr_data_balance() > finalValue) {
                            edt_fiat_bal.setText(String.format("%.4f", (usdCoinValue * finalValue)));
                        } else {
                            CommonUtilities.ShowToastMessage(SendCoinActivity.this, getResources().getString(R.string.insufficient_fund));
                            edt_fiat_bal.setText("0");
                            edt_amount_bal.setText("0");
                        }
                    } else {
                        edt_fiat_bal.setText("0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    edt_fiat_bal.setText("0");
                }
            } else {
                edt_fiat_bal.setText("0");
            }
        }
    }

    private void convertCoinToAmount(String coinTextValue) {
        if (isEditFiat) {
            if (!coinTextValue.trim().isEmpty()) {
                try {
                    if (Double.parseDouble(coinTextValue) != 0) {
                        Double finalValue = Double.parseDouble(coinTextValue);
                        if (selectedAccountWallet.getStr_data_balance() > (finalValue / usdCoinValue)) {
                            edt_amount_bal.setText(String.format("%.4f", (finalValue / usdCoinValue)));
                        } else {
                            edt_amount_bal.setText("0");
                            edt_fiat_bal.setText("0");

                            CommonUtilities.ShowToastMessage(SendCoinActivity.this, getResources().getString(R.string.insufficient_fund));
                        }
                    } else {
                        edt_amount_bal.setText("0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    edt_amount_bal.setText("0");
                }
            } else {
                edt_amount_bal.setText("0");
            }
        }
    }

    private void convertCoinValue(final String from_coin, final String to_coin) {
        try {
            progressDialog = ProgressDialog.show(SendCoinActivity.this, "", getResources().getString(R.string.please_wait), true);
            USDValues apiService = DeviantXApiClient.getCoinValues().create(USDValues.class);
            Call<USDValue> apiResponse = apiService.getUsdConversion(from_coin/*, to_coin*/);
            Log.i("API:\t:", apiResponse.toString());
            apiResponse.enqueue(new Callback<USDValue>() {
                @Override
                public void onResponse(Call<USDValue> call, Response<USDValue> response) {
                    try {

                        if (response != null) {
                            progressDialog.dismiss();
                            usdCoinValue = response.body().getUSD();
                            editor.putString(CONSTANTS.usdValue, String.valueOf(usdCoinValue));
                            editor.apply();
//                            CommonUtilities.ShowToastMessage(SendCoinActivity.this, "fetched");
//                            Log.e(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        } else {
//                            CommonUtilities.ShowToastMessage(SendCoinActivity.this, loginResponseMsg);
//                            Toast.makeText(getApplicationContext(), responsevalue, Toast.LENGTH_LONG).show();
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + response.message());
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(SendCoinActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<USDValue> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(SendCoinActivity.this, getResources().getString(R.string.Timeout));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Timeout), Toast.LENGTH_SHORT).show();
                    } else if (t instanceof java.net.ConnectException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(SendCoinActivity.this, getResources().getString(R.string.networkerror));
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.networkerror), Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(SendCoinActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception ex) {
            progressDialog.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(SendCoinActivity.this, getResources().getString(R.string.errortxt));
//            Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
        }
    }

    private void customDialog(final AccountWallet selectedAccountWallet, String send_bal, String fiat_bal/*, String fee*/, final Double ttl_rcv, final String toAddress) {
        //                Creating A Custom Dialog Using DialogPlus
        ViewHolder viewHolder = new ViewHolder(R.layout.dialog_send_confirm);
        final DialogPlus dialog = DialogPlus.newDialog(SendCoinActivity.this)
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
                SendingCoins(selectedAccountWallet.getStr_coin_name(), toAddress, ttl_rcv);
                dialog.dismiss();

            }
        });
//                Displaying DialogPlus
        dialog.show();

    }

    private void SendingCoins(String fromAddress, String toAddress, Double amount) {
        try {
            JSONObject params = new JSONObject();
            try {
                params.put("fromAddress", fromAddress);
                Log.e("fromAddress:", fromAddress);
                params.put("toAddress", toAddress);
                params.put("amount", amount);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            progressDialog = ProgressDialog.show(SendCoinActivity.this, "", getResources().getString(R.string.please_wait), true);
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

                                CommonUtilities.ShowToastMessage(SendCoinActivity.this, loginResponseMsg);

                                Intent serviceIntent = new Intent(getApplicationContext(), WalletDataFetch.class);
                                serviceIntent.putExtra("walletList", true);
                                startService(serviceIntent);
                                finish();

//                                Intent intent = new Intent(SendCoinActivity.this, DashBoardActivity.class);
//                                startActivity(intent);

                            } else {
//                                Intent intent = new Intent(SendCoinActivity.this, DashBoardActivity.class);
//                                startActivity(intent);
                                CommonUtilities.ShowToastMessage(SendCoinActivity.this, getResources().getString(R.string.transaction) + loginResponseMsg);
                            }

                        } else {
                            CommonUtilities.ShowToastMessage(SendCoinActivity.this, loginResponseMsg);
//                            Toast.makeText(getApplicationContext(), responsevalue, Toast.LENGTH_LONG).show();
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(SendCoinActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(SendCoinActivity.this, getResources().getString(R.string.Timeout));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Timeout), Toast.LENGTH_SHORT).show();
                    } else if (t instanceof java.net.ConnectException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(SendCoinActivity.this, getResources().getString(R.string.networkerror));
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.networkerror), Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(SendCoinActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception ex) {
            progressDialog.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(SendCoinActivity.this, getResources().getString(R.string.errortxt));
//            Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
        }
    }


   /* //Getting the scan results
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //  IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        //  if (result != null) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                            *//*String contents = intent.getStringExtra("SCAN_RESULT");
                            String format = intent.getStringExtra("SCAN_RESULT_FORMAT");*//*
                edt_btcp_address.setText(data.getStringExtra("SCAN_RESULT"));
                // Handle successful scan
            } else if (resultCode == RESULT_CANCELED) {
                CommonUtilities.ShowToastMessage(SendCoinActivity.this, getResources().getString(R.string.cancelled));
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        myApplication.disableScreenCapture(this);
        CommonUtilities.serviceStart(SendCoinActivity.this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_HOME) {
//            Log.e("home key pressed", "****");
            // write your code here to stop the activity
            CommonUtilities.serviceStop(SendCoinActivity.this);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
//        Log.e("home key pressed on pause", "****");
        // write your code here to stop your service
        mScannerView.stopCamera();
        CommonUtilities.serviceStop(SendCoinActivity.this);
        super.onPause();
    }


    @Override
    public void handleResult(Result result) {
        edt_btcp_address.setText(result.getText());
        mScannerLayout.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        if (mScannerLayout.getVisibility() == View.VISIBLE) {
            mScannerLayout.setVisibility(View.GONE);
            CommonUtilities.ShowToastMessage(SendCoinActivity.this, getResources().getString(R.string.cancelled));
        } else
            super.onBackPressed();
    }
}