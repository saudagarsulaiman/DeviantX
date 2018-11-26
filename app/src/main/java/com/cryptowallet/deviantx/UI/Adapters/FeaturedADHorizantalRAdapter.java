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
import com.cryptowallet.deviantx.Utilities.CONSTANTS;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FeaturedADHorizantalRAdapter extends RecyclerView.Adapter<FeaturedADHorizantalRAdapter.ViewHolder> {
    Context context;

    public FeaturedADHorizantalRAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.coins_ad_lyt, viewGroup, false);
        FeaturedADHorizantalRAdapter.ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

//        Picasso.with(context).load(i).into(viewHolder.img_coin);
        viewHolder.txt_coin_name_code.setText("Coin Name" + " (" + "Code" + ")");
        viewHolder.txt_coin_value.setText("Estimated $" + i + " ref");

        viewHolder.lnr_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CoinInfoADAcivity.class);
                Bundle bundle = new Bundle();
//                bundle.putParcelable(CONSTANTS.selectedCoin,"coin");
                bundle.putString(CONSTANTS.selectedCoin, "coin");
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 10;
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
