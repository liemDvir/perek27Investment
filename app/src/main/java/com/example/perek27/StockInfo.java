package com.example.perek27;

import java.util.Date;

public class StockInfo extends Stock{

    private float open;
    private float high;
    private float low;
    private float price;
    private Date latest_trading_day;
    private float previous_close;
    private float change;
    private String change_percent;

    public float getOpen() {
        return open;
    }

    public void setOpen(float open) {
        this.open = open;
    }

    public float getHigh() {
        return high;
    }

    public void setHigh(float high) {
        this.high = high;
    }

    public float getLow() {
        return low;
    }

    public void setLow(float low) {
        this.low = low;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Date getLatest_trading_day() {
        return latest_trading_day;
    }

    public void setLatest_trading_day(Date latest_trading_day) {
        this.latest_trading_day = latest_trading_day;
    }

    public float getPrevious_close() {
        return previous_close;
    }

    public void setPrevious_close(float previous_close) {
        this.previous_close = previous_close;
    }

    public float getChange() {
        return change;
    }

    public void setChange(float change) {
        this.change = change;
    }

    public String getChange_percent() {
        return change_percent;
    }

    public void setChange_percent(String change_percent) {
        this.change_percent = change_percent;
    }
}
