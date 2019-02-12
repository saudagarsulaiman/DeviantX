package com.cryptowallet.deviantx.UI.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.UI.Models.AccountWallet;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cryptowallet.deviantx.Utilities.MyApplication.myApplication;

public class WalletOptionsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_center_back)
    Toolbar toolbar_center_back;
    @BindView(R.id.lnr_wallet_details)
    LinearLayout lnr_wallet_details;
    @BindView(R.id.lnr_sweep_wallet)
    LinearLayout lnr_sweep_wallet;
    @BindView(R.id.lnr_message)
    LinearLayout lnr_message;
    @BindView(R.id.lnr_wallet_history)
    LinearLayout lnr_wallet_history;
    @BindView(R.id.lnr_received_history)
    LinearLayout lnr_received_history;
    @BindView(R.id.lnr_sent_history)
    LinearLayout lnr_sent_history;


    AccountWallet selectedAccountWallet;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;


    @Override
    protected void onResume() {
        super.onResume();
        myApplication.disableScreenCapture(this);
//        CommonUtilities.serviceStart(WalletOptionsActivity.this);
    }

  /*  @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_HOME) {
//            Log.e("home key pressed", "****");
            // write your code here to stop the activity
            CommonUtilities.serviceStop(WalletOptionsActivity.this);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
//        Log.e("home key pressed on pause", "****");
        // write your code here to stop your service
        CommonUtilities.serviceStop(WalletOptionsActivity.this);
        super.onPause();
    }*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_options);

        ButterKnife.bind(this);

        sharedPreferences = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        final Bundle bundle = getIntent().getExtras();
        selectedAccountWallet = bundle.getParcelable(CONSTANTS.selectedAccountWallet);

        toolbar_center_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        lnr_wallet_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificationDialog(selectedAccountWallet);
            }
        });
        lnr_sweep_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent= new Intent(WalletOptionsActivity.this,.class);
//                Bundle bundle1 = new Bundle();
//                bundle1.putParcelable(CONSTANTS.selectedAccountWallet, selectedAccountWallet);
//                intent.putExtras(bundle);
//                startActivity(intent);
            }
        });
        lnr_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent= new Intent(WalletOptionsActivity.this,.class);
//                Bundle bundle1 = new Bundle();
//                bundle1.putParcelable(CONSTANTS.selectedAccountWallet, selectedAccountWallet);
//                intent.putExtras(bundle);
//                startActivity(intent);
            }
        });
        lnr_wallet_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WalletOptionsActivity.this, WalletHistoryActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putParcelable(CONSTANTS.selectedAccountWallet, selectedAccountWallet);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        lnr_sent_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WalletOptionsActivity.this, WalletHistoryActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putParcelable(CONSTANTS.selectedAccountWallet, selectedAccountWallet);
                bundle1.putString(CONSTANTS.transType,CONSTANTS.sent);
                intent.putExtras(bundle1);
                startActivity(intent);
            }
        });
        lnr_received_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WalletOptionsActivity.this, WalletHistoryActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putParcelable(CONSTANTS.selectedAccountWallet, selectedAccountWallet);
                bundle1.putString(CONSTANTS.transType,CONSTANTS.received);
                intent.putExtras(bundle1);
                startActivity(intent);
            }
        });


    }

    private void verificationDialog(final AccountWallet selectedAccountWallet) {


        ViewHolder viewHolder = new ViewHolder(R.layout.dialog_verification);
        final DialogPlus dialog = DialogPlus.newDialog(WalletOptionsActivity.this)
                .setContentHolder(viewHolder)
                .setGravity(Gravity.BOTTOM)
                .setCancelable(true)
                .setInAnimation(R.anim.slide_in_bottom)
                .setOutAnimation(R.anim.slide_out_bottom)
                .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
//                        .setOnDismissListener(new OnDismissListener() {
//                            @Override
//                            public void onDismiss(DialogPlus dialog) {
//
//                            }
//                        })
//                        .setExpanded(true) // default is false, only works for grid and list
                .create();

//                Initializing Widgets
        View view = dialog.getHolderView();

        final EditText edt_pswd = view.findViewById(R.id.edt_pswd);
        Button btn_submit = view.findViewById(R.id.btn_submit);


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String entered_pswd = edt_pswd.getText().toString().trim();
                if (!entered_pswd.isEmpty()) {
                    String main_pswd = sharedPreferences.getString(CONSTANTS.pswd, "");
                    if (main_pswd.equals(entered_pswd)) {
                        Intent intent = new Intent(WalletOptionsActivity.this, WalletDetailsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(CONSTANTS.selectedAccountWallet, selectedAccountWallet);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else {
                        CommonUtilities.ShowToastMessage(WalletOptionsActivity.this, getResources().getString(R.string.unmatch_pswd));
                    }
                } else {
                    CommonUtilities.ShowToastMessage(WalletOptionsActivity.this, getResources().getString(R.string.empty_pswd));
                }

                dialog.dismiss();
            }
        });

//                Displaying DialogPlus
        dialog.show();

    }
}
