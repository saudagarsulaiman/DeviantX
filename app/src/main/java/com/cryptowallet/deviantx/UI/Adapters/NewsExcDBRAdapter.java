package com.cryptowallet.deviantx.UI.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cryptowallet.deviantx.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsExcDBRAdapter extends RecyclerView.Adapter<NewsExcDBRAdapter.ViewHolder> {
    Context context;
    ArrayList<String> newsList;

    public NewsExcDBRAdapter(Context context, ArrayList<String> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_lyt, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        if (i % 2 == 0) {
//            viewHolder.txt_news_headline.setText("Golden December, this week, 10 new coins listed in Deviant X.");
            SpannableString spannableString = new SpannableString("Golden December, this week, 10 new coins listed in Deviant X.  ANNOUNCEMENT ");

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
            spannableString.setSpan(new RelativeSizeSpan(0.8f), spannableString.length() - 14, spannableString.length() - 0, 0); // set size
            spannableString.setSpan(clickableSpan, spannableString.length() - 14, spannableString.length() - 0, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewHolder.txt_news_headline.setText(spannableString, TextView.BufferType.SPANNABLE);
            viewHolder.txt_news_headline.setHighlightColor(context.getResources().getColor(R.color.yellow));
            viewHolder.txt_news_headline.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
//            viewHolder.txt_news_headline.setText("Stellar opens trade contest inscriptions, 500,000 XLM prize");
            SpannableString spannableString = new SpannableString("Stellar opens trade contest inscriptions, 500,000 XLM prize.  SPONSORED ");

            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View textView) {
                }

                @Override
                public void updateDrawState(final TextPaint textPaint) {
                    textPaint.setColor(context.getResources().getColor(R.color.black));
                    textPaint.setUnderlineText(false);
                    textPaint.bgColor = context.getResources().getColor(R.color.yellow);
                }
            };
            spannableString.setSpan(new RelativeSizeSpan(0.8f), spannableString.length() - 11, spannableString.length() - 0, 0); // set size
            spannableString.setSpan(clickableSpan, spannableString.length() - 11, spannableString.length() - 0, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewHolder.txt_news_headline.setText(spannableString, TextView.BufferType.SPANNABLE);
            viewHolder.txt_news_headline.setHighlightColor(context.getResources().getColor(R.color.yellow));
            viewHolder.txt_news_headline.setMovementMethod(LinkMovementMethod.getInstance());
        }

        viewHolder.txt_news_text.setText("Excepteur sint occaecat cupidatat non dent, sunt in culpa qui officia desurant mollit anim.");
    }

    @Override
    public int getItemCount() {
//        return newsList.size();
        return 10;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_news_headline)
        TextView txt_news_headline;
        @BindView(R.id.txt_news_text)
        TextView txt_news_text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
