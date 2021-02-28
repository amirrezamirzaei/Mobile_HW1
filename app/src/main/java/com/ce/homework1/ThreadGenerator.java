package com.ce.homework1;

import android.util.Log;

import com.ce.homework1.model.Coin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Response;

public class ThreadGenerator {

    public static Thread getCoins(int start, int limit){
            return new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = Requester.getInstance().RequestCoinName(start, limit);
                try {
                    String coinsListString = response.body().string();
                    Log.d("kurogiri",coinsListString);
                    JSONObject obj = null;
                    try {
                        obj = new JSONObject(coinsListString);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        JSONArray coinsList = obj.getJSONArray("data");
                        ArrayList<Coin> coins = new ArrayList<>();
                        Log.d("kurogiri", String.valueOf(coinsList.length()));
                        Log.d("kurogiri1", String.valueOf(coinsList.get(0)));
                        Log.d("kurogiri2", String.valueOf(coinsList.get(1)));

                        for(int i=0;i<coinsList.length();i++){
                            JSONObject coinJson = coinsList.getJSONObject(i);
                            int id = coinJson.getInt("id");
                            String name = coinJson.getString("name");
                            String symbol = coinJson.getString("symbol");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
