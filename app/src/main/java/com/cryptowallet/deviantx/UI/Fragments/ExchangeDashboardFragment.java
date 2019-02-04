package com.cryptowallet.deviantx.UI.Fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.ServiceAPIs.CoinsControllerApi;
import com.cryptowallet.deviantx.ServiceAPIs.HeaderPanelControllerApi;
import com.cryptowallet.deviantx.ServiceAPIs.NewsPanelControllerApi;
import com.cryptowallet.deviantx.UI.Adapters.ExchangeDashboardSlideAdapter;
import com.cryptowallet.deviantx.UI.Adapters.FeaturedCoinsExcDBRAdapter;
import com.cryptowallet.deviantx.UI.Adapters.GainerLoserExcDBRAdapter;
import com.cryptowallet.deviantx.UI.Adapters.NewsExcDBRAdapter;
import com.cryptowallet.deviantx.UI.Interfaces.AllCoinsUIListener;
import com.cryptowallet.deviantx.UI.Interfaces.HeaderBannerUIListener;
import com.cryptowallet.deviantx.UI.Interfaces.NewsDXUIListener;
import com.cryptowallet.deviantx.UI.Models.AllCoins;
import com.cryptowallet.deviantx.UI.Models.HeaderBanner;
import com.cryptowallet.deviantx.UI.Models.NewsDX;
import com.cryptowallet.deviantx.UI.RoomDatabase.Database.DeviantXDB;
import com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB.ExploreCoinsDao;
import com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB.HeaderBannerDao;
import com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB.NewsDXDao;
import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.ExploreCoinsDB;
import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.HeaderBannerDB;
import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.NewsDXDB;
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

public class ExchangeDashboardFragment extends Fragment /*implements DiscreteScrollView.OnItemChangedListener<RecyclerView.ViewHolder> */ {


    View view;
    ExchangeDashboardSlideAdapter dashboardSlideAdapter;

    @BindView(R.id.item_picker)
    DiscreteScrollView itemPicker;
    @BindView(R.id.img_user)
    ImageView img_user;
    @BindView(R.id.img_support)
    ImageView img_support;
    @BindView(R.id.img_search)
    ImageView img_search;
    @BindView(R.id.rview_featured_coins)
    RecyclerView rview_featured_coins;
    @BindView(R.id.rview_devx_news)
    RecyclerView rview_devx_news;
    @BindView(R.id.rview_gain_loose)
    RecyclerView rview_gain_loose;
    //    @BindView(R.id.magic_indicator)
//    MagicIndicator magic_indicator;
    @BindView(R.id.img_gainers)
    ImageView img_gainers;
    @BindView(R.id.txt_gainers)
    TextView txt_gainers;
    @BindView(R.id.rltv_gainers)
    RelativeLayout rltv_gainers;
    @BindView(R.id.rltv_gainers_view)
    RelativeLayout rltv_gainers_view;
    @BindView(R.id.img_losers)
    ImageView img_losers;
    @BindView(R.id.txt_losers)
    TextView txt_losers;
    @BindView(R.id.rltv_losers)
    RelativeLayout rltv_losers;
    @BindView(R.id.rltv_losers_view)
    RelativeLayout rltv_losers_view;
    @BindView(R.id.lnr_empty_news)
    LinearLayout lnr_empty_news;
    @BindView(R.id.lnr_empty_feat_coins)
    LinearLayout lnr_empty_feat_coins;
    @BindView(R.id.lnr_empty_headers)
    LinearLayout lnr_empty_headers;

//    @BindView(R.id.)
//    ;

    LinearLayoutManager linearLayoutHorizantal, linearLayoutHorizantal1, linearLayoutVertical;
    FeaturedCoinsExcDBRAdapter featuredCoinsExcDBRAdapter;
    NewsExcDBRAdapter newsExcDBRAdapter;
    GainerLoserExcDBRAdapter gainerLoserExcDBRAdapter;


    ArrayList<String> gainersLoserList, gainersList, loosersList;


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ArrayList<HeaderBanner> allHeaderBanner;
    ArrayList<NewsDX> allNewsDX;
    ArrayList<AllCoins> allCoinsList;
    String loginResponseData, loginResponseStatus, loginResponseMsg;

    DeviantXDB deviantXDB;


    @Override
    public void onResume() {
        super.onResume();
        myApplication.setHeaderBannerUIListener(headerBannerUIListener);
        myApplication.setNewsDXUIListener(newsDXUIListener);
        myApplication.setAllCoinsUIListener(allCoinsUIListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myApplication.setHeaderBannerUIListener(null);
        myApplication.setNewsDXUIListener(null);
        myApplication.setAllCoinsUIListener(null);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.exchange_dashboard_fragment, container, false);
        ButterKnife.bind(this, view);
        deviantXDB = DeviantXDB.getDatabase(getActivity());

        sharedPreferences = getActivity().getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();


        linearLayoutHorizantal = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        linearLayoutHorizantal1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        linearLayoutVertical = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        rview_featured_coins.setLayoutManager(linearLayoutHorizantal);
        rview_devx_news.setLayoutManager(linearLayoutHorizantal1);
        rview_gain_loose.setLayoutManager(linearLayoutVertical);

        gainersLoserList = new ArrayList<>();
        gainersList = new ArrayList<>();
        loosersList = new ArrayList<>();

        allHeaderBanner = new ArrayList<>();
        allNewsDX = new ArrayList<>();
        allCoinsList = new ArrayList<>();


//        dashboardSlideAdapter = new ExchangeDashboardSlideAdapter(getActivity());
//        itemPicker.setOrientation(DSVOrientation.HORIZONTAL);
////        itemPicker.addOnItemChangedListener(this);
//        itemPicker.addOnItemChangedListener(this);
//        itemPicker.setAdapter(dashboardSlideAdapter);
//        itemPicker.setItemTransitionTimeMillis(150);
//        itemPicker.setItemTransformer(new ScaleTransformer.Builder()
//                .setMinScale(0.8f)
//                .build());
//        itemPicker.scrollToPosition(1);

//        featuredCoinsExcDBRAdapter = new FeaturedCoinsExcDBRAdapter(getActivity(), featuredCoinsList);
//        rview_featured_coins.setAdapter(featuredCoinsExcDBRAdapter);

        newsExcDBRAdapter = new NewsExcDBRAdapter(getActivity(), allNewsDX);
        rview_devx_news.setAdapter(newsExcDBRAdapter);

        gainerLoserExcDBRAdapter = new GainerLoserExcDBRAdapter(getActivity(), gainersList, " ", true, false);
        rview_gain_loose.setAdapter(gainerLoserExcDBRAdapter);

        rltv_gainers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_gainers.setImageDrawable(getResources().getDrawable(R.drawable.graph_up_selected));
                txt_gainers.setTextColor(getResources().getColor(R.color.yellow));
                rltv_gainers_view.setVisibility(View.VISIBLE);
                img_losers.setImageDrawable(getResources().getDrawable(R.drawable.graph_down_unselected));
                txt_losers.setTextColor(getResources().getColor(R.color.white));
                rltv_losers_view.setVisibility(View.GONE);

                gainerLoserExcDBRAdapter = new GainerLoserExcDBRAdapter(getActivity(), gainersList, " ", true, false);
                rview_gain_loose.setAdapter(gainerLoserExcDBRAdapter);
                gainerLoserExcDBRAdapter.notifyDataSetChanged();
            }
        });

        rltv_losers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_losers.setImageDrawable(getResources().getDrawable(R.drawable.graph_down_selected));
                txt_losers.setTextColor(getResources().getColor(R.color.yellow));
                rltv_losers_view.setVisibility(View.VISIBLE);
                img_gainers.setImageDrawable(getResources().getDrawable(R.drawable.graph_up_unselected));
                txt_gainers.setTextColor(getResources().getColor(R.color.white));
                rltv_gainers_view.setVisibility(View.GONE);

                gainerLoserExcDBRAdapter = new GainerLoserExcDBRAdapter(getActivity(), loosersList, " ", false, false);
                rview_gain_loose.setAdapter(gainerLoserExcDBRAdapter);
                gainerLoserExcDBRAdapter.notifyDataSetChanged();
            }
        });


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                onLoadHeaderBanner();
                onLoadNewsDX();
                onLoadAllCoins();
            }
        }, 200);


        return view;
    }


    //    **************GETTING HEADER BANNER**************
    private void onLoadHeaderBanner() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                HeaderBannerDao headerBannerDao = deviantXDB.headerBannerDao();
                if ((headerBannerDao.getHeaderBanner()) != null) {
                    String newsResult = headerBannerDao.getHeaderBanner().headerBannerDB;
                    updateUIHeaderBanner(newsResult);
                } else {
                    if (CommonUtilities.isConnectionAvailable(getActivity())) {
                        fetchHeaderBanner();
                    } else {
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.internetconnection));
                    }
                }
            }
        });

    }

    HeaderBannerUIListener headerBannerUIListener = new HeaderBannerUIListener() {
        @Override
        public void onChangedHeaderBanner(String allHeaderBanner) {
            updateUIHeaderBanner(allHeaderBanner);
        }

    };

    private void updateUIHeaderBanner(String responsevalue) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject(responsevalue);
                    loginResponseMsg = jsonObject.getString("msg");
                    loginResponseStatus = jsonObject.getString("status");

                    if (loginResponseStatus.equals("true")) {
                        loginResponseData = jsonObject.getString("data");
                        HeaderBanner[] coinsStringArray = GsonUtils.getInstance().fromJson(loginResponseData, HeaderBanner[].class);
                        allHeaderBanner = new ArrayList<HeaderBanner>(Arrays.asList(coinsStringArray));

                        ArrayList<HeaderBanner> headerList = new ArrayList<>();
                        for (HeaderBanner coinName : allHeaderBanner) {
                            headerList.add(coinName);
                        }
                        if (headerList.size() > 0) {
                            lnr_empty_headers.setVisibility(View.GONE);
                            itemPicker.setVisibility(View.VISIBLE);
                            dashboardSlideAdapter = new ExchangeDashboardSlideAdapter(getActivity(),headerList);
                            itemPicker.setOrientation(DSVOrientation.HORIZONTAL);
//        itemPicker.addOnItemChangedListener(this);
                            itemPicker.setAdapter(dashboardSlideAdapter);
                            itemPicker.setItemTransitionTimeMillis(150);
                            itemPicker.setItemTransformer(new ScaleTransformer.Builder()
                                    .setMinScale(0.8f)
                                    .build());
                            itemPicker.scrollToPosition(0);
                        } else {
                            lnr_empty_headers.setVisibility(View.VISIBLE);
                            itemPicker.setVisibility(View.GONE);
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

    private void fetchHeaderBanner() {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
//            progressDialog = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.please_wait), true);
            HeaderPanelControllerApi apiService = DeviantXApiClient.getClient().create(HeaderPanelControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.getHeaderPanel(CONSTANTS.DeviantMulti + token);
            apiResponse.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responsevalue = response.body().string();
//                        progressDialog.dismiss();

                        if (!responsevalue.isEmpty() && responsevalue != null) {
                            updateUIHeaderBanner(responsevalue);
//                            progressDialog.dismiss();
                            HeaderBannerDao mDao = deviantXDB.headerBannerDao();
                            HeaderBannerDB headerBannerDB = new HeaderBannerDB(1, responsevalue);
                            mDao.insertHeaderBanner(headerBannerDB);
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


    //    **************GETTING NEWS**************
    private void onLoadNewsDX() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                NewsDXDao newsDXDao = deviantXDB.newsDXDao();
                if ((newsDXDao.getNewsDX()) != null) {
                    String newsResult = newsDXDao.getNewsDX().newsDXDB;
                    updateUINewsDX(newsResult);
                } else {
                    if (CommonUtilities.isConnectionAvailable(getActivity())) {
                        fetchCoinsNewsDX();
                    } else {
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.internetconnection));
                    }
                }
            }
        });

    }

    NewsDXUIListener newsDXUIListener = new NewsDXUIListener() {
        @Override
        public void onChangedNewsDX(String allNewsDX) {
            updateUINewsDX(allNewsDX);
        }

    };

    private void updateUINewsDX(String responsevalue) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject(responsevalue);
                    loginResponseMsg = jsonObject.getString("msg");
                    loginResponseStatus = jsonObject.getString("status");

                    if (loginResponseStatus.equals("true")) {
                        loginResponseData = jsonObject.getString("data");
                        NewsDX[] coinsStringArray = GsonUtils.getInstance().fromJson(loginResponseData, NewsDX[].class);
                        allNewsDX = new ArrayList<NewsDX>(Arrays.asList(coinsStringArray));

                        ArrayList<NewsDX> newsList = new ArrayList<>();
                        for (NewsDX coinName : allNewsDX) {
                            newsList.add(coinName);
                        }
                        if (newsList.size() > 0) {
                            lnr_empty_news.setVisibility(View.GONE);
                            newsExcDBRAdapter = new NewsExcDBRAdapter(getActivity(), newsList);
                            rview_devx_news.setAdapter(newsExcDBRAdapter);
                        } else {
                            lnr_empty_news.setVisibility(View.VISIBLE);
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

    private void fetchCoinsNewsDX() {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
//            progressDialog = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.please_wait), true);
            NewsPanelControllerApi apiService = DeviantXApiClient.getClient().create(NewsPanelControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.getNewsPanel(CONSTANTS.DeviantMulti + token);
            apiResponse.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responsevalue = response.body().string();
//                        progressDialog.dismiss();

                        if (!responsevalue.isEmpty() && responsevalue != null) {
                            updateUINewsDX(responsevalue);
//                            progressDialog.dismiss();
                            NewsDXDao mDao = deviantXDB.newsDXDao();
                            NewsDXDB newsDXDB = new NewsDXDB(1, responsevalue);
                            mDao.insertNewsDX(newsDXDB);
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


    //    **************GETTING FEATURED COINS**************
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

                        ArrayList<AllCoins> allFeaturedCoins = new ArrayList<>();
                        ArrayList<AllCoins> allUnFeaturedCoins = new ArrayList<>();
                        for (int i = 0; i < allCoinsList.size(); i++) {
                            if (allCoinsList.get(i).getStr_isFeatureCoin().trim().equals("NO")) {
                                allUnFeaturedCoins.add(allCoinsList.get(i));
                            } else {
                                allFeaturedCoins.add(allCoinsList.get(i));
                            }
                        }

                        if (allFeaturedCoins.size() > 0) {
                            rview_featured_coins.setVisibility(View.VISIBLE);
                            lnr_empty_feat_coins.setVisibility(View.GONE);
                            featuredCoinsExcDBRAdapter = new FeaturedCoinsExcDBRAdapter(getActivity(), allFeaturedCoins);
                            rview_featured_coins.setAdapter(featuredCoinsExcDBRAdapter);
                        } else {
                            rview_featured_coins.setVisibility(View.GONE);
                            lnr_empty_feat_coins.setVisibility(View.VISIBLE);
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
//            progressDialog = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.please_wait), true);
            CoinsControllerApi apiService = DeviantXApiClient.getClient().create(CoinsControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.getAllCoins(CONSTANTS.DeviantMulti + token);
            apiResponse.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responsevalue = response.body().string();

                        if (!responsevalue.isEmpty() && responsevalue != null) {
//                            progressDialog.dismiss();
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
                        Toast.makeText(getActivity(), getResources().getString(R.string.networkerror), Toast.LENGTH_SHORT).show();
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
