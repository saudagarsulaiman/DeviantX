package com.cryptowallet.deviantx.UI.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.UI.Activities.ReceiveCoinActivity;
import com.cryptowallet.deviantx.UI.Activities.SendCoinActivity;
import com.cryptowallet.deviantx.UI.Activities.WalletDetailsActivity;
import com.cryptowallet.deviantx.UI.Activities.WalletHistoryActivity;
import com.cryptowallet.deviantx.UI.Interfaces.FavListener;
import com.cryptowallet.deviantx.UI.Models.AccountWallet;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CircleTransform;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.orhanobut.dialogplus.DialogPlus;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cryptowallet.deviantx.Utilities.MyApplication.myApplication;

public class MyWalletCoinsRAdapter extends RecyclerView.Adapter<MyWalletCoinsRAdapter.ViewHolder> {

    Context context;
    ArrayList<AccountWallet> accountWalletlist;
    //    ArrayList<AccountWallet> selectedAccountWallet;
    AccountWallet accountWallet;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    boolean hideBal;
    FavListener favListener;

    public MyWalletCoinsRAdapter(Activity context, ArrayList<AccountWallet> accountWalletlist, FavListener favListener) {
        this.context = context;
        this.favListener = favListener;
        this.accountWalletlist = accountWalletlist;
        accountWallet = null;
//        this.selectedAccountWallet = new ArrayList<>();
        sharedPreferences = context.getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        this.hideBal = myApplication.getHideBalance();
    }

    public void setIsHideBalance(Boolean isHideBalance) {
        this.hideBal = isHideBalance;
    }

    @NonNull
    @Override
    public MyWalletCoinsRAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mywallet_coins_rview_lyt, viewGroup, false);
        MyWalletCoinsRAdapter.ViewHolder viewHolder = new MyWalletCoinsRAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyWalletCoinsRAdapter.ViewHolder viewHolder, final int i) {
        Picasso.with(context).load(accountWalletlist.get(i).getAllCoins().getStr_coin_logo()).transform(new CircleTransform()).into(viewHolder.img_coin_logo);
        viewHolder.txt_coin_name.setText(accountWalletlist.get(i).getStr_data_walletName());
        if (!hideBal) {
            viewHolder.txt_coin_usd_value.setText("$ " + String.format("%.2f", accountWalletlist.get(i).getStr_data_balanceInUSD()) + " USD");
            viewHolder.txt_coin_value.setText(String.format("%.4f", accountWalletlist.get(i).getStr_data_balance()) + " " + accountWalletlist.get(i).getAllCoins().getStr_coin_code());
        } else {
            viewHolder.txt_coin_usd_value.setText("$ " + "***" + " USD");
            viewHolder.txt_coin_value.setText("***" + " " + accountWalletlist.get(i).getAllCoins().getStr_coin_code());
        }
        //Fav
        if (accountWalletlist.get(i).getAllCoins().getFav())
            viewHolder.fav_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.favourite));
        else
            viewHolder.fav_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.un_favourite));

//        viewHolder.txt_coin_usd_value.setText("$ "+accountWalletlist.get(i).getAllCoins().getStr_coin_usdValue()+" USD");

        viewHolder.fav_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favListener.addOrRemoveFav(accountWalletlist.get(i), i);
            }
        });
        viewHolder.lnr_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CommonUtilities.isConnectionAvailable(context)) {
                    accountWallet = new AccountWallet(
                            accountWalletlist.get(i).getStr_data_id(),
                            accountWalletlist.get(i).getStr_data_address(),
                            accountWalletlist.get(i).getStr_data_walletName(),
                            accountWalletlist.get(i).getStr_data_privatekey(),
                            accountWalletlist.get(i).getStr_data_passcode(),
                            accountWalletlist.get(i).getStr_data_balance(),
                            accountWalletlist.get(i).getStr_data_balanceInUSD(),
                            accountWalletlist.get(i).getStr_data_balanceInINR(),
                            accountWalletlist.get(i).getStr_data_account(),
                            accountWalletlist.get(i).getAllCoins());

                    customDialog(accountWallet);

                } else {
                    CommonUtilities.ShowToastMessage(context, context.getResources().getString(R.string.internetconnection));
                }
//                CommonUtilities.ShowToastMessage(context,"selected");
            }
        });

    }

    public void updateData(ArrayList<AccountWallet> accountWallet, int pos) {
        this.accountWalletlist = accountWallet;
        notifyItemChanged(pos);
    }

    private void customDialog(final AccountWallet accountWallet) {

        //                Creating A Custom Dialog Using DialogPlus
        com.orhanobut.dialogplus.ViewHolder viewHolder = new com.orhanobut.dialogplus.ViewHolder(R.layout.dialog_coins_transactions);
        final DialogPlus dialog = DialogPlus.newDialog(context)
                .setContentHolder(viewHolder)
                .setGravity(Gravity.BOTTOM)
                .setCancelable(true)
                .setInAnimation(R.anim.slide_in_bottom)
                .setOutAnimation(R.anim.slide_out_bottom)
                .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
//                        .setOnDismissListener(new OnDismissListener() {
//                            @Override
//                            public void onDismiss(DialogPlus dialog) {
//
//                            }
//                        })
//                        .setExpanded(true) // default is false, only works for grid and list
                .create();

//                Initializing Widgets
        View view = dialog.getHolderView();
        LinearLayout lnr_information = view.findViewById(R.id.lnr_information);
        LinearLayout lnr_send = view.findViewById(R.id.lnr_send);
        LinearLayout lnr_receive = view.findViewById(R.id.lnr_receive);
        LinearLayout lnr_delete = view.findViewById(R.id.lnr_delete);
        LinearLayout lnr_details = view.findViewById(R.id.lnr_details);
        LinearLayout lnr_history = view.findViewById(R.id.lnr_history);

        TextView txt_coin_value = view.findViewById(R.id.txt_coin_value);
        TextView txt_wallet_name = view.findViewById(R.id.txt_wallet_name);
        TextView txt_usd_value = view.findViewById(R.id.txt_usd_value);
        TextView txt_percentage = view.findViewById(R.id.txt_percentage);
        TextView txt_h_per = view.findViewById(R.id.txt_h_per);
        TextView txt_d_per = view.findViewById(R.id.txt_d_per);
        TextView txt_m_per = view.findViewById(R.id.txt_m_per);
        TextView txt_rank = view.findViewById(R.id.txt_rank);
        TextView txt_markcap_usd = view.findViewById(R.id.txt_markcap_usd);
        TextView txt_vol_usd = view.findViewById(R.id.txt_vol_usd);
        ImageView img_coin_logo = view.findViewById(R.id.img_coin_logo);


        /*GRAPH STARTS*/
        GraphView grapgh_dlg = view.findViewById(R.id.grapgh_dlg);
        // first series is a line
        DataPoint[] points = new DataPoint[100];
        for (int i = 0; i < points.length; i++) {
            points[i] = new DataPoint(i, Math.sin(i * 0.5) * 20 * (Math.random() * 10 + 1));
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(points);
        // set manual X bounds
        grapgh_dlg.getViewport().setYAxisBoundsManual(false);
//        grapgh_dlg.getViewport().setMinY(-150);
//        grapgh_dlg.getViewport().setMaxY(150);
        grapgh_dlg.getViewport().setXAxisBoundsManual(false);
//        grapgh_dlg.getViewport().setMinX(4);
//        grapgh_dlg.getViewport().setMaxX(80);
//        // styling series
//        series.setTitle("Random Curve 1");
        series.setColor(context.getResources().getColor(R.color.yellow));
        series.setThickness(8);
        series.setDrawBackground(true);
        series.setBackgroundColor(context.getResources().getColor(R.color.yellow_trans));
        grapgh_dlg.getViewport().setScrollable(false); // disables horizontal scrolling
        grapgh_dlg.getViewport().setScrollableY(false); // disables vertical scrolling
        grapgh_dlg.getViewport().setScalable(false); // disables horizontal zooming and scrolling
        grapgh_dlg.getViewport().setScalableY(false); // disables vertical zooming and scrolling
        grapgh_dlg.addSeries(series);
//        Disabling Labels
        GridLabelRenderer gridLabelRenderer = grapgh_dlg.getGridLabelRenderer();
        gridLabelRenderer.setHorizontalLabelsVisible(true);
        gridLabelRenderer.setVerticalLabelsVisible(true);
        gridLabelRenderer.setGridColor(Color.WHITE);
        /*GRAPH ENDS*/


        Picasso.with(context).load(accountWallet.getAllCoins().getStr_coin_logo()).into(img_coin_logo);
        txt_wallet_name.setText(accountWallet.getStr_data_walletName());

        DecimalFormat rank = new DecimalFormat("0.00");
        DecimalFormat value = new DecimalFormat("0.00");

        if (!hideBal) {
            txt_coin_value.setText(value.format(accountWallet.getStr_data_balance()) + " " + accountWallet.getAllCoins().getStr_coin_code());
            txt_usd_value.setText(value.format(accountWallet.getStr_data_balanceInUSD()) + " USD");
        } else {
            txt_coin_value.setText("***" + " " + accountWallet.getAllCoins().getStr_coin_code());
            txt_usd_value.setText("***" + " USD");
        }

        txt_rank.setText(accountWallet.getAllCoins().getInt_coin_rank() + "#");
        txt_markcap_usd.setText("$ " + value.format(accountWallet.getAllCoins().getDbl_coin_marketCap()));
        txt_vol_usd.setText("$ " + value.format(accountWallet.getAllCoins().getDbl_coin_volume()));


        if (accountWallet.getAllCoins().getDbl_coin_24h() < 0) {
            txt_percentage.setText("-" + rank.format(accountWallet.getAllCoins().getDbl_coin_24h()) + "%");
            txt_percentage.setTextColor(context.getResources().getColor(R.color.google_red));
            txt_h_per.setText("-" + rank.format(accountWallet.getAllCoins().getDbl_coin_24h()));
            txt_h_per.setTextColor(context.getResources().getColor(R.color.google_red));
        } else {
            txt_percentage.setText("+" + rank.format(accountWallet.getAllCoins().getDbl_coin_24h()) + "%");
            txt_percentage.setTextColor(context.getResources().getColor(R.color.green));
            txt_h_per.setTextColor(context.getResources().getColor(R.color.green));
            txt_h_per.setText("+" + rank.format(accountWallet.getAllCoins().getDbl_coin_24h()));
        }
        if (accountWallet.getAllCoins().getDbl_coin_7d() < 0) {
            txt_d_per.setText("-" + rank.format(accountWallet.getAllCoins().getDbl_coin_7d()));
            txt_d_per.setTextColor(context.getResources().getColor(R.color.google_red));
        } else {
            txt_d_per.setText("+" + rank.format(accountWallet.getAllCoins().getDbl_coin_7d()));
            txt_d_per.setTextColor(context.getResources().getColor(R.color.green));
        }
        if (accountWallet.getAllCoins().getDbl_coin_1m() < 0) {
            txt_m_per.setText("-" + rank.format(accountWallet.getAllCoins().getDbl_coin_1m()));
            txt_m_per.setTextColor(context.getResources().getColor(R.color.google_red));
        } else {
            txt_m_per.setText("+" + rank.format(accountWallet.getAllCoins().getDbl_coin_1m()));
            txt_m_per.setTextColor(context.getResources().getColor(R.color.green));
        }


        lnr_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent= new Intent(context,CoinInformationActivity.class);
//                context.startActivity(intent);
                dialog.dismiss();
            }
        });

        lnr_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SendCoinActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(CONSTANTS.selectedAccountWallet, accountWallet);
                intent.putExtras(bundle);
                context.startActivity(intent);
                dialog.dismiss();
            }
        });

        lnr_receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ReceiveCoinActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(CONSTANTS.selectedAccountWallet, accountWallet);
                intent.putExtras(bundle);
                context.startActivity(intent);
                dialog.dismiss();
            }
        });

        lnr_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WalletDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(CONSTANTS.selectedAccountWallet, accountWallet);
                intent.putExtras(bundle);
                context.startActivity(intent);
                dialog.dismiss();
            }
        });

        lnr_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WalletHistoryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(CONSTANTS.selectedAccountWallet, accountWallet);
                intent.putExtras(bundle);
                context.startActivity(intent);
                dialog.dismiss();
            }
        });

        lnr_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

//                Displaying DialogPlus
        dialog.show();


    }

    private boolean isNegative(double d) {
        return Double.doubleToRawLongBits(d) < 0;
    }

    @Override
    public int getItemCount() {
//        return 10;
        return accountWalletlist.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.lnr_item)
        LinearLayout lnr_item;
        @BindView(R.id.img_coin_logo)
        ImageView img_coin_logo;
        @BindView(R.id.fav_icon)
        ImageView fav_icon;
        @BindView(R.id.txt_coin_name)
        TextView txt_coin_name;
        @BindView(R.id.txt_coin_value)
        TextView txt_coin_value;
        @BindView(R.id.txt_coin_usd_value)
        TextView txt_coin_usd_value;
        @BindView(R.id.txt_percentage)
        TextView txt_percentage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

