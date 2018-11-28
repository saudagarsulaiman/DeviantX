package com.cryptowallet.deviantx.UI.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.ServiceAPIs.WalletControllerApi;
import com.cryptowallet.deviantx.UI.Adapters.SpinnerDaysAdapter;
import com.cryptowallet.deviantx.UI.Adapters.WalletListRAdapter;
import com.cryptowallet.deviantx.UI.Interfaces.FavListener;
import com.cryptowallet.deviantx.UI.Models.AccountWallet;
import com.cryptowallet.deviantx.UI.Models.WalletList;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;
import com.cryptowallet.deviantx.Utilities.DeviantXApiClient;

import org.json.JSONArray;
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

public class WithdrawFundsAirdropActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    //    @BindView(R.id.)
//    ;
    @BindView(R.id.toolbar_center_back)
    Toolbar toolbar_center_back;
    @BindView(R.id.txt_avail_bal)
    TextView txt_avail_bal;
    @BindView(R.id.edt_wallet_address)
    EditText edt_wallet_address;
    @BindView(R.id.spnr_wallets)
    Spinner spnr_wallets;
    @BindView(R.id.cbox_wallet)
    CheckBox cbox_wallet;
    @BindView(R.id.edt_amount)
    EditText edt_amount;
    @BindView(R.id.txt_all)
    TextView txt_all;
    @BindView(R.id.btn_withdraw)
    Button btn_withdraw;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;

    ArrayList<WalletList> walletList;
    private String loginResponseData, loginResponseStatus, loginResponseMsg, str_data_name;
    private int int_data_walletid;
    private double dbl_data_totalBal;
    private boolean defaultWallet = false;

    @Override
    protected void onResume() {
        super.onResume();
        myApplication.disableScreenCapture(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_funds_airdrop);

        ButterKnife.bind(this);
        walletList = new ArrayList<>();

        sharedPreferences = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        toolbar_center_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        if (cbox_wallet.isChecked()) {
            spnr_wallets.setVisibility(View.VISIBLE);
            edt_wallet_address.setVisibility(View.GONE);
        } else {
            spnr_wallets.setVisibility(View.GONE);
            edt_wallet_address.setVisibility(View.VISIBLE);
        }

        cbox_wallet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    spnr_wallets.setVisibility(View.VISIBLE);
                    edt_wallet_address.setVisibility(View.GONE);
                } else {
                    spnr_wallets.setVisibility(View.GONE);
                    edt_wallet_address.setVisibility(View.VISIBLE);
                }
            }
        });

        spnr_wallets.setOnItemSelectedListener(this);
        if (CommonUtilities.isConnectionAvailable(WithdrawFundsAirdropActivity.this)) {
            getAllWallets();
        } else {
            CommonUtilities.ShowToastMessage(WithdrawFundsAirdropActivity.this, getResources().getString(R.string.internetconnection));
        }
//        spnr_wallets.setAdapter(new SpinnerDaysAdapter(WithdrawFundsAirdropActivity.this, R.layout.spinner_item_days_dropdown, categories));

        btn_withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = new Intent(WithdrawFundsAirdropActivity.this, TwoFAAirDropActivity.class);
                startActivity(intent);*/
               checkInputs();
            }
        });


    }

    private void checkInputs() {

    }

    private void getAllWallets() {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            progressDialog = ProgressDialog.show(WithdrawFundsAirdropActivity.this, "", getResources().getString(R.string.please_wait), true);
            WalletControllerApi apiService = DeviantXApiClient.getClient().create(WalletControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.getAllWallet(CONSTANTS.DeviantMulti + token);
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
                                JSONArray jsonArrayData = new JSONArray(loginResponseData);
                                for (int i = 0; i < jsonArrayData.length(); i++) {
                                    JSONObject jsonObjectData = jsonArrayData.getJSONObject(i);
                                    try {
                                        int_data_walletid = jsonObjectData.getInt("id");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        str_data_name = jsonObjectData.getString("name");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        dbl_data_totalBal = jsonObjectData.getDouble("toatalBalance");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        defaultWallet = jsonObjectData.getBoolean("defaultWallet");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    walletList.add(new WalletList(int_data_walletid, str_data_name, dbl_data_totalBal, defaultWallet));
                                }
                                ArrayList<String> walletNamesList = new ArrayList<>();
                                for (int i = 0; i < walletList.size(); i++) {
                                    walletNamesList.add(walletList.get(i).getStr_data_name());
                                }
                                spnr_wallets.setAdapter(new SpinnerDaysAdapter(WithdrawFundsAirdropActivity.this, R.layout.spinner_item_days_dropdown, walletNamesList));

                            } else {
                                CommonUtilities.ShowToastMessage(WithdrawFundsAirdropActivity.this, loginResponseMsg);
                            }

                        } else {
                            CommonUtilities.ShowToastMessage(WithdrawFundsAirdropActivity.this, loginResponseMsg);
//                            Toast.makeText(getApplicationContext(), responsevalue, Toast.LENGTH_LONG).show();
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(WithdrawFundsAirdropActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(WithdrawFundsAirdropActivity.this, getResources().getString(R.string.Timeout));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Timeout), Toast.LENGTH_SHORT).show();
                    } else if (t instanceof java.net.ConnectException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(WithdrawFundsAirdropActivity.this, getResources().getString(R.string.networkerror));
                        Toast.makeText(WithdrawFundsAirdropActivity.this, getResources().getString(R.string.networkerror), Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(WithdrawFundsAirdropActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception ex) {
            progressDialog.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(WithdrawFundsAirdropActivity.this, getResources().getString(R.string.errortxt));
//            Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String walletName = parent.getItemAtPosition(position).toString();
        transferAmount(walletName);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void transferAmount(String walletName) {

    }

}
