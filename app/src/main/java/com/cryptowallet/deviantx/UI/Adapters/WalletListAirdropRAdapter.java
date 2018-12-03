package com.cryptowallet.deviantx.UI.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.UI.Activities.WithdrawFundsAirdropActivity;
import com.cryptowallet.deviantx.UI.Models.WalletList;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cryptowallet.deviantx.Utilities.MyApplication.myApplication;

public class WalletListAirdropRAdapter extends RecyclerView.Adapter<WalletListAirdropRAdapter.ViewHolder> {

    Context context;
    ArrayList<WalletList> walletLists;

    public WalletListAirdropRAdapter(Context context, ArrayList<WalletList> walletList) {
        this.context = context;
        this.walletLists = walletList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.wallet_list_lyt, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.txt_wallet_name.setText(walletLists.get(i).getStr_data_name());

        if (myApplication.getDefaultWallet() == i) {
            viewHolder.txt_wallet_name.setTextColor(context.getResources().getColor(R.color.white));
            viewHolder.lnr_item.setBackground(context.getResources().getDrawable(R.drawable.rec_brinjal1_gradient_c2));
        } else {
            viewHolder.txt_wallet_name.setTextColor(context.getResources().getColor(R.color.black));
            viewHolder.lnr_item.setBackground(context.getResources().getDrawable(R.drawable.rec_white));
        }

    }

    @Override
    public int getItemCount() {
        return walletLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_wallet_name)
        TextView txt_wallet_name;
        @BindView(R.id.lnr_item)
        LinearLayout lnr_item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
