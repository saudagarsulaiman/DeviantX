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

public class MarketTradesRAdapter extends RecyclerView.Adapter<MarketTradesRAdapter.ViewHolder> {

    Context context;
    ArrayList<String> tradesList;
    boolean isShort;

    public MarketTradesRAdapter(Context context, ArrayList<String> tradesList, boolean isShort) {
        this.context = context;
        this.tradesList = tradesList;
        this.isShort = isShort;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.market_trades_lyt, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        if (i % 2 == 0) {
            viewHolder.txt_time.setText("23:16:56");
            viewHolder.txt_price.setText("0.0005112");
            viewHolder.txt_price.setTextColor(context.getResources().getColor(R.color.red));
            viewHolder.txt_amount.setText("2.30");
        } else {
            viewHolder.txt_time.setText("23:18:50");
            viewHolder.txt_price.setText("0.0062546");
            viewHolder.txt_price.setTextColor(context.getResources().getColor(R.color.green_txt));
            viewHolder.txt_amount.setText("315.05");
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

        @BindView(R.id.txt_time)
        TextView txt_time;
        @BindView(R.id.txt_price)
        TextView txt_price;
        @BindView(R.id.txt_amount)
        TextView txt_amount;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
