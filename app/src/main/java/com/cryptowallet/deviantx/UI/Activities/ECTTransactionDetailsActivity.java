package com.cryptowallet.deviantx.UI.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.ServiceAPIs.ChangellyControllerApi;
import com.cryptowallet.deviantx.UI.Models.ECTCreateTransaction;
import com.cryptowallet.deviantx.UI.Models.ECTCreatedTransactionData;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;
import com.cryptowallet.deviantx.Utilities.DeviantXApiClient;
import com.cryptowallet.deviantx.Utilities.GsonUtils;

import org.json.JSONObject;

import java.net.SocketTimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ECTTransactionDetailsActivity extends AppCompatActivity {

    @BindView(R.id.img_qrcode)
    ImageView img_qrcode;
    @BindView(R.id.txt_payin_lbl_address)
    TextView txt_payin_lbl_address;
    @BindView(R.id.txt_payin_address)
    TextView txt_payin_address;
    @BindView(R.id.txt_lbl_payin_add)
    TextView txt_lbl_payin_add;
    @BindView(R.id.img_copy_address)
    ImageView img_copy_address;
    @BindView(R.id.txt_payin_amount)
    TextView txt_payin_amount;
    @BindView(R.id.img_tick_ec)
    ImageView img_tick_ec;
    @BindView(R.id.img_tick_pd)
    ImageView img_tick_pd;
    @BindView(R.id.img_tick_c)
    ImageView img_tick_c;
    @BindView(R.id.img_center_back)
    ImageView img_center_back;


    ECTCreateTransaction ectCreateTransaction;
    ECTCreatedTransactionData ectCreatedTransactionData;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;

    String loginResponseMsg, loginResponseStatus, loginResponseData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ect_transaction_details);
        ButterKnife.bind(this);

        sharedPreferences = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        img_center_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        int transID = getIntent().getIntExtra(CONSTANTS.ectTransID, 0);
        if (transID == 0) {
            ectCreateTransaction = getIntent().getParcelableExtra(CONSTANTS.ectTransData);
            txt_payin_lbl_address.setText(getResources().getString(R.string.payin_address) + " (" + ectCreateTransaction.getStr_currencyFrom().toUpperCase() + ")");
            txt_lbl_payin_add.setText(ectCreateTransaction.getStr_currencyFrom().toUpperCase() + " " + getResources().getString(R.string.address));
            txt_payin_address.setText(ectCreateTransaction.getStr_payinAddress());
            CommonUtilities.qrCodeGenerate(ectCreateTransaction.getStr_payinAddress(), img_qrcode, ECTTransactionDetailsActivity.this);
            txt_payin_amount.setText(ectCreateTransaction.getStr_amountExpectedFrom() + " " + ectCreateTransaction.getStr_currencyFrom().toUpperCase());
            setTransStatus(ectCreateTransaction.getStr_status());
        } else {
            if (CommonUtilities.isConnectionAvailable(ECTTransactionDetailsActivity.this)) {
                //GET Transaction By ID
                getTransactionDataByID(transID);
            } else {
                CommonUtilities.ShowToastMessage(ECTTransactionDetailsActivity.this, getResources().getString(R.string.internetconnection));
            }
        }

        img_copy_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtilities.copyToClipboard(ECTTransactionDetailsActivity.this, txt_payin_address.getText().toString().trim(), txt_lbl_payin_add.getText().toString().trim());
            }
        });


    }

    private void getTransactionDataByID(int id) {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            progressDialog = ProgressDialog.show(ECTTransactionDetailsActivity.this, "", getResources().getString(R.string.please_wait), true);
            ChangellyControllerApi apiService = DeviantXApiClient.getClient().create(ChangellyControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.getTransactionById(CONSTANTS.DeviantMulti + token, String.valueOf(id));
            apiResponse.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responsevalue = response.body().string();

                        progressDialog.dismiss();
                        if (!responsevalue.isEmpty() && responsevalue != null) {

                            JSONObject jsonObject = new JSONObject(responsevalue);
                            loginResponseMsg = jsonObject.getString("msg");
                            loginResponseStatus = jsonObject.getString("status");

                            if (loginResponseStatus.equals("true")) {
                                loginResponseData = jsonObject.getString("data");
                                ECTCreatedTransactionData[] transaction = GsonUtils.getInstance().fromJson(loginResponseData, ECTCreatedTransactionData[].class);
                                ectCreatedTransactionData = transaction[0];

                                txt_payin_lbl_address.setText(getResources().getString(R.string.payin_address) + " (" + ectCreatedTransactionData.getStr_currencyFrom().toUpperCase() + ")");
                                txt_lbl_payin_add.setText(ectCreatedTransactionData.getStr_currencyFrom().toUpperCase() + " " + getResources().getString(R.string.address));
                                txt_payin_address.setText(ectCreatedTransactionData.getStr_payinAddress());
                                CommonUtilities.qrCodeGenerate(ectCreatedTransactionData.getStr_payinAddress(), img_qrcode, ECTTransactionDetailsActivity.this);
                                txt_payin_amount.setText(ectCreatedTransactionData.getStr_amountExpectedFrom() + " " + ectCreatedTransactionData.getStr_currencyFrom().toUpperCase());
                                setTransStatus(ectCreatedTransactionData.getStr_status());
                            } else {
                                CommonUtilities.ShowToastMessage(ECTTransactionDetailsActivity.this, loginResponseMsg);
                            }

                        } else {
                            CommonUtilities.ShowToastMessage(ECTTransactionDetailsActivity.this, loginResponseMsg);
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(ECTTransactionDetailsActivity.this, getResources().getString(R.string.errortxt));
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(ECTTransactionDetailsActivity.this, getResources().getString(R.string.Timeout));
                    } else if (t instanceof java.net.ConnectException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(ECTTransactionDetailsActivity.this, getResources().getString(R.string.networkerror));
                    } else {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(ECTTransactionDetailsActivity.this, getResources().getString(R.string.errortxt));
                    }
                }
            });
        } catch (Exception ex) {
            progressDialog.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(ECTTransactionDetailsActivity.this, getResources().getString(R.string.errortxt));
        }
    }

    private void setTransStatus(String status) {
        if (status.trim().equals("new")) {
            img_tick_ec.setImageDrawable(getResources().getDrawable(R.drawable.tick_green));
            img_tick_pd.setImageDrawable(getResources().getDrawable(R.drawable.tick_grey));
            img_tick_c.setImageDrawable(getResources().getDrawable(R.drawable.tick_grey));
        } else if (status.trim().equals("waiting")) {
            img_tick_ec.setImageDrawable(getResources().getDrawable(R.drawable.tick_green));
            img_tick_pd.setImageDrawable(getResources().getDrawable(R.drawable.tick_green));
            img_tick_c.setImageDrawable(getResources().getDrawable(R.drawable.tick_grey));
        } else if (status.trim().toLowerCase().equals("completed") || status.trim().toLowerCase().equals("complete") || status.trim().toLowerCase().equals("success") || status.trim().toLowerCase().equals("successful")) {
            img_tick_ec.setImageDrawable(getResources().getDrawable(R.drawable.tick_green));
            img_tick_pd.setImageDrawable(getResources().getDrawable(R.drawable.tick_green));
            img_tick_c.setImageDrawable(getResources().getDrawable(R.drawable.tick_green));
        } else {
            img_tick_ec.setImageDrawable(getResources().getDrawable(R.drawable.tick_green));
            img_tick_pd.setImageDrawable(getResources().getDrawable(R.drawable.tick_grey));
            img_tick_c.setImageDrawable(getResources().getDrawable(R.drawable.tick_grey));
        }
    }

}
