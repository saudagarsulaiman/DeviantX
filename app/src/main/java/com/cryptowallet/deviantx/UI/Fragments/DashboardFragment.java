package com.cryptowallet.deviantx.UI.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.ServiceAPIs.CryptoControllerApi;
import com.cryptowallet.deviantx.ServiceAPIs.WalletControllerApi;
import com.cryptowallet.deviantx.UI.Activities.AddCoinsActivity;
import com.cryptowallet.deviantx.UI.Activities.CoinInformationActivity;
import com.cryptowallet.deviantx.UI.Activities.ExchangeDashBoardActivity;
import com.cryptowallet.deviantx.UI.Activities.SetUpWalletActivity;
import com.cryptowallet.deviantx.UI.Activities.WalletHistoryActivity;
import com.cryptowallet.deviantx.UI.Adapters.MyWalletCoinsRAdapter;
import com.cryptowallet.deviantx.UI.Adapters.WalletListRAdapter;
import com.cryptowallet.deviantx.UI.Interfaces.FavListener;
import com.cryptowallet.deviantx.UI.Interfaces.WalletUIChangeListener;
import com.cryptowallet.deviantx.UI.Models.AccountWallet;
import com.cryptowallet.deviantx.UI.Models.WalletList;
import com.cryptowallet.deviantx.UI.RoomDatabase.Database.DeviantXDB;
import com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB.AccountWalletDao;
import com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB.AllCoinsDao;
import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.AccountWalletDB;
import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.AllCoinsDB;
import com.cryptowallet.deviantx.UI.Services.AirdropWalletFetch;
import com.cryptowallet.deviantx.UI.Services.AirdropsHistoryFetch;
import com.cryptowallet.deviantx.UI.Services.ExcOrdersFetch;
import com.cryptowallet.deviantx.UI.Services.WalletDataFetch;
import com.cryptowallet.deviantx.UI.Services.WalletDetailsFetch;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;
import com.cryptowallet.deviantx.Utilities.DeviantXApiClient;
import com.cryptowallet.deviantx.Utilities.GsonUtils;
import com.cryptowallet.deviantx.Utilities.VerticalTextView;
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

import static com.cryptowallet.deviantx.UI.Activities.DashBoardActivity.img_tlbr_refresh;
import static com.cryptowallet.deviantx.Utilities.MyApplication.myApplication;


public class DashboardFragment extends Fragment implements DiscreteScrollView.OnItemChangedListener<RecyclerView.ViewHolder>/*implements DiscreteScrollView.OnItemChangedListener<RecyclerView.ViewHolder>*/ {

    View view;
    //    @BindView(R.id.viewPager)
//    ViewPager viewPager;
    @BindView(R.id.rview_wallet_coins)
    RecyclerView rview_wallet_coins;
    @BindView(R.id.lnr_wallet)
    LinearLayout lnr_wallet;
    @BindView(R.id.lnr_empty_coins)
    LinearLayout lnr_empty_coins;
    //    @BindView(R.id.txt_wallet_name)
//    TextView txt_wallet_name;
//    @BindView(R.id.txt_wallet_percentage)
//    TextView txt_wallet_percentage;
//    @BindView(R.id.txt_wallet_bal)
//    TextView txt_wallet_bal;
//    @BindView(R.id.txt_wallet_coin)
//    TextView txt_wallet_coin;
    @BindView(R.id.lnr_add_coins)
    LinearLayout lnr_add_coins;
    @BindView(R.id.img_add_coin)
    ImageView img_add_coin;
    @BindView(R.id.lnr_reload)
    LinearLayout lnr_reload;
    @BindView(R.id.pb)
    ProgressBar pb;
    @BindView(R.id.lnr_add_new_coins)
    LinearLayout lnr_add_new_coins;
    @BindView(R.id.lnr_new_wallet)
    LinearLayout lnr_new_wallet;
    @BindView(R.id.item_picker)
    DiscreteScrollView itemPicker;
    @BindView(R.id.new_wallet)
    VerticalTextView newWallet;
    @BindView(R.id.fav_filter)
    ImageView favFilter;
    @BindView(R.id.lnr_no_fav_avail)
    LinearLayout lnr_no_fav_avail;
    @BindView(R.id.lnr_notification)
    LinearLayout lnr_notification;
    @BindView(R.id.txt_notification)
    TextView txt_notification;
    @BindView(R.id.lnr_buysell)
    LinearLayout lnr_buysell;


    WalletListRAdapter walletListRAdapter;
    ArrayList<WalletList> walletList;

    MyWalletCoinsRAdapter myWalletCoinsRAdapter;
    LinearLayoutManager layoutManager;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;

    ArrayList<AccountWallet> accountWalletlist;
    ArrayList<AccountWallet> filterCoinlist;
    String loginResponseData, loginResponseStatus, loginResponseMsg, str_coin_name, str_coin_code, str_coin_logo,
            str_data_address, str_data_walletName, str_data_privatekey, str_data_passcode,
            str_data_account, str_data_coin, str_coin_chart_data;
    int int_coin_id, int_data_id, int_coin_rank;
    Double dbl_coin_usdValue, dbl_data_balance, dbl_data_balanceInUSD, dbl_data_balanceInINR, dbl_coin_marketCap, dbl_coin_volume, dbl_coin_24h, dbl_coin_7d, dbl_coin_1m;
    boolean isFav = false;
    String str_data_name;
    int int_data_walletid;
    double dbl_data_totalBal;
    boolean defaultWallet = false;
    FavListener favListener;
    DeviantXDB db;
    boolean hideBal;

   /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            String wallet_name = sharedPreferences.getString(CONSTANTS.walletName, " ");
            int wallet_id = sharedPreferences.getInt(CONSTANTS.walletId, 0);
            fetchAccountWallet(wallet_name, wallet_id);
        }
    }*/

    @Override
    public void onResume() {
        super.onResume();

        myApplication.setWalletUIChangeListener(walletUIChangeListener);
        if (myWalletCoinsRAdapter != null) {
            myWalletCoinsRAdapter.setIsHideBalance(myApplication.getHideBalance());
            myWalletCoinsRAdapter.notifyDataSetChanged();
        }
        if (walletListRAdapter != null) {
            walletListRAdapter.setIsHideBalance(myApplication.getHideBalance());
            walletListRAdapter.notifyDataSetChanged();
           /* String wallet_name = sharedPreferences.getString(CONSTANTS.walletName, " ");
            int wallet_id = sharedPreferences.getInt(CONSTANTS.walletId, 0);
            fetchAccountWallet(wallet_name, wallet_id);*/
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (walletUIChangeListener != null) {
            myApplication.setWalletUIChangeListener(null);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    WalletUIChangeListener walletUIChangeListener = new WalletUIChangeListener() {
        @Override
        public void onWalletUIChanged(String wallets, Boolean isDefaultWalle) {
            walletUIChange(wallets, isDefaultWalle);
        }

        @Override
        public void onWalletCoinUIChanged(String loadedWalletName) {
            String wallet_name = sharedPreferences.getString(CONSTANTS.walletName, "sss");
            int walletId = sharedPreferences.getInt(CONSTANTS.walletId, 0);
            if (loadedWalletName.equalsIgnoreCase(wallet_name))
                fetchAccountWallet(wallet_name, walletId);
        }
    };

    private void onProgressLoad() {
        pb.setVisibility(View.VISIBLE);
        lnr_empty_coins.setVisibility(View.GONE);
        rview_wallet_coins.setVisibility(View.GONE);
    }

    private void onProgressHide() {
        pb.setVisibility(View.GONE);
    }

    private void fetchAccountWallet(String wallet_name, int walletId) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onProgressLoad();
                try {
                    AllCoinsDao allCoinsDao = db.allCoinsDao();
                    if ((allCoinsDao.getAllAllCoins(wallet_name) != null)) {
                        updateCoins(allCoinsDao.getAllAllCoins(wallet_name).coinsList);
                    } else {
                        if (CommonUtilities.isConnectionAvailable(getActivity())) {
                            onLineFetchAccountWallet(wallet_name, walletId);
                        } else {
                            CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.internetconnection));
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void walletUIChange(String responsevalue, Boolean isDefaultWalle) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
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

                            walletListRAdapter.setAllWallets(walletList);
                            if (isDefaultWalle)
                                itemPicker.scrollToPosition(myApplication.getDefaultWallet());

                        } else {
                            CommonUtilities.ShowToastMessage(getActivity(), loginResponseMsg);
                        }

                    } else {
                        CommonUtilities.ShowToastMessage(getActivity(), loginResponseMsg);
                        Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.errortxt));
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.dashboard_fragment, container, false);
        ButterKnife.bind(this, view);
        db = DeviantXDB.getDatabase(getContext());
        sharedPreferences = getActivity().getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        hideBal = sharedPreferences.getBoolean(CONSTANTS.hideBal, true);
        newWallet.setTextColor(Color.WHITE);
        walletList = new ArrayList<>();
        accountWalletlist = new ArrayList<>();
        pb.getIndeterminateDrawable().setColorFilter(
                getResources().getColor(R.color.white),
                android.graphics.PorterDuff.Mode.SRC_IN);
        editor.putString(CONSTANTS.walletName, "");
        editor.putInt(CONSTANTS.walletId, 0);
        editor.apply();
        walletListRAdapter = new WalletListRAdapter(getActivity(), walletList);
        itemPicker.setAdapter(walletListRAdapter);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                onLoadWallet();
            }
        }, 100);

        itemPicker.setOrientation(DSVOrientation.HORIZONTAL);
        itemPicker.addOnItemChangedListener(this);
        itemPicker.setItemTransitionTimeMillis(150);
        itemPicker.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build());
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rview_wallet_coins.setLayoutManager(layoutManager);
        myWalletCoinsRAdapter = new MyWalletCoinsRAdapter(getActivity(), accountWalletlist, favListener);
        rview_wallet_coins.setAdapter(myWalletCoinsRAdapter);
        /*if (!walletList.isEmpty()) {
            onItemChanged(walletList.get(0));
        }*/

        accountWalletlist = new ArrayList<>();
        filterCoinlist = new ArrayList<>();
        favListener = new FavListener() {
            @Override
            public void addOrRemoveFav(AccountWallet accountWallet, int pos) {
                favAddRemove(accountWallet.getInt_data_id(), !accountWallet.getFav(), pos);
            }
        };

        //layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        //rview_wallet_coins.setLayoutManager(layoutManager);
        //myWalletCoinsRAdapter = new MyWalletCoinsRAdapter(getActivity(), accountWalletlist, favListener);
        //rview_wallet_coins.setAdapter(myWalletCoinsRAdapter);
        favFilter.setImageDrawable(getResources().getDrawable(R.drawable.z_grey));
        favFilter.setTag("unFav");
        img_tlbr_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String walName = walletList.get(itemPicker.getCurrentItem()).getStr_data_name();
                    Intent sintent = new Intent(getActivity(), WalletDataFetch.class);
                    sintent.putExtra("walletName", walName);

                    progressDialog = ProgressDialog.show(getActivity(), ""/*getResources().getString(R.string.updating)*/, getResources().getString(R.string./*please_wait*/updating), true);
                    Log.e("*******DEVIANT*******", "Dashboard Fragment services Executing");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        getActivity().startForegroundService(sintent);
                        getActivity().startForegroundService(new Intent(getActivity(), AirdropWalletFetch.class));
                        getActivity().startForegroundService(new Intent(getActivity(), AirdropsHistoryFetch.class));
                        getActivity().startForegroundService(new Intent(getActivity(), WalletDetailsFetch.class));
                        getActivity().startForegroundService(new Intent(getActivity(), ExcOrdersFetch.class));
                    } else {
                        getActivity().startService(sintent);
                        getActivity().startService(new Intent(getActivity(), AirdropWalletFetch.class));
                        getActivity().startService(new Intent(getActivity(), AirdropsHistoryFetch.class));
                        getActivity().startService(new Intent(getActivity(), WalletDetailsFetch.class));
                        getActivity().startService(new Intent(getActivity(), ExcOrdersFetch.class));
                    }
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                        }
                    }, 800);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.START | ItemTouchHelper.END) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                //awesome code when user grabs recycler card to reorder
                return true;
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                //awesome code to run when user drops card and completes reorder
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int fromPos = viewHolder.getAdapterPosition();
                if (direction == ItemTouchHelper.START) {
                    // DO Action for Left
                    Intent intent = new Intent(getActivity(), CoinInformationActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(CONSTANTS.selectedCoin, accountWalletlist.get(fromPos)/*.getAllCoins()*/);
                    bundle.putBoolean(CONSTANTS.isExploreCoins, false);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else if (direction == ItemTouchHelper.END) {
                    // DO Action for Right
//                    Intent intent = new Intent(getActivity(), WalletOptionsActivity.class);
                    Intent intent = new Intent(getActivity(), WalletHistoryActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(CONSTANTS.selectedAccountWallet, accountWalletlist.get(fromPos));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(rview_wallet_coins);

        lnr_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonUtilities.isConnectionAvailable(getActivity())) {
//            GET Account Wallet
                    lnr_reload.setVisibility(View.GONE);
                    String wallet_name = sharedPreferences.getString(CONSTANTS.walletName, "sss");
                    int wallet_id = sharedPreferences.getInt(CONSTANTS.walletId, 0);
                    fetchAccountWallet(wallet_name, wallet_id);
                } else {
                    CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.internetconnection));
                }
            }
        });

        lnr_add_new_coins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddCoinsActivity.class);
                startActivity(intent);
            }
        });
        lnr_add_coins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddCoinsActivity.class);
                startActivity(intent);
            }
        });
        lnr_new_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SetUpWalletActivity.class);
                startActivity(intent);
            }
        });
        favFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterLoad((filterCoinlist.size() == 0) && favFilter.getTag().equals("unFav"));
            }
        });
        lnr_buysell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ExchangeDashBoardActivity.class);
                intent.putExtra(CONSTANTS.seletedTab, 1);
                startActivity(intent);
            }
        });

        return view;
    }

    private void onLoadWallet() {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AccountWalletDao accountWalletDao = db.accountWalletDao();
                if ((accountWalletDao.getAllAccountWallet()).size() > 0) {
                    String walletResult = (accountWalletDao.getAllAccountWallet()).get(0).walletDatas;
                    walletUpdate(walletResult);
                } else {
                    if (CommonUtilities.isConnectionAvailable(getActivity())) {
                        invokeWallet();
                    } else {
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.internetconnection));
                    }
                }
            }
        });

    }

    private void onItemChanged(WalletList walletList) {
//            GET Account Wallet
        lnr_reload.setVisibility(View.GONE);
        editor.putString(CONSTANTS.walletName, walletList.getStr_data_name());
        editor.putInt(CONSTANTS.walletId, walletList.getInt_data_id());
        editor.apply();
        fetchAccountWallet(walletList.getStr_data_name(), walletList.getInt_data_id());

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
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.Timeout));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Timeout), Toast.LENGTH_SHORT).show();
                    } else if (t instanceof java.net.ConnectException) {
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.networkerror));
                    } else {
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.errortxt));
//            Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
        }

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
                            editor.putString(CONSTANTS.defaultWalletName, walletList.get(i).getStr_data_name());
                            editor.apply();
                            myApplication.setDefaultWallet(i);
                        }
                        String wallet_name = sharedPreferences.getString(CONSTANTS.walletName, "");
                        int wallet_id = sharedPreferences.getInt(CONSTANTS.walletId, 0);
                        onLineFetchAccountWallet(wallet_name, wallet_id);
                    }

                    walletListRAdapter.setAllWallets(walletList);
                    itemPicker.scrollToPosition(myApplication.getDefaultWallet());

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
            CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
        }
    }

    private void favAddRemove(final int id, final boolean isFav, final int position) {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            CryptoControllerApi apiService = DeviantXApiClient.getClient().create(CryptoControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.favAddRemove(CONSTANTS.DeviantMulti + token, id, isFav);
            apiResponse.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responsevalue = response.body().string();

                        if (!responsevalue.isEmpty() && responsevalue != null) {

                            JSONObject jsonObject = new JSONObject(responsevalue);
                            loginResponseMsg = jsonObject.getString("msg");
                            loginResponseStatus = jsonObject.getString("status");

                            if (loginResponseStatus.equals("true")) {
                                Intent serviceIntent = new Intent(getActivity(), WalletDataFetch.class);
                                int wallet_id = sharedPreferences.getInt(CONSTANTS.walletId, 0);
                                String wallet_name = sharedPreferences.getString(CONSTANTS.walletName, "sss");
                                serviceIntent.putExtra("walletName", wallet_name);
                                serviceIntent.putExtra("walletId", wallet_id);
                                serviceIntent.putExtra("isRefresh", false);
                                getActivity().startService(serviceIntent);
                                if (filterCoinlist.size() > 0) {
                                    for (AccountWallet wallet : accountWalletlist) {
/*
                                        if (wallet.getStr_data_address().equalsIgnoreCase(address))
*/
                                        if (wallet.getInt_data_id() == id)
                                            wallet.setFav(isFav);
                                    }
                                    filterLoad(true);
                                } else {
                                    accountWalletlist.get(position).setFav(isFav);
                                    myWalletCoinsRAdapter.updateData(accountWalletlist, position);
                                }
                            } else if (loginResponseStatus.equals("401")) {
                                CommonUtilities.sessionExpired(getActivity(), loginResponseMsg);
                            } else {
                                CommonUtilities.ShowToastMessage(getActivity(), loginResponseMsg);
                            }
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
//                        Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.networkerror), Toast.LENGTH_SHORT).show();
                    } else {
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.errortxt));
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.errortxt));
//            Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
        }
    }

    private void filterLoad(boolean isFilter) {
        filterCoinlist = new ArrayList<>();
        if (isFilter) {
            for (AccountWallet wallet : accountWalletlist) {
                if (wallet.getFav())
                    filterCoinlist.add(wallet);
            }

            myWalletCoinsRAdapter = new MyWalletCoinsRAdapter(getActivity(), filterCoinlist, favListener);
            rview_wallet_coins.setAdapter(myWalletCoinsRAdapter);
            favFilter.setImageDrawable(getResources().getDrawable(R.drawable.z));
            favFilter.setTag("Fav");
            if (filterCoinlist.size() == 0) {
                lnr_no_fav_avail.setVisibility(View.VISIBLE);
            } else {
                lnr_no_fav_avail.setVisibility(View.GONE);
            }

        } else {
            lnr_no_fav_avail.setVisibility(View.GONE);
            myWalletCoinsRAdapter = new MyWalletCoinsRAdapter(getActivity(), accountWalletlist, favListener);
            rview_wallet_coins.setAdapter(myWalletCoinsRAdapter);
            favFilter.setImageDrawable(getResources().getDrawable(R.drawable.z_grey));
            favFilter.setTag("unFav");
        }
    }


    private void coinValueUpdate(String responsevalue) {
        try {
            if (!responsevalue.isEmpty() && responsevalue != null) {
                lnr_reload.setVisibility(View.GONE);
                JSONObject jsonObject = new JSONObject(responsevalue);
                loginResponseMsg = jsonObject.getString("msg");
                loginResponseStatus = jsonObject.getString("status");

                if (loginResponseStatus.equals("true")) {
                    lnr_reload.setVisibility(View.GONE);
                    loginResponseData = jsonObject.getString("data");
                    accountWalletlist = new ArrayList<>();
                    filterCoinlist = new ArrayList<>();
                    AccountWallet[] accountWallets = GsonUtils.getInstance().fromJson(loginResponseData, AccountWallet[].class);
                    accountWalletlist = new ArrayList<AccountWallet>(Arrays.asList(accountWallets));
                    if (accountWalletlist.size() == 0) {
                        rview_wallet_coins.setVisibility(View.GONE);
                        lnr_empty_coins.setVisibility(View.VISIBLE);
//                                    lnr_add_new_coins.setVisibility(View.GONE);
                    } else {
                        lnr_add_new_coins.setVisibility(View.VISIBLE);
                        lnr_empty_coins.setVisibility(View.GONE);
                        rview_wallet_coins.setVisibility(View.VISIBLE);
                        favFilter.setImageDrawable(getResources().getDrawable(R.drawable.z_grey));
                        favFilter.setTag("unFav");
                        lnr_no_fav_avail.setVisibility(View.GONE);
                        myWalletCoinsRAdapter.setAllCoins(accountWalletlist);
                    }
                } else if (loginResponseStatus.equals("401")) {
                    CommonUtilities.sessionExpired(getActivity(), loginResponseMsg);
                } else {
                    CommonUtilities.ShowToastMessage(getActivity(), loginResponseMsg);
                }
            } else {
                CommonUtilities.ShowToastMessage(getActivity(), loginResponseMsg);
//                            Toast.makeText(getApplicationContext(), responsevalue, Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            lnr_reload.setVisibility(View.VISIBLE);
            CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
        }
    }

    private void updateCoins(String responsevalue) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                // this will run in the main thread
                coinValueUpdate(responsevalue);
                onProgressHide();
                myWalletCoinsRAdapter.notifyDataSetChanged();
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
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateCoins(responsevalue);
                                AllCoinsDao mDao = db.allCoinsDao();
                                AllCoinsDB allCoinsDB = new AllCoinsDB(walletName, responsevalue, walletId);
                                mDao.insertAllCoins(allCoinsDB);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        lnr_reload.setVisibility(View.VISIBLE);
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.Timeout));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Timeout), Toast.LENGTH_SHORT).show();
                    } else if (t instanceof java.net.ConnectException) {
                        lnr_reload.setVisibility(View.VISIBLE);
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.networkerror));
//                        Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.networkerror), Toast.LENGTH_SHORT).show();
                    } else {
                        lnr_reload.setVisibility(View.VISIBLE);
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.errortxt));
            lnr_reload.setVisibility(View.VISIBLE);
//            Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {
        if (adapterPosition > -1) {
            String wallet_name = sharedPreferences.getString(CONSTANTS.walletName, "sss");
            if (!wallet_name.equals(walletList.get(adapterPosition).getStr_data_name())) {
                onItemChanged(walletList.get(adapterPosition));
                accountWalletlist = new ArrayList<>();
                filterCoinlist = new ArrayList<>();
                favFilter.setImageDrawable(getResources().getDrawable(R.drawable.z_grey));
                favFilter.setTag("unFav");
                myWalletCoinsRAdapter = new MyWalletCoinsRAdapter(getActivity(), accountWalletlist, favListener);
                rview_wallet_coins.setAdapter(myWalletCoinsRAdapter);
            }
        }
    }

}