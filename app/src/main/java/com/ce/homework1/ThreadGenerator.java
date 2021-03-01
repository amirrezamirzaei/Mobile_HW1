package com.ce.homework1;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ce.homework1.model.Coin;
import com.ce.homework1.model.MessageResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import java.util.logging.LogRecord;

import okhttp3.Response;

public class ThreadGenerator {

    public static Thread getCoins(int start, int limit, Handler handler){
            return new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = Requester.getInstance().RequestCoinName(start, limit);
                try {
                    String coinsListString = response.body().string();
                    Log.d("thread_coin_getter",coinsListString);
                    JSONObject obj = null;
                    try {
                        obj = new JSONObject(coinsListString);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        JSONArray coinsList = obj.getJSONArray("data");
                        ArrayList<Integer> allID = new ArrayList<>();
                        ArrayList<Coin> coins = new ArrayList<>();
                        for(int i=0;i<coinsList.length();i++){
                            JSONObject coinJson = coinsList.getJSONObject(i);
                            int id = coinJson.getInt("id");
                            String name = coinJson.getString("name");
                            String symbol = coinJson.getString("symbol");
                            JSONObject priceInfoUSD = coinJson.getJSONObject("quote").getJSONObject("USD");
                            Double price = priceInfoUSD.getDouble("price");
                            Double percentChangeHour = priceInfoUSD.getDouble("percent_change_1h");
                            Double percentChangeDay = priceInfoUSD.getDouble("percent_change_24h");
                            Double percentChangeWeek = priceInfoUSD.getDouble("percent_change_7d");
                            coins.add(new Coin(symbol,name,id,price,percentChangeHour,percentChangeDay,percentChangeWeek));
                            allID.add(id);
                        }

                        response = Requester.getInstance().RequestCoinsLogo(allID);   // requesting coin logos
                        String allInfo = response.body().string();
                        Log.d("thread_coin_getter",allInfo);
                        try {
                            obj = new JSONObject(allInfo).getJSONObject("data");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("thread_coin_getter",obj.toString());
                        try {
                            Log.d("thread_coin_getter",  obj.getJSONObject("1").getString("logo"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        for (Coin coin : coins) {
                            coin.setImageUrl(obj.getJSONObject(String.valueOf(coin.getId())).getString("logo"));
                        }


                        MainActivity.setCoinsToBeAdded(coins);
                        Log.d("thread_coin_getter","finish");
                        Message message = new Message();
                        message.what = MessageResult.SUCCESSFUL;
                        handler.sendMessage(message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NullPointerException e){
                    System.out.println("no connection");
                }
            }
        });
    }



}
