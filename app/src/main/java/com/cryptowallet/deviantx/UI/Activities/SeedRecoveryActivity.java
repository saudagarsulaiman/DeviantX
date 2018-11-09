package com.cryptowallet.deviantx.UI.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.ServiceAPIs.UserControllerApi;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;
import com.cryptowallet.deviantx.Utilities.DeviantXApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketTimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SeedRecoveryActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_center_back)
    Toolbar toolbar_center_back;
    @BindView(R.id.edt_seed)
    EditText edt_seed;
    @BindView(R.id.btn_submit)
    Button btn_submit;

    ProgressDialog progressDialog;

    String loginResponseMsg, loginResponseStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seed_recovery);

        ButterKnife.bind(this);

        toolbar_center_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String seed = edt_seed.getText().toString().trim();

                if (!seed.isEmpty()) {
                    checkSeed(seed);
                } else {
                    CommonUtilities.ShowToastMessage(SeedRecoveryActivity.this, getResources().getString(R.string.enter_seed));
                }
            }
        });


    }

    private void checkSeed(String seed) {
        try {
            JSONObject params = new JSONObject();
            try {
                params.put("seed", seed);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialog = ProgressDialog.show(SeedRecoveryActivity.this, "", getResources().getString(R.string.please_wait), true);
            UserControllerApi apiService = DeviantXApiClient.getClient().create(UserControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.restoreSeed(params.toString());
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
                                Intent intent = new Intent(SeedRecoveryActivity.this, RecoveryConfirmActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
//                                CommonUtilities.ShowToastMessage(SeedRecoveryActivity.this, getResources().getString(R.string.pswd_changed_succcess));
                            } else {
                                CommonUtilities.ShowToastMessage(SeedRecoveryActivity.this, loginResponseMsg);
                            }

                            CommonUtilities.ShowToastMessage(SeedRecoveryActivity.this, loginResponseMsg);
                        } else {
                            CommonUtilities.ShowToastMessage(SeedRecoveryActivity.this, loginResponseMsg);
//                            Toast.makeText(getApplicationContext(), responsevalue, Toast.LENGTH_LONG).show();
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(SeedRecoveryActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(SeedRecoveryActivity.this, getResources().getString(R.string.Timeout));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Timeout), Toast.LENGTH_SHORT).show();
                    } else if (t instanceof java.net.ConnectException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(SeedRecoveryActivity.this, getResources().getString(R.string.networkerror));
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.networkerror), Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(SeedRecoveryActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception ex) {
            progressDialog.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(SeedRecoveryActivity.this, getResources().getString(R.string.errortxt));
//            Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
        }

    }


}
