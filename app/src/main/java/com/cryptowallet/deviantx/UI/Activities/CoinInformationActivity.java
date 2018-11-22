package com.cryptowallet.deviantx.UI.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.ServiceAPIs.CoinGraphApi;
import com.cryptowallet.deviantx.UI.Adapters.MyAdapter;
import com.cryptowallet.deviantx.UI.Adapters.SpinnerDaysAdapter;
import com.cryptowallet.deviantx.UI.Models.AllCoins;
import com.cryptowallet.deviantx.UI.Models.CoinGraph;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;
import com.cryptowallet.deviantx.Utilities.DeviantXApiClient;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.jjoe64.graphview.series.DataPoint;
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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cryptowallet.deviantx.Utilities.MyApplication.myApplication;

public class CoinInformationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    @BindView(R.id.img_coin_logo)
    ImageView img_coin_logo;
    @BindView(R.id.txt_coin_name)
    TextView txt_coin_name;
    @BindView(R.id.txt_coin_usd)
    TextView txt_coin_usd;
    @BindView(R.id.txt_per_change)
    TextView txt_per_change;
    @BindView(R.id.txt_per_high)
    TextView txt_per_high;
    @BindView(R.id.txt_per_low)
    TextView txt_per_low;
    @BindView(R.id.toolbar_center_back)
    Toolbar toolbar_center_back;
    @BindView(R.id.line_chart)
    LineChart line_chart;
    @BindView(R.id.lnr_line_graph)
    LinearLayout lnr_line_graph;
    @BindView(R.id.lnr_candle_graph)
    LinearLayout lnr_candle_graph;
    @BindView(R.id.lnr_spnr)
    LinearLayout lnr_spnr;
    @BindView(R.id.spnr_days)
    Spinner spnr_days;
    @BindView(R.id.pb)
    ProgressBar pb;
    @BindView(R.id.candle_chart)
    CandleStickChart candle_chart;
    @BindView(R.id.lnr_result)
    LinearLayout lnr_result;
    @BindView(R.id.txt_open)
    TextView txt_open;
    @BindView(R.id.txt_high)
    TextView txt_high;
    @BindView(R.id.txt_low)
    TextView txt_low;
    @BindView(R.id.txt_close)
    TextView txt_close;
    ArrayList<CoinGraph> responseList;
    public static final int DURATION_MILLIS = 1000;
    public static final float SIZE = 9f;
    public static final String DATA_SET_1 = "DataSet 1";
    public static final float GRANULARITY = 100f;

    AllCoins selectedCoin;

    //    SharedPreferences sharedPreferences;
//    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;

    ArrayList<CoinGraph> coinGraphList;
    List<String> stringResponseList;
    CoinGraph coinGraph;

    /*
    private final Handler mHandler = new Handler();
     private Runnable mTimer1;
     private Runnable mTimer2;
     private LineGraphSeries<DataPoint> mSeries1;
     private LineGraphSeries<DataPoint> mSeries2;
     private double graph2LastXValue = 5d;
 */
    @Override
    protected void onResume() {
        super.onResume();
        myApplication.disableScreenCapture(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_information);

        ButterKnife.bind(this);

//        sharedPreferences = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
//        editor = sharedPreferences.edit();

        coinGraphList = new ArrayList<>();
        stringResponseList = new ArrayList<>();
        responseList = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        selectedCoin = bundle.getParcelable(CONSTANTS.selectedCoin);


        toolbar_center_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        Picasso.with(CoinInformationActivity.this).load(selectedCoin.getStr_coin_logo()).into(img_coin_logo);
        txt_coin_name.setText(selectedCoin.getStr_coin_name());
        txt_coin_usd.setText("$" + String.format("%.4f", selectedCoin.getDbl_coin_usdValue()));

        DecimalFormat rank = new DecimalFormat("00.00");
        if (selectedCoin.getDbl_coin_24h() < 0) {
            txt_per_change.setText(rank.format(selectedCoin.getDbl_coin_24h()) + "%");
            txt_per_change.setTextColor(getResources().getColor(R.color.google_red));
        } /*else if (responseList.get(responseList.size() - 1).getChange() == 0) {
                            }*/ else {
            txt_per_change.setText("+" + rank.format(selectedCoin.getDbl_coin_24h()) + "%");
            txt_per_change.setTextColor(getResources().getColor(R.color.green));
        }

        // Spinner click listener
        spnr_days.setOnItemSelectedListener(this);
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("1 Day");
        categories.add("7 Days");
//        categories.add("1 Month");
//        categories.add("6 Months");
//        categories.add("1 Year");
//        categories.add("All");

//        // Creating adapter for spinner
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item_days, categories);
//
//        // Drop down layout style - list view with radio button
//        dataAdapter.setDropDownViewResource(R.layout.spinner_item_days_dropdown);

        // attaching data adapter to spinner
        spnr_days.setAdapter(new SpinnerDaysAdapter(CoinInformationActivity.this, R.layout.spinner_item_days_dropdown, categories));

        lnr_candle_graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnr_candle_graph.setBackground(getResources().getDrawable(R.drawable.rec_brinjal_gradient_c2));
                lnr_line_graph.setBackground(getResources().getDrawable(R.drawable.rec_grey_trans_c2));
                line_chart.setVisibility(View.GONE);
                candle_chart.setVisibility(View.VISIBLE);
                SwitchCases(spnr_days.getSelectedItem().toString());
            }
        });

        lnr_line_graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                CommonUtilities.ShowToastMessage(CoinInformationActivity.this, spnr_days.getSelectedItem().toString());
                lnr_line_graph.setBackground(getResources().getDrawable(R.drawable.rec_brinjal_gradient_c2));
                lnr_candle_graph.setBackground(getResources().getDrawable(R.drawable.rec_grey_trans_c2));
                line_chart.setVisibility(View.VISIBLE);
                candle_chart.setVisibility(View.GONE);
                SwitchCases(spnr_days.getSelectedItem().toString());
            }
        });

        long startTime = System.currentTimeMillis() - (24 * 60 * 60 * 1000);
        long endTime = System.currentTimeMillis();
        if (CommonUtilities.isConnectionAvailable(CoinInformationActivity.this)) {
            invokeCoinDefGraph(selectedCoin.getStr_coin_code(), "1h", 800, startTime, endTime);
            pb.setVisibility(View.VISIBLE);
        } else {
            pb.setVisibility(View.GONE);
            CommonUtilities.ShowToastMessage(CoinInformationActivity.this, getResources().getString(R.string.internetconnection));
        }




    /*
  LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 5),
                new DataPoint(5, 10),
                new DataPoint(15, 2),
                new DataPoint(50, 132),
                new DataPoint(55, 10),
                new DataPoint(65, 2),
                new DataPoint(70, 32),
                new DataPoint(95, 10),
                new DataPoint(115, 182),
                new DataPoint(150, 32),
                new DataPoint(155, 110),
                new DataPoint(175, 114),
                new DataPoint(180, 32)
        });

        // styling series
        series.setTitle("Random Curve 1");
        series.setColor(getResources().getColor(R.color.yellow));
        series.setThickness(8);
        series.setDrawBackground(true);
        series.setBackgroundColor(getResources().getColor(R.color.yellow_trans));

        line_chart.addSeries(series);
*/


/*
        // generate Dates
        Calendar calendar = Calendar.getInstance();
        Date d1 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d2 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d3 = calendar.getTime();

// you can directly pass Date objects to DataPoint-Constructor
// this will convert the Date to double via Date#getTime()
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(d1, 1),
                new DataPoint(d2, 5),
                new DataPoint(d3, 3)
        });

        line_chart.addSeries(series);

// set date label formatter
        line_chart.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(CoinInformationActivity.this));
        line_chart.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space

// set manual x bounds to have nice steps
        line_chart.getViewport().setMinX(d1.getTime());
        line_chart.getViewport().setMaxX(d3.getTime());
        line_chart.getViewport().setXAxisBoundsManual(true);

// as we use dates as labels, the human rounding to nice readable numbers
// is not necessary
        line_chart.getGridLabelRenderer().setHumanRounding(false);
*/



/*
        mSeries1 = new LineGraphSeries<>(generateData());
        line_chart.addSeries(mSeries1);
*/

/*
        mSeries2 = new LineGraphSeries<>();
        line_chart.addSeries(mSeries2);
        line_chart.getViewport().setXAxisBoundsManual(true);
        line_chart.getViewport().setMinX(0);
        line_chart.getViewport().setMaxX(40);
*/


//        GridLabelRenderer gridLabelRenderer = line_chart.getGridLabelRenderer();
//        GridLabelRenderer.Styles mStyles;
//        GridLabelRenderer.GridStyle mgridStyle;
//        GridLabelRenderer.VerticalLabelsVAlign verticalLabelsVAlign;
//        gridLabelRenderer.setHorizontalLabelsVisible(true);
//        gridLabelRenderer.setVerticalLabelsVisible(true);

    }


    private void invokeCoinDefGraph(final String symbol_coinCodeX, final String intervalX, final int limitX, final long startTimeX, final long endTimeX) {
        try {
//            progressDialog = ProgressDialog.show(CoinInformationActivity.this, "", getResources().getString(R.string.please_wait), true);
            CoinGraphApi apiService = DeviantXApiClient.getCoinGraph().create(CoinGraphApi.class);
            Call<ResponseBody> apiResponse = apiService.getCoinGraph(symbol_coinCodeX, intervalX, limitX, startTimeX, endTimeX);
            Log.i("API:\t:", apiResponse.toString());
            apiResponse.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responsevalue = response.body().string();

                        if (!responsevalue.isEmpty() && responsevalue != null && !responsevalue.contains("code")) {
                            //  CommonUtilities.ShowToastMessage(CoinInformationActivity.this, "responsevalue" + responsevalue);
                            pb.setVisibility(View.GONE);
                            //progressDialog.dismiss();
                            JSONArray jsonArray = new JSONArray(responsevalue);

                            responseList = new ArrayList<>();
                            DataPoint[] points = new DataPoint[jsonArray.length()];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONArray childArray = jsonArray.getJSONArray(i);
                                for (int j = 0; j < childArray.length(); j++) {
                                    coinGraph = new CoinGraph(childArray.getLong(0), childArray.getDouble(1), childArray.getDouble(2), childArray.getDouble(3), childArray.getDouble(4), childArray.getDouble(5), childArray.getDouble(6));
                                    responseList.add(coinGraph);
                                    /*Calendar calendar = Calendar.getInstance();
                                    calendar.setTimeInMillis(childArray.getLong(0));
                                    Date d1 = calendar.getTime();
                                    points[i]=new DataPoint(d1,childArray.getLong(2));*/
                                }

                            }
                            txt_open.setText(getResources().getString(R.string.open));
                            txt_high.setText(getResources().getString(R.string.high));
                            txt_low.setText(getResources().getString(R.string.low));
                            txt_close.setText(getResources().getString(R.string.closee));
                            setChart();
                            line_chart.setData(null);
                            candle_chart.setData(null);
                            setChartData(responseList);

                            txt_per_high.setText("$" + String.format("%.4f", responseList.get(responseList.size() - 1).getHigh()));
                            txt_per_low.setText("$" + String.format("%.4f", responseList.get(responseList.size() - 1).getLow()));


                           /* Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(jsonArray.getJSONArray(0).getLong(0));
                            Date d1 = calendar.getTime();
                            Calendar calendar1 = Calendar.getInstance();
                            calendar1.setTimeInMillis(jsonArray.getJSONArray(jsonArray.length()-1).getLong(0));
                            Date d2 = calendar1.getTime();
                            line_chart.getViewport().setMinX(d1.getTime());
                            line_chart.getViewport().setMaxX(d2.getTime());
*/

                        } else {
                            CommonUtilities.ShowToastMessage(CoinInformationActivity.this, responsevalue);
//                            Toast.makeText(getApplicationContext(), responsevalue, Toast.LENGTH_LONG).show();
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + response.message());
                            pb.setVisibility(View.GONE);
                        }

                    } catch (Exception e) {
                        pb.setVisibility(View.GONE);
                        e.printStackTrace();
//                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(CoinInformationActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        pb.setVisibility(View.GONE);
//                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(CoinInformationActivity.this, getResources().getString(R.string.Timeout));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Timeout), Toast.LENGTH_SHORT).show();
                    } else if (t instanceof java.net.ConnectException) {
                        pb.setVisibility(View.GONE);
//                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(CoinInformationActivity.this, getResources().getString(R.string.networkerror));
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.networkerror), Toast.LENGTH_SHORT).show();
                    } else {
                        pb.setVisibility(View.GONE);
//                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(CoinInformationActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception ex) {
            pb.setVisibility(View.GONE);
//            progressDialog.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(CoinInformationActivity.this, getResources().getString(R.string.errortxt));
//            Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
        }
    }

    private void invokeCoinGraph(final String symbol_coinCodeX, final String intervalX, final int limitX, final long startTimeX, final long endTimeX) {
        try {
//            progressDialog = ProgressDialog.show(CoinInformationActivity.this, "", getResources().getString(R.string.please_wait), true);
            CoinGraphApi apiService = DeviantXApiClient.getCoinGraph().create(CoinGraphApi.class);
            Call<ResponseBody> apiResponse = apiService.getCoinGraph(symbol_coinCodeX, intervalX, limitX, startTimeX, endTimeX);
            Log.i("API:\t:", apiResponse.toString());
            apiResponse.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responsevalue = response.body().string();

                        if (!responsevalue.isEmpty() && responsevalue != null && !responsevalue.contains("code")) {
                            //  CommonUtilities.ShowToastMessage(CoinInformationActivity.this, "responsevalue" + responsevalue);
                            pb.setVisibility(View.GONE);
                            //progressDialog.dismiss();
                            JSONArray jsonArray = new JSONArray(responsevalue);

                            responseList = new ArrayList<>();
                            DataPoint[] points = new DataPoint[jsonArray.length()];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONArray childArray = jsonArray.getJSONArray(i);
                                for (int j = 0; j < childArray.length(); j++) {
                                    coinGraph = new CoinGraph(childArray.getLong(0), childArray.getDouble(1), childArray.getDouble(2), childArray.getDouble(3), childArray.getDouble(4), childArray.getDouble(5), childArray.getDouble(6));
                                    responseList.add(coinGraph);
                                    /*Calendar calendar = Calendar.getInstance();
                                    calendar.setTimeInMillis(childArray.getLong(0));
                                    Date d1 = calendar.getTime();
                                    points[i]=new DataPoint(d1,childArray.getLong(2));*/
                                }

                            }
                            txt_open.setText(getResources().getString(R.string.open));
                            txt_high.setText(getResources().getString(R.string.high));
                            txt_low.setText(getResources().getString(R.string.low));
                            txt_close.setText(getResources().getString(R.string.closee));
                            setChart();
                            line_chart.setData(null);
                            candle_chart.setData(null);
                            setChartData(responseList);
                           /* Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(jsonArray.getJSONArray(0).getLong(0));
                            Date d1 = calendar.getTime();
                            Calendar calendar1 = Calendar.getInstance();
                            calendar1.setTimeInMillis(jsonArray.getJSONArray(jsonArray.length()-1).getLong(0));
                            Date d2 = calendar1.getTime();
                            line_chart.getViewport().setMinX(d1.getTime());
                            line_chart.getViewport().setMaxX(d2.getTime());
*/

                        } else {
                            CommonUtilities.ShowToastMessage(CoinInformationActivity.this, responsevalue);
//                            Toast.makeText(getApplicationContext(), responsevalue, Toast.LENGTH_LONG).show();
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + response.message());
                            pb.setVisibility(View.GONE);
                        }

                    } catch (Exception e) {
                        pb.setVisibility(View.GONE);
                        e.printStackTrace();
//                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(CoinInformationActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        pb.setVisibility(View.GONE);
//                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(CoinInformationActivity.this, getResources().getString(R.string.Timeout));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Timeout), Toast.LENGTH_SHORT).show();
                    } else if (t instanceof java.net.ConnectException) {
                        pb.setVisibility(View.GONE);
//                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(CoinInformationActivity.this, getResources().getString(R.string.networkerror));
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.networkerror), Toast.LENGTH_SHORT).show();
                    } else {
                        pb.setVisibility(View.GONE);
//                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(CoinInformationActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception ex) {
            pb.setVisibility(View.GONE);
//            progressDialog.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(CoinInformationActivity.this, getResources().getString(R.string.errortxt));
//            Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
        }
    }

    private void setChart() {


//        candle_chart.setNoDataText(" ");
//        // no description text
//        candle_chart.getDescription().setEnabled(false);
//        candle_chart.setTouchEnabled(true);
//        candle_chart.setDragEnabled(true);
//        candle_chart.setScaleEnabled(true);
//        XAxis xAxisCandle = candle_chart.getXAxis();
//        xAxisCandle.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxisCandle.setDrawGridLines(true);
//        xAxisCandle.enableGridDashedLine(10f, 10f, 0f);
//        xAxisCandle.setDrawGridLines(true);
//        xAxisCandle.setDrawAxisLine(true);
//        xAxisCandle.setLabelCount(5);
//        DateFormat formatterCandle = new SimpleDateFormat("HH:mm");
//        //formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
//        xAxisCandle.setValueFormatter((value, axis) -> {
//            Date date = new Date((long) value);
//            return formatterCandle.format(date);
//        }); // hide text
//        xAxisCandle.setTextSize(11f);
//        xAxisCandle.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
//        xAxisCandle.setGranularity(GRANULARITY);
//        YAxis leftAxisCandle = candle_chart.getAxisLeft();
//        leftAxisCandle.setDrawGridLines(false);
//        leftAxisCandle.enableGridDashedLine(10f, 10f, 0f);
//        leftAxisCandle.setTextSize(11f);
//        leftAxisCandle.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
//        candle_chart.getAxisRight().setEnabled(false);
////        candle_chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
////            @Override
////            public void onValueSelected(Entry e, Highlight h) {
////                Long date = (long) e.getX();
////                Calendar calendar1 = Calendar.getInstance();
////                calendar1.setTimeInMillis(date);
////                Date d2 = calendar1.getTime();
////                SimpleDateFormat curFormaterCandle = new SimpleDateFormat("MM/dd/yyyy HH:mm");
////                String newDateStr = curFormaterCandle.format(d2);
////                CommonUtilities.ShowToastMessage(CoinInformationActivity.this, e.getY() + " USD | " + newDateStr);
////            }
////
////            @Override
////            public void onNothingSelected() {
////
////            }
////        });


        line_chart.setNoDataText(" ");
        // no description text
        line_chart.getDescription().setEnabled(false);

        line_chart.setTouchEnabled(true);

        line_chart.setDragEnabled(true);
        line_chart.setScaleEnabled(true);

        XAxis xAxis = line_chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setDrawGridLines(true);
        xAxis.setDrawAxisLine(true);
        xAxis.setLabelCount(5);
        DateFormat formatter = new SimpleDateFormat("HH:mm");
        //formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        xAxis.setValueFormatter((value, axis) -> {
            Date date = new Date((long) value);
            return formatter.format(date);
        }); // hide text
        xAxis.setTextSize(11f);

        xAxis.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        xAxis.setGranularity(GRANULARITY);

        YAxis leftAxis = line_chart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setTextSize(11f);
        leftAxis.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        line_chart.getAxisRight().setEnabled(false);
        line_chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Long date = (long) e.getX();
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTimeInMillis(date);
                Date d2 = calendar1.getTime();
                SimpleDateFormat curFormater = new SimpleDateFormat("MM/dd/yyyy HH:mm");
                String newDateStr = curFormater.format(d2);
                CommonUtilities.ShowToastMessage(CoinInformationActivity.this, e.getY() + " USD | " + newDateStr);
                lnr_result.setVisibility(View.VISIBLE);
                for (int i = 0; i < responseList.size(); i++) {
                    if (responseList.get(i).getHigh() == e.getY()) {
                        txt_open.setText(getResources().getString(R.string.open) + " $" + responseList.get(i).getOpen());
                        txt_high.setText(getResources().getString(R.string.high) + " $" + responseList.get(i).getHigh());
                        txt_low.setText(getResources().getString(R.string.low) + " $" + responseList.get(i).getLow());
                        txt_close.setText(getResources().getString(R.string.closee) + " $" + responseList.get(i).getClose());
//                        CommonUtilities.ShowToastMessage(CoinInformationActivity.this, e.getY()+ " " + responseList.get(i).getHigh() );
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });

    }

    public void setChartData(ArrayList<CoinGraph> histories) {
        // set data
        Collections.sort(histories);
        ArrayList<CandleEntry> candleValues = new ArrayList<>();
        CandleDataSet candle_set;
        for (CoinGraph history : histories) {
            candleValues.add(new CandleEntry(history.high, history.high, (float) history.low, (float) history.open, (float) history.close));
        }
        if (candle_chart.getData() != null && candle_chart.getData().getDataSetCount() > 0) {
            candle_set = (CandleDataSet) candle_chart.getData().getDataSetByIndex(0);
            candle_set.setValues(candleValues);
//            XAxis xAxis = binding.chart.getXAxis();
//            xAxis.setValueFormatter(new IAxisValueFormatter() {
//                @Override
//                public String getFormattedValue(float value, AxisBase axis) {
//                    return "sjd";
//                }
//            });
            candle_chart.getData().notifyDataChanged();
            candle_chart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            candle_set = new CandleDataSet(candleValues, DATA_SET_1);
            candle_set.setDrawIcons(false);
            candle_set.setColors(ContextCompat.getColor(getApplicationContext(), R.color.brdr_yellow));
//            candle_set.setLineWidth(1f);
//            candle_set.setDrawCircles(false);
            candle_set.setValueTextSize(SIZE);
//            candle_set.setDrawFilled(true);
            candle_set.setDrawValues(false);
//            candle_set.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
            candle_set.setHighlightEnabled(true); // allow highlighting for DataSet
            // set this to false to disable the drawing of highlight indicator (lines)
            candle_set.setDrawHighlightIndicators(true);
            //  candle_set.setHighlightColor(Color.BLACK); // color for highlight indicator
//            candle_set.setDrawHighlightIndicators(false);
            // candle_set.setFillDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.back_chart));
//            candle_set.setFillColor(ContextCompat.getColor(getApplicationContext(), R.color.yellow_trans));
            ArrayList<ICandleDataSet> cdataSets = new ArrayList<>();
            cdataSets.add(candle_set); // add the datasets
            // create a data object with the datasets
            CandleData data = new CandleData(cdataSets);
            // set data
            candle_chart.setData(data);
            candle_chart.getData().setHighlightEnabled(true);
            candle_chart.animateY(DURATION_MILLIS);
            // get the legend (only possible after setting data)
            candle_chart.getLegend().setEnabled(false);
            candle_chart.getData().notifyDataChanged();
            candle_chart.notifyDataSetChanged();
        }


        ArrayList<Entry> line_values = new ArrayList<>();
        LineDataSet line_set;
        for (CoinGraph history : histories) {
            line_values.add(new Entry(history.time.getTime(), history.high));
        }
        if (line_chart.getData() != null && line_chart.getData().getDataSetCount() > 0) {
            line_set = (LineDataSet) line_chart.getData().getDataSetByIndex(0);
            line_set.setValues(line_values);
//            XAxis xAxis = binding.chart.getXAxis();
//            xAxis.setValueFormatter(new IAxisValueFormatter() {
//                @Override
//                public String getFormattedValue(float value, AxisBase axis) {
//                    return "sjd";
//                }
//            });
            line_chart.getData().notifyDataChanged();
            line_chart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            line_set = new LineDataSet(line_values, DATA_SET_1);
            line_set.setDrawIcons(false);
            line_set.setColor(ContextCompat.getColor(getApplicationContext(), R.color.brdr_yellow));
            line_set.setLineWidth(1f);
            line_set.setDrawCircles(false);
            line_set.setValueTextSize(SIZE);
            line_set.setDrawFilled(true);
            line_set.setDrawValues(false);
            line_set.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
            line_set.setHighlightEnabled(true); // allow highlighting for DataSet
            // set this to false to disable the drawing of highlight indicator (lines)
            line_set.setDrawHighlightIndicators(true);
            //  line_set.setHighlightColor(Color.BLACK); // color for highlight indicator
//            line_set.setDrawHighlightIndicators(false);
            // line_set.setFillDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.back_chart));
            line_set.setFillColor(ContextCompat.getColor(getApplicationContext(), R.color.yellow_trans));
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(line_set); // add the datasets
            // create a data object with the datasets
            LineData data = new LineData(dataSets);
            // set data
            line_chart.setData(data);
            line_chart.getData().setHighlightEnabled(true);
            line_chart.animateY(DURATION_MILLIS);
            // get the legend (only possible after setting data)
            line_chart.getLegend().setEnabled(false);
            line_chart.getData().notifyDataChanged();
            line_chart.notifyDataSetChanged();
        }


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        SwitchCases(item);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void SwitchCases(String item) {
        long startTime = System.currentTimeMillis() - (24 * 60 * 60 * 1000);
        long endTime = System.currentTimeMillis();
        switch (item) {
            case "1 Day":
                if (CommonUtilities.isConnectionAvailable(CoinInformationActivity.this)) {
                    invokeCoinGraph(selectedCoin.getStr_coin_code(), "1h", 2000, startTime, endTime);
                    pb.setVisibility(View.VISIBLE);
                } else {
                    pb.setVisibility(View.GONE);
                    CommonUtilities.ShowToastMessage(CoinInformationActivity.this, getResources().getString(R.string.internetconnection));
                }
                break;
            case "7 Days":
                startTime = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000);
                if (CommonUtilities.isConnectionAvailable(CoinInformationActivity.this)) {
                    invokeCoinGraph(selectedCoin.getStr_coin_code(), "1h", 2000, startTime, endTime);
                    pb.setVisibility(View.VISIBLE);
                } else {
                    pb.setVisibility(View.GONE);
                    CommonUtilities.ShowToastMessage(CoinInformationActivity.this, getResources().getString(R.string.internetconnection));
                }
                break;
//            case "1 Month":
//                break;
//            case "6 Months":
//                break;
//            case "1 Year":
//                break;
//            case "All":
//                break;
            default:
//                if (CommonUtilities.isConnectionAvailable(CoinInformationActivity.this)) {
//                    invokeCoinGraph(selectedCoin.getStr_coin_code(), "1h", 800, startTime, endTime);
//                } else {
//                    CommonUtilities.ShowToastMessage(CoinInformationActivity.this, getResources().getString(R.string.internetconnection));
//                }
                break;
        }
    }

    //                getArguments().getInt(MainActivity.ARG_SECTION_NUMBER));
    //        ((MainActivity) activity).onSectionAttached(
    //        super.onAttach(activity);
    //    public void onAttach(Activity activity) {
//    @Override

//    }
    /*  @Override
      public void onResume() {
          super.onResume();
  //        mTimer1 = new Runnable() {
  //            @Override
  //            public void run() {
  //                mSeries1.resetData(generateData());
  //                mHandler.postDelayed(this, 300);
  //            }
  //        };
  //        mHandler.postDelayed(mTimer1, 300);

  //        mTimer2 = new Runnable() {
  //            @Override
  //            public void run() {
  //                line_chart2LastXValue += 1d;
  //                mSeries2.appendData(new DataPoint(line_chart2LastXValue, getRandom()), true, 40);
  //                mHandler.postDelayed(this, 200);
  //            }
  //        };
  //        mHandler.postDelayed(mTimer2, 1000);
      }

      @Override
      public void onPause() {
          mHandler.removeCallbacks(mTimer1);
          mHandler.removeCallbacks(mTimer2);
          super.onPause();
      }

      private DataPoint[] generateData() {
          int count = 30;
          DataPoint[] line_values = new DataPoint[count];
          for (int i=0; i<count; i++) {
              double x = i;
              double f = mRand.nextDouble()*0.15+0.3;
              double y = Math.sin(i*f+2) + mRand.nextDouble()*0.3;
              DataPoint v = new DataPoint(x, y);
              line_values[i] = v;
          }
          return line_values;
      }

      double mLastRandom = 2;
      Random mRand = new Random();
      private double getRandom() {
          return mLastRandom += mRand.nextDouble()*0.5 - 0.25;
      }
  */

}
