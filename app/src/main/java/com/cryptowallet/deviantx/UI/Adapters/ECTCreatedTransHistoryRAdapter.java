package com.cryptowallet.deviantx.UI.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.UI.Activities.ECTTransactionDetailsActivity;
import com.cryptowallet.deviantx.UI.Models.ECTCreatedTransactionData;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ECTCreatedTransHistoryRAdapter extends RecyclerView.Adapter<ECTCreatedTransHistoryRAdapter.ViewHolder> {

    Context context;
    ArrayList<ECTCreatedTransactionData> ordersList;

    public ECTCreatedTransHistoryRAdapter(Context context, ArrayList<ECTCreatedTransactionData> ordersList) {
        this.context = context;
        this.ordersList = ordersList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.lyt_ect_trans_history, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.txt_coin_from.setText(ordersList.get(i).getStr_currencyFrom().toUpperCase());
        viewHolder.txt_coin_to.setText(ordersList.get(i).getStr_currencyTo().toUpperCase());
        viewHolder.txt_date.setText(CommonUtilities.covertMsToTime(ordersList.get(i).getLong_date()));
        viewHolder.txt_rec_amt.setText(ordersList.get(i).getStr_amountExpectedTo());
        viewHolder.txt_rec_amt_code.setText(ordersList.get(i).getStr_currencyTo().toUpperCase());
        viewHolder.txt_sent_amt.setText(ordersList.get(i).getStr_amountExpectedFrom());
        viewHolder.txt_sent_amt_code.setText(ordersList.get(i).getStr_currencyFrom().toUpperCase());
        viewHolder.txt_fee_amt_code.setText(ordersList.get(i).getStr_txnFee() + " " + ordersList.get(i).getStr_currencyFrom().toUpperCase());

        viewHolder.lnr_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ECTTransactionDetailsActivity.class);
                intent.putExtra(CONSTANTS.ectTransID, Integer.parseInt(ordersList.get(i).getStr_id()));
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.lnr_item)
        LinearLayout lnr_item;
        @BindView(R.id.txt_coin_from)
        TextView txt_coin_from;
        @BindView(R.id.txt_coin_to)
        TextView txt_coin_to;
        @BindView(R.id.txt_date)
        TextView txt_date;
        @BindView(R.id.txt_rec_amt)
        TextView txt_rec_amt;
        @BindView(R.id.txt_rec_amt_code)
        TextView txt_rec_amt_code;
        @BindView(R.id.txt_sent_amt)
        TextView txt_sent_amt;
        @BindView(R.id.txt_sent_amt_code)
        TextView txt_sent_amt_code;
        @BindView(R.id.txt_fee_amt_code)
        TextView txt_fee_amt_code;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
