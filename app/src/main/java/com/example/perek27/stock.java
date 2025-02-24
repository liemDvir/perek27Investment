package com.example.perek27;

import java.util.Date;

public class stock
{
    private float moneyInvested;
    private String typeOfStock;

    private Date date;

    stock(String newTypeOfStock,float newMoneyInvested , Date newDate)
    {
        this.moneyInvested = newMoneyInvested;
        this.typeOfStock = newTypeOfStock;
        this.date = newDate;
    }

    public stock(){

    }

    public float getMoneyInvested() {
        return moneyInvested;
    }
    public void setMoneyInvested(int moneyInvested) {
        this.moneyInvested = moneyInvested;
    }

    public String getTypeOfStock() {
        return typeOfStock;
    }

    public Date getDate() {
        return date;
    }
}
