package com.cryptowallet.deviantx.UI.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.UI.Models.ExcOrders;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MarketTradesRAdapter extends RecyclerView.Adapter<MarketTradesRAdapter.ViewHolder> {

    Context context;
    ArrayList<ExcOrders> bidList, askList, allList;
    boolean isShort;
    SharedPreferences sharedPreferences;

    public MarketTradesRAdapter(Context context, ArrayList<ExcOrders> bidList, ArrayList<ExcOrders> askList, ArrayList<ExcOrders> allList, boolean isShort) {
        this.context = context;
        this.bidList = bidList;
        this.askList = askList;
        this.allList = allList;
        this.isShort = isShort;
        sharedPreferences = context.getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.market_trades_lyt, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        String myEmail = sharedPreferences.getString(CONSTANTS.email, null);
        if (allList.get(i).getStr_orderType().equals("buy")) {
            if (allList.get(i).getStr_user().trim().equals(myEmail)) {
                viewHolder.txt_price.setTextColor(context.getResources().getColor(R.color.sky_blue1));
                viewHolder.txt_amount.setTextColor(context.getResources().getColor(R.color.sky_blue1));
            } else {
                viewHolder.txt_price.setTextColor(context.getResources().getColor(R.color.graph_wallet_brdr_green));
                viewHolder.txt_amount.setTextColor(context.getResources().getColor(R.color.white));
            }
            long ms = Long.parseLong(allList.get(i).getStr_createdAt());
            viewHolder.txt_time.setText(CommonUtilities.covertMsToTime(ms));
            viewHolder.txt_price.setText(String.format("%.6f", allList.get(i).getDbl_price()));
/*
            viewHolder.txt_price.setTextColor(context.getResources().getColor(R.color.graph_wallet_brdr_green));
*/
            viewHolder.txt_amount.setText(String.format("%.6f", allList.get(i).getDbl_amount()));
        } else {
            if (allList.get(i).getStr_user().trim().equals(myEmail)) {
                viewHolder.txt_price.setTextColor(context.getResources().getColor(R.color.sky_blue1));
                viewHolder.txt_amount.setTextColor(context.getResources().getColor(R.color.sky_blue1));
            } else {
                viewHolder.txt_price.setTextColor(context.getResources().getColor(R.color.google_red));
                viewHolder.txt_amount.setTextColor(context.getResources().getColor(R.color.white));
            }
            long ms = Long.parseLong(allList.get(i).getStr_createdAt());
            viewHolder.txt_time.setText(CommonUtilities.covertMsToTime(ms));
            viewHolder.txt_price.setText(String.format("%.6f", allList.get(i).getDbl_price()));
/*
            viewHolder.txt_price.setTextColor(context.getResources().getColor(R.color.google_red));
*/
            viewHolder.txt_amount.setText(String.format("%.6f", allList.get(i).getDbl_amount()));
        }

    }

    @Override
    public int getItemCount() {
        if (isShort) {
            return getCount(allList.size());
        } else {
            return allList.size();
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

    private int getCount(int size) {
        int count = 0;
        switch (size) {
            case 1:
                count = 1;
                break;
            case 2:
                count = 2;
                break;
            case 3:
                count = 3;
                break;
            case 4:
                count = 4;
                break;
            case 5:
                count = 5;
                break;
            default:
                count = 5;
        }
        return count;
    }

}
