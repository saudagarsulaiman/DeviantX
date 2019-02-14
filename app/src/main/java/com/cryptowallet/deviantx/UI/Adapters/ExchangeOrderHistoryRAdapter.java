package com.cryptowallet.deviantx.UI.Adapters;

import android.content.Context;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cryptowallet.deviantx.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExchangeOrderHistoryRAdapter extends RecyclerView.Adapter<ExchangeOrderHistoryRAdapter.ViewHolder> {
    Context context;
    boolean isShort;

    public ExchangeOrderHistoryRAdapter(Context context, boolean isShort) {
        this.context = context;
        this.isShort = isShort;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.exc_order_history_lyt, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        if (isShort) {
            SpannableString spannableString = new SpannableString("LTC/USDT  OPEN ORDER ");
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View textView) {
                }

                @Override
                public void updateDrawState(final TextPaint textPaint) {
                    textPaint.setColor(context.getResources().getColor(R.color.black));
                    textPaint.setUnderlineText(false);
//                    BackgroundColorSpan backgroundColorSpan= new BackgroundColorSpan(context.getResources().getColor(R.color.yellow));
                    textPaint.bgColor = context.getResources().getColor(R.color.yellow);
                }
            };
            spannableString.setSpan(new RelativeSizeSpan(0.6f), spannableString.length() - 18, spannableString.length() - 0, 0); // set size
            spannableString.setSpan(clickableSpan, spannableString.length() - 12, spannableString.length() - 0, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewHolder.txt_coin_name.setText(spannableString, TextView.BufferType.SPANNABLE);
            viewHolder.txt_coin_name.setHighlightColor(context.getResources().getColor(R.color.yellow));
            viewHolder.txt_coin_name.setMovementMethod(LinkMovementMethod.getInstance());

            viewHolder.lnr_close.setVisibility(View.VISIBLE);
        } else {
            if (i % 2 == 0) {
                SpannableString spannableString = new SpannableString("LTC/USDT  OPEN ORDER ");
                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View textView) {
                    }

                    @Override
                    public void updateDrawState(final TextPaint textPaint) {
                        textPaint.setColor(context.getResources().getColor(R.color.black));
                        textPaint.setUnderlineText(false);
//                    BackgroundColorSpan backgroundColorSpan= new BackgroundColorSpan(context.getResources().getColor(R.color.yellow));
                        textPaint.bgColor = context.getResources().getColor(R.color.yellow);
                    }
                };
                spannableString.setSpan(new RelativeSizeSpan(0.6f), spannableString.length() - 18, spannableString.length() - 0, 0); // set size
                spannableString.setSpan(clickableSpan, spannableString.length() - 12, spannableString.length() - 0, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                viewHolder.txt_coin_name.setText(spannableString, TextView.BufferType.SPANNABLE);
                viewHolder.txt_coin_name.setHighlightColor(context.getResources().getColor(R.color.yellow));
                viewHolder.txt_coin_name.setMovementMethod(LinkMovementMethod.getInstance());

                viewHolder.lnr_close.setVisibility(View.VISIBLE);
            } else if (i % 3 == 0) {
                SpannableString spannableString = new SpannableString("LTC/USDT  SELL ");
                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View textView) {
                    }

                    @Override
                    public void updateDrawState(final TextPaint textPaint) {
                        textPaint.setColor(context.getResources().getColor(R.color.white));
                        textPaint.setUnderlineText(false);
//                    BackgroundColorSpan backgroundColorSpan= new BackgroundColorSpan(context.getResources().getColor(R.color.yellow));
                        textPaint.bgColor = context.getResources().getColor(R.color.google_red);
                    }
                };
                spannableString.setSpan(new RelativeSizeSpan(0.6f), spannableString.length() - 12, spannableString.length() - 0, 0); // set size
                spannableString.setSpan(clickableSpan, spannableString.length() - 6, spannableString.length() - 0, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                viewHolder.txt_coin_name.setText(spannableString, TextView.BufferType.SPANNABLE);
                viewHolder.txt_coin_name.setHighlightColor(context.getResources().getColor(R.color.yellow));
                viewHolder.txt_coin_name.setMovementMethod(LinkMovementMethod.getInstance());

                viewHolder.lnr_close.setVisibility(View.GONE);
            } else {
                SpannableString spannableString = new SpannableString("LTC/USDT  BUY ");
                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View textView) {
                    }

                    @Override
                    public void updateDrawState(final TextPaint textPaint) {
                        textPaint.setColor(context.getResources().getColor(R.color.black));
                        textPaint.setUnderlineText(false);
//                    BackgroundColorSpan backgroundColorSpan= new BackgroundColorSpan(context.getResources().getColor(R.color.yellow));
                        textPaint.bgColor = context.getResources().getColor(R.color.graph_brdr_green);
                    }
                };
                spannableString.setSpan(new RelativeSizeSpan(0.6f), spannableString.length() - 11, spannableString.length() - 0, 0); // set size
                spannableString.setSpan(clickableSpan, spannableString.length() - 5, spannableString.length() - 0, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                viewHolder.txt_coin_name.setText(spannableString, TextView.BufferType.SPANNABLE);
                viewHolder.txt_coin_name.setHighlightColor(context.getResources().getColor(R.color.yellow));
                viewHolder.txt_coin_name.setMovementMethod(LinkMovementMethod.getInstance());

                viewHolder.lnr_close.setVisibility(View.GONE);
            }

        }

    }

    @Override
    public int getItemCount() {
        if (isShort)
            return 3;
        else
            return 10;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_coin_name)
        TextView txt_coin_name;
        @BindView(R.id.txt_date)
        TextView txt_date;
        @BindView(R.id.txt_coin_bal)
        TextView txt_coin_bal;
        @BindView(R.id.txt_coin_bal_usd)
        TextView txt_coin_bal_usd;
        @BindView(R.id.lnr_close)
        LinearLayout lnr_close;
        @BindView(R.id.img_close)
        ImageView img_close;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
