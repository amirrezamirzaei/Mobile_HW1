package com.ce.homework1.model;

import android.graphics.Color;
import android.graphics.ColorSpace;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import com.ce.homework1.R;

public class Coin {
    private String symbol;
    private String name;

    private Integer id;
    private Double price;
    private Double percentChangeHour;
    private Double percentChangeDay;
    private Double percentChangeWeek;

    String imageUrl;

    public Coin(String symbol, String name, Integer id, Double price, Double percentChangeHour, Double percentChangeDay, Double percentChangeWeek) {
        this.symbol = symbol;
        this.name = name;
        this.id = id;
        this.price = price;
        this.percentChangeHour = percentChangeHour;
        this.percentChangeDay = percentChangeDay;
        this.percentChangeWeek = percentChangeWeek;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public Double getPercentChangeHour() {
        return percentChangeHour;
    }

    public Double getPercentChangeDay() {
        return percentChangeDay;
    }

    public Double getPercentChangeWeek() {
        return percentChangeWeek;
    }

    public Double getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getImageUrlLowQuality(){
        return imageUrl.replace("64*64","32*32");
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public static String getColoredSpanned(String text) {
        return "<font color=" + (text.contains("-") ? "#ff7f7f":"#90EE90") + ">" + text + "</font>";
    }

    @Override
    public String toString() {
        return "Coin{" +
                "symbol='" + symbol + '\'' +
                ", name='" + name + '\'' +
                ", id=" + id +
                ", price=" + price +
                ", percentChangeHour=" + percentChangeHour +
                ", percentChangeDay=" + percentChangeDay +
                ", percentChangeWeek=" + percentChangeWeek +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
