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
import com.cryptowallet.deviantx.UI.Models.AirdropWalletTransaction;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AirdropWalletHistoryRAdapter extends RecyclerView.Adapter<AirdropWalletHistoryRAdapter.ViewHolder> {

    Context context;
    ArrayList<AirdropWalletTransaction> transactions;
    boolean hideBal;

    public AirdropWalletHistoryRAdapter(Context context, ArrayList<AirdropWalletTransaction> transactions) {
        this.context = context;
        this.transactions = transactions;
//        this.hideBal = myApplication.getHideBalance();
        this.hideBal = false;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.wallet_history_lyt, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        viewHolder.lnr_trans_avail.setVisibility(View.VISIBLE);
        viewHolder.lnr_no_trans.setVisibility(View.GONE);

        if (transactions.get(i).getStr_to() == null)
            transactions.get(i).setStr_to("0Dadjjdad455kdjbwk85154545dad85");

        if (transactions.get(i).getStr_txnDate() == null)
            transactions.get(i).setStr_txnDate("1550158633000");

        if (transactions.get(i).getStr_category().equals("Sent")) {
            if (!hideBal) {
                if (transactions.get(i).getStr_to().length() < 15) {
                    viewHolder.txt_trans_address.setText("To " + transactions.get(i).getStr_to());
                } else {
                    String address = transactions.get(i).getStr_to();
                    String dummy = ".....";
                    String first_half = String.format("%.7s", address);
                    String second_half = address.substring(address.length() - 7);
                    viewHolder.txt_trans_address.setText("To " + first_half + dummy + second_half);
                }
                viewHolder.txt_trans_amount.setText(String.format("%.4f", transactions.get(i).getDbl_amount()) + " " + transactions.get(i).getStr_coinCode());
            } else {
                viewHolder.txt_trans_address.setText("To " + "***");
                viewHolder.txt_trans_amount.setText("***" + " " + transactions.get(i).getStr_coinCode());
            }
            viewHolder.txt_time.setText(getTime(transactions.get(i).getStr_txnDate()));
            viewHolder.txt_trans_type.setText(context.getResources().getString(R.string.sent));
            viewHolder.img_send_type.setBackground(context.getResources().getDrawable(R.drawable.cir_brdr_red));
            viewHolder.img_send_type.setImageDrawable(context.getResources().getDrawable(R.drawable.send));
            viewHolder.txt_trans_amount.setTextColor(context.getResources().getColor(R.color.google_red));
        } else {
            if (!hideBal) {
                if (transactions.get(i).getStr_to().length() < 15) {
                    viewHolder.txt_trans_address.setText("From " + transactions.get(i).getStr_to());
                } else {
                    String address = transactions.get(i).getStr_to();
                    String dummy = ".....";
                    String first_half = String.format("%.7s", address);
                    String second_half = address.substring(address.length() - 7);
                    viewHolder.txt_trans_address.setText("From " + first_half + dummy + second_half);
                }
                viewHolder.txt_trans_amount.setText(String.format("%.4f", transactions.get(i).getDbl_amount()) + " " + transactions.get(i).getStr_coinCode());
            } else {
                viewHolder.txt_trans_address.setText("From " + "***");
                viewHolder.txt_trans_amount.setText("***" + " " + transactions.get(i).getStr_coinCode());
            }
            viewHolder.txt_time.setText(getTime(transactions.get(i).getStr_txnDate()));
            viewHolder.txt_trans_type.setText(context.getResources().getString(R.string.received));
            viewHolder.img_send_type.setBackground(context.getResources().getDrawable(R.drawable.cir_brdr_green));
            viewHolder.img_send_type.setImageDrawable(context.getResources().getDrawable(R.drawable.receive));
            viewHolder.txt_trans_amount.setTextColor(context.getResources().getColor(R.color.green));
        }

    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public void setIsHideBalance(Boolean isHideBalance) {
        this.hideBal = isHideBalance;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_send_type)
        ImageView img_send_type;
        @BindView(R.id.txt_time)
        TextView txt_time;
        @BindView(R.id.txt_trans_type)
        TextView txt_trans_type;
        @BindView(R.id.txt_trans_address)
        TextView txt_trans_address;
        @BindView(R.id.txt_trans_amount)
        TextView txt_trans_amount;

        @BindView(R.id.lnr_no_trans)
        LinearLayout lnr_no_trans;
        @BindView(R.id.lnr_trans_avail)
        LinearLayout lnr_trans_avail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private String getTime(String started) {
        try {
            return CommonUtilities.convertToHumanReadable(Long.parseLong(started));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

}
