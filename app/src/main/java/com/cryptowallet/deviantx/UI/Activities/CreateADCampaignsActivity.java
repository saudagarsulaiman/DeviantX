package com.cryptowallet.deviantx.UI.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.ServiceAPIs.CryptoControllerApi;
import com.cryptowallet.deviantx.ServiceAPIs.WalletControllerApi;
import com.cryptowallet.deviantx.UI.Adapters.SpinnerDaysAdapter;
import com.cryptowallet.deviantx.UI.Adapters.WalletListAirdropRAdapter;
import com.cryptowallet.deviantx.UI.Interfaces.WalletSelectableListener;
import com.cryptowallet.deviantx.UI.Models.AccountWallet;
import com.cryptowallet.deviantx.UI.Models.WalletList;
import com.cryptowallet.deviantx.UI.RoomDatabase.Database.DeviantXDB;
import com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB.AccountWalletDao;
import com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB.AllCoinsDao;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;
import com.cryptowallet.deviantx.Utilities.DeviantXApiClient;
import com.cryptowallet.deviantx.Utilities.GsonUtils;
import com.yarolegovich.discretescrollview.DSVOrientation;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

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
    String walletName;
    ArrayList<WalletList> walletList;
    private String loginResponseData, loginResponseStatus, loginResponseMsg, str_data_name;
    private int int_data_walletid;
    private double dbl_data_totalBal;
    private boolean defaultWallet = false;
    String /*walletName,*/ regResponseMsg, regResponseStatus, regResponsedata;
    Boolean isEditAmount = false;
    WalletSelectableListener walletSelectableListener;
    //    ArrayList<AirdropWallet> airdropWalletlist;
    int selectedCoinId = 0;
    String selectedWalletName = "";
    Double avail_bal = 0.0, selectedWalletBal = 0.0;
    DeviantXDB db;
    ArrayList<AccountWallet> accountWalletlist;
    ArrayList<String> walletCoinsList;
    SpinnerDaysAdapter spinnerAdapter;

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

        walletListAirdropRAdapter = new WalletListAirdropRAdapter(CreateADCampaignsActivity.this, walletList, walletSelectableListener);
        itemPicker.setAdapter(walletListAirdropRAdapter);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                onLoadWallet();
            }
        }, 100);

        itemPicker.setOrientation(DSVOrientation.HORIZONTAL);
        itemPicker.addOnItemChangedListener(this);
        itemPicker.setAdapter(walletListAirdropRAdapter);
        itemPicker.setItemTransitionTimeMillis(150);
        itemPicker.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build());

        spnr_sel_coin.setOnItemSelectedListener(this);
        txt_avail_coins.setText("0");


        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtilities.serviceStart(CreateADCampaignsActivity.this);
                Intent intent = new Intent(CreateADCampaignsActivity.this, DashBoardActivity.class);
                intent.putExtra(CONSTANTS.seletedTab, 2);
                startActivity(intent);
            }
        });

    }

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
                        com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.AccountWallet accountWallet = new com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.AccountWallet(responsevalue);
                        accountWalletDao.insertAccountWallet(accountWallet);
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


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

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
                                com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.AllCoins allCoins = new com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.AllCoins(walletName, responsevalue, walletId);
                                mDao.insertAllCoins(allCoins);
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

    private void onProgressHide() {
        pb.setVisibility(View.GONE);
    }

}
