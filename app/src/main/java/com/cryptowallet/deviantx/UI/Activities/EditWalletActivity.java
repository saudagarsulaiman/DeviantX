package com.cryptowallet.deviantx.UI.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.ServiceAPIs.WalletControllerApi;
import com.cryptowallet.deviantx.UI.Models.WalletList;
import com.cryptowallet.deviantx.UI.Services.WalletDataFetch;
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

public class EditWalletActivity extends AppCompatActivity {


    @BindView(R.id.edt_wallet)
    EditText edt_wallet;
    @BindView(R.id.txt_note_defWal)
    TextView txt_note_defWal;
    @BindView(R.id.btn_create)
    Button btn_create;
    @BindView(R.id.scompat_defWallet)
    SwitchCompat scompat_defWallet;
    @BindView(R.id.scompat_pin)
    SwitchCompat scompat_pin;
    @BindView(R.id.toolbar_center_back)
    Toolbar toolbar_center_back;


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String s_WalletName;
    String loginResponseData, loginResponseStatus, loginResponseMsg;
    ProgressDialog progressDialog;
    WalletList walletList;


    @Override
    protected void onRestart() {
        super.onRestart();
        myApplication.disableScreenCapture(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_wallet);
        ButterKnife.bind(this);

        sharedPreferences = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Bundle bundle = getIntent().getExtras();
        walletList = bundle.getParcelable(CONSTANTS.walletName);
        edt_wallet.setText(walletList.getStr_data_name());

        if (walletList.isDefaultWallet()) {
            txt_note_defWal.setVisibility(View.VISIBLE);
            scompat_defWallet.setChecked(true);
            scompat_defWallet.setEnabled(false);
        } else {
            txt_note_defWal.setVisibility(View.INVISIBLE);
            scompat_defWallet.setChecked(false);
            scompat_defWallet.setEnabled(true);
        }
        scompat_defWallet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (scompat_defWallet.isChecked()) {
//                    scompat_defWallet.setEnabled(false);
                    txt_note_defWal.setVisibility(View.VISIBLE);
                } else {
//                    scompat_defWallet.setEnabled(true);
                    txt_note_defWal.setVisibility(View.INVISIBLE);
                }
            }
        });


        toolbar_center_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s_WalletName = edt_wallet.getText().toString().trim();
                if (s_WalletName.isEmpty()) {
                    edt_wallet.setText("");
                    CommonUtilities.ShowToastMessage(EditWalletActivity.this, getResources().getString(R.string.empty_wallet));
                } else {
                    if (CommonUtilities.isConnectionAvailable(EditWalletActivity.this)) {
                        if (s_WalletName.equals(walletList.getStr_data_name()))
                            updatePrimaryWallet(s_WalletName);
                        else
                            renameWallet(s_WalletName);
                    } else {
                        CommonUtilities.ShowToastMessage(EditWalletActivity.this, getResources().getString(R.string.internetconnection));
                    }
                }
            }
        });


    }

    private void renameWallet(String s_walletName) {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            progressDialog = ProgressDialog.show(EditWalletActivity.this, "", getResources().getString(R.string.please_wait), true);
            WalletControllerApi apiService = DeviantXApiClient.getClient().create(WalletControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.renameWallet(CONSTANTS.DeviantMulti + token, walletList.getStr_data_name(), s_walletName, scompat_defWallet.isChecked());
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
                                if (scompat_defWallet.isChecked()) {
                                    editor.putString(CONSTANTS.defaultWalletName, s_walletName);
//                                    editor.putInt(CONSTANTS.defaultWallet, walletList.getInt_data_id());
                                    editor.apply();
                                }
                                Intent serviceIntent = new Intent(getApplicationContext(), WalletDataFetch.class);
                                serviceIntent.putExtra("walletList", true);
                                serviceIntent.putExtra("walletIsDefault", scompat_defWallet.isChecked());
                                startService(serviceIntent);
                                finish();
                            } else {
                                CommonUtilities.ShowToastMessage(EditWalletActivity.this, loginResponseMsg);
                            }

                        } else {
                            CommonUtilities.ShowToastMessage(EditWalletActivity.this, loginResponseMsg);
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(EditWalletActivity.this, getResources().getString(R.string.errortxt));
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(EditWalletActivity.this, getResources().getString(R.string.Timeout));
                    } else if (t instanceof java.net.ConnectException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(EditWalletActivity.this, getResources().getString(R.string.networkerror));
                    } else {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(EditWalletActivity.this, getResources().getString(R.string.errortxt));
                    }
                }
            });
        } catch (Exception ex) {
            progressDialog.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(EditWalletActivity.this, getResources().getString(R.string.errortxt));
        }

    }

    private void updatePrimaryWallet(String s_walletName) {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            progressDialog = ProgressDialog.show(EditWalletActivity.this, "", getResources().getString(R.string.please_wait), true);
            WalletControllerApi apiService = DeviantXApiClient.getClient().create(WalletControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.updatePrimaryWallet(CONSTANTS.DeviantMulti + token, walletList.getInt_data_id(), scompat_defWallet.isChecked());
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
                                if (scompat_defWallet.isChecked()) {
                                    editor.putString(CONSTANTS.defaultWalletName, s_walletName);
//                                    editor.putInt(CONSTANTS.defaultWallet, walletList.getInt_data_id());
                                    editor.apply();
                                }
                                Intent serviceIntent = new Intent(getApplicationContext(), WalletDataFetch.class);
                                serviceIntent.putExtra("walletList", true);
                                serviceIntent.putExtra("walletIsDefault", scompat_defWallet.isChecked());
                                startService(serviceIntent);
                                finish();
                            } else {
                                CommonUtilities.ShowToastMessage(EditWalletActivity.this, loginResponseMsg);
                            }

                        } else {
                            CommonUtilities.ShowToastMessage(EditWalletActivity.this, loginResponseMsg);
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(EditWalletActivity.this, getResources().getString(R.string.errortxt));
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(EditWalletActivity.this, getResources().getString(R.string.Timeout));
                    } else if (t instanceof java.net.ConnectException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(EditWalletActivity.this, getResources().getString(R.string.networkerror));
                    } else {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(EditWalletActivity.this, getResources().getString(R.string.errortxt));
                    }
                }
            });
        } catch (Exception ex) {
            progressDialog.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(EditWalletActivity.this, getResources().getString(R.string.errortxt));
        }

    }
}
