package com.cryptowallet.deviantx.UI.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cryptowallet.deviantx.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExchangeDashboardSlideAdapter extends RecyclerView.Adapter<ExchangeDashboardSlideAdapter.ViewHolder> {

    Context context;

    public ExchangeDashboardSlideAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.slide_exc_db, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        switch (i) {
            case 0:
                viewHolder.img_left.setVisibility(View.GONE);
                viewHolder.img_right.setVisibility(View.GONE);
                viewHolder.img_center.setVisibility(View.GONE);
                viewHolder.img_bg.setBackground(context.getResources().getDrawable(R.drawable.exc_bg1));
                break;
            case 1:
                viewHolder.img_bg.setBackground(context.getResources().getDrawable(R.drawable.exc_bg2));
                viewHolder.img_left.setVisibility(View.VISIBLE);
                viewHolder.img_left.setBackground(context.getResources().getDrawable(R.drawable.exc_left2));
                viewHolder.img_right.setVisibility(View.VISIBLE);
                viewHolder.img_center.setVisibility(View.GONE);
                viewHolder.img_right.setBackground(context.getResources().getDrawable(R.drawable.exc_right2));
                break;
            case 2:
                viewHolder.img_left.setVisibility(View.GONE);
                viewHolder.img_right.setVisibility(View.GONE);
                viewHolder.img_center.setVisibility(View.VISIBLE);
                viewHolder.img_bg.setBackground(context.getResources().getDrawable(R.drawable.exc_bg3));
                viewHolder.img_center.setBackground(context.getResources().getDrawable(R.drawable.exc_center));
                break;
            default:
                viewHolder.img_left.setVisibility(View.GONE);
                viewHolder.img_right.setVisibility(View.GONE);
                viewHolder.img_center.setVisibility(View.GONE);
                viewHolder.img_bg.setBackground(context.getResources().getDrawable(R.drawable.exc_bg1));
                break;
        }

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_center)
        ImageView img_center;
        @BindView(R.id.img_right)
        ImageView img_right;
        @BindView(R.id.img_left)
        ImageView img_left;
        @BindView(R.id.img_bg)
        ImageView img_bg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
