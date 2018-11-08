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
import com.aequalis.deviantx.Utilities.CONSTANTS;
import com.aequalis.deviantx.Utilities.CommonUtilities;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aequalis.deviantx.Utilities.MyApplication.myApplication;

public class WalletHistoryRAdapter extends RecyclerView.Adapter<WalletHistoryRAdapter.ViewHolder> {
    Context context;
    ArrayList<Transaction> transactionList;
    Transaction transaction;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    boolean hideBal;

    public WalletHistoryRAdapter(Context context, ArrayList<Transaction> transactions) {
        this.context = context;
        this.transactionList = transactions;
        transaction = null;
        this.hideBal = myApplication.getHideBalance();

    }

    public void setIsHideBalance(Boolean isHideBalance) {
        this.hideBal = isHideBalance;
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

//        Picasso.with(context).load(R.drawable.dot_inactive).into(viewHolder.img_send_type);
//        viewHolder.txt_time.setText();
        viewHolder.txt_time.setText(getTime(transactionList.get(i).getStr_data_txnDate()));

        if (!hideBal) {
            if (transactionList.get(i).getStr_data_toAddress().length() < 15) {
                viewHolder.txt_trans_address.setText("To " + transactionList.get(i).getStr_data_toAddress());
            } else {
                String address = transactionList.get(i).getStr_data_toAddress();
                String dummy = "{...}";
                String first_half = String.format("%.7s", address);
                String second_half = address.substring(address.length() - 7);

                viewHolder.txt_trans_address.setText("To " + first_half + dummy + second_half);

            }
            viewHolder.txt_trans_amount.setText(transactionList.get(i).getdbl_data_coinValue() + " " + transactionList.get(i).getAllCoins().getStr_coin_code());
        } else {
            viewHolder.txt_trans_address.setText("To " + "***");
            viewHolder.txt_trans_amount.setText("***" + " " + transactionList.get(i).getAllCoins().getStr_coin_code());
        }
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

            ButterKnife.bind(this, itemView);

        }
    }

    private String getTime(String started) {

        try {
            return CommonUtilities.convertToHumanReadable(Long.parseLong(started));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return "";
    }
}
