package com.cryptowallet.deviantx.UI.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.ServiceAPIs.WalletControllerApi;
import com.cryptowallet.deviantx.UI.Services.WalletDataFetch;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;
import com.cryptowallet.deviantx.Utilities.DeviantXApiClient;

import org.json.JSONObject;

import java.net.SocketTimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cryptowallet.deviantx.Utilities.MyApplication.myApplication;

public class SetUpWalletActivity extends AppCompatActivity {

    @BindView(R.id.edt_wallet)
    EditText edt_wallet;
    @BindView(R.id.txt_note_defWal)
    TextView txt_note_defWal;
    @BindView(R.id.btn_create)
    Button btn_create;
    @BindView(R.id.scompat_defWallet)
    SwitchCompat scompat_defWallet;
    @BindView(R.id.scompat_pin)
    SwitchCompat scompat_pin;
    @BindView(R.id.toolbar_center_back)
    Toolbar toolbar_center_back;


    String s_WalletName;
    private Boolean exit = false;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String loginResponseData, loginResponseStatus, loginResponseMsg;
    ProgressDialog progressDialog;

    @Override
    protected void onRestart() {
        super.onRestart();
        myApplication.disableScreenCapture(this);
    }

   /* @Override
    protected void onResume() {
        super.onResume();
        CommonUtilities.serviceStart(SetUpWalletActivity.this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_HOME) {
//            Log.e("home key pressed", "****");
            // write your code here to stop the activity
            CommonUtilities.serviceStop(SetUpWalletActivity.this);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
//        Log.e("home key pressed on pause", "****");
        // write your code here to stop your service
        CommonUtilities.serviceStop(SetUpWalletActivity.this);
        super.onPause();
    }*/


    boolean firstTimeCreation = false, emptyWallet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_wallet);

        ButterKnife.bind(this);

        sharedPreferences = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        firstTimeCreation = sharedPreferences.getBoolean(CONSTANTS.first_wallet, false);
        emptyWallet = sharedPreferences.getBoolean(CONSTANTS.empty_wallet, false);
        if (firstTimeCreation) {
            scompat_defWallet.setChecked(true);
            scompat_defWallet.setClickable(false);
            editor.putBoolean(CONSTANTS.first_wallet, false);
            editor.apply();
        } else {
            scompat_defWallet.setChecked(false);
            scompat_defWallet.setClickable(true);
        }

        if (scompat_defWallet.isChecked()) {
            txt_note_defWal.setVisibility(View.VISIBLE);
        } else {
            txt_note_defWal.setVisibility(View.INVISIBLE);
        }

        scompat_defWallet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (scompat_defWallet.isChecked()) {
                    txt_note_defWal.setVisibility(View.VISIBLE);
                } else {
                    txt_note_defWal.setVisibility(View.INVISIBLE);
                }
            }
        });


        toolbar_center_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s_WalletName = edt_wallet.getText().toString().trim();
                if (s_WalletName.isEmpty()) {
                    edt_wallet.setText("");
                    CommonUtilities.ShowToastMessage(SetUpWalletActivity.this, getResources().getString(R.string.empty_wallet));
                } else {
                    if (CommonUtilities.isConnectionAvailable(SetUpWalletActivity.this)) {
                        createWallet(s_WalletName);
                    } else {
                        CommonUtilities.ShowToastMessage(SetUpWalletActivity.this, getResources().getString(R.string.internetconnection));
                    }
                }
            }
        });

    }

    private void createWallet(String s_walletName) {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            progressDialog = ProgressDialog.show(SetUpWalletActivity.this, "", getResources().getString(R.string.please_wait), true);
            WalletControllerApi apiService = DeviantXApiClient.getClient().create(WalletControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.getAddNewWallet(CONSTANTS.DeviantMulti + token, s_walletName, scompat_defWallet.isChecked());
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
                                if (emptyWallet) {
                                    editor.putBoolean(CONSTANTS.empty_wallet, false);
                                    editor.putBoolean(CONSTANTS.first_wallet, false);
                                    editor.apply();
                                    Intent serviceIntent = new Intent(getApplicationContext(), WalletDataFetch.class);
                                    serviceIntent.putExtra("walletList", true);
                                    serviceIntent.putExtra("walletIsDefault", scompat_defWallet.isChecked());
                                    startService(serviceIntent);
                                    Intent intent = new Intent(SetUpWalletActivity.this, DashBoardActivity.class);
                                    startActivity(intent);
                                } else {
                                    Intent serviceIntent = new Intent(getApplicationContext(), WalletDataFetch.class);
                                    serviceIntent.putExtra("walletList", true);
                                    serviceIntent.putExtra("walletIsDefault", scompat_defWallet.isChecked());
                                    startService(serviceIntent);
                                    finish();
                                }
                            } else {
                                CommonUtilities.ShowToastMessage(SetUpWalletActivity.this, loginResponseMsg);
                            }
                        } else {
                            CommonUtilities.ShowToastMessage(SetUpWalletActivity.this, loginResponseMsg);
//                            Toast.makeText(getApplicationContext(), responsevalue, Toast.LENGTH_LONG).show();
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(SetUpWalletActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(SetUpWalletActivity.this, getResources().getString(R.string.Timeout));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Timeout), Toast.LENGTH_SHORT).show();
                    } else if (t instanceof java.net.ConnectException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(SetUpWalletActivity.this, getResources().getString(R.string.networkerror));
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.networkerror), Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(SetUpWalletActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception ex) {
            progressDialog.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(SetUpWalletActivity.this, getResources().getString(R.string.errortxt));
//            Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Back button listener.
     * Will close the application if the back button pressed twice.
     */
    @Override
    public void onBackPressed() {
//        if (exit) {
//            finishAffinity(); // Close all activites
//            System.exit(0);  // Releasing resources
//
//        } else {
//            Toast.makeText(this, "Press Back again to Exit.", Toast.LENGTH_SHORT).show();
//            exit = true;
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    exit = false;
//                }
//            }, 2 * 1000);
//        }
        super.onBackPressed();
    }

}
