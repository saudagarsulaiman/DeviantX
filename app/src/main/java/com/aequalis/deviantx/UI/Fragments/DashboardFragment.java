package com.aequalis.deviantx.UI.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aequalis.deviantx.R;
import com.aequalis.deviantx.ServiceAPIs.CryptoControllerApi;
import com.aequalis.deviantx.UI.Activities.DashBoardActivity;
import com.aequalis.deviantx.UI.Adapters.MyWalletCoinsRAdapter;
import com.aequalis.deviantx.UI.Models.AccountWallet;
import com.aequalis.deviantx.UI.Models.AllCoins;
import com.aequalis.deviantx.Utilities.CONSTANTS;
import com.aequalis.deviantx.Utilities.CommonUtilities;
import com.aequalis.deviantx.Utilities.DeviantXApiClient;

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


public class DashboardFragment extends Fragment {

    View view;
    //    @BindView(R.id.viewPager)
//    ViewPager viewPager;
    @BindView(R.id.rview_wallet_coins)
    RecyclerView rview_wallet_coins;
    @BindView(R.id.lnr_wallet)
    LinearLayout lnr_wallet;
    @BindView(R.id.lnr_empty_coins)
    LinearLayout lnr_empty_coins;
    @BindView(R.id.txt_wallet_name)
    TextView txt_wallet_name;
    @BindView(R.id.txt_wallet_percentage)
    TextView txt_wallet_percentage;
    @BindView(R.id.txt_wallet_bal)
    TextView txt_wallet_bal;
    @BindView(R.id.txt_wallet_coin)
    TextView txt_wallet_coin;
//    @BindView(R.id.)
//    TextView ;


    MyWalletCoinsRAdapter myWalletCoinsRAdapter;
    LinearLayoutManager layoutManager;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;

    ArrayList<AccountWallet> accountWalletlist;
    String loginResponseData, loginResponseStatus, loginResponseMsg, str_coin_name, str_coin_code, str_coin_logo,
            str_data_address, str_data_walletName, str_data_privatekey, str_data_passcode,
            str_data_account, str_data_coin;
    int int_coin_id, int_data_id, int_coin_rank;
    Double dbl_coin_usdValue, dbl_data_balance, dbl_data_balanceInUSD, dbl_data_balanceInINR, dbl_coin_marketCap, dbl_coin_volume, dbl_coin_24h, dbl_coin_7d, dbl_coin_1m;

    String total_avail_bal = "0.0";
    boolean hideBal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.dashboard_fragment, container, false);
        ButterKnife.bind(this, view);

//        LayoutViewPagerAdapter viewPagerAdapter = new LayoutViewPagerAdapter(getActivity());
//        viewPager.setAdapter(viewPagerAdapter);

        sharedPreferences = getActivity().getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        hideBal = sharedPreferences.getBoolean(CONSTANTS.hideBal, true);

//        total_avail_bal = sharedPreferences.get(CONSTANTS.total_avail_bal, "0.0");

        accountWalletlist = new ArrayList<>();

        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rview_wallet_coins.setLayoutManager(layoutManager);
        myWalletCoinsRAdapter = new MyWalletCoinsRAdapter(getActivity(), accountWalletlist);
        rview_wallet_coins.setAdapter(myWalletCoinsRAdapter);

        if (CommonUtilities.isConnectionAvailable(getActivity())) {
//            GET Account Wallet
            fetchAccountWallet();
            if (hideBal) {
                txt_wallet_bal.setText("~$ " + total_avail_bal);
            } else {
                txt_wallet_bal.setText("~$ ***");
            }
        } else {
            CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.internetconnection));
        }


//        lnr_wallet.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // load fragment
//                Fragment fragment = new AccountListFragment();
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                FragmentTransaction transaction = fragmentManager.beginTransaction();
//                transaction.replace(R.id.frame_container, fragment);
//                transaction.addToBackStack(null);
//                transaction.commit();
//            }
//        });

        return view;
    }

    private void fetchAccountWallet() {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            progressDialog = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.please_wait), true);
            CryptoControllerApi apiService = DeviantXApiClient.getClient().create(CryptoControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.getAccountWallet(CONSTANTS.DeviantMulti + token);
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
                                if (jsonArrayData.length() == 0) {
                                    lnr_empty_coins.setVisibility(View.VISIBLE);
                                    rview_wallet_coins.setVisibility(View.GONE);
                                } else {
                                    lnr_empty_coins.setVisibility(View.GONE);
                                    rview_wallet_coins.setVisibility(View.VISIBLE);
                                    double ttl_amt;
                                    for (int i = 0; i < jsonArrayData.length(); i++) {
                                        JSONObject jsonObjectData = jsonArrayData.getJSONObject(i);
                                        try {
                                            int_data_id = jsonObjectData.getInt("id");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            str_data_address = jsonObjectData.getString("address");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            str_data_walletName = jsonObjectData.getString("walletName");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            str_data_privatekey = jsonObjectData.getString("privatekey");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            str_data_passcode = jsonObjectData.getString("passcode");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            dbl_data_balance = jsonObjectData.getDouble("balance");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            dbl_data_balanceInUSD = jsonObjectData.getDouble("balanceInUSD");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            dbl_data_balanceInINR = jsonObjectData.getDouble("balanceInINR");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            str_data_account = jsonObjectData.getString("account");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            str_data_coin = jsonObjectData.getString("coin");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        JSONObject jsonObjectCoins = new JSONObject(str_data_coin);

                                        try {
                                            int_coin_id = jsonObjectCoins.getInt("id");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            str_coin_name = jsonObjectCoins.getString("name");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            str_coin_code = jsonObjectCoins.getString("code");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            str_coin_logo = jsonObjectCoins.getString("logo");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            dbl_coin_usdValue = jsonObjectCoins.getDouble("usdValue");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            int_coin_rank = jsonObjectCoins.getInt("rank");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            dbl_coin_marketCap = jsonObjectCoins.getDouble("marketCap");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            dbl_coin_volume = jsonObjectCoins.getDouble("volume");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            dbl_coin_24h = jsonObjectCoins.getDouble("change24H");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            dbl_coin_7d = jsonObjectCoins.getDouble("change7D");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            dbl_coin_1m = jsonObjectCoins.getDouble("change1M");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        AllCoins allCoins = new AllCoins(int_coin_id, str_coin_name, str_coin_code, str_coin_logo, dbl_coin_usdValue);
                                        accountWalletlist.add(new AccountWallet(int_data_id, str_data_address, str_data_walletName,
                                                str_data_privatekey, str_data_passcode, dbl_data_balance, dbl_data_balanceInUSD,
                                                dbl_data_balanceInINR, str_data_account, allCoins));
                                    }
                                    myWalletCoinsRAdapter = new MyWalletCoinsRAdapter(getActivity(), accountWalletlist);
                                    rview_wallet_coins.setAdapter(myWalletCoinsRAdapter);

                                    Double totalBalance = 0.00;
                                    for (AccountWallet accountWallet : accountWalletlist) {
                                        totalBalance += accountWallet.getStr_data_balanceInUSD();
                                    }
                                    if (hideBal) {
                                        txt_wallet_bal.setText("~$ " + totalBalance);
                                    } else {
                                        txt_wallet_bal.setText("~$ ***");
                                    }
                                }
                            } else {
                                CommonUtilities.ShowToastMessage(getActivity(), loginResponseMsg);
                            }
                        } else {
                            CommonUtilities.ShowToastMessage(getActivity(), loginResponseMsg);
//                            Toast.makeText(getApplicationContext(), responsevalue, Toast.LENGTH_LONG).show();
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.Timeout));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Timeout), Toast.LENGTH_SHORT).show();
                    } else if (t instanceof java.net.ConnectException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.networkerror));
//                        Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.networkerror), Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception ex) {
            progressDialog.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.errortxt));
//            Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
        }
    }

}