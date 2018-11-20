package com.cryptowallet.deviantx.UI.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.ServiceAPIs.CoinGraphApi;
import com.cryptowallet.deviantx.UI.Models.AllCoins;
import com.cryptowallet.deviantx.UI.Models.CoinGraph;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;
import com.cryptowallet.deviantx.Utilities.DeviantXApiClient;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.MPPointF;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.text.DateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cryptowallet.deviantx.Utilities.MyApplication.myApplication;

public class CoinInformationActivity extends AppCompatActivity {


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
    @BindView(R.id.graph)
    LineChart graph;
    public static final int DURATION_MILLIS = 1000;
    public static final float SIZE = 9f;
    public static final String DATA_SET_1 = "DataSet 1";
    public static final float GRANULARITY = 100f;

    AllCoins selectedCoin;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
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

        sharedPreferences = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        coinGraphList = new ArrayList<>();
        stringResponseList = new ArrayList<>();

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

//        if (.contains("-")) {
//            txt_per_change.setTextColor(getResources().getColor(R.color.google_red));
//        } else {
//            txt_per_change.setTextColor(getResources().getColor(R.color.green));
//        }
//        txt_per_change.setText(+"%");
//        txt_per_high.setText("$"+String.format("%.6f", ));
//        txt_per_low.setText("$"+String.format("%.6f", ));

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

        graph.addSeries(series);
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

        graph.addSeries(series);

// set date label formatter
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(CoinInformationActivity.this));
        graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space

// set manual x bounds to have nice steps
        graph.getViewport().setMinX(d1.getTime());
        graph.getViewport().setMaxX(d3.getTime());
        graph.getViewport().setXAxisBoundsManual(true);

// as we use dates as labels, the human rounding to nice readable numbers
// is not necessary
        graph.getGridLabelRenderer().setHumanRounding(false);
*/



/*
        mSeries1 = new LineGraphSeries<>(generateData());
        graph.addSeries(mSeries1);
*/

/*
        mSeries2 = new LineGraphSeries<>();
        graph.addSeries(mSeries2);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(40);
*/


//        GridLabelRenderer gridLabelRenderer = graph.getGridLabelRenderer();
//        GridLabelRenderer.Styles mStyles;
//        GridLabelRenderer.GridStyle mgridStyle;
//        GridLabelRenderer.VerticalLabelsVAlign verticalLabelsVAlign;
//        gridLabelRenderer.setHorizontalLabelsVisible(true);
//        gridLabelRenderer.setVerticalLabelsVisible(true);


        long startTime = System.currentTimeMillis() - (24 * 60 * 60 * 1000);
        long endTime = System.currentTimeMillis();
        if (CommonUtilities.isConnectionAvailable(CoinInformationActivity.this)) {
            invokeCoinGraph(selectedCoin.getStr_coin_code(), "1h", 800, startTime, endTime);
        } else {
            CommonUtilities.ShowToastMessage(CoinInformationActivity.this, getResources().getString(R.string.internetconnection));
        }


    }


//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        ((MainActivity) activity).onSectionAttached(
//                getArguments().getInt(MainActivity.ARG_SECTION_NUMBER));
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
  //                graph2LastXValue += 1d;
  //                mSeries2.appendData(new DataPoint(graph2LastXValue, getRandom()), true, 40);
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
          DataPoint[] values = new DataPoint[count];
          for (int i=0; i<count; i++) {
              double x = i;
              double f = mRand.nextDouble()*0.15+0.3;
              double y = Math.sin(i*f+2) + mRand.nextDouble()*0.3;
              DataPoint v = new DataPoint(x, y);
              values[i] = v;
          }
          return values;
      }

      double mLastRandom = 2;
      Random mRand = new Random();
      private double getRandom() {
          return mLastRandom += mRand.nextDouble()*0.5 - 0.25;
      }

  */
    private void setChart() {

        graph.setNoDataText(" ");
        // no description text
        graph.getDescription().setEnabled(false);

        graph.setTouchEnabled(true);

        graph.setDragEnabled(true);
        graph.setScaleEnabled(true);


        XAxis xAxis = graph.getXAxis();
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

        YAxis leftAxis = graph.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setTextSize(11f);
        leftAxis.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        graph.getAxisRight().setEnabled(false);
        graph.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Long date = (long) e.getX();
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTimeInMillis(date);
                Date d2 = calendar1.getTime();
                SimpleDateFormat curFormater = new SimpleDateFormat("MM/dd/yyyy HH:mm");
                String newDateStr = curFormater.format(d2);
                CommonUtilities.ShowToastMessage(CoinInformationActivity.this, e.getY()+" USD | "+newDateStr);

            }

            @Override
            public void onNothingSelected() {

            }
        });

    }

    private void invokeCoinGraph(final String symbol_coinCodeX, final String intervalX, final int limitX, final long startTimeX, final long endTimeX) {
        try {
            progressDialog = ProgressDialog.show(CoinInformationActivity.this, "", getResources().getString(R.string.please_wait), true);
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
                            progressDialog.dismiss();
                            JSONArray jsonArray = new JSONArray(responsevalue);

                            ArrayList<CoinGraph> responseList = new ArrayList<>();
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
                            setChart();
                            setChartData(responseList);
                           /* Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(jsonArray.getJSONArray(0).getLong(0));
                            Date d1 = calendar.getTime();
                            Calendar calendar1 = Calendar.getInstance();
                            calendar1.setTimeInMillis(jsonArray.getJSONArray(jsonArray.length()-1).getLong(0));
                            Date d2 = calendar1.getTime();
                            graph.getViewport().setMinX(d1.getTime());
                            graph.getViewport().setMaxX(d2.getTime());
*/

                        } else {
                            CommonUtilities.ShowToastMessage(CoinInformationActivity.this, responsevalue);
//                            Toast.makeText(getApplicationContext(), responsevalue, Toast.LENGTH_LONG).show();
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + response.message());
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(CoinInformationActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(CoinInformationActivity.this, getResources().getString(R.string.Timeout));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Timeout), Toast.LENGTH_SHORT).show();
                    } else if (t instanceof java.net.ConnectException) {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(CoinInformationActivity.this, getResources().getString(R.string.networkerror));
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.networkerror), Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(CoinInformationActivity.this, getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception ex) {
            progressDialog.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(CoinInformationActivity.this, getResources().getString(R.string.errortxt));
//            Toast.makeText(getApplicationContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
        }
    }

    public void setChartData(ArrayList<CoinGraph> histories) {
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
//            XAxis xAxis = binding.chart.getXAxis();
//            xAxis.setValueFormatter(new IAxisValueFormatter() {
//                @Override
//                public String getFormattedValue(float value, AxisBase axis) {
//                    return "sjd";
//                }
//            });
            graph.getData().notifyDataChanged();
            graph.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, DATA_SET_1);

            set1.setDrawIcons(false);
            set1.setColor(ContextCompat.getColor(getApplicationContext(), R.color.brdr_yellow));
            set1.setLineWidth(1f);
            set1.setDrawCircles(false);
            set1.setValueTextSize(SIZE);
            set1.setDrawFilled(true);
            set1.setDrawValues(false);
            set1.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);

            set1.setHighlightEnabled(true); // allow highlighting for DataSet

            // set this to false to disable the drawing of highlight indicator (lines)
            set1.setDrawHighlightIndicators(true);
            //  set1.setHighlightColor(Color.BLACK); // color for highlight indicator
//            set1.setDrawHighlightIndicators(false);

            // set1.setFillDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.back_chart));
            set1.setFillColor(ContextCompat.getColor(getApplicationContext(), R.color.yellow_trans));

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            LineData data = new LineData(dataSets);

            // set data
            graph.setData(data);
            graph.getData().setHighlightEnabled(true);

            graph.animateY(DURATION_MILLIS);

            // get the legend (only possible after setting data)
            graph.getLegend().setEnabled(false);
        }
    }


}
