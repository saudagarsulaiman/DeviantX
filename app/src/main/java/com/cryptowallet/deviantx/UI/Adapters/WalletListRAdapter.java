package com.cryptowallet.deviantx.UI.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.ServiceAPIs.CoinGraphApi;
import com.cryptowallet.deviantx.UI.Models.WalletList;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;
import com.cryptowallet.deviantx.Utilities.DeviantXApiClient;
import com.cryptowallet.trendchart.DateValue;
import com.cryptowallet.trendchart.TrendView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cryptowallet.deviantx.Utilities.MyApplication.myApplication;

public class WalletListRAdapter extends RecyclerView.Adapter<WalletListRAdapter.ViewHolder> {
    Context context;
    ArrayList<WalletList> walletList;
    boolean hideBal;
    long startTime;
    long endTime;

    public WalletListRAdapter(Context context, ArrayList<WalletList> walletList) {
        this.context = context;
        this.walletList = walletList;
        this.hideBal = myApplication.getHideBalance();
        this.startTime = System.currentTimeMillis() - (24 * 60 * 60 * 1000);
        this.endTime = System.currentTimeMillis();
    }

    public void setIsHideBalance(Boolean isHideBalance) {
        this.hideBal = isHideBalance;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.walletlist_lyt, viewGroup, false);
        WalletListRAdapter.ViewHolder viewHolder = new WalletListRAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.txt_wallet_name.setText(walletList.get(i).getStr_data_name());
        if (hideBal) {
            viewHolder.txt_wallet_bal.setText("~$ " + "***");
        } else {
            viewHolder.txt_wallet_bal.setText("~$ " + String.format("%.4f", walletList.get(i).getDbl_data_totalBal()));
        }

        if (i % 2 == 0) {
            viewHolder.lnr_wallet.setBackground(context.getResources().getDrawable(R.drawable.rec_brinjal_gradient_c2));
        } else {
            viewHolder.lnr_wallet.setBackground(context.getResources().getDrawable(R.drawable.rec_wh_gradient_c2));
        }

        if (CommonUtilities.isConnectionAvailable(context)) {
            if (walletList.get(i).getResponseList().size() == 0) {
                invokeCoinGraph(i, viewHolder.graph, "BTC", "1m", 800, startTime, endTime);
                viewHolder.pb.setVisibility(View.VISIBLE);
            } else {
                viewHolder.pb.setVisibility(View.GONE);
                setChartData(walletList.get(i).getResponseList(), viewHolder.graph, walletList.get(i).getHighValue());
            }
        } else {
            viewHolder.pb.setVisibility(View.GONE);
            CommonUtilities.ShowToastMessage(context, context.getResources().getString(R.string.internetconnection));
        }


        /*    *//*GRAPH STARTS*//*
        // first series is a line
        DataPoint[] points = new DataPoint[100];
        for (int j = 0; j < points.length; j++) {
            points[j] = new DataPoint(j, Math.sin(j * 0.5) * 20 * (Math.random() * 10 + 1));
        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(points);
        // set manual X bounds
        viewHolder.graph_wallet.getViewport().setYAxisBoundsManual(false);
//        viewHolder.graph_wallet.getViewport().setMinY(-150);
//        viewHolder.graph_wallet.getViewport().setMaxY(150);
        viewHolder.graph_wallet.getViewport().setXAxisBoundsManual(false);
//        viewHolder.graph_wallet.getViewport().setMinX(4);
//        viewHolder.graph_wallet.getViewport().setMaxX(80);
//        // styling series
//        series.setTitle("Random Curve 1");
        series.setColor(context.getResources().getColor(R.color.sky_blue));
        series.setThickness(5);
        series.setDrawBackground(true);
        series.setBackgroundColor(context.getResources().getColor(R.color.l_blue));
        viewHolder.graph_wallet.getViewport().setScrollable(false); // disables horizontal scrolling
        viewHolder.graph_wallet.getViewport().setScrollableY(false); // disables vertical scrolling
        viewHolder.graph_wallet.getViewport().setScalable(false); // disables horizontal zooming and scrolling
        viewHolder.graph_wallet.getViewport().setScalableY(false); // disables vertical zooming and scrolling
        viewHolder.graph_wallet.addSeries(series);
//        Disabling Labels
        GridLabelRenderer gridLabelRenderer = viewHolder.graph_wallet.getGridLabelRenderer();
        gridLabelRenderer.setHorizontalLabelsVisible(false);
        gridLabelRenderer.setVerticalLabelsVisible(false);
        gridLabelRenderer.setGridColor(Color.TRANSPARENT);
        *//*GRAPH ENDS*//*
         */
    }


    private void invokeCoinGraph(int pos, TrendView graph, final String symbol_coinCodeX, final String intervalX, final int limitX, final long startTimeX, final long endTimeX) {
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

                            List<DateValue> responseList = new ArrayList<>();
                            Double hisghValue = 0.0;
                            DataPoint[] points = new DataPoint[jsonArray.length()];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONArray childArray = jsonArray.getJSONArray(i);
                                for (int j = 0; j < childArray.length(); j++) {
                                    if (hisghValue < childArray.getDouble(2))
                                        hisghValue = childArray.getDouble(2);
                                    // coinGraph = new CoinGraph(childArray.getLong(0), childArray.getDouble(1), childArray.getDouble(2), childArray.getDouble(3), childArray.getDouble(4), childArray.getDouble(5), childArray.getDouble(6));
                                    responseList.add(new DateValue(childArray.getDouble(2), childArray.getLong(0)));
                                }
                            }
                            if (pos >= 0) {
                                walletList.get(pos).setResponseList(responseList);
                                walletList.get(pos).setHighValue(hisghValue);
                            } else {
                                //setChart(graph);
                                setChartData(responseList, graph, hisghValue);
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

    public void setChartData(List<DateValue> histories, TrendView graph, Double hisghValue) {
        graph.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));

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
        return walletList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_wallet_name)
        TextView txt_wallet_name;
        @BindView(R.id.txt_wallet_percentage)
        TextView txt_wallet_percentage;
        @BindView(R.id.txt_wallet_bal)
        TextView txt_wallet_bal;
        @BindView(R.id.txt_wallet_coin)
        TextView txt_wallet_coin;
        @BindView(R.id.graph_lyt)
        TrendView graph;
        @BindView(R.id.pb)
        ProgressBar pb;
        @BindView(R.id.graph_wallet)
        GraphView graph_wallet;
        @BindView(R.id.lnr_wallet)
        LinearLayout lnr_wallet;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
