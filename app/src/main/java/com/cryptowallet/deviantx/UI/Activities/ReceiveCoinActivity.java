package com.cryptowallet.deviantx.UI.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.ServiceAPIs.CryptoControllerApi;
import com.cryptowallet.deviantx.UI.Models.AccountWallet;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;
import com.cryptowallet.deviantx.Utilities.DeviantXApiClient;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cryptowallet.deviantx.Utilities.MyApplication.myApplication;

public class ReceiveCoinActivity extends AppCompatActivity {

    //    @BindView(R.id.) ;
    @BindView(R.id.toolbar_center_back)
    Toolbar toolbar_center_back;
    @BindView(R.id.txt_dev_address)
    TextView txt_dev_address;
    @BindView(R.id.img_qrcode)
    ImageView img_qrcode;
    @BindView(R.id.img_copy_address)
    ImageView img_copy_address;
    @BindView(R.id.lnr_down)
    LinearLayout lnr_down;
    @BindView(R.id.btn_share_qrcode)
    Button btn_share_qrcode;
    @BindView(R.id.img_coin_logo)
    ImageView img_coin_logo;
    @BindView(R.id.txt_coin_value)
    TextView txt_coin_value;
    @BindView(R.id.txt_wallet_name)
    TextView txt_wallet_name;
    @BindView(R.id.txt_note_dev_add)
    TextView txt_note_dev_add;
    @BindView(R.id.txt_lbl_coin_add)
    TextView txt_lbl_coin_add;
    @BindView(R.id.txt_coin_usd_value)
    TextView txt_coin_usd_value;
    @BindView(R.id.txt_percentage)
    TextView txt_percentage;


    AccountWallet selectedAccountWallet;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;
    String loginResponseMsg, loginResponseStatus, loginResponseData;


    @Override
    protected void onResume() {
        super.onResume();
        myApplication.disableScreenCapture(this);
//        CommonUtilities.serviceStart(ReceiveCoinActivity.this);
    }

  /*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_HOME) {
//            Log.e("home key pressed", "****");
            // write your code here to stop the activity
            CommonUtilities.serviceStop(ReceiveCoinActivity.this);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
//        Log.e("home key pressed on pause", "****");
        // write your code here to stop your service
        CommonUtilities.serviceStop(ReceiveCoinActivity.this);
        super.onPause();
    }
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_coin);

        ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        toolbar_center_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Bundle bundle = getIntent().getExtras();
        selectedAccountWallet = bundle.getParcelable(CONSTANTS.selectedAccountWallet);


        if (CommonUtilities.isConnectionAvailable(ReceiveCoinActivity.this)) {
//            Fetch Address
            fetchAddress(selectedAccountWallet);

        } else {
            CommonUtilities.ShowToastMessage(ReceiveCoinActivity.this, getResources().getString(R.string.internetconnection));
        }



/*
        txt_dev_address.setText(selectedAccountWallet.getStr_data_address());
*/

        txt_note_dev_add.setText(getResources().getString(R.string.attention_dev_address1) + " " + selectedAccountWallet/*.getAllCoins()*/.getStr_coin_code() + ". " + getResources().getString(R.string.attention_dev_address2));

        Picasso.with(ReceiveCoinActivity.this).load(selectedAccountWallet/*.getAllCoins()*/.getStr_coin_logo()).into(img_coin_logo);
        txt_coin_value.setText(selectedAccountWallet/*.getAllCoins()*/.getStr_coin_code());
        txt_wallet_name.setText(selectedAccountWallet/*.getAllCoins()*/.getStr_coin_name());
        txt_lbl_coin_add.setText(selectedAccountWallet/*.getAllCoins()*/.getStr_coin_code() + " Address");

        txt_coin_usd_value.setText("$ " + String.format("%.4f", selectedAccountWallet/*.getAllCoins().getStr_coin_usdValue()*/.getDbl_coin_usdValue()) + " USD");
        DecimalFormat rank = new DecimalFormat("0.00");
        if (selectedAccountWallet/*.getAllCoins()*/.getDbl_coin_24h() < 0) {
            txt_percentage.setText("" + rank.format(selectedAccountWallet/*.getAllCoins()*/.getDbl_coin_24h()) + "%");
            txt_percentage.setTextColor(getResources().getColor(R.color.google_red));
        } else {
            txt_percentage.setText("+" + rank.format(selectedAccountWallet/*.getAllCoins()*/.getDbl_coin_24h()) + "%");
            txt_percentage.setTextColor(getResources().getColor(R.color.green));
        }

        img_copy_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*
                CommonUtilities.copyToClipboard(ReceiveCoinActivity.this, selectedAccountWallet.getStr_data_address(), selectedAccountWallet*//*.getAllCoins()*//*.getStr_coin_name());
                 */
                CommonUtilities.copyToClipboard(ReceiveCoinActivity.this, txt_dev_address.getText().toString().trim()/* selectedAccountWallet.getStr_coin_name()*/, selectedAccountWallet/*.getAllCoins()*/.getStr_coin_name());
            }
        });

//           QR Code Generator
/*
        CommonUtilities.qrCodeGenerate(selectedAccountWallet.getStr_data_address(), img_qrcode, ReceiveCoinActivity.this);
*/

        btn_share_qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//           Sharing Address
/*
                CommonUtilities.shareAddress(selectedAccountWallet.getStr_data_address(), ReceiveCoinActivity.this);
*/
                CommonUtilities.shareAddress(txt_dev_address.getText().toString().trim()/*selectedAccountWallet.getStr_coin_name()*/, ReceiveCoinActivity.this);
            }
        });


    }

    private void fetchAddress(final AccountWallet selectedAccountWallet) {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            String wallet_name = sharedPreferences.getString(CONSTANTS.walletName, "sss");
            progressDialog = ProgressDialog.show(ReceiveCoinActivity.this, "", getResources().getString(R.string.please_wait), true);
            CryptoControllerApi apiService = DeviantXApiClient.getClient().create(CryptoControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.receiveCoins(CONSTANTS.DeviantMulti + token, selectedAccountWallet.getStr_coin_code(), wallet_name);
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
                                txt_dev_address.setText(loginResponseData);
                                CommonUtilities.qrCodeGenerate(loginResponseData, img_qrcode, ReceiveCoinActivity.this);
                            } else {
                                CommonUtilities.ShowToastMessage(ReceiveCoinActivity.this, loginResponseMsg);
                            }
                        } else {
                            CommonUtilities.ShowToastMessage(ReceiveCoinActivity.this, loginResponseMsg);
//                            Toast.makeText(getApplicationContext(), responsevalue, Toast.LENGTH_LONG).show();
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(ReceiveCoinActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(ReceiveCoinActivity.this, getResources().getString(R.string.Timeout));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Timeout), Toast.LENGTH_SHORT).show();
                    } else if (t instanceof java.net.ConnectException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(ReceiveCoinActivity.this, getResources().getString(R.string.networkerror));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.networkerror), Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(ReceiveCoinActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception ex) {
            progressDialog.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(ReceiveCoinActivity.this, getResources().getString(R.string.errortxt));
//            Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
        }

    }


}
