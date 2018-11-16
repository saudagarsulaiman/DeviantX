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
import com.cryptowallet.deviantx.UI.Activities.WalletListActivity;
import com.cryptowallet.deviantx.UI.Models.AllCoins;
import com.cryptowallet.deviantx.UI.Models.WalletList;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cryptowallet.deviantx.Utilities.MyApplication.myApplication;

public class MyWalletListRAdapter extends RecyclerView.Adapter<MyWalletListRAdapter.ViewHolder> {

    Context context;
    ArrayList<WalletList> walletList;
    boolean hideBal;

    public MyWalletListRAdapter(Context context, ArrayList<WalletList> walletList) {
        this.context = context;
        this.walletList = walletList;
        this.hideBal = myApplication.getHideBalance();
    }

    public void setIsHideBalance(Boolean isHideBalance) {
        this.hideBal = isHideBalance;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_walletlist_lyt, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.txt_wallet_name.setText(walletList.get(i).getStr_data_name());
        if (hideBal){
            viewHolder.txt_wallet_bal.setText("~$ " + "***");
        }else {
            viewHolder.txt_wallet_bal.setText("~$ " + String.format("%.4f", walletList.get(i).getDbl_data_totalBal()));
        }
//        viewHolder.lnr_my_wallet.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return walletList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.lnr_my_wallet)
        LinearLayout lnr_my_wallet;
        @BindView(R.id.txt_wallet_name)
        TextView txt_wallet_name;
        @BindView(R.id.txt_wallet_bal)
        TextView txt_wallet_bal;
//        @BindView(R.id.txt_wallet_coin)
//        TextView txt_wallet_coin;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
