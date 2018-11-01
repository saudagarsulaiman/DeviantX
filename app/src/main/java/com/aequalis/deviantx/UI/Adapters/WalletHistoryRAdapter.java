package com.aequalis.deviantx.UI.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aequalis.deviantx.R;
import com.aequalis.deviantx.UI.Models.Transaction;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;

public class WalletHistoryRAdapter extends RecyclerView.Adapter<WalletHistoryRAdapter.ViewHolder> {
    Context context;
    ArrayList<Transaction> transactionList;
    Transaction transaction;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public WalletHistoryRAdapter(Context context, ArrayList<Transaction> transactions) {
        this.context = context;
        this.transactionList = transactions;
        transaction = null;
        sharedPreferences = context.getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.wallet_history_lyt, viewGroup, false);
        WalletHistoryRAdapter.ViewHolder viewHolder = new WalletHistoryRAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Picasso.with(context).load(R.drawable.dot_inactive).into(viewHolder.img_send_type);
//        viewHolder.txt_time.setText();
        viewHolder.txt_trans_address.setText("To "+transactionList.get(i).getStr_data_toAddress());
//        viewHolder.txt_time.setText();
        viewHolder.txt_trans_amount.setText(transactionList.get(i).getdbl_data_coinValue()+" "+transactionList.get(i).getAllCoins().getStr_coin_code());
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_send_type)
        ImageView img_send_type;
        @BindView(R.id.txt_time)
        TextView txt_time;
        @BindView(R.id.txt_trans_type)
        TextView txt_trans_type;
        @BindView(R.id.txt_trans_address)
        TextView txt_trans_address;
        @BindView(R.id.txt_trans_amount)
        TextView txt_trans_amount;

        public ViewHolder(View itemView) {
            super(itemView);

        }
    }
}
