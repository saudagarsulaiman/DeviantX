package com.cryptowallet.deviantx.UI.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.ServiceAPIs.AirdropWalletControllerApi;
import com.cryptowallet.deviantx.ServiceAPIs.CryptoControllerApi;
import com.cryptowallet.deviantx.ServiceAPIs.UserAirdropControllerApi;
import com.cryptowallet.deviantx.ServiceAPIs.WalletControllerApi;
import com.cryptowallet.deviantx.UI.Adapters.SpinnerDaysAdapter;
import com.cryptowallet.deviantx.UI.Adapters.WalletListAirdropRAdapter;
import com.cryptowallet.deviantx.UI.Interfaces.WalletSelectableListener;
import com.cryptowallet.deviantx.UI.Models.AccountWallet;
import com.cryptowallet.deviantx.UI.Models.WalletList;
import com.cryptowallet.deviantx.UI.RoomDatabase.Database.DeviantXDB;
import com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB.AllCoinsDao;
import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.AllCoinsDB;
import com.cryptowallet.deviantx.UI.Services.FeaturedAirdropsFetch;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;
import com.cryptowallet.deviantx.Utilities.DeviantXApiClient;
import com.cryptowallet.deviantx.Utilities.GsonUtils;
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
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cryptowallet.deviantx.Utilities.MyApplication.myApplication;

public class CreateADCampaignsActivity extends AppCompatActivity implements DiscreteScrollView.OnItemChangedListener<RecyclerView.ViewHolder>, AdapterView.OnItemSelectedListener {

    @BindView(R.id.toolbar_center_back)
    Toolbar toolbar_center_back;
    @BindView(R.id.txt_coin_code)
    TextView txt_coin_code;
    @BindView(R.id.edt_amount)
    EditText edt_amount;
    @BindView(R.id.spnr_sel_coin)
    Spinner spnr_sel_coin;
    @BindView(R.id.txt_avail_coins)
    TextView txt_avail_coins;
    @BindView(R.id.txt_avail_coinCode)
    TextView txt_avail_coinCode;
    @BindView(R.id.btn_continue)
    Button btn_continue;
    @BindView(R.id.item_picker)
    DiscreteScrollView itemPicker;
    @BindView(R.id.lnr_minus)
    LinearLayout lnr_minus;
    @BindView(R.id.lnr_plus)
    LinearLayout lnr_plus;
    @BindView(R.id.pb)
    ProgressBar pb;
    @BindView(R.id.lnr_empty_coins)
    LinearLayout lnr_empty_coins;

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
    ArrayList<String> walletCoinsList;
    SpinnerDaysAdapter spinnerAdapter;

    String coinName;

    @Override
    protected void onResume() {
        super.onResume();
        myApplication.disableScreenCapture(this);
//        CommonUtilities.serviceStart(CreateADCampaignsActivity.this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ad_campaigns);
        ButterKnife.bind(this);
        db = DeviantXDB.getDatabase(CreateADCampaignsActivity.this);
        sharedPreferences = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        walletList = new ArrayList<>();
//        airdropWalletlist = new ArrayList<>();
        accountWalletlist = new ArrayList<>();
        walletCoinsList = new ArrayList<>();
        editor.putString(CONSTANTS.walletName, "");
        editor.putInt(CONSTANTS.walletId, 0);
        editor.apply();

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
                try {
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
//                    btn_withdraw.setVisibility(View.VISIBLE);
                    } /*else*/
//                    btn_withdraw.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        walletListAirdropRAdapter = new WalletListAirdropRAdapter(CreateADCampaignsActivity.this, walletList, walletSelectableListener, false);
        itemPicker.setAdapter(walletListAirdropRAdapter);
/*
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                onLoadWallet();
            }
        }, 100);
*/
        if (CommonUtilities.isConnectionAvailable(CreateADCampaignsActivity.this)) {
            getAllWallets();
        } else {
            CommonUtilities.ShowToastMessage(CreateADCampaignsActivity.this, getResources().getString(R.string.internetconnection));
        }

        itemPicker.setOrientation(DSVOrientation.HORIZONTAL);
        itemPicker.addOnItemChangedListener(this);
        itemPicker.setAdapter(walletListAirdropRAdapter);
        itemPicker.setItemTransitionTimeMillis(150);
        itemPicker.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build());

        spnr_sel_coin.setOnItemSelectedListener(this);
        txt_avail_coins.setText("0");
        txt_avail_coinCode.setText(" ");


        lnr_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String edtValue = edt_amount.getText().toString().trim();
                Double edtVal = Double.parseDouble(edtValue);
                Double txtVal = Double.parseDouble(txt_avail_coins.getText().toString().trim());
                edtVal++;
                if (edtVal > txtVal) {
                    CommonUtilities.ShowToastMessage(CreateADCampaignsActivity.this, getResources().getString(R.string.insufficient_fund));
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
                Double txtVal = Double.parseDouble(txt_avail_coins.getText().toString().trim());
                if (!amountTextValue.trim().isEmpty()) {
                    try {
                        if (Double.parseDouble(amountTextValue) != 0) {
                            Double finalValue = Double.parseDouble(amountTextValue);
                            avail_bal = txtVal - 0.01;
                            if (avail_bal < finalValue) {
                                CommonUtilities.ShowToastMessage(CreateADCampaignsActivity.this, getResources().getString(R.string.insufficient_fund));
                                edt_amount.setText("0");
                                btn_continue.setVisibility(View.GONE);
                            } else {
                                btn_continue.setVisibility(View.VISIBLE);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    CommonUtilities.ShowToastMessage(CreateADCampaignsActivity.this, getResources().getString(R.string.enter_amount));
                }
            }
        });

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coinName = sharedPreferences.getString(CONSTANTS.selectedCoinName, null);
                String walletName = walletList.get(myApplication.getDefaultWallet()).getStr_data_name();
                String amount = edt_amount.getText().toString().trim();
                customDialog(walletName, coinName, amount);

/*
                CommonUtilities.serviceStart(CreateADCampaignsActivity.this);
                Intent intent = new Intent(CreateADCampaignsActivity.this, DashBoardActivity.class);
                intent.putExtra(CONSTANTS.seletedTab, 2);
                startActivity(intent);
*/
            }
        });

    }

    private void customDialog(String walletName, String coinName, String amount) {
        //                Creating A Custom Dialog Using DialogPlus
        ViewHolder viewHolder = new ViewHolder(R.layout.dialog_airdrop_campaign);
        final DialogPlus dialog = DialogPlus.newDialog(CreateADCampaignsActivity.this)
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
        TextView txt_wallet_name = view.findViewById(R.id.txt_wallet_name);
        TextView txt_coin_name = view.findViewById(R.id.txt_coin_name);
        TextView txt_amount = view.findViewById(R.id.txt_amount);

        txt_wallet_name.setText(walletName);
        txt_coin_name.setText(coinName);
        txt_amount.setText(amount);

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        txt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createAirdropCampaign(walletName, amount);

                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void createAirdropCampaign(String walletName, String amount) {
        try {
            JSONObject params = new JSONObject();
            String token = sharedPreferences.getString(CONSTANTS.token, "");
            String coinCode = sharedPreferences.getString(CONSTANTS.selectedCoinCode, "");
            try {
                params.put("amount", amount);
                params.put("coinCode", coinCode);
                params.put("walletName", walletName);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialog = ProgressDialog.show(CreateADCampaignsActivity.this, "", getResources().getString(R.string.please_wait), true);
            UserAirdropControllerApi apiService = DeviantXApiClient.getClient().create(UserAirdropControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.createNewAD(params.toString(), "DEVIANTMULTI " + token);
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
                                CommonUtilities.serviceStart(CreateADCampaignsActivity.this);
                                regResponsedata = jsonObject.getString("data");
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    startForegroundService(new Intent(CreateADCampaignsActivity.this, FeaturedAirdropsFetch.class));
                                } else {
                                    startService(new Intent(CreateADCampaignsActivity.this, FeaturedAirdropsFetch.class));
                                }
                                Intent intent = new Intent(CreateADCampaignsActivity.this, DashBoardActivity.class);
                                intent.putExtra(CONSTANTS.seletedTab, 2);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                CommonUtilities.ShowToastMessage(CreateADCampaignsActivity.this, regResponseMsg);
                            } else {
                                CommonUtilities.ShowToastMessage(CreateADCampaignsActivity.this, regResponseMsg);
                            }
                        } else {
                            CommonUtilities.ShowToastMessage(CreateADCampaignsActivity.this, regResponseMsg);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(CreateADCampaignsActivity.this, getResources().getString(R.string.errortxt));
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(CreateADCampaignsActivity.this, getResources().getString(R.string.Timeout));
                    } else if (t instanceof java.net.ConnectException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(CreateADCampaignsActivity.this, getResources().getString(R.string.networkerror));
                    } else {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(CreateADCampaignsActivity.this, getResources().getString(R.string.errortxt));
                    }
                }
            });
        } catch (Exception ex) {
            progressDialog.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(CreateADCampaignsActivity.this, getResources().getString(R.string.errortxt));
        }

    }

    private void getAllWallets() {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            progressDialog = ProgressDialog.show(CreateADCampaignsActivity.this, "", getResources().getString(R.string.please_wait), true);
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
                                walletListAirdropRAdapter = new WalletListAirdropRAdapter(CreateADCampaignsActivity.this, walletList, walletSelectableListener, false);
                                itemPicker.setAdapter(walletListAirdropRAdapter);
//                                itemPicker.scrollToPosition(defaultWalletPos);
                                itemPicker.scrollToPosition(myApplication.getDefaultWallet());
                                ArrayList<String> walletNamesList = new ArrayList<>();
                                for (int i = 0; i < walletList.size(); i++) {
                                    walletNamesList.add(walletList.get(i).getStr_data_name());
                                }
                            } else {
                                CommonUtilities.ShowToastMessage(CreateADCampaignsActivity.this, loginResponseMsg);
                            }

                        } else {
                            CommonUtilities.ShowToastMessage(CreateADCampaignsActivity.this, loginResponseMsg);
//                            Toast.makeText(getApplicationContext(), responsevalue, Toast.LENGTH_LONG).show();
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(CreateADCampaignsActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(CreateADCampaignsActivity.this, getResources().getString(R.string.Timeout));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Timeout), Toast.LENGTH_SHORT).show();
                    } else if (t instanceof java.net.ConnectException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(CreateADCampaignsActivity.this, getResources().getString(R.string.networkerror));
                        Toast.makeText(CreateADCampaignsActivity.this, getResources().getString(R.string.networkerror), Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(CreateADCampaignsActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception ex) {
            progressDialog.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(CreateADCampaignsActivity.this, getResources().getString(R.string.errortxt));
        }

    }

/*
    private void onLoadWallet() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AccountWalletDao accountWalletDao = db.accountWalletDao();
                if ((accountWalletDao.getAllAccountWallet()).size() > 0) {
                    String walletResult = (accountWalletDao.getAllAccountWallet()).get(0).walletDatas;
                    walletUpdate(walletResult);
                } else {
                    if (CommonUtilities.isConnectionAvailable(CreateADCampaignsActivity.this)) {
                        invokeWallet();
                    } else {
                        CommonUtilities.ShowToastMessage(CreateADCampaignsActivity.this, getResources().getString(R.string.internetconnection));
                    }
                }
            }
        });
    }
*/

/*
    private void walletUpdate(String responsevalue) {
        try {
            if (!responsevalue.isEmpty() && responsevalue != null) {

                JSONObject jsonObject = new JSONObject(responsevalue);
                loginResponseMsg = jsonObject.getString("msg");
                loginResponseStatus = jsonObject.getString("status");

                if (loginResponseStatus.equals("true")) {
                    loginResponseData = jsonObject.getString("data");

                    WalletList[] coinsStringArray = GsonUtils.getInstance().fromJson(loginResponseData, WalletList[].class);
                    walletList = new ArrayList<WalletList>(Arrays.asList(coinsStringArray));

                    for (int i = 0; i < walletList.size(); i++) {
                        if (walletList.get(i).isDefaultWallet()) {
                            editor.putInt(CONSTANTS.defaultWallet, i);
                            editor.apply();
                            myApplication.setDefaultWallet(i);
                        }
                    }
                    walletListAirdropRAdapter.setAllWallets(walletList);
                    itemPicker.scrollToPosition(myApplication.getDefaultWallet());

                } else {
                    CommonUtilities.ShowToastMessage(CreateADCampaignsActivity.this, loginResponseMsg);
                }

            } else {
                CommonUtilities.ShowToastMessage(CreateADCampaignsActivity.this, loginResponseMsg);
//                            Toast.makeText(getApplicationContext(), responsevalue, Toast.LENGTH_LONG).show();
                Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
            }

        } catch (Exception e) {
            e.printStackTrace();
            CommonUtilities.ShowToastMessage(CreateADCampaignsActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
        }
    }
*/

/*
    private void invokeWallet() {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            WalletControllerApi apiService = DeviantXApiClient.getClient().create(WalletControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.getAllWallet(CONSTANTS.DeviantMulti + token);
            apiResponse.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responsevalue = response.body().string();
                        walletUpdate(responsevalue);
                        AccountWalletDao accountWalletDao = db.accountWalletDao();
                        accountWalletDao.deleteAllAccountWallet();
                        AccountWalletDB accountWalletDB = new AccountWalletDB(responsevalue);
                        accountWalletDao.insertAccountWallet(accountWalletDB);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        CommonUtilities.ShowToastMessage(CreateADCampaignsActivity.this, getResources().getString(R.string.Timeout));
                    } else if (t instanceof java.net.ConnectException) {
                        CommonUtilities.ShowToastMessage(CreateADCampaignsActivity.this, getResources().getString(R.string.networkerror));
                    } else {
                        CommonUtilities.ShowToastMessage(CreateADCampaignsActivity.this, getResources().getString(R.string.errortxt));
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(CreateADCampaignsActivity.this, getResources().getString(R.string.errortxt));
        }
    }
*/

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        coinName = parent.getItemAtPosition(position).toString();
        editor.putString(CONSTANTS.selectedCoinName, coinName);
        editor.apply();
        getAvailBal(coinName, accountWalletlist);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {
        if (adapterPosition > -1) {
            String wallet_name = sharedPreferences.getString(CONSTANTS.walletName, "sss");
            if (!wallet_name.equals(walletList.get(adapterPosition).getStr_data_name())) {
                onItemChanged(walletList.get(adapterPosition));
//                accountWalletlist = new ArrayList<>();
                walletCoinsList = new ArrayList<>();
                spinnerAdapter = new SpinnerDaysAdapter(CreateADCampaignsActivity.this, R.layout.spinner_item_days_dropdown, walletCoinsList);
                spnr_sel_coin.setAdapter(spinnerAdapter);
            }
        }
    }

    private void onItemChanged(WalletList walletList) {
//            GET Account Wallet
        editor.putString(CONSTANTS.walletName, walletList.getStr_data_name());
        editor.putInt(CONSTANTS.walletId, walletList.getInt_data_id());
        editor.apply();
        fetchAccountWallet(walletList.getStr_data_name(), walletList.getInt_data_id());
    }

    private void fetchAccountWallet(String wallet_name, int walletId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onProgressLoad();
                try {
                    AllCoinsDao allCoinsDao = db.allCoinsDao();
                    if ((allCoinsDao.getAllAllCoins(wallet_name) != null)) {
                        updateCoins(allCoinsDao.getAllAllCoins(wallet_name).coinsList);
                    } else {
                        if (CommonUtilities.isConnectionAvailable(CreateADCampaignsActivity.this)) {
                            onLineFetchAccountWallet(wallet_name, walletId);
                        } else {
                            CommonUtilities.ShowToastMessage(CreateADCampaignsActivity.this, getResources().getString(R.string.internetconnection));
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void onProgressLoad() {
        pb.setVisibility(View.VISIBLE);
        lnr_empty_coins.setVisibility(View.GONE);
        spnr_sel_coin.setVisibility(View.GONE);
    }

    private void updateCoins(String responsevalue) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                // this will run in the main thread
                coinValueUpdate(responsevalue);
                onProgressHide();
                spinnerAdapter.notifyDataSetChanged();
            }
        });
    }

    private void onLineFetchAccountWallet(String walletName, int walletId) {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            CryptoControllerApi apiService = DeviantXApiClient.getClient().create(CryptoControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.getAccountWallet(CONSTANTS.DeviantMulti + token, walletName);
            apiResponse.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responsevalue = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateCoins(responsevalue);
                                AllCoinsDao mDao = db.allCoinsDao();
                                AllCoinsDB allCoinsDB = new AllCoinsDB(walletName, responsevalue, walletId);
                                mDao.insertAllCoins(allCoinsDB);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        CommonUtilities.ShowToastMessage(CreateADCampaignsActivity.this, getResources().getString(R.string.Timeout));
                    } else if (t instanceof java.net.ConnectException) {
                        CommonUtilities.ShowToastMessage(CreateADCampaignsActivity.this, getResources().getString(R.string.networkerror));
                    } else {
                        CommonUtilities.ShowToastMessage(CreateADCampaignsActivity.this, getResources().getString(R.string.errortxt));
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(CreateADCampaignsActivity.this, getResources().getString(R.string.errortxt));
        }
    }

    private void coinValueUpdate(String responsevalue) {
        try {
            if (!responsevalue.isEmpty() && responsevalue != null) {
                JSONObject jsonObject = new JSONObject(responsevalue);
                loginResponseMsg = jsonObject.getString("msg");
                loginResponseStatus = jsonObject.getString("status");
                if (loginResponseStatus.equals("true")) {
                    loginResponseData = jsonObject.getString("data");
                    accountWalletlist = new ArrayList<>();
                    AccountWallet[] accountWallets = GsonUtils.getInstance().fromJson(loginResponseData, AccountWallet[].class);
                    accountWalletlist = new ArrayList<AccountWallet>(Arrays.asList(accountWallets));
                    if (accountWalletlist.size() == 0) {
                        spnr_sel_coin.setVisibility(View.GONE);
                        lnr_empty_coins.setVisibility(View.VISIBLE);
//                                    lnr_add_new_coins.setVisibility(View.GONE);
                        txt_avail_coins.setText("0");
                    } else {
                        lnr_empty_coins.setVisibility(View.GONE);
                        spnr_sel_coin.setVisibility(View.VISIBLE);
                        for (int i = 0; i < accountWalletlist.size(); i++) {
                            walletCoinsList.add(accountWalletlist.get(i).getStr_coin_name() + " (" + accountWalletlist.get(i).getStr_coin_code() + ")");
                        }
                        spinnerAdapter.setAllCoins(walletCoinsList);
                        spnr_sel_coin.setAdapter(spinnerAdapter);

                        coinName = sharedPreferences.getString(CONSTANTS.selectedCoinName, null);
/*
                        for (AccountWallet accountWallet : accountWalletlist) {
                            String cname = accountWallet.getStr_coin_name() + " (" + accountWallet.getStr_coin_code() + ")";
                            if (cname.equals(coinName)) {
                                txt_avail_coins.setText(String.format("%.4f", accountWallet.getStr_data_balance()) + " " + accountWallet.getStr_coin_code());
                                txt_coin_code.setText(accountWallet.getStr_coin_code());
                            }
                        }
*/
                        getAvailBal(coinName, accountWalletlist);

                    }
                } else if (loginResponseStatus.equals("401")) {
                    CommonUtilities.sessionExpired(CreateADCampaignsActivity.this, loginResponseMsg);
                } else {
                    CommonUtilities.ShowToastMessage(CreateADCampaignsActivity.this, loginResponseMsg);
                }
            } else {
                CommonUtilities.ShowToastMessage(CreateADCampaignsActivity.this, loginResponseMsg);
//                            Toast.makeText(getApplicationContext(), responsevalue, Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            CommonUtilities.ShowToastMessage(CreateADCampaignsActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
        }
    }

    private void getAvailBal(String coinName, ArrayList<AccountWallet> accountWalletlist) {
        for (AccountWallet accountWallet : accountWalletlist) {
            String cname = accountWallet.getStr_coin_name() + " (" + accountWallet.getStr_coin_code() + ")";
            if (cname.equals(coinName)) {
                editor.putString(CONSTANTS.selectedCoinCode, accountWallet.getStr_coin_code());
                editor.apply();
                txt_avail_coins.setText(String.format("%.4f", accountWallet.getStr_data_balance()));
                txt_avail_coinCode.setText(accountWallet.getStr_coin_code());
                txt_coin_code.setText(accountWallet.getStr_coin_code());
            }
        }

    }

    private void onProgressHide() {
        pb.setVisibility(View.GONE);
    }

}
