package com.cryptowallet.deviantx.UI.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.ServiceAPIs.CoinGraphApi;
import com.cryptowallet.deviantx.ServiceAPIs.CoinsControllerApi;
import com.cryptowallet.deviantx.UI.Adapters.SpinnerDaysAdapter;
import com.cryptowallet.deviantx.UI.Models.AccountWallet;
import com.cryptowallet.deviantx.UI.Models.AllCoins;
import com.cryptowallet.deviantx.UI.Models.CandleChartData;
import com.cryptowallet.deviantx.UI.Models.CoinGraph;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;
import com.cryptowallet.deviantx.Utilities.DeviantXApiClient;
import com.cryptowallet.trendchart.DateValue;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.jjoe64.graphview.series.DataPoint;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    @BindView(R.id.txt_time)
    TextView txt_time;
    @BindView(R.id.txt_date)
    TextView txt_date;


    ArrayList<CoinGraph> responseList;
    ArrayList<CandleChartData> responseCList;
    public static final int DURATION_MILLIS = 1000;
    public static final float SIZE = 9f;
    public static final String DATA_SET_1 = "DataSet 1";
    public static final float GRANULARITY = 100f;

    String chart_data, data, candle_chart_data;
    /*AllCoinsDB*/ AccountWallet selectedCoin;
    AllCoins selectedCoin1;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;

    ArrayList<CoinGraph> coinGraphList;
    List<String> stringResponseList;
    CoinGraph coinGraph;
    CandleChartData candleChartData;


    String respData, respStatus, respMsg;
//    boolean  = false;

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

        sharedPreferences = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        coinGraphList = new ArrayList<>();
        stringResponseList = new ArrayList<>();
        responseList = new ArrayList<>();
        responseCList = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        boolean isExpCoin = bundle.getBoolean(CONSTANTS.isExploreCoins);
        if (isExpCoin) {
            selectedCoin1 = bundle.getParcelable(CONSTANTS.selectedCoin);
            Picasso.with(CoinInformationActivity.this).load(selectedCoin1.getStr_coin_logo()).into(img_coin_logo);
            txt_coin_name.setText(selectedCoin1.getStr_coin_name());
            txt_coin_usd.setText("$" + String.format("%.4f", selectedCoin1.getDbl_coin_usdValue()));

            DecimalFormat rank = new DecimalFormat("00.00");
            if (selectedCoin1.getDbl_coin_24h() < 0) {
                txt_per_change.setText(rank.format(selectedCoin1.getDbl_coin_24h()) + "%");
                txt_per_change.setTextColor(getResources().getColor(R.color.google_red));
            } /*else if (responseList.get(responseList.size() - 1).getChange() == 0) {
                            }*/ else {
                txt_per_change.setText("+" + rank.format(selectedCoin1.getDbl_coin_24h()) + "%");
                txt_per_change.setTextColor(getResources().getColor(R.color.green));
            }

            if (CommonUtilities.isConnectionAvailable(CoinInformationActivity.this)) {
                getCoinChartData1(selectedCoin1);
            } else {
                CommonUtilities.ShowToastMessage(CoinInformationActivity.this, getResources().getString(R.string.internetconnection));
            }
        } else {
            selectedCoin = bundle.getParcelable(CONSTANTS.selectedCoin);
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
            if (CommonUtilities.isConnectionAvailable(CoinInformationActivity.this)) {
                getCoinChartData(selectedCoin);
            } else {
                CommonUtilities.ShowToastMessage(CoinInformationActivity.this, getResources().getString(R.string.internetconnection));
            }
        }

        toolbar_center_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        candle_chart.getDescription().setEnabled(false);
        // scaling can now only be done on x- and y-axis separately
        candle_chart.setPinchZoom(false);
        candle_chart.setDrawGridBackground(false);

      /*  Picasso.with(CoinInformationActivity.this).load(selectedCoin.getStr_coin_logo()).into(img_coin_logo);
        txt_coin_name.setText(selectedCoin.getStr_coin_name());
        txt_coin_usd.setText("$" + String.format("%.4f", selectedCoin.getDbl_coin_usdValue()));

        DecimalFormat rank = new DecimalFormat("00.00");
        if (selectedCoin.getDbl_coin_24h() < 0) {
            txt_per_change.setText(rank.format(selectedCoin.getDbl_coin_24h()) + "%");
            txt_per_change.setTextColor(getResources().getColor(R.color.google_red));
        } *//*else if (responseList.get(responseList.size() - 1).getChange() == 0) {
                            }*//* else {
            txt_per_change.setText("+" + rank.format(selectedCoin.getDbl_coin_24h()) + "%");
            txt_per_change.setTextColor(getResources().getColor(R.color.green));
        }*/

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
                txt_open.setText(getResources().getString(R.string.open) + "$00.00");
                txt_high.setText(getResources().getString(R.string.high) + "$00.00");
                txt_low.setText(getResources().getString(R.string.low) + "$00.00");
                txt_close.setText(getResources().getString(R.string.closee) + "$00.00");
                txt_date.setText(getResources().getString(R.string.date) + "dd/MM/yyyy");
                txt_time.setText(getResources().getString(R.string.time) + "hh:mm");

                lnr_result.setVisibility(View.VISIBLE);
                lnr_candle_graph.setBackground(getResources().getDrawable(R.drawable.rec_brinjal_gradient_c2));
                lnr_line_graph.setBackground(getResources().getDrawable(R.drawable.rec_grey_trans_c2));
                line_chart.setVisibility(View.GONE);
                candle_chart.setVisibility(View.VISIBLE);
//                SwitchCases(spnr_days.getSelectedItem().toString());
            }
        });

        lnr_line_graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_open.setText(getResources().getString(R.string.open) + "$00.00");
                txt_high.setText(getResources().getString(R.string.high) + "$00.00");
                txt_low.setText(getResources().getString(R.string.low) + "$00.00");
                txt_close.setText(getResources().getString(R.string.closee) + "$00.00");
                txt_date.setText(getResources().getString(R.string.date) + "dd/MM/yyyy");
                txt_time.setText(getResources().getString(R.string.time) + "hh:mm");

                lnr_result.setVisibility(View.VISIBLE);
//                CommonUtilities.ShowToastMessage(CoinInformationActivity.this, spnr_days.getSelectedItem().toString());
                lnr_line_graph.setBackground(getResources().getDrawable(R.drawable.rec_brinjal_gradient_c2));
                lnr_candle_graph.setBackground(getResources().getDrawable(R.drawable.rec_grey_trans_c2));
                line_chart.setVisibility(View.VISIBLE);
                candle_chart.setVisibility(View.GONE);
//                SwitchCases(spnr_days.getSelectedItem().toString());
            }
        });
        txt_open.setText(getResources().getString(R.string.open) + "$00.00");
        txt_high.setText(getResources().getString(R.string.high) + "$00.00");
        txt_low.setText(getResources().getString(R.string.low) + "$00.00");
        txt_close.setText(getResources().getString(R.string.closee) + "$00.00");
        txt_date.setText(getResources().getString(R.string.date) + "dd/MM/yyyy");
        txt_time.setText(getResources().getString(R.string.time) + "hh:mm");


    }

    private void getCoinChartData(AccountWallet selectedCoin) {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            progressDialog = ProgressDialog.show(CoinInformationActivity.this, "", getResources().getString(R.string.please_wait), true);
            CoinsControllerApi apiService = DeviantXApiClient.getClient().create(CoinsControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.getCoinChartData(CONSTANTS.DeviantMulti + token, selectedCoin.getStr_coin_code());
            apiResponse.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responsevalue = response.body().string();

                        if (!responsevalue.isEmpty() && responsevalue != null) {

                            progressDialog.dismiss();
                            pb.setVisibility(View.GONE);
                            JSONObject jsonObject = new JSONObject(responsevalue);
                            respStatus = jsonObject.getString("status");
                            respMsg = jsonObject.getString("msg");

                            if (respStatus.equals("true")) {
                                try {
                                    respData = jsonObject.getString("data");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                setCoinLineChartData(respData);

                            } else {
                                CommonUtilities.ShowToastMessage(CoinInformationActivity.this, getResources().getString(R.string.empty_data));
                            }

                        } else {
                            CommonUtilities.ShowToastMessage(CoinInformationActivity.this, getResources().getString(R.string.empty_data));
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

    private void getCoinChartData1(AllCoins selectedCoin) {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            progressDialog = ProgressDialog.show(CoinInformationActivity.this, "", getResources().getString(R.string.please_wait), true);
            CoinsControllerApi apiService = DeviantXApiClient.getClient().create(CoinsControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.getCoinChartData(CONSTANTS.DeviantMulti + token, selectedCoin.getStr_coin_code());
            apiResponse.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responsevalue = response.body().string();

                        if (!responsevalue.isEmpty() && responsevalue != null) {

                            progressDialog.dismiss();
                            pb.setVisibility(View.GONE);
                            JSONObject jsonObject = new JSONObject(responsevalue);
                            respStatus = jsonObject.getString("status");
                            respMsg = jsonObject.getString("msg");

                            if (respStatus.equals("true")) {
                                try {
                                    respData = jsonObject.getString("data");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                setCoinLineChartData(respData);

                            } else {
                                CommonUtilities.ShowToastMessage(CoinInformationActivity.this, getResources().getString(R.string.empty_data));
                            }

                        } else {
                            CommonUtilities.ShowToastMessage(CoinInformationActivity.this, getResources().getString(R.string.empty_data));
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + response.message());
                            pb.setVisibility(View.GONE);
                        }

                    } catch (Exception e) {
                        pb.setVisibility(View.GONE);
                        e.printStackTrace();
                        CommonUtilities.ShowToastMessage(CoinInformationActivity.this, getResources().getString(R.string.errortxt));
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        pb.setVisibility(View.GONE);
                        CommonUtilities.ShowToastMessage(CoinInformationActivity.this, getResources().getString(R.string.Timeout));
                    } else if (t instanceof java.net.ConnectException) {
                        pb.setVisibility(View.GONE);
                        CommonUtilities.ShowToastMessage(CoinInformationActivity.this, getResources().getString(R.string.networkerror));
                    } else {
                        pb.setVisibility(View.GONE);
                        CommonUtilities.ShowToastMessage(CoinInformationActivity.this, getResources().getString(R.string.errortxt));
                    }
                }
            });
        } catch (Exception ex) {
            pb.setVisibility(View.GONE);
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(CoinInformationActivity.this, getResources().getString(R.string.errortxt));
        }
    }

    private void setCoinLineChartData(String respData) {
        try {
            chart_data = respData;
            JSONObject jsonObject = new JSONObject(chart_data);
            try {
                data = jsonObject.getString("Data");
            } catch (Exception e) {
                e.printStackTrace();
            }
            JSONArray jsonArray = new JSONArray(data);
            List<DateValue> responseList2 = new ArrayList<>();
            Double hisghValue = 0.0;
            DataPoint[] points = new DataPoint[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject childobject = jsonArray.getJSONObject(i);
                coinGraph = new CoinGraph(childobject.getLong("time"), childobject.getDouble("close"), childobject.getDouble("high"), childobject.getDouble("low"), childobject.getDouble("open"), childobject.getDouble("volumefrom"), childobject.getDouble("volumeto"));
                if (hisghValue < childobject.getDouble("high"))
                    hisghValue = childobject.getDouble("high");
                responseList.add(coinGraph);
                responseList2.add(new DateValue(childobject.getDouble("high"), childobject.getLong("time")));

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(childobject.getLong("time"));
                Date d1 = calendar.getTime();
                points[i] = new DataPoint(d1, childobject.getLong("high"));
            }
            setLineChart();
            line_chart.setData(null);
            setLineChartData(responseList);
            txt_per_high.setText("$" + String.format("%.4f", responseList.get(responseList.size() - 1).getHigh()));
            txt_per_low.setText("$" + String.format("%.4f", responseList.get(responseList.size() - 1).getLow()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setLineChart() {
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
            Date date = new Date((long) value * 1000);
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
                Long date = (long) e.getX() * 1000;

                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTimeInMillis(date);
                Date d2 = calendar1.getTime();
                SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                SimpleDateFormat dateFormater = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat timeFormater = new SimpleDateFormat("HH:mm");
                String newDateStr = curFormater.format(d2);
                String dateStr = dateFormater.format(d2);
                String timeStr = timeFormater.format(d2);
                txt_date.setText(getResources().getString(R.string.date) + " " + dateStr);
                txt_time.setText(getResources().getString(R.string.time) + " " + timeStr);
                lnr_result.setVisibility(View.VISIBLE);
                for (int i = 0; i < responseList.size(); i++) {
                    if (responseList.get(i).getHigh() == e.getY()) {
                        txt_open.setText(getResources().getString(R.string.open) + " $" + String.format("%.4f", responseList.get(i).getOpen()));
                        txt_high.setText(getResources().getString(R.string.high) + " $" + String.format("%.4f", responseList.get(i).getHigh()));
                        txt_low.setText(getResources().getString(R.string.low) + " $" + String.format("%.4f", responseList.get(i).getLow()));
                        txt_close.setText(getResources().getString(R.string.closee) + " $" + String.format("%.4f", responseList.get(i).getClose()));
//                        CommonUtilities.ShowToastMessage(CoinInformationActivity.this, e.getY()+ " " + responseList.get(i).getHigh() );
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });


//        CANDLE CHART OLD DATA
        candle_chart.setHighlightPerDragEnabled(true);
        candle_chart.setDrawBorders(true);
        candle_chart.setBorderColor(getResources().getColor(R.color.green));

        YAxis yAxis = candle_chart.getAxisLeft();
        YAxis rightAxis = candle_chart.getAxisRight();
        yAxis.setDrawGridLines(false);
        rightAxis.setDrawGridLines(false);
        candle_chart.requestDisallowInterceptTouchEvent(true);

        XAxis xAxiss = candle_chart.getXAxis();

        xAxiss.setDrawGridLines(false);// disable x axis grid lines
        xAxiss.setDrawLabels(true);
        xAxiss.setTextColor(Color.WHITE);
        rightAxis.setTextColor(Color.WHITE);
        yAxis.setDrawLabels(false);
        xAxiss.setGranularityEnabled(true);
        xAxiss.setAvoidFirstLastClipping(true);
//        DateFormat formatter = new SimpleDateFormat("HH:mm");
        xAxiss.setValueFormatter((value, axis) -> {
            Date date = new Date((long) value * 1000);
            return formatter.format(date);
        }); // hide text
        xAxis.setTextSize(11f);
        Legend l = candle_chart.getLegend();
        l.setEnabled(false);


        candle_chart.setTouchEnabled(true);
        candle_chart.setDragEnabled(true);
        candle_chart.setScaleEnabled(true);
        candle_chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
               /* Long date = (long) e.getX() * 1000;

                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTimeInMillis(date);
                Date d2 = calendar1.getTime();
                SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                SimpleDateFormat dateFormater = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat timeFormater = new SimpleDateFormat("HH:mm");
                String newDateStr = curFormater.format(d2);
                String dateStr = dateFormater.format(d2);
                String timeStr = timeFormater.format(d2);
                txt_date.setText(getResources().getString(R.string.date) + " " + dateStr);
                txt_time.setText(getResources().getString(R.string.time) + " " + timeStr);
                lnr_result.setVisibility(View.VISIBLE);
                for (int i = 0; i < responseList.size(); i++) {
                    if (responseList.get(i).getHigh()==e.getY()){
                        txt_open.setText(getResources().getString(R.string.open) + " $" + String.format("%.4f", responseList.get(i).getOpen()));
                        txt_high.setText(getResources().getString(R.string.high) + " $" + String.format("%.4f", responseList.get(i).getHigh()));
                        txt_low.setText(getResources().getString(R.string.low) + " $" + String.format("%.4f", responseList.get(i).getLow()));
                        txt_close.setText(getResources().getString(R.string.closee) + " $" + String.format("%.4f", responseList.get(i).getChange()));
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected() {
//                lnr_result.setVisibility(View.INVISIBLE);
            }
        });*/
                Long date = (long) e.getX() * 1000;

                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTimeInMillis(date);
                Date d2 = calendar1.getTime();
                SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                SimpleDateFormat dateFormater = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat timeFormater = new SimpleDateFormat("HH:mm");
                String newDateStr = curFormater.format(d2);
                String dateStr = dateFormater.format(d2);
                String timeStr = timeFormater.format(d2);
                txt_date.setText(getResources().getString(R.string.date) + " " + dateStr);
                txt_time.setText(getResources().getString(R.string.time) + " " + timeStr);
                lnr_result.setVisibility(View.VISIBLE);
                for (int i = 0; i < responseList.size(); i++) {
                    if (responseList.get(i).getHigh() == e.getY() || responseList.get(i).getOpen() == e.getY() || responseList.get(i).getLow() == e.getY() || responseList.get(i).getClose() == e.getY()) {
                        txt_open.setText(getResources().getString(R.string.open) + " $" + String.format("%.4f", responseList.get(i).getOpen()));
                        txt_high.setText(getResources().getString(R.string.high) + " $" + String.format("%.4f", responseList.get(i).getHigh()));
                        txt_low.setText(getResources().getString(R.string.low) + " $" + String.format("%.4f", responseList.get(i).getLow()));
                        txt_close.setText(getResources().getString(R.string.closee) + " $" + String.format("%.4f", responseList.get(i).getClose()));
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

    public void setLineChartData(ArrayList<CoinGraph> histories) {
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


        ArrayList<CandleEntry> yValsCandleStick = new ArrayList<CandleEntry>();
        for (int i = 0; i < histories.size(); i++) {
            yValsCandleStick.add(new CandleEntry(i, histories.get(i).high, (float) histories.get(i).low, (float) histories.get(i).open, (float) histories.get(i).close));
        }

        CandleDataSet set1;
        if (candle_chart.getData() != null && candle_chart.getData().getDataSetCount() > 0) {
            set1 = (CandleDataSet) candle_chart.getData().getDataSetByIndex(0);
            set1.setValues(yValsCandleStick);

            candle_chart.getData().notifyDataChanged();
            candle_chart.notifyDataSetChanged();
        } else {
            set1 = new CandleDataSet(yValsCandleStick, "DataSet 1");
            set1.setColor(Color.rgb(80, 80, 80));
            set1.setShadowColor(getResources().getColor(R.color.yellow));
            set1.setShadowWidth(0.8f);
            set1.setDecreasingColor(getResources().getColor(R.color.google_red));
            set1.setDecreasingPaintStyle(Paint.Style.FILL);
            set1.setIncreasingColor(getResources().getColor(R.color.graph_brdr_green));
            set1.setIncreasingPaintStyle(Paint.Style.FILL);
            set1.setNeutralColor(Color.LTGRAY);
            set1.setDrawValues(false);
// create a data object with the datasets
            CandleData data = new CandleData(set1);
// set data
            candle_chart.setData(data);
            candle_chart.invalidate();
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

    private void invokeCoinGraph(final String symbol_coinCodeX, final String intervalX, final int limitX, final long startTimeX, final long endTimeX) {
        try {
            CoinGraphApi apiService = DeviantXApiClient.getCoinGraph().create(CoinGraphApi.class);
            Call<ResponseBody> apiResponse = apiService.getCoinGraph(symbol_coinCodeX, "USD", 1000);
            Log.i("API:\t:", apiResponse.toString());
            apiResponse.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responsevalue = response.body().string();

                        if (!responsevalue.isEmpty() && responsevalue != null && !responsevalue.contains("code")) {
                            pb.setVisibility(View.GONE);
                            JSONArray jsonArray = new JSONArray(responsevalue);

                            responseList = new ArrayList<>();
                            DataPoint[] points = new DataPoint[jsonArray.length()];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONArray childArray = jsonArray.getJSONArray(i);
                                for (int j = 0; j < childArray.length(); j++) {
                                    coinGraph = new CoinGraph(childArray.getLong(0), childArray.getDouble(1), childArray.getDouble(2), childArray.getDouble(3), childArray.getDouble(4), childArray.getDouble(5), childArray.getDouble(6));
                                    responseList.add(coinGraph);
                                }

                            }
                            txt_open.setText(getResources().getString(R.string.open) + "$00.00");
                            txt_high.setText(getResources().getString(R.string.high) + "$000.00");
                            txt_low.setText(getResources().getString(R.string.low) + "$00.00");
                            txt_close.setText(getResources().getString(R.string.closee) + "$00.00");
                            txt_date.setText(getResources().getString(R.string.date) + "dd/MM/yyyy");
                            txt_time.setText(getResources().getString(R.string.time) + "hh:mm");

                            setLineChart();
                            line_chart.setData(null);
                            candle_chart.setData(null);
                            setLineChartData(responseList);

                        } else {
                            CommonUtilities.ShowToastMessage(CoinInformationActivity.this, responsevalue);
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + response.message());
                            pb.setVisibility(View.GONE);
                        }

                    } catch (Exception e) {
                        pb.setVisibility(View.GONE);
                        e.printStackTrace();
                        CommonUtilities.ShowToastMessage(CoinInformationActivity.this, getResources().getString(R.string.errortxt));
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        pb.setVisibility(View.GONE);
                        CommonUtilities.ShowToastMessage(CoinInformationActivity.this, getResources().getString(R.string.Timeout));
                    } else if (t instanceof java.net.ConnectException) {
                        pb.setVisibility(View.GONE);
                        CommonUtilities.ShowToastMessage(CoinInformationActivity.this, getResources().getString(R.string.networkerror));
                    } else {
                        pb.setVisibility(View.GONE);
                        CommonUtilities.ShowToastMessage(CoinInformationActivity.this, getResources().getString(R.string.errortxt));
                    }
                }
            });
        } catch (Exception ex) {
            pb.setVisibility(View.GONE);
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(CoinInformationActivity.this, getResources().getString(R.string.errortxt));
        }
    }

}
