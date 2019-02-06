package com.cryptowallet.deviantx.UI.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.ServiceAPIs.WalletControllerApi;
import com.cryptowallet.deviantx.UI.Activities.ExchangeOrderHistoryActivity;
import com.cryptowallet.deviantx.UI.Adapters.ExpandableListViewAdapter;
import com.cryptowallet.deviantx.UI.Interfaces.WalletDetailsUIListener;
import com.cryptowallet.deviantx.UI.Models.WalletDetails;
import com.cryptowallet.deviantx.UI.RoomDatabase.Database.DeviantXDB;
import com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB.WalletDetailsDao;
import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.WalletDetailsDB;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;
import com.cryptowallet.deviantx.Utilities.DeviantXApiClient;
import com.cryptowallet.deviantx.Utilities.GsonUtils;

import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cryptowallet.deviantx.Utilities.MyApplication.myApplication;

public class ExchangeFundsFragment extends Fragment {

    @BindView(R.id.expandable_list_view)
    ExpandableListView expandable_list_view;
    @BindView(R.id.img_history)
    ImageView img_history;

    private ExpandableListViewAdapter expandableListViewAdapter;

    View view;

    private ArrayList<String> listDataHeader, valuesList;
    ArrayList<WalletDetails> SubHeader/*, SubHeader1, SubHeader2, SubHeader3*/;
    private HashMap<String, ArrayList<WalletDetails>> listDataChild;
    String loginResponseData, loginResponseStatus, loginResponseMsg;


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    //    ProgressDialog progressDialog;
    private ArrayList<WalletDetails> allWalletDetailsList;
    DeviantXDB deviantXDB;

    @Override
    public void onResume() {
        super.onResume();
        myApplication.setWalletDetailsUIListener(walletDetailsUIListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myApplication.setWalletDetailsUIListener(null);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.exchange_funds_fragment, container, false);
        ButterKnife.bind(this, view);
        deviantXDB = DeviantXDB.getDatabase(getActivity());

        sharedPreferences = getActivity().getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<String, ArrayList<WalletDetails>>();
        SubHeader = new ArrayList<>();
        valuesList = new ArrayList<>();
/*        SubHeader1 = new ArrayList<AllCoins>();
        SubHeader2 = new ArrayList<AllCoins>();
        SubHeader3 = new ArrayList<AllCoins>();
     */
        allWalletDetailsList = new ArrayList<>();

//        listDataHeader.add("Trade Wallet");
//        listDataHeader.add("My Test Wallet");
//        listDataHeader.add("Holding Wallet");
//        listDataHeader.add("My Test1 Wallet");

        /*SubHeader.add(new AllCoins(1, "Deviantcoin", "DEV",
         *//*getResources().getDrawable(R.drawable.ic_dlg)*//*
                "logo", 349.52, 5, 6511321.565,
                5461616.225, 64.56, 54.65, 45.54));

        SubHeader1.add(new AllCoins(2, "Bitcoin", "BTC",
                *//*getResources().getDrawable(R.drawable.ic_dlg)*//*
                "logo", 546.52, 8, 4453165.565,
                553215.225, 88.56, 55.65, 54.54));
        SubHeader1.add(new AllCoins(3, "Bitcoin Diamond", "BTCD",
                *//*getResources().getDrawable(R.drawable.ic_dlg)*//*
                "logo", 465.57, 78, 4453165.565,
                553215.225, 88.56, 55.65, 54.54));

        SubHeader2.add(new AllCoins(3, "Ethereum", "ETH",
                *//*getResources().getDrawable(R.drawable.ic_dlg)*//*
                "logo", 874.65, 8, 65431.565,
                5461616.225, 64.56, 54.65, 45.54));

        SubHeader3.add(new AllCoins(4, "Litcoin", "LTC",
                *//*getResources().getDrawable(R.drawable.ic_dlg)*//*
                "logo", 5453.52, 18, 54.5,
                457.56, 88.56, 55.65, 54.54));
*/
       /* listDataChild.put(listDataHeader.get(0), SubHeader);
        listDataChild.put(listDataHeader.get(1), SubHeader1);
        listDataChild.put(listDataHeader.get(2), SubHeader2);
        listDataChild.put(listDataHeader.get(3), SubHeader3);
*/
//        expandableListViewAdapter = new ExpandableListViewAdapter(getActivity(), listDataHeader, listDataChild);
//        expandable_list_view.setAdapter(expandableListViewAdapter);
//        expandable_list_view.setBackgroundResource(R.drawable.rec_gray_white);


//        Closing/Collapsing Previously Opened ListView
        expandable_list_view.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousItem = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousItem)
                    expandable_list_view.collapseGroup(previousItem);
                previousItem = groupPosition;
            }
        });

        img_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ExchangeOrderHistoryActivity.class);
                startActivity(intent);
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                onLoadWalletDetails();
            }
        }, 200);

        return view;
    }


    //    **************GETTING WALLET DETAILS**************
    private void onLoadWalletDetails() {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                WalletDetailsDao walletDetailsDao = deviantXDB.walletDetailsDao();
                if ((walletDetailsDao.getAllWalletDetails()) != null) {
                    String walletResult = walletDetailsDao.getAllWalletDetails().walletDetails;
                    updateUIWalletDetails(walletResult);
                } else {
                    if (CommonUtilities.isConnectionAvailable(getActivity())) {
                        fetchCoinsWalletDetails();
                    } else {
                        CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.internetconnection));
                    }
                }
            }
        });

    }

    WalletDetailsUIListener walletDetailsUIListener = new WalletDetailsUIListener() {
        @Override
        public void onChangedWalletDetails(String allWalletDetails) {
            updateUIWalletDetails(allWalletDetails);
        }

    };

    private void updateUIWalletDetails(String responsevalue) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject(responsevalue);
                    loginResponseMsg = jsonObject.getString("msg");
                    loginResponseStatus = jsonObject.getString("status");

                    if (loginResponseStatus.equals("true")) {
                        loginResponseData = jsonObject.getString("data");
                        WalletDetails[] coinsStringArray = GsonUtils.getInstance().fromJson(loginResponseData, WalletDetails[].class);
                        allWalletDetailsList = new ArrayList<WalletDetails>(Arrays.asList(coinsStringArray));

                        if (allWalletDetailsList.size() > 0) {
                            expandable_list_view.setVisibility(View.VISIBLE);
                           /* listDataHeader = new ArrayList<>();
                            SubHeader = new ArrayList<>();


                            for (int i = 0; i < allWalletDetailsList.size(); i++) {
                                listDataHeader.add(allWalletDetailsList.get(i).getStr_data_walletName());

                            }
                            for (int j = 0; j < listDataHeader.size(); j++) {
                                SubHeader = new ArrayList<>();
                                valuesList = new ArrayList<>();
//                                valuesList.add(allWalletDetailsList.get(j).getStr_data_values());
                                for (int i = 0; i < valuesList.size(); i++) {
                                    SubHeader.add(allWalletDetailsList.get(i));
                                }
                                listDataChild.put(listDataHeader.get(j), SubHeader);
                            }*/


                            expandableListViewAdapter = new ExpandableListViewAdapter(getActivity(), /*listDataHeader, listDataChild*/allWalletDetailsList);
                            expandable_list_view.setAdapter(expandableListViewAdapter);
                        } else {
                            expandable_list_view.setVisibility(View.GONE);
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

    private void fetchCoinsWalletDetails() {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
//            progressDialog = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.please_wait), true);
            WalletControllerApi apiService = DeviantXApiClient.getClient().create(WalletControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.getAllWalletsDetails(CONSTANTS.DeviantMulti + token);
            apiResponse.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responsevalue = response.body().string();
//                        progressDialog.dismiss();

                        if (!responsevalue.isEmpty() && responsevalue != null) {
                            updateUIWalletDetails(responsevalue);
//                            progressDialog.dismiss();
                            WalletDetailsDao mDao = deviantXDB.walletDetailsDao();
                            WalletDetailsDB walletDetailsDB = new WalletDetailsDB(1, responsevalue);
                            mDao.insertWalletDetails(walletDetailsDB);

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
