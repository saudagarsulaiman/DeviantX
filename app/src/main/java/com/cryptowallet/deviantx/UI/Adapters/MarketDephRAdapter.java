package com.cryptowallet.deviantx.UI.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.UI.Models.ExcOrders;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MarketDephRAdapter extends RecyclerView.Adapter<MarketDephRAdapter.ViewHolder> {

    Context context;
    ArrayList<ExcOrders> bidList, askList;
    boolean isShort, isBid;
/*
    String title_pair;
*/

    public MarketDephRAdapter(Context context, /*String title_pair,*/ boolean isBid, ArrayList<ExcOrders> bidList, ArrayList<ExcOrders> askList, boolean isShort) {
        this.context = context;
        this.bidList = bidList;
        this.askList = askList;
        this.isShort = isShort;
        this.isBid = isBid;
/*
        this.title_pair = title_pair;
*/
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
        try {
            viewHolder.seekbar_per.setEnabled(false);
            viewHolder.seekbar_per.setVisibility(View.GONE);

            if (isBid) {
                viewHolder.seekbar_per.setProgress(getPer(bidList.get(i).getDbl_total(), bidList.get(i).getDbl_executedVolume()));
                viewHolder.txt_price.setText(String.format("%.4f", bidList.get(i).getDbl_price()));
                viewHolder.txt_price.setTextColor(context.getResources().getColor(R.color.graph_wallet_brdr_green));
                viewHolder.txt_amount.setText(String.format("%.4f", bidList.get(i).getDbl_amount()));

            } else {
                viewHolder.seekbar_per.setProgress(getPer(askList.get(i).getDbl_total(), askList.get(i).getDbl_executedVolume()));
                viewHolder.txt_price.setText(String.format("%.4f", askList.get(i).getDbl_price()));
                viewHolder.txt_price.setTextColor(context.getResources().getColor(R.color.blue));
                viewHolder.txt_amount.setText(String.format("%.4f", askList.get(i).getDbl_amount()));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private int getPer(double dbl_total, double dbl_executedVolume) {
        int result = 0;

        result = (int) ((dbl_total / dbl_executedVolume) * 100);

        return result;
    }

    @Override
    public int getItemCount() {
/*
        if (isShort) {
            return 6;
        } else {
            return 16;
        }
*/
        if (isBid) {
            if (isShort) {
                return getCount(bidList.size());
            } else {
                return bidList.size();
            }
//            return bidList.size();
        } else {
            if (isShort) {
                return getCount(askList.size());
            } else {
                return askList.size();
            }
//            return askList.size();
        }
//        return bidList.size();
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_amount)
        TextView txt_amount;
        @BindView(R.id.txt_price)
        TextView txt_price;
        @BindView(R.id.seekbar_per)
        SeekBar seekbar_per;
        @BindView(R.id.view_bg)
        View view_bg;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
