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
import android.widget.LinearLayout;
import android.widget.TextView;

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
    @BindView(R.id.lnr_coins)
    LinearLayout lnr_coins;
    @BindView(R.id.lnr_tokens)
    LinearLayout lnr_tokens;
    @BindView(R.id.lnr_empty_coins)
    LinearLayout lnr_empty_coins;
    @BindView(R.id.lnr_empty_tokens)
    LinearLayout lnr_empty_tokens;
    @BindView(R.id.rview_all)
    RecyclerView rview_all;
    @BindView(R.id.lnr_search)
    LinearLayout lnr_search;
    @BindView(R.id.edt_search)
    EditText edt_search;
    @BindView(R.id.txt_coins_lbl)
    TextView txt_coins_lbl;
    @BindView(R.id.txt_tokens_lbl)
    TextView txt_tokens_lbl;


    ExploreCoinsRAdapter allCoinsRAdapter;
    LinearLayoutManager layoutManager;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;

    String loginResponseData, loginResponseStatus, loginResponseMsg;
    ArrayList<AllCoins> allList, allCoinsList, allTokensList;
    boolean isCoins = true, isTokens = false;
    DeviantXDB deviantXDB;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.explore_coins_fragment, container, false);
        ButterKnife.bind(this, view);

        deviantXDB = DeviantXDB.getDatabase(getActivity());

        sharedPreferences = getActivity().getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        allList = new ArrayList<>();
        allCoinsList = new ArrayList<>();
        allTokensList = new ArrayList<>();


        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rview_all.setLayoutManager(layoutManager);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                onLoadAllCoins();
            }
        }, 150);


        lnr_coins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnr_coins.setBackground(getResources().getDrawable(R.drawable.rec_dash_white_trans_c5));
                txt_coins_lbl.setTextColor(getResources().getColor(R.color.white));
                lnr_tokens.setBackground(getResources().getDrawable(R.drawable.rec_dash_grey_trans_c5));
                txt_tokens_lbl.setTextColor(getResources().getColor(R.color.grey));

                isCoins = true;
                isTokens = false;

                if (allCoinsList.size() > 0) {
                    rview_all.setVisibility(View.VISIBLE);
                    lnr_empty_coins.setVisibility(View.GONE);
                    lnr_empty_tokens.setVisibility(View.GONE);
                    allCoinsRAdapter = new ExploreCoinsRAdapter(getActivity(), allCoinsList);
                    rview_all.setAdapter(allCoinsRAdapter);
                } else {
                    rview_all.setVisibility(View.GONE);
                    lnr_empty_coins.setVisibility(View.VISIBLE);
                    lnr_empty_tokens.setVisibility(View.GONE);
                }
            }
        });

        lnr_tokens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnr_tokens.setBackground(getResources().getDrawable(R.drawable.rec_dash_white_trans_c5));
                txt_tokens_lbl.setTextColor(getResources().getColor(R.color.white));
                lnr_coins.setBackground(getResources().getDrawable(R.drawable.rec_dash_grey_trans_c5));
                txt_coins_lbl.setTextColor(getResources().getColor(R.color.grey));

                isCoins = false;
                isTokens = true;

                if (allTokensList.size() > 0) {
                    rview_all.setVisibility(View.VISIBLE);
                    lnr_empty_coins.setVisibility(View.GONE);
                    lnr_empty_tokens.setVisibility(View.GONE);
                    allCoinsRAdapter = new ExploreCoinsRAdapter(getActivity(), allTokensList);
                    rview_all.setAdapter(allCoinsRAdapter);
                } else {
                    rview_all.setVisibility(View.GONE);
                    lnr_empty_coins.setVisibility(View.GONE);
                    lnr_empty_tokens.setVisibility(View.VISIBLE);
                }
            }
        });

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isCoins) {
                    ArrayList<AllCoins> searchCoinsList = new ArrayList<>();
                    for (AllCoins coinName : allCoinsList) {
                        if (coinName.getStr_coin_name().toLowerCase().contains(s.toString().toLowerCase())) {
                            searchCoinsList.add(coinName);
                        }
                    }
                    allCoinsRAdapter = new ExploreCoinsRAdapter(getActivity(), searchCoinsList);
                    rview_all.setAdapter(allCoinsRAdapter);
                } else if (isTokens) {
                    ArrayList<AllCoins> searchCoinsList = new ArrayList<>();
                    for (AllCoins coinName : allTokensList) {
                        if (coinName.getStr_coin_name().toLowerCase().contains(s.toString().toLowerCase())) {
                            searchCoinsList.add(coinName);
                        }
                    }
                    allCoinsRAdapter = new ExploreCoinsRAdapter(getActivity(), searchCoinsList);
                    rview_all.setAdapter(allCoinsRAdapter);
                } /*else {
                    ArrayList<AllCoins> searchCoinsList = new ArrayList<>();
                    for (AllCoins coinName : allList) {
                        if (coinName.getStr_coin_name().toLowerCase().contains(s.toString().toLowerCase())) {
                            searchCoinsList.add(coinName);
                        }
                    }
                    allCoinsRAdapter = new ExploreCoinsRAdapter(getActivity(), searchCoinsList);
                    rview_all.setAdapter(allCoinsRAdapter);
                }*/
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
        public void onChangedAllCoins(String allList) {
            updateUI(allList);
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
                        allList = new ArrayList<AllCoins>(Arrays.asList(coinsStringArray));
                        if (allList.size() > 0) {
                            lnr_search.setVisibility(View.VISIBLE);
                            rview_all.setVisibility(View.VISIBLE);
                            lnr_empty_coins.setVisibility(View.GONE);
                            lnr_empty_tokens.setVisibility(View.GONE);

                            allCoinsList = new ArrayList<>();
                            allTokensList = new ArrayList<>();
                            for (AllCoins coinName : allList) {
                                if (coinName.isBool_isToken()) {
                                    allTokensList.add(coinName);
                                } else {
                                    allCoinsList.add(coinName);
                                }
                            }
                            if (isCoins) {
                                if (allCoinsList.size() > 0) {
                                    rview_all.setVisibility(View.VISIBLE);
                                    lnr_empty_coins.setVisibility(View.GONE);
                                    lnr_empty_tokens.setVisibility(View.GONE);
                                    allCoinsRAdapter = new ExploreCoinsRAdapter(getActivity(), allCoinsList);
                                    rview_all.setAdapter(allCoinsRAdapter);
                                } else {
                                    rview_all.setVisibility(View.GONE);
                                    lnr_empty_coins.setVisibility(View.VISIBLE);
                                    lnr_empty_tokens.setVisibility(View.GONE);

                                }
                            }

                            if (isTokens) {
                                if (allTokensList.size() > 0) {
                                    rview_all.setVisibility(View.VISIBLE);
                                    lnr_empty_coins.setVisibility(View.GONE);
                                    lnr_empty_tokens.setVisibility(View.GONE);
                                    allCoinsRAdapter = new ExploreCoinsRAdapter(getActivity(), allTokensList);
                                    rview_all.setAdapter(allCoinsRAdapter);
                                } else {
                                    rview_all.setVisibility(View.GONE);
                                    lnr_empty_coins.setVisibility(View.GONE);
                                    lnr_empty_tokens.setVisibility(View.VISIBLE);
                                }
                            }

                           /* if (!edt_search.getText().toString().isEmpty()) {
                                ArrayList<AllCoins> searchCoinsList = new ArrayList<>();
                                for (AllCoins coinName : allList) {
                                    if (coinName.getStr_coin_name().toLowerCase().contains(edt_search.getText().toString().toLowerCase())) {
                                        searchCoinsList.add(coinName);
                                    }
                                }
                                allCoinsRAdapter = new ExploreCoinsRAdapter(getActivity(), searchCoinsList);
                                rview_all.setAdapter(allCoinsRAdapter);
                            } else {
                                allCoinsRAdapter = new ExploreCoinsRAdapter(getActivity(), allList);
                                rview_all.setAdapter(allCoinsRAdapter);
                            }*/

                        } else {
                            lnr_search.setVisibility(View.GONE);
                            rview_all.setVisibility(View.GONE);
                            if (isCoins) {
                                lnr_empty_coins.setVisibility(View.VISIBLE);
                                lnr_empty_tokens.setVisibility(View.GONE);
                            } else if (isTokens) {
                                lnr_empty_tokens.setVisibility(View.VISIBLE);
                                lnr_empty_coins.setVisibility(View.GONE);
                            }
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
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.errortxt));
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.Timeout));
                    } else if (t instanceof java.net.ConnectException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.networkerror));
                    } else {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.errortxt));
                    }
                }
            });
        } catch (Exception ex) {
            progressDialog.dismiss();
            CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.errortxt));
        }

    }


}
