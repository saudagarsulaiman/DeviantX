package com.aequalis.deviantx.UI.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aequalis.deviantx.R;
import com.aequalis.deviantx.UI.Models.AllCoins;
import com.aequalis.deviantx.UI.Interfaces.CoinSelectableListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CoinsListRAdapter extends RecyclerView.Adapter<CoinsListRAdapter.ViewHolder> {

    Context context;
    ArrayList<AllCoins> allCoinsList;
    CoinSelectableListener coinSelectableListener;

    public CoinsListRAdapter(Activity context, ArrayList<AllCoins> allCoinsList, CoinSelectableListener coinSelectableListener) {
        this.context = context;
        this.allCoinsList = allCoinsList;
        this.coinSelectableListener = coinSelectableListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.add_coins_rview_lyt, viewGroup, false);
        CoinsListRAdapter.ViewHolder viewHolder = new CoinsListRAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

        viewHolder.lnr_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allCoinsList.get(i).getSelected()) {
                    allCoinsList.get(i).setSelected(false);
                    Picasso.with(context).load(R.drawable.dot_inactive).into(viewHolder.img_avail);
//                    viewHolder.img_avail.setImageDrawable(context.getResources().getDrawable(R.drawable.dot_inactive));
                    viewHolder.lnr_item.setBackgroundResource(R.drawable.rec_lytblue_c5);
                } else{
                    allCoinsList.get(i).setSelected(true);
                    Picasso.with(context).load(R.drawable.dot_active).into(viewHolder.img_avail);
//                    viewHolder.img_avail.setImageDrawable(context.getResources().getDrawable(R.drawable.dot_active));
                    viewHolder.lnr_item.setBackgroundResource(R.drawable.rec_yellow_lytblue_c5);
                }
                coinSelectableListener.CoinSelected(allCoinsList);
            }
        });

        Picasso.with(context).load(allCoinsList.get(i).getStr_coin_logo()).into(viewHolder.img_coin);
        viewHolder.txt_coin_name.setText(allCoinsList.get(i).getStr_coin_name());
        viewHolder.txt_coin_value.setText("$ " + allCoinsList.get(i).getStr_coin_usdValue() + " USD");
    }

    @Override
    public int getItemCount() {
        return allCoinsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.lnr_item)
        LinearLayout lnr_item;
        @BindView(R.id.img_coin)
        ImageView img_coin;
        @BindView(R.id.txt_coin_name)
        TextView txt_coin_name;
        @BindView(R.id.txt_coin_value)
        TextView txt_coin_value;
        @BindView(R.id.img_avail)
        ImageView img_avail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
