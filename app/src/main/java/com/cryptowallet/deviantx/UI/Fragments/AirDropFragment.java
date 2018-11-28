package com.cryptowallet.deviantx.UI.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import com.cryptowallet.deviantx.ServiceAPIs.CoinsControllerApi;
import com.cryptowallet.deviantx.UI.Activities.ConfigWalletAirdropActivity;
import com.cryptowallet.deviantx.UI.Activities.FeaturedADAcivity;
import com.cryptowallet.deviantx.UI.Activities.RecentADHistoryAcivity;
import com.cryptowallet.deviantx.UI.Activities.WithdrawFundsAirdropActivity;
import com.cryptowallet.deviantx.UI.Adapters.FeaturedADHorizantalRAdapter;
import com.cryptowallet.deviantx.UI.Adapters.RecentADHistoryRAdapter;
import com.cryptowallet.deviantx.UI.Models.AccountWallet;
import com.cryptowallet.deviantx.UI.Models.AirdropWallet;
import com.cryptowallet.deviantx.UI.Models.AllCoins;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;
import com.cryptowallet.deviantx.Utilities.DeviantXApiClient;
import com.shehabic.droppy.DroppyClickCallbackInterface;
import com.shehabic.droppy.DroppyMenuPopup;
import com.squareup.picasso.Picasso;

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


    ArrayList<AirdropWallet> airdropWalletlist;
    int int_ad_data_id, int_ad_coin_id, int_ad_coin_rank;
    String str_data_ad_address, str_data_ad_privatekey, str_data_ad_passcode, str_data_ad_account, str_data_ad_coin, str_ad_coin_name, str_ad_coin_code, str_ad_coin_logo, str_ad_coin_chart_data;
    Double dbl_data_ad_balance, dbl_data_ad_balanceInUSD, dbl_ad_coin_usdValue, dbl_ad_coin_marketCap, dbl_ad_coin_volume, dbl_ad_coin_1m, dbl_ad_coin_7d, dbl_ad_coin_24h;
    String timestamp;

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.air_drop_fragment, container, false);

        ButterKnife.bind(this, view);

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
                showDialog(getActivity(), v.getRight() - (v.getWidth() * 5), v.getTop() - (v.getHeight() * 6));
//                showDialog(getActivity(), v.getX(), v.getY());
//                showDialog(getActivity(), v.getRight(), v.getBottom());
//                showDialog(this, view.getLeft()-(view.getWidth()*2), view.getTop()+(view.getHeight()*2));
            }
        });

        if (CommonUtilities.isConnectionAvailable(getActivity())) {
//            GET AIRDROP WALLET
            fetchAirdropWallet();
          /*  //GET ALL COINS
            fetchCoins();*/
        } else {
            CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.internetconnection));
        }

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

                            JSONObject jsonObject = new JSONObject(responsevalue);
                            loginResponseMsg = jsonObject.getString("msg");
                            loginResponseStatus = jsonObject.getString("status");

                            if (loginResponseStatus.equals("true")) {
                                loginResponseData = jsonObject.getString("data");
                                JSONArray jsonArrayData = new JSONArray(loginResponseData);
                                for (int i = 0; i < jsonArrayData.length(); i++) {
                                    JSONObject jsonObjectCoins = jsonArrayData.getJSONObject(i);

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

                                    allCoinsList.add(new AllCoins(int_coin_id, str_coin_name, str_coin_code, str_coin_logo, dbl_coin_usdValue, int_coin_rank, dbl_coin_marketCap, dbl_coin_volume, dbl_coin_24h, dbl_coin_7d, dbl_coin_1m));
                                }
                                featuredADHorizantalRAdapter = new FeaturedADHorizantalRAdapter(getActivity(), allCoinsList);
                                rview_fad_coins.setAdapter(featuredADHorizantalRAdapter);

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
                                        timestamp = jsonObjectData.getString("airdropStartDate");
                                        if (timestamp.equals("null")) {
                                            timestamp = "0";
                                        }

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
                                            int_ad_coin_rank, dbl_ad_coin_marketCap, dbl_ad_coin_volume, dbl_ad_coin_24h, dbl_ad_coin_7d, dbl_ad_coin_1m, str_ad_coin_chart_data);
                                    airdropWalletlist.add(new AirdropWallet(timestamp, int_ad_data_id, str_data_ad_address, str_data_ad_privatekey,
                                            str_data_ad_passcode, dbl_data_ad_balance, dbl_data_ad_balanceInUSD,
                                            str_data_ad_account, allCoins));
                                }
                                Picasso.with(getActivity()).load(airdropWalletlist.get(0).getAllCoins().getStr_coin_logo()).into(img_coin_icon);
                                txt_coin_name_code.setText(airdropWalletlist.get(0).getAllCoins().getStr_coin_name() + " (" + airdropWalletlist.get(0).getAllCoins().getStr_coin_code() + " )");
                                txt_coin_address.setText(airdropWalletlist.get(0).getStr_data_ad_address());
                                txt_holding_bal.setText(String.format("%.4f", airdropWalletlist.get(0).getDbl_data_ad_balance()));
                                txt_holding_days.setText(airdropWalletlist.get(0).getTimestamp());

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

}
