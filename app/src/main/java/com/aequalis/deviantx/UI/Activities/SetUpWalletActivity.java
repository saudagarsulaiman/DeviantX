package com.aequalis.deviantx.UI.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aequalis.deviantx.R;
import com.aequalis.deviantx.Utilities.CONSTANTS;
import com.aequalis.deviantx.Utilities.CommonUtilities;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SetUpWalletActivity extends AppCompatActivity {

    @BindView(R.id.edt_wallet)
    EditText edt_wallet;
    @BindView(R.id.btn_save)
    Button btn_save;

    String s_WalletName;
    private Boolean exit = false;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_wallet);

        ButterKnife.bind(this);

        sharedPreferences = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s_WalletName = edt_wallet.getText().toString();
                if (s_WalletName.isEmpty()) {
                    CommonUtilities.ShowToastMessage(SetUpWalletActivity.this, getResources().getString(R.string.empty_wallet));
                } else {
                    Intent intent = new Intent(SetUpWalletActivity.this, AddCoinsActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putString(CONSTANTS.walletName,s_WalletName);
//                    intent.putExtras(bundle);
//                    editor.putBoolean(CONSTANTS.wallet,true);
                    editor.putString(CONSTANTS.walletName,s_WalletName);
                    editor.apply();
                    startActivity(intent);
                }
            }
        });

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
