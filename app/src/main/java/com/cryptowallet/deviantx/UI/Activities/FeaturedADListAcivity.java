package com.cryptowallet.deviantx.UI.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.ServiceAPIs.UserAirdropControllerApi;
import com.cryptowallet.deviantx.UI.Adapters.FeaturedADVerticalRAdapter;
import com.cryptowallet.deviantx.UI.Models.FeaturedAirdrops;
import com.cryptowallet.deviantx.UI.RoomDatabase.Database.DeviantXDB;
import com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB.FeaturedAirdropsDao;
import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.FeaturedAirdropsDB;
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

public class FeaturedADListAcivity extends AppCompatActivity {

    @BindView(R.id.toolbar_center_back)
    Toolbar toolbar_center_back;
    @BindView(R.id.edt_search)
    EditText edt_search;
    @BindView(R.id.img_filter)
    ImageView img_filter;
    @BindView(R.id.rview_ad_coins_list)
    RecyclerView rview_ad_coins_list;
    @BindView(R.id.lnr_search)
    LinearLayout lnr_search;
    @BindView(R.id.lnr_empty_feat_coins)
    LinearLayout lnr_empty_feat_coins;


    FeaturedADVerticalRAdapter featuredADVerticalRAdapter;
    GridLayoutManager layoutManager;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;
    DeviantXDB deviantXDB;


    String loginResponseData, loginResponseStatus, loginResponseMsg;
    ArrayList<FeaturedAirdrops> allFeaturedAirdrops;

    @Override
    protected void onResume() {
        super.onResume();
        myApplication.disableScreenCapture(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_featured_ad_list);

        ButterKnife.bind(this);
        deviantXDB = DeviantXDB.getDatabase(this);

        allFeaturedAirdrops = new ArrayList<>();

        toolbar_center_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        sharedPreferences = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        layoutManager = new GridLayoutManager(FeaturedADListAcivity.this, 2, GridLayoutManager.VERTICAL, false);
        rview_ad_coins_list.setLayoutManager(layoutManager);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                onLoadUserAirdrops();
            }
        }, 100);


        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ArrayList<FeaturedAirdrops> searchCoinsList = new ArrayList<>();
                for (FeaturedAirdrops coinName : allFeaturedAirdrops) {
                    if (coinName.getStr_coinName().toLowerCase().contains(s.toString().toLowerCase())) {
                        searchCoinsList.add(coinName);
                    }
                }
                featuredADVerticalRAdapter = new FeaturedADVerticalRAdapter(FeaturedADListAcivity.this, searchCoinsList);
                rview_ad_coins_list.setAdapter(featuredADVerticalRAdapter);
            }
        });

    }

    private void onLoadUserAirdrops() {

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                FeaturedAirdropsDao featuredAirdropsDao = deviantXDB.featuredAirdropsDao();
                if ((featuredAirdropsDao.getFeaturedAirdrops()) != null) {
                    String walletResult = featuredAirdropsDao.getFeaturedAirdrops().featuredAirdrops;
                    updateUIUserAirdrops(walletResult);
                } else {
                    if (CommonUtilities.isConnectionAvailable(FeaturedADListAcivity.this)) {
                        fetchCoinsUserAirdrops();
                    } else {
                        CommonUtilities.ShowToastMessage(FeaturedADListAcivity.this, getResources().getString(R.string.internetconnection));
                    }
                }
            }
        });

    }

    private void updateUIUserAirdrops(String responsevalue) {
        FeaturedADListAcivity.this.runOnUiThread(new Runnable() {
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
                            lnr_empty_feat_coins.setVisibility(View.GONE);
                            lnr_search.setVisibility(View.VISIBLE);
                            rview_ad_coins_list.setVisibility(View.VISIBLE);
                            featuredADVerticalRAdapter = new FeaturedADVerticalRAdapter(FeaturedADListAcivity.this, allFeaturedAirdrops);
                            rview_ad_coins_list.setAdapter(featuredADVerticalRAdapter);
                        } else {
                            lnr_search.setVisibility(View.GONE);
                            rview_ad_coins_list.setVisibility(View.GONE);
                            lnr_empty_feat_coins.setVisibility(View.VISIBLE);
                        }
                        featuredADVerticalRAdapter.notifyDataSetChanged();

                    } else {
                        CommonUtilities.ShowToastMessage(FeaturedADListAcivity.this, loginResponseMsg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void fetchCoinsUserAirdrops() {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            progressDialog = ProgressDialog.show(FeaturedADListAcivity.this, "", getResources().getString(R.string.please_wait), true);
            UserAirdropControllerApi apiService = DeviantXApiClient.getClient().create(UserAirdropControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.getUserAD(CONSTANTS.DeviantMulti + token);
            apiResponse.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responsevalue = response.body().string();
                        progressDialog.dismiss();

                        if (!responsevalue.isEmpty() && responsevalue != null) {
                            updateUIUserAirdrops(responsevalue);
                            FeaturedAirdropsDao mDao = deviantXDB.featuredAirdropsDao();
                            FeaturedAirdropsDB featuredAirdropsDB = new FeaturedAirdropsDB(1, responsevalue);
                            mDao.insertFeaturedAirdrops(featuredAirdropsDB);

                        } else {
                            CommonUtilities.ShowToastMessage(FeaturedADListAcivity.this, loginResponseMsg);
//                            Toast.makeText(getApplicationContext(), responsevalue, Toast.LENGTH_LONG).show();
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(FeaturedADListAcivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(FeaturedADListAcivity.this, getResources().getString(R.string.Timeout));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Timeout), Toast.LENGTH_SHORT).show();
                    } else if (t instanceof java.net.ConnectException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(FeaturedADListAcivity.this, getResources().getString(R.string.networkerror));
                        Toast.makeText(FeaturedADListAcivity.this, getResources().getString(R.string.networkerror), Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(FeaturedADListAcivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception ex) {
            progressDialog.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(FeaturedADListAcivity.this, getResources().getString(R.string.errortxt));
//            Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
        }

    }

}
