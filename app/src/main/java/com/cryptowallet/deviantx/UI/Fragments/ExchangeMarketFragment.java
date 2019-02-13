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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.ServiceAPIs.ExchangePairControllerApi;
import com.cryptowallet.deviantx.UI.Adapters.ExchangeCoinsDataPagerAdapter;
import com.cryptowallet.deviantx.UI.Interfaces.PairsListUIListener;
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

import static com.cryptowallet.deviantx.Utilities.MyApplication.myApplication;

public class ExchangeMarketFragment extends Fragment {


    //    @BindView(R.id.)
//            ;
    @BindView(R.id.img_search)
    ImageView img_search;
    @BindView(R.id.tab_lyt_coinsList)
    TabLayout tab_lyt_coinsList;
    @BindView(R.id.view_pager_Sup_product)
    ViewPager view_pager_Sup_product;

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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.exchange_market_fragment, container, false);
        ButterKnife.bind(this, view);
        deviantXDB = DeviantXDB.getDatabase(getActivity());

        sharedPreferences = getActivity().getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();
//        coinsList = new ArrayList<>();
        allPairsList = new ArrayList<>();

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

//        coinsList.add("Favorites");
//        coinsList.add("DEV");
//        coinsList.add("BTC");
//        coinsList.add("ETH");
//        coinsList.add("BTCP");
//        coinsList.add("USDT");
//        coinsList.add("LTC");
//        coinsList.add("NEM");

       /* setupViewPager(view_pager_Sup_product);
        tab_lyt_coinsList.setupWithViewPager(view_pager_Sup_product);
*/
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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myApplication.setPairsListUIListener(null);
    }


    //    **************GETTING AIRDROPS HISTORY**************
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

                        ArrayList<PairsList> PairsListList = new ArrayList<>();
                        for (PairsList coinName : allPairsList) {
                            PairsListList.add(coinName);
                        }
                        if (PairsListList.size() > 0) {
                          /*  txt_radh_viewAll.setVisibility(View.VISIBLE);
                            lnr_empty_his_list.setVisibility(View.GONE);
                            recentADHistoryRAdapter = new RecentADHistoryRAdapter(getActivity(), PairsListList, false);
                            rview_radh_coins.setAdapter(recentADHistoryRAdapter);*/
                            setupViewPager(view_pager_Sup_product);
                            tab_lyt_coinsList.setupWithViewPager(view_pager_Sup_product);
                        } else {
/*
                            txt_radh_viewAll.setVisibility(View.GONE);
                            lnr_empty_his_list.setVisibility(View.VISIBLE);
*/
                            setupViewPager(view_pager_Sup_product);
                            tab_lyt_coinsList.setupWithViewPager(view_pager_Sup_product);
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

}
