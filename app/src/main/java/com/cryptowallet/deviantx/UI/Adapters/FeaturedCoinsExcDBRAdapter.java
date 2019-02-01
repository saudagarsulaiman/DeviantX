package com.cryptowallet.deviantx.UI.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.UI.Models.AllCoins;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FeaturedCoinsExcDBRAdapter extends RecyclerView.Adapter<FeaturedCoinsExcDBRAdapter.ViewHolder> {

    Context context;
    ArrayList<AllCoins> featuredCoinsList;

    public FeaturedCoinsExcDBRAdapter(Context context, ArrayList<AllCoins> featuredCoinsList) {
        this.context = context;
        this.featuredCoinsList = featuredCoinsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.coins_excdb_featured_lyt, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        if (featuredCoinsList.get(i).getDbl_coin_24h() > 0) {
            viewHolder.txt_coin_per.setText("+" + String.format("%.4f", featuredCoinsList.get(i).getDbl_coin_24h()) + "%");
            viewHolder.txt_coin_per.setTextColor(context.getResources().getColor(R.color.green_txt));
        } else {
            viewHolder.txt_coin_per.setText(String.format("%.4f", featuredCoinsList.get(i).getDbl_coin_24h()) + "%");
            viewHolder.txt_coin_per.setTextColor(context.getResources().getColor(R.color.google_red));
        }
        Picasso.with(context).load(featuredCoinsList.get(i).getStr_coin_logo()).into(viewHolder.img_coin);
        viewHolder.txt_coin_name_code.setText(featuredCoinsList.get(i).getStr_coin_code() + "/USDT");
        viewHolder.txt_coin_value.setText("$" + String.format("%.4f", featuredCoinsList.get(i).getDbl_coin_usdValue()));
    }

    @Override
    public int getItemCount() {
        return featuredCoinsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_coin_per)
        TextView txt_coin_per;
        @BindView(R.id.img_coin)
        ImageView img_coin;
        @BindView(R.id.txt_coin_name_code)
        TextView txt_coin_name_code;
        @BindView(R.id.txt_coin_value)
        TextView txt_coin_value;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
