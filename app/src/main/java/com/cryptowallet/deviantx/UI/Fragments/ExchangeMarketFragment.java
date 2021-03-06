package com.cryptowallet.deviantx.UI.Fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.ServiceAPIs.ExchangePairControllerApi;
import com.cryptowallet.deviantx.UI.Adapters.ExchangeCoinsDataPagerAdapter;
import com.cryptowallet.deviantx.UI.Adapters.GainerLoserExcDBRAdapter;
import com.cryptowallet.deviantx.UI.Interfaces.PairsListUIListener;
import com.cryptowallet.deviantx.UI.Models.CoinPairs;
import com.cryptowallet.deviantx.UI.Models.PairsList;
import com.cryptowallet.deviantx.UI.RoomDatabase.Database.DeviantXDB;
import com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB.PairsListDao;
import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.PairsListDB;
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
import rx.functions.Action1;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.client.StompClient;
import ua.naiksoftware.stomp.client.StompMessage;

import static android.support.constraint.Constraints.TAG;
import static com.cryptowallet.deviantx.Utilities.MyApplication.myApplication;

public class ExchangeMarketFragment extends Fragment {


    //    @BindView(R.id.)
//            ;
    @BindView(R.id.rview_coin)
    RecyclerView rview_coin;
    @BindView(R.id.img_search)
    ImageView img_search;
    @BindView(R.id.tab_lyt_coinsList)
    TabLayout tab_lyt_coinsList;
    @BindView(R.id.view_pager_Sup_product)
    ViewPager view_pager_Sup_product;
    /*
        @BindView(R.id.pb)
        ProgressBar pb;
    */
    @BindView(R.id.lnr_empty_gain_loose)
    LinearLayout lnr_empty_gain_loose;

    private ExchangeCoinsDataPagerAdapter exchangeCoinsDataPagerAdapter;

    //    ArrayList<String> coinsList;
    ArrayList<PairsList> allPairsList;

    View view;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    DeviantXDB deviantXDB;
    String loginResponseData, loginResponseStatus, loginResponseMsg, responseMsg, responseStatus, responseData;

/*
    private static final String TAG = "DEVIANTX";
    private StompClient stompClient;
*/

    LinearLayoutManager linearLayoutVertical;
    GainerLoserExcDBRAdapter gainerLoserExcDBRAdapter;
    public static StompClient stompClient;
    ArrayList<CoinPairs> allCoinPairs, allCoinPairsList;
    String selectedCoinName = "BTC";
    int selectedCoinPos = 0;
    ArrayList<PairsList> PairsListList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.exchange_market_fragment, container, false);
        ButterKnife.bind(this, view);
        deviantXDB = DeviantXDB.getDatabase(getActivity());

        sharedPreferences = getActivity().getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();
//        coinsList = new ArrayList<>();
        allPairsList = new ArrayList<>();
        allCoinPairs = new ArrayList<>();
        PairsListList = new ArrayList<>();
        allCoinPairsList = new ArrayList<>();

        linearLayoutVertical = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rview_coin.setLayoutManager(linearLayoutVertical);
//        gainerLoserExcDBRAdapter = new GainerLoserExcDBRAdapter(getActivity(), allCoinPairs, selectedCoinName, false, true);
//        rview_coin.setAdapter(gainerLoserExcDBRAdapter);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                onLoadPairsList();
                fetchCoinsAllPairs();
            }
        }, 200);

        try {
            stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, CommonUtilities.WS);
            stompClient.connect();
            Log.e(TAG, "*****Connected " + "*****: /topic/exchange_pair/get_all");
            allCoinPairs = new ArrayList<>();
            rview_coin.setVisibility(View.GONE);

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    stompClient.topic("/topic/exchange_pair/get_all" /*+ PairsListList.get(selectedCoinPos).getStr_Code().trim()*/).subscribe(new Action1<StompMessage>() {
                        @Override
                        public void call(StompMessage message) {
                            try {

                                allCoinPairsList = new ArrayList<>();
                                Log.e(TAG, "*****Received " /*+ PairsListList.get(selectedCoinPos).getStr_Code() */ + "*****: EMSFselectedTab" + message.getPayload());
                                CoinPairs[] coinsStringArray = GsonUtils.getInstance().fromJson(message.getPayload(), CoinPairs[].class);
                                allCoinPairs = new ArrayList<CoinPairs>(Arrays.asList(coinsStringArray));

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        for (int i = 0; i < allCoinPairs.size(); i++) {
                                            if (!allCoinPairs.get(i).getStr_pairCoin().trim().equals(allCoinPairs.get(i).getStr_exchangeCoin().trim()))
//                                                if ((allCoinPairs.get(i).getStr_pairCoin().equals("DEV") && allCoinPairs.get(i).getStr_exchangeCoin().equals("BTC")) || (allCoinPairs.get(i).getStr_pairCoin().equals("DEV") && allCoinPairs.get(i).getStr_exchangeCoin().equals("ETH")) || (allCoinPairs.get(i).getStr_pairCoin().equals("ETH") && allCoinPairs.get(i).getStr_exchangeCoin().equals("BTC")) || (allCoinPairs.get(i).getStr_pairCoin().equals("ETH") && allCoinPairs.get(i).getStr_exchangeCoin().equals("DEV")) || (allCoinPairs.get(i).getStr_pairCoin().equals("BTC") && allCoinPairs.get(i).getStr_exchangeCoin().equals("ETH")) || (allCoinPairs.get(i).getStr_pairCoin().equals("BTC") && allCoinPairs.get(i).getStr_exchangeCoin().equals("DEV")))
                                                if (allCoinPairs.get(i).getStr_exchangeCoin().trim().equals(selectedCoinName))
                                                    allCoinPairsList.add(allCoinPairs.get(i));
                                        }

                                        if (allCoinPairsList.size() > 0) {
                                            gainerLoserExcDBRAdapter = new GainerLoserExcDBRAdapter(getActivity(), allCoinPairsList, selectedCoinName, false, true);
                                            rview_coin.setAdapter(gainerLoserExcDBRAdapter);
                                            lnr_empty_gain_loose.setVisibility(View.GONE);
                                            rview_coin.setVisibility(View.VISIBLE);
                                        } else {
                                            lnr_empty_gain_loose.setVisibility(View.VISIBLE);
                                            rview_coin.setVisibility(View.GONE);
                                        }
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Errorrrrr:", e.toString());
        }

        tab_lyt_coinsList.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectedCoinPos = tab.getPosition();
                selectedCoinName = PairsListList.get(selectedCoinPos).getStr_Code();
                rview_coin.setVisibility(View.GONE);
//                pb.setVisibility(View.VISIBLE);
//                            Updating List
                updateCoinPairs(selectedCoinName);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }

    private void updateCoinPairs(String selectedCoinName) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                allCoinPairsList = new ArrayList<>();
                for (int i = 0; i < allCoinPairs.size(); i++) {
                    if (!allCoinPairs.get(i).getStr_pairCoin().trim().equals(allCoinPairs.get(i).getStr_exchangeCoin().trim()))
//                        if ((allCoinPairs.get(i).getStr_pairCoin().equals("DEV") && allCoinPairs.get(i).getStr_exchangeCoin().equals("BTC")) || (allCoinPairs.get(i).getStr_pairCoin().equals("DEV") && allCoinPairs.get(i).getStr_exchangeCoin().equals("ETH")) || (allCoinPairs.get(i).getStr_pairCoin().equals("ETH") && allCoinPairs.get(i).getStr_exchangeCoin().equals("BTC")) || (allCoinPairs.get(i).getStr_pairCoin().equals("ETH") && allCoinPairs.get(i).getStr_exchangeCoin().equals("DEV")) || (allCoinPairs.get(i).getStr_pairCoin().equals("BTC") && allCoinPairs.get(i).getStr_exchangeCoin().equals("ETH")) || (allCoinPairs.get(i).getStr_pairCoin().equals("BTC") && allCoinPairs.get(i).getStr_exchangeCoin().equals("DEV")))
                        if (allCoinPairs.get(i).getStr_exchangeCoin().trim().equals(selectedCoinName))
                            allCoinPairsList.add(allCoinPairs.get(i));
                }

                if (allCoinPairsList.size() > 0) {
                    gainerLoserExcDBRAdapter = new GainerLoserExcDBRAdapter(getActivity(), allCoinPairsList, selectedCoinName, false, true);
                    rview_coin.setAdapter(gainerLoserExcDBRAdapter);
                    lnr_empty_gain_loose.setVisibility(View.GONE);
                    rview_coin.setVisibility(View.VISIBLE);
                } else {
                    lnr_empty_gain_loose.setVisibility(View.VISIBLE);
                    rview_coin.setVisibility(View.GONE);
                }

            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        myApplication.setPairsListUIListener(PairsListUIListener);
//        myApplication.setCoinPairsUIListener(coinPairsUIListener);
//        stompClient.reconnect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myApplication.setPairsListUIListener(null);
//        myApplication.setCoinPairsUIListener(null);
//        stompClient.disconnect();
    }

    @Override
    public void onPause() {
        super.onPause();
//        stompClient.disconnect();
    }

    //    **************GETTING Pairs List**************
    private void onLoadPairsList() {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                PairsListDao PairsListDao = deviantXDB.pairsListDao();
                if ((PairsListDao.getAllPairsList()) != null) {
                    String walletResult = PairsListDao.getAllPairsList().pairsList;
                    updateUIPairsList(walletResult);
                } else {
                    if (CommonUtilities.isConnectionAvailable(getActivity())) {
                        fetchCoinsPairsList();
                    } else {
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.internetconnection));
                    }
                }
            }
        });

    }

    PairsListUIListener PairsListUIListener = new PairsListUIListener() {
        @Override
        public void onChangedPairsList(String allPairsList) {
            updateUIPairsList(allPairsList);
        }

    };

    private void updateUIPairsList(String responsevalue) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject(responsevalue);
                    loginResponseMsg = jsonObject.getString("msg");
                    loginResponseStatus = jsonObject.getString("status");

                    if (loginResponseStatus.equals("true")) {
                        loginResponseData = jsonObject.getString("data");
                        PairsList[] coinsStringArray = GsonUtils.getInstance().fromJson(loginResponseData, PairsList[].class);
                        allPairsList = new ArrayList<PairsList>(Arrays.asList(coinsStringArray));

                        PairsListList = new ArrayList<>();
                        for (PairsList coinName : allPairsList) {
                            if (coinName.getStr_Code().equals("DEV") || coinName.getStr_Code().equals("ETH") || coinName.getStr_Code().equals("BTC"))
                                PairsListList.add(coinName);
                        }

                        tab_lyt_coinsList.removeAllTabs();
                        for (int i = 0; i <= PairsListList.size(); i++) {
                            tab_lyt_coinsList.addTab(tab_lyt_coinsList.newTab().setText(PairsListList.get(i).getStr_Code()));
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

    private void fetchCoinsPairsList() {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            ExchangePairControllerApi apiService = DeviantXApiClient.getClient().create(ExchangePairControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.getPairsList(/*CONSTANTS.DeviantMulti + token*/);
            apiResponse.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responsevalue = response.body().string();

                        if (!responsevalue.isEmpty() && responsevalue != null) {
                            updateUIPairsList(responsevalue);
                            PairsListDao mDao = deviantXDB.pairsListDao();
                            PairsListDB PairsListDB = new PairsListDB(1, responsevalue);
                            mDao.insertPairsList(PairsListDB);

                        } else {
                            CommonUtilities.ShowToastMessage(getActivity(), loginResponseMsg);
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.errortxt));
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.Timeout));
                    } else if (t instanceof java.net.ConnectException) {
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.networkerror));
                    } else {
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.errortxt));
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.errortxt));
        }

    }


    //    **************GETTING ALL PAIRS**************
    private void fetchCoinsAllPairs() {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            ExchangePairControllerApi apiService = DeviantXApiClient.getClient().create(ExchangePairControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.getAllPairs(/*CONSTANTS.DeviantMulti + token*/);
            apiResponse.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responsevalue = response.body().string();
                        if (!responsevalue.isEmpty() && responsevalue != null) {

                            JSONObject jsonObject = new JSONObject(responsevalue);
                            responseMsg = jsonObject.getString("msg");
                            responseStatus = jsonObject.getString("status");

                            if (responseStatus.equals("true")) {
                                responseData = jsonObject.getString("data");
                                allCoinPairsList = new ArrayList<>();
                                CoinPairs[] coinsStringArray = GsonUtils.getInstance().fromJson(responseData, CoinPairs[].class);
                                allCoinPairs = new ArrayList<CoinPairs>(Arrays.asList(coinsStringArray));
                                for (int i = 0; i < allCoinPairs.size(); i++) {
                                    if (!allCoinPairs.get(i).getStr_pairCoin().trim().equals(allCoinPairs.get(i).getStr_exchangeCoin().trim()))
//                                        if ((allCoinPairs.get(i).getStr_pairCoin().equals("DEV") && allCoinPairs.get(i).getStr_exchangeCoin().equals("BTC")) || (allCoinPairs.get(i).getStr_pairCoin().equals("DEV") && allCoinPairs.get(i).getStr_exchangeCoin().equals("ETH")) || (allCoinPairs.get(i).getStr_pairCoin().equals("ETH") && allCoinPairs.get(i).getStr_exchangeCoin().equals("BTC")) || (allCoinPairs.get(i).getStr_pairCoin().equals("ETH") && allCoinPairs.get(i).getStr_exchangeCoin().equals("DEV")) || (allCoinPairs.get(i).getStr_pairCoin().equals("BTC") && allCoinPairs.get(i).getStr_exchangeCoin().equals("ETH")) || (allCoinPairs.get(i).getStr_pairCoin().equals("BTC") && allCoinPairs.get(i).getStr_exchangeCoin().equals("DEV")))
                                        if (allCoinPairs.get(i).getStr_exchangeCoin().trim().equals(selectedCoinName))
                                            allCoinPairsList.add(allCoinPairs.get(i));
                                }
                                if (allCoinPairsList.size() > 0) {
                                    gainerLoserExcDBRAdapter = new GainerLoserExcDBRAdapter(getActivity(), allCoinPairsList, selectedCoinName, false, true);
                                    rview_coin.setAdapter(gainerLoserExcDBRAdapter);
                                    lnr_empty_gain_loose.setVisibility(View.GONE);
                                    rview_coin.setVisibility(View.VISIBLE);
                                } else {
                                    lnr_empty_gain_loose.setVisibility(View.VISIBLE);
                                    rview_coin.setVisibility(View.GONE);
                                }
                            } else {
                                lnr_empty_gain_loose.setVisibility(View.VISIBLE);
                                rview_coin.setVisibility(View.GONE);
                                CommonUtilities.ShowToastMessage(getActivity(), loginResponseMsg);
                            }

                        } else {
                            CommonUtilities.ShowToastMessage(getActivity(), loginResponseMsg);
//                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.errortxt));
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.Timeout));
                    } else if (t instanceof java.net.ConnectException) {
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.networkerror));
                    } else {
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.errortxt));
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.errortxt));
        }

    }


}
