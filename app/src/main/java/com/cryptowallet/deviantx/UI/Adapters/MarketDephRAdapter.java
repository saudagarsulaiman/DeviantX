package com.cryptowallet.deviantx.UI.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cryptowallet.deviantx.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MarketDephRAdapter extends RecyclerView.Adapter<MarketDephRAdapter.ViewHolder> {

    Context context;
    ArrayList<String> tradesList;
    boolean isShort, isBid;

    public MarketDephRAdapter(Context context, boolean isBid, ArrayList<String> tradesList, boolean isShort) {
        this.context = context;
        this.tradesList = tradesList;
        this.isShort = isShort;
        this.isBid = isBid;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        if (isBid) {
            View view = LayoutInflater.from(context).inflate(R.layout.market_deph_bid_lyt, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.market_deph_ask_lyt, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        if (isBid) {
            viewHolder.txt_price.setText("0.0005");
            viewHolder.txt_amount.setTextColor(context.getResources().getColor(R.color.green_txt));
            viewHolder.txt_amount.setText("2.30");
        } else {
            viewHolder.txt_price.setText("0.4321");
            viewHolder.txt_amount.setTextColor(context.getResources().getColor(R.color.blue));
            viewHolder.txt_amount.setText("414.44");
        }

    }

    @Override
    public int getItemCount() {
        if (isShort) {
            return 6;
        } else {
            return 16;
//            return tradesList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_amount)
        TextView txt_amount;
        @BindView(R.id.txt_price)
        TextView txt_price;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
