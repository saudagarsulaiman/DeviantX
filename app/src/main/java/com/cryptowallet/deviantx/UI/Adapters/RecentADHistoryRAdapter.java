package com.cryptowallet.deviantx.UI.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.UI.Models.AirdropsHistory;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecentADHistoryRAdapter extends RecyclerView.Adapter<RecentADHistoryRAdapter.ViewHolder> {

    Context context;
    ArrayList<AirdropsHistory> airdropsHistoryList;
    boolean isFull = false;

    public RecentADHistoryRAdapter(Context context, ArrayList<AirdropsHistory> airdropsHistoryList, boolean isFull) {
        this.context = context;
        this.airdropsHistoryList = airdropsHistoryList;
        this.isFull = isFull;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.coins_ad_list_lyt, viewGroup, false);
        RecentADHistoryRAdapter.ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Picasso.with(context).load(airdropsHistoryList.get(i).getStr_coinlogo()).into(viewHolder.img_coin_logo);
        viewHolder.txt_coin_name.setText(airdropsHistoryList.get(i).getStr_coinName() + " (" + airdropsHistoryList.get(i).getStr_coinCode() + ")");
        viewHolder.txt_coin_value.setText(airdropsHistoryList.get(i).getDbl_amount() + " " + airdropsHistoryList.get(i).getStr_coinCode());
        viewHolder.txt_percentage.setText(getTime(airdropsHistoryList.get(i).getStr_txnDate()));

        viewHolder.lnr_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent= new Intent(context,.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);*/
            }
        });
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

    @Override
    public int getItemCount() {
        if (isFull) {
            return airdropsHistoryList.size();
        } else {
            if (airdropsHistoryList.size() == 0)
                return 0;
            else if (airdropsHistoryList.size() > 1)
                return 2;
            else
                return 1;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.lnr_item)
        LinearLayout lnr_item;
        @BindView(R.id.img_coin_logo)
        ImageView img_coin_logo;
        @BindView(R.id.img_rec)
        ImageView img_rec;
        @BindView(R.id.txt_coin_name)
        TextView txt_coin_name;
        @BindView(R.id.txt_coin_value)
        TextView txt_coin_value;
        @BindView(R.id.txt_coin_usd_value)
        TextView txt_coin_usd_value;
        @BindView(R.id.txt_percentage)
        TextView txt_percentage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
