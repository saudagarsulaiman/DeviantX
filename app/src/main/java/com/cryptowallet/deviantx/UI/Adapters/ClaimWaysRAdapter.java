package com.cryptowallet.deviantx.UI.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cryptowallet.deviantx.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClaimWaysRAdapter extends RecyclerView.Adapter<ClaimWaysRAdapter.ViewHolder> {

    Context context;
    ArrayList<String> claimsList;

    public ClaimWaysRAdapter(Context context, ArrayList<String> claimsList) {
        this.context = context;
        this.claimsList = claimsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.claim_list_lyt, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {

//        Picasso.with(context).load(claimsList.get(i)).into(viewHolder.img_logo);

//        viewHolder.txt_line.setText(claimsList.get(i));

//        viewHolder.txt_type.setText(claimsList.get(i));
//        viewHolder.txt_type.setBackground(context.getResources().getDrawable(R..));

//        viewHolder.txt_type.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return claimsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_type)
        TextView txt_type;
        @BindView(R.id.txt_line)
        TextView txt_line;
        @BindView(R.id.img_logo)
        ImageView img_logo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
