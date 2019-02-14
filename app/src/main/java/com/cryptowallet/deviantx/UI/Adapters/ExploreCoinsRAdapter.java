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
import com.cryptowallet.deviantx.UI.Activities.CoinInformationActivity;
import com.cryptowallet.deviantx.UI.Models.AllCoins;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExploreCoinsRAdapter extends RecyclerView.Adapter<ExploreCoinsRAdapter.ViewHolder> {

    Context context;
    ArrayList<AllCoins> allCoinsList;

    public ExploreCoinsRAdapter(Context context, ArrayList<AllCoins> allCoinsList) {
        this.context = context;
        this.allCoinsList = allCoinsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.coins_list_lyt, viewGroup, false);
        ExploreCoinsRAdapter.ViewHolder viewHolder = new ExploreCoinsRAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        Picasso.with(context).load(allCoinsList.get(i).getStr_coin_logo()).into(viewHolder.img_coin_logo);
        viewHolder.txt_coin_name.setText(allCoinsList.get(i).getStr_coin_name());
        viewHolder.txt_coin_value.setText(allCoinsList.get(i).getStr_coin_code());
        viewHolder.txt_coin_usd_value.setText("$ " + String.format("%.4f", allCoinsList.get(i).getDbl_coin_usdValue()) + " USD");

        DecimalFormat rank = new DecimalFormat("0.00");
        if (allCoinsList.get(i).getDbl_coin_24h() < 0) {
            viewHolder.txt_percentage.setText("" + rank.format(allCoinsList.get(i).getDbl_coin_24h()) + "%");
            viewHolder.txt_percentage.setTextColor(context.getResources().getColor(R.color.google_red));
        } else {
            viewHolder.txt_percentage.setText("+" + rank.format(allCoinsList.get(i).getDbl_coin_24h()) + "%");
            viewHolder.txt_percentage.setTextColor(context.getResources().getColor(R.color.green));
        }

        viewHolder.lnr_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllCoins selectedcoin = allCoinsList.get(i);
//                AllCoinsDB selectedCoin = new AllCoinsDB(
//                        allCoinsList.get(i).getInt_coin_id(),
//                        allCoinsList.get(i).getStr_coin_name(),
//                        allCoinsList.get(i).getStr_coin_code(),
//                        allCoinsList.get(i).getStr_coin_logo(),
//                        allCoinsList.get(i).getDbl_coin_usdValue()
//                );

                Intent intent = new Intent(context, CoinInformationActivity.class);
                Bundle bundle = new Bundle();
//                bundle.putParcelable(CONSTANTS.selectedWalletCoin);
                bundle.putParcelable(CONSTANTS.selectedCoin, selectedcoin);
                bundle.putBoolean(CONSTANTS.isExploreCoins, true);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return allCoinsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.lnr_item)
        LinearLayout lnr_item;
        @BindView(R.id.img_coin_logo)
        ImageView img_coin_logo;
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
