package com.example.perek27;

public class TransactionHistory {
    private int moneyInvested;
    private String typeOfStock;
    private float amountOfStock;

    private boolean isBuy; // sell is negative(false), and buy is positive(true)

    public TransactionHistory()
    {

    }
    TransactionHistory(int newMoneyInvested, String newTypeOfStock, boolean newIsBuy)
    {
        this.moneyInvested = newMoneyInvested;
        this.typeOfStock = newTypeOfStock;
        this.isBuy =  newIsBuy;

    }

    public int getMoneyInvested() {
        return moneyInvested;
    }
    public void setMoneyInvested(int moneyInvested) {
        this.moneyInvested = moneyInvested;
    }

    public String getTypeOfStock() {
        return typeOfStock;
    }

    public void setTypeOfStock(String typeOfStock) {
        this.typeOfStock = typeOfStock;
    }

    public void getIsBuy(boolean positiveOrNegative) {
        this.isBuy = positiveOrNegative;
    }

    public boolean getIsBuy() {
        return isBuy;
    }
}
