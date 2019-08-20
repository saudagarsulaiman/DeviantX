package com.cryptowallet.deviantx.UI.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.ServiceAPIs.ChangellyControllerApi;
import com.cryptowallet.deviantx.UI.Adapters.MySpinnerAdapter;
import com.cryptowallet.deviantx.UI.Models.ECTCreateTransaction;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;
import com.cryptowallet.deviantx.Utilities.DeviantXApiClient;
import com.cryptowallet.deviantx.Utilities.GsonUtils;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

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

public class ChangellyActivity extends AppCompatActivity {


    @BindView(R.id.toolbar_center_back)
    Toolbar toolbar_center_back;
    @BindView(R.id.img_history)
    ImageView img_history;
    @BindView(R.id.edt_exc_amt)
    EditText edt_exc_amt;
    @BindView(R.id.txt_exc_amt_code)
    TextView txt_exc_amt_code;
    @BindView(R.id.spnr_amt_exc)
    Spinner spnr_amt_exc;
    @BindView(R.id.img_interchange)
    ImageView img_interchange;
    @BindView(R.id.edt_rec_amt)
    /*EditText */ com.cryptowallet.deviantx.Utilities.AutoScrollableTextView edt_rec_amt;
    @BindView(R.id.txt_rec_amt_code)
    TextView txt_rec_amt_code;
    @BindView(R.id.spnr_amt_rec)
    Spinner spnr_amt_rec;
    @BindView(R.id.btn_continue)
    Button btn_continue;
    @BindView(R.id.btn_getExpAmt)
    Button btn_getExpAmt;
    @BindView(R.id.txt_min_amt)
    TextView txt_min_amt;


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialogCr, progressDialogMA, progressDialogEA, progressDialogCT;

    String loginResponseData, loginResponseStatus, loginResponseMsg, recItem = " ", excItem = " ";

    int recPosition = 0, excPosition = 0;

    ArrayList</*AllCoins*/String> allCoinsList, filteredCoins;

    MySpinnerAdapter mySpinnerAdapterExc, mySpinnerAdapterRec;

    @Override
    protected void onResume() {
        super.onResume();
        myApplication.disableScreenCapture(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changelly);

        ButterKnife.bind(this);

        sharedPreferences = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        allCoinsList = new ArrayList<>();

        toolbar_center_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (CommonUtilities.isConnectionAvailable(ChangellyActivity.this)) {
            //GET ALL COINS
            fetchCoins();
        } else {
            CommonUtilities.ShowToastMessage(ChangellyActivity.this, getResources().getString(R.string.internetconnection));
        }

        spnr_amt_exc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
/*
                String excItem = allCoinsList.get(position).getStr_coin_code();
*/
                excItem = parent.getItemAtPosition(position).toString();
                excPosition = position;
/*
                CommonUtilities.ShowToastMessage(ChangellyActivity.this, excItem);
*/
                txt_exc_amt_code.setText(excItem);
                filteredCoins = new ArrayList<>();
                for (int i = 0; i < allCoinsList.size(); i++) {
                    if (i != position) {
                        filteredCoins.add(allCoinsList.get(i));
                    }
                }
                recItem = filteredCoins.get(0);
                mySpinnerAdapterRec = new MySpinnerAdapter(ChangellyActivity.this, R.layout.spinner_row_lyt, filteredCoins);
                spnr_amt_rec.setAdapter(mySpinnerAdapterRec);

                getMinimumAmount(excItem, recItem);
                getExpectedAmount(excItem, recItem, edt_exc_amt.getText().toString().trim());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnr_amt_rec.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
/*
                String recItem = filteredCoins.get(position).getStr_coin_code();
*/
                recItem = parent.getItemAtPosition(position).toString();
                recPosition = position;
/*
                CommonUtilities.ShowToastMessage(ChangellyActivity.this, recItem);
*/
                txt_rec_amt_code.setText(/*String.format("%.12f", */recItem/*)*/);
                
/*
                edt_exc_amt.setText("1");
*/

                getMinimumAmount(excItem, recItem);
                getExpectedAmount(excItem, recItem, edt_exc_amt.getText().toString().trim());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        edt_exc_amt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString().trim();
                if (!str.isEmpty()) {
                    if (Double.parseDouble(str) > 0) {
                        getExpectedAmount(excItem, recItem, str);
                    } else {
                        CommonUtilities.ShowToastMessage(ChangellyActivity.this, getResources().getString(R.string.incorrect_amount));
                        edt_exc_amt.setText("1");
                    }
                } else {
                    edt_exc_amt.setText("1");
                }
            }
        });

        btn_getExpAmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMinimumAmount(excItem, recItem);
                getExpectedAmount(excItem, recItem, edt_exc_amt.getText().toString().trim());
            }
        });

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String exc_amt = edt_exc_amt.getText().toString().trim();
                String rec_amt = edt_rec_amt.getText().toString().trim();
                if (!exc_amt.isEmpty()) {
                    addressDialog(excItem, recItem, exc_amt);
                } else {
                    CommonUtilities.ShowToastMessage(ChangellyActivity.this, getResources().getString(R.string.empty_exc));
                }
            }
        });

        img_interchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*allCoinsList*/
                if (recItem != null) {
                    int spinnerPosition = mySpinnerAdapterExc.getPosition(recItem);
                    spnr_amt_exc.setSelection(spinnerPosition);
/*
                    String rec_amt = edt_rec_amt.getText().toString().trim();
                    edt_exc_amt.setText(rec_amt);
*/
                }
                /*if (excItem != null) {
                    int spinnerPosition = mySpinnerAdapterRec.getPosition(excItem);
                    spnr_amt_exc.setSelection(spinnerPosition);
                }*/
            }
        });
        img_history.setVisibility(View.VISIBLE);
        img_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangellyActivity.this, ECTTransactionsHistoryActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addressDialog(String from, String to, String amount) {

        ViewHolder viewHolder = new ViewHolder(R.layout.dialog_enter_address);
        final DialogPlus dialog = DialogPlus.newDialog(ChangellyActivity.this)
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

        final EditText edt_address = view.findViewById(R.id.edt_address);
        final TextView txt_ur_add = view.findViewById(R.id.txt_ur_add);
        final ImageView img_center_back = view.findViewById(R.id.img_center_back);
        Button btn_submit = view.findViewById(R.id.btn_submit);

        txt_ur_add.setText("Enter Your " + to.toUpperCase() + " Address");

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonUtilities.isConnectionAvailable(ChangellyActivity.this)) {
                    String address = edt_address.getText().toString().trim();
                    if (address.isEmpty()) {
                        CommonUtilities.ShowToastMessage(ChangellyActivity.this, getResources().getString(R.string.empty_field));
                    } else {
                        createTransaction(from, to, amount, address);
                        dialog.dismiss();
                    }
                } else {
                    CommonUtilities.ShowToastMessage(ChangellyActivity.this, getResources().getString(R.string.internetconnection));
                }
            }
        });

        img_center_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

//                Displaying DialogPlus
        dialog.show();
    }

    private void fetchCoins() {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            progressDialogCr = ProgressDialog.show(ChangellyActivity.this, "", getResources().getString(R.string.please_wait), true);
            ChangellyControllerApi apiService = DeviantXApiClient.getClient().create(ChangellyControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.getCurrencies(CONSTANTS.DeviantMulti + token);
            apiResponse.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responsevalue = response.body().string();

                        progressDialogCr.dismiss();
                        if (!responsevalue.isEmpty() && responsevalue != null) {

                            JSONObject jsonObject = new JSONObject(responsevalue);
                            loginResponseMsg = jsonObject.getString("msg");
                            loginResponseStatus = jsonObject.getString("status");

                            if (loginResponseStatus.equals("true")) {
                                loginResponseData = jsonObject.getString("data");
                                JSONArray jsonArrayData = new JSONArray(loginResponseData);
                                allCoinsList = new ArrayList<>();
                                for (int i = 0; i < jsonArrayData.length(); i++) {
                                    allCoinsList.add(jsonArrayData.getString(i));
                                }
                                mySpinnerAdapterExc = new MySpinnerAdapter(ChangellyActivity.this, R.layout.spinner_row_lyt, allCoinsList);
                                spnr_amt_exc.setAdapter(mySpinnerAdapterExc);
                            } else {
                                CommonUtilities.ShowToastMessage(ChangellyActivity.this, loginResponseMsg);
                            }

                        } else {
                            CommonUtilities.ShowToastMessage(ChangellyActivity.this, loginResponseMsg);
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialogCr.dismiss();
                        CommonUtilities.ShowToastMessage(ChangellyActivity.this, getResources().getString(R.string.errortxt));
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        progressDialogCr.dismiss();
                        CommonUtilities.ShowToastMessage(ChangellyActivity.this, getResources().getString(R.string.Timeout));
                    } else if (t instanceof java.net.ConnectException) {
                        progressDialogCr.dismiss();
                        CommonUtilities.ShowToastMessage(ChangellyActivity.this, getResources().getString(R.string.networkerror));
                    } else {
                        progressDialogCr.dismiss();
                        CommonUtilities.ShowToastMessage(ChangellyActivity.this, getResources().getString(R.string.errortxt));
                    }
                }
            });
        } catch (Exception ex) {
            progressDialogCr.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(ChangellyActivity.this, getResources().getString(R.string.errortxt));
        }

    }

    private void getMinimumAmount(String from, String to) {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            //progressDialogMA = ProgressDialog.show(ChangellyActivity.this, "", getResources().getString(R.string.please_wait), true);
            ChangellyControllerApi apiService = DeviantXApiClient.getClient().create(ChangellyControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.getMinimumAmount(CONSTANTS.DeviantMulti + token, from, to);
            apiResponse.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responsevalue = response.body().string();

                        if (!responsevalue.isEmpty() && responsevalue != null) {
                            //progressDialogMA.dismiss();

                            JSONObject jsonObject = new JSONObject(responsevalue);
                            loginResponseMsg = jsonObject.getString("msg");
                            loginResponseStatus = jsonObject.getString("status");

                            if (loginResponseStatus.equals("true")) {
                                loginResponseData = jsonObject.getString("data");
                                txt_min_amt.setText(" " + Double.parseDouble(loginResponseData));
                            } else {
                                txt_min_amt.setText("0.1");
                                CommonUtilities.ShowToastMessage(ChangellyActivity.this, loginResponseMsg);
                            }

                        } else {
                            CommonUtilities.ShowToastMessage(ChangellyActivity.this, loginResponseMsg);
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        //progressDialogMA.dismiss();
                        CommonUtilities.ShowToastMessage(ChangellyActivity.this, getResources().getString(R.string.errortxt));
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        //progressDialogMA.dismiss();
                        CommonUtilities.ShowToastMessage(ChangellyActivity.this, getResources().getString(R.string.Timeout));
                    } else if (t instanceof java.net.ConnectException) {
                        //progressDialogMA.dismiss();
                        CommonUtilities.ShowToastMessage(ChangellyActivity.this, getResources().getString(R.string.networkerror));
                    } else {
                        //progressDialogMA.dismiss();
                        CommonUtilities.ShowToastMessage(ChangellyActivity.this, getResources().getString(R.string.errortxt));
                    }
                }
            });
        } catch (Exception ex) {
//            //progressDialogMA.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(ChangellyActivity.this, getResources().getString(R.string.errortxt));
        }

    }

    private void getExpectedAmount(String from, String to, String amount) {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            //progressDialogEA = ProgressDialog.show(ChangellyActivity.this, "", getResources().getString(R.string.please_wait), true);
            ChangellyControllerApi apiService = DeviantXApiClient.getClient().create(ChangellyControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.getExpectedAmount(CONSTANTS.DeviantMulti + token, from, to, amount);
            apiResponse.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responsevalue = response.body().string();

                        //progressDialogEA.dismiss();
                        if (!responsevalue.isEmpty() && responsevalue != null) {

                            JSONObject jsonObject = new JSONObject(responsevalue);
                            loginResponseMsg = jsonObject.getString("msg");
                            loginResponseStatus = jsonObject.getString("status");

                            if (loginResponseStatus.equals("true")) {
                                loginResponseData = jsonObject.getString("data");

                                edt_rec_amt.setText(" " + Double.parseDouble(loginResponseData));

                            } else {
                                edt_rec_amt.setText("0");
                                CommonUtilities.ShowToastMessage(ChangellyActivity.this, loginResponseMsg);
                            }

                        } else {
                            CommonUtilities.ShowToastMessage(ChangellyActivity.this, loginResponseMsg);
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        //progressDialogEA.dismiss();
                        CommonUtilities.ShowToastMessage(ChangellyActivity.this, getResources().getString(R.string.errortxt));
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        //progressDialogEA.dismiss();
                        CommonUtilities.ShowToastMessage(ChangellyActivity.this, getResources().getString(R.string.Timeout));
                    } else if (t instanceof java.net.ConnectException) {
                        //progressDialogEA.dismiss();
                        CommonUtilities.ShowToastMessage(ChangellyActivity.this, getResources().getString(R.string.networkerror));
                    } else {
                        //progressDialogEA.dismiss();
                        CommonUtilities.ShowToastMessage(ChangellyActivity.this, getResources().getString(R.string.errortxt));
                    }
                }
            });
        } catch (Exception ex) {
            //progressDialogEA.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(ChangellyActivity.this, getResources().getString(R.string.errortxt));
        }
    }

    private void createTransaction(String from, String to, String amount, String address) {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            progressDialogCT = ProgressDialog.show(ChangellyActivity.this, "", getResources().getString(R.string.please_wait), true);
            ChangellyControllerApi apiService = DeviantXApiClient.getClient().create(ChangellyControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.createTransaction(CONSTANTS.DeviantMulti + token, from, to, address, amount);
            apiResponse.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responsevalue = response.body().string();

                        progressDialogCT.dismiss();
                        if (!responsevalue.isEmpty() && responsevalue != null) {

                            JSONObject jsonObject = new JSONObject(responsevalue);
                            loginResponseMsg = jsonObject.getString("msg");
                            loginResponseStatus = jsonObject.getString("status");

                            if (loginResponseStatus.equals("true")) {
                                loginResponseData = jsonObject.getString("data");
                                showPaymentDialog(loginResponseData);
                            } else {
                                edt_rec_amt.setText("0");
                                CommonUtilities.ShowToastMessage(ChangellyActivity.this, loginResponseMsg);
                            }

                        } else {
                            CommonUtilities.ShowToastMessage(ChangellyActivity.this, loginResponseMsg);
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialogCT.dismiss();
                        CommonUtilities.ShowToastMessage(ChangellyActivity.this, getResources().getString(R.string.errortxt));
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        progressDialogCT.dismiss();
                        CommonUtilities.ShowToastMessage(ChangellyActivity.this, getResources().getString(R.string.Timeout));
                    } else if (t instanceof java.net.ConnectException) {
                        progressDialogCT.dismiss();
                        CommonUtilities.ShowToastMessage(ChangellyActivity.this, getResources().getString(R.string.networkerror));
                    } else {
                        progressDialogCT.dismiss();
                        CommonUtilities.ShowToastMessage(ChangellyActivity.this, getResources().getString(R.string.errortxt));
                    }
                }
            });
        } catch (Exception ex) {
            progressDialogCT.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(ChangellyActivity.this, getResources().getString(R.string.errortxt));
        }
    }

    private void showPaymentDialog(String loginResponseData) {
        ViewHolder viewHolder = new ViewHolder(R.layout.dialog_exchange_confirm);
        final DialogPlus dialog = DialogPlus.newDialog(ChangellyActivity.this)
                .setContentHolder(viewHolder)
                .setGravity(Gravity.BOTTOM)
                .setCancelable(false)
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

        final TextView txt_exc_amount_bal = view.findViewById(R.id.txt_amount_bal);
        final TextView txt_exc_amount_code = view.findViewById(R.id.txt_amount_code);
        final TextView txt_rec_amount_bal = view.findViewById(R.id.txt_fiat_bal);
        final TextView txt_rec_amount_code = view.findViewById(R.id.txt_fiat_code);
        final TextView txt_fee = view.findViewById(R.id.txt_fee);
        final TextView txt_fee_code = view.findViewById(R.id.txt_fee_code);
        final TextView txt_to_address = view.findViewById(R.id.txt_to_address);
        TextView txt_confirm = view.findViewById(R.id.txt_confirm);

        ECTCreateTransaction transaction = GsonUtils.getInstance().fromJson(loginResponseData, ECTCreateTransaction.class);

        txt_exc_amount_bal.setText(transaction.getStr_amountExpectedFrom());
        txt_exc_amount_code.setText(transaction.getStr_currencyFrom().toUpperCase());
        txt_rec_amount_bal.setText(transaction.getStr_amountExpectedTo());
        txt_rec_amount_code.setText(transaction.getStr_currencyTo().toUpperCase());
        txt_fee.setText(transaction.getStr_changellyFee());
        txt_fee_code.setText(transaction.getStr_currencyFrom().toUpperCase());
        txt_to_address.setText(transaction.getStr_payoutAddress());

        txt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangellyActivity.this, ECTTransactionDetailsActivity.class);
                intent.putExtra(CONSTANTS.ectTransData, transaction);
                intent.putExtra(CONSTANTS.ectTransID, 0);
                startActivity(intent);
                dialog.dismiss();
            }
        });

//                Displaying DialogPlus
        dialog.show();
    }

}
