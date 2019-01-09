package com.cryptowallet.deviantx.UI.Adapters;

import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GainerLoserExcDBRAdapter extends RecyclerView.Adapter<GainerLoserExcDBRAdapter.Viewholder> {
    Context context;
    ArrayList<String> gainersLoserList;
    boolean isGainer;
    boolean isAll;
    String selectedCoinName;

    public GainerLoserExcDBRAdapter(Context context, ArrayList<String> gainersLoserList, String selectedCoinName, boolean isGainer, boolean isAll) {
        this.context = context;
        this.gainersLoserList = gainersLoserList;
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
        if (i == /*gainersLoserList.size()-1*/9) {
            viewholder.view.setVisibility(View.INVISIBLE);
        }

        if (isAll) {
            viewholder.txt_coin_vol.setVisibility(View.VISIBLE);
            viewholder.txt_coin_usd.setVisibility(View.VISIBLE);
            viewholder.txt_coin_price_change.setText("0.13566");
            viewholder.txt_coin_usd.setText("$0.44");
            if (i % 2 == 0) {
                viewholder.txt_coin_per.setBackground(context.getResources().getDrawable(R.color.graph_brdr_green));
                viewholder.txt_coin_per.setText("+42.90%");
            } else {
                viewholder.txt_coin_per.setBackground(context.getResources().getDrawable(R.color.graph_brdr_red));
                viewholder.txt_coin_per.setText("-18.35%");
            }

            SpannableString spannableString = new SpannableString("BTC/" + selectedCoinName);
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
            spannableString.setSpan(new RelativeSizeSpan(0.8f), spannableString.length() - (selectedCoinName.length() + 1), spannableString.length() - 0, 0); // set size
            spannableString.setSpan(clickableSpan, spannableString.length() - (selectedCoinName.length() + 1), spannableString.length() - 0, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewholder.txt_coin_name.setText(spannableString, TextView.BufferType.SPANNABLE);
            viewholder.txt_coin_name.setMovementMethod(LinkMovementMethod.getInstance());

            viewholder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ExchangeCoinInfoActivity.class);
                    context.startActivity(intent);
                }
            });

        } else {
            if (isGainer) {
                viewholder.txt_coin_per.setBackground(context.getResources().getDrawable(R.color.graph_brdr_green));
                viewholder.txt_coin_per.setText("+42.90%");
            } else {
                viewholder.txt_coin_per.setBackground(context.getResources().getDrawable(R.color.graph_brdr_red));
                viewholder.txt_coin_per.setText("-18.35%");
            }


        }

    }

    @Override
    public int getItemCount() {
//        return gainersLoserList.size();
        return 10;
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
