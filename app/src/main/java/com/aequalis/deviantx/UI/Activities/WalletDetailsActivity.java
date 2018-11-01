package com.aequalis.deviantx.UI.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aequalis.deviantx.R;
import com.aequalis.deviantx.ServiceAPIs.AuthenticationApi;
import com.aequalis.deviantx.ServiceAPIs.CryptoControllerApi;
import com.aequalis.deviantx.UI.Models.AccountWallet;
import com.aequalis.deviantx.Utilities.CONSTANTS;
import com.aequalis.deviantx.Utilities.CommonUtilities;
import com.aequalis.deviantx.Utilities.DeviantXApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketTimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.aequalis.deviantx.Utilities.MyApplication.myApplication;

public class WalletDetailsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_center_back)
    Toolbar toolbar_center_back;
    @BindView(R.id.txt_mywallet_usd)
    TextView txt_mywallet_usd;
    @BindView(R.id.txt_mywallet_btc)
    TextView txt_mywallet_btc;
    @BindView(R.id.img_dev_qrcode)
    ImageView img_dev_qrcode;
    @BindView(R.id.lnr_share)
    LinearLayout lnr_share;
    @BindView(R.id.lnr_copy)
    LinearLayout lnr_copy;
    @BindView(R.id.txt_dev_address)
    TextView txt_dev_address;
    @BindView(R.id.txt_dev_private_key)
    TextView txt_dev_private_key;
    @BindView(R.id.txt_derivation_path)
    TextView txt_derivation_path;


    AccountWallet selectedAccountWallet;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;


    String loginResponseMsg, loginResponseStatus, loginResponseData;

    @Override
    protected void onRestart() {
        super.onRestart();
        myApplication.disableScreenCapture(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_details);

        ButterKnife.bind(this);

        sharedPreferences = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Bundle bundle = getIntent().getExtras();
        selectedAccountWallet = bundle.getParcelable(CONSTANTS.selectedAccountWallet);

        toolbar_center_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
//        Fetching Private Key
        String s_pswd = sharedPreferences.getString(CONSTANTS.pswd, "");

        if (CommonUtilities.isConnectionAvailable(WalletDetailsActivity.this)) {

            fetchPrivateKey(selectedAccountWallet.getStr_data_address(), s_pswd);

            txt_mywallet_usd.setText("$ " + selectedAccountWallet.getStr_data_balanceInUSD() + " USD");
            txt_mywallet_btc.setText(selectedAccountWallet.getStr_data_balance() + " " + selectedAccountWallet.getAllCoins().getStr_coin_code());
            txt_dev_address.setText(selectedAccountWallet.getStr_data_address());

            txt_derivation_path.setText("M/44H/425H/0H");

//        QR Code Generator
            CommonUtilities.qrCodeGenerate(selectedAccountWallet.getStr_data_address(), img_dev_qrcode, WalletDetailsActivity.this);

            lnr_copy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonUtilities.copyToClipboard(WalletDetailsActivity.this, selectedAccountWallet.getStr_data_address(), selectedAccountWallet.getAllCoins().getStr_coin_name());
                }
            });

            lnr_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//           Sharing Address
                    CommonUtilities.shareAddress(selectedAccountWallet.getStr_data_address(), WalletDetailsActivity.this);
                }
            });

        } else {
            CommonUtilities.ShowToastMessage(WalletDetailsActivity.this, getResources().getString(R.string.internetconnection));
        }

    }

    private void fetchPrivateKey(String s_address, String s_pswd) {

        try {
            JSONObject params = new JSONObject();
            try {
                params.put("address", s_address);
                params.put("password", s_pswd);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            progressDialog = ProgressDialog.show(WalletDetailsActivity.this, "", getResources().getString(R.string.please_wait), true);
            CryptoControllerApi apiService = DeviantXApiClient.getClient().create(CryptoControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.getPrivateKey(params.toString(), CONSTANTS.DeviantMulti + token);
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

                                txt_dev_private_key.setText(loginResponseData);

                            } else {
                                CommonUtilities.ShowToastMessage(WalletDetailsActivity.this, loginResponseMsg);
                            }

                        } else {
                            CommonUtilities.ShowToastMessage(WalletDetailsActivity.this, loginResponseMsg);
//                            Toast.makeText(getApplicationContext(), responsevalue, Toast.LENGTH_LONG).show();
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(WalletDetailsActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(WalletDetailsActivity.this, getResources().getString(R.string.Timeout));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Timeout), Toast.LENGTH_SHORT).show();
                    } else if (t instanceof java.net.ConnectException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(WalletDetailsActivity.this, getResources().getString(R.string.networkerror));
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.networkerror), Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(WalletDetailsActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception ex) {
            progressDialog.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(WalletDetailsActivity.this, getResources().getString(R.string.errortxt));
//            Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
        }

    }


}
