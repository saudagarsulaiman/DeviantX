package com.aequalis.deviantx.UI.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aequalis.deviantx.R;
import com.aequalis.deviantx.UI.Activities.WalletOptionsActivity;
import com.aequalis.deviantx.UI.Models.AccountWallet;
import com.aequalis.deviantx.Utilities.CONSTANTS;
import com.aequalis.deviantx.Utilities.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WalletCoinsRAdapter extends RecyclerView.Adapter<WalletCoinsRAdapter.ViewHolder> {

    Context context;
    ArrayList<AccountWallet> accountWalletlist;
    AccountWallet accountWallet;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public WalletCoinsRAdapter(Context context, ArrayList<AccountWallet> accountWalletlist) {
        this.context = context;
        this.accountWalletlist = accountWalletlist;

        accountWallet = null;
        sharedPreferences = context.getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    @NonNull
    @Override
    public WalletCoinsRAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.wallet_coins_rview_lyt, viewGroup, false);
        WalletCoinsRAdapter.ViewHolder viewHolder = new WalletCoinsRAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(WalletCoinsRAdapter.ViewHolder viewHolder, final int i) {

        Picasso.with(context).load(accountWalletlist.get(i).getAllCoins().getStr_coin_logo()).transform(new CircleTransform()).into(viewHolder.img_coin_logo);

        viewHolder.txt_coin_name.setText(accountWalletlist.get(i).getStr_data_walletName());

        viewHolder.txt_coin_usd_value.setText("$ " + accountWalletlist.get(i).getStr_data_balanceInUSD() + " USD");

        viewHolder.txt_coin_value.setText(accountWalletlist.get(i).getStr_data_balance() + " " + accountWalletlist.get(i).getAllCoins().getStr_coin_code());

        viewHolder.lnr_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                Intent intent = new Intent(context, WalletOptionsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(CONSTANTS.selectedAccountWallet, accountWallet);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return accountWalletlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.lnr_item)
        LinearLayout lnr_item;
        @BindView(R.id.img_coin_logo)
        ImageView img_coin_logo;
        @BindView(R.id.img_go)
        ImageView img_go;
        @BindView(R.id.lnr_go)
        LinearLayout lnr_go;
        @BindView(R.id.txt_coin_name)
        TextView txt_coin_name;
        @BindView(R.id.txt_coin_value)
        TextView txt_coin_value;
        @BindView(R.id.txt_coin_usd_value)
        TextView txt_coin_usd_value;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}

