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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FeaturedCoinsExcDBRAdapter extends RecyclerView.Adapter<FeaturedCoinsExcDBRAdapter.ViewHolder> {

    Context context;
    ArrayList<String> featuredCoinsList;

    public FeaturedCoinsExcDBRAdapter(Context context, ArrayList<String> featuredCoinsList) {
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

    }

    @Override
    public int getItemCount() {
//        return featuredCoinsList.size();
        return 10;
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
