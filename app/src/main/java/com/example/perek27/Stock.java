package com.example.perek27;

import java.util.Date;

public class Stock
{
    private float amountOfStock; // כמות המניות שהשקיע

    private String typeOfStock;// שם המניה

    private Date lastUpdate;// התאריך האחרון

    //private float moneyInvested;// כמות הכסף שהלקוח השקיע

    //private float currentValue;// ערך מעודכן למניה אחת

    public Stock(String newTypeOfStock, float newAmountOfStock/*,float newCurrentValue*/,Date newDate)
    {
        this.amountOfStock = newAmountOfStock;
        //this.moneyInvested = currentValue*newAmountOfStock;
        this.typeOfStock = newTypeOfStock;
        this.lastUpdate = newDate;
        //this.currentValue = newCurrentValue;

    }

    public Stock(){

    }
    /*public void setMoneyInvested(int moneyInvested) {
        this.moneyInvested = moneyInvested;
    }*/

    public String getTypeOfStock() {
        return typeOfStock;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public float getAmountOfStock() {
        return amountOfStock;
    }

    /*public float getCurrentValue() {
        return currentValue;
    }*/

    /*public float getMoneyInvested() {
        return moneyInvested;
    }*/
}
