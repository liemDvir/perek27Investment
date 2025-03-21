package com.example.perek27;

import java.util.Date;

public class Transaction extends Stock{

    private int mMoneyInvested;
    private Date mTransactionTime;

    public Transaction()
    {

    }
    Transaction(int moneyInvested, String stockName, float amountOfStock, Date transactionTime)
    {
        mMoneyInvested = moneyInvested;
        this.setStockName(stockName);
        this.setAmountOfStock(amountOfStock);
        mTransactionTime = transactionTime;
    }

    public int getMoneyInvested() {
        return mMoneyInvested;
    }
    public void setMoneyInvested(int moneyInvested) {
        this.mMoneyInvested = moneyInvested;
    }

    public Date getTransactionTime() {
        return mTransactionTime;
    }

    public void setmTransactionTime(Date mTransactionTime) {
        this.mTransactionTime = mTransactionTime;
    }


}
