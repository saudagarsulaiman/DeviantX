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
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.ServiceAPIs.AirdropWalletControllerApi;
import com.cryptowallet.deviantx.ServiceAPIs.WalletControllerApi;
import com.cryptowallet.deviantx.UI.Adapters.SpinnerDaysAdapter;
import com.cryptowallet.deviantx.UI.Adapters.WalletListAirdropRAdapter;
import com.cryptowallet.deviantx.UI.Interfaces.WalletSelectableListener;
import com.cryptowallet.deviantx.UI.Models.AirdropWallet;
import com.cryptowallet.deviantx.UI.Models.WalletList;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;
import com.cryptowallet.deviantx.Utilities.DeviantXApiClient;
import com.google.zxing.Result;
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
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cryptowallet.deviantx.Utilities.MyApplication.myApplication;

public class WithdrawFundsAirdropActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, ZXingScannerView.ResultHandler, DiscreteScrollView.OnItemChangedListener<RecyclerView.ViewHolder> {


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
    @BindView(R.id.img_scanner)
    ImageView img_scanner;

    @BindView(R.id.lnr_address)
    LinearLayout lnr_address;

    @BindView(R.id.scan_qr)
    ZXingScannerView mScannerView;
    @BindView(R.id.scan_view)
    RelativeLayout mScannerLayout;
    @BindView(R.id.item_picker)
    DiscreteScrollView itemPicker;

    WalletListAirdropRAdapter walletListAirdropRAdapter;
    String walletName;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;

    ArrayList<WalletList> walletList;
    private String loginResponseData, loginResponseStatus, loginResponseMsg, str_data_name;
    private int int_data_walletid;
    private double dbl_data_totalBal;
    private boolean defaultWallet = false;

    String /*walletName,*/ regResponseMsg, regResponseStatus, regResponsedata;
    Boolean isEditAmount = false;

    WalletSelectableListener walletSelectableListener;

    ArrayList<AirdropWallet> airdropWalletlist;
    int selectedCoinId = 0;
    String selectedWalletName = "";
    Double avail_bal = 0.0, selectedWalletBal = 0.0;

    @Override
    protected void onResume() {
        super.onResume();
        myApplication.disableScreenCapture(this);
//        CommonUtilities.serviceStart(WithdrawFundsAirdropActivity.this);
    }

/*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_HOME) {
//            Log.e("home key pressed", "****");
            // write your code here to stop the activity
            CommonUtilities.serviceStop(WithdrawFundsAirdropActivity.this);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
//        Log.e("home key pressed on pause", "****");
        // write your code here to stop your service
        CommonUtilities.serviceStop(WithdrawFundsAirdropActivity.this);
        super.onPause();
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
        setContentView(R.layout.activity_withdraw_funds_airdrop);

        ButterKnife.bind(this);
        walletList = new ArrayList<>();
        airdropWalletlist = new ArrayList<>();

        sharedPreferences = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        toolbar_center_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        itemPicker.setOrientation(DSVOrientation.HORIZONTAL);
        itemPicker.addOnItemChangedListener(this);
        walletListAirdropRAdapter = new WalletListAirdropRAdapter(WithdrawFundsAirdropActivity.this, walletList, walletSelectableListener);
        itemPicker.setAdapter(walletListAirdropRAdapter);
        itemPicker.setItemTransitionTimeMillis(150);
        itemPicker.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build());

        Bundle bundle = getIntent().getExtras();
        airdropWalletlist = bundle.getParcelableArrayList(CONSTANTS.selectedAccountWallet);
        txt_avail_bal.setText(String.format("%.4f", airdropWalletlist.get(0).getDbl_data_ad_balance()));

        avail_bal = airdropWalletlist.get(0).getDbl_data_ad_balance() - 0.01;

        if (cbox_wallet.isChecked()) {
            spnr_wallets.setVisibility(View.GONE);
            editor.putBoolean(CONSTANTS.check, true);
            editor.apply();
            //            lnr_address.setBackground(getResources().getDrawable(R.drawable.rec_grey2_white));
//            spnr_wallets.setVisibility(View.VISIBLE);
            itemPicker.setVisibility(View.VISIBLE);
            edt_wallet_address.setVisibility(View.GONE);
            img_scanner.setVisibility(View.GONE);
            lnr_address.setBackground(getResources().getDrawable(R.color.transparent));
        } else {
            editor.putBoolean(CONSTANTS.check, false);
            editor.apply();
            //            lnr_address.setBackground(getResources().getDrawable(R.drawable.rec_lytblue_c5));
            lnr_address.setBackground(getResources().getDrawable(R.drawable.rec_lytblue_c5));
            itemPicker.setVisibility(View.GONE);
            spnr_wallets.setVisibility(View.GONE);
            edt_wallet_address.setVisibility(View.VISIBLE);
            img_scanner.setVisibility(View.VISIBLE);
        }

        cbox_wallet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.putBoolean(CONSTANTS.check, true);
                    editor.apply();
                    lnr_address.setBackground(getResources().getDrawable(R.color.transparent));
                    spnr_wallets.setVisibility(View.GONE);
//                    spnr_wallets.setVisibility(View.VISIBLE);
                    itemPicker.setVisibility(View.VISIBLE);
                    edt_wallet_address.setVisibility(View.GONE);

                    img_scanner.setVisibility(View.GONE);
                } else {
                    editor.putBoolean(CONSTANTS.check, false);
                    editor.apply();
                    lnr_address.setBackground(getResources().getDrawable(R.drawable.rec_lytblue_c5));
                    itemPicker.setVisibility(View.GONE);
                    spnr_wallets.setVisibility(View.GONE);
                    edt_wallet_address.setVisibility(View.VISIBLE);
                    img_scanner.setVisibility(View.VISIBLE);
                }
            }
        });

        img_scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   /* Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                    intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                    startActivityForResult(intent, 0);*/
                if (hasPermissions(WithdrawFundsAirdropActivity.this, PERMISSIONS)) {
                    mScannerLayout.setVisibility(View.VISIBLE);
                    mScannerView.setResultHandler(WithdrawFundsAirdropActivity.this); // Register ourselves as a handler for scan results.<br />
                    mScannerView.startCamera();
                } else {
                    ActivityCompat.requestPermissions(WithdrawFundsAirdropActivity.this, PERMISSIONS, PERMISSION_ALL);
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


        walletSelectableListener = new WalletSelectableListener() {
            @Override
            public void WalletSelected(ArrayList<WalletList> selected_allWalletList, int pos) {

                int i = 0;
                //  final WalletList selectedWallet = new WalletList();
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


            }
        };

        btn_withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double fee = 0.01;
                if (cbox_wallet.isChecked()) {
                    int position = spnr_wallets.getSelectedItemPosition();
//                    String walletName = spnr_wallets.getItemAtPosition(position).toString();
//                            String walletName = walletList.get(itemPicker.getCurrentItem()).getStr_data_name();
                    String walletName = selectedWalletName;
                    String amount = edt_amount.getText().toString().trim();
                    if (!amount.isEmpty()) {
                        float amt = Float.parseFloat(amount);
                        if (amt > 0) {
                            if (walletName.length() > 0) {
                                if (Double.parseDouble(amount) + fee < selectedWalletBal) {
                                    if (myApplication.get2FA()) {
                                        Intent intent = new Intent(WithdrawFundsAirdropActivity.this, TwoFAAirDropActivity.class);
                                        Bundle bundle1 = new Bundle();
                                        bundle1.putString(CONSTANTS.walletName, walletName);
                                        bundle1.putString(CONSTANTS.address, "");
                                        bundle1.putString(CONSTANTS.amount, amount);
                                        bundle1.putParcelableArrayList(CONSTANTS.selectedAccountWallet, airdropWalletlist);
                                        intent.putExtras(bundle1);
                                        startActivity(intent);
                                    } else {
                                        toWalletDialog(walletName, amount, airdropWalletlist.get(0).getStr_data_ad_address(), airdropWalletlist.get(0).getAllCoins().getStr_coin_code());
//                        transferAmountToWallet(airdropWalletlist.get(0).getStr_data_ad_address(), walletName, amount, airdropWalletlist.get(0).getAllCoins().getStr_coin_code());
                                    }
                                } else {
                                    CommonUtilities.ShowToastMessage(WithdrawFundsAirdropActivity.this, getResources().getString(R.string.maintain_bal));
                                }
                            } else {
                                CommonUtilities.ShowToastMessage(WithdrawFundsAirdropActivity.this, getResources().getString(R.string.select_wallet));
                            }
                        } else {
                            CommonUtilities.ShowToastMessage(WithdrawFundsAirdropActivity.this, getResources().getString(R.string.insufficient_fund));
                        }
                    } else {
                        CommonUtilities.ShowToastMessage(WithdrawFundsAirdropActivity.this, getResources().getString(R.string.empty_amount));
                    }

                } else {
                    String to_address = edt_wallet_address.getText().toString().trim();
                    String amount = edt_amount.getText().toString().trim();
                    if (!to_address.isEmpty()) {
                        if (!amount.isEmpty()) {
                            float amt = Float.parseFloat(amount);
                            if (amt > 0) {
                                if (Double.parseDouble(amount) + fee < selectedWalletBal) {
                                    if (myApplication.get2FA()) {
                                        Intent intent = new Intent(WithdrawFundsAirdropActivity.this, TwoFAAirDropActivity.class);
                                        Bundle bundle1 = new Bundle();
                                        bundle1.putString(CONSTANTS.walletName, "");
                                        bundle1.putString(CONSTANTS.address, to_address);
                                        bundle1.putString(CONSTANTS.amount, amount);
                                        bundle1.putParcelableArrayList(CONSTANTS.selectedAccountWallet, airdropWalletlist);
                                        intent.putExtras(bundle1);
                                        startActivity(intent);
                                    } else {
                                        toAddressDialog(to_address, amount, airdropWalletlist.get(0).getStr_data_ad_address(), airdropWalletlist.get(0).getAllCoins().getStr_coin_code());
//                            transferAmountToAddress(edt_address, amount, airdropWalletlist.get(0).getStr_data_ad_address());
                                    }
                                } else {
                                    CommonUtilities.ShowToastMessage(WithdrawFundsAirdropActivity.this, getResources().getString(R.string.maintain_bal));
                                }
                            } else {
                                CommonUtilities.ShowToastMessage(WithdrawFundsAirdropActivity.this, getResources().getString(R.string.insufficient_fund));
                            }
                        } else {
                            CommonUtilities.ShowToastMessage(WithdrawFundsAirdropActivity.this, getResources().getString(R.string.empty_amount));
                        }
                    } else {
                        CommonUtilities.ShowToastMessage(WithdrawFundsAirdropActivity.this, getResources().getString(R.string.empty_address));
                    }

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
                            avail_bal = airdropWalletlist.get(0).getDbl_data_ad_balance() - 0.01;
                            if (avail_bal < finalValue) {
                                CommonUtilities.ShowToastMessage(WithdrawFundsAirdropActivity.this, getResources().getString(R.string.insufficient_fund));
                                edt_amount.setText("0");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    CommonUtilities.ShowToastMessage(WithdrawFundsAirdropActivity.this, getResources().getString(R.string.enter_amount));
                }
            }
        });

       /* edt_amount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                isEditAmount = b;
            }
        });*/
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
                                walletListAirdropRAdapter = new WalletListAirdropRAdapter(WithdrawFundsAirdropActivity.this, walletList, walletSelectableListener);
                                itemPicker.setAdapter(walletListAirdropRAdapter);
//                                itemPicker.scrollToPosition(defaultWalletPos);
                                itemPicker.scrollToPosition(myApplication.getDefaultWallet());
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

    private void toAddressDialog(String edt_address, String amount, String str_data_ad_address, String str_coin_code) {
        //                Creating A Custom Dialog Using DialogPlus
        ViewHolder viewHolder = new ViewHolder(R.layout.dialog_withdraw_confirm);
        final DialogPlus dialog = DialogPlus.newDialog(WithdrawFundsAirdropActivity.this)
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
        TextView txt_fee_amt_code = view.findViewById(R.id.txt_fee_amt_code);
        TextView txt_address = view.findViewById(R.id.txt_address);
        TextView txt_privacy_policy = view.findViewById(R.id.txt_privacy_policy);

        txt_withdraw_amt.setText(amount);
        txt_withdraw_amt_code.setText(str_coin_code);
//        txt_fee_amt.setText(String.format("%.4f",));
        txt_fee_amt.setText("0.01");
        txt_fee_amt_code.setText(str_coin_code);
        txt_address.setText(edt_address);
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
                txt_avail_bal.setText(String.format("%.4f", airdropWalletlist.get(0).getDbl_data_ad_balance()));
                dialog.dismiss();
            }
        });
        txt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transferAmountToAddress(edt_address, amount, airdropWalletlist.get(0).getStr_data_ad_address());
                dialog.dismiss();
            }
        });

        txt_avail_bal.setText(String.format("%.4f", avail_bal - Double.parseDouble(amount)));
        dialog.show();
    }

    private void toWalletDialog(String walletName, String amount, String str_data_ad_address, String str_coin_code) {
        //                Creating A Custom Dialog Using DialogPlus
        ViewHolder viewHolder = new ViewHolder(R.layout.dialog_withdraw_confirm);
        final DialogPlus dialog = DialogPlus.newDialog(WithdrawFundsAirdropActivity.this)
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
        TextView txt_fee_amt_code = view.findViewById(R.id.txt_fee_amt_code);
        TextView txt_address = view.findViewById(R.id.txt_address);
        TextView txt_privacy_policy = view.findViewById(R.id.txt_privacy_policy);


        txt_withdraw_amt.setText(amount);
        txt_withdraw_amt_code.setText(str_coin_code);
//        txt_fee_amt.setText(String.format("%.4f",));
        txt_fee_amt.setText("0.01");
        txt_fee_amt_code.setText(str_coin_code);
        txt_address.setText(walletName);

        txt_privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_avail_bal.setText(String.format("%.4f", airdropWalletlist.get(0).getDbl_data_ad_balance()));
                dialog.dismiss();
            }
        });
        txt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transferAmountToWallet(airdropWalletlist.get(0).getStr_data_ad_address(), walletName, amount, airdropWalletlist.get(0).getAllCoins().getStr_coin_code());
                dialog.dismiss();
            }
        });

        txt_avail_bal.setText(String.format("%.4f", avail_bal - Double.parseDouble(amount)));
        dialog.show();

    }

    private void transferAmountToAddress(String edt_address, String amount, String walletAddress) {
        try {
            JSONObject params = new JSONObject();
            String token = sharedPreferences.getString(CONSTANTS.token, "");
            try {
                params.put("fromAddress", walletAddress);
                params.put("toAddress", edt_address);
                params.put("amount", amount);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialog = ProgressDialog.show(WithdrawFundsAirdropActivity.this, "", getResources().getString(R.string.please_wait), true);
            AirdropWalletControllerApi apiService = DeviantXApiClient.getClient().create(AirdropWalletControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.transferToAddress(params.toString(), "DEVIANTMULTI " + token);
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
//                                CommonUtilities.ShowToastMessage(WithdrawFundsAirdropActivity.this, getResources().getString(R.string.login_success));
                                Intent intent = new Intent(WithdrawFundsAirdropActivity.this, DashBoardActivity.class);
                                intent.putExtra(CONSTANTS.seletedTab, 2);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                CommonUtilities.ShowToastMessage(WithdrawFundsAirdropActivity.this, regResponseMsg);
//                                onBackPressed();
//                                Log.i(CONSTANTS.TAG, "onResponse:\n" + loginResponseMsg);
                            } else {
                                CommonUtilities.ShowToastMessage(WithdrawFundsAirdropActivity.this, regResponseMsg);
                            }
                        } else {
                            CommonUtilities.ShowToastMessage(WithdrawFundsAirdropActivity.this, regResponseMsg);
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
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.networkerror), Toast.LENGTH_SHORT).show();
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

    private void transferAmountToWallet(String walletAddress, String walletName, String amount, String coinCode) {
        try {
            JSONObject params = new JSONObject();
            String token = sharedPreferences.getString(CONSTANTS.token, "");
            try {
                params.put("fromAddress", walletAddress);
                params.put("wallet", walletName);
                params.put("amount", amount);
                params.put("coinCode", coinCode);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialog = ProgressDialog.show(WithdrawFundsAirdropActivity.this, "", getResources().getString(R.string.please_wait), true);
            AirdropWalletControllerApi apiService = DeviantXApiClient.getClient().create(AirdropWalletControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.transferToWallet(params.toString(), "DEVIANTMULTI " + token);
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
//                                CommonUtilities.ShowToastMessage(WithdrawFundsAirdropActivity.this, getResources().getString(R.string.login_success));
                                Intent intent = new Intent(WithdrawFundsAirdropActivity.this, DashBoardActivity.class);
                                intent.putExtra(CONSTANTS.seletedTab, 2);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                CommonUtilities.ShowToastMessage(WithdrawFundsAirdropActivity.this, regResponseMsg);
//                                onBackPressed();
//                                Log.i(CONSTANTS.TAG, "onResponse:\n" + loginResponseMsg);
                            } else {
                                CommonUtilities.ShowToastMessage(WithdrawFundsAirdropActivity.this, regResponseMsg);
                            }
                        } else {
                            CommonUtilities.ShowToastMessage(WithdrawFundsAirdropActivity.this, regResponseMsg);
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
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.networkerror), Toast.LENGTH_SHORT).show();
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mScannerLayout.setVisibility(View.VISIBLE);
            mScannerView.setResultHandler(WithdrawFundsAirdropActivity.this); // Register ourselves as a handler for scan results.<br />
            mScannerView.startCamera();
        } else {
            CommonUtilities.ShowToastMessage(WithdrawFundsAirdropActivity.this, getResources().getString(R.string.cam_per_failed));
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    public void handleResult(Result result) {
        edt_wallet_address.setText(result.getText().trim());
        mScannerLayout.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        if (mScannerLayout.getVisibility() == View.VISIBLE) {
            mScannerLayout.setVisibility(View.GONE);
            CommonUtilities.ShowToastMessage(WithdrawFundsAirdropActivity.this, getResources().getString(R.string.cancelled));
        } else
            super.onBackPressed();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        walletName = parent.getItemAtPosition(position).toString();
//        transferAmount(walletName);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {
        if (adapterPosition > -1) {
            walletName = walletList.get(adapterPosition).getStr_data_name();
        }

/*
        if (adapterPosition > -1) {
            String wallet_name = sharedPreferences.getString(CONSTANTS.walletName, "sss");
            if (!wallet_name.equals(walletList.get(adapterPosition).getStr_data_name())) {
                onItemChanged(walletList.get(adapterPosition));
                accountWalletlist = new ArrayList<>();
                filterCoinlist = new ArrayList<>();
                favFilter.setImageDrawable(getResources().getDrawable(R.drawable.z_grey));
                favFilter.setTag("unFav");
                myWalletCoinsRAdapter = new MyWalletCoinsRAdapter(getActivity(), accountWalletlist, favListener);
                rview_wallet_coins.setAdapter(myWalletCoinsRAdapter);
            }
        }
*/
    }


}
