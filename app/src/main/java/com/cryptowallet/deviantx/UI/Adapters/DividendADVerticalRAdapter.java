package com.cryptowallet.deviantx.UI.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.UI.Activities.WithdrawADClaimActivity;
import com.cryptowallet.deviantx.UI.Models.DividendAirdrops;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DividendADVerticalRAdapter extends RecyclerView.Adapter<DividendADVerticalRAdapter.ViewHolder> {
    Context context;
    ArrayList<DividendAirdrops> allDividendCoins;
    boolean isFull = false;
    Double dbl_data_ad_balance;
    String str_ad_status;

    public DividendADVerticalRAdapter(Context context, ArrayList<DividendAirdrops> allDividendCoins, boolean isFull, Double dbl_data_ad_balance, String str_ad_status) {
        this.context = context;
        this.allDividendCoins = allDividendCoins;
        this.isFull = isFull;
        this.dbl_data_ad_balance = dbl_data_ad_balance;
        this.str_ad_status = str_ad_status;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.coins_div_ad_lyt, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Picasso.with(context).load(allDividendCoins.get(i).getStr_coinlogo()).into(viewHolder.img_coin);
        viewHolder.txt_coin_name_code.setText(allDividendCoins.get(i).getStr_coinName() + " (" + allDividendCoins.get(i).getStr_coinCode() + ")");
        viewHolder.txt_coin_value.setText("Estimated $" + String.format("%.2f", allDividendCoins.get(i).getDbl_airdropAmountInUSD()) + " ref");

        viewHolder.btn_claim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dbl_data_ad_balance < 500) {
//                if (!str_ad_status.equals("true")) {
//                    CommonUtilities.ShowToastMessage(context, context.getResources().getString(R.string.no_en_bal_claim));
                    CommonUtilities.ShowToastMessage(context, context.getResources().getString(R.string.maintain_bal_500));
                } else {
                    Intent intent = new Intent(context, WithdrawADClaimActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(CONSTANTS.claimCoin, allDividendCoins.get(i));
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            }
        });


       /* viewHolder.lnr_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CoinInfoADAcivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(CONSTANTS.selectedCoin, allDividendCoins.get(i));
//                bundle.putString(CONSTANTS.selectedCoin, allDividendCoins.get(i).getStr_coin_name());
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });*/
    }

    @Override
    public int getItemCount() {
/*
        if (isFull) {
            if (allDividendCoins.size() >)
        } else {

        }
*/
        return allDividendCoins.size();
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
        @BindView(R.id.btn_claim)
        Button btn_claim;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}

