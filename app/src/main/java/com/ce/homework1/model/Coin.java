package com.ce.homework1.model;

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

    public Integer getId() {
        return id;
    }
}
