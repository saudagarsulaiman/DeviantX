package com.aequalis.deviantx.UI.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aequalis.deviantx.R;
import com.aequalis.deviantx.UI.Activities.CoinInformationActivity;
import com.aequalis.deviantx.UI.Activities.ReceiveCoinActivity;
import com.aequalis.deviantx.UI.Activities.SendCoinActivity;
import com.aequalis.deviantx.UI.Models.AccountWallet;
import com.aequalis.deviantx.Utilities.CONSTANTS;
import com.aequalis.deviantx.Utilities.CircleTransform;
import com.aequalis.deviantx.Utilities.CommonUtilities;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyWalletCoinsRAdapter extends RecyclerView.Adapter<MyWalletCoinsRAdapter.ViewHolder> {

    Context context;
    ArrayList<AccountWallet> accountWalletlist;
    //    ArrayList<AccountWallet> selectedAccountWallet;
    AccountWallet accountWallet;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    boolean hideBal;

    public MyWalletCoinsRAdapter(Context context, ArrayList<AccountWallet> accountWalletlist) {
        this.context = context;
        this.accountWalletlist = accountWalletlist;
        accountWallet = null;
//        this.selectedAccountWallet = new ArrayList<>();
        sharedPreferences = context.getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        hideBal = sharedPreferences.getBoolean(CONSTANTS.hideBal, false);
    }

    @NonNull
    @Override
    public MyWalletCoinsRAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mywallet_coins_rview_lyt, viewGroup, false);
        MyWalletCoinsRAdapter.ViewHolder viewHolder = new MyWalletCoinsRAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyWalletCoinsRAdapter.ViewHolder viewHolder, final int i) {
        Picasso.with(context).load(accountWalletlist.get(i).getAllCoins().getStr_coin_logo()).transform(new CircleTransform()).into(viewHolder.img_coin_logo);
        viewHolder.txt_coin_name.setText(accountWalletlist.get(i).getStr_data_walletName());
        if (hideBal) {
            viewHolder.txt_coin_usd_value.setText("$ " + accountWalletlist.get(i).getStr_data_balanceInUSD() + " USD");
            viewHolder.txt_coin_value.setText(accountWalletlist.get(i).getStr_data_balance() + " " + accountWalletlist.get(i).getAllCoins().getStr_coin_code());
        } else {
            viewHolder.txt_coin_usd_value.setText("$ " + "***" + " USD");
            viewHolder.txt_coin_value.setText("***" + " " + accountWalletlist.get(i).getAllCoins().getStr_coin_code());
        }
//        viewHolder.txt_coin_usd_value.setText("$ "+accountWalletlist.get(i).getAllCoins().getStr_coin_usdValue()+" USD");

        viewHolder.lnr_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                editor.putString(CONSTANTS.data_id, accountWalletlist.get(i).getStr_data_id());
//                editor.putString(CONSTANTS.data_address, accountWalletlist.get(i).getStr_data_address());
//                editor.putString(CONSTANTS.data_walletName, accountWalletlist.get(i).getStr_data_walletName());
//                editor.putString(CONSTANTS.data_privatekey, accountWalletlist.get(i).getStr_data_privatekey());
//                editor.putString(CONSTANTS.data_passcode, accountWalletlist.get(i).getStr_data_passcode());
//                editor.putString(CONSTANTS.data_balance,accountWalletlist.get(i).getStr_data_balance());
//                editor.putString(CONSTANTS.data_balUsd,accountWalletlist.get(i).getStr_data_balanceInUSD());
//                editor.putString(CONSTANTS.data_balInr,accountWalletlist.get(i).getStr_data_balanceInINR());
//                editor.putString(CONSTANTS.data_account,accountWalletlist.get(i).getStr_data_account());
//                editor.putString(CONSTANTS.data_coin_id,accountWalletlist.get(i).getAllCoins().getStr_coin_id());
//                editor.putString(CONSTANTS.data_coin_name,accountWalletlist.get(i).getAllCoins().getStr_coin_name());
//                editor.putString(CONSTANTS.data_coin_code,accountWalletlist.get(i).getAllCoins().getStr_coin_code());
//                editor.putString(CONSTANTS.data_coin_logo,accountWalletlist.get(i).getAllCoins().getStr_coin_logo());
//                editor.putString(CONSTANTS.data_coin_usdval, accountWalletlist.get(i).getAllCoins().getStr_coin_usdValue());
//                editor.apply();

                if (CommonUtilities.isConnectionAvailable(context)) {
                    accountWallet = new AccountWallet(
                            accountWalletlist.get(i).getStr_data_id(),
                            accountWalletlist.get(i).getStr_data_address(),
                            accountWalletlist.get(i).getStr_data_walletName(),
                            accountWalletlist.get(i).getStr_data_privatekey(),
                            accountWalletlist.get(i).getStr_data_passcode(),
                            accountWalletlist.get(i).getStr_data_balance(),
                            accountWalletlist.get(i).getStr_data_balanceInUSD(),
                            accountWalletlist.get(i).getStr_data_balanceInINR(),
                            accountWalletlist.get(i).getStr_data_account(),
                            accountWalletlist.get(i).getAllCoins());

                    customDialog(accountWallet);

                } else {
                    CommonUtilities.ShowToastMessage(context, context.getResources().getString(R.string.internetconnection));
                }
//                CommonUtilities.ShowToastMessage(context,"selected");
            }
        });

    }

    private void customDialog(final AccountWallet accountWallet) {

        //                Creating A Custom Dialog Using DialogPlus
        com.orhanobut.dialogplus.ViewHolder viewHolder = new com.orhanobut.dialogplus.ViewHolder(R.layout.dialog_coins_transactions);
        final DialogPlus dialog = DialogPlus.newDialog(context)
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
        LinearLayout lnr_information = view.findViewById(R.id.lnr_information);
        LinearLayout lnr_send = view.findViewById(R.id.lnr_send);
        LinearLayout lnr_receive = view.findViewById(R.id.lnr_receive);
        LinearLayout lnr_delete = view.findViewById(R.id.lnr_delete);

        TextView txt_coin_value = view.findViewById(R.id.txt_coin_value);
        TextView txt_wallet_name = view.findViewById(R.id.txt_wallet_name);
        TextView txt_usd_value = view.findViewById(R.id.txt_usd_value);
//        TextView txt_percentage = view.findViewById(R.id.txt_percentage);
        TextView txt_h_per = view.findViewById(R.id.txt_h_per);
        TextView txt_d_per = view.findViewById(R.id.txt_d_per);
        TextView txt_m_per = view.findViewById(R.id.txt_m_per);
        TextView txt_rank = view.findViewById(R.id.txt_rank);
        TextView txt_markcap_usd = view.findViewById(R.id.txt_markcap_usd);
        TextView txt_vol_usd = view.findViewById(R.id.txt_vol_usd);


        ImageView img_coin_logo = view.findViewById(R.id.img_coin_logo);

        Picasso.with(context).load(accountWallet.getAllCoins().getStr_coin_logo()).into(img_coin_logo);
        txt_wallet_name.setText(accountWallet.getStr_data_walletName());
        if (hideBal) {
            txt_coin_value.setText(accountWallet.getStr_data_balance() + " " + accountWallet.getAllCoins().getStr_coin_code());
            txt_usd_value.setText(accountWallet.getStr_data_balanceInUSD() + " USD");
        } else {
            txt_coin_value.setText("***"+ " " + accountWallet.getAllCoins().getStr_coin_code());
            txt_usd_value.setText("***"+ " USD");
        }


        txt_rank.setText(""+accountWallet.getAllCoins().getInt_coin_rank());
        txt_markcap_usd.setText(""+String.format("%.4f", (accountWallet.getAllCoins().getDbl_coin_marketCap())));
        txt_vol_usd.setText(""+String.format("%.5f", (accountWallet.getAllCoins().getDbl_coin_volume())));
        txt_h_per.setText(""+String.format("%.2f", (accountWallet.getAllCoins().getDbl_coin_24h())));
        txt_d_per.setText(""+String.format("%.2f", (accountWallet.getAllCoins().getDbl_coin_7d())));
        txt_m_per.setText(""+String.format("%.2f", (accountWallet.getAllCoins().getDbl_coin_1m())));

        lnr_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent= new Intent(context,CoinInformationActivity.class);
//                context.startActivity(intent);
                dialog.dismiss();
            }
        });

        lnr_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SendCoinActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(CONSTANTS.selectedAccountWallet, accountWallet);
                intent.putExtras(bundle);
                context.startActivity(intent);
                dialog.dismiss();
            }
        });

        lnr_receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ReceiveCoinActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(CONSTANTS.selectedAccountWallet, accountWallet);
                intent.putExtras(bundle);
                context.startActivity(intent);
                dialog.dismiss();
            }
        });

        lnr_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

//                Displaying DialogPlus
        dialog.show();


    }

    @Override
    public int getItemCount() {
//        return 10;
        return accountWalletlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.lnr_item)
        LinearLayout lnr_item;
        @BindView(R.id.img_coin_logo)
        ImageView img_coin_logo;
        @BindView(R.id.txt_coin_name)
        TextView txt_coin_name;
        @BindView(R.id.txt_coin_value)
        TextView txt_coin_value;
        @BindView(R.id.txt_coin_usd_value)
        TextView txt_coin_usd_value;
        @BindView(R.id.txt_percentage)
        TextView txt_percentage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

