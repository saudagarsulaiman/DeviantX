package com.cryptowallet.deviantx.UI.Adapters;

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

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.UI.Activities.WalletOptionsActivity;
import com.cryptowallet.deviantx.UI.Models.AccountWallet;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cryptowallet.deviantx.Utilities.MyApplication.myApplication;

public class WalletCoinsRAdapter extends RecyclerView.Adapter<WalletCoinsRAdapter.ViewHolder> {

    Context context;
    ArrayList<AccountWallet> accountWalletlist;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    boolean hideBal;

    public WalletCoinsRAdapter(Context context, ArrayList<AccountWallet> accountWalletlist) {
        this.context = context;
        this.accountWalletlist = accountWalletlist;
        this.hideBal = myApplication.getHideBalance();

    }

    public void setIsHideBalance(Boolean isHideBalance){
        this.hideBal=isHideBalance;
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

        Picasso.with(context).load(accountWalletlist.get(i)/*.getAllCoins()*/.getStr_coin_logo()).transform(new CircleTransform()).into(viewHolder.img_coin_logo);

        viewHolder.txt_coin_name.setText(accountWalletlist.get(i).getStr_data_walletName());

        if (!hideBal) {
            viewHolder.txt_coin_usd_value.setText("$ " + String.format("%.2f",accountWalletlist.get(i).getStr_data_balanceInUSD() )+ " USD");
            viewHolder.txt_coin_value.setText(String.format("%.2f",accountWalletlist.get(i).getStr_data_balance()) + " " + accountWalletlist.get(i)/*.getAllCoins()*/.getStr_coin_code());
        } else {
            viewHolder.txt_coin_usd_value.setText("$ " + "***" + " USD");
            viewHolder.txt_coin_value.setText("***" + " " + accountWalletlist.get(i)/*.getAllCoins()*/.getStr_coin_code());
        }

        viewHolder.lnr_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, WalletOptionsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(CONSTANTS.selectedAccountWallet, accountWalletlist.get(i));
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

