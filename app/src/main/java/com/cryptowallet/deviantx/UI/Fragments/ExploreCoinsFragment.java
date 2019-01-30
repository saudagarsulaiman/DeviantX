package com.cryptowallet.deviantx.UI.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.ServiceAPIs.CoinsControllerApi;
import com.cryptowallet.deviantx.UI.Adapters.ExploreCoinsRAdapter;
import com.cryptowallet.deviantx.UI.Interfaces.AllCoinsUIListener;
import com.cryptowallet.deviantx.UI.Models.AllCoins;
import com.cryptowallet.deviantx.UI.RoomDatabase.Database.DeviantXDB;
import com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB.ExploreCoinsDao;
import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.ExploreCoinsDB;
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

public class ExploreCoinsFragment extends Fragment {

    View view;
    @BindView(R.id.rview_all_coins)
    RecyclerView rview_all_coins;
    @BindView(R.id.edt_search)
    EditText edt_search;

    ExploreCoinsRAdapter allCoinsRAdapter;
    LinearLayoutManager layoutManager;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;

    int int_coin_id, int_coin_rank;
    Double dbl_coin_usdValue, dbl_coin_marketCap, dbl_coin_volume, dbl_coin_24h, dbl_coin_7d, dbl_coin_1m;
    String loginResponseData, loginResponseStatus, loginResponseMsg, str_coin_name, str_coin_code, str_coin_logo, str_coin_chart_data;
    ArrayList<AllCoins> allCoinsList;
    DeviantXDB deviantXDB;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.explore_coins_fragment, container, false);
        ButterKnife.bind(this, view);

        deviantXDB = DeviantXDB.getDatabase(getActivity());

        sharedPreferences = getActivity().getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        allCoinsList = new ArrayList<>();

        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rview_all_coins.setLayoutManager(layoutManager);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                onLoadAllCoins();
            }
        }, 150);


        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ArrayList<AllCoins> searchCoinsList = new ArrayList<>();
                for (AllCoins coinName : allCoinsList) {
                    if (coinName.getStr_coin_name().toLowerCase().contains(s.toString().toLowerCase())) {
                        searchCoinsList.add(coinName);
                    }
                }
                allCoinsRAdapter = new ExploreCoinsRAdapter(getActivity(), searchCoinsList);
                rview_all_coins.setAdapter(allCoinsRAdapter);
            }
        });

        return view;
    }

    private void onLoadAllCoins() {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ExploreCoinsDao exploreCoinsDao = deviantXDB.exploreCoinsDao();
                if ((exploreCoinsDao.getExploreCoins()) != null) {
                    String walletResult = exploreCoinsDao.getExploreCoins().exploreCoins;
                    updateUI(walletResult);
                } else {
                    if (CommonUtilities.isConnectionAvailable(getActivity())) {
                        fetchCoins();
                    } else {
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.internetconnection));
                    }
                }
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        myApplication.setAllCoinsUIListener(allCoinsUIListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myApplication.setAllCoinsUIListener(null);
    }

    AllCoinsUIListener allCoinsUIListener = new AllCoinsUIListener() {
        @Override
        public void onChangedAllCoins(String allCoinsList) {
            updateUI(allCoinsList);
        }
    };


    private void updateUI(String responsevalue) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject(responsevalue);
                    loginResponseMsg = jsonObject.getString("msg");
                    loginResponseStatus = jsonObject.getString("status");

                    if (loginResponseStatus.equals("true")) {
                        loginResponseData = jsonObject.getString("data");
                        AllCoins[] coinsStringArray = GsonUtils.getInstance().fromJson(loginResponseData, AllCoins[].class);
                        allCoinsList = new ArrayList<AllCoins>(Arrays.asList(coinsStringArray));
                       /* for (int i = 0; i < jsonArrayData.length(); i++) {
                            JSONObject jsonObjectCoins = jsonArrayData.getJSONObject(i);

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
                            try {
                                str_coin_chart_data = jsonObjectCoins.getString("chartData");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            allCoinsList.add(new AllCoinsDB(int_coin_id, str_coin_name, str_coin_code, str_coin_logo, dbl_coin_usdValue, int_coin_rank, dbl_coin_marketCap, dbl_coin_volume, dbl_coin_24h, dbl_coin_7d, dbl_coin_1m, false, str_coin_chart_data));
                        }*/
                        if (!edt_search.getText().toString().isEmpty()) {
                            ArrayList<AllCoins> searchCoinsList = new ArrayList<>();
                            for (AllCoins coinName : allCoinsList) {
                                if (coinName.getStr_coin_name().toLowerCase().contains(edt_search.getText().toString().toLowerCase())) {
                                    searchCoinsList.add(coinName);
                                }
                            }
                            allCoinsRAdapter = new ExploreCoinsRAdapter(getActivity(), searchCoinsList);
                            rview_all_coins.setAdapter(allCoinsRAdapter);
                        } else {
                            allCoinsRAdapter = new ExploreCoinsRAdapter(getActivity(), allCoinsList);
                            rview_all_coins.setAdapter(allCoinsRAdapter);
                        }

                    } else {
                        CommonUtilities.ShowToastMessage(getActivity(), loginResponseMsg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void fetchCoins() {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            progressDialog = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.please_wait), true);
            CoinsControllerApi apiService = DeviantXApiClient.getClient().create(CoinsControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.getAllCoins(CONSTANTS.DeviantMulti + token);
            apiResponse.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responsevalue = response.body().string();

                        if (!responsevalue.isEmpty() && responsevalue != null) {
                            progressDialog.dismiss();
                            updateUI(responsevalue);
                            ExploreCoinsDao mDao = deviantXDB.exploreCoinsDao();
                            ExploreCoinsDB exploreCoinsDB = new ExploreCoinsDB(1, responsevalue);
                            mDao.insertAllCoins(exploreCoinsDB);

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
                        Toast.makeText(getActivity(), getResources().getString(R.string.networkerror), Toast.LENGTH_SHORT).show();
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
