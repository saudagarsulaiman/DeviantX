package com.cryptowallet.deviantx.UI.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.UI.Models.AccountWallet;
import com.cryptowallet.deviantx.UI.Models.Transaction;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cryptowallet.deviantx.Utilities.MyApplication.myApplication;

public class WalletHistoryRAdapter extends RecyclerView.Adapter<WalletHistoryRAdapter.ViewHolder> {
    Context context;
    ArrayList<Transaction> transactionList;
    Transaction transaction;
    boolean hideBal;
    AccountWallet selectedAccountWallet;
    boolean oneTime;

    public WalletHistoryRAdapter(Context context, ArrayList<Transaction> transactions, AccountWallet selectedAccountWallet) {
        this.context = context;
        this.transactionList = transactions;
        transaction = null;
        this.hideBal = myApplication.getHideBalance();
        this.selectedAccountWallet = selectedAccountWallet;
        this.oneTime = true;
    }

    public void setIsHideBalance(Boolean isHideBalance) {
        this.hideBal = isHideBalance;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.wallet_history_lyt, viewGroup, false);
        WalletHistoryRAdapter.ViewHolder viewHolder = new WalletHistoryRAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

//        if (selectedAccountWallet.getStr_data_address().equals(transactionList.get(i).getCryptoWallet().getStr_data_cryptoWallet_address())) {
//        oneTime = false;
        viewHolder.lnr_trans_avail.setVisibility(View.VISIBLE);
        viewHolder.lnr_no_trans.setVisibility(View.GONE);
//        Picasso.with(context).load(R.drawable.dot_inactive).into(viewHolder.img_send_type);
//        viewHolder.txt_time.setText();
        viewHolder.txt_time.setText(getTime(transactionList.get(i).getStr_data_txnDate()));




        if (transactionList.get(i).getStr_data_category().equals("sent")) {
            if (!hideBal) {
                if (transactionList.get(i).getStr_data_toAddress().length() < 15) {
                    viewHolder.txt_trans_address.setText("To " + transactionList.get(i).getStr_data_toAddress());
                } else {
                    String address = transactionList.get(i).getStr_data_toAddress();
                    String dummy = ".....";
                    String first_half = String.format("%.7s", address);
                    String second_half = address.substring(address.length() - 7);
                    viewHolder.txt_trans_address.setText("To " + first_half + dummy + second_half);
                }
                viewHolder.txt_trans_amount.setText(String.format("%.4f", transactionList.get(i).getdbl_data_coinValue()) + " " + transactionList.get(i).getAllCoins().getStr_coin_code());
            } else {
                viewHolder.txt_trans_address.setText("To " + "***");
                viewHolder.txt_trans_amount.setText("***" + " " + transactionList.get(i).getAllCoins().getStr_coin_code());
            }
            viewHolder.txt_trans_type.setText(context.getResources().getString(R.string.sent));
            viewHolder.img_send_type.setBackground(context.getResources().getDrawable(R.drawable.cir_brdr_red));
            viewHolder.img_send_type.setImageDrawable(context.getResources().getDrawable(R.drawable.send));
            viewHolder.txt_trans_amount.setTextColor(context.getResources().getColor(R.color.google_red));
        } else {
            if (!hideBal) {
                if (transactionList.get(i).getStr_data_toAddress().length() < 15) {
                    viewHolder.txt_trans_address.setText("From " + transactionList.get(i).getStr_data_toAddress());
                } else {
                    String address = transactionList.get(i).getStr_data_toAddress();
                    String dummy = ".....";
                    String first_half = String.format("%.7s", address);
                    String second_half = address.substring(address.length() - 7);
                    viewHolder.txt_trans_address.setText("From " + first_half + dummy + second_half);
                }
                viewHolder.txt_trans_amount.setText(String.format("%.4f", transactionList.get(i).getdbl_data_coinValue()) + " " + transactionList.get(i).getAllCoins().getStr_coin_code());
            } else {
                viewHolder.txt_trans_address.setText("From " + "***");
                viewHolder.txt_trans_amount.setText("***" + " " + transactionList.get(i).getAllCoins().getStr_coin_code());
            }
            viewHolder.txt_trans_type.setText(context.getResources().getString(R.string.receive));
            viewHolder.img_send_type.setBackground(context.getResources().getDrawable(R.drawable.cir_brdr_green));
            viewHolder.img_send_type.setImageDrawable(context.getResources().getDrawable(R.drawable.receive));
            viewHolder.txt_trans_amount.setTextColor(context.getResources().getColor(R.color.green));
        }

       /* } else {
//            viewHolder.lnr_trans_avail.setVisibility(View.GONE);
            if (oneTime) {
                viewHolder.lnr_no_trans.setVisibility(View.VISIBLE);
                viewHolder.lnr_trans_avail.setVisibility(View.GONE);
                oneTime = false;
            } else {
                viewHolder.lnr_no_trans.setVisibility(View.GONE);
                oneTime = false;
            }
        }*/

    }

    @Override
    public int getItemCount() {
        return transactionList.size();
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


        public ViewHolder(View itemView) {
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
