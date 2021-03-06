package com.cryptowallet.deviantx.UI.Adapters;

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
import com.cryptowallet.deviantx.UI.Models.FeaturedAirdrops;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FeaturedADHorizantalRAdapter extends RecyclerView.Adapter<FeaturedADHorizantalRAdapter.ViewHolder> {
    Context context;
    ArrayList<FeaturedAirdrops> featuredAirdrops;
    boolean isFull = false;

    public FeaturedADHorizantalRAdapter(Context context, ArrayList<FeaturedAirdrops> featuredAirdrops, boolean isFull) {
        this.context = context;
        this.featuredAirdrops = featuredAirdrops;
        this.isFull = isFull;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.coins_ad_lyt, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Picasso.with(context).load(featuredAirdrops.get(i).getStr_coinlogo()).into(viewHolder.img_coin);
        viewHolder.txt_coin_name_code.setText(featuredAirdrops.get(i).getStr_coinName() + " (" + featuredAirdrops.get(i).getStr_coinCode() + ")");
        viewHolder.txt_coin_value.setText("Estimated $" + String.format("%.2f", featuredAirdrops.get(i).getdbl_estimated()) + " ref");

      /*  viewHolder.lnr_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AllCoinsDB selected_coin = featuredAirdrops.get(i);
                Intent intent = new Intent(context, CoinInfoADAcivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(CONSTANTS.selectedCoin, featuredAirdrops.get(i));
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });*/
    }

    @Override
    public int getItemCount() {
 /*       if (isFull){
            if (featuredAirdrops.size()>)
        }else {

        }
 */
        return featuredAirdrops.size();
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
