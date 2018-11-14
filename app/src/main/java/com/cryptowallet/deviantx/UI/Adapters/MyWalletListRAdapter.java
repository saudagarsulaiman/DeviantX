package com.cryptowallet.deviantx.UI.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.UI.Activities.WalletListActivity;
import com.cryptowallet.deviantx.UI.Models.AllCoins;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyWalletListRAdapter extends RecyclerView.Adapter<MyWalletListRAdapter.ViewHolder> {

    Context context;

    public MyWalletListRAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_walletlist_lyt, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

//        viewHolder.txt_wallet_name.setText();
//        viewHolder.txt_wallet_bal.setText("~$ "+);
//        viewHolder.lnr_my_wallet.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.lnr_my_wallet)
        LinearLayout lnr_my_wallet;
        @BindView(R.id.txt_wallet_name)
        TextView txt_wallet_name;
        @BindView(R.id.txt_wallet_bal)
        TextView txt_wallet_bal;
//        @BindView(R.id.txt_wallet_coin)
//        TextView txt_wallet_coin;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
