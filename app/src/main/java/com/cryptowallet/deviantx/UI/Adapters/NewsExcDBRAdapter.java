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
import android.widget.TextView;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.UI.Models.NewsDX;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsExcDBRAdapter extends RecyclerView.Adapter<NewsExcDBRAdapter.ViewHolder> {
    Context context;
    ArrayList<NewsDX> newsList;
    String newsType;

    public NewsExcDBRAdapter(Context context, ArrayList<NewsDX> newsList) {
        this.context = context;
        this.newsList = newsList;
        this.newsType = "DX";
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

        viewHolder.txt_news_text.setText(newsList.get(i).getStr_content());

        if (newsList.get(i).getStr_newsPanelType().trim().equals("SPONSORED")) {
            newsType = newsList.get(i).getStr_head().trim() + " " + newsList.get(i).getStr_newsPanelType().trim() + " ";
            SpannableString spannableString = new SpannableString(newsType);
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
        } else {
            newsType = newsList.get(i).getStr_head().trim() + " " + newsList.get(i).getStr_newsPanelType().trim() + " ";
            SpannableString spannableString = new SpannableString(newsType);
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
            spannableString.setSpan(new RelativeSizeSpan(0.8f), spannableString.length() - 14, spannableString.length() - 0, 0); // set size
            spannableString.setSpan(clickableSpan, spannableString.length() - 14, spannableString.length() - 0, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewHolder.txt_news_headline.setText(spannableString, TextView.BufferType.SPANNABLE);
            viewHolder.txt_news_headline.setHighlightColor(context.getResources().getColor(R.color.yellow));
            viewHolder.txt_news_headline.setMovementMethod(LinkMovementMethod.getInstance());
        }

    }

    @Override
    public int getItemCount() {
        return newsList.size();
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
