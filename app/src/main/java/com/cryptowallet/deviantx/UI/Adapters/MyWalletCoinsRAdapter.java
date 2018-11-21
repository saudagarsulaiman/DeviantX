package com.cryptowallet.deviantx.UI.Adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.Toast;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.ServiceAPIs.CoinGraphApi;
import com.cryptowallet.deviantx.UI.Activities.CoinInformationActivity;
import com.cryptowallet.deviantx.UI.Activities.ReceiveCoinActivity;
import com.cryptowallet.deviantx.UI.Activities.SendCoinActivity;
import com.cryptowallet.deviantx.UI.Activities.WalletDetailsActivity;
import com.cryptowallet.deviantx.UI.Activities.WalletHistoryActivity;
import com.cryptowallet.deviantx.UI.Interfaces.FavListener;
import com.cryptowallet.deviantx.UI.Models.AccountWallet;
import com.cryptowallet.deviantx.UI.Models.CoinGraph;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CircleTransform;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;
import com.cryptowallet.deviantx.Utilities.DeviantXApiClient;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.gms.vision.text.Line;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.orhanobut.dialogplus.DialogPlus;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.net.SocketTimeoutException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    int DURATION_MILLIS;
    float SIZE;
    String DATA_SET_1;
    float GRANULARITY;
    private ProgressDialog progressDialog;
    CoinGraph coinGraph;
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
        DecimalFormat rank = new DecimalFormat("00.00");
        if (accountWallet.getAllCoins().getDbl_coin_24h() < 0) {
            viewHolder.txt_percentage.setText("" + rank.format(accountWallet.getAllCoins().getDbl_coin_24h()) + "%");
            viewHolder.txt_percentage.setTextColor(context.getResources().getColor(R.color.google_red));
        } else {
            viewHolder.txt_percentage.setText("+" + rank.format(accountWallet.getAllCoins().getDbl_coin_24h()) + "%");
            viewHolder.txt_percentage.setTextColor(context.getResources().getColor(R.color.green));
        }

        //Fav
        if (accountWalletlist.get(i).getAllCoins().getFav())
            viewHolder.fav_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.favourite));
        else
            viewHolder.fav_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.un_favourite));

//        viewHolder.txt_coin_usd_value.setText("$ "+accountWalletlist.get(i).getAllCoins().getStr_coin_usdValue()+" USD");

        if (CommonUtilities.isConnectionAvailable(context)) {
            if (accountWalletlist.get(i).getResponseList().size() == 0) {
                invokeCoinGraph(i, viewHolder.graph, accountWalletlist.get(i).getAllCoins().getStr_coin_code(), "1h", 800, startTime, endTime);
                viewHolder.pb.setVisibility(View.VISIBLE);
            } else {
                viewHolder.pb.setVisibility(View.GONE);
                setChart(viewHolder.graph);
                setChartData(accountWalletlist.get(i).getResponseList(), viewHolder.graph);
            }
        } else {
            viewHolder.pb.setVisibility(View.GONE);
            CommonUtilities.ShowToastMessage(context, context.getResources().getString(R.string.internetconnection));
        }

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
                    customDialog(accountWalletlist.get(i));

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
        ImageView img_center_back = view.findViewById(R.id.img_center_back);
        LineChart graph_dlg = view.findViewById(R.id.graph_dlg);


        if (CommonUtilities.isConnectionAvailable(context)) {
            if (accountWallet.getResponseList().size() == 0)
                invokeCoinGraph(-1, graph_dlg, accountWallet.getAllCoins().getStr_coin_code(), "1h", 800, startTime, endTime);
            else {
                setChart(graph_dlg);
                setChartData(accountWallet.getResponseList(), graph_dlg);
            }
        } else {
            CommonUtilities.ShowToastMessage(context, context.getResources().getString(R.string.internetconnection));
        }
//        /*GRAPH STARTS*/
//        GraphView grapgh_dlg = view.findViewById(R.id.grapgh_dlg);
//        // first series is a line
//        DataPoint[] points = new DataPoint[100];
//        for (int i = 0; i < points.length; i++) {
//            points[i] = new DataPoint(i, Math.sin(i * 0.5) * 20 * (Math.random() * 10 + 1));
//        }
//
//        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(points);
//        // set manual X bounds
//        grapgh_dlg.getViewport().setYAxisBoundsManual(false);
////        grapgh_dlg.getViewport().setMinY(-150);
////        grapgh_dlg.getViewport().setMaxY(150);
//        grapgh_dlg.getViewport().setXAxisBoundsManual(false);
////        grapgh_dlg.getViewport().setMinX(4);
////        grapgh_dlg.getViewport().setMaxX(80);
////        // styling series
////        series.setTitle("Random Curve 1");
//        series.setColor(context.context.getResources().getColor(R.color.yellow));
//        series.setThickness(8);
//        series.setDrawBackground(true);
//        series.setBackgroundColor(context.context.getResources().getColor(R.color.yellow_trans));
//        grapgh_dlg.getViewport().setScrollable(false); // disables horizontal scrolling
//        grapgh_dlg.getViewport().setScrollableY(false); // disables vertical scrolling
//        grapgh_dlg.getViewport().setScalable(false); // disables horizontal zooming and scrolling
//        grapgh_dlg.getViewport().setScalableY(false); // disables vertical zooming and scrolling
//        grapgh_dlg.addSeries(series);
////        Disabling Labels
//        GridLabelRenderer gridLabelRenderer = grapgh_dlg.getGridLabelRenderer();
//        gridLabelRenderer.setHorizontalLabelsVisible(true);
//        gridLabelRenderer.setVerticalLabelsVisible(true);
//        gridLabelRenderer.setGridColor(Color.WHITE);
//        /*GRAPH ENDS*/


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
            txt_percentage.setText("" + rank.format(accountWallet.getAllCoins().getDbl_coin_24h()) + "%");
            txt_percentage.setTextColor(context.getResources().getColor(R.color.google_red));
            txt_h_per.setText("" + rank.format(accountWallet.getAllCoins().getDbl_coin_24h()));
            txt_h_per.setTextColor(context.getResources().getColor(R.color.google_red));
        } else {
            txt_percentage.setText("+" + rank.format(accountWallet.getAllCoins().getDbl_coin_24h()) + "%");
            txt_percentage.setTextColor(context.getResources().getColor(R.color.green));
            txt_h_per.setTextColor(context.getResources().getColor(R.color.green));
            txt_h_per.setText("+" + rank.format(accountWallet.getAllCoins().getDbl_coin_24h()));
        }
        if (accountWallet.getAllCoins().getDbl_coin_7d() < 0) {
            txt_d_per.setText("" + rank.format(accountWallet.getAllCoins().getDbl_coin_7d()));
            txt_d_per.setTextColor(context.getResources().getColor(R.color.google_red));
        } else {
            txt_d_per.setText("+" + rank.format(accountWallet.getAllCoins().getDbl_coin_7d()));
            txt_d_per.setTextColor(context.getResources().getColor(R.color.green));
        }
        if (accountWallet.getAllCoins().getDbl_coin_1m() < 0) {
            txt_m_per.setText("" + rank.format(accountWallet.getAllCoins().getDbl_coin_1m()));
            txt_m_per.setTextColor(context.getResources().getColor(R.color.google_red));
        } else {
            txt_m_per.setText("+" + rank.format(accountWallet.getAllCoins().getDbl_coin_1m()));
            txt_m_per.setTextColor(context.getResources().getColor(R.color.green));
        }

        img_center_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

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

    private void invokeCoinGraph(int pos, LineChart graph, final String symbol_coinCodeX, final String intervalX, final int limitX, final long startTimeX, final long endTimeX) {
        try {
            // progressDialog = ProgressDialog.show(context, "", context.getResources().getString(R.string.please_wait), true);
            CoinGraphApi apiService = DeviantXApiClient.getCoinGraph().create(CoinGraphApi.class);
            Call<ResponseBody> apiResponse = apiService.getCoinGraph(symbol_coinCodeX, intervalX, limitX, startTimeX, endTimeX);
            Log.i("API:\t:", apiResponse.toString());
            apiResponse.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responsevalue = response.body().string();
                        //  progressDialog.dismiss();

                        if (!responsevalue.isEmpty() && responsevalue != null && !responsevalue.contains("code")) {
                            //  CommonUtilities.ShowToastMessage(context, "responsevalue" + responsevalue);
                            //progressDialog.dismiss();
                            JSONArray jsonArray = new JSONArray(responsevalue);

                            ArrayList<CoinGraph> responseList = new ArrayList<>();
                            DataPoint[] points = new DataPoint[jsonArray.length()];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONArray childArray = jsonArray.getJSONArray(i);
                                for (int j = 0; j < childArray.length(); j++) {
                                    coinGraph = new CoinGraph(childArray.getLong(0), childArray.getDouble(1), childArray.getDouble(2), childArray.getDouble(3), childArray.getDouble(4), childArray.getDouble(5), childArray.getDouble(6));
                                    responseList.add(coinGraph);
                                }
                            }
                            if (pos >= 0)
                                accountWalletlist.get(pos).setResponseList(responseList);
                            else {
                                setChart(graph);
                                setChartData(responseList, graph);
                            }
                            // progressDialog.dismiss();
                        } else {
                            // progressDialog.dismiss();
                            CommonUtilities.ShowToastMessage(context, responsevalue);
//                            Toast.makeText(context, responsevalue, Toast.LENGTH_LONG).show();
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + response.message());
                        }
                        if (pos >= 0)
                            notifyItemChanged(pos);
                    } catch (Exception e) {
                        e.printStackTrace();
                        //  progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(context, context.getResources().getString(R.string.errortxt));
//                        Toast.makeText(context, context.getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        //  progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(context, context.getResources().getString(R.string.Timeout));
//                        Toast.makeText(context, context.getResources().getString(R.string.Timeout), Toast.LENGTH_SHORT).show();
                    } else if (t instanceof java.net.ConnectException) {
                        // progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(context, context.getResources().getString(R.string.networkerror));
                        Toast.makeText(context, context.getResources().getString(R.string.networkerror), Toast.LENGTH_SHORT).show();
                    } else {
                        // progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(context, context.getResources().getString(R.string.errortxt));
//                        Toast.makeText(context, context.getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception ex) {
            // progressDialog.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(context, context.getResources().getString(R.string.errortxt));
//            Toast.makeText(context, context.getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
        }
    }

    private void setChartData(ArrayList<CoinGraph> histories, LineChart graph) {
        // set data
        Collections.sort(histories);
        ArrayList<Entry> values = new ArrayList<>();
        LineDataSet set1;
        for (CoinGraph history : histories) {
            values.add(new Entry(history.time.getTime(), history.high));
        }
        if (graph.getData() != null && graph.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) graph.getData().getDataSetByIndex(0);
            set1.setValues(values);
            graph.getData().notifyDataChanged();
            graph.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, DATA_SET_1);
            set1.setDrawIcons(false);
            set1.setLineWidth(1f);
            set1.setDrawCircles(false);
            set1.setValueTextSize(SIZE);
            set1.setDrawFilled(true);
            set1.setDrawValues(false);
            set1.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
            set1.setHighlightEnabled(false); // allow highlighting for DataSet
            // set this to false to disable the drawing of highlight indicator (lines)
            set1.setDrawHighlightIndicators(false);
            //  set1.setHighlightColor(Color.BLACK); // color for highlight indicator
//            set1.setDrawHighlightIndicators(false);

            // set1.setFillDrawable(ContextCompat.getDrawable(context, R.drawable.back_chart));
//            set1.setColor(ContextCompat.getColor(context, R.color.brdr_yellow));
//            set1.setFillColor(ContextCompat.getColor(context, R.color.yellow_trans));

            if (histories.get(0).getHigh() < histories.get(histories.size() - 1).getHigh()) {
                set1.setColor(ContextCompat.getColor(context, R.color.graph_brdr_green));
                set1.setFillColor(ContextCompat.getColor(context, R.color.graph_green));
            } else {
                set1.setColor(ContextCompat.getColor(context, R.color.graph_brdr_red));
                set1.setFillColor(ContextCompat.getColor(context, R.color.graph_red));
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            LineData data = new LineData(dataSets);

            // set data
            graph.setData(data);
            graph.getData().setHighlightEnabled(false);

            graph.animateY(DURATION_MILLIS);
            // get the legend (only possible after setting data)
            graph.getLegend().setEnabled(false);
        }
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
        @BindView(R.id.graph_lyt)
        LineChart graph;
        @BindView(R.id.pb)
        ProgressBar pb;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

