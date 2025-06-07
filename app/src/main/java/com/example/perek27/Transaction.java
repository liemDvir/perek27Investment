package com.example.perek27;

import java.util.Date;

public class Transaction extends Stock{

    private float mNumOfStockInvested;

    private Date mTransactionTime;

    public Transaction()
    {

    }
    Transaction(int moneyInvested, String stockName, float amountOfStock, Date transactionTime)
    {
        mNumOfStockInvested = moneyInvested;
        this.setStockName(stockName);
        this.setAmountOfStock(amountOfStock);
        mTransactionTime = transactionTime;
    }

    public float getMoneyInvested() {
        return mNumOfStockInvested;
    }
    public void setMoneyInvested(int moneyInvested) {
        this.mNumOfStockInvested = moneyInvested;
    }

    public Date getTransactionTime() {
        return mTransactionTime;
    }

    public void setmTransactionTime(Date mTransactionTime) {
        this.mTransactionTime = mTransactionTime;
    }


}
