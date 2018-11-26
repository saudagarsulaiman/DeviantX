package com.cryptowallet.deviantx.UI.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.UI.Activities.FeaturedADAcivity;
import com.cryptowallet.deviantx.UI.Activities.RecentADHistoryAcivity;
import com.cryptowallet.deviantx.UI.Adapters.FeaturedADHorizantalRAdapter;
import com.cryptowallet.deviantx.UI.Adapters.RecentADHistoryRAdapter;
import com.shehabic.droppy.DroppyClickCallbackInterface;
import com.shehabic.droppy.DroppyMenuPopup;
import com.shehabic.droppy.animations.DroppyScaleAnimation;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AirDropFragment extends Fragment implements DroppyClickCallbackInterface {

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
    DroppyMenuPopup droppyMenu;


    FeaturedADHorizantalRAdapter featuredADHorizantalRAdapter;
    RecentADHistoryRAdapter recentADHistoryRAdapter;

    LinearLayoutManager layoutManagerHorizontal;
    LinearLayoutManager layoutManagerVertical;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;

//    String loginResponseData, loginResponseStatus, loginResponseMsg, str_coin_name, str_coin_code, str_coin_logo,
//            str_data_address, str_data_walletName, str_data_privatekey, str_data_passcode,
//            str_data_account, str_data_coin;
//    int int_coin_id, int_data_id;
//    Double dbl_coin_usdValue, dbl_data_balance, dbl_data_balanceInUSD, dbl_data_balanceInINR;

    @Override
    public void onResume() {
        super.onResume();
//        if (walletCoinsRAdapter != null) {
//            walletCoinsRAdapter.setIsHideBalance(myApplication.getHideBalance());
//            walletCoinsRAdapter.notifyDataSetChanged();
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.air_drop_fragment, container, false);

        ButterKnife.bind(this, view);

        sharedPreferences = getActivity().getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        layoutManagerHorizontal = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rview_fad_coins.setLayoutManager(layoutManagerHorizontal);
        layoutManagerVertical = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rview_radh_coins.setLayoutManager(layoutManagerVertical);

        seekbar_per.setEnabled(false);
//        seekbar_per.setProgress(95);

        featuredADHorizantalRAdapter = new FeaturedADHorizantalRAdapter(getActivity().getApplicationContext());
        rview_fad_coins.setAdapter(featuredADHorizantalRAdapter);
        recentADHistoryRAdapter = new RecentADHistoryRAdapter(getActivity().getApplicationContext());
        rview_radh_coins.setAdapter(recentADHistoryRAdapter);

        txt_fad_viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FeaturedADAcivity.class);
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
                initDroppyMenuFromXml(img_menu);
            }
        });


//        accountWalletlist = new ArrayList<>();
//
////        walletCoinsRAdapter = new WalletCoinsRAdapter(getActivity().getApplicationContext());
////        rview_wallet_coins.setAdapter(walletCoinsRAdapter);
//
//        if (CommonUtilities.isConnectionAvailable(getActivity())) {
////            GET Account Wallet
//            fetchAccountWallet();
//        } else {
//            CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.internetconnection));
//        }
//        txt_add_wallet.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), SetUpWalletActivity.class);
//                startActivity(intent);
//            }
//        });

        return view;
    }

    private void initDroppyMenuFromXml(ImageView img_menu) {
        DroppyMenuPopup.Builder droppyBuilder = new DroppyMenuPopup.Builder(getActivity(), img_menu);
        DroppyMenuPopup droppyMenu = droppyBuilder.fromMenu(R.menu.menu_items_airdrop)
                .triggerOnAnchorClick(false)
                .setOnClick(this)
//                .setPopupAnimation(new DroppyScaleAnimation())
                .build();
        droppyMenu.show();
    }

    @Override
    public void call(View v, int id) {
        String idText;

        switch (id) {
            case R.id.item_copy_wallet:
                idText = "Copied";
                break;
            case R.id.item_withdraw_funds:
                idText = "Withdrawn";
                break;
            case R.id.item_config_wallet:
                idText = "Configured";
                break;
            case R.id.item_info_ad:
                idText = "No Info Available";
                break;
            case R.id.item_delete:
                idText = "Deleted";
                break;
            default:
                idText = String.valueOf(id);
        }

        Toast.makeText(getActivity(), "Tapped on item with id: " + idText, Toast.LENGTH_SHORT).show();
    }

/*
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_items_airdrop, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
*/

    //    private void fetchAccountWallet() {
//        try {
//            String token = sharedPreferences.getString(CONSTANTS.token, null);
//            progressDialog = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.please_wait), true);
//            CryptoControllerApi apiService = DeviantXApiClient.getClient().create(CryptoControllerApi.class);
//            Call<ResponseBody> apiResponse = apiService.getAccountWallet(CONSTANTS.DeviantMulti + token);
//            apiResponse.enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                    try {
//                        String responsevalue = response.body().string();
//
//                        if (!responsevalue.isEmpty() && responsevalue != null) {
//                            progressDialog.dismiss();
//
//                            JSONObject jsonObject = new JSONObject(responsevalue);
//                            loginResponseMsg = jsonObject.getString("msg");
//                            loginResponseStatus = jsonObject.getString("status");
//
//                            if (loginResponseStatus.equals("true")) {
//                                loginResponseData = jsonObject.getString("data");
//                                JSONArray jsonArrayData = new JSONArray(loginResponseData);
//                                if (jsonArrayData.length() == 0) {
//                                    lnr_empty_coins.setVisibility(View.VISIBLE);
//                                    rview_wallet_coins.setVisibility(View.GONE);
//                                } else {
//                                    lnr_empty_coins.setVisibility(View.GONE);
//                                    rview_wallet_coins.setVisibility(View.VISIBLE);
//                                    for (int i = 0; i < jsonArrayData.length(); i++) {
//                                        JSONObject jsonObjectData = jsonArrayData.getJSONObject(i);
//                                        try {
//                                            int_data_id = jsonObjectData.getInt("id");
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//                                        try {
//                                            str_data_address = jsonObjectData.getString("address");
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//                                        try {
//                                            str_data_walletName = jsonObjectData.getString("walletName");
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//                                        try {
//                                            str_data_privatekey = jsonObjectData.getString("privatekey");
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//                                        try {
//                                            str_data_passcode = jsonObjectData.getString("passcode");
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//                                        try {
//                                            dbl_data_balance = jsonObjectData.getDouble("balance");
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//                                        try {
//                                            dbl_data_balanceInUSD = jsonObjectData.getDouble("balanceInUSD");
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//                                        try {
//                                            dbl_data_balanceInINR = jsonObjectData.getDouble("balanceInINR");
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//                                        try {
//                                            str_data_account = jsonObjectData.getString("account");
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//                                        try {
//                                            str_data_coin = jsonObjectData.getString("coin");
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//                                        JSONObject jsonObjectCoins = new JSONObject(str_data_coin);
//                                        try {
//                                            int_coin_id = jsonObjectCoins.getInt("id");
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//                                        try {
//                                            str_coin_name = jsonObjectCoins.getString("name");
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//                                        try {
//                                            str_coin_code = jsonObjectCoins.getString("code");
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//                                        try {
//                                            str_coin_logo = jsonObjectCoins.getString("logo");
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//                                        try {
//                                            dbl_coin_usdValue = jsonObjectCoins.getDouble("usdValue");
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//                                        AllCoins allCoins = new AllCoins(int_coin_id, str_coin_name, str_coin_code, str_coin_logo, dbl_coin_usdValue);
//                                        accountWalletlist.add(new AccountWallet(int_data_id, str_data_address, str_data_walletName,
//                                                str_data_privatekey, str_data_passcode, dbl_data_balance, dbl_data_balanceInUSD,
//                                                dbl_data_balanceInINR, str_data_account, allCoins));
//                                    }
//                                    walletCoinsRAdapter = new WalletCoinsRAdapter(getActivity(), accountWalletlist);
//                                    rview_wallet_coins.setAdapter(walletCoinsRAdapter);
//                                }
//                            } else {
//                                CommonUtilities.ShowToastMessage(getActivity(), loginResponseMsg);
//                            }
//                        } else {
//                            CommonUtilities.ShowToastMessage(getActivity(), loginResponseMsg);
////                            Toast.makeText(getApplicationContext(), responsevalue, Toast.LENGTH_LONG).show();
//                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
//                        }
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        progressDialog.dismiss();
//                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.errortxt));
////                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    if (t instanceof SocketTimeoutException) {
//                        progressDialog.dismiss();
//                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.Timeout));
////                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Timeout), Toast.LENGTH_SHORT).show();
//                    } else if (t instanceof java.net.ConnectException) {
//                        progressDialog.dismiss();
//                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.networkerror));
////                        Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.networkerror), Toast.LENGTH_SHORT).show();
//                    } else {
//                        progressDialog.dismiss();
//                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.errortxt));
////                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//        } catch (Exception ex) {
//            progressDialog.dismiss();
//            ex.printStackTrace();
//            CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.errortxt));
////            Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
//        }
//    }

}
