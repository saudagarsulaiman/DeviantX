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
import com.cryptowallet.deviantx.UI.Models.ReceivedHistory;
import com.cryptowallet.deviantx.UI.Models.SentHistory;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cryptowallet.deviantx.Utilities.MyApplication.myApplication;

public class WalletHistoryRAdapter extends RecyclerView.Adapter<WalletHistoryRAdapter.ViewHolder> {
    Context context;
    //    ArrayList<Transaction> transactionList;
    ArrayList<SentHistory> sentHistoriesList;
    ArrayList<ReceivedHistory> receivedHistoriesList;
    //    Transaction transaction;
    boolean hideBal;
    //    AccountWallet selectedAccountWallet;
    boolean oneTime;
    boolean isSent;

    public WalletHistoryRAdapter(Context context, ArrayList<SentHistory> sentHistoriesList, ArrayList<ReceivedHistory> receivedHistoriesList, boolean isSent) {
        this.context = context;
//        this.transactionList = transactions;
//        transaction = null;
        this.hideBal = myApplication.getHideBalance();
//        this.selectedAccountWallet = selectedAccountWallet;
        this.oneTime = true;
        this.sentHistoriesList = sentHistoriesList;
        this.isSent = isSent;
        this.receivedHistoriesList = receivedHistoriesList;
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
//        viewHolder.txt_time.setText(getTime(transactionList.get(i).getStr_data_txnDate()));


/*
        if (isSent) {
            if (!hideBal) {
                if (sentHistoriesList.get(i).getStr_toAddress().length() < 15) {
                    viewHolder.txt_trans_address.setText("To " + sentHistoriesList.get(i).getStr_toAddress());
                } else {
                    String address = sentHistoriesList.get(i).getStr_toAddress();
                    String dummy = ".....";
                    String first_half = String.format("%.7s", address);
                    String second_half = address.substring(address.length() - 7);
                    viewHolder.txt_trans_address.setText("To " + first_half + dummy + second_half);
                }
                viewHolder.txt_trans_amount.setText(String.format("%.4f", sentHistoriesList.get(i).getDbl_coinValue()) + " " + sentHistoriesList.get(i).getStr_coinCode());
            } else {
                viewHolder.txt_trans_address.setText("To " + "***");
                viewHolder.txt_trans_amount.setText("***" + " " + sentHistoriesList.get(i).getStr_coinCode());
            }
            viewHolder.txt_time.setText(getTime(sentHistoriesList.get(i).getStr_date()));
            viewHolder.txt_trans_type.setText(context.getResources().getString(R.string.sent));
            viewHolder.img_send_type.setBackground(context.getResources().getDrawable(R.drawable.cir_brdr_red));
            viewHolder.img_send_type.setImageDrawable(context.getResources().getDrawable(R.drawable.send));
            viewHolder.txt_trans_amount.setTextColor(context.getResources().getColor(R.color.google_red));
        } else {
            if (!hideBal) {
                if (receivedHistoriesList.get(i).getStr_fromAddress().length() < 15) {
                    viewHolder.txt_trans_address.setText("From " + receivedHistoriesList.get(i).getStr_fromAddress());
                } else {
                    String address = receivedHistoriesList.get(i).getStr_fromAddress();
                    String dummy = ".....";
                    String first_half = String.format("%.7s", address);
                    String second_half = address.substring(address.length() - 7);
                    viewHolder.txt_trans_address.setText("From " + first_half + dummy + second_half);
                }
                viewHolder.txt_trans_amount.setText(String.format("%.4f", receivedHistoriesList.get(i).getDbl_coinValue()) + " " + receivedHistoriesList.get(i).getStr_coinCode());
            } else {
                viewHolder.txt_trans_address.setText("From " + "***");
                viewHolder.txt_trans_amount.setText("***" + " " + receivedHistoriesList.get(i).getStr_coinCode());
            }
            viewHolder.txt_time.setText(getTime(receivedHistoriesList.get(i).getStr_date()));
            viewHolder.txt_trans_type.setText(context.getResources().getString(R.string.received));
            viewHolder.img_send_type.setBackground(context.getResources().getDrawable(R.drawable.cir_brdr_green));
            viewHolder.img_send_type.setImageDrawable(context.getResources().getDrawable(R.drawable.receive));
            viewHolder.txt_trans_amount.setTextColor(context.getResources().getColor(R.color.green));
        }
*/

        if (sentHistoriesList.get(i).getStr_toAddress() != null) {
            if (!hideBal) {
                if (sentHistoriesList.get(i).getStr_toAddress().length() < 15) {
                    viewHolder.txt_trans_address.setText("To " + sentHistoriesList.get(i).getStr_toAddress());
                } else {
                    String address = sentHistoriesList.get(i).getStr_toAddress();
                    String dummy = ".....";
                    String first_half = String.format("%.7s", address);
                    String second_half = address.substring(address.length() - 7);
                    viewHolder.txt_trans_address.setText("To " + first_half + dummy + second_half);
                }
                viewHolder.txt_trans_amount.setText(String.format("%.4f", sentHistoriesList.get(i).getDbl_coinValue()) + " " + sentHistoriesList.get(i).getStr_coinCode());
            } else {
                viewHolder.txt_trans_address.setText("To " + "***");
                viewHolder.txt_trans_amount.setText("***" + " " + sentHistoriesList.get(i).getStr_coinCode());
            }
            viewHolder.txt_time.setText(getTime(sentHistoriesList.get(i).getStr_date()));
            viewHolder.txt_trans_type.setText(context.getResources().getString(R.string.sent));
            viewHolder.img_send_type.setBackground(context.getResources().getDrawable(R.drawable.cir_brdr_red));
            viewHolder.img_send_type.setImageDrawable(context.getResources().getDrawable(R.drawable.send));
            viewHolder.txt_trans_amount.setTextColor(context.getResources().getColor(R.color.google_red));
        } else {
            if (!hideBal) {
                if (sentHistoriesList.get(i).getStr_fromAddress().length() < 15) {
                    viewHolder.txt_trans_address.setText("From " + sentHistoriesList.get(i).getStr_fromAddress());
                } else {
                    String address = sentHistoriesList.get(i).getStr_fromAddress();
                    String dummy = ".....";
                    String first_half = String.format("%.7s", address);
                    String second_half = address.substring(address.length() - 7);
                    viewHolder.txt_trans_address.setText("From " + first_half + dummy + second_half);
                }
                viewHolder.txt_trans_amount.setText(String.format("%.4f", sentHistoriesList.get(i).getDbl_coinValue()) + " " + sentHistoriesList.get(i).getStr_coinCode());
            } else {
                viewHolder.txt_trans_address.setText("From " + "***");
                viewHolder.txt_trans_amount.setText("***" + " " + sentHistoriesList.get(i).getStr_coinCode());
            }
            viewHolder.txt_time.setText(getTime(sentHistoriesList.get(i).getStr_date()));
            viewHolder.txt_trans_type.setText(context.getResources().getString(R.string.received));
            viewHolder.img_send_type.setBackground(context.getResources().getDrawable(R.drawable.cir_brdr_green));
            viewHolder.img_send_type.setImageDrawable(context.getResources().getDrawable(R.drawable.receive));
            viewHolder.txt_trans_amount.setTextColor(context.getResources().getColor(R.color.green));
        }

    }

    @Override
    public int getItemCount() {
/*
        if (isSent) {
            return sentHistoriesList.size();
        } else {
            return receivedHistoriesList.size();
        }
*/
        return sentHistoriesList.size();
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
