package com.ce.homework1;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;



import com.ce.homework1.model.Candle;
import com.ce.homework1.model.Coin;
import com.ce.homework1.model.MessageResult;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class CoinActivity extends Activity {
    static Coin coin;
    private Handler handler;
    private static ArrayList<Candle> candlesToBeAdded;
    private static ArrayList<CandleEntry> candleEntries;
    private static boolean canUpdate = false;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin);

        TextView title = findViewById(R.id.coinName);
        title.setText(coin.getName());

        progressBar = findViewById(R.id.progressBar);

        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MessageResult.SUCCESSFUL) {
                    Log.d("done!", "correct");
                    updateUI();
                } else {
                    Toast.makeText(getApplicationContext(),"Error: you aren't connected to internet",Toast.LENGTH_SHORT).show();
                }
            }
        };

        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        String date = df.format(new Date());

        getCoinDetail(coin, date, Range.weekly);

        Button weekBtn = findViewById(R.id.weekButton);
        weekBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update(date, Range.weekly);
            }
        });

        Button monthBtn = findViewById(R.id.monthButton);
        monthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("month clicked!");
                update(date, Range.oneMonth);
            }
        });
    }

    public void update(String date, Range range) {
        if(canUpdate && isNetworkAvailable()){
            canUpdate = false;
            progressBar.setVisibility(View.VISIBLE);
            getCoinDetail(coin, date, range);
        }else if(isNetworkAvailable()==false){
            Toast.makeText(this,"Error: you aren't connected to internet",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"Error: please wait before requesting updates",Toast.LENGTH_SHORT).show();
        }
    }

    public void getCoinDetail(Coin coin, String date, Range range) {
        MainActivity.executorService.submit(ThreadGenerator.getCoinDetail(coin, range, date, handler));
    }

    public static void setCoin(Coin coin) {
        CoinActivity.coin = coin;
    }

    public static void setCandlesToBeAdded(ArrayList<Candle> candlesToBeAdded, ArrayList<CandleEntry> candleEntries) {
        CoinActivity.candlesToBeAdded = candlesToBeAdded;
        CoinActivity.candleEntries = candleEntries;
    }


    public enum Range {
        weekly,
        oneMonth,
    }

    public void updateUI() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                CandleStickChart candleStickChart = findViewById(R.id.candle_stick_chart);
                candleStickChart.setHighlightPerDragEnabled(true);

                candleStickChart.setDrawBorders(true);

                candleStickChart.setBorderColor(getResources().getColor(R.color.candle_orange));

                YAxis yAxis = candleStickChart.getAxisLeft();
                YAxis rightAxis = candleStickChart.getAxisRight();
                yAxis.setDrawGridLines(false);
                rightAxis.setDrawGridLines(false);
                candleStickChart.requestDisallowInterceptTouchEvent(true);

                XAxis xAxis = candleStickChart.getXAxis();

                candleStickChart.setContentDescription("Coin");
                XAxis bottomAxis = candleStickChart.getXAxis();
                bottomAxis.setDrawGridLines(false);
                bottomAxis.setTextColor(getResources().getColor(R.color.candle_orange));

                xAxis.setDrawGridLines(false);// disable x axis grid lines
                xAxis.setDrawLabels(false);
                rightAxis.setTextColor(getResources().getColor(R.color.candle_orange));
                yAxis.setDrawLabels(false);
                xAxis.setGranularity(1f);
                xAxis.setGranularityEnabled(true);
                xAxis.setAvoidFirstLastClipping(true);

                Legend l = candleStickChart.getLegend();
                l.setEnabled(false);


                CandleDataSet set1 = new CandleDataSet(candleEntries, "DataSet 1");
                set1.setColor(Color.rgb(80, 80, 80));
                set1.setShadowColor(getResources().getColor(R.color.candle_blue));
                set1.setShadowWidth(0.8f);
                set1.setDecreasingColor(getResources().getColor(R.color.candle_red));
                set1.setDecreasingPaintStyle(Paint.Style.FILL);
                set1.setIncreasingColor(getResources().getColor(R.color.candle_green));
                set1.setIncreasingPaintStyle(Paint.Style.FILL);
                set1.setNeutralColor(Color.LTGRAY);
                set1.setDrawValues(false);

                // create a data object with the datasets
                CandleData data = new CandleData(set1);

                // set data
                candleStickChart.setData(data);
                candleStickChart.invalidate();

                canUpdate = true;
                progressBar.setVisibility(View.INVISIBLE);
            }
        }).start();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
