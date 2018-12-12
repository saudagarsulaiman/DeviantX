package com.cryptowallet.deviantx.UI.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.ServiceAPIs.AirdropWalletControllerApi;
import com.cryptowallet.deviantx.UI.Activities.ConfigWalletAirdropActivity;
import com.cryptowallet.deviantx.UI.Activities.FeaturedADAcivity;
import com.cryptowallet.deviantx.UI.Activities.RecentADHistoryAcivity;
import com.cryptowallet.deviantx.UI.Activities.WithdrawFundsAirdropActivity;
import com.cryptowallet.deviantx.UI.Adapters.FeaturedADHorizantalRAdapter;
import com.cryptowallet.deviantx.UI.Adapters.RecentADHistoryRAdapter;
import com.cryptowallet.deviantx.UI.Interfaces.AirdropWalletUIListener;
import com.cryptowallet.deviantx.UI.Models.AllCoins;
import com.cryptowallet.deviantx.UI.RoomDatabase.Database.DeviantXDB;
import com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB.AirdropWalletDao;
import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.AirdropWallet;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;
import com.cryptowallet.deviantx.Utilities.DeviantXApiClient;
import com.shehabic.droppy.DroppyMenuPopup;
import com.shehabic.droppy.animations.DroppyScaleAnimation;
import com.squareup.picasso.Picasso;
import com.zyyoona7.popup.EasyPopup;
import com.zyyoona7.popup.XGravity;
import com.zyyoona7.popup.YGravity;

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

public class AirDropFragment extends Fragment /*implements DroppyClickCallbackInterface, DroppyMenuPopup.OnDismissCallback */ {

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
    @BindView(R.id.lnr_empty_coins)
    LinearLayout lnr_empty_coins;


    FeaturedADHorizantalRAdapter featuredADHorizantalRAdapter;
    RecentADHistoryRAdapter recentADHistoryRAdapter;

    LinearLayoutManager layoutManagerHorizontal;
    LinearLayoutManager layoutManagerVertical;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;


    ArrayList<AllCoins> allCoinsList;
    int int_coin_id, int_coin_rank;
    Double dbl_coin_usdValue, dbl_coin_marketCap, dbl_coin_volume, dbl_coin_24h, dbl_coin_7d, dbl_coin_1m;
    String loginResponseData, loginResponseStatus, loginResponseMsg, str_coin_name, str_coin_code, str_coin_logo;


    ArrayList<com.cryptowallet.deviantx.UI.Models.AirdropWallet> airdropWalletlist;
    int int_ad_data_id, int_ad_coin_id, int_ad_coin_rank, int_ad_noOfDays;
    String str_data_ad_address, str_data_ad_privatekey, str_data_ad_passcode, str_data_ad_account, str_data_ad_coin, str_ad_coin_name, str_ad_coin_code, str_ad_coin_logo, str_ad_coin_chart_data;
    Double dbl_data_ad_balance, dbl_data_ad_balanceInUSD, dbl_ad_coin_usdValue, dbl_ad_coin_marketCap, dbl_ad_coin_volume, dbl_ad_coin_1m, dbl_ad_coin_7d, dbl_ad_coin_24h;
    String startDate;


    EasyPopup mRvPop;
    private float mLastX;
    private float mLastY;

    DeviantXDB deviantXDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.air_drop_fragment, container, false);

        ButterKnife.bind(this, view);
        deviantXDB = DeviantXDB.getDatabase(getActivity());

        allCoinsList = new ArrayList<>();
        airdropWalletlist = new ArrayList<>();

        sharedPreferences = getActivity().getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        layoutManagerHorizontal = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rview_fad_coins.setLayoutManager(layoutManagerHorizontal);
        layoutManagerVertical = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rview_radh_coins.setLayoutManager(layoutManagerVertical);


        seekbar_per.setEnabled(false);
//        seekbar_per.setProgress(95);
//        txt_seekbar_per.setText("0%");


//        featuredADHorizantalRAdapter = new FeaturedADHorizantalRAdapter(getActivity().getApplicationContext(), allCoinsList);
//        rview_fad_coins.setAdapter(featuredADHorizantalRAdapter);
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
////                showDialog(getActivity(), v.getRight() , v.getBottom());
////                showDialog(getActivity(), v.getRight() - (v.getWidth() * 5), v.getTop() - (v.getHeight() * 6));
////                showDialog(getActivity(), v.getX(), v.getY());
////                showDialog(getActivity(), v.getRight(), v.getBottom());
////                showDialog(this, view.getLeft()-(view.getWidth()*2), view.getTop()+(view.getHeight()*2));
//
////                initDroppyMenuFromXml(img_menu);
//
                initEvents(v);

            }
        });


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                onLoadAirDropWallet();
            }
        }, 200);


        return view;
    }

    //    private void showDialog(Context context, int x, int y) {
    private void showDialog(Context context, float x, float y) {
// x -->  X-Cordinate
        // y -->  Y-Cordinate
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_airdrop_menu);
        dialog.setCanceledOnTouchOutside(true);

        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
//        wmlp.gravity = Gravity.TOP | Gravity.LEFT;
        wmlp.x = (int) x;
        wmlp.y = (int) y;

        dialog.show();

        TextView txt_copy_wallet = dialog.findViewById(R.id.txt_copy_wallet);
        TextView txt_withdraw_funds = dialog.findViewById(R.id.txt_withdraw_funds);
        TextView txt_config_wallet = dialog.findViewById(R.id.txt_config_wallet);
        TextView txt_ad_info = dialog.findViewById(R.id.txt_ad_info);
        TextView txt_delete = dialog.findViewById(R.id.txt_delete);

        txt_copy_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                CommonUtilities.ShowToastMessage(getActivity(), /*getResources().getString(R.string.copy_wallet)*/airdropWalletlist.get(0).getStr_data_ad_address());
                CommonUtilities.copyToClipboard(getActivity(), airdropWalletlist.get(0).getStr_data_ad_address(), airdropWalletlist.get(0).getAllCoins().getStr_coin_name());
                dialog.dismiss();
            }
        });

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
                dialog.dismiss();
            }
        });

        txt_config_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ConfigWalletAirdropActivity.class);
                Bundle bundle = new Bundle();
//                bundle.putParcelable(CONSTANTS.selectedCoin, airdropWalletlist.get(0));
                bundle.putParcelableArrayList(CONSTANTS.selectedAccountWallet, airdropWalletlist);
                intent.putExtras(bundle);
                startActivity(intent);
//                CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.config_wallet));
                dialog.dismiss();
            }
        });

        txt_ad_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.ad_info));
                dialog.dismiss();
            }
        });

        txt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.delete));
                dialog.dismiss();
            }
        });


    }

    private void initDroppyMenuFromXml(ImageView btn) {
        DroppyMenuPopup.Builder droppyBuilder = new DroppyMenuPopup.Builder(getActivity(), btn);
        DroppyMenuPopup droppyMenu = droppyBuilder.fromMenu(R.menu.menu_items_airdrop)
                .triggerOnAnchorClick(false)
//                .setOnClick(this)
//                .setOnDismissCallback(this)
                .setPopupAnimation(new DroppyScaleAnimation())
                .build();
        droppyMenu.show();
    }

    private void initEvents(View v) {

        mRvPop = EasyPopup.create()
                .setContext(getActivity())
                .setContentView(R.layout.dialog_airdrop_menu)
//                .setAnimationStyle(R.style.RightTopPopAnim)
//                .setHeight(700)
//                .setWidth(600)
                .setFocusAndOutsideEnable(true)
                .setBackgroundDimEnable(true)
//                .setDimValue(0.5f)
//                .setDimColor(Color.RED)
//                .setDimView(mTitleBar)
                .apply();


        TextView txt_copy_wallet = mRvPop.findViewById(R.id.txt_copy_wallet);
        TextView txt_withdraw_funds = mRvPop.findViewById(R.id.txt_withdraw_funds);
        TextView txt_config_wallet = mRvPop.findViewById(R.id.txt_config_wallet);
        TextView txt_ad_info = mRvPop.findViewById(R.id.txt_ad_info);
        TextView txt_delete = mRvPop.findViewById(R.id.txt_delete);

        txt_copy_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                CommonUtilities.ShowToastMessage(getActivity(), /*getResources().getString(R.string.copy_wallet)*/airdropWalletlist.get(0).getStr_data_ad_address());
                CommonUtilities.copyToClipboard(getActivity(), airdropWalletlist.get(0).getStr_data_ad_address(), airdropWalletlist.get(0).getAllCoins().getStr_coin_name());
                mRvPop.dismiss();
            }
        });

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

        txt_config_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ConfigWalletAirdropActivity.class);
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
            progressDialog = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.please_wait), true);
            AirdropWalletControllerApi apiService = DeviantXApiClient.getClient().create(AirdropWalletControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.getAirdropWallet(CONSTANTS.DeviantMulti + token);
            apiResponse.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responsevalue = response.body().string();

                        if (!responsevalue.isEmpty() && responsevalue != null) {
                            progressDialog.dismiss();
                            updateUI(responsevalue);
                            AirdropWalletDao mDao = deviantXDB.airdropWalletDao();
                            AirdropWallet airdropWallet = new AirdropWallet(1, responsevalue);
                            mDao.insertAirdropWallet(airdropWallet);

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
        myApplication.setAirdropWalletUIListener(airdropWalletUIListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myApplication.setAirdropWalletUIListener(null);
    }

    AirdropWalletUIListener airdropWalletUIListener = new AirdropWalletUIListener() {
        @Override
        public void onChangedAirdropWallet(String airdropWalletList) {
            updateUI(airdropWalletList);
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
                        JSONArray jsonArrayData = new JSONArray(loginResponseData);
                        airdropWalletlist = new ArrayList<>();
                        for (int i = 0; i < jsonArrayData.length(); i++) {
                            JSONObject jsonObjectData = jsonArrayData.getJSONObject(i);
                            try {
                                int_ad_data_id = jsonObjectData.getInt("id");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
//                                    try {
//                                        isFav = jsonObjectData.getBoolean("fav");
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
                            try {
                                str_data_ad_address = jsonObjectData.getString("address");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                str_data_ad_privatekey = jsonObjectData.getString("airdropPrivatekey");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                str_data_ad_passcode = jsonObjectData.getString("airdropPasscode");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                dbl_data_ad_balance = jsonObjectData.getDouble("balance");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                dbl_data_ad_balanceInUSD = jsonObjectData.getDouble("balanceInUSD");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                startDate = jsonObjectData.getString("airdropStartDate");
                                       /* if (startDate.equals("null")) {
                                            startDate = "0";
                                        }*/

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                str_data_ad_account = jsonObjectData.getString("account");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                str_data_ad_coin = jsonObjectData.getString("coin");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                int_ad_noOfDays = jsonObjectData.getInt("noOfDays");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            JSONObject jsonObjectCoins = new JSONObject(str_data_ad_coin);

                            try {
                                int_ad_coin_id = jsonObjectCoins.getInt("id");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                str_ad_coin_name = jsonObjectCoins.getString("name");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                str_ad_coin_code = jsonObjectCoins.getString("code");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                str_ad_coin_logo = jsonObjectCoins.getString("logo");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                dbl_ad_coin_usdValue = jsonObjectCoins.getDouble("usdValue");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                int_ad_coin_rank = jsonObjectCoins.getInt("rank");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                dbl_ad_coin_marketCap = jsonObjectCoins.getDouble("marketCap");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                dbl_ad_coin_volume = jsonObjectCoins.getDouble("volume");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                dbl_ad_coin_24h = jsonObjectCoins.getDouble("change24H");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                dbl_ad_coin_7d = jsonObjectCoins.getDouble("change7D");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                dbl_ad_coin_1m = jsonObjectCoins.getDouble("change1M");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                str_ad_coin_chart_data = jsonObjectCoins.getString("chartData");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            AllCoins allCoins = new AllCoins(int_ad_coin_id, str_ad_coin_name, str_ad_coin_code, str_ad_coin_logo, dbl_ad_coin_usdValue,
                                    int_ad_coin_rank, dbl_ad_coin_marketCap, dbl_ad_coin_volume, dbl_ad_coin_24h, dbl_ad_coin_7d, dbl_ad_coin_1m, false, str_ad_coin_chart_data);
                            airdropWalletlist.add(new com.cryptowallet.deviantx.UI.Models.AirdropWallet(startDate, int_ad_data_id, str_data_ad_address, str_data_ad_privatekey,
                                    str_data_ad_passcode, dbl_data_ad_balance, dbl_data_ad_balanceInUSD,
                                    str_data_ad_account, int_ad_noOfDays, allCoins));
                        }
                        Picasso.with(getActivity()).load(airdropWalletlist.get(0).getAllCoins().getStr_coin_logo()).into(img_coin_icon);
                        txt_coin_name_code.setText(airdropWalletlist.get(0).getAllCoins().getStr_coin_name() + " (" + airdropWalletlist.get(0).getAllCoins().getStr_coin_code() + " )");
                        txt_coin_address.setText(airdropWalletlist.get(0).getStr_data_ad_address());
                        if (myApplication.getHideBalance()) {
                            txt_holding_bal.setText("***");
                        } else {
                            txt_holding_bal.setText(String.format("%.4f", airdropWalletlist.get(0).getDbl_data_ad_balance()));
                        }

                        if (airdropWalletlist.get(0).getStartDate().equals("null")) {
                            txt_holding_days.setText("0 Days");
                            seekbar_per.setProgress(0);
                            txt_seekbar_per.setText("0" + "%");
                        } else {
                            txt_holding_days.setText(getDays(airdropWalletlist.get(0).getStartDate()));
                            seekbar_per.setProgress(getDaysPer(airdropWalletlist.get(0).getStartDate(), airdropWalletlist.get(0).getInt_ad_noOfDays()));
                            txt_seekbar_per.setText(getDaysPer(airdropWalletlist.get(0).getStartDate(), airdropWalletlist.get(0).getInt_ad_noOfDays()) + "%");
                        }

//                                if (airdropWalletlist.get(0).getStr_data_ad_address().length() < 15) {
                        txt_coin_address.setText(airdropWalletlist.get(0).getStr_data_ad_address());
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
                    updateUI(walletResult);
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

/*
    @Override
    public void call(View v, int id) {
        switch (id) {
            case R.id.item_copy_wallet:
                CommonUtilities.copyToClipboard(getActivity(), airdropWalletlist.get(0).getStr_data_ad_address(), airdropWalletlist.get(0).getAllCoins().getStr_coin_name());
                break;
            case R.id.item_withdraw_funds:
                Intent intent = new Intent(getActivity(), WithdrawFundsAirdropActivity.class);
                Bundle bundle = new Bundle();
//                bundle.putParcelable(CONSTANTS.selectedCoin, airdropWalletlist.get(0));
                bundle.putParcelableArrayList(CONSTANTS.selectedAccountWallet, airdropWalletlist);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.item_config_wallet:
                Intent intent1 = new Intent(getActivity(), ConfigWalletAirdropActivity.class);
                Bundle bundle1 = new Bundle();
//                bundle.putParcelable(CONSTANTS.selectedCoin, airdropWalletlist.get(0));
                bundle1.putParcelableArrayList(CONSTANTS.selectedAccountWallet, airdropWalletlist);
                intent1.putExtras(bundle1);
                startActivity(intent1);
                break;
            case R.id.item_info_ad:
                ;
                break;
            case R.id.item_delete:
                ;
                break;

            default:
                break;
        }

    }

    @Override
    public void call() {

    }
*/
}
