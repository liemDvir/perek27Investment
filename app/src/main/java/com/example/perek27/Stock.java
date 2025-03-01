package com.example.perek27;

import java.util.Date;

public class Stock
{
    private float moneyInvested;

    private float amountOfStock;

    private float currentValue;
    private String typeOfStock;

    private Date lastUpdate;

    Stock(String newTypeOfStock, float newMoneyInvested , Date newDate)
    {
        this.moneyInvested = newMoneyInvested;
        this.typeOfStock = newTypeOfStock;
        this.lastUpdate = newDate;
    }

    public Stock(){

    }

    public float getValue() {
        return moneyInvested;
    }
    public void setMoneyInvested(int moneyInvested) {
        this.moneyInvested = moneyInvested;
    }

    public String getTypeOfStock() {
        return typeOfStock;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

}
