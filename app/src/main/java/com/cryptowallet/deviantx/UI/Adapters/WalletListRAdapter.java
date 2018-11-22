package com.cryptowallet.deviantx.UI.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.UI.Models.WalletList;
import com.cryptowallet.trendchart.TrendView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cryptowallet.deviantx.Utilities.MyApplication.myApplication;

public class WalletListRAdapter extends RecyclerView.Adapter<WalletListRAdapter.ViewHolder> {
    Context context;
    ArrayList<WalletList> walletList;
    boolean hideBal;

    public WalletListRAdapter(Context context, ArrayList<WalletList> walletList) {
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


        /*GRAPH STARTS*/
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
        /*GRAPH ENDS*/

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
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
