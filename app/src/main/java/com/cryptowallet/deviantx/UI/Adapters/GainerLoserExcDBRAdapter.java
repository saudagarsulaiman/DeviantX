package com.cryptowallet.deviantx.UI.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.UI.Activities.ExchangeCoinInfoActivity;
import com.cryptowallet.deviantx.UI.Models.CoinPairs;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GainerLoserExcDBRAdapter extends RecyclerView.Adapter<GainerLoserExcDBRAdapter.Viewholder> {
    Context context;
    ArrayList<CoinPairs> allCoinPairs;
    boolean isGainer;
    boolean isAll;
    String selectedCoinName;

    public GainerLoserExcDBRAdapter(Context context, ArrayList<CoinPairs> allCoinPairs, String selectedCoinName, boolean isGainer, boolean isAll) {
        this.context = context;
        this.allCoinPairs = allCoinPairs;
        this.isGainer = isGainer;
        this.isAll = isAll;
        this.selectedCoinName = selectedCoinName;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.gainer_losers_lyt, viewGroup, false);
        Viewholder viewholder = new Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(Viewholder viewholder, int i) {
        /*if (i == allCoinPairs.size() - 1*//*9*//*) {
            viewholder.view.setVisibility(View.INVISIBLE);
        }*/

//        if (!allCoinPairs.get(i).getStr_exchangeCoin().equals(allCoinPairs.get(i).getStr_pairCoin())) {

        if (isAll) {
            viewholder.txt_coin_vol.setVisibility(View.VISIBLE);
            viewholder.txt_coin_usd.setVisibility(View.VISIBLE);
            viewholder.txt_coin_price_change.setText(String.format("%.4f", allCoinPairs.get(i).getDbl_previousValue()));

//            viewholder.txt_coin_usd.setText(String.format("%.4f", allCoinPairs.get(i).getDbl_twentyFourChangeUsd()));

            if (allCoinPairs.get(i).getDbl_twentyFourChangePercentage() >= 0) {
                viewholder.txt_coin_per.setBackground(context.getResources().getDrawable(R.color.graph_brdr_green));
                viewholder.txt_coin_per.setText("+" + String.format("%.2f", allCoinPairs.get(i).getDbl_twentyFourChangePercentage()) + "%");
            } else {
                viewholder.txt_coin_per.setBackground(context.getResources().getDrawable(R.color.graph_brdr_red));
                viewholder.txt_coin_per.setText(String.format("%.2f", allCoinPairs.get(i).getDbl_twentyFourChangePercentage()) + "%");
            }

            SpannableString spannableString = new SpannableString(allCoinPairs.get(i).getStr_pairCoin() + "/" + allCoinPairs.get(i).getStr_exchangeCoin());
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View textView) {
                }

                @Override
                public void updateDrawState(final TextPaint textPaint) {
                    textPaint.setColor(context.getResources().getColor(R.color.white));
                    textPaint.setUnderlineText(false);
//                        textPaint.bgColor = context.getResources().getColor(R.color.yellow);
                }
            };
            spannableString.setSpan(new RelativeSizeSpan(0.8f), spannableString.length() - (allCoinPairs.get(i).getStr_exchangeCoin().length() + 1), spannableString.length() - 0, 0); // set size
            spannableString.setSpan(clickableSpan, spannableString.length() - (allCoinPairs.get(i).getStr_exchangeCoin().length() + 1), spannableString.length() - 0, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewholder.txt_coin_name.setText(spannableString, TextView.BufferType.SPANNABLE);
            viewholder.txt_coin_name.setMovementMethod(LinkMovementMethod.getInstance());

            viewholder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
/*
                    Intent intent = new Intent(context, ExchangeDashBoardActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(CONSTANTS.selectedCoin, allCoinPairs.get(i));
                    intent.putExtras(bundle);
                    intent.putExtra(CONSTANTS.seletedTab, 3);
                    context.startActivity(intent);
*/
                    Intent intent = new Intent(context, ExchangeCoinInfoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(CONSTANTS.selectedCoin, allCoinPairs.get(i));
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });

        } else {
            if (isGainer) {
//                if (!allCoinPairs.get(i).getStr_status().equals("DECREASED")) {
                viewholder.txt_coin_per.setBackground(context.getResources().getDrawable(R.color.graph_brdr_green));
                viewholder.txt_coin_per.setText("+" + String.format("%.2f", allCoinPairs.get(i).getDbl_twentyFourChangePercentage()) + "%");
//                }
            } else {
//                if (allCoinPairs.get(i).getStr_status().equals("DECREASED")) {
                viewholder.txt_coin_per.setBackground(context.getResources().getDrawable(R.color.graph_brdr_red));
                viewholder.txt_coin_per.setText(String.format("%.2f", allCoinPairs.get(i).getDbl_twentyFourChangePercentage()) + "%");
//                }
            }
            viewholder.txt_coin_name.setText(allCoinPairs.get(i).getStr_pairCoin() + "/" + allCoinPairs.get(i).getStr_exchangeCoin());
            viewholder.txt_coin_price_change.setText(String.format("%.4f", allCoinPairs.get(i).getDbl_currentValue()) + "/" + String.format("%.2f", allCoinPairs.get(i).getDbl_twentyFourChangeUsd()));

        }
        /*}*//* else {
            viewholder.itemView.setVisibility(View.GONE);
        }*/
    }

    @Override
    public int getItemCount() {
        if (allCoinPairs != null)
            return allCoinPairs.size();
        else
            return 0;
//        return 10;
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        //        @BindView(R.id.)
//                ;
        @BindView(R.id.txt_coin_name)
        TextView txt_coin_name;
        @BindView(R.id.txt_coin_price_change)
        TextView txt_coin_price_change;
        @BindView(R.id.txt_coin_per)
        TextView txt_coin_per;
        @BindView(R.id.txt_coin_usd)
        TextView txt_coin_usd;
        @BindView(R.id.txt_coin_vol)
        TextView txt_coin_vol;
        @BindView(R.id.view)
        View view;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
