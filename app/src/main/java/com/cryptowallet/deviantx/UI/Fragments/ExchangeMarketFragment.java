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
import android.widget.ProgressBar;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.ServiceAPIs.ExchangePairControllerApi;
import com.cryptowallet.deviantx.UI.Adapters.ExchangeCoinsDataPagerAdapter;
import com.cryptowallet.deviantx.UI.Adapters.GainerLoserExcDBRAdapter;
import com.cryptowallet.deviantx.UI.Interfaces.CoinPairsUIListener;
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
import rx.Scheduler;
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
    @BindView(R.id.pb)
    ProgressBar pb;

    private ExchangeCoinsDataPagerAdapter exchangeCoinsDataPagerAdapter;

    //    ArrayList<String> coinsList;
    ArrayList<PairsList> allPairsList;

    View view;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    DeviantXDB deviantXDB;
    String loginResponseData, loginResponseStatus, loginResponseMsg;

/*
    private static final String TAG = "DEVIANTX";
    private StompClient stompClient;
*/

    LinearLayoutManager linearLayoutVertical;
    GainerLoserExcDBRAdapter gainerLoserExcDBRAdapter;
    private StompClient stompClient;
    ArrayList<CoinPairs> allCoinPairs;
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

        linearLayoutVertical = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rview_coin.setLayoutManager(linearLayoutVertical);

/*
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://142.93.51.57:3323/deviant/websocket");
        stompClient.connect();

        stompClient.topic("/topic/exchange_pair/BTC").subscribe(new Action1<StompMessage>() {
            @Override
            public void call(StompMessage message) {
                Log.e(TAG, "*****Received BTC*****: " + message.getPayload());
            }
        });
        stompClient.topic("/topic/exchange_pair/DEV").subscribe(new Action1<StompMessage>() {
            @Override
            public void call(StompMessage message) {
                Log.e(TAG, "*****Received DEV*****: " + message.getPayload());
            }
        });
*/
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                onLoadPairsList();
            }
        }, 200);
        gainerLoserExcDBRAdapter = new GainerLoserExcDBRAdapter(getActivity(), allCoinPairs, selectedCoinName, false, true);
        rview_coin.setAdapter(gainerLoserExcDBRAdapter);


      /*  Intent serviceIntent = new Intent(getActivity(), CoinPairsFetch.class);
        serviceIntent.putExtra(CONSTANTS.selectedCoinName, selectedCoinName);
        getActivity().startService(serviceIntent);*/


       /* setupViewPager(view_pager_Sup_product);
        tab_lyt_coinsList.setupWithViewPager(view_pager_Sup_product);
*/
        ArrayList<String> list = new ArrayList<>();

        tab_lyt_coinsList.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectedCoinPos = tab.getPosition();
                selectedCoinName = PairsListList.get(selectedCoinPos).getStr_Code();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://142.93.51.57:3323/deviant/websocket");
                            stompClient.connect();
                            allCoinPairs = new ArrayList<>();
                            rview_coin.setVisibility(View.GONE);
                            pb.setVisibility(View.VISIBLE);

                          /*  if (list.size() > 0) {
                                for (int i = 0; i < list.size(); i++){
                                    if (selectedCoinName.equals(list.get(i))){
                                        stompClient.topic("/topic/exchange_pair/" + PairsListList.get(selectedCoinPos).getStr_Code()).unsubscribeOn(new Scheduler() {
                                            @Override
                                            public Worker createWorker() {
                                                return null;
                                            }
                                        });
                                    }*//*else {

                                    }*//*
                                }
                            } else {

                            }*/


                            /*stompClient.lifecycle().unsubscribeOn(new Scheduler() {
                                @Override
                                public Worker createWorker() {
                                    return null;
                                }
                            });*/


                            stompClient.topic("/topic/exchange_pair/" + PairsListList.get(selectedCoinPos).getStr_Code()).subscribe(new Action1<StompMessage>() {
                                @Override
                                public void call(StompMessage message) {
//                            rview_coin.setVisibility(View.GONE);
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                Log.e(TAG, "*****Received " + PairsListList.get(selectedCoinPos).getStr_Code() + "*****: EMSFselectedTab" + message.getPayload());
                                                CoinPairs[] coinsStringArray = GsonUtils.getInstance().fromJson(message.getPayload(), CoinPairs[].class);
                                                allCoinPairs = new ArrayList<CoinPairs>(Arrays.asList(coinsStringArray));
//                            stompClient.disconnect();

                                                gainerLoserExcDBRAdapter = new GainerLoserExcDBRAdapter(getActivity(), allCoinPairs, selectedCoinName, false, true);
                                                rview_coin.setAdapter(gainerLoserExcDBRAdapter);
//                            gainerLoserExcDBRAdapter.notifyDataSetChanged();
                                                pb.setVisibility(View.GONE);
                                                rview_coin.setVisibility(View.VISIBLE);
//                                    stompClient.disconnect();
//                            stompClient.lifecycle()
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });

                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
/*
                Intent serviceIntent = new Intent(getActivity(), CoinPairsFetch.class);
                serviceIntent.putExtra(CONSTANTS.selectedCoinName, selectedCoinName);
                getActivity().startService(serviceIntent);
*/

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
//                allCoinPairs = new ArrayList<>();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        exchangeCoinsDataPagerAdapter = new ExchangeCoinsDataPagerAdapter(getChildFragmentManager());
        for (int i = 0; i < allPairsList.size(); i++) {
            Bundle bundle = new Bundle();
            bundle.putString("title", allPairsList.get(i).getStr_Code());
            ExchangeMarketSubFragment fragment = new ExchangeMarketSubFragment();
            fragment.setArguments(bundle);
            exchangeCoinsDataPagerAdapter.addFrag(fragment, allPairsList.get(i).getStr_Code());
/*
            Intent serviceIntent = new Intent(getActivity(), CoinPairsFetch.class);
            serviceIntent.putExtra(CONSTANTS.selectedCoinName, coinsList.get(i));
            getActivity().startService(serviceIntent);
*/
        }
        viewPager.setAdapter(exchangeCoinsDataPagerAdapter);

/*
        Intent serviceIntent = new Intent(getActivity(), CoinPairsFetch.class);
        serviceIntent.putExtra(CONSTANTS.selectedCoinName, "");
        getActivity().startService(serviceIntent);
*/

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
                            PairsListList.add(coinName);
                        }

                        tab_lyt_coinsList.removeAllTabs();
                        for (int i = 0; i <= PairsListList.size(); i++) {
                            tab_lyt_coinsList.addTab(tab_lyt_coinsList.newTab().setText(PairsListList.get(i).getStr_Code()));
                        }

/*
                        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://142.93.51.57:3323/deviant/websocket");
                        stompClient.connect();

                        stompClient.topic("/topic/exchange_pair/" + PairsListList.get(selectedCoinPos).getStr_Code()).subscribe(new Action1<StompMessage>() {
                            @Override
                            public void call(StompMessage message) {
                                Log.e(TAG, "*****Received " + PairsListList.get(selectedCoinPos).getStr_Code() + "*****: EMSFselectedTab" + message.getPayload());
                                CoinPairs[] coinsStringArray = GsonUtils.getInstance().fromJson(message.getPayload(), CoinPairs[].class);
                                allCoinPairs = new ArrayList<CoinPairs>(Arrays.asList(coinsStringArray));
                                gainerLoserExcDBRAdapter = new GainerLoserExcDBRAdapter(getActivity(), allCoinPairs, selectedCoinName, false, true);
                                rview_coin.setAdapter(gainerLoserExcDBRAdapter);
                            }
                        });
*/

/*
                        if (PairsListList.size() > 0) {
*/
/*
                            setupViewPager(view_pager_Sup_product);
                            tab_lyt_coinsList.setupWithViewPager(view_pager_Sup_product);
*//*

                        } else {
*/
/*
                            setupViewPager(view_pager_Sup_product);
                            tab_lyt_coinsList.setupWithViewPager(view_pager_Sup_product);
*//*
                        }
*/
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
//            progressDialog = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.please_wait), true);
            ExchangePairControllerApi apiService = DeviantXApiClient.getClient().create(ExchangePairControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.getPairsList(/*CONSTANTS.DeviantMulti + token*/);
            apiResponse.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responsevalue = response.body().string();
//                        progressDialog.dismiss();

                        if (!responsevalue.isEmpty() && responsevalue != null) {
                            updateUIPairsList(responsevalue);
//                            progressDialog.dismiss();
                            PairsListDao mDao = deviantXDB.pairsListDao();
                            PairsListDB PairsListDB = new PairsListDB(1, responsevalue);
                            mDao.insertPairsList(PairsListDB);

                        } else {
                            CommonUtilities.ShowToastMessage(getActivity(), loginResponseMsg);
//                            Toast.makeText(getApplicationContext(), responsevalue, Toast.LENGTH_LONG).show();
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
//                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
//                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.Timeout));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Timeout), Toast.LENGTH_SHORT).show();
                    } else if (t instanceof java.net.ConnectException) {
//                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.networkerror));
                    } else {
//                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception ex) {
//            progressDialog.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.errortxt));
//            Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
        }

    }

    //    **************GETTING DATA**************
    CoinPairsUIListener coinPairsUIListener = new CoinPairsUIListener() {
        @Override
        public void onChangedCoinPairs(String selectedCoinName, ArrayList<CoinPairs> coinPairs) {
//            setData(selectedCoinPos, selectedCoinName);
            gainerLoserExcDBRAdapter = new GainerLoserExcDBRAdapter(getActivity(), coinPairs, selectedCoinName, false, true);
            rview_coin.setAdapter(gainerLoserExcDBRAdapter);
        }
    };

//    private void setData(int selectedCoinPos, String selectedCoinName) {
//
//    }


}
