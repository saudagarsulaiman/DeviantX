package com.cryptowallet.deviantx.UI.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.ServiceAPIs.UserAirdropControllerApi;
import com.cryptowallet.deviantx.ServiceAPIs.WalletControllerApi;
import com.cryptowallet.deviantx.UI.Adapters.SpinnerDaysAdapter;
import com.cryptowallet.deviantx.UI.Adapters.WalletListAirdropRAdapter;
import com.cryptowallet.deviantx.UI.Interfaces.WalletSelectableListener;
import com.cryptowallet.deviantx.UI.Models.AccountWallet;
import com.cryptowallet.deviantx.UI.Models.DividendAirdrops;
import com.cryptowallet.deviantx.UI.Models.WalletList;
import com.cryptowallet.deviantx.UI.RoomDatabase.Database.DeviantXDB;
import com.cryptowallet.deviantx.UI.Services.DividendAirdropsFetch;
import com.cryptowallet.deviantx.UI.Services.FeaturedAirdropsFetch;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;
import com.cryptowallet.deviantx.Utilities.DeviantXApiClient;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.yarolegovich.discretescrollview.DSVOrientation;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import org.json.JSONArray;
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

public class WithdrawADClaimActivity extends AppCompatActivity implements DiscreteScrollView.OnItemChangedListener<RecyclerView.ViewHolder> {


    @BindView(R.id.toolbar_center_back)
    Toolbar toolbar_center_back;
    @BindView(R.id.txt_coin_code)
    TextView txt_coin_code;
    @BindView(R.id.edt_amount)
    EditText edt_amount;
    @BindView(R.id.txt_avail_coins)
    TextView txt_avail_coins;
    @BindView(R.id.txt_avail_coinCode)
    TextView txt_avail_coinCode;
    @BindView(R.id.btn_withdraw)
    Button btn_withdraw;
    @BindView(R.id.item_picker)
    DiscreteScrollView itemPicker;
    @BindView(R.id.lnr_minus)
    LinearLayout lnr_minus;
    @BindView(R.id.lnr_plus)
    LinearLayout lnr_plus;
    @BindView(R.id.pb)
    ProgressBar pb;


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;

    WalletListAirdropRAdapter walletListAirdropRAdapter;
    ArrayList<WalletList> walletList;
    private String loginResponseData, loginResponseStatus, loginResponseMsg, str_data_name, regResponseMsg, regResponseStatus, regResponsedata;
    private int int_data_walletid;
    private double dbl_data_totalBal;
    private boolean defaultWallet = false;
    WalletSelectableListener walletSelectableListener;
    int selectedCoinId = 0;
    String selectedWalletName = "";
    Double avail_bal = 0.0, selectedWalletBal = 0.0;
    DeviantXDB db;
    ArrayList<AccountWallet> accountWalletlist;
    SpinnerDaysAdapter spinnerAdapter;

    String coinName;
    String walletName;
    DividendAirdrops dividendAirdrops;


    @Override
    protected void onResume() {
        super.onResume();
        myApplication.disableScreenCapture(this);
//        CommonUtilities.serviceStart(WithdrawADClaimActivity.this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_adclaim);

        ButterKnife.bind(this);
        db = DeviantXDB.getDatabase(WithdrawADClaimActivity.this);
        sharedPreferences = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        walletList = new ArrayList<>();
        accountWalletlist = new ArrayList<>();
        editor.putString(CONSTANTS.walletName, "");
        editor.putInt(CONSTANTS.walletId, 0);
        editor.apply();

        Bundle bundle = getIntent().getExtras();
        dividendAirdrops = bundle.getParcelable(CONSTANTS.claimCoin);
        txt_avail_coins.setText(String.format("%.4f", dividendAirdrops.getDbl_airdropAmount()));
        txt_avail_coinCode.setText(dividendAirdrops.getStr_coinCode());
        txt_coin_code.setText(dividendAirdrops.getStr_coinCode());


        toolbar_center_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        walletSelectableListener = new WalletSelectableListener() {
            @Override
            public void WalletSelected(ArrayList<WalletList> selected_allWalletList, int pos) {

                int i = 0;
                //  final WalletListDB selectedWallet = new WalletListDB();
                for (WalletList wallets : selected_allWalletList) {
                    if (wallets.getSelected()) {
                        wallets.setSelected(false);
                        walletListAirdropRAdapter.notifyItemChanged(i);
                    }
                    i++;
                }

                walletListAirdropRAdapter.setWalletValue(!selected_allWalletList.get(pos).getSelected(), pos);

                if (selected_allWalletList.get(pos).getSelected()) {
                    selectedCoinId = selected_allWalletList.get(pos).getInt_data_id();
                    selectedWalletName = selected_allWalletList.get(pos).getStr_data_name();
                    selectedWalletBal = selected_allWalletList.get(pos).getDbl_data_totalBal();
                    btn_withdraw.setVisibility(View.VISIBLE);
                } else
                    btn_withdraw.setVisibility(View.GONE);
            }
        };

        walletListAirdropRAdapter = new WalletListAirdropRAdapter(WithdrawADClaimActivity.this, walletList, walletSelectableListener, true);
        itemPicker.setAdapter(walletListAirdropRAdapter);

        if (CommonUtilities.isConnectionAvailable(WithdrawADClaimActivity.this)) {
            getAllWallets();
        } else {
            CommonUtilities.ShowToastMessage(WithdrawADClaimActivity.this, getResources().getString(R.string.internetconnection));
        }

        itemPicker.setOrientation(DSVOrientation.HORIZONTAL);
        itemPicker.addOnItemChangedListener(this);
        itemPicker.setAdapter(walletListAirdropRAdapter);
        itemPicker.setItemTransitionTimeMillis(150);
        itemPicker.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build());


        lnr_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String edtValue = edt_amount.getText().toString().trim();
                Double edtVal = Double.parseDouble(edtValue);
                Double txtVal = Double.parseDouble(txt_avail_coins.getText().toString().trim());
                edtVal++;
                if (edtVal > txtVal) {
                    CommonUtilities.ShowToastMessage(WithdrawADClaimActivity.this, getResources().getString(R.string.insufficient_fund));
                } else {
                    edt_amount.setText("" + edtVal);
                }
            }
        });
        lnr_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String edt_value = edt_amount.getText().toString().trim();
                Double edtVal = Double.parseDouble(edt_value);
                edtVal--;
                if (edtVal < 0) {
                    edt_amount.setText("0");
                } else {
                    edt_amount.setText("" + edtVal);
                }

            }
        });


        btn_withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double fee = 0.01;

                String amount = edt_amount.getText().toString().trim();
                if (!amount.isEmpty()) {
                    float amt = Float.parseFloat(amount);
                    if (amt > 0) {
                        if (Double.parseDouble(amount)/* + fee */ <= dividendAirdrops.getDbl_airdropAmount()) {
                            /* if (myApplication.get2FA()) {
                             *//*
                                        Intent intent = new Intent(WithdrawADClaimActivity.this, TwoFAAirDropActivity.class);
                                        Bundle bundle1 = new Bundle();
                                        bundle1.putString(CONSTANTS.walletName, "");
                                        bundle1.putString(CONSTANTS.address, to_address);
                                        bundle1.putString(CONSTANTS.amount, amount);
                                        bundle1.putParcelable(CONSTANTS.selectedAccountWallet, dividendAirdrops);
                                        intent.putExtras(bundle1);
                                        startActivity(intent);
*//*
                            } else {*/
                            customDialog(/*selectedWalletName*/walletName, dividendAirdrops.getStr_coinCode(), amount);
//                            }
                        } else {
                            CommonUtilities.ShowToastMessage(WithdrawADClaimActivity.this, getResources().getString(R.string.maintain_bal));
                        }
                    } else {
                        CommonUtilities.ShowToastMessage(WithdrawADClaimActivity.this, getResources().getString(R.string.enter_amount));
                    }
                } else {
                    CommonUtilities.ShowToastMessage(WithdrawADClaimActivity.this, getResources().getString(R.string.empty_amount));
                }


            }
        });


        edt_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String amountTextValue = s.toString();
                if (!amountTextValue.trim().isEmpty()) {
                    try {
                        if (Double.parseDouble(amountTextValue) != 0) {
                            Double finalValue = Double.parseDouble(amountTextValue);
                            avail_bal = dividendAirdrops.getDbl_airdropAmount();
                            if (avail_bal < finalValue) {
                                CommonUtilities.ShowToastMessage(WithdrawADClaimActivity.this, getResources().getString(R.string.insufficient_fund));
                                edt_amount.setText("0");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    CommonUtilities.ShowToastMessage(WithdrawADClaimActivity.this, getResources().getString(R.string.enter_amount));
                }
            }
        });


/*
        btn_withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coinName = sharedPreferences.getString(CONSTANTS.selectedCoinName, null);
                String walletName = walletList.get(myApplication.getDefaultWallet()).getStr_data_name();
                String amount = edt_amount.getText().toString().trim();
                customDialog(walletName, coinName, amount);
            }
        });
*/
        /*edt_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String amountTextValue = s.toString();
                Double txtVal = Double.parseDouble(txt_avail_coins.getText().toString().trim());
                if (!amountTextValue.trim().isEmpty()) {
                    try {
                        if (Double.parseDouble(amountTextValue) != 0) {
                            Double finalValue = Double.parseDouble(amountTextValue);
                            avail_bal = txtVal - 0.01;
                            if (avail_bal < finalValue) {
                                CommonUtilities.ShowToastMessage(WithdrawADClaimActivity.this, getResources().getString(R.string.insufficient_fund));
                                edt_amount.setText("0");
                                btn_withdraw.setVisibility(View.GONE);
                            } else {
                                btn_withdraw.setVisibility(View.VISIBLE);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    CommonUtilities.ShowToastMessage(WithdrawADClaimActivity.this, getResources().getString(R.string.enter_amount));
                }
            }
        });
*/
    }

    private void customDialog(String walletname, String coincode, String amt) {
        //                Creating A Custom Dialog Using DialogPlus
        ViewHolder viewHolder = new ViewHolder(R.layout.dialog_withdraw_confirm);
        final DialogPlus dialog = DialogPlus.newDialog(WithdrawADClaimActivity.this)
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
        TextView txt_fee_lbl = view.findViewById(R.id.txt_fee_lbl);
        TextView txt_fee_amt_code = view.findViewById(R.id.txt_fee_amt_code);
        TextView txt_address = view.findViewById(R.id.txt_address);
        TextView txt_privacy_policy = view.findViewById(R.id.txt_privacy_policy);

        txt_withdraw_amt.setText(amt);
        txt_withdraw_amt_code.setText(coincode);
//        txt_fee_amt.setText(String.format("%.4f",));
        txt_fee_amt.setText("0.01");
        txt_fee_amt_code.setText(coincode);
        txt_address.setText(walletname);
        txt_fee_lbl.setVisibility(View.GONE);
        txt_fee_amt.setVisibility(View.GONE);
        txt_fee_amt_code.setVisibility(View.GONE);
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
                txt_avail_coins.setText(String.format("%.4f", dividendAirdrops.getDbl_airdropAmount()));
                dialog.dismiss();
            }
        });
        txt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                claimAirdrop(walletname, amt, coincode);
                dialog.dismiss();
            }
        });

//        txt_avail_coins.setText(String.format("%.4f", avail_bal - Double.parseDouble(amt)));
        dialog.show();

    }

    private void claimAirdrop(String walName, String amnt, String ccode) {
        try {
            JSONObject params = new JSONObject();
            String token = sharedPreferences.getString(CONSTANTS.token, "");
            try {
                params.put("amount", amnt);
                params.put("coinCode", ccode);
                params.put("walletName", walName);
                params.put("id", dividendAirdrops.getInt_id());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialog = ProgressDialog.show(WithdrawADClaimActivity.this, "", getResources().getString(R.string.please_wait), true);
            UserAirdropControllerApi apiService = DeviantXApiClient.getClient().create(UserAirdropControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.postClaimADAmount(params.toString(), "DEVIANTMULTI " + token);
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
                                CommonUtilities.serviceStart(WithdrawADClaimActivity.this);
                                regResponsedata = jsonObject.getString("data");

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    startForegroundService(new Intent(WithdrawADClaimActivity.this, FeaturedAirdropsFetch.class));
                                    startForegroundService(new Intent(WithdrawADClaimActivity.this, DividendAirdropsFetch.class));
                                } else {
                                    startService(new Intent(WithdrawADClaimActivity.this, FeaturedAirdropsFetch.class));
                                    startService(new Intent(WithdrawADClaimActivity.this, DividendAirdropsFetch.class));
                                }


                                Intent intent = new Intent(WithdrawADClaimActivity.this, DashBoardActivity.class);
                                intent.putExtra(CONSTANTS.seletedTab, 2);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                CommonUtilities.ShowToastMessage(WithdrawADClaimActivity.this, regResponseMsg);
                            } else {
                                CommonUtilities.ShowToastMessage(WithdrawADClaimActivity.this, regResponseMsg);
                            }
                        } else {
                            CommonUtilities.ShowToastMessage(WithdrawADClaimActivity.this, regResponseMsg);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(WithdrawADClaimActivity.this, getResources().getString(R.string.errortxt));
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(WithdrawADClaimActivity.this, getResources().getString(R.string.Timeout));
                    } else if (t instanceof java.net.ConnectException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(WithdrawADClaimActivity.this, getResources().getString(R.string.networkerror));
                    } else {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(WithdrawADClaimActivity.this, getResources().getString(R.string.errortxt));
                    }
                }
            });
        } catch (Exception ex) {
            progressDialog.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(WithdrawADClaimActivity.this, getResources().getString(R.string.errortxt));
        }

    }

    private void getAllWallets() {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            progressDialog = ProgressDialog.show(WithdrawADClaimActivity.this, "", getResources().getString(R.string.please_wait), true);
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
//                                int defaultWalletPos = 0;
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
                                        if (defaultWallet) {
//                                            defaultWalletPos = i;
                                            editor.putInt(CONSTANTS.defaultWallet, i);
                                            editor.apply();
                                            myApplication.setDefaultWallet(i);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    walletList.add(new WalletList(int_data_walletid, str_data_name, dbl_data_totalBal, defaultWallet));
                                }
                                walletListAirdropRAdapter = new WalletListAirdropRAdapter(WithdrawADClaimActivity.this, walletList, walletSelectableListener, false);
                                itemPicker.setAdapter(walletListAirdropRAdapter);
//                                itemPicker.scrollToPosition(defaultWalletPos);
                                itemPicker.scrollToPosition(myApplication.getDefaultWallet());
                                ArrayList<String> walletNamesList = new ArrayList<>();
                                for (int i = 0; i < walletList.size(); i++) {
                                    walletNamesList.add(walletList.get(i).getStr_data_name());
                                }
                            } else {
                                CommonUtilities.ShowToastMessage(WithdrawADClaimActivity.this, loginResponseMsg);
                            }

                        } else {
                            CommonUtilities.ShowToastMessage(WithdrawADClaimActivity.this, loginResponseMsg);
//                            Toast.makeText(getApplicationContext(), responsevalue, Toast.LENGTH_LONG).show();
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(WithdrawADClaimActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(WithdrawADClaimActivity.this, getResources().getString(R.string.Timeout));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Timeout), Toast.LENGTH_SHORT).show();
                    } else if (t instanceof java.net.ConnectException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(WithdrawADClaimActivity.this, getResources().getString(R.string.networkerror));
                        Toast.makeText(WithdrawADClaimActivity.this, getResources().getString(R.string.networkerror), Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(WithdrawADClaimActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception ex) {
            progressDialog.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(WithdrawADClaimActivity.this, getResources().getString(R.string.errortxt));
        }

    }


    @Override
    public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {
        if (adapterPosition > -1) {
            walletName = walletList.get(adapterPosition).getStr_data_name();
        }
    }


}
