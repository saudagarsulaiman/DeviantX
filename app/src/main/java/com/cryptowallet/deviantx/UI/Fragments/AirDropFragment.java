package com.cryptowallet.deviantx.UI.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.ServiceAPIs.AirdropWalletControllerApi;
import com.cryptowallet.deviantx.ServiceAPIs.UserAirdropControllerApi;
import com.cryptowallet.deviantx.UI.Activities.AirdropWalletHistoryActivity;
import com.cryptowallet.deviantx.UI.Activities.CreateADCampaignsActivity;
import com.cryptowallet.deviantx.UI.Activities.DepositWalletAirdropActivity;
import com.cryptowallet.deviantx.UI.Activities.DividendADListActivity;
import com.cryptowallet.deviantx.UI.Activities.FeaturedADListAcivity;
import com.cryptowallet.deviantx.UI.Activities.RecentADHistoryAcivity;
import com.cryptowallet.deviantx.UI.Activities.WithdrawFundsAirdropActivity;
import com.cryptowallet.deviantx.UI.Adapters.CreatorADHorizontalRAdapter;
import com.cryptowallet.deviantx.UI.Adapters.DividendADHorizantalRAdapter;
import com.cryptowallet.deviantx.UI.Adapters.FeaturedADHorizantalRAdapter;
import com.cryptowallet.deviantx.UI.Adapters.RecentADHistoryRAdapter;
import com.cryptowallet.deviantx.UI.Interfaces.AirdropWalletUIListener;
import com.cryptowallet.deviantx.UI.Interfaces.AirdropsHistoryUIListener;
import com.cryptowallet.deviantx.UI.Interfaces.DividendAirdropsUIListener;
import com.cryptowallet.deviantx.UI.Interfaces.FeaturedAirdropsUIListener;
import com.cryptowallet.deviantx.UI.Models.AirdropWallet;
import com.cryptowallet.deviantx.UI.Models.AirdropsHistory;
import com.cryptowallet.deviantx.UI.Models.CreatorAirdrop;
import com.cryptowallet.deviantx.UI.Models.DividendAirdrops;
import com.cryptowallet.deviantx.UI.Models.FeaturedAirdrops;
import com.cryptowallet.deviantx.UI.RoomDatabase.Database.DeviantXDB;
import com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB.AirdropWalletDao;
import com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB.AirdropsHistoryDao;
import com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB.DividendAirdropsDao;
import com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB.FeaturedAirdropsDao;
import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.AirdropWalletDB;
import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.AirdropsHistoryDB;
import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.DividendAirdropsDB;
import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.FeaturedAirdropsDB;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;
import com.cryptowallet.deviantx.Utilities.DeviantXApiClient;
import com.cryptowallet.deviantx.Utilities.GsonUtils;
import com.squareup.picasso.Picasso;
import com.zyyoona7.popup.EasyPopup;
import com.zyyoona7.popup.XGravity;
import com.zyyoona7.popup.YGravity;

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

public class AirDropFragment extends Fragment {

    View view;
    @BindView(R.id.rview_fad_coins)
    RecyclerView rview_fad_coins;
    @BindView(R.id.rview_radh_coins)
    RecyclerView rview_radh_coins;
    @BindView(R.id.txt_coin_name_code)
    TextView txt_coin_name_code;
    @BindView(R.id.txt_coin_address)
    TextView txt_coin_address;
    @BindView(R.id.txt_holding_bal)
    TextView txt_holding_bal;
    @BindView(R.id.txt_holding_days)
    TextView txt_holding_days;
    @BindView(R.id.txt_seekbar_per)
    TextView txt_seekbar_per;
    @BindView(R.id.txt_fad_viewAll)
    TextView txt_fad_viewAll;
    @BindView(R.id.txt_radh_viewAll)
    TextView txt_radh_viewAll;
    @BindView(R.id.txt_airdrop_lbl)
    TextView txt_airdrop_lbl;
    @BindView(R.id.img_menu)
    ImageView img_menu;
    @BindView(R.id.btn_menu)
    Button btn_menu;
    @BindView(R.id.img_coin_icon)
    ImageView img_coin_icon;
    @BindView(R.id.seekbar_per)
    SeekBar seekbar_per;
    @BindView(R.id.lnr_empty_his_list)
    LinearLayout lnr_empty_his_list;
    @BindView(R.id.txt_acc_status)
    TextView txt_acc_status;
    //    New Design
    @BindView(R.id.lnr_empty_prtcptn)
    LinearLayout lnr_empty_prtcptn;
    @BindView(R.id.lnr_search_airdrops)
    LinearLayout lnr_search_airdrops;
    @BindView(R.id.lnr_create_ad_camp)
    LinearLayout lnr_create_ad_camp;
    @BindView(R.id.txt_div_ad_viewAll)
    TextView txt_div_ad_viewAll;
    @BindView(R.id.rview_div_ad_coins)
    RecyclerView rview_div_ad_coins;
    @BindView(R.id.lnr_empty_feat_coins)
    LinearLayout lnr_empty_feat_coins;
    @BindView(R.id.lnr_empty_div_coins)
    LinearLayout lnr_empty_div_coins;

    @BindView(R.id.lnr_empty_creator_coins)
    LinearLayout lnr_empty_creator_coins;
    @BindView(R.id.txt_creator_ad_viewAll)
    TextView txt_creator_ad_viewAll;
    @BindView(R.id.rview_creator_ad_coins)
    RecyclerView rview_creator_ad_coins;


    FeaturedADHorizantalRAdapter featuredADHorizantalRAdapter;
    CreatorADHorizontalRAdapter creatorADHorizontalRAdapter;
    DividendADHorizantalRAdapter dividendADHorizantalRAdapter;
    RecentADHistoryRAdapter recentADHistoryRAdapter;

    LinearLayoutManager layoutManagerHorizontal, layoutManagerHorizontalDivAd,layoutManagerHorizontalCreatorAd, layoutManagerVertical;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    ArrayList<FeaturedAirdrops> allFeaturedAirdrops;
    ArrayList<DividendAirdrops> allDividendAirdrops;
    ArrayList<AirdropsHistory> allAirdropsHistory;
    ArrayList<CreatorAirdrop> allCreatorAirdrops;
    String loginResponseData, loginResponseStatus, loginResponseMsg;


    ArrayList<com.cryptowallet.deviantx.UI.Models.AirdropWallet> airdropWalletlist;

    EasyPopup mRvPop;
    DeviantXDB deviantXDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.air_drop_fragment, container, false);

        ButterKnife.bind(this, view);
        deviantXDB = DeviantXDB.getDatabase(getActivity());
        seekbar_per.setEnabled(false);

        allFeaturedAirdrops = new ArrayList<>();
        allDividendAirdrops = new ArrayList<>();
        allAirdropsHistory = new ArrayList<>();
        airdropWalletlist = new ArrayList<>();
        allCreatorAirdrops = new ArrayList<>();

        sharedPreferences = getActivity().getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        layoutManagerHorizontal = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rview_fad_coins.setLayoutManager(layoutManagerHorizontal);
        layoutManagerHorizontalDivAd = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rview_div_ad_coins.setLayoutManager(layoutManagerHorizontalDivAd);
        layoutManagerHorizontalCreatorAd= new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rview_creator_ad_coins.setLayoutManager(layoutManagerHorizontalCreatorAd);
        layoutManagerVertical = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rview_radh_coins.setLayoutManager(layoutManagerVertical);


        rview_fad_coins.setVisibility(View.GONE);
        txt_fad_viewAll.setVisibility(View.GONE);
        lnr_empty_feat_coins.setVisibility(View.VISIBLE);
        rview_div_ad_coins.setVisibility(View.GONE);
        txt_div_ad_viewAll.setVisibility(View.GONE);
        lnr_empty_div_coins.setVisibility(View.VISIBLE);
        rview_radh_coins.setVisibility(View.GONE);
        txt_radh_viewAll.setVisibility(View.GONE);
        lnr_empty_his_list.setVisibility(View.VISIBLE);

        txt_fad_viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FeaturedADListAcivity.class);
                startActivity(intent);
            }
        });
        txt_div_ad_viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DividendADListActivity.class);
                intent.putExtra(CONSTANTS.amount, airdropWalletlist.get(0).getDbl_data_ad_balance());
                intent.putExtra(CONSTANTS.isADTrue, airdropWalletlist.get(0).getStr_airdropStatus());
                startActivity(intent);
            }
        });
        txt_radh_viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RecentADHistoryAcivity.class);
                startActivity(intent);
            }
        });

        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                initEvents(v);

            }
        });


        lnr_create_ad_camp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), /*SelectADCampaignsActivity*/CreateADCampaignsActivity.class);
                startActivity(intent);
            }
        });
        lnr_search_airdrops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FeaturedADListAcivity.class);
                startActivity(intent);
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                onLoadAirDropWallet();
                onLoadFeaturedAirdrops();
                onLoadDividendAirdrops();
                onLoadAirdropsHistory();
                fetchCoinsCreatorAirdrop();
            }
        }, 200);


        return view;
    }

    private void initEvents(View v) {

        mRvPop = EasyPopup.create()
                .setContext(getActivity())
                .setContentView(R.layout.dialog_airdrop_menu)
                .setFocusAndOutsideEnable(true)
                .setBackgroundDimEnable(true)
                .apply();


        TextView txt_copy_wallet = mRvPop.findViewById(R.id.txt_copy_wallet);
        TextView txt_withdraw_funds = mRvPop.findViewById(R.id.txt_withdraw_funds);
        TextView txt_config_wallet = mRvPop.findViewById(R.id.txt_config_wallet);
        TextView txt_ad_info = mRvPop.findViewById(R.id.txt_ad_info);
        TextView txt_delete = mRvPop.findViewById(R.id.txt_delete);
        TextView txt_ad_history = mRvPop.findViewById(R.id.txt_ad_history);

/*
        txt_copy_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtilities.copyToClipboard(getActivity(), airdropWalletlist.get(0).getStr_data_ad_address(), airdropWalletlist.get(0).getAllCoins().getStr_coin_name());
                mRvPop.dismiss();
            }
        });
*/

        txt_withdraw_funds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WithdrawFundsAirdropActivity.class);
                Bundle bundle = new Bundle();
//                bundle.putParcelable(CONSTANTS.selectedCoin, airdropWalletlist.get(0));
                bundle.putParcelableArrayList(CONSTANTS.selectedAccountWallet, airdropWalletlist);
                intent.putExtras(bundle);
                startActivity(intent);
//                CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.withdraw_funds));
                mRvPop.dismiss();
            }
        });

        txt_ad_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AirdropWalletHistoryActivity.class);
/*
                Bundle bundle = new Bundle();
//                bundle.putParcelable(CONSTANTS.selectedCoin, airdropWalletlist.get(0));
                bundle.putParcelableArrayList(CONSTANTS.selectedAccountWallet, airdropWalletlist);
                intent.putExtras(bundle);
*/
                startActivity(intent);
                mRvPop.dismiss();
            }
        });

        txt_config_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DepositWalletAirdropActivity.class);
                Bundle bundle = new Bundle();
//                bundle.putParcelable(CONSTANTS.selectedCoin, airdropWalletlist.get(0));
                bundle.putParcelableArrayList(CONSTANTS.selectedAccountWallet, airdropWalletlist);
                intent.putExtras(bundle);
                startActivity(intent);
//                CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.config_wallet));
                mRvPop.dismiss();
            }
        });

        txt_ad_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.ad_info));
                mRvPop.dismiss();
            }
        });

        txt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.delete));
                mRvPop.dismiss();
            }
        });

        mRvPop.showAtAnchorView(v, YGravity.BELOW, XGravity.LEFT);

    }

    private void fetchAirdropWallet() {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            AirdropWalletControllerApi apiService = DeviantXApiClient.getClient().create(AirdropWalletControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.getAirdropWallet(CONSTANTS.DeviantMulti + token);
            apiResponse.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responsevalue = response.body().string();

                        if (!responsevalue.isEmpty() && responsevalue != null) {
                            updateUIADWallet(responsevalue);
                            AirdropWalletDao mDao = deviantXDB.airdropWalletDao();
                            AirdropWalletDB airdropWallet = new AirdropWalletDB(1, responsevalue);
                            mDao.insertAirdropWallet(airdropWallet);

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

    private int getDaysPer(String startDate, int noOfDays) {
        int result = 0;
//        String endDate = "1551378600000";  // endDate = 1551378600000 = 01/03/2019
        long start = Long.parseLong(startDate);
//        long end = Long.parseLong(endDate);
//        long totalDiff = end - start;
        long currentDiff = System.currentTimeMillis() - start;

//        int totalDays = (int) (totalDiff / 86400000);
        int totalDays = noOfDays;
        int currentDays = (int) (currentDiff / 86400000);

        result = (currentDays * 100) / 60;

        return result;
    }

    private String getDays(String started) {
        try {
            long time = System.currentTimeMillis();
            long diff = time - Long.parseLong(started);
            int days = (int) (diff / 86400000);     //86400000 = 1000 * 60 * 60 * 24;
            if (days > 1) {
                return days + " Days";
            } else {
                return days + " Day";
            }
//            return CommonUtilities.convertToHumanReadable(Long.parseLong(started));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }


    @Override
    public void onResume() {
        super.onResume();
        fetchAirdropWallet();
        fetchCoinsCreatorAirdrop();
        myApplication.setAirdropWalletUIListener(airdropWalletUIListener);
        myApplication.setFeaturedAirdropsUIListener(featuredAirdropUIListener);
        myApplication.setDividendAirdropsUIListener(dividendAirdropsUIListener);
        myApplication.setAirdropsHistoryUIListener(airdropsHistoryUIListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myApplication.setAirdropWalletUIListener(null);
        myApplication.setFeaturedAirdropsUIListener(null);
        myApplication.setDividendAirdropsUIListener(null);
        myApplication.setAirdropsHistoryUIListener(null);
    }

    AirdropWalletUIListener airdropWalletUIListener = new AirdropWalletUIListener() {
        @Override
        public void onChangedAirdropWallet(String airdropWalletList) {
            updateUIADWallet(airdropWalletList);
        }
    };

    private void updateUIADWallet(String responsevalue) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject(responsevalue);
                    loginResponseMsg = jsonObject.getString("msg");
                    loginResponseStatus = jsonObject.getString("status");

                    if (loginResponseStatus.equals("true")) {
                        loginResponseData = jsonObject.getString("data");

                        AirdropWallet[] coinsStringArray = GsonUtils.getInstance().fromJson(loginResponseData, AirdropWallet[].class);
                        airdropWalletlist = new ArrayList<AirdropWallet>(Arrays.asList(coinsStringArray));


                        Picasso.with(getActivity()).load(airdropWalletlist.get(0).getStr_ad_coin_logo()).into(img_coin_icon);
                        txt_coin_name_code.setText(airdropWalletlist.get(0).getStr_ad_coin_name() + " (" + airdropWalletlist.get(0).getStr_ad_coin_code() + ")");
/*
                        txt_coin_address.setText(airdropWalletlist.get(0).getStr_data_ad_address());
*/
                        if (myApplication.getHideBalance()) {
                            txt_holding_bal.setText("***");
                        } else {
                            txt_holding_bal.setText(String.format("%.4f", airdropWalletlist.get(0).getDbl_data_ad_balance()));
                        }


                        if (airdropWalletlist.get(0).getStr_airdropStatus().equals("false")) {
                            txt_acc_status.setText(getResources().getString(R.string.inactive));
                            txt_acc_status.setBackground(getResources().getDrawable(R.drawable.rec_google_red_c5));
                        } else {
                            txt_acc_status.setText(getResources().getString(R.string.active));
                            txt_acc_status.setBackground(getResources().getDrawable(R.drawable.rec_green_c5));
                        }


                        if (airdropWalletlist.get(0).getStartDate() == null) {
                            txt_holding_days.setText("0 Days");
                            seekbar_per.setProgress(0);
                            txt_seekbar_per.setText("0" + "%");
                        } else {
                            txt_holding_days.setText(getDays(airdropWalletlist.get(0).getStartDate()));
                            seekbar_per.setProgress(getDaysPer(airdropWalletlist.get(0).getStartDate(), airdropWalletlist.get(0).getInt_ad_noOfDays()));
                            txt_seekbar_per.setText(getDaysPer(airdropWalletlist.get(0).getStartDate(), airdropWalletlist.get(0).getInt_ad_noOfDays()) + "%");
                        }

//                                if (airdropWalletlist.get(0).getStr_data_ad_address().length() < 15) {
/*
                        txt_coin_address.setText(airdropWalletlist.get(0).getStr_data_ad_address());
*/
//                                } else {
//                                    String address = airdropWalletlist.get(0).getStr_data_ad_address();
//                                    String dummy = "{...}";
//                                    String first_half = String.format("%.7s", address);
//                                    String second_half = address.substring(address.length() - 7);
//                                    txt_coin_address.setText(first_half + dummy + second_half);
//                                }

                    } else {
                        CommonUtilities.ShowToastMessage(getActivity(), loginResponseMsg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private void onLoadAirDropWallet() {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AirdropWalletDao airdropWalletDao = deviantXDB.airdropWalletDao();
                if ((airdropWalletDao.getAllAirdropWallet()) != null) {
                    String walletResult = airdropWalletDao.getAllAirdropWallet().airdropWallet;
                    updateUIADWallet(walletResult);
                } else {
                    if (CommonUtilities.isConnectionAvailable(getActivity())) {
                        fetchAirdropWallet();
                    } else {
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.internetconnection));
                    }
                }
            }
        });

    }


    //    **************GETTING USER AIRDROPS**************
    private void onLoadFeaturedAirdrops() {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                FeaturedAirdropsDao featuredAirdropsDao = deviantXDB.featuredAirdropsDao();
                if ((featuredAirdropsDao.getFeaturedAirdrops()) != null) {
                    String walletResult = featuredAirdropsDao.getFeaturedAirdrops().featuredAirdrops;
                    updateUIFeaturedAirdrops(walletResult);
                } else {
                    if (CommonUtilities.isConnectionAvailable(getActivity())) {
                        fetchCoinsFeaturedAirdrops();
                    } else {
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.internetconnection));
                    }
                }
            }
        });

    }

    FeaturedAirdropsUIListener featuredAirdropUIListener = new FeaturedAirdropsUIListener() {
        @Override
        public void onChangedFeaturedAirdrops(String allFeaturedAirdrops) {
            updateUIFeaturedAirdrops(allFeaturedAirdrops);
        }

    };

    private void updateUIFeaturedAirdrops(String responsevalue) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject(responsevalue);
                    loginResponseMsg = jsonObject.getString("msg");
                    loginResponseStatus = jsonObject.getString("status");

                    if (loginResponseStatus.equals("true")) {
                        loginResponseData = jsonObject.getString("data");
                        FeaturedAirdrops[] coinsStringArray = GsonUtils.getInstance().fromJson(loginResponseData, FeaturedAirdrops[].class);
                        allFeaturedAirdrops = new ArrayList<FeaturedAirdrops>(Arrays.asList(coinsStringArray));

                        ArrayList<FeaturedAirdrops> featuredCoinsList = new ArrayList<>();
                        for (FeaturedAirdrops coinName : allFeaturedAirdrops) {
                            featuredCoinsList.add(coinName);
                        }
                        if (featuredCoinsList.size() > 0) {
                            txt_fad_viewAll.setVisibility(View.VISIBLE);
                            lnr_empty_feat_coins.setVisibility(View.GONE);
                            lnr_empty_prtcptn.setVisibility(View.GONE);
                            featuredADHorizantalRAdapter = new FeaturedADHorizantalRAdapter(getActivity(), featuredCoinsList, false);
                            rview_fad_coins.setAdapter(featuredADHorizantalRAdapter);
//                            rview_fad_coins.setVisibility(View.VISIBLE);
                            rview_fad_coins.setVisibility(View.GONE);
                        } else {
                            lnr_empty_prtcptn.setVisibility(View.VISIBLE);
                            txt_fad_viewAll.setVisibility(View.GONE);
//                            lnr_empty_feat_coins.setVisibility(View.VISIBLE);
                            lnr_empty_feat_coins.setVisibility(View.GONE);
                            rview_fad_coins.setVisibility(View.GONE);
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

    private void fetchCoinsFeaturedAirdrops() {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            UserAirdropControllerApi apiService = DeviantXApiClient.getClient().create(UserAirdropControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.getUserAD(CONSTANTS.DeviantMulti + token);
            apiResponse.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responsevalue = response.body().string();
                        if (!responsevalue.isEmpty() && responsevalue != null) {
                            updateUIFeaturedAirdrops(responsevalue);
                            FeaturedAirdropsDao mDao = deviantXDB.featuredAirdropsDao();
                            FeaturedAirdropsDB featuredAirdropsDB = new FeaturedAirdropsDB(1, responsevalue);
                            mDao.insertFeaturedAirdrops(featuredAirdropsDB);

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


    //    **************GETTING DIVIDEND AIRDROPS**************
    private void onLoadDividendAirdrops() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DividendAirdropsDao dividendAirdropsDao = deviantXDB.dividendAirdropsDao();
                if ((dividendAirdropsDao.getDividendAirdrops()) != null) {
                    String walletResult = dividendAirdropsDao.getDividendAirdrops().diviendAirdrops;
                    updateUIDividendAirdrops(walletResult);
                } else {
                    if (CommonUtilities.isConnectionAvailable(getActivity())) {
                        fetchCoinsDividendAirdrops();
                    } else {
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.internetconnection));
                    }
                }
            }
        });

    }

    DividendAirdropsUIListener dividendAirdropsUIListener = new DividendAirdropsUIListener() {
        @Override
        public void onChangedDividendAirdrops(String allDividendAirdrops) {
            updateUIDividendAirdrops(allDividendAirdrops);
        }

    };

    private void updateUIDividendAirdrops(String responsevalue) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject(responsevalue);
                    loginResponseMsg = jsonObject.getString("msg");
                    loginResponseStatus = jsonObject.getString("status");

                    if (loginResponseStatus.equals("true")) {
                        loginResponseData = jsonObject.getString("data");
                        DividendAirdrops[] coinsStringArray = GsonUtils.getInstance().fromJson(loginResponseData, DividendAirdrops[].class);
                        allDividendAirdrops = new ArrayList<DividendAirdrops>(Arrays.asList(coinsStringArray));

                        ArrayList<DividendAirdrops> dividendCoinsList = new ArrayList<>();
                        for (DividendAirdrops coinName : allDividendAirdrops) {
                            dividendCoinsList.add(coinName);
                        }
                        if (dividendCoinsList.size() > 0) {
                            txt_div_ad_viewAll.setVisibility(View.VISIBLE);
                            lnr_empty_div_coins.setVisibility(View.GONE);
                            rview_div_ad_coins.setVisibility(View.VISIBLE);
                            dividendADHorizantalRAdapter = new DividendADHorizantalRAdapter(getActivity(), dividendCoinsList, false, airdropWalletlist.get(0).getDbl_data_ad_balance(), airdropWalletlist.get(0).getStr_airdropStatus());
                            rview_div_ad_coins.setAdapter(dividendADHorizantalRAdapter);
                        } else {
                            rview_div_ad_coins.setVisibility(View.GONE);
                            txt_div_ad_viewAll.setVisibility(View.GONE);
                            lnr_empty_div_coins.setVisibility(View.VISIBLE);
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

    private void fetchCoinsDividendAirdrops() {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            UserAirdropControllerApi apiService = DeviantXApiClient.getClient().create(UserAirdropControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.getClaimADAmount(CONSTANTS.DeviantMulti + token);
            apiResponse.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responsevalue = response.body().string();
                        if (!responsevalue.isEmpty() && responsevalue != null) {
                            updateUIDividendAirdrops(responsevalue);
                            DividendAirdropsDao mDao = deviantXDB.dividendAirdropsDao();
                            DividendAirdropsDB dividendAirdropsDB = new DividendAirdropsDB(1, responsevalue);
                            mDao.insertDividendAirdrops(dividendAirdropsDB);
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

    //    **************GETTING AIRDROPS HISTORY**************
    private void onLoadAirdropsHistory() {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AirdropsHistoryDao airdropsHistoryDao = deviantXDB.airdropsHistoryDao();
                if ((airdropsHistoryDao.getAirdropsHistory()) != null) {
                    String walletResult = airdropsHistoryDao.getAirdropsHistory().airdropsHistory;
                    updateUIAirdropsHistory(walletResult);
                } else {
                    if (CommonUtilities.isConnectionAvailable(getActivity())) {
                        fetchCoinsAirdropsHistory();
                    } else {
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.internetconnection));
                    }
                }
            }
        });

    }

    AirdropsHistoryUIListener airdropsHistoryUIListener = new AirdropsHistoryUIListener() {
        @Override
        public void onChangedAirdropsHistory(String allAirdropsHistory) {
            updateUIAirdropsHistory(allAirdropsHistory);
        }

    };

    private void updateUIAirdropsHistory(String responsevalue) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject(responsevalue);
                    loginResponseMsg = jsonObject.getString("msg");
                    loginResponseStatus = jsonObject.getString("status");

                    if (loginResponseStatus.equals("true")) {
                        loginResponseData = jsonObject.getString("data");
                        AirdropsHistory[] coinsStringArray = GsonUtils.getInstance().fromJson(loginResponseData, AirdropsHistory[].class);
                        allAirdropsHistory = new ArrayList<AirdropsHistory>(Arrays.asList(coinsStringArray));
                        ArrayList<AirdropsHistory> airdropsHistoryList = new ArrayList<>();
                        for (AirdropsHistory coinName : allAirdropsHistory) {
                            airdropsHistoryList.add(coinName);
                        }
                        if (airdropsHistoryList.size() > 0) {
                            txt_radh_viewAll.setVisibility(View.VISIBLE);
                            lnr_empty_his_list.setVisibility(View.GONE);
                            rview_radh_coins.setVisibility(View.VISIBLE);
                            recentADHistoryRAdapter = new RecentADHistoryRAdapter(getActivity(), airdropsHistoryList, false);
                            rview_radh_coins.setAdapter(recentADHistoryRAdapter);
                        } else {
                            rview_radh_coins.setVisibility(View.GONE);
                            txt_radh_viewAll.setVisibility(View.GONE);
                            lnr_empty_his_list.setVisibility(View.VISIBLE);
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

    private void fetchCoinsAirdropsHistory() {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            UserAirdropControllerApi apiService = DeviantXApiClient.getClient().create(UserAirdropControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.getADHistory(CONSTANTS.DeviantMulti + token);
            apiResponse.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responsevalue = response.body().string();
                        if (!responsevalue.isEmpty() && responsevalue != null) {
                            updateUIAirdropsHistory(responsevalue);
                            AirdropsHistoryDao mDao = deviantXDB.airdropsHistoryDao();
                            AirdropsHistoryDB airdropsHistoryDB = new AirdropsHistoryDB(1, responsevalue);
                            mDao.insertAirdropsHistory(airdropsHistoryDB);
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


    //    **************GETTING CREATOR AIRDROPS**************

    private void updateUICreatorAirdrops(String responsevalue) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject(responsevalue);
                    loginResponseMsg = jsonObject.getString("msg");
                    loginResponseStatus = jsonObject.getString("status");

                    if (loginResponseStatus.equals("true")) {
                        loginResponseData = jsonObject.getString("data");
                        CreatorAirdrop[] coinsStringArray = GsonUtils.getInstance().fromJson(loginResponseData, CreatorAirdrop[].class);
                        allCreatorAirdrops = new ArrayList<CreatorAirdrop>(Arrays.asList(coinsStringArray));

                        ArrayList<CreatorAirdrop> createdADCoinsList = new ArrayList<>();
                        for (CreatorAirdrop coinName : allCreatorAirdrops) {
                            createdADCoinsList.add(coinName);
                        }
                        if (createdADCoinsList.size() > 0) {
/*
                            txt_creator_ad_viewAll.setVisibility(View.VISIBLE);
*/
                            lnr_empty_creator_coins.setVisibility(View.GONE);
                            rview_creator_ad_coins.setVisibility(View.VISIBLE);
                            creatorADHorizontalRAdapter = new CreatorADHorizontalRAdapter(getActivity(), createdADCoinsList, false);
                            rview_creator_ad_coins.setAdapter(creatorADHorizontalRAdapter);
                        } else {
                            rview_creator_ad_coins.setVisibility(View.GONE);
/*
                            txt_creator_ad_viewAll.setVisibility(View.GONE);
*/
                            lnr_empty_creator_coins.setVisibility(View.VISIBLE);
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

    private void fetchCoinsCreatorAirdrop() {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            UserAirdropControllerApi apiService = DeviantXApiClient.getClient().create(UserAirdropControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.getCreatorADHistory(CONSTANTS.DeviantMulti + token);
            apiResponse.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responsevalue = response.body().string();
                        if (!responsevalue.isEmpty() && responsevalue != null) {
                            updateUICreatorAirdrops(responsevalue);
/*
                            CreatorAirdropDao mDao = deviantXDB.dividendAirdropsDao();
                            CreatorAirdropDB dividendAirdropsDB = new CreatorAirdropDB(1, responsevalue);
                            mDao.insertCreatorAirdrop(dividendAirdropsDB);
*/
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

}
