package com.cryptowallet.deviantx.UI.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.ServiceAPIs.CryptoControllerApi;
import com.cryptowallet.deviantx.ServiceAPIs.WalletControllerApi;
import com.cryptowallet.deviantx.UI.Activities.AddCoinsActivity;
import com.cryptowallet.deviantx.UI.Activities.CoinInformationActivity;
import com.cryptowallet.deviantx.UI.Activities.DashBoardActivity;
import com.cryptowallet.deviantx.UI.Activities.ECTActivity;
import com.cryptowallet.deviantx.UI.Activities.SetUpWalletActivity;
import com.cryptowallet.deviantx.UI.Activities.WalletListActivity;
import com.cryptowallet.deviantx.UI.Activities.WalletOptionsActivity;
import com.cryptowallet.deviantx.UI.Adapters.MyWalletCoinsRAdapter;
import com.cryptowallet.deviantx.UI.Adapters.MyWalletListRAdapter;
import com.cryptowallet.deviantx.UI.Adapters.WalletListRAdapter;
import com.cryptowallet.deviantx.UI.Models.AccountWallet;
import com.cryptowallet.deviantx.UI.Models.AllCoins;
import com.cryptowallet.deviantx.UI.Models.WalletList;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;
import com.cryptowallet.deviantx.Utilities.DeviantXApiClient;
import com.cryptowallet.deviantx.Utilities.VerticalTextView;
import com.github.brnunes.swipeablerecyclerview.SwipeableRecyclerViewTouchListener;
import com.yarolegovich.discretescrollview.DSVOrientation;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

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
    @BindView(R.id.lnr_add_new_coins)
    LinearLayout lnr_add_new_coins;
    @BindView(R.id.lnr_new_wallet)
    LinearLayout lnr_new_wallet;
    @BindView(R.id.item_picker)
    DiscreteScrollView itemPicker;
    @BindView(R.id.new_wallet)
    VerticalTextView newWallet;


    WalletListRAdapter walletListRAdapter;
    ArrayList<WalletList> walletList;

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

    String str_data_name;
    int int_data_walletid;
    double dbl_data_totalBal;
    InfiniteScrollAdapter infiniteAdapter;

    boolean hideBal;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            String wallet_name = sharedPreferences.getString(CONSTANTS.walletName, "sss");
            fetchAccountWallet(wallet_name);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (myWalletCoinsRAdapter != null) {
            myWalletCoinsRAdapter.setIsHideBalance(myApplication.getHideBalance());
            myWalletCoinsRAdapter.notifyDataSetChanged();
        }
        if (walletListRAdapter != null) {
            walletListRAdapter.setIsHideBalance(myApplication.getHideBalance());
            walletListRAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.dashboard_fragment, container, false);
        ButterKnife.bind(this, view);

        sharedPreferences = getActivity().getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        hideBal = sharedPreferences.getBoolean(CONSTANTS.hideBal, true);
        newWallet.setTextColor(Color.WHITE);
        walletList = new ArrayList<>();

        if (CommonUtilities.isConnectionAvailable(getActivity())) {
//        Getting Wallets List
            invokeWallet();
        } else {
            CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.internetconnection));
        }

        itemPicker.setOrientation(DSVOrientation.HORIZONTAL);
//        itemPicker.addOnItemChangedListener(this);
        infiniteAdapter = InfiniteScrollAdapter.wrap(new WalletListRAdapter(getActivity(), walletList));
        itemPicker.addOnItemChangedListener(this);
        itemPicker.setAdapter(infiniteAdapter);
        itemPicker.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build());
        if (!walletList.isEmpty()) {
            onItemChanged(walletList.get(0));
        }

        accountWalletlist = new ArrayList<>();

        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rview_wallet_coins.setLayoutManager(layoutManager);
        myWalletCoinsRAdapter = new MyWalletCoinsRAdapter(getActivity(), accountWalletlist);
        rview_wallet_coins.setAdapter(myWalletCoinsRAdapter);


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
                    Bundle bundle=new Bundle();
                    bundle.putParcelable(CONSTANTS.selectedCoin,accountWalletlist.get(fromPos).getAllCoins());
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else if (direction == ItemTouchHelper.END) {
                    // DO Action for Right
                    Intent intent = new Intent(getActivity(), WalletOptionsActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putParcelable(CONSTANTS.selectedCoin,accountWalletlist.get(fromPos).getAllCoins());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(rview_wallet_coins);

       /* SwipeableRecyclerViewTouchListener swipeTouchListener = new SwipeableRecyclerViewTouchListener(rview_wallet_coins, new SwipeableRecyclerViewTouchListener.SwipeListener() {
            @Override
            public boolean canSwipeLeft(int position) {
                return true;
            }

            @Override
            public boolean canSwipeRight(int position) {
                return true;
            }

            @Override
            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
//              Toast.makeText(MainActivity.this, mItems.get(position) + " swiped left", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), CoinInformationActivity.class);
//                editor.putString(CONSTANTS.,);
//                editor.apply();
                startActivity(intent);
            }

            @Override
            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
//              Toast.makeText(MainActivity.this, mItems.get(position) + " swiped left", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), WalletOptionsActivity.class);
                startActivity(intent);
            }
        });
        rview_wallet_coins.addOnItemTouchListener(swipeTouchListener);*/

        lnr_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonUtilities.isConnectionAvailable(getActivity())) {
//            GET Account Wallet
                    lnr_reload.setVisibility(View.GONE);
                    String wallet_name = sharedPreferences.getString(CONSTANTS.walletName, "sss");
                    fetchAccountWallet(wallet_name);
                } else {
                    CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.internetconnection));
                }
            }
        });

        lnr_add_new_coins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddCoinsActivity.class);
                startActivityForResult(intent, 100);
            }
        });
        lnr_add_coins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddCoinsActivity.class);
                startActivityForResult(intent, 100);
            }
        });
        lnr_new_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SetUpWalletActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void onItemChanged(WalletList walletList) {
        if (CommonUtilities.isConnectionAvailable(getActivity())) {
//            GET Account Wallet
            lnr_reload.setVisibility(View.GONE);
            editor.putString(CONSTANTS.walletName, walletList.getStr_data_name());
            editor.apply();
            fetchAccountWallet(walletList.getStr_data_name());
        } else {
            CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.internetconnection));
        }
    }

    private void invokeWallet() {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            progressDialog = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.please_wait), true);
            WalletControllerApi apiService = DeviantXApiClient.getClient().create(WalletControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.getAllWallet(CONSTANTS.DeviantMulti + token);
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
                                for (int i = 0; i < jsonArrayData.length(); i++) {
                                    JSONObject jsonObjectData = jsonArrayData.getJSONObject(i);
                                    try {
                                        int_data_walletid = jsonObjectData.getInt("id");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        str_data_name = jsonObjectData.getString("name");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        dbl_data_totalBal = jsonObjectData.getDouble("toatalBalance");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    walletList.add(new WalletList(int_data_walletid, str_data_name, dbl_data_totalBal));
                                }
//                                walletListRAdapter = new WalletListRAdapter(getActivity(), walletList);
                                infiniteAdapter = InfiniteScrollAdapter.wrap(new WalletListRAdapter(getActivity(), walletList));
                                itemPicker.setAdapter(infiniteAdapter);
//                                itemPicker.setItemTransformer(new ScaleTransformer.Builder()
//                                        .setMinScale(0.8f)
//                                        .build());
//                                onItemChanged(walletList.get(0));

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

    private void fetchAccountWallet(String walletName) {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            progressDialog = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.please_wait), true);
            CryptoControllerApi apiService = DeviantXApiClient.getClient().create(CryptoControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.getAccountWallet(CONSTANTS.DeviantMulti + token, walletName);
            apiResponse.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responsevalue = response.body().string();

                        if (!responsevalue.isEmpty() && responsevalue != null) {
                            progressDialog.dismiss();

                            lnr_reload.setVisibility(View.GONE);
                            JSONObject jsonObject = new JSONObject(responsevalue);
                            loginResponseMsg = jsonObject.getString("msg");
                            loginResponseStatus = jsonObject.getString("status");

                            if (loginResponseStatus.equals("true")) {
                                lnr_reload.setVisibility(View.GONE);
                                loginResponseData = jsonObject.getString("data");
                                JSONArray jsonArrayData = new JSONArray(loginResponseData);
                                if (jsonArrayData.length() == 0) {
                                    lnr_empty_coins.setVisibility(View.VISIBLE);
                                    lnr_add_new_coins.setVisibility(View.GONE);
                                    rview_wallet_coins.setVisibility(View.GONE);
                                } else {
                                    lnr_add_new_coins.setVisibility(View.VISIBLE);
                                    lnr_empty_coins.setVisibility(View.GONE);
                                    rview_wallet_coins.setVisibility(View.VISIBLE);
                                    double ttl_amt;
                                    accountWalletlist = new ArrayList<>();
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

                                        AllCoins allCoins = new AllCoins(int_coin_id, str_coin_name, str_coin_code, str_coin_logo, dbl_coin_usdValue, int_coin_rank, dbl_coin_marketCap, dbl_coin_volume, dbl_coin_24h, dbl_coin_7d, dbl_coin_1m);
                                        accountWalletlist.add(new AccountWallet(int_data_id, str_data_address, str_data_walletName,
                                                str_data_privatekey, str_data_passcode, dbl_data_balance, dbl_data_balanceInUSD,
                                                dbl_data_balanceInINR, str_data_account, allCoins));
                                    }
                                    myWalletCoinsRAdapter = new MyWalletCoinsRAdapter(getActivity(), accountWalletlist);
                                    rview_wallet_coins.setAdapter(myWalletCoinsRAdapter);
//                                    totalBalance = 0.0;
//                                    for (AccountWallet accountWallet : accountWalletlist) {
//                                        totalBalance += accountWallet.getStr_data_balanceInUSD();
//                                    }
//                                    if (myApplication.getHideBalance()) {
//                                        txt_wallet_bal.setText("~$ ***");
//                                    } else {
//                                        txt_wallet_bal.setText("~$ " + String.format("%.4f", totalBalance));
//                                    }
                                }
                            } else if (loginResponseStatus.equals("401")) {
                                CommonUtilities.sessionExpired(getActivity(), loginResponseMsg);
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
                        lnr_reload.setVisibility(View.VISIBLE);
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        lnr_reload.setVisibility(View.VISIBLE);
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.Timeout));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Timeout), Toast.LENGTH_SHORT).show();
                    } else if (t instanceof java.net.ConnectException) {
                        lnr_reload.setVisibility(View.VISIBLE);
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.networkerror));
//                        Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.networkerror), Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        lnr_reload.setVisibility(View.VISIBLE);
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception ex) {
            progressDialog.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.errortxt));
            lnr_reload.setVisibility(View.VISIBLE);
//            Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {
        int positionInDataSet = infiniteAdapter.getRealPosition(adapterPosition);
        onItemChanged(walletList.get(positionInDataSet));
    }

//    @Override
//    public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {
//        int positionInDataSet = infiniteAdapter.getRealPosition(position);
//        onItemChanged(data.get(positionInDataSet));
//    }
//    private void onItemChanged(Item item) {
//        currentItemName.setText(item.getName());
//        currentItemPrice.setText(item.getPrice());
//        changeRateButtonState(item);
//    }


}