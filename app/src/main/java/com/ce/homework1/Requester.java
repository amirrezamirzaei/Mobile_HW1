package com.ce.homework1;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Requester {
    private static Requester requester;
    private final String COIN_MARKET_API_KEY = "75ec9747-3e6d-4ca9-876f-20c026b960ea";

    private Requester(){}

    public static Requester getInstance(){
        if(requester==null) requester = new Requester();
        return requester;
    }

    public Response RequestCoinName(int start,int limit){
        OkHttpClient okHttpClient = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest?start="+start+"&limit="+limit+"&convert=USD")
                .newBuilder();
        String url = urlBuilder.build().toString();
        System.out.println(url);
        final Request request = new Request.Builder().url(url)
                .addHeader("X-CMC_PRO_API_KEY", COIN_MARKET_API_KEY)
                .build();
        try {
            return okHttpClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Response RequestCoinsLogo(ArrayList<Integer> allCoinId){
        OkHttpClient okHttpClient = new OkHttpClient();
        StringBuilder query = new StringBuilder("");
        for(int i=allCoinId.size()-1 ;i>-1;i--){
            System.out.println(allCoinId.get(i));
            query.append(String.valueOf(allCoinId.get(i)));
            if(i!=0)
                query.append(",");
        }
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://pro-api.coinmarketcap.com/v1/cryptocurrency/info?id=".concat(String.valueOf(query)).concat("&aux=logo"))
                .newBuilder();
        System.out.println(urlBuilder.toString());
        String url = urlBuilder.build().toString();
        final Request request = new Request.Builder().url(url)
                .addHeader("X-CMC_PRO_API_KEY", COIN_MARKET_API_KEY)
                .build();
        try {
            return okHttpClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}
