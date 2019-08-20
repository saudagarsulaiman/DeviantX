package com.cryptowallet.deviantx.UI.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.UI.Activities.ChangellyActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ToolsAdapter extends RecyclerView.Adapter<ToolsAdapter.ViewHolder> {
    Context context;
    ArrayList<String> title;
    ArrayList<String> slogan;
    ArrayList<Integer> icons;

    public ToolsAdapter(Context context, ArrayList<String> title, ArrayList<String> slogan, ArrayList<Integer> icons) {
        this.context = context;
        this.title = title;
        this.slogan = slogan;
        this.icons = icons;
    }

    @NonNull
    @Override
    public ToolsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.slide_tools, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ToolsAdapter.ViewHolder viewHolder, int i) {
        switch (i) {
            case /*0*/ 2:
                viewHolder.txt_coming_soon.setVisibility(View.VISIBLE);
                viewHolder.lnr_tools_ect.setBackground(context.getResources().getDrawable(R.drawable.rec_brinjal_gradient_c2));
                break;
            case /*1*/ 3:
                viewHolder.txt_coming_soon.setVisibility(View.VISIBLE);
                viewHolder.lnr_tools_ect.setBackground(context.getResources().getDrawable(R.drawable.rec_wh_gradient_c2));
                break;
            case /*3*/ /*2*/ 1:
                viewHolder.txt_coming_soon.setVisibility(View.VISIBLE);
                viewHolder.lnr_tools_ect.setBackground(context.getResources().getDrawable(R.drawable.rec_brinjal_gradient_c2));
                break;
            case /*4*/ /*3*/ 0:
                viewHolder.txt_coming_soon.setVisibility(View.INVISIBLE);
                viewHolder.lnr_tools_ect.setBackground(context.getResources().getDrawable(R.drawable.rec_wh_gradient_c2));
                break;
          /*  case 2:
                viewHolder.lnr_tools_ect.setBackground(context.getResources().getDrawable(R.drawable.rec_airdrop_gradient));
                break;*/
            default:
                viewHolder.lnr_tools_ect.setBackground(context.getResources().getDrawable(R.drawable.rec_brinjal_gradient_c2));
                break;
        }

//        if (i % 2 == 0) {
//            viewHolder.lnr_tools_ect.setBackground(context.getResources().getDrawable(R.drawable.rec_brinjal_gradient_c2));
//        } else {
//            viewHolder.lnr_tools_ect.setBackground(context.getResources().getDrawable(R.drawable.rec_wh_gradient_c2));
//        }

        viewHolder.txt_title.setText(title.get(i));
        viewHolder.txt_slogan.setText(slogan.get(i));
        Picasso.with(context).load(icons.get(i)).into(viewHolder.img_icon);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i == 0) {
                    Intent intent = new Intent(context, ChangellyActivity.class);
                    context.startActivity(intent);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return title.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.lnr_tools_ect)
        LinearLayout lnr_tools_ect;
        @BindView(R.id.img_icon)
        ImageView img_icon;
        @BindView(R.id.txt_title)
        TextView txt_title;
        @BindView(R.id.txt_coming_soon)
        TextView txt_coming_soon;
        @BindView(R.id.txt_slogan)
        TextView txt_slogan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
