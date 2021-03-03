package com.ce.homework1;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ce.homework1.model.Candle;
import com.ce.homework1.model.Coin;
import com.ce.homework1.model.MessageResult;
import com.github.mikephil.charting.data.CandleEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


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
                        obj = new JSONObject(coinsListString);
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
                        obj = new JSONObject(allInfo).getJSONObject("data");
                        Log.d("thread_coin_getter",obj.toString());
                        for (Coin coin : coins) {
                            coin.setImageUrl(obj.getJSONObject(String.valueOf(coin.getId())).getString("logo"));
                        }
                        MainActivity.setCoinsToBeAdded(coins);
                        Log.d("thread_coin_getter","finish");
                        Message message = new Message();
                        message.what = MessageResult.SUCCESSFUL;
                        handler.sendMessage(message);
                    } catch (JSONException e) {
                        Log.e("Json","response isn't valid, probably because free api calls are finished for today");
                        e.printStackTrace();
                    } catch (IOException e) {
                    Log.e("internet","response time out");
                    e.printStackTrace();
                } catch (NullPointerException e){
                    Message message = new Message();
                    message.what = MessageResult.FAILED;
                    handler.sendMessage(message);
                }
            }
        });
    }

    public static Thread getCoinDetail(Coin coin, CoinActivity.Range range, String startDate, Handler handler){
        return new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("ENTER", "getCoinDetail");
                Response response = Requester.getInstance().RequestCoinDetail(coin, range, startDate);
                System.out.println(response);
                try {
                    String coinsListString = response.body().string();
                    Log.d("COIN_DETAIL_BODY",coinsListString);
//                    JSONObject obj = null;
                    JSONArray candlesList = null;
                    try {
                        candlesList = new JSONArray(coinsListString);
                        Log.d("jsonArray", candlesList.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("exceptCandle", candlesList.toString());
                    }
                    try {
//                        JSONArray candlesList = obj.getJSONArray("");
                        ArrayList<Candle> candles = new ArrayList<>();
                        ArrayList<CandleEntry> candleEntry = new ArrayList<>();
                        for(int i=0;i<candlesList.length();i++){
                            JSONObject coinJson = candlesList.getJSONObject(i);
                            double price_high = coinJson.getDouble("price_high");
                            double price_low = coinJson.getDouble("price_low");
                            double price_open = coinJson.getDouble("price_open");
                            double price_close = coinJson.getDouble("price_close");
                            candles.add(new Candle(price_high, price_low, price_open, price_close));
                            candleEntry.add(new CandleEntry(i+1, (float) price_high, (float) price_low, (float) price_open, (float) price_close));
                        }

                        CoinActivity.setCandlesToBeAdded(candles, candleEntry);
                        Log.d("thread_candle_getter","finish");
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
