package com.cryptowallet.deviantx.UI.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.ServiceAPIs.WalletControllerApi;
import com.cryptowallet.deviantx.UI.Adapters.MyWalletListRAdapter;
import com.cryptowallet.deviantx.UI.Interfaces.WalletUIChangeListener;
import com.cryptowallet.deviantx.UI.Models.WalletList;
import com.cryptowallet.deviantx.UI.RoomDatabase.Database.DeviantXDB;
import com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB.AccountWalletDao;
import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.AccountWalletDB;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;
import com.cryptowallet.deviantx.Utilities.DeviantXApiClient;
import com.cryptowallet.deviantx.Utilities.GsonUtils;

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

public class WalletListActivity extends AppCompatActivity {


    @BindView(R.id.toolbar_center_back)
    Toolbar toolbar_center_back;
    @BindView(R.id.rview_my_walletlist)
    RecyclerView rview_my_walletlist;
    @BindView(R.id.btn_create_wallet)
    Button btn_create_wallet;


    LinearLayoutManager linearLayoutManager;
    MyWalletListRAdapter myWalletListRAdapter;

        @Override
    public void onDestroy() {
        super.onDestroy();
        if (walletUIChangeListener != null) {
            myApplication.setWalletUIChangeListener(null);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        myApplication.setWalletUIChangeListener(walletUIChangeListener);
    }

    WalletUIChangeListener walletUIChangeListener = new WalletUIChangeListener() {
        @Override
        public void onWalletUIChanged(String wallets, Boolean isDefaultWalle) {
            walletUpdate(wallets);
        }

        @Override
        public void onWalletCoinUIChanged(String allCoins) {

        }
    };
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;

    String loginResponseData, loginResponseStatus, loginResponseMsg;
    ArrayList<WalletList> walletList;

    DeviantXDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_list);

        ButterKnife.bind(this);
        db = DeviantXDB.getDatabase(this);

        walletList = new ArrayList<>();
        myWalletListRAdapter = new MyWalletListRAdapter(WalletListActivity.this, walletList);

        sharedPreferences = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        toolbar_center_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

/*
//        Getting Wallets List
        invokeWallet();

*/

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                onLoadWallet();
            }
        }, 100);


        linearLayoutManager = new LinearLayoutManager(WalletListActivity.this, LinearLayoutManager.VERTICAL, false);
        rview_my_walletlist.setLayoutManager(linearLayoutManager);

//        myWalletListRAdapter = new MyWalletListRAdapter(WalletListActivity.this);
//        rview_my_walletlist.setAdapter(myWalletListRAdapter);

        btn_create_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WalletListActivity.this, SetUpWalletActivity.class);
                startActivity(intent);
            }
        });

    }

    private void onLoadWallet() {

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AccountWalletDao accountWalletDao = db.accountWalletDao();
                if ((accountWalletDao.getAllAccountWallet()).size() > 0) {
                    String walletResult = (accountWalletDao.getAllAccountWallet()).get(0).walletDatas;
                    walletUpdate(walletResult);
                } else {
                    if (CommonUtilities.isConnectionAvailable(getApplicationContext())) {
                        invokeWallet();
                    } else {
                        CommonUtilities.ShowToastMessage(getApplicationContext(), getResources().getString(R.string.internetconnection));
                    }
                }
            }
        });

    }

    private void walletUpdate(String responsevalue) {
        try {
            if (!responsevalue.isEmpty() && responsevalue != null) {
                walletList = new ArrayList<>();
                JSONObject jsonObject = new JSONObject(responsevalue);
                loginResponseMsg = jsonObject.getString("msg");
                loginResponseStatus = jsonObject.getString("status");

                if (loginResponseStatus.equals("true")) {
                    loginResponseData = jsonObject.getString("data");
                    WalletList[] coinsStringArray = GsonUtils.getInstance().fromJson(loginResponseData, WalletList[].class);
                    walletList = new ArrayList<WalletList>(Arrays.asList(coinsStringArray));

                    myWalletListRAdapter.setAllWallets(walletList);
                    rview_my_walletlist.setAdapter(myWalletListRAdapter);

                } else {
                    CommonUtilities.ShowToastMessage(this, loginResponseMsg);
                }

            } else {
                CommonUtilities.ShowToastMessage(this, loginResponseMsg);
//                            Toast.makeText(getApplicationContext(), responsevalue, Toast.LENGTH_LONG).show();
                Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
            }

        } catch (Exception e) {
            e.printStackTrace();
            CommonUtilities.ShowToastMessage(this, getResources().getString(R.string.errortxt));
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
                        AccountWalletDB accountWalletDB = new AccountWalletDB(responsevalue);
                        accountWalletDao.insertAccountWallet(accountWalletDB);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        CommonUtilities.ShowToastMessage(WalletListActivity.this, getResources().getString(R.string.Timeout));
                    } else if (t instanceof java.net.ConnectException) {
                        CommonUtilities.ShowToastMessage(WalletListActivity.this, getResources().getString(R.string.networkerror));
                    } else {
                        CommonUtilities.ShowToastMessage(WalletListActivity.this, getResources().getString(R.string.errortxt));
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(WalletListActivity.this, getResources().getString(R.string.errortxt));
        }

    }

//    private void invokeWallet() {
//        try {
//            String token = sharedPreferences.getString(CONSTANTS.token, null);
//            progressDialog = ProgressDialog.show(WalletListActivity.this, "", getResources().getString(R.string.please_wait), true);
//            WalletControllerApi apiService = DeviantXApiClient.getClient().create(WalletControllerApi.class);
//            Call<ResponseBody> apiResponse = apiService.getAllWallet(CONSTANTS.DeviantMulti + token);
//            apiResponse.enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                    try {
//                        String responsevalue = response.body().string();
//
//                        if (!responsevalue.isEmpty() && responsevalue != null) {
//                            progressDialog.dismiss();
//
//                            JSONObject jsonObject = new JSONObject(responsevalue);
//                            loginResponseMsg = jsonObject.getString("msg");
//                            loginResponseStatus = jsonObject.getString("status");
//
//                            if (loginResponseStatus.equals("true")) {
//                                walletList = new ArrayList<>();
//                                loginResponseData = jsonObject.getString("data");
//                                JSONArray jsonArrayData = new JSONArray(loginResponseData);
//                                for (int i = 0; i < jsonArrayData.length(); i++) {
//                                    JSONObject jsonObjectData = jsonArrayData.getJSONObject(i);
//                                    try {
//                                        int_data_id = jsonObjectData.getInt("id");
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                    try {
//                                        str_data_name = jsonObjectData.getString("name");
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                    try {
//                                        dbl_data_totalBal = jsonObjectData.getDouble("toatalBalance");
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                    try {
//                                        defaultWallet = jsonObjectData.getBoolean("defaultWallet");
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                    walletList.add(new WalletListDB(int_data_id, str_data_name, dbl_data_totalBal, defaultWallet));
//                                }
//                                myWalletListRAdapter = new MyWalletListRAdapter(WalletListActivity.this, walletList);
//                                rview_my_walletlist.setAdapter(myWalletListRAdapter);
//
//                            } else {
//                                CommonUtilities.ShowToastMessage(WalletListActivity.this, loginResponseMsg);
//                            }
//
//
//                        } else {
//                            CommonUtilities.ShowToastMessage(WalletListActivity.this, loginResponseMsg);
////                            Toast.makeText(getApplicationContext(), responsevalue, Toast.LENGTH_LONG).show();
//                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
//                        }
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        progressDialog.dismiss();
//                        CommonUtilities.ShowToastMessage(WalletListActivity.this, getResources().getString(R.string.errortxt));
////                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    if (t instanceof SocketTimeoutException) {
//                        progressDialog.dismiss();
//                        CommonUtilities.ShowToastMessage(WalletListActivity.this, getResources().getString(R.string.Timeout));
////                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Timeout), Toast.LENGTH_SHORT).show();
//                    } else if (t instanceof java.net.ConnectException) {
//                        progressDialog.dismiss();
//                        CommonUtilities.ShowToastMessage(WalletListActivity.this, getResources().getString(R.string.networkerror));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.networkerror), Toast.LENGTH_SHORT).show();
//                    } else {
//                        progressDialog.dismiss();
//                        CommonUtilities.ShowToastMessage(WalletListActivity.this, getResources().getString(R.string.errortxt));
////                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//        } catch (Exception ex) {
//            progressDialog.dismiss();
//            ex.printStackTrace();
//            CommonUtilities.ShowToastMessage(WalletListActivity.this, getResources().getString(R.string.errortxt));
////            Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
//        }
//
//
//    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(WalletListActivity.this,DashBoardActivity.class);
        startActivity(intent);
    }
}
