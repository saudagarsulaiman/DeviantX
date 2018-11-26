package com.cryptowallet.deviantx.UI.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.UI.Models.AllCoins;
import com.cryptowallet.deviantx.UI.Interfaces.CoinSelectableListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddCoinsRAdapter extends RecyclerView.Adapter<AddCoinsRAdapter.ViewHolder> {

    Context context;
    ArrayList<AllCoins> allCoinsList;
    CoinSelectableListener coinSelectableListener;

    public AddCoinsRAdapter(Activity context, ArrayList<AllCoins> allCoinsList, CoinSelectableListener coinSelectableListener) {
        this.context = context;
        this.allCoinsList = allCoinsList;
        this.coinSelectableListener = coinSelectableListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_coins_lyt, viewGroup, false);
        AddCoinsRAdapter.ViewHolder viewHolder = new AddCoinsRAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

        if (allCoinsList.get(i).getSelected()) {
            Picasso.with(context).load(R.drawable.dot_active).into(viewHolder.img_avail);
//                    viewHolder.img_avail.setImageDrawable(context.getResources().getDrawable(R.drawable.dot_active));
            viewHolder.lnr_item.setBackgroundResource(R.drawable.rec_yellow_lytblue_c5);
        } else {
            Picasso.with(context).load(R.drawable.dot_inactive).into(viewHolder.img_avail);
//                    viewHolder.img_avail.setImageDrawable(context.getResources().getDrawable(R.drawable.dot_inactive));
            viewHolder.lnr_item.setBackgroundResource(R.drawable.rec_lytblue_c5);
        }

        viewHolder.lnr_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (allCoinsList.get(i).getSelected()) {
                    allCoinsList.get(i).setSelected(false);
                } else{
                    allCoinsList.get(i).setSelected(true);
                }
                notifyItemChanged(i);*/
                coinSelectableListener.CoinSelected(allCoinsList, i);
            }
        });

        Picasso.with(context).load(allCoinsList.get(i).getStr_coin_logo()).into(viewHolder.img_coin);
        viewHolder.txt_coin_name.setText(allCoinsList.get(i).getStr_coin_name());
        viewHolder.txt_coin_value.setText("$ " + String.format("%4f", allCoinsList.get(i).getStr_coin_usdValue()) + " USD");
    }

    public void setCoinValue(Boolean isSelected, int pos) {
        allCoinsList.get(pos).setSelected(isSelected);
        notifyItemChanged(pos);
    }

    @Override
    public int getItemCount() {
        return allCoinsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.lnr_item)
        LinearLayout lnr_item;
        @BindView(R.id.img_coin)
        ImageView img_coin;
        @BindView(R.id.txt_coin_name)
        TextView txt_coin_name;
        @BindView(R.id.txt_coin_value)
        TextView txt_coin_value;
        @BindView(R.id.img_avail)
        ImageView img_avail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
