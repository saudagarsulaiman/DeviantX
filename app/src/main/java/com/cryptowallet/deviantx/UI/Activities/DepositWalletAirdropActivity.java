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
import android.widget.TextView;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.ServiceAPIs.AirdropWalletControllerApi;
import com.cryptowallet.deviantx.UI.Models.AirdropWallet;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;
import com.cryptowallet.deviantx.Utilities.DeviantXApiClient;

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

public class DepositWalletAirdropActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_center_back)
    Toolbar toolbar_center_back;
    @BindView(R.id.img_coin_logo)
    ImageView img_coin_logo;
    @BindView(R.id.txt_coin_code)
    TextView txt_coin_code;
    @BindView(R.id.txt_coin_name)
    TextView txt_coin_name;
    @BindView(R.id.txt_coin_usd_value)
    TextView txt_coin_usd_value;
    @BindView(R.id.img_qrcode)
    ImageView img_qrcode;
    @BindView(R.id.img_copy_address)
    ImageView img_copy_address;
    @BindView(R.id.txt_address)
    TextView txt_address;
    @BindView(R.id.btn_share_qrcode)
    Button btn_share_qrcode;

    AirdropWallet selected_coin;
    String address;
    ArrayList<AirdropWallet> selectedAccountWallet;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;
    String loginResponseMsg, loginResponseStatus, loginResponseData;

    @Override
    protected void onResume() {
        super.onResume();
        myApplication.disableScreenCapture(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit_wallet_airdrop);

        ButterKnife.bind(this);

        selectedAccountWallet = new ArrayList<>();
        toolbar_center_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        sharedPreferences = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();


        Bundle bundle = getIntent().getExtras();
//        selected_coin = bundle.getParcelable(CONSTANTS.selectedCoin);
        selectedAccountWallet = bundle.getParcelableArrayList(CONSTANTS.selectedAccountWallet);

        if (CommonUtilities.isConnectionAvailable(DepositWalletAirdropActivity.this)) {
//            Fetch Address
            fetchAddress(selectedAccountWallet.get(0));

        } else {
            CommonUtilities.ShowToastMessage(DepositWalletAirdropActivity.this, getResources().getString(R.string.internetconnection));
        }


/*
        txt_address.setText(selectedAccountWallet.get(0).getStr_data_ad_address());
*/
//           QR Code Generator
/*
        CommonUtilities.qrCodeGenerate(selectedAccountWallet.get(0).getStr_data_ad_address(), img_qrcode, DepositWalletAirdropActivity.this);
*/
//        txt_coin_usd_value.setText(String.format("%.4f", ));
        img_copy_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtilities.copyToClipboard(DepositWalletAirdropActivity.this, txt_address.getText().toString().trim(), selectedAccountWallet.get(0).getStr_ad_coin_name());
            }
        });
        btn_share_qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//           Sharing Address
                CommonUtilities.shareAddress(txt_address.getText().toString().trim(), DepositWalletAirdropActivity.this);
            }
        });

    }

    private void fetchAddress(final AirdropWallet selectedAccountWallet) {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            progressDialog = ProgressDialog.show(DepositWalletAirdropActivity.this, "", getResources().getString(R.string.please_wait), true);
            AirdropWalletControllerApi apiService = DeviantXApiClient.getClient().create(AirdropWalletControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.getAirdropWalletAddress(CONSTANTS.DeviantMulti + token, selectedAccountWallet.getStr_ad_coin_code());
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
                                txt_address.setText(loginResponseData);
                                CommonUtilities.qrCodeGenerate(loginResponseData, img_qrcode, DepositWalletAirdropActivity.this);
                            } else {
                                CommonUtilities.ShowToastMessage(DepositWalletAirdropActivity.this, loginResponseMsg);
                            }
                        } else {
                            CommonUtilities.ShowToastMessage(DepositWalletAirdropActivity.this, loginResponseMsg);
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(DepositWalletAirdropActivity.this, getResources().getString(R.string.errortxt));
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(DepositWalletAirdropActivity.this, getResources().getString(R.string.Timeout));
                    } else if (t instanceof java.net.ConnectException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(DepositWalletAirdropActivity.this, getResources().getString(R.string.networkerror));
                    } else {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(DepositWalletAirdropActivity.this, getResources().getString(R.string.errortxt));
                    }
                }
            });
        } catch (Exception ex) {
            progressDialog.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(DepositWalletAirdropActivity.this, getResources().getString(R.string.errortxt));
        }

    }


}
