package com.example.perek27;

public class Stock
{

    private String mStockName;
    private float mAmountOfStock;

    public Stock()
    {

    }
    public Stock(String stockName, float amountOfStock)
    {
        mStockName = stockName;
        mAmountOfStock = amountOfStock;
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

    public void setAmountOfStock(float amountOfStock) {
        this.mAmountOfStock = amountOfStock;
    }

}
