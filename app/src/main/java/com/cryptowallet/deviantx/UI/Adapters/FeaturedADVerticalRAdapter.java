package com.cryptowallet.deviantx.UI.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.UI.Activities.CoinInfoADAcivity;
import com.cryptowallet.deviantx.UI.Models.FeaturedAirdrops;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FeaturedADVerticalRAdapter extends RecyclerView.Adapter<FeaturedADVerticalRAdapter.ViewHolder> {
    Context context;
    ArrayList<FeaturedAirdrops> allFeaturedAirdrops;

    public FeaturedADVerticalRAdapter(Context context, ArrayList<FeaturedAirdrops> allFeaturedAirdrops) {
        this.context = context;
        this.allFeaturedAirdrops = allFeaturedAirdrops;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_coins_ad_lyt, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Picasso.with(context).load(allFeaturedAirdrops.get(i).getStr_coinlogo()).into(viewHolder.img_coin);
        viewHolder.txt_coin_name_code.setText(allFeaturedAirdrops.get(i).getStr_coinName() + " (" + allFeaturedAirdrops.get(i).getStr_coinCode() + ")");
        viewHolder.txt_coin_value.setText("Estimated $" + String.format("%.2f", allFeaturedAirdrops.get(i).getdbl_estimated()) + " ref");
        viewHolder.lnr_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CoinInfoADAcivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(CONSTANTS.selectedCoin, allFeaturedAirdrops.get(i));
//                bundle.putString(CONSTANTS.selectedCoin, allFeaturedAirdrops.get(i).getStr_coin_name());
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return allFeaturedAirdrops.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_coin)
        ImageView img_coin;
        @BindView(R.id.txt_coin_name_code)
        TextView txt_coin_name_code;
        @BindView(R.id.txt_coin_value)
        TextView txt_coin_value;
        @BindView(R.id.lnr_item)
        LinearLayout lnr_item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}

