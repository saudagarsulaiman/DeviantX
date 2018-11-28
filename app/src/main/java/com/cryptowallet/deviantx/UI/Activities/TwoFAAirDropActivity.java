package com.cryptowallet.deviantx.UI.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cryptowallet.deviantx.R;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TwoFAAirDropActivity extends AppCompatActivity {

    //    @BindView(R.id.)
//    ;
    @BindView(R.id.toolbar_center_back)
    Toolbar toolbar_center_back;
    @BindView(R.id.edt_google_auth_code)
    EditText edt_google_auth_code;
    @BindView(R.id.btn_submit)
    Button btn_submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_fa_airdrop);

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
                customDialog();
            }
        });

    }

    private void customDialog() {
        //                Creating A Custom Dialog Using DialogPlus
        ViewHolder viewHolder = new ViewHolder(R.layout.dialog_withdraw_confirm);
        final DialogPlus dialog = DialogPlus.newDialog(TwoFAAirDropActivity.this)
                .setContentHolder(viewHolder)
                .setGravity(Gravity.BOTTOM)
                .setCancelable(true)
                .setInAnimation(R.anim.slide_in_bottom)
                .setOutAnimation(R.anim.slide_out_bottom)
                .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .create();

//                Initializing Widgets
        View view = dialog.getHolderView();
        TextView txt_cancel = view.findViewById(R.id.txt_cancel);
        TextView txt_confirm = view.findViewById(R.id.txt_confirm);
        TextView txt_withdraw_amt = view.findViewById(R.id.txt_withdraw_amt);
        TextView txt_withdraw_amt_code = view.findViewById(R.id.txt_withdraw_amt_code);
        TextView txt_fee_amt = view.findViewById(R.id.txt_fee_amt);
        TextView txt_fee_amt_code = view.findViewById(R.id.txt_fee_amt_code);
        TextView txt_address = view.findViewById(R.id.txt_address);
        TextView txt_privacy_policy = view.findViewById(R.id.txt_privacy_policy);

//        txt_withdraw_amt.setText(String.format("%.6f",));
//        txt_withdraw_amt_code.setText();
//        txt_fee_amt.setText(String.format("%.4f",));
//        txt_fee_amt_code.setText();
//        txt_address.setText();
/*
        txt_privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
*/

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        txt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TwoFAAirDropActivity.this, DashBoardActivity.class);
//                startActivityForResult(intent, 200);
                startActivity(intent);
                dialog.dismiss();
            }
        });


        dialog.show();
    }
}
