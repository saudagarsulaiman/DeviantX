package com.cryptowallet.deviantx.UI.Adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.UI.Activities.ReceiveCoinActivity;
import com.cryptowallet.deviantx.UI.Activities.SendCoinActivity;
import com.cryptowallet.deviantx.UI.Interfaces.FavListener;
import com.cryptowallet.deviantx.UI.Models.AccountWallet;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CircleTransform;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;
import com.cryptowallet.trendchart.DateValue;
import com.cryptowallet.trendchart.TrendView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.jjoe64.graphview.series.DataPoint;
import com.orhanobut.dialogplus.DialogPlus;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cryptowallet.deviantx.Utilities.MyApplication.myApplication;

public class MyWalletCoinsRAdapter extends RecyclerView.Adapter<MyWalletCoinsRAdapter.ViewHolder> {

    Context context;
    ArrayList<AccountWallet> accountWalletlist;
    //    ArrayList<AccountWalletDB> selectedAccountWallet;
    AccountWallet accountWallet;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    boolean hideBal;
    FavListener favListener;
    int DURATION_MILLIS;
    float SIZE;
    String DATA_SET_1;
    float GRANULARITY;
    private ProgressDialog progressDialog;
    long startTime;
    long endTime;

    public MyWalletCoinsRAdapter(Activity context, ArrayList<AccountWallet> accountWalletlist, FavListener favListener) {
        this.DURATION_MILLIS = 1000;
        this.SIZE = 9f;
        this.DATA_SET_1 = "DataSet 1";
        this.GRANULARITY = 100f;
        this.startTime = System.currentTimeMillis() - (24 * 60 * 60 * 1000);
        this.endTime = System.currentTimeMillis();
        this.context = context;
        this.favListener = favListener;
        this.accountWalletlist = accountWalletlist;
        accountWallet = null;
//        this.selectedAccountWallet = new ArrayList<>();
        sharedPreferences = context.getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        this.hideBal = myApplication.getHideBalance();
    }

    public void setAllCoins(ArrayList<AccountWallet> accountWalletlist) {
        this.accountWalletlist = accountWalletlist;
        //  notifyDataSetChanged();
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
        try {
            Picasso.with(context).load(accountWalletlist.get(i).getStr_coin_logo()).transform(new CircleTransform()).into(viewHolder.img_coin_logo);
            viewHolder.txt_coin_name.setText(accountWalletlist.get(i).getStr_coin_name());
            if (accountWalletlist.get(i).getResponseList() == null)
                accountWalletlist.get(i).setResponseList(new ArrayList<>());
            if (!hideBal) {
/*
                viewHolder.txt_coin_avail_value.setText("A: " + String.format("%.4f", accountWalletlist.get(i).getStr_data_balance()) + " " + accountWalletlist.get(i).getStr_coin_code());
                viewHolder.txt_coin_avail_usd_value.setText("$ " + String.format("%.2f", CommonUtilities.getUSDValue(accountWalletlist.get(i).getStr_data_balance(), accountWalletlist.get(i).getDbl_coin_usdValue())) + " USD");
                viewHolder.txt_coin_reserve_value.setText("R: " + String.format("%.4f", accountWalletlist.get(i).getStr_data_reservedBalance()) + " " + accountWalletlist.get(i).getStr_coin_code());
                viewHolder.txt_coin_usd_reserve_value.setText("$ " + String.format("%.2f", CommonUtilities.getUSDValue(accountWalletlist.get(i).getStr_data_reservedBalance(), accountWalletlist.get(i).getDbl_coin_usdValue())) + " USD");
                viewHolder.txt_coin_total_value.setText(String.format("%.4f", CommonUtilities.getTotalBal(accountWalletlist.get(i).getStr_data_reservedBalance(), accountWalletlist.get(i).getStr_data_balance())) + " " + accountWalletlist.get(i).getStr_coin_code());
                viewHolder.txt_coin_usd_total_value.setText("$ " + String.format("%.2f", CommonUtilities.getTotalBal(CommonUtilities.getUSDValue(accountWalletlist.get(i).getStr_data_reservedBalance(), accountWalletlist.get(i).getDbl_coin_usdValue()), CommonUtilities.getUSDValue(accountWalletlist.get(i).getStr_data_balance(), accountWalletlist.get(i).getDbl_coin_usdValue()))) + " USD");
*/
                viewHolder.txt_coin_avail_value.setText("A: " + String.format("%.4f", accountWalletlist.get(i).getStr_data_balance()) + " " + accountWalletlist.get(i).getStr_coin_code());
                viewHolder.txt_coin_avail_usd_value.setText("$ " + String.format("%.2f", CommonUtilities.getUSDValue(accountWalletlist.get(i).getStr_data_balance(), accountWalletlist.get(i).getDbl_coin_usdValue())) + " USD");
            } else {
/*
                viewHolder.txt_coin_avail_value.setText("A: " + "***" + " " + accountWalletlist.get(i).getStr_coin_code());
                viewHolder.txt_coin_avail_usd_value.setText("$ " + "***" + " USD");
                viewHolder.txt_coin_reserve_value.setText("R: " + "***" + " " + accountWalletlist.get(i).getStr_coin_code());
                viewHolder.txt_coin_usd_reserve_value.setText("$ " + "***" + " USD");
                viewHolder.txt_coin_total_value.setText("***" + " " + accountWalletlist.get(i).getStr_coin_code());
                viewHolder.txt_coin_usd_total_value.setText("$ " + "***" + " USD");
*/
                viewHolder.txt_coin_avail_value.setText("A: " + "***" + " " + accountWalletlist.get(i).getStr_coin_code());
                viewHolder.txt_coin_avail_usd_value.setText("$ " + "***" + " USD");
            }
            DecimalFormat rank = new DecimalFormat("0.00");
            if (accountWalletlist.get(i).getDbl_coin_24h() < 0) {
                viewHolder.txt_percentage.setText("" + rank.format(accountWalletlist.get(i).getDbl_coin_24h()) + "%");
                viewHolder.txt_percentage.setTextColor(context.getResources().getColor(R.color.google_red));
            } else {
                viewHolder.txt_percentage.setText("+" + rank.format(accountWalletlist.get(i).getDbl_coin_24h()) + "%");
                viewHolder.txt_percentage.setTextColor(context.getResources().getColor(R.color.green));
            }

            //Fav
            if (accountWalletlist.get(i).getFav())
                viewHolder.fav_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.favourite));
            else
                viewHolder.fav_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.un_favourite));

//        viewHolder.txt_coin_avail_usd_value.setText("$ "+accountWalletlist.get(i).getStr_coin_usdValue()+" USD");

            if (accountWalletlist.get(i).getResponseList().size() == 0) {
                coinCartData(i, accountWalletlist.get(i)/*.getAllCoins()*/, viewHolder.graph);
                viewHolder.pb.setVisibility(View.GONE);
            } else {
                viewHolder.pb.setVisibility(View.GONE);
                setChartData(accountWalletlist.get(i).getResponseList(), viewHolder.graph, accountWalletlist.get(i).getHighValue());
            }

/*
        if (CommonUtilities.isConnectionAvailable(context)) {
            if (accountWalletlist.get(i).getResponseList().size() == 0) {
                invokeCoinGraph(i, viewHolder.graph, accountWalletlist.get(i).getAllCoins().getStr_coin_code(), "1m", 800, startTime, endTime);
                viewHolder.pb.setVisibility(View.VISIBLE);
            } else {
                viewHolder.pb.setVisibility(View.GONE);
                setChartData(accountWalletlist.get(i).getResponseList(), viewHolder.graph, accountWalletlist.get(i).getHighValue());
            }
        } else {
            viewHolder.pb.setVisibility(View.GONE);
            CommonUtilities.ShowToastMessage(context, context.getResources().getString(R.string.internetconnection));
        }
*/

            viewHolder.fav_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    favListener.addOrRemoveFav(accountWalletlist.get(i), i);
                }
            });
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (CommonUtilities.isConnectionAvailable(context)) {
                        customDialog(accountWalletlist.get(i));
                        Log.e(" dev", " msg");
                    } else {
                        CommonUtilities.ShowToastMessage(context, context.getResources().getString(R.string.internetconnection));
                    }
//                CommonUtilities.ShowToastMessage(context,"selected");
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void coinCartData(int pos, AccountWallet selectedCoin, TrendView graph) {
        try {
            String chart_data, data = "";
            chart_data = selectedCoin.getStr_coin_daily_chart_data();
            JSONObject jsonObject = new JSONObject(chart_data);
            try {
                data = jsonObject.getString("Data");
            } catch (Exception e) {
                e.printStackTrace();
            }
            JSONArray jsonArray = new JSONArray(data);
            if (jsonArray.length() != 0) {
                ArrayList<DateValue> responseList = new ArrayList<>();
                Double hisghValue = 0.0;
                DataPoint[] points = new DataPoint[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject childobject = jsonArray.getJSONObject(i);
                    if (hisghValue < childobject.getDouble("high"))
                        hisghValue = childobject.getDouble("high");
                    responseList.add(new DateValue(childobject.getDouble("high"), childobject.getLong("time")));
                }
                if (pos >= 0) {
                    accountWalletlist.get(pos).setResponseList(responseList);
                    accountWalletlist.get(pos).setHighValue(hisghValue);
                    setChartData(responseList, graph, hisghValue);
                } else {
                    //setChart(graph);
                    setChartData(responseList, graph, hisghValue);
                }
            } else {
                CommonUtilities.ShowToastMessage(context, context.getResources().getString(R.string.empty_data));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        TextView txt_wallet_name = view.findViewById(R.id.txt_wallet_name);
        TextView txt_coin_avail_value = view.findViewById(R.id.txt_coin_avail_value);
        TextView txt_coin_reserve_value = view.findViewById(R.id.txt_coin_reserve_value);
        TextView txt_coin_total_value = view.findViewById(R.id.txt_coin_total_value);
        TextView txt_usd_avail_value = view.findViewById(R.id.txt_usd_avail_value);
        TextView txt_usd_reserve_value = view.findViewById(R.id.txt_usd_reserve_value);
        TextView txt_usd_total_value = view.findViewById(R.id.txt_usd_total_value);
        TextView txt_percentage = view.findViewById(R.id.txt_percentage);
        TextView txt_h_per = view.findViewById(R.id.txt_h_per);
        TextView txt_d_per = view.findViewById(R.id.txt_d_per);
        TextView txt_m_per = view.findViewById(R.id.txt_m_per);
        TextView txt_rank = view.findViewById(R.id.txt_rank);
        TextView txt_markcap_usd = view.findViewById(R.id.txt_markcap_usd);
        TextView txt_vol_usd = view.findViewById(R.id.txt_vol_usd);
        ImageView img_coin_logo = view.findViewById(R.id.img_coin_logo);
        ImageView img_center_back = view.findViewById(R.id.img_center_back);
        TrendView graph_dlg = view.findViewById(R.id.graph_dlg);


        if (CommonUtilities.isConnectionAvailable(context)) {
            if (accountWallet.getResponseList().size() == 0)
                coinCartData(-1, accountWallet/*.getAllCoins()*/, graph_dlg);
//            invokeCoinGraph(-1, graph_dlg, accountWallet.getAllCoins().getStr_coin_code(), "1h", 800, startTime, endTime);
            else {
                setChartData(accountWallet.getResponseList(), graph_dlg, accountWallet.getHighValue());
            }
        } else {
            CommonUtilities.ShowToastMessage(context, context.getResources().getString(R.string.internetconnection));
        }

        Picasso.with(context).load(accountWallet.getStr_coin_logo()).into(img_coin_logo);
        txt_wallet_name.setText(accountWallet.getStr_coin_name());

        DecimalFormat rank = new DecimalFormat("0.00");
        DecimalFormat value = new DecimalFormat("0.00");

        if (!hideBal) {
/*
            txt_coin_avail_value.setText(value.format(accountWallet.getStr_data_balance()) + " " + accountWallet.getStr_coin_code());
            txt_usd_avail_value.setText(value.format(accountWallet.getStr_data_balanceInUSD()) + " USD");
        } else {
            txt_coin_avail_value.setText("***" + " " + accountWallet.getStr_coin_code());
            txt_usd_avail_value.setText("***" + " USD");
        }
*/
            txt_coin_avail_value.setText("A: " + String.format("%.4f", accountWallet.getStr_data_balance()) + " " + accountWallet.getStr_coin_code());
            txt_usd_avail_value.setText("$ " + String.format("%.2f", CommonUtilities.getUSDValue(accountWallet.getStr_data_balance(), accountWallet.getDbl_coin_usdValue())) + " USD");
            txt_coin_reserve_value.setText("R: " + String.format("%.4f", accountWallet.getStr_data_reservedBalance()) + " " + accountWallet.getStr_coin_code());
            txt_usd_reserve_value.setText("$ " + String.format("%.2f", CommonUtilities.getUSDValue(accountWallet.getStr_data_reservedBalance(), accountWallet.getDbl_coin_usdValue())) + " USD");
            txt_coin_total_value.setText("T: " + String.format("%.4f", CommonUtilities.getTotalBal(accountWallet.getStr_data_reservedBalance(), accountWallet.getStr_data_balance())) + " " + accountWallet.getStr_coin_code());
            txt_usd_total_value.setText("$ " + String.format("%.2f", CommonUtilities.getTotalBal(CommonUtilities.getUSDValue(accountWallet.getStr_data_reservedBalance(), accountWallet.getDbl_coin_usdValue()), CommonUtilities.getUSDValue(accountWallet.getStr_data_balance(), accountWallet.getDbl_coin_usdValue()))) + " USD");
        } else {
            txt_coin_avail_value.setText("A: " + "***" + " " + accountWallet.getStr_coin_code());
            txt_usd_avail_value.setText("$ " + "***" + " USD");
            txt_coin_reserve_value.setText("R: " + "***" + " " + accountWallet.getStr_coin_code());
            txt_usd_reserve_value.setText("$ " + "***" + " USD");
            txt_coin_total_value.setText("T: " + "***" + " " + accountWallet.getStr_coin_code());
            txt_usd_total_value.setText("$ " + "***" + " USD");
        }
        txt_rank.setText("#" + accountWallet.getInt_coin_rank());

        NumberFormat nf = NumberFormat.getInstance(Locale.US);
        String mcap = nf.format(accountWallet.getDbl_coin_marketCap());
        String mvol = nf.format(accountWallet.getDbl_coin_volume());

        txt_markcap_usd.setText("$ " + mcap);
        txt_vol_usd.setText("$ " + mvol);


        if (accountWallet.getDbl_coin_24h() < 0) {
            txt_percentage.setText("" + rank.format(accountWallet.getDbl_coin_24h()) + "%");
            txt_percentage.setTextColor(context.getResources().getColor(R.color.google_red));
            txt_h_per.setText(rank.format(accountWallet.getDbl_coin_24h()) + "%");
            txt_h_per.setTextColor(context.getResources().getColor(R.color.google_red));
        } else {
            txt_percentage.setText("+" + rank.format(accountWallet.getDbl_coin_24h()) + "%");
            txt_percentage.setTextColor(context.getResources().getColor(R.color.green));
            txt_h_per.setTextColor(context.getResources().getColor(R.color.green));
            txt_h_per.setText("+" + rank.format(accountWallet.getDbl_coin_24h()) + "%");
        }
        if (accountWallet.getDbl_coin_7d() < 0) {
            txt_d_per.setText("" + rank.format(accountWallet.getDbl_coin_7d()) + "%");
            txt_d_per.setTextColor(context.getResources().getColor(R.color.google_red));
        } else {
            txt_d_per.setText("+" + rank.format(accountWallet.getDbl_coin_7d()) + "%");
            txt_d_per.setTextColor(context.getResources().getColor(R.color.green));
        }
        if (accountWallet.getDbl_coin_1m() < 0) {
            txt_m_per.setText("" + rank.format(accountWallet.getDbl_coin_1m()) + "%");
            txt_m_per.setTextColor(context.getResources().getColor(R.color.google_red));
        } else {
            txt_m_per.setText("+" + rank.format(accountWallet.getDbl_coin_1m()) + "%");
            txt_m_per.setTextColor(context.getResources().getColor(R.color.green));
        }

        img_center_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        lnr_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

//                Displaying DialogPlus
        dialog.show();


    }

    private void setChart(LineChart graph) {
        graph.setNoDataText(" ");
        // no description text
        graph.getDescription().setEnabled(false);
        graph.setTouchEnabled(false);
        graph.setDragEnabled(false);
        graph.setScaleEnabled(false);

        XAxis xAxis = graph.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
//        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
//        xAxis.setLabelCount(5);
        DateFormat formatter = new SimpleDateFormat("HH:mm");
        //formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        xAxis.setValueFormatter((value, axis) -> {
            Date date = new Date((long) value);
            return formatter.format(date);
        }); // hide text
        xAxis.setTextSize(0f);
        xAxis.setDrawLabels(false);
        xAxis.setTextColor(ContextCompat.getColor(context, R.color.black));
        xAxis.setGranularity(GRANULARITY);
        YAxis leftAxis = graph.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawLabels(false);
//        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setTextSize(0f);
        leftAxis.setTextColor(ContextCompat.getColor(context, R.color.black));
        graph.getAxisRight().setEnabled(false);
        graph.getLegend().setEnabled(false);
        graph.invalidate();
    }


    private void setChartData(List<DateValue> histories, TrendView graph, Double hisghValue) {
        graph.setBackgroundColor(context.getResources().getColor(R.color.white));
        if (histories.get(0).getValue() < histories.get(histories.size() - 1).getValue()) {
            graph.setBorderandFillColor(ContextCompat.getColor(context, R.color.graph_brdr_green), ContextCompat.getColor(context, R.color.graph_green));
        } else {
            graph.setBorderandFillColor(ContextCompat.getColor(context, R.color.graph_brdr_red), ContextCompat.getColor(context, R.color.graph_red));

        }
        //set static labels of x axis
        graph
                .withLine(new com.cryptowallet.trendchart.Line(histories))
                .withPrevClose(hisghValue)
                .withDisplayFrom(0)
                .withDisplayNumber(histories.size())
                .show();
    }


    @Override
    public int getItemCount() {
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
        @BindView(R.id.txt_coin_avail_value)
        TextView txt_coin_avail_value;
        @BindView(R.id.txt_coin_reserve_value)
        TextView txt_coin_reserve_value;
        @BindView(R.id.txt_coin_total_value)
        TextView txt_coin_total_value;
        @BindView(R.id.txt_coin_avail_usd_value)
        TextView txt_coin_avail_usd_value;
        @BindView(R.id.txt_coin_usd_reserve_value)
        TextView txt_coin_usd_reserve_value;
        @BindView(R.id.txt_coin_usd_total_value)
        TextView txt_coin_usd_total_value;
        @BindView(R.id.txt_percentage)
        TextView txt_percentage;
        @BindView(R.id.graph_lyt)
        TrendView graph;
        @BindView(R.id.pb)
        ProgressBar pb;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

