package com.cryptowallet.deviantx.UI.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.ServiceAPIs.CoinsControllerApi;
import com.cryptowallet.deviantx.UI.Adapters.AllCoinsRAdapter;
import com.cryptowallet.deviantx.UI.Models.AllCoins;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;
import com.cryptowallet.deviantx.Utilities.DeviantXApiClient;

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

public class ExploreCoinsFragment extends Fragment {

    View view;
    @BindView(R.id.rview_all_coins)
    RecyclerView rview_all_coins;
    @BindView(R.id.edt_search)
    EditText edt_search;

    AllCoinsRAdapter allCoinsRAdapter;
    LinearLayoutManager layoutManager;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;

    int int_coin_id;
    Double dbl_coin_usdValue;
    String loginResponseData, loginResponseStatus, loginResponseMsg,  str_coin_name, str_coin_code, str_coin_logo;
    ArrayList<AllCoins> allCoinsList;
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.explore_coins_fragment, container, false);
        ButterKnife.bind(this, view);


        sharedPreferences = getActivity().getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        allCoinsList = new ArrayList<>();

        layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        rview_all_coins.setLayoutManager(layoutManager);
     
//        allCoinsRAdapter = new AllCoinsRAdapter(getActivity().getApplicationContext());
//        rview_all_coins.setAdapter(allCoinsRAdapter);

        if (CommonUtilities.isConnectionAvailable(getActivity())) {
            //GET ALL COINS
            fetchCoins();
        } else {
            CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.internetconnection));
        }

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ArrayList<AllCoins> searchCoinsList=new ArrayList<>();
                for (AllCoins coinName : allCoinsList){
                    if(coinName.getStr_coin_name().toLowerCase().contains(s.toString().toLowerCase())){
                        searchCoinsList.add(coinName);
                    }
                }
                allCoinsRAdapter = new AllCoinsRAdapter(getActivity(), searchCoinsList);
                rview_all_coins.setAdapter(allCoinsRAdapter);
            }
        });

        return view;
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
                                    allCoinsList.add(new AllCoins(int_coin_id, str_coin_name, str_coin_code, str_coin_logo, dbl_coin_usdValue));
                                }
                                allCoinsRAdapter = new AllCoinsRAdapter(getActivity(), allCoinsList);
                                rview_all_coins.setAdapter(allCoinsRAdapter);

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
