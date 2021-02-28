package com.ce.homework1;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ce.homework1.model.Coin;

public class CoinActivity extends Activity {
    static Coin coin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin);

        TextView text = findViewById(R.id.test);
        text.setText(coin.toString());
    }

    public static void setCoin(Coin coin) {
        CoinActivity.coin = coin;
    }
}
