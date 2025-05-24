package com.example.perek27;

public class Stock
{

    private String mStockName;
    private String mStockSymbol;
    private float mAmountOfStock;

    public Stock()
    {

    }
    public Stock(String stockName, String stockSymbol, float amountOfStock)
    {
        mStockName = stockName;
        mAmountOfStock = amountOfStock;
        mStockSymbol = stockSymbol;
    }


    public String getStockName() {
        return mStockName;
    }

    public void setStockName(String stockName) {
        this.mStockName = stockName;
    }

    public float getAmountOfStock() {
        return mAmountOfStock;
    }

    public String getStockSymbol() {
        return mStockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.mStockSymbol = stockSymbol;
    }

    public void setAmountOfStock(float amountOfStock) {
        this.mAmountOfStock = amountOfStock;
    }

}
