package com.ce.homework1;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ce.homework1.model.Candle;
import com.ce.homework1.model.Coin;
import com.ce.homework1.model.MessageResult;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class CoinActivity extends Activity {
    static Coin coin;
    private Handler handler;
    private static ArrayList<Candle> candlesToBeAdded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin);

        TextView text = findViewById(R.id.test);
        text.setText(coin.toString());

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MessageResult.SUCCESSFUL) {
                    Log.d("mido", "correct");
//                    addCoinToView();
                } else {

                }
            }
        };

        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        String date = df.format(new Date());

        getCoinDetail(coin, date);
    }

    public void getCoinDetail(Coin coin, String date) {
        ThreadGenerator.getCoinDetail(coin, Range.weekly, date, handler).start();
    }

    public static void setCoin(Coin coin) {
        CoinActivity.coin = coin;
    }

    public static void setCandlesToBeAdded(ArrayList<Candle> candlesToBeAdded) {
        CoinActivity.candlesToBeAdded = candlesToBeAdded;
    }


    public enum Range {
        weekly,
        oneMonth,
    }

}
