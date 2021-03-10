package com.ce.homework1.model;


public class Candle {

    private double highPrice;
    private double lowPrice;
    private double openPrice;
    private double closePrice;

    public Candle(double highPrice, double lowPrice, double openPrice, double closePrice) {
        setHighPrice(highPrice);
        setLowPrice(lowPrice);
        setOpenPrice(openPrice);
        setClosePrice(closePrice);
    }

    private void setHighPrice(double highPrice) {
        this.highPrice = highPrice;
    }

    private void setLowPrice(double lowPrice) {
        this.lowPrice = lowPrice;
    }

    private void setOpenPrice(double openPrice) {
        this.openPrice = openPrice;
    }

    private void setClosePrice(double closePrice) {
        this.closePrice = closePrice;
    }

    public double getHighPrice() {
        return highPrice;
    }

    public double getLowPrice() {
        return lowPrice;
    }

    public double getOpenPrice() {
        return openPrice;
    }

    public double getClosePrice() {
        return closePrice;
    }
}
