package com.cryptowallet.deviantx.UI.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.ServiceAPIs.CoinsControllerApi;
import com.cryptowallet.deviantx.UI.Adapters.MyAdapter;
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

public class ShapeShiftActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_center_back)
    Toolbar toolbar_center_back;
    @BindView(R.id.edt_exc_amt)
    EditText edt_exc_amt;
    @BindView(R.id.txt_exc_amt_code)
    TextView txt_exc_amt_code;
    @BindView(R.id.spnr_amt_exc)
    Spinner spnr_amt_exc;
    @BindView(R.id.lnr_interchange)
    LinearLayout lnr_interchange;
    @BindView(R.id.edt_rec_amt)
    EditText edt_rec_amt;
    @BindView(R.id.txt_rec_amt_code)
    TextView txt_rec_amt_code;
    @BindView(R.id.spnr_amt_rec)
    Spinner spnr_amt_rec;
    @BindView(R.id.btn_continue)
    Button btn_continue;



    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;

    int int_coin_id;
    Double dbl_coin_usdValue;
    String loginResponseData, loginResponseStatus, loginResponseMsg, str_coin_name, str_coin_code, str_coin_logo;
    ArrayList<AllCoins> allCoinsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shape_shift);

        ButterKnife.bind(this);
        
        sharedPreferences = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        allCoinsList = new ArrayList<>();

        toolbar_center_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (CommonUtilities.isConnectionAvailable(ShapeShiftActivity.this)) {
            //GET ALL COINS
            fetchCoins();
        } else {
            CommonUtilities.ShowToastMessage(ShapeShiftActivity.this, getResources().getString(R.string.internetconnection));
        }


        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String exc_amt = edt_exc_amt.getText().toString();
                String rec_amt = edt_rec_amt.getText().toString();
//
//                if (exc_amt.isEmpty()){
//
//                }else {
//                    CommonUtilities.ShowToastMessage(ShapeShiftActivity.this,getResources().getString(R.string.empty_exc));
//                }


            }
        });

    }


    private void fetchCoins() {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            progressDialog = ProgressDialog.show(ShapeShiftActivity.this, "", getResources().getString(R.string.please_wait), true);
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
                                spnr_amt_exc.setAdapter(new MyAdapter(ShapeShiftActivity.this, R.layout.spinner_row_lyt, allCoinsList));
                                spnr_amt_rec.setAdapter(new MyAdapter(ShapeShiftActivity.this, R.layout.spinner_row_lyt, allCoinsList));

                            } else {
                                CommonUtilities.ShowToastMessage(ShapeShiftActivity.this, loginResponseMsg);
                            }

                        } else {
                            CommonUtilities.ShowToastMessage(ShapeShiftActivity.this, loginResponseMsg);
//                            Toast.makeText(getApplicationContext(), responsevalue, Toast.LENGTH_LONG).show();
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(ShapeShiftActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(ShapeShiftActivity.this, getResources().getString(R.string.Timeout));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Timeout), Toast.LENGTH_SHORT).show();
                    } else if (t instanceof java.net.ConnectException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(ShapeShiftActivity.this, getResources().getString(R.string.networkerror));
                        Toast.makeText(ShapeShiftActivity.this, getResources().getString(R.string.networkerror), Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(ShapeShiftActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception ex) {
            progressDialog.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(ShapeShiftActivity.this, getResources().getString(R.string.errortxt));
//            Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
        }

    }


}
