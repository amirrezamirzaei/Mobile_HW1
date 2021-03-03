package com.ce.homework1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ce.homework1.model.Coin;
import com.ce.homework1.model.MessageResult;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainActivity extends Activity {
    private static int coinsLoaded = 1;
    private static int loadLimit = 5;
    private static boolean canUpdate = false;

    private static ArrayList<Coin> coinsToBeAdded;

    private Handler handler;
    private LinearLayout mainLayout;
    private Button updateButton;
    private ProgressBar progressBar;


    static final int DEFAULT_THREAD_POOL_SIZE = 5;
    static ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainLayout = findViewById(R.id.mainLayout);
        updateButton = findViewById(R.id.updateButton);
        progressBar = findViewById(R.id.progressBar);
        executorService = Executors.newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE);
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MessageResult.SUCCESSFUL) {
                    progressBar.setProgress(progressBar.getProgress() + loadLimit);
                    addCoinToView();
                } else {

                }
            }
        };
        if (isNetworkAvailable()) {   // connected to internet
            makeViewIntoLoading();
            getCoins();
        } else {
            Toast.makeText(this, "Error: you aren't connected to internet", Toast.LENGTH_SHORT).show();
            /*TODO update coins from database you only need to get coin from database and call setcoinstobeadded*/
        }
    }

    public void addCoinToView() {
        mainLayout.post(new Runnable() {
            @Override
            public void run() {
                for (Coin coin : coinsToBeAdded) {
                    View layoutToBeAdded = LayoutInflater.from(MainActivity.this).inflate(R.layout.fragment_coin, mainLayout, false);
                    RelativeLayout relativeLayout = (RelativeLayout) ((LinearLayout) layoutToBeAdded.getRootView()).getChildAt(0);
                    ImageView logo = (ImageView) relativeLayout.getChildAt(0);
                    TextView name = (TextView) relativeLayout.getChildAt(1);
                    TextView price = (TextView) relativeLayout.getChildAt(2);
                    TextView percentDayHour = (TextView) relativeLayout.getChildAt(3);
                    TextView percentWeek = (TextView) relativeLayout.getChildAt(4);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(getBaseContext()).load(coin.getImageUrl()).placeholder(R.drawable.loading).error(R.drawable.no_connection).into(logo);
                        }
                    });
                    price.setText(String.format("%.03f", coin.getPrice()) + "$");
                    name.setText(coin.getName() + " | " + coin.getSymbol());
                    percentDayHour.setText("1h:" + String.format("%.02f", coin.getPercentChangeHour()) + "%  " + "1D:" + String.format("%.02f", coin.getPercentChangeDay()) + "%");
                    percentWeek.setText("7D:" + String.format("%.02f", coin.getPercentChangeWeek()) + "%");
                    layoutToBeAdded.getRootView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            CoinActivity.setCoin(coin);
                            Intent i = new Intent(MainActivity.this, CoinActivity.class);
                            startActivity(i);
                        }
                    });
                    mainLayout.addView(layoutToBeAdded);
                }
                coinsLoaded += loadLimit;
                canUpdate = true;
                setCoinsToBeAdded(null);
                makeViewIntoNotLoading();
            }
        });

    }

    public void getCoins() {
        executorService.submit( ThreadGenerator.getCoins(coinsLoaded, loadLimit, handler));
    }

    public static void setCoinsToBeAdded(ArrayList<Coin> coinsToBeAdded) {
        MainActivity.coinsToBeAdded = coinsToBeAdded;
    }

    public void update(View view) {
        if (canUpdate && isNetworkAvailable()) {
            canUpdate = false;
            makeViewIntoLoading();
            getCoins();
        } else if (isNetworkAvailable() == false) {
            Toast.makeText(this, "Error: you aren't connected to internet", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error: please wait before requesting updates", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void makeViewIntoLoading() {
        updateButton.setBackgroundColor(getResources().getColor(R.color.light_red));
        progressBar.setVisibility(View.VISIBLE);
    }

    private void makeViewIntoNotLoading() {
        updateButton.setBackgroundColor(getResources().getColor(R.color.light_green));
        progressBar.setVisibility(View.INVISIBLE);
    }

}